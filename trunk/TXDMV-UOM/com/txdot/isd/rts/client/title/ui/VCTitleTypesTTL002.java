package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCTitleTypesTTL002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Govindappa 02/26/03    Fixing defect 5590. Made changes to 
 * 							processData(..) to waive the registration 
 * 							for stolen vehicle.
 * B Arredondo	10/28/2003	Defects  6029, 6090, 6134, and 6312
 * 	J Rue					Moving code to 
 * 							FrmTitleRecordTTL003.actionPerformed()
 *							and 
 *							FrmNoTitleRecordTTL004.setDatatoDataObject()
 *							Code from defect 5590 caused above defects.
 *							method processData()
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/23/2005	Change ScreenConstant from DTA008a to DTA008
 * 							Cleanup leftover codeing
 * 							modify processData()
 * 							defect 6963,7898 Ver 5.2.3
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
 * K Harrell	12/15/2009	DTA Cleanup 
 * 							delete handleError()
 * 							modify processData(), setTransCd()  
 * 							defect 10290 Ver Defect_POS_H 
 * B Woodson	01/25/2012	modify processData()
 * 							defect 11228 Ver 6.10.0
 * B Woodson	02/9/2012	unmodify processData()
 * 							defect 11228 Ver 6.10.0
 * ------------------------------------------------------------------
 *      
 */

/**
 * This is the controller for TTL002 screen
 * 
 * @version Defect_POS_H 02/9/2012
 * @author Ashish Mahajan <br>
 *         Creation Date: 06/27/2001 20:54:25
 */

public class VCTitleTypesTTL002 extends AbstractViewController
{
	/**
	 * VCTitleTypesTTL002 constructor comment.
	 */

	public VCTitleTypesTTL002()
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
	 * RTSMediator
	 * 
	 * @param aaData java.lang.Object
	 * @param aiCommand int
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setData(
						((FrmTitleTypesTTL002) getFrame())
							.getOrigObject());
					VehicleInquiryData laVehInqData =
						(VehicleInquiryData) aaData;

					// defects 6029, 6090, 6134, and 6312
					//Moving code to FrmTitleRecordTTL003.actionPerformed().
					//Setting RegWaivedIndi to 1 when it should not set 
					//	until Title Record
					//so commenting out code

					//defect 5590
					//				  if (vehInqData.getMfVehicleData() != null)
					//				  {
					//				     VehicleData lvehData = vehInqData.
					//				     	getMfVehicleData().getVehicleData();
					//				     RegistrationData lregData = vehInqData.
					//				     	getMfVehicleData().getRegData();
					//				                    
					//				     if (lvehData != null && lregData != null
					//				        && lvehData.getDpsStlnIndi() == 1)
					//				     {
					//				         lregData.setRegWaivedIndi(1);
					//				     }
					//				 }
					//end defects 6029, 6090, 6134, and 6312

					//Do Validation for DocType
					int liDocTypeCd =
						laVehInqData
							.getMfVehicleData()
							.getTitleData()
							.getDocTypeCd();
					
					if (com
						.txdot
						.isd
						.rts
						.client
						.title
						.business
						.TitleClientBusiness
						.isVehAllowedToBeTitled(liDocTypeCd))
					{
						setTransCd(laVehInqData);

						// If transcd is DTA always go to reg008
						if (UtilityMethods.isDTA(getTransCode()))
						{
							// defect 10290 
							//save info in vault
							//	getMediator().closeVault(
							//		ScreenConstant.TTL002,
							//		laVehInqData);
							// end defect 10290 
							
							setNextController(ScreenConstant.REG008);

							setDirectionFlow(
								AbstractViewController.NEXT);
							try
							{
								getMediator().processData(
									getModuleName(),
									TitleConstant.VALIDATION002,
									laVehInqData);
							}
							catch (RTSException aeRTSEx)
							{
								aeRTSEx.displayError(getFrame());
							}
						}
						// check for record found on mainframe
						else if (laVehInqData.getNoMFRecs() > 0)
						{
							setNextController(ScreenConstant.TTL003);
							setDirectionFlow(
								AbstractViewController.NEXT);
							try
							{
								getMediator().processData(
									getModuleName(),
									TitleConstant.VALIDATION002,
									laVehInqData);
							}
							catch (RTSException aeRTSEx)
							{
								aeRTSEx.displayError(getFrame());
							}
						}
						else
						{
							setNextController(ScreenConstant.REG008);
							setDirectionFlow(
								AbstractViewController.NEXT);
							try
							{
								getMediator().processData(
									getModuleName(),
									TitleConstant.NO_DATA_TO_BUSINESS,
									laVehInqData);
							}
							catch (RTSException aeRTSEx)
							{
								aeRTSEx.displayError(getFrame());
							}
						}
					}
					else
					{
						setDirectionFlow(AbstractViewController.FINAL);
						try
						{
							//Display error message
							RTSException leRTSEx078 =
								new RTSException(78);
							leRTSEx078.displayError(getFrame());
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								aaData);
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
					// defect 10290 
					close(); 

					if (UtilityMethods.isDTA(getTransCode()))
					{
						// end defect 10290 
						
							setDirectionFlow(
								AbstractViewController.PREVIOUS);
						
						setPreviousController(ScreenConstant.DTA008);
						try
						{
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								new RTSException());
								
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
					}
					else
					{
						setDirectionFlow(AbstractViewController.FINAL);
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
					}
					break;
				}
		}
	}
	
	/**
	 * Set TransCd.
	 * 
	 * @param aaData Object
	 */
	private void setTransCd(Object aaData)
	{
		VehicleInquiryData laVehInqData = (VehicleInquiryData) aaData;
		
		TitleValidObj laTtlValidObj =
			(TitleValidObj) laVehInqData.getValidationObject();
			
		DealerTitleData laDlrTtlData =
			(DealerTitleData) laTtlValidObj.getDlrTtlData();
			
		if (getTransCode().equals(TransCdConstant.TITLE)
			|| getTransCode().equals(TransCdConstant.CORTTL)
			|| getTransCode().equals(TransCdConstant.NONTTL)
			// defect 10290 
			|| UtilityMethods.isDTA(getTransCode()))
			// end defect 10290 
		{
			switch (((FrmTitleTypesTTL002) getFrame())
				.getSelectedRadioBtn())
			{
				case TitleTypes.INT_ORIGINAL :
					{
						if (laDlrTtlData != null)
						{
							if (laDlrTtlData.isKeyBoardEntry())
							{
								setTransCode(TransCdConstant.DTAORK);
							}
							else
							{
								setTransCode(TransCdConstant.DTAORD);
							}
							laDlrTtlData.setTransCd(getTransCode());
						}
						else
						{
							setTransCode(TransCdConstant.TITLE);
						}
						break;

					}
				case TitleTypes.INT_CORRECTED :
					{
						setTransCode(TransCdConstant.CORTTL);
						break;
					}
				case TitleTypes.INT_REGPURPOSE :
					{
						setTransCode(TransCdConstant.TITLE);
						break;
					}
				case TitleTypes.INT_NONTITLED :
					{
						if (laDlrTtlData != null)
						{
							if (laDlrTtlData.isKeyBoardEntry())
							{
								setTransCode(TransCdConstant.DTANTK);
							}
							else
							{
								setTransCode(TransCdConstant.DTANTD);
							}
							laDlrTtlData.setTransCd(getTransCode());
						}
						else
						{
							setTransCode(TransCdConstant.NONTTL);
						}
						break;
					}
			}
		}
	}
	
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector
	 * @param asTransCode java.lang.String
	 * @param aaData java.lang.Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDBox =
				getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmTitleTypesTTL002(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmTitleTypesTTL002(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
