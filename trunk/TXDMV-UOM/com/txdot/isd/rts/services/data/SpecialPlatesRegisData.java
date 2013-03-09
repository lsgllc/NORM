package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * SpecialPlatesRegisData.java    
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		01/30/2007	Add getters/setters
 * 							address will be stored in AddressData()
 *							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Add serialVersionUID
 * 							defect 9086 Ver Special Plates
 * B Hargrove 	02/08/2007 	For Special Plates Full implementation,
 * 							add indicator (and getter\setter) 
 * 							for whether to charge the special plate fee.
 * 							add ciSpclPltChrgFeeIndi, 
 * 							getSpclPltChrgFeeIndi(),
 * 							setSpclPltChrgFeeIndi() 
 * 							defect 9126 Ver Special Plates
 * J Rue		02/08/2007	Change SpclFee from int to Dollar
 * 							modify setSpclFee(), getSpclFee()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/09/2007	Remove csPltOwnrFstName and csPltOwnrLstName
 * 							deprecate getPltOwnrLstName()
 * 							deprecate setPltOwnrLstName()
 * 							deprecate getPltOwnrFstName()
 * 							deprecate setPltOwnrFstName()
 * 							defect 9086 Ver Special Plates
 * K Harrell	02/13/2007	Change PltOwnrPhoneNo to String
 * 							Rename ciRegIssueDate to PltApplDate
 * 							  rename getters/setters
 * 							defect 9085 Special Plates
 * K Harrell	02/19/2007	Modify to user OwnerData vs. independent
 * 							fields, to calculate plate age
 * 							add caOwnerData,ciRegPltAge, add get/set methods
 * 							add computePltAge() 
 *  						defect 9085 Ver Special Plates
 * J Rue		02/22/2007	Change variable name ciSpclRegId
 * 							modify getSpclRegId(), setSpclRegId()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/26/2007	Change SpclRegId from int to long
 * 							modify getSpclRegId(), setSpclRegId()
 * 							defect 9086 Ver Special Plates
 * K Harrell	03/04/2007	added ciMfgSpclPltIndi, get/set methods
 * 							defect 9086 Ver Special Plates
 * K Harrell	03/26/2007	calculate the MFGDate based upon 
 * 							day of week, TxDOT Holiday schedule and  	
 * 							whether personalized
 * 							add setMFGDate()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/10/2007	add initWhereNull(),ciNoMonthsToCharge,
 * 							 cbEnterOnSPL002, get/set methods
 * 							defect 9085 Ver Special Plates  
 * B Hargrove	04/12/2007	add  cbCustomerSupplied, get/set methods
 * 							defect 9126 Ver Special Plates  
 * J Rue		04/13/2007	add  cbMFDownSP, get/set methods
 * 							defect 9086 Ver Special Plates
 * K Harrell	05/20/2007	add csItrntTraceNo, get/set methods
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/24/2007	Rename ciResComptCntyNo, get/set methods  
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/29/2007	remove cbCustomerSupplied, get/set methods
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/01/2007	Use UtilityMethods.isSPAPPL() vs. check for
 * 							SPAPPL 
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/06/2007	Add csOrigTransCd, get/set methods
 * 						   	defect 9085 Ver Special Plates
 * K Harrell	06/11/2007	Augment setMfgPltNo() for new DV requirements
 * 							modify setMfgPltNo()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/17/2007	Augment setMfgPltNo() for DV plates w/o 
 * 							"DV".
 * 							modify setMfgPltNo()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/22/2007	add ciOrigRegExpMo, ciOrigRegExpYr, get and
 * 							set methods.
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/11/2007	Do not add Texas silhouette for DVMCP,DVMCPF
 * 							modify setMfgPltNo()
 * 							defect 9356 Ver Special Plates   
 * K Harrell	11/05/2007	Do not set up default MfgPltNo.  
 * 							add cbResetChrgFee
 * 								isResetChrgFee(), setResetChrgFee() 
 * 							modify setMFGDate()
 * 							defect 9389 Ver Special Plates 2   
 * J Rue		03/26/2008	Add new attributes
 * 							add getDissociatedCd(), setDissociatedCd()
 * 								getV21PltId(), setV21PltId()
 * 								getPltOwnrDlrGDNNew(), 
 * 								setPltOwnrDlrGDNNew()
 * 							defect 9581 Ver Defect_POS_A 
 * J Rue		04/30/2008	Rename DissociateCd to PltRmvCd
 * 							add getPltRmvCd(), setPltRmvCd()
 * 							defect 9581 Ver Defect_POS_A 						
 * J Rue		05/06/2008	Reset RegPltNo if RmvPltCd > 0
 * 							Add getter/setter RmvRegPltNo, original
 * 							  RegPltNo saved if PltRmvCd > 0
 * 							add getRmvRegPltNo(), setRmvRegPltNo,
 * 								ResetFields()
 * 							defect 9630 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9630 Ver Defect_POS_A
 * J Rue		06/17/2008	Remove all reference to PltOwnrDlrGDNNew
 * 							delete csPltOwnrDlrGDNNew
 * 							delete getPltOwnrDlrGDNNew(), 
 * 								setPltOwnrDlrGDNNew()
 * 							defect 9557 Ver Defect_POA_A
 * B Brown		06/25/2008	Add Plate term for tr_fds_detail
 * 							add ciPltTerm, getPltTerm(), setPltTerm()
 * 							defect 9711 Ver MyPlates_POS
 * B Brown		07/07/2008  Change mfg date to be 5 business days in 
 * 							the future for TransCdConstant.VPAPPL
 * 							modify setMFGDate()
 * 							defect 9711 Ver MyPlates_POS
 * J Rue		12/31/2008	Add PltExpMo, PltExpYr and PltValidityTerm
 * 							to match ADABAS
 * 							add ciPltExpMo, ciPltExpYr,ciPltValidityTerm
 * 								(getters, setters)
 * 							defect 9655 Ver Defect_POS_D
 * K Harrell	01/07/2009	Removed ciPltExpMo/Yr, get/set methods in order
 * 							to refactor ciRegExpMo/Yr, get/set methods.
 * 							refactor ciRegExpMo/Yr to ciPltExpMo/Yr
 * 							refactor ciOrigRegExpMo/Yr to ciOrigPltExpMo/Yr
 * 							refactor OrigRegExpMo/Yr get/set methods to 
 * 								OrigPltExpMo/Yr get/set methods 
 * 							defect 9864 Ver Defect_POS_D
 * K Harrell	07/03/2009	modify initWhereNull()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	10/14/2009	delete ciPrivacyOptCd, get/set methods
 * 							defect 10246 Ver Defect_POS_G 
 * K Harrell	12/18/2009	delete ciPltTerm, get/set methods 
 * 							defect 10311 Ver Defect_POS_H
 * K Harrell	02/09/2010	delete csSpclRegStkrNo, csVRIMSMFGCd
 * 							add ciAuctnPltIndi, ciMktngAllowdIndi,
 * 							 ciVendorTransDate, csResrvReasnCd,
 * 							 ciResrvEffDate, caAuctnPdAmt, 
 * 							 csFINDocNo, get/set methods.
 * 							add isMktngAllowd(), isAuctnPlt(), isISA()
 * 							defect 10366 Ver POS_640  
 * K Harrell	04/06/2010	modify initWhereNull()
 * 							defect 10366 Ver POS_640
 * K Harrell	06/15/2010	add ciElectionPndngIndi, get/set methods
 * 							add isElectionPndng() 
 * 							defect 10507 Ver 6.5.0  
 * K Harrell	12/20/2010	add cbPrntPrmt, get/set methods. 
 * 							defect 10700 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * SpecialPlatesRegis 
 * 
 * @version	6.7.0			12/20/2010
 * @author	Jeff Rue
 * <br>Creation Date:	   	01/30/2007 17:43:23 
 */

public class SpecialPlatesRegisData implements java.io.Serializable
{
	// boolean 
	private boolean cbChrgRemakeOnReplace;
	private boolean cbEnterOnSPL002;
	private boolean cbMFDownSP;
	private boolean cbResetChrgFee;

	// defect 10700 
	private boolean cbPrntPrmt;
	// end defect 10700

	// int 
	private int ciAccptIndi;
	private int ciAddlSetIndi;
	private int ciDelIndi;
	private int ciExistingDuplIndi;
	private int ciInvItmYr;
	private int ciISAIndi;
	private int ciMFGDate;
	private int ciMfgSpclPltIndi;
	private int ciMnyRcvdDate;
	private int ciNoMonthsToCharge;
	private int ciOrigPltExpMo;
	private int ciOrigPltExpYr;
	private int ciPltApplDate;
	private int ciPltBirthDate;
	private int ciPltExpMo;
	private int ciPltExpYr;
	private int ciPltRmvCd;
	private int ciPltSetNo;
	private int ciPltValidityTerm;
	private int ciRegClassCd;
	private int ciRegEffDate;
	private int ciRegPltAge;
	private int ciResComptCntyNo;
	private int ciSpclPltChrgFeeIndi;
	private int ciV21PltId;

	// long
	private long clSpclRegId;

	// String 
	private String csHOOPSRegPltNo;
	private String csInvCd;
	private String csItrntTraceNo;
	private String csMfgPltNo;
	private String csMFGStatusCd;
	private String csMnyRcvdUpdtCd;
	private String csOrgNo;
	private String csOrigTransCd;
	private String csPltOwnrDist;
	private String csPltOwnrDlrGDN;
	private String csPltOwnrEMail;
	private String csPltOwnrOfcCd;
	private String csPltOwnrPhoneNo;
	private String csRcptCd;
	private String csRegPltCd;
	private String csRegPltNo;
	private String csRequestType;
	private String csRmvRegPltNo;
	private String csSAuditTrailTransId;
	private String csSpclDocNo;
	private String csSpclRemks;
	private String csSpclRenwlCd;
	private String csTransCd;
	private String csTransEmpId;

	//	Object 
	private OwnerData caOwnrData;

	// Dollar
	private Dollar caSpclFee;
	private InventoryAllocationData caVIAllocData;

	// defect 10366
	//private String csSpclRegStkrNo; 
	//private String csVRIMSMFGCd;
	// defect 10366
	private int ciAuctnPltIndi;
	private int ciMktngAllowdIndi;
	private int ciResrvEffDate;
	private int ciVendorTransDate;
	private String csResrvReasnCd;
	private String csFINDocNo;
	private Dollar caAuctnPdAmt;
	// end defect 10366 

	// defect 10507 
	private int ciElectionPndngIndi;
	// end defect 10507 

	static final long serialVersionUID = -8836108020115872198L;

	/**
	 * Special Plates Regis Data constructor comment.
	 */
	public SpecialPlatesRegisData()
	{
		super();

		// defect 10112 
		ciOrigPltExpMo = -1;
		ciOrigPltExpYr = -1;
		caOwnrData = new OwnerData();
		// end defect 10112  
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
	 * Return value of AccptIndi
	 * 
	 * @return int
	 */
	public int getAccptIndi()
	{
		return ciAccptIndi;
	}
	/**
	 * Return value of AddlSetIndi
	 * 
	 * @return int
	 */
	public int getAddlSetIndi()
	{
		return ciAddlSetIndi;
	}

	//	/**
	//	 * Set value of VRIMSMFGCd
	//	 * 
	//	 * @param String
	//	 */
	//	public void setVRIMSMFGCd(String asVRIMSMFGCd)
	//	{
	//		csVRIMSMFGCd = asVRIMSMFGCd;
	//	}

	/**
	 * Gets value of caAuctnPdAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getAuctnPdAmt()
	{
		return caAuctnPdAmt;
	}

	/**
	 * Gets value of ciAuctnPltIndi 
	 * 
	 * @return int 
	 */
	public int getAuctnPltIndi()
	{
		return ciAuctnPltIndi;
	}
	/**
	 * Get value of cbChrgRemakeOnReplace
	 * 
	 * @return boolean
	 */
	public boolean getChrgRemakeOnReplace()
	{
		return cbChrgRemakeOnReplace;
	}
	/**
	 * Return value of DelIndi
	 * 
	 * @return int
	 */
	public int getDelIndi()
	{
		return ciDelIndi;
	}

	/**
	 * Get value of ciElectionPndngIndi
	 * 
	 * @return int
	 */
	public int getElectionPndngIndi()
	{
		return ciElectionPndngIndi;
	}

	/**
	 * Return value of ExistingDuplIndi
	 * 
	 * @return int
	 */
	public int getExistingDuplIndi()
	{
		return ciExistingDuplIndi;
	}

	/**
	 * Gets value of csFINDocNo
	 * 
	 * @return String 
	 */
	public String getFINDocNo()
	{
		return csFINDocNo;
	}
	/**
	 * Return value of HOOPSRegPltNo
	 * 
	 * @return String
	 */
	public String getHOOPSRegPltNo()
	{
		return csHOOPSRegPltNo;
	}
	/**
	 * Return value of InvCd
	 * 
	 * @return String
	 */
	public String getInvCd()
	{
		return csInvCd;
	}

	/**
	 * Get value of InvItmYr
	 * 
	 * @return int
	 */
	public int getInvItmYr()
	{
		return ciInvItmYr;
	}
	/**
	 * Return value of ISAIndi
	 * 
	 * @return int
	 */
	public int getISAIndi()
	{
		return ciISAIndi;
	}

	/**
	 * Return value of ItrntTraceNo
	 * 
	 * @return String 
	 */
	public String getItrntTraceNo()
	{
		return csItrntTraceNo;
	}
	/**
	 * Return value of MFGDate
	 * 
	 * @return int
	 */
	public int getMFGDate()
	{
		return ciMFGDate;
	}
	/**
	 * Return value of MfgPltNo
	 * 
	 * @return String
	 */
	public String getMfgPltNo()
	{
		return csMfgPltNo;
	}
	/**
	 * Return value of ciMfgSpclPltIndi
	 * 
	 * @return
	 */
	public int getMfgSpclPltIndi()
	{
		return ciMfgSpclPltIndi;
	}
	/**
	 * Return value of MFGStatusCd
	 * 
	 * @return String
	 */
	public String getMFGStatusCd()
	{
		return csMFGStatusCd;
	}

	/**
	 * Gets value of ciMktngAllowdIndi
	 * 
	 * @return int 
	 */
	public int getMktngAllowdIndi()
	{
		return ciMktngAllowdIndi;
	}
	/**
	 * Return value of MnyRcvdDate
	 * 
	 * @return int
	 */
	public int getMnyRcvdDate()
	{
		return ciMnyRcvdDate;
	}
	/**
	 * Return value of MnyRcvDupdtCd
	 * 
	 * @return String
	 */
	public String getMnyRcvdUpdtCd()
	{
		return csMnyRcvdUpdtCd;
	}

	/**
	 * Get value of NoMonthsToCharge
	 * 
	 * @return int
	 */
	public int getNoMonthsToCharge()
	{
		return ciNoMonthsToCharge;
	}
	/**
	 * Return value of OrgNo
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}

	/**
	 * Get Original Exp Mo
	 * 
	 * @return ciOrigPltExpMo 
	 */
	public int getOrigPltExpMo()
	{
		return ciOrigPltExpMo;
	}

	/**
	 * Get Original Exp Yr
	 * 
	 * @return ciOrigPltExpYr
	 */
	public int getOrigPltExpYr()
	{
		return ciOrigPltExpYr;
	}

	/**
	 * Return the Original TransCd
	 * 
	 * @return String
	 */
	public String getOrigTransCd()
	{
		return csOrigTransCd;
	}

	/**
	 * Returns value of caOwnrData
	 * 
	 * @return
	 */
	public OwnerData getOwnrData()
	{
		return caOwnrData;
	}
	/**
	 * Return value of ciPltApplDate
	 * 
	 * @return int
	 */
	public int getPltApplDate()
	{
		return ciPltApplDate;
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
	 * Return value of PltExpMo
	 * 
	 * @return int
	 */
	public int getPltExpMo()
	{
		return ciPltExpMo;
	}
	/**
	 * Return value of PltExpYr
	 * 
	 * @return int
	 */
	public int getPltExpYr()
	{
		return ciPltExpYr;
	}
	/**
	 * Return value of PltOwnrDist
	 * 
	 * @return String
	 */
	public String getPltOwnrDist()
	{
		return csPltOwnrDist;
	}

	/**
	 * get Plate Owner Dealer GDN 
	 * 
	 * @return
	 */
	public String getPltOwnrDlrGDN()
	{
		return csPltOwnrDlrGDN;
	}
	/**
	 * Return value of PltOwnrMail
	 * 
	 * @return String
	 */
	public String getPltOwnrEMail()
	{
		return csPltOwnrEMail;
	}
	/**
	 * Return value of PltOwnrOfcCd
	 * 
	 * @return String
	 */
	public String getPltOwnrOfcCd()
	{
		return csPltOwnrOfcCd;
	}

	/**
	 * Return value of PltOwnrPhoneNo
	 * 
	 * @return String
	 */
	public String getPltOwnrPhoneNo()
	{
		return csPltOwnrPhoneNo;
	}

	/**
	 * get Plate Remove Code
	 * 
	 * @return
	 */
	public int getPltRmvCd()
	{
		return ciPltRmvCd;
	}
	/**
	 * Return value of PltSetNo
	 * 
	 * @return int
	 */
	public int getPltSetNo()
	{
		return ciPltSetNo;
	}
	//	/**
	//	 * Get the number of ownership years for the SP
	//	 * 
	//	 * @return int 
	//	 */
	//	public int getPltTerm()
	//	{
	//		return ciPltTerm;
	//	}

	/**
	 * Return PLTVALIDITYTERM
	 * 
	 * @return
	 */
	public int getPltValidityTerm()
	{
		return ciPltValidityTerm;
	}
	//	/**
	//	 * Return value of PrivacyOptCd
	//	 * 
	//	 * @return int
	//	 */
	//	public int getPrivacyOptCd()
	//	{
	//		return ciPrivacyOptCd;
	//	}
	/**
	 * Return value of RcptCd
	 * 
	 * @return String
	 */
	public String getRcptCd()
	{
		return csRcptCd;
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
	 * Return value of RegEffDate
	 * 
	 * @return int
	 */
	public int getRegEffDate()
	{
		return ciRegEffDate;
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
					getPltExpYr(),
					getPltExpMo(),
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
	 * Get value of csRequestType
	 * 
	 * @return String
	 */
	public String getRequestType()
	{
		return csRequestType;
	}

	/**
	 * Return value of RegComptCntyNo
	 * 
	 * @return int
	 */
	public int getResComptCntyNo()
	{
		return ciResComptCntyNo;
	}

	/**
	 * Gets value of ciResrvEffDate
	 * 
	 * @return int 
	 */
	public int getResrvEffDate()
	{
		return ciResrvEffDate;
	}

	/**
	 * Gets value of csResrvReasnCd
	 * 
	 * @return String 
	 */
	public String getResrvReasnCd()
	{
		return csResrvReasnCd;
	}

	/**
	 * get Remove Regis Plate Number
	 * 
	 * @return
	 */
	public String getRmvRegPltNo()
	{
		return csRmvRegPltNo;
	}
	/**
	 * Return value of SAuditTrailTransId
	 * 
	 * @return String
	 */
	public String getSAuditTrailTransId()
	{
		return csSAuditTrailTransId;
	}
	/**
	 * Return value of SpclDocNo
	 * 
	 * @return String
	 */
	public String getSpclDocNo()
	{
		return csSpclDocNo;
	}
	/**
	 * Return value of SpclFee
	 * 
	 * @return Dollar
	 */
	public Dollar getSpclFee()
	{
		return caSpclFee;
	}
	/**
	 * Get value for whether to charge Special Plate Fee
	 * 
	 * @return int
	 */
	public int getSpclPltChrgFeeIndi()
	{
		return ciSpclPltChrgFeeIndi;
	}

	/**
	 * Return value of SpclRegId
	 * 
	 * @return long
	 */
	public long getSpclRegId()
	{
		return clSpclRegId;
	}
	//	/**
	//	 * Return value of SpclRegStkrNo
	//	 * 
	//	 * @return String
	//	 */
	//	public String getSpclRegStkrNo()
	//	{
	//		return csSpclRegStkrNo;
	//	}
	/**
	 * Return value of SpclRemks
	 * 
	 * @return String
	 */
	public String getSpclRemks()
	{
		return csSpclRemks;
	}
	/**
	 * Return value of SpclRenwlCd
	 * 
	 * @return String
	 */
	public String getSpclRenwlCd()
	{
		return csSpclRenwlCd;
	}
	/**
	 * Return value of TransCd
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		return csTransCd;
	}
	/**
	 * Return value of TransEmpId
	 * 
	 * @return String
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
	}

	/**
	 * get ciV21PltId
	 * 
	 * @return
	 */
	public int getV21PltId()
	{
		return ciV21PltId;
	}

	/**
	 * Gets value of ciVendorTransDate
	 * 
	 * @return int 
	 */
	public int getVendorTransDate()
	{
		return ciVendorTransDate;
	}
	/**
	 * Set value of VIAllocData
	 * 
	 * @return InventoryAllocationData
	 */
	public InventoryAllocationData getVIAllocData()
	{
		return caVIAllocData;
	}
	//	/**
	//	 * Return value of VRIMSMFGCd
	//	 * 
	//	 * @return String
	//	 */
	//	public String getVRIMSMFGCd()
	//	{
	//		return csVRIMSMFGCd;
	//	}
	/** 
	 * 
	 * Initialize Strings and Objects where Null
	 * 
	 */
	public void initWhereNull()
	{
		// csHOOPSRegPltNo
		csHOOPSRegPltNo =
			csHOOPSRegPltNo == null ? "" : csHOOPSRegPltNo;

		// csRegPltNo
		csRegPltNo = csRegPltNo == null ? "" : csRegPltNo;

		// csMfgPltNo
		csMfgPltNo = csMfgPltNo == null ? "" : csMfgPltNo;

		// csPltOwnrPhoneNo
		csPltOwnrPhoneNo =
			csPltOwnrPhoneNo == null ? "" : csPltOwnrPhoneNo;

		// csPltOwnrEMail
		csPltOwnrEMail = csPltOwnrEMail == null ? "" : csPltOwnrEMail;

		// csPltOwnrOfcCd
		csPltOwnrOfcCd = csPltOwnrOfcCd == null ? "" : csPltOwnrOfcCd;

		// csPltOwnrDist
		csPltOwnrDist = csPltOwnrDist == null ? "" : csPltOwnrDist;

		// csPltOwnrDlrGDN
		csPltOwnrDlrGDN =
			csPltOwnrDlrGDN == null ? "" : csPltOwnrDlrGDN;

		// csTransCd 
		csTransCd = csTransCd == null ? "" : csTransCd;

		// csRegPltCd
		csRegPltCd = csRegPltCd == null ? "" : csRegPltCd;

		// csMFGStatusCd 
		csMFGStatusCd = csMFGStatusCd == null ? "" : csMFGStatusCd;

		// csOrgNo
		csOrgNo = csOrgNo == null ? "" : csOrgNo;

		// csSAuditTrailTransId
		csSAuditTrailTransId =
			csSAuditTrailTransId == null ? "" : csSAuditTrailTransId;

		// csInvCd
		csInvCd = csInvCd == null ? "" : csInvCd;

		// csMnyRcvdUpdtCd
		csMnyRcvdUpdtCd =
			csMnyRcvdUpdtCd == null ? "" : csMnyRcvdUpdtCd;

		// csRequestType
		csRequestType = csRequestType == null ? "" : csRequestType;

		// csRcptCd 
		csRcptCd = csRcptCd == null ? "" : csRcptCd;

		// csSpclDocNo
		csSpclDocNo = csSpclDocNo == null ? "" : csSpclDocNo;

		// csSpclRenwlCd
		csSpclRenwlCd = csSpclRenwlCd == null ? "" : csSpclRenwlCd;

		// defect 10366 
		// csSpclRegStkrNo
		//		csSpclRegStkrNo =
		//			csSpclRegStkrNo == null ? "" : csSpclRegStkrNo;
		// csVRIMSMFGCD
		//		csVRIMSMFGCd = csVRIMSMFGCd == null ? "" : csVRIMSMFGCd;
		csResrvReasnCd = csResrvReasnCd == null ? "" : csResrvReasnCd;
		csFINDocNo = csFINDocNo == null ? "" : csFINDocNo;
		caAuctnPdAmt =
			caAuctnPdAmt == null ? new Dollar(0) : caAuctnPdAmt;
		// end defect 10366

		// csSpclRemks
		csSpclRemks = csSpclRemks == null ? "" : csSpclRemks;
		// csTransEmpId 
		csTransEmpId = csTransEmpId == null ? "" : csTransEmpId;

		// caOwnrData
		caOwnrData = caOwnrData == null ? new OwnerData() : caOwnrData;

		caOwnrData.setOwnrId(
			caOwnrData.getOwnrId() == null
				? ""
				: caOwnrData.getOwnrId());

		// defect 10112 
		caOwnrData.setAddressData(
			caOwnrData.getAddressData() == null
				? new AddressData()
				: caOwnrData.getAddressData());

		caOwnrData.setName1(
			caOwnrData.getName1() == null ? "" : caOwnrData.getName1());

		caOwnrData.setName2(
			caOwnrData.getName2() == null ? "" : caOwnrData.getName2());

		// caOwnrData.getOwnrAddr()
		AddressData laAddrData = caOwnrData.getAddressData();
		// end defect 10112 

		laAddrData.setSt1(
			laAddrData.getSt1() == null ? "" : laAddrData.getSt1());
		laAddrData.setSt2(
			laAddrData.getSt2() == null ? "" : laAddrData.getSt2());
		laAddrData.setCity(
			laAddrData.getCity() == null ? "" : laAddrData.getCity());
		laAddrData.setState(
			laAddrData.getState() == null ? "" : laAddrData.getState());
		laAddrData.setCntry(
			laAddrData.getCntry() == null ? "" : laAddrData.getCntry());
		laAddrData.setZpcd(
			laAddrData.getZpcd() == null ? "" : laAddrData.getZpcd());
		laAddrData.setZpcdp4(
			laAddrData.getZpcdp4() == null
				? ""
				: laAddrData.getZpcdp4());
	}

	/**
	 * 
	 * Determine if DlrPlt
	 * 
	 * @return boolean
	 */
	public boolean isDlrPlt()
	{
		boolean lbDlrPlt = false;
		if (csRegPltCd != null
			&& (csRegPltCd.trim().equals(SpecialPlatesConstant.PLPDLR)
				|| csRegPltCd.trim().equals(
					SpecialPlatesConstant.PLPDLRMC)))
		{
			lbDlrPlt = true;
		}
		return lbDlrPlt;
	}

	/**
	 *Return boolean to denote if ciAuctnPltIndi = 1
	 * 
	 * @return boolean
	 */
	public boolean isAuctnPlt()
	{
		return ciAuctnPltIndi == 1;
	}

	/**
	 * Return boolean to denote if ciISAIndi = 1
	 * 
	 * @return boolean
	 */
	public boolean isISA()
	{
		return ciISAIndi == 1;
	}

	/**
	 * Return boolean to denote if ciMktngAllowdIndi = 1
	 * 
	 * @return boolean
	 */
	public boolean isMktngAllowd()
	{
		return ciMktngAllowdIndi == 1;
	}

	/**
	 * Get value of EnterOnSPL002
	 * 
	 * @return boolean 
	 */
	public boolean isEnterOnSPL002()
	{
		return cbEnterOnSPL002;
	}
	/**
	 * Is Election Pending
	 * 
	 * @return boolean
	 */
	public boolean isElectionPndng()
	{
		return ciElectionPndngIndi == 1;
	}

	/**
	 * Get MF Down indicator for Special Plate
	 * 
	 * @return boolean
	 */
	public boolean isMFDownSP()
	{
		return cbMFDownSP;
	}

	/**
	 * Return ResetChrgFee
	 * 
	 * @return boolean 
	 */
	public boolean isResetChrgFee()
	{
		return cbResetChrgFee;
	}

	/**
	 * Reset select fields, Special Plates  (CICS R05)
	 * 
	 * @param aaSpclPltRegisData
	 * @return SpecialPlatesRegisData 
	 */
	public SpecialPlatesRegisData resetFields(SpecialPlatesRegisData aaSpclPltRegisData)
	{
		SpecialPlatesRegisData laSpclRegisData = aaSpclPltRegisData;

		// Set RegPltNo = "NOPLATE" if PltRmvCd > 0
		if (laSpclRegisData.getPltRmvCd() > 0)
		{
			laSpclRegisData.setRmvRegPltNo(
				laSpclRegisData.getRegPltNo());
			laSpclRegisData.setRegPltNo(RegistrationConstant.NOPLATE);
		}

		return laSpclRegisData;
	}

	/**
	 * Set value of AccptIndi
	 * 
	 * @param int
	 */
	public void setAccptIndi(int aiAccptIndi)
	{
		ciAccptIndi = aiAccptIndi;
	}

	/**
	 * Set value of AddlSetIndi
	 * 
	 * @param int
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		ciAddlSetIndi = aiAddlSetIndi;
	}

	/**
	 * Sets value of caAuctnPdAmt
	 * 
	 * @param aaAuctnPdAmt
	 */
	public void setAuctnPdAmt(Dollar aaAuctnPdAmt)
	{
		caAuctnPdAmt = aaAuctnPdAmt;
	}

	/**
	 * Sets value of ciAuctnPltIndi
	 * 
	 * @param aiAuctnPltIndi
	 */
	public void setAuctnPltIndi(int aiAuctnPltIndi)
	{
		ciAuctnPltIndi = aiAuctnPltIndi;
	}

	/**
	 * Set value of cbChrgRemakeOnReplace
	 * 
	 * @param abChrgRemakeOnReplace
	 */
	public void setChrgRemakeOnReplace(boolean abChrgRemakeOnReplace)
	{
		cbChrgRemakeOnReplace = abChrgRemakeOnReplace;
	}

	/**
	 * Set value of DelIndi
	 * 
	 * @param int
	 */
	public void setDelIndi(int aiDelIndi)
	{
		ciDelIndi = aiDelIndi;
	}

	/**
	 * Set value of ciElectionPndngIndi
	 * 
	 * @param aiElectionPndngIndi
	 */
	public void setElectionPndngIndi(int aiElectionPndngIndi)
	{
		ciElectionPndngIndi = aiElectionPndngIndi;
	}

	/**
	 * Set value for cbEnterOnSPL002
	 * 
	 * @param abEnterOnSPL002
	 */
	public void setEnterOnSPL002(boolean abEnterOnSPL002)
	{
		cbEnterOnSPL002 = abEnterOnSPL002;
	}

	/**
	 * set value of ExistingDuplIndi
	 * 
	 * @param int
	 */
	public void setExistingDuplIndi(int aiExistingDuplIndi)
	{
		ciExistingDuplIndi = aiExistingDuplIndi;
	}

	/**
	 * Sets value of csFINDocNo
	 * 
	 * @param asFINDocNo
	 */
	public void setFINDocNo(String asFINDocNo)
	{
		csFINDocNo = asFINDocNo;
	}

	/**
	 * Set value of HOOPSRegPltNo
	 * 
	 * @param String
	 */
	public void setHOOPSRegPltNo(String asHOOPSRegPltNo)
	{
		csHOOPSRegPltNo = asHOOPSRegPltNo;
	}

	/**
	 * Set value of InvCd
	 * 
	 * @param String
	 */
	public void setInvCd(String asInvCd)
	{
		csInvCd = asInvCd;
	}

	/**
	 * Set value of InvItmYr
	 * 
	 * @param aiInvItmYr
	 */
	public void setInvItmYr(int aiInvItmYr)
	{
		ciInvItmYr = aiInvItmYr;
	}

	/**
	 * Set value of ISAIndi
	 * 
	 * @param int
	 */
	public void setISAIndi(int aiISAIndi)
	{
		ciISAIndi = aiISAIndi;
	}

	/**
	 * Set value of ItrntTraceNo
	 * 
	 * @param asItrntTraceNo
	 */
	public void setItrntTraceNo(String asItrntTraceNo)
	{
		csItrntTraceNo = asItrntTraceNo;
	}

	/**
	 * Set MF Down indicator for Special Plate
	 * 
	 * @param abMFDownSP
	 */
	public void setMFDownSP(boolean abMFDownSP)
	{
		cbMFDownSP = abMFDownSP;
	}

	/**
	* Set value of MFGDate
	* 
	* @param int
	*/
	public void setMFGDate(int aiMFGDate)
	{
		ciMFGDate = aiMFGDate;
	}

	/** 
	* 
	* Set the MfgDate
	* 
	* @param asTransCd
	*/
	public void setMFGDate(String asTransCd)
	{
		setMFGDate(asTransCd, new RTSDate());
	}

	/**
	 * Set the MfgDate for the Plate during
	 * 
	 *  - Special Plate Internet Application 
	 *  - Special Plate Application
	 *  - Special Plate Renew Plate Only
	 *  - Special Plate Revise 
	 *  - Title/Registration events  
	 * 
	 * @param asTransCd
	 * @param aaEndDate 
	 */
	public void setMFGDate(String asTransCd, RTSDate aaEndDate)
	{
		int liNumDays = 1;
		PlateTypeData laPTData =
			PlateTypeCache.getPlateType(csRegPltCd);

		boolean lbAPPL =
			UtilityMethods.isSPAPPL(asTransCd)
				|| asTransCd.equals(TransCdConstant.IAPPL);

		boolean lbCP =
			csRegPltCd.trim().equals(SpecialPlatesConstant.PLPCP)
				|| csRegPltCd.trim().equals(
					SpecialPlatesConstant.PLPGOTXC)
				|| csRegPltCd.trim().equals(
					SpecialPlatesConstant.GOTEX2CP);

		//RTSDate laEndDate = new RTSDate();
		int liToYear = new RTSDate().getYear();
		int liAddDays = 1;
		// defect 9711
		// comment out this "{" and the matching "}"  
		//		{
		// CP Plate & (SPAPPL || IAPPL)  
		if (lbCP && lbAPPL)
		{
			aaEndDate =
				new RTSDate(
					liToYear,
					SpecialPlatesConstant.CP_START_MO,
					1);
			// Start counting the day after end of February
			aaEndDate =
				new RTSDate(RTSDate.AMDATE, aaEndDate.getAMDate() - 1);
		}
		//if ((laPTData.getUserPltNoIndi() == 1 || lbCP) && (lbAPPL))
		if (((laPTData.getUserPltNoIndi() == 1 || lbCP) && (lbAPPL))
			|| asTransCd.equals(TransCdConstant.VPAPPL))
		{
			liNumDays = 5;
		}
		//		}
		// end defect 9711
		int i = 0;
		do
		{
			aaEndDate = aaEndDate.add(RTSDate.DATE, liAddDays);
			if (aaEndDate.isBusinessDay())
			{
				i = i + 1;
			}
		}
		while (i < liNumDays);

		ciMFGDate = aaEndDate.getYYYYMMDDDate();

		// defect 9389 
		if (csMfgPltNo == null || csMfgPltNo.trim().length() == 0)
		{
			setMfgPltNo();
		}
		// end defect 9389 
	}

	/**
	 * 
	 * Determine MfgPltNo for Non Personalized Plates
	 * 
	 * @throws RTSException 
	 */
	public void setMfgPltNo()
	{
		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(csRegPltCd);

		if (laPltTypeData != null
			&& laPltTypeData.getUserPltNoIndi() != 1
			&& csRegPltNo != null)
		{
			// ISA:  ISA Symbol in front of PltNo for all plates 
			if (ciISAIndi == 1)
			{
				csMfgPltNo =
					SpecialPlatesConstant.ISA_SYMBOL + csRegPltNo;
			}
			//  DVP - TX symbol placement as follows
			//    DV*99   123*DV4   1234*DV    DV*1  1*DVB
			//    99*DV   1DV*234   DV*1234    1*DV
			//  

			// Note:  1DV234 is not valid but accommodated in the following:

			// defect 9356 
			// Include only DVP, DVPF i.e. exclude DVMCP, DVMCPF 

			// else if (
			//	laPltTypeData.getBaseRegPltCd().equals(
			//		SpecialPlatesConstant.DV_BASEREGPLTCD))

			else if (
				laPltTypeData.getRegPltCd().equals("DVP")
					|| laPltTypeData.getRegPltCd().equals("DVPF"))
				// end defect 9356 
			{
				// DV Plate should always have "DV"!
				int liDV = csRegPltNo.indexOf("DV");
				if (liDV >= 0)
				{
					if (csRegPltNo.startsWith("DV")
						|| (liDV == 1 && csRegPltNo.length() > 4))
					{
						csMfgPltNo =
							csRegPltNo.substring(0, liDV + 2)
								+ SpecialPlatesConstant.TX_SYMBOL
								+ csRegPltNo.substring(liDV + 2);
					}
					else
					{
						csMfgPltNo =
							csRegPltNo.substring(0, liDV)
								+ SpecialPlatesConstant.TX_SYMBOL
								+ csRegPltNo.substring(liDV);
					}
				}
				else if (csRegPltNo.length() >= 2)
				{
					boolean lbSet = false;
					for (int i = 2; i >= 1; i--)
					{
						String lsPrefix = csRegPltNo.substring(0, i);
						if (UtilityMethods.isNumeric(lsPrefix))
						{
							csMfgPltNo =
								csRegPltNo.substring(0, i)
									+ SpecialPlatesConstant.TX_SYMBOL
									+ csRegPltNo.substring(i);
							lbSet = true;
							break;
						}

					}
					if (!lbSet)
					{
						csMfgPltNo =
							csRegPltNo.substring(0, 2)
								+ SpecialPlatesConstant.TX_SYMBOL
								+ csRegPltNo.substring(2);
					}

				}
			}
			// Else MfgPltNo = RegPltNo 
			else
			{
				csMfgPltNo = csRegPltNo;
			}
		}
	}

	/**
	 * Set value of MfgPltNo
	 * 
	 * @param String
	 */
	public void setMfgPltNo(String asMfgPltNo)
	{
		csMfgPltNo = asMfgPltNo;
	}

	/**
	 * Set value of ciMfgSpclPltIndi
	 * 
	 * @param aiMfgSpclPltIndi
	 */
	public void setMfgSpclPltIndi(int aiMfgSpclPltIndi)
	{
		ciMfgSpclPltIndi = aiMfgSpclPltIndi;
	}

	/**
	 * 
	 * Set MFGStatusCd based upon RequestType
	 * 
	 */
	public void setMFGStatusCd()
	{
		csMFGStatusCd = new String();

		if (csRequestType.equals(SpecialPlatesConstant.MANUFACTURE)
			|| csRequestType.equals(SpecialPlatesConstant.FROM_RESERVE))
		{
			csMFGStatusCd =
				SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD;

		}
		else if (
			csRequestType.equals(SpecialPlatesConstant.UNACCEPTABLE))
		{
			csMFGStatusCd =
				SpecialPlatesConstant.UNACCEPTABLE_MFGSTATUSCD;
		}
		else if (
			csRequestType.equals(SpecialPlatesConstant.FROM_RESERVE))
		{
			csMFGStatusCd = SpecialPlatesConstant.RESERVE_MFGSTATUSCD;
		}
	}

	/**
	 * Set value of MFGStatusCd
	 * 
	 * @param String
	 */
	public void setMFGStatusCd(String asMFGStatusCd)
	{
		csMFGStatusCd = asMFGStatusCd;
	}

	/**
	 * Sets value of ciMktngAllowdIndi
	 * 
	 * @param aiMktngAllowdIndi
	 */
	public void setMktngAllowdIndi(int aiMktngAllowdIndi)
	{
		ciMktngAllowdIndi = aiMktngAllowdIndi;
	}

	/**
	 * Set value of MnyRcvdDate
	 * 
	 * @param int
	 */
	public void setMnyRcvdDate(int aiMnyRcvdDate)
	{
		ciMnyRcvdDate = aiMnyRcvdDate;
	}
	/**
	 * Set value of MnyRcvdUpdtCd
	 * 
	 * @param String
	 */
	public void setMnyRcvdUpdtCd(String asMnyRcvdUpdtCd)
	{
		csMnyRcvdUpdtCd = asMnyRcvdUpdtCd;
	}

	/**
	 * Set value for NoMonthsToCharge
	 * 
	 * @param aiNoMonthsToCharge
	 */
	public void setNoMonthsToCharge(int aiNoMonthsToCharge)
	{
		ciNoMonthsToCharge = aiNoMonthsToCharge;
	}

	/**
	 * Set value of OrgNo
	 * 
	 * @param String
	 */
	public void setOrgNo(String asOrgNo)
	{
		csOrgNo = asOrgNo;
	}

	/**
	 * Set Original Exp Mo
	 * 
	 * @param aiOrigPltExpMo
	 */
	public void setOrigPltExpMo(int aiOrigPltExpMo)
	{
		ciOrigPltExpMo = aiOrigPltExpMo;
	}

	/**
	 * Set Original Exp Yr
	 * 
	 * @param aiOrigPltExpYr
	 */
	public void setOrigPltExpYr(int aiOrigPltExpYr)
	{
		ciOrigPltExpYr = aiOrigPltExpYr;
	}

	/**
	 * Set the Original TransCd
	 * 
	 * @param asOrigTransCd
	 */
	public void setOrigTransCd(String asOrigTransCd)
	{
		csOrigTransCd = asOrigTransCd;
	}

	/**
	 * Sets value of caOwnrData
	 * 
	 * @param aaOwnrData
	 */
	public void setOwnrData(OwnerData aaOwnrData)
	{
		caOwnrData = aaOwnrData;
	}

	/**
	 * Set value of PltApplDate
	 * 
	 * @param int
	 */
	public void setPltApplDate(int aiRegIssueDate)
	{
		ciPltApplDate = aiRegIssueDate;
	}

	/**
	 * Set value of PltBirthDate
	 * 
	 * @param int
	 */
	public void setPltBirthDate(int aiPltBirthDate)
	{
		ciPltBirthDate = aiPltBirthDate;
	}

	/**
	 * Set value of PltExpMo
	 * 
	 * @param int
	 */
	public void setPltExpMo(int aiPltExpMo)
	{
		ciPltExpMo = aiPltExpMo;
	}

	/**
	 * Set value of PltExpYr
	 * 
	 * @param int
	 */
	public void setPltExpYr(int aiPltExpYr)
	{
		ciPltExpYr = aiPltExpYr;
	}

	/**
	 * Set value of PltOwnrDist
	 * 
	 * @param String
	 */
	public void setPltOwnrDist(String asPltOwnrDist)
	{
		csPltOwnrDist = asPltOwnrDist;
	}

	/**
	 * Set value of PltOwnrDlrGDN
	 * 
	 * @param String
	 */
	public void setPltOwnrDlrGDN(String asPltOwnrDlrGDN)
	{
		csPltOwnrDlrGDN = asPltOwnrDlrGDN;
	}

	/**
	 * Set value of PltOwnrMail
	 * 
	 * @param String
	 */
	public void setPltOwnrEMail(String asPltOwnrEMail)
	{
		csPltOwnrEMail = asPltOwnrEMail;
	}

	/**
	 * Set value of PltOwnrOfcCd
	 * 
	 * @param String
	 */
	public void setPltOwnrOfcCd(String asPltOwnrOfcCd)
	{
		csPltOwnrOfcCd = asPltOwnrOfcCd;
	}

	/**
	 * Set value of PltOwnrPhoneNo
	 * 
	 * @param String
	 */
	public void setPltOwnrPhoneNo(String asPltOwnrPhoneNo)
	{
		csPltOwnrPhoneNo = asPltOwnrPhoneNo;
	}

	/**
	 * set Plate Remove Code
	 * 
	 * @param aiPltRmvCd
	 */
	public void setPltRmvCd(int aiPltRmvCd)
	{
		ciPltRmvCd = aiPltRmvCd;
	}

	/**
	 * Set value of PltSetNo
	 * 
	 * @param int
	 */
	public void setPltSetNo(int aiPltSetNo)
	{
		ciPltSetNo = aiPltSetNo;
	}

	//	/**
	//	 * Set the number of ownership years for the SP
	//	 * 
	//	 * @param aiPltTerm int
	//	 */
	//	public void setPltTerm(int aiPltTerm)
	//	{
	//		ciPltTerm = aiPltTerm;
	//	}

	/**
	 * Set PLTVALIDITYTERM
	 * 
	 * @param aiPltValidityTerm
	 */
	public void setPltValidityTerm(int aiPltValidityTerm)
	{
		ciPltValidityTerm = aiPltValidityTerm;
	}

	//	
	//	/**
	//	 * Set value of PrivacyOptCd
	//	 * 
	//	 * @param int
	//	 */
	//	public void setPrivacyOptCd(int aiPrivacyOptCd)
	//	{
	//		ciPrivacyOptCd = aiPrivacyOptCd;
	//	}
	//	
	/**
	 * Set value of RcptCd
	 * 
	 * @param String
	 */
	public void setRcptCd(String asRcptCd)
	{
		csRcptCd = asRcptCd;
	}

	/**
	 * Set value of RegClassCd
	 * 
	 * @param int
	 */
	public void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}

	/**
	 * Set value of RegEffDate
	 * 
	 * @param int
	 */
	public void setRegEffDate(int aiRegEffDate)
	{
		ciRegEffDate = aiRegEffDate;
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
	 * @param String
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Set value of RegPltNo
	 * 
	 * @param String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Set value of csRequestType
	 * 
	 * @param asRequestType
	 */
	public void setRequestType(String asRequestType)
	{
		csRequestType = asRequestType;
	}

	/**
	 * Set value of ResComptCntyNo
	 * 
	 * @param int
	 */
	public void setResComptCntyNo(int aiResComptCntyNo)
	{
		ciResComptCntyNo = aiResComptCntyNo;
	}

	/**
	 * Set ResetChrgFee
	 * 
	 * @param abResetChrgFee
	 */
	public void setResetChrgFee(boolean abResetChrgFee)
	{
		cbResetChrgFee = abResetChrgFee;

	}

	/**
	 * Sets value of ciResrvEffDate
	 * 
	 * @param aiResrvEffDate
	 */
	public void setResrvEffDate(int aiResrvEffDate)
	{
		ciResrvEffDate = aiResrvEffDate;
	}

	/**
	 * Sets value of csResrvReasnCd
	 * 
	 * @param asResrvReasnCd
	 */
	public void setResrvReasnCd(String asResrvReasnCd)
	{
		csResrvReasnCd = asResrvReasnCd;
	}

	/**
	 * set Remove Regis Plate Number
	 * 
	 * @param asRmvRegPltNo
	 */
	public void setRmvRegPltNo(String asRmvRegPltNo)
	{
		csRmvRegPltNo = asRmvRegPltNo;
	}

	/**
	 * Set value of SAuditTrailTransId
	 * 
	 * @param String
	 */
	public void setSAuditTrailTransId(String asSAuditTrailTransId)
	{
		csSAuditTrailTransId = asSAuditTrailTransId;
	}

	/**
	 * Set value of SpclDocNo
	 * 
	 * @param String
	 */
	public void setSpclDocNo(String asSpclDocNo)
	{
		csSpclDocNo = asSpclDocNo;
	}
	/**
	 * Set value of SpclFee
	 * 
	 * @param Dollar
	 */
	public void setSpclFee(Dollar aaSpclFee)
	{
		caSpclFee = aaSpclFee;
	}

	/**
	 * Set value for whether to charge Special Plate Fee
	 * 
	 * @param aiSpclPltChrgFeeIndi
	 */
	public void setSpclPltChrgFeeIndi(int aiSpclPltChrgFeeIndi)
	{
		ciSpclPltChrgFeeIndi = aiSpclPltChrgFeeIndi;
	}

	/**
	 *  Set value of SpclRegId
	 * 
	 * @param long
	 */
	public void setSpclRegId(long alSpclRegId)
	{
		clSpclRegId = alSpclRegId;
	}

	//	/**
	//	 * Set value of SpclRegStkrNo
	//	 * 
	//	 * @param String
	//	 */
	//	public void setSpclRegStkrNo(String asSpclRegStkrNo)
	//	{
	//		csSpclRegStkrNo = asSpclRegStkrNo;
	//	}

	/**
	 * Set value of SpclRemks
	 * 
	 * @param String
	 */
	public void setSpclRemks(String asSpclRemks)
	{
		csSpclRemks = asSpclRemks;
	}

	/**
	 * Set value of SpclRenwlCd
	 *
	 * @param String
	 */
	public void setSpclRenwlCd(String asSpclRenwlCd)
	{
		csSpclRenwlCd = asSpclRenwlCd;
	}

	/**
	 * Set value of TransCd
	 * 
	 * @param String
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * Set value of TransEmpId
	 * 
	 * @param String
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
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
	 * Sets value of ciVendorTransDate
	 * 
	 * @param aiVendorTransDate
	 */
	public void setVendorTransDate(int aiVendorTransDate)
	{
		ciVendorTransDate = aiVendorTransDate;
	}

	/**
	 * Get value of VIAllocData
	 * 
	 * @param aaVIAllocData
	 */
	public void setVIAllocData(InventoryAllocationData aaVIAllocData)
	{
		caVIAllocData = aaVIAllocData;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public boolean isPrntPrmt()
	{
		return cbPrntPrmt;
	}
	
	/**
	 * Method description
	 * 
	 * @param 
	 */
	public void setPrntPrmt(boolean abPrntPrmt)
	{
		cbPrntPrmt = abPrntPrmt;
	}
}