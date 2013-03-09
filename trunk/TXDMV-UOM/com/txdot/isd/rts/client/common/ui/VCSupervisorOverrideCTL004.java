package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * VCSupervisorOverrideCTL004.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	03/05/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * B Hargrove	04/26/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/20/2005	deleted ununsed variables. Reorganized
 * 							imports 
 * 							defect 7885 Ver 5.2.3
 * K Harrell	09/11/2008	Data restored on TTL008 after CTL004.
 * 							"PREVIOUS" returns through setView() 
 * 							Modifying to Cancel vs. Enter on Title && DTA
 * 							type transactions only to limit scope.  
 * 							modify processData() 
 * 							defect 9807 Ver Defect_POS_B 
 * K Harrell	09/11/2008	Data restored on TTL008 after CTL004.
 * 							"PREVIOUS" returns through setView() 
 * 							Modifying to Cancel vs. Enter on Title or DTA
 * 							type transactions only to limit scope.  
 * 							modify processData() 
 * 							defect 9807 Ver MyPlates_POS
  * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: Supervisor Override CTL004
 * 
 * @version	Defect_POS_B	09/11/2008
 * @author	Nancy Ting
 * <br>Creation Date:		10/05/2001 10:56:30
 */

public class VCSupervisorOverrideCTL004
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	/**
	 * VCSupervisorOverrideCTL004 constructor comment.
	 */
	public VCSupervisorOverrideCTL004()
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
		return com
			.txdot
			.isd
			.rts
			.services
			.util
			.constants
			.GeneralConstant
			.COMMON;
	}

	/**
	 * Controls the screen flow from CTL004.  It passes the data to 
	 * the RTSMediator. 
	 * 
	 * @param aiCommand int 
	 * @param aaData Object  
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					// defect 9807
					// Use CANCEL on Title & DTA Events vs. PREVIOUS
					// setDirectionFlow(AbstractViewController.PREVIOUS);
					String lsEventType =
						UtilityMethods.getEventType(getTransCode());

					int liDirectionFlow =
						(lsEventType
							.equals(TransCdConstant.TTL_EVENT_TYPE)
							|| lsEventType.equals(
								TransCdConstant.DTA_EVENT_TYPE))
							? AbstractViewController.CANCEL
							: AbstractViewController.PREVIOUS;

					setDirectionFlow(liDirectionFlow);
					// end defect 9807


					try
					{
						getMediator().processData(
							getModuleName(),
							com
								.txdot
								.isd
								.rts
								.services
								.util
								.constants
								.CommonConstant
								.GET_MISC_DATA,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						(
							(FrmSupervisorOverrideCTL004) getFrame())
								.displayError(
							aeRTSEx);
						return;
					}
					getFrame().setVisibleRTS(false);

				}
				break;
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						getMediator().processData(
							getModuleName(),
							com
								.txdot
								.isd
								.rts
								.services
								.util
								.constants
								.CommonConstant
								.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
						return;
					}
					getFrame().setVisibleRTS(false);

				}
				break;
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
				setFrame(new FrmSupervisorOverrideCTL004(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmSupervisorOverrideCTL004(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}