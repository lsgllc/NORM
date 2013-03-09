package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

/*
 *
 * VCItemNumberNotFoundINV003.java 
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							modify processData()
 * 							Ver 5.2.0	
 * Ray Rowehl	03/16/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * K Harrell	05/02/2005	Rename from INV014
 * 							defect 6966 Ver 5.2.3   
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for VCItemNumberNotFoundINV003
 * 
 * @version	5.2.3		05/02/2005
 * @author 	Nancy Ting
 * <br>Creation Date:	10/05/2001
 */

public class VCItemNumberNotFoundINV003
	extends AbstractViewController
{
	/**
	 * VCItemNumberNotFoundINV003 constructor comment.
	 */
	public VCItemNumberNotFoundINV003()
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
		return GeneralConstant
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
			case ENTER:
			{
				setDirectionFlow(AbstractViewController.PREVIOUS);
				try
				{
					getFrame().setVisibleRTS(false);
					// PCR 34
					//mediator.processData(					
					//getModuleName(),
					//com.txdot.isd.rts.services.util.constants.
					// CommonConstant.HOLD_INV014_ITM,
					//data);
					getMediator().processData(
						GeneralConstant
							.REGISTRATION,
						RegistrationConstant
							.PROCESS_SUBCON_RENWL,
						aaData);
					// End PCR 34
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
					return;
				}
			}
			break;
			case CANCEL:
			{
				setDirectionFlow(AbstractViewController.CANCEL);
				try
				{
					getFrame().setVisibleRTS(false);
					//PCR 34
					getMediator().processData(
					// getModuleName(),
					// com.txdot.isd.rts.services.util.constants.
					//  CommonConstant.NO_DATA_TO_BUSINESS,
					GeneralConstant
						.REGISTRATION,
						RegistrationConstant
							.RELEASE_INV003_ITM,
					// End PCR 34 
					aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
					return;
				}
			}
			break;
		}
	}
	/**
	 * Send data to frame.
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
				setFrame(new FrmItemNumberNotFoundINV003(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmItemNumberNotFoundINV003(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
