package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCDisabledPlacardReportMRG027.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/21/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for FrmDisabledPlacardReportMRG027
 *
 * @version	POS_Defect_B	10/21/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		10/21/2008
 */

public class VCDisabledPlacardReportMRG027
	extends AbstractViewController
{
	/**
	 * VCDisabledPlacardReportMRG027 constructor comment.
	 */
	public VCDisabledPlacardReportMRG027()
	{
		super();
	}

	/**
	 * All subclasses must override this method to return their own
	 * module name.
	 * @return String
	 */
	public int getModuleName()
	{
		return GeneralConstant.MISCELLANEOUSREG;
	}

	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the RTSMediator.
	 * 
	 * @param aiCommand int command so the Frame can communicate with VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setData(aaData);
					setNextController(ScreenConstant.RPR000);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							MiscellaneousRegConstant
								.GENERATE_DSABLD_PLCRD_REPORT,
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
					setDirectionFlow(AbstractViewController.FINAL);
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
					close();
					break;
				}
		}
	}

	/**
	 * setView
	 * 
	 * @param Vector avPreviousControllers
	 * @param String asTransCode
	 * @param Object aaData
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDiagBox = getMediator().getParent();
			if (laRTSDiagBox != null)
				setFrame(
					new FrmDisabledPlacardReportMRG027(laRTSDiagBox));
			else
				setFrame(
					new FrmDisabledPlacardReportMRG027(
						getMediator().getDesktop()));
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
