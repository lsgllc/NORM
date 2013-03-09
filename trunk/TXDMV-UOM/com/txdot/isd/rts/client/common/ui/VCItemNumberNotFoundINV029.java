package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * VCItemNumberNotFoundINV029.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	03/16/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The VC for INV029
 * 
 * @version	5.2.3		03/16/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	10/25/2001 14:01:34
 */
public class VCItemNumberNotFoundINV029
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCItemNumberNotFoundINV029 constructor comment.
	 */
	public VCItemNumberNotFoundINV029()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return com
			.txdot
			.isd
			.rts
			.services
			.util
			.constants
			.GeneralConstant
			.COMMON;
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
					try
					{
						getFrame().setVisibleRTS(false);
						getMediator().processData(
							getModuleName(),
							CommonConstant.PUT_ON_HOLD,
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
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						getFrame().setVisibleRTS(false);
						getMediator().processData(
							getModuleName(),
							CommonConstant.TAKE_OFF_HOLD_TEMP,
							((FrmItemNumberNotFoundINV029) getFrame())
								.getCompleteTransactionData());
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
		}
	}
	/**
	 * Send the data to the frame.
	 * 
	 * @param avPreviousControllers Vector
	 * @param asTransCode String
	 * @param aaData Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmItemNumberNotFoundINV029(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmItemNumberNotFoundINV029(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
