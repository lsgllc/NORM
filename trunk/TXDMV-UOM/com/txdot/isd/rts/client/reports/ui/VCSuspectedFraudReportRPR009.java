package com.txdot.isd.rts.client.reports.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCSuspectedFraudReportRPR009.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/03/2011	Created 
 * 							defect 10900 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: RPR009
 *
 * @version	6.8.0 			06/03/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		06/03/2011 14:38:17
 */
public class VCSuspectedFraudReportRPR009
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int EXPORT_DATA = 8;
	public static final int DISPLAY_REPORT = 9;

	/**
	 * VCSuspectedFraudReportRPR009.java Constructor
	 * 
	 */
	public VCSuspectedFraudReportRPR009()
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
				case EXPORT_DATA :
					{
						setData(aaData);
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							ReportConstant.GENERATE_FRAUD_EXPORT,
							aaData);
						break;
					}
				case DISPLAY_REPORT :
					{
						setData(aaData);
						setNextController(ScreenConstant.RPR000);
						setDirectionFlow(AbstractViewController.NEXT);
						try
						{
							getMediator().processData(
								getModuleName(),
								ReportConstant.GENERATE_FRAUD_REPORT,
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
				setFrame(new FrmSuspectedFraudReportRPR009(leRTSDB));
			}
			else
			{
				setFrame(
					new FrmSuspectedFraudReportRPR009(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
