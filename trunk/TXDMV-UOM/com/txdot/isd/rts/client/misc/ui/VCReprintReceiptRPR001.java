package com.txdot.isd.rts.client.misc.ui;

import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCReprintReceiptRPR001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.	
 * 							add directCall()
 * 							remove handleError()
 * 							Ver 5.2.0	
 * J Zwiener	03/17/2005	Java 1.4
 *							defect 7892 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * K Harrell	09/16/2008	Pass text for Reason Supervisor Override 
 * 							Code is required.
 * 							modify processData()
 * 							defect 7283 Ver Defect_POS_B 
 * ---------------------------------------------------------------------
 */

/**
 * View controller handles the control operation of screens.
 *
 * @version	Defect_POS_B	09/16/2008
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class VCReprintReceiptRPR001
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	/**
	 * VCReprintReceiptRPR001 constructor comment.
	 */
	public VCReprintReceiptRPR001()
	{
		super();
	}

	/**
	 * directCall.
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
	 * module name
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
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							MiscellaneousConstant.PRINT_RECEIPTS,
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
					close();
					break;
				}
			case MiscellaneousConstant.OVERRIDE_NEEDED :
				{
					try
					{
						// defect 7283 
						// Extract Reason for presentation on CTL004
						Map laMap = (Map) aaData;
						String lsReason =
							(String) laMap.get(
								MiscellaneousConstant.MAP_ENTRY_MSG);
						aaData =
							(Vector) laMap.get(
								MiscellaneousConstant.MAP_ENTRY_DATA);

						this.setData(aaData);
						setDirectionFlow(NEXT);
						setNextController(ScreenConstant.CUS004);
						getMediator().processData(
							getModuleName(),
							MiscellaneousConstant.NO_DATA_TO_BUSINESS,
							lsReason);
						// end defect 7283 
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
				}
		}
	}

	/**
	 * Set View checks if frame = null, set the screen and pass control
	 * to the controller.
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
		if (aaData instanceof Boolean)
		{
			if (((Boolean) aaData).booleanValue() == true)
			{
				processData(ENTER, this.getData());
			}
			return;
		}

		if (getFrame() == null)
		{
			RTSDialogBox laRTSDBox = getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmReprintReceiptRPR001(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmReprintReceiptRPR001(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCd, aaData);
	}
}
