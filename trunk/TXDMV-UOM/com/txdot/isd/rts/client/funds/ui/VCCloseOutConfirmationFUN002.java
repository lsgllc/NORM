package com.txdot.isd.rts.client.funds.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCCloseOutConfirmationFUN002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	07/12/2005	Modify code for move to Java 1.4.
 *							modify for WSAD. Bring code to standards.
 * 	 						Use new getters\setters for frame, 
 *							controller, etc. Remove any unused methods,
 *							variables, arguments, imports.
 * 							Chg '/**' to '/*' to begin prolog.
 * 							defect 7886 Ver 5.2.3
  * K Harrell	10/24/2005	Consistent verbiage for Closeout
 * 							defect 8379 Ver 5.2.3            
 * ---------------------------------------------------------------------
 */

/**
 * VCCash Drawer Selection Screen FUN002
 * View conrtoller handles the control operation of screens.
 *
 * @version	5.2.3		10/24/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:	09/05/2001 13:30:59
 */

public class VCCloseOutConfirmationFUN002
	extends AbstractViewController
{
	/**
	 * VCCloseOutConfirmationFUN002 constructor comment.
	 */
	public VCCloseOutConfirmationFUN002()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their
	 * own module name=FUNDS
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.FUNDS;
	}
	
	/**
	 * Handle the user actions.  Upon pressing "Enter", the control
	 * should be passed to screen FUN015, and no report is generated.
	 * Cancel and Help destroy screen or provide appropriate help.
	 * 
	 * @param aiCommand int
	 * @param aaData Object 
	 */

	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{ 
			//Proceed to screen FUN015	
			case (ENTER) :
				this.setData(aaData);
				setDirectionFlow(AbstractViewController.NEXT);
				setNextController(ScreenConstant.FUN015);
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant.CLOSE_OUT_FOR_DAY,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;

				//Go back to screen FUN001 if workstation is a server
			case (CANCEL) :
				this.setData(aaData);
				setDirectionFlow(AbstractViewController.PREVIOUS);
				setPreviousController(ScreenConstant.FUN001);
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				getFrame().setVisibleRTS(false);
				break;

				//Close all screens, if workstation was not a server	
			case (FINAL) :
				setDirectionFlow(AbstractViewController.FINAL);
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				getFrame().setVisibleRTS(false);
				break;
			default :
			{
				// empty code block
			}
			
		}

	}
	/**
	 * Stores variables, displays frame, and passes control.
	 *
	 * @param avPreviousControllers java.util.Vector containing the 
	 *   names of the previous controllers 
	 * @param asTransCode String The TransCode
	 * @param aaData Object  
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
				setFrame(new FrmCloseOutConfirmationFUN002(
					laRTSDiagBox));
			}
			else
			{
				setFrame(new FrmCloseOutConfirmationFUN002(
						getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);

	}
}
