package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.common.business.CommonUtil;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.MFLogError;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCInquiryKeySelectionKEY001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ---------------------------------------------------------------
 * Joe Peters   09/12/2001	Added Comments and intitialized screen items
 * N Ting		04/11/2002	Added Beep
 * 							defect 3444
 * N Ting		04/15/2002	In setView, add sticker switch
 * 							to redirect to REG002 for cancelled sticker.
 * 							defect 3446
 * MAbs			04/16/2002	Fixing dead screen after cancelled license
 * 							plate 
 * 							defect 3503
 * Taylor&Kwik	04/24/2002	Add test if sticker indi == 1 then display 
 * 							error 729.
 * 							defect 3650
 * MAbs			04/25/2002	Fix Refund showing wrong error number in 
 * 							odd circumstances
 * 							defect 3670
 * MAbs			04/29/2002	Fix Item Seized showing wrong error number 
 * 							in plate seized 
 * 							defect 3695
 * N Ting		05/14/2002	ProcessData(), MFVehicle.setDoNotBuildMVFunc
 * 							to true when record not found
 * 							defect 3901
 * J Kwik		05/24/2002	Handle registration modify events in mf 
 * 							unavailable mode.
 * 							defect 4087
 * MAbs			05/30/2002	Handle registration modify events in mf 
 * 							down mode
 * 							defect 4095
 * MAbs/TP		06/05/2002	MultiRecs in Archive 
 * 							defect 4019
 * MAbs			06/13/2002	Making sure Archive question pops up every
 * 							time
 * MAbs			06/14/2002	Making sure Archive question pops up every
 * 							time
 * Min Wang    	11/25/2002	Modified setView().
 * 							defect 5074
 * K Harrell   	05/01/2002  HQ Print Option 3 should print and return 
 * 							to Main Menu
 *                          method processData
 * 							defect 6009
 * Ray Rowehl	07/09/2003	Set setFromMF
 *							update assignMFDownData()
 *							defect 6011
 * Bob Brown    02/05/2004  Changed the processData method. Commented 
 * 							out ((FrmInquiryKeySelectionKEY001) frame)
 * 							.focusTxtField();
 *                          because that is now an empty method.
 *                          defect 6079 Ver 5.1.6 
 * Ray Rowehl	02/08/2005	Changed import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3 
 * Ray Rowehl	02/18/2005	Change reference to CommonClientUIConstants.
 * 							defect 7705 Ver 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3  
 * B Hargrove	06/27/2005	Refactor\Move 
 * 							RegistrationModifyData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * Ray Rowehl	07/29/2005	RTS 5.2.3 Code Cleanup
 * 							remove non-static references,
 * 							rename fields, organize imports,
 * 							format source.
 * 							defect 7885 Ver 5.2.3
 * T Pederson	10/20/2005	RTS 5.2.3 Code Cleanup
 * 							remove unused variables
 * 							defect 7885 Ver 5.2.3
 * K Harrell	12/22/2005	Pass search key variables for REFUND
 * 							Removed all references to StickerNo as key
 * 							add updateVehInqDataForRefund()
 * 							delete getCSTransCd(),setCSTransCd
 * 							modify assignMFDownData(),
 * 							createVehInqData(),processData(),setView() 
 * 							defect 8495 Ver 5.2.3
 * K Harrell	01/02/2006	Display error msg 57 if no record found on 
 * 							VehInq when HQ and search archive. 
 * 							modify processData()  
 * 							defect 7792 Ver 5.2.3  
 * Jeff S.		02/02/2006	Removed code that moved focus to the KEY001
 * 							input field when there was an error.  This
 * 							is handled in KEY001.
 * 							modify processData()
 * 							defect 7885 Ver 5.2.3 
 * Jeff S.		06/23/2006	Used screen constant for CTL001 Title.
 * 							remove ERRMSG_CONFIRM
 * 							modify processData()
 * 							defect 8756 Ver 5.2.3 
 * J Rue		02/13/2007	Add Special Plate nextController  SPL001
 *  						modify processData()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/23/2007	SP Cancel Plate record, display msg 729
 * 							return to KEY001
 * 							modify processData(), setView()
 * 							defect 9086 Ver Special Plates
 * K Harrell	02/25/2007	Added check for null data
 * 							modify  setView()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/01/2007	Add SPAPPL to those transactions where 
 * 							return to desktop. Modify to use 
 * 							SystemProperty.isHQ()
 * 							modify handleDBDown(), handleMainframeDown(),
 * 							  processData()
 * 							defect 9085 Ver Special Plates 
 * J Rue		04/25/2007	Replace setVisibleRTS() with cloase()
 * 							modify processData()	
 * 							defect 9086 8884 Ver Special Plates	
 * J Rue		05/01/2007	Add if KEY1 = SpclRegId to conditional for 
 * 							invalid search key.
 * 							modify processData()
 * 							defect 9086 Ver Special Plates
 * J Rue		05/15/2007	Move conditional to before Mult-Recs test
 * 							modify setVirw()
 * 							defect 8984 Ver Special Plates	
 * J Rue		05/15/2007	Add null check to CancelledRecord		
 * 							modify setView()
 * 							defect 8984 Ver Special Plates
 * J Rue		05/17/2007	Do not close frame on return from  INQ004
 * 							modify processData()
 * 							defect 9086 Ver Special Plates
 * K Harrell	06/01/2007	Use UtilityMethods.isSPAPPL() vs. check for
 * 							SPAPPL 
 * 							defect 9085 Ver Special Plates   
 * 							defect 9086 Ver Special Plates
 * J rue		06/01/2007	add getNoOfRecs = 1 to Archive and Cancel 
 * 							Plate
 * 							modify setView()
 * 							defect 9086 special Plates
 * J Rue		06/04/2007	add caCancelledRecord == null
 * 							modify setView()
 * 							defect 9105 Special Plates
 * J Rue		07/19/2007	Add check for partial records if Archive
 * 							and Cancel Plate was returned.
 * 							modify setView()
 * 							defect 9086 Ver Special Plates
 * K Harrell	05/29/2009	Throw Error on No Record Found if HQ & user 
 * 							doesn't have 'Legal Restraint No' Access. 
 *							Replaced all hard coded Err Msg nos. Sorted
 *							members. 
 * 							add LGLRESTRNTACCSREQD
 * 							add needLegalRestraintAccess() 
 * 							modify processData(), handleDBDown(),
 * 							 handleMainframeDown(), setView() 
 * 							defect 10036 Ver Defect_POS_F
 * B Hargrove	06/09/2009	Remove all 'Cancelled Sticker' references.
 * 							modify processData(), setView()
 * 							delete CANC_STKR
 * 							defect 9953 Ver Defect_POS_F
 * K Harrell	06/29/2009	Accommodate new handling of LienholderData,
 * 							OwnerData 
 * 							modify createInquiryObject() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	04/09/2010	Remove unused code/constant 
 * 							delete REQD_PLT
 * 							modify processData() 
 * 							defect 10430 Ver POS_640
 * K Harrell	09/22/2010	Update for presentation of in-process trans
 * 							add INPROCS_TRANS 
 * 							modify setView(), processData()
 * 							defect 10598 Ver 6.6.0  
 * R Pilon		06/10/2011	Implement Special Plate Inquiry
 * 							modify handleMainframeDown(), processData(),
 * 							  setView()
 * 							defect 10820 Ver 6.8.0
 * K Harrell	11/14/2011	Implement VTR 275 
 * 							Print Only is no longer available
 * 							modify processData() 
 * 							defect 11052 Ver 6.9.0 
 * B Woodson   	11/22/2011  add assignFromGSD() 
 * 							modify processData()
 *                          defect 11052 Ver 6.9.0 
 * K Harrell	12/08/2011	modify to handle no rcd found for Special 
 * 							Plate
 * 							modify processData(), assignFromGSD()
 * 							defect 11052 Ver 6.9.0 
 * B Woodson	02/10/2012	modify processData()
 * 							defect 11228 Ver 6.10.0
 * K Harrell	02/23/2012 	Null Pointer Exception in Special Plate 
 * 							Application when specify "No Vehicle" 
 * 							modify processData() 
 * 							defect 11228 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Controller for FrmInquiryKeySelectionKEY001
 * 
 * @version 6.10.0 			02/23/2012
 * @author  Joseph Peters
 * <br>Creation Date:		08/27/2001 13:19:47
 */

public class VCInquiryKeySelectionKEY001 extends AbstractViewController
{
	public static final int MF_DOWN = 20;
	public static final int MULT_RECS = 21;
	public static final int NO_REC_FOUND = 22;
	public static final int SEARCH_ARCHIVE = 23;
	public static final int SINGLE_REC = 24;
	public static final int ARCHIVE_REC_FOUND = 25;
	public static final int GET_SAVED_VEHICLE = 26;

	// defect 10598 
	public static final int INPROCS_TRANS = 27;
	// end defect 10598 

	private static final String CANC_PLT = " ** CANCELED PLATE **";

	private static final String ERRMSG_REC_NOT_FOUND =
		"Record not found in Active or Inactive file.\nDo you want"
			+ " to check Archive file?\n";

	// defect 10036
	private static final String LGLRESTRNTACCSREQD =
		"  <br><br>User must have 'Legal Restraint No' access to continue.";
	// end defect 10036 

	private static final int ONE_HUNDRED = 100;

	private static final String STR_EQL_SIGN = "=";

	private CommonUtil caUtil = new CommonUtil();

	private VehicleInquiryData caCancelledRecord =
		new VehicleInquiryData();

	private GeneralSearchData caGeneralSearchData = null;

	private RegistrationModifyData caRegModData =
		new RegistrationModifyData();

	// boolean 
	private boolean cbAlreadyAskedArchive;

	// int
	public int ciMfSearched = 0;

	// String 
	private String csTransCd;

	/**
	 * VCInquiryKeySelectionKEY001 constructor.
	 */
	public VCInquiryKeySelectionKEY001()
	{
		super();
	}

	/**
	 * Responsible for Assigning inquiry info to data object
	 * 
	 * @param aaInqData VehicleInquiryData
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData assignMFDownData(VehicleInquiryData aaInqData)
	{
		if (aaInqData.getMfVehicleData() == null)
		{
			aaInqData = createInquiryObject(aaInqData);
		}
		if (caGeneralSearchData
			.getKey1()
			.equals(CommonConstant.REG_PLATE_NO))
		{
			aaInqData.getMfVehicleData().getRegData().setRegPltNo(
				caGeneralSearchData.getKey2());
		}
		else if (
			caGeneralSearchData.getKey1().equals(CommonConstant.VIN))
		{
			aaInqData.getMfVehicleData().getVehicleData().setVin(
				caGeneralSearchData.getKey2());
		}
		else if (
			caGeneralSearchData.getKey1().equals(
				CommonConstant.DOC_NO))
		{
			aaInqData.getMfVehicleData().getTitleData().setDocNo(
				caGeneralSearchData.getKey2());
		}

		RTSDate laRTSEffDt = new RTSDate();
		aaInqData.setRTSEffDt(laRTSEffDt.getYYYYMMDDDate());

		// defect 6011
		// set FromMF to false to indicate that MF was down
		aaInqData.getMfVehicleData().setFromMF(false);
		// end defect 6011

		return aaInqData;
	}
	/** 
	 * Assign search parameter to VehicleInquiryData object
	 * 
	 * @param aaVehInqData
	 */
	private void assignFromGSD(VehicleInquiryData aaVehInqData)
	{
		if (caGeneralSearchData != null)
		{
			if (csTransCd.equals(TransCdConstant.SPINQ))
			{
				aaVehInqData.setMfVehicleData(new MFVehicleData()); 
				aaVehInqData.getMfVehicleData().setRegData(new RegistrationData()); 
				aaVehInqData.getMfVehicleData().setSpclPltRegisData(new SpecialPlatesRegisData()); 
				aaVehInqData.getMfVehicleData().getSpclPltRegisData().setRegPltNo(caGeneralSearchData.getKey2());
				aaVehInqData.getMfVehicleData().getSpclPltRegisData().setOwnrData(new OwnerData());
				aaVehInqData.getMfVehicleData().setSPRecordOnlyVehInq(true);
				aaVehInqData.getMfVehicleData().setVehicleData(new VehicleData());
				aaVehInqData.getMfVehicleData().getVehicleData().setVin(new String());
			}
			else
			{
				if (caGeneralSearchData
						.getKey1()
						.equals(CommonConstant.REG_PLATE_NO))
				{
					aaVehInqData.getMfVehicleData().getRegData().setRegPltNo(
							caGeneralSearchData.getKey2());
				}
				else if (
						caGeneralSearchData.getKey1().equals(CommonConstant.VIN))
				{
					aaVehInqData.getMfVehicleData().getVehicleData().setVin(
							caGeneralSearchData.getKey2());
				}
				else if (
						caGeneralSearchData.getKey1().equals(
								CommonConstant.DOC_NO))
				{
					aaVehInqData.getMfVehicleData().getTitleData().setDocNo(
							caGeneralSearchData.getKey2());
				}
			}
		}
	}

	/**
	 * Creates Inquiry Object
	 * 
	 * @param aaVehData VehicleInquiryData
	 * @return VehicleInquiryData 
	 */
	private VehicleInquiryData createInquiryObject(VehicleInquiryData aaVehData)
	{
		MFVehicleData laMFVehData = new MFVehicleData();
		RegistrationData laRegData = new RegistrationData();
		TitleData laTTLData = new TitleData();
		AddressData laRNWLAddrData = new AddressData();
		AddressData laTTLVehAddrData = new AddressData();
		OwnerData laOwnrData = new OwnerData();
		VehicleData laVehicleData = new VehicleData();
		Object laValidationObject = new Object();
		laRegData.setRenwlMailAddr(laRNWLAddrData);
		laTTLData.setTtlVehAddr(laTTLVehAddrData);
		laMFVehData.setRegData(laRegData);
		laMFVehData.setTitleData(laTTLData);
		laMFVehData.setVehicleData(laVehicleData);
		laMFVehData.setOwnerData(laOwnrData);
		laMFVehData.setVehicleData(laVehicleData);

		// defect 4087
		if (aaVehData.getValidationObject() == null)
		{
			if (caRegModData != null)
			{
				aaVehData.setValidationObject(caRegModData);
			}
			else
				aaVehData.setValidationObject(laValidationObject);
		}
		// end defect 4087
		aaVehData.setMfVehicleData(laMFVehData);
		return aaVehData;
	}

	/**
	 * Create Vehicle Inquiry Data
	 * 
	 * @param aaGeneralSearchData GeneralSearchData
	 * @return VehicleInquiryData 
	 */
	public VehicleInquiryData createVehInqData(GeneralSearchData aaGeneralSearchData)
	{
		VehicleInquiryData laVehicleInquiryData =
			new VehicleInquiryData();
		MFVehicleData laMFVehicleData = new MFVehicleData();
		RegistrationData laRegistrationData = new RegistrationData();
		laMFVehicleData.setRegData(laRegistrationData);
		VehicleData laVehicleData = new VehicleData();
		laMFVehicleData.setVehicleData(laVehicleData);
		laVehicleInquiryData.setMfVehicleData(laMFVehicleData);
		TitleData laTitleData = new TitleData();
		laMFVehicleData.setTitleData(laTitleData);
		if (aaGeneralSearchData
			.getKey1()
			.equals(CommonConstant.REG_PLATE_NO))
		{
			laRegistrationData.setRegPltNo(
				aaGeneralSearchData.getKey2());
		}
		else if (
			aaGeneralSearchData.getKey1().equals(CommonConstant.VIN))
		{
			laVehicleData.setVin(aaGeneralSearchData.getKey2());
		}
		else if (
			aaGeneralSearchData.getKey1().equals(
				CommonConstant.DOC_NO))
		{
			laTitleData.setDocNo(aaGeneralSearchData.getKey2());
		}
		return laVehicleInquiryData;
	}

	/**
	 * Returns the Module name constant used by the RTSMediator to pass
	 * the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.COMMON;
	}

	/**
	 * Sets error message and navigation for DB Down scenario
	 * 
	 * @param aaInqData VehicleInquiryData
	 */
	public void handleDBDown(VehicleInquiryData aaInqData)
	{
		setNextController(ScreenConstant.REG008);
		setDirectionFlow(AbstractViewController.NEXT);
		try
		{
			// Throw exception for mainframe down
			if (getTransCode().equals(TransCdConstant.CCO)
				|| getTransCode().equals(TransCdConstant.CCDO)
				|| UtilityMethods.isSPAPPL(getTransCode()))
			{
				// new RTSException(1)
				RTSException leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_RECORD_RETRIEVAL_DOWN);

				leRTSEx.displayError(getFrame());
				processData(AbstractViewController.CANCEL, null);
				return;
			}
			else
			{
				// new RTSException(20);
				RTSException leRTSEx =
					new RTSException(
						ErrorsConstant
							.ERR_NUM_RECORD_CANNOT_BE_RETRIEVED_CONTINUE);
				leRTSEx.displayError(getFrame());
			}
			aaInqData = assignMFDownData(aaInqData);

			getMediator().processData(
				getModuleName(),
				CommonConstant.NO_DATA_TO_BUSINESS,
				aaInqData);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}

	/**
	 * Sets error message and navigation for Mainframe Down scenario
	 * 
	 * @param aaInqData VehicleInquiryData
	 */
	public void handleMainframeDown(VehicleInquiryData aaInqData)
	{
		setNextController(ScreenConstant.REG008);
		setDirectionFlow(AbstractViewController.NEXT);
		try
		{
			//Throw exception for mainframe down
			if (getTransCode().equals(TransCdConstant.DUPL)
				|| getTransCode().equals(TransCdConstant.ADDR)
				|| getTransCode().equals(TransCdConstant.CCO)
				|| getTransCode().equals(TransCdConstant.CCDO)
				|| getTransCode().equals(TransCdConstant.STAT)
				|| getTransCode().equals(TransCdConstant.VEHINQ)
				|| getTransCode().equals(TransCdConstant.REFUND)
				|| getTransCode().equals(TransCdConstant.HOTCK)
				|| getTransCode().equals(TransCdConstant.HOTDED)
				|| getTransCode().equals(TransCdConstant.CKREDM)
				|| getTransCode().equals(TransCdConstant.HCKITM)
				// defect 10820
				// special plates inquiry - temporary trans code 
				|| getTransCode().equals(TransCdConstant.SPINQ)
				// end defect 10820
				|| UtilityMethods.isSPAPPL(getTransCode()))
			{
				//new RTSException(01); 
				RTSException leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_RECORD_RETRIEVAL_DOWN);
				leRTSEx.displayError(getFrame());
				processData(AbstractViewController.CANCEL, null);
				return;
			}
			else
			{
				// new RTSException(20);
				RTSException leRTSEx =
					new RTSException(
						ErrorsConstant
							.ERR_NUM_RECORD_CANNOT_BE_RETRIEVED_CONTINUE);
				leRTSEx.displayError(getFrame());
			}

			// has no effect
			aaInqData = assignMFDownData(aaInqData);
			getMediator().processData(
				getModuleName(),
				CommonConstant.NO_DATA_TO_BUSINESS,
				aaInqData);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}

	/**
	 * Need Legal Restraint Access
	 * 
	 * @param abHQ
	 * @return boolean
	 */
	private boolean needLegalRestraintAccess(boolean abHQ)
	{
		boolean lbNeedLglRstrntAccs = false;
		if (abHQ && csTransCd.equals(TransCdConstant.STAT))
		{
			SecurityData laSecurityData =
				getMediator().getDesktop().getSecurityData();

			lbNeedLglRstrntAccs =
				laSecurityData.getLegalRestrntNoAccs() == 0;
		}
		if (lbNeedLglRstrntAccs)
		{
			String[] larrStrArray = new String[1];
			larrStrArray[0] = LGLRESTRNTACCSREQD;
			RTSException leRTSEx =
				new RTSException(
					ErrorsConstant.ERR_NUM_NO_RECORD_FOUND,
					larrStrArray);
			leRTSEx.displayError();

		}
		return lbNeedLglRstrntAccs;
	}

	/**
	 * Controls the screen flow from KEY001.  It passes the data to the
	 * RTSMediator.
	 * 
	 * @param aiCommand int 
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		boolean lbHQ = SystemProperty.isHQ();

		switch (aiCommand)
		{
			// defect 10598
			case INPROCS_TRANS :
				{
					setNextController(ScreenConstant.INQ002);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						setData(aaData);
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// end defect 10598 
			case AbstractViewController.ENTER :
				//validation rules will be in cGeneralSearchData.IntKey2
				//1 - Validate that the user wants a 7 digit Plate No, 
				//  builds confirmation screen from Plate No
				//2 - User entered search criteria of incorrect length 
				//  (ERR150)
				{
					caGeneralSearchData = (GeneralSearchData) aaData;
					if (caCancelledRecord != null)
					{
						caCancelledRecord.setMfVehicleData(null);
					}
					setDirectionFlow(AbstractViewController.CURRENT);

					try
					{

						// defect 10820
						// commented out validation check below and
						// changed the validation to use RTSException
//						if (caGeneralSearchData.getIntKey2()
//							== CommonClientUIConstants.INCORRECT_KEY
//							&& caGeneralSearchData.getKey1()
//								!= CommonConstant.SPCL_REG_ID)
//						{
//							FrmInquiryKeySelectionKEY001 laFrmKey001 =
//								(FrmInquiryKeySelectionKEY001) getFrame();
//							laFrmKey001.dispInvalidFieldEntryExp();
//							return;
//						}
						// end defect 10820

						// set flag so that system knows Mainframe
						// has been searched
						ciMfSearched = 1;
						//Send Trans Code to the Server
						caGeneralSearchData.setKey3(csTransCd);
						caGeneralSearchData.setKey5(
							MFLogError.getErrorString());
						getMediator().processData(
							getModuleName(),
							CommonConstant.GET_VEH,
							caGeneralSearchData);
					}
					catch (RTSException aeRTSEx)
					{
						if (aeRTSEx
							.getMsgType()
							.equals(RTSException.SERVER_DOWN)
							|| aeRTSEx.getMsgType().equals(
								RTSException.MF_DOWN))
						{
							VehicleInquiryData laInqData =
								new VehicleInquiryData();
							laInqData.setMfDown(1);
							handleMainframeDown(laInqData);
						}
						else if (
							aeRTSEx.getMsgType().equals(
								RTSException.DB_DOWN))
						{
							VehicleInquiryData laInqData =
								new VehicleInquiryData();
							handleDBDown(laInqData);
						}
						else
							aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
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
			case VCInquiryKeySelectionKEY001.MF_DOWN :
				{
					VehicleInquiryData laVehData =
						new VehicleInquiryData();
					laVehData = (VehicleInquiryData) aaData;
					handleMainframeDown(laVehData);
					close();
					break;
				}
			case VCInquiryKeySelectionKEY001.MULT_RECS :
				{
					setNextController(ScreenConstant.INQ004);
					setDirectionFlow(AbstractViewController.NEXT);
					VehicleInquiryData laVehData =
						new VehicleInquiryData();
					laVehData = (VehicleInquiryData) aaData;

					if (laVehData.getPartialDataList() != null
						&& !laVehData.getPartialDataList().isEmpty()
						&& laVehData.getPartialDataList().firstElement()
							instanceof MFPartialData)
					{
						if (laVehData.getPartialDataList().size()
							> ONE_HUNDRED)
						{
							// new RTSException(148);
							RTSException leRTSEx =
								new RTSException(
									ErrorsConstant
										.ERR_NUM_TOO_MANY_RCDS_FOUND);
							leRTSEx.displayError(getFrame());
							break;
						}
					}
					try
					{
						setData(aaData);
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCInquiryKeySelectionKEY001.GET_SAVED_VEHICLE :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						setData(aaData);
						getMediator().processData(
							getModuleName(),
							CommonConstant.GET_SAVED_VEH,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					close();
					break;
				}
			case VCInquiryKeySelectionKEY001.NO_REC_FOUND :
				{
					//defect 11052
					VehicleInquiryData laVehData = (VehicleInquiryData) aaData; 
					if (laVehData.isVTR275PrintOptions())
					{
						assignFromGSD(laVehData);
						setNextController(csTransCd.equals(TransCdConstant.SPINQ) ?
								ScreenConstant.SPL002 :	ScreenConstant.REG003);
						setDirectionFlow(AbstractViewController.NEXT);

						try
						{
							setData(aaData);
							getMediator().processData(
								getModuleName(),
								CommonConstant.NO_DATA_TO_BUSINESS,
								aaData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						return;
					}
					//end defect 11052
					
					setDirectionFlow(AbstractViewController.CURRENT);
					RTSException leRTSEx = null;

					// defect 10036
					// Should not proceed further if HQ, No Record Found, 
					//  no access to LegalRestraint
					if (!needLegalRestraintAccess(lbHQ))
					{
						// end defect 10036 

						// defect 7792
						// Error Msg 211 if HQ && !(CC0 || VEHINQ || SPAPPL) 
						if (lbHQ
							&& !(getTransCode()
								.equals(TransCdConstant.CCO)
								|| getTransCode().equals(
									TransCdConstant.VEHINQ)
								// defect 10820
								|| getTransCode().equals(
									TransCdConstant.SPINQ)
								// end defect 10820
								|| UtilityMethods.isSPAPPL(
									getTransCode())))
						{
							// new RTSException (211);
							leRTSEx =
								new RTSException(
									ErrorsConstant
										.ERR_NUM_NO_RECORD_FOUND_CONTINUE);
						}
						else
						{
							// new RTSException(57); 
							leRTSEx =
								new RTSException(
									ErrorsConstant
										.ERR_NUM_NO_RECORD_FOUND);
						}
						// end defect 7792
						leRTSEx.displayError(getFrame());
						if (getTransCode()
							.equals(TransCdConstant.REFUND))
						{
							try
							{
								VehicleInquiryData laVehicleInquiryData =
									(VehicleInquiryData) aaData;
								// defect 8495
								// Assign search key values 
								updateVehInqDataForRefund(laVehicleInquiryData);
								// end defect 8495
								setNextController(
									ScreenConstant.INQ007);
								close();
								setDirectionFlow(
									AbstractViewController.NEXT);
								getMediator().processData(
									getModuleName(),
									CommonConstant.NO_DATA_TO_BUSINESS,
									laVehicleInquiryData);
							}
							catch (RTSException aeRTSEx2)
							{
								aeRTSEx2.displayError(getFrame());
							}
						}
						else if (
							csTransCd.equals(TransCdConstant.STAT))
						{
							try
							{
								if (lbHQ)
								{
									setNextController(
										ScreenConstant.TTL006);
									setDirectionFlow(
										AbstractViewController.NEXT);
									getMediator().processData(
										getModuleName(),
										CommonConstant
											.NO_DATA_TO_BUSINESS,
										createVehInqData(caGeneralSearchData));
								}
								else
								{
									setDirectionFlow(
										AbstractViewController.FINAL);
									getMediator().processData(
										getModuleName(),
										CommonConstant
											.NO_DATA_TO_BUSINESS,
										null);
								}
							}
							catch (RTSException aeRTSEx2)
							{
								aeRTSEx2.displayError(getFrame());
							}
						}
						else if (
							getTransCode().equals(
								TransCdConstant.VEHINQ))
						{
							setDirectionFlow(
								AbstractViewController.FINAL);
							try
							{
								getMediator().processData(
									getModuleName(),
									CommonConstant.NO_DATA_TO_BUSINESS,
									aaData);
							}
							catch (RTSException aeRTSEx2)
							{
								aeRTSEx2.displayError(getFrame());
							}
							close();
						}
					}
					break;
				}
			case VCInquiryKeySelectionKEY001.SEARCH_ARCHIVE :
				{
					try
					{
						VehicleInquiryData laInqData =
							(VehicleInquiryData) aaData;
						if (laInqData
							.getMfVehicleData()
							.getRegData()
							.getCancPltIndi()
							!= 1)
						{
							int liRet = 0;
							if (!cbAlreadyAskedArchive)
							{
								RTSException leRTSExQuestion =
									new RTSException(
										RTSException
											.DECISION_VERIFICATION,
										ERRMSG_REC_NOT_FOUND
											+ caGeneralSearchData
												.getKey1()
											+ STR_EQL_SIGN
											+ caGeneralSearchData
												.getKey2(),
										ScreenConstant
											.CTL001_FRM_TITLE);
								leRTSExQuestion.setBeep(
									RTSException.BEEP);
								liRet =
									leRTSExQuestion.displayError(
										getFrame());
								caGeneralSearchData.setIntKey4(1);
								cbAlreadyAskedArchive = true;
							}
							else
							{
								liRet = RTSException.YES;
							}
							if (liRet == RTSException.YES)
							{
								setDirectionFlow(
									AbstractViewController.CURRENT);
								// Sets key to indicate to business layer
								// to search archive
								caGeneralSearchData.setIntKey2(
									CommonConstant.SEARCH_ARCHIVE);
							}
							else if (liRet == RTSException.NO)
							{
								cbAlreadyAskedArchive = false;
								if (csTransCd
									.equals(TransCdConstant.REFUND))
								{
									setNextController(
										ScreenConstant.INQ007);
									setDirectionFlow(
										AbstractViewController.NEXT);
									close();
									// defect 8495
									// Assign search key values 
									updateVehInqDataForRefund(laInqData);
									// end defect 8495
									try
									{
										getMediator().processData(
											getModuleName(),
											CommonConstant
												.NO_DATA_TO_BUSINESS,
											laInqData);
									}
									catch (RTSException aeRTSEx)
									{
										aeRTSEx.displayError(
											getFrame());
									}
									return;
								}

								// new RTSException(57);
								RTSException leRTSExMsg =
									new RTSException(
										ErrorsConstant
											.ERR_NUM_NO_RECORD_FOUND);

								leRTSExMsg.displayError(getFrame());
								return;
							}
							else if (liRet == RTSException.CANCEL)
							{
								cbAlreadyAskedArchive = false;
								setDirectionFlow(
									AbstractViewController.CANCEL);
								return;
							}
						}
						else
						{
							setDirectionFlow(
								AbstractViewController.CURRENT);
							// Sets key to indicate to business layer to 
							// search archive
							caGeneralSearchData.setIntKey2(
								CommonConstant.SEARCH_ARCHIVE);
						}
						caGeneralSearchData.setKey5(
							MFLogError.getErrorString());
						cbAlreadyAskedArchive = false;
						getMediator().processData(
							getModuleName(),
							CommonConstant.GET_VEH,
							caGeneralSearchData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCInquiryKeySelectionKEY001.ARCHIVE_REC_FOUND :
				{
					setNextController(ScreenConstant.CTL005);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						Vector lvCancelledArchiveInfo = new Vector();
						lvCancelledArchiveInfo.addElement(
							caCancelledRecord);
						lvCancelledArchiveInfo.addElement(
							(VehicleInquiryData) aaData);
						lvCancelledArchiveInfo.addElement(
							caGeneralSearchData);
						caCancelledRecord = null;
						//Clear out Cancelled record info
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							lvCancelledArchiveInfo);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCInquiryKeySelectionKEY001.SINGLE_REC :
				{
					// defect 6009 
					VehicleInquiryData laVehData =
						new VehicleInquiryData();
					laVehData = (VehicleInquiryData) aaData;

					// defect 11228
					if ((!UtilityMethods.isVehInqTransCd(getTransCode())) &&
							laVehData != null && 
							laVehData.getMfVehicleData() != null &&
							laVehData.getMfVehicleData().getTitleData() != null &&
							laVehData.getMfVehicleData()
							.getTitleData()
							.getExportIndi() == 1)
					{
						RTSException leRTSEx1 = new RTSException(
								ErrorsConstant.ERR_NUM_ACTION_INVALID_EXPORT); 
						leRTSEx1.displayError(getFrame()); 
						break;
					}
					// end defect 11228
					
					if (UtilityMethods.isSPAPPL(getTransCode()))
					{
						// Cancel Plate returned, display message 
						// "PLATE IS CANCELLED. PROCESSING CANNOT 
						//	CONTINUE."
						if (laVehData != null
							&& (laVehData
								.getMfVehicleData()
								.getRegData()
								.getCancPltIndi()
								== 1))
						{
							RTSException leRTSExHolder3 = null;

							// new RTSException(729);
							leRTSExHolder3 =
								new RTSException(
									ErrorsConstant
										.ERR_NUM_PLT_CANCELLED);
							leRTSExHolder3.displayError(getFrame());
							break;
						}
						else
						{
							// Continue to next screen SPL001	
							setNextController(ScreenConstant.SPL001);
						}
					}
					else
					{
						// defect 11052 
						// Use VehicleInquiryData Constant 
						//	if ((getTransCode()
						//		.equals(TransCdConstant.VEHINQ)
						//		|| (getTransCode()
						//			.equals(TransCdConstant.SPINQ)))
						//		&& lbHQ
						//		&& laVehData.getPrintOptions()
						//			== VehicleInquiryData.PRINT)
						//	{
						//		setDirectionFlow(
						//			AbstractViewController.FINAL);
						//		try
						//		{
						//			CompleteTransactionData laCompTransData =
						//				new CompleteTransactionData();
						//			laCompTransData.setOrgVehicleInfo(
						//				laVehData.getMfVehicleData());
						//			laCompTransData.setVehicleInfo(
						//				laVehData.getMfVehicleData());
						//			laCompTransData.setTransCode(csTransCd);
						//			laCompTransData.setPrintOptions(
						//				laVehData.getPrintOptions());
						//
						//			// defect 10820
						//			// special plates inquiry - temporary 
						//			// trans code 
						//			if (getTransCode()
						//				.equals(TransCdConstant.SPINQ))
						//			{
						//				// set the trans code to vehicle 
						//				// inquiry (VEHINQ) for processing 
						//				// fees and storing trans in DB
						//				laCompTransData.setTransCode(
						//					TransCdConstant.VEHINQ);
						//			}
						//			// end defect 10820
						//		
						//			getMediator().processData(
						//				GeneralConstant.COMMON,
						//				CommonConstant.ADD_TRANS,
						//				laCompTransData);
						//		}
						//		catch (RTSException aeRTSEx)
						//		{
						//			aeRTSEx.displayError(getFrame());
						//		}
						//		break;
						//	}
						//	else 
						// end defect 11052 
						if (laVehData.getSpecialOwner() == 1)
						{
							if (getTransCode()
								.equals(TransCdConstant.VEHINQ)
								|| getTransCode().equals(
									TransCdConstant.DRVED))
							{
								setNextController(
									ScreenConstant.REG002);
							}
							else if (
								laVehData
									.getMfVehicleData()
									.getRegData()
									.getCancPltIndi()
									== 1)
							{
								if (csTransCd
									.equals(TransCdConstant.REFUND))
								{
									// new RTSException(57); 
									RTSException leRTSExMsg =
										new RTSException(
											ErrorsConstant
												.ERR_NUM_NO_RECORD_FOUND);
									leRTSExMsg.displayError(getFrame());
									setNextController(
										ScreenConstant.INQ007);
									setDirectionFlow(
										AbstractViewController.NEXT);
									getFrame().setVisibleRTS(false);
									try
									{
										getMediator().processData(
											getModuleName(),
											CommonConstant
												.NO_DATA_TO_BUSINESS,
											laVehData);
									}
									catch (RTSException aeRTSEx)
									{
										aeRTSEx.displayError(
											getFrame());
									}
									break;
								}
								else
								{
									// defect 2454
									// defect 10036 
									// Consolidate HCKITM in following if
									// Use ErrorsConstant 
									RTSException leRTSExHolder2 = null;
									if (getTransCode()
										.equals(TransCdConstant.HOTCK)
										|| getTransCode().equals(
											TransCdConstant.HOTDED)
										|| getTransCode().equals(
											TransCdConstant.CKREDM)
										|| getTransCode().equals(
											TransCdConstant.HCKITM))
									{
										// new RTSException(537)
										leRTSExHolder2 =
											new RTSException(
												ErrorsConstant
													.ERR_NUM_PLT_OR_STKR_CANCELLED);
									}
									else
									{
										// new RTSException(729)
										leRTSExHolder2 =
											new RTSException(
												ErrorsConstant
													.ERR_NUM_PLT_CANCELLED);
									}
									leRTSExHolder2.displayError(
										getFrame());
									break;
								}
							}
							else
							{
								RTSException leRTSExHolder2 = null;

								if (csTransCd
									.equals(TransCdConstant.REFUND))
								{
									// new RTSException(57); 
									leRTSExHolder2 =
										new RTSException(
											ErrorsConstant
												.ERR_NUM_NO_RECORD_FOUND);
								}
								else
								{
									// new RTSException(149);
									leRTSExHolder2 =
										new RTSException(
											ErrorsConstant
												.ERR_NUM_TRANS_INVALID_FOR_SPCL_REG);
								}
								leRTSExHolder2.displayError(getFrame());
								break;
							}
						}
						else if (
							UtilityMethods.isSPAPPL(getTransCode()))
						{
							setNextController(ScreenConstant.SPL001);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.REJCOR))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.CORREG))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.DUPL))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.EXCH))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.VEHINQ))
						{
							MFVehicleData laMfVehData =
								(MFVehicleData) UtilityMethods.copy(
									(
										(MFVehicleData)
											(
												(VehicleInquiryData) aaData)
										.getMfVehicleData()));

							// Record found on Special Plate
							//	only file, display INQ005
							if (laMfVehData.getRegData() == null
								|| laMfVehData.getRegData().getRegPltNo()
									== null)
							{
								setNextController(
									ScreenConstant.INQ005);
							}
							else
							{
								// Record found on Title/Regis and
								//	possibly Special Plate files
								setNextController(
									ScreenConstant.REG003);
							}
						}
						else if (
							getTransCode().equals(
								TransCdConstant.PAWT))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.TAWPT))
						{
							setNextController(ScreenConstant.MRG010);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.STAT))
						{
							setNextController(ScreenConstant.TTL006);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.RENEW))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.REPL))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.SBRNW))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.ADDR))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(TransCdConstant.CCO))
						{
							setNextController(ScreenConstant.TTL018);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.HOTCK))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.REFUND))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.CKREDM))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.HOTDED))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.HCKITM))
						{
							setNextController(ScreenConstant.REG003);
						}
						// defect 10820
						// special plates inquiry - temporary trans code 
						else if (
							getTransCode().equals(
								TransCdConstant.SPINQ))
						{
							setNextController(ScreenConstant.SPL002);
						}
						// end defect 10820
					}
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						setData(aaData);
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					if (!getTransCode().equals(TransCdConstant.CCO))
					{
						close();
					}
					break;
				}
		}
	}

	/**
	 * Creates the actual frame, stores the protected variables 
	 * needed by the VC, and sends the data to the frame.
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
		if (aaData instanceof RegistrationModifyData || aaData == null)
		{
			if (aaData instanceof RegistrationModifyData)
			{
				caRegModData = (RegistrationModifyData) aaData;
			}
			setTransCode(asTransCode);
			if (Transaction.getCumulativeTransIndi() == 1)
			{
				processData(GET_SAVED_VEHICLE, aaData);
			}
			// If this is not a cumulative transaction go ahead and
			// set the frame.
			else if (getFrame() == null)
			{
				RTSDialogBox laRTSDB = getMediator().getParent();
				if (laRTSDB != null)
				{
					setFrame(new FrmInquiryKeySelectionKEY001(laRTSDB));
				}
				else
				{
					setFrame(
						new FrmInquiryKeySelectionKEY001(
							getMediator().getDesktop()));
				}
				csTransCd = asTransCode;
				super.setView(
					avPreviousControllers,
					asTransCode,
					aaData);
			}
			return;
		}
		//If mainframe has not been searched yet, set the Frame
		else if (getFrame() == null)
		{
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmInquiryKeySelectionKEY001(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmInquiryKeySelectionKEY001(
						getMediator().getDesktop()));
			}
			csTransCd = asTransCode;
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		// Get record from MF
		else if (aaData instanceof GeneralSearchData)
		{
			processData(ENTER, aaData);
		}
		// If mainframe has been searched, VehicleInquiryData is 
		// returned and processing for next screen is run
		else
		{
			VehicleInquiryData laInqData = (VehicleInquiryData) aaData;
			if (caRegModData != null)
			{
				laInqData.setValidationObject(caRegModData);
			}
			if (laInqData.getMfDown() == 1)
			{
				processData(MF_DOWN, laInqData);
			}
			// defect 8984
			//	Move conditional to before Mult-Recs test
			// Archive searched and Cancel Plate found
			// defect 9086
			//	Add getNoMFRecs() == 1, this will display CTL005
			//	Add laInqData.getPartialDataList().size() > 0
			//	  This picks up Multiple Archive records with a Cancel 
			//	  Plate 
			else if (
				(laInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ARCHIVE)
					&& (laInqData.getNoMFRecs() == 1
						|| laInqData.getPartialDataList().size() > 0)
					&& (caCancelledRecord.getMfVehicleData() != null
						&& caCancelledRecord
							.getMfVehicleData()
							.getRegData()
							.getCancPltIndi()
							== 1))
			{ // end defect 9086

				//defect 10112 
				caCancelledRecord.getMfVehicleData().getOwnerData()
				//.setOwnrTtlName1(
				.setName1(CANC_PLT);
				// end defect 10112 
				processData(ARCHIVE_REC_FOUND, laInqData);
			}
			// end defect 8984
			// Multiple records found on Title or Regis files
			else if (laInqData.getNoMFRecs() > 1)
			{
				processData(MULT_RECS, laInqData);
			}

			// defect 9085 
			// added check for is not null  (SPAPPL) and vehicle found
			//	with a Cancel Plate/Sticker 
			else if (
				laInqData.getMfVehicleData() != null
					&& laInqData.getMfVehicleData().getRegData() != null
					&& (laInqData
						.getMfVehicleData()
						.getRegData()
						.getCancPltIndi()
						== 1)
					&& laInqData.getSearchArchiveIndi()
						== CommonConstant.SEARCH_ACTIVE_INACTIVE)
			{
				if (caGeneralSearchData
					.getKey1()
					.equals(CommonConstant.VIN)
					|| caGeneralSearchData.getKey1().equals(
						CommonConstant.DOC_NO)
					|| caGeneralSearchData.getKey1().equals(
						CommonConstant.REG_PLATE_NO))
				{
					// defect 9086
					//	Send control to processData() to handle Cancel
					//	Plate for SP
					if (UtilityMethods.isSpecialPlates(getTransCode()))
					{
						caCancelledRecord = laInqData;
						processData(SINGLE_REC, laInqData);
					}
					else
					{
						caCancelledRecord = laInqData;
						processData(SEARCH_ARCHIVE, laInqData);
					}
					// end defect 9086
				}
				else
				{
					if (getTransCode().equals(TransCdConstant.RENEW)
						|| getTransCode().equals(TransCdConstant.DUPL)
						|| getTransCode().equals(TransCdConstant.EXCH)
						|| getTransCode().equals(TransCdConstant.REPL)
						|| getTransCode().equals(TransCdConstant.CORREG)
						|| getTransCode().equals(TransCdConstant.ADDR))
					{
						if (laInqData
							.getMfVehicleData()
							.getRegData()
							.getCancPltIndi()
							== 1)
						{
							// defect 10036
							//new RTSException(729); 
							new RTSException(
								ErrorsConstant
									.ERR_NUM_PLT_CANCELLED)
									.displayError(
								getFrame());
							// end defect 10036

							processData(CANCEL, null);
						}
					}
					else
					{
						// defect 10036 
						//new RTSException(57); 
						new RTSException(
							ErrorsConstant
								.ERR_NUM_NO_RECORD_FOUND)
								.displayError(
							getFrame());
						// end defect 10036 
						processData(CANCEL, null);
					}
				}
			}
			// Scenario -
			// Multiple records were found on Special Plate file only
			//	and user selected a single record from INQ004, search by
			//	SpclRegId a single record was returned
			else if (
				caGeneralSearchData.getKey1().equals(
					CommonConstant.SPCL_REG_ID))
			{
				processData(SINGLE_REC, laInqData);
			}
			// defect 9105
			//	add caCancelledRecord == null
			else if (
				(laInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ACTIVE_INACTIVE)
					&& (laInqData.getNoMFRecs() == 1)
					&& (caCancelledRecord == null
						|| caCancelledRecord.getMfVehicleData() == null))
			{
				// end defect 9105
				// defect 10598 
				if (laInqData.hasInProcsTrans())
				{
					processData(INPROCS_TRANS, laInqData);
				}
				else
				{
					caUtil.validateVehWts(laInqData);
					laInqData.setPrintOptions(
						caGeneralSearchData.getIntKey1());
					processData(SINGLE_REC, laInqData);
				}
				// end defect 10598 
			}
			else if (
				(laInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ACTIVE_INACTIVE)
					&& (laInqData.getNoMFRecs() == 0))
			{
				// defect 9086
				//	Send control to processData() to handle "NO RECORD 
				//	FOUND" for SP
				if (UtilityMethods.isSpecialPlates(getTransCode())
				// defect 10820
				// special plates inquiry - temporary trans code 
					|| asTransCode.equals(TransCdConstant.SPINQ))
				// end defect 10820
				{
					processData(NO_REC_FOUND, aaData);
					return;
				}
				// end defect 9086
				else if (
					caGeneralSearchData.getKey1().equals(
						CommonConstant.VIN)
						|| caGeneralSearchData.getKey1().equals(
							CommonConstant.DOC_NO)
						|| caGeneralSearchData.getKey1().equals(
							CommonConstant.REG_PLATE_NO))
				{
					// defect 10598 
					if (laInqData.hasInProcsTrans())
					{
						processData(INPROCS_TRANS, laInqData);
					}
					else
					{
						processData(SEARCH_ARCHIVE, laInqData);
					}
					// end defect 10598 
				}
				else
				{
					if (asTransCode.equals(TransCdConstant.REFUND))
					{
						processData(NO_REC_FOUND, aaData);
						return;
					}
					else
					{
						// defect 10036 
						//new RTSException(57
						new RTSException(
							ErrorsConstant
								.ERR_NUM_NO_RECORD_FOUND)
								.displayError(
							getFrame());
						// end defect 10036 
					}
				}
			}
			else if (
				(laInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ARCHIVE)
					&& (laInqData.getNoMFRecs() == 0)
					&& (caCancelledRecord == null
						|| caCancelledRecord.getMfVehicleData() == null))
			{
				processData(NO_REC_FOUND, laInqData);
			}
			else if (
				(laInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ARCHIVE)
					&& (laInqData.getNoMFRecs() == 1)
					&& (caCancelledRecord.getMfVehicleData() == null))
			{
				// defect 10598 
				if (laInqData.hasInProcsTrans())
				{
					processData(INPROCS_TRANS, laInqData);
				}
				else
				{
					caUtil.validateVehWts(laInqData);
					laInqData.setPrintOptions(
						caGeneralSearchData.getIntKey1());
					processData(SINGLE_REC, laInqData);
				}
				// end defect 10598 
			}
			else if (
				(laInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ARCHIVE)
					&& (laInqData.getNoMFRecs() == 0)
					&& (laInqData.getPartialDataList() == null
						|| laInqData.getPartialDataList().size() == 0)
					&& (caCancelledRecord
						.getMfVehicleData()
						.getRegData()
						.getCancPltIndi()
						== 1))
			{
				// Set Special Owner Indi so that cancelled plate is 
				// displayed on Special Owner Screen
				caCancelledRecord.setSpecialOwner(1);
				if (caCancelledRecord
					.getMfVehicleData()
					.getRegData()
					.getCancPltIndi()
					== 1)
				{
					caCancelledRecord.getMfVehicleData().getOwnerData()
					.setName1(CANC_PLT);
				}
				processData(SINGLE_REC, caCancelledRecord);
			}
		}
	}

	/** 
	 * Set values for search key 
	 *
	 * @param aaVehicleInquiryData VehicleInquiryData
	 */
	public void updateVehInqDataForRefund(VehicleInquiryData aaVehicleInquiryData)
	{
		aaVehicleInquiryData.getMfVehicleData().setDoNotBuildMvFunc(
			true);
		if (caGeneralSearchData
			.getKey1()
			.equals(CommonConstant.REG_PLATE_NO))
		{
			aaVehicleInquiryData
				.getMfVehicleData()
				.getRegData()
				.setRegPltNo(
				caGeneralSearchData.getKey2());
		}
		else if (
			caGeneralSearchData.getKey1().equals(CommonConstant.VIN))
		{
			aaVehicleInquiryData
				.getMfVehicleData()
				.getVehicleData()
				.setVin(
				caGeneralSearchData.getKey2());
		}
		else if (
			caGeneralSearchData.getKey1().equals(
				CommonConstant.DOC_NO))
		{
			aaVehicleInquiryData
				.getMfVehicleData()
				.getTitleData()
				.setDocNo(
				caGeneralSearchData.getKey2());
		}
	}
}