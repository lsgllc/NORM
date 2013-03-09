package com.txdot.isd.rts.client.funds.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCDateRangeEntriesFUN003.java
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
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */

/**
 * VCCash Drawer Selection Screen FUN003
 * View conrtoller handles the control operation of screens.
 *
 * @version	5.2.4		08/11/2006
 * @author	Bobby Tulsiani
 * <br>Creation Date:	09/05/2001 13:30:59
 */

public class VCDateRangeEntriesFUN003 extends AbstractViewController
{
	/**
	 * VCDateRangeEntriesFUN003 constructor comment.
	 */
	public VCDateRangeEntriesFUN003()
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
			//Go to FUN007	
			case (ENTER) :
				setDirectionFlow(AbstractViewController.PREVIOUS);
				setNextController(ScreenConstant.FUN007);
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
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisibleRTS(false);
				// end 8884
				break;

			//Cancel and goto previsou screen
			case (CANCEL) :
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
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisible(false);
				// end 8884
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
		//if frame has not been displayed	
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDiagBox = getMediator().getParent();
			if (laRTSDiagBox != null)
			{
				setFrame(new FrmDateRangeEntriesFUN003(
					laRTSDiagBox));
			}
			else
			{
				setFrame(new FrmDateRangeEntriesFUN003(
						getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
