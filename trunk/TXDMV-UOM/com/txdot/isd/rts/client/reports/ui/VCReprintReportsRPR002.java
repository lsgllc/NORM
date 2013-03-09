package com.txdot.isd.rts.client.reports.ui;

import java.awt.Dialog;
import java.io.File;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCReprintReportsRPR002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3 
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify processData()
 *							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * View Controller for Screen: Reprint Reports RPR002
 * 
 * @version	5.2.3			05/31/2005
 * @author	Administrator
 * <br>Creation Date:		08/22/2001 09:44:05
 */
public class VCReprintReportsRPR002 extends AbstractViewController
{
	public static final int DISPLAY_REPORTS = 10;
	public static final int PRINT_REPORTS = 11;

	/**
	 * VCReprintReportsRPR002 constructor 
	 */
	public VCReprintReportsRPR002()
	{
		super();
	}

	/**
	 * Returns the module name
	 * All subclasses must override this method to return their own
	 * module name
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.REPORTS;
	}

	/**
	 * Handles any errors that may occur
	 * 
	 * @param leRTSEx RTSException
	 */
	public void handleError(RTSException leRTSEx)
	{
		// empty code block
	}

	/**
	 * Handle data coming from JDialogBox 
	 * 
	 * @param aiCommand int
	 * @param aaData Object the data object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			//Handle request for displaying or printing reports
			case DISPLAY_REPORTS :
				{
					this.setData(aaData);
					setNextController(ScreenConstant.RPR000);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							ReportConstant.GENERATE_REPRINT_REPORT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case PRINT_REPORTS :
				{
					this.setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							ReportConstant.PRINT_REPRINT_REPORT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case (CANCEL) :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						getMediator().processData(
							getModuleName(),
							FundsConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						// empty code block
					}
					// defect 7590
					// changed setVisible to setVisibleRTS
					getFrame().setVisibleRTS(false);
					// end defect 7590
					break;
				}
		}
	}
	
	/**
	 * Set View checks if frame = null, set the screen and pass control
	 * to the controller.
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
			Dialog laRD = getMediator().getParent();
			if (laRD != null)
			{
				setFrame(new FrmReprintReportsRPR002(laRD));
			}
			else
			{
				setFrame(
					new FrmReprintReportsRPR002(
						getMediator().getDesktop()));
			}
		}
		File caRptFilesDir =
			new File(SystemProperty.getReportsDirectory());
		File[] carrRptFiles = caRptFilesDir.listFiles();
		if (carrRptFiles.length == 0)
		{
			RTSException leRTSEx = new RTSException(389);
			leRTSEx.displayError(getFrame());
			processData(CANCEL, null);
			return;
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}