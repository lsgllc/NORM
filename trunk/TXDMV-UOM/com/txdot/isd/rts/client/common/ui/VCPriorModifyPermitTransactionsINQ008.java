package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 * VCPriorModifyPermitTransactionsINQ008.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/18/2011	Created
 * 							defect 10844 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * VC for presentation of Prior Modify Permit Transactions
 *
 * @version	6.8.0			06/18/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		06/18/2011 14:48:17 
 */
public class VCPriorModifyPermitTransactionsINQ008
	extends AbstractViewController
{

	/**
	 * VCPriorModifyPermitTransactionsINQ008.java Constructor
	 * 
	 */
	public VCPriorModifyPermitTransactionsINQ008()
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
			setFrame(
				new FrmPriorModifyPermitTransactionsINQ008(laRTSDB));
		}
		else
		{
			setFrame(
				new FrmPriorModifyPermitTransactionsINQ008(
					getMediator().getDesktop()));
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
