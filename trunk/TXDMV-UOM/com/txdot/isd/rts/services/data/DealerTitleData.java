package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.OwnershipEvidenceCodesCache;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *
 * DealerTitleData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		06/25/2002	Trim the VehOdomtrReading, 
 * 							method setTextToObject()
 * 							defect 4367 	
 * J Rue		07/12/2002	case type Long for DocNo, method 
 * 							setTextToObjectDealerTitleData().
 * 							defect 4390 
 * J Rue		07/25/20022	Store IMCNo as a long in 
 *							dlrData.getMiscData().
 * 							methods consolidateVehicleInfoWithDealerInfo
 * 							defect 4437 
 * J Rue		08/23/2002	Set RegExpMo and RegExpYr to 0 in vehInqData 
 * 							if NoRecFound. 
 * 							method 
 * 								consolidateVehicleInfoWithDealerInfo().
 * 							defect 4647 
 * J Rue/ 		08/23/2002	convert Odometer brand reading
 *							to uppercase when "exempt" is enter in dta
 *							diskette
 *							defect 4664
 * R Arrond					by making changes to setTextToObject().
 * J Rue 		09/27/2002	modify the edits from Cities, 
 *							Streets, Names and Countries to allow single
 *							or muliple blanks to be accepted. methods
 *							checkForCity(),	checkForCounty(), 
 *							checkForNameOrSt(),	setTextToObject()
 *							defect 4775
 * J Rue		11/07/2002	Set field for Doc number if valid.
 * S Govindappa				modify setTextToObject().
 *							defect 5078
 * J Rue       	03/11/2003  Made changes to 
 *							setTextToObject(..) to accept null values 
 *							for Dealer input fields from disk and 
 *							Removal of setting the flags for invalid 
 *							data on selected fields.
 *							defect 5142
 * J Rue		02/21/2003	Add exception for rebate for all exceptions
 *							modify setTextToObject()
 *							defect 5142
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							add class variables and associated getters/ 
 * 							setters.
 * 							add isVoided(),setField()
 * 							Ver 5.2.0 	
 * J Rue		07/19/2004	5.2.1 Merge.
 *							Set input parameters to Hungarian Notation
 *							Integrate if statement to validate all input 
 *							values.
 *							modify setFields()
 *							defect 7294 Ver 5.2.1	
 * J Rue		07/20/2004	Incorporate prior 5.1.6 DTA defects to 5.2.1
 *							defect 7350 Ver 5.2.1
 * J Rue		07/22/2004	Invalid Record indi not set in v5.1.6
 *							defect 7350 Ver 5.2.1
 * J Rue		08/03/2004	add try/catch box to catch 
 *							NumberFormatException for RebateAmt field.
 *							Write 0.00 dollars if exception is thrown
 *							modify setField()
 *							defect 7371 Ver 5.2.1
 * J Rue		08/25/2004	DealerTitleData.NewRegExpMo() and 
 *							DealerTitleData.NewRegExpYr() were 
 *							converted to integer.
 *							modify setField(), getNewRegExpMo(), 
 *							getNewRegExpYr(), setNewRegExpMo(), 
 *							setNewRegExpYr()
 *							defect 7496 Ver 5.2.1
 * J Rue		08/31/2004	Case Number (91) TaxPdOthrState exception
 *							should be NumberFormatException not 
 *							NullPointerException
 *							modify setField()
 *							defect 7350 Ver 5.2.1
 * J Rue		11/24/2004	Add RSPS Original Print Date to this data
 *							object
 *							add csOrigPrntDate
 *							add getOrigPrntDate(), setOrigPrntDate()
 *							modify setField(), getField()
 *							defect 7745 Ver 5.2.2
 * J Rue		12/10/2004	If DocNo from Dealer equal to 0 
 *							or field length != 17 or not a number,
 *							set to EMPTY
 *							Update JavaDoc and Prolog 
 *							add EMPTY
 *							delete DEFAULT
 *							modify setField()
 *							defect 6844 Ver 5.2.2
 * J Rue		12/15/2004	Initialize scannerProcessorNum = ""
 *							defect 7736 Ver 5.2.2
 * K Harrell	12/17/2004	Formatting/JavaDoc/Variable Name Cleanup
 *							defect 7736 Ver 5.2.2
 * K Harrell	04/22/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell	05/03/2005	RegistrationMiscData is deprecated
 * 							modify constructor
 * 							defect 8188 Ver 5.2.3 
 * J Rue		05/24/2005	set case index to field name of DLR field
 * 							name.
 * 							Clean up code, Set case to standard in
 * 							getField()
 * 							deprecate setData(String)
 * 							modify setField(), getField()
 * 							defect 7770, 7898 Ver 5.2.3
 * J Rue		06/15/2005	Rename RSPS variables and getters/setters
 * 							to better define their meaning
 * 							modify setField(), getField()
 * 							defect 8217 Ver 5.2.3
 * K Harrell	06/20/2005	Java 1.4 Work
 * 							defect 7899 Ver 5.2.3 
 * J Rue		06/28/2005	Use the new MediaValidations.
 * 							validateYear(String, String, String) 
 * 							replacing MediaValidations.
 * 							validateYear(String)
 * 							modify setField()
 * 							defect 8260 Ver 5.2.3
 * J Rue		12/01/2005	Set subString() parameters for 
 * 							newRegExpMo/Yr. Assign to ciMonth and ciYear
 * 							modify setField()
 * 							defect 8260 Ver 5.2.3
 * J Rue		12/06/2005	Replace "" with 
 * 								CommonConstant.STR_SPACE_EMPTY
 * 							Replace "0.00" with
 * 								CommonConstant.STR_ZERO_DOLLAR
 * 							Replace "." with
 * 								CommonConstant.STR_PERIOD
 * 							defect 7898 Ver 5.2.3 
 * J Rue		01/03/2006	Set DlrId(int) to -1 as default
 * 							modify setField()
 * 							defect 7898 Ver 5.2.3
 * Min Wang		10/10/2006	New Requirement for handling plate age 
 * 							modify consolidateVehicleInfoWithDealerInfo()
 *							defect 8901 Ver Exempts
 * J Rue		10/17/2007	Accept any value the dealer sends for 
 * 							 DlrGDN
 * 							modify setField()
 * 							defect 9367 Ver Special Plates 2
 * K Harrell	11/05/2007	add cbCustSuppliedPlt
 * 							add setCustSuppliedPlt(), isCustSuppliedPlt()
 * 							defect 9425 Ver SpecialPlates 2
 * K Harrell	11/07/2007	add RegPltAge, get/set methods 
 * 							defect 9425 Ver Special Plates 2
 * J Rue		05/14/2008	Add CustSuppliedIndi and CustSuppliedPltAge
 * 							add getCustSuppliedIndi,
 * 								setCustSuppliedPltAge()
 * 							modify setFields()
 * 							defect 9656 Ver Defect_POS_A   
 * J Rue		05/14/2008	Add Record Type in DealerTitleData
 * 							add getRecType, setRecType()
 * 							defect 9656 Ver Defect_POS_A
 * J Rue		05/21/2008	add CustSuppliedPltIndi, CustSuppliedPltAge 
 * 							to setField() and getField()
 * 							modify setField(), getField()   
 * 							defect 9656 Ver Defect_POS_A
 * J Rue		05/22/2008	Add new record types 3 and 4
 * 							modify getField(), setField()
 * 							defect 9656 Ver Defect_POS_A
 * J Rue		05/23/2008	Accept formats with trailing "\"
 * 							modify setField(), getField()
 * 							defect 9656 Ver Defect_POS_A 
 * K Harrell	05/25/2008	Reset CustSuppliedPltAge if invalid
 * 							delete cbCustSuppliedPlt, ciRegPltAge, 
 * 							 setCustSuppliedPlt(), getRegPltAge()
 * 							modify isCustSuppliedPlt()
 * 							defect 9656 Ver Defect_POS_A
 * J Rue		06/03/2008	Replace magic number 3 with 
 * 							RSPS_ORIG_FRMT_125. 
 * 							add RSPS_ORIG_FRMT_125
 * 							modify getField()
 * 							defect 9656 Ver Defect_POS_A
 * J Rue		06/10/2008	Add break to case statement 127 and 128
 * 							update comments
 * 							modify setField()
 * 							defect 9656 Ver Defect_POS_A   
 * J Rue		06/16/2008	Change RSPS_CUST_SUPPLY_125_DEL to
 * 							RSPS_CUST_SUPPLY_127_DEL for FIELD_INDEX_127
 * 							and FIELD_INDEX_128
 * 							Change CommonConstant.STR_EMPTY to null
 * 							modify getField()
 * 							defect 9656 Ver Defect_POS_A
 * J Rue		09/22/2008	Modify conditional from liIndi >= 0 && 
 * 							liIndi <= 80000 to liIndi >= 1 && 
 * 							liIndi <= 99
 * 							modify setField()
 * 							defect 8752 Ver Defect_POS_B
 * J Rue		09/30/2008	Change variable liIndi to liVehLngth for
 * 							vehicle length test
 * 							modify setField()
 * 							defect 8752 Ver Defect_POS_B
 * J Rue		02/26/2009	RegPltNo is not captured
 * 							Add ETtlCd
 * 							 getETtlCd(), setETtlCd()
 * 							modify getField(), setField()
 * 							defect 9973 Ver Defect_POS_E
 * J Rue		03/03/2009	Add 
 * 							if (getRecType() == RSPS_PERMLIENHLDR_FRMT_CD)
 * 							for case 129 - 132
 * 							modify getField()
 * 							defect 9973 Ver Defect_POS_E
 * K Harrell	03/03/2009	Use AddressData with LienholdersData objects
 * 							add isETtlReqst()
 * 							modify getField(), setField()
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	03/27/2009	add cvLienError, get/set methods()
 * 							defect 9979 Ver Defect_POS_E
 * J Rue		04/29/2009	Capture RegPltNo. Is used for searching MF 
 * 							for record by RegPltNo
 * 							modify setField()
 * 							defect 9973 Ver Defect_POS_E 
 * B Hargrove	05/22/2009  Add Flashdrive option to DTA. Change 
 * 							verbiage from 'diskette'.
 * 							modify setField() (references to 
 * 							diskValidations() became mediaValidations()). 
 * 							defect 10075 Ver Defect_POS_F  
 * K Harrell	07/03/2009	delete csDlrbatchNo, csDealersData, 
 * 							 get/set methods
 * 							modify caDlrData (DealerData vs. DealersData) 
 * 							modify setField(), DealerTitleData(),
 * 							 consolidateVehicleInfoWithDealerInfo()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/20/2009	Implement HB3517 - Handling of OwnerId 
 * 							Do not collect from diskette
 * 							modify setField() 
 * 							defect 10130 Ver Defect_POS_F 
 * J Rue		08/18/2009	Adjust statement that DocNo from dealer is
 * 							carried forward if and only if:
 * 							17 characters,Numeric and not equal to zero
 * 							Not a BigDecimal(no decimal value)
 * 							modify setField()
 * 							defect 10144 Ver Defect_POS_F
 * J Rue		08/20/2009	Update if stmt to say
 * 							!asValue.equals(ZERODOCNO)  
 * 							modify setField()
 * 							defect 10144 Ver Defect_POS_F
 * J Rue		09/21/2009	Delete NewStkrNo. Replace by LemonLawIndi 
 * 							(Manufacturing Buyback). get/set are for 
 * 							retaining the original value to be written
 * 							back to diskette for RSPS formats.
 * 							LemonLawIndi is stored in the TitleData 
 * 							object used for processing.
 * 							add LEMONLAWINDI, getLemonLawIndi(), 
 * 								setLemonLawIndi(), assignLemonLawIndi()
 * 							modify getField(), setField()
 * 							delete getNewStkrNo(), setNewStkrNo, 
 * 								csNewStkrNo, NEWSTKRNO
 * 							defect 10232 Ver Defect_POS_F
 * K Harrell	12/17/2009	DTA Cleanup 
 * 							add DEALERID, caDealerData, ciDealerId,
 * 							 csDealerSeqNo, csTransDate, get/set methods 
 * 							  (for naming consistency)
 * 							delete DLRID, ciDlrId, csDlrSeqNo, csTransDt,
 * 							  csLemonLawIndi, get/set methods
 * 							delete cbProcessed, caDlrData,cbNoVinChecked, 
 * 							 get/set methods
 * 							refactor getMiscData() to getVehMiscData()
 * 							refactor setMiscData() to setVehMiscData()  
 * 							defect 10290 Ver Defect_POS_H 
 * K Harrell	12/28/2009	add cvDTAInvalidRecordData, get/set methods
 * 							add setInvalidRecord()  
 * 							delete setInvalidRecord(boolean)
 * 							modify setField(int,String,int)
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	01/02/2009	add getDlrBatchNo()
 * 							modify getStrDealerid()
 * 							defect 10290 Ver Defect_POS_H
 * T Pederson	06/23/2010	Changed variable name and values for Email
 * 							disk format.
 * 							add RSPS_EMAIL_FRMT_CD, EMAIL_FRMT_CD, 
 * 							PERMLIENHLDR1, PERMLIENHLDR2, PERMLIENHLDR3,
 * 							ETTLRQST, FIELD_INDEX_133, FIELD_INDEX_134
 * 							modify getField(int), setField(int,String,int)
 * 							defect 10509 Ver POS_650
 * T Pederson	08/30/2010	Corrected variable name in getField for
 * 							case PERMLIENHLDR2.
 * 							modify getField(int)
 * 							defect 10509 Ver POS_650
 * T Pederson	01/21/2011	Changed variable name and values for vehicle
 * 							color disk format.
 * 							add RSPS_VEHCOLOR_FRMT_CD, VEHCOLOR_FRMT_CD, 
 * 							EMAILRENWLREQCD, RECPNTEMAIL
 * 							delete RSPS_EMAIL_FRMT_CD, EMAIL_FRMT_CD
 * 							modify getField(int), setField(int,String,int)
 * 							consolidateVehicleInfoWithDealerInfo(),
 * 							doCopyDlrData()
 * 							defect 10709 Ver POS_670
 * K Harrell	02/15/2011	modify for MilitaryIndi
 * 							add FIELD_INDEX_137
 * 							add ciMilitaryIndi, get,is, set methods
 * 							modify getField(int), setField(int,String,int) 
 * 							defect 10752 Ver 6.7.0
 * K Harrell	11/22/2011	Validate Ownership Evidence against Cache
 * 							modify setField(int,String,int)
 * 							defect 11051 Ver 6.9.0  
 * K Harrell	01/16/2012	Set error when SurvivorshipRightsIndi = 1 and 
 * 							after SystemProperty.DTARejectSurvivorDate
 * 							modify setField(int,String,int) 
 * 							defect 10827 Ver 6.10.0
  * K Harrell	05/29/2012	Do not copy Replica data to Vehicle Record on 
 * 							Record Found unless valid. 
 * 							modify  doCopyDlrData() 
 * 							defect 11368 Ver 6.10.1 
 * K Harrell	06/06/2012	Changed requirement:  Replica data should be
 * 							ignored on record found.
 * 							modify doCopyDlrData()
 * 							defect 11368 Ver 6.10.1 
 * ---------------------------------------------------------------------
 */

/**     
 * This class defines the data elements associated with the DealerTitle 
 *	Diskette. 
 *
 * @version	6.10.1			06/06/2012
 * @author	M Rajang
 * <br>Creation Date:		02/04/2002 08:23:02
 */
public class DealerTitleData
	implements java.io.Serializable, Comparable, Parseable
{
	// ***	boolean
	private boolean cbInvalidRecord = false;
	private boolean cbKeyBoardEntry = true;
	private boolean cbPOSProcsIndi;
	private boolean cbRecordRejected = false;
	private boolean cbSkipCurrObj = false;
	private boolean cbToBePrinted;
	private boolean cbUpdtPOSRSPSPrntIndi;

	// int 
	private int ciAddlToknFeeIndi = 0;
	private int ciChrgTrnsfrFee = 0;
	private int ciCustSuppliedPltAge = 0;
	private int ciCustSuppliedPltIndi = 0;
	// defect 10290 
	private int ciDealerId = 0;
	// end defect 10290 
	private int ciETtlRqst;
	private int ciMonth = 0;
	private int ciNewRegExpMo = 0;
	private int ciNewRegExpYr = 0;
	private int ciOffHghwyIndi = 0;
	private int ciPOSPrntInvQty;
	private int ciRecType = 0;
	private int ciRSPSDiskSeqNo;
	private int ciRSPSPrntInvQty;
	private int ciTransNo = 0;
	// Void is not used
	private int ciVoided;
	private int ciYear = 0;

	// defect 10752 
	private int ciMilitaryIndi;
	// end defect 10752 

	// ***	String
	private String csDealerId;
	private String csDealerSeqNo;
	private String csForm31No;
	// defect 10290 
	//private String csLemonLawIndi;
	// end defect 10290 
	private String csNewPltNo;
	private String csRSPSId = CommonConstant.STR_SPACE_EMPTY;
	private String csRSPSOrigPrntDate = CommonConstant.STR_SPACE_EMPTY;
	private String csTransCd;
	private String csTransDate;
	private String csTransId = null;

	//	***	Vector 
	private Vector cvInventoryData = null;
	private Vector cvLienError = null;
	private Vector cvRegFee = null;

	// defect 10290 
	private Vector cvDTAInvalidRecordData = new Vector();
	// end defect 10290

	// ***	Object
	private Dollar caCompleteTransFee;
	private Dollar caFee;
	private Dollar caTtlRebateAmt;

	private MFVehicleData caMFVehicleData;
	private MFVehicleData caMFVehicleDataFromMF;
	private VehMiscData caVehMiscData;

	// Constants 
	public final static int BASE_FRMT_CD = 1;
	// defect 10709
	//public final static int EMAIL_FRMT_CD = 2;
	public final static int VEHCOLOR_FRMT_CD = 2;
	// end defect 10709
	public final static int RSPS_FRMT_CD = 3;
	// defect 10709
	//public final static int RSPS_EMAIL_FRMT_CD = 4;
	public final static int RSPS_VEHCOLOR_FRMT_CD = 4;
	// end defect 10709

	private final static String A = "A";
	private final static String F = "F";
	private final static String N = "N";
	private final static String S = "S";
	private final static String X = "X";

	private final static String US = "US";
	private final static String WS = "WS";

	private final static int DEFAULT_CUST_PLT_AGE = 3;

	private final static String EMPTY = CommonConstant.STR_SPACE_EMPTY;
	private final static String EXEMPT = "EXEMPT";

	private final static int ZERO = 0;
	private final static String ZERODOCNO = "00000000000000000";

	private final static int ADDLLIENRECRDINDI = 1;
	private final static int ADDLTOKNFEEINDI = 2;
	private final static int ADDLTRADEINDI = 3;
	private final static int ASEQNO = 4;
	private final static int CHRGTRNSFRFEEINDI = 5;
	private final static int CUSTSUPPLYINDI = 119;
	private final static int CUSTSUPPLYPLTAGE = 120;
	private final static int DEALERID = 8;
	private final static int DIESELINDI = 6;
	private final static int DLRGDN = 7;
	private final static int DLRTRANSDATE = 9;
	private final static int DOCNO = 10;
	// defect 10709
	private final static int EMAILRENWLREQCD = 125;
	// end defect 10709
	// defect 10509
	private final static int ETTLRQST = 124;
	// end defect 10509
	private final static int FEE = 11;
	//	// RSPSPRNTINVQTY / VEHMAJORCOLORCD
	//	private final static int FIELD_INDEX_127 = 127;
	//	// POSPRNTINVQTY / VEHMINORCOLORCD
	//	private final static int FIELD_INDEX_128 = 128;
	//	// POSPROCSDINDI / RSPSPRNTINVQTY
	//	private final static int FIELD_INDEX_129 = 129;
	//	// VOIDEDINDI / POSPRNTINVQTY
	//	private final static int FIELD_INDEX_130 = 130;
	//	// UPDTPOSRSPSPRNTINDI / POSPRCSDINDI
	//	private final static int FIELD_INDEX_131 = 131;
	//	// RSPSDISKSEQNO / RSPSORIGPRNTDDATE
	//	private final static int FIELD_INDEX_132 = 132;
	//	// RSPSID / UPDTPOSRSPSPRNTINDI
	//	private final static int FIELD_INDEX_133 = 133;
	//	// RSPSORIGPRNTDDATE / RSPSDISKSEQNO
	//	private final static int FIELD_INDEX_134 = 134;
	//	// RSPSID
	//	private final static int FIELD_INDEX_135 = 135;
	//	// RSPSORIGPRNTDDATE
	//	private final static int FIELD_INDEX_136 = 136;
	//	// end defect 10709

	// defect 10752 
	// RSPSPRNTINVQTY / VEHMAJORCOLORCD
	private final static int FIELD_INDEX_127 = 127;

	// POSPRNTINVQTY / VEHMINORCOLORCD
	private final static int FIELD_INDEX_128 = 128;

	// POSPROCSDINDI / MILITARYINDI
	private final static int FIELD_INDEX_129 = 129;

	// VOIDEDINDI / RSPSPRNTINVQTY
	private final static int FIELD_INDEX_130 = 130;

	// UPDTPOSRSPSPRNTINDI / POSPRNTINVQTY
	private final static int FIELD_INDEX_131 = 131;

	// RSPSDISKSEQNO / POSPRCSDINDI
	private final static int FIELD_INDEX_132 = 132;

	// RSPSID / VOIDEDINDI
	private final static int FIELD_INDEX_133 = 133;

	// RSPSORIGPRNTDATE / UPDTPOSRSPSPRNTINDI
	private final static int FIELD_INDEX_134 = 134;

	// RSPSDISKSEQNO
	private final static int FIELD_INDEX_135 = 135;

	// RSPSID
	private final static int FIELD_INDEX_136 = 136;

	// RSPSORIGPRNTDATE
	private final static int FIELD_INDEX_137 = 137;
	// end defect 10752 

	private final static int FLOODMGEINDI = 12;
	private final static int FORM31NO = 13;
	private final static int FXDWTINDI = 14;
	private final static int HVYVEHUSETAXINDI = 15;
	private final static int IMCNO = 16;
	private final static int LEMONLAWINDI = 50;
	private final static int LIEN1DATE = 17;
	private final static int LIEN2DATE = 18;
	private final static int LIEN3DATE = 19;
	private final static int LIENHLDR1CITY = 20;
	private final static int LIENHLDR1CNTRY = 21;
	private final static int LIENHLDR1NAME1 = 22;
	private final static int LIENHLDR1NAME2 = 23;
	private final static int LIENHLDR1ST1 = 24;
	private final static int LIENHLDR1ST2 = 25;
	private final static int LIENHLDR1STATE = 26;
	private final static int LIENHLDR1ZPCD = 27;
	private final static int LIENHLDR1ZPCDP4 = 28;
	private final static int LIENHLDR2CITY = 29;
	private final static int LIENHLDR2CNTRY = 30;
	private final static int LIENHLDR2NAME1 = 31;
	private final static int LIENHLDR2NAME2 = 32;
	private final static int LIENHLDR2ST1 = 33;
	private final static int LIENHLDR2ST2 = 34;
	private final static int LIENHLDR2STATE = 35;
	private final static int LIENHLDR2ZPCD = 36;
	private final static int LIENHLDR2ZPCDP4 = 37;
	private final static int LIENHLDR3CITY = 38;
	private final static int LIENHLDR3CNTRY = 39;
	private final static int LIENHLDR3NAME1 = 40;
	private final static int LIENHLDR3NAME2 = 41;
	private final static int LIENHLDR3ST1 = 42;
	private final static int LIENHLDR3ST2 = 43;
	private final static int LIENHLDR3STATE = 44;
	private final static int LIENHLDR3ZPCD = 45;
	private final static int LIENHLDR3ZPCDP4 = 46;
	private final static int MAX_CUST_PLT_AGE = 7;
	private final static int NEWPLTNO = 47;
	private final static int NEWREGEXPMO = 48;
	private final static int NEWREGEXPYR = 49;
	private final static int OFFHWYUSEINDI = 51;
	private final static int OWNRCITY = 52;
	private final static int OWNRCNTRY = 53;
	private final static int OWNRID = 54;
	private final static int OWNRSHPEVIDCD = 55;
	private final static int OWNRST1 = 56;
	private final static int OWNRST2 = 57;
	private final static int OWNRSTATE = 58;
	private final static int OWNRTTLNAME1 = 59;
	private final static int OWNRTTLNAME2 = 60;
	private final static int OWNRZPCD = 61;
	private final static int OWNRZPCDP4 = 62;
	// defect 10509
	private final static int PERMLIENHLDR1 = 121;
	private final static int PERMLIENHLDR2 = 122;
	private final static int PERMLIENHLDR3 = 123;
	// end defect 10509
	private final static int PREVOWNRCITY = 63;
	private final static int PREVOWNRNAME = 64;
	private final static int PREVOWNRSTATE = 65;
	private final static int PRMTREQRDINDI = 66;
	private final static int RECONDINDI = 67;
	private final static int RECONTINDI = 68;
	// defect 10709
	private final static int RECPNTEMAIL = 126;
	// end defect 10709
	private final static int RECPNTNAME = 69;
	private final static int REGCLASSCD = 70;
	private final static int REGEXPMO = 71;
	private final static int REGEXPYR = 72;
	private final static int REGPLTCD = 73;
	private final static int REGPLTNO = 74;
	private final static int REGSTKRCD = 75;
	private final static int REGSTKRNO = 76;
	private final static int RENWLMAILNGCITY = 77;
	private final static int RENWLMAILNGST1 = 78;
	private final static int RENWLMAILNGST2 = 79;
	private final static int RENWLMAILNGSTATE = 80;
	private final static int RENWLMAILNGZPCD = 81;
	private final static int RENWLMAILNGZPCDP4 = 82;
	private final static int REPLICAVEHMK = 83;
	private final static int REPLICAVEHMODLYR = 84;
	private final static int RESCOMPTCNTYNO = 85;
	private final static int SALESTAXCAT = 86;
	private final static int SALESTAXDATE = 87;
	private final static int SALESTAXEXMPTCD = 88;
	private final static int SALESTAXPNTLYPRCNT = 89;
	private final static int SURVSHPRIGHTSINDI = 90;
	private final static int TAXPDOTHRSTATE = 91;
	private final static int TRADEINVEHMK = 92;
	private final static int TRADEINVEHYR = 93;
	private final static int TRADEINVIN = 94;
	private final static int TRLRTYPE = 95;
	private final static int TTLVEHLOCCITY = 96;
	private final static int TTLVEHLOCST1 = 97;
	private final static int TTLVEHLOCST2 = 98;
	private final static int TTLVEHLOCSTATE = 99;
	private final static int TTLVEHLOCZPCD = 100;
	private final static int TTLVEHLOCZPCDP4 = 101;
	private final static int VEHBDYTYPE = 102;
	private final static int VEHBDYVIN = 103;
	private final static int VEHCARYNGCAP = 104;
	private final static int VEHCLASSCD = 105;
	private final static int VEHEMPTYWT = 106;
	private final static int VEHLNGTH = 107;
	private final static int VEHMK = 108;
	private final static int VEHMODL = 109;
	private final static int VEHMODLYR = 110;
	private final static int VEHODMTRBRND = 111;
	private final static int VEHODMTRREADNG = 112;
	private final static int VEHSALESPRICE = 113;
	private final static int VEHTON = 114;
	private final static int VEHTRADEINALLOWANCE = 115;
	private final static int VEHUNITNO = 116;
	private final static int VIN = 117;
	private final static int ZTOTALREBATEAMT = 118;

	private final static long serialVersionUID = 504840706706587360L;

	/**
	 * Consolidate MF and Dealer Data
	 * 
	 * @param aaDlrTtlData DealerTitleData
	 * @param aaMFVehData MFVehicleData
	 */
	public static void consolidateVehicleInfoWithDealerInfo(
		DealerTitleData aaDlrTtlData,
		VehicleInquiryData aaVehInqData)
	{
		if (aaDlrTtlData != null && aaVehInqData != null)
		{
			if (!aaDlrTtlData.isKeyBoardEntry())
			{
				MFVehicleData laMFVehData =
					aaVehInqData.getMfVehicleData();
				// VehInq data
				MFVehicleData laDlrMFVehData =
					aaDlrTtlData.getMFVehicleData();

				//save a copy of the MFVehicleData from MF
				aaDlrTtlData.setMFVehicleDataFromMF(
					(MFVehicleData) UtilityMethods.copy(laMFVehData));
				if (laDlrMFVehData != null && laMFVehData != null)
				{
					VehMiscData laMFVehMiscData =
						aaVehInqData.getVehMiscData();
					VehMiscData laDlrVehMiscData =
						aaDlrTtlData.getVehMiscData();
					if (laDlrVehMiscData != null
						&& laMFVehMiscData != null)
					{
						laMFVehMiscData.setSalesTaxCat(
							laDlrVehMiscData.getSalesTaxCat());
						laMFVehMiscData.setSalesTaxDate(
							laDlrVehMiscData.getSalesTaxDate());
						laMFVehMiscData.setSalesTaxExmptCd(
							laDlrVehMiscData.getSalesTaxExmptCd());
						laMFVehMiscData.setSalesTaxPnltyPer(
							laDlrVehMiscData.getSalesTaxPnltyPer());
						laMFVehMiscData.setIMCNo(
							laDlrVehMiscData.getIMCNo());
					}
					if (aaVehInqData.getNoMFRecs() > 0)
					{
						//MF record found. Use MF record and copy disk 
						// values to overwrite MF record
						MFVehicleData laCopyOfMFVehData =
							(MFVehicleData) UtilityMethods.copy(
								laMFVehData);
						doCopyDlrData(
							laCopyOfMFVehData,
							laDlrMFVehData);

						// TitleData
						laCopyOfMFVehData.getTitleData().setDocNo(
							laMFVehData.getTitleData().getDocNo());
						laCopyOfMFVehData
							.getVehicleData()
							.setVehClassCd(
							laMFVehData
								.getVehicleData()
								.getVehClassCd());

						// RegistrationData
						laCopyOfMFVehData
							.getRegData()
							.setResComptCntyNo(
							laMFVehData
								.getRegData()
								.getResComptCntyNo());
						laCopyOfMFVehData.getRegData().setRegClassCd(
							laMFVehData.getRegData().getRegClassCd());
						laCopyOfMFVehData.getRegData().setRegExpMo(
							laMFVehData.getRegData().getRegExpMo());
						laCopyOfMFVehData.getRegData().setRegExpYr(
							laMFVehData.getRegData().getRegExpYr());
						// defect 8901
						//laCopyOfMFVehData.getRegData().setRegPltAge(
						//	laMFVehData.getRegData().getRegPltAge());
						laCopyOfMFVehData.getRegData().setRegPltAge(
							laMFVehData.getRegData().getRegPltAge(
								false));
						// end defect 8901
						laCopyOfMFVehData.getRegData().setRegPltCd(
							laMFVehData.getRegData().getRegPltCd());
						laCopyOfMFVehData.getRegData().setRegPltNo(
							laMFVehData.getRegData().getRegPltNo());
						laCopyOfMFVehData.getRegData().setRegStkrCd(
							laMFVehData.getRegData().getRegStkrCd());
						laCopyOfMFVehData.getRegData().setRegStkrNo(
							laMFVehData.getRegData().getRegStkrNo());
						if (laMFVehData.getVehicleData().getVehMk()
							!= null)
						{
							laCopyOfMFVehData
								.getVehicleData()
								.setVehMk(
								laMFVehData
									.getVehicleData()
									.getVehMk());
						}
						// defect 10709
						if (!(UtilityMethods
							.isEmpty(
								laMFVehData
									.getVehicleData()
									.getVehMjrColorCd()))
							&& (UtilityMethods
								.isEmpty(
									(laCopyOfMFVehData
										.getVehicleData()
										.getVehMjrColorCd()))))
						{
							laCopyOfMFVehData
								.getVehicleData()
								.setVehMjrColorCd(
								laMFVehData
									.getVehicleData()
									.getVehMjrColorCd());
							laCopyOfMFVehData
								.getVehicleData()
								.setVehMnrColorCd(
								laMFVehData
									.getVehicleData()
									.getVehMnrColorCd());
						}
						// end defect 10709
						laCopyOfMFVehData
							.getVehicleData()
							.setVehModlYr(
							laMFVehData
								.getVehicleData()
								.getVehModlYr());

						// defect 10112
						laCopyOfMFVehData.getOwnerData().setName1(
							laMFVehData.getOwnerData().getName1());
						laCopyOfMFVehData.getOwnerData().setName2(
							laMFVehData.getOwnerData().getName2());
						// end defect 10112 

						laCopyOfMFVehData.getOwnerData().setOwnrId(
							laMFVehData.getOwnerData().getOwnrId());
						laCopyOfMFVehData
							.getVehicleData()
							.setVehOdmtrBrnd(
							laMFVehData
								.getVehicleData()
								.getVehOdmtrBrnd());
						aaVehInqData.setMfVehicleData(
							laCopyOfMFVehData);
					}
					else
					{
						//All data from disks
						MFVehicleData laCopyOfDlrMFVehData =
							(MFVehicleData) UtilityMethods.copy(
								laDlrMFVehData);

						//calculating the gross weight
						int liGrossWt =
							CommonValidations.calcGrossWeight(
								String.valueOf(
									laCopyOfDlrMFVehData
										.getVehicleData()
										.getVehEmptyWt()),
								String.valueOf(
									laCopyOfDlrMFVehData
										.getRegData()
										.getVehCaryngCap()));
						if (laCopyOfDlrMFVehData.getRegData() != null)
						{
							laCopyOfDlrMFVehData
								.getRegData()
								.setVehGrossWt(
								liGrossWt);
						}
						//Overwrite info if Vina returns with 
						//information
						if (aaVehInqData.isVINAExists())
						{
							int liModlYr =
								laMFVehData
									.getVehicleData()
									.getVehModlYr();
							if (liModlYr > 0)
							{
								((MFVehicleData) laCopyOfDlrMFVehData)
									.getVehicleData()
									.setVehModlYr(
									liModlYr);
							}
							String lsVehMk =
								laMFVehData.getVehicleData().getVehMk();
							if (lsVehMk != null
								&& lsVehMk.length() > 0)
							{
								((MFVehicleData) laCopyOfDlrMFVehData)
									.getVehicleData()
									.setVehMk(
									lsVehMk);
							}
							String lsVehBdyType =
								laMFVehData
									.getVehicleData()
									.getVehBdyType();
							if (lsVehBdyType != null
								&& lsVehBdyType.length() > 0)
							{
								((MFVehicleData) laCopyOfDlrMFVehData)
									.getVehicleData()
									.setVehBdyType(
									lsVehBdyType);
							}
							String lsVehMod =
								laMFVehData
									.getVehicleData()
									.getVehModl();
							if (lsVehMod != null
								&& lsVehMod.length() > 0)
							{
								((MFVehicleData) laCopyOfDlrMFVehData)
									.getVehicleData()
									.setVehModl(
									lsVehMod);
							}
							int liDieselIndi =
								laMFVehData
									.getVehicleData()
									.getDieselIndi();
							if (liDieselIndi == 1)
							{
								((MFVehicleData) laCopyOfDlrMFVehData)
									.getVehicleData()
									.setDieselIndi(
									liDieselIndi);
							}
						}
						// If Dealer VIN = null and MF != null then set 
						//	Dealer VIN = MF VIN
						if (laCopyOfDlrMFVehData
							.getVehicleData()
							.getVin()
							== null
							&& laMFVehData.getVehicleData().getVin()
								!= null)
						{
							((MFVehicleData) laCopyOfDlrMFVehData)
								.getVehicleData()
								.setVin(
								laMFVehData.getVehicleData().getVin());
						}
						// Copy Dealer data to MF Veh data
						aaVehInqData.setMfVehicleData(
							laCopyOfDlrMFVehData);
						//if no record found, use vin, dealer disk, then
						//	copy reg plt to copy of old MF for 
						//	transaction table
						MFVehicleData laTransMFVeh =
							aaDlrTtlData.getMFVehicleDataFromMF();
						laTransMFVeh.getRegData().setRegPltNo(
							laDlrMFVehData.getRegData().getRegPltNo());
						// If RgeExpMo and RegExpYr = 0 then set 
						//	aaVehInqData fields to 0
						if (laMFVehData.getRegData().getRegExpMo() == 0
							&& laMFVehData.getRegData().getRegExpYr()
								== 0)
						{
							((MFVehicleData) laCopyOfDlrMFVehData)
								.getRegData()
								.setRegExpMo(
								0);
							((MFVehicleData) laCopyOfDlrMFVehData)
								.getRegData()
								.setRegExpYr(
								0);
						} // end if						
					}
					//aaVehInqData.setMfVehicleData(laCopyOfMFVehData);
					int liDt = 0;
					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					if (UtilityMethods
						.isNumeric(aaDlrTtlData.getTransDate()))
					{
						liDt =
							Integer.parseInt(
								aaDlrTtlData.getTransDate());
					}
					else
					{
						liDt = 0;
					}
					// end defect 7773
					aaVehInqData.setRTSEffDt(liDt);
				}
			}
		}
	}

	/**
	 * Copy Dealer Title Data
	 * 
	 * @param aaCopyOfMFVehData MFVehicleData
	 * @param aaDlrMFVehData MFVehicleData
	 */
	public static void doCopyDlrData(
		MFVehicleData aaCopyOfMFVehData,
		MFVehicleData aaDlrMFVehData)
	{
		//copy values
		TitleData laTtlMFData = aaCopyOfMFVehData.getTitleData();
		TitleData laTtlDlrData = aaDlrMFVehData.getTitleData();
		RegistrationData laRegMFData = aaCopyOfMFVehData.getRegData();
		RegistrationData laRegDlrData = aaDlrMFVehData.getRegData();
		VehicleData laVehMFData = aaCopyOfMFVehData.getVehicleData();
		VehicleData laVehDlrData = aaDlrMFVehData.getVehicleData();
		if (laTtlMFData != null && laTtlDlrData != null)
		{
			//laTtlMFData.setAddlTradeInIndi(
			//	laTtlDlrData.getAddlTradeInIndi());
			laTtlMFData.setDlrGdn(laTtlDlrData.getDlrGdn());
			laTtlMFData.setDocNo(laTtlDlrData.getDocNo());
			laTtlMFData.setImcNo(laTtlDlrData.getImcNo());
			/* Do not overwrite mf lienholder data with dealer data.
			   Lien entry TTL035 will handle the display of dealer 
			   lienholder data.
			//laTtlMFData.setAddlLienRecrdIndi(
			//	laTtlDlrData.getAddlLienRecrdIndi());				
			laTtlMFData.setLienHolder1(laTtlDlrData.getLienHolder1());
			laTtlMFData.setLienHolder1Date(laTtlDlrData.
				getLienHolder1Date());
			laTtlMFData.setLienHolder2(laTtlDlrData.getLienHolder2());
			laTtlMFData.setLienHolder2Date(laTtlDlrData.
				getLienHolder2Date());
			laTtlMFData.setLienHolder3(laTtlDlrData.getLienHolder3());
			laTtlMFData.setLienHolder3Date(laTtlDlrData.
				getLienHolder3Date());
			*/
			laTtlMFData.setPrevOwnrCity(laTtlDlrData.getPrevOwnrCity());
			laTtlMFData.setPrevOwnrName(laTtlDlrData.getPrevOwnrName());
			laTtlMFData.setPrevOwnrState(
				laTtlDlrData.getPrevOwnrState());
			laTtlMFData.setTtlVehAddr(laTtlDlrData.getTtlVehAddr());
			laTtlMFData.setVehSalesPrice(
				laTtlDlrData.getVehSalesPrice());
			laTtlMFData.setVehTradeinAllownce(
				laTtlDlrData.getVehTradeinAllownce());
			laTtlMFData.setVehUnitNo(laTtlDlrData.getVehUnitNo());
			laTtlMFData.setOwnrShpEvidCd(
				laTtlDlrData.getOwnrShpEvidCd());
		}
		if (laRegMFData != null && laRegDlrData != null)
		{
			laRegMFData.setHvyVehUseTaxIndi(
				laRegDlrData.getHvyVehUseTaxIndi());
			laRegMFData.setRecpntName(laRegDlrData.getRecpntName());
			laRegMFData.setRegClassCd(laRegDlrData.getRegClassCd());
			laRegMFData.setRegExpMo(laRegDlrData.getRegExpMo());
			laRegMFData.setRecpntName(laRegDlrData.getRecpntName());
			laRegMFData.setRegExpMo(laRegDlrData.getRegExpMo());
			laRegMFData.setRegExpYr(laRegDlrData.getRegExpYr());
			laRegMFData.setRegPltCd(laRegDlrData.getRegPltCd());
			laRegMFData.setRegPltNo(laRegDlrData.getRegPltNo());
			laRegMFData.setRegStkrNo(laRegDlrData.getRegStkrNo());
			laRegMFData.setRegStkrCd(laRegDlrData.getRegStkrCd());
			laRegMFData.setRenwlMailAddr(
				laRegDlrData.getRenwlMailAddr());
			laRegMFData.setResComptCntyNo(
				laRegDlrData.getResComptCntyNo());
			laRegMFData.setVehCaryngCap(laRegDlrData.getVehCaryngCap());
			//laRegMFData.setVehGrossWt(laRegDlrData.getVehGrossWt());
		}
		if (laVehMFData != null && laVehDlrData != null)
		{
			laVehMFData.setDieselIndi(laVehDlrData.getDieselIndi());
			laVehMFData.setFloodDmgeIndi(
				laVehDlrData.getFloodDmgeIndi());
			laVehMFData.setFxdWtIndi(laVehDlrData.getFxdWtIndi());
			laVehMFData.setPrmtReqrdIndi(
				laVehDlrData.getPrmtReqrdIndi());
			//laVehMFData.setRecondCd(laVehDlrData.getRecondCd());	
			// should use mf data here.
			laVehMFData.setReContIndi(laVehDlrData.getReContIndi());
			
			// defect 11368 
			// Do not use DTA Replica values (changed requirement) 
			//			if (CommonValidations.isValidYearModel(laVehDlrData.getReplicaVehModlYr()) 
			//			&& !UtilityMethods.isEmpty(laVehDlrData.getReplicaVehMk()))
			//			{
			//			if (laVehDlrData.getReplicaVehMk() != null)
			//			{
			//			laVehMFData.setReplicaVehMk(
			//			laVehDlrData.getReplicaVehMk());
			//			}
			//			else
			//			{
			//			laVehMFData.setReplicaVehMk(
			//			new String(CommonConstant.STR_SPACE_EMPTY));
			//			}
			//			laVehMFData.setReplicaVehModlYr(
			//			laVehDlrData.getReplicaVehModlYr());
			//			}
			// end defect 11368 

			laVehMFData.setTrlrType(laVehDlrData.getTrlrType());
			laVehMFData.setVehBdyType(laVehDlrData.getVehBdyType());
			laVehMFData.setVehBdyVin(laVehDlrData.getVehBdyVin());
			laVehMFData.setVehClassCd(laVehDlrData.getVehClassCd());
			laVehMFData.setVehEmptyWt(laVehDlrData.getVehEmptyWt());
			laVehMFData.setVehLngth(laVehDlrData.getVehLngth());
			laVehMFData.setVehMk(laVehDlrData.getVehMk());
			laVehMFData.setVehModl(laVehDlrData.getVehModl());
			// defect 10709
			laVehMFData.setVehMjrColorCd(
				laVehDlrData.getVehMjrColorCd());
			laVehMFData.setVehMnrColorCd(
				laVehDlrData.getVehMnrColorCd());
			// end defect 10709
			laVehMFData.setVehOdmtrBrnd(laVehDlrData.getVehOdmtrBrnd());
			laVehMFData.setVehOdmtrReadng(
				laVehDlrData.getVehOdmtrReadng());
			laVehMFData.setVin(laVehDlrData.getVin());
			laVehMFData.setVehTon(laVehDlrData.getVehTon());
		}
		int liGrossWt =
			CommonValidations.calcGrossWeight(
				String.valueOf(laVehMFData.getVehEmptyWt()),
				String.valueOf(laRegMFData.getVehCaryngCap()));
		if (laRegMFData != null)
		{
			laRegMFData.setVehGrossWt(liGrossWt);
		}
	}

	/**
	 * DealerTitleData constructor comment.
	 */
	public DealerTitleData()
	{
		super();
		caMFVehicleData = new MFVehicleData();

		if (caMFVehicleData != null)
		{
			// Create subobjects and initialize 
			// Title Data
			TitleData laTtlData = new TitleData();

			// Registration Data
			RegistrationData laRegData = new RegistrationData();
			laRegData.setRenwlMailAddr(new AddressData());

			// Owner Data 
			OwnerData laOwnrData = new OwnerData();
			laOwnrData.setAddressData(new AddressData());

			// MFVehicle Data
			caMFVehicleData.setTitleData(laTtlData);
			caMFVehicleData.setRegData(laRegData);
			caMFVehicleData.setOwnerData(laOwnrData);
			caMFVehicleData.setVctSalvage(new Vector());
			caMFVehicleData.getVctSalvage().add(new SalvageData());
			caMFVehicleData.setVehicleData(new VehicleData());
			caVehMiscData = new VehMiscData();
		}
	}

	/**
	 * DealerTitleData constructor comment.
	 */
	public DealerTitleData(String asSkip)
	{
		if (asSkip.equals(TitleConstant.DTA_SKIP_LABEL))
		{
			setForm31No(TitleConstant.DTA_SKIP_LABEL);
			setSkipCurrObj(true);
		}
	}

	/**
	 * Assign Indi 0, 1, blank or null
	 * 
	 * @param asStr 		java.lang.String
	 * @return int
	 */
	public int assignLemonLawIndi(String asStr)
	{
		// If empty assign 0 to the value.
		asStr = asStr.length() == 0 ? "0" : asStr;

		int liIndi = 0;

		try
		{
			// Cast value to Integer
			liIndi = Integer.parseInt(asStr);

			// AbstractValue greater than 1 set Invalid Record to false
			if (liIndi != 0 && liIndi != 1)
			{
				liIndi = 0;
				// defect 10290 
				setInvalidRecord("LemonLawIndi", asStr, "0");
			}
		}
		catch (NumberFormatException aeNFEx)
		{
			setInvalidRecord("LemonLawIndi", asStr, "0");
			// end defect 10290 
		}
		return liIndi;
	}

	/**
	 * Compares this object with the specified object for order.  
	 * Returns a negative integer, zero, or a positive integer as this 
	 * object is less than, equal to, or greater than the specified 
	 * object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.(This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> 
	 * implies <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that 
	 * <tt>x.compareTo(y)==0</tt> implies that <tt>sgn(x.compareTo(z)) 
	 * == sgn(y.compareTo(z))</tt>, for all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally 
	 * speaking, any class that implements the <tt>Comparable</tt> 
	 * interface and violates this condition should clearly indicate 
	 * this fact.  The recommended language is "Note: this class has a 
	 * natural ordering that is inconsistent with equals."
	 * 
	 * @param   aaObject the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this 
	 *		object is less than, equal to, or greater than the specified
	 *		object.
	 * 
	 * @throws ClassCastException if the specified object's type 
	 * 			prevents it from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		DealerTitleData laCompTo = (DealerTitleData) aaObject;
		String lsForm31 = this.getForm31No();
		String lsCompToStr = laCompTo.getForm31No();
		if (lsForm31 != null && lsCompToStr != null)
		{
			return lsForm31.compareTo(lsCompToStr);
		}
		return -1;
	}

	/**
	 * Return value of AddlToknFeeIndi
	 * 
	 * @return int
	 */
	public int getAddlToknFeeIndi()
	{
		return ciAddlToknFeeIndi;
	}

	/**
	 * Return value of ChrgTransfrFee
	 * 
	 * @return int
	 */
	public int getChrgTrnsfrFee()
	{
		return ciChrgTrnsfrFee;
	}

	/**
	 * get Customer Supplied Plate age
	 * 
	 * @return
	 */
	public int getCustSuppliedPltAge()
	{
		return ciCustSuppliedPltAge;
	}

	/**
	 * get Customer Supplied Indi
	 * 
	 * @return int
	 */
	public int getCustSuppliedPltIndi()
	{
		return ciCustSuppliedPltIndi;
	}

	/**
	 * Return DealerId
	 * 
	 * @return int
	 */
	public int getDealerId()
	{
		return ciDealerId;
	}

	/**
	 * Return value of DlrSeqNo
	 * 
	 * @return String
	 */
	public String getDealerSeqNo()
	{
		return csDealerSeqNo;
	}

	/**
	 * Return Vector of DTAInvalidRecordData
	 * 
	 * @return
	 */
	public Vector getDTAInvalidRecordData()
	{
		return cvDTAInvalidRecordData;
	}

	/**
	 * Return derived value of Dealer BatchNo
	 * 
	 * @return String
	 */
	public String getDlrBatchNo()
	{
		return getStrDealerId()
			+ CommonConstant.STR_DASH
			+ getDealerSeqNo();
	}

	/**
	 * Get ETtlRqst
	 * 
	 * @return int
	 */
	public int getETtlRqst()
	{
		return ciETtlRqst;
	}

	/**
	 * Return value of Fee
	 * 
	 * @return Dollar
	 */
	public Dollar getFee()
	{
		return caFee;
	}

	/**
	 * Get data from Dealer Data Object.
	 * 
	 * @parm aiFieldNum int
	 * @return java.lang.String
	 */
	public String getField(int aiFieldNum)
	{
		// defect 7770
		//	Set case index to field name
		//	Replace 126 return with 1 
		// defect 9656
		//	 Change CommonConstant.STR_EMPTY to null
		String lsRtn = null;
		// end defect 9656
		// defect 10112 
		LienholderData laLienHldrData1 =
			caMFVehicleData.getTitleData().getLienholderData(
				TitleConstant.LIENHLDR1);
		AddressData laLienAddressData1 =
			laLienHldrData1.getAddressData();

		LienholderData laLienHldrData2 =
			caMFVehicleData.getTitleData().getLienholderData(
				TitleConstant.LIENHLDR2);
		AddressData laLienAddressData2 =
			laLienHldrData2.getAddressData();

		LienholderData laLienHldrData3 =
			caMFVehicleData.getTitleData().getLienholderData(
				TitleConstant.LIENHLDR3);

		AddressData laLienAddressData3 =
			laLienHldrData3.getAddressData();
		// end defect 10112 

		switch (aiFieldNum)
		{
			case ADDLLIENRECRDINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getAddlLienRecrdIndi();
				break;
			case ADDLTOKNFEEINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getAddlToknFeeIndi();
				break;
			case ADDLTRADEINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getVehMiscData().getAddlTradeInIndi();
				break;
			case ASEQNO :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY + getDealerSeqNo();
				break;
			case CHRGTRNSFRFEEINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY + getChrgTrnsfrFee();
				break;
			case DIESELINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getVehicleData()
							.getDieselIndi();
				break;
			case DLRGDN :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getTitleData().getDlrGdn();
				break;
			case DEALERID :
				lsRtn = CommonConstant.STR_SPACE_EMPTY + getDealerId();
				break;
			case DLRTRANSDATE :
				lsRtn = CommonConstant.STR_SPACE_EMPTY + getTransDate();
				break;
			case DOCNO :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getTitleData().getDocNo();
				break;
			case FEE :
				lsRtn = CommonConstant.STR_SPACE_EMPTY + getFee();
				break;
			case FLOODMGEINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getVehicleData()
							.getFloodDmgeIndi();
				break;
			case FORM31NO :
				lsRtn = CommonConstant.STR_SPACE_EMPTY + getForm31No();
				break;
			case FXDWTINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getVehicleData().getFxdWtIndi();
				break;
			case HVYVEHUSETAXINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getRegData()
							.getHvyVehUseTaxIndi();
				break;
			case IMCNO :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getVehMiscData().getIMCNo();
				break;

				// defect 10112 
				// LIENHOLDER 1 DATA 
			case LIEN1DATE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ laLienHldrData1.getLienDate();
				break;

			case LIENHLDR1NAME1 :
				lsRtn = laLienHldrData1.getName1();
				break;

			case LIENHLDR1NAME2 :
				lsRtn = laLienHldrData1.getName2();
				break;

			case LIENHLDR1ST1 :
				lsRtn = laLienAddressData1.getSt1();
				break;

			case LIENHLDR1ST2 :
				lsRtn = laLienAddressData1.getSt2();
				break;

			case LIENHLDR1CITY :
				lsRtn = laLienAddressData1.getCity();
				break;

			case LIENHLDR1STATE :
				lsRtn = laLienAddressData1.getState();
				break;

			case LIENHLDR1CNTRY :
				lsRtn = laLienAddressData1.getCntry();
				break;

			case LIENHLDR1ZPCD :
				lsRtn = laLienAddressData1.getZpcd();
				break;

			case LIENHLDR1ZPCDP4 :
				lsRtn = laLienAddressData1.getZpcdp4();
				break;
			case LIEN2DATE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ laLienHldrData2.getLienDate();
				break;

			case LIENHLDR2NAME1 :
				lsRtn = laLienHldrData2.getName1();
				break;

			case LIENHLDR2NAME2 :
				lsRtn = laLienHldrData2.getName2();
				break;

			case LIENHLDR2ST1 :
				lsRtn = laLienAddressData2.getSt1();
				break;

			case LIENHLDR2ST2 :
				lsRtn = laLienAddressData2.getSt2();
				break;

			case LIENHLDR2CITY :
				lsRtn = laLienAddressData2.getCity();
				break;

			case LIENHLDR2STATE :
				lsRtn = laLienAddressData2.getState();
				break;

			case LIENHLDR2CNTRY :
				lsRtn = laLienAddressData2.getCntry();
				break;

			case LIENHLDR2ZPCD :
				lsRtn = laLienAddressData2.getZpcd();
				break;

			case LIENHLDR2ZPCDP4 :
				lsRtn = laLienAddressData2.getZpcdp4();
				break;

				// LIENHOLDER 3 DATA 
			case LIEN3DATE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ laLienHldrData3.getLienDate();
				break;

			case LIENHLDR3NAME1 :
				lsRtn = laLienHldrData3.getName1();
				break;

			case LIENHLDR3NAME2 :
				lsRtn = laLienHldrData3.getName2();
				break;

			case LIENHLDR3ST1 :
				lsRtn = laLienAddressData3.getSt1();
				break;

			case LIENHLDR3ST2 :
				lsRtn = laLienAddressData3.getSt2();
				break;

			case LIENHLDR3CITY :
				lsRtn = laLienAddressData3.getCity();
				break;

			case LIENHLDR3STATE :
				lsRtn = laLienAddressData3.getState();
				break;

			case LIENHLDR3CNTRY :
				lsRtn = laLienAddressData3.getCntry();
				break;

			case LIENHLDR3ZPCD :
				lsRtn = laLienAddressData3.getZpcd();
				break;

			case LIENHLDR3ZPCDP4 :
				lsRtn = laLienAddressData3.getZpcdp4();
				break;
				// end defect 9969 
				// end defect 10112 

			case NEWPLTNO :
				lsRtn = CommonConstant.STR_SPACE_EMPTY + getNewPltNo();
				break;
			case NEWREGEXPMO :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY + getNewRegExpMo();
				break;
			case NEWREGEXPYR :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY + getNewRegExpYr();
				break;
				// defect 10232
				// 	LEMONLAWINDI replaces NEWSTKRNO
				//			case NEWSTKRNO :
				//				lsRtn = CommonConstant.STR_SPACE_EMPTY + getNewStkrNo();
			case LEMONLAWINDI :
				// defect 10290 
				lsRtn = CommonConstant.STR_SPACE_EMPTY +
					//getLemonLawIndi();
	caMFVehicleData.getTitleData().getLemonLawIndi();
				// end defect 10290 
				// end defect 10232
				break;
			case OFFHWYUSEINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY + getOffHghwyIndi();
				break;
			case OWNRCITY :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getOwnerData()
							.getAddressData()
							.getCity();
				break;
			case OWNRCNTRY :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getOwnerData()
							.getAddressData()
							.getCntry();
				break;
			case OWNRID :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getOwnerData().getOwnrId();
				break;
			case OWNRSHPEVIDCD :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getOwnrShpEvidCd();
				break;
			case OWNRST1 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getOwnerData()
							.getAddressData()
							.getSt1();
				break;
			case OWNRST2 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getOwnerData()
							.getAddressData()
							.getSt2();
				break;
			case OWNRSTATE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getOwnerData()
							.getAddressData()
							.getState();
				break;
			case OWNRTTLNAME1 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getOwnerData().getName1();
				break;
			case OWNRTTLNAME2 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getOwnerData().getName2();
				break;
			case OWNRZPCD :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getOwnerData()
							.getAddressData()
							.getZpcd();
				break;
			case OWNRZPCDP4 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getOwnerData()
							.getAddressData()
							.getZpcdp4();
				break;
			case PREVOWNRCITY :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getPrevOwnrCity();
				break;
			case PREVOWNRNAME :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getPrevOwnrName();
				break;
			case PREVOWNRSTATE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getPrevOwnrState();
				break;
			case PRMTREQRDINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getVehicleData()
							.getPrmtReqrdIndi();
				break;
			case RECONDINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getVehicleData().getRecondCd();
				break;
			case RECONTINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getVehicleData()
							.getReContIndi();
				break;
			case RECPNTNAME :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getRegData().getRecpntName();
				break;
			case REGCLASSCD :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getRegData().getRegClassCd();
				break;
			case REGEXPMO :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getRegData().getRegExpMo();
				break;
			case REGEXPYR :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getRegData().getRegExpYr();
				break;
			case REGPLTCD :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getRegData().getRegPltCd();
				break;
			case REGPLTNO :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getRegData().getRegPltNo();
				break;
			case REGSTKRCD :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getRegData().getRegStkrCd();
				break;
			case REGSTKRNO :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getRegData().getRegStkrNo();
				break;
			case RENWLMAILNGCITY :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getRegData()
							.getRenwlMailAddr()
							.getCity();
				break;
			case RENWLMAILNGST1 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getRegData()
							.getRenwlMailAddr()
							.getSt1();
				break;
			case RENWLMAILNGST2 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getRegData()
							.getRenwlMailAddr()
							.getSt2();
				break;
			case RENWLMAILNGSTATE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getRegData()
							.getRenwlMailAddr()
							.getState();
				break;
			case RENWLMAILNGZPCD :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getRegData()
							.getRenwlMailAddr()
							.getZpcd();
				break;
			case RENWLMAILNGZPCDP4 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getRegData()
							.getRenwlMailAddr()
							.getZpcdp4();
				break;
			case REPLICAVEHMK :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getVehicleData()
							.getReplicaVehMk();
				break;
			case REPLICAVEHMODLYR :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getVehicleData()
							.getReplicaVehModlYr();
				break;
			case RESCOMPTCNTYNO :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getRegData()
							.getResComptCntyNo();
				break;
			case SALESTAXCAT :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getVehMiscData().getSalesTaxCat();
				break;
			case SALESTAXDATE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getVehMiscData().getSalesTaxDate();
				break;
			case SALESTAXEXMPTCD :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getVehMiscData().getSalesTaxExmptCd();
				break;
			case SALESTAXPNTLYPRCNT :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getVehMiscData().getSalesTaxPnltyPer();
				break;
			case SURVSHPRIGHTSINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getSurvshpRightsIndi();
				break;
			case TAXPDOTHRSTATE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getVehMiscData().getTaxPdOthrState();
				break;
			case TRADEINVEHMK :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getVehMiscData().getTradeInVehMk();
				break;
			case TRADEINVEHYR :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getVehMiscData().getTradeInVehYr();
				break;
			case TRADEINVIN :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getVehMiscData().getTradeInVehVIN();
				break;
			case TRLRTYPE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getVehicleData().getTrlrType();
				break;
			case TTLVEHLOCCITY :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getTtlVehAddr()
							.getCity();
				break;
			case TTLVEHLOCST1 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getTtlVehAddr()
							.getSt1();
				break;
			case TTLVEHLOCST2 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getTtlVehAddr()
							.getSt2();
				break;
			case TTLVEHLOCSTATE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getTtlVehAddr()
							.getState();
				break;
			case TTLVEHLOCZPCD :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getTtlVehAddr()
							.getZpcd();
				break;
			case TTLVEHLOCZPCDP4 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getTtlVehAddr()
							.getZpcdp4();
				break;
			case VEHBDYTYPE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getVehicleData()
							.getVehBdyType();
				break;
			case VEHBDYVIN :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getVehicleData().getVehBdyVin();
				break;
			case VEHCARYNGCAP :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getRegData().getVehCaryngCap();
				break;
			case VEHCLASSCD :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getVehicleData()
							.getVehClassCd();
				break;
			case VEHEMPTYWT :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getVehicleData()
							.getVehEmptyWt();
				break;
			case VEHLNGTH :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getVehicleData().getVehLngth();
				break;
			case VEHMK :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getVehicleData().getVehMk();
				break;
			case VEHMODL :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getVehicleData().getVehModl();
				break;
			case VEHMODLYR :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getVehicleData().getVehModlYr();
				break;
			case VEHODMTRBRND :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getVehicleData()
							.getVehOdmtrBrnd();
				break;
			case VEHODMTRREADNG :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getVehicleData()
							.getVehOdmtrReadng();
				break;
			case VEHSALESPRICE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getVehSalesPrice();
				break;
			case VEHTON :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getVehicleData().getVehTon();
				break;
			case VEHTRADEINALLOWANCE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getVehTradeinAllownce();
				break;
			case VEHUNITNO :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getTitleData().getVehUnitNo();
				break;
			case VIN :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getVehicleData().getVin();
				break;
			case ZTOTALREBATEAMT :
				lsRtn = CommonConstant.STR_SPACE_EMPTY + getRebateAmt();
				break;
				// defect 9973
				// Define new base format (118 to 120) fields.
				//	RSPS data will be appended at 121-128 or 125-132.
				//	119 and 120 have been move to the new base file format
				//	Add CUSTSUPPLYINDI and CUSTSUPPLYPLTAGE to the
				//	base format
			case CUSTSUPPLYINDI :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getCustSuppliedPltIndi();
				break;
			case CUSTSUPPLYPLTAGE :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ getCustSuppliedPltAge();
				break;
				// end defect 9973

				// defect 10509
			case PERMLIENHLDR1 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getPermLienHldrId1();
				break;
			case PERMLIENHLDR2 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getPermLienHldrId2();
				break;
			case PERMLIENHLDR3 :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getTitleData()
							.getPermLienHldrId3();
				break;
			case ETTLRQST :
				lsRtn = CommonConstant.STR_SPACE_EMPTY + getETtlRqst();
				break;
				// end defect 10509
				// defect 10709
			case EMAILRENWLREQCD :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData
							.getRegData()
							.getEMailRenwlReqCd();
				break;
			case RECPNTEMAIL :
				lsRtn =
					CommonConstant.STR_SPACE_EMPTY
						+ caMFVehicleData.getRegData().getRecpntEMail();
				break;
			case FIELD_INDEX_127 : // RSPSPRNTINVQTY/VEHMAJORCOLORCD
				{
					if (getRecType() == RSPS_FRMT_CD)
					{
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ getRSPSPrntInvQty();
					}
					else
					{
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ caMFVehicleData
									.getVehicleData()
									.getVehMjrColorCd();
					}
					break;
				}
			case FIELD_INDEX_128 : // POSPRNTINVQTY/VEHMINORCOLORCD
				{
					if (getRecType() == RSPS_FRMT_CD)
					{
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ getPosPrntInvQty();
					}
					else
					{
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ caMFVehicleData
									.getVehicleData()
									.getVehMnrColorCd();
					}
					break;
				}
				// defect 10752 
			case FIELD_INDEX_129 : // POSPROCSDINDI/MILITARYINDI
				{
					if (getRecType() == RSPS_FRMT_CD)
					{
						lsRtn = "0";
						if (cbPOSProcsIndi)
						{
							lsRtn = "1";
						}
					}
					else
					{
						//		lsRtn =
						//			CommonConstant.STR_SPACE_EMPTY
						//			+ getRSPSPrntInvQty();
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ getMilitaryIndi();
					}
					break;
				}

			case FIELD_INDEX_130 : // VOIDED/RSPSPRNTINVQTY
				{
					if (getRecType() == RSPS_FRMT_CD)
					{
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY + ciVoided;
					}
					else
					{
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ getRSPSPrntInvQty();
						//+ getPosPrntInvQty();

					}
					break;
				}
			case FIELD_INDEX_131 : // UPDTPOSRSPSPRNTINDI/POSPRNTINVQTY
				{
					if (getRecType() == RSPS_FRMT_CD)
					{
						lsRtn = "0";
						if (cbUpdtPOSRSPSPrntIndi)
						{
							lsRtn = "1";
						}
					}
					else
					{
						//		lsRtn = "0";
						//		if (cbPOSProcsIndi)
						//		{
						//			lsRtn = "1";
						//		}
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ getPosPrntInvQty();
					}
					break;
				}
			case FIELD_INDEX_132 : // RSPSDISKSEQNO/POSPROCSDINDI
				{
					if (getRecType() == RSPS_FRMT_CD)
					{
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ getRSPSDiskSeqNo();
					}
					else
					{
						lsRtn = "0";
						if (cbPOSProcsIndi)
						{
							lsRtn = "1";
						}

						//		lsRtn =
						//			CommonConstant.STR_SPACE_EMPTY + ciVoided;
					}
					break;
				}
			case FIELD_INDEX_133 : // RSPSID/VOIDEDINDI
				{
					if (getRecType() == RSPS_FRMT_CD)
					{
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ getRSPSId();
					}
					else
					{
						//		lsRtn = "0";
						//		if (cbUpdtPOSRSPSPrntIndi)
						//		{
						//			lsRtn = "1";
						//		}
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY + ciVoided;
					}
					break;
				}
			case FIELD_INDEX_134 : // RSPSORIGPRNTDDATE/UPDTPOSRSPSPRNTINDI
				{
					if (getRecType() == RSPS_FRMT_CD)
					{
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ getRSPSOrigPrntDate();
					}
					else
					{
						//		lsRtn =
						//			CommonConstant.STR_SPACE_EMPTY
						//+ getRSPSDiskSeqNo();
						lsRtn = "0";
						if (cbUpdtPOSRSPSPrntIndi)
						{
							lsRtn = "1";
						}

					}
					break;
				}
			case FIELD_INDEX_135 : // RSPSDISKSEQNO
				{
					if (getRecType() == RSPS_VEHCOLOR_FRMT_CD)
					{
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ getRSPSDiskSeqNo();
					}
					break;
				}
			case FIELD_INDEX_136 : // RSPSID
				{
					if (getRecType() == RSPS_VEHCOLOR_FRMT_CD)
					{
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ getRSPSId();
					}
					break;
				}

			case FIELD_INDEX_137 : // RSPSORIGPRNTDDATE
				{
					if (getRecType() == RSPS_VEHCOLOR_FRMT_CD)
					{
						lsRtn =
							CommonConstant.STR_SPACE_EMPTY
								+ getRSPSOrigPrntDate();
					}
					break;
				}
				// end defect 10709
				// end defect 9656
			default :
				lsRtn = null;
		} // end defect 7770

		return lsRtn;
	}

	/**
	 * Get data from Form31No
	 * 
	 * @return String
	 */
	public String getForm31No()
	{
		return csForm31No;
	}

	/**
	 * Return InventoryData
	 * 
	 * @return Vector
	 */
	public Vector getInventoryData()
	{
		return cvInventoryData;
	}

	//	/**
	//	 * Get the value of LemonLawIndi
	//	 * 
	//	 * @param asNewStkrNo String
	//	 */
	//	public String getLemonLawIndi()
	//	{
	//		return csLemonLawIndi;
	//	}

	/**
	 * Return LienError
	 * 
	 * @return Vector
	 */
	public Vector getLienError()
	{
		return cvLienError;
	}

	/**
	 * 
	 * Return MFVehicleData
	 * 
	 * @return MFVehicleData
	 */
	public MFVehicleData getMFVehicleData()
	{
		return caMFVehicleData;
	}

	/**
	 * Return MFVehicleDataFromMF
	 * 
	 * @return MFVehicleData
	 */
	public MFVehicleData getMFVehicleDataFromMF()
	{
		return caMFVehicleDataFromMF;
	}
	
	/**
	 * Return value of ciMilitaryIndi
	 * 
	 * @return int 
	 */
	public int getMilitaryIndi()
	{
		return ciMilitaryIndi;
	}
	
	/** 
	 * Is Military
	 * 
	 * @return boolean 
	 */
	public boolean isMilitary()
	{
		return ciMilitaryIndi == 1; 	
	}

	/**
	 * Return NewPltNo
	 * 
	 * @return String
	 */
	public String getNewPltNo()
	{
		return csNewPltNo;
	}

	/**
	 * Return NewRegExpMo
	 * 
	 * @return int 
	 */
	public int getNewRegExpMo()
	{
		return ciNewRegExpMo;
	}

	/**
	 * Return NewRegExpYr
	 * 
	 * @return int 
	 */
	public int getNewRegExpYr()
	{
		return ciNewRegExpYr;
	}

	/**
	 * Return OffHghwyIndi
	 * 
	 * @return int
	 */
	public int getOffHghwyIndi()
	{
		return ciOffHghwyIndi;
	}

	/**
	 * Return POS Print Inv Quanity
	 * 
	 * @return int
	 */
	public int getPosPrntInvQty()
	{
		return ciPOSPrntInvQty;
	}

	/**
	 * Return TtlRebateAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getRebateAmt()
	{
		return caTtlRebateAmt;
	}

	/**
	 * Get Record Type
	 * 
	 * @return
	 */
	public int getRecType()
	{
		return ciRecType;
	}

	/**
	 * Return RegFee
	 * 
	 * @return Vector 
	 */
	public Vector getRegFee()
	{
		return cvRegFee;
	}

	/**
	 * Return value of DisketteNumber
	 * 
	 * @return int
	 */
	public int getRSPSDiskSeqNo()
	{
		return ciRSPSDiskSeqNo;
	}

	/**
	 * Return RSPSId
	 * 
	 * @return String 
	 */
	public String getRSPSId()
	{
		return csRSPSId;
	}

	/**
	 * Return RSPS Original Print Date.
	 * 
	 * @return String
	 */
	public String getRSPSOrigPrntDate()
	{
		return csRSPSOrigPrntDate;
	}

	/**
	 * Return value of RSPSPrntInvQty
	 * 
	 * @return int
	 */
	public int getRSPSPrntInvQty()
	{
		return ciRSPSPrntInvQty;
	}

	/**
	 * Return DealerId  (String) 
	 * 
	 * @return String
	 */
	public String getStrDealerId()
	{
		return UtilityMethods.addPadding(
			csDealerId == null
				? CommonConstant.STR_SPACE_EMPTY
				: csDealerId,
			TitleConstant.DTA_MAX_DEALER_ID_LENGTH,
			CommonConstant.STR_ZERO);
	}

	/**
	 * Return TransCd
	 * 
	 * @return String 
	 */
	public String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Return TransDt 
	 * 
	 * @return String 
	 */
	public String getTransDate()
	{
		return csTransDate;
	}

	/**
	 * Return CompleteTransFee 
	 * 
	 * @return Dollar 
	 */
	public Dollar getTransFee()
	{
		return caCompleteTransFee;
	}

	/**
	 * Return TransId 
	 * 
	 * @return String
	 */
	public String getTransId()
	{
		return csTransId;
	}

	/**
	 * Return TransNo
	 * 
	 * @return int
	 */
	public int getTransNo()
	{
		return ciTransNo;
	}

	/**
	 * Return VehMiscData
	 * 
	 * @return VehMiscData
	 */
	public VehMiscData getVehMiscData()
	{
		return caVehMiscData;
	}

	/**
	* Return Voided
	* 
	* @return int
	*/
	public int getVoided()
	{
		return ciVoided;
	}

	/**
	 * Return boolean based on whether CustSuppliedPltIndi == 1
	 * 
	 * @return boolean 
	 */
	public boolean isCustSuppliedPlt()
	{
		return ciCustSuppliedPltIndi == 1;
	}

	// defect 9973 
	/**
	 * Is ETtlRqst = 1 
	 * 
	 * @return boolean
	 */
	public boolean isETtlRqst()
	{
		return ciETtlRqst == 1;
	}

	/**
	 * Return InvalidRecord
	 * 
	 * @return boolean
	 */
	public boolean isInvalidRecord()
	{
		return cbInvalidRecord;
	}

	/**
	 * Return KeyBoardEntry
	 * 
	 * @return boolean
	 */
	public boolean isKeyBoardEntry()
	{
		return cbKeyBoardEntry;
	}

	/**
	 * Return POSProcsIndi
	 * 
	 * @return boolean 
	 */
	public boolean isPOSProcsIndi()
	{
		return cbPOSProcsIndi;
	}

	//	/**
	//	 * Return Processed
	//	 * 
	//	 * @return boolean
	//	 */
	//	public boolean isProcessed()
	//	{
	//		return cbProcessed;
	//	}

	/**
	 * Return RecordRejected
	 * 
	 * @return boolean
	 */
	public boolean isRecordRejected()
	{
		return cbRecordRejected;
	}

	/**
	 * Return boolean to indicate if "Skipped" record
	 * 
	 * @return boolean
	 */
	public boolean isSkipCurrObj()
	{
		return cbSkipCurrObj;
	}

	/**
	 * Return ToBePrinted
	 * 
	 * @return boolean 
	 */
	public boolean isToBePrinted()
	{
		return cbToBePrinted;
	}

	/**
	 * Return UpdtPOSRSPSPrntIndi 
	 * 
	 * @return boolean
	 */
	public boolean isUpdtPOSRSPSPrntIndi()
	{
		return cbUpdtPOSRSPSPrntIndi;
	}
	//	/**
	//	 * Return NoVinChecked
	//	 * 
	//	 * @return boolean
	//	 */
	//	public boolean isNoVinChecked()
	//	{
	//		return cbNoVinChecked;
	//	}

	/**
	 * Return (Voided >0)
	 *
	 * @return boolean
	 */
	public boolean isVoided()
	{
		return (ciVoided > 0);
	}

	/**
	 * Sets the value of AddlToknFeeIndi
	 * 
	 * @param aiAddlToknFeeIndi int 
	 */
	public void setAddlToknFeeIndi(int aiAddlToknFeeIndi)
	{
		ciAddlToknFeeIndi = aiAddlToknFeeIndi;
	}

	/**
	 * Sets the value of ChrgTrnsfrFee
	 * 
	 * @param aiChrgTrnsfrFee int 
	 */
	public void setChrgTrnsfrFee(int aiChrgTrnsfrFee)
	{
		ciChrgTrnsfrFee = aiChrgTrnsfrFee;
	}

	/**
	 * set Customer Supplied Plate age
	 * 
	 * @param aiCustSuppliedPltAge
	 */
	public void setCustSuppliedPltAge(int aiCustSuppliedPltAge)
	{
		ciCustSuppliedPltAge = aiCustSuppliedPltAge;
	}

	/**
	 * set Customer Supplied Plt Indi
	 * 
	 * @param aiCustSuppliedPltIndi
	 */
	public void setCustSuppliedPltIndi(int aiCustSuppliedPltIndi)
	{
		ciCustSuppliedPltIndi = aiCustSuppliedPltIndi;
	}

	/**
	 * Sets the value of DealerId
	 * 
	 * @param aiDealerId int 
	 */
	public void setDealerId(int aiDealerId)
	{
		ciDealerId = aiDealerId;
	}

	/**
	 * Sets DealerId 
	 * (Note: getStrDlrId is used to retrieve DealerId(String))
	 * 
	 * @param asDlrId String
	 */
	public void setDealerId(String aiDealerId)
	{
		csDealerId = aiDealerId;
	}

	/**
	 * Sets the value of DlrSeqNo
	 * 
	 * @param asDlrSeqNo String 
	 */
	public void setDealerSeqNo(String asDlrSeqNo)
	{
		csDealerSeqNo = asDlrSeqNo;
	}

	/**
	 * Set Vector of DTAInvalidRecordData
	 * 
	 * @return
	 */
	public void setDTAInvalidRecordData(Vector avDTAInvalidRecordData)
	{
		cvDTAInvalidRecordData = avDTAInvalidRecordData;
	}

	/**
	 * Set ETtlRqst
	 * 
	 * @param aiETtlRqst
	 */
	public void setETtlRqst(int aiETtlRqst)
	{
		ciETtlRqst = aiETtlRqst;
	}

	/**
	 * Sets the value of Fee
	 * 
	 * @param aaFee Dollar
	 */
	public void setFee(Dollar aaFee)
	{
		caFee = aaFee;
	}

	/**
	 * Set Dealer Title data object from Dealer Diskette
	 * 
	 * @param aiFieldNum int
	 * @param asValue String
	 */
	public void setField(int aiFieldNum, String asValue)
	{
	}

	/**
	 * Set Dealer Title data object from Dealer Diskette
	 * This method includes changes after MF Version U
	 * 
	 * @param aiFieldNum 	int		case number for field 
	 * @param asValue 		String	data
	 * @param asMFVer		String	Record type (120, 124, 128, 132)
	 */
	public void setField(int aiFieldNum, String asValue, int aiRecType)
	{
		// defect 5142
		// Validate all input fields.
		// Set input parameters to Hungarian Notation:
		//		fieldNum to asFieldNum and 'value' to 'asValue'
		// Set input field to a EMPTY if null
		if (asValue == null)
		{
			// defect 6844
			//	Replace DEFAULT with EMPTY
			asValue = EMPTY;
			// end defect 6844
		}
		// end defect 5142
		// defect 10709
		asValue = asValue.toUpperCase();
		// end defect 10709
		OwnerData laOwnrData = caMFVehicleData.getOwnerData();
		RegistrationData laRegData = caMFVehicleData.getRegData();
		TitleData laTtlData = caMFVehicleData.getTitleData();
		LienholderData laLienHldrData1 =
			caMFVehicleData.getTitleData().getLienholderData(
				TitleConstant.LIENHLDR1);
		AddressData laLienAddressData1 =
			laLienHldrData1.getAddressData();

		LienholderData laLienHldrData2 =
			caMFVehicleData.getTitleData().getLienholderData(
				TitleConstant.LIENHLDR2);
		AddressData laLienAddressData2 =
			laLienHldrData2.getAddressData();

		LienholderData laLienHldrData3 =
			caMFVehicleData.getTitleData().getLienholderData(
				TitleConstant.LIENHLDR3);

		AddressData laLienAddressData3 =
			laLienHldrData3.getAddressData();
		VehicleData laVehData = caMFVehicleData.getVehicleData();

		String lsSaveValue = (asValue == null ? "" : asValue.trim());

		switch (aiFieldNum)
		{
			// defect 7770
			//	set case number to field name
			case ADDLLIENRECRDINDI : // AddlLienRecrdIndi
				{

					// defect 10075
					// diskValidations() refactored/renamed to
					// mediaValidations() in all cases below
					// end defect 10075

					laTtlData.setAddlLienRecrdIndi(
						MediaValidations.validateIndi(asValue));
					break;
				}
			case ADDLTOKNFEEINDI : // AddlToknFeeIndi
				{
					setAddlToknFeeIndi(
						MediaValidations.validateIndi(asValue));
					break;
				}
			case ADDLTRADEINDI : // AddlTradeInIndi
				{
					getVehMiscData().setAddlTradeInIndi(
						MediaValidations.validateIndi(asValue));
					break;
				}
			case ASEQNO : // DlrSeqNo
				{
					// defect 6844
					//	Replace DEFAULT with EMPTY
					// Intilize SeqNo Number
					setDealerSeqNo(EMPTY);
					// end defect 6844

					int liIndi = ZERO;
					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						liIndi = Integer.parseInt(asValue);
						if (liIndi > 0 && liIndi <= 999)
						{
							setDealerSeqNo(asValue);
						}
					}
					// end defect 7773
					break;
				}
			case CHRGTRNSFRFEEINDI : // ChrgTrnsfrFee
				{
					setChrgTrnsfrFee(
						MediaValidations.validateIndi(asValue));
					break;
				}
			case DIESELINDI : // DieselIndi
				{
					laVehData.setDieselIndi(
						MediaValidations.validateIndi(asValue));
					break;
				}
			case DLRGDN : // DlrGDN
				{
					// defect 9367
					// Accept any value the dealer sends
					// 	FrmOwnerEntryTTL007.setDlrGDN() will 
					//	pad/substring the value
					//	if (asValue.length() <= 6)
					//	{
					laTtlData.setDlrGdn(asValue);
					//	}
					//	else
					//	{
					//		// defect 6844
					//		//	Replace DEFAULT with EMPTY
					//		laTtlData.setDlrGdn(EMPTY);
					//		// end defect 6844
					//	}
					// end defect 9367
					break;
				}
			case DEALERID : // DealerId
				{
					// Initialize DealerId
					// asValue is the value from the diskette.
					setDealerId(-1);
					setDealerId(asValue);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						int liDealerId = Integer.parseInt(asValue);
						if (liDealerId > 0 && liDealerId <= 999)
						{
							setDealerId(liDealerId);
						}
					}
					// end defect 7773
					break;
				}
			case DLRTRANSDATE : // DlrTransDate
				{
					if (MediaValidations.validateDtFormat(asValue))
					{
						setTransDt(asValue);
						// defect 8260
						//	Set class variables month/year to DlrTransDt
						ciMonth =
							Integer.parseInt(
								getTransDate().substring(4, 6));
						ciYear =
							Integer.parseInt(
								getTransDate().substring(0, 4));
						// end defect 8260

					}
					else
					{
						// defect 6844
						//	Replace DEFAULT with EMPTY
						setTransDt(EMPTY);
						// end defect 6844

						// defect 10290 
						setInvalidRecord(
							"TransDate",
							lsSaveValue,
							EMPTY);
						// end defect 10290 

						// defect 8260
						//	Set class variables month/year to RTSDate
						ciMonth = RTSDate.getCurrentDate().getMonth();
						ciYear = RTSDate.getCurrentDate().getYear();
						// end defect 8260

					}
					break;
				}
			case DOCNO : // DocNo
				{
					// Initialize DocNo
					laTtlData.setDocNo(EMPTY);

					// Note: 4775 - no validation indi set for DocNo, 
					//	RTSI
					// boolean bBadVal = true;
					// defect 6844
					//	If DocNo from Dealer is 0, or non-numeric or 
					//	length not equal to 17 then set DocNo to EMPTY.
					//	if (asValue.length() == 17
					//		|| asValue.compareTo(ZERODOCNO) == ZERO
					//		&& (UtilityMethods.isNumeric(asValue)
					//			&& asValue.indexOf(CommonConstant.STR_PERIOD)
					//== -1))
					// defect 10144
					//	Adjust statement that DocNo from dealer is 
					//	carried forward if and only if:
					//		17 characters
					//		Numeric and not equal to zero
					//		Not a BigDecimal(no decimal value)
					if ((asValue.length() == 17
						&& (UtilityMethods.isNumeric(asValue)
							&& asValue.indexOf(CommonConstant.STR_PERIOD)
								== -1))
						&& !asValue.equals(ZERODOCNO))
					{
						laTtlData.setDocNo(asValue);
					}
					// end defect 10144
					// end defect 6844
					// end defect 4775
					break;
				}
			case FEE : // Fees
				{
					boolean lbBadVal = true;

					// Initialize Fee
					setFee(new Dollar(CommonConstant.STR_ZERO_DOLLAR));

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue))
					{
						if (Float.parseFloat(asValue) <= 999999.99)
						{
							setFee(new Dollar(asValue));
							lbBadVal = false;
						}
					}
					// end defect 7773
					if (lbBadVal)
					{
						// defect 10290 
						setInvalidRecord(
							"Fee",
							lsSaveValue,
							CommonConstant.STR_ZERO_DOLLAR);
						// end defect 10290 
					}
					break;
				}
			case FLOODMGEINDI : // FloodDmgeIndi
				{
					laVehData.setFloodDmgeIndi(
						MediaValidations.validateIndi(asValue));
					break;
				}
			case FORM31NO : // Form31No
				{
					if (asValue.length() <= 7)
					{
						setForm31No(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"Form31No",
							lsSaveValue,
							EMPTY);
						// end defect 10290 

						// defect 6844
						//	Replace DEFAULT with EMPTY
						setForm31No(EMPTY);
						// end defect 6844
					}
					break;
				}
			case FXDWTINDI : // FxdWtIndi
				{
					laVehData.setFxdWtIndi(
						MediaValidations.validateIndi(asValue));
					break;
				}
			case HVYVEHUSETAXINDI : // HvyVehUseTaxIndi
				{
					laRegData.setHvyVehUseTaxIndi(
						MediaValidations.validateIndi(asValue));
					break;
				}
			case IMCNO : // IMCNo
				{
					// Initialize IMCNo
					getVehMiscData().setIMCNo(01);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						long llValue = Long.parseLong(asValue);
						if (llValue >= 10000000000l
							&& llValue <= 99999999999l)
						{
							getVehMiscData().setIMCNo(llValue);
						}
					}
					// end defect 7773
					break;
				}
			case LIEN1DATE : // LienHolder1Date
				{
					// Intialize Lien1Date
					//laTtlData.setLienHolder1Date(ZERO);
					laLienHldrData1.setLienDate(0);

					// defect 7773
					// Replace try/catch
					//	Validate data format
					if (MediaValidations.validateDtFormat(asValue))
					{
						//laTtlData.setLienHolder1Date(
						laLienHldrData1.setLienDate(
							Integer.parseInt(asValue));
					}
					// end defect 7773
					break;
				}
			case LIEN2DATE : // LienHolder2Date
				{
					// Intialize Lien2Date
					laLienHldrData2.setLienDate(0);

					// defect 7773
					// Replace try/catch 
					//	Validate data format
					if (MediaValidations.validateDtFormat(asValue))
					{
						//laTtlData.setLienHolder2Date(
						laLienHldrData2.setLienDate(
							Integer.parseInt(asValue));
					}

					// end defect 7773
					break;
				}
			case LIEN3DATE : // LienHolder3Date
				{
					// Intialize Lien3Date
					laLienHldrData3.setLienDate(ZERO);

					// defect 7773
					// Replace try/catch 
					//	Validate data format
					if (MediaValidations.validateDtFormat(asValue))
					{
						laLienHldrData3.setLienDate(
							Integer.parseInt(asValue));
					}

					// end defect 7773
					break;
				}
			case LIENHLDR1NAME1 : // LienHldrName1
				{
					laLienHldrData1.setName1(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}
			case LIENHLDR1NAME2 : // LienHldrName2
				{
					laLienHldrData1.setName2(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}
			case LIENHLDR2NAME1 : // LienHldrName1
				{
					laLienHldrData2.setName1(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}
			case LIENHLDR2NAME2 : //LienHldrname2
				{
					laLienHldrData2.setName2(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}
			case LIENHLDR3NAME1 : // LienHldrName1
				{
					laLienHldrData3.setName1(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}
			case LIENHLDR3NAME2 : // LienHldrName2
				{
					laLienHldrData3.setName2(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}

				// defect 9969 
			case LIENHLDR1CITY : // LienHldrCity
				{
					laLienAddressData1.setCity(
						MediaValidations.validateCity(asValue));
					break;
				}
			case LIENHLDR1CNTRY : // LienHldrCntry
				{
					laLienAddressData1.setCntry(
						MediaValidations.validateCountry(asValue));
					break;
				}
			case LIENHLDR1ST1 : // LienHldrSt1
				{
					laLienAddressData1.setSt1(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}
			case LIENHLDR1ST2 : // LienHldrSt2
				{
					laLienAddressData1.setSt2(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}
			case LIENHLDR1STATE : //LienHldrState
				{
					laLienAddressData1.setState(
						MediaValidations.validateState(asValue));
					break;
				}
			case LIENHLDR1ZPCD : // LienHldrZpCd
				{
					laLienAddressData1.setZpcd(
						MediaValidations.validateZp(asValue));
					break;
				}
			case LIENHLDR1ZPCDP4 : // LienHldrZpCdP4
				{
					laLienAddressData1.setZpcdp4(
						MediaValidations.validateZp4(asValue));
					break;
				}
			case LIENHLDR2CITY : // LienHldrCity
				{
					laLienAddressData2.setCity(
						MediaValidations.validateCity(asValue));
					break;
				}
			case LIENHLDR2CNTRY : // LienHldrCntry
				{
					laLienAddressData2.setCntry(
						MediaValidations.validateCountry(asValue));
					break;
				}
			case LIENHLDR2ST1 : // LienHldrSt1
				{
					laLienAddressData2.setSt1(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}
			case LIENHLDR2ST2 : // LienHldrSt2
				{
					laLienAddressData2.setSt2(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}
			case LIENHLDR2STATE : // LienHldrState
				{
					laLienAddressData2.setState(
						MediaValidations.validateState(asValue));
					break;
				}
			case LIENHLDR2ZPCD : // LienHldrZpCd
				{
					laLienAddressData2.setZpcd(
						MediaValidations.validateZp(asValue));
					break;
				}
			case LIENHLDR2ZPCDP4 : // LienHldrZpCdP4
				{
					laLienAddressData2.setZpcdp4(
						MediaValidations.validateZp4(asValue));
					break;
				}
			case LIENHLDR3CITY : // LienHldrCity
				{
					laLienAddressData3.setCity(
						MediaValidations.validateCity(asValue));
					break;
				}
			case LIENHLDR3CNTRY : // LienHldrCntry
				{
					laLienAddressData3.setCntry(
						MediaValidations.validateCountry(asValue));
					break;
				}
			case LIENHLDR3ST1 : // LienHldrSt1
				{
					laLienAddressData3.setSt1(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}
			case LIENHLDR3ST2 : // LienHldrSt2
				{
					laLienAddressData3.setSt2(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}
			case LIENHLDR3STATE : // LienHldrState
				{
					laLienAddressData3.setState(
						MediaValidations.validateState(asValue));
					break;
				}
			case LIENHLDR3ZPCD : // LienHldrZpCd
				{
					laLienAddressData3.setZpcd(
						MediaValidations.validateZp(asValue));
					break;
				}
			case LIENHLDR3ZPCDP4 : // LienHldrZpCdP4
				{
					laLienAddressData3.setZpcdp4(
						MediaValidations.validateZp4(asValue));
					break;
				}
				// end defect 9969 
			case NEWPLTNO : // NewPltNo
				{
					if (asValue.length() <= 7)
					{
						setNewPltNo(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"NewPltNo",
							lsSaveValue,
							EMPTY);
						// end defect 10290

						// defect 6844
						//	Replace DEFAULT with EMPTY
						setNewPltNo(EMPTY);
						// end defect 6844
					}
					break;
				}
			case NEWREGEXPMO : // NewRegExpMo
				{
					// defect 7773
					// Replace try/catch 
					setNewRegExpMo(
						Integer.parseInt(
							MediaValidations.validateMonth(asValue)));
					// end defect 7773
					break;
				}
			case NEWREGEXPYR : // NewRegExpYr
				{
					// defect 7773
					// 	Remove try/catch 
					//	If validation fails, 0 will be returned
					// defect 8260
					//	Use DlrTransDate month/Year to validate year
					//	in MediaValidations.validateYear().
					setNewRegExpYr(
						Integer.parseInt(
							MediaValidations.validateYear(
								asValue,
								ciYear,
								ciMonth)));
					// end defect 8260
					// end defect 7773
					break;
				}
			case LEMONLAWINDI : // LemonLawIndi (Manufacturer Buyback)
				{
					// Save original value
					// defect 10290 
					// setLemonLawIndi(asValue);
					// end defect 10290 
					// Assign Indi 
					laTtlData.setLemonLawIndi(
						assignLemonLawIndi(asValue));
					break;
				}
				// end defect 10232
			case OFFHWYUSEINDI : // OffHghwyIndi
				{
					setOffHghwyIndi(
						MediaValidations.validateIndi(asValue));
					break;
				}
			case OWNRCITY : // OwnrCity
				{
					asValue = MediaValidations.validateCity(asValue);
					if (asValue.length() < 1)
					{
						// defect 10290 
						setInvalidRecord(
							"OwnrCity",
							lsSaveValue,
							EMPTY);
						// end defect 10290
					}
					laOwnrData.getAddressData().setCity(asValue);
					break;
				}
			case OWNRCNTRY : // OwnrCntry
				laOwnrData.getAddressData().setCntry(
					MediaValidations.validateCountry(asValue));
				break;

			case OWNRID : // OwnrId
				{
					laOwnrData.setOwnrId(EMPTY);

					// defect 10130 
					// if (asValue.length() == 9)
					//	{
					//		laOwnrData.setOwnrId(asValue);
					//	}
					//	else
					//	{
					//		laOwnrData.setOwnrId(EMPTY);
					//	}
					// end defect 10130 
					break;
				}
			case OWNRSHPEVIDCD : // OwnrShpEvidCd
				{
					// defect 11051
					int liOwnrEvid = -1; 
					
					try
					{
						liOwnrEvid = Integer.parseInt(asValue); 
					}
					catch (NumberFormatException aeNFEx)
					{
						
					}
					if (OwnershipEvidenceCodesCache.getOwnrEvidCd(liOwnrEvid) == null)
					{
						liOwnrEvid = -1; 
					}
					laTtlData.setOwnrShpEvidCd(liOwnrEvid);

//					// defect 7773
//					// Replace try/catch
//					//	(-1 means no decimal point)
//					if (UtilityMethods.isNumeric(asValue)
//						&& asValue.indexOf(CommonConstant.STR_PERIOD)
//							== -1)
//					{
//						int liIndi = Integer.parseInt(asValue);
//						if (liIndi >= 0 && liIndi <= 23)
//						{
//							laTtlData.setOwnrShpEvidCd(
//								Integer.parseInt(asValue));
//						}
//					}
					// end defect 7773
					// end defect 11051 
					break;
				}
			case OWNRST1 : // OwnrSt1
				{
					asValue =
						MediaValidations.validateNameOrSt(asValue);
					if (asValue.length() < 1)
					{
						// defect 10290 
						setInvalidRecord("OwnrSt1", lsSaveValue, EMPTY);
						// end defect 10290
					}
					laOwnrData.getAddressData().setSt1(asValue);
					break;
				}
			case OWNRST2 : // OwnrSt2
				{
					asValue =
						MediaValidations.validateNameOrSt(asValue);
					laOwnrData.getAddressData().setSt2(asValue);
					break;
				}
			case OWNRSTATE : // OwnrState
				{
					asValue = MediaValidations.validateState(asValue);
					laOwnrData.getAddressData().setState(asValue);
					break;
				}
			case OWNRTTLNAME1 : // OwnrTtlName1
				{
					asValue =
						MediaValidations.validateNameOrSt(asValue);
					if (asValue.length() < 1)
					{
						// defect 10290 
						setInvalidRecord(
							"OwnrTtlName",
							lsSaveValue,
							EMPTY);
						// end defect 10290
					}
					laOwnrData.setName1(asValue);
					break;
				}
			case OWNRTTLNAME2 : // OwnrTtlName2
				{
					laOwnrData.setName2(
						MediaValidations.validateNameOrSt(asValue));
					break;
				}
			case OWNRZPCD : // OwnrZpCd
				{
					laOwnrData.getAddressData().setZpcd(
						MediaValidations.validateZp(asValue));
					break;
				}
			case OWNRZPCDP4 : // OwnrZpCdP4
				{
					laOwnrData.getAddressData().setZpcdp4(
						MediaValidations.validateZp4(asValue));
					break;
				}
			case PREVOWNRCITY : // PrevOwnrCity
				{
					asValue = MediaValidations.validateCity(asValue);
					if (asValue.length() < 1)
					{
						// defect 10290 
						setInvalidRecord(
							"PrevOwnrCity",
							lsSaveValue,
							EMPTY);
						// end defect 10290
					}
					laTtlData.setPrevOwnrCity(asValue);
					break;
				}
			case PREVOWNRNAME : // PrevOwnrName
				{
					if (asValue.length() <= 24)
					{
						laTtlData.setPrevOwnrName(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"PrevOwnrName",
							lsSaveValue,
							EMPTY);
						// end defect 10290

						// defect 6844
						//	Replace DEFAULT with EMPTY
						laTtlData.setPrevOwnrName(EMPTY);
						// end defect 6844
					}
					break;
				}
			case PREVOWNRSTATE : // PrevOwnrState
				{
					laTtlData.setPrevOwnrState(
						MediaValidations.validateState(asValue));
					break;
				}
			case PRMTREQRDINDI : // PrmtReqrdIndi
				{
					laVehData.setPrmtReqrdIndi(
						MediaValidations.validateIndi(asValue));
					break;
				}
			case RECONDINDI : // RecondCd
				{
					laVehData.setRecondCd(
						CommonConstant.STR_SPACE_EMPTY
							+ MediaValidations.validateIndi(asValue));
					break;
				}
			case RECONTINDI : // ReContIndi
				{
					laVehData.setReContIndi(
						MediaValidations.validateIndi(asValue));
					break;
				}
			case RECPNTNAME : // RecpntName
				{
					asValue =
						MediaValidations.validateNameOrSt(asValue);
					// defect 7350
					// Invalid Record indi not set in v5.1.6
					//if (asValue.length() < 1)
					//	setInvalidRecord(true);
					// end defect 7350
					laRegData.setRecpntName(asValue);
					break;
				}
			case REGCLASSCD : // RegClassCd
				{
					// Initialize variable
					laRegData.setRegClassCd(0);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						int liIndi = Integer.parseInt(asValue);
						if (liIndi >= 7 && liIndi <= 61)
						{
							laRegData.setRegClassCd(liIndi);
						}
					}
					// end defect 7773
					break;
				}
			case REGEXPMO : // RegExpMo
				{
					// Initialize variable
					laRegData.setRegExpMo(0);

					// defect 7773
					// Replace try/catch 
					laRegData.setRegExpMo(
						Integer.parseInt(
							MediaValidations.validateMonth(asValue)));
					// end defect 7773
					break;
				}
			case REGEXPYR : // RegExpYr
				{
					// Initialize variable
					laRegData.setRegExpYr(0);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						int liValue = Integer.parseInt(asValue);
						// if RegExpYr is not within range, set to 0
						if (liValue >= 1950 && liValue <= ciYear + 2)
						{
							laRegData.setRegExpYr(liValue);
						}
					}
					// end defect 7773
					break;
				}
			case REGPLTCD : // RegPltCd
				{
					if (asValue.length() <= 8)
					{
						laRegData.setRegPltCd(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"RegPltCd",
							lsSaveValue,
							EMPTY);
						// end defect 10290 

						// defect 6844
						//	Replace DEFAULT with EMPTY
						laRegData.setRegPltCd(EMPTY);
						// end defect 6844
					}
					break;
				}
			case REGPLTNO : // RegPltNo
				{
					// defect 9973
					//	Use only to search mainframe if VIN does not 
					//	exist.
					if (asValue.length() <= 7)
					{
						laRegData.setRegPltNo(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"RegPltNo",
							lsSaveValue,
							EMPTY);

						laRegData.setRegPltNo(EMPTY);
						// end defect 10290
					}
					// end defect 9973
					break;
				}
			case REGSTKRCD : // RegStrkCd
				{
					if (asValue.equals(US) || asValue.equals(WS))
					{
						laRegData.setRegStkrCd(asValue);
					}
					else
					{
						// defect 6844
						//	Replace DEFAULT with EMPTY
						laRegData.setRegStkrCd(EMPTY);
						// end defect 648
					}
					break;
				}
			case REGSTKRNO : // RegStkrNo
				{
					if (asValue.length() <= 11)
					{
						laRegData.setRegStkrNo(asValue);
					}
					else
					{
						// defect 6844
						//	Replace DEFAULT with EMPTY
						laRegData.setRegStkrNo(EMPTY);
						// end defect 6844
					}
					break;
				}
			case RENWLMAILNGCITY : // RenwlMailAddrCity
				{
					asValue = MediaValidations.validateCity(asValue);
					// defect 7350
					// Invalid Record indi not set in v5.1.6
					//if (asValue.length() < 1)
					//{
					//	laRegData.getRenwlMailAddr().setCity(EMPTY);
					//	setInvalidRecord(true);
					//}
					//else
					//{
					//	laRegData.getRenwlMailAddr().setCity(asValue);
					//}
					laRegData.getRenwlMailAddr().setCity(asValue);
					// end defect 7350
					break;
				}
			case RENWLMAILNGST1 : // RenwlMailAddrSt1
				{
					asValue =
						MediaValidations.validateNameOrSt(asValue);
					// defect 7350
					// Invalid Record indi not set in v5.1.6
					//if (asValue.length() < 1)
					//{
					//	laRegData.getRenwlMailAddr().setSt1(EMPTY);
					//	setInvalidRecord(true);
					//}
					//else
					//{
					//	laRegData.getRenwlMailAddr().setSt1(asValue);
					//}
					laRegData.getRenwlMailAddr().setSt1(asValue);
					// end defect 7350
					break;
				}
			case RENWLMAILNGST2 : // RenwlMailAddrSt2
				{
					asValue =
						MediaValidations.validateNameOrSt(asValue);
					// defect 7350
					// Invalid Record indi not set in v5.1.6
					//if (asValue.length() < 1)
					//{
					//	laRegData.getRenwlMailAddr().setSt2(EMPTY);
					//	setInvalidRecord(true);
					//}
					//else
					//{
					//	laRegData.getRenwlMailAddr().setSt2(asValue);
					//}
					laRegData.getRenwlMailAddr().setSt2(asValue);
					break;
				}
			case RENWLMAILNGSTATE : // RenwlMailAddrState
				{
					laRegData.getRenwlMailAddr().setState(
						MediaValidations.validateState(asValue));
					break;
				}
			case RENWLMAILNGZPCD : // RenwlMailAddrZpCd
				{
					laRegData.getRenwlMailAddr().setZpcd(
						MediaValidations.validateZp(asValue));
					break;
				}
			case RENWLMAILNGZPCDP4 : // RenwlMailAddrZpCdP4
				{
					laRegData.getRenwlMailAddr().setZpcdp4(
						MediaValidations.validateZp4(asValue));
					break;
				}
			case REPLICAVEHMK : // ReplicaVehMk
				{
					if (asValue.length() <= 4)
					{
						laVehData.setReplicaVehMk(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"ReplicaVehMk",
							lsSaveValue,
							EMPTY);
						// end defect 10290

						// defect 6844
						//	Replace DEFAULT with EMPTY
						laVehData.setReplicaVehMk(EMPTY);
						// end defect 6844
					}
					break;
				}
			case REPLICAVEHMODLYR : // ReplicaVehModlYr
				{
					// Initialize variable
					laVehData.setReplicaVehModlYr(0);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						int liIndi = Integer.parseInt(asValue);
						int iCurrYr =
							RTSDate.getCurrentDate().getYear();
						if (liIndi >= 1880 && liIndi <= iCurrYr + 2)
						{
							laVehData.setReplicaVehModlYr(liIndi);
						}
					}
					// end defect 7773
					break;
				}
			case RESCOMPTCNTYNO : // ResComptCntyNo
				{
					// defect 7773
					// 	Remove try/catch 
					// Use getNewRegExpMo() for validation
					//	If validation fails, 0 will be returned
					laRegData.setResComptCntyNo(
						Integer.parseInt(
							MediaValidations.validateCntyNo(asValue)));
					break;
				}
			case SALESTAXCAT : // SalesTaxCat
				{
					if (asValue.length() <= 10)
					{
						getVehMiscData().setSalesTaxCat(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"SalesTaxCat",
							lsSaveValue,
							EMPTY);
						// end defect 10290

						// defect 6844
						//	Replace DEFAULT with EMPTY
						getVehMiscData().setSalesTaxCat(EMPTY);
						// end defect 6844
					}
					break;
				}
			case SALESTAXDATE : // SalesTaxDate
				{
					// Unitialize variable
					getVehMiscData().setSalesTaxDate(0);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						int liIndi = Integer.parseInt(asValue);
						if (liIndi >= 19410501
							&& MediaValidations.validateDtFormat(
								CommonConstant.STR_SPACE_EMPTY
									+ liIndi))
						{
							getVehMiscData().setSalesTaxDate(liIndi);
						}
					}
					// end defect 7773
					break;
				}
			case SALESTAXEXMPTCD : // SalesTaxExmptCd
				{
					// Initialize variable
					getVehMiscData().setSalesTaxExmptCd(0);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						int liIndi = Integer.parseInt(asValue);
						if (liIndi >= 0 && liIndi <= 16)
						{
							getVehMiscData().setSalesTaxExmptCd(liIndi);
						}
					}
					// defect 7773
					break;
				}
			case SALESTAXPNTLYPRCNT : // SalesTaxPnltyPer
				{
					// Initialize variable 
					getVehMiscData().setSalesTaxPnltyPer(0);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						int liIndi = Integer.parseInt(asValue);
						if (liIndi == 0 || liIndi == 5 || liIndi == 10)
						{
							getVehMiscData().setSalesTaxPnltyPer(
								liIndi);
						}
					}
					// end defect 7773
					break;
				}
			case SURVSHPRIGHTSINDI : // SurvshpRightsIndi
				{
					laTtlData.setSurvshpRightsIndi(
						MediaValidations.validateIndi(asValue));
					
					// defect 10827 
					if (laTtlData.getSurvshpRightsIndi() == 1 && 
							Integer.parseInt(getTransDate()) >= 
								SystemProperty.getDTARejectSurvivorDate())
					{
						setInvalidRecord(
								"SurvshpRightsIndi",
								lsSaveValue,
								"1");
					}
					
					// end defect 10827 
					break;
				}
			case TAXPDOTHRSTATE : // TaxPdOthrState
				{
					// Initialize variable
					getVehMiscData().setTaxPdOthrState(
						new Dollar(CommonConstant.STR_ZERO_DOLLAR));

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					if (UtilityMethods.isNumeric(asValue))
					{
						if (Float.parseFloat(asValue) <= 999999.99)
						{
							getVehMiscData().setTaxPdOthrState(
								new Dollar(asValue));
						}
					}
					// end defect 7773
					break;
				}
			case TRADEINVEHMK : // TradeInVehMk
				{
					if (asValue.length() <= 4)
					{
						getVehMiscData().setTradeInVehMk(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"TradeInVehMk",
							lsSaveValue,
							EMPTY);
						// end defect 10290

						// defect 6844
						//	Replace DEFAULT with EMPTY
						getVehMiscData().setTradeInVehMk(EMPTY);
						// end defect 6844
					}
					break;
				}
			case TRADEINVEHYR : // TradeInVehYr
				{
					// Initialize variable
					getVehMiscData().setTradeInVehYr(0);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						int liIndi = Integer.parseInt(asValue);
						int iCurrYr =
							RTSDate.getCurrentDate().getYear();
						if (liIndi >= 1880 && liIndi <= iCurrYr + 2)
						{
							getVehMiscData().setTradeInVehYr(liIndi);
						}
					}
					// end defect 7773
					break;
				}
			case TRADEINVIN : // TradeInVIN
				{
					if (asValue.length() <= 22)
					{
						getVehMiscData().setTradeInVehVIN(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"TradeInVIN",
							lsSaveValue,
							EMPTY);

						//getVehMiscData().setTradeInVehVIN(asValue);
						getVehMiscData().setTradeInVehVIN(EMPTY);
					}
					break;
				}
			case TRLRTYPE : // TrlrType
				{
					if (asValue.equals(F) || asValue.equals(S))
					{
						laVehData.setTrlrType(asValue);
					}
					else
					{
						// dfect 6844
						//	Replace DEFAULT with EMPTY
						laVehData.setTrlrType(EMPTY);
						// end defect 6844
					}
					break;
				}
			case TTLVEHLOCCITY : // TtlVehAddrCIty
				{
					asValue = MediaValidations.validateCity(asValue);
					// defect 7350
					// Invalid Record indi not set in v5.1.6
					//if (asValue.length() < 1)
					//{
					//	setInvalidRecord(true);
					//}
					// end defect 7350
					laTtlData.getTtlVehAddr().setCity(asValue);
					break;
				}
			case TTLVEHLOCST1 : // TtlVehAddrSt1
				{
					asValue =
						MediaValidations.validateNameOrSt(asValue);
					// defect 7350
					// Invalid Record indi not set in v5.1.6
					//if (asValue.length() < 1)
					//{
					//	setInvalidRecord(true);
					//}
					// end defect 7350
					laTtlData.getTtlVehAddr().setSt1(asValue);
					break;
				}
			case TTLVEHLOCST2 : // TtlVehAddrSt2
				{
					asValue =
						MediaValidations.validateNameOrSt(asValue);
					// defect 7350
					// Invalid Record indi not set in v5.1.6
					//if (asValue.length() < 1)
					//{
					//	setInvalidRecord(true);
					//}
					// end defect 7350
					laTtlData.getTtlVehAddr().setSt2(asValue);
					break;
				}
			case TTLVEHLOCSTATE : // TtlVehAddrState
				{
					laTtlData.getTtlVehAddr().setState(
						MediaValidations.validateState(asValue));
					break;
				}
			case TTLVEHLOCZPCD : // TtlVehAddrZpCd
				{
					laTtlData.getTtlVehAddr().setZpcd(
						MediaValidations.validateZp(asValue));
					break;
				}
			case TTLVEHLOCZPCDP4 : // TtlVehAddrZpCdP4
				{
					laTtlData.getTtlVehAddr().setZpcdp4(
						MediaValidations.validateZp4(asValue));
					break;
				}
			case VEHBDYTYPE : // VehBdyType
				{
					if (asValue.length() == 2)
					{
						laVehData.setVehBdyType(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"VehBdyType",
							lsSaveValue,
							EMPTY);
						// end defect 10290

						// defect 6844
						//	Replace DEFAULT with EMPTY
						laVehData.setVehBdyType(EMPTY);
						// end defect 6844

					}
					break;
				}
			case VEHBDYVIN : // VehBdyVIN
				{
					if (asValue.length() <= 22)
					{
						laVehData.setVehBdyVin(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"VehBdyVIN",
							lsSaveValue,
							EMPTY);
						// end defect 10290

						// defect 6844
						//	Replace DEFAULT with EMPTY
						laVehData.setVehBdyVin(EMPTY);
						// end defect 6844
					}
					break;
				}
			case VEHCARYNGCAP : // VehCaryngCap
				{
					//Initialize variable
					laRegData.setVehCaryngCap(0);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						int liIndi = Integer.parseInt(asValue);
						if (liIndi >= 0 && liIndi <= 80000)
						{
							laRegData.setVehCaryngCap(liIndi);
						}
					}
					// end defect 7773
					break;
				}
			case VEHCLASSCD : // VehClassCd
				{
					if (asValue.length() <= 8)
					{
						laVehData.setVehClassCd(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"VehClassCd",
							lsSaveValue,
							EMPTY);
						// end defect 10290

						// defect 6844
						//	Replace DEFAULT with EMPTY
						laVehData.setVehClassCd(EMPTY);
						// end defect 6844
					}
					break;
				}
			case VEHEMPTYWT : // VehEmptyWt
				{
					//Initialize variable
					laVehData.setVehEmptyWt(0);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						int liIndi = Integer.parseInt(asValue);
						if (liIndi >= 0 && liIndi <= 80000)
						{
							laVehData.setVehEmptyWt(liIndi);
						}
					}
					// end defect 7773
					break;
				}
			case VEHLNGTH : // VehLngth
				{
					//Initialize variable
					laVehData.setVehLngth(0);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						int liVehLngth = Integer.parseInt(asValue);
						// defect 8752
						//	Correct conditional statement
						// VehLngth is from 1 to 99 feet
						//		if (liIndi >= 0 && liIndi <= 80000)
						if (liVehLngth >= 1 && liVehLngth <= 99)
						{
							laVehData.setVehLngth(liVehLngth);
						}
						// end defect 8752
					}
					// end defect 7773
					break;
				}
			case VEHMK : // VehMk
				{
					if (asValue.length() <= 4)
					{
						laVehData.setVehMk(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord("VehMk", lsSaveValue, EMPTY);
						// end defect 10290

						// defect 6844
						//	Replace DEFAULT with EMPTY
						laVehData.setVehMk(EMPTY);
						// end defect 6844
					}
					break;
				}
			case VEHMODL : // VehModl
				{
					if (asValue.length() <= 3)
					{
						laVehData.setVehModl(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"VehModl",
							lsSaveValue,
							asValue);
						// end defect 10290

						laVehData.setVehModl(asValue);
					}
					break;
				}
			case VEHMODLYR : // VehModlYr
				{
					//Initialize variable
					laVehData.setVehModlYr(0);

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					//	and check for a decimal point 
					//	(-1 means no decimal point)
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						int liIndi = Integer.parseInt(asValue);
						RTSDate laCurrDate = RTSDate.getCurrentDate();
						int iCurrYr = laCurrDate.getYear();
						if (liIndi >= 1880 && liIndi <= iCurrYr + 2)
						{
							laVehData.setVehModlYr(liIndi);
						}
					}
					// end defect 7773
					break;
				}
			case VEHODMTRBRND : // VehOdmtrBrnd
				{
					if (asValue.equals(A)
						|| asValue.equals(X)
						|| asValue.equals(N))
					{
						laVehData.setVehOdmtrBrnd(asValue);
					}
					else
					{
						// defect 6844
						//	Replace DEFAULT with EMPTY
						laVehData.setVehOdmtrBrnd(EMPTY);
						// end defect 6844
					}
					break;
				}
			case VEHODMTRREADNG : // VehOdmReadng
				{
					// defect 6844
					//	Replace DEFAULT with EMPTY
					// Initialize variable
					laVehData.setVehOdmtrReadng(EMPTY);
					// end defect 6844

					if (!asValue
						.trim()
						.equals(CommonConstant.STR_SPACE_EMPTY))
					{ // defect 4664
						// Convert 'exempt' to 'EXEMPT'
						if (asValue.toUpperCase().equals(EXEMPT))
							//if (asValue.equals(EMPTY))
							// end defect 4664
						{
							laVehData.setVehOdmtrReadng(asValue);
						}
						else
						{
							// defect 7773
							// Replace try/catch with 
							//	UtilityMethods.isNumeric()
							//	and check for a decimal point 
							//	(-1 means no decimal point)
							if (UtilityMethods.isNumeric(asValue)
								&& asValue.indexOf(
									CommonConstant.STR_PERIOD)
									== -1)
							{
								int liOd =
									Integer.parseInt(asValue.trim());
								if (liOd <= 999999)
								{
									laVehData.setVehOdmtrReadng(
										asValue);
								}
							}
						}
					}
					// end defect 7773
					break;
				}
			case VEHSALESPRICE : // VehSalesPrice
				{
					// Initialize Fee
					laTtlData.setVehSalesPrice(
						new Dollar(CommonConstant.STR_ZERO_DOLLAR));

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					if (UtilityMethods.isNumeric(asValue))
					{
						if (Float.parseFloat(asValue) <= 999999.99)
						{
							laTtlData.setVehSalesPrice(
								new Dollar(asValue));
						}
					}
					// end defect 7773
					break;
				}
			case VEHTON : // VehTon
				{
					// Initialize variable
					laVehData.setVehTon(
						new Dollar(CommonConstant.STR_ZERO_DOLLAR));

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					if (UtilityMethods.isNumeric(asValue))
					{
						if (Float.parseFloat(asValue) >= 0.25
							&& Float.parseFloat(asValue) <= 25.00)
						{
							laVehData.setVehTon(new Dollar(asValue));
						}
					}
					// end defect 7773
					break;
				}
			case VEHTRADEINALLOWANCE : // VehTradeinAllownce
				{
					// Initialize variable
					laTtlData.setVehTradeinAllownce(
						new Dollar(CommonConstant.STR_ZERO_DOLLAR));

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					if (UtilityMethods.isNumeric(asValue))
					{
						if (Float.parseFloat(asValue) <= 999999.99)
						{
							laTtlData.setVehTradeinAllownce(
								new Dollar(asValue));
						}
					}
					// end defect 7773
					break;
				}
			case VEHUNITNO : // VehUnitNo
				{
					if (asValue.length() <= 6)
					{
						laTtlData.setVehUnitNo(asValue);
					}
					else
					{
						// defect 10290 
						setInvalidRecord(
							"VehUnitNo",
							lsSaveValue,
							EMPTY);
						// end defect 10290

						// defect 6844
						//	Replace DEFAULT with EMPTY
						laTtlData.setVehUnitNo(EMPTY);
						// end defect 6844
					}
					break;
				}
			case VIN : // Vin
				{
					if (asValue.length() <= 22)
					{
						laVehData.setVin(asValue.toUpperCase());
					}
					else
					{
						// defect 10290 
						setInvalidRecord("Vin", lsSaveValue, EMPTY);
						// end defect 10290

						// defect 6844
						//	Replace DEFAULT with EMPTY
						laVehData.setVin(EMPTY);
						// end defect 6844
					}
					break;
				}
			case ZTOTALREBATEAMT : // RebateAmt
				{
					// Initialize variable
					setRebateAmt(
						new Dollar(CommonConstant.STR_ZERO_DOLLAR));

					// defect 7773
					// Replace try/catch with UtilityMethods.isNumeric()
					if (UtilityMethods.isNumeric(asValue))
					{
						if (Float.parseFloat(asValue) <= 999999.99)
						{
							setRebateAmt(new Dollar(asValue));
						}
					}
					// end defect 7773
					break;
				}
				// defect 9973
				// Define new base format (118 to 120) fields.
				//	RSPS data will be appended at 121-128 or 125-132.
				//	119 and 120 have been move to the new base file format
				//	Add CUSTSUPPLYINDI and CUSTSUPPLYPLTAGE to the
				//	base format
			case CUSTSUPPLYINDI :
				{
					setCustSuppliedPltIndi(
						MediaValidations.validateIndi(asValue));
					break;
				}
			case CUSTSUPPLYPLTAGE :
				{
					int liPltAge = DEFAULT_CUST_PLT_AGE;
					// TEST: If CustSuppliedPltAge is 0 or >= 7 then
					//	set CustSuppliedPltAge = 3
					//	(refer to T:\RTS II Team\DEV\Build\
					//		Build Documents\DTADTA Change Document)
					try
					{
						liPltAge = Integer.parseInt(asValue);
						if (liPltAge < 0
							|| liPltAge >= MAX_CUST_PLT_AGE)
						{
							liPltAge = DEFAULT_CUST_PLT_AGE;
						}
					}
					catch (NumberFormatException aeNFE)
					{
						liPltAge = DEFAULT_CUST_PLT_AGE;
					}
					setCustSuppliedPltAge(liPltAge);
					break;
				}
				// defect 10509
			case PERMLIENHLDR1 :
				{
					laTtlData.setPermLienHldrId1(asValue);
				}
				break;
			case PERMLIENHLDR2 :
				{
					laTtlData.setPermLienHldrId2(asValue);
				}
				break;
			case PERMLIENHLDR3 :
				{
					laTtlData.setPermLienHldrId3(asValue);
				}
				break;
			case ETTLRQST :
				{
					setETtlRqst(MediaValidations.validateIndi(asValue));

					if (asValue.equals("1"))
					{
						laTtlData.setETtlCd(
							TitleConstant.ELECTRONIC_ETTLCD);
					}
					break;
				}
				// end defect 10509
				// defect 10709
			case EMAILRENWLREQCD :
				{
					laRegData.setEMailRenwlReqCd(0);
					if (UtilityMethods.isNumeric(asValue)
						&& asValue.indexOf(CommonConstant.STR_PERIOD)
							== -1)
					{
						laRegData.setEMailRenwlReqCd(
							Integer.parseInt(asValue));
					}
					break;
				}
			case RECPNTEMAIL :
				{
					laRegData.setRecpntEMail(asValue);
				}
				break;
			case FIELD_INDEX_127 :
				{
					if (aiRecType == RSPS_FRMT_CD)
					{
						setRSPSPrntInvQty(
							MediaValidations.validateIndi(asValue));
					}
					else if (
						aiRecType == VEHCOLOR_FRMT_CD
							|| aiRecType == RSPS_VEHCOLOR_FRMT_CD)
					{
						laVehData.setVehMjrColorCd(asValue);
					}
					break;
				}
			case FIELD_INDEX_128 :
				{
					if (aiRecType == RSPS_FRMT_CD)
					{
						setPosPrntInvQty(
							MediaValidations.validateIndi(asValue));
					}
					else if (
						aiRecType == VEHCOLOR_FRMT_CD
							|| aiRecType == RSPS_VEHCOLOR_FRMT_CD)
					{
						laVehData.setVehMnrColorCd(asValue);
					}
					break;
				}
				// defect 10752 
			case FIELD_INDEX_129 :
				{
					if (aiRecType == RSPS_FRMT_CD)
					{
						setPOSProcsIndi(
							(1
								== MediaValidations.validateIndi(
									asValue)));
					}
					else if (
						aiRecType == VEHCOLOR_FRMT_CD
							|| aiRecType == RSPS_VEHCOLOR_FRMT_CD)
					{
						// setRSPSPrntInvQty(
						setMilitaryIndi(
							MediaValidations.validateIndi(asValue));
					}
					break;
				}
			case FIELD_INDEX_130 :
				{
					if (aiRecType == RSPS_FRMT_CD)
					{
						setVoided(
							MediaValidations.validateIndi(asValue));
					}
					else if (
						aiRecType == VEHCOLOR_FRMT_CD
							|| aiRecType == RSPS_VEHCOLOR_FRMT_CD)
					{
						// setPosPrntInvQty(
						setRSPSPrntInvQty(
							MediaValidations.validateIndi(asValue));
					}
					break;
				}
				// end defect 7745
			case FIELD_INDEX_131 :
				{
					if (aiRecType == RSPS_FRMT_CD)
					{
						setUpdtPOSRSPSPrntIndi(
							(1
								== MediaValidations.validateIndi(
									asValue)));
					}
					else if (
						aiRecType == VEHCOLOR_FRMT_CD
							|| aiRecType == RSPS_VEHCOLOR_FRMT_CD)
					{

						//setUpdtPOSRSPSPrntIndi((1
						//	== MediaValidations.validateIndi(
						//	asValue)));
						setPosPrntInvQty(
							MediaValidations.validateIndi(asValue));
					}
					break;
				}
			case FIELD_INDEX_132 :
				{
					if (aiRecType == RSPS_FRMT_CD)
					{
						setRSPSDiskSeqNo(
							MediaValidations.validateIndi(asValue));
					}
					else if (
						aiRecType == VEHCOLOR_FRMT_CD
							|| aiRecType == RSPS_VEHCOLOR_FRMT_CD)
					{
						setPOSProcsIndi(
							(1
								== MediaValidations.validateIndi(
									asValue)));
						// setVoided(
						//	MediaValidations.validateIndi(asValue));
					}
					break;
				}
			case FIELD_INDEX_133 :
				{
					if (aiRecType == RSPS_FRMT_CD)
					{
						setRSPSId(asValue);
					}
					else if (
						aiRecType == VEHCOLOR_FRMT_CD
							|| aiRecType == RSPS_VEHCOLOR_FRMT_CD)
					{
						setVoided(
							MediaValidations.validateIndi(asValue));
						// setUpdtPOSRSPSPrntIndi(
						//	(1
						//		== MediaValidations.validateIndi(
						//		asValue)));
					}
					break;
				}
			case FIELD_INDEX_134 :
				{
					if (aiRecType == RSPS_FRMT_CD)
					{
						setRSPSOrigPrntDate(asValue);
					}
					else if (
						aiRecType == VEHCOLOR_FRMT_CD
							|| aiRecType == RSPS_VEHCOLOR_FRMT_CD)
					{
						setUpdtPOSRSPSPrntIndi(
							(1
								== MediaValidations.validateIndi(
									asValue)));
						// setRSPSDiskSeqNo(
						//	MediaValidations.validateIndi(asValue));
					}
					break;
				}
				// end defect 9656
				// and defect 7770
				// end defect 9973
			case FIELD_INDEX_135 :
				{
					if (aiRecType == RSPS_VEHCOLOR_FRMT_CD)
					{
						//setRSPSId(asValue);
						setRSPSDiskSeqNo(
							MediaValidations.validateIndi(asValue));
					}
					break;
				}
			case FIELD_INDEX_136 :
				{
					if (aiRecType == RSPS_VEHCOLOR_FRMT_CD)
					{
						// setRSPSOrigPrntDate(asValue);
						setRSPSId(asValue);
					}
					break;
				}
				// end defect 10709
			case FIELD_INDEX_137 :
				{
					if (aiRecType == RSPS_VEHCOLOR_FRMT_CD)
					{
						setRSPSOrigPrntDate(asValue);
					}
					break;
				}
				// end defect 10752 
		}
	}

	/**
	 * Sets the value of Form31No
	 * 
	 * @param asForm31No String 
	 */
	public void setForm31No(String asForm31No)
	{
		csForm31No = asForm31No;
	}

	/**
	 * Sets the value of InvalidRecord
	 * 
	 * @param abInvalidRecord boolean 
	 */
	public void setInvalidRecord(
		String asFieldName,
		String asFieldValue,
		String asResetFieldValue)
	{
		cbInvalidRecord = true;
		DealerTitleInvalidRecordData laDlrTtlDataInvRcd =
			new DealerTitleInvalidRecordData(
				asFieldName,
				asFieldValue,
				asResetFieldValue);

		cvDTAInvalidRecordData.add(laDlrTtlDataInvRcd);
	}

	/**
	 * Sets the value of InventoryData
	 * 
	 * @param avInvData Vector
	 */
	public void setInventoryData(Vector avInvData)
	{
		cvInventoryData = avInvData;
	}

	/**
	 * Sets the value of KeyBoardEntry
	 * 
	 * @param abKeyBoardEntry boolean
	 */
	public void setKeyBoardEntry(boolean abKeyBoardEntry)
	{
		cbKeyBoardEntry = abKeyBoardEntry;
	}

	//	/**
	//	 * Sets the value of LemonLawIndi
	//	 * 
	//	 * @param asLemonLawIndi String
	//	 */
	//	public void setLemonLawIndi(String asLemonLawIndi)
	//	{
	//		csLemonLawIndi = asLemonLawIndi;
	//	}

	/**
	 * Set value of  LienError
	 * 
	 * @param avLienError
	 */
	public void setLienError(Vector avLienError)
	{
		cvLienError = avLienError;
	}

	/**
	 * Sets the value of MFVehicleData
	 * 
	 * @param aaMFVehicleData MFVehicleData
	 */
	public void setMFVehicleData(MFVehicleData aaMFVehicleData)
	{
		caMFVehicleData = aaMFVehicleData;
	}

	/**
	 * Sets the value of MFVehicleDataFromMF
	 * 
	 * @param aaMFVehicleDataFromMF MFVehicleData
	 */
	public void setMFVehicleDataFromMF(MFVehicleData aaMFVehicleDataFromMF)
	{
		caMFVehicleDataFromMF = aaMFVehicleDataFromMF;
	}

	/**
	 * Set value of MilitaryIndi
	 * 
	 * @param aiMilitaryIndi
	 */
	public void setMilitaryIndi(int aiMilitaryIndi)
	{
		ciMilitaryIndi = aiMilitaryIndi;
	}

	/**
	 * Sets the value of NewPltNo
	 * 
	 * @param asNewPltNo String
	 */
	public void setNewPltNo(String asNewPltNo)
	{
		csNewPltNo = asNewPltNo;
	}

	/**
	 * Sets the value of NewRegExpMo
	 * 
	 * @param aiNewRegExpMo int
	 */
	public void setNewRegExpMo(int aiNewRegExpMo)
	{
		ciNewRegExpMo = aiNewRegExpMo;
	}

	/**
	 * Sets the value of NewRegExpMo
	 * 
	 * @param aiNewRegExpYr int
	 */
	public void setNewRegExpYr(int aiNewRegExpYr)
	{
		ciNewRegExpYr = aiNewRegExpYr;
	}
	/**
	 * Sets the value of OffHghwyIndi
	 * 
	 * @param aiOffHghwyIndi int
	 */
	public void setOffHghwyIndi(int aiOffHghwyIndi)
	{
		ciOffHghwyIndi = aiOffHghwyIndi;
	}

	/**
	 * Sets the value of POSPrntInvQty
	 * 
	 * @param aiPOSPrntInvQty int
	 */
	public void setPosPrntInvQty(int aiPOSPrntInvQty)
	{
		ciPOSPrntInvQty = aiPOSPrntInvQty;
	}

	/**
	 * Sets POSProcsIndi
	 * 
	 * @param abPOSProcs boolean 
	 */
	public void setPOSProcsIndi(boolean abPOSProcsIndi)
	{
		cbPOSProcsIndi = abPOSProcsIndi;
	}

	//	/**
	//	 * Sets the value of Processed
	//	 * 
	//	 * @param abProcessed boolean
	//	 */
	//	public void setProcessed(boolean abProcessed)
	//	{
	//		cbProcessed = abProcessed;
	//	}

	/**
	 * Sets the value of TtlRebateAmt
	 * 
	 * @param aaRebateAmt Dolalr 
	 */
	public void setRebateAmt(Dollar aaRebateAmt)
	{
		caTtlRebateAmt = aaRebateAmt;
	}

	/**
	 * Sets the value of RecordRejected
	 * 
	 * @param abRecordRejected boolean
	 */
	public void setRecordRejected(boolean abRecordRejected)
	{
		cbRecordRejected = abRecordRejected;
	}

	/**
	 * Set Record Type 
	 * 
	 * @param aiRecType
	 */
	public void setRecType(int aiRecType)
	{
		ciRecType = aiRecType;
	}

	/**
	 * Sets the value of RegFee
	 * 
	 * @param avRegFee Vector
	 */
	public void setRegFee(Vector avRegFee)
	{
		cvRegFee = avRegFee;
	}

	/**
	 * Sets the value of RSPSDiskSeqNo
	 * 
	 * @param aiRSPSDiskSeqNo int
	 */
	public void setRSPSDiskSeqNo(int aiRSPSDiskSeqNo)
	{
		ciRSPSDiskSeqNo = aiRSPSDiskSeqNo;
	}

	/**
	 * Set RSPSId
	 * 
	 * @param asScanProcNum String
	 */
	public void setRSPSId(String asRSPSId)
	{
		csRSPSId = asRSPSId;
	}

	/**
	 * RSPS Original Print Date.
	 * 
	 * @param asRSPSOrigPrntDate String
	 */
	// defect 8217
	//	Set method name to match field name
	public void setRSPSOrigPrntDate(String asRSPSOrigPrntDate)
	{
		csRSPSOrigPrntDate = asRSPSOrigPrntDate;
	}

	/**
	 * Sets the value of RSPSPrntInvQty
	 * 
	 * @param aiRSPSPrntInvQty int 
	 */
	public void setRSPSPrntInvQty(int aiRSPSPrntInvQty)
	{
		ciRSPSPrntInvQty = aiRSPSPrntInvQty;
	}

	/**
	 * Sets SkipCurrObj
	 * 
	 * @param abSkipCurrObj boolean 
	 */
	public void setSkipCurrObj(boolean abSkipCurrObj)
	{
		cbSkipCurrObj = abSkipCurrObj;
	}

	/**
	 * Sets ToBePrinted
	 * 
	 * @param abToBePrinted boolean 
	 */
	public void setToBePrinted(boolean abToBePrinted)
	{
		cbToBePrinted = abToBePrinted;
	}

	/**
	 * Sets TransCd
	 * 
	 * @param asTransCd String
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * Sets TransDt
	 * 
	 * @param asTransDt String 
	 */
	public void setTransDt(String asTransDt)
	{
		csTransDate = asTransDt;
	}

	/**
	 * Sets CompleteTransFee
	 * 
	 * @param aaTransFee Dollar
	 */
	public void setTransFee(Dollar aaTransFee)
	{
		caCompleteTransFee = aaTransFee;
	}

	/**
	 * Sets TransId
	 * 
	 * @param asTransId String 
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}

	/**
	 * Sets TransNo
	 * 
	 * @param aiTransNo int 
	 */
	public void setTransNo(int aiTransNo)
	{
		ciTransNo = aiTransNo;
	}

	/**
	 * Sets the value of cbUpdtPOSRSPSPrnt
	 * 
	 * @param abUpdtPOSRSPSPrntIndi boolean 
	 */
	public void setUpdtPOSRSPSPrntIndi(boolean abUpdtPOSRSPSPrntIndi)
	{
		cbUpdtPOSRSPSPrntIndi = abUpdtPOSRSPSPrntIndi;
	}

	/**
	 * Sets the value of VehMiscData
	 * 
	 * @param aaVehMiscData VehMiscData
	 */
	public void setVehMiscData(VehMiscData aaVehMiscData)
	{
		caVehMiscData = aaVehMiscData;
	}

	/**
	 * Sets Voided
	 * 
	 * @param aiVoided int 
	 */
	public void setVoided(int aiVoided)
	{
		ciVoided = aiVoided;
	}
	// end defect 9973
}

