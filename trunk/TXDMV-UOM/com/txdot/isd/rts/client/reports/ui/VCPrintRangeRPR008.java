package com.txdot.isd.rts.client.reports.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 * VCPrintRangeRPR008.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/15/2009	Created
 * 							defect 10086 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: RPR008
 *
 * @version	Defect_POS_F	06/15/2009
 * @author	kharre-c
 * <br>Creation Date:		06/15/2009
 */
public class VCPrintRangeRPR008
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	/**
	 * VCPrintRangeRPR008 constructor comment.
	 */
	public VCPrintRangeRPR008()
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
		return GeneralConstant.REPORTS;
	}

	/**
	 * Controls the screen flow from RPR008. It passes the data to 
	 * the RTSMediator.
	 * 
	 * @param aiCommand int  
	 * @param aaData Object  
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			// Intentionally dropping through
			case AbstractViewController.ENTER :
			case AbstractViewController.CANCEL :
				{
					close();
					setDirectionFlow(AbstractViewController.CANCEL);
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
					break;
				}
			default :
				{
					// empty code block
				}
		}
	}

	/**
	 * Set View checks if frame = null, sets the screen and passes 
	 * control to the controller.
	 *
	 * @param avPreviousControllers Vector
	 * @param asTransCode String  
	 * @param aaData Object the data
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox leRTSDB = getMediator().getParent();
			if (leRTSDB != null)
			{
				setFrame(new FrmPrintRangeRPR008(leRTSDB));
			}
			else
			{
				setFrame(
					new FrmPrintRangeRPR008(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
