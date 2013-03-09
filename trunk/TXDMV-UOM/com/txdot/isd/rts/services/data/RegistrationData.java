package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

/*
 * RegistrationData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/17/2001	Make class serializable
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * B. Brown		08/19/2005  Regain removed fields needed due to Complete
 * 							TransactionData being stored in table 
 * 							rts_itrnt_data.
 * 							add cCustActlRegFee,cCustBaseRegFee,
 *								cCustChildFee,cCustDieselFee,
 *								cCustRdBrdgFee,cRegRefAmt,
 *                          add getCCustActlRegFee(),
 * 								getCCustBaseRegFee(),getCCustChildFee(),
 * 								getCCustDieselFee(),getCCustRdBrdgFee(),
 * 								getCRegRefAmt()
 * 							modify getCustActlRegFee(),
 * 								   getCustBaseRegFee(),
 * 								   getCustChildFee (),
 * 								   getCustDieselFee(),
 * 								   getCustRdBrdgFee(),getRegRefAmt()			
 *							defect 7899 Ver 5.2.3
 * K Harrell	09/28/2006	add fields/methods for Hoops, PltBirth 
 * 							and TERP Project
 * 							add csHoopsRegPltNo,ciPltBirthDate,
 * 								ciSpclRegId, csOrgNo
 * 							add getHoopsRegPltNo(),setHoopsRegPltNo()
 * 							    getPltBirthDate(),setPltBirthDate(),
 * 								getSpclRegId(), setSpclRegId(),
 * 								getOrgNo(),setOrgNo()
 * 							defect 8901,8902,8903 Ver Exempts 
 * Min Wang		10/10/2006	add and modify methods for RegPltAge
 * 							add computePltAge()
 * 							modify getPltAge(),
 * 							defect 8901 Ver Exempts
 * K Harrell	10/19/2006 	add getHoopsRegPltNo()
 * 							defect 8902 Ver Exempts
 * Min Wang		10/27/2006	add IF when plate age is -1.
 * 							modify computePltAge()
 * 							defect 8901 Ver Exempts
 * Min Wang		11/15/2006	Add 1 month to cover reg effective Date
 * 							when it is a renewal.
 * 							modify computePltAge()
 * 							defect 8901 Ver Exempts
 * K Harrell	05/21/2007	Removed work from 7889 per design 
 * 							defect 9085 Ver Special Plates 
 * Min Wang		12/04/2007  add boolean to check insurance status.
 *                          add cbInsuranceRequired, cbInsuranceVerified
 * 							isInsuranceRequired(), isInsuranceVerified()
 * 							setInsuranceRequired(), setInsuranceVerified()
 * 							defect 9459 Ver FRVP
 * Min Wang		12/21/2007	set insurance required to true by default.
 * 							This forces insurance verification when MF 
 * 							is down.
 * 							modify cbInsuranceRequired
 * 							defect 9459 Ver FRVP
  * J Rue		12/14/2007	Add DTARegInvldIndi for TTL003 indicator box
 * 							add setDTARegInvlIndi(), setDTARegInvlIndi()
 * 							defect 8329 Ver DEFECT-POS-A
 * J Rue		03/27/2008	Add new attributes
 * 							add ciDissociateCd, ciV21PltId, csPrismLvlCd
 * 								getDissociateCd(),setDissociateCd()
 * 								getV21PltId(), setV21PltId()
 * 								getPrismLvlCd(), setPrismLvlCd()
 * 							defect 9581 Ver Defect_POS_A
 * B Hargrove	04/16/2008	Checking references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe. (see: defect 9557).
 * 							No changes needed here. 
 * 							defect 9631 Ver Defect POS A
 * K Harrell	04/25/2008	Update for DocNo for Cancelled Plates
 * 							add csCancPltDocNo, get/set methods				 
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		04/30/2008	Rename DissociateCd to PltRmvCd
 * 							add getPltRmvCd(),setPltRmvCd()
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		05/06/2008	Reset RegPltNo if RmvPltCd > 0
 * 							Add getter/setter RmvRegPltNo, original
 * 							  RegPltNo saved if PltRmvCd > 0
 * 							add getRmvRegPltNo(), setRmvRegPltNo,
 * 								ResetFields()
 * 							defect 9630 Ver Defect_POS_A
 * J Rue		05/30/2008	Rename RmvPltNo to RmvdPltNo
 * 							Orginize methods
 * 							Update method description
 * 							modify getRmvdPltNo(), setRmvdPltNo()
 * 								resetFields(), resetFieldsMultRegis()
 * 							defect 9630 VerDefect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect _POS_A
 * 							defect 9630 Ver Defect_POS_A
 * J Rue		01/12/2009	Remove OrgNo.
 * 							setOrgNo() must remain for T and V versions.
 * 							The next interface release, this var. and 
 * 							method will be removed.
 * 							deprecate getOrgNo(), setOrgNo()
 * 							defect 9655 Ver Defect_POS_D
 * B Hargrove	01/21/2009  Add getter to return the Insurance 
 * 							Verification result.
 * 							add getInsuranceVerified()
 * 							defect 9691 Ver Defect_POS_D
 * J Rue		02/25/2009	add csVTRRegEmrgCd1, csVTRRegEmrgCd2, get/set
 * 							methods.
 * 							defect 9961 Ver Defect_POS_E
 * B Hargrove	03/06/2008	Remove bogus 'deprecated' reference on   
 * 							methods. These are still used.
 * 							modify get\setCustBaseRegFee(), 
 * 							get\setCustDieselFee()
 * 							defect 9631 Ver Defect POS E
 * B Hargrove	06/08/2009	Remove all 'Cancelled Sticker' references.
 * 							delete getCancStkrCd(), getCancStkrDt(), 
 * 							getCancStkrExpYr(), getCancStkrIndi(),
 * 							getCancStkrVin(), 
 * 							setCancStkrCd(), setCancStkrDt(), 
 * 							setCancStkrExpYr(), setCancStkrIndi(),
 * 							setCancStkrVin()
 * 							defect 9953 Ver Defect_POS_F
 * K Harrell	07/03/2009	return new AddressData if RenewalAddressData 
 * 							is null.
 * 							modify getRenwlMailAddr()	
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	07/10/2009	add isApportioned() 
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	03/19/2010	add isOffHwyUse() 
 * 							defect 10416 Ver POS_640
 * K Harrell	03/23/2010	add ciSOReelectionIndi, 
 * 							 csRecpntEMail, get/set methods
 * 							delete csOrgNo, setOrgNo()  
 * 							defect 10366 Ver POS_640   
 * K Harrell	04/21/2010	add isATV()
 * 							defect 10453 Ver POS_640 
 * K Harrell	04/26/2010	add isATVROV()
 * 							delete isATV()
 * 							defect 10453 Ver POS_640 
 * K Harrell	06/15/2010	delete ciSOReelectionIndi, get/set methods
 * 							add ciEMailRenwlReqCd, get/set methods
 * 							defect 10508 Ver 6.5.0 
 * K Harrell	07/11/2010	add isEReminder(), isValidEMail()
 * 							defect 10508 Ver 6.5.0
 * K Harrell	03/11/2011	add isExpired 
 * 							defect 10768 Ver 6.7.1
 * K Harrell	11/04/2011	add isTokenTrailer()
 * 							defect 11138 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * RegistrationData 
 * 
 * @version	6.9.0 			11/04/2011
 * @author	Marx Rajangam
 * @since 					08/22/2001 10:41:50 
 */

public class RegistrationData
	implements java.io.Serializable, Comparable
{

	// boolean 
	private boolean cbInsuranceRequired = true;
	private boolean cbInsuranceVerified;

	// int
	private int ciApprndCntyNo;
	private int ciCanclPltDt;
	private int ciCancPltIndi;
	private int ciCancRegExpMo;
	private int ciClaimComptCntyNo;
	private int ciDpsSaftySuspnIndi;
	// defect 8329
	//	RegInvldIndi for DTA only
	private int ciDTARegInvlIndi;
	private int ciEffDt;
	private int ciEffTime;
	private int ciExmptIndi;
	private int ciExpDt;
	private int ciExpTime;
	private int ciFileTierCd;
	private int ciHvyVehUseTaxIndi;
	private int ciNoChrgIndi;
	private int ciOffHwyUseIndi;
	private int ciOwnrSuppliedExpYr;
	private int ciPltBirthDate;
	private int ciPltRmvdCd;
	private int ciPltSeizedIndi;
	private int ciPrevExpMo;
	private int ciPrevExpYr;
	private int ciRegClassCd;
	private int ciRegEffDt;
	private int ciRegExpMo;
	private int ciRegExpYr;
	private int ciRegHotCkIndi;
	private int ciRegInvldIndi;
	private int ciRegIssueDt;
	private int ciRegPltAge;
	private int ciRegWaivedIndi;
	private int ciRenwlMailRtrnIndi;
	private int ciRenwlYrMismatchIndi;
	private int ciResComptCntyNo;
	private int ciSpclRegId;
	private int ciStkrSeizdIndi;
	private int ciSubconId;
	private int ciSubconIssueDt;
	private int ciTrafficWarrantIndi;
	private int ciUnregisterVehIndi;
	private int ciV21PltId;
	private int ciVehCaryngCap;
	private int ciVehGrossWt;

	// String  
	private String csCancPltDocNo;
	private String csCancPltVin;
	private String csDlsCertNo;
	private String csEmissionSourceCd;
	private String csHoopsRegPltNo;
	private String csNotfyngCity;
	private String csOwnrSuppliedPltNo;
	private String csPrevPltNo;
	private String csPrismLvlCd;
	private String csRecpntName;
	private String csRegPltCd;
	private String csRegPltNo;
	private String csRegPltOwnrName;
	private String csRegStkrCd;
	private String csRegStkrNo;
	private String csRmvRegPltNo;
	private String csTireTypeCd;
	private String csVehRegState;
	private String csVTNSource;
	private String csVTRRegEmrgCd1;
	private String csVTRRegEmrgCd2;

	// Dollar 
	private Dollar caCustActlRegFee;
	private Dollar caCustBaseRegFee;
	private Dollar caCustChildFee;
	private Dollar caCustDieselFee;
	private Dollar caCustRdBrdgFee;
	private Dollar caRegRefAmt;

	//	Object
	private AddressData caRenwlMailAddr;

	// defect 10366 
	private String csRecpntEMail;
	// end defect 10366

	// defect 10508 
	private int ciEMailRenwlReqCd;
	// private int ciSOReelectionIndi 
	// end defect 10508  

	private final static long serialVersionUID = 7249555303884416505L;

	/**
	 * This method is used to sort the method and get the maximum
	 * 
	 * @param aaObject Object
	 */
	public int compareTo(Object aaObject)
	{

		RegistrationData laRegData = (RegistrationData) aaObject;

		int liCurrentValue = ciRegExpYr * 100 + ciRegExpMo;
		int liCompareToValue =
			laRegData.getRegExpYr() * 100 + laRegData.getRegExpMo();

		if (liCurrentValue == liCompareToValue)
		{
			return 0;
		}
		if (liCurrentValue < liCompareToValue)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}

	/** 
	 * This method is used to computer plate age
	 * 
	 * @param aiPltBirthDate 
	 * @param aiRegExprYr 
	 * @param aiRegExprMo 
	 * @param abRenewal 
	 * @return 
	 */
	private int computePltAge(
		int aiPltBirthDate,
		int aiRegExprYr,
		int aiRegExprMo,
		boolean abRenewal)
	{
		int liReturnRegYear = -1;
		if (aiPltBirthDate > 0)
		{
			int liCheckMonths =
				(RTSDate.getCurrentDate().getYear() * 12)
					+ RTSDate.getCurrentDate().getMonth();
			// Check to see if we should use the renewal date. 
			// if boolean is false, it is not a renewal.  
			if (abRenewal && aiRegExprYr > 0 && aiRegExprMo > 0)
			{
				int liRenewalExprMonths =
					(aiRegExprYr * 12) + aiRegExprMo;
				// if within renewal window and it is a renewal, use the renewal date 
				if (liRenewalExprMonths >= liCheckMonths
					&& liRenewalExprMonths <= liCheckMonths + 2)
				{
					liCheckMonths = liRenewalExprMonths;
				}

				// add 1 month to cover reg effective Date
				liCheckMonths = liCheckMonths + 1;

			}
			// compute the plt birth date 
			int liPltBirthMonths =
				(aiPltBirthDate / 100 * 12) + (aiPltBirthDate % 100);
			// compute plt age in years 
			liReturnRegYear = (liCheckMonths - liPltBirthMonths) / 12;
		}
		else
		{
			liReturnRegYear = 0;
		}
		return liReturnRegYear;
	}

	/**
	 * Return value of ApprndCntyNo
	 * 
	 * @return int
	 */
	public int getApprndCntyNo()
	{
		return ciApprndCntyNo;
	}

	/**
	 * Return value of CanclPltDt
	 * 
	 * @return int
	 */
	public int getCanclPltDt()
	{
		return ciCanclPltDt;
	}

	/**
	 * Return value of CancPltDocNo
	 * 
	 * @return String
	 */
	public String getCancPltDocNo()
	{
		return csCancPltDocNo;
	}

	/**
	 * Return value of CancPltIndi
	 * 
	 * @return int
	 */
	public int getCancPltIndi()
	{
		return ciCancPltIndi;
	}

	/**
	 * Return value of CancPltVin
	 * 
	 * @return String
	 */
	public String getCancPltVin()
	{
		return csCancPltVin;
	}

	/**
	 * Return value of CancRegExpMo
	 * 
	 * @return int
	 */
	public int getCancRegExpMo()
	{
		return ciCancRegExpMo;
	}

	/**
	 * Return value of ClaimComptCntyNo
	 * 
	 * @return int
	 */
	public int getClaimComptCntyNo()
	{
		return ciClaimComptCntyNo;
	}

	/**
	 * Return value of CustActlRegFee
	 * 
	 * @return Dollar
	 */
	public Dollar getCustActlRegFee()
	{
		return caCustActlRegFee;
	}

	/**
	 * Return value of CustBaseRegFee
	 * 
	 * @return Dollar
	 */
	public Dollar getCustBaseRegFee()
	{
		return caCustBaseRegFee;
	}

	/**
	 * Return value of CustChildFee
	 * 
	 * @return Dollar
	 */
	public Dollar getCustChildFee()
	{
		return caCustChildFee;
	}
	/**
	 * Return value of CustDieselFee
	 * 
	 * @return Dollar
	 */
	public Dollar getCustDieselFee()
	{
		return caCustDieselFee;
	}

	/**
	 * Return value of CustRdBrdgFee
	 * 
	 * @return Dollar
	 */
	public Dollar getCustRdBrdgFee()
	{
		return caCustRdBrdgFee;
	}

	/**
	 * Return value of DlsCertNo
	 * 
	 * @return String
	 */
	public String getDlsCertNo()
	{
		return csDlsCertNo;
	}

	/**
	 * Return value of DpsSaftySuspnIndi
	 * 
	 * @return int
	 */
	public int getDpsSaftySuspnIndi()
	{
		return ciDpsSaftySuspnIndi;
	}

	/**
	 * Get DTARegInvlIndi for TTL003 indicators
	 * 
	 * @return int
	 */
	public int getDTARegInvlIndi()
	{
		return ciDTARegInvlIndi;
	}

	/**
	 * Return value of EffDt
	 * 
	 * @return int
	 */
	public int getEffDt()
	{
		return ciEffDt;
	}

	/**
	 * Return value of EffTime
	 * 
	 * @return int
	 */
	public int getEffTime()
	{
		return ciEffTime;
	}

	/**
	 * Sets value of ciEMailRenwlReqCd
	 * 
	 * @return int 
	 */
	public int getEMailRenwlReqCd()
	{
		return ciEMailRenwlReqCd;
	}

	/**
	 * Return value of EmissionSourceCd
	 * 
	 * @return String
	 */
	public String getEmissionSourceCd()
	{
		return csEmissionSourceCd;
	}

	/**
	 * Return value of ExmptIndi
	 * 
	 * @return int
	 */
	public int getExmptIndi()
	{
		return ciExmptIndi;
	}

	/**
	 * Return value of ExpDt
	 * 
	 * @return int
	 */
	public int getExpDt()
	{
		return ciExpDt;
	}

	/** 
	 * Return the number of Days Expired
	 * 
	 * @return int 
	 */
	public int getNoDaysExpired()
	{
		int liNoDaysExpired = 0;

		return liNoDaysExpired;

	}
	/** 
	 * Return the number of Months Expired
	 * 
	 * @return int
	 */
	public int getNoMonthsExpired()
	{

		int liNoMonthsExpired = 0;

		return liNoMonthsExpired;

	}

	/**
	 * Return value of ExpTime
	 * 
	 * @return int
	 */
	public int getExpTime()
	{
		return ciExpTime;
	}

	/**
	 * Return value of FileTierCd
	 * 
	 * @return int
	 */
	public int getFileTierCd()
	{
		return ciFileTierCd;
	}

	/**
	 * Return value of HoopsRegPltNo
	 * 
	 * @return String
	 */
	public String getHoopsRegPltNo()
	{
		return csHoopsRegPltNo;
	}

	/**
	 * Return value of HvyVehUseTaxIndi
	 * 
	 * @return int
	 */
	public int getHvyVehUseTaxIndi()
	{
		return ciHvyVehUseTaxIndi;
	}

	/**
	 * Return the Insurance Verified boolean.
	 * 
	 * @return boolean
	 */
	public boolean getInsuranceVerified()
	{
		return cbInsuranceVerified;
	}

	/**
	 * Return value of NoChrgIndi
	 * 
	 * @return int
	 */
	public int getNoChrgIndi()
	{
		return ciNoChrgIndi;
	}

	/**
	 * Return value of NotfyngCity
	 * 
	 * @return String
	 */
	public String getNotfyngCity()
	{
		return csNotfyngCity;
	}

	/**
	 * Return value of OffHwyUseIndi
	 * 
	 * @return int
	 */
	public int getOffHwyUseIndi()
	{
		return ciOffHwyUseIndi;
	}

	/**
	 * Return value of OwnrSuppliedExpYr
	 * 
	 * @return int
	 */
	public int getOwnrSuppliedExpYr()
	{
		return ciOwnrSuppliedExpYr;
	}

	/**
	 * Return value of OwnrSuppliedPltNo
	 * 
	 * @return String
	 */
	public String getOwnrSuppliedPltNo()
	{
		return csOwnrSuppliedPltNo;
	}

	/**
	 * Return value of PltBirthDate
	 * 
	 * @return int
	 */
	public int getPltBirthDate()
	{
		return ciPltBirthDate;
	}

	/**
	 * get PTO Plate Remove Code Code
	 * 
	 * @return int
	 */
	public int getPltRmvCd()
	{
		return ciPltRmvdCd;
	}

	/**
	 * Return value of PltSeizedIndi
	 * 
	 * @return int
	 */
	public int getPltSeizedIndi()
	{
		return ciPltSeizedIndi;
	}

	/**
	 * Return value of PrevExpMo
	 * 
	 * @return int
	 */
	public int getPrevExpMo()
	{
		return ciPrevExpMo;
	}

	/**
	 * Return value of PrevExpYr
	 * 
	 * @return int
	 */
	public int getPrevExpYr()
	{
		return ciPrevExpYr;
	}

	/**
	 * Return value of PrevPltNo
	 * 
	 * @return String
	 */
	public String getPrevPltNo()
	{
		return csPrevPltNo;
	}

	/**
	 * get PrimsLvlCd
	 * 
	 * @return String
	 */
	public String getPrismLvlCd()
	{
		return csPrismLvlCd;
	}

	/**
	 * Return value of RecpntName
	 * 
	 * @return String
	 */
	public String getRecpntName()
	{
		return csRecpntName;
	}

	/**
	 * Return value of RegClassCd
	 * 
	 * @return int
	 */
	public int getRegClassCd()
	{
		return ciRegClassCd;
	}

	/**
	 * Return value of RegEffDt
	 * 
	 * @return int
	 */
	public int getRegEffDt()
	{
		return ciRegEffDt;
	}

	/**
	 * Return value of RegExpMo
	 * 
	 * @return int
	 */
	public int getRegExpMo()
	{
		return ciRegExpMo;
	}

	/**
	 * Return value of RegExpYr
	 * 
	 * @return int
	 */
	public int getRegExpYr()
	{
		return ciRegExpYr;
	}

	/**
	 * Return value of RegHotCkIndi
	 * 
	 * @return int
	 */
	public int getRegHotCkIndi()
	{
		return ciRegHotCkIndi;
	}

	/**
	 * Return value of RegInvldIndi
	 * 
	 * @return int
	 */
	public int getRegInvldIndi()
	{
		return ciRegInvldIndi;
	}

	/**
	 * Return value of RegIssueDt
	 * 
	 * @return int
	 */
	public int getRegIssueDt()
	{
		return ciRegIssueDt;
	}

	/** 
	 * Return value of RegPltAge
	 * 
	 * @param abRenewal boolean
	 * @return ciRegPltAge int 
	*/
	public int getRegPltAge(boolean abRenewal)
	{
		if (ciRegPltAge < 0)
		{
			ciRegPltAge =
				computePltAge(
					ciPltBirthDate,
					getRegExpYr(),
					getRegExpMo(),
					abRenewal);
		}
		return ciRegPltAge;
	}

	/**
	 * Return value of RegPltCd
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Return value of RegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Return value of RegPltOwnrName
	 * 
	 * @return String
	 */
	public String getRegPltOwnrName()
	{
		return csRegPltOwnrName;
	}

	/**
	 * Return value of RegRefAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getRegRefAmt()
	{
		return caRegRefAmt;
	}

	/**
	 * Return value of RegStkrCd
	 * 
	 * @return String
	 */
	public String getRegStkrCd()
	{
		return csRegStkrCd;
	}

	/**
	 * Return value of RegStkrNo
	 * 
	 * @return String
	 */
	public String getRegStkrNo()
	{
		return csRegStkrNo;
	}

	/**
	 * Return value of RegWaivedIndi
	 * 
	 * @return int
	 */
	public int getRegWaivedIndi()
	{
		return ciRegWaivedIndi;
	}

	/**
	 * Return value of RenwlMailAddr
	 * 
	 * @return AddressData
	 */
	public AddressData getRenwlMailAddr()
	{
		// defect 10112 
		caRenwlMailAddr =
			caRenwlMailAddr == null
				? new AddressData()
				: caRenwlMailAddr;
		// end defect 10112 

		return caRenwlMailAddr;
	}

	/**
	 * Gets value of csRecpntEMail
	 * 
	 * @return String 
	 */
	public String getRecpntEMail()
	{
		return csRecpntEMail;
	}

	/**
	 * Return value of RenwlMailRtrnIndi
	 * 
	 * @return int
	 */
	public int getRenwlMailRtrnIndi()
	{
		return ciRenwlMailRtrnIndi;
	}

	/**
	 * Return value of RenwlYrMismatchIndi
	 * 
	 * @return int
	 */
	public int getRenwlYrMismatchIndi()
	{
		return ciRenwlYrMismatchIndi;
	}

	/**
	 * Return value of ResComptCntyNo
	 * 
	 * @return int
	 */
	public int getResComptCntyNo()
	{
		return ciResComptCntyNo;
	}
	/**
	 * get Removed Regis Plate Number
	 * 
	 * @return String
	 */
	public String getRmvdRegPltNo()
	{
		return csRmvRegPltNo;
	}

	/**
	 * Returns value of ciSpclRegId
	 * 
	 * @return int
	 */
	public int getSpclRegId()
	{
		return ciSpclRegId;
	}

	/**
	 * Return value of StkrSeizdIndi
	 * 
	 * @return int
	 */
	public int getStkrSeizdIndi()
	{
		return ciStkrSeizdIndi;
	}

	/**
	 * Return value of SubconId
	 * 
	 * @return int
	 */
	public int getSubconId()
	{
		return ciSubconId;
	}

	/**
	 * Return value of SubconIssueDt
	 * 
	 * @return int
	 */
	public int getSubconIssueDt()
	{
		return ciSubconIssueDt;
	}

	/**
	 * Return value of TireTypeCd
	 * 
	 * @return String
	 */
	public String getTireTypeCd()
	{
		return csTireTypeCd;
	}

	/**
	 * Return value of TrafficWarrantIndi
	 * 
	 * @return int
	 */
	public int getTrafficWarrantIndi()
	{
		return ciTrafficWarrantIndi;
	}

	/**
	 * Return value of VehCaryngCap
	 * 
	 * @return int
	 */
	public int getUnregisterVehIndi()
	{
		return ciUnregisterVehIndi;
	}

	/**
	 * get V21PltId
	 * 
	 * @return int
	 */
	public int getV21PltId()
	{
		return ciV21PltId;
	}

	/**
	 * Return value of VehCaryngCap
	 * 
	 * @return int
	 */
	public int getVehCaryngCap()
	{
		return ciVehCaryngCap;
	}

	/**
	 * Return value of VehGrossWt
	 * 
	 * @return int
	 */
	public int getVehGrossWt()
	{
		return ciVehGrossWt;
	}

	/**
	 * Return value of VehRegState
	 * 
	 * @return String
	 */
	public String getVehRegState()
	{
		return csVehRegState;
	}

	/**
	 * get VTNSource
	 * 
	 * @return String
	 */
	public String getVTNSource()
	{
		return csVTNSource;
	}

	// defect 9961
	//	getter/setter for VTRRegEmrgCd1
	/**
	 * Get VTRRegEmrgCd1
	 *
	 * @return String
	 */
	public String getVTRRegEmrgCd1()
	{
		return csVTRRegEmrgCd1;
	}
	/**
	 * Get VTRRegEmrgCd2
	 *
	 * @return String
	 */
	public String getVTRRegEmrgCd2()
	{
		return csVTRRegEmrgCd2;
	}

	/** 
	 * 
	 * Is Apportioned
	 * 
	 * @return boolean 
	 */
	public boolean isApportioned()
	{
		return csRegPltCd != null
			&& csRegPltCd.lastIndexOf(RegistrationConstant.APPR) != -1;
	}

	/**
	 * Is EReminder
	 * 
	 * @return boolean 
	 */
	public final boolean isEReminder()
	{
		return ciEMailRenwlReqCd == RegistrationConstant.EREMINDER;
	}

	/**
	 * Is Expired
	 * 
	 * @return boolean 
	 */
	public final boolean isExpired()
	{
		return RTSDate.getCurrentDate().getYear() * 12
			+ RTSDate.getCurrentDate().getMonth()
			> getRegExpYr() * 12 + getRegExpMo();
	}

	/**
	 * Boolean indicating if Insurance has Required.
	 * 
	 * @return boolean
	 */
	public final boolean isInsuranceRequired()
	{
		return cbInsuranceRequired;
	}
	
	/**
	 * Boolean indicating if Insurance has Verified.
	 * 
	 * @return boolean
	 */
	public final boolean isInsuranceVerified()
	{
		return cbInsuranceVerified;
	}

	/**
	 * Is Off Highway Use Indictor set
	 * 
	 * @return boolean  
	 */
	public boolean isOffHwyUse()
	{
		return ciOffHwyUseIndi == 1;
	}

	/** 
	 * Is ATV or ROV
	 * 
	 * @return boolean 
	 */
	public boolean isATV_ROV()
	{
		return ciRegClassCd == RegistrationConstant.ATV_REGCLASSCD
			|| ciRegClassCd == RegistrationConstant.ROV_REGCLASSCD;
	}
	
	/**
	 * Is Token Trailer 
	 * 
	 *  @return boolean 
	 */
	public boolean isTokenTrailer()
	{
		return ciRegClassCd == RegistrationConstant.REGCLASSCD_TOKEN_TRLR;
	}
	/**
	 * Is Valid getRecpntEMail && getRecpntEMail/EMailRenwlReqCd
	 *   
	 *  Invalid if  
	 *    - EMail address is invalid or  
	 *    - EMailRenwlCd = 1 and EMail address is empty
	 *   
	 * @return boolean 
	 */
	public boolean isValidEMail()
	{
		boolean lbValid = true;
		if (!UtilityMethods.isEmpty(getRecpntEMail()))
		{
			lbValid = CommonValidations.isValidEMail(getRecpntEMail());
		}
		else
		{
			lbValid = !isEReminder();
		}
		return lbValid;
	}

	/**
	 * Reset select fields, Registration (CICS R01, R04)
	 * 
	 * @param aaRegisData
	 * @return RegistrationData 
	 */
	public RegistrationData resetFields(RegistrationData aaRegisData)
	{
		RegistrationData laRegisData = aaRegisData;

		// Set RegPltNo to "NOPLATE" if PltRmvdCd > 0
		// RegPltNo will be save to RmvdPltNo
		if (laRegisData.getPltRmvCd() > 0)
		{
			laRegisData.setRmvdRegPltNo(laRegisData.getRegPltNo());
			laRegisData.setRegPltNo(RegistrationConstant.NOPLATE);
		}
		return laRegisData;
	}

	/**
	 * Reset select fields, Multiple Registrations (CICS R33)
	 * 
	 * @param avData
	 * @return Vector 
	 */
	public Vector resetFieldsMultRegis(Vector avData)
	{
		Vector lvData = avData;
		Vector lvRtnData = new Vector();
		RegistrationData laRegisData = new RegistrationData();

		for (int liIndex = 0; liIndex < lvData.size(); ++liIndex)
		{
			laRegisData = (RegistrationData) lvData.get(liIndex);

			// Set RegPltNo to "NOPLATE" if PltRmvdCd > 0
			// RegPltNo will be save to RmvdPltNo
			if (laRegisData.getPltRmvCd() > 0)
			{
				laRegisData.setRmvdRegPltNo(laRegisData.getRegPltNo());
				laRegisData.setRegPltNo(RegistrationConstant.NOPLATE);
			}

			// Add Regis record to return vector
			lvRtnData.add(liIndex, laRegisData);
		}

		return lvRtnData;
	}
	// end defect 9459

	/**
	 * Set value of ApprndCntyNo
	 *
	 * @param aiApprndCntyNo int
	 */
	public void setApprndCntyNo(int aiApprndCntyNo)
	{
		ciApprndCntyNo = aiApprndCntyNo;
	}

	/**
	 * Set value of CanclPltDt
	 * 
	 * @param aiCanclPltDt int
	 */
	public void setCanclPltDt(int aiCanclPltDt)
	{
		ciCanclPltDt = aiCanclPltDt;
	}

	/**
	 * Set value of CancPltDocNo
	 * 
	 * @param asCancPltDocNo
	 */
	public void setCancPltDocNo(String asCancPltDocNo)
	{
		csCancPltDocNo = asCancPltDocNo;
	}

	/**
	 * Set value of CancPltIndi
	 * 
	 * @param aiCancPltIndi int
	 */
	public void setCancPltIndi(int aiCancPltIndi)
	{
		ciCancPltIndi = aiCancPltIndi;
	}

	/**
	 * Set value of CancPltVin
	 * 
	 * @param asCancPltVin String
	 */
	public void setCancPltVin(String asCancPltVin)
	{
		csCancPltVin = asCancPltVin;
	}

	/**
	 * Set value of CancRegExpMo
	 * 
	 * @param aiCancRegExpMo int
	 */
	public void setCancRegExpMo(int aiCancRegExpMo)
	{
		ciCancRegExpMo = aiCancRegExpMo;
	}

	/**
	 * Set value of ClaimComptCntyNo
	 * 
	 * @param aiClaimComptCntyNo int
	 */
	public void setClaimComptCntyNo(int aiClaimComptCntyNo)
	{
		ciClaimComptCntyNo = aiClaimComptCntyNo;
	}

	/**
	 * Set value of CustActlRegFee
	 * 
	 * @param aaCustActlRegFee Dollar
	 */
	public void setCustActlRegFee(Dollar aaCustActlRegFee)
	{
		caCustActlRegFee = aaCustActlRegFee;
	}
	/**
	 * Set value of CustBaseRegFee
	 * 
	 * @param aaCustBaseRegFee Dollar
	 */
	public void setCustBaseRegFee(Dollar aaCustBaseRegFee)
	{
		caCustBaseRegFee = aaCustBaseRegFee;
	}

	/**
	 * Set value of CustChildFee
	 * 
	 * @param aaCustChildFee Dollar
	 */
	public void setCustChildFee(Dollar aaCustChildFee)
	{
		caCustChildFee = aaCustChildFee;
	}
	/**
	 * Set value of CustDieselFee
	 * 
	 * @param aaCustDieselFee Dollar
	 */
	public void setCustDieselFee(Dollar aaCustDieselFee)
	{
		caCustDieselFee = aaCustDieselFee;
	}

	/**
	 * Set value of CustRdBrdgFee
	 * 
	 * @param aaCustRdBrdgFee Dollar
	 */
	public void setCustRdBrdgFee(Dollar aaCustRdBrdgFee)
	{
		caCustRdBrdgFee = aaCustRdBrdgFee;
	}

	/**
	 * Set value of DlsCertNo
	 * 
	 * @param asDlsCertNo String
	 */
	public void setDlsCertNo(String asDlsCertNo)
	{
		csDlsCertNo = asDlsCertNo;
	}

	/**
	 * Set value of DpsSaftySuspnIndi
	 * 
	 * @param aiDpsSaftySuspnIndi int
	 */
	public void setDpsSaftySuspnIndi(int aiDpsSaftySuspnIndi)
	{
		ciDpsSaftySuspnIndi = aiDpsSaftySuspnIndi;
	}

	/**
	 * Set DTARegInvlIndi for DTA only
	 * This variable is set in 
	 * 
	 * @param aiDTARegInvldIndi	int
	 */
	public void setDTARegInvlIndi(int aiDTARegInvldIndi)
	{
		ciDTARegInvlIndi = aiDTARegInvldIndi;
	}

	/**
	 * Set value of EffDt
	 * 
	 * @param aiEffDt int
	 */
	public void setEffDt(int aiEffDt)
	{
		ciEffDt = aiEffDt;
	}

	/**
	 * Set value of EffTime
	 * 
	 * @param aiEffTime int
	 */
	public void setEffTime(int aiEffTime)
	{
		ciEffTime = aiEffTime;
	}

	/**
	 * Sets value of ciEMailRenwlReqCd
	 * 
	 * @param aiEMailRenwlReqCd
	 */
	public void setEMailRenwlReqCd(int aiEMailRenwlReqCd)
	{
		ciEMailRenwlReqCd = aiEMailRenwlReqCd;
	}

	/**
	 * Set value of EmissionSourceCd
	 * 
	 * @param asEmissionSourceCd String
	 */
	public void setEmissionSourceCd(String asEmissionSourceCd)
	{
		csEmissionSourceCd = asEmissionSourceCd;
	}

	/**
	 * Set value of ExmptIndi
	 * 
	 * @param aiExmptIndi int
	 */
	public void setExmptIndi(int aiExmptIndi)
	{
		ciExmptIndi = aiExmptIndi;
	}

	/**
	 * Set value of ExpDt
	 * 
	 * @param aiExpDt int
	 */
	public void setExpDt(int aiExpDt)
	{
		ciExpDt = aiExpDt;
	}

	/**
	 * Set value of ExpTime
	 * 
	 * @param aiExpTime int
	 */
	public void setExpTime(int aiExpTime)
	{
		ciExpTime = aiExpTime;
	}

	/**
	 * Set value of FileTierCd
	 * 
	 * @param aiFileTierCd int
	 */
	public void setFileTierCd(int aiFileTierCd)
	{
		ciFileTierCd = aiFileTierCd;
	}

	/**
	 * Set value of HoopsRegPltNo
	 * 
	 * @param asHoopsRegPltNo String
	 */
	public void setHoopsRegPltNo(String asHoopsRegPltNo)
	{
		csHoopsRegPltNo = asHoopsRegPltNo;
	}

	/**
	 * Set value of HvyVehUseTaxIndi
	 * 
	 * @param aiHvyVehUseTaxIndi int
	 */
	public void setHvyVehUseTaxIndi(int aiHvyVehUseTaxIndi)
	{
		ciHvyVehUseTaxIndi = aiHvyVehUseTaxIndi;
	}

	/**
	 * Set the InsuranceRequired boolean.
	 * 
	 * @param boolean abInsuranceRequired
	 */
	public final void setInsuranceRequired(boolean abInsuranceRequired)
	{
		cbInsuranceRequired = abInsuranceRequired;
	}
	/**
	 * Set the InsuranceRequired boolean.
	 * 
	 * @param boolean abInsuranceRequired
	 */
	public final void setInsuranceVerified(boolean abInsuranceVerified)
	{
		cbInsuranceVerified = abInsuranceVerified;
	}

	/**
	 * Set value of NoChrgIndi
	 * 
	 * @param aiNoChrgIndi int
	 */
	public void setNoChrgIndi(int aiNoChrgIndi)
	{
		ciNoChrgIndi = aiNoChrgIndi;
	}

	/**
	 * Set value of NotfyngCity
	 * 
	 * @param asNotfyngCity String
	 */
	public void setNotfyngCity(String asNotfyngCity)
	{
		csNotfyngCity = asNotfyngCity;
	}

	/**
	 * Set value of OffHwyUseIndi
	 * 
	 * @param aiOffHwyUseIndi int
	 */
	public void setOffHwyUseIndi(int aiOffHwyUseIndi)
	{
		ciOffHwyUseIndi = aiOffHwyUseIndi;
	}

	/**
	 * Set value of OwnrSuppliedExpYr
	 * 
	 * @param aiOwnrSuppliedExpYr int
	 */
	public void setOwnrSuppliedExpYr(int aiOwnrSuppliedExpYr)
	{
		ciOwnrSuppliedExpYr = aiOwnrSuppliedExpYr;
	}

	/**
	 * Set value of OwnrSuppliedPltNo
	 * 
	 * @param asOwnrSuppliedPltNo String
	 */
	public void setOwnrSuppliedPltNo(String asOwnrSuppliedPltNo)
	{
		csOwnrSuppliedPltNo = asOwnrSuppliedPltNo;
	}

	/**
	 * Set value of PltBirthDate
	 * 
	 * @param aiPltBirthDate int
	 */
	public void setPltBirthDate(int aiPltBirthDate)
	{
		ciPltBirthDate = aiPltBirthDate;
	}

	/**
	 * set PTO Plate Remove Code
	 * 
	 * @param aiPltRmvCd
	 */
	public void setPltRmvCd(int aiPltRmvCd)
	{
		ciPltRmvdCd = aiPltRmvCd;
	}

	/**
	 * Set value of PltSeizedIndi
	 * 
	 * @param aiPltSeizedIndi int
	 */
	public void setPltSeizedIndi(int aiPltSeizedIndi)
	{
		ciPltSeizedIndi = aiPltSeizedIndi;
	}

	/**
	 * Set value of PrevExpMo
	 * 
	 * @param aiPrevExpMo int
	 */
	public void setPrevExpMo(int aiPrevExpMo)
	{
		ciPrevExpMo = aiPrevExpMo;
	}

	/**
	 * Set value of PrevExpYr
	 * 
	 * @param aiPrevExpYr int
	 */
	public void setPrevExpYr(int aiPrevExpYr)
	{
		ciPrevExpYr = aiPrevExpYr;
	}

	/**
	 * Set value of PrevPltNo
	 * 
	 * @param asPrevPltNo String
	 */
	public void setPrevPltNo(String asPrevPltNo)
	{
		csPrevPltNo = asPrevPltNo;
	}

	/**
	 * set PrimsLvlCd
	 * 
	 * @param string
	 */
	public void setPrismLvlCd(String asPrismLvlCd)
	{
		csPrismLvlCd = asPrismLvlCd;
	}

	/**
	 * Set value of RecpntName
	 * 
	 * @param asRecpntName String
	 */
	public void setRecpntName(String asRecpntName)
	{
		csRecpntName = asRecpntName;
	}

	/**
	 * Set value of RegClassCd
	 * 
	 * @param aiRegClassCd int
	 */
	public void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}

	/**
	 * Set value of RegEffDt
	 * 
	 * @param aiRegEffDt int
	 */
	public void setRegEffDt(int aiRegEffDt)
	{
		ciRegEffDt = aiRegEffDt;
	}

	/**
	 * Set value of RegExpMo
	 * 
	 * @param aiRegExpMo int
	 */
	public void setRegExpMo(int aiRegExpMo)
	{
		ciRegExpMo = aiRegExpMo;
	}

	/**
	 * Set value of RegExpYr
	 * 
	 * @param aiRegExpYr int
	 */
	public void setRegExpYr(int aiRegExpYr)
	{
		ciRegExpYr = aiRegExpYr;
	}

	/**
	 * Set value of RegHotCkIndi
	 * 
	 * @param aiRegHotCkIndi int
	 */
	public void setRegHotCkIndi(int aiRegHotCkIndi)
	{
		ciRegHotCkIndi = aiRegHotCkIndi;
	}

	/**
	 * Set value of RegInvldIndi
	 * 
	 * @param aiRegInvldIndi int
	 */
	public void setRegInvldIndi(int aiRegInvldIndi)
	{
		ciRegInvldIndi = aiRegInvldIndi;
	}

	/**
	 * Set value of RegIssueDt 
	 * 
	 * @param aiRegIssueDt int
	 */
	public void setRegIssueDt(int aiRegIssueDt)
	{
		ciRegIssueDt = aiRegIssueDt;
	}

	/**
	 * Set value of RegPltAge
	 * 
	 * @param aiRegPltAge int
	 */
	public void setRegPltAge(int aiRegPltAge)
	{
		ciRegPltAge = aiRegPltAge;
	}

	/**
	 * Set value of RegPltCd
	 * 
	 * @param asRegPltCd String
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Set value of RegPltNo
	 * 
	 * @param asRegPltNo String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Set value of RegPltOwnrName
	 * 
	 * @param asRegPltOwnrName String
	 */
	public void setRegPltOwnrName(String asRegPltOwnrName)
	{
		csRegPltOwnrName = asRegPltOwnrName;
	}

	/**
	 * Set value of RegRefAmt
	 * 
	 * @param aaRegRefAmt Dollar
	 */
	public void setRegRefAmt(Dollar aaRegRefAmt)
	{
		caRegRefAmt = aaRegRefAmt;
	}

	/**
	 * Set value of RegStkrCd
	 * 
	 * @param asRegStkrCd String
	 */
	public void setRegStkrCd(String asRegStkrCd)
	{
		csRegStkrCd = asRegStkrCd;
	}

	/**
	 * Set value of RegStkrNo
	 * 
	 * @param asRegStkrNo String
	 */
	public void setRegStkrNo(String asRegStkrNo)
	{
		csRegStkrNo = asRegStkrNo;
	}

	/**
	 * Set value of RegWaivedIndi
	 * 
	 * @param aiRegWaivedIndi int
	 */
	public void setRegWaivedIndi(int aiRegWaivedIndi)
	{
		ciRegWaivedIndi = aiRegWaivedIndi;
	}

	/**
	 * Set value of RenwlMailAddr
	 * 
	 * @param aaRenwlMailAddr AddressData
	 */
	public void setRenwlMailAddr(AddressData aaRenwlMailAddr)
	{
		caRenwlMailAddr = aaRenwlMailAddr;
	}

	/**
	 * Sets value of csRecpntEMail
	 * 
	 * @param asRecpntEMail
	 */
	public void setRecpntEMail(String asRecpntEMail)
	{
		csRecpntEMail = asRecpntEMail;
	}

	/**
	 * Set value of RenwlMailRtrnIndi
	 * 
	 * @param aiRenwlMailRtrnIndi int
	 */
	public void setRenwlMailRtrnIndi(int aiRenwlMailRtrnIndi)
	{
		ciRenwlMailRtrnIndi = aiRenwlMailRtrnIndi;
	}

	/**
	 * Set value of RenwlYrMismatchIndi
	 * 
	 * @param aiRenwlYrMismatchIndi int
	 */
	public void setRenwlYrMismatchIndi(int aiRenwlYrMismatchIndi)
	{
		ciRenwlYrMismatchIndi = aiRenwlYrMismatchIndi;
	}

	/**
	 * Set value of ResComptCntyNo
	 * 
	 * @param aiResComptCntyNo int
	 */
	public void setResComptCntyNo(int aiResComptCntyNo)
	{
		ciResComptCntyNo = aiResComptCntyNo;
	}

	/**
	 * set Removed Regis Plate Number
	 * 
	 * @param asRmvRegPltNo
	 */
	public void setRmvdRegPltNo(String asRmvRegPltNo)
	{
		csRmvRegPltNo = asRmvRegPltNo;
	}

	/**
	 * Sets value of SpclRegId 
	 * 
	 * @param asSpclRegId int
	 */
	public void setSpclRegId(int asSpclRegId)
	{
		ciSpclRegId = asSpclRegId;
	}

	/**
	 * Set value of StkrSeizdIndi
	 * 
	 * @param aiStkrSeizdIndi int
	 */
	public void setStkrSeizdIndi(int aiStkrSeizdIndi)
	{
		ciStkrSeizdIndi = aiStkrSeizdIndi;
	}

	/**
	 * Set value of SubconId
	 * 
	 * @param aiSubconId int
	 */
	public void setSubconId(int aiSubconId)
	{
		ciSubconId = aiSubconId;
	}

	/**
	 * Set value of SubconIssueDt
	 * 
	 * @param aiSubconIssueDt int
	 */
	public void setSubconIssueDt(int aiSubconIssueDt)
	{
		ciSubconIssueDt = aiSubconIssueDt;
	}

	/**
	 * Set value of TireTypeCd
	 * 
	 * @param asTireTypeCd String
	 */
	public void setTireTypeCd(String asTireTypeCd)
	{
		csTireTypeCd = asTireTypeCd;
	}

	/**
	 * Set value of TrafficWarrantIndi
	 * 
	 * @param aiTrafficWarrantIndi int
	 */
	public void setTrafficWarrantIndi(int aiTrafficWarrantIndi)
	{
		ciTrafficWarrantIndi = aiTrafficWarrantIndi;
	}

	/**
	 * Set value of UnregisterVehIndi
	 * 
	 * @param aiUnregisterVehIndi int
	 */
	public void setUnregisterVehIndi(int aiUnregisterVehIndi)
	{
		ciUnregisterVehIndi = aiUnregisterVehIndi;
	}

	/**
	 * set V21PltId
	 * 
	 * @param aiV21PltId
	 */
	public void setV21PltId(int aiV21PltId)
	{
		ciV21PltId = aiV21PltId;
	}

	/**
	 * Set value of VehCaryngCap
	 * 
	 * @param aiVehCaryngCap int
	 */
	public void setVehCaryngCap(int aiVehCaryngCap)
	{
		ciVehCaryngCap = aiVehCaryngCap;
	}

	/**
	 * Set value of VehGrossWt
	 * 
	 * @param aiVehGrossWt int
	 */
	public void setVehGrossWt(int aiVehGrossWt)
	{
		ciVehGrossWt = aiVehGrossWt;
	}

	/**
	 * Set value of VehRegState
	 * 
	 * @param asVehRegState String
	 */
	public void setVehRegState(String asVehRegState)
	{
		csVehRegState = asVehRegState;
	}

	/**
	 * set VTNSource
	 * 
	 * @param aiVTNSource
	 */
	public void setVTNSource(String aiVTNSource)
	{
		csVTNSource = aiVTNSource;
	}
	/**
	 * Set VTRRegEmrgCd1
	 *
	 * @param asVTRRegEmrgCd1 String
	 */
	public void setVTRRegEmrgCd1(String asVTRRegEmrgCd1)
	{
		csVTRRegEmrgCd1 = asVTRRegEmrgCd1;
	}
	/**
	 * Set VTRRegEmrgCd2
	 *
	 * @param asVTRRegEmrgCd2 String
	 */
	public void setVTRRegEmrgCd2(String asVTRRegEmrgCd2)
	{
		csVTRRegEmrgCd2 = asVTRRegEmrgCd2;
	}
}
