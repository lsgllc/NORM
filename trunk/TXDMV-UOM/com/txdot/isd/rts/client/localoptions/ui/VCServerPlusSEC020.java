package com.txdot.isd.rts.client.localoptions.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.MiscellaneousData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCServerPlusSEC020.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * B Hargrove	08/26/2004	Show popup message when ServerPlus is  
 *							enabled or disabled alerting the user that
 *							RTS must be stopped\restarted on all ws.
 *							modify setView()
 *							defect 7243 Ver 5.2.1
 * Min Wang		02/25/2005	Make basic RTS 5.2.3 changes
 * 							organize imports, format source
 * 							defect 7891 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3 
 * Min Wang 	06/20/2005  display error msg on desk top since 
 * 							FrmCTL001 is deprecated.
 * 							modify setView()
 * 							defect 8197 Ver 5.2.3   
 * Min Wang		09/08/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * K Harrell	09/15/2005	Do not show success msg if Server/DB Down
 * 							while changing ServerPlusIndi
 * 							modify setView()  
 * 							defect 8274 Ver 5.2.3
 * Jeff S.		06/22/2006	Used screen constant for CTL001 Title.
 * 							remove TXT_CONFM_MSG
 * 							modify setView()
 * 							defect 8756 Ver 5.2.3 
 * K Harrell	08/29/2008	Send vector of Misc Data, AdminLog Data 
 * 							on ServerPlus Change
 * 							add SERVER_PLUS, ENABLED, DISABLED
 * 							add getAdminLogData() 
 * 							modify setView() 
 * 							defect 8595 Ver Defect_POS_B
 * K Harrell	08/22/2009	Implement new AdminLogData constructor
 * 							modify getAdminLogDta()
 * 							defect 8628 Ver Defect_POS_F         
 * --------------------------------------------------------------------- 
 */

/**
 * Controller for Server Plus screen SEC020 
 * 
 * @version	Defect_POS_F	08/22/2009 
 * @author 	Ashish Mahajan
 * <br>Creation Date:		10/12/2001 15:01:26  	  
 */

public class VCServerPlusSEC020 extends AbstractViewController
{
	// defect 8595 
	private static final String SERVER_PLUS = "SrvrPlus";
	private static final String ENABLED = "Enabled";
	private static final String DISABLED = "Disabled";
	// end defect 8595 

	private static final String TXT_SERVER_PLUS_CHANGE =
		"ServerPlus status has changed! RTS must be restarted "
			+ "on all workstations to implement the change.";

	/**
	 * VCServerPlusSEC020 constructor comment.
	 */
	public VCServerPlusSEC020()
	{
		super();
	}

	/** 
	 * Return populated AdminLogData Object
	 * 
	 * @return AdministrationLogData
	 */
	private AdministrationLogData getAdminLogData(int aiServerPlusIndi)
	{
		// defect 8628 
		AdministrationLogData laLogData =
			new AdministrationLogData(ReportConstant.CLIENT);
		// end defect 8628 
		laLogData.setEntity(SERVER_PLUS);
		laLogData.setAction(CommonConstant.TXT_ADMIN_LOG_REVISE);
		laLogData.setEntityValue(
			aiServerPlusIndi == 1 ? ENABLED : DISABLED);
		return laLogData;
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
	 * Handles data coming from their JDialogBox - inside the 
	 * subclasses implementation should be calls to fireRTSEvent() to
	 * pass the data to the RTSMediator.
	 * 
	 * @param aiCommand int
	 * @param aaData Object  
	 */
	public void processData(int aiCommand, Object aaData)
	{
		// No action taken
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
		try
		{
			MiscellaneousData laMiscData = new MiscellaneousData();
			laMiscData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laMiscData.setSubstaId(SystemProperty.getSubStationId());

			// defect 8595
			// Send Vector Misc Data and AdminLog Data 
			laMiscData.setServerPlusIndi(
				asTransCode.equals(TransCdConstant.SP_DIS) ? 0 : 1);
			Vector lvData = new Vector();
			lvData.add(CommonConstant.ELEMENT_0, laMiscData);
			lvData.add(
				CommonConstant.ELEMENT_1,
				getAdminLogData(laMiscData.getServerPlusIndi()));
			setDirectionFlow(AbstractViewController.FINAL);
			getMediator().processData(
				getModuleName(),
				LocalOptionConstant.SERVER_PLUS,
				lvData);
			// end defect 8595

			// defect 8274 
			// Do not show confirmation msg if Server or DB Down while 
			// processing. 
			if (RTSApplicationController.isDBReady())
			{
				// defect 8756
				// Used common constant for CTL001 title
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						TXT_SERVER_PLUS_CHANGE,
						ScreenConstant.CTL001_FRM_TITLE);
				// end defect 8756
				// defect 8197
				leRTSEx.displayError(getMediator().getDesktop());
			}
			// end defect 8274 
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}
}
