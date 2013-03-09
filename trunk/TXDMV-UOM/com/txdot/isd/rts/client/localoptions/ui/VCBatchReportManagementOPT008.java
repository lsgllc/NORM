package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;

/*
 * VCBatchReportManagementOPT008.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/14/2011	Created
 * 							defect 10701 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * VC for FrmBatchReportManagementOPT008
 *
 * @version	6.7.0 			01/14/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/14/2011 18:27:17
 */
public class VCBatchReportManagementOPT008
	extends AbstractViewController
{
	public static final int REVISE = 5;

	/**
	 * VCBatchReportManagementOPT008.java Constructor
	 * 
	 */
	public VCBatchReportManagementOPT008()
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
		return GeneralConstant.LOCAL_OPTIONS;
	}
	/**
	 * Handles data coming from their JDialogBox - inside the subclasses 
	 * implementation should be calls to fireRTSEvent() to pass the data 
	 * to the RTSMediator.
	 * 
	 * @param aiCommand int
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		try
		{
			switch (aiCommand)
			{
				case CANCEL :
					{
						setDirectionFlow(AbstractViewController.FINAL);
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
						getFrame().setVisibleRTS(false);
						break;
					}
				case REVISE :
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.UPDATE_BATCH_RPT_MGMT,
							aaData);
						break;
					}
				case SEARCH :
					{
						setData(aaData);
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.GET_BATCH_RPT_MGMT,
							aaData);
						break;
					}
			}
		}
		catch (RTSException leRTSEx)
		{
			leRTSEx.displayError(getFrame());
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
			Dialog laDialog = getMediator().getParent();
			if (laDialog != null)
			{
				setFrame(new FrmBatchReportManagementOPT008(laDialog));
			}
			else
			{
				setFrame(
					new FrmBatchReportManagementOPT008(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
