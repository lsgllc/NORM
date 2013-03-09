package com.txdot.isd.rts.server.inventory;

import java.util.Iterator;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.inventory.*;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.server.db.*;
import com.txdot.isd.rts.server.reports.ReportsServerBusiness;

/*
 * 
 * InventoryServerBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/02/2002	Fixed bug that "Inventory On Hold" always 
 *							apeared in DTA when 
 *							using allocated inventory
 * C Walker		05/08/2002	(CQU100003798) Added code to correctly check 
 *							for the qty and
 *							inv numbers matching in validateInvcItms().
 * MAbs			05/10/2002	Fixed DTA automatic allocation so it only 
 *							allocates to matching dealer id 
 *							CQU100003859
 * Ray, Kathy, 	05/17/2002	Fixed defect CQU100003935. Printing of 
 * Min						Specific Item in History.
 * MAbs			05/20/2002	DTA will not throw "Inventory on Hold" when 
 *							issuing employee allocated inventory CQU4030
 * Ray Rowehl	06/24/2002	Handle Constants differently so that 
 * 							constants also come into invalid letter 
 * 							lookup. CQU100004302
 * Ray Rowehl	08/02/2002	Issue and Delete should just use a qty of 
 * 							one when compute the seqno for the end number.
 *							CQU100004562
 * Min Wang		08/07/2002	Fix defect(CQU100004582). Set Inv Loc Id 
 * 							Cd to U when inventory has been issued and 
 * 							not in allocation table.
 * Min Wang		08/16/2002	Fix Defect(CQU100004610)  Corrected error in 
 * Ray Rowehl				invoice retreival introduced by defect 4302.
 * Min Wang		08/25/2002	Fixed defect(CQU100004668) Added reCalcGetInv(), 
 *							reCalcUpdtInv() and modified processData().
 * Min Wang		09/06/2002	Fixed defect CQU100004713 Modified 
 * 							updInvStatusCd()to allow issue of stock items.
 * Min Wang 	09/25/2002  Modified ProcessData() and added new method 
 *							chkForInvItmToIssue() for defect CQU100004734.
 * Min Wang		10/31/2002	Modified rcveInvc() for printing Inventory 
 *							Received report correctly. Defect CQU100005001.
 * Min Wang		11/15/2002	Modified findInvldLtrCombos() to calculate 
 *							InvEndNo correctly from the begin item number 
 *							and the quantity. Defect CQU100004918.
 * Min Wang		01/02/2003  Modified ChkForInvItmToIssue() so that row is 
 *							in update lock as soon as it is identified. 
 *							This will prevent other users from trying to 
 *							work with the same row. 
 * 							Make one server call. Defect(CQU100004755)
 * Min Wang		01/03/2002  modify issueInv() to put logical lock on 
 * 							Inventory before splitting. (CQU100005038)
 * Min Wang		01/03/2003  modify reCalc methods to check for overlapping 
 *							ranges.(CQU100005201)
 * Min Wang		01/03/2003	move returns to be inside try blocks.
 * Min Wang		01/17/2003	Added addInvHistData() and Modified rcvelInvc() 
 *							and delInvItm() to insert InvHstry Data when 
 *							processing Receives and Deletes. Defect 5111.
 * Ray Rowehl	02/03/2003	Fix Defect (CQU100004588)
 *							Pass ClientHost to MFAccess
 * Ray Rowehl	02/20/2003  Allow WsId List to be return by itself.
 * Min Wang     03/17/2003  Added new method getEntityMaxAlloctn() and 
 *							modified allocInvItms().(CQU100005518)
 * Min Wang		05/12/2003  Defect 6116. Modified updInvStatusCd() to do 
 *							beginTransaction when dba is null.
 * Min Wang 	05/12/2003  Defect 6078. Modified delForIssueInv(), 
 *							calcInvUnknown(), and updInvStatusCd() 
 *							to eliminate redundant validation of end no 
 *							which is always the same in issue inventory.
 * Min Wang		06/30/2003  Modified method allocInvItms() to verify the 
 *							type of Object. 
 *                          Throw execption and write log file if object 
 * 							is not right.
 *                          Defect 6031
 * Min Wang	    07/07/2003  Reflow Server Business to reduce the number 
 * 							of quiries required to issue an item.
 *                          Modified Modified addModfyDelInvcItm(), 
 *							allocInvItms(), chkForCompleteRngeAndSplt(), 
 *                          chkForInvItmInDb(), delForIssueInv(), 
 * 							delInvItm(),genInvInqRpt(), getInvRngeInDb(),
 * 							getNextInvItmNo(),reCalcCheckInv(), 
 * 							updInvStatusCd(),validateInvcItms()
 *							and processData() to update access to the 
 *							methods moved from 
 *							server.inventory.InventoryServerBusiness 
 *							into services.data.ValidateInventoryPattern. 
 *                          defect 6076
 * Ray Rowehl	07/31/2003	Change updInvStatusCd to force calculation 
 * 							if the end pattern seq no is 0.
 *							modify updInvStatusCd()
 *							defect 6426
 * Min Wang		12/03/2003	Remove call to do update in reCalcCheckInv.  
 *							Only report the problem back to caller.
 *							Modified reCalcCheckInv()
 *							defect 6618 Version 5.1.5  Fix2
 * Min Wang		12/15/2003	Pass parameter OfcIssuancenNo and ItmCd from 
 *							InvReCalc (optional).
 *							Modified reCalcGetInv()
 *							Defect 6745  Version 5.1.5  Fix2
 * Min Wang		01/09/2003	Close all opened db connections after 
 * 							complete a unit of work.
 *   						Modified reCalcGetInv().
 *                          Defect 6783 Version 5.1.5 Fix2
 * Min Wang 	06/21/2004  Fix inventory is not showing on the
 *							Hold/Release INV017 screen and is not
 *                          on the Inventory action Report when
 *							received inventory in to stock then
 *							put on hold.
 *							Modified updInvStatusCd()
 *							defect 6936 Ver 5.2.0
 * K Harrell	06/21/2004	Include check for received inventory
 *							with printableindi
 *							modify validateInvcItms()
 *							defect 7222 Ver 5.2.0
 * K Harrell	07/16/2004	In issueInventory, only process where
 *							not printable
 *							PCR 34 Ver 5.2.0
 * K Harrell	08/12/2004  if vector passed, use elementAt(1)
 *							modify chkForInvItmInDb()
 *							PCR 34 Ver 5.2.1
 * K Harrell	11/12/2004	set InvId = "0" for InvLocIdCd = "U"
 *							modify chkForInvItmInDb()
 *							defect 7536 Ver 5.2.2
 * K Harrell	02/03/2005	Acknowledge dba passed from Subcon Server
 *							modify chkForInvItmInDb()
 *							defect 7963 Ver 5.2.2 
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7890 Ver 5.2.3 
 * Min Wang		05/13/2005  Change to use new isCalcInv() instead of 
 * 							old getCalcInv().
 * 							modify delForIssueInv(), getInvRngeInDb(),
 * 							updInvStatusCd()
 * 							defect 6370 Ver 5.2.3
 * K Harrell	05/19/2005	InventoryHistoryUIData element renaming
 * 							defect 7899 Ver 5.2.3
 * Min Wang		06/24/2005	Allow ReCalc to do updates when a UserName 
 * 							RequestId are passed in.  Also add a write
 * 							to Admin Log when a ReCalc update is done.
 * 							modify reCalcCheckInv(), reCalcUpdtInv()
 * 							defect 8244 Ver 5.2.2 Fix 5
 * Ray Rowehl	07/06/2005	Cleanup of method javadocs.
 * 							Make better use of Constants.  Especially,
 * 							use ReportConstants.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/09/2005	Further cleanup of constants.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/12/2005	Further cleanup of constants
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/01/2005	Further cleanup of Invoice handling.
 * 							defect 7890 Ver 5.2.3
 * Min Wang     03/31/2006	Setting right constants for InvLocIdCd.
 * 							modify rcveInvc()
 * 							defect 8656 Ver 5.2.3
 * Ray Rowehl	04/11/2006	Connect DBA null in chkForInvItmToIssue
 * 							modify chkForInvItmToIssue()
 * 							defect 8696 Ver 5.2.3
 * Ray Rowehl	07/12/2006	Do a retry on StaleConnection if just 
 * 							selecting data for Substa.
 * 							modify getSubstaDispData()
 * 							defect 8849 Ver 5.2.3
 * Ray Rowehl	08/04/2006	Write the StaleConnection to the log.
 * 							modify getSubstaDispData()
 * 							defect 8869 Ver 5.2.4
 * Min Wang		02/09/2007	add new method for getting next Virtual 
 * 							Inventory Item Number.
 * 							add getNextViItmNo()
 * 							modify processData()
 * 							defect 9117 Ver Special Plates  
 * Ray Rowehl	02/09/2007	Setup stub handling of Personalized Plate.
 * 							add validatePerPlate()
 * 							modify processData()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	02/12/2007	Clean up handling of ProcessInventory due to 
 * 							TransWsId being numeric now.
 * 							modify chkNextAvailIndi(), getInvRngeInDb(),
 * 								updInvStatusCd()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	02/17/2007	Improve validation of personalized plates.
 * 							add lookupVirtualForItem(),
 * 								validatePerPlate(), 
 * 								validatePerPlateChkInvalidLtrCombo(),
 * 								validatePerPlateChkPatterns(),
 * 								validatePerPlateChkMainFrame(),
 * 								validatePerPlateInsertViRow()
 * 							defect 9116 Ver Special Plates 
 * Min Wang		02/17/2007	add new methods for getting next 
 * 							Virtual Inventory Item Number.
 * 							add chkForVICompleteRngeAndSplt()
 * 								chkVirtualInvForSpecificItem()
 * 								chkVirtualInvItmRange(), updVIStatusCd()
 * 							modify processData()
 * 							defect 9117 Ver Special Plates
 * Min Wang		02/19/2007	add new method for updating the original row
 * 							and inserting one row into Inventory Virtual
 * 							table.
 * 							add performUpdateInsertForVIAlloctn()
 * 							defect 9117 Ver Special Plates
 * Ray Rowehl	02/22/2007	Add insert of SP Rejection Log.
 * 							add insrtSpRejLog()
 * 							defect 9116 Ver Special Plates
 * J Rue		02/22/2007	Rename getSpclPltRegis() to 
 * 							getSpclPltRegisData()
 * 							modify validatePerPlateChkMainFrame()
 * 							defect 9086 Ver Special Plates
 * Min Wang		02/27/2007  Add virtual inventory for Inquiry report.
 * 							modify genInvInqRpt()
 * 							defect 9117 Ver Special Plates
 * Ray Rowehl	03/09/2007	Add a process to allow IVTRS to validate
 * 							a plp without holding it.
 * 							Modify receive logic to populate transempid.
 * 							add validatePerPlateNoHold()
 * 							modify rcveInvc()
 * 							defect 9116 Ver Special Plates
 * Min Wang		03/09/2007	Modify delete inv item populate transempid.
 * 							modify delInvItm()
 * 							defect 9117 Ver Special Plates
 * Min Wang		03/20/2007	add virtual inventory rejection report.
 * 							add genVIRejectionRpt()
 * 							defect 9117 Ver Special Plates
 * Min Wang		04/04/2007	add Exception msg for the virtual inventory 
 * 							report.
 * 							add TXT_NO_RECORD
 * 							modify genInvInqRpt()
 * 							defect 9117 Ver Special Plates
 * Ray Rowehl	04/10/2007	Catch NullPointer in issueInv and several 
 * 							other places and throw ServerDown.
 * 							modify chkForInvItmInDb(), 
 * 								chkForInvItmToIssue(), issueInv(),
 * 								updInvStatusCd()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	04/19/2007	If issueInv sees an object where the 
 * 							InvLocIdCd is unknown and the itm is not 
 * 							found on the database, go ahead and return
 * 							it as unknown instead of forcing the user
 * 							to type it in on INV001.
 * 							Allow IVTRS to bypass reserve of a PLP if 
 * 							they do not have an ItmCd.
 * 							modify issueInv(), updVIStatusCd(),
 * 								validatePerPlate()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	04/26/2007	Need to set patrnseqno to 0 on VI Status
 * 							Updates so sql will find the record.	
 * 							modify updViStatusCodeComplete(), 
 * 								updViStatusCodeRelease()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/03/2007	Throw a VI Unavilable error if MF is down
 * 							when validating PLPs.
 * 							modify validatePerPlateChkMainFrame()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/08/2007	Modify get next VI for when there are no 
 * 							ISAs defined for a pattern.
 * 							DV plates may be an example of this.
 * 							modify getNextIVItmNo()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/15/2007	Use the generic PLP Validation Rejection 
 * 							message to respond to the user.  Specific 
 * 							reason is still logged for reporting.
 * 							Now throw numbered exceptions back from vi 
 * 							insert.
 * 							modify insrtViRow(), validatePer*
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/17/2007	If the VI item is not calculated, do it 
 * 							before inserting or deleting.
 * 							modify delInvVirtual(), insrtViRow()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/22/2007	Provide a method that attempts to put an
 * 							item on hold.  If that item is not found,
 * 							then attempt to insert it.
 * 							add updVIStatusCdRecover()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/25/2007	Set and handle the CustSuppliedIndi as 
 * 							needed.  ISB will handle setting it to true
 * 							where expected.  Requestor can still set to
 * 							true and have it pass through, but can not
 * 							override ISB where we set it.
 * 							Allow Void to release a completed item.
 * 							Do not check ROP and OLDPLT* against invalid 
 * 							letters and existing patterns on PLPValidate.
 * 							modify updVIStatusCdRecover(),
 * 								updViStatusCodeRelease(), 
 * 								validatePerPlate()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/31/2007	Remove todo for handling customer supplied.
 * 							modify validatePerPlate()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/31/2007	Allow the Recover function handle ViItmCd 
 * 							lookup failures by using the ItmCd.
 * 							modify updVIStatusCdRecover()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/01/2007	Allow ROP's and OLDPLT's to bypass any
 * 							PLP validation.  Still allows SP to make the
 * 							call completely dumb to this business 
 * 							requirement.
 * 							modify validatePerPlate()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/07/2007	Only set the year to 0 when using actual
 * 							Virtual Item Codes.  When using regular 
 * 							Item Codes, the year must be respected for
 * 							calculations to work.
 * 							Pass DBA on the updates.
 * 							modify updVIStatusCd(),
 * 								updViStatusCodeComplete(),
 * 								updViStatusCodeConfirm(), 
 * 								updVIStatusCdRecover(),
 * 								updViStatusCodeRelease()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/08/2007	Block the System Hold of a record where
 * 							the TransTime is set.
 * 							modify updVIStatusCd(),
 * 								validatePerPlateQueryExisting()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/11/2007	Modify the VI Rej report to log calls.
 * 							modify genVIRejectionRpt()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/12/2007	Do not rollback when updating vi status code
 * 							if exception is 182.
 * 							modify updVIStatusCd(),
 * 								updViStatusCodeComplete(),
 * 								updViStatusCodeConfirm(), 
 * 								updVIStatusCdRecover(),
 * 								updViStatusCodeRelease()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/15/2007	Improve ViItem Lookup to more properly 
 * 							handle items.  There was a problem if the 
 * 							ViItmCd matches the ItmCd on patterns.
 * 							Modify Confirm Hold process to just update 
 * 							the record.  Do not attempt to put it on 
 * 							hold again.
 * 							Add new method to check to see if Duplicates
 * 							are allowed.  If Duplicates are allowed,
 * 							some validations are not required.  
 * 							Duplicates also exempt not found exceptions.
 * 							add chkDupsAllowed()
 * 							modify lookupVirtualForItem(),
 * 								updViStatusCodeConfirm(),
 * 								validatePerPlate()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/20/2007	Now throw exceptions back to CSB.  Sending
 * 							back Null on exception was worse than just
 * 							sending the original exception!
 * 							modify updVIStatusCd(),
 * 								updViStatusCodeComplete(),
 * 								updViStatusCodeConfirm(), 
 * 								updVIStatusCdRecover(),
 * 								updViStatusCodeRelease()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/22/2007	Include the ip address on the confirmation
 * 							of still having a hold.
 * 							Add new handling for HoopRegPltNo for 
 * 							VI PLP handling.
 * 							Remove TransAmDate for confirm hold so 
 * 							IVTRS can pass initial testing.
 * 							Need to review adding back to do
 * 							modify updViStatusCodeConfirm(),
 * 								validatePerPlate()
 * 								validatePerPlateQueryExisting()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/23/2007	Insert should not commit if DBA was passed.
 * 							modify insrtViRow()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/25/2007	Intersection check should use Hoops for User
 * 							Defined plates.
 * 							Match CSB and ISB error handling on VI 
 * 							exceptions.  Coordinate when to rollback.
 * 							Log delete of complete plp rows to activity 
 * 							log for support follow up. 
 * 							modify insrtViRow(), updViStatusCodeComplete(),
 * 								updViStatusCodeRelease(), updViTransTime()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/26/2007	Review handling of VI Locks for IVTRS.
 * 							They will not use TransAmDate.  They do not 
 * 							store on their side.
 * 							modify updVIStatusCd()
 * 							defect 9116 Ver Special Plates
 * K Harrell	07/08/2007	Make alternate call for VI Inventory Inquiry
 * 							modify getInvItmsForInq()
 * 							defect 9085 Ver Special Plates 
 * Ray Rowehl	07/09/2007	If the ItmCd matches the ViItmCd this means
 * 							that we do not have a "real" VI Item.
 * 							In this case, do not set the year back to 
 * 							0 for the lookup.
 * 							modify lookupVirtualForItem()
 * 							defect 9116 Ver Special Plates
 * K Harrell	07/12/2007	Fix null pointer exception from 7/8 fix
 * 							Remove Sort of Inventory Vectors upon 
 * 							  return of SQL
 * 							modify getInvItmsForInq(),genInvInqRpt()
 *							defect 9085 Ver Special Plates
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/12/2007	The check to see if the item code matches
 * 							the VI item code should be determined by the
 * 							pattern being checked at the moment.
 * 							modify lookupVirtualForItem()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/18/2007	Make purge handling for VI more clear.  
 * 							There are two components.  One purges 
 * 							VI that has been marked as complete on 
 * 							the transaction date.  The second component
 * 							releases any holds that have been abandoned.
 * 							modify purgeInventoryVirtual(),
 * 								purgeInventoryVirtualReleaseHolds()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/19/2007	VI Get Next needs to be able to switch to 
 * 							the following pattern if the current pattern
 * 							is exhausted.  It was not.
 * 							modify getNextIVItmNo()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/20/2007	Write to Admin_Log when we get a 1002 error
 * 							on GetNextVI function.
 * 							modify getNextIVItmNo()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/23/2007	Further restrict re-use of a personalized
 * 							plate to have to be from same empid, wsid, 
 * 							and amdate in addition to system hold and
 * 							zero transtime.
 * 							Move the create of DBA in getNextIVItmNo
 * 							so we error out of db down before using
 * 							cache. 
 * 							Rollback from selection in getNextIVItmNo
 * 							if the selected plate number does not 
 * 							calculate for the specified item code.
 * 							This will be hard to test in normal 
 * 							circumstances.  VI is matched to patterns.
 * 							modify getNextIVItmNo(),
 * 								validatePerPlateQueryExisting()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	08/03/2007	Modify the PlateType check to allow IVTRS no
 * 							reserve requests to go through even though
 * 							they are not passing an ItmCd.
 * 							modify validatePerPlate()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	08/14/2007	Add some additional error logging when the 
 * 							trace level is setup to 20.
 * 							Also clean up some method logging.
 * 							modify chkVirtualInvItmRange(), 
 * 								getNextIVItmNo(),
 * 								updVIStatusCd()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	09/07/2007	Modify ReCalc routines to check Virtual 
 * 							Inventory as well.
 * 							Virtual will not be updated at this time..
 * 							add reCalcCheckViRow()
 * 							modify processData(),
 * 								reCalcCheckInv(),
 * 								reCalcGetInv()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	09/19/2007	Need to do a rollback on exception if the 
 * 							passed DBA is null.
 * 							modify updVIStatusCd(), updVIStatusCdRecover(),
 * 								updViStatusCodeConfirm(), 
 * 								updViStatusCodeRelease(),
 * 								updViTransTime()
 * 							defect 9323 Ver Special Plates
 * Ray Rowehl	10/01/2007	Modify release when voiding to allow Void to 
 * 							work.  Void is not always done with the same 
 * 							TransEmpId.  Release requires this normally.
 * 							modify updViStatusCodeRelease()
 * 							defect 9341 Ver Special Plates
 * K Harrell	10/30/2007	Add logging to track VI access.  Move call 
 * 	Ray Rowehl				 to Mainframe for Personalized Plate check
 * 							 outside Unit of Work.
 * 							add logUOW() 
 * 							modify validatePerPlate(),delInvVirtual(), 
 *							 getNextIVItmNo(),insrtViRow(),updVIStatusCd(),
 *							 updViStatusCodeConfirm(),
 *							 updViStatusCodeRelease(),updViTransTime(),
 *							 validatePerPlate(),validatePerPlateChkMainFame()
 * 							defect 9404 Ver Special Plates
 * K Harrell	11/06/2007	remove logging from 10/30/2007
 * 							deprecate logUOW() 
 * 							modify validatePerPlate(),delInvVirtual(), 
 *							 getNextIVItmNo(),insrtViRow(),updVIStatusCd(),
 *							 updViStatusCodeConfirm(),
 *							 updViStatusCodeRelease(),updViTransTime(),
 *							 validatePerPlate(),validatePerPlateChkMainFame()
 * 							defect 9404 Ver Special Plates 
 * Ray Rowehl	11/08/2007	Modify the purge routines to use the DBA 
 * 							passed in.  It needs to be setup in Purge.
 * 							modify purgeInventoryVirtual(),
 * 								purgeInventoryVirtualReleaseHolds()
 * 							defect 9431 Ver Special Plates
 * Ray Rowehl	12/04/2007	Enable update of VI on ReCalc.
 * 							modify reCalcCheckInv(),
 * 								reCalcUpdtInv()
 * 							defect ? Ver Special Plates
 * K Harrell	01/16/2008	Does not insert into SpclPltRejLog if 
 * 							duplicate of MF Record.
 * 							modify validatePerPlate()
 * 							defect 9486 Ver 3 Amigos Prep 
 * Min Wang		01/22/2008	add pattern detection for Hoops version of 
 * 							plate number.
 * 							modify validatePerPlateChkPatterns()
 * 							defect 9516 Ver 3_Amigos Prep
 * Min Wang		01/28/2008	Modify Hoops to use a new interfacing method 
 * 							in ValidateInventoryPattern.
 * 							modify validatePerPlateChkPatterns()
 * 							defect 9516 Ver 3_Amigos Prep
 * Min Wang		02/13/2008  If InvItmNo is null, do not update to lock 
 * 							the range.
 * 							modify issueInv()
 * 							defect 9558 Ver Ver 3_Amigos Prep
 * Ray Rowehl	04/22/2008	We want to keep pattern plates on hold 
 * 							longer for IVTRS.
 * 							modify purgeInventoryVirtualReleaseHolds()
 * 							defect 9606 Ver FRVP
 * Ray Rowehl	06/11/2008	Use TempChange flag to prevent insert into 
 * 							VI on validate PLP when just checking
 * 							availability.  Also block the delete 
 * 							of a row if just temporary.
 * 							modify validatePerPlateInsertViRow(),
 * 								validatePerPlateNoHold(),
 * 								validatePerPlateQueryExisting()
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	06/20/2008	Do not reject a Reserved plate if we 
 * 							are attempting to get a Reserved plate.
 * 							modify validatePerPlateChkMainFrame()
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	06/25/2008	Add office match up check to reserved 
 * 							plate checking.
 * 							modify validatePerPlateChkMainFrame()
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	07/03/2008	If it is a temp change and expired, 
 * 							allow query to work ok.
 * 							modify validatePerPlateQueryExisting()
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	07/11/2008	Guard against null itmcds passed in from 
 * 							Vendor Plates web service.  Can not trust 
 * 							the caller's ability to edit data.
 * 							modify lookupVirtualForItem()
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	07/15/2008	Found that we are not blocking release of an 
 * 							item when transtime is not 0.  Non-0 indicates
 * 							that the item is paid for!!
 * 							modify updViStatusCodeRelease()
 * 							defect 9679 Ver MyPlates_POS
 * Ray Rowehl	07/16/2008	Confirm hold clears the the TransTime.  Do
 * 							not allow confirm when transtime is set.
 * 							Just return the record requested.
 * 							modify updViStatusCodeConfirm()
 * 							defect 9679 Ver MyPlates_POS
 * Min Wang		07/29/2008	Ensure the right data type before attempting 
 * 							the operation.
 * 							modify validatePerPlateNoHold()
 * 							defect 9780 Ver MyPlates_POS
 * Min Wang		08/01/2008	Allow POS to continue to set the TransTime
 * 							column even though money is not collected.
 * 							Re-word the comments before the check to
 * 							help make the decision tree more clear.
 * 							add INV_MAX_TRANSWSID
 * 							modify updViStatusCodeRelease()
 * 							defect 9782 Ver MyPlates_POS
 * K Harrell	08/29/2008	Insert into RTS_ADMIN_LOG for Inventory
 * 							  Receive/Delete History Reports (Region)
 * 							modify genDelInvHistoryRpt(),
 * 							  genRcveInvHistoryRpt()
 * 							defect 8940 Ver Defect_POS_B
 * K Harrell	09/04/2008	RTS_ADMIN_LOG changes for PLP_Rejection Rpt
 * 							Use mixed case for Action, use MM/DD/YY for 
 * 							Entity AbstractValue vs. AMDate.
 * 							Mixed case for "Delete" - use 
 * 							  CommonConstant.TXT_ADMIN_LOG_DELETE  
 * 							modify genVIRejectionRpt(), 
 * 							  updViStatusCodeRelease() 
 * 							defect 8595 Ver Defect_POS_B
 * K Harrell	01/28/2009	Throw 1004 when !Internet Request when  
 * 							requested plate no  for personalized plate
 * 							matches existing pattern.
 * 							modify validatePerPlateChkPatterns() 
 * 							defect 9578 Ver Defect_POS_D
 * K Harrell	06/26/2009	Implement DealerData
 * 							modify getDlrs()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	08/22/2009	Standardize Report methods
 * 							add initReportProperties() 
 * 							modify genInvActionRpt(), genInvInqRpt(), 
 * 							genInvRcveRpt(), genRcveInvHistoryRpt(), 
 * 							genVIRejectionRpt()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	10/12/2009	Create one report for Inventory History 
 * 							modify genDelInvHistoryRpt(), 
 * 							 genRcveInvHistoryRpt()
 * 							defect 10207 Ver Defect_POS_G
 * Ray Rowehl	10/19/2009	Modify the recover insert to also set the 
 * 							status code to system hold.
 * 							modify updVIStatusCdRecover()
 * 							defect 10253 Ver Defect_POS_G 
 * Ray Rowehl	10/21/2009	Should use 593 instead of 1009 when 
 * 							trying to release an item on hold for 
 * 							someone else.
 * 							modify updViStatusCodeRelease()
 * 							defect 10253 Ver Defect_POS_G 
 * B Hargrove	10/27/2009	Inventory Inquiry report can be run for a 
 * 							substation. Add SubstaId to search data. 
 * 							modify genInvInqRpt()
 * 							defect 10260 Ver Defect_POS_G
 * K Harrell	12/16/2009	DTA Cleanup 
 * 							modify issueInv()
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	12/16/2009	Null pointer when dups allowed.	Check for 
 * 							 null DBA prior to End Transaction. 
 * 							modify validatePerPlate()  
 * 							defect 10293 Ver Defect_POS_H  
 * Min Wang 	11/24/2009	Merge Issue. add missed code.
 * 							defect 9116 Ver Defect_POS_H
 * K Harrell	02/18/2010	Implement new SubcontractorData
 * 							modify getSubcons()
 * 							defect 10161 Ver POS_640
 * Ray Rowehl	03/09/2010	Modify getNextVIItmNo to handle a passed 
 * 							DBA.  MyPlates will be calling. 
 * 							modify getNextIVItmNo()
 * 							defect 10400 Ver 6.4.0
 * Min Wang		04/08/2010	Should be returning a non-zero error when
 * 							MyPlates checks availability of a plate 
 * 							that is on "reserve".
 * 							modify validatePerPlateChkMainFrame()
 * 							defect 10415  Ver 6.4.0
 * Min Wang		04/14/2010	Clean up code review comments.
 * 							Move check for MFDown to after check for null.
 * 							Replace magic number with 
 * 								CommonConstant.SEARCH_SPECIAL_PLATES.
 * 							Replace ApplicationControlConstants
 *							.SA_MFGSTATUS_DO_NOT with 
 *							SpecialPlatesConstant.UNACCEPTABLE_MFGSTATUSCD.
 *							Replace ApplicationControlConstants
 *							.SA_MFGSTATUS_RESERVED with 
 * 							modify validatePerPlateChkMainFrame()
 * 							defect 10415  Ver 6.4.0
 * Min Wang		04/19/2010	Re-flowed handling of null VehicleInquiryData.
 * Ray Rowehl				It appears that an original design choice
 * 							was not fully correct.  The null should result
 * 							in a VI unavialable message.
 * 							modify validatePerPlateChkMainFrame()
 * 							defect 10415  Ver 6.4.0
 * Ray Rowehl	04/20/2010	Only start a DBA for adminLog write if the 
 * 							DBA was not passed in.
 * 							modify getNextIVItmNo()
 * 							defect 10400  Ver 6.4.0
 * Min Wang		06/18/2010  Display msg "INVOICE NOT FOUND" when an 
 * 							invoice is not found on the mainframe 
 * 							invoice file.
 * 							modify getInvFromMF()
 * 							defect 8265  Ver 6.5.0
 * Min Wang		06/22/2010	Back out the change
 * 							defect 8265  Ver 6.5.0
 * K Harrell	03/17/2011	modify to validate InvLocIdCd, InvId on 
 * 							delete Inventory 
 * 							modify delForIssueInv()
 * 							defect 10769 Ver 6.7.1 
 * Min Wang		01/12/2012 	updInvStatusCd does not rollback when the dba was 
 * 							not passed in and there was an error.
 * 							modify updInvStatusCd()
 * 							defect 11180 Ver 6.10.0
 * Min Wang   	01/20/2012 	modify insrtViRow()
 * 							defect 11180 Ver 6.10.0
 * Min Wang    01/27/2012	modify updInvStatusCd()
 * 							defect 11180 Ver 6.10.0
 * Min Wang    01/30/2012	modify updInvStatusCd()
 * 							defect 11180 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/** 
 * This class handles Inventory server business layer.
 *
 * @version	6.10.0 			01/30/2012
 * @author	Charlie Walker
 * @author	Ray Rowehl
 * @author	Min Wang
 * <br>Creation Date:		09/06/2001 16:50:10 
 */

public class InventoryServerBusiness
{
	// string constants
	private static final int INV_MAX_TRANSWSID = 900;
	private static final String CENTRAL_LOCID = "0";
	private static final String INV_DEFAULT_EMPLOYEE = "DEFAULT";
	// Method begin and end messages
	private static final String MSG_ADDINVHISTDATA_BEGIN =
		"addInvHistData - Begin ";
	private static final String MSG_ADDINVHISTDATA_END =
		"addInvHistData - End ";
	private static final String MSG_ADDINVPROFILE_BEGIN =
		"addInvProfile - Begin ";
	private static final String MSG_ADDINVPROFILE_END =
		"addInvProfile - End ";
	private static final String MSG_ADDMODFYDELINVCITM_BEGIN =
		"addModfyDelInvcItm - Begin ";
	private static final String MSG_ADDMODFYDELINVCITM_END =
		"addModfyDelInvcItm - End ";
	private static final String MSG_ALLOCINVITMS_BEGIN =
		"allocInvItms - Begin ";
	private static final String MSG_ALLOCINVITMS_END =
		"allocInvItms - End ";
	private static final String MSG_CHKFORCOMPLETERNGEANDSPLT_END =
		"chkForCompleteRngeAndSplt - End ";
	private static final String MSG_CHKFORINVITMINDB_BEGIN =
		"chkForInvItmInDb - Begin ";
	private static final String MSG_CHKFORINVITMINDB_END =
		"chkForInvItmInDb - End ";
	private static final String MSG_CHKFORINVITMTOISSUE_BEGIN =
		"chkForInvItmToIssue - Begin ";
	private static final String MSG_CHKFORINVITMTOISSUE_END =
		"chkForInvItmToIssue - End ";
	private static final String MSG_CHKFORPLP_BEGIN =
		"chkForPLP - Begin ";
	private static final String MSG_CHKFORPLP_END = "chkForPLP - End ";
	private static final String MSG_CHKFROMCOMPLETERNGEANDSPLT_BEGIN =
		"chkForCompleteRngeAndSplt - Begin ";
	private static final String MSG_CHKINVITMRANGE_BEGIN =
		"chkInvItmRange - Begin ";
	private static final String MSG_CHKINVITMRANGE_END =
		"chkInvItmRange - End ";
	private static final String MSG_CHKNEXTAVAILINDI_BEGIN =
		"chkNextAvailIndi - Begin ";
	private static final String MSG_CHKNEXTAVAILINDI_END =
		"chkNextAvailIndi - End ";
	private static final String MSG_DELFORISSUEINV_BEGIN =
		"delForIssueInv - Begin ";
	private static final String MSG_DELFORISSUEINV_END =
		"delForIssueInv - End ";
	private static final String MSG_DELINVITM_BEGIN =
		"delInvItm - Begin ";
	private static final String MSG_DELINVITM_END = "delInvItm - End ";
	private static final String MSG_DELINVPROFILE_BEGIN =
		"delInvProfile - Begin ";
	private static final String MSG_DELINVPROFILE_END =
		"delInvProfile - End ";
	private static final String MSG_GENDELINVHISTORYRPT_BEGIN =
		"genDelInvHistoryRpt - Begin ";
	private static final String MSG_GENDELINVHISTORYRPT_END =
		"genDelInvHistoryRpt - End ";
	private static final String MSG_GENINVACTIONRPT_BEGIN =
		"genInvActionRpt - Begin ";
	private static final String MSG_GENINVACTIONRPT_END =
		"genInvActionRpt - End ";
	private static final String MSG_GENINVINQRPT_BEGIN =
		"genInvInqRpt - Begin";
	private static final String MSG_GENINVINQRPT_END =
		"genInvInqRpt - End ";
	private static final String MSG_GENINVRCVERPT_BEGIN =
		"genInvRcveRpt - Begin ";
	private static final String MSG_GENINVRCVERPT_END =
		"genInvRcveRpt - End ";
	private static final String MSG_GENRCVEINVHISTORYRPT_BEGIN =
		"genRcveInvHistoryRpt - Begin ";
	private static final String MSG_GENRCVEINVHISTORYRPT_END =
		"genRcveInvHistoryRpt - End ";
	private static final String MSG_GETALLOCTNDISPDATA_BEGIN =
		"getAlloctnDispData - Begin ";
	private static final String MSG_GETALLOCTNDISPDATA_END =
		"getAlloctnDispData - End ";
	private static final String MSG_GETDLRS_BEGIN = "getDlrs - Begin ";
	private static final String MSG_GETDLRS_END = "getDlrs - End ";
	private static final String MSG_GETEMPS_BEGIN = "getEmps - Begin ";
	private static final String MSG_GETEMPS_END = "getEmps - End ";
	private static final String MSG_GETENTITSMAXALLOCTN_BEGIN =
		"getEntitysMaxAlloctn - Begin ";
	private static final String MSG_GETENTITYSMAXALLOCTN_END =
		"getEntitysMaxAlloctn - End ";
	private static final String MSG_GETINQENTITYDATA_BEGIN =
		"getInqEntityData - Begin ";
	private static final String MSG_GETINQENTITYDATA_END =
		"getInqEntityData - End ";
	private static final String MSG_GETINVALLOCDATA_BEGIN =
		"getInvAllocData - Begin ";
	private static final String MSG_GETINVALLOCDATA_END =
		"getInvAllocData - End ";
	private static final String MSG_GETINVFROMMF_BEGIN =
		"getInvFromMF - Begin ";
	private static final String MSG_GETINVFROMMF_END =
		"getInvFromMF - End ";
	private static final String MSG_GETINVFUNCTRANSANDTRINVDETAIL_BEGIN =
		"getInvFuncTransAndTrInvDetail - Begin ";
	private static final String MSG_GETINVFUNCTRANSANDTRINVDETAIL_END =
		"getInvFuncTransAndTrInvDetail - End ";
	private static final String MSG_GETINVITMS_BEGIN =
		"getInvItms - Begin ";
	private static final String MSG_GETINVITMS_END =
		"getInvItms - End ";
	private static final String MSG_GETINVITMSFORINQ_BEGIN =
		"getInvItmsForInq - Begin ";
	private static final String MSG_GETINVITMSFORINQ_END =
		"getInvItmsForInq - End ";
	private static final String MSG_GETINVPROFILE_BEGIN =
		"getInvProfile - Begin ";
	private static final String MSG_GETINVPROFILE_END =
		"getInvProfile - End ";
	private static final String MSG_GETINVPROFILEDISPDATA_BEGIN =
		"getInvProfileDispData - Begin ";
	private static final String MSG_GETINVPROFILEDISPDATA_END =
		"getInvProfileDispData - End ";
	private static final String MSG_GETINVRNGEINDB_BEGIN =
		"getInvRngeInDb - Begin ";
	private static final String MSG_GETINVRNGEINDB_END =
		"getInvRngeInDb - End ";
	private static final String MSG_GETNEXTINVITMNO_BEGIN =
		"getNextInvItmNo - Begin ";
	private static final String MSG_GETNEXTINVITMNO_END =
		"getNextInvItmNo - End ";
	private static final String MSG_GETREGIONALCOUNTIES_BEGIN =
		"getRegionalCounties - Begin ";
	private static final String MSG_GETREGIONALCOUNTIES_END =
		"getRegionalCounties - End ";
	private static final String MSG_GETSUBCONS_BEGIN =
		"getSubcons - Begin ";
	private static final String MSG_GETSUBCONS_END =
		"getSubcons - End ";
	private static final String MSG_GETSUBSTA_BEGIN =
		"getSubsta - Begin ";
	private static final String MSG_GETSUBSTA_END = "getSubsta - End ";
	private static final String MSG_GETSUBSTADISPDATA_BEGIN =
		"getSubstaDispData - Begin ";
	private static final String MSG_GETSUBSTADISPDATA_END =
		"getSubstaDispData - End ";
	private static final String MSG_GETWSIDS_BEGIN =
		"getWsIds - Begin ";
	private static final String MSG_GETWSIDS_END = "getWsIds - End ";
	private static final String MSG_ISSUEINV_BEGIN =
		"issueInv - Begin ";
	private static final String MSG_ISSUEINV_END = "issueInv - End ";
	private static final String MSG_NO_TRINVDETAIL =
		"No TrInvDetail data";

	// embedded message text
	private static final String MSG_NOINVFUNCTRANS =
		"No InventoryFuncTrans data";
	private static final String MSG_PERFORMRECACLCHECK_BEGIN =
		"performRecalcCheck - Begin ";
	private static final String MSG_PERFORMRECALCCHECK_END =
		"performRecalcCheck - End ";
	private static final String MSG_PERFORMRECALCUPDATE_END =
		"performRecalcUpdate - End ";
	private static final String MSG_PERFORMUPDATEINSERTFORALLOCTN_BEGIN =
		"performUpdateInsertForAlloctn - Begin ";
	private static final String MSG_PERFORMUPDATEINSERTFORALLOCTN_END =
		"performUpdateInsertForAlloctn - End ";
	private static final String MSG_PREFORMRECALCUPDATE_BEGIN =
		"performRecalcUpdate - Begin ";
	private static final String MSG_RCVEINVC_BEGIN =
		"rcveInvc - Begin ";
	private static final String MSG_RCVEINVC_END = "rcveInvc - End ";
	private static final String MSG_RECALCGETINV_BEGIN =
		"recalcGetInv - Begin ";
	private static final String MSG_RECALCGETINV_END =
		"recalcGetInv - End ";
	private static final String MSG_UPDINVPROFILE_BEGIN =
		"updInvProfile - Begin ";
	private static final String MSG_UPDINVPROFILE_END =
		"updInvProfile - End ";
	private static final String MSG_UPDINVSTATUSCD_BEGIN =
		"updInvStatusCd - Begin ";
	private static final String MSG_UPDINVSTATUSCD_END =
		"updInvStatusCd - End ";
	private static final String MSG_VALIDATEINVCITMS_BEGIN =
		"validateInvcItms - Begin ";
	private static final String MSG_VALIDATEINVCITMS_END =
		"validateInvcItms - End ";

	// Reports stuff	
	private static final String RPT_3021_FILENAME = "INVRCV";
	private static final String RPT_3031_INVINQ_FILENAME = "INVINQ";
	private static final String RPT_3072_FILENAME = "INRCVR";

	private static final int SLEEP_1_SECOND = 1000;

	private static final String TRANS_DTA_PREFIX = "DTA";
	private static final String TXT_DASH_WITH_SPACES = " - ";
	private static final String TXT_ERROR = "ERROR";
	private static final String TXT_ERROR_COLON = "ERROR: ";
	private static final String TXT_NO_IAUID_OBJECT =
		" No InventoryAllocationUIData Object";
	// defect 9117
	private static final String TXT_NO_RECORD =
		"No records were found on the database.  ";
	private static final String TXT_NO_TRANSHDR =
		" No TransactionHeaderData Object";
	private static final String TXT_OBJECT_NOT_RIGHT =
		" Object is not right";
	private static final String TXT_PAREN_CLOSE = ")";
	private static final String TXT_PAREN_OPEN = "  (";
	private static final String TXT_PAREN_OPEN_2 = " (";
	private static final String TXT_PRINT_EXCEPTION_RPT_QUESTION =
		"No records were found on the database.  "
			+ "Do you want to print the Exception Report?";
	// end defect 9117
	private static final String TXT_RECALC_ACTION = "RECALC";
	private static final String TXT_WRONG_OBJECT = " Wrong Object ";

	/**
	 * Main method.  Used for stand alone testing.
	 * 
	 * @param aarrArgs java.lang.String[]
	 * @throws  RTSException  
	 */
	public static void main(String[] aarrArgs) throws RTSException
	{

		//**
		// * Use for testing computing inv
		// */
		//		/*
		//			InventoryAllocationUIData lInvAlloc = 
		//			new InventoryAllocationUIData();
		//			InventoryAllocationUIData lInvAllocReturn = 
		//			new InventoryAllocationUIData();
		//		
		//			lInvAlloc.setOfcIssuanceNo(170);
		//			lInvAlloc.setSubstaId(0);
		//			lInvAlloc.setItmCd("WS");
		//			lInvAlloc.setInvItmYr(2002);
		//			lInvAlloc.setInvItmNo("105wc");
		//		
		//			lInvAlloc.setInvId("RTS0000");
		//			lInvAlloc.setInvLocIdCd("E");
		//			lInvAlloc.setInvItmEndNo("106WC");
		//			lInvAlloc.setInvQty(2);
		//			lInvAlloc.setPatrnSeqNo(105);
		//			lInvAlloc.setInvStatusCd(0);
		//			lInvAlloc.setPatrnSeqCd(0);
		//		
		//			try
		//			{
		//				CacheManager.loadCache();
		//		
		//				InventoryServerBusiness server = new InventoryServerBusiness();
		//		
		//		//		ComputeInventoryData lCID = server.validateItmNoInput(lInvAlloc);
		//		//		ComputeInventoryData lCID2 = (ComputeInventoryData) UtilityMethods.copy(lCID);
		//		//		lInvAllocReturn = (InventoryAllocationUIData) server.calcInvNo(lCID);
		//		//		lCID2.setInvAlloctnUIData(lInvAllocReturn);
		//		//		lInvAllocReturn = (InventoryAllocationUIData) server.calcInvEndNo(lCID2);
		//		
		//				lInvAllocReturn = (InventoryAllocationUIData) server.sqlSetUp(lInvAlloc);
		//				
		//		//		lInvAllocReturn = (InventoryAllocationUIData) server.allocInvItms(lInvAlloc);
		//				
		//		//		boolean b = server.chkIfItmPLPOrOLDPLTOrROP(lInvAlloc.getItmCd());
		//				
		//		//		lInvAllocReturn = (InventoryAllocationUIData) server.getNextInvItmNo(lInvAlloc);
		//		
		//		//		lInvAllocReturn = (InventoryAllocationUIData) server.processData(0, InventoryConstant.CALCULATE_INVENTORY_UNKNOWN, lInvAlloc);
		//		
		//				int z = 0;
		//		//		RTSException lex = new RTSException(605);
		//		//		throw lex;
		//			}
		//			catch (RTSException e)
		//			{
		//				javax.swing.JDialog frame = new javax.swing.JDialog();
		//				e.displayError(frame);
		//			}
		//		*/

		///**
		// * Use for testing issueinv
		// */
		//		/*
		//			Vector lvProcsInvData = new Vector();
		//			CompleteTransactionData lCmpltTransData = new CompleteTransactionData();
		//			ProcessInventoryData lProcsInvData1 = new ProcessInventoryData();
		//			ProcessInventoryData lProcsInvData2 = new ProcessInventoryData();
		//			Vector lvData = new Vector();
		//		
		//			lProcsInvData1.setOfcIssuanceNo(170);
		//			lProcsInvData1.setSubstaId(0);
		//			lProcsInvData1.setItmCd("PSP");
		//			lProcsInvData1.setInvItmYr(0);
		//			lProcsInvData1.setTransWsId("300");
		//			lProcsInvData1.setTransEmpId("RTS0000");
		//		
		//			lProcsInvData1.setEntity("E");
		//			lProcsInvData1.setId("RTS0000");
		//			
		//			lvProcsInvData.add(lProcsInvData1);
		//		
		//			lProcsInvData2.setOfcIssuanceNo(170);
		//			lProcsInvData2.setSubstaId(0);
		//			lProcsInvData2.setItmCd("WS");
		//			lProcsInvData2.setInvItmYr(2002);
		//			lProcsInvData2.setTransWsId("300");
		//			lProcsInvData2.setTransEmpId("RTS0000");
		//			lvProcsInvData.add(lProcsInvData2);
		//		
		//			lCmpltTransData.setInvItms(lvProcsInvData);
		//		
		//			try
		//			{
		//				CacheManager.loadCache();
		//				InventoryServerBusiness server = new InventoryServerBusiness();
		//		
		//				lCmpltTransData = (CompleteTransactionData) server.issueInv(lCmpltTransData);
		//		
		//				int z = 0;
		//			}
		//			catch (RTSException e)
		//			{
		//				javax.swing.JDialog frame = new javax.swing.JDialog();
		//				e.displayError(frame);
		//			}
		//		*/

		///**
		// * Use for testing chkNextAvailIndi
		// */
		//		/*
		//			InventoryProfileData lInvProfileData1 = new InventoryProfileData();
		//			InventoryProfileData lInvProfileData2 = new InventoryProfileData();
		//			Vector lvInvProfile = new Vector();
		//			DatabaseAccess dba = new DatabaseAccess();
		//			
		//			lInvProfileData1.setOfcIssuanceNo(170);
		//			lInvProfileData1.setSubstaId(0);
		//			lInvProfileData1.setItmCd("PSP");
		//			lInvProfileData1.setInvItmYr(0);
		//			lInvProfileData1.setTransWsId("300");
		//			lInvProfileData1.setTransEmpId("RTS0000");
		//			lvInvProfile.add(lInvProfileData1);
		//		
		//			lInvProfileData2.setOfcIssuanceNo(170);
		//			lInvProfileData2.setSubstaId(0);
		//			lInvProfileData2.setItmCd("WS");
		//			lInvProfileData2.setInvItmYr(2002);
		//			lInvProfileData2.setTransWsId("300");
		//			lInvProfileData2.setTransEmpId("RTS0000");
		//			lvInvProfile.add(lInvProfileData2);
		//		
		//			try
		//			{
		//				dba.beginTransaction();
		//				InventoryServerBusiness server = new InventoryServerBusiness();
		//		
		//				Integer k = server.chkNextAvailIndi(lvInvProfile, dba);
		//				dba.endTransaction(DatabaseAccess.NONE);
		//				int z = 0;
		//			}
		//			catch (RTSException e)
		//			{
		//				dba.endTransaction(DatabaseAccess.NONE);
		//				javax.swing.JDialog frame = new javax.swing.JDialog();
		//				e.displayError(frame);
		//			}
		//		*/
		///**
		// * Use for testing hold itm for RTS
		// */
		//		/*
		//			Vector lvProcsInvData = new Vector();
		//			ProcessInventoryData lProcsInvData1 = new ProcessInventoryData();
		//			ProcessInventoryData lProcsInvData2 = new ProcessInventoryData();
		//		
		//			lProcsInvData1.setOfcIssuanceNo(170);
		//			lProcsInvData1.setSubstaId(0);
		//			lProcsInvData1.setItmCd("PSP");
		//			lProcsInvData1.setInvItmYr(0);
		//			lProcsInvData1.setTransWsId("300");
		//			lProcsInvData1.setTransEmpId("RTS0000");
		//			lProcsInvData1.setEntity("E");
		//			lProcsInvData1.setId("RTS0000");
		//		
		//			try
		//			{
		//				InventoryServerBusiness server = new InventoryServerBusiness();
		//		
		//				lvProcsInvData = (Vector) server.holdInvItmForRTS(lProcsInvData1);
		//				int z = 0;
		//			}
		//			catch (RTSException e)
		//			{
		//				javax.swing.JDialog frame = new javax.swing.JDialog();
		//				e.displayError(frame);
		//			}
		//		*/

	}

	// class level fields
	private String csClientHost = "Unknown";

	/**
	 * InventorySeverBusiness constructor comment.
	 */
	public InventoryServerBusiness()
	{
		super();
	}

	/**
	 * InventorySeverBusiness constructor comment.
	 */
	public InventoryServerBusiness(String asClientHost)
	{
		super();
		csClientHost = asClientHost;
	}

	/**
	 * Add Inventory History Rows
	 *
	 * <p>Paramter avData is a Vector with two elements.  They are:
	 * <ul>
	 * <li>Element(0) - The inventory profile that needs to be added.
	 * <li>Element(1) - The entry for the admin log table.
	 * <eul>
	 * 
	 * @param avData Vector
	 * @throws RTSException  
	 */
	private void addInvHistData(Vector avData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_ADDINVHISTDATA_BEGIN + csClientHost);

		Vector lvInput = (Vector) avData;
		CompleteTransactionData laCTData =
			(CompleteTransactionData) lvInput.elementAt(
				CommonConstant.ELEMENT_1);
		DatabaseAccess laDBA =
			(DatabaseAccess) lvInput.elementAt(
				CommonConstant.ELEMENT_2);

		try
		{
			// add rows to inv hist
			InventoryHistory laInvHist = new InventoryHistory(laDBA);

			Vector lvTRInvDetail = laCTData.getTransInvDetail();
			for (int i = 0; i < lvTRInvDetail.size(); i++)
			{
				InventoryHistoryData laInvHistData =
					new InventoryHistoryData();

				laInvHistData.setOfcIssuanceNo(
					laCTData.getInvFuncTrans().getOfcIssuanceNo());
				laInvHistData.setSubStaId(
					laCTData.getInvFuncTrans().getSubstaId());
				laInvHistData.setTransAMDate(
					laCTData.getInvFuncTrans().getTransAMDate());
				laInvHistData.setTransWsId(
					laCTData.getInvFuncTrans().getTransWsId());
				laInvHistData.setTransTime(
					laCTData.getInvFuncTrans().getTransTime());
				laInvHistData.setTransEmpId(
					laCTData.getInvFuncTrans().getEmpId());
				laInvHistData.setTransCd(
					laCTData.getInvFuncTrans().getTransCd());
				laInvHistData.setInvcNo(
					laCTData.getInvFuncTrans().getInvcNo());
				laInvHistData.setInvcCorrIndi(
					laCTData.getInvFuncTrans().getInvcCorrIndi());

				TransactionInventoryDetailData laTIDD =
					(
						TransactionInventoryDetailData) lvTRInvDetail
							.elementAt(
						i);
				laInvHistData.setItmCd(laTIDD.getItmCd());
				laInvHistData.setInvId(laTIDD.getInvId());
				laInvHistData.setInvItmYr(laTIDD.getInvItmYr());
				laInvHistData.setInvItmNo(laTIDD.getInvItmNo());
				laInvHistData.setInvEndNo(laTIDD.getInvEndNo());
				laInvHistData.setInvQty(laTIDD.getInvQty());
				laInvHistData.setDelInvReasnCd(
					laTIDD.getDelInvReasnCd());
				laInvHistData.setDelInvReasnTxt(
					laTIDD.getDelInvReasnTxt());

				laInvHist.insInventoryHistory(laInvHistData);
			}

			// update log
			InventoryHistoryLog laInvHistLog =
				new InventoryHistoryLog(laDBA);
			InventoryHistoryLogData laInvHistLogData =
				new InventoryHistoryLogData();
			laInvHistLogData.setOfcIssuanceNo(
				laCTData.getInvFuncTrans().getOfcIssuanceNo());
			laInvHistLogData.setLastInsertDate(new RTSDate());
			laInvHistLog.updInventoryHistoryLog(laInvHistLogData);

		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_ADDINVHISTDATA_END + csClientHost);
		}
	}

	/**
	 * Adds an inventory profile in the RTS_INV_PROFILE table and 
	 * inserts a row in the RTS_ADMIN_LOG table.
	 *
	 * <p>Paramter avData is a Vector where the elements have to be as 
	 * follows:
	 * <ul>
	 * <li>Element(0) - The inventory profile that needs to be added.
	 * <li>Element(1) - The entry for the admin log table.
	 * <eul>
	 * 
	 * @param avData Vector
	 * @throws RTSException 
	 */
	private void addInvProfile(Vector avData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_ADDINVPROFILE_BEGIN + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();
		boolean lbIsSuccessful = false;

		try
		{
			laDBA.beginTransaction();
			InventoryProfile laDbIP = new InventoryProfile(laDBA);
			laDbIP.insInventoryProfile(
				(InventoryProfileData) avData.get(
					CommonConstant.ELEMENT_0));

			AdministrationLog laDbAL = new AdministrationLog(laDBA);
			laDbAL.insAdministrationLog(
				(AdministrationLogData) avData.get(
					CommonConstant.ELEMENT_1));
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			lbIsSuccessful = true;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_ADDINVPROFILE_END + csClientHost);
			if (!lbIsSuccessful)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
		}
	}

	/**
	 * Used when adding, modifying, or deleting an invoice line item.
	 *
	 * <p>Returns a vector where the elements are as follows:
	 * <ul>
	 * <lir>Element(1) - The modified invoice in the form of 
	 * 					 MFInventoryAllocationData object.
	 * <eul>
	 * 
	 * <p>Parameters avData is a vector where the elements have to be as 
	 * follows:
	 * <ul>
	 * <li>Element(1) - The invoice as a MFInventoryAllocationData 
	 * 					 object.
	 * <eul>
	 * 
	 * @param avData - Object (Vector)
	 * @return Vector
	 * @throws RTSException  
	 */
	private Object addModfyDelInvcItm(Object avData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_ADDMODFYDELINVCITM_BEGIN + csClientHost);

		Vector lvVct = (Vector) avData;
		MFInventoryAllocationData laMFInvAlloctnData =
			(MFInventoryAllocationData) lvVct.get(
				CommonConstant.ELEMENT_1);

		// Perform Inventory calculations
		if (!laMFInvAlloctnData.getCalcInv())
		{
			int liSelctdInvcItm = laMFInvAlloctnData.getSelctdRowIndx();
			InventoryAllocationUIData laInvAlloctnUIData =
				(InventoryAllocationUIData) laMFInvAlloctnData
					.getInvAlloctnData()
					.get(
					liSelctdInvcItm);

			if (!laMFInvAlloctnData
				.getTransCd()
				.equals(InventoryConstant.DEL))
			{
				// Calculate the inventory unknown
				ValidateInventoryPattern laVIP =
					new ValidateInventoryPattern();
				laInvAlloctnUIData =
					(InventoryAllocationUIData) laVIP.calcInvUnknown(
						laInvAlloctnUIData);

				// defect 9116
				if (laInvAlloctnUIData.getInvQty()
					> InventoryConstant.MAX_QTY)
				{
					throw new RTSException(591);
				}
				// end defect 9116

				laMFInvAlloctnData.getInvAlloctnData().set(
					liSelctdInvcItm,
					laInvAlloctnUIData);
			}

			// This flag is used to show that the calculation was 
			// successful.
			laMFInvAlloctnData.setCalcInv(true);

			lvVct.set(1, laMFInvAlloctnData);
		}
		// Validate the invoice item
		else if (laMFInvAlloctnData.getCalcInv())
		{
			int liSelctdInvcItm = laMFInvAlloctnData.getSelctdRowIndx();
			InventoryAllocationUIData laInvAlloctnUIData =
				(InventoryAllocationUIData) laMFInvAlloctnData
					.getInvAlloctnData()
					.get(
					liSelctdInvcItm);

			if (laMFInvAlloctnData
				.getTransCd()
				.equals(InventoryConstant.ADD))
			{
				// Validate this item against the invoice
				Vector lvTmp = validateInvcItms(lvVct);
				laMFInvAlloctnData =
					(MFInventoryAllocationData) lvTmp.get(
						CommonConstant.ELEMENT_0);

				// Check for any errors
				if (laInvAlloctnUIData.getStatusCd() != null)
				{
					// Create the exception
					int liErrorCd = laInvAlloctnUIData.getErrorCd();
					if (liErrorCd == ErrorsConstant.ERR_NUM_ITM_EXISTS)
					{
						// If the item already exists in the database, 
						// add the substa name to the detail text of 
						// the exception.
						ErrorMessagesData laEMD =
							ErrorMessagesCache.getErrMsg(
								ErrorsConstant.ERR_NUM_ITM_EXISTS);
						String lsMsgType = laEMD.getErrMsgCat();
						String lsMsg = laEMD.getErrMsgDesc();
						String lsTitle =
							RTSException.createErrorTitle(
								ErrorsConstant.ERR_NUM_ITM_EXISTS);

						lsMsg =
							lsMsg
								+ TXT_PAREN_OPEN
								+ (String) lvTmp.get(
									CommonConstant.ELEMENT_1)
								+ TXT_PAREN_CLOSE;

						RTSException leRTSException =
							new RTSException(lsMsgType, lsMsg, lsTitle);
						throw leRTSException;
					}
					else
					{
						RTSException leEx = new RTSException(liErrorCd);
						throw leEx;
					}
				}

				// If the invc itm has been added successfully, set the 
				// itm status to ADDED OK
				if (laInvAlloctnUIData.getStatusCd() == null)
				{
					laInvAlloctnUIData.setStatusCd(
						InventoryConstant.CHECK);
					laInvAlloctnUIData.setStatusDesc(
						InventoryConstant.ADDED_OK);
					laInvAlloctnUIData.setOrigItmModfyIndi(
						InventoryConstant.ITM_INDI_NEW);
				}
			}
			else if (
				laMFInvAlloctnData.getTransCd().equals(
					InventoryConstant.MODFY))
			{
				// Validate this item against the invoice
				Vector lvTmp = validateInvcItms(lvVct);
				laMFInvAlloctnData =
					(MFInventoryAllocationData) lvTmp.get(
						CommonConstant.ELEMENT_0);

				// Check for any errors
				if (laInvAlloctnUIData.getStatusCd() != null)
				{
					// Create the exception
					int liErrorCd = laInvAlloctnUIData.getErrorCd();
					if (liErrorCd == ErrorsConstant.ERR_NUM_ITM_EXISTS)
					{
						// If the item already exists in the database, 
						// add the substa name to the detail text of the
						// exception.
						ErrorMessagesData laEMD =
							ErrorMessagesCache.getErrMsg(
								ErrorsConstant.ERR_NUM_ITM_EXISTS);
						String lsMsgType = laEMD.getErrMsgCat();
						String lsMsg = laEMD.getErrMsgDesc();
						String lsTitle =
							RTSException.createErrorTitle(
								ErrorsConstant.ERR_NUM_ITM_EXISTS);

						lsMsg =
							lsMsg
								+ TXT_PAREN_OPEN
								+ (String) lvTmp.get(
									CommonConstant.ELEMENT_1)
								+ TXT_PAREN_CLOSE;

						RTSException leRTSException =
							new RTSException(lsMsgType, lsMsg, lsTitle);
						throw leRTSException;
					}
					else
					{
						RTSException leEx = new RTSException(liErrorCd);
						throw leEx;
					}
				}

				// If the invc itm has been modified successfully, set 
				// the itm status to ITEM MODIFIED AND VALIDATED
				if (laInvAlloctnUIData.getStatusCd() == null)
				{
					laInvAlloctnUIData.setStatusCd(
						InventoryConstant.CHECK);
					laInvAlloctnUIData.setStatusDesc(
						InventoryConstant.MODIFIED_OK);
					if (laInvAlloctnUIData.getOrigItmModfyIndi()
						!= InventoryConstant.ITM_INDI_NEW)
					{
						laInvAlloctnUIData.setOrigItmModfyIndi(
							InventoryConstant.ITM_INDI_MODIFIED);
					}
				}
			}
			else if (
				laMFInvAlloctnData.getTransCd().equals(
					InventoryConstant.DEL))
			{
				// Mark the item user deleted
				laInvAlloctnUIData.setStatusCd(
					InventoryConstant.DELETE);
				laInvAlloctnUIData.setStatusDesc(
					InventoryConstant.USER_DELETED);
				if (laInvAlloctnUIData.getOrigItmModfyIndi()
					== InventoryConstant.ITM_INDI_NEW)
				{
					laInvAlloctnUIData.setOrigItmModfyIndi(
						InventoryConstant.ITM_INDI_IGNORED);
				}
				else
				{
					laInvAlloctnUIData.setOrigItmModfyIndi(
						InventoryConstant.ITM_INDI_REMOVED);
				}
			}

			// This flag is used to show that the verification was 
			// successful.
			laMFInvAlloctnData.setProcsdInvItm(true);

			lvVct.set(CommonConstant.ELEMENT_1, laMFInvAlloctnData);
		}

		Log.write(
			Log.METHOD,
			this,
			MSG_ADDMODFYDELINVCITM_END + csClientHost);

		return lvVct;
	}

	/**
	 * Used to allocate an inventory range from one entity to another 
	 * entity.
	 *
	 * <p>Parameter avData is a vector where the elements have to be as 
	 * follows:
	 * <ul>
	 * <li>Element(1) - The inventory range that needs to be allocated 
	 * 					 in the form of InventoryAllocationUIData object.
	 * <li>Element(2) - The transaction header for the transcation in 
	 * 					 the form of TransactionHeader object.
	 * <eul>
	 * 
	 * @param avData Object
	 * @return Object
	 * @throws RTSException  
	 */
	private Object allocInvItms(Object avData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_ALLOCINVITMS_BEGIN + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();

		// make sure aData and the related objects exist and are the 
		//right classes.

		InventoryAllocationUIData laInvAlloctnUIData = null;

		if (avData != null
			&& avData instanceof Vector
			&& ((Vector) avData).size() >= CommonConstant.ELEMENT_2)
		{

			if (((Vector) avData).get(CommonConstant.ELEMENT_1) != null
				&& ((Vector) avData).get(CommonConstant.ELEMENT_1)
					instanceof InventoryAllocationUIData)
			{
				laInvAlloctnUIData =
					(InventoryAllocationUIData) ((Vector) avData).get(
						CommonConstant.ELEMENT_1);
			}
			else if (
				((Vector) avData).get(CommonConstant.ELEMENT_1)
					== null)
			{
				Log.write(Log.SQL_EXCP, this, TXT_NO_IAUID_OBJECT);
				RTSException leRTSEx =
					new RTSException(RTSException.JAVA_ERROR);
				leRTSEx.setDetailMsg(
					TXT_ERROR_COLON + TXT_NO_IAUID_OBJECT);
				throw leRTSEx;
			}
			else
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					TXT_WRONG_OBJECT
						+ ((Vector) avData).get(
							CommonConstant.ELEMENT_1));
				RTSException leRTSEx =
					new RTSException(RTSException.JAVA_ERROR);
				leRTSEx.setDetailMsg(
					TXT_ERROR_COLON
						+ TXT_WRONG_OBJECT
						+ ((Vector) avData).get(1));
				throw leRTSEx;
			}

			if (((Vector) avData).get(CommonConstant.ELEMENT_2)
				== null)
			{
				Log.write(Log.SQL_EXCP, this, TXT_NO_TRANSHDR);
				RTSException leRTSEx =
					new RTSException(RTSException.JAVA_ERROR);
				leRTSEx.setDetailMsg(TXT_ERROR_COLON + TXT_NO_TRANSHDR);
				throw leRTSEx;
			}
			else if (
				!(((Vector) avData).get(CommonConstant.ELEMENT_2)
					instanceof TransactionHeaderData))
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					TXT_WRONG_OBJECT + ((Vector) avData).get(2));
				RTSException leRTSEx =
					new RTSException(RTSException.JAVA_ERROR);
				leRTSEx.setDetailMsg(
					TXT_ERROR_COLON
						+ TXT_WRONG_OBJECT
						+ ((Vector) avData).get(
							CommonConstant.ELEMENT_2));
				throw leRTSEx;
			}
		}
		else
		{
			Log.write(Log.SQL_EXCP, this, TXT_OBJECT_NOT_RIGHT);
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR);
			leRTSEx.setDetailMsg(
				TXT_ERROR_COLON + TXT_OBJECT_NOT_RIGHT);
			throw leRTSEx;
		}

		InventoryAllocationUIData laInvAlloctionUIData =
			new InventoryAllocationUIData();
		ProcessInventoryData laProcsInvDataOrig =
			new ProcessInventoryData();
		ProcessInventoryData laUpdateProcsInvData =
			new ProcessInventoryData();
		Vector lvProcsInvData = new Vector();
		ComputeInventoryData laTmpComputeInvData =
			new ComputeInventoryData();
		ValidateInventoryPattern laValidateInvPatrn =
			new ValidateInventoryPattern();
		int liToSubstaId = laInvAlloctnUIData.getSubstaId();
		int liFromSubstaId = Integer.MIN_VALUE;

		CommonServerBusiness laCommnServerBus =
			new CommonServerBusiness();
		InventoryFunctionTransactionData laInvFuncTransData =
			new InventoryFunctionTransactionData();
		Vector lvTransInvDetailData =
			new Vector(CommonConstant.ELEMENT_2);
		CompleteTransactionData laCompltTransData =
			new CompleteTransactionData();

		boolean lbIsSuccessful = false;

		try
		{
			// get the max qty first
			ProcessInventoryData laProcInvMaxQty =
				getEntitysMaxAlloctn(laInvAlloctnUIData, laDBA);

			laDBA.beginTransaction();

			// Put the range(s) on hold until the allocation is complete.
			// Note that the range has already calculated.
			InventoryAllocation laLockInvAlloctnSQL =
				new InventoryAllocation(laDBA);
			// create an endpatrnseqno to attempt to put all range(s) 
			//on hold
			laInvAlloctnUIData.setEndPatrnSeqNo(
				laInvAlloctnUIData.getPatrnSeqNo()
					+ laInvAlloctnUIData.getInvQty()
					- 1);
			laLockInvAlloctnSQL.updInventoryAllocationForLogicalLock(
				laInvAlloctnUIData);

			// Make call to db and get a list of the substations to 
			// determine the from substation id (If from location 
			// different from the to location)
			if (!laInvAlloctnUIData
				.getFromLoc()
				.equals(laInvAlloctnUIData.getToLoc()))
			{
				GeneralSearchData laGSData = new GeneralSearchData();
				laGSData.setIntKey1(
					laInvAlloctnUIData.getOfcIssuanceNo());
				Vector lvSubsta = getSubsta(laGSData, laDBA);
				for (int i = 0; i < lvSubsta.size(); i++)
				{
					SubstationData laSub =
						(SubstationData) lvSubsta.get(i);
					if (laSub
						.getSubstaName()
						.equals(laInvAlloctnUIData.getFromLoc()))
					{
						liFromSubstaId = laSub.getSubstaId();
						break;
					}
				}
			}

			// Determine if inventory item is a PLP or OLDPLT or ROP
			if (ValidateInventoryPattern
				.chkIfItmPLPOrOLDPLTOrROP(
					laInvAlloctnUIData.getItmCd()))
			{
				// Make a copy of the aInvAlloctnUIData as 
				// lProcsInvDataOrig
				laProcsInvDataOrig =
					laProcsInvDataOrig.convertFromInvAlloctnData(
						laInvAlloctnUIData);
				laProcsInvDataOrig.setPatrnSeqCd(-1);
				laProcsInvDataOrig.setEndPatrnSeqNo(Integer.MIN_VALUE);
				laProcsInvDataOrig.setPatrnSeqNo(Integer.MIN_VALUE);
				laProcsInvDataOrig.setPLPFlag(true);
				laProcsInvDataOrig.setEntity(
					laInvAlloctnUIData.getInvLocIdCd());
				laProcsInvDataOrig.setId(laInvAlloctnUIData.getInvId());
				laProcsInvDataOrig.setInvLocIdCd(
					laInvAlloctnUIData.getFromInvLocIdCd());
				laProcsInvDataOrig.setInvId(
					laInvAlloctnUIData.getFromInvId());
			}
			else
			{
				// Validate the begin inventory item number and 
				// calculate the PatrnSeqNo
				laTmpComputeInvData =
					laValidateInvPatrn.validateItmNoInput(
						laInvAlloctnUIData);

				laInvAlloctnUIData =
					(
						InventoryAllocationUIData) laValidateInvPatrn
							.calcInvNo(
						laTmpComputeInvData);

				// Make a copy of the aInvAlloctnUIData as 
				// lProcsInvDataOrig
				laProcsInvDataOrig =
					laProcsInvDataOrig.convertFromInvAlloctnData(
						laInvAlloctnUIData);
				laProcsInvDataOrig.setEntity(
					laInvAlloctnUIData.getInvLocIdCd());
				laProcsInvDataOrig.setId(laInvAlloctnUIData.getInvId());
				laProcsInvDataOrig.setInvLocIdCd(
					laInvAlloctnUIData.getFromInvLocIdCd());
				laProcsInvDataOrig.setInvId(
					laInvAlloctnUIData.getFromInvId());

				// Calculate the EndPatrnSeqNo
				laInvAlloctionUIData.setItmCd(
					laInvAlloctnUIData.getItmCd());
				laInvAlloctionUIData.setInvItmYr(
					laInvAlloctnUIData.getInvItmYr());

				// the qty here should only be 1 since we are just 
				//computing the number
				laInvAlloctionUIData.setInvQty(
					(new Integer(1)).intValue());
				laInvAlloctionUIData.setInvItmNo(
					laInvAlloctnUIData.getInvItmEndNo());
				laInvAlloctionUIData.setInvItmEndNo(
					laInvAlloctnUIData.getInvItmEndNo());

				laTmpComputeInvData =
					laValidateInvPatrn.validateItmNoInput(
						laInvAlloctionUIData);

				laInvAlloctionUIData =
					(
						InventoryAllocationUIData) laValidateInvPatrn
							.calcInvNo(
						laTmpComputeInvData);

				// Verify begin and end numbers have the same PatrnSeqCd
				if (laProcsInvDataOrig.getPatrnSeqCd()
					!= laInvAlloctionUIData.getPatrnSeqCd())
				{
					RTSException leRTSException = new RTSException(607);
					throw leRTSException;
				}

				// Prepare data to verify inventory ranges exist for 
				// allocation
				laProcsInvDataOrig.setEndPatrnSeqNo(
					laInvAlloctionUIData.getPatrnSeqNo());
				if (laProcsInvDataOrig
					.getInvLocIdCd()
					.equals(InventoryConstant.CHAR_STOCK)
					|| laProcsInvDataOrig.getInvLocIdCd().equals(
						InventoryConstant.CHAR_CENTRAL))
				{
					laProcsInvDataOrig.setCentralStock(1);
				}
				else
				{
					laProcsInvDataOrig.setCentralStock(0);
				}
			}

			// Check to see if the item is currently in the from 
			// substation Inventory Allocation table
			if (liFromSubstaId != Integer.MIN_VALUE)
			{
				laProcsInvDataOrig.setSubstaId(liFromSubstaId);
			}
			else
			{
				laProcsInvDataOrig.setSubstaId(liToSubstaId);
			}

			if (laProcsInvDataOrig.isPLPFlag())
			{
				lvProcsInvData = chkForPLP(laProcsInvDataOrig, laDBA);
			}
			else
			{
				lvProcsInvData =
					chkInvItmRange(laProcsInvDataOrig, laDBA);
			}

			// Throw error 182 if ranges do not exist and the database 
			// is up
			if (lvProcsInvData.size() <= CommonConstant.ELEMENT_0)
			{
				RTSException leRTSException = new RTSException(182);
				throw leRTSException;
			}

			// If allocation to a different substation, check to see if 
			// the item is currently in the to substation Inventory 
			// Allocation table
			if (liFromSubstaId != Integer.MIN_VALUE)
			{
				Vector lvToSubstaData = new Vector();
				ProcessInventoryData laPID =
					(ProcessInventoryData) UtilityMethods.copy(
						laProcsInvDataOrig);
				laPID.setSubstaId(liToSubstaId);

				if (laPID.isPLPFlag())
				{
					lvToSubstaData = chkForPLP(laPID, laDBA);
				}
				else
				{
					lvToSubstaData = chkInvItmRange(laPID, laDBA);
				}

				// Throw error 642 if ranges exist in the To Substation
				if (lvToSubstaData.size() > CommonConstant.ELEMENT_0)
				{
					RTSException leRTSException = new RTSException(642);
					throw leRTSException;
				}
			}

			// Test if any of the selected rows are currently on hold
			for (int i = 0; i < lvProcsInvData.size(); i++)
			{
				if (((ProcessInventoryData) lvProcsInvData.get(i))
					.getInvStatusCd()
					!= 0)
				{
					RTSException leRTSException = new RTSException(594);
					throw leRTSException;
				}
			}

			// Check for complete range and then split
			if (!laProcsInvDataOrig.isPLPFlag())
			{
				chkForCompleteRngeAndSplt(
					laProcsInvDataOrig,
					lvProcsInvData,
					laDBA);
			}

			// Determine if this entity will now have too much allocated
			if (!laProcsInvDataOrig
				.getEntity()
				.equals(InventoryConstant.CHAR_STOCK)
				&& !laProcsInvDataOrig.getEntity().equals(
					InventoryConstant.CHAR_CENTRAL))
			{
				// check to see if there was a profile
				// then check to see if current 
				// qty + requested qty > maxqty
				if (laProcInvMaxQty.getMaxQty() != Integer.MIN_VALUE
					&& laProcInvMaxQty.getInvQty()
						+ (laProcsInvDataOrig.getEndPatrnSeqNo()
							- laProcsInvDataOrig.getPatrnSeqNo()
							+ 1)
						> laProcInvMaxQty.getMaxQty())
				{
					// we are over, throw the error
					RTSException leRTSEx = new RTSException(608);
					throw leRTSEx;
				}
			}

			// Reselect ranges to allocate
			if (laProcsInvDataOrig.isPLPFlag())
			{
				lvProcsInvData = chkForPLP(laProcsInvDataOrig, laDBA);
			}
			else
			{
				lvProcsInvData =
					chkInvItmRange(laProcsInvDataOrig, laDBA);
			}

			// Loop to change and update the InvId and InvLocIdCd
			for (int i = 0; i < lvProcsInvData.size(); i++)
			{
				laUpdateProcsInvData =
					(ProcessInventoryData) lvProcsInvData.get(i);
				laUpdateProcsInvData.setNewSubstaId(liToSubstaId);
				laUpdateProcsInvData.setInvLocIdCd(
					laProcsInvDataOrig.getEntity());
				laUpdateProcsInvData.setInvId(
					laProcsInvDataOrig.getId());

				InventoryAllocation laInvAlloctnSQL =
					new InventoryAllocation(laDBA);
				laInvAlloctnSQL.updInventoryAllocation(
					laUpdateProcsInvData.convertToInvAlloctnUIData(
						laUpdateProcsInvData));
			}

			// Loop to write transaction data objects
			if (laInvAlloctnUIData
				.getFromLoc()
				.equals(laInvAlloctnUIData.getToLoc()))
			{
				// Create the InvFuncTrans data object
				laInvFuncTransData.setTransCd(TransCdConstant.INVALL);
				laInvFuncTransData.setInvcCorrIndi(0);
				laInvFuncTransData.setInvcNo(
					CommonConstant.STR_SPACE_EMPTY);

				// Create the TransInvDetail data object
				for (int i = 0; i < 2; i++)
				{
					TransactionInventoryDetailData laTransInvDetailData =
						new TransactionInventoryDetailData();
					laTransInvDetailData.setTransCd(
						TransCdConstant.INVALL);
					laTransInvDetailData.setItmCd(
						laProcsInvDataOrig.getItmCd());
					laTransInvDetailData.setInvItmYr(
						laProcsInvDataOrig.getInvItmYr());
					laTransInvDetailData.setInvItmNo(
						laProcsInvDataOrig.getInvItmNo());
					laTransInvDetailData.setInvEndNo(
						laProcsInvDataOrig.getInvItmEndNo());
					laTransInvDetailData.setInvQty(
						laProcsInvDataOrig.getInvQty());
					laTransInvDetailData.setDelInvReasnCd(0);
					if (i == 0)
					{
						laTransInvDetailData.setInvId(
							laProcsInvDataOrig.getInvId());
						laTransInvDetailData.setInvLocIdCd(
							laProcsInvDataOrig.getInvLocIdCd());
						laTransInvDetailData.setDetailStatusCd(-1);
					}
					else
					{
						laTransInvDetailData.setInvId(
							laProcsInvDataOrig.getId());
						laTransInvDetailData.setInvLocIdCd(
							laProcsInvDataOrig.getEntity());
						laTransInvDetailData.setDetailStatusCd(1);
					}
					lvTransInvDetailData.addElement(
						laTransInvDetailData);
				}

				// Make call to common to finish and write the trans 
				// data
				laCompltTransData.setTransCode(TransCdConstant.INVALL);
				laCompltTransData.setInvFuncTrans(laInvFuncTransData);
				laCompltTransData.setTransInvDetail(
					lvTransInvDetailData);

				Vector lvInputTrans = new Vector();
				lvInputTrans.addElement(
					((Vector) avData).get(CommonConstant.ELEMENT_2));
				lvInputTrans.addElement(laCompltTransData);
				lvInputTrans.addElement(laDBA);
				laCompltTransData =
					(
						CompleteTransactionData) laCommnServerBus
							.processData(
						GeneralConstant.COMMON,
						CommonConstant.PROC_TRANS,
						lvInputTrans);
			}
			else
			{
				// Create transactions for INVALL if from locidcd not 
				// equal to 'A' or 'C'
				if (!laProcsInvDataOrig
					.getInvLocIdCd()
					.equals(InventoryConstant.CHAR_CENTRAL)
					&& !laProcsInvDataOrig.getInvLocIdCd().equals(
						InventoryConstant.CHAR_STOCK))
				{
					// Create the InvFuncTrans data object
					laInvFuncTransData.setTransCd(
						TransCdConstant.INVALL);
					laInvFuncTransData.setInvcCorrIndi(0);
					laInvFuncTransData.setInvcNo(
						CommonConstant.STR_SPACE_EMPTY);

					// Create the TransInvDetail data object
					for (int i = 0; i < 2; i++)
					{
						TransactionInventoryDetailData lTransInvDetailData =
							new TransactionInventoryDetailData();
						lTransInvDetailData.setTransCd(
							TransCdConstant.INVALL);
						lTransInvDetailData.setItmCd(
							laProcsInvDataOrig.getItmCd());
						lTransInvDetailData.setInvItmYr(
							laProcsInvDataOrig.getInvItmYr());
						lTransInvDetailData.setInvItmNo(
							laProcsInvDataOrig.getInvItmNo());
						lTransInvDetailData.setInvEndNo(
							laProcsInvDataOrig.getInvItmEndNo());
						lTransInvDetailData.setDelInvReasnCd(0);
						if (i == 0)
						{
							lTransInvDetailData.setInvQty(
								-laProcsInvDataOrig.getInvQty());
							lTransInvDetailData.setInvId(
								laProcsInvDataOrig.getInvId());
							lTransInvDetailData.setInvLocIdCd(
								laProcsInvDataOrig.getInvLocIdCd());
							lTransInvDetailData.setDetailStatusCd(-1);
						}
						else
						{
							lTransInvDetailData.setInvQty(
								laProcsInvDataOrig.getInvQty());
							lTransInvDetailData.setInvId(CENTRAL_LOCID);
							lTransInvDetailData.setInvLocIdCd(
								InventoryConstant.CHAR_CENTRAL);
							lTransInvDetailData.setDetailStatusCd(1);
						}
						lvTransInvDetailData.addElement(
							lTransInvDetailData);
					}

					// Make call to common to finish and write the trans 
					// data
					laCompltTransData.setTransCode(
						TransCdConstant.INVALL);
					laCompltTransData.setInvFuncTrans(
						laInvFuncTransData);
					laCompltTransData.setTransInvDetail(
						lvTransInvDetailData);

					Vector lvInputTrans = new Vector();
					lvInputTrans.addElement(
						((Vector) avData).get(
							CommonConstant.ELEMENT_2));
					lvInputTrans.addElement(laCompltTransData);
					lvInputTrans.addElement(laDBA);
					laCompltTransData =
						(
							CompleteTransactionData) laCommnServerBus
								.processData(
							GeneralConstant.COMMON,
							CommonConstant.PROC_TRANS,
							lvInputTrans);
				}

				// Code to wait for 1 second between writing the INVALL 
				// and INVOFC transactions.
				// Without this pause, there is a duplicate key error.
				Thread.sleep(SLEEP_1_SECOND);

				// Create transactions for INVOFC

				// Create the InvFuncTrans data object
				laInvFuncTransData.setTransCd(TransCdConstant.INVOFC);
				laInvFuncTransData.setInvcCorrIndi(0);
				laInvFuncTransData.setInvcNo(
					CommonConstant.STR_SPACE_EMPTY);

				// Create the TransInvDetail data object
				for (int i = 0; i < 2; i++)
				{
					TransactionInventoryDetailData laTransInvDetailData =
						new TransactionInventoryDetailData();
					laTransInvDetailData.setTransCd(
						TransCdConstant.INVOFC);
					laTransInvDetailData.setItmCd(
						laProcsInvDataOrig.getItmCd());
					laTransInvDetailData.setInvItmYr(
						laProcsInvDataOrig.getInvItmYr());
					laTransInvDetailData.setInvItmNo(
						laProcsInvDataOrig.getInvItmNo());
					laTransInvDetailData.setInvEndNo(
						laProcsInvDataOrig.getInvItmEndNo());
					laTransInvDetailData.setInvQty(
						laProcsInvDataOrig.getInvQty());
					laTransInvDetailData.setDelInvReasnCd(0);
					if (i == 0)
					{
						laTransInvDetailData.setInvId(
							String.valueOf(liFromSubstaId));
						if (laProcsInvDataOrig
							.getInvLocIdCd()
							.equals(InventoryConstant.CHAR_STOCK))
						{
							laTransInvDetailData.setInvLocIdCd(
								InventoryConstant.CHAR_STOCK);
						}
						else
							laTransInvDetailData.setInvLocIdCd(
								InventoryConstant.CHAR_CENTRAL);
						laTransInvDetailData.setDetailStatusCd(-1);
					}
					else
					{
						laTransInvDetailData.setInvId(
							String.valueOf(liToSubstaId));
						if (laProcsInvDataOrig
							.getInvLocIdCd()
							.equals(InventoryConstant.CHAR_STOCK))
							laTransInvDetailData.setInvLocIdCd(
								InventoryConstant.CHAR_STOCK);
						else
							laTransInvDetailData.setInvLocIdCd(
								InventoryConstant.CHAR_CENTRAL);
						laTransInvDetailData.setDetailStatusCd(1);
					}
					lvTransInvDetailData.addElement(
						laTransInvDetailData);
				}

				// Make call to common to finish and write the trans data
				laCompltTransData.setTransCode(TransCdConstant.INVOFC);
				laCompltTransData.setInvFuncTrans(laInvFuncTransData);
				laCompltTransData.setTransInvDetail(
					lvTransInvDetailData);

				Vector lvInputTrans = new Vector();
				lvInputTrans.addElement(
					((Vector) avData).get(CommonConstant.ELEMENT_2));
				lvInputTrans.addElement(laCompltTransData);
				lvInputTrans.addElement(laDBA);
				laCompltTransData =
					(
						CompleteTransactionData) laCommnServerBus
							.processData(
						GeneralConstant.COMMON,
						CommonConstant.PROC_TRANS,
						lvInputTrans);
			}

			// Get the transid
			laInvAlloctnUIData.setTransId(
				laCompltTransData.getTransId());

			// Commit to database
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			lbIsSuccessful = true;

			return avData;
		}
		catch (InterruptedException aeIEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeIEx);
			throw leRTSEx;
		}
		// catch classcastexception 
		catch (ClassCastException aeCCEx)
		{
			lbIsSuccessful = false;
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeCCEx);
			throw leRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_ALLOCINVITMS_END + csClientHost);
			if (!lbIsSuccessful)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
		}
	}

	/**
	 * Checks to see if duplicates are allowed.
	 * 
	 * @param laInvAlloctnData InventoryAllocationData
	 * @return boolean
	 */
	private boolean chkDupsAllowed(String lsItemCode)
	{
		// check to see if we should check for duplicates
		PlateTypeData laPltType2 =
			PlateTypeCache.getPlateType(lsItemCode);
		return (
			laPltType2 != null
				&& laPltType2.getDuplsAllowdCd() > 1
				&& laPltType2.getUserPltNoIndi() > 0);
	}

	/**
	 * Used to break apart inventory ranges in the RTS_INV_ALLOCATION 
	 * table.
	 * <p>Example: If the range in the database is 3WD to 19WD all 
	 * allocated to central and the user wants to allocate the range 5WD 
	 * to 10WD to workstation 100, this method will update the original 
	 * range to 3WD to 4WD, insert a second range 5WD to 10WD, and 
	 * insert a third range 11WD to 19WD.
	 *
	 * @param aaProcsInvDataOrig ProcessInventoryData The original 
	 * 		  inventory range entered by the user.
	 * @param avProcsInvData Vector A vector of ProcessInventoryData 
	 * 		  objects that represent the rows in the RTS_INV_ALLOCATION 
	 * 		  table that intersect the user specified range and 
	 *        subsequently need to be broken up.
	 * @param aaDBA DatabaseAccess The database connection
	 * @throws RTSException  
	 */
	private void chkForCompleteRngeAndSplt(
		ProcessInventoryData aaProcsInvDataOrig,
		Vector avProcsInvData,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_CHKFROMCOMPLETERNGEANDSPLT_BEGIN + csClientHost);

		ProcessInventoryData laUpdateProcsInvData =
			new ProcessInventoryData();
		ProcessInventoryData laInsertProcsInvData1 =
			new ProcessInventoryData();
		ProcessInventoryData laInsertProcsInvData2 =
			new ProcessInventoryData();
		ValidateInventoryPattern laValidateInvPatrn =
			new ValidateInventoryPattern();
		boolean lbInvalid = false;

		// Determine if the complete range exists
		if (!((ProcessInventoryData) avProcsInvData
			.get(CommonConstant.ELEMENT_0))
			.getRangeCd()
			.equals(InventoryConstant.RANGE_OUTSIDE))
		{
			lbInvalid = false;
			int liBeginWithin = -1;
			int liBeginOutside = -1;
			int liBeginEnd = -1;
			int liEndWithin = -1;
			int liEndOutside = -1;
			int liLeft = -1;
			int liRight = -1;
			for (int i = 0; i < avProcsInvData.size(); i++)
			{
				ProcessInventoryData laProcsInvData =
					(ProcessInventoryData) avProcsInvData.get(i);
				if (laProcsInvData
					.getRangeCd()
					.equals(InventoryConstant.RANGE_BEGIN_END))
				{
					liBeginEnd = i;
				}
				else if (
					laProcsInvData.getRangeCd().equals(
						InventoryConstant.RANGE_BEGIN_WITHIN))
				{
					liBeginWithin = i;
				}
				else if (
					laProcsInvData.getRangeCd().equals(
						InventoryConstant.RANGE_BEGIN_OUTSIDE))
				{
					liBeginOutside = i;
				}
				else if (
					laProcsInvData.getRangeCd().equals(
						InventoryConstant.RANGE_WITHIN_END))
				{
					liEndWithin = i;
				}
				else if (
					laProcsInvData.getRangeCd().equals(
						InventoryConstant.RANGE_END_OUTSIDE))
				{
					liEndOutside = i;
				}
				else if (
					laProcsInvData.getRangeCd().equals(
						InventoryConstant.RANGE_LEFT))
				{
					liLeft = i;
				}
				else if (
					laProcsInvData.getRangeCd().equals(
						InventoryConstant.RANGE_RIGHT))
				{
					liRight = i;
				}
			}
			if ((liBeginEnd >= 0)
				|| (liBeginOutside >= 0 || liEndOutside >= 0)
				|| (liBeginWithin >= 0 && liEndWithin >= 0)
				|| (liBeginWithin >= 0 && liRight >= 0)
				|| (liLeft >= 0 && liEndWithin >= 0)
				|| (liLeft >= 0 && liRight >= 0))
			{
				// Determine if the sequence is contiquous
				for (int i = 0; i + 1 < avProcsInvData.size(); i++)
				{
					ProcessInventoryData laPID =
						(ProcessInventoryData) avProcsInvData.get(i);
					ProcessInventoryData laPID2 =
						(ProcessInventoryData) avProcsInvData.get(
							i + 1);
					if (laPID2.getPatrnSeqNo()
						!= laPID.getPatrnSeqNo() + laPID.getInvQty())
					{
						lbInvalid = true;
						break;
					}
				}
			}
			else
			{
				lbInvalid = true;
			}

			// If the sequence is not contiquous, throw error 605
			if (lbInvalid)
			{
				RTSException leRTSException = new RTSException(605);
				throw leRTSException;
			}
		}

		// Split row or rows based on which RangeCd is returned
		for (int i = 0; i < avProcsInvData.size(); i++)
		{
			ProcessInventoryData laProcsInvData =
				(ProcessInventoryData) avProcsInvData.get(i);

			if (laProcsInvData
				.getRangeCd()
				.equals(InventoryConstant.RANGE_BEGIN_END)
				|| laProcsInvData.getRangeCd().equals(
					InventoryConstant.RANGE_BEGIN_WITHIN)
				|| laProcsInvData.getRangeCd().equals(
					InventoryConstant.RANGE_WITHIN_END)
				|| laProcsInvData.getRangeCd().equals(
					InventoryConstant.RANGE_WITHIN))
			{
				continue;
			}
			else if (
				laProcsInvData.getRangeCd().equals(
					InventoryConstant.RANGE_BEGIN_OUTSIDE)
					|| laProcsInvData.getRangeCd().equals(
						InventoryConstant.RANGE_RIGHT))
			{
				// Convert lProcsInvData to type 
				// InventoryAllocationUIData 
				// and calculate the new beg no for the right using the 
				// orig end number and a qty of 2
				InventoryAllocationUIData laIAUID =
					laProcsInvData.convertToInvAlloctnUIData(
						laProcsInvData);
				laIAUID.setInvItmNo(
					aaProcsInvDataOrig.getInvItmEndNo());
				laIAUID.setInvQty(InventoryConstant.COMPUTE_NEXT_ITEM);
				ComputeInventoryData laCID =
					laValidateInvPatrn.validateItmNoInput(laIAUID);

				laIAUID =
					(
						InventoryAllocationUIData) laValidateInvPatrn
							.calcInvEndNo(
						laCID);

				// Prepare update data (left)
				laUpdateProcsInvData =
					(ProcessInventoryData) UtilityMethods.copy(
						laProcsInvData);
				laUpdateProcsInvData.setInvItmEndNo(
					aaProcsInvDataOrig.getInvItmEndNo());
				laUpdateProcsInvData.setInvQty(
					aaProcsInvDataOrig.getEndPatrnSeqNo()
						- laProcsInvData.getPatrnSeqNo()
						+ InventoryConstant.COMPUTE_CURRENT_ITEM);

				// Prepare insert data (right)
				laInsertProcsInvData1 =
					(ProcessInventoryData) UtilityMethods.copy(
						laProcsInvData);
				laInsertProcsInvData1.setInvItmNo(
					laIAUID.getInvItmEndNo());
				laInsertProcsInvData1.setPatrnSeqNo(
					laProcsInvData.getPatrnSeqNo()
						+ laUpdateProcsInvData.getInvQty());
				laInsertProcsInvData1.setInvQty(
					laProcsInvData.getInvQty()
						- laUpdateProcsInvData.getInvQty());

				// Update the existing inventory row and insert the 
				// new row
				Vector lvTmp = new Vector(CommonConstant.ELEMENT_2);
				lvTmp.add(laUpdateProcsInvData);
				lvTmp.add(laInsertProcsInvData1);
				performUpdateInsertForAlloctn(lvTmp, aaDBA);

				continue;
			}
			else if (
				laProcsInvData.getRangeCd().equals(
					InventoryConstant.RANGE_END_OUTSIDE)
					|| laProcsInvData.getRangeCd().equals(
						InventoryConstant.RANGE_LEFT))
			{
				// Convert lProcsInvData to type 
				// InventoryAllocationUIData 
				// and calculate the new end no for the left using the 
				// orig end number and a qty of -1
				InventoryAllocationUIData laIAUID =
					laProcsInvData.convertToInvAlloctnUIData(
						laProcsInvData);
				laIAUID.setInvItmNo(aaProcsInvDataOrig.getInvItmNo());
				laIAUID.setInvQty(
					InventoryConstant.COMPUTE_PREVIOUS_ITEM);
				ComputeInventoryData laCID =
					laValidateInvPatrn.validateItmNoInput(laIAUID);

				laIAUID =
					(
						InventoryAllocationUIData) laValidateInvPatrn
							.calcInvEndNo(
						laCID);

				// Prepare update data (left)
				laUpdateProcsInvData =
					(ProcessInventoryData) UtilityMethods.copy(
						laProcsInvData);
				laUpdateProcsInvData.setInvItmEndNo(
					laIAUID.getInvItmEndNo());
				laUpdateProcsInvData.setInvQty(
					aaProcsInvDataOrig.getPatrnSeqNo()
						- laProcsInvData.getPatrnSeqNo());

				// Prepare insert data (right)
				laInsertProcsInvData1 =
					(ProcessInventoryData) UtilityMethods.copy(
						laProcsInvData);
				laInsertProcsInvData1.setInvItmNo(
					aaProcsInvDataOrig.getInvItmNo());
				laInsertProcsInvData1.setPatrnSeqNo(
					laProcsInvData.getPatrnSeqNo()
						+ laUpdateProcsInvData.getInvQty());
				laInsertProcsInvData1.setInvQty(
					laProcsInvData.getInvQty()
						- laUpdateProcsInvData.getInvQty());

				// Update the existing inventory row and insert 
				// the new row
				Vector lvTmp = new Vector(CommonConstant.ELEMENT_2);
				lvTmp.add(laUpdateProcsInvData);
				lvTmp.add(laInsertProcsInvData1);
				performUpdateInsertForAlloctn(lvTmp, aaDBA);

				continue;
			}
			else if (
				laProcsInvData.getRangeCd().equals(
					InventoryConstant.RANGE_OUTSIDE))
			{
				// Convert lProcsInvData to type 
				// InventoryAllocationUIData 
				// and calculate the new end no for the left using the 
				// orig end number and a qty of -1
				InventoryAllocationUIData laIAUID =
					laProcsInvData.convertToInvAlloctnUIData(
						laProcsInvData);
				laIAUID.setInvItmNo(aaProcsInvDataOrig.getInvItmNo());
				laIAUID.setInvQty(
					InventoryConstant.COMPUTE_PREVIOUS_ITEM);
				ComputeInventoryData laCID =
					laValidateInvPatrn.validateItmNoInput(laIAUID);

				laIAUID =
					(
						InventoryAllocationUIData) laValidateInvPatrn
							.calcInvEndNo(
						laCID);

				// Prepare update data (left)
				laUpdateProcsInvData =
					(ProcessInventoryData) UtilityMethods.copy(
						laProcsInvData);
				laUpdateProcsInvData.setInvItmEndNo(
					laIAUID.getInvItmEndNo());
				laUpdateProcsInvData.setInvQty(
					aaProcsInvDataOrig.getPatrnSeqNo()
						- laProcsInvData.getPatrnSeqNo());

				// Prepare insert data
				ProcessInventoryData laTmpProcsInvData =
					new ProcessInventoryData();
				laTmpProcsInvData =
					(ProcessInventoryData) UtilityMethods.copy(
						laProcsInvData);
				laTmpProcsInvData.setInvItmNo(
					aaProcsInvDataOrig.getInvItmNo());
				laTmpProcsInvData.setPatrnSeqNo(
					laProcsInvData.getPatrnSeqNo()
						+ laUpdateProcsInvData.getInvQty());
				laTmpProcsInvData.setInvQty(
					laProcsInvData.getInvQty()
						- laUpdateProcsInvData.getInvQty());

				// Convert lTmpProcsInvData to type 
				// InventoryAllocationUIData and calculate the new beg 
				// no for the right using the orig end number and a qty 
				// of 2
				laIAUID =
					laProcsInvData.convertToInvAlloctnUIData(
						laTmpProcsInvData);
				laIAUID.setInvItmNo(
					aaProcsInvDataOrig.getInvItmEndNo());
				laIAUID.setInvQty(InventoryConstant.COMPUTE_NEXT_ITEM);
				laCID = laValidateInvPatrn.validateItmNoInput(laIAUID);

				laIAUID =
					(
						InventoryAllocationUIData) laValidateInvPatrn
							.calcInvEndNo(
						laCID);

				// Prepare insert1 data (middle)
				laInsertProcsInvData1 =
					(ProcessInventoryData) UtilityMethods.copy(
						laTmpProcsInvData);
				laInsertProcsInvData1.setInvItmEndNo(
					aaProcsInvDataOrig.getInvItmEndNo());
				laInsertProcsInvData1.setInvQty(
					aaProcsInvDataOrig.getEndPatrnSeqNo()
						- laTmpProcsInvData.getPatrnSeqNo()
						+ InventoryConstant.COMPUTE_CURRENT_ITEM);

				// Prepare insert2 data (right)
				laInsertProcsInvData2 =
					(ProcessInventoryData) UtilityMethods.copy(
						laTmpProcsInvData);
				laInsertProcsInvData2.setInvItmNo(
					laIAUID.getInvItmEndNo());
				laInsertProcsInvData2.setPatrnSeqNo(
					laTmpProcsInvData.getPatrnSeqNo()
						+ laInsertProcsInvData1.getInvQty());
				laInsertProcsInvData2.setInvQty(
					laTmpProcsInvData.getInvQty()
						- laInsertProcsInvData1.getInvQty());

				// Update the existing inventory row and insert the 
				// new row
				Vector lvTmp = new Vector(CommonConstant.ELEMENT_3);
				lvTmp.add(laUpdateProcsInvData);
				lvTmp.add(laInsertProcsInvData1);
				lvTmp.add(laInsertProcsInvData2);
				performUpdateInsertForAlloctn(lvTmp, aaDBA);

				continue;
			}
		}

		Log.write(
			Log.METHOD,
			this,
			MSG_CHKFORCOMPLETERNGEANDSPLT_END + csClientHost);
	}

	/**
	 * Check for a specific inventory item in the RTS_INV_ALLOCATION 
	 * table.  If the item is not found, then it checks in the 
	 * RTS_TR_INV_DETAIL table.
	 * 
	 * @param aaData ProcessInventoryData The search parameters in the 
	 * 		  form of ProcessInventoryData.
	 * @return Vector
	 * @throws RTSException 
	 */
	private Vector chkForInvItmInDb(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_CHKFORINVITMINDB_BEGIN + csClientHost);

		// PCR 34
		ProcessInventoryData laProcsInvData = null;
		// Use DatabaseAccess if provided 
		DatabaseAccess laDBA = null;
		boolean lbContainsDBA = false;
		if (aaData instanceof Vector)
		{
			laProcsInvData =
				(ProcessInventoryData) (((Vector) aaData).elementAt(1));
			if (((Vector) aaData).elementAt(CommonConstant.ELEMENT_0)
				instanceof DatabaseAccess)
			{
				laDBA =
					(DatabaseAccess) (((Vector) aaData)
						.elementAt(CommonConstant.ELEMENT_0));
				lbContainsDBA = true;
			}
		}
		else
		{
			laProcsInvData = (ProcessInventoryData) aaData;
		}
		// end PCR 34
		Vector lvRtrnData = new Vector();

		try
		{
			if (laDBA == null)
			{
				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();
			}
			// Calculate the patrnseqno
			laProcsInvData.setInvQty(
				InventoryConstant.COMPUTE_CURRENT_ITEM);
			InventoryAllocationUIData laInvData =
				laProcsInvData.convertToInvAlloctnUIData(
					laProcsInvData);

			ValidateInventoryPattern laVID =
				new ValidateInventoryPattern();
			laInvData =
				(InventoryAllocationUIData) laVID.calcInvUnknown(
					laInvData);

			laProcsInvData =
				laProcsInvData.convertFromInvAlloctnData(laInvData);

			// Check for the inventory in the rts_inv_allocation table.
			InventoryAllocation laInvAlloctnSQL =
				new InventoryAllocation(laDBA);

			if (ValidateInventoryPattern
				.chkIfItmPLPOrOLDPLTOrROP(laProcsInvData.getItmCd()))
			{
				laProcsInvData.setPatrnSeqNo(Integer.MIN_VALUE);
			}

			lvRtrnData =
				laInvAlloctnSQL.qryInventoryAllocationForSpecificItem(
					laProcsInvData);

			// If the inventory is not found, then check for the 
			// inventory in the rts_tr_inv_detail table.
			if (lvRtrnData.size() == CommonConstant.ELEMENT_0)
			{
				TransactionInventoryDetailData laTransInvDetailData =
					new TransactionInventoryDetailData();
				TransactionInventoryDetail laTransInvDetailSQL =
					new TransactionInventoryDetail(laDBA);

				laTransInvDetailData.setOfcIssuanceNo(
					laProcsInvData.getOfcIssuanceNo());
				laTransInvDetailData.setSubstaId(
					laProcsInvData.getSubstaId());
				laTransInvDetailData.setItmCd(
					laProcsInvData.getItmCd());
				laTransInvDetailData.setInvItmYr(
					laProcsInvData.getInvItmYr());
				laTransInvDetailData.setInvItmNo(
					laProcsInvData.getInvItmNo());
				laTransInvDetailData.setInvEndNo(
					laProcsInvData.getInvItmNo());

				lvRtrnData =
					laTransInvDetailSQL
						.qryTransactionInventoryDetailForSpecificItem(
						laTransInvDetailData);

				if (lvRtrnData.size() > CommonConstant.ELEMENT_0)
				{
					// Convert trinvdetaildata object to 
					// processinventorydata object. If more than one 
					// item is found, only return the first row found.
					ProcessInventoryData laPID =
						new ProcessInventoryData();
					laTransInvDetailData =
						(
							TransactionInventoryDetailData) lvRtrnData
								.get(
							CommonConstant.ELEMENT_0);
					laPID.setOfcIssuanceNo(
						laTransInvDetailData.getOfcIssuanceNo());
					laPID.setSubstaId(
						laTransInvDetailData.getSubstaId());
					laPID.setItmCd(laTransInvDetailData.getItmCd());
					laPID.setInvItmYr(
						laTransInvDetailData.getInvItmYr());
					laPID.setInvItmNo(
						laTransInvDetailData.getInvItmNo());
					laPID.setInvItmEndNo(
						laTransInvDetailData.getInvEndNo());
					laPID.setInvLocIdCd(InventoryConstant.NOT_FOUND);
					// Set InvId = "0" when InvLocIdCd = "U" 
					laPID.setInvId(CENTRAL_LOCID);
					laPID.setInvQty(laTransInvDetailData.getInvQty());
					laPID.setAlreadyIssued(true);

					// Set PreviouslyVoided indicator if the item number 
					// has ever been voided
					if (lvRtrnData.size() > CommonConstant.ELEMENT_1)
					{
						for (int j = 0; j < lvRtrnData.size(); j++)
						{
							TransactionInventoryDetailData laTrInvDetail =
								(
									TransactionInventoryDetailData) lvRtrnData
										.get(
									j);
							if (laTrInvDetail
								.getTransCd()
								.equals(TransCdConstant.INVVD))
							{
								laPID.setPreviouslyVoidedItem(true);
								break;
							}
						}
					}
					lvRtrnData.set(CommonConstant.ELEMENT_0, laPID);
				}
			}
			else
			{
				// Convert allocation data object to processinventorydata 
				// object
				ProcessInventoryData laPID = new ProcessInventoryData();
				laPID =
					laPID.convertFromInvAlloctnData(
						(InventoryAllocationData) lvRtrnData.get(
							CommonConstant.ELEMENT_0));
				lvRtrnData.set(CommonConstant.ELEMENT_0, laPID);
			}
			// do not end transaction if dba passed
			if (!lbContainsDBA)
			{
				laDBA.endTransaction(DatabaseAccess.NONE);
			}

			return lvRtrnData;
			// defect 9116
		}
		catch (NullPointerException aeNPEx)
		{
			// if we get a null pointer, make sure we do a rollback
			throw new RTSException(RTSException.SERVER_DOWN, aeNPEx);
		}
		catch (RTSException aeRTSEx)
		{
			// end defect 9116
			if (!lbContainsDBA)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_CHKFORINVITMINDB_END + csClientHost);
		}
	}

	/**
	 * Check for a specific inventory item in the RTS_INV_ALLOCATION 
	 * table.  
	 * If the item is not found, then it checks in the RTS_TR_INV_DETAIL 
	 * table.
	 * 
	 * <p>Now this method will also put the inventory on hold if 
	 * appropriate.
	 *  
	 * @param aaData ProcessInventoryData The search parameters in the 
	 * 		  form of ProcessInventoryData.
	 * @return Vector
	 * @throws RTSException  
	 */
	private Vector chkForInvItmToIssue(Object aaData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_CHKFORINVITMTOISSUE_BEGIN + csClientHost);

		Vector lvRtrnData = new Vector();

		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			// defect 8696 
			// we should start the connection at 
			laDBA.beginTransaction();

			// pass the dba in a vector
			Vector lvChkInDb = new Vector(2);
			lvChkInDb.addElement(laDBA);
			lvChkInDb.addElement(aaData);

			lvRtrnData = chkForInvItmInDb(lvChkInDb);
			// end defect 8696

			if (lvRtrnData.size() > CommonConstant.ELEMENT_0)
			{
				ProcessInventoryData laProcsOrigInvData =
					(ProcessInventoryData) aaData;

				ProcessInventoryData laProcsNewInvData =
					(ProcessInventoryData) lvRtrnData.elementAt(
						CommonConstant.ELEMENT_0);

				if (!laProcsOrigInvData
					.getInvItmNo()
					.equals(laProcsNewInvData.getInvItmNo()))
				{
					laProcsNewInvData.setInvItmNo(
						laProcsOrigInvData.getInvItmNo());
				}
				if (laProcsOrigInvData.getInvItmEndNo() == null)
				{
					laProcsNewInvData.setInvItmEndNo(
						laProcsOrigInvData.getInvItmNo());
				}
				else if (
					!laProcsOrigInvData.getInvItmEndNo().equals(
						laProcsNewInvData.getInvItmEndNo()))
				{
					laProcsNewInvData.setInvItmEndNo(
						laProcsOrigInvData.getInvItmEndNo());
				}

				// set the quantity
				if (laProcsOrigInvData.getInvQty()
					!= laProcsNewInvData.getInvQty())
				{
					laProcsNewInvData.setInvQty(
						laProcsOrigInvData.getInvQty());
				}

				// put it on hold if issuing
				if (!laProcsNewInvData.isAlreadyIssued())
				{
					// call update to lock this range
					InventoryAllocation lInventoryAllocation =
						new InventoryAllocation(laDBA);
					lInventoryAllocation
						.updInventoryAllocationForLogicalLock(
						laProcsNewInvData.convertToInvAlloctnUIData(
							laProcsNewInvData));
					//lProcsNewInvData.setInvQty(1);
					laProcsNewInvData.setInvStatusCd(
						InventoryConstant.HOLD_INV_SYSTEM);
					// put it on hold right away
					updInvStatusCd(laDBA, laProcsNewInvData);
				}
			}
			laDBA.endTransaction(DatabaseAccess.COMMIT);

			return lvRtrnData;
			// defect 9116
		}
		catch (NullPointerException aeNPEx)
		{
			// if we get a null pointer, make sure we do a rollback
			throw new RTSException(RTSException.SERVER_DOWN, aeNPEx);
		}
		catch (RTSException aeRTSEx)
		{
			// end defect 9116
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			// defect 8696
			laDBA = null;
			// end defect 8696
			Log.write(
				Log.METHOD,
				this,
				MSG_CHKFORINVITMTOISSUE_END + csClientHost);
		}
	}

	/**
	 * Performs a select on the RTS_INV_ALLOCATION table and returns the 
	 * personalized plate if it exists.
	 *
	 * @param aaProcsInvData ProcessInventoryData The search parameters 
	 * 		  in the form of ProcessInventoryData.
	 * @param aaDBA DatabaseAccess The database connection
	 * @return Vector
	 * @exception RTSException  
	 */
	private Vector chkForPLP(
		ProcessInventoryData aaProcsInvData,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_CHKFORPLP_BEGIN + csClientHost);

		Vector lvPID = new Vector();
		try
		{
			InventoryAllocation laInvAlloctnSQL =
				new InventoryAllocation(aaDBA);

			lvPID =
				laInvAlloctnSQL.qryInventoryAllocationForSpecificItem(
					aaProcsInvData);

			// Convert from InventoryAllocationData object to 
			// ProcessInventoryData object
			if (lvPID.size() > CommonConstant.ELEMENT_0)
			{
				InventoryAllocationData laIAD =
					(InventoryAllocationData) lvPID.get(0);
				ProcessInventoryData laPID = new ProcessInventoryData();
				laPID = laPID.convertFromInvAlloctnData(laIAD);
				lvPID.removeAllElements();
				lvPID.addElement(laPID);
			}

			return lvPID;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_CHKFORPLP_END + csClientHost);
		}

	}

	/**
	 * Used to break apart inventory ranges in the RTS_INV_VIRTUAL 
	 * table.
	 * <p>Example: If the range in the database is 3WD to 19WD  
	 * and the user wants to use the range 5WD 
	 * to 10WD, this method will update the original 
	 * range to 3WD to 4WD, insert a second range 5WD to 10WD, and 
	 * insert a third range 11WD to 19WD.
	 *
	 * @param aaInvAllocDataOrig ProcessInventoryData The original 
	 * 		  inventory range entered by the user.
	 * @param avInvAllocData Vector A vector of InventoryAllocationData 
	 * 		  objects that represent the rows in the RTS_INV_VIRTUAL 
	 * 		  table that intersect the user specified range and 
	 *        subsequently need to be broken up.
	 * @param aaDBA DatabaseAccess The database connection
	 * @throws RTSException  
	 */

	private void chkForVICompleteRngeAndSplt(
		InventoryAllocationData aaInvAllocDataOrig,
		Vector avInvAllocData,
		DatabaseAccess aaDBA)
		throws RTSException
	{

		Log.write(
			Log.METHOD,
			this,
			MSG_CHKFROMCOMPLETERNGEANDSPLT_BEGIN + csClientHost);

		InventoryAllocationData laUpdateInvAllocData =
			new InventoryAllocationData();
		InventoryAllocationData laInsertInvAllocData1 =
			new InventoryAllocationData();
		InventoryAllocationData laInsertInvAllocData2 =
			new InventoryAllocationData();
		ValidateInventoryPattern laValidateInvPatrn =
			new ValidateInventoryPattern();
		boolean lbInvalid = false;

		// Determine if the complete range exists
		if (!((InventoryAllocationData) avInvAllocData
			.get(CommonConstant.ELEMENT_0))
			.getRangeCd()
			.equals(InventoryConstant.RANGE_OUTSIDE))
		{
			lbInvalid = false;
			int liBeginWithin = -1;
			int liBeginOutside = -1;
			int liBeginEnd = -1;
			int liEndWithin = -1;
			int liEndOutside = -1;
			int liLeft = -1;
			int liRight = -1;
			for (int i = 0; i < avInvAllocData.size(); i++)
			{
				InventoryAllocationData laInvAllocData =
					(InventoryAllocationData) avInvAllocData.get(i);
				if (laInvAllocData
					.getRangeCd()
					.equals(InventoryConstant.RANGE_BEGIN_END))
				{
					liBeginEnd = i;
				}
				else if (
					laInvAllocData.getRangeCd().equals(
						InventoryConstant.RANGE_BEGIN_WITHIN))
				{
					liBeginWithin = i;
				}
				else if (
					laInvAllocData.getRangeCd().equals(
						InventoryConstant.RANGE_BEGIN_OUTSIDE))
				{
					liBeginOutside = i;
				}
				else if (
					laInvAllocData.getRangeCd().equals(
						InventoryConstant.RANGE_WITHIN_END))
				{
					liEndWithin = i;
				}
				else if (
					laInvAllocData.getRangeCd().equals(
						InventoryConstant.RANGE_END_OUTSIDE))
				{
					liEndOutside = i;
				}
				else if (
					laInvAllocData.getRangeCd().equals(
						InventoryConstant.RANGE_LEFT))
				{
					liLeft = i;
				}
				else if (
					laInvAllocData.getRangeCd().equals(
						InventoryConstant.RANGE_RIGHT))
				{
					liRight = i;
				}
			}
			if ((liBeginEnd >= 0)
				|| (liBeginOutside >= 0 || liEndOutside >= 0)
				|| (liBeginWithin >= 0 && liEndWithin >= 0)
				|| (liBeginWithin >= 0 && liRight >= 0)
				|| (liLeft >= 0 && liEndWithin >= 0)
				|| (liLeft >= 0 && liRight >= 0))
			{
				// Determine if the sequence is contiquous
				for (int i = 0; i + 1 < avInvAllocData.size(); i++)
				{
					InventoryAllocationData laInvAllocData =
						(InventoryAllocationData) avInvAllocData.get(i);
					InventoryAllocationData laInvAllocData2 =
						(InventoryAllocationData) avInvAllocData.get(
							i + 1);
					if (laInvAllocData2.getPatrnSeqNo()
						!= laInvAllocData.getPatrnSeqNo()
							+ laInvAllocData.getInvQty())
					{
						lbInvalid = true;
						break;
					}
				}
			}
			else
			{
				lbInvalid = true;
			}

			// If the sequence is not contiquous, throw error 605
			if (lbInvalid)
			{
				RTSException leRTSException = new RTSException(605);
				throw leRTSException;
			}
		}

		// Split row or rows based on which RangeCd is returned
		for (int i = 0; i < avInvAllocData.size(); i++)
		{
			InventoryAllocationData laInvallocData =
				(InventoryAllocationData) avInvAllocData.get(i);

			if (laInvallocData
				.getRangeCd()
				.equals(InventoryConstant.RANGE_BEGIN_END)
				|| laInvallocData.getRangeCd().equals(
					InventoryConstant.RANGE_BEGIN_WITHIN)
				|| laInvallocData.getRangeCd().equals(
					InventoryConstant.RANGE_WITHIN_END)
				|| laInvallocData.getRangeCd().equals(
					InventoryConstant.RANGE_WITHIN))
			{
				continue;
			}
			else if (
				laInvallocData.getRangeCd().equals(
					InventoryConstant.RANGE_BEGIN_OUTSIDE)
					|| laInvallocData.getRangeCd().equals(
						InventoryConstant.RANGE_RIGHT))
			{

				// calculate the new beg no for the right using the 
				// orig end number and a qty of 2
				InventoryAllocationData laIAData =
					(InventoryAllocationData) UtilityMethods.copy(
						laInvallocData);
				laIAData.setInvItmNo(
					aaInvAllocDataOrig.getInvItmEndNo());
				laIAData.setInvQty(InventoryConstant.COMPUTE_NEXT_ITEM);
				ComputeInventoryData laCID =
					laValidateInvPatrn.validateItmNoInput(laIAData);

				laIAData =
					(
						InventoryAllocationData) laValidateInvPatrn
							.calcInvEndNo(
						laCID);

				// Prepare update data (left)
				laUpdateInvAllocData =
					(InventoryAllocationData) UtilityMethods.copy(
						laInvallocData);
				// end no should equal begin no
				// this is not quite right!!
				laUpdateInvAllocData.setInvItmEndNo(
					aaInvAllocDataOrig.getInvItmNo());
				laUpdateInvAllocData.setInvQty(
					aaInvAllocDataOrig.getEndPatrnSeqNo()
						- laInvallocData.getPatrnSeqNo()
						+ InventoryConstant.COMPUTE_CURRENT_ITEM);

				// Prepare insert data (right)
				laInsertInvAllocData1 =
					(InventoryAllocationData) UtilityMethods.copy(
						laInvallocData);
				laInsertInvAllocData1.setInvItmNo(
					laIAData.getInvItmEndNo());
				laInsertInvAllocData1.setPatrnSeqNo(
					laInvallocData.getPatrnSeqNo()
						+ laUpdateInvAllocData.getInvQty());
				laInsertInvAllocData1.setInvQty(
					laInvallocData.getInvQty()
						- laUpdateInvAllocData.getInvQty());

				// Update the existing inventory row and insert the 
				// new row
				Vector lvTmp = new Vector(CommonConstant.ELEMENT_2);
				lvTmp.add(laUpdateInvAllocData);
				lvTmp.add(laInsertInvAllocData1);
				performUpdateInsertForVI(lvTmp, aaDBA);

				continue;
			}
			else if (
				laInvallocData.getRangeCd().equals(
					InventoryConstant.RANGE_END_OUTSIDE)
					|| laInvallocData.getRangeCd().equals(
						InventoryConstant.RANGE_LEFT))
			{

				// calculate the new end no for the left using the 
				// orig end number and a qty of -1
				InventoryAllocationData laIAData =
					(InventoryAllocationData) UtilityMethods.copy(
						laInvallocData);
				laIAData.setInvItmNo(aaInvAllocDataOrig.getInvItmNo());
				laIAData.setInvQty(
					InventoryConstant.COMPUTE_PREVIOUS_ITEM);
				ComputeInventoryData laCID =
					laValidateInvPatrn.validateItmNoInput(laIAData);

				laIAData =
					(
						InventoryAllocationData) laValidateInvPatrn
							.calcInvEndNo(
						laCID);

				// Prepare update data (left)
				laUpdateInvAllocData =
					(InventoryAllocationData) UtilityMethods.copy(
						laInvallocData);
				laUpdateInvAllocData.setInvItmEndNo(
					laIAData.getInvItmEndNo());
				laUpdateInvAllocData.setInvQty(
					aaInvAllocDataOrig.getPatrnSeqNo()
						- laInvallocData.getPatrnSeqNo());

				// Prepare insert data (right)
				laInsertInvAllocData1 =
					(InventoryAllocationData) UtilityMethods.copy(
						laInvallocData);
				laInsertInvAllocData1.setInvItmNo(
					aaInvAllocDataOrig.getInvItmNo());
				laInsertInvAllocData1.setPatrnSeqNo(
					laInvallocData.getPatrnSeqNo()
						+ laUpdateInvAllocData.getInvQty());
				laInsertInvAllocData1.setInvQty(
					laInvallocData.getInvQty()
						- laUpdateInvAllocData.getInvQty());

				// Update the existing inventory row and insert 
				// the new row
				Vector lvTmp = new Vector(CommonConstant.ELEMENT_2);
				lvTmp.add(laUpdateInvAllocData);
				lvTmp.add(laInsertInvAllocData1);
				performUpdateInsertForVI(lvTmp, aaDBA);

				continue;
			}
			else if (
				laInvallocData.getRangeCd().equals(
					InventoryConstant.RANGE_OUTSIDE))
			{
				// calculate the new end no for the left using the 
				// orig end number and a qty of -1
				InventoryAllocationData laIAData =
					(InventoryAllocationData) UtilityMethods.copy(
						laInvallocData);
				laIAData.setInvItmNo(aaInvAllocDataOrig.getInvItmNo());
				laIAData.setInvQty(
					InventoryConstant.COMPUTE_PREVIOUS_ITEM);
				ComputeInventoryData laCID =
					laValidateInvPatrn.validateItmNoInput(laIAData);

				laIAData =
					(
						InventoryAllocationData) laValidateInvPatrn
							.calcInvEndNo(
						laCID);

				// Prepare update data (left)
				laUpdateInvAllocData =
					(InventoryAllocationData) UtilityMethods.copy(
						laInvallocData);
				laUpdateInvAllocData.setInvItmEndNo(
					laIAData.getInvItmEndNo());
				laUpdateInvAllocData.setInvQty(
					aaInvAllocDataOrig.getPatrnSeqNo()
						- laInvallocData.getPatrnSeqNo());

				// Prepare insert data
				InventoryAllocationData laTmpInvAllocData =
					new InventoryAllocationData();
				laTmpInvAllocData =
					(InventoryAllocationData) UtilityMethods.copy(
						laInvallocData);
				laTmpInvAllocData.setInvItmNo(
					aaInvAllocDataOrig.getInvItmNo());
				laTmpInvAllocData.setPatrnSeqNo(
					laInvallocData.getPatrnSeqNo()
						+ laUpdateInvAllocData.getInvQty());
				laTmpInvAllocData.setInvQty(
					laInvallocData.getInvQty()
						- laUpdateInvAllocData.getInvQty());

				// calculate the new beg no for the right using 
				// the orig end number and a qty of 2
				laIAData =
					(InventoryAllocationData) UtilityMethods.copy(
						laTmpInvAllocData);
				laIAData.setInvItmNo(
					aaInvAllocDataOrig.getInvItmEndNo());
				laIAData.setInvQty(InventoryConstant.COMPUTE_NEXT_ITEM);
				laCID = laValidateInvPatrn.validateItmNoInput(laIAData);

				laIAData =
					(
						InventoryAllocationData) laValidateInvPatrn
							.calcInvEndNo(
						laCID);

				// Prepare insert1 data (middle)
				laInsertInvAllocData1 =
					(InventoryAllocationData) UtilityMethods.copy(
						laTmpInvAllocData);
				laInsertInvAllocData1.setInvItmEndNo(
					aaInvAllocDataOrig.getInvItmEndNo());
				laInsertInvAllocData1.setInvQty(
					aaInvAllocDataOrig.getEndPatrnSeqNo()
						- laTmpInvAllocData.getPatrnSeqNo()
						+ InventoryConstant.COMPUTE_CURRENT_ITEM);

				// Prepare insert2 data (right)
				laInsertInvAllocData2 =
					(InventoryAllocationData) UtilityMethods.copy(
						laTmpInvAllocData);
				laInsertInvAllocData2.setInvItmNo(
					laIAData.getInvItmEndNo());
				laInsertInvAllocData2.setPatrnSeqNo(
					laTmpInvAllocData.getPatrnSeqNo()
						+ laInsertInvAllocData1.getInvQty());
				laInsertInvAllocData2.setInvQty(
					laTmpInvAllocData.getInvQty()
						- laInsertInvAllocData1.getInvQty());

				// Update the existing inventory row and insert the 
				// new row
				Vector lvTmp = new Vector(CommonConstant.ELEMENT_3);
				lvTmp.add(laUpdateInvAllocData);
				lvTmp.add(laInsertInvAllocData1);
				lvTmp.add(laInsertInvAllocData2);
				performUpdateInsertForVI(lvTmp, aaDBA);

				continue;
			}
		}

		Log.write(
			Log.METHOD,
			this,
			MSG_CHKFORCOMPLETERNGEANDSPLT_END + csClientHost);
	}

	/**
	 * Queries the RTS_INV_ALLOCATION table and returns all rows that 
	 * intersect the given inventory range.
	 *
	 * @param aaProcsInvData ProcessInventoryData Contains the inventory 
	 * 		  range.
	 * @param aaDBA DatabaseAccess The database connection
	 * @return Vector
	 * @throws RTSException  
	 */
	private Vector chkInvItmRange(
		ProcessInventoryData aaProcsInvData,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_CHKINVITMRANGE_BEGIN + csClientHost);

		Vector lvPID = new Vector();
		try
		{
			ProcessInventorySQL laDbInvSQL =
				new ProcessInventorySQL(aaDBA);

			lvPID = laDbInvSQL.qryInventoryRange(aaProcsInvData);

			return lvPID;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_CHKINVITMRANGE_END + csClientHost);
		}
	}

	/**
	 * Returns the NextAvailIndi which corresponds to whether or not the 
	 * RTS system should 'prompt' the user with the next available 
	 * inventory item allocated to them or the workstation.
	 *  
	 * <p>The default is to 'prompt'.  To turn off 'prompt'ing, you 
	 * must set up a profile to do this.
	 * 
	 * <p>Parameter aaData is of type ProcessInventoryData.
	 * The required fields for search are:
	 * <ul>
	 * <li>OfcIssuanceNo - The current office issuance number
	 * <li>SubstaId - The current substation id
	 * <li>ItmCd - The item code for the inventory
	 * <li>InvItmYr - The inventory year
	 * <li>TransWsId - The current workstation id
	 * <li>TransEmpId - The current user id
	 * <eul>
	 * 
	 * @param aaData Object
	 * @param aaDBA DatabaseAccess The database connection
	 * @return Integer
	 * @throws RTSException 
	 */
	private Integer chkNextAvailIndi(
		Object aaData,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_CHKNEXTAVAILINDI_BEGIN + csClientHost);

		ProcessInventoryData laProcsInvData =
			(ProcessInventoryData) aaData;
		InventoryProfileData laInvProfileData =
			new InventoryProfileData();
		Vector lvInvProfileReturn = new Vector();

		try
		{
			laInvProfileData.setOfcIssuanceNo(
				laProcsInvData.getOfcIssuanceNo());
			laInvProfileData.setSubstaId(laProcsInvData.getSubstaId());
			laInvProfileData.setItmCd(laProcsInvData.getItmCd());
			laInvProfileData.setInvItmYr(laProcsInvData.getInvItmYr());

			// defect 9116
			laInvProfileData.setTransWsId(
				String.valueOf(laProcsInvData.getTransWsId()));
			// end defect 9116

			laInvProfileData.setTransEmpId(
				laProcsInvData.getTransEmpId());

			InventoryProfile laInvProfileSQL =
				new InventoryProfile(aaDBA);
			lvInvProfileReturn =
				laInvProfileSQL.qryInventoryProfileNextAvailIndi(
					laInvProfileData);

			if (lvInvProfileReturn.size() == CommonConstant.ELEMENT_0)
			{
				return new Integer(1);
			}
			else
			{
				InventoryProfileData laInvData =
					(InventoryProfileData) lvInvProfileReturn.get(0);
				return new Integer(laInvData.getNextAvailIndi());
			}
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_CHKNEXTAVAILINDI_END + csClientHost);
		}
	}

	/**
	 * Performs a select on the RTS_INV_VIRTUAL table and returns the 
	 * personalized plate if it exists.
	 *
	 * @param aaInvAllocData InventoryAllocationData The search parameters 
	 * 		  in the form of InventoryAllocationData.
	 * @param aaDBA DatabaseAccess The database connection
	 * @return Vector
	 * @exception RTSException  
	 */
	private Vector chkVirtualInvForSpecificItem(
		InventoryAllocationData aaInvAllocData,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_CHKFORPLP_BEGIN + csClientHost);

		Vector lvVIdata = new Vector();
		try
		{
			InventoryVirtual laInvVirtualSQL =
				new InventoryVirtual(aaDBA);

			lvVIdata =
				laInvVirtualSQL.qryInventoryForSpecificItem(
					aaInvAllocData);

			if (lvVIdata.size() > CommonConstant.ELEMENT_0)
			{
				InventoryAllocationData laIAD =
					(InventoryAllocationData) lvVIdata.get(0);

				lvVIdata.removeAllElements();
				lvVIdata.addElement(laIAD);
			}

			return lvVIdata;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_CHKFORPLP_END + csClientHost);
		}
	}

	/**
	 * Queries the RTS_INV_VIRTUAL table and returns all rows that 
	 * intersect the given VI range.
	 *
	 * @param aaInvAllocData InventoryAllocationData Contains the VI 
	 * 		  range.
	 * @param aaDBA DatabaseAccess The database connection
	 * @return Vector
	 * @throws RTSException  
	 */
	private Vector chkVirtualInvItmRange(
		InventoryAllocationData aaInvAllocData,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"chkVirtualInvItmRange - Begin " + csClientHost);

		Vector lvVirtualInvData = new Vector();
		try
		{
			InventoryVirtual laIVSQL = new InventoryVirtual(aaDBA);

			lvVirtualInvData =
				laIVSQL.qryInventoryRange(aaInvAllocData, csClientHost);

			return lvVirtualInvData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				"chkVirtualInvItmRange - End " + csClientHost);
		}
	}

	/**
	 * Used by the issue inventory process to delete inventory items 
	 * from the RTS_INV_ALLOCATION table.
	 * 
	 * <p>Parameter aaData is a vector where the elements have to 
	 * be as follows:
	 * <ul>
	 * <li>Element(0) - The database connection.
	 * <li>Element(1) - The inventory that needs to be deleted in a 
	 * 						vector as ProcessInventoryData objects.
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Vector
	 * @throws RTSException 
	 */
	private Object delForIssueInv(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_DELFORISSUEINV_BEGIN + csClientHost);

		Vector lvInput = (Vector) aaData;
		ProcessInventoryData laProcsInvDataOrig =
			(ProcessInventoryData) lvInput.get(
				CommonConstant.ELEMENT_1);
		DatabaseAccess laDBA =
			(DatabaseAccess) lvInput.get(CommonConstant.ELEMENT_0);

		// defect 10769 	
		boolean lbVerifyInsOwner =
			laProcsInvDataOrig.isVerifyInvOwner();
		String lsInvLocIdCd = laProcsInvDataOrig.getInvLocIdCd();
		String lsInvId = laProcsInvDataOrig.getInvId();
		// end defect 10769 

		ProcessInventoryData laUpdateProcsInvData =
			new ProcessInventoryData();
		Vector lvProcsInvData = new Vector();

		// Validate and set up inventory information and then make call 
		// to find the item in the Inventory Allocation table
		if (!ValidateInventoryPattern
			.chkIfItmPLPOrOLDPLTOrROP(laProcsInvDataOrig.getItmCd()))
		{
			laProcsInvDataOrig.setPLPFlag(false);

			// Validate the begin inventory item number and calculate 
			// the PatrnSeqNo
			InventoryAllocationUIData laIAUID =
				laProcsInvDataOrig.convertToInvAlloctnUIData(
					laProcsInvDataOrig);

			//see if calculation is needed.
			if (!laIAUID.isCalcInv())
			{
				ValidateInventoryPattern laVID =
					new ValidateInventoryPattern();
				laIAUID =
					(InventoryAllocationUIData) laVID.calcInvUnknown(
						laIAUID);
				laProcsInvDataOrig =
					laProcsInvDataOrig.convertFromInvAlloctnData(
						laIAUID);

				// Calculate the EndPatrnSeqNo

				// if qty is one, just use the begin patrnseqno.
				// otherwise calculate.
				if (laProcsInvDataOrig.getInvQty() != 1)
				{
					laIAUID.setItmCd(laProcsInvDataOrig.getItmCd());
					laIAUID.setInvItmYr(
						laProcsInvDataOrig.getInvItmYr());
					laIAUID.setInvQty(laProcsInvDataOrig.getInvQty());
					laIAUID.setInvItmNo(
						laProcsInvDataOrig.getInvItmEndNo());
					laIAUID.setInvItmEndNo(
						laProcsInvDataOrig.getInvItmEndNo());

					laIAUID =
						(
							InventoryAllocationUIData) laVID
								.calcInvUnknown(
							laIAUID);
					laProcsInvDataOrig.setEndPatrnSeqNo(
						laIAUID.getPatrnSeqNo());

					// Verify begin and end numbers have the same 
					// PatrnSeqCd
					if (laProcsInvDataOrig.getPatrnSeqCd()
						!= laIAUID.getPatrnSeqCd())
					{
						RTSException leRTSException =
							new RTSException(607);
						throw leRTSException;
					}

				}
				else
				{
					laProcsInvDataOrig.setEndPatrnSeqNo(
						laProcsInvDataOrig.getPatrnSeqNo());
					laProcsInvDataOrig.setInvItmEndNo(
						laProcsInvDataOrig.getInvItmNo());
				}

			}

			laProcsInvDataOrig.setInvId(null);

			lvProcsInvData = chkInvItmRange(laProcsInvDataOrig, laDBA);
		}
		else
		{
			laProcsInvDataOrig.setInvQty(1);
			laProcsInvDataOrig.setPatrnSeqCd(-1);
			laProcsInvDataOrig.setEndPatrnSeqNo(Integer.MIN_VALUE);
			laProcsInvDataOrig.setPatrnSeqNo(Integer.MIN_VALUE);
			laProcsInvDataOrig.setPLPFlag(true);

			lvProcsInvData = chkForPLP(laProcsInvDataOrig, laDBA);
		}

		// Throw error 182 if ranges do not exist and the database is up
		if (lvProcsInvData.size() <= CommonConstant.ELEMENT_0)
		{
			RTSException leRTSException = new RTSException(182);
			throw leRTSException;
		}

		// Check for complete range and then split
		if (!laProcsInvDataOrig.isPLPFlag())
		{
			chkForCompleteRngeAndSplt(
				laProcsInvDataOrig,
				lvProcsInvData,
				laDBA);
		}

		// Reselect ranges to delete
		if (!laProcsInvDataOrig.isPLPFlag())
		{
			lvProcsInvData = chkInvItmRange(laProcsInvDataOrig, laDBA);
		}
		else
		{
			lvProcsInvData = chkForPLP(laProcsInvDataOrig, laDBA);
		}

		// Delete all rows for the given range
		for (int i = 0; i < lvProcsInvData.size(); i++)
		{
			laUpdateProcsInvData =
				(ProcessInventoryData) lvProcsInvData.get(i);

			if (laProcsInvDataOrig.getInvLocIdCd() == null)
			{
				laProcsInvDataOrig.setInvLocIdCd(
					laUpdateProcsInvData.getInvLocIdCd());
			}
			// defect 10769
			if (lbVerifyInsOwner
				&& lsInvId != null
				&& lsInvLocIdCd != null)
			{
				if (!(lsInvId.equals(laUpdateProcsInvData.getInvId())
					&& lsInvLocIdCd.equals(
						laUpdateProcsInvData.getInvLocIdCd())))
				{
					throw new RTSException(612);
				}
			}
			// end defect 10769 

			InventoryAllocation laInvAlloctnSQL =
				new InventoryAllocation(laDBA);
			laInvAlloctnSQL.delInventoryAllocation(
				laUpdateProcsInvData);
		}

		Log.write(
			Log.METHOD,
			this,
			MSG_DELFORISSUEINV_END + csClientHost);

		return lvProcsInvData;
	}

	/**
	 * Used to delete inventory items from the RTS_INV_ALLOCATION table.
	 *
	 * <p>Parameter aaData is a vector where the elements have to be as 
	 * follows:
	 * <ul>
	 * <li>Element(0) - The inventory that needs to be deleted in a 
	 * 						vector as InventoryAllocationUIData objects.
	 * <li>Element(1) - Not required but contains the transaction 
	 * 						header for when the transaction needs to be 
	 * 						written.
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Vector
	 * @throws RTSException  
	 */
	private Object delInvItm(Object aaData) throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_DELINVITM_BEGIN + csClientHost);

		Vector lvInput = (Vector) aaData;
		Vector lvInvAlloctnUIData = (Vector) lvInput.get(0);
		TransactionHeaderData laTransactionHeaderData =
			new TransactionHeaderData();
		if (lvInput.size() > CommonConstant.ELEMENT_0)
		{
			laTransactionHeaderData =
				(TransactionHeaderData) lvInput.get(
					CommonConstant.ELEMENT_1);
		}
		int liLoopIndx = 0;
		//Used when exception 605 is thrown to determine what the del 
		// reasn cd and text are

		ProcessInventoryData laProcsInvDataOrig =
			new ProcessInventoryData();
		ProcessInventoryData laUpdateProcsInvData =
			new ProcessInventoryData();
		Vector lvProcsInvData = new Vector();
		DatabaseAccess laDBA = new DatabaseAccess();
		InventoryFunctionTransactionData laInvFuncTransData =
			new InventoryFunctionTransactionData();
		TransactionInventoryDetailData laTransInvDetailData =
			new TransactionInventoryDetailData();

		try
		{
			laDBA.beginTransaction();

			for (int j = 0; j < lvInvAlloctnUIData.size(); j++)
			{
				liLoopIndx = j;

				InventoryAllocationUIData laInvAlloctnUIData =
					(InventoryAllocationUIData) lvInvAlloctnUIData.get(
						j);

				laProcsInvDataOrig =
					laProcsInvDataOrig.convertFromInvAlloctnData(
						laInvAlloctnUIData);

				// Validate and set up inventory information and then 
				// make call to Inventory Allocation table
				if (!ValidateInventoryPattern
					.chkIfItmPLPOrOLDPLTOrROP(
						laInvAlloctnUIData.getItmCd()))
				{
					laProcsInvDataOrig.setPLPFlag(false);

					// Validate the begin inventory item number and 
					// calculate the PatrnSeqNo
					InventoryAllocationUIData laIAUID =
						laProcsInvDataOrig.convertToInvAlloctnUIData(
							laProcsInvDataOrig);

					ValidateInventoryPattern laVIP =
						new ValidateInventoryPattern();
					laIAUID =
						(
							InventoryAllocationUIData) laVIP
								.calcInvUnknown(
							laIAUID);

					laProcsInvDataOrig =
						laProcsInvDataOrig.convertFromInvAlloctnData(
							laIAUID);

					// Calculate the EndPatrnSeqNo
					laIAUID.setItmCd(laInvAlloctnUIData.getItmCd());
					laIAUID.setInvItmYr(
						laInvAlloctnUIData.getInvItmYr());

					// should only set the qty to 1 to compute the seqno
					laIAUID.setInvQty((new Integer(1)).intValue());
					laIAUID.setInvItmNo(
						laProcsInvDataOrig.getInvItmEndNo());
					laIAUID.setInvItmEndNo(
						laProcsInvDataOrig.getInvItmEndNo());

					laIAUID =
						(
							InventoryAllocationUIData) laVIP
								.calcInvUnknown(
							laIAUID);

					laProcsInvDataOrig.setEndPatrnSeqNo(
						laIAUID.getPatrnSeqNo());
					laProcsInvDataOrig.setInvId(null);

					// Verify begin and end numbers have the same 
					// PatrnSeqCd
					if (laProcsInvDataOrig.getPatrnSeqCd()
						!= laIAUID.getPatrnSeqCd())
					{
						RTSException leRTSException =
							new RTSException(607);
						throw leRTSException;
					}

					lvProcsInvData =
						chkInvItmRange(laProcsInvDataOrig, laDBA);
				}
				else
				{
					laProcsInvDataOrig.setPatrnSeqCd(-1);
					laProcsInvDataOrig.setEndPatrnSeqNo(
						Integer.MIN_VALUE);
					laProcsInvDataOrig.setPatrnSeqNo(Integer.MIN_VALUE);
					laProcsInvDataOrig.setPLPFlag(true);

					lvProcsInvData =
						chkForPLP(laProcsInvDataOrig, laDBA);
				}

				//Put rows on hold
				InventoryAllocation laInventoryAllocation =
					new InventoryAllocation(laDBA);
				laInventoryAllocation
					.updInventoryAllocationForLogicalLock(
					laProcsInvDataOrig.convertToInvAlloctnUIData(
						laProcsInvDataOrig));

				// Throw error 182 if ranges do not exist and the 
				// database is up
				if (lvProcsInvData.size() <= CommonConstant.ELEMENT_0)
				{
					RTSException leRTSException = new RTSException(182);
					throw leRTSException;
				}

				// Test if any of the selected rows are currently on 
				// hold
				for (int i = 0; i < lvProcsInvData.size(); i++)
				{
					if (((ProcessInventoryData) lvProcsInvData.get(i))
						.getInvStatusCd()
						== InventoryConstant.HOLD_INV_USER)
					{
						RTSException leRTSException =
							new RTSException(594);
						throw leRTSException;
					}
					else if (
						((ProcessInventoryData) lvProcsInvData.get(i))
							.getInvStatusCd()
							== InventoryConstant.HOLD_INV_SYSTEM)
					{
						RTSException leRTSException =
							new RTSException(593);
						throw leRTSException;
					}
				}

				// Check for complete range and then split
				if (!laProcsInvDataOrig.isPLPFlag())
				{
					chkForCompleteRngeAndSplt(
						laProcsInvDataOrig,
						lvProcsInvData,
						laDBA);
				}

				// Reselect ranges to delete
				if (!laProcsInvDataOrig.isPLPFlag())
				{
					lvProcsInvData =
						chkInvItmRange(laProcsInvDataOrig, laDBA);
				}
				else
				{
					lvProcsInvData =
						chkForPLP(laProcsInvDataOrig, laDBA);
				}

				// Delete all rows for the given range
				for (int i = 0; i < lvProcsInvData.size(); i++)
				{
					laUpdateProcsInvData =
						(ProcessInventoryData) lvProcsInvData.get(i);
					if (laProcsInvDataOrig.getInvLocIdCd() == null)
					{
						laProcsInvDataOrig.setInvLocIdCd(
							laUpdateProcsInvData.getInvLocIdCd());
					}

					InventoryAllocation laInvAlloctnSQL =
						new InventoryAllocation(laDBA);
					laInvAlloctnSQL.delInventoryAllocation(
						laUpdateProcsInvData);
				}

				// Code to wait for 1 second between writing each of the 
				// transactions.
				// Without this pause, there is a duplicate key error.
				if (liLoopIndx > 0)
				{
					Thread.sleep(SLEEP_1_SECOND);
				}

				// Prepare transaction data objects
				laInvFuncTransData.setTransCd(TransCdConstant.INVDEL);
				laInvFuncTransData.setInvcCorrIndi(0);
				laInvFuncTransData.setInvcNo(
					CommonConstant.STR_SPACE_EMPTY);
				//defect 9117
				laInvFuncTransData.setEmpId(
					laTransactionHeaderData.getTransEmpId());
				// end defect 9117
				laTransInvDetailData.setTransCd(TransCdConstant.INVDEL);
				laTransInvDetailData.setItmCd(
					laProcsInvDataOrig.getItmCd());
				laTransInvDetailData.setInvItmYr(
					laProcsInvDataOrig.getInvItmYr());
				laTransInvDetailData.setInvItmNo(
					laProcsInvDataOrig.getInvItmNo());
				laTransInvDetailData.setInvEndNo(
					laProcsInvDataOrig.getInvItmEndNo());
				laTransInvDetailData.setInvQty(
					laProcsInvDataOrig.getInvQty());
				laTransInvDetailData.setInvId(
					String.valueOf(laProcsInvDataOrig.getSubstaId()));
				laTransInvDetailData.setInvLocIdCd(
					laProcsInvDataOrig.getInvLocIdCd());
				if (laInvAlloctnUIData.getDelInvReasnCd() == 9)
				{
					laTransInvDetailData.setDetailStatusCd(0);
				}
				else
				{
					laTransInvDetailData.setDetailStatusCd(-1);
				}
				laTransInvDetailData.setDelInvReasnCd(
					laInvAlloctnUIData.getDelInvReasnCd());
				laTransInvDetailData.setDelInvReasnTxt(
					laInvAlloctnUIData.getDelInvReasnTxt());

				// Make call to common to finish and write trans data
				Vector lvTransInvDetailData = new Vector();
				lvTransInvDetailData.addElement(laTransInvDetailData);

				CompleteTransactionData laCompltTransData =
					new CompleteTransactionData();

				laCompltTransData.setInvFuncTrans(laInvFuncTransData);
				laCompltTransData.setTransInvDetail(
					lvTransInvDetailData);
				CommonServerBusiness laCommnServerBus =
					new CommonServerBusiness();

				Vector lvInputTrans = new Vector();
				lvInputTrans.addElement(laTransactionHeaderData);
				laCompltTransData.setTransCode(TransCdConstant.INVDEL);
				lvInputTrans.addElement(laCompltTransData);
				lvInputTrans.addElement(laDBA);
				laCompltTransData =
					(
						CompleteTransactionData) laCommnServerBus
							.processData(
						GeneralConstant.COMMON,
						CommonConstant.PROC_TRANS,
						lvInputTrans);

				//  Add this data to the History table
				addInvHistData(lvInputTrans);

				// Get the transid
				laInvAlloctnUIData.setTransId(
					laCompltTransData.getTransId());
			}

			laDBA.endTransaction(DatabaseAccess.COMMIT);

			Vector lvVct = new Vector();
			lvVct.add(new Integer(1));
			lvVct.add(lvInvAlloctnUIData);
			return lvVct;
		}
		catch (InterruptedException aeIEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeIEx);
			throw leRTSEx;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			// If the the delete range is not complete in the database, 
			// return the existing ranges and go to INV025.
			if (aeRTSEx.getCode() == 605)
			{
				Vector lvIAUID = new Vector(lvProcsInvData.size());
				for (int i = 0; i < lvProcsInvData.size(); i++)
				{
					ProcessInventoryData laPID =
						(ProcessInventoryData) lvProcsInvData.get(i);
					InventoryAllocationUIData laIAUID =
						laPID.convertToInvAlloctnUIData(laPID);
					InventoryAllocationUIData lOrigIAUID =
						(
							InventoryAllocationUIData) lvInvAlloctnUIData
								.get(
							liLoopIndx);
					laIAUID.setDelInvReasnCd(
						lOrigIAUID.getDelInvReasnCd());
					laIAUID.setDelInvReasnTxt(
						lOrigIAUID.getDelInvReasnTxt());
					lvIAUID.add(laIAUID);
				}
				Vector lvVct = new Vector();
				lvVct.add(new Integer(0));
				lvVct.add(lvIAUID);
				return lvVct;
			}
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_DELINVITM_END + csClientHost);
		}
	}

	/**
	 * Deletes an inventory profile from the database and adds a row to 
	 * the RTS_ADMIN_LOG table.
	 *
	 * <p>Parameter aaData is a vector where the elements have to be as 
	 * follows:
	 * <ul>
	 * <li>Element(0) - The inventory profile that needs to be 
	 * 						deleted as an InventoryProfileData object.
	 * <li>Element(1) - The new row for the RTS_ADMIN_LOG table in 
	 * 						the form of	AdministrationLogData.
	 * <eul>
	 * 
	 * @param aaData Vector
	 * @throws RTSException 
	 */
	private void delInvProfile(Vector aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_DELINVPROFILE_BEGIN + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			InventoryProfile laDbIP = new InventoryProfile(laDBA);
			laDbIP.delInventoryProfile(
				(InventoryProfileData) aaData.get(
					CommonConstant.ELEMENT_0));

			AdministrationLog laDbAL = new AdministrationLog(laDBA);
			laDbAL.insAdministrationLog(
				(AdministrationLogData) aaData.get(
					CommonConstant.ELEMENT_1));
			laDBA.endTransaction(DatabaseAccess.COMMIT);
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_DELINVPROFILE_END + csClientHost);
		}
	}

	/**
	 * THIS METHOD IS NOT COMPLETE!!
	 * Delete specified item from Virtual Inventory.
	 * 
	 * <p>This method assumes that the data provided is for a certain 
	 * row.
	 * 
	 * @param aaData Vector
	 * @throws RTSException
	 */
	private Object delInvVirtual(
		DatabaseAccess aaDBA,
		InventoryAllocationData aaInvDataOrig)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"delInvVirtual - Begin " + csClientHost);

		boolean lbDbaLocal = false;

		// defect 9404 
		try
		{

			// if this item is not calculated, do it now.
			if (!aaInvDataOrig.isCalcInv())
			{
				ValidateInventoryPattern laVIP =
					new ValidateInventoryPattern();
				aaInvDataOrig =
					(InventoryAllocationData) laVIP.calcInvUnknownVI(
						aaInvDataOrig);
			}

			if (aaDBA == null)
			{
				lbDbaLocal = true;
				aaDBA = new DatabaseAccess();
				aaDBA.beginTransaction();
				//logUOW("delInvVirtual", ": BEGIN");

			}

			// do the delete
			InventoryVirtual laIV = new InventoryVirtual(aaDBA);
			laIV.delInventoryVirtual(aaInvDataOrig);

			if (lbDbaLocal)
			{
				aaDBA.endTransaction(DatabaseAccess.COMMIT);
				//logUOW("delInvVirtual", ": COMMIT");
				aaDBA = null;
			}

			return aaInvDataOrig;
		}
		catch (RTSException aeRTSEx)
		{
			if (aaDBA != null)
			{
				aaDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("delInvVirtual", ": ROLLBACK");
				aaDBA = null;
			}
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				"delInvVirtual - End " + csClientHost);
		}
		// end defect 9404 
	}

	/**
	 * Sets up the data to generate the Inventory History Delete Report.
	 *
	 * <p>Parameter aaData is a vector where the elements have to be as 
	 * follows:
	 * <ul>
	 * <li>Element(0) - The InventoryHistoryUIData object.
	 * <li>Element(1) - ReportSearchData object with the following 
	 * 						fields:
	 * <ul>
	 * <li>IntKey1() - The office issuance number
	 * <li>IntKey2() - The substation id
	 * <li>IntKey3() - The workstation id
	 * <li>Key1() - The current user
	 * <eul>
	 * <eul>
	 * 
	 * <p>Returns the report in the form of ReportSearchData.
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException  
	 */
	public Object genDelInvHistoryRpt(Object aaData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GENDELINVHISTORYRPT_BEGIN + csClientHost);

		InventoryHistoryUIData laInvHisUIData =
			(InventoryHistoryUIData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_0);
		ReportSearchData laRptSearchData =
			(ReportSearchData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_1);
		// defect 8940 
		AdministrationLogData laAdminLogData =
			(AdministrationLogData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_2);
		// end defect 8940 
		DatabaseAccess laDBA = new DatabaseAccess();

		// UOW #1 BEGIN
		ReportProperties laRptProps =
			initReportProperties(
				laRptSearchData,
				laDBA,
				ReportConstant
					.RPT_3071_DELETE_INVENTORY_HISTORY_REPORT_ID);
		// UOW #1 END 

		// UOW #2 BEGIN 
		laDBA.beginTransaction();
		AdministrationLog laAdminlog = new AdministrationLog(laDBA);
		laAdminlog.insAdministrationLog(laAdminLogData);
		laDBA.endTransaction(DatabaseAccess.COMMIT);
		// UOW #2 END 

		GenDeleteInventoryHistoryReport laDIHR =
			new GenDeleteInventoryHistoryReport(
				ReportConstant
					.RPT_3071_DELETE_INVENTORY_HISTORY_REPORT_TITLE,
				laRptProps);

		InventoryReportsSQL laInvRptSQL =
			new InventoryReportsSQL(laDBA);

		//	UOW #3 BEGIN
		laDBA.beginTransaction();

		// defect 10207 
		// One SQL call for all OfcIssuanceNo 
		Vector lvQueryResults =
			laInvRptSQL.qryInventoryDeleteHistoryReport(laInvHisUIData);
		// end defect 10207 
		laDBA.endTransaction(DatabaseAccess.COMMIT);
		// UOW #3 END 

		// Use Vector to send laInvHisUIData and lvQueryResults
		// (Need date range, county name and query results) 					 
		Vector lvReportInputData = new Vector();
		lvReportInputData.addElement(laInvHisUIData);
		lvReportInputData.addElement(lvQueryResults);
		laDIHR.formatReport(lvReportInputData);

		Log.write(
			Log.METHOD,
			this,
			MSG_GENDELINVHISTORYRPT_END + csClientHost);

		return new ReportSearchData(
			laDIHR.getFormattedReport().toString(),
			ReportConstant
				.RPT_3071_DELETE_INVENTORY_HISTORY_REPORT_FILENAME,
			ReportConstant
				.RPT_3071_DELETE_INVENTORY_HISTORY_REPORT_TITLE,
			ReportConstant.RPT_7_COPIES,
			ReportConstant.PORTRAIT);
	}

	/**
	 * Sets up the data to generate the OnLine Inventory Action Report.
	 *
	 * <p>Parameter aaData is a ReportSearchData object with the 
	 * following fields:
	 * <ul>
	 * <li>IntKey1() - The office issuance number
	 * <li>IntKey2() - The substation id
	 * <li>IntKey3() - The workstation id
	 * <li>Key1() - The current user
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException  
	 */
	public Object genInvActionRpt(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GENINVACTIONRPT_BEGIN + csClientHost);

		ReportSearchData laRptSearchData = (ReportSearchData) aaData;

		// defect 8628 
		DatabaseAccess laDBA = new DatabaseAccess();

		// UOW #1 BEGIN
		ReportProperties laRptProps =
			initReportProperties(
				laRptSearchData,
				laDBA,
				ReportConstant.RPT_9901_IAR_REPORT_ID);
		// UOW #1 END 
		// end defect 8628 

		GenBatchInventoryActionReport laBIAR =
			new GenBatchInventoryActionReport(
				ReportConstant.RPT_9901_IAR_REPORT_TITLE,
				laRptProps);

		// Report Date
		RTSDate laReportDataDate =
			new RTSDate(
				RTSDate.AMDATE,
				Integer.parseInt(laRptSearchData.getKey2()));

		// Vector to pass objects 
		Vector lvReportData = new Vector();
		lvReportData.addElement(laReportDataDate.toString());
		Vector lvQueryResults = new Vector();

		InventoryActionReportSQL laInvRptSQL =
			new InventoryActionReportSQL(laDBA);
		// defect 8628 
		// UOW #2 BEGIN 
		laDBA.beginTransaction();
		// end defect 8628 
		//Executes Part A of Batch Inventory Action Report
		laRptSearchData.setKey1(InventoryConstant.NOT_FOUND);
		//InvLocIdCd
		lvQueryResults = laInvRptSQL.qryIARPartAB(laRptSearchData);
		lvReportData.addElement(lvQueryResults);
		laBIAR.partA(lvReportData);

		//Executes Part B 
		laRptSearchData.setKey1(InventoryConstant.VOID); //InvLocIdCd
		lvReportData.removeElementAt(CommonConstant.ELEMENT_1);
		lvQueryResults = laInvRptSQL.qryIARPartAB(laRptSearchData);
		lvReportData.addElement(lvQueryResults);
		laBIAR.partB(lvReportData);

		//Executes Part C
		lvReportData.removeElementAt(CommonConstant.ELEMENT_1);
		lvQueryResults = laInvRptSQL.qryIARPartC(laRptSearchData);
		lvReportData.addElement(lvQueryResults);
		laBIAR.partC(lvReportData);

		//Executes Part D
		laRptSearchData.setIntKey3(2); //status code - used in D 
		lvReportData.removeElementAt(CommonConstant.ELEMENT_1);
		lvReportData.addElement(null);
		laBIAR.partD(lvReportData);

		//Executes Part E
		lvReportData.removeElementAt(CommonConstant.ELEMENT_1);
		laRptSearchData.setIntKey3(1); //status code - used in  E
		lvQueryResults = laInvRptSQL.qryIARPartDE(laRptSearchData);
		lvReportData.addElement(lvQueryResults);
		laBIAR.partE(lvReportData);

		//Executes Part F
		lvReportData.removeElementAt(CommonConstant.ELEMENT_1);
		lvQueryResults = laInvRptSQL.qryIARPartF(laRptSearchData);
		lvReportData.addElement(lvQueryResults);
		laBIAR.partF(lvReportData);

		//Executes Part G
		lvReportData.removeElementAt(CommonConstant.ELEMENT_1);
		lvQueryResults = laInvRptSQL.qryIARPartG(laRptSearchData);
		lvReportData.addElement(lvQueryResults);
		laBIAR.partG(lvReportData);

		//Executes Part H
		lvReportData.removeElementAt(CommonConstant.ELEMENT_1);
		lvQueryResults = laInvRptSQL.qryIARPartH(laRptSearchData);
		lvReportData.addElement(lvQueryResults);
		laBIAR.partH(lvReportData);

		//Executes Part I
		lvReportData.removeElementAt(CommonConstant.ELEMENT_1);
		lvQueryResults = laInvRptSQL.qryIARPartI(laRptSearchData);
		lvReportData.addElement(lvQueryResults);
		laBIAR.partI(lvReportData);

		//Executes Part J
		lvReportData.removeElementAt(CommonConstant.ELEMENT_1);
		lvQueryResults =
			laInvRptSQL.qryIARPartJ(
				laRptSearchData,
				new RTSDate(
					RTSDate.AMDATE,
					Integer.parseInt(laRptSearchData.getKey2())));
		lvReportData.addElement(lvQueryResults);
		laBIAR.partJ(lvReportData);

		//Executes Part K
		lvReportData.removeElementAt(CommonConstant.ELEMENT_1);
		lvQueryResults =
			laInvRptSQL.qryIARPartK(
				laRptSearchData,
				new RTSDate(
					RTSDate.AMDATE,
					Integer.parseInt(laRptSearchData.getKey2())));
		lvReportData.addElement(lvQueryResults);
		laBIAR.partK(lvReportData);
		laDBA.endTransaction(DatabaseAccess.COMMIT);
		// UOW #2 END  

		// defect 8628 
		Log.write(
			Log.METHOD,
			this,
			MSG_GENINVACTIONRPT_END + csClientHost);

		return new ReportSearchData(
			laBIAR.getFormattedReport().toString(),
			ReportConstant.RPT_9901_IAR_FILENAME,
			ReportConstant.RPT_9901_IAR_REPORT_TITLE,
			ReportConstant.RPT_1_COPY,
			ReportConstant.PORTRAIT);
		// end defect 8628 
	}

	/**
	 * Sets up the data to generate the Inventory Inquiry Report.
	 *
	 * <p>Parameter aaData is a vector where the elements have to be 
	 * as follows:
	 * <ul>
	 * <li>Element(0) - The InventoryInquiryUIData object.
	 * <li>Element(1) - ReportSearchData object with the following 
	 * fields:
	 * <ul>
	 * <li>IntKey1() - The office issuance number
	 * <li>IntKey2() - The substation id
	 * <li>IntKey3() - The workstation id
	 * <li>Key1()    - The current user
	 * <eul>
	 * <eul>
	 * 
	 * <p>Returns report in the form of ReportSearchData.
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	public Object genInvInqRpt(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GENINVINQRPT_BEGIN + csClientHost);

		InventoryInquiryUIData laInvInqUIData =
			(InventoryInquiryUIData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_0);
		ReportSearchData laRptSearchData =
			(ReportSearchData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_1);
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvCurBalQueryResults = new Vector();
		Vector lvHistoryQueryResults = new Vector();
		Vector lvReport = new Vector();
		try
		{
			// defect 8628
			// defect 10260 
			// Inv Inquiry report can be run for a substation
			laRptSearchData.setIntKey2(laInvInqUIData.getSubstaId());
			// end defect 10260
			// UOW #1 BEGIN
			ReportProperties laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBA,
					ReportConstant
						.RPT_3031_INVENTORY_INQUIRY_REPORT_ID);
			// UOW #1 END 
			// end defect 8628

			// UOW #2 BEGIN 				  
			laDBA.beginTransaction();
			// defect 9117
			GenInventoryInquiryReport laGIIR = null;
			InventoryReportsSQL laInvRptSQL = null;
			InventoryVirtual laIVRptSQL = null;

			if (laInvInqUIData
				.getRptType()
				.equals(InventoryConstant.CUR_VIRTUAL))
			{
				//GenVirtualInventoryInquiryReport 
				laGIIR =
					new GenInventoryInquiryReport(
						ReportConstant
							.RPT_3031_VIRTUAL_INVENTORY_INQUIRY_REPORT_TITLE,
						laRptProps);
				laIVRptSQL = new InventoryVirtual(laDBA);
			}
			else
			{
				//GenInventoryInquiryReport 
				laGIIR =
					new GenInventoryInquiryReport(
						ReportConstant
							.RPT_3031_INVENTORY_INQUIRY_REPORT_TITLE,
						laRptProps);
				laInvRptSQL = new InventoryReportsSQL(laDBA);
			}

			//InventoryReportsSQL laInvRptSQL =
			//	new InventoryReportsSQL(laDBA);
			// end defect 9117
			// Set up for a specific item
			if (laInvInqUIData
				.getInvInqBy()
				.equals(InventoryConstant.SPECIFIC_ITM))
			{
				// "normal" specific item
				if (!ValidateInventoryPattern
					.chkIfItmPLPOrOLDPLTOrROP(
						laInvInqUIData.getItmCd()))
				{
					InventoryAllocationUIData laIAUID =
						new InventoryAllocationUIData();

					laIAUID.setOfcIssuanceNo(
						laInvInqUIData.getOfcIssuanceNo());
					laIAUID.setSubstaId(laInvInqUIData.getSubstaId());
					laIAUID.setItmCd(laInvInqUIData.getItmCd());
					laIAUID.setInvItmYr(laInvInqUIData.getInvItmYr());
					laIAUID.setInvItmNo(laInvInqUIData.getInvItmNo());
					laIAUID.setInvItmEndNo(
						laInvInqUIData.getInvItmEndNo());
					laIAUID.setInvQty(laInvInqUIData.getInvQty());

					ValidateInventoryPattern laVIP =
						new ValidateInventoryPattern();
					laIAUID =
						(
							InventoryAllocationUIData) laVIP
								.calcInvUnknown(
							laIAUID);

					// pass PatrnSeqNo and PatrnSeqCd to query
					laInvInqUIData.setPatrnSeqNo(
						laIAUID.getPatrnSeqNo());
					laInvInqUIData.setPatrnSeqCd(
						laIAUID.getPatrnSeqCd());
				}
				else
				{
					// just pass min value for certain plates
					laInvInqUIData.setPatrnSeqNo(Integer.MIN_VALUE);
					laInvInqUIData.setPatrnSeqCd(Integer.MIN_VALUE);
				}

				// Need to change single entry for item code and year 
				//into the vector form for the query.
				// add Item Code Vector to lInvInqUIData
				Vector lvTempItmCd = new Vector();
				lvTempItmCd.add(laInvInqUIData.getItmCd());
				laInvInqUIData.setItmCds(lvTempItmCd);
				// add Item Year to lInvInqUIData
				Vector lvTempItmYr = new Vector();
				lvTempItmYr.add(
					new Integer(laInvInqUIData.getInvItmYr()));
				laInvInqUIData.setInvItmYrs(lvTempItmYr);

			}

			// CUR_BAL_HISTORY runs both Current Balance and History.
			// The same is true for SPECIFIC_ITM.
			// The report processes the results of both queries.
			// Run query against INV_ALLOC table
			if (laInvInqUIData
				.getRptType()
				.equals(InventoryConstant.CUR_BAL)
				|| laInvInqUIData.getRptType().equals(
					InventoryConstant.CUR_BAL_HISTORY)
				|| laInvInqUIData.getInvInqBy().equals(
					InventoryConstant.SPECIFIC_ITM))
			{
				lvCurBalQueryResults =
					(
						Vector) laInvRptSQL
							.qryInventoryInquiryCurrentBalanceReport(
						laInvInqUIData);
			}

			// defect 9117
			// Run query against INV_VIRTUAL table
			if (laInvInqUIData
				.getRptType()
				.equals(InventoryConstant.CUR_VIRTUAL))
			{
				lvCurBalQueryResults =
					(
						Vector) laIVRptSQL
							.qryInventoryInquiryCurrentBalanceReport(
						laInvInqUIData);
			}
			// end defect 9117

			// Run query against TR_INV_DETAIL (history)
			if (laInvInqUIData
				.getRptType()
				.equals(InventoryConstant.HISTORY)
				|| laInvInqUIData.getRptType().equals(
					InventoryConstant.CUR_BAL_HISTORY)
				|| laInvInqUIData.getInvInqBy().equals(
					InventoryConstant.SPECIFIC_ITM))
			{
				lvHistoryQueryResults =
					(
						Vector) laInvRptSQL
							.qryInventoryInquiryHistoryReport(
						laInvInqUIData);
			}

			// Check if need to prompt user that no data was found and 
			// if they want to create an exception report.
			if (lvCurBalQueryResults.size() < CommonConstant.ELEMENT_1
				&& lvHistoryQueryResults.size()
					< CommonConstant.ELEMENT_1)
			{
				if ((laInvInqUIData
					.getInvInqBy()
					.equals(InventoryConstant.CNTRL)
					|| laInvInqUIData.getInvInqBy().equals(
						InventoryConstant.ITM_TYPES))
					&& laInvInqUIData.getExceptionReport() == 0)
				{
					String lsMsgType = new String(RTSException.CTL001);
					// defect 9117
					String lsMsg = "";
					if (laInvInqUIData
						.getRptType()
						.equals(InventoryConstant.CUR_VIRTUAL))
					{
						lsMsg = new String(TXT_NO_RECORD);
					}
					else
					{
						lsMsg =
							new String(TXT_PRINT_EXCEPTION_RPT_QUESTION);
					}
					// end defect 9117
					RTSException leRTSException =
						new RTSException(lsMsgType, lsMsg, null);
					leRTSException.setDefaultKey(RTSException.NO);
					throw leRTSException;
				}
				else if (
					!laInvInqUIData.getInvInqBy().equals(
						InventoryConstant.CNTRL)
						&& !laInvInqUIData.getInvInqBy().equals(
							InventoryConstant.ITM_TYPES))
				{
					RTSException leRTSException = new RTSException(182);
					throw leRTSException;
				}
			}
			laDBA.endTransaction(DatabaseAccess.COMMIT);

			// Sort the Current Balance vector by InvId, ItmCd, InvItmYr 
			// and PatrnSeqNo.
			// Sort the History Balance vector by InvId, ItmCd and 
			// InvItmYr.
			// UtilityMethods.sort(lvCurBalQueryResults);
			// UtilityMethods.sort(lvHistoryQueryResults);

			// setup the results vector
			lvReport.add(laInvInqUIData);
			lvReport.add(lvCurBalQueryResults);
			lvReport.add(lvHistoryQueryResults);

			// run the report
			laGIIR.formatReport(lvReport);

			// defect 8628 
			return new ReportSearchData(
				laGIIR.getFormattedReport().toString(),
				RPT_3031_INVINQ_FILENAME,
				ReportConstant.RPT_3031_INVENTORY_INQUIRY_REPORT_TITLE,
				ReportConstant.RPT_7_COPIES,
				ReportConstant.PORTRAIT);
			// end defect 8628  
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GENINVINQRPT_END + csClientHost);
		}
	}

	/**
	 * Sets up the data to generate the Inventory Receive Report.
	 *
	 * <p>Parameter aaData is a vector where the elements have to be as 
	 * follows:
	 * <ul>
	 * <li>Element(0) - The MFInventoryAllocationData object.
	 * <li>Element(1) - ReportSearchData object with the following 
	 * fields:
	 * <ul>
	 * <li>IntKey1() - The office issuance number
	 * <li>IntKey2() - The substation id
	 * <li>IntKey3() - The workstation id
	 * <li>Key1()    - The current user
	 * <eul>
	 * <eul>
	 * 
	 * <p>Returns the report in the form of ReportSearchData.
	 * 
	 * @param aaData Object
	 * @param aaDBA DatabaseAccess 
	 * @return ReportSearchData
	 * @throws RTSException 
	 */
	public Object genInvRcveRpt(Object aaData, DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GENINVRCVERPT_BEGIN + csClientHost);

		MFInventoryAllocationData laMFInvAllcData =
			(MFInventoryAllocationData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_0);

		ReportSearchData laRptSearchData =
			(ReportSearchData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_1);

		// defect 8628 
		// UOW #1 BEGIN
		ReportProperties laRptProps =
			initReportProperties(
				laRptSearchData,
				aaDBA,
				ReportConstant.RPT_3021_INVENTORY_RECEIVE_REPORT_ID);
		// UOW #1 END 
		// end defect 8628 

		GenInventoryReceivedReport laIRR =
			new GenInventoryReceivedReport(
				ReportConstant.RPT_3021_INVENTORY_RECEIVE_REPORT_TITLE,
				laRptProps);

		laIRR.formatReport(laMFInvAllcData);

		Log.write(
			Log.METHOD,
			this,
			MSG_GENINVRCVERPT_END + csClientHost);

		// defect 8628
		return new ReportSearchData(
			laIRR.getFormattedReport().toString(),
			RPT_3021_FILENAME,
			ReportConstant.RPT_3021_INVENTORY_RECEIVE_REPORT_TITLE,
			ReportConstant.RPT_7_COPIES,
			ReportConstant.PORTRAIT);

		// end defect 8628 
	}

	/**
	 * Sets up the data to generate the Inventory History Receive Report.
	 *
	 * <p>Return the report in the form of ReportSearchData.
	 * 
	 * <p>Parameter aaData is a vector where the elements have to be as 
	 * follows:
	 * <ul>
	 * <li>Element(0) - The InventoryHistoryUIData object.
	 * <li>Element(1) - ReportSearchData object with the following 
	 * fields:
	 * <ul>
	 * <li>IntKey1() - The office issuance number
	 * <li>IntKey2() - The substation id
	 * <li>IntKey3() - The workstation id
	 * <li>Key1()    - The current user
	 * <eul>
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	public Object genRcveInvHistoryRpt(Object aaData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GENRCVEINVHISTORYRPT_BEGIN + csClientHost);

		InventoryHistoryUIData laInvHisUIData =
			(InventoryHistoryUIData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_0);
		ReportSearchData laRptSearchData =
			(ReportSearchData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_1);

		// defect 8940 
		AdministrationLogData laAdminLogData =
			(AdministrationLogData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_2);
		// end defect 8940 
		DatabaseAccess laDBA = new DatabaseAccess();

		// defect 8628 
		// UOW #1 BEGIN
		ReportProperties laRptProps =
			initReportProperties(
				laRptSearchData,
				laDBA,
				ReportConstant
					.RPT_3072_RECEIVE_INVENTORY_HISTORY_REPORT_ID);
		// UOW #1 END 
		// end defect 8628

		// defect 8940
		// UOW #2 BEGIN 
		laDBA.beginTransaction();
		AdministrationLog laAdminlog = new AdministrationLog(laDBA);
		laAdminlog.insAdministrationLog(laAdminLogData);
		laDBA.endTransaction(DatabaseAccess.COMMIT);
		// UOW #2 END  
		// end defect 8940 

		GenReceiveInventoryHistoryReport laIHR =
			new GenReceiveInventoryHistoryReport(
				ReportConstant
					.RPT_3072_RECEIVE_INVENTORY_HISTORY_REPORT_TITLE,
				laRptProps);

		InventoryReportsSQL laInvRptSQL =
			new InventoryReportsSQL(laDBA);

		// defect 8628 
		// UOW #3 BEGIN 
		laDBA.beginTransaction();
		// defect 10207 
		Vector lvQueryResults =
			laInvRptSQL.qryInventoryReceiveHistoryReport(
				laInvHisUIData);
		// end defect 10207 
		laDBA.endTransaction(DatabaseAccess.COMMIT);
		// UOW #3 END
		// end defect 8628  

		// send laInvHisUIData along with queryResults to the report 
		// generator	 
		Vector lvReportInput = new Vector();
		lvReportInput.addElement(laInvHisUIData);
		lvReportInput.addElement(lvQueryResults);
		laIHR.formatReport(lvReportInput);

		Log.write(
			Log.METHOD,
			this,
			MSG_GENRCVEINVHISTORYRPT_END + csClientHost);

		return new ReportSearchData(
			laIHR.getFormattedReport().toString(),
			RPT_3072_FILENAME,
			ReportConstant
				.RPT_3072_RECEIVE_INVENTORY_HISTORY_REPORT_TITLE,
			ReportConstant.RPT_7_COPIES,
			ReportConstant.PORTRAIT);
	}

	/**
	 * Sets up the data to generate the OnLine Inventory Action Report.
	 *
	 * <p>Parameter aaData is a ReportSearchData object with the 
	 * following fields:
	 * <ul>
	 * <li>IntKey1() - The office issuance number
	 * <li>IntKey2() - The substation id
	 * <li>IntKey3() - The workstation id
	 * <li>Key1() - The current user
	 * <li>Key2() - TransAmDate to report on
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException  
	 */
	public Object genVIRejectionRpt(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"genVIRejectionRpt Begin " + csClientHost);

		ReportSearchData laRptSearchData = (ReportSearchData) aaData;

		DatabaseAccess laDBA = new DatabaseAccess();

		// defect 8628

		// UOW #1 BEGIN
		ReportProperties laRptProps =
			initReportProperties(
				laRptSearchData,
				laDBA,
				ReportConstant.RPT_3073_VI_REJ_REPORT_ID);
		// UOW #1 END 

		AdministrationLogData laLogData =
			new AdministrationLogData(laRptSearchData);
		laLogData.setAction("Report");
		laLogData.setEntity("ViRejRpt");
		// Use MM/DD/YY vs. AMDate to represent date  
		int liAMDate = Integer.parseInt(laRptSearchData.getKey2());
		RTSDate laRTSDate = new RTSDate(RTSDate.AMDATE, liAMDate);
		laLogData.setEntityValue(laRTSDate.getMMDDYY());

		// UOW #2 BEGIN
		laDBA.beginTransaction();
		AdministrationLog laAdminLog = new AdministrationLog(laDBA);
		laAdminLog.insAdministrationLog(laLogData);
		laDBA.endTransaction(DatabaseAccess.COMMIT);
		// UOW #2 END  

		SpecialPlateRejectionLogData laSpclPltRejLogData =
			new SpecialPlateRejectionLogData();

		laSpclPltRejLogData.setTransAMDate(
			Integer.parseInt(laRptSearchData.getKey2()));

		// UOW #3 BEGIN
		laDBA.beginTransaction();
		SpecialPlateRejectionLog laSplPltRegLog =
			new SpecialPlateRejectionLog(laDBA);

		Vector lvQueryResults =
			laSplPltRegLog.qrySpecialPlateRejectionLog(
				laSpclPltRejLogData);
		laDBA.endTransaction(DatabaseAccess.COMMIT);
		// UOW #3 END

		GenVIRejectionReport laVIRejReport =
			new GenVIRejectionReport(
				ReportConstant.RPT_3073_VI_REJ_REPORT_TITLE,
				laRptProps);

		// Report Date
		RTSDate laReportDataDate =
			new RTSDate(
				RTSDate.AMDATE,
				Integer.parseInt(laRptSearchData.getKey2()));

		// Vector to pass Data
		Vector lvReportData = new Vector();
		lvReportData.addElement(laReportDataDate.toString());
		lvReportData.addElement(lvQueryResults);

		// Format 
		laVIRejReport.formatReport(lvReportData);

		Log.write(
			Log.METHOD,
			this,
			"genVIRejectionRpt End   " + csClientHost);

		return new ReportSearchData(
			laVIRejReport.getFormattedReport().toString(),
			ReportConstant.RPT_3073_FILENAME,
			ReportConstant.RPT_3073_VI_REJ_REPORT_TITLE,
			ReportConstant.RPT_1_COPY,
			ReportConstant.PORTRAIT);
		// end defect 8628 
	}

	/**
	 * Used to retrieve the subcontractor, workstation, dealer, 
	 * employee, and substation information required for screens INV009 
	 * and INV010.
	 *
	 * <p>Parameter aaData is search parameters in the form of 
	 * GeneralSearchData. The required fields are as follows:
	 * <ul>
	 * <li>IntKey(1) - The office issuance number
	 * <li>IntKey(2) - The substation id
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return AllocationDbData
	 * @throws RTSException  
	 */
	private AllocationDbData getAlloctnDispData(Object aaData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETALLOCTNDISPDATA_BEGIN + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();
		AllocationDbData laAlloctnDbData = new AllocationDbData();
		try
		{
			laDBA.beginTransaction();

			laAlloctnDbData.setSubconWrap(getSubcons(aaData, laDBA));
			laAlloctnDbData.setWsWrap(getWsIds(aaData, laDBA));
			laAlloctnDbData.setDlrWrap(getDlrs(aaData, laDBA));
			laAlloctnDbData.setSecrtyWrap(getEmps(aaData, laDBA));
			laAlloctnDbData.setSubstaWrap(getSubsta(aaData, laDBA));

			laDBA.endTransaction(DatabaseAccess.NONE);

			return laAlloctnDbData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETALLOCTNDISPDATA_END + csClientHost);
		}
	}

	/**
	 * Returns all the dealers given SubstationId and 
	 * OfficeIssuanceNumber.
	 *
	 * <p>Parameter aaData is search parameters in the form of 
	 * GeneralSearchData.  The required fields are as follows:
	 * <ul>
	 * <li>IntKey(1) - The office issuance number
	 * <li>IntKey(2) - The substation id
	 * <eul>
	 * 
	 * @param aaData Object
	 * @param aaDBA DatabaseAccess 
	 * @return Vector
	 * @throws RTSException 
	 */
	private Vector getDlrs(Object aaData, DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_GETDLRS_BEGIN + csClientHost);

		Vector lvDlrData = new Vector();
		try
		{
			GeneralSearchData laGSData = (GeneralSearchData) aaData;
			// defect 10112 
			Dealer laDbDlr = new Dealer(aaDBA);
			DealerData laDlrData = new DealerData();

			laDlrData.setOfcIssuanceNo(laGSData.getIntKey1());
			laDlrData.setSubstaId(laGSData.getIntKey2());
			laDlrData.setId(Integer.MIN_VALUE);
			lvDlrData = laDbDlr.qryDealer(laDlrData);
			// end defect 10112  

			return lvDlrData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(Log.METHOD, this, MSG_GETDLRS_END + csClientHost);
		}
	}

	/**
	 * Returns all the employees given SubstationId and 
	 * OfficeIssuanceNumber.
	 *  
	 * <p>Parameter aaData has the search parameters in the form of 
	 * GeneralSearchData.  The required fields are as follows:
	 * <ul>
	 * <li>IntKey(1) - The office issuance number
	 * <li>IntKey(2) - The substation id
	 * <eul>
	 * 
	 * @param aaData Object
	 * @param aaDBA DatabaseAccess 
	 * @return Vector
	 * @throws RTSException  
	 */
	private Vector getEmps(Object aaData, DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_GETEMPS_BEGIN + csClientHost);

		Vector lvSecrtyData = new Vector();
		try
		{
			GeneralSearchData laGSData = (GeneralSearchData) aaData;
			Security laDbSecrty = new Security(aaDBA);
			SecurityData laSecrtyData = new SecurityData();

			laSecrtyData.setOfcIssuanceNo(laGSData.getIntKey1());
			laSecrtyData.setSubstaId(laGSData.getIntKey2());
			lvSecrtyData = laDbSecrty.qrySecurity(laSecrtyData);

			return lvSecrtyData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(Log.METHOD, this, MSG_GETEMPS_END + csClientHost);
		}
	}

	/**
	 * Returns the max number of inventory items (for a specific item 
	 * code) that can be allocated to an entity.
	 * 
	 * @param aaInvAllocData InventoryAllocationUIData
	 * @param aaDBA DatabaseAccess 
	 * @return ProcessInventoryData
	 * @throws RTSException 
	 */
	private ProcessInventoryData getEntitysMaxAlloctn(
		InventoryAllocationUIData aaInvAllocData,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETENTITSMAXALLOCTN_BEGIN + csClientHost);

		ProcessInventoryData laProcsInvData =
			new ProcessInventoryData();
		Vector lvProfile = new Vector();
		Vector lvInvAlloc = new Vector();
		int liEntitysQty = 0;

		try
		{
			aaDBA.beginTransaction();

			// find out if there is a profile for the entity
			// set up SQL components
			InventoryProfile laInvProfileSQL =
				new InventoryProfile(aaDBA);
			InventoryProfileData laInvProfileData =
				new InventoryProfileData();

			// populate search data
			laInvProfileData.setOfcIssuanceNo(
				aaInvAllocData.getOfcIssuanceNo());
			laInvProfileData.setSubstaId(aaInvAllocData.getSubstaId());
			laInvProfileData.setItmCd(aaInvAllocData.getItmCd());
			laInvProfileData.setInvItmYr(aaInvAllocData.getInvItmYr());
			laInvProfileData.setEntity(aaInvAllocData.getInvLocIdCd());
			laInvProfileData.setId(aaInvAllocData.getInvId());

			lvProfile =
				laInvProfileSQL.qryInventoryProfile(laInvProfileData);

			// check the results
			if (lvProfile.size() > CommonConstant.ELEMENT_0)
			{
				laInvProfileData =
					(InventoryProfileData) lvProfile.get(
						CommonConstant.ELEMENT_0);
			}
			else if (
				aaInvAllocData.getInvLocIdCd().equals(
					InventoryConstant.CHAR_E))
			{
				// if this is an "E" and no return, check for "DEFAULT"
				laInvProfileData.setId(INV_DEFAULT_EMPLOYEE);
				lvProfile =
					laInvProfileSQL.qryInventoryProfile(
						laInvProfileData);
				if (lvProfile.size() > CommonConstant.ELEMENT_0)
				{
					laInvProfileData =
						(InventoryProfileData) lvProfile.get(
							CommonConstant.ELEMENT_0);
				}
			}

			// if no profile found, set the qty's to min_value
			if (lvProfile.size() == CommonConstant.ELEMENT_0)
			{
				laInvProfileData.setMaxQty(Integer.MIN_VALUE);
				laInvProfileData.setMinQty(Integer.MIN_VALUE);
				laInvProfileData.setNextAvailIndi(Integer.MIN_VALUE);
			}

			// get rows for entity
			InventoryAllocation laInvAllSql =
				new InventoryAllocation(aaDBA);
			InventoryAllocationData laIAD =
				new InventoryAllocationData();
			laIAD.setOfcIssuanceNo(aaInvAllocData.getOfcIssuanceNo());
			laIAD.setSubstaId(aaInvAllocData.getSubstaId());
			laIAD.setItmCd(aaInvAllocData.getItmCd());
			laIAD.setInvItmYr(aaInvAllocData.getInvItmYr());
			laIAD.setInvLocIdCd(aaInvAllocData.getInvLocIdCd());
			laIAD.setInvId(aaInvAllocData.getInvId());

			lvInvAlloc = laInvAllSql.qryInventoryAllocation(laIAD);

			// sum up qty for entity.
			for (int j = 0; j < lvInvAlloc.size(); j++)
			{
				laIAD = (InventoryAllocationData) lvInvAlloc.get(j);
				liEntitysQty = liEntitysQty + laIAD.getInvQty();
			}

			// return the qty added up.
			laProcsInvData.setInvQty(liEntitysQty);
			laProcsInvData.setMaxQty(laInvProfileData.getMaxQty());

			// check to see what we do about Central profile?

			aaDBA.endTransaction(DatabaseAccess.NONE);

			return laProcsInvData;
		}
		catch (RTSException aeRTSEx)
		{
			aaDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETENTITYSMAXALLOCTN_END + csClientHost);
		}
	}

	/**
	 * Retrieve the entity data required for screen INV027.
	 * 
	 * <p>Parameter aaData is in the form of InventoryInquiryData.
	 * 
	 * @param aaData Object 
	 * @return Vector
	 * @throws RTSException  
	 */
	private Vector getInqEntityData(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETINQENTITYDATA_BEGIN + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();
		InventoryInquiryUIData laInvInqUIData =
			(InventoryInquiryUIData) aaData;
		GeneralSearchData laGSA = new GeneralSearchData();
		Vector lvRtrn = new Vector();

		try
		{
			laDBA.beginTransaction();

			laGSA.setIntKey1(laInvInqUIData.getOfcIssuanceNo());
			laGSA.setIntKey2(laInvInqUIData.getSubstaId());

			lvRtrn.add(laInvInqUIData);

			if (laInvInqUIData
				.getInvInqBy()
				.equals(InventoryConstant.EMP))
			{
				lvRtrn.add(getEmps(laGSA, laDBA));
			}
			else if (
				laInvInqUIData.getInvInqBy().equals(
					InventoryConstant.WS))
			{
				lvRtrn.add(getWsIds(laGSA, laDBA));
			}
			else if (
				laInvInqUIData.getInvInqBy().equals(
					InventoryConstant.DLR))
			{
				lvRtrn.add(getDlrs(laGSA, laDBA));
			}
			else if (
				laInvInqUIData.getInvInqBy().equals(
					InventoryConstant.SUBCON))
			{
				lvRtrn.add(getSubcons(laGSA, laDBA));
			}

			laDBA.endTransaction(DatabaseAccess.NONE);

			return lvRtrn;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETINQENTITYDATA_END + csClientHost);
		}
	}

	/**
	 * Retrieve the inventory data from the RTS_INV_ALLOCATION table 
	 * required for screen INV017.
	 *
	 * @param aaData Object (InventoryAllocationData)
	 * @return Vector
	 * @throws RTSException  
	 */
	private Vector getInvAllocData(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETINVALLOCDATA_BEGIN + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();

		Vector lvRetData = null;

		try
		{
			laDBA.beginTransaction();
			InventoryAllocation laDbIA = new InventoryAllocation(laDBA);
			lvRetData =
				(Vector) laDbIA.qryInventoryAllocation(
					(InventoryAllocationData) aaData);
			laDBA.endTransaction(DatabaseAccess.NONE);

			return lvRetData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETINVALLOCDATA_END + csClientHost);
		}
	}

	/**
	 * Retrieves the invoice information from the mainframe and then 
	 * validates all the line items of the invoice.
	 *
	 * <p>Paramtere aaData is a vector where the elements have to be as 
	 * follows:
	 * <ul>
	 * <li>Element(0) - Vector conatianing substation data in the 
	 * 					form of SubstationData.
	 * <li>Element(1) - The invoice header information as a 
	 * 					MFInventoryAllocationData object.
	 * <li>Element(2) - The main frame search parameters in the form 
	 * 					of GeneralSearchData.
	 * <eul>
	 * 
	 * <p>Returns a vector where the elements are as follows:
	 * <ul>
	 * <li>Element(0) - Vector conatianing substation data in the 
	 * 					form of SubstationData.
	 * <li>Element(1) - The invoice in the form of a 
	 * 					MFInventoryAllocationData object.
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Vector
	 * @throws RTSException 
	 */
	private Vector getInvFromMF(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETINVFROMMF_BEGIN + csClientHost);

		MFInventoryAllocationData laMFInvAlloctnData =
			new MFInventoryAllocationData();
		Vector lvSubstaData =
			(Vector) ((Vector) aaData).get(CommonConstant.ELEMENT_0);
		MFInventoryAllocationData laMFInvAllocationData =
			(MFInventoryAllocationData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_1);
		GeneralSearchData laGSData =
			(GeneralSearchData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_2);

		// Test if the invoice has already been received
		// The first character of the invoice determines the type
		if (!laGSData
			.getKey1()
			.substring(0, 1)
			.equals(InventoryConstant.INVOICE_PREFIX_DUMMY))
		{
			int liInvcRcvd = 0;
			DatabaseAccess laDBA = new DatabaseAccess();
			try
			{
				laDBA.beginTransaction();
				InventoryFunctionTransaction laInvFuncTrans =
					new InventoryFunctionTransaction(laDBA);

				liInvcRcvd =
					laInvFuncTrans
						.qryInventoryFunctionTransactionInvoice(
						laGSData);
				laDBA.endTransaction(DatabaseAccess.NONE);
			}
			catch (RTSException aeRTSEx)
			{
				laDBA.endTransaction(DatabaseAccess.NONE);
				throw aeRTSEx;
			}

			if (liInvcRcvd != 0)
			{
				RTSException leRTSEx = new RTSException(602);
				throw leRTSEx;
			}
		}

		// Call the mainframe to retrieve the invoice information
		MfAccess laMF = new MfAccess(csClientHost);
		laMFInvAlloctnData = laMF.retrieveInvoice(laGSData);

		// this section is for stand alone testing
		// lMFInvAlloctnData = aMFInvAlloctnData;
		// lMFInvAlloctnData.getMFInvAckData().setInvcNo(
		//		lGSData.getKey1());
		// lMFInvAlloctnData.getMFInvAckData().setOfcIssuanceNo(
		//		lGSData.getIntKey1());
		// lMFInvAlloctnData.getMFInvAckData().setInvItmOrderDt(
		//		RTSDate.getCurrentDate());
		// lMFInvAlloctnData.setInvAlloctnData(new Vector());
		//

		// Copy the destination, receive into, and verification on 
		// information collected from INV004 to the new data object with 
		// the mf data
		laMFInvAlloctnData.getMFInvAckData().setDest(
			laMFInvAllocationData.getMFInvAckData().getDest());
		laMFInvAlloctnData.getMFInvAckData().setRcveInto(
			laMFInvAllocationData.getMFInvAckData().getRcveInto());
		laMFInvAlloctnData.getMFInvAckData().setVerificationOn(
			laMFInvAllocationData
				.getMFInvAckData()
				.getVerificationOn());
		laMFInvAlloctnData.setSelctdSubstaIndx(
			laMFInvAllocationData.getSelctdSubstaIndx());

		// Test if the invcno == "", if yes display error 990
		if (laMFInvAlloctnData.getMFInvAckData().getInvcNo() == null)
		{
			RTSException leRTSEx =
				new RTSException(
					ErrorsConstant.ERR_NUM_INVOICE_NOT_ON_MF);
			throw leRTSEx;
		}
		// Test if the invoice being acknowledged is for this county
		if (laMFInvAlloctnData.getMFInvAckData().getOfcIssuanceNo()
			!= laGSData.getIntKey1())
		{
			RTSException leRTSEx = new RTSException(285);
			throw leRTSEx;
		}

		// If the invoice is not empty, validate each item on the 
		// received invoice
		if (laMFInvAlloctnData.getInvAlloctnData().size()
			!= CommonConstant.ELEMENT_0)
		{
			// Make of copy of the original invoice qty, invitmno, and 
			// invitmendno for each line item.
			for (int i = 0;
				i < laMFInvAlloctnData.getInvAlloctnData().size();
				i++)
			{
				InventoryAllocationUIData laIAUID =
					(InventoryAllocationUIData) laMFInvAlloctnData
						.getInvAlloctnData()
						.get(
						i);
				laIAUID.setOrigInvQty(laIAUID.getInvQty());
				laIAUID.setOrigInvItmNo(laIAUID.getInvItmNo());
				laIAUID.setOrigInvItmEndNo(laIAUID.getInvItmEndNo());
			}

			laMFInvAlloctnData.setTransCd(InventoryConstant.RCVE);

			Vector lvTmp = new Vector(CommonConstant.ELEMENT_2);
			lvTmp.add(lvSubstaData);
			lvTmp.add(laMFInvAlloctnData);
			lvTmp = validateInvcItms(lvTmp);
			laMFInvAlloctnData =
				(MFInventoryAllocationData) lvTmp.get(
					CommonConstant.ELEMENT_0);
		}

		Vector lvVct = new Vector(CommonConstant.ELEMENT_2);
		lvVct.add(((Vector) aaData).get(CommonConstant.ELEMENT_0));
		lvVct.add(laMFInvAlloctnData);

		Log.write(
			Log.METHOD,
			this,
			MSG_GETINVFROMMF_END + csClientHost);

		return lvVct;
	}

	/**
	 * Used to get the InvFuncTrans and TrInvDetail objects from the 
	 * database.
	 *
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException  
	 */
	private Object getInvFuncTransAndTrInvDetail(Object aaData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETINVFUNCTRANSANDTRINVDETAIL_BEGIN + csClientHost);

		//Set up vars
		TransactionKey laTransactionKey = (TransactionKey) aaData;
		Vector lvReturn = new Vector();

		//Retrieve InventoryFunctionTransactionData
		InventoryFunctionTransactionData laInvFuncTransData =
			new InventoryFunctionTransactionData();
		laInvFuncTransData.setOfcIssuanceNo(
			laTransactionKey.getOfcIssuanceNo());
		laInvFuncTransData.setSubstaId(laTransactionKey.getSubstaId());
		laInvFuncTransData.setTransAMDate(
			laTransactionKey.getTransAMDate());
		laInvFuncTransData.setTransWsId(
			laTransactionKey.getTransWsId());
		laInvFuncTransData.setCustSeqNo(
			laTransactionKey.getCustSeqNo());
		laInvFuncTransData.setTransTime(
			laTransactionKey.getTransTime());

		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			InventoryFunctionTransaction laInvFuncTrans =
				new InventoryFunctionTransaction(laDBA);
			laDBA.beginTransaction();
			Vector lvTmpVec =
				laInvFuncTrans.qryInventoryFunctionTransaction(
					laInvFuncTransData);
			laDBA.endTransaction(DatabaseAccess.NONE);

			if (lvTmpVec == null
				|| lvTmpVec.size() == CommonConstant.ELEMENT_0)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					MSG_NOINVFUNCTRANS,
					TXT_ERROR);
			}
			else
			{
				lvReturn.addElement(
					lvTmpVec.get(CommonConstant.ELEMENT_0));
			}

			//Retrieve Transaction Inventory Detail
			TransactionInventoryDetailData laTransInvDetailData =
				new TransactionInventoryDetailData();
			laTransInvDetailData.setOfcIssuanceNo(
				laTransactionKey.getOfcIssuanceNo());
			laTransInvDetailData.setSubstaId(
				laTransactionKey.getSubstaId());
			laTransInvDetailData.setTransAMDate(
				laTransactionKey.getTransAMDate());
			laTransInvDetailData.setTransWsId(
				laTransactionKey.getTransWsId());
			laTransInvDetailData.setCustSeqNo(
				laTransactionKey.getCustSeqNo());
			laTransInvDetailData.setTransTime(
				laTransactionKey.getTransTime());
			TransactionInventoryDetail laTransInvDetail =
				new TransactionInventoryDetail(laDBA);
			laDBA.beginTransaction();
			lvTmpVec =
				laTransInvDetail.qryTransactionInventoryDetail(
					laTransInvDetailData);
			laDBA.endTransaction(DatabaseAccess.NONE);
			if (lvTmpVec == null
				|| lvTmpVec.size() == CommonConstant.ELEMENT_0)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					MSG_NO_TRINVDETAIL,
					TXT_ERROR);
			}
			else
			{
				lvReturn.addElement(lvTmpVec);
			}

			return lvReturn;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETINVFUNCTRANSANDTRINVDETAIL_END + csClientHost);
		}
	}

	/**
	 * Used to get a list of all the inventory item codes and 
	 * descriptions with their associated years.
	 *
	 * @return Vector 
	 * @throws RTSException  
	 */
	private Vector getInvItms() throws RTSException
	{
		// TODO only used for Region History.  Do we need this?
		Log.write(
			Log.METHOD,
			this,
			MSG_GETINVITMS_BEGIN + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();
		GeneralSearchData laGSD = new GeneralSearchData();
		Vector lvRetData = new Vector();

		try
		{
			laDBA.beginTransaction();
			InventoryPatterns laInventoryPatterns =
				new InventoryPatterns(laDBA);
			laGSD.setIntKey1(3);
			lvRetData =
				laInventoryPatterns.qryInventoryPatternsDescription(
					laGSD);
			laDBA.endTransaction(DatabaseAccess.NONE);

			return lvRetData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETINVITMS_END + csClientHost);
		}
	}

	/**
	 * Used to get a list of all the inventory item codes and 
	 * descriptions with their associated years.
	 *
	 * <p>Paramter aaData is a vector that contains three elements and 
	 * the third element has to be as follows:
	 * <ul>
	 * <li>Element(2) - The search parameters in the form of 
	 * 					GeneralSearchData.
	 *                  The required fields are as follows:
	 * <li>IntKey(1) - If this is equal to 2, it will return 
	 * 			   		inventory with all processing codes.  
	 * 					If this is equal to something besides 2, 
	 * 					it will return only inventory with 
	 * 					processing codes of 1 and 2.
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Vector
	 * @throws RTSException 
	 */
	private Vector getInvItmsForInq(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETINVITMSFORINQ_BEGIN + csClientHost);

		Vector lvInqData = (Vector) aaData;
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			laDBA.beginTransaction();

			InventoryPatterns laInventoryPatterns =
				new InventoryPatterns(laDBA);

			// defect 9085 
			// Call qryVIInventoryPatternsDescription if 
			//  request for VI Inquiry 
			InventoryInquiryUIData laInvInqUIData =
				(InventoryInquiryUIData) lvInqData.elementAt(
					CommonConstant.ELEMENT_0);

			String lsReportType = laInvInqUIData.getRptType();

			if (lsReportType != null
				&& lsReportType.equals(InventoryConstant.CUR_VIRTUAL))
			{
				lvInqData.setElementAt(
					laInventoryPatterns
						.qryVIInventoryPatternsDescription(),
					2);
			}
			else
			{
				lvInqData.setElementAt(
					laInventoryPatterns
						.qryInventoryPatternsDescription(
						(GeneralSearchData) lvInqData.get(
							CommonConstant.ELEMENT_2)),
					2);
			}
			// end defect 9085 

			laDBA.endTransaction(DatabaseAccess.NONE);

			return lvInqData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETINVITMSFORINQ_END + csClientHost);
		}
	}

	/**
	 * Used to get an inventory profile from the RTS_INV_PROFILE table.
	 *  
	 * @param aaData Object 
	 * @param aaDBA DatabaseAccess 
	 * @return Vector
	 * @throws RTSException 
	 */
	private Object getInvProfile(Object aaData, DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETINVPROFILE_BEGIN + csClientHost);

		Object laRetData = null;
		boolean lbDba = true;
		try
		{
			if (aaDBA == null)
			{
				aaDBA = new DatabaseAccess();
				aaDBA.beginTransaction();
				lbDba = false;
			}
			InventoryProfile laDbIP = new InventoryProfile(aaDBA);

			laRetData =
				(Object) laDbIP.qryInventoryProfile(
					(InventoryProfileData) aaData);

			if (!lbDba)
			{
				aaDBA.endTransaction(DatabaseAccess.NONE);
			}

			return laRetData;
		}
		catch (RTSException aeRTSEx)
		{
			aaDBA.endTransaction(DatabaseAccess.NONE);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETINVPROFILE_END + csClientHost);
		}
	}

	/**
	 * Used to retrieve the inventory profile, subcontractor, 
	 * workstation, dealer, employee, and substation information 
	 * required for screen INV016.
	 *
	 * <p>Parameter aaData is a vector where the elements have to be 
	 * as follows:
	 * <ul>
	 * <li>Element(0) - The search parameters in the form of 
	 * 					GeneralSearchData.  The required fields are 
	 * 					as follows:
	 * <ul>
	 * <li>IntKey(1) - The office issuance number
	 * <li>IntKey(2) - The substation id
	 * <eul>
	 * <li>Element(1) - The parameters for the inventory profile in 
	 * 					the form of an InventoryProfileData object.
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Vector
	 * @throws RTSException  
	 */
	private Vector getInvProfileDispData(Object aaData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETINVPROFILEDISPDATA_BEGIN + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();

		Vector lvVct = (Vector) aaData;
		Vector lvRetData = new Vector();

		GeneralSearchData laGSData =
			(GeneralSearchData) lvVct.elementAt(
				CommonConstant.ELEMENT_0);
		InventoryProfileData laIPData =
			(InventoryProfileData) lvVct.elementAt(
				CommonConstant.ELEMENT_1);

		AllocationDbData laAlloctnDbData = new AllocationDbData();
		try
		{
			laDBA.beginTransaction();

			Vector lvVctRet = (Vector) getInvProfile(laIPData, laDBA);
			if (lvVctRet != null && !lvVctRet.isEmpty())
			{
				laIPData =
					(InventoryProfileData) lvVctRet.get(
						CommonConstant.ELEMENT_0);
			}
			else
			{
				laIPData = null;
			}

			laAlloctnDbData.setSubconWrap(getSubcons(laGSData, laDBA));
			laAlloctnDbData.setWsWrap(getWsIds(laGSData, laDBA));
			laAlloctnDbData.setDlrWrap(getDlrs(laGSData, laDBA));
			laAlloctnDbData.setSecrtyWrap(getEmps(laGSData, laDBA));
			laAlloctnDbData.setSubstaWrap(getSubsta(laGSData, laDBA));

			lvRetData.addElement(laIPData);
			lvRetData.addElement(laAlloctnDbData);

			laDBA.endTransaction(DatabaseAccess.NONE);

			return lvRetData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETINVPROFILEDISPDATA_END + csClientHost);
		}
	}

	/**
	 * Used to look up inventory items in the RTS_INV_ALLOCTION table.  
	 * Currently used by send cache to look up the InvLocIdCd and InvId 
	 * for the inventory items issued when the database was down.
	 *
	 * <p>Parameter aaData is a vector where the elements have to be as 
	 * follows:
	 * <ul
	 * <li>Element(0) - The database access
	 * <li>Element(1) - The inventory range to look up in the db as 
	 * 					a ProcessInventoryData object
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Vector
	 * @throws RTSException 
	 */
	private Vector getInvRngeInDb(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETINVRNGEINDB_BEGIN + csClientHost);

		DatabaseAccess laDBA =
			(DatabaseAccess) ((Vector) aaData).get(
				CommonConstant.ELEMENT_0);
		ProcessInventoryData laProcsInvData =
			(ProcessInventoryData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_1);
		;
		ProcessInventoryData laProcsInvDataOrig =
			(ProcessInventoryData) UtilityMethods.copy(laProcsInvData);
		Vector lvProcsInvData = new Vector();

		try
		{
			// Validate and set up inventory information and then make 
			// call to  Inventory Allocation table
			if (!ValidateInventoryPattern
				.chkIfItmPLPOrOLDPLTOrROP(laProcsInvData.getItmCd()))
			{
				// Validate the begin inventory item number and 
				// calculate the PatrnSeqNo
				InventoryAllocationUIData laIAUID =
					laProcsInvDataOrig.convertToInvAlloctnUIData(
						laProcsInvDataOrig);

				// see if calculation is needed.
				if (!laIAUID.isCalcInv())
				{
					ValidateInventoryPattern laVIP =
						new ValidateInventoryPattern();
					laIAUID =
						(
							InventoryAllocationUIData) laVIP
								.calcInvUnknown(
							laIAUID);
					laProcsInvDataOrig =
						laProcsInvDataOrig.convertFromInvAlloctnData(
							laIAUID);

					// Calculate the EndPatrnSeqNo
					laIAUID.setItmCd(laProcsInvData.getItmCd());
					laIAUID.setInvItmYr(laProcsInvData.getInvItmYr());
					laIAUID.setInvQty(laProcsInvData.getInvQty());
					laIAUID.setInvItmNo(
						laProcsInvData.getInvItmEndNo());
					laIAUID.setInvItmEndNo(
						laProcsInvData.getInvItmEndNo());

					laIAUID =
						(
							InventoryAllocationUIData) laVIP
								.calcInvUnknown(
							laIAUID);
					laProcsInvDataOrig.setEndPatrnSeqNo(
						laIAUID.getPatrnSeqNo());
				}

				laProcsInvDataOrig.setInvId(null);

				// Verify begin and end numbers have the same PatrnSeqCd
				if (laProcsInvDataOrig.getPatrnSeqCd()
					!= laIAUID.getPatrnSeqCd())
				{
					RTSException leRTSException = new RTSException(607);
					throw leRTSException;
				}

				lvProcsInvData =
					chkInvItmRange(laProcsInvDataOrig, laDBA);
			}
			else
			{
				laProcsInvDataOrig.setInvQty(1);
				laProcsInvDataOrig.setPatrnSeqCd(-1);
				laProcsInvDataOrig.setPatrnSeqNo(Integer.MIN_VALUE);
				laProcsInvDataOrig.setEndPatrnSeqNo(Integer.MIN_VALUE);
				lvProcsInvData = chkForPLP(laProcsInvDataOrig, laDBA);
			}

			// Check for complete range and then split
			if (lvProcsInvData.size() > CommonConstant.ELEMENT_0
				&& !ValidateInventoryPattern.chkIfItmPLPOrOLDPLTOrROP(
					laProcsInvData.getItmCd()))
			{
				chkForCompleteRngeAndSplt(
					laProcsInvDataOrig,
					lvProcsInvData,
					laDBA);
			}

			// Reselect ranges
			if (!ValidateInventoryPattern
				.chkIfItmPLPOrOLDPLTOrROP(laProcsInvData.getItmCd()))
			{
				lvProcsInvData =
					chkInvItmRange(laProcsInvDataOrig, laDBA);
			}
			else
			{
				lvProcsInvData = chkForPLP(laProcsInvDataOrig, laDBA);
			}

			// defect 9116
			// original if stmt
			//if (lvProcsInvData.size() > CommonConstant.ELEMENT_0
			//				&& laProcsInvData.getTransWsId() != null
			//				&& !laProcsInvData.getTransWsId().equals(
			//					CommonConstant.STR_SPACE_EMPTY))
			// For issue inventory, save the following values from the 
			// original data
			if (lvProcsInvData.size() > CommonConstant.ELEMENT_0
				&& laProcsInvData.getTransWsId() > -1)
			{
				// end defect 9116
				ProcessInventoryData lPID =
					(ProcessInventoryData) lvProcsInvData.get(
						CommonConstant.ELEMENT_0);
				lPID.setTransWsId(laProcsInvData.getTransWsId());
				lPID.setTransEmpId(laProcsInvData.getTransEmpId());
				lPID.setTransAMDate(laProcsInvData.getTransAMDate());
				lPID.setTransTime(laProcsInvData.getTransTime());
			}
			return lvProcsInvData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETINVRNGEINDB_END + csClientHost);
		}
	}

	/**
	 * Used to calculate the next item number given the inventory type 
	 * and begin item number.  
	 * 
	 * <p>If there is a validation exception, the 
	 * InvItmEndNo is set to Integer.MIN_VALUE.
	 *
	 * @return Object
	 * <p>Parameter aaData is the original inventory item.  
	 * The required fields are:
	 * <ul>
	 * <li>ItmCd    - The item code for the inventory
	 * <li>InvItmYr - The inventory year
	 * <li>InvItmNo - The inventory number for which the next 
	 * 				   number will be calculated.
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object getNextInvItmNo(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETNEXTINVITMNO_BEGIN + csClientHost);

		InventoryAllocationUIData laInvAlloctnUIData =
			(InventoryAllocationUIData) aaData;

		laInvAlloctnUIData.setInvQty(
			InventoryConstant.COMPUTE_NEXT_ITEM);
		laInvAlloctnUIData.setInvItmEndNo(
			CommonConstant.STR_SPACE_EMPTY);

		try
		{
			ValidateInventoryPattern laVIP =
				new ValidateInventoryPattern();
			laInvAlloctnUIData =
				(InventoryAllocationUIData) laVIP.calcInvUnknown(
					laInvAlloctnUIData);
		}
		catch (RTSException aeRTSEx)
		{
			laInvAlloctnUIData.setInvItmEndNo(
				String.valueOf(Integer.MIN_VALUE));
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETNEXTINVITMNO_END + csClientHost);
			return laInvAlloctnUIData;
		}
	}

	/**
	 * Returns the next available Virtual Item number for a specified
	 * Item Code.
	 * 
	 * <p>Throws an RTSException if there is no Virtual Inventory 
	 * available for the item code requested.  Error number is 
	 * ErrorsConstant.ERR_NUM_VI_NEXTITEM_UNAVAILABLE.
	 * 
	 * @param aaData Object (InventoryAllocationData) or (Vector)
	 * @return Object (InventoryAllocationData)
	 * @throws RTSException
	 */
	private Object getNextIVItmNo(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"getNextIVInvItmNo - Begin " + csClientHost);

		DatabaseAccess laDBA = null;

		// defect 10400
		boolean lbPassedDBA = false;

		InventoryAllocationData laInvAllocationData =
			new InventoryAllocationData();
		// end defect 10400

		// defect 9404
		// add logging 
		try
		{
			// defect 10400
			if (aaData instanceof InventoryAllocationData)
			{
				laInvAllocationData = (InventoryAllocationData) aaData;
			}
			else if (aaData instanceof Vector)
			{
				laDBA = (DatabaseAccess) ((Vector) aaData).elementAt(0);
				lbPassedDBA = true;
				laInvAllocationData =
					(InventoryAllocationData)
						((Vector) aaData).elementAt(
						1);
			}
			else
			{
				throw new RTSException(
					RTSException.FAILURE_VALIDATION,
					"Invalid object type",
					"Invalid object type");
			}
			// end defect 10400

			// return object	
			InventoryAllocationData laReturnData = null;

			// start the dba
			// If DBA can not start, no reason to do cache work.
			// USECASE GetNextVI VI Not Avail (DB Down)(5.2.1)
			// defect 10400
			if (!lbPassedDBA)
			{
				laDBA = new DatabaseAccess();
			}
			// end defect 10400
			//laDBA.beginTransaction();

			// USECASE GetNextVI Lookup VI Item Code (3.1.1)
			Vector lvPatterns =
				InventoryPatternsCache.getInvPatrns(
					laInvAllocationData.getItmCd(),
					laInvAllocationData.getInvItmYr());

			// check to see if any patterns contain an ISA
			boolean lbISAsExist =
				InventoryPatternsCache.containsIsaPatternAndVIItmCd(
					lvPatterns);

			for (int i = 0; i < lvPatterns.size(); i++)
			{
				// get the pattern
				InventoryPatternsData laInvPatternsData =
					(InventoryPatternsData) lvPatterns.elementAt(i);

				// only use the pattern if it has a VI Item Code
				if (laInvPatternsData.getVIItmCd() != null
					&& laInvPatternsData.getVIItmCd().trim().length() > 0
					&& !laInvPatternsData.getVIItmCd().equalsIgnoreCase(
						laInvPatternsData.getItmCd()))
				{
					// USECASE GetNextVI Match ISA indicators (3.2.4.1)
					// If there are ISAs with VI Itm Cds,
					// make sure that the request matches the pattern.
					// No need to check if there are no ISAs to worry 
					// about.
					if (lbISAsExist
						&& ((!laInvAllocationData.isISA()
							&& laInvPatternsData.getISAIndi() == 1)
							|| (laInvAllocationData.isISA()
								&& laInvPatternsData.getISAIndi() != 1)))
					{
						continue;
					}

					// setup the search data
					InventoryAllocationData laViRow =
						new InventoryAllocationData();
					laViRow.setItmCd(laInvPatternsData.getVIItmCd());

					// do not use year in the lookup.
					laViRow.setInvItmYr(0);

					laViRow.setISA(laInvAllocationData.isISA());

					// start the connection
					// defect 10400
					if (!lbPassedDBA)
					{
						laDBA.beginTransaction();
					}
					// end defect 10400
					//logUOW("getNextIVItmNo", "BEGIN");
					// setup sql class
					InventoryVirtual laVirtualInventory =
						new InventoryVirtual(laDBA);

					// USECASE GetNextVI Find next available Virtual Item (3.1.2)
					// get next item
					InventoryAllocationData laNewInventoryAllocData =
						laVirtualInventory.qryNextAvailableItem(
							laViRow,
							csClientHost);

					// check to see if we got anything
					if (laNewInventoryAllocData.getInvItmNo() == null)
					{
						Log.write(
							Log.DEBUG,
							laNewInventoryAllocData,
							"VI Item is Null "
								+ laInvAllocationData.getItmCd()
								+ " "
								+ laInvAllocationData.getTransEmpId()
								+ " "
								+ laInvAllocationData.getTransWsId()
								+ " "
								+ csClientHost);
						// RollBack any changes.  There are none, just releasing.
						// defect 10400
						if (!lbPassedDBA)
						{
							laDBA.endTransaction(
								DatabaseAccess.ROLLBACK);
						}
						// end defect 10400
						laVirtualInventory = null;
						// USECASE GetNextVI No item found (3.2.3.1)
						// If we did not get anything, we should just 
						// continue to the next pattern.
						continue;
					}

					Log.write(
						Log.DEBUG,
						laNewInventoryAllocData,
						"VI Item range is picked, lock the range "
							+ laInvAllocationData.getItmCd()
							+ " "
							+ laInvAllocationData.getTransEmpId()
							+ " "
							+ laInvAllocationData.getTransWsId()
							+ " "
							+ csClientHost);

					// put db2 exclusive lock on row
					laVirtualInventory
						.updInventoryVirtualForLogicalLock(
						laNewInventoryAllocData);

					// USECASE GetNextVI Place item on System Hold (3.1.5)
					// put the row on hold
					laNewInventoryAllocData.setInvStatusCd(
						InventoryConstant.HOLD_INV_SYSTEM);

					// copy over requestor information
					laNewInventoryAllocData.setItrntReq(
						laInvAllocationData.isItrntReq());
					laNewInventoryAllocData.setOfcIssuanceNo(
						laInvAllocationData.getOfcIssuanceNo());
					laNewInventoryAllocData.setTransAmDate(
						laInvAllocationData.getTransAmDate());
					laNewInventoryAllocData.setTransWsId(
						laInvAllocationData.getTransWsId());
					laNewInventoryAllocData.setTransEmpId(
						laInvAllocationData.getTransEmpId());
					laNewInventoryAllocData.setRequestorIpAddress(
						laInvAllocationData.getRequestorIpAddress());
					laNewInventoryAllocData.setRequestorRegPltNo(
						laInvAllocationData.getRequestorRegPltNo());

					// put it on hold.
					// this includes range split.
					updVIStatusCd(laDBA, laNewInventoryAllocData);

					// calculate new object to return here
					laReturnData =
						(InventoryAllocationData) UtilityMethods.copy(
							laNewInventoryAllocData);
					laReturnData.setViItmCd(
						laNewInventoryAllocData.getItmCd());
					laReturnData.setItmCd(
						laInvAllocationData.getItmCd());
					laReturnData.setInvItmYr(
						laInvAllocationData.getInvItmYr());
					laReturnData.setInvItmEndNo(
						laNewInventoryAllocData.getInvItmNo());
					laReturnData.setInvQty(1);
					laReturnData.setPatrnSeqCd(-1);
					laReturnData.setPatrnSeqNo(0);
					laReturnData.setCalcInv(false);

					// USECASE GetNextVI Calculate new Item (3.1.6)
					ValidateInventoryPattern laValidateInvPattern =
						new ValidateInventoryPattern();

					try
					{
						laReturnData =
							(
								InventoryAllocationData) laValidateInvPattern
									.calcInvUnknownVI(
								laReturnData);

						// defect 10400
						if (!lbPassedDBA)
						{
							// end defect 10400
							// Do not commit until we calculate the 
							// new item.
							laDBA.endTransaction(DatabaseAccess.COMMIT);
							//logUOW("getNextIVItmNo", "COMMIT");
							laDBA = null;
							// defect 10400
						}
						// end defect 10400

						// USECASE GetNextVI Return to User (3.1.7)
						return laReturnData;
					}
					catch (RTSException aeRTSEx)
					{
						Log.write(
							Log.DEBUG,
							laNewInventoryAllocData,
							"VI GetNext had a problem "
								+ aeRTSEx.getCode()
								+ " "
								+ laInvAllocationData.getItmCd()
								+ " "
								+ laInvAllocationData.getTransEmpId()
								+ " "
								+ laInvAllocationData.getTransWsId()
								+ " "
								+ csClientHost);

						// USECASE GetNextVI Rollback record to try 
						// another one.  (3.2.6.1)
						// Rollback the row and then continue
						laDBA.endTransaction(DatabaseAccess.ROLLBACK);
						//logUOW("getNextIVItmNo", "ROLLBACK");
						laDBA = null;
						laDBA = new DatabaseAccess();
						//laDBA.beginTransaction();

						// if the problem is an 11, do not throw back 
						// yet.  Let's try it again.  Different pattern.
						if (aeRTSEx.getCode()
							!= ErrorsConstant
								.ERR_NUM_COMPUTED_NUMBER_INVALID)
						{
							throw aeRTSEx;
						}
					}
				}
				else
				{
					continue;
				}
			} // end for loop

			// USECASE GetNextVI No VI Match (3.2.2.1) 
			// if we got to this point, no vi was found
			// write an entry to admin log documenting Missing VI GetNext
			// defect 10400
			// We should only start trans if not a passed DBA.
			if (!lbPassedDBA)
			{
				laDBA.beginTransaction();
			}
			// end defect 10400
			AdministrationLogData laLogData =
				new AdministrationLogData();
			laLogData.setAction("VI GETNEXT");
			laLogData.setEntity(laInvAllocationData.getItmCd());
			laLogData.setEntityValue(
				laInvAllocationData.getInvItmNo()
					+ TXT_DASH_WITH_SPACES
					+ laInvAllocationData.getInvItmEndNo());
			laLogData.setOfcIssuanceNo(
				laInvAllocationData.getOfcIssuanceNo());
			laLogData.setSubStaId(laInvAllocationData.getSubstaId());
			laLogData.setTransAMDate(
				RTSDate.getCurrentDate().getAMDate());
			laLogData.setTransEmpId(
				laInvAllocationData.getTransEmpId());
			laLogData.setTransTime(
				RTSDate.getCurrentDate().get24HrTime());
			laLogData.setTransTimestmp(RTSDate.getCurrentDate());
			laLogData.setTransWsId(laInvAllocationData.getTransWsId());

			AdministrationLog lDbAL = new AdministrationLog(laDBA);
			lDbAL.insAdministrationLog(laLogData);

			// USECASE GetNextVI No VI Match (3.2.2.2)
			// throw the no vi exception
			throw new RTSException(
				ErrorsConstant.ERR_NUM_VI_NEXTITEM_UNAVAILABLE);

		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			// defect 10400
			if (!lbPassedDBA)
			{
				// if dba is there, roll it back
				if (laDBA != null)
				{
					laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				}

				// set to null before leaving.
				laDBA = null;
			}
			// end defect 10400
			Log.write(
				Log.METHOD,
				this,
				"getNextIVInvItmNo - End   " + csClientHost);
		}
		// end defect 9404 
	}

	/**
	 * Returns all counties and their last insert date given an 
	 * OfficeIssuanceNumber.
	 * 
	 * <p>Parameter aaData is the search parameters in the form of 
	 * InventoryHistoryLogData. 
	 * The required field is as follows:
	 * <ul>
	 * <li>OfcIssuanceNo - The office issuance number
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Vector
	 * @throws RTSException  
	 */
	private Vector getRegionalCounties(Object aaData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETREGIONALCOUNTIES_BEGIN + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();

		InventoryHistoryLogData laInvHistoryLogData =
			(InventoryHistoryLogData) aaData;
		Vector lvRetData = new Vector();

		try
		{
			laDBA.beginTransaction();

			InventoryHistoryLog laInvHistoryLog =
				new InventoryHistoryLog(laDBA);
			lvRetData =
				laInvHistoryLog.qryInventoryHistoryLog(
					laInvHistoryLogData);

			laDBA.endTransaction(DatabaseAccess.NONE);

			return lvRetData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.NONE);

			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETREGIONALCOUNTIES_END + csClientHost);
		}
	}

	/**
	 * Returns all the subcontractors given SubstationId and 
	 * OfficeIssuanceNumber.
	 *
	 * <p>Parameter aaData is the search parameters in the form of 
	 * GeneralSearchData.  The required fields are as follows:
	 * <ul>
	 * <li>IntKey(1) - The office issuance number
	 * <li>IntKey(2) - The substation id
	 * <eul>
	 * 
	 * @param aaData Object
	 * @param aaDBA DatabaseAccess The database connection
	 * @return Vector
	 * @throws RTSException  
	 */
	private Vector getSubcons(Object aaData, DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETSUBCONS_BEGIN + csClientHost);

		Vector lvSubconData = new Vector();
		try
		{
			GeneralSearchData laGSData = (GeneralSearchData) aaData;
			Subcontractor laDbSubcon = new Subcontractor(aaDBA);
			SubcontractorData laSubconData = new SubcontractorData();

			laSubconData.setOfcIssuanceNo(laGSData.getIntKey1());
			laSubconData.setSubstaId(laGSData.getIntKey2());
			// defect 10161 
			laSubconData.setId(Integer.MIN_VALUE);
			// end defect 10161 
			lvSubconData = laDbSubcon.qrySubcontractor(laSubconData);

			return lvSubconData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETSUBCONS_END + csClientHost + csClientHost);
		}
	}

	/**
	 * Returns all the substations given SubstationId and 
	 * OfficeIssuanceNumber.
	 *
	 * <p>Parameter aaData is the search parameters in the form of 
	 * GeneralSearchData.  The required fields are as follows:
	 * <ul>
	 * <li>IntKey(1) - The office issuance number
	 * <eul>
	 * 
	 * @param aaData Object
	 * @param aaDBA DatabaseAccess 
	 * @return Vector
	 * @throws RTSException 
	 */
	private Vector getSubsta(Object aaData, DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_GETSUBSTA_BEGIN + csClientHost);

		Vector lvSubstaData = new Vector();
		try
		{
			GeneralSearchData laGSData = (GeneralSearchData) aaData;
			Substation laDbSubsta = new Substation(aaDBA);
			SubstationData laSubstaData = new SubstationData();

			laSubstaData.setOfcIssuanceNo(laGSData.getIntKey1());
			lvSubstaData = laDbSubsta.qrySubstation(laSubstaData);

			if (lvSubstaData.size() == CommonConstant.ELEMENT_0)
			{
				RTSException leRTSEx = new RTSException(182);
				throw leRTSEx;
			}
			return lvSubstaData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETSUBSTA_END + csClientHost);
		}
	}

	/**
	 * Retrieve the substation data required for screens INV004 and 
	 * INV021.
	 *
	 * <p>Parameter aaData has the search parameters in the form of 
	 * GeneralSearchData.  The required fields are as follows:
	 * <ul>
	 * <li>IntKey(1) - The office issuance number
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Vector
	 * @throws RTSException  
	 */
	private Vector getSubstaDispData(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_GETSUBSTADISPDATA_BEGIN + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvSubstaData = new Vector();
		try
		{
			laDBA.beginTransaction();

			// defect 8849
			// retry if we get a stale connection
			for (int i = 0; i < 2; i++)
			{
				try
				{
					lvSubstaData.addAll(getSubsta(aaData, laDBA));
					break;
				}
				catch (RTSException aeRTSEx)
				{
					if (aeRTSEx.getDetailMsg() != null
						&& aeRTSEx.getDetailMsg().indexOf(
							"StaleConnection")
							> -1)
					{
						// add the client host to the message detail
						aeRTSEx.setDetailMsg(
							aeRTSEx.getDetailMsg()
								+ CommonConstant.SYSTEM_LINE_SEPARATOR
								+ "Client "
								+ csClientHost);

						// defect 8869
						// log the StaleConnection
						aeRTSEx.writeExceptionToLog();
						// end defect 8869

						// re-establish connection.
						laDBA = new DatabaseAccess();
						laDBA.beginTransaction();
					}
					else
					{
						throw aeRTSEx;
					}
				}

			}
			// end defect 8849

			laDBA.endTransaction(DatabaseAccess.NONE);

			return lvSubstaData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETSUBSTADISPDATA_END + csClientHost);
		}
	}

	/**
	 * Returns all the workstations given SubstationId and 
	 * OfficeIssuanceNumber.
	 *
	 * <p>Paramter aaData has the search parameters in the form of 
	 * GeneralSearchData.  The required fields are as follows:
	 * <ul>
	 * <li>IntKey(1) - The office issuance number
	 * <li>IntKey(2) - The substation id
	 * <eul>
	 * 
	 * @param aaData Object
	 * @param aaDBA DatabaseAccess 
	 * @return Vector
	 * @throws RTSException  
	 */
	private Vector getWsIds(Object aaData, DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_GETWSIDS_BEGIN + csClientHost);

		Vector lvWsData = new Vector();
		boolean lbIsDBAPassed = true;

		try
		{
			// create dba is it was not passed
			if (aaDBA == null)
			{
				aaDBA = new DatabaseAccess();
				aaDBA.beginTransaction();
				lbIsDBAPassed = false;
			}

			GeneralSearchData laGSData = (GeneralSearchData) aaData;
			AssignedWorkstationIds laDbWs =
				new AssignedWorkstationIds(aaDBA);
			AssignedWorkstationIdsData laWsData =
				new AssignedWorkstationIdsData();

			laWsData.setOfcIssuanceNo(laGSData.getIntKey1());
			laWsData.setSubstaId(laGSData.getIntKey2());
			laWsData.setWsId(Integer.MIN_VALUE);
			lvWsData = laDbWs.qryAssignedWorkstationIds(laWsData);

			if (!lbIsDBAPassed)
			{
				aaDBA.endTransaction(DatabaseAccess.NONE);
			}

			return lvWsData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_GETWSIDS_END + csClientHost);
		}
	}
	/** 
	 * Init ReportProperties
	 * 
	 * @param aaDBAccess
	 * @param aaFundsData
	 * @return ReportProperties
	 * @throws RTSException
	 */
	private ReportProperties initReportProperties(
		ReportSearchData aaRptSearchData,
		DatabaseAccess aaDBAccess,
		String asReportId)
		throws RTSException
	{
		ReportsServerBusiness laRptSrvrBus =
			new ReportsServerBusiness();

		return laRptSrvrBus.initReportProperties(
			aaRptSearchData,
			aaDBAccess,
			asReportId);
	}

	/**
	 * Insert a Special Plate Rejection Log row.
	 * 
	 * @param aaData InventoryAllocationData
	 * @param aaDBA DatabaseAccess
	 * @throws RTSException
	 */
	private void insrtSpRejLog(
		InventoryAllocationData aaData,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		SpecialPlateRejectionLogData laSpRLData =
			new SpecialPlateRejectionLogData();
		laSpRLData.setInvItmNo(aaData.getInvItmNo());
		laSpRLData.setMfgPltNo(aaData.getMfgPltNo());
		laSpRLData.setItrntReqIndi(aaData.isItrntReq());
		laSpRLData.setRegPltNo(aaData.getRequestorRegPltNo());
		laSpRLData.setReqIPAddr(aaData.getRequestorIpAddress());
		laSpRLData.setOfcIssuanceNo(aaData.getOfcIssuanceNo());
		laSpRLData.setTransWsId(aaData.getTransWsId());
		laSpRLData.setTransAMDate(aaData.getTransAmDate());
		laSpRLData.setTransEmpId(aaData.getTransEmpId());
		laSpRLData.setRejReasnCd(aaData.getErrorCode());

		SpecialPlateRejectionLog laSPRLSql =
			new SpecialPlateRejectionLog(aaDBA);
		laSPRLSql.insSpecialPlateRejectionLog(laSpRLData);
	}

	/**
	 * Inserts the provided InventoryAllocationData object into the 
	 * database.
	 * 
	 * @param aaDBA	DatabaseAccess
	 * @param aaData Object (InventoryAllocationData)
	 * @return Object
	 * @throws RTSException
	 */
	private Object insrtViRow(DatabaseAccess aaDBA, Object aaData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insrtViRow - Begin " + csClientHost);
		DatabaseAccess laDBA = null;
		// defect 9404 
		// add logging 
		try
		{
			if (aaDBA == null)
			{
				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();
				//logUOW("insrtViRow", "BEGIN");
			}
			else
			{
				laDBA = aaDBA;
			}

			InventoryAllocationData laIAD = null;
			if (aaData instanceof InventoryAllocationData)
			{
				laIAD = (InventoryAllocationData) aaData;
			}
			else
			{
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WRONG_OBJECT_TYPE);
			}

			// if this item is not calculated, do it now.
			if (!laIAD.isCalcInv())
			{
				ValidateInventoryPattern laVIP =
					new ValidateInventoryPattern();
				laIAD =
					(InventoryAllocationData) laVIP.calcInvUnknownVI(
						laIAD);
			}

			InventoryVirtual laIV = new InventoryVirtual(laDBA);

			// if the plate is user defined, attempt a hoops conversion
			// before checking for intersections.
			if (laIAD.isUserPltNo())
			{
				laIAD.setHoopsRegPltNo(
					CommonValidations.convert_i_and_o_to_1_and_0(
						laIAD.getInvItmNo()));
			}

			// check for intersections before inserting
			Vector lvIntersections =
				laIV.qryInventoryVirtualIntersection(laIAD);
			if (lvIntersections.size() > 0)
			{
				throw new RTSException(
					ErrorsConstant.ERR_NUM_VI_INTERSECTION);
			}

			laIV.insInventoryVirtual(laIAD, csClientHost);

			if (aaDBA == null)
			{
				laDBA.endTransaction(DatabaseAccess.COMMIT);
				//logUOW("insrtViRow", "COMMIT");
				laDBA = null;
			}

			return aaData;
		}
		catch (RTSException aeRTSEx)
		{	 
			// defect 11180
			if (aaDBA == null && laDBA != null)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("insrtViRow", "ROLLBACK");
				//if (aaDBA == null)
				//{
					laDBA = null;
				//}
			}
			// end defect 11180
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				"insrtViRow - End " + csClientHost);
		}
		// end defect 9404 
	}

	/**
	 * Used to automatically issue inventory in a transaction.  First 
	 * checks the inventory profiles to determine if whether or not to 
	 * "prompt" (automatically issue) inventory.  If should prompt, then 
	 * check to see if the workstation or employee has any inventory 
	 * allocated to them and place the next inventory item on hold.
	 *
	 * <p>Parameter aaData is the complete transaction 
	 * 		  data object that is being used in the transaction.  
	 *        The InvItms vector has to contain the the inventory items 
	 *        that need to be issued for this transaction as 
	 *        ProcessInventoryData objects.  The required fields in each 
	 *        object are:
	 * <ul>
	 * <li>OfcIssuanceNo - The current office issuance number
	 * <li>SubstaId - The current substation id
	 * <li>ItmCd - The item code for the inventory to be issued
	 * <li>InvItmYr - The inventory year for the inventory to be issued
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object issueInv(Object aaData) throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_ISSUEINV_BEGIN + csClientHost);

		CompleteTransactionData laCmpltTransData =
			(CompleteTransactionData) aaData;
		Vector lvProcsInvData = laCmpltTransData.getInvItms();
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvProcsInvDataReturn = new Vector();

		try
		{
			laDBA.beginTransaction();

			for (int i = 0; i < lvProcsInvData.size(); i++)
			{

				ProcessInventoryData laProcsInvData =
					(ProcessInventoryData) lvProcsInvData.get(i);

				if (StickerPrintingUtilities
					.isStickerPrintable(laProcsInvData))
				{
					continue;
				}

				// Check profiles to see if should prompt for next 
				// available inventory item
				Integer laNextAvailIndi =
					chkNextAvailIndi(laProcsInvData, laDBA);

				// If should prompt for next inv itm, get the next inv 
				// itm and put it on hold
				if (laNextAvailIndi.intValue() == 1)
				{
					if (!ValidateInventoryPattern
						.chkIfItmPLPOrOLDPLTOrROP(
							laProcsInvData.getItmCd()))
					{
						// Item is a patterned inv item
						// Make call to Inventory Allocation table
						ProcessInventorySQL laProcsInvSQL =
							new ProcessInventorySQL(laDBA);

						// This handles the case during DTA when the 
						// inventory item number is already filled in.
						if (laProcsInvData.getInvItmNo() == null
							|| laProcsInvData.getInvItmNo().equals(
								CommonConstant.STR_SPACE_EMPTY))
						{
							laProcsInvData =
								laProcsInvSQL
									.qryNextAvailableInventory(
									laProcsInvData);

							// call update to lock this range
							// defect 9558
							if (laProcsInvData.getInvItmNo() != null)
							{
								InventoryAllocation laInventoryAllocation =
									new InventoryAllocation(laDBA);
								laInventoryAllocation
									.updInventoryAllocationForLogicalLock(
									laProcsInvData
										.convertToInvAlloctnUIData(
										laProcsInvData));
							}
							// end defect 9558
						}

						// for DTA
						if (laProcsInvData.getInvItmNo() != null)
						{
							laProcsInvData.setInvQty(1);
							laProcsInvData.setInvStatusCd(
								InventoryConstant.HOLD_INV_SYSTEM);
							try
							{
								Vector lvVct =
									updInvStatusCd(
										laDBA,
										laProcsInvData);
								if (laCmpltTransData
									.getTransCode()
									.startsWith(TRANS_DTA_PREFIX))
								{
									// make sure it's the same dealer 
									// before allocating it to them
									ProcessInventoryData laPID =
										(
											ProcessInventoryData) lvVct
												.get(
											CommonConstant.ELEMENT_0);
									if (laPID
										.getInvLocIdCd()
										.equals(
											InventoryConstant.CHAR_D)
										&& laCmpltTransData
											.getDlrTtlData()
											!= null // defect 10290 											 
									//laCmpltTransData.getDlrTtlDataObjs()
									//!= null
									// && laCmpltTransData
									//	.getDlrTtlDataObjs()
									//	.size()
									//  > 0
									//	&& (
									//	(DealerTitleData) laCmpltTransData
									//	getDlrTtlDataObjs()
									//	.get(0))
									//	.getDealerId()
									// end defect 10290 
										&& laCmpltTransData
											.getDlrTtlData()
											.getDealerId()
											== Integer.parseInt(
												laPID.getInvId()))
									{
										lvProcsInvDataReturn.add(
											(
												ProcessInventoryData) lvVct
													.get(
												CommonConstant
													.ELEMENT_0));
									}
									else
									{
										laPID.setInvStatusCd(
											InventoryConstant
												.HOLD_INV_NOT);
										updInvStatusCd(laDBA, laPID);
									}
								}
								else
								{
									lvProcsInvDataReturn.add(
										(
											ProcessInventoryData) lvVct
												.get(
											CommonConstant.ELEMENT_0));
								}

							}
							catch (RTSException aeRTSEx)
							{
								// do not process
								if (aeRTSEx.getCode() > 0)
								{
									System.out.println(
										"Error Code "
											+ aeRTSEx.getCode());
								}

								// defect 9116
								// this section gets Special app plate added
								// even though it is not in InvAlloc
								if (laProcsInvData.getInvLocIdCd()
									!= null
									&& laProcsInvData
										.getInvLocIdCd()
										.equals(
										"U")
									&& aeRTSEx.getCode()
										== ErrorsConstant
											.ERR_NUM_NO_RECORDS_IN_DB)
								{
									lvProcsInvDataReturn.add(
										laProcsInvData);
								}
								// defect 9116
							}
						}
					}
					else
					{
						// Make call to Inventory Allocation table for 
						// specific item
						InventoryAllocation laInvAlloctnSQL =
							new InventoryAllocation(laDBA);

						// This handles the case during DTA when the 
						// inventory item number is already filled in.
						if (laProcsInvData.getInvItmNo() == null
							|| laProcsInvData.getInvItmNo().equals(
								CommonConstant.STR_SPACE_EMPTY))
						{
							laProcsInvData.setPatrnSeqNo(
								Integer.MIN_VALUE);
							Vector lvVct =
								laInvAlloctnSQL
									.qryInventoryAllocationForSpecificItem(
									laProcsInvData);
							if (lvVct.size()
								> CommonConstant.ELEMENT_0)
							{
								laProcsInvData =
									(ProcessInventoryData) lvVct.get(
										CommonConstant.ELEMENT_0);
							}

							// call update to lock this range
							// defect 9558
							if (laProcsInvData.getInvItmNo() != null)
							{
								InventoryAllocation laInventoryAllocation =
									new InventoryAllocation(laDBA);
								laInventoryAllocation
									.updInventoryAllocationForLogicalLock(
									laProcsInvData
										.convertToInvAlloctnUIData(
										laProcsInvData));
							}
							// end defect 9558
						}

						if (laProcsInvData.getInvItmNo() != null)
						{
							laProcsInvData.setInvQty(1);
							laProcsInvData.setInvStatusCd(
								InventoryConstant.HOLD_INV_SYSTEM);
							try
							{
								Vector lvVct =
									updInvStatusCd(
										laDBA,
										laProcsInvData);
								if (laCmpltTransData
									.getTransCode()
									.startsWith(TRANS_DTA_PREFIX))
								{
									// make sure it's the same dealer 
									//before allocating it to them
									ProcessInventoryData laPID =
										(
											ProcessInventoryData) lvVct
												.get(
											CommonConstant.ELEMENT_0);
									if (laPID
										.getInvLocIdCd()
										.equals(
											InventoryConstant.CHAR_D)
										&& laCmpltTransData
											.getDlrTtlData()
											!= null //	&& laCmpltTransData
									//	.getDlrTtlDataObjs()
									//	.size()
									//  > 0
									//	&& (
									//	(DealerTitleData) laCmpltTransData
									//	getDlrTtlDataObjs()
									//	.get(0))
									//	.getDealerId()
										&& laCmpltTransData
											.getDlrTtlData()
											.getDealerId()
											== Integer.parseInt(
												laPID.getInvId()))
									{
										lvProcsInvDataReturn.add(
											(
												ProcessInventoryData) lvVct
													.get(
												CommonConstant
													.ELEMENT_0));
									}
									else
									{
										laPID.setInvStatusCd(
											InventoryConstant
												.HOLD_INV_NOT);
										updInvStatusCd(laDBA, laPID);
									}
								}
								else
								{
									lvProcsInvDataReturn.add(
										(
											ProcessInventoryData) lvVct
												.get(
											CommonConstant.ELEMENT_0));
								}
							}
							catch (RTSException aeRTSEx)
							{
								// defect 9116
								// this section gets Special app plate added
								// even though it is not in InvAlloc
								if (laProcsInvData.getInvLocIdCd()
									!= null
									&& laProcsInvData
										.getInvLocIdCd()
										.equals(
										"U")
									&& aeRTSEx.getCode()
										== ErrorsConstant
											.ERR_NUM_NO_RECORDS_IN_DB)
								{
									lvProcsInvDataReturn.add(
										laProcsInvData);
								}
								// end defect 9116
							}
						}
					}
				}
				else
				{
					continue;
				}
			}
			laCmpltTransData.setAllocInvItms(lvProcsInvDataReturn);

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return laCmpltTransData;
			// defect 9116
		}
		catch (NullPointerException aeNPEx)
		{
			// if we get a null pointer, make sure we do a rollback
			throw new RTSException(RTSException.SERVER_DOWN, aeNPEx);
		}
		catch (RTSException aeRTSEx)
		{
			// end defect 9116
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			laDBA = null;
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_ISSUEINV_END + csClientHost);
		}
	}
	/** 
	 * Log Unit of Work
	 * 
	 * @param asMethodName
	 * @param asPosUOW
	 * @deprecated   
	 */
	private void logUOW(String asMethodName, String asPosUOW)
	{
		String lsString = "VI_METHOD: " + asMethodName + " " + asPosUOW;
		//System.out.println(lsString);

		Log.write(Log.SQL_EXCP, this, lsString);
	}

	/**
	 * Given an InventoryAllocationData object, figure out what 
	 * Virtual Inventory Item Code matches it.
	 * 
	 * @param aaIAD InventoryAllocationData
	 * @return String
	 * @throws RTSException
	 */
	private String lookupVirtualForItem(InventoryAllocationData aaIAD)
		throws RTSException
	{
		try
		{
			String lsReturnItmCd = "";

			// defect 9679
			// add a check to block empty itmcd's from Vendor Plates ws.
			if (aaIAD.getItmCd() == null
				|| aaIAD.getItmCd().trim().length() < 1)
			{
				lsReturnItmCd = null;
			}
			else if (aaIAD.isUserPltNo())
			{
				// end defect 9679
				// if it is personalized, return the item code
				lsReturnItmCd = aaIAD.getItmCd();
			}
			else
			{
				// check to see if this is a Virtual Item code first
				ItemCodesData laICD =
					ItemCodesCache.getItmCd(aaIAD.getItmCd());
				if (laICD.getItmTrckngType().equals("V"))
				{
					// this is a Virtual Item Code
					lsReturnItmCd = aaIAD.getItmCd();
				}
				else
				{
					// We need to see if we can look it up.
					Vector lvPatterns =
						InventoryPatternsCache.getInvPatrns(
							aaIAD.getItmCd(),
							aaIAD.getInvItmYr());
					if (lvPatterns != null)
					{
						for (Iterator laIter = lvPatterns.iterator();
							laIter.hasNext();
							)
						{
							InventoryPatternsData laIPD =
								(InventoryPatternsData) laIter.next();
							if (laIPD.getVIItmCd() != null)
							{
								// copy the object over for calculation.
								InventoryAllocationData laTempIAD =
									(
										InventoryAllocationData) UtilityMethods
											.copy(
										aaIAD);
								laTempIAD.setItmCd(laIPD.getVIItmCd());
								laTempIAD.setViItmCd(
									laIPD.getVIItmCd());

								// TODO Do not set to 0 if there is no VI Itm
								// If we are looking up a VI Item Code, 
								// set the year to 0.
								// Otherwise, use the passed year.
								// The pattern being used should determine 
								// if matchup criteria. 
								if (!laIPD
									.getItmCd()
									.equals(laIPD.getVIItmCd()))
								{
									laTempIAD.setInvItmYr(0);
								}
								else
								{
									laTempIAD.setInvItmYr(
										laIPD.getInvItmYr());
								}

								ValidateInventoryPattern laVIP =
									new ValidateInventoryPattern();

								try
								{
									laVIP.calcInvUnknownVI(laTempIAD);

									// if we are using the normal item code
									// the pattern seq code should match as
									// well.
									if (aaIAD
										.getItmCd()
										.equals(laTempIAD.getViItmCd()))
									{
										// Go ahead and set this in case 
										// there are no other matches.
										lsReturnItmCd =
											laTempIAD.getViItmCd();

										// if the seq cd does not match or 
										// if not calculated, keep looking.
										if (aaIAD.getPatrnSeqCd()
											!= laTempIAD.getPatrnSeqCd()
											|| !aaIAD.isCalcInv())
										{
											continue;
										}
									}

									// calculation was successful, return this string.
									lsReturnItmCd =
										laTempIAD.getViItmCd();
									break;
								}
								catch (RTSException aeRTSEx)
								{
									// do nothing.
									// just try the next one.
								}
							}
						}
					}
					else
					{
						throw new RTSException(
							ErrorsConstant
								.ERR_NUM_VI_NEXTITEM_UNAVAILABLE);
					}
				}
			}

			// return the data if exists.
			// otherwise, throw an exception.
			if (lsReturnItmCd != null && lsReturnItmCd.length() > 0)
			{
				return lsReturnItmCd;
			}
			else
			{
				// throw the exception documenting that there is no VI for item code
				throw new RTSException(
					ErrorsConstant.ERR_NUM_VI_NEXTITEM_UNAVAILABLE);
			}
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
	}

	/**
		 * Used to break up inventory ranges in the RTS_INV_ALLOCATION table 
		 * by updating the original row and inserting one or two additional 
		 * rows depending on how the range needs to be broken up.
		 *
		 * @param Vector A vector where the elements have to be as follows:
		 *     <br>Element(0) - The row that needs to be updated as an 
		 * 						ProcessInventoryData object.
		 *     <br>Element(1) - The new row that needs to be inserted as an 
		 *                      ProcessInventoryData object.
		 *     <br>Element(2) - The second row, if needed, that needs to be 
		 * 						inserted as an ProcessInventoryData object.
		 * @param aaDBA DatabaseAccess The database connection
		 * @throws RTSException  
		 */
	private void performUpdateInsertForAlloctn(
		Vector avProcsInvData,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_PERFORMUPDATEINSERTFORALLOCTN_BEGIN + csClientHost);

		ProcessInventoryData laUpdateProcsInvData =
			new ProcessInventoryData();
		ProcessInventoryData laInsertProcsInvData1 =
			new ProcessInventoryData();
		ProcessInventoryData laInsertProcsInvData2 =
			new ProcessInventoryData();
		InventoryAllocationData laUpdateInvAlloctn =
			new InventoryAllocationData();
		InventoryAllocationData laInsertInvAlloctn1 =
			new InventoryAllocationData();
		InventoryAllocationData laInsertInvAlloctn2 =
			new InventoryAllocationData();

		try
		{
			// Prepare the data objects for the update and insert(s)
			laUpdateProcsInvData =
				(ProcessInventoryData) avProcsInvData.get(
					CommonConstant.ELEMENT_0);
			laInsertProcsInvData1 =
				(ProcessInventoryData) avProcsInvData.get(
					CommonConstant.ELEMENT_1);
			if (avProcsInvData.size() > CommonConstant.ELEMENT_2)
			{
				laInsertProcsInvData2 =
					(ProcessInventoryData) avProcsInvData.get(
						CommonConstant.ELEMENT_2);
			}

			laUpdateInvAlloctn =
				laUpdateProcsInvData.convertToInvAlloctnUIData(
					laUpdateProcsInvData);
			laInsertInvAlloctn1 =
				laInsertProcsInvData1.convertToInvAlloctnUIData(
					laInsertProcsInvData1);
			if (avProcsInvData.size() > CommonConstant.ELEMENT_2)
			{
				laInsertInvAlloctn2 =
					laInsertProcsInvData2.convertToInvAlloctnUIData(
						laInsertProcsInvData2);
			}

			// Aren't changing the substaid so set the NewSubstaId = 
			// Integer.Min_Value
			laUpdateInvAlloctn.setNewSubstaId(Integer.MIN_VALUE);

			InventoryAllocation laInvAlloctnSQL =
				new InventoryAllocation(aaDBA);

			// Update the inventory row
			laInvAlloctnSQL.updInventoryAllocation(laUpdateInvAlloctn);

			// Insert the first new inventory row
			laInvAlloctnSQL.insInventoryAllocation(laInsertInvAlloctn1);

			// If it's not empty, insert the second new inventory row
			if (avProcsInvData.size() > CommonConstant.ELEMENT_2)
			{
				laInvAlloctnSQL.insInventoryAllocation(
					laInsertInvAlloctn2);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aaDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_PERFORMUPDATEINSERTFORALLOCTN_END + csClientHost);
		}
	}

	/**
	 * Used to break up inventory ranges in the RTS_INv_Virtual table 
	 * by updating the original row and inserting one or two additional 
	 * rows depending on how the range needs to be broken up.
	 *
	 * @param Vector A vector where the elements have to be as follows:
	 *     <br>Element(0) - The row that needs to be updated as an 
	 * 						InventoryAllocationData object.
	 *     <br>Element(1) - The new row that needs to be inserted as an 
	 *                      InventoryAllocationData object.
	 *     <br>Element(2) - The second row, if needed, that needs to be 
	 * 						inserted as an InventoryAllocationData object.
	 * @param aaDBA DatabaseAccess The database connection
	 * @throws RTSException  
	 */
	private void performUpdateInsertForVI(
		Vector avInvAllocData,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_PERFORMUPDATEINSERTFORALLOCTN_BEGIN + csClientHost);

		InventoryAllocationData laUpdateInvAllocData =
			new InventoryAllocationData();
		InventoryAllocationData laInsertInvAllocData1 =
			new InventoryAllocationData();
		InventoryAllocationData laInsertInvAllocData2 =
			new InventoryAllocationData();
		//		InventoryAllocationData laUpdateInvAlloctn =
		//			new InventoryAllocationData();
		//		InventoryAllocationData laInsertInvAlloctn1 =
		//			new InventoryAllocationData();
		//		InventoryAllocationData laInsertInvAlloctn2 =
		//			new InventoryAllocationData();

		try
		{
			// Prepare the data objects for the update and insert(s)
			laUpdateInvAllocData =
				(InventoryAllocationData) avInvAllocData.get(
					CommonConstant.ELEMENT_0);
			laInsertInvAllocData1 =
				(InventoryAllocationData) avInvAllocData.get(
					CommonConstant.ELEMENT_1);
			if (avInvAllocData.size() > CommonConstant.ELEMENT_2)
			{
				laInsertInvAllocData2 =
					(InventoryAllocationData) avInvAllocData.get(
						CommonConstant.ELEMENT_2);
			}

			//			laUpdateInvAlloctn =
			//				laUpdateProcsInvData.convertToInvAlloctnUIData(
			//					laUpdateProcsInvData);
			//			laInsertInvAlloctn1 =
			//				laInsertProcsInvData1.convertToInvAlloctnUIData(
			//					laInsertProcsInvData1);
			//			if (avInvAllocData.size() > CommonConstant.ELEMENT_2)
			//			{
			//				laInsertInvAlloctn2 =
			//					laInsertProcsInvData2.convertToInvAlloctnUIData(
			//						laInsertProcsInvData2);
			//			}

			// Aren't changing the substaid so set the NewSubstaId = 
			// Integer.Min_Value
			//			laUpdateInvAlloctn.setNewSubstaId(Integer.MIN_VALUE);
			laUpdateInvAllocData.setNewSubstaId(Integer.MIN_VALUE);

			InventoryVirtual laInvAlloctnSQL =
				new InventoryVirtual(aaDBA);

			// Update the inventory row
			laInvAlloctnSQL.updInventoryVirtual(
				laUpdateInvAllocData,
				csClientHost);

			// Insert the first new inventory row
			laInvAlloctnSQL.insInventoryVirtual(
				laInsertInvAllocData1,
				csClientHost);

			// If it's not empty, insert the second new inventory row
			if (avInvAllocData.size() > CommonConstant.ELEMENT_2)
			{
				laInvAlloctnSQL.insInventoryVirtual(
					laInsertInvAllocData2,
					csClientHost);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aaDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_PERFORMUPDATEINSERTFORALLOCTN_END + csClientHost);
		}
	}

	/**
	 * Used to call the private methods of the inventory server business.
	 *
	 * @param aiModule int 
	 * @param aiFunctionId int 
	 * @param aaData Object 
	 * @return Object
	 * @exception RTSException 
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		try
		{
			switch (aiFunctionId)
			{
				case InventoryConstant.ALLOC_INVENTORY_ITEMS :
					{
						return allocInvItms(aaData);
					}
				case InventoryConstant
					.RETRIEVE_ALLOCATION_DISPLAY_DATA :
					{
						return getAlloctnDispData(aaData);
					}
				case InventoryConstant.RETRIEVE_SUBSTATION_DATA :
					{
						return getSubstaDispData(aaData);
					}
				case InventoryConstant.GET_INVENTORY_FROM_MF :
					{
						return getInvFromMF(aaData);
					}
				case InventoryConstant
					.RETRIEVE_INVENTORY_PROFILE_DISPLAY_DATA :
					{
						return getInvProfileDispData(aaData);
					}
				case InventoryConstant.GET_INVENTORY_PROFILE :
					{
						return getInvProfile(aaData, null);
					}
				case InventoryConstant.ADD_INVENTORY_PROFILE :
					{
						addInvProfile((Vector) aaData);
						return aaData;
					}
				case InventoryConstant.DELETE_INVENTORY_PROFILE :
					{
						delInvProfile((Vector) aaData);
						return aaData;
					}
				case InventoryConstant.REVISE_INVENTORY_PROFILE :
					{
						updInvProfile((Vector) aaData);
						return aaData;
					}
				case InventoryConstant.GET_NEXT_INVENTORY_NO :
					{
						return getNextInvItmNo(aaData);
					}
				case InventoryConstant.INV_GET_NEXT_VI_ITEM_NO :
					{
						// defect 9117
						return getNextIVItmNo(aaData);
						// end defect 9117
					}
				case InventoryConstant
					.ADD_MODIFY_DELETE_INVENTORY_ITEM :
					{
						return addModfyDelInvcItm(aaData);
					}
				case InventoryConstant.RECEIVE_INVENTORY_TO_DB :
					{
						return rcveInvc(aaData);
					}
				case InventoryConstant.ISSUE_INVENTORY :
					{
						return issueInv(aaData);
					}
				case InventoryConstant.RETRIEVE_REGIONAL_COUNTIES :
					{
						return getRegionalCounties(aaData);
					}
				case InventoryConstant
					.GENERATE_INVENTORY_ACTION_REPORT :
					{
						return genInvActionRpt(aaData);
					}
				case InventoryConstant.VERIFY_INVENTORY_ITEM_IN_DB :
					{
						return chkForInvItmInDb(aaData);
					}
				case InventoryConstant
					.VERIFY_INVENTORY_ITEM_FOR_ISSUE :
					{
						return chkForInvItmToIssue(aaData);
					}
				case InventoryConstant
					.GET_INV_FUNC_TRANS_AND_TR_INV_DETAIL :
					{
						return getInvFuncTransAndTrInvDetail(aaData);
					}
				case InventoryConstant.RETRIEVE_INV_ITEMS :
					{
						return getInvItms();
					}
				case InventoryConstant
					.GENERATE_RECEIVE_INVENTORY_HISTORY_REPORT :
					{
						return genRcveInvHistoryRpt(aaData);
					}
				case InventoryConstant
					.GENERATE_DELETE_INVENTORY_HISTORY_REPORT :
					{
						return genDelInvHistoryRpt(aaData);
					}
				case InventoryConstant.RETRIEVE_INQUIRY_ENTITY_DATA :
					{
						return getInqEntityData(aaData);
					}
				case InventoryConstant.GET_INVENTORY_ALLOCATION_DATA :
					{
						return getInvAllocData(aaData);
					}
				case InventoryConstant.UPDATE_INVENTORY_STATUS_CD :
					{
						if (aaData instanceof Vector)
						{
							return updInvStatusCd(
								(DatabaseAccess) ((Vector) aaData).get(
									CommonConstant.ELEMENT_0),
								(ProcessInventoryData)
									((Vector) aaData).get(
									CommonConstant.ELEMENT_1));
						}
						else
						{
							return updInvStatusCd(
								null,
								(ProcessInventoryData) aaData);
						}
					}
				case InventoryConstant.INV_VI_UPDATE_INV_STATUS_CD :
					{
						if (aaData instanceof Vector)
						{
							return updVIStatusCd(
								(DatabaseAccess) ((Vector) aaData).get(
									CommonConstant.ELEMENT_0),
								(InventoryAllocationData)
									((Vector) aaData).get(
									CommonConstant.ELEMENT_1));
						}
						else
						{
							return updVIStatusCd(
								null,
								(InventoryAllocationData) aaData);
						}
					}
				case InventoryConstant
					.INV_VI_UPDATE_INV_STATUS_CD_RECOVER :
					{
						// defect 9116
						if (aaData instanceof Vector)
						{
							return updVIStatusCdRecover(
								(DatabaseAccess) ((Vector) aaData).get(
									CommonConstant.ELEMENT_0),
								(InventoryAllocationData)
									((Vector) aaData).get(
									CommonConstant.ELEMENT_1));
						}
						else
						{
							return updVIStatusCdRecover(
								null,
								(InventoryAllocationData) aaData);
						}
						// end defect 9116
					}
				case InventoryConstant
					.RETRIEVE_INVENTORY_ITEMS_FOR_INQUIRY :
					{
						return getInvItmsForInq(aaData);
					}
				case InventoryConstant.DELETE_INVENTORY_ITEM :
					{
						return delInvItm(aaData);
					}
				case InventoryConstant.DELETE_FOR_ISSUE_INVENTORY :
					{
						return delForIssueInv(aaData);
					}
				case InventoryConstant
					.GENERATE_INVENTORY_INQUIRY_REPORT :
					{
						return genInvInqRpt(aaData);
					}
				case InventoryConstant.GET_INVENTORY_RANGE_IN_DB :
					{
						return getInvRngeInDb(aaData);
					}
				case InventoryConstant.GET_WS_IDS :
					{
						return getWsIds(aaData, null);
					}
				case InventoryConstant.INV_VI_VALIDATE_PER_PLT :
					{
						return validatePerPlate(aaData);
					}
				case InventoryConstant.RECALC_GET_INV :
					{
						return reCalcGetInv(aaData);
					}
				case InventoryConstant.RECALC_CHECK_INV :
					{
						return reCalcCheckInv(aaData);
					}
				case InventoryConstant.RECALC_CHECK_VI :
					{
						return reCalcCheckViRow(aaData);
					}
				case InventoryConstant.INV_VI_DELETE_ITEM :
					{
						if (aaData instanceof InventoryAllocationData)
						{
							return delInvVirtual(
								null,
								(InventoryAllocationData) aaData);
						}
						else
						{
							return null;
						}
					}
				case InventoryConstant
					.INV_VI_ITEM_APPLICATION_COMPLETE :
					{
						if (aaData instanceof Vector)
						{
							return updViStatusCodeComplete(
								(DatabaseAccess) ((Vector) aaData).get(
									CommonConstant.ELEMENT_0),
								((Vector) aaData).get(
									CommonConstant.ELEMENT_1));
						}
						else
						{
							return updViStatusCodeComplete(
								null,
								aaData);
						}
					}
				case InventoryConstant.INV_VI_CONFIRM_HOLD :
					{
						if (aaData instanceof Vector)
						{
							return updViStatusCodeConfirm(
								(DatabaseAccess) ((Vector) aaData).get(
									CommonConstant.ELEMENT_0),
								((Vector) aaData).get(
									CommonConstant.ELEMENT_1));
						}
						else
						{
							return updViStatusCodeConfirm(null, aaData);
						}
					}
				case InventoryConstant.INV_VI_RELEASE_HOLD :
					{
						if (aaData instanceof Vector)
						{
							return updViStatusCodeRelease(
								(DatabaseAccess) ((Vector) aaData).get(
									CommonConstant.ELEMENT_0),
								((Vector) aaData).get(
									CommonConstant.ELEMENT_1));
						}
						else
						{
							return updViStatusCodeRelease(null, aaData);
						}
					}
				case InventoryConstant.INV_VI_VALIDATE_PLP_NO_HOLD :
					{
						return validatePerPlateNoHold(aaData);
					}
					// defect 9117
				case InventoryConstant.GENERATE_VI_REJECTION_REPORT :
					{
						return genVIRejectionRpt(aaData);
					}
					// end defect 9117
				case InventoryConstant.INV_VI_PURGE :
					{
						return purgeInventoryVirtual(aaData);
					}
				case InventoryConstant
					.INV_VI_RELEASE_ALL_SYSTEM_HOLDS :
					{
						return purgeInventoryVirtualReleaseHolds(aaData);
					}
				case InventoryConstant.INV_VI_INSERT_ROW :
					{
						return insrtViRow(null, aaData);
					}
				case InventoryConstant.INV_VI_UPDATE_TRANSTIME :
					{
						if (aaData instanceof Vector)
						{
							return updViTransTime(
								(DatabaseAccess) ((Vector) aaData).get(
									CommonConstant.ELEMENT_0),
								(InventoryAllocationData)
									((Vector) aaData).get(
									CommonConstant.ELEMENT_1));
						}
						else
						{
							return updViTransTime(
								null,
								(InventoryAllocationData) aaData);
						}
					}
				default :
					{
						return null;
					}
			}
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
	}

	/**
	 * Purge any completed Virtual Inventory rows.
	 * 
	 * @param aaParameters Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object purgeInventoryVirtual(Object aaParameters)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeInventoryVirtual - Begin " + csClientHost);
		DatabaseAccess laDBA = null;
		int liTransAmDate = 0;

		try
		{
			// defect 9431
			if (aaParameters instanceof Vector)
			{
				// Get the values out of the Vector
				laDBA =
					(DatabaseAccess) ((Vector) aaParameters).elementAt(
						0);
				liTransAmDate =
					((Integer) ((Vector) aaParameters).elementAt(1))
						.intValue();
			}
			else
			{
				throw new RTSException(
					RTSException.DB_DOWN,
					new Exception("Invalid Object " + csClientHost));
			}

			// setup the ints
			int liPurgeCount = 0;
			//int liTransAmDate = ((Integer) aaParameters).intValue();

			// setup the database access objects
			// use the passed dba
			//laDBA = new DatabaseAccess();
			// end defect 9431

			InventoryVirtual laIV = new InventoryVirtual(laDBA);
			laDBA.beginTransaction();

			// do the purge for completes
			liPurgeCount =
				laIV.purgeInventoryVirtual(
					liTransAmDate,
					InventoryConstant.HOLD_INV_ORDER_COMPLETE);

			// clean up and return
			laDBA.endTransaction(DatabaseAccess.COMMIT);

			// defect 9431
			Log.write(
				Log.DEBUG,
				this,
				"purgeInventoryVirtual - Purge Count " + liPurgeCount);
			// end defect 9431

			return new Integer(liPurgeCount);
		}
		catch (RTSException aeRTSEx)
		{
			// Got an exception while processing
			try
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx2)
			{
				// Got an exception while attempting to rollback
				System.out.println(
					"Got an error on RollBack in purgeInventoryVirtual");
				aeRTSEx2.printStackTrace();
			}
			throw aeRTSEx;
		}
		finally
		{
			// defect 9431
			// do not null out the dba.  it is passed.
			//laDBA = null;
			// end defect 9431
			Log.write(
				Log.METHOD,
				this,
				"purgeInventoryVirtual - End " + csClientHost);
		}
	}

	/**
	 * Release System Holds on Virtual Inventory rows.
	 * 
	 * @param aaParameters Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object purgeInventoryVirtualReleaseHolds(Object aaParameters)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeInventoryVirtualReleaseHolds - Begin "
				+ csClientHost);
		DatabaseAccess laDBA = null;

		try
		{
			// defect 9431
			if (aaParameters instanceof Vector)
			{
				// Get the dba out of the Vector
				laDBA =
					(DatabaseAccess) ((Vector) aaParameters).elementAt(
						0);
			}
			else
			{
				throw new RTSException(
					RTSException.DB_DOWN,
					new Exception("Invalid Object " + csClientHost));
			}

			// setup the ints
			int liReleaseCount = 0;

			// setup the database access objects
			//laDBA = new DatabaseAccess();
			// end defect 9431

			InventoryVirtual laIV = new InventoryVirtual(laDBA);
			laDBA.beginTransaction();

			// delete all the customer supplieds first
			liReleaseCount = laIV.purgeInventoryVirtualInvStatusCdCS();

			// do the release for SystemHolds
			// Note this must follow the purge for customer supplied.
			// We want to get those deleted.
			// defect 9606
			// Only release POS requests
			liReleaseCount =
				liReleaseCount
					+ laIV.updInventoryVirtualInvStatusCdNonItrnt();

			// Only release IVTRS requests
			liReleaseCount =
				liReleaseCount
					+ laIV.updInventoryVirtualInvStatusCdItrnt();
			// end defect 9606

			// TODO there is no release for holds where transtime > 0
			// Monitor with queries for now.

			// clean up and return
			laDBA.endTransaction(DatabaseAccess.COMMIT);

			// defect 9431
			// allow for output of the release count
			Log.write(
				Log.DEBUG,
				this,
				"purgeInventoryVirtualReleaseHolds - Release count "
					+ liReleaseCount);
			// end defect 9431

			return new Integer(liReleaseCount);
		}
		catch (RTSException aeRTSEx)
		{
			// Got an exception while processing
			try
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx2)
			{
				// Got an exception while attempting to rollback
				System.out.println(
					"Got an error on RollBack in purgeInventoryVirtualReleaseHolds");
				aeRTSEx2.printStackTrace();
			}
			throw aeRTSEx;
		}
		finally
		{
			// defect 9431
			// do not null out dba since it is passed
			//laDBA = null;
			// end defect 9431
			Log.write(
				Log.METHOD,
				this,
				"purgeInventoryVirtualReleaseHolds - End "
					+ csClientHost);
		}
	}

	/**
	 * Used to insert the invoice line items into the 
	 * RTS_INV_ALLOCATION table.  After setting up the items to be 
	 * inserted, checks to see if they're in the RTS_INV_ALLOCATION
	 * table before inserting them.  Then creates the entries for the 
	 * RTS_TR_INV_DETAIL and RTS_INV_FUNC_TRANS tables.
	 *
	 * <p>Parameter aaData is a  vector where the elements have to be 
	 * as follows:
	 * <ul>
	 * <li>Element(0) - A vector of SubstationData that contains all 
	 * 					the substations
	 * <li>Element(1) - The invoice as a MFInventoryAllocationData 
	 * 					object
	 * <li>Element(2) - The information required for the report as a 
	 * 					ReportSearchData object
	 * <li>Element(3) - The header for the transaction as a 
	 * 					TransactionHeaderData object
	 * <eul>
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException  
	 */
	private Object rcveInvc(Object aaData) throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_RCVEINVC_BEGIN + csClientHost);

		Vector lvSubstaData =
			(Vector) ((Vector) aaData).get(CommonConstant.ELEMENT_0);
		MFInventoryAllocationData laMFInvAlloctnData =
			(MFInventoryAllocationData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_1);
		ReportSearchData laRSD =
			(ReportSearchData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_2);
		TransactionHeaderData laTransHeaderData =
			(TransactionHeaderData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_3);
		DatabaseAccess laDBA = new DatabaseAccess();
		CompleteTransactionData laCompltTransData =
			new CompleteTransactionData();
		Vector lvTrInvDetailData = new Vector();

		boolean lbIsSuccessful = false;
		boolean lbIsInvcCorrIndi = false;

		// Set up the rows to be inserted into the Allocation table
		for (int i = 0;
			i < laMFInvAlloctnData.getInvAlloctnData().size();
			i++)
		{
			InventoryAllocationUIData laIAUID =
				(InventoryAllocationUIData) laMFInvAlloctnData
					.getInvAlloctnData()
					.get(
					i);

			laIAUID.setOfcIssuanceNo(
				laMFInvAlloctnData
					.getMFInvAckData()
					.getOfcIssuanceNo());
			for (int j = 0; j < lvSubstaData.size(); j++)
			{
				SubstationData laSubstaData =
					(SubstationData) lvSubstaData.get(j);
				if (laMFInvAlloctnData
					.getMFInvAckData()
					.getDest()
					.equals(laSubstaData.getSubstaName()))
				{
					laIAUID.setSubstaId(laSubstaData.getSubstaId());
					break;
				}
			}
			laIAUID.setInvId(CENTRAL_LOCID);
			if (laMFInvAlloctnData
				.getMFInvAckData()
				.getRcveInto()
				.equals(InventoryConstant.STR_CENTRAL))
			{
				laIAUID.setInvLocIdCd(InventoryConstant.CHAR_CENTRAL);
			}
			else if (
				laMFInvAlloctnData
					.getMFInvAckData()
					.getRcveInto()
					.equals(
					InventoryConstant.STR_STOCK))
			{

				laIAUID.setInvLocIdCd(InventoryConstant.CHAR_STOCK);

			}
			laIAUID.setInvStatusCd(InventoryConstant.HOLD_INV_NOT);
		}

		// Check to see that the item is not in the allocation table of 
		// the destination office and insert the valid invoice rows into 
		// the RTS_INV_ALLOCATION table
		try
		{
			laDBA.beginTransaction();
			InventoryAllocation laInvAlloctnSQL =
				new InventoryAllocation(laDBA);

			for (int k = 0;
				k < laMFInvAlloctnData.getInvAlloctnData().size();
				k++)
			{
				Vector lvReturnQryData = new Vector();
				InventoryAllocationUIData lIAUID =
					(InventoryAllocationUIData) laMFInvAlloctnData
						.getInvAlloctnData()
						.get(
						k);

				if (!lIAUID
					.getStatusCd()
					.equals(InventoryConstant.CHECK))
				{
					continue;
				}

				if (!ValidateInventoryPattern
					.chkIfItmPLPOrOLDPLTOrROP(lIAUID.getItmCd()))
				{
					lvReturnQryData =
						laInvAlloctnSQL
							.qryInventoryAllocationIntersection(
							lIAUID);
				}
				else
				{
					lIAUID.setPatrnSeqNo(Integer.MIN_VALUE);
					lvReturnQryData =
						laInvAlloctnSQL
							.qryInventoryAllocationForSpecificItem(
							lIAUID);
					lIAUID.setPatrnSeqNo(0);
				}

				if (lvReturnQryData.size() > CommonConstant.ELEMENT_0)
				{
					RTSException leRTSEx =
						new RTSException(
							ErrorsConstant.ERR_NUM_ITM_EXISTS);
					throw leRTSEx;
				}

				// Insert the invoice line item
				laInvAlloctnSQL.insInventoryAllocation(lIAUID);

				// Write INVRCV transactions
				TransactionInventoryDetailData laTransInvDetailData =
					new TransactionInventoryDetailData();

				laTransInvDetailData.setTransCd(TransCdConstant.INVRCV);
				if (lIAUID.getOrigItmModfyIndi()
					== InventoryConstant.ITM_INDI_NO_CHANGE)
				{

					laTransInvDetailData.setItmCd(lIAUID.getItmCd());
					laTransInvDetailData.setInvItmYr(
						lIAUID.getInvItmYr());
					laTransInvDetailData.setInvQty(lIAUID.getInvQty());
					laTransInvDetailData.setInvItmNo(
						lIAUID.getInvItmNo());
					laTransInvDetailData.setInvEndNo(
						lIAUID.getInvItmEndNo());
					laTransInvDetailData.setInvId(
						String.valueOf(lIAUID.getSubstaId()));
					if (laMFInvAlloctnData
						.getMFInvAckData()
						.getRcveInto()
						.equals(InventoryConstant.STR_CENTRAL))
					{
						// defect 8656
						//laTransInvDetailData.setInvLocIdCd(
						//	InventoryConstant.STR_CENTRAL);
						laTransInvDetailData.setInvLocIdCd(
							InventoryConstant.CHAR_CENTRAL);
						// end defect 8656
					}
					else
					{ // defect 8656
						//laTransInvDetailData.setInvLocIdCd(
						//	InventoryConstant.STR_STOCK);
						laTransInvDetailData.setInvLocIdCd(
							InventoryConstant.CHAR_STOCK);
						// end defect 8656
					}
					if (laMFInvAlloctnData
						.getMFInvAckData()
						.getInvcNo()
						.substring(0, 1)
						.equals(InventoryConstant.INVOICE_PREFIX_DUMMY)
						|| laMFInvAlloctnData
							.getMFInvAckData()
							.getInvcNo()
							.substring(
							0,
							1).equals(
							InventoryConstant.STR_STOCK))
					{
						laTransInvDetailData.setDetailStatusCd(1);
					}
					else
					{
						laTransInvDetailData.setDetailStatusCd(0);
					}
					laTransInvDetailData.setDelInvReasnCd(0);

					lvTrInvDetailData.addElement(laTransInvDetailData);

				}
				else if (
					lIAUID.getOrigItmModfyIndi()
						== InventoryConstant.ITM_INDI_MODIFIED)
				{
					//defect5001
					// set the modified boolean to indicate that the 
					//invoice was modified
					lbIsInvcCorrIndi = true;
					//end defect5001

					//Assign invoiced row to detail table
					if (laMFInvAlloctnData
						.getMFInvAckData()
						.getInvcNo()
						.substring(0, 1)
						.equals(InventoryConstant.INVOICE_PREFIX_DUMMY)
						|| laMFInvAlloctnData
							.getMFInvAckData()
							.getInvcNo()
							.substring(
							0,
							1).equals(
							InventoryConstant.STR_STOCK)
						|| lIAUID.getOrigInvItmNo() == null)
					{
						laTransInvDetailData.setItmCd(
							lIAUID.getItmCd());
						laTransInvDetailData.setInvItmYr(
							lIAUID.getInvItmYr());
						laTransInvDetailData.setInvQty(
							lIAUID.getOrigInvQty());
						laTransInvDetailData.setInvItmNo(
							lIAUID.getOrigInvItmNo());
						laTransInvDetailData.setInvEndNo(
							lIAUID.getOrigInvItmEndNo());
						laTransInvDetailData.setInvId(
							String.valueOf(0));
						if (laMFInvAlloctnData
							.getMFInvAckData()
							.getRcveInto()
							.equals(InventoryConstant.STR_CENTRAL))
						{
							// defect 8656
							//laTransInvDetailData.setInvLocIdCd(
							//	InventoryConstant.STR_CENTRAL);
							laTransInvDetailData.setInvLocIdCd(
								InventoryConstant.CHAR_CENTRAL);
							// end defect 8656
						}
						else
						{
							// defect 8656
							//laTransInvDetailData.setInvLocIdCd(
							//	InventoryConstant.STR_STOCK);
							laTransInvDetailData.setInvLocIdCd(
								InventoryConstant.CHAR_STOCK);
							// end defect 8656
						}
						laTransInvDetailData.setDetailStatusCd(1);
						laTransInvDetailData.setDelInvReasnCd(0);

						lvTrInvDetailData.addElement(
							laTransInvDetailData);
					}
					else
					{
						//Assign received row to detail table
						laTransInvDetailData.setItmCd(
							lIAUID.getItmCd());
						laTransInvDetailData.setInvItmYr(
							lIAUID.getInvItmYr());
						laTransInvDetailData.setInvQty(
							lIAUID.getInvQty());
						laTransInvDetailData.setInvItmNo(
							lIAUID.getInvItmNo());
						laTransInvDetailData.setInvEndNo(
							lIAUID.getInvItmEndNo());
						laTransInvDetailData.setInvId(
							String.valueOf(lIAUID.getSubstaId()));
						if (laMFInvAlloctnData
							.getMFInvAckData()
							.getRcveInto()
							.equals(InventoryConstant.STR_CENTRAL))
						{
							// defect 8656
							//laTransInvDetailData.setInvLocIdCd(
							//	InventoryConstant.STR_CENTRAL);
							laTransInvDetailData.setInvLocIdCd(
								InventoryConstant.CHAR_CENTRAL);
							// end defect 8656
						}
						else
						{
							// defect 8656
							//laTransInvDetailData.setInvLocIdCd(
							//	InventoryConstant.STR_STOCK);
							laTransInvDetailData.setInvLocIdCd(
								InventoryConstant.CHAR_STOCK);
							// end defect 8656
						}
						laTransInvDetailData.setDetailStatusCd(-1);
						laTransInvDetailData.setDelInvReasnCd(0);

						lvTrInvDetailData.addElement(
							laTransInvDetailData);
					}
				}
				else if (
					lIAUID.getOrigItmModfyIndi()
						== InventoryConstant.ITM_INDI_NEW)
				{
					//Assign received row to detail table
					laTransInvDetailData.setItmCd(lIAUID.getItmCd());
					laTransInvDetailData.setInvItmYr(
						lIAUID.getInvItmYr());
					laTransInvDetailData.setInvQty(lIAUID.getInvQty());
					laTransInvDetailData.setInvItmNo(
						lIAUID.getInvItmNo());
					laTransInvDetailData.setInvEndNo(
						lIAUID.getInvItmEndNo());
					laTransInvDetailData.setInvId(
						String.valueOf(lIAUID.getSubstaId()));
					if (laMFInvAlloctnData
						.getMFInvAckData()
						.getRcveInto()
						.equals(InventoryConstant.STR_CENTRAL))
					{
						laTransInvDetailData.setInvLocIdCd(
							InventoryConstant.CHAR_CENTRAL);
					}
					else
					{
						laTransInvDetailData.setInvLocIdCd(
							InventoryConstant.CHAR_STOCK);
					}
					laTransInvDetailData.setDetailStatusCd(1);
					laTransInvDetailData.setDelInvReasnCd(0);

					lvTrInvDetailData.addElement(laTransInvDetailData);
				}
				else if (
					lIAUID.getOrigItmModfyIndi()
						== InventoryConstant.ITM_INDI_REMOVED)
				{
					if (!laMFInvAlloctnData
						.getMFInvAckData()
						.getInvcNo()
						.substring(0, 1)
						.equals(InventoryConstant.INVOICE_PREFIX_DUMMY)
						|| !laMFInvAlloctnData
							.getMFInvAckData()
							.getInvcNo()
							.substring(0, 1)
							.equals(InventoryConstant.STR_STOCK)
						|| lIAUID.getInvItmNo() != null)
					{
						laTransInvDetailData.setItmCd(
							lIAUID.getItmCd());
						laTransInvDetailData.setInvItmYr(
							lIAUID.getInvItmYr());
						laTransInvDetailData.setInvQty(
							lIAUID.getInvQty());
						laTransInvDetailData.setInvItmNo(
							lIAUID.getInvItmNo());
						laTransInvDetailData.setInvEndNo(
							lIAUID.getInvItmEndNo());
						laTransInvDetailData.setInvId(
							String.valueOf(0));
						if (laMFInvAlloctnData
							.getMFInvAckData()
							.getRcveInto()
							.equals(InventoryConstant.STR_CENTRAL))
						{
							// defect 8656
							laTransInvDetailData.setInvLocIdCd(
								InventoryConstant.STR_CENTRAL);
							laTransInvDetailData.setInvLocIdCd(
								InventoryConstant.CHAR_CENTRAL);
							// end defect 8656
						}
						else
						{
							// defect 8656
							//laTransInvDetailData.setInvLocIdCd(
							//	InventoryConstant.STR_STOCK);
							laTransInvDetailData.setInvLocIdCd(
								InventoryConstant.CHAR_STOCK);
							// end defect 8656
						}
						laTransInvDetailData.setDetailStatusCd(-1);
						laTransInvDetailData.setDelInvReasnCd(0);

						lvTrInvDetailData.addElement(
							laTransInvDetailData);
					}
				}
			}

			CommonServerBusiness laCommnServerBus =
				new CommonServerBusiness();

			InventoryFunctionTransactionData laInventoryFunctionTransactionData =
				new InventoryFunctionTransactionData();
			laInventoryFunctionTransactionData.setTransCd(
				TransCdConstant.INVRCV);
			laInventoryFunctionTransactionData.setInvcNo(
				laMFInvAlloctnData.getMFInvAckData().getInvcNo());
			// defect 9116
			laInventoryFunctionTransactionData.setEmpId(
				laTransHeaderData.getTransEmpId());
			// end defect 9116

			if (lbIsInvcCorrIndi == true)
			{
				laInventoryFunctionTransactionData.setInvcCorrIndi(1);

			}
			else
			{
				laInventoryFunctionTransactionData.setInvcCorrIndi(0);
			}

			// Make call to common to finish and write trans data
			laCompltTransData.setInvFuncTrans(
				laInventoryFunctionTransactionData);
			laCompltTransData.setTransInvDetail(lvTrInvDetailData);

			Vector lvInputTrans = new Vector();
			lvInputTrans.addElement(laTransHeaderData);
			laCompltTransData.setTransCode(TransCdConstant.INVRCV);
			lvInputTrans.addElement(laCompltTransData);
			lvInputTrans.addElement(laDBA);
			laCompltTransData =
				(CompleteTransactionData) laCommnServerBus.processData(
					GeneralConstant.COMMON,
					CommonConstant.PROC_TRANS,
					lvInputTrans);

			// Add this data to the History table
			addInvHistData(lvInputTrans);

			// Get the transid
			laMFInvAlloctnData.setTransCd(
				laCompltTransData.getTransId());

			// Generate the report
			Vector lvVct = new Vector(CommonConstant.ELEMENT_2);
			lvVct.addElement(laMFInvAlloctnData);
			lvVct.addElement(laRSD);

			laRSD = (ReportSearchData) genInvRcveRpt(lvVct, laDBA);

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			lbIsSuccessful = true;

			return laRSD;
		}
		finally
		{
			if (!lbIsSuccessful)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			Log.write(
				Log.METHOD,
				this,
				MSG_RCVEINVC_END + csClientHost);
		}
	}

	/**
	 * Used to update inventory object in the RTS_INV_ALLOCATION table 
	 * by updating the original row with a new PatrnSeqNo and quantity.
	 *
	 * <p>Parameter aaData is a vector contains calculated range, 
	 *					  plus a vector containing the row(s) containing 
	 *					  the begin and/or end of the range
	 *
	 * <p>Returns a Vector containing the range to check.
	 *
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object reCalcCheckInv(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_PERFORMRECACLCHECK_BEGIN + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();
		laDBA.beginTransaction();

		try
		{
			// create new vector to return
			Vector lvReturn = new Vector();

			// convert the passed object into a vector
			Vector lvData = (Vector) aaData;

			InventoryAllocationData laCheckInvAllocData =
				(InventoryAllocationData) lvData.elementAt(
					CommonConstant.ELEMENT_0);

			// defect 8244
			String lsUserName = null;
			String lsRequestId = null;

			if (lvData.size() > CommonConstant.ELEMENT_1)
			{
				lsUserName =
					(String) lvData.elementAt(CommonConstant.ELEMENT_1);
			}
			if (lvData.size() > CommonConstant.ELEMENT_2)
			{
				lsRequestId =
					(String) lvData.elementAt(CommonConstant.ELEMENT_2);
			}

			int liOrigQty = laCheckInvAllocData.getInvQty();
			laCheckInvAllocData.setInvQty(0);

			ValidateInventoryPattern laVIP =
				new ValidateInventoryPattern();

			InventoryAllocationData laNewInvAllocData = null;

			// defect 9116
			// Add check for Virtual and section for Virtual
			if (laCheckInvAllocData.isVirtual())
			{
				laNewInvAllocData =
					(InventoryAllocationData) laVIP.calcInvUnknownVI(
						laCheckInvAllocData);
			}
			else
			{
				laNewInvAllocData =
					(InventoryAllocationData) laVIP.calcInvUnknown(
						new InventoryAllocationUIData(laCheckInvAllocData));
			}
			// end defect 9116

			// send the "new" range back to client for reporting.
			lvReturn.addElement(laNewInvAllocData);

			// Do the update if the RequestId is passed in
			// defect 9116
			// do not process for Virtual
			if (lsRequestId != null)
				//			&& !laCheckInvAllocData.isVirtual())
			{
				// end defect 9116
				if (laCheckInvAllocData.getPatrnSeqNo()
					!= laNewInvAllocData.getPatrnSeqNo()
					|| liOrigQty != laNewInvAllocData.getInvQty()
					|| laCheckInvAllocData.getPatrnSeqCd()
						!= laNewInvAllocData.getPatrnSeqCd())
				{
					reCalcUpdtInv(
						laDBA,
						laNewInvAllocData,
						lsUserName,
						lsRequestId);
				}
			}

			// check for dups (Intersections)
			InventoryAllocationData laIntCheck =
				new InventoryAllocationData();
			laIntCheck.setOfcIssuanceNo(Integer.MIN_VALUE);
			laIntCheck.setSubstaId(Integer.MIN_VALUE);
			laIntCheck.setItmCd(laNewInvAllocData.getItmCd());
			laIntCheck.setInvItmYr(laNewInvAllocData.getInvItmYr());
			laIntCheck.setPatrnSeqCd(laNewInvAllocData.getPatrnSeqCd());
			laIntCheck.setPatrnSeqNo(laNewInvAllocData.getPatrnSeqNo());
			// defect 9116
			laIntCheck.setInvQty(laNewInvAllocData.getInvQty());
			laIntCheck.setEndPatrnSeqNo(
				laNewInvAllocData.getPatrnSeqNo()
					+ laNewInvAllocData.getInvQty());
			// end defect 9116
			laIntCheck.setVirtual(laNewInvAllocData.isVirtual());

			Vector lvdups = new Vector();

			// defect 9116
			// Check for Virtual and process appropriately
			if (!laNewInvAllocData.isVirtual())
			{
				// do a query to get all rows matching the begin and end
				InventoryAllocation laInvAlloctnSQL =
					new InventoryAllocation(laDBA);

				lvdups =
					laInvAlloctnSQL.qryInventoryAllocationIntersection(
						laIntCheck);
			}
			else
			{
				laIntCheck.setViItmCd(laIntCheck.getItmCd());
				// do a query to get all rows matching the begin and end
				InventoryVirtual laInvVirtualSQL =
					new InventoryVirtual(laDBA);

				lvdups =
					laInvVirtualSQL.qryInventoryVirtualIntersection(
						laIntCheck);
			}
			// end defect 9116

			// do not report PLP, ROP, OLDPLT!
			if (lvdups != null
				&& lvdups.size() > CommonConstant.ELEMENT_1
				&& !ValidateInventoryPattern.chkIfItmPLPOrOLDPLTOrROP(
					laNewInvAllocData.getItmCd()))
			{
				lvReturn.addElement(lvdups);
			}
			// end of dups check

			laDBA.endTransaction(DatabaseAccess.COMMIT);

			return lvReturn;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_PERFORMRECALCCHECK_END + csClientHost);
		}
	}

	/**
	 * Calculate a plate based on VI item codes and check to see if it 
	 * exists.
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object reCalcCheckViRow(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"performRecalcUpdate - Begin" + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			laDBA.beginTransaction();

			InventoryAllocationData laUpdateInvAllocaData =
				(InventoryAllocationData) aaData;

			String lsViItmCd =
				lookupVirtualForItem(laUpdateInvAllocaData);
			laUpdateInvAllocaData.setViItmCd(lsViItmCd);

			Vector lvResults =
				chkVirtualInvForSpecificItem(
					laUpdateInvAllocaData,
					laDBA);

			if (lvResults.size() > 0)
			{
				throw new RTSException();
			}

			laUpdateInvAllocaData.setVirtual(true);

			laDBA.endTransaction(DatabaseAccess.NONE);
			laDBA = null;

			return laUpdateInvAllocaData;
		}
		finally
		{
			if (laDBA != null)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				laDBA = null;
			}
			Log.write(
				Log.METHOD,
				this,
				"performRecalcUpdate - End" + csClientHost);
		}
	}

	/**
	 * Retrieve data from the RTS_INV_ALLOCATION or
	 * RTS_INV_VIRTUAL table depending on the switch passed.
	 * The query can be more limited to just one office and even just
	 * one item code.
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException  
	 */
	private Object reCalcGetInv(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_RECALCGETINV_BEGIN + csClientHost);

		int liOfcNo =
			Integer.parseInt(
				(String) (((Vector) aaData)
					.elementAt(CommonConstant.ELEMENT_0)));

		String lsItmCd =
			(String) (((Vector) aaData)
				.elementAt(CommonConstant.ELEMENT_1));

		// defect 9116
		// parse out Virtual boolean
		boolean lbVirtual =
			(((Boolean) (((Vector) aaData)
				.elementAt(CommonConstant.ELEMENT_2)))
				.booleanValue());
		// end defect 9116

		Vector lvInvAllocaUIData = null;
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();

			// defect 9116
			// Add check for Virtual and qry to get Virtual
			if (!lbVirtual)
			{
				InventoryAllocation laInvAlloctnSQL =
					new InventoryAllocation(laDBA);
				lvInvAllocaUIData =
					laInvAlloctnSQL.qryGetAllInv(liOfcNo, lsItmCd);
			}
			else
			{
				InventoryVirtual laInvVirtualSQL =
					new InventoryVirtual(laDBA);
				lvInvAllocaUIData =
					laInvVirtualSQL.qryGetAllVirtualInv(lsItmCd);
			}
			// end defect 9116
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return lvInvAllocaUIData;

		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_RECALCGETINV_END + csClientHost);
		}
	}

	/**
	 * Used to update inventory object in the RTS_INV_ALLOCATION 
	 * table by updating the
	 * original row with a new PatrnSeqNo and quantity.
	 *
	 * @param aaDBA DatabaseAccess
	 * @param aaData Object 
	 * @param asUserName String
	 * @param asRequestId String
	 * @return Object
	 * @exception RTSException 
	 */
	private Object reCalcUpdtInv(
		DatabaseAccess aaDBA,
		Object aaData,
		String asUserName,
		String asRequestId)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_PREFORMRECALCUPDATE_BEGIN + csClientHost);

		DatabaseAccess laDBA = null;
		if (aaDBA != null)
		{
			laDBA = aaDBA;
		}
		else
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
		}

		InventoryAllocationData laUpdateInvAllocaData =
			(InventoryAllocationData) aaData;

		try
		{
			if (laUpdateInvAllocaData.isVirtual())
			{
				// doing Virtual Inv
				InventoryVirtual laInvVirtualSQL =
					new InventoryVirtual(laDBA);

				laInvVirtualSQL.updInventoryVirtual(
					laUpdateInvAllocaData,
					csClientHost);
			}
			else
			{
				// Aren't changing the substaid so set the NewSubstaId = 
				// Integer.Min_Value
				laUpdateInvAllocaData.setNewSubstaId(Integer.MIN_VALUE);

				InventoryAllocation laInvAlloctnSQL =
					new InventoryAllocation(laDBA);

				// Update the inventory row
				laInvAlloctnSQL.updInventoryAllocation(
					laUpdateInvAllocaData);
			}

			// write an entry to admin log documenting update
			AdministrationLogData laLogData =
				new AdministrationLogData();
			laLogData.setAction(TXT_RECALC_ACTION);
			laLogData.setEntity(
				laUpdateInvAllocaData.getItmCd()
					+ TXT_DASH_WITH_SPACES
					+ asRequestId);
			laLogData.setEntityValue(
				laUpdateInvAllocaData.getInvItmNo()
					+ TXT_DASH_WITH_SPACES
					+ laUpdateInvAllocaData.getInvItmEndNo());
			laLogData.setOfcIssuanceNo(
				laUpdateInvAllocaData.getOfcIssuanceNo());
			laLogData.setSubStaId(laUpdateInvAllocaData.getSubstaId());
			laLogData.setTransAMDate(
				RTSDate.getCurrentDate().getAMDate());
			laLogData.setTransEmpId(asUserName);
			laLogData.setTransTime(
				RTSDate.getCurrentDate().get24HrTime());
			laLogData.setTransTimestmp(RTSDate.getCurrentDate());
			laLogData.setTransWsId(-1);
			AdministrationLog laLogSQL = new AdministrationLog(laDBA);
			laLogSQL.insAdministrationLog(laLogData);

			if (aaDBA == null)
			{
				laDBA.endTransaction(DatabaseAccess.COMMIT);
			}
			return aaData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_PERFORMRECALCUPDATE_END + csClientHost);
		}
	}

	/**
	 * Updates an inventory profile in the RTS_INV_PROFILE table and 
	 * inserts a row in the RTS_ADMIN_LOG table.
	 *
	 * <p>Parameter avData is a vector where the elements have to be 
	 * as follows:
	 * <ul>
	 * <li>Element(0) - The inventory profile that needs to be added.
	 * <li>Element(1) - The entry for the admin log table.
	 * <eul>
	 * 
	 * @param avData Vector
	 * @throws RTSException  
	 */
	private void updInvProfile(Vector avData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_UPDINVPROFILE_BEGIN + csClientHost);

		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			InventoryProfile laDbIP = new InventoryProfile(laDBA);
			laDbIP.updInventoryProfile(
				(InventoryProfileData) avData.get(
					CommonConstant.ELEMENT_0));

			AdministrationLog lDbAL = new AdministrationLog(laDBA);
			lDbAL.insAdministrationLog(
				(AdministrationLogData) avData.get(
					CommonConstant.ELEMENT_1));
			laDBA.endTransaction(DatabaseAccess.COMMIT);
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				MSG_UPDINVPROFILE_END + csClientHost);
		}
	}

	/**
	 * Used to update the inventory status code for a range of inventory 
	 * items in the RTS_INV_ALLOCATION table.  Can set the status code 
	 * to either 0, 1, or 2.  Zero means the inventory item is not on 
	 * hold.  One means the item is on a user hold. And two means the 
	 * item is on a system hold.
	 *
	 * <p>Parameter aaProcsInvData is of form ProcessInventoryData.
	 * It is the inventory range that needs to have the status code 
	 * changed.  
	 * The required fields are:
	 * <ul>
	 * <li>OfcIssuanceNo - The current office issuance number
	 * <li>SubstaId - The current substation id
	 * <li>ItmCd - The item code for the inventory
	 * <li>InvItmYr - The inventory year
	 * <li>InvItmNo - The inventory begin number of the item range
	 * <li>InvStatusCd - This is the status to which the inventory 
	 * 						 range will be changed
	 * <li>In addition to these required fields, one of the following 
	 * 		  fields need to be set
	 * <ul>
	 * <li>InvQty - The quantity
	 * <li>InvItmEndNo - The inventory end number of the item range
	 * <eul>
	 * <eul>
	 * 
	 * @param aaDBA DatabaseAccess
	 * @param aaProcesInvData ProcessInventoryData
	 * @return Vector
	 * @exception RTSException 
	 */
	private Vector updInvStatusCd(
		DatabaseAccess aaDBA,
		ProcessInventoryData aaProcsInvData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_UPDINVSTATUSCD_BEGIN + csClientHost);

		ProcessInventoryData laProcsInvDataOrig =
			(ProcessInventoryData) UtilityMethods.copy(aaProcsInvData);

		// If the inventory has already been issued, don't try to change 
		// the status code because the item is no longer in the 
		// allocation table but rather in the RTS_TR_INV_DETAIL table.  
		// (This is only an issue with when issuing inventory.)
		if (laProcsInvDataOrig.isAlreadyIssued())
		{
			Vector lvVector = new Vector();
			lvVector.add(laProcsInvDataOrig);
			return lvVector;
		}

		ProcessInventoryData laUpdateProcsInvData =
			new ProcessInventoryData();
		Vector lvProcsInvData = new Vector();
		boolean lbPLPIndi = false;
		int liNewInvStatusCd = laProcsInvDataOrig.getInvStatusCd();

		// If a database connection was not passed in, then create a new 
		// database connection.
		DatabaseAccess laDBA = null;

		try
		{
			// If a database connection was not passed in, then create 
			// a new database connection.
			if (aaDBA != null)
			{
				laDBA = aaDBA;
			}
			else
			{
				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();
			}

			// Check if item is a personalized plate
			lbPLPIndi =
				ValidateInventoryPattern.chkIfItmPLPOrOLDPLTOrROP(
					aaProcsInvData.getItmCd());

			// Validate and set up inventory information and then make 
			// call to  Inventory Allocation table
			if (!lbPLPIndi)
			{
				// Validate the begin inventory item number and 
				// calculate the PatrnSeqNo
				InventoryAllocationUIData laIAUID =
					laProcsInvDataOrig.convertToInvAlloctnUIData(
						laProcsInvDataOrig);

				// if endpatrnseqno = 0, calculate
				if (laIAUID.getEndPatrnSeqNo() == 0)
				{
					laIAUID.setCalcInv(false);
					laProcsInvDataOrig.setCalcInv(false);
				}

				// see if calculation is needed.
				if (!laProcsInvDataOrig.isCalcInv())
				{
					ValidateInventoryPattern laVIP =
						new ValidateInventoryPattern();
					laIAUID =
						(
							InventoryAllocationUIData) laVIP
								.calcInvUnknown(
							laIAUID);
					laProcsInvDataOrig =
						laProcsInvDataOrig.convertFromInvAlloctnData(
							laIAUID);

					// Calculate the EndPatrnSeqNo
					// if qty is one, just use the begin patrnseqno.
					// otherwise calculate.
					if (laProcsInvDataOrig.getInvQty() != 1)
					{
						laIAUID.setItmCd(aaProcsInvData.getItmCd());
						laIAUID.setInvItmYr(
							aaProcsInvData.getInvItmYr());
						laIAUID.setInvQty(aaProcsInvData.getInvQty());
						laIAUID.setInvItmNo(
							laProcsInvDataOrig.getInvItmEndNo());
						laIAUID.setInvItmEndNo(
							laProcsInvDataOrig.getInvItmEndNo());

						laIAUID =
							(
								InventoryAllocationUIData) laVIP
									.calcInvUnknown(
								laIAUID);
						laProcsInvDataOrig.setEndPatrnSeqNo(
							laIAUID.getPatrnSeqNo());
						laProcsInvDataOrig.setCalcInv(
							laIAUID.isCalcInv());
						// Verify begin and end numbers have the same 
						// PatrnSeqCd
						if (laProcsInvDataOrig.getPatrnSeqCd()
							!= laIAUID.getPatrnSeqCd())
						{
							RTSException leRTSException =
								new RTSException(607);
							throw leRTSException;
						}
					}
					else
					{
						laProcsInvDataOrig.setEndPatrnSeqNo(
							laProcsInvDataOrig.getPatrnSeqNo());
						laProcsInvDataOrig.setInvItmEndNo(
							laProcsInvDataOrig.getInvItmNo());
					}
				}
				laProcsInvDataOrig.setInvId(null);
				lvProcsInvData =
					chkInvItmRange(laProcsInvDataOrig, laDBA);
			}
			else
			{
				laProcsInvDataOrig.setInvQty(1);
				laProcsInvDataOrig.setPatrnSeqCd(-1);
				laProcsInvDataOrig.setPatrnSeqNo(Integer.MIN_VALUE);
				laProcsInvDataOrig.setEndPatrnSeqNo(Integer.MIN_VALUE);
				lvProcsInvData = chkForPLP(laProcsInvDataOrig, laDBA);
			}

			// Throw error 182 if ranges do not exist and the database
			//  is up
			if (lvProcsInvData.size() <= CommonConstant.ELEMENT_0)
			{
				RTSException leRTSException =
					new RTSException(
						ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB);
				throw leRTSException;
			}

			// Test if any of the selected rows are currently on hold
			if (liNewInvStatusCd != InventoryConstant.HOLD_INV_NOT)
			{
				for (int i = 0; i < lvProcsInvData.size(); i++)
				{
					if (((ProcessInventoryData) lvProcsInvData.get(i))
						.getInvStatusCd()
						== InventoryConstant.HOLD_INV_USER)
					{
						RTSException leRTSException =
							new RTSException(594);
						throw leRTSException;
					}
					else if (
						((ProcessInventoryData) lvProcsInvData.get(i))
							.getInvStatusCd()
							== InventoryConstant.HOLD_INV_SYSTEM)
					{
						RTSException leRTSException =
							new RTSException(593);
						throw leRTSException;
					}
				}
			}

			// Check for complete range and then split
			if (!lbPLPIndi)
			{
				chkForCompleteRngeAndSplt(
					laProcsInvDataOrig,
					lvProcsInvData,
					laDBA);
			}

			// Reselect ranges to place on hold
			if (!lbPLPIndi)
			{
				lvProcsInvData =
					chkInvItmRange(laProcsInvDataOrig, laDBA);
			}
			else
			{
				lvProcsInvData = chkForPLP(laProcsInvDataOrig, laDBA);
			}

			// Update inventory status to hold by RTS
			for (int i = 0; i < lvProcsInvData.size(); i++)
			{
				laUpdateProcsInvData =
					(ProcessInventoryData) lvProcsInvData.get(i);
				laUpdateProcsInvData.setInvStatusCd(liNewInvStatusCd);

				// Aren't changing the substaid so set the NewSubstaId = 
				// Integer.Min_Value
				laUpdateProcsInvData.setNewSubstaId(Integer.MIN_VALUE);
				InventoryAllocation laInvAlloctnSQL =
					new InventoryAllocation(laDBA);
				laInvAlloctnSQL.updInventoryAllocation(
					laUpdateProcsInvData);
			}

			// Reselect ranges for return
			if (!lbPLPIndi)
			{
				lvProcsInvData =
					chkInvItmRange(laProcsInvDataOrig, laDBA);
			}
			else
			{
				lvProcsInvData = chkForPLP(laProcsInvDataOrig, laDBA);
			}

			if (aaDBA == null)
			{
				laDBA.endTransaction(DatabaseAccess.COMMIT);
			}

			// For issue inventory, save the following values from the 
			// original data
			ProcessInventoryData laPID =
				(ProcessInventoryData) lvProcsInvData.get(
					CommonConstant.ELEMENT_0);
			laPID.setTransWsId(aaProcsInvData.getTransWsId());
			laPID.setTransEmpId(aaProcsInvData.getTransEmpId());
			laPID.setTransAMDate(aaProcsInvData.getTransAMDate());
			laPID.setTransTime(aaProcsInvData.getTransTime());

			return lvProcsInvData;
		}
		catch (NullPointerException aeNPEx)
		{
			// if we get a null pointer, make sure we do a rollback
			if (laDBA != null)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			throw new RTSException(RTSException.SERVER_DOWN, aeNPEx);
		}
		catch (RTSException aeRTSEx)
		{
			// defect 11180
			if (aaDBA == null && laDBA != null)
			{
				// Always rollback if it is a local dba
			    laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			else if (aaDBA != null && 
			    aeRTSEx.getCode() != ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB)
			{
			    // If DBA is passed and the error message is not 
			    // no records found, rollback.
			    // No records found is acceptable.
			    laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			// end defect 11180
			throw aeRTSEx;
		}
		finally
		{
			if (aaDBA == null)
			{
				// do not null out laDBA if aaDBA was passed.
				laDBA = null;
			}
			Log.write(
				Log.METHOD,
				this,
				MSG_UPDINVSTATUSCD_END + csClientHost);
		}
	}

	/**
	 * Updates the Inv Status Code on a Virtual Inv. Item.
	 * 
	 * @param aaDBA DatabaseAccess
	 * @param aaInvAllocData InventoryAllocationData
	 * @return Vector
	 * @throws RTSException
	 */
	private Vector updVIStatusCd(
		DatabaseAccess aaDBA,
		InventoryAllocationData aaInvAllocData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_UPDINVSTATUSCD_BEGIN + csClientHost);

		// make a copy of the original data
		InventoryAllocationData laInvAllocDataOrig =
			(InventoryAllocationData) UtilityMethods.copy(
				aaInvAllocData);

		// If the inventory has already been issued, don't try to change 
		// the status code because the item is no longer in the 
		// allocation table but rather in the RTS_TR_INV_DETAIL table.  
		// (This is only an issue with when issuing inventory.)
		if (laInvAllocDataOrig.isAlreadyIssued())
		{
			Vector lvVector = new Vector();
			lvVector.add(laInvAllocDataOrig);
			return lvVector;
		}

		// setup the update data
		InventoryAllocationData laUpdateInvAllocData =
			new InventoryAllocationData();
		Vector lvInvAllocData = new Vector();
		boolean lbPLPIndi = false;
		int liNewInvStatusCd = laInvAllocDataOrig.getInvStatusCd();

		// make the label to the local DBA object
		DatabaseAccess laDBA = null;

		// defect 9404 
		// add logging
		try
		{
			// If a database connection was not passed in, then create 
			// a new database connection.
			if (aaDBA != null)
			{
				laDBA = aaDBA;
			}
			else
			{
				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();
				//logUOW("updVIStatusCd", ":BEGIN");
			}

			// Create working copy of object
			InventoryAllocationData laInvData =
				(InventoryAllocationData) UtilityMethods.copy(
					laInvAllocDataOrig);

			// check on VI itm cd status
			if (laInvData.getViItmCd() == null
				|| laInvData.getViItmCd().equals(
					CommonConstant.STR_SPACE_EMPTY))
			{
				laInvData.setItmCd(lookupVirtualForItem(laInvData));
				laInvData.setViItmCd(laInvData.getItmCd());
				laInvAllocDataOrig.setViItmCd(laInvData.getItmCd());

				// if using real virtual, set year to 0
				if (!laInvData
					.getItmCd()
					.equalsIgnoreCase(laInvData.getItmCd()))
				{
					laInvData.setInvItmYr(0);
					laInvData.setPatrnSeqCd(0);
				}

				laInvData.setVirtual(true);
			}
			else
			{
				laInvData.setItmCd(laInvAllocDataOrig.getViItmCd());

				// if using real virtual, set year to 0
				if (!laInvData
					.getItmCd()
					.equalsIgnoreCase(laInvData.getItmCd()))
				{
					laInvData.setInvItmYr(0);
					laInvData.setPatrnSeqCd(0);
				}
			}

			// Check if item is a personalized plate
			lbPLPIndi =
				ValidateInventoryPattern.chkIfItmPLPOrOLDPLTOrROP(
					laInvAllocDataOrig.getItmCd());

			// Validate and set up inventory information and then make 
			// call to  Inventory Allocation table
			if (!lbPLPIndi)
			{
				// if endpatrnseqno = 0, calculate
				if (laInvData.getEndPatrnSeqNo() == 0)
				{
					laInvData.setCalcInv(false);
					laInvAllocDataOrig.setCalcInv(false);
				}

				// see if calculation is needed.
				if (!laInvData.isCalcInv())
				{
					ValidateInventoryPattern laVIP =
						new ValidateInventoryPattern();
					laInvData =
						(
							InventoryAllocationData) laVIP
								.calcInvUnknownVI(
							laInvData);

					// Calculate the EndPatrnSeqNo
					// if qty is one, just use the begin patrnseqno.
					// otherwise calculate.
					if (laInvData.getInvQty() != 1)
					{
						laInvData.setItmCd(aaInvAllocData.getItmCd());
						laInvData.setInvItmYr(
							aaInvAllocData.getInvItmYr());
						laInvData.setInvQty(aaInvAllocData.getInvQty());
						laInvData.setInvItmNo(
							aaInvAllocData.getInvItmEndNo());
						laInvData.setInvItmEndNo(
							aaInvAllocData.getInvItmEndNo());

						laInvData =
							(
								InventoryAllocationUIData) laVIP
									.calcInvUnknown(
								laInvData);
						laInvAllocDataOrig.setEndPatrnSeqNo(
							laInvData.getPatrnSeqNo());
						laInvAllocDataOrig.setCalcInv(
							laInvData.isCalcInv());
						// Verify begin and end numbers have the same 
						// PatrnSeqCd
						if (laInvAllocDataOrig.getPatrnSeqCd()
							!= laInvData.getPatrnSeqCd())
						{
							RTSException leRTSException =
								new RTSException(
									ErrorsConstant
										.ERR_NUM_PATRN_SEQ_NOT_RIGHT);
							throw leRTSException;
						}
					}
					else
					{
						laInvAllocDataOrig.setPatrnSeqNo(
							laInvData.getPatrnSeqNo());
						laInvAllocDataOrig.setEndPatrnSeqNo(
							laInvData.getPatrnSeqNo());
						laInvAllocDataOrig.setInvItmEndNo(
							laInvAllocDataOrig.getInvItmNo());
					}
				}
				laInvAllocDataOrig.setInvId(null);
				lvInvAllocData =
					chkVirtualInvItmRange(laInvData, laDBA);
			}
			else
			{
				laInvAllocDataOrig.setInvQty(1);
				laInvAllocDataOrig.setPatrnSeqCd(-1);
				laInvAllocDataOrig.setPatrnSeqNo(Integer.MIN_VALUE);
				laInvAllocDataOrig.setEndPatrnSeqNo(Integer.MIN_VALUE);
				lvInvAllocData =
					chkVirtualInvForSpecificItem(
						laInvAllocDataOrig,
						laDBA);
			}

			// Throw error 182 if ranges do not exist and the database
			//  is up
			if (lvInvAllocData.size() <= CommonConstant.ELEMENT_0)
			{
				RTSException leRTSException =
					new RTSException(
						ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB);
				throw leRTSException;
			}

			// Test if any of the selected rows are currently on hold
			if (liNewInvStatusCd != InventoryConstant.HOLD_INV_NOT)
			{
				for (int i = 0; i < lvInvAllocData.size(); i++)
				{
					if (((InventoryAllocationData) lvInvAllocData
						.get(i))
						.getInvStatusCd()
						== InventoryConstant.HOLD_INV_USER)
					{
						RTSException leRTSException =
							new RTSException(
								ErrorsConstant.ERR_NUM_ITEM_ON_HOLD);
						throw leRTSException;
					}
					else if (
						((InventoryAllocationData) lvInvAllocData
							.get(i))
							.getInvStatusCd()
							> InventoryConstant.HOLD_INV_SYSTEM)
					{
						// defect 9252
						// write a message to the log if we get this 
						// condition and logging is in debug mode
						Log.write(
							Log.DEBUG,
							(
								InventoryAllocationData) lvInvAllocData
									.get(
								i),
							"VI Item is past system hold, "
								+ "not available "
								+ laInvAllocDataOrig.getItmCd()
								+ " "
								+ laInvAllocDataOrig.getTransEmpId()
								+ " "
								+ laInvAllocDataOrig.getTransWsId()
								+ " "
								+ csClientHost);
						// end defect 9252

						// If the item is beyond hold (confirmed or
						// in error), you can not change the status here.
						RTSException leRTSException =
							new RTSException(
								ErrorsConstant
									.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN);
						throw leRTSException;
					}
					else if (
						((InventoryAllocationData) lvInvAllocData
							.get(i))
							.getTransTime()
							> 0
							&& ((InventoryAllocationData) lvInvAllocData
								.get(i))
								.getInvStatusCd()
								== laInvAllocDataOrig.getInvStatusCd()
							&& laInvAllocDataOrig.getInvStatusCd()
								== InventoryConstant.HOLD_INV_SYSTEM)
					{
						// defect 9252
						// write a message to the log if we get this 
						// condition and logging is in debug mode
						Log.write(
							Log.DEBUG,
							(
								InventoryAllocationData) lvInvAllocData
									.get(
								i),
							"VI Item is on hold with a "
								+ "positive transtime "
								+ laInvAllocDataOrig.getItmCd()
								+ " "
								+ laInvAllocDataOrig.getTransEmpId()
								+ " "
								+ laInvAllocDataOrig.getTransWsId()
								+ " "
								+ csClientHost);
						// end defect 9252

						// If the item is beyond initial hold.
						RTSException leRTSException =
							new RTSException(
								ErrorsConstant
									.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN);
						throw leRTSException;
					}
					else if (
						((InventoryAllocationData) lvInvAllocData
							.get(i))
							.getInvStatusCd()
							== liNewInvStatusCd
							&& (((InventoryAllocationData) lvInvAllocData
								.get(i))
								.getOfcIssuanceNo()
								!= laInvAllocDataOrig.getOfcIssuanceNo()
								|| (
									!(
										(InventoryAllocationData) lvInvAllocData
									.get(i))
									.getTransEmpId()
									.equalsIgnoreCase(
										laInvAllocDataOrig
											.getTransEmpId()))
								|| (
									!(
										(InventoryAllocationData) lvInvAllocData
									.get(i))
									.getRequestorIpAddress()
									.equalsIgnoreCase(
										laInvAllocDataOrig
											.getRequestorIpAddress()))))
					{
						// defect 9252
						// write a message to the log if we get this 
						// condition and logging is in debug mode
						Log.write(
							Log.DEBUG,
							(
								InventoryAllocationData) lvInvAllocData
									.get(
								i),
							"VI Item belongs to a different user "
								+ laInvAllocDataOrig.getItmCd()
								+ " "
								+ laInvAllocDataOrig.getTransEmpId()
								+ " "
								+ laInvAllocDataOrig.getTransWsId()
								+ " "
								+ csClientHost);
						// end defect 9252

						// Can not change hold status if you are not 
						//  the same requestor
						RTSException leRTSException =
							new RTSException(
								ErrorsConstant
									.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN);
						throw leRTSException;
					}
					else if (
						((InventoryAllocationData) lvInvAllocData
							.get(i))
							.getInvStatusCd()
							== liNewInvStatusCd
							&& liNewInvStatusCd
								!= InventoryConstant.HOLD_INV_SYSTEM
							&& !((InventoryAllocationData) lvInvAllocData
								.get(i))
								.isVirtual())
					{
						// defect 9252
						// write a message to the log if we get this 
						// condition and logging is in debug mode
						Log.write(
							Log.DEBUG,
							(
								InventoryAllocationData) lvInvAllocData
									.get(
								i),
							"VI Item is on hold and is not virtual "
								+ laInvAllocDataOrig.getItmCd()
								+ " "
								+ laInvAllocDataOrig.getTransEmpId()
								+ " "
								+ laInvAllocDataOrig.getTransWsId()
								+ " "
								+ csClientHost);
						// end defect 9252
						// Do not throw a 593 if this is virtual and 
						// the hold code is 2.  We can be just updating
						// it.
						RTSException leRTSException =
							new RTSException(
								ErrorsConstant
									.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN);
						throw leRTSException;
					}
				}
			}

			// Check for complete range and then split
			if (!lbPLPIndi)
			{
				chkForVICompleteRngeAndSplt(
					laInvData,
					lvInvAllocData,
					laDBA);
			}

			// Reselect ranges to place on hold
			if (!lbPLPIndi)
			{
				lvInvAllocData =
					chkVirtualInvItmRange(laInvData, laDBA);
			}
			else
			{
				lvInvAllocData =
					chkVirtualInvForSpecificItem(laInvData, laDBA);
			}

			// Update inventory status to hold by RTS
			for (int i = 0; i < lvInvAllocData.size(); i++)
			{
				laUpdateInvAllocData =
					(InventoryAllocationData) lvInvAllocData.get(i);
				laUpdateInvAllocData.setInvStatusCd(liNewInvStatusCd);

				laUpdateInvAllocData.setOfcIssuanceNo(
					laInvAllocDataOrig.getOfcIssuanceNo());
				laUpdateInvAllocData.setItrntReq(
					laInvAllocDataOrig.isItrntReq());
				laUpdateInvAllocData.setTransAmDate(
					laInvAllocDataOrig.getTransAmDate());
				laUpdateInvAllocData.setTransWsId(
					laInvAllocDataOrig.getTransWsId());
				laUpdateInvAllocData.setTransEmpId(
					laInvAllocDataOrig.getTransEmpId());
				laUpdateInvAllocData.setRequestorIpAddress(
					laInvAllocDataOrig.getRequestorIpAddress());

				// Aren't changing the substaid so set the NewSubstaId = 
				// Integer.Min_Value
				laUpdateInvAllocData.setNewSubstaId(Integer.MIN_VALUE);
				//InventoryAllocation laInvAlloctnSQL =
				//	new InventoryAllocation(laDBA);
				InventoryVirtual laInventoryVirtualSQL =
					new InventoryVirtual(laDBA);
				laInventoryVirtualSQL.updInventoryVirtual(
					laUpdateInvAllocData,
					csClientHost);
			}

			// Reselect ranges for return
			if (!lbPLPIndi)
			{
				lvInvAllocData =
					chkVirtualInvItmRange(laInvData, laDBA);
			}
			else
			{
				//lvInvAllocData = chkForPLP(laInvAllocDataOrig, laDBA);
				lvInvAllocData =
					chkVirtualInvForSpecificItem(laInvData, laDBA);
			}
			if (aaDBA == null)
			{
				laDBA.endTransaction(DatabaseAccess.COMMIT);
				//logUOW("updVIStatusCd", ":COMMIT");
			}

			if (lvInvAllocData.size() < 1)
			{
				throw new RTSException(
					ErrorsConstant.ERR_NUM_VI_UNAVAILABLE);
			}

			return lvInvAllocData;
		}
		catch (NullPointerException aeNPEx)
		{
			// if we get a null pointer, make sure we do a rollback
			if (laDBA != null)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			else if (aaDBA == null && laDBA != null)
			{
				aaDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("updVIStatusCd", ": ROLLBACK");
			}
			throw new RTSException(RTSException.SERVER_DOWN, aeNPEx);
		}
		catch (RTSException aeRTSEx)
		{
			// defect 9323
			// if no records found, let caller decide on rollback
			if (aaDBA == null && laDBA != null)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("updVIStatusCd", ": ROLLBACK");
				// end defect 9323
			}
			else if (
				laDBA != null
					&& aeRTSEx.getCode()
						!= ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB
					&& aeRTSEx.getCode()
						!= ErrorsConstant
							.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN
					&& aeRTSEx.getCode()
						!= ErrorsConstant.ERR_NUM_ITEM_ON_HOLD
					&& aeRTSEx.getCode()
						!= ErrorsConstant.ERR_NUM_INCOMPLETE_RANGE
					&& aeRTSEx.getCode()
						!= ErrorsConstant.ERR_NUM_PATRN_SEQ_NOT_RIGHT
					&& aeRTSEx.getCode()
						!= ErrorsConstant.ERR_NUM_VI_NEXTITEM_UNAVAILABLE)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}

			throw aeRTSEx;
		}
		finally
		{
			if (aaDBA == null)
			{
				laDBA = null;
			}
			Log.write(
				Log.METHOD,
				this,
				MSG_UPDINVSTATUSCD_END + csClientHost);
		}
		// end defect 9404 
	}

	/**
	 * Attempt to change the inv status of an item.  
	 * If the item is not found, try to insert it.
	 * 
	 * @param aaDBA DatabaseAccess
	 * @param aaInvAllocData InventoryAllocationData
	 * @return Vector
	 * @throws RTSException
	 */
	private Vector updVIStatusCdRecover(
		DatabaseAccess aaDBA,
		InventoryAllocationData aaInvAllocData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updViInvStatusCdRecover - Begin " + csClientHost);

		// make the label to the local DBA object
		DatabaseAccess laDBA = null;

		try
		{
			if (aaDBA != null)
			{
				laDBA = aaDBA;
			}
			else
			{
				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();
				//logUOW("updVIStatusCdRecover", ": BEGIN");
			}

			// make a copy of the original data
			InventoryAllocationData laInvAllocDataOrig =
				(InventoryAllocationData) UtilityMethods.copy(
					aaInvAllocData);

			// create the update object
			InventoryAllocationData laInvAllocDataUpdate =
				(InventoryAllocationData) UtilityMethods.copy(
					aaInvAllocData);

			// check for ViItmCd
			if (laInvAllocDataUpdate.getViItmCd() == null
				|| laInvAllocDataUpdate.getViItmCd().equals(
					CommonConstant.STR_SPACE_EMPTY))
			{
				// attempt to lookup the ViItmCd
				try
				{
					laInvAllocDataUpdate.setItmCd(
						lookupVirtualForItem(laInvAllocDataUpdate));
				}
				catch (RTSException aeRTSExLookup)
				{
					// if the error code is 1002, just use the normal 
					// itmcd provided.
					// Otherwise, throw the execption.
					//if (aeRTSExLookup.getCode()
					//	!= ErrorsConstant.ERR_NUM_VI_NEXTITEM_UNAVAILABLE)
					//{
					throw aeRTSExLookup;
					//}
				}

				laInvAllocDataUpdate.setViItmCd(
					laInvAllocDataUpdate.getItmCd());
				laInvAllocDataOrig.setViItmCd(
					laInvAllocDataUpdate.getItmCd());

				// if using real virtual, set year to 0
				if (!laInvAllocDataOrig
					.getItmCd()
					.equalsIgnoreCase(laInvAllocDataUpdate.getItmCd()))
				{
					laInvAllocDataUpdate.setInvItmYr(0);
					laInvAllocDataUpdate.setPatrnSeqCd(0);
				}
				laInvAllocDataUpdate.setVirtual(true);
			}
			else
			{
				laInvAllocDataUpdate.setItmCd(
					laInvAllocDataOrig.getViItmCd());

				// if using real virtual, set year to 0
				if (!laInvAllocDataOrig
					.getItmCd()
					.equalsIgnoreCase(laInvAllocDataUpdate.getItmCd()))
				{
					laInvAllocDataUpdate.setInvItmYr(0);
					laInvAllocDataUpdate.setPatrnSeqCd(0);
				}
			}

			// calc the item
			laInvAllocDataUpdate.setCalcInv(false);
			ValidateInventoryPattern laVIP =
				new ValidateInventoryPattern();
			laInvAllocDataUpdate =
				(InventoryAllocationData) laVIP.calcInvUnknownVI(
					laInvAllocDataUpdate);

			Vector lvReturnData = null;

			// attempt to update the row
			try
			{
				laInvAllocDataUpdate.setInvStatusCd(
					laInvAllocDataOrig.getInvStatusCd());
				lvReturnData =
					updVIStatusCd(laDBA, laInvAllocDataUpdate);

				if (lvReturnData == null)
				{
					throw new RTSException(
						ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB);
				}

				lvReturnData = new Vector(1);
				laInvAllocDataOrig.setViItmCd(
					laInvAllocDataUpdate.getViItmCd());
				lvReturnData.add(laInvAllocDataOrig);

			}
			catch (RTSException aeRTSEx2)
			{
				if (aeRTSEx2.getCode()
					== ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB)
				{
					// the item was not found, lets try an insert.
					aeRTSEx2 = null;
					laInvAllocDataUpdate.setCustSupplied(true);
					// defect 10253
					// The status needs to be a 2 as well.
					laInvAllocDataUpdate.setInvStatusCd(
						InventoryConstant.HOLD_INV_SYSTEM);
					// end defect 10253

					laInvAllocDataUpdate =
						(InventoryAllocationData) insrtViRow(laDBA,
							laInvAllocDataUpdate);

					lvReturnData = new Vector(1);
					laInvAllocDataOrig.setCustSupplied(true);
					laInvAllocDataOrig.setViItmCd(
						laInvAllocDataUpdate.getViItmCd());
					lvReturnData.add(laInvAllocDataOrig);
				}
				else
				{
					throw aeRTSEx2;
				}
			}

			if (aaDBA == null)
			{
				laDBA.endTransaction(DatabaseAccess.COMMIT);
				//logUOW("updVIStatusCdRecover", ": COMMIT");
			}

			return lvReturnData;
		}
		catch (NullPointerException aeNPEx)
		{
			// if we get a null pointer, make sure we do a rollback
			if (laDBA != null)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("updVIStatusCdRecover", ": ROLLBACK");
			}
			throw new RTSException(RTSException.SERVER_DOWN, aeNPEx);
		}
		catch (RTSException aeRTSEx)
		{
			// defect 9323 
			// If passed DBA is null, do a rollback
			if (aaDBA == null)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("updVIStatusCdRecover", ": ROLLBACK");
			}
			// if no records found, let caller decide on rollback
			else if (
				laDBA != null
					&& (aeRTSEx.getCode()
						!= ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB
						&& aeRTSEx.getCode() != 1002))
			{
				// end defect 9323
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}

			throw aeRTSEx;
		}
		finally
		{
			if (aaDBA == null)
			{
				laDBA = null;
			}
			Log.write(
				Log.METHOD,
				this,
				MSG_UPDINVSTATUSCD_END + csClientHost);
		}
	}

	/**
	 * Update the selected record with the order complete hold code.
	 * 
	 * @param aaDBA DatabaseAccess
	 * @param aaInvAllocData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object updViStatusCodeComplete(
		DatabaseAccess aaDBA,
		Object aaInvAllocData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updViStatusCodeComplete - Begin " + csClientHost);

		DatabaseAccess laDBA = null;

		try
		{
			if (aaDBA != null)
			{
				laDBA = aaDBA;
			}
			else
			{
				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();
				//logUOW("updViStatusCodeComplete", ": BEGIN");
			}

			InventoryAllocationData laInvDataOrig =
				(InventoryAllocationData) aaInvAllocData;

			InventoryAllocationData laInvDataUpdate =
				(InventoryAllocationData) UtilityMethods.copy(
					laInvDataOrig);

			if (laInvDataUpdate.getViItmCd() == null
				|| laInvDataUpdate.getViItmCd().equals(
					CommonConstant.STR_SPACE_EMPTY))
			{
				laInvDataUpdate.setItmCd(
					lookupVirtualForItem(laInvDataUpdate));
				laInvDataUpdate.setViItmCd(laInvDataUpdate.getItmCd());
				laInvDataOrig.setViItmCd(laInvDataUpdate.getItmCd());

				// if using real virtual, set year to 0
				if (!laInvDataOrig
					.getItmCd()
					.equalsIgnoreCase(laInvDataOrig.getViItmCd()))
				{
					laInvDataUpdate.setPatrnSeqCd(0);
					laInvDataUpdate.setInvItmYr(0);
				}

				laInvDataOrig.setVirtual(true);
			}
			else
			{
				laInvDataUpdate.setItmCd(laInvDataUpdate.getViItmCd());

				// if using real virtual, set year to 0
				if (!laInvDataOrig
					.getItmCd()
					.equalsIgnoreCase(laInvDataOrig.getViItmCd()))
				{
					laInvDataUpdate.setPatrnSeqCd(0);
					laInvDataUpdate.setInvItmYr(0);
				}
			}

			// we need to re-validate the pattern, so set
			// PatrnSeqNo and EndPatrnSeqNo = 0.
			laInvDataUpdate.setPatrnSeqNo(0);
			laInvDataUpdate.setEndPatrnSeqNo(0);
			laInvDataUpdate.setCalcInv(false);

			// needed to run calculation 
			laInvDataUpdate.setItmCd(laInvDataUpdate.getViItmCd());

			laInvDataUpdate.setInvStatusCd(
				InventoryConstant.HOLD_INV_ORDER_COMPLETE);

			Vector lvUpdate = updVIStatusCd(laDBA, laInvDataUpdate);

			InventoryAllocationData laUpdatedInvData =
				(InventoryAllocationData) lvUpdate.elementAt(0);

			if (aaDBA == null)
			{
				laDBA.endTransaction(DatabaseAccess.COMMIT);
				//logUOW("updViStatusCodeComplete", ": COMMIT");
			}

			// change the status cd and return the original data
			laInvDataOrig.setInvStatusCd(
				laUpdatedInvData.getInvStatusCd());
			return laInvDataOrig;
		}
		catch (RTSException aeRTSEx)
		{
			// if no records found, let caller decide on rollback
			if (laDBA != null
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_ITEM_ON_HOLD
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_INCOMPLETE_RANGE
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_PATRN_SEQ_NOT_RIGHT
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_VI_NEXTITEM_UNAVAILABLE)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("updViStatusCodeComplete", ": ROLLBACK");
			}

			throw aeRTSEx;

		}
		finally
		{
			if (aaDBA == null)
			{
				laDBA = null;
			}
			Log.write(
				Log.METHOD,
				this,
				"updViStatusCodeComplete - End" + csClientHost);
		}
	}

	/**
	 * Confirm that the item is still on hold for the requestor.
	 * 
	 * @param aaDBA DatabaseAccess
	 * @param aaInvAllocData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object updViStatusCodeConfirm(
		DatabaseAccess aaDBA,
		Object aaInvAllocData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updViStatusCodeConfirm - Begin" + csClientHost);

		DatabaseAccess laDBA = null;

		// defect 9404
		// Add logging  
		try
		{
			if (aaDBA != null)
			{
				laDBA = aaDBA;
			}
			else
			{
				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();
				//logUOW("updViStatusCodeConfirm", ": BEGIN");
			}
			InventoryAllocationData laInvData =
				(InventoryAllocationData) aaInvAllocData;

			InventoryAllocationData laInvDataUpdate =
				(InventoryAllocationData) UtilityMethods.copy(
					laInvData);

			// if the ViItmCd is empty, compute it.
			if (laInvDataUpdate.getViItmCd() == null
				|| laInvDataUpdate.getViItmCd().equals(
					CommonConstant.STR_SPACE_EMPTY))
			{
				laInvDataUpdate.setItmCd(
					lookupVirtualForItem(laInvDataUpdate));
				laInvDataUpdate.setViItmCd(laInvDataUpdate.getItmCd());
				laInvData.setViItmCd(laInvDataUpdate.getItmCd());

				// if using real virtual, set year to 0
				if (!laInvData
					.getItmCd()
					.equalsIgnoreCase(laInvData.getItmCd()))
				{
					laInvData.setInvItmYr(0);
					laInvData.setPatrnSeqCd(0);
				}

				laInvData.setVirtual(true);
			}
			else
			{
				laInvDataUpdate.setItmCd(laInvDataUpdate.getViItmCd());

				// if using real virtual, set year to 0
				if (!laInvData
					.getItmCd()
					.equalsIgnoreCase(laInvData.getItmCd()))
				{
					laInvData.setInvItmYr(0);
					laInvData.setPatrnSeqCd(0);
				}
			}

			// we need to re-validate the pattern, so set
			// PatrnSeqNo and EndPatrnSeqNo = 0.
			laInvDataUpdate.setPatrnSeqNo(0);
			laInvDataUpdate.setEndPatrnSeqNo(0);
			laInvDataUpdate.setCalcInv(false);

			Vector lvQuery =
				chkVirtualInvForSpecificItem(laInvDataUpdate, laDBA);

			if (lvQuery.size() < 1)
			{
				throw new RTSException(
					ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB);
			}

			InventoryAllocationData laInvDataResult =
				(InventoryAllocationData) lvQuery.elementAt(0);

			// defect 9679
			// modify so we do not update the record if a transtime is set
			// make sure the item is still on hold for the requestor
			if (laInvData.getOfcIssuanceNo()
				== laInvDataResult.getOfcIssuanceNo()
				&& laInvData.getTransEmpId().equalsIgnoreCase(
					laInvDataResult.getTransEmpId())
				&& laInvData.getRequestorIpAddress().equalsIgnoreCase(
					laInvDataResult.getRequestorIpAddress())
				&& laInvDataResult.getTransTime() == 0)
			{
				// end defect 9679
				InventoryAllocationData laUpdatedInvData =
					(InventoryAllocationData) updViTransTime(laDBA,
						laInvDataUpdate);

				laInvData.setInvHoldTimeStmp(
					laUpdatedInvData.getInvHoldTimeStmp());

				if (aaDBA == null)
				{
					laDBA.endTransaction(DatabaseAccess.COMMIT);
					//logUOW("updViStatusCodeConfirm", ": COMMIT");
				}

				return laInvData;
				// defect 9679
			}
			else if (
				laInvData.getOfcIssuanceNo()
					== laInvDataResult.getOfcIssuanceNo()
					&& laInvData.getTransEmpId().equalsIgnoreCase(
						laInvDataResult.getTransEmpId())
					&& laInvData.getRequestorIpAddress().equalsIgnoreCase(
						laInvDataResult.getRequestorIpAddress())
					&& laInvDataResult.getTransTime() != 0)
			{
				// just return the record
				return laInvData;
			}
			else
			{
				// end defect 9679

				// throw the exception because the item no 
				// longer belongs to the requestor.
				throw new RTSException(
					ErrorsConstant.ERR_NUM_VI_ITEM_NOT_RESERVED);
			}
		}
		catch (RTSException aeRTSEx)
		{
			// defect 9323
			if (aaDBA == null && laDBA != null)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("updViStatusCodeConfirm", ": ROLLBACK");
			}
			else if (
				laDBA != null
					&& (aeRTSEx.getCode()
						!= ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB
						&& aeRTSEx.getCode() != 1002))
			{
				// end defect 9323
				// if no records found, let caller decide on rollback
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("updViStatusCodeConfirm", ": ROLLBACK");
			}

			throw aeRTSEx;
		}
		finally
		{
			if (aaDBA == null)
			{
				laDBA = null;
			}
			Log.write(
				Log.METHOD,
				this,
				"updViStatusCodeConfirm - End " + csClientHost);
		}
		// end defect 9404 
	}

	/**
	 * Confirm that the item is still on hold for the requestor.
	 * 
	 * @param aaDBA DatabaseAccess
	 * @param aaInvAllocData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object updViStatusCodeRelease(
		DatabaseAccess aaDBA,
		Object aaInvAllocData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updViStatusCodeRelease - Begin " + csClientHost);

		DatabaseAccess laDBA = null;

		// defect 9404
		// Add logging  
		try
		{
			if (aaDBA != null)
			{
				laDBA = aaDBA;
			}
			else
			{
				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();
				//logUOW("updViStatusCodeRelease", ": BEGIN");
			}

			InventoryAllocationData laInvData =
				(InventoryAllocationData) aaInvAllocData;

			InventoryAllocationData laInvDataUpdate =
				(InventoryAllocationData) UtilityMethods.copy(
					laInvData);

			// if the ViItmCd is empty, compute it. 
			if (laInvDataUpdate.getViItmCd() == null
				|| laInvDataUpdate.getViItmCd().equals(
					CommonConstant.STR_SPACE_EMPTY))
			{
				laInvDataUpdate.setItmCd(
					lookupVirtualForItem(laInvDataUpdate));
				laInvDataUpdate.setViItmCd(laInvDataUpdate.getItmCd());
				laInvData.setViItmCd(laInvDataUpdate.getItmCd());

				// if using real virtual, set year to 0
				if (!laInvData
					.getItmCd()
					.equalsIgnoreCase(laInvDataUpdate.getItmCd()))
				{
					laInvDataUpdate.setInvItmYr(0);
					laInvDataUpdate.setPatrnSeqCd(0);
				}

				laInvData.setVirtual(true);
			}
			else
			{
				laInvDataUpdate.setItmCd(laInvDataUpdate.getViItmCd());

				// if using real virtual, set year to 0
				if (!laInvData
					.getItmCd()
					.equalsIgnoreCase(laInvDataUpdate.getItmCd()))
				{
					laInvDataUpdate.setInvItmYr(0);
					laInvDataUpdate.setPatrnSeqCd(0);
				}
			}

			// we need to re-validate the pattern, so set
			// PatrnSeqNo and EndPatrnSeqNo = 0.
			laInvDataUpdate.setPatrnSeqNo(0);
			laInvDataUpdate.setEndPatrnSeqNo(0);
			laInvDataUpdate.setCalcInv(false);

			Vector lvQuery =
				chkVirtualInvForSpecificItem(laInvDataUpdate, laDBA);

			if (lvQuery.size() < 1)
			{
				throw new RTSException(
					ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB);
			}

			InventoryAllocationData laInvDataResult =
				(InventoryAllocationData) lvQuery.elementAt(0);

			// If it is not on hold, we can act like we took it off hold.
			// If it is on system hold, it must belong to the office
			// and employee along with some other considerations.
			// defect 9782
			// If the TransTime is not 0 and the request is not POS SP, 
			// it can not be released!!  It has been paid for.
			// SPECIAL NOTE FOR POS
			// POS Void will not have the ReqIpAddr, so it is null!
			// Can not avoid the null check.
			// Bypass TransEmpId check on void. (ReqIpAddr == null)
			if ((laInvDataResult.getInvStatusCd()
				== InventoryConstant.HOLD_INV_NOT)
				|| (laInvData.getOfcIssuanceNo()
					== laInvDataResult.getOfcIssuanceNo()
					&& (laInvData.getRequestorIpAddress() == null
						|| laInvData.getTransEmpId().equalsIgnoreCase(
							laInvDataResult.getTransEmpId()))
					&& (laInvData.getRequestorIpAddress() == null
						|| laInvData
							.getRequestorIpAddress()
							.equalsIgnoreCase(
							laInvDataResult.getRequestorIpAddress()))
					&& (laInvDataResult.getTransWsId() < INV_MAX_TRANSWSID
						|| (laInvDataResult.getTransWsId()
							>= INV_MAX_TRANSWSID
							&& laInvDataResult.getTransTime() == 0))))
			{
				// end defect 9782
				InventoryAllocationData laUpdatedInvData = null;

				if (!laInvDataResult.isCustSupplied())
				{
					// normal release of the hold.
					// The plate number is provided by the system.
					laInvDataResult.setInvStatusCd(
						InventoryConstant.HOLD_INV_NOT);

					// clear out the requestor information
					laInvDataResult.setTransAmDate(0);
					laInvDataResult.setTransTime(0);
					laInvDataResult.setTransWsId(0);
					laInvDataResult.setTransEmpId("");
					laInvDataResult.setItrntReq(false);

					Vector lvUpdate =
						updVIStatusCd(laDBA, laInvDataResult);
					laUpdatedInvData =
						(InventoryAllocationData) lvUpdate.elementAt(0);
				}
				else
				{
					// delete the item if the plate number is provided 
					// by the customer.
					laUpdatedInvData =
						(InventoryAllocationData) delInvVirtual(laDBA,
							laInvDataResult);

					// if the row was completed, this delete is as a 
					// result of a void.  Record it to the activity log.
					if (laInvDataResult.getInvStatusCd()
						> InventoryConstant.HOLD_INV_SYSTEM)
					{
						AdministrationLog laAdminLog =
							new AdministrationLog(laDBA);
						AdministrationLogData laLogData =
							new AdministrationLogData();
						// defect 8595 
						laLogData.setAction(
							CommonConstant.TXT_ADMIN_LOG_DELETE);
						// end defect 8595 
						laLogData.setEntity("VI CS");
						laLogData.setOfcIssuanceNo(
							laInvData.getOfcIssuanceNo());
						laLogData.setSubStaId(laInvData.getSubstaId());
						laLogData.setTransEmpId(
							laInvData.getTransEmpId());
						laLogData.setTransAMDate(
							RTSDate.getCurrentDate().getAMDate());
						laLogData.setTransTime(
							laInvData.getTransTime());
						laLogData.setTransWsId(
							laInvData.getTransWsId());
						laLogData.setEntityValue(
							laInvData.getInvItmNo());
						laAdminLog.insAdministrationLog(laLogData);
					}
				}

				if (aaDBA == null)
				{
					laDBA.endTransaction(DatabaseAccess.COMMIT);
					//logUOW("updViStatusCodeRelease", ": COMMIT");
				}

				// return the result of the update
				return laUpdatedInvData;
			}
			else
			{
				// throw the exception because the item no 
				// longer belongs to the requestor.
				// defect 10253 
				// use the 593 error code
				throw new RTSException(
					ErrorsConstant.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN);
				// end defect 10253
			}
		}
		catch (RTSException aeRTSEx)
		{
			if (aaDBA == null && laDBA != null)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("updViStatusCodeRelease", ": ROLLBACK");
			}
			// defect 10253
			// use 593 for hold problems
			if (laDBA != null
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_VI_NEXTITEM_UNAVAILABLE
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN)
			{
				// end defect 10253
				// end defect 9323
				// if no records found, let caller decide on rollback
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("updViStatusCodeRelease", ": ROLLBACK");
			}

			throw aeRTSEx;
		}
		finally
		{
			if (aaDBA == null)
			{
				laDBA = null;
			}
			Log.write(
				Log.METHOD,
				this,
				"updViStatusCodeRelease - End " + csClientHost);
		}
		// end defect 9404 
	}

	/**
	 * Updates the TransTime of the selected record.
	 * 
	 * @param aaDBA DatabaseAccess
	 * @param aaInvAllocData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object updViTransTime(
		DatabaseAccess aaDBA,
		Object aaInvAllocData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updViTransTime - Begin " + csClientHost);

		DatabaseAccess laDBA = null;

		// defect 9404
		// Add logging  
		try
		{
			if (aaDBA != null)
			{
				laDBA = aaDBA;
			}
			else
			{
				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();
				//logUOW("updViTransTime", ": BEGIN");
			}

			// make a copy of the original data
			InventoryAllocationData laInvAllocDataOrig =
				(InventoryAllocationData) UtilityMethods.copy(
					aaInvAllocData);

			// setup the update data
			Vector lvInvAllocData = new Vector();
			boolean lbPLPIndi = false;

			// Create working copy of object
			InventoryAllocationData laInvData =
				(InventoryAllocationData) UtilityMethods.copy(
					laInvAllocDataOrig);

			// check on VI itm cd status
			if (laInvData.getViItmCd() == null
				|| laInvData.getViItmCd().equals(
					CommonConstant.STR_SPACE_EMPTY))
			{
				laInvData.setItmCd(lookupVirtualForItem(laInvData));
				laInvData.setViItmCd(laInvData.getItmCd());
				laInvAllocDataOrig.setViItmCd(laInvData.getItmCd());

				// if using real virtual, set year to 0
				if (!laInvData
					.getItmCd()
					.equalsIgnoreCase(laInvAllocDataOrig.getItmCd()))
				{
					laInvData.setInvItmYr(0);
					laInvData.setPatrnSeqCd(0);
					laInvData.setCalcInv(false);
				}

				laInvData.setVirtual(true);
			}
			else
			{
				laInvData.setItmCd(laInvAllocDataOrig.getViItmCd());

				// if using real virtual, set year to 0
				if (!laInvData
					.getItmCd()
					.equalsIgnoreCase(laInvAllocDataOrig.getItmCd()))
				{
					laInvData.setInvItmYr(0);
					laInvData.setPatrnSeqCd(0);
					laInvData.setCalcInv(false);
				}
			}

			// Check if item is a personalized plate
			lbPLPIndi =
				ValidateInventoryPattern.chkIfItmPLPOrOLDPLTOrROP(
					laInvAllocDataOrig.getItmCd());

			// Validate and set up inventory information and then make 
			// call to  Inventory Allocation table
			if (!lbPLPIndi)
			{
				// if endpatrnseqno = 0, calculate
				if (laInvData.getEndPatrnSeqNo() == 0)
				{
					laInvData.setCalcInv(false);
					laInvAllocDataOrig.setCalcInv(false);
				}

				// see if calculation is needed.
				if (!laInvData.isCalcInv())
				{
					ValidateInventoryPattern laVIP =
						new ValidateInventoryPattern();
					laInvData =
						(
							InventoryAllocationData) laVIP
								.calcInvUnknownVI(
							laInvData);

					// Calculate the EndPatrnSeqNo
					// if qty is one, just use the begin patrnseqno.
					// otherwise calculate.
					if (laInvData.getInvQty() != 1)
					{
						laInvData.setItmCd(
							laInvAllocDataOrig.getItmCd());
						laInvData.setInvItmYr(
							laInvAllocDataOrig.getInvItmYr());
						laInvData.setInvQty(
							laInvAllocDataOrig.getInvQty());
						laInvData.setInvItmNo(
							laInvAllocDataOrig.getInvItmEndNo());
						laInvData.setInvItmEndNo(
							laInvAllocDataOrig.getInvItmEndNo());

						laInvData =
							(
								InventoryAllocationUIData) laVIP
									.calcInvUnknown(
								laInvData);
						laInvAllocDataOrig.setEndPatrnSeqNo(
							laInvData.getPatrnSeqNo());
						laInvAllocDataOrig.setCalcInv(
							laInvData.isCalcInv());
						// Verify begin and end numbers have the same 
						// PatrnSeqCd
						if (laInvAllocDataOrig.getPatrnSeqCd()
							!= laInvData.getPatrnSeqCd())
						{
							RTSException leRTSException =
								new RTSException(607);
							throw leRTSException;
						}
					}
					else
					{
						laInvAllocDataOrig.setPatrnSeqNo(
							laInvData.getPatrnSeqNo());
						laInvAllocDataOrig.setEndPatrnSeqNo(
							laInvData.getPatrnSeqNo());
						laInvAllocDataOrig.setInvItmEndNo(
							laInvAllocDataOrig.getInvItmNo());
					}
				}
				lvInvAllocData =
					chkVirtualInvItmRange(laInvData, laDBA);
			}
			else
			{
				laInvAllocDataOrig.setInvQty(1);
				laInvAllocDataOrig.setPatrnSeqCd(-1);
				laInvAllocDataOrig.setPatrnSeqNo(Integer.MIN_VALUE);
				laInvAllocDataOrig.setEndPatrnSeqNo(Integer.MIN_VALUE);
				lvInvAllocData =
					chkVirtualInvForSpecificItem(
						laInvAllocDataOrig,
						laDBA);
			}

			if (lvInvAllocData.size() > 0)
			{
				InventoryVirtual laIV = new InventoryVirtual(laDBA);
				for (Iterator laIter = lvInvAllocData.iterator();
					laIter.hasNext();
					)
				{
					InventoryAllocationData laIADUpdate =
						(InventoryAllocationData) laIter.next();

					// validate right record.
					if (laInvAllocDataOrig.getOfcIssuanceNo()
						== laIADUpdate.getOfcIssuanceNo()
						&& laInvAllocDataOrig.getTransWsId()
							== laIADUpdate.getTransWsId()
						&& laInvAllocDataOrig.getTransEmpId().equals(
							laIADUpdate.getTransEmpId())
						&& (laInvAllocDataOrig
							.getRequestorIpAddress()
							.equals(
								laIADUpdate.getRequestorIpAddress()))
						&& laIADUpdate.getInvStatusCd()
							== InventoryConstant.HOLD_INV_SYSTEM)
					{
						// Do the update.
						laIADUpdate.setTransTime(
							laInvAllocDataOrig.getTransTime());
						laIV.updInventoryVirtual(
							laIADUpdate,
							csClientHost);
					}
					else
					{
						// TODO we have an invalid record.
						laIter.remove();
						if (lvInvAllocData.size() < 1)
						{
							throw new RTSException(
								ErrorsConstant
									.ERR_NUM_NO_RECORDS_IN_DB);
						}
					}
				}
			}
			else
			{
				// no records found exception
				throw new RTSException(
					ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB);
			}

			// close out if dba was not passed.
			if (aaDBA == null)
			{
				laDBA.endTransaction(DatabaseAccess.COMMIT);
				//logUOW("updViTransTime", ": COMMIT");
			}

			return aaInvAllocData;
		}
		catch (RTSException aeRTSEx)
		{
			// defect 9323
			if (aaDBA == null && laDBA != null)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("updViTransTime", ": ROLLBACK");
			}
			if (laDBA != null
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_ITEM_ON_HOLD
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_INCOMPLETE_RANGE
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_PATRN_SEQ_NOT_RIGHT
				&& aeRTSEx.getCode()
					!= ErrorsConstant.ERR_NUM_VI_NEXTITEM_UNAVAILABLE)
			{
				// end defect 9323
				// if no records found, let caller decide on rollback
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("updViTransTime", ": ROLLBACK");
			}

			throw aeRTSEx;
		}
		finally
		{
			if (aaDBA == null)
			{
				laDBA = null;
			}
			Log.write(
				Log.METHOD,
				this,
				"updViTransTime - End " + csClientHost);
		}
		// end defect 9404 
	}

	/**
	 * Used to validate the invoice line items.  First, checks to see if 
	 * the item code, year, and number are valid.  Second, checks to 
	 * make sure the invoice item does not intersect with any of the 
	 * previous items on the invoice.  And third, checks
	 * to see if the invoice item is already in the database.
	 *
	 * <p>Paramter avData is a vector where the elements have to be as 
	 * follows:
	 * <ul>
	 * <li>Element(0) - A vector of SubstationData that contains all 
	 * 					the substations.
	 * <li>Element(1) - The invoice as a MFInventoryAllocationData 
	 * 					object.
	 * <eul>
	 * 
	 * @param avData Vector
	 * @return Vector
	 * @throws RTSException  
	 */
	private Vector validateInvcItms(Vector avData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			MSG_VALIDATEINVCITMS_BEGIN + csClientHost);

		Vector lvSubstaData =
			(Vector) ((Vector) avData).get(CommonConstant.ELEMENT_0);
		MFInventoryAllocationData laMFInvAlloctnData =
			(MFInventoryAllocationData) ((Vector) avData).get(
				CommonConstant.ELEMENT_1);
		DatabaseAccess laDBA = new DatabaseAccess();
		ValidateInventoryPattern laValidateInvPatrn =
			new ValidateInventoryPattern();
		String lsSubstaInvExistsIn =
			new String(CommonConstant.STR_SPACE_EMPTY);

		// Initialize an array that contains the invoice item's patern 
		// sequence code
		//	Array[y][0] = ItmPatrnSeqCd
		//  Array[y][1] = ItmPatrnSeqNoBeg
		//  Array[y][2] = ItmPatrnSeqNoEnd
		int[][] larrPatrnSeqNo =
			new int[laMFInvAlloctnData
				.getInvAlloctnData()
				.size()][CommonConstant
				.ELEMENT_3];

		for (int i = 0;
			i < laMFInvAlloctnData.getInvAlloctnData().size();
			i++)
		{
			boolean lbPersonalizedItmIndi = false;
			boolean lbInvalidItm = false;
			InventoryAllocationUIData laInvAlloctnUIData =
				(InventoryAllocationUIData) laMFInvAlloctnData
					.getInvAlloctnData()
					.get(
					i);

			// Validate the item code
			ItemCodesData laICD =
				ItemCodesCache.getItmCd(laInvAlloctnUIData.getItmCd());
			if (laICD == null)
			{
				laInvAlloctnUIData.setStatusCd(
					InventoryConstant.DELETE);
				laInvAlloctnUIData.setErrorCd(399);
				ErrorMessagesData laEMD =
					ErrorMessagesCache.getErrMsg(399);

				laInvAlloctnUIData.setStatusDesc(
					InventoryConstant.REJECTED
						+ CommonConstant.STR_SPACE_ONE
						+ laEMD.getErrMsgDesc());
				laInvAlloctnUIData.setOrigItmModfyIndi(
					InventoryConstant.ITM_INDI_REMOVED);
				continue;
			}

			// Reject item if printable 
			// Determine if printable
			if (StickerPrintingUtilities
				.isStickerPrintable(laICD.getItmCd()))
			{
				laInvAlloctnUIData.setStatusCd(
					InventoryConstant.DELETE);
				laInvAlloctnUIData.setErrorCd(2005);
				ErrorMessagesData laEMD =
					ErrorMessagesCache.getErrMsg(2005);

				laInvAlloctnUIData.setStatusDesc(
					InventoryConstant.REJECTED
						+ CommonConstant.STR_SPACE_ONE
						+ laEMD.getErrMsgDesc());
				laInvAlloctnUIData.setOrigItmModfyIndi(
					InventoryConstant.ITM_INDI_REMOVED);
				continue;
			}

			// Test if the processing code is not equal to 1 or 2.  
			// Only throw an error if the office code is not equal to 2.
			if (laICD.getInvProcsngCd() != 1
				&& laICD.getInvProcsngCd() != 2
				&& laMFInvAlloctnData.getMFInvAckData().getOfcIssuanceCd()
					!= 2)
			{
				laInvAlloctnUIData.setStatusCd(
					InventoryConstant.DELETE);
				laInvAlloctnUIData.setErrorCd(307);
				ErrorMessagesData laEMD =
					ErrorMessagesCache.getErrMsg(307);

				laInvAlloctnUIData.setStatusDesc(
					InventoryConstant.REJECTED
						+ CommonConstant.STR_SPACE_ONE
						+ laEMD.getErrMsgDesc());
				laInvAlloctnUIData.setOrigItmModfyIndi(
					InventoryConstant.ITM_INDI_REMOVED);
				continue;
			}

			// Test if the item is a personalized plate
			if (ValidateInventoryPattern
				.chkIfItmPLPOrOLDPLTOrROP(
					laInvAlloctnUIData.getItmCd()))
				lbPersonalizedItmIndi = true;

			// Compute the begin inventory number to see if there are 
			// any errors
			try
			{
				InventoryAllocationUIData laIAUID =
					(InventoryAllocationUIData) UtilityMethods.copy(
						laInvAlloctnUIData);
				ComputeInventoryData laCID =
					laValidateInvPatrn.validateItmNoInput(laIAUID);
				laIAUID =
					(
						InventoryAllocationUIData) laValidateInvPatrn
							.calcInvNo(
						laCID);
				larrPatrnSeqNo[i][CommonConstant.ELEMENT_0] =
					laIAUID.getPatrnSeqCd();
				larrPatrnSeqNo[i][CommonConstant.ELEMENT_1] =
					laIAUID.getPatrnSeqNo();
			}
			catch (RTSException aeRTSEx)
			{
				if (aeRTSEx.getCode() == 102)
				{
					laInvAlloctnUIData.setStatusCd(
						InventoryConstant.DELETE);
					laInvAlloctnUIData.setErrorCd(399);
					ErrorMessagesData laEMD =
						ErrorMessagesCache.getErrMsg(399);

					laInvAlloctnUIData.setStatusDesc(
						InventoryConstant.REJECTED
							+ CommonConstant.STR_SPACE_ONE
							+ laEMD.getErrMsgDesc());
					laInvAlloctnUIData.setOrigItmModfyIndi(
						InventoryConstant.ITM_INDI_REMOVED);
					continue;
				}
				else
				{
					laInvAlloctnUIData.setStatusCd(
						InventoryConstant.DELETE);
					laInvAlloctnUIData.setErrorCd(165);
					ErrorMessagesData laEMD =
						ErrorMessagesCache.getErrMsg(165);

					laInvAlloctnUIData.setStatusDesc(
						InventoryConstant.REJECTED
							+ CommonConstant.STR_SPACE_ONE
							+ laEMD.getErrMsgDesc());
					laInvAlloctnUIData.setOrigItmModfyIndi(
						InventoryConstant.ITM_INDI_REMOVED);
					continue;
				}
			}

			// Compute the end inventory number to see if there are any 
			// errors
			try
			{
				InventoryAllocationUIData laIAUID =
					(InventoryAllocationUIData) UtilityMethods.copy(
						laInvAlloctnUIData);
				laIAUID.setInvItmNo(laIAUID.getInvItmEndNo());
				ComputeInventoryData laCID =
					laValidateInvPatrn.validateItmNoInput(laIAUID);
				laIAUID =
					(
						InventoryAllocationUIData) laValidateInvPatrn
							.calcInvNo(
						laCID);

				larrPatrnSeqNo[i][CommonConstant.ELEMENT_2] =
					laIAUID.getPatrnSeqNo();
			}
			catch (RTSException aeRTSEx)
			{
				if (aeRTSEx.getCode() == 102)
				{
					laInvAlloctnUIData.setStatusCd(
						InventoryConstant.DELETE);
					laInvAlloctnUIData.setErrorCd(399);
					ErrorMessagesData laEMD =
						ErrorMessagesCache.getErrMsg(399);

					laInvAlloctnUIData.setStatusDesc(
						InventoryConstant.REJECTED
							+ CommonConstant.STR_SPACE_ONE
							+ laEMD.getErrMsgDesc());
					laInvAlloctnUIData.setOrigItmModfyIndi(
						InventoryConstant.ITM_INDI_REMOVED);
					continue;
				}
				else
				{
					laInvAlloctnUIData.setStatusCd(
						InventoryConstant.DELETE);
					laInvAlloctnUIData.setErrorCd(166);
					ErrorMessagesData laEMD =
						ErrorMessagesCache.getErrMsg(166);

					laInvAlloctnUIData.setStatusDesc(
						InventoryConstant.REJECTED
							+ CommonConstant.STR_SPACE_ONE
							+ laEMD.getErrMsgDesc());
					laInvAlloctnUIData.setOrigItmModfyIndi(
						InventoryConstant.ITM_INDI_REMOVED);
					continue;
				}
			}

			// This outside if statement is only here to simulate the 
			// way RTSI works.  The inner statement is a valid statement 
			// and needs to be run when the invoice is pulled down from 
			// the MF and the items are being validated.  In theory, this
			// should also work when adding or modifying invoice items 
			// because the inventory calculation is being done by RTS.  
			// However, there is an error with some itmcd and qty 
			//  combinations that causes the inventory calculation to 
			// not work correctly.  I.e., if you enter a qty of 3 and 
			// give it CJ001C for the begin number, the end number that 
			// is calculated is CJ002C.  The problem is that this is 
			// actually a qty of 4.  So the following check will fail.  
			// Once the inventory calculation problem is fixed, this 
			// outside if statement can be removed.
			if (laMFInvAlloctnData
				.getTransCd()
				.equals(InventoryConstant.RCVE))
			{
				// Compute the quantity by subtracting the begin item 
				// seq code from the end item seq code and adding one.  
				// See if that matches the quantity on the invoice.
				if (laInvAlloctnUIData.getInvQty()
					!= (larrPatrnSeqNo[i][CommonConstant.ELEMENT_2]
						- larrPatrnSeqNo[i][CommonConstant.ELEMENT_1]
						+ 1))
				{
					laInvAlloctnUIData.setStatusCd(
						InventoryConstant.FAILED);
					laInvAlloctnUIData.setStatusDesc(
						InventoryConstant.WRONG_QTY_OR_NO);
					laInvAlloctnUIData.setOrigItmModfyIndi(
						InventoryConstant.ITM_INDI_NO_CHANGE);
					continue;
				}
			}

			// Check to see if the item's range does not fall in or 
			// intersect with a previous item's range
			for (int k = 0; k < i; k++)
			{
				InventoryAllocationUIData laPreviousInvcItm =
					(InventoryAllocationUIData) laMFInvAlloctnData
						.getInvAlloctnData()
						.get(
						k);

				if (!laInvAlloctnUIData
					.getItmCd()
					.equals(laPreviousInvcItm.getItmCd()))
				{
					continue;
				}
				if (laInvAlloctnUIData.getInvItmYr()
					!= laPreviousInvcItm.getInvItmYr())
				{
					continue;
				}
				if (lbPersonalizedItmIndi
					&& !laInvAlloctnUIData.getInvItmNo().trim().equals(
						laPreviousInvcItm.getInvItmNo().trim()))
				{
					continue;
				}
				if (!lbPersonalizedItmIndi)
				{
					if (larrPatrnSeqNo[i][CommonConstant.ELEMENT_0]
						!= larrPatrnSeqNo[k][CommonConstant.ELEMENT_0])
					{
						continue;
					}
					if (larrPatrnSeqNo[i][CommonConstant.ELEMENT_1]
						< larrPatrnSeqNo[k][CommonConstant.ELEMENT_1]
						&& larrPatrnSeqNo[i][CommonConstant.ELEMENT_2]
							< larrPatrnSeqNo[k][CommonConstant
								.ELEMENT_1])
					{
						continue;
					}
					if (larrPatrnSeqNo[i][CommonConstant.ELEMENT_1]
						> larrPatrnSeqNo[k][CommonConstant.ELEMENT_2]
						&& larrPatrnSeqNo[i][CommonConstant.ELEMENT_2]
							> larrPatrnSeqNo[k][CommonConstant
								.ELEMENT_2])
					{
						continue;
					}
				}
				lbInvalidItm = true;
				break;
			}
			if (lbInvalidItm)
			{
				laInvAlloctnUIData.setStatusCd(
					InventoryConstant.DELETE);
				laInvAlloctnUIData.setErrorCd(615);
				ErrorMessagesData laEMD =
					ErrorMessagesCache.getErrMsg(615);

				laInvAlloctnUIData.setStatusDesc(
					InventoryConstant.REJECTED
						+ CommonConstant.STR_SPACE_ONE
						+ laEMD.getErrMsgDesc());
				laInvAlloctnUIData.setOrigItmModfyIndi(
					InventoryConstant.ITM_INDI_REMOVED);
				continue;
			}

			// Check to see if the item is not in the allocation table 
			// of the destination office.
			try
			{
				laDBA.beginTransaction();
				InventoryAllocation laInvAlloctnSQL =
					new InventoryAllocation(laDBA);

				for (int k = 0;
					k < laMFInvAlloctnData.getSelctdSubstaIndx().size();
					k++)
				{
					Vector lvReturnQryData = new Vector();
					int liSubstaIndx =
						((Integer) laMFInvAlloctnData
							.getSelctdSubstaIndx()
							.get(k))
							.intValue();
					SubstationData laSubstaData =
						(SubstationData) lvSubstaData.get(liSubstaIndx);
					InventoryAllocationUIData laIAUID =
						(
							InventoryAllocationUIData) UtilityMethods
								.copy(
							laInvAlloctnUIData);
					laIAUID.setOfcIssuanceNo(
						laMFInvAlloctnData
							.getMFInvAckData()
							.getOfcIssuanceNo());
					laIAUID.setSubstaId(laSubstaData.getSubstaId());
					laIAUID.setPatrnSeqNo(
						larrPatrnSeqNo[i][CommonConstant.ELEMENT_1]);
					laIAUID.setEndPatrnSeqNo(
						larrPatrnSeqNo[i][CommonConstant.ELEMENT_2]);
					laIAUID.setPatrnSeqCd(
						larrPatrnSeqNo[i][CommonConstant.ELEMENT_0]);

					if (!lbPersonalizedItmIndi)
					{
						lvReturnQryData =
							laInvAlloctnSQL
								.qryInventoryAllocationIntersection(
								laIAUID);
					}
					else
					{
						laIAUID.setEndPatrnSeqNo(Integer.MIN_VALUE);
						laIAUID.setPatrnSeqNo(Integer.MIN_VALUE);
						lvReturnQryData =
							laInvAlloctnSQL
								.qryInventoryAllocationForSpecificItem(
								laIAUID);
					}

					if (lvReturnQryData.size()
						> CommonConstant.ELEMENT_0)
					{
						laInvAlloctnUIData.setStatusCd(
							InventoryConstant.DELETE);
						laInvAlloctnUIData.setErrorCd(
							ErrorsConstant.ERR_NUM_ITM_EXISTS);
						ErrorMessagesData laEMD =
							ErrorMessagesCache.getErrMsg(
								ErrorsConstant.ERR_NUM_ITM_EXISTS);
						lsSubstaInvExistsIn =
							laSubstaData.getSubstaName();

						laInvAlloctnUIData.setStatusDesc(
							InventoryConstant.REJECTED
								+ CommonConstant.STR_SPACE_ONE
								+ laEMD.getErrMsgDesc()
								+ TXT_PAREN_OPEN_2
								+ ((SubstationData) lvSubstaData.get(k))
									.getSubstaName()
								+ TXT_PAREN_CLOSE);
						laInvAlloctnUIData.setOrigItmModfyIndi(
							InventoryConstant.ITM_INDI_REMOVED);
						break;
					}
				}
				laDBA.endTransaction(DatabaseAccess.NONE);
			}
			catch (RTSException aeRTSEx)
			{
				laDBA.endTransaction(DatabaseAccess.NONE);
				throw aeRTSEx;
			}

			if (laMFInvAlloctnData
				.getTransCd()
				.equals(InventoryConstant.RCVE)
				&& laInvAlloctnUIData.getStatusCd() == null)
			{
				laInvAlloctnUIData.setStatusCd(InventoryConstant.CHECK);
				laInvAlloctnUIData.setStatusDesc(
					InventoryConstant.VERIFIED_OK);
				laInvAlloctnUIData.setOrigItmModfyIndi(
					InventoryConstant.ITM_INDI_NO_CHANGE);
				laInvAlloctnUIData.setPatrnSeqCd(
					larrPatrnSeqNo[i][CommonConstant.ELEMENT_0]);
				laInvAlloctnUIData.setPatrnSeqNo(
					larrPatrnSeqNo[i][CommonConstant.ELEMENT_1]);
				laInvAlloctnUIData.setEndPatrnSeqNo(
					larrPatrnSeqNo[i][CommonConstant.ELEMENT_2]);
			}
		}

		Vector lvVct = new Vector(CommonConstant.ELEMENT_2);
		lvVct.addElement(laMFInvAlloctnData);
		lvVct.addElement(lsSubstaInvExistsIn);

		Log.write(
			Log.METHOD,
			this,
			MSG_VALIDATEINVCITMS_END + csClientHost);
		return lvVct;
	}

	/**
	 * This method drives the validation of a Personalized Plate request.
	 * 
	 * <p>If an Item Code is provided, the plate number is reserved
	 * for the user.
	 * 
	 * <p>If an Item Code is not provided, this method just does a 
	 * check if no reservation.
	 * 
	 * <p>Object in and out is InventoryAllocationData.
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object validatePerPlate(Object aaData) throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"validatePerPlate Begin " + csClientHost);

		DatabaseAccess laDBA = null;
		Object laReturnObject = null;

		// defect 9679
		InventoryAllocationData laInvAlloctnData = null;
		boolean lbPassedDBA = false;

		if (aaData instanceof InventoryAllocationData)
		{
			laInvAlloctnData = (InventoryAllocationData) aaData;
		}
		else if (aaData instanceof Vector)
		{
			laDBA = (DatabaseAccess) ((Vector) aaData).elementAt(0);
			laInvAlloctnData =
				(InventoryAllocationData) ((Vector) aaData).elementAt(
					1);
			lbPassedDBA = true;
		}
		// end defect 9679

		try
		{
			// defect 9404 
			// Add logging; Move call to mf outside UOW   
			// laDBA = new DatabaseAccess();
			// laDBA.beginTransaction();
			// end defect 9404 

			// boolean indicating that we should check for duplicates
			boolean lbNoDups = true;

			// allow ivtrs to bypass the vi itm code lookup 
			//  if they do not have an itmcd
			if (laInvAlloctnData.getItmCd() != null)
			{
				// make sure the ViItmd is populated
				if (laInvAlloctnData.getViItmCd() == null
					|| laInvAlloctnData.getViItmCd().equals(
						CommonConstant.STR_SPACE_EMPTY))
				{
					laInvAlloctnData.setViItmCd(
						lookupVirtualForItem(laInvAlloctnData));
					laInvAlloctnData.setVirtual(true);

					lbNoDups =
						(!chkDupsAllowed(laInvAlloctnData
							.getViItmCd()));
				}
			}

			// Bypass all checking if duplicates are allowed and it is
			// user plt no
			if (lbNoDups)
			{

				// defect 9486
				// laDBA moved prior to MF call in case must insert 
				// into rejection log 
				// defect 9679  
				if (!lbPassedDBA)
				{
					laDBA = new DatabaseAccess();
				}
				// end defect 9679

				// defect 9404 
				// MF check moved to prior to unit of work 
				// check to see if plate exists on mf
				validatePerPlateChkMainFrame(laInvAlloctnData);

				// format the HoopsRegPltNo for lookup and storage
				laInvAlloctnData.setHoopsRegPltNo(
					CommonValidations.convert_i_and_o_to_1_and_0(
						laInvAlloctnData.getInvItmNo()));

				// laDBA = new DatabaseAccess();
				// end defect 9486

				// defect 9679
				if (!lbPassedDBA)
				{
					laDBA.beginTransaction();
				}
				// end defect 9679
				//logUOW("validatePerPlate", ": BEGIN");
				// end defect 9404

				// check to see if the plate already exists
				validatePerPlateQueryExisting(laDBA, laInvAlloctnData);

				// allow ivtrs to bypass the insert 
				// if they do not have an itmcd
				if (laInvAlloctnData.getItmCd() != null)
				{
					// set the CustSuppliedIndi before inserting
					laInvAlloctnData.setCustSupplied(true);
					// try inserting the record before doing further 
					// validatation
					validatePerPlateInsertViRow(
						laDBA,
						laInvAlloctnData);
				}

				PlateTypeData laPltType = new PlateTypeData();

				// Do not check ROP's or OLDPLT's agains invalid letters 
				// or patterns.
				if (laInvAlloctnData.getViItmCd() != null)
				{
					laPltType =
						PlateTypeCache.getPlateType(
							laInvAlloctnData.getViItmCd());
				}

				// if duplicates are allowed and the plate number is
				// owner defined, we do not check for patterns or 
				// invalid combos
				// Currently ROP and OLDPLT match this definition.
				if (laPltType != null && lbNoDups)
				{
					// check for pattern matches
					validatePerPlateChkPatterns(laInvAlloctnData);

					// check to see if any match invalid letter combos.
					validatePerPlateChkInvalidLtrCombo(laInvAlloctnData);
				}

				// check to see if plate exists on mf
				// validatePerPlateChkMainFrame(laInvAlloctnData);
			}

			// defect 10293 
			// Check for laDBA != null
			// if we got this far, the request passed all automated checks.
			// defect 9679
			if (!lbPassedDBA && laDBA != null)
			{
				// end defect 10293 
				laDBA.endTransaction(DatabaseAccess.COMMIT);
				//logUOW("validatePerPlate", ": COMMIT");
				laDBA = null;
			}
			// end defect 9679

			Log.write(
				Log.METHOD,
				this,
				"validatePerPlate End " + csClientHost);

			laReturnObject = laInvAlloctnData;
		}
		catch (RTSException aeRTSEx)
		{
			// rollback any changes that got made 
			if (laDBA != null)
			{
				laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				//logUOW("validatePerPlate", ":ROLLBACK");

				// insert the rejection log record
				if (laInvAlloctnData.getErrorCode() > 0)
				{
					laDBA.beginTransaction();
					insrtSpRejLog(laInvAlloctnData, laDBA);
					laDBA.endTransaction(DatabaseAccess.COMMIT);
				}

				if (!lbPassedDBA)
				{
					// null out the dba if it was not passed.
					laDBA = null;
				}
			}

			Log.write(
				Log.METHOD,
				this,
				"validatePerPlate Exception " + csClientHost);

			throw aeRTSEx;
		}
		// end defect 9404 

		return laReturnObject;
	}

	/**
	 * This method does a check to see if a personalized plate has any 
	 * invalid letter combinations.  Only the open ones are checked 
	 * (blank item code).
	 * 
	 * @param aaIAD InventoryAllocationData
	 * @throws RTSException
	 */
	private void validatePerPlateChkInvalidLtrCombo(InventoryAllocationData aaIAD)
		throws RTSException
	{
		// get the list of Invalid Letters
		Vector lvInvldLtr = new Vector();
		lvInvldLtr.addAll(InvalidLetterCache.getInvldLtrs(""));

		// check for any matches
		for (Iterator laIter = lvInvldLtr.iterator();
			laIter.hasNext();
			)
		{
			InvalidLetterData laILD = (InvalidLetterData) laIter.next();
			if (aaIAD.getInvItmNo().indexOf(laILD.getInvldLtrCombo())
				> -1)
			{
				aaIAD.setErrorCode(
					ErrorsConstant.ERR_NUM_VI_PER_HAS_INVALID_LTR);
				throw new RTSException(
					ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
			}
		}
	}

	/**
	 * Check to see if the plate exists on MainFrame.
	 * If it does, throw an appropriate exception.
	 * 
	 * @param asPltNo String
	 * @throws RTSException
	 */
	private void validatePerPlateChkMainFrame(InventoryAllocationData laInvAlloctnData)
		throws RTSException
	{

		try
		{
			CommonServerBusiness laCSB = new CommonServerBusiness();
			GeneralSearchData laGSD = new GeneralSearchData();

			laGSD.setKey1(CommonConstant.REG_PLATE_NO);
			// TODO defect 9320  Should be using HoopsRegPltNo
			laGSD.setKey2(laInvAlloctnData.getInvItmNo());
			laGSD.setKey3("");
			// defect 10415
			//laGSD.setIntKey2(2);
			laGSD.setIntKey2(CommonConstant.SEARCH_SPECIAL_PLATES);
			// end defect 10415

			VehicleInquiryData laVID =
				(VehicleInquiryData) laCSB.processData(
					GeneralConstant.INVENTORY,
					CommonConstant.GET_VEH,
					laGSD);
			// defect 10415
			// if laVID is null or if MF Down, just say VI is unavailable.
			if (laVID == null || laVID.getMfDown() != 0)
			{
				laInvAlloctnData.setErrorCode(
					ErrorsConstant.ERR_NUM_VI_UNAVAILABLE);
				throw new RTSException(
					ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
			}
			// check the result data
			if ((laVID.getMfVehicleData() == null
				|| laVID.getMfVehicleData().getSpclPltRegisData() == null)
				&& (laVID.getPartialSpclPltsDataList() == null
					|| laVID.getPartialSpclPltsDataList().size() < 1))
			{
				if (laInvAlloctnData.isFromReserve())
				{
					laInvAlloctnData.setErrorCode(
						ErrorsConstant.ERR_NUM_NO_RECORD_FOUND);
					throw new RTSException(
						ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
				}
				// end defect 10415
			}
			else if (
				laVID.getMfVehicleData() != null
					&& laVID.getMfVehicleData().getSpclPltRegisData()
						!= null
					&& laVID
						.getMfVehicleData()
						.getSpclPltRegisData()
						.getDelIndi()
						== 0)
			{
				// the above if was not changed.
				String lsCode =
					laVID
						.getMfVehicleData()
						.getSpclPltRegisData()
						.getMFGStatusCd();

				// defect 10415		
				if (lsCode
					.equalsIgnoreCase(
						SpecialPlatesConstant
							.UNACCEPTABLE_MFGSTATUSCD))
				{
					// end defect 10415
					// mfg status code is set to not manufacture
					laInvAlloctnData.setErrorCode(
						ErrorsConstant
							.ERR_NUM_VI_PER_EXISTS_OBJECTIONABLE);
					throw new RTSException(
						ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
					// defect 9679
					// if the item is supposed to be from reserve
					// accept it if it is from reserve.
					// defect 10415
				}
				else if (
					lsCode.equalsIgnoreCase(
						SpecialPlatesConstant.RESERVE_MFGSTATUSCD)
						&& !laInvAlloctnData.isFromReserve())
				{
					// end defect 10415
					// end defect 9679
					// the plate is currently reserved
					laInvAlloctnData.setErrorCode(
						ErrorsConstant.ERR_NUM_VI_PER_EXISTS_RESERVED);
					throw new RTSException(
						ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
					// defect 9679
					// defect 10415
				}
				else if (
					laInvAlloctnData.isFromReserve()
						&& (!lsCode
							.equalsIgnoreCase(
								SpecialPlatesConstant
									.RESERVE_MFGSTATUSCD)))
				{
					laInvAlloctnData.setErrorCode(
						ErrorsConstant.ERR_NUM_NO_RECORD_FOUND);
					throw new RTSException(
						ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
				}
				else if (
					lsCode.equalsIgnoreCase(
						SpecialPlatesConstant.RESERVE_MFGSTATUSCD)
						&& laInvAlloctnData.isFromReserve()
						&& laInvAlloctnData.getOfcIssuanceNo()
							!= laVID
								.getMfVehicleData()
								.getSpclPltRegisData()
								.getResComptCntyNo())
				{
					// defect 10415
					// offices do not match up
					laInvAlloctnData.setErrorCode(
						ErrorsConstant
							.ERR_NUM_VI_PER_EXISTS_RESERVED_DIFFERENT_COUNTY);
					throw new RTSException(
						ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
					// defect 10415
				}
				else if (
					lsCode.equalsIgnoreCase(
						SpecialPlatesConstant.RESERVE_MFGSTATUSCD)
						&& laInvAlloctnData.isFromReserve()
						&& laInvAlloctnData.getOfcIssuanceNo()
							== laVID
								.getMfVehicleData()
								.getSpclPltRegisData()
								.getResComptCntyNo())
				{
					// end defect 10415
					// no action required.
					// we want the reserved plate that is assigned to 
					// this office.
				}
				else
				{
					// end defect 9679
					// the plate exists
					laInvAlloctnData.setErrorCode(
						ErrorsConstant.ERR_NUM_VI_PER_EXISTS_MF);
					throw new RTSException(
						ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
				}
			}
			else if (laVID.getPartialSpclPltsDataList() != null)
			{
				// end defect 9086
				Vector lvPartialList =
					laVID.getPartialSpclPltsDataList();
				for (Iterator laIter = lvPartialList.iterator();
					laIter.hasNext();
					)
				{
					MFPartialSpclPltData laElement =
						(MFPartialSpclPltData) laIter.next();
					if (laElement.getdelIndi() == 1)
					{
						continue;
						// defect 10415
					}
					else if (
						laElement.getMFGStatusCd().equalsIgnoreCase(
							SpecialPlatesConstant
								.UNACCEPTABLE_MFGSTATUSCD))
					{
						// end defect 10415
						// mfg status code is set to not manufacture
						laInvAlloctnData.setErrorCode(
							ErrorsConstant
								.ERR_NUM_VI_PER_EXISTS_OBJECTIONABLE);
						throw new RTSException(
							ErrorsConstant
								.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
						// defect 9679
						// if the item is supposed to be from reserve
						// accept it if it is from reserve.
						// defect 10415
					}
					else if (
						laElement.getMFGStatusCd().equalsIgnoreCase(
							SpecialPlatesConstant.RESERVE_MFGSTATUSCD)
							&& !laInvAlloctnData.isFromReserve())
					{
						// end defect 10415
						// end defect 9679
						// the plate is reserved
						laInvAlloctnData.setErrorCode(
							ErrorsConstant
								.ERR_NUM_VI_PER_EXISTS_RESERVED);
						throw new RTSException(
							ErrorsConstant
								.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
						// defect 9679
					}
					else
					{
						// end defect 9679
						// the plate exists
						laInvAlloctnData.setErrorCode(
							ErrorsConstant.ERR_NUM_VI_PER_EXISTS_MF);
						throw new RTSException(
							ErrorsConstant
								.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
					}
				}
			}
			else
			{
				// just ignore this.  We should have not gotten to this
				// point, but it means no matches.
			}
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		catch (NullPointerException aeNPR)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"Got a NullPtr for MFAccess in validatePerPlateChkMainFrame");
			throw aeNPR;
		}
	}

	/**
	 * Checks the requested item against all known patterns.
	 * 
	 * @param laInvAlloctnData
	 * @throws RTSException
	 */
	private void validatePerPlateChkPatterns(InventoryAllocationData laInvAlloctnData)
		throws RTSException
	{
		// defect 9516
		ValidateInventoryPattern laVIP = new ValidateInventoryPattern();
		Vector lvMatchingPatterns =
			laVIP.getMatchingPatterns(laInvAlloctnData);

		if (lvMatchingPatterns.size() > 0)
		{
			laInvAlloctnData.setErrorCode(
				ErrorsConstant.ERR_NUM_VI_PER_MATCHES_PTRN);

			// defect 9578
			// If Not Internet, throw same error 
			int liErrMsgNo =
				laInvAlloctnData.isItrntReq()
					? ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE
					: ErrorsConstant.ERR_NUM_VI_PER_MATCHES_PTRN;

			throw new RTSException(
			//ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
			liErrMsgNo);
			// end defect 9578  
		}
		// end defect 9516
	}

	/**
	 * Attempt to insert the requested Per Plate into Virtual Inventory.
	 * This is ensures uniqueness for applications that have not been 
	 * processed yet.
	 * 
	 * <p>If there is a problem, an exception is thrown.
	 * 
	 * @param aaDBA DatabaseAccess
	 * @param aaInvAlloctnData InventoryAllocationData
	 * @throws RTSException
	 */
	private void validatePerPlateInsertViRow(
		DatabaseAccess aaDBA,
		InventoryAllocationData aaInvAlloctnData)
		throws RTSException
	{
		// defect 9679
		if (!aaInvAlloctnData.isTempChange())
		{
			// end defect 9679
			InventoryVirtual laVISql = new InventoryVirtual(aaDBA);
			try
			{
				aaInvAlloctnData.setInvStatusCd(
					InventoryConstant.HOLD_INV_SYSTEM);
				laVISql.insInventoryVirtual(
					aaInvAlloctnData,
					csClientHost);
			}
			catch (RTSException aeRTSExInsrt)
			{
				// check for DuplicateKeyException 
				if (aeRTSExInsrt
					.getDetailMsg()
					.indexOf("DuplicateKeyException")
					!= -1)
				{
					// we need to add handling to check for duplicates here
					Vector lvDupRow =
						laVISql.qryInventoryForSpecificItem(
							aaInvAlloctnData);
					if (lvDupRow.size() > 0)
					{
						InventoryAllocationData laFoundRec =
							(
								InventoryAllocationData) lvDupRow
									.elementAt(
								0);
						RTSDate laCompDate = new RTSDate();
						laCompDate.add(java.util.Calendar.MINUTE, -15);

						// if the returned row has an expired timestamp
						// and the office issuance numbers match.
						// we can update it.  
						// Otherwise, our request is a dup.
						if (laFoundRec.getInvStatusCd() == 0
							|| ((laFoundRec
								.getInvHoldTimeStmp()
								.compareTo(laCompDate)
								< 1)
								&& aaInvAlloctnData.getOfcIssuanceNo()
									== laFoundRec.getOfcIssuanceNo()))
						{
							laVISql.updInventoryVirtual(
								aaInvAlloctnData,
								csClientHost);
						}
						else
						{
							aaInvAlloctnData.setErrorCode(
								ErrorsConstant
									.ERR_NUM_VI_PER_EXISTS_VI);
							throw new RTSException(
								ErrorsConstant
									.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
						}
					}
					else
					{
						aaInvAlloctnData.setErrorCode(
							ErrorsConstant.ERR_NUM_VI_PER_EXISTS_VI);
						throw new RTSException(
							ErrorsConstant
								.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
					}
				}
				else
				{
					aaInvAlloctnData.setErrorCode(
						ErrorsConstant.ERR_NUM_VI_PER_EXISTS_VI);
					throw new RTSException(
						ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
				}
			}
			// defect 9679
		}
		// end defect 9679
	}

	/**
	 * This method validates a plp request and then releases the hold.
	 * This is used by IVTRS to see if a plp is available before 
	 * ordering.
	 * 
	 * <p>Object in is InventoryAllocationData.
	 * <p>Object out is Boolean.
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object validatePerPlateNoHold(Object aaData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"validatePerPlateNoHold Begin " + csClientHost);

		Boolean laReturn = new Boolean(false);

		try
		{
			// defect 9780
			if (aaData instanceof Vector)
			{
				(
					(InventoryAllocationData)
						(((Vector) aaData)).elementAt(
						1)).setTempChange(
					true);
			}
			else if (aaData instanceof InventoryAllocationData)
			{
				((InventoryAllocationData) aaData).setTempChange(true);
			}
			// end defect 9780
			aaData = validatePerPlate(aaData);
			// There was no insert so no reason to release!
			//aaData = updViStatusCodeRelease(aaData);
			laReturn = new Boolean(true);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.METHOD,
				this,
				"validatePerPlateNoHold Exception " + csClientHost);
			throw aeRTSEx;
		}

		Log.write(
			Log.METHOD,
			this,
			"validatePerPlateNoHold End " + csClientHost);

		return laReturn;
	}

	/**
	 * Check to see if the plate exists in InvVirtual.
	 * 
	 * @param aaDBA
	 * @param aaInvAlloctnData
	 * @throws RTSException
	 */
	private void validatePerPlateQueryExisting(
		DatabaseAccess aaDBA,
		InventoryAllocationData aaInvAlloctnData)
		throws RTSException
	{
		InventoryVirtual laVISql = new InventoryVirtual(aaDBA);
		try
		{
			InventoryAllocationData laIAD =
				new InventoryAllocationData();
			laIAD.setInvItmNo(aaInvAlloctnData.getInvItmNo());
			laIAD.setHoopsRegPltNo(aaInvAlloctnData.getHoopsRegPltNo());
			laIAD.setUserPltNo(aaInvAlloctnData.isUserPltNo());

			Vector lvResult =
				laVISql.qryInventoryForSpecificItem(laIAD);
			if (lvResult.size() > 0)
			{
				RTSDate laCompareTime = new RTSDate();
				laCompareTime.setMinute(laCompareTime.getMinute() - 15);

				for (Iterator laIter = lvResult.iterator();
					laIter.hasNext();
					)
				{
					InventoryAllocationData laTestRec =
						(InventoryAllocationData) laIter.next();

					// check to see if this row can be deleted.
					if (laTestRec.getInvStatusCd()
						== InventoryConstant.HOLD_INV_NOT)
					{
						// This row is not on hold.
						// It can be deleted.
						delInvVirtual(aaDBA, laTestRec);
					}
					else if (
						laTestRec.isItrntReq()
							&& laTestRec.getInvHoldTimeStmp().compareTo(
								laCompareTime)
								< 0
							&& laTestRec.getInvStatusCd()
								== InventoryConstant.HOLD_INV_SYSTEM
							&& laTestRec.getTransTime() == 0
							&& !aaInvAlloctnData.isTempChange())
					{
						// TODO This does not allow the same user to 
						// come back in and get his plate after 
						// disconnect.
						// reqipaddr can be the same for many users.

						// this is an IVTRS request that has expired.
						// It can be deleted
						delInvVirtual(aaDBA, laTestRec);
					}
					else if (
						laTestRec.isItrntReq()
							&& laTestRec.getInvHoldTimeStmp().compareTo(
								laCompareTime)
								< 0
							&& laTestRec.getInvStatusCd()
								== InventoryConstant.HOLD_INV_SYSTEM
							&& laTestRec.getTransTime() == 0
							&& aaInvAlloctnData.isTempChange())
					{
						// do nothing.
						// this is ok.
					}
					else if (
						!laTestRec.isItrntReq()
							&& laTestRec.getOfcIssuanceNo()
								== aaInvAlloctnData.getOfcIssuanceNo()
							&& laTestRec.getTransEmpId().equalsIgnoreCase(
								aaInvAlloctnData.getTransEmpId())
							&& laTestRec.getTransWsId()
								== aaInvAlloctnData.getTransWsId()
							&& laTestRec.getTransAmDate()
								== aaInvAlloctnData.getTransAmDate()
							&& laTestRec.getInvStatusCd()
								== InventoryConstant.HOLD_INV_SYSTEM
							&& laTestRec.getTransTime() == 0)
					{
						// the POS person on the same workstation doing 
						// the request is apparently changing the request
						// after a disconnect.
						// It can be deleted.
						delInvVirtual(aaDBA, laTestRec);
					}
					else
					{
						// the row exists and can not be deleted.
						aaInvAlloctnData.setErrorCode(
							ErrorsConstant.ERR_NUM_VI_PER_EXISTS_VI);
						throw new RTSException(
							ErrorsConstant
								.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
					}
				}
			}
		}
		catch (RTSException aeRTSExQry)
		{
			throw aeRTSExQry;
		}
	}
}
