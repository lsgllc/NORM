package com.txdot.isd.rts.client.help.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 *
 * VCReplyMES002.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		04/03/2007	Created Class.
 * 							defect 7768 Ver Broadcast Message
 * --------------------------------------------------------------------- 
 */

/**
 * Controller for screen FrmReplyMES002.
 * 
 * @version	Broadcast Message	04/03/2007
 * @author 	Jeff Seifert
 * <p>Creation Date:			03/21/2006 14:15:00 	  
 */
public class VCReplyMES002 extends AbstractViewController
{
	/**
	 * VCMessagesMES001 constructor comment.
	 */
	public VCReplyMES002()
	{
		super();
	}
	
	/**
	 * Returns the Module name constant used by the RTSMediator to pass 
	 * the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.HELP;
	}
	
	/**
	 * Method overriding the base class method Calls the mediator 
	 * process data. Catch RTSException thrown by mediator
	 * 
	 * @param aiCommand int
	 * @return aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					close();
					try
					{
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			default :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					try
					{
						getMediator().processData(
							getModuleName(),
							aiCommand,
							aaData);
						close();
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
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
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
			Dialog laDialog = getMediator().getParent();
			if (laDialog != null)
			{
				setFrame(new FrmReplyMES002(laDialog));
			}
			else
			{
				setFrame(
					new FrmReplyMES002(getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
