package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 *
 * VCCreditPMT003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	03/17/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * K Harrell	12/27/2005	Modification to support "ESC" processing
 * 							modify processData()
 * 							defect 8486 Ver 5.2.3  
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: Credit PMT003
 * 
 * @version	5.2.3		12/27/2005
 * @author	Nancy Ting
 * <br>Creation Date:	09/13/2001 14:01:42
 */
public class VCCreditPMT003 extends AbstractViewController
{
	/**
	 * VCCreditPMT003 constructor comment.
	 */
	public VCCreditPMT003()
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
		return GeneralConstant.COMMON;
	}
	/**
	 * Controls the screen flow from PMT003.  It passes the data to the
	 * RTSMediator.
	 * 
	 * @param aiCommand int
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		// defect 8486 
		// Add test for CANCEL 
		if (aiCommand == AbstractViewController.ENTER
			|| aiCommand == AbstractViewController.CANCEL)
		// end defect 8486	
		{
			setDirectionFlow(AbstractViewController.PREVIOUS);

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
			getFrame().setVisibleRTS(false);
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
				setFrame(new FrmCreditPMT003(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmCreditPMT003(getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
