package com.txdot.isd.rts.client.misc.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousConstant;

/*
 *
 * VCPrintDestinationCTL009.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Zwiener	03/01/2005	Java 1.4
 *							defect 7892 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */

/**
 * View controller handles the control operation of screens.
 *
 * @version	5.2.4 			08/11/2006
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class VCPrintDestinationCTL009
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCPrintDestinationCTL009 constructor comment.
	 */
	public VCPrintDestinationCTL009()
	{
		super();
	}
	/**
	 * Return module name.
	 *
	 * @return Int
	 */
	public int getModuleName()
	{
		return GeneralConstant.MISC;
	}
	/**
	 * Handles any errors that may occur
	 * 
	 * @param leRTSEx com.txdot.isd.rts.client.util.exception.RTSException
	 */
	public void handleError(
		com.txdot.isd.rts.services.exception.RTSException leRTSEx)
	{
		// empty block of code
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
	public void processData(int aiCommand, java.lang.Object aaData)
	{
		switch (aiCommand)
		{ //Close all screens, and update print destination	
			case (ENTER) :
				setDirectionFlow(AbstractViewController.FINAL);
				try
				{
					getMediator().processData(
						getModuleName(),
						MiscellaneousConstant.NO_DATA_TO_BUSINESS,
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
						MiscellaneousConstant.NO_DATA_TO_BUSINESS,
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

			case (HELP) :
				{
				}
		}
	}
	/**
	 * Set View checks if frame = null, set the screen and pass control
	 * to the controller.
	 * 
	 * @param avPreviousControllers Vector
	 * @param asTransCd String
	 * @param aaData Object
	 */

	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCd,
		Object aaData)
	{ //if frame has not been displayed
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDBox = getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmPrintDestinationCTL009(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmPrintDestinationCTL009(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCd, aaData);
	}
}
