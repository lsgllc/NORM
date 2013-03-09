package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.InventoryInquiryUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCInventoryInquiryCriteriaINV021.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/26/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * Ray Rowehl	07/18/2005	Constants work.
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * K Harrell	05/07/2007	use SystemProperty.getOfficeIssuanceCd() 
 *     	   	 				modify processData()
 *     	   	 				defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV021 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	Special Plates	05/07/2007
 * @author	Charlie Walker
 * <br>Creation Date:		11/20/2001 22:19:45
 */

public class VCInventoryInquiryCriteriaINV021
	extends AbstractViewController
{

	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int RETRIEVE_SUBSTATION_DATA = 15;

	/**
	 * Flag that shows if the substation data has been retrieved from
	 * the db
	 */
	private boolean cbRcvdSubstaData = false;

	/**
	 * Flag that shows if INV004 has been initialized
	 */
	private boolean cbInit = false;

	/**
	 * InventoryInquiryUIData object used to hold the UI data
	 */
	private InventoryInquiryUIData caInvInqUIData =
		new InventoryInquiryUIData();

	/**
	 * VCAllocateFromLocationINV021 constructor comment.
	 */
	public VCInventoryInquiryCriteriaINV021()
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
		return GeneralConstant.INVENTORY;
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
			case AbstractViewController.ENTER :
				{
					int liFuncId = 0;
					if (!((InventoryInquiryUIData) aaData)
						.getInvInqBy()
						.equals(InventoryConstant.ITM_TYPES)
						&& !((InventoryInquiryUIData) aaData)
							.getInvInqBy()
							.equals(
							InventoryConstant.CNTRL))
					{
						setNextController(ScreenConstant.INV027);
						liFuncId =
							InventoryConstant
								.RETRIEVE_INQUIRY_ENTITY_DATA;
					}
					else
					{
						Vector lvDataOut =
							new Vector(CommonConstant.ELEMENT_3);
						lvDataOut.add(aaData);
						lvDataOut.add(new Vector());

						// Store the OfcIssuanceCd. Used in determining 
						// which item codes to display on INV022.
						// defect 9085 
						// Use SystemProperty.getOfficeIssuanceCd() 
						// OfficeIdsData laOfcIds =
						//		OfficeIdsCache.getOfcId(
						//		SystemProperty.getOfficeIssuanceNo());
						GeneralSearchData laGSD =
							new GeneralSearchData();
						//laGSD.setIntKey1(laOfcIds.getOfcIssuanceCd());
						laGSD.setIntKey1(
							SystemProperty.getOfficeIssuanceCd());
						// end defect 9085 

						lvDataOut.add(laGSD);

						aaData = lvDataOut;
						setNextController(ScreenConstant.INV022);
						liFuncId =
							InventoryConstant
								.RETRIEVE_INVENTORY_ITEMS_FOR_INQUIRY;
					}
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							liFuncId,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.PREVIOUS);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
			case RETRIEVE_SUBSTATION_DATA :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.RETRIEVE_SUBSTATION_DATA,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					return;
				}
		}
	}

	/**
	 * Directs flow to the frame.
	 * If frame has not been instantiated, create it.
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
		if (!cbRcvdSubstaData)
		{
			caInvInqUIData = (InventoryInquiryUIData) aaData;
			GeneralSearchData laGSData = new GeneralSearchData();
			laGSData.setIntKey1(SystemProperty.getOfficeIssuanceNo());
			laGSData.setIntKey2(SystemProperty.getSubStationId());
			cbRcvdSubstaData = true;

			processData(RETRIEVE_SUBSTATION_DATA, laGSData);
			return;
		}
		else if (!cbInit)
		{
			Vector lvDataIn = new Vector();
			lvDataIn.addElement(aaData);
			lvDataIn.addElement(caInvInqUIData);

			if (getFrame() == null)
			{
				RTSDialogBox laRTSDB = getMediator().getParent();
				if (laRTSDB != null)
				{
					setFrame(
						new FrmInventoryInquiryCriteriaINV021(laRTSDB));
				}
				else
				{
					setFrame(
						new FrmInventoryInquiryCriteriaINV021(
							getMediator().getDesktop()));
				}
			}
			cbInit = true;
			super.setView(avPreviousControllers, asTransCode, lvDataIn);
		}
		else if (cbInit)
		{
			return;
		}
	}
}
