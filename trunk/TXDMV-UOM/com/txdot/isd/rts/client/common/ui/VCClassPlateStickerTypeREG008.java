package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.title.ui.TitleValidObj;
import com.txdot.isd.rts.services.data.RegistrationValidationData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * VCClassPlateStickerTypeREG008.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		12/08/2004	Comemnt out redundant code
 *  						modify processData() 
 *							defect 6198, Ver 5.1.5 Fix 2, 
 * J Rue		01/14/2004	Return code to previous state of 
 *							defect 6198
 *							Open defect 6794	
 * J Rue		01/03/2005	ChangeRegis() boolean is not used.
 *							JavaDoc/Formatting/Variable Name Cleanup
 *							modify processData()
 *							defect 6818 Ver 5.2.2
 * J Rue		02/01/2005	Set RegInvalidIndi = false for all DTA.
 *							add setInvPrcsCd()
 *							modify processData()
 *							defect 7934, Ver 5.2.2
 * Ray Rowehl	03/17/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * J Rue		04/15/2005	Add if statement to check if frame is 
 * 							visible
 * 							Clean up code
 * 							modify processData()
 * 							defect 7898 Ver 5.2.3
 * B Hargrove	04/26/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3  
 * B Hargrove	06/28/2005	Refactor\Move
 * 							RegistrationValidationData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * K Harrell	02/21/2007	Renaming to TitleValidObj.setInvProcsngCd()
 * 							for analysis.
 * 							renamed setInvPrcsCd() to setInvProcsngCd()
 * 							modify processData(), setInvProcsngCd()
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/27/2007	Remove reference to InvProcsngCd()
 * 							modify processData(), setInvProcsngCd()
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/04/2007	Begin work on Special Plate from REG008
 * 							modify processData()
 * 							defect 9085 Ver Special Plates
 * J Rue		03/08/2007	Add KEY002 processing
 * 							modify processData(), setView()
 * 							defect 9085 Ver Special Plates
 * J Rue		03/23/2007	Add REDIRECT processing
 * 							modify setView()
 * 							defect 9086 Ver Special Plates
 * J Rue		03/27/2007	Add code to handle Cancel from SPL002
 * 							modify setView()
 * 							defect 9086 Ver Special Plates
 * J Rue		03/29/2007	Make copy of VehInqData before getting MF
 * 							modify processData()
 * 							defect 9086 Ver Special Plates
 * B Hargrove	04/03/2007	For Special Plates, we come through
 * 						 	'REDIRECT' case (not 'ENTER') so need to
 * 							setVehClassOK to true.
 * 							modify processData()
 * 							defect 9126 Ver Special Plates
 * J Rue		04/13/2007	Check if previous screen = TTL040
 * 							modify processData()
 * 							defect 9086 Ver Special Plates
 * K Harrell	04/17/2007	Working.... 
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: REG008
 *
 * @version Special Plates	04/17/007
 * @author	Joseph Peters
 * <br>Creation Date: 		08/27/2001 13:06:15 
 *
 */
public class VCClassPlateStickerTypeREG008
	extends AbstractViewController
{
	// Objects
	VehicleInquiryData caVehInqData = new VehicleInquiryData();
	VehicleInquiryData caOrigVehData = new VehicleInquiryData();

	// Constants
	public static final int GET_SPCL_PLT = 20;
	public static final int REDIRECT = 21;

	// Vector
	Vector cvPreviousControllers = new Vector();

	/**
	 * VCClassPlateStickerTypeReg008 constructor comment.
	 */
	public VCClassPlateStickerTypeREG008()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 *
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.COMMON;
	}
	/**
	 * Handles data coming from their JDialogBox
	 *
	 * @param aiCommand int
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		VehicleInquiryData laVehData = new VehicleInquiryData();
		laVehData = (VehicleInquiryData) aaData;
		RegistrationValidationData laValData;

		Object laObjValidation = null;

		// Needed for Title
		if (aaData != null)
		{
			laObjValidation = laVehData.getValidationObject();
		}
		switch (aiCommand)
		{
			// defect 9086 / 9085 
			case GET_SPCL_PLT :
				{
					int liVctCnt = cvPreviousControllers.size();
					String lsPrevScr =
						(String) cvPreviousControllers.get(
							liVctCnt - 1);
					if (getTransCode().equals(TransCdConstant.PLTOPT))
					{
						processData(CANCEL, null);
						return;
					}
					// If MF Down 
					//   Title Transfer (TTL040)
					//   Exchange 
					else if (
						lsPrevScr.equals(ScreenConstant.TTL040)
							|| (lsPrevScr.equals(ScreenConstant.KEY001)
								&& getTransCode().equals(
									TransCdConstant.EXCH)))
					{
						processData(
							AbstractViewController.ENTER,
							aaData);
					}
					else
					{
						setNextController(ScreenConstant.KEY002);
						setDirectionFlow(AbstractViewController.NEXT);
						try
						{
							// Make a copy of the data
							caVehInqData =
								(
									VehicleInquiryData) UtilityMethods
										.copy(
									aaData);

							//	Add data to parameter
							getMediator().processData(
								getModuleName(),
								CommonConstant.NO_DATA_TO_BUSINESS,
								caVehInqData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
					}
					break;
				}
				// end defect 9086/9085
			case AbstractViewController.ENTER :
				{
					if (getTransCode().equals(TransCdConstant.PLTOPT))
					{
						processData(CANCEL, null);
						return;
					}

					// defect 7934
					//Set RegInvalidIndi = false for all DTA
					if (UtilityMethods.isDTA(getTransCode()))
					{
						laVehData
							.getMfVehicleData()
							.getRegData()
							.setRegInvldIndi(
							0);

						// Get InvProcsngCd with current plate code from
						// ItemCodesCache
						// defect 9085 
						// laVehData = setInvProcsngCd(laVehData);
						// end defect 9085 

					}
					// end defect 7936

					// Check MF Down
					//	 and NOT shouldGoPrevious()
					//	 and NOT DTA 
					//			or Title events
					//			or NoOfRecs = 0									
					if (laVehData.getMfDown() == 1
						&& !laVehData.shouldGoPrevious()
						&& (!(UtilityMethods
							.isDTA(
								getTransCode()) //	check for title when user picked texas 
					//	transfer during mf down								
							|| ((getTransCode()
								.equals(TransCdConstant.NONTTL)
								|| getTransCode().equals(
									TransCdConstant.TITLE)
								|| getTransCode().equals(
									TransCdConstant.REJCOR))
								&& laVehData.getNoMFRecs() == 0))))
					{
						setNextController(ScreenConstant.REG014);
						setDirectionFlow(AbstractViewController.NEXT);
					}
					else if (
						getTransCode().equals(TransCdConstant.RENEW)
							|| getTransCode().equals(
								TransCdConstant.EXCH))
					{
						laValData =
							(RegistrationValidationData) laVehData
								.getValidationObject();
						laValData.setVehClassOK(1);
						laVehData.setValidationObject(laValData);
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
					}
					///** 
					// * DTA:
					// * 1. Go to TTL004 if the record is not found in 
					//		MF
					// * 2. directionflow is always NEXT.
					// *
					// * If TTL/REJCOR:
					// * 	if no record found on MF, go to TTL004 as
					// *    the next screen.
					// *  if record found, go to TTL003 
					// *   (value of isFromTTL003 is always true in this
					// *  case) as the prev screen. 
					// */
					else if (
						(UtilityMethods.isDTA(getTransCode())
							|| getTransCode().equals(
								TransCdConstant.NONTTL)
							|| getTransCode().equals(
								TransCdConstant.TITLE)
							|| getTransCode().equals(
								TransCdConstant.REJCOR))
							&& (laVehData.getNoMFRecs() == 0))
					{
						// defect 6818
						//((TitleValidObj)laObjValidation).
						//	setChangeRegis(true);
						setNextController(ScreenConstant.TTL004);
						setDirectionFlow(AbstractViewController.NEXT);
					}
					///**
					// * DTA: 
					// * 1. go to TTL003 if record found on MF
					// * 2. if from TTL003, setdirectionflow = previous
					// *    if not from TTL003, setdirectionflow = next
					// */
					else if (
						(UtilityMethods.isDTA(getTransCode())
							|| getTransCode().equals(
								TransCdConstant.NONTTL)
							|| getTransCode().equals(
								TransCdConstant.TITLE)
							|| getTransCode().equals(
								TransCdConstant.REJCOR))
						//DTA, record found
							&& (laVehData.getNoMFRecs() > 0))
					{
						// defect 6818
						//((TitleValidObj)laObjValidation).
						//	setChangeRegis(true);
						setNextController(ScreenConstant.TTL003);
						// if the previous screen is TTL003, 
						//	just go to prev screen. 
						if (((TitleValidObj) laObjValidation)
							.isFromTTL003())
						{
							setDirectionFlow(
								AbstractViewController.PREVIOUS);
						}
						else
						{
							//else make it the next screen.
							setDirectionFlow(
								AbstractViewController.NEXT);
						}
						(
							(
								TitleValidObj) laObjValidation)
									.setFromTTL003(
							false);
					}
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							laVehData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					close(); 
					break;
				}
			case REDIRECT :
				{
					try
					{
						if (getTransCode()
							.equals(TransCdConstant.RENEW)
							|| getTransCode().equals(
								TransCdConstant.EXCH))
						{

							// For MF Down 
							int liCnt = cvPreviousControllers.size();
							String lsPrevScreen =
								(String) cvPreviousControllers.get(
									liCnt - 1);
							if (lsPrevScreen
								.equals(ScreenConstant.KEY001))
							{
								processData(
									AbstractViewController.ENTER,
									aaData);
							}
							else
							{
								// defect 9126
								// For Special Plates, we come through 
								// 'REDIRECT' case (not 'ENTER') so need to
								// setVehClassOK to true.
								laValData =
									(RegistrationValidationData) laVehData
										.getValidationObject();
								laValData.setVehClassOK(1);
								laVehData.setValidationObject(
									laValData);
								// end defect 9126
								setDirectionFlow(
									AbstractViewController.PREVIOUS);
								getFrame().setVisibleRTS(false);
								getMediator().processData(
									getModuleName(),
									CommonConstant.NO_DATA_TO_BUSINESS,
									aaData);
							}
						}
						else
						{
							// For Titles, call ENTER to set flow
							processData(
								AbstractViewController.ENTER,
								aaData);
						}

					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					laVehData =
						(VehicleInquiryData)
							(
								(FrmClassPlateStickerTypeREG008) getFrame())
							.getData();

					if (getTransCode().equals(TransCdConstant.PLTOPT))
					{
						setDirectionFlow(AbstractViewController.FINAL);
					}
					else if (
						laVehData.getNoMFRecs() == 0
							&& laVehData.getMfDown() == 1
							&& (getTransCode()
								.equals(TransCdConstant.RENEW)
								|| getTransCode().equals(
									TransCdConstant.EXCH)
								|| getTransCode().equals(
									TransCdConstant.REPL)
								|| getTransCode().equals(
									TransCdConstant.PAWT)
								|| getTransCode().equals(
									TransCdConstant.CORREG)
								|| getTransCode().equals(
									TransCdConstant.TAWPT)))
					{
						setDirectionFlow(AbstractViewController.FINAL);
					}
					else
					{
						setDirectionFlow(AbstractViewController.CANCEL);
					}
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
					close();
					break;
				}
		}
	}
	//	/**
	//	 * Find the Inventory Processing Code.
	//	 * 
	//	 * @param aaVehData VehicleInquiryData
	//	 * @return VehicleInquiryData
	//	 * @deprecated 
	//	 */
	//	public VehicleInquiryData setInvProcsngCd(VehicleInquiryData aaVehData)
	//	{
	//		MFVehicleData laMfVehData =
	//			(MFVehicleData) aaVehData.getMfVehicleData();
	//
	//		// Get inventory prcs code with current plate code 
	//		//	from ItemCodesCache
	//		ItemCodesData laItmCdData =
	//			(ItemCodesData) ItemCodesCache.getItmCd(
	//				laMfVehData.getRegData().getRegPltCd());
	//		if (laItmCdData != null)
	//		{
	//			int liInvPrcCd = laItmCdData.getInvProcsngCd();
	//			// defect 9085
	//			(
	//				(TitleValidObj) aaVehData
	//					.getValidationObject())
	//					.setInvProcsngCd(
	//				liInvPrcCd);
	//			// end defect 9085 
	//		}
	//		return aaVehData;
	//	}
	/**
	 * Set View checks if frame = null, set the screen and pass control
	 * to the controller.
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
			if (laRTSDB == null)
			{
				setFrame(
					new FrmClassPlateStickerTypeREG008(
						getMediator().getDesktop()));
			}
			else
			{
				setFrame(new FrmClassPlateStickerTypeREG008(laRTSDB));
			}
			caOrigVehData = (VehicleInquiryData) aaData;
			cvPreviousControllers = avPreviousControllers;
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		else if (aaData instanceof VehicleInquiryData)
		{
			processData(REDIRECT, aaData);
		}
		// defect 9086
		//	aaData is null means Cancelled from SPL002
		else if (aaData == null)
		{
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		// end defect 9086
	}
}
