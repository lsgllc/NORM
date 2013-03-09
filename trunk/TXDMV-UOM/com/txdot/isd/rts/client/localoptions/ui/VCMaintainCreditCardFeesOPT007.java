package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;

/*
 *
 * VCMaintainCreditCardFeesOPT007.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		02/28/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 *							defect 7891  Ver 5.2.3
 * --------------------------------------------------------------------- 
 */
/**
 * Controller for screen OPT007. 
 * 
 * @version	5.2.3		02/28/2005
 * @author 	Michael Abernethy
 * <br>Creation Date:	04/25/2002 10:45:59	  
 */
public class VCMaintainCreditCardFeesOPT007
	extends AbstractViewController
{
	/**
	 * VCMaintainCreditCardFeesOPT007 constructor comment.
	 */
	public VCMaintainCreditCardFeesOPT007()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return 
	 * their own module name.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.LOCAL_OPTIONS;
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand String  
	 * @param aaData Object  
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					getFrame().setVisibleRTS(false);
					try
					{
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							LocalOptionConstant.UPDATE_CREDIT_DB,
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
					getFrame().setVisibleRTS(false);
					try
					{
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
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
	 * @param asTransCd String
	 * @param aaData Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCd,
		Object aaData)
	{
		if (getFrame() == null)
		{
			Dialog laDialog = getMediator().getParent();
			if (laDialog != null)
			{
				setFrame(new FrmMaintainCreditCardFeesOPT007(laDialog));
			}
			else
			{
				setFrame(
					new FrmMaintainCreditCardFeesOPT007(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCd, aaData);
	}
}
