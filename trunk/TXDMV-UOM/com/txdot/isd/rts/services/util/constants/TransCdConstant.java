package com.txdot.isd.rts.services.util.constants;

/*
 *
 * TransCdConstant.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Joseph K		09/20/2001	add reg comment
 * Bob Brown	12/16/2003	Added the variable INTERNET, so when either 
 *							address change, or registration renewals are
 *							done, this variable gets used. 
 *                          defect 6709. Version 5.1.5 fix 2.
 * Jeff S.		02/23/2004	added TTLP for Title Package Report
 *							add TTLP
 *							defect 6848, 6898 Ver 5.1.6
 * K Harrell	03/29/2004	removed QRENEW
 *							Ver 5.2.0
 * K Harrell	04/07/2004	add RPTSTK as transaction code
 *							Ver 5.2.0
 * J Zwiener	07/17/2005  Enhancement for Disable Placard event
 * 							add BPM,BTM,RPNM,RTNM
 * 							remove PDC,TDC
 * 							defect 8268 Ver 5.2.2 Fix 6
 * K Harrell	08/31/2005	Remove references to RLSALL, TTLREJ, TTLRLS,
 * 							FNDADJ,INVADJ
 * 							defect 8348 Ver 5.2.3	
 * K Harrell	09/20/2005  Add missing transcd for repainting Desktop
 * 							Menu	
 * 							add FNDINQ  
 * 							defect 8337 Ver 5.2.3 
 * K Harrell	02/06/2007	Added Special Plates Transaction Constants
 * 							SPAPPL, SPRNW, SPREV, SPRSRV, SPUNAC, SPDEL
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/30/2007	Added SPRNR
 * 							defect 9085 Ver Special Plates
 * B Brown		04/12/2007	Added APPLICATION
 * 							defect 9119 Ver Special Plates
 * K Harrell	04/19/2007	Added constants for event types for 
 * 							accessing TransactionControl
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/20/2007	Added IAPPL
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/31/2007	Added SPAPPC,SPAPPI,SPAPPO,SPAPPR
 * 							defect 9085 Ver Special Plates  
 * B Hargrove	01/24/2008	Added V21VTN
 * 							(Vision 21 Vehicle Transaction Notification)
 * 							defect 9502 Ver 
 * B Hargrove	02/07/2008	Added V21PLD
 * 							(Vision 21 Plate to Owner)
 * 							defect 9502 Ver 
 * Ray Rowehl	02/19/2008	Added Global 360 VTN
 * 							add G36VTN
 * 							defect 9502 Ver 
 * K Harrell	04/26/2008	Add constants for Corrected SCOT, NRCOT	
 * 							add CORNRT, CORSLV
 * 							delete SLVG, DPLSLG  
 * 							defect 9636 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9636 Defect_POS_A
 * B Brown		06/17/2008	Added new constants for MyPlates
 * 							add VPAPPL and VPAPPR.
 * 							defect 9711 Ver MyPlates_POS. 
 * K Harrell	10/27/2008	Added constants for enhanced Disabled Placard
 * 							processing. 
 * 							add DLPBPM, DLBTM, DLRPNM, DLRTNM, 
 * 							 RPPBPM, RPBTM, RPRPNM, RPRTNM
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	02/25/2009  Add constant for Certified Lienholder 
 * 							report. 
 * 							add CERTFD_LIENHLDR_RPT
 * 							defect 9971 Ver Defect_POS_E  
 * K Harrell	07/26/2009	Modify for HB3095: Disregard Mobility 
 * 							 for Disabled Placards
 * 							add TDC, PDC, RPLTDC, RPLPDC, DELTDC, DELPDC 
 * 							delete DLBPM, DLBTM, DLRPNM, DLRTNM, RPBPM,
 * 							 RPBTM, RPRPNM, RPRTNM
 * 							defect 10133 Ver Defect_POS_F
 * K Harrell	10/13/2009	Run Local Options Reports by Id/Name 
 * 							add CERTFD_LIENHLDR_RPT_ALPHA_SORT,
 *							 CERTFD_LIENHLDR_RPT_ID_SORT, 
 *							 LIENHLDR_RPT_ALPHA_SORT, 
 * 							 LIENHLDR_RPT_ID_SORT,
 *							 DEALER_RPT_ALPHA_SORT,
 *							 DEALER_RPT_ID_SORT,
 * 							 SUBCON_RPT_NAME_SORT, 
 *							 SUBCON_RPT_ID_SORT 
 *							delete CERTFD_LIENHLDR_RPT, 
 * 							 LIENHLDR_RPT, DLR_RPT, SUBCON_RPT 
 *							defect 10250 Ver Defect_POS_G
 * Ray Rowehl	03/26/2010	Add Vendor Plates Restyle.
 *							add VPRSTL
 *							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/09/2010	Add Vendor Plates Delete, Revise, Reserve
 * 							and Unacceptable.
 *							add VPDEL, VPREV, VPRSRV, VPUNAC
 *							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/13/2010	Add port and redo.	
 *							add VPPORT, VPREDO
 *							defect 10401 Ver 6.4.0
 * K Harrell	05/24/2010	Add new TransCode for Duplicate Permit Receipt
 * 							Remove TransCode for 24 Hour Permit  
 * 							add PRMDUP
 * 							delete PT24  
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/14/2010	add RPRPRM
 * 							defect 10491 Ver 6.5.0 
 * B Brown		12/08/2010	add IADDRE
 * 							defect 10610 Ver 6.7.0 
 * K Harrell	12/21/2010	add DPSPPT
 * 							defect 10700 Ver 6.7.0 
 * K Harrell	01/12/2011	add WRENEW 
 * 							defect 10708 Ver 6.7.0 
 * K Harrell	05/28/2011	add MODPT
 * 							defect 10844 Ver 6.8.0 
 * R Pilon		06/10/2011	add SPINQ
 * 							defect 10820 Ver 6.8.0
 * K Harrell	11/14/2011	add VDSC, VDSCN, VDS, VDSN
 * 							defect 11052 Ver 6.9.0 
 * K Harrell	01/11/2012	add RENPDC, REIPDC, REITDC 
 * 							defect 11214 Ver 6.10.0 
 * ------------------------------------------------------------------
 */

/**
 * Provides variables for RTS transaction types to be used by the system
 *
 * @version	6.10.0  		01/11/2012
 * @author	Joseph Kwik
 * <br>Creation Date:		09/05/2001 09:28:25
 */

public class TransCdConstant
{

	// defect 9085
	// Event Types (for TransactionControl)
	public final static String REG_EVENT_TYPE = "REG";
	public final static String TTL_EVENT_TYPE = "TTL";
	public final static String DTA_EVENT_TYPE = "DTA";
	public final static String VEHINQ_EVENT_TYPE = "VEHINQ";

	// defect 9831 
	public final static String ADD_DP_EVENT_TYPE = "ADD_DP";
	public final static String RPL_DP_EVENT_TYPE = "RPL_DP";
	public final static String DEL_DP_EVENT_TYPE = "DEL_DP";
	// end defect 9831
	
	// defect 11214 
	public final static String REI_DP_EVENT_TYPE = "REI_DP";
	public final static String REN_DP_EVENT_TYPE = "REN_DP";
	// end defect 11214 

	public final static String MRG_EVENT_TYPE = "MRG";
	public final static String SPCLPLT_EVENT_TYPE = "SPCLPLT";
	public final static String MISC_EVENT_TYPE = "MISC";
	public final static String ACC_EVENT_TYPE = "ACC";
	public final static String INV_EVENT_TYPE = "INV";
	public final static String SLVG_EVENT_TYPE = "SLVG";
	// end defect 9085

	// Registration Transaction Codes
	public final static String RENEW = "RENEW";
	public final static String ADDR = "ADDR";
	public final static String RNR = "RNR";
	public final static String EXCH = "EXCH";
	public final static String CORREG = "CORREG";
	public final static String CORREGX = "CORREGX";
	public final static String PAWT = "PAWT";
	public final static String REPL = "REPL";
	public final static String DUPL = "DUPL";
	public final static String SBRNW = "SBRNW";

	public final static String VEHINQ = "VEHINQ";

	// Accounting Transaction Codes
	public final static String REFUND = "REFUND";
	public final static String CKREDM = "CKREDM";
	public final static String HOTDED = "HOTDED";
	public final static String HOTCK = "HOTCK";
	public final static String HCKITM = "HCKITM";
	public final static String RGNCOL = "RGNCOL";
	public final static String ADLCOL = "ADLCOL";
	public final static String FNDREM = "FNDREM";
	public final static String EFTFND = "EFTFND";
	public final static String VOIDFD = "VOIDFD";
	public final static String RFCASH = "RFCASH";
	public final static String FNDINQ = "FNDINQ";

	// Funds Transaction Codes
	public final static String CLOSE = "CLOSE";
	public final static String CURRENT = "CURRENT";
	public final static String DETAIL = "DETAIL";
	public final static String BALANCE = "BALANCE";
	public final static String CLOSEOUT_STATS_RPT =
		"CLOSEOUT_STATS_RPT";

	// Title Transaction Codes
	public final static String TITLE = "TITLE";
	public final static String CORTTL = "CORTTL";
	public final static String NONTTL = "NONTTL";
	public final static String REJCOR = "REJCOR";
	public final static String DTAORD = "DTAORD";
	public final static String DTAORK = "DTAORK";
	public final static String DTANTD = "DTANTD";
	public final static String DTANTK = "DTANTK";
	public final static String CCO = "CCO";
	public final static String COA = "COA";
	public final static String SCOT = "SCOT";
	public final static String NRCOT = "NRCOT";
	public final static String CCOSCT = "CCOSCT";
	public final static String CCONRT = "CCONRT";
	public final static String STAT = "STAT";
	public final static String STATJK = "STATJK";
	public final static String STATRF = "STATRF";
	public final static String REGIVD = "REGIVD";
	public final static String ADLSTX = "ADLSTX";

	// defect 9636
	//public final static String DPLSLG = "DPLSLG";
	//public final static String SLVG = "SLVG"; 
	public final static String CORNRT = "CORNRT";
	public final static String CORSLV = "CORSLV";
	// end defect 9636 

	// defect 6848, 6898
	// Added for Title Package Report
	public final static String TTLP = "TTLP";
	// end defect 6848, 6898

	// Local Options Codes
	public final static String LIENHLDR_RPT = "LIENHLDR_RPT";
	public final static String DLR_RPT = "DLR_RPT";
	public final static String SUBCON_RPT = "SUBCON_RPT";
	public final static String REPRINT_RPT = "REPRINT_RPT";
	public final static String PUBLISHING_RPT = "PUBLISHING_RPT";
	public final static String EVTSEC_RPT = "EVTSEC_RPT";
	public final static String SECCHG_RPT = "SECCHG_RPT";
	public final static String EMPSEC_RPT = "EMPSEC_RPT";
	public final static String SP_ENA = "SP_ENA";
	public final static String SP_DIS = "SP_DIS";

	// defect 10250 
	public final static String CERTFD_LIENHLDR_RPT_NAME_SORT =
		"CERTFD_LIENHLDR_RPT_ALPHA_SORT";
	public final static String CERTFD_LIENHLDR_RPT_ID_SORT =
		"CERTFD_LIENHLDR_RPT_ID_SORT";

	public final static String LIENHLDR_RPT_ALPHA_SORT =
		"LIENHLDR_RPT_ALPHA_SORT";
	public final static String LIENHLDR_RPT_ID_SORT =
		"LIENHLDR_RPT_ID_SORT";

	public final static String DEALER_RPT_ALPHA_SORT =
		"DEALER_RPT_ALPHA_SORT";
	public final static String DEALER_RPT_ID_SORT =
		"DEALER_RPT_ID_SORT";

	public final static String SUBCON_RPT_NAME_SORT =
		"SUBCON_RPT_ALPHA_SORT";
	public final static String SUBCON_RPT_ID_SORT =
		"SUBCON_RPT_ID_SORT";
	// end defect 10250 

	// Miscellaneous Reg Codes
	public final static String PT72 = "72PT";
	public final static String PT144 = "144PT";
	public final static String OTPT = "OTPT";
	public final static String FDPT = "FDPT";
	public final static String PT30 = "30PT";
	public final static String TOWP = "TOWP";
	public final static String TAWPT = "TAWPT";
	
	// defect 10844 
	public final static String MODPT = "MODPT";
	// end defect 10844  

	// defect 10491
	// 24PT does not exist in RTS application; does exist in 
	//  RTS_TRANS_CDS 
	// public final static String PT24 = "24PT";

	//	new Duplicate Permit Receipt 
	public final static String PRMDUP = "PRMDUP";
	// Reprint Permit 
	public final static String RPRPRM = "RPRPRM";
	// end defect 10491

	// defect 8268
	//	public final static String BPM = "BPM";
	//	public final static String BTM = "BTM";
	//	public final static String RPNM = "RPNM";
	//	public final static String RTNM = "RTNM";
	// end defect 8268

	// defect 9831
	//	public final static String DLBPM = "DLBPM";
	//	public final static String DLBTM = "DLBTM";
	//	public final static String DLRPNM = "DLRPNM";
	//	public final static String DLRTNM = "DLRTNM";
	//
	//	public final static String RPBPM = "RPBPM";
	//	public final static String RPBTM = "RPBTM";
	//	public final static String RPRPNM = "RPRPNM";
	//	public final static String RPRTNM = "RPRTNM";
	// end defect 9831 

	// defect 10133 
	public final static String PDC = "PDC";
	public final static String TDC = "TDC";
	public final static String DELPDC = "DELPDC";
	public final static String DELTDC = "DELTDC";
	public final static String RPLPDC = "RPLPDC";
	public final static String RPLTDC = "RPLTDC";
	// end defect 10133 

	public final static String NRIPT = "NRIPT";
	public final static String NROPT = "NROPT";

	// Special Owner Codes
	public final static String SPECDL = "SPECDL";
	public final static String ADDRSP = "ADDRSP";
	public final static String DRVED = "DRVED";

	// defect 9085 
	// Special Plate Application - Manufacture 
	public final static String SPAPPL = "SPAPPL";
	// Special Plate Application - Customer Supplied 
	public final static String SPAPPC = "SPAPPC";
	// Special Plate Application - Issue Inventory  
	public final static String SPAPPI = "SPAPPI";
	// Special Plate Application - Owner Change 
	public final static String SPAPPO = "SPAPPO";
	// Special Plate Application - Reserve 
	public final static String SPAPPR = "SPAPPR";
	//	Special Plate Renew Plate Only
	public final static String SPRNW = "SPRNW";
	//	Special Plate Revise 
	public final static String SPREV = "SPREV";
	//	Special Plate Reserve
	public final static String SPRSRV = "SPRSRV";
	//	Special Plate Unacceptable
	public final static String SPUNAC = "SPUNAC";
	//	Special Plate Delete
	public final static String SPDEL = "SPDEL";
	//	Special Plate Delete
	public final static String SPRNR = "SPRNR";
	// Internet Special Plates Application 
	public final static String IAPPL = "IAPPL";
	// end defect 9085 

	// defect 10820 
	// special plates inquiry - temporary trans code 
	public final static String SPINQ = "SPINQ";
	// end defect 10820 

	// defect 10700 
	public final static String DPSPPT = "DPSPPT";
	// end defect 10700 

	// Reject Release
	public final static String DELTIP = "DELTIP";

	// CCOCCDO
	public final static String CCDO = "CCDO";

	// Accounting

	// Void
	public final static String VOID = "VOID";
	public final static String VOIDNC = "VOIDNC";
	public final static String INVVD = "INVVD";

	// Closeout
	public final static String CLSOUT = "CLSOUT";

	// Reprint Sticker 
	public final static String RPRSTK = "RPRSTK";

	//Inventory 
	public final static String INVOFC = "INVOFC";
	public final static String INVALL = "INVALL";
	public final static String INVDEL = "INVDEL";
	public final static String INVRCV = "INVRCV";

	public final static String PLTOPT = "PLTOPT";

	//Internet
	public final static String IRENEW = "IRENEW";
	public final static String IADDR = "IADDR";
	public final static String IRNR = "IRNR";

	// defect 6709
	public final static String INTERNET = "INTERNET";
	// end defect 6709

	// Credit Card Fee 
	public final static String CCPYMNT = "CRDFEE";

	// defect 9119
	public final static String APPLICATION = "APPLICATION";
	// end defect 9119

	// defect 9502
	public final static String G36VTN = "G36VTN";
	public final static String V21PLD = "V21PLD";
	public final static String V21VTN = "V21VTN";
	// end defect 9502

	// defect 9711
	public final static String VPAPPL = "VPAPPL";
	public final static String VPAPPR = "VPAPPR";
	// end defect 9711

	// defect 10401
	public final static String VPDEL = "VPDEL";
	public final static String VPPORT = "VPPORT";
	public final static String VPREDO = "VPREDO";
	public final static String VPREV = "VPREV";
	public final static String VPRSRV = "VPRSRV";
	public final static String VPRSTL = "VPRSTL";
	public final static String VPUNAC = "VPUNAC";
	// end defect 10401
	
	// defect 10610
	public final static String IADDRE = "IADDRE";
	// end defect 10610
	
	// defect 10708 
	public final static String WRENEW = "WRENEW";
	// end defect 10708 
	
	// defect 11052
	public final static String VDSC = "VDSC"; 
	public final static String VDSCN = "VDSCN";
	public final static String VDS = "VDS";
	public final static String VDSN = "VDSN";
	// end defect 11052
	
	// defect 11214 
	public final static String RENPDC = "RENPDC";
	public final static String REIPDC = "REIPDC";
	public final static String REITDC = "REITDC";
	// end defect 11214 
}
