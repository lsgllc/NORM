package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCSpecialRegistrationREG002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/08/2005	Change package for Transaction
 * 							modify processData()
 * 							defect 7705 Ver 5.2.3
 * Ray Rowehl	03/05/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * B Hargrove	04/26/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * View Controller for Screen: Special Registration REG002
 * 
 * @version	5.2.3		04/26/2005
 * @author	Joseph Peters
 * <br>Creation Date:	10/27/2001 17:16:26
 */
public class VCSpecialRegistrationREG002 extends AbstractViewController
{
	public static final int INQ007 = 80;
	/**
	 * VCSpecialRegistrationREG002 constructor.
	 */
	public VCSpecialRegistrationREG002()
	{
		super();
	}
	/**
	 * Returns the Module name constant used by the RTSMediator to 
	 * pass the data to the appropriate Business Layer class.
	 * 
	 * @return String
	 */
	public int getModuleName()
	{
		return GeneralConstant.COMMON;
	}
	/**
	 * Controls the screen flow from REG002.  It passes the data to 
	 * the RTSMediator.
	 * 
	 * @param aiCommand int Let the VC know which action to perform
	 * @param aaData Object The data from the frame
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER:
			{
				// defect 7705
				// change package for Transaction
				if (Transaction
					.getTransactionHeaderData()
					== null)
				{
					setDirectionFlow(AbstractViewController.FINAL);
				}
				else
				{
					setDirectionFlow(
						AbstractViewController.DESKTOP);
				}
				// end defect 7705

				try
				{
					getMediator().processData(
						getModuleName(),
						CommonConstant.ADD_TRANS,
						aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				getFrame().setVisibleRTS(false);
				break;
			}
			case INQ007:
			{
				setNextController(ScreenConstant.INQ007);
				setDirectionFlow(AbstractViewController.NEXT);
				try
				{
					getMediator().processData(
						getModuleName(),
						CommonConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				getFrame().setVisibleRTS(false);
				break;
			}
			case AbstractViewController.CANCEL:
			{
				setDirectionFlow(AbstractViewController.FINAL);
				try
				{
					getMediator().processData(
						getModuleName(),
						CommonConstant.NO_DATA_TO_BUSINESS,
						null);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				getFrame().setVisibleRTS(false);
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
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmSpecialRegistrationREG002(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmSpecialRegistrationREG002(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
