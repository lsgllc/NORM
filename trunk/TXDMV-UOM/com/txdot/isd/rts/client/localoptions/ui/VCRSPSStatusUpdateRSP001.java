package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;

/*
 *
 * VCDealerInformationOPT001.java
 *
 * (c) Texas Department of Transportation 2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		06/24/2004	New class 
 * 							defect 7135 Ver 5.2.1
 * Min Wang		08/20/2004	Additional updates
 *							defect 7135 Ver 5.2.1
 * Min Wang		03/03/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 *							defect 7891  Ver 5.2.3
 * Min Wang 	04/16/2005	remove unused method
 * 							delete handleError()
 * 							defect 7891 Ver 5.2.3
 * --------------------------------------------------------------------- 
 */

/**
 * Controller for screen RSP001. 
 * 
 * @version	5.2.3		04/16/2005
 * @author 	Min Wang
 * <p>Creation Date:	06/24/2004  08:30:59	  
 */
public class VCRSPSStatusUpdateRSP001 extends AbstractViewController
{
	public static final int GET_IDS_LIST = 10;
	/**
	 * VCRSPSStatusUpdateRSP001 constructor comment.
	 */
	public VCRSPSStatusUpdateRSP001()
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
// defect 7891
//	/**
//	 * Handles any errors that may occur
//	 * 
//	 * @param aeRTSEx RTSException
//	 */
//	public void handleError(RTSException aeRTSEx)
//	{
//		// Do we use this?
//	}
// end defect 7891
	/**
	 * Method overriding the base class method Calls the mediator 
	 * process data. Catch RTSException thrown by mediator
	 * 
	 * @param aiCommand int
	 * @return aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			// get the RSPS Ids
			case GET_IDS_LIST :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.GET_RSPS_IDS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}

			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
				}
				break;

			case ENTER :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.GET_RSPS_UPDT,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
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
			if (aaData == null)
			{
				processData(
					VCRSPSStatusUpdateRSP001.GET_IDS_LIST,
					null);
				return;
			}

			Dialog laDialog = getMediator().getParent();
			if (laDialog != null)
			{
				setFrame(new FrmRSPSStatusUpdateRSP001(laDialog));
			}
			else
			{
				setFrame(
					new FrmRSPSStatusUpdateRSP001(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
