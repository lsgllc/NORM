package com.txdot.isd.rts.client.funds.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 *
 * VCReportGenerationStatusFUN013.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	03/03/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 * 							defect 7886 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * VCCash Drawer Selection Screen FUN013
 * View conrtoller handles the control operation of screens.
 *
 * @version	5.2.3		06/30/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:	09/05/2001 13:30:59
 */

public class VCReportGenerationStatusFUN013
	extends AbstractViewController
{
	/**
	 * VCReportGenerationStatusFUN013 constructor comment.
	 */
	public VCReportGenerationStatusFUN013()
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
			//Close all screens	
			case (ENTER) :
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
			}
		}

	}
	/**
	 * Stores variables, displays frame, and passes control.
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
		//if frame has not been displayed	
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDiagBox = getMediator().getParent();
			if (laRTSDiagBox != null)
			{
				setFrame(new FrmReportGenerationStatusFUN013(
					laRTSDiagBox));
			}
			else
			{
				setFrame(new FrmReportGenerationStatusFUN013(
						getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
