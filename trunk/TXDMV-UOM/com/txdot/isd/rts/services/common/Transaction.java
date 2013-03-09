package com.txdot.isd.rts.services.common;

import java.io.*;

import java.util.*;

import com.txdot.isd.rts.client.common.business.CommonClientBusiness;
import com.txdot.isd.rts.client.common.ui.FrmPaymentPMT001;
import com.txdot.isd.rts.client.funds.business.FundsClientBusiness;
import com
	.txdot
	.isd
	.rts
	.client
	.inventory
	.business
	.InventoryClientBusiness;
import com.txdot.isd.rts.client.misc.business.MiscClientBusiness;
import com.txdot.isd.rts.client.title.business.TitleClientBusiness;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com
	.txdot
	.isd
	.rts
	.services
	.reports
	.accounting
	.GenAccountingReceipt;
import com.txdot.isd.rts.services.reports.accounting.GenTimeLagReport;
import com.txdot.isd.rts.services.reports.accounting.RegOfcColl;
import com.txdot.isd.rts.services.reports.miscellaneous.*;
import com
	.txdot
	.isd
	.rts
	.services
	.reports
	.registration
	.GenRegistrationReceipts;
import com
	.txdot
	.isd
	.rts
	.services
	.reports
	.specialplates
	.GenSpclPltApplReceipt;
import com
	.txdot
	.isd
	.rts
	.services
	.reports
	.specialplates
	.GenSpecialPlatePermit;
import com.txdot.isd.rts.services.reports.title.*;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.event.CommEvent;


/*
 * 
 * Transaction.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Walker&Ting	04/11/2002	Fixed CQU100003435 by catching exception
 * M Rajangam	04/22/2002	Fixed CQU100003464 by selecting tray3 for 
 *							cco
 * N Ting		04/22/2002	Fixed CQU100003587 by not inserting payment 
 *							record for internet renewal
 * N Ting		04/22/2002	Fixed CQU100003575 by changing populateTrans 
 *							and populateMvFuncTrans to use isUseBarCode 
 *							instead of barcode indi
 * RR Rowehl	04/23/2002	Fixed CQU100003634 by changing printDocument
 * J Kwik		04/24/2002	Fixed CQU100003482 by changing 
 *							aCompleteTransactionData.getInvItms() to 
 *							aCompleteTransactionData.getAllocInvItms() 
 *							in populateMvTrans().
 * S Govindappa 04/25/2002  Fixed CQU100003466 by changing printDocument 
 *							to look for endTrans to prevent duplicate 
 *							printing for Salvage
 * S Govindappa 04/29/2002  Fixing CQU100003681 by changing 
 *							printDocument() to print COA in landscape
 * N Ting		04/30/2002	Fix for defect CQU100003689
 *							restoreSetAside()
 * S Govindappa 04/01/2002	Fixed defect CQU100003681 by changing 
 *							printDocument() to print COA from Tray1.
 * S Govindappa 05/02/2002	Fixed defect CQU100003762 by changing
 *							saveReceipts() to have a description of 
 *							"For Microfilm purposes" for DTA event
 * S Govindappa 05/02/2002	Fixed defect CQU100003762 by changing 
 *							genDealerCompletedReport(Vector) to show up 
 *							Dealer Completed Report in Reprint Receipts 
 *							screen for DTA event
 * N Ting		05/08/2002	Fixed defect CQU100003807 by changing 
 *							populateFundFuncTrnsTrFdsDetail to fill in 
 *							Comptcntyno to ofcissuanceno for FNDREM
 * J Rue		05/12/2002	Fixed defect 3865 by modify method
 *							genDealerCompletedReport to print the 
 *							correct number of dealer completed report.
 * MAbs			05/20/2002	Opened up CashDrawer CQU100003990
 * K Harrell    05/28/2002	Fixed defects 4032(endTrans) & 4053 
 *							(populateInternetDbTables)
 * N Ting		07/01/2002	Fix defect CQU100004320 by not saving 
 *							vehicle for Quick Counter events
 * N Ting		07/24/2002	Fix defect CQU100004517
 * Min Wang		07/26/2002	Defect(CQU100004443) Added handling to put 
 *							ADDR Trans in RCPTLOG,
 * 						 	These changes can also support Quick Counter
 *							in RCPTLOG.
 * K. Harrell   07/30/2002 	No MVFuncTrans for 
 *							Refund/RFCash on Cancelled Plate/Stkr
 *							defect 4520
 * M Wang		07/31/2002	Fixed defect (CQU100004319) by adding 
 *							TransCd in the addTrans().
 * M Wang		08/02/2002	Fixed defect (CQU100004546) Cash Receipt is 
 *							printing duplicate receipt even when 
 *							production status is
 *						    2 in rtscld.cfg file.
 * Ray Rowehl	08/03/2002	Fixed defect CQU100004571.  catch 
 *							rtsexceptions on
 *							opening cashdrawer so we do not fail back to
 *							desktop.
 * Ray Rowehl	08/22/2002	Work on CQU100003700
 *							Add handling for InternetTransData in 
 *							populateInternetDB. Comment out this part 
 *							of the code and hold for later.
 * Ray Rowehl	08/24/2002	Fix defect CQU100004671
 *							Put writeToCache before postTrans. Failure 
 *							on disk write should come before posting to 
 *							DB.
 * Ray Rowehl	08/24/2002	Fix Defect CQU100004672 in cacheIO.
 *							Do not attempt to open a file that is a 
 *							directory.
 * Ray Rowehl	08/26/2002	Fix Defect CQU100004659.  Sort file list in 
 *							cacheIO.
 * MAbs			08/26/2002	PCR 41 fixes
 * MAbs			09/17/2002	PCR 41 Integration
 * B Brown		08/19/2002	Bob added the 
 *							"|| lsTransCode.equals(TransCdConstant.RNR))" 
 *							in the addtrans method, so the "request for 
 *							renewal" checked in reg033 would be added 
 *							to pending trans screen and in RPR001 
 *							reprint receipts.
 *							See defect #4726 for this.
 * Ray Rowehl	09/25/2002 	enable PCR42 in populateInternetTables
 * J Rue		09/12/2002	Reset the apprehended county 
 *							to 0 when completing title transaction.
 *							modify addTrans()
 *							defect 4637 
 * Ray Rowehl	10/21/2002	Move printing of receipt to after posting 
 *							of trans. will look at changing other 
 *							postTrans calls later.
 *							add processCacheVector()
 *							modify endTrans() 
 *							defect 4891
 * J Rue		11/01/2002	Set Special Examination	Required indi to 0. 
 *							modify addTrans()
 *							defect 4894 
 * S Govindappa 11/13/2002 	Changed populateMvTrans 
 *							method to calculate
 *                          liRegEffYr(RegEffDate written to MF) 
 *							correctly for subcon events
 * 							defect 5100 
 * Ray Rowehl	11/11/2002	Add methods to handle Void.
 *							defect 4745
 * K Harrell   	02/14/2003  Exit on error reading TrxCache
 *							defect 5225
 * Ray Rowehl	02/18/2003	debug message on populateTrans
 *							should only occur in development.
 *							defect 5512 
 * K Harrell   	03/26/2003  Move receipt handling after processing 
 * 							of transaction.
 *							defect 5816 
 * K Harrell   	03/27/2003  Retain Client Time from Subcon in addTrans
 *							modify addTrans()
 *							defect 5860
 * K Harrell   	04/21/2003  Need to setLastCSN() for BackOffice 
 * 							Transaction so that can reprint	Receipt. 
 *                          modify addTrans()
 * 							defect 5986  
 * K Harrell   	04/23/2003  DelTIP did not populate VehMk in trans
 *                          modify populateTrans()
 * 							defect 5943
 * K Harrell   	05/02/2003  modify populateTrans()
 * 							defect 5943 	
 * J Rue		05/05/2003	Set margin for patch code printing for
 * 							NRCOT and SCOT
 *							modify printDocument()
 *							defect 6032 
 * K Harrell    05/07/2003  Do not reset Substaid for Special Plates; 
 * 							Will Set in SendTrans;
 *							modify populateMvTrans()
 *							defect 6084  
 * Ray Rowehl	05/08/2003	create a new method to handle Special Plates
 * 							Docno's so it can also be called from 
 * 							ReceiptTemplate.
 *		   				 	add populateSpecTtlNoMf()
 *							modify populateMvTrans()
 *							defect 6082 
 * K Harrell    05/14/2003  Move most recent Salvage Data to MVFuncTrans
 * B Arredondo				modify  populateMvTrans()
 * 							defect 5942  
 * K Harrell    05/14/2003  Update CCOIssueDate for CCO
 * B Arredondo				update method  populateMvTrans()
 * 							defect 6093  
 * K Harrell    05/22/2003  HQ Title did not update BatchCount
 * 							Move postTransaction after processCacheVector
 *							modify addTrans() 
 *							defect 6158  
 * Ray Rowehl	06/10/2003	Do not print a receipt for CCOSCT.
 *							modify printDocument()
 *							defect 6173
 * Ray Rowehl	06/11/2003 	Print receipts before documents for salvage.
 *							modify printDocument()
 *							defect 6234
 * Ray Rowehl	07/07/2003	Properly set MF Down indicator in MV Func
 *							method populateMvTrans()
 *							defect 6011
 * Ray Rowehl	07/08/2003	Make ProcessCacheVector more robust with a 
 *							retry on send to server and a DB_Down 
 *							message on failure. Also reformat code on 
 *							whole class.
 *							modified processCacheVector(),writeToCache()
 *							defect 6110
 * J Rue		07/18/2003	write NRCOT, CCONRT, SCOT, 
 *							CCOSCT to  SALVCCO.CRT
 *							method printDocument()
 *							defect 6181 
 * K Harrell    07/20/2003  Exit application on	StreamCorruptedException
 *							modify cacheIO()
 *							defect 6098. 
 * Ray Rowehl	09/24/2003	On subcon renewals for with expiration month
 *							in December, we were adding year to year. 
 *							Instead we should have been adding 1 to 
 *							year. this change only effects subcon 
 *							renewal. This change was previously made 
 *							under defect 5100
 *							modify populateMvTrans()
 *							defect 6605  Ver 5.1.5
 * K Harrell	10/16/2003	DB Server Down processing Cleanup
 *							Write to SendCache log on error during 
 *							readFromCache
 *							new directSendCacheRead()
 *							modified cacheIO(),readFromCache(),
 *							processCacheVector(),
 *							defect 6614 5.1.5.1
 * K Harrell	10/28/2003	Order Delete Transaction Vector same as 
 *							Insert
 *							modified createDeleteAllTransVector(),
 *							createDeleteSelectedTransVector()
 *							defect 6664 5.1.5.1
 * B Hargrove   12/05/2003  Added 'Special assignments for (CREDIT CARD)
 *							PAYMENT' so that the Owner Name is used for 
 *							the Credit Card transaction when 'Charge' is
 *							the payment type
 *							modify populateTrans()
 *							defect 6285  Ver 5.1.5 Fix 2
 * K Harrell	12/31/2003  Handle errors on deleteTrans, i.e. no 
 *							Trans_Hdr w/ transtimestmp = null.
 *							modify deleteTrans(), processCacheVector()
 *							Defect 6772  Ver 5.1.5 Fix 2
 * J Rue		01/14/2004	Comment out call to 
 *							TitleRejectionCorrection() as is not 
 *							required.
 *							modified formatReceipt()
 *							defect 6199 Ver 5.1.5.2
 * K Harrell	01/11/2004	Beginning of work for eliminating the 
 *							multiple units of work for  "autoendtrans" 
 *							transactions.Introduce autoEndTrans field to 
 *							TransactionControl. Only set for IRENEW. In 
 *							addTrans(), if isAutoEndTrans, add end 
 *							transaction processing to 
 *							TransactionCacheVector.  Subsequently, print
 *							receipt, set LastCSN and reset transaction.
 *							added autoEndTrans values for all 
 *							TransactionControl values
 *							modified addTrans()  
 *							(Note: Also removed comment header) 
 *							added addEndTrans()
 *							defect 6721 Ver 5.1.5 Fix 2
 *							Note: Also reorganized import statements
 * K Harrell	01/12/2004  Ensure transtime on IRENEW transaction cache
 *							is the same as the other TransactionCache 
 *							Objects in that UOW
 *							modified populateInternetDbTables()
 *							Defect 6782 Ver 5.1.5 Fix 2
 * K Harrell	01/12/2004  Deprecate unused Transaction.endTrans(). 
 *							This code has been replace with 
 *							endTransForVoid().
 *							deprecated endTrans(CompleteTransactionData,
 *							TransactionHeaderData)
 *							defect 6784 Ver 5.1.5 Fix 2
 * Jeff S.		02/23/2004	Changed from FTP to LPR printing. Removed
 *							call to open and close FTP connection.
 *							Replaced calls to genBarCodeReceiptFile()
 *							with direct sendToPrinter() calls and let
 *							the printDocManager handle printing of dups
 *							and barcode files.
 *							modify addTrans(CompleteTransactionData),
 *							endTrans(Vector,TransactionHeaderData,
 *							Vector), genDealerCompletedReport(Vector),
 *							printAllReceipts(),
 *							printDocument(CompleteTransactionData, 
 *							boolean)
 *							defect 6848, 6898 Ver 5.1.6
 * K Harrell	03/30/2004	5.2.0 Merge
 *							add addTrans(CompleteTransactionData,Vector,
 *							 Vector), deserializeObject(),endTransReceipt(),
 *							 serializeObject(),UpdateReprintSticker()
 * 							modify genDealerCompletedReport() 
 * 							Ver 5.2.0
 * K Harrell	04/20/2004	transaction processing for RPRSTK
 *							add TransactionControl details for RPRSTK
 *							modify populateTrans()
 *							defect 7018 Ver 5.2.0  
 * K Harrell	04/27/2004	update voidtrans.log on Void
 *							deprecate endTransReceiptVoid(int)
 *							modify setUpFirstTransactionOfDay()
 *							add endTransReceiptVoid(CompleteTransactionData)
 *							add deleteVoidTransLog()
 *							defect 7019  Ver 5.2.0
 * K Harrell	05/05/2004	On InvVd, set PrntInvQty to 0.
 *							modify popultateInvFuncTransTrInvDetail()
 *							defect 5872  Ver 5.2.0
 * K Harrell	05/07/2004	rework 7019
 *							delete endTransReceiptVoid(CompleteTransactionData)
 *							modify endTransForVoid()
 *							defect 7019 Ver 5.2.0
 * K Harrell	05/25/2004	Continue work w/ AutoEndTrans
 *							modify shTransactionControl hash table for
 *							REFUND,HOTCK,HOTDED,HCKITM,RFCASH,
 *							SLVG,SCOT,NRCOT,DPLSLG 
 *							modify addTrans(CompleteTransactionData,
 *							Vector,Vector,boolean)
 *							deprecated automaticEndTrans()
 *							defect 7102 Ver 5.2.0
 * Jeff S.		05/26/2004	Using one call to get page properties (PCL)
 *							Print.getDefaultPageProps() so that keeping
 *							up with trays are alot easier.
 *							modify formatReceipt(Vector)
 *							modify genDealerCompletedReport(Vector)
 *							defect 7078 Ver 5.2.0
 * K Harrell	06/04/2004	Where PrintableIndi = 1 && ItmTrckngType = "P"
 *							assign invitmno to last 6 digits of VIN
 *							modify populateInvFuncTransTrInvDetail()
 *							defect 7137 Ver 5.2.0
 * K Harrell	06/10/2004	HQ should generate a unique custseqno for
 *							all Non-BackOffice Transactions.
 *							modify addTrans(CompleteTransactionData,
 *							Vector,Vector,boolean)
 *							modify isTreatedasBackOffice()
 *							defect 7154 Ver 5.2.0
 * Jeff S.		06/22/2004	Add tray definition to Salvage Title 
 *							Document
 *							modify printDocument(
 *							CompleteTransactionData, boolean)
 *							defect 7184 Ver. 5.2.0
 * K Harrell	06/22/2004	In addTrans, do not print if PrintImmediate
 *							&& AutoEndTrans
 *							modify addTrans(CompleteTransactionData,
 *							Vector,Vector,boolean)
 *							defect 7219  Ver 5.2.0
 * Jeff S.		07/06/2004	Temp fix to subtract one hour off the
 *							RTSDate for the transHeader timestamp. This
 *							is to help with the Elpaso Mountain Time
 *							zone issue.
 *							modify populateTransData(Vector, 
 *							TransactionHeaderData)
 *							defect 7255 Ver. 5.2.0
 * K Harrell	07/11/2004	modifications for 5.2.1 Subcon/PCR 34
 *							modify addTrans(CompleteTransactionData,
 *							Vector,Vector,boolean)
 *							modify saveReceipt()
 *							PCR 34  Ver 5.2.1
 * J Rue		08/03/2005  Catch all exceptions. Continuing processing.
 *							modify genDealerCompletedReport()
 *							defect 8309 Ver 5.2.2 Fix 6
 * K Harrell	08/12/2004	use CommonValidations.isHq() vs.
 *							!(UtilityMethods.isCounty || .isRegion)
 *							as Subcon transactions built at Server
 *							modify populateFundFuncTrnsTrFdsDetail()
 *							PCR 34 Ver 5.2.1
 * K Harrell	08/13/2004	Use correct variable from CompleteTransaction
 *							data to determine DealerId for StickerPrinting
 *							modify populateInvFuncTransTrInvDetail()
 *							defect 7437 Ver 5.2.1
 * K Harrell	08/17/2004	Always move VIN & DocNo to RTS_TRANS in Subcon
 *							modify populateTrans()
 *							defect 7458 Ver 5.2.1
 * J Rue		08/18/2004	Write TITLE APPLICATION RECEIPT to rcpt log
 *							for all DTA receipts
 *							modify saveReceipt()
 *							defect 7436 Ver 5.2.1
 * J Rue		08/20/2004	DTA-Print Sticker selected on DTA008, 
 *							print Sticker and County receipts.
 *							modify addTrans(
 *							Vector, CompleteTransaction Data, vector)
 *							saveReceipt()
 *							defect 7430 Ver 5.2.1
 * K Harrell	08/21/2004	Correctly populate InvLocIdCd,InvId,
 *							IssueMisMatchIndi for SBRNW ATVS
 *							modify populateInvFuncTransTrInvDetail()
 *							defect 7477 Ver 5.2.1
 * K Harrell	08/30/2004	Use postTrans to process ReprintSticker
 *							transaction.
 *							modify updateReprintSticker()
 *							defect 7349 Ver 5.2.1
 * K Harrell	08/31/2004	SBRNW Cleanup for RTS_TR_INV_DETAIL
 *							modify populateInvFuncTransTrInvDetail()
 *							defect 5963  Ver 5.2.1
 * J Zwiener	09/17/2004	Capture AuditTrailTransid for MV_Func_Trans
 *							modify populateMvTrans()
 *							defect 7553 Ver 5.2.1
 * K Harrell	10/10/2004	Modifications for Subcon Rearchitecture
 *							add ciSavedTransTime,ciCacheTransTime
 *							add getSavedTransTime(),setCacheTransTime(),
 *							setSavedTransTime()
 *							deprecate saveSubconInfo(),setSubconTransTime()
 *							modify addTrans(),createDeleteAllTransVector(),
 *							endTrans(),populateFundFuncTrnsTrFdsDetail(),
 *							populateInvFuncTransTrInvDetail(),
 *							populateMvTrans(),populateTrans(),
 *							populateTransData(),saveReceipt(),
 *							constructors
 *							documentation change for updateReprintSticker()
 *							defect 7586  Ver 5.2.1
 * K Harrell	10/13/2004	printAllReceipts should check for the
 *							existence of RCPTLOG.
 *							defect 7624  Ver 5.2.1
 * K Harrell	10/25/2004	Correct population of ComptCntyNo for EFTFND
 * 							&& VOIDFD
 *							modify populateFundFuncTrnsTrFdsDetail()
 *							defect 7650  Ver 5.2.2
 * K Harrell	10/25/2004	Remove reference to isFirstTransactionOfDay()
 *							deprecate isFirstTransactionOfDay()
 *							modify addTransForVoid() 
 *							defect 7581  Ver 5.2.2
 * K Harrell	11/15/2004	Adjust ciTime for Server Created Transactions
 *							modify populateTrans()
 *							add imports for above
 *							defect 6780  Ver 5.2.2
 * K Harrell	11/30/2004	Plate Age calc moved to populateMVTrans
 *							from INV007.actionPerformed()
 *							add calculatePlateAge()
 *							modify populateMVTrans()
 *							defect 7737 Ver 5.2.2
 * J Rue		12/09/2004	Do not write back to the dealer diskette if
 *							NON-RSPS.
 *							If RSPS then update the RSPS data only.
 *							Retain the original dealer data.
 *							modify genDealerCompletedReport()
 *							defect 7738 Ver 5.2.2
 * K Harrell	12/17/2004  change reference from isIsRecordRejected()
 *							to isRecordRejected()
 *							Formatting/JavaDoc/Variable Name Cleanup 
 * 							modify genDealerCompletedReport() 
 *							defect 7736 Ver 5.2.2
 * Jeff S.		01/04/2005	Added printer reset to the beginning of all
 *							documents that are printed. COA, CCO, SCOT
 *							NRPCOT, and SALV.
 *							modify printDocument()
 *							defect 7826 Ver 5.2.2
 * K Harrell	01/25/2005	Do not calculate plate age unless plate or
 *							sticker is issued. Fix to defect 7737. 
 *							modify calculatePlateAge() 
 *							defect 7799 Ver 5.2.2
 * Ray Rowehl	02/08/2005	Move Transaction to services.common
 * 							modify package
 * 							defect 7705 Ver 5.2.3
 * Ray Rowehl	02/18/2005	Change reference to NoHeaderOutputStream.
 * 							Change reference to TransactionControl.
 * 							They are now in services.data.
 * 							defect 7705 Ver 5.2.3
 * K Harrell	04/29/2005	Payment required for RFCash.
 *							modify addEndTrans()
 *							defect 8083 Ver 5.2.3 from Ver 5.2.2 Fix 3
 * K Harrell	04/29/2005	Java 1.4 Work 
 * 							deprecate isPrintCashReceipt()
 *							delete automaticEndTrans()
 *							delete setSubconTransTime(),
 *							endTrans(CTData,TransHdrData),
 *							saveSubConInfo(),
 *							isFirstTransactionOfDay()
 *							defect 7885	  Ver 5.2.3 
 * K Harrell	05/03/2005	Remove references to MiscData in MFVehicle
 * 							modify populateMvTrans()
 * 							defect 8188   Ver 5.2.3 
 * K Harrell	05/04/2005 	Adjust TransHdr timestamp for Mountain Time
 * 							for custseqno 0.
 * 							modify setUpFirstTransactionOfDay() 
 * 							defect 8004   Ver 5.2.3
 * K Harrell	05/19/2005	Rename of GenVehInquiryReceipt 	
 * 							Ver 5.2.3	  
 * J Rue		06/15/2005	Match RSPS getters/setters set in 
 * 							DealerTitleData to better define their 
 * 							meaning.
 * 							modify genDealerCompletedReport()
 * 							modify populateInvFuncTransTrInvDetail()
 * 							defect 8217 Ver 5.2.3
 * J Rue		07/13/2005	Rearrange the DTA proces that will generate
 * 							Dealer Completed Report, Print all receipts,
 * 							Set CSN number, (RSPS Only) update POS data 
 * 							on each record and write back to the 
 * 							diskette 
 * 							modfiy genDealerCompletedReport()
 * 							defect 8227 Ver 5.2.2 Fix 6
 * J Zwiener	07/17/2005  Enhancement for Disable Placard event
 * 							modify class variables
 * 							modify formatReceipt()
 * 							modify saveReceipt()
 * 							defect 8268 Ver 5.2.2 Fix 6
 * K Harrell	08/19/2005	Throw Exception if setupFirstTransactionOfDay()
 * 							called from Server Side
 * 							Add'l class cleanup, esp for 6848,6898 
 * 							delete DLRTITLE
 * 							modify setupFirstTransactionOfDay() 
 * 							defect 8285 Ver 5.2.3
 * K Harrell	08/23/2005	Reduce the parameters for addTrans(). Also
 * 							remove unneeded logic in addTransForVoid()
 * 							delete addTrans(CTData)
 * 							addTrans(CTData,Vector,Vector,boolean)
 * 							add addTrans(CTData,Vector)
 * 							modify addTransForVoid()   
 * 							defect 8344 Ver 5.2.3
 * K Harrell	08/28/2005	Remove references to RLSALL, TTLREJ, TTLRLS,
 * 							FNDADJ,INVADJ
 * 							modify populateFundFuncTrnsTrFdsDetail(),
 * 							saveReceipt()
 * 							defect 8348 Ver 5.2.3 
 * K Harrell	08/25/2005	Do not assign DlrId to MvFunc SubconId
 * 							populateMvTrans() 
 * 							defect 8351 Ver 5.2.3 
 * K Harrell	08/28/2005	Ignore files in TRXCache directory which 
 * 							are not numeric.
 * 							modify cacheIO() 
 * 							defect 8352 Ver 5.2.3 
 * K Harrell	08/28/2005	Transaction Class Cleanup / Refactor
 *   						add caCommonClientBusiness & saCompTransData
 * 							 and modified methods as appropriate  
 * 							add getCacheQueue(),getTransId(),
 * 							 processReceipt(), saveVehicleData(),
 * 							 updateRcptLogAddrRnr(), writeCumRcptLog()
 * 							delete getDTAinfo(),getSavedTransCd(),
 * 							 getStoredPrintIndi(),isMultipleTrans(),
 * 							 isPrintCashReceipt(),automaticEndTrans(),
 * 							 endTrans(CTData,..),endTransReceipt(),
 * 							 getCacheTransTime(),getSavedTransTime(),
 * 							 getSubconInventory(),saveSubconInfo()
 * 							modify variables from public to private
 * 							 as appropriate
 * 							defect 7885 Ver 5.2.3 
 * K Harrell	08/28/2005	Modify the Transaction Control definitions
 * 							for REFUND, HOTCK,HOTDED,HCKITM 
 * 							as payment not required. 
 * 							defect 8354 Ver 5.2.3
 * K Harrell	08/31/2005	Make SUCCESS/FAILURE private
 * 							defect 8353 Ver 5.2.3
 * K Harrell	09/29/2005	refactoring
 * 							renamed isFeesIncurred() to doFeesExist() 
 * 							defect 7885 Ver 5.2.3
 * K Harrell	11/16/2005	Enhancement to handling errors during trans
 * 							processing
 * 							add saPriorRunningSubtotal
 * 							modify addTrans(),endTrans(),
 * 							processCacheVector(),
 * 							populateFundFuncTrnsTrFdsDetail(),
 * 							updateCumRcptLog(),writeCumRcptLog()
 * 							defect 6456 Ver 5.2.3
 * K Harrell	12/05/2005	Restore Correct Trans Control for CKREDM
 * 							cbBuildMVFuncTrans to true. Restore assignment
 * 							of laRegistrationDataNew 
 *							modify populateTrans() 							
 * 							defect 7885 Ver 5.2.3  
 * J Rue		12/14/2005	Move updateRecord process to inside the for
 * 							loop.
 * 							Check to ensure diskette is in the A:\ drive 
 * 							add ensureDiskInADrive() 
 * 							modify genDealerCompletedReport() 
 * 							defect 7898 Ver 5.2.3
 * K Harrell	12/21/2005	Error on More Trans after CKREDM 
 * 							modify ifTreatedAsBackOfficeTransaction()
 * 							defect 7885 Ver 5.2.3  
 * J Rue		01/02/2006	Correct problem when making call to 
 * 							ensureDiskInADrive(TransCd), move block of
 * 							code inside For loop. If diskette is not in
 * 							A:\drive and 3 attempts have been made,
 * 							return to main menu
 * 							modify genDealerCompletedReport() 
 * 							defect 7898 Ver 5.2.3
 * J Rue		01/04/2006	Update diskette only if RSPS Id exist on 
 * 							diskette file, else return to main menu.
 * 							modify genDealerCompletedReport() 
 * 							defect 7898 Ver 5.2.3
 * K Harrell	01/10/2006	Replace existing uses of lsTransId w/ 
 * 							csTransId
 * 							modify setUpTransaction()
 * 							defect 7885 Ver 5.2.3
 * K Harrell	01/10/2006	Replaced  "System.getProperty(
 *							CommonConstant.SYSTEM_LINE_SEPARATOR)" with
 *							CommonConstant.SYSTEM_LINE_SEPARATOR
 *							modify genDealerCompletedReport()
 *							defect 7885 Ver 5.2.3
 * J Ralph		01/27/2006	FormFeed constant cleanup
 * 							Source format
 * 							modify barcodeNeeded(), formatReceipt(),
 * 							printDocument(), saveCashReceipt(),
 * 							processReceipt()
 * 							defect 8524 Ver 5.2.3
 * K Harrell	02/13/2006	Consolidate assignment of MFDWNCD 
 * 							modify populateMvTrans()
 * 							defect 6861 Ver 5.2.3 	   
 * J Rue		02/15/2006	Ensure disk is in the A:\\ drive before
 * 							writing updated dealer data to DLRTITLE.DAT
 * 							modify genDealerCompletedReport()
 * 							defect 8227 Ver 5.2.3
 * K Harrell	03/14/2006	Remove test for AutoEndTrans and Print 
 * 							Receipt when saving Complete Transaction
 * 							Data 
 * 							modify processReceipt()
 * 							defect 8625 Ver 5.2.3 	
 * J Rue		04/05/2006	Change variable value from DTASTKR to 
 * 							DLRSTKR
 * 							modify DLRSTKR  (class variable)
 * 							defect 7430 Ver 5.2.3
 * K Harrell	08/14/2006	CCO was not printing 1st receipt of the day.
 * 							modify processReceipt()
 * 							defect 8886 Ver 5.2.4    
 * B Hargrove 	09/28/2006  Change check for Exempt RegClassCd (39)
 * 							to check for FeeCalcCat = 4 (No Regis Fees).
 * 							modify isSpecialRegistration()
 * 							delete EXEMPT_REG_CLASS_CD
 * 							defect 8900  Ver Exempts
 * K Harrell	10/07/2006	Insert, Update, Delete ExemptAudit
 * 							Populate new TERP fields.
 * 							add populateExemptAudit()
 * 							modify populateTransData(Vector,
 * 							  TransactionHeaderData),
 * 							  populateTransData(CompleteTransactionData,
 * 							  TransactionHeaderData), 
 * 							  createDeleteAllTransVector(),
 * 							  createDeleteSelectedTramsVector()
 * 							defect 8900, 8903 Ver Exempts 
 * K Harrell	10/10/2006	Do not disregard 0.00 fees in Subcon
 * 							modify populateFundFuncTrnsTrFdsDetail()
 * 							defect 8900 Ver Exempts 
 * Min Wang		10/10/2006	New Requirement for handling plate age 
 * 							modify calculatePlateAge(), populateMvTrans()
 *							defect 8901 Ver Exempts
 * Min Wang		10/17/2006	remove calculate plate age from 
 * 							calculatePlateAge()
 * 							modify calculatePlateAge()
 * 							defect 8901 Ver Exempts
 * K Harrell	10/17/2006	Make DRVED autoendTrans. Remove assignment
 * 							to ZERO_DOLLAR; 
 * 							modify entry in shTransactionControl
 * 							defect 8900 Ver Exempts
 * K Harrell	10/18/2006	Populate MVFuncTrans with PltBirthDate
 * 							modify populateMvTrans()
 * 							defect 8901  Ver Exempts  
 * K Harrell	10/23/2006	Build FundsDetail/FundFuncTrans for DRVED 
 * 							modify populateFundFuncTrnsTrFdsDetail()
 * 							defect 8900  Ver Exempts
 * K Harrell	10/29/2006	Use Ofcissuanceno from TransHdrData vs. 
 * 							CompleteTransactionData.  Corrected typo
 * 							in isSpecialRegistration()
 * 							modify populateFundFuncTrnsTrFdsDetail(),
 * 							isSpecialRegistration()
 * 							defect 8900  Ver Exempts
 * K Harrell	11/14/2006	Update calculation of plate age when 
 *  Min Wang				 issue new plate.
 * 							modify calculatePlateAge()
 * 							defect 8901  Ver Exempts
 * Min Wang		12/08/2006	Do not calculate the plate age in 
 * Ray Rowehl				populateMvTrans() unless we are populating
 * 							MVFunc.
 * 							Block nullpointers when setting the plate age.  
 * 							modify calculatePlateAge(), populateMvTrans()
 * 							defect 9056 Ver Exempts
 * T Pederson	01/29/2007	Set AcctItmCd for regional office charging
 * 							credit card fee.
 * 							modify populateFundFuncTrnsTrFdsDetail()
 * 							defect 9088  Ver Region Credit Card
 * J Rue		08/07/2007	Add LemonLawIndi
 * 							ChildSupportArrearsIndi will replace
 * 							LemonLawIndi
 * 							modify populateMvTrans()
 * 							defect 9225 Ver Broadcast Messaging
 * K Harrell	02/01/2007	Use PlateTypeCache vs. 
 *  							RegistrationRenewalsCache
 * 							modify saveVehicleData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/06/2007	Add methods necessary for SR_Func_Trans
 * 							transaction processing. Added elements into
 * 							transaction control for Special Plates 
 * 							transcds. 
 * 							add SPCLPLT
 * 							add populateSRFuncTrans()
 * 							modify createDeleteAllTransVector(),
 * 							 createDeleteSelectedTransVector(),
 *                           populateSRFuncTrans()
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/13/2007	Customize populateSRFuncTrans() for SBRNW
 * 							add isSpecialPlateTransaction()
 * 							... TO BE CONTINUED... 
 * 							defect 9085 Ver Special Plates  	
 * J Rue		02/22/2007	Rename getSpclPltRegis() to 
 * 							getSpclPltRegisData()
 * 							modify isSpecialPlateTransaction(),
 * 							modify populateSRFuncTrans()
 * 							defect 9086 Ver Special Plates
 * K Harrell	02/25/2007	Continued work for SpclPltes
 * 							modify populateSRFuncTrans(),
 * 							 populateSpclPltTransHstry()
 * 							defect 9085 Ver Special Plates
 * J Rue		02/26/2007	Change SpclRegId from Dollar to long
 * 							modify populateSRFuncTrans()
 * 							defect 9086 Ver Special Plates
 * K Harrell	03/04/2007	Continued work on Special Plates
 * 							modify populateSRFuncTrans()
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/09/2007	Use SystemProperty.isHQ()
 * 							modify createTransactionControl()
 * 							defect 9085 Ver Special Plates 		
 * B Hargrove	03/09/2007	Add Special Plate Application receipt.
 * 							modify formatReceipt(), saveReceipt()
 * 							defect 9126 Ver Special Plates
 * Jeff S.		03/29/2007	Added to handle printing a receipt for 
 * 							a renewal of a special plate.
 * 							modify formatReceipt();
 * 							defect 9145 Ver Special Plates
 * K Harrell	04/05/2007	Working ..
 * 							add SPRNR to shTransactionControl
 * 							add deletion of saved special plate veh
 * 							 data on transaction completion.
 * 							SPREV not AutoEndTrans. Move Address
 * 							data from MFVehicleData to SpecialPlates
 * 							data when !SpclPlates events.
 * 							modify populateSRFuncTrans()
 * 							modify reset()
 * 							defect 9085 Ver Special Plates 
 * J Rue		04/06/2007	Add check for SP - VehInq
 * 							modify formatReceipt()
 * 							defect 9086 Ver Special Plates
 * K Harrell	04/09/2007	Use SystemProperty.isHQ(), isRegion()
 * 							delete isRegionalOffice()
 * 							correct population of SpclPltTransHstry
 * 							modify populateSpclPltTransHstry() 
 * 							defect 9085 Ver Special Plates
 * Jeff S.		04/10/2007	Added to handle printing a receipt for 
 * 							a revise of a special plate and changed the
 * 							file name of the receipt to SPCLPLT.
 * 							modify formatReceipt(), saveReceipt()
 * 							defect 9145 Ver Special Plates
 * K Harrell	04/22/2007	Followup to 9126/9145. Need to add SPRNR
 * 							Use caTransactionControl.EventType to 
 * 							manage printing for SpclPlates
 * 							add getTransactionControl()
 * 							modify formatReceipt(), saveReceipt()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/27/2007	Only build SR_FUNC_TRANS when we also
 * 							build MV_FUNC_TRANS. Corrected misplaced
 * 							bracket for MV_FUNC_TRANS creation 
 * 							modify populateSRFuncTrans(),
 * 							 populateMVTrans()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/01/2007	Transaction not reset after back office 
 * 							transaction
 * 							modify saveVehicleData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/15/2007	Add code to determine exp yr for Manufactured
 * 							Special Annual Plate. Set PltSetNo in 
 * 							SRFuncTrans. 
 * 							modify populateSRFuncTrans()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/29/2007	add IAPPL to TransactionControl, 
 * 							add ItrntTraceNo to SpclPltTransHstry
 *							modify populateFundFuncTrnsTrFdsDetail(),
 *							 populateSRFuncTrans()
 *							defect 9085 Ver Special Plates
 * K Harrell	05/30/2007	Release Virtual Inventory on Void of SPAPPL
 * 							add updtSpclPltVIForVoid()
 * 							modify addTransForVoid()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	05/31/2007	Add'l work on Special Plates Transaction
 * 							add to TransactionControl: SPAPPC,SPAPPI,
 * 							 SPAPPR, SPAPPO. 
 * 							modify populateSRFuncTrans(), 
 * 								saveVehicleData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/01/2007	Save TransId for "Same Special Plate"
 * 							modify saveVehicleData() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/06/2007	Save OrigTransCd w/ saved Special Plate. 
 * 							Update VI TransTime when add Transaction
 * 							Save VI data on AutoEndTrans
 * 							add updateSpclPltVITransTime()
 * 							modify saveVehicleData(),addTrans()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/08/2007	Add'l work on VI update 
 * 							modify saveVehicleData(), updateSpclPltVIForVoid(),
 * 							 updateSpclPltVITransTime(), addTransForVoid()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/18/2007	Add'l work for ShowCache
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/19/2007 	Add RegClassCd on Transactions w/ PLPDLR, 
 * 							 PLPDLRMC Plates
 * 							modify populateMvTrans()  
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/20/2007	Call to SpclPltTransHstry even if do not 
 * 							build MV_FUNC_TRANS.  (See 4/27 entry)
 * 							modify populateSRFuncTrans()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/25/2007	Add InvItmYr on Void of Spcl Plt 
 * 							modify updateSpclPltVIForVoid()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/05/2007	Restored code lost in merge.(?)
 * 							modify addTrans()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/09/2007	Do not disable same special plate in HQ after
 * 							SPAPPL, SPREV, SPRNR, SPRNW
 * 							modify reset()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/10/2007	Reset SpecialPlatesRegisData.setEnterOnSPL002
 * 							when save. Check for null saCompTransData 
 * 							when reset.
 * 							modify saveVehicleData(), reset()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/19/2007	Do not populate SR_FUNC_TRANS when SBRNW
 * 							modify populateSRFuncTrans()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/25/2007	Set Plate BirthDate on SPAPPC 
 * 							modify saveVehicleData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/08/2007	Print Receipts for SPUNAC, SPDEL, SPRSRV
 * 							modify shTransactionControl entries 
 * 							defect 9237 Ver Special Plates
 * K Harrell	08/17/2007	Build/Send SR_FUNC_TRANS for VOID, not for
 * 							INVVD.
 * 							populateSRFuncTrans() 
 * 							defect 9259 Ver Special Plates
 * J Rue		08/23/2007	Add LemonLawIndi to MvFuncTrans
 * 							modify populateMvTrans()
 * 							defect 9225 Ver Broadcast Messaging
 * K Harrell	10/01/2007	Set Substaid on Void for Update to 
 * 							SpclPltTransHstry
 * 							modify populateSpclPltTransHstry()
 * 							defect 9340 Ver Special Plates
 * K Harrell	10/19/2007	Reset MustChangePltIndi when save vehicle 
 * 							data for "Same Vehicle" processing.
 *							defect 9368 Ver Special Plates 2
 * K Harrell	10/22/2007	SPAPPI now uses VI
 * 							modify addTrans(),updateSpclPltVIForVoid()
 * 							  populateSRFuncTrans()
 * 							defect 9386 Ver Special Plates 2
 * K Harrell	11/04/2007	Update with Owner Address for same Vehicle
 * 							Processing 
 * 							modify saveVehicleData()
 * 							defect 9389 Ver Special Plates 2
 * K Harrell	11/05/2007	Void of VehInq update to SpclPltTransHstry
 * 							modify populateSRFuncTrans(), 
 * 							  populateSpclPltTransHstry()
 * 							defect 9423 Ver Special Plates 2
 * K Harrell	11/16/2007	Reset SpecialPlates "reset" charge fee 
 * 							indicator for future transactions. 
 * 							modify saveVehicle()
 * 							defect 9450 Ver Special Plates 2
 * K Harrell	01/16/2008	Reinstate code to assign MfgPltNo when 
 * 							Special Plate assigned from Inventory, e.g. 
 * 							in REPL. 
 * 					        modify populateSRFuncTrans()
 * 							defect 9487 Ver 3 Amigos Prep 
 * B Hargrove	01/24/2008	Added V21TRANS for TransCd V21VTN.
 * 							Vision 21 Transaction for:
 * 							Vehicle Transaction Notification from Vision 21
 * 							defect 9502 Ver 3_Amigos_PH_A
 * K Harrell	02/04/2008  Use TitleData.ChildSupportIndi vs. 
 * 							LemonLawIndi 
 * 							modify populateMVFuncTrans() 
 * 							defect 9546 Ver 3 Amigos PH A 
 * B Hargrove	02/19/2008	Added V21PLD, G36VTN for V21TRANS
 * 							Vision 21 Transaction codes for:
 * 							Plate Disposition from Vision 21
 * 							Vehicle Transaction Notification from Global 360
 * 							defect 9502 Ver 3_Amigos_PH_A
 * 							defect 9450 Ver Special Plates 2
 * K Harrell	03/11/2008	COA writing LienholderData to MvFuncTrans
 * 							modify populate MvTrans()
 * 							defect 6424 Ver Defect POS A
 * K Harrell	03/11/2008	Null pointer on Same Vehicle after REJCOR
 * 							modify saveVehicleData()
 * 							defect 6073 Ver Defect POS A
 * J Rue		04/01/2008	Add ChildSupportIndi,TtlSignDate,ETtlCd 
 * 							modify populateMvTrans()
 * 							defect 9581 Ver Defect POS A
 * K Harrell	04/07/2008	Add processing for DissociateCd, V21VTNId,
 * 							 V21PltId, VTNSource, PrismLvlCd,
 * 			                 TtlTrnsfrPnltyExmptCd, TtlTrnsfrEntCd
 * 							modify populateMvTrans(),
 * 							 populateSRFuncTrans()  
 * 							defect 9581 Ver Defect POS A 
 * B Hargrove	04/07/2008	Comment out references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe (see: defect 9557).
 * 							modify populateMvTrans()
 * 							defect 9631 Ver Defect POS A
 * K Harrell	04/30/2008	Add Transaction Control Processing for 
 * 							CORSLV, CORNRT.  Remove for SLVG, DPLSLG
 * 							modify printDocument
 * 							defect 9636 Ver Defect POS A
 * J Rue		04/30/2008	DissociateCd was change to PltRmvdCd
 * 							modify populateMvTrans()
 * 							defect 9581 Ver Defect POS A
 * K Harrell	05/07/2008	Copy Salvage Reports to Receipt Directory
 * 							Print Duplicates even if "CCO" type events
 * 							Include logic to treat COA as "NRVT"  
 * 							add copyDocToRcptDirectory()
 * 							modify printDocument()
 * 							modify COA transaction control entry
 * 							defect 9643, 9642 Ver Defect POS A
 * K Harrell	05/21/2008	Remove reference to TtlTrnsfrPnltyExmptCd
 * 							modify populateMvTrans()
 * 							defect 9581 Ver Defect POS A
 * K Harrell	05/22/2008	If Plate Removed, populate RegPltno from 
 * 							 saved value.
 * 							modify populateMvTrans(), populateTrans()
 * 							defect 9670 Ver Defect POS A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9670 Ver Defect POS A
 * J Rue		06/17/2008	Replace reference from 6 digit DlrGDN to 10
 * 							digit DlrGDN
 * 							modify populateSRFuncTrans(), 
 * 								populateMvTrans()
 * 							defect 9557 Ver Defect POS A
 * K Harrell	06/22/2008	Restore reference to TtlTrnsfrPnltyExmptCd
 * 							modify populateMvTrans()
 * 							defect 9724 Ver Defect POS A
 * 							defect 9450 Ver Special Plates 2
 * B Hargrove	06/13/2008	If Vendor Plate, do not overwrite the   
 * 							Special Plate Regis Data object Expiration 
 * 							Month\Year. We need this to still have the
 * 							Vendor Plate RegExpMo\Yr (plate expiration) 
 * 							to print on receipt.
 * 							Note: there are no 'PlateExpMo\Yr' fields,
 * 							we are using RegExpMo\Yr fields.   
 * 							modify populateSRFuncTrans()
 * 							defect 9689 Ver Defect MyPlates_POS
 * B Brown		07/11/2008	Added VPAPPL, and VPAPPR for vendor
 * 							Special Plate processing.
 * 							modify populateFundFuncTrnsTrFdsDetail()
 * 							defect 9711 Ver MyPlates_POS
 * B Brown		07/14/2008	Comptcntyno and itrnttraceno in fund_func_
 * 							trans need to be populated properly.
 * 							Also, populate transEmpId in rts_trans with
 * 							the transcd like IAPPL does. 
 * 							modify populateFundFuncTrnsTrFdsDetail(),
 * 							populateTrans()
 * 							defect 9711 Ver MyPlates_POS
 * K Harrell	08/26/2008	Write to log when request CashDrawer open
 * 							in development. 
 * 							modify endTrans() 
 * 							defect 7111 Ver Defect_POS_B
 * K Harrell	09/10/2008	Reset Supervisor Override Code / Reason when 
 * 							save Vehicle Data for MoreTrans processing.
 * 							modify saveVehicleData()
 * 							defect 9823 Ver Defect_POS_B 
 * K Harrell	10/27/2008	Modifications for Disabled Placard
 * 							add DLBPM, DLBTM, DLRPNM, DLRTNM, RPBPM,
 * 							  RPBTM, RPRPNM, RPRTNM to TransactionControl
 * 							add ADD_DP, DEL_DP, RPL_DP 
 * 							add populateDsabldPlcrdTrans()
 * 							modify createDeleteAllTransVector(), 
 * 							  formatReceipt(), populateTransData(),
 * 							  saveReceipt() 
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	11/01/2008	Populate VoidTransEmpId 
 * 							modify populateDsabldPlcrdTrans() 
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	01/07/2009 Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify updateSpclPltVIForVoid(), 
 * 							 populateSRFuncTrans(),
 * 							 populateSpclPltTransHstry()   
 *        					defect 9864 Ver Defect_POS_D
 * K Harrell	01/08/2009	Remove reference to RegisData.setOrgNo()
 * 							modify isSpecialPlateTransaction()
 * 							defect 9912 Ver Defect_POS_D
 * K Harrell	01/08/2009	Reset PrismLvlCd for DTA*, TITLE, SCOT, 
 * 							 NRCOT, COA transactions
 * 							modify populateMvTrans() 
 * 							defect 9903 Ver Defect_POS_D
 * J Rue		01/16/2009	Display error message and write to log file
 * 							when IOException is thrown from 
 * 							updateRecord(). Return to main menu
 * 							modify genDealerCompletedReport()
 * 							defect 8918 Ver Defect_POS_D
 * J Rue		01/23/2009	Use RTSException(String, Exception) to 
 * 							retain focus on the message box.
 * 							add method to handle RSPS exceptions
 * 							add handleRSPSExceptions()
 * 							modify genDealerCompletedReport()
 * 							defect 8918 Ver Defect_POS_D
 * K Harrell	01/26/2009	Set indicator to disable Cancel for CCO. 
 * 							modify setCancelTrans(), processReceipt(),
 * 							  reset()
 * 							defect 7717 Ver Defect_POS_D 
 * J Zwiener	01/29/2009	Copy over Renewal Recipient addr, if avail.,
 * 							to SR Func Trans for non-special plate
 * 							events.  Otherwise, use Owner address.
 * 							modify populateSRFuncTrans()
 * 							modify saveVehicleData() (02/05/2009)
 * 							defect 9893 Ver Defect_POS_D
 * J Rue		02/02/2009	Add check to ensure the app is NOT at the server
 * 							before displayError() is called.
 * 							modify handleRSPSExceptions()
 * 							defect 8918 Ver Defect_POS_D
 * K Harrell	02/03/2009	Save TransHdr Object for resetting 
 * 							sbAllowCancel upon restore.
 * 							add SAVED_TRANS_HDR  
 * 							modify saveVehicle(), restoreSetAside(),
 * 								setAside() 
 * 							defect 7717 Ver Defect_POS_D
 * K Harrell	03/03/2009	Modify to use w/ Lienholder Address Data
 * 							modify populateMvTrans()
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	03/12/2009	Trans changes for ELT. 
 * 							populateMvTrans()
 * 							defect 9981 
 * K Harrell	03/20/2009	ETitle Report Changes 
 * 							add populateETtlHstry()
 * 							modify populateTrans(), 
 * 							  createDeleteAllTransVector(), 
 * 							  populateTransData(CTData,TransHdrData), 
 * 							  populateTransData(Vector,TransHdrData) 
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	04/08/2009	Added logic to reassign DlrTtlData ETtlRqst
 * 							modify populateMvTrans()
 * 							defect 9971 Ver Defect_POS_E 
 * K Harrell 	05/29/2009	Implement correct time for set-aside 
 * 							transactions in transaction cache. 
 * 							modify setAside()
 * 							defect 10085 Ver Defect_POS_F 
 * B Hargrove	06/01/2009  Add Flashdrive option to DTA. Change 
 * 							verbiage from 'diskette'.
 * 							modify genDealerCompletedReport()
 * 							defect 10075 Ver Defect_POS_F  
 * B Hargrove	06/08/2009	Remove all 'Cancelled Sticker' references.
 * 							modify populateMvTrans()
 * 							defect 9953 Ver Defect_POS_F
 * K Harrell	06/19/2009	UtilityMethods.saveReport(), 
 * 							 Print.getDefaultPageProps() now static
 * 							modify formatReceipt(),
 *							 genDealerCompletedReport()
 * 							defect 10023 Ver Defect_POS_F
 * K Harrell	07/12/2009	Implement new OwnerData()
 * 							modify addTransHeader(), updateCumRcptLog(),
 * 							 populateSRFuncTrans(),
 * 							 populateSpclPltTransHstry(),
 * 							 populateInvFuncTransTrInvDetail(),
 * 							 populateMvTrans(), populateTrans()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/26/2009	Modify for HB3095: Disregard Mobility 
 * 							 for Disabled Placards 
 * 							modify shTransactionControl
 * 							defect 10133 Ver Defect_POS_F 
 * K Harrell	08/11/2009	Modifications for new DB2 Driver 
 * 							modify processCacheVector() 
 * 							defect 10164 Ver Defect_POS_E'/F    
 * J Zwiener	08/25/2009	Add new fields to Spcl_Plt_Trans_Hstry
 * 							modify populateSpclPltTransHstry(),
 * 							defect 10097 Ver Defect_POS_F
 * K Harrell	08/31/2009	Preserve LienxDate when 
 * 							 PermLienhldrIdx is populated. 
 * 							modify populateMvTrans()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	09/02/2009	Set PriorExpMo/Yr for Special Plate when 
 * 							save for MoreTrans
 * 							modify saveVehicleData()
 * 							defect 10097 Ver Defect_POS_F 
 * K Harrell	10/13/2009	Implement Report Constants for Dealer 
 * 							Completed Report.
 * 							delete DLRCOMPLRPT, DLRCOMPL, DLRCOMPLFILE 
 * 							modify genDealerCompletedReport()
 * 							defect 10251 Ver Defect_POS_G 
 * K Harrell	10/14/2009	Remove reference to PrivacyOptCd
 * 							delete  PRIVACY_OPT_CD
 *  						modify populateMvTrans()
 * 							defect 10246 Ver Defect_POS_G 
 * K Harrell	11/23/2009	Corrected merge Issues
 * 							Ver Defect_POS_H
 * K Harrell	12/16/2009	add svDTADlrTtlData, siDTADlrTtlDataIndex,
 *							 siDTADealerId, saDTADealerData, get/set 
 *							 methods
 *							add resetDTA(), setCurrDlrTtlData(),
 *							  genDTADealerCompletedReport() 
 *							delete deserializeObject(), 
 *							 isDTAPrintSticker(), setCurrDTAInfo(), 
 *							 genDealerCompletedReport() 
 *							modify addTransHeader(),deleteTrans(), 
 *							 populateInvFuncTransTrInvDetail(), reset(),
 *							 saveReceipt(), setUpTransaction() 
 *							defect 10290 Ver Defect_POS_H
 * K Harrell	12/18/2009	ciPltTerm removed from SpecialPlateRegisData
 * 							Use PltValidityTerm.
 * 							modify populateFundFuncTrnsTrFdsDetail()
 * 							defect 10311 Ver Defect_POS_H
 * K Harrell	12/30/2009	DocNo not populated in RTS_TRANS for DTAORK.
 * 							modify populateTrans() 
 * 							defect 10282 Ver Defect_POS_H 
 * K Harrell	01/13/2010	Performance Update for DTA.  Only write updated 
 * 							RSPS data once.
 * 							add svDTAOrigDlrTtlData, set method
 * 							modify genDTADealerCompletedReport(), 
 * 							 resetDTA()  
 * 							defect 10290  Ver Defect_POS_H
 * K Harrell	02/10/2010	Dealer Completed Report not created/printing
 * 							if followed by different transaction type. 
 * 							Checking last TransCd vs. Transaction Vector.
 * 							modify genDTADealerCompletedReport()
 * 							defect 10373 Ver Defect_POS_H 
 * K Harrell	02/10/2010 	Implement TitleData csPvtLawEnfVehCd, 
 * 								csNonTtlGolfCartCd
 * 							 vs. csVTRTtlEmrgCd1, csVTRTtlEmrgCd2
 * 							defect 10366 Ver POS_640
 * K Harrell	02/18/2010	Implement new SubcontractorData 
 * 							modify addTransHeader()
 * K Harrell	02/18/2010	Implement fields for Data Objects 
 * 							modify populateMVTrans(), populateSRFuncTrans(),
 * 							 populateSpclPltTransHstry()
 * 							defect 10375 Ver POS_640
 * K Harrell	03/23/2010	Reset SOReElectionIndi for New Title
 * 							modify populateMVTrans()
 * 							defect 10376 Ver POS_640 
 * K Harrell	04/03/2010	Implement OfficeTimeZoneCache 
 * 							modify populateTrans(), populateTransData(), 
 *  							setUpFirstTransactionOfDay() 
 * 							defect 10427 Ver POS_640 
 * Ray Rowehl	03/29/2010	Add VP Restyle Processing.
 * 							modify shTransactionControl
 * 							modify addEndTrans(),
 * 								populateFundFuncTrnsTrFdsDetail()
 * 							defect 10401 Ver 6.4.0
 * K Harrell	04/03/2010	Assign PltSoldMos to SRFuncTrans 
 * 							modify populateSRFuncTrans()
 * 							defect 10375 Ver POS_640 
 * Ray Rowehl	04/09/2010	Add VP transaction Processing.
 * 							modify shTransactionControl
 * 							modify addEndTrans(),
 * 								populateFundFuncTrnsTrFdsDetail()
 * 							defect 10401 Ver 6.4.0
 * T Pederson	04/09/2010 	Change so that SR_FUNC_TRANS will be 
 * 							written for subcontractor renewal trans
 * 							with new plate expiration and term.
 * 							modify populateSRFuncTrans() 
 *							defect 10392  Ver POS_640
 * Ray Rowehl	04/13/2010	Add VPPORT and VPREDO funtion.
 * 							modify shTransactionControl
 * 							modify populateTrans()
 *							defect 10401 Ver 6.4.0
 * K Harrell	04/29/2010  Did not print Replace Reason on Receipt	
 * 							RPLPDC/RPLTDC should have assigned 
 * 							 EventType of RPL_DP vs. ADD_DP
 * 							Did not print Replace Reason on Receipt  
 * 							modify shTransactionControl 
 * 							modify 10468 Ver 6.4.0 
 * T Pederson	05/02/2010 	Plate sold months was not getting set 
 * 							in SRFuncTrans for subcontractor renewal.
 * 							modify populateSRFuncTrans() 
 *							defect 10392  Ver POS_640
 * Ray Rowehl	05/04/2010	Need to add all VP** trans to lookup.
 *							modify populateFundFuncTrnsTrFdsDetail()
 *							defect 10401 Ver 6.4.0
 * K Harrell	05/24/2010  New Permit Processing	
 * 							modify shTransactionControl
 * 							modify populateTransData(),
 * 							 populateTrans(), reset(),  
 * 							 createDeleteAllTransVector(), 
 * 							 createDeleteSelectedTransVector(), 
 * 							 setCacheTransTime(), 
 * 							 addEndTrans(),addTrans(), addTransForVoid(), 
 * 							 createDeleteAllTransVector(), 
 * 							 createDeleteSelectedTransVector(),
 * 							 deleteTrans(), endTrans(), saveReceipt(), 
 * 							 saveVehicleData(), populateTrans(), 
 * 							 populateTransData() 
 * 							add populatePrmtTrans(), updatePrmtVIRequest(),
 * 							 updatePrmtVITransTime(), updatePrmtVIForVoid() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	06/04/2010	Assignment of SRFuncTrans for Void
 * 							modify populateSRFuncTrans()
 * 							defect 10500 Ver 6.4.0 
 * K Harrell	06/14/2010	add assignment of TransId
 * 							modify populateDsabldPlcrdTrans(), 
 * 							 populateETtlHstry(), populateExemptAudit(), 
 * 							 populatePrmtTrans(), populateSRFuncTrans(), 
 * 							 populateSpclPltTransHstry(), 
 * 							 populateFundFuncTrnsTrFdsDetail(), 
 * 							 populateInvFuncTransTrInvDetail(),
 * 							 populateMvTrans() 
 * 							defect 10505 Ver 6.5.0
 * K Harrell	06/15/2010	add support for EMailRenwlReqCd, 
 *     						 ElectionPndngIndi
 * 							modify populateMvTrans(), 
 * 							 populateSRFuncTrans()
 * 							defect 10507, 10508 Ver 6.5.0 
 * K Harrell	06/22/2010	Populate V21PltId, DissociateCd,
 * 							 V21VTNId, VTNSource only if V21 Transaction
 * 							modify populateMvTrans() 
 * 							defect 10476 Ver 6.5.0 
 * K Harrell	07/02/2010	Added support for generating Permit 
 * 							modify barcodeNeeded(),formatReceipt()   
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/14/2010	Added support for TR_INV_DETAIL records, 
 * 							tracking Reprints. Clear Stored PermitData
 * 							 if not PermitApplication.  
 * 							modify populateInvFuncTransTrInvDetail(),
 * 							 saveVehicle() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/15/2010	Correct behavior of "Same Vehicle" for 
 * 							Timed Permits
 * 							modify reset(), restoreSetAside() 
 * 							defect 10491 Ver 6.5.0 
 * Min Wang		07/19/2010  Using custom size for CCO.
 * 							modify printDocument()
 * 							defect 10398 Ver 6.5.0
 * K Harrell	07/18/2010	On Void, only write RTS_PRMT_TRANS w/ "VOID",
 * 							not "INVVD". (Added TR_INV_DETAIL 7/14/2010)
 * 							modify populatePrmtTrans()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/23/2010	Add population of VoidTransId for RTS_TRANS
 * 							modify populateTrans()
 * 							defect 10505 Ver 6.5.0 
 * K Harrell	08/02/2010	Error during devolopment, could not reproduce
 * 							In Title, Same Vehicle enabled after PRMDUP 
 * 							modify saveVehicleData() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	09/14/2010	Add processing for Preview Receipt 
 * 							add genPreviewRcpt()
 * 							modify populateTrans()
 * 							defect 10590   Ver 6.6.0  
 * K Harrell	09/20/2010	add assignment for TtlExmnIndi for population
 * 							in RTS_MV_FUNC_TRANS for Title Package Rpt. 
 * 							modify populateMvTrans() 
 * 							defect 10013 Ver 6.6.0
 * K Harrell	09/29/2010	Permit does not release vi if restored from 
 * 								setAside 
 * 							add SAVED_PRMT_VCT
 * 							modify saveVehicle(), restoreSetAside()  
 * 							defect 10617 Ver 6.5.0 / 6.6.0 
 * K Harrell	10/20/2010	Move update of VI to end of populating 
 * 							Transaction tables 
 * 							modify populateTransData() 
 * 							defect 10636 Ver 6.6.0 
 * K Harrell	11/10/2010	Save Inventory on Tow Truck, Non-Resident 
 * 							for later release, delete 
 * 							modify saveVehicleData() 
 * 							defect 10662 Ver 6.6.0 
 * B Brown		12/08/2010	Add IADDRE to shTransactionControl for 
 * 							EReminder defect 10610 Ver 6.7.0 
 * K Harrell	12/10/2010	Throw exception on Payment Error 
 * 							  (e.g. *10+3 error) 
 * 							modify populateTransData(Vector,
 *							  TransactionHeaderData) 
 *							defect 10348 Ver 6.7.0 
 * K Harrell	12/26/2010	add shTransactionControl entry  (DPSPPT) 
 * 							add permitNeeded(), populateSpclPltPrmt() 
 * 							modify addTransHeader(), 
 * 							 createDeleteAllTransVector(), 
 * 							 createDeleteSelectedTransVector(),
 * 							 endTrans(),formatReceipt(), saveReceipt(),
 * 							 saveVehicleData(), barcodeNeeded(),
 * 							 populateSpclPltPrmt(), saveCashReceipt() 
 * 							defect 10700 Ver 6.7.0 
 * K Harrell	01/05/2011	add assignment of VehMjrColorCd, 
 * 							 VehMnrColorCd 
 * 							modify populateMvTrans() 
 * 							defect 10712 Ver 6.7.0 
 * K Harrell	01/20/2011	add setTransTime()
 * 							defect 10674 Ver 6.7.0
 * K Harrell	01/24/2011	add shTransactionControl entry (WRENEW)
 * 							modify isSpecialPlateTransaction(),
 * 							 populateSRFuncTrans(), 
 * 							 populateSpclPltTransHstry(), 
 *							 populateFundFuncTrnsTrFdsDetail(),
 *							 populateInvFuncTransTrInvDetail(),
 *							 populateMvTrans(), populateTrans() 
 *							defect 10734 Ver 6.7.0  
 * K Harrell	02/08/2011	consider Credit Card fee in Payment comparison
 * 							to subtotal 
 * 							modify populateTransData(Vector,
 *							 TransactionHeaderData), addEndTrans(), 
 *							 endTrans(), endTransForVoid()
 * 							defect 10348 Ver 6.7.0 
 * K Harrell	02/11/2011	modify DTA code to use the standard method
 * 							 to update the ReceiptLog.
 * 							modify genDTADealerCompletedReport()  
 * 							defect 10700 Ver 6.7.0 
 * K Harrell	03/08/2011	Reorganize import to resolve merge issue
 * 							6.7.1 Merge Issue 
 * K Harrell	04/22/2011	implement SubconRenewalData.isMfgSpclPlt() 
 * 							modify populateSRFuncTrans() 
 * 							defect 10734 Ver 6.7.1
 * K Harrell	06/02/2011	add shTransactionControl entry (MODPT)
 * 							modify saveVehicleData(), populateTrans(),
 * 							  populatePrmtTrans(), populateTransData(),
 * 							 createDeleteAllTransVector(),  
 * 							 createDeleteSelectedTransVector(),
 * 							 saveVehicleData(), saveReceipt(),
 * 							 genDTADealerCompletedReport()  
 * 							defect 10844 Ver 6.8.0 
 * K Harrell	06/23/2011	modify printAllReceipts() 
 * 							defect 10844 Ver 6.8.0 
 * K Harrell	07/06/2011	modify createDeleteAllTransVector() 
 * 							defect 10844 Ver 6.8.0
 * K Harrell	10/27/2011	modify saveVehicleData()
 * 							defect 10951 Ver 6.9.0  
 * K Harrell	10/29/2011	modify populateMvTrans() 
 * 							defect 10841 Ver 6.9.0 
 * K Harrell	11/05/2011	modify populateMvTrans(), 
 * 							 populateTrans(Vector,
 * 							  CompleteTransactionData,
 *							  TransactionHeaderData)
 * 							defect 11137 Ver 6.9.0 
 * K Harrell	11/14/2011	Begin VTR 275 
 * 							modify shTransactionControl
 * 							 (add VDSC, FDSCN, VDS, VDSN)
 * 							defect 11052 Ver 6.9.0 
 * B Woodson    11/16/2011  modify printAllReceipts()
 * 							modify processReceipt()
 * 							defect 11052 Ver 6.9.0
 * K Harrell	11/22/2011	modify addTransHeader(), updateRcptLog() 
 * 							defect 11052 Ver 6.9.0 
 * K Harrell	12/07/2011	Enable Reprint Receipt for VTR275 for HQ
 * 							modify addTrans() 
 * 							defect 11168 Ver 6.9.0 
 * K Harrell	12/07/2011	Check for null MfVehicleData on WRENEW 
 * 							to handle WebAgent 1.0.0 transactions
 * 							modify populateMvTrans()
 * 							defect 11181 Ver 6.9.0 
 * K Harrell	01/10/2012	Correct assignment for Number of Special 
 * 							Plates Months to Charge
 * 							modify populateSRFuncTrans()
 * 							defect 11206 Ver 6.10.0 
 * K Harrell	01/16/2012	Add assignments of SurvShpRightsName1, 
 * 							 SurvShpRightsName2,AddlSurvivorIndi, 
 * 							 ExportIndi,SalvIndi to 
 * 							 MotorVehicleTransactionData
 * 							modify populateMvTrans()
 * 							defect 11231 Ver 6.10.0 
 * B Woodson	01/31/2012	modify populateMvTrans()
 * 							defect 11251 Ver 6.10.0 
 * K Harrell	02/05/2012	add REI_DP,REN_DP
 * 							modify shTransactionControl (add REIPDC, 
 * 								REITDC, RENPDC)
 * 							defect 11214 Ver 6.10.0 
 * R Pilon		05/04/2012	Verify cache is not corrupt prior to writing 
 * 							  to cache.
 * 							modify writeToCache(Vector)
 * 							add isCurrentCacheValid()
 * 							defect 11073 Ver 7.0.0
 * R Pilon		07/03/2012	Prevent populating svCacheQueue when only verifying 
 * 							  cache.
 * 							add sbVerifyingCache
 * 							modify cacheIO(int,Vector,RTSDate), 
 * 							  isCurrentCacheValid()
 * 							defect 11406 Ver 7.0.0
 * R Pilon		08/07/2012	Format method isCurrentCacheValid().
 * 							modify isCurrentCacheValid()
 * 							defect 11406 Ver 7.0.0
 * ---------------------------------------------------------------------
 */

/**     
 * This class handles the Transaction management on client side for 
 * common module as well as allowing server side calls to create 
 * transaction objects.
 *
 * @version POS_700			08/07/2012
 * @author	Nancy Ting
 * <br>Creation Date: 		02/04/2002 08:22:01
 */

public class Transaction
{
	//Initialize Transaction Engine
	//constants
	private static final int SUCCESS = 1;
	private static final int FAILURE = 0;

	private static final String RCPTLOG = "RCPTLOG";

	/**
	 * Constant for CUSTOMER-based Transactions
	 */
	private static final int CUSTOMER = 1;
	/**
	 * Constant for BACKOFFICE Transactions
	 */
	private static final int BACK_OFFICE = 0;
	/**
	 * Transname for BACKOFFICE Transactions
	 */
	private static final String BACK_OFFICE_TRANSNAME = "BackOffice";

	/**
	 * Transname for CUSTOMER Transactions
	 */
	/**
	 * Name for Transaction Event - REGISTRATION
	 */
	private static final String REG = "REG";
	/**
	 * Name for Transaction Event - TITLE
	 */
	private static final String TTL = "TTL";
	/**
	 * Name for Transaction Event - VEHICLE INQUIRY
	 */
	private static final String VEHINQ = "VEHINQ";
	/**
	 * Name for Transaction Event - MISCELLANEOUS REGISTRATION
	 */
	private static final String MRG = "MRG";
	/**
	 * Name for Transaction Event - ADD DISABLED PLACARD
	 */
	private static final String ADD_DP = "ADD_DP";
	/**
	 * Name for Transaction Event - DELETE DISABLED PLACARD
	 */
	private static final String DEL_DP = "DEL_DP";
	/**
	 * Name for Transaction Event - REPLACE DISABLED PLACARD
	 */
	private static final String RPL_DP = "RPL_DP";
	
	// defect 11214 
	/**
	 * Name for Transaction Event - REINSTATE DISABLED PLACARD
	 */
	private static final String REI_DP = "REI_DP";
	/**
	 * Name for Transaction Event - RENEW DISABLED PLACARD
	 */
	private static final String REN_DP = "REN_DP";
	// end defect 11214 
	
	/**
	 * Name for Transaction Event - MISCELLANEOUS 
	 */
	private static final String MISC = "MISC";
	/**
	 * Name for Transaction Event - SPECIAL OWNER
	 */
	private static final String SPCLOWNR = "SPCLOWNR";
	/**
	 * Name for Transaction Event - REJECT/RELEASE
	 */
	private static final String REJREL = "REJREL";
	/**
	 * Name for Transaction Event - CCO/CCDO
	 */
	private static final String CCOCCDO = "CCOCCDO";
	/**
	 * Name for Transaction Event - SALVAGE/COA
	 */
	private static final String SALCOASC = "SALCOASC";
	/**
	* Name for Transaction Event - ACCOUNTING
	*/
	private static final String ACC = "ACC";
	/**
	* Name for Transaction Event - VOID
	*/
	private static final String VOID = "VOID";
	/**
	 * Name for Transaction Event - SUBCONTRACTOR
	 */
	private static final String SBRNW = "SBRNW";
	/**
	 * Name for Transaction Event - DEALER TITLE
	 */
	private static final String DTA = "DTA";
	/**
	 * Name for Transaction Event - SALVAGE
	 */
	private static final String SLVG = "SLVG";
	/**
	 * Name for Transaction Event - CLOSEOUT
	 */
	private static final String CLSOUT = "CLSOUT";
	/**
	 * Name for Transaction Event - INVENTORY
	 */
	private static final String INV = "INV";
	/**
	 * Name for Transaction Event - PAYMENT
	 */
	private static final String PYMNT = "PAYMENT";

	// defect 9085
	private static final String SPCLPLT = "SPCLPLT";
	// end defect 9085

	// defect 9502
	private static final String V21TRANS = "V21TRANS";
	// end defect 9502 

	//define control table
	public static Hashtable shTransactionControl = new Hashtable();
	//initialize the control table
	// Transaction Control Structure 
	// String 	csTransCd;
	// boolean 	cbBuildPayment;
	// boolean 	cbPrintReceipt;
	// boolean 	cbBuildMVFuncTrans;
	// int 		ciCustomer;
	// String 	csEventType;
	// defect 6721 
	// protected boolean cbAutoEndTrans;
	//
	static {
		//REG
		shTransactionControl.put(
			TransCdConstant.ADDR,
			new TransactionControl(
				TransCdConstant.ADDR,
				false,
				true,
				true,
				CUSTOMER,
				REG,
				false));
		shTransactionControl.put(
			TransCdConstant.RENEW,
			new TransactionControl(
				TransCdConstant.RENEW,
				true,
				true,
				true,
				CUSTOMER,
				REG,
				false));
		shTransactionControl.put(
			TransCdConstant.RNR,
			new TransactionControl(
				TransCdConstant.RNR,
				false,
				true,
				true,
				CUSTOMER,
				REG,
				false));
		shTransactionControl.put(
			TransCdConstant.CORREG,
			new TransactionControl(
				TransCdConstant.CORREG,
				true,
				true,
				true,
				CUSTOMER,
				REG,
				false));
		shTransactionControl.put(
			TransCdConstant.EXCH,
			new TransactionControl(
				TransCdConstant.EXCH,
				true,
				true,
				true,
				CUSTOMER,
				REG,
				false));
		shTransactionControl.put(
			TransCdConstant.PAWT,
			new TransactionControl(
				TransCdConstant.PAWT,
				true,
				true,
				true,
				CUSTOMER,
				REG,
				false));
		shTransactionControl.put(
			TransCdConstant.REPL,
			new TransactionControl(
				TransCdConstant.REPL,
				true,
				true,
				true,
				CUSTOMER,
				REG,
				false));
		shTransactionControl.put(
			TransCdConstant.DUPL,
			new TransactionControl(
				TransCdConstant.DUPL,
				true,
				true,
				true,
				CUSTOMER,
				REG,
				false));
		//TTL
		shTransactionControl.put(
			TransCdConstant.TITLE,
			new TransactionControl(
				TransCdConstant.TITLE,
				true,
				true,
				true,
				CUSTOMER,
				TTL,
				false));
		shTransactionControl.put(
			TransCdConstant.REJCOR,
			new TransactionControl(
				TransCdConstant.REJCOR,
				true,
				true,
				true,
				CUSTOMER,
				TTL,
				false));
		shTransactionControl.put(
			TransCdConstant.NONTTL,
			new TransactionControl(
				TransCdConstant.NONTTL,
				true,
				true,
				true,
				CUSTOMER,
				TTL,
				false));
		shTransactionControl.put(
			TransCdConstant.CORTTL,
			new TransactionControl(
				TransCdConstant.CORTTL,
				true,
				true,
				true,
				CUSTOMER,
				TTL,
				false));

		//VEHINQ
		shTransactionControl.put(
			TransCdConstant.VEHINQ,
			new TransactionControl(
				TransCdConstant.VEHINQ,
				true,
				true,
				false,
				CUSTOMER,
				VEHINQ,
				false));
		// VDSC
		shTransactionControl.put(
			TransCdConstant.VDSC,
			new TransactionControl(
				TransCdConstant.VDSC,
				true,
				true,
				false,
				CUSTOMER,
				VEHINQ,
				false));
		// VDSCN
		shTransactionControl.put(
			TransCdConstant.VDSCN,
			new TransactionControl(
				TransCdConstant.VDSCN,
				true,
				true,
				false,
				CUSTOMER,
				VEHINQ,
				false));
		// VDS
		shTransactionControl.put(
			TransCdConstant.VDS,
			new TransactionControl(
				TransCdConstant.VDS,
				true,
				true,
				false,
				CUSTOMER,
				VEHINQ,
				false));
		// VDSN
		shTransactionControl.put(
			TransCdConstant.VDSN,
			new TransactionControl(
				TransCdConstant.VDSN,
				true,
				true,
				false,
				CUSTOMER,
				VEHINQ,
				false));	

		//MRG
		// defect 10491 
		shTransactionControl.put(
			TransCdConstant.PRMDUP,
			new TransactionControl(
				TransCdConstant.PRMDUP,
				true,
				true,
				false,
				CUSTOMER,
				MRG,
				false));
		// end defect 10491 

		// defect 10844 
		// Transaction Control Structure 
		// String 	csTransCd;
		// boolean 	cbBuildPayment;
		// boolean 	cbPrintReceipt;
		// boolean 	cbBuildMVFuncTrans;
		// int 		ciCustomer;
		// String 	csEventType;
		shTransactionControl.put(
			TransCdConstant.MODPT,
			new TransactionControl(
				TransCdConstant.MODPT,
				false,
				true,
				false,
				CUSTOMER,
				MRG,
				false));
		// end defect 10491 

		shTransactionControl.put(
			TransCdConstant.PT72,
			new TransactionControl(
				TransCdConstant.PT72,
				true,
				true,
				false,
				CUSTOMER,
				MRG,
				false));
		shTransactionControl.put(
			TransCdConstant.PT144,
			new TransactionControl(
				TransCdConstant.PT144,
				true,
				true,
				false,
				CUSTOMER,
				MRG,
				false));
		shTransactionControl.put(
			TransCdConstant.OTPT,
			new TransactionControl(
				TransCdConstant.OTPT,
				true,
				true,
				false,
				CUSTOMER,
				MRG,
				false));
		shTransactionControl.put(
			TransCdConstant.FDPT,
			new TransactionControl(
				TransCdConstant.FDPT,
				true,
				true,
				false,
				CUSTOMER,
				MRG,
				false));
		shTransactionControl.put(
			TransCdConstant.PT30,
			new TransactionControl(
				TransCdConstant.PT30,
				true,
				true,
				false,
				CUSTOMER,
				MRG,
				false));
		shTransactionControl.put(
			TransCdConstant.TOWP,
			new TransactionControl(
				TransCdConstant.TOWP,
				true,
				true,
				false,
				CUSTOMER,
				MRG,
				false));
		shTransactionControl.put(
			TransCdConstant.TAWPT,
			new TransactionControl(
				TransCdConstant.TAWPT,
				true,
				true,
				false,
				CUSTOMER,
				MRG,
				false));
		// defect 11214 
		shTransactionControl.put(
				TransCdConstant.RENPDC,
				new TransactionControl(
					TransCdConstant.RENPDC,
					true,
					true,
					false,
					CUSTOMER,
					REN_DP,
					false));

		shTransactionControl.put(
		TransCdConstant.REIPDC,
		new TransactionControl(
			TransCdConstant.REIPDC,
			true,
			true,
			false,
			CUSTOMER,
			REI_DP,
			false));
		
		shTransactionControl.put(
			TransCdConstant.REITDC,
			new TransactionControl(
				TransCdConstant.REIPDC,
				true,
				true,
				false,
				CUSTOMER,
				REI_DP,
				false));
		// end defect 11214
		
		// defect 10133 
		// ADD 
		shTransactionControl.put(
			TransCdConstant.PDC,
			new TransactionControl(
				TransCdConstant.PDC,
				true,
				true,
				false,
				CUSTOMER,
				ADD_DP,
				false));

		shTransactionControl.put(
			TransCdConstant.TDC,
			new TransactionControl(
				TransCdConstant.TDC,
				true,
				true,
				false,
				CUSTOMER,
				ADD_DP,
				false));

		shTransactionControl.put(
			TransCdConstant.DELPDC,
			new TransactionControl(
				TransCdConstant.DELPDC,
				true,
				true,
				false,
				CUSTOMER,
				DEL_DP,
				false));

		shTransactionControl.put(
			TransCdConstant.DELTDC,
			new TransactionControl(
				TransCdConstant.DELTDC,
				true,
				true,
				false,
				CUSTOMER,
				DEL_DP,
				false));

		// defect 10468 
		shTransactionControl
			.put(
				TransCdConstant.RPLPDC,
				new TransactionControl(
					TransCdConstant.RPLPDC,
					true,
					true,
					false,
					CUSTOMER,
					RPL_DP,
		//ADD_DP,
		false));

		shTransactionControl
			.put(
				TransCdConstant.RPLTDC,
				new TransactionControl(
					TransCdConstant.RPLTDC,
					true,
					true,
					false,
					CUSTOMER,
					RPL_DP,
		//ADD_DP,
		false));
		// end defect 10468 

		// defect 9831
		// ADD 
		//		shTransactionControl.put(
		//			TransCdConstant.BPM,
		//			new TransactionControl(
		//				TransCdConstant.BPM,
		//				true,
		//				true,
		//				false,
		//				CUSTOMER,
		//				ADD_DP,
		//				false));
		//		shTransactionControl.put(
		//			TransCdConstant.BTM,
		//			new TransactionControl(
		//				TransCdConstant.BTM,
		//				true,
		//				true,
		//				false,
		//				CUSTOMER,
		//				ADD_DP,
		//				false));
		//		shTransactionControl.put(
		//			TransCdConstant.RPNM,
		//			new TransactionControl(
		//				TransCdConstant.RPNM,
		//				true,
		//				true,
		//				false,
		//				CUSTOMER,
		//				ADD_DP,
		//				false));
		//		shTransactionControl.put(
		//			TransCdConstant.RTNM,
		//			new TransactionControl(
		//				TransCdConstant.RTNM,
		//				true,
		//				true,
		//				false,
		//				CUSTOMER,
		//				ADD_DP,
		//				false));
		//		// DELETE 
		//		shTransactionControl.put(
		//			TransCdConstant.DLBPM,
		//			new TransactionControl(
		//				TransCdConstant.DLBPM,
		//				true,
		//				true,
		//				false,
		//				CUSTOMER,
		//				DEL_DP,
		//				false));
		//		shTransactionControl.put(
		//			TransCdConstant.DLBTM,
		//			new TransactionControl(
		//				TransCdConstant.DLBTM,
		//				true,
		//				true,
		//				false,
		//				CUSTOMER,
		//				DEL_DP,
		//				false));
		//		shTransactionControl.put(
		//			TransCdConstant.DLRPNM,
		//			new TransactionControl(
		//				TransCdConstant.DLRPNM,
		//				true,
		//				true,
		//				false,
		//				CUSTOMER,
		//				DEL_DP,
		//				false));
		//		shTransactionControl.put(
		//			TransCdConstant.DLRTNM,
		//			new TransactionControl(
		//				TransCdConstant.DLRTNM,
		//				true,
		//				true,
		//				false,
		//				CUSTOMER,
		//				DEL_DP,
		//				false));
		//		shTransactionControl.put(
		//			TransCdConstant.RPBPM,
		//			new TransactionControl(
		//				TransCdConstant.RPBPM,
		//				true,
		//				true,
		//				false,
		//				CUSTOMER,
		//				RPL_DP,
		//				false));
		//		shTransactionControl.put(
		//			TransCdConstant.RPBTM,
		//			new TransactionControl(
		//				TransCdConstant.RPBTM,
		//				true,
		//				true,
		//				false,
		//				CUSTOMER,
		//				RPL_DP,
		//				false));
		//		shTransactionControl.put(
		//			TransCdConstant.RPRPNM,
		//			new TransactionControl(
		//				TransCdConstant.RPRPNM,
		//				true,
		//				true,
		//				false,
		//				CUSTOMER,
		//				RPL_DP,
		//				false));
		//		shTransactionControl.put(
		//			TransCdConstant.RPRTNM,
		//			new TransactionControl(
		//				TransCdConstant.RPRTNM,
		//				true,
		//				true,
		//				false,
		//				CUSTOMER,
		//				RPL_DP,
		//				false));
		// end defect 9831 
		// end defect 10133 

		shTransactionControl.put(
			TransCdConstant.NRIPT,
			new TransactionControl(
				TransCdConstant.NRIPT,
				true,
				true,
				false,
				CUSTOMER,
				MRG,
				false));
		shTransactionControl.put(
			TransCdConstant.NROPT,
			new TransactionControl(
				TransCdConstant.NROPT,
				true,
				true,
				false,
				CUSTOMER,
				MRG,
				false));
		//SPLOWNR
		shTransactionControl.put(
			TransCdConstant.SPECDL,
			new TransactionControl(
				TransCdConstant.SPECDL,
				true,
				true,
				true,
				CUSTOMER,
				SPCLOWNR,
				false));
		shTransactionControl.put(
			TransCdConstant.ADDRSP,
			new TransactionControl(
				TransCdConstant.ADDRSP,
				false,
				false,
				true,
				CUSTOMER,
				SPCLOWNR,
				false));
		// initialize the control table
		// Transaction Control Structure 
		// String 	csTransCd;
		// boolean 	cbBuildPayment;
		// boolean 	cbPrintReceipt;
		// boolean 	cbBuildMVFuncTrans;
		// int 		ciCustomer;
		// String 	csEventType;
		// protected boolean cbAutoEndTrans;

		// defect 9085 
		// Internet Special Plate Application	
		shTransactionControl.put(
			TransCdConstant.IAPPL,
			new TransactionControl(
				TransCdConstant.IAPPL,
				false,
				false,
				true,
				CUSTOMER,
				SPCLPLT,
				true));

		// SPCL PLATES 	
		// Special Plate Application - Manufacture 	
		shTransactionControl.put(
			TransCdConstant.SPAPPL,
			new TransactionControl(
				TransCdConstant.SPAPPL,
				true,
				true,
				true,
				CUSTOMER,
				SPCLPLT,
				false));
		// Special Plate Application - Customer Supplied 	
		shTransactionControl.put(
			TransCdConstant.SPAPPC,
			new TransactionControl(
				TransCdConstant.SPAPPC,
				true,
				true,
				true,
				CUSTOMER,
				SPCLPLT,
				false));
		// Special Plate Application - Inventory Issue 	
		shTransactionControl.put(
			TransCdConstant.SPAPPI,
			new TransactionControl(
				TransCdConstant.SPAPPI,
				true,
				true,
				true,
				CUSTOMER,
				SPCLPLT,
				false));
		// Special Plate Application - Owner Change 	
		shTransactionControl.put(
			TransCdConstant.SPAPPO,
			new TransactionControl(
				TransCdConstant.SPAPPO,
				true,
				true,
				true,
				CUSTOMER,
				SPCLPLT,
				false));
		// Special Plate Application - Reserve 	
		shTransactionControl.put(
			TransCdConstant.SPAPPR,
			new TransactionControl(
				TransCdConstant.SPAPPR,
				true,
				true,
				true,
				CUSTOMER,
				SPCLPLT,
				false));

		// Special Plate Request Renewal Notice		
		shTransactionControl.put(
			TransCdConstant.SPRNR,
			new TransactionControl(
				TransCdConstant.SPRNR,
				false,
				true,
				true,
				CUSTOMER,
				SPCLPLT,
				false));

		// Special Plate Renew Only	
		shTransactionControl.put(
			TransCdConstant.SPRNW,
			new TransactionControl(
				TransCdConstant.SPRNW,
				true,
				true,
				true,
				CUSTOMER,
				SPCLPLT,
				false));

		// Special Plate Revise	
		shTransactionControl.put(
			TransCdConstant.SPREV,
			new TransactionControl(
				TransCdConstant.SPREV,
				false,
				true,
				true,
				CUSTOMER,
				SPCLPLT,
				false));

		// defect 9237
		// Create receipts for SPRSRV, SPUNAC, SPDEL
		// 3rd parameter  
		// Special Plate Reserve	
		shTransactionControl.put(
			TransCdConstant.SPRSRV,
			new TransactionControl(
				TransCdConstant.SPRSRV,
				false,
				true,
				true,
				BACK_OFFICE,
				SPCLPLT,
				true));

		// Special Plate Unacceptable	
		shTransactionControl.put(
			TransCdConstant.SPUNAC,
			new TransactionControl(
				TransCdConstant.SPUNAC,
				false,
				true,
				true,
				BACK_OFFICE,
				SPCLPLT,
				true));

		// Special Plate Delete	
		shTransactionControl.put(
			TransCdConstant.SPDEL,
			new TransactionControl(
				TransCdConstant.SPDEL,
				false,
				true,
				true,
				BACK_OFFICE,
				SPCLPLT,
				true));
		// end defect 9085
		// end defect 9237  

		// defect 8900 
		// AutoEnd true  (last entry)
		shTransactionControl.put(
			TransCdConstant.DRVED,
			new TransactionControl(
				TransCdConstant.DRVED,
				false,
				true,
				true,
				CUSTOMER,
				SPCLOWNR,
				true));
		// end defect 8900

		// REJREL Reject Release
		shTransactionControl.put(
			TransCdConstant.ADLSTX,
			new TransactionControl(
				TransCdConstant.ADLSTX,
				true,
				true,
				true,
				CUSTOMER,
				REJREL,
				false));
		shTransactionControl.put(
			TransCdConstant.DELTIP,
			new TransactionControl(
				TransCdConstant.DELTIP,
				false,
				false,
				true,
				BACK_OFFICE,
				REJREL,
				false));

		// CCOCCDO Certified Copy Original/
		//	Certified Copy Duplicate Original
		shTransactionControl.put(
			TransCdConstant.CCO,
			new TransactionControl(
				TransCdConstant.CCO,
				false,
				false,
				true,
				CUSTOMER,
				CCOCCDO,
				false));
		shTransactionControl.put(
			TransCdConstant.CCDO,
			new TransactionControl(
				TransCdConstant.CCDO,
				false,
				false,
				false,
				CUSTOMER,
				CCOCCDO,
				false));
		// SALCOASC ????????????

		shTransactionControl.put(
			TransCdConstant.STAT,
			new TransactionControl(
				TransCdConstant.STAT,
				false,
				false,
				true,
				BACK_OFFICE,
				SALCOASC,
				false));
		shTransactionControl.put(
			TransCdConstant.STATJK,
			new TransactionControl(
				TransCdConstant.STATJK,
				false,
				false,
				true,
				CUSTOMER,
				SALCOASC,
				false));
		shTransactionControl.put(
			TransCdConstant.STATRF,
			new TransactionControl(
				TransCdConstant.STATRF,
				false,
				false,
				true,
				BACK_OFFICE,
				SALCOASC,
				false));
		shTransactionControl.put(
			TransCdConstant.REGIVD,
			new TransactionControl(
				TransCdConstant.REGIVD,
				false,
				true,
				true,
				CUSTOMER,
				SALCOASC,
				false));
		//ACC
		shTransactionControl.put(
			TransCdConstant.CKREDM,
			new TransactionControl(
				TransCdConstant.CKREDM,
				true,
				true,
				true,
				CUSTOMER,
				ACC,
				false));
		// defect 7102 
		// defect 8354 
		// No pymnt req'd for REFUND,HOTCK,HOTDED,HCKITM 
		// Changed 2nd parameter to false   
		shTransactionControl.put(
			TransCdConstant.REFUND,
			new TransactionControl(
				TransCdConstant.REFUND,
				false,
				true,
				true,
				CUSTOMER,
				ACC,
				true));
		shTransactionControl.put(
			TransCdConstant.HOTCK,
			new TransactionControl(
				TransCdConstant.HOTCK,
				false,
				true,
				true,
				CUSTOMER,
				ACC,
				true));
		shTransactionControl.put(
			TransCdConstant.HOTDED,
			new TransactionControl(
				TransCdConstant.HOTDED,
				false,
				true,
				false,
				CUSTOMER,
				ACC,
				true));
		// defect 8354 
		shTransactionControl.put(
			TransCdConstant.HCKITM,
			new TransactionControl(
				TransCdConstant.HCKITM,
				false,
				false,
				true,
				BACK_OFFICE,
				ACC,
				true));
		// end defect 8354 		
		shTransactionControl.put(
			TransCdConstant.RFCASH,
			new TransactionControl(
				TransCdConstant.RFCASH,
				true,
				true,
				true,
				CUSTOMER,
				ACC,
				true));
		// end defect 7102 
		shTransactionControl.put(
			TransCdConstant.ADLCOL,
			new TransactionControl(
				TransCdConstant.ADLCOL,
				true,
				true,
				false,
				CUSTOMER,
				ACC,
				false));
		shTransactionControl.put(
			TransCdConstant.FNDREM,
			new TransactionControl(
				TransCdConstant.FNDREM,
				false,
				false,
				false,
				BACK_OFFICE,
				ACC,
				false));
		shTransactionControl.put(
			TransCdConstant.EFTFND,
			new TransactionControl(
				TransCdConstant.EFTFND,
				false,
				false,
				false,
				BACK_OFFICE,
				ACC,
				false));
		shTransactionControl.put(
			TransCdConstant.VOIDFD,
			new TransactionControl(
				TransCdConstant.VOIDFD,
				false,
				false,
				false,
				BACK_OFFICE,
				ACC,
				false));
		shTransactionControl.put(
			TransCdConstant.VOID,
			new TransactionControl(
				TransCdConstant.VOID,
				true,
				false,
				false,
				CUSTOMER,
				VOID,
				false));
		shTransactionControl.put(
			TransCdConstant.VOIDNC,
			new TransactionControl(
				TransCdConstant.VOIDNC,
				false,
				false,
				false,
				CUSTOMER,
				VOID,
				false));
		shTransactionControl.put(
			TransCdConstant.INVVD,
			new TransactionControl(
				TransCdConstant.INVVD,
				false,
				false,
				true,
				CUSTOMER,
				VOID,
				false));
		//SBRNW Subcontractor Renewal
		shTransactionControl.put(
			TransCdConstant.SBRNW,
			new TransactionControl(
				TransCdConstant.SBRNW,
				true,
				false,
				true,
				CUSTOMER,
				SBRNW,
				false));

		// defect 10734 
		// WRENEW
		shTransactionControl.put(
			TransCdConstant.WRENEW,
			new TransactionControl(
				TransCdConstant.WRENEW,
				true,
				false,
				true,
				CUSTOMER,
				REG,
				false));
		// end defect 10734 

		//DTA Dealer Title Application
		shTransactionControl.put(
			TransCdConstant.DTAORD,
			new TransactionControl(
				TransCdConstant.DTAORD,
				true,
				true,
				true,
				CUSTOMER,
				DTA,
				false));
		shTransactionControl.put(
			TransCdConstant.DTANTD,
			new TransactionControl(
				TransCdConstant.DTANTD,
				true,
				true,
				true,
				CUSTOMER,
				DTA,
				false));
		shTransactionControl.put(
			TransCdConstant.DTANTK,
			new TransactionControl(
				TransCdConstant.DTANTK,
				true,
				true,
				true,
				CUSTOMER,
				DTA,
				false));
		shTransactionControl.put(
			TransCdConstant.DTAORK,
			new TransactionControl(
				TransCdConstant.DTAORK,
				true,
				true,
				true,
				CUSTOMER,
				DTA,
				false));

		//SLVG Salvage
		// defect defect 9636 7102 
		//		shTransactionControl.put(
		//			TransCdConstant.SLVG,
		//			new TransactionControl(
		//				TransCdConstant.SLVG,
		//				false,
		//				false,
		//				true,
		//				CUSTOMER,
		//				SLVG,
		//				true));
		//		shTransactionControl.put(
		//			TransCdConstant.DPLSLG,
		//			new TransactionControl(
		//				TransCdConstant.DPLSLG,
		//				false,
		//				false,
		//				true,
		//				CUSTOMER,
		//				SLVG,
		//				true));

		shTransactionControl.put(
			TransCdConstant.SCOT,
			new TransactionControl(
				TransCdConstant.SCOT,
				false,
				false,
				true,
				CUSTOMER,
				SLVG,
				true));
		shTransactionControl.put(
			TransCdConstant.NRCOT,
			new TransactionControl(
				TransCdConstant.NRCOT,
				false,
				false,
				true,
				CUSTOMER,
				SLVG,
				true));

		shTransactionControl.put(
			TransCdConstant.CORSLV,
			new TransactionControl(
				TransCdConstant.CORSLV,
				false,
				false,
				true,
				CUSTOMER,
				SLVG,
				true));

		shTransactionControl.put(
			TransCdConstant.CORNRT,
			new TransactionControl(
				TransCdConstant.CORNRT,
				false,
				false,
				true,
				CUSTOMER,
				SLVG,
				true));
		// end defect 9636
		// defect 9642 
		shTransactionControl.put(
			TransCdConstant.COA,
			new TransactionControl(
				TransCdConstant.COA,
				false,
				false,
				true,
				CUSTOMER,
				SLVG,
				true));
		// end defect 9642 

		shTransactionControl.put(
			TransCdConstant.CCOSCT,
			new TransactionControl(
				TransCdConstant.CCOSCT,
				false,
				false,
				true,
				CUSTOMER,
				SLVG,
				false));
		shTransactionControl.put(
			TransCdConstant.CCONRT,
			new TransactionControl(
				TransCdConstant.CCONRT,
				false,
				false,
				true,
				CUSTOMER,
				SLVG,
				false));

		//CLSOUT
		shTransactionControl.put(
			TransCdConstant.CLSOUT,
			new TransactionControl(
				TransCdConstant.CLSOUT,
				false,
				false,
				false,
				BACK_OFFICE,
				CLSOUT,
				false));
		//INV
		shTransactionControl.put(
			TransCdConstant.INVOFC,
			new TransactionControl(
				TransCdConstant.INVOFC,
				false,
				false,
				false,
				BACK_OFFICE,
				INV,
				false));
		shTransactionControl.put(
			TransCdConstant.INVALL,
			new TransactionControl(
				TransCdConstant.INVALL,
				false,
				false,
				false,
				BACK_OFFICE,
				INV,
				false));
		shTransactionControl.put(
			TransCdConstant.INVDEL,
			new TransactionControl(
				TransCdConstant.INVDEL,
				false,
				false,
				false,
				BACK_OFFICE,
				INV,
				false));
		shTransactionControl.put(
			TransCdConstant.INVRCV,
			new TransactionControl(
				TransCdConstant.INVRCV,
				false,
				false,
				false,
				BACK_OFFICE,
				INV,
				false));
		//INTERNET
		shTransactionControl.put(
			TransCdConstant.IADDR,
			new TransactionControl(
				TransCdConstant.IADDR,
				false,
				false,
				true,
				CUSTOMER,
				REG,
				false));
		shTransactionControl.put(
			TransCdConstant.IRENEW,
			new TransactionControl(
				TransCdConstant.IRENEW,
				false,
				true,
				true,
				CUSTOMER,
				REG,
				true));
		shTransactionControl.put(
			TransCdConstant.IRNR,
			new TransactionControl(
				TransCdConstant.IRNR,
				false,
				false,
				true,
				CUSTOMER,
				REG,
				false));
		//Regional Collections
		shTransactionControl.put(
			TransCdConstant.RGNCOL,
			new TransactionControl(
				TransCdConstant.RGNCOL,
				true,
				true,
				false,
				CUSTOMER,
				ACC,
				false));
		//Credit Card Payment
		shTransactionControl.put(
			TransCdConstant.CCPYMNT,
			new TransactionControl(
				TransCdConstant.CCPYMNT,
				false,
				false,
				false,
				CUSTOMER,
				PYMNT,
				false));
		// RPRSTK       
		shTransactionControl.put(
			TransCdConstant.RPRSTK,
			new TransactionControl(
				TransCdConstant.RPRSTK,
				false,
				false,
				false,
				BACK_OFFICE,
				MISC,
				true));

		// defect 10700 
		// DPSPPT       
		shTransactionControl.put(
			TransCdConstant.DPSPPT,
			new TransactionControl(
				TransCdConstant.DPSPPT,
				false,
				true,
				false,
				CUSTOMER,
				SPCLPLT,
				false));
		// end defect 10700 

		// defect 10491 
		// RPRPRM       
		shTransactionControl.put(
			TransCdConstant.RPRPRM,
			new TransactionControl(
				TransCdConstant.RPRPRM,
				false,
				false,
				false,
				BACK_OFFICE,
				MISC,
				true));
		// end defect 10491 

		// defect 9502
		// V21TRANS       
		shTransactionControl.put(
			TransCdConstant.V21VTN,
			new TransactionControl(
				TransCdConstant.V21VTN,
				false,
				false,
				true,
				BACK_OFFICE,
				V21TRANS,
				true));
		shTransactionControl.put(
			TransCdConstant.V21PLD,
			new TransactionControl(
				TransCdConstant.V21PLD,
				false,
				false,
				true,
				BACK_OFFICE,
				V21TRANS,
				true));
		shTransactionControl.put(
			TransCdConstant.G36VTN,
			new TransactionControl(
				TransCdConstant.G36VTN,
				false,
				false,
				true,
				BACK_OFFICE,
				V21TRANS,
				true));
		// end defect 9502

		// defect 9711
		shTransactionControl.put(
			TransCdConstant.VPAPPL,
			new TransactionControl(
				TransCdConstant.VPAPPL,
				false,
				false,
				true,
				CUSTOMER,
				SPCLPLT,
				true));
		shTransactionControl.put(
			TransCdConstant.VPAPPR,
			new TransactionControl(
				TransCdConstant.VPAPPR,
				false,
				false,
				true,
				CUSTOMER,
				SPCLPLT,
				true));
		// end defect 9711
		// defect 10401
		shTransactionControl.put(
			TransCdConstant.VPDEL,
			new TransactionControl(
				TransCdConstant.VPDEL,
				false,
				false,
				true,
				CUSTOMER,
				SPCLPLT,
				true));
		shTransactionControl.put(
			TransCdConstant.VPPORT,
			new TransactionControl(
				TransCdConstant.VPPORT,
				false,
				false,
				true,
				CUSTOMER,
				SPCLPLT,
				true));
		shTransactionControl.put(
			TransCdConstant.VPREDO,
			new TransactionControl(
				TransCdConstant.VPREDO,
				false,
				false,
				true,
				CUSTOMER,
				SPCLPLT,
				true));
		shTransactionControl.put(
			TransCdConstant.VPREV,
			new TransactionControl(
				TransCdConstant.VPREV,
				false,
				false,
				true,
				CUSTOMER,
				SPCLPLT,
				true));
		shTransactionControl.put(
			TransCdConstant.VPRSRV,
			new TransactionControl(
				TransCdConstant.VPRSRV,
				false,
				false,
				true,
				CUSTOMER,
				SPCLPLT,
				true));
		shTransactionControl.put(
			TransCdConstant.VPRSTL,
			new TransactionControl(
				TransCdConstant.VPRSTL,
				false,
				false,
				true,
				CUSTOMER,
				SPCLPLT,
				true));
		shTransactionControl.put(
			TransCdConstant.VPUNAC,
			new TransactionControl(
				TransCdConstant.VPUNAC,
				false,
				false,
				true,
				CUSTOMER,
				SPCLPLT,
				true));
		// end defect 10401
		// defect 10610
		shTransactionControl.put(
			TransCdConstant.IADDRE,
			new TransactionControl(
				TransCdConstant.IADDRE,
				false,
				false,
				true,
				CUSTOMER,
				REG,
				false));
		// end defect 10610
	}
	// end defect 6721 

	// Static Variables

	// Static boolean 
	//Indicator for whether transaction can be cancelled	
	private static boolean sbAllowCancelTrans = true;
	//if current transaction is NOT backoffice
	private static boolean sbCustomer;
	private static boolean sbHeaderAdded;
	//Print Cash Register Receipt Indicator
	private static boolean sbPrintCashReceipt = false;
	//Multiple Transaction Indicator for Receipts
	private static boolean sbMultipleTrans = false;
	//indicator for setAside
	private static boolean sbRestoredSetAside = false;
	private static boolean sbAutoEndTrans = false;
	private static boolean sbSavedSpclPlate = false;

	// Static int 
	private static int siCumulativeTransIndi;
	private static int siStoredPrintIndi;

	// Static Object 
	private static TransactionHeaderData saTransHdrData = null;
	// defect 6456
	// On Exception, reset saRunningSubtotal to saPriorRunningSubtotal 
	private static Dollar saPriorRunningSubtotal = new Dollar("0.00");
	// end defect 6456 
	private static Dollar saRunningSubtotal = new Dollar("0.00");
	private static Dollar saCreditRemaining;

	// Object
	private static CompleteTransactionData saCompTransData = null;
	private CommonClientBusiness caCommonClientBusiness = null;

	// Static String 
	private static String ssCustName = null;
	private static String ssRcptDirForCust = null;
	private static String ssRcptLogFile = null;
	private static String ssSavedAuthCd = null;
	private static String ssSavedTransCd = null;

	// defect 10290
	private static Vector svDTADlrTtlData;
	private static Vector svDTAOrigDlrTtlData;
	private static int siDTADlrTtlDataIndex;

	private static int siDTADealerId;
	private static DealerData saDTADealerData;
	// end defect 10290 

	// String 
	private String csRcptFileName = null;
	private String csTransId = null;

	// Static Vector
	private static Vector svCacheQueue = new Vector();
	private static Vector svTransIdList = new Vector();

	// Constants
	// Constants - String  
	private final static String COMPLETE_TRANS_FILE_NAME =
		"COMPTRAN.DAT";
	private static final String UNSPECIFIED_SUBCONTRACTOR =
		"Unspecified Subcontractor";
	private static final String UNSPECIFIED_DEALER =
		"Unspecified Dealer";
	private static final String ACCOUNTING = "Accounting";
	private static final String ADDITIONAL_SALES_TAX =
		"Additional Sales Tax";
	private static final String NO_CUSTOMER_NAME = "";

	private static final String CUMULATIVE_TRANS_AUTH_CD =
		"CUMULATIVETRANS";
	// defect 7436
	private final static String TITLE = "TITLE";
	private final static String CSN = "CSN";
	// defect 7430
	// Use as temp TransCd if DTA-Print Sticker
	private final static String DLRSTKR = "DLRSTKR";
	private final static String SALV = "SALV";
	private final static String NRVT = "NRVT";
	private final static String CERTIFICATE = "CERTIFICATE";
	private final static String RECEIPT = "RECEIPT";
	//	private final static String CRT_EXTENSION = ".CRT";
	//	private final static String LINE_SEPARATOR = "line.separator";
	private final static String BACK_SLASH = "\\";
	private final static String LAST_CSN_FNAME = "lastcsn.dat";
	// end defect 7430
	// end defect 7436

	// file name for set aside
	private final static String SAVED_MF_VEHICLE = "MFVEH.DAT";
	private final static String SAVED_TIME_PERMIT = "TIMEPER.DAT";
	private final static String SAVED_MISC_VEHICLE = "MISVEH.DAT";
	private final static String SAVED_INVENTORY = "INV.DAT";
	// defect 9085 
	private final static String SAVED_SPCL_PLT = "SPCLPLT.DAT";
	private final static String SAVED_MFG_SPCL_PLT_VECTOR =
		"SPMFGVCT.DAT";
	// end defect 9085  
	// defect 7717 
	private final static String SAVED_TRANS_HDR = "TRANSHDR.DAT";
	// end defect 7717 

	// defect 10617
	private final static String SAVED_PRMT_VCT = "PRMTVCT.DAT";
	// end defect 10617  

	// Constants - int
	private final static int INVVD_DEL_INV_REASON_CD = 5;
	private final static int INVVD_INV_QTY = 1;
	private final static int INVVD_DETAIL_STATUS_CD = -1;
	// defect 10246 
	// private final static int PRIVACY_OPT_CD = 3;
	// end defect 10246 
	private final static int SPECIAL_REG_SECTION_OFISSUANCE_NO = 291;
	// defect 8900
	//private final static int EXEMPT_REG_CLASS_CD = 39;
	// end 8900

	private final static int OFFICE_HEAD_QUARTERS = 1;
	private final static int OFFICE_REGIONAL_OFFICE = 2;
	private final static int OFFICE_COUNTY = 3;

	private final static int READ = 0;
	private final static int WRITE = 1;

	// int
	// defect 7586
	private int ciCacheTransTime;
	private int ciSavedTransTime;
	// end defect 7586
	private int ciTransTime;

	// Object
	private DealerTitleData caCurrDlrTtlData;
	private RTSDate caRTSDateTrans;
	private static final Dollar ZERO_DOLLAR = new Dollar("0.00");

	// String 
	private TransactionControl caTransactionControl = null;

	// Vector 
	private Vector cvTransaction = new Vector();

	// defect 11406
	private static boolean sbVerifyingCache = false;
	// end defect 11406
	
	/**
	 * Transaction constructor
	 */

	public Transaction() throws RTSException
	{
		super();
		//Set up the trans time
		caRTSDateTrans = new RTSDate();
		ciTransTime = caRTSDateTrans.get24HrTime();
		ciSavedTransTime = 0;
		ciCacheTransTime = 0;
	}
	/**
	 * Transaction constructor
	 *
	 * @param  asTransCd String 
	 * @throws RTSException
	 */
	public Transaction(String asTransCd) throws RTSException
	{
		this();
		if (!shTransactionControl.containsKey(asTransCd))
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Unable to locate Control Entry for " + asTransCd,
				"S608 Control Logic Error");
		}
		caTransactionControl =
			(TransactionControl) shTransactionControl.get(asTransCd);

	}

	/**
	 * End the Transaction by deleting inventory and setting the 
	 * transtimestamp in the TransactionHeader table.  
	 * Called from addTrans if (AutoEndTrans & HeaderAdded) || BackOffice
	 *
	 * @param  aaCompTransData CompleteTransactionData
	 * @param  aaTransHdrData TransactionHeaderData
	 * @throws RTSException
	 */
	private void addEndTrans(
		CompleteTransactionData aaCompTransData,
		TransactionHeaderData aaTransHdrData)
		throws RTSException
	{

		Vector lvTransactionCache = new Vector();
		Vector lvTempVector = new Vector();
		Vector lvTransPayment = new Vector();
		lvTempVector.add(aaCompTransData);
		// defect 8083 
		//	if (cTransactionControl.getEventType().equals(VOID)
		//	&& !aTransactionHeaderData.getTransName().equals(BACK_OFFICE_TRANSNAME))
		if (caTransactionControl.isBuildPayment() && sbCustomer)
		{
			// get vector of Payment details 
			lvTransPayment =
				getPaymentVector(lvTempVector, aaTransHdrData);
		}
		// end defect 8083 
		// PopulateTransData will return Vector of TransactionCacheData
		// which contains update to TransactionHeader
		lvTransactionCache =
			populateTransData(
				lvTransPayment,
				aaTransHdrData,
				aaCompTransData.getCreditCardFeeData());
		;
		//PROC Delete Inventory on Hold for this customer
		deleteIssuedInventory(lvTransactionCache);
		// defect 9085
		RTSDate laCacheRTSDate = new RTSDate();
		laCacheRTSDate.setTime(ciTransTime);

		updateSpclPltVIRequest(
			lvTransactionCache,
			TransactionCacheData.UPDATE,
			laCacheRTSDate);

		// end defect 9085

		// defect 10491
		updatePrmtVIRequest(
			lvTransactionCache,
			TransactionCacheData.UPDATE,
			laCacheRTSDate);
		// end defect 10491  

		for (int i = 0; i < lvTransactionCache.size(); i++)
		{
			cvTransaction.add(lvTransactionCache.elementAt(i));
		}
	}

	/**
	 * This method: 
	 * <ul>
	 * <li> Creates File for Receipt Preview
	 * <li> Returns name of Preview Receipt 
	 * </ul> 
	 * @param  aaCTData CompleteTransactionData
	 * @return String 
	 * @throws RTSException
	 */
	public String genPreviewRcpt(CompleteTransactionData aaCTData)
		throws RTSException
	{
		createTransactionControl(aaCTData.getTransCode());
		Vector lvVector = new Vector();
		CompleteTransactionData laCTData =
			(CompleteTransactionData) UtilityMethods.copy(aaCTData);

		laCTData.setPreviewReceipt(true);

		if (UtilityMethods
			.printsPermit(caTransactionControl.getTransCd()))
		{
			laCTData.assignAllocInvItmsForTimedPermit();
		}
		else if (
			UtilityMethods.isDsabldPlcrdEvent(
				caTransactionControl.getTransCd()))
		{
			if (!caTransactionControl
				.getEventType()
				.equals(TransCdConstant.DEL_DP_EVENT_TYPE))
			{
				Vector lvVector1 =
					laCTData
						.getTimedPermitData()
						.getDPCustData()
						.getDsabldPlcrd();
				int liInvIndex = 0;
				for (int i = 0; i < lvVector1.size(); i++)
				{
					DisabledPlacardData laDPData =
						(DisabledPlacardData) lvVector1.elementAt(i);

					if (laDPData != null && laDPData.getTransTypeCd()
						== MiscellaneousRegConstant.DP_ADD_TRANS_TYPE_CD)
					{
						InventoryAllocationData laInvData =
							(InventoryAllocationData) laCTData
								.getAllocInvItms()
								.elementAt(
								liInvIndex);
						liInvIndex = liInvIndex + 1;

						laDPData.setInvItmNo(laInvData.getInvItmNo());
					}
				}
			}

		}
		lvVector.add(laCTData);
		String lsRcptContent = formatReceipt(lvVector);
		String lsRcptFileName = ReportConstant.PREVIEW_RCPT_NAME;
		File laFile = new File(lsRcptFileName);
		try
		{
			laFile.delete();
			laFile.createNewFile();
		}
		catch (IOException aeIOExp)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOExp);
		}
		try
		{
			FileOutputStream laFileOutStream =
				new FileOutputStream(lsRcptFileName, true);
			OutputStreamWriter laOutputStreamWtr =
				new OutputStreamWriter(laFileOutStream);
			BufferedWriter laBuffWtr =
				new BufferedWriter(laOutputStreamWtr);
			laBuffWtr.write(lsRcptContent);
			laBuffWtr.newLine();
			laBuffWtr.flush();
			laFileOutStream.close();
		}
		catch (FileNotFoundException aeFNFEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeFNFEx);
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}
		return lsRcptFileName;
	}

	/**
	 * This method: 
	 * <ul>
	 * <li> Builds transaction objects
	 * <li> Writes to Cache and Sends To Server
	 * <li> Updates Receipt and Cumulative Receipt Log
	 * <li> Prints receipts if transactions are automatically completed.
	 * <li> Saves Vehicle Information for "more trans" 
	 * <li> Saves Inventory Information 
	 * </ul> 
	 * @param  aaCompTransData CompleteTransactionData
	 * @param  avAddlInfo      Vector
	 * @return CompleteTransactionData 
	 * @throws RTSException
	 *
	 */
	public CompleteTransactionData addTrans(
		CompleteTransactionData aaCompTransData,
		Vector avAddlInfo)
		throws RTSException
	{
		saCompTransData = aaCompTransData;
		String lsTransCd = saCompTransData.getTransCode();
		sbAutoEndTrans = false;
		csRcptFileName = null;
		csTransId = "";

		caCommonClientBusiness = new CommonClientBusiness();

		createTransactionControl(lsTransCd);

		// defect 6456
		// Multiple try blocks
		// Take action according to:
		//  - Type of transaction
		//  - Type of problem  
		try
		{
			Log.write(Log.METHOD, saCompTransData, " Begin addTrans");

			if (avAddlInfo != null)
			{
				cvTransaction.addAll(avAddlInfo);
			}

			// Transaction Data Populated, added to Transaction Vector 
			setUpTransaction();

			// 
			if ((caTransactionControl.isAutoEndTrans()
				&& sbHeaderAdded)
				|| !sbCustomer)
			{
				caCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_INVENTORY,
					aaCompTransData.getAllocInvItms());

				// defect 9386 
				// Update Virtual Inventory for SPAPPI 

				// defect 9085 
				// Save for Subsequent update as Complete
				boolean lbVIInventory =
					lsTransCd.equals(TransCdConstant.SPAPPL)
						|| lsTransCd.equals(TransCdConstant.SPAPPC)
						|| lsTransCd.equals(TransCdConstant.SPAPPI)
						|| lsTransCd.equals(TransCdConstant.SPUNAC)
						|| lsTransCd.equals(TransCdConstant.SPRSRV);
				// end defect 9386 

				if (lbVIInventory
					|| isMfgNewSpclPltOnReplace(
						saCompTransData,
						lsTransCd))
				{
					// Save Virtual Inventory Requests for Subsequent
					// Complete or Release
					SpecialPlatesRegisData laSpclPltRegisData =
						aaCompTransData
							.getVehicleInfo()
							.getSpclPltRegisData();

					if (laSpclPltRegisData.getVIAllocData() != null)
					{
						caCommonClientBusiness.processData(
							GeneralConstant.COMMON,
							CommonConstant.SAVE_VI_SPCL_PLT_VECTOR,
							laSpclPltRegisData);
					}
				}
				// end defect 9085

				// defect 10491 
				if (UtilityMethods.isPermitApplication(lsTransCd))
				{
					PermitData laPrmtData =
						(PermitData) aaCompTransData
							.getTimedPermitData();

					if (laPrmtData.getVIAllocData() != null)
					{
						caCommonClientBusiness.processData(
							GeneralConstant.COMMON,
							CommonConstant.SAVE_VI_PRMT_VECTOR,
							laPrmtData);

					}
				}
				// end defect 10491 
				addEndTrans(aaCompTransData, saTransHdrData);
				sbAutoEndTrans = true;
			}
			processCacheVector(cvTransaction);
		}
		catch (RTSException aeRTSEx)
		{
			// If autoEndTrans or first of trans set
			//  - Issue Transaction.reset() 
			if (sbAutoEndTrans || sbHeaderAdded)
			{
				reset();
			}
			// If nth transaction
			//  - Restore saRunningSubtotal
			//  - "Clear" CompleteTransactionData Object  
			else
			{
				saRunningSubtotal = saPriorRunningSubtotal;
				saCompTransData = null;
			}

			RTSException leRTSEx =
				new RTSException(RTSException.TR_ERROR, aeRTSEx);
			leRTSEx.displayError();
			return saCompTransData;
		}
		try
		{
			// Post Transaction add processing 
			postTransaction();

			// Update the Receipt Log for ADDR, RNR 
			updateRcptLogAddrRnr();

			// Receipt Handling 
			processReceipt();
			saveVehicleData();

			if (sbAutoEndTrans)
			{
				if (csRcptFileName != null)
				{
					Print laPrint = new Print();
					laPrint.sendToPrinter(csRcptFileName, lsTransCd);
					updateCumRcptLog(false);
				}
				// defect 11168 
				else if (UtilityMethods.isVTR275TransCd(caTransactionControl.getTransCd()))
				{
					updateCumRcptLog(false);
				}
				// end defect 11168 
				
				setLastCSN();
				reset();
			}
		}
		catch (RTSException aeRTSEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.TR_ERROR, aeRTSEx);
			leRTSEx.displayError();

			// If autoEndTrans or first of trans set
			//  - Issue Transaction.reset() 	
			if (sbAutoEndTrans)
			{
				try
				{
					reset();
				}
				catch (RTSException aeRTSEx2)
				{
				}
			}
		}
		finally
		{
			Log.write(Log.METHOD, aaCompTransData, " end addTrans");
			return saCompTransData;
		}
		// end defect 6456
	}
	/**
	 * This method takes CompleteTransactionData object as arguments
	 * and converts it into RTS transaction table objects formats and sends
	 * that back  
	 * It also calls method to store and generate receipts and reports.  
	 * 
	 * <P>Calls:<br>
	 * setUpFirstTransactionOfDay()
	 * populateTransactionData(...)
	 * 
	 * @param  aaCompTransData CompleteTransactionData
	 * @return Vector Contains the transaction objects and the trans id. 
	 * @throws RTSException
	 */
	public Vector addTransForVoid(CompleteTransactionData aaCompTransData)
		throws RTSException
	{
		try
		{
			//Determine the TransCd logic to use
			String lsTransCd = aaCompTransData.getTransCode();
			createTransactionControl(lsTransCd);

			// First Transaction of Void Transactions   
			if (saTransHdrData == null)
			{
				setUpFirstTransactionOfDay();
				saTransHdrData = addTransHeader(aaCompTransData, false);
				createReceiptDirectory(false);
				cvTransaction.addAll(
					populateTransData(aaCompTransData, saTransHdrData));
			}
			// Subsequent Void Transactions   
			else
			{
				cvTransaction.addAll(
					populateTransData(aaCompTransData, null));
			}
			// defect 10491 
			updateSpclPltVIForVoid(aaCompTransData, cvTransaction);
			updatePrmtVIForVoid(aaCompTransData, cvTransaction);
			// end defect 10491 

			//Create TransId
			csTransId = getTransId();
			aaCompTransData.setTransId(csTransId);
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		// create and return a vector containing the Trans Objects
		// and the TransId
		Vector lvReturn = new Vector();
		lvReturn.add(cvTransaction);
		lvReturn.add(aaCompTransData.getTransId());
		return lvReturn;
	}
	/**
	 * Adds or creates a new transaction header in the cache and 
	 * server. The method returns a object of type TransactionHeaderData.
	 * 
	 * @param  aaCompTransData CompleteTransactionData
	 * @param  abBackOffice boolean 
	 * @return TransactionHeaderData 
	 * @throws RTSException
	 */
	public TransactionHeaderData addTransHeader(
		CompleteTransactionData aaCompTransData,
		boolean abBackOffice)
		throws RTSException
	{
		TransactionHeaderData laTransHdrData = null;
		if (aaCompTransData.getVoidTransactionHeaderData() != null)
		{
			//Use previous void trans header
			laTransHdrData =
				aaCompTransData.getVoidTransactionHeaderData();
		}
		else
		{
			laTransHdrData = new TransactionHeaderData();
		}
		laTransHdrData.setTransTime(ciTransTime);
		laTransHdrData.setTransWsId(SystemProperty.getWorkStationId());
		laTransHdrData.setTransAMDate(new RTSDate().getAMDate());
		laTransHdrData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		laTransHdrData.setOfcIssuanceCd(
			SystemProperty.getOfficeIssuanceCd());
		laTransHdrData.setSubstaId(SystemProperty.getSubStationId());

		laTransHdrData.setVersionCd(
			SystemProperty.getMainFrameVersion());

		laTransHdrData.setTransEmpId(SystemProperty.getCurrentEmpId());

		AssignedWorkstationIdsData laAssgndWsIdsData =
			AssignedWorkstationIdsCache.getAsgndWsId(
				SystemProperty.getOfficeIssuanceNo(),
				SystemProperty.getSubStationId(),
				SystemProperty.getWorkStationId());

		if (laAssgndWsIdsData == null)
		{
			laTransHdrData.setCashWsId(
				SystemProperty.getWorkStationId());
		}
		else
		{
			laTransHdrData.setCashWsId(laAssgndWsIdsData.getCashWsId());
		}

		if (abBackOffice)
		{
			laTransHdrData.setCustSeqNo(BACK_OFFICE);
			if (caTransactionControl.getEventType().equals(VOID))
			{
				laTransHdrData.setFeeSourceCd(1);
			}
			else
			{
				laTransHdrData.setFeeSourceCd(0);
			}
			laTransHdrData.setTransName(BACK_OFFICE_TRANSNAME);
		}

		// Not Back Office 
		else
		{
			laTransHdrData.setPrintImmediate(
				SystemProperty.getPrintImmediateIndi());

			CustomerSequence laCustomerSequence =
				CustomerSequence.getInstance();
			laCustomerSequence.increment();
			laTransHdrData.setCustSeqNo(
				CustomerSequence.getInstance().getCustSeqNo());

			if (!caTransactionControl.getEventType().equals(VOID))
			{
				//PROC Get FeeSourceCd for Trans from TransCd
				TransactionCodesData laTransCdsData =
					TransactionCodesCache.getTransCd(
						caTransactionControl.getTransCd());
				if (laTransCdsData != null)
				{
					laTransHdrData.setFeeSourceCd(
						laTransCdsData.getFeeSourceCd());
				}
			}
			if (caTransactionControl
				.getTransCd()
				.equals(TransCdConstant.SBRNW))
			{
				// end defect 10734 
				SubcontractorRenewalCacheData laSubconRenewalCacheData =
					aaCompTransData.getSubcontractorRenewalCacheData();
				if (laSubconRenewalCacheData == null)
				{
					throw new RTSException(
						RTSException.FAILURE_MESSAGE,
						"SubcontractorRenewalData is null",
						"error");
				}
				SubcontractorData laSubconData =
					SubcontractorCache.getSubcon(
						SystemProperty.getOfficeIssuanceNo(),
						SystemProperty.getSubStationId(),
						laSubconRenewalCacheData
							.getSubcontractorHdrData()
							.getSubconId());
				if (laSubconData == null
					|| laSubconData.getName1() == null
					|| laSubconData.getName1().trim().equals(""))
				{
					laTransHdrData.setTransName(
						UNSPECIFIED_SUBCONTRACTOR);
				}
				else
				{
					laTransHdrData.setTransName(
						laSubconData.getName1());
				}
			}
			// defect 10700 
			else if (
				caTransactionControl.getTransCd().equals(
					TransCdConstant.DPSPPT))
			{
				if (aaCompTransData.getSpclPltPrmtData() != null)
				{
					laTransHdrData.setTransName(
						aaCompTransData
							.getSpclPltPrmtData()
							.getPltOwnrName1());
				}
			}
			// end defect 10700 
			else if (caTransactionControl.getEventType().equals(DTA))
			{
				// defect 10290 
				//	DealerData laDealerData =
				//		DealersCache.getDlr(
				//			SystemProperty.getOfficeIssuanceNo(),
				//			SystemProperty.getSubStationId(),
				//			caCurrDlrTtlData.getDealerId());

				if (saDTADealerData == null
					|| UtilityMethods.isEmpty(saDTADealerData.getName1()))
				{
					laTransHdrData.setTransName(UNSPECIFIED_DEALER);
				}
				else
				{
					laTransHdrData.setTransName(
						saDTADealerData.getName1());
				}
				// end defect 10290   
			}
			// CLSOUT does not use this code
			//else if (
			//	caTransactionControl.getEventType().equals(CLSOUT))
			//{
			//	laTransHdrData.setTransName(CLOSE_OUT);
			//}
			else if (caTransactionControl.getEventType().equals(ACC))
			{
				MFVehicleData laMFVehicleData =
					aaCompTransData.getVehicleInfo();
				if (laMFVehicleData != null)
				{
					OwnerData laOwnerData =
						laMFVehicleData.getOwnerData();
					if (laOwnerData != null)
					{
						// defect 10112 
						if (laOwnerData.getName1() == null
							|| laOwnerData.getName1().trim().equals(""))
						{
							laTransHdrData.setTransName(ACCOUNTING);
							Transaction.setCustName(ACCOUNTING);
						}
						else
						{
							Transaction.setCustName(
								laOwnerData.getName1().trim());
						}
						// end defect 10112 
					}
				}
				else
				{
					laTransHdrData.setTransName(ACCOUNTING);
					Transaction.setCustName(ACCOUNTING);
				}
			}
			else if (
				caTransactionControl.getTransCd().equals(
					TransCdConstant.ADLSTX))
			{
				laTransHdrData.setTransName(ADDITIONAL_SALES_TAX);
			}
			// INV Delete does not use this code 

			//else if (
			//	caTransactionControl.getTransCd().equals(
			//		TransCdConstant.INVDEL))
			//{
			//	laTransHdrData.setTransName(INVENTORY_DELETE);
			//}
			else if (
				caTransactionControl.getTransCd().equals(
					TransCdConstant.VOID))
			{
				laTransHdrData.setTransName(
					aaCompTransData.getVoidTransName());
			}
			else
			{
				MFVehicleData laMFVehicleData =
					aaCompTransData.getVehicleInfo();
				String lsOwnerTtlName = NO_CUSTOMER_NAME;
				if (laMFVehicleData != null
					&& laMFVehicleData.getOwnerData() != null)
				{
					OwnerData laOwnerData =
						laMFVehicleData.getOwnerData();

					// defect 10112 
					String lsData = laOwnerData.getName1();
					// end defect 10112

					if (lsData != null && !lsData.trim().equals(""))
					{
						lsOwnerTtlName = lsData;
					}
					// defect 11052 
					else if (!UtilityMethods.isEmpty(aaCompTransData.getCustName()))
					{
						lsOwnerTtlName = aaCompTransData.getCustName(); 
					}
					// end defect 11052 
				}
				laTransHdrData.setTransName(lsOwnerTtlName);
			}
		}
		return laTransHdrData;
	}

	/**
	 * Creates barcoded receipt text stream, if required.
	 *
	 * @param  avVector (TransactionData?) used to create barcode receipt
	 * @return String containing barcoded receipt, terminated with EOF 
	 *		(char 12), or a null string if no barcoded receipt required. 
	 * @throws RTSException 
	 */
	private String barcodeNeeded(Vector avVector) throws RTSException
	{
		String lsReturn = null;
		if (caTransactionControl.getEventType().equals(TTL)
			|| caTransactionControl.getEventType().equals(DTA))
		{
			GenTitleReceipt laGenRpt = new GenTitleReceipt();
			laGenRpt.formatReceipt(avVector, true);
			String lsRcptContent = laGenRpt.getReceipt().toString();

			if (lsRcptContent != null && !lsRcptContent.equals(""))
			{
				// defect 8524
				// FormFeed constant cleanup 
				lsReturn = lsRcptContent + ReportConstant.FF;
				// end defect 8524
			}
		}
		// defect 10700 
		// Now included in "permitNeeded" 
		//		 // defect 10491 
		//		else if (caTransactionControl.getEventType().equals(MRG))
		//		{
		//			GenTimedPermit laGenPrmt = new GenTimedPermit();
		//			laGenPrmt.formatReceipt(avVector);
		//			String lsRcptContent = laGenPrmt.getReceipt().toString();
		//			if (!UtilityMethods.isEmpty(lsRcptContent))
		//			{
		//				lsReturn = lsRcptContent + ReportConstant.FF;
		//			}
		//		}
		//		 // end defect 10491
		// end defect 10700 

		return lsReturn;
	}

	/**
	 * Creates Permit receipt text stream, if required.
	 *
	 * @param  avVector 
	 * @return String  
	 * @throws RTSException 
	 */
	private String permitNeeded(Vector avVector) throws RTSException
	{
		String lsReturn = null;

		CompleteTransactionData laCTData =
			(CompleteTransactionData) avVector.elementAt(0);

		if (laCTData.getSpclPltPrmtData() != null)
		{
			GenSpecialPlatePermit laGenSpclPltPrmt =
				new GenSpecialPlatePermit();
			laGenSpclPltPrmt.formatReceipt(avVector);
			String lsRcptContent =
				laGenSpclPltPrmt.getReceipt().toString();
			if (!UtilityMethods.isEmpty(lsRcptContent))
			{
				lsReturn = lsRcptContent + ReportConstant.FF;
			}
		}
		else if (
			caTransactionControl.getEventType().equals(MRG)
				&& laCTData.getTimedPermitData() instanceof PermitData)
		{
			GenTimedPermit laGenPrmt = new GenTimedPermit();
			laGenPrmt.formatReceipt(avVector);
			String lsRcptContent = laGenPrmt.getReceipt().toString();
			if (!UtilityMethods.isEmpty(lsRcptContent))
			{
				lsReturn = lsRcptContent + ReportConstant.FF;
			}
		}
		return lsReturn;
	}

	/**
	 * Build the payment vector. If none exists, a default $0.00 cash 
	 * payment is created.
	 *
	 * @param  aaCompTransData CompleteTransactionData
	 * @param  avTransactionData Vector 
	 * @return Vector containing laCompTransData and associated 
	 *		TransactionPaymentData 
	 */
	private Vector buildPaymentVector(
		CompleteTransactionData aaCompTransData,
		Vector avTransactionData)
	{
		Vector lvReceiptInput = new Vector();
		lvReceiptInput.add(aaCompTransData);
		for (int i = 0; i < avTransactionData.size() - 1; i++)
		{
			TransactionCacheData laTransCacheData =
				(TransactionCacheData) avTransactionData.get(i);
			if (laTransCacheData.getObj()
				instanceof TransactionPaymentData)
			{
				TransactionPaymentData laTransPaymentData =
					(TransactionPaymentData) laTransCacheData.getObj();
				lvReceiptInput.add(laTransPaymentData);
			}
		}
		//Check to see if any payment is present if not add default
		if (lvReceiptInput.size() == 1)
		{
			TransactionPaymentData laTransPaymentData =
				new TransactionPaymentData();
			laTransPaymentData.setPymntType("CASH");
			laTransPaymentData.setPymntTypeAmt(new Dollar("0.00"));
			lvReceiptInput.add(laTransPaymentData);
		}
		return lvReceiptInput;
	}
	/**
	 * Cache IO
	 * reads from transaction cache for readFromCache 
	 * writes to transaction cache for writeToCache
	 *
	 * @param  aiType int
	 * @param  avTransCacheData Vector
	 * @param  aaRTSDateRTSDate RTSDate 
	 * @return Map
	 * @throws RTSException
	 */
	private static synchronized Map cacheIO(
		int aiType,
		Vector avTransCacheData,
		RTSDate aaRTSDate)
		throws RTSException
	{
		Map lhmMap = new HashMap();
		if (aiType == READ)
		{
			FileInputStream laFileInputStream = null;
			ObjectInputStream laObjectInputStream = null;
			Vector lvTransactionCacheVector = new Vector();
			try
			{
				File laTransCacheDirectory =
					new File(SystemProperty.getTransactionDirectory());
				int liFileName = 0;

				// Order files list
				File[] larrFiles = laTransCacheDirectory.listFiles();
				Arrays.sort(larrFiles);

				for (int i = 0; i < larrFiles.length; i++)
				{
					if (larrFiles[i].isDirectory())
					{
						continue;
					}
					// defect 8352 
					// Ignore file names which are not numeric 
					try
					{
						liFileName =
							Integer.parseInt(larrFiles[i].getName());
					}
					catch (NumberFormatException aeNFEx)
					{
						continue;
					}
					// end defect 8352 

					// Process if FileName >= Parm Date
					// (TrxCache FileName = RTSDate)  
					if (liFileName >= aaRTSDate.getAMDate())
					{
						laFileInputStream =
							new FileInputStream(larrFiles[i]);
						laObjectInputStream =
							new ObjectInputStream(laFileInputStream);
						Object laObject;

						boolean lbContinue = true;
						while (laFileInputStream.available() > 0
							&& lbContinue)
						{
							laObject = laObjectInputStream.readObject();
							Vector lvTransactionCache =
								(Vector) laObject;

							if (lvTransactionCache.size() > 0)
							{
								TransactionCacheData laTransCacheData =
									(
										TransactionCacheData) lvTransactionCache
											.get(
										0);

								// Only process those transactions where 
								// date & time > passed parm
								if (laTransCacheData
									.getDateTime()
									.compareTo(aaRTSDate)
									> 0)
								{
									lvTransactionCacheVector.add(
										lvTransactionCache);
								}
							}
							else
							{
								lbContinue = false;
							}
						}
						laObjectInputStream.close();
						laFileInputStream.close();
					}
				}
				lhmMap.put("VECTOR", lvTransactionCacheVector);
				// defect 11406
				// only populate svCacheQueue if NOT verifying cache
				if (!sbVerifyingCache) 
				{
					svCacheQueue = lvTransactionCacheVector;
				}
				// end defect 11406
				return lhmMap;
			}
			catch (StreamCorruptedException aeSCEx)
			{
				throw new RTSException(RTSException.JAVA_ERROR, aeSCEx);
			}
			catch (IOException aeIOEx)
			{
				throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
			}
			catch (ClassNotFoundException aeCNFEx)
			{
				throw new RTSException(
					RTSException.JAVA_ERROR,
					aeCNFEx);
			}
		}
		else
		{
			try
			{
				// If !isDBReady(), add to the CacheQueue processed by 
				// SendCache 
				if (!(com
					.txdot
					.isd
					.rts
					.client
					.desktop
					.RTSApplicationController
					.isDBReady()))
				{
					svCacheQueue.add(avTransCacheData);
				}
				// Always add to Transaction Cache 
				RTSDate aaToday = RTSDate.getCurrentDate();
				String lsFilename =
					Integer.toString(aaToday.getAMDate());
				File laTRXCacheFile =
					new File(
						SystemProperty.getTransactionDirectory()
							+ lsFilename);
				FileOutputStream laFileOutputStream =
					new FileOutputStream(
						SystemProperty.getTransactionDirectory()
							+ lsFilename,
						true);
				ObjectOutputStream laObjectOutputStream = null;
				if (laTRXCacheFile.length() > 0)
				{
					laObjectOutputStream =
						new NoHeaderOutputStream(laFileOutputStream);
				}
				else
				{
					laObjectOutputStream =
						new ObjectOutputStream(laFileOutputStream);
				}
				laObjectOutputStream.writeObject(avTransCacheData);
				laObjectOutputStream.flush();
				laObjectOutputStream.close();
				laFileOutputStream.close();
				lhmMap.put("INT", new Integer(SUCCESS));
				return lhmMap;
			}
			catch (IOException aeIOEx)
			{
				throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
			}
		}
	}
	/**
	 * Calculate Plate Age
	 *
	 * @param aaCompTransData CompleteTransactionData 
	 */
	private void calculatePlateAge(CompleteTransactionData aaCompTransData)
	{
		// defect 8901 
		// Assign plateAge of 0 when issue new plate
		if (aaCompTransData.getAllocInvItms() != null)
		{
			for (int i = 0;
				i < aaCompTransData.getAllocInvItms().size();
				i++)
			{
				ProcessInventoryData laProcInvData =
					(ProcessInventoryData) aaCompTransData
						.getAllocInvItms()
						.get(
						i);
				ItemCodesData laItemCodesData =
					ItemCodesCache.getItmCd(laProcInvData.getItmCd());

				// defect 9056
				// Add null check for VehicleInfo and RegData.
				// Regional collections do not have VehicleInfo attached.
				if (laItemCodesData.getItmTrckngType().equals("P")
					&& aaCompTransData.getVehicleInfo() != null
					&& aaCompTransData.getVehicleInfo().getRegData()
						!= null)
				{
					// end defect 9056
					aaCompTransData
						.getVehicleInfo()
						.getRegData()
						.setRegPltAge(
						0);
					break;
				}
			}
		}
		// end defect 8901 
	}
	/**
	 * Create a vector of TransactionCacheObject which is used on the server
	 * side to delete the entire set of Transaction
	 * 
	 * @param avSendToServer Vector
	 * @param aaTransHdrData TransactionHeaderData 
	 * @param aaCurrentDateTime RTSDate
	 */
	public static void createDeleteAllTransVector(
		Vector avSendToServer,
		TransactionHeaderData aaTransHdrData,
		RTSDate aaCurrentDateTime)
	{
		TransactionCacheData laTransCacheData =
			new TransactionCacheData();
		// TransactionHeader
		aaTransHdrData.setCacheTransAMDate(
			aaCurrentDateTime.getAMDate());
		aaTransHdrData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData.setObj(aaTransHdrData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);

		// defect 6664
		// Order transaction vector for DELETE the same as INSERT
		// TRANSHDR,TRANS,MVFUNC,INVFUNC,TRINVDETAIL,FUNDFUNC,TRFDSDETAIL

		//Transaction
		TransactionData laTransData = new TransactionData();
		laTransData.setOfcIssuanceNo(aaTransHdrData.getOfcIssuanceNo());
		laTransData.setSubstaId(aaTransHdrData.getSubstaId());
		laTransData.setTransAMDate(aaTransHdrData.getTransAMDate());
		laTransData.setTransWsId(aaTransHdrData.getTransWsId());
		laTransData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laTransData.setCacheTransAMDate(aaCurrentDateTime.getAMDate());
		laTransData.setCacheTransTime(aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laTransData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);

		//MotorVehicleFunctionTransaction
		MotorVehicleFunctionTransactionData laMVFuncTransData =
			new MotorVehicleFunctionTransactionData();
		laMVFuncTransData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laMVFuncTransData.setSubstaId(aaTransHdrData.getSubstaId());
		laMVFuncTransData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laMVFuncTransData.setTransWsId(aaTransHdrData.getTransWsId());
		laMVFuncTransData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laMVFuncTransData.setCacheTransAMDate(
			aaCurrentDateTime.getAMDate());
		laMVFuncTransData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laMVFuncTransData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);

		//InventoryFunctionTransaction
		InventoryFunctionTransactionData laInvFuncTransData =
			new InventoryFunctionTransactionData();
		laInvFuncTransData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laInvFuncTransData.setSubstaId(aaTransHdrData.getSubstaId());
		laInvFuncTransData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laInvFuncTransData.setTransWsId(aaTransHdrData.getTransWsId());
		laInvFuncTransData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laInvFuncTransData.setCacheTransAMDate(
			aaCurrentDateTime.getAMDate());
		laInvFuncTransData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laInvFuncTransData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);

		//TransactionInventoryDetail
		TransactionInventoryDetailData laTrInvDetailData =
			new TransactionInventoryDetailData();
		laTrInvDetailData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laTrInvDetailData.setSubstaId(aaTransHdrData.getSubstaId());
		laTrInvDetailData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laTrInvDetailData.setTransWsId(aaTransHdrData.getTransWsId());
		laTrInvDetailData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laTrInvDetailData.setCacheTransAMDate(
			aaCurrentDateTime.getAMDate());
		laTrInvDetailData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laTrInvDetailData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);

		//FundFuncTrans
		FundFunctionTransactionData laFundFuncTrnsData =
			new FundFunctionTransactionData();
		laFundFuncTrnsData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laFundFuncTrnsData.setSubstaId(aaTransHdrData.getSubstaId());
		laFundFuncTrnsData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laFundFuncTrnsData.setTransWsId(aaTransHdrData.getTransWsId());
		laFundFuncTrnsData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laFundFuncTrnsData.setCacheTransAMDate(
			aaCurrentDateTime.getAMDate());
		laFundFuncTrnsData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laFundFuncTrnsData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);

		//TransactionFundsDetail
		TransactionFundsDetailData laTrFdsDetailData =
			new TransactionFundsDetailData();
		laTrFdsDetailData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laTrFdsDetailData.setSubstaId(aaTransHdrData.getSubstaId());
		laTrFdsDetailData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laTrFdsDetailData.setTransWsId(aaTransHdrData.getTransWsId());
		laTrFdsDetailData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laTrFdsDetailData.setCacheTransAMDate(
			aaCurrentDateTime.getAMDate());
		laTrFdsDetailData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laTrFdsDetailData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);
		// end defect 6664
		// defect 8900
		// ExemptAudit
		ExemptAuditData laExmptAuditData = new ExemptAuditData();
		laExmptAuditData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laExmptAuditData.setSubstaId(aaTransHdrData.getSubstaId());
		laExmptAuditData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laExmptAuditData.setTransWsId(aaTransHdrData.getTransWsId());
		laExmptAuditData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laExmptAuditData.setCacheTransAMDate(
			aaCurrentDateTime.getAMDate());
		laExmptAuditData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laExmptAuditData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);
		// end defect 8900

		// defect 9085 
		// SpecialRegistrationFunctionTransaction
		SpecialRegistrationFunctionTransactionData laSRFuncTransData =
			new SpecialRegistrationFunctionTransactionData();
		laSRFuncTransData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laSRFuncTransData.setSubstaId(aaTransHdrData.getSubstaId());
		laSRFuncTransData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laSRFuncTransData.setTransWsId(aaTransHdrData.getTransWsId());
		laSRFuncTransData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laSRFuncTransData.setCacheTransAMDate(
			aaCurrentDateTime.getAMDate());
		laSRFuncTransData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laSRFuncTransData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);

		// defect 9085 
		// SpecialPlateTransactionHistory
		SpecialPlateTransactionHistoryData laSpclPltTransHstryTransData =
			new SpecialPlateTransactionHistoryData();
		laSpclPltTransHstryTransData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laSpclPltTransHstryTransData.setSubstaId(
			aaTransHdrData.getSubstaId());
		laSpclPltTransHstryTransData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laSpclPltTransHstryTransData.setTransWsId(
			aaTransHdrData.getTransWsId());
		laSpclPltTransHstryTransData.setCustSeqNo(
			aaTransHdrData.getCustSeqNo());
		laSpclPltTransHstryTransData.setCacheTransAMDate(
			aaCurrentDateTime.getAMDate());
		laSpclPltTransHstryTransData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laSpclPltTransHstryTransData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);
		// end defect 9085 

		// defect 9831 
		DisabledPlacardTransactionData laDPTransData =
			new DisabledPlacardTransactionData();
		laDPTransData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laDPTransData.setSubstaId(aaTransHdrData.getSubstaId());
		laDPTransData.setTransAMDate(aaTransHdrData.getTransAMDate());
		laDPTransData.setTransWsId(aaTransHdrData.getTransWsId());
		laDPTransData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laDPTransData.setCacheTransAMDate(
			aaCurrentDateTime.getAMDate());
		laDPTransData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laDPTransData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);
		// end defect 9831

		// defect 9972
		ElectronicTitleHistoryData laETtlHstryData =
			new ElectronicTitleHistoryData();
		laETtlHstryData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laETtlHstryData.setSubstaId(aaTransHdrData.getSubstaId());
		laETtlHstryData.setTransAMDate(aaTransHdrData.getTransAMDate());
		laETtlHstryData.setTransWsId(aaTransHdrData.getTransWsId());
		laETtlHstryData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laETtlHstryData.setCacheTransAMDate(
			aaCurrentDateTime.getAMDate());
		laETtlHstryData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laETtlHstryData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);
		// end defect 9972	

		// defect 10491 
		PermitTransactionData laPrmtTransData =
			new PermitTransactionData();
		laPrmtTransData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laPrmtTransData.setSubstaId(aaTransHdrData.getSubstaId());
		laPrmtTransData.setTransAMDate(aaTransHdrData.getTransAMDate());
		laPrmtTransData.setTransWsId(aaTransHdrData.getTransWsId());
		laPrmtTransData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laPrmtTransData.setCacheTransAMDate(
			aaCurrentDateTime.getAMDate());
		laPrmtTransData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laPrmtTransData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);
		// end defect 10491 	

		// defect 10700  
		SpecialPlatePermitData laSpclPltPrmtData =
			new SpecialPlatePermitData();
		laSpclPltPrmtData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laSpclPltPrmtData.setSubstaId(aaTransHdrData.getSubstaId());
		laSpclPltPrmtData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laSpclPltPrmtData.setTransWsId(aaTransHdrData.getTransWsId());
		laSpclPltPrmtData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laSpclPltPrmtData.setCacheTransAMDate(
			aaTransHdrData.getTransAMDate());
		laSpclPltPrmtData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laTransCacheData = new TransactionCacheData();
		laTransCacheData.setObj(laSpclPltPrmtData);
		laTransCacheData.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData);
		// end defect 10700

		// defect 10844 
		ModifyPermitTransactionHistoryData laModPrmtTransHstryData =
			new ModifyPermitTransactionHistoryData();
		laModPrmtTransHstryData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laModPrmtTransHstryData.setSubstaId(
			aaTransHdrData.getSubstaId());
		laModPrmtTransHstryData.setTransWsId(
			aaTransHdrData.getTransWsId());
		laModPrmtTransHstryData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laModPrmtTransHstryData.setCacheTransAMDate(
			aaTransHdrData.getTransAMDate());
		laModPrmtTransHstryData.setCacheTransTime(
			aaCurrentDateTime.get24HrTime());
		laModPrmtTransHstryData.setCustSeqNo(
			aaTransHdrData.getCustSeqNo());
		TransactionCacheData laTransCacheData6 =
			new TransactionCacheData();
		laTransCacheData6.setObj(laModPrmtTransHstryData);
		laTransCacheData6.setProcName(TransactionCacheData.DELETE);
		avSendToServer.addElement(laTransCacheData6);
		// end defect 10844
	}

	/**
	 * Create a vector of TransactionCacheData to delete a subset of 
	 * Transaction on the server side.
	 * 
	 * @param avSendToServer Vector 
	 * @param avTransactionKey Vector  
	 */
	private static void createDeleteSelectedTransVector(
		Vector avSendToServer,
		Vector avTransactionKey)
	{
		RTSDate laCurrentDateTime = new RTSDate();
		if (avTransactionKey != null && avTransactionKey.size() > 0)
		{
			for (int i = 0; i < avTransactionKey.size(); i++)
			{
				TransactionKey laTransKey =
					(TransactionKey) avTransactionKey.get(i);

				// defect 6664
				// Order transaction vector for DELETE same as INSERT
				// TRANSHDR,TRANS,MVFUNC,INVFUNC,TRINVDETAIL,FUNDFUNC,
				// TRFDSDETAIL
				//Transaction
				TransactionData laTransData = new TransactionData();
				laTransData.setOfcIssuanceNo(
					laTransKey.getOfcIssuanceNo());
				laTransData.setSubstaId(laTransKey.getSubstaId());
				laTransData.setTransAMDate(laTransKey.getTransAMDate());
				laTransData.setTransWsId(laTransKey.getTransWsId());
				laTransData.setCustSeqNo(laTransKey.getCustSeqNo());
				laTransData.setTransTime(laTransKey.getTransTime());
				laTransData.setCacheTransAMDate(
					laCurrentDateTime.getAMDate());
				laTransData.setCacheTransTime(
					laCurrentDateTime.get24HrTime());
				TransactionCacheData laTransCacheData =
					new TransactionCacheData();
				laTransCacheData.setObj(laTransData);
				laTransCacheData.setProcName(
					TransactionCacheData.DELETE);
				avSendToServer.addElement(laTransCacheData);

				//MotorVehicleFunctionTransaction
				MotorVehicleFunctionTransactionData laMVFuncTransData =
					new MotorVehicleFunctionTransactionData();
				laMVFuncTransData.setOfcIssuanceNo(
					laTransKey.getOfcIssuanceNo());
				laMVFuncTransData.setSubstaId(laTransKey.getSubstaId());
				laMVFuncTransData.setTransAMDate(
					laTransKey.getTransAMDate());
				laMVFuncTransData.setTransWsId(
					laTransKey.getTransWsId());
				laMVFuncTransData.setCustSeqNo(
					laTransKey.getCustSeqNo());
				laMVFuncTransData.setTransTime(
					laTransKey.getTransTime());
				laMVFuncTransData.setCacheTransAMDate(
					laCurrentDateTime.getAMDate());
				laMVFuncTransData.setCacheTransTime(
					laCurrentDateTime.get24HrTime());

				laTransCacheData = new TransactionCacheData();
				laTransCacheData.setObj(laMVFuncTransData);
				laTransCacheData.setProcName(
					TransactionCacheData.DELETE);
				avSendToServer.addElement(laTransCacheData);

				//InventoryFunctionTransaction
				InventoryFunctionTransactionData laInvFuncTransData =
					new InventoryFunctionTransactionData();
				laInvFuncTransData.setOfcIssuanceNo(
					laTransKey.getOfcIssuanceNo());
				laInvFuncTransData.setSubstaId(
					laTransKey.getSubstaId());
				laInvFuncTransData.setTransAMDate(
					laTransKey.getTransAMDate());
				laInvFuncTransData.setTransWsId(
					laTransKey.getTransWsId());
				laInvFuncTransData.setCustSeqNo(
					laTransKey.getCustSeqNo());
				laInvFuncTransData.setTransTime(
					laTransKey.getTransTime());
				laInvFuncTransData.setCacheTransAMDate(
					laCurrentDateTime.getAMDate());
				laInvFuncTransData.setCacheTransTime(
					laCurrentDateTime.get24HrTime());
				laTransCacheData = new TransactionCacheData();
				laTransCacheData.setObj(laInvFuncTransData);
				laTransCacheData.setProcName(
					TransactionCacheData.DELETE);
				avSendToServer.addElement(laTransCacheData);

				//TransactionInventoryDetail
				TransactionInventoryDetailData laTrInvDetailData =
					new TransactionInventoryDetailData();
				laTrInvDetailData.setOfcIssuanceNo(
					laTransKey.getOfcIssuanceNo());
				laTrInvDetailData.setSubstaId(laTransKey.getSubstaId());
				laTrInvDetailData.setTransAMDate(
					laTransKey.getTransAMDate());
				laTrInvDetailData.setTransWsId(
					laTransKey.getTransWsId());
				laTrInvDetailData.setCustSeqNo(
					laTransKey.getCustSeqNo());
				laTrInvDetailData.setTransTime(
					laTransKey.getTransTime());
				laTrInvDetailData.setCacheTransAMDate(
					laCurrentDateTime.getAMDate());
				laTrInvDetailData.setCacheTransTime(
					laCurrentDateTime.get24HrTime());
				laTransCacheData = new TransactionCacheData();
				laTransCacheData.setObj(laTrInvDetailData);
				laTransCacheData.setProcName(
					TransactionCacheData.DELETE);
				avSendToServer.addElement(laTransCacheData);

				//FundFuncTrans
				FundFunctionTransactionData laFundFuncTrnsData =
					new FundFunctionTransactionData();
				laFundFuncTrnsData.setOfcIssuanceNo(
					laTransKey.getOfcIssuanceNo());
				laFundFuncTrnsData.setSubstaId(
					laTransKey.getSubstaId());
				laFundFuncTrnsData.setTransAMDate(
					laTransKey.getTransAMDate());
				laFundFuncTrnsData.setTransWsId(
					laTransKey.getTransWsId());
				laFundFuncTrnsData.setCustSeqNo(
					laTransKey.getCustSeqNo());
				laFundFuncTrnsData.setTransTime(
					laTransKey.getTransTime());
				laFundFuncTrnsData.setCacheTransAMDate(
					laCurrentDateTime.getAMDate());
				laFundFuncTrnsData.setCacheTransTime(
					laCurrentDateTime.get24HrTime());
				laTransCacheData = new TransactionCacheData();
				laTransCacheData.setObj(laFundFuncTrnsData);
				laTransCacheData.setProcName(
					TransactionCacheData.DELETE);
				avSendToServer.addElement(laTransCacheData);

				//TransactionFundsDetail
				TransactionFundsDetailData laTrFdsDetailData =
					new TransactionFundsDetailData();
				laTrFdsDetailData.setOfcIssuanceNo(
					laTransKey.getOfcIssuanceNo());
				laTrFdsDetailData.setSubstaId(laTransKey.getSubstaId());
				laTrFdsDetailData.setTransAMDate(
					laTransKey.getTransAMDate());
				laTrFdsDetailData.setTransWsId(
					laTransKey.getTransWsId());
				laTrFdsDetailData.setCustSeqNo(
					laTransKey.getCustSeqNo());
				laTrFdsDetailData.setTransTime(
					laTransKey.getTransTime());
				laTrFdsDetailData.setCacheTransAMDate(
					laCurrentDateTime.getAMDate());
				laTrFdsDetailData.setCacheTransTime(
					laCurrentDateTime.get24HrTime());
				laTransCacheData = new TransactionCacheData();
				laTransCacheData.setObj(laTrFdsDetailData);
				laTransCacheData.setProcName(
					TransactionCacheData.DELETE);
				avSendToServer.addElement(laTransCacheData);
				// end defect 6664

				// defect 8900 
				// Exempt Audit Data 
				ExemptAuditData laExmptAuditData =
					new ExemptAuditData();
				laExmptAuditData.setOfcIssuanceNo(
					laTransKey.getOfcIssuanceNo());
				laExmptAuditData.setSubstaId(laTransKey.getSubstaId());
				laExmptAuditData.setTransAMDate(
					laTransKey.getTransAMDate());
				laExmptAuditData.setTransWsId(
					laTransKey.getTransWsId());
				laExmptAuditData.setCustSeqNo(
					laTransKey.getCustSeqNo());
				laExmptAuditData.setTransTime(
					laTransKey.getTransTime());
				laExmptAuditData.setCacheTransAMDate(
					laCurrentDateTime.getAMDate());
				laExmptAuditData.setCacheTransTime(
					laCurrentDateTime.get24HrTime());
				laTransCacheData = new TransactionCacheData();
				laTransCacheData.setObj(laExmptAuditData);
				laTransCacheData.setProcName(
					TransactionCacheData.DELETE);
				avSendToServer.addElement(laTransCacheData);
				// end defect 8900

				//	defect 9085 
				// SpecialRegistrationFunctionTransaction
				SpecialRegistrationFunctionTransactionData laSRFuncTransData =
					new SpecialRegistrationFunctionTransactionData();
				laSRFuncTransData.setOfcIssuanceNo(
					laTransKey.getOfcIssuanceNo());
				laSRFuncTransData.setSubstaId(laTransKey.getSubstaId());
				laSRFuncTransData.setTransAMDate(
					laTransKey.getTransAMDate());
				laSRFuncTransData.setTransWsId(
					laTransKey.getTransWsId());
				laSRFuncTransData.setCustSeqNo(
					laTransKey.getCustSeqNo());
				laSRFuncTransData.setCacheTransAMDate(
					laCurrentDateTime.getAMDate());
				laSRFuncTransData.setCacheTransTime(
					laCurrentDateTime.get24HrTime());
				laSRFuncTransData.setTransTime(
					laTransKey.getTransTime());
				laTransCacheData = new TransactionCacheData();
				laTransCacheData.setObj(laSRFuncTransData);
				laTransCacheData.setProcName(
					TransactionCacheData.DELETE);
				avSendToServer.addElement(laTransCacheData);

				// defect 9085 
				// SpecialPlateTransactionHistory
				SpecialPlateTransactionHistoryData laSpclPltTransHstryTransData =
					new SpecialPlateTransactionHistoryData();
				laSpclPltTransHstryTransData.setOfcIssuanceNo(
					laTransKey.getOfcIssuanceNo());
				laSpclPltTransHstryTransData.setSubstaId(
					laTransKey.getSubstaId());
				laSpclPltTransHstryTransData.setTransAMDate(
					laTransKey.getTransAMDate());
				laSpclPltTransHstryTransData.setTransWsId(
					laTransKey.getTransWsId());
				laSpclPltTransHstryTransData.setCustSeqNo(
					laTransKey.getCustSeqNo());
				laSpclPltTransHstryTransData.setTransTime(
					laTransKey.getTransTime());
				laSpclPltTransHstryTransData.setCacheTransAMDate(
					laCurrentDateTime.getAMDate());
				laSpclPltTransHstryTransData.setCacheTransTime(
					laCurrentDateTime.get24HrTime());
				laTransCacheData = new TransactionCacheData();
				laTransCacheData.setObj(laSpclPltTransHstryTransData);
				laTransCacheData.setProcName(
					TransactionCacheData.DELETE);
				avSendToServer.addElement(laTransCacheData);
				// end defect 9085

				// defect 10491 
				PermitTransactionData laPrmtTransData =
					new PermitTransactionData();
				laPrmtTransData.setOfcIssuanceNo(
					laTransKey.getOfcIssuanceNo());
				laPrmtTransData.setSubstaId(laTransKey.getSubstaId());
				laPrmtTransData.setTransAMDate(
					laTransKey.getTransAMDate());
				laPrmtTransData.setTransWsId(laTransKey.getTransWsId());
				laPrmtTransData.setCustSeqNo(laTransKey.getCustSeqNo());
				laPrmtTransData.setTransTime(laTransKey.getTransTime());
				laPrmtTransData.setCacheTransAMDate(
					laCurrentDateTime.getAMDate());
				laPrmtTransData.setCacheTransTime(
					laCurrentDateTime.get24HrTime());
				laTransCacheData = new TransactionCacheData();
				laTransCacheData.setObj(laPrmtTransData);
				laTransCacheData.setProcName(
					TransactionCacheData.DELETE);
				avSendToServer.addElement(laTransCacheData);
				// end defect 10491 	

				// defect 10700  
				SpecialPlatePermitData laSpclPltPrmtData =
					new SpecialPlatePermitData();
				laSpclPltPrmtData.setOfcIssuanceNo(
					laTransKey.getOfcIssuanceNo());
				laSpclPltPrmtData.setSubstaId(laTransKey.getSubstaId());
				laSpclPltPrmtData.setTransAMDate(
					laTransKey.getTransAMDate());
				laSpclPltPrmtData.setTransWsId(
					laTransKey.getTransWsId());
				laSpclPltPrmtData.setCustSeqNo(
					laTransKey.getCustSeqNo());
				laSpclPltPrmtData.setTransTime(
					laTransKey.getTransTime());
				laSpclPltPrmtData.setCacheTransAMDate(
					laCurrentDateTime.getAMDate());
				laSpclPltPrmtData.setCacheTransTime(
					laCurrentDateTime.get24HrTime());
				laTransCacheData = new TransactionCacheData();
				laTransCacheData.setObj(laSpclPltPrmtData);
				laTransCacheData.setProcName(
					TransactionCacheData.DELETE);
				avSendToServer.addElement(laTransCacheData);
				// end defect 10700 

				// defect 10844 
				ModifyPermitTransactionHistoryData laModPrmtTransHstryData =
					new ModifyPermitTransactionHistoryData();
				laModPrmtTransHstryData.setOfcIssuanceNo(
					laTransKey.getOfcIssuanceNo());
				laModPrmtTransHstryData.setSubstaId(
					laTransKey.getSubstaId());
				laModPrmtTransHstryData.setTransWsId(
					laTransKey.getTransWsId());
				laModPrmtTransHstryData.setTransAMDate(
					laTransKey.getTransAMDate());
				laModPrmtTransHstryData.setTransTime(
					laTransKey.getTransTime());
				laModPrmtTransHstryData.setCacheTransAMDate(
					laCurrentDateTime.getAMDate());
				laModPrmtTransHstryData.setCacheTransTime(
					laCurrentDateTime.get24HrTime());
				laModPrmtTransHstryData.setCustSeqNo(
					laTransKey.getCustSeqNo());
				TransactionCacheData laTransCacheData6 =
					new TransactionCacheData();
				laTransCacheData6.setObj(laModPrmtTransHstryData);
				laTransCacheData6.setProcName(
					TransactionCacheData.DELETE);
				avSendToServer.addElement(laTransCacheData6);
				// end defect 10844
			}
		}
	}
	/**
	 * Create the receipt directory for receipt generation and storage
	 *
	 * @param  abIgnoreDate boolean
	 * @throws RTSException
	 */
	public static void createReceiptDirectory(boolean abIgnoreDate)
		throws RTSException
	{
		String lsCustSeqNo = getPaddedCustSeqNo();
		String lsReceiptRootDir = SystemProperty.getReceiptsDirectory();
		String lsLocationForReceipt =
			lsReceiptRootDir + CSN + lsCustSeqNo + BACK_SLASH;
		String lsMkDir =
			lsLocationForReceipt.substring(
				0,
				lsLocationForReceipt.length() - 1);
		ssRcptDirForCust = lsMkDir + BACK_SLASH;

		try
		{
			// Clear the directory if the receipts are not from today     
			File laRcptFilesDir = new File(lsLocationForReceipt);
			File[] larrRcptFiles = laRcptFilesDir.listFiles();
			if (larrRcptFiles != null && larrRcptFiles.length != 0)
			{
				File laRcptFile = larrRcptFiles[0];
				RTSDate laRcptFileDate =
					new RTSDate((long) laRcptFile.lastModified());
				if (abIgnoreDate)
				{
					for (int i = 0; i < larrRcptFiles.length; i++)
					{
						boolean lbDelIndi = larrRcptFiles[i].delete();
						if (!lbDelIndi)
						{
							throw new RTSException(
								RTSException.FAILURE_MESSAGE,
								"FILE COULD NOT BE DELETED",
								"DELETE FILE");
						}
					}
				}
				else if (
					!laRcptFileDate.equals(RTSDate.getCurrentDate()))
				{
					for (int i = 0; i < larrRcptFiles.length; i++)
					{
						larrRcptFiles[i].delete();
					}
				}
			}
			else
			{
				File laDir = new File(lsMkDir);
				laDir.mkdir();
			}
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
	}
	/**
	 * Create the transaction control
	 *
	 * @param  asTranscd String
	 * @return TransactionControl
	 * @throws RTSException 
	 */
	private void createTransactionControl(String asTranscd)
		throws RTSException
	{
		if (!shTransactionControl.containsKey(asTranscd))
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Unable to locate Control Entry for " + asTranscd,
				"Transacation Control Logic Error");
		}
		caTransactionControl =
			(TransactionControl) shTransactionControl.get(asTranscd);

		// HQ trans do not go to Pending Trans screen
		if (SystemProperty.isHQ()
			&& !caTransactionControl.getEventType().equals(VOID))
		{
			caTransactionControl.setAutoEndTrans(true);
		}
		// end defect 9085 

	}
	/**
	 * Delete Cumulative Receipt Log
	 * 
	 * @throws Exception 
	 */
	public void deleteCumulativeReceiptLog() throws Exception
	{
		File laCumRcptLogFile =
			new File(
				SystemProperty.getReceiptsDirectory()
					+ SystemProperty.getCumRcptLogFileName());
		if (laCumRcptLogFile.exists())
		{
			laCumRcptLogFile.delete();
		}
	}

	/**
	 * 
	 * Copy Doc to Rcpt Directory
	 * 
	 * @param asFileName 
	 * @param asTransCd
	 * @throws RTSException
	 */
	private void copyDocToRcptDirectory(
		String asFileName,
		String asNewFileName)
		throws RTSException
	{
		FileUtil.copyFile(
			asFileName,
			getCompleteTransRcptName(saTransHdrData.getCustSeqNo())
				+ asNewFileName
				+ ".CRT");

	}
	/**
	 * Create Transaction Cache Objects which will be used on the server
	 * side to delete the issued inventory in endTrans()
	 * 
	 * @param  avInventory Vector 
	 * @throws RTSException 
	 */
	private void deleteIssuedInventory(Vector avInventory)
		throws RTSException
	{
		//Get Saved Inventory Information
		CommonClientBusiness laCommonClientBusiness =
			new CommonClientBusiness();
		Object lvSavedObj =
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.GET_SAVED_INVENTORY,
				null);
		//Note: If event type was INV or VOID, lvSavedInv should be null
		if (lvSavedObj instanceof Vector)
		{
			Vector lvSaveInv = (Vector) lvSavedObj;
			if (lvSaveInv != null && lvSaveInv.size() > 0)
			{
				for (int i = 0; i < lvSaveInv.size(); i++)
				{
					ProcessInventoryData laProcInvData =
						(ProcessInventoryData) lvSaveInv.get(i);
					// defect 9085
					laProcInvData.setTransTime(ciTransTime);
					laProcInvData.setTransAMDate(
						new RTSDate().getAMDate());
					laProcInvData.setCacheTransAMDate(
						saTransHdrData.getTransAMDate());
					laProcInvData.setCacheTransTime(
						saTransHdrData.getTransTime());
					// end defect 9085 				
					TransactionCacheData laTransCacheData =
						new TransactionCacheData();
					laTransCacheData.setObj(laProcInvData);
					laTransCacheData.setProcName(
						TransactionCacheData.DELETE);
					avInventory.addElement(laTransCacheData);
				}
			}
		}
	}
	/**
	 * Populate the vector with TransactionCacheObjects used to delete 
	 * selected Transactions on the server side
	 *
	 * @param   avTransKey Vector Transaction to be deleted
	 * @return  int 
	 * @throws RTSException
	 */
	public int deleteSelectedTrans(Vector avTransKey)
		throws RTSException
	{
		Vector lvSendToServer = new Vector();
		createDeleteSelectedTransVector(lvSendToServer, avTransKey);
		// defect 4891
		processCacheVector(cvTransaction);
		// end defect 4891
		return SUCCESS;
	}
	/**
	 * Delete the transaction, release the issued inventory and
	 * reset the transaction variables.
	 *
	 * @return int
	 * @throws RTSException
	 */
	public int deleteTrans() throws RTSException
	{
		if (saTransHdrData == null)
		{
			// defect 10290 
			resetDTA();
			// end defect 10290 

			return SUCCESS;
		}
		try
		{
			RTSDate laRTSDate = new RTSDate();
			Vector lvDeleteItms = new Vector();
			//Release Issued Inventory for Customer Transaction
			//Create TransactionKey
			TransactionKey laTransKey = new TransactionKey();
			laTransKey.setCustSeqNo(saTransHdrData.getCustSeqNo());
			laTransKey.setOfcIssuanceNo(
				saTransHdrData.getOfcIssuanceNo());
			laTransKey.setSubstaId(saTransHdrData.getSubstaId());
			laTransKey.setTransAMDate(saTransHdrData.getTransAMDate());
			laTransKey.setTransWsId(saTransHdrData.getTransWsId());
			laTransKey.setCacheTime(laRTSDate.get24HrTime());
			laTransKey.setCacheAMDate(laRTSDate.getAMDate());

			TransactionCacheData laTransCacheData =
				new TransactionCacheData();
			laTransCacheData.setObj(laTransKey);
			laTransCacheData.setProcName(TransactionCacheData.UPDATE);
			lvDeleteItms.addElement(laTransCacheData);
			createDeleteAllTransVector(
				lvDeleteItms,
				saTransHdrData,
				laRTSDate);

			// defect 9085 
			updateSpclPltVIRequest(
				lvDeleteItms,
				TransactionCacheData.DELETE,
				laRTSDate);
			// end defect 9085

			// defect 10491 
			updatePrmtVIRequest(
				lvDeleteItms,
				TransactionCacheData.DELETE,
				laRTSDate);
			// end defect 10491 

			processCacheVector(lvDeleteItms);

			reset();

			siCumulativeTransIndi = 0;

			CommonClientBusiness laCommonClientBusiness =
				new CommonClientBusiness();
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.CLEAR_SAVE_VEH_INFO,
				null);
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_INVENTORY,
				null);

			// defect 9085 
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_VI_SPCL_PLT_VECTOR,
				null);

			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_SPCL_PLT,
				null);
			// end defect 9085 

		}
		catch (RTSException aeRTSEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.TR_ERROR, aeRTSEx);
			throw leRTSEx;
		}
		return SUCCESS;
	}
	/**
	 * Delete Void Trans Log file
	 * 
	 * @throws Exception 
	 */
	private void deleteVoidTransLog() throws Exception
	{
		File laVoidTransLogFile =
			new File(
				SystemProperty.getReceiptsDirectory()
					+ SystemProperty.getVoidTransLogFileName());
		if (laVoidTransLogFile.exists())
		{
			laVoidTransLogFile.delete();
		}
	}
	/**
	 * Used by receipt generation to reconstruct CompleteTransactionData
	 * that is serialized in addTrans()
	 * 
	 * @param  asDirName String
	 * @return CompleteTransactionData
	 * @throws RTSException
	 */
	private static CompleteTransactionData deSerializeCompleteTrans(String asDirName)
		throws RTSException
	{
		try
		{
			File laFile = new File(asDirName);
			FileInputStream laFileInputStream =
				new FileInputStream(laFile);
			BufferedReader laBuffRdr =
				new BufferedReader(
					new InputStreamReader(laFileInputStream));
			String lsLineIn = null;
			Object laObject = null;
			while ((lsLineIn = laBuffRdr.readLine()) != null)
			{
				laObject = Comm.StringToObj(lsLineIn);
			}
			laBuffRdr.close();
			laFileInputStream.close();
			if (laObject != null)
			{
				return (CompleteTransactionData) laObject;
			}
			else
			{
				return null;
			}
		}
		// defect 11052 
		catch (FileNotFoundException aeFNFEx)
		{
			return null; 
		}
		// end defect 11052 
		catch (IOException aeIOEx)
		{
			RTSException laRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeIOEx);
			throw laRTSEx;
		}
	}
	/**
	 * deserialize file for set aside
	 *
	 * @param aaFile File
	 * @throws RTSException 
	 */
	private static Object deSerializeFileForSetAside(File aaFile)
		throws RTSException
	{
		try
		{
			if (aaFile.exists())
			{
				FileInputStream laFileInputStream =
					new FileInputStream(aaFile);
				ObjectInputStream laObjectInputStream =
					new ObjectInputStream(laFileInputStream);
				Object laObject = laObjectInputStream.readObject();
				laObjectInputStream.close();
				return laObject;
			}
		}
		catch (IOException aeIOEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeIOEx);
			throw leRTSEx;
		}
		catch (ClassNotFoundException aeCNFEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeCNFEx);
			throw leRTSEx;
		}
		return null;
	}

	//	/**
	//	 * Deserialize Object 
	//	 * 
	//	 * @param asFilename String
	//	 * @return Object
	//	 * @throws RTSException
	//	 */
	//	private static Object deserializeObject(String asFileName)
	//		throws RTSException
	//	{
	//		try
	//		{
	//			FileInputStream laFileInputStream =
	//				new FileInputStream(asFileName);
	//			ObjectInputStream laObjectInputStream =
	//				new ObjectInputStream(laFileInputStream);
	//			Object laObject = laObjectInputStream.readObject();
	//			laObjectInputStream.close();
	//			return laObject;
	//		}
	//		catch (ClassNotFoundException aeCNFEx)
	//		{
	//			throw new RTSException(RTSException.JAVA_ERROR, aeCNFEx);
	//		}
	//		catch (IOException aeIOEx)
	//		{
	//			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
	//		}
	//	} 

	/**
	 * Returns a vector of transactioncache data objects for Direct 
	 * Sendcache
	 *
	 * @param aaBegDate RTSDate
	 * @param aaEndDate RTSDate
	 * @return Vector
	 */
	public static synchronized Vector directSendCacheRead(
		RTSDate aaBegDate,
		RTSDate aaEndDate)
	{
		FileInputStream laFileInputStream = null;
		ObjectInputStream laObjectInputStream = null;
		Vector lvTransCacheDataVector = new Vector();
		try
		{
			File laFile =
				new File(SystemProperty.getTransactionDirectory());
			File[] larrFiles = laFile.listFiles();
			Arrays.sort(larrFiles);
			for (int i = 0; i < larrFiles.length; i++)
			{
				if (larrFiles[i].isDirectory())
				{
					continue;
				}
				laFileInputStream = new FileInputStream(larrFiles[i]);
				laObjectInputStream =
					new ObjectInputStream(laFileInputStream);
				Object laObject;
				while (laFileInputStream.available() > 0)
				{
					laObject = laObjectInputStream.readObject();
					Vector lvTemp = (Vector) laObject;
					// Compare both the start and end date/time
					TransactionCacheData laTransCacheData =
						(TransactionCacheData) lvTemp.get(0);
					if ((laTransCacheData
						.getDateTime()
						.compareTo(aaBegDate)
						>= 0)
						&& (laTransCacheData
							.getDateTime()
							.compareTo(aaEndDate)
							<= 0))
					{
						lvTransCacheDataVector.add(lvTemp);
					}
				}
				laObjectInputStream.close();
				laFileInputStream.close();
			}
		}
		catch (Exception aeEx)
		{
			file : SendCacheLog.write(
				"Direct Send Cache encountered error with cache file "
					+ laFileInputStream);
			System.err.println(
				"Direct Send Cache encountered error with cache file "
					+ laFileInputStream);
			System.err.println(aeEx.getMessage());
			System.exit(0);
		}
		return lvTransCacheDataVector;
	}

	/**
	 * End the Transaction by writing payment records, and 
	 * setting the transtimestamp in the TransactionHeader table.
	 *
	 * @param  avTransPaymentData TransactionPaymentData
	 * @param  aaTransHdrData TransactionHeaderData
	 * @param  avAddlInfo Vector
	 * @return int (always returns SUCCESS) 
	 * @throws RTSException
	 */
	public int endTrans(
		Vector avTransPaymentData,
		TransactionHeaderData aaTransHdrData,
		Vector avAddlInfo)
		throws RTSException
	{
		Log.write(Log.DEBUG, aaTransHdrData, " Begin endTrans");
		boolean lbZeroPaymentRecord = false;

		// defect 6456
		// Handle error based upon success of posting 
		boolean lbSuccessfulPost = false;

		// end defect 6456
		saTransHdrData = aaTransHdrData;
		CompleteTransactionData laCompTransData = null;
		Vector lvTransactionData = new Vector();

		// PROC Write a $0.00 payment record under certain conditions
		try
		{
			if (avTransPaymentData != null)
			{
				if (avTransPaymentData.get(0)
					instanceof CompleteTransactionData)
				{
					laCompTransData =
						(
							CompleteTransactionData) avTransPaymentData
								.get(
							0);
					avTransPaymentData.remove(laCompTransData);
				}
				if (laCompTransData != null)
				{
					// If Not IRENEW
					if (laCompTransData.getTransCode() != null
						&& !laCompTransData.getTransCode().equals(
							TransCdConstant.IRENEW))
					{
						if (caTransactionControl == null)
						{
							String lsTransCd =
								laCompTransData.getTransCode();

							caTransactionControl =
								(
									TransactionControl) shTransactionControl
										.get(
									lsTransCd);
						}
						// If Customer Transaction and Fees Exist 
						// write payment record
						if (avTransPaymentData.size() == 0
							&& caTransactionControl.getCustomer()
								== CUSTOMER
							&& laCompTransData.getRegFeesData() != null
							&& laCompTransData
								.getRegFeesData()
								.getVectFees()
								!= null
							&& laCompTransData
								.getRegFeesData()
								.getVectFees()
								.size()
								> 0)
						{
							TransactionPaymentData laTransPaymentData =
								new TransactionPaymentData();
							laTransPaymentData.setPymntTypeCd(
								FrmPaymentPMT001.CASH);
							laTransPaymentData.setPymntType(
								FrmPaymentPMT001.COMBO_CASH);

							//depending on transcd, a different amount 
							//will be written
							if (laCompTransData.getTransCode() != null
								&& laCompTransData.getTransCode().equals(
									TransCdConstant.RFCASH))
							{
								laTransPaymentData.setPymntTypeAmt(
									Transaction.getRunningSubtotal());
							}
							else
							{
								laTransPaymentData.setPymntTypeAmt(
									new Dollar("0.00"));
							}
							avTransPaymentData.addElement(
								laTransPaymentData);
							lbZeroPaymentRecord = true;
						}
					}
				}
			}
			if (avTransPaymentData != null
				&& avTransPaymentData.size() != 0)
			{
				// Open the cash drawer if a payment was entered
				if ((laCompTransData != null
					&& laCompTransData.getTransCode() != null
					&& laCompTransData.getTransCode().equals(
						TransCdConstant.RFCASH))
					|| (!lbZeroPaymentRecord
						&& !(laCompTransData != null
							&& laCompTransData.getTransCode().equals(
								TransCdConstant.IRENEW))))
				{

					// defect 7111
					if (SystemProperty.getProdStatus()
						== SystemProperty.APP_DEV_STATUS)
					{
						Log.write(
							Log.START_END,
							new SystemProperty(),
							"Open CashDrawer - Development");
					}
					// end defect 7111 
					try
					{
						CashDrawer.open();
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError();
					}
				}
			}
			//Populate Transaction
			lvTransactionData =
				populateTransData(
					avTransPaymentData,
					aaTransHdrData,
					laCompTransData.getCreditCardFeeData());

			deleteIssuedInventory(lvTransactionData);

			// defect 9085 
			RTSDate laCacheRTSDate = new RTSDate();
			laCacheRTSDate.setTime(ciTransTime);

			updateSpclPltVIRequest(
				lvTransactionData,
				TransactionCacheData.UPDATE,
				laCacheRTSDate);
			// end defect 9085

			// defect 10491 
			updatePrmtVIRequest(
				lvTransactionData,
				TransactionCacheData.UPDATE,
				laCacheRTSDate);
			// end defect 10491 

			// For addl formatted data, e.g. SBRNW release inventory
			// on hold 
			if (avAddlInfo != null)
			{
				lvTransactionData.addAll(avAddlInfo);
			}
			// PCR25
			if (laCompTransData != null
				&& laCompTransData.getCreditCardFeeData() != null)
			{
				//add the credit card fee transaction
				TransactionControl laTmpTransControl =
					caTransactionControl;
				caTransactionControl =
					(TransactionControl) shTransactionControl.get(
						TransCdConstant.CCPYMNT);
				populateTrans(
					lvTransactionData,
					laCompTransData,
					aaTransHdrData);
				populateFundFuncTrnsTrFdsDetail(
					lvTransactionData,
					laCompTransData,
					aaTransHdrData);
				caTransactionControl = laTmpTransControl;
			}

			// Post Transactions 
			processCacheVector(lvTransactionData);
			// defect 6456
			// Indicate successful processing of cache 
			lbSuccessfulPost = true;
			// end defect 6456

			// Update Cumulative Receipt Log, Deserialize CTData 
			updateCumRcptLog(true);

			if (laCompTransData
				.getTransCode()
				.equals(TransCdConstant.SBRNW))
			{
				setLastCSN();
				printAllReceipts();
			}
			else
			{
				if (!printDocument(true)
					&& !genDTADealerCompletedReport(lvTransactionData)
					&& !(laCompTransData == null))
				{
					if (SystemProperty.getPrintImmediateIndi() != 0)
					{
						Vector lvReceiptInput =
							buildPaymentVector(
								laCompTransData,
								lvTransactionData);
						String lsFileName =
							saveCashReceipt(lvReceiptInput);
						Print laPrint = new Print();
						setLastCSN();
						laPrint.sendToPrinter(lsFileName, "CSHREG");
					}
					else
					{
						if (sbMultipleTrans == false)
						{
							laCompTransData =
								deSerializeCompleteTrans(
									ssRcptDirForCust
										+ COMPLETE_TRANS_FILE_NAME);
							Vector lvReceiptIn =
								buildPaymentVector(
									laCompTransData,
									lvTransactionData);
							String lsRcptContent =
								formatReceipt(lvReceiptIn);
							Vector avVector = new Vector();
							avVector.add(laCompTransData);
							avVector.add(lsRcptContent);
							avVector.add(barcodeNeeded(avVector));

							// defect 10700 
							avVector.add(permitNeeded(avVector));

							if ((lsRcptContent != null
								&& !lsRcptContent.equals(""))
								|| laCompTransData.getTransCode().equals(
									TransCdConstant.DPSPPT))
							{
								// end defect 10700
								createReceiptDirectory(true);
								String lsReceiptName =
									saveReceipt(avVector);
								setLastCSN();
								Print laPrint = new Print();
								laPrint.sendToPrinter(
									lsReceiptName,
									laCompTransData.getTransCode());
							}
						} // end not multiple trans
						else
						{
							Vector lvReceiptInput =
								buildPaymentVector(
									laCompTransData,
									lvTransactionData);
							saveCashReceipt(lvReceiptInput);
							setLastCSN();
							printAllReceipts();
						}
					}
				}
			}
			// defect 6456 
			// Only reset if finish successfully 
			// Set up for next customer transaction
			reset();
			// end defect 6456 
		}
		// defect 6456 
		catch (RTSException aeRTSEx)
		{
			// If the transaction was successfully posted or we 
			// received a duplicate key exception, display the error
			// and return to the desktop    
			if (lbSuccessfulPost
				|| (aeRTSEx.getDetailMsg() != null
					&& aeRTSEx.getDetailMsg().indexOf(
				//"DuplicateKeyException")
			CommonConstant
					.DUPLICATE_KEY_EXCEPTION)
						> -1))
			{
				aeRTSEx.displayError();
				//Set up for next customer transaction
				reset();
			}
			// Else throw the error and return to the Pending Trans Screen
			// DB is not updated.  
			else
			{
				RTSException leRTSEx =
					new RTSException(RTSException.TR_ERROR, aeRTSEx);
				throw leRTSEx;
			}
		}
		finally
		{
			// No longer reset here if error thrown 
			// reset(); 
			Log.write(Log.DEBUG, aaTransHdrData, " End endTrans");
		}
		// end defect 6456 
		return SUCCESS;
	}
	/**
	 * End the transaction for VOID events
	 *
	 * @param  aaCompTransData CompleteTransactionData
	 * @param  aaTransactionHeaderData TransactionHeaderData
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector endTransForVoid(
		CompleteTransactionData aaCompTransData,
		TransactionHeaderData aaTransHdrData)
		throws RTSException
	{
		Vector lvTransactionCache = new Vector();
		//Build Payment Transaction if req'd
		if (caTransactionControl == null)
		{
			String lsTransCd = aaCompTransData.getTransCode();
			if (!shTransactionControl.containsKey(lsTransCd))
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					"Unable to locate Control Entry for " + lsTransCd,
					"S608 Control Logic Error");
			}
			caTransactionControl =
				(TransactionControl) shTransactionControl.get(
					lsTransCd);
		}
		if (caTransactionControl.getEventType().equals(VOID)
			&& !aaTransHdrData.getTransName().equals(
				BACK_OFFICE_TRANSNAME))
		{
			//P900 Build Payment transaction for VOID
			//Get the TRANS_PAYMENT transaction(s)
			TransactionPaymentData laTransPaymentData =
				new TransactionPaymentData();
			laTransPaymentData.setOfcIssuanceNo(
				aaCompTransData.getVoidOfcIssuanceNo());
			laTransPaymentData.setSubstaId(
				aaCompTransData.getVoidSubstaId());
			laTransPaymentData.setTransAMDate(
				aaCompTransData.getVoidTransAMDate());
			laTransPaymentData.setTransWsId(
				aaCompTransData.getVoidTransWsId());
			laTransPaymentData.setCustSeqNo(
				aaCompTransData.getVoidCustSeqNo());
			CommonClientBusiness laCommonClientBusiness =
				new CommonClientBusiness();
			Vector lvTransPayment =
				(Vector) laCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.GET_TRANS_PAYMENT,
					laTransPaymentData);
			Vector lvNewTransPayment = new Vector();
			if (lvTransPayment != null && lvTransPayment.size() > 0)
			{
				//Test if all transaction were voided for this custseqno
				if (aaCompTransData.getVoidAllIndi() == 1)
				{
					//Build payment for all transactions voided within custseqno
					for (int i = 0; i < lvTransPayment.size(); i++)
					{
						TransactionPaymentData laTransPaymentData2 =
							(
								TransactionPaymentData) lvTransPayment
									.get(
								i);
						TransactionPaymentData laNewTransPaymentData =
							(
								TransactionPaymentData) UtilityMethods
									.copy(
								laTransPaymentData2);
						laNewTransPaymentData.setPymntTypeAmt(
							new Dollar("0.00").subtract(
								laTransPaymentData2.getPymntTypeAmt()));
						laNewTransPaymentData.setChngDue(
							new Dollar("0.00").subtract(
								laTransPaymentData2.getChngDue()));
						laNewTransPaymentData.setTransPostedMfIndi(0);
						lvNewTransPayment.addElement(
							laNewTransPaymentData);
					}
				}
				else
				{
					//Subset of Transactions Voided
					Dollar laVoidPymntAmt =
						aaCompTransData.getVoidPymntAmt();
					int i = 0;
					Dollar laPymntAmount = new Dollar("0.00");
					while (laVoidPymntAmt.compareTo(laPymntAmount) > 0)
					{
						TransactionPaymentData laTransPaymentData3 =
							(
								TransactionPaymentData) lvTransPayment
									.get(
								i);
						TransactionPaymentData laNewTransPaymentData2 =
							(
								TransactionPaymentData) UtilityMethods
									.copy(
								laTransPaymentData3);
						if (laVoidPymntAmt
							.compareTo(
								laTransPaymentData3.getPymntTypeAmt())
							>= 0)
						{
							laNewTransPaymentData2.setPymntTypeAmt(
								laPymntAmount.subtract(
									laTransPaymentData3
										.getPymntTypeAmt()));
							laVoidPymntAmt =
								laVoidPymntAmt.subtract(
									laTransPaymentData3
										.getPymntTypeAmt());
						}
						else
						{
							laNewTransPaymentData2.setPymntTypeAmt(
								laPymntAmount.subtract(laVoidPymntAmt));
							laVoidPymntAmt =
								laVoidPymntAmt.subtract(laVoidPymntAmt);
						}
						laNewTransPaymentData2.setTransPostedMfIndi(0);
						laNewTransPaymentData2.setChngDue(
							laPymntAmount);
						lvNewTransPayment.addElement(
							laNewTransPaymentData2);
						++i;
					}
				}
			}
			lvTransactionCache =
				populateTransData(
					lvNewTransPayment,
					aaTransHdrData,
					aaCompTransData.getCreditCardFeeData());
			;
			reset();
		}
		return lvTransactionCache;
	}
	/**
	 * 
	 * Update Special Plate Virtual Inventory 
	 * 
	 * @param avTrxCache
	 * @throws RTSException
	 */
	private void updateSpclPltVIRequest(
		Vector avTrxCache,
		int aiProcName,
		RTSDate aaRTSDate)
		throws RTSException
	{
		// Get Saved Mfg Plate Requests
		CommonClientBusiness laCommonClientBusiness =
			new CommonClientBusiness();
		Object lvSavedObj =
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.GET_VI_SPCL_PLT_VECTOR,
				null);

		if (lvSavedObj instanceof Vector)
		{
			Vector lvSaveSpclPlt = (Vector) lvSavedObj;
			if (lvSaveSpclPlt != null && lvSaveSpclPlt.size() > 0)
			{
				for (int i = 0; i < lvSaveSpclPlt.size(); i++)
				{
					SpecialPlatesRegisData laSpclPltRegisData =
						(SpecialPlatesRegisData) lvSaveSpclPlt.get(i);
					InventoryAllocationData laInvAllData =
						laSpclPltRegisData.getVIAllocData();
					if (laInvAllData != null)
					{
						TransactionCacheData laTransCacheData =
							new TransactionCacheData();
						laTransCacheData.setObj(laInvAllData);
						laInvAllData.setCacheTransAMDate(
							aaRTSDate.getAMDate());
						laInvAllData.setCacheTransTime(
							aaRTSDate.get24HrTime());
						laTransCacheData.setProcName(aiProcName);
						avTrxCache.addElement(laTransCacheData);
					}
				}
			}

		}
	}

	/**
	 * Update Permit Virtual Inventory 
	 * 
	 * @param avTrxCache
	 * @throws RTSException
	 */
	private void updatePrmtVIRequest(
		Vector avTrxCache,
		int aiProcName,
		RTSDate aaRTSDate)
		throws RTSException
	{
		// Get Saved VI Requests
		CommonClientBusiness laCommonClientBusiness =
			new CommonClientBusiness();

		Object lvSavedObj =
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.GET_VI_PRMT_VECTOR,
				null);

		if (lvSavedObj instanceof Vector)
		{
			Vector lvPrmtData = (Vector) lvSavedObj;
			if (lvPrmtData != null && lvPrmtData.size() > 0)
			{
				for (int i = 0; i < lvPrmtData.size(); i++)
				{
					PermitData laPrmtData =
						(PermitData) lvPrmtData.get(i);

					if (laPrmtData.getVIAllocData() != null)
					{
						InventoryAllocationData laInvAllData =
							laPrmtData.getVIAllocData();

						if (laInvAllData != null)
						{
							TransactionCacheData laTransCacheData =
								new TransactionCacheData();
							laTransCacheData.setObj(laInvAllData);
							laInvAllData.setCacheTransAMDate(
								aaRTSDate.getAMDate());
							laInvAllData.setCacheTransTime(
								aaRTSDate.get24HrTime());
							laTransCacheData.setProcName(aiProcName);
							avTrxCache.addElement(laTransCacheData);
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * Update Special Plate Virtual Inventory for Void  
	 * 
	 * @param aaComplTransData
	 * @param avTrxCache
	 * @throws RTSException
	 */
	private void updateSpclPltVIForVoid(
		CompleteTransactionData aaComplTransData,
		Vector avTrxCache)
		throws RTSException
	{
		String lsVoidTransCd = aaComplTransData.getVoidTransCd();

		boolean lbReplMfgNewPlt =
			isMfgNewSpclPltOnReplace(aaComplTransData, lsVoidTransCd);

		// defect 9386
		// Add SPAPPI  
		boolean lbVIInventory =
			lsVoidTransCd.equals(TransCdConstant.SPAPPL)
				|| lsVoidTransCd.equals(TransCdConstant.SPAPPC)
				|| lsVoidTransCd.equals(TransCdConstant.SPAPPI)
				|| lsVoidTransCd.equals(TransCdConstant.SPUNAC)
				|| lsVoidTransCd.equals(TransCdConstant.SPRSRV);
		// end defect 9386 

		// except where OwnerSupplied (OldPlt) || ROP 
		if (lbVIInventory)
		{
			String lsRegPltCd =
				aaComplTransData
					.getVehicleInfo()
					.getSpclPltRegisData()
					.getRegPltCd();

			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(lsRegPltCd);

			// No VI logic for ROP || OwnerSupplied 
			if (lsRegPltCd.equals("ROP")
				|| laPltTypeData.getNeedsProgramCd().equals(
					SpecialPlatesConstant.OWNER))
			{
				lbVIInventory = false;
			}
		}

		if (lbVIInventory || lbReplMfgNewPlt)
		{
			SpecialPlatesRegisData laSpclPltRegisData =
				aaComplTransData.getVehicleInfo().getSpclPltRegisData();
			InventoryAllocationData laInvAllData =
				new InventoryAllocationData();
			laInvAllData.setTransAmDate(
				aaComplTransData.getVoidTransAMDate());
			laInvAllData.setTransTime(
				aaComplTransData.getVoidTransTime());
			laInvAllData.setCacheTransAMDate(
				saTransHdrData.getCacheTransAMDate());
			laInvAllData.setCacheTransTime(
				saTransHdrData.getTransTime());
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(
					laSpclPltRegisData.getRegPltCd());
			if (laPltTypeData.getUserPltNoIndi() == 1)
			{
				laInvAllData.setViItmCd(
					laSpclPltRegisData.getRegPltCd());
			}
			if (laPltTypeData.getAnnualPltIndi() == 1)
			{
				// defect 9864 
				// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
				laInvAllData.setInvItmYr(
					laSpclPltRegisData.getPltExpYr());
				// end defect 9864 
			}
			laInvAllData.setInvItmNo(laSpclPltRegisData.getRegPltNo());
			laInvAllData.setInvItmEndNo(
				laSpclPltRegisData.getRegPltNo());
			laInvAllData.setTransEmpId(saTransHdrData.getTransEmpId());
			laInvAllData.setInvQty(1);
			laInvAllData.setItmCd(laSpclPltRegisData.getRegPltCd());
			laInvAllData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laInvAllData.setSubstaId(SystemProperty.getSubStationId());
			TransactionCacheData laTransCacheData =
				new TransactionCacheData();
			laTransCacheData.setObj(laInvAllData);
			laTransCacheData.setProcName(TransactionCacheData.DELETE);
			avTrxCache.addElement(laTransCacheData);

		}
	}

	/** 
	 * Update Permit Virtual Inventory for Void  
	 * 
	 * @param aaComplTransData
	 * @param avTrxCache
	 * @throws RTSException
	 */
	private void updatePrmtVIForVoid(
		CompleteTransactionData aaComplTransData,
		Vector avTrxCache)
		throws RTSException
	{
		String lsVoidTransCd = aaComplTransData.getVoidTransCd();

		if (UtilityMethods.isPermitApplication(lsVoidTransCd))
		{
			PermitData laPrmtData =
				(PermitData) aaComplTransData.getTimedPermitData();

			if (laPrmtData.getVIAllocData() != null)
			{
				InventoryAllocationData laInvAllData =
					laPrmtData.getVIAllocData();
				laInvAllData.setCacheTransAMDate(
					saTransHdrData.getCacheTransAMDate());
				laInvAllData.setCacheTransTime(
					saTransHdrData.getTransTime());
				laInvAllData.setTransEmpId(
					saTransHdrData.getTransEmpId());
				laInvAllData.setSubstaId(
					SystemProperty.getSubStationId());
				laInvAllData.setItmCd(laPrmtData.getItmCd());
				TransactionCacheData laTransCacheData =
					new TransactionCacheData();
				laTransCacheData.setObj(laInvAllData);
				laTransCacheData.setProcName(
					TransactionCacheData.DELETE);
				avTrxCache.addElement(laTransCacheData);
			}
		}
	}

	/**
	 * Write to Cumulative Receipt Log
	 * 
	 * @param aaTransHdrData  TransactionHeaderData
	 * @param abGetSerializedCTData boolean 
	 */
	private void updateCumRcptLog(boolean abGetSerializedCTData)
		throws RTSException
	{
		// PCR 34
		String lsCSN = "" + saTransHdrData.getCustSeqNo();
		String lsTransName = null;
		String lsTransCd = null;
		try
		{
			// Subcon Event 
			if (saTransHdrData.getFeeSourceCd() == 2)
			{
				lsTransCd = "SBRNW";
				lsTransName = saTransHdrData.getTransName();
			}
			else
			{
				if (abGetSerializedCTData)
				{
					saCompTransData =
						deSerializeCompleteTrans(
							ssRcptDirForCust
								+ COMPLETE_TRANS_FILE_NAME);
				}

				if (saCompTransData != null)
				{
					if (saCompTransData.getVehicleInfo() != null
						&& saCompTransData.getVehicleInfo().getOwnerData()
							!= null)
					{
						// defect 10112 
						lsTransName =
							saCompTransData
								.getVehicleInfo()
								.getOwnerData()
								.getName1();
						// end defect 10112 
					}
					if (lsTransName == null)
					{
						lsTransName = "*No Name*";
					}
					lsTransCd = saCompTransData.getTransCode();
					if (lsTransCd == null)
					{
						lsTransCd = "*NoTransCode*";
					}
				}
			}
			String lsReceiptRecord =
				lsCSN + " - " + lsTransName + " - " + lsTransCd;
			writeCumRcptLog(lsReceiptRecord);
		}
		catch (RTSException aeEx)
		{
			// defect 6456 
			// throw exception vs. write to log 
			//writeCumRcptLog(
			//	lsCSN + " - " + "*No Name*" + " - " + "*NoTransCode*");
			throw aeEx;
			// end defect 6456 
		}
	}

	/**
	 * Add Transaction Info to Cumulative Receipt Log 
	 * 
	 * @param asReceiptRecord String
	 */
	private void writeCumRcptLog(String asReceiptRecord)
		throws RTSException
	{
		try
		{
			File laFile =
				new File(
					SystemProperty.getReceiptsDirectory()
						+ SystemProperty.getCumRcptLogFileName());
			Vector lvReceiptRecord = null;
			if (laFile.exists())
			{
				FileInputStream laFileInputStream =
					new FileInputStream(laFile);
				if (laFileInputStream.available() == 0)
				{
					laFile.createNewFile();
					lvReceiptRecord = new Vector();
					lvReceiptRecord.add(asReceiptRecord);
				}
				else
				{
					ObjectInputStream laObjectInputStream =
						new ObjectInputStream(laFileInputStream);
					lvReceiptRecord =
						(Vector) laObjectInputStream.readObject();
					// if new day, erase old data
					RTSDate laLastModified =
						new RTSDate(laFile.lastModified());
					RTSDate laToday = RTSDate.getCurrentDate();
					if (laLastModified.getDate() < laToday.getDate())
					{
						lvReceiptRecord.clear();
					}
					lvReceiptRecord.add(0, asReceiptRecord);
					laObjectInputStream.close();
				}
				laFileInputStream.close();
			}
			else
			{
				laFile.createNewFile();
				lvReceiptRecord = new Vector();
				lvReceiptRecord.add(asReceiptRecord);
			}
			ObjectOutputStream laObjectOutputStream =
				new ObjectOutputStream(new FileOutputStream(laFile));
			laObjectOutputStream.writeObject(lvReceiptRecord);
			laObjectOutputStream.flush();
			laObjectOutputStream.close();
		}
		// defect 6456 
		// Throw exception vs. printStackTrace()  
		catch (IOException aeIOEx)
		{
			// aeIOEx.printStackTrace();
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}
		catch (ClassNotFoundException aeCNFEx)
		{
			// aeCNFEx.printStackTrace();
			throw new RTSException(RTSException.JAVA_ERROR, aeCNFEx);
		}
		// end defect 6456 
	}
	/**
	 * Format the receipt
	 *
	 * @param  avResults Vector
	 * @return String
	 * @throws RTSException 
	 */
	private String formatReceipt(Vector avResults) throws RTSException
	{
		String lsRcptContent = "";
		String lsTransCd = "";
		try
		{
			if (caTransactionControl == null)
			{
				return null;
			}
			lsTransCd = caTransactionControl.getTransCd();
			if (lsTransCd.equals(TransCdConstant.REFUND)
				|| caTransactionControl.getTransCd().equals(
					TransCdConstant.HOTCK)
				|| lsTransCd.equals(TransCdConstant.HOTDED)
				|| lsTransCd.equals(TransCdConstant.CKREDM)
				|| lsTransCd.equals(TransCdConstant.RFCASH)
				|| lsTransCd.equals(TransCdConstant.HCKITM))
			{
				GenAccountingReceipt laGenAccRcpt =
					new GenAccountingReceipt();
				laGenAccRcpt.formatReceipt(avResults);
				lsRcptContent = laGenAccRcpt.getReceipt().toString();
			}
			else if (
				lsTransCd.equals(TransCdConstant.RGNCOL)
					|| (lsTransCd.equals(TransCdConstant.CCO)
						&& SystemProperty.isRegion()))
			{
				RegOfcColl laGenAccRcpt = new RegOfcColl();
				laGenAccRcpt.formatReceipt(avResults);
				lsRcptContent = laGenAccRcpt.getReceipt().toString();
			}
			else if (lsTransCd.equals(TransCdConstant.ADLCOL))
			{
				// defet 10023 
				ReportProperties laRptProps =
					new ReportProperties("RTS.POS.2301");
				laRptProps.initClientInfo();
				// end defect 10023

				GenTimeLagReport laGenRpt =
					new GenTimeLagReport(
						"ADDITIONAL COLLECTIONS REPORT",
						laRptProps);
				laGenRpt.formatReport(avResults);
				// defect 10023
				String lsPage = Print.getDefaultPageProps();
				// end defect 10023 
				lsRcptContent =
					lsPage + laGenRpt.getFormattedReport().toString();
				return lsRcptContent;
			}
			else if (lsTransCd.equals(TransCdConstant.CCO))
			{
				GenTitleCCOReports laGenRpt = new GenTitleCCOReports();
				laGenRpt.formatReport(avResults);
				lsRcptContent =
					laGenRpt.getFormattedReport().toString();
			}
			// defect 10491 
			else if (UtilityMethods.printsPermit(lsTransCd))
			{
				// end defect 10491 
				GenTimedPermitReceipts laGenRpt =
					new GenTimedPermitReceipts();
				laGenRpt.formatReceipt(avResults);
				lsRcptContent = laGenRpt.getReceipt().toString();
			}
			// defect 9831 
			else if (UtilityMethods.isDsabldPlcrdEvent(lsTransCd))
			{
				GenDisabledParkingCardReceipts laGenRpt =
					new GenDisabledParkingCardReceipts();
				laGenRpt.formatReceipt(avResults);
				lsRcptContent = laGenRpt.getReceipt().toString();
			}
			// end defect 9831 
			else if (lsTransCd.equals(TransCdConstant.TAWPT))
			{
				GenTempAdditionalWeightReceipts laGenRpt =
					new GenTempAdditionalWeightReceipts();
				laGenRpt.formatReceipt(avResults);
				lsRcptContent = laGenRpt.getReceipt().toString();
			}
			else if (
				lsTransCd.equals(TransCdConstant.NRIPT)
					|| lsTransCd.equals(TransCdConstant.NROPT))
			{
				GenNonResidentAgriculturePermitReceipts laGenRpt =
					new GenNonResidentAgriculturePermitReceipts();
				laGenRpt.formatReceipt(avResults);
				lsRcptContent = laGenRpt.getReceipt().toString();
			}
			else if (lsTransCd.equals(TransCdConstant.TOWP))
			{
				GenTowTruckReceipts laGenRpt =
					new GenTowTruckReceipts();
				laGenRpt.formatReceipt(avResults);
				lsRcptContent = laGenRpt.getReceipt().toString();
			}
			else if (lsTransCd.equals(TransCdConstant.VEHINQ))
			{
				if (((MFVehicleData)
							((CompleteTransactionData) avResults
					.get(0))
					.getVehicleInfo())
					.isSPRecordOnlyVehInq())
				{
					GenSpclPltApplReceipt laSpclPltApplReceipt =
						new GenSpclPltApplReceipt();
					laSpclPltApplReceipt.formatReceipt(avResults);
					lsRcptContent =
						laSpclPltApplReceipt.getReceipt().toString();
				}
				else
				{
					GenVehInquiryReceipt laGenRpt =
						new GenVehInquiryReceipt();
					laGenRpt.formatReceipt(avResults);
					lsRcptContent = laGenRpt.getReceipt().toString();
				}
			}
			else if (
				lsTransCd.equals(TransCdConstant.RENEW)
					|| lsTransCd.equals(TransCdConstant.IRENEW)
					|| lsTransCd.equals(TransCdConstant.DUPL)
					|| lsTransCd.equals(TransCdConstant.EXCH)
					|| lsTransCd.equals(TransCdConstant.REPL)
					|| lsTransCd.equals(TransCdConstant.PAWT)
					|| lsTransCd.equals(TransCdConstant.CORREG)
					|| lsTransCd.equals(TransCdConstant.DRVED))
			{
				GenRegistrationReceipts laGenRpt =
					new GenRegistrationReceipts();
				laGenRpt.formatReceipt(avResults);
				lsRcptContent = laGenRpt.getReceipt().toString();
			}
			else if (
				caTransactionControl.getTransCd().equals(
					TransCdConstant.ADLSTX))
			{
				AdditionalSalesTaxReceipt laGenRpt =
					new AdditionalSalesTaxReceipt();
				laGenRpt.formatReceipt(avResults);
				lsRcptContent = laGenRpt.getReceipt().toString();
			}
			else if (
				caTransactionControl.getEventType().equals(TTL)
					|| caTransactionControl.getEventType().equals(DTA))
			{
				GenTitleReceipt laGenRpt = new GenTitleReceipt();
				laGenRpt.formatReceipt(avResults, false);
				lsRcptContent = laGenRpt.getReceipt().toString();
			}
			// defect 10700 
			else if (
				caTransactionControl.getTransCd().equals(
					TransCdConstant.DPSPPT))
			{
				// This will be covered by permitNeeded()
			}
			// end defect 10700 
			// defect 9126/9145/9085
			// Add receipt for Special Plate Events
			else if (
				caTransactionControl.getEventType().equals(SPCLPLT))
			{
				GenSpclPltApplReceipt laSpclPltApplReceipt =
					new GenSpclPltApplReceipt();
				laSpclPltApplReceipt.formatReceipt(avResults);
				lsRcptContent =
					laSpclPltApplReceipt.getReceipt().toString();
			}
			// end defect 9126/9145/9085 
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		if (lsRcptContent.equals(""))
		{
			return null;
		}
		else
		{
			// defect 8524
			// FormFeed constant cleanup 
			return lsRcptContent + ReportConstant.FF;
			// end defect 8524
		}
	}

	/**
	 * Process sequence to complete DTA process
	 * 1) Returns false if the transaction contains no DTA info.
	 * 2) Generates the Dealer Completed Report
	 * 3) Print all receipts
	 * 4) Update Customer Sequence Number
	 * 5) Update POS information
	 * 6) Append RSPS/POS Info to each record
	 * 7) Copy RSPS_DTA.DAT to the receipt directory and rename to 
	 * 		to DLRTITLE.DAT
	 * 8) Copy DLRTITLE.DAT to the receipt directory
	 * 9) Verify the diskette is in the A:\ drive or a flashdrive unit
	 *    is in USB port  (f:\).
	 * 10) Verify the correct RSPSID and Media Number is in the drive
	 * 11) Copy RSPS_DTA.DAT to drive and rename to DLRTITLE.DAT
	 * 
	 *
	 * @param  lvTransactionData Vector
	 * @return boolean 
	 * @throws RTSException
	 */
	private boolean genDTADealerCompletedReport(Vector avTransactionData)
		throws RTSException
	{
		// defect 10290 
		// Reorganize for single return; reduce the number of writes. 
		//String lsTransCd = caTransactionControl.getTransCd();
		//boolean lbDTA = UtilityMethods.isDTA(lsTransCd);
		boolean lbDTA =
			svDTADlrTtlData != null && !svDTADlrTtlData.isEmpty();
		// end defect 10373 

		// Do not continue if !DTA 
		if (lbDTA)
		{
			// end defect 10290 

			// Initialize Vector
			Vector lvPymntInfo = new Vector();

			// Get Transaction Payment Data
			for (int i = 0; i < avTransactionData.size() - 1; i++)
			{
				TransactionCacheData laTransCacheData =
					(TransactionCacheData) avTransactionData.get(i);
				if (laTransCacheData.getObj()
					instanceof TransactionPaymentData)
				{
					TransactionPaymentData laTransPaymentData =
						(TransactionPaymentData) laTransCacheData
							.getObj();
					lvPymntInfo.add(laTransPaymentData);
				}
			}

			// ***** Get data to generate Dealer Completed Report

			// defect 10251
			// Implement ReportConstant   
			ReportProperties laRptProps =
				new ReportProperties(
					ReportConstant.DLR_COMPLETED_RPT_ID);

			laRptProps.initClientInfo();

			GenDealerTitleCompletedReport laGenRpt =
				new GenDealerTitleCompletedReport(
					ReportConstant.DLR_COMPLETED_RPT_TITLE,
					laRptProps);
			// end defect 10251

			// defect 10290 
			Vector lvData =
				UtilityMethods.getNewDTADlrTtlDataVectorWithSkip(
					svDTADlrTtlData);

			laGenRpt.formatReport(lvData, lvPymntInfo);
			// end defect 10290 

			String lsReport = laGenRpt.getFormattedReport().toString();

			String lsPageProps = Print.getDefaultPageProps();
			String lsRpt =
				lsPageProps
					+ CommonConstant.SYSTEM_LINE_SEPARATOR
					+ lsReport;

			// ***** Start process to print Dealer Completed Report
			// defect 10251 
			String lsFileName =
				UtilityMethods.saveReport(
					lsRpt,
					ReportConstant.DLR_COMPLETED_RPT_FILENAME,
					1);

			FileUtil.copyFile(
				lsFileName,
				getCompleteTransRcptName(saTransHdrData.getCustSeqNo())
					+ ReportConstant.DLR_COMPLETED_RPT_FILENAME
					+ ".RPT");

			//Add a line for dealer completed report in the RCPTLOG file

			// defect 10844 
			// Use standard updateRcptLog 
			updateRcptLog(
				ReportConstant.DLR_COMPLETED_RPT_FILENAME + ".RPT",
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				CommonConstant.STR_SPACE_EMPTY,
				ReportConstant.DLR_COMPLETED_RPT_TITLE,
				CommonConstant.STR_SPACE_EMPTY,
				ReportConstant.DLR_COMPLETED_RPT_FILENAME,
				CommonConstant.STR_SPACE_EMPTY);
			// end defect 10844

			// Print immediate on, print dlrcompl.rpt for rcptlog file, else
			//	print from variable defined location, (3865)
			if (SystemProperty.getPrintImmediateIndi() == 0)
			{
				printAllReceipts();
			}
			else
			{
				// defect 10251 
				Print laPrint = new Print();
				laPrint.sendToPrinter(lsFileName);
				// end defect 10251
			}

			// ***** Set Customer Sequence Number
			//	setLastCSN() throws FileNotFoundException and IOException
			setLastCSN();

			// defect 10290 
			// We have as a static vector. 
			// Revisit w/ set-aside. 
			// ***** RSPS - Update and write RSPS data on the external media
			//			to DLRTITLE.DAT
			//		// PCR 34
			//		Vector lvDealerRecords = new Vector();
			//
			//		try
			//		{
			//			lvDealerRecords =
			//				(Vector) deserializeObject(CommonClientBusiness
			//					.DEALER_INPUT_FILE);
			//			// Note: DEALER_INPUT_FILE is DLRDISK.DAT on the hard
			//			//	drive containing the serialize DTA objects.
			//		}
			//		catch (RTSException aeRTSEx)
			//		{
			//			// ignore exceptions and don't update diskette
			//		}

			// defect 8227
			//	Clean up diskette processing.
			//	Update the D:\RTS\RTSAPPL\RSPS_DTA.DAT file.
			//	When completed, copy files to appropriate locations.
			//		A:\DLRTITLE.DAT
			//		D:\RTS\RCPT\CSN####\DLRTITLE.ORG
			//		D:\RTS\RCPT\CSN####\RSPS_DTA.DAT

			// defect 7738
			//	Write back to the diskette ONLY FOR RSPS DISKETTES
			// 	If the data is from RSPS then update the RSPS data 
			//	only.
			//	Retain the original dealer data.
			//
			// Determine if RSPSId exist.
			//	If it does then update the DTA support files.
			// If RSPSId does not exist, return to the main menu.
			//	(Note: All diskettes that came from a RSPS workstation will 
			//			have a RSPS Id.)

			DealerTitleData laFirstDlrTtlData =
				(DealerTitleData) svDTADlrTtlData.elementAt(0);

			if (!UtilityMethods.isEmpty(laFirstDlrTtlData.getRSPSId()))
			{
				//boolean lbDlrFileExist = true;

				try
				{
					//	for (int liIndex = 0;
					//		liIndex < svDTADlrTtlData.size();
					//		liIndex++)
					//	{
					//		DealerTitleData laDlrTtlData =
					//			(DealerTitleData) svDTADlrTtlData.get(liIndex);
					//		String lsTransCd = laDlrTtlData.getTransCd();

					// Append RSPS data to Dealer Original Data
					// Process diskette record that has not rejected and has
					//	not been previously processed through POS.
					//	(Note: isProcessed(), is set in 
					//		FrmDealerTitleTransactionDTA008.setData()
					//		isProcessed(), should always be true.)
					//
					// If Keyboard, rejected or has not been processed 
					//	through POS, get next record
					// defect 10290 
					//					if (laDlrTtlData.isKeyBoardEntry()
					//						|| laDlrTtlData.isRecordRejected())
					//						//|| !laDlrTtlData.isProcessed())
					//					{
					//						continue;
					//					}
					// end defect 10290 
					// Ensure diskette is in the A:\\ drive, DLRTITLE.DAT 
					// If not then return to main menu.
					//					else if (
					//						!TitleClientBusiness.ensureDiskInADrive(
					//							lsTransCd))
					//					{
					//						lbDlrFileExist = false;
					//						break;
					//					}
					//	laDlrTtlData.setPOSProcsIndi(true);

					// Verify the RSPS ID and the RSPS Disk Sequence 
					//	Number match the processed record (RSPS_DTA.DAT)
					//	verifyOrigMatchPrcsDTA() checks to ensure diskette 
					//	in drive.
					//laDlrTtlData.setTransCd(lsTransCd);
					//					TitleClientBusiness.verifyOrigMatchPrcsDTA(
					//						laDlrTtlData);

					// defect 10290 
					// POS will not print from RSPS 
					//	if (SystemProperty.getPrintImmediateIndi() == 0)
					//	{
					//		// defect 8217
					//		//	Reset getters to match DealerTitleData's 
					//		// Total count of prints at POS
					//		//	(Note: PosPrntInvQty should always be zero if 
					//		//		   record is RSPS)
					//		int liPOSPrintNum =
					//			laDlrTtlData.getPosPrntInvQty();
					//			
					//		if (laDlrTtlData.isToBePrinted())
					//		{
					//			laDlrTtlData.setPosPrntInvQty(
					//                 liPOSPrintNum + 1);
					//		}
					//		// end defect 8217
					//	}
					// end defect 10290 

					// Update RSPS Data on Orig DTA record
					// defect 8227
					// Update RSPS data to Dealer Original Data
					//					laDlrTtlData =
					//						TitleClientBusiness
					//							.updateRSPSDataOnOrigDTARecord(
					//							laDlrTtlData,
					//							TitleClientBusiness.RSPS_DTA,
					//							liIndex);
					// end defect 8227
					// defect 7898
					// 	Move this process from outside for loop.
					//	Each record will be written to it's proper 
					//	location.
					//	Update record in D:\RTS\RTSAPPL\RSPS_DTA.DAT
					// defect 10075
					// Refactor\rename DiskParser to MediaParser
					//					MediaParser.updateRecord(
					//						new File(TitleClientBusiness.RSPS_DTA),
					//						laDlrTtlData,
					//						TitleClientBusiness.BACK_SLASH,
					//						liIndex);
					// end defect 10075
					// end defect 7898
					// end defect 8309
					// end defect 7738
					//				}

					Vector lvToDisk = new Vector();

					for (int liIndex = 0;
						liIndex < svDTAOrigDlrTtlData.size();
						liIndex++)
					{
						DealerTitleData laOrigDlrTtlData =
							(
								DealerTitleData) svDTAOrigDlrTtlData
									.elementAt(
								liIndex);

						DealerTitleData laUpdtDlrTtlData =
							(
								DealerTitleData) svDTADlrTtlData
									.elementAt(
								liIndex);

						laOrigDlrTtlData.setPOSProcsIndi(
							!laUpdtDlrTtlData.isRecordRejected());

						String lsDlrTtlData =
							MediaParser.convertParseableToString(
								laOrigDlrTtlData,
								TitleClientBusiness.BACK_SLASH);

						lvToDisk.add(lsDlrTtlData);
					}

					MediaParser.updateRecord(
						new File(TitleClientBusiness.RSPS_DTA),
						lvToDisk);

					//	laDlrTtlData.setPOSProcsIndi(true);

					// Verify the RSPS ID and the RSPS Disk Sequence 
					//	Number match the processed record (RSPS_DTA.DAT)
					//	verifyOrigMatchPrcsDTA() checks to ensure diskette 
					//	in drive.
					//laDlrTtlData.setTransCd(lsTransCd);
					//					TitleClientBusiness.verifyOrigMatchPrcsDTA(
					//						laDlrTtlData);

					// Complete the process writing the files to:
					//	RSPS_DTA to receipt directory
					//	D:\RTS\RTSAPPL\DLRTITLE.DAT to receipt directory
					//		(Save as DLRTITLE.ORG)
					//	RSPS_DTA to A:\DLRTITLE.DAT
					// defect 8309
					//	Do not throw exceptions. 
					//	Log all exceptions.
					// Copy RSPS_DTA.DAT to the receipt directory

					FileUtil.copyFile(
						TitleClientBusiness.RSPS_DTA,
						getCompleteTransRcptName(
							saTransHdrData.getCustSeqNo())
							+ TitleClientBusiness.RSPS_DTA_FILE);

					// Copy DLRTITLE.DAT to the receipt directory
					//	and save as DLRTITLE.ORG
					FileUtil.copyFile(
						TitleClientBusiness.DLRTITLEDAT,
						getCompleteTransRcptName(
							saTransHdrData.getCustSeqNo())
							+ TitleClientBusiness.DLRTITLE_ORG);

					// Copy RSPS_DTA.DAT to A:\\DLRTITLE.DAT if diskette is
					//	in the A:\\ drive.
					//	For Dealer/Keyboard trans combinations , all dealer 
					//	records are processed first then Keyboard
					//	Only the dealer records are written to RSPS_DTA file.
					// defect 8227
					//	Add check to ensure diskette in in the A:\\ drive.
					// defect 10075
					// Use new variable indicating either diskette or flashdrive
					//FileUtil.copyFile(
					//	TitleClientBusiness.RSPS_DTA,
					//	TitleClientBusiness.DTA_FROM_DISKETTE);

					// defect 10373
					// Use TransCdConstant vs. local variable 
					if (TitleClientBusiness
						.ensureDiskInADrive(TransCdConstant.DTAORD))
					{
						// end defect 10373 
						TitleClientBusiness.verifyOrigMatchPrcsDTA(
							laFirstDlrTtlData);

						FileUtil.copyFile(
							TitleClientBusiness.RSPS_DTA,
							SystemProperty.getExternalMedia());
						// end defect 10075
					}
					// end defect 8227
				}
				// end defect 8309

				// defect 8309 replace by 8912
				//	Catch all exceptions then continuing processing
				//
				// defect 8918
				//	Display message error if IOException or RTSException 
				//	occurs when updating RSPS data on the diskette.
				//	Log stack trace
				//			catch (IOException aeIOExc)
				//			{
				//				handleRSPSExceptions(aeIOExc);
				//			}
				catch (RTSException aeRTSExc)
				{
					handleRSPSExceptions(aeRTSExc);
				}
				// end defect 8918
				catch (Exception aeExc)
				{
					RTSException leRTSEx =
						new RTSException(
							RTSException.JAVA_ERROR,
							aeExc);
					leRTSEx.displayError();
				}
			}
		}
		return lbDTA;
	}

	/**
	 * The Payment PMT004 is active but not visible.
	 * Use RTSException(String, Exception) to retain
	 * focus on the message box.
	 * Write error to Log
	 * 
	 * @param aaObject	Exception of some type
	 */
	private void handleRSPSExceptions(Object aaObject)
	{
		RTSException leRTSEx = new RTSException();
		// Log error message
		Log.write(Log.START_END, null, ErrorsConstant.RSPSUPDTERROR);

		if (aaObject instanceof IOException)
		{
			leRTSEx =
				new RTSException(
					RTSException.FAILURE_MESSAGE,
					(IOException) aaObject);
		}
		if (aaObject instanceof RTSException)
		{
			leRTSEx =
				new RTSException(
					RTSException.FAILURE_MESSAGE,
					new IOException());
		}
		leRTSEx.setMessage(ErrorsConstant.RSPSUPDTERROR);
		// Check to ensure the app is NOT at the server before 
		//	displayError() is called.
		if (!Comm.isServer())
		{
			leRTSEx.displayError();
		}

	}
	/**
	 * Return svCacheQueue NEW
	 */
	public static Vector getCacheQueue()
	{
		return svCacheQueue;
	}
	/**
	 * Returns the name of the receipt directory path for the customer
	 * sequence number provided.
	 *
	 * @param  aiCustSeqNo int
	 * @return String 
	 */
	public static String getCompleteTransRcptName(int aiCustSeqNo)
	{
		String lsCustSeqNo =
			UtilityMethods.addPadding(
				String.valueOf(aiCustSeqNo),
				4,
				"0");
		String lsReceiptRootDir = SystemProperty.getReceiptsDirectory();
		String lsLocationForReceipt =
			lsReceiptRootDir + CSN + lsCustSeqNo + BACK_SLASH;
		return lsLocationForReceipt;
	}
	/**
	 * Get the Crediting Remaining of the current set of Transactions
	 * 
	 * @return Dollar 
	 */
	public static Dollar getCreditRemaining()
	{
		return saCreditRemaining;
	}
	/**
	 * Get the CumulativeTransIndi for the current set of transactions
	 * 
	 * @return int
	 */
	public static int getCumulativeTransIndi()
	{
		return siCumulativeTransIndi;
	}
	/**
	 * Get the customer name of the current transaction
	 * 
	 * @return String 
	 */
	public static String getCustName()
	{
		if (saTransHdrData != null)
		{
			return saTransHdrData.getTransName();
		}
		else
		{
			return ssCustName;
		}
	}

	/** 
	 * Get DTA DealerData
	 * 
	 * @return
	 */
	public static DealerData getDTADealerData()
	{
		return saDTADealerData;
	}
	/**
	 * Set value of DealerData
	 * 
	 * @param aaDealerData
	 */
	public static void setDTADealerData(DealerData aaDTADealerData)
	{
		saDTADealerData = aaDTADealerData;
	}

	/** 
	 * Set DTA DealerId
	 * 
	 * @param aiDTADealerId
	 */
	public static void setDTADealerId(int aiDTADealerId)
	{
		siDTADealerId = aiDTADealerId;
	}

	/** 
	 * Get DTA DealerId
	 * 
	 * @return int 
	 */
	public static int getDTADealerId()
	{
		return siDTADealerId;
	}

	/** 
	 * Get DTA DlrTtlData Vector
	 * 
	 * @return Vector 
	 */
	public static Vector getDTADlrTtlData()
	{
		return svDTADlrTtlData;
	}

	/**
	 * Set value of Current Index into DTA DlrTtlData Vector
	 * 
	 * @param aiDTADlrTtlDataIndex
	 */
	public static void setDTADlrTtlDataIndex(int aiDTADlrTtlDataIndex)
	{
		siDTADlrTtlDataIndex = aiDTADlrTtlDataIndex;
	}

	/**
	 * Get value of Current Index into DTA DlrTtlData Vector
	 * 
	 */
	public static int getDTADlrTtlDataIndex()
	{
		return siDTADlrTtlDataIndex;
	}

	/**
	 * Return the padded string value of CustSeqNo 
	 * 
	 * @return String
	 */
	private static String getPaddedCustSeqNo()
	{
		return UtilityMethods.addPadding(
			String.valueOf(saTransHdrData.getCustSeqNo()),
			4,
			"0");
	}
	/**
	 * Get Payment Vector
	 * 
	 * @param  avTransPaymentData TransactionPaymentData
	 * @param  aaTransactionHeaderData TransactionHeaderData
	 * @return Vector
	 * @throws RTSException 
	 */
	private Vector getPaymentVector(
		Vector avTransPaymentData,
		TransactionHeaderData aaTransHdrData)
		throws RTSException
	{
		try
		{
			Log.write(Log.DEBUG, aaTransHdrData, " Begin endTrans");
			// PROC Write a $0.00 payment record under certain conditions
			CompleteTransactionData laCompTransData = null;
			if (avTransPaymentData != null)
			{
				if (avTransPaymentData.get(0)
					instanceof CompleteTransactionData)
				{
					laCompTransData =
						(
							CompleteTransactionData) avTransPaymentData
								.get(
							0);
					avTransPaymentData.remove(laCompTransData);
				}
				if (laCompTransData != null)
				{
					//Check if internet renewal
					if (laCompTransData.getTransCode() != null
						&& laCompTransData.getTransCode().equals(
							TransCdConstant.IRENEW))
					{
						//do not add any payment record
					}
					else
					{
						// PROC Write a $0.00 payment record under certain conditions
						if (caTransactionControl == null)
						{
							String lsTransCd =
								laCompTransData.getTransCode();
							if (lsTransCd == null)
							{
								throw new RTSException(
									RTSException.FAILURE_MESSAGE,
									"Transcode is null",
									"ERROR");
							}
							if (!shTransactionControl
								.containsKey(lsTransCd))
							{
								throw new RTSException(
									RTSException.FAILURE_MESSAGE,
									"Unable to locate Control Entry for "
										+ lsTransCd,
									"S608 Control Logic Error");
							}
							caTransactionControl =
								(
									TransactionControl) shTransactionControl
										.get(
									lsTransCd);
						}
						if (avTransPaymentData.size() == 0
							&& caTransactionControl.getCustomer()
								== CUSTOMER)
						{
							//if Customer has funds
							if (laCompTransData.getRegFeesData()
								!= null
								&& laCompTransData
									.getRegFeesData()
									.getVectFees()
									!= null
								&& laCompTransData
									.getRegFeesData()
									.getVectFees()
									.size()
									> 0)
							{
								TransactionPaymentData laTransPaymentData =
									new TransactionPaymentData();
								laTransPaymentData.setPymntTypeCd(
									FrmPaymentPMT001.CASH);
								laTransPaymentData.setPymntType(
									FrmPaymentPMT001.COMBO_CASH);
								//depending on transcd, a different amount will be written
								if (laCompTransData.getTransCode()
									!= null
									&& laCompTransData
										.getTransCode()
										.equals(
										TransCdConstant.RFCASH))
								{
									laTransPaymentData.setPymntTypeAmt(
										Transaction
											.getRunningSubtotal());
								}
								else
								{
									laTransPaymentData.setPymntTypeAmt(
										new Dollar("0.00"));
								}
								avTransPaymentData.addElement(
									laTransPaymentData);
							}
						}
					}
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.TR_ERROR, aeRTSEx);
			throw leRTSEx;
		}
		finally
		{
			Log.write(Log.DEBUG, aaTransHdrData, " End endTrans");
		}
		return avTransPaymentData;
	}
	/**
	 * Get the current receipt directory
	 * 
	 * @return String
	 */
	public static String getRcptDirForCust()
	{
		return ssRcptDirForCust;
	}
	/**
	 * Get the current rcptlog
	 * 
	 * @return String
	 */
	private static String getRcptLog()
	{
		return ssRcptLogFile;
	}

	/**
	 * Get the next receipt number for multiple receipts.
	 *
	 * @param asFileName String
	 * @param asExtension String
	 * @return int
	 */
	private int getReceiptFileNumber(
		String asFileName,
		String asExtension)
	{
		// return value for file number
		int liNumberOfFiles = 0;
		// get the directory of files for this customer
		File laRcptFilesDir = new File(ssRcptDirForCust);
		File[] larrRcptFiles = laRcptFilesDir.listFiles();
		// Check for any files of the same name
		for (int i = 0;
			larrRcptFiles != null && i < larrRcptFiles.length;
			i++)
		{
			String lsFileName = larrRcptFiles[i].getName();
			if (lsFileName.startsWith(asFileName)
				&& lsFileName.endsWith(asExtension))
			{
				liNumberOfFiles++;
			}
			if (lsFileName.startsWith(asFileName)
				&& lsFileName.endsWith("B" + asExtension))
			{
				liNumberOfFiles--;
			}
		}
		// return number of files found
		return liNumberOfFiles;
	}
	/**
	 * Get the current Subtotal for the Transaction
	 * 
	 * @return Dollar
	 */
	public static Dollar getRunningSubtotal()
	{
		if (saRunningSubtotal == null)
		{
			setRunningSubtotal(new Dollar(0.0));
		}
		return saRunningSubtotal;
	}

	/**
	 * Get the TransactionHeader for current Transaction Set 
	 * 
	 * @return TransactionHeaderData 
	 */
	public static TransactionHeaderData getTransactionHeaderData()
	{
		return saTransHdrData;
	}
	/**
	 * Used by receipts to get the list of TransactionId in the current 
	 * Transaction context.
	 *
	 * @return Vector 
	 */
	public static Vector getTransIdList()
	{
		return svTransIdList;
	}
	/**
	 * 
	 * Get the transaction control table 
	 * 
	 * @return Hashtable
	 */
	public static Hashtable getTransactionControl()
	{
		return shTransactionControl;
	}
	/**
	 * Get the Transtime for the current transaction as hhmmss
	 * 
	 * @return TransTime 
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}
	/**
	 * Get the TransId
	 * 
	 * @return String  
	 */
	private String getTransId()
	{
		String csTransId = new String();
		//TODO Improve! 
		if (saTransHdrData != null)
		{
			csTransId = getTransId(saTransHdrData);
		}
		return csTransId;
	}
	/**
	 * Get the TransId
	 * 
	 * @return String  
	 */
	private String getTransId(TransactionHeaderData aaTransHdrData)
	{
		return UtilityMethods.addPadding(
			new String[] {
				String.valueOf(aaTransHdrData.getOfcIssuanceNo()),
				String.valueOf(aaTransHdrData.getTransWsId()),
				String.valueOf(aaTransHdrData.getTransAMDate()),
				String.valueOf(ciTransTime)},
			new int[] { 3, 3, 5, 6 },
			"0");
	}
	/**
	 * To indicate whether Cancel button should be enabled or not.
	 *
	 * @return boolean 
	 */
	public static boolean isCancelTrans()
	{
		return sbAllowCancelTrans;
	}
	/**
	 * Indicate if the transaction is a customer or a backoffice 
	 * transaction
	 * 
	 * @return boolean  
	 */
	public static boolean isCustomer()
	{
		return sbCustomer;
	}

	/**
	 * Check if fees is incurred
	 * 
	 * @param aaCompTransData CompleteTransactionData
	 * @return boolean
	 */
	private boolean doFeesExist(CompleteTransactionData aaCompTransData)
	{
		boolean lbFeesIncurred = true;
		if ((aaCompTransData.getRegFeesData() == null)
			|| (aaCompTransData.getRegFeesData().getVectFees() == null)
			|| (aaCompTransData.getRegFeesData().getVectFees().size()
				== 0))
		{
			lbFeesIncurred = false;
		}
		return lbFeesIncurred;
	}

	/**
	 * Indicate whether the trans is a restored setaside transaction.
	 * 
	 * @return boolean 
	 */
	private static boolean isRestoredSetAside()
	{
		return sbRestoredSetAside;
	}
	/**
	 * Check if it is a special registration
	 *
	 * @param aiRegClassCd int
	 * @return boolean
	 */
	private boolean isSpecialRegistration(int aiRegClassCd)
	{
		boolean lbSpecialRegistration = false;
		// Test if HQ && Special Reg Section && !"EXEMPT"  

		// defect 8900
		// Use CommonFeesCache to test for Standard Exempt
		if (SystemProperty.isHQ()
			&& (SystemProperty.getOfficeIssuanceNo()
				== SPECIAL_REG_SECTION_OFISSUANCE_NO)
			&& !CommonFeesCache.isStandardExempt(aiRegClassCd))
		{
			lbSpecialRegistration = true;
		}
		// end defect 8900

		return lbSpecialRegistration;
	}
	/**
	 * Check if it is a special registration
	 *
	 * @param aaCompTransData
	 * @return boolean
	 */
	private boolean isSpecialPlateTransaction(CompleteTransactionData aaCompTransData)
	{
		boolean lbSpclPltTrans = false;
		// defect 10734 
		if (caTransactionControl
			.getTransCd()
			.equals(TransCdConstant.SBRNW)
			|| caTransactionControl.getTransCd().equals(
				TransCdConstant.WRENEW))
		{
			// end defect 10734 
			SubcontractorRenewalData laSubconRenewalData =
				aaCompTransData
					.getSubcontractorRenewalCacheData()
					.getTempSubconRenewalData();
			if (laSubconRenewalData.getSpclPltIndi() == 1)
			{
				lbSpclPltTrans = true;
			}
		}
		else
		{
			MFVehicleData laMFVehData =
				(MFVehicleData) aaCompTransData.getVehicleInfo();
			if (laMFVehData != null
				&& laMFVehData.getSpclPltRegisData() != null)
			{
				lbSpclPltTrans = true;
				// defect 9912
				// Remove reference to RegisData.setOrgNo()
				//	laMFVehData.getRegData().setOrgNo(
				//		laMFVehData.getSpclPltRegisData().getOrgNo());
				// end defect 9912
			}
		}
		return lbSpclPltTrans;
	}
	/**
	 * Check if the transaction is being treated as a backoffice 
	 * transaction
	 * 
	 * @param aaTransactionControl TransactionControl
	 * @return boolean
	 * @throws RTSException
	 */
	private boolean isTreatedAsBackOfficeTransaction(
		TransactionControl aaTransactionControl,
		CompleteTransactionData aaCompTransData)
		throws RTSException
	{
		boolean lbBackOffice = false;
		String lsTransCd = aaTransactionControl.getTransCd();
		// Transaction Control - Always Back Office 
		if (aaTransactionControl.getCustomer() == (BACK_OFFICE))
		{
			lbBackOffice = true;
		} // Back Office: 
		// No Charge ViewOnly VEHINQ
		// ADDR, RNR part of More Trans
		else if (
			saTransHdrData == null
				|| (saTransHdrData.getTransName() != null
					&& saTransHdrData.getTransName().equals(
						BACK_OFFICE_TRANSNAME)))
		{
			if ((lsTransCd.equals(TransCdConstant.VEHINQ)
				&& aaCompTransData.getPrintOptions()
					== VehicleInquiryData.VIEW_ONLY)
				|| aaTransactionControl.getTransCd().equals(
					TransCdConstant.ADDR)
				|| aaTransactionControl.getTransCd().equals(
					TransCdConstant.RNR))
			{
				lbBackOffice = true;
			}
		}

		return lbBackOffice;
	}

	/** 
	 * Return True if Mfg New Plate No on Replace. Else, false. 
	 * 
	 * @param avVector
	 * @param aaCompTransData
	 * @param aaTransHdr
	 * @throws RTSException
	 */
	private boolean isMfgNewSpclPltOnReplace(
		CompleteTransactionData aaComplTransData,
		String asTransCd)
	{
		boolean lbReplMfgNewSpclPlt = false;
		// Determine if voided event was a Replacement where
		// a new plate no was manufactured.  
		if (asTransCd.equals(TransCdConstant.REPL))
		{
			if (aaComplTransData.getVehicleInfo().isSpclPlt())
			{
				SpecialPlatesRegisData laSpclPltRegisData =
					aaComplTransData
						.getVehicleInfo()
						.getSpclPltRegisData();
				laSpclPltRegisData.initWhereNull();
				lbReplMfgNewSpclPlt =
					aaComplTransData.getVehicleInfo().getRegData()
						!= null
						&& aaComplTransData
							.getVehicleInfo()
							.getRegData()
							.getRegPltNo()
							!= null
						&& !aaComplTransData
							.getVehicleInfo()
							.getRegData()
							.getRegPltNo()
							.equals(
							laSpclPltRegisData.getRegPltNo())
						&& laSpclPltRegisData.getMFGStatusCd().equals(
							SpecialPlatesConstant
								.MANUFACTURE_MFGSTATUSCD);
			}
		}

		return lbReplMfgNewSpclPlt;
	}

	/**
	 * Populate RTS_DSABLD_PLCRD_TRANS 
	 * 
	 * @param avVector
	 * @param aaCompTransData
	 * @param aaTransHdr
	 * @throws RTSException
	 */
	public void populateDsabldPlcrdTrans(
		Vector avVector,
		CompleteTransactionData aaCompTransData,
		TransactionHeaderData aaTransHdrData)
		throws RTSException
	{
		if (UtilityMethods
			.isDsabldPlcrdEvent(caTransactionControl.getTransCd())
			&& aaCompTransData.getTimedPermitData() != null
			&& aaCompTransData.getTimedPermitData().getDPCustData()
				!= null)
		{
			DisabledPlacardTransactionData laDPTransData =
				new DisabledPlacardTransactionData();
			laDPTransData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
			laDPTransData.setOfcIssuanceNo(
				aaTransHdrData.getOfcIssuanceNo());
			laDPTransData.setSubstaId(aaTransHdrData.getSubstaId());
			laDPTransData.setTransAMDate(
				aaTransHdrData.getTransAMDate());
			laDPTransData.setTransWsId(aaTransHdrData.getTransWsId());
			laDPTransData.setTransCd(caTransactionControl.getTransCd());
			laDPTransData.setTransTime(ciTransTime);
			// defect 10505
			laDPTransData.setTransId(getTransId(aaTransHdrData));
			// end defect 10505 
			laDPTransData.setTransEmpId(aaTransHdrData.getTransEmpId());
			DisabledPlacardCustomerData laDPCustData =
				aaCompTransData.getTimedPermitData().getDPCustData();
			laDPTransData.setCustIdntyNo(laDPCustData.getCustIdntyNo());
			laDPTransData.setDsabldPlcrd(laDPCustData.getDsabldPlcrd());
			// defect 
			if (!caTransactionControl
				.getEventType()
				.equals(TransCdConstant.DEL_DP_EVENT_TYPE) && !caTransactionControl.getEventType()
				.equals(TransCdConstant.REI_DP_EVENT_TYPE))
			{
				Vector lvVector = laDPCustData.getDsabldPlcrd();
				int liInvIndex = 0;
				for (int i = 0; i < lvVector.size(); i++)
				{
					DisabledPlacardData laDPData =
						(DisabledPlacardData) lvVector.elementAt(i);
					if (laDPData.getTransTypeCd()
						== MiscellaneousRegConstant.DP_ADD_TRANS_TYPE_CD)
					{
						InventoryAllocationData laInvData =
							(InventoryAllocationData) aaCompTransData
								.getAllocInvItms()
								.elementAt(
								liInvIndex);
						liInvIndex = liInvIndex + 1;
						laDPData.setInvItmNo(laInvData.getInvItmNo());
					}
				}
			}

			TransactionCacheData laTransCacheData =
				new TransactionCacheData();
			laTransCacheData.setProcName(TransactionCacheData.INSERT);
			laTransCacheData.setObj(laDPTransData);
			avVector.addElement(laTransCacheData);
		}
		else if (
			aaCompTransData.getTransCode().equals(
				TransCdConstant.VOID))
		{
			DisabledPlacardTransactionData laDPTransData =
				new DisabledPlacardTransactionData();
			laDPTransData.setCustSeqNo(
				aaCompTransData.getVoidCustSeqNo());
			laDPTransData.setOfcIssuanceNo(
				aaCompTransData.getVoidOfcIssuanceNo());
			laDPTransData.setSubstaId(
				aaCompTransData.getVoidSubstaId());
			laDPTransData.setTransAMDate(
				aaCompTransData.getVoidTransAMDate());
			laDPTransData.setTransWsId(
				aaCompTransData.getVoidTransWsId());
			laDPTransData.setTransCd(caTransactionControl.getTransCd());
			laDPTransData.setTransTime(
				aaCompTransData.getVoidTransTime());
			laDPTransData.setVoidedTransIndi(1);
			laDPTransData.setVoidTransId(getTransId(aaTransHdrData));
			laDPTransData.setVoidTransEmpId(
				SystemProperty.getCurrentEmpId());
			TransactionCacheData laTransCacheData =
				new TransactionCacheData();
			laTransCacheData.setProcName(TransactionCacheData.VOID);
			laTransCacheData.setObj(laDPTransData);
			avVector.addElement(laTransCacheData);
		}
	}
	/**
	 * Populate RTS_ETTL_HSTRY
	 * 
	 * @param avVector
	 * @param aaCompTransData
	 * @param aaTransHdr
	 * @throws RTSException
	 */
	public void populateETtlHstry(
		Vector avVector,
		CompleteTransactionData aaCompTransData,
		TransactionHeaderData aaTransHdr)
		throws RTSException
	{
		ElectronicTitleHistoryData laETtlData =
			aaCompTransData.getETtlHstryData(aaTransHdr, ciTransTime);
		if (laETtlData != null)
		{
			TransactionCacheData laTransCacheData =
				new TransactionCacheData();
			if (caTransactionControl.getTransCd().startsWith("VOID"))
			{
				laTransCacheData.setProcName(TransactionCacheData.VOID);
			}
			else
			{
				laTransCacheData.setProcName(
					TransactionCacheData.INSERT);
			}
			laTransCacheData.setObj(laETtlData);
			avVector.addElement(laTransCacheData);
		} // If laETtlData !=null
	}
	/**
	  * Populate RTS_EXMPT_AUDIT
	  *
	  * @param avVector Vector to which RTS_Exmpt_Audit will be added
	  * @param aaCompTransData CompleteTransactionData
	  * @param aaTransHdr TransactionHeaderData
	  * @throws RTSException
	  */
	public void populateExemptAudit(
		Vector avVector,
		CompleteTransactionData aaCompTransData,
		TransactionHeaderData aaTransHdr)
		throws RTSException
	{
		ExemptAuditData laExemptAuditData =
			aaCompTransData.getExemptAuditData(aaTransHdr, ciTransTime);
		if (laExemptAuditData != null)
		{
			//	Add TransactionData to Vector
			TransactionCacheData laTransCacheData =
				new TransactionCacheData();
			if (caTransactionControl.getTransCd().startsWith("VOID"))
			{
				laTransCacheData.setProcName(TransactionCacheData.VOID);
			}
			else
			{
				laTransCacheData.setProcName(
					TransactionCacheData.INSERT);
			}
			laTransCacheData.setObj(laExemptAuditData);
			avVector.addElement(laTransCacheData);
		} // If ExemptAuditData !=null
	}
	/**
	 * Populate Permit Trans - RTS_PRMT_TRANS 
	 * 
	 * @param avVector
	 * @param aaCompTransData
	 * @param aaTransHdrData
	 * @throws RTSException
	 */
	public void populatePrmtTrans(
		Vector avVector,
		CompleteTransactionData aaCompTransData,
		TransactionHeaderData aaTransHdrData)
		throws RTSException
	{
		if (aaCompTransData.getTimedPermitData() instanceof PermitData)
		{
			String lsTransCd = aaCompTransData.getTransCode();

			if (UtilityMethods.printsPermit(lsTransCd)
				|| lsTransCd.startsWith(TransCdConstant.VOID))
			{
				PermitData laPrmtData =
					(PermitData) aaCompTransData.getTimedPermitData();
				PermitTransactionData laPrmtTransData =
					new PermitTransactionData(laPrmtData);
				laPrmtTransData.setCustSeqNo(
					aaTransHdrData.getCustSeqNo());
				laPrmtTransData.setOfcIssuanceNo(
					aaTransHdrData.getOfcIssuanceNo());
				laPrmtTransData.setSubstaId(
					aaTransHdrData.getSubstaId());
				laPrmtTransData.setTransAMDate(
					aaTransHdrData.getTransAMDate());
				laPrmtTransData.setTransWsId(
					aaTransHdrData.getTransWsId());
				laPrmtTransData.setTransCd(
					caTransactionControl.getTransCd());
				laPrmtTransData.setTransTime(ciTransTime);
				// defect 10505 
				laPrmtTransData.setTransId(getTransId(aaTransHdrData));
				// end defect 10505

				// Only set for Permit Application  
				if (UtilityMethods.isPermitApplication(lsTransCd))
				{
					laPrmtTransData.setPrmtIssuanceId(getTransId());
					laPrmtData.setPrmtIssuanceId(getTransId());
				}
				else if (lsTransCd.equals(TransCdConstant.MODPT))
				{
					laPrmtData.setAuditTrailTransId(getTransId());
				}

				TransactionCacheData laTransCacheData =
					new TransactionCacheData();
				laTransCacheData.setProcName(
					TransactionCacheData.INSERT);
				laTransCacheData.setObj(laPrmtTransData);
				avVector.addElement(laTransCacheData);
			}

			// defect 10844 
			if (aaCompTransData
				.getTransCode()
				.equals(TransCdConstant.MODPT))
			{
				populateModPrmtTransHstry(
					avVector,
					aaCompTransData,
					aaTransHdrData);
			}
			// end defect 10844 
		}
	}

	/**
	 * Populate Permit Trans - RTS_PRMT_TRANS 
	 * 
	 * @param avVector
	 * @param aaCompTransData
	 * @param aaTransHdrData
	 * @throws RTSException
	 */
	public void populateModPrmtTransHstry(
		Vector avVector,
		CompleteTransactionData aaCTData,
		TransactionHeaderData aaTransHdrData)
		throws RTSException
	{
		ModifyPermitTransactionHistoryData laModPrmtTransHstryData =
			new ModifyPermitTransactionHistoryData(
				(PermitData) aaCTData.getTimedPermitData());
		{
			TransactionCacheData laTransCacheData =
				new TransactionCacheData();

			if (caTransactionControl
				.getTransCd()
				.startsWith(TransCdConstant.VOID))
			{
				laModPrmtTransHstryData.setOfcIssuanceNo(
					aaCTData.getVoidOfcIssuanceNo());
				laModPrmtTransHstryData.setSubstaId(
					aaCTData.getVoidSubstaId());
				laModPrmtTransHstryData.setTransWsId(
					aaCTData.getVoidTransWsId());
				laModPrmtTransHstryData.setTransAMDate(
					aaCTData.getVoidTransAMDate());
				laModPrmtTransHstryData.setTransTime(
					aaCTData.getVoidTransTime());
				laModPrmtTransHstryData.setCacheTransAMDate(
					aaTransHdrData.getTransAMDate());
				laModPrmtTransHstryData.setCacheTransAMDate(
					ciTransTime);
				laTransCacheData.setProcName(TransactionCacheData.VOID);
			}
			else
			{
				laModPrmtTransHstryData.setOfcIssuanceNo(
					aaTransHdrData.getOfcIssuanceNo());
				laModPrmtTransHstryData.setSubstaId(
					aaTransHdrData.getSubstaId());
				laModPrmtTransHstryData.setTransAMDate(
					aaTransHdrData.getTransAMDate());
				laModPrmtTransHstryData.setTransWsId(
					aaTransHdrData.getTransWsId());
				laModPrmtTransHstryData.setCustSeqNo(
					aaTransHdrData.getCustSeqNo());
				laModPrmtTransHstryData.setTransTime(ciTransTime);
				laModPrmtTransHstryData.setTransId(
					getTransId(aaTransHdrData));
				laModPrmtTransHstryData.setTransCd(
					caTransactionControl.getTransCd());
				laModPrmtTransHstryData.setTransEmpId(
					aaTransHdrData.getTransEmpId());

				laTransCacheData.setProcName(
					TransactionCacheData.INSERT);
			}
			laTransCacheData.setObj(laModPrmtTransHstryData);
			avVector.addElement(laTransCacheData);
		}
	}

	/**
	 * Set up the Vector to populate RTS_SR_FUNC_TRANS  
	 * 
	 * @param avVector Vector  
	 * @param aaCompTransData CompleteTransactionData
	 * @param aaTransHdrData TransactionHeaderData
	 * @throws RTSException
	 */
	public void populateSRFuncTrans(
		Vector avVector,
		CompleteTransactionData aaCompTransData,
		TransactionHeaderData aaTransHdrData)
		throws RTSException
	{
		// defect 9423 
		// VehInq doesn't create MVFuncTrans, so Void of VehInq doesn't 
		//  return data for isSpecialPlateTransaction()
		// Therefore create Void of SpecialPlateTransHstry for all Voids
		int liResComptCntyNo = 0;
		int liChrgSpclPltFeeIndi = 0;
		String lsItrntTraceNo = "";
		SpecialRegistrationFunctionTransactionData laSRFuncTransData =
			null;
		String lsTransCd = caTransactionControl.getTransCd();
		if (isSpecialPlateTransaction(aaCompTransData))
		{
			// PROC Assign key fields to 
			//  SpecialRegistrationFunctionTransactionData
			laSRFuncTransData =
				new SpecialRegistrationFunctionTransactionData();
			laSRFuncTransData.setCustSeqNo(
				aaTransHdrData.getCustSeqNo());
			laSRFuncTransData.setOfcIssuanceNo(
				aaTransHdrData.getOfcIssuanceNo());
			laSRFuncTransData.setSubstaId(aaTransHdrData.getSubstaId());
			laSRFuncTransData.setTransAMDate(
				aaTransHdrData.getTransAMDate());
			laSRFuncTransData.setTransWsId(
				aaTransHdrData.getTransWsId());
			laSRFuncTransData.setTransCd(
				caTransactionControl.getTransCd());
			laSRFuncTransData.setTransTime(ciTransTime);
			// defect 10505 
			laSRFuncTransData.setTransId(getTransId(aaTransHdrData));
			// end defect 10505 
			// defect 10734 
			if (caTransactionControl
				.getTransCd()
				.equals(TransCdConstant.SBRNW)
				|| caTransactionControl.getTransCd().equals(
					TransCdConstant.WRENEW))
			{
				// end defect 10734 
				SubcontractorRenewalData laSubconRenewalData =
					aaCompTransData
						.getSubcontractorRenewalCacheData()
						.getTempSubconRenewalData();

				// defect 10734 
				if (caTransactionControl
					.getTransCd()
					.equals(TransCdConstant.WRENEW))
				{
					// defect 11206 
					// Set Number of Months for Special Plate 
					//aaCompTransData.setNoMoChrg(
					aaCompTransData.setSpclPlateNoMoCharge(
						laSubconRenewalData.getPltSoldMos());
					// end defect 11206 
				}
				// end defect 10734

				laSRFuncTransData.setSpclRegId(0);
				laSRFuncTransData.setSAuditTrailTransId("0");
				// Assign SBRNW available fields 
				laSRFuncTransData.setOrgNo(
					laSubconRenewalData.getOrgNo());
				laSRFuncTransData.setRegPltCd(
					laSubconRenewalData.getSpclPltRegPltCd());
				laSRFuncTransData.setAddlSetIndi(
					laSubconRenewalData.getAddlSetIndi());
				laSRFuncTransData.setPltSetNo(
					laSubconRenewalData.getAddlSetIndi() + 1);
				laSRFuncTransData.setPltExpYr(
					laSubconRenewalData.getPltNextExpYr());
				laSRFuncTransData.setPltExpMo(
					laSubconRenewalData.getPltNextExpMo());
				laSRFuncTransData.setPltValidityTerm(
					laSubconRenewalData.getPltVldtyTerm());
				laSRFuncTransData.setPltSoldMos(
					aaCompTransData.getSpclPlateNoMoCharge());
				laSRFuncTransData.setRegPltNo(
					laSubconRenewalData.getRegPltNo());
				liResComptCntyNo = aaTransHdrData.getOfcIssuanceNo();
				setCacheTransTime(laSRFuncTransData, ciCacheTransTime);

				// defect 10734
				// WRENEW  
				if (laSubconRenewalData.getPltBirthDate() != 0)
				{
					SpecialPlatesRegisData laSpclPltRegisData =
						new SpecialPlatesRegisData();
					laSpclPltRegisData.setPltBirthDate(
						laSubconRenewalData.getPltBirthDate());
					laSpclPltRegisData.setRegPltCd(
						laSubconRenewalData.getPltItmCd());
					laSpclPltRegisData.setPltExpMo(
						laSubconRenewalData.getPltExpMo());
					laSpclPltRegisData.setPltExpYr(
						laSubconRenewalData.getPltExpYr());

					if (laSubconRenewalData.isMfgSpclPlt())
					{
						laSpclPltRegisData.setMFGDate(lsTransCd);
						laSRFuncTransData.setMfgDate(
							laSpclPltRegisData.getMFGDate());
						laSRFuncTransData.setMfgStatusCd(
							SpecialPlatesConstant
								.MANUFACTURE_MFGSTATUSCD);
					}
				}
				// end defect 10734 
			}
			else
			{
				MFVehicleData laMFVehData =
					(MFVehicleData) aaCompTransData.getVehicleInfo();
				SpecialPlatesRegisData laSpclPltRegisData =
					laMFVehData.getSpclPltRegisData();
				laSpclPltRegisData.initWhereNull();
				String lsSAuditTrailTransId =
					laSpclPltRegisData.getSAuditTrailTransId().trim();
				if (UtilityMethods.isAllZeros(lsSAuditTrailTransId)
					|| lsSAuditTrailTransId.trim().equals(""))
				{
					lsSAuditTrailTransId = "0";
				}
				laSRFuncTransData.setSAuditTrailTransId(
					lsSAuditTrailTransId);
				lsItrntTraceNo = laSpclPltRegisData.getItrntTraceNo();
				OwnerData laMFOwnrData = laMFVehData.getOwnerData();
				// defect 9487 
				// Reinstate for REPL 
				// defect 9386 
				if (lsTransCd.equals(TransCdConstant.REPL)
					&& laSpclPltRegisData.getRequestType().equals(
						SpecialPlatesConstant.ISSUE_FROM_INVENTORY))
				{
					ProcessInventoryData laProcInvData =
						(ProcessInventoryData) aaCompTransData
							.getAllocInvItms()
							.elementAt(
							0);
					laSpclPltRegisData.setRegPltNo(
						laProcInvData.getInvItmNo());
					laSpclPltRegisData.setMfgPltNo();
				} // end defect 9386 
				// end defect 9487 
				laSRFuncTransData.setSpclRegId(
					laSpclPltRegisData.getSpclRegId());
				laSRFuncTransData.setRegPltCd(
					laSpclPltRegisData.getRegPltCd());
				laSRFuncTransData.setRegPltNo(
					laSpclPltRegisData.getRegPltNo());
				laSRFuncTransData.setOrgNo(
					laSpclPltRegisData.getOrgNo());
				laSRFuncTransData.setMfgStatusCd(
					laSpclPltRegisData.getMFGStatusCd());
				laSRFuncTransData.setMfgDate(
					laSpclPltRegisData.getMFGDate());
				laSRFuncTransData.setMfgPltNo(
					laSpclPltRegisData.getMfgPltNo());
				laSRFuncTransData.setAddlSetIndi(
					laSpclPltRegisData.getAddlSetIndi());
				laSRFuncTransData.setPltSetNo(
					laSpclPltRegisData.getAddlSetIndi() + 1);
				// defect 9864 
				// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
				// Update Special Plate Regis Data ExpMo/Yr if set 
				// in CompleteTransaction data  (e.g. SPRNW, 
				// Title/Registration   
				// defect 9689
				// For Vendor Plates, do not overwrite the Special Plate 
				// Expiration Mo\Yr.
				if (PlateTypeCache
					.isVendorPlate(laSpclPltRegisData.getRegPltCd()))
				{
					laSRFuncTransData.setPltExpMo(
						laSpclPltRegisData.getPltExpMo());
					laSRFuncTransData.setPltExpYr(
						laSpclPltRegisData.getPltExpYr());
				}
				else
				{
					if (saCompTransData != null
						&& saCompTransData.getExpMo() != 0
						&& saCompTransData.getExpYr() != 0)
					{
						laSpclPltRegisData.setPltExpMo(
							saCompTransData.getExpMo());
						laSpclPltRegisData.setPltExpYr(
							saCompTransData.getExpYr());
					}
					laSRFuncTransData.setPltExpMo(
						laSpclPltRegisData.getPltExpMo());
					laSRFuncTransData.setPltExpYr(
						laSpclPltRegisData.getPltExpYr());
				} // end defect 9689
				// end defect 9864 
				// OwnerData 
				OwnerData laOwnrData = laSpclPltRegisData.getOwnrData();
				// defect 10112 
				// Set OwnrId before potentially overwrite  
				// laSRFuncTransData.setPltOwnrId(laOwnrData.getOwnrId());
				String lsOwnrId = laOwnrData.getOwnrId();
				if (!UtilityMethods
					.isSpecialPlates(caTransactionControl.getTransCd())
					&& laMFOwnrData != null)
				{
					// defect 9893
					laMFVehData.assgnSpclPltRegisAddr();
					// end defect 9893
					// If MF Down, Copy OwnrData to Special Plate
					if (laSpclPltRegisData.isMFDownSP())
					{
						laOwnrData =
							(OwnerData) UtilityMethods.copy(
								laMFOwnrData);
					}
				}
				// OwnerData Continued
				laOwnrData.setOwnrId(lsOwnrId);
				laSRFuncTransData.setPltOwnerData(
					(OwnerData) UtilityMethods.copy(laOwnrData));
				// EMail
				laSRFuncTransData.setPltOwnrEMail(
					laSpclPltRegisData.getPltOwnrEMail());
				// Phone 
				laSRFuncTransData.setPltOwnrPhone(
					laSpclPltRegisData.getPltOwnrPhoneNo());
				// ISAIndi
				laSRFuncTransData.setISAIndi(
					laSpclPltRegisData.getISAIndi());
				// DlrGDN (10 Digit)
				// defect 9557
				//	Reference 10 digit DlrGDN
				laSRFuncTransData.setPltOwnrDlrGDN(
					laSpclPltRegisData.getPltOwnrDlrGDN());
				// end defect 9557
				// defect 9581
				laSRFuncTransData.setDissociateCd(
					laSpclPltRegisData.getPltRmvCd());
				// end defect 9581
				// HQ Only Input Fields 
				laSRFuncTransData.setPltOwnrOfcCd(
					laSpclPltRegisData.getPltOwnrOfcCd());
				laSRFuncTransData.setPltOwnrDist(
					laSpclPltRegisData.getPltOwnrDist());
				laSRFuncTransData.setSpclDocNo(
					laSpclPltRegisData.getSpclDocNo());
				laSRFuncTransData.setSpclRemks(
					laSpclPltRegisData.getSpclRemks());

				// To Pass to RTS_Spcl_Plt_Trans_Hstry
				liResComptCntyNo =
					laSpclPltRegisData.getResComptCntyNo();
				liChrgSpclPltFeeIndi =
					laSpclPltRegisData.getSpclPltChrgFeeIndi();
				// defect 10375 
				// add ResrvReasnCd, MktngAllowdIndi,
				//	 AuctnPltIndi, AuctnPdAmt, PltValidityTerm, 
				//   PltSoldMos,ItrntTraceNo 
				laSRFuncTransData.setResrvReasnCd(
					laSpclPltRegisData.getResrvReasnCd());
				laSRFuncTransData.setMktngAllowdIndi(
					laSpclPltRegisData.getMktngAllowdIndi());
				laSRFuncTransData.setAuctnPltIndi(
					laSpclPltRegisData.getAuctnPltIndi());
				laSRFuncTransData.setAuctnPdAmt(
					laSpclPltRegisData.getAuctnPdAmt());
				laSRFuncTransData.setPltValidityTerm(
					laSpclPltRegisData.getPltValidityTerm());
				// defect 10500 
				// Assign differently for VOID 
				// defect 10401
				// use passed in vs. static 
				if (lsTransCd.startsWith(TransCdConstant.VOID))
				{
					laSRFuncTransData.setPltSoldMos(
						laSpclPltRegisData.getNoMonthsToCharge());
				}
				else
				{
					laSRFuncTransData.setPltSoldMos(
						aaCompTransData.getSpclPlateNoMoCharge());
				} // end defect 10401
				// end defect 10500 
				laSRFuncTransData.setItrntTraceNo(
					laSpclPltRegisData.getItrntTraceNo());
				// end defect 10375 
				// defect 10507 
				laSRFuncTransData.setElectionPndngIndi(
					laSpclPltRegisData.getElectionPndngIndi());
				// end defect 10507 
				if (aaCompTransData.isPrntSpclPltPrmt())
				{

				}
			} // Only create SR_FUNC_TRANS if:
			//  - !INVVD
			//  - Build MVFuncTrans || VOIDxx
			// defect 9259 
			// Always write SR_FUNC_TRANS w/ VOID vs. w/ INVVD 
			// defect 10392 
			//if (!lsTransCd.equals(TransCdConstant.SBRNW)
			//	&& !lsTransCd.equals(TransCdConstant.INVVD)
			if (!lsTransCd.equals(TransCdConstant.INVVD)
				&& (caTransactionControl.isBuildMVFuncTrans()
					|| lsTransCd.startsWith(TransCdConstant.VOID)))
			{
				TransactionCacheData laTransCacheData =
					new TransactionCacheData();
				laTransCacheData.setProcName(
					TransactionCacheData.INSERT);
				laTransCacheData.setObj(laSRFuncTransData);
				avVector.addElement(laTransCacheData);
			}
		}
		if ((isSpecialPlateTransaction(aaCompTransData)
			|| lsTransCd.startsWith(TransCdConstant.VOID))
			&& !lsTransCd.equals(TransCdConstant.INVVD))
		{
			populateSpclPltTransHstry(
				avVector,
				aaCompTransData,
				laSRFuncTransData,
				aaTransHdrData,
				lsItrntTraceNo,
				liResComptCntyNo,
				liChrgSpclPltFeeIndi);
		}
		// defect 10700 
		if (aaCompTransData.isPrntSpclPltPrmt())
		{
			populateSpclPltPrmt(
				avVector,
				aaCompTransData,
				aaTransHdrData);
		} // end defect 10700 
	}
	/**
	 * Set up the Vector to populate RTS_SPCL_PLT_TRANS_HSTRY  
	 * 
	 * @param avVector Vector  
	 * @param aaCompTransData CompleteTransactionData
	 * @param aaTransHdrData TransactionHeaderData
	 * @param aiRescomptCntyNo
	 * @param aiChrgSpclPltFeeIndi
	 * @throws RTSException
	 */
	public void populateSpclPltTransHstry(
		Vector avVector,
		CompleteTransactionData aaCTData,
		SpecialRegistrationFunctionTransactionData laSRFuncTransData,
		TransactionHeaderData aaTransHdrData,
		String asItrntTraceNo,
		int aiResComptCntyNo,
		int aiChrgSpclPltFeeIndi)
		throws RTSException
	{
		// PROC Assign key fields to SpecialPlateTransactionHistoryData
		SpecialPlateTransactionHistoryData laSpclPltTransHstryData =
			new SpecialPlateTransactionHistoryData();
		// defect 9423 
		TransactionCacheData laTransCacheData =
			new TransactionCacheData();
		if (caTransactionControl
			.getTransCd()
			.startsWith(TransCdConstant.VOID))
		{
			laSpclPltTransHstryData.setOfcIssuanceNo(
				aaCTData.getVoidOfcIssuanceNo());
			// defect 9340
			// Set Substaid for Update  
			laSpclPltTransHstryData.setSubstaId(
				aaCTData.getVoidSubstaId());
			// end defect 9340 
			laSpclPltTransHstryData.setTransWsId(
				aaCTData.getVoidTransWsId());
			laSpclPltTransHstryData.setTransAMDate(
				aaCTData.getVoidTransAMDate());
			laSpclPltTransHstryData.setTransTime(
				aaCTData.getVoidTransTime());
			laSpclPltTransHstryData.setCacheTransAMDate(
				aaTransHdrData.getTransAMDate());
			laSpclPltTransHstryData.setCacheTransAMDate(ciTransTime);
			laTransCacheData.setProcName(TransactionCacheData.VOID);
		}
		else
		{
			laSpclPltTransHstryData.setOfcIssuanceNo(
				aaTransHdrData.getOfcIssuanceNo());
			laSpclPltTransHstryData.setSubstaId(
				aaTransHdrData.getSubstaId());
			laSpclPltTransHstryData.setTransAMDate(
				aaTransHdrData.getTransAMDate());
			laSpclPltTransHstryData.setTransWsId(
				aaTransHdrData.getTransWsId());
			laSpclPltTransHstryData.setCustSeqNo(
				aaTransHdrData.getCustSeqNo());
			laSpclPltTransHstryData.setTransTime(ciTransTime);
			laSpclPltTransHstryData.setTransId(
				getTransId(aaTransHdrData));
			laSpclPltTransHstryData.setTransCd(
				caTransactionControl.getTransCd());
			laSpclPltTransHstryData.setTransEmpId(
				aaTransHdrData.getTransEmpId());

			// Registration Info 		
			laSpclPltTransHstryData.setRegPltCd(
				laSRFuncTransData.getRegPltCd());
			laSpclPltTransHstryData.setRegPltNo(
				laSRFuncTransData.getRegPltNo());
			laSpclPltTransHstryData.setMfgPltNo(
				laSRFuncTransData.getMfgPltNo());
			laSpclPltTransHstryData.setOrgNo(
				laSRFuncTransData.getOrgNo());
			laSpclPltTransHstryData.setPltExpMo(
				laSRFuncTransData.getPltExpMo());
			laSpclPltTransHstryData.setPltExpYr(
				laSRFuncTransData.getPltExpYr());

			// Owner Data
			laSpclPltTransHstryData.setPltOwnerData(
				(OwnerData) UtilityMethods.copy(
					laSRFuncTransData.getPltOwnerData()));

			// EMail 
			laSpclPltTransHstryData.setPltOwnrEMail(
				laSRFuncTransData.getPltOwnrEMail());
			// Phone 
			laSpclPltTransHstryData.setPltOwnrPhone(
				laSRFuncTransData.getPltOwnrPhone());
			// ISAIndi 
			laSpclPltTransHstryData.setISAIndi(
				laSRFuncTransData.getISAIndi());
			// ResComptCntyNo 
			laSpclPltTransHstryData.setResComptCntyNo(aiResComptCntyNo);
			// ItrntTraceNo
			laSpclPltTransHstryData.setItrntTraceNo(asItrntTraceNo);
			// ChrgSpclPltFeeIndi 
			laSpclPltTransHstryData.setChrgSpclPltFeeIndi(
				aiChrgSpclPltFeeIndi);

			// defect 10734 
			if (caTransactionControl
				.getTransCd()
				.equals(TransCdConstant.WRENEW)
				|| caTransactionControl.getTransCd().equals(
					TransCdConstant.SBRNW))
			{
				SubcontractorRenewalData laSubconRenewalData =
					aaCTData
						.getSubcontractorRenewalCacheData()
						.getTempSubconRenewalData();

				laSpclPltTransHstryData.setPriorPltExpMo(
					laSubconRenewalData.getPltExpMo());

				laSpclPltTransHstryData.setPriorPltExpYr(
					laSubconRenewalData.getPltExpYr());
			}

			//			// defect 10097
			//			// TODO - This should accommodate MyPlates
			//			else if (
			//				caTransactionControl.getTransCd().equals(
			//					TransCdConstant.SBRNW))
			//			{
			//				laSpclPltTransHstryData.setPriorPltExpMo(
			//					laSRFuncTransData.getPltExpMo());
			//				laSpclPltTransHstryData.setPriorPltExpYr(
			//					laSRFuncTransData.getPltExpYr() - 1);
			//			}
			//			// END TODO
			// end defect 10734 
			else if (
				aaCTData.getVehicleInfo() != null
					&& aaCTData.getVehicleInfo().isSpclPlt())
			{
				laSpclPltTransHstryData.setPriorPltExpMo(
					aaCTData
						.getVehicleInfo()
						.getSpclPltRegisData()
						.getOrigPltExpMo());

				laSpclPltTransHstryData.setPriorPltExpYr(
					aaCTData
						.getVehicleInfo()
						.getSpclPltRegisData()
						.getOrigPltExpYr());
			}

			laSpclPltTransHstryData.setPltFee(
				aaCTData.getSpclPlateFee());
			laSpclPltTransHstryData.setPLPFee(
				aaCTData.getSpclPlatePLPFee());
			// end defect 10097

			// defect 10375 
			// add ResrvReasnCd, MktngAllowdIndi, 
			//	 AuctnPltIndi, AuctnPdAmt, PltValidityTerm
			laSpclPltTransHstryData.setResrvReasnCd(
				laSRFuncTransData.getResrvReasnCd());
			laSpclPltTransHstryData.setMktngAllowdIndi(
				laSRFuncTransData.getMktngAllowdIndi());
			laSpclPltTransHstryData.setAuctnPltIndi(
				laSRFuncTransData.getAuctnPltIndi());
			laSpclPltTransHstryData.setAuctnPdAmt(
				laSRFuncTransData.getAuctnPdAmt());
			laSpclPltTransHstryData.setPltValidityTerm(
				laSRFuncTransData.getPltValidityTerm());
			// end defect 10375 
			laTransCacheData.setProcName(TransactionCacheData.INSERT);
		}
		laTransCacheData.setObj(laSpclPltTransHstryData);
		avVector.addElement(laTransCacheData);
		// end defect 9423 
	}
	/**
	 * Set up the Vector to populate Rts_Fund_Func_Trns and 
	 * Rts_Tr_Fds_Detail
	 * 
	 * @param avVector Vector  
	 * @param aaCompTransData CompleteTransactionData
	 * @param aaTransHdrData TransactionHeaderData
	 * @throws RTSException
	 */
	public void populateFundFuncTrnsTrFdsDetail(
		Vector avVector,
		CompleteTransactionData aaCompTransData,
		TransactionHeaderData aaTransHdrData)
		throws RTSException
	{
		//TEST IF it is not county office
		boolean lbContinue = false;
		// defect 9085
		boolean lbIAPPL =
			aaCompTransData.getTransCode().equals(
				TransCdConstant.IAPPL);
		// defect 9711
		// defect 10401
		boolean lbVendorPlate =
			(aaCompTransData
				.getTransCode()
				.equals(TransCdConstant.VPAPPL)
				|| aaCompTransData.getTransCode().equals(
					TransCdConstant.VPAPPR)
				|| aaCompTransData.getTransCode().equals(
					TransCdConstant.VPDEL)
				|| aaCompTransData.getTransCode().equals(
					TransCdConstant.VPPORT)
				|| aaCompTransData.getTransCode().equals(
					TransCdConstant.VPREDO)
				|| aaCompTransData.getTransCode().equals(
					TransCdConstant.VPREV)
				|| aaCompTransData.getTransCode().equals(
					TransCdConstant.VPRSRV)
				|| aaCompTransData.getTransCode().equals(
					TransCdConstant.VPRSTL)
				|| aaCompTransData.getTransCode().equals(
					TransCdConstant.VPUNAC));
		// end defect 10401
		// end defect 9711		
		// end defect 9085 
		// defect 6456 
		// Save prior running subtotal in the even of an error, i.e. 
		// need to 'rollback' 
		saPriorRunningSubtotal = saRunningSubtotal;
		// end defect 6456 
		Vector lvTransactionFundsDetailData = new Vector();
		FundFunctionTransactionData laFundFuncTrnsData =
			new FundFunctionTransactionData();
		//Local vars
		RegFeesData laRegFeesData = aaCompTransData.getRegFeesData();
		//for head quarters
		if (!OfficeIdsCache.isHQ(aaTransHdrData.getOfcIssuanceNo())
			|| lbIAPPL
			|| lbVendorPlate)
		{
			lbContinue = true;
		}
		else if (caTransactionControl.getEventType().equals(ACC))
		{
			lbContinue = true;
		}
		else if (caTransactionControl.getEventType().equals(VOID))
		{
			lbContinue = true;
		}

		if (lbContinue)
		{
			lbContinue = false;
			//PROC Build funds function and funds detail transactions
			if (caTransactionControl.getEventType().equals(VOID))
			{ //PROC Call non-payment routines for funds assignments from void module
				if (caTransactionControl
					.getTransCd()
					.equals(TransCdConstant.VOID)
					|| caTransactionControl.getTransCd().equals(
						TransCdConstant.VOIDNC))
				{
					//P900 PROC Assign data to transaction tables for VOID
					//Retrieve void Funds Trans
					TransactionKey laTransKey = new TransactionKey();
					laTransKey.setOfcIssuanceNo(
						aaCompTransData.getVoidOfcIssuanceNo());
					laTransKey.setSubstaId(
						aaCompTransData.getVoidSubstaId());
					laTransKey.setTransAMDate(
						aaCompTransData.getVoidTransAMDate());
					laTransKey.setTransWsId(
						aaCompTransData.getVoidTransWsId());
					laTransKey.setCustSeqNo(
						aaCompTransData.getVoidCustSeqNo());
					laTransKey.setTransTime(
						aaCompTransData.getVoidTransTime());
					FundsClientBusiness laFundsClientBusiness =
						new FundsClientBusiness();
					Vector lvFundsFuncAndTrFdsDetail =
						(Vector) laFundsClientBusiness.processData(
							GeneralConstant.FUNDS,
							FundsConstant
								.GET_FUND_FUNC_TRANS_AND_TR_FDS_DETAIL,
							laTransKey);
					if (lvFundsFuncAndTrFdsDetail != null
						&& lvFundsFuncAndTrFdsDetail.size() > 0)
					{
						laFundFuncTrnsData =
							(
								FundFunctionTransactionData) lvFundsFuncAndTrFdsDetail
									.get(
								0);
						lvTransactionFundsDetailData =
							(Vector) lvFundsFuncAndTrFdsDetail.get(1);
						//Make FundsPymntAmt negative
						laFundFuncTrnsData.setFundsPymntAmt(
							new Dollar("0.00").subtract(
								laFundFuncTrnsData.getFundsPymntAmt()));
						//Loop Make ItmPrice negative
						Dollar laMultipleVoidPymntAmt =
							new Dollar("0.00");
						for (int i = 0;
							i < lvTransactionFundsDetailData.size();
							i++)
						{
							TransactionFundsDetailData laTransFdsDetailData =
								(
									TransactionFundsDetailData) lvTransactionFundsDetailData
										.get(
									i);
							// defect 7586
							setCacheTransTime(
								laTransFdsDetailData,
								ciCacheTransTime);
							// end defect 7586
							laTransFdsDetailData.setItmPrice(
								new Dollar("0.00").subtract(
									laTransFdsDetailData
										.getItmPrice()));
							if (laTransFdsDetailData
								.getCashDrawerIndi()
								== 1)
							{
								laMultipleVoidPymntAmt =
									laMultipleVoidPymntAmt.add(
										laTransFdsDetailData
											.getItmPrice());
							}
						}
					}
				}
				else
				{
					lbContinue = true;
				}
			}
			else if (caTransactionControl.getEventType().equals(ACC))
			{ //PROC Call non-payment routines for funds assignments from accounting module
				if (caTransactionControl
					.getTransCd()
					.equals(TransCdConstant.HOTCK)
					|| caTransactionControl.getTransCd().equals(
						TransCdConstant.HOTDED))
				{
					//P230 PROC Build FundFuncTrans and TrFdsDetail for hot check events
					laFundFuncTrnsData.setComptCntyNo(
						aaCompTransData.getOfcIssuanceNo());
					if (!doFeesExist(aaCompTransData))
					{
						throw new RTSException(
							RTSException.FAILURE_MESSAGE,
							"Fees is null",
							"ERROR");
					}
					if (aaCompTransData.getRegFeesData() != null
						&& aaCompTransData.getRegFeesData().getVectFees()
							!= null)
					{
						Vector lvFees =
							aaCompTransData
								.getRegFeesData()
								.getVectFees();
						//LOOP to update Trans Funds Detail for credit amts > 0
						for (int i = 0; i < lvFees.size(); i++)
						{
							FeesData laFeesData =
								(FeesData) lvFees.get(i);
							//PROC if credit amount is greater than zero post to TrFdsDetail
							if (laFeesData
								.getItemPrice()
								.compareTo(new Dollar("0.00"))
								> 0)
							{
								TransactionFundsDetailData laTransFdsDetailData =
									new TransactionFundsDetailData();
								laTransFdsDetailData.setAcctItmCd(
									laFeesData.getAcctItmCd());
								if (caTransactionControl
									.getTransCd()
									.equals(TransCdConstant.HOTCK))
								{
									Dollar laPrice =
										new Dollar("0.00").subtract(
											laFeesData.getItemPrice());
									laTransFdsDetailData.setItmPrice(
										laPrice);
									//Assign ItmPrice to Acct{#} to generate CustSubtotal for display
									Transaction.setRunningSubtotal(
										Transaction
											.getRunningSubtotal()
											.add(
											laPrice));
									aaCompTransData.setCustSubtotal(
										aaCompTransData
											.getCustSubtotal()
											.add(
											laPrice));
								}
								else
								{
									laTransFdsDetailData.setItmPrice(
										laFeesData.getItemPrice());
									//Assign ItmPrice to Acct{#} to generate CustSubtotal for display
									Transaction.setRunningSubtotal(
										Transaction
											.getRunningSubtotal()
											.add(
											laFeesData.getItemPrice()));
									aaCompTransData.setCustSubtotal(
										aaCompTransData
											.getCustSubtotal()
											.add(
											laFeesData.getItemPrice()));
								}
								lvTransactionFundsDetailData
									.addElement(
									laTransFdsDetailData);
							}
						}
					}
				}
				else if (
					caTransactionControl.getTransCd().equals(
						TransCdConstant.REFUND)
						|| caTransactionControl.getTransCd().equals(
							TransCdConstant.RFCASH))
				{
					//P230 PROC Build FundFuncTrans and TrFdsDetail for refund event
					laFundFuncTrnsData.setComptCntyNo(
						aaCompTransData.getOfcIssuanceNo());
					//LOOP - to update Funds Func and Funds Detail for refund amounts greater than zero
					if (!doFeesExist(aaCompTransData))
					{
						throw new RTSException(
							RTSException.FAILURE_MESSAGE,
							"Fees is null",
							"ERROR");
					}
					if (aaCompTransData.getRegFeesData() != null
						&& aaCompTransData.getRegFeesData().getVectFees()
							!= null)
					{
						Vector lvFees =
							aaCompTransData
								.getRegFeesData()
								.getVectFees();
						//LOOP to update Trans Funds Detail for credit amts > 0
						for (int i = 0; i < lvFees.size(); i++)
						{
							FeesData laFeesData =
								(FeesData) lvFees.get(i);
							//PROC if credit amount is greater than zero post to TrFdsDetail
							if (laFeesData
								.getItemPrice()
								.compareTo(new Dollar("0.00"))
								> 0)
							{
								TransactionFundsDetailData laTransFdsDetailData =
									new TransactionFundsDetailData();
								laTransFdsDetailData.setAcctItmCd(
									laFeesData.getAcctItmCd());
								laTransFdsDetailData.setItmPrice(
									new Dollar("0.00").subtract(
										laFeesData.getItemPrice()));
								//Assign ItmPrice to Acct{#} to generate CustSubtotal for display
								Transaction.setRunningSubtotal(
									Transaction
										.getRunningSubtotal()
										.add(
										laTransFdsDetailData
											.getItmPrice()));
								aaCompTransData.setCustSubtotal(
									aaCompTransData
										.getCustSubtotal()
										.add(
										laTransFdsDetailData
											.getItmPrice()));
								lvTransactionFundsDetailData
									.addElement(
									laTransFdsDetailData);
							}
						}
					} // If authorized reg refund amount exists update Tr 
					// Funds Detail with acct item code
					MFVehicleData laMFVehicleData =
						aaCompTransData.getVehicleInfo();
					if (laMFVehicleData != null)
					{
						RegistrationData laRegistrationData =
							laMFVehicleData.getRegData();
						if (laRegistrationData
							.getRegRefAmt()
							.compareTo(new Dollar("0.00"))
							> 0)
						{
							TransactionFundsDetailData laTransFdsDetailData =
								new TransactionFundsDetailData();
							laTransFdsDetailData.setAcctItmCd("RFREG");
							laTransFdsDetailData.setItmPrice(
								new Dollar("0.00").subtract(
									laRegistrationData.getRegRefAmt()));
							lvTransactionFundsDetailData.addElement(
								laTransFdsDetailData);
							Transaction.setRunningSubtotal(
								Transaction
									.getRunningSubtotal()
									.subtract(
									laRegistrationData.getRegRefAmt()));
							aaCompTransData.setCustSubtotal(
								aaCompTransData
									.getCustSubtotal()
									.subtract(
									laRegistrationData.getRegRefAmt()));
						}
					}
				}
				else if (
					// P230 PROC Build FundFuncTrans and TrFdsDetail for 
				// ADLCOL & RGNCOL events
				// defect 8348 
				//caTransactionControl.getTransCd().equals(
				//	TransCdConstant.FNDADJ) ||
				// end defect 8348 
				caTransactionControl
					.getTransCd()
					.equals(TransCdConstant.ADLCOL)
						|| caTransactionControl.getTransCd().equals(
							TransCdConstant.RGNCOL))
				{

					if (aaCompTransData.getRegFeesData() != null
						&& aaCompTransData.getRegFeesData().getVectFees()
							!= null)
					{
						Vector lvFeesData =
							aaCompTransData
								.getRegFeesData()
								.getVectFees();
						Dollar laTransactionSubtotal =
							new Dollar("0.00");
						laFundFuncTrnsData.setComptCntyNo(
							SystemProperty.getOfficeIssuanceNo());
						laFundFuncTrnsData.setFundsAdjReasn(
							laRegFeesData.getReason());
						// LOOP - to transfer funds adjustment info to 
						// TrFdsDetail table
						for (int i = 0; i < lvFeesData.size(); i++)
						{
							//Set the accounting values off from the screen
							TransactionFundsDetailData laTransFdsDetailData =
								new TransactionFundsDetailData();
							FeesData laFeesData =
								(FeesData) lvFeesData.get(i);
							laTransFdsDetailData.setAcctItmCd(
								laFeesData.getAcctItmCd());
							laTransFdsDetailData.setItmQty(
								laFeesData.getItmQty());
							laTransFdsDetailData.setItmPrice(
								laFeesData.getItemPrice());
							lvTransactionFundsDetailData.addElement(
								laTransFdsDetailData);
							laTransactionSubtotal =
								laTransactionSubtotal.add(
									laFeesData.getItemPrice());
							Transaction.setRunningSubtotal(
								Transaction.getRunningSubtotal().add(
									laFeesData.getItemPrice()));
							aaCompTransData.setCustSubtotal(
								aaCompTransData.getCustSubtotal().add(
									laFeesData.getItemPrice()));
						}
					}
				}
				else if (
					caTransactionControl.getTransCd().equals(
						TransCdConstant.FNDREM)
						|| caTransactionControl.getTransCd().equals(
							TransCdConstant.EFTFND)
						|| caTransactionControl.getTransCd().equals(
							TransCdConstant.VOIDFD))
				{
					//P231 PROC Build FundFuncTrans and TrFdsDetail for remit/void remit events
					//PROC Build FundFuncTrans for Funds Remittance
					FundsUpdateData laFundsUpdateData =
						aaCompTransData.getFundsUpdate();
					if (laFundsUpdateData == null)
					{
						throw new RTSException(
							RTSException.FAILURE_MESSAGE,
							"lFundsUpdateData is null",
							"ERROR");
					}
					laFundFuncTrnsData.setTraceNo(
						laFundsUpdateData.getTraceNo());
					if (caTransactionControl
						.getTransCd()
						.equals(TransCdConstant.EFTFND))
					{
						// defect 7560
						// AccountNoCd is initially entered as 5 characters; Will 
						// drop leading 0 in conversion to Integer; Thus, repad.
						// Or else, use SystemProperty.getOfficeIssuanceNo()
						String lsAccountNum =
							Integer.toString(
								laFundsUpdateData.getAccountNoCd());
						lsAccountNum =
							UtilityMethods.addPadding(
								lsAccountNum,
								5,
								"0");
						laFundFuncTrnsData.setComptCntyNo(
							Integer.parseInt(
								lsAccountNum.substring(0, 3)));
						laFundFuncTrnsData.setAccntNoCd(
							Integer.parseInt(
								lsAccountNum.substring(
									lsAccountNum.length() - 1)));
						// end defect 7560 
					}
					else
					{
						laFundFuncTrnsData.setAccntNoCd(
							laFundsUpdateData.getAccountNoCd());
						if (caTransactionControl
							.getTransCd()
							.equals(TransCdConstant.FNDREM))
						{
							laFundFuncTrnsData.setComptCntyNo(
								SystemProperty.getOfficeIssuanceNo());
						}
						else
						{
							// defect 7560 
							//laFundFuncTransData
							//	.setComptCntyNo(
							//	0);
							laFundFuncTrnsData.setComptCntyNo(
								laFundsUpdateData.getComptCountyNo());
							// end 7560 
						}
					}
					laFundFuncTrnsData.setCkNo(
						laFundsUpdateData.getCheckNo());
					laFundFuncTrnsData.setApprndComptCntyNo(0);
					Vector lvFundsDueData =
						laFundsUpdateData.getFundsDue();
					Dollar laFundsPymntAmt = new Dollar("0.00");
					//LOOP Build FdsDetail for Funds Remittance
					//Calculate Total 
					for (int i = 0; i < lvFundsDueData.size(); i++)
					{
						FundsDueData laFundsDueData =
							(FundsDueData) lvFundsDueData.get(i);
						laFundsPymntAmt.add(
							laFundsDueData.getRemitAmount());
						//PROC Assign FdsDetail for Funds Remittance
						if (laFundsDueData
							.getRemitAmount()
							.compareTo(new Dollar("0.00"))
							!= 0)
						{
							TransactionFundsDetailData laTransFdsDetailData =
								new TransactionFundsDetailData();
							laTransFdsDetailData.setFundsCat(
								laFundsDueData.getFundsCategory());
							laTransFdsDetailData.setFundsRcvdAmt(
								laFundsDueData.getRemitAmount());
							laTransFdsDetailData.setFundsRcvngEnt(
								laFundsDueData
									.getFundsReceivingEntity());
							laTransFdsDetailData.setFundsRptDate(
								laFundsDueData
									.getFundsReportDate()
									.getYYYYMMDDDate());
							laTransFdsDetailData.setRptngDate(
								laFundsDueData
									.getReportingDate()
									.getYYYYMMDDDate());
							lvTransactionFundsDetailData.addElement(
								laTransFdsDetailData);
							laFundsPymntAmt =
								laFundsPymntAmt.add(
									laTransFdsDetailData
										.getFundsRcvdAmt());
						}
					}
					laFundFuncTrnsData.setFundsPymntAmt(
						laFundsPymntAmt);
					laFundFuncTrnsData.setFundsPymntDate(
						laFundsUpdateData
							.getFundsPaymentDate()
							.getYYYYMMDDDate());
				}
				else
				{
					lbContinue = true;
				}
			}
			// defect 10734 
			else if (
				caTransactionControl.getEventType().equals(SBRNW)
					|| caTransactionControl.getTransCd().equals(
						TransCdConstant.WRENEW))
			{
				// end defect 10734 
				//Build TrFdsDetails
				SubcontractorRenewalData laSubconRenewalData =
					aaCompTransData
						.getSubcontractorRenewalCacheData()
						.getTempSubconRenewalData();
				Vector lvDetailFees =
					laSubconRenewalData.getFeesDataTrFunds();
				Transaction.setRunningSubtotal(
					Transaction.getRunningSubtotal().add(
						laSubconRenewalData.getRenwlTotalFees()));
				if (lvDetailFees != null && lvDetailFees.size() > 0)
				{
					for (int i = 0; i < lvDetailFees.size(); i++)
					{
						FeesData laFeesData =
							(FeesData) lvDetailFees.get(i);
						// defect 8900 
						// Do not disregard fee if 0.00 && populated
						// AcctItmCd 
						if ((laFeesData
							.getItemPrice()
							.compareTo(ZERO_DOLLAR)
							> 0)
							|| (laFeesData
								.getItemPrice()
								.compareTo(ZERO_DOLLAR)
								== 0
								&& laFeesData.getAcctItmCd() != null
								&& laFeesData.getAcctItmCd().length()
									!= 0))
						{
							TransactionFundsDetailData laTransFdsDetailData =
								new TransactionFundsDetailData();

							// defect 10734
							// ItmQty was missing for SBRNW; Verified 
							//  change w/ Reuben.  	
							laTransFdsDetailData.setItmQty(
								laFeesData.getItmQty());
							// end defect 10734 														

							laTransFdsDetailData.setItmPrice(
								laFeesData.getItemPrice());
							laTransFdsDetailData.setAcctItmCd(
								laFeesData.getAcctItmCd());
							lvTransactionFundsDetailData.addElement(
								laTransFdsDetailData);
						} // end defect 8900 
					}
				}
				laFundFuncTrnsData.setComptCntyNo(
					aaCompTransData.getOfcIssuanceNo());
				laFundFuncTrnsData.setSubconId(
					aaCompTransData
						.getSubcontractorRenewalCacheData()
						.getSubcontractorHdrData()
						.getSubconId());
				laFundFuncTrnsData.setSubconIssueDate(
					laSubconRenewalData.getSubconIssueDate());
			}
			else if (
				caTransactionControl.getTransCd().equals(
					TransCdConstant.CCPYMNT))
			{
				lvTransactionFundsDetailData.clear();
				laFundFuncTrnsData = new FundFunctionTransactionData();
				//Build funds function transaction
				laFundFuncTrnsData.setComptCntyNo(
					aaTransHdrData.getOfcIssuanceNo());
				TransactionFundsDetailData laTransFdsDetailData =
					new TransactionFundsDetailData();
				// defect 9088
				if (SystemProperty.isRegion())
				{
					laTransFdsDetailData.setAcctItmCd("CCARDF-R");
				}
				else
				{
					laTransFdsDetailData.setAcctItmCd("CCARDFEE");
				} // end defect 9088 
				laTransFdsDetailData.setItmPrice(
					aaCompTransData
						.getCreditCardFeeData()
						.getItmPrice());
				laTransFdsDetailData.setItmQty(1);
				lvTransactionFundsDetailData.addElement(
					laTransFdsDetailData);
			} // defect 8900
			else if (
				caTransactionControl.getTransCd().equals(
					TransCdConstant.DRVED))
			{
				lvTransactionFundsDetailData.clear();
				TransactionFundsDetailData laTransFdsDetailData =
					new TransactionFundsDetailData();
				laTransFdsDetailData.setAcctItmCd("EXEMPT");
				laTransFdsDetailData.setItmPrice(new Dollar("0.00"));
				laTransFdsDetailData.setItmQty(1);
				lvTransactionFundsDetailData.addElement(
					laTransFdsDetailData);
				// Assign Fund Function Transaction
				laFundFuncTrnsData = new FundFunctionTransactionData();
				laFundFuncTrnsData.setComptCntyNo(
					aaTransHdrData.getOfcIssuanceNo());
			} // end defect 8900 
			else
			{
				lbContinue = true;
			}
			if (lbContinue)
			{
				//PROC Build funds function and funds detail transaction
				Vector lvFees = null;
				if (laRegFeesData != null)
				{
					lvFees = laRegFeesData.getVectFees();
				}

				if ((laRegFeesData != null
					&& lvFees != null
					&& lvFees.size() != 0)
					&& (!SystemProperty.isHQ()
						|| lbIAPPL)) // write FdsDetail, etc if IAPPL
				{
					lvTransactionFundsDetailData.clear();
					laFundFuncTrnsData =
						new FundFunctionTransactionData();
					//Build funds function transaction
					laFundFuncTrnsData.setComptCntyNo(
						aaTransHdrData.getOfcIssuanceNo());
					//Set apprehended county number
					RegTtlAddlInfoData laRegTtlAddlInfoData =
						aaCompTransData.getRegTtlAddlInfoData();
					if (laRegTtlAddlInfoData != null)
					{
						laFundFuncTrnsData.setApprndComptCntyNo(
							laRegTtlAddlInfoData
								.getApprhndFndsCntyNo());
					}
					Dollar laTransactionSubtotal = new Dollar("0.00");
					for (int i = 0; i < lvFees.size(); i++)
					{
						FeesData laFeesData = (FeesData) lvFees.get(i);
						TransactionFundsDetailData laTransFdsDetailData =
							new TransactionFundsDetailData();
						laTransFdsDetailData.setAcctItmCd(
							laFeesData.getAcctItmCd());
						laTransFdsDetailData.setItmPrice(
							laFeesData.getItemPrice());
						// defect 9711	
						if (lbVendorPlate)
						{
							SpecialPlatesRegisData laSpclPltRegisData =
								aaCompTransData
									.getVehicleInfo()
									.getSpclPltRegisData();
							// defect 10311
							laTransFdsDetailData.setItmQty(
							//laSpclPltRegisData.getPltTerm());
							laSpclPltRegisData.getPltValidityTerm());
							// end defect 10311 
						}
						else
						{
							laTransFdsDetailData.setItmQty(
								laFeesData.getItmQty());
						} // end defect 9711	
						laTransactionSubtotal =
							laTransactionSubtotal.add(
								laFeesData.getItemPrice());
						lvTransactionFundsDetailData.addElement(
							laTransFdsDetailData);
					} // Assign ItmPrice to Acct{#} to general CustSubtotal
					// for display
					Transaction.setRunningSubtotal(
						Transaction.getRunningSubtotal().add(
							laTransactionSubtotal));
					aaCompTransData.setCustSubtotal(
						aaCompTransData.getCustSubtotal().add(
							laTransactionSubtotal));
				}
			} //end PROC Build funds function and funds detail transactions
			//PROC Build TransId fields and CashDrawerIndi in TrFdsDetail{#}
			//LOOP - Insert TransID and CashDrawer Indi into TrFdsDetail{#}
			Vector lvTransCacheData = new Vector();
			TransactionCodesData laTransCdsData =
				TransactionCodesCache.getTransCd(
					caTransactionControl.getTransCd());
			if (laTransCdsData == null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					"No TransactionCodesData for "
						+ caTransactionControl.getTransCd(),
					"Error");
			} // Build Key fields for RTS_TR_FDS_DETAIL
			for (int i = 0;
				i < lvTransactionFundsDetailData.size();
				i++)
			{
				TransactionFundsDetailData laTransFdsDetailData =
					(
						TransactionFundsDetailData) lvTransactionFundsDetailData
							.get(
						i);
				laTransFdsDetailData.setCashDrawerIndi(
					laTransCdsData.getCashDrawerIndi());
				laTransFdsDetailData.setOfcIssuanceNo(
					aaTransHdrData.getOfcIssuanceNo());
				laTransFdsDetailData.setSubstaId(
					aaTransHdrData.getSubstaId());
				laTransFdsDetailData.setTransAMDate(
					aaTransHdrData.getTransAMDate());
				laTransFdsDetailData.setTransWsId(
					aaTransHdrData.getTransWsId());
				laTransFdsDetailData.setCustSeqNo(
					aaTransHdrData.getCustSeqNo());
				laTransFdsDetailData.setTransTime(ciTransTime);
				// defect 10505 
				laTransFdsDetailData.setTransId(
					getTransId(aaTransHdrData));
				// end defect 10505 
				setCacheTransTime(
					laTransFdsDetailData,
					ciCacheTransTime);
				laTransFdsDetailData.setTransCd(
					caTransactionControl.getTransCd());
				TransactionCacheData laTransCacheData =
					new TransactionCacheData();
				laTransCacheData.setObj(laTransFdsDetailData);
				laTransCacheData.setProcName(
					TransactionCacheData.INSERT);
				lvTransCacheData.addElement(laTransCacheData);
			} // defect 8900 
			// If records exist for RTS_TR_FDS_DETAIL, assign key 
			// fields for RTS_FUND_FUNC_TRNS and add to 
			// Transaction Cache
			if (lvTransactionFundsDetailData.size() > 0)
			{
				laFundFuncTrnsData.setCustSeqNo(
					aaTransHdrData.getCustSeqNo());
				laFundFuncTrnsData.setOfcIssuanceNo(
					aaTransHdrData.getOfcIssuanceNo());
				laFundFuncTrnsData.setSubstaId(
					aaTransHdrData.getSubstaId());
				laFundFuncTrnsData.setTransAMDate(
					aaTransHdrData.getTransAMDate());
				laFundFuncTrnsData.setTransCd(
					caTransactionControl.getTransCd());
				laFundFuncTrnsData.setTransTime(ciTransTime);
				// defect 10505 
				laFundFuncTrnsData.setTransId(getTransId());
				// end defect 10505 
				laFundFuncTrnsData.setTransWsId(
					aaTransHdrData.getTransWsId());
				// defect 9085 
				// IAPPL ItrntTraceNo 
				// defect 9711
				// if (lbIAPPL)
				if (lbIAPPL || lbVendorPlate)
				{
					laFundFuncTrnsData.setItrntTraceNo(
						aaCompTransData
							.getVehicleInfo()
							.getSpclPltRegisData()
							.getItrntTraceNo());
					laFundFuncTrnsData.setComptCntyNo(
						aaCompTransData
							.getVehicleInfo()
							.getSpclPltRegisData()
							.getResComptCntyNo());
				} // end defect 9711
				// end defect 9085 
				laFundFuncTrnsData.setCacheTransTime(ciCacheTransTime);
				TransactionCacheData laTransCacheData =
					new TransactionCacheData();
				laTransCacheData.setProcName(
					TransactionCacheData.INSERT);
				laTransCacheData.setObj(laFundFuncTrnsData);
				avVector.addElement(laTransCacheData);
				avVector.addAll(lvTransCacheData);
			} // end defect 8900 
		}
	}
	/**
	 * Populate the internet tables. Used with IRENEW and VOIDNC 
	 * (when voiding IRENEW) transcds.
	 *
	 * @param  avVector Vector 
	 * @param  aaCompTransData CompleteTransactionData 
	 * @throws RTSException
	 */
	private void populateInternetDbTables(
		Vector avVector,
		CompleteTransactionData aaCompTransData)
		throws RTSException
	{
		if (caTransactionControl
			.getTransCd()
			.equals(TransCdConstant.IRENEW))
		{
			// defect 3700
			// InternetTransData must process before MFVehicleData for setting
			// of CntyStatusCd.  (3, 4)
			// add InternetTransData to vector
			// Phase 2 is ready to go
			InternetTransactionData laInternetTransData =
				aaCompTransData.getInternetTransactionData();
			TransactionCacheData laTransCacheDataI =
				new TransactionCacheData();
			laTransCacheDataI.setObj(laInternetTransData);
			laTransCacheDataI.setProcName(TransactionCacheData.UPDATE);
			// defect 6782 
			// Ensure transtime on IRENEW transaction cache is the same
			// as the other TransactionCache Objects in that UOW
			RTSDate laRTSDate = caRTSDateTrans;
			laRTSDate.setMillisecond(0);
			laInternetTransData.setTransDateTime(laRTSDate);
			// end defect 6782
			avVector.addElement(laTransCacheDataI);
			// end defect 3700
			MFVehicleData laMFVehicleData =
				aaCompTransData.getVehicleInfo();
			laMFVehicleData.setTransAMDate(caRTSDateTrans.getAMDate());
			laMFVehicleData.setTransTime(ciTransTime);
			TransactionCacheData laTransCacheData =
				new TransactionCacheData();
			laTransCacheData.setObj(laMFVehicleData);
			laTransCacheData.setProcName(TransactionCacheData.UPDATE);
			avVector.addElement(laTransCacheData);
			MFVehicleData laMFVehicleData2 =
				aaCompTransData.getVehicleInfo();
			laMFVehicleData2.setTransAMDate(caRTSDateTrans.getAMDate());
			laMFVehicleData2.setTransTime(ciTransTime);
			TransactionCacheData laTransCacheData2 =
				new TransactionCacheData();
			laTransCacheData2.setObj(laMFVehicleData2);
			laTransCacheData2.setProcName(TransactionCacheData.INSERT);
			avVector.addElement(laTransCacheData2);
		} // defect 4053
		// Does not update CntyStatusCd for Void of IRENEW
		// (Alter VOID to VOIDNC)
		else if (
			caTransactionControl.getTransCd().equals(
				TransCdConstant.VOIDNC)
				&& aaCompTransData.getVoidTransCd() != null
				&& aaCompTransData.getVoidTransCd().equals(
					TransCdConstant.IRENEW))
		{
			MFVehicleData laMFVehicleData =
				aaCompTransData.getVehicleInfo();
			laMFVehicleData.setTransAMDate(caRTSDateTrans.getAMDate());
			laMFVehicleData.setTransTime(ciTransTime);
			TransactionCacheData laTransCacheData =
				new TransactionCacheData();
			laTransCacheData.setObj(laMFVehicleData);
			laTransCacheData.setProcName(TransactionCacheData.DELETE);
			avVector.addElement(laTransCacheData);
		}
	}
	/**
	 * Set up the Vector to populate RTS_INV_FUNC_TRANS && 
	 * RTS_TR_INV_DETAIL
	 * 
	 * @param avVector Vector
	 * @param aaCompTransData CompleteTransactionData
	 * @param aaTransHdr TransactionHeader 
	 * @throws RTSException
	 */
	public void populateInvFuncTransTrInvDetail(
		Vector avVector,
		CompleteTransactionData aaCompTransData,
		TransactionHeaderData aaTransHdrData)
		throws RTSException
	{ // if it is county or regional office
		if (aaTransHdrData.getOfcIssuanceCd() == OFFICE_COUNTY
			|| aaTransHdrData.getOfcIssuanceCd()
				== OFFICE_REGIONAL_OFFICE)
		{
			//Prepare all the variables
			InventoryFunctionTransactionData laInvFuncTransData =
				new InventoryFunctionTransactionData();
			TransactionInventoryDetailData laTransInvDetailData =
				new TransactionInventoryDetailData();
			Vector lvTransInvDetailData = new Vector();

			//PROC Move non-key data to InvFuncTrans and TrInvDetail
			// defect 5963
			// SBRNW Issue Inventory Cleanup 
			// defect 10734 
			if (caTransactionControl.getEventType().equals(SBRNW)
				|| caTransactionControl.getTransCd().equals(
					TransCdConstant.WRENEW))
			{
				// end defect 10734 
				SubcontractorRenewalData laSubconRenewalData =
					aaCompTransData
						.getSubcontractorRenewalCacheData()
						.getTempSubconRenewalData();
				String lsThisSubconId =
					String.valueOf(
						aaCompTransData
							.getSubcontractorRenewalCacheData()
							.getSubcontractorHdrData()
							.getSubconId());
				// Build TrInvDetail for Sticker
				// 5.2.1 All Subcon Stickers are printable! 
				if (laSubconRenewalData.getRecordType()
					== SubcontractorRenewalData.STKR
					|| laSubconRenewalData.getRecordType()
						== SubcontractorRenewalData.PLT_STKR)
				{
					// Sticker Detail 
					laTransInvDetailData.setDetailStatusCd(-1);
					laTransInvDetailData.setItmCd(
						laSubconRenewalData.getStkrItmCd());
					laTransInvDetailData.setInvItmNo(
						laSubconRenewalData.getNewStkrNo());
					laTransInvDetailData.setInvEndNo(
						laSubconRenewalData.getNewStkrNo());
					laTransInvDetailData.setInvItmYr(
						laSubconRenewalData.getNewExpYr());
					laTransInvDetailData.setInvQty(1);
					// Sticker Allocation Info
					laTransInvDetailData.setInvLocIdCd("S");
					laTransInvDetailData.setInvId(lsThisSubconId);
					laTransInvDetailData.setIssueMisMatchIndi(0);
					//Set the reprintCount to -1 so that when inserting into the db it is inserted
					//as a zero (see the trinvdetail db class for the various values it is expecting.
					if (!laSubconRenewalData.isPrint())
					{
						laTransInvDetailData.setReprntCount(-1);
					}
					else
					{
						laTransInvDetailData.setReprntCount(0);
					}
					lvTransInvDetailData.addElement(
						laTransInvDetailData);
				}

				//Build TrInvDetail for Plate
				// 5.2.1 One Plate is printable! 
				if (laSubconRenewalData.getRecordType()
					== SubcontractorRenewalData.PLT
					|| laSubconRenewalData.getRecordType()
						== SubcontractorRenewalData.PLT_STKR)
				{
					laTransInvDetailData =
						new TransactionInventoryDetailData();
					// Plate Detail
					laTransInvDetailData.setDetailStatusCd(-1);
					laTransInvDetailData.setItmCd(
						laSubconRenewalData.getPltItmCd());
					laTransInvDetailData.setInvItmNo(
						laSubconRenewalData.getNewPltNo());
					laTransInvDetailData.setInvEndNo(
						laSubconRenewalData.getNewPltNo());
					if (laSubconRenewalData.getRecordType()
						== SubcontractorRenewalData.PLT)
					{
						laTransInvDetailData.setInvItmYr(
							laSubconRenewalData.getNewExpYr());
					}
					else
					{
						laTransInvDetailData.setInvItmYr(0);
					} // Plate Allocation Info
					//
					// Printable Only 
					// defect 7477
					// Assign InvLocIdCd, InvId, IssueMisMatchIndi, 
					// InvQty, ReprntCount  
					if (StickerPrintingUtilities
						.isStickerPrintable(
							laSubconRenewalData.getPltItmCd()))
					{
						laTransInvDetailData.setInvLocIdCd("S");
						laTransInvDetailData.setInvId(lsThisSubconId);
						laTransInvDetailData.setIssueMisMatchIndi(0);
						laTransInvDetailData.setInvQty(1);
						if (!laSubconRenewalData.isPrint())
						{
							laTransInvDetailData.setReprntCount(-1);
						}
						else
						{
							laTransInvDetailData.setReprntCount(0);
						}
					} // end defect 7477
					// Not Printable 
					else
					{
						laTransInvDetailData.setReprntCount(-1);
						// Get associated Allocation Info from lSubcontractorRenewalData 
						String lsInvLocIdCd =
							laSubconRenewalData
								.getProcInvPlt()
								.getInvLocIdCd();
						String lsInvId =
							laSubconRenewalData
								.getProcInvPlt()
								.getInvId();
						laTransInvDetailData.setInvLocIdCd(
							lsInvLocIdCd);
						laTransInvDetailData.setInvId(lsInvId);
						if (lsInvLocIdCd.equals("V"))
						{
							laTransInvDetailData.setInvQty(0);
						}
						else
						{
							laTransInvDetailData.setInvQty(1);
						} //PROC Determine the mismatched for TrInvDetail{0}
						// defect 7610
						// Do not assign MisMatchIndi when InvLocIdCd = "X"
						// If Found and Not Subcontractor or not SubconId, 
						// and not Server Down  assign MisMatched!
						//   added && !lsInvLocIdCd.equals("X") 
						if (!lsInvLocIdCd.equals("U")
							&& !lsInvLocIdCd.equals("V")
							&& !lsInvLocIdCd.equals("X")
							&& (!lsInvLocIdCd.equals("S")
								|| !lsInvId.equals(lsThisSubconId)))
						{
							laTransInvDetailData.setIssueMisMatchIndi(
								1);
						}
						else
						{
							laTransInvDetailData.setIssueMisMatchIndi(
								0);
						} // end defect 7610 
					}
					lvTransInvDetailData.addElement(
						laTransInvDetailData);
				} // end Plate or Plate & Sticker 
				//Build InvFuncTrans
				laInvFuncTransData.setSubconIssueDate(
					laSubconRenewalData.getSubconIssueDate());
			} // end defect 5963 
			else if (caTransactionControl.getEventType().equals(INV))
			{
				lvTransInvDetailData =
					aaCompTransData.getTransInvDetail();
				laInvFuncTransData = aaCompTransData.getInvFuncTrans();
			}
			else if (
				caTransactionControl.getTransCd().equals(
					TransCdConstant.INVVD))
			{
				TransactionKey laTransKey = new TransactionKey();
				laTransKey.setOfcIssuanceNo(
					aaCompTransData.getVoidOfcIssuanceNo());
				laTransKey.setSubstaId(
					aaCompTransData.getVoidSubstaId());
				laTransKey.setTransAMDate(
					aaCompTransData.getVoidTransAMDate());
				laTransKey.setTransWsId(
					aaCompTransData.getVoidTransWsId());
				laTransKey.setCustSeqNo(
					aaCompTransData.getVoidCustSeqNo());
				laTransKey.setTransTime(
					aaCompTransData.getVoidTransTime());
				InventoryClientBusiness laInventoryClientBusiness =
					new InventoryClientBusiness();
				Vector lvInvFuncTrInvDetail =
					(Vector) laInventoryClientBusiness.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant
							.GET_INV_FUNC_TRANS_AND_TR_INV_DETAIL,
						laTransKey);
				laInvFuncTransData =
					(
						InventoryFunctionTransactionData) lvInvFuncTrInvDetail
							.get(
						0);
				laInvFuncTransData.setVoidedTransIndi(0);
				lvTransInvDetailData =
					(Vector) lvInvFuncTrInvDetail.get(1);
				for (int i = 0; i < lvTransInvDetailData.size(); i++)
				{
					TransactionInventoryDetailData laTransInvDetailData2 =
						(
							TransactionInventoryDetailData) lvTransInvDetailData
								.get(
							i);
					laTransInvDetailData2.setDelInvReasnCd(
						INVVD_DEL_INV_REASON_CD);
					laTransInvDetailData2.setInvQty(INVVD_INV_QTY);
					laTransInvDetailData2.setDetailStatusCd(
						INVVD_DETAIL_STATUS_CD);
					// defect 5872
					ItemCodesData laItemCodesData =
						ItemCodesCache.getItmCd(
							laTransInvDetailData2.getItmCd());
					if (laItemCodesData.isPrintable())
					{
						laTransInvDetailData2.setReprntCount(-1);
					} // end defect 5872 
				}
			}
			else
			{ //S603 PROC Build InvFuncTrans and TrInvDetail
				//If County, Region || (HQ && DRVED) 
				if (aaTransHdrData.getOfcIssuanceCd() == OFFICE_COUNTY
					|| aaTransHdrData.getOfcIssuanceCd()
						== OFFICE_REGIONAL_OFFICE
					|| (aaTransHdrData.getOfcIssuanceCd()
						== OFFICE_HEAD_QUARTERS
						&& aaCompTransData.getTransCode().equals(
							TransCdConstant.DRVED)))
				{
					// defect 10491 
					boolean lbPrmtTrans =
						UtilityMethods.printsPermit(
							caTransactionControl.getTransCd());
					if (lbPrmtTrans)
					{
						aaCompTransData
							.assignAllocInvItmsForTimedPermit();
					} // end defect 10491 
					// LOOP Build TrInvDetail
					if (aaCompTransData.getAllocInvItms() != null)
					{
						for (int i = 0;
							i
								< aaCompTransData
									.getAllocInvItms()
									.size();
							i++)
						{
							//ASSIGN Build TrInvDetail record
							laTransInvDetailData =
								new TransactionInventoryDetailData();
							ProcessInventoryData laProcInvData =
								(ProcessInventoryData) aaCompTransData
									.getAllocInvItms()
									.get(
									i);
							// defect 10491 
							// Write Permit Plate No to TR_INV_DETAIL 
							// PCR 34
							if (StickerPrintingUtilities
								.isStickerPrintable(laProcInvData)
								|| lbPrmtTrans)
							{
								// defect 7137
								// Where PrintableIndi = 1 && ItmTrckngType = "P"
								// assign invitmno to last 6 digits of VIN 
								String lsItmNo = "";
								ItemCodesData laItmCdsData =
									ItemCodesCache.getItmCd(
										laProcInvData.getItmCd());
								if (lbPrmtTrans)
								{
									lsItmNo =
										laProcInvData.getInvItmNo();
								}
								else if (
									laItmCdsData
										.getItmTrckngType()
										.equals(
										"P"))
								{
									String lsVIN =
										aaCompTransData
											.getVehicleInfo()
											.getVehicleData()
											.getVin();
									lsItmNo =
										UtilityMethods.addPadding(
											lsVIN,
											22,
											" ").substring(
											16);
									laProcInvData.setInvItmNo(lsItmNo);
								}
								laTransInvDetailData.setInvEndNo(
									lsItmNo);
								laTransInvDetailData.setInvItmNo(
									lsItmNo);
								// end defect 10491  
								// end defect 7137 
								if (UtilityMethods
									.isDTA(
										aaCompTransData
											.getTransCode()))
								{
									// defect 10290 
									DealerTitleData laLastChngdDlrTtlData =
										caCurrDlrTtlData;
									//	for (int j =
									//			aaCompTransData
									//		.getDlrTtlDataObjs()
									//		.size()
									//		- 1;
									//	j > -1;
									//	j--)
									//{
									//	DealerTitleData laDlrTitleData =
									//		(DealerTitleData) aaCompTransData
									//			.getDlrTtlDataObjs()
									//			.get(
									//			j);
									//	if (laDlrTitleData
									//		.isProcessed())
									//	{
									//		laLastChngdDlrTitleData =
									//			laDlrTitleData;
									//		break;
									//	}
									//	}
									// defect 8217
									//	Reset getters to match 
									//		DealerTitleData's 
									//if (laLastChngdDlrTitleData
									//	.getBlackBoxPrintNum()
									//	> 1)
									//	laTransInvDetailData
									//		.setReprntCount(2);
									if (laLastChngdDlrTtlData
										.getRSPSPrntInvQty()
										> 1)
										laTransInvDetailData
											.setReprntCount(
											2);
									// end defect 8217
									if (!laLastChngdDlrTtlData
										.isToBePrinted())
										laTransInvDetailData
											.setReprntCount(
											-1);
									laTransInvDetailData.setInvLocIdCd(
										"D");
									// defect 7437
									// Use DlrTtlDataObjs() to derive DealerId
									// laTransInvDetailData.setInvId(""+aaCompTransData.getDealerId());
									laTransInvDetailData.setInvId(
										""
											+ laLastChngdDlrTtlData
												.getDealerId());
									// end defect 7437 
									// end defect 10290 
								}
								else
								{
									laTransInvDetailData.setInvLocIdCd(
										"E");
									laTransInvDetailData.setInvId(
										SystemProperty
											.getCurrentEmpId());
								}
							}
							else
							{
								laTransInvDetailData.setInvEndNo(
									laProcInvData.getInvItmEndNo());
								laTransInvDetailData.setInvItmNo(
									laProcInvData.getInvItmNo());
								laTransInvDetailData.setInvLocIdCd(
									laProcInvData.getInvLocIdCd());
								laTransInvDetailData.setInvId(
									laProcInvData.getInvId());
							} // END PCR 34
							laTransInvDetailData.setInvItmYr(
								laProcInvData.getInvItmYr());
							laTransInvDetailData.setItmCd(
								laProcInvData.getItmCd());
							laTransInvDetailData.setDetailStatusCd(-1);
							//ASSIGN InvQty, InvId, InvLocIdCd for TrInvDetail{#}
							switch (laProcInvData.getIssueStatus())
							{
								case ProcessInventoryData
									.NOT_EXISTED_ITEM_REUSED :
									laTransInvDetailData.setInvQty(0);
									laTransInvDetailData.setInvLocIdCd(
										"V");
									break;
								case ProcessInventoryData
									.NOT_EXISTED_ITEM :
								case ProcessInventoryData
									.NOT_EXISTED_ITEM_REISSUED :
									laTransInvDetailData.setInvQty(1);
									laTransInvDetailData.setInvLocIdCd(
										"U");
									break;
								default :
									laTransInvDetailData.setInvQty(1);
									if (laProcInvData.getInvStatusCd()
										== CommonConstant.MISMATCHED_ITEM)
									{
										laTransInvDetailData
											.setIssueMisMatchIndi(
											1);
									}
									else
									{
										laTransInvDetailData
											.setIssueMisMatchIndi(
											0);
									}
							}
							lvTransInvDetailData.addElement(
								laTransInvDetailData);
						}
					}
				} //end S603 PROC Build InvFuncTrans and TrInvDetail
			} //PROC Build TransID fields in InvFuncTrns{#}
			laInvFuncTransData.setCustSeqNo(
				aaTransHdrData.getCustSeqNo());
			laInvFuncTransData.setOfcIssuanceNo(
				aaTransHdrData.getOfcIssuanceNo());
			laInvFuncTransData.setSubstaId(
				aaTransHdrData.getSubstaId());
			laInvFuncTransData.setTransAMDate(
				aaTransHdrData.getTransAMDate());
			laInvFuncTransData.setTransWsId(
				aaTransHdrData.getTransWsId());
			laInvFuncTransData.setTransTime(ciTransTime);
			// defect 10505 
			laInvFuncTransData.setTransId(getTransId(aaTransHdrData));
			// end defect 10505 
			laInvFuncTransData.setTransCd(
				aaCompTransData.getTransCode());
			laInvFuncTransData.setEmpId(aaTransHdrData.getTransEmpId());
			//defect 7586
			setCacheTransTime(laInvFuncTransData, ciCacheTransTime);
			//end defect 7586
			TransactionCacheData laInvFuncTransCacheData =
				new TransactionCacheData();
			laInvFuncTransCacheData.setProcName(
				TransactionCacheData.INSERT);
			laInvFuncTransCacheData.setObj(laInvFuncTransData);
			//PROC Build TransID fields in TrInvDetail{#}
			Vector lvTransInvDetailCacheData = new Vector();
			for (int i = 0; i < lvTransInvDetailData.size(); i++)
			{
				TransactionInventoryDetailData laTransInvDetailData3 =
					(
						TransactionInventoryDetailData) lvTransInvDetailData
							.get(
						i);
				laTransInvDetailData3.setCustSeqNo(
					aaTransHdrData.getCustSeqNo());
				laTransInvDetailData3.setOfcIssuanceNo(
					aaTransHdrData.getOfcIssuanceNo());
				laTransInvDetailData3.setSubstaId(
					aaTransHdrData.getSubstaId());
				laTransInvDetailData3.setTransAMDate(
					aaTransHdrData.getTransAMDate());
				laTransInvDetailData3.setTransWsId(
					aaTransHdrData.getTransWsId());
				laTransInvDetailData3.setTransTime(ciTransTime);
				// defect 10505 
				laTransInvDetailData3.setTransId(getTransId());
				// end defect 10505 
				laTransInvDetailData3.setTransCd(
					aaCompTransData.getTransCode());
				// defect 7586
				//if (cTransactionControl.getTransCd().equals(TransCdConstant.SBRNW))
				//{
				//	saveSubConInfo(laTransInvDetailData3);
				//}
				setCacheTransTime(
					laTransInvDetailData3,
					ciCacheTransTime);
				// end defect 7586 
				TransactionCacheData laTransCacheData =
					new TransactionCacheData();
				laTransCacheData.setProcName(
					TransactionCacheData.INSERT);
				laTransCacheData.setObj(laTransInvDetailData3);
				lvTransInvDetailCacheData.addElement(laTransCacheData);
			}
			if (lvTransInvDetailData.size() > 0)
			{
				avVector.addElement(laInvFuncTransCacheData);
				if (lvTransInvDetailCacheData.size() != 0)
				{
					avVector.addAll(lvTransInvDetailCacheData);
				}
			}
		}
	}
	/**
	 * Set up the Vector to populate RTS_MV_FUNC_TRANS
	 *
	 * @param avVector Vector 
	 * @param aaCompTransData CompleteTransactionData
	 * @param aaTransHdr TransactionHeaderData
	 * @throws RTSException
	 */
	public void populateMvTrans(
		Vector avVector,
		CompleteTransactionData aaCompTransData,
		TransactionHeaderData aaTransHdrData)
		throws RTSException
	{
		boolean lbAddlBuildCriteria = true;
		// defect 9056
		// make the call to calculate the plate age below the 
		// if statement that determines if we need to build mv func.
		// defect 7737
		// Calculate plate age; Moved from INV007
		//calculatePlateAge(aaCompTransData);
		// end defect 7737
		// end defect 9056
		// defect 4520
		// Do no write MV_Func_Trans when Refund (REFUND | RFCASH) 
		// and Cancelled (Plate | Sticker)
		if (aaCompTransData.getVehicleInfo() != null
			&& (aaCompTransData
				.getVehicleInfo()
				.getRegData()
				.getCancPltIndi()
			// defect 9953
				== 1) //== 1
		//|| aaCompTransData
		//	.getVehicleInfo()
		//	.getRegData()
		//	.getCancStkrIndi()
		//	== 1)
		// end defect 9953
			&& (caTransactionControl
				.getTransCd()
				.equals(TransCdConstant.RFCASH)
				|| caTransactionControl.getTransCd().equals(
					TransCdConstant.REFUND)))
		{
			lbAddlBuildCriteria = false;
		}
		if (aaCompTransData.getVehicleInfo() != null
			&& aaCompTransData.getVehicleInfo().isDoNotBuildMvFunc())
		{
			lbAddlBuildCriteria = false;
		}
		else if (aaCompTransData.getVoidTransCd() != null)
		{
			TransactionControl laTransCtl =
				(TransactionControl) shTransactionControl.get(
					aaCompTransData.getVoidTransCd());
			if (laTransCtl != null)
			{
				if (!laTransCtl.isBuildMVFuncTrans())
				{
					lbAddlBuildCriteria = false;
				}
			}
		}

		if (caTransactionControl.isBuildMVFuncTrans()
			&& lbAddlBuildCriteria)
		{
			// defect 9056
			// Only need to calculate plate age if we are building 
			// mv func record.  
			// For further expaination see defect 7737.
			// Calculate plate age; Moved from INV007
			calculatePlateAge(aaCompTransData);
			// end defect 9056
			MotorVehicleFunctionTransactionData laMVFuncTransData =
				new MotorVehicleFunctionTransactionData();
			TitleData laTitleData = null;
			OwnerData laOwnerData = null;
			RegistrationData laRegistrationData = null;
			VehicleData laVehicleData = null;
			SpecialPlatesRegisData laSpclPltRegisData = null;
			// defect 10112 
			//AddressData laAddressData = null;
			LienholderData laLienholderData1 = new LienholderData();
			LienholderData laLienholderData2 = new LienholderData();
			LienholderData laLienholderData3 = new LienholderData();
			// end defect 10112 
			SalvageData laSalvageData = null;
			VehMiscData laVehMiscData = null;
			MFVehicleData laMFVehicleData =
				aaCompTransData.getVehicleInfo();
			if (aaCompTransData.getVehMisc() != null)
			{
				laVehMiscData = aaCompTransData.getVehMisc();
			}
			if (laMFVehicleData != null)
			{
				laTitleData = laMFVehicleData.getTitleData();
				laOwnerData = laMFVehicleData.getOwnerData();
				laRegistrationData = laMFVehicleData.getRegData();
				laVehicleData = laMFVehicleData.getVehicleData();
				laSpclPltRegisData =
					laMFVehicleData.getSpclPltRegisData();
				// defect 9903 
				// Reset PrismLvlCd for COA, SCOT, NRCOT, TITLE, DTA*
				// defect 10507
				// SORelectionIndi became ElectionPndngIndi on 
				//  SpecialRegisData 
				if (laRegistrationData
					!= null 
					&& UtilityMethods.isNewTitleTransCd(
						caTransactionControl.getTransCd()))
				{
					//laRegistrationData.setSORelectionIndi(0);
					// end defect 10507
					laRegistrationData.setPrismLvlCd(
						CommonConstant.STR_SPACE_EMPTY);
				} // end defect 9903 
				// defect 9085 	
				if (caTransactionControl
					.getEventType()
					.equals(SPCLPLT))
				{
					laRegistrationData.setResComptCntyNo(
						laSpclPltRegisData.getResComptCntyNo());
					laRegistrationData.setRegPltCd(
						laSpclPltRegisData.getRegPltCd());
					laRegistrationData.setRegPltNo(
						laSpclPltRegisData.getRegPltNo());
					laRegistrationData.setRegEffDt(
						laSpclPltRegisData.getRegEffDate());
					int liDLRPLPRegClassCd =
						ClassToPlateCache.getPLPDLRRegClassCd(
							laSpclPltRegisData.getRegPltCd());
					if (liDLRPLPRegClassCd != 0)
					{
						laRegistrationData.setRegClassCd(
							liDLRPLPRegClassCd);
					}
				} // end defect 9085 
				else
				{ // defect 6861
					// Set to MFDown if necessary 
					if (!laMFVehicleData.isFromMF())
					{
						laMVFuncTransData.setMfDwnCd(1);
					} // Consolidate into single check for laMFVehicleData !=null
					// from below  
					if (laMFVehicleData.getVctSalvage() != null)
					{
						int lastElement =
							laMFVehicleData.getVctSalvage().size() - 1;
						laSalvageData =
							(SalvageData) laMFVehicleData
								.getVctSalvage()
								.get(
								lastElement);
					} // end defect 6861
				}
			} 
			// defect 6424 
			// Do not write LienholdersData for COA 
			if (laTitleData != null
				&& !caTransactionControl.getTransCd().equals(
					TransCdConstant.COA))
			{
				// defect 10112 
				laLienholderData1 =
					(LienholderData) UtilityMethods.copy(
						laTitleData.getLienholderData(
							TitleConstant.LIENHLDR1));
				laLienholderData2 =
					(LienholderData) UtilityMethods.copy(
						laTitleData.getLienholderData(
							TitleConstant.LIENHLDR2));
				laLienholderData3 =
					(LienholderData) UtilityMethods.copy(
						laTitleData.getLienholderData(
							TitleConstant.LIENHLDR3));
				// end defect 10112 
			} // end defect 6424 
			//INVVD
			// Note from KPH:  This code is bogus, as laMVFuncTransData
			//                 OfcIssuanceNo nor the RegClassCd have 
			//                 been set. 
			//                 
			if (caTransactionControl
				.getTransCd()
				.equals(TransCdConstant.INVVD))
			{ 
				//Test TtlNoMfIndi for MVFuncTrans{0}.TtlNoMf
				//	if (!isSpecialRegistration(aaTransHdrData
				//		.getOfcIssuanceCd(),
				//		laMVFuncTransData.getOfcIssuanceNo(),
				//		laMVFuncTransData.getRegClassCd()))
				if (!isSpecialRegistration(laMVFuncTransData
					.getRegClassCd()))
				{
					laMVFuncTransData.setTtlNoMf(
						getTransId(aaTransHdrData));
				}
			}
			else
			{ 
				//Assign Data to MVFuncTrans
				if ((!(caTransactionControl
					.getTransCd()
					.equals(TransCdConstant.REFUND)
					|| caTransactionControl.getTransCd().equals(
						TransCdConstant.RFCASH)))
					|| !(aaCompTransData.getOrgVehicleInfo() == null))
				{ 
					//PROC Clear reg invalid indi if there is inventory
					if (aaCompTransData.getAllocInvItms() != null
						&& aaCompTransData.getAllocInvItms().size() > 0)
					{
						laRegistrationData.setRegInvldIndi(0);
					}
					// defect 10734 
					if (caTransactionControl
						.getEventType()
						.equals(SBRNW)
						|| caTransactionControl.getTransCd().equals(
							TransCdConstant.WRENEW))
					{
						// end defect 10734  
						SubcontractorRenewalCacheData laSubconRenewalCacheData =
							aaCompTransData
								.getSubcontractorRenewalCacheData();
						SubcontractorRenewalData laSubconRenewalData =
							aaCompTransData
								.getSubcontractorRenewalCacheData()
								.getTempSubconRenewalData();
						laMVFuncTransData.setCustActulRegFee(
							laSubconRenewalData.getRenwlTotalFees());
						
						// defect 5100 previously.  It was not in clearcase.
						// Calculate regeffdate
						int liRegEffYr =
							laSubconRenewalData.getNewExpYr() - 1;
						int liRegEffMo =
							laSubconRenewalData.getRegExpMo() + 1;
						// defect 6605
						// Only add 1 to year.  Previously we were adding year to year
						// effectively doubling the year.  
						if (liRegEffMo == 13)
						{
							liRegEffMo = 1;
							liRegEffYr = liRegEffYr + 1;
						} // end defect 6605
						// end defect 5100
						String lsRegEffDate =
							UtilityMethods.addPadding(
								new String[] {
									String.valueOf(liRegEffYr),
									String.valueOf(liRegEffMo),
									"1" },
								new int[] { 4, 2, 2 },
								"0");
						laMVFuncTransData.setRegEffDate(
							Integer.parseInt(lsRegEffDate));
						laMVFuncTransData.setRegExpMo(
							laSubconRenewalData.getRegExpMo());
						laMVFuncTransData.setRegExpYr(
							laSubconRenewalData.getNewExpYr() - 1);
						laMVFuncTransData.setRegPltNo(
							laSubconRenewalData.getRegPltNo());
						laMVFuncTransData.setSubconId(
							aaCompTransData
								.getSubcontractorRenewalCacheData()
								.getSubcontractorHdrData()
								.getSubconId());
						laMVFuncTransData.setSubconIssueDate(
							laSubconRenewalData.getSubconIssueDate());
						laMVFuncTransData.setRegClassCd(
							Integer.parseInt(
								laSubconRenewalData.getRegClassCd()));
						// defect 7553
						if (laSubconRenewalData.getAuditTrailTransid()
							!= 0)
						{
							laMVFuncTransData.setAuditTrailTransId(
								UtilityMethods.addPadding(
									String.valueOf(
										laSubconRenewalData
											.getAuditTrailTransid()),
									17,
									"0"));
						} // end defect 7553
						laMVFuncTransData.setResComptCntyNo(
							aaCompTransData.getOfcIssuanceNo());
						//Bar code info
						if (laSubconRenewalCacheData.isUseBarCode())
						{
							RenewalBarCodeData laRenewalBarCodeData =
								laSubconRenewalData
									.getRenewalBarCodeData();
							// PCR 34 
							if (laRenewalBarCodeData != null)
							{
								String lsDocNo =
									UtilityMethods.addPadding(
										laRenewalBarCodeData.getDocNo(),
										17,
										"0");
								laMVFuncTransData.setDocNo(lsDocNo);
								laMVFuncTransData.setVIN(
									laRenewalBarCodeData.getVin());
							}
						} //defect 5097 
						else
						{
							String lsDocNo =
								UtilityMethods.addPadding(
									laSubconRenewalData.getDocNo(),
									17,
									"0");
							laMVFuncTransData.setDocNo(lsDocNo);
							laMVFuncTransData.setVIN(
								laSubconRenewalData.getVIN());
						} // end defect 5097
						// End PCR 34 
						
						// defect 11137  
						// defect 11181 
						// Check for Null laMfVehicleData 
						if (caTransactionControl.getTransCd().equals(
							TransCdConstant.WRENEW) && laMFVehicleData != null)
						{
							// end defect 11181 
							laMVFuncTransData.setOwnerData(laMFVehicleData.getOwnerData());
							if (laRegistrationData!= null)
							{
								laMVFuncTransData.setRecpntName(laRegistrationData.getRecpntName()); 
								laMVFuncTransData.setRenewalAddrData(laRegistrationData.getRenwlMailAddr());
							}
						}
						// end defect 11137 
					}
					else
					{
						if (laRegistrationData != null)
						{
							laMVFuncTransData.setCustActulRegFee(
								laRegistrationData.getCustActlRegFee());
							// defect 9631
							//laMVFuncTransData.setCustBaseRegFee(
							//	laRegistrationData.getCustBaseRegFee());
							//laMVFuncTransData.setCustDieselFee(
							//	laRegistrationData.getCustDieselFee());
							// end defect 9631
							laMVFuncTransData.setNotfyngCity(
								laRegistrationData.getNotfyngCity());
							laMVFuncTransData.setResComptCntyNo(
								laRegistrationData.getResComptCntyNo());
							laMVFuncTransData.setRegClassCd(
								laRegistrationData.getRegClassCd());
							laMVFuncTransData.setRegEffDate(
								laRegistrationData.getRegEffDt());
							laMVFuncTransData.setRegExpMo(
								laRegistrationData.getRegExpMo());
							laMVFuncTransData.setRegExpYr(
								laRegistrationData.getRegExpYr());
							// defect 8901
							// PltBirth
							boolean lbRenewal = false;
							if (aaCompTransData
								.getTransCode()
								.equals(TransCdConstant.RENEW))
							{
								lbRenewal = true;
							} //laMVFuncTransData.setRegPltAge(
							//	laRegistrationData.getRegPltAge());
							laMVFuncTransData.setRegPltAge(
								laRegistrationData.getRegPltAge(
									lbRenewal));
							laMVFuncTransData.setPltBirthDate(
								laRegistrationData.getPltBirthDate());
							// end defect 8901
							laMVFuncTransData.setRegPltCd(
								laRegistrationData.getRegPltCd());
							// defect 9670 
							// If PltRemoved, use RmvRegPltNo vs. 
							//  RegPltNo
							laMVFuncTransData.setRegPltNo(
								laRegistrationData.getPltRmvCd() == 0
									? laRegistrationData.getRegPltNo()
									: laRegistrationData
										.getRmvdRegPltNo());
							// end defect 9670
							laMVFuncTransData.setRegPltOwnrName(
								laRegistrationData.getRegPltOwnrName());
							laMVFuncTransData.setRegRefAmt(
								laRegistrationData.getRegRefAmt());
							laMVFuncTransData.setRegStkrCd(
								laRegistrationData.getRegStkrCd());
							laMVFuncTransData.setRecpntName(
								laRegistrationData.getRecpntName());
							//RECPNTLSTNAME,RECPNTFSTNAME,RECPNTMI 
							AddressData laRenwlMailAddr =
								laRegistrationData.getRenwlMailAddr();
							if (laRenwlMailAddr != null)
							{
								// defect 10112 
								laMVFuncTransData.setRenewalAddrData(
									(AddressData) UtilityMethods.copy(
										laRenwlMailAddr));
								//	laMVFuncTransData.setRenwlMailngSt1(
								//	   lRenwlMailAddr.getSt1());
								//	laMVFuncTransData.setRenwlMailngSt2(
								//	   lRenwlMailAddr.getSt2());
								//	laMVFuncTransData.setRenwlMailngCity(
								//		lRenwlMailAddr.getCity());
								//	laMVFuncTransData.setRenwlMailngState(
								//		lRenwlMailAddr.getState());
								//	laMVFuncTransData.setRenwlMailngZPCd(
								//		lRenwlMailAddr.getZpcd());
								//	laMVFuncTransData.setRenwlMailngZPCdP4(
								//		lRenwlMailAddr.getZpcdp4());
								// end defect 10112 
							}
							laMVFuncTransData.setTireTypeCd(
								laRegistrationData.getTireTypeCd());
							laMVFuncTransData.setVehCaryngCap(
								laRegistrationData.getVehCaryngCap());
							laMVFuncTransData.setVehGrossWt(
								laRegistrationData.getVehGrossWt());
							laMVFuncTransData.setOwnrSuppliedExpYr(
								laRegistrationData
									.getOwnrSuppliedExpYr());
							laMVFuncTransData.setExmptIndi(
								laRegistrationData.getExmptIndi());
							laMVFuncTransData.setHvyVehUseTaxIndi(
								laRegistrationData
									.getHvyVehUseTaxIndi());
							laMVFuncTransData.setPltsSeizdIndi(
								laRegistrationData.getPltSeizedIndi());
							laMVFuncTransData.setRenwlMailRtrnIndi(
								laRegistrationData
									.getRenwlMailRtrnIndi());
							laMVFuncTransData.setRenwlYrMsmtchIndi(
								laRegistrationData
									.getRenwlYrMismatchIndi());
							laMVFuncTransData.setFileTierCd(
								laRegistrationData.getFileTierCd());
							laMVFuncTransData.setRegHotCkIndi(
								laRegistrationData.getRegHotCkIndi());
							laMVFuncTransData.setRegInvldIndi(
								laRegistrationData.getRegInvldIndi());
							laMVFuncTransData.setClaimComptCntyNo(
								laRegistrationData
									.getClaimComptCntyNo());
							laMVFuncTransData.setEmissionSourceCd(
								laRegistrationData
									.getEmissionSourceCd());
							laMVFuncTransData.setStkrSeizdIndi(
								laRegistrationData.getStkrSeizdIndi());
							// defect 10476
							// Do not populate unless V21 transaction  
							if (caTransactionControl
								.getEventType()
								.equals(V21TRANS))
							{
								// defect 9581
								laMVFuncTransData.setV21PltId(
									laRegistrationData.getV21PltId());
								laMVFuncTransData.setDissociateCd(
									laRegistrationData.getPltRmvCd());
							} // end defect 10476 
							laMVFuncTransData.setPrismLvlCd(
								laRegistrationData.getPrismLvlCd());
							// end defect 9581
							// defect 9981 
							laMVFuncTransData.setVTRRegEmrgCd1(
								laRegistrationData.getVTRRegEmrgCd1());
							laMVFuncTransData.setVTRRegEmrgCd2(
								laRegistrationData.getVTRRegEmrgCd2());
							// end defect 9981
							// defect 10508 
							laMVFuncTransData.setEMailRenwlReqCd(
								laRegistrationData
									.getEMailRenwlReqCd());
							// end defect 10508 
							// defect 10507
							// defect 10375  
							// laMVFuncTransData.setSOReelectionIndi(
							// 	laRegistrationData
							// 		.getSOReelectionIndi());
							// end defect 10507
							laMVFuncTransData.setRecpntEMail(
								laRegistrationData.getRecpntEMail());
							// end defect 10375 
						}
						if (laVehicleData != null)
						{
							laMVFuncTransData.setReplicaVehMk(
								laVehicleData.getReplicaVehMk());
							laMVFuncTransData.setReplicaVehModlYr(
								laVehicleData.getReplicaVehModlYr());
							laMVFuncTransData.setAuditTrailTransId(
								laVehicleData.getAuditTrailTransId());
							laMVFuncTransData.setVehBdyType(
								laVehicleData.getVehBdyType());
							laMVFuncTransData.setVehBdyVin(
								laVehicleData.getVehBdyVin());
							laMVFuncTransData.setVehClassCd(
								laVehicleData.getVehClassCd());
							laMVFuncTransData.setVehEmptyWt(
								laVehicleData.getVehEmptyWt());
							laMVFuncTransData.setVehLngth(
								laVehicleData.getVehLngth());
							laMVFuncTransData.setVehMk(
								laVehicleData.getVehMk());
							laMVFuncTransData.setVehModl(
								laVehicleData.getVehModl());
							laMVFuncTransData.setVehModlYr(
								laVehicleData.getVehModlYr());
							laMVFuncTransData.setVehOdmtrBrnd(
								laVehicleData.getVehOdmtrBrnd());
							laMVFuncTransData.setVehOdmtrReadng(
								laVehicleData.getVehOdmtrReadng());
							laMVFuncTransData.setVehTon(
								laVehicleData.getVehTon());
							laMVFuncTransData.setVIN(
								laVehicleData.getVin());
							laMVFuncTransData.setDieselIndi(
								laVehicleData.getDieselIndi());
							laMVFuncTransData.setDOTStndrdsIndi(
								laVehicleData.getDotStndrdsIndi());
							laMVFuncTransData.setDPSSaftySuspnIndi(
								laRegistrationData
									.getDpsSaftySuspnIndi());
							laMVFuncTransData.setDPSStlnIndi(
								laVehicleData.getDpsStlnIndi());
							laMVFuncTransData.setFloodDmgeIndi(
								laVehicleData.getFloodDmgeIndi());
							laMVFuncTransData.setFxdWtIndi(
								laVehicleData.getFxdWtIndi());
							laMVFuncTransData.setPrmtReqrdIndi(
								laVehicleData.getPrmtReqrdIndi());
							if (laVehicleData.getRecondCd() != null
								&& !laVehicleData
									.getRecondCd()
									.trim()
									.equals(
									""))
							{
								laMVFuncTransData.setRecondCd(
									Integer.parseInt(
										laVehicleData.getRecondCd()));
							}
							laMVFuncTransData.setRecontIndi(
								laVehicleData.getReContIndi());
							laMVFuncTransData.setVINErrIndi(
								laVehicleData.getVinErrIndi());
							// defect 10712 
							// Add VehMjrColorCd, VehMnrColorCd 
							laMVFuncTransData.setVehMjrColorCd(
								laVehicleData.getVehMjrColorCd());
							laMVFuncTransData.setVehMnrColorCd(
								laVehicleData.getVehMnrColorCd());
							// end defect 10712 
						}
						if (laTitleData != null)
						{
							// defect 10013 
							laMVFuncTransData.setTtlExmnIndi(
								laTitleData.getTtlExmnIndi());
							// defect 10013 
							laMVFuncTransData.setBatchNo(
								laTitleData.getBatchNo());
							laMVFuncTransData.setBndedTtlCd(
								laTitleData.getBndedTtlCd());
							// defect 9969
							// Now set w/in CCO event 
							laMVFuncTransData.setCCOIssueDate(
								laTitleData.getCcoIssueDate());
							// ETtlCd set w/in Transaction 
							laMVFuncTransData.setETtlCd(
								laTitleData.getETtlCd());
							if (caCurrDlrTtlData != null)
							{
								caCurrDlrTtlData.setETtlRqst(
									laTitleData.isETitle() ? 1 : 0);
							} // end defect 9969
							// defect 10841 
							laMVFuncTransData.setETtlPrntDate(
									laTitleData.getETtlPrntDate());
							// end defect 10841 
							laMVFuncTransData.setDocNo(
								laTitleData.getDocNo());
							laMVFuncTransData.setDocTypeCd(
								laTitleData.getDocTypeCd());
							laMVFuncTransData.setLegalRestrntNo(
								laTitleData.getLegalRestrntNo());
							laMVFuncTransData.setPrevOwnrCity(
								laTitleData.getPrevOwnrCity());
							laMVFuncTransData.setPrevOwnrName(
								laTitleData.getPrevOwnrName());
							laMVFuncTransData.setPrevOwnrState(
								laTitleData.getPrevOwnrState());
							laMVFuncTransData.setTrlrType(
								laVehicleData.getTrlrType());
							laMVFuncTransData.setTtlApplDate(
								laTitleData.getTtlApplDate());
							laMVFuncTransData.setTtlProcsCd(
								laTitleData.getTtlProcsCd());
							laMVFuncTransData.setOldDocNo(
								laTitleData.getOldDocNo());
							laMVFuncTransData.setTtlIssueDate(
									laTitleData.getTtlIssueDate());
							laMVFuncTransData.setTtlRejctnDate(
								laTitleData.getTtlRejctnDate());
							laMVFuncTransData.setTtlRejctnOfc(
								laTitleData.getTtlRejctnOfc());
							laMVFuncTransData.setSalesTaxPdAmt(
								laTitleData.getSalesTaxPdAmt());
							AddressData laTtlVehAddr =
								laTitleData.getTtlVehAddr();
							// defect 10112 
							if (laTtlVehAddr != null)
							{
								laMVFuncTransData.setVehLocAddrData(
									(AddressData) UtilityMethods.copy(
										laTtlVehAddr));
								//	laMVFuncTransData.setTtlVehLocSt1(
								//		laTtlVehAddr.getSt1());
								//	laMVFuncTransData.setTtlVehLocSt2(
								//	    laTtlVehAddr.getSt2());
								//	laMVFuncTransData.setTtlVehLocCity(
								//		laTtlVehAddr.getCity());
								//	laMVFuncTransData.setTtlVehLocState(
								//		laTtlVehAddr.getState());
								//	laMVFuncTransData.setTtlVehLocZpCd(
								//		laTtlVehAddr.getZpcd());
								//	laMVFuncTransData.setTtlVehLocZpCdP4(
								//		laTtlVehAddr.getZpcdp4());
							} // end defect 10112 
							laMVFuncTransData.setVehSalesPrice(
								laTitleData.getVehSalesPrice());
							laMVFuncTransData.setVehSoldDate(
								laTitleData.getVehSoldDate());
							laMVFuncTransData.setVehTradeInAllownce(
								laTitleData.getVehTradeinAllownce());
							laMVFuncTransData.setVehUnitNo(
								laTitleData.getVehUnitNo());
							// defect 8903 
							// TERP
							laMVFuncTransData.setVehValue(
								laTitleData.getVehicleValue());
							// end defect 8903 
							//OWNRLSTNAME,OWNRFSTNAME,OWNRMI
							// defect 10112 
							//	laMVFuncTransData.setLien1Date(
							//		laTitleData.getLienHolder1Date());
							//	laMVFuncTransData.setLien2Date(
							//		laTitleData.getLienHolder2Date());
							//	laMVFuncTransData.setLien3Date(
							//		laTitleData.getLienHolder3Date());
							// end defect 10112 
							laMVFuncTransData.setComptCntyNo(
								laTitleData.getCompCntyNo());
							laMVFuncTransData.setDlrGDN(
								laTitleData.getDlrGdn());
							laMVFuncTransData.setSurrTtlDate(
								laTitleData.getSurrTtlDate());
							//TTLNOMF (only for special registration)
							laMVFuncTransData.setAddlLienRecrdIndi(
								laTitleData.getAddlLienRecrdIndi());
							laMVFuncTransData.setAgncyLoandIndi(
								laTitleData.getAgncyLoandIndi());
							laMVFuncTransData.setGovtOwndIndi(
								laTitleData.getGovtOwndIndi());
							laMVFuncTransData.setInspectnWaivedIndi(
								laTitleData.getInspectnWaivedIndi());
							laMVFuncTransData.setPriorCCOIssueIndi(
								laTitleData.getPriorCCOIssueIndi());
							laMVFuncTransData.setSurvshpRightsIndi(
								laTitleData.getSurvshpRightsIndi());
							laMVFuncTransData.setTtlRevkdIndi(
								laTitleData.getTtlRevkdIndi());
							laMVFuncTransData.setTtlHotCkIndi(
								laTitleData.getTtlHotCkIndi());
							laMVFuncTransData.setSalvStateCntry(
								laTitleData.getSalvStateCntry());
							// defect 9225
							//	Add LemonLaw
							laMVFuncTransData.setLemonLawIndi(
								laTitleData.getLemonLawIndi());
							// end defect 9225
							// defect 9581
							// Add ChildSupport, TtlSignDate and ETtlCd
							laMVFuncTransData.setChildSupportIndi(
								laTitleData.getChildSupportIndi());
							laMVFuncTransData.setTtlSignDate(
								laTitleData.getTtlSignDate());
							laMVFuncTransData.setETtlCd(
								laTitleData.getETtlCd());
							// end defect 9581
							// defect 9981 
							laMVFuncTransData.setPermLienHldrId1(
								laTitleData.getPermLienHldrId1());
							laMVFuncTransData.setPermLienHldrId2(
								laTitleData.getPermLienHldrId2());
							laMVFuncTransData.setPermLienHldrId3(
								laTitleData.getPermLienHldrId3());
							laMVFuncTransData.setLienRlseDate1(
								laTitleData.getLienRlseDate1());
							laMVFuncTransData.setLienRlseDate2(
								laTitleData.getLienRlseDate2());
							laMVFuncTransData.setLienRlseDate3(
								laTitleData.getLienRlseDate3());
							// defect 10366 
							laMVFuncTransData.setPvtLawEnfVehCd(
								laTitleData.getPvtLawEnfVehCd());
							laMVFuncTransData.setNonTtlGolfCartCd(
								laTitleData.getNonTtlGolfCartCd());
							// end defect 10366
							// defect 10375 
							laMVFuncTransData.setVTRTtlEmrgCd3(
								laTitleData.getVTRTtlEmrgCd3());
							laMVFuncTransData.setVTRTtlEmrgCd4(
								laTitleData.getVTRTtlEmrgCd4());
							// end defect 10375 
							laMVFuncTransData.setUTVMislblIndi(
								laTitleData.getUTVMislblIndi());
							// end defect 9981 
							
							// defect 11231 
							laMVFuncTransData.setExportIndi(laTitleData.getExportIndi()); 
							laMVFuncTransData.setSurvShpRightsName1(laTitleData.getSurvShpRightsName1());
							laMVFuncTransData.setSurvShpRightsName2(laTitleData.getSurvShpRightsName2());
							laMVFuncTransData.setAddlSurvivorIndi(laTitleData.getAddlSurvivorIndi());
							// end defect 11231 
							
							//SUBCONID,SUBCONISSUEDATE (not required in here)
						} // defect 10112 
						//	if (laOwnerData != null)
						//		{
						//			laMVFuncTransData.setOwnrId(
						//laOwnerData.getOwnrId());
						//			laMVFuncTransData.setPrivacyOptCd(
						//laOwnerData.getPrivacyOptCd());
						//		}
						// end defect 10112 
						// defect 9981 
						// Only not send Lienholder Date if 
						//   PermLienHldrIdx is populated 
						if (laTitleData != null)
						{ // defect 10112 
							if (UtilityMethods
								.isEmpty(
									laTitleData.getPermLienHldrId1()))
							{
								laMVFuncTransData.setLienholderData(
									TitleConstant.LIENHLDR1,
									laLienholderData1);
							}
							else
							{
								laMVFuncTransData
									.getLienholderData(
										TitleConstant.LIENHLDR1)
									.setLienDate(
										laLienholderData1
											.getLienDate());
							}
							if (UtilityMethods
								.isEmpty(
									laTitleData.getPermLienHldrId2()))
							{
								laMVFuncTransData.setLienholderData(
									TitleConstant.LIENHLDR2,
									laLienholderData2);
							}
							else
							{
								laMVFuncTransData
									.getLienholderData(
										TitleConstant.LIENHLDR2)
									.setLienDate(
										laLienholderData2
											.getLienDate());
							}
							if (UtilityMethods
								.isEmpty(
									laTitleData.getPermLienHldrId3()))
							{
								laMVFuncTransData.setLienholderData(
									TitleConstant.LIENHLDR3,
									laLienholderData3);
							}
							else
							{
								laMVFuncTransData
									.getLienholderData(
										TitleConstant.LIENHLDR3)
									.setLienDate(
										laLienholderData3
											.getLienDate());
							} // end defect 10112 
						} // end defect 9981 
						if (laVehMiscData != null)
						{
							laMVFuncTransData.setAuthCd(
								laVehMiscData.getAuthCd());
							// defect 8903  	
							// TERP 		
							laMVFuncTransData.setEmissionSalesTax(
								laVehMiscData.getEmissionSalesTax());
							// end defect 8903 
							laMVFuncTransData.setSalesTaxDate(
								laVehMiscData.getSalesTaxDate());
							laMVFuncTransData.setSalesTaxCat(
								laVehMiscData.getSalesTaxCat());
							laMVFuncTransData.setTaxPdOthrState(
								laVehMiscData.getTaxPdOthrState());
							laMVFuncTransData.setTradeInVehYr(
								laVehMiscData.getTradeInVehYr());
							laMVFuncTransData.setTradeInVehMk(
								laVehMiscData.getTradeInVehMk());
							laMVFuncTransData.setTradeInVehVin(
								laVehMiscData.getTradeInVehVIN());
							laMVFuncTransData.setSalesTaxExmptCd(
								laVehMiscData.getSalesTaxExmptCd());
							laMVFuncTransData.setTotalRebateAmt(
								laVehMiscData.getTotalRebateAmt());
							laMVFuncTransData.setSpclPltProgIndi(
								laVehMiscData.getSpclPltProgIndi());
							laMVFuncTransData.setAddlTradeInIndi(
								laVehMiscData.getAddlTradeInIndi());
							laMVFuncTransData.setIMCNo(
								new Dollar(
									Long.toString(
										laVehMiscData.getIMCNo())));
							// defect 10476
							// Do not populate unless V21 transaction  
							if (caTransactionControl
								.getEventType()
								.equals(V21TRANS))
							{
								// 	defect 9581 
								laMVFuncTransData.setV21VTNId(
									laVehMiscData.getV21VTNId());
								laMVFuncTransData.setVTNSource(
									laVehMiscData.getVTNSource());
							} // end defect 10476 
							laMVFuncTransData.setTtlTrnsfrEntCd(
								laVehMiscData.getTtlTrnsfrEntCd());
							// end defect 9581
							// defect 9724 
							laMVFuncTransData.setTtlTrnsfrPnltyExmptCd(
								laVehMiscData
									.getTtlTrnsfrPnltyExmptCd());
							// end defect 9724  
						}
						laMVFuncTransData.setOwnrSuppliedPltNo(
							aaCompTransData.getOwnrSuppliedPltNo());
						// defect 8901
						boolean lbRenewal = false;
						if (aaCompTransData
							.getTransCode()
							.equals(TransCdConstant.RENEW))
						{
							lbRenewal = true;
						}

						laMVFuncTransData.setRegPltAge(
							laRegistrationData.getRegPltAge(lbRenewal));
						// end defect 8901
						//Clear MfJnk{#} if TITLE, NONTTL, DTA
						if (caTransactionControl
							.getEventType()
							.equals(TTL)
							|| caTransactionControl.getEventType().equals(
								DTA))
						{
							laMVFuncTransData.setJnkCd(0);
							laMVFuncTransData.setJnkDate(0);
							laMVFuncTransData.setLien2NotRlsedIndi(0);
							laMVFuncTransData.setLien3NotRlsedIndi(0);
							laMVFuncTransData.setLienNotRlsedIndi(0);
							laMVFuncTransData.setOthrGovtTtlNo("");
							laMVFuncTransData.setOthrStateCntry("");
							laMVFuncTransData.setOwnrshpEvidCd(0);
							laMVFuncTransData.setSalvYardNo("");
							laMVFuncTransData.setSalvIndi(0);
						}
						else
						{
							if (laSalvageData != null)
							{
								laMVFuncTransData.setJnkCd(
									laSalvageData.getSlvgCd());
								if (laSalvageData.getSlvgDt() != null)
								{
									laMVFuncTransData.setJnkDate(
										laSalvageData
											.getSlvgDt()
											.getYYYYMMDDDate());
								}
								laMVFuncTransData.setLien2NotRlsedIndi(
									laSalvageData
										.getLienNotRlsedIndi2());
								laMVFuncTransData.setLien3NotRlsedIndi(
									laSalvageData
										.getLienNotRlsedIndi3());
								laMVFuncTransData.setLienNotRlsedIndi(
									laSalvageData
										.getLienNotRlsedIndi());
								laMVFuncTransData.setOthrGovtTtlNo(
									laSalvageData.getOthrGovtTtlNo());
								laMVFuncTransData.setOthrStateCntry(
									laSalvageData.getOthrStateCntry());
								laMVFuncTransData.setOwnrshpEvidCd(
									laSalvageData.getOwnrEvdncCd());
								laMVFuncTransData.setSalvYardNo(
									laSalvageData.getSalvYardNo());
								laMVFuncTransData.setSalvIndi(laSalvageData.getSalvIndi());
							}
						} //***********Other Criteria found by testing******************
						if (caTransactionControl
							.getTransCd()
							.equals(TransCdConstant.ADDR)
							|| caTransactionControl.getTransCd().equals(
								TransCdConstant.IADDR))
						{
							laMVFuncTransData.setJnkCd(0);
							laMVFuncTransData.setJnkDate(0);
							laMVFuncTransData.setLien2NotRlsedIndi(0);
							laMVFuncTransData.setLien3NotRlsedIndi(0);
							laMVFuncTransData.setLienNotRlsedIndi(0);
							laMVFuncTransData.setOthrGovtTtlNo("");
							laMVFuncTransData.setOthrStateCntry("");
							laMVFuncTransData.setOwnrshpEvidCd(0);
							laMVFuncTransData.setSalvYardNo("");
						} //*********************************************
						//If req'd, copy MfOwnr information
						// defect 10112 
						if (laOwnerData.isPopulated())
							//	if ((laOwnerData != null)
							//		&& (laOwnerData.getName1() != null)
							//		&& !laOwnerData.getName1().trim().equals(""))
						{
							//		//MVFuncTrans{0}:=MfOwnr{1}
							//		if (laAddressData != null)
							//		{
							//			laMVFuncTransData.setOwnrCity(
							//laAddressData.getCity());
							//			laMVFuncTransData.setOwnrCntry(
							//laAddressData.getCntry());
							//			laMVFuncTransData.setOwnrSt1(
							//laAddressData.getSt1());
							//			laMVFuncTransData.setOwnrSt2(
							//laAddressData.getSt2());
							//			laMVFuncTransData.setOwnrState(
							//laAddressData.getState());
							//			laMVFuncTransData.setOwnrZpCd(
							//laAddressData.getZpcd());
							//			laMVFuncTransData.setOwnrZpCdP4(
							//laAddressData.getZpcdp4());
							//		}
							//		laMVFuncTransData.setOwnrTtlName1(
							//			laOwnerData.getName1());
							//		laMVFuncTransData.setOwnrTtlName2(
							//			laOwnerData.getName2());
							laMVFuncTransData.setOwnerData(laOwnerData);
							// defect 10246
							// No longer set  
							// laOwnerData.setPrivacyOptCd(PRIVACY_OPT_CD);
							// end defect 10246 
						} // end defect 10112
						if (laTitleData != null)
						{
							laMVFuncTransData.setOwnrshpEvidCd(
								laTitleData.getOwnrShpEvidCd());
						} //:Assign MVFuncTrans{0}:=MFVehMisc{0}
						//If Status Change (surrendered title) preserve OthrStateCntry$
						if (caTransactionControl
							.getTransCd()
							.equals(TransCdConstant.STAT))
						{
							if (laSalvageData != null
								&& laTitleData != null)
							{
								laMVFuncTransData.setOthrStateCntry(
									laTitleData.getOthrStateCntry());
							}
						} //PROC Special registration section processig for Titles
						boolean lbBuildSpecialReg = false;
						if (caTransactionControl
							.getEventType()
							.equals(TTL))
						{ // defect 9085
							// remove extra parameters
							// if (isSpecialRegistration(
							//	aaTransHdrData
							//		.getOfcIssuanceCd(),
							//	SystemProperty
							//		.getOfficeIssuanceNo(),  
							if (isSpecialRegistration(laMVFuncTransData
								.getRegClassCd())
								&& laRegistrationData.getExmptIndi() == 0)
								// end defect 9085 
							{
								lbBuildSpecialReg = true;
								// laRegistrationData.setExmptIndi(0);
								// laMVFuncTransData.setExmptIndi(0);
								// defect 6082
								// created a new method to create the TtlNoMf
								//Build TtlNoMf for special registration section
								laMVFuncTransData.setTtlNoMf(
									populateSpecTtlNoMf(
										laRegistrationData
											.getResComptCntyNo(),
										aaTransHdrData.getTransWsId(),
										aaTransHdrData.getTransAMDate(),
										ciTransTime));
								// end defect 6082 
								//Set owner supplied inventory data
								laMVFuncTransData.setOwnrSuppliedExpYr(
									laRegistrationData
										.getOwnrSuppliedExpYr());
								laMVFuncTransData.setOwnrSuppliedPltNo(
									aaCompTransData
										.getOwnrSuppliedPltNo());
							}
						} //Test TtlNoMfIndi for MVFuncTrans{0}.TtlNoMf 
						if (!lbBuildSpecialReg)
						{
							//Build MVFuncTrans{0}.TtlNoMf
							laMVFuncTransData.setTtlNoMf(
								UtilityMethods.addPadding(
									new String[] {
										String.valueOf(
											aaTransHdrData
												.getOfcIssuanceNo()),
										String.valueOf(
											aaTransHdrData
												.getTransWsId()),
										String.valueOf(
											aaTransHdrData
												.getTransAMDate()),
										String.valueOf(ciTransTime)},
									new int[] { 3, 3, 5, 6 },
									"0"));
						} //Check if Additional Trans auth code is needed
						CommonClientBusiness laCommonClientBusiness =
							new CommonClientBusiness();
						//There is a saved vehicle
						if (!(laCommonClientBusiness
							.processData(
								GeneralConstant.COMMON,
								CommonConstant.GET_VEH_MISC,
								null)
							instanceof String))
						{
							if (((ssSavedTransCd != null)
								&& ((ssSavedTransCd
									.equals(TransCdConstant.TITLE)
									|| ssSavedTransCd.equals(
										TransCdConstant.NONTTL)
									|| ssSavedTransCd.equals(
										TransCdConstant.REJCOR)
									|| ssSavedTransCd.equals(
										TransCdConstant.CORTTL))))
								|| (ssSavedAuthCd != null
									&& ssSavedAuthCd.equals(
										CUMULATIVE_TRANS_AUTH_CD)))
							{
								if (laMVFuncTransData.getAuthCd()
									== null
									|| laMVFuncTransData
										.getAuthCd()
										.trim()
										.equals(
										""))
								{
									laMVFuncTransData.setAuthCd(
										CUMULATIVE_TRANS_AUTH_CD);
									ssSavedAuthCd =
										CUMULATIVE_TRANS_AUTH_CD;
								}
							}
						}
					} //end PROC Assign data to MVFuncTrans
					//Save Transcd for next
					setSavedTransCd(aaCompTransData.getTransCode());
					//PROC Assign key fields to MVFuncTrans
					laMVFuncTransData.setCustSeqNo(
						aaTransHdrData.getCustSeqNo());
					laMVFuncTransData.setOfcIssuanceNo(
						aaTransHdrData.getOfcIssuanceNo());
					laMVFuncTransData.setSubstaId(
						aaTransHdrData.getSubstaId());
					laMVFuncTransData.setTransAMDate(
						aaTransHdrData.getTransAMDate());
					laMVFuncTransData.setTransWsId(
						aaTransHdrData.getTransWsId());
					laMVFuncTransData.setTransCd(
						caTransactionControl.getTransCd());
					laMVFuncTransData.setTransTime(ciTransTime);
					// defect 10505 
					laMVFuncTransData.setTransId(getTransId());
					// end defect 10505 
					// defect 7586
					setCacheTransTime(
						laMVFuncTransData,
						ciCacheTransTime);
					// end defect 7586
				}
			} // If HQ, write inventory as owner supplied plate
			if (aaTransHdrData.getOfcIssuanceCd()
				== OFFICE_HEAD_QUARTERS)
			{
				Vector lvInventory = aaCompTransData.getAllocInvItms();
				if (lvInventory != null)
				{
					for (int i = 0; i < lvInventory.size(); i++)
					{
						ProcessInventoryData lProcInvData =
							(ProcessInventoryData) lvInventory.get(i);
						if (lProcInvData.getInvItmYr() != 0)
						{
							laMVFuncTransData.setOwnrSuppliedExpYr(
								lProcInvData.getInvItmYr());
						}
						if (lProcInvData.getItmCd() != null)
						{
							ItemCodesData laItemCodesData =
								ItemCodesCache.getItmCd(
									lProcInvData.getItmCd());
							if (laItemCodesData != null)
							{

								if (laItemCodesData.getItmTrckngType()
									!= null
									&& laItemCodesData
										.getItmTrckngType()
										.equals(
										"P"))
								{
									laMVFuncTransData
										.setOwnrSuppliedPltNo(
										lProcInvData.getInvItmNo());
								}
							}
							else
							{
								throw new RTSException(
									RTSException.JAVA_ERROR,
									new Exception("Item code does not exist in Item code cache"));
							}
						}
						else
						{
							throw new RTSException(
								RTSException.JAVA_ERROR,
								new Exception("Item code is null for inventory"));
						}
					}
				}
			} //Assign key fields to MvFuncTrans
			laMVFuncTransData.setCustSeqNo(
				aaTransHdrData.getCustSeqNo());
			laMVFuncTransData.setOfcIssuanceNo(
				aaTransHdrData.getOfcIssuanceNo());
			laMVFuncTransData.setSubstaId(aaTransHdrData.getSubstaId());
			laMVFuncTransData.setTransAMDate(
				aaTransHdrData.getTransAMDate());
			laMVFuncTransData.setTransWsId(
				aaTransHdrData.getTransWsId());
			laMVFuncTransData.setTransCd(
				aaCompTransData.getTransCode());
			laMVFuncTransData.setTransTime(ciTransTime);
			// defect 6861 
			// Remove redundant code for MF Down  
			// end defect 6861
			//Add MVFuncTrans to Vector
			TransactionCacheData laTransCacheData =
				new TransactionCacheData();
			laTransCacheData.setProcName(TransactionCacheData.INSERT);
			laTransCacheData.setObj(laMVFuncTransData);
			avVector.addElement(laTransCacheData);
		}
	}
	/**
	 * This method formats and verifies the Special Plates TtlNoMf.
	 *
	 * @param aiOffice int 
	 * @param aiWsId   int 
	 * @param aiTransAMDate int 
	 * @param aiTransTime   int
	 * @return String TtlNoMf (docno) for HQ Special Plates transaction
	 * @since RTS v5.1.?
	 */
	public static String populateSpecTtlNoMf(
		int aiOffice,
		int aiWsId,
		int aiTransAMDate,
		int aiTransTime)
	{
		boolean lbContFindUniqueDocNo = true;
		int liNumFound = 1;
		String lsTtlNoMf = "";
		int liTempTransAmDate = aiTransAMDate - 7;
		int liTempTransTime = aiTransTime;
		int liRetryCount = 0;
		int liMaxRetries = 10;
		TitleClientBusiness laTitleClientBusiness =
			new TitleClientBusiness();
		GeneralSearchData laGenSearchData = new GeneralSearchData();
		// Loop to format and verify docno
		while (lbContFindUniqueDocNo)
		{
			lsTtlNoMf =
				UtilityMethods.addPadding(
					new String[] {
						String.valueOf(aiOffice),
						String.valueOf(aiWsId),
						String.valueOf(liTempTransAmDate),
						String.valueOf(liTempTransTime)},
					new int[] { 3, 3, 5, 6 },
					"0");
			// check mainframe to see if there is a duplicate
			laGenSearchData.setKey1(lsTtlNoMf);
			try
			{
				liNumFound =
					((Integer) laTitleClientBusiness
						.processData(
							GeneralConstant.TITLE,
							TitleConstant.GET_NUM_DOC_RECORD,
							laGenSearchData))
						.intValue();
			}
			catch (RTSException aeRTSEx)
			{
				// if there is an exception, try again
				liNumFound = 1;
				liRetryCount = liRetryCount + 1;
			} // leave the loop if there is no match
			// or if we have reached the max number of retries
			if (liNumFound == 0 || liRetryCount == liMaxRetries)
			{
				// There was no match, this is good.
				lbContFindUniqueDocNo = false;
			}
			else
			{
				// increment the time so it is unique
				RTSDate lNewTime = new RTSDate(liTempTransTime + 1);
				liTempTransTime = lNewTime.get24HrTime();
			}
		}
		return lsTtlNoMf;
	}
	/**
	 * Populate RTS_TRANS
	 *
	 * <P>Note: <br>
	 * CUSTLSTNAME, CUSTFSTNAME, CUSTMINAME no longer used <br>
	 * BSNDATE, BSNDATETOTALAMT used only for CLSOUT <br>
	 * Note: Check for Type and Verify
	 *
	 * @param avVector Vector to which Rts_Mv_Func_Trans will be added
	 * @param aaCompTransData CompleteTransactionData
	 * @param aaTransHdr TransactionHeaderData
	 * @throws RTSException
	 */
	public CompleteTransactionData populateTrans(
		Vector avVector,
		CompleteTransactionData aaCompTransData,
		TransactionHeaderData aaTransHdr)
		throws RTSException
	{
		TransactionData laTransData = new TransactionData();
		MFVehicleData laMFVehicleData = null;
		TitleData laTitleData = null;
		OwnerData laOwnerData = null;
		RegistrationData laRegistrationData = null;
		VehicleData laVehicleData = null;
		AddressData laAddressData = null;
		RegTtlAddlInfoData laRegTtlAddlInfoData =
			aaCompTransData.getRegTtlAddlInfoData();
		// defect 10491 
		String lsTransCd = caTransactionControl.getTransCd();
		// end defect 10491 
		// defect 10491 
		// defect 7018
		// Exception Processing for RPRSTK
		//
		if (lsTransCd.equals(TransCdConstant.RPRSTK)
			&& lsTransCd.equals(TransCdConstant.RPRPRM))
		{
			ciTransTime = aaCompTransData.getTransTime();
		} // end defect 7018
		// end defect 10491 
		// defect 6780
		// Adjust transtime for server created transactions
		//  where office timezone is mountain 
		else
		{
			if (Comm.isServer())
			{ //Get time zone for office
				// defect 10427 
				// CacheManagerServerBusiness laCacheManagerServerBusiness =
				//	new CacheManagerServerBusiness();
				// String lsTimeZone =
				//	laCacheManagerServerBusiness.getOfficeTimeZone(
				//		aaTransHdr.getOfcIssuanceNo(),
				//		aaTransHdr.getSubstaId());
				// if (lsTimeZone.equals("M"))
				// {
				//  	ciTransTime = ciTransTime - 10000;
				// }
				if (OfficeTimeZoneCache
					.isMountainTimeZone(aaTransHdr.getOfcIssuanceNo()))
				{
					ciTransTime = ciTransTime - 10000;
				} // end defect 10427 
			}
		} // end defect 6780 
		// defect 10282 
		// Original
		//	if (caTransactionControl
		//		.getTransCd()
		//		.equals(TransCdConstant.DTANTD)
		//		|| caTransactionControl.getTransCd().equals(
		//			TransCdConstant.DTANTK)
		//		|| caTransactionControl.getTransCd().equals(
		//			TransCdConstant.DTAORD)
		//		|| caTransactionControl.getTransCd().equals(
		//			TransCdConstant.DTAORK))
		//	{
		//		laMFVehicleData = caCurrDlrTtlData.getMFVehicleDataFromMF();
		//	}
		//		else
		//		{
		//			
		laMFVehicleData = aaCompTransData.getOrgVehicleInfo();
		//		}
		// end defect 10282 
		if (laMFVehicleData != null)
		{
			laTitleData = laMFVehicleData.getTitleData();
			laOwnerData = laMFVehicleData.getOwnerData();
			laRegistrationData = laMFVehicleData.getRegData();
			laVehicleData = laMFVehicleData.getVehicleData();
		} // defect 5943 
		// Populate VehMk on DelTIP  (Note:  No OrgVehicleInfo())
		else
		{
			if (lsTransCd.equals(TransCdConstant.DELTIP)
				&& aaCompTransData.getVehicleInfo() != null)
			{
				laVehicleData =
					aaCompTransData.getVehicleInfo().getVehicleData();
			}
		} // end defect 5943
		
		// defect 11137
		// Capture Owner Information for Trans table (TransRecon) 
		if (lsTransCd.equals(TransCdConstant.WRENEW))
		{
			MFVehicleData laMfVehData = aaCompTransData.getVehicleInfo();
			if (laMfVehData != null && laMfVehData.getOwnerData() != null)
			{
				laOwnerData = laMfVehData.getOwnerData();
			}
		}
		// end defect 11137  
		
		if (laOwnerData != null)
		{
			laAddressData = laOwnerData.getAddressData();
		}
		MFVehicleData laMFVehicleDataNew = null;
		TitleData laTitleDataNew = null;
		OwnerData laOwnerDataNew = null;
		RegistrationData laRegistrationDataNew = null;
		VehicleData laVehicleDataNew = null;
		AddressData laAddressDataNew = null;
		//New
		laMFVehicleDataNew = aaCompTransData.getVehicleInfo();
		if (laMFVehicleDataNew != null)
		{
			laTitleDataNew = laMFVehicleDataNew.getTitleData();
			laOwnerDataNew = laMFVehicleDataNew.getOwnerData();
			laVehicleDataNew = laMFVehicleDataNew.getVehicleData();
			laRegistrationDataNew = laMFVehicleDataNew.getRegData();
		}
		if (laOwnerDataNew != null)
		{
			// defect 10112 
			laAddressDataNew = laOwnerDataNew.getAddressData();
			// end defect 10112 
		}
		laTransData.setVoidTransCd(aaCompTransData.getVoidTransCd());
		laTransData.setVoidTransTime(
			aaCompTransData.getVoidTransTime());
		laTransData.setVoidOfcIssuanceNo(
			aaCompTransData.getVoidOfcIssuanceNo());
		laTransData.setVoidTransAMDate(
			aaCompTransData.getVoidTransAMDate());
		laTransData.setVoidTransWsId(
			aaCompTransData.getVoidTransWsId());
		// defect 7018
		// If Reprint Sticker Transaction, assign values of the
		// original transaction to RprStkXXX 
		if (lsTransCd.equals(TransCdConstant.RPRSTK)
			|| lsTransCd.equals(TransCdConstant.RPRPRM))
		{
			laTransData.setRprStkOfcIssuanceNo(
				aaCompTransData.getRprStkOfcIssuanceNo());
			laTransData.setRprStkTransWsId(
				aaCompTransData.getRprStkTransWsId());
			laTransData.setRprStkTransAMDate(
				aaCompTransData.getRprStkTransAMDate());
			laTransData.setRprStkTransTime(
				aaCompTransData.getRprStkTransTime());
		} // end defect 7018
		if (laTitleData != null)
		{
			laTransData.setDocNo(laTitleData.getDocNo());
			laTransData.setVehUnitNo(laTitleData.getVehUnitNo());
		}
		if (laOwnerData != null)
		{
			laTransData.setOwnrId(laOwnerData.getOwnrId());
			// defect 10112 
			laTransData.setCustName1(laOwnerData.getName1());
			laTransData.setCustName2(laOwnerData.getName2());
			// end defect 10112 
		}
		if (laAddressData != null)
		{
			laTransData.setCustSt1(laAddressData.getSt1());
			laTransData.setCustSt2(laAddressData.getSt2());
			laTransData.setCustCity(laAddressData.getCity());
			laTransData.setCustState(laAddressData.getState());
			laTransData.setCustZpCd(laAddressData.getZpcd());
			laTransData.setCustZpCdP4(laAddressData.getZpcdp4());
			laTransData.setCustCntry(laAddressData.getCntry());
		}
		if (laRegistrationData != null)
		{
			laTransData.setRegClassCd(
				laRegistrationData.getRegClassCd());
			// defect 9670 
			// If PltRemoved, use RmvRegPltNo vs. RegPltNo
			laTransData.setRegPltNo(
				laRegistrationData.getPltRmvCd() == 0
					? laRegistrationData.getRegPltNo()
					: laRegistrationData.getRmvdRegPltNo());
			// end defect 9670 
			laTransData.setTireTypeCd(
				laRegistrationData.getTireTypeCd());
			laTransData.setVehCaryngCap(
				laRegistrationData.getVehCaryngCap());
			laTransData.setVehGrossWt(
				laRegistrationData.getVehGrossWt());
			laTransData.setRegStkrNo(laRegistrationData.getRegStkrNo());
			laTransData.setDLSCertNo(laRegistrationData.getDlsCertNo());
			laTransData.setEffTime(laRegistrationData.getEffTime());
			laTransData.setEffDate(laRegistrationData.getEffDt());
			laTransData.setExpDate(laRegistrationData.getExpDt());
			laTransData.setExpTime(laRegistrationData.getExpTime());
			laTransData.setVehRegState(
				laRegistrationData.getVehRegState());
		}
		if (laVehicleData != null)
		{
			laTransData.setDieselIndi(laVehicleData.getDieselIndi());
			laTransData.setVehBdyType(laVehicleData.getVehBdyType());
			laTransData.setVehEmptyWt(laVehicleData.getVehEmptyWt());
			laTransData.setVehMk(laVehicleData.getVehMk());
			laTransData.setVehModlYr(laVehicleData.getVehModlYr());
			laTransData.setVIN(laVehicleData.getVin());
		}
		if (laRegTtlAddlInfoData != null)
		{
			laTransData.setProcsdByMailIndi(
				laRegTtlAddlInfoData.getProcsByMailIndi());
		} // Special assignments from REG (P447200) events
		if (caTransactionControl.getEventType().equals(REG)
			&& !caTransactionControl.getTransCd().equals(
				TransCdConstant.WRENEW))
		{
			if (laOwnerDataNew != null)
			{
				// defect 10112 
				laTransData.setCustName1(laOwnerDataNew.getName1());
				laTransData.setCustName2(laOwnerDataNew.getName2());
				// end defect 10112 
			}
			laTransData.setRegPnltyChrgIndi(
				aaCompTransData.getRegPnltyChrgIndi());
		}
		//Special assignments from SBRNW (P447201) events
		// defect 10734 
		else if (
			caTransactionControl.getEventType().equals(SBRNW)
				|| caTransactionControl.getTransCd().equals(
					TransCdConstant.WRENEW))
		{
			//end defect 10734 
			SubcontractorRenewalCacheData laSubconRenewalCacheData =
				aaCompTransData.getSubcontractorRenewalCacheData();
			laTransData.setRegPltNo(
				laSubconRenewalCacheData
					.getTempSubconRenewalData()
					.getRegPltNo());
			laTransData.setRegClassCd(
				Integer.parseInt(
					laSubconRenewalCacheData
						.getTempSubconRenewalData()
						.getRegClassCd()));
			//set Transheader for later restructing Transaction from bundle
			if (laSubconRenewalCacheData.getTransactionHeaderData()
				== null)
			{
				laSubconRenewalCacheData.setTransactionHeaderData(
					aaTransHdr);
			}
			//set Transid for reports
			SubcontractorRenewalData laSubconRenewalData =
				laSubconRenewalCacheData.getTempSubconRenewalData();
			laTransData.setDocNo(
				laSubconRenewalCacheData
					.getTempSubconRenewalData()
					.getDocNo());
			laTransData.setVIN(
				laSubconRenewalCacheData
					.getTempSubconRenewalData()
					.getVIN());
			laSubconRenewalData.setTransID(getTransId());
			//set Transaction key for possible deletion of individual transactions
			TransactionKey laTransKey = new TransactionKey();
			laTransKey.setCustSeqNo(aaTransHdr.getCustSeqNo());
			laTransKey.setOfcIssuanceNo(aaTransHdr.getOfcIssuanceNo());
			laTransKey.setSubstaId(aaTransHdr.getSubstaId());
			laTransKey.setTransAMDate(aaTransHdr.getTransAMDate());
			laTransKey.setTransTime(ciTransTime);
			laTransKey.setTransWsId(aaTransHdr.getTransWsId());
			laSubconRenewalData.setTransactionKey(laTransKey);
		}
		// Special assignments from SPCLOWNR (P447202) events
		// Special assignments from SPCLPLT
		else if (
			caTransactionControl.getEventType().equals(SPCLOWNR)
				|| (caTransactionControl.getEventType().equals(SPCLPLT)))
		{
			if (laOwnerDataNew != null)
			{
				// defect 10112 
				laTransData.setCustName1(laOwnerDataNew.getName1());
				laTransData.setCustName2(laOwnerDataNew.getName2());
				// end defect 10112 
				laTransData.setOwnrId(laOwnerDataNew.getOwnrId());
			}
			if (laAddressDataNew != null)
			{
				laTransData.setCustSt1(laAddressDataNew.getSt1());
				laTransData.setCustSt2(laAddressDataNew.getSt2());
				laTransData.setCustCity(laAddressDataNew.getCity());
				laTransData.setCustState(laAddressDataNew.getState());
				laTransData.setCustZpCd(laAddressDataNew.getZpcd());
				laTransData.setCustZpCdP4(laAddressDataNew.getZpcdp4());
				laTransData.setCustCntry(laAddressDataNew.getCntry());
			}
			if (laRegistrationDataNew != null)
			{
				laTransData.setRegClassCd(
					laRegistrationDataNew.getRegClassCd());
			}
			String lsRegPltNo = new String();
			if (laMFVehicleDataNew != null
				&& laMFVehicleDataNew.getSpclPltRegisData() != null)
			{
				lsRegPltNo =
					laMFVehicleDataNew
						.getSpclPltRegisData()
						.getRegPltNo();
			}
			else if (aaCompTransData.getSpclPltPrmtData() != null)
			{
				lsRegPltNo =
					aaCompTransData.getSpclPltPrmtData().getRegPltNo();
			}

			laTransData.setRegPltNo(lsRegPltNo);
		}
		else if (caTransactionControl.getEventType().equals(TTL))
		{
			if (laOwnerDataNew != null)
			{
				// defect 10112 
				laTransData.setCustName1(laOwnerDataNew.getName1());
				laTransData.setCustName2(laOwnerDataNew.getName2());
				// end defect 10112 
			}
			if (laAddressDataNew != null)
			{
				laTransData.setCustSt1(laAddressDataNew.getSt1());
				laTransData.setCustSt2(laAddressDataNew.getSt2());
				laTransData.setCustCity(laAddressDataNew.getCity());
				laTransData.setCustState(laAddressDataNew.getState());
				laTransData.setCustZpCd(laAddressDataNew.getZpcd());
				laTransData.setCustZpCdP4(laAddressDataNew.getZpcdp4());
				laTransData.setCustCntry(laAddressDataNew.getCntry());
			}
			if (laVehicleDataNew != null)
			{
				laTransData.setVIN(laVehicleDataNew.getVin());
			}
		} // defect 6285 
		// Special assignments for (CREDIT CARD) PAYMENT
		else if (caTransactionControl.getEventType().equals(PYMNT))
		{
			if (laOwnerDataNew != null)
			{
				// defect 10112 
				laTransData.setCustName1(laOwnerDataNew.getName1());
				laTransData.setCustName2(laOwnerDataNew.getName2());
				// end defect 10112 
			}
			if (laAddressDataNew != null)
			{
				laTransData.setCustSt1(laAddressDataNew.getSt1());
				laTransData.setCustSt2(laAddressDataNew.getSt2());
				laTransData.setCustCity(laAddressDataNew.getCity());
				laTransData.setCustState(laAddressDataNew.getState());
				laTransData.setCustZpCd(laAddressDataNew.getZpcd());
				laTransData.setCustZpCdP4(laAddressDataNew.getZpcdp4());
				laTransData.setCustCntry(laAddressDataNew.getCntry());
			}
			if (laVehicleDataNew != null)
			{
				laTransData.setVIN(laVehicleDataNew.getVin());
			}
		} //end defect 6285
		//Special assignments from SLVG (P447211) events
		else if (caTransactionControl.getEventType().equals(SLVG))
		{
			if (laOwnerDataNew != null)
			{
				// defect 10112 
				laTransData.setCustName1(laOwnerDataNew.getName1());
				laTransData.setCustName2(laOwnerDataNew.getName2());
				// end defect 10112 
				laTransData.setOwnrId(laOwnerDataNew.getOwnrId());
			}
			if (laAddressDataNew != null)
			{
				laTransData.setCustSt1(laAddressDataNew.getSt1());
				laTransData.setCustSt2(laAddressDataNew.getSt2());
				laTransData.setCustCity(laAddressDataNew.getCity());
				laTransData.setCustState(laAddressDataNew.getState());
				laTransData.setCustZpCd(laAddressDataNew.getZpcd());
				laTransData.setCustZpCdP4(laAddressDataNew.getZpcdp4());
				laTransData.setCustCntry(laAddressDataNew.getCntry());
			}
			if (laVehicleDataNew != null)
			{
				laTransData.setVIN(laVehicleDataNew.getVin());
			}
		} //Special assignments from SALCOASC (P447213) event
		else if (caTransactionControl.getEventType().equals(SALCOASC))
		{
			if (laVehicleDataNew != null)
			{
				laTransData.setVIN(laVehicleDataNew.getVin());
			}
		} //Special assignments from DTA (P447216) events
		else if (caTransactionControl.getEventType().equals(DTA))
		{
			if (laVehicleDataNew != null)
			{
				laTransData.setVIN(laVehicleDataNew.getVin());
			}
			if (laOwnerDataNew != null)
			{
				// defect 10112 
				laTransData.setCustName1(laOwnerDataNew.getName1());
				laTransData.setCustName2(laOwnerDataNew.getName2());
				// end defect 10112 
			}
			if (laAddressDataNew != null)
			{
				laTransData.setCustSt1(laAddressDataNew.getSt1());
				laTransData.setCustSt2(laAddressDataNew.getSt2());
				laTransData.setCustCity(laAddressDataNew.getCity());
				laTransData.setCustState(laAddressDataNew.getState());
				laTransData.setCustZpCd(laAddressDataNew.getZpcd());
				laTransData.setCustZpCdP4(laAddressDataNew.getZpcdp4());
				laTransData.setCustCntry(laAddressDataNew.getCntry());
			}
		} //Special assignments from MRG (P447220) events
		else if (
			caTransactionControl.getEventType().equals(MRG)
				|| caTransactionControl.getEventType().equals(ADD_DP)
				|| caTransactionControl.getEventType().equals(RPL_DP)
				|| caTransactionControl.getEventType().equals(DEL_DP))
		{
			if (UtilityMethods.isPermitApplication(lsTransCd))
			{
				laTransData.setAddPrmtRecIndi(1);
			}
			// defect 10844
			// Setting both Add & Delete is a flag to MF 
			//  to Replace (Modify) the record 
			else if (lsTransCd.equals(TransCdConstant.MODPT))
			{
				laTransData.setAddPrmtRecIndi(1);
				laTransData.setDelPrmtRecIndi(1);
			}
			// end defect 10844

			TimedPermitData laTimedPermitData =
				aaCompTransData.getTimedPermitData();
			if (laTimedPermitData != null)
			{ // defect 10491 
				//int liHr;
				//String lsTime = null;
				if (laTimedPermitData.getRTSDateEffDt() != null)
				{
					laTransData.setEffDate(
						laTimedPermitData
							.getRTSDateEffDt()
							.getYYYYMMDDDate());
					laTransData.setEffTime(
						laTimedPermitData
							.getRTSDateEffDt()
							.get24HrTime());
					// This was really wrong! 
					//	liHr =
					//		laTimedPermitData.getRTSDateEffDt().getHour();
					//	if (liHr > 12)
					//	{
					//		liHr = liHr - 12;
					//	}
					//	else if (liHr == 0)
					//	{
					//		if (laTimedPermitData
					//			.getTimedPrmtType()
					//			.equals(TransCdConstant.PT30)
					//			|| laTimedPermitData
					//				.getTimedPrmtType()
					//				.equals(
					//				TransCdConstant.FDPT)
					//			|| laTimedPermitData
					//				.getTimedPrmtType()
					//				.equals(
					//				TransCdConstant.OTPT))
					//		{
					//			liHr = 12;
					//		}
					//	}
					//	lsTime =
					//		UtilityMethods.addPadding(
					//			new String[] {
					//				String.valueOf(liHr),
					//				String.valueOf(
					//					laTimedPermitData
					//						.getRTSDateEffDt()
					//						.getMinute()),
					//				String.valueOf(
					//					laTimedPermitData
					//						.getRTSDateEffDt()
					//						.getSecond())},
					//			new int[] { 2, 2, 2 },
					//			"0");
					//	laTransData.setEffTime(Integer.parseInt(lsTime));
					// end defect 10491 
				}
				else
				{
					laTransData.setEffDate(0);
					laTransData.setEffTime(0);
				}
				if (laTimedPermitData.getRTSDateExpDt() != null)
				{
					// defect 10491 
					// Wrong again! 
					//	liHr =
					//		laTimedPermitData.getRTSDateExpDt().getHour();
					//	if (liHr > 12)
					//	{
					//		liHr = liHr - 12;
					//	}
					//	else if (liHr == 0)
					//	{
					//		if (laTimedPermitData
					//			.getTimedPrmtType()
					//			.equals(TransCdConstant.PT30)
					//			|| laTimedPermitData
					//				.getTimedPrmtType()
					//				.equals(
					//				TransCdConstant.FDPT)
					//			|| laTimedPermitData
					//				.getTimedPrmtType()
					//				.equals(
					//				TransCdConstant.OTPT))
					//		{
					//			liHr = 12;
					//		}
					//	}
					//	lsTime =
					//		UtilityMethods.addPadding(
					//			new String[] {
					//				String.valueOf(liHr),
					//				String.valueOf(
					//					laTimedPermitData
					//						.getRTSDateExpDt()
					//						.getMinute()),
					//				String.valueOf(
					//					laTimedPermitData
					//						.getRTSDateExpDt()
					//						.getSecond())},
					//			new int[] { 2, 2, 2 },
					//			"0");
					//laTransData.setExpTime(Integer.parseInt(lsTime));
					laTransData.setExpDate(
						laTimedPermitData
							.getRTSDateExpDt()
							.getYYYYMMDDDate());
					laTransData.setExpTime(
						laTimedPermitData
							.getRTSDateExpDt()
							.get24HrTime());
					// end defect 10491 
				}
				else
				{
					laTransData.setExpDate(0);
					laTransData.setExpTime(0);
				}
				laTransData.setDLSCertNo(
					laTimedPermitData.getDlsCertNo());
				laTransData.setVehRegState(
					laTimedPermitData.getVehRegState());
			}
			if (laOwnerDataNew != null)
			{
				// defect 10112 
				laTransData.setCustName1(laOwnerDataNew.getName1());
				laTransData.setCustName2(laOwnerDataNew.getName2());
				// end defect 10112 
			}
			if (laAddressDataNew != null)
			{
				laTransData.setCustSt1(laAddressDataNew.getSt1());
				laTransData.setCustSt2(laAddressDataNew.getSt2());
				laTransData.setCustCity(laAddressDataNew.getCity());
				laTransData.setCustState(laAddressDataNew.getState());
				laTransData.setCustZpCd(laAddressDataNew.getZpcd());
				laTransData.setCustZpCdP4(laAddressDataNew.getZpcdp4());
				laTransData.setCustCntry(laAddressDataNew.getCntry());
			}
		} //Special assignments from ACC (P447230) events
		else if (caTransactionControl.getEventType().equals(ACC))
		{
			if (laTitleDataNew != null)
			{
				laTransData.setDocNo(laTitleDataNew.getDocNo());
				laTransData.setVehUnitNo(laTitleDataNew.getVehUnitNo());
			}
			if (laOwnerDataNew != null)
			{
				laTransData.setOwnrId(laOwnerDataNew.getOwnrId());
				// defect 10112 
				laTransData.setCustName1(laOwnerDataNew.getName1());
				laTransData.setCustName2(laOwnerDataNew.getName2());
				// end defect 10112 
				laTransData.setCustSt1(null);
				laTransData.setCustSt2(null);
				laTransData.setCustCity(null);
				laTransData.setCustState(null);
				laTransData.setCustZpCd(null);
				laTransData.setCustZpCdP4(null);
				laTransData.setCustCntry(null);
			}
			if (laRegistrationDataNew != null)
			{
				laTransData.setRegClassCd(
					laRegistrationDataNew.getRegClassCd());
				laTransData.setRegPltNo(
					laRegistrationDataNew.getRegPltNo());
				laTransData.setTireTypeCd(
					laRegistrationDataNew.getTireTypeCd());
				laTransData.setVehCaryngCap(
					laRegistrationDataNew.getVehCaryngCap());
				laTransData.setVehGrossWt(
					laRegistrationDataNew.getVehGrossWt());
				laTransData.setRegStkrNo(
					laRegistrationDataNew.getRegStkrNo());
				laTransData.setDLSCertNo(
					laRegistrationDataNew.getDlsCertNo());
				laTransData.setEffTime(
					laRegistrationDataNew.getEffTime());
				laTransData.setEffDate(
					laRegistrationDataNew.getEffDt());
				laTransData.setExpDate(
					laRegistrationDataNew.getExpDt());
				laTransData.setExpTime(
					laRegistrationDataNew.getExpTime());
				laTransData.setVehRegState(
					laRegistrationDataNew.getVehRegState());
			}
			if (laVehicleDataNew != null)
			{
				laTransData.setDieselIndi(
					laVehicleDataNew.getDieselIndi());
				laTransData.setVehBdyType(
					laVehicleDataNew.getVehBdyType());
				laTransData.setVehEmptyWt(
					laVehicleDataNew.getVehEmptyWt());
				laTransData.setVehMk(laVehicleDataNew.getVehMk());
				laTransData.setVehModlYr(
					laVehicleDataNew.getVehModlYr());
				laTransData.setVIN(laVehicleDataNew.getVin());
			}
		} //Special assignments from VEHINQ (P447240) events
		else if (caTransactionControl.getEventType().equals(VEHINQ))
		{
			// defect 10590
			//if (laOwnerDataNew != null)
			//{
			// defect 10112 
			// Implement new OwnerData object 
			laTransData.setCustName1(aaCompTransData.getCustName());
			// laOwnerDataNew.setName1(laOwnerData.getName1());
			// end defect 10112
			//}
			// end defect 10590
			laTransData.setCustSt1(null);
			laTransData.setCustSt2(null);
			laTransData.setCustCity(null);
			laTransData.setCustState(null);
			laTransData.setCustZpCd(null);
			laTransData.setCustZpCdP4(null);
		} // INV events do not use this code 
		//Special assignments from INV (P447301) events
		//else if (caTransactionControl.getEventType().equals(INV))
		//{
		//do nothing
		//}
		//Special assignments from CLSOUT (P447500) event
		else if (caTransactionControl.getEventType().equals(CLSOUT))
		{
			laTransData = new TransactionData();
			laTransData.setEffDate(aaTransHdr.getTransAMDate());
			laTransData.setEffTime(ciTransTime);
			laTransData.setCashWsId(aaCompTransData.getCashWsid());
		} //Special assignments from VOID (P447900) events
		else if (caTransactionControl.getEventType().equals(VOID))
		{
			//Retrieve Void Transaction
			TransactionData laTempTransData = new TransactionData();
			laTempTransData.setOfcIssuanceNo(
				aaCompTransData.getVoidOfcIssuanceNo());
			laTempTransData.setSubstaId(
				aaCompTransData.getVoidSubstaId());
			laTempTransData.setTransAMDate(
				aaCompTransData.getVoidTransAMDate());
			laTempTransData.setTransWsId(
				aaCompTransData.getVoidTransWsId());
			laTempTransData.setCustSeqNo(
				aaCompTransData.getVoidCustSeqNo());
			laTempTransData.setTransTime(
				aaCompTransData.getVoidTransTime());
			MiscClientBusiness laMiscClientBusiness =
				new MiscClientBusiness();
			laTransData =
				(TransactionData) laMiscClientBusiness.processData(
					GeneralConstant.MISC,
					MiscellaneousConstant.GET_VOID_RTS_TRANS,
					laTempTransData);
			//Set the new Void Transaction
			laTransData.setVoidedTransIndi(0);
			laTransData.setTransPostedMfIndi(0);
			laTransData.setVoidTransCd(laTransData.getTransCd());
			laTransData.setVoidOfcIssuanceNo(
				aaCompTransData.getVoidOfcIssuanceNo());
			laTransData.setVoidTransWsId(
				aaCompTransData.getVoidTransWsId());
			laTransData.setVoidTransAMDate(
				aaCompTransData.getVoidTransAMDate());
			laTransData.setVoidTransTime(
				aaCompTransData.getVoidTransTime());
			// defect 10501 
			String lsVoidTransId =
				UtilityMethods.getTransId(
					aaCompTransData.getVoidOfcIssuanceNo(),
					aaCompTransData.getVoidTransWsId(),
					aaCompTransData.getVoidTransAMDate(),
					aaCompTransData.getVoidTransTime());
			laTransData.setVoidTransId(lsVoidTransId);
			// end defect 10501 
		} //Special assignments from CCO/CCDO (P447212) events
		if (caTransactionControl.getEventType().equals(CCOCCDO))
		{
			if (laOwnerDataNew == null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					"New OwnerData is null",
					"Error");
			} // defect 10112
			laTransData.setCustName1(laOwnerDataNew.getName1());
			// end defect 10112 
		} //Special assignments from REJREL (P447214) events
		if (caTransactionControl.getEventType().equals(REJREL))
		{
			if (laVehicleDataNew != null)
			{
				laTransData.setVIN(laVehicleDataNew.getVin());
			}
			if (laTitleDataNew != null)
			{
				laTransData.setDocNo(laTitleDataNew.getDocNo());
			}
		} // Populate Trans Header Data
		laTransData.setCustSeqNo(aaTransHdr.getCustSeqNo());
		laTransData.setOfcIssuanceNo(aaTransHdr.getOfcIssuanceNo());
		laTransData.setSubstaId(aaTransHdr.getSubstaId());
		laTransData.setTransAMDate(aaTransHdr.getTransAMDate());
		laTransData.setTransCd(caTransactionControl.getTransCd());
		laTransData.setTransWsId(aaTransHdr.getTransWsId());
		laTransData.setTransTime(ciTransTime);
		// defect 10505 
		laTransData.setTransId(getTransId());
		// end defect 10505 
		// defect 9711
		// defect 10401
		// TODO this should be done better.
		if (aaCompTransData
			.getTransCode()
			.equals(TransCdConstant.VPAPPL)
			|| aaCompTransData.getTransCode().equals(
				TransCdConstant.VPAPPR)
			|| aaCompTransData.getTransCode().equals(
				TransCdConstant.VPDEL)
			|| aaCompTransData.getTransCode().equals(
				TransCdConstant.VPPORT)
			|| aaCompTransData.getTransCode().equals(
				TransCdConstant.VPREDO)
			|| aaCompTransData.getTransCode().equals(
				TransCdConstant.VPREV)
			|| aaCompTransData.getTransCode().equals(
				TransCdConstant.VPRSRV)
			|| aaCompTransData.getTransCode().equals(
				TransCdConstant.VPRSTL)
			|| aaCompTransData.getTransCode().equals(
				TransCdConstant.VPUNAC))
		{
			// end defect 10401
			SpecialPlatesRegisData laSpclPltRegisData =
				aaCompTransData.getVehicleInfo().getSpclPltRegisData();
			laTransData.setTransEmpId(
				laSpclPltRegisData.getTransEmpId());
		}
		else
		{
			laTransData.setTransEmpId(aaTransHdr.getTransEmpId());
		} // defect 9711
		laTransData.setTransPostedMfIndi(0);
		// defect 7586
		setCacheTransTime(laTransData, ciCacheTransTime);
		// end defect 7586 
		//Other data
		laTransData.setVersionCd(aaTransHdr.getVersionCd());
		// defect 5512
		// only write this message in developer environment on client side
		if (!Comm.isServer() && SystemProperty.getProdStatus() != 0)
		{
			System.out.println(
				"CustSeqNo="
					+ aaTransHdr.getCustSeqNo()
					+ " and TransAMDate="
					+ aaTransHdr.getTransAMDate()
					+ " and TransTime="
					+ ciTransTime);
		} //Add TransactionData to Vector
		TransactionCacheData laTransCacheData =
			new TransactionCacheData();
		laTransCacheData.setProcName(TransactionCacheData.INSERT);
		laTransCacheData.setObj(laTransData);
		avVector.addElement(laTransCacheData);
		return aaCompTransData;
	}
	/**
	 * Populates TransactionCacheData objects and returns a vector of  
	 * TransactionCacheData objects to the calling method. This method 
	 * takes CompleteTransactionData object and TransHdrData object 
	 * as arguments.
	 * 
	 * @param  aaCompTransData CompleteTransactionData
	 * @param  aaTransHdrData  TransactionHeaderData
	 * @return Vector of TransactionCacheData objects 
	 * @throws RTSException
	 */
	private Vector populateTransData(
		CompleteTransactionData aaCompTransData,
		TransactionHeaderData aaTransHdrData)
		throws RTSException
	{
		Vector lvVector = new Vector();
		TransactionHeaderData laTransHdrData = null;
		if (aaTransHdrData != null)
		{
			TransactionCacheData laTransCacheData =
				new TransactionCacheData();
			laTransCacheData.setProcName(TransactionCacheData.INSERT);
			laTransCacheData.setObj(aaTransHdrData);
			lvVector.addElement(laTransCacheData);
			laTransHdrData = aaTransHdrData;
		}
		else
		{
			laTransHdrData = saTransHdrData;
		} // defect 7586
		if (ciSavedTransTime != 0)
		{
			ciCacheTransTime = ciTransTime;
			ciTransTime = ciSavedTransTime;
			ciSavedTransTime = 0;
		} // end defect 7586
		//Move data to Trans Table
		aaCompTransData =
			populateTrans(lvVector, aaCompTransData, laTransHdrData);
		//Move data to MvFuncTrans
		populateMvTrans(lvVector, aaCompTransData, laTransHdrData);
		//Move data to InvFuncTrans and TrInvDetail
		populateInvFuncTransTrInvDetail(
			lvVector,
			aaCompTransData,
			laTransHdrData);
		//Move data to FundFuncTrns and TrFdsDetail
		populateFundFuncTrnsTrFdsDetail(
			lvVector,
			aaCompTransData,
			laTransHdrData);
		//populate vector with internet group related tables
		populateInternetDbTables(lvVector, aaCompTransData);
		// defect 8900  
		// Exempt Audit Data
		populateExemptAudit(lvVector, aaCompTransData, laTransHdrData);
		// end defect 8900 
		// defect 9085
		// Special Registration Function Transaction
		populateSRFuncTrans(lvVector, aaCompTransData, laTransHdrData);
		// end defect 9085 
		// defect 9831 
		populateDsabldPlcrdTrans(
			lvVector,
			aaCompTransData,
			laTransHdrData);
		// end defect 9831 
		// defect 9972 
		populateETtlHstry(lvVector, aaCompTransData, laTransHdrData);
		// end defect 9972
		// defect 10491  
		populatePrmtTrans(lvVector, aaCompTransData, laTransHdrData);
		// end defect 10491
		// defect 10636
		// Move update of Virtual Inventory to match delete 
		updateSpclPltVITransTime(lvVector, aaCompTransData);
		updatePrmtVITransTime(lvVector, aaCompTransData);
		// end defect 10636 
		ssSavedTransCd = aaCompTransData.getTransCode();
		return lvVector;
	}

	/**
	 * Populate the TransactionPaymentData with the required table information
	 *
	 * @return Returns a vector of TransactionCacheData objects
	 * @param avTransPaymentData
	 * @param aaTransHdrData
	 */
	private Vector populateTransData(
		Vector avTransPaymentData,
		TransactionHeaderData aaTransHdrData,
		CreditCardFeeData aaCreditCardFeeData)
		throws RTSException
	{
		Vector lvVector = new Vector();
		// defect 10348 
		if (avTransPaymentData != null
			&& avTransPaymentData.size() != 0)
		{
			// end defect 10348 
			int liPymntNo = 1;
			// defect 10348 
			Dollar laTotalPayment = new Dollar(0);
			// end defect 10348 
			for (int i = 0; i < avTransPaymentData.size(); i++)
			{
				// defect 10348 
				TransactionPaymentData laTransPaymentData =
					(TransactionPaymentData) UtilityMethods.copy(
						avTransPaymentData.get(i));
				// end defect 10348 
				if (laTransPaymentData != null)
				{
					// defect 10348 
					// Determine Total Payment  
					laTotalPayment =
						laTotalPayment.addNoRound(
							laTransPaymentData.getPymntTypeAmt());
					if (laTransPaymentData.getChngDue() != null)
					{
						laTotalPayment =
							laTotalPayment.subtractNoRound(
								laTransPaymentData.getChngDue());
					} // end defect 10348  
					laTransPaymentData.setCustSeqNo(
						aaTransHdrData.getCustSeqNo());
					laTransPaymentData.setOfcIssuanceNo(
						aaTransHdrData.getOfcIssuanceNo());
					laTransPaymentData.setPymntNo(
						UtilityMethods.addPadding(
							String.valueOf(liPymntNo),
							2,
							"0"));
					laTransPaymentData.setSubstaId(
						aaTransHdrData.getSubstaId());
					laTransPaymentData.setTransAMDate(
						aaTransHdrData.getTransAMDate());
					laTransPaymentData.setTransWsId(
						aaTransHdrData.getTransWsId());
					laTransPaymentData.setTransTime(ciTransTime);
					TransactionCacheData laTransCacheData =
						new TransactionCacheData();
					laTransCacheData.setObj(laTransPaymentData);
					laTransCacheData.setProcName(
						TransactionCacheData.INSERT);
					lvVector.addElement(laTransCacheData);
					++liPymntNo;
				}
			}
			// defect 10348 
			// Log all Transaction Total / Payment Info 
			// Throw error when payment data incorrect, e.g. *10 + 3  
			if (saRunningSubtotal != null
				&& saRunningSubtotal.compareTo(new Dollar(0)) != 0)
			{
				Dollar laCompareSubtotal =
					(Dollar) UtilityMethods.copy(saRunningSubtotal);

				String lsCreditCardFee = new String();

				if (aaCreditCardFeeData != null)
				{
					laCompareSubtotal =
						laCompareSubtotal.add(
							aaCreditCardFeeData.getItmPrice());

					lsCreditCardFee =
						" (Subtotal: "
							+ saRunningSubtotal
							+ " / CreditCardFee "
							+ aaCreditCardFeeData.getItmPrice()
							+ ")";
				}
				String lsCSN =
					UtilityMethods.addPadding(
						"" + aaTransHdrData.getCustSeqNo(),
						4,
						"0");

				String lsMsg =
					"CSN"
						+ lsCSN
						+ " - Payment: "
						+ laTotalPayment
						+ " / Total Fees: "
						+ laCompareSubtotal
						+ lsCreditCardFee;

				System.out.println(lsMsg);

				if (laTotalPayment.compareTo(laCompareSubtotal) != 0)
				{
					throw new RTSException(
						ErrorsConstant.ERR_NUM_PAYMENT_IN_ERROR);
				}
			} // end defect 10348 
		}

		//SQL S700 Update Transtimestmp in RTS_Trans_Hdr (Completes Customer Transaction)
		TransactionHeaderData laTransHdrData =
			new TransactionHeaderData();
		laTransHdrData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laTransHdrData.setSubstaId(aaTransHdrData.getSubstaId());
		laTransHdrData.setTransAMDate(aaTransHdrData.getTransAMDate());
		laTransHdrData.setTransWsId(aaTransHdrData.getTransWsId());
		laTransHdrData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laTransHdrData.setTransTime(ciTransTime);
		// defect 7255
		// Taking away an hour from trans header date if Time Zone
		// is mountain time. This is a temp fix for XP. Temp Fix
		RTSDate laNewRTSDate = new RTSDate();
		if (!Comm.isServer())
		{ // defect 10427 
			//			try
			//			{
			//				AssignedWorkstationIdsData laAssgndWsIdsData =
			//					AssignedWorkstationIdsCache.getAsgndWsId(
			//						aaTransHdrData.getOfcIssuanceNo(),
			//						aaTransHdrData.getSubstaId(),
			//						aaTransHdrData.getTransWsId());
			//				if (laAssgndWsIdsData
			//					.getTimeZone()
			//					.equalsIgnoreCase("M"))
			//				{
			//					laNewRTSDate.setHour(laNewRTSDate.getHour() - 1);
			//				}
			//			}
			//			catch (RTSException aeRTSEx)
			//			{
			//			}
			if (OfficeTimeZoneCache
				.isMountainTimeZone(aaTransHdrData.getOfcIssuanceNo()))
			{
				laNewRTSDate.setHour(laNewRTSDate.getHour() - 1);
			} // end defect 10427
		}
		laTransHdrData.setTransTimestmp(laNewRTSDate);
		// end defect 7255
		TransactionCacheData laTransCacheData =
			new TransactionCacheData();
		laTransCacheData.setObj(laTransHdrData);
		laTransCacheData.setProcName(TransactionCacheData.UPDATE);
		lvVector.addElement(laTransCacheData);
		// defect 8900 
		// Add update for Exempt Audit updates 
		ExemptAuditData laExmptAuditData = new ExemptAuditData();
		laExmptAuditData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laExmptAuditData.setSubstaId(aaTransHdrData.getSubstaId());
		laExmptAuditData.setTransWsId(aaTransHdrData.getTransWsId());
		laExmptAuditData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laExmptAuditData.setTransTime(ciTransTime);
		laExmptAuditData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laExmptAuditData.setTransCompleteIndi(1);
		TransactionCacheData laTransCacheData2 =
			new TransactionCacheData();
		laTransCacheData2.setObj(laExmptAuditData);
		laTransCacheData2.setProcName(TransactionCacheData.UPDATE);
		lvVector.addElement(laTransCacheData2);
		// end defect 8900
		// defect 9085 
		// Add update for Special Plate Trans History 
		SpecialPlateTransactionHistoryData laSpclPltTransHstryData =
			new SpecialPlateTransactionHistoryData();
		laSpclPltTransHstryData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laSpclPltTransHstryData.setSubstaId(
			aaTransHdrData.getSubstaId());
		laSpclPltTransHstryData.setTransWsId(
			aaTransHdrData.getTransWsId());
		laSpclPltTransHstryData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laSpclPltTransHstryData.setTransTime(ciTransTime);
		laSpclPltTransHstryData.setCustSeqNo(
			aaTransHdrData.getCustSeqNo());
		laSpclPltTransHstryData.setTransCompleteIndi(1);
		TransactionCacheData laTransCacheData3 =
			new TransactionCacheData();
		laTransCacheData3.setObj(laSpclPltTransHstryData);
		laTransCacheData3.setProcName(TransactionCacheData.UPDATE);
		lvVector.addElement(laTransCacheData3);
		// end defect 9085		
		// defect 9831 
		// Add update to Disabled Placard Trans 
		DisabledPlacardTransactionData laDsabldPlcrdTransData =
			new DisabledPlacardTransactionData();
		laDsabldPlcrdTransData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laDsabldPlcrdTransData.setSubstaId(
			aaTransHdrData.getSubstaId());
		laDsabldPlcrdTransData.setTransWsId(
			aaTransHdrData.getTransWsId());
		laDsabldPlcrdTransData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laDsabldPlcrdTransData.setTransTime(ciTransTime);
		laDsabldPlcrdTransData.setCustSeqNo(
			aaTransHdrData.getCustSeqNo());
		laDsabldPlcrdTransData.setTransTimestmp(
			laTransHdrData.getTransTimestmp());
		TransactionCacheData laTransCacheData4 =
			new TransactionCacheData();
		laTransCacheData4.setObj(laDsabldPlcrdTransData);
		laTransCacheData4.setProcName(TransactionCacheData.UPDATE);
		lvVector.addElement(laTransCacheData4);
		// end defect 9831 
		// defect 9972 
		ElectronicTitleHistoryData laETtlHstryData =
			new ElectronicTitleHistoryData();
		laETtlHstryData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laETtlHstryData.setSubstaId(aaTransHdrData.getSubstaId());
		laETtlHstryData.setTransWsId(aaTransHdrData.getTransWsId());
		laETtlHstryData.setTransAMDate(aaTransHdrData.getTransAMDate());
		laETtlHstryData.setTransTime(ciTransTime);
		laETtlHstryData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		laETtlHstryData.setTransCompleteIndi(1);
		TransactionCacheData laTransCacheData5 =
			new TransactionCacheData();
		laTransCacheData5.setObj(laETtlHstryData);
		laTransCacheData5.setProcName(TransactionCacheData.UPDATE);
		lvVector.addElement(laTransCacheData5);
		// end defect 9972 	

		// defect 10844 
		ModifyPermitTransactionHistoryData laModPrmtTransHstryData =
			new ModifyPermitTransactionHistoryData();
		laModPrmtTransHstryData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		laModPrmtTransHstryData.setSubstaId(
			aaTransHdrData.getSubstaId());
		laModPrmtTransHstryData.setTransWsId(
			aaTransHdrData.getTransWsId());
		laModPrmtTransHstryData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		laModPrmtTransHstryData.setTransTime(ciTransTime);
		laModPrmtTransHstryData.setCustSeqNo(
			aaTransHdrData.getCustSeqNo());
		laModPrmtTransHstryData.setTransCompleteIndi(1);
		TransactionCacheData laTransCacheData6 =
			new TransactionCacheData();
		laTransCacheData6.setObj(laModPrmtTransHstryData);
		laTransCacheData6.setProcName(TransactionCacheData.UPDATE);
		lvVector.addElement(laTransCacheData6);
		// end defect 10844

		return lvVector;
	}
	/** 
	 * Populate Special Plate Permit Data w/ required data; Add to 
	 * Transaction Cache 
	 * 
	 * @param avVector 
	 * @param aaCTData
	 * @param aaTransHdrData 
	 */
	public void populateSpclPltPrmt(
		Vector avVector,
		CompleteTransactionData aaCTData,
		TransactionHeaderData aaTransHdrData)
	{
		aaCTData.setSpclPltPrmtData(aaTransHdrData, ciTransTime);
		TransactionCacheData laTransCacheData =
			new TransactionCacheData();
		laTransCacheData.setProcName(TransactionCacheData.INSERT);
		laTransCacheData.setObj(aaCTData.getSpclPltPrmtData());
		avVector.addElement(laTransCacheData);
	}
	/**
	 * This method calls server side postTransaction method with vector 
	 * of TransactionCacheData object. A failure here returns an object
	 * of type boolean (false).
	 * 
	 * @param avTransCacheData Vector 
	 * @return Object CompleteTransactionData if successful, Object
	 * 		boolean (false) if not.
	 * @throws RTSException
	 */
	public static Object postTrans(Vector avTransCacheData)
		throws RTSException
	{
		Object laReturn = null;
		try
		{
			Vector lvInput = new Vector();
			lvInput.addElement(null);
			lvInput.addElement(avTransCacheData);
			laReturn =
				Comm.sendToServer(
					GeneralConstant.COMMON,
					CommonConstant.POST_TRANS,
					lvInput);
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getMsgType() != null
				&& (aeRTSEx.getMsgType().equals(RTSException.DB_DOWN)
					|| aeRTSEx.getMsgType().equals(
						RTSException.SERVER_DOWN)))
			{
				return new Boolean(false);
			}
			else
			{
				throw aeRTSEx;
			}
		}
		return laReturn;
	}
	/**
	 * Tasks that need to be done after a transaction is completed
	 * 
	 * @param  aaCompTransData CompleteTransactionData
	 * @throws RTSException 
	 */
	private void postTransaction() throws RTSException
	{
		if (saCompTransData
			.getTransCode()
			.equals(TransCdConstant.TITLE)
			|| saCompTransData.getTransCode().equals(
				TransCdConstant.CORTTL)
			|| saCompTransData.getTransCode().equals(
				TransCdConstant.REJCOR)
			|| saCompTransData.getTransCode().equals(
				TransCdConstant.DTAORD)
			|| saCompTransData.getTransCode().equals(
				TransCdConstant.DTAORK))
		{
			TitleBatchGenerator.getInstance().increment();
		}
	}
	/**
	 * Tasks that need to be done before a transaction starts.
	 * 
	 * @throws RTSException
	 */
	private void preTransaction() throws RTSException
	{
		if (saCompTransData
			.getTransCode()
			.equals(TransCdConstant.TITLE)
			|| saCompTransData.getTransCode().equals(
				TransCdConstant.CORTTL)
			|| saCompTransData.getTransCode().equals(
				TransCdConstant.REJCOR)
			|| saCompTransData.getTransCode().equals(
				TransCdConstant.DTAORD)
			|| saCompTransData.getTransCode().equals(
				TransCdConstant.DTAORK))
		{
			String lsBatchNo =
				UtilityMethods.addPadding(
					new String[] {
						String.valueOf(
							SystemProperty.getWorkStationId()),
						String.valueOf(new RTSDate().getAMDate()),
						String.valueOf(
							TitleBatchGenerator
								.getInstance()
								.getBatchNo())},
					new int[] { 3, 5, 2 },
					"0");
			saCompTransData.setBatchCount(
				String.valueOf(
					TitleBatchGenerator.getInstance().getCount()));
			saCompTransData.getVehicleInfo().getTitleData().setBatchNo(
				lsBatchNo);
		}
		if (saCompTransData
			.getTransCode()
			.equals(TransCdConstant.RENEW))
		{
			MFVehicleData laMFVehicleData =
				saCompTransData.getVehicleInfo();
			if (laMFVehicleData != null)
			{
				RegistrationData laRegData =
					laMFVehicleData.getRegData();
				if (laRegData != null)
				{
					laRegData.setPltSeizedIndi(0);
					laRegData.setStkrSeizdIndi(0);
					laRegData.setRegHotCkIndi(0);
					laRegData.setRegInvldIndi(0);
					laRegData.setRegRefAmt(null);
					laRegData.setNotfyngCity(null);
					laRegData.setEmissionSourceCd(null);
				}
			}
		}
		else if (
			saCompTransData.getTransCode().equals(TransCdConstant.EXCH)
				|| saCompTransData.getTransCode().equals(
					TransCdConstant.REPL))
		{
			MFVehicleData laMFVehicleData =
				saCompTransData.getVehicleInfo();
			if (laMFVehicleData != null)
			{
				RegistrationData laRegData =
					laMFVehicleData.getRegData();
				if (laRegData != null)
				{
					laRegData.setPltSeizedIndi(0);
					laRegData.setStkrSeizdIndi(0);
					laRegData.setRegHotCkIndi(0);
				}
			}
		}
	}
	/**
	 * Print all the receipts saved for the transaction
	 *
	 * @throws RTSException
	 */
	public void printAllReceipts() throws RTSException
	{
		String lsCustSeqNo = getPaddedCustSeqNo();
		String lsReceiptRootDir = SystemProperty.getReceiptsDirectory();
		String lsLocationForReceipt =
			lsReceiptRootDir + CSN + lsCustSeqNo + BACK_SLASH;
		Print laPrint = new Print();
		File laRcptLog = new File(lsLocationForReceipt + "rcptlog");
		// defect 7624
		// Subcon & DTA may not print receipts
		if (laRcptLog.exists())
		{
			Vector lvFiles = new Vector();
			FileReader laFileReader = null;
			BufferedReader laBuffRdr = null;
			String lsLineIn = "";
			try
			{
				laFileReader = new FileReader(laRcptLog);
				laBuffRdr = new BufferedReader(laFileReader);
				while ((lsLineIn = laBuffRdr.readLine()) != null)
				{
					ReportSearchData laRptSearchData =
						new ReportSearchData();
					int liCommaIndex1 = lsLineIn.indexOf(",");
					laRptSearchData.setKey1(
						lsLocationForReceipt
							+ lsLineIn.substring(0, liCommaIndex1));
					// defect 4443
					// do not try to print if file does not exist.
					if (laRptSearchData.getKey1().length() < 21)
					{
						continue;
					}
					//end defect 4443

					// defect 10844
					// Strip off PrmIssuanceId to grab TransCd 
					int liLastIndex = lsLineIn.lastIndexOf(",");
					lsLineIn = lsLineIn.substring(0, liLastIndex);
					liLastIndex = lsLineIn.lastIndexOf(",");
					// end defect 10844

					laRptSearchData.setKey2(
						lsLineIn.substring(
							liLastIndex + 1,
							lsLineIn.length()));
					lvFiles.add(laRptSearchData);
				}
			}
			catch (IOException aeIOEx)
			{
				aeIOEx.printStackTrace();
			}
			for (int i = 0; i < lvFiles.size(); i++)
			{
				ReportSearchData lRSearchData =
					(ReportSearchData) lvFiles.get(i);
				
				// defect 11052 
				if (!lRSearchData.getKey1().endsWith(".JPG"))
				{
					laPrint.sendToPrinter(
							lRSearchData.getKey1(),
							lRSearchData.getKey2());
				}
				else
				{
					(new PrintGraphics()).printForm275JPG(lRSearchData.getKey1());
				}
				// end defect 11052 
			}
		} // end defect 7624 
	}
	/**
	 * Print the Document
	 *
	 * @param  abEndTrans boolean
	 * @return boolean
	 * @throws RTSException
	 */
	private boolean printDocument(boolean abEndTrans)
		throws RTSException
	{
		try
		{
			if (saCompTransData == null)
			{
				return false;
			}
			Vector lvVector = new Vector();
			lvVector.add(saCompTransData);
			Print laPrint = new Print();
			ReportSearchData laRptSearchData = new ReportSearchData();
			String lsTransCd = caTransactionControl.getTransCd();
			// defect 9643
			String lsRptFileName = "";
			String lsBarcodeFileName = "";
			String lsFormattedReport = "";
			String lsQualifiedRptFileName = "";
			// end defect 9643 
			if (lsTransCd.equals(TransCdConstant.CCO))
			{
				if (abEndTrans && SystemProperty.isRegion())
				{
					return false;
				}
				GenTitleCCOReports laGenRpt = new GenTitleCCOReports();
				laGenRpt.formatReport(lvVector);
				// defect 7826
				// Add printer reset at the beginning of all salvage 
				// certificates
				lsFormattedReport =
					laGenRpt.getFormattedReport().toString();
				// defect 10398
				String lsPrinterProps =
					Print.getPRINT_RESET()
						+ Print.getPRINT_TRAY_3()
						+ Print.getPRINT_PICA()
						+ Print.getPRINT_BOLD()
						+ Print.getPRINT_LINE_SPACING_1_8()
					//						+ Print.getPRINT_PAGE_LENGTH_88();
	+Print.getCUSTOM_SIZE();
				// end defect 10398
				// end defect 7826
				// defect 8524
				// FormFeed constant cleanup 
				laRptSearchData.setKey1(
					lsPrinterProps
						+ lsFormattedReport
						+ ReportConstant.FF);
				// end defect 8524
				laRptSearchData.setKey2(TransCdConstant.CCO);
				laRptSearchData.setIntKey1(0);
				lsQualifiedRptFileName =
					(String) saveReports(laRptSearchData);
				laPrint.sendToPrinter(lsQualifiedRptFileName);
			} // defect 9643 
			// process Salvage
			//			else if (
			//lsTransCd.equals(TransCdConstant.SLVG)
			//	|| lsTransCd.equals(TransCdConstant.DPLSLG))
			//			{
			//if (!abEndTrans)
			//{
			//	GenSC laGenRpt = new GenSC();
			//	laGenRpt.formatReport(lvVector);
			//	String lsReport =
			//		laGenRpt.getFormattedReport().toString();
			//	// defect 7184
			//	// Added tray 1 definition to force the salvage title doc
			//	// to pull from tray 1
			//	// Add printer reset at the beginning of all salvage 
			//	// certificates
			//	// defect 7826
			//	String lsPrinter =
			//		Print.getPRINT_RESET()
			//			+ Print.getPRINT_TRAY_1()
			//			+ Print.getPRINT_PICA()
			//			+ Print.getPRINT_LINE_SPACING_1_6()
			//			+ Print.getPRINT_PAGE_LENGTH_48();
			//	// end defect 7826
			//	// end defect 7184
			//	laRptSearchData.setKey1(
			//		lsPrinter + lsReport + ReportConstant.FF);
			//	laRptSearchData.setKey2(SALV);
			//	laRptSearchData.setIntKey1(0);
			//	String lsFileName =
			//		(String) saveReports(laRptSearchData);
			//	laPrint.sendToPrinter(lsFileName);
			//}
			//			} 
			//
			// Add CORSLV, CORNRT processing 
			// Process SCOT, CCOSCT, CORSLV
			else if (
				lsTransCd.equals(TransCdConstant.SCOT)
					|| lsTransCd.equals(TransCdConstant.CCOSCT)
					|| lsTransCd.equals(TransCdConstant.CORSLV))
				// defect 6234
				// print the receipt before certificate per VTR
			{
				GenSCOT laGenRpt = null;
				lvVector.add("");
				// Set file name for Salvage Vehicle Title  
				// Default to 'SALV'; Also 'SALVCCO','SALVCOR'
				lsRptFileName = SALV;
				if (lsTransCd.equals(TransCdConstant.CCOSCT))
				{
					lsRptFileName = lsRptFileName + "CCO";
				}
				else if (lsTransCd.equals(TransCdConstant.CORSLV))
				{
					lsRptFileName = lsRptFileName + "COR";
				}
				lsBarcodeFileName = lsRptFileName + "B";
				// defect 6173
				// Do not print a receipt for CCOSCT  
				//if (!lsTransCd.equals(TransCdConstant.CCOSCT))
				//{
				// end defect 6173 
				laGenRpt = new GenSCOT();
				lvVector.set(1, RECEIPT);
				laGenRpt.formatReport(lvVector);
				lsFormattedReport =
					laGenRpt.getFormattedReport().toString();
				// defect 6032, Set margin and line spacing for patch code
				// defect 7826
				// Add printer reset at the beginning of all salvage 
				// certificates
				String lsTray1 =
					Print.getPRINT_RESET()
						+ Print.getPRINT_TRAY_1()
						+ Print.getPRINT_LINE_SPACING_1_8();
				//	end defect 7826
				//+ laPrint.getPRINT_SLVG_TOP_MARGIN();
				// end defect 6032
				laRptSearchData.setKey1(
					lsTray1 + lsFormattedReport + ReportConstant.FF);
				laRptSearchData.setKey2(lsBarcodeFileName);
				laRptSearchData.setIntKey1(0);
				lsQualifiedRptFileName =
					(String) saveReports(laRptSearchData);
				laPrint.setBarcodeTrans(1);
				copyDocToRcptDirectory(
					lsQualifiedRptFileName,
					lsBarcodeFileName);
				//}
				laGenRpt = new GenSCOT();
				lvVector.set(1, CERTIFICATE);
				laGenRpt.formatReport(lvVector);
				// defect 7826
				// Add printer reset at the beginning of all salvage 
				// certificates
				lsFormattedReport =
					laGenRpt.getFormattedReport().toString();
				String lsTray2 =
					Print.getPRINT_RESET() + Print.getPRINT_TRAY_2();
				//	end defect 7826
				laRptSearchData.setKey1(
					lsTray2 + lsFormattedReport + ReportConstant.FF);
				laRptSearchData.setKey2(lsRptFileName);
				// end defect 6181
				laRptSearchData.setIntKey1(0);
				lsQualifiedRptFileName =
					(String) saveReports(laRptSearchData);
				// Deleting the barcode file; Not created for CCOSCT
				//if (!lsTransCd.equals(TransCdConstant.CCOSCT))
				//{
				//	File laBarCodeFile =
				//		new File(
				//			SystemProperty.getReportsDirectory()
				//+ lsBarcodeFileName
				//+ CRT_EXTENSION);
				//	laBarCodeFile.delete();
				//} 
				// end defect 6234
				copyDocToRcptDirectory(
					lsQualifiedRptFileName,
					lsRptFileName);
				TransactionCodesData laTransCdData =
					TransactionCodesCache.getTransCd(lsTransCd);
				String lsTransCdDesc = laTransCdData.getTransCdDesc();
				String lsVIN =
					saCompTransData
						.getVehicleInfo()
						.getVehicleData()
						.getVin();
				updateRcptLog(
					lsRptFileName + ".CRT",
					lsRptFileName + "B.CRT",
					saCompTransData.getTransId(),
					lsTransCdDesc,
					lsVIN,
					lsTransCd);
				updateCumRcptLog(false);
				laPrint.sendToPrinter(
					lsQualifiedRptFileName,
					lsTransCd);
			} // process COA, NRCOT, CCONRT, CORNRT 
			else if (
				lsTransCd.equals(TransCdConstant.COA)
					|| lsTransCd.equals(TransCdConstant.NRCOT)
					|| lsTransCd.equals(TransCdConstant.CCONRT)
					|| lsTransCd.equals(TransCdConstant.CORNRT))
			{
				lsRptFileName = NRVT;
				if (lsTransCd.equals(TransCdConstant.COA))
				{
					lsRptFileName = "COA";
				}
				else if (lsTransCd.equals(TransCdConstant.CCONRT))
				{
					lsRptFileName = lsRptFileName + "CCO";
				}
				else if (lsTransCd.equals(TransCdConstant.CORNRT))
				{
					lsRptFileName = lsRptFileName + "COR";
				}
				lsBarcodeFileName = lsRptFileName + "B";
				// defect 6234
				// print the receipt before certificate per VTR
				lvVector.add("");
				GenNRPCOT laGenRpt = null;
				//if (!lsTransCd.equals(TransCdConstant.CCONRT))
				//{
				laGenRpt = new GenNRPCOT();
				lvVector.set(1, RECEIPT);
				laGenRpt.formatReport(lvVector);
				lsFormattedReport =
					laGenRpt.getFormattedReport().toString();
				// defect 6032
				// Set margin and line spacing for patch code
				// defect 7826
				// Add printer reset at the beginning of all salvage 
				// certificates
				String lsTray1 =
					Print.getPRINT_RESET()
						+ Print.getPRINT_TRAY_1()
						+ Print.getPRINT_LINE_SPACING_1_8();
				//	end defect 7826
				//+ laPrint.getPRINT_SLVG_TOP_MARGIN();
				// end defect 6032
				laRptSearchData.setKey1(
					lsTray1 + lsFormattedReport + ReportConstant.FF);
				// defect 6848, 6898
				// Changed the name of the barcode file to match the filename
				// + an upercase B - this is deleted after printing barcode file
				// laRptSearchData.setKey2("NRPCOTr");
				laRptSearchData.setKey2(lsBarcodeFileName);
				// end defect 6848, 6898
				laRptSearchData.setIntKey1(0);
				lsQualifiedRptFileName =
					(String) saveReports(laRptSearchData);
				laPrint.setBarcodeTrans(1);
				copyDocToRcptDirectory(
					lsQualifiedRptFileName,
					lsBarcodeFileName);
				//}
				laGenRpt = new GenNRPCOT();
				lvVector.set(1, CERTIFICATE);
				laGenRpt.formatReport(lvVector);
				// defect 7826
				// Add printer reset at the beginning of all salvage 
				// certificates
				String lsReport =
					laGenRpt.getFormattedReport().toString();
				String lsTray3 =
					Print.getPRINT_RESET() + Print.getPRINT_TRAY_3();
				laRptSearchData.setKey1(
					lsTray3 + lsReport + ReportConstant.FF);
				// end defect 7826
				// Set file name for NRCOT, CCONRT, CORNRT
				laRptSearchData.setKey2(lsRptFileName);
				laRptSearchData.setIntKey1(0);
				lsQualifiedRptFileName =
					(String) saveReports(laRptSearchData);
				copyDocToRcptDirectory(
					lsQualifiedRptFileName,
					lsRptFileName);
				TransactionCodesData laTransCdData =
					TransactionCodesCache.getTransCd(lsTransCd);
				String lsTransCdDesc = laTransCdData.getTransCdDesc();
				String lsVIN =
					saCompTransData
						.getVehicleInfo()
						.getVehicleData()
						.getVin();
				updateRcptLog(
					lsRptFileName + ".CRT",
					lsRptFileName + "B.CRT",
					saCompTransData.getTransId(),
					lsTransCdDesc,
					lsVIN,
					lsTransCd);
				updateCumRcptLog(false);
				laPrint.sendToPrinter(
					lsQualifiedRptFileName,
					lsTransCd);
				// Deleting the barcode file  
				//if (!caTransactionControl
				//	.getTransCd()
				//	.equals(TransCdConstant.CCONRT))
				//{
				//	File laBarcodeFile =
				//		new File(
				//			SystemProperty.getReportsDirectory()
				//+ lsBarcodeFileName
				//+ CRT_EXTENSION);
				//	laBarcodeFile.delete();
				//}
				// end defect 6234
			} // COA Processing
			//			else if (lsTransCd.equals(TransCdConstant.COA))
			//			{
			//GenCOADocument laGenRpt = new GenCOADocument();
			//laGenRpt.formatReport(saCompTransData);
			//String lsReport =
			//	laGenRpt.getFormattedReport().toString();
			//// defect 7826
			//// Add printer reset at the beginning of all salvage 
			//// certificates
			//laRptSearchData.setKey1(
			//	Print.getPRINT_RESET()
			//		+ Print.getPRINT_TRAY_1()
			//		+ Print.getPRINT_LANDSCAPE()
			//		+ System.getProperty(LINE_SEPARATOR)
			//		+ lsReport
			//		+ ReportConstant.FF);
			//// end defect 7826
			//laRptSearchData.setKey2(TransCdConstant.COA);
			//laRptSearchData.setIntKey1(0);
			//String lsFileName =
			//	(String) saveReports(laRptSearchData);
			//laPrint.sendToPrinter(lsFileName);
			//			}
			// end defect 9643
			else
			{
				return false;
			}
			return true;
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
	}
	/**
	 * Take a vector of TransactionCache objects and process it.
	 * Calls writeToCache and postTrans.
	 * <p> 
	 * A failure on write to cache means we can process the data.Reset
	 * and back out.
	 * <p>
	 *	A failure to post the vector to the server can be forgiven.
	 *  SendCache must be started to catch it up though.
	 * Retry postTrans on errors.
	 * <p>
	 * @param avTransactionData Data to be processed 
	 * @throws RTSException
	 */
	public void processCacheVector(Vector avTransactionData)
		throws RTSException
	{
		// write to cache first.
		// if that fails, do not post to DB
		int liRC = writeToCache(avTransactionData);
		if (liRC != FAILURE)
		{ // only try postTrans if DBReady 
			if (com
				.txdot
				.isd
				.rts
				.client
				.desktop
				.RTSApplicationController
				.isDBReady())
			{ // allow for a retry on postTrans failure.
				try
				{
					Object laResult = postTrans(avTransactionData);
					// if the result is a boolean and it is false, we had
					// a DB or Server down error.
					// Write msg to log. No need to throw an exception!
					if (laResult != null
						&& laResult instanceof Boolean
						&& !((Boolean) laResult).booleanValue())
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							" postTrans failed DB Down "
								+ laResult.toString());
					}
				}
				catch (RTSException aeRTSEx)
				{ // defect 10164 
					// Throw the 803 error.  Do not retry.
					if (aeRTSEx.getDetailMsg() != null
						&& aeRTSEx.getDetailMsg().indexOf(
						//"DuplicateKeyException")
					CommonConstant
							.DUPLICATE_KEY_EXCEPTION)
							> -1)
					{
						// end defect 10164 
						throw aeRTSEx;
					} // Throw TransHdr error. Do not retry.
					// Do not invoke SendCache. 
					if (aeRTSEx.getDetailMsg() != null
						&& aeRTSEx.getDetailMsg().indexOf(
							"RTS II Application Error.")
							> -1)
					{
						throw aeRTSEx;
					} // Retry the post just to see if it will go through
					try
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							" Retrying postTrans due to "
								+ aeRTSEx.toString());
						Object laResult2 = postTrans(avTransactionData);
					}
					catch (RTSException aeRTSEx2)
					{ // just write a message to the log.  Do not throw 
						// an exception.
						if (aeRTSEx2.getDetailMsg() != null
							&& aeRTSEx2.getDetailMsg().indexOf(
								CommonConstant.DUPLICATE_KEY_EXCEPTION)
							//"DuplicateKeyException")
								> -1)
						{
							Log.write(
								Log.SQL_EXCP,
								this,
								" SQL0803 on retry. Duplicate Key.  Continue. ");
						}
						else
						{
							// could not write to server.
							Log.write(
								Log.SQL_EXCP,
								this,
								" postTrans error due to "
									+ aeRTSEx2.toString());
							// signal DB Down so sendcache can try to catch up
							// also do not to loose any trans
							Comm.fireCommEvent(
								new CommEvent(CommEvent.DB_DOWN));
							// display the aeRTSEx2 message
							// defect 6456 
							aeRTSEx2.displayError();
							//	new javax.swing.JFrame());
							// end defect 6456 
						}
					}
				}
			}
		}
		else
		{
			Log.write(Log.SQL_EXCP, this, " Write to Cache error");
			// defect 6456 
			//reset();
			RTSException leRTSEx =
				new RTSException(
					RTSException.TR_ERROR,
					"Could not Write to Transaction Cache.",
					"WriteToCache");
			leRTSEx.setDetailMsg(
				"Could not Write to Transaction Cache. ");
			//			+ "<br>Transaction has been reset. "
			//			+ "<br>Please re-do your transaction. ");
			// end defect 6456
			throw leRTSEx;
		}
	}
	/**
	 * Read the Cached Transactions from the trxcache directory since a 
	 * given datetime.
	 *
	 * @param aaRTSDate RTSDate 
	 * @return Vector  
	 * @throws RTSException
	 */
	public static Vector readFromCache(RTSDate aaRTSDate)
		throws RTSException
	{
		// defect 5225
		// Exit on Bad Transaction Cache
		Map map = null;
		try
		{
			map = cacheIO(READ, null, aaRTSDate);
		}
		catch (RTSException aeRTSEx)
		{
			// defect 6614
			// Write to SendCache log on error
			file : SendCacheLog.write(
				"Send Cache encountered error with cache file "
					+ aaRTSDate.getAMDate());
			// end defect 6614
			System.err.println(
				"Send Cache encountered error with cache file "
					+ aaRTSDate.getAMDate());
			System.err.println(aeRTSEx.getMessage());
			System.err.println(aeRTSEx.getDetailMsg());
			System.exit(0);
			// end defect 5225 
		}
		return (Vector) map.get("VECTOR");
	}
	/**
	 * Clean up the static variables in memory for the next Transaction
	 *
	 * @throws RTSException
	 */
	public static void reset() throws RTSException
	{
		saTransHdrData = null;
		Transaction.setRunningSubtotal(new Dollar(0.0));
		// defect 7717 
		// sbAllowCancelTrans = true;
		setCancelTrans(true);
		// end defect 7717 
		svTransIdList.clear();
		// defect 10290
		resetDTA();
		// end defect 10290  
		sbMultipleTrans = false;
		sbPrintCashReceipt = false;
		if (isRestoredSetAside())
		{
			sbRestoredSetAside = false;
			SystemProperty.setPrintImmediateIndi(siStoredPrintIndi);
		}
		ssRcptDirForCust = null;
		ssRcptLogFile = null;
		sbCustomer = false;
		saCreditRemaining = null;
		ssSavedAuthCd = null;
		ssSavedTransCd = null;
		CommonClientBusiness laCommonClientBusiness =
			new CommonClientBusiness();
		laCommonClientBusiness.processData(
			GeneralConstant.COMMON,
			CommonConstant.SAVE_INVENTORY,
			CommonClientBusiness.ERASE_SAVED_INV);
		// defect 10491 
		laCommonClientBusiness.processData(
			GeneralConstant.COMMON,
			CommonConstant.SAVE_VI_PRMT_VECTOR,
			CommonClientBusiness.ERASE_SAVED_INV);
		laCommonClientBusiness.processData(
			GeneralConstant.COMMON,
			CommonConstant.SAVE_TIME_PERMIT,
			null);
		// end defect 10491 
		//		laCommonClientBusiness.processData(
		//			GeneralConstant.COMMON,
		//			CommonConstant.SAVE_DP_CUST_DATA,
		//			null);
		// defect 9085
		laCommonClientBusiness.processData(
			GeneralConstant.COMMON,
			CommonConstant.SAVE_VI_SPCL_PLT_VECTOR,
			CommonClientBusiness.ERASE_SAVED_INV);
		// If !(HQ & (SPAPPL || SPREV || SPRNW || SPRNR))
		if (saCompTransData != null)
		{
			String lsTransCd = saCompTransData.getTransCode();
			if (lsTransCd != null
				&& !(SystemProperty.isHQ()
					&& (UtilityMethods.isSpecialPlates(lsTransCd)
						&& !lsTransCd.equals(TransCdConstant.SPUNAC)
						&& !lsTransCd.equals(TransCdConstant.SPRSRV)
						&& !lsTransCd.equals(TransCdConstant.SPDEL))))
			{
				sbSavedSpclPlate = false;
				laCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_SPCL_PLT,
					null);
			}
		} // end defect 9085 
	}
	/** 
	 * Reset DTA
	 */
	public static void resetDTA()
	{
		svDTADlrTtlData = null;
		svDTAOrigDlrTtlData = null;
		siDTADealerId = -1;
		siDTADlrTtlDataIndex = -1;
		saDTADealerData = null;
	}
	/**
	 * Restore set aside transaction
	 *
	 * @throws RTSException
	 */
	public static void restoreSetAside() throws RTSException
	{
		if (saTransHdrData != null)
		{
			//erase objects in memory
			CommonClientBusiness laCommonClientBusiness =
				new CommonClientBusiness();
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_INVENTORY,
				null);
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_VEH,
				null);
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_VEH_MISC,
				null);
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_TIME_PERMIT,
				null);
			// defect 9085 
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_SPCL_PLT,
				null);
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_VI_SPCL_PLT_VECTOR,
				null);
			// end defect 9085
			//	laCommonClientBusiness.processData(
			//		GeneralConstant.COMMON,
			//		CommonConstant.SAVE_DP_CUST_DATA,
			//		null);
			// defect 10617 
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_VI_SPCL_PLT_VECTOR,
				null);
			// end defect 10617 
			String lsRootDir = SystemProperty.getSetAsideDir();
			File laRootDir = new File(lsRootDir);
			if (!laRootDir.exists())
			{
				return;
			}
			StringBuffer lsStringBuffer = new StringBuffer();
			lsStringBuffer.append(lsRootDir);
			lsStringBuffer.append(CSN);
			lsStringBuffer.append(getPaddedCustSeqNo());
			lsStringBuffer.append(BACK_SLASH);
			String lsFileDir = lsStringBuffer.toString();
			File laFileDir = new File(lsFileDir);
			if (laFileDir.exists())
			{
				File[] larrFileList = laFileDir.listFiles();
				for (int i = 0; i < larrFileList.length; i++)
				{
					Object laObject =
						deSerializeFileForSetAside(larrFileList[i]);
					if (laObject != null)
					{ // defect 9085 
						// Both Inventory and Special Plates Regis Data  
						if (laObject instanceof Vector)
						{
							Vector laVector = (Vector) laObject;
							if (laVector.size() > 0)
							{
								if (laVector.elementAt(0)
									instanceof ProcessInventoryData)
								{
									laCommonClientBusiness.processData(
										GeneralConstant.COMMON,
										CommonConstant.SAVE_INVENTORY,
										laObject);
								} // If Special Plates Regis Data 
								else if (
									laVector.elementAt(0)
										instanceof SpecialPlatesRegisData)
								{
									laCommonClientBusiness.processData(
										GeneralConstant.COMMON,
										CommonConstant
											.SAVE_VI_SPCL_PLT_VECTOR,
										laObject);
								} // defect 10617 
								//	Permit Data 
								else if (
									laVector.elementAt(0)
										instanceof PermitData)
								{
									laCommonClientBusiness.processData(
										GeneralConstant.COMMON,
										CommonConstant
											.SAVE_VI_PRMT_VECTOR,
										laObject);
								} // end defect 10617 
							}
						}
						else if (
							laObject instanceof SpecialPlatesRegisData)
						{
							laCommonClientBusiness.processData(
								GeneralConstant.COMMON,
								CommonConstant.SAVE_SPCL_PLT,
								laObject);
							setSavedSpecialPlate(true);
						} // end defect 9085
						else if (laObject instanceof MFVehicleData)
						{
							siCumulativeTransIndi = 2;
							laCommonClientBusiness.processData(
								GeneralConstant.COMMON,
								CommonConstant.SAVE_VEH,
								laObject);
						}
						else if (laObject instanceof VehMiscData)
						{
							laCommonClientBusiness.processData(
								GeneralConstant.COMMON,
								CommonConstant.SAVE_VEH_MISC,
								laObject);
						} // TODO  
						//	else if (
						//		laObject
						//			instanceof DisabledPlacardCustomerData)
						//		{
						//			siCumulativeTransIndi = 1;
						//			laCommonClientBusiness.processData(
						//				GeneralConstant.COMMON,
						//				CommonConstant.SAVE_DP_CUST_DATA,
						//				laObject);
						//		}
						else if (laObject instanceof TimedPermitData)
						{
							// defect 10491 
							siCumulativeTransIndi = 1;
							// end defect 10491 
							laCommonClientBusiness.processData(
								GeneralConstant.COMMON,
								CommonConstant.SAVE_TIME_PERMIT,
								laObject);
						} // defect 7717 
						else if (
							laObject instanceof TransactionHeaderData)
						{
							setCancelTrans(
								((TransactionHeaderData) laObject)
									.isCancelTrans());
						} // end defect 7717 
					}
				}
			}
			else
			{
				return;
			}
		}
	}
	/**
	 * Generate the cash receipt
	 *
	 * @param  avVector Vector 
	 * @throws RTSException
	 */
	private String saveCashReceipt(Vector avVector) throws RTSException
	{
		GenCashRegisterReceipt laGenRcpt = new GenCashRegisterReceipt();
		laGenRcpt.formatReceipt(avVector);
		// defect 8524
		// FormFeed constant cleanup 
		String lsCashRegisterContent =
			laGenRcpt.getReceipt().toString() + ReportConstant.FF;
		// end defect 8524
		//Create CashReceipts file
		String lsCustSeqNo = getPaddedCustSeqNo();
		String lsReceiptRootDir = SystemProperty.getReceiptsDirectory();
		String lsLocationForReceipt =
			lsReceiptRootDir + CSN + lsCustSeqNo + BACK_SLASH;
		String lsExtension = ".RCP";
		String lsCashRegister = "CSHREG";
		String lsReceiptLogFile = lsLocationForReceipt + "RCPTLOG";
		if (lsCashRegisterContent != null)
		{
			File laCashReceiptsFile =
				new File(
					lsLocationForReceipt
						+ lsCashRegister
						+ lsExtension);
			if (!laCashReceiptsFile.exists())
			{
				try
				{
					laCashReceiptsFile.createNewFile();
				}
				catch (IOException aeIOEx)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeIOEx);
				}
			}
			try
			{
				FileOutputStream laFileOutStream =
					new FileOutputStream(
						laCashReceiptsFile.getAbsolutePath(),
						true);
				OutputStreamWriter laOutputStreamWtr =
					new OutputStreamWriter(laFileOutStream);
				BufferedWriter laBuffWtr =
					new BufferedWriter(laOutputStreamWtr);
				laBuffWtr.write(lsCashRegisterContent);
				laBuffWtr.newLine();
				laBuffWtr.flush();
				laFileOutStream.close();
			}
			catch (FileNotFoundException aeFNFEx)
			{
				throw new RTSException(
					RTSException.JAVA_ERROR,
					aeFNFEx);
			}
			catch (IOException aeIOEx)
			{
				throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
			}
		}
		StringBuffer lsLogFileInput = new StringBuffer("");
		lsLogFileInput.append("CSHREG.RCP");
		lsLogFileInput.append(",");
		lsLogFileInput.append(" ");
		lsLogFileInput.append(",");
		lsLogFileInput.append(" ");
		lsLogFileInput.append(",");
		// defect 10700 
		lsLogFileInput.append(" ");
		lsLogFileInput.append(",");
		// end defect 10700 
		lsLogFileInput.append("CASH RECEIPT");
		lsLogFileInput.append(",");
		lsLogFileInput.append(" ");
		lsLogFileInput.append(",");
		lsLogFileInput.append("CSHREG");
		// defect 10844 
		lsLogFileInput.append(",");
		lsLogFileInput.append(" ");
		// end defect 10844
		try
		{
			FileOutputStream laFileOutStream =
				new FileOutputStream(lsReceiptLogFile, true);
			OutputStreamWriter laOutputStreamWtr =
				new OutputStreamWriter(laFileOutStream);
			BufferedWriter laBuffWtr =
				new BufferedWriter(laOutputStreamWtr);
			laBuffWtr.write(lsLogFileInput.toString());
			laBuffWtr.newLine();
			laBuffWtr.flush();
			laFileOutStream.close();
		}
		catch (FileNotFoundException aeFNFEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeFNFEx);
		}
		catch (IOException aeIOExp)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOExp);
		}
		return (lsLocationForReceipt + lsCashRegister + lsExtension);
	}
	/**
	 * Creates the Receipts files for transactions
	 *
	 * @param  avRcptDetails Vector
	 * @throws RTSException
	 * @return String  
	*/
	public String saveReceipt(Vector avRcptDetails) throws RTSException
	{
		CompleteTransactionData laCTData =
			(CompleteTransactionData) avRcptDetails.elementAt(0);
		TransactionCodesData laTransCdsData = null;
		String lsRcptContent = null;
		String lsRcptWithBarCdContent = null;
		String lsBarCodeFileName = "";

		// defect 10844 
		String lsPrmtIssuanceId = new String();

		if (laCTData.getTimedPermitData() instanceof PermitData)
		{
			lsPrmtIssuanceId =
				((PermitData) laCTData.getTimedPermitData())
					.getPrmtIssuanceId();
		}
		// end defect 10844 

		if (avRcptDetails.elementAt(1) != null)
		{
			lsRcptContent = (String) avRcptDetails.elementAt(1);
		}
		if (avRcptDetails.elementAt(2) != null)
		{
			lsRcptWithBarCdContent =
				(String) avRcptDetails.elementAt(2);
		} // defect 10700 
		String lsPermitRcptContent = null;
		if (avRcptDetails.size() == 4
			&& avRcptDetails.elementAt(3) != null)
		{
			lsPermitRcptContent = (String) avRcptDetails.elementAt(3);
		} // end defect 10700 
		// use the class variable for receipt location
		String lsLocationForReceipt = getRcptDirForCust();
		String lsExtension = ".RCP";
		try
		{
			String lsReceiptLogFile = lsLocationForReceipt + "RCPTLOG";
			String lsTransCd = laCTData.getTransCode();
			String lsFileNameToLog = null;
			String lsTransactionId = null;
			String lsTransCdDesc = null;
			String lsVIN = null;
			lsFileNameToLog = "";
			lsTransactionId = laCTData.getTransId();
			if ((laCTData.getVehicleInfo() == null)
				|| laCTData.getVehicleInfo().getVehicleData() == null
				|| laCTData.getVehicleInfo().getVehicleData().getVin()
					== null)
			{
				lsVIN = "NO VIN";
			}
			else
			{
				lsVIN =
					laCTData.getVehicleInfo().getVehicleData().getVin();
			} // defect 7436
			// Set rcpt.log file to title application receipt
			// Determine Trans Code Description
			// defect 10290 
			if (UtilityMethods.isDTA(lsTransCd))
			{
				// end defect 10290 
				laTransCdsData =
					(
						TransactionCodesData) TransactionCodesCache
							.getTransCd(
						TITLE);
			}
			else
			{
				laTransCdsData =
					(
						TransactionCodesData) TransactionCodesCache
							.getTransCd(
						lsTransCd);
			}
			lsTransCdDesc = laTransCdsData.getTransCdDesc();
			File laFile = new File(lsReceiptLogFile);
			if (!laFile.exists())
			{
				try
				{
					laFile.createNewFile();
				}
				catch (IOException aeIOExp)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeIOExp);
				}
			}
			String lsFileName = null;
			// Set File Name
			// (Non Dealer) Title Application events
			if (lsTransCd.equals(TransCdConstant.TITLE)
				|| lsTransCd.equals(TransCdConstant.NONTTL)
				|| lsTransCd.equals(TransCdConstant.CORTTL)
				|| lsTransCd.equals(TransCdConstant.REJCOR))
			{
				lsFileName = "TTL";
			} // Dealer Title Application events
			// defect 10290 
			else if (UtilityMethods.isDTA(lsTransCd))
			{
				// end defect 10290 
				lsFileName = "DTL";
			}
			// Vehicle Inquiry
			// TODO - FOR VTR-275  
			// 
			//else if (lsTransCd.equals(TransCdConstant.VEHINQ))
			else if (caTransactionControl.getEventType().equals(TransCdConstant.VEHINQ))
			{
				lsFileName = "VHINQ";
			} // Registration Events
			else if (
				lsTransCd.equals(TransCdConstant.RENEW)
					|| lsTransCd.equals(TransCdConstant.IRENEW)
					|| lsTransCd.equals(TransCdConstant.DUPL)
					|| lsTransCd.equals(TransCdConstant.EXCH)
					|| lsTransCd.equals(TransCdConstant.PAWT)
					|| lsTransCd.equals(TransCdConstant.CORREG)
					|| lsTransCd.equals(TransCdConstant.REPL)
					|| lsTransCd.equals(TransCdConstant.DRVED))
			{
				lsFileName = "REGIS";
			} // Hot Check Redeemed
			else if (lsTransCd.equals(TransCdConstant.CKREDM))
			{
				lsFileName = "CKRDM";
			} // Misc Registration
			// defect 9831 
			// defect 8268
			// defect 10491 
			else if (
				UtilityMethods.printsPermit(
					lsTransCd) // end defect 10491 
					|| lsTransCd.equals(TransCdConstant.NROPT)
					|| lsTransCd.equals(TransCdConstant.NRIPT)
					|| UtilityMethods.isDsabldPlcrdEvent(lsTransCd)
					|| lsTransCd.equals(TransCdConstant.TOWP)
					|| lsTransCd.equals(TransCdConstant.TAWPT))
				// end defect 8268
				// end defect 9831 
			{
				lsFileName = "MSCRG";
			} // Hot Check Credit
			else if (lsTransCd.equals(TransCdConstant.HOTCK))
			{
				lsFileName = "HOTCK";
			} // Hot check deduct
			else if (lsTransCd.equals(TransCdConstant.HOTDED))
			{
				lsFileName = "HOTDT";
			} // Refund-other
			else if (lsTransCd.equals(TransCdConstant.REFUND))
			{
				lsFileName = "REFND";
			} // Refund-Cash
			else if (lsTransCd.equals(TransCdConstant.RFCASH))
			{
				lsFileName = "REFND";
			} // Additional sales tax
			else if (lsTransCd.equals(TransCdConstant.ADLSTX))
			{
				lsFileName = "ADSTX";
			} // Additional Collections
			// defect 8348
			// Funds Adjustment no longer available
			else if (
				// lsTransCd.equals(TransCdConstant.FNDADJ) ||
			lsTransCd
				.equals(
					TransCdConstant.ADLCOL))
			{
				lsFileName = "ADJ";
			} // end defect 8348 
			// Funds remittance/EFT
			else if (
				lsTransCd.equals(TransCdConstant.FNDREM)
					|| lsTransCd.equals(TransCdConstant.EFTFND))
			{
				lsFileName = "RMTVR";
			} // Regional Collection
			else if (
				lsTransCd.equals(TransCdConstant.RGNCOL)
					|| lsTransCd.equals(TransCdConstant.CCO))
			{
				lsFileName = "RGCOL";
			} // PCR 34 
			// Subcontractor
			else if (lsTransCd.equals(TransCdConstant.SBRNW))
			{
				lsFileName = "SR";
			} // END PCR 34 
			// defect 9145/9126/9085 
			// Add Special Plate Receipts  
			else if (
				caTransactionControl.getEventType().equals(SPCLPLT))
			{
				lsFileName = "SPCLPLT";
			} // defect 9145/9126/9085
			// call getReceiptFileNumber to get the file number
			int liNumberOfFiles =
				getReceiptFileNumber(lsFileName, lsExtension);
			String lsRcptFileName =
				lsLocationForReceipt
					+ lsFileName
					+ liNumberOfFiles
					+ lsExtension;
			lsFileNameToLog =
				lsFileName + liNumberOfFiles + lsExtension;
			if (lsRcptContent != null && !lsRcptContent.equals(""))
			{
				laFile = new File(lsRcptFileName);
				try
				{
					laFile.createNewFile();
				}
				catch (IOException aeIOExp)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeIOExp);
				}
				try
				{
					FileOutputStream laFileOutStream =
						new FileOutputStream(lsRcptFileName, true);
					OutputStreamWriter laOutputStreamWtr =
						new OutputStreamWriter(laFileOutStream);
					BufferedWriter laBuffWtr =
						new BufferedWriter(laOutputStreamWtr);
					laBuffWtr.write(lsRcptContent);
					laBuffWtr.newLine();
					laBuffWtr.flush();
					laFileOutStream.close();
				}
				catch (FileNotFoundException aeFNFEx)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeFNFEx);
				}
				catch (IOException aeIOEx)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeIOEx);
				}

			} // reate Receipt File with Bar Code for Titled events     
			if (lsRcptWithBarCdContent != null
				&& !lsRcptWithBarCdContent.equals(""))
			{
				// defect 10700 
				// Implement CommonConstant.BARCODE_SUFFIX
				lsBarCodeFileName =
					lsFileName
						+ liNumberOfFiles
						+ CommonConstant.BARCODE_SUFFIX
						+ lsExtension;
				// end defect 10700 
				File laRcptFileWithBarCd =
					new File(lsLocationForReceipt + lsBarCodeFileName);
				try
				{
					laRcptFileWithBarCd.createNewFile();
				}
				catch (IOException aeIOEx)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeIOEx);
				}
				try
				{
					FileOutputStream laFileOutStream =
						new FileOutputStream(
							lsLocationForReceipt + lsBarCodeFileName,
							true);
					OutputStreamWriter laOutputStreamWtr =
						new OutputStreamWriter(laFileOutStream);
					BufferedWriter laBuffWtr =
						new BufferedWriter(laOutputStreamWtr);
					laBuffWtr.write(
						lsRcptWithBarCdContent == null
							? ""
							: lsRcptWithBarCdContent);
					laBuffWtr.newLine();
					laBuffWtr.flush();
					laFileOutStream.close();
				}
				catch (FileNotFoundException aeFNFEx)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeFNFEx);
				}
				catch (IOException aeIOExp)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeIOExp);
				}
			} // defect 10700
			String lsPermitFileName = new String();
			if (lsPermitRcptContent != null
				&& !lsPermitRcptContent.equals(""))
			{
				lsPermitFileName =
					lsFileName
						+ liNumberOfFiles
						+ CommonConstant.PERMIT_SUFFIX
						+ lsExtension;
				File laPermitFile =
					new File(lsLocationForReceipt + lsPermitFileName);
				try
				{
					laPermitFile.createNewFile();
				}
				catch (IOException aeIOEx)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeIOEx);
				}
				try
				{
					FileOutputStream laFileOutStream =
						new FileOutputStream(
							lsLocationForReceipt + lsPermitFileName,
							true);
					OutputStreamWriter laOutputStreamWtr =
						new OutputStreamWriter(laFileOutStream);
					BufferedWriter laBuffWtr =
						new BufferedWriter(laOutputStreamWtr);
					laBuffWtr.write(
						lsPermitRcptContent == null
							? ""
							: lsPermitRcptContent);
					laBuffWtr.newLine();
					laBuffWtr.flush();
					laFileOutStream.close();
				}
				catch (FileNotFoundException aeFNFEx)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeFNFEx);
				}
				catch (IOException aeIOExp)
				{
					throw new RTSException(
						RTSException.JAVA_ERROR,
						aeIOExp);
				}
			}
			// end defect 10700

			// defect 7430
			// If DTA and Print Sticker selected on DTA008, reset 
			// transcd to DLRSTKR
			// Print Sticker and County Receipts
			if (UtilityMethods.isDTA(lsTransCd) //defect 10290 
				//&& isDTAPrintSticker(laCompTransData))
				&& caCurrDlrTtlData.isToBePrinted())
				// end defect 10290 
			{
				lsTransCd = DLRSTKR;
			}
			// defect 10844 
			// add Permit File Name parameter 
			updateRcptLog(
				lsFileNameToLog,
				lsBarCodeFileName,
				lsPermitFileName,
				lsTransactionId,
				lsTransCdDesc,
				lsVIN,
				lsTransCd,
				lsPrmtIssuanceId);
			// end defect 10844 

			return lsRcptFileName;
		}
		catch (RTSException aeRTSEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeRTSEx);
		}
	}

	/**
	 * Creates the Report files for transactions
	 * 
	 * @param  aaData Object
	 * @return String 
	 * @throws RTSException  
	 */
	private String saveReports(Object aaData) throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
		String lsRptName = laRptSearchData.getKey2();
		String lsStr = laRptSearchData.getKey1();
		int liExtension = laRptSearchData.getIntKey1();
		String lsFileName = "";
		try
		{
			File laBaseDir =
				new File(SystemProperty.getRTSAppDirectory());
			if (!laBaseDir.exists())
			{
				laBaseDir.mkdir();
			}
			File laRptDirectory =
				new File(SystemProperty.getReportsDirectory());
			if (!laRptDirectory.exists())
			{
				laRptDirectory.mkdir();
			}
			File[] larrFileList = laRptDirectory.listFiles();
			//Get all files which have supplied name as file name
			for (int i = 0; i < larrFileList.length; i++)
			{ //If directory, continue
				if (larrFileList[i].isDirectory())
				{
					continue;
				}
				String lsCommFileName = larrFileList[i].getName();
				int liIndexOfDot = lsCommFileName.indexOf(".");
				//File does not have any extension, discard it.
				if (liIndexOfDot == -1)
				{
					continue;
				}
				String lsFileName2 =
					lsCommFileName.substring(0, liIndexOfDot);
				if (lsFileName2.toUpperCase().equals(lsRptName))
				{
					larrFileList[i].delete();
					break;
				}
			}
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		try
		{
			if (liExtension == 0)
			{
				lsFileName =
					SystemProperty.getReportsDirectory()
						+ lsRptName
						+ ".CRT";
			}
			else
			{
				lsFileName =
					SystemProperty.getReportsDirectory()
						+ lsRptName
						+ ".RCP";
			}
			FileOutputStream laFileOutputStream =
				new FileOutputStream(lsFileName, true);
			PrintWriter laPrintWriter =
				new PrintWriter(laFileOutputStream);
			laPrintWriter.write(lsStr);
			laPrintWriter.close();
			laFileOutputStream.close();
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}
		return lsFileName;
	}
	/**
	 * 
	 * save Vehicle Data
	 * 
	 * @throws RTSException
	 */
	private void saveVehicleData() throws RTSException
	{
		// defect 9085
		// Set cumulativetransindi to determine if 
		//  same vehicle is available in next transaction 
		TransactionCodesData laTransCdsData =
			TransactionCodesCache.getTransCd(
				saCompTransData.getTransCode());
		if (laTransCdsData == null)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Transaction Data for transcd "
					+ saCompTransData.getTransCode()
					+ " is null",
				"ERROR");
		}
		else
		{
			setCumulativeTransIndi(
				laTransCdsData.getCumulativeTransCd());
		} // Reset MfgStatusCd on Special Plate
		// Note: Transactions already written
		SpecialPlatesRegisData laSpclPltRegisData = null;
		if (saCompTransData.getVehicleInfo() != null)
		{
			laSpclPltRegisData =
				saCompTransData.getVehicleInfo().getSpclPltRegisData();
		}

		String lsTransCd = caTransactionControl.getTransCd();
		boolean lbMfgNewSpclPltOnReplace = false;
		// Required for "More Trans, Same Vehicle" 
		if (laSpclPltRegisData != null)
		{
			laSpclPltRegisData.setEnterOnSPL002(false);
			// defect 9450
			laSpclPltRegisData.setResetChrgFee(false);
			// end defect 9450
			// defect 10097
			laSpclPltRegisData.setOrigPltExpMo(
				laSpclPltRegisData.getPltExpMo());
			laSpclPltRegisData.setOrigPltExpYr(
				laSpclPltRegisData.getPltExpYr());
			// end defect 10097 
			// defect 9893 - commented out code below.  No need since
			//               already in SpclPltRegisData
			// defect 9389
			//			String lsOwnerId =
			//laSpclPltRegisData.getOwnrData().getOwnrId();
			//			if (laSpclPltRegisData.isMFDownSP())
			//			{
			//laSpclPltRegisData.setOwnrData(
			//	(OwnerData) UtilityMethods.copy(
			//		saCompTransData
			//			.getVehicleInfo()
			//			.getOwnerData()));
			//
			//// Restore OwnerId from Screen 
			//laSpclPltRegisData.getOwnrData().setOwnrId(lsOwnerId);
			//			}
			//			else
			//			{
			//laSpclPltRegisData.getOwnrData().setOwnrAddr(
			//	(AddressData) UtilityMethods.copy(
			//		saCompTransData
			//			.getVehicleInfo()
			//			.getOwnerData()
			//			.getOwnrAddr()));
			//			}
			//			// end defect 9389 
			//			// end defect 9893
			if (laSpclPltRegisData
				.getMFGStatusCd()
				.equals(SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD)
				|| laSpclPltRegisData.getRequestType().equals(
					SpecialPlatesConstant.ISSUE_FROM_INVENTORY)
				|| lsTransCd.equals(TransCdConstant.SPAPPC))
			{
				lbMfgNewSpclPltOnReplace =
					isMfgNewSpclPltOnReplace(
						saCompTransData,
						lsTransCd);
				// defect 10700 		
				if (laSpclPltRegisData
					.getMFGStatusCd()
					.equals(
						SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD))
				{
					if (lbMfgNewSpclPltOnReplace
						|| UtilityMethods.isSPAPPL(lsTransCd))
					{
						laSpclPltRegisData.setPrntPrmt(true);
					}
				} // end defect 10700 
				laSpclPltRegisData.setMFGStatusCd(
					SpecialPlatesConstant.ASSIGN_MFGSTATUSCD);
				laSpclPltRegisData.setRequestType("");
				laSpclPltRegisData.setMfgSpclPltIndi(0);
				// Set PltBirthDate to current date 
				laSpclPltRegisData.setPltBirthDate(
					((new RTSDate()).getYYYYMMDDDate()) / 100);
				laSpclPltRegisData.setRegPltAge(0);
			}
		}
		// defect 10491
		if (UtilityMethods.isPermitApplication(lsTransCd))
		{
			PermitData laPrmtData =
				(PermitData) saCompTransData.getTimedPermitData();
			laPrmtData.setPrmDupTrans(false);

			if (laPrmtData.getVIAllocData() != null)
			{
				laPrmtData.getVIAllocData().setTransTime(ciTransTime);
				caCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_VI_PRMT_VECTOR,
					laPrmtData);
			}
		} // end defect 10491 
		// Add Virtual Inventory Requests save mfg inventory Vector
		// Save for SPAPPL 
		// defect 10700 
		if ((!lsTransCd.equals(TransCdConstant.DPSPPT))
			&& (UtilityMethods.isSpecialPlates(lsTransCd)
				|| lbMfgNewSpclPltOnReplace))
		{ // end defect 10700 
			if (laSpclPltRegisData.getVIAllocData() != null)
			{
				laSpclPltRegisData.getVIAllocData().setTransTime(
					ciTransTime);
				caCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_VI_SPCL_PLT_VECTOR,
					laSpclPltRegisData);
			}

			if (laSpclPltRegisData
				.getMFGStatusCd()
				.equals(SpecialPlatesConstant.ASSIGN_MFGSTATUSCD))
			{
				laSpclPltRegisData.setOrigTransCd(lsTransCd);
				laSpclPltRegisData.setSAuditTrailTransId(getTransId());
			} // Save for Same Special Plate
			if (UtilityMethods.isSPAPPL(lsTransCd)
				|| lsTransCd.equals(TransCdConstant.SPRNR)
				|| lsTransCd.equals(TransCdConstant.SPRNW)
				|| lsTransCd.equals(TransCdConstant.SPREV))
			{
				caCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_SPCL_PLT,
					laSpclPltRegisData);
				//	Set flag accessible from KEY002 to enable 
				// "Same Special Plate"  
				setSavedSpecialPlate(true);
			}
		}
		else
		{
			caCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_SPCL_PLT,
				null);
			setSavedSpecialPlate(false);
		} // Save Special Plate Data
		// Clean out "Same Vehicle" if Special Plate 
		if (caTransactionControl.getEventType().equals(SPCLPLT))
		{
			// Resetting the saved values 
			caCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.CLEAR_SAVE_VEH_INFO,
				null);
			// Add any inventory to saved inventory vector 
			caCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_INVENTORY,
				saCompTransData.getAllocInvItms());
			setSavedTransCd(null);
		} // Clean out "Same Special Plate" if completing Non-Special Plate 
		// Transaction 
		else
		{
			setSavedSpecialPlate(false);
			// end defect 9085 
			// Save Vehicle Data 
			// defect 9823 
			// After Transaction, reset Supervisor Override Code, 
			//   Reason 
			if (saCompTransData.getVehicleInfo() != null
				&& saCompTransData.getVehMisc() != null)
			{
				saCompTransData.getVehMisc().setSupvOvride(
					CommonConstant.STR_SPACE_EMPTY);
				saCompTransData.getVehMisc().setSupvOvrideReason(
					CommonConstant.STR_SPACE_EMPTY);
			} // end defect 9823 
			// PROC After Title, clear out junk remarks/record for addl trans processing
			if (caTransactionControl.getEventType().equals(TTL))
			{
				MFVehicleData laMFVehicleData =
					saCompTransData.getVehicleInfo();
				if (laMFVehicleData.getVctSalvage() != null)
				{
					laMFVehicleData.getVctSalvage().set(0, null);
				} // Reset select fields, Title
				//reset junk indi to 0
				laMFVehicleData.setJnkIndi(0);
				// defect 4637
				// Set apprehended county number to 0
				laMFVehicleData.getRegData().setApprndCntyNo(0);
				// defect 4894
				// Set Special Examination Required (TTLEXMNINDI) to 0
				laMFVehicleData.getTitleData().setTtlExmnIndi(0);
				laMFVehicleData.getTitleData().setMustChangePltIndi(0);
			} //clear title proc cd
			if (caTransactionControl
				.getTransCd()
				.equals(TransCdConstant.REJCOR))
			{
				MFVehicleData laMFVehicleData =
					saCompTransData.getVehicleInfo();
				if (laMFVehicleData != null)
				{
					TitleData laTitleData =
						laMFVehicleData.getTitleData();
					if (laTitleData != null)
					{
						// defect 6073 
						// Set to "" vs. null
						//laTitleData.setTtlProcsCd(null);
						laTitleData.setTtlProcsCd(
							CommonConstant.STR_SPACE_EMPTY);
						// end defect 6073  
					}
				}
			} //			if (saCompTransData.getTimedPermitData() != null
			//				&& saCompTransData.getTimedPermitData().getDPCustData()
			//					!= null)
			//			{
			//				caCommonClientBusiness.processData(
			//					GeneralConstant.COMMON,
			//					CommonConstant.SAVE_DP_CUST_DATA,
			//					saCompTransData
			//						.getTimedPermitData()
			//						.getDPCustData());
			//
			//			}
			if (getCumulativeTransIndi() == 2
				|| getCumulativeTransIndi() == 1)
			{ // defect 10491 
				if (caTransactionControl.getEventType().equals(MRG))
				{
					// Clear Saved Permit Data if FDPT, PRMDUP 
					if (saCompTransData
						.getTransCode()
						.equals(TransCdConstant.FDPT)
						|| saCompTransData.getTransCode().equals(
							TransCdConstant.PRMDUP))
					{
						caCommonClientBusiness.processData(
							GeneralConstant.COMMON,
							CommonConstant.SAVE_TIME_PERMIT,
							null);
					}
					else
					{
						// defect 10844 
						// Reset MODPT indicator 
						if (saCompTransData.getTimedPermitData()
							instanceof PermitData)
						{
							PermitData laPermitData =
								((PermitData) saCompTransData
									.getTimedPermitData());
							laPermitData.setModPtTrans(false);
							laPermitData.setAuditTrailTransId(
								new String());
							laPermitData.setPrmtIssuanceId(
								new String());

						}
						// end defect 10844 

						// Save Permit Data 
						caCommonClientBusiness.processData(
							GeneralConstant.COMMON,
							CommonConstant.SAVE_TIME_PERMIT,
							saCompTransData.getTimedPermitData());
					}

					//		caCommonClientBusiness.processData(
					//	 	    GeneralConstant.COMMON,
					//	 		CommonConstant.SAVE_TIME_PERMIT,
					//			saCompTransData.getTimedPermitData());
					//	end defect 10491
					caCommonClientBusiness.processData(
						GeneralConstant.COMMON,
						CommonConstant.SAVE_VEH,
						null);
					caCommonClientBusiness.processData(
						GeneralConstant.COMMON,
						CommonConstant.SAVE_VEH_MISC,
						null);
					// defect 10662 
					// Should not be null for Non-Resident/Tow Truck
					// defect 10491 
					// Permits really only issues 'Virtual' inventory 
					caCommonClientBusiness.processData(
						GeneralConstant.COMMON,
						CommonConstant.SAVE_INVENTORY,
						saCompTransData.getAllocInvItms());
					//	null);
					// end defect 10491
					// end defect 10662  
					setSavedTransCd(null);
				}
				else
				{
					caCommonClientBusiness.processData(
						GeneralConstant.COMMON,
						CommonConstant.SAVE_TIME_PERMIT,
						null);
					if (!(UtilityMethods
						.isDsabldPlcrdEvent(
							caTransactionControl.getTransCd())))
					{
						Vector lvInv = null;
						MFVehicleData laMFVehicleData = null;
						laMFVehicleData =
							saCompTransData.getVehicleInfo();
						//Save doc number for TTL events
						if (caTransactionControl
							.getEventType()
							.equals(TTL))
						{
							if (laMFVehicleData.getTitleData() != null)
							{
								laMFVehicleData
									.getTitleData()
									.setDocNo(
									csTransId);
							}
						}
						lvInv = saCompTransData.getAllocInvItms();
						if (laMFVehicleData == null)
						{
							caCommonClientBusiness.processData(
								GeneralConstant.COMMON,
								CommonConstant.SAVE_VEH,
								null);
						}
						else
						{
							RegistrationData laRegistrationData =
								laMFVehicleData.getRegData();
							boolean lbIsAnnualPlt = false;
							if (lvInv != null && lvInv.size() > 0)
							{
								for (int i = 0; i < lvInv.size(); i++)
								{
									ProcessInventoryData laProcInvData =
										(
											ProcessInventoryData) lvInv
												.get(
											i);
									ItemCodesData laItmCdsData =
										ItemCodesCache.getItmCd(
											laProcInvData.getItmCd());
									if (laItmCdsData != null)
									{
										if (laItmCdsData.getItmCd()
											!= null)
										{
											if (laItmCdsData
												.getItmTrckngType()
												.equals("P"))
											{
												//Plate
												laRegistrationData
													.setRegPltNo(
													laProcInvData
														.getInvItmNo());
												laRegistrationData
													.setRegPltCd(
													laProcInvData
														.getItmCd());
												if (laProcInvData
													.getInvItmYr()
													!= 0)
												{
													laRegistrationData
														.setRegExpYr(
														laProcInvData
															.getInvItmYr());
												} 
												// defect 10951 
												laRegistrationData.setPltRmvCd(0); 
												// end defect 10951 
												
												//Check if it's annual plate
												// defect 9085 
												// PlateTypeCache replaces RegistrationRenewalsCache
												//RegistrationRenewalsData laRegRenewalsData =
												//	RegistrationRenewalsCache
												//		.getRegRenwl(
												//		laProcInvData
												//			.getItmCd());
												PlateTypeData laPlateTypeData =
													PlateTypeCache
														.getPlateType(
														laProcInvData
															.getItmCd());
												if (laPlateTypeData
													!= null)
												{
													if (laPlateTypeData
														.getAnnualPltIndi()
														== 1)
													{
														lbIsAnnualPlt =
															true;
													}
												}
											} // end defect 9085
											else if (
												laItmCdsData
													.getItmTrckngType()
													.equals(
													"S"))
											{
												//Sticker
												laRegistrationData
													.setRegStkrNo(
													laProcInvData
														.getInvItmNo());
												laRegistrationData
													.setRegStkrCd(
													laProcInvData
														.getItmCd());
												laRegistrationData
													.setRegExpYr(
													laProcInvData
														.getInvItmYr());
											}
										}
									}
									else
									{
										throw new RTSException(
											RTSException
												.FAILURE_MESSAGE,
											"Item codes for "
												+ laProcInvData
													.getItmCd()
												+ " is null",
											"ERROR");
									}
								}
							}

							boolean lbSetExpYr = false;
							if (saCompTransData.getOwnrSuppliedPltNo()
								!= null
								&& !saCompTransData
									.getOwnrSuppliedPltNo()
									.trim()
									.equals(
									""))
							{
								laRegistrationData.setRegPltNo(
									saCompTransData
										.getOwnrSuppliedPltNo());
								lbSetExpYr = true;
							}
							if (saCompTransData.getOwnrSuppliedStkrNo()
								!= null
								&& !saCompTransData
									.getOwnrSuppliedStkrNo()
									.trim()
									.equals(
									""))
							{
								laRegistrationData.setRegStkrNo(
									saCompTransData
										.getOwnrSuppliedStkrNo());
								lbSetExpYr = true;
							}

							if (lbSetExpYr)
							{
								laRegistrationData.setRegExpYr(
									saCompTransData
										.getVehicleInfo()
										.getRegData()
										.getOwnrSuppliedExpYr());
							} //set previous if orig regpltno != newregpltno
							//prev
							MFVehicleData laOrigMFVehicleData =
								saCompTransData.getOrgVehicleInfo();
							if (laOrigMFVehicleData != null)
							{
								RegistrationData laOrigRegData =
									laOrigMFVehicleData.getRegData();
								if ((laOrigRegData != null
									&& laOrigRegData.getRegPltNo()
										== null)
									|| ((laOrigRegData != null
										&& laOrigRegData.getRegPltNo()
											!= null
										&& laRegistrationData != null
										&& laRegistrationData
											.getRegPltNo()
											!= null)
										&& (!laOrigRegData
											.getRegPltNo()
											.equals(
												laRegistrationData
													.getRegPltNo()))))
								{
									laRegistrationData.setPrevExpMo(
										laOrigRegData.getRegExpMo());
									laRegistrationData.setPrevExpYr(
										laOrigRegData.getRegExpYr());
									laRegistrationData.setPrevPltNo(
										laOrigRegData.getRegPltNo());
								}
							}
							if (lbIsAnnualPlt)
							{
								laRegistrationData.setRegStkrNo("");
								laRegistrationData.setRegStkrCd("");
							}
							caCommonClientBusiness.processData(
								GeneralConstant.COMMON,
								CommonConstant.SAVE_VEH,
								laMFVehicleData);
						}
						caCommonClientBusiness.processData(
							GeneralConstant.COMMON,
							CommonConstant.SAVE_VEH_MISC,
							saCompTransData.getVehMisc());
						// Add to vector of Saved Inventory 	
						if (lvInv != null && lvInv.size() > 0)
						{
							caCommonClientBusiness.processData(
								GeneralConstant.COMMON,
								CommonConstant.SAVE_INVENTORY,
								saCompTransData.getAllocInvItms());
						}
					}
				}
			} //else if (sbAddlTransIndi)
			else if (sbCustomer)
			{
				caCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_TIME_PERMIT,
					null);
				caCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_VEH,
					null);
				caCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_VEH_MISC,
					null);
				if (!caTransactionControl
					.getTransCd()
					.equals(TransCdConstant.SBRNW))
				{
					caCommonClientBusiness.processData(
						GeneralConstant.COMMON,
						CommonConstant.SAVE_INVENTORY,
						saCompTransData.getAllocInvItms());
				}

			}
			else
			{
				caCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_TIME_PERMIT,
					null);
				caCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_VEH,
					null);
				caCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_VEH_MISC,
					null);
				caCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_INVENTORY,
					null);
				// defect 9085 
				caCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.SAVE_VI_SPCL_PLT_VECTOR,
					null);
				// end defect 9085 
				setSavedTransCd(null);
			}
		} //serialize and save the vehicle etc
		saveVehicle();
	}
	/**
	 * Save vehicle, or save time permit
	 * 
	 * @throws RTSException 
	 */

	private void saveVehicle() throws RTSException
	{
		//find directory name	
		//clear out directory
		String lsRootDir = SystemProperty.getSetAsideDir();
		File laRootDir = new File(lsRootDir);
		if (!laRootDir.exists())
		{
			laRootDir.mkdir();
		}
		StringBuffer lsStringBuffer = new StringBuffer();
		lsStringBuffer.append(lsRootDir);
		lsStringBuffer.append(CSN);
		lsStringBuffer.append(getPaddedCustSeqNo());
		lsStringBuffer.append(BACK_SLASH);
		String lsFileDir = lsStringBuffer.toString();
		File laFileDir = new File(lsFileDir);
		if (laFileDir.exists())
		{
			File[] larrFileList = laFileDir.listFiles();
			for (int i = 0; i < larrFileList.length; i++)
			{
				larrFileList[i].delete();
			}
		}
		else
		{
			laFileDir.mkdir();
		} //save any information required     
		CommonClientBusiness laCommonClientBusiness =
			new CommonClientBusiness();
		// defect 9085
		Object laSpclPltRegisData =
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.GET_SAVED_SPCL_PLT,
				null);
		Object laSaveSpclPlt =
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.GET_VI_SPCL_PLT_VECTOR,
				null);
		// end defect 9085 
		// defect 10617 
		Object laSavePermitData =
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.GET_VI_PRMT_VECTOR,
				null);
		// end defect 10617 
		Object laSaveInv =
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.GET_SAVED_INVENTORY,
				null);
		Object laSaveVeh =
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.GET_SAVED_VEH,
				null);
		Object laSaveVehMisc =
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.GET_VEH_MISC,
				null);
		Object laSaveTimePermit =
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.GET_TIME_PERMIT,
				null);
		//		Object laSaveDPCustData =
		//			laCommonClientBusiness.processData(
		//				GeneralConstant.COMMON,
		//				CommonConstant.GET_DP_CUST_DATA,
		//				null);
		// defect 7717 
		// Save to SetAside Directory 
		saTransHdrData.setCancelTrans(isCancelTrans());
		serializeFile(lsFileDir + SAVED_TRANS_HDR, saTransHdrData);
		// end defect 7717 
		// defect 9085 	
		if (laSpclPltRegisData instanceof SpecialPlatesRegisData)
		{
			serializeFile(
				lsFileDir + SAVED_SPCL_PLT,
				laSpclPltRegisData);
		} //		// 6.6.0 
		//		if (laSaveDPCustData instanceof DisabledPlacardCustomerData)
		//		{
		//			serializeFile(lsFileDir + "DPCUST.DAT", laSaveDPCustData);
		//		}
		if (laSaveSpclPlt instanceof Vector)
		{
			serializeFile(
				lsFileDir + SAVED_MFG_SPCL_PLT_VECTOR,
				laSaveSpclPlt);
		} // end defect 9085 
		if (laSaveInv instanceof Vector)
		{
			serializeFile(lsFileDir + SAVED_INVENTORY, laSaveInv);
		}

		if (laSaveVeh instanceof MFVehicleData)
		{
			serializeFile(lsFileDir + SAVED_MF_VEHICLE, laSaveVeh);
		}
		if (laSaveVehMisc instanceof VehMiscData)
		{
			serializeFile(
				lsFileDir + SAVED_MISC_VEHICLE,
				laSaveVehMisc);
		}
		if (laSaveTimePermit instanceof TimedPermitData)
		{
			serializeFile(
				lsFileDir + SAVED_TIME_PERMIT,
				laSaveTimePermit);
		} // defect 10617 
		if (laSavePermitData instanceof Vector)
		{
			serializeFile(lsFileDir + SAVED_PRMT_VCT, laSavePermitData);
		} // end defect 10617 
	}
	/**
	 * Write serialized transaction data to be used by receipts to file. 
	 * 
	 * @param asDirName String 
	 * @throws RTSException
	 */
	private static void serializeCompleteTrans(String asDirName)
		throws RTSException
	{
		try
		{
			String lsObj = Comm.objToString(saCompTransData);
			FileOutputStream laFileOutStream =
				new FileOutputStream(asDirName, true);
			OutputStreamWriter laOutputStreamWtr =
				new OutputStreamWriter(laFileOutStream);
			BufferedWriter laBuffWtr =
				new BufferedWriter(laOutputStreamWtr);
			laBuffWtr.write(lsObj);
			laBuffWtr.flush();
			laFileOutStream.close();
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}
	}
	/**
	 * Serialize objects to filename for set aside
	 *
	 * @param asFileName String
	 * @param aaObject Object
	 * @throws RTSException 
	 */
	private void serializeFile(String asFileName, Object aaObject)
		throws RTSException
	{
		try
		{
			FileOutputStream laFileOutputStream =
				new FileOutputStream(asFileName);
			ObjectOutputStream laObjectOutputStream =
				new ObjectOutputStream(laFileOutputStream);
			laObjectOutputStream.writeObject(aaObject);
			laObjectOutputStream.flush();
			laObjectOutputStream.close();
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}
	}
	/**
	 * Serialize Object to File
	 * 
	 * @param  asFilename String
	 * @param  aaObject Object
	 * @throws RTSException
	 */

	public static void serializeObject(
		String asFileName,
		Object aaObject)
		throws RTSException
	{
		try
		{
			FileOutputStream laFileOutputStream =
				new FileOutputStream(asFileName);
			ObjectOutputStream laObjectOutputStream =
				new ObjectOutputStream(laFileOutputStream);
			laObjectOutputStream.writeObject(aaObject);
			laObjectOutputStream.flush();
			laObjectOutputStream.close();
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}
	}
	/**
	 * Perform set aside operation for transaction
	 *
	 * @param  asName String 
	 * @return int
	 * @throws RTSException
	 */
	public int setAside(String asName) throws RTSException
	{
		if (!asName.equals(""))
		{
			//update TransactionHeader
			TransactionHeaderData laTransHdrData =
				(TransactionHeaderData) UtilityMethods.copy(
					saTransHdrData);
			laTransHdrData.setTransName(asName);
			// DbTypeIndi indicates which update statement to use
			// TransName vs. TransTimestmp 
			laTransHdrData.setDbTypeIndi(
				TransactionHeaderData.TRANS_NAME);
			// defect 10085
			// Corrected setting of TransTime  
			laTransHdrData.setTransTime(
			//new RTSDate().getYYYYMMDDDate());
			ciTransTime);
			// end defect 10085
			Vector lvInput = new Vector();
			TransactionCacheData laTransCacheData =
				new TransactionCacheData();
			laTransCacheData.setProcName(TransactionCacheData.UPDATE);
			laTransCacheData.setObj(laTransHdrData);
			lvInput.addElement(laTransCacheData);
			// defect 4891
			processCacheVector(lvInput);
			// end defect 4891
		} //Set up for next customer transaction
		reset();
		return SUCCESS;
	}
	/**
	 * Set time added to transaction Cache
	 * 
	 * @param aaObject Object
	 * @param aiCacheTransTime	int
	 */
	public void setCacheTransTime(
		Object aaObject,
		int aiCacheTransTime)
	{
		if (aaObject == null || aiCacheTransTime == 0)
		{
			return;
		}
		if (aaObject instanceof MotorVehicleFunctionTransactionData)
		{
			(
				(
					MotorVehicleFunctionTransactionData) aaObject)
						.setCacheTransTime(
				aiCacheTransTime);
			(
				(
					MotorVehicleFunctionTransactionData) aaObject)
						.setCacheTransAMDate(
				((MotorVehicleFunctionTransactionData) aaObject)
					.getTransAMDate());
		}
		else if (aaObject instanceof TransactionData)
		{
			((TransactionData) aaObject).setCacheTransTime(
				aiCacheTransTime);
			((TransactionData) aaObject).setCacheTransAMDate(
				((TransactionData) aaObject).getTransAMDate());
		} // defect 10491 
		else if (aaObject instanceof PermitTransactionData)
		{
			((PermitTransactionData) aaObject).setCacheTransTime(
				aiCacheTransTime);
			((PermitTransactionData) aaObject).setCacheTransAMDate(
				((PermitTransactionData) aaObject).getTransAMDate());
		} // end defect 10491 
		else if (aaObject instanceof TransactionFundsDetailData)
		{
			((TransactionFundsDetailData) aaObject).setCacheTransTime(
				aiCacheTransTime);
			(
				(
					TransactionFundsDetailData) aaObject)
						.setCacheTransAMDate(
				((TransactionFundsDetailData) aaObject)
					.getTransAMDate());
		}
		else if (aaObject instanceof TransactionInventoryDetailData)
		{
			(
				(
					TransactionInventoryDetailData) aaObject)
						.setCacheTransTime(
				aiCacheTransTime);
			(
				(
					TransactionInventoryDetailData) aaObject)
						.setCacheTransAMDate(
				((TransactionInventoryDetailData) aaObject)
					.getTransAMDate());
		}
		else if (aaObject instanceof FundFunctionTransactionData)
		{
			((FundFunctionTransactionData) aaObject).setCacheTransTime(
				aiCacheTransTime);
			(
				(
					FundFunctionTransactionData) aaObject)
						.setCacheTransAMDate(
				((FundFunctionTransactionData) aaObject)
					.getTransAMDate());
		}
		else if (aaObject instanceof InventoryFunctionTransactionData)
		{
			(
				(
					InventoryFunctionTransactionData) aaObject)
						.setCacheTransTime(
				aiCacheTransTime);
			(
				(
					InventoryFunctionTransactionData) aaObject)
						.setCacheTransAMDate(
				((InventoryFunctionTransactionData) aaObject)
					.getTransAMDate());
		}
	}
	/**
	 * Set the Cancel Trans Indicator
	 *
	 * @param abCancelTrans boolean
	 */
	public static void setCancelTrans(boolean abCancelTrans)
	{
		sbAllowCancelTrans = abCancelTrans;
	}

	/**
	 * Set the amount for Credit Remaining in the current Transaction 
	 * context
	 * 
	 * @param aaCreditRemaining .Dollar
	 */
	public static void setCreditRemaining(Dollar aaCreditRemaining)
	{
		saCreditRemaining = aaCreditRemaining;
	}

	/**
	 * Set the cumulative trans indicator
	 * 
	 * @param aiCumulativeTransIndi Set to 1 if this is a 'cumulative' 
	 *		transaction
	 */
	public static void setCumulativeTransIndi(int aiCumulativeTransIndi)
	{
		siCumulativeTransIndi = aiCumulativeTransIndi;
	}

	/**
	 * Set the current DTA data object
	 */
	private void setCurrDlrTtlData()
	{
		if (caTransactionControl.getEventType().equals(DTA))
		{
			caCurrDlrTtlData = saCompTransData.getDlrTtlData();
			// for (int i = 0;
			// i < saCompTransData.getDlrTtlDataObjs().size();
			// i++)
			//			{
			//if (((DealerTitleData) saCompTransData
			//	.getDlrTtlDataObjs()
			//	.get(i))
			//	.isProcessed())
			//{
			//	caCurrDlrTtlData =
			//		(DealerTitleData) saCompTransData
			//			.getDlrTtlDataObjs()
			//			.get(
			//			i);
			//}
			//			}
		}
	}

	/**
	 * Set the customer name for the transaction set.
	 * 
	 * @param asCustName String 
	 */
	private static void setCustName(String asCustName)
	{
		ssCustName = asCustName;
	}

	/**
	 * Set DTA information
	 * 
	 * @param avDTADlrTtlData Vector
	 */
	public static void setDTADlrTtlData(Vector avDTADlrTtlData)
	{
		svDTADlrTtlData = avDTADlrTtlData;
	}

	/**
	 * Set DTA information
	 * 
	 * @param avDTAOrigDlrTtlData Vector
	 */
	public static void setDTAOrigDlrTtlData(Vector avDTAOrigDlrTtlData)
	{
		svDTAOrigDlrTtlData = avDTAOrigDlrTtlData;
	}

	/**
	 * Set the last CSN, remove COMPTRAN.DAT 
	 *
	 * @throws RTSException 
	 */
	private void setLastCSN() throws RTSException
	{
		String lsReceiptRootDir = SystemProperty.getReceiptsDirectory();
		String lsLocationForReceipt = lsReceiptRootDir;
		File laLastCSNFile =
			new File(
				lsLocationForReceipt + BACK_SLASH + LAST_CSN_FNAME);
		String lsCustSeqNo = getPaddedCustSeqNo();
		lsLocationForReceipt = lsReceiptRootDir + CSN + lsCustSeqNo;
		File laTransObjFile =
			new File(
				lsLocationForReceipt
					+ BACK_SLASH
					+ COMPLETE_TRANS_FILE_NAME);
		if (laTransObjFile.exists())
		{
			laTransObjFile.delete();
		}
		try
		{
			if (!laLastCSNFile.exists())
			{
				laLastCSNFile.createNewFile();
			}
		}
		catch (IOException aeIOExp)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOExp);
		}
		try
		{
			FileOutputStream laFileOutStream =
				new FileOutputStream(
					laLastCSNFile.getAbsolutePath(),
					false);
			OutputStreamWriter laOutputStreamWtr =
				new OutputStreamWriter(laFileOutStream);
			BufferedWriter laBuffWtr =
				new BufferedWriter(laOutputStreamWtr);
			laBuffWtr.write(CSN + lsCustSeqNo);
			laBuffWtr.newLine();
			laBuffWtr.flush();
			laBuffWtr.close();
		}
		catch (FileNotFoundException aeFNFEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeFNFEx);
		}
		catch (IOException aeIOExp)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOExp);
		}
	}

	/**
	 * Set the indicator for multiple transaction.
	 *
	 * @param abMultipleTrans	boolean
	 */
	public static void setMultipleTrans(boolean abMultipleTrans)
	{
		sbMultipleTrans = abMultipleTrans;
	}
	public static boolean getSavedSpecialPlate()
	{
		return sbSavedSpclPlate;
	}

	public static void setSavedSpecialPlate(boolean abSavedSpclPlt)
	{
		sbSavedSpclPlate = abSavedSpclPlt;
	}

	/**
	 * Set the indicator for print cash receipt
	 *
	 * @param abPrintCashReceipt boolean
	 */
	public static void setPrintCashReceipt(boolean abPrintCashReceipt)
	{
		sbPrintCashReceipt = abPrintCashReceipt;
	}

	/**
	 * Set the receipt directory for a customer-based transaction
	 * 
	 * @param asRcptDirForCust String
	 */
	public static void setRcptDirForCust(String asRcptDirForCust)
	{
		ssRcptDirForCust = asRcptDirForCust;
	}
	/**
	 * Set the rcptlog for a customer-based transaction
	 * 
	 * @param asRcptLog String 
	 */
	private static void setRcptLog(String asRcptLog)
	{
		ssRcptLogFile = asRcptLog;
	}

	/**
	 * Set the indicator to show whether the current transaction is a 
	 * restore-set-aside transaction.
	 * 
	 * @param abRestoredSetAside  boolean 
	 */
	public static void setRestoredSetAside(boolean abRestoredSetAside)
	{
		sbRestoredSetAside = abRestoredSetAside;
	}

	/**
	 * Set the subtotal for the current transaction
	 * 
	 * @param aaRunningSubtotal Dollar
	 */

	public static void setRunningSubtotal(Dollar aaRunningSubtotal)
	{
		saRunningSubtotal = aaRunningSubtotal;
	}

	/**
	 * Set the saved trans code.
	 * 
	 * @param asSavedTransCd String 
	 */
	private static void setSavedTransCd(String asSavedTransCd)
	{
		ssSavedTransCd = asSavedTransCd;
	}

	/**
	 * Set Transaction SavedTransTime
	 * Used in Subcon Modify to create replacement transaction with
	 * saved transaction time
	 * 
	 * @param aiSavedTransTime int
	 */
	public void setSavedTransTime(int aiSavedTransTime)
	{
		ciSavedTransTime = aiSavedTransTime;
	}

	/**
	 * Set the stored PrintIndi
	 * 
	 * @param aiStoredPrintIndi int
	 */
	public static void setStoredPrintIndi(int aiStoredPrintIndi)
	{
		siStoredPrintIndi = aiStoredPrintIndi;
	}

	/**
	 * Set the transaction header
	 * 
	 * @param aaTransHdrData TransactionHeaderData 
	 */
	public static void setTransactionHeaderData(TransactionHeaderData aaTransHdrData)
	{
		saTransHdrData = aaTransHdrData;
	}

	/**
	 * Set the transaction id list.
	 *
	 * @param avTransIdList Vector
	 */
	public static void setTransIdList(Vector avTransIdList)
	{
		svTransIdList = avTransIdList;
	}

	/**
	 * Set the transaction time
	 *
	 * @param aiTransTime
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * Set up the transaction data for posting.
	 *
	 * @throws RTSException 
	 */
	private void setUpTransaction() throws RTSException
	{
		sbHeaderAdded = false;
		// 	Set up First Transaction of the day if required
		setUpFirstTransactionOfDay();
		// Determine if Customer Transaction vs. Back Office 
		sbCustomer =
			!(isTreatedAsBackOfficeTransaction(caTransactionControl,
				saCompTransData));
		preTransaction();
		// get current DTA object reference
		setCurrDlrTtlData();
		// If Customer Transaction
		if (sbCustomer)
		{ // First transaction of transaction set
			if (saTransHdrData == null)
			{
				saTransHdrData = addTransHeader(saCompTransData, false);
				sbHeaderAdded = true;
				// defect 10290 
				if (!UtilityMethods
					.isDTA(caTransactionControl.getTransCd()))
				{
					resetDTA();
				} // end defect 10290 
				// PROC Create/Clear out receipt directory
				createReceiptDirectory(false);
				//	if (caTransactionControl.getCustomer() == BACK_OFFICE)
				//	{
				//		sbAddlTransIndi = false;
				//	}
				//	else
				//	{
				//		sbAddlTransIndi = true;
				//	}
				cvTransaction.addAll(
					populateTransData(saCompTransData, saTransHdrData));
			} // In more trans, saTransactionHeaderData will be populated
			else
			{
				cvTransaction.addAll(
					populateTransData(saCompTransData, null));
			}
		} // Back Office Transactions 
		else
		{
			//Assign RTS_Trans_Hdr fields for BackOffice transaction
			saTransHdrData = addTransHeader(saCompTransData, true);
			// Create/Clear out receipt directory; Disregard date
			createReceiptDirectory(false);
			// Back Office Trans do not generate, save or print rcpt 
			// sbAddlTransIndi = false;
			// Moved PCR 34 
			cvTransaction.addAll(
				populateTransData(saCompTransData, null));
		} //Save transaction information to cust subtotal table
		//Done using static variable in CompleteTransactionData
		csTransId = getTransId();
		saCompTransData.setTransId(csTransId);
		// Removed from CommonClientBusiness 
		// set transid to dealer object if DTA
		// Update Transaction Vector 
		if (caTransactionControl.getEventType().equals(DTA))
		{
			saCompTransData.getDlrTtlData().setTransId(csTransId);
			svDTADlrTtlData.set(
				siDTADlrTtlDataIndex,
				(DealerTitleData) UtilityMethods.copy(
					saCompTransData.getDlrTtlData()));
		}
		svTransIdList.addElement(csTransId);
	}

	/**
	 * Set up the first Transaction of the day. This is done by resetting 
	 * the CustomerSeqNo, setting up a back office TransHdr (custseqno=0), 
	 * and writing it to the database.
	 *
	 * @throws RTSException 
	 */
	public void setUpFirstTransactionOfDay() throws RTSException
	{ // defect 8285 
		if (!Comm.isServer())
		{
			RTSDate laRTSDateToday = new RTSDate();
			RTSDate laRTSDateCustSeq =
				CustomerSequence.getInstance().getDate();
			if (!((laRTSDateToday.getDate()
				== laRTSDateCustSeq.getDate())
				&& (laRTSDateToday.getMonth()
					== laRTSDateCustSeq.getMonth())
				&& (laRTSDateToday.getYear()
					== laRTSDateCustSeq.getYear())))
			{
				//Reset CustomerSequence
				CustomerSequence.getInstance().reset();
				// Setup TransHeader for backoffice
				TransactionHeaderData laTransHdrData =
					new TransactionHeaderData();
				laTransHdrData.setTransWsId(
					SystemProperty.getWorkStationId());
				laTransHdrData.setTransAMDate(
					new RTSDate().getAMDate());
				laTransHdrData.setCustSeqNo(
					CustomerSequence.getInstance().getCustSeqNo());
				laTransHdrData.setTransTime(ciTransTime);
				laTransHdrData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				laTransHdrData.setOfcIssuanceCd(
					SystemProperty.getOfficeIssuanceCd());
				laTransHdrData.setSubstaId(
					SystemProperty.getSubStationId());
				laTransHdrData.setVersionCd(
					SystemProperty.getMainFrameVersion());
				laTransHdrData.setTransEmpId(
					SystemProperty.getCurrentEmpId());
				// set TransWsId
				AssignedWorkstationIdsData laAssgndWsIdsData =
					AssignedWorkstationIdsCache.getAsgndWsId(
						SystemProperty.getOfficeIssuanceNo(),
						SystemProperty.getSubStationId(),
						SystemProperty.getWorkStationId());
				// defect 10427
				// Implement OfficeTimeZoneCache
				// Could not startup if record no found in 
				//    AssignedWorkstationIdsCache			
				// if (laAssgndWsIdsData == null)
				//	{
				//		throw new RTSException(
				//			RTSException.FAILURE_MESSAGE,
				//			"AssignedWorkstationIdsData is null",
				//			"Error");
				//	}
				// defect 8004 
				//If Mountain Time, Subtract one hour for Timestamp 
				RTSDate laRTSDate = new RTSDate();
				//	if (laAssgndWsIdsData
				//		.getTimeZone()
				//		.equalsIgnoreCase("M"))
				//		{
				if (OfficeTimeZoneCache
					.isMountainTimeZone(
						SystemProperty.getOfficeIssuanceNo()))
				{
					laRTSDate.setHour(laRTSDate.getHour() - 1);
					// end defect 10427 
				}
				laTransHdrData.setCashWsId(
					laAssgndWsIdsData.getCashWsId());
				laTransHdrData.setTransName(BACK_OFFICE_TRANSNAME);
				laTransHdrData.setFeeSourceCd(0);
				//laTransHdrData.setTransTimestmp(new RTSDate());
				laTransHdrData.setTransTimestmp(laRTSDate);
				// end defect 8004
				TransactionCacheData laTransCacheData =
					new TransactionCacheData();
				laTransCacheData.setProcName(
					TransactionCacheData.INSERT);
				laTransCacheData.setObj(laTransHdrData);
				// we are client side
				cvTransaction.addElement(laTransCacheData);
				try
				{
					// delete Cumulative Receipt Log 
					deleteCumulativeReceiptLog();
					// defect 7019 
					// delete Void Trans Log 
					deleteVoidTransLog();
					// end defect 7019
					processCacheVector(cvTransaction);
					// end defect 4891
				}
				catch (RTSException aeRTSEx)
				{
					// Previously just eating this exception
					aeRTSEx.displayError();
				}
				catch (Exception aeEx)
				{
				}
				laTransHdrData = null;
				cvTransaction = new Vector();
			}
		}
		else
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"This should not be called from the Server",
				"Error");
		} // end defect 8285 
	}

	/**
	* Add transId for RNR / ADDR to receipt log
	* 
	* @throws RTSException
	*/
	private void updateRcptLogAddrRnr() throws RTSException
	{

		String lsTransCd = saCompTransData.getTransCode();
		if (lsTransCd.equals(TransCdConstant.ADDR)
			|| lsTransCd.equals(TransCdConstant.RNR))
		{
			String lsTransCdDesc = null;
			String lsVIN = null;
			TransactionCodesData laTransCdsData =
				(
					TransactionCodesData) TransactionCodesCache
						.getTransCd(
					lsTransCd);
			if (laTransCdsData == null)
			{
				lsTransCdDesc = "DESCRIPTION NOT FOUND";
			}
			else
			{
				lsTransCdDesc = laTransCdsData.getTransCdDesc();
			}
			if ((saCompTransData.getVehicleInfo() == null)
				|| saCompTransData.getVehicleInfo().getVehicleData()
					== null
				|| saCompTransData
					.getVehicleInfo()
					.getVehicleData()
					.getVin()
					== null)
			{
				lsVIN = "NO VIN";
			}
			else
			{
				lsVIN =
					saCompTransData
						.getVehicleInfo()
						.getVehicleData()
						.getVin();
			}
			updateRcptLog(
				"",
				"",
				csTransId,
				lsTransCdDesc,
				lsVIN,
				lsTransCd);
		}
	}

	/** 
	 * This method will:
	 * 	  <ul>
	 * 	  <li> Serialize CompleteTransactionData
	 * 	  <li> Save Receipt
	 * 	  <li> Set Flag for CashReceipt Printing
	 * 	  <li> Print Receipts if appropriate
	 * 	  <eul> 
	 * 
	 * @throws RTSException
	 */
	private void processReceipt() throws RTSException
	{
		// Serialize CompleteTransaction Data Object.
		// If only one transaction, will deserialize,add payment 
		// information and regenerate receipt.
		boolean lbCallPrintDoc = false;
		// defect 8886
		// Do not use local variable
		// boolean lbPrintReceipt = caTransactionControl.isPrintReceipt();
		// end defect 8886 
		String lsTransCd = caTransactionControl.getTransCd();
		// defect 8625 
		// Restore test to that of prior version
		// Control Flags are representative
		// if (sbHeaderAdded)
		if (sbHeaderAdded
			&& !lsTransCd
				.equals(TransCdConstant.SBRNW))
		{
			//if (lbPrintReceipt && !sbAutoEndTrans)
			//	{
			serializeCompleteTrans(
				ssRcptDirForCust + COMPLETE_TRANS_FILE_NAME);
			//}
		} // end defect 8625
		else if (sbCustomer)
		{
			sbMultipleTrans = true;
			setPrintCashReceipt(true);
		}

		if (SystemProperty.isRegion()
			&& lsTransCd.equals(TransCdConstant.CCO))
		{
			printDocument(false);
			lbCallPrintDoc = true;
			caTransactionControl.setPrintReceipt(true);
			// defect 7717 
			setCancelTrans(false);
			// end defect 7717 
		} // defect 8886
		
		//defect 11052
		if (UtilityMethods.isVTR275TransCd(lsTransCd))
		{
			boolean lbFileCreatedAndPrinted = (new PrintGraphics()).
				createForm275(saCompTransData, getRcptDirForCust(), null);
			TransactionCodesData laTransCdData =
				TransactionCodesCache.getTransCd(lsTransCd);
			String lsTransCdDesc = laTransCdData.getTransCdDesc();
			String lsVIN =
				saCompTransData
					.getVehicleInfo()
					.getVehicleData()
					.getVin();
			updateRcptLog(
					saCompTransData.getTransId() + ".JPG",
					new String(),
					saCompTransData.getTransId(),
					lsTransCdDesc,
					lsVIN,
					lsTransCd);
			
			if (sbAutoEndTrans || SystemProperty.getPrintImmediateIndi() == 1)
			{
				String lsLocationForReceipt =
					SystemProperty.getReceiptsDirectory() + CSN + getPaddedCustSeqNo() + BACK_SLASH;
				String lsPrintFile = lsLocationForReceipt + saCompTransData.getTransId() + ".JPG"; 
				(new PrintGraphics()).printForm275JPG(lsPrintFile);
			}
			else
			{
				// Ensure Cash Register Receipt printed
				setMultipleTrans(true);
			}
		}
		else
		{
			//end defect 11052

			// Do not use local variable as CCO was not printing 1st
			// receipt of the day.  
			// if (lbPrintReceipt
			if (caTransactionControl.isPrintReceipt()
					&& !(lsTransCd.equals(TransCdConstant.VEHINQ)
							&& saCompTransData.getPrintOptions()
							== VehicleInquiryData.VIEW_ONLY))
			{
				Vector lvVector = new Vector();
				lvVector.add(saCompTransData);
				String lsRcptContent = formatReceipt(lvVector);
				lvVector.add(lsRcptContent);
				lvVector.add(barcodeNeeded(lvVector));
				lvVector.add(permitNeeded(lvVector));
				// defect 8524
				// FormFeed constant cleanup 
				if (lsRcptContent != null
						&& !lsRcptContent.equals("" + ReportConstant.FF)
						|| lsTransCd.equals(TransCdConstant.DPSPPT))
				{
					// end defect 8524
					csRcptFileName = saveReceipt(lvVector);
				}

			} // end defect 8886 
			else if (lbCallPrintDoc == false)
			{
				printDocument(false);
			} // do not print if sbAutoEndTrans & PrintImmediate 
			if (!sbAutoEndTrans
					&& SystemProperty.getPrintImmediateIndi() != 0
					&& csRcptFileName != null)
			{
				Print laPrint = new Print();
				// defect 7430
				//  If DTA and Print Sticker, pass TransCd = DLRSTKR
				//  else use existing TransCd
				//if (UtilityMethods.isDTA(lsTransCd)
				if (caTransactionControl.getEventType().equals(DTA)
						//&& isDTAPrintSticker(saCompTransData))
						&& caCurrDlrTtlData.isToBePrinted())
				{
					laPrint.sendToPrinter(csRcptFileName, DLRSTKR);
				}
				else
				{
					laPrint.sendToPrinter(csRcptFileName, lsTransCd);
				} // end defect 7430
				setPrintCashReceipt(true);
				setCancelTrans(false);
			} // end defect 7219 
		}
	}

	/**
	 * 
	 * Update Special Plate Virtual Inventory for Void  
	 * 
	 * @param aaComplTransData
	 * @param avTrxCache
	 * @throws RTSException
	 */
	private void updateSpclPltVITransTime(
		Vector avTrxCache,
		CompleteTransactionData aaComplTransData)
		throws RTSException
	{
		if (aaComplTransData.getVehicleInfo() != null)
		{
			SpecialPlatesRegisData laSpclPltRegisData =
				aaComplTransData.getVehicleInfo().getSpclPltRegisData();
			if (laSpclPltRegisData != null
				&& laSpclPltRegisData.getVIAllocData() != null
				&& laSpclPltRegisData.getVIAllocData().getTransTime()
					== 0)
			{
				InventoryAllocationData laInvAllData =
					laSpclPltRegisData.getVIAllocData();
				laInvAllData.setTransTime(ciTransTime);
				laInvAllData.setCacheTransAMDate(
					new RTSDate().getAMDate());
				laInvAllData.setCacheTransTime(ciTransTime);
				TransactionCacheData laTransCacheData =
					new TransactionCacheData();
				laTransCacheData.setObj(laInvAllData);
				laTransCacheData.setProcName(
					TransactionCacheData.UPDVITRANSTIME);
				avTrxCache.addElement(laTransCacheData);
			}
		}
	}
	/**
	 * 
	 * Update Prmt VI Trans Time   
	 * 
	 * @param aaComplTransData
	 * @param avTrxCache
	 * @throws RTSException
	 */
	private void updatePrmtVITransTime(
		Vector avTrxCache,
		CompleteTransactionData aaComplTransData)
		throws RTSException
	{
		String lsTransCd = aaComplTransData.getTransCode();
		if (UtilityMethods.isPermitApplication(lsTransCd)
			&& aaComplTransData.getTimedPermitData() != null)
		{
			PermitData laPrmtData =
				(PermitData) aaComplTransData.getTimedPermitData();
			if (laPrmtData.getVIAllocData() != null
				&& laPrmtData.getVIAllocData().getTransTime() == 0)
			{
				InventoryAllocationData laInvAllData =
					laPrmtData.getVIAllocData();
				laInvAllData.setTransTime(ciTransTime);
				laInvAllData.setCacheTransAMDate(
					new RTSDate().getAMDate());
				laInvAllData.setCacheTransTime(ciTransTime);
				TransactionCacheData laTransCacheData =
					new TransactionCacheData();
				laTransCacheData.setObj(laInvAllData);
				laTransCacheData.setProcName(
					TransactionCacheData.UPDVITRANSTIME);
				avTrxCache.addElement(laTransCacheData);
			}
		}
	}
	/**
	 * This method updates the Receipt Log File with the transaction
	 * information.
	 * If the file does not exist, create it.
	 * 
	 * @param asFileNameToLog 	String
	 * @param asBarCodeFileName String
	 * @param asTransactionId 	String
	 * @param asTransCdDesc 	String
	 * @param asVIN  			String
	 * @param asTransCd  		String
	 * @throws RTSException
	 */
	private void updateRcptLog(
		String asFileNameToLog,
		String asBarCodeFileName,
		String asTransactionId,
		String asTransCdDesc,
		String asVIN,
		String asTransCd)
		throws RTSException
	{
		updateRcptLog(
			asFileNameToLog,
			asBarCodeFileName,
			new String(),
			asTransactionId,
			asTransCdDesc,
			asVIN,
			asTransCd,
			new String());
	}
	/**
	 * This method updates the Receipt Log File with the transaction
	 * information.
	 * If the file does not exist, create it.
	 * 
	 * @param asFileNameToLog 	String
	 * @param asBarCodeFileName String
	 * @param asPermitFileName 	String
	 * @param asTransactionId 	String
	 * @param asTransCdDesc 	String
	 * @param asVIN  			String
	 * @param asTransCd  		String
	 * @param asPrmtIssuanceId	String
	 * @throws RTSException
	 */
	private void updateRcptLog(
		String asFileNameToLog,
		String asBarCodeFileName,
		String asPermitFileName,
		String asTransactionId,
		String asTransCdDesc,
		String asVIN,
		String asTransCd,
		String asPrmtIssuanceId)
		throws RTSException
	{
		try
		{
			// defect 11152 
			asVIN = asVIN == null ? new String() : asVIN;
			// end defect 11152 
			
			// create receipt log here
			setRcptLog(getRcptDirForCust() + RCPTLOG);
			File laRcptLog = new File(getRcptLog());
			if (!laRcptLog.exists())
			{
				laRcptLog.createNewFile();
			} // format line for receipt log
			StringBuffer lsLogFileInput = new StringBuffer("");
			lsLogFileInput.append(asFileNameToLog);
			lsLogFileInput.append(",");
			lsLogFileInput.append(asBarCodeFileName);
			lsLogFileInput.append(",");
			lsLogFileInput.append(asPermitFileName);
			lsLogFileInput.append(",");
			lsLogFileInput.append(asTransactionId);
			lsLogFileInput.append(",");
			lsLogFileInput.append(asTransCdDesc);
			lsLogFileInput.append(",");
			lsLogFileInput.append(asVIN);
			lsLogFileInput.append(",");
			lsLogFileInput.append(asTransCd);
			// defect 10844 
			lsLogFileInput.append(",");
			lsLogFileInput.append(asPrmtIssuanceId);
			// end defect 10844 
			// write the line to the ReceiptLog
			FileOutputStream laFileOutStream =
				new FileOutputStream(getRcptLog(), true);
			OutputStreamWriter laOutputStreamWtr =
				new OutputStreamWriter(laFileOutStream);
			BufferedWriter laBuffWtr =
				new BufferedWriter(laOutputStreamWtr);
			laBuffWtr.write(lsLogFileInput.toString());
			laBuffWtr.newLine();
			laBuffWtr.flush();
			laFileOutStream.close();
		}
		catch (FileNotFoundException aeFNFEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeFNFEx);
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}
	}
	/**
	 * Process ReprintData request
	 * Insert RPRSTK transaction into RTS_TRANS
	 * Update RTS_RPRNT_STKR with new reprint quantity (insert or update) 
	 * 
	 * @param  aaReprintData ReprintData
	 * @throws RTSException
	 */
	public static void updateReprintSticker(ReprintData aaReprintData)
		throws RTSException
	{
		TransactionCacheData laTransCacheData =
			new TransactionCacheData();
		// defect 7349
		// Use Transaction.postTrans() to insert RPRSTK transaction as
		//  well as update Reprint Sticker Table 
		laTransCacheData.setObj(aaReprintData);
		laTransCacheData.setProcName(TransactionCacheData.INSERT);
		Vector lvTransCacheData = new Vector();
		lvTransCacheData.add(laTransCacheData);
		Transaction.writeToCache(lvTransCacheData);
		postTrans(lvTransCacheData);
		// end defect 7349
	}
	/**
	 * Writes transaction data to the local trxcache tables. This process 
	 * happens both in server up and server down phases.  This method 
	 * takes a vector of TransactionCacheData as arguments.
	 * A return of:<br>
	 * 1 indicates a successful write ino cache and<br>
	 * 0 indicates a failure. <br>
	 * 
	 * @param  avTransCacheData Vector 
	 * @return int
	 */
	public static int writeToCache(Vector avTransCacheData)
	{
		try
		{
			// defect 11073
			// test the current cache file in an attempt to ensure that it 
			// is not corrupted
			if (!isCurrentCacheValid()) 
			{
				return FAILURE;
			}
			// end defect 11073
			
			Map laMap = cacheIO(WRITE, avTransCacheData, null);
			return ((Integer) laMap.get("INT")).intValue();
		}
		catch (RTSException aeRTSEx)
		{
			// defect 6110
			aeRTSEx.writeExceptionToLog();
			// end defect 6110
			return FAILURE;
		}
	}

	
	/**
	 * Verify that the current cache file is not corrupted.
	 * 
	 * @return boolean
	 */
	public static boolean isCurrentCacheValid()
	{
		boolean lbValidCache = false;
		try
		{
			// defect 11406
			// prevent cacheIO from populating svCacheQueue
			sbVerifyingCache = true;
			// end defect 11406

			// Attempt to read the cache file for the current Trans AM date.  If 
			// an exception is returned, then it is assumed to be corrupted.
			cacheIO(READ, null, new RTSDate());
			lbValidCache = true;
		}
		catch (RTSException aeRTSEx)
		{
			// not logged here as the original exception has already been 
			// logged in cacheIO
			lbValidCache = false;
		}

		// defect 11406
		// reset boolean used to prevent cacheIO from populating svCacheQueue
		sbVerifyingCache = false;
		// end defect 11406

		return lbValidCache;
	}
}
