package com.txdot.isd.rts.client.common.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.ProcessInventoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCInventoryOverrideInventoryItemINV005.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		02/03/2003  Prevent NullPointerException.
 * 							modify processData()
 * 							defect 5129
 * Min Wang		02/14/2003	Exception Handling
 * 							modify handleReturnCode(), processData()
 * 							defect 5365
 * Ray Rowehl	07/29/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The VC for the INV005 screen
 * 
 * @version	5.2.3		07/29/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	10/25/2001 13:59:17
 */

public class VCInventoryOverrideInventoryItemINV005
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public final static int VALIDATION_SUCCESSFUL =
		VCInventoryItemNumberInputINV001.VALIDATION_SUCCESSFUL;
	public final static int VALIDATION_FAILED =
		VCInventoryItemNumberInputINV001.VALIDATION_FAILED;
	public final static int ALLOCATION_FAILED =
		VCInventoryItemNumberInputINV001.ALLOCATION_FAILED;
	public final static int ALREADY_ISSUED =
		VCInventoryItemNumberInputINV001.ALREADY_ISSUED;
		
	private final static String EXCEPTION = "EXCEPTION";
	private final static String DATA = "DATA";
	private final static String INV_DATA = "INV_DATA";
	private final static String OLD_DATA = "OLD_DATA";
	private final static String RET_CODE = "RETURN_CODE";
	
	/**
	 * VCInventoryOverrideInventoryItemINV005 constructor comment.
	 */
	public VCInventoryOverrideInventoryItemINV005()
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
		return com
			.txdot
			.isd
			.rts
			.services
			.util
			.constants
			.GeneralConstant
			.COMMON;
	}
	/**
	 * Handle Return Codes.
	 * 
	 * @param aaMap Map
	 */
	private void handleReturnCode(Map aaMap)
	{
		int liReturnCode =
			((Integer) aaMap.get(RET_CODE)).intValue();
		//defect 5365
		RTSDialogBox laRTSDB = null;
		if (getFrame().isVisible())
		{
			laRTSDB = getFrame();
		}
		else
		{
			laRTSDB = getMediator().getParent();
		}
		//end defect 5365	
		switch (liReturnCode)
		{
			case ALLOCATION_FAILED :
				{
					processData(ALLOCATION_FAILED, aaMap);
					break;
				}

			case VALIDATION_FAILED :
				{
					RTSException leRTSExMsg =
						(RTSException) aaMap.get(EXCEPTION);
					if (leRTSExMsg.getCode() == 593)
					{
						leRTSExMsg.setCode(594);
					}
					leRTSExMsg.displayError(laRTSDB);
					break;
				}

			case VALIDATION_SUCCESSFUL :
				{
					if (aaMap.get(EXCEPTION) != null)
					{
						RTSException leRTSExMsg =
							(RTSException) aaMap.get(EXCEPTION);
						leRTSExMsg.displayError(laRTSDB);
					}
					processData(
						VALIDATION_SUCCESSFUL,
						aaMap.get(DATA));
					break;
				}

			case ALREADY_ISSUED :
				{
					RTSException leRTSExMsg =
						(RTSException) aaMap.get(EXCEPTION);
					int liYesNo = leRTSExMsg.displayError(laRTSDB);
					if (liYesNo == RTSException.YES)
					{
						processData(
							VALIDATION_SUCCESSFUL,
							aaMap.get(DATA));
					}
					else
					{
						ProcessInventoryData laPInvData =
							(ProcessInventoryData) aaMap.get(INV_DATA);
						CompleteTransactionData laTransData =
							(CompleteTransactionData) aaMap.get(DATA);
						laTransData.getInvItms().remove(laPInvData);
						laTransData.getAllocInvItms().remove(
							laPInvData);
						laTransData.getInvItms().add(
							aaMap.get(OLD_DATA));
						laTransData.getAllocInvItms().add(
							aaMap.get(OLD_DATA));
					}
					break;
				}
		}
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
		//defect5365
		RTSDialogBox laRTSDB = null;
		if (getFrame() != null && getFrame().isVisible())
		{
			laRTSDB = getFrame();
		}
		else
			laRTSDB = getMediator().getParent();
		//end defect5365
		switch (aiCommand)
		{
			case ENTER :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.VALIDATE,
							aaData);
					}
					catch (RTSException leRTSEX)
					{
						leRTSEX.displayError(laRTSDB);
					}
					break;
				}

			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						//defect5129
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
							laRTSDB = getMediator().getParent();
						}
						//end defect5129
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(laRTSDB);
					}
					break;
				}

			case VALIDATION_SUCCESSFUL :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					try
					{ //defect5129
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
							laRTSDB = getMediator().getParent();
						}
						//end defect5129
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(laRTSDB);
					}
					break;
				}

			case ALLOCATION_FAILED :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.INV029);
					try
					{
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(laRTSDB);
					}
				}
		}
	}
	/**
	 * Send data to frame.
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
			RTSDialogBox leRTSDB = getMediator().getParent();
			if (leRTSDB != null)
			{
				setFrame(
					new FrmInventoryOverrideInventoryItemINV005(leRTSDB));
			}
			else
			{
				setFrame(
					new FrmInventoryOverrideInventoryItemINV005(
						getMediator().getDesktop()));
			}
		}
		HashMap laMap = (HashMap) aaData;
		if (laMap != null && laMap.get(RET_CODE) != null)
		{
			handleReturnCode(laMap);
			return;
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
