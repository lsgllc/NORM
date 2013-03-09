package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.TitleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * 
 * VCTitleAdditionalInfoTTL008.java 
 * 
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		7/31/2002	Defect 4468, fix code to ensure the ENTER 
 * 							command process has completed before the ESC
 * 							can execute. Method actionPerformed()
 * J Rue		8/22/2002 	Fixed Defect 4661. Copied code from setView 
 * S Govindappa				of AbstractController to TTL008 controller's
 * 							setView method and prevented TTL008 frame 
 * 							from being displayed multiple times.
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.   
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbage.
 * 							defect 7898 Ver 5.2.3    
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * J Rue		07/17/2006	Redo process flow for REJCOR 
 * 							title type = ROP or Non-Titled 
 * 							modify doCompleteTransaction()
 * 							defect 8850 Ver 5.2.3
 * K Harrell	05/26/2008	Add Supervisor Override Code Processing
 * 							add SUPV_OVRIDE
 * 							modify processData()
 * 							defect 9584 Ver Defect POS A 
 * K Harrell	12/15/2009	delete handleError() 
 * 							modify processData() 
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	01/16/2012	modify processData()
 * 							defect 10827 Ver 6.10.0 
 * K Harrell	02/02/2012	delete REDIRECT_IS_NEXT_VC_REG029
 * 							modify processData()
 * 							defect 10827 Ver 6.10.0 
 * K Harrell	02/10/2012	Clear saved data for TTL010 if not 
 * 							Rights of Survivorship 
 * 							modify processData() 
 * 							defect 10827 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
* VC controller for Title Additional Info screen.
*
* @version 	6.10.0 			02/10/2012
* @author 	Administrator
* <br>Creation Date:		08/22/2001 15:47:18
*/

public class VCTitleAdditionalInfoTTL008 extends AbstractViewController
{

	// defect 10827 
	// public final static int REDIRECT_IS_NEXT_VC_REG029 = 6;
	// end defect 10827 
	
	public final static int REDIRECT_NEXT_VC = 7;
	//	defect 9584 
	public final static int SUPV_OVRIDE = 24;
	// end defect 9584 

	/**
	 * VCTitleAdditionalInfoTTL008 constructor comment.
	 */
	public VCTitleAdditionalInfoTTL008()
	{
		super();
	}

	/**
	 * Get data for Corrected Rejected Titles: RPO and Non-Title
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 */
	private void doCompleteTransaction(VehicleInquiryData aaVehInqData)
	{
		CompleteTitleTransaction laCompTtlTrans = null;
		CompleteTransactionData laCompTtlTransData = null;
		try
		{
			laCompTtlTrans =
				new CompleteTitleTransaction(
					aaVehInqData,
					getTransCode());
			laCompTtlTransData = laCompTtlTrans.doCompleteTransaction();

		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
		setDirectionFlow(AbstractViewController.DIRECT_CALL);
		try
		{
			//	Make call to mediator to get vector of 
			//		boolean - display Reg029, always false
			//		CompleteTransactiondata object
			Vector lvRegCorrData =
				(Vector) getMediator().processData(
					GeneralConstant.COMMON,
					CommonConstant.IS_NEXT_VC_REG029,
					laCompTtlTransData);

			//	Make call to mediator to get vector of 
			//		String - Name of next frame, FrmFeesDuePMT004()
			//		CompleteTransactiondata object
			lvRegCorrData =
				(Vector) getMediator().processData(
					GeneralConstant.COMMON,
					CommonConstant.GET_NEXT_COMPLETE_TRANS_VC,
					((Vector) lvRegCorrData).elementAt(1));

			// Make call to continue processing to the next frame
			processData(REDIRECT_NEXT_VC, lvRegCorrData);
			// end defect 8850 	
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}

	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.TITLE;
	}

	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int 
	 * 	the command so the Frame can communicate with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					boolean lbContFlow = true;
					
					// defect 10827 
					// Use laTitleData vs. laCopyVehData.getMfVehicleData().getTitleData()
					// to make following code more readable 
					VehicleInquiryData laCopyVehData =
						(VehicleInquiryData) UtilityMethods.copy(
								aaData);
					
					TitleData laTitleData = laCopyVehData.getMfVehicleData().getTitleData(); 

					int liTtlType =	laTitleData.getTtlTypeIndi();

					if (liTtlType == TitleTypes.INT_REGPURPOSE)
					{
						laTitleData.setOwnrShpEvidCd(21);

						laTitleData.setPriorCCOIssueIndi(0);

						if (getTransCode()
							.equals(TransCdConstant.REJCOR))
						{
							lbContFlow = false;
							doCompleteTransaction(laCopyVehData);
						}
						else
						{
							setNextController(ScreenConstant.TTL012);
						}
					}
					else if (liTtlType == TitleTypes.INT_NONTITLED)
					{
						laTitleData.setPriorCCOIssueIndi(0);

						if (getTransCode()
							.equals(TransCdConstant.REJCOR))
						{
							lbContFlow = false;
							doCompleteTransaction(laCopyVehData);
						}
						else
						{
							// This is bizarre (KPH)  2/2/12
							if (getTransCode()
								.equals(TransCdConstant.DTANTK)
								|| getTransCode().equals(
									TransCdConstant.DTANTD))
							{
								
									laTitleData.setOwnrShpEvidCd(0);
							}
							setNextController(ScreenConstant.TTL012);
						}
					}
					else if (
						liTtlType == TitleTypes.INT_ORIGINAL
							|| liTtlType == TitleTypes.INT_CORRECTED)
					{
						if (laTitleData.getSurvshpRightsIndi() == 1) 
						{
							setNextController(ScreenConstant.TTL010);
						}
						else
						{
							laTitleData.resetSurvivorData(); 
							// Reset Data if previously saved from 
							// TTL010
							getMediator().closeVault(
									ScreenConstant.TTL010,
									null);
							setNextController(ScreenConstant.TTL011);
						}
						// end defect 10827 
					}
					if (lbContFlow)
					{
						setDirectionFlow(AbstractViewController.NEXT);
						try
						{
							// This is interesting logic which saves OwnershipEvidence
							// Codes in Title Validation Object. 
							getMediator()
								.processData(
									getModuleName(),
									TitleConstant.GET_OWNR_EVID_CDS,
							laCopyVehData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
					}
					break;
				}
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					close();
					break;
				}
				// defect 9584 
			case SUPV_OVRIDE :
				{
					try
					{
						setNextController(ScreenConstant.CTL004);
						setDirectionFlow(AbstractViewController.NEXT);
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
				}
				// end defect 9584
				// defect 10827 
				// This is never referenced 
				//			case REDIRECT_IS_NEXT_VC_REG029 :
				//				{
				//					try
				//					{
				//						Vector lvIsNextVCREG029 = (Vector) aaData;
				//						Boolean lbGoToREG029 =
				//							(Boolean) lvIsNextVCREG029.get(0);
				//						// first element is flag whether to go to REG029
				//						CompleteTransactionData lData =
				//							(
				//								CompleteTransactionData) lvIsNextVCREG029
				//									.get(
				//								1);
				//						if (lbGoToREG029.equals(Boolean.TRUE))
				//						{
				//							setNextController(ScreenConstant.REG029);
				//							setDirectionFlow(
				//								AbstractViewController.NEXT);
				//							getMediator().processData(
				//								GeneralConstant.COMMON,
				//								TitleConstant.NO_DATA_TO_BUSINESS,
				//								lData);
				//						}
				//						else
				//						{
				//							// determine next vc if NOT reg029
				//							setDirectionFlow(
				//								AbstractViewController.CURRENT);
				//							getMediator().processData(
				//								GeneralConstant.COMMON,
				//								CommonConstant
				//									.GET_NEXT_COMPLETE_TRANS_VC,
				//								((Vector) aaData).elementAt(1));
				//						}
				//					}
				//					catch (RTSException aeRTSEx)
				//					{
				//						aeRTSEx.displayError(getFrame());
				//					}
				//					break;
				//				}
				// end defect 10827 
				
			case REDIRECT_NEXT_VC :
				{
					try
					{
						Vector lvNextVC = (Vector) aaData;
						String lsNextVCName = (String) lvNextVC.get(0);
						// first element is name of next controller 
						CompleteTransactionData laData =
							(CompleteTransactionData) lvNextVC.get(1);
						if (lsNextVCName != null)
						{
							setNextController(lsNextVCName);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								GeneralConstant.COMMON,
								TitleConstant.NO_DATA_TO_BUSINESS,
								laData);
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
		}
	}

	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param cvPreviousControllers java.util.Vector
	 * @param asTransCode java.lang.String
	 * @param aaData java.lang.Object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDBox =
				getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmTitleAdditionalInfoTTL008(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmTitleAdditionalInfoTTL008(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
