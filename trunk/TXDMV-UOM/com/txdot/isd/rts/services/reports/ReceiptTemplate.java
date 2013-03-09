package com.txdot.isd.rts.services.reports;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.pdf417.PDF417;

/*
 * ReceiptTemplate.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff Rue		10/31/2001	New Class
 * Jeff Rue		10/31/2001	Added Comments
 * R Duggirala	12/03/2001  Integrated multiple versions of  
 * 							ReceiptTemplate
 * R Duggirala	12/03/2001  Deleted Common Methods Class and moved 
 * 							methods to this template 
 * Jeff Rue		02/08/2002  Add method determinePltNo, determineStkrNo
 * R Taylor &	04/26/2002  Changed methods printOwnerInfo, and 
 * 	Bob Brown				parseOwnrPermitInfo for adding Entry point 
 * 							info for non-resident-ag permit.
 *                          defect 3657
 * J Kwik		04/26/2002  Fix related to PLP annual plates
 *							in printTemplateA4().
 *							defect 3669 
 * S Govindappa 04/26/2002  Changed determineAndPrintHeading() to 
 * 							make receipts print from Tray2
 * 							defect 3464 
 * S Govindappa 04/29/2002  Removed the fix for defect 3464
 * S Govindappa 04/29/2002  Added the fix back for defect 3464
 * Ray Rowehl	04/30/2002	Partial fix for defect 3712
 * J Kwik &    	04/30/2002  Corrected parseOwnrPermitInfo method  to 
 * Bob Brown				print Entry Point info.  Only for   
 *                  		getTransCode().equals(TransCdConstant.NROPT)
 * 							defect 3720.
 * R Taylor &  	05/01/2002  Added getTransCode().equals(TransCdConstant. 
 * Bob Brown				.NRIPT) in parseOwnrPermitInfo method to 
 * 							print customer name and address text for 
 * 							Non-resident ag texas permit 
 *             				defect 3657. (related to 3720.)
 * Ray Rowehl	05/03/2002	Removed fix for defect 3712
 * J Rue		05/21/2002	Misspelling, csPrevPltNoText = 
 * 							"PREVIUOS PLATE NO: to 
 * 							csPrevPltNoText = "PREVIOUS PLATE NO:
 * 							defect 4023 
 * J Rue		05/29/2002	Use current date if VehInqry or Accounting 
 * 							to retrieve RegClassCdDesc from Common Fees
 * 							cache table. 
 * 							method getCacheRegClassDesc(int,int,String)
 * 							Added Title and Registration to list of 
 * 							TransCd  to use current date. 
 * 							this follows RTS I code.
 * 							defect 4084 
 * J Kwik & 	05/29/2002  Added checks for owner supplied exp in 
 * 	T Pederson				buildExpirationDt and printTemplateA4
 * 							defect 4099 
 * J Rue		06/14/2002	Defect 4288, Added code to parseOwnrInfo() 
 * 							to not print Recipient Address if trans is 
 * 							TempAddlWt
 * Ray Rowehl & 07/10/2002	Merging PCR5 code, Added 
 * S Govindappa				formatCreditCardFeeMessage() method for 
 * 							Credit Card Processing.
 * MAbs			09/17/2002	PCR 41 Integration
 * J Rue & 		09/9/2002	Defect 4699, Update method 
 *  B Arredondo				buildExpirationDt(Object) and getForm31No
 * 							(Object) where it will search for inventory
 * 							issued reguardless of order. 
 * J Rue & 		10/14/2002  and capture the MFVehRegExpYr if Form31 
 * 	B Arredondo				issued and no Stkr/Plt issued
 * B Brown      01/21/2003  Changed method generateCityCntry to make 
 * 							sure no comma is printed when recipient 
 * 							info address is not changed, and changed 
 * 							method parseOwnerInfo to make sure recipient
 * 							address gets picked up on receipt when its 
 * 							changed - note code change notes below.
 * 							defect 5184
 * B Brown 		01/21/2003 	Defect 5184. Changed to make sure
 * 							recipient addr gets picked up on receipt
 * 							when its changed note code change notes
 * 							below.
 * B Brown      02/27/2003  Defect 5184. Added the return "" if
 * 							(lsCityStateCntry.equals("")) to remove
 * 							comma when recipient info address is not
 * 							changed  
 * Min Wang		03/03/2003	Modified generateCityCntry(). 
 * 							defect 5643.
 * K Harrell    04/27/2003  For HQ 291, print County Name, TAC Name on 
 * 							Registration & Renewal receipts if not 
 * 							exempt
 *                          modify printCntyOffice()
 * 							defect 6022. 
 * Ray Rowehl	05/02/2003	Do not attempt to use Renewal Address 
 * 							Information	to populate Owner address if it
 * 							is null. This happened with Driver's Ed 
 * 							Plates.
 *							modify parseOwnerInfo()
 *							defect 6065
 * Ray Rowehl	05/08/2003	change title receipts to use dummy TtlNoMf 
 * 							for docno and transid for special cases.
 *							modify printTemplateA4() &printCntyOffice()
 *							add createDummyDocNo()
 *							defect 6082
 * Ray Rowehl	05/09/2003	Change so Vehinq can handle Canceled Plates
 *							modify printTemplateA4() & printRegExpDate()
 *							defect 6068
 * Ray Rowehl	05/20/2003	Modify so Special Plates Title will also 
 * 							use the appropriate BarCode.
 *							modify generateReceiptHeader()
 *							defect 6082
 * Ray Rowehl	07/12/2003  Modify so expiration date set in REG029 
 * 							takes precedence.
 *							modify printTemplateA4()
 *							defect 6319
 * B Hargrove   07/21/2003  Changed to use Owner Address for Recipient 
 * 							Address if the Recipient Name is entered 
 * 							but no Recipient Address is entered.
 * 							modify parseOwnrInfo()
 *                          defect 6121. Ver 5.1.4.   
 * B Hargrove   07/31/2003  Fix problem with Temp Addl Wt receipt.
 *							see markers for 6121.
 *							modify parseOwnrInfo().
 *                          defect 6429. Ver 5.1.4. 
 * K Harrell	12/29/2003	Restructure code for owner/renewal 
 * 							recipient.
 *							modify parseOwnrInfo()
 *							defect 6540 Ver 5.1.5 Fix 2
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							add cbShouldStickersPrint,
 *							csSubconRenewalLocText,
 *							csInvItmTxt
 *							add generateStickers(),
 *							maskVIN() 
 *							modify buildDataObjects(), 
 *							determineAndPrintHeading(),
 *							printStickerNo()
 * 							Ver 5.2.0
 * Jeff S.		05/12/2004	Sticker layout Changed. Removed *'s from the
 *							vin Mask.  Added class variables that handle
 *							the positioning of each item in both the 
 *							plate and Windshield stickers.  If a 
 *							position, font, stroke, or justification 
 *							needs changing they can be changed below.  
 *							Modified generateStickers() to use the new 
 *							PCLUtilities class variable instead of the
 *							hard coded PCL int he Print class.
 *							modify maskVIN(String)
 *							generateStickers(CompleteTransactionData)
 *							defect 7079 Ver 5.2.0
 * Jeff S.		05/27/2004	Barcode format change on WS/US stickers 
 *							(output). Made a few changes to make it 
 *							easier to add Item codes in the future.
 *							Most of the changed class below was from
 *							PCLUtilities and was moved to clean things.
 *							add dataToBarcode()
 *							modify dataToBarcode()
 *							modify generateStickers()
 *							defect 7107 Ver 5.2.0
 * Jeff S.		05/27/2004	When there was a customer supplied plate the
 *							wrong plate number was ont he sticker and in
 *							the barcode.
 *							modify generateStickers()
 *							modify dataToBarcode()
 *							defect 7103 Ver 5.2.0
 * Jeff S.		05/28/2004	Change max size from 10 to 13 for the county
 *							name that is printed on the sticker.
 *							modify generateStickers()
 *							defect 7130 Ver 5.2.0
 * K Harrell	06/07/2004	Match barcode to VTR format request
 *							modify dataToBarcode()
 *							add determineDocNo() 
 *							add toConsole()
 *							defect 7150 Ver 5.2.0
 * Jeff S.		06/08/2004	VTR format request was altered to remove
 *							the item codes/fees from the barcode.  Also
 *							change the date format from MMDDYYY to
 *							YYYYMMDD for both print & expiration dates.
 *							County Name printed on the sticker should be
 *							the ResCompCnty of the customer.
 *							add determineResCompCntyNo()
 *							modify generateStickers()
 *							modify dataToBarcode()
 *							delete NUM_ITEM_CODES
 *							defect 7151 Ver 5.2.0
 * Jeff S.		06/09/2004	County Name printed on the sticker should be
 *							the ResCompCnty of the customer.
 *							add determineResCompCntyName()
 *							modify generateStickers()
 *							defect 7113 Ver 5.2.0
 * Jeff S.		06/23/2004	HQ's was not printing the correct Exp Year
 *							going to inventory to get the year just like
 *							the barcode was doing it.
 *							modify generateStickers()
 *							defect 7233 Ver 5.2.0
 * Jeff S.		06/28/2004	Made minor adjustments to the plate sticker
 *							allignment.  Moved all items down 35 pxls.
 *							Moved VIN and Plate 5 to the left.
 *							defect 7241 Ver 5.2.0
 * J Rue		08/16/2004	Print 'TITLE APPLICATION RECEIPT' on
 *							DTA titles.
 *							new determineAndPrintHeading(String, boolean)
 *							defect 7436 Ver 5.2.1
 * B Hargrove   08/17/2004  Changes to Subcontractor Receipt, which is 
 * 							now printing as of Ver 5.2.1. 
 *							Add Class vars csSbrnwAddrText,
 *							csTransCdSbrnw, RENEWALSUBCONRECEIPTSTRING
 * 							modify determineAndPrintHeading(String),
 *							parseOwnrInfo(), regisTransCd()
 *                          defect 7450 Ver 5.2.1.   
 * J Rue		08/25/2004	DealerTitleData.NewRegExpMo() and 
 *							DealerTitleData.NewRegExpYr() were converted 
 *							to integer.
 *							method dataToBarcode(), generateStickers()
 *							defect 7496 Ver 5.2.1
 * J Zwiener	02/01/2005	Correct Tow Truck plt not printing on stkr
 *							when "currently registered" selected (new
 *							small tow truck plt not issued).
 *							modify generateStickers()
 *							defect 7942 Ver 5.2.2
 * Jeff S.		05/10/2005	Used PCL to change the font of the address
 * 							information so that the post office can
 * 							read the address.  Also move the regis desc
 * 							out of the mailer window.
 * 							add RECEIPT_FONT, OWNER_DATA_FONT,
 * 								ciHeaderGrpRegisDesc
 * 							modify printOwnerInfo(), ciHeaderOwnr2
 * 								printRegisDesc()
 * 							defect 8193 Ver. 5.2.2 Fix 3
 * Ray Rowehl	02/08/2005	Change package reference for Transaction
 * 							modify createDummyDocNo()
 * 							defect 7705 Ver 5.2.3
 * Jeff S.		04/04/2005	Cleanup of the print class cause static 
 * 							calls to the print class to change.
 * 							defect 7562 Ver. 5.2.3
 * K Harrell	05/12/2005	Use cbShouldStickersPrint vs. lbChangeFont 
 *  J Rue 					to determine if should modify font & change
 *							position of regis desc.
 *							Further Java 1.4 work
 *							renamed lbPermit throughout to lbTransCd
 *							add printStickerNo()
 *							deleted printTowTrkStkrNo()
 *							deprecate printStickerNo(asStickerNo)
 *							modify printOwnerInfo(), printRegisDesc()
 *							defect 7562,8193 Ver 5.2.2 Fix 3
 * K Harrell	05/22/2005	Java 1.4
 * 							deprecate formatSystemTime(),generateTransId(),
 * 							getCurrentDate(),getExpirationDate(),
 * 							getNoOfDetailLines,addlSalesTax()
 * 							defect 7896 Ver 5.2.3  
 * S Johnston	06/14/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							add getSTICKER_SETTINGS()
 * 							modify determineAndPrintHeading(String),
 * 							determineAndPrintHeading(String, boolean)
 *							defect 7896 Ver 5.2.3
 * J Zwiener	07/17/2005  Enhancement for Disable Placard event
 *							modify class variables
 *							modify isDisablePlacardTransCd()
 *							modify printTemplateA3()
 *							modify caRpt.print()
 * 							defect 8268 Ver 5.2.2 Fix 6
 * J Zwiener	12/08/2005  Stickers not going to Tray 3.
 * 							Change getSTICKER_SETTINGS() to the
 * 							getPRINT_TRAY_STICKER().
 * 							modify determineAndPrintHeading(String),
 * 							determineAndPrintHeading(String, boolean)
 * 							defect 7896 Ver 5.2.3
 * Jeff S.		06/28/2006	Loaded all of the sticker format variables
 * 							into a hash table that is keyed off of the
 * 							element name + version.  This will allow the
 * 							same code that generates the sticker to work
 * 							for different versions.
 * 							add BARCODE, CNTY, EXP, NEW_LINE, PLT, 
 * 								VERSION1, VERSION2, VIN, VOID 
 * 							add loadStickerFormatValues()
 * 							modify generateStickers()
 * 							defect 8829 Ver. 5.2.4
 * John R.		08/03/2006	Verbiage change per VTR
 * 							add NOUSE
 * 							delete NOSEUSE	
 * 							defect 8829 Ver. 5.2.4
 * Jeff S.		08/08/2006	Load the sticker format values only once per
 * 							JVM using a static call.
 * 							modify All ReceiptTemplate() constructors
 * 							defect 8829 Ver. 5.2.4
 * Jeff S.		08/14/2006	Removed the NEW_LINE between each field on 
 * 							the sticker.  This was done b/c it is 
 * 							possible to force the items off the page
 * 							because of two many lines.
 * 							modify generateStickers() 
 * 							defect 8829 ver. 5.2.4
 * Min Wang		10/11/2006	New Requirement for handling plate age 
 * 							modify parseRegisInfo()
 *							defect 8901 Ver Exempts
 * K Harrell	10/25/2006	Replace w/ " " Exp Mo/Yr of "0" 
 * 							Also, remove deprecated methods
 * 							delete ssRunDateFormated,ssRunTimeFormated ,
 * 							 POUNDSIGN 
 *							delete addlSalesTax(),getExpirationDate(),
 *							 formatSystemTime(),getCurrentDate(),
 *							 generateTransId(),printStickerNo(),
 *							 determineStkrNo(),getExpirationDate()
 *							 getNoOfDetailLines(),printExpDate()
 *							modify printPrevExpMoYr(),printRegExpDate()
 *							defect 8900 Ver Exempts
 * K Harrell	11/01/2006	Pass dates of "0/0" for Expiration Date  
 * 							when Standard Exempt.
 * 			 				modify printTemplateA4()
 * 							defect 8900 Ver Exempts 
 * K Harrell	02/11/2007	Beginning work on adding Organization to 
 * 							Receipt. 
 * 							add ORGANIZATION_TEXT
 * 							modify parseRegsDescVehLoc()
 * 							defect 9085 Ver Special Plates 
 * B Brown		02/20/2007	Fix the temp addl weight receipt sticker 
 * 							type line
 * 							modify mergeOwnrRegisInfo()
 * 							defect 9085 Ver Special Plates 
 * B Hargrove	03/23/2007	Set up Special Plate Application receipt. 
 * 							modify parseOwnrInfo(),
 * 							printFirst3HeaderLines(), printTemplateA5()
 * 							defect 9126 Ver Special Plates 
 * Jeff S.		03/30/2007	Made changes to allow the printing of a
 * 							plate sticker for special plate dealer.
 * 							Also allow Special Plate renewal.
 * 							modify generateStickers()
 * 							defect 9145 Ver Special Plates
 * Jeff S.		04/06/2007	Adding manufacturing notation method.
 * 							add printManufacturingNotation()
 * 							defect 9145 Ver Special Plates
 * Jeff S.		04/10/2007	Add handling of Veh. Inquiry of a Special
 * 							Plate.
 * 							modify buildDataObjects(), 
 * 								generateReceiptHeader(), 
 * 								printFirst3HeaderLines(), 
 * 								printTemplateA5()
 * 							defect 9145 Ver Special Plates
 * K Harrell	03/26/2008	Null Pointer exception when writing address
 * 							for Cancelled Plates 
 * 							modify parseOwnerInfo()
 * 							defect 9114 Ver Defect POS A
 * K Harrell	04/25/2008	If Cancelled Plate, print Cancelled Plate 
 * 							Doc No on Receipt
 * 							modify printTemplateA4()
 * 							defect 9641 Ver Tres Amigos PH A   
 * B Hargrove	07/11/2008	If Vendor Plate, print the Plate Expiration  
 * 							instead of the blank line after Trans ID.
 * 							Do not print Vendor Plate expiration for 
 * 							Temp Addl Weight Permit.  
 * 							modify printTemplateA4()
 * 							defect 9689 Ver Defect MyPlates_POS
 * K Harrell	08/27/2008	Prevent Null Pointer Exception when no 
 * 							indicators in DB Server Down. 
 * 							modify buildNotationsVector()
 * 							defect 7003 Ver Defect_POS_B 
 * B Brown		09/25/2008  Make sure sticker type shows on IRENEW 
 * 							receipts
 * 							modify parseRegsDescVehLoc() 
 * 							defect 9673 Ver Defect_POS_B
 * K Harrell	10/27/2008	Modify for Disabled Placard 
 * 							add PLACARD, INSTITUTION_NAME_ADDR,
 * 							 DSABLD_PLCRD_NAME_ADDR 
 * 							delete TRANSCD_BPM, *_BTM, *_RPNM, *_RTNM,
 * 							 CARD 
 *  						modify  isDisablePlacardTransCd(), 
 * 							 printTemplateA3(), parseOwnrPermitInfo() 
 * 							defect 9831 Ver Defect_POS_B    
 * B Hargrove	12/31/2008  Add 'EXCH' so appropriate cnty is printed 
 * 							for 291. Only print special DocNo for Title 
 * 							events.
 * 							modify printCntyOffice(), printTemplateA4()    
 * 							defect 9098 Ver Defect_POS_D    
 * K Harrell	01/07/2009	Modified in refactor of SpclPltRegisData 
 * 							RegExpMo/Yr methods to PltExpMo/Yr methods.
 * 							modify generateStickers(), printTemplateA4()  
 * 							defect 9864 Ver Defect_POS_D 
 * K Harrell	01/08/2009	Remove reference to RegisData.getOrgNo()
 * 							refactor laRegClassCd to laRegisData  
 * 							modify parseRegsDescVehLoc() 
 * 							defect 9912 Ver Defect_POS_D  
 * K Harrell	03/06/2009	Add Prev Doc No for DTA/Title Type 
 * 							Transactions.
 * 							add PREV_DOC_NO_TEXT, E_TITLE_PRT_CD,
 * 							 PRINT_TITLE_PRT_CD
 * 							modify printTemplateA4()
 * 							defect 9978 Ver Defect_POS_E  
 * K Harrell	07/19/2009	add VIN_ID_NO
 * 							delete generateCityCntry(), 
 * 							 generateZipCdP4(), csVINText
 * 							modify parseOwnrInfo(),parseOwnrPermitInfo(),
 * 							 parseRegsDescVehLoc()   
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/05/2009	Remove unused constants
 * 							OTHERSALESTAXPAIDTEXT, REFUND_TYPE_CASH_TEXT,
 *							FUND_TYPE_CHK_TEXT, REGCREDITSTRING,
 *							SALESPRICETEXT, SALESTAXCATTXT,
 *							SALESTAXDATETEXT, SALESTAXPAIDTEXT,
 *							STICKER_NO_TEXT,SUBCON_RENEW_LOC_TEXT, 
 *							TAXABLEAMOUNTTEXT, TAXPENALTYTEXT, 
 *							TOTALTAXPAIDTEXT, TOTAMTPAIDTXT, 
 *							TRADEINALLOWTEXT,VEHISSUEDSTRING 
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	09/05/2009	Do not print Renewal Recipient Name if 
 * 							Space(s)
 * 							modify parseOwnrInfo()
 * 							defect 10112 Ver Defect_POS_F  
 * K Harrell	12/15/2009	refactor caCTData from caTransData
 * 							modify dataToBarcode() 
 * 							defect 10290 Ver Defect_POS_H 
 * K Harrell	04/12/2010	add TERM 
 * 							modify PLATE_EXP_DATE_TEXT
 * 							modify printTemplateA4(),
 * 							  printTemplateA5() 
 * 							defect 10449 Ver POS_640
 * K Harrell	05/25/2010	Modify for new PermitData
 * 							delete TRANSCD_72PT, TRANSCD_144PT, 
 * 							 TRANSCD_30PT, TRANSCD_FDPT, TRANSCD_OTPT
 * 							add isPermitTransCd
 * 							delete permitTransCd  
 * 							modify parseOwnrPermitInfo() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	06/20/2010	RegClassCd to 3 digits on Receipt Barcode
  							modify dataToBarcode(), BARCODE_VERSION 
 * 							defect 10464 Ver 6.5.0 
 * K Harrell	06/20/2010	modify determineAndPrintHeading(String) 
 * 							defect 10491 Ver 6.5.0 
 * Min Wang		07/21/2010	Delete the existing Certificate No. on the 
 * 							Tow Truck Receipt.
 * 							delete CERTIF_NO_TEXT, printCertifNo()
 * 							modify printTemplateA6()
 * 							defect 10007 Ver 6.5.0
 * K Harrell	07/19/2010	add APPL_ADDR_TEXT 
 * 							modify determineAndPrintHeading(String), 
 * 							 isPermitTransCd() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	02/02/2011	delete TRANSCD_SBRNW
 * 							modify determineResCompCntyNo(), 
 * 							 generateReceiptHeader(), generateStickers(),
 * 							 isRegisTransCd(), parseOwnrInfo(), 
 * 							 printTemplateA4()
 * 							defect 10745 Ver 6.7.0
 * K Harrell	04/26/2011	modify printSystemDate(), printSystemTime()
 * 							defect 10745 Ver 6.7.1
 * K Harrell	06/02/2011	modify isPermitTransCd()
 * 							defect 10844 Ver 6.8.0  
 * B Hargrove	09/30/2011  We need to set/use 'new RegEffDt'.  
 *        					modify printTemplateA5() 
 *        					defect 10948 Ver 6.9.0
 * K Harrell	10/10/2011	add printDisabledPlacard() 
 * 							modify printTemplateA3(), printOwnerInfo()  
 * 							defect 11050 Ver 6.9.0 
 * K Harrell	11/04/2011  Modify for WebAgent Owner Info 
 * 							modify parseOwnrInfo()
 * 							defect 11137 Ver 6.9.0  
 * K Harrell	11/05/2011 	Modify for WebAgent Token Trailer Receipt
 * 							All Sticker Versions are now "2" 
 * 							delete caVehData, getStkrVersion() 
 * 							modify generateStickers(),buildDataObjects(),
 * 							 loadStickerFormatValues()
 * 							defect 11138 Ver 6.9.0 
 * K Harrell	11/12/2011	Modify to print over 5 placards. 
 * 							modify printTemplateA3(),printDisabledPlacard() 
 * 							defect 11050 Ver 6.9.0 
 * K Harrell	11/14/2011	temp change for VTR275
 * 							defect 11052 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */

/**
 * The ReceiptTemplate Class is an abstract class that must be
 * implemented by all classes that intend to generate receipt.
 * 
 * @version	6.9.0				11/14/2011
 * @author 	Rakesh Duggirala
 * <br>Creation Date:			10/31/2001 
 */
public class ReceiptTemplate
{
	public static final int AMT_LNGTH_13 = 13;

	public static final String AUTOMATION_1_STRING =
		"Current law requires an additional $1.00 fee (already";
	public static final String AUTOMATION_2_STRING =
		"included) in counties with 50,000 or more vehicles.";

	private static final String BARCODE = "BARCODE";
	// Barcode Type
	private static final String BARCODE_TYPE = "X";

	// Barcode Version
	private static final String BARCODE_VERSION = "02";


	private static final String BDY_VIN_TEXT =
		"BODY VEHICLE IDENTIFICATION NO: ";
	private static final String BLANK_TRCKNG_TYPE = "";
	private static final String BODY_STYLE_TEXT = "BODY STYLE: ";
	private static final String CARD_NO_TEXT = "PLACARD: ";
	private static final String CARRY_CAP_TEXT = "CARRYING CAPACITY: ";
	protected static final String CHARGE = "CHARGE";
	protected static final String CHECK = "CHECK";
	private static final String CNTY = "CNTY";

	public static final int COL_01 = 1;
	public static final int COL_05 = 5;
	public static final int COL_06 = 6;
	public static final int COL_08 = 8;
	public static final int COL_16 = 16;
	public static final int COL_18 = 18;
	public static final int COL_20 = 20;
	public static final int COL_21 = 21;
	public static final int COL_24 = 24;
	public static final int COL_26 = 26;
	public static final int COL_31 = 31;
	public static final int COL_33 = 33;
	public static final int COL_35 = 35;
	public static final int COL_38 = 38;
	public static final int COL_39 = 39;
	public static final int COL_40 = 40;
	public static final int COL_45 = 45;
	public static final int COL_49 = 49;
	public static final int COL_50 = 50;
	public static final int COL_51 = 51;
	public static final int COL_53 = 53;
	public static final int COL_55 = 55;
	public static final int COL_56 = 56;
	public static final int COL_57 = 57;
	public static final int COL_58 = 58;
	public static final int COL_62 = 62;
	public static final int COL_63 = 63;
	public static final int COL_67 = 67;
	public static final int COL_68 = 68;
	public static final int COL_72 = 72;
	public static final int COL_77 = 77;
	public static final int COL_80 = 80;
	public static final int COL_81 = 81;
	public static final int COL_83 = 83;

	protected static final String COMMERCIALVEHICLESTRING =
		"THIS RECEIPT TO BE CARRIED IN ALL COMMERCIAL VEHICLES.";
	private static final String COUNTY_NO_TEXT = "COUNTY NO: ";
	private static final int COUNTY_OFFICE_CD = 3;
	private static final String COUNTY_TEXT = "COUNTY: ";
	private static final String VIN_ID_NO =
		"VEHICLE IDENTIFICATION NO: ";
	private static String APPL_ADDR_TEXT = "APPLICANT NAME AND ADDRESS";
	private static String CUST_ADDR_TEXT = "CUSTOMER NAME AND ADDRESS";
	protected static final String CUST_NAME_TEXT = "CUSTOMER NAME";
	protected static final String DATE_TEXT = "DATE: ";
	private static final String DOC_NO_TEXT = "DOCUMENT NO: ";
	protected static final String DOLLAR_SIGN = "$";
	private static final String DONOTUSE = "DO NOT USE/";
	private static String DSABLD_PLCRD_NAME_ADDR =
		"DISABLED PERSON NAME AND ADDRESS ";
	private static final String E_TITLE_PRT_CD = " E";
	private static final String EFF_DATE_TEXT = "EFFECTIVE DATE: ";
	private static final String EFF_TIME_TEXT = "EFFECTIVE TIME: ";
	private static final String EMP_ID_TEXT = "EMPLOYEE ID: ";
	private static final String EMPTY_WT_TEXT = "EMPTY WT: ";
	private static final String ENTRY_POINT_TEXT = "ENTRY POINT: ";
	private static final String EXP_DATE_TEXT = "EXPIRATION DATE: ";
	private static final String EXP_TIME_TEXT = "EXPIRATION TIME: ";
	protected static final String EXP_YR_TEXT = "EXPIRATION YR:";
	private static final String EXPMO = "EXPMO";
	private static final String EXPYR = "EXPYR";
	protected static final String FEES_ASSESSED_TEXT = "FEES ASSESSED";
	private static final int FIRST_RECORD = 0;
	protected static final String FIRSTLIENTXT = "1ST LIEN";
	private static final String FORM_31_NO_TEXT = "FORM 31 NO: ";
	private static final String GROSS_WT = "GROSS WT: ";
	private static final int HEADER_GRP_REGIS_DESC = 47;
	private static final int HEADER_GRP1 = 5;
	private static final int HEADER_GRP2 = 39; //40;
	private static final int HEADER_GRP3 = 63; //64;

	// COLUMN POSITIONS FOR THE OWNER/RECIPIENT RECEIPT LINES
	private static final int HEADER_OWNR2 = 52;
	private static final int HEADER_OWNR3 = 40;
	private static final String HOT_CK_CRDT_TEXT =
		"HOT CHECK CREDIT(S)";
	private static String INSTITUTION_NAME_ADDR =
		"INSTITUTION NAME AND ADDRESS";
	protected static final String INV_ITM_TEXT = "INVENTORY ITEM(S)";
	protected static final String INV_ITM_YR_TEXT = "YR";
	protected static final String METHOD_OF_PAY_TEXT =
		"METHOD OF PAYMENT AND PAYMENT AMOUNT:";
	protected static final String MFG_REQ_NOTATION =
		"* PLATE MANUFACTURE REQUEST";
	private static final String MODEL_TEXT = "MODEL: ";
	private static final String NEW_LINE = "\n";
	private static final String NOUSE = "NO USE";
	private static final String OFFICE_TEXT = "OFFICE: ";
	private static final String ORGANIZATION_TEXT = "ORGANIZATION: ";
	private static final String OWNER_DATA_FONT = "(s0p14h&l6C";
	protected static String OWNR_ADDR_TEXT = "OWNER NAME AND ADDRESS";
	protected static final String OWNRSHIP_EVID_TEXT =
		"OWNERSHIP EVIDENCE:";
	protected static final String PAYMENTCHANGEDUESTRING = "CHANGE DUE";
	protected static final String PAYMENTSTRING =
		"  METHOD OF PAYMENT AND PAYMENT AMOUNT:";
	protected static final String PAYMENTTOTALSTRING =
		"TOTAL AMOUNT PAID ";
	public static final String PERMIT_NO_TEXT = "PERMIT NO: ";
	// Added variables to keep up with the sticker layout
	// all sticker layout changes need to be done here
	private static final int PL_CENTER = 4650;

	// defect 10449 
	private static final String PLATE_EXP_DATE_TEXT =
		//"PLATE EXPIRATION DATE: ";
	"PLATE EXP DATE: ";
	private static final String TERM = "TERM: ";
	// end defect 10449 

	private static final String PLATE_NO_TEXT = "PLATE NO: ";
	private static final String PLATE_TRCKNG_TYPE = "P";
	protected static final String PLATE_TYPE_TEXT = "PLATE TYPE: ";
	private static final String PLT = "PLT";
	private static final String PLT_AGE_TEXT = "PLATE AGE: ";
	private static final String PREV_CITY_STATE_TEXT =
		"PREV CITY/STATE: ";
	private static final String PREV_DOC_NO_TEXT = "PREV DOC NO: ";
	private static final String PREV_EXP_MO_YR_TEXT =
		"PREVIOUS EXP MO/YR: ";
	private static final String PREV_OWNR_TEXT = "PREV OWNER NAME: ";
	private static final String PREV_PLATE_NO_TEXT =
		"PREVIOUS PLATE NO: ";
	private static final String PRINT_TITLE_PRT_CD = " P";
	private static final String RECEIPT_FONT = "(s0p16h&l4C";
	private static final String REFUND_HD_TEXT = "REFUND CREDIT(S)";
	protected static final String REG_CLASS_TEXT =
		"REGISTRATION CLASS: ";
	private static final String REG_ISSUE_DATE_TEXT =
		"REGISTRATION ISSUE DATE: ";
	private static String RENEW_RCPT_TEXT =
		"RENEWAL RECIPIENT NAME AND ADDRESS";
	private static final String RENEWALSUBCONRECEIPTSTRING =
		"REGISTRATION RENEWAL RECEIPT - SUBCONTRACTOR";
	protected static final String RF_CASH_TEXT = "REFUND TYPE: CASH";
	protected static final String RF_CHECK_TEXT = "REFUND TYPE: CHECK";

	private static String SBRNW_ADDR_TEXT =
		"SUBCONTRACTOR NAME AND ADDRESS";
	private static final int SECOND_RECORD = 1;
	protected static final String SECONDLIENTXT = "2ND LIEN";
	private static final String STATE_REGISTERED_TEXT =
		"STATE REGISTERED: ";
	private static final String STICKER_TRCKNG_TYPE = "S";
	protected static final String STICKER_TYPE_TEXT = "STICKER TYPE: ";
	private static final String TAC_NAME_TEXT = "TAC NAME: ";
	private static final int THIRD_RECORD = 2;
	protected static final String THIRDLIENTXT = "3RD LIEN";
	private static final String TIME_TEXT = "TIME: ";
	private static final String TIRE_TYPE_TEXT = "TIRE TYPE: ";
	private static final String TON_TEXT = "TONNAGE: ";
	protected static final String TOTAL_TEXT = "TOTAL";
	protected static final String TOTALAMTPAIDTEXT =
		"TOTAL AMOUNT PAID";

	private static final String TOW_TRK_PLT_NO_TEXT =
		"TOW TRUCK PLATE NO: ";
	private static final String TRANS_ID_TEXT = "TRANSACTION ID: ";

	// defect 10745 
	private static String WRENEW_ADDR_TEXT =
		"WEBAGENT NAME AND ADDRESS";
	// end defect 10745
	
	// defect 11138 
	// private VehicleData caVehData = new VehicleData();
	// end defect 11138 

	// ***  TRANS CODES  ***
	// MISCELLANOUS

	// Additional Sales
	private static final String TRANSCD_ADLSTX = TransCdConstant.ADLSTX;
	// ACCOUNTING
	//	Hot Check Redeemed
	private static final String TRANSCD_CKREDM = TransCdConstant.CKREDM;
	// Registration Correction/Apprehended Permanent Additional Weight
	private static final String TRANSCD_CORREG = TransCdConstant.CORREG;
	// Corrected Title
	private static final String TRANSCD_CORTTL = TransCdConstant.CORTTL;
	//	Issue Driver Education Plates
	private static final String TRANSCD_DRVED = TransCdConstant.DRVED;
	//	DTA Non-Title/Diskette
	protected static final String TRANSCD_DTANTD = TransCdConstant.DTANTD;
	//	DTA Non-Title/Keyboard
	protected static final String TRANSCD_DTANTK = TransCdConstant.DTANTK;
	//	DTA Original/Diskette
	private static final String TRANSCD_DTAORD = TransCdConstant.DTAORD;
	//	DTA Original/Keyboard
	private static final String TRANSCD_DTAORK = TransCdConstant.DTAORK;
	
	// REGISTRATION
	// Duplicate
	private static final String TRANSCD_DUPL = TransCdConstant.DUPL;
	// Exchange
	private static final String TRANSCD_EXCH = TransCdConstant.EXCH;
	//	Hot Check Credit
	private static final String TRANSCD_HOTCK = TransCdConstant.HOTCK;
	//	Deduct Hot Check Credit
	private static final String TRANSCD_HOTDED = TransCdConstant.HOTDED;
	// Internet Renewal Renewal
	private static final String TRANSCD_IRENEW = TransCdConstant.IRENEW;
	// Non-Title
	protected static final String TRANSCD_NONTTL = TransCdConstant.NONTTL;
	//	Non-Resident Texas Agriculture
	private static final String TRANSCD_NRIPT = TransCdConstant.NRIPT;
	//	Non-Resident Out-Of-State Agriculture (Not Used)
	private static final String TRANSCD_NROPT = TransCdConstant.NROPT;

	//	Voluntary Permanent Additional Weight
	private static final String TRANSCD_PAWT = TransCdConstant.PAWT;
	//	Refund
	protected static final String TRANSCD_REFUND =
		TransCdConstant.REFUND;
	// Correct Title Rejection
	private static final String TRANSCD_REJCOR = TransCdConstant.REJCOR;
	// Renewal
	private static final String TRANSCD_RENEW = TransCdConstant.RENEW;
	// Replacement
	private static final String TRANSCD_REPL = TransCdConstant.REPL;
	protected static final String TRANSCD_RFCASH = TransCdConstant.RFCASH;
	
	// REGIONAL OFFICE COLLECTIONS
	private static final String TRANSCD_RGNCOL = TransCdConstant.RGNCOL;

	// Temporary Additional Weight	
	private static final String TRANSCD_TAWPT = TransCdConstant.TAWPT;
	
	// TITLE
	private static final String TRANSCD_TITLE = TransCdConstant.TITLE;
	//	Tow Truck Plate
	public static final String TRANSCD_TOWP = TransCdConstant.TOWP;
	// VEHICLE INQUIRY
	//private static final String TRANSCD_VEHINQ = TransCdConstant.VEHINQ;
	private static final String TRLR_TYPE_TEXT = "TRAILER TYPE: ";
	private static final String TRVL_TRLR_TEXT = "TRAVEL TRLR LNG/WDTH: ";
	private static final String TTL_VEH_LOC_TEXT = "VEHICLE LOCATION ADDRESS";
	private static final String UNIT_NO_TEXT = "UNIT NO: ";
	private static final String VEH_CLASS_TEXT =
		"VEHICLE CLASSIFICATION: ";
	protected static final String VEH_INFO_TEXT = "VEHICLE INFORMATION";
	protected static final String VEH_NOTATION_TEXT =
		"VEHICLE RECORD NOTATIONS";
	protected static final String VEH_ODO_BRND_TEXT = "BRAND: ";
	protected static final String VEH_ODO_RDNG_TEXT =
		"ODOMETER READING: ";
	private final static String VERSION1 = "1";
	private final static String VERSION2 = "2";
	private final static String VIN = "VIN";
	protected static final String VIN_TEXT = "VIN:";
	private static final String VOID = "VOID";
	private static final String YR_MK_TEXT = "YR/MAKE: ";

	static {
		loadStickerFormatValues();
	}

	/**
	 * getCacheItmTrkngType
	 * 
	 * @param  asItmCd String
	 * @return String
	 */
	public static String getCacheItmTrkngType(String asItmCd)
	{
		ItemCodesData laItmTrkngType = ItemCodesCache.getItmCd(asItmCd);
		if (laItmTrkngType != null)
		{
			return laItmTrkngType.getItmTrckngType();
		}
		else
		{
			return "";
		}
	}

	/**
	 * getCachePlateTypeDesc
	 * 
	 * @param asItmCd String
	 * @return String
	 */
	public static String getCachePlateTypeDesc(String asItmCd)
	{
		ItemCodesData laItmData = ItemCodesCache.getItmCd(asItmCd);
		if (laItmData != null)
		{
			return laItmData.getItmCdDesc();
		}
		else
		{
			return "";
		}
	}

	/**
	 * Get TransCd description from Cache
	 *
	 * @param  asTransCd String
	 * @return String
	 */
	public static String getCacheTransCdDesc(String asTransCd)
	{
		try
		{
			TransactionCodesData laTransCdData =
				TransactionCodesCache.getTransCd(asTransCd);
			if (laTransCdData != null)
			{
				return laTransCdData.getTransCdDesc();
			}
			else
			{
				return "UNKNOWN RECEIPT TYPE";
			}
		}
		catch (RTSException aeEx)
		{
			System.out.println("Through RTSException " + aeEx);
		}
		return "UNKNOWN RECEIPT TYPE";
	}

	/**
	 * Loads all of the layout values for the sticker elements into a
	 * hashtable for lookup everytime a sticker is printed.
	 */
	private static void loadStickerFormatValues()
	{
		StickerFormatData laNewStickerFormat;
		
		// defect 11138
		// Version 1 no longer used 
		//		laNewStickerFormat =
		//			new StickerFormatData(
		//				VIN + VERSION1,
		//				2775,
		//				6475,
		//				14,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.MEDIUM,
		//				StickerPrintingConstant.RIGHT,
		//				5330,
		//				5825,
		//				10,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.BOLD,
		//				StickerPrintingConstant.RIGHT);
		//		laNewStickerFormat =
		//			new StickerFormatData(
		//				PLT + VERSION1,
		//				2775,
		//				5400,
		//				18,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.BOLD,
		//				StickerPrintingConstant.RIGHT,
		//				5330,
		//				6355,
		//				10,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.BOLD,
		//				StickerPrintingConstant.RIGHT);
		//		laNewStickerFormat =
		//			new StickerFormatData(
		//				EXPMO + VERSION1,
		//				1400,
		//				6250,
		//				88,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.BOLD,
		//				StickerPrintingConstant.RIGHT,
		//				4580,
		//				6185,
		//				36,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.BOLD,
		//				StickerPrintingConstant.RIGHT);
		//		laNewStickerFormat =
		//			new StickerFormatData(
		//				EXPYR + VERSION1,
		//				1400,
		//				6250,
		//				88,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.BOLD,
		//				StickerPrintingConstant.LEFT,
		//				4570,
		//				6185,
		//				36,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.BOLD,
		//				StickerPrintingConstant.LEFT);
		//		laNewStickerFormat =
		//			new StickerFormatData(
		//				CNTY + VERSION1,
		//				350,
		//				6475,
		//				14,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.MEDIUM,
		//				StickerPrintingConstant.LEFT,
		//				3930,
		//				6355,
		//				10,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.BOLD,
		//				StickerPrintingConstant.LEFT);
		//		
		//		laNewStickerFormat =
		//			new StickerFormatData(
		//				BARCODE + VERSION1,
		//				375,
		//				6580,
		//				0,
		//				CommonConstant.STR_SPACE_EMPTY,
		//				CommonConstant.STR_SPACE_EMPTY,
		//				StickerPrintingConstant.LEFT,
		//				0,
		//				0,
		//				0,
		//				CommonConstant.STR_SPACE_EMPTY,
		//				CommonConstant.STR_SPACE_EMPTY,
		//				CommonConstant.STR_SPACE_EMPTY);
		//		laNewStickerFormat =
		//			new StickerFormatData(
		//				VOID + VERSION1,
		//				1655,
		//				6150,
		//				48,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.BOLD,
		//				StickerPrintingConstant.CENTER,
		//				4675,
		//				6150,
		//				36,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.BOLD,
		//				StickerPrintingConstant.CENTER);
		
		// VIN /////////////////////////////////////////////////////////
		laNewStickerFormat =
			new StickerFormatData(
				VIN + VERSION2,
				2550,
				6860,
				14,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.MEDIUM,
				StickerPrintingConstant.RIGHT,
				5360,
				6125,
				10,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.RIGHT);

		// PLATE ///////////////////////////////////////////////////////
		laNewStickerFormat =
			new StickerFormatData(
				PLT + VERSION2,
				350,
				5885,
				14,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT,
				5360,
				6660,
				10,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.RIGHT);
		
		// EXPIRATION //////////////////////////////////////////////////
		laNewStickerFormat =
			new StickerFormatData(
				EXPMO + VERSION2,
				1275,
				6650,
				80,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.RIGHT,
				4660,
				6460,
				36,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.RIGHT);
		laNewStickerFormat =
			new StickerFormatData(
				EXPYR + VERSION2,
				1400,
				6650,
				80,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT,
				4770,
				6460,
				36,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);
		
		// COUNTY //////////////////////////////////////////////////////
		laNewStickerFormat =
			new StickerFormatData(
				CNTY + VERSION2,
				350,
				6860,
				14,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.MEDIUM,
				StickerPrintingConstant.LEFT,
				3965,
				6660,
				10,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);
		
		// BARCODE /////////////////////////////////////////////////////
		laNewStickerFormat =
			new StickerFormatData(
				BARCODE + VERSION2,
				1085,
				5750,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				StickerPrintingConstant.LEFT,
				0,
				0,
				0,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY);
		
		// VOID ////////////////////////////////////////////////////////
		laNewStickerFormat =
			new StickerFormatData(
				VOID + VERSION2,
				1450,
				6420,
				48,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER,
				4675,
				6440,
				36,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);
		
		// DO NOT USE ////////////////////////////////////////////////////////
		laNewStickerFormat =
			new StickerFormatData(
				DONOTUSE + VERSION2,
				1450,
				6560,
				14,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER,
				PL_CENTER + 25,
				6540,
				10,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);
		
		// NO USE ////////////////////////////////////////////////////////
		laNewStickerFormat =
			new StickerFormatData(
				NOUSE + VERSION2,
				1450,
				6700,
				14,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER,
				PL_CENTER + 25,
				6640,
				10,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);
	}
	// end defect 6082

	// Define objects
	DealerTitleData caDlrTtlData = new DealerTitleData();
	MFVehicleData caMFVehData = new MFVehicleData();
	OwnerData caOwnrData = new OwnerData();

	//Instance of ReceiptProperties Class
	protected ReceiptProperties caRcptProps = new ReceiptProperties();
	private RegistrationData caRegData = new RegistrationData();
	public Report caRpt = new Report(); 
	private TitleData caTitleData = new TitleData();
	public Dollar caTotalFees = new Dollar("0.00");
	public Dollar caTotalPay = new Dollar("0.00");
	private CompleteTransactionData caCTData =
		new CompleteTransactionData();
	
	protected boolean cbShouldStickersPrint;
	
	// Used for Registration Expiration Year printing
	protected int ciRegExpYrInvYr = 0;

	// defect 6082
	// save a copy of the formatted TtlNoMf
	private String csLocalDocNo = null;
	private Vector cvRcptInfo;
	// end defect 8829

	/**
	 * ReceiptTemplate constructor
	 */
	public ReceiptTemplate()
	{
		super();
	}

	/**
	 * ReceiptTemplate constructor
	 * 
	 * @param asName String
	 * @param aiNewWidth int
	 * @param aiLinesPerPage int
	 */
	public ReceiptTemplate(
		String asName,
		int aiNewWidth,
		int aiLinesPerPage)
	{
		caRpt = new Report(asName, aiNewWidth, aiLinesPerPage);
	}

	/**
	 * ReceiptTemplate constructor
	 *
	 * @param asRcptName String
	 * @param aaRcptProps ReceiptProperties
	 */
	public ReceiptTemplate(
		String asRcptName,
		ReceiptProperties aaRcptProps)
	{
		int liPageHeight = aaRcptProps.getPageHeight();
		// Get page height from ReportProperties
		int liPageWidth = aaRcptProps.getPageWidth();
		// Get page width from ReportProperties
		caRpt = new Report(asRcptName, liPageWidth, liPageHeight);
		caRcptProps = aaRcptProps;
	}

	/**
	 * Add element to vector if it is appropriate.
	 * 
	 * @param aaIndicatorCode IndicatorDescriptionsData
	 * @param avNotationsVector Vector
	 * @return int - Vector size
	 */
	public int addToNotations(
		IndicatorDescriptionsData aaIndicatorCode,
		Vector avNotationsVector)
	{
		if (aaIndicatorCode.getIndiRcptPriority() > 0)
		{
			avNotationsVector.addElement(aaIndicatorCode);
		}
		return avNotationsVector.size();
	}

	/**
	 * Build the data objects.
	 * 
	 * @param avRcptHeader Vector
	 */
	public void buildDataObjects(Vector avRcptHeader)
	{
		int liCompleteTrans = 0;

		caCTData =
			(CompleteTransactionData) avRcptHeader.elementAt(
				liCompleteTrans);
		
		if (caCTData.getVehicleInfo() != null)
		{
			caMFVehData = (MFVehicleData) caCTData.getVehicleInfo();
		}
		
		if (caMFVehData.getOwnerData() != null)
		{
			// defect 9145
			// If this is a Veh Inquiry of a special plate only then
			// we need to get the owner data from specail regis.
			if (isVehInqTransCd(caCTData.getTransCode())
				&& caMFVehData.isSPRecordOnlyVehInq()
				&& caMFVehData.isSpclPlt())
			{
				caOwnrData =
					(OwnerData) caMFVehData
						.getSpclPltRegisData()
						.getOwnrData();
			}
			else
			{
				caOwnrData = (OwnerData) caMFVehData.getOwnerData();
			}
			// end defect 9145
		}
		if (caMFVehData.getRegData() != null)
		{
			caRegData = (RegistrationData) caMFVehData.getRegData();
		}
		if (caMFVehData.getTitleData() != null)
		{
			caTitleData = (TitleData) caMFVehData.getTitleData();
		}
		// defect 11138
		//		if (caMFVehData.getVehicleData() != null)
		//		{
		//			caVehData = (VehicleData) caMFVehData.getVehicleData();
		//		}
		// end defect 11138 
	}

	/**
	 * Set which registration expiration year to by indi
	 *				IN = Inventory Issued
	 *				OS = Ownered Supplied.
	 *				MF = MFVeh
	 *  All code replaced in this method for PCR 34	
	 * 
	 * @param  aaTransData Object
	 * @return String
	 */
	protected String buildExpirationDt(Object aaTransData)
	{
		String lsItmTrckngType = "";
		String lsExpDt = "";
		Vector lvInvItm = new Vector();
		int liIndex = 0;
		CompleteTransactionData laTransData =
			(CompleteTransactionData) aaTransData;
		ProcessInventoryData laInvItms = new ProcessInventoryData();
		lvInvItm = laTransData.getAllocInvItms();
		// Vector of Inventory Items
		try
		{
			// Get the tracking type for the first element
			// defect 5126
			// Iterate through all inventory items to find a year.
			if (lvInvItm != null && lvInvItm.size() > 0)
			{
				while (liIndex < lvInvItm.size())
				{
					laInvItms =
						(ProcessInventoryData) lvInvItm.elementAt(
							liIndex);
					if (laInvItms.getInvItmYr() > 0)
					{
						lsItmTrckngType =
							getCacheItmTrkngType(laInvItms.getItmCd());
						// Test for Sticker or Plate, return MFVeh data
						// Indi
						// defect 3712
						// need to come back and find out
						// why we are doing it this way.  Seems like if
						// it has a year, just use it.
						//				Effects Combination Plate..
						// Do not count on plate having a year!
						if (lsItmTrckngType.equals(STICKER_TRCKNG_TYPE)
							|| lsItmTrckngType.equals(PLATE_TRCKNG_TYPE))
						{
							lsExpDt = "IN";
							ciRegExpYrInvYr = laInvItms.getInvItmYr();
							return lsExpDt;
						}
					}
					// end defect 3712
					++liIndex;
				} // end while
				//end defect 5126
			} // end if
			// Check for ownersupplied for Reg Correction since it 
			// doesn't use REG029 or Inv screens to set exp yr.
			if (caCTData
				.getVehicleInfo()
				.getRegData()
				.getOwnrSuppliedExpYr()
				> 0)
			{
				lsExpDt = "OS";
			}
			// Check for MFVeh data
			else if (caRegData.getRegExpYr() >= 0)
			{
				if ((caRegData.getRegPltNo() != null
					&& !caRegData.getRegPltNo().equals(""))
					|| (caRegData.getRegStkrNo() != null
						&& caRegData.getRegStkrNo().equals("")))
				{
					lsExpDt = "MF";
				}
			}
		} // end try box
		catch (NullPointerException aeNPEx)
		{
			System.out.println(
				aeNPEx + " refer to ReceiptTemplate/getPermitNo");
		}
		return lsExpDt;
	}

	/**
	 * This method builds up the vehicle notations vector.
	 * 
	 * @param  aaTransData CompleteTransactionData
	 * @return Vector
	 */
	public Vector buildNotationsVector(CompleteTransactionData aaTransData)
	{
		// Set up Vector to hold vehicle notations
		Vector lvNotations = new Vector();
		MFVehicleData lvMFVehData = aaTransData.getVehicleInfo();
		Vector lvIndicators =
			IndicatorLookup.getIndicators(
				lvMFVehData,
				aaTransData.getTransCode(),
				IndicatorLookup.RECEIPT);

		if (lvIndicators != null && lvIndicators.size() != 0)
		{
			for (int i = 0; i < lvIndicators.size(); i++)
			{
				IndicatorData laData =
					(IndicatorData) lvIndicators.get(i);
				lvNotations.add(laData.getDesc());
			}
		}
		return lvNotations;
	}

	/**
	 * Get the object off of the DataVector and check to see it they are
	 * Payment Objects.  If they are, put them on the outgoing Payment
	 * Vector.
	 * 
	 * @param  avQueryData Vector
	 * @return Vector
	 */
	public Vector buildPaymentVector(Vector avDataVector)
	{
		Vector lvPaymentVector = new Vector();

		// counter for adding elements to Vector
		int liPaymentCounter = 0;

		for (int i = 0; i < avDataVector.size(); i++)
		{
			Object laThisElement = avDataVector.elementAt(i);

			if (laThisElement instanceof TransactionPaymentData)
			{
				lvPaymentVector.addElement(
					(TransactionPaymentData) avDataVector.elementAt(i));
				liPaymentCounter = liPaymentCounter + 1;
			}
		}
		return lvPaymentVector;
	}

	/**
	 * Calculate the character length og the input string.
	 * 
	 * @param asInputStr String
	 * @return int
	 */
	public int charLength(String asInputStr)
	{
		return asInputStr.length();
	}

	/**
	 * Convert Date from integer to String in a RTSDate format.
	 * 
	 * @param aiDate int
	 * @return String
	 */
	public String convertDtToStr(int aiDate)
	{
		return new RTSDate(RTSDate.YYYYMMDD, aiDate).toString();
	}

	/**
	 * Create dummy docno to use on receipt.
	 * 
	 * @param aiOfcNo int
	 */
	private void createDummyDocNo(int aiOfcNo)
	{
		int liTransAMDate =
			Integer
				.valueOf(caCTData.getTransId().substring(6, 11))
				.intValue();
		int liTransTime =
			Integer
				.valueOf(caCTData.getTransId().substring(11, 17))
				.intValue();
		// defect 7705
		// change package reference
		//Build transid and docno for special registration section
		csLocalDocNo =
			Transaction.populateSpecTtlNoMf(
				aiOfcNo,
				SystemProperty.getWorkStationId(),
				liTransAMDate,
				liTransTime);
		// end defect 7705
	}

	/**
	 * Create BarCode for Sticker Printing
	 * 
	 * @param  aaTransData CompleteTransactionData
	 * @return PDF417
	 */
	private PDF417 dataToBarcode(CompleteTransactionData aaCTData)
	{
		// if CompleteTransactionData is not going to work return error
		if (aaCTData == null || aaCTData.getVehicleInfo() == null)
		{
			PDF417 laPdf = new PDF417();
			laPdf.setDataToEncode("*** ERROR ***");
			return laPdf;
		}
		DealerTitleData laLastRecord = aaCTData.getDlrTtlData();

		// defect 10290 
		//		if (UtilityMethods.isDTA(aaTransData.getTransCode()))
		//		{
		//			for (int i = aaTransData.getDlrTtlDataObjs().size() - 1;
		//				i > -1;
		//				i--)
		//			{
		//				DealerTitleData laDTD =
		//					(DealerTitleData) aaTransData
		//						.getDlrTtlDataObjs()
		//						.get(
		//						i);
		//				if (laDTD.isProcessed())
		//				{
		//					laLastRecord = laDTD;
		//					break;
		//				}
		//			}
		//		}
		// end defect 10290 

		String lsStrInfo = "";
		String lsString = "";
		String lsRegExpYr = "";
		toConsole(
			"Testing",
			lsString.length(),
			"123456789012345678901234567");
		// defect 7107
		/**
		 *  Barcode Type
		 */
		lsString = BARCODE_TYPE;
		toConsole("BarCodeType:", lsString.length(), lsString);
		lsStrInfo += lsString;
		/**
		 * Barcode Version
		 */
		lsString = BARCODE_VERSION;
		toConsole("BarCodeVersion:", lsString.length(), lsString);
		lsStrInfo += lsString;
		/**
		 * Document Number
		 */
		lsString = UtilityMethods.addPadding(determineDocNo(), 17, " ");
		toConsole("DocNo:", lsString.length(), lsString);
		lsStrInfo += lsString;
		/**
		 * VIN
		 */
		lsString = "";
		if (aaCTData.getVehicleInfo().getVehicleData() != null
			&& aaCTData.getVehicleInfo().getVehicleData().getVin()
				!= null)
		{
			lsString =
				aaCTData.getVehicleInfo().getVehicleData().getVin();
		}
		lsString = UtilityMethods.addPaddingRight(lsString, 22, " ");
		toConsole("VIN:", lsString.length(), lsString);
		lsStrInfo += lsString;
		/**
		 * Residence County Number
		 */

		// defect 7151
		// now calling determineResCompCntyNo() to rid duplicate code
		lsString =
			UtilityMethods.addPadding(determineResCompCntyNo(), 3, "0");
		// end defect 7151
		toConsole("ResComptCntyNo:", lsString.length(), lsString);
		lsStrInfo += lsString;
		/**
		 * Workstation ID
		 */
		lsString = Integer.toString(SystemProperty.getWorkStationId());
		lsString = UtilityMethods.addPaddingRight(lsString, 10, " ");
		toConsole("Workstation ID:", lsString.length(), lsString);
		lsStrInfo += lsString;
		/**
		 * County Number - Office Issuance Number
		 */
		lsString =
			UtilityMethods.addPadding(
				Integer.toString(SystemProperty.getOfficeIssuanceNo()),
				3,
				"0");
		toConsole("Print County Number:", lsString.length(), lsString);
		lsStrInfo += lsString;
		/**
		 * Sticker Print Date 
		 */
		RTSDate laRTSDate = new RTSDate();
		lsString = Integer.toString(laRTSDate.getYYYYMMDDDate());
		toConsole("Sticker Print Date:", lsString.length(), lsString);
		lsStrInfo += lsString;
		/**
		 * Registration Plate Cd 
		 */
		lsString = "";
		if (aaCTData.getVehicleInfo().getRegData() != null
			&& aaCTData.getVehicleInfo().getRegData().getRegPltCd()
				!= null)
		{
			lsString =
				aaCTData.getVehicleInfo().getRegData().getRegPltCd();
		}
		lsString = UtilityMethods.addPaddingRight(lsString, 8, " ");
		toConsole("RegPltCd:", lsString.length(), lsString);
		lsStrInfo += lsString;
		/**
		 * Registration Sticker Cd 
		 */
		lsString = "";
		if (aaCTData.getVehicleInfo().getRegData() != null
			&& aaCTData.getVehicleInfo().getRegData().getRegStkrCd()
				!= null)
		{
			lsString =
				aaCTData.getVehicleInfo().getRegData().getRegStkrCd();
		}
		lsString = UtilityMethods.addPaddingRight(lsString, 8, " ");
		toConsole("RegStkrCd:", lsString.length(), lsString);
		lsStrInfo += lsString;
		/**
		 * Plate Num
		 */
		// Run through issued inventory for
		//   Plate Num and and RegExpYr
		lsString = "";
		for (int i = 0; i < aaCTData.getAllocInvItms().size(); i++)
		{
			ProcessInventoryData laInvData =
				(ProcessInventoryData) aaCTData.getAllocInvItms().get(
					i);
			String lsItmCd = laInvData.getItmCd();
			ItemCodesData laItemData = ItemCodesCache.getItmCd(lsItmCd);
			String lsItmTrckngType = laItemData.getItmTrckngType();
			if (lsItmTrckngType.equals("P")
				|| lsItmTrckngType.equals("S"))
			{
				// If Plate,grab InvItmNo as RegPltNo 
				if (lsItmTrckngType.equals("P"))
				{
					lsString = laInvData.getInvItmNo();
				}
				// If assigned InvItmYr, will use for RegExpYr  
				if (laInvData.getInvItmYr() != 0)
				{
					lsRegExpYr =
						Integer.toString(laInvData.getInvItmYr());
				}
			}
		}
		if (lsString.equals(""))
		{
			// no registration	
			if (aaCTData.getVehicleInfo().getRegData() == null
				|| aaCTData.getVehicleInfo().getRegData().getRegPltNo()
					== null)
			{
				if (UtilityMethods.isDTA(aaCTData.getTransCode())
					&& laLastRecord != null)
				{
					lsString = laLastRecord.getNewPltNo();
				}
			}
			else
			{
				// defect 7103
				//
				// Owner Supplied
				//
				if (aaCTData.getOwnrSuppliedPltNo() != null
					&& !aaCTData.getOwnrSuppliedPltNo().trim().equals(""))
				{
					lsString = aaCTData.getOwnrSuppliedPltNo();
				}
				// end defect 7103
				else
				{
					lsString =
						aaCTData
							.getVehicleInfo()
							.getRegData()
							.getRegPltNo();
				}
			}
		}
		lsString = UtilityMethods.addPaddingRight(lsString, 7, " ");
		toConsole("PlateNum:", lsString.length(), lsString);
		lsStrInfo += lsString;

		// defect 7151
		// Moved year before month so it uses the format (YYYYMM)
		// Removed all Item codes and fees from the barcode
		/**
		 * Expiration Year
		 */
		if (lsRegExpYr.equals(""))
		{
			if (aaCTData.getVehicleInfo().getRegData() != null)
			{
				if (UtilityMethods.isDTA(aaCTData.getTransCode())
					&& laLastRecord != null)
				{
					// Defect 7496
					//  NewRegExpMo and NewRegExpYr were converted to integer
					//lsString = lastRecord.getNewRegExpYr();
					lsString =
						Integer.toString(laLastRecord.getNewRegExpYr());
					// End Defect 7496
				}
				else
				{
					lsString =
						Integer.toString(
							aaCTData
								.getVehicleInfo()
								.getRegData()
								.getRegExpYr());
				}
			}
		}
		lsString = lsRegExpYr;
		if (lsString.equals(""))
		{
			lsString = UtilityMethods.addPadding(lsString, 4, " ");
		}
		else
		{
			lsString = UtilityMethods.addPadding(lsString, 4, "0");
		}
		toConsole("RegExpYr:", lsString.length(), lsString);
		lsStrInfo += lsString;
		/**
		 * Expiration Month 
		 */
		if (aaCTData.getVehicleInfo().getRegData() != null)
		{
			if (UtilityMethods.isDTA(aaCTData.getTransCode())
				&& laLastRecord != null)
			{
				// defect 7496
				// NewRegExpMo and NewRegExpYr were converted to integer
				//lsString = lastRecord.getNewRegExpMo();
				lsString =
					Integer.toString(laLastRecord.getNewRegExpMo());
				// end defect 7496
			}
			else
			{
				lsString =
					Integer.toString(
						aaCTData
							.getVehicleInfo()
							.getRegData()
							.getRegExpMo());
			}
		}
		if (lsString.equals(""))
		{
			lsString = UtilityMethods.addPadding(lsString, 2, " ");
		}
		else
		{
			lsString = UtilityMethods.addPadding(lsString, 2, "0");
		}
		toConsole("RegExpMo:", lsString.length(), lsString);
		lsStrInfo += lsString;

		/**
		 * RegClassCd 
		 */
		lsString = "";
		if (aaCTData.getVehicleInfo().getRegData() != null)
		{
			lsString =
				Integer.toString(
					aaCTData
						.getVehicleInfo()
						.getRegData()
						.getRegClassCd());
		}

		// defect 10464 
		//lsString = UtilityMethods.addPadding(lsString, 2, "0");
		lsString = UtilityMethods.addPadding(lsString, 3, "0");
		// end defect 10464 

		toConsole("RegClassCd:", lsString.length(), lsString);
		lsStrInfo += lsString;
		if (SystemProperty.getProdStatus() != 0)
		{
			System.out.println("**** End of BarCode Text ****");
		}
		// end defect 7107
		PDF417 laPdf = new PDF417();
		laPdf.setSize(330, 60);
		laPdf.setDataToEncode(lsStrInfo);
		laPdf.setPDFColumns(12);
		laPdf.setPDFRows(10);
		laPdf.setPDFMode(PDF417.PDF_TEXT);
		laPdf.setTopMarginCM(.2);
		laPdf.setLeftMarginCM(.2);
		return laPdf;
	}

	/**
	 * Determine Header based on Trans Code and Print it.
	 * Handle RTSException by print a message about it on the receipt.
	 * 
	 * @param  asTransCd String 
	 * @throws RTSException
	 */
	public void determineAndPrintHeading(String asTransCd)
		throws RTSException
	{
		// Set font for the heading
		String lsPageProperties = "";
		if (!cbShouldStickersPrint)
		{
			lsPageProperties =
				Print.getPRINT_TOP_MARGIN()
					+ Print.getPRINT_TRAY_2()
					+ Print.getPRINT_ELITE()
					+ Print.getPRINT_LINE_SPACING_1_8()
					+ " "
					+ Print.getPRINT_BOLD()
					+ " "
					+ Print.getPRINT_10CPI();
		}
		else
		{
			lsPageProperties =
				Print.getPRINT_TOP_MARGIN()
					+ Print.getPRINT_TRAY_STICKER()
					+ Print.getPRINT_ELITE()
					+ Print.getPRINT_LINE_SPACING_1_8()
					+ " "
					+ Print.getPRINT_BOLD()
					+ " "
					+ Print.getPRINT_10CPI();
		}
		this.caRpt.printAttributesNoReturn(lsPageProperties);
		int COL_02 = 2;
		// get TransCode Desc from TransC
		if (isDealerTitleTransCd(asTransCd))
		{
			//  defect 7436
			//  Remove period from verbaige "FOR MICROFILM PURPOSES"
			String lsHeader = "FOR MICROFILM PURPOSES";
			// End Defect 7436
			this.caRpt.print(lsHeader, COL_02, lsHeader.length());
		}
		else if (
			caTitleData != null
				&& caTitleData.getTtlTypeIndi()
					== TitleTypes.INT_REGPURPOSE
				&& isTitleTransCd(asTransCd))
		{
			String lsHeader =
				"REGISTRATION PURPOSES ONLY APPLICATION RECEIPT";
			this.caRpt.print(lsHeader, COL_02, lsHeader.length());
		}
		else
		{
			TransactionCodesData laTransCd =
				TransactionCodesCache.getTransCd(asTransCd);
			if (laTransCd != null)
			{
				// Print TransCode Description as the Header
				// defect 7450
				//this.cRpt.print(
				//	lTransCd.getTransCdDesc().trim(),
				//	COL_02,
				//	lTransCd.getTransCdDesc().length());
				// defect 10491 
				if (asTransCd.equals(TransCdConstant.PRMDUP))
				{
					PermitData laPrmtData =
						(PermitData) caCTData.getTimedPermitData();
					String lsTransCdDesc =
						TransactionCodesCache.getTransCdDesc(asTransCd);
					int liDupLoc = lsTransCdDesc.indexOf("DUPLICATE");
					lsTransCdDesc = lsTransCdDesc.substring(liDupLoc);
					String lsItemCdDesc =
						ItemCodesCache.getItmCdDesc(
							laPrmtData.getItmCd());

					lsTransCdDesc =
						lsItemCdDesc + " - " + lsTransCdDesc;
					this.caRpt.print(
						lsTransCdDesc.trim(),
						COL_02,
						lsTransCdDesc.length());

				}
				else
					// end defect 10491  
					if (!asTransCd.equals(TransCdConstant.SBRNW))
					{
						this.caRpt.print(
							laTransCd.getTransCdDesc().trim(),
							COL_02,
							laTransCd.getTransCdDesc().length());
					}
					else
					{
						this.caRpt.print(
							RENEWALSUBCONRECEIPTSTRING.trim(),
							COL_02,
							RENEWALSUBCONRECEIPTSTRING.length());
					}
				//end defect 7450
			}
			else
			{
				this.caRpt.print(
					"TRANS CODE NOT FOUND",
					COL_06,
					"TRANS CODE NOT FOUND".length());
			}
		}
		// Set font for normal printing and close the line
		if (!cbShouldStickersPrint)
		{
			this.caRpt.printAttributes(
				Print.getPRINT_NORMAL() + Print.getPRINT_12CPI());
			this.caRpt.printAttributes(
				Print.getPRINT_ELITE()
					+ Print.getPRINT_LINE_SPACING_1_8());
		}
		else
		{
			caRpt.printAttributes(
				Print.getPRINT_NORMAL() + Print.getPRINT_12CPI());
			caRpt.printAttributes(Print.getSTICKER_SETTINGS());
		}
	}

	/**
	 * Determine Header based on Trans Code and Print it.
	 * Handle RTSException by print a message about it on the receipt.
	 * 
	 * @param asTransCd String
	 * @param abBarcode boolean
	 * @throws RTSException
	 */
	public void determineAndPrintHeading(
		String asTransCd,
		boolean abBarcode)
		throws RTSException
	{
		// Set font for the heading
		String lsPageProperties = "";
		if (!cbShouldStickersPrint)
		{
			lsPageProperties =
				Print.getPRINT_TOP_MARGIN()
					+ Print.getPRINT_TRAY_2()
					+ Print.getPRINT_ELITE()
					+ Print.getPRINT_LINE_SPACING_1_8()
					+ " "
					+ Print.getPRINT_BOLD()
					+ " "
					+ Print.getPRINT_10CPI();
		}
		else
		{
			lsPageProperties =
				Print.getPRINT_TOP_MARGIN()
					+ Print.getPRINT_TRAY_STICKER()
					+ Print.getPRINT_ELITE()
					+ Print.getPRINT_LINE_SPACING_1_8()
					+ " "
					+ Print.getPRINT_BOLD()
					+ " "
					+ Print.getPRINT_10CPI();
		}
		this.caRpt.printAttributesNoReturn(lsPageProperties);
		int COL_02 = 2;
		// get TransCode Desc from TransC
		if (isDealerTitleTransCd(asTransCd))
		{
			// defect 7436
			//  Print appropriate header for DTA Title/Barcode Receipt
			String lsHeader = "";
			if (abBarcode)
			{
				lsHeader = "FOR MICROFILM PURPOSES";
			}
			else
			{
				TransactionCodesData laTransCd =
					TransactionCodesCache.getTransCd("TITLE");
				lsHeader = laTransCd.getTransCdDesc().trim();
			}
			// end defect 7436
			this.caRpt.print(lsHeader, COL_02, lsHeader.length());
		}
		else if (
			caTitleData != null
				&& caTitleData.getTtlTypeIndi()
					== TitleTypes.INT_REGPURPOSE
				&& isTitleTransCd(asTransCd))
		{
			String lsHeader =
				"REGISTRATION PURPOSES ONLY APPLICATION RECEIPT";
			this.caRpt.print(lsHeader, COL_02, lsHeader.length());
		}
		else
		{
			TransactionCodesData laTransCd =
				TransactionCodesCache.getTransCd(asTransCd);
			if (laTransCd != null)
			{
				// Print TransCode Description as the Header
				this.caRpt.print(
					laTransCd.getTransCdDesc().trim(),
					COL_02,
					laTransCd.getTransCdDesc().length());
			}
			else
			{
				this.caRpt.print(
					"TRANS CODE NOT FOUND",
					COL_06,
					"TRANS CODE NOT FOUND".length());
			}
		}
		// Set font for normal printing and close the line
		if (!cbShouldStickersPrint)
		{
			this.caRpt.printAttributes(
				Print.getPRINT_NORMAL() + Print.getPRINT_12CPI());
			this.caRpt.printAttributes(
				Print.getPRINT_ELITE()
					+ Print.getPRINT_LINE_SPACING_1_8());
		}
		else
		{
			caRpt.printAttributes(
				Print.getPRINT_NORMAL() + Print.getPRINT_12CPI());
			caRpt.printAttributes(Print.getSTICKER_SETTINGS());
		}
	}

	/**
	 * Determine the correct Document No to Print
	 * 
	 * @return String
	 */
	protected String determineDocNo()
	{
		String lsDocNo = csLocalDocNo;
		if (lsDocNo == null)
		{
			// If Title/DTA, new DocNo = TransId
			if (isTitleTransCd(caCTData.getTransCode())
				|| isDealerTitleTransCd(caCTData.getTransCode()))
			{
				lsDocNo = caCTData.getTransId();
			}
			else
			{
				lsDocNo = caTitleData.getDocNo();
			}
		}
		return lsDocNo;
	}

	/**
	 * Determine the correct plate to print.
	 * 
	 * @return String
	 */
	protected String determinePltNo()
	{
		String lsRegPltNo = "";
		// get the first inventroy item in the inventory object
		lsRegPltNo =
			getInvItmNo(caCTData, PLATE_TRCKNG_TYPE, FIRST_RECORD);
		// Is the first item in the Inventory object a plate number, 
		//   returns a blank if not found
		if (lsRegPltNo != null && !lsRegPltNo.equals(""))
		{
			return lsRegPltNo;
		}

		// get the second inventory item in the inventory object
		lsRegPltNo =
			getInvItmNo(caCTData, PLATE_TRCKNG_TYPE, SECOND_RECORD);

		// Is the second item in the Inventory table a plate number,
		//   returns a blank if not found
		if (lsRegPltNo != null && !lsRegPltNo.equals(""))
		{
			return lsRegPltNo;
		}
		// get the third inventroy item in the inventory object
		lsRegPltNo =
			getInvItmNo(caCTData, PLATE_TRCKNG_TYPE, THIRD_RECORD);
		// Is the third item in the Inventory table a plate number,
		//   returns a blank if not found
		if (lsRegPltNo != null && !lsRegPltNo.equals(""))
		{
			return lsRegPltNo;
		}
		// Check for ownersupplied, else return RegPltNo
		if (caCTData.getOwnrSuppliedPltNo() != null
			&& !caCTData.getOwnrSuppliedPltNo().equals(""))
		{
			lsRegPltNo = caCTData.getOwnrSuppliedPltNo();
		}
		else
		{
			lsRegPltNo = caRegData.getRegPltNo();
		}
		return lsRegPltNo == null ? "" : lsRegPltNo;
	}

	/**
	 * Determine trhe correct County Name to print on
	 * the sticker receipt.
	 *
	 * @return String lsResCompCntyName
	 */
	protected String determineResCompCntyName()
	{
		String lsResCompCntyName;
		try
		{
			// get the ResCompCntyNumber
			String lsResCompCntyNo = determineResCompCntyNo();
			// try to convert string to integer if number format then
			// return ""
			int liCompCntyNo = Integer.parseInt(lsResCompCntyNo);
			// lookup county name from the office ids cache table for
			// given Res Comp County No
			lsResCompCntyName =
				OfficeIdsCache.getOfcId(liCompCntyNo).getOfcName();
		}
		catch (NumberFormatException aeNFEx)
		{
			lsResCompCntyName = "";
		}
		return lsResCompCntyName;
	}

	/**
	 * Determine the correct Document No to Print
	 * 
	 * @return String
	 */
	protected String determineResCompCntyNo()
	{
		// done the same as in 
		String lsResCompCntyNo = "";

		// defect 10745 
		if (caCTData.getTransCode().equals(TransCdConstant.SBRNW)
			|| caCTData.getTransCode().equals(TransCdConstant.WRENEW))
		{
			// end defect 10745 

			lsResCompCntyNo =
				Integer.toString(caCTData.getOfcIssuanceNo());
		}
		else
		{
			if (caCTData.getVehicleInfo().getRegData() != null
				&& caCTData
					.getVehicleInfo()
					.getRegData()
					.getResComptCntyNo()
					!= 0)
			{
				lsResCompCntyNo =
					Integer.toString(
						caCTData
							.getVehicleInfo()
							.getRegData()
							.getResComptCntyNo());
			}
		}
		return lsResCompCntyNo;
	}

	/**
	 * Format the string to show the Credit Card Fee Added message if
	 * credit card is charged.
	 *
	 * @return String 
	 */
	protected String formatCreditCardFeeMessage()
	{
		return "(Credit Card Fee)";
	}

	/**
	* Pad fees to eleven characters. Add the dollar sign.
	* 
	* @param asDollarAmt String
	* @param aiPad int
	* @return String
	*/
	public String formatDollarAmt(String asDollarAmt, int aiPad)
	{
		if (asDollarAmt == null)
		{
			asDollarAmt = "0.00";
		}
		String lsDollarStr = asDollarAmt;
		// Get string and pad length
		String lsPadLngth = caRpt.blankSpaces(aiPad);
		int lsAmtLngth = asDollarAmt.length();
		int lsThirteenDigits = lsPadLngth.length();
		// Pad string
		for (int i = lsAmtLngth; i < lsThirteenDigits; i++)
		{
			lsDollarStr = caRpt.blankSpaces(1) + lsDollarStr;
		}
		// Add dollar sign
		lsDollarStr = caRpt.blankSpaces(1) + DOLLAR_SIGN + lsDollarStr;
		return lsDollarStr;
	}

	/**
	 * formatReceipt
	 * 
	 * @param avResults Vector
	 */
	public void formatReceipt(Vector avResults)
	{
		// empty code block
	}

	/**
	* This Method defines the accounting event receipts.
	* The header, owner, vehicle and remarks will be generated.
	*
	* @param avRcptHeader Vector
	* @throws RTSException
	*/
	protected void generateReceiptHeader(Vector avRcptHeader)
		throws RTSException
	{
		// Define local variables		
		Vector lvOwnrInfo = new Vector();
		Vector lvRcptInfo = new Vector();
		// Parse TransData object
		buildDataObjects(avRcptHeader);
		// ***************  NAME OF THE RECEIPT ***************
		determineAndPrintHeading(caCTData.getTransCode());

		caRpt.blankLines(4);
		// **********  Print County/TAC or Office names ************

		// defect 10745 
		if (!caCTData.getTransCode().equals(TransCdConstant.WRENEW))
		{
			printCntyOffice(SystemProperty.getOfficeIssuanceNo());
		}
		else
		{
			printCntyOffice(caCTData.getOfcIssuanceNo());
		}
		// end defect 10745 

		caRpt.nextLine();
		// **********  PRINT THE FIRST THREE LINES OF THE HEADER ************
		printFirst3HeaderLines();
		// *** OWNER INFORMATION - EXCLUDE ADDITIONAL SALES TAX ************
		if (!(caCTData.getTransCode().equals(TRANSCD_ADLSTX)))
		{
			// ***************  OWNER INFORMATION  ***************
			if (isPermitTransCd(caCTData.getTransCode())
				|| isDisablePlacardTransCd(caCTData.getTransCode())
				|| isNonResidtAgrTransCd(caCTData.getTransCode())
				|| isTowTrkTransCd(caCTData.getTransCode()))
			{
				cvRcptInfo = parseOwnrPermitInfo(caCTData);
			}
			else if (!isRegColTransCd(caCTData.getTransCode()))
			{
				cvRcptInfo = parseOwnrInfo(caOwnrData, caRegData);
			}

			// Do not print owner information for Temp Additional Weight, save vector
			if (isTempAddlWtTransCd(caCTData.getTransCode()))
			{
				lvOwnrInfo = cvRcptInfo;
			}
			else if (!isRegColTransCd(caCTData.getTransCode()))
			{
				printOwnerInfo(cvRcptInfo);
			}

			// ***************  REGIS/PLATE/STICKER DESCRIPTION  ***************
			// defect 9145
			// Handle Veh Inquiry of Special Plate only record.
			if ((isVehInqTransCd(caCTData.getTransCode())
				&& !caMFVehData.isSPRecordOnlyVehInq())
				|| isTitleTransCd(caCTData.getTransCode())
				|| isTempAddlWtTransCd(caCTData.getTransCode())
				|| isRegisTransCd(caCTData.getTransCode())
				|| isAcctTransCd(caCTData.getTransCode())
				|| isDealerTitleTransCd(caCTData.getTransCode()))
			{
				cvRcptInfo =
					parseRegsDescVehLoc(caRegData, caTitleData);
				if (isTempAddlWtTransCd(caCTData.getTransCode()))
				{
					lvRcptInfo = cvRcptInfo;
					cvRcptInfo =
						mergeOwnrRegisInfo(lvOwnrInfo, lvRcptInfo);
					printOwnerInfo(cvRcptInfo);
				}
				else
				{
					printRegisDesc(cvRcptInfo);
				}
			}
			// end defect 9145
			caRpt.blankLines(1);
		} // end if
	}

	/**
	* This Method defines the accounting event receipts.
	* The header, owner, vehicle and remarks will be generated.
	*
	* @param avRcptHeader Vector Additional Headers that need to
	* 		be displayed on each page
	* @param abBarcode boolean
	* @throws RTSException
	*/
	protected void generateReceiptHeader(
		Vector avRcptHeader,
		boolean abBarcode)
		throws RTSException
	{
		// Define local variables 
		Vector lvOwnrInfo = new Vector();
		Vector lvRcptInfo = new Vector();
		// Parse TransData object
		buildDataObjects(avRcptHeader);
		// ***************  NAME OF THE RECEIPT ***************
		// defect 7436
		//  Send barcode boolean to set DTA receipt header verbage
		determineAndPrintHeading(caCTData.getTransCode(), abBarcode);
		// end defect 7436
		// Move down 5 lines
		caRpt.blankLines(3);
		if (abBarcode
			&& (isTitleTransCd(caCTData.getTransCode())
				|| isDealerTitleTransCd(caCTData.getTransCode())))
		{
			this.caRpt.print("", 54, 1);
			// defect 6068
			// setup transid to send to barcode
			if (caRegData != null
				&& caRegData.getExmptIndi() != 1
				&& caCTData.getOfcIssuanceNo() == 291
				&& (isTitleTransCd(caCTData.getTransCode())
					|| isDealerTitleTransCd(caCTData.getTransCode())
					|| caCTData.getTransCode().equals(TRANSCD_RENEW)
					|| caCTData.getTransCode().equals(TRANSCD_REPL)))
			{
				int liOfficeNo = caRegData.getResComptCntyNo();
				// call createDummyDocNo if this is special case
				createDummyDocNo(liOfficeNo);
			}
			if (csLocalDocNo != null)
			{
				// use created local doc no  
				this.caRpt.print(
					Print.getBARCODE_TAG(
						csLocalDocNo,
						7.2,
						cbShouldStickersPrint));
			}
			else
			{
				this.caRpt.print(
					Print.getBARCODE_TAG(
						caCTData.getTransId(),
						7.2,
						cbShouldStickersPrint));
				// verify this later
			}
			// end defect 6068
		}
		caRpt.blankLines(2);
		// **********  Print County/TAC or Office names ************
		//printCntyOffice(coTransData.getOfcIssuanceNo());
		printCntyOffice(SystemProperty.getOfficeIssuanceNo());

		caRpt.nextLine();
		// **********  PRINT THE FIRST THREE LINES OF THE HEADER ************
		printFirst3HeaderLines();

		// *** OWNER INFORMATION - EXCLUDE ADDITIONAL SALES TAX ************
		if (!(caCTData.getTransCode().equals(TRANSCD_ADLSTX)))
		{
			// ***************  OWNER INFORMATION  ***************
			if (isPermitTransCd(caCTData.getTransCode())
				|| isDisablePlacardTransCd(caCTData.getTransCode())
				|| isNonResidtAgrTransCd(caCTData.getTransCode())
				|| isTowTrkTransCd(caCTData.getTransCode()))
			{
				cvRcptInfo = parseOwnrPermitInfo(caCTData);
			}
			else if (!isRegColTransCd(caCTData.getTransCode()))
			{
				cvRcptInfo = parseOwnrInfo(caOwnrData, caRegData);
			}
			// Do not print owner information for Temp Additional Weight, save vector
			if (isTempAddlWtTransCd(caCTData.getTransCode()))
			{
				lvOwnrInfo = cvRcptInfo;
			}
			else if (!isRegColTransCd(caCTData.getTransCode()))
			{
				printOwnerInfo(cvRcptInfo);
			}
			// ***************  REGIS/PLATE/STICKER DESCRIPTION  ***************
			if (isVehInqTransCd(caCTData.getTransCode())
				|| isTitleTransCd(caCTData.getTransCode())
				|| isTempAddlWtTransCd(caCTData.getTransCode())
				|| isRegisTransCd(caCTData.getTransCode())
				|| isAcctTransCd(caCTData.getTransCode())
				|| isDealerTitleTransCd(caCTData.getTransCode()))
			{
				cvRcptInfo =
					parseRegsDescVehLoc(caRegData, caTitleData);
				if (isTempAddlWtTransCd(caCTData.getTransCode()))
				{
					lvRcptInfo = cvRcptInfo;
					cvRcptInfo =
						mergeOwnrRegisInfo(lvOwnrInfo, lvRcptInfo);
					printOwnerInfo(cvRcptInfo);
				}
				else
				{
					printRegisDesc(cvRcptInfo);
				}
			} // end if
			// Move down 2 line
			caRpt.blankLines(1);
		} // end if
	}

	/**
	 * Generate Fees Header
	 *
	 * @param  asTransCd String
	 * @return String
	 */
	protected String generaterFeesHeader(String asTransCd)
	{
		String lsHeading;
		if (asTransCd.equals(TRANSCD_REFUND)
			|| asTransCd.equals(TRANSCD_RFCASH))
		{
			lsHeading = REFUND_HD_TEXT;
		}
		else if (
			asTransCd.equals(TRANSCD_HOTCK)
				|| asTransCd.equals(TRANSCD_HOTDED))
		{
			lsHeading = HOT_CK_CRDT_TEXT;
		}
		else
		{
			lsHeading = FEES_ASSESSED_TEXT;
		}
		return lsHeading;
	}

	/**
	 * Print receipt stickers.
	 *
	 * @param aaCTData CompleteTransactionData
	 */
	protected void generateStickers(CompleteTransactionData aaCTData)
	{
		// defect 8829
		// Adjusted this method to allow multiple versions of the
		// sticker to be printed.  Instead of referencing class level
		// constants for the location and font of the sticker element we
		// now do a hash table lookup using element name + version.  
		// This lookup will either return a null or a StickerFormatData
		// object containing the location and font for that element for
		// that version.  If null is returned then that element does
		// not exist for that version.
		// end defect 8829

		/// defect 11138
		// Move assignment of Void/DoNotUse StickerFormat 
		String lsVersion = "2";

		boolean lbWATokenTrailer = caCTData.getTransCode().equals(TransCdConstant.WRENEW)
				&& caRegData.isTokenTrailer();
			
		StickerFormatData laStickerFormat =
			StickerFormatData.getLayout(DONOTUSE + lsVersion);
		String lsDoNotUseWindshield = CommonConstant.STR_SPACE_EMPTY;
		String lsDoNotUsePlate = CommonConstant.STR_SPACE_EMPTY;
		if (laStickerFormat != null)
		{
			lsDoNotUseWindshield =
				PCLUtilities.genStickerLayoutPCL(
					laStickerFormat.getWS_HORI(),
					laStickerFormat.getWS_VERT(),
					DONOTUSE,
					laStickerFormat.getWS_JUST(),
					laStickerFormat.getWS_FONT(),
					laStickerFormat.getWS_FONT_SIZE(),
					laStickerFormat.getWS_STROKE());

			lsDoNotUsePlate =
				PCLUtilities.genStickerLayoutPCL(
					laStickerFormat.getPL_HORI(),
					laStickerFormat.getPL_VERT(),
					DONOTUSE,
					laStickerFormat.getPL_JUST(),
					laStickerFormat.getPL_FONT(),
					laStickerFormat.getPL_FONT_SIZE(),
					laStickerFormat.getPL_STROKE());
		}

		laStickerFormat =
			StickerFormatData.getLayout(NOUSE + lsVersion);
		String lsNoUseWindshield = CommonConstant.STR_SPACE_EMPTY;
		String lsNoUsePlate = CommonConstant.STR_SPACE_EMPTY;
		
		if (laStickerFormat != null)
		{
			lsNoUseWindshield =
				PCLUtilities.genStickerLayoutPCL(
					laStickerFormat.getWS_HORI(),
					laStickerFormat.getWS_VERT(),
					NOUSE,
					laStickerFormat.getWS_JUST(),
					laStickerFormat.getWS_FONT(),
					laStickerFormat.getWS_FONT_SIZE(),
					laStickerFormat.getWS_STROKE());

			lsNoUsePlate =
				PCLUtilities.genStickerLayoutPCL(
					laStickerFormat.getPL_HORI(),
					laStickerFormat.getPL_VERT(),
					NOUSE,
					laStickerFormat.getPL_JUST(),
					laStickerFormat.getPL_FONT(),
					laStickerFormat.getPL_FONT_SIZE(),
					laStickerFormat.getPL_STROKE());
		}
			
		laStickerFormat = StickerFormatData.getLayout(VOID + lsVersion);
		String lsStrVoidWindshield = CommonConstant.STR_SPACE_EMPTY;
		String lsStrVoidPlate = CommonConstant.STR_SPACE_EMPTY;
		if (laStickerFormat != null)
		{
			lsStrVoidWindshield =
				NEW_LINE
				+ PCLUtilities.genStickerLayoutPCL(
						laStickerFormat.getWS_HORI(),
						laStickerFormat.getWS_VERT(),
						VOID,
						laStickerFormat.getWS_JUST(),
						laStickerFormat.getWS_FONT(),
						laStickerFormat.getWS_FONT_SIZE(),
						laStickerFormat.getWS_STROKE());

			lsStrVoidPlate =
				NEW_LINE
				+ PCLUtilities.genStickerLayoutPCL(
						laStickerFormat.getPL_HORI(),
						laStickerFormat.getPL_VERT(),
						VOID,
						laStickerFormat.getPL_JUST(),
						laStickerFormat.getPL_FONT(),
						laStickerFormat.getPL_FONT_SIZE(),
						laStickerFormat.getPL_STROKE());
		}
		
		if (lbWATokenTrailer)
		{
			// VOID WINDSHIELD STICKER 
			caRpt.print(
					lsStrVoidWindshield,
					0,
					lsStrVoidWindshield.length());
			caRpt.print(
					lsDoNotUseWindshield,
					0,
					lsDoNotUseWindshield.length());

			caRpt.print(
					lsNoUseWindshield,
					0,
					lsNoUseWindshield.length());
			
			// VOID PLATE STICKER
			caRpt.print(lsStrVoidPlate, 0, lsStrVoidPlate.length());
			caRpt.print(lsDoNotUsePlate, 0, lsDoNotUsePlate.length());
			caRpt.print(lsNoUsePlate, 0, lsNoUsePlate.length());
		}
		else
		{
			// end defect 11138 
			String lsBarcode = "";
			String lsPlateNum = "";
			String lsExpmo = "";
			String lsExpyr = "";

			// defect 7079
			// Sticker layout change - Use PCLUtilities methods to generate PCl on the fly
			// instead of using hardcoded PCL in the Print class

			// defect 7151
			// Moved from the bottom so that sticker position would be known and used
			// to decide if generating the barcode is needed
			String lsItemcode = "";
			if (aaCTData.getStickers() == null
					|| aaCTData.getStickers().size() == 0)
			{
				if (UtilityMethods.isDTA(aaCTData.getTransCode()) ||lbWATokenTrailer)
				{
					lsItemcode =
						aaCTData
						.getVehicleInfo()
						.getRegData()
						.getRegStkrCd();
					if (lsItemcode == null)
					{
						lsItemcode = "US";
					}
				}
				else
				{
					return;
				}
			}
			else
			{
				ProcessInventoryData laInvData =
					(ProcessInventoryData) aaCTData.getStickers().get(0);
				lsItemcode = laInvData.getItmCd();
			}

			ItemCodesData laItemCodeData =
				ItemCodesCache.getItmCd(lsItemcode);
			// Get the associated char value for the PrntCd returned ie 
			// 76 = 'L'
			char lchStickerPosition = (char) laItemCodeData.getPrntCd();

			// only get barcode data for windshield stickers
			if (lchStickerPosition == ItemCodesCache.WINDSHIELD_STICKER)
			{
				// defect 7107
				// Moved dataToBarcode() from PCLUtilities to this class.
				// Had to change the call that was made to dataToPCL.
				// String barcode = PCLUtilities.dataToPCL(transData);
				// if the vector is empty, someone messed up and it
				// shouldn't print a sticker
				lsBarcode = PCLUtilities.dataToPCL(dataToBarcode(aaCTData));
				// end defect 7107
			}
			// end defect 7151

			/**
			 * County Name
			 */
			// defect 7113
			// VTR made approval to print the Res Comp County Name on the
			// sticker instead of the county where it was printed
			String lsCountyName = determineResCompCntyName();
			// end defect 7113

			// defect 7130
			// Change max size from 10 to 13
			if (lsCountyName.length() > 13)
			{
				lsCountyName = lsCountyName.substring(0, 13);
			}
			while (lsCountyName.length() < 13)
			{
				lsCountyName = lsCountyName + " ";
			}
			// end defect 7130

			/**
			 * Plate Number
			 */
			// defect 9145
			if (aaCTData.getAllocInvItms() != null)
			{
				// figure out the plate number - if a new plate was entered, 
				// we should grab that one if not, then just use the old one
				for (int i = 0; i < aaCTData.getAllocInvItms().size(); i++)
				{
					ProcessInventoryData laInvData =
						(ProcessInventoryData) aaCTData
						.getAllocInvItms()
						.get(
								i);
					String lsCode = laInvData.getItmCd();
					ItemCodesData laItemData =
						ItemCodesCache.getItmCd(lsCode);

					// defect 7233
					// HQ exp year was not showing up - made sticker consistant
					// with the way barcode gets the exp year
					String lsItmTrckngType = laItemData.getItmTrckngType();
					if (lsItmTrckngType.equals("P")
							|| lsItmTrckngType.equals("S"))
					{
						if (lsItmTrckngType.equals("P"))
						{
							lsPlateNum = laInvData.getInvItmNo();
							//break;
						}
						// If assigned InvItmYr, will use for RegExpYr  
						if (laInvData.getInvItmYr() != 0)
						{
							lsExpyr =
								Integer.toString(laInvData.getInvItmYr());
						}
					}
				}
			}
			// end defect 9145
			if (lsPlateNum == null || lsPlateNum.equals(""))
			{
				// defect 7103
				// Make sure to pick up the correct plate number if one is
				// supplied plateNum = transData.getVehicleInfo().
				// getRegData().getRegPltNo();
				if (aaCTData.getOwnrSuppliedPltNo() != null
						&& !aaCTData.getOwnrSuppliedPltNo().equals(""))
				{
					lsPlateNum = aaCTData.getOwnrSuppliedPltNo();
				}
				// defect 7942
				// use the current tow truck plate nbr (small) on the stkr if available
				else if (
						aaCTData.getTimedPermitData() != null
						&& aaCTData.getTimedPermitData().getTowTrkPltNo()
						!= null
						&& !aaCTData
						.getTimedPermitData()
						.getTowTrkPltNo()
						.equals(
						""))
				{
					lsPlateNum =
						aaCTData.getTimedPermitData().getTowTrkPltNo();
				}
				// end defect 7492
				else
				{
					lsPlateNum =
						aaCTData
						.getVehicleInfo()
						.getRegData()
						.getRegPltNo();
				}
				// end defect 7103
			}

			DealerTitleData laLastRecord = aaCTData.getDlrTtlData();

			/**
			 * Expiration Year
			 */
			if (lsExpyr.equals("") || lsExpyr.equals("0"))
			{
				if (aaCTData.getVehicleInfo().getRegData() != null)
				{
					if (UtilityMethods.isDTA(aaCTData.getTransCode())
							&& laLastRecord != null)
					{
						lsExpyr =
							Integer.toString(laLastRecord.getNewRegExpYr());
					}
					// defect 9145
					// Added else if for Special Plate Dealer Plates.
					else if (
							UtilityMethods.isSpecialPlates(
									aaCTData.getTransCode())
									&& aaCTData.getVehicleInfo().getSpclPltRegisData()
									!= null)
					{
						// defect 9864 
						// Modified in refactor of RegExpYr to PltExpYr  
						lsExpyr =
							Integer.toString(
									aaCTData
									.getVehicleInfo()
									.getSpclPltRegisData()
									.getPltExpYr());
						// end defect 9864 
					}
					// end defect 9145
					else
					{
						lsExpyr =
							Integer.toString(
									aaCTData
									.getVehicleInfo()
									.getRegData()
									.getRegExpYr());
					}
				}
			}

			lsExpyr = UtilityMethods.addPadding(lsExpyr, 4, "0");

			/**
			 * Expiration Month 
			 */
			if (aaCTData.getVehicleInfo().getRegData() != null)
			{
				if (UtilityMethods.isDTA(aaCTData.getTransCode())
						&& laLastRecord != null)
				{
					// defect 7496
					//  NewRegExpMo and NewRegExpYr were converted to integer
					//expmo = lastRecord.getNewRegExpMo();
					lsExpmo =
						Integer.toString(laLastRecord.getNewRegExpMo());
					// end defect 7496
				}
				// defect 9145
				// Added else if for Special Plate Dealer Plates.
				else if (
						UtilityMethods.isSpecialPlates(aaCTData.getTransCode())
						&& aaCTData.getVehicleInfo().getSpclPltRegisData()
						!= null)
				{
					// defect 9864 
					// Modified in refactor of RegExpMo to PltExpMo  
					lsExpmo =
						Integer.toString(
								aaCTData
								.getVehicleInfo()
								.getSpclPltRegisData()
								.getPltExpMo());
					// end defect 9864 
				}
				// end defect 9145
				else
				{
					lsExpmo =
						Integer.toString(
								aaCTData
								.getVehicleInfo()
								.getRegData()
								.getRegExpMo());
				}
			}

			if (lsVersion.equals(VERSION1))
			{
				lsExpmo = UtilityMethods.addPaddingRight(lsExpmo, 2, " ");
			}
			else
			{
				lsExpmo =
					UtilityMethods.addPadding(
							lsExpmo,
							2,
							CommonConstant.STR_ZERO);
			}
			// end defect 8829

			// defect 9145
			// For Special Plate dealer plate we will have a sticker and
			// will use the VIN position for the Dealer GDN.
			String lsVIN = CommonConstant.STR_SPACE_EMPTY;
			if (aaCTData.getVehicleInfo().getRegData() != null)
			{
				if (UtilityMethods.isSpecialPlates(aaCTData.getTransCode())
						&& aaCTData.getVehicleInfo().getSpclPltRegisData()
						!= null)
				{
					lsVIN =
						maskVIN(
								aaCTData
								.getVehicleInfo()
								.getSpclPltRegisData()
								.getPltOwnrDlrGDN());
				}
				else
				{
					lsVIN =
						maskVIN(
								aaCTData
								.getVehicleInfo()
								.getVehicleData()
								.getVin());
				}
			}
			// end defect 9145
			// end defect 7233

			laStickerFormat =
				StickerFormatData.getLayout(VIN + lsVersion);
			String lsWsVin = CommonConstant.STR_SPACE_EMPTY;
			if (laStickerFormat != null)
			{
				// defect 9145
				// Changed to use the variable for VIN so that we can put
				// the Dealer GDN number in that field for Special Plate.
				lsWsVin =
					PCLUtilities.genStickerLayoutPCL(
							laStickerFormat.getWS_HORI(),
							laStickerFormat.getWS_VERT(),
							lsVIN,
							laStickerFormat.getWS_JUST(),
							laStickerFormat.getWS_FONT(),
							laStickerFormat.getWS_FONT_SIZE(),
							laStickerFormat.getWS_STROKE());
				// end defect 9145
			}

			laStickerFormat =
				StickerFormatData.getLayout(BARCODE + lsVersion);
			String lsWsBarcode = CommonConstant.STR_SPACE_EMPTY;
			if (laStickerFormat != null)
			{
				lsWsBarcode =
					PCLUtilities.genHorizontalPCL(
							laStickerFormat.getWS_HORI())
							+ PCLUtilities.genVerticalPCL(
									laStickerFormat.getWS_VERT())
									+ PCLUtilities.ZERO_LEFT_MARGIN
									+ CommonConstant.STR_SPACE_ONE
									+ lsBarcode;
			}

			// defect 8829
			// Broke the Expiration Month and Year into two lines b/c
			// using a space as we did originally will not work for the 
			// new sticker because the texas logo is two big.
			laStickerFormat =
				StickerFormatData.getLayout(EXPMO + lsVersion);
			String lsWSExpMo = CommonConstant.STR_SPACE_EMPTY;
			if (laStickerFormat != null)
			{
				lsWSExpMo =
					PCLUtilities.genStickerLayoutPCL(
							laStickerFormat.getWS_HORI(),
							laStickerFormat.getWS_VERT(),
							lsExpmo,
							laStickerFormat.getWS_JUST(),
							laStickerFormat.getWS_FONT(),
							laStickerFormat.getWS_FONT_SIZE(),
							laStickerFormat.getWS_STROKE());
			}

			laStickerFormat =
				StickerFormatData.getLayout(EXPYR + lsVersion);
			String lsWSExpYr = CommonConstant.STR_SPACE_EMPTY;
			if (laStickerFormat != null)
			{
				lsWSExpYr =
					PCLUtilities.genStickerLayoutPCL(
							laStickerFormat.getWS_HORI(),
							laStickerFormat.getWS_VERT(),
							lsExpyr.substring(2, 4),
							laStickerFormat.getWS_JUST(),
							laStickerFormat.getWS_FONT(),
							laStickerFormat.getWS_FONT_SIZE(),
							laStickerFormat.getWS_STROKE());
			}
			// end defect 8829

			laStickerFormat = StickerFormatData.getLayout(CNTY + lsVersion);
			String lsWsCounty = CommonConstant.STR_SPACE_EMPTY;
			if (laStickerFormat != null)
			{
				lsWsCounty =
					PCLUtilities.genStickerLayoutPCL(
							laStickerFormat.getWS_HORI(),
							laStickerFormat.getWS_VERT(),
							lsCountyName.toUpperCase(),
							laStickerFormat.getWS_JUST(),
							laStickerFormat.getWS_FONT(),
							laStickerFormat.getWS_FONT_SIZE(),
							laStickerFormat.getWS_STROKE());
			}

			laStickerFormat = StickerFormatData.getLayout(PLT + lsVersion);
			String lsWsPlate = CommonConstant.STR_SPACE_EMPTY;
			if (laStickerFormat != null)
			{
				lsWsPlate =
					PCLUtilities.genStickerLayoutPCL(
							laStickerFormat.getWS_HORI(),
							laStickerFormat.getWS_VERT(),
							lsPlateNum,
							laStickerFormat.getWS_JUST(),
							laStickerFormat.getWS_FONT(),
							laStickerFormat.getWS_FONT_SIZE(),
							laStickerFormat.getWS_STROKE());
			}

			// defect 8829
			// Removed the NEW_LINE's between each field because it
			// could cause the items to drop to a separate page.
			String lsStrWindshieldSticker =
				lsWsBarcode
				+ NEW_LINE
				+ lsWsVin
				+ CommonConstant.STR_SPACE_ONE
				+ lsWSExpMo
				+ lsWSExpYr
				+ lsWsCounty
				+ lsWsPlate;
			// end defect 8829

			laStickerFormat = StickerFormatData.getLayout(VIN + lsVersion);
			String lsPlVin = CommonConstant.STR_SPACE_EMPTY;
			if (laStickerFormat != null)
			{
				// defect 9145
				// Changed to use the variable for VIN so that we can put
				// the Dealer GDN number in that field for Special Plate.
				lsPlVin =
					PCLUtilities.genStickerLayoutPCL(
							laStickerFormat.getPL_HORI(),
							laStickerFormat.getPL_VERT(),
							lsVIN,
							laStickerFormat.getPL_JUST(),
							laStickerFormat.getPL_FONT(),
							laStickerFormat.getPL_FONT_SIZE(),
							laStickerFormat.getPL_STROKE());
				// end defect 9145
			}

			// defect 8829
			// Broke the Expiration Month and Year into two lines b/c
			// using a space as we did originally will not work for the 
			// new sticker because the texas logo is two big.
			laStickerFormat =
				StickerFormatData.getLayout(EXPMO + lsVersion);
			String lsPlExpMo = CommonConstant.STR_SPACE_EMPTY;
			if (laStickerFormat != null)
			{
				lsPlExpMo =
					PCLUtilities.genStickerLayoutPCL(
							laStickerFormat.getPL_HORI(),
							laStickerFormat.getPL_VERT(),
							lsExpmo,
							laStickerFormat.getPL_JUST(),
							laStickerFormat.getPL_FONT(),
							laStickerFormat.getPL_FONT_SIZE(),
							laStickerFormat.getPL_STROKE());
			}

			laStickerFormat =
				StickerFormatData.getLayout(EXPYR + lsVersion);
			String lsPlExpYr = CommonConstant.STR_SPACE_EMPTY;
			if (laStickerFormat != null)
			{
				lsPlExpYr =
					PCLUtilities.genStickerLayoutPCL(
							laStickerFormat.getPL_HORI(),
							laStickerFormat.getPL_VERT(),
							lsExpyr.substring(2, 4),
							laStickerFormat.getPL_JUST(),
							laStickerFormat.getPL_FONT(),
							laStickerFormat.getPL_FONT_SIZE(),
							laStickerFormat.getPL_STROKE());
			}
			// end defect 8829

			laStickerFormat = StickerFormatData.getLayout(CNTY + lsVersion);
			String lsPlCounty = CommonConstant.STR_SPACE_EMPTY;
			if (laStickerFormat != null)
			{
				lsPlCounty =
					PCLUtilities.genStickerLayoutPCL(
							laStickerFormat.getPL_HORI(),
							laStickerFormat.getPL_VERT(),
							lsCountyName.toUpperCase(),
							laStickerFormat.getPL_JUST(),
							laStickerFormat.getPL_FONT(),
							laStickerFormat.getPL_FONT_SIZE(),
							laStickerFormat.getPL_STROKE());
			}

			laStickerFormat = StickerFormatData.getLayout(PLT + lsVersion);
			String lsPlPlate = CommonConstant.STR_SPACE_EMPTY;
			if (laStickerFormat != null)
			{
				lsPlPlate =
					PCLUtilities.genStickerLayoutPCL(
							laStickerFormat.getPL_HORI(),
							laStickerFormat.getPL_VERT(),
							lsPlateNum,
							laStickerFormat.getPL_JUST(),
							laStickerFormat.getPL_FONT(),
							laStickerFormat.getPL_FONT_SIZE(),
							laStickerFormat.getPL_STROKE());
			}

			// defect 8829
			// Removed the NEW_LINE's between each field because it
			// could cause the items to drop to a separate page.
			String lsStrPlateSticker =
				lsPlVin
				+ CommonConstant.STR_SPACE_ONE
				+ lsPlExpYr
				+ lsPlExpMo
				+ lsPlCounty
				+ lsPlPlate;
			// end defect 8829

			// defect 11138 
			// Moved to beginning of method  
			//		laStickerFormat = StickerFormatData.getLayout(VOID + lsVersion);
			//		String lsStrVoidWindshield = CommonConstant.STR_SPACE_EMPTY;
			//		String lsStrVoidPlate = CommonConstant.STR_SPACE_EMPTY;
			//		if (laStickerFormat != null)
			//		{
			//			lsStrVoidWindshield =
			//				NEW_LINE
			//					+ PCLUtilities.genStickerLayoutPCL(
			//						laStickerFormat.getWS_HORI(),
			//						laStickerFormat.getWS_VERT(),
			//						VOID,
			//						laStickerFormat.getWS_JUST(),
			//						laStickerFormat.getWS_FONT(),
			//						laStickerFormat.getWS_FONT_SIZE(),
			//						laStickerFormat.getWS_STROKE());
			//
			//			lsStrVoidPlate =
			//				NEW_LINE
			//					+ PCLUtilities.genStickerLayoutPCL(
			//						laStickerFormat.getPL_HORI(),
			//						laStickerFormat.getPL_VERT(),
			//						VOID,
			//						laStickerFormat.getPL_JUST(),
			//						laStickerFormat.getPL_FONT(),
			//						laStickerFormat.getPL_FONT_SIZE(),
			//						laStickerFormat.getPL_STROKE());
			//		}
			// defect 8829
			// Added for sticker version 2.  If it is sticker version 1 then
			// the getLayout() call will return a null and this element
			// will not be printed
			//		laStickerFormat =
			//			StickerFormatData.getLayout(DONOTUSE + lsVersion);
			//		String lsDoNotUseWindshield = CommonConstant.STR_SPACE_EMPTY;
			//		String lsDoNotUsePlate = CommonConstant.STR_SPACE_EMPTY;
			//		if (laStickerFormat != null)
			//		{
			//			lsDoNotUseWindshield =
			//				PCLUtilities.genStickerLayoutPCL(
			//					laStickerFormat.getWS_HORI(),
			//					laStickerFormat.getWS_VERT(),
			//					DONOTUSE,
			//					laStickerFormat.getWS_JUST(),
			//					laStickerFormat.getWS_FONT(),
			//					laStickerFormat.getWS_FONT_SIZE(),
			//					laStickerFormat.getWS_STROKE());
			//
			//			lsDoNotUsePlate =
			//				PCLUtilities.genStickerLayoutPCL(
			//					laStickerFormat.getPL_HORI(),
			//					laStickerFormat.getPL_VERT(),
			//					DONOTUSE,
			//					laStickerFormat.getPL_JUST(),
			//					laStickerFormat.getPL_FONT(),
			//					laStickerFormat.getPL_FONT_SIZE(),
			//					laStickerFormat.getPL_STROKE());
			//		}
			//
			//		laStickerFormat =
			//			StickerFormatData.getLayout(NOUSE + lsVersion);
			//		String lsNoUseWindshield = CommonConstant.STR_SPACE_EMPTY;
			//		String lsNoUsePlate = CommonConstant.STR_SPACE_EMPTY;
			//		if (laStickerFormat != null)
			//		{
			//			lsNoUseWindshield =
			//				PCLUtilities.genStickerLayoutPCL(
			//					laStickerFormat.getWS_HORI(),
			//					laStickerFormat.getWS_VERT(),
			//					NOUSE,
			//					laStickerFormat.getWS_JUST(),
			//					laStickerFormat.getWS_FONT(),
			//					laStickerFormat.getWS_FONT_SIZE(),
			//					laStickerFormat.getWS_STROKE());
			//
			//			lsNoUsePlate =
			//				PCLUtilities.genStickerLayoutPCL(
			//					laStickerFormat.getPL_HORI(),
			//					laStickerFormat.getPL_VERT(),
			//					NOUSE,
			//					laStickerFormat.getPL_JUST(),
			//					laStickerFormat.getPL_FONT(),
			//					laStickerFormat.getPL_FONT_SIZE(),
			//					laStickerFormat.getPL_STROKE());
			//		}
			// defect 8829

			if (lchStickerPosition == ItemCodesCache.PLATE_STICKER)
			{
				caRpt.print(
						lsStrPlateSticker,
						0,
						lsStrPlateSticker.length());
				caRpt.print(
						lsStrVoidWindshield,
						0,
						lsStrVoidWindshield.length());
				caRpt.print(
						lsDoNotUseWindshield,
						0,
						lsDoNotUseWindshield.length());
				caRpt.print(
						lsNoUseWindshield,
						0,
						lsNoUseWindshield.length());
			}
			else if (
					lchStickerPosition == ItemCodesCache.WINDSHIELD_STICKER)
			{
				caRpt.print(
						lsStrWindshieldSticker,
						0,
						lsStrWindshieldSticker.length());
				caRpt.print(lsStrVoidPlate, 0, lsStrVoidPlate.length());
				caRpt.print(lsDoNotUsePlate, 0, lsDoNotUsePlate.length());
				caRpt.print(lsNoUsePlate, 0, lsNoUsePlate.length());
			}
		}
	}

	/**
	 * Retrieve the RegClassCdDesc from the Common Fees cache table.
	 * 
	 * @param aiRegType int
	 * @param aiEffDate int
	 * @param asTransCd String
	 */
	public String getCacheRegClassDesc(
		int aiRegType,
		int aiEffDate,
		String asTransCd)
	{
		// If trans code is an Accounting or Vehicle Inquiry use current date.
		// Mimic method FrmRegistrationREG003.setData()
		if (isAcctTransCd(asTransCd)
			|| isRegisTransCd(asTransCd)
			|| isTitleTransCd(asTransCd)
			|| isVehInqTransCd(asTransCd))
		{
			aiEffDate = new RTSDate().getYYYYMMDDDate();
		}
		CommonFeesData laFeeData =
			CommonFeesCache.getCommonFee(aiRegType, aiEffDate);
		if (laFeeData != null)
		{
			return laFeeData.getRegClassCdDesc();
		}
		else
		{
			return "";
		}
	}

	/**
	 * getCurrX
	 * 
	 * @return int
	 */
	public int getCurrX()
	{
		int liCurrX = caRpt.getCurrX();
		return liCurrX;
	}

	/**
	 * Get and return the Form 31 number.
	 * 
	 * @param  aaTransData Object
	 * @return String
	 */
	protected String getForm31No(Object aaTransData)
	{
		String lsForm31No = "";
		Vector lvInvItm = new Vector();
		CompleteTransactionData laTransData =
			(CompleteTransactionData) aaTransData;
		lvInvItm = laTransData.getAllocInvItms();
		// Vector of Inventory Items
		if (lvInvItm != null && lvInvItm.size() != 0)
		{
			for (int i = 0; i < lvInvItm.size(); i++)
			{
				lsForm31No =
					getInvItmNo(laTransData, BLANK_TRCKNG_TYPE, i);
				if (!lsForm31No.equals(""))
				{
					break;
				}
			}
		}
		return lsForm31No == null ? "" : lsForm31No;
	}

	/**
	 * Return the correct sticker number.
	 * pass args:Complete Transaction Data Object
	 * InvtrackingType P =Plate,	S =	Sticker Record number
	 * Note:This method gets the Registration Data base on Tracking Type.
	 * If new inventory was issued,this methods get it from
	 * processInventoryData.
	 * 
	 * @param aaTransData Object
	 * @param asTrckngType String
	 * @param aiIndex int
	 * @return String
	 */
	protected String getInvItmNo(
		Object aaTransData,
		String asTrckngType,
		int aiIndex)
	{
		Vector lvInvItm = new Vector();
		String lsInvItmNo = "";
		String lsItmTrckngType = "";
		CompleteTransactionData laTransData =
			(CompleteTransactionData) aaTransData;
		ProcessInventoryData laInvItms = new ProcessInventoryData();
		lvInvItm = laTransData.getAllocInvItms();
		// Vector of Inventory Items
		try
		{
			if (!(lvInvItm == null || lvInvItm.size() == 0)
				&& lvInvItm.size() > aiIndex)
			{
				laInvItms =
					(ProcessInventoryData) lvInvItm.elementAt(aiIndex);
				lsItmTrckngType =
					getCacheItmTrkngType(laInvItms.getItmCd());
				if (lsItmTrckngType.equals(asTrckngType))
				{
					lsInvItmNo = laInvItms.getInvItmNo();
				}
			}
		}
		catch (NullPointerException aeNPEx)
		{
			System.out.println(
				aeNPEx + " refer to ReceiptTemplate/getPermitNo");
		}
		return lsInvItmNo;
	}

	/**
	 * getReceipt
	 * 
	 * @return StringBuffer
	 */
	public StringBuffer getReceipt()
	{
		StringBuffer lsReportStr = caRpt.getReport();

		return lsReportStr;
	}

//	/**
//	 * Goes to the assigned workstations cache to retrieve the
//	 * sticker version number for this workstation.  We will defecult to
//	 * sticker version 1 if we get an error getting the sticker version.
//	 * 
//	 * @return int
//	 */
//	private int getStkrVersion()
//	{
//		int liVersion = 1;
//		try
//		{
//			liVersion =
//				AssignedWorkstationIdsCache
//					.getAsgndWsId(
//						SystemProperty.getOfficeIssuanceNo(),
//						SystemProperty.getSubStationId(),
//						SystemProperty.getWorkStationId())
//					.getStkrVersionNo();
//		}
//		catch (Exception aeEx)
//		{
//			// Instantiate the RTS exception so it will be logged
//			new RTSException(RTSException.JAVA_ERROR, aeEx);
//		}
//		return liVersion;
//	}

	/**
	 * Accounting 
	 * 
	 * @param asTransCd String
	 */
	public boolean isAcctTransCd(String asTransCd)
	{
		boolean lbTransCd = false;
		if (asTransCd.equals(TRANSCD_HOTDED)
			|| asTransCd.equals(TRANSCD_REFUND)
			|| asTransCd.equals(TRANSCD_HOTCK)
			|| asTransCd.equals(TRANSCD_CKREDM)
			|| asTransCd.equals(TRANSCD_RFCASH))
		{
			lbTransCd = true;
		}
		return lbTransCd;
	}

	/**
	 * Additional Sales Tax
	 * 
	 * @param asTransCd String
	 */
	public boolean isAddlSalesTaxTransCd(String asTransCd)
	{
		boolean lbTransCd = false;
		if (asTransCd.equals(TRANSCD_ADLSTX))
		{
			lbTransCd = true;
		}
		return lbTransCd;
	}

	/**
	 * Dealer Title Events
	 * 
	 * @param asTransCd String
	 * @return boolean
	 */
	public boolean isDealerTitleTransCd(String asTransCd)
	{
		boolean lbTransCd = false;
		if (asTransCd.equals(TRANSCD_DTANTD)
			|| asTransCd.equals(TRANSCD_DTANTK)
			|| asTransCd.equals(TRANSCD_DTAORD)
			|| asTransCd.equals(TRANSCD_DTAORK))
		{
			lbTransCd = true;
		}
		return lbTransCd;
	}

	/**
	 * Disabled Placard 
	 * 
	 * @param asTransCd String
	 * @return boolean
	 */
	public boolean isDisablePlacardTransCd(String asTransCd)
	{
		// defect 9831 
		//		boolean lbTransCd = false;
		//		// defect 8268
		//		//if (asTransCd.equals(TRANSCD_PDC)
		//		//  	|| asTransCd.equals(TRANSCD_TDC))
		//		if (asTransCd.equals(TRANSCD_BPM)
		//			|| asTransCd.equals(TRANSCD_BTM)
		//			|| asTransCd.equals(TRANSCD_RPNM)
		//			|| asTransCd.equals(TRANSCD_RTNM))
		//			// end defect 8268
		//		{
		//			lbTransCd = true;
		//		}
		//		return lbTransCd;
		// end defect 9831 
		return UtilityMethods.isDsabldPlcrdEvent(asTransCd);
	}

	/**
	 * Drivers Ed
	 * 
	 * @param asTransCd String
	 * @return boolean
	 */
	public boolean isDrvEdTransCd(String asTransCd)
	{
		boolean lbTransCd = false;
		if (asTransCd.equals(TRANSCD_DRVED))
		{
			lbTransCd = true;
		}
		return lbTransCd;
	}

	/**
	 * Non-Resident Agriculature 
	 * 
	 * @param avTransCd String
	 * @return boolean
	 */
	public boolean isNonResidtAgrTransCd(String avTransCd)
	{
		boolean lbTransCd = false;
		if (avTransCd.equals(TRANSCD_NRIPT)
			|| avTransCd.equals(TRANSCD_NROPT))
		{
			lbTransCd = true;
		}
		return lbTransCd;
	}

	/**
	 * Regional Collection.
	 * 
	 * @param avTransCd String
	 */
	public boolean isRegColTransCd(String avTransCd)
	{
		boolean lbTransCd = false;
		if (avTransCd.equals(TRANSCD_RGNCOL)
			|| avTransCd.equals(TransCdConstant.CCO))
		{
			lbTransCd = true;
		}
		return lbTransCd;
	}

	/**
	 * Registration Event.
	 * 
	 * @param asTransCd String
	 */
	public boolean isRegisTransCd(String asTransCd)
	{
		// defect 10745 
		return (
			asTransCd.equals(TRANSCD_DUPL)
				|| asTransCd.equals(TRANSCD_CORREG)
				|| asTransCd.equals(TRANSCD_RENEW)
				|| asTransCd.equals(TRANSCD_REPL)
				|| asTransCd.equals(TRANSCD_IRENEW)
				|| asTransCd.equals(TRANSCD_PAWT)
				|| asTransCd.equals(TRANSCD_EXCH)
				|| asTransCd.equals(TransCdConstant.SBRNW)
				|| asTransCd.equals(TransCdConstant.WRENEW));
		// end defect 10745 
	}

	/**
	 * Temporary Additional Weight
	 * 
	 * @param asTransCd String
	 * @return boolean
	 */
	public boolean isTempAddlWtTransCd(String asTransCd)
	{
		boolean lbTransCd = false;
		if (asTransCd.equals(TRANSCD_TAWPT))
		{
			lbTransCd = true;
		}
		return lbTransCd;
	}

	/**
	 * Temporary and Permanent Additional Weight
	 * 
	 * @param asTransCd String
	 * @return boolean
	 */
	public boolean isTempPermAddlWtTransCd(String asTransCd)
	{
		boolean lbTransCd = false;
		if (asTransCd.equals(TRANSCD_TAWPT)
			|| asTransCd.equals(TRANSCD_PAWT))
		{
			lbTransCd = true;
		}
		return lbTransCd;
	}

	/**
	 * Title Events
	 * 
	 * @param asTransCd String
	 * @return boolean
	 */
	public boolean isTitleTransCd(String asTransCd)
	{
		boolean lbTransCd = false;
		if (asTransCd.equals(TRANSCD_TITLE)
			|| asTransCd.equals(TRANSCD_NONTTL)
			|| asTransCd.equals(TRANSCD_CORTTL)
			|| asTransCd.equals(TRANSCD_REJCOR))
		{
			lbTransCd = true;
		}
		return lbTransCd;
	}

	/**
	 * Tow Truck Event
	 * 
	 * @param avTransCd String
	 * @return boolean
	 */
	public boolean isTowTrkTransCd(String avTransCd)
	{
		boolean lbTransCd = false;
		if (avTransCd.equals(TRANSCD_TOWP))
		{
			lbTransCd = true;
		}
		return lbTransCd;
	}

	/**
	 * Vehicle Inquiry 
	 * 
	 * @param avTransCd Sring
	 * @return boolean
	 */
	public boolean isVehInqTransCd(String asTransCd)
	{
		// TODO DEFECT 11052 VTR275 REMOVE 
		//		boolean lbTransCd = false;
		//		if (asTransCd.equals(TRANSCD_VEHINQ))
		//		{
		//			lbTransCd = true;
		//		}
		//		return lbTransCd;
		// TODO DEFECT 11052 END VTR275 REMOVE
		return UtilityMethods.isVehInqTransCd(asTransCd); 
	}

	/**
	 * maskVIN
	 * 
	 * @param asVIN String
	 * @return String
	 */
	private String maskVIN(String asVIN)
	{
		String lsActualVIN;

		if (asVIN == null)
		{
			lsActualVIN = "";
		}
		else if (asVIN.length() < 9)
		{
			lsActualVIN = asVIN;
		}
		else
		{
			lsActualVIN = asVIN.substring(asVIN.length() - 8);
			// defect 7079
			// Sticker Layout and design change.  Not using * when
			// masking vin
			// while (actualVIN.length() < length)
			// actualVIN = "*" + actualVIN;
			// end defect 7079
		}
		return lsActualVIN;
	}

	/**
	* Merge Owner and Regis decription for temperary Additional weight.
	* 
	* @param avOwnrInfo Vector
	* @param avRegisInfo Vector
	* @return Vector
	*/
	protected Vector mergeOwnrRegisInfo(
		Vector avOwnrInfo,
		Vector avRegisInfo)
	{
		int liSize = avOwnrInfo.size();
		int liRegDescIndex = 0;
		String lsInputStr = "";
		Vector lvReturn = new Vector();
		// Set size of the return vector
		if (liSize < 5)
		{
			for (int i = liSize; i < 5; i++)
			{
				liSize++;
			}
		}
		// Move the first and second owner info lines to the lvReturn
		// vector
		for (int i = 0; i < 4; i++)
		{
			lsInputStr = (String) avOwnrInfo.elementAt(i);
			lvReturn.addElement(lsInputStr);
		}
		// Merge Owner info and Regis Description
		for (int i = 4; i <= liSize; i++)
		{
			if (i < avOwnrInfo.size())
			{
				lsInputStr = (String) avOwnrInfo.elementAt(i);
				if (lsInputStr.equals(""))
				{
					if (liRegDescIndex < 4)
					{
						lsInputStr =
							(String) avRegisInfo.elementAt(
								liRegDescIndex);
						lvReturn.addElement(lsInputStr);
						liRegDescIndex++;
					}
					else
					{
						lvReturn.addElement("");
					}
				}
				else
				{
					lvReturn.addElement(lsInputStr);
				}
			}
			else
			{
				lvReturn.addElement("");
				if (liRegDescIndex < 4)
				{
					lsInputStr =
						(String) avRegisInfo.elementAt(liRegDescIndex);
					lvReturn.addElement(lsInputStr);
					liRegDescIndex++;
				}
			}
		} // end for loop
		// defect 9085
		for (int x = liRegDescIndex; x < avRegisInfo.size(); x++)
		{
			lsInputStr = (String) avRegisInfo.elementAt(x);
			lvReturn.addElement(""); // this is to cause the next 
			//	element below to print in 
			// correct column 
			lvReturn.addElement(lsInputStr);
		}
		// defect 9085

		return lvReturn;
	}

	/**
	 * numOfUnderScores
	 * 
	 * @param  aiUnderScores int
	 * @return String
	 */
	public String numOfUnderScores(int aiUnderScores)
	{
		String lsCharLngth = "";
		for (int i = 0; i < aiUnderScores; i++)
		{
			lsCharLngth = lsCharLngth + "_";
		}
		return lsCharLngth;
	}

	/**
	 * Parse out the Owner Information and place in Vectors to be written
	 * to the Receipt.
	 * 
	 * @param aaOwnrTtlInfo Object
	 * @param aaVehData Object
	 * @return String
	 */
	protected Vector parseOwnrInfo(
		Object aaOwnrTtlInfo,
		Object aaVehData)
	{
		int liMaxOwnrCnt = 5;
		Vector lvOwnrInfo = new Vector();
		Vector lvRnewal = new Vector(5);
		Vector lvReturn = new Vector(5);
		OwnerData laOwnrInfo = (OwnerData) aaOwnrTtlInfo;
		AddressData laOwnrAddress =
			(AddressData) laOwnrInfo.getAddressData();
		RegistrationData laRnewal = (RegistrationData) aaVehData;
		AddressData laRnwlAddress =
			(AddressData) laRnewal.getRenwlMailAddr();

		// Get Renewal Recipient data
		// If Transaction is a tempAddlWt, do not get Renewal Recipient
		// data
		// Only look at transcd for defect 5184, and print renewal 
		// recipient info even when its blank for all other trans codes.
		// defect 6065
		// Do not attempt to refer to laRnwlAddress if it is null
		// added to if statement
		if (isTempAddlWtTransCd(caCTData.getTransCode())
			|| laRnwlAddress == null)
		{
			// end defect 6065
			lvRnewal.addElement(null);
			lvRnewal.addElement(null);
			lvRnewal.addElement(null);
		}
		else
		{
			String lsName = laRnewal.getRecpntName();
			lsName = lsName == null ? "" : lsName.trim();

			// defect 6540 
			// Reorganize 6121/5184 
			// defect 6121 - if there is Recipient Name but no Recipient Address,
			// ver 5.1.4    use Owner Address as Recipient Address
			if (!(lsName.equals("")
				&& laRnwlAddress.getSt1().equals("")))
			{
				if (lsName.equals(""))
				{
					lsName = laOwnrInfo.getName1();
				}
				lvRnewal.addElement(lsName);

				// defect 10112 
				AddressData laAddrData =
					laRnwlAddress.isPopulated()
						? laRnwlAddress
						: laOwnrAddress;

				lvRnewal.addAll(laAddrData.getAddressVector(false));
			}
			// end defect 6540
		}
		// Get Owner data
		lvOwnrInfo.addAll(laOwnrInfo.getNameAddressVector());

		// Add Recipient/Owner info to the vector
		// If Receipt street 1 is blank, format Owner only
		// changed the index to check  0 - receipient info would be here - defect 5184 again!
		// defect 6121. REPLACE THIS:if (lvRnewal == null || lvRnewal.size() == 0). 
		if (lvRnewal.size() == 0
			|| lvRnewal.elementAt(0) == null
			|| lvRnewal.elementAt(0).equals(""))
		{
			// defect 7540
			//lvReturn.addElement(csOwnrAddrText);

			if (caCTData.getTransCode().equals(TransCdConstant.SBRNW))
			{
				lvReturn.addElement(SBRNW_ADDR_TEXT);
			}
			// defect 11137 
			// WebAgent Heading will continue to print for records where 
			//   the "Owner Information" belongs to the WebAgent (Subcontractor)  
			else if (caCTData.getTransCode().equals(TransCdConstant.WRENEW) 
					&& caCTData.isWRENEWSubconAddr())
			{
				lvReturn.addElement(WRENEW_ADDR_TEXT);
			}
			// end defect 11137 
			// defect 9126
			else if (
				UtilityMethods.isSpecialPlates(
					caCTData.getTransCode()))
			{
				lvReturn.addElement(CUST_ADDR_TEXT);
			}
			// end defect 9126
			else
			{
				lvReturn.addElement(OWNR_ADDR_TEXT);
			}
			// end defect 7540
			lvReturn.addElement("");
			for (int i = 0; i < lvOwnrInfo.size(); i++)
			{
				if (!(lvOwnrInfo.elementAt(i) == null
					|| lvOwnrInfo.elementAt(i).equals("")))
				{
					lvReturn.addElement(lvOwnrInfo.elementAt(i));
					lvReturn.addElement("");
				}
			}
		} // end if
		// If Receipt name and street 1 is not blank, format 
		// Recipient/Owner info 
		else if (
			lvRnewal.elementAt(0) != null
				|| !lvRnewal.elementAt(0).equals("")
				&& (lvRnewal.elementAt(1) != null
					|| !lvRnewal.elementAt(1).equals("")))
		{
			lvReturn.addElement(RENEW_RCPT_TEXT);
			lvReturn.addElement(OWNR_ADDR_TEXT);
			for (int i = 0; i < liMaxOwnrCnt; i++)
			{
				if (i < lvRnewal.size())
				{
					lvReturn.addElement(lvRnewal.elementAt(i));
				}
				else
				{
					lvReturn.addElement("");
				}
				if (i < lvOwnrInfo.size())
				{
					lvReturn.addElement(lvOwnrInfo.elementAt(i));
				}
				else
				{
					lvReturn.addElement("");
				}
			}
		} // end else
		// If Receipt name is not blank and street 1 is blank, 
		// format Receipt's name/Owner address
		else if (
			!(lvRnewal.elementAt(0) == null
				|| lvRnewal.elementAt(0).equals(""))
				&& (lvRnewal.elementAt(1) == null
					|| lvRnewal.elementAt(1).equals("")))
		{
			lvReturn.addElement(RENEW_RCPT_TEXT);
			lvReturn.addElement(OWNR_ADDR_TEXT);
			lvReturn.addElement(lvRnewal.elementAt(0));
			lvReturn.addElement(lvOwnrInfo.elementAt(0));
			for (int i = 1; i < liMaxOwnrCnt; i++)
			{
				if (i < lvOwnrInfo.size())
				{
					lvReturn.addElement(lvOwnrInfo.elementAt(i));
					lvReturn.addElement(lvOwnrInfo.elementAt(i));
				}
			}
		}
		return lvReturn;
	}

	/**
	* Parse the owner permit Info.
	* 
	* @param aaOwnrTtlInfo Object
	* @return Vector
	*/
	protected Vector parseOwnrPermitInfo(Object aaTransData)
	{
		try
		{
			// defect 10112 
			// String lsZipCd;
			// String lsCityZip;
			// end defect 10112 
			Vector lvReturn = new Vector(5);
			// parse TransData object
			CompleteTransactionData laTransData =
				(CompleteTransactionData) aaTransData;
			TimedPermitData laTimedPermit =
				(TimedPermitData) laTransData.getTimedPermitData();
			MFVehicleData laMFVehData =
				(MFVehicleData) laTransData.getVehicleInfo();
			OwnerData laOwnrInfo =
				(OwnerData) laMFVehData.getOwnerData();
			AddressData laOwnrAddress =
				(AddressData) laOwnrInfo.getAddressData();

			// Determine Name/Customer title
			// defect 3720
			// removed check for nonResidtArgTransCd in following if, &
			// add (lTransData.getTransCode().equals(TransCdConstant.NRIPT) check here and 
			// added (lTransData.getTransCode().equals(TransCdConstant.NROPT) the last else if  in the following if
			// defect 10491 
			if (isPermitTransCd(laTransData.getTransCode()))
			{
				lvReturn.addElement(APPL_ADDR_TEXT);
				lvReturn.addElement("");
			}
			else if (
				laTransData.getTransCode().equals(
					TransCdConstant.NRIPT))
			{
				// end defect 10491
				lvReturn.addElement(CUST_ADDR_TEXT);
				lvReturn.addElement("");
			}
			// defect 9831
			//			else if (
			//				isDisablePlacardTransCd(laTransData.getTransCode()))
			//			{
			//				lvReturn.addElement(CUST_NAME_TEXT);
			//				lvReturn.addElement("");
			//			}
			else if (
				isDisablePlacardTransCd(laTransData.getTransCode()))
			{
				// Add blank line before "Name" header 
				lvReturn.addElement("");
				lvReturn.addElement("");
				DisabledPlacardCustomerData laDPAData =
					laTimedPermit.getDPCustData();
				lvReturn.addElement(
					laDPAData.isInstitution()
						? INSTITUTION_NAME_ADDR
						: DSABLD_PLCRD_NAME_ADDR);
				lvReturn.addElement("");
			}
			// end defect 9831 
			else if (
				isTowTrkTransCd(laTransData.getTransCode())
					|| isTempAddlWtTransCd(laTransData.getTransCode()))
			{
				lvReturn.addElement(OWNR_ADDR_TEXT);
				lvReturn.addElement("");
			}
			else if (
				laTransData.getTransCode().equals(
					TransCdConstant.NROPT))
			{
				lvReturn.addElement(CUST_ADDR_TEXT);
				lvReturn.addElement(
					ENTRY_POINT_TEXT
						+ laTimedPermit.getEntryOriginPnt());
			}
			// Get Owner data
			String lsName1 = laOwnrInfo.getName1();
			// defect 9831 
			// This prints the whole name, including middle initial 
			if (isDisablePlacardTransCd(laTransData.getTransCode()))
			{
				lsName1 = laTimedPermit.getDPCustData().getOwnerName();
			}
			// defect 10491 
			else if (isPermitTransCd(laTransData.getTransCode()))
			{
				lsName1 =
					((PermitData) laTimedPermit)
						.getCustomerData()
						.getCustNameData()
						.getCustName();
				laOwnrAddress =
					((PermitData) laTimedPermit)
						.getCustomerData()
						.getAddressData();
			}
			// end defect 10491 

			// end defect 9831 
			lvReturn.addElement(lsName1);
			lvReturn.addElement("");
			// defect 9831 
			// Disabled Placard now collects address information 
			//if (!(isDisablePlacardTransCd(laTransData.getTransCode())))
			//{
			String lsName2 = laOwnrInfo.getName2();
			if (!(lsName2.equals("") || lsName2 == null))
			{
				lvReturn.addElement(lsName2);
				lvReturn.addElement("");
			}
			// defect 10112
			// TODO Future investigation:  For some reason, this needs 
			//  a space between each element. Else, street2 if off. 

			Vector lvAddr = laOwnrAddress.getAddressVector(false);
			for (int i = 0; i < lvAddr.size(); i++)
			{
				lvReturn.addElement(lvAddr.elementAt(i));
				lvReturn.addElement("");
			}
			//		lvReturn.addElement(laOwnrAddress.getSt1());
			//		lvReturn.addElement("");
			//		if (!(laOwnrAddress.getSt2().equals("")
			//			|| laOwnrAddress.getSt2() == null))
			//		{
			//			lvReturn.addElement(laOwnrAddress.getSt2());
			//			lvReturn.addElement("");
			//		}
			//		String lsOwnrTtlCity = laOwnrAddress.getCity();
			//		String lsOwnrTtlCntry = laOwnrAddress.getCntry();
			//		String lsOwnrTtlState = laOwnrAddress.getState();
			//		String lsOwnrTtlZipCd = laOwnrAddress.getZpcd();
			//		String lsOwnrTtlZipCdP4 = laOwnrAddress.getZpcdp4();
			//		// Format CITY, (CNTRY/STATE), ZIP CODE + P4
			//		lsZipCd =
			//			generateZipCdP4(
			//				lsOwnrTtlZipCd,
			//				lsOwnrTtlZipCdP4,
			//				lsOwnrTtlState);
			//		lsCityZip =
			//			generateCityCntry(
			//				lsOwnrTtlCity,
			//				lsOwnrTtlState,
			//				lsOwnrTtlCntry,
			//				lsZipCd);
			//		lvReturn.addElement(lsCityZip);
			//				lvReturn.addElement(laOwnrAddress.getCityStateCntryZip()); 
			//				lvReturn.addElement("");
			//			//} // end Get Owner
			//			// end defect 9831 
			// end defect 10112 

			return lvReturn;
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leRTSEx = new RTSException("", aeNPEx);
		}
		return null;
	}

	/**
	* Get the registration information.
	* 
	* @param aaVehData Object
	* @param aaRegisData Object
	* @param aaTitleData Object
	* @return Vector
	*/
	protected Vector parseRegisInfo(
		Object aaVehData,
		Object aaRegisData,
		Object aaTitleData)
	{
		Vector lvVehData = new Vector();
		VehicleData laVehData = (VehicleData) aaVehData;
		RegistrationData laRegData = (RegistrationData) aaRegisData;
		TitleData laTitleData = (TitleData) aaTitleData;
		int liVehModlYr = laVehData.getVehModlYr();
		String lsVehMk = laVehData.getVehMk();
		if (lsVehMk == null)
		{
			lsVehMk = "";
		}
		String lsYrMk = "";
		if (liVehModlYr < 10)
		{
			lsYrMk = String.valueOf(liVehModlYr) + "   /" + lsVehMk;
		}
		else if (liVehModlYr < 100)
		{
			lsYrMk = String.valueOf(liVehModlYr) + "  /" + lsVehMk;
		}
		else if (liVehModlYr < 1000)
		{
			lsYrMk = String.valueOf(liVehModlYr) + " /" + lsVehMk;
		}
		else
		{
			lsYrMk = String.valueOf(liVehModlYr) + "/" + lsVehMk;
		}
		// Add data to the vector
		lvVehData.addElement(laVehData.getVehClassCd());
		lvVehData.addElement(laRegData.getPrevPltNo());
		lvVehData.addElement(laVehData.getVin());
		lvVehData.addElement(lsYrMk);
		lvVehData.addElement(laVehData.getVehModl());
		lvVehData.addElement(laVehData.getVehBdyType());
		lvVehData.addElement(laTitleData.getVehUnitNo());
		lvVehData.addElement(
			new Integer(laRegData.getResComptCntyNo()));
		lvVehData.addElement(new Integer(laVehData.getVehEmptyWt()));
		lvVehData.addElement(new Integer(laRegData.getVehCaryngCap()));
		lvVehData.addElement(new Integer(laRegData.getVehGrossWt()));
		lvVehData.addElement(laVehData.getVehTon());
		lvVehData.addElement(laVehData.getTrlrType());
		lvVehData.addElement(laVehData.getVehBdyVin());
		lvVehData.addElement(new Integer(laVehData.getVehLngth()));
		lvVehData.addElement(laRegData.getTireTypeCd());
		lvVehData.addElement(new Integer(laRegData.getRegIssueDt()));
		lvVehData.addElement(laVehData.getVehOdmtrReadng());
		lvVehData.addElement(laVehData.getVehOdmtrBrnd());
		lvVehData.addElement(laRegData.getPrevPltNo());
		lvVehData.addElement(new Integer(laRegData.getPrevExpMo()));
		lvVehData.addElement(new Integer(laRegData.getPrevExpYr()));
		lvVehData.addElement(laTitleData.getPrevOwnrName());
		lvVehData.addElement(laTitleData.getPrevOwnrCity());
		lvVehData.addElement(laTitleData.getPrevOwnrState());
		// defect 8901
		// lvVehData.addElement(new Integer(laRegData.getRegPltAge()));
		lvVehData.addElement(
			new Integer(laRegData.getRegPltAge(false)));
		// end defect 8901
		for (int i = 0; i < lvVehData.size(); i++)
		{
			if (lvVehData.elementAt(i) == null)
			{
				lvVehData.setElementAt("", i);
			}
		}
		return lvVehData;
	}

	/**
	 * Get vehicle title location and registration description.
	 * 
	 * @param aaRegData Object
	 * @param aaTitleData Object
	 * @return Vector
	 */
	protected Vector parseRegsDescVehLoc(
		Object aaRegData,
		Object aaTitleData)
	{
		try
		{
			// defect 10112 
			// String lsCityZip;
			// String lsZipCd;
			// end defect 10112 
			int liMaxOwnrCnt = 3;
			Vector lvTtlLocInfo = new Vector();
			Vector lvRegisDesc = new Vector();
			Vector lvReturn = new Vector();

			// defect 9912
			// Remove reference to RegisData.getOrgNo()
			// Refactor laRegClassCd to laRegisData 
			// Get Registration Class and Plate Code and description
			RegistrationData laRegisData = (RegistrationData) aaRegData;
			int liRegClassCd = laRegisData.getRegClassCd();
			int liRegEffDt = laRegisData.getRegEffDt();
			String lsRegPltCd = laRegisData.getRegPltCd();
			String lsRegStkrCd = laRegisData.getRegStkrCd();

			//String lsOrgNo = laRegisData.getOrgNo();
			String lsOrgNo = new String();
			if (caMFVehData.isSpclPlt())
			{
				lsOrgNo = caMFVehData.getSpclPltRegisData().getOrgNo();
			}
			// end defect 9912 
			// defect 9085
			// Get Organization Name  
			String lsOrgName = new String();
			if (lsRegPltCd != null
				&& lsRegPltCd.length() != 0
				&& lsOrgNo != null)
			{
				lsOrgName =
					OrganizationNumberCache.getOrgName(
						lsRegPltCd,
						lsOrgNo);
			}
			// end defect 9085 
			if (liRegEffDt == 0)
			{
				liRegEffDt = new RTSDate().getYYYYMMDDDate();
			}
			String lsRegDesc =
				getCacheRegClassDesc(
					liRegClassCd,
					liRegEffDt,
					caCTData.getTransCode());
			if (lsRegDesc.equals("NOT KNOWN"))
			{
				lsRegDesc = "";
			}
			String lsRegPltDesc = getCachePlateTypeDesc(lsRegPltCd);
			// **************  TEMPORARY ADDITIONAL WEIGHT **************  
			// Get Registration Class Description
			if (isTempAddlWtTransCd(caCTData.getTransCode()))
			{
				lvReturn.addElement(REG_CLASS_TEXT + lsRegDesc);
				lvReturn.addElement(PLATE_TYPE_TEXT + lsRegPltDesc);

				//	defect 9085
				//	Add Organization Name to Receipt
				lvReturn.addElement(ORGANIZATION_TEXT + lsOrgName);
				// end defect  9085

				if (lsRegStkrCd == null)
				{
					lsRegStkrCd = "";
				}
				lvReturn.addElement(STICKER_TYPE_TEXT + lsRegStkrCd);
				return lvReturn;
			}
			// Get Title Location data for vehicle located at a 
			// different address than titled
			TitleData laTitleData = (TitleData) aaTitleData;
			AddressData laTtlLocAddr =
				(AddressData) laTitleData.getTtlVehAddr();

			// defect 10112
			if (!laTtlLocAddr.isPopulated())
			{
				lvTtlLocInfo.addElement(null);
				lvTtlLocInfo.addElement(null);
				lvTtlLocInfo.addElement(null);
			}
			else
			{
				lvTtlLocInfo.addAll(
					laTtlLocAddr.getAddressVector(false));

				//	lvTtlLocInfo.addElement(laTtlLocAddr.getSt1());
				//	if (!(laTtlLocAddr.getSt2().equals("")
				//		|| laTtlLocAddr.getSt2() == null))
				//	{
				//		lvTtlLocInfo.addElement(laTtlLocAddr.getSt2());
				//	}
				//	String lsTtlLocCity = laTtlLocAddr.getCity();
				//	String lsTtlLocCntry = laTtlLocAddr.getCntry();
				//	String lsTtlLocState = laTtlLocAddr.getState();
				//	String lsTtlLocZipCd = laTtlLocAddr.getZpcd();
				//	String lsTtlLocZipCdP4 = laTtlLocAddr.getZpcdp4();
				//	// Format CITY, (CNTRY/STATE), ZIP CODE + P4
				//	lsZipCd =
				//		generateZipCdP4(
				//			lsTtlLocZipCd,
				//			lsTtlLocZipCdP4,
				//			lsTtlLocState);
				//	lsCityZip =
				//		generateCityCntry(
				//			lsTtlLocCity,
				//			lsTtlLocState,
				//			lsTtlLocCntry,
				//			lsZipCd);
				//	lvTtlLocInfo.addElement(lsCityZip);
				// end defect 10112  
			}

			// Get Registration Class Description, This is temperary
			lvRegisDesc.addElement(REG_CLASS_TEXT + lsRegDesc);

			lvRegisDesc.addElement(PLATE_TYPE_TEXT + lsRegPltDesc);
			//	defect 9085
			//	Add Organization Name to Receipt 
			lvRegisDesc.addElement(ORGANIZATION_TEXT + lsOrgName);
			// end defect  9085 
			if (lsRegStkrCd == null)
			{
				lsRegStkrCd = "";
			}
			lvRegisDesc.addElement(STICKER_TYPE_TEXT + lsRegStkrCd);
			// Add Registration Description info to the vector
			if (lvTtlLocInfo.elementAt(0) == null
				|| lvTtlLocInfo.elementAt(0).equals(""))
			{
				for (int i = 0; i < lvRegisDesc.size(); i++)
				{
					if (!(lvRegisDesc.elementAt(i) == null
						|| lvRegisDesc.elementAt(i).equals("")))
					{
						lvReturn.addElement("");
						lvReturn.addElement(lvRegisDesc.elementAt(i));
					}
				}
			}
			else
			{

				//				Add Title Location/Registration Description info to 
				// 				the vector
				lvReturn.addElement(TTL_VEH_LOC_TEXT);
				// defect 9673
				int liSectionSize =
					Math.max(liMaxOwnrCnt, lvRegisDesc.size());
				// for (int i = 0; i < liMaxOwnrCnt; i++)
				for (int i = 0; i < liSectionSize; i++)
				{
					// end defect 9673
					if (i < lvRegisDesc.size())
					{
						lvReturn.addElement(lvRegisDesc.elementAt(i));
					}
					else
					{
						lvReturn.addElement("");
					}

					if (i < lvTtlLocInfo.size())
					{
						lvReturn.addElement(lvTtlLocInfo.elementAt(i));
					}
					else
					{
						lvReturn.addElement("");
					}

				} // end for loop
			} // end else
			return lvReturn;
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leEx = new RTSException("", aeNPEx);
		}
		return null;
	}

	/**
	 * Permit Events
	 * 
	 * @param asTransCd String
	 * @return boolean
	 */
	public boolean isPermitTransCd(String asTransCd)
	{
		// defect 10844 
		return UtilityMethods.printsPermit(asTransCd);
		// end defect 10844 
	}

	/**
	 * Print card number.
	 * 
	 * @param asCardNo String
	 */
	protected void printCardNo(String asCardNo)
	{
		// ***************  PLACARD NUMBER  ***************
		caRpt.print(
			CARD_NO_TEXT,
			HEADER_GRP1,
			charLength(CARD_NO_TEXT));
		caRpt.print(asCardNo);
	}

	/**
	 * Print Vehicle Classification.
	 * 
	 * @param asVehClassification String
	 * @param aiPrtCol int
	 */
	protected void printCarryCap(String asCarryCap, int aiPrtCol)
	{
		caRpt.print(
			CARRY_CAP_TEXT,
			aiPrtCol,
			charLength(CARRY_CAP_TEXT));
		caRpt.print(asCarryCap);
	}

	/**
	 * This method prints out the office name and tac string as
	 * appropriate Change to use OfficeIssuanceCode from Office Ids to
	 * determine if this is a county
	 * 
	 * @param aiOfficeNumber int
	 */
	public void printCntyOffice(int aiOfficeNo)
	{
		// get Office Id Information 
		// defect 6022 
		// Use County Ofcissuanceno for Renew/Repl
		// added test for " || (Renew or Replacement)  to test for
		// Title and DTA events
		// defect 9098
		// Add Exch		
		if (caRegData != null
			&& caRegData.getExmptIndi() != 1
			&& aiOfficeNo == 291
			&& (isTitleTransCd(caCTData.getTransCode())
				|| isDealerTitleTransCd(caCTData.getTransCode())
				|| caCTData.getTransCode().equals(TRANSCD_RENEW)
				|| caCTData.getTransCode().equals(TRANSCD_REPL)
				|| caCTData.getTransCode().equals(TRANSCD_EXCH)))
		{
			// end defect 9098
			aiOfficeNo = caRegData.getResComptCntyNo();
			// defect 6082
			// call createDummyDocNo if this is special case
			createDummyDocNo(aiOfficeNo);
			// end defect 6082
		} // end defect 6022 
		OfficeIdsData laOfficeInfo =
			OfficeIdsCache.getOfcId(aiOfficeNo);
		String lsOfcOrCounty = "";
		String lsTacName = "";
		if (laOfficeInfo.getOfcIssuanceCd() == COUNTY_OFFICE_CD)
		{
			lsOfcOrCounty = COUNTY_TEXT;
			lsTacName = TAC_NAME_TEXT + laOfficeInfo.getTacName();
		}
		else
		{
			lsOfcOrCounty = OFFICE_TEXT;
		}
		this.caRpt.print(
			lsOfcOrCounty + laOfficeInfo.getOfcName(),
			HEADER_GRP1,
			(lsOfcOrCounty + laOfficeInfo.getOfcName()).length());
		this.caRpt.print(lsTacName, HEADER_GRP2, lsTacName.length());
	}

	/**
	 * Print Doc No
	 * 
	 * @param asDocNo String
	 */
	protected void printDocNo(String asDocNo)
	{
		caRpt.print(DOC_NO_TEXT, HEADER_GRP1, charLength(DOC_NO_TEXT));
		caRpt.print(asDocNo);
	}

	/**
	 * Print registration effective date.
	 * 
	 * @param aiRegEffDate int
	 */
	protected void printEffDate(int aiRegEffDate)
	{
		// ***************  EFFECTIVE DATE and (HOT CHECK REDEEMED ONLY)  ***************
		String lsRTSEffDate = "";
		if (aiRegEffDate != 0)
		{
			lsRTSEffDate = convertDtToStr(aiRegEffDate);
		}
		caRpt.print(
			EFF_DATE_TEXT,
			HEADER_GRP3,
			charLength(EFF_DATE_TEXT));
		caRpt.print(lsRTSEffDate);
	}

	/**
	 * Print the effective date.
	 * 
	 * @param aaRTSEffDate RTSDate
	 */
	protected void printEffDate(RTSDate aaRTSEffDate)
	{
		// ***************  EFFECTIVE DATE   ***************
		caRpt.print(
			EFF_DATE_TEXT,
			HEADER_GRP3,
			charLength(EFF_DATE_TEXT));
		caRpt.print(
			aaRTSEffDate.toString() == null
				? ""
				: aaRTSEffDate.toString());
	}

	/**
	 * Print reg effective time.
	 * 
	 * @param asRegEffTime String
	 */
	protected void printEffTime(String asRegEffTime)
	{
		// ***************  EFFECTIVE TIME   ***************
		caRpt.print(
			EFF_TIME_TEXT,
			HEADER_GRP3,
			charLength(EFF_TIME_TEXT));
		caRpt.print(asRegEffTime);
	}
	/**
	 * printEmpId
	 * 
	 * @param asEmpId String
	 */
	protected void printEmpId(String asEmpId)
	{
		caRpt.print(EMP_ID_TEXT, HEADER_GRP2, charLength(EMP_ID_TEXT));
		caRpt.print(asEmpId);
	}

	/**
	 * printFees
	 * 
	 * @param asFeeDescr String
	 * @param aaFeeAmt Dollar
	 */
	protected void printFees(String asFeeDescr, Dollar aaFeeAmt)
	{
		this.caRpt.print(asFeeDescr, COL_49, charLength(asFeeDescr));
		this.caRpt.print(DOLLAR_SIGN, COL_80, charLength(DOLLAR_SIGN));

		// If Refund or Hot Check Credit print negitive sign " - "
		if (!(caCTData.getTransCode().equals(TRANSCD_REFUND))
			&& !(caCTData.getTransCode().equals(TRANSCD_RFCASH))
			&& !(caCTData.getTransCode().equals(TRANSCD_HOTCK)))
		{
			this.caRpt.rightAlign(
				aaFeeAmt.printDollar().substring(1),
				COL_81,
				AMT_LNGTH_13);
		}
		else
		{
			this.caRpt.rightAlign(
				"-" + aaFeeAmt.toString(),
				COL_81,
				AMT_LNGTH_13);
		}
		this.caRpt.nextLine();
		caTotalFees = caTotalFees.add(aaFeeAmt);
	}

	/**
	 * printFeeTotal
	 */
	public void printFeeTotal()
	{
		this.caRpt.print(TOTAL_TEXT, COL_68, charLength(TOTAL_TEXT));
		this.caRpt.print(DOLLAR_SIGN, COL_80, charLength(DOLLAR_SIGN));

		// If Refund or Hot Check Credit print negitive sign " - "
		if (!(caCTData.getTransCode().equals(TRANSCD_REFUND))
			&& !(caCTData.getTransCode().equals(TRANSCD_RFCASH))
			&& !(caCTData.getTransCode().equals(TRANSCD_HOTCK)))
		{
			this.caRpt.rightAlign(
				caTotalFees.printDollar().substring(1),
				COL_81,
				AMT_LNGTH_13);
		}
		else
		{
			this.caRpt.rightAlign(
				"-" + caTotalFees.toString(),
				COL_81,
				AMT_LNGTH_13);
		}
	}

	/**
	 * printFirst3HeaderLines
	 */
	protected void printFirst3HeaderLines()
	{
		// ********  ACCOUNT EVENTS/ ADDITIONAL SALES TAX  *************
		if (isAcctTransCd(caCTData.getTransCode())
			|| isAddlSalesTaxTransCd(caCTData.getTransCode()))
		{
			printTemplateA1();
		}
		// ***************  REGIONAL OFFICE COLLECTIONS  ***************
		if (isRegColTransCd(caCTData.getTransCode()))
		{
			printTemplateA2();
		}

		// **  PERMIT EVENTS/DISABLE PLACARD/NON-RESIDENT AGRICULTURE **
		if (isPermitTransCd(caCTData.getTransCode())
			|| isDisablePlacardTransCd(caCTData.getTransCode())
			|| isNonResidtAgrTransCd(caCTData.getTransCode()))
		{
			printTemplateA3();
		}

		// defect 9145
		// Handle Veh Inquiry of Special Plate only.
		// * TITLE EVENT/REGISTRATION/INQUIRY TEMP/PERM-ADDL-WT EVENTS *
		if (isDealerTitleTransCd(caCTData.getTransCode())
			|| isTitleTransCd(caCTData.getTransCode())
			|| isRegisTransCd(caCTData.getTransCode())
			|| (isVehInqTransCd(caCTData.getTransCode())
				&& caMFVehData != null
				&& !caMFVehData.isSPRecordOnlyVehInq())
			|| isTempPermAddlWtTransCd(caCTData.getTransCode()))
		{
			printTemplateA4();
		}
		// defect 9145

		// ***************  DRIVER EDUCATION  ***************
		// defect 9145
		// Add Veh Inquiry of a special plate only
		// defect 9126
		// Add Special Plate Application receipt.
		if (isDrvEdTransCd(caCTData.getTransCode())
			|| (UtilityMethods.isSpecialPlates(caCTData.getTransCode()))
			|| (isVehInqTransCd(caCTData.getTransCode())
				&& caMFVehData != null
				&& caMFVehData.isSPRecordOnlyVehInq()))
		{
			printTemplateA5();
		}
		// end defect 9126
		// end defect 9145
		// ***************  TOW TRUCK EVENT  ***************
		if (isTowTrkTransCd(caCTData.getTransCode()))
		{
			printTemplateA6();
		}
	}

	/**
	 * printForm31No
	 * 
	 * @param asForm31No String
	 */
	protected void printForm31No(String asForm31No)
	{

		caRpt.print(
			FORM_31_NO_TEXT,
			HEADER_GRP3,
			charLength(FORM_31_NO_TEXT));
		caRpt.print(asForm31No);
	}

	/**
	 * Prints the manufacturing notation.
	 * 
	 * @param aiPrtCol int
	 */
	protected void printManufacturingNotation(int aiPrtCol)
	{
		caRpt.print(
			MFG_REQ_NOTATION,
			aiPrtCol,
			MFG_REQ_NOTATION.length());
	}

	/**
	 * printMiscExpDate
	 * 
	 * @param aaMiscExpdate RTSDate
	 */
	protected void printMiscExpDate(RTSDate aaMiscExpdate)
	{
		caRpt.print(
			EXP_DATE_TEXT,
			HEADER_GRP2,
			charLength(EXP_DATE_TEXT));
		// defect 8268
		if (isDisablePlacardTransCd(caCTData.getTransCode()))
		{
			caRpt.print(aaMiscExpdate.getMMYYYY());
		}
		else
		{
			caRpt.print(aaMiscExpdate.toString());
		}
		// end defect 8268
	}

	/**
	 * printMiscExpTime
	 * 
	 * @param asMiscEffTime String
	 */
	protected void printMiscExpTime(String asMiscEffTime)
	{
		// ***************  EFFECTIVE TIME   ***************
		caRpt.print(
			EXP_TIME_TEXT,
			HEADER_GRP3 + 5,
			charLength(EXP_TIME_TEXT));
		caRpt.print(asMiscEffTime);
	}

	/**
	 * printOfficeName
	 */
	protected void printOfficeName()
	{
		caRpt.print(OFFICE_TEXT, HEADER_GRP1, charLength(OFFICE_TEXT));
		// ***  OFFICE NAME  ***
		caRpt.println("**************");
	}

	/**
	 * Print Recipient Owner information.
	 * 
	 * @param avOwnrData Vector
	 */
	protected void printOwnerInfo(Vector avOwnrData)
	{
		int i = 0;
		int liPrintPosit2 = 0;

		// Set print column position
		if (isTempAddlWtTransCd(caCTData.getTransCode()))
		{
			liPrintPosit2 = HEADER_OWNR3;
		}
		// 40
		// 4/26/2002 changes
		else if (isNonResidtAgrTransCd(caCTData.getTransCode()))
		{
			liPrintPosit2 = HEADER_GRP2;
		}
		//39
		// end of 4/26/2002 changes   
		else
		{
			liPrintPosit2 = HEADER_OWNR2;
		}
		//52

		// defect 8193 
		// Modify font if printing sticker
		if (cbShouldStickersPrint)
		{
			caRpt.println(OWNER_DATA_FONT);
		}
		// end defect 8193 

		while (i < avOwnrData.size())
		{
			caRpt.print(
				avOwnrData.elementAt(i).toString(),
				HEADER_GRP1,
				charLength(avOwnrData.elementAt(i).toString()));
			i++;
			// Print the second item
			if (i < avOwnrData.size())
			{
				caRpt.print(
					avOwnrData.elementAt(i).toString(),
					liPrintPosit2,
					charLength(avOwnrData.elementAt(i).toString()));
				i++;
			}

			// Since we want the new font change on the same line as the 
			// last bit of data we have to check if this is the last 
			// line.  If it is the last line then revert the font back 
			// to what it was and then go to the next line.
			if (i < avOwnrData.size())
			{
				caRpt.blankLines(1);
			}
			else
			{
				// defect 8193 
				// Restore original font if printing sticker 
				if (cbShouldStickersPrint)
				{
					caRpt.print(RECEIPT_FONT);
				}
				// end defect 8193 
				caRpt.blankLines(1);
			}
		} // end for loop
		
		// defect 11050 
		if (isDisablePlacardTransCd(caCTData.getTransCode()))
		{
			caRpt.blankLines(1);
		}
		// end defect 11050 
	}

	/**
	 * printPermitNo
	 * 
	 * @param asPermitNo String
	 */
	protected void printPermitNo(String asPermitNo)
	{
		// ***************  STICKER NUMBER  ***************
		caRpt.print(
			PERMIT_NO_TEXT,
			HEADER_GRP1,
			charLength(PERMIT_NO_TEXT));
		caRpt.print(asPermitNo);
	}
	
	/** 
	 * print Placard Number
	 * 
	 * @param aiPlcrdNo
	 * @param abPrintLine
	 */
	protected void printDisabledPlacard(int aiPlcrdNo,boolean abPrintLine)
	{
		if (isDisablePlacardTransCd(caCTData.getTransCode()))
		{
			Vector lvPlcrd =
				caCTData
				.getTimedPermitData()
				.getDPCustData()
				.getDsabldPlcrd();

			if (lvPlcrd != null && lvPlcrd.size() >aiPlcrdNo)
			{
				if (abPrintLine)
				{
					caRpt.blankLines(1);
				}
				DisabledPlacardData laData =
					(DisabledPlacardData) lvPlcrd.elementAt(aiPlcrdNo);
				printCardNo(laData.getInvItmNo());
			}
		}
	}
		
	/**
	 * printPlateNo
	 * 
	 * @param asRegPltNo String
	 */
	protected void printPlateNo(String asRegPltNo)
	{
		caRpt.print(
			PLATE_NO_TEXT,
			HEADER_GRP1,
			charLength(PLATE_NO_TEXT));
		caRpt.print(asRegPltNo);
	}

	/**
	 * Print Plate Age
	 * 
	 * @param asPltAge String
	 * @param aiPrtCol int
	 */
	protected void printPltAge(String asPltAge, int aiPrtCol)
	{

		caRpt.print(PLT_AGE_TEXT, aiPrtCol, charLength(PLT_AGE_TEXT));
		caRpt.print(asPltAge);
	}

	/**
	 * Print Previous City State
	 * 
	 * @param asPrevCityState String
	 * @param aiPrtCol int
	 */
	protected void printPrevCityState(
		String asPrevCityState,
		int aiPrtCol)
	{
		caRpt.print(
			PREV_CITY_STATE_TEXT,
			aiPrtCol,
			charLength(PREV_CITY_STATE_TEXT));
		caRpt.print(asPrevCityState);
	}

	/**
	 * Print Previous Expiration Month and Year
	 * 
	 * @param asPrevExpMoYr String
	 * @param aiPrtCol int
	 */
	protected void printPrevExpMoYr(String asPrevExpMoYr, int aiPrtCol)
	{
		// defect 8900 
		String lsPrevExpMoYr =
			asPrevExpMoYr.equals("0/0") ? " / " : asPrevExpMoYr;
		caRpt.print(
			PREV_EXP_MO_YR_TEXT,
			aiPrtCol,
			charLength(PREV_EXP_MO_YR_TEXT));
		caRpt.print(lsPrevExpMoYr);
		// end defect 8900
	}

	/**
	 * Print Previous Owner
	 * 
	 * @param asPrevOwnr String
	 * @param aiPrtCol int
	 */
	protected void printPrevOwnr(String asPrevOwnr, int aiPrtCol)
	{
		caRpt.print(
			PREV_OWNR_TEXT,
			aiPrtCol,
			charLength(PREV_OWNR_TEXT));
		caRpt.print(asPrevOwnr);
	}

	/**
	 * Print Previous Plate Number
	 * 
	 * @param asPrevPltNo String
	 * @param aiPrtCol int
	 */
	protected void printPrevPltNo(String asPrevPltNo, int aiPrtCol)
	{
		caRpt.print(
			PREV_PLATE_NO_TEXT,
			aiPrtCol,
			charLength(PREV_PLATE_NO_TEXT));
		caRpt.print(asPrevPltNo);
	}

	/**
	 * Print Registration Expiration Date
	 * 
	 * @param aiRegExpMo int
	 * @param aiRegExpYr int
	 */
	protected void printRegExpDate(int aiRegExpMo, int aiRegExpYr)
	{
		// defect 8900 
		String lsRegExpMo =
			aiRegExpMo == 0 ? " " : String.valueOf(aiRegExpMo);
		String lsRegExpYr =
			aiRegExpYr == 0 ? " " : String.valueOf(aiRegExpYr);

		String lsExpDate = lsRegExpMo + "/" + lsRegExpYr;
		caRpt.print(
			EXP_DATE_TEXT,
			HEADER_GRP3,
			charLength(EXP_DATE_TEXT));
		caRpt.print(lsExpDate);
	}

	/**
	 * Print Registration Description
	 * 
	 * @param avTtlLocRegDesc Vector
	 */
	protected void printRegisDesc(Vector avTtlLocRegDesc)
	{
		int liRegisDescRow = 17;
		// Set printer pointer
		for (int i = getCurrX(); i < liRegisDescRow; i++)
		{
			caRpt.blankLines(1);
		}
		int i = 0;
		while (i < avTtlLocRegDesc.size())
		{
			caRpt.print(
				avTtlLocRegDesc.elementAt(i).toString(),
				HEADER_GRP1,
				charLength(avTtlLocRegDesc.elementAt(i).toString()));
			i++;
			if (i < avTtlLocRegDesc.size())
			{
				// defect 8193
				if (cbShouldStickersPrint)
				{
					// move regis description out of mailer window
					// if printing sticker 
					caRpt.print(
						avTtlLocRegDesc.elementAt(i).toString(),
						HEADER_GRP_REGIS_DESC,
						charLength(
							avTtlLocRegDesc.elementAt(i).toString()));
				}
				else
				{
					caRpt.print(
						avTtlLocRegDesc.elementAt(i).toString(),
						HEADER_GRP2 + 1,
						charLength(
							avTtlLocRegDesc.elementAt(i).toString()));
				}
				// end defect 8193
				i++;
			} // end if

			caRpt.blankLines(1);
		} // end for loop
	}

	/**
	 * Print Registration Issue Date
	 * 
	 * @param asRegIssueDt String
	 * @param aiPrtCol int
	 */
	protected void printRegIssueDt(String asRegIssueDt, int aiPrtCol)
	{
		if (asRegIssueDt == null || asRegIssueDt.length() != 8)
		{
			caRpt.print(
				REG_ISSUE_DATE_TEXT,
				aiPrtCol,
				charLength(REG_ISSUE_DATE_TEXT));
			caRpt.print("");
		}
		else
		{
			int aiYear = Integer.parseInt(asRegIssueDt.substring(0, 4));
			int aiMonth =
				Integer.parseInt(asRegIssueDt.substring(4, 6));
			int aiDay = Integer.parseInt(asRegIssueDt.substring(6, 8));
			RTSDate laRegIssDt = new RTSDate(aiYear, aiMonth, aiDay);
			caRpt.print(
				REG_ISSUE_DATE_TEXT,
				aiPrtCol,
				charLength(REG_ISSUE_DATE_TEXT));
			caRpt.print(laRegIssDt.toString());
		}
	}

	/**
	 * Print Resident County Number
	 * 
	 * @param asResCntyNo String
	 * @param aiPrtCol int
	 */
	protected void printResCntyNo(String asResCntyNo, int aiPrtCol)
	{
		caRpt.print(
			COUNTY_NO_TEXT,
			aiPrtCol,
			charLength(COUNTY_NO_TEXT));
		caRpt.print(asResCntyNo);
	}

	/**
	 * printStateRegistered
	 * 
	 * @param asStRegisNo String
	 */
	protected void printStateRegistered(String asStRegisNo)
	{
		caRpt.print(
			STATE_REGISTERED_TEXT,
			HEADER_GRP1,
			charLength(STATE_REGISTERED_TEXT));
		caRpt.print(asStRegisNo);
	}

	/**
	 * Print Sticker Number
	 * 
	 */
	protected void printStickerNo()
	{
		caRpt.print("    ", HEADER_GRP1, "    ".length());
		caRpt.print("");
	}

	/**
	 * Print System Date
	 */
	protected void printSystemDate()
	{
		String lsTransId = caCTData.getTransId();

		// defect 10745
		RTSDate laDate = new RTSDate();

		if (caCTData.getTransCode().equals(TransCdConstant.WRENEW))
		{
			laDate =
				caCTData
					.getSubconRenwlData()
					.getSubconProcessDateTime();
		}
		else if (lsTransId == null || lsTransId.length() != 17)
		{
			return;
		}

		// ************  SYSTEM / WEBAGENT PROCESS DATE **********
		caRpt.print(DATE_TEXT, HEADER_GRP2, charLength(DATE_TEXT));
		caRpt.print(laDate.toString());
		// end defect 10745 
	}

	/**
	 * print system time
	 */
	protected void printSystemTime()
	{
		String lsTransId = caCTData.getTransId();
		
		// defect 10745
		RTSDate laDate = new RTSDate();

		if (caCTData.getTransCode().equals(TransCdConstant.WRENEW))
		{
			laDate =
				caCTData
					.getSubconRenwlData()
					.getSubconProcessDateTime();
		}
		else if (lsTransId == null || lsTransId.length() != 17)
		{
			return;
		}
		else
		{
			int liAMDate = Integer.parseInt(lsTransId.substring(6, 11));
			int liAMTime =
				Integer.parseInt(
					lsTransId.substring(11, lsTransId.length()));
			laDate = new RTSDate(RTSDate.AMDATE, liAMDate);
			laDate.setTime(liAMTime);
		}

		// ***************  SYSTEM TIME  ***************
		caRpt.print(TIME_TEXT, HEADER_GRP2, charLength(TIME_TEXT));
		caRpt.print(laDate.getTime());
	}

	/**
	 * Print TAC Name
	 */
	protected void printTACName()
	{
		// WsOfficeIds.OfcName
		caRpt.print(COUNTY_TEXT);
		caRpt.print("************ ");
		// WsOfficeIds.TACName
		caRpt.print(
			TAC_NAME_TEXT,
			HEADER_GRP2,
			charLength(TAC_NAME_TEXT));
		caRpt.print("FIRST LAST name");
	}

	/**
	 * Print ACCOUNT EVENTS / ADDITIONAL SALES TAX
	 */
	protected void printTemplateA1()
	{
		// Pass Registration Sticker Number
		boolean lbResult = false;
		// ***************  STICKER NUMBER  ***************
		lbResult = isAddlSalesTaxTransCd(caCTData.getTransCode());
		if (!(lbResult))
		{
			printStickerNo();
		}

		// ***************  SYSTEM DATE  ***************
		printSystemDate();
		// ***************  REGISTRATION EFFECTIVE DATE (HOT CHECK REDEEMED ONLY)  *************
		if (caCTData.getTransCode().equals(TRANSCD_CKREDM))
		{
			printEffDate(caRegData.getRegEffDt());
		}

		caRpt.blankLines(1);
		// ***************  PLATE NUMBER  ***************
		String lsRegPltNo = determinePltNo();
		printPlateNo(lsRegPltNo);
		// ***************  SYSTEM TIME  ***************
		printSystemTime();
		// ***************  REGISTRATION EXPIRATION DATE (HOT CHECK REDEEMED ONLY) ***************
		if (caCTData.getTransCode().equals(TRANSCD_CKREDM))
		{
			printRegExpDate(
				caRegData.getRegExpMo(),
				caRegData.getRegExpYr());
		}

		caRpt.blankLines(1);
		// ***************  DOCUMENT NUMBER  ***************
		printDocNo(caTitleData.getDocNo());
		// ***************  EMPLOYEE ID  ***************
		printEmpId(SystemProperty.getCurrentEmpId());
		// ***************  TRANSACTION ID  ***************
		printTransId(caCTData.getTransId());

		caRpt.blankLines(2);
	}

	/**
	* Print Regional Office Collections
	*/
	protected void printTemplateA2()
	{
		// ***************  SYSTEM DATE  ***************
		printSystemDate();
		caRpt.blankLines(1);
		// ***************  SYSTEM TIME  ***************
		printSystemTime();
		caRpt.blankLines(1);
		// ***************  EMPLOYEE ID  ***************
		printEmpId(SystemProperty.getCurrentEmpId());
		// ***************  TRANSACTION ID  ***************
		printTransId(caCTData.getTransId());

		caRpt.blankLines(2);
	}

	/** 
	 * Print the first three lines of the Permit Event,
	 * Non - Resident Argriculture and Disable Placard.
	 */
	protected void printTemplateA3()
	{
		// Define local variables
		TimedPermitData laTimedPermit =
			(TimedPermitData) caCTData.getTimedPermitData();

		// ***************  PERMIT/CARD/PLATE NUMBER  ***************
		if (isPermitTransCd(caCTData.getTransCode()))
		{
			// defect 10491
			PermitData laPrmtData =
				(PermitData) caCTData.getTimedPermitData();

			printPermitNo(laPrmtData.getPrmtNo());
			//getInvItmNo(caCTData, BLANK_TRCKNG_TYPE, FIRST_RECORD));
		}
		else if (isDisablePlacardTransCd(caCTData.getTransCode()))
		{
			// defect 9831 
			Vector lvPlcrd =
				caCTData
					.getTimedPermitData()
					.getDPCustData()
					.getDsabldPlcrd();

			if (lvPlcrd != null && lvPlcrd.size() != 0)
			{
				DisabledPlacardData laData = (DisabledPlacardData) lvPlcrd
						.get(0);
				String lsCardNo = laData.getInvItmNo();
				if (UtilityMethods
						.getEventType(caCTData.getTransCode()).equals(
								TransCdConstant.RPL_DP_EVENT_TYPE)
						&& laData.getTransTypeCd() == MiscellaneousRegConstant.DP_DEL_TRANS_TYPE_CD)
				{
					lsCardNo = lsCardNo + " (REPLACED)";
				}
				else if (UtilityMethods.getEventType(
						caCTData.getTransCode()).equals(
						TransCdConstant.REN_DP_EVENT_TYPE))
				{
					lsCardNo = lsCardNo + " (RENEWED)";
				}

				printCardNo(lsCardNo);
				// printCardNo(
				// getInvItmNo(
				// caTransData,
				// BLANK_TRCKNG_TYPE,
				// FIRST_RECORD));
			} // end defect 9831
		}
		//	*********  NON_RESIDENT AGRICULTURE TEXAS / OUT OF STATE
		else if (isNonResidtAgrTransCd(caCTData.getTransCode()))
		{
			printPlateNo(caRegData.getRegPltNo());
		}

		// ***************  SYSTEM DATE  ***************
		printSystemDate();

		// ***************  REGISTRATION EFFECTIVE DATE   ***************
		if (!(isDisablePlacardTransCd(caCTData.getTransCode())))
		{
			printEffDate(laTimedPermit.getRTSDateEffDt());
		}

		caRpt.blankLines(1);
		// ***************  PERMIT NUMBER (NON-RESIDENT AGRICULTURE) ***************
		if (isNonResidtAgrTransCd(caCTData.getTransCode()))
		{
			printPermitNo(
				getInvItmNo(caCTData, BLANK_TRCKNG_TYPE, FIRST_RECORD));
		} // defect 8268

		// ***************  SECOND CARD NUMBER ***************
		// defect 11050 
		// defect 9831 
		if (isDisablePlacardTransCd(caCTData.getTransCode()))
		{
			printDisabledPlacard(1,false);
			
			//			Vector lvPlcrd =
			//			caCTData
			//			.getTimedPermitData()
			//			.getDPCustData()
			//			.getDsabldPlcrd();
			
			//			if (lvPlcrd != null && lvPlcrd.size() != 1)
			//			{
			//			DisabledPlacardData laData =
			//			(DisabledPlacardData) lvPlcrd.elementAt(1);
			//			String lsCardNo = laData.getInvItmNo();
			//			printCardNo(lsCardNo);

			//	printCardNo(
			//	getInvItmNo(
			//		caTransData,
			//		BLANK_TRCKNG_TYPE,
			//		SECOND_RECORD));
			// end defect 9831
			//}

		} // end defect 8268
		// end defect 9831 

		// ***************  SYSTEM TIME  ***************
		printSystemTime();
		// ***************  REGISTRATION EFFECTIVE TIME  ***************
		if (isPermitTransCd(caCTData.getTransCode()))
		{
			printEffTime(laTimedPermit.getRTSDateEffDt().getTime());
		}

		caRpt.blankLines(1);
		printDisabledPlacard(2,false);
		// ***************  EMPLOYEE ID  ***************
		printEmpId(SystemProperty.getCurrentEmpId());
		// ***************  TRANSACTION ID  ***************
		printTransId(caCTData.getTransId());
		caRpt.blankLines(1);
		// ***************  STATE REGISTRATERED (NON-RESIDENT AGRICULTURE) ***************
		if (isNonResidtAgrTransCd(caCTData.getTransCode()))
		{
			printStateRegistered(laTimedPermit.getVehRegState());
		} 
		// *************** 	MISC REGISTRATION EXPIRATION DATE  ***************
		printDisabledPlacard(3,false);
		printMiscExpDate(laTimedPermit.getRTSDateExpDt());
		if (isDisablePlacardTransCd(caCTData.getTransCode()))
		{
			Vector lvPlcrd = caCTData.getTimedPermitData()
				.getDPCustData().getDsabldPlcrd();
			if (lvPlcrd != null && lvPlcrd.size() >4)
			{
				for (int j = 4; j <lvPlcrd.size(); j++)
				{
					printDisabledPlacard(j,true);	
				}
			}
			caRpt.blankLines(1);
		}
		// end defect 11050 
		// *************** 	MISC REGISTRATION EXPIRATION TIME  ***************
		if (isPermitTransCd(caCTData.getTransCode()))
		{
			printMiscExpTime(laTimedPermit.getRTSDateExpDt().getTime());
		}

		caRpt.blankLines(1);
	}

	/**
	 * Print the first three lines of the TITLE EVENT / REGISTRATION 
	 * / VEHICLE INQUIRY / TEMPORARY PERMANENT ADDITIONAL WEIGHT EVENTS
	 */
	protected void printTemplateA4()
	{
		// Define local variables
		TimedPermitData laTimedPermit =
			(TimedPermitData) caCTData.getTimedPermitData();
		// ***************  STICKER NUMBER  ***************
		// String lsRegStkrNo = determineStkrNo();
		// printStickerNo(lsRegStkrNo);
		printStickerNo();
		// ***************  SYSTEM DATE  ***************
		printSystemDate();
		// ***************  REGISTRATION EFFECTIVE DATE   *************
		// defect 6068
		// use CanceledPlatDate if plate is canceled
		if (caRegData.getCancPltIndi() > 0)
		{
			printEffDate(caRegData.getCanclPltDt());
		}
		else
		{
			printEffDate(caRegData.getRegEffDt());
		} // end defect 6068
		caRpt.blankLines(1);

		// ***************  PLATE NUMBER  ***************
		String lsRegPltNo = determinePltNo();
		printPlateNo(lsRegPltNo);

		// ***************  SYSTEM TIME  ***************
		printSystemTime();
		// ***************  REGISTRATION EXPIRATION DATE  ***************
		if (!caCTData.getTransCode().equals(TRANSCD_TAWPT))
		{
			String lsExpDtIndi = buildExpirationDt(caCTData);
			// Print RegExpMo, Inventory Issued Year
			// defect 6068
			// handle canceled plate.  Do not have a "canceled" year
			if (caRegData.getCancPltIndi() > 0)
			{
				printRegExpDate(caRegData.getCancRegExpMo(), 0);
			} // end defect 6068
			// defect 8900
			else if (
				CommonFeesCache.isStandardExempt(
					caRegData.getRegClassCd()))
			{
				printRegExpDate(0, 0);
			} // end defect 8900
			// defect 6319
			// use transaction expiration date if it exists
			// CQU100003669 fix related to PLP annual plates
			else if (
				caCTData.getExpMo() > 0 && caCTData.getExpYr() > 0)
			{
				printRegExpDate(
					caCTData.getExpMo(),
					caCTData.getExpYr());
			} // end defect 6319
			else if (lsExpDtIndi.equals("IN"))
			{
				printRegExpDate(
					caRegData.getRegExpMo(),
					ciRegExpYrInvYr);
			} // Print RegExpMo, Owner Supplied Year
			else if (lsExpDtIndi.equals("OS"))
			{
				printRegExpDate(
					caRegData.getRegExpMo(),
					caCTData
						.getVehicleInfo()
						.getRegData()
						.getOwnrSuppliedExpYr());
			} // Print RegExpMo, RegExpYr
			else if (lsExpDtIndi.equals("MF"))
			{
				printRegExpDate(
					caRegData.getRegExpMo(),
					caRegData.getRegExpYr());
			}
			else
			{
				printRegExpDate(0, 0);
			}
		} // end if
		caRpt.blankLines(1);
		// ***************  DOCUMENT NUMBER  ***************
		// defect 6082
		// use csLocalDocNo for document number if it has been created
		// defect 9098
		// Only print special docno for title events		
		if (csLocalDocNo != null
			&& isTitleTransCd(caCTData.getTransCode()))
		{
			// end defect 9098
			printDocNo(csLocalDocNo);
		}
		else if (
			isTitleTransCd(caCTData.getTransCode())
				|| isDealerTitleTransCd(caCTData.getTransCode()))
		{
			printDocNo(caCTData.getTransId());
		} // defect 9641
		// Print Cancelled Plate Doc No 
		else if (caRegData.getCancPltIndi() == 0)
		{
			printDocNo(caTitleData.getDocNo());
		}
		else
		{
			printDocNo(caRegData.getCancPltDocNo());
		} // end defect 9641 
		// end defect 6082
		// ***************  EMPLOYEE ID  ***************

		// defect 10745 
		if (caCTData.getTransCode().equals(TransCdConstant.WRENEW))
		{
			caRpt.print("USER: ", HEADER_GRP2, charLength("USER: "));
			caRpt.print(caCTData.getWebAgntUserName());
		}
		else
		{
			printEmpId(SystemProperty.getCurrentEmpId());
		}
		// end defect 10745 

		// ***************  TRANSACTION ID  ***************
		// defect 6082
		// use csLocalDocNo for transid if it exists
		if (csLocalDocNo != null)
		{
			printTransId(String.valueOf(csLocalDocNo));
		}
		else
		{
			printTransId(caCTData.getTransId());
		}
		// end defect 6082

		// defect 9978 
		// New Prev Doc No for Title /DTA Trans
		caRpt.nextLine();
		if (isDealerTitleTransCd(caCTData.getTransCode())
			|| isTitleTransCd(caCTData.getTransCode()))
		{
			if (caCTData.getOrgVehicleInfo() != null
				&& caCTData.getOrgVehicleInfo().getTitleData() != null
				&& !UtilityMethods.isEmpty(
					caCTData
						.getOrgVehicleInfo()
						.getTitleData()
						.getDocNo()))
			{
				String lsETtlTrans = "";
				TitleData laOrigTitleData =
					caCTData.getOrgVehicleInfo().getTitleData();

				if (laOrigTitleData.getETtlCd()
					!= TitleConstant.NON_NEGOTIABLE_ETTLCD)
				{
					lsETtlTrans =
						laOrigTitleData.isETitle()
							? E_TITLE_PRT_CD
							: PRINT_TITLE_PRT_CD;
				}
				String lsDocNo =
					caCTData
						.getOrgVehicleInfo()
						.getTitleData()
						.getDocNo();
				caRpt.print(
					PREV_DOC_NO_TEXT,
					HEADER_GRP1,
					charLength(PREV_DOC_NO_TEXT));
				caRpt.print(lsDocNo + lsETtlTrans);
			}

		}

		// defect 10449
		// Print for all Special Plates 

		// defect 9689
		// For Vendor Plates, print Plate Expiration instead of blank 
		// line unless this is Temp Addl Wt Permit
		// ********  VENDOR PLATE EXPIRATION DATE  ********
		SpecialPlatesRegisData laSpecialPlatesRegData =
			caCTData.getVehicleInfo().getSpclPltRegisData();
		if (laSpecialPlatesRegData
			!= null //	&& PlateTypeCache.isVendorPlate(
		//		laSpecialPlatesRegData.getRegPltCd())
			&& !caCTData.getTransCode().equals(TRANSCD_TAWPT))
		{
			// end defect 10449 

			// defect 9864 
			// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
			String lsPltExpMo =
				Integer.toString(laSpecialPlatesRegData.getPltExpMo());
			String lsPltExpYr =
				Integer.toString(laSpecialPlatesRegData.getPltExpYr());
			// end defect 9864 
			String lsPltExpDate = lsPltExpMo + "/" + lsPltExpYr;
			caRpt.print(
				PLATE_EXP_DATE_TEXT,
				HEADER_GRP3,
				charLength(PLATE_EXP_DATE_TEXT));
			caRpt.print(lsPltExpDate);

			// defect 10449 
			caRpt.print("  ");
			caRpt.print(TERM);
			caRpt.print(
				"" + laSpecialPlatesRegData.getPltValidityTerm());
			// end defect 10449 
		}
		//		else
		//		{
		//			caRpt.printblankLines(1);
		//		} // end defect 9689
		// end defect 9978 

		// *************** 	MISC REGISTRATION EXPIRATION DATE  ***************
		if (caCTData.getTransCode().equals(TRANSCD_TAWPT))
		{
			printMiscExpDate(laTimedPermit.getRTSDateExpDt());
		} // ***************  FORM31 NUMBER  ***************
		if (isDealerTitleTransCd(caCTData.getTransCode()))
		{
			printForm31No(getForm31No(caCTData));
		}
		caRpt.blankLines(1);
	}

	/**
	 * Print the first three lines of Driver Education Plates
	 */
	protected void printTemplateA5()
	{ // ***************  PLATE NUMBER  ***************
		// defect 9145
		// Add Veh Inquiry of a special plate only
		// defect 9126
		// Add Special Plate Application receipt.
		if (UtilityMethods.isSpecialPlates(caCTData.getTransCode())
			|| (isVehInqTransCd(caCTData.getTransCode())
				&& caMFVehData != null
				&& caMFVehData.isSPRecordOnlyVehInq()))
		{
			printPlateNo(
				caCTData
					.getVehicleInfo()
					.getSpclPltRegisData()
					.getRegPltNo());
		}
		else
		{
			printPlateNo(
				getInvItmNo(caCTData, PLATE_TRCKNG_TYPE, FIRST_RECORD));
		} // end defect 9126
		// end defect 9145
		// ***************  SYSTEM DATE  ***************
		printSystemDate();
		// ***************  REGISTRATION EFFECTIVE DATE   *************
		// defect 9145
		// Add Veh Inquiry of a special plate only
		// defect 9126
		// Print Current Date for Special Plate Application

		// defect 10948
		// For Renew, set RTSEffDate  to PltExpDt + 1
		int liRTSEffDT = new RTSDate().getYYYYMMDDDate();
		if (caCTData.getTransCode().equals(TransCdConstant.SPRNW))
		{
			if (caCTData
			.getVehicleInfo()
			.getSpclPltRegisData().getOrigPltExpMo() != 12)
			{
				liRTSEffDT =
				(caCTData
				.getVehicleInfo()
				.getSpclPltRegisData().getOrigPltExpYr() * 10000)
				+ ((caCTData
				.getVehicleInfo()
				.getSpclPltRegisData().getOrigPltExpMo() + 1) * 100)
				 + 1;
			}
			else
			{
				liRTSEffDT =
				((caCTData
				.getVehicleInfo()
				.getSpclPltRegisData().getOrigPltExpYr() + 1) * 10000)
				+ 100
				+ 1;
			}
		}
		// end defect 10948
		
		if (UtilityMethods.isSpecialPlates(caCTData.getTransCode())
			|| (isVehInqTransCd(caCTData.getTransCode())
				&& caMFVehData != null
				&& caMFVehData.isSPRecordOnlyVehInq()))
		{
			// defect 10948
			//printEffDate(RTSDate.getCurrentDate());
			printEffDate(liRTSEffDT);
			// end defect 10948
		}
		else
		{
			printEffDate(caRegData.getRegEffDt());
		} // end defect 9126
		// end defect 9145
		caRpt.blankLines(1);
		// ***************  SYSTEM TIME  ***************
		printSystemTime();
		// ***************  REGISTRATION EXPIRATION DATE  ***************
		// defect 9145
		// Add Veh Inquiry of a special plate only
		// defect 9126
		// Print RegExpDt for Special Plate Application
		if (UtilityMethods.isSpecialPlates(caCTData.getTransCode())
			|| (isVehInqTransCd(caCTData.getTransCode())
				&& caMFVehData != null
				&& caMFVehData.isSPRecordOnlyVehInq()))
		{
			printRegExpDate(
				caCTData
					.getVehicleInfo()
					.getSpclPltRegisData()
					.getPltExpMo(),
				caCTData
					.getVehicleInfo()
					.getSpclPltRegisData()
					.getPltExpYr());

			// defect 10449 
			caRpt.print("  ");
			caRpt.print(TERM);
			caRpt.print(
				""
					+ caCTData
						.getVehicleInfo()
						.getSpclPltRegisData()
						.getPltValidityTerm());
			// end defect 10449 

		}
		else
		{
			printRegExpDate(
				caRegData.getRegExpMo(),
				caRegData.getRegExpYr());
		} // end defect 9126
		// end defect 9145
		caRpt.blankLines(1);
		// ***************  EMPLOYEE ID  ***************
		printEmpId(SystemProperty.getCurrentEmpId());
		// ***************  TRANSACTION ID  ***************
		printTransId(caCTData.getTransId());
		caRpt.blankLines(1);
	}

	/**
	 * Print first lines of Tow Truck.
	 */
	protected void printTemplateA6()
	{
		// Define local variables		
		TimedPermitData laTimedPermit =
			(TimedPermitData) caCTData.getTimedPermitData();
		// ***************  SYSTEM DATE  ***************
		printSystemDate();
		// ***************  TOW TRUCK PLATE NUMBER   ***************
		printTowTrkPltNo(laTimedPermit.getTowTrkPltNo());
		caRpt.blankLines(1);
		// ***************  PLATE NUMBER  ***************
		printPlateNo(caRegData.getRegPltNo());
		// ***************  SYSTEM TIME  ***************
		printSystemTime();
		caRpt.blankLines(1);
		// ***************  STATE REGISTRATERED  ***************
		printStateRegistered(laTimedPermit.getVehRegState());
		// ***************  EMPLOYEE ID  ***************
		printEmpId(SystemProperty.getCurrentEmpId());
		// ***************  TRANSACTION ID  ***************
		printTransId(caCTData.getTransId());
		caRpt.blankLines(1);
		// defect 10007
		// *************** 	CERTIFICATE NUMKBER  ***************
		//printCertifNo(laTimedPermit.getDlsCertNo());
		// end defect 10007
		caRpt.blankLines(1);
	}

	/**
	 * Print Tire Type
	 * 
	 * @param asTireType String
	 * @param aiPrtCol int
	 */
	protected void printTireType(String asTireType, int aiPrtCol)
	{
		caRpt.print(
			TIRE_TYPE_TEXT,
			aiPrtCol,
			charLength(TIRE_TYPE_TEXT));
		caRpt.print(asTireType);
	}

	/**
	 * Print Tow Truck Plate Number
	 * 
	 * @param aiTowTrkPltNo String
	 */
	protected void printTowTrkPltNo(String aiTowTrkPltNo)
	{
		// ***************  EFFECTIVE DATE (HOT CHECK REDEEMED ONLY)  ***************
		caRpt.print(
			TOW_TRK_PLT_NO_TEXT,
			HEADER_GRP3,
			charLength(TOW_TRK_PLT_NO_TEXT));
		caRpt.print(aiTowTrkPltNo);
	}

	/**
	 * Print TransId.
	 * 
	 * @param asTransId String
	 */
	protected void printTransId(String asTransId)
	{
		caRpt.print(
			TRANS_ID_TEXT,
			HEADER_GRP3,
			charLength(TRANS_ID_TEXT));
		// Transaction Id number comes from: Title event => MVFuncTrans
		// or TransHdr
		caRpt.print(asTransId);
	}

	/**
	 * Print Trailer Type
	 * 
	 * @param asTrlrType String
	 * @param aiPrtCol int
	 */
	protected void printTrlrType(String asTrlrType, int aiPrtCol)
	{
		caRpt.print(
			TRLR_TYPE_TEXT,
			aiPrtCol,
			charLength(TRLR_TYPE_TEXT));
		caRpt.print(asTrlrType);
	}

	/**
	 * Print Travel Trailer Length
	 * 
	 * @param asTrvlTrlrLngth String
	 * @param aiPrtCol int
	 */
	protected void printTrvlTrlrLngth(
		String asTrvlTrlrLngth,
		int aiPrtCol)
	{
		caRpt.print(
			TRVL_TRLR_TEXT,
			aiPrtCol,
			charLength(TRVL_TRLR_TEXT));
		caRpt.print(asTrvlTrlrLngth);
	}

	/**
	 * Print Vehicle Body Style
	 * 
	 * @param asVehBdyStyle String
	 * @param aiPrtCol int
	 */
	protected void printVehBdyStyle(String asVehBdyStyle, int aiPrtCol)
	{
		caRpt.print(
			BODY_STYLE_TEXT,
			aiPrtCol,
			charLength(BODY_STYLE_TEXT));
		caRpt.print(asVehBdyStyle);
	}

	/**
	 * Print Vehicle body VIN
	 * 
	 * @param asVehBdyVIN String
	 * @param aiPrtCol int
	 */
	protected void printVehBdyVIN(String asVehBdyVIN, int aiPrtCol)
	{
		caRpt.print(BDY_VIN_TEXT, aiPrtCol, charLength(BDY_VIN_TEXT));
		caRpt.print(asVehBdyVIN);
	}

	/**
	 * Print Vehicle Classification.
	 * 
	 * @param asVehClassification String
	 * @param aiPrtCol int
	 */
	protected void printVehClassifcation(
		String asVehClassification,
		int aiPrtCol)
	{
		caRpt.print(
			VEH_CLASS_TEXT,
			aiPrtCol,
			charLength(VEH_CLASS_TEXT));
		caRpt.print(asVehClassification);
	}

	/**
	 * Print the vehicle data for Accounting events.
	 * 
	 * @param avVehdata Vector
	 */
	protected void printVehDataAcct(Vector avVehdata)
	{
		int liVehInfoRow = 26;
		// Set printer pointer
		for (int i = getCurrX(); i < liVehInfoRow; i++)
		{
			this.caRpt.blankLines(1);
		}
		for (int i = 0; i < avVehdata.size(); i++)
		{
			switch (i)
			{
				case 0 : // Print VehClassCd
					{
						printVehClassifcation(
							avVehdata.elementAt(i).toString(),
							COL_01);
						break;
					}
				case 1 : // Print PrevPltNo
					{
						printPrevPltNo(
							avVehdata.elementAt(i).toString(),
							COL_40);
						caRpt.blankLines(1);
						break;
					}
				case 2 : // Print VIN
					{
						printVIN(
							avVehdata.elementAt(i).toString(),
							COL_01);
						caRpt.blankLines(1);
						break;
					}
				case 3 : // Print Year/Make
					{
						printVehYrMk(
							avVehdata.elementAt(i).toString(),
							COL_01);
						break;
					}
				case 4 : // Print Model
					{
						printVehModl(
							avVehdata.elementAt(i).toString(),
							COL_20);
						break;
					}
				case 5 : // Print Body Style
					{
						printVehBdyStyle(
							avVehdata.elementAt(i).toString(),
							COL_33);
						break;
					}
				case 6 : // Print Unit number
					{
						printVehUnitNo(
							avVehdata.elementAt(i).toString(),
							COL_50);
						caRpt.blankLines(1);
						break;
					}
				case 7 : // Print Empty weight
					{
						printVehEmptyWt(
							avVehdata.elementAt(i).toString(),
							COL_01);
						break;
					}
				case 8 : // Print Carrying capacity
					{
						printCarryCap(
							avVehdata.elementAt(i).toString(),
							COL_18);
						break;
					}
				case 9 : // Print Gross Weight
					{
						printVehGrossWt(
							avVehdata.elementAt(i).toString(),
							COL_45);
						break;
					}
				case 10 : // Print Tonnage
					{
						printVehTon(
							avVehdata.elementAt(i).toString(),
							COL_62);
						caRpt.blankLines(1);
						break;
					}
				case 11 : // Print Body VIN
					{
						printVehBdyStyle(
							avVehdata.elementAt(i).toString(),
							COL_01);
						break;
					}
				case 12 : // Print Unit number
					{
						printTrvlTrlrLngth(
							avVehdata.elementAt(i).toString(),
							COL_57);
						caRpt.blankLines(1);
						break;
					}
				default :
					{
						System.out.println(
							"oops, drop out of the switch prematurely");
					}
			} // switch
		} // end for loop
	}

	/**
	 * Print Vehicle Data Title
	 * 
	 * @param avVehdata Vector
	 */
	public void printVehDataTitle(Vector avVehdata)
	{
		int liVehInfoRow = 26;
		// Set printer pointer
		for (int i = getCurrX(); i < liVehInfoRow; i++)
		{
			this.caRpt.blankLines(1);
		}
		for (int i = 0; i < avVehdata.size(); i++)
		{
			switch (i)
			{
				case 0 : // print VehVin
					{
						printVIN(
							avVehdata.elementAt(i).toString(),
							COL_06);
						break;
					}
				case 1 : // Print VehClassCd
					{
						printVehClassifcation(
							avVehdata.elementAt(i).toString(),
							COL_58);
						// Move down 1 line
						caRpt.nextLine();
						break;
					}
				case 2 : // Print Year/Make
					{
						printVehYrMk(
							avVehdata.elementAt(i).toString(),
							COL_06);
						break;
					}
				case 3 : // Print Model
					{
						printVehModl(
							avVehdata.elementAt(i).toString(),
							COL_26);
						break;
					}
				case 4 : // Print Body Style
					{
						printVehBdyStyle(
							avVehdata.elementAt(i).toString(),
							COL_38);
						break;
					}
				case 5 : // Print Unit number
					{
						printVehUnitNo(
							avVehdata.elementAt(i).toString(),
							COL_56);
						// Move down 1 line
						caRpt.nextLine();
						break;
					}
				case 6 : // Print Empty weight
					{
						printVehEmptyWt(
							avVehdata.elementAt(i).toString(),
							COL_06);
						break;
					}
				case 7 : // Print Carrying capacity
					{
						printCarryCap(
							avVehdata.elementAt(i).toString(),
							COL_24);
						break;
					}
				case 8 : // Print Gross Weight
					{
						printVehGrossWt(
							avVehdata.elementAt(i).toString(),
							COL_51);
						break;
					}
				case 9 : // Print Tonnage
					{
						printVehTon(
							avVehdata.elementAt(i).toString(),
							COL_68);
						// Move down 1 line
						break;
					}
				case 10 : // Print Trailer Type
					{
						printTrlrType(
							avVehdata.elementAt(i).toString(),
							COL_83);
						// Move down 1 line
						caRpt.nextLine();
						break;
					}
				case 11 : // Print Body VIN
					{
						printVehBdyVIN(
							avVehdata.elementAt(i).toString(),
							COL_06);
						break;
					}
				case 12 : // Print Trailer Length
					{
						printTrvlTrlrLngth(
							avVehdata.elementAt(i).toString(),
							COL_62);
						// Move down 1 line
						caRpt.nextLine();
						break;
					}
				case 13 : // Print prev owner name
					{
						printPrevOwnr(
							avVehdata.elementAt(i).toString(),
							COL_06);
						break;
					}
				case 14 : // Print prev owner City, State
					{
						printPrevCityState(
							avVehdata.elementAt(i).toString(),
							COL_49);
						caRpt.blankLines(1);
						break;
					}
				default :
					{
						System.out.println(
							"oops, drop out of the switch prematurely");
					}
			} // switch
		} // end for loop
	}

	/**
	 * Print Vehicle Classification.
	 * 
	 * @param asVehEmptyWt String
	 * @param aiPrtCol int
	 */
	protected void printVehEmptyWt(String asVehEmptyWt, int aiPrtCol)
	{
		caRpt.print(EMPTY_WT_TEXT, aiPrtCol, charLength(EMPTY_WT_TEXT));
		caRpt.print(asVehEmptyWt);
	}

	/**
	 * Print Vehicle Classification.
	 * 
	 * @param asVehGrossWt String
	 * @param aiPrtCol int
	 */
	protected void printVehGrossWt(String asVehGrossWt, int aiPrtCol)
	{

		caRpt.print(GROSS_WT, aiPrtCol, charLength(GROSS_WT));
		caRpt.print(asVehGrossWt);
	}

	/**
	 * Print Vehicle Classification.
	 * 
	 * @param asVehModl String
	 * @param aiPrtCol int
	 */
	protected void printVehModl(String asVehModl, int aiPrtCol)
	{
		caRpt.print(MODEL_TEXT, aiPrtCol, charLength(MODEL_TEXT));
		caRpt.print(asVehModl);
	}

	/**
	 * Print Vehicle Notation
	 * 
	 * @param avVehNotation Vector
	 */
	protected void printVehNotation(Vector avVehNotation)
	{
		// Print the title text for vehicle notation
		caRpt.println(VEH_NOTATION_TEXT);
		// print vehicle notation
		for (int i = 0; i < avVehNotation.size(); i++)
		{
			caRpt.println(avVehNotation.elementAt(i).toString());
		}
	}

	/**
	 * Print vehicle notations.
	 * 
	 * @param avVehNotation Vector
	 * @param aiPrntPos int 
	 */
	protected void printVehNotation(
		Vector avVehNotation,
		int aiPrntPos)
	{
		caRpt.print(
			VEH_NOTATION_TEXT,
			aiPrntPos,
			charLength(VEH_NOTATION_TEXT));
		caRpt.nextLine();
		// print vehicle notation
		for (int i = 0; i < avVehNotation.size(); i++)
		{
			caRpt.print(
				avVehNotation.elementAt(i).toString(),
				aiPrntPos,
				charLength(avVehNotation.elementAt(i).toString()));
			caRpt.blankLines(1);
		}
	}

	/**
	 * Print Vehicle Odometer Brand
	 * 
	 * @param asVehOdoBrnd String
	 * @param aiPrtCol int
	 */
	protected void printVehOdoBrnd(String asVehOdoBrnd, int aiPrtCol)
	{
		caRpt.print(
			VEH_ODO_BRND_TEXT,
			aiPrtCol,
			charLength(VEH_ODO_BRND_TEXT));
		caRpt.print(asVehOdoBrnd);
	}

	/**
	 * Print Vehicle Odometer Reading
	 * 
	 * @param asVehOdoRdng String
	 * @param aiPrtCol int
	 */
	protected void printVehOdoRdng(String asVehOdoRdng, int aiPrtCol)
	{
		caRpt.print(
			VEH_ODO_RDNG_TEXT,
			aiPrtCol,
			charLength(VEH_ODO_RDNG_TEXT));
		caRpt.print(asVehOdoRdng);
	}

	/**
	 * Print Vehicle Tonnage
	 * 
	 * @param asVehTon String
	 * @param aiPrtCol int
	 */
	protected void printVehTon(String asVehTon, int aiPrtCol)
	{
		caRpt.print(TON_TEXT, aiPrtCol, charLength(TON_TEXT));
		caRpt.print(asVehTon);
	}

	/**
	 * Print Vehicle Unit Number
	 * 
	 * @param asVehClassification String
	 */
	protected void printVehUnitNo(String asVehUnitNo, int aiPrtCol)
	{
		caRpt.print(UNIT_NO_TEXT, aiPrtCol, charLength(UNIT_NO_TEXT));
		caRpt.print(asVehUnitNo);
	}

	/**
	 * Print Vehicle Year Make
	 * 
	 * @param asVehYrMake String
	 * @param aiPrtCol int
	 */
	protected void printVehYrMk(String asVehYrMake, int aiPrtCol)
	{
		caRpt.print(YR_MK_TEXT, aiPrtCol, charLength(YR_MK_TEXT));
		caRpt.print(asVehYrMake);
	}

	/**
	 * Print Vehicle VIN
	 * 
	 * @param asVIN String
	 * @param aiPrtCol int
	 */
	protected void printVIN(String asVIN, int aiPrtCol)
	{
		// defect 10112 
		caRpt.print(VIN_ID_NO, aiPrtCol, charLength(VIN_ID_NO));
		// end defect 10112 
		caRpt.print(asVIN);
	}

	/**
	 * setNumOfDashes
	 * 
	 * @param aiNumOfDashes int
	 */
	public String setNumOfDashes(int aiNumOfDashes)
	{
		String lsCharLngth = "";
		for (int i = 0; i < aiNumOfDashes; i++)
		{
			lsCharLngth = lsCharLngth + "- ";
		}
		return lsCharLngth;
	}

	/**
	 * setPrinterPointer
	 * 
	 * @param aiNewPointerPosit int
	 */
	public void setPrinterPointer(int aiNewPointerPosit)
	{
		Report laRpt = new Report();
		for (int i = laRpt.getCurrX(); i < aiNewPointerPosit; i++)
		{
			laRpt.blankLines(1);
		}
	}

	/**
	 * set Printer Pointer Receipt
	 * 
	 * @param aiNewPointerPosit int
	 */
	public void setPrinterPointerRcpt(int aiNewPointerPosit)
	{
		Report cRpt = new Report();
		for (int i = getCurrX(); i < aiNewPointerPosit; i++)
		{
			cRpt.blankLines(1);
		}
	}

	/**
	 * Write to console if not production
	 * 
	 * @param asLabel String
	 * @param aiLength int
	 * @param asValue String
	 */
	protected void toConsole(
		String asLabel,
		int aiLength,
		String asValue)
	{
		if (SystemProperty.getProdStatus() != 0)
		{
			System.out.println(
				UtilityMethods.addPaddingRight(asLabel, 22, " ")
					+ " "
					+ UtilityMethods.addPadding(
						Integer.toString(aiLength),
						5,
						" ")
					+ "    "
					+ UtilityMethods.addPaddingRight(asValue, 25, "*"));
		}
	}
}