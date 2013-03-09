package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * 
 * VCCopyFailureDTA005.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/08/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/23/2005	Change ScreenConstant from DTA008b to DTA009
 * 							modify processData()
 * 							defect 6963,7898 Ver 5.2.3
 * J Rue		03/30/2005	Change ScreenConstant from DTA008b to DTA009
 * 							modify processData()
 * 							defect 6963,7898 Ver 5.2.3
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: Copy Failure DTA005
 * 
 * @version	5.2.3			11/09/2005
 * @author: Ashish Mahajan
 * <br>Creation Date: 		10/31/01 5:40:15
 */

public class VCCopyFailureDTA005
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public static final int MANUAL_ENTRY = 5;
	/**
	 * VCCopyFailureDTA005 constructor.
	 */
	public VCCopyFailureDTA005()
	{
		super();
	}
	/**
	 * Returns the Module name constant used by the RTSMediator to pass 
	 * the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.TITLE;
	}
	/**
	 * Controls the screen flow from DTA005.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int A constant letting the VC know which action 
	 * to perform
	 * @param aaData Object The data from the frame
	 */
	public void processData(int aiCommand, Object aaData)
	{
		try
		{
			switch (aiCommand)
			{
				case ENTER :
				{
					break;
				}
				case CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					getMediator().processData(
						getModuleName(),
						GeneralConstant.NO_DATA_TO_BUSINESS,
						aaData);
					if (getFrame() != null)
					{
						getFrame().setVisibleRTS(false);
					}
					break;
				}
				case MANUAL_ENTRY :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					// defect 6963
					//	Change DTA008b to DTA009
					//setNextController(ScreenConstant.DTA008b);
					setNextController(ScreenConstant.DTA009);
					// end defect 6963
					getMediator().processData(
						getModuleName(),
						GeneralConstant.NO_DATA_TO_BUSINESS,
						aaData);
					if (getFrame() != null)
					{
						getFrame().setVisibleRTS(false);
					}
					break;
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
		catch (Exception  aeEx)
		{
			RTSException leRTSEx = new RTSException(
				CommonConstant.STR_SPACE_EMPTY, aeEx);
			leRTSEx.displayError(getFrame());

		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector A vector containing 
	 * 			the String names of the previous controllers in order
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
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
				setFrame(new FrmCopyFailureDTA005(laRTSDBox));
			}
			else
			{
				setFrame(new FrmCopyFailureDTA005(
					getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
