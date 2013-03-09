package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.constants.ApplicationControlConstants;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.server.systemcontrolbatch.MFTrans;

/*
 * MfbaSendTransT.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	10/09/2006	New Class
 * 							defect 6701 Ver Exempts
 * Ray Rowehl	10/10/2006	New class.
 * 							Since this class will not be active in the 
 * 							Exempts project, there is no significant 
 * 							checking for constants or comments.
 * 							defect 6701 Ver Exempts
 * J Rue		02/26/2007	New Class
 * 							defect 9086 Ver Special Plates
 * J Rue		03/02/2007	Change char values to SC_MFA_INSERT_AT_END
 * 							modify getValuesFromSRFuncTransData()
 * 							defect 9086 Ver Special Plates
 * Ray Rowehl	05/14/2007	Add ItrntTraceNo to FundFunc.
 * 							add IL_TFF_ITRNTTRACENO_LENGTH
 * 							modify getValuesFromFundFuncTrans()
 * 							defect 9086 Ver Special Plates
 * J Rue		05/23/2007	Reset PltSetNo padding to beginning
 * 							modify getValuesFromSRFuncTransData()
 * 							defect 9086 Ver Special Plates
 * J Rue		03/28/2008	Increase New DlrGDN No = 10
 * 							modify IL_TMV_DLRGDNNEW_LENGTH,
 * 							   IL_TSR_PLTOWNRDLRGDNNEW_LENGTH
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		04/03/2008	Increase IL_TMV_DLRGDN_LENGTH = 10
 * 							add IL_TMV_DLRGDNOLD_LENGTH
 * 							modify IL_TMV_DLRGDN_LENGTH, 
 * 								getValuesFromSRFuncTransData()
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		05/23/2008	Clean up code for SendTrans
 * 							modify getValuesFromMVFuncTransactionData()
 * 								getValuesFromSRFuncTransData()
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/12/2008	Change Ver to Defect_POS_A
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/17/2008	Remove references to 6 digit GDN
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 9557 Ver Defect_POS_A
 * K Harrell	06/20/2008	Populate TTLTRNSFRPNLTEXMPTCD from
 * 							MVFuncTransData 
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 9724 Ver Defect POS A
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								formatSendTransData(), 
 * 								getValuesFromFundFuncTrans(),
 * 								getValuesFromTransInvDetailData()
 * 								getValuesFromTransactionPaymentData()
 * 								getValuesFromTransactionData()
 * 								getValuesFromMVFuncTransactionData()
 * 								getValuesFromSRFuncTransData()
 * 								getValuesFromLogonFunc()
 * 								getValuesFromInvFuncTransData()
 * 								getValuesFromFundsDtlTransactionData()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		10/15/2008	Add Lien data PERMLIENHLDRID and LIENRLSEDATE
 * 							add class int for PERMLIENHLDRID and 
 * 							LIENRLSEDATE length for all three liens
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		10/23/2008	Convert MFInterfaceVersionCode to T
 * 							modify all class variables,
 * 								formatSendTransData(), 
 * 								getValuesFromFundFuncTrans(),
 * 								getValuesFromTransInvDetailData()
 * 								getValuesFromTransactionPaymentData()
 * 								getValuesFromTransactionData()
 * 								getValuesFromMVFuncTransactionData()
 * 								getValuesFromSRFuncTransData()
 * 								getValuesFromLogonFunc()
 * 								getValuesFromInvFuncTransData()
 * 								getValuesFromFundsDtlTransactionData()
 * 							defect 9849 Ver MyPlates_II
 * J Rue		10/24/2008	New feilds for MyPlates_II. 
 * 							Remove INVCCD, MNYRCVDDUPDTCD, RCPTCD
 * 								SPCLREGSTKRNO, SPCLRENWLCD, ACCPTINDI,
 * 								VRIMSMFGCD, EXISTINGDUPLINDI,
 * 								PLTOWNRDLRGDNOLD
 * 							add IL_TSR_PLTOWNRDLRGDNNEW_LENGTH
 * 								IL_TSR_NONEXPPLTINDI_LENGTH
 * 								IL_TSR_ITRNTTRACENO_LENGTH
 * 								IL_TSR_FINDOCNO_LENGTH
 * 								IL_TSR_PREVPLTCD_LENGTH
 * 								IL_TSR_PREVPLTEXPYR_LENGTH
 * 								IL_TSR_PREVPLTEXPMO_LENGTH
 * 								IL_TSR_PREVMFGPLTNO_LENGTH
 * 								IL_TSR_PREVPLTVALIDITYTERM_LENGTH
 * 							modify getValuesFromSRFuncTransData()
 * 							defect 9849 Ver MyPlates_II
 * J Rue		11/07/2008	Change to MotorVehicleFunctionTrans where
 * 							getter/setters were change to uppser/lower 
 * 							case
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 9833 Ver Defect_POS_B
 * K Harrell	11/12/2008	SRFuncTrans refactor of RegExpMo/Yr ==> 
 * 							PltExpMo/Yr
 * 							defect 9864 Ver MyPlates_II 
 * J Rue		12/31/2008	Rename offsets to match MF/POS definitions
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 9655 Ver Defect_POS_D
 * K Harrell	01/07/2009	SRFuncTrans refactor of RegExpMo/Yr ==> 
 * 							PltExpMo/Yr
 * 							defect 9864 Ver Defect_POS_D
 * J Rue		02/26/2009	Modify MV_FUNC_TRANS for ELT
 * 							modify IL_TMV_LEGALRESTRNTNO_LENGTH,
 * 								getValuesFromMVFuncTransactionData()
 * 							defect 9961 Ver Defect_POS_E
 * K Harrell	03/26/2009	Typo, had used LienRlseDat2e vs. Lien2Date
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 9961 Ver Defect_POS_E
 * J Rue		03/26/2009	Fill spaces with char, PermLienHldrId
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 9961 Ver Defect_POS_E
 * K Harrell	04/08/2009	Remove reference to AAMVAMSGID
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	07/03/2009	Implement new data structures 
 * 							modify getValuesFromMVFuncTransactionData(), 
 * 							 getValuesSRFuncTransData() 
 * 							defect 10112 Ver Defect_POS_F
 * J Rue		09/03/2009	Adjust LienHldr2St1 reference. Remove 
 * 							duplicate OwnrZdP4
 * 							modify getValuesFromMVFuncTransactionData(), 
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	09/03/2009	Correct MVFuncTrans layout
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 10112 Ver Defect_POS_F    
 * K Harrell	09/10/2009	Reimplement Jeff's change from 3/26/09. 
 * 							Lost in merge? 
 * 						 	modify getValuesFromMVFuncTransactionData()
 * 							defect 9961 Ver Defect_POS_F
 * J Rue		10/05/2009	Adjust offsets for RCCPI version T
 * 							modify all class variables,
 * 								getValuesFromSRFuncTransData(),
 * 								getValuesFromMVFuncTransactionData(),
 * 								getValuesFromTransactionData()
 * 							defect 10244 Ver Defect_POS_G
 * K Harrell	10/14/2009	Send Default PrivacyOptCd 
 * 							modify getValuesFromMVFuncTransactionData() 
 * 							defect 10246 Ver Defect_POS_G   
 * K Harrell	10/15/2009	Send Default PrivacyOptCd 
 * 							modify getValuesFromMVFuncTransactionData() 
 * 							defect 10246 Ver Defect_POS_G 
 * B Hargrove	10/22/2009	Consolidate U/V/T JavaDoc.
 * 							defect 10244 Ver Defect_POS_G
 * K Harrell	02/09/2010	Modified w/ refactor of TitleData 
 * 							 VTRTtlEmrgCd1/2 to PvtLawEnfVehCd/
 * 							 NonTtlGolfCartCd.
 * 							delete IL_TMV_VTRTTLEMRGCD1_LENGTH, 
 * 							 IL_TMV_VTRTTLEMRGCD2_LENGTH
 * 							add IL_TMV_PVTLAWENFVEHCD_LENGTH,
 * 							 IL_TMV_NONTTLGOLFCARTCD_LENGTH
 * 							modify getValuesFromMVFunctionTransaction() 	
 * 							defect 10366 Ver POS_640
 * M Reyes		03/03/2010	Copy version T to version U for MF
 * 							MF version U. Modified All.
 * 							defect 10378 Ver POS_640
 * M Reyes		04/06/2010	add IL_TMV_VTRTTLEMRGCD3_LENGTH,
 * 							IL_TMV_VTRTTLEMRGCD4_LENGTH,
 * 							IL_TMV_RECPNTEMAIL_LENGTH,
 * 							IL_TMV_SOREELECTIONINDI_LENGTH,
 * 							IL_TSR_RESRVREASNCD_LENGTH,
 * 							IL_TSR_MKTNGALLOWDINDI_LENGTH,
 * 							IL_TSR_PLTVALIDITYTERM_LENGTH,
 * 							IL_TSR_PLTSOLDMOS_LENGTH,
 * 							IL_TSR_ITRNTTRACENO_LENGTH, 
 * 							IL_TSR_AUCTNPLTINDI_LENGTH,
 * 							IL_TSR_AUCTNPDAMT_LENGTH
 * 							Removed IL_TMV_REGPLTAGE_LENGTH & 
 * 							IL_TMV_DLRGDN_LENGTH. 
 * 							Modified IL_TMV_DLRGDN_LENGTH & 
 * 							IL_TSR_PLTOWNRDLRGDN_LENGTH
 * 							defect 10389 Ver POS_640
 * M Reyes		04/07/2010	Modified getvaluesfromMVFuncTransactionData
 * 							& getvaluesfromSRFuncTransData()
 * 							defect 10389 Ver POS_640
 * K Harrell	06/15/2010	add IL_TMV_EMAILRENWLREQCD_LENGTH
 * 							delete IL_TMV_SOREELECTIONINDI_LENGTH	
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 10508 Ver 6.5.0		
 * K Harrell	06/18/2010	add IL_T_TRANSPRMT_OFFSET,
 *							 IL_T_TRANSPRMT_END 
 * 							 IL_PT_TRANSCD_LENGTH,
 * 							 IL_PT_OFCISSUANCENO_LENGTH,I
 * 							 IL_PT_TRANSWSID_LENGTH,
 *							 IL_PT_TRANSAMDATE_LENGTH,
 *							 IL_PT_TRANSTIME_LENGTH,
 *							 IL_PT_TRANSEMPID_LENGTH,
 *							 IL_PT_PRMTISSUANCEID_LENGTH,
 *							 IL_PT_CUSTLSTNAME_LENGTH,
 *							 IL_PT_CUSTFSTNAME_LENGTH,
 *							 IL_PT_CUSTMINAME_LENGTH,
 *							 IL_PT_CUSTBSNNAME_LENGTH,
 *							 IL_PT_CUSTST_LENGTH,
 *							 IL_PT_CUSTCITY_LENGTH,
 *							 IL_PT_CUSTSTATE_LENGTH,
 *							 IL_PT_CUSTZPCD_LENGTH,
 *							 IL_PT_CUSTZPCDP4_LENGTH,
 *							 IL_PT_CUSTCNTRY_LENGTH,
 *							 IL_PT_CUSTEMAIL_LENGTH,
 *							 IL_PT_CUSTPHONE_LENGTH,
 *							 IL_PT_PRMTNO_LENGTH,
 *							 IL_PT_ITMCD_LENGTH,
 *							 IL_PT_ACCTITMCD_LENGTH,
 *							 IL_PT_PRMTPDAMT_LENGTH,
 *							 IL_PT_EFFDATE_LENGTH,
 *							 IL_PT_EFFTIME_LENGTH,
 *							 IL_PT_EXPDATE_LENGTH,
 *							 IL_PT_EXPTIME_LENGTH,
 *							 IL_PT_VEHBDYTYPE_LENGTH,
 *							 IL_PT_VEHMK_LENGTH,
 *							 IL_PT_VEHMODLYR_LENGTH,
 *							 IL_PT_VIN_LENGTH,
 *							 IL_PT_VEHREGCNTRY_LENGTH,
 *							 IL_PT_VEHREGSTATE_LENGTH,
 *							 IL_PT_VEHREGPLTNO_LENGTH,
 *							 IL_PT_ONETRIPPNT_LENGTH,
 *							 IL_PT_MFDOWN_LENGTH, 
 *							 IL_TT_ADDPRMTRECINDI_LENGTH,
 *							 IL_TT_DELPRMTRECINDI_LENGTH,
 *							 IL_TSR_ELECTIONPNDNGINDI_LENGTH
 * 							add getValuesFromPermitTransactionData(),
 * 							appendToBuffer()  (4)
 * 							modify IL_T_TRANPYMT_OFFSET, 
 * 							  IL_T_LOGFUNC_OFFSET
 * 							modify getValuesFromTransactionData(),
 * 							 getValuesFromSRFuncTransData(), 
 * 							 formatSendTrans()
 *							defect 10492 Ver 6.5.0  	
 * K Harrell	06/23/2010	delete IL_TT_CUSTLSTNAME_LENGTH,
 * 							 IL_TT_CUSTFSTNAME_LENGTH,
 * 							 IL_TT_CUSTMINAME_LENGTH
 * 							modify handling of PhoneNo to pad w/ '0' as 
 * 							numeric on MF.   
 * 							modify getValuesFromPermitTransactionData()
 * 							defect 10492 Ver 6.5.0 
 * M Reyes		10/06/2010	Copy version V to version T for MF
 * 							MF version T. Modified All.
 * 							defect 10595 Ver POS_660
 * M Reyes		10/06/2010	add IL_TMV_TTLEXMNINDI_LENGTH
 * 							modify IL_TTP_PYMNTTYPEAMT_LENGTH
 * 							modify getValuesFromMVFuncTransationData()
 * 							defect 10595 Ver POS_660
 * M Reyes		01/03/2011	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver 6.7.0
 * M Reyes		01/10/2011	modify IL_TFF_SUBCONID_LENGTH,
 * 							IL_TMV_SUBCONID_LENGTH
 * 							add IL_TMV_VEHMJRCOLORCD_LENGTH,
 * 							IL_TMV_VEHMNRCOLORCD_LENGTH
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 10710 Ver 6.7.0 
 * B Woodson	06/10/2011  modify getValuesFromTransactionPaymentData
 *                          defect 10783 Ver 6.8.0	
 * K Harrell	11/01/2011	Copy version U to V for MF Version V 
 * 							add IL_TMV_ETTLPRNTDATE_LENGTH
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 11045 Ver 6.9.0
 * K Harrell	11/01/2011	Correct data assignment for Emissions  
 * 							Sales Tax  
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 10969 Ver 6.9.0                           						
 * B Woodson	01/20/2012	Copy version V to T for MF Version T
 *  						add IL_TMV_SURVSHPRIGHTSNAME1_LENGTH,
 *  						 IL_TMV_SURVSHPRIGHTSNAME2_LENGTH,
 *  						 IL_TMV_ADDLSURVIVORINDI_LENGTH,
 *  						 IL_TMV_EXPORTINDI_LENGTH,
 *  						 IL_TMV_SALVINDI_LENGTH
 * 							modify getValuesFromMVFuncTransactionData()
 * 							defect 11251 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Formats SendTrans Data for Version V.
 *
 * @version	6.10.0 			01/20/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		10/09/2006 07:45:00
 */

public class MfbaSendTransT
{
	private char cchBufferInt = '0';
	//	private char cchBufferChar = ' ';

	//	define all constants 
	private static final int IL_T_NO_OF_OBJECTS_LENGTH = 2;

	// The offsets section deals with the counts section of data.
	// see TRANS_BASIC_BUFF_DESC_AREA.
	//Offsets for TRANS
	private static final int IL_T_TRANS_OFFSET = 8;
	private static final int IL_T_TRANS_END =
		IL_T_TRANS_OFFSET + IL_T_NO_OF_OBJECTS_LENGTH;

	//Offsets for MVFUNC
	private static final int IL_T_MVFUNC_OFFSET = 18;
	private static final int IL_T_MVFUNC_END =
		IL_T_MVFUNC_OFFSET + IL_T_NO_OF_OBJECTS_LENGTH;

	//Offsets for SRFUNC
	private static final int IL_T_SRFUNC_OFFSET = 28;
	private static final int IL_T_SRFUNC_END =
		IL_T_SRFUNC_OFFSET + IL_T_NO_OF_OBJECTS_LENGTH;

	//Offsets for INVFUNV
	private static final int IL_T_INVFUNC_OFFSET = 38;
	private static final int IL_T_INVFUNC_END =
		IL_T_INVFUNC_OFFSET + IL_T_NO_OF_OBJECTS_LENGTH;

	//Offsets for FUNDFUNC
	private static final int IL_T_FUNDFUNC_OFFSET = 48;
	private static final int IL_T_FUNDFUNC_END =
		IL_T_FUNDFUNC_OFFSET + IL_T_NO_OF_OBJECTS_LENGTH;

	//Offsets for TRINVDTL
	private static final int IL_T_TRINVDTL_OFFSET = 58;
	private static final int IL_T_TRINVDTL_END =
		IL_T_TRINVDTL_OFFSET + IL_T_NO_OF_OBJECTS_LENGTH;

	//Offsets for TRFDSDTL
	private static final int IL_T_TRFDSDTL_OFFSET = 68;
	private static final int IL_T_TRFDSDTL_END =
		IL_T_TRFDSDTL_OFFSET + IL_T_NO_OF_OBJECTS_LENGTH;

	// defect 10492 
	//Offsets for PRMT 
	private static final int IL_T_TRANSPRMT_OFFSET = 78;
	private static final int IL_T_TRANSPRMT_END =
		IL_T_TRANSPRMT_OFFSET + IL_T_NO_OF_OBJECTS_LENGTH;

	// Offsets for TRAN_PYMNT
	private static final int IL_T_TRANPYMT_OFFSET = 88;
	private static final int IL_T_TRANPYMT_END =
		IL_T_TRANPYMT_OFFSET + IL_T_NO_OF_OBJECTS_LENGTH;

	// Offsets for LOG_FUNC
	private static final int IL_T_LOGFUNC_OFFSET = 98;
	private static final int IL_T_LOGFUNC_END =
		IL_T_LOGFUNC_OFFSET + IL_T_NO_OF_OBJECTS_LENGTH;
	// end defect 10492 

	////////////////////////////////////////////////////////////////////
	//define the lengths for Funds_Detail
	/**
	 * Trans Code for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is TRANSCD.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TFD_TRANSCD_LENGTH = 6;
	/**
	 * Office for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is OFCISSUANCENO
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TFD_OFCISSUANCENO_LENGTH = 3;
	/**
	 * Trans Workstation Id for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is TRANSWSID.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TFD_TRANSWSID_LENGTH = 3;
	/**
	 * Trans AM Date for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is TRANAMDATE.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TFD_TRANSAMDATE_LENGTH = 5;
	/**
	 * Trans Time for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is TRANTIME.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TFD_TRANSTIME_LENGTH = 6;
	/**
	 * Account Item Code for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is ACCTITMCD.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TFD_ACCTITMCD_LENGTH = 8;
	// defect 10389
	/**
	 * Item Price for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is ITMPRICE.
	 * 
	 * <p>Length is 11.
	 */
	private static final int IL_TFD_ITMPRICE_LENGTH = 11;
	//	end defect 10389
	/**
	 * Funds Reporting Date for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is FUNDSRPTDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TFD_FUNDSRPTDATE_LENGTH = 8;
	/**
	 * Reporting Date for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is RPTNGDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TFD_RPTNGDATE_LENGTH = 8;
	/**
	 * Funds Category for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is FUNDSCAT.
	 * 
	 * <p>Length is 14.
	 */
	private static final int IL_TFD_FUNDSCAT_LENGTH = 14;
	/**
	 * Funds Receiving Entity for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is FUNDSRCVNGENT.
	 * 
	 * Length is 9.
	 */
	private static final int IL_TFD_FUNDSRCVNGENT_LENGTH = 9;
	/**
	 * Funds Received Amount for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is FUNDSRCVDAMT.
	 * 
	 * <p>Length is 11.
	 */
	private static final int IL_TFD_FUNDSRCVDAMT_LENGTH = 11;
	/**
	 * Item Quantity for Trans Funds Detail.
	 * 
	 * <p>Mainframe field is ITMQTY.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TFD_ITMQTY_LENGTH = 4;

	////////////////////////////////////////////////////////////////////
	//define the lengths for Fund_Func
	/**
	 * Trans Code for Fund Func.
	 * 
	 * <p>Mainframe field is TRANSCD.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TFF_TRANSCD_LENGTH = 6;
	/**
	 * Office for Fund Func.
	 * 
	 * <p>Mainframe field is OFCISSUANCENO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TFF_OFCISSUANCENO_LENGTH = 3;
	/**
	 * Trans Workstation Id for Fund Func.
	 * 
	 * <p>Mainframe field is TRANSWSID.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TFF_TRANSWSID_LENGTH = 3;
	/**
	 * Trans AM Date for Fund Func.
	 * 
	 * <p>Mainframe field is TRANSAMDATE.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TFF_TRANSAMDATE_LENGTH = 5;
	/**
	 * Trans Time for Fund Func.
	 * 
	 * <p>Mainframe field is TRANSTIME.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TFF_TRANSTIME_LENGTH = 6;
	/**
	 * Office Issuance Code for Fund Func.
	 * 
	 * <p>Mainframe field is OFCISSUANCECD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TFF_OFCISSUANCECD_LENGTH = 1;
	/**
	 * Voided Trans Indicator for Fund Func.
	 * 
	 * <p>Mainframe field is VOIDEDTRANSINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TFF_VOIDEDTRANSINDI_LENGTH = 1;
	/**
	 * Subcon Issue Date for Fund Func.
	 * 
	 * <p>Mainframe field is SUBCONISSUEDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TFF_SUBCONISSUEDATE_LENGTH = 8;
	// defect 10710
	/**
	 * Subcon Id for Fund Func.
	 * 
	 * <p>Mainframe field is SUBCONID.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TFF_SUBCONID_LENGTH = 5;
	//	end defect 10710
	/**
	 * Apprended Controller County Number for Fund Func.
	 * 
	 * <p>Mainframe field is APPRNDCOMPTCNTYNO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TFF_APPRNDCOMPTCNTYNO_LENGTH = 3;
	/**
	 * Check Number for Fund Func.
	 * 
	 * <p>Mainframe field is CKNO.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TFF_CKNO_LENGTH = 7;
	/**
	 * Funds Payment Amount for Fund Func.
	 * 
	 * <p>Mainframe field is FUNDSPYMNTAMT.
	 * 
	 * <p>Length is 11.
	 */
	private static final int IL_TFF_FUNDSPYMNTAMT_LENGTH = 11;
	/**
	 * Funds Payment Date for Fund Func.
	 * 
	 * <p>Mainframe field is FUNDSPYMNTDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TFF_FUNDSPYMNTDATE_LENGTH = 8;
	/**
	 * Funds Adjustment Reason for Fund Func.
	 * 
	 * <p>Mainframe field is FUNDSADJREASN.
	 * 
	 * <p>Length is 40.
	 */
	private static final int IL_TFF_FUNDSADJREASN_LENGTH = 40;
	/**
	 * Comptroller County Number for Fund Func.
	 * 
	 * <p>Mainframe field is COMPTCNTYNO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TFF_COMPTCNTYNO_LENGTH = 3;
	/**
	 * Account Number Code for Fund Func.
	 * 
	 * <p>Mainframe field is ACCNTNOCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TFF_ACCNTNOCD_LENGTH = 1;
	/**
	 * Trace Number for Fund Func.
	 * 
	 * <p>Mainframe field is TRACENO.
	 * 
	 * <p>Length is 9.
	 */
	private static final int IL_TFF_TRACENO_LENGTH = 9;
	/**
	 * Internet Trace Number for Fund Func.
	 * 
	 * <p>Mainframe field is ITRNTTRACENO.
	 * 
	 * <p>Length is 15.
	 */
	private static final int IL_TFF_ITRNTTRACENO_LENGTH = 15;

	////////////////////////////////////////////////////////////////////
	//define lengths for Inv_Detail
	/**
	 * Trans Code for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is TRANSCD.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TID_TRANSCD_LENGTH = 6;
	/**
	 * Office for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is OFCISSUANCENO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TID_OFCISSUANCENO_LENGTH = 3;
	/**
	 * Trans Workstation Id for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is TRANSWSID.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TID_TRANSWSID_LENGTH = 3;
	/**
	 * Trans AM Date for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is TRANSAMDATE.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TID_TRANSAMDATE_LENGTH = 5;
	/**
	 * Trans Time for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is TRANSTIME.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TID_TRANSTIME_LENGTH = 6;
	/**
	 * Item Code for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is ITMCD.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TID_ITMCD_LENGTH = 8;
	/**
	 * Inventory Item Year for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is INVITMYR.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TID_INVITMYR_LENGTH = 4;
	/**
	 * Inventory Item End Number for Trans Inventory Detail.
	 * 
	 * <p>Matches up with INVITMNO.
	 * 
	 * <p>Mainframe field is INVENDNO.
	 * 
	 * <p>Length is 10.
	 */
	private static final int IL_TID_INVENDNO_LENGTH = 10;
	/**
	 * Inventory Item Quantity for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is INVQTY.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TID_INVQTY_LENGTH = 7;
	/**
	 * Inventory Item Reorder Level for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is INVITMREORDERLVL.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TID_INVITMREORDERLVL_LENGTH = 7;
	/**
	 * Office Inventory Item Reorder Level for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is OFCINVREORDERQTY.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TID_OFCINVREORDERQTY_LENGTH = 7;
	/**
	 * Inventory Item Tracking Office for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is INVITMTRCKNGOFC.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TID_INVITMTRCKNGOFC_LENGTH = 3;
	/**
	 * Inventory Item Number for Trans Inventory Detail.
	 * 
	 * <p>Matches up with INVENDNO.
	 * 
	 * <p>Mainframe field is INVITMNO.
	 * 
	 * <p>Length is 10.
	 */
	private static final int IL_TID_INVITMNO_LENGTH = 10;
	/**
	 * Inventory Location Id Code for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is INVLOCIDCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TID_INVLOCIDCD_LENGTH = 1;
	/**
	 * Delete Reason Code for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is DELINVREASNCD.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TID_DELINVREASNCD_LENGTH = 2;
	/**
	 * Entitity for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is INVID.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TID_INVID_LENGTH = 7;
	/**
	 * Detail Status Code for Trans Inventory Detail.
	 * 
	 * <p>Mainframe field is DETAILSTATUSCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TID_DETAILSTATUSCD_LENGTH = 1;

	////////////////////////////////////////////////////////////////////
	//define all lengths for Inv_Func
	/**
	 * Trans Code for Inventory Func.
	 * 
	 * <p>Mainframe field is TRANSCD.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TIF_TRANSCD_LENGTH = 6;
	/**
	 * Office for Inventory Func.
	 * 
	 * <p>Mainframe field is OFCISSUANCENO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TIF_OFCISSUANCENO_LENGTH = 3;
	/**
	 * Trans Workstation Id for Inventory Func.
	 * 
	 * <p>Mainframe field is TRANSWSID.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TIF_TRANSWSID_LENGTH = 3;
	/**
	 * Trans AM Date for Inventory Func.
	 * 
	 * <p>Mainframe field is TRANSAMDATE.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TIF_TRANSAMDATE_LENGTH = 5;
	/**
	 * Trans Time for Inventory Func.
	 * 
	 * <p>Mainframe field is TRANSTIME.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TIF_TRANSTIME_LENGTH = 6;
	/**
	 * Office Issuance Code for Inventory Func.
	 * 
	 * <p>Mainframe field is OFCISSUANCECD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TIF_OFCISSUANCECD_LENGTH = 1;
	/**
	 * Voided Trans Indicator for Inventory Func.
	 * 
	 * <p>Mainframe field is VOIDEDTRANSINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TIF_VOIDEDTRANSINDI_LENGTH = 1;
	/**
	 * Employee Id for Inventory Func.
	 * 
	 * <p>Mainframe field is EMPID.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TIF_EMPID_LENGTH = 7;
	/**
	 * Subcon Issue Date for Inventory Func.
	 * 
	 * <p>Mainframe field is SUBCONISSUEDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TIF_SUBCONISSUEDATE_LENGTH = 8;
	/**
	 * Invoice Number for Inventory Func.
	 * 
	 * <p>Mainframe field is INVCNO.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TIF_INVCNO_LENGTH = 6;
	/**
	 * Invoice Correct Indicator for Inventory Func.
	 * 
	 * <p>Mainframe field is INVCCORRINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TIF_INVCCORRINDI_LENGTH = 1;

	////////////////////////////////////////////////////////////////////
	//define all lengths Logon_Func
	/**
	 * Office for Logon Func.
	 * 
	 * <p>Mainframe field is OFCISSUANCENO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TLF_OFCISSUANCENO_LENGTH = 3;
	/**
	 * Workstation Id for Logon Func.
	 * 
	 * <p>Mainframe field is WSID.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TLF_WSID_LENGTH = 3;
	/**
	 * SubStation Id for Logon Func.
	 * 
	 * <p>Mainframe field is SUBSTAID.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TLF_SUBSTAID_LENGTH = 2;
	/**
	 * Employee Id for Logon Func.
	 * 
	 * <p>Mainframe field is EMPID.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TLF_EMPID_LENGTH = 7;
	/**
	 * Logon Date for Logon Func.
	 * 
	 * <p>Mainframe field is SYSDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TLF_SYSDATE_LENGTH = 8;
	/**
	 * Logon Time for Logon Func.
	 * 
	 * <p>Mainframe field is SYSTIME.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TLF_SYSTIME_LENGTH = 6;
	/**
	 * Logon Successful Indicator for Logon Func.
	 * 
	 * <p>Mainframe field is SUCCESSFULINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TLF_SUCCESSFULINDI_LENGTH = 1;
	/**
	 * Trans Posted to Lan Indicator for Logon Func.
	 * 
	 * <p>Mainframe field is TRANSPOSTEDLANINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TLF_TRANSPOSTEDLANINDI_LENGTH = 1;
	/**
	 * Trans Posted to MainFrame Indicator for Logon Func.
	 * 
	 * <p>Mainframe field is TRANSPOSTEDMFINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TLF_TRANSPOSTEDMFINDI_LENGTH = 1;

	////////////////////////////////////////////////////////////////////
	//define all lengths for MV_Func
	/**
	 * Trans Code for MV Func.
	 * 
	 * <p>Mainframe field is TRANSCD.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TMV_TRANSCD_LENGTH = 6;
	/**
	 * Office for MV Func.
	 * 
	 * <p>Mainframe field is OFCISSUANCENO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TMV_OFCISSUANCENO_LENGTH = 3;
	/**
	 * Trans Workstation Id for MV Func.
	 * 
	 * <p>Mainframe field is TRANSWSID.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TMV_TRANSWSID_LENGTH = 3;
	/**
	 * Trans AM Date for MV Func.
	 * 
	 * <p>Mainframe field is TRANSAMDATE.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TMV_TRANSAMDATE_LENGTH = 5;
	/**
	 * Trans Time for MV Func.
	 * 
	 * <p>Mainframe field is TRANSTIME.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TMV_TRANSTIME_LENGTH = 6;
	/**
	 * Office Issuance Code for MV Func.
	 * 
	 * <p>Mainframe field is OFCISSUANCECD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_OFCISSUANCECD_LENGTH = 1;
	/**
	 * Substation Id for MV Func.
	 * 
	 * <p>Mainframe field is SUBSTAID.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_SUBSTAID_LENGTH = 2;
	/**
	 * Trans Employee Id for MV Func.
	 * 
	 * <p>Mainframe field is TRANSEMPID.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TMV_TRANSEMPID_LENGTH = 7;
	/**
	 * MainFrame Down Code for MV Func.
	 * 
	 * <p>Mainframe field is MFDWNCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_MFDWNCD_LENGTH = 1;
	/**
	 * Voided Trans Indicator for MV Func.
	 * 
	 * <p>Mainframe field is VOIDEDTRANSINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_VOIDEDTRANSINDI_LENGTH = 1;
	/**
	 * Custonmer Actual Regis Fee for MV Func.
	 * 
	 * <p>Mainframe field is CUSTACTULREGFEE.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TMV_CUSTACTULREGFEE_LENGTH = 6;
	/**
	 * Notifying City for MV Func.
	 * 
	 * <p>Mainframe field is NOTFYNGCITY.
	 * 
	 * <p>Length is 19.
	 */
	private static final int IL_TMV_NOTFYNGCITY_LENGTH = 19;
	/**
	 * Resident Comptroller County Number for MV Func.
	 * 
	 * <p>Mainframe field is RESCOMPTCNTYNO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TMV_RESCOMPTCNTYNO_LENGTH = 3;

	// start of MV Func RegisData
	/**
	 * Replica Vehicle Make for MV Func.
	 * 
	 * <p>Mainframe field is REPLICAVEHMK.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_REPLICAVEHMK_LENGTH = 4;
	/**
	 * Replica Vehicle Model Year for MV Func.
	 * 
	 * <p>Mainframe field is REPLICAVEHMODLYR.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_REPLICAVEHMODLYR_LENGTH = 4;
	/**
	 * Regis Class Code for MV Func.
	 * 
	 * <p>Mainframe field is REGCLASSCD.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TMV_REGCLASSCD_LENGTH = 3;
	/**
	 * Regis Effective Date for MV Func.
	 * 
	 * <p>Mainframe field is REGEFFDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_REGEFFDATE_LENGTH = 8;
	/**
	 * Regis Expiration Month for MV Func.
	 * 
	 * <p>Mainframe field is REGEXPMO.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_REGEXPMO_LENGTH = 2;
	/**
	 * Regis Expiration Year for MV Func.
	 * 
	 * <p>Mainframe field is REGEXPYR.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_REGEXPYR_LENGTH = 4;
	// defect 10389
	// removed Plate Age
	/**
	 * Regis Plate Age for MV Func.
	 * 
	 * <p>Mainframe field is REGPLTAGE.
	 * 
	 * <p>Length is 2.
	 */
	//private static final int IL_TMV_REGPLTAGE_LENGTH = 2;
	// end defect 10389
	/**
	 * Regis Plate Code for MV Func.
	 * 
	 * <p>Mainframe field is REGPLTCD.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_REGPLTCD_LENGTH = 8;
	/**
	 * Regis Plate Number for MV Func.
	 * 
	 * <p>Mainframe field is REGPLTNO.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TMV_REGPLTNO_LENGTH = 7;
	/**
	 * Regis Plate Owner Name for MV Func.
	 * 
	 * <p>Mainframe field is REGPLTOWNRNAME.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_REGPLTOWNRNAME_LENGTH = 30;
	/**
	 * Regis Refund Amount for MV Func.
	 * 
	 * <p>Mainframe field is REGREFAMT.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TMV_REGREFAMT_LENGTH = 6;
	/**
	 * Regis Sticker Code for MV Func.
	 * 
	 * <p>Mainframe field is REGSTKRCD.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_REGSTKRCD_LENGTH = 8;

	// start of MV Func RenwlData
	/**
	 * Recipient Name for MV Func.
	 * 
	 * <p>Mainframe field is RECPNTNAME.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_RECPNTNAME_LENGTH = 30;
	/**
	 * Recipient Last Name for MV Func.
	 * 
	 * <p>NOTE: Is this used?
	 * 
	 * <p>Mainframe field is RECPNTLSTNAME.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_RECPNTLSTNAME_LENGTH = 1;
	/**
	 * Recipient First Name for MV Func.
	 * 
	 * <p>NOTE: Is this used?
	 * 
	 * <p>Mainframe field is RECPNTFSTNAME.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_RECPNTFSTNAME_LENGTH = 1;
	/**
	 * Recipient Middle Initial for MV Func.
	 * 
	 * <p>NOTE: Is this used?
	 * 
	 * <p>Mainframe field is RECPNTMI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_RECPNTMI_LENGTH = 1;

	// start of MV Func RenwlAddr
	/**
	 * Renewal Mailing Address Street Line 1.
	 * 
	 * <p>Mainframe field is RENWLMAILNGST1.
	 * 
	 * <p>Length is 30.  
	 */
	private static final int IL_TMV_RENWLMAILNGST1_LENGTH = 30;
	/**
	 * Renewal Mailing Address Street Line 2.
	 * 
	 * <p>Mainframe field is RENWLMAILNGST2.
	 * 
	 * <p>Length is 30.  
	 */
	private static final int IL_TMV_RENWLMAILNGST2_LENGTH = 30;
	/**
	 * Renewal Mailing City.
	 * 
	 * <p>Mainframe field is RENWLMAILNGCITY.
	 * 
	 * <p>Length is 19.  
	 */
	private static final int IL_TMV_RENWLMAILNGCITY_LENGTH = 19;
	/**
	 * Renewal Mailing State.
	 * 
	 * <p>Mainframe field is RENWLMAILNGSTATE.
	 * 
	 * <p>Length is 2.  
	 */
	private static final int IL_TMV_RENWLMAILNGSTATE_LENGTH = 2;
	/**
	 * Renewal Mailing Zip Code.
	 * 
	 * <p>Mainframe field is RENWLMAILNGZPCD.
	 * 
	 * <p>Length is 5.  
	 */
	private static final int IL_TMV_RENWLMAILNGZPCD_LENGTH = 5;
	/**
	 * Renewal Mailing Zip Code Plus 4.
	 * 
	 * <p>Mainframe field is RENWLMAILNGZPCDP4.
	 * 
	 * <p>Length is 4.  
	 */
	private static final int IL_TMV_RENWLMAILNGZPCDP4_LENGTH = 4;
	/**
	 * Title Batch Number.
	 * 
	 * <p>Mainframe field is BATCHNO.
	 * 
	 * <p>Length is 10.  
	 */
	private static final int IL_TMV_BATCHNO_LENGTH = 10;
	/**
	 * Bonded Title Code.
	 * 
	 * <p>Mainframe field is BNDEDTTLCD.
	 * 
	 * <p>Length is 1.  
	 */
	private static final int IL_TMV_BNDEDTTLCD_LENGTH = 1;
	/**
	 * CCO Issue Date.
	 * 
	 * <p>Mainframe field is CCOISSUEDATE.
	 * 
	 * <p>Length is 8.  
	 */
	private static final int IL_TMV_CCOISSUEDATE_LENGTH = 8;
	/**
	 * Document Number.
	 * 
	 * <p>Mainframe field is DOCNO.
	 * 
	 * <p>Length is 17.  
	 */
	private static final int IL_TMV_DOCNO_LENGTH = 17;
	/**
	 * Document Type Code.
	 * 
	 * <p>Mainframe field is DOCTYPECD.
	 * 
	 * <p>Length is 2.  
	 */
	private static final int IL_TMV_DOCTYPECD_LENGTH = 2;
	/**
	 * Legal Restraint Number.
	 * 
	 * <p>Mainframe field is LEGALRESTRNTNO.
	 * 
	 * <p>Length is 7.  
	 */
	// defect 9961
	//	Field increase to 9 characters
	//private static final int IL_TMV_LEGALRESTRNTNO_LENGTH = 7;
	private static final int IL_TMV_LEGALRESTRNTNO_LENGTH = 9;
	// end defect 9961
	/**
	 * Other State or Country.
	 * 
	 * <p>Mainframe field is OTHRSTATECNTRY.
	 * 
	 * <p>Length is 2.  
	 */
	private static final int IL_TMV_OTHRSTATECNTRY_LENGTH = 2;
	/**
	 * Ownership Evidence Code.
	 * 
	 * <p>Mainframe field is OWNRSHPEVIDCD.
	 * 
	 * <p>Length is 2.  
	 */
	private static final int IL_TMV_OWNRSHPEVIDCD_LENGTH = 2;
	/**
	 * Previous Owner Name.
	 * 
	 * <p>Mainframe field is PREVOWNRNAME.
	 * 
	 * <p>Length is 24.  
	 */
	private static final int IL_TMV_PREVOWNRNAME_LENGTH = 24;
	/**
	 * Previous Owner City.
	 * 
	 * <p>Mainframe field is PREVOWNRCITY.
	 * 
	 * <p>Length is 11.
	 */
	private static final int IL_TMV_PREVOWNRCITY_LENGTH = 11;
	/**
	 * Previous Owner State.
	 * 
	 * <p>Mainframe field is PREVOWNRSTATE.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_PREVOWNRSTATE_LENGTH = 2;
	/**
	 * Trailer Type.
	 * 
	 * <p>Mainframe field is TRLRTYPE.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_TRLRTYPE_LENGTH = 1;
	/**
	 * Title Application Date.
	 * 
	 * <p>Mainframe field is TTLAPPLDATE.
	 * 
	 * <p>Length is 5.
	 * <br>Format is AM Date.
	 */
	private static final int IL_TMV_TTLAPPLDATE_LENGTH = 5;
	/**
	 * Title Processing Code.
	 * 
	 * <p>Mainframe field is TTLPROCSCD.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_TTLPROCSCD_LENGTH = 2;
	/**
	 * Old Document Number.
	 * 
	 * <p>Mainframe field is OLDDOCNO.
	 * 
	 * <p>Length is 17.
	 */
	private static final int IL_TMV_OLDDOCNO_LENGTH = 17;
	/**
	 * Title Rejection Date.
	 * 
	 * <p>Mainframe field is TTLREJCTNDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_TTLREJCTNDATE_LENGTH = 8;
	/**
	 * Title Rejection Office.
	 * 
	 * <p>Mainframe field is TTLREJCTNOFC.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TMV_TTLREJCTNOFC_LENGTH = 3;
	/**
	 * Title Issue Date.
	 * 
	 * <p>Mainframe field is TTLISSUEDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_TTLISSUEDATE_LENGTH = 8;
	/**
	 * Audit Trail Transaction Id.
	 * 
	 * <p>Mainframe field is AUDITTRAILTRANSID.
	 * 
	 * <p>Length is 17.
	 */
	private static final int IL_TMV_AUDITTRAILTRANSID_LENGTH = 17;
	/**
	 * Sales Tax Date.
	 * 
	 * <p>Mainframe field is SALESTAXDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_SALESTAXDATE_LENGTH = 8;
	/**
	 * Sales Tax Paid Amount.
	 * 
	 * <p>Mainframe field is SALESTAXPDAMT.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_SALESTAXPDAMT_LENGTH = 8;
	/**
	 * Sales Tax Category.
	 * 
	 * <p>Mainframe field is SALESTAXCAT.
	 * 
	 * <p>Length is 10.
	 */
	private static final int IL_TMV_SALESTAXCAT_LENGTH = 10;
	/**
	 * Tax Paid in anOther State.
	 * 
	 * <p>Mainframe field is TAXPDOTHRSTATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_TAXPDOTHRSTATE_LENGTH = 8;

	// Start of MV Func VehLoc
	/**
	 * Vehicle Location Street Line 1.
	 * 
	 * <p>Mainframe field is TTLVEHLOCST1.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_TTLVEHLOCST1_LENGTH = 30;
	/**
	 * Vehicle Location Street Line 2.
	 * 
	 * <p>Mainframe field is TTLVEHLOCST2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_TTLVEHLOCST2_LENGTH = 30;
	/**
	 * Vehicle Location City.
	 * 
	 * <p>Mainframe field is TTLVEHLOCCITY.
	 * 
	 * <p>Length is 19.
	 */
	private static final int IL_TMV_TTLVEHLOCCITY_LENGTH = 19;
	/**
	 * Vehicle Location State.
	 * 
	 * <p>Mainframe field is TTLVEHLOCSTATE.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_TTLVEHLOCSTATE_LENGTH = 2;
	/**
	 * Vehicle Location Zip Code.
	 * 
	 * <p>Mainframe field is TTLVEHLOCZPCD.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TMV_TTLVEHLOCZPCD_LENGTH = 5;
	/**
	 * Vehicle Location Zip Code Plus 4.
	 * 
	 * <p>Mainframe field is TTLVEHLOCZPCDP4.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_TTLVEHLOCZPCDP4_LENGTH = 4;

	// Start of MV Func VehData
	/**
	 * Tire Type Code.
	 * 
	 * <p>Mainframe field is TIRETYPECD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_TIRETYPECD_LENGTH = 1;
	/**
	 * Trade In Vehicle Year.
	 * 
	 * <p>Mainframe field is TRADEINVEHYR.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_TRADEINVEHYR_LENGTH = 4;
	/**
	 * Trade In Vehicle Make.
	 * 
	 * <p>Mainframe field is TRADEINVEHMK.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_TRADEINVEHMK_LENGTH = 4;
	/**
	 * Trade In Vehicle VIN.
	 * 
	 * <p>Mainframe field is TRADEINVEHVIN.
	 * 
	 * <p>Length is 22.
	 */
	private static final int IL_TMV_TRADEINVEHVIN_LENGTH = 22;
	/**
	 * Vehicle Body Type.
	 * 
	 * <p>Mainframe field is VEHBDYTYPE.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_VEHBDYTYPE_LENGTH = 2;
	/**
	 * Vehicle Body VIN.
	 * 
	 * <p>Mainframe field is VEHBDYTYPE.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_VEHBDYVIN_LENGTH = 22;
	/**
	 * Vehicle Carrying Capacity.
	 * 
	 * <p>Mainframe field is VEHCARYNGCAP.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TMV_VEHCARYNGCAP_LENGTH = 6;
	/**
	 * Vehicle Class Code.
	 * 
	 * <p>Mainframe field is VEHCLASSCD.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_VEHCLASSCD_LENGTH = 8;
	/**
	 * Vehicle Empty Weight.
	 * 
	 * <p>Mainframe field is VEHEMPTYWT.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TMV_VEHEMPTYWT_LENGTH = 6;
	/**
	 * Vehicle Gross Weight.
	 * 
	 * <p>Mainframe field is VEHGROSSWT.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TMV_VEHGROSSWT_LENGTH = 6;
	/**
	 * Vehicle Length.
	 * 
	 * <p>Mainframe field is VEHLNGTH.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_VEHLNGTH_LENGTH = 2;
	/**
	 * Vehicle Make.
	 * 
	 * <p>Mainframe field is VEHMK.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_VEHMK_LENGTH = 4;
	/**
	 * Vehicle Model.
	 * 
	 * <p>Mainframe field is VEHMODL.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TMV_VEHMODL_LENGTH = 3;
	/**
	 * Vehicle Model Year.
	 * 
	 * <p>Mainframe field is VEHMODLYR.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_VEHMODLYR_LENGTH = 4;
	/**
	 * Vehicle Odometer Brand.
	 * 
	 * <p>Mainframe field is VEHODMTRBRND.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_VEHODMTRBRND_LENGTH = 1;
	/**
	 * Vehicle Odometer Reading.
	 * 
	 * <p>Mainframe field is VEHODMTRREADNG.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TMV_VEHODMTRREADNG_LENGTH = 6;
	/**
	 * Vehicle Sales Price.
	 * 
	 * <p>Mainframe field is VEHSALESPRICE.
	 * 
	 * <p>Length is 10.
	 */
	private static final int IL_TMV_VEHSALESPRICE_LENGTH = 10;
	/**
	 * Vehicle Sold Date.
	 * 
	 * <p>Mainframe field is VEHSOLDDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_VEHSOLDDATE_LENGTH = 8;
	/**
	 * Vehicle Tonnage.
	 * 
	 * <p>Mainframe field is VEHTON.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_VEHTON_LENGTH = 4;
	/**
	 * Vehicle Trade In Allowance.
	 * 
	 * <p>Mainframe field is VEHTRADEINALLOWNCE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_VEHTRADEINALLOWNCE_LENGTH = 8;
	/**
	 * Vehicle Unit Number.
	 * 
	 * <p>Mainframe field is VEHUNITNO.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TMV_VEHUNITNO_LENGTH = 6;
	/**
	 * Vehicle VIN.
	 * 
	 * <p>Mainframe field is VIN.
	 * 
	 * <p>Length is 22.
	 */
	private static final int IL_TMV_VIN_LENGTH = 22;

	// Start of MV Func OwnrData
	/**
	 * Vehicle Owner Tax Id.
	 * 
	 * <p>Mainframe field is OWNRID.
	 * 
	 * <p>Length is 9.
	 */
	private static final int IL_TMV_OWNRID_LENGTH = 9;
	/**
	 * Vehicle Owner Title Name Line 1.
	 * 
	 * <p>Mainframe field is OWNRTTLNAME1.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_OWNRTTLNAME1_LENGTH = 30;
	/**
	 * Vehicle Owner Title Name Line 2.
	 * 
	 * <p>Mainframe field is OWNRTTLNAME2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_OWNRTTLNAME2_LENGTH = 30;
	/**
	 * Vehicle Owner Title Last Name.
	 * 
	 * <p>TODO This field is not used.
	 * 
	 * <p>Mainframe field is OWNRLSTNAME.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_OWNRLSTNAME_LENGTH = 1;
	/**
	 * Vehicle Owner Title First Name.
	 * 
	 * <p>TODO This field is not used.
	 * 
	 * <p>Mainframe field is OWNRFSTNAME.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_OWNRFSTNAME_LENGTH = 1;
	/**
	 * Vehicle Owner Title Middle Initial.
	 * 
	 * <p>TODO This field is not used.
	 * 
	 * <p>Mainframe field is OWNRMI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_OWNRMI_LENGTH = 1;
	/**
	 * Vehicle Owner Title Street Line 1.
	 * 
	 * <p>Mainframe field is OWNRST1.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_OWNRST1_LENGTH = 30;
	/**
	 * Vehicle Owner Title Street Line 2.
	 * 
	 * <p>Mainframe field is OWNRST2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_OWNRST2_LENGTH = 30;
	/**
	 * Vehicle Owner Title City.
	 * 
	 * <p>Mainframe field is OWNRCITY.
	 * 
	 * <p>Length is 19.
	 */
	private static final int IL_TMV_OWNRCITY_LENGTH = 19;
	/**
	 * Vehicle Owner Title State.
	 * 
	 * <p>Mainframe field is OWNRSTATE.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_OWNRSTATE_LENGTH = 2;
	/**
	 * Vehicle Owner Title ZipCode.
	 * 
	 * <p>Mainframe field is OWNRZPCD.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TMV_OWNRZPCD_LENGTH = 5;
	/**
	 * Vehicle Owner Title ZipCode Plus 4.
	 * 
	 * <p>Mainframe field is OWNRZPCDP4.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_OWNRZPCDP4_LENGTH = 4;
	/**
	 * Vehicle Owner Title Country.
	 * 
	 * <p>Mainframe field is OWNRCNTRY.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_OWNRCNTRY_LENGTH = 4;

	// start of MV Func LienData1
	/**
	 * LienHolder 1 Lien Date.
	 * 
	 * <p>Mainframe field is LIENHLDR1DATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_LIENHLDR1DATE_LENGTH = 8;
	/**
	 * LienHolder 1 Name Line 1.
	 * 
	 * <p>Mainframe field is LIENHLDR1NAME1.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_LIENHLDR1NAME1_LENGTH = 30;
	/**
	 * LienHolder 1 Name Line 2.
	 * 
	 * <p>Mainframe field is LIENHLDR1NAME2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_LIENHLDR1NAME2_LENGTH = 30;
	/**
	 * LienHolder 1 Street Line 1.
	 * 
	 * <p>Mainframe field is LIENHLDR1ST1.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_LIENHLDR1ST1_LENGTH = 30;
	/**
	 * LienHolder 1 Street Line 2.
	 * 
	 * <p>Mainframe field is LIENHLDR1ST2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_LIENHLDR1ST2_LENGTH = 30;
	/**
	 * LienHolder 1 City.
	 * 
	 * <p>Mainframe field is LIENHLDR1CITY.
	 * 
	 * <p>Length is 19.
	 */
	private static final int IL_TMV_LIENHLDR1CITY_LENGTH = 19;
	/**
	 * LienHolder 1 State.
	 * 
	 * <p>Mainframe field is LIENHLDR1STATE.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_LIENHLDR1STATE_LENGTH = 2;
	/**
	 * LienHolder 1 Zip Code.
	 * 
	 * <p>Mainframe field is LIENHLDR1ZPCD.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TMV_LIENHLDR1ZPCD_LENGTH = 5;
	/**
	 * LienHolder 1 Zip Code Plus 4.
	 * 
	 * <p>Mainframe field is LIENHLDR1ZPCDP4.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_LIENHLDR1ZPCDP4_LENGTH = 4;
	/**
	 * LienHolder 1 Country.
	 * 
	 * <p>Mainframe field is LIENHLDR1CNTRY.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_LIENHLDR1CNTRY_LENGTH = 4;
	/**
	 * LienHolder 1 Perm Lien Holder ID.
	 * 
	 * <p>Mainframe field is PERMLIEN1HLDRID.
	 * 
	 * <p>Length is 11.
	 */
	private static final int IL_TMV_PERMLIEN1HLDRID_LENGTH = 11;
	/**
	 * LienHolder 1 Release date.
	 * 
	 * <p>Mainframe field is LIENRLSEDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_LIEN1RLSEDATE_LENGTH = 8;

	// Start of MV Func LienData2
	/**
	 * LienHolder 2 Lien Date.
	 * 
	 * <p>Mainframe field is LIENHLDR2DATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_LIENHLDR2DATE_LENGTH = 8;
	/**
	 * LienHolder 2 Name Line 1.
	 * 
	 * <p>Mainframe field is LIENHLDR2NAME1.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_LIENHLDR2NAME1_LENGTH = 30;
	/**
	 * LienHolder 2 Name Line 2.
	 * 
	 * <p>Mainframe field is LIENHLDR2NAME2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_LIENHLDR2NAME2_LENGTH = 30;
	/**
	 * LienHolder 2 Street Line 1.
	 * 
	 * <p>Mainframe field is LIENHLDR2ST1.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_LIENHLDR2ST1_LENGTH = 30;
	/**
	 * LienHolder 2 Street Line 2.
	 * 
	 * <p>Mainframe field is LIENHLDR2ST2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_LIENHLDR2ST2_LENGTH = 30;
	/**
	 * LienHolder 2 City.
	 * 
	 * <p>Mainframe field is LIENHLDR2CITY.
	 * 
	 * <p>Length is 19.
	 */
	private static final int IL_TMV_LIENHLDR2CITY_LENGTH = 19;
	/**
	 * LienHolder 2 State.
	 * 
	 * <p>Mainframe field is LIENHLDR2STATE.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_LIENHLDR2STATE_LENGTH = 2;
	/**
	 * LienHolder 2 Zip Code.
	 * 
	 * <p>Mainframe field is LIENHLDR2ZPCD.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TMV_LIENHLDR2ZPCD_LENGTH = 5;
	/**
	 * LienHolder 2 Zip Code Plus 4.
	 * 
	 * <p>Mainframe field is LIENHLDR2ZPCDP4.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_LIENHLDR2ZPCDP4_LENGTH = 4;
	/**
	 * LienHolder 2 Country.
	 * 
	 * <p>Mainframe field is LIENHLDR2CNTRY.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_LIENHLDR2CNTRY_LENGTH = 4;
	/**
	 * LienHolder 2 Perm Lien Holder ID.
	 * 
	 * <p>Mainframe field is PERMLIENHLDRID.
	 * 
	 * <p>Length is 11.
	 */
	private static final int IL_TMV_PERMLIEN2HLDRID_LENGTH = 11;
	/**
	 * LienHolder 2 Release date.
	 * 
	 * <p>Mainframe field is LIENRLSEDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_LIEN2RLSEDATE_LENGTH = 8;

	// Start of MV Func LienData3
	/**
	 * LienHolder 3 Lien Date.
	 * 
	 * <p>Mainframe field is LIENHLDR3DATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_LIENHLDR3DATE_LENGTH = 8;
	/**
	 * LienHolder 3 Name Line 1.
	 * 
	 * <p>Mainframe field is LIENHLDR3NAME1.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_LIENHLDR3NAME1_LENGTH = 30;
	/**
	 * LienHolder 3 Name Line 2.
	 * 
	 * <p>Mainframe field is LIENHLDR3NAME2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_LIENHLDR3NAME2_LENGTH = 30;
	/**
	 * LienHolder 3 Street Line 1.
	 * 
	 * <p>Mainframe field is LIENHLDR3ST1.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_LIENHLDR3ST1_LENGTH = 30;
	/**
	 * LienHolder 3 Street Line 2.
	 * 
	 * <p>Mainframe field is LIENHLDR3ST2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TMV_LIENHLDR3ST2_LENGTH = 30;
	/**
	 * LienHolder 3 City.
	 * 
	 * <p>Mainframe field is LIENHLDR3CITY.
	 * 
	 * <p>Length is 19.
	 */
	private static final int IL_TMV_LIENHLDR3CITY_LENGTH = 19;
	/**
	 * LienHolder 3 State.
	 * 
	 * <p>Mainframe field is LIENHLDR3STATE.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_LIENHLDR3STATE_LENGTH = 2;
	/**
	 * LienHolder 3 Zip Code.
	 * 
	 * <p>Mainframe field is LIENHLDR3ZPCD.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TMV_LIENHLDR3ZPCD_LENGTH = 5;
	/**
	 * LienHolder 3 Zip Code Plus 4.
	 * 
	 * <p>Mainframe field is LIENHLDR3ZPCDP4.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_LIENHLDR3ZPCDP4_LENGTH = 4;
	/**
	 * LienHolder 3 Country.
	 * 
	 * <p>Mainframe field is LIENHLDR3CNTRY.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_LIENHLDR3CNTRY_LENGTH = 4;
	/**
	 * LienHolder 3 Perm Lien Holder ID.
	 * 
	 * <p>Mainframe field is PERMLIENHLDRID.
	 * 
	 * <p>Length is 11.
	 */
	private static final int IL_TMV_PERMLIEN3HLDRID_LENGTH = 11;
	/**
	 * LienHolder 3 Release date.
	 * 
	 * <p>Mainframe field is LIENRLSEDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_LIEN3RLSEDATE_LENGTH = 8;

	// end Lien Data

	// Start of MV Func ?
	/**
	 * Junk Code.
	 * 
	 * <p>Mainframe field is JNKCD.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_JNKCD_LENGTH = 2;
	/**
	 * Junk Date.
	 * 
	 * <p>Mainframe field is JNKDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_JNKDATE_LENGTH = 8;
	/**
	 * Other Goverment Title Number.
	 * 
	 * <p>Mainframe field is OTHRGOVTTTLNO.
	 * 
	 * <p>Length is 11.
	 */
	private static final int IL_TMV_OTHRGOVTTTLNO_LENGTH = 11;
	/**
	 * Salvage Yard Number.
	 * 
	 * <p>Mainframe field is SALVYARDNO.
	 * 
	 * <p>Length is 9.
	 */
	private static final int IL_TMV_SALVYARDNO_LENGTH = 9;
	/**
	 * Authorization Code to destroy.
	 * 
	 * <p>Mainframe field is AUTHCD.
	 * 
	 * <p>Length is 15.
	 */
	private static final int IL_TMV_AUTHCD_LENGTH = 15;
	/**
	 * Comptroller County Number for Vehicle.
	 * 
	 * <p>Mainframe field is COMPTCNTYNO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TMV_COMPTCNTYNO_LENGTH = 3;
	// defect 10389
	// Removed DLRGDN
	/**
	 * Dealer's 6 digit GDN for seller.
	 * 
	 * <p>This field is no longer consitered by MF.
	 * 
	 * <p>Length is 6.
	 */
	//private static final int IL_TMV_DLRGDN_LENGTH = 6;
	// end defect 10389
	/**
	 * IMC Number.
	 * 
	 * <p>Mainframe field is IMCNO.
	 * 
	 * <p>Length is 11.
	 */
	private static final int IL_TMV_IMCNO_LENGTH = 11;
	/**
	 * Owner Supplied Plate Number.
	 * 
	 * <p>Mainframe field is OWNRSUPPLIEDPLTNO.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TMV_OWNRSUPPLIEDPLTNO_LENGTH = 7;
	/**
	 * Owner Supplied Expiration Year.
	 * 
	 * <p>Mainframe field is OWNRSUPPLIEDEXPYR.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_OWNRSUPPLIEDEXPYR_LENGTH = 4;
	/**
	 * Owner Supplied Sticker Number.
	 * 
	 * <p>Mainframe field is OWNRSUPPLIEDSTKRNO.
	 * 
	 * <p>Length is 10.
	 */
	private static final int IL_TMV_OWNRSUPPLIEDSTKRNO_LENGTH = 10;
	/**
	 * Sales Tax Exempt Code.
	 * 
	 * <p>Mainframe field is SALESTAXEXMPTCD.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_SALESTAXEXMPTCD_LENGTH = 2;
	// defect 10710
	/**
	 * Subcontractor Id.
	 * 
	 * <p>Mainframe field is SUBCONID.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TMV_SUBCONID_LENGTH = 5;
	// end defect 10710
	/**
	 * Subcontractor Issue Date.
	 * 
	 * <p>Mainframe field is SUBCONISSUEDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_SUBCONISSUEDATE_LENGTH = 8;
	/**
	 * Title Surrendered Date.
	 * 
	 * <p>Mainframe field is SURRTTLDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_SURRTTLDATE_LENGTH = 8;
	/**
	 * MainFrame Title Number.
	 * 
	 * <p>Mainframe field is TTLNOMF.
	 * 
	 * <p>Length is 17.
	 */
	private static final int IL_TMV_TTLNOMF_LENGTH = 17;

	// Start of MV Func Indicators
	/**
	 * Additional Lien Indicator.
	 * 
	 * <p>Mainframe field is ADDLLIENRECRDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_ADDLLIENRECRDINDI_LENGTH = 1;
	/**
	 * Additional Trade In Indicator.
	 * 
	 * <p>Mainframe field is ADDLTRADEININDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_ADDLTRADEININDI_LENGTH = 1;
	/**
	 * Agency Loaned Indicator.
	 * 
	 * <p>Mainframe field is AGNCYLOANDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_AGNCYLOANDINDI_LENGTH = 1;
	/**
	 * Diesel Indicator.
	 * 
	 * <p>Mainframe field is DIESELINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_DIESELINDI_LENGTH = 1;
	/**
	 * DOT Standards Indicator.
	 * 
	 * <p>Mainframe field is DOTSTNDRDSINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_DOTSTNDRDSINDI_LENGTH = 1;
	/**
	 * DPS Safety Suspended Indicator.
	 * 
	 * <p>Mainframe field is DPSSAFTYSUSPNINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_DPSSAFTYSUSPNINDI_LENGTH = 1;
	/**
	 * DPS Stolen Indicator.
	 * 
	 * <p>Mainframe field is DPSSTLNINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_DPSSTLNINDI_LENGTH = 1;
	/**
	 * Exempt Indicator.
	 * 
	 * <p>Mainframe field is EXMPTINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_EXMPTINDI_LENGTH = 1;
	/**
	 * Flood Damaged Indicator.
	 * 
	 * <p>Mainframe field is FLOODDMGEINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_FLOODDMGEINDI_LENGTH = 1;
	/**
	 * Fixed Weight Indicator.
	 * 
	 * <p>Mainframe field is FXDWTINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_FXDWTINDI_LENGTH = 1;
	/**
	 * Goverment Owned Indicator.
	 * 
	 * <p>Mainframe field is GOVTOWNDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_GOVTOWNDINDI_LENGTH = 1;
	/**
	 * Heavy Vehicle Use Tax Indicator.
	 * 
	 * <p>Mainframe field is HVYVEHUSETAXINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_HVYVEHUSETAXINDI_LENGTH = 1;
	/**
	 * Inspection Waived Indicator.
	 * 
	 * <p>Mainframe field is INSPECTNWAIVEDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_INSPECTNWAIVEDINDI_LENGTH = 1;
	/**
	 * Lien Not Released Indicator.
	 * 
	 * <p>Mainframe field is LIENNOTRLSEDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_LIENNOTRLSEDINDI_LENGTH = 1;
	/**
	 * Lien2 Not Released Indicator.
	 * 
	 * <p>Mainframe field is LIEN2NOTRLSEDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_LIEN2NOTRLSEDINDI_LENGTH = 1;
	/**
	 * Lien3 Not Released Indicator.
	 * 
	 * <p>Mainframe field is LIEN3NOTRLSEDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_LIEN3NOTRLSEDINDI_LENGTH = 1;
	/**
	 * Privacy Opt Out Code.
	 * 
	 * <p>Mainframe field is PRIVACYOPTCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_PRIVACYOPTCD_LENGTH = 1;
	/**
	 * Plates Seized Indicator.
	 * 
	 * <p>Mainframe field is PLTSSEIZDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_PLTSSEIZDINDI_LENGTH = 1;
	/**
	 * Prior CCO Issued Indicator.
	 * 
	 * <p>Mainframe field is PRIORCCOISSUEINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_PRIORCCOISSUEINDI_LENGTH = 1;
	/**
	 * Permit Required Indicator.
	 * 
	 * <p>Mainframe field is PRMTREQRDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_PRMTREQRDINDI_LENGTH = 1;
	/**
	 * Reconditioned Code.
	 * 
	 * <p>Mainframe field is RECONDCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_RECONDCD_LENGTH = 1;
	/**
	 * Reconditioned Indicator.
	 * 
	 * <p>Mainframe field is RECONTINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_RECONTINDI_LENGTH = 1;
	/**
	 * Renewal Mail Returned Indicator.
	 * 
	 * <p>Mainframe field is RENWLMAILRTRNINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_RENWLMAILRTRNINDI_LENGTH = 1;
	/**
	 * Renewal Year Mis-match Indicator.
	 * 
	 * <p>Mainframe field is RENWLYRMSMTCHINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_RENWLYRMSMTCHINDI_LENGTH = 1;
	/**
	 * Special Plate Program Indicator.
	 * 
	 * <p>Mainframe field is SPCLPLTPROGINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_SPCLPLTPROGINDI_LENGTH = 1;
	/**
	 * Sticker Seized Indicator.
	 * 
	 * <p>Mainframe field is STKRSEIZDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_STKRSEIZDINDI_LENGTH = 1;
	/**
	 * SurvivorShip Rights Indicator.
	 * 
	 * <p>Mainframe field is SURVSHPRIGHTSINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_SURVSHPRIGHTSINDI_LENGTH = 1;
	/**
	 * Title Revoked Indicator.
	 * 
	 * <p>Mainframe field is TTLREVKDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_TTLREVKDINDI_LENGTH = 1;
	/**
	 * VIN Error Indicator.
	 * 
	 * <p>Mainframe field is VINERRINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_VINERRINDI_LENGTH = 1;
	/**
	 * File Tier Code.
	 * 
	 * <p>Mainframe field is FILETIERCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_FILETIERCD_LENGTH = 1;
	/**
	 * Regis Hot Check Indicator.
	 * 
	 * <p>Mainframe field is REGHOTCKINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_REGHOTCKINDI_LENGTH = 1;
	/**
	 * Title Hot Check Indicator.
	 * 
	 * <p>Mainframe field is TTLHOTCKINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_TTLHOTCKINDI_LENGTH = 1;
	/**
	 * Regis Invalid Indicator.
	 * 
	 * <p>Mainframe field is REGINVLDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_REGINVLDINDI_LENGTH = 1;
	/**
	 * Salvage State / Country.
	 * 
	 * <p>Mainframe field is SALVSTATECNTRY.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TMV_SALVSTATECNTRY_LENGTH = 2;
	/**
	 * Emission Source Code.
	 * 
	 * <p>Mainframe field is EMISSIONSOURCECD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_EMISSIONSOURCECD_LENGTH = 1;
	/**
	 * Claim Comptroller County Number.
	 * 
	 * <p>Mainframe field is CLAIMCOMPTCNTYNO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TMV_CLAIMCOMPTCNTYNO_LENGTH = 3;

	// Start of MV Func Terps additions
	/**
	 * Vehicle AbstractValue.
	 * 
	 * <p>Mainframe field is VEHVALUE.
	 * 
	 * <p>Length is 10.
	 */
	private static final int IL_TMV_VEHVALUE_LENGTH = 10;
	/**
	 * Emissions Sales Tax.
	 * 
	 * <p>Mainframe field is EMISSIONSALESTAX.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TMV_EMISSIONSALESTAX_LENGTH = 8;
	/**
	 * Lemon Law Indicator.
	 * 
	 * <p>Mainframe field is LEMONLAWINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_LEMONLAWINDI_LENGTH = 1;

	/**
	 * Hoops Reg Plate Number.
	 * 
	 * <p>Mainframe field is HOOPSREGPLTNO.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TMV_HOOPSREGPLTNO_LENGTH = 7;
	/**
	 * Plate Birth Date.
	 * 
	 * <p>Mainframe field is PLTBIRTHDATE.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TMV_PLTBIRTHDATE_LENGTH = 6;

	// defect 9557
	/**
	 * Child Support Indi.
	 * 
	 * <p>Mainframe field is CHILDSUPPORTINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_CHILDSUPPORTINDI_LENGTH = 1;
	// defect 10389
	// Name change
	// defect 9557
	//	New attributes
	/**
	 * Dealer GDN 10 characters
	 * 
	 * <p>Mainframe field is DLRGDNNEW.
	 * 
	 * <p>Length is 10
	 */
	private static final int IL_TMV_DLRGDN_LENGTH = 10;
	// end defect 9557
	// end defect 10389
	/**
	 * Title Sign date
	 * 
	 * <p>Mainframe field is TTLSIGNDATE
	 * 
	 * <p>Length is 8
	 */
	private static final int IL_TMV_TTLSIGNDATE_LENGTH = 8;

	/**
	 * Electronic Lien Code.
	 * 
	 * <p>Mainframe field is ELIENCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_ETTLCD_LENGTH = 1;

	/**
	 * DissociateCd Plate.
	 * 
	 * <p>Mainframe field is DISSOCIATECD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_DISSOCIATECD_LENGTH = 1;

	/**
	 * V21 Plate Id.
	 * 
	 * <p>Mainframe field is V21PLTID.
	 * 
	 * <p>Length is 10.
	 */
	private static final int IL_TMV_V21PLTID_LENGTH = 10;

	/**
	 * Vehicle Transfer Notice Source.
	 * 
	 * <p>Mainframe field is VTNSOURCE.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TMV_VTNSOURCE_LENGTH = 4;

	/**
	 * V21 Vehicle Transfer Notice Id.
	 * 
	 * <p>Mainframe field is V21VTNID.
	 * 
	 * <p>Length is 10.
	 */
	private static final int IL_TMV_V21VTNID_LENGTH = 10;

	/**
	 * Prisum Level Code.
	 * 
	 * <p>Mainframe field is PRISMLVLCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_PRISMLVLCD_LENGTH = 1;
	/**
	 * Prisum Level Code.
	 * 
	 * <p>Mainframe field is PRISMLVLCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_TTLTRNSFRPNLTEXMPTCD_LENGTH = 3;
	/**
	 * Prisum Level Code.
	 * 
	 * <p>Mainframe field is PRISMLVLCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_TTLTRNSFRENTCD_LENGTH = 1;
	// end defect 9557

	// defect 9961
	// 	New fields for ELT
	/**
	 * Prisum Level Code.
	 * 
	 * <p>Mainframe field is AAMVAMSGID.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_AAMVAMSGID_LENGTH = 30;

	/**
	 * Prisum Level Code.
	 * 
	 * <p>Mainframe field is UTVMISLBLINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_UTVMISLBLINDI_LENGTH = 1;

	/**
	 * Prisum Level Code.
	 * 
	 * <p>Mainframe field is VTRREGEMRGCD1.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_VTRREGEMRGCD1_LENGTH = 1;

	/**
	 * Prisum Level Code.
	 * 
	 * <p>Mainframe field is VTRREGEMRGCD2.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_VTRREGEMRGCD2_LENGTH = 1;

	// defect 10366 
	/**
	 * Private Law Enforcement Vehicle Code.
	 * 
	 * <p>Mainframe field is PVTLAWENFVEHCD.
	 * 
	 * <p>Length is 1.
	 */
	//private static final int IL_TMV_VTRTTLEMRGCD1_LENGTH = 1;
	private static final int IL_TMV_PVTLAWENFVEHCD_LENGTH = 1;

	/**
	 * NonTtl Golf Cart Code 
	 * 
	 * <p>Mainframe field is NONTTLGOLFCARTCD
	 * 
	 * <p>Length is 1.
	 */
	//private static final int IL_TMV_VTRTTLEMRGCD2_LENGTH = 1;
	private static final int IL_TMV_NONTTLGOLFCARTCD_LENGTH = 1;
	// end defect 10366 

	// defect 10389
	// New fields for 640
	/**
	 * VTR Title Emergency code.
	 * 
	 * <p>Mainframe field is VTRTTLEMRGCD3.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_VTRTTLEMRGCD3_LENGTH = 1;
	/**
	 * VTR Title Emergency code.
	 * 
	 * <p>Mainframe field is VTRTTLEMRGCD4.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_VTRTTLEMRGCD4_LENGTH = 1;
	/**
	 * Email Address for the renewal recipient.
	 * 
	 * <p>Mainframe field is RECPNTEMAIL.
	 * 
	 * <p>Length is 50.
	 */
	private static final int IL_TMV_RECPNTEMAIL_LENGTH = 50;
	// defect 10508 
	/**
	 * EMail Renewal Request Code
	 * 
	 * <p>Mainframe field is EMAILRENWLREQCD
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TMV_EMAILRENWLREQCD_LENGTH = 1;
	// end defect 10389
	// end defect 10508 
	// defect 10595
	/**
	 * Title Examine Indi
	 * 
	 * <p>Mainframe field is TTLEXMNINDI
	 * 
	 * <p>Length is 1.
	 */
		private static final int IL_TMV_TTLEXMNINDI_LENGTH = 1;
	// end defect 10595
	// defect 10710
	/**
	 * Vehicle Major Color
	 * 
	 * <p>Mainframe field is VEHMJRCOLORCD
	 * 
	 * <p>Length is 3.
	 */
		 private static final int IL_TMV_VEHMJRCOLORCD_LENGTH = 3;
	/**
	 * Vehicle Minor Color
	 * 
	 * <p>Mainframe field is VEHMNRCOLORCD
	 * 
	 * <p>Length is 3.
	 */
	 private static final int IL_TMV_VEHMNRCOLORCD_LENGTH = 3;
	// end defect 10710	
		 
	// defect 11045 
 	/**
	 * ETTL Print Date
	 * 
	 * <p>Mainframe field is ETTLPRNTDATE
	 * 
	 * <p>Length is 8.
	 */
	 private static final int IL_TMV_ETTLPRNTDATE_LENGTH = 8;	 
 	// end defect 11045
	 
	 //defect 11251 
	 private static final int IL_TMV_SURVSHPRIGHTSNAME1_LENGTH = 30;
	 private static final int IL_TMV_SURVSHPRIGHTSNAME2_LENGTH = 30;
	 private static final int IL_TMV_ADDLSURVIVORINDI_LENGTH = 1;
	 private static final int IL_TMV_EXPORTINDI_LENGTH = 1;
	 private static final int IL_TMV_SALVINDI_LENGTH = 1;
	 //end defect 11251
	 
	////////////////////////////////////////////////////////////////////
	//define all lengths for SR Func Trans
	/**
	/**
	 * Transaction Code.
	 * 
	 * <p>Mainframe field is TRANSCD.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TSR_TRANSCD_LENGTH = 6;
	/**
	 * Office Issuance Number.
	 * 
	 * <p>Mainframe field is OFCISSUANCENO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TSR_OFCISSUANCENO_LENGTH = 3;
	/**
	 * Transaction Workstation Id.
	 * 
	 * <p>Mainframe field is TRANSWSID.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TSR_TRANSWSID_LENGTH = 3;
	/**
	 * Transaction AMDate.
	 * 
	 * <p>Mainframe field is TRANSAMDATE.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TSR_TRANSAMDATE_LENGTH = 5;
	/**
	 * Transaction Time.
	 * 
	 * <p>Mainframe field is TRANSTIME.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TSR_TRANSTIME_LENGTH = 6;
	/**
	 * OFCISSUANCECD.
	 * 
	 * <p>Mainframe field is OFCISSUANCECD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_OFCISSUANCECD_LENGTH = 1;
	/**
	 * Transaction Employee Id.
	 * 
	 * <p>Mainframe field is TRANSEMPID.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TSR_TRANSEMPID_LENGTH = 7;
	/**
	 * Special Plates Regis Id.
	 * 
	 * <p>Mainframe field is SPCLREGID.
	 * 
	 * <p>Length is 9.
	 */
	private static final int IL_TSR_SPCLREGID_LENGTH = 9;
	/**
	 * ORGANIZATION/NUMBER
	 * 
	 * <p>Mainframe field is ORGNO.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TSR_ORGNO_LENGTH = 4;
	/**
	 * ADDITIONAL/SET/INDICATOR
	 * 
	 * <p>Mainframe field is ADDLSETINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_ADDLSETINDI_LENGTH = 1;
	/**
	 * Plate Expiration Month.
	 * 
	 * <p>Mainframe field is PLTEXPMO.
	 * 
	 * <p>Length is 2.
	 */
	// defect 9864
	//	Rename IL_TSR_REGEXPMO_LENGTH to IL_TSR_PLTEXPMO_LENGTH
	private static final int IL_TSR_PLTEXPMO_LENGTH = 2;
	// end defect 9864
	/**
	 * Plate Expiration Year.
	 * 
	 * <p>Mainframe field is PLTEXPYR.
	 * 
	 * <p>Length is 4.
	 */
	// defect 9864
	//	Rename IL_TSR_REGEXPYR_LENGTH to IL_TSR_PLTEXPYR_LENGTH
	private static final int IL_TSR_PLTEXPYR_LENGTH = 4;
	// end defect 9864
	/**
	 * MANUFACTURING PLATE NUMBER
	 * 
	 * <p>Mainframe field is MFGPLTNO.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TSR_MFGPLTNO_LENGTH = 8;
	/**
	 * MANUFACTURING STATUS CODE
	 * 
	 * <p>Mainframe field is MFGSTATUSCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_MFGSTATUSCD_LENGTH = 1;
	/**
	 * PLATE OWNER ID
	 * 
	 * <p>Mainframe field is PLTOWNRID.
	 * 
	 * <p>Length is 9.
	 */
	private static final int IL_TSR_PLTOWNRID_LENGTH = 9;
	/**
	 * PLATE OWNER NAME1
	 * 
	 * <p>Mainframe field is PLTOWNRNAME1.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TSR_PLTOWNRNAME1_LENGTH = 30;
	/**
	 * PLATE OWNER NAME2
	 * 
	 * <p>Mainframe field is PLTOWNRNAME2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TSR_PLTOWNRNAME2_LENGTH = 30;
	/**
	 * OWNER LAST NAME
	 * 
	 * <p>Mainframe field is PLTOWNRLSTNAME.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_PLTOWNRLSTNAME_LENGTH = 1;
	/**
	 * OWNER FIRST NAME
	 * 
	 * <p>Mainframe field is PLTOWNRFSTNAME.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_PLTOWNRFSTNAME_LENGTH = 1;
	/**
	 * OWNER MIDDLE NAME
	 * 
	 * <p>Mainframe field is PLTOWNRMI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_PLTOWNRMI_LENGTH = 1;
	/**
	 * PLATE OWNER ST1
	 * 
	 * <p>Mainframe field is PLTOWNRST1.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TSR_PLTOWNRST1_LENGTH = 30;
	/**
	 * PLATE OWNER ST2
	 * 
	 * <p>Mainframe field is PLTOWNRST2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TSR_PLTOWNRST2_LENGTH = 30;
	/**
	 * PLATE OWNER CITY
	 * 
	 * <p>Mainframe field is PLTOWNRCITY.
	 * 
	 * <p>Length is 19.
	 */
	private static final int IL_TSR_PLTOWNRCITY_LENGTH = 19;
	/**
	 * PLATE OWNER STATE
	 * 
	 * <p>Mainframe field is PLTOWNRSTATE.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TSR_PLTOWNRSTATE_LENGTH = 2;
	/**
	 * PLATE OWNER ZIP CODE
	 * 
	 * <p>Mainframe field is PLTOWNRZPCD.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TSR_PLTOWNRZPCD_LENGTH = 5;
	/**
	 * PLATE OWNER ZIP CODE P4
	 * 
	 * <p>Mainframe field is PLTOWNRZPCDP4.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TSR_PLTOWNRZPCDP4_LENGTH = 4;
	/**
	 * PLATE OWNER COUNTRY
	 * 
	 * <p>Mainframe field is PLTOWNRCNTRY.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TSR_PLTOWNRCNTRY_LENGTH = 4;
	/**
	 * PLATE OWNER PHONE NUMBER
	 * 
	 * <p>Mainframe field is PLTOWNRPHONENO.
	 * 
	 * <p>Length is 10.
	 */
	private static final int IL_TSR_PLTOWNRPHONENO_LENGTH = 10;
	/**
	 * PLATE OWNER E-MAIL
	 * 
	 * <p>Mainframe field is PLTOWNREMAIL.
	 * 
	 * <p>Length is 50.
	 */
	private static final int IL_TSR_PLTOWNREMAIL_LENGTH = 50;
	/**
	 * SPECIAL PLATE AUDIT TRAIL TRANSI
	 * 
	 * <p>Mainframe field is SAUDITTRAILTRANSID.
	 * 
	 * <p>Length is 17.
	 */
	private static final int IL_TSR_SAUDITTRAILTRANSID_LENGTH = 17;
	// defect 10389
	// New AbstractAttribute
	/**
	 * Reserve Reason Code
	 * 
	 * <p>Mainframe field is RESRVREASNCD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_RESRVREASNCD_LENGTH = 1;
	// end defect 10389
	/**
	 * MANUFACTURING DATE
	 * 
	 * <p>Mainframe field is MFGDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TSR_MFGDATE_LENGTH = 8;
	/**
	 * PLATE SET NUMBER
	 * 
	 * <p>Mainframe field is PLTSETNO.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TSR_PLTSETNO_LENGTH = 2;
	/**
	 * SPECIAL PLATE DOC NUMBER
	 * 
	 * <p>Mainframe field is SPCLDOCNO.
	 * 
	 * <p>Length is 17.
	 */
	private static final int IL_TSR_SPCLDOCNO_LENGTH = 17;
	/**
	 * SPECIAL PLATE FEE
	 * 
	 * <p>Mainframe field is SPCLFEE.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TSR_SPCLFEE_LENGTH = 6;
	/**
	 * PLATE OWNER OFC CODE
	 * 
	 * <p>Mainframe field is PLTOWNROFCCD.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TSR_PLTOWNROFCCD_LENGTH = 2;
	/**
	 * PLATE OWNER DISTRICT
	 * 
	 * <p>Mainframe field is PLTOWNRDIST.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TSR_PLTOWNRDIST_LENGTH = 3;
	// defect 10389
	// New attribute
	/**
	 * MARKETING ALLOWED INDICATOR
	 * 
	 * <p>Mainframe field is MKTNGALLOWDINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_MKTNGALLOWDINDI_LENGTH = 1;
	// end defect 10389
	/**
	 * INTERNATIONAL SYMBOL OF ACCESS INDI
	 * 
	 * <p>Mainframe field is ISAINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_ISAINDI_LENGTH = 1;
	/**
	 * SPECIAL REMARKS
	 * 
	 * <p>Mainframe field is SPCLREMKS.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TSR_SPCLREMKS_LENGTH = 30;

	// defect 10492 / 10507
	/**
	 * Election Pending Indicator
	 * 
	 * <p>Mainframe field is ELECTIONPNDNGINDI
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_ELECTIONPNDNGINDI_LENGTH = 1;
	// end defect 10492 / 10507 

	// defect 10389
	// changed name
	/**
	 * PLTOWNRDLRGDN.
	 * 
	 * <p>Mainframe field is PLTOWNRDLRGDN.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_PLTOWNRDLRGDN_LENGTH = 10;
	// end defect 10389
	/**
	 * Disassociated Plate.
	 * 
	 * <p>Mainframe field is DISSOCIATECD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_DISSOCIATECD_LENGTH = 1;
	// defect 10389
	// New Attributes
	/**
	 * Number of Years a Plate is Valid.
	 * 
	 * <p>Mainframe field is PLTVALIDITYTERM.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TSR_PLTVALIDITYTERM_LENGTH = 2;
	/**
	 * Number of Months of a Valid Plate Term.
	 * 
	 * <p>Mainframe field is PLTSOLDMOS.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TSR_PLTSOLDMOS_LENGTH = 3;
	/**
	 * Internet Trace NumberVendor Transaction Date.
	 * 
	 * <p>Mainframe field is ITRNTTRACENO.
	 * 
	 * <p>Length is 15.
	 */
	private static final int IL_TSR_ITRNTTRACENO_LENGTH = 15;
	/**
	 * Auction Plate Indicator.
	 * 
	 * <p>Mainframe field is AUCTNPLTINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TSR_AUCTNPLTINDI_LENGTH = 1;
	/**
	 * Auction Paid Amount.
	 * 
	 * <p>Mainframe field is AUCTNPDAMT.
	 * 
	 * <p>Length is 11.
	 */
	private static final int IL_TSR_AUCTNPDAMT_LENGTH = 11;
	// end defect 10389

	////////////////////////////////////////////////////////////////////
	//define all lengths for Transaction
	/**
	 * Transaction Code.
	 * 
	 * <p>Mainframe field is TRANSCD.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TT_TRANSCD_LENGTH = 6;
	/**
	 * Office Issuance Number.
	 * 
	 * <p>Mainframe field is OFCISSUANCENO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TT_OFCISSUANCENO_LENGTH = 3;
	/**
	 * Transaction Workstation Id.
	 * 
	 * <p>Mainframe field is TRANSWSID.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TT_TRANSWSID_LENGTH = 3;
	/**
	 * Transaction AMDate.
	 * 
	 * <p>Mainframe field is TRANSAMDATE.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TT_TRANSAMDATE_LENGTH = 5;
	/**
	 * Transaction Time.
	 * 
	 * <p>Mainframe field is TRANSTIME.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TT_TRANSTIME_LENGTH = 6;
	/**
	 * Office Issuance Code.
	 * 
	 * <p>Mainframe field is OFCISSUANCECD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TT_OFCISSUANCECD_LENGTH = 1;
	/**
	 * Substation Id.
	 * 
	 * <p>Mainframe field is SUBSTAID.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TT_SUBSTAID_LENGTH = 2;
	/**
	 * Cash Workstation Id.
	 * 
	 * <p>Mainframe field is CASHWSID.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TT_CASHWSID_LENGTH = 3;
	/**
	 * Transaction Employee Id.
	 * 
	 * <p>Mainframe field is TRANSEMPID.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TT_TRANSEMPID_LENGTH = 7;
	// also know as INCOMPL_TRANS_INDI
	/**
	 * Selected Transaction Id.
	 * <br>Also known as Incomplete Transaction Id.
	 * 
	 * <p>Mainframe field is SELCTDTRANSINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TT_SELCTDTRANSINDI_LENGTH = 1;
	/**
	 * Transaction Posted MainFrame Indicator.
	 * 
	 * <p>Mainframe field is TRANSPOSTEDMFINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TT_TRANSPOSTEDMFINDI_LENGTH = 1;
	/**
	 * Transaction Posted LAN Indicator.
	 * 
	 * <p>Mainframe field is TRANSPOSTEDLANINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TT_TRANSPOSTEDLANINDI_LENGTH = 1;
	/**
	 * Transaction Voided Indicator.
	 * 
	 * <p>Mainframe field is VOIDEDTRANSINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TT_VOIDEDTRANSINDI_LENGTH = 1;

	// start of Transaction Inquiry Data
	/**
	 * Regis Plate Number.
	 * 
	 * <p>Mainframe field is REGPLTNO.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TT_REGPLTNO_LENGTH = 7;
	/**
	 * Document Number.
	 * 
	 * <p>Mainframe field is DOCNO.
	 * 
	 * <p>Length is 17.
	 */
	private static final int IL_TT_DOCNO_LENGTH = 17;
	/**
	 * Owner Id.
	 * 
	 * <p>Mainframe field is OWNRID.
	 * 
	 * <p>Length is 9.
	 */
	private static final int IL_TT_OWNRID_LENGTH = 9;
	/**
	 * VIN.
	 * 
	 * <p>Mainframe field is VIN.
	 * 
	 * <p>Length is 22.
	 */
	private static final int IL_TT_VIN_LENGTH = 22;
	/**
	 * Regis Sticker Number.
	 * 
	 * <p>TODO - Do we still need this?
	 * 
	 * <p>Mainframe field is REGSTKRNO.
	 * 
	 * <p>Length is 10.
	 */
	private static final int IL_TT_REGSTKRNO_LENGTH = 10;

	// start of Transaction CustData
	/**
	 * Customer Sequence Number.
	 * 
	 * <p>Mainframe field is CUSTSEQNO.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TT_CUSTSEQNO_LENGTH = 5;
	/**
	 * Customer Name Line 1.
	 * 
	 * <p>Mainframe field is CUSTNAME1.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TT_CUSTNAME1_LENGTH = 30;
	/**
	 * Customer Name Line 2.
	 * 
	 * <p>Mainframe field is CUSTNAME2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TT_CUSTNAME2_LENGTH = 30;
	
	// defect 10492 
	//	/**
	//	 * Customer Last Name.
	//	 * 
	//	 * 
	//	 * <p>Mainframe field is CUSTLSTNAME.
	//	 * 
	//	 * <p>Length is 1.
	//	 */
	//	private static final int IL_TT_CUSTLSTNAME_LENGTH = 1;
	//	/**
	//	 * Customer First Name.
	//	 * 
	//	 * 
	//	 * <p>Mainframe field is CUSTFSTNAME.
	//	 * 
	//	 * <p>Length is 1.
	//	 */
	//	private static final int IL_TT_CUSTFSTNAME_LENGTH = 1;
	//	/**
	//	 * Customer Middle Initial.
	//	 * 
	//	 * 
	//	 * <p>Mainframe field is CUSTMINAME.
	//	 * 
	//	 * <p>Length is 1.
	//	 */
	//	private static final int IL_TT_CUSTMINAME_LENGTH = 1;
	//	/**
	//	 * Customer Street Line 1.
	//	 * 
	//	 * <p>Mainframe field is CUSTST1.
	//	 * 
	//	 * <p>Length is 30.
	//	 */
	// end defect 10492 
	private static final int IL_TT_CUSTST1_LENGTH = 30;
	/**
	 * Customer Street Line 2.
	 * 
	 * <p>Mainframe field is CUSTST2.
	 * 
	 * <p>Length is 30.
	 */
	private static final int IL_TT_CUSTST2_LENGTH = 30;
	/**
	 * Customer City.
	 * 
	 * <p>Mainframe field is CUSTCITY.
	 * 
	 * <p>Length is 19.
	 */
	private static final int IL_TT_CUSTCITY_LENGTH = 19;
	/**
	 * Customer State.
	 * 
	 * <p>Mainframe field is CUSTSTATE.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TT_CUSTSTATE_LENGTH = 2;
	/**
	 * Customer Zip Code.
	 * 
	 * <p>Mainframe field is CUSTZPCD.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TT_CUSTZPCD_LENGTH = 5;
	/**
	 * Customer Zip Code Plus 4.
	 * 
	 * <p>Mainframe field is CUSTZPCDP4.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TT_CUSTZPCDP4_LENGTH = 4;
	/**
	 * Customer Country if not US.
	 * 
	 * <p>Mainframe field is CUSTCNTRY.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TT_CUSTCNTRY_LENGTH = 4;
	/**
	 * DLS Certification Number.
	 * 
	 * <p>Mainframe field is DLSCERTNO.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TT_DLSCERTNO_LENGTH = 6;

	// start of Transaction EffPeriod
	/**
	 * Effective Date.
	 * 
	 * <p>Mainframe field is EFFDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TT_EFFDATE_LENGTH = 8;
	/**
	 * Effective Time.
	 * 
	 * <p>Mainframe field is EFFTIME.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TT_EFFTIME_LENGTH = 6;
	/**
	 * Expiration Date.
	 * 
	 * <p>Mainframe field is EXPDATE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TT_EXPDATE_LENGTH = 8;
	/**
	 * Expiration Time.
	 * 
	 * <p>Mainframe field is EXPTIME.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TT_EXPTIME_LENGTH = 6;

	// start of Transaction VehData
	/**
	 * Regis Class Code.
	 * 
	 * <p>Mainframe field is REGCLASSCD.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TT_REGCLASSCD_LENGTH = 3;
	/**
	 * Tire Type Code.
	 * 
	 * <p>Mainframe field is TIRETYPECD.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TT_TIRETYPECD_LENGTH = 1;
	/**
	 * Vehicle Body Type.
	 * 
	 * <p>Mainframe field is VEHBDYTYPE.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TT_VEHBDYTYPE_LENGTH = 2;
	/**
	 * Vehicle Carrying Capacity.
	 * 
	 * <p>Mainframe field is VEHCARYNGCAP.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TT_VEHCARYNGCAP_LENGTH = 6;
	/**
	 * Vehicle Empty Weight.
	 * 
	 * <p>Mainframe field is VEHEMPTYWT.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TT_VEHEMPTYWT_LENGTH = 6;
	/**
	 * Vehicle Gross Weight.
	 * 
	 * <p>Mainframe field is VEHGROSSWT.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TT_VEHGROSSWT_LENGTH = 6;
	/**
	 * Vehicle Make.
	 * 
	 * <p>Mainframe field is VEHMK.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TT_VEHMK_LENGTH = 4;
	/**
	 * Vehicle Model Year.
	 * 
	 * <p>Mainframe field is VEHMODLYR.
	 * 
	 * <p>Length is 4.
	 */
	private static final int IL_TT_VEHMODLYR_LENGTH = 4;
	/**
	 * Vehicle Regis State.
	 * 
	 * <p>Mainframe field is VEHREGSTATE.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TT_VEHREGSTATE_LENGTH = 2;
	/**
	 * Vehicle Unit Number.
	 * 
	 * <p>Mainframe field is VEHUNITNO.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TT_VEHUNITNO_LENGTH = 6;

	// start of Transaction VoidTransData
	/**
	 * Office issuing Void.
	 * 
	 * <p>Mainframe field is VOIDOFCISSUANCENO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TT_VOIDOFCISSUANCENO_LENGTH = 3;
	/**
	 * Transaction Workstation Id issuing Void.
	 * 
	 * <p>Mainframe field is VOIDTRANSWSID.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TT_VOIDTRANSWSID_LENGTH = 3;
	/**
	 * Transaction AMDate when the Void was done.
	 * 
	 * <p>Mainframe field is VOIDTRANSAMDATE.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TT_VOIDTRANSAMDATE_LENGTH = 5;
	/**
	 * Transaction Time when the Void was done.
	 * 
	 * <p>Mainframe field is VOIDTRANSTIME.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TT_VOIDTRANSTIME_LENGTH = 6;

	// start of Transaction Indicators
	/**
	 * Diesel Indicator.
	 * 
	 * <p>Mainframe field is DIESELINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TT_DIESELINDI_LENGTH = 1;
	/**
	 * Regis Penalty Charge Indicator.
	 * 
	 * <p>Mainframe field is REGPNLTYCHRGINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TT_REGPNLTYCHRGINDI_LENGTH = 1;
	/**
	 * Processed By Mail Indicator.
	 * 
	 * <p>Mainframe field is PROCSDBYMAILINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TT_PROCSDBYMAILINDI_LENGTH = 1;
	/**
	 * MainFrame Version Number.
	 * 
	 * <p>Mainframe field is VERSIONCD.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TT_VERSIONCD_LENGTH = 2;
	/**
	 * Business Date.
	 * 
	 * <p>Mainframe field is BSNDATE.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TT_BSNDATE_LENGTH = 5;
	/**
	 * Business Date Total Amount.
	 * 
	 * <p>Mainframe field is BSNDATETOTALAMT.
	 * 
	 * <p>Length is 10.
	 */
	private static final int IL_TT_BSNDATETOTALAMT_LENGTH = 10;
	/**
	 * Void Transaction Code.
	 * 
	 * <p>Mainframe field is VOIDTRANSCD.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TT_VOIDTRANSCD_LENGTH = 6;

	// defect 10492 
	/**
	 * Add Permit Record Indicator 
	 * 
	 * <p>Mainframe field is ADDPRMTRECINDI
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TT_ADDPRMTRECINDI_LENGTH = 1;

	/**
	 * Delete Permit Record Indicator 
	 * 
	 * <p>Mainframe field is DELPRMTRECINDI
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TT_DELPRMTRECINDI_LENGTH = 1;
	// end defect 10492
		
	////////////////////////////////////////////////////////////////////
	//define all lengths for Trans_Payment
	/**
	 * Transaction AMDate.
	 * 
	 * <p>Mainframe field is TRANSAMDATE.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TTP_TRANSAMDATE_LENGTH = 5;
	/**
	 * Cash Workstation Id.
	 * 
	 * <p>Mainframe field is CASHWSID.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TTP_CASHWSID_LENGTH = 3;
	/**
	 * Payment Number.
	 * 
	 * <p>Mainframe field is PYMNTNO.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TTP_PYMNTNO_LENGTH = 2;
	/**
	 * Office Issuance Number.
	 * 
	 * <p>Mainframe field is OFCISSUANCENO.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TTP_OFCISSUANCENO_LENGTH = 3;

	/**
	 * Transaction Workstation Id.
	 * 
	 * <p>Mainframe field is TRANSWSID.
	 * 
	 * <p>Length is 3.
	 */
	private static final int IL_TTP_TRANSWSID_LENGTH = 3;
	/**
	 * Customer Sequence Number.
	 * 
	 * <p>Mainframe field is CUSTSEQNO.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TTP_CUSTSEQNO_LENGTH = 5;
	// defect 10595
	/**
	 * Payment Type Amount.
	 * 
	 * <p>Mainframe field is PYMNTTYPEAMT.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TTP_PYMNTTYPEAMT_LENGTH = 11;
	// end defect 10595
	/**
	 * Payment Check Number.
	 * 
	 * <p>Mainframe field is PYMNTCKNO.
	 * 
	 * <p>Length is 6.
	 */
	private static final int IL_TTP_PYMNTCKNO_LENGTH = 6;
	/**
	 * Change Due.
	 * 
	 * <p>Mainframe field is CHNGDUE.
	 * 
	 * <p>Length is 8.
	 */
	private static final int IL_TTP_CHNGDUE_LENGTH = 8;
	/**
	 * Transaction Posted to LAN Indicator.
	 * 
	 * <p>Mainframe field is TRANSPOSTEDLANINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TTP_TRANSPOSTEDLANINDI_LENGTH = 1;
	/**
	 * Transaction Posted to MainFrame Indicator.
	 * 
	 * <p>Mainframe field is TRANSPOSTEDMFINDI.
	 * 
	 * <p>Length is 1.
	 */
	private static final int IL_TTP_TRANSPOSTEDMFINDI_LENGTH = 1;
	/**
	 * MainFrame Version Number.
	 * 
	 * <p>Mainframe field is VERSIONCD.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TTP_VERSIONCD_LENGTH = 2;
	/**
	 * Business Date.
	 * 
	 * <p>Mainframe field is BSNDATE.
	 * 
	 * <p>Length is 5.
	 */
	private static final int IL_TTP_BSNDATE_LENGTH = 5;
	/**
	 * Transaction Employee Id.
	 * 
	 * <p>Mainframe field is TRANSEMPID.
	 * 
	 * <p>Length is 7.
	 */
	private static final int IL_TTP_TRANSEMPID_LENGTH = 7;
	/**
	 * Substation Id.
	 * 
	 * <p>Mainframe field is SUBSTAID.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TTP_SUBSTAID_LENGTH = 2;
	/**
	 * Payment Type Code.
	 * 
	 * <p>Mainframe field is PYMNTTYPECD.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TTP_PYMNTTYPECD_LENGTH = 2;
	/**
	 * Change Due Payment Type Code.
	 * 
	 * <p>Mainframe field is CHNGDUEPYMNTTYPECD.
	 * 
	 * <p>Length is 2.
	 */
	private static final int IL_TTP_CHNGDUEPYMNTTYPECD_LENGTH = 2;

	// Permit Transaciton Data 
	private static final int IL_PT_TRANSCD_LENGTH = 6;
	private static final int IL_PT_OFCISSUANCENO_LENGTH = 3;
	private static final int IL_PT_TRANSWSID_LENGTH = 3;
	private static final int IL_PT_TRANSAMDATE_LENGTH = 5;
	private static final int IL_PT_TRANSTIME_LENGTH = 6;
	private static final int IL_PT_TRANSEMPID_LENGTH = 7;
	private static final int IL_PT_PRMTISSUANCEID_LENGTH = 17;
	private static final int IL_PT_CUSTLSTNAME_LENGTH = 30;
	private static final int IL_PT_CUSTFSTNAME_LENGTH = 30;
	private static final int IL_PT_CUSTMINAME_LENGTH = 1;
	private static final int IL_PT_CUSTBSNNAME_LENGTH = 60;
	private static final int IL_PT_CUSTST_LENGTH = 30;
	private static final int IL_PT_CUSTCITY_LENGTH = 19;
	private static final int IL_PT_CUSTSTATE_LENGTH = 2;
	private static final int IL_PT_CUSTZPCD_LENGTH = 5;
	private static final int IL_PT_CUSTZPCDP4_LENGTH = 4;
	private static final int IL_PT_CUSTCNTRY_LENGTH = 4;
	private static final int IL_PT_CUSTEMAIL_LENGTH = 50;
	private static final int IL_PT_CUSTPHONE_LENGTH = 10;
	private static final int IL_PT_PRMTNO_LENGTH = 7;
	private static final int IL_PT_ITMCD_LENGTH = 8;
	private static final int IL_PT_ACCTITMCD_LENGTH = 8;
	private static final int IL_PT_PRMTPDAMT_LENGTH = 11;
	private static final int IL_PT_EFFDATE_LENGTH = 8;
	private static final int IL_PT_EFFTIME_LENGTH = 6;
	private static final int IL_PT_EXPDATE_LENGTH = 8;
	private static final int IL_PT_EXPTIME_LENGTH = 6;
	private static final int IL_PT_VEHBDYTYPE_LENGTH = 2;
	private static final int IL_PT_VEHMK_LENGTH = 4;
	private static final int IL_PT_VEHMKDESC_LENTH = 15;
	private static final int IL_PT_VEHMODLYR_LENGTH = 4;
	private static final int IL_PT_VIN_LENGTH = 22;
	private static final int IL_PT_VEHREGCNTRY_LENGTH = 4;
	private static final int IL_PT_VEHREGSTATE_LENGTH = 2;
	private static final int IL_PT_VEHREGPLTNO_LENGTH = 15;
	private static final int IL_PT_ONETRIPPNT_LENGTH = 32;
	private static final int IL_PT_MFDOWN_LENGTH = 1;
	private static final int IL_PT_BULKPRMTVENDORID_LENGTH = 11;
	private static final int IL_PT_VOIDEDTRANSINDI_LENGHT = 1;

	// MfAccess Object for using utility methods
	private MfAccess caMfAccess;

	/**
	 * MfbaSendTransU.java Constructor
	 * 
	 * @param aaMfAccess
	 */
	public MfbaSendTransT(MfAccess aaMfAccess)
	{
		caMfAccess = aaMfAccess;
	}

	/**
	 * Formats SendTrans Buffer for Version T.
	 * 
	 * @param asBuffDescArea
	 * @param aaMFTrans
	 * @return String
	 * @throws RTSException
	 */
	public Vector formatSendTransData(
		String asBuffDescArea,
		MFTrans aaMFTrans)
		throws RTSException
	{
		// This is the Vector to be returned
		Vector lvResponse = null;

		// Get the Transaction Objects into the container
		// This Vector contains either
		// 1. One Trans object and all related objects 
		//     (no LOGTRAN or TRANSPYMNT)
		// 2. A number of TRANSPYMNT objects
		// 3. A number of LOGTRAN objects
		Vector lvTransactionContainer = aaMFTrans.getTransObjects();

		if (lvTransactionContainer != null
			&& lvTransactionContainer.size() != 0)
		{
			// Contains the transaction buffer counts
			StringBuffer lsBufferCountsDesc =
				new StringBuffer(asBuffDescArea);

			// Contains the formatted data going to mf
			String lsFormatedData = CommonConstant.STR_SPACE_EMPTY;

			// set up the error handling data
			int liOfc = 0;
			int liWsId = 0;
			String lsTransAmDate = CommonConstant.STR_SPACE_EMPTY;
			String lsTransTime = CommonConstant.STR_SPACE_EMPTY;

			// initialize the vector 
			lvResponse = new Vector();

			// for each other object, I need to loop through and find 
			// instanceOf and find out their count
			int liTransCount = 0;
			int liMvFuncCount = 0;
			int liSRFuncCount = 0;
			int liInvFuncCount = 0;
			int liFundFuncCount = 0;
			int liTrInvDtlCount = 0;
			int liTrFdsDtlCount = 0;
			int liTransPayCount = 0;
			int liLogFuncCount = 0;

			// defect 10492
			int liPrmtTransCount = 0;
			PermitTransactionData laPrmtTransData =
				new PermitTransactionData();
			// end defect 10492  

			// Define all objects
			TransactionData laTransactionData = new TransactionData();
			// MvFuncTrans
			MotorVehicleFunctionTransactionData laMvFuncData =
				new MotorVehicleFunctionTransactionData();
			// SRFuncTrans
			SpecialRegistrationFunctionTransactionData laSRFuncTrans =
				new SpecialRegistrationFunctionTransactionData();
			// InvFuncTrans
			InventoryFunctionTransactionData laInvFuncData =
				new InventoryFunctionTransactionData();
			// FundFuncTrans
			FundFunctionTransactionData laFundFuncData =
				new FundFunctionTransactionData();
			// TransInvDetail
			TransactionInventoryDetailData laTrInvDtlData =
				new TransactionInventoryDetailData();
			// TransFundsDetail
			TransactionFundsDetailData laTrFdsDtlData =
				new TransactionFundsDetailData();
			// TransPymnt
			TransactionPaymentData laTransactionPaymentData =
				new TransactionPaymentData();
			// LogonFuncTrans
			LogonFunctionTransactionData laLogonFunctionTransactionData =
				new LogonFunctionTransactionData();
			int liTotalCount = lvTransactionContainer.size();

			// if structure looking for instances IN ORDER and 
			// getting strings from and appending them to the Buffer
			for (int i = 0; i < liTotalCount; i++)
			{
				Object laTransObject =
					lvTransactionContainer.elementAt(i);

				// TRANS
				if (laTransObject instanceof TransactionData)
				{
					laTransactionData =
						(TransactionData) lvTransactionContainer
							.firstElement();
					String lsTrans =
						getValuesFromTransactionData(laTransactionData);
					lsFormatedData = lsFormatedData + lsTrans;
					liTransCount = liTransCount + 1;

					// initialize error data if needed
					if (liOfc == 0)
					{
						liOfc = laTransactionData.getOfcIssuanceNo();
						liWsId = laTransactionData.getTransWsId();
						lsTransAmDate =
							String.valueOf(
								laTransactionData.getTransAMDate());
						lsTransTime =
							String.valueOf(
								laTransactionData.getTransTime());
					}

					continue;
				}

				// MVFUNC
				if (laTransObject
					instanceof MotorVehicleFunctionTransactionData)
				{
					laMvFuncData =
						(MotorVehicleFunctionTransactionData) laTransObject;
					String lsMvFunc =
						getValuesFromMVFuncTransactionData(laMvFuncData);
					lsFormatedData = lsFormatedData + lsMvFunc;
					liMvFuncCount = liMvFuncCount + 1;
					continue;
				}

				// SRFuncTrans
				if (laTransObject
					instanceof SpecialRegistrationFunctionTransactionData)
				{
					laSRFuncTrans =
						(SpecialRegistrationFunctionTransactionData) laTransObject;
					String lsSRFuncTrans =
						getValuesFromSRFuncTransData(laSRFuncTrans);
					lsFormatedData = lsFormatedData + lsSRFuncTrans;
					liSRFuncCount = liSRFuncCount + 1;
					continue;
				}

				// INVFUNC
				if (laTransObject
					instanceof InventoryFunctionTransactionData)
				{
					laInvFuncData =
						(InventoryFunctionTransactionData) laTransObject;
					String lsInvFunc =
						getValuesFromInvFuncTransData(laInvFuncData);
					lsFormatedData = lsFormatedData + lsInvFunc;
					liInvFuncCount = liInvFuncCount + 1;
					continue;
				}

				// FUNDFUNC
				if (laTransObject
					instanceof FundFunctionTransactionData)
				{
					laFundFuncData =
						(FundFunctionTransactionData) laTransObject;
					String lsFundFunc =
						getValuesFromFundFuncTrans(laFundFuncData);
					lsFormatedData = lsFormatedData + lsFundFunc;
					liFundFuncCount = liFundFuncCount + 1;
					continue;
				}

				// TRINVDTL
				if (laTransObject
					instanceof TransactionInventoryDetailData)
				{
					laTrInvDtlData =
						(TransactionInventoryDetailData) laTransObject;
					String lsTrInvDtl =
						getValuesFromTransInvDetailData(laTrInvDtlData);
					lsFormatedData = lsFormatedData + lsTrInvDtl;
					liTrInvDtlCount = liTrInvDtlCount + 1;
					continue;
				}

				// TRFDSDTL
				if (laTransObject
					instanceof TransactionFundsDetailData)
				{
					laTrFdsDtlData =
						(TransactionFundsDetailData) laTransObject;
					String lsTrFdsDtl =
						getValuesFromFundsDtlTransactionData(laTrFdsDtlData);
					lsFormatedData = lsFormatedData + lsTrFdsDtl;
					liTrFdsDtlCount = liTrFdsDtlCount + 1;
					continue;
				}

				// PRMTTRANS
				if (laTransObject instanceof PermitTransactionData)
				{
					laPrmtTransData =
						(PermitTransactionData) laTransObject;
					String lsPrmtTrans =
						getValuesFromPermitTransactionData(laPrmtTransData);
					lsFormatedData = lsFormatedData + lsPrmtTrans;
					liPrmtTransCount = liPrmtTransCount + 1;
					continue;
				}

				// TRANSPAYMENT
				if (laTransObject instanceof TransactionPaymentData)
				{
					laTransactionPaymentData =
						(TransactionPaymentData) laTransObject;
					String lsTransPay =
						getValuesFromTransactionPaymentData(laTransactionPaymentData);
					lsFormatedData = lsFormatedData + lsTransPay;
					liTransPayCount = liTransPayCount + 1;

					// initialize error data if needed
					if (liOfc == 0)
					{
						liOfc =
							laTransactionPaymentData.getOfcIssuanceNo();
						liWsId =
							laTransactionPaymentData.getTransWsId();
						lsTransAmDate =
							String.valueOf(
								laTransactionPaymentData
									.getTransAMDate());
						lsTransTime =
							String.valueOf(
								laTransactionPaymentData
									.getTransTime());
					}

					continue;
				}

				// LOGFUNCTRANS
				if (lvTransactionContainer.elementAt(i)
					instanceof LogonFunctionTransactionData)
				{
					laLogonFunctionTransactionData =
						(LogonFunctionTransactionData) laTransObject;
					String lsLogFunc =
						getValuesFromLogonFunc(laLogonFunctionTransactionData);
					lsFormatedData = lsFormatedData + lsLogFunc;
					liLogFuncCount = liLogFuncCount + 1;

					// initialize error data if needed
					if (liOfc == 0)
					{
						liOfc =
							laLogonFunctionTransactionData
								.getOfcIssuanceNo();
						liWsId =
							laLogonFunctionTransactionData.getWsId();
						lsTransAmDate =
							String.valueOf(
								laLogonFunctionTransactionData
									.getSysDate());
						lsTransTime =
							String.valueOf(
								laLogonFunctionTransactionData
									.getSysTime());
					}

					continue;
				}
			}

			//Populate buffer
			// TRANS 
			lsBufferCountsDesc.replace(
				IL_T_TRANS_OFFSET,
				IL_T_TRANS_END,
				caMfAccess.resizeStringtoLength(
					Integer.toString(liTransCount),
					IL_T_NO_OF_OBJECTS_LENGTH,
					cchBufferInt,
					ApplicationControlConstants
						.SC_MFA_INSERT_AT_BEGINNING));

			// MVFUNCTRANS 
			lsBufferCountsDesc.replace(
				IL_T_MVFUNC_OFFSET,
				IL_T_MVFUNC_END,
				caMfAccess.resizeStringtoLength(
					Integer.toString(liMvFuncCount),
					IL_T_NO_OF_OBJECTS_LENGTH,
					cchBufferInt,
					ApplicationControlConstants
						.SC_MFA_INSERT_AT_BEGINNING));

			// SRFUNCTRANS 
			lsBufferCountsDesc.replace(
				IL_T_SRFUNC_OFFSET,
				IL_T_SRFUNC_END,
				caMfAccess.resizeStringtoLength(
					Integer.toString(liSRFuncCount),
					IL_T_NO_OF_OBJECTS_LENGTH,
					cchBufferInt,
					ApplicationControlConstants
						.SC_MFA_INSERT_AT_BEGINNING));

			// INVFUNC
			lsBufferCountsDesc.replace(
				IL_T_INVFUNC_OFFSET,
				IL_T_INVFUNC_END,
				caMfAccess.resizeStringtoLength(
					Integer.toString(liInvFuncCount),
					IL_T_NO_OF_OBJECTS_LENGTH,
					cchBufferInt,
					ApplicationControlConstants
						.SC_MFA_INSERT_AT_BEGINNING));

			// FUNDFUNC
			lsBufferCountsDesc.replace(
				IL_T_FUNDFUNC_OFFSET,
				IL_T_FUNDFUNC_END,
				caMfAccess.resizeStringtoLength(
					Integer.toString(liFundFuncCount),
					IL_T_NO_OF_OBJECTS_LENGTH,
					cchBufferInt,
					ApplicationControlConstants
						.SC_MFA_INSERT_AT_BEGINNING));

			// TRINVDTL
			lsBufferCountsDesc.replace(
				IL_T_TRINVDTL_OFFSET,
				IL_T_TRINVDTL_END,
				caMfAccess.resizeStringtoLength(
					Integer.toString(liTrInvDtlCount),
					IL_T_NO_OF_OBJECTS_LENGTH,
					cchBufferInt,
					ApplicationControlConstants
						.SC_MFA_INSERT_AT_BEGINNING));

			// TRFDSDTL 
			lsBufferCountsDesc.replace(
				IL_T_TRFDSDTL_OFFSET,
				IL_T_TRFDSDTL_END,
				caMfAccess.resizeStringtoLength(
					Integer.toString(liTrFdsDtlCount),
					IL_T_NO_OF_OBJECTS_LENGTH,
					cchBufferInt,
					ApplicationControlConstants
						.SC_MFA_INSERT_AT_BEGINNING));

			// defect 10492 
			// TRPRMT 
			lsBufferCountsDesc.replace(
				IL_T_TRANSPRMT_OFFSET,
				IL_T_TRANSPRMT_END,
				caMfAccess.resizeStringtoLength(
					Integer.toString(liPrmtTransCount),
					IL_T_NO_OF_OBJECTS_LENGTH,
					cchBufferInt,
					ApplicationControlConstants
						.SC_MFA_INSERT_AT_BEGINNING));
			// end defect 10492 

			// TRANSPAY
			lsBufferCountsDesc.replace(
				IL_T_TRANPYMT_OFFSET,
				IL_T_TRANPYMT_END,
				caMfAccess.resizeStringtoLength(
					Integer.toString(liTransPayCount),
					IL_T_NO_OF_OBJECTS_LENGTH,
					cchBufferInt,
					ApplicationControlConstants
						.SC_MFA_INSERT_AT_BEGINNING));

			// LOGFUNC
			lsBufferCountsDesc.replace(
				IL_T_LOGFUNC_OFFSET,
				IL_T_LOGFUNC_END,
				caMfAccess.resizeStringtoLength(
					Integer.toString(liLogFuncCount),
					IL_T_NO_OF_OBJECTS_LENGTH,
					cchBufferInt,
					ApplicationControlConstants
						.SC_MFA_INSERT_AT_BEGINNING));

			// add the trans counts to the vector
			lvResponse.add(lsBufferCountsDesc.toString());
			// add the data buffer to the vector
			lvResponse.add(lsFormatedData);

			// Add Office for error reporting
			lvResponse.add(new Integer(liOfc));
			// Add workstationid for error handling
			lvResponse.add(new Integer(liWsId));
			// Add Trans AM Date for error handling
			lvResponse.add(lsTransAmDate);
			// Add Trans Time for error handling
			lvResponse.add(lsTransTime);
		}

		return lvResponse;
	}

	/**
	 * Returns a valid mainframe update string from the values in the
	 * <code>FundFunctionTransactionData</code> object. This string is
	 * corresponds to the mainframe buffer <it> FUNDFUNC</it>. 
	 * 
	 * @return String The FundFunc buffer sent to mainframe
	 * @param aaFundFuncTrans FundFunctionTransactionData 
	 */
	private String getValuesFromFundFuncTrans(FundFunctionTransactionData aaFundFuncTransData)
	{
		StringBuffer lsFundFuncData = new StringBuffer();

		//buffers
		char lchBufferInt = '0';
		char lchBufferChar = ' ';

		//get values from obj
		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaFundFuncTransData.getTransCd(),
				IL_TFF_TRANSCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaFundFuncTransData.getOfcIssuanceNo()),
				IL_TFF_OFCISSUANCENO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaFundFuncTransData.getTransWsId()),
				IL_TFF_TRANSWSID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaFundFuncTransData.getTransAMDate()),
				IL_TFF_TRANSAMDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaFundFuncTransData.getTransTime()),
				IL_TFF_TRANSTIME_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaFundFuncTransData.getOfcIssuanceCd()),
				IL_TFF_OFCISSUANCECD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaFundFuncTransData.getVoidedTransIndi()),
				IL_TFF_VOIDEDTRANSINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaFundFuncTransData.getSubconIssueDate()),
				IL_TFF_SUBCONISSUEDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaFundFuncTransData.getSubconId()),
				IL_TFF_SUBCONID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaFundFuncTransData.getApprndComptCntyNo()),
				IL_TFF_APPRNDCOMPTCNTYNO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaFundFuncTransData.getCkNo(),
				IL_TFF_CKNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		//changed to getvalue from Dollar to handle negative values
		//Dollar Field
		lsFundFuncData.append(
			caMfAccess.getValueFromDollar(
				aaFundFuncTransData.getFundsPymntAmt(),
				IL_TFF_FUNDSPYMNTAMT_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaFundFuncTransData.getFundsPymntDate()),
				IL_TFF_FUNDSPYMNTDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaFundFuncTransData.getFundsAdjReasn(),
				IL_TFF_FUNDSADJREASN_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaFundFuncTransData.getComptCntyNo()),
				IL_TFF_COMPTCNTYNO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaFundFuncTransData.getAccntNoCd()),
				IL_TFF_ACCNTNOCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaFundFuncTransData.getTraceNo()),
				IL_TFF_TRACENO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaFundFuncTransData.getItrntTraceNo(),
				IL_TFF_ITRNTTRACENO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		//return the result
		return lsFundFuncData.toString();
	}

	/**
	 * Returns a valid mainframe update string from the values in the
	 * <code>TransactionFundsDetailData</code> object. This string is
	 * corresponds to the mainframe buffer <it>TRFDSDTL</it>. 
	 *
	 * @return String
	 * @param aaFundsDetailTransData TransactionFundsDetailData
	 */
	private String getValuesFromFundsDtlTransactionData(TransactionFundsDetailData aaFundsDetailTransData)
	{
		StringBuffer lsFundsDtlData = new StringBuffer();

		//define buffers
		char lchBufferInt = '0';
		char lchBufferChar = ' ';

		//get values from obj
		lsFundsDtlData.append(
			caMfAccess.resizeStringtoLength(
				aaFundsDetailTransData.getTransCd(),
				IL_TFD_TRANSCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsFundsDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaFundsDetailTransData.getOfcIssuanceNo()),
				IL_TFD_OFCISSUANCENO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundsDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaFundsDetailTransData.getTransWsId()),
				IL_TFD_TRANSWSID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundsDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaFundsDetailTransData.getTransAMDate()),
				IL_TFD_TRANSAMDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundsDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaFundsDetailTransData.getTransTime()),
				IL_TFD_TRANSTIME_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundsDtlData.append(
			caMfAccess.resizeStringtoLength(
				aaFundsDetailTransData.getAcctItmCd(),
				IL_TFD_ACCTITMCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		//Dollar Field
		lsFundsDtlData.append(
			caMfAccess.getValueFromDollar(
				aaFundsDetailTransData.getItmPrice(),
				IL_TFD_ITMPRICE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundsDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaFundsDetailTransData.getFundsRptDate()),
				IL_TFD_FUNDSRPTDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundsDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaFundsDetailTransData.getRptngDate()),
				IL_TFD_RPTNGDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundsDtlData.append(
			caMfAccess.resizeStringtoLength(
				aaFundsDetailTransData.getFundsCat(),
				IL_TFD_FUNDSCAT_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsFundsDtlData.append(
			caMfAccess.resizeStringtoLength(
				aaFundsDetailTransData.getFundsRcvngEnt(),
				IL_TFD_FUNDSRCVNGENT_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		//Dollar Field
		lsFundsDtlData.append(
			caMfAccess.getValueFromDollar(
				aaFundsDetailTransData.getFundsRcvdAmt(),
				IL_TFD_FUNDSRCVDAMT_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsFundsDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaFundsDetailTransData.getItmQty()),
				IL_TFD_ITMQTY_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//return the result
		return lsFundsDtlData.toString();
	}

	/**
	 * Returns a valid mainframe update string from the values in the
	 * <code>TransactionInventoryDetailData</code> object. This string
	 * is corresponds to the mainframe buffer <it>TRINVDTL</it>. 
	 * 
	 * @return String
	 * @param aaTransInvDetailData TransactionInventoryDetailData
	 */
	private String getValuesFromTransInvDetailData(TransactionInventoryDetailData aaTransInvDetailData)
	{
		StringBuffer lsInvDtlData = new StringBuffer();

		//define buffers
		char lchBufferInt = '0';
		char lchBufferChar = ' ';

		//get values from object
		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				aaTransInvDetailData.getTransCd(),
				IL_TID_TRANSCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransInvDetailData.getOfcIssuanceNo()),
				IL_TID_OFCISSUANCENO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransInvDetailData.getTransWsId()),
				IL_TID_TRANSWSID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransInvDetailData.getTransAMDate()),
				IL_TID_TRANSAMDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransInvDetailData.getTransTime()),
				IL_TID_TRANSTIME_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				aaTransInvDetailData.getItmCd(),
				IL_TID_ITMCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransInvDetailData.getInvItmYr()),
				IL_TID_INVITMYR_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				aaTransInvDetailData.getInvEndNo(),
				IL_TID_INVENDNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransInvDetailData.getInvQty()),
				IL_TID_INVQTY_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransInvDetailData.getInvItmReorderLvl()),
				IL_TID_INVITMREORDERLVL_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransInvDetailData.getOfcInvReorderQty()),
				IL_TID_OFCINVREORDERQTY_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransInvDetailData.getInvItmTrckngOfc()),
				IL_TID_INVITMTRCKNGOFC_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				aaTransInvDetailData.getInvItmNo(),
				IL_TID_INVITMNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				aaTransInvDetailData.getInvLocIdCd(),
				IL_TID_INVLOCIDCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransInvDetailData.getDelInvReasnCd()),
				IL_TID_DELINVREASNCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				aaTransInvDetailData.getInvId(),
				IL_TID_INVID_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsInvDtlData.append(
			caMfAccess.resizeStringtoLength(
				caMfAccess.setZonedDecimalFromInt(
					aaTransInvDetailData.getDetailStatusCd()),
				IL_TID_DETAILSTATUSCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//return result
		return lsInvDtlData.toString();
	}

	/**
	 * Returns a valid mainframe update string from the values in the
	 * <code>InventoryFunctionTransactionData</code> object. This string
	 * corresponds to the mainframe buffer <it>INVFUNC</it>. 
	 *
	 * @return String
	 * @param aaInvFuncTransData InventoryFunctionTransactionData
	 */
	private String getValuesFromInvFuncTransData(InventoryFunctionTransactionData aaInvFuncTransData)
	{
		StringBuffer lsInvFuncData = new StringBuffer();

		//define buffer characters
		char lchBufferInt = '0';
		char lchBufferChar = ' ';

		//add code for getting values
		lsInvFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaInvFuncTransData.getTransCd(),
				IL_TIF_TRANSCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsInvFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaInvFuncTransData.getOfcIssuanceNo()),
				IL_TIF_OFCISSUANCENO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaInvFuncTransData.getTransWsId()),
				IL_TIF_TRANSWSID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaInvFuncTransData.getTransAMDate()),
				IL_TIF_TRANSAMDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaInvFuncTransData.getTransTime()),
				IL_TIF_TRANSTIME_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaInvFuncTransData.getOfcIssuanceCd()),
				IL_TIF_OFCISSUANCECD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaInvFuncTransData.getVoidedTransIndi()),
				IL_TIF_VOIDEDTRANSINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaInvFuncTransData.getEmpId(),
				IL_TIF_EMPID_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsInvFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaInvFuncTransData.getSubconIssueDate()),
				IL_TIF_SUBCONISSUEDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsInvFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaInvFuncTransData.getInvcNo(),
				IL_TIF_INVCNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsInvFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaInvFuncTransData.getInvcCorrIndi()),
				IL_TIF_INVCCORRINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//return result
		return lsInvFuncData.toString();
	}

	/**
	 * Returns a valid mainframe update string from the values in the
	 * <code>LogonFunctionTransactionData</code> object. This string is
	 * corresponds to the mainframe buffer <it>LOGTRAN</it>. 
	 *
	 * @return String
	 * @param aaLogonFunctionTransactionData LogonFunctionTransactionData
	 */
	private String getValuesFromLogonFunc(LogonFunctionTransactionData aaLogonFunctionTransactionData)
	{
		StringBuffer laLogonTrans = new StringBuffer();

		char lchBufferChar = ' ';
		char lchBufferInt = '0';

		// The order in appending these values is IMPORTANT as 
		// MF expects them so
		laLogonTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaLogonFunctionTransactionData.getOfcIssuanceNo()),
				IL_TLF_OFCISSUANCENO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laLogonTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaLogonFunctionTransactionData.getWsId()),
				IL_TLF_WSID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laLogonTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaLogonFunctionTransactionData.getSubstaId()),
				IL_TLF_SUBSTAID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laLogonTrans.append(
			caMfAccess.resizeStringtoLength(
				aaLogonFunctionTransactionData.getEmpId(),
				IL_TLF_EMPID_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laLogonTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaLogonFunctionTransactionData.getSysDate()),
				IL_TLF_SYSDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laLogonTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaLogonFunctionTransactionData.getSysTime()),
				IL_TLF_SYSTIME_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laLogonTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaLogonFunctionTransactionData.getSuccessfulIndi()),
				IL_TLF_SUCCESSFULINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laLogonTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaLogonFunctionTransactionData
						.getTransPostedLANIndi()),
				IL_TLF_TRANSPOSTEDLANINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laLogonTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaLogonFunctionTransactionData
						.getTransPostedMfIndi()),
				IL_TLF_TRANSPOSTEDMFINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//return the values in a string
		return laLogonTrans.toString();
	}

	/**
	 * Returns a valid mainframe update string from the values in the
	 * <code>MotorVehicleFunctionTransactionData</code> object. This 
	 * string corresponds to the mainframe buffer <it>MVFUNC</it>.
	 * 
	 *  SC_MFA_INSERT_AT_BEGINNING => For all int's
	 *  SC_MFA_INSERT_AT_END       => For all Strings
	 *	  For int, this is required, ADABASE will not except int's with
	 *	  trailing place holders.
	 * 
	 * 
	 * @return java.lang.String
	 * @param aaMVFuncData MotorVehicleFunctionTransactionData
	 */
	private String getValuesFromMVFuncTransactionData(MotorVehicleFunctionTransactionData aaMVFuncTransData)
	{
		// defect 10112 
		LienholderData laLienHldrData1 =
			aaMVFuncTransData.getLienholderData(
				TitleConstant.LIENHLDR1);
		AddressData laLienAddressData1 =
			laLienHldrData1.getAddressData();

		LienholderData laLienHldrData2 =
			aaMVFuncTransData.getLienholderData(
				TitleConstant.LIENHLDR2);
		AddressData laLienAddressData2 =
			laLienHldrData2.getAddressData();

		LienholderData laLienHldrData3 =
			aaMVFuncTransData.getLienholderData(
				TitleConstant.LIENHLDR3);

		AddressData laLienAddressData3 =
			laLienHldrData3.getAddressData();

		OwnerData laOwnrData = aaMVFuncTransData.getOwnerData();
		AddressData laOwnerAddressData = laOwnrData.getAddressData();
		// end defect 10112 

		StringBuffer laMVFuncData = new StringBuffer();

		char lchBufferInt = '0';
		char lchBufferChar = ' ';

		//code for setting all the values in the buffer

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getTransCd(),
				IL_TMV_TRANSCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getOfcIssuanceNo()),
				IL_TMV_OFCISSUANCENO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getTransWsId()),
				IL_TMV_TRANSWSID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getTransAMDate()),
				IL_TMV_TRANSAMDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getTransTime()),
				IL_TMV_TRANSTIME_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getOfcIssuanceCd()),
				IL_TMV_OFCISSUANCECD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getSubstaId()),
				IL_TMV_SUBSTAID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getTransEmpId(),
				IL_TMV_TRANSEMPID_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getMfDwnCd()),
				IL_TMV_MFDWNCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getVoidedTransIndi()),
				IL_TMV_VOIDEDTRANSINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//Dollar Field
		String lsCustActulRegFee =
			aaMVFuncTransData.getCustActulRegFee().toString();

		int liDecimalPtPosition = lsCustActulRegFee.indexOf(".");

		StringBuffer laCustActulRegFee =
			new StringBuffer(lsCustActulRegFee);

		laCustActulRegFee.replace(
			liDecimalPtPosition,
			liDecimalPtPosition + 1,
			CommonConstant.STR_SPACE_EMPTY);

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				laCustActulRegFee.toString(),
				IL_TMV_CUSTACTULREGFEE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getNotfyngCity(),
				IL_TMV_NOTFYNGCITY_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getResComptCntyNo()),
				IL_TMV_RESCOMPTCNTYNO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getReplicaVehMk(),
				IL_TMV_REPLICAVEHMK_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getReplicaVehModlYr()),
				IL_TMV_REPLICAVEHMODLYR_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getRegClassCd()),
				IL_TMV_REGCLASSCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getRegEffDate()),
				IL_TMV_REGEFFDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getRegExpMo()),
				IL_TMV_REGEXPMO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getRegExpYr()),
				IL_TMV_REGEXPYR_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// defect 10389
		// Removed Regpltage
		//laMVFuncData.append(
		//	caMfAccess.resizeStringtoLength(
		//		Integer.toString(aaMVFuncTransData.getRegPltAge()),
		//		IL_TMV_REGPLTAGE_LENGTH,
		//		lchBufferInt,
		//		ApplicationControlConstants
		//			.SC_MFA_INSERT_AT_BEGINNING));
		// end defect 10389

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getRegPltCd(),
				IL_TMV_REGPLTCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getRegPltNo(),
				IL_TMV_REGPLTNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getRegPltOwnrName(),
				IL_TMV_REGPLTOWNRNAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		//Dollar Field
		String lsRegRefAmt =
			aaMVFuncTransData.getRegRefAmt().toString();
		liDecimalPtPosition = lsRegRefAmt.indexOf(".");
		StringBuffer laRegRefAmt = new StringBuffer(lsRegRefAmt);
		laRegRefAmt.replace(
			liDecimalPtPosition,
			liDecimalPtPosition + 1,
			CommonConstant.STR_SPACE_EMPTY);
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				laRegRefAmt.toString(),
				IL_TMV_REGREFAMT_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getRegStkrCd(),
				IL_TMV_REGSTKRCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getRecpntName(),
				IL_TMV_RECPNTNAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getRecpntLstName(),
				IL_TMV_RECPNTLSTNAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getRecpntFstName(),
				IL_TMV_RECPNTFSTNAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getRecpntMI(),
				IL_TMV_RECPNTMI_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// defect 10112 
		AddressData laRenwlAddrData =
			aaMVFuncTransData.getRenewalAddrData();

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getRenwlMailngSt1(),
		laRenwlAddrData.getSt1(),
			IL_TMV_RENWLMAILNGST1_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getRenwlMailngSt2(),
		laRenwlAddrData.getSt2(),
			IL_TMV_RENWLMAILNGST2_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getRenwlMailngCity(),
		laRenwlAddrData.getCity(),
			IL_TMV_RENWLMAILNGCITY_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		// aaMVFuncTransData.getRenwlMailngState(),
		laRenwlAddrData.getState(),
			IL_TMV_RENWLMAILNGSTATE_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		// aaMVFuncTransData.getRenwlMailngZPCd(),
		laRenwlAddrData.getZpcd(),
			IL_TMV_RENWLMAILNGZPCD_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getRenwlMailngZPCdP4(),
		laRenwlAddrData.getZpcdp4(),
			IL_TMV_RENWLMAILNGZPCDP4_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 10112 

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getBatchNo(),
				IL_TMV_BATCHNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getBndedTtlCd(),
				IL_TMV_BNDEDTTLCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getCCOIssueDate()),
				IL_TMV_CCOISSUEDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getDocNo(),
				IL_TMV_DOCNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getDocTypeCd()),
				IL_TMV_DOCTYPECD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getLegalRestrntNo(),
				IL_TMV_LEGALRESTRNTNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getOthrStateCntry(),
				IL_TMV_OTHRSTATECNTRY_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getOwnrshpEvidCd()),
				IL_TMV_OWNRSHPEVIDCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getPrevOwnrName(),
				IL_TMV_PREVOWNRNAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getPrevOwnrCity(),
				IL_TMV_PREVOWNRCITY_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getPrevOwnrState(),
				IL_TMV_PREVOWNRSTATE_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getTrlrType(),
				IL_TMV_TRLRTYPE_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getTtlApplDate()),
				IL_TMV_TTLAPPLDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getTtlProcsCd(),
				IL_TMV_TTLPROCSCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getOldDocNo(),
				IL_TMV_OLDDOCNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getTtlRejctnDate()),
				IL_TMV_TTLREJCTNDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getTtlRejctnOfc()),
				IL_TMV_TTLREJCTNOFC_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getTtlIssueDate()),
				IL_TMV_TTLISSUEDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getAuditTrailTransId(),
				IL_TMV_AUDITTRAILTRANSID_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getSalesTaxDate()),
				IL_TMV_SALESTAXDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//Dollar Field
		String lsSalesTaxPdAmt =
			aaMVFuncTransData.getSalesTaxPdAmt().toString();

		liDecimalPtPosition = lsSalesTaxPdAmt.indexOf(".");

		StringBuffer laSalesTaxPdAmt =
			new StringBuffer(lsSalesTaxPdAmt);

		laSalesTaxPdAmt.replace(
			liDecimalPtPosition,
			liDecimalPtPosition + 1,
			CommonConstant.STR_SPACE_EMPTY);

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				laSalesTaxPdAmt.toString(),
				IL_TMV_SALESTAXPDAMT_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getSalesTaxCat(),
				IL_TMV_SALESTAXCAT_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		//Dollar Field
		String lsTaxPdOthrState =
			aaMVFuncTransData.getTaxPdOthrState().toString();
		liDecimalPtPosition = lsTaxPdOthrState.indexOf(".");
		StringBuffer laTaxPdOthrState =
			new StringBuffer(lsTaxPdOthrState);
		laTaxPdOthrState.replace(
			liDecimalPtPosition,
			liDecimalPtPosition + 1,
			CommonConstant.STR_SPACE_EMPTY);

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				laTaxPdOthrState.toString(),
				IL_TMV_TAXPDOTHRSTATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//	defect 10112 
		AddressData laVehLocAddr =
			aaMVFuncTransData.getVehLocAddrData();

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getTtlVehLocSt1(),
		laVehLocAddr.getSt1(),
			IL_TMV_TTLVEHLOCST1_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getTtlVehLocSt2(),
		laVehLocAddr.getSt2(),
			IL_TMV_TTLVEHLOCST2_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getTtlVehLocCity(),
		laVehLocAddr.getCity(),
			IL_TMV_TTLVEHLOCCITY_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getTtlVehLocState(),
		laVehLocAddr.getState(),
			IL_TMV_TTLVEHLOCSTATE_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		// aaMVFuncTransData.getTtlVehLocZpCd(),
		laVehLocAddr.getZpcd(),
			IL_TMV_TTLVEHLOCZPCD_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		// aaMVFuncTransData.getTtlVehLocZpCdP4(),
		laVehLocAddr.getZpcdp4(),
			IL_TMV_TTLVEHLOCZPCDP4_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 10112 

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getTireTypeCd(),
				IL_TMV_TIRETYPECD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getTradeInVehYr()),
				IL_TMV_TRADEINVEHYR_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getTradeInVehMk(),
				IL_TMV_TRADEINVEHMK_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getTradeInVehVin(),
				IL_TMV_TRADEINVEHVIN_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVehBdyType(),
				IL_TMV_VEHBDYTYPE_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVehBdyVin(),
				IL_TMV_VEHBDYVIN_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getVehCaryngCap()),
				IL_TMV_VEHCARYNGCAP_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVehClassCd(),
				IL_TMV_VEHCLASSCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getVehEmptyWt()),
				IL_TMV_VEHEMPTYWT_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getVehGrossWt()),
				IL_TMV_VEHGROSSWT_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getVehLngth()),
				IL_TMV_VEHLNGTH_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVehMk(),
				IL_TMV_VEHMK_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVehModl(),
				IL_TMV_VEHMODL_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getVehModlYr()),
				IL_TMV_VEHMODLYR_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVehOdmtrBrnd(),
				IL_TMV_VEHODMTRBRND_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVehOdmtrReadng(),
				IL_TMV_VEHODMTRREADNG_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		//Dollar Field
		String lsVehSalesPrice =
			aaMVFuncTransData.getVehSalesPrice().toString();
		liDecimalPtPosition = lsVehSalesPrice.indexOf(".");
		StringBuffer laVehSalesPrice =
			new StringBuffer(lsVehSalesPrice);
		laVehSalesPrice.replace(
			liDecimalPtPosition,
			liDecimalPtPosition + 1,
			CommonConstant.STR_SPACE_EMPTY);

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				laVehSalesPrice.toString(),
				IL_TMV_VEHSALESPRICE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getVehSoldDate()),
				IL_TMV_VEHSOLDDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//Dollar Field
		String lsVehTon = aaMVFuncTransData.getVehTon().toString();
		liDecimalPtPosition = lsVehTon.indexOf(".");
		StringBuffer laVehTon = new StringBuffer(lsVehTon);
		laVehTon.replace(
			liDecimalPtPosition,
			liDecimalPtPosition + 1,
			CommonConstant.STR_SPACE_EMPTY);

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				laVehTon.toString(),
				IL_TMV_VEHTON_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//Dollar Fields
		String lsVehTradeInAllownce =
			aaMVFuncTransData.getVehTradeInAllownce().toString();
		liDecimalPtPosition = lsVehTradeInAllownce.indexOf(".");
		StringBuffer laVehTradeInAllownce =
			new StringBuffer(lsVehTradeInAllownce);
		laVehTradeInAllownce.replace(
			liDecimalPtPosition,
			liDecimalPtPosition + 1,
			CommonConstant.STR_SPACE_EMPTY);

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				laVehTradeInAllownce.toString(),
				IL_TMV_VEHTRADEINALLOWNCE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVehUnitNo(),
				IL_TMV_VEHUNITNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVIN(),
				IL_TMV_VIN_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// defect 10112 
		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getOwnrId(),
		laOwnrData.getOwnrId(),
			IL_TMV_OWNRID_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getOwnrTtlName1(),
		laOwnrData.getName1(),
			IL_TMV_OWNRTTLNAME1_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		// aaMVFuncTransData.getOwnrTtlName2(),
		laOwnrData.getName2(),
			IL_TMV_OWNRTTLNAME2_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 10112 

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getOwnrLstName(),
				IL_TMV_OWNRLSTNAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getOwnrFstName(),
				IL_TMV_OWNRFSTNAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getOwnrMI(),
				IL_TMV_OWNRMI_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// defect 10112 
		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getOwnrSt1(),
		laOwnerAddressData.getSt1(),
			IL_TMV_OWNRST1_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getOwnrSt2(),
		laOwnerAddressData.getSt2(),
			IL_TMV_OWNRST2_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		// aaMVFuncTransData.getOwnrCity(),
		laOwnerAddressData.getCity(),
			IL_TMV_OWNRCITY_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getOwnrState(),
		laOwnerAddressData.getState(),
			IL_TMV_OWNRSTATE_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getOwnrZpCd(),
		laOwnerAddressData.getZpcd(),
			IL_TMV_OWNRZPCD_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getOwnrZpCdP4(),
		laOwnerAddressData.getZpcdp4(),
			IL_TMV_OWNRZPCDP4_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getOwnrCntry(),
		laOwnerAddressData.getCntry(),
			IL_TMV_OWNRCNTRY_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData
			.append(caMfAccess.resizeStringtoLength(Integer.toString(
		//aaMVFuncTransData.getLien1Date()),
		laLienHldrData1.getLienDate()),
			IL_TMV_LIENHLDR1DATE_LENGTH,
			lchBufferInt,
			ApplicationControlConstants.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr1Name1(),
		laLienHldrData1.getName1(),
			IL_TMV_LIENHLDR1NAME1_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr1Name2(),
		laLienHldrData1.getName2(),
			IL_TMV_LIENHLDR1NAME2_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr1St1(),
		laLienAddressData1.getSt1(),
			IL_TMV_LIENHLDR1ST1_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr1St2(),
		laLienAddressData1.getSt2(),
			IL_TMV_LIENHLDR1ST2_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr1City(),
		laLienAddressData1.getCity(),
			IL_TMV_LIENHLDR1CITY_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr1State(),
		laLienAddressData1.getState(),
			IL_TMV_LIENHLDR1STATE_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr1ZpCd(),
		laLienAddressData1.getZpcd(),
			IL_TMV_LIENHLDR1ZPCD_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr1ZpCdP4(),
		laLienAddressData1.getZpcdp4(),
			IL_TMV_LIENHLDR1ZPCDP4_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr1Cntry(),
		laLienAddressData1.getCntry(),
			IL_TMV_LIENHLDR1CNTRY_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 10112

		// defect 9655   (re-Add: KPH 9/10/09) 
		//	Add PERMLIEN1HLDRID and LIEN1RLSEDATE
		laMVFuncData
			.append(
				caMfAccess
				.resizeStringtoLength(
					aaMVFuncTransData.getPermLienHldrId1(),
					IL_TMV_PERMLIEN1HLDRID_LENGTH,
		//lchBufferInt,
		lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getLienRlseDate1()),
				IL_TMV_LIEN1RLSEDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// end defect 9655 		

		laMVFuncData
			.append(caMfAccess.resizeStringtoLength(Integer.toString(
		//aaMVFuncTransData.getLien2Date()),
		laLienHldrData2.getLienDate()),
			IL_TMV_LIENHLDR2DATE_LENGTH,
			lchBufferInt,
			ApplicationControlConstants.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr2Name1(),
		laLienHldrData2.getName1(),
			IL_TMV_LIENHLDR2NAME1_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr2Name2(),
		laLienHldrData2.getName2(),
			IL_TMV_LIENHLDR2NAME2_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr2St1(),
		laLienAddressData2.getSt1(),
			IL_TMV_LIENHLDR2ST1_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr2St2(),
		laLienAddressData2.getSt2(),
			IL_TMV_LIENHLDR2ST2_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr2City(),
		laLienAddressData2.getCity(),
			IL_TMV_LIENHLDR2CITY_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr2State(),
		laLienAddressData2.getState(),
			IL_TMV_LIENHLDR2STATE_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr2ZpCd(),
		laLienAddressData2.getZpcd(),
			IL_TMV_LIENHLDR2ZPCD_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr2ZpCdP4(),
		laLienAddressData2.getZpcdp4(),
			IL_TMV_LIENHLDR2ZPCDP4_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr2Cntry(),
		laLienAddressData2.getCntry(),
			IL_TMV_LIENHLDR2CNTRY_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// defect 9655 (re-Add: KPH 9/10/09)
		//	Add PERMLIENHLDRID2 and LIENRLSEDATE2
		//	MotorVehicleFunctionTrans change to upper/lower case
		laMVFuncData
			.append(
				caMfAccess
				.resizeStringtoLength(
					aaMVFuncTransData.getPermLienHldrId2(),
					IL_TMV_PERMLIEN2HLDRID_LENGTH,
		//lchBufferInt,
		lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getLienRlseDate2()),
				IL_TMV_LIEN2RLSEDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// end defect 9655 		

		laMVFuncData
			.append(caMfAccess.resizeStringtoLength(Integer.toString(
		//aaMVFuncTransData.getLien3Date()),
		laLienHldrData3.getLienDate()),
			IL_TMV_LIENHLDR3DATE_LENGTH,
			lchBufferInt,
			ApplicationControlConstants.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr3Name1(),
		laLienHldrData3.getName1(),
			IL_TMV_LIENHLDR3NAME1_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr3Name2(),
		laLienHldrData3.getName2(),
			IL_TMV_LIENHLDR3NAME2_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr3St1(),
		laLienAddressData3.getSt1(),
			IL_TMV_LIENHLDR3ST1_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr3St2(),
		laLienAddressData3.getSt2(),
			IL_TMV_LIENHLDR3ST2_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr3City(),
		laLienAddressData3.getCity(),
			IL_TMV_LIENHLDR3CITY_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr3State(),
		laLienAddressData3.getState(),
			IL_TMV_LIENHLDR3STATE_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr3ZpCd(),
		laLienAddressData3.getZpcd(),
			IL_TMV_LIENHLDR3ZPCD_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr3ZpCdP4(),
		laLienAddressData3.getZpcdp4(),
			IL_TMV_LIENHLDR3ZPCDP4_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(caMfAccess.resizeStringtoLength(
		//aaMVFuncTransData.getLienHldr3Cntry(),
		laLienAddressData3.getCntry(),
			IL_TMV_LIENHLDR3CNTRY_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 10112 

		// defect 9655   
		//	Add PERMLIENHLDRID3 and LIENRLSEDATE3
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getPermLienHldrId3(),
				IL_TMV_PERMLIEN3HLDRID_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getLienRlseDate3()),
				IL_TMV_LIEN3RLSEDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// end defect 9655

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getJnkCd()),
				IL_TMV_JNKCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getJnkDate()),
				IL_TMV_JNKDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getOthrGovtTtlNo(),
				IL_TMV_OTHRGOVTTTLNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getSalvYardNo(),
				IL_TMV_SALVYARDNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getAuthCd(),
				IL_TMV_AUTHCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getComptCntyNo()),
				IL_TMV_COMPTCNTYNO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// defect 10389
		// Removed DLRGDN
		// defect 9557
		// This value is no longer considered @ MF  
		//laMVFuncData.append(
		//caMfAccess.resizeStringtoLength(
		//CommonConstant.STR_SPACE_ONE,
		//IL_TMV_DLRGDN_LENGTH,
		//lchBufferChar,
		//ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 9557
		// end defect 10389

		//Dollar Field
		String lsIMCNo = aaMVFuncTransData.getIMCNo().toString();
		liDecimalPtPosition = lsIMCNo.indexOf(".");
		StringBuffer laIMCNo = new StringBuffer(lsIMCNo);
		laIMCNo.replace(
			liDecimalPtPosition,
			liDecimalPtPosition + 1,
			CommonConstant.STR_SPACE_EMPTY);
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				laIMCNo.toString(),
				IL_TMV_IMCNO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getOwnrSuppliedPltNo(),
				IL_TMV_OWNRSUPPLIEDPLTNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getOwnrSuppliedExpYr()),
				IL_TMV_OWNRSUPPLIEDEXPYR_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getOwnrSuppliedStkrNo(),
				IL_TMV_OWNRSUPPLIEDSTKRNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getSalesTaxExmptCd()),
				IL_TMV_SALESTAXEXMPTCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getSubconId()),
				IL_TMV_SUBCONID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getSubconIssueDate()),
				IL_TMV_SUBCONISSUEDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getSurrTtlDate()),
				IL_TMV_SURRTTLDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getTtlNoMf(),
				IL_TMV_TTLNOMF_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getAddlLienRecrdIndi()),
				IL_TMV_ADDLLIENRECRDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getAddlTradeInIndi()),
				IL_TMV_ADDLTRADEININDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getAgncyLoandIndi()),
				IL_TMV_AGNCYLOANDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getDieselIndi()),
				IL_TMV_DIESELINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getDOTStndrdsIndi()),
				IL_TMV_DOTSTNDRDSINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getDPSSaftySuspnIndi()),
				IL_TMV_DPSSAFTYSUSPNINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getDPSStlnIndi()),
				IL_TMV_DPSSTLNINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getExmptIndi()),
				IL_TMV_EXMPTINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getFloodDmgeIndi()),
				IL_TMV_FLOODDMGEINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getFxdWtIndi()),
				IL_TMV_FXDWTINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getGovtOwndIndi()),
				IL_TMV_GOVTOWNDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getHvyVehUseTaxIndi()),
				IL_TMV_HVYVEHUSETAXINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getInspectnWaivedIndi()),
				IL_TMV_INSPECTNWAIVEDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getLienNotRlsedIndi()),
				IL_TMV_LIENNOTRLSEDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getLien2NotRlsedIndi()),
				IL_TMV_LIEN2NOTRLSEDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getLien3NotRlsedIndi()),
				IL_TMV_LIEN3NOTRLSEDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// defect 10246 
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(CommonConstant.DEFAULT_PRIVACY_OPT_CD),
				IL_TMV_PRIVACYOPTCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// end defect 10246 

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getPltsSeizdIndi()),
				IL_TMV_PLTSSEIZDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getPriorCCOIssueIndi()),
				IL_TMV_PRIORCCOISSUEINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getPrmtReqrdIndi()),
				IL_TMV_PRMTREQRDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getRecondCd()),
				IL_TMV_RECONDCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getRecontIndi()),
				IL_TMV_RECONTINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getRenwlMailRtrnIndi()),
				IL_TMV_RENWLMAILRTRNINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getRenwlYrMsmtchIndi()),
				IL_TMV_RENWLYRMSMTCHINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getSpclPltProgIndi()),
				IL_TMV_SPCLPLTPROGINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getStkrSeizdIndi()),
				IL_TMV_STKRSEIZDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getSurvshpRightsIndi()),
				IL_TMV_SURVSHPRIGHTSINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getTtlRevkdIndi()),
				IL_TMV_TTLREVKDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getVINErrIndi()),
				IL_TMV_VINERRINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getFileTierCd()),
				IL_TMV_FILETIERCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getRegHotCkIndi()),
				IL_TMV_REGHOTCKINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getTtlHotCkIndi()),
				IL_TMV_TTLHOTCKINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getRegInvldIndi()),
				IL_TMV_REGINVLDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getSalvStateCntry(),
				IL_TMV_SALVSTATECNTRY_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getEmissionSourceCd(),
				IL_TMV_EMISSIONSOURCECD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getClaimComptCntyNo()),
				IL_TMV_CLAIMCOMPTCNTYNO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// new terps fields
		// Dollar Field
		String lsVehValue = aaMVFuncTransData.getVehValue().toString();
		liDecimalPtPosition = lsVehValue.indexOf(".");
		StringBuffer laVehValue = new StringBuffer(lsVehValue);
		laVehValue.replace(
			liDecimalPtPosition,
			liDecimalPtPosition + 1,
			CommonConstant.STR_SPACE_EMPTY);
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				laVehValue.toString(),
				IL_TMV_VEHVALUE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// Dollar Field
		// defect 10969 
		//		String lsEmissionSalesTax =
		//			aaMVFuncTransData.getEmissionSalesTax().toString();
		//		liDecimalPtPosition = lsEmissionSalesTax.indexOf(".");
		//		StringBuffer laEmissionSalesTax =
		//			new StringBuffer(lsEmissionSalesTax);
		//		laEmissionSalesTax.replace(
		//			liDecimalPtPosition,
		//			liDecimalPtPosition + 1,
		//			CommonConstant.STR_SPACE_EMPTY);
		//		laMVFuncData.append(
		//			caMfAccess.resizeStringtoLength(
		//				laEmissionSalesTax.toString(),
		//				IL_TMV_EMISSIONSALESTAX_LENGTH,
		//				lchBufferInt,
		//				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		laMVFuncData.append(
				caMfAccess.getValueFromDollar(
					aaMVFuncTransData.getEmissionSalesTax(),
					IL_TMV_EMISSIONSALESTAX_LENGTH,
					lchBufferInt,
					ApplicationControlConstants
						.SC_MFA_INSERT_AT_BEGINNING));
		// end defect 10969 


		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getLemonLawIndi()),
				IL_TMV_LEMONLAWINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getHoopsRegPltNo(),
				IL_TMV_HOOPSREGPLTNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getPltBirthDate()),
				IL_TMV_PLTBIRTHDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// defect 9557
		//	New attributes
		// CHILDSUPPORTINDI
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getChildSupportIndi()),
				IL_TMV_CHILDSUPPORTINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// defect 10389
		// DLRGDN (10 DIGITS)
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getDlrGDN(),
				IL_TMV_DLRGDN_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 10389
		// TTLSIGNDATE
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getTtlSignDate()),
				IL_TMV_TTLSIGNDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// ETTLCD
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getETtlCd()),
				IL_TMV_ETTLCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// DISSOCIATECD
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getDissociateCd()),
				IL_TMV_DISSOCIATECD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// V21PLTI
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getV21PltId()),
				IL_TMV_V21PLTID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// VTNSOURCE
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVTNSource(),
				IL_TMV_VTNSOURCE_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// V21VTNID
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getV21VTNId()),
				IL_TMV_V21VTNID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// PRISMLVLCD
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getPrismLvlCd(),
				IL_TMV_PRISMLVLCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// defect 9724 
		// TTLTRNSFRPNLTEXMPTCD
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getTtlTrnsfrPnltyExmptCd(),
				IL_TMV_TTLTRNSFRPNLTEXMPTCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 9724 

		// TTLTRNSFRENTCD
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getTtlTrnsfrEntCd(),
				IL_TMV_TTLTRNSFRENTCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 9557

		// Remove AAMVAMsgId 
		// defect 9961
		//	Add AAMVAMSGID, UTVMISLBLINDI, VTRREGEMRGCD1, VTRREGEMRGCD2,
		//		PVTLAWENFVEHCD, NONTTLGOLFCARTCD
		// AAMVAMSGID
		laMVFuncData
			.append(
				caMfAccess
				.resizeStringtoLength(CommonConstant.STR_SPACE_EMPTY,
		//laMVFuncTransData.getAAMVAMsgId(),
		IL_TMV_AAMVAMSGID_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 9969 

		// UTVMISLBLINDI
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaMVFuncTransData.getUTVMislblIndi()),
				IL_TMV_UTVMISLBLINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// VTRREGEMRGCD1
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVTRRegEmrgCd1(),
				IL_TMV_VTRREGEMRGCD1_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// VTRREGEMRGCD2
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVTRRegEmrgCd2(),
				IL_TMV_VTRREGEMRGCD2_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// defect 10366 
		// PVTLAWENFVEHCD
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getPvtLawEnfVehCd(),
				IL_TMV_PVTLAWENFVEHCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// NONTTLGOLFCARTCD
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getNonTtlGolfCartCd(),
				IL_TMV_NONTTLGOLFCARTCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 10366

		// defect 10389
		// New attributes
		// VTRTTLEMRGCD3
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVTRTtlEmrgCd3(),
				IL_TMV_VTRTTLEMRGCD3_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// VTRTTLEMRGCD4
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getVTRTtlEmrgCd4(),
				IL_TMV_VTRTTLEMRGCD4_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// RECPNTEMAIL
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				aaMVFuncTransData.getRecpntEMail(),
				IL_TMV_RECPNTEMAIL_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// defect 10508 
		// EMAILRENWLREQCD
		laMVFuncData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getEMailRenwlReqCd()),
				IL_TMV_EMAILRENWLREQCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// end defect 10508 
		// end defect 10389
		// defect 10595 
		// TTLEXMNINDI
		 laMVFuncData.append(
			 caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaMVFuncTransData.getTtlExmnIndi()),
				IL_TMV_TTLEXMNINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
						 .SC_MFA_INSERT_AT_BEGINNING));
		// end defect 10595
		// defect 10710
		// VEHMJRCOLORCD
			 laMVFuncData.append(
				 caMfAccess.resizeStringtoLength(
					 aaMVFuncTransData.getVehMjrColorCd(),
					 IL_TMV_VEHMJRCOLORCD_LENGTH,
					 lchBufferChar,
					 ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// VEHMNRCOLORCD
			 laMVFuncData.append(
				 caMfAccess.resizeStringtoLength(
					 aaMVFuncTransData.getVehMnrColorCd(),
					 IL_TMV_VEHMNRCOLORCD_LENGTH,
					 lchBufferChar,
					 ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 10710
		// defect 11045 	 
		laMVFuncData.append(
					 caMfAccess.resizeStringtoLength(
						Integer.toString(
							aaMVFuncTransData.getETtlPrntDate()),
						IL_TMV_ETTLPRNTDATE_LENGTH,
						lchBufferInt,
						ApplicationControlConstants
								 .SC_MFA_INSERT_AT_BEGINNING)); 
		// end defect 11045 
		
		//defect 11251
		 laMVFuncData.append(
				 caMfAccess.resizeStringtoLength(
					 aaMVFuncTransData.getSurvShpRightsName1(),
					 IL_TMV_SURVSHPRIGHTSNAME1_LENGTH,
					 lchBufferChar,
					 ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		 laMVFuncData.append(
				 caMfAccess.resizeStringtoLength(
					 aaMVFuncTransData.getSurvShpRightsName2(),
					 IL_TMV_SURVSHPRIGHTSNAME2_LENGTH,
					 lchBufferChar,
					 ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		laMVFuncData.append(
				 caMfAccess.resizeStringtoLength(
					Integer.toString(
						aaMVFuncTransData.getAddlSurvivorIndi()),
					IL_TMV_ADDLSURVIVORINDI_LENGTH,
					lchBufferInt,
					ApplicationControlConstants
							 .SC_MFA_INSERT_AT_BEGINNING));
		laMVFuncData.append(
				 caMfAccess.resizeStringtoLength(
					Integer.toString(
						aaMVFuncTransData.getExportIndi()),
					IL_TMV_EXPORTINDI_LENGTH,
					lchBufferInt,
					ApplicationControlConstants
							 .SC_MFA_INSERT_AT_BEGINNING));
		laMVFuncData.append(
				 caMfAccess.resizeStringtoLength(
					Integer.toString(
						aaMVFuncTransData.getSalvIndi()),
					IL_TMV_SALVINDI_LENGTH,
					lchBufferInt,
					ApplicationControlConstants
							 .SC_MFA_INSERT_AT_BEGINNING));
		//end defect 11251 
		return laMVFuncData.toString();
	}

	/**
	 * Returns a valid mainframe update string from the values in the
	 * <code>TransactionData</code> object. This string is
	 * corresponds to the mainframe buffer <it>TRANS</it>. 
	 * 
	 * @return java.lang.String
	 * @param aaTransactionData TransactionData
	 */

	private String getValuesFromTransactionData(TransactionData aaTransactionData)
	{

		StringBuffer lsTransData = new StringBuffer();
		char lchBufferChar = ' ';
		char lchBufferInt = '0';

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getTransCd(),
				IL_TT_TRANSCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getOfcIssuanceNo()),
				IL_TT_OFCISSUANCENO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getTransWsId()),
				IL_TT_TRANSWSID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getTransAMDate()),
				IL_TT_TRANSAMDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getTransTime()),
				IL_TT_TRANSTIME_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getOfcIssuanceCd()),
				IL_TT_OFCISSUANCECD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getSubstaId()),
				IL_TT_SUBSTAID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getCashWsId()),
				IL_TT_CASHWSID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getTransEmpId(),
				IL_TT_TRANSEMPID_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionData.getIncomplTransIndi()),
				IL_TT_SELCTDTRANSINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionData.getTransPostedMfIndi()),
				IL_TT_TRANSPOSTEDMFINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionData.getTransPostedLANIndi()),
				IL_TT_TRANSPOSTEDLANINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionData.getVoidedTransIndi()),
				IL_TT_VOIDEDTRANSINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getRegPltNo(),
				IL_TT_REGPLTNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getDocNo(),
				IL_TT_DOCNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getOwnrId(),
				IL_TT_OWNRID_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getVIN(),
				IL_TT_VIN_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getRegStkrNo(),
				IL_TT_REGSTKRNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getCustSeqNo()),
				IL_TT_CUSTSEQNO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getCustName1(),
				IL_TT_CUSTNAME1_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getCustName2(),
				IL_TT_CUSTNAME2_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		//		lsTransData.append(
		//			caMfAccess.resizeStringtoLength(
		//				aaTransactionData.getCustLstName(),
		//				IL_TT_CUSTLSTNAME_LENGTH,
		//				lchBufferChar,
		//				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		//
		//		lsTransData.append(
		//			caMfAccess.resizeStringtoLength(
		//				aaTransactionData.getCustFstName(),
		//				IL_TT_CUSTFSTNAME_LENGTH,
		//				lchBufferChar,
		//				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		//
		//		lsTransData.append(
		//			caMfAccess.resizeStringtoLength(
		//				aaTransactionData.getCustMIName(),
		//				IL_TT_CUSTMINAME_LENGTH,
		//				lchBufferChar,
		//				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getCustSt1(),
				IL_TT_CUSTST1_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getCustSt2(),
				IL_TT_CUSTST2_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getCustCity(),
				IL_TT_CUSTCITY_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getCustState(),
				IL_TT_CUSTSTATE_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getCustZpCd(),
				IL_TT_CUSTZPCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getCustZpCdP4(),
				IL_TT_CUSTZPCDP4_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getCustCntry(),
				IL_TT_CUSTCNTRY_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getDLSCertNo(),
				IL_TT_DLSCERTNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getEffDate()),
				IL_TT_EFFDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getEffTime()),
				IL_TT_EFFTIME_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getExpDate()),
				IL_TT_EXPDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getExpTime()),
				IL_TT_EXPTIME_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getRegClassCd()),
				IL_TT_REGCLASSCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getTireTypeCd(),
				IL_TT_TIRETYPECD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getVehBdyType(),
				IL_TT_VEHBDYTYPE_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getVehCaryngCap()),
				IL_TT_VEHCARYNGCAP_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getVehEmptyWt()),
				IL_TT_VEHEMPTYWT_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getVehGrossWt()),
				IL_TT_VEHGROSSWT_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getVehMk(),
				IL_TT_VEHMK_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getVehModlYr()),
				IL_TT_VEHMODLYR_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getVehRegState(),
				IL_TT_VEHREGSTATE_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getVehUnitNo(),
				IL_TT_VEHUNITNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionData.getVoidOfcIssuanceNo()),
				IL_TT_VOIDOFCISSUANCENO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getVoidTransWsId()),
				IL_TT_VOIDTRANSWSID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionData.getVoidTransAMDate()),
				IL_TT_VOIDTRANSAMDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getVoidTransTime()),
				IL_TT_VOIDTRANSTIME_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getDieselIndi()),
				IL_TT_DIESELINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionData.getRegPnltyChrgIndi()),
				IL_TT_REGPNLTYCHRGINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionData.getProcsdByMailIndi()),
				IL_TT_PROCSDBYMAILINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getVersionCd()),
				IL_TT_VERSIONCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionData.getBsnDate()),
				IL_TT_BSNDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//this is a dollar amount
		String lsBsnDateTotalAmt =
			aaTransactionData.getBsnDateTotalAmt().toString();
		int liDecimalPtPosition = lsBsnDateTotalAmt.indexOf(".");
		StringBuffer laBsnDateTotalAmt =
			new StringBuffer(lsBsnDateTotalAmt);
		laBsnDateTotalAmt.replace(
			liDecimalPtPosition,
			liDecimalPtPosition + 1,
			CommonConstant.STR_SPACE_EMPTY);

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				laBsnDateTotalAmt.toString(),
				IL_TT_BSNDATETOTALAMT_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		//////////////////////////////////////////////

		lsTransData.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionData.getVoidTransCd(),
				IL_TT_VOIDTRANSCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// defect 10492 
		appendToBuffer(
			lsTransData,
			aaTransactionData.getAddPrmtRecIndi(),
			IL_TT_ADDPRMTRECINDI_LENGTH);

		appendToBuffer(
			lsTransData,
			aaTransactionData.getDelPrmtRecIndi(),
			IL_TT_DELPRMTRECINDI_LENGTH);
		// end defect 01492 

		return lsTransData.toString();
	}

	/**
	 * Returns a valid mainframe update string from the values in the
	 * <code>TransactionPaymentData</code> object. This string is
	 * corresponds to the mainframe buffer <it>TRANPYMT</it>. 
	 * 
	 * @return String
	 * @param aaTransactionPaymentData TransactionPaymentData
	 */
	private String getValuesFromTransactionPaymentData(TransactionPaymentData aaTransactionPaymentData)
	{
		//define the StringBuffer
		StringBuffer lsTransPymnt = new StringBuffer();

		char lchBufferChar = ' ';
		char lchBufferInt = '0';

		// The order in which these fields are added to the buffer is 
		// important 
		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionPaymentData.getTransAMDate()),
				IL_TTP_TRANSAMDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionPaymentData.getCashWsId()),
				IL_TTP_CASHWSID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionPaymentData.getPymntNo(),
				IL_TTP_PYMNTNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionPaymentData.getOfcIssuanceNo()),
				IL_TTP_OFCISSUANCENO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionPaymentData.getTransWsId()),
				IL_TTP_TRANSWSID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionPaymentData.getCustSeqNo()),
				IL_TTP_CUSTSEQNO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//Dollar field
		lsTransPymnt.append(
			caMfAccess.getValueFromDollar(
				aaTransactionPaymentData.getPymntTypeAmt(),
				IL_TTP_PYMNTTYPEAMT_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionPaymentData.getPymntCkNo(),
				IL_TTP_PYMNTCKNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		//begin 10783
		lsTransPymnt.append(
			caMfAccess.getValueFromDollar(
				aaTransactionPaymentData.getChngDue(),
				IL_TTP_CHNGDUE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		//end 10783					

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionPaymentData.getTransPostedLANIndi()),
				IL_TTP_TRANSPOSTEDLANINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionPaymentData.getTransPostedMfIndi()),
				IL_TTP_TRANSPOSTEDMFINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionPaymentData.getVersionCd()),
				IL_TTP_VERSIONCD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaTransactionPaymentData.getBsnDate()),
				IL_TTP_BSNDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				aaTransactionPaymentData.getTransEmpId(),
				IL_TTP_TRANSEMPID_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionPaymentData.getSubstaId()),
				IL_TTP_SUBSTAID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionPaymentData.getPymntTypeCd()),
				IL_TTP_PYMNTTYPECD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsTransPymnt.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaTransactionPaymentData.getChngDuePymntTypeCd()),
				IL_TTP_CHNGDUEPYMNTTYPECD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		//return the result
		return lsTransPymnt.toString();
	}

	/**
	 * Returns a valid mainframe update string from the values in the
	 * <code>TransactionPaymentData</code> object. This string is
	 * corresponds to the mainframe buffer <it>TRANPYMT</it>. 
	 * 
	 * @return String
	 * @param aaTransactionPaymentData TransactionPaymentData
	 */
	private String getValuesFromSRFuncTransData(SpecialRegistrationFunctionTransactionData aaSRFuncTransData)
	{
		//define the StringBuffer
		StringBuffer lsSRFuncTrans = new StringBuffer();

		char lchBufferChar = ' ';
		char lchBufferInt = '0';

		// The order in which these fields are added to the buffer is 
		// important 
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getTransCd(),
				IL_TSR_TRANSCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getOfcIssuanceNo()),
				IL_TSR_OFCISSUANCENO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getTransWsId()),
				IL_TSR_TRANSWSID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getTransAMDate()),
				IL_TSR_TRANSAMDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getTransTime()),
				IL_TSR_TRANSTIME_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getOfcIssuanceCd()),
				IL_TSR_OFCISSUANCECD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getTransEmpId(),
				IL_TSR_TRANSEMPID_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				String.valueOf(aaSRFuncTransData.getSpclRegId()),
				IL_TSR_SPCLREGID_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getOrgNo(),
				IL_TSR_ORGNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getAddlSetIndi()),
				IL_TSR_ADDLSETINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getPltExpMo()),
				IL_TSR_PLTEXPMO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getPltExpYr()),
				IL_TSR_PLTEXPYR_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getMfgPltNo(),
				IL_TSR_MFGPLTNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getMfgStatusCd(),
				IL_TSR_MFGSTATUSCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// defect 10112 
		OwnerData laPltOwnrData = aaSRFuncTransData.getPltOwnerData();

		lsSRFuncTrans.append(caMfAccess.resizeStringtoLength(
		//aaSRFuncTransData.getPltOwnrId(),
		laPltOwnrData.getOwnrId(),
			IL_TSR_PLTOWNRID_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsSRFuncTrans.append(caMfAccess.resizeStringtoLength(
		// aaSRFuncTransData.getPltOwnrName1(),
		laPltOwnrData.getName1(),
			IL_TSR_PLTOWNRNAME1_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsSRFuncTrans.append(caMfAccess.resizeStringtoLength(
		//aaSRFuncTransData.getPltOwnrName2(),
		laPltOwnrData.getName2(),
			IL_TSR_PLTOWNRNAME2_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 10112 

		// getPltOwnrLstName is not passed
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				CommonConstant.STR_SPACE_ONE,
				IL_TSR_PLTOWNRLSTNAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// getPltOwnrFstName is not passed
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				CommonConstant.STR_SPACE_ONE,
				IL_TSR_PLTOWNRFSTNAME_LENGTH,
				lchBufferChar,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// getPltOwnrMi is not passed
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				CommonConstant.STR_SPACE_ONE,
				IL_TSR_PLTOWNRMI_LENGTH,
				lchBufferChar,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// defect 10112 
		AddressData laPltAddrData = laPltOwnrData.getAddressData();

		lsSRFuncTrans.append(caMfAccess.resizeStringtoLength(
		// aaSRFuncTransData.getPltOwnrSt1(),
		laPltAddrData.getSt1(),
			IL_TSR_PLTOWNRST1_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsSRFuncTrans.append(caMfAccess.resizeStringtoLength(
		//aaSRFuncTransData.getPltOwnrSt2(),
		laPltAddrData.getSt2(),
			IL_TSR_PLTOWNRST2_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsSRFuncTrans.append(caMfAccess.resizeStringtoLength(
		// aaSRFuncTransData.getPltOwnrCity(),
		laPltAddrData.getCity(),
			IL_TSR_PLTOWNRCITY_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsSRFuncTrans.append(caMfAccess.resizeStringtoLength(
		//aaSRFuncTransData.getPltOwnrState(),
		laPltAddrData.getState(),
			IL_TSR_PLTOWNRSTATE_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsSRFuncTrans.append(caMfAccess.resizeStringtoLength(
		// aaSRFuncTransData.getPltOwnrZpCd(),
		laPltAddrData.getZpcd(),
			IL_TSR_PLTOWNRZPCD_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsSRFuncTrans.append(caMfAccess.resizeStringtoLength(
		//aaSRFuncTransData.getPltOwnrZpCdP4(),
		laPltAddrData.getZpcdp4(),
			IL_TSR_PLTOWNRZPCDP4_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsSRFuncTrans.append(caMfAccess.resizeStringtoLength(
		//aaSRFuncTransData.getPltOwnrCntry(),
		laPltAddrData.getCntry(),
			IL_TSR_PLTOWNRCNTRY_LENGTH,
			lchBufferChar,
			ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 10112 

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getPltOwnrPhone(),
				IL_TSR_PLTOWNRPHONENO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getPltOwnrEMail(),
				IL_TSR_PLTOWNREMAIL_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getSAuditTrailTransId(),
				IL_TSR_SAUDITTRAILTRANSID_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// defect 10389
		// New AbstractAttribute
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getResrvReasnCd(),
				IL_TSR_RESRVREASNCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 10389

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getMfgDate()),
				IL_TSR_MFGDATE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getPltSetNo()),
				IL_TSR_PLTSETNO_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getSpclDocNo(),
				IL_TSR_SPCLDOCNO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// getSpclFee is not passed
		// Must be Dollar
		String lsSpclFee = CommonConstant.STR_ZERO_DOLLAR;
		int liDecimalPtPosition = lsSpclFee.indexOf(".");
		StringBuffer laSpclFee = new StringBuffer(lsSpclFee);
		laSpclFee.replace(
			liDecimalPtPosition,
			liDecimalPtPosition + 1,
			CommonConstant.STR_SPACE_EMPTY);
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				laSpclFee.toString(),
				IL_TSR_SPCLFEE_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// end SpclFee

		// PltOmnrOfcCd
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getPltOwnrOfcCd(),
				IL_TSR_PLTOWNROFCCD_LENGTH,
				lchBufferChar,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// PltOwnrDist
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getPltOwnrDist(),
				IL_TSR_PLTOWNRDIST_LENGTH,
				lchBufferChar,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// defect 10389
		// MKTNGALLOWDINDI
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaSRFuncTransData.getMktngAllowdIndi()),
				IL_TSR_MKTNGALLOWDINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// end defect 10389

		// ISAIndi
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getISAIndi()),
				IL_TSR_ISAINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// SpclRemks
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getSpclRemks(),
				IL_TSR_SPCLREMKS_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));

		// defect 10492 / 10507
		appendToBuffer(
			lsSRFuncTrans,
			aaSRFuncTransData.getElectionPndngIndi(),
			IL_TSR_ELECTIONPNDNGINDI_LENGTH);
		// end defect 10492 / 10507 

		// defect 10389
		// changed name
		// defect 9557
		// MF calls this field PLTOWNRDLRGDNNEW  
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getPltOwnrDlrGDN(),
				IL_TSR_PLTOWNRDLRGDN_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// end defect 9557
		// end defect 10389

		// DissociateCd
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getDissociateCd()),
				IL_TSR_DISSOCIATECD_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// defect 10389
		// PltValidityTerm
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(
					aaSRFuncTransData.getPltValidityTerm()),
				IL_TSR_PLTVALIDITYTERM_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// PLTSOLDMOS
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getPltSoldMos()),
				IL_TSR_PLTSOLDMOS_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// ITRNTTRACENO
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				aaSRFuncTransData.getItrntTraceNo(),
				IL_TSR_ITRNTTRACENO_LENGTH,
				lchBufferChar,
				ApplicationControlConstants.SC_MFA_INSERT_AT_END));
		// AUCTNPLTINDI
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aaSRFuncTransData.getAuctnPltIndi()),
				IL_TSR_AUCTNPLTINDI_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
		// AUCTNPDAMT
		// Must be Dollar
		String lsAuctPdAmt =
			aaSRFuncTransData.getAuctnPdAmt().toString();
		liDecimalPtPosition = lsAuctPdAmt.indexOf(".");
		StringBuffer laAuctPdAmt = new StringBuffer(lsAuctPdAmt);
		laAuctPdAmt.replace(
			liDecimalPtPosition,
			liDecimalPtPosition + 1,
			CommonConstant.STR_SPACE_EMPTY);
		lsSRFuncTrans.append(
			caMfAccess.resizeStringtoLength(
				laAuctPdAmt.toString(),
				IL_TSR_AUCTNPDAMT_LENGTH,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));

		// end defect 10389
		//return the result
		return lsSRFuncTrans.toString();
	}
	/**
	 * Returns a valid mainframe update string from the values in the
	 * <code>TransactionPaymentData</code> object. This string is
	 * corresponds to the mainframe buffer <it>TRANPYMT</it>. 
	 * 
	 * @return String
	 * @param aaPrmtTransData PermitTransactionData
	 */
	private String getValuesFromPermitTransactionData(PermitTransactionData aaPrmtTransData)
	{
		//define the StringBuffer
		StringBuffer lsPrmtTrans = new StringBuffer();

		// TransCd
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getTransCd(),
			IL_PT_TRANSCD_LENGTH);

		// OfcissuanceNo 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getOfcIssuanceNo(),
			IL_PT_OFCISSUANCENO_LENGTH);

		// TransWsId 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getTransWsId(),
			IL_PT_TRANSWSID_LENGTH);

		// TransAMDate 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getTransAMDate(),
			IL_PT_TRANSAMDATE_LENGTH);

		// TransTime 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getTransTime(),
			IL_PT_TRANSTIME_LENGTH);

		// TransEmpId
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getTransEmpId(),
			IL_PT_TRANSEMPID_LENGTH);

		// PrmtIssuanceId 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getPrmtIssuanceId(),
			IL_PT_PRMTISSUANCEID_LENGTH);

		// CustLstName 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustLstName(),
			IL_PT_CUSTLSTNAME_LENGTH);

		// CustFstName 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustFstName(),
			IL_PT_CUSTFSTNAME_LENGTH);

		// CustMIName 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustMIName(),
			IL_PT_CUSTMINAME_LENGTH);

		// CustBsnName 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustBsnName(),
			IL_PT_CUSTBSNNAME_LENGTH);

		// CustSt1 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustSt1(),
			IL_PT_CUSTST_LENGTH);

		// CustSt2 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustSt2(),
			IL_PT_CUSTST_LENGTH);

		// CustCity 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustCity(),
			IL_PT_CUSTCITY_LENGTH);

		// CustState 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustState(),
			IL_PT_CUSTSTATE_LENGTH);

		// CustZpcd 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustZpcd(),
			IL_PT_CUSTZPCD_LENGTH);

		// CustZpcdP4 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustZpcdP4(),
			IL_PT_CUSTZPCDP4_LENGTH);

		// CustCntry 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustCntry(),
			IL_PT_CUSTCNTRY_LENGTH);

		// CustEMail 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustEMail(),
			IL_PT_CUSTEMAIL_LENGTH);

		// CustPhone
		// MF is Numeric 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getCustPhone(),
			IL_PT_CUSTPHONE_LENGTH,
			'0');

		// PrmtNo 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getPrmtNo(),
			IL_PT_PRMTNO_LENGTH);

		// ItmCd 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getItmCd(),
			IL_PT_ITMCD_LENGTH);

		// AcctItmCd 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getAcctItmCd(),
			IL_PT_ACCTITMCD_LENGTH);

		// PrmtPdAmt  
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getPrmtPdAmt(),
			IL_PT_PRMTPDAMT_LENGTH);

		// EffDate 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getEffDate(),
			IL_PT_EFFDATE_LENGTH);

		// EffTime 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getEffTime(),
			IL_PT_EFFTIME_LENGTH);

		// ExpDate 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getExpDate(),
			IL_PT_EXPDATE_LENGTH);

		// ExpTime 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getExpTime(),
			IL_PT_EXPTIME_LENGTH);

		// VehBdyType 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getVehBdyType(),
			IL_PT_VEHBDYTYPE_LENGTH);

		// VehMk 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getVehMk(),
			IL_PT_VEHMK_LENGTH);
			
		// VehMkDesc 
		appendToBuffer(
			 lsPrmtTrans,
			 aaPrmtTransData.getVehMkDesc(),
			IL_PT_VEHMKDESC_LENTH);

		// VehModlYr 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getVehModlYr(),
			IL_PT_VEHMODLYR_LENGTH);

		// VIN
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getVin(),
			IL_PT_VIN_LENGTH);

		// VehRegCntry
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getVehRegCntry(),
			IL_PT_VEHREGCNTRY_LENGTH);

		// VehRegState
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getVehRegState(),
			IL_PT_VEHREGSTATE_LENGTH);

		// VehRegPltNo
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getVehRegPltNo(),
			IL_PT_VEHREGPLTNO_LENGTH);

		// OneTripPrmtOrigPnt 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getOneTripPrmtOrigPnt(),
			IL_PT_ONETRIPPNT_LENGTH);

		// OneTripPrmtPnt1 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getOneTripPrmtPnt1(),
			IL_PT_ONETRIPPNT_LENGTH);

		// OneTripPrmtPnt2 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getOneTripPrmtPnt2(),
			IL_PT_ONETRIPPNT_LENGTH);

		// OneTripPrmtPnt3 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getOneTripPrmtPnt3(),
			IL_PT_ONETRIPPNT_LENGTH);

		// OneTripPrmtDestPnt 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getOneTripPrmtDestPnt(),
			IL_PT_ONETRIPPNT_LENGTH);

		// MfDwnCd 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getMfDwnCd(),
			IL_PT_MFDOWN_LENGTH);
			
		// BulkPrmtVendorId 
		appendToBuffer(
			lsPrmtTrans,
			aaPrmtTransData.getBulkPrmtVendorId(),
			IL_PT_BULKPRMTVENDORID_LENGTH);
			
		// VoidedTransIndi 
		 appendToBuffer(
			 lsPrmtTrans,
			 aaPrmtTransData.getVoidedTransIndi(),
			 IL_PT_VOIDEDTRANSINDI_LENGHT);
		

		return lsPrmtTrans.toString();

	}

	/**
	 * Append to Buffer (String) 
	 * 
	 * @param asTransBuffer
	 * @param aiValue
	 * @param aiLen
	 */
	private void appendToBuffer(
		StringBuffer asTransBuffer,
		String asValue,
		int aiLength)
	{
		appendToBuffer(asTransBuffer, asValue, aiLength, ' ');
	}
	
	/**
	 * Append to Buffer (String)  
	 * 
	 * @param asTransBuffer
	 * @param aiValue
	 * @param aiLen
	 */
	private void appendToBuffer(
		StringBuffer asTransBuffer,
		String asValue,
		int aiLength,
		char achBufferChar)
	{
		asTransBuffer.append(
			caMfAccess.resizeStringtoLength(
				asValue,
				aiLength,
				achBufferChar,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_END));
	}

	/**
	 * Append to Buffer (String) 
	 * 
	 * @param asTransBuffer
	 * @param aiValue
	 * @param aiLength
	 */
	private void appendToBuffer(
		StringBuffer asTransBuffer,
		int aiValue,
		int aiLength)
	{
		char lchBufferInt = '0';
		asTransBuffer.append(
			caMfAccess.resizeStringtoLength(
				Integer.toString(aiValue),
				aiLength,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
	}

	/**
	 * Append to Buffer (Dollar) 
	 * 
	 * @param asTransBuffer
	 * @param aaValue
	 * @param aiLength
	 */
	private void appendToBuffer(
		StringBuffer asTransBuffer,
		Dollar aaValue,
		int aiLength)
	{
		char lchBufferInt = '0';
		asTransBuffer.append(
			caMfAccess.getValueFromDollar(
				aaValue,
				aiLength,
				lchBufferInt,
				ApplicationControlConstants
					.SC_MFA_INSERT_AT_BEGINNING));
	}
}
