package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

import com.txdot.isd.rts.server.dataaccess.MfAccess;

/*
 *
 * MfbaTitleU.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/27/2006	Update offset to new format
 * 							defect 6701 Ver Exempts
 * J Rue		10/17/2006	Remove TODOs
 * 							Replace "" with 
 * 							CommonConstant.STR_SPACE_EMPTY
 * 							modify MfbaTitleU()
 * 							modify setTitleDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		10/20/2006	Move process from MfAccess
 * 							defect 6701 Ver Exempts
 * J Rue		10/20/2006	Update JavaDoc
 * 							modify setTitleDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		01/30/2007	Title Hot Check Indi change from 837 
 * 							(RegHotChkIndi)
 * 							modify class var. TTL_HOT_CK_INDI_OFFSET
 * 							defect 9089 Ver Region-Credit Card
 * J Rue		02/01/2007	Add offsets and code
 * 							defect 9086 Ver Special Plates
 * J Rue		02/02/2007	Clean up JavaDoc 
 * 							defect 9086 Ver Special Plates
 * J Rue		05/30/2007	Update offset for TTL_HOT_CHK
 * 							defetc 9086 Ver Special Plates
 * J Rue		08/07/2007	Add LemonLawIndi
 * 							add LEMON_LAW_INDI_OFFSET, 
 * 							 LEMON_LAW_INDI_LENGTH
 * 							modify setTitleDataFromMf()
 * 							defect 9225 Ver Broadcast Messaging
 * J Rue		08/23/2007	Cleanup comments
 *   						defect 9225 Ver Broadcast Messaging
 * K Harrell	02/19/2008  Added missing code for ChildSupport
 * 	J Rue					add LEMON_LAW_INDI_OFFSET,
 *       					   LEMON_LAW_INDI_LENGTH 
 *							modify setTitleDataFromMf()    	
 *							defect 9548 Ver 3 Amigos PH A 
 * J Rue		04/01/2008	Commit out all references to LemonLaw
 *							modify setTitleDataFromMf()    	
 *							defect 9581 Ver 3_Amigos_PH_B 
 * J Rue		04/01/2008	Replace LemonLaw with Childsupport
 * 							modify setTitleDataFromMf()
 * 							defect 9581 Ver Defect_POS_A
 * J Rue		04/01/2008	New attributes for MF V version
 * 							Copy version U code to V
 * 							modify setTitleDataFromMf()
 * 							defect 9557 Ver Defect_POS_A
 * K Harrell	04/14/2008	Adjust offsets for Cancel Plates DocNo
 * 							defect 9557 Ver 3_AMIGOS_PH_B
 * J Rue		04/29/2008	Adjust offsets for Cancel Plates DocNo
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setTitleDataFromMf()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		09/30/2008	Add PERMLIENHLDRID and 	LIENRLSEDATE
 * 							modify setTitleDataFromMf()
 * 							defect 9833 Ver ELT_MfAccess
 * K Harrell	11/08/2008	Restored code for LemonLawIndi
 * 							add LEMON_LAW_INDI_OFFSET,
 * 							  LEMON_LAW_INDI_LENGTH  
 * 							modify setTitleDataFromMf() 
 * 							defect 9860 Ver Defect_POS_B
 * J Rue		11/13/2008	Add PERMLIENHLDRID and LIENRLSEDATE
 *							modify setTitleDataFromMf()    	
 *							defect 9833 Ver Defect_POS_B 
 * J Rue		12/30/2008	Set offsets to MFInterfaceVersionCode U
 * 							modify all class variables
 * 							defect 9655 Ver Defect_POS_D
 * J Rue		12/30/2008	Adjust offsets for OrgNo, RegPltAge, 
 * 							DISSOCIATECD.  
 * 							modify all class variablesdefect 
 * 							9655 Ver Defect_POS_D
 * 							defect 9655 Special Plates
 * J Rue		02/26/2009	Adjust offsets to ELT
 * 							Increase LEGALRESTRNTNO length to 9
 *							modify setTitleDataFromMf()    	
 *							defect 9833 Ver Defect_POS_E 
 * J Rue		02/27/2009	Set PermLienHldr to String
 * 							modify setTitleDataFromMf()
 * 							defect 9961 Ver Defect_POS_E
 * K Harrell	03/03/2009	Modify to use Lienholder Address Data
 * 							modify setTitleDataFromMf()
 * 							defect 9969 Ver Defect_POS_E  
 * J Rue		11/13/2008	Add PERMLIENHLDRID and LIENRLSEDATE
 *							modify setTitleDataFromMf()    	
 *							defect 9833 Ver Defect_POS_B 
 * J Rue		12/30/2008	Set offsets to MFInterfaceVersionCode U
 * 							modify all class variables
 * 							defect 9655 Ver Defect_POS_D
 * J Rue		12/30/2008	Adjust offsets for OrgNo, RegPltAge, 
 * 							DISSOCIATECD.  
 * 							modify all class variablesdefect 
 * 							9655 Ver Defect_POS_D
 * 							defect 9655 Special Plates
 * K Harrell	03/03/2009	Modify to use Lienholder Address Data
 * 							modify setTitleDataFromMf()
 * 							defect 9969 Ver Defect_POS_E  
 * J Rue		04/28/2009	Add UTVMISLBLINDI, VTRTTLEMRGCD1, 
 * 							VTRTTLEMRGCD2
 * 							add UTV_MISLBL_INDI_OFFSET, 
 * 								UTV_MISLBL_INDI_LENGTH,
 * 								VTR_TTL_EMRG_CD1_OFFSET,
 * 								VTR_TTL_EMRG_CD1_LENGTH,
 * 								VTR_TTL_EMRG_CD2_OFFSET,
 * 								VTR_TTL_EMRG_CD2_LENGTH
 * 							modify setTitleDataFromMf()
 *							defect 9961 Ver Defect_POS_E
 * J Rue		05/19/2009	Restored code for LemonLawIndi
 * 							add LEMON_LAW_INDI_OFFSET,
 * 							  LEMON_LAW_INDI_LENGTH  
 * 							modify setTitleDataFromMf() 
 * 							defect 9860 Ver Defect_POS_B
 * J Rue		05/19/2009	Restored code for LemonLawIndi
 * 							add LEMON_LAW_INDI_OFFSET,
 * 							  LEMON_LAW_INDI_LENGTH  
 * 							modify setTitleDataFromMf() 
 * 							defect 9860 Ver Defect_POS_B
 * K Harrell	07/03/2009	modify setTitleDataFromMf()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/24/2009	Set LienholdersData() to LienholderData()
 * 							modify setTitleDataFromMf()
 * 							defect 10112 Ver Defect_POS_F
 * J Rue		10/05/2009	Adjust offsets for RCCPI version T
 * 							modify all class variables,
 * 								setTitleDataFromMf()
 * 							defect 10244 Ver Defect_POS_G
 * B Hargrove	10/22/2009	Consolidate U/V/T JavaDoc.
 * 							defect 10244 Ver Defect_POS_G
 * K Harrell	02/09/2009	Modified w/ refactor of TitleData 
 * 							 VTRTtlEmrgCd1/2 to PvtLawEnfVehCd/
 * 							 NonTtlGolfCartCd.
 * 							delete VTR_TTL_EMRG_CD1_OFFSET,
 * 							  VTR_TTL_EMRG_CD1_LENGTH, 
 * 							  VTR_TTL_EMRG_CD2_OFFSET, 
 * 							  VTR_TTL_EMRG_CD2_LENGTH
 * 							add PVT_LAW_ENF_VEH_CD_OFFSET,
 * 							  PVT_LAW_ENF_VEH_CD_LENGTH,
 * 							  NONTTL_GOLF_CART_CD_OFFSET,
 * 							  NONTTL_GOLF_CART_CD_LENGTH  
 * 							modify setTitleDataFromMF() 	
 * 							defect 10366 Ver POS_640
 * M Reyes		03/03/2010	Copy version T to version U for MF
 * 							MF version U. Modified All.
 * 							defect 10378 Ver POS_640
 * M Reyes		04/06/2010	Added VTR_TTL_EMRG_CD3_OFFSET,
 * 							VTR_TTL_EMRG_CD3_LENGTH,
 * 							VTR_TTL_EMRG_CD4_OFFSET,
 * 							VTR_TTL_EMRG_CD4_LENGTH
 * 							defect 10378 Ver POS_640 
 * K Harrell 	06/22/2010  Copy version U to version V for MF
 *        					MF version V. Modified All.
 *        					defect 10492 Ver 6.5.0
 * M Reyes		10/06/2010	Copy version V to version T for MF
 * 							MF version T. Modified All.
 * 							defect 10595 Ver POS_660
 * M Reyes		01/10/2011	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver POS_670
 * M Reyes		01/13/2011	modify MULTIPLE_REG_INDI_OFFSET,
 * 							MULTIPLE_REG_INDI_LENGTH 					
 * 							defect 10710 Ver POS_670
 * K Harrell	11/01/2011	modify MULTIPLE_REG_INDI_OFFSET (made public)
 * 							defect 11045 Ver 6.9.0
 * K Harrell	11/06/2011	modify DOC_NO_OFFSET  (made public)
 * 							defect 11045 Ver 6.9.0    				 					
 * ---------------------------------------------------------------------
 */

/**
 * Parse CICS transactions into TitleData. 
 * Used by the server side business layer. 
 *
 * @version	6.9.0	  	11/06/2011
 * @author	Jeff Rue
 * <br>Creation Date:	09/26/2006 14:10:32
 */
public class MfbaTitleU
{
	//define all lengths and offsets for Vehicle Data
	// Note: offset is -1 minus from CICS file

	// Lienholder 1
	private static final int LIEN_HLDR1_NAME1_OFFSET = 75;
	private static final int LIEN_HLDR1_NAME1_LENGTH = 30;
	private static final int LIEN_HLDR1_NAME2_OFFSET = 105;
	private static final int LIEN_HLDR1_NAME2_LENGTH = 30;
	private static final int LIEN_HLDR1_NO_OFFSET = 135;
	private static final int LIEN_HLDR1_NO_LENGTH = 9;
	private static final int LIEN_HLDR1_ST1_OFFSET = 144;
	private static final int LIEN_HLDR1_ST1_LENGTH = 30;
	private static final int LIEN_HLDR1_ST2_OFFSET = 174;
	private static final int LIEN_HLDR1_ST2_LENGTH = 30;
	private static final int LIEN_HLDR1_CITY_OFFSET = 204;
	private static final int LIEN_HLDR1_CITY_LENGTH = 19;
	private static final int LIEN_HLDR1_STATE_OFFSET = 223;
	private static final int LIEN_HLDR1_STATE_LENGTH = 2;
	private static final int LIEN_HLDR1_ZPCD_OFFSET = 225;
	private static final int LIEN_HLDR1_ZPCD_LENGTH = 5;
	private static final int LIEN_HLDR1_ZPCDP4_OFFSET = 230;
	private static final int LIEN_HLDR1_ZPCDP4_LENGTH = 4;
	private static final int LIEN_HLDR1_CNTRY_OFFSET = 234;
	private static final int LIEN_HLDR1_CNTRY_LENGTH = 4;
	// Lienholder 2
	private static final int LIEN_HLDR2_NAME1_OFFSET = 238;
	private static final int LIEN_HLDR2_NAME1_LENGTH = 30;
	private static final int LIEN_HLDR2_NAME2_OFFSET = 268;
	private static final int LIEN_HLDR2_NAME2_LENGTH = 30;
	private static final int LIEN_HLDR2_NO_OFFSET = 298;
	private static final int LIEN_HLDR2_NO_LENGTH = 9;
	private static final int LIEN_HLDR2_ST1_OFFSET = 307;
	private static final int LIEN_HLDR2_ST1_LENGTH = 30;
	private static final int LIEN_HLDR2_ST2_OFFSET = 337;
	private static final int LIEN_HLDR2_ST2_LENGTH = 30;
	private static final int LIEN_HLDR2_CITY_OFFSET = 367;
	private static final int LIEN_HLDR2_CITY_LENGTH = 19;
	private static final int LIEN_HLDR2_STATE_OFFSET = 386;
	private static final int LIEN_HLDR2_STATE_LENGTH = 2;
	private static final int LIEN_HLDR2_ZPCD_OFFSET = 388;
	private static final int LIEN_HLDR2_ZPCD_LENGTH = 5;
	private static final int LIEN_HLDR2_ZPCDP4_OFFSET = 393;
	private static final int LIEN_HLDR2_ZPCDP4_LENGTH = 4;
	private static final int LIEN_HLDR2_CNTRY_OFFSET = 397;
	private static final int LIEN_HLDR2_CNTRY_LENGTH = 4;
	// Lienholder 3
	private static final int LIEN_HLDR3_NAME1_OFFSET = 401;
	private static final int LIEN_HLDR3_NAME1_LENGTH = 30;
	private static final int LIEN_HLDR3_NAME2_OFFSET = 431;
	private static final int LIEN_HLDR3_NAME2_LENGTH = 30;
	private static final int LIEN_HLDR3_NO_OFFSET = 461;
	private static final int LIEN_HLDR3_NO_LENGTH = 9;
	private static final int LIEN_HLDR3_ST1_OFFSET = 470;
	private static final int LIEN_HLDR3_ST1_LENGTH = 30;
	private static final int LIEN_HLDR3_ST2_OFFSET = 500;
	private static final int LIEN_HLDR3_ST2_LENGTH = 30;
	private static final int LIEN_HLDR3_CITY_OFFSET = 530;
	private static final int LIEN_HLDR3_CITY_LENGTH = 19;
	private static final int LIEN_HLDR3_STATE_OFFSET = 549;
	private static final int LIEN_HLDR3_STATE_LENGTH = 2;
	private static final int LIEN_HLDR3_ZPCD_OFFSET = 551;
	private static final int LIEN_HLDR3_ZPCD_LENGTH = 5;
	private static final int LIEN_HLDR3_ZPCDP4_OFFSET = 556;
	private static final int LIEN_HLDR3_ZPCDP4_LENGTH = 4;
	private static final int LIEN_HLDR3_CNTRY_OFFSET = 560;
	private static final int LIEN_HLDR3_CNTRY_LENGTH = 4;
	// Title In Process
	// defect 10378
	private static final int OLD_DOC_NO_OFFSET = 963;
	private static final int OLD_DOC_NO_LENGTH = 17;
	private static final int OWNRSHP_EVID_CD_OFFSET = 980;
	private static final int OWNRSHP_EVID_CD_LENGTH = 2;
	private static final int BATCH_NO_OFFSET = 982;
	private static final int BATCH_NO_LENGTH = 10;
	private static final int TTL_REJCTN_DATE_OFFSET = 992;
	private static final int TTL_REJCTN_DATE_LENGTH = 8;
	private static final int TTL_REJCTN_OFC_OFFSET = 1000;
	private static final int TTL_REJCTN_OFC_LENGTH = 3;
	// Title Rec
	
	// defect 11045 
	//private static final int DOC_NO_OFFSET = 1004;
	public static final int DOC_NO_OFFSET = 1004;
	// end defect 11045 
	
	private static final int DOC_NO_LENGTH = 17;
	private static final int BNDED_TTL_CD_OFFSET = 1259;
	private static final int BNDED_TTL_CD_LENGTH = 1;
	private static final int DOC_TYPE_CD_OFFSET = 1260;
	private static final int DOC_TYPE_CD_LENGTH = 2;
	private static final int TTL_ISSUE_DATE_OFFSET = 1262;
	private static final int TTL_ISSUE_DATE_LENGTH = 8;
	private static final int TTL_PROCS_CD_OFFSET = 1270;
	private static final int TTL_PROCS_CD_LENGTH = 2;
	private static final int PREV_OWNR_NAME_OFFSET = 1272;
	private static final int PREV_OWNR_NAME_LENGTH = 24;
	private static final int PREV_OWNR_CITY_OFFSET = 1296;
	private static final int PREV_OWNR_CITY_LENGTH = 11;
	private static final int PREV_OWNR_STATE_OFFSET = 1307;
	private static final int PREV_OWNR_STATE_LENGTH = 2;
	// Vehicle data
	private static final int VEH_SALES_PRICE_OFFSET = 1374;
	private static final int VEH_SALES_PRICE_LENGTH = 10;
	private static final int VEH_SALES_PRICE_DECIMAL = 2;
	private static final int VEH_SOLD_DATE_OFFSET = 1384;
	private static final int VEH_SOLD_DATE_LENGTH = 8;
	private static final int VEH_TRADE_IN_ALLOWNCE_OFFSET = 1392;
	private static final int VEH_TRADE_IN_ALLOWNCE_LENGTH = 8;
	private static final int VEH_TRADE_IN_ALLOWNCE_DECIMAL = 2;
	private static final int VEH_UNIT_NO_OFFSET = 1400;
	private static final int VEH_UNIT_NO_LENGTH = 6;
	// Indicators
	private static final int ADDL_LIEN_HOLD_INDI_OFFSET = 1412;
	private static final int ADDL_LIEN_HOLD_INDI_LENGTH = 1;
	private static final int AGNCY_LOAND_INDI_OFFSET = 1413;
	private static final int AGNCY_LOAND_INDI_LENGTH = 1;
	private static final int GOVT_OWND_INDI_OFFSET = 1420;
	private static final int GOVT_OWND_INDI_LENGTH = 1;
	private static final int TTL_HOT_CK_INDI_OFFSET = 1421;
	private static final int TTL_HOT_CK_INDI_LENGTH = 1;
	private static final int INSPCTN_WAIVED_INDI_OFFSET = 1422;
	private static final int INSPCTN_WAIVED_INDI_LENGTH = 1;
	private static final int PRIOR_CCO_ISSUE_INDI_OFFSET = 1425;
	private static final int PRIOR_CCO_ISSUE_INDI_LENGTH = 1;
	private static final int SURVSHP_RIGHTS_INDI_OFFSET = 1428;
	private static final int SURVSHP_RIGHTS_INDI_LENGTH = 1;
	private static final int TTL_REVKD_INDI_OFFSET = 1429;
	private static final int TTL_REVKD_INDI_LENGTH = 1;
	private static final int SALES_TAX_PD_AMT_OFFSET = 1431;
	private static final int SALES_TAX_PD_AMT_LENGTH = 8;
	private static final int SALES_TAX_PD_AMT_DECIMAL = 2;
	private static final int OTHR_STATE_CNTRY_OFFSET = 1439;
	private static final int OTHR_STATE_CNTRY_LENGTH = 2;
	private static final int SURR_TTL_DATE_OFFSET = 1441;
	private static final int SURR_TTL_DATE_LENGTH = 8;
	private static final int LEGAL_RESTRNT_NO_OFFSET = 1449;
	// defect 9961
	//	Increase length to 9
	//private static final int LEGAL_RESTRNT_NO_LENGTH = 7;
	private static final int LEGAL_RESTRNT_NO_LENGTH = 9;
	// end defect 9961
	private static final int CCO_ISSUE_DATE_OFFSET = 1458;
	private static final int CCO_ISSUE_DATE_LENGTH = 8;
	// Title Vehicle Location
	private static final int TTL_VEH_LOC_ST1_OFFSET = 1466;
	private static final int TTL_VEH_LOC_ST1_LENGTH = 30;
	private static final int TTL_VEH_LOC_ST2_OFFSET = 1496;
	private static final int TTL_VEH_LOC_ST2_LENGTH = 30;
	private static final int TTL_VEH_LOC_CITY_OFFSET = 1526;
	private static final int TTL_VEH_LOC_CITY_LENGTH = 19;
	private static final int TTL_VEH_LOC_STATE_OFFSET = 1545;
	private static final int TTL_VEH_LOC_STATE_LENGTH = 2;
	private static final int TTL_VEH_LOC_ZPCDP_OFFSET = 1547;
	private static final int TTL_VEH_LOC_ZPCDP_LENGTH = 5;
	private static final int TTL_VEH_LOC_ZPCDP4_OFFSET = 1552;
	private static final int TTL_VEH_LOC_ZPCDP4_LENGTH = 4;
	// defect 9833
	// Add PERMLIENHLDRID and LIENRLSEDATE
	private static final int PERM_LIENHLDR1_ID_OFFSET = 1556;
	private static final int PERM_LIENHLDR1_ID_LENGTH = 11;
	private static final int PERM_LIENHLDR2_ID_OFFSET = 1567;
	private static final int PERM_LIENHLDR2_ID_LENGTH = 11;
	private static final int PERM_LIENHLDR3_ID_OFFSET = 1578;
	private static final int PERM_LIENHLDR3_ID_LENGTH = 11;
	private static final int LIEN1_RLSE_DATE_OFFSET = 1589;
	private static final int LIEN1_RLSE_DATE_LENGTH = 8;
	private static final int LIEN2_RLSE_DATE_OFFSET = 1597;
	private static final int LIEN2_RLSE_DATE_LENGTH = 8;
	private static final int LIEN3_RLSE_DATE_OFFSET = 1605;
	private static final int LIEN3_RLSE_DATE_LENGTH = 8;
	// end defect 9833
	// LIENDATE
	private static final int LIEN1_DATE_OFFSET = 1640;
	private static final int LIEN1_DATE_LENGTH = 8;
	private static final int LIEN2_DATE_OFFSET = 1648;
	private static final int LIEN2_DATE_LENGTH = 8;
	private static final int LIEN3_DATE_OFFSET = 1656;
	private static final int LIEN3_DATE_LENGTH = 8;

	private static final int SALV_STATE_CNTRY_OFFSET = 1664;
	private static final int SALV_STATE_CNTRY_LENGTH = 2;
	// defect 9557
	// New attributes for ELT
	private static final int CHILD_SUPPORT_INDI_OFFSET = 1673;
	private static final int CHILD_SUPPORT_INDI_LENGTH = 1;
	private static final int TTL_SIGN_DATE_OFFSET = 1674;
	private static final int TTL_SIGN_DATE_LENGTH = 8;
	private static final int E_TTL_CD_OFFSET = 1682;
	private static final int E_TTL_CD_LENGTH = 1;
	// end defect 9557    
	// defect 9961
	// New attributes for ELT
	private static final int UTV_MISLBL_INDI_OFFSET = 1683;
	private static final int UTV_MISLBL_INDI_LENGTH = 1;

	// defect 10366 
	//	private static final int VTR_TTL_EMRG_CD1_OFFSET = 1631;
	//	private static final int VTR_TTL_EMRG_CD1_LENGTH = 1;
	//	private static final int VTR_TTL_EMRG_CD2_OFFSET = 1632;
	//	private static final int VTR_TTL_EMRG_CD2_LENGTH = 1;

	private static final int PVT_LAW_ENF_VEH_CD_OFFSET = 1684;
	private static final int PVT_LAW_ENF_VEH_CD_LENGTH = 1;
	private static final int NONTTL_GOLF_CART_CD_OFFSET = 1685;
	private static final int NONTTL_GOLF_CART_CD_LENGTH = 1;
	// end defect 9961 
	// defect 11045   
	public static final int MULTIPLE_REG_INDI_OFFSET = 1694;
	// end defect 11045
	private static final int MULTIPLE_REG_INDI_LENGTH = 1;

	//	defect 9860
	// Restoring LemonLawIndi 
	private static final int LEMON_LAW_INDI_OFFSET = 1672;
	private static final int LEMON_LAW_INDI_LENGTH = 1;
	// end defect 9860
	// end defect 10378

	// defect 10378
	// New Attributes for verion U
	private static final int VTR_TTL_EMRG_CD3_OFFSET = 1686;
	private static final int VTR_TTL_EMRG_CD3_LENGTH = 1;
	private static final int VTR_TTL_EMRG_CD4_OFFSET = 1687;
	private static final int VTR_TTL_EMRG_CD4_LENGTH = 1;
	// end defect 10378
	/**
	 * 
	 */
	public MfbaTitleU()
	{
		super();
	}

	public static void main(String[] args)
	{
	}

	/**
	 * Sets and returns the TitleData from mainframe response
	 * 
	 * @param asMfResponse String
	 * @return TitleData
	 */
	public TitleData setTitleDataFromMf(String asMfResponse)
	{
		MfAccess laMFAccess = new MfAccess();

		//create the return object
		TitleData laTitleData = new TitleData();

		if (!(asMfResponse.equals(CommonConstant.STR_SPACE_EMPTY))
			&& (asMfResponse != null))
		{
			// LIENHOLDER 1
			// defect 10112
			//	Set LienholdersData to LienholderData
			LienholderData laLienHolderData1 = new LienholderData();
			// end defect LienholderData
			laLienHolderData1.setLienDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								LIEN1_DATE_OFFSET,
								LIEN1_DATE_OFFSET
									+ LIEN1_DATE_LENGTH)))
					.intValue());

			// defect 10112 
			laLienHolderData1.setName1(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR1_NAME1_OFFSET,
						LIEN_HLDR1_NAME1_OFFSET
							+ LIEN_HLDR1_NAME1_LENGTH)));

			laLienHolderData1.setName2(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR1_NAME2_OFFSET,
						LIEN_HLDR1_NAME2_OFFSET
							+ LIEN_HLDR1_NAME2_LENGTH)));
			// end defect 10112 

			laLienHolderData1.setId(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								LIEN_HLDR1_NO_OFFSET,
								LIEN_HLDR1_NO_OFFSET
									+ LIEN_HLDR1_NO_LENGTH)))
					.intValue());

			// defect 9969 
			laLienHolderData1.getAddressData().setSt1(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR1_ST1_OFFSET,
						LIEN_HLDR1_ST1_OFFSET
							+ LIEN_HLDR1_ST1_LENGTH)));

			laLienHolderData1.getAddressData().setSt2(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR1_ST2_OFFSET,
						LIEN_HLDR1_ST2_OFFSET
							+ LIEN_HLDR1_ST2_LENGTH)));

			laLienHolderData1.getAddressData().setCity(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR1_CITY_OFFSET,
						LIEN_HLDR1_CITY_OFFSET
							+ LIEN_HLDR1_CITY_LENGTH)));

			laLienHolderData1.getAddressData().setState(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR1_STATE_OFFSET,
						LIEN_HLDR1_STATE_OFFSET
							+ LIEN_HLDR1_STATE_LENGTH)));

			laLienHolderData1.getAddressData().setCntry(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR1_CNTRY_OFFSET,
						LIEN_HLDR1_CNTRY_OFFSET
							+ LIEN_HLDR1_CNTRY_LENGTH)));

			laLienHolderData1.getAddressData().setZpcd(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR1_ZPCD_OFFSET,
						LIEN_HLDR1_ZPCD_OFFSET
							+ LIEN_HLDR1_ZPCD_LENGTH)));

			laLienHolderData1.getAddressData().setZpcdp4(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR1_ZPCDP4_OFFSET,
						LIEN_HLDR1_ZPCDP4_OFFSET
							+ LIEN_HLDR1_ZPCDP4_LENGTH)));

			// LIENHOLDER 2
			LienholderData laLienHolderData2 = new LienholderData();

			laLienHolderData2.setLienDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								LIEN2_DATE_OFFSET,
								LIEN2_DATE_OFFSET
									+ LIEN2_DATE_LENGTH)))
					.intValue());

			// defect 10112 
			laLienHolderData2.setName1(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR2_NAME1_OFFSET,
						LIEN_HLDR2_NAME1_OFFSET
							+ LIEN_HLDR2_NAME1_LENGTH)));

			laLienHolderData2.setName2(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR2_NAME2_OFFSET,
						LIEN_HLDR2_NAME2_OFFSET
							+ LIEN_HLDR2_NAME2_LENGTH)));
			// end defect 10112 

			laLienHolderData2.setId(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								LIEN_HLDR2_NO_OFFSET,
								LIEN_HLDR2_NO_OFFSET
									+ LIEN_HLDR2_NO_LENGTH)))
					.intValue());

			laLienHolderData2.getAddressData().setSt1(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR2_ST1_OFFSET,
						LIEN_HLDR2_ST1_OFFSET
							+ LIEN_HLDR2_ST1_LENGTH)));

			laLienHolderData2.getAddressData().setSt2(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR2_ST2_OFFSET,
						LIEN_HLDR2_ST2_OFFSET
							+ LIEN_HLDR2_ST2_LENGTH)));

			laLienHolderData2.getAddressData().setCity(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR2_CITY_OFFSET,
						LIEN_HLDR2_CITY_OFFSET
							+ LIEN_HLDR2_CITY_LENGTH)));

			laLienHolderData2.getAddressData().setState(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR2_STATE_OFFSET,
						LIEN_HLDR2_STATE_OFFSET
							+ LIEN_HLDR2_STATE_LENGTH)));

			laLienHolderData2.getAddressData().setCntry(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR2_CNTRY_OFFSET,
						LIEN_HLDR2_CNTRY_OFFSET
							+ LIEN_HLDR2_CNTRY_LENGTH)));

			laLienHolderData2.getAddressData().setZpcd(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR2_ZPCD_OFFSET,
						LIEN_HLDR2_ZPCD_OFFSET
							+ LIEN_HLDR2_ZPCD_LENGTH)));

			laLienHolderData2.getAddressData().setZpcdp4(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR2_ZPCDP4_OFFSET,
						LIEN_HLDR2_ZPCDP4_OFFSET
							+ LIEN_HLDR2_ZPCDP4_LENGTH)));

			// LIENHOLDER 3
			LienholderData laLienHolderData3 = new LienholderData();

			laLienHolderData3.setLienDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								LIEN3_DATE_OFFSET,
								LIEN3_DATE_OFFSET
									+ LIEN3_DATE_LENGTH)))
					.intValue());

			// defect 10112 
			laLienHolderData3.setName1(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR3_NAME1_OFFSET,
						LIEN_HLDR3_NAME1_OFFSET
							+ LIEN_HLDR3_NAME1_LENGTH)));

			laLienHolderData3.setName2(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR3_NAME2_OFFSET,
						LIEN_HLDR3_NAME2_OFFSET
							+ LIEN_HLDR3_NAME2_LENGTH)));
			// end defect 10112 

			laLienHolderData3.setId(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								LIEN_HLDR3_NO_OFFSET,
								LIEN_HLDR3_NO_OFFSET
									+ LIEN_HLDR3_NO_LENGTH)))
					.intValue());

			laLienHolderData3.getAddressData().setSt1(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR3_ST1_OFFSET,
						LIEN_HLDR3_ST1_OFFSET
							+ LIEN_HLDR3_ST1_LENGTH)));

			laLienHolderData3.getAddressData().setSt2(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR3_ST2_OFFSET,
						LIEN_HLDR3_ST2_OFFSET
							+ LIEN_HLDR3_ST2_LENGTH)));

			laLienHolderData3.getAddressData().setCity(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR3_CITY_OFFSET,
						LIEN_HLDR3_CITY_OFFSET
							+ LIEN_HLDR3_CITY_LENGTH)));

			laLienHolderData3.getAddressData().setState(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR3_STATE_OFFSET,
						LIEN_HLDR3_STATE_OFFSET
							+ LIEN_HLDR3_STATE_LENGTH)));

			laLienHolderData3.getAddressData().setCntry(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR3_CNTRY_OFFSET,
						LIEN_HLDR3_CNTRY_OFFSET
							+ LIEN_HLDR3_CNTRY_LENGTH)));

			laLienHolderData3.getAddressData().setZpcd(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR3_ZPCD_OFFSET,
						LIEN_HLDR3_ZPCD_OFFSET
							+ LIEN_HLDR3_ZPCD_LENGTH)));

			laLienHolderData3.getAddressData().setZpcdp4(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LIEN_HLDR3_ZPCDP4_OFFSET,
						LIEN_HLDR3_ZPCDP4_OFFSET
							+ LIEN_HLDR3_ZPCDP4_LENGTH)));
			// end defect 9969

			// defect 10112 
			// laTitleData.setLienHolder2(laLienHolderData1);
			// laTitleData.setLienHolder2(laLienHolderData2);
			// laTitleData.setLienHolder3(laLienHolderData3);

			laTitleData.setLienholderData(
				TitleConstant.LIENHLDR1,
				laLienHolderData1);
			laTitleData.setLienholderData(
				TitleConstant.LIENHLDR2,
				laLienHolderData2);
			laTitleData.setLienholderData(
				TitleConstant.LIENHLDR3,
				laLienHolderData3);
			// end defect 10112 

			laTitleData.setBatchNo(
				laMFAccess.getStringFromZonedDecimal(
					asMfResponse.substring(
						BATCH_NO_OFFSET,
						BATCH_NO_OFFSET + BATCH_NO_LENGTH)));

			laTitleData.setOldDocNo(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						OLD_DOC_NO_OFFSET,
						OLD_DOC_NO_OFFSET + OLD_DOC_NO_LENGTH)));

			laTitleData.setOwnrShpEvidCd(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								OWNRSHP_EVID_CD_OFFSET,
								OWNRSHP_EVID_CD_OFFSET
									+ OWNRSHP_EVID_CD_LENGTH)))
					.intValue());

			laTitleData.setTtlRejctnDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								TTL_REJCTN_DATE_OFFSET,
								TTL_REJCTN_DATE_OFFSET
									+ TTL_REJCTN_DATE_LENGTH)))
					.intValue());

			laTitleData.setTtlRejctnOfc(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								TTL_REJCTN_OFC_OFFSET,
								TTL_REJCTN_OFC_OFFSET
									+ TTL_REJCTN_OFC_LENGTH)))
					.intValue());

			// Indicators
			laTitleData.setAddlLienRecrdIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								ADDL_LIEN_HOLD_INDI_OFFSET,
								ADDL_LIEN_HOLD_INDI_OFFSET
									+ ADDL_LIEN_HOLD_INDI_LENGTH)))
					.intValue());

			laTitleData.setAgncyLoandIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								AGNCY_LOAND_INDI_OFFSET,
								AGNCY_LOAND_INDI_OFFSET
									+ AGNCY_LOAND_INDI_LENGTH)))
					.intValue());

			laTitleData.setBndedTtlCd(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						BNDED_TTL_CD_OFFSET,
						BNDED_TTL_CD_OFFSET + BNDED_TTL_CD_LENGTH)));

			laTitleData.setCcoIssueDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								CCO_ISSUE_DATE_OFFSET,
								CCO_ISSUE_DATE_OFFSET
									+ CCO_ISSUE_DATE_LENGTH)))
					.intValue());

			laTitleData.setDocNo(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						DOC_NO_OFFSET,
						DOC_NO_OFFSET + DOC_NO_LENGTH)));

			laTitleData.setDocTypeCd(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								DOC_TYPE_CD_OFFSET,
								DOC_TYPE_CD_OFFSET
									+ DOC_TYPE_CD_LENGTH)))
					.intValue());

			laTitleData.setGovtOwndIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								GOVT_OWND_INDI_OFFSET,
								GOVT_OWND_INDI_OFFSET
									+ GOVT_OWND_INDI_LENGTH)))
					.intValue());

			laTitleData.setTtlHotCkIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								TTL_HOT_CK_INDI_OFFSET,
								TTL_HOT_CK_INDI_OFFSET
									+ TTL_HOT_CK_INDI_LENGTH)))
					.intValue());

			laTitleData.setInspectnWaivedIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								INSPCTN_WAIVED_INDI_OFFSET,
								INSPCTN_WAIVED_INDI_OFFSET
									+ INSPCTN_WAIVED_INDI_LENGTH)))
					.intValue());

			laTitleData.setLegalRestrntNo(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						LEGAL_RESTRNT_NO_OFFSET,
						LEGAL_RESTRNT_NO_OFFSET
							+ LEGAL_RESTRNT_NO_LENGTH)));

			laTitleData.setOthrStateCntry(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						OTHR_STATE_CNTRY_OFFSET,
						OTHR_STATE_CNTRY_OFFSET
							+ OTHR_STATE_CNTRY_LENGTH)));

			laTitleData.setPrevOwnrCity(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						PREV_OWNR_CITY_OFFSET,
						PREV_OWNR_CITY_OFFSET
							+ PREV_OWNR_CITY_LENGTH)));

			laTitleData.setPrevOwnrName(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						PREV_OWNR_NAME_OFFSET,
						PREV_OWNR_NAME_OFFSET
							+ PREV_OWNR_NAME_LENGTH)));

			laTitleData.setPrevOwnrState(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						PREV_OWNR_STATE_OFFSET,
						PREV_OWNR_STATE_OFFSET
							+ PREV_OWNR_STATE_LENGTH)));

			laTitleData.setPriorCCOIssueIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								PRIOR_CCO_ISSUE_INDI_OFFSET,
								PRIOR_CCO_ISSUE_INDI_OFFSET
									+ PRIOR_CCO_ISSUE_INDI_LENGTH)))
					.intValue());

			laTitleData.setSurrTtlDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								SURR_TTL_DATE_OFFSET,
								SURR_TTL_DATE_OFFSET
									+ SURR_TTL_DATE_LENGTH)))
					.intValue());

			laTitleData.setSurvshpRightsIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								SURVSHP_RIGHTS_INDI_OFFSET,
								SURVSHP_RIGHTS_INDI_OFFSET
									+ SURVSHP_RIGHTS_INDI_LENGTH)))
					.intValue());

			laTitleData.setTtlIssueDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								TTL_ISSUE_DATE_OFFSET,
								TTL_ISSUE_DATE_OFFSET
									+ TTL_ISSUE_DATE_LENGTH)))
					.intValue());

			laTitleData.setTtlProcsCd(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						TTL_PROCS_CD_OFFSET,
						TTL_PROCS_CD_OFFSET + TTL_PROCS_CD_LENGTH)));

			laTitleData.setTtlRevkdIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								TTL_REVKD_INDI_OFFSET,
								TTL_REVKD_INDI_OFFSET
									+ TTL_REVKD_INDI_LENGTH)))
					.intValue());

			AddressData laAddressData = new AddressData();

			// TTLVEHLOC
			laAddressData.setCity(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						TTL_VEH_LOC_CITY_OFFSET,
						TTL_VEH_LOC_CITY_OFFSET
							+ TTL_VEH_LOC_CITY_LENGTH)));

			laAddressData.setSt1(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						TTL_VEH_LOC_ST1_OFFSET,
						TTL_VEH_LOC_ST1_OFFSET
							+ TTL_VEH_LOC_ST1_LENGTH)));

			laAddressData.setSt2(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						TTL_VEH_LOC_ST2_OFFSET,
						TTL_VEH_LOC_ST2_OFFSET
							+ TTL_VEH_LOC_ST2_LENGTH)));

			laAddressData.setState(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						TTL_VEH_LOC_STATE_OFFSET,
						TTL_VEH_LOC_STATE_OFFSET
							+ TTL_VEH_LOC_STATE_LENGTH)));

			laAddressData.setZpcd(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						TTL_VEH_LOC_ZPCDP_OFFSET,
						TTL_VEH_LOC_ZPCDP_OFFSET
							+ TTL_VEH_LOC_ZPCDP_LENGTH)));

			laAddressData.setZpcdp4(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						TTL_VEH_LOC_ZPCDP4_OFFSET,
						TTL_VEH_LOC_ZPCDP4_OFFSET
							+ TTL_VEH_LOC_ZPCDP4_LENGTH)));

			laTitleData.setTtlVehAddr(laAddressData);

			laTitleData.setVehSoldDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								VEH_SOLD_DATE_OFFSET,
								VEH_SOLD_DATE_OFFSET
									+ VEH_SOLD_DATE_LENGTH)))
					.intValue());

			laTitleData.setVehUnitNo(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						VEH_UNIT_NO_OFFSET,
						VEH_UNIT_NO_OFFSET + VEH_UNIT_NO_LENGTH)));

			// LIENDATA
			// defect 9883
			//	Add PermLienHldrId and LienRlseDate
			laTitleData.setPermLienHldrId1(
				laMFAccess.getStringFromZonedDecimal(
					asMfResponse.substring(
						PERM_LIENHLDR1_ID_OFFSET,
						PERM_LIENHLDR1_ID_OFFSET
							+ PERM_LIENHLDR1_ID_LENGTH)));

			laTitleData.setPermLienHldrId2(
				laMFAccess.getStringFromZonedDecimal(
					asMfResponse.substring(
						PERM_LIENHLDR2_ID_OFFSET,
						PERM_LIENHLDR2_ID_OFFSET
							+ PERM_LIENHLDR2_ID_LENGTH)));

			laTitleData.setPermLienHldrId3(
				laMFAccess.getStringFromZonedDecimal(
					asMfResponse.substring(
						PERM_LIENHLDR3_ID_OFFSET,
						PERM_LIENHLDR3_ID_OFFSET
							+ PERM_LIENHLDR3_ID_LENGTH)));

			laTitleData.setLienRlseDate1(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								LIEN1_RLSE_DATE_OFFSET,
								LIEN1_RLSE_DATE_OFFSET
									+ LIEN1_RLSE_DATE_LENGTH)))
					.intValue());

			laTitleData.setLienRlseDate2(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								LIEN2_RLSE_DATE_OFFSET,
								LIEN2_RLSE_DATE_OFFSET
									+ LIEN2_RLSE_DATE_LENGTH)))
					.intValue());

			laTitleData.setLienRlseDate3(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								LIEN3_RLSE_DATE_OFFSET,
								LIEN3_RLSE_DATE_OFFSET
									+ LIEN3_RLSE_DATE_LENGTH)))
					.intValue());
			// end defect 9833

			//
			//			laTitleData.setLienHolder1Date(
			//				Integer
			//					.valueOf(
			//						laMFAccess.getStringFromZonedDecimal(
			//							asMfResponse.substring(
			//								LIEN1_DATE_OFFSET,
			//								LIEN1_DATE_OFFSET
			//									+ LIEN1_DATE_LENGTH)))
			//					.intValue());
			//
			//			laTitleData.setLienHolder2Date(
			//				Integer
			//					.valueOf(
			//						laMFAccess.getStringFromZonedDecimal(
			//							asMfResponse.substring(
			//								LIEN2_DATE_OFFSET,
			//								LIEN2_DATE_OFFSET
			//									+ LIEN2_DATE_LENGTH)))
			//					.intValue());
			//
			//			laTitleData.setLienHolder3Date(
			//				Integer
			//					.valueOf(
			//						laMFAccess.getStringFromZonedDecimal(
			//							asMfResponse.substring(
			//								LIEN3_DATE_OFFSET,
			//								LIEN3_DATE_OFFSET
			//									+ LIEN3_DATE_LENGTH)))
			//					.intValue());

			// Others
			laTitleData.setSalvStateCntry(
				laMFAccess.trimMfString(
					asMfResponse.substring(
						SALV_STATE_CNTRY_OFFSET,
						SALV_STATE_CNTRY_OFFSET
							+ SALV_STATE_CNTRY_LENGTH)));

			//set the amount/fees fields
			StringBuffer laSalesTaxPdAmtBuffer =
				new StringBuffer(
					laMFAccess.getStringFromZonedDecimal(
						asMfResponse.substring(
							SALES_TAX_PD_AMT_OFFSET,
							SALES_TAX_PD_AMT_OFFSET
								+ SALES_TAX_PD_AMT_LENGTH)));

			if (laSalesTaxPdAmtBuffer.charAt(0) != '-')
			{
				laSalesTaxPdAmtBuffer.insert(
					SALES_TAX_PD_AMT_LENGTH - SALES_TAX_PD_AMT_DECIMAL,
					'.');
			}
			else
			{
				laSalesTaxPdAmtBuffer.insert(
					SALES_TAX_PD_AMT_LENGTH
						- SALES_TAX_PD_AMT_DECIMAL
						+ 1,
					'.');
			}
			Dollar laSalesTaxPdAmt =
				new Dollar(laSalesTaxPdAmtBuffer.toString());
			laTitleData.setSalesTaxPdAmt(laSalesTaxPdAmt);
			StringBuffer laVehSalesPriceBuffer =
				new StringBuffer(
					laMFAccess.getStringFromZonedDecimal(
						asMfResponse.substring(
							VEH_SALES_PRICE_OFFSET,
							VEH_SALES_PRICE_OFFSET
								+ VEH_SALES_PRICE_LENGTH)));

			if (laVehSalesPriceBuffer.charAt(0) != '-')
			{
				laVehSalesPriceBuffer.insert(
					VEH_SALES_PRICE_LENGTH - VEH_SALES_PRICE_DECIMAL,
					'.');
			}
			else
			{
				laVehSalesPriceBuffer.insert(
					VEH_SALES_PRICE_LENGTH
						- VEH_SALES_PRICE_DECIMAL
						+ 1,
					'.');
			}
			Dollar laVehSalesPrice =
				new Dollar(laVehSalesPriceBuffer.toString());
			laTitleData.setVehSalesPrice(laVehSalesPrice);

			StringBuffer laVehTradeInAllowanceBuffer =
				new StringBuffer(
					laMFAccess.getStringFromZonedDecimal(
						asMfResponse.substring(
							VEH_TRADE_IN_ALLOWNCE_OFFSET,
							VEH_TRADE_IN_ALLOWNCE_OFFSET
								+ VEH_TRADE_IN_ALLOWNCE_LENGTH)));

			if (laVehTradeInAllowanceBuffer.charAt(0) != '-')
			{
				laVehTradeInAllowanceBuffer.insert(
					VEH_TRADE_IN_ALLOWNCE_LENGTH
						- VEH_TRADE_IN_ALLOWNCE_DECIMAL,
					'.');
			}
			else
			{
				laVehTradeInAllowanceBuffer.insert(
					VEH_TRADE_IN_ALLOWNCE_LENGTH
						- VEH_TRADE_IN_ALLOWNCE_DECIMAL
						+ 1,
					'.');
			}
			Dollar laVehTradeInAllowance =
				new Dollar(laVehTradeInAllowanceBuffer.toString());
			laTitleData.setVehTradeinAllownce(laVehTradeInAllowance);

			// defect 9557
			//	New attributes for MF version V
			laTitleData.setChildSupportIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								CHILD_SUPPORT_INDI_OFFSET,
								CHILD_SUPPORT_INDI_OFFSET
									+ CHILD_SUPPORT_INDI_LENGTH)))
					.intValue());

			laTitleData.setTtlSignDate(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								TTL_SIGN_DATE_OFFSET,
								TTL_SIGN_DATE_OFFSET
									+ TTL_SIGN_DATE_LENGTH)))
					.intValue());

			laTitleData.setETtlCd(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								E_TTL_CD_OFFSET,
								E_TTL_CD_OFFSET + E_TTL_CD_LENGTH)))
					.intValue());
			// end defect 9557
			// defect 9961
			// New attributes for ELT
			laTitleData.setUTVMislblIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								UTV_MISLBL_INDI_OFFSET,
								UTV_MISLBL_INDI_OFFSET
									+ UTV_MISLBL_INDI_LENGTH)))
					.intValue());

			// defect 10366
			laTitleData.setPvtLawEnfVehCd(
				asMfResponse.substring(
					PVT_LAW_ENF_VEH_CD_OFFSET,
					PVT_LAW_ENF_VEH_CD_OFFSET
						+ PVT_LAW_ENF_VEH_CD_LENGTH));

			laTitleData.setNonTtlGolfCartCd(
				asMfResponse.substring(
					NONTTL_GOLF_CART_CD_OFFSET,
					NONTTL_GOLF_CART_CD_OFFSET
						+ NONTTL_GOLF_CART_CD_LENGTH));
			// end defect 10366

			// defect 10378
			// New Attributes for verion U
			laTitleData.setVTRTtlEmrgCd3(
				asMfResponse.substring(
					VTR_TTL_EMRG_CD3_OFFSET,
					VTR_TTL_EMRG_CD3_OFFSET + VTR_TTL_EMRG_CD3_LENGTH));

			laTitleData.setVTRTtlEmrgCd4(
				asMfResponse.substring(
					VTR_TTL_EMRG_CD4_OFFSET,
					VTR_TTL_EMRG_CD4_OFFSET + VTR_TTL_EMRG_CD4_LENGTH));
			// end 10378
			// end defect 9961
			laTitleData.setMultRegIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								MULTIPLE_REG_INDI_OFFSET,
								MULTIPLE_REG_INDI_OFFSET
									+ MULTIPLE_REG_INDI_LENGTH)))
					.intValue());

			// defect 9860 
			laTitleData.setLemonLawIndi(
				Integer
					.valueOf(
						laMFAccess.getStringFromZonedDecimal(
							asMfResponse.substring(
								LEMON_LAW_INDI_OFFSET,
								LEMON_LAW_INDI_OFFSET
									+ LEMON_LAW_INDI_LENGTH)))
					.intValue());
			// end defect 9860 

		} //end of if
		return laTitleData;
	}
}
