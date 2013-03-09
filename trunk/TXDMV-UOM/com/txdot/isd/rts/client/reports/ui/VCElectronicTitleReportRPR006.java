package com.txdot.isd.rts.client.reports.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCElectronicTitleReportRPR006.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/26/2009	Created 
 * 							defect 9972 Ver Defect_POS_E 
 * ---------------------------------------------------------------------
 */

/**
* The View Controller for the RPR006 screen.  It handles screen 
* navigation and controls the visibility of it's frame.
*
* @version	Ver Defect_POS_E	02/26/2009
* @author	Kathy Harrell
* <br>Creation Date:			02/26/2009
**/

public class VCElectronicTitleReportRPR006
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	

	/**
	 * VCElectronicTitleReportRPR006 constructor comment.
	 */
	public VCElectronicTitleReportRPR006()
	{
		super();
	}

	/**
	 * All subclasses must override this method to return their own
	 * module name.
	 * @return java.lang.String
	 */
	public int getModuleName()
	{
		return GeneralConstant.REPORTS;
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
		try
		{
			switch (aiCommand)
			{
				case ENTER :
					{
						setDirectionFlow(AbstractViewController.NEXT);
						setNextController(ScreenConstant.RPR007);
						getMediator().processData(
							getModuleName(),
							ReportConstant.NO_DATA_TO_BUSINESS,
							aaData);
						break; 
					}
				case CANCEL :
					{
						setDirectionFlow(AbstractViewController.FINAL);
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
		catch (Exception aeEx)
		{
			RTSException leRTSEx2 =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSEx2.displayError(getFrame());
		}
	}

	/**
	 * Set View checks if frame = null, sets the screen and passes 
	 * control to the controller.
	 *
	 * @param avPreviousControllers Vector
	 * @param asTransCode String  
	 * @param aaData Object the data
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
				setFrame(new FrmElectronicTitleReportRPR006(leRTSDB));
			}
			else
			{
				setFrame(
					new FrmElectronicTitleReportRPR006(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
