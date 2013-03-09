package com.txdot.isd.rts.client.registration.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.SubcontractorRenewalCacheData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCModifySubcontractorRenewalREG009.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/26/2004	5.2.0 Merge. See PCR 34 comments. Formatted.
 * 							Ver 5.2.0	 
 * K Harrell	10/04/2004	Correct typo
 *							modify processData()
 *							defect 7586 Ver 5.2.1 
 * K Harrell	05/02/2005	Frame INV014 renamed to INV003
 * 							modify processData()  
 * 							defect 6966 Ver 5.2.3  
 * B Hargrove	06/23/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 * 							Use new getters\setters for frame, 
 *							controller, etc.
 * 							defect 7894 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */
/**
 * VC ModifySubcontractorRenewal REG009
 *
 * @version	5.2.4		08/11/2006
 * @author	Nancy Ting
 * <br>Creation Date:	10/08/2001 11:54:00
 */

public class VCModifySubcontractorRenewalREG009
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public static final int REDIRECT_TO_REG007 = 80;

	/**
	 * VC ModifySubcontractorRenewal REG009
	 */
	public VCModifySubcontractorRenewalREG009()
	{
		super();
	}

	/**
	 * Returns the Module name constant used by the RTSMediator to pass 
	 * the data to the appropriate Business Layer class.
	 * @return int
	 */
	public int getModuleName()
	{
		return com.txdot.isd.rts.services.util.constants.GeneralConstant
			.REGISTRATION;
	}

	/**
	 * Controls the screen flow from REG009.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int A constant letting the VC know which action 
	 * to perform
	 * @param aaData Object The data from the frame
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case REDIRECT_TO_REG007 :
			{
				setDirectionFlow(AbstractViewController.PREVIOUS);
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisible(false);
				// end 8884
				try
				{
					getMediator().processData(getModuleName(),
						com.txdot.isd.rts.services.util.constants
						.RegistrationConstant.NO_DATA_TO_BUSINESS,
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
				setDirectionFlow(AbstractViewController.CANCEL);
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisible(false);
				// end 8884
				try
				{
					getMediator().processData(getModuleName(),
						com.txdot.isd.rts.services.util.constants
						.RegistrationConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}
			case ENTER :
			{
				setDirectionFlow(AbstractViewController.DIRECT_CALL);
				try
				{
					aaData = getMediator().processData(
						getModuleName(),
						com.txdot.isd.rts.services.util.constants
						.RegistrationConstant.PROCESS_SUBCON_RENWL,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					if (aeRTSEx.getCode() == 594
						|| aeRTSEx.getCode() == 706)
					{
						((SubcontractorRenewalCacheData) aaData)
							.setException(aeRTSEx);
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
					// defect 7586
					// Correct to reference REG009 vs REG007
					if (leRTSEx.getCode() == 594)
					{
						leRTSEx2.addException(leRTSEx,
						// defect 7586
						((FrmModifySubcontractorRenewalREG009) 
							getFrame()).getNewPlt());
					}
					else
					{
						leRTSEx2.addException(
							leRTSEx,
							((FrmModifySubcontractorRenewalREG009) 
								getFrame()).getRenwlFee());
					}
					// end defect 7586 
					leRTSEx2.displayError(getFrame());
					leRTSEx2.getFirstComponent().requestFocus();
					return;
				}
				boolean lbPrevious = false;
				if (((SubcontractorRenewalCacheData) aaData)
					.getINV003ProcessInventoryData()
					!= null)
				{
					// defect 6966 
					// INV014b renamed to INV003 
					//transaction not added yet, go to INV003 	   
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.INV003);
					// end defect 6966
				}
				else
				{
					//transaction completed
					setDirectionFlow(AbstractViewController.PREVIOUS);
					lbPrevious = true;
				}
				try
				{
					if (lbPrevious)
					{
						// defect 8884
						// use close() so that it does setVisibleRTS()
						close();
						//getFrame().setVisible(false);
						// end 8884
					}
					getMediator().processData(getModuleName(),
						RegistrationConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}
			default :
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector A vector containing
	 *  the String names of the previous controllers in order
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		//redirect from INV003, completed Trans
		if (((SubcontractorRenewalCacheData) aaData)
			.getRecordTobeModified()
			== null)
		{
			processData(
				VCModifySubcontractorRenewalREG009.REDIRECT_TO_REG007,
				aaData);
		}
		else
		{
			if (getFrame() == null)
			{
				com.txdot.isd.rts.client.general.ui.RTSDialogBox 
					laRTSDialogBox = getMediator().getParent();
				if (laRTSDialogBox != null)
				{
					setFrame(new FrmModifySubcontractorRenewalREG009(
						laRTSDialogBox));
				}
				else
				{
					setFrame(new FrmModifySubcontractorRenewalREG009(
						getMediator().getDesktop()));
				}
				//((FrmModifySubcontractorRenewalREG009)frame).
				//setStoreLastTransIssueDate(((
				//SubcontractorRenewalCacheData)data).
				//getRecordTobeModified().getSubconIssueDate());        			
			}
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
	}
}
