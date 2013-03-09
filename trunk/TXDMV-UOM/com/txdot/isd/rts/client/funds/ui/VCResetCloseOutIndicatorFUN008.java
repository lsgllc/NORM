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
 * VCResetCloseOutIndicatorFUN008.java
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
 * VCCash Drawer Selection Screen FUN008
 * View conrtoller handles the control operation of screens.
 *
 * @version	5.2.3		06/30/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:	09/05/2001 13:30:59
 */

public class VCResetCloseOutIndicatorFUN008
	extends AbstractViewController
{
	/**
	 * VCResetCloseOutIndicatorFUN008 constructor comment.
	 */
	public VCResetCloseOutIndicatorFUN008()
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
	// * @param leRTSEx RTSException
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
			//Go to FUN016	
			case (ENTER) :
			{
				setDirectionFlow(AbstractViewController.NEXT);
				setNextController(ScreenConstant.FUN016);
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
			//Cancel screen, back to desktop
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
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				getFrame().setVisibleRTS(false);
				break;
			}
			//Retrieve drawers from database 	
			case (FundsConstant.GET_CASH_DRAWERS_RESET) :
			{
				setDirectionFlow(AbstractViewController.CURRENT);
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant.GET_CASH_DRAWERS_RESET,
						aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
			}
				//Used to clear desktop when error message is displayed
				//indicating there are no drawers 
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
			//		// empty code block
			//	}
			// end defect 7886
		}
	}
	/**
	 * Retrieves apporpriate data if necessary, displays frame,
	 * and passes control.
	 * 
	 * @param avPreviousControllers java.util.Vector containing the 
	 *   names of the previous controllers 
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
	 */

	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{ 
		//Retrieve records or drawers with indicator set	
		if (aaData == null)
		{
			FundsData laFundsData = new FundsData();
			processData(
				FundsConstant.GET_CASH_DRAWERS_RESET,
				laFundsData);
			return;
		}
		//If no drawers with indicator on, present message
		else if (aaData != null)
		{
			FundsData laFundsData = (FundsData) aaData;
			if (laFundsData.getCashDrawers().size() == 0)
			{
				RTSException InvalidData = new RTSException(694);
				InvalidData.displayError((JDialog) null);
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
				setFrame(new FrmResetCloseOutIndicatorFUN008(
					laRTSDiagBox));
			}
			else
			{
				setFrame(new FrmResetCloseOutIndicatorFUN008(
					getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
