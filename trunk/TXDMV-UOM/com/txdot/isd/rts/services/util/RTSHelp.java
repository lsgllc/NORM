package com.txdot.isd.rts.services.util;

import java.io.*;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * 
 * RTSHelp.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Richard      01/09/2002	Class creation
 * 	Hicks
 * MAbs			05/17/2002	Changed INV029
 * Becky 		11/27/2002	Added multiple bookmarks
 *	Arredondo
 * Becky 		12/10/2002	Modified bookmarks
 *	Arredondo
 * B Arredondo  12/16/2002	Fixing Defect# 5147. Made changes 
 *				12/19/2002	for the user help guide so had 
 *				12/20/2002	to make changes in actionPerformed().
 * Jeff S.		08/28/2003	Getting the DefaultBrowserPath from
 *							the static cfg file incase there is a 
 *							change. displayHelp()  
 *							defect 5972 Ver 5.1.6
 * Ray Rowehl	04/20/2004	add new method to allow logout to
 *							destroy the current browser thread.
 *							Also add log write for when help is called.
 *							add releaseBrowser()
 *							modify displayHelp()
 *							defect 7022 Ver 5.1.6
 * J Rue		03/23/2005	Change class number from 008 to 009.
 * 							Bring class up to RTS standards
 * 							class var DTA008 to DTA009
 * 							defect 6963,7898 Ver 5.2.3
 * J Rue		03/30/2005	Cleanup code
 * 							modify main()
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	04/04/2005	rename INV020b to INV019
 * 							add INV019, INV020
 * 							delete INV020a, INV020b
 * 							modify main()
 * 							defect 6964 Ver 5.2.3
 * Ray Rowehl	04/04/2005	rename INV015 to INV028 to match frame
 * 							defect 6965 Ver 5.2.3
 * Ray Rowehl	04/05/2005	Remove INV008
 * 							delete INV008, INV008A
 * 							modify main()
 * 							defect 7954 Ver 5.2.3
 * K Harrell	04/28/2005	delete INV014B
 * 							add INV003 
 * 							defect 6966 Ver 5.2.3 
 * B Hargrove	05/12/2005	Update definitions for User Guide Updates
 * 							(fix merged in from VAJ)
 *  						add/modify/remove class variables 
 * 							modify main()
 * 							defect 8177 Ver 5.2.2 Fix 5
 * 							Add definition for RSPS RSP001
 *  						add class variable 
 * 							modify main()
 * 							defect 7570 Ver 5.2.2 Fix 5
 * Ray Rowehl	05/16/2005	Update to allow the url to be formed from
 * 							properties.  This will allow us to use
 * 							either file or http to connect.
 * 							merge from 5.2.2 fix 5
 * 							delete URL_STRING1
 * 							modify displayHelp()
 * 							defect 8180 Ver 5.2.2 Fix 5
 * B Hargrove	05/31/2005	Update definitions for INV003
 * 							(fix merged in from VAJ where this sceen
 * 							 was named INV014 - defect 6966)
 *  						add/remove class variables 
 * 							modify main()
 * 							defect 8177 Ver 5.2.2 Fix 5
 * B Hargrove	06/09/2005	Add missing test cases to main(). These were
 * 							not part of original defect changes, just
 * 							cases that were somehow left out earlier.
 * 							modify main()
 * 							defect 8177 Ver 5.2.2 Fix 5
 * B Hargrove	06/17/2005	Further clean-up on bookmark names. Trying
 * 							to get everything in synch with html doc.
 * 							modify class variables
 * 							defect 8177 Ver 5.2.2 Fix 5
 * T Pederson	09/08/2006	add TTL045
 * 							modify main()
 * 							defect 8926 Ver 5.2.5
 * Jeff S.		10/12/2006	Add the ability to launch IE passing a 
 * 							given document. 
 * 							Task 42 - Launch Exempt agency list
 * 							add displayPage()
 * 							defect 8900 Ver Exempts
 * J Ralph		10/26/2006	add RPR005
 * 							modify main()
 * 							defect 8900 Ver Exempts
 * K Harrell	02/27/2007	add SPL001, SPL002X, SPL003
 * 							modify main()
 * 							defect 9085 Ver Special Plates
 * M Reyes		02/28/2007	Changes for Special Plates
 * 							add SEC018
 * 							modify main()
 * 							defect 9124 Ver Special Plates	 
 * B Hargrove	04/01/2008	Change all occurrences of 'progress' to   
 * 							'process' 
 * 							(ie: TTL042 = "ch06_deletetitleinprocess").
 * 							modify constant TTL042 
 * 							defect 8786 Ver Defect POS A
 * B Hargrove	04/06/2009	Update for User Guide bookmark changes.
 * 							Modify code in main() to test.
 *							add MRG020
 *							modify SEC018
 *							delete MRG002  
 * 							modify main() 
 * 							defect 10004 Ver Defect_POS_E 
 * B Hargrove	08/07/2009	Add check for ftp (a type of 'launch'
 * 							from Help for MVD Dealer List). 
 *							add FTP
 *							modify displayPage()  
 * 							defect 10151 Ver Defect_POS_F 
 * Ray Rowehl	09/17/2009	Found that "ftp" does not work well when 
 * 							RTS POS is launched as a service (prod mode).
 * 							Switch to using "http".
 * 							add REMOTE_HTTP, REMOTE_HTTP_LENGTH 
 * 							delete FTP
 * 							modify displayPage() 
 * 							defect 10151 Ver Defect_POS_F 
 * Ray Rowehl	06/12/2012	Do not use releaseBrowser() any more.
 * 							Since the Desktop is launched from a user 
 * 							session, we don't have a concern with
 * 							closing old browser sessions.
 * 							In fact, closing sessions interferes with
 * 							how a user might use the browser.
 * 							deprecate getBrowser(), releaseBrowser()
 * 							modify displayHelp(), displayPage()
 * 							defect 11374 Ver POS_700
 * ---------------------------------------------------------------------
 */

/**
 * RTSHelp is a utility class that launches a browser for viewing
 * the online users guide.  It is called whenever a Help button is
 * selected from a screen.
 *
 * @version POS_700			06/12/2012
 * @author	Richard Hicks
 * @since					01/09/2002 10:01:45
 */

public class RTSHelp
{
	final public static String ACC001 = "ch11_additionalcollections";

	final public static String ACC004A = "ch11_hotcheckcredit";
	final public static String ACC004B = "ch11_hotcheckredeemed";

	final public static String ACC004C = "ch11_deducthotcheckcredit";
	final public static String ACC005 = "ch11_itemsseized";
	final public static String ACC006 = "ch11_refund";
	final public static String ACC018 = "ch11_fundsremitackldg_full";
	final public static String ACC020 = "ch11_fundspayment";
	final public static String ACC022 = "ch11_paymentdetail";
	final public static String ACC023 = "ch11_paymentsummary";
	final public static String ACC024 = "ch11_fundsdetailspayments";
	/** 
	 * Handle to the browser process so that multiple
	 *instances are not launcehed.
	 */
	static private Process caBrowser = null;
	final public static String CTL001 = "ch03_commnscrn_confirmaction";
	final public static String CTL004 =
		"ch03_commnscrn_supervisoroverride";

	final public static String CTL005 =
		"ch03_commnscrn_recordfoundcanceldplate";
	// defect 8177
	//final public static String CTL006 = "ch14_documentnumberconfirm";
	final public static String CTL006A = "ch14_docnoconfirm_renewal";
	final public static String CTL006B = "ch14_docnoconfirm_title";
	final public static String CTL006C = "ch14_docnoconfirm_repl";
	final public static String CTL006D = "ch14_docnoconfirm_exchange";
	final public static String CTL006E = "ch14_docnoconfirm_volpawt";
	final public static String CTL006F = "ch14_docnoconfirm_apprhpawt";
	final public static String CTL006G = "ch14_docnoconfirm_regcorrect";
	final public static String CTL006H = "ch14_docnoconfirm_tempadlwt";
	// end defect 8177
	final public static String CTL009 = "ch08_printdestination";
	final public static String CTL010 = "ch06_vtrauthorization";
	final public static String CUS001 = "ch08_setasideselecttrans";
	// defect 8177
	final public static String CUS003A =
		"ch08_selectreceipt_reprintall";

	final public static String CUS003B =
		"ch08_selectreceipt_reprintlast";
	final public static String CUS003C =
		"ch08_selectreceipt_reprintnotrans";
	// end defect 8177
	public final static String DEALER_REPORT = "ch12_dealerreport";
	public final static String DELETE_INVENTORY_HISTORY_REPORT =
		"ch09_deleteinventoryhistoryreport";

	final public static String DTA001A =
		"ch06_dealer_entrypreferences_disk";
	final public static String DTA001B =
		"ch06_dealer_entrypreferences_manual";
	final public static String DTA003 = "ch06_copyinstructions";
	final public static String DTA004 = "ch06_copyconfirmation";
	final public static String DTA005 = "ch99_nohelpavailable";
	// copy failure
	final public static String DTA007 = "ch06_dealer_diskcontents";
	//final public static String DTA008A="ch06_dealertitletransaction";
	final public static String DTA008 = "ch06_dealertitletransaction";
	// defect 6963
	//	Changed the VC/Frame number from 008 to 009
	//	and 008A to DTA008, DTA008B to DTA00
	//final public static String DTA008 = "ch06_manualentry_dta";
	final public static String DTA009 = "ch06_manualentry_dta";
	public final static String EMPLOYEE_SECURITY_REPORT =
		"ch12_employeesecurityreport";
	final public static String ERR001 = "ch16_error_messages";
	// RTS Help Choices
	public final static String EVENT_SECURITY_REPORT =
		"ch12_eventsecurityreport";
	public final static String FEES_REPORT = "ch15_fundsbalancereports";
	// DTA008b is not used
	//final public static String DTA008B = 
	//	"ch06_dealertitletrans_manual";
	// end defect 6963
	final public static String FUN001 = "ch15_cashdrawerselection";
	final public static String FUN002 = "ch15_closeoutconfirmation";
	final public static String FUN003 = "ch15_reportingranges";
	final public static String FUN006 = "ch15_fundsreportselection";
	final public static String FUN007 = "ch15_reportselection";
	final public static String FUN008 = "ch15_resetcloseoutindicator";

	final public static String FUN010 = "ch15_substationsummary";

	final public static String FUN011 = "ch15_employeeselection";
	final public static String FUN012 = "ch15_reportdateselection";

	final public static String FUN015 = "ch15_closeoutstatus";
	public final static String INET_ADDRESS_CHANGE_REPORT =
		"ch18_addresschangereport";
	public final static String INET_RECONCILIATION_REPORT =
		"ch18_reconciliationreport";
	public final static String INET_VENDOR_PAYMENT_REPORT =
		"ch18_vendorpaymentreport";
	final public static String INQ004 =
		"ch03_commnscrn_multiplerecords";
	//	final public static String INQ007A = 
	//	"ch04_printrecordcustomername";
	final public static String INQ007 = "ch11_customernamerefund";
	final public static String INQ008 = "ch04_printrecordcustomername";
	// defect 8177
	final public static String INV001 = "ch11_inv_itemnoinput_regcol";
	final public static String INV001A =
		"ch03_commnscrn_inventoryitemnumber";
	final public static String INV002 = "ch09_inventory_modifyinvoice";
	// defect 6966
	// From INV014 
	//final public static String INV003 =
	//	"ch05_itemnumbernotfound_subcon";
	final public static String INV003A =
		"ch05_itemnumbernotfound_subcondisk";
	final public static String INV003B =
		"ch05_itemnumbernotfound_subconmanual";
	// end defect 6966	
	final public static String INV004 = "ch09_inventory_receiveinvoice";
	final public static String INV005 =
		"ch11_inv_overrideinvitem_regcol";
	final public static String INV005A = "ch05_overrideinventoryitem";
	final public static String INV006 =
		"ch09_inventory_inventorydelete";
	final public static String INV007 = "ch11_inv_regitems_regcol";
	final public static String INV007A =
		"ch03_commnscrn_inventoryregitems";
	final public static String INV009 =
		"ch09_inventory_allocatefromlocation";
	final public static String INV010 =
		"ch09_inventory_allocatetolocation";
	final public static String INV011 = "ch99_nohelpavailable";
	final public static String INV012 =
		"ch09_inventoryadditemtoinvoice";
	final public static String INV013 = "ch09_inventory_allocateitem";
	final public static String INV014A =
		"ch09_inventory_allocationsummary";
	final public static String INV015 =
		"ch09_inventoryhistorycountyselection";
	// end defect 6965
	final public static String INV016 =
		"ch09_inventory_inventoryprofile";
	final public static String INV017 = "ch09_inventory_itemsonhold";
	final public static String INV018 =
		"ch09_inventory_placeitemsonhold";
	// defect 6964
	final public static String INV019 =
		"ch09_inventoryactionreportdateselection";
	final public static String INV020 =
		"ch09_inventoryinquiryselection";
	// end defect 6964
	final public static String INV021 =
		"ch09_inventory_inquirycriteria";
	final public static String INV022 =
		"ch09_inventory_inquiryitemtypes";
	final public static String INV023 =
		"ch09_inventory_inquirybyspecificitemnmbr";
	final public static String INV024 =
		"ch09_inventoryhistoryitemselection";
	final public static String INV025 = "ch99_nohelpavailable";
	//  inventory item range delete
	final public static String INV026 = "ch09_inventorydeletionsummary";
	final public static String INV027 =
		"ch09_inventory_inquiryselection";
	// defect 6965
	final public static String INV028 =
		"ch09_inventory_modifyitemoninvoice";
	// defect 6965 
	// What is this?
	final public static String INV028A = "ch99_nohelpavailable";
	// end defect 6965
	// inventory adjustment reason
	//final public static String INV029A =
	//	"ch05_itemnumbernotfound_subcon";
	final public static String INV029B =
		"ch05_itemnumbernotfound_renew";
	// end defect 8177
	final public static String INV029C =
		"ch05_itemnumbernotfound_exchg";
	final public static String INV029D = "ch05_itemnumbernotfound_repl";
	final public static String INV029E =
		"ch06_itemnumbernotfound_title";
	final public static String INV029F =
		"ch06_itemnumbernotfound_dtadisk";
	final public static String INV029G = "ch07_itemnumbernotfound_72hr";
	final public static String INV029H =
		"ch07_itemnumbernotfound_144hr";
	final public static String INV029I =
		"ch07_itemnumbernotfound_1trip";
	final public static String INV029J =
		"ch07_itemnumbernotfound_30day";
	final public static String INV029K =
		"ch07_itemnumbernotfound_nonres";
	final public static String INV029L =
		"ch07_itemnumbernotfound_towtrk";
	final public static String INV029M =
		"ch14_itemnumbernotfound_mfunavlbltitle";
	final public static String INV029N =
		"ch14_itemnumbernotfound_mfunavlblrepl";
	final public static String INV029O =
		"ch14_itemnumbernotfound_mfunavlblrenew";
	final public static String INV029P =
		"ch14_itemnumbernotfound_mfunavlblexchg";
	final public static String INV029Q = "ch99_nohelpavailable";
	final public static String INV030 =
		"ch09_inventoryviewinventoryprofile";
	public final static String INVENTORY_ACTION_REPORT =
		"ch09_inventoryactionreport";
	public final static String INVENTORY_ALLOCATE_REPORT =
		"ch09_inventoryallocatereport";
	public final static String INVENTORY_DELETE_REPORT =
		"ch09_inventorydeletereport";
	public final static String INVENTORY_DETAIL_REPORT =
		"ch15_fundsbalancereports";
	public final static String INVENTORY_HOLD_REPORT =
		"ch09_inventoryholdreport";
	public final static String INVENTORY_INQUIRY_REPORT =
		"ch09_inventoryinquiryreport";
	public final static String INVENTORY_RECEIVE_REPORT =
		"ch09_inventoryreceivereport";
	public final static String INVENTORY_SUMMARY_REPORT =
		"ch15_fundsbalancereports";
	//    final public static String KEY001 = 
	//	"ch03_commnscrn_inquirykeyselection";
	final public static String KEY001A = "ch04_inquirykeyselection_inq";
	final public static String KEY001B =
		"ch05_inquirykeyselection_renew";
	final public static String KEY001C =
		"ch05_inquirykeyselection_dupl";
	final public static String KEY001D =
		"ch05_inquirykeyselection_exchg";
	final public static String KEY001E =
		"ch05_inquirykeyselection_repl";
	final public static String KEY001F =
		"ch05_inquirykeyselection_volpawt";
	final public static String KEY001G =
		"ch05_inquirykeyselection_apprhpawt";
	final public static String KEY001H =
		"ch05_inquirykeyselection_regcor";
	final public static String KEY001I =
		"ch05_inquirykeyselection_addchgprntrenu";
	final public static String KEY001J =
		"ch06_inquirykeyselection_statchg";
	final public static String KEY001K = "ch06_inquirykeyselection_cco";
	final public static String KEY001L =
		"ch06_inquirykeyselection_statchgro";
	final public static String KEY001M =
		"ch07_inquirykeyselection_tempadlwt";
	final public static String KEY001N =
		"ch11_inquirykeyselection_refund";
	final public static String KEY001O =
		"ch11_inquirykeyselection_hotckcredit";
	final public static String KEY001P =
		"ch11_inquirykeyselection_hotckredmd";
	final public static String KEY001Q =
		"ch11_inquirykeyselection_deducthotckcrd";
	final public static String KEY001R =
		"ch11_inquirykeyselection_itemseized";
	final public static String KEY001S =
		"ch14_inquirykeyselection_mfunavlblrenew";
	final public static String KEY001T =
		"ch14_inquirykeyselection_mfunavlblrepl";

	final public static String KEY001U =
		"ch14_inquirykeyselection_mfunavlblexchg";
	final public static String KEY001V =
		"ch14_inqkeyselection_mfunavlblvolpawt";
	final public static String KEY001W =
		"ch14_inqkeyselection_mfunavlblapprhpawt";
	final public static String KEY001X =
		"ch14_inqkeyselection_mfunavlblregcor";
	final public static String KEY001Y =
		"ch14_inqkeyselection_mfunavlbltempadlwt";
	final public static String KEY003 = "ch10_salestaxinquiry";
	final public static String KEY006A = "ch06_vinkeyselection_title";
	final public static String KEY006B =
		"ch06_vinkeyselection_dtamanual";
	final public static String KEY006C =
		"ch06_vinkeyselection_dtadiskl";
	final public static String KEY006D = "ch06_vinkeyselection_coa";
	final public static String KEY006E = "ch06_vinkeyselection_salvg";
	final public static String KEY006F =
		"ch14_vinkeyselection_mfunavlbltitle";
	final public static String KEY007 = "ch06_documentnumberinquiry";
	final public static String KEY008 = "ch06_platekeyselection";
	final public static String KEY008A =
		"ch14_platekeyselection_mfunavlbltitle";

	public final static String LIENHOLDER_REPORT =
		"ch12_lienholderreport";
	final public static String MRG001 = "ch07_30daypermittypes";
	// defect 10004
	//final public static String MRG002 = "ch07_disabledparkingcard";
	// end defect 10004
	final public static String MRG004 = "ch07_towtruck";
	final public static String MRG005 = "ch07_timedpermittypes";
	final public static String MRG006_144_HOUR = "ch07_144_hour_permit";
	final public static String MRG006_30_DAY = "ch07_30_day_permit";
	final public static String MRG006_72_HOUR = "ch07_72_hour_permit";
	final public static String MRG006_FACTORY = "ch07_factory_delivery";
	final public static String MRG006_ONE_TRIP = "ch07_one_trip_permit";
	final public static String MRG008 = "ch07_onetripdestination";
	// defect 8177
	//final public static String MRG010 = "ch07_tempaddlweight";
	final public static String MRG010A = "ch07_tempaddlweight";
	final public static String MRG010B = "ch14_tempaddlwt_mfunavlbl";
	//final public static String MRG011 = "ch07_tempaddlweightoptions";
	final public static String MRG011A = "ch07_tempaddlweightoptions";
	final public static String MRG011B =
		"ch14_tempadlwtoptions_mfunavlbl";
	// end defect 8177
	final public static String MRG013 = "ch07_nonresidentagriculture";
	// defect 10004
	final public static String MRG020 = "ch07_disabledplacards";
	// end defect 10004
	public final static String PAYMENT_REPORT =
		"ch15_fundsbalancereports";
	final public static String PENDING_TRANSACTIONS =
		"ch03_pendingtransactions";
	// defect 8177
	final public static String PMT001A = "ch11_payment_regcol";
	// end defect 8177
	final public static String PMT001B = "ch03_commnscrn_payment";
	final public static String PMT004 = "ch03_commnscrn_feesdue";
	final public static String PMT004A = "ch04_feesdue_inq";
	final public static String PMT004aa =
		"ch14_feesdue_mfunavlblregcor";
	final public static String PMT004B = "ch05_feesdue_renew";
	final public static String PMT004bb =
		"ch14_feesdue_mfunavlbltempaddlwt";
	final public static String PMT004C = "ch05_feesdue_duprec";
	final public static String PMT004cc = "ch06_feesdue_dtamanual";
	final public static String PMT004D = "ch05_feesdue_exchg";
	final public static String PMT004dd = "ch06_feesdue_dtadisk";
	final public static String PMT004E = "ch05_feesdue_repl";
	final public static String PMT004ee = "ch11_feesdue_regcol";
	final public static String PMT004F = "ch05_feesdue_volpawt";
	final public static String PMT004ff =
		"ch11_feesdue_additionalcollections";
	final public static String PMT004G = "ch05_feesdue_apprhpawt";
	final public static String PMT004gg = "ch11_feesdue_hotckredmd";

	final public static String PMT004I = "ch06_feesdue_title";

	final public static String PMT004J = "ch06_feesdue_adlslstx";
	final public static String PMT004K = "ch06_feesdue_cco";
	final public static String PMT004L = "ch07_feesdue_72hr";
	final public static String PMT004M = "ch07_feesdue_144hr";
	final public static String PMT004N = "ch07_feesdue_1trip";
	final public static String PMT004O = "ch07_feesdue_factorydel";
	final public static String PMT004P = "ch07_feesdue_30day";
	final public static String PMT004Q = "ch07_feesdue_pdctdc";
	final public static String PMT004R = "ch07_feesdue_nonresag";
	final public static String PMT004S = "ch07_feesdue_tempadlwt";
	final public static String PMT004T = "ch07_feesdue_towtrk";
	final public static String PMT004U = "ch14_feesdue_mfunavlblrenew";
	final public static String PMT004V = "ch14_feesdue_mfunavlbltitle";
	final public static String PMT004W = "ch14_feesdue_mfunavlblrepl";
	final public static String PMT004X = "ch14_feesdue_mfunavlblexchg";
	final public static String PMT004Y =
		"ch14_feesdue_mfunavlblvolpawt";
	final public static String PMT004Z =
		"ch14_feesdue_mfunavlblapprhpawt";
	final public static String PUB002 = "ch12_publishingupdate";
	public final static String PUBLISHING_REPORT =
		"ch12_publishingreport";
	public final static String RECEIVE_INVENTORY_HISTORY_REPORT =
		"ch09_receiveinventoryhistoryreport";
	final public static String REG001 = "ch05_stickerselection";
	// defect 8177
	final public static String REG001A =
		"ch14_stkrselection_mfunavlblrepl";
	// end defect 8177
	final public static String REG002 = "ch99_nohelpavailable";
	// special owner
	final public static String REG003A = "ch05_regrenew";
	final public static String REG003B = "ch05_regdupl";
	final public static String REG003C = "ch05_regexch";
	final public static String REG003D = "ch05_regrepl";
	final public static String REG003E = "ch05_regpawt";
	final public static String REG003F = "ch05_regcorreg";
	final public static String REG003G = "ch05_regaddr";
	final public static String REG003H = "ch04_regvehinq";
	final public static String REG003I = "ch11_reghotck";
	final public static String REG003J = "ch11_reghotded";
	final public static String REG003K = "ch11_regckredm";
	final public static String REG003L = "ch11_regckitm";
	final public static String REG003M = "ch11_regrefund";
	final public static String REG003N =
		"ch14_registration_mfunavlblrenew";
	final public static String REG003O =
		"ch14_registration_mfunavlblrepl";
	final public static String REG003P =
		"ch14_registration_mfunavlblexchg";
	final public static String REG003Q =
		"ch14_registration_mfunavlblvolpawt";
	final public static String REG003R =
		"ch14_registration_mfunavlblapprhpawt";
	final public static String REG003S = "ch05_regapprhpawt";
	final public static String REG003T =
		"ch14_registration_mfunavlblregcor";
	final public static String REG004 = "ch06_registrationrefund";
	final public static String REG005 = "ch06_cancelregistration";
	final public static String REG006A = "ch05_subcontractorentry_disk";
	final public static String REG006B =
		"ch05_subcontractorentry_manual";
	final public static String REG007A =
		"ch05_subcontractorrenewal_disk";
	final public static String REG007B =
		"ch05_subcontractorrenewal_manual";
	final public static String REG008 = "ch04_classplatestickertype";

	final public static String REG008A =
		"ch04_clspltstkrtype_pltoptions";
	final public static String REG008B = "ch05_clspltstkrtype_renewal";
	final public static String REG008C = "ch06_clspltstkrtype_title";
	final public static String REG008D =
		"ch14_clspltstkrtype_mfunavlblrenewal";
	final public static String REG008E =
		"ch14_clspltstkrtype_mfunavlbltitle";
	final public static String REG008F =
		"ch14_clspltstkrtype_mfunavlblrepl";
	final public static String REG008G =
		"ch14_clspltstkrtype_mfunavlblexchg";
	final public static String REG008H =
		"ch14_clspltstkrtype_mfunavlblvolpawt";
	final public static String REG008I =
		"ch14_clspltstkrtype_mfunavlblapprpawt";
	final public static String REG008J =
		"ch14_clspltstkrtype_mfunavlblregcor";
	final public static String REG008K =
		"ch14_clspltstkrtype_mfunavlblexchgsplplt";
	final public static String REG008L =
		"ch14_clspltstkrtype_mfunavlbltempaddwt";
	final public static String REG008M =
		"ch06_clspltstkrtype_dtamanual";
	final public static String REG008N = "ch06_clspltstkrtype_dtadisk";
	final public static String REG008O =
		"ch05_clspltstkrtype_apprhpawt";
	final public static String REG008P = "ch05_clspltstkrtype_exchg";
	final public static String REG009B =
		"ch05_modifysubcontractorrenewal";
	final public static String REG010 = "ch05_vehicleweight";
	final public static String REG010A =
		"ch14_vehiclewt_mfunavlblvolpawt";
	final public static String REG010B =
		"ch14_vehiclewt_mfunavlblapprhpawt";
	final public static String REG014A = "ch14_mfrecordnotavlb_renewal";
	final public static String REG014B = "ch14_mfrecordnotavlb_title";
	final public static String REG014C = "ch14_mfrecordnotavlb_repl";
	final public static String REG014D =
		"ch14_mfrecordnotavlb_exchange";
	final public static String REG014E = "ch14_mfrecordnotavlb_volpawt";
	final public static String REG014F =
		"ch14_mfrecordnotavlb_apprhpawt";
	final public static String REG014G =
		"ch14_mfrecordnotavlb_regcorrect";
	final public static String REG014H =
		"ch14_mfrecordnotavlb_tempadlwt";
	final public static String REG016 = "ch05_replacementchoices";
	final public static String REG016A =
		"ch14_replchoices_mfunavlblrepl";
	final public static String REG024 =
		"ch05_modifyregistrationchoices";
	final public static String REG024A =
		"ch14_modregchoices_mfunavlblvolpawt";
	final public static String REG024B =
		"ch14_modregchoices_mfunavlblapprhpawt";
	final public static String REG024C =
		"ch14_modregchoices_mfunavlblregcor";
	final public static String REG025A = "ch05_registrationcorrection";
	final public static String REG025B =
		"ch14_regcorrection_mfunavlbltempadlwt";
	final public static String REG029A =
		"ch05_designatenewexpirationmonth";
	final public static String REG029B = "ch05_specialplateoptions";
	final public static String REG029C =
		"ch06_specialplateoptions_title";
	final public static String REG029D = "ch06_enterregismoyr_title";
	final public static String REG029E =
		"ch14_enterregexpmoyr_mfunavlblrenu";
	final public static String REG029F =
		"ch14_enterregexpmoyr_mfunavlbltitle";
	final public static String REG033A =
		"ch05_owneraddressduplicaterec";
	final public static String REG033B = "ch05_owneraddressexchange";
	final public static String REG033C = "ch05_owneraddressreplacement";
	final public static String REG033D = "ch05_owneraddressvolpawt";
	final public static String REG033E = "ch05_owneraddressapprhpawt";
	final public static String REG033F = "ch05_owneraddressregcor";
	final public static String REG033G = "ch05_owneraddressaddchange";
	final public static String REG033H =
		"ch14_owneraddress_mfunavlblrenu";
	final public static String REG033I =
		"ch14_owneraddress_mfunavlblrepl";
	final public static String REG033J =
		"ch14_owneraddress_mfunavlblexchg";
	final public static String REG033K =
		"ch14_owneraddress_mfunavlblvolpawt";

	final public static String REG033L =
		"ch14_owneraddress_mfunavlblapprhpawt";

	final public static String REG033M =
		"ch14_owneraddress_mfunavlblregcor";
	final public static String REG033N = "ch05_owneraddressrenew";
	final public static String REG039A =
		"ch05_registrationadditionalinfo";
	final public static String REG039B =
		"ch05_registrationadditionalinfoexchange";
	final public static String REG039C =
		"ch05_registrationadditionalinforepl";
	final public static String REG039D =
		"ch05_registrationadditionalinfovolpawt";
	final public static String REG039E =
		"ch05_registrationadditionalinfoapprhpawt";
	final public static String REG039F =
		"ch05_registrationadditionalinforegcor";
	final public static String REG039G =
		"ch14_regadlinfo_mfunavlblrenewal";
	final public static String REG039H =
		"ch14_regadlinfo_mfunavlblrepl";

	final public static String REG039I =
		"ch14_regadlinfo_mfunavlblexchg";

	final public static String REG039J =
		"ch14_regadlinfo_mfunavlblvolpawt";
	final public static String REG039K =
		"ch14_regadlinfo_mfunavlblapprhpawt";
	final public static String REG039L =
		"ch14_regadlinfo_mfunavlblregcor";
	final public static String REG039M =
		"ch05_registrationadditionalinfo_renew";
	final public static String REG050A =
		"ch05_entrypreferences_subcondisk";
	final public static String REG050B =
		"ch05_entrypreferences_subconmanual";

	final public static String REG051 = "ch05_copyinstructions";
	final public static String REG053 = "ch05_copyfailure";
	//  County Processsing
	final public static String REG101 = "ch18_vehiclerecordsearch";
	final public static String REG102 = "ch18_vehiclesearchresults";
	final public static String REG103 = "ch18_vhlinfoandprocessing";

	final public static String REG104 =
		"ch18_vehicleinsuranceinformation";

	final public static String REG105 =
		"ch18_recipientaddressinformation";
	final public static String REG106 = "ch18_reportselection";
	// defect 10151
	//private final static String FTP = "ftp";
	private static final String REMOTE_HTTP = "remotehttp";
	private static final int REMOTE_HTTP_LENGTH = REMOTE_HTTP.length();
	// end defect 10151
	final public static String RPR000 = "ch10_previewreport";
	final public static String RPR001 = "ch08_toreprintreceipt";
	final public static String RPR002 = "ch10_reprintreports";
	final public static String RPR003 = "ch10_titlepackagereport";
	// defect 8900
	final public static String RPR005 = "ch10_exemptauditreport";
	// end defect 8900
	// defect 7570
	final public static String RSP001 = "ch12_rspsstatusupdate";
	// end defect 7570
	public final static String SALES_TAX_ALLOCATION_REPORT =
		"ch10_salestaxallocationreport";
	final public static String SEC001 = "ch02_logonscreen";
	final public static String SEC002 = "ch02_passwordchange";
	final public static String SEC005 = "ch12_employeeaccessrights";
	final public static String SEC006 =
		"ch12_securityaccessrts_registrationonly";
	final public static String SEC007 =
		"ch12_securityaccessrts_titleregistration";

	final public static String SEC008 =
		"ch12_securityaccessrts_statuschange";
	final public static String SEC009 =
		"ch12_securityaccessrts_inquiry";
	final public static String SEC010 =
		"ch12_securityaccessrts_miscregistration";
	final public static String SEC011 =
		"ch12_securityaccessrts_miscellaneous";
	final public static String SEC012 =
		"ch12_securityaccessrts_reports";
	final public static String SEC013 =
		"ch12_securityaccessrts_localoptions";
	final public static String SEC014 =
		"ch12_securityaccessrts_accounting";
	final public static String SEC015 =
		"ch12_securityaccessrts_inventory";
	final public static String SEC016 = "ch12_securityaccessrts_funds";
	final public static String SEC017 = "ch12_supervisoroverrideupdate";
	// defect 9124
	// defect 10004
	//final public static String SEC018 = "ch17_spclpltapplication";
	final public static String SEC018 = 
		"ch12_securityaccessrts_specialplates";
	// end defect 10004
	// end defect 9124 
	public final static String SECURITY_CHANGE_REPORT =
		"ch12_securitychangereport";
	// Special Plates
	// defect 9085
	final public static String SPL001 = "ch17_spclpltapplication";
	// SPL002 - Special Plate Events 
	final public static String SPL002A = "ch17_spclpltinfoappl";
	final public static String SPL002B = "ch17_spclpltinfosprenew";
	final public static String SPL002C = "ch17_spclpltinforevise";
	final public static String SPL002D = "ch17_spclpltinforeserve";
	final public static String SPL002E = "ch17_spclpltinfounaccept";
	final public static String SPL002F = "ch17_spclpltinfodelete";
	// SPL002 - Reg Events
	final public static String SPL002G = "ch17_spclpltinforenew";
	final public static String SPL002H = "ch17_spclpltinfoexch";
	// SPL002 - Title
	final public static String SPL002I = "ch17_spclpltinfotitle";
	// SPL002 - MF Down 
	final public static String SPL002J = "ch17_spclpltinfomfdown";
	// SPL003 
	final public static String SPL003 = "ch17_spclpltreports";
	// end defect 9085	 
	public final static String SUBCONTRACTOR_RENEWAL_REPORT =
		"ch05_subcontractorrenewalreport";
	public final static String SUBCONTRACTOR_REPORT =
		"ch12_subcontractorreport";
	public final static String TITLE_PACKAGE_REPORT =
		"ch10_titlepackagereport";
	public final static String TRANSACTION_RECONCILIATION_REPORT =
		"ch15_fundsbalancereports";
	final public static String TTL001 = "ch06_salvagelienentry";
	final public static String TTL002A = "ch06_titletypes";
	final public static String TTL002B = "ch06_titletypes_manual";
	final public static String TTL002C = "ch06_titletypes_dtadisk";
	final public static String TTL002D =
		"ch14_titletypes_mfunavlbltitle";
	final public static String TTL003A = "ch06_titlerecord";
	final public static String TTL003B = "ch06_titlerecord_dtamanual";
	final public static String TTL003C = "ch06_titlerecord_dtadisk";
	final public static String TTL003D =
		"ch14_titlerecord_mfunavlbltitle";
	final public static String TTL004A = "ch06_notitlerecord";
	final public static String TTL004B =
		"ch14_nottlrecord_mfunavlbltitle";
	final public static String TTL004C = "ch06_notitlerecord_dtamanual";
	final public static String TTL004D = "ch99_nohelpavailable";
	final public static String TTL006 = "ch06_statuschangerecord";
	final public static String TTL007A = "ch06_ownerentry";
	final public static String TTL007B = "ch06_ownerentry_dtamanual";
	final public static String TTL007C = "ch06_ownerentry_dtadisk";
	final public static String TTL007D =
		"ch14_ownerentry_mfunavlbltitle";
	final public static String TTL008A = "ch06_titleadditionalinfo";
	final public static String TTL008B =
		"ch06_titleadditionalinfo_dtamanual";

	final public static String TTL008C =
		"ch06_titleadditionalinfo_dtadisk";
	final public static String TTL008D =
		"ch14_titleadlinfo_mfunavlbltitle";

	final public static String TTL011A = "ch06_ownershipevidence";
	final public static String TTL011B =
		"ch06_ownershipevidence_dtamanual";

	final public static String TTL011C =
		"ch06_ownershipevidence_dtadisk";
	final public static String TTL011D =
		"ch14_ownershipevidence_mfunavlbltitle";
	final public static String TTL012A = "ch06_salestax";
	final public static String TTL012B = "ch06_salestax_dtamanual";
	final public static String TTL012C = "ch06_salestax_dtadisk";
	final public static String TTL012D = "ch14_salestax_mfunavlbltitle";
	final public static String TTL013 = "ch99_nohelpavailable";
	// salvage CCO Mailing Info
	final public static String TTL015 = "ch06_coa";
	final public static String TTL016 = "ch06_salvage";
	final public static String TTL018 = "ch06_ccoccdo";
	final public static String TTL019 = "ch06_ccoccdomailinginfo";
	final public static String TTL028 = "ch06_vehiclejunked";
	final public static String TTL029 = "ch06_titlesurrendered";
	final public static String TTL030 = "ch06_miscellaneousremarks";
	final public static String TTL033 = "ch06_additionalsalestax";
	final public static String TTL035A = "ch06_lienentry";
	final public static String TTL035B = "ch06_lienentry_dtamanual";
	final public static String TTL035C = "ch06_lienentry_dtadisk";
	final public static String TTL035D =
		"ch14_lienentry_mfunavlbltitle";
	final public static String TTL037 = "ch06_stolensrs";
	final public static String TTL040 = "ch14_transferother";

	// defect 8786
	final public static String TTL042 = "ch06_deletetitleinprocess";
	// end defect 8786
	final public static String TTL044 = "ch06_ccoissuedate";
	// defect 8926
	final public static String TTL045 = "ch06_presumptivevalue";
	// end defect 8926

	/**
	 * Adds the chapter part of the url.  
	 * Gets combined with the chapter number. 
	 */
	final private static String URL_STRING2 = "RTS2CH";
	
	/**
	 * Adds the htm component to the url.
	 */
	final private static String URL_STRING3 = ".htm";
		
	final public static String VOI001 = "ch08_tovoidatransaction";
	final public static String VOI002 =
		"ch08_transactionsavailabletovoid";

	/**
	 *	Method called to display the specified bookmark in a browser.
	 *
	 *  @param asBookmark java.lang.String
	 **/
	public static void displayHelp(String asBookmark)
	{
		try
		{
			Runtime laRunTime = Runtime.getRuntime();

			// defect 10374
			// releaseBrowser();
			// end defect 10374

			// Getting default browser path from the static cfg file
			String lsURL =
				SystemProperty.getDefaultBrowserPath()
					+ CommonConstant.STR_SPACE_ONE
					+SystemProperty.getHelpDir()
					+ URL_STRING2
					+ asBookmark.substring(2, 4)
					+ URL_STRING3
					+ CommonConstant.STR_POUND
					+ asBookmark;

			System.out.println(lsURL);
			Log.write(Log.SQL_EXCP, lsURL, lsURL);
			setBrowser(laRunTime.exec(lsURL));

		}
		catch (IOException leIOEx)
		{
			leIOEx.printStackTrace();
			setBrowser(null);
		}
	}

	/**
	 * Method called to display the specified page in a browser.
	 * This could be a direct link to an html or pdf.
	 * 
	 * @param asForm java.lang.String
	 */
	public static void displayPage(String asForm)
	{
		try
		{
			Runtime laRunTime = Runtime.getRuntime();

			// defect 10374
			// releaseBrowser();
			// end defect 10374

			// defect 10151
			// Check for FTP. FTPs have the full path name. 
			//String lsURL =
			//	SystemProperty.getDefaultBrowserPath()
			//		+ CommonConstant.STR_SPACE_ONE
			//		+ SystemProperty.getHelpDir()
			//		+ asForm;
			String lsURL = SystemProperty.getDefaultBrowserPath()
							+ CommonConstant.STR_SPACE_ONE;
							
			//if (asForm.substring(0,3).equals(FTP))
			// allow for http page access outside of the local system
			if (asForm.substring(0,REMOTE_HTTP_LENGTH).equalsIgnoreCase(REMOTE_HTTP))
			{
				lsURL = lsURL 
							+ asForm.substring(REMOTE_HTTP_LENGTH + 1);
			}
			else
			{
				lsURL =	lsURL
							+ SystemProperty.getHelpDir()
							+ asForm;
			}
			// end defect 10151

			System.out.println(lsURL);
			Log.write(Log.SQL_EXCP, lsURL, lsURL);
			setBrowser(laRunTime.exec(lsURL));
		}
		catch (IOException leIOEx)
		{
			leIOEx.printStackTrace();
			setBrowser(null);
		}
	}
	
	/**
	 *  Method to get the handle to the browser.
	 *  
	 *  @deprecated
	 **/
	private static synchronized Process getBrowser()
	{
		return caBrowser;
	}
	
	/**
	 *  Method to test the bookmarks of the online users guide.
	 * 
	 * @param aaArgs Array
	 **/
	public static void main(String[] aaArgs)
	{
		try
		{
			while (true)
			{
				System.out.println("Enter a screen name...");
				BufferedReader laBuffReader =
					new BufferedReader(
						new InputStreamReader(System.in));
				String lsScreen = laBuffReader.readLine();
				if (lsScreen.trim().equals(""))
				{
					break;
				}

				if (lsScreen.toUpperCase().equals("ACC001"))
				{
					displayHelp(RTSHelp.ACC001);
				}
				else if (lsScreen.toUpperCase().equals("ACC004A"))
				{
					displayHelp(RTSHelp.ACC004A);
				}
				else if (lsScreen.toUpperCase().equals("ACC004B"))
				{
					displayHelp(RTSHelp.ACC004B);
				}
				else if (lsScreen.toUpperCase().equals("ACC004C"))
				{
					displayHelp(RTSHelp.ACC004C);
				}
				else if (lsScreen.toUpperCase().equals("ACC005"))
				{
					displayHelp(RTSHelp.ACC005);
				}
				else if (lsScreen.toUpperCase().equals("ACC006"))
				{
					displayHelp(RTSHelp.ACC006);
				}
				else if (lsScreen.toUpperCase().equals("ACC018"))
				{
					displayHelp(RTSHelp.ACC018);
				}
				// defect 8177
				else if (lsScreen.toUpperCase().equals("ACC020"))
				{
					displayHelp(RTSHelp.ACC020);
				}
				// end defect 8177
				else if (lsScreen.toUpperCase().equals("ACC022"))
				{
					displayHelp(RTSHelp.ACC022);
				}
				else if (lsScreen.toUpperCase().equals("ACC023"))
				{
					displayHelp(RTSHelp.ACC023);
				}
				else if (lsScreen.toUpperCase().equals("ACC024"))
				{
					displayHelp(RTSHelp.ACC024);
				}
				else if (lsScreen.toUpperCase().equals("CTL001"))
				{
					displayHelp(RTSHelp.CTL001);
				}
				else if (lsScreen.toUpperCase().equals("CTL004"))
				{
					displayHelp(RTSHelp.CTL004);
				}
				else if (lsScreen.toUpperCase().equals("CTL005"))
				{
					displayHelp(RTSHelp.CTL005);
				}
				else if (lsScreen.toUpperCase().equals("CTL006A"))
				{
					displayHelp(RTSHelp.CTL006A);
				}
				else if (lsScreen.toUpperCase().equals("CTL006B"))
				{
					displayHelp(RTSHelp.CTL006B);
				}
				else if (lsScreen.toUpperCase().equals("CTL006C"))
				{
					displayHelp(RTSHelp.CTL006C);
				}
				else if (lsScreen.toUpperCase().equals("CTL006D"))
				{
					displayHelp(RTSHelp.CTL006D);
				}
				else if (lsScreen.toUpperCase().equals("CTL006E"))
				{
					displayHelp(RTSHelp.CTL006E);
				}
				else if (lsScreen.toUpperCase().equals("CTL006F"))
				{
					displayHelp(RTSHelp.CTL006F);
				}
				else if (lsScreen.toUpperCase().equals("CTL006G"))
				{
					displayHelp(RTSHelp.CTL006G);
				}
				else if (lsScreen.toUpperCase().equals("CTL006H"))
				{
					displayHelp(RTSHelp.CTL006H);
				}
				// end defect 8177
				else if (lsScreen.toUpperCase().equals("CTL009"))
				{
					displayHelp(RTSHelp.CTL009);
				}
				else if (lsScreen.toUpperCase().equals("CTL010"))
				{
					displayHelp(RTSHelp.CTL010);
				}

				else if (lsScreen.toUpperCase().equals("CUS003A"))
				{
					displayHelp(RTSHelp.CUS003A);
				}
				else if (lsScreen.toUpperCase().equals("CUS003B"))
				{
					displayHelp(RTSHelp.CUS003B);
				}
				else if (lsScreen.toUpperCase().equals("CUS003C"))
				{
					displayHelp(RTSHelp.CUS003C);
				}
				else if (lsScreen.toUpperCase().equals("DTA001A"))
				{
					displayHelp(RTSHelp.DTA001A);
				}
				else if (lsScreen.toUpperCase().equals("DTA001B"))
				{
					displayHelp(RTSHelp.DTA001B);
				}
				else if (lsScreen.toUpperCase().equals("DTA003"))
				{
					displayHelp(RTSHelp.DTA003);
				}
				else if (lsScreen.toUpperCase().equals("DTA004"))
				{
					displayHelp(RTSHelp.DTA004);
				}
				else if (lsScreen.toUpperCase().equals("DTA005"))
				{
					displayHelp(RTSHelp.DTA005);
				}
				else if (lsScreen.toUpperCase().equals("DTA007"))
				{
					displayHelp(RTSHelp.DTA007);
				}
				// defect 6963
				//	Set conditional to correct screen name
				//	DTA008 - Dealer Transaction Screen
				//	DTA009 - Manual Entry Screen
				else if (lsScreen.toUpperCase().equals("DTA008"))
				{
					displayHelp(RTSHelp.DTA008);
				}
				else if (lsScreen.toUpperCase().equals("DTA009"))
				{
					displayHelp(RTSHelp.DTA009);
				}
				// end defect 6963
				else if (lsScreen.toUpperCase().equals("FUN001"))
				{
					displayHelp(RTSHelp.FUN001);
				}
				else if (lsScreen.toUpperCase().equals("FUN002"))
				{
					displayHelp(RTSHelp.FUN002);
				}
				else if (lsScreen.toUpperCase().equals("FUN003"))
				{
					displayHelp(RTSHelp.FUN003);
				}
				else if (lsScreen.toUpperCase().equals("FUN006"))
				{
					displayHelp(RTSHelp.FUN006);
				}
				else if (lsScreen.toUpperCase().equals("FUN007"))
				{
					displayHelp(RTSHelp.FUN007);
				}
				else if (lsScreen.toUpperCase().equals("FUN008"))
				{
					displayHelp(RTSHelp.FUN008);
				}
				else if (lsScreen.toUpperCase().equals("FUN010"))
				{
					displayHelp(RTSHelp.FUN010);
				}
				else if (lsScreen.toUpperCase().equals("FUN011"))
				{
					displayHelp(RTSHelp.FUN011);
				}
				else if (lsScreen.toUpperCase().equals("FUN012"))
				{
					displayHelp(RTSHelp.FUN012);
				}
				else if (lsScreen.toUpperCase().equals("FUN015"))
				{
					displayHelp(RTSHelp.FUN015);
				}
				else if (lsScreen.toUpperCase().equals("INQ004"))
				{
					displayHelp(RTSHelp.INQ004);
				}
				else if (lsScreen.toUpperCase().equals("INQ008"))
				{
					displayHelp(RTSHelp.INQ008);
				}
				else if (lsScreen.toUpperCase().equals("INQ007"))
				{
					displayHelp(RTSHelp.INQ007);
				}
				else if (lsScreen.toUpperCase().equals("INQ008"))
				{
					displayHelp(RTSHelp.INQ008);
				}
				else if (lsScreen.toUpperCase().equals("INV001"))
				{
					displayHelp(RTSHelp.INV001);
				}
				// defect 8177
				else if (lsScreen.toUpperCase().equals("INV001A"))
				{
					displayHelp(RTSHelp.INV001);
				}
				// end defect 8177
				else if (lsScreen.toUpperCase().equals("INV002"))
				{
					displayHelp(RTSHelp.INV002);
				}

				else if (lsScreen.toUpperCase().equals("INV003A"))
				{
					displayHelp(RTSHelp.INV003A);
				}
				else if (lsScreen.toUpperCase().equals("INV003B"))
				{
					displayHelp(RTSHelp.INV003B);
				}
				// end defect 6966 
				else if (lsScreen.toUpperCase().equals("INV004"))
				{
					displayHelp(RTSHelp.INV004);
				}
				else if (lsScreen.toUpperCase().equals("INV005"))
				{
					displayHelp(RTSHelp.INV005);
				}
				// defect 8177
				else if (lsScreen.toUpperCase().equals("INV005A"))
				{
					displayHelp(RTSHelp.INV005A);
				}
				// end defect 8177
				else if (lsScreen.toUpperCase().equals("INV006"))
				{
					displayHelp(RTSHelp.INV006);
				}
				else if (lsScreen.toUpperCase().equals("INV007"))
				{
					displayHelp(RTSHelp.INV007);
				}
				// defect 8177 
				else if (lsScreen.toUpperCase().equals("INV007A"))
				{
					displayHelp(RTSHelp.INV007A);
				}

				else if (lsScreen.toUpperCase().equals("INV009"))
				{
					displayHelp(RTSHelp.INV009);
				}
				else if (lsScreen.toUpperCase().equals("INV010"))
				{
					displayHelp(RTSHelp.INV010);
				}
				else if (lsScreen.toUpperCase().equals("INV011"))
				{
					displayHelp(RTSHelp.INV011);
				}
				else if (lsScreen.toUpperCase().equals("INV012"))
				{
					displayHelp(RTSHelp.INV012);
				}
				else if (lsScreen.toUpperCase().equals("INV013"))
				{
					displayHelp(RTSHelp.INV013);
				}
				else if (lsScreen.toUpperCase().equals("INV014A"))
				{
					displayHelp(RTSHelp.INV014A);
				}
				// defect 8177 
				else if (lsScreen.toUpperCase().equals("INV015"))
				{
					displayHelp(RTSHelp.INV015);
				}
				// end defect 8177 
				else if (lsScreen.toUpperCase().equals("INV028"))
				{
					displayHelp(RTSHelp.INV028);
				}
				else if (lsScreen.toUpperCase().equals("INV016"))
				{
					displayHelp(RTSHelp.INV016);
				}
				else if (lsScreen.toUpperCase().equals("INV017"))
				{
					displayHelp(RTSHelp.INV017);
				}
				else if (lsScreen.toUpperCase().equals("INV018"))
				{
					displayHelp(RTSHelp.INV018);
				}
				// defect 6964
				else if (lsScreen.toUpperCase().equals("INV019"))
				{
					displayHelp(RTSHelp.INV019);
				}
				else if (lsScreen.toUpperCase().equals("INV020"))
				{
					displayHelp(RTSHelp.INV020);
				}
				// end defect 6964
				else if (lsScreen.toUpperCase().equals("INV021"))
				{
					displayHelp(RTSHelp.INV021);
				}
				else if (lsScreen.toUpperCase().equals("INV022"))
				{
					displayHelp(RTSHelp.INV022);
				}
				else if (lsScreen.toUpperCase().equals("INV023"))
				{
					displayHelp(RTSHelp.INV023);
				}
				else if (lsScreen.toUpperCase().equals("INV024"))
				{
					displayHelp(RTSHelp.INV024);
				}
				else if (lsScreen.toUpperCase().equals("INV025"))
				{
					displayHelp(RTSHelp.INV025);
				}
				else if (lsScreen.toUpperCase().equals("INV026"))
				{
					displayHelp(RTSHelp.INV026);
				}
				else if (lsScreen.toUpperCase().equals("INV027"))
				{
					displayHelp(RTSHelp.INV027);
				}
				else if (lsScreen.toUpperCase().equals("INV028"))
				{
					displayHelp(RTSHelp.INV028);
				}

				else if (lsScreen.toUpperCase().equals("INV029C"))
				{
					displayHelp(RTSHelp.INV029C);
				}
				else if (lsScreen.toUpperCase().equals("INV029D"))
				{
					displayHelp(RTSHelp.INV029D);
				}
				else if (lsScreen.toUpperCase().equals("INV029E"))
				{
					displayHelp(RTSHelp.INV029E);
				}
				else if (lsScreen.toUpperCase().equals("INV029F"))
				{
					displayHelp(RTSHelp.INV029F);
				}
				else if (lsScreen.toUpperCase().equals("INV029G"))
				{
					displayHelp(RTSHelp.INV029G);
				}
				else if (lsScreen.toUpperCase().equals("INV029H"))
				{
					displayHelp(RTSHelp.INV029H);
				}
				else if (lsScreen.toUpperCase().equals("INV029I"))
				{
					displayHelp(RTSHelp.INV029I);
				}
				else if (lsScreen.toUpperCase().equals("INV029J"))
				{
					displayHelp(RTSHelp.INV029J);
				}
				else if (lsScreen.toUpperCase().equals("INV029K"))
				{
					displayHelp(RTSHelp.INV029K);
				}
				else if (lsScreen.toUpperCase().equals("INV029L"))
				{
					displayHelp(RTSHelp.INV029L);
				}
				else if (lsScreen.toUpperCase().equals("INV029M"))
				{
					displayHelp(RTSHelp.INV029M);
				}
				else if (lsScreen.toUpperCase().equals("INV029N"))
				{
					displayHelp(RTSHelp.INV029N);
				}
				else if (lsScreen.toUpperCase().equals("INV029O"))
				{
					displayHelp(RTSHelp.INV029O);
				}
				// defect 8177
				else if (lsScreen.toUpperCase().equals("INV029P"))
				{
					displayHelp(RTSHelp.INV029P);
				}
				else if (lsScreen.toUpperCase().equals("INV029Q"))
				{
					displayHelp(RTSHelp.INV029Q);
				}
				// end defect 8177
				else if (lsScreen.toUpperCase().equals("INV030"))
				{
					displayHelp(RTSHelp.INV030);
				}
				else if (lsScreen.toUpperCase().equals("KEY001A"))
				{
					displayHelp(RTSHelp.KEY001A);
				}
				else if (lsScreen.toUpperCase().equals("KEY001B"))
				{
					displayHelp(RTSHelp.KEY001B);
				}
				else if (lsScreen.toUpperCase().equals("KEY001C"))
				{
					displayHelp(RTSHelp.KEY001C);
				}
				else if (lsScreen.toUpperCase().equals("KEY001D"))
				{
					displayHelp(RTSHelp.KEY001D);
				}
				else if (lsScreen.toUpperCase().equals("KEY001E"))
				{
					displayHelp(RTSHelp.KEY001E);
				}
				else if (lsScreen.toUpperCase().equals("KEY001F"))
				{
					displayHelp(RTSHelp.KEY001F);
				}
				else if (lsScreen.toUpperCase().equals("KEY001G"))
				{
					displayHelp(RTSHelp.KEY001G);
				}
				else if (lsScreen.toUpperCase().equals("KEY001H"))
				{
					displayHelp(RTSHelp.KEY001H);
				}
				else if (lsScreen.toUpperCase().equals("KEY001I"))
				{
					displayHelp(RTSHelp.KEY001I);
				}
				else if (lsScreen.toUpperCase().equals("KEY001J"))
				{
					displayHelp(RTSHelp.KEY001J);
				}
				else if (lsScreen.toUpperCase().equals("KEY001K"))
				{
					displayHelp(RTSHelp.KEY001K);
				}
				else if (lsScreen.toUpperCase().equals("KEY001L"))
				{
					displayHelp(RTSHelp.KEY001L);
				}
				else if (lsScreen.toUpperCase().equals("KEY001M"))
				{
					displayHelp(RTSHelp.KEY001M);
				}
				else if (lsScreen.toUpperCase().equals("KEY001N"))
				{
					displayHelp(RTSHelp.KEY001N);
				}
				else if (lsScreen.toUpperCase().equals("KEY001O"))
				{
					displayHelp(RTSHelp.KEY001O);
				}
				else if (lsScreen.toUpperCase().equals("KEY001P"))
				{
					displayHelp(RTSHelp.KEY001P);
				}
				else if (lsScreen.toUpperCase().equals("KEY001Q"))
				{
					displayHelp(RTSHelp.KEY001Q);
				}
				else if (lsScreen.toUpperCase().equals("KEY001R"))
				{
					displayHelp(RTSHelp.KEY001R);
				}
				else if (lsScreen.toUpperCase().equals("KEY001S"))
				{
					displayHelp(RTSHelp.KEY001S);
				}
				else if (lsScreen.toUpperCase().equals("KEY001T"))
				{
					displayHelp(RTSHelp.KEY001T);
				}
				else if (lsScreen.toUpperCase().equals("KEY001U"))
				{
					displayHelp(RTSHelp.KEY001U);
				}
				else if (lsScreen.toUpperCase().equals("KEY001V"))
				{
					displayHelp(RTSHelp.KEY001V);
				}
				else if (lsScreen.toUpperCase().equals("KEY001W"))
				{
					displayHelp(RTSHelp.KEY001W);
				}
				else if (lsScreen.toUpperCase().equals("KEY001X"))
				{
					displayHelp(RTSHelp.KEY001X);
				}
				else if (lsScreen.toUpperCase().equals("KEY001Y"))
				{
					displayHelp(RTSHelp.KEY001Y);
				}
				else if (lsScreen.toUpperCase().equals("KEY003"))
				{
					displayHelp(RTSHelp.KEY003);
				}
				else if (lsScreen.toUpperCase().equals("KEY006A"))
				{
					displayHelp(RTSHelp.KEY006A);
				}
				else if (lsScreen.toUpperCase().equals("KEY006B"))
				{
					displayHelp(RTSHelp.KEY006B);
				}
				else if (lsScreen.toUpperCase().equals("KEY006C"))
				{
					displayHelp(RTSHelp.KEY006C);
				}
				else if (lsScreen.toUpperCase().equals("KEY006D"))
				{
					displayHelp(RTSHelp.KEY006D);
				}
				else if (lsScreen.toUpperCase().equals("KEY006E"))
				{
					displayHelp(RTSHelp.KEY006E);
				}
				else if (lsScreen.toUpperCase().equals("KEY006F"))
				{
					displayHelp(RTSHelp.KEY006F);
				}
				else if (lsScreen.toUpperCase().equals("KEY007"))
				{
					displayHelp(RTSHelp.KEY007);
				}
				else if (lsScreen.toUpperCase().equals("KEY008"))
				{
					displayHelp(RTSHelp.KEY008);
				}
				// defect 8177
				else if (lsScreen.toUpperCase().equals("KEY008A"))
				{
					displayHelp(RTSHelp.KEY008A);
				}
				// end defect 8177
				else if (lsScreen.toUpperCase().equals("MRG001"))
				{
					displayHelp(RTSHelp.MRG001);
				}
				// defect 10004
				//else if (lsScreen.toUpperCase().equals("MRG002"))
				//{
				//	displayHelp(RTSHelp.MRG002);
				//}
				// end defect 10004
				else if (lsScreen.toUpperCase().equals("MRG004"))
				{
					displayHelp(RTSHelp.MRG004);
				}
				else if (lsScreen.toUpperCase().equals("MRG005"))
				{
					displayHelp(RTSHelp.MRG005);
				}
				else if (
					lsScreen.toUpperCase().equals("MRG006_144_HOUR"))
				{
					displayHelp(RTSHelp.MRG006_144_HOUR);
				}
				else if (
					lsScreen.toUpperCase().equals("MRG006_30_DAY"))
				{
					displayHelp(RTSHelp.MRG006_30_DAY);
				}
				else if (
					lsScreen.toUpperCase().equals("MRG006_72_HOUR"))
				{
					displayHelp(RTSHelp.MRG006_72_HOUR);
				}
				else if (
					lsScreen.toUpperCase().equals("MRG006_FACTORY"))
				{
					displayHelp(RTSHelp.MRG006_FACTORY);
				}
				else if (
					lsScreen.toUpperCase().equals("MRG006_ONE_TRIP"))
				{
					displayHelp(RTSHelp.MRG006_ONE_TRIP);
				}
				else if (lsScreen.toUpperCase().equals("MRG008"))
				{
					displayHelp(RTSHelp.MRG008);
				}

				else if (lsScreen.toUpperCase().equals("MRG010A"))
				{
					displayHelp(RTSHelp.MRG010A);
				}
				else if (lsScreen.toUpperCase().equals("MRG010B"))
				{
					displayHelp(RTSHelp.MRG010B);
				}

				else if (lsScreen.toUpperCase().equals("MRG011A"))
				{
					displayHelp(RTSHelp.MRG011A);
				}
				else if (lsScreen.toUpperCase().equals("MRG011B"))
				{
					displayHelp(RTSHelp.MRG011B);
				}

				else if (lsScreen.toUpperCase().equals("MRG013"))
				{
					displayHelp(RTSHelp.MRG013);
				}
				// defect 10004
				else if (lsScreen.toUpperCase().equals("MRG020"))
				{
					displayHelp(RTSHelp.MRG020);
				}
				// end defect 10004
				else if (lsScreen.toUpperCase().equals("PMT001A"))
				{
					displayHelp(RTSHelp.PMT001A);
				}
				else if (lsScreen.toUpperCase().equals("PMT001B"))
				{
					displayHelp(RTSHelp.PMT001B);
				}
				else if (lsScreen.toUpperCase().equals("PMT004A"))
				{
					displayHelp(RTSHelp.PMT004A);
				}
				else if (lsScreen.toUpperCase().equals("PMT004B"))
				{
					displayHelp(RTSHelp.PMT004B);
				}
				else if (lsScreen.toUpperCase().equals("PMT004C"))
				{
					displayHelp(RTSHelp.PMT004C);
				}
				else if (lsScreen.toUpperCase().equals("PMT004D"))
				{
					displayHelp(RTSHelp.PMT004D);
				}
				else if (lsScreen.toUpperCase().equals("PMT004E"))
				{
					displayHelp(RTSHelp.PMT004E);
				}
				else if (lsScreen.toUpperCase().equals("PMT004F"))
				{
					displayHelp(RTSHelp.PMT004F);
				}
				else if (lsScreen.toUpperCase().equals("PMT004G"))
				{
					displayHelp(RTSHelp.PMT004G);
				}

				else if (lsScreen.toUpperCase().equals("PMT004I"))
				{
					displayHelp(RTSHelp.PMT004I);
				}
				else if (lsScreen.toUpperCase().equals("PMT004J"))
				{
					displayHelp(RTSHelp.PMT004J);
				}
				else if (lsScreen.toUpperCase().equals("PMT004K"))
				{
					displayHelp(RTSHelp.PMT004K);
				}
				else if (lsScreen.toUpperCase().equals("PMT004L"))
				{
					displayHelp(RTSHelp.PMT004L);
				}
				else if (lsScreen.toUpperCase().equals("PMT004M"))
				{
					displayHelp(RTSHelp.PMT004M);
				}
				else if (lsScreen.toUpperCase().equals("PMT004N"))
				{
					displayHelp(RTSHelp.PMT004N);
				}
				else if (lsScreen.toUpperCase().equals("PMT004O"))
				{
					displayHelp(RTSHelp.PMT004O);
				}
				else if (lsScreen.toUpperCase().equals("PMT004P"))
				{
					displayHelp(RTSHelp.PMT004P);
				}
				else if (lsScreen.toUpperCase().equals("PMT004Q"))
				{
					displayHelp(RTSHelp.PMT004Q);
				}
				else if (lsScreen.toUpperCase().equals("PMT004R"))
				{
					displayHelp(RTSHelp.PMT004R);
				}
				else if (lsScreen.toUpperCase().equals("PMT004S"))
				{
					displayHelp(RTSHelp.PMT004S);
				}
				else if (lsScreen.toUpperCase().equals("PMT004T"))
				{
					displayHelp(RTSHelp.PMT004T);
				}
				else if (lsScreen.toUpperCase().equals("PMT004U"))
				{
					displayHelp(RTSHelp.PMT004U);
				}
				else if (lsScreen.toUpperCase().equals("PMT004V"))
				{
					displayHelp(RTSHelp.PMT004V);
				}
				else if (lsScreen.toUpperCase().equals("PMT004W"))
				{
					displayHelp(RTSHelp.PMT004W);
				}
				else if (lsScreen.toUpperCase().equals("PMT004X"))
				{
					displayHelp(RTSHelp.PMT004X);
				}
				else if (lsScreen.toUpperCase().equals("PMT004Y"))
				{
					displayHelp(RTSHelp.PMT004Y);
				}
				else if (lsScreen.toUpperCase().equals("PMT004Z"))
				{
					displayHelp(RTSHelp.PMT004Z);
				}
				else if (lsScreen.toUpperCase().equals("PMT004aa"))
				{
					displayHelp(RTSHelp.PMT004aa);
				}
				else if (lsScreen.toUpperCase().equals("PMT004bb"))
				{
					displayHelp(RTSHelp.PMT004bb);
				}
				else if (lsScreen.toUpperCase().equals("PMT004cc"))
				{
					displayHelp(RTSHelp.PMT004cc);
				}
				else if (lsScreen.toUpperCase().equals("PMT004dd"))
				{
					displayHelp(RTSHelp.PMT004dd);
				}
				else if (lsScreen.toUpperCase().equals("PMT004ee"))
				{
					displayHelp(RTSHelp.PMT004ee);
				}
				// defect 8177
				else if (lsScreen.toUpperCase().equals("PUB002"))
				{
					displayHelp(RTSHelp.PUB002);
				}
				// end defect 8177
				else if (lsScreen.toUpperCase().equals("REG001"))
				{
					displayHelp(RTSHelp.REG001);
				}
				// defect 8177
				else if (lsScreen.toUpperCase().equals("REG001A"))
				{
					displayHelp(RTSHelp.REG001A);
				}
				// end defect 8177
				else if (lsScreen.toUpperCase().equals("REG002"))
				{
					displayHelp(RTSHelp.REG002);
				}
				else if (lsScreen.toUpperCase().equals("REG003A"))
				{
					displayHelp(RTSHelp.REG003A);
				}
				else if (lsScreen.toUpperCase().equals("REG003B"))
				{
					displayHelp(RTSHelp.REG003B);
				}
				else if (lsScreen.toUpperCase().equals("REG003C"))
				{
					displayHelp(RTSHelp.REG003C);
				}
				else if (lsScreen.toUpperCase().equals("REG003D"))
				{
					displayHelp(RTSHelp.REG003D);
				}
				else if (lsScreen.toUpperCase().equals("REG003E"))
				{
					displayHelp(RTSHelp.REG003E);
				}
				else if (lsScreen.toUpperCase().equals("REG003F"))
				{
					displayHelp(RTSHelp.REG003F);
				}
				else if (lsScreen.toUpperCase().equals("REG003G"))
				{
					displayHelp(RTSHelp.REG003G);
				}
				else if (lsScreen.toUpperCase().equals("REG003H"))
				{
					displayHelp(RTSHelp.REG003H);
				}
				else if (lsScreen.toUpperCase().equals("REG003I"))
				{
					displayHelp(RTSHelp.REG003I);
				}
				else if (lsScreen.toUpperCase().equals("REG003J"))
				{
					displayHelp(RTSHelp.REG003J);
				}
				else if (lsScreen.toUpperCase().equals("REG003K"))
				{
					displayHelp(RTSHelp.REG003K);
				}
				else if (lsScreen.toUpperCase().equals("REG003L"))
				{
					displayHelp(RTSHelp.REG003L);
				}
				else if (lsScreen.toUpperCase().equals("REG003M"))
				{
					displayHelp(RTSHelp.REG003M);
				}
				else if (lsScreen.toUpperCase().equals("REG003N"))
				{
					displayHelp(RTSHelp.REG003N);
				}
				// defect 8177
				else if (lsScreen.toUpperCase().equals("REG003O"))
				{
					displayHelp(RTSHelp.REG003O);
				}
				else if (lsScreen.toUpperCase().equals("REG003P"))
				{
					displayHelp(RTSHelp.REG003P);
				}
				else if (lsScreen.toUpperCase().equals("REG003Q"))
				{
					displayHelp(RTSHelp.REG003Q);
				}
				else if (lsScreen.toUpperCase().equals("REG003R"))
				{
					displayHelp(RTSHelp.REG003R);
				}
				else if (lsScreen.toUpperCase().equals("REG003S"))
				{
					displayHelp(RTSHelp.REG003S);
				}
				else if (lsScreen.toUpperCase().equals("REG003T"))
				{
					displayHelp(RTSHelp.REG003T);
				}
				// end defect 8177
				else if (lsScreen.toUpperCase().equals("REG004"))
				{
					displayHelp(RTSHelp.REG004);
				}
				else if (lsScreen.toUpperCase().equals("REG005"))
				{
					displayHelp(RTSHelp.REG005);
				}

				else if (lsScreen.toUpperCase().equals("REG006A"))
				{
					displayHelp(RTSHelp.REG006A);
				}
				else if (lsScreen.toUpperCase().equals("REG006B"))
				{
					displayHelp(RTSHelp.REG006B);
				}

				else if (lsScreen.toUpperCase().equals("REG007A"))
				{
					displayHelp(RTSHelp.REG007A);
				}
				else if (lsScreen.toUpperCase().equals("REG007B"))
				{
					displayHelp(RTSHelp.REG007B);
				}
				// end defect 8177
				else if (lsScreen.toUpperCase().equals("REG008"))
				{
					displayHelp(RTSHelp.REG008);
				}
				else if (lsScreen.toUpperCase().equals("REG008A"))
				{
					displayHelp(RTSHelp.REG008A);
				}
				else if (lsScreen.toUpperCase().equals("REG008B"))
				{
					displayHelp(RTSHelp.REG008B);
				}
				else if (lsScreen.toUpperCase().equals("REG008C"))
				{
					displayHelp(RTSHelp.REG008C);
				}
				else if (lsScreen.toUpperCase().equals("REG008D"))
				{
					displayHelp(RTSHelp.REG008D);
				}
				else if (lsScreen.toUpperCase().equals("REG008E"))
				{
					displayHelp(RTSHelp.REG008E);
				}
				else if (lsScreen.toUpperCase().equals("REG008F"))
				{
					displayHelp(RTSHelp.REG008F);
				}
				else if (lsScreen.toUpperCase().equals("REG008G"))
				{
					displayHelp(RTSHelp.REG008G);
				}
				else if (lsScreen.toUpperCase().equals("REG008H"))
				{
					displayHelp(RTSHelp.REG008H);
				}
				else if (lsScreen.toUpperCase().equals("REG008I"))
				{
					displayHelp(RTSHelp.REG008I);
				}
				else if (lsScreen.toUpperCase().equals("REG008J"))
				{
					displayHelp(RTSHelp.REG008J);
				}
				else if (lsScreen.toUpperCase().equals("REG008K"))
				{
					displayHelp(RTSHelp.REG008K);
				}
				else if (lsScreen.toUpperCase().equals("REG008L"))
				{
					displayHelp(RTSHelp.REG008L);
				}
				else if (lsScreen.toUpperCase().equals("REG008M"))
				{
					displayHelp(RTSHelp.REG008M);
				}
				else if (lsScreen.toUpperCase().equals("REG008N"))
				{
					displayHelp(RTSHelp.REG008N);
				}
				// defect 8177
				else if (lsScreen.toUpperCase().equals("REG008O"))
				{
					displayHelp(RTSHelp.REG008O);
				}
				else if (lsScreen.toUpperCase().equals("REG008P"))
				{
					displayHelp(RTSHelp.REG008P);
				}

				else if (lsScreen.toUpperCase().equals("REG010"))
				{
					displayHelp(RTSHelp.REG010);
				}
				else if (lsScreen.toUpperCase().equals("REG010A"))
				{
					displayHelp(RTSHelp.REG010A);
				}
				else if (lsScreen.toUpperCase().equals("REG010B"))
				{
					displayHelp(RTSHelp.REG010B);
				}

				else if (lsScreen.toUpperCase().equals("REG014A"))
				{
					displayHelp(RTSHelp.REG014A);
				}
				else if (lsScreen.toUpperCase().equals("REG014B"))
				{
					displayHelp(RTSHelp.REG014B);
				}
				else if (lsScreen.toUpperCase().equals("REG014C"))
				{
					displayHelp(RTSHelp.REG014C);
				}
				else if (lsScreen.toUpperCase().equals("REG014D"))
				{
					displayHelp(RTSHelp.REG014D);
				}
				else if (lsScreen.toUpperCase().equals("REG014E"))
				{
					displayHelp(RTSHelp.REG014E);
				}
				else if (lsScreen.toUpperCase().equals("REG014F"))
				{
					displayHelp(RTSHelp.REG014F);
				}
				else if (lsScreen.toUpperCase().equals("REG014G"))
				{
					displayHelp(RTSHelp.REG014G);
				}
				else if (lsScreen.toUpperCase().equals("REG014H"))
				{
					displayHelp(RTSHelp.REG014H);
				}

				else if (lsScreen.toUpperCase().equals("REG016"))
				{
					displayHelp(RTSHelp.REG016);
				}
				else if (lsScreen.toUpperCase().equals("REG016A"))
				{
					displayHelp(RTSHelp.REG016A);
				}
				else if (lsScreen.toUpperCase().equals("REG024"))
				{
					displayHelp(RTSHelp.REG024);
				}
				else if (lsScreen.toUpperCase().equals("REG024A"))
				{
					displayHelp(RTSHelp.REG024A);
				}
				else if (lsScreen.toUpperCase().equals("REG024B"))
				{
					displayHelp(RTSHelp.REG024B);
				}
				else if (lsScreen.toUpperCase().equals("REG024C"))
				{
					displayHelp(RTSHelp.REG024C);
				}

				else if (lsScreen.toUpperCase().equals("REG025A"))
				{
					displayHelp(RTSHelp.REG025A);
				}
				else if (lsScreen.toUpperCase().equals("REG025B"))
				{
					displayHelp(RTSHelp.REG025B);
				}

				else if (lsScreen.toUpperCase().equals("REG029A"))
				{
					displayHelp(RTSHelp.REG029A);
				}
				else if (lsScreen.toUpperCase().equals("REG029B"))
				{
					displayHelp(RTSHelp.REG029B);
				}
				else if (lsScreen.toUpperCase().equals("REG029C"))
				{
					displayHelp(RTSHelp.REG029C);
				}
				else if (lsScreen.toUpperCase().equals("REG029D"))
				{
					displayHelp(RTSHelp.REG029D);
				}
				else if (lsScreen.toUpperCase().equals("REG029E"))
				{
					displayHelp(RTSHelp.REG029E);
				}
				else if (lsScreen.toUpperCase().equals("REG029F"))
				{
					displayHelp(RTSHelp.REG029F);
				}
				else if (lsScreen.toUpperCase().equals("REG033A"))
				{
					displayHelp(RTSHelp.REG033A);
				}
				else if (lsScreen.toUpperCase().equals("REG033B"))
				{
					displayHelp(RTSHelp.REG033B);
				}
				else if (lsScreen.toUpperCase().equals("REG033C"))
				{
					displayHelp(RTSHelp.REG033C);
				}
				else if (lsScreen.toUpperCase().equals("REG033D"))
				{
					displayHelp(RTSHelp.REG033D);
				}
				else if (lsScreen.toUpperCase().equals("REG033E"))
				{
					displayHelp(RTSHelp.REG033E);
				}
				else if (lsScreen.toUpperCase().equals("REG033F"))
				{
					displayHelp(RTSHelp.REG033F);
				}
				else if (lsScreen.toUpperCase().equals("REG033G"))
				{
					displayHelp(RTSHelp.REG033G);
				}
				else if (lsScreen.toUpperCase().equals("REG033H"))
				{
					displayHelp(RTSHelp.REG033H);
				}
				else if (lsScreen.toUpperCase().equals("REG033I"))
				{
					displayHelp(RTSHelp.REG033I);
				}
				else if (lsScreen.toUpperCase().equals("REG033J"))
				{
					displayHelp(RTSHelp.REG033J);
				}
				else if (lsScreen.toUpperCase().equals("REG033K"))
				{
					displayHelp(RTSHelp.REG033K);
				}
				else if (lsScreen.toUpperCase().equals("REG033L"))
				{
					displayHelp(RTSHelp.REG033L);
				}
				else if (lsScreen.toUpperCase().equals("REG033M"))
				{
					displayHelp(RTSHelp.REG033M);
				}
				else if (lsScreen.toUpperCase().equals("REG033N"))
				{
					displayHelp(RTSHelp.REG033N);
				}
				else if (lsScreen.toUpperCase().equals("REG039A"))
				{
					displayHelp(RTSHelp.REG039A);
				}
				else if (lsScreen.toUpperCase().equals("REG039B"))
				{
					displayHelp(RTSHelp.REG039B);
				}
				else if (lsScreen.toUpperCase().equals("REG039C"))
				{
					displayHelp(RTSHelp.REG039C);
				}
				else if (lsScreen.toUpperCase().equals("REG039D"))
				{
					displayHelp(RTSHelp.REG039D);
				}
				else if (lsScreen.toUpperCase().equals("REG039E"))
				{
					displayHelp(RTSHelp.REG039E);
				}
				else if (lsScreen.toUpperCase().equals("REG039F"))
				{
					displayHelp(RTSHelp.REG039F);
				}
				else if (lsScreen.toUpperCase().equals("REG039G"))
				{
					displayHelp(RTSHelp.REG039G);
				}
				else if (lsScreen.toUpperCase().equals("REG039H"))
				{
					displayHelp(RTSHelp.REG039H);
				}
				else if (lsScreen.toUpperCase().equals("REG039I"))
				{
					displayHelp(RTSHelp.REG039I);
				}
				else if (lsScreen.toUpperCase().equals("REG039J"))
				{
					displayHelp(RTSHelp.REG039J);
				}
				else if (lsScreen.toUpperCase().equals("REG039K"))
				{
					displayHelp(RTSHelp.REG039K);
				}
				else if (lsScreen.toUpperCase().equals("REG039L"))
				{
					displayHelp(RTSHelp.REG039L);
				}
				else if (lsScreen.toUpperCase().equals("REG039M"))
				{
					displayHelp(RTSHelp.REG039M);
				}

				else if (lsScreen.toUpperCase().equals("REG050A"))
				{
					displayHelp(RTSHelp.REG050A);
				}
				else if (lsScreen.toUpperCase().equals("REG050B"))
				{
					displayHelp(RTSHelp.REG050B);
				}
				else if (lsScreen.toUpperCase().equals("REG051"))
				{
					displayHelp(RTSHelp.REG051);
				}
				else if (lsScreen.toUpperCase().equals("REG053"))
				{
					displayHelp(RTSHelp.REG053);
				}
				// end defect 8177
				else if (lsScreen.toUpperCase().equals("REG101"))
				{
					displayHelp(RTSHelp.REG101);
				}
				else if (lsScreen.toUpperCase().equals("REG102"))
				{
					displayHelp(RTSHelp.REG102);
				}
				else if (lsScreen.toUpperCase().equals("REG103"))
				{
					displayHelp(RTSHelp.REG103);
				}
				else if (lsScreen.toUpperCase().equals("REG104"))
				{
					displayHelp(RTSHelp.REG104);
				}
				else if (lsScreen.toUpperCase().equals("REG105"))
				{
					displayHelp(RTSHelp.REG105);
				}
				else if (lsScreen.toUpperCase().equals("REG106"))
				{
					displayHelp(RTSHelp.REG106);
				}
				else if (lsScreen.toUpperCase().equals("RPR000"))
				{
					displayHelp(RTSHelp.RPR000);
				}
				else if (lsScreen.toUpperCase().equals("RPR001"))
				{
					displayHelp(RTSHelp.RPR001);
				}
				else if (lsScreen.toUpperCase().equals("RPR002"))
				{
					displayHelp(RTSHelp.RPR002);
				}
				else if (lsScreen.toUpperCase().equals("RPR003"))
				{
					displayHelp(RTSHelp.RPR003);
				}
				// defect 8900
				else if (lsScreen.toUpperCase().equals("RPR005"))
				{
					displayHelp(RTSHelp.RPR005);
				}
				// end defect 8900
				// defect 7570
				else if (lsScreen.toUpperCase().equals("RSP001"))
				{
					displayHelp(RTSHelp.RSP001);
				}
				// end defect 7570
				else if (lsScreen.toUpperCase().equals("SEC001"))
				{
					displayHelp(RTSHelp.SEC001);
				}
				else if (lsScreen.toUpperCase().equals("SEC002"))
				{
					displayHelp(RTSHelp.SEC002);
				}
				else if (lsScreen.toUpperCase().equals("SEC005"))
				{
					displayHelp(RTSHelp.SEC005);
				}
				else if (lsScreen.toUpperCase().equals("SEC006"))
				{
					displayHelp(RTSHelp.SEC006);
				}
				else if (lsScreen.toUpperCase().equals("SEC007"))
				{
					displayHelp(RTSHelp.SEC007);
				}
				else if (lsScreen.toUpperCase().equals("SEC008"))
				{
					displayHelp(RTSHelp.SEC008);
				}
				else if (lsScreen.toUpperCase().equals("SEC009"))
				{
					displayHelp(RTSHelp.SEC009);
				}
				else if (lsScreen.toUpperCase().equals("SEC010"))
				{
					displayHelp(RTSHelp.SEC010);
				}
				else if (lsScreen.toUpperCase().equals("SEC011"))
				{
					displayHelp(RTSHelp.SEC011);
				}
				else if (lsScreen.toUpperCase().equals("SEC012"))
				{
					displayHelp(RTSHelp.SEC012);
				}
				else if (lsScreen.toUpperCase().equals("SEC013"))
				{
					displayHelp(RTSHelp.SEC013);
				}
				else if (lsScreen.toUpperCase().equals("SEC014"))
				{
					displayHelp(RTSHelp.SEC014);
				}
				else if (lsScreen.toUpperCase().equals("SEC015"))
				{
					displayHelp(RTSHelp.SEC015);
				}
				else if (lsScreen.toUpperCase().equals("SEC016"))
				{
					displayHelp(RTSHelp.SEC016);
				}
				else if (lsScreen.toUpperCase().equals("SEC017"))
				{
					displayHelp(RTSHelp.SEC017);
				}
				// defect 9124
				else if (lsScreen.toUpperCase().equals("SEC018"))
				{
					displayHelp(RTSHelp.SEC018);
				}
				// end defect 9124
				// defect 9085 
				else if (lsScreen.toUpperCase().equals("SPL001"))
				{
					displayHelp(RTSHelp.SPL001);
				}
				else if (lsScreen.toUpperCase().equals("SPL002A"))
				{
					displayHelp(RTSHelp.SPL002A);
				}
				else if (lsScreen.toUpperCase().equals("SPL002B"))
				{
					displayHelp(RTSHelp.SPL002B);
				}
				else if (lsScreen.toUpperCase().equals("SPL002C"))
				{
					displayHelp(RTSHelp.SPL002C);
				}
				else if (lsScreen.toUpperCase().equals("SPL002D"))
				{
					displayHelp(RTSHelp.SPL002D);
				}
				else if (lsScreen.toUpperCase().equals("SPL002E"))
				{
					displayHelp(RTSHelp.SPL002E);
				}
				else if (lsScreen.toUpperCase().equals("SPL002F"))
				{
					displayHelp(RTSHelp.SPL002F);
				}
				else if (lsScreen.toUpperCase().equals("SPL002G"))
				{
					displayHelp(RTSHelp.SPL002G);
				}
				else if (lsScreen.toUpperCase().equals("SPL002H"))
				{
					displayHelp(RTSHelp.SPL002H);
				}
				else if (lsScreen.toUpperCase().equals("SPL002I"))
				{
					displayHelp(RTSHelp.SPL002I);
				}
				else if (lsScreen.toUpperCase().equals("SPL002J"))
				{
					displayHelp(RTSHelp.SPL002J);
				}
				else if (lsScreen.toUpperCase().equals("SPL003"))
				{
					displayHelp(RTSHelp.SPL003);
				}
				// end defect 9085 
				else if (lsScreen.toUpperCase().equals("TTL001"))
				{
					displayHelp(RTSHelp.TTL001);
				}
				else if (lsScreen.toUpperCase().equals("TTL002A"))
				{
					displayHelp(RTSHelp.TTL002A);
				}
				else if (lsScreen.toUpperCase().equals("TTL002B"))
				{
					displayHelp(RTSHelp.TTL002B);
				}
				else if (lsScreen.toUpperCase().equals("TTL002C"))
				{
					displayHelp(RTSHelp.TTL002C);
				}
				// defect 8177
				else if (lsScreen.toUpperCase().equals("TTL002D"))
				{
					displayHelp(RTSHelp.TTL002D);
				}
				else if (lsScreen.toUpperCase().equals("TTL003A"))
				{
					displayHelp(RTSHelp.TTL003A);
				}
				else if (lsScreen.toUpperCase().equals("TTL003B"))
				{
					displayHelp(RTSHelp.TTL003B);
				}
				else if (lsScreen.toUpperCase().equals("TTL003C"))
				{
					displayHelp(RTSHelp.TTL003C);
				}
				else if (lsScreen.toUpperCase().equals("TTL003D"))
				{
					displayHelp(RTSHelp.TTL003D);
				}
				else if (lsScreen.toUpperCase().equals("TTL004A"))
				{
					displayHelp(RTSHelp.TTL004A);
				}
				else if (lsScreen.toUpperCase().equals("TTL004B"))
				{
					displayHelp(RTSHelp.TTL004B);
				}
				else if (lsScreen.toUpperCase().equals("TTL004C"))
				{
					displayHelp(RTSHelp.TTL004C);
				}
				else if (lsScreen.toUpperCase().equals("TTL004D"))
				{
					displayHelp(RTSHelp.TTL004D);
				}
				else if (lsScreen.toUpperCase().equals("TTL006"))
				{
					displayHelp(RTSHelp.TTL006);
				}
				else if (lsScreen.toUpperCase().equals("TTL007A"))
				{
					displayHelp(RTSHelp.TTL007A);
				}
				else if (lsScreen.toUpperCase().equals("TTL007B"))
				{
					displayHelp(RTSHelp.TTL007B);
				}
				else if (lsScreen.toUpperCase().equals("TTL007C"))
				{
					displayHelp(RTSHelp.TTL007C);
				}
				else if (lsScreen.toUpperCase().equals("TTL007D"))
				{
					displayHelp(RTSHelp.TTL007D);
				}
				else if (lsScreen.toUpperCase().equals("TTL008A"))
				{
					displayHelp(RTSHelp.TTL008A);
				}
				else if (lsScreen.toUpperCase().equals("TTL008B"))
				{
					displayHelp(RTSHelp.TTL008B);
				}
				else if (lsScreen.toUpperCase().equals("TTL008C"))
				{
					displayHelp(RTSHelp.TTL008C);
				}
				else if (lsScreen.toUpperCase().equals("TTL008D"))
				{
					displayHelp(RTSHelp.TTL008D);
				}
				else if (lsScreen.toUpperCase().equals("TTL011A"))
				{
					displayHelp(RTSHelp.TTL011A);
				}
				else if (lsScreen.toUpperCase().equals("TTL011B"))
				{
					displayHelp(RTSHelp.TTL011B);
				}
				else if (lsScreen.toUpperCase().equals("TTL011C"))
				{
					displayHelp(RTSHelp.TTL011C);
				}
				else if (lsScreen.toUpperCase().equals("TTL011D"))
				{
					displayHelp(RTSHelp.TTL011D);
				}
				else if (lsScreen.toUpperCase().equals("TTL012A"))
				{
					displayHelp(RTSHelp.TTL012A);
				}
				else if (lsScreen.toUpperCase().equals("TTL012B"))
				{
					displayHelp(RTSHelp.TTL012B);
				}
				else if (lsScreen.toUpperCase().equals("TTL012C"))
				{
					displayHelp(RTSHelp.TTL012C);
				}
				else if (lsScreen.toUpperCase().equals("TTL012D"))
				{
					displayHelp(RTSHelp.TTL012D);
				}
				else if (lsScreen.toUpperCase().equals("TTL013"))
				{
					displayHelp(RTSHelp.TTL013);
				}
				else if (lsScreen.toUpperCase().equals("TTL015"))
				{
					displayHelp(RTSHelp.TTL015);
				}
				else if (lsScreen.toUpperCase().equals("TTL016"))
				{
					displayHelp(RTSHelp.TTL016);
				}
				else if (lsScreen.toUpperCase().equals("TTL018"))
				{
					displayHelp(RTSHelp.TTL018);
				}
				else if (lsScreen.toUpperCase().equals("TTL019"))
				{
					displayHelp(RTSHelp.TTL019);
				}
				else if (lsScreen.toUpperCase().equals("TTL028"))
				{
					displayHelp(RTSHelp.TTL028);
				}
				else if (lsScreen.toUpperCase().equals("TTL029"))
				{
					displayHelp(RTSHelp.TTL029);
				}
				else if (lsScreen.toUpperCase().equals("TTL030"))
				{
					displayHelp(RTSHelp.TTL030);
				}
				else if (lsScreen.toUpperCase().equals("TTL033"))
				{
					displayHelp(RTSHelp.TTL033);
				}
				else if (lsScreen.toUpperCase().equals("TTL035A"))
				{
					displayHelp(RTSHelp.TTL035A);
				}
				else if (lsScreen.toUpperCase().equals("TTL035B"))
				{
					displayHelp(RTSHelp.TTL035B);
				}
				else if (lsScreen.toUpperCase().equals("TTL035C"))
				{
					displayHelp(RTSHelp.TTL035C);
				}
				else if (lsScreen.toUpperCase().equals("TTL035D"))
				{
					displayHelp(RTSHelp.TTL035D);
				}
				// end defect 8177
				else if (lsScreen.toUpperCase().equals("TTL037"))
				{
					displayHelp(RTSHelp.TTL037);
				}
				else if (lsScreen.toUpperCase().equals("TTL040"))
				{
					displayHelp(RTSHelp.TTL040);
				}
				else if (lsScreen.toUpperCase().equals("TTL042"))
				{
					displayHelp(RTSHelp.TTL042);
				}
				else if (lsScreen.toUpperCase().equals("TTL044"))
				{
					displayHelp(RTSHelp.TTL044);
				}
				// defect 8926
				else if (lsScreen.toUpperCase().equals("TTL045"))
				{
					displayHelp(RTSHelp.TTL045);
				}
				// end defect 8926
				else if (lsScreen.toUpperCase().equals("VOI001"))
				{
					displayHelp(RTSHelp.VOI001);
				}
				else if (lsScreen.toUpperCase().equals("VOI002"))
				{
					displayHelp(RTSHelp.VOI002);
				}
				else if (
					lsScreen.toUpperCase().equals(
						"PENDING_TRANSACTIONS"))
				{
					displayHelp(RTSHelp.PENDING_TRANSACTIONS);
				}
				else if (lsScreen.toUpperCase().equals("ERR001"))
				{
					displayHelp(RTSHelp.ERR001);
				}
			}

		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
		}
	}
	
	/**
	 * This method releases the browser.
	 * 
	 * @deprecated
	 */
	public static void releaseBrowser()
	{
		if (getBrowser() != null)
		{
			getBrowser().destroy();
		}
	}
	
	/**
	 *  Method to set the handle to the browser.
	 * 
	 * @param aaProcess Process
	 **/
	private static synchronized void setBrowser(Process aaProcess)
	{
		caBrowser = aaProcess;
	}
}
