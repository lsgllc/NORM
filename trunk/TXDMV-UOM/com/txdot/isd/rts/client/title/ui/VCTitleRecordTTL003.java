package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.data.PresumptiveValueData;
import com.txdot.isd.rts.services.data.TitleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCTitleRecordTTL003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * T Pederson   05/23/2002  Added DTA check in processData case 
 * 							RECOR_NOTAPPL CQU100004054.
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
 * 							Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.   
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbage.
 * 							defect 7898 Ver 5.2.3    
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * T Pederson	09/08/2006	Added case for presumptive value to 
 * 							processData()
 *							modify processData()
 * 							defect 8926 Ver 5.2.5
 * K Harrell	09/14/2006	Add code for handling exceptions during
 * 							presumptive value call 
 * 							modify processData()
 * 							defect 8926 Ver 5.2.5 
 * T Pederson	09/15/2006	Changed direction flow to DIRECT_CALL. 
 *							modify processData()
 * 							defect 8926 Ver 5.2.5
 * K Harrell	03/10/2009	delete handleError()
 * 							modify processData()
 * 							defect 9971 Ver Defect_POS_E 
 * K Harrell 	12/15/2009	Do not save to vault
 * 							modify processData() 
 * 							defect 10290 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to display/capture vehicle information of an 
 * existing mainframe record.
 *
 * @version	Defect_POS_H 	12/15/2009
 * @author	Ashish Mahajan
 * <br>Creation Date:		06/27/2001 20:54:25
 */

public class VCTitleRecordTTL003 extends AbstractViewController
{
	// defect 8926
	public final static int PRESUMP_VAL = 22;
	// end defect 8926
	public final static int CHANGE_REG = 23;
	public final static int RECOR_NOTAPPL = 24;
	public final static int VTR_AUTH = 25;
	public final static int VTR_SUPV = 26;

	/**
	 * VCTitleRecordTTL003 constructor comment.
	 */
	public VCTitleRecordTTL003()
	{
		super();
	}

	/**
	 * All subclasses must override this method to return their own 
	 * module name
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
					setData(aaData);
					
					VehicleInquiryData laVehData =
						(VehicleInquiryData) aaData;
						
					// defect 10290 
					// No longer save to vault 
					//  // save to vault if DTA
					//if (getTransCode().equals(TransCdConstant.DTANTD)
					//	|| getTransCode().equals(TransCdConstant.DTANTK)
					//	|| getTransCode().equals(TransCdConstant.DTAORD)
					//	|| getTransCode().equals(TransCdConstant.DTAORK))
					//{
					//	getMediator().closeVault(
					//		ScreenConstant.TTL003,
					//		laVehData);
					//}

					//copy the data object
					//Save copy of dlrttlData
					TitleValidObj laValidObj =
						(TitleValidObj) laVehData.getValidationObject();

					DealerTitleData laDlrTtlData =
						(DealerTitleData) laValidObj.getDlrTtlData();

					VehicleInquiryData laCopyVehData =
						(VehicleInquiryData) UtilityMethods.copy(
							laVehData);

					laValidObj =
						(TitleValidObj) laCopyVehData
							.getValidationObject();

					laValidObj.setDlrTtlData(laDlrTtlData);

					// display owner entry screen if no lien exists
					TitleData laTtlData =
						laVehData.getMfVehicleData().getTitleData();

					// defect 9971 
					boolean lbDispLien = laTtlData.hasLien();
					// end defect 9971 

					// defect 10290 
					setNextController(
						lbDispLien
							? ScreenConstant.TTL005
							: ScreenConstant.TTL007);
					// end defect 10290 
					
					setDirectionFlow(AbstractViewController.NEXT);
					
					// TODO setDoneTTL005?  
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							laCopyVehData);

						if (lbDispLien && laValidObj.isDoneTTL005())
						{
							laValidObj.setDoneTTL005(false);
							setNextController(ScreenConstant.TTL007);
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								laCopyVehData);
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case CANCEL :
				{
					// defect 10290
					// Implement UtilityMethods.isDTA(), close() 
					//      lbDTA vs. lbIsDTA
					boolean lbDTA = false; 
					
					if (UtilityMethods.isDTA(getTransCode()))
					{
						lbDTA = true; 
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						aaData = new RTSException();
						setPreviousController(ScreenConstant.TTL002);
					}
					else
					{
						setDirectionFlow(AbstractViewController.CANCEL);
					}
					close();
					
					try
					{
						if (lbDTA)
						{
							// end defect 10290
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								new RTSException());
						}
						else
						{
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								aaData);
						}
					}
					// end defect 10290
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;

				}
			case CHANGE_REG :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						setNextController(ScreenConstant.REG008);
						(
							(
								VehicleInquiryData) aaData)
									.setShouldGoPrevious(
							true);
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

				// defect 8926
			case PRESUMP_VAL :
				{
					try
					{
						setNextController(ScreenConstant.TTL003);
						setDirectionFlow(
							AbstractViewController.DIRECT_CALL);

						Object laObject =
							getMediator().processData(
								getModuleName(),
								TitleConstant.GET_PRIVATE_PARTY_VALUE,
								aaData);
						if (laObject != null
							&& laObject instanceof PresumptiveValueData)
						{
							setDirectionFlow(
								AbstractViewController.NEXT);
							setNextController(ScreenConstant.TTL045);
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								(PresumptiveValueData) laObject);
						}
						else
						{
							RTSException leRTSEx =
								new RTSException(
									ErrorsConstant
										.ERR_NUM_SPV_SVC_UNAVAILABLE);
							leRTSEx.displayError(getFrame());
						}
					}
					catch (RTSException aeRTSEx)
					{
						RTSException leRTSEx =
							new RTSException(
								ErrorsConstant
									.ERR_NUM_SPV_SVC_UNAVAILABLE);
						leRTSEx.displayError(getFrame());
					}
					break;
				}
				// end defect 8926
			case RECOR_NOTAPPL :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					// defect 10290 
					if (UtilityMethods.isDTA(getTransCode()))
					{
						// end defect 10290 
						setPreviousController(ScreenConstant.TTL002);
					}
					try
					{
						// defect 10290 
						close();
						// end defect 10290 
						
						VehicleInquiryData laVehInqData =
							(VehicleInquiryData) aaData;
						laVehInqData.setNoMFRecs(0);

						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;

				}
			case VTR_AUTH :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.CTL003);
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
					break;

				}
			case VTR_SUPV :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.CTL004);
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
					break;
				}

			case FINAL :
				{
					setDirectionFlow(AbstractViewController.DESKTOP);
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
					break;
				}
		}
	}

	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param cvPreviousControllers Vector
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
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDBox =
				getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmTitleRecordTTL003(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmTitleRecordTTL003(
						getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
