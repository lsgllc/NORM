package com.txdot.isd.rts.client.misc.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousConstant;

/*
 *
 * VCPrintSupervisorOverrideCUS004.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/19/2004	JavaDoc Cleanup.  
 * 							Ver 5.2.0
 * J Zwiener	03/17/2005	Java 1.4
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

public class VCPrintSupervisorOverrideCUS004
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCPrintSupervisorOverrideCUS004 constructor comment.
	 */
	public VCPrintSupervisorOverrideCUS004()
	{
		super();
	}
	/**
	 * directCall
	 * 
	 * @param aiCommand Int
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException
	 */
	public Object directCall(int aiCommand, Object aaData)
		throws RTSException
	{
		switch (aiCommand)
		{
			case MiscellaneousConstant.OVERRIDE_NEEDED :
				{
					setDirectionFlow(
						AbstractViewController.DIRECT_CALL);
					return getMediator().processData(
						GeneralConstant.MISC,
						aiCommand,
						aaData);
				}
		}
		return null;
	}
	/**
	 * All subclasses must override this method to return their own
	 * module name.
	 * 
	 * @return String
	 */
	public int getModuleName()
	{
		return GeneralConstant.MISC;
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
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					// defect 8884
					// use close() so that it does setVisibleRTS()
					close();
					//getFrame().setVisible(false);
					// end 8884
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
					break;
				}
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
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
				}
		}
	}
	/**
	 * Set View checks if frame = null, set the screen and pass control to the 
	 * controller.
	 * 
	 * @param avPreviousControllers Vector
	 * @param asTransCd String
	 * @param aaData controller.AbstractDataObject
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCd,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDBox = getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(
					new FrmPrintSupervisorOverrideCUS004(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmPrintSupervisorOverrideCUS004(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCd, aaData);
	}
}
