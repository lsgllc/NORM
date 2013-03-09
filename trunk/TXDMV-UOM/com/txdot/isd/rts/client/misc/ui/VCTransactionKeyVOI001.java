package com.txdot.isd.rts.client.misc.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCTransactionKeyVOI001.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Zwiener	03/17/2005	Java 1.4
 *							defect 7892 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */

/**
 * VCCash Drawer Selection Screen VOI001
 * View conrtoller handles the control operation of screens.
 *
 * @version	5.2.4 			08/11/2006
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class VCTransactionKeyVOI001
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCTransactionKeyVOI001 constructor comment.
	 */
	public VCTransactionKeyVOI001()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 */
	public int getModuleName()
	{
		return GeneralConstant.MISC;
	}
	/**
	 * Handles any errors that may occur
	 * 
	 * @param aeRTSEx RTSException
	 */
	public void handleError(RTSException aeRTSEx)
	{
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the
	 * RTSMediator.
	 * 
	 * @param aiCommand Int
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{ //Retrieve transaction, and goto VOI002	
			case (ENTER) :
				setDirectionFlow(AbstractViewController.NEXT);
				setNextController(ScreenConstant.VOI002);
				try
				{
					getMediator().processData(
						getModuleName(),
						MiscellaneousConstant.GET_VOID_TRANSACTION,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
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
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
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
	 * @param aaPreviousControllers Vector
	 * @param asTransCode String
	 */

	public void setView(
		Vector aaPreviousControllers,
		String asTransCode,
		Object aaData)
	{ //if frame has not been displayed
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDBox = getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmTransactionKeyVOI001(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmTransactionKeyVOI001(
						getMediator().getDesktop()));
			}
		}
		super.setView(aaPreviousControllers, asTransCode, aaData);
	}
}
