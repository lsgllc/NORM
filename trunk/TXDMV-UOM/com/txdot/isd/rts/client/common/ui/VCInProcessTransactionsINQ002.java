package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCInProcessTransactionsINQ002.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/17/2010	Created 
 * 							defect 10598 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: FrmInProcessTransactionsINQ002
 *
 * @version	6.6.0 		09/17/2010
 * @author	Kathy Harrell
 * <br>Creation Date:	09/17/2010 19:15:17 
 */
public class VCInProcessTransactionsINQ002
	extends AbstractViewController
{

	/**
	 * VCInProcessTransactionsINQ002.java Constructor
	 * 
	 */
	public VCInProcessTransactionsINQ002()
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
		return GeneralConstant.COMMON;
	}

	/**
	 * All subclasses must override this method to handle data 
	 * coming from their JDialogBox - inside the subclasses 
	 * implementation should be calls to fireRTSEvent() to pass
	 * the data to the RTSMediator.
	 * 
	 * @param aiCommand int 
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);

					if (UtilityMethods.isDTA(getTransCode()))
					{
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						setPreviousController(ScreenConstant.DTA008);
					}
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					close();
					break;
				}
			case AbstractViewController.ENTER :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					close();
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
		}
	}

	/**
	 *  Send the data to the frame.
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
		RTSDialogBox laRTSDB = getMediator().getParent();
		if (laRTSDB != null)
		{
			setFrame(new FrmInProcessTransactionsINQ002(laRTSDB));
		}
		else
		{
			setFrame(
				new FrmInProcessTransactionsINQ002(
					getMediator().getDesktop()));
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
