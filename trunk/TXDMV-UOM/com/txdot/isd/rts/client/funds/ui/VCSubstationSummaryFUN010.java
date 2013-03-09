package com.txdot.isd.rts.client.funds.ui;

import javax.swing.JDialog;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCSubstationSummaryFUN010.java
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
 * VCCash Drawer Selection Screen FUN010
 * View conrtoller handles the control operation of screens.
 *
 * @version	5.2.3		06/30/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:	09/05/2001 13:30:59
 */

public class VCSubstationSummaryFUN010 extends AbstractViewController
{
	/**
	 * VCSubstationSummaryFUN010 constructor comment.
	 */
	public VCSubstationSummaryFUN010()
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
			//Go to report viewer	
			case (ENTER) :
			{
				setDirectionFlow(AbstractViewController.NEXT);
				setNextController(ScreenConstant.RPR000);
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant
							.GENERATE_SUBSTATION_SUMMARY_REPORT,
						aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				break;
			}
			//Close all screens
			case (CANCEL) :
			{
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
			}
			//Get list of substaion closeouts from DB
			case (FundsConstant.GET_CASH_DRAWERS_SUBSTATION) :
			{
				setDirectionFlow(AbstractViewController.CURRENT);
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant.GET_CASH_DRAWERS_SUBSTATION,
						aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				break;
			}
				//Used to clear desktop when exception dialog box is
				//presented
			case (FundsConstant.CLEAR_DESKTOP) :
			{
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
				break;
			}
			// defect 7886
			// Help is launched from the frame
			//case (HELP) :
			//	{
			//		//empty code block
			//	}
			// end defect 7886
		}
	}
	/**
	 * Retrieves apporpriate data if necessary, displays frame,
	 * and passes control.
	 * 
	 * @param avPreviousControllers java.util.Vector containing the 
	 * names of the previous controllers 
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
	 */

	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{ 
		//Retrieve all substation closeouts from db	
		if (aaData == null)
		{
			FundsData laFundsData = new FundsData();
			processData(
				FundsConstant.GET_CASH_DRAWERS_SUBSTATION,
				laFundsData);
			return;
		}

		//If no closeouts, display exception dialog saying there are
		//none
		else if (aaData != null)
		{
			FundsData laFundsData = (FundsData) aaData;
			if (laFundsData.getCashDrawers().size() == 0)
			{
				RTSException leRTSExInvalidData = new RTSException(684);
				leRTSExInvalidData.displayError((JDialog) null);
				processData(FundsConstant.CLEAR_DESKTOP, laFundsData);
				return;
			}
		}

		//if frame has not been displayed	
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDiagBox = getMediator().getParent();
			if (laRTSDiagBox != null)
			{
				setFrame(new FrmSubstationSummaryFUN010(
					laRTSDiagBox));
			}
			else
			{
				setFrame(new FrmSubstationSummaryFUN010(
					getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
