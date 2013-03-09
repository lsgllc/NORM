package com.txdot.isd.rts.client.misc.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCSelectReceiptCUS003.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							New class. Ver 5.2.0	
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

public class VCSelectReceiptCUS003
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCSelectReceipt constructor comment.
	 */
	public VCSelectReceiptCUS003()
	{
		super();
	}
	/**
	 * Direct call.
	 * 
	 * @param aiCommand Int
	 * @return aaData java.lang.Object
	 * @exception com.txdot.isd.rts.services.exception.RTSException
	 */
	public Object directCall(int aiCommand, Object aaData)
		throws com.txdot.isd.rts.services.exception.RTSException
	{
		switch (aiCommand)
		{
			case MiscellaneousConstant.GET_LAST_CSN :
				{
					setDirectionFlow(
						AbstractViewController.DIRECT_CALL);
					return getMediator().processData(
						GeneralConstant.MISC,
						aiCommand,
						null);
				}
		}
		return null;
	}
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 * 
	 * @return Int
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
	 * @param aiCommand Int
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.RPR001);
					try
					{
						getMediator().processData(
							getModuleName(),
							MiscellaneousConstant.REPRINT_ONE_RECEIPT,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
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
				}
		}
	}
	/**
	 * Set View
	 * 
	 * @param avPreviousControllers java.util.Vector
	 * @param asTransCode java.lang.String
	 * @param aaData java.lang.Object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDBox = getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmSelectReceiptCUS003(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmSelectReceiptCUS003(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
