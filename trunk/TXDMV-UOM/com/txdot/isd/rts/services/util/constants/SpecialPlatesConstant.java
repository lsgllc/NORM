package com.txdot.isd.rts.services.util.constants;

import java.util.Hashtable;
import java.util.Vector;

/*
 *
 * SpecialPlatesConstant
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/16/2007	Created
 *							defect 9085 Ver Special Plates
 * K Harrell	03/08/2007	Add'l constants
 * 							defect 9085 Ver Special Plates 
 * J Rue		03/20/2007	Add GET_VEH
 * 							defect 9086 Ver Special Plates
 * Jeff S.		03/29/2007	Add constant for Plate Sticker.
 * 							add PLT_STKR
 * 							defect 9145 Ver Special Plates
 * K Harrell	04/24/2007	add LP_PLATE
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/01/2007	add POS_EVENTS,   
 *							   BOTH_POS_AND_ITRNT_EVENTS 
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/17/2007	add PLTSETIMPRTNCECDxxxx
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/06/2007	add MAX_PLTNO_LENGTH 
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/10/2007	add PLP_ACCTITMCD_PREFIX
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/20/2007	add RADIO_OPERATOR_PLATE
 * 							defect 9384 Ver Special Plates 2
 * K Harrell	10/24/2007	add PER_DP_PRIVATE_BUS_PLT 
 * 							defect 9389 Ver Special Plates 2  
 * B Brown		06/18/2008  Add Vendor Plates constants.	
 * 							add VPAPPL_OFCISSUANCENO    
 * 								VPAPPL_SUBSTAID         
 *								VPAPPL_TRANSWSID        
 * 								VPAPPL_EMPID            
 * 								VPAPPL_BACK_OFFICE_TRANS
 *								VPAPPR_OFCISSUANCENO    
 *								VPAPPR_SUBSTAID         
 *								VPAPPR_TRANSWSID        
 *								VPAPPR_EMPID            
 *								VPAPPR_BACK_OFFICE_TRANS
 * 							defect 9711 Ver MyPlates_POS  
 * B Brown		07/02/2008  Genericize the Vendor Plates constants.
 * 							Remove VPAPPR and VPAPPL constants	
 * 							add VENDOR_OFCISSUANCENO    
 * 								VENDOR_SUBSTAID         
 *								VENDOR_TRANSWSID        
 * 								VENDOR_EMPID            
 * 								VENDOR_BACK_OFFICE_TRANS
 * 							defect 9711 Ver MyPlates_POS
 * K Harrell	02/18/2009	add SPCL_PLT_KEEP_ADDRESS, 
 * 							 SPCL_PLT_USE_OWNER_ADDRESS,
 * 							 SPCL_PLT_USE_RECIPIENT_ADDRESS,
 * 							 SPCL_PLT_ADDR_CHG_MSG_PREFIX, 
 * 							 SPCL_PLT_ADDR_CHG_MSG_SUFFIX  
 * 							defect 9893 Ver Defect_POS_D
 * K Harrell	12/28/2009 add FIRST_YEAR_SPECIALTY_PLATES,
 * 							 MAX_PLUS_CURRENT_YR,
 * 							 MAX_PLUS_CURRENT_YR_VENDOR
 * 							defect 10295 Ver Defect_POS_H 
 * T Pederson	01/05/2010 	Removed special plates fees constant
 *							delete SPCLPLT_FEES_NOT_INCLUDED
 *							defect 10303  Ver Defect_POS_H
 * K Harrell	04/12/2010	add PLP_REGPLTCD,RESRVREASN_INDINAME,
 * 							NON_VENDOR_PLT_TERM  
 * 							defect 10441 Ver POS_640
 * Ray Rowehl	04/12/2010	Add account code for MyPlates Restyle 
 * 							add ACCT_CD_RESTYLE
 * 							defect 10401 Ver 6.4.0
 * K Harrell	04/15/2010	add DEFAULT_SPCLPLT_VALIDITY_TERM,
 * 							  PLATE_VALIDITY_TERMS
 * 							defect 10458 Ver POS_640
 * Ray Rowehl	05/04/2010	Setup account code for Auctions.
 * 							add ACCT_CD_AUCTION
 * 							defect 10401 Ver 6.4.0 
 * K Harrell	10/28/2010	add new element to PLATE_VALIDITY_TERMS
 * 							defect 10644 Ver 6.6.0 
 * K Harrell	12/26/2010	add GET_DUPL_INSIG
 * 							defect 10700 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Constants for Special Plates 
 *
 * @version	6.7.0				12/26/2010
 * @author	Kathy Harrell
 * <br>Creation Date:			02/16/2007 12:42:00
 */
public class SpecialPlatesConstant
{
	public final static int NO_DATA_TO_BUSINESS =
		GeneralConstant.NO_DATA_TO_BUSINESS;

	// Function calls
	public final static int GET_VEH = 1;
	
	// defect 10700 
	public final static int GET_DUPL_INSIG = 2;
	// end defect 10700 
	
	public final static int GENERATE_SPCL_PLT_APPL_REPORT = 8;

	public final static int MAX_CP_APPL_MO = 2;
	public final static int CP_START_MO = 3;
	public final static int INTERNET_OFCISSUANCECD = 5;
	public final static int MAX_PLP_PLTNO_LENGTH = 6;
	public final static int MAX_PLTNO_LENGTH = 7;

	// Events Types 
	public final static String REGIS_TYPE_EVENTS = "R";
	public final static String ORDER_TYPE_EVENTS = "O";
	public final static String BOTH_ORDER_AND_REGIS_EVENTS = "B";

	// ISAALLOWDCD 
	public final static String POS_EVENTS = "P";
	public final static String BOTH_POS_AND_ITRNT_EVENTS = "B";

	// DUPLSALLOWDCD
	public final static int DUPLSALLOWDCD_NEVER = 0;
	public final static int DUPLSALLOWDCD_IF_NOT_SAME_YR = 1;
	public final static int DUPLSALLOWDCD_EVEN_IF_SAME_YR = 2;

	// PLTSETIMPRTNCECD 
	public final static int PLTSETIMPRTNCECD_NOT_IMPORTANT = 0;
	public final static int PLTSETIMPRTNCECD_FIRST_SET_ONLY = 1;
	public final static int PLTSETIMPRTNCECD_ADDL_SET_ONLY = 2;
	public final static int PLTSETIMPRTNCECD_EITHER_FIRST_OR_ADDL_SET =
		3;

	// IAPPL 
	public static final int IAPPL_OFCISSUANCENO = 291;
	public static final int IAPPL_SUBSTAID = 0;
	public static final int IAPPL_TRANSWSID = 999;
	public static final String IAPPL_EMPID = "IAPPL";
	public static final String IAPPL_BACK_OFFICE_TRANS =
		"IAPPL BackOffice";

	// defect 9711
	// represents VPAPPL and VPAPPR 
	public static final int VENDOR_OFCISSUANCENO = 294;
	public static final int VENDOR_SUBSTAID = 0;
	public static final int VENDOR_TRANSWSID = 999;
	public static final String VENDOR_EMPID = "VENDOR";
	public static final String VENDOR_BACK_OFFICE_TRANS =
		"VENDOR BackOffice";
	// end defect 9711

	// NeedsProgramCd, etc. 	
	public final static String ENTITLED_OWNER = "E";
	public final static String OWNER = "O";
	public final static String VEHICLE = "V";
	public final static String LP_PLATE = "L";

	// Symbols
	public final static String ISA_SYMBOL = "%";
	public final static String TX_SYMBOL = "*";

	// defect 10303
	//public final static String SPCLPLT_FEES_NOT_INCLUDED = "K";
	// end defect 10303

	// Types of Combination Plates 
	public static final String PLPCP = "PLPCP";
	public static final String GOTEX2CP = "GOTEX2CP";
	public static final String PLPGOTXC = "PLPGOTXC";

	// Types of PLP Dealer Plates 
	public static final String PLPDLR = "PLPDLR";
	public static final String PLPDLRMC = "PLPDLRMC";

	//	defect 9384 
	public static final String RADIO_OPERATOR_PLATE = "ROP";
	// end defect 9384 
	
	// defect 10441 
	public static final String PLP_REGPLTCD = "PLP"; 
	public static final String RESRVREASN_INDINAME = "RESRVREASNCD";
	public static final int NON_VENDOR_PLT_TERM = 1; 
	// end defect 10441 

	// defect 9389 
	public static final String PER_DP_PRIVATE_BUS_PLT = "PLPDPPBP";
	// end defect 9389 

	// Interpretations of Manufacture Status Codes
	public static final String MANUFACTURE = "MANUFACTURE";
	public static final String FROM_RESERVE = "RESERVED";
	public static final String CUSTOMER_SUPPLIED = "CUSTOMER SUPPLIED";
	public static final String PLATE_OWNER_CHANGE =
		"PLATE OWNER CHANGE";
	public static final String ISSUE_FROM_INVENTORY =
		"ISSUE FROM INVENTORY";
	public static final String UNACCEPTABLE = "UNACCEPTABLE";
	public static final String ASSIGNED = "ASSIGNED";

	public static final String MANUFACTURE_MFGSTATUSCD = "M";
	public static final String RESERVE_MFGSTATUSCD = "R";
	public static final String UNACCEPTABLE_MFGSTATUSCD = "N";
	public static final String ASSIGN_MFGSTATUSCD = "";

	public static final String TRANSFERABLE = "1";
	public static final String LIMITED_TRANSFERABILITY = "2";
	public static final String PLT_STKR = "US";

	public static final String PLP_ACCTITMCD_PREFIX = "SPL0090";

	// defect 9893 	
	public final static int SPCL_PLT_KEEP_ADDRESS = 0;
	public final static int SPCL_PLT_USE_OWNER_ADDRESS = 1;
	public final static int SPCL_PLT_USE_RECIPIENT_ADDRESS = 2;
	public static final String SPCL_PLT_ADDR_CHG_MSG_PREFIX =
		"The Address on the Special Plate"
			+ " will be changed to match the ";

	public static final Hashtable SPCL_PLT_ADDR_CHG_MSG_SUFFIX =
		new Hashtable();
	static {
		SPCL_PLT_ADDR_CHG_MSG_SUFFIX.put(
			new Integer(SPCL_PLT_USE_OWNER_ADDRESS),
			"Vehicle Owner Address.");

		SPCL_PLT_ADDR_CHG_MSG_SUFFIX.put(
			new Integer(SPCL_PLT_USE_RECIPIENT_ADDRESS),
			"Renewal Recipient Address.");
	}
	// end defect 9893 

	public static final Hashtable STATUSCDS = new Hashtable();

	static {
		STATUSCDS.put(MANUFACTURE, "M");
		STATUSCDS.put(FROM_RESERVE, "R");
		STATUSCDS.put(ASSIGNED, " ");
		STATUSCDS.put(UNACCEPTABLE, "N");
	}

	public static final Hashtable INTERPRET_STATUSCDS = new Hashtable();
	static {
		INTERPRET_STATUSCDS.put("R", FROM_RESERVE);
		INTERPRET_STATUSCDS.put("", ASSIGNED);
		INTERPRET_STATUSCDS.put(" ", ASSIGNED);
		INTERPRET_STATUSCDS.put("N", UNACCEPTABLE);
		INTERPRET_STATUSCDS.put("M", MANUFACTURE);
		INTERPRET_STATUSCDS.put("H", "HOLD");

	}
	
	// defect 10295 
	public static final int FIRST_YEAR_SPECIALTY_PLATES = 1965;
	public static final int MAX_PLUS_CURRENT_YR = 5; 
	public static final int MAX_PLUS_CURRENT_YR_VENDOR = 15;
	// end defect 10295
	
	// defect 10401
	/**
	 * The Account Code used for MyPlates when the transaction code is 
	 * VPRSTL.
	 */
	public static final String ACCT_CD_RESTYLE = "RESTYLE";
	
	/**
	 * The Account Code used for MyPlates when doing Auctions.
	 */
	public static final String ACCT_CD_AUCTION = "SPLAUCTN";
	// end defect 10401
	
	// defect 10435 
	public static int DEFAULT_SPCLPLT_VALIDITY_TERM = 1; 
	public static Vector PLATE_VALIDITY_TERMS = new Vector();
	 
	static
	{ 
		PLATE_VALIDITY_TERMS.add(0,new Integer(1)); 
		PLATE_VALIDITY_TERMS.add(1,new Integer(5)); 
		PLATE_VALIDITY_TERMS.add(2,new Integer(10));
		// defect 10644  
		PLATE_VALIDITY_TERMS.add(3,new Integer(25));
		// end defect 10644 
	}
	// end defect 10435
	
}
