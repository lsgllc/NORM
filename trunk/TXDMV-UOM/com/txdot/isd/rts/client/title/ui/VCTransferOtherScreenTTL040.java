package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCTransferOtherScreenTTL040.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 *  Ray Rowehl	01/05/2004	Refer to constants as static in case 
 * 							statements 
 * 							modified processData()
 * 							defect 6731  Ver 5.1.5 fix 2
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.   
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbage.
 * 							defect 7898 Ver 5.2.3    
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * ------------------------------------------------------------------
 */

/**
 * Controller for TTL040 frame.
 *
 * @version: 5.2.3			11/09/2005
 * @author: Administrator
 * <br>Creation Date:		8/22/01 3:50:50 
 */

public class VCTransferOtherScreenTTL040
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	public final static int TEXAS_TRANS = 20;
	public final static int OTHER = 21;

	/**
	 * VCTransferOtherScreenTTL040 constructor comment.
	 */
	public VCTransferOtherScreenTTL040()
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
		return GeneralConstant.TITLE;
	}
	/**
	 * Handles any errors that may occur
	 * 
	 * @param aeRTSEx 
	 * 		com.txdot.isd.rts.client.util.exception.RTSException
	 */
	public void handleError(
		com.txdot.isd.rts.services.exception.RTSException aeRTSEx)
	{
		// Empty code block
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand String the command so the Frame can communicate 
	 * 					with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, java.lang.Object aaData)
	{

		VehicleInquiryData laVehData = new VehicleInquiryData();
		laVehData = (VehicleInquiryData) aaData;

		switch (aiCommand)
		{
			// defect 6731
			// reference constant as static in case statement
			case VCTransferOtherScreenTTL040.TEXAS_TRANS :
				// end defect 6731
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.REG008);
					laVehData.setNoMFRecs(1);
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							laVehData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// 	defect 6731
				// reference constant as static in case statement
			case VCTransferOtherScreenTTL040.OTHER :
				// end defect 6731
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.TTL002);
					laVehData.setNoMFRecs(0);
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							laVehData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							null);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}

		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector
	 * @param asTransCode java.lang.String
	 * @param aaData java.lang.Object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDBox =
				getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmTransferOtherScreenTTL040(laRTSDBox));
			}
			else
			{
				setFrame(new FrmTransferOtherScreenTTL040(
						getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
