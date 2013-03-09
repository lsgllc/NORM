package com.txdot.isd.rts.client.reports.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCSalesTaxInquiryKEY003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Duggirala 	09/07/2001	Added comments  
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3 
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify processData()
 *							defect 7896 Ver 5.2.3	
 * ---------------------------------------------------------------------
 */
/**
 * VCSalesTaxInquiryKEY003
 * 
 * @version	5.2.3			05/31/2005
 * @author	Rakesh Duggirala
 * <br>Creation Date:		07/03/2001 08:27:41
 */
public class VCSalesTaxInquiryKEY003 extends AbstractViewController
{
	/**
	 * ControllerACC018 constructor
	 */
	public VCSalesTaxInquiryKEY003()
	{
		super();
	}
	
	/**
	 * getModuleName
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
	 * @param aiCommand int the command so the Frame can communicate
	 * 	with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					this.setData(aaData);
					setNextController(ScreenConstant.RPR000);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							ReportConstant.GENERATE_SALES_TAX_REPORT,
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
					try
					{
						getMediator().processData(
							getModuleName(),
							ReportConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					// defect 7590
					// changed setVisible to setVisibleRTS
					getFrame().setVisibleRTS(false);
					// end defect 7590
					break;
				}
			case HELP :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							ReportConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
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
				setFrame(new FrmSalesTaxInquiryKEY003(laRD));
			}
			else
			{
				setFrame(
					new FrmSalesTaxInquiryKEY003(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
