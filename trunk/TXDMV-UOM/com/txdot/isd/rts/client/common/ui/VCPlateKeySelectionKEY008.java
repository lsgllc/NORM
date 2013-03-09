package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.title.ui.TitleValidObj;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.MFLogError;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * VCPlateKeySelectionKEY008.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Arrd		07/11/2002	move frame.setVisible(false) to a more 
 * Jeff Rue					appropriate location. 
 * 							modify processData()
 * 							defect 4445
 * J Rue		03/23/2005	Change DTA008a to DTA008
 * 							Cleanup code
 * 							modify processData()
 * 							defect 6963 Ver 5.2.3
 * J Rue		03/30/2005	Applied changes from walkthrough review
 * 							Clean up paramater comments
 * 							modify setView()	
 * 							defect 6963 Ver 5.2.3
 * K Harrell	05/03/2005	Remove reference to RegistrationMiscData
 * 							modify processData()
 * 							defect 8188 Ver 5.2.3
 * Ray Rowehl	07/29/2005	RTS 5.2.3 code cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3 
 * Jeff S.		06/23/2006	Used screen constant for CTL001 Title.
 * 							remove ERRMSG_CONFIRM
 * 							modify processData()
 * 							defect 8756 Ver 5.2.3
 * K Harrell	12/16/2009	Cleanup.  Implement close(). 
 * 							delete caGeneralSearchData, ERRMSG_REQ_PLT
 * 							modify processData()
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	01/25/2010	Plate No can be empty 
 * 							modify processData()
 * 							defect 10339 Ver Defect_POS_H 
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: Plate KeySelection KEY008
 * 
 * @version	Defect_POS_H	01/25/2010
 * @author	Joseph Peters
 * <br>Creation Date:		09/05/2001 09:56:32
 */

public class VCPlateKeySelectionKEY008 extends AbstractViewController
{
	// defect 10290 
	// private GeneralSearchData caGeneralSearchData = null;
	// private final static String ERRMSG_REQD_PLT =
	//	"Requested plate number is 7 digits.\n";
	// end defect 10290 

	/**
	 * VCPlateKeySelectionKEY008 constructor.
	 */
	public VCPlateKeySelectionKEY008()
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
		return GeneralConstant.COMMON;
	}

	/**
	 * Controls the screen flow from KEY008.  It passes the data to 
	 * the RTSMediator.
	 * 
	 * @param aiCommand int  
	 * @param aaData Object  
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);

					// defect 10290
					GeneralSearchData laGSD =
						(GeneralSearchData) aaData;

					try
					{
						// Now handled in Frame validateData() 
						//   or not at all 
						// *** This was for Length PlateNo = 7 - Removed
						//if (caGeneralSearchData.getIntKey2() == 1)
						//{
						//	// defect 8756
						//	// Used common constant for CTL001 title
						//	RTSException leRTSExMsg =
						//		new RTSException(
						//			RTSException.CTL001,
						//			ERRMSG_REQD_PLT,
						//			ScreenConstant.CTL001_FRM_TITLE);
						//	// end defect 8756
						//	int liRet =
						//		leRTSExMsg.displayError(getFrame());
						//	if (liRet == RTSException.NO)
						//	{
						//		return;
						//	}
						//}
						//	*** This was for PlateNo > 7 or null
						//  >7 can't happened; 
						//      Empty handled in validateData()
						// defect 10339 
						// Restore code for handling empty plate no 
						// Modify to handle new DTA structure 
						if (laGSD.getIntKey2()
							== CommonConstant.KEY008_NO_PLT_FLG)
						{
							VehicleInquiryData laVehInqData =
								new VehicleInquiryData();
							MFVehicleData laMFVehData =
								new MFVehicleData();
							laMFVehData.setVehicleData(
								new VehicleData());
							laMFVehData.setTitleData(new TitleData());
							laMFVehData.setRegData(
								new RegistrationData());
							laMFVehData.setOwnerData(new OwnerData());
							laMFVehData.setVctSalvage(
								new java.util.Vector());
							laMFVehData.getVctSalvage().add(
								new SalvageData());
							laVehInqData.setMfVehicleData(laMFVehData);
							laVehInqData.setNoMFRecs(0);
							if (getTransCode()
								.equals(TransCdConstant.DTAORD))
							{
								//default screen
								boolean lbChngDirFlow = false;

								RTSException leRTSExHldr = null;

								setNextController(
									ScreenConstant.TTL002);

								getFrame().setVisibleRTS(false);

								if (
									(
										(FrmPlateKeySelectionKEY008) getFrame())
									.getDlrTtlData()
									!= null)
								{
									TitleValidObj laTtlValidObj =
										(TitleValidObj) laVehInqData
											.getValidationObject();

									DealerTitleData laDlrTitleData =
										(
											(FrmPlateKeySelectionKEY008) getFrame())
											.getDlrTtlData();

									// VIN should never happen (KPH)  
									if (laGSD
										.getKey1()
										.equals(CommonConstant.VIN))
									{
										laDlrTitleData
											.getMFVehicleData()
											.getVehicleData()
											.setVin(
											laGSD.getKey2());
									}
									else if (
										laGSD.getKey1().equals(
											CommonConstant
												.REG_PLATE_NO))
									{
										laDlrTitleData
											.getMFVehicleData()
											.getRegData()
											.setRegPltNo(
											laGSD.getKey2());
									}
									//	Vector lvDlrTitleList =
									//		(
									//			(FrmPlateKeySelectionKEY008) getFrame())
									//		.getDlrTitlDataObjs();

									//check for data from MF/DTA008
									leRTSExHldr =
										VCVinKeySelectionKEY006
											.validateDTA008DataFromMFData(
											laDlrTitleData,
											laVehInqData);

									if (leRTSExHldr != null)
									{
										lbChngDirFlow = true;
										setPreviousController(
											ScreenConstant.DTA008);
									}
									//end check for data from MF/DTA008
									if (laTtlValidObj != null)
									{
										laTtlValidObj.setDlrTtlData(
											laDlrTitleData);
									}
									else
									{
										laTtlValidObj =
											new TitleValidObj();
										laTtlValidObj.setDlrTtlData(
											laDlrTitleData);
										laVehInqData
											.setValidationObject(
											laTtlValidObj);
									}
								}
								if (lbChngDirFlow)
								{
									setDirectionFlow(
										AbstractViewController
											.PREVIOUS);
								}
								else
								{
									setDirectionFlow(
										AbstractViewController.NEXT);
								}
								try
								{
									setData(aaData);
									close();
									if (lbChngDirFlow)
									{
										getMediator().processData(
											getModuleName(),
											CommonConstant
												.NO_DATA_TO_BUSINESS,
											leRTSExHldr);
									}
									else
									{
										getMediator().processData(
											getModuleName(),
											CommonConstant
												.NO_DATA_TO_BUSINESS,
											laVehInqData);
									}
								}
								catch (RTSException aeRTSEx)
								{
									aeRTSEx.displayError(getFrame());
								}
								break;
							}
							else
							{
								setNextController(
									ScreenConstant.TTL002);
								setDirectionFlow(
									AbstractViewController.NEXT);
								getMediator().processData(
									getModuleName(),
									CommonConstant.NO_DATA_TO_BUSINESS,
									laVehInqData);
								return;
							}
						}
						// end defect 10339  

						//  added to destroy the KEY008 
						//  controller/dialog, since the controller will
						//  be removed upon the next call to the 
						//	Mediator
						close();
						// end defect 10290
						laGSD.setKey5(MFLogError.getErrorString());

						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							laGSD);
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
					// defect 10290 
					close();
					// end defect 10290 

					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							null);
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
	 * @param avPreviousControllers Vector 
	 * @param asTransCode String 
	 * @param asData Object  
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
				setFrame(new FrmPlateKeySelectionKEY008(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmPlateKeySelectionKEY008(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
