package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;

/*
 *
 * VCSupervisorOverrideUpdateSEC017.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		02/25/2005	Make basic RTS 5.2.3 changes
 * 							organize imports, format source
 * 							defect 7891 Ver 5.2.3 
 * --------------------------------------------------------------------- 
 */

/**
 * Controller for Supervisor Override Update screen SEC017 
 * 
 * @version	5.2.3		03/24/2004
 * @author 	Ashish Mahajan
 * <p>Creation Date:	10/5/2001 13:41:52  	  
 */
public class VCSupervisorOverrideUpdateSEC017
	extends AbstractViewController
{
	private static final String TXT_OFC = "OFC";
	private static final String TXT_SUB = "SUB";
	private final static int GET_SUPER_OVERRIDE = 21;

	/**
	 * VCSupervisorOverrideUpdateSEC017 constructor comment.
	 */
	public VCSupervisorOverrideUpdateSEC017()
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
		return GeneralConstant.LOCAL_OPTIONS;
	}

	/**
	 * Handles data coming from their JDialogBox - inside the 
	 * subclasses implementation should be calls to fireRTSEvent() 
	 * to pass the data to the RTSMediator.
	 * 
	 * @param aiCommand int
	 * @param aaData Object  
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							getData());
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
			case ENTER :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.SUPV_OVERIDE,
							aaData);
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case GET_SUPER_OVERRIDE :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					Map laMap = new HashMap();
					laMap.put(
						TXT_OFC,
						new Integer(
							SystemProperty.getOfficeIssuanceNo()));
					laMap.put(
						TXT_SUB,
						new Integer(SystemProperty.getSubStationId()));
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.SUPER_OVERRIDE_LOOKUP,
							laMap);
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
	 * @param asTransCd String The TransCode
	 * @param aaData Object The data object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCd,
		Object aaData)
	{
		if (aaData == null)
		{
			processData(GET_SUPER_OVERRIDE, null);
			return;
		}
		if (getFrame() == null)
		{
			Dialog laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(
					new FrmSupervisorOverrideUpdateSEC017(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmSupervisorOverrideUpdateSEC017(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCd, aaData);
	}
}
