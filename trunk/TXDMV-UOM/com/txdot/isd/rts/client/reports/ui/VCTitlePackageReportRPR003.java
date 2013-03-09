package com.txdot.isd.rts.client.reports.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCTitlePackageReportRPR003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Johnston	03/08/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify processData()
 *							defect 7896 Ver 5.2.3	  
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3 
 * S Johnston	05/31/2005	Added displayError to empty catch
 * 							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * VCTitlePackageReportRPR003
 * 
 * @version	5.2.3			05/31/2005
 * @author	Administrator
 * <br>Creation Date:		08/22/2001 09:44:05
 */
public class VCTitlePackageReportRPR003 extends AbstractViewController
{
	/**
	 * VCDEmployeeSelectionFUN011 constructor
	 */
	public VCTitlePackageReportRPR003()
	{
		super();
	}
	
	/**
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
	 * All subclasses must override this method to handle data coming
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the
	 * RTSMediator
	 * 
	 * @param aiCommand int the command so the Frame can communicate
	 * 	with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{ //if user selects enter, pass control to screen FUN015
			case ENTER :
				{
					this.setData(aaData);
					setNextController(ScreenConstant.RPR000);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							ReportConstant
								.GENERATE_TITLE_PACKAGE_REPORT,
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
							ReportConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						// defect 7896
						// added displayError to empty catch block
						aeRTSEx.displayError(getFrame());
						// end defect 7896
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
		//if frame has not been displayed	
		if (getFrame() == null)
		{
			Dialog laRD = getMediator().getParent();
			if (laRD != null)
			{
				setFrame(new FrmTitlePackageReportRPR003(laRD));
			}
			else
			{
				setFrame(
					new FrmTitlePackageReportRPR003(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}