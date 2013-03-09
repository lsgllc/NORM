package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.client.title.ui.TitleValidObj;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.common
	.business
	.CommonEligibility;

/*
 *
 * VehicleInquiryData.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/28/2002	Added mail object in VehInqData to store 
 * 							mail info 
 * 							defect 4122 
 * MAbs/TP		06/05/2002	MultiRecs in Archive 
 * 							defect 4019
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * J Rue		02/07/2007	Add SpclPlts Partials
 * 							add getPartialSpclPltsData()
 * 							add setPartialSpclPltsData()
 * 							defect 9086 Ver Special Plates
 * J Rue		01/15/2009	Move methods createDlrEmptyVehInqObj
 * 							from TitleClientUtilityMethods 
 * 							add retainVehInqDataDlrNotApp(),
 * 							 retainTitleDataDlrNotApp(),
 * 							defect 8631 Ver Defect_POS_D
 * J Rue		01/21/2009	Change methods createDlrEmptyVehInqObj,
 * 							retainVehInqDataDlrNotApp(),
 * 							and retainTitleDataDlrNotApp() to non-static
 * 							methods
 * 							modify retainVehInqDataDlrNotApp(),
 * 								retainTitleDataDlrNotApp(),
 * 								createDlrEmptyVehInqObj(),
 * 							defect 8631 Ver Defect_POS_D
 * J Rue		01/22/2009	Remove parameters passed and returnd for 
 * 							createDlrEmptyVehInqObj() and
 * 							retainVehInqDataDlrNotApp()
 * 							modify retainVehInqDataDlrNotApp(),
 * 								createDlrEmptyVehInqObj()
 * 							defect 8631 Ver Defect_POS_D
 * J Rue		05/14/2009	laTitleDataOrig object is not assign to the
 *							class object
 * 							modify retainTitleDataDlrNotApp()
 * 							defect 10072 Ver Defect_POS_F
 * K Harrell	06/09/2009	add isRecordFound(), isMFUp(), isMFDown()
 * 							defect 10035 Ver Defect_POS_F
 * K Harrell	07/03/2009	add VehicleInquiryData(GeneralSearchData,
 * 							 boolean), updatePerGSD() 
 * 							modify caMailingAddress, get/set methods, 
 * 							 VehicleInquiryData(GSD,boolean),
 * 							 retainTitleDataDlrNoApp() 
 * 							defect 10112 Ver Defect_POS_F  
 * J Rue		10/15/2009	Remove registration data for DTA/Record Not 
 * 							applicable	
 * 							modify createDlrEmptyVehInqObj()
 * 							defect 10245 Ver Defect_POS_G 
 * J Rue		10/16/2009	Update comments
 * 							modify createDlrEmptyVehInqObj()
 * 							defect 10245 Ver Defect_POS_G 
 * J Rue		10/21/2009	Remove RegistrationData  - VehCaryngCap 
 * 							comment in prologue
 * 							modify createDlrEmptyVehInqObj(),
 * 								retainVehInqDataDlrNotApp()
 * 							defect 10245 Ver Defect_POS_G 
 * K Harrell	12/23/2009	add hasPartialDataList(), 
 * 							 hasPartialSpclPltsDataList()
 * 							defect 10280 Ver Defect_POS_H
 * K Harrell	06/15/2010	add hasHardStops(), hasAuthCode()
 * 							defect 10492 Ver 6.5.0 
 * K Harrell	09/17/2010	add cvInProcsTransDataList, get/set methods.
 * 							add hasInProcsTrans() 
 * 							defect 10598 Ver 6.6.0 
 * K Harrell	10/15/2010	add hasTtlProcsCd()
 * 							defect 10624 Ver 6.6.0 
 * K Harrell	03/09/2011	add validateForWRENEW() 
 * 							defect 10768 Ver 6.7.1
 * K Harrell	03/23/2011	add check for IRENEW
 * 							modify validateForWRENEW(
 * 							defect 10768 Ver 6.7.1 
 * K Harrell	04/22/2011	Return HardStop error if NotfyngCity set
 * 							modify validateForWRENEW()
 * 							defect 10768 Ver 6.7.1 
 * B Woodson	06/07/2011	add caFraudStateData, get/set methods
 * 							defect 10865 Ver 6.8.0 
 * K Harrell	06/22/2011	add getVehTypeCdforPermit()
 * 							defect 10844 Ver 6.8.0  
 * K Harrell	11/14/2011	add VIEW_AND_PRINT_AND_CHARGE,
 * 							 VIEW_AND_CERTFD,
 * 							 VIEW_AND_CERTFD_AND_CHARGE,	
 * 							 VIEW_AND_NONCERTFD,
 * 							 VIEW_AND_NONCERTFD_AND_CHARGE
 * 							add isInqChrgFee()
 * 							delete PRINT, CHARGE_FEE_VIEW_AND_PRINT
 * 							defect 11052 Ver 6.9.0
 * B Woodson   11/22/2011   add isVTR275PrintOptions()
 *                          defect 11052 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * VehicleInquiryData 
 * 
 * @version	6.9.0  			11/14/2011
 * @author	Administrator
 * <br>Creation Date:		08/22/2001 13:18:31 
 */
public class VehicleInquiryData implements java.io.Serializable
{
	// boolean 
	private boolean cbGoPrevious;
	private boolean cbSalvageLienIndi;
	private boolean cbVINAExists;

	// int 
	private int ciMfDown = 0;
	private int ciMultiArchiveStatus;
	private int ciNoMFRecs;
	private int ciPrintOptions;
	private int ciRTSEffDt;
	private int ciSearchArchiveIndi;
	private int ciSpecialOwner;

	// String 
	private String csOwnerId;

	// Vector 
	private Vector cvPartialDataList = new Vector();
	private Vector cvPartialSpclPltsDataList = new Vector();

	// defect 10865 
	private FraudStateData caFraudStateData;
	// end defect 10865 

	// defect 10598 
	private Vector cvInProcsTransDataList = new Vector();
	// end defect 10598 

	//	Object 
	// defect 10112 
	private NameAddressData caMailingAddress;
	// end defect 10112 

	private MFVehicleData caMfVehicleData;
	private Object caValidationObject;
	private VehMiscData caVehMiscData = new VehMiscData();

	// Constants
	public static final int VIEW_ONLY = 1;
	public static final int VIEW_AND_PRINT = 2;
	
	// defect 11052 
	public static final int VIEW_AND_PRINT_AND_CHARGE = 3;
	public static final int VIEW_AND_CERTFD = 4;
	public static final int VIEW_AND_CERTFD_AND_CHARGE = 5;
	public static final int VIEW_AND_NONCERTFD = 6;
	public static final int VIEW_AND_NONCERTFD_AND_CHARGE = 7;
	//public static final int PRINT = 3; 
	// public static final int CHARGE_FEE_VIEW_AND_PRINT = 4;
	// end defect 11052 
	
	private final static long serialVersionUID = -802436503625985506L;

	/**
	 * VehicleInquiryData constructor comment.
	 */
	public VehicleInquiryData()
	{
		super();

		// defect 10112 
		caMailingAddress = new NameAddressData();
		// end defect 10112 
	}

	/**
	 * VehicleInquiryData constructor  
	 */
	public VehicleInquiryData(
		GeneralSearchData aaGSD,
		boolean abMFDown)
	{
		super();
		caMfVehicleData = new MFVehicleData();
		caValidationObject = new Object();
		caMailingAddress = new NameAddressData();
		caMfVehicleData.setVehicleData(new VehicleData());
		caMfVehicleData.setTitleData(new TitleData());
		caMfVehicleData.setRegData(new RegistrationData());
		updatePerGSD(aaGSD);
		if (abMFDown)
		{
			caMfVehicleData.setFromMF(false);
			ciRTSEffDt = new RTSDate().getYYYYMMDDDate();
		}
	}

	/**
	 * Retained selected data for Record Not Applicable for DTAORD
	 * 	TitleData - LienData
	 *   	  	  - Previous Owner info
	 *        	  - VehSalesPrice
	 *       	  - TransInAllowance
	 * 			  - DlrGdn
	 * 			  - OwnrShpEvidCd
	 * 	VehicleInquiryData  
	 * 		TitleDate		  - DocNo
	 * 		VehicleData 	  - Initialize all fields, retain VIN
	 * 
	 */
	public void createDlrEmptyVehInqObj()
	{
		// laTtlValidationObj will return the current transaction. 
		// This means any changes to laTtlValidationObj
		// will be reflected in the object VehicleInquiryData.
		TitleValidObj laTtlValidationObj =
			(TitleValidObj) getValidationObject();

		/*******   DlrTitleData/ORIG - RegistrationData   **************/
		// Initialize all RegistrationData fields for DealerTitleData
		//	and MfVehicleData
		((DealerTitleData) laTtlValidationObj.getDlrTtlData())
			.getMFVehicleData()
			.setRegData(new RegistrationData());
		// defect 10245
		// 	Create a new RegistrationData object MfVehicleData
		//	This mimics what Title does. 
		//	Title: in TitleClientUtilityMethods.createEmptyVehInqObj()
		//	 sets getMfVehicleData().setRegData(new RegistrationData());
		getMfVehicleData().setRegData(new RegistrationData());
		// end defect 10245

		/*******   DlrTitleData - VehicleData   *******************/
		// Initialize all VehicleData fields for DealerTitleData
		((DealerTitleData) laTtlValidationObj.getDlrTtlData())
			.getMFVehicleData()
			.setVehicleData(new VehicleData());

		/*******   DlrTitleData - TitleData   *********************/
		// Keep selected fields info and initialize the remaining
		retainTitleDataDlrNotApp();

		/*******   VehicleInquiryData   ***************************/
		// Keep selected fields info and initialize the remaining
		retainVehInqDataDlrNotApp();

		/*******   Miscellanious   ***************************/
		// defect 8631
		//	Set DocNo to empty when record not applicable
		getMfVehicleData().getTitleData().setDocNo(
			CommonConstant.STR_SPACE_EMPTY);
		// end defect 8631

	}
	/**
		* Return value of FraudStateData
		* 
		* @return FraudStateData
		*/
	public FraudStateData getFraudStateData()
	{
		if (caFraudStateData == null)
		{
			caFraudStateData = new FraudStateData();
		}
		return caFraudStateData;
	}

	/**
	 * Return value of MailingAddress
	 * 
	 * @return NameAddressData
	 */
	public NameAddressData getMailingAddress()
	{
		return caMailingAddress;
	}
	/**
	 * Return value of MfDown
	 * 
	 * @return int
	 */
	public int getMfDown()
	{
		return ciMfDown;
	}
	/**
	 * Return value of MfVehicleData
	 * 
	 * @return MFVehicleData
	 */
	public MFVehicleData getMfVehicleData()
	{
		return caMfVehicleData;
	}
	/**
	 * Return value of MultiArchiveStatus
	 * 
	 * @return int
	 */
	public int getMultiArchiveStatus()
	{
		return ciMultiArchiveStatus;
	}
	/**
	 * Return value of 
	 * 
	 * @return int
	 */
	public int getNoMFRecs()
	{
		return ciNoMFRecs;
	}
	/**
	 * Return value of OwnerId
	 * 
	 * @return String
	 */
	public String getOwnerId()
	{
		return csOwnerId;
	}
	/**
	 * Return value of PartialDataList
	 * 
	 * @return Vector
	 */
	public Vector getPartialDataList()
	{
		return cvPartialDataList;
	}
	/**
	 * Return value of PartialSpclPltsDataList
	 * 
	 * @return Vector
	 */
	public Vector getPartialSpclPltsDataList()
	{
		return cvPartialSpclPltsDataList;
	}
	/**
	 * Return cvInProcsTransDataList
	 * 
	 * @return Vector 
	 */
	public Vector getInProcsTransDataList()
	{
		return cvInProcsTransDataList;
	}
	/**
	 * Return value of PrintOptions
	 * 
	 * @return int
	 */
	public int getPrintOptions()
	{
		return ciPrintOptions;
	}
	/**
	 * Return value of RTSEffDt
	 * 
	 * @return int
	 */
	public int getRTSEffDt()
	{
		return ciRTSEffDt;
	}
	/**
	 * Return value of SearchArchiveIndi
	 * 
	 * @return int
	 */
	public int getSearchArchiveIndi()
	{
		return ciSearchArchiveIndi;
	}
	/**
	 * Return value of SpecialOwner
	 * 
	 * @return int
	 */
	public int getSpecialOwner()
	{
		return ciSpecialOwner;
	}
	/**
	 * Return value of ValidationObject
	 * 
	 * @return Object
	 */
	public Object getValidationObject()
	{
		return caValidationObject;
	}
	/**
	 * Return value of VehMiscData
	 * 
	 * @return VehMiscData
	 */
	public VehMiscData getVehMiscData()
	{
		return caVehMiscData;
	}
	/**
	 * Return VehTypeCd for Permit
	 * 
	 * @return String 
	 */
	public String getVehTypeCdforPermit()
	{
		String lsVehTypeCd = new String();

		if (getMfVehicleData() != null
			&& getMfVehicleData().getVehicleData() != null)
		{
			VehicleData laVehData = getMfVehicleData().getVehicleData();
			if (getNoMFRecs() == 0)
			{
				if (!UtilityMethods
					.isEmpty(
						getMfVehicleData()
							.getVehicleData()
							.getVehTypeCd()))
				{
					lsVehTypeCd = laVehData.getVehTypeCd();

					if (lsVehTypeCd
						.equals(CommonConstant.VINA_PASSENGER_VEHTYPECD)
						|| lsVehTypeCd.equals(
							CommonConstant.VINA_TRUCK_VEHTYPECD))
					{
						lsVehTypeCd =
							MiscellaneousRegConstant
								.PERMIT_REGULAR_VEHTYPECD;
					}
				}
			}
			else
			{
				String lsVehClassCd = laVehData.getVehClassCd();

				if (!UtilityMethods.isEmpty(lsVehClassCd))
				{
					if (lsVehClassCd
						.equals(CommonConstant.VEHCLASSCD_MOTORCYCLE))
					{
						lsVehTypeCd =
							MiscellaneousRegConstant
								.PERMIT_MOTORCYCLE_VEHTYPECD;
					}
					else
					{
						lsVehTypeCd =
							MiscellaneousRegConstant
								.PERMIT_REGULAR_VEHTYPECD;
					}
				}
			}
		}
		return lsVehTypeCd;
	}

	/**
	 * Return boolean to denote that Hard Stops are associated with
	 * Record 
	 * 
	 * @param asTransCd
	 * @return boolean 
	 */
	public boolean hasHardStops(String asTransCd)
	{
		Vector lvIndis =
			IndicatorLookup.getIndicators(
				getMfVehicleData(),
				asTransCd,
				IndicatorLookup.SCREEN);

		return IndicatorLookup.hasHardStop(lvIndis);
	}

	/** 
	 * Has Partial Data List (Vehicle Inquiry Records)
	 * 
	 * @return boolean 
	 */
	public boolean hasPartialDataList()
	{
		return cvPartialDataList != null
			&& !cvPartialDataList.isEmpty();
	}

	/** 
	 * Has Partial Spcl Plts Data List 
	 * 
	 * @return boolean 
	 */
	public boolean hasPartialSpclPltsDataList()
	{
		return cvPartialSpclPltsDataList != null
			&& !cvPartialSpclPltsDataList.isEmpty();
	}

	/**
	 * Return boolean to denote that acceptable Auth Code has been
	 * specified 
	 * 
	 * @return boolean 
	 */
	public boolean hasAuthCode()
	{
		return caVehMiscData != null
			&& !UtilityMethods.isEmpty(caVehMiscData.getAuthCd());
	}

	/**
	 * Has In Process Transactions
	 * 
	 * @return boolean 
	 */
	public boolean hasInProcsTrans()
	{
		return cvInProcsTransDataList != null
			&& cvInProcsTransDataList.size() > 0;
	}
	/** 
	 * Return boolean to denote if has Title In Process Code 
	 * 
	 * @return boolean 
	 */
	public boolean hasTtlProcsCd()
	{
		boolean lbTtlProcsCd = false;

		if (getMfVehicleData() != null
			&& getMfVehicleData().getTitleData() != null)
		{
			lbTtlProcsCd =
				!UtilityMethods.isEmpty(
					getMfVehicleData().getTitleData().getTtlProcsCd());
		}
		return lbTtlProcsCd;
	}

	/**
	 * Return boolean to denote if MF Down
	 * 	- true ciMfDown = 1 
	 *  - false ciMfDown !=1 
	 * 
	 * @return boolean 
	 */
	public boolean isMFDown()
	{
		return ciMfDown == 1;
	}

	/**
	 * Return boolean to denote if MF Up
	 * 	- true ciMfDown = 0 
	 *  - false ciMfDown !=0 
	 * 
	 * @return boolean 
	 */
	public boolean isMFUp()
	{
		return ciMfDown == 0;
	}

	/**
	 * Return boolean to denote if Record Found
	 * 	- true ciMFRecs >0 
	 *  - false ciMFRecs <=0 
	 * 
	 * @return boolean 
	 */
	public boolean isRecordFound()
	{
		return ciNoMFRecs > 0;
	}

	/**
	 * Return value of VINAExists
	 * 
	 * @return boolean
	 */
	public boolean isVINAExists()
	{
		return cbVINAExists;
	}
	/**
	  * Retain selected fields DlrTitleData - TitleData
	  *  TitleData - LienData
	  *         - Previous Owner info
	  *           - VehSalesPrice
	  *          - TransInAllowance
	  *      - DlrGdn
	  *      - OwnrShpEvidCd
	  */
	public void retainTitleDataDlrNotApp()
	{
		// Create an empty TitleData object that will contain only the 
		// retained DTA info
		// 
		TitleData laTitleDataRetained = new TitleData();
		TitleData laTitleDataOrig =
			(TitleData)
				(
					(DealerTitleData)
						((TitleValidObj) getValidationObject())
				.getDlrTtlData())
				.getMFVehicleData()
				.getTitleData();
		// defect 6797
		// Retain DTA Lien info if exist
		// defect 5941, 6044
		// Create new LienHolder object and LienDate
		// Get Lien data and dates
		// defect 10112 
		laTitleDataRetained.setLienholderData(
			TitleConstant.LIENHLDR1,
			laTitleDataOrig.getLienholderData(TitleConstant.LIENHLDR1));
		laTitleDataRetained.setLienholderData(
			TitleConstant.LIENHLDR2,
			laTitleDataOrig.getLienholderData(TitleConstant.LIENHLDR2));
		laTitleDataRetained.setLienholderData(
			TitleConstant.LIENHLDR3,
			laTitleDataOrig.getLienholderData(TitleConstant.LIENHLDR3));
		//	laTitleDataRetained.setLienHolder1Date(
		//		laTitleDataOrig.getLienHolder1Date());
		//	laTitleDataRetained.setLienHolder2Date(
		//		laTitleDataOrig.getLienHolder2Date());
		//	laTitleDataRetained.setLienHolder3Date(
		//		laTitleDataRetained.getLienHolder3Date());
		//laTitleDataRetained.setLienHolder1(
		//	laTitleDataOrig.getLienHolder1());
		//laTitleDataRetained.setLienHolder2(
		//	laTitleDataOrig.getLienHolder2());
		//laTitleDataRetained.setLienHolder3(
		//	laTitleDataOrig.getLienHolder3());
		// end defect 8603
		// end defect 5941, 6044
		// Retain Previous Owner
		laTitleDataRetained.setPrevOwnrName(
			laTitleDataOrig.getPrevOwnrName());
		laTitleDataRetained.setPrevOwnrCity(
			laTitleDataOrig.getPrevOwnrCity());
		laTitleDataRetained.setPrevOwnrState(
			laTitleDataOrig.getPrevOwnrState());
		// Retain Sales Price and Trade In Allownce
		laTitleDataRetained.setVehSalesPrice(
			laTitleDataOrig.getVehSalesPrice());
		laTitleDataRetained.setVehTradeinAllownce(
			laTitleDataRetained.getVehTradeinAllownce());
		// Retain DlrGdn, OwnrShpEvidCd
		laTitleDataRetained.setDlrGdn(laTitleDataOrig.getDlrGdn());
		laTitleDataRetained.setOwnrShpEvidCd(
			laTitleDataOrig.getOwnrShpEvidCd());
		// defect 10072
		// laTitleDataOrig object is not assign to the class object
		//laTitleDataOrig =
		// (TitleData) UtilityMethods.copy(laTitleDataRetained);
		// This step assigns the retained data to the TitleValidObj
		((DealerTitleData) ((TitleValidObj) getValidationObject())
			.getDlrTtlData())
			.getMFVehicleData()
			.setTitleData(laTitleDataRetained);
		// end defect 10072
	}
	/**
	 * Retain selected fields DlrTitleData - VehicleInquiryData
	 * 	VehicleInquiryData  
	 * 		TitleDate		  - DocNo
	 * 		VehicleData 	  - Initialize all fields, retain VIN
	 *  	RegistrationData  - VehCaryngCap
	 */
	public void retainVehInqDataDlrNotApp()
	{
		//	Reset selected data items in VehicleInquiryData
		// defect 5941,
		//	Save the VIN before initializing the object
		String lsVIN = getMfVehicleData().getVehicleData().getVin();
		// Remove all data for VehicleData
		getMfVehicleData().setVehicleData(new VehicleData());
		// Retain the VIN
		getMfVehicleData().getVehicleData().setVin(lsVIN);
		// end defect 5941
		// defect 10245
		// 	setVehCaryngCap() is initialize in createDlrEmptyVehInqObj()
		// defect 8783
		//	Remove Carrying Capacity data
		//getMfVehicleData().getRegData().setVehCaryngCap(0);
		// end defect 8783
		// end defect 10245
	}

	/**
	 * Set value of caFraudStateData
	 *
	 * @param  aaFraudStateData
	 */
	public void setFraudStateData(FraudStateData aaFraudStateData)
	{
		caFraudStateData = aaFraudStateData;
	}

	/**
	 * Set value of MailingAddress
	 * 
	 * @param aaMailingAddress NameAddressData
	*/
	public void setMailingAddress(NameAddressData aaMailingAddress)
	{
		caMailingAddress = aaMailingAddress;
	}
	/**
	 * Set value of MfDown
	 * 
	 * @param aiMfDown int
	 */
	public void setMfDown(int aiMfDown)
	{
		ciMfDown = aiMfDown;
	}
	/**
	 * Set value of MfVehicleData
	 * 
	 * @param aaMfVehicleData MFVehicleData
	 */
	public void setMfVehicleData(MFVehicleData aaMfVehicleData)
	{
		caMfVehicleData = aaMfVehicleData;
	}
	/**
	 * Set value of MultiArchiveStatus
	 * 
	 * @param aiMultiArchiveStatus int
	 */
	public void setMultiArchiveStatus(int aiMultiArchiveStatus)
	{
		ciMultiArchiveStatus = aiMultiArchiveStatus;
	}
	/**
	 * Set value of NoMFRecs
	 * 
	 * @param aiNoMFRecs int
	 */
	public void setNoMFRecs(int aiNoMFRecs)
	{
		ciNoMFRecs = aiNoMFRecs;
	}
	/**
	 * Set value of OwnerId
	 * 
	 * @param asOwnerId String
	 */
	public void setOwnerId(String asOwnerId)
	{
		csOwnerId = asOwnerId;
	}
	/**
	 * Set value of PartialDataList
	 * 
	 * @param avPartialDataList Vector
	 */
	public void setPartialDataList(Vector avPartialDataList)
	{
		cvPartialDataList = avPartialDataList;
	}
	/**
	 * Set value of PartialSpclPltsDataList
	 * 
	 * @param avPartialSpclPltsDataList Vector
	 */
	public void setPartialSpclPltsDataList(Vector avPartialSpclPltsDataList)
	{
		cvPartialSpclPltsDataList = avPartialSpclPltsDataList;
	}
	/**
	 * Set value of cvInProcsTransDataList
	 * 
	 * @param avInProcsTransDataList
	 */
	public void setInProcsTransDataList(Vector avInProcsTransDataList)
	{
		cvInProcsTransDataList = avInProcsTransDataList;
	}
	/**
	 * Set value of PrintOptions
	 * 
	 * @param aiPrintOptions int
	 */
	public void setPrintOptions(int aiPrintOptions)
	{
		ciPrintOptions = aiPrintOptions;
	}
	/**
	 * Set value of RTSEffDt
	 * 
	 * @param aiRTSEffDt int
	 */
	public void setRTSEffDt(int aiRTSEffDt)
	{
		ciRTSEffDt = aiRTSEffDt;
	}
	/**
	 * Set value of SalvageLienIndi 
	 * 
	 * @param abSalvageLienIndi boolean
	 */
	public void setSalvageLienIndi(boolean abSalvageLienIndi)
	{
		cbSalvageLienIndi = abSalvageLienIndi;
	}
	/**
	 * Set value of SearchArchiveIndi 
	 * 
	 * @param aiSearchArchiveIndi int
	 */
	public void setSearchArchiveIndi(int aiSearchArchiveIndi)
	{
		ciSearchArchiveIndi = aiSearchArchiveIndi;
	}
	/**
	 * Set value of GoPrevious
	 * 
	 * @param abShouldGoPrevious boolean
	 */
	public void setShouldGoPrevious(boolean abShouldGoPrevious)
	{
		cbGoPrevious = abShouldGoPrevious;
	}
	/**
	 * Set value of SpecialOwner
	 * 
	 * @param aiSpecialOwner int
	 */
	public void setSpecialOwner(int aiSpecialOwner)
	{
		ciSpecialOwner = aiSpecialOwner;
	}
	/**
	 * Set value of ValidationObject
	 * 
	 * @param aaValidationObject Object
	 */
	public void setValidationObject(Object aaValidationObject)
	{
		caValidationObject = aaValidationObject;
	}
	/**
	 * Set value of VehMiscData
	 * 
	 * @param aaVehMiscData VehMiscData
	 */
	public void setVehMiscData(VehMiscData aaVehMiscData)
	{
		caVehMiscData = aaVehMiscData;
	}
	/**
	 * Set value of VINAExists
	 * 
	 * @param abVINAExists boolean
	 */
	public void setVINAExists(boolean abVINAExists)
	{
		cbVINAExists = abVINAExists;
	}
	/**
	 * Return value of GoPrevious
	 * 
	 * @return boolean
	 */
	public boolean shouldGoPrevious()
	{
		return cbGoPrevious;
	}

	/**
	 * Update object per contents of GSD 
	 * 
	 * @param aaGSD
	 */
	public void updatePerGSD(GeneralSearchData aaGSD)
	{
		if (aaGSD.getKey1().equals(CommonConstant.REG_PLATE_NO))
		{
			caMfVehicleData.getRegData().setRegPltNo(aaGSD.getKey2());
		}
		else if (aaGSD.getKey1().equals(CommonConstant.VIN))
		{
			caMfVehicleData.getVehicleData().setVin(aaGSD.getKey2());
		}
		else if (aaGSD.getKey1().equals(CommonConstant.DOC_NO))
		{
			caMfVehicleData.getTitleData().setDocNo(aaGSD.getKey2());
		}
	}

	/** 
	 * Validate for WRENEW 
	 * 
	 * @throws RTSExceptiom 
	 */
	public void validateForWRENEW() throws RTSException
	{
		CommonEligibility laCommElig = new CommonEligibility(this);

		// Too Far In Advance: 2306 
		if (laCommElig.isRegistrationTooFarInFuture())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_LOOKUP_REG_TOO_FAR_IN_ADVANCE);
		}

		// HardStops: 2308  
		if (hasHardStops(TransCdConstant.WRENEW)
			|| !UtilityMethods.isEmpty(
				caMfVehicleData.getRegData().getNotfyngCity()))
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_LOOKUP_HARDSTOP_EXISTS);
		}

		// Invalid DocTypeCd: 2310   
		if (!laCommElig.isDocTypeCdEligible())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_LOOKUP_INVALID_DOCTYPECD);
		}

		// Plate Not Eligible: 2311   
		if (!laCommElig.isPlateEligible(TransCdConstant.WRENEW)
			|| !laCommElig.isPlateTypeEligible())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_LOOKUP_LOC_NOT_AUTHORIZED);
		}

		// Gross Wt Invalid: 2312   
		if (getMfVehicleData().getRegData().getVehGrossWt() >= 55000)
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_LOOKUP_GROSS_WT_EXCESSIVE);
		}

		// RegClassCd Not Eligible: 2313  
		if (!laCommElig.isRegClassCodeEligible())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_LOOKUP_REGCLASSCD_NOT_ELIGIBLE);
		}

		// Transactions In Process: 2309 (last validation per specification) 
		if (hasInProcsTrans())
		{
			boolean lbInProcess = false;
			for (int i = 0; i < cvInProcsTransDataList.size(); i++)
			{
				InProcessTransactionData laTransactionData =
					(
						InProcessTransactionData) cvInProcsTransDataList
							.elementAt(
						i);
				String lsTransCd =
					laTransactionData.getTransCd().trim();

				if (lsTransCd.equals(TransCdConstant.RENEW)
					|| lsTransCd.equals(TransCdConstant.IRENEW)
					|| lsTransCd.equals(TransCdConstant.SBRNW)
					|| lsTransCd.equals(TransCdConstant.WRENEW))
				{
					lbInProcess = true;
					break;
				}
			}
			if (lbInProcess)
			{
				throw new RTSException(
					ErrorsConstant
						.ERR_NUM_WEBAGNT_LOOKUP_INPROCESS_TRANS_EXISTS);
			}
		}
	}

	/**
	 * 
	 * @return the ciInqChrgFeeIndi
	 */
	public boolean isInqChrgFee()
	{
		return ciPrintOptions == VIEW_AND_PRINT_AND_CHARGE || 
		ciPrintOptions == VIEW_AND_NONCERTFD_AND_CHARGE || 
		ciPrintOptions == VIEW_AND_CERTFD_AND_CHARGE; 
	}

	/**
	 * @return
	 */
	public boolean isVTR275PrintOptions()
	{
		return getPrintOptions() == VIEW_AND_CERTFD ||
			getPrintOptions() == VIEW_AND_CERTFD_AND_CHARGE ||
			getPrintOptions() == VIEW_AND_NONCERTFD ||
			getPrintOptions() == VIEW_AND_NONCERTFD_AND_CHARGE;
	}
}
