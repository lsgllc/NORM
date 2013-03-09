package com.txdot.isd.rts.client.reports.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCExemptAuditReportRPR005.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Ralph		10/18/2006	New Class - Exempts
 * J Ralph		10/25/2006  Redefined EXPORT_DATA, DISPLAY_REPORT per
 * 							Walkthru
 * 							defect 8900 Ver Exempts
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the RPR005 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	Exempts		10/25/2006
 * @author	John Ralph
 * <br>Creation date:	10/18/2006 10:23:10
 */

public class VCExemptAuditReportRPR005
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int EXPORT_DATA = 8;
	public static final int DISPLAY_REPORT = 9;
	
	/**
	 * VCExemptAuditReportRPR005 constructor comment.
	 */
	public VCExemptAuditReportRPR005()
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
				case EXPORT_DATA :
					{
						setData(aaData);
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							ReportConstant.GENERATE_EXEMPT_AUDIT_EXPORT,
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
								ReportConstant.GENERATE_EXEMPT_AUDIT_REPORT,
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
				setFrame(new FrmExemptAuditReportRPR005(leRTSDB));
			}
			else
			{
				setFrame(
					new FrmExemptAuditReportRPR005(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}

