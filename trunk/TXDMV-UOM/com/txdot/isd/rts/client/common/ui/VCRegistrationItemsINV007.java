package com.txdot.isd.rts.client.common.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.ProcessInventoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCRegistrationItemsINV007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/22/2002	Added Internet code in case it's not sticker 
 *							defect 4041
 * MAbs			05/31/2002	Showed exception if reg month changed 
 *							defect 4132
 * S Govindappa	08/06/202	Added a boolean graph variable and made
 *							changes to setView method to prevent error 
 *							572 from being displayed multiple times.
 *							defect 4566. 
 * S Govindappa 08/06/2002	Fixed defect 4566 for IRenew.
 * Min Wang    	07/02/2003  Fixed defect 6101. Show exception if data is 
 *							null.
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 *							add cvStickers, STICKER_PRINTING
 *							modify import statements
 * 							modify processData(),setView()
 * 							Ver 5.2.0
 * K Harrell	04/13/2004	Added logic to complete transaction for HQ
 *							modify setView()
 *							Ver 5.2.0
 * K Harrell	11/03/2004	Call mediator with no data to process for
 *							Sticker Printing & Plate
 *							renamed from WRITE_INV_TO_CACHE to
 *							 ADD_TRANS_INV_NO_PYMNT
 *							modify processData()
 *							defect 6720  Ver 5.2.2 
 * Ray Rowehl	04/13/2005	Remove sticker confirmation path.
 * 							delete STICKER
 * 							modify processData()
 * 							defect 7954 Ver 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3 
 * Ray Rowehl	07/29/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * Jeff S.		08/16/2005	Renamed CP003 to REG103
 * 							modify setView()
 * 							defect 7889 Ver 5.2.3
 * K Harrell	09/19/2005	Would not prompt for nth item when n>1 && 
 * 							only 1 inventory item allocated
 * 							modify setView()  
 * 							defect 8350 Ver 5.2.3
 * Jeff S.		06/22/2006	Used screen constant for CTL001 Title.
 * 							remove CONFIRM_ACTION_TITLE_STR
 * 							modify setView()
 * 							defect 8756 Ver 5.2.3
 * T Pederson	10/13/2006	Added case for finalizing and posting
 * 							transaction for driver ed exempts.
 * 							modify processData()
 * 							defect 8900 Ver Exempts
 * K Harrell	10/17/2006	use ADD_TRANS vs. POST_TRANS in above
 * 							modify processData()
 * 							defect 8900 Ver Exempts
 * K Harrell	04/26/2007	Use SystemProperty.isHQ(),
 * 							CommonConstant.TXT_COMPLETE_TRANS_QUESTION
 * 							modify setView()
 * 							defect 9085 Ver Special Plates 		
 * ---------------------------------------------------------------------
 */

/**
 * The VC for INV007
 * 
 * @version	Special Plates	04/26/2007
 * @author	Michael Abernethy
 * <br>Creation Date:		10/25/2001 
 */

public class VCRegistrationItemsINV007
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	// boolean
	private boolean cbError572DispIndi = false;

	// Vector
	private Vector cvStickers;
	private Vector lvInvToAllocate;

	// Constants
	private static final String ALLOC = "ALLOC";
	private static final String CANCEL_TXT = "CANCEL";
	private static final String DATA = "DATA";
	private static final String ERRMSG_SYSERR =
		"System Error - Internal Data Problem";

	public final static int OVERRIDE = 20;
	private final static int ALLOCATE_INVENTORY = 21;
	public final static int PLATE = 24;
	public final static int HQ = 25;
	public final static int INTERNET = 26;
	private final static int STICKER_PRINTING = 28;

	// defect 8900
	public final static int ISSDRVED = 29;
	// end defect 8900

	/**
	 * VCRegistrationItemsINV007 constructor comment.
	 */
	public VCRegistrationItemsINV007()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own
	 * module name.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.COMMON;
	}
	/**
	 * All subclasses must override this method to handle data coming
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case OVERRIDE :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.INV005);
					try
					{
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case PLATE :
				{
					setData(aaData);
					((CompleteTransactionData) aaData).setStickers(
						cvStickers);
					((CompleteTransactionData) aaData)
						.getAllocInvItms()
						.addAll(
						cvStickers);
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.PMT004);
					try
					{

						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case STICKER_PRINTING :
				{
					setData(aaData);
					((CompleteTransactionData) aaData).setStickers(
						cvStickers);
					((CompleteTransactionData) aaData)
						.getAllocInvItms()
						.addAll(
						cvStickers);
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.PMT004);
					try
					{
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);

						getMediator().hardCancel();
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case HQ :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					((CompleteTransactionData) aaData)
						.getAllocInvItms()
						.addAll(
						cvStickers);
					((CompleteTransactionData) aaData).setStickers(
						cvStickers);
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.ADD_TRANS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case CANCEL :
				{
					if (getData() instanceof CompleteTransactionData)
					{
						setTransCode(
							((CompleteTransactionData) getData())
								.getTransCode());
					}
					if (getTransCode().equals(TransCdConstant.DTANTD)
						|| getTransCode().equals(TransCdConstant.DTANTK)
						|| getTransCode().equals(TransCdConstant.DTAORD)
						|| getTransCode().equals(TransCdConstant.DTAORK))
					{
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						setPreviousController(ScreenConstant.TTL008);
					}
					else
					{
						setDirectionFlow(AbstractViewController.CANCEL);
					}
					try
					{
						// Take everything off of hold that was put on
						// hold in this process.  The 2 differences 
						// are a result of anomalies in DTA processing
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
						if (aaData instanceof Map)
						{
							getMediator().processData(
								getModuleName(),
								CommonConstant.TAKE_OFF_HOLD,
								((Map) aaData).get(DATA));
						}
						else
						{
							getMediator().processData(
								getModuleName(),
								CommonConstant.TAKE_OFF_HOLD,
								(
									(FrmRegistrationItemsINV007) getFrame())
									.getData());
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case ALLOCATE_INVENTORY :
				{
					Map laMap = new HashMap();
					laMap.put(DATA, aaData);
					laMap.put(ALLOC, lvInvToAllocate);
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.INV001);
					try
					{
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							laMap);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case INTERNET :
				{
					// PCR 34
					((CompleteTransactionData) aaData)
						.getAllocInvItms()
						.addAll(
						cvStickers);
					((CompleteTransactionData) aaData).setStickers(
						cvStickers);
					// End PCR 34
					setDirectionFlow(AbstractViewController.PREVIOUS);
					CompleteTransactionData laCTD =
						(CompleteTransactionData) aaData;
					setPreviousController(laCTD.getInternetScreen());
					try
					{
						getFrame().setVisibleRTS(false);
						getMediator().processData(
							GeneralConstant.COMMON,
							CommonConstant.ADD_TRANS_IRENEW,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// defect 8900 
			case ISSDRVED :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					((CompleteTransactionData) aaData)
						.getAllocInvItms()
						.addAll(
						cvStickers);
					((CompleteTransactionData) aaData).setStickers(
						cvStickers);
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.ADD_TRANS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// end defect 8900 
		}
	}
	/**
	 * Move the data to the frame.
	 * Create the frame if it has not been created.
	 * 
	 * @param avPreviousControllers Vector
	 * @param asTransCode String
	 * @param aaData Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmRegistrationItemsINV007(laRTSDB, true));
			}
			else
			{
				setFrame(
					new FrmRegistrationItemsINV007(
						getMediator().getDesktop()));
			}
		}
		// defect 6101
		// Show exception if data is null.
		if (aaData == null)
		{
			RTSException leRTSExMsg = new RTSException(ERRMSG_SYSERR);
			leRTSExMsg.displayError(getMediator().getParent());
			processData(CANCEL, aaData);
			return;
		}
		// end defect 6101
		// If coming back INV001 and cancel was pressed
		if (aaData instanceof Map
			&& ((Map) aaData).get(CANCEL_TXT) != null)
		{
			processData(CANCEL, aaData);
			return;
		}
		setData(aaData);
		CompleteTransactionData laTransData =
			(CompleteTransactionData) aaData;
		// defect 4132
		if (laTransData.getVehicleInfo() != null
			&& laTransData.getVehicleInfo().getRegData() != null
			&& laTransData.getOrgVehicleInfo() != null
			&& laTransData.getOrgVehicleInfo().getRegData() != null)
		{
			if (laTransData.getVehicleInfo().getRegData().getRegExpMo()
				!= laTransData
					.getOrgVehicleInfo()
					.getRegData()
					.getRegExpMo())
			{
				if (laTransData
					.getTransCode()
					.equals(TransCdConstant.RENEW)
					|| laTransData.getTransCode().equals(
						TransCdConstant.EXCH)
					|| laTransData.getTransCode().equals(
						TransCdConstant.TOWP))
				{
					if (avPreviousControllers
						.get(avPreviousControllers.size() - 1)
						.equals(ScreenConstant.REG003)
						&& !cbError572DispIndi)
					{
						cbError572DispIndi = true;
						RTSException leRTSEx = new RTSException(572);
						leRTSEx.displayError(getMediator().getParent());
					}
				}
				else if (
					laTransData.getTransCode().equals(
						TransCdConstant.IRENEW))
				{
					// defect 7889
					// Renamed CP003 to REG103
					if (avPreviousControllers
						.get(avPreviousControllers.size() - 1)
						.equals(ScreenConstant.REG103)
						&& !cbError572DispIndi)
						// end defect 7889
					{
						cbError572DispIndi = true;
						RTSException leRTSEx = new RTSException(572);
						leRTSEx.displayError(getMediator().getParent());
					}
				}
			}
		}
		// end defect 4132
		// PCR 34
		if (cvStickers == null)
		{
			cvStickers = new Vector();
			int i = 0;
			while (i < laTransData.getInvItms().size())
			{
				ProcessInventoryData laInvData =
					(ProcessInventoryData) laTransData
						.getInvItms()
						.get(
						i);
				if (StickerPrintingUtilities
					.isStickerPrintable(laInvData))
				{
					cvStickers.add(laInvData);
					laTransData.getInvItms().remove(laInvData);
					continue;
				}
				i++;
			}
			if (laTransData.getInvItms().size() == 0)
			{
				if (laTransData
					.getTransCode()
					.equals(TransCdConstant.IRENEW))
				{
					processData(INTERNET, laTransData);
				}
				else
				{
					// defect 9085 
					//int liOfcNo = SystemProperty.getOfficeIssuanceNo();
					//OfficeIdsData laOfficeIdsData =
					//	OfficeIdsCache.getOfcId(liOfcNo);
					//if (laOfficeIdsData.getOfcIssuanceCd() == 1)
					if (SystemProperty.isHQ())
					{
						// defect 8756
						// Used common constant for CTL001 title
						RTSException leRTSEx =
							new RTSException(
								RTSException.CTL001,
								CommonConstant.TXT_COMPLETE_TRANS_QUESTION,
								ScreenConstant.CTL001_FRM_TITLE);
						// end defect 8756
						int liResponse =
							leRTSEx.displayError(
								getMediator().getParent());
						if (liResponse == RTSException.YES)
						{
							processData(HQ, laTransData);
						}
						else
						{
							return;
						}
					}
					else
					{
						processData(STICKER_PRINTING, laTransData);
					}
					// end defect 9085 
				}
				return;
			}
		}
		// End PCR 34
		if (lvInvToAllocate == null)
		{
			// defect 8350 
			// Create HashSet for those no longer available 
			HashSet lhsUnavailInv = new HashSet();

			// create Vector of ProcessInventoryData that needs to be 
			// allocated
			lvInvToAllocate = new Vector();
			for (int i = 0; i < laTransData.getInvItms().size(); i++)
			{
				boolean lbAlreadyAllocated = false;
				ProcessInventoryData laTempData =
					(ProcessInventoryData) laTransData
						.getInvItms()
						.get(
						i);

				for (int j = 0;
					j < laTransData.getAllocInvItms().size();
					j++)
				{
					ProcessInventoryData laAllocData =
						(ProcessInventoryData) laTransData
							.getAllocInvItms()
							.get(
							j);
					if (laTempData
						.getItmCd()
						.equals(laAllocData.getItmCd())
						&& !lhsUnavailInv.contains(laAllocData))
					{
						lbAlreadyAllocated = true;
						// add laAllocData as no longer available  
						lhsUnavailInv.add(laAllocData);
						break;
					}
				}
				// end defect 8350 
				if (!lbAlreadyAllocated)
				{
					lvInvToAllocate.add(laTempData);
				}

			}
		}
		// If inventory needs to be allocated, show INV001
		if (lvInvToAllocate.size() > 0)
		{
			processData(ALLOCATE_INVENTORY, aaData);
			return;
		}
		// If all the inventory has been allocated, show INV007
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
