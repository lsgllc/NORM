package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 * VCPaymentPMT001.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/11/2002	Added Beep
 * J Rue		12/09/2004	Determine if diskette is in the A:\\ drive
 *							This is required that DTAORD and DTANTD
 *							Update Prolog and JavaDoc
 *							add import 
 *							com.txdot.isd.rts.services.util.constants
 *							com.txdot.isd.rts.services.util
 *							add DISKERROR
 *							add diskInADriveDTA()
 *							modify processData()
 *							defect 7736 Ver 5.2.2
 * J Rue		12/15/2004	Change conditional while (true) to not break
 *							from loop.
 *							defect 7736 Ver 5.2.2
 * K Harrell	12/16/2004	Formatting/JavaDoc/Variable Name Cleanup
 *							defect 7736 Ver 5.2.2 
 * J Rue		07/15/2005	Remove changes for defect 7736
 * 							modify processData()
 * 							delete diskInADriveDTA()
 * 							defect 8227 Ver 5.2.3
 * Ray Rowehl	07/29/2005	RTS 5.2.3 cleanup.
 * 							format source, organize imports, 
 * 							some field names
 * 							defect 7885 Ver 5.2.3
 * T Pederson	10/21/2005	RTS 5.2.3 cleanup.
 * 							Removed case for help from processData. 
 * 							modify processData()
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * View Controller for Screen: Payment PMT001
 *
 * @version	5.2.3			10/21/2005
 * @author	Nancy Ting
 * <br>Creation Date:		09/13/2001 14:01:42 
 */
public class VCPaymentPMT001 extends AbstractViewController
{
	/**
	 * VCPaymentPMT001 constructor comment.
	 */
	public VCPaymentPMT001()
	{
		super();
	}
	/**
	 * Returns the Module name constant used by the RTSMediator to 
	 * pass the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.COMMON;
	}
	/**
	 * Controls the screen flow from PMT001.  
	 * It passes the data to the RTSMediator.
	 * 
	 * @param aiCommand int  
	 * @param aaData Object 
	 */

	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				{
					setDirectionFlow(AbstractViewController.DESKTOP);

					getFrame().setVisibleRTS(false);
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.END_TRANS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}

					break;
				}
			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.DESKTOP);
					getFrame().setVisibleRTS(false);
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							null);
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
	 * Creates the actual frame, stores the protected variables needed by 
	 * the VC, and sends the data to the frame.
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
			RTSDialogBox laRd = getMediator().getParent();
			if (laRd != null)
			{
				setFrame(new FrmPaymentPMT001(laRd));
			}
			else
			{
				setFrame(
					new FrmPaymentPMT001(getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCd, aaData);
	}
}