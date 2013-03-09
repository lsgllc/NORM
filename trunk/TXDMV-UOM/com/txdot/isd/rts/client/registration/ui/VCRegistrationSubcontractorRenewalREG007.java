package com.txdot.isd.rts.client.registration.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.SubcontractorRenewalCacheData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCRegistrationSubcontractorRenewalREG007
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		05/02/2002	CQU100003747, put setVisible(false) before 
 * 							calling mediator
 * K Harrell	01/26/2004	5.2.0 Merge.  See PCR 34 comments.Formatted.
 * 							Version 5.2.0	
 * Ray Rowehl	02/08/2005	Change package references to Transaction
 * 							modify processData()
 * 							defect 7705 Ver 5.2.3   
 * B Hargrove	03/09/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD
 *							defect 7894 Ver 5.2.3
 * B Hargrove	03/21/2005	Use new getters\setters for frame, 
 *							controller, etc.
 *							defect 7894 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7894 Ver 5.2.3
 * K Harrell	05/02/2005	Frame INV014 renamed to INV003
 * 							modify processData()  
 * 							defect 6966 Ver 5.2.3   
 * B Hargrove	06/16/2005	Remove unused variable.
 * 							defect 7894 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------
 */

/**
 * VC RegistrationSubcontractorRenewal REG007
 * 
 * 
 * @version	5.2.4		08/11/2006
 * @author	Nancy Ting
 * <br>Creation Date:	10/18/2001 11:10:31 
 */

public class VCRegistrationSubcontractorRenewalREG007
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public static final int PROCESS_RENWL = 90;
	public static final int CLEAN_CANCEL = 91;
	public static final int CANCEL_HELD_ITMS_ONLY = 92;
	public static final int GENERATE_REPORT = 93;
	public static final int DEL_SELECTED_SUBCON_RENWL = 94;
	public static final int MODIFY_SUBCON_RENWL_RECORD = 95;
	public static final int CHECK_DISK_INVENTORY = 96;
	public static final int ADD_AND_END_TRANS = 97;

	/**
	 * VCRegistrationSubcontractorRenewalREG007 constructor.
	 */
	public VCRegistrationSubcontractorRenewalREG007()
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
		return com
			.txdot
			.isd
			.rts
			.services
			.util
			.constants
			.GeneralConstant
			.REGISTRATION;
	}

	/**
	 * Controls the screen flow from REG007.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int  
	 * @param aaData Object 
	*/
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.CANCEL :
				setDirectionFlow(AbstractViewController.FINAL);
				try
				{
					// defect 8884
					// use close() so that it does setVisibleRTS()
					close();
					//getFrame().setVisible(false);
					// end 8884
					getMediator().processData(
						getModuleName(),
						RegistrationConstant.NO_DATA_TO_BUSINESS,
						null);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				if (getFrame().isVisible())
				{
					(
						(FrmRegistrationSubcontractorRenewalREG007) getFrame())
						.repaintComponents();
				}
				break;
			case PROCESS_RENWL :
				//call for validation, and adding a transaction
				setDirectionFlow(AbstractViewController.DIRECT_CALL);
				try
				{
					aaData =
						getMediator().processData(
							getModuleName(),
							com
								.txdot
								.isd
								.rts
								.services
								.util
								.constants
								.RegistrationConstant
								.PROCESS_SUBCON_RENWL,
							aaData);
				}
				catch (RTSException aeRTSEx)
				{
					if (aeRTSEx.getCode() == 594
						|| aeRTSEx.getCode() == 706)
					{
						(
							(
								SubcontractorRenewalCacheData) aaData)
									.setException(
							aeRTSEx);
					}
					else
					{
						aeRTSEx.displayError(getFrame());
						return;
					}
				}
				if (((SubcontractorRenewalCacheData) aaData)
					.getException()
					!= null)
				{
					RTSException leRTSEx =
						((SubcontractorRenewalCacheData) aaData)
							.getException();
					RTSException leRTSEx2 = new RTSException();
					if (leRTSEx.getCode() == 594)
					{
						leRTSEx2.addException(
							leRTSEx,
							(
								(FrmRegistrationSubcontractorRenewalREG007) getFrame())
								.getNewPlateTxtField());
					}
					else
					{
						leRTSEx2.addException(
							leRTSEx,
							(
								(FrmRegistrationSubcontractorRenewalREG007) getFrame())
								.getNewFeeTxtField());
					}
					leRTSEx2.displayError(getFrame());
					leRTSEx2.getFirstComponent().requestFocus();
					return;
				}
				// defect 7894
				// remove unused variable
				//boolean lbNext = false;
				//
				if (((SubcontractorRenewalCacheData) aaData)
					.getINV003ProcessInventoryData()
					!= null)
				{
					//transaction not added yet, go to INV003	   
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.INV003);
				//	lbNext = true;
				// end defect 7894
				}
				else
				{
					//transaction completed
					setDirectionFlow(AbstractViewController.CURRENT);
				}
				try
				{
					getMediator().processData(
						getModuleName(),
						RegistrationConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			case AbstractViewController.ENTER :
				setDirectionFlow(AbstractViewController.NEXT);
				setNextController(ScreenConstant.RPR000);
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
							.RegistrationConstant
							.PROC_COMPLETE_SUBCON_RENWL,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				if (getFrame().isVisible())
				{
					(
						(FrmRegistrationSubcontractorRenewalREG007) getFrame())
						.repaintComponents();
				}
				break;
			case CLEAN_CANCEL :
				setDirectionFlow(AbstractViewController.FINAL);
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
							.RegistrationConstant
							.CANCEL_SUBCON,
						aaData);
					if (getFrame() != null)
					{
						// defect 8884
						// use close() so that it does setVisibleRTS()
						close();
						//getFrame().setVisible(false);
						// end 8884
					}
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			case CANCEL_HELD_ITMS_ONLY :
				setDirectionFlow(AbstractViewController.FINAL);
				try
				{
					if (getFrame() != null)
					{
						// defect 8884
						// use close() so that it does setVisibleRTS()
						close();
						//getFrame().setVisible(false);
						// end 8884
					}
					getMediator().processData(
						getModuleName(),
						com
							.txdot
							.isd
							.rts
							.services
							.util
							.constants
							.RegistrationConstant
							.CANCEL_HELD_SUBCON,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			case DEL_SELECTED_SUBCON_RENWL :
				setDirectionFlow(AbstractViewController.CURRENT);
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
							.RegistrationConstant
							.DEL_SELECTED_SUBCON_RENWL_RECORD,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
				
			case GENERATE_REPORT :
				setDirectionFlow(AbstractViewController.NEXT);
				setNextController(ScreenConstant.RPR000);
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
							.RegistrationConstant
							.GENERATE_SUBCON_REPORT_DRAFT,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				if (getFrame().isVisible())
				{
					(
						(FrmRegistrationSubcontractorRenewalREG007) getFrame())
						.repaintComponents();
				}
				break;
			case MODIFY_SUBCON_RENWL_RECORD :
				setDirectionFlow(AbstractViewController.NEXT);
				setNextController(
					com
						.txdot
						.isd
						.rts
						.services
						.util
						.constants
						.ScreenConstant
						.REG009);
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
							.RegistrationConstant
							.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
				
			case CHECK_DISK_INVENTORY :
				setDirectionFlow(AbstractViewController.DIRECT_CALL);
				try
				{
					aaData =
						getMediator().processData(
							getModuleName(),
							com
								.txdot
								.isd
								.rts
								.services
								.util
								.constants
								.RegistrationConstant
								.CHECK_DISK_INVENTORY,
							aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
					return;
				}
				SubcontractorRenewalCacheData laSubconCacheData =
					(SubcontractorRenewalCacheData) aaData;
					
				if (laSubconCacheData.getInvValIndex().size() == 0)
				{
					//all found, no need to validate
					processData(ADD_AND_END_TRANS, laSubconCacheData);
				}
				else
				{
					//not all found, need to validate some
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						"Not all renewals are validated.  Please"
							+ " click on modify for any renewals that"
							+ " contain a red icon.",
						"").displayError(
						getFrame());
					//count number of errors
					(
						(FrmRegistrationSubcontractorRenewalREG007) getFrame())
							.showError(
						laSubconCacheData.getInvValIndex().size());
					setDirectionFlow(AbstractViewController.CURRENT);
					
					try
					{
						getMediator().processData(
							getModuleName(),
							RegistrationConstant.NO_DATA_TO_BUSINESS,
							laSubconCacheData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
				}
				break;
			case ADD_AND_END_TRANS :
				setDirectionFlow(AbstractViewController.DIRECT_CALL);
				try
				{
					aaData =
						getMediator().processData(
							getModuleName(),
							com
								.txdot
								.isd
								.rts
								.services
								.util
								.constants
								.RegistrationConstant
								.ADD_DISK_TRANS_AND_END_TRANS,
							aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				SubcontractorRenewalCacheData laSubcontractorCacheData =
					(SubcontractorRenewalCacheData) aaData;
				// defect 7705
				// change package for Transaction
				com
					.txdot
					.isd
					.rts
					.services
					.common
					.Transaction
					.setRunningSubtotal(
					laSubcontractorCacheData.getRunningTotal());

				int liReturnStatus =
					new RTSException(
						RTSException.CTL001,
						"Total is $ "
							+ com
								.txdot
								.isd
								.rts
								.services
								.common
								.Transaction
								.getRunningSubtotal()
								.toString(),
						"").displayError(
						getFrame());
				// end defect 7705
				if (liReturnStatus == RTSException.YES)
				{
					processData(
						AbstractViewController.ENTER,
						laSubcontractorCacheData);
				}
				else
				{
					return;
				}
				break;
			case AbstractViewController.HELP :
				// to be implemented
				break;
			default :
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
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDB =
				getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(
					new FrmRegistrationSubcontractorRenewalREG007(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmRegistrationSubcontractorRenewalREG007(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
