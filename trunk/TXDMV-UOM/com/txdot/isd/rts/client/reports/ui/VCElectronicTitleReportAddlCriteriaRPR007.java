package com.txdot.isd.rts.client.reports.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCElectronicTitleReportAddlCriteriaRPR007.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/20/2009	Created
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	03/26/2009	Added FINAL processing
 * 							modify processData()
 * 							defect 9972 Ver Defect_POS_E
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the RPR007 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	Defect_POS_E	03/26/2009
 * @author	Kathy Harrell 
 * <br>Creation Date:		03/20/2009 
 */
public class VCElectronicTitleReportAddlCriteriaRPR007
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	public static final int EXPORT_DATA = 8;
	public static final int DISPLAY_REPORT = 9;

	/**
	 * VCElectronicTitleReportAddlCriteriaRPR007 constructor comment.
	 */
	public VCElectronicTitleReportAddlCriteriaRPR007()
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
							ReportConstant.GENERATE_ETITLE_EXPORT,
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
								ReportConstant.GENERATE_ETITLE_REPORT,
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
						setDirectionFlow(AbstractViewController.CANCEL);
						close();
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							null);
						break;
					}
				case FINAL :
					{
						setDirectionFlow(AbstractViewController.FINAL);
						close(); 
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							null);
						break;
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
				setFrame(
					new FrmElectronicTitleReportAddlCriteriaRPR007(leRTSDB));
			}
			else
			{
				setFrame(
					new FrmElectronicTitleReportAddlCriteriaRPR007(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
