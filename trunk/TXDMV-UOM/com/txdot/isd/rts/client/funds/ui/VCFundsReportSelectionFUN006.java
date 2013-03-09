package com.txdot.isd.rts.client.funds.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCFundsReportSelectionFUN006.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	06/30/2005	Modify code for move to Java 1.4.
 *							modify for WSAD. Bring code to standards.
 * 	 						Use new getters\setters for frame, 
 *							controller, etc. Remove any unused methods,
 *							variables, arguments, imports.
 * 							Chg '/**' to '/*' to begin prolog.
 * 							defect 7886 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * VCCash Drawer Selection Screen FUN006
 * View conrtoller handles the control operation of screens.
 *
 * @version	5.2.3		06/30/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:	09/05/2001 13:30:59
 */

public class VCFundsReportSelectionFUN006
	extends AbstractViewController
{
	/**
	 * VCFundsReportSelectionFUN006 constructor comment.
	 */
	public VCFundsReportSelectionFUN006()
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
		return GeneralConstant.FUNDS;
	}
	
	// defect 7886
	// remove unused method
	///**
	// * Handles any errors that may occur
	// * 
	// * @param RTSException leRTSEx
	// */
	//public void handleError(
	//	com.txdot.isd.rts.services.exception.RTSException leRTSEx)
	//{
	//}
	// end defect 7886
	
	/**
	 * All subclasses must override this method to handle data coming
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the
	 * RTSMediator
	 *
	 * @param aiCommand int
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
		    //Goto FUN011 if entity is employee, or FUN001 if cashdrawer	
			case (ENTER) :
				this.setData(aaData);
				setDirectionFlow(AbstractViewController.NEXT);
				if (((FundsData) aaData).getFundsReportData().
					getEntity()	== FundsConstant.EMPLOYEE)
				{
					setNextController(ScreenConstant.FUN011);
				}
				else
				{
					setNextController(ScreenConstant.FUN001);
				}
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				break;

			//Close all screens 		
			case (CANCEL) :
				setDirectionFlow(AbstractViewController.FINAL);
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				getFrame().setVisibleRTS(false);
				break;

			// defect 7886
			// Help is launched from the frame
			//case (HELP) :
			//	{
			//	}
			// end defect 7886
		}
	}
	/**
	 * Stores variables, displays frame, and passes control.
	 * 
	 * @param avpreviousControllers java.util.Vector containing the 
	 *   names of the previous controllers 
	 * @param astransCode String The TransCode
	 * @param aadata Object The data object
	 */

	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		//if frame has not been displayed
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDiagBox = getMediator().getParent();
			if (laRTSDiagBox != null)
			{
				setFrame(new FrmFundsReportSelectionFUN006(
					laRTSDiagBox));
			}
			else
			{
				setFrame(new FrmFundsReportSelectionFUN006(
						getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);

	}
}
