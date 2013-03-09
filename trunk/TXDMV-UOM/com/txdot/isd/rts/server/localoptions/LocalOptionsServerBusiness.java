package com.txdot.isd.rts.server.localoptions;

import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.OfficeTimeZoneCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.localoptions.*;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.*;
import com.txdot.isd.rts.server.reports.ReportsServerBusiness;

/*
 * LocalOptionsServerBusiness.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * BTulsiani	04/30/2002	Added e.setBeep to errors thrown on 
 * Ray Rowehl	02/10/2003	server.  Change processLogin to not 
 *							decrypt passwords.  Change 
 *							processNewPass to not trim new password 
 *							defect 4735
 * Ray Rowehl	05/29/2003	Allow procesing of windows logins
 *							modify processLogin(), processNewPass(),
 *							addEmpSec(), updtEmpSec(), processData()
 *							defect 6445 Ver  5.1.6
 * Ray Rowehl	01/15/2004	Follow up on problems where user was 
 *							logically deleted but we did not detect 
 *							correctly and handle.  Now we delete the 
 *							row, before insert.  This is because  
 *							UserName and EmpId both act as keys for 
 *							our data.  Move databaseaccess out so 
 *							rollbacks can be done.  Also updated 
 *							some constant references to be static.
 *							update addEmpSec(), updtEmpSec()
 *							defect 6445 Ver  5.1.6
 * Ray Rowehl	02/03/2004	Not properly deleting logically deleted 
 *							rows before attempted insert.  This is a 
 *							case where we are using an EmpId with a
 *							different UserName.
 *							modify addEmpSec(), updtEmpSec() 
 *							defect 6865 Ver 5.1.6
 * Ray Rowehl	02/05/2004	Add 5.1.6 specific error messages for
 *							Employee add and revise errors
 *							modify addEmpSec(), updtEmpSec()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/09/2004	Correct object reference in updtEmpSec.
 *							Was pointing to sec instead of secData.
 *							modify updtEmpSec()
 *							defect 6865 Ver 5.1.6
 * Ray Rowehl	02/09/2004	If EmpId is empty, fill it with "missing"
 *							for logfunctrans insert.
 *							modify processLogin()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/17/2004	Reformat to match updated Java Standards.
 * 							Access dba methods as static where needed.
 * 							format code
 * 							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/19/2004	trim usernames for comparision
 *							modify updtEmpSec()
 *							defect 6445 Ver 5.1.6
 * K Harrell	03/04/2004	Correct parameter for call to sec.delSecurity
 *							to false to request delete by empid. 
 *							modify updtEmpSec()
 *							defect 6445 Ver 5.1.6
 * K Harrell	04/02/2004	remove conditional operating system coding
 *							modify processLogin(),processData()
 *							delete processNewPass()
 * Ray Rowehl	07/21/2004	Add processing for RSPS Updates
 *							add updateRSPS()
 *							modify processData()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/16/2004	Improvements to RSPS processing.
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/18/2004	Improve sys update list check to handle
 *							multiple laptops.
 *							Adjust timestamp for timezone.
 *							modify getSysUpdtList(), updateRSPS()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/20/2004	cleanup RSPSWsStatus and RSPSWsSysupdtHstry
 *							on delete of dealers and subcons
 *							modify delDlr(), delSubcon()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	09/15/2004	add processing for ScanEngine
 *							modify updateRSPS()
 *							defect 7135 Ver 5.2.1.1
 * Min Wang		11/01/2004	Allow for delete of SysUpdateHistory for
 *							RSPS
 *							modify updateRSPS()
 *							defect 7671 Ver 5.2.1.1
 * Ray Rowehl	11/15/2004	Avoid NullPointers in processLogin()
 *							Also clean up imports.
 *							modify processLogin()
 *							defect 7234 Ver 5.2.2
 * Min Wang		11/16/2004	Improve employee delete for non-publishing.
 *							modify delEmpSec()
 *							defect 7462 Ver 5.2.2
 * K Harrell	01/12/2005	JavaDoc/Formatting/Variable Name Cleanup
 *							Set SuccessfulIndi on successful logon
 *							modify processLogin()
 *							defect 7756 Ver 5.2.2
 * K Harrell	01/12/2005	Correct spelling of
 *							LocalOptionsConstant.RETREIVE_CRDT_FEES
 *							modify processData()
 *							defect 7902 Ver 5.2.2 
 * K Harrell	01/17/2005 	Deprecate unused methods 
 *							deprecate updtDlr(),updtLien(),updtSubcon()
 *							defect 7901 Ver 5.2.2 
 * Jeff S.		04/15/2005	IP address was not being updated when it 
 * 							changed b/c it was not added to the data obj
 * 							that was sent to the DB access class.
 * 							modify updateRSPS()
 * 							defect 8155 Ver. 5.2.2 Fix 4
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7891 Ver 5.2.3
 * K Harrell	06/19/2005	Remove SubStaId and associated get/set 
 * 							methods for RSPSWsStatusData objects
 * 							defect 7899 Ver 5.2.3 
 * Jeff S.		08/01/2005	DB Version was not being updated when it's 
 * 							value changed.
 * 							modify updateRSPS()
 * 							defect 8314 Ver 5.2.2 Fix 6
 * Jeff S.		09/27/2005	DAT level was not being updated when it's 
 * 							value changed.
 * 							modify updateRSPS()
 * 							defect 8382 Ver 5.2.3
 * Jeff S.		09/27/2005	Now we are collecting logdate.  The date 
 * 							that the laptop was updated.
 * 							modify updateRSPS()
 * 							defect 8381 Ver 5.2.3
 * K Harrell	11/02/2005	Refer to ReportConstant for Ids and Titles 
 * 							and number of reports generations to keep.
 * 							Removed unused variables. Used constant 
 * 							"COMMIT" vs. "NONE" for DatabaseAccess.
 * 							deleted updtDlr(),updtLien(),updtSubcon()
 * 							modify genDealerReport(),
 * 							genEmployeeSecurityReport(),
 * 							genEventSecurityReport(),
 * 							genLienHolderReport(),genPublishingReport(),
 * 							genPublishingReport,genSecurityChangeReport(),
 * 							genSubconReport(),updateRSPS()
 * 							defect 8379 Ver 5.2.3 
 * Ray Rowehl	07/12/2006	Add logic to retry processLogin if the first
 * 							attempt got a StaleConnectionException.
 * 							add csClientHost
 * 							add LocalOptionsServerBusiness(String)
 * 							modify processLogin()
 * 							defect 8849 Ver 5.2.3
 * Ray Rowehl	08/04/2006	Write the StaleConnection to the log.
 * 							modify processLogin()
 * 							defect 8869 Ver 5.2.4
 * Ray Rowehl	09/27/2006	Write the MfVersionNo to LogonFuncTrans.
 * 							Do some minor code cleanup.
 * 							modify processLogin()
 * 							defect 8959 Ver FallAdminTables
 * K Harrell	09/05/2008	Update for common Admin Log and Admin Cache 
 * 							 processing 
 * 							add MAX_ENTITY_VALUE_LENGTH, RSPS_ENTITY
 * 							add updtMisc()
 * 							delete serverPlus(), supvOverride()
 * 							modify addCreditUpdate(), insAdminLog() 
 * 							 addDlr(),addEmpSec(),addLien(),
 * 							 addPaymentAccount(), addSubcon(), 
 * 							 delCreditUpdate(), delDlr(), delEmpSec(),
 * 							 delLien(), delPaymentAccount(), 
 * 							 delSubcon(), processData(), 
 * 							 reviseCreditUpdate(), reviseDlr(),
 * 							 revisePaymentAccount(), revisePub(), 
 * 							 revLien(), revSubcon(), updtEmpSec(),
 * 							 updtRSPS()
 * 							defect 8595,8721 Ver Defect_POS_B
 * K Harrell	09/11/2008	Added Admin Logging for Security Reports
 * 							modify genEmployeeSecurityReport(),
 * 							  genEventSecurityReport(), 
 * 							  genSecurityChangeReport() 
 * 							defect 8595 Ver Defect_POS_B 
 * J Zwiener	03/02/2009	Add CERTLIEN constant
 * 							modify processData()
 * 							Add GenCertdLienhldr()
 * 							modify processData()
 * 							defect 9968 Ver Defect_POS_E
 * K Harrell	06/26/2009	Implement new DealerData, LienholderData
 * 							modify addDlr(),delDlr(),reviseDlr(), 
 * 							 addLien(),delLien(),reviseLien(),
 * 							 getDlrDataOnId(), getLienhldrDataOnId(),
 * 							 genDealerReport(), getLienHolderReport()
 * 							defect 10112 Ver Defect_POS_F
 * Ray Rowehl	07/13/2009	Modify constants used for login failure.
 * 							modify processLogin()
 * 							defect 10103 Ver Defect_POS_F
 * K Harrell	08/22/2009	Implement new coding strategies for reports.
 * 							Add UOW comments.  Add AdminLog entries for
 * 							 reports: Dealer, Certfd Lienhldr, Lienhldr,
 * 							  Subcon, Publishing 
 * 							add initReportProperties() 
 * 							modify genCertfdLienHldrReport(), 
 * 							genDealerReport(), genEmployeeSecurityReport(), 
 * 							genEventSecurityReport(), genLienHolderReport(), 
 * 							genPublishingReport, genSecurityChangeReport(),
 * 							genSubconReport(), getDlrDataOnId(), 
 * 							 getEmpData(), getLienhldrDataOnId(), 
 * 							 getSubconDataOnId(), getSysUpdtList() 
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	10/13/2009	Implement Local Options Report Sort Options
 * 							modify genCertfdLienHldrReport(), 
 * 							 genDealerReport(), genLienHolderReport(), 
 * 							 genSubconReport()
 * 							defect 10250 Ver Defect_POS_G 
 * K Harrell	10/20/2009	Implement Report Constants. Add add'l defect
 * 							markers. 
 * 							add getAdminLogSortBy()
 * 							modify genCertfdLienHldrReport(), 
 * 							 genDealerReport(), genLienHolderReport(), 
 * 							 genSubconReport() 		
 * 							defect 10250 Ver Defect_POS_G
 * K Harrell	02/18/2010	Implement new SubcontractorData
 * 							modify delSubcon(), updateRSPS()
 * 							defect 10161 Ver POS_640   
 * K Harrell	03/08/2010	Synchronize AdminLog entry for Subcon Report 
 * 							 with Add/Delete/Revise.  Implement constants
 * 							 for AdminLog entries for Dealer, Lienholder, 
 * 							 Subcon reports. 
 * 							modify genSubconReport(), genDealerReport(), 
 * 							 genLienHolderReport()
 * 							defect 10168 Ver POS_640 
 * K Harrell	04/03/2010	Implement OfficeTimeZoneCache
 * 							delete TIME_ZONE_CALL_BEGIN
 * 							modify updateRSPS()  
 * 							defect 10427 Ver POS_640  
 * K Harrell	01/18/2011	add getBatchRptMgmt(), updBatchRptMgmt(),
 * 							processData()
 * 							defect 10701 Ver 6.7.0
 * Ray Rowehl	02/08/2012	Add Rollback to close the uow on 
 * 							getBatchRptMgmt.
 * 							modify getBatchRptMgmt()
 * 							defect 11180 Ver 6.10.0
 * Ray Rowehl	02/18/2012	Change Rollback to Commit per walkthru 
 * 							request.
 * 							modify getBatchRptMgmt()
 * 							defect 11180 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Local Options server business layer.
 * 
 * @version 6.10.0	 	 	02/18/2012
 * @author	Ashish Mahajan
 * @since 09/06/2001 16:50:10
 */

public class LocalOptionsServerBusiness
{
	private static final String PERCENT = "%";
	private static final String ZERO = "0";
	private static final String MISSING = "Missing";

	// Report File Names 
	private static final String DLRLST = "DLRLST";
	private static final String SECEMP = "SECEMP";
	private static final String SECEVT = "SECEVT";
	private static final String LIENHDL = "LIENHDL";
	private static final String PUBLISH = "PUBLISH";
	private static final String SECCHNG = "SECCHNG";
	private static final String SUBLST = "SUBLST";
	// defect 9968
	private static final String CERTLIEN = "CERTLIEN";
	// end defect 9968

	// End Report File Names

	private static final String JAVA_ERROR = "Java Error";
	private static final String RTS_JAVA_EX = "RTS Java Exception";
	private static final String START_DB_CALL_MISC =
		"Starting DB call to Miscellaneous";
	private static final String SUCCESS_DB_CALL_MISC =
		"Successful DB call to Miscellaneous";
	private static final String FAILED_DB_CALL_MISC =
		"Failed DB call to Miscellaneous";

	// defect 10427 
	//	private static final String TIME_ZONE_CALL_BEGIN =
	//		" - CacheManagerServerBusiness - getOfficeTimeZone - Begin";
	// end defect 10427 

	private static final String QRY_TIMESTAMP_BEGIN =
		" - qryCurrentTimestamp - Begin";
	private static final String QRY_TIMESTAMP_END =
		" - qryCurrentTimestamp - End";
	private static final String ERROR = "ERROR";
	private static final String INV_PROFILE_REC_MSG =
		"Inventory Profile record(s) for this entity "
			+ "exist at substation ";
	private static final String DEL_INV_PROFILES_MSG =
		".  Delete Inventory Profile(s) for this "
			+ "entity before deleting.";
	private static final String INV_EXIST_MSG =
		"Inventory exists for this entity at substation ";
	private static final String REM_INV_MSG =
		".  Remove Inventory from this entity before deleting.";

	// defect 8595 
	private static final int MAX_ENTITY_VALUE_LENGTH = 25;
	private static final String RSPS_ENTITY = "RSPS";
	// end defect 8595 

	private String csClientHost = "Unknown";

	/**
	 * LocalOptionsServerBusiness constructor comment.
	 */
	public LocalOptionsServerBusiness()
	{
		super();
	}

	/**
	 * LocalOptionsServerBusiness constructor comment.
	 * 
	 * @param String asClientHost
	 */
	public LocalOptionsServerBusiness(String asClientHost)
	{
		super();
		csClientHost = asClientHost;
	}

	/**
	 * Update RTS_CRDT_CARD_FEE
	 * 
	 * @param	aaData Object   
	 * @return	Object
	 * @throws	RTSException
	 */
	private Object addCreditUpdate(Object aaData) throws RTSException
	{
		Map laMap = (Map) aaData;

		CreditCardFeeData laCreditCardFeeData =
			(CreditCardFeeData) laMap.get(
				CreditCardFeesConstants.SELECTED_RECORD);

		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			laDBA.beginTransaction();

			// defect 8721
			// Insert record into RTS_CRDT_CARD_FEE
			CreditCardFee laCreditCardFee = new CreditCardFee(laDBA);
			laCreditCardFee.insCreditCardFee(laCreditCardFeeData);

			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laCreditCardFeeData.getOfcIssuanceNo(),
				laCreditCardFeeData.getSubstaId(),
				LocalOptionConstant.RTS_CRDT_CARD_FEE,
				laDBA);
			// end defect 8721 	

			// defect 8595 
			// Insert record into RTS_ADMIN_LOG 
			insAdminLog(
				(AdministrationLogData) laMap.get(
					CreditCardFeesConstants.ADMIN),
				laDBA);
			// end defect 8595 

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Adds a Dealer in the database.
	 * 
	 * @param  aaData Object   
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object addDlr(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			laDBA.beginTransaction();

			// defect 10112
			// Insert record into RTS_DEALERS
			Dealer laDealer = new Dealer(laDBA);
			Vector lvVector = (Vector) aaData;
			DealerData laDealerData = (DealerData) lvVector.get(0);
			laDealer.insDealer(laDealerData);

			// defect 8721 
			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laDealerData.getOfcIssuanceNo(),
				laDealerData.getSubstaId(),
				LocalOptionConstant.RTS_DEALERS,
				laDBA);
			// end defect 8721
			// end defect 10112 

			// defect 8595 
			// Insert record into RTS_ADMIN_LOG 
			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);
			// end defect 8595 

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Adds the employee security information in the database
	 *  
	 * @param  aaData Object   
	 * @return Object
	 * @throws RTSException 
	 */
	private Object addEmpSec(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			Vector lvVector = (Vector) aaData;
			Security laSecurity = new Security(laDBA);
			SecurityLog laSecurityLog = new SecurityLog(laDBA);
			SecurityData laSecurityData =
				(SecurityData) lvVector.get(0);
			// defect 6445
			// defect 6865
			// Use the appropriate object to get UserName.
			// Set the ChngTimestmp for search.
			// commit delete before attempting insert.
			// make sure UserName is not a dup.
			// Unique Index will be setup later
			// Do not check if it is null. 
			if (laSecurityData.getUserName() != null
				&& laSecurityData.getUserName().length() > 0)
			{
				SecurityData laChkForDupUserName =
					(SecurityData) UtilityMethods.copy(laSecurityData);
				laChkForDupUserName.setEmpId(null);
				laChkForDupUserName.setChngTimestmp(
					new RTSDate(1900, 0, 0));
				Vector lvSecChkDupUserName =
					laSecurity.qrySecurity(laChkForDupUserName);
				if (lvSecChkDupUserName.size() > 0)
				{
					// check to see if it is a deleted row
					SecurityData laChkForDelete =
						(SecurityData) lvSecChkDupUserName.elementAt(0);
					if (laChkForDelete.getDeleteIndi() == 0)
					{
						// throw an invalid userid error if this is not
						// a deleted employee
						throw new RTSException(770);
					}
					else
					{
						// delete the row for real
						laSecurity.delSecurity(
							laChkForDupUserName,
							true);
						laDBA.endTransaction(DatabaseAccess.COMMIT);
						laDBA.beginTransaction();
					}
				}
				// check for Duplicate EmployeeId before insert 
				SecurityData laChkForDupEmpId =
					(SecurityData) UtilityMethods.copy(laSecurityData);
				laChkForDupEmpId.setUserName(null);
				laChkForDupEmpId.setChngTimestmp(
					new RTSDate(1900, 0, 0));
				Vector lvSecChkDupEmpId =
					laSecurity.qrySecurity(laChkForDupEmpId);
				if (lvSecChkDupEmpId.size() > 0)
				{
					// check to see if it is a deleted row
					SecurityData laChkForDelete =
						(SecurityData) lvSecChkDupEmpId.elementAt(0);
					if (laChkForDelete.getDeleteIndi() == 0)
					{
						// throw an invalid userid error if this is not
						//  a deleted employee
						throw new RTSException(771);
					}
					else
					{
						// delete the row for real 
						laSecurity.delSecurity(laChkForDelete, false);
						laDBA.endTransaction(DatabaseAccess.COMMIT);
						laDBA.beginTransaction();
					}
				}
			}
			// end defect 6865
			// end defect 6445 
			laSecurity.insSecurity(laSecurityData);
			SecurityLogData laSecLogData =
				(SecurityLogData) lvVector.get(1);
			laSecurityLog.insSecurityLog(laSecLogData);

			// defect 8721 
			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laSecurityData.getOfcIssuanceNo(),
				laSecurityData.getSubstaId(),
				LocalOptionConstant.RTS_SECURITY,
				laDBA);
			// end defect 8721 

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			// defect 6445
			// refer to the constant in static
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			// end defect 6445
			throw aeRTSEx;
		}
	}

	/**
	 * Adds the Leinholder information in the database
	 *  
	 * @param  aaData Object   
	 * @return Object
	 * @throws RTSException 
	 */
	private Object addLien(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			laDBA.beginTransaction();

			// defect 10112 	
			// Insert record into RTS_LIENHOLDERS 
			Lienholder laLienholder = new Lienholder(laDBA);
			Vector lvVector = (Vector) aaData;
			LienholderData laLienholderData =
				(LienholderData) lvVector.get(0);
			laLienholder.insLienholder(laLienholderData);

			// defect 8721 
			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laLienholderData.getOfcIssuanceNo(),
				laLienholderData.getSubstaId(),
				LocalOptionConstant.RTS_LIENHOLDERS,
				laDBA);
			// end defect 8721
			// end defect 10112

			// defect 8595 
			// Insert record into RTS_ADMIN_LOG 
			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);
			// end defect 8595
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Adds a payment accounts to the DB.
	 * 
	 * @param  aaData Object  
	 * @return Object
	 * @throws RTSException 
	 */
	private Object addPaymentAccount(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			// defect 8595 
			// Implement logging in RTS_ADMIN_LOG 
			Vector lvVector = (Vector) aaData;

			laDBA.beginTransaction();

			// Insert into RTS_PYMNT_ACCT 
			PaymentAccount laPaymentAccount = new PaymentAccount(laDBA);
			PaymentAccountData laPaymentAccountData =
				(PaymentAccountData) lvVector.get(0);
			laPaymentAccount.insPaymentAccount(laPaymentAccountData);

			// defect 8721 
			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laPaymentAccountData.getOfcIssuanceNo(),
				laPaymentAccountData.getSubstaId(),
				LocalOptionConstant.RTS_PYMNT_ACCT,
				laDBA);
			// end defect 8721 

			// Insert into RTS_ADMIN_LOG 
			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);
			// end defect 8595

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Adds a Subcontractor to the database
	 * 
	 * @param  aaData Object   
	 * @return Object
	 * @throws RTSException 
	 */
	private Object addSubcon(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			Subcontractor laSubcontractor = new Subcontractor(laDBA);
			Vector lvVector = (Vector) aaData;

			// Insert into RTS_SUBCON
			SubcontractorData laSubcontractorData =
				(SubcontractorData) lvVector.get(0);
			laSubcontractor.insSubcontractor(laSubcontractorData);

			// defect 8721 
			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laSubcontractorData.getOfcIssuanceNo(),
				laSubcontractorData.getSubstaId(),
				LocalOptionConstant.RTS_SUBCON,
				laDBA);
			// end defect 8721 	

			// defect 8595
			// Insert into RTS_ADMIN_LOG
			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);
			// end defect 8595

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Delete from RTS_CRDT_CARD_FEE
	 * 
	 * @param  aaData Map 
	 * @return Object
	 * @throws RTSException 
	 */
	private Object delCreditUpdate(Object aaData) throws RTSException
	{
		Map laMap = (Map) aaData;
		Vector lvFees =
			(Vector) laMap.get(CreditCardFeesConstants.DATA);
		CreditCardFeeData laCreditCardFeeData =
			(CreditCardFeeData) laMap.get(
				CreditCardFeesConstants.SELECTED_RECORD);
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();

			// Delete from RTS_CRDT_CARD_FEE 
			CreditCardFee laCreditCardFee = new CreditCardFee(laDBA);
			laCreditCardFee.delCreditCardFee(laCreditCardFeeData);

			// defect 8721 
			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laCreditCardFeeData.getOfcIssuanceNo(),
				laCreditCardFeeData.getSubstaId(),
				LocalOptionConstant.RTS_CRDT_CARD_FEE,
				laDBA);
			// end defect 8721 	

			// Insert into RTS_ADMIN_LOG 
			// defect 8595 
			insAdminLog(
				(AdministrationLogData) laMap.get(
					CreditCardFeesConstants.ADMIN),
				laDBA);
			// end defect 8595

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return lvFees;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Delete a dealer from the database
	 *
	 * @param  aaData Object 
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object delDlr(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();

			// defect 10112 
			Dealer laDealer = new Dealer(laDBA);
			InventoryAllocation laInventoryAllocation =
				new InventoryAllocation(laDBA);
			InventoryProfile laInventoryProfile =
				new InventoryProfile(laDBA);
			int liSubstaId;
			Vector lvVector = (Vector) aaData;
			DealerData laDealerData = (DealerData) lvVector.get(0);
			// end defect 10112 

			// Verify Inventory Allocation for Dealer 
			// Check if the dealer has inventory allocated
			// Can only delete a dealer if there is no inventory 
			// allocated to the dealer
			InventoryAllocationData laInventoryAllocationData =
				new InventoryAllocationData();
			laInventoryAllocationData.setOfcIssuanceNo(
				laDealerData.getOfcIssuanceNo());
			laInventoryAllocationData.setSubstaId(
				laDealerData.getSubstaId());
			laInventoryAllocationData.setInvId(
				CommonConstant.STR_SPACE_EMPTY + laDealerData.getId());
			laInventoryAllocationData.setInvLocIdCd(
				CommonConstant.POS_DTA);

			// Verify Inventory Profile for Dealer 
			// Can only delete a dealer if there is no inventory profile
			// for the dealer
			InventoryProfileData laInventoryProfileData =
				new InventoryProfileData();
			laInventoryProfileData.setOfcIssuanceNo(
				laDealerData.getOfcIssuanceNo());
			laInventoryProfileData.setSubstaId(
				laDealerData.getSubstaId());
			laInventoryProfileData.setId(
				CommonConstant.STR_SPACE_EMPTY + laDealerData.getId());
			laInventoryProfileData.setEntity(CommonConstant.POS_DTA);
			liSubstaId =
				laInventoryAllocation
					.qryMaxSubstaIdForInventoryAllocation(
					laInventoryAllocationData);

			if (liSubstaId != Integer.MIN_VALUE)
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						INV_EXIST_MSG + liSubstaId + REM_INV_MSG,
						ERROR);
				leRTSEx.setBeep(RTSException.BEEP);
				throw leRTSEx;
			}

			liSubstaId =
				laInventoryProfile.qryMaxSubstaIdForInventoryProfile(
					laInventoryProfileData);

			if (liSubstaId != Integer.MIN_VALUE)
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						INV_PROFILE_REC_MSG
							+ liSubstaId
							+ DEL_INV_PROFILES_MSG,
						ERROR);
				leRTSEx.setBeep(RTSException.BEEP);
				throw leRTSEx;
			}
			// defect 10112 
			// Delete from RTS_DEALERS 
			laDealer.delDealer(laDealerData);

			// defect 8721 
			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laDealerData.getOfcIssuanceNo(),
				laDealerData.getSubstaId(),
				LocalOptionConstant.RTS_DEALERS,
				laDBA);
			// end defect 8721 

			// Delete from RTS_RSPS_WS_STATUS 
			// defect 7135
			// delete any existing rsps ws status records
			RSPSWsStatusData laRSPSWsStatusData =
				new RSPSWsStatusData();
			laRSPSWsStatusData.setOfcIssuanceNo(
				laDealerData.getOfcIssuanceNo());

			// defect 7899 
			laRSPSWsStatusData.setLocIdCd(CommonConstant.POS_DTA);
			laRSPSWsStatusData.setLocId(laDealerData.getId());
			RSPSWsStatus laRSPSWsStatusSQL = new RSPSWsStatus(laDBA);

			// defect 8595/8721			
			AdministrationLogData laAdministrationLogData =
				(AdministrationLogData) lvVector.get(1);

			// Insert into RTS_ADMIN_LOG
			insAdminLog(laAdministrationLogData, laDBA);

			// Update RTS_ADMIN_CACHE for RSPS if any rows deleted 
			int liNumRows =
				laRSPSWsStatusSQL.delRSPSWsStatus(laRSPSWsStatusData);

			if (liNumRows != 0)
			{
				updateAdminCache(
					laRSPSWsStatusData.getOfcIssuanceNo(),
					0,
					LocalOptionConstant.RTS_RSPS_WS_STATUS,
					laDBA);

				AdministrationLogData laRSPSAdministrationLogData =
					(AdministrationLogData) UtilityMethods.copy(
						laAdministrationLogData);
				laRSPSAdministrationLogData.setEntity(RSPS_ENTITY);
				String lsRecord =
					liNumRows == 1 ? " record " : " records";
				String lsEntityValue =
					CommonConstant.POS_DTA
						+ UtilityMethods.addPadding(
							"" + laDealerData.getId(),
							3,
							"0")
						+ " "
						+ liNumRows
						+ lsRecord;
				laRSPSAdministrationLogData.setEntityValue(
					lsEntityValue);
				insAdminLog(laRSPSAdministrationLogData, laDBA);
			}
			// end defect 8595/8721 

			// Delete any existing rsps ws sys update history
			RSPSSysUpdateHistoryData laRSPSSysData =
				new RSPSSysUpdateHistoryData();
			laRSPSSysData.setOfcIssuanceNo(
				laDealerData.getOfcIssuanceNo());
			String lsRSPSLike =
				CommonConstant.POS_DTA
					+ UtilityMethods.addPadding(
						String.valueOf(laDealerData.getId()),
						3,
						ZERO)
					+ PERCENT;
			// end defect 10112 

			laRSPSSysData.setRSPSId(lsRSPSLike);
			RSPSSysUpdateHistory laRSPSSysSQL =
				new RSPSSysUpdateHistory(laDBA);
			laRSPSSysSQL.delRSPSSysWsUpdateHstry(laRSPSSysData);
			// end defect 7135
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Delete employee laSecurity information from the Database
	 *  
	 * @param  aaData Object 
	 * @return Object
	 * @throws RTSException 
	 */
	private Object delEmpSec(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			Security laSecurity = new Security(laDBA);
			InventoryAllocation laInventoryAllocation =
				new InventoryAllocation(laDBA);
			InventoryProfile laInventoryProfile =
				new InventoryProfile(laDBA);
			SecurityLog laSecurityLog = new SecurityLog(laDBA);
			int liSubstaId;
			Vector lvVector = (Vector) aaData;
			SecurityData laSecurityData =
				(SecurityData) lvVector.get(0);

			// Verify Inventory Allocation for Employee
			// Check if the employee has inventory allocated
			// Can only delete an employee if there is no inventory 
			// allocated to employee
			InventoryAllocationData laInvAllocData =
				new InventoryAllocationData();
			laInvAllocData.setOfcIssuanceNo(
				laSecurityData.getOfcIssuanceNo());
			laInvAllocData.setSubstaId(laSecurityData.getSubstaId());
			laInvAllocData.setInvId(
				CommonConstant.STR_SPACE_EMPTY
					+ laSecurityData.getEmpId());
			laInvAllocData.setInvLocIdCd(InventoryConstant.CHAR_E);

			// Verify Inventory Profile for Employee 
			// Can only delete an employee if there is no inventory 
			// profile for employee
			InventoryProfileData laInventoryProfileData =
				new InventoryProfileData();
			laInventoryProfileData.setOfcIssuanceNo(
				laSecurityData.getOfcIssuanceNo());
			laInventoryProfileData.setSubstaId(
				laSecurityData.getSubstaId());
			laInventoryProfileData.setId(
				CommonConstant.STR_SPACE_EMPTY
					+ laSecurityData.getEmpId());
			laInventoryProfileData.setEntity(InventoryConstant.CHAR_E);

			liSubstaId =
				laInventoryAllocation
					.qryMaxSubstaIdForInventoryAllocation(
					laInvAllocData);

			if (liSubstaId != Integer.MIN_VALUE)
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						INV_EXIST_MSG + liSubstaId + REM_INV_MSG,
						ERROR);
				leRTSEx.setBeep(RTSException.BEEP);
				throw leRTSEx;
			}
			liSubstaId =
				laInventoryProfile.qryMaxSubstaIdForInventoryProfile(
					laInventoryProfileData);

			if (liSubstaId != Integer.MIN_VALUE)
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						INV_PROFILE_REC_MSG
							+ liSubstaId
							+ DEL_INV_PROFILES_MSG,
						ERROR);
				leRTSEx.setBeep(RTSException.BEEP);
				throw leRTSEx;
			}

			// Delete from RTS_SECURITY 
			laSecurity.delSecurity(laSecurityData);
			SecurityLogData laSecurityLogData =
				(SecurityLogData) lvVector.get(1);

			// Insert into RTS_SECURITY_LOG 
			laSecurityLog.insSecurityLog(laSecurityLogData);

			// defect 8721 
			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laSecurityData.getOfcIssuanceNo(),
				laSecurityData.getSubstaId(),
				LocalOptionConstant.RTS_SECURITY,
				laDBA);
			// end defect 8721 

			// defect 7462
			// qry Security to see if other entries exist for username
			// if vector size is greater then 0, return it.
			laSecurityData.setSubstaId(Integer.MIN_VALUE);
			laSecurityData.setChngTimestmp(null);

			// Query RTS_SECURITY 
			Vector lvSecurity = laSecurity.qrySecurity(laSecurityData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			if (lvSecurity.size() < 1)
			{
				return null;
			}
			else
			{
				return lvSecurity;
			}
			// end defect 7462
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Delete leinholder information from the database
	 * 
	 * @param  Object Vector 
	 * @return Object
	 * @throws RTSException 
	 */
	private Object delLien(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			// defect 10112 
			Lienholder laLienholder = new Lienholder(laDBA);
			Vector lvVector = (Vector) aaData;

			// Delete from RTS_LIENHOLDERS 
			LienholderData laLienholderData =
				(LienholderData) lvVector.get(0);
			laLienholder.delLienholder(laLienholderData);
			// end defect 10112 

			// defect 8721 
			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laLienholderData.getOfcIssuanceNo(),
				laLienholderData.getSubstaId(),
				LocalOptionConstant.RTS_LIENHOLDERS,
				laDBA);
			// end defect 8721 

			// defect 8595
			// Insert into RTS_ADMIN_LOG
			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);
			// end defect 8595
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Delete a payment account from the DB.
	 *
	 * @param  aaData Object 
	 * @return Object
	 * @throws RTSException
	 */
	private Object delPaymentAccount(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			// defect 8595 
			// Implement logging in RTS_ADMIN_LOG 
			Vector lvVector = (Vector) aaData;

			laDBA.beginTransaction();

			// Delete from RTS_PYMNT_ACCT 
			PaymentAccount laPaymentAccount = new PaymentAccount(laDBA);
			PaymentAccountData laPaymentAccountData =
				(PaymentAccountData) lvVector.get(0);
			laPaymentAccount.delPaymentAccount(laPaymentAccountData);

			// defect 8721 
			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laPaymentAccountData.getOfcIssuanceNo(),
				laPaymentAccountData.getSubstaId(),
				LocalOptionConstant.RTS_PYMNT_ACCT,
				laDBA);
			// end defect 8721

			// Insert into RTS_ADMIN_LOG
			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);
			// end defect 8595

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Delete a subcontractor from the database
	 * 
	 * @param  aaData Object 
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object delSubcon(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			Subcontractor laSubcontractor = new Subcontractor(laDBA);
			InventoryAllocation laInventoryAllocation =
				new InventoryAllocation(laDBA);
			InventoryProfile laInventoryProfile =
				new InventoryProfile(laDBA);
			int liSubstaId;
			Vector lvVector = (Vector) aaData;
			SubcontractorData laSubcontractorData =
				(SubcontractorData) lvVector.get(0);

			// Verify Inventory Allocation for Subcontractor 
			// Check if the Subcontractor has inventory allocated
			// Can only delete a subcontractor if there is no inventory 
			// allocated
			InventoryAllocationData laInventoryAllocationData =
				new InventoryAllocationData();
			laInventoryAllocationData.setOfcIssuanceNo(
				laSubcontractorData.getOfcIssuanceNo());
			laInventoryAllocationData.setSubstaId(
				laSubcontractorData.getSubstaId());

			// defect 10161 
			laInventoryAllocationData.setInvId(
				CommonConstant.STR_SPACE_EMPTY
					+ laSubcontractorData.getId());
			// end defect 10161 

			laInventoryAllocationData.setInvLocIdCd(
				InventoryConstant.CHAR_S);

			// Verify Inventory Profile for Subcontractor 
			InventoryProfileData laInventoryProfileData =
				new InventoryProfileData();
			laInventoryProfileData.setOfcIssuanceNo(
				laSubcontractorData.getOfcIssuanceNo());
			laInventoryProfileData.setSubstaId(
				laSubcontractorData.getSubstaId());

			// defect 10161 
			laInventoryProfileData.setId(
				CommonConstant.STR_SPACE_EMPTY
					+ laSubcontractorData.getId());
			// end defect 10161 

			laInventoryProfileData.setEntity(InventoryConstant.CHAR_S);

			liSubstaId =
				laInventoryAllocation
					.qryMaxSubstaIdForInventoryAllocation(
					laInventoryAllocationData);

			if (liSubstaId != Integer.MIN_VALUE)
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						INV_EXIST_MSG + liSubstaId + REM_INV_MSG,
						ERROR);
				leRTSEx.setBeep(RTSException.BEEP);
				throw leRTSEx;
			}
			liSubstaId =
				laInventoryProfile.qryMaxSubstaIdForInventoryProfile(
					laInventoryProfileData);

			if (liSubstaId != Integer.MIN_VALUE)
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						INV_PROFILE_REC_MSG
							+ liSubstaId
							+ DEL_INV_PROFILES_MSG,
						ERROR);
				leRTSEx.setBeep(RTSException.BEEP);
				throw leRTSEx;
			}
			laSubcontractor.delSubcontractor(laSubcontractorData);

			// defect 8721 
			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laSubcontractorData.getOfcIssuanceNo(),
				laSubcontractorData.getSubstaId(),
				LocalOptionConstant.RTS_SUBCON,
				laDBA);
			// end defect 8721 	

			// defect 7135
			// delete any existing rsps ws status records
			RSPSWsStatusData laRSPSWsStatusData =
				new RSPSWsStatusData();
			laRSPSWsStatusData.setOfcIssuanceNo(
				laSubcontractorData.getOfcIssuanceNo());
			// defect 7899
			// SubstaId not longer part of RSPSWsStatusData
			//laRSPSWsStatusData.setSubstaId(
			//					laSubcontractorData.getSubstaId());
			// end defect 7899
			laRSPSWsStatusData.setLocIdCd(CommonConstant.POS_SUB);

			// defect 10161 
			laRSPSWsStatusData.setLocId(laSubcontractorData.getId());
			// end defect 10161 

			RSPSWsStatus laRSPSWsStatusSQL = new RSPSWsStatus(laDBA);
			// defect 8721 
			// Update RTS_ADMIN_CACHE for RSPS if any rows deleted 
			int liNumRows =
				laRSPSWsStatusSQL.delRSPSWsStatus(laRSPSWsStatusData);

			// defect 8595
			// Insert into RTS_ADMIN_LOG
			AdministrationLogData laAdministrationLogData =
				(AdministrationLogData) lvVector.get(1);

			insAdminLog(laAdministrationLogData, laDBA);

			if (liNumRows != 0)
			{
				// defect 8721 
				updateAdminCache(
					laRSPSWsStatusData.getOfcIssuanceNo(),
					0,
					LocalOptionConstant.RTS_RSPS_WS_STATUS,
					laDBA);
				// end defect 8721 

				AdministrationLogData laRSPSAdministrationLogData =
					(AdministrationLogData) UtilityMethods.copy(
						laAdministrationLogData);
				laRSPSAdministrationLogData.setEntity(RSPS_ENTITY);
				String lsRecord =
					liNumRows == 1 ? " record " : " records";

				// defect 10161 
				String lsEntityValue =
					CommonConstant.POS_SUB
						+ UtilityMethods.addPadding(
							"" + laSubcontractorData.getId(),
							3,
							"0")
						+ " "
						+ liNumRows
						+ lsRecord;
				// end defect 10161 

				laRSPSAdministrationLogData.setEntityValue(
					lsEntityValue);
				insAdminLog(laRSPSAdministrationLogData, laDBA);
			}
			// end defect 8595 

			// delete any existing rsps ws sys update history
			RSPSSysUpdateHistoryData laRSPSSysData =
				new RSPSSysUpdateHistoryData();
			laRSPSSysData.setOfcIssuanceNo(
				laSubcontractorData.getOfcIssuanceNo());

			// defect 10161
			String lsRSPSLike =
				CommonConstant.POS_SUB
					+ UtilityMethods.addPadding(
						String.valueOf(laSubcontractorData.getId()),
						3,
						ZERO)
					+ PERCENT;
			// end defect 10161 

			laRSPSSysData.setRSPSId(lsRSPSLike);
			RSPSSysUpdateHistory laRSPSSysSQL =
				new RSPSSysUpdateHistory(laDBA);
			laRSPSSysSQL.delRSPSSysWsUpdateHstry(laRSPSSysData);
			// end defect 7135

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Generate Certified Lienholder Report 
	 * 
	 * @param  aaData Object ReportSearchData
	 * @return Object
	 * @throws RTSException 
	 */
	public Object genCertfdLienHldrReport(Object aaData)
		throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
		// defect 8628 
		// Reorganize Logic 		
		ReportProperties laRptProps = null;
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvQueryResults = new Vector();

		// defect 10250 
		boolean lbIdSort =
			laRptSearchData.getIntKey4()
				== ReportConstant.SORT_BY_ID_INDI;

		String lsSortBy = getAdminLogSortBy(lbIdSort);
		// end defect 10250

		try
		{
			// UOW #1 BEGIN 
			laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBA,
					ReportConstant.CERTFD_LIENHLDR_REPORT_ID);
			// UOW #1 END
			laRptProps.setPageOrientation(ReportConstant.LANDSCAPE);

			AdministrationLogData laAdminLogData =
				new AdministrationLogData(laRptSearchData);

			// defect 10168 
			laAdminLogData.setEntity(
				CommonConstant.TXT_ADMIN_LOG_CERTFD_LIENHLDR);
			laAdminLogData.setAction(CommonConstant.TXT_REPORT);
			// end defect 10168 

			// defect 10250 
			laAdminLogData.setEntityValue("All" + " - by " + lsSortBy);
			// end defect 10250 

			// UOW #2 BEGIN 
			laDBA.beginTransaction();
			AdministrationLog laAdminLog = new AdministrationLog(laDBA);
			laAdminLog.insAdministrationLog(laAdminLogData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END 

			// UOW #3 BEGIN 
			// Select from RTS_CERTFD_LIENHLDR
			laDBA.beginTransaction();
			CertifiedLienholder laCertifiedLienholder =
				new CertifiedLienholder(laDBA);
			CertifiedLienholderData laCertifiedLienholderData =
				new CertifiedLienholderData();
			// defect 10250 
			lvQueryResults =
				laCertifiedLienholder.qryCertifiedLienholder(
					laCertifiedLienholderData,
					lbIdSort);
			// end defect 10250 
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END 

		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		// defect 10250 
		// Generate the Report
		GenCertfdLienhldrReport laGR =
			new GenCertfdLienhldrReport(
				ReportConstant.CERTFD_LIENHLDR_REPORT_TITLE,
				laRptProps,
				lbIdSort);
		// end defect 10250 

		laGR.formatReport(lvQueryResults);

		return new ReportSearchData(
			laGR.getFormattedReport().toString(),
			CERTLIEN,
			ReportConstant.CERTFD_LIENHLDR_REPORT_TITLE,
			ReportConstant.RPT_1_COPY,
			ReportConstant.LANDSCAPE);
		// end defect 8628 
	}

	/**
	 * Generate a Dealer report
	 *  
	 * @param  aaData Object  
	 * @return Object 
	 * @throws RTSException 
	 */
	public Object genDealerReport(Object aaData) throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
		// defect 8628 
		// Reorganize Logic
		ReportProperties laRptProps = null;
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvQueryResults = new Vector();

		// defect 10250 
		boolean lbIdSort =
			laRptSearchData.getIntKey4()
				== ReportConstant.SORT_BY_ID_INDI;

		String lsSortBy = getAdminLogSortBy(lbIdSort);
		// end defect 10250

		int liOfcIssuanceNo = laRptSearchData.getIntKey1();
		int liSubstaId = laRptSearchData.getIntKey2();

		try
		{
			//	UOW #1 BEGIN 
			laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBA,
					ReportConstant.DEALER_REPORT_ID);
			// UOW #1 END

			AdministrationLogData laAdminLogData =
				new AdministrationLogData(laRptSearchData);

			// defect 10168 
			laAdminLogData.setEntity(CommonConstant.TXT_DEALER);
			laAdminLogData.setAction(CommonConstant.TXT_REPORT);
			// end defect 10168 

			// defect 10250 
			laAdminLogData.setEntityValue("All" + " - by " + lsSortBy);
			// end defect 10250 

			// UOW #2 BEGIN 
			laDBA.beginTransaction();
			AdministrationLog laAdminLog = new AdministrationLog(laDBA);
			laAdminLog.insAdministrationLog(laAdminLogData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END 

			// Initialize for Query  
			DealerData laDealerData = new DealerData();
			laDealerData.setOfcIssuanceNo(liOfcIssuanceNo);
			laDealerData.setSubstaId(liSubstaId);

			// UOW #3 BEGIN
			laDBA.beginTransaction();
			Dealer laDealer = new Dealer(laDBA);
			// defect 10250 
			lvQueryResults = laDealer.qryDealer(laDealerData, lbIdSort);
			// end defect 10250 
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END 
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		// defect 10250
		// Generate the Report
		GenDealerReport laGR =
			new GenDealerReport(
				ReportConstant.DEALER_REPORT,
				laRptProps,
				lbIdSort);
		// end defect 10250 

		laGR.formatReport(lvQueryResults);

		return new ReportSearchData(
			laGR.getFormattedReport().toString(),
			DLRLST,
			ReportConstant.DEALER_REPORT,
			ReportConstant.RPT_1_COPY,
			ReportConstant.PORTRAIT);
		// end defect 8628 
	}

	/**
	 * Generate an Employee Security Report 
	 * 
	 * @param  aaData Object  
	 * @return Object 
	 * @throws RTSException 
	 */
	public Object genEmployeeSecurityReport(Object aaData)
		throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
		// defect 8628 
		// Reorganize Logic 
		ReportProperties laRptProps = null;
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvQueryResults = new Vector();

		int liOfcIssuanceNo = laRptSearchData.getIntKey1();
		int liSubstaId = laRptSearchData.getIntKey2();

		try
		{
			// UOW #1 BEGIN 
			laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBA,
					ReportConstant.EMPSEC_REPORT_ID);
			// UOW #1 END

			// Initialize for Insert 
			AdministrationLogData laLogData =
				new AdministrationLogData(laRptSearchData);
			laLogData.setAction("Report");
			laLogData.setEntity("Security");
			laLogData.setEntityValue("Employee Security");

			// UOW #2 BEGIN
			laDBA.beginTransaction();
			AdministrationLog laAdminLog = new AdministrationLog(laDBA);
			laAdminLog.insAdministrationLog(laLogData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END

			// Initialize for query 
			SecurityData laSecurityData = new SecurityData();
			laSecurityData.setOfcIssuanceNo(liOfcIssuanceNo);
			laSecurityData.setSubstaId(liSubstaId);

			// UOW #3 BEGIN  
			laDBA.beginTransaction();
			Security laSecurity = new Security(laDBA);
			lvQueryResults = laSecurity.qrySecurity(laSecurityData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END 
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		// Generate the Report
		GenEmployeeSecurityReport laGenEmpSecRpt =
			new GenEmployeeSecurityReport(
				ReportConstant.EMPLOYEE_SECURITY_REPORT,
				laRptProps);
		laGenEmpSecRpt.formatReport(lvQueryResults);

		return new ReportSearchData(
			laGenEmpSecRpt.getFormattedReport().toString(),
			SECEMP,
			ReportConstant.EMPLOYEE_SECURITY_REPORT,
			ReportConstant.RPT_1_COPY,
			ReportConstant.PORTRAIT);
		// end defect 8628 
	}

	/**
	 * Generate event security report
	 * 
	 * @param aaData Object  
	 * @return Object
	 * @throws RTSException 
	 */
	public Object genEventSecurityReport(Object aaData)
		throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
		// defect 8628 
		// Reorganize Logic 
		ReportProperties laRptProps = null;
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvQueryResults = new Vector();

		try
		{
			// UOW #1 BEGIN 
			laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBA,
					ReportConstant.EVENT_SECURITY_REPORT_ID);
			// UOW #1 END

			// Initialized for Insert 
			AdministrationLog laAdminLog = new AdministrationLog(laDBA);
			AdministrationLogData laLogData =
				new AdministrationLogData(laRptSearchData);
			laLogData.setAction("Report");
			laLogData.setEntity("Security");
			laLogData.setEntityValue("Event Security");

			// UOW #2 BEGIN 
			laDBA.beginTransaction();
			laAdminLog.insAdministrationLog(laLogData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END 
			// end defect 8595

			// UOW #3 BEGIN 
			laDBA.beginTransaction();
			SubstationSubscription laSubstaSub =
				new SubstationSubscription(laDBA);
			int liSubstaId =
				laSubstaSub.qrySubstationSubscriptionSecurity(
					laRptSearchData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END
			laRptSearchData.setIntKey2(liSubstaId);

			// UOW #4 BEGIN 
			laDBA.beginTransaction();
			LocalOptionsReportsSQL laEventSecurity =
				new LocalOptionsReportsSQL(laDBA);
			lvQueryResults =
				laEventSecurity.qryEventSecurityReport(laRptSearchData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #4 END
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		// Generate the Report 
		GenEventSecurityReport laGR =
			new GenEventSecurityReport(
				ReportConstant.EVENT_SECURITY_REPORT,
				laRptProps);
		laGR.formatReport(lvQueryResults);

		return new ReportSearchData(
			laGR.getFormattedReport().toString(),
			SECEVT,
			ReportConstant.EVENT_SECURITY_REPORT,
			ReportConstant.RPT_1_COPY,
			ReportConstant.PORTRAIT);
		// end defect 8628 
	}

	/**
	 * Generate lienholder report
	 * 
	 * @param  aaData Object ReportSearchData
	 * @return Object
	 * @throws RTSException 
	 */
	public Object genLienHolderReport(Object aaData)
		throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
		// defect 8628 
		// Reorganize Logic 
		ReportProperties laRptProps = null;
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvQueryResults = new Vector();

		int liOfcIssuanceNo = laRptSearchData.getIntKey1();
		int liSubstaId = laRptSearchData.getIntKey2();

		// defect 10250 
		boolean lbIdSort =
			laRptSearchData.getIntKey4()
				== ReportConstant.SORT_BY_ID_INDI;

		String lsSortBy = getAdminLogSortBy(lbIdSort);
		// end defect 10250

		try
		{
			// UOW #1 BEGIN 
			laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBA,
					ReportConstant.LIENHOLDER_REPORT_ID);
			// UOW #1 END

			AdministrationLogData laAdminLogData =
				new AdministrationLogData(laRptSearchData);

			// defect 10168 
			laAdminLogData.setEntity(CommonConstant.TXT_LIENHOLDER);
			laAdminLogData.setAction(CommonConstant.TXT_REPORT);
			// end defect 10168 

			// defect 10250 
			laAdminLogData.setEntityValue("All" + " - by " + lsSortBy);
			// end defect 10250 

			// UOW #2 BEGIN 
			laDBA.beginTransaction();
			AdministrationLog laAdminLog = new AdministrationLog(laDBA);
			laAdminLog.insAdministrationLog(laAdminLogData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END 

			// Initialized for Query
			LienholderData laLienholderData = new LienholderData();
			laLienholderData.setOfcIssuanceNo(liOfcIssuanceNo);
			laLienholderData.setSubstaId(liSubstaId);

			// Select from RTS_LIENHOLDERS
			// UOW #3 BEGIN 
			laDBA.beginTransaction();
			Lienholder laLienholder = new Lienholder(laDBA);

			// defect 10250 
			lvQueryResults =
				laLienholder.qryLienholder(laLienholderData, lbIdSort);
			// end defect 10250 

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END 
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		// defect 10250
		// Generate the Report  
		GenLienHolderReport laGR =
			new GenLienHolderReport(
				ReportConstant.LIENHOLDER_REPORT,
				laRptProps,
				lbIdSort);
		// end defect 10250  

		laGR.formatReport(lvQueryResults);

		return new ReportSearchData(
			laGR.getFormattedReport().toString(),
			LIENHDL,
			ReportConstant.LIENHOLDER_REPORT,
			ReportConstant.RPT_1_COPY,
			ReportConstant.PORTRAIT);
		// end defect 8628 
	}

	/**
	 * Generate publishing report
	 * 
	 * @param  aaData Object  
	 * @return Object
	 * @throws RTSException 
	 */
	public Object genPublishingReport(Object aaData)
		throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
		// defect 8628 
		// Reorganize Logic 
		ReportProperties laRptProps = null;
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvQueryResults = new Vector();

		int liOfcIssuanceNo = laRptSearchData.getIntKey1();

		try
		{
			// UOW #1 BEGIN 
			laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBA,
					ReportConstant.PUBLISHING_REPORT_ID);
			// UOW #1 END

			AdministrationLogData laAdminLogData =
				new AdministrationLogData(laRptSearchData);
			laAdminLogData.setEntity("Publishing");
			laAdminLogData.setAction("Report");
			laAdminLogData.setEntityValue("All");

			// UOW #2 BEGIN 
			laDBA.beginTransaction();
			AdministrationLog laAdminLog = new AdministrationLog(laDBA);
			laAdminLog.insAdministrationLog(laAdminLogData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END 

			// UOW #3 BEGIN
			// Select from RTS_SUBSTA_SUBSCR  
			laDBA.beginTransaction();
			LocalOptionsReportsSQL laLOReportsSQL =
				new LocalOptionsReportsSQL(laDBA);
			lvQueryResults =
				laLOReportsSQL.qryPublishingReport(liOfcIssuanceNo);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END 
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		// Generate the Report
		GenPublishingReport laGR =
			new GenPublishingReport(
				ReportConstant.PUBLISHING_REPORT_TITLE,
				laRptProps);
		laGR.formatReport(lvQueryResults);

		return new ReportSearchData(
			laGR.getFormattedReport().toString(),
			PUBLISH,
			ReportConstant.PUBLISHING_REPORT_TITLE,
			ReportConstant.RPT_1_COPY,
			ReportConstant.PORTRAIT);
		// end defect 8628  
	}

	/**
	 * Generate Security Change Report
	 * 
	 * @param  aaData Object 
	 * @return Object
	 * @throws RTSException 
	 */
	public Object genSecurityChangeReport(Object aaData)
		throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
		// defect 8628 
		// Reorganize Logic 
		ReportProperties laRptProps = null;
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvQueryResults = new Vector();

		int liOfcIssuanceNo = laRptSearchData.getIntKey1();
		int liSubstaId = laRptSearchData.getIntKey2();

		try
		{
			// UOW #1 BEGIN 
			laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBA,
					ReportConstant.SECCHG_REPORT_ID);
			// UOW #1 END

			// Initialized for Insert 
			AdministrationLogData laLogData =
				new AdministrationLogData(laRptSearchData);
			laLogData.setAction("Report");
			laLogData.setEntity("Security");
			laLogData.setEntityValue("Security Change");

			// UOW #2 BEGIN
			laDBA.beginTransaction();
			AdministrationLog laAdminLog = new AdministrationLog(laDBA);
			laAdminLog.insAdministrationLog(laLogData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END

			// Initialize for query 
			SecurityLogData laSecurityLogData = new SecurityLogData();
			laSecurityLogData.setOfcIssuanceNo(liOfcIssuanceNo);
			laSecurityLogData.setSubstaId(liSubstaId);

			// UOW #3 BEGIN 
			laDBA.beginTransaction();
			SecurityLog laSecurityLog = new SecurityLog(laDBA);
			lvQueryResults =
				laSecurityLog.qrySecurityLog(laSecurityLogData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END 
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		// Generate the Report
		GenSecurityChangeReport laGR =
			new GenSecurityChangeReport(
				ReportConstant.SECURITY_CHANGE_REPORT,
				laRptProps);
		laGR.formatReport(lvQueryResults);

		return new ReportSearchData(
			laGR.getFormattedReport().toString(),
			SECCHNG,
			ReportConstant.SECURITY_CHANGE_REPORT,
			ReportConstant.RPT_1_COPY,
			ReportConstant.PORTRAIT);
		// end defect 8628 
	}

	/**
	 * Generate Subcon Report
	 * 
	 * @param  aaData Object  
	 * @return Object
	 * @throws RTSException 
	 */
	public Object genSubconReport(Object aaData) throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
		// defect 8628 
		// Reorganize Logic 
		ReportProperties laRptProps = null;
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvQueryResults = new Vector();

		// defect 10250 
		boolean lbIdSort =
			laRptSearchData.getIntKey4()
				== ReportConstant.SORT_BY_ID_INDI;

		String lsSortBy = getAdminLogSortBy(lbIdSort);
		// end defect 10250

		int liOfcIssuanceNo = laRptSearchData.getIntKey1();
		int liSubstaId = laRptSearchData.getIntKey2();

		try
		{
			// UOW #1 BEGIN 
			laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBA,
					ReportConstant.SUBCON_REPORT_ID);
			// UOW #1 END

			AdministrationLogData laAdminLogData =
				new AdministrationLogData(laRptSearchData);

			// defect 10168 
			laAdminLogData.setEntity(CommonConstant.TXT_SUBCON);
			laAdminLogData.setAction(CommonConstant.TXT_REPORT);
			// end defect 10168 

			// defect 10250 
			laAdminLogData.setEntityValue("All" + " - by " + lsSortBy);
			// end defect 10250 

			// UOW #2 BEGIN 
			laDBA.beginTransaction();
			AdministrationLog laAdminLog = new AdministrationLog(laDBA);
			laAdminLog.insAdministrationLog(laAdminLogData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END 

			// Initialize for Query 
			SubcontractorData laSubcontratorData =
				new SubcontractorData();
			laSubcontratorData.setOfcIssuanceNo(liOfcIssuanceNo);
			laSubcontratorData.setSubstaId(liSubstaId);

			// UOW #3 BEGIN  
			laDBA.beginTransaction();
			Subcontractor laSubcontractor = new Subcontractor(laDBA);
			// defect 10250 
			lvQueryResults =
				laSubcontractor.qrySubcontractor(
					laSubcontratorData,
					lbIdSort);
			// end defect 10250 
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END 
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		// defect 10250
		// Generate the Report
		GenSubconReport laGR =
			new GenSubconReport(
				ReportConstant.SUBCONTRACTOR_REPORT,
				laRptProps,
				lbIdSort);
		// end defect 10250 

		laGR.formatReport(lvQueryResults);

		return new ReportSearchData(
			laGR.getFormattedReport().toString(),
			SUBLST,
			ReportConstant.SUBCONTRACTOR_REPORT,
			ReportConstant.RPT_1_COPY,
			ReportConstant.PORTRAIT);
		// end defect 8628 
	}

	/** 
	 * Get Sort By for Admin Log 
	 * 
	 * @param aaRptSearchData 
	 * @return String 
	 */
	private String getAdminLogSortBy(boolean abIdSort)
	{
		return abIdSort
			? ReportConstant.ADMIN_LOG_SORT_ORDER_ID
			: ReportConstant.ADMIN_LOG_SORT_ORDER_NAME;
	}

	/**
	 * Select from RTS_CRDT_CARD_FEE
	 * 
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object getCreditUpdates(Object aaData) throws RTSException
	{
		Map laMap = (Map) aaData;
		int liOfcIssuanceNo =
			((Integer) laMap.get(CreditCardFeesConstants.OFC))
				.intValue();

		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvQueryResults = new Vector();

		try
		{
			CreditCardFeeData laCreditCardFeeData =
				new CreditCardFeeData();
			laCreditCardFeeData.setOfcIssuanceNo(liOfcIssuanceNo);

			// UOW #1 BEGIN 
			// Select from RTS_CRDT_CARD_FEE			
			laDBA.beginTransaction();
			CreditCardFee laCreditCardFee = new CreditCardFee(laDBA);
			lvQueryResults =
				laCreditCardFee.qryCreditCardFee(laCreditCardFeeData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			// TODO Modify SQL to only return relevant rows (KPH) 
			// Remove Credit Card Fee Data where end data is earlier
			//  than today 
			int i = 0;
			while (i < lvQueryResults.size())
			{
				laCreditCardFeeData =
					(CreditCardFeeData) lvQueryResults.get(i);
				if (laCreditCardFeeData.getRTSEffEndDate().getAMDate()
					< RTSDate.getCurrentDate().getAMDate())
				{
					lvQueryResults.remove(i);
					continue;
				}
				i++;
			}
			return lvQueryResults;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Select from RTS_DEALERS 
	 * 
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object getDlrDataOnId(Object aaData) throws RTSException
	{
		DealerData laDealerData = (DealerData) aaData;
		Object laReturnData = null;
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			// UOW #1 BEGIN 
			// Select from RTS_DEALERS 
			laDBA.beginTransaction();
			Dealer laDealer = new Dealer(laDBA);
			Vector lvQueryResults = laDealer.qryDealer(laDealerData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			// defect 8628 
			// One return 
			if (lvQueryResults.size() != 0)
			{
				laReturnData = lvQueryResults.get(0);
			}
			return laReturnData;
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Select from RTS_SECURITY
	 * 
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object getEmpData(Object aaData) throws RTSException
	{
		SecurityData laSecurityData = (SecurityData) aaData;
		Object laReturnData = null;
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			// UOW #1 BEGIN
			// Select from RTS_SECURITY 
			laDBA.beginTransaction();
			Security laSecurity = new Security(laDBA);
			Vector lvQueryResults =
				laSecurity.qrySecurity(laSecurityData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			// defect 8628 
			// One return 
			if (lvQueryResults.size() != 0)
			{
				laReturnData = lvQueryResults.get(0);
			}
			return laReturnData;
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Select from RTS_LIENHOLDER
	 * 
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object getLienhldrDataOnId(Object aaData)
		throws RTSException
	{
		LienholderData laLienholderData = (LienholderData) aaData;
		Object laReturnData = null;
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			// UOW #1 BEGIN 
			laDBA.beginTransaction();
			Lienholder laLienholder = new Lienholder(laDBA);
			Vector lvQueryResults =
				laLienholder.qryLienholder(laLienholderData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			// defect 8628 
			if (lvQueryResults.size() != 0)
			{
				laReturnData = lvQueryResults.get(0);
			}
			return laReturnData;
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Select from RTS_PYMNT_ACCT
	 * 
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object getPaymentAccounts(Object aaData)
		throws RTSException
	{
		PaymentAccountData laPaymentAccountData =
			(PaymentAccountData) aaData;
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			laDBA.beginTransaction();

			// UOW #1 BEGIN 
			// Select from RTS_PYMNT_ACCT
			PaymentAccount laPaymentAccount = new PaymentAccount(laDBA);
			Vector lvQueryResults =
				laPaymentAccount.qryPaymentAccount(
					laPaymentAccountData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			return lvQueryResults;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Select from RTS_SUBCON
	 * 
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object getSubconDataOnId(Object aaData) throws RTSException
	{
		SubcontractorData laSubcontractorData =
			(SubcontractorData) aaData;
		Object laReturnData = null;
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			// UOW #1 BEGIN 
			laDBA.beginTransaction();
			// Select from RTS_SUBCON 
			Subcontractor laSubcontractor = new Subcontractor(laDBA);
			Vector lvQueryResults =
				laSubcontractor.qrySubcontractor(laSubcontractorData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			// defect 8628 
			// One return 
			if (lvQueryResults.size() != 0)
			{
				laReturnData = lvQueryResults.get(0);
			}
			return laReturnData;
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Create a Vector of SysUpdates already applied for an office
	 * 
	 * @param  aaData Object
	 * @return Vector
	 * @throws RTSException 
	 */
	private Vector getSysUpdtList(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			RSPSUpdData laRSPSUpdData = (RSPSUpdData) aaData;
			RSPSWsStatusData laRSPSWsStatusData =
				laRSPSUpdData.getRSPSWsStatusData();

			// defect 8628 
			// UOW #1 BEGIN 
			laDBA.beginTransaction();
			// get the count of workstations for this location
			RSPSWsStatus laRSPSWsStatus = new RSPSWsStatus(laDBA);
			Vector lvRSPSIds =
				laRSPSWsStatus.qryRSPSWsStatusLaptopList(
					laRSPSWsStatusData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);

			// UOW #2 BEGIN 
			// get the vector of Updates Applied
			laDBA.beginTransaction();
			// end defect 8628 
			Vector lvUpdatesApplied = new Vector();
			RSPSSysUpdateHistory laRSPSSysUpdateHistory =
				new RSPSSysUpdateHistory(laDBA);
			Vector lvUpdateList =
				laRSPSSysUpdateHistory.qryRSPSSysWsUpdtsHstryForLoc(
					laRSPSWsStatusData,
					lvRSPSIds);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END

			int liCount = lvRSPSIds.size();

			for (int i = 0; i < lvUpdateList.size(); i++)
			{
				Vector lvUpdateRow = (Vector) lvUpdateList.elementAt(i);

				boolean lbUpdateApplied = false;

				if (liCount == 1)
				{
					lbUpdateApplied = true;
				}
				else
				{
					String lsUpdate = (String) lvUpdateRow.elementAt(1);

					for (int k = 1; k < liCount; k++)
					{
						if ((i + k) >= lvUpdateList.size())
						{
							break;
						}
						Vector lvCompare =
							(Vector) lvUpdateList.elementAt(i + k);

						// if they do not equal, break out
						if (!lsUpdate
							.equalsIgnoreCase(
								(String) lvCompare.elementAt(1)))
						{
							break;
						}

						if ((k + 1) >= liCount)
						{
							lbUpdateApplied = true;
							i = i + k;
						}
					}

				}

				if (lbUpdateApplied)
				{
					lvUpdatesApplied.add(
						(String) lvUpdateRow.elementAt(2));
				}
			}

			return lvUpdatesApplied;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			laDBA = null;
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
	 * Insert AdminLog Data 
	 * 
	 * @param aaAdminLogData
	 * @param aaDBA
	 * @throws RTSException
	 */
	private void insAdminLog(
		AdministrationLogData aaAdminLogData,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		try
		{
			AdministrationLog laAdminLog = new AdministrationLog(aaDBA);
			String lsEntityValue = aaAdminLogData.getEntityValue();
			lsEntityValue =
				lsEntityValue.length() <= MAX_ENTITY_VALUE_LENGTH
					? lsEntityValue
					: lsEntityValue.substring(0, MAX_ENTITY_VALUE_LENGTH);
			aaAdminLogData.setEntityValue(lsEntityValue);
			laAdminLog.insAdministrationLog(aaAdminLogData);
		}
		catch (RTSException aaRTSEx)
		{
			throw aaRTSEx;
		}
	}

	/**
	 * This method does various operations depending on the functionId
	 * passed to it.
	 * 
	 * @param  aiModule String
	 * @param  aiFunctionId int
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException 
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
				case LocalOptionConstant.ADD_DLR :
					{
						return addDlr(aaData);
					}
				case LocalOptionConstant.DEL_DLR :
					{
						return delDlr(aaData);
					}
				case LocalOptionConstant.GET_DLR_DATA_ONID :
					{
						return getDlrDataOnId(aaData);
					}
				case LocalOptionConstant.REVISE_DLR :
					{
						return reviseDlr(aaData);
					}
				case LocalOptionConstant.ADD_LIENHLDR :
					{
						return addLien(aaData);
					}
				case LocalOptionConstant.DEL_LIENHLDR :
					{
						return delLien(aaData);
					}
				case LocalOptionConstant.REVISE_LIENHLDR :
					{
						return revLien(aaData);
					}
				case LocalOptionConstant.GET_LIENHLDR_DATA_ONID :
					{
						return getLienhldrDataOnId(aaData);
					}
				case LocalOptionConstant.ADD_SUBCON :
					{
						return addSubcon(aaData);
					}
				case LocalOptionConstant.DEL_SUBCON :
					{
						return delSubcon(aaData);
					}
				case LocalOptionConstant.REVISE_SUBCON :
					{
						return revSubcon(aaData);
					}
				case LocalOptionConstant.GET_SUBCON_DATA_ONID :
					{
						return getSubconDataOnId(aaData);
					}
				case LocalOptionConstant.RETRIEVE_CREDIT_FEES :
					{
						return getCreditUpdates(aaData);
					}
				case LocalOptionConstant.UPDATE_CREDIT_DB :
					{
						return updateCreditDB(aaData);
					}
				case LocalOptionConstant.GET_PAYMENT_ACCOUNTS :
					{
						return getPaymentAccounts(aaData);
					}
				case LocalOptionConstant.ADD_PAYMENT_ACCOUNT :
					{
						return addPaymentAccount(aaData);
					}
				case LocalOptionConstant.REVISE_PAYMENT_ACCOUNT :
					{
						return revisePaymentAccount(aaData);
					}
				case LocalOptionConstant.DELETE_PAYMENT_ACCOUNT :
					{
						return delPaymentAccount(aaData);
					}
				case LocalOptionConstant.LOGIN :
					{
						return processLogin(aaData);
					}
				case LocalOptionConstant.ADD_EMP_ACCS_RIGHTS :
					{
						return addEmpSec(aaData);
					}
				case LocalOptionConstant.DEL_EMP_ACCS_RIGHTS :
					{
						return delEmpSec(aaData);
					}
				case LocalOptionConstant.REVISE_EMP_ACCS_RIGHTS :
					{
						return updtEmpSec(aaData);
					}
				case LocalOptionConstant.GET_EMP_DATA_ONID :
					{
						return getEmpData(aaData);
					}
				case LocalOptionConstant.GENERATE_DLR_RPT :
					{
						return genDealerReport(aaData);
					}
				case LocalOptionConstant.GENERATE_LIENHLDR_RPT :
					{
						return genLienHolderReport(aaData);
					}
					// defect 9968
				case LocalOptionConstant.GENERATE_CERTFD_LIENHLDR_RPT :
					{
						return genCertfdLienHldrReport(aaData);
					}
					// end defect 9968
				case LocalOptionConstant.GENERATE_SUBCON_RPT :
					{
						return genSubconReport(aaData);
					}
				case LocalOptionConstant.GENERATE_PUBLISHING_RPT :
					{
						return genPublishingReport(aaData);
					}
				case LocalOptionConstant.GENERATE_EVENT_SECRTY_RPT :
					{
						return genEventSecurityReport(aaData);
					}
				case LocalOptionConstant.GENERATE_EMP_SECRTY_RPT :
					{
						return genEmployeeSecurityReport(aaData);
					}
				case LocalOptionConstant.GENERATE_SECRTY_CHNG_RPT :
					{
						return genSecurityChangeReport(aaData);
					}
				case LocalOptionConstant.SUPER_OVERRIDE_LOOKUP :
					{
						return supervisorLookup(aaData);
					}
					// defect 8595 
				case LocalOptionConstant.SUPV_OVERIDE :
					{
						// left intentionally empty
						// return supvOverride(aaData);
					}
				case LocalOptionConstant.SERVER_PLUS :
					{
						//return serverPlus(aaData);
						return updtMisc(aaData);
					}
					// end defect 8595 
				case LocalOptionConstant.PUB_UPDT :
					{
						return publishingUpdate(aaData);
					}
				case LocalOptionConstant.REVISE_PUB :
					{
						return revisePub(aaData);
					}
				case LocalOptionConstant.PROCESS_RSPS_UPDT :
					{
						return updateRSPS(aaData);
					}
				case LocalOptionConstant.GET_SYSUPDATE_LIST :
					{
						return getSysUpdtList(aaData);
					}
					// defect 10701 
				case LocalOptionConstant.GET_BATCH_RPT_MGMT :
					{
						return getBatchRptMgmt(aaData);
					}
				case LocalOptionConstant.UPDATE_BATCH_RPT_MGMT :
					{
						return updBatchRptMgmt(aaData);
					}

					// end defect 10701 
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
	 * Processes the Login into the application.
	 * 
	 * @param  aaData Object 
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object processLogin(Object aaData) throws RTSException
	{
		LogonData laLogonData = (LogonData) aaData;
		SecurityData laSecurityData = null;
		Security laSecurity = null;
		DatabaseAccess laDBA = null;
		RTSDate laRTSDate = null;

		if (laLogonData == null)
		{
			throw new RTSException(
				RTSException.JAVA_ERROR,
				RTS_JAVA_EX,
				JAVA_ERROR);
		}
		try
		{
			// Set up SecurityData object to query DB2 Security table
			laSecurityData = new SecurityData();
			laSecurityData.setUserName(
				laLogonData.getUserName().trim().toUpperCase());
			laSecurityData.setOfcIssuanceNo(
				laLogonData.getOfcIssuanceNo());
			laSecurityData.setSubstaId(laLogonData.getSubstaId());

			laDBA = new DatabaseAccess();
			laSecurity = new Security(laDBA);

			laDBA.beginTransaction();

			// defect 8849
			// If we get a stale connection on the first select, 
			// try it one more time.
			Vector lvSecurity = new Vector();

			// Allow for one retry if we get a StaleConnection
			for (int i = 0; i < 2; i++)
			{
				try
				{
					lvSecurity = laSecurity.qrySecurity(laSecurityData);
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
						laSecurity = new Security(laDBA);
						laDBA.beginTransaction();
					}
					else
					{
						throw aeRTSEx;
					}
				}

			}
			// end defect 8849

			laRTSDate = new RTSDate();

			if (lvSecurity.size() == 0 || lvSecurity.size() > 1)
			{
				// defect 10103
				laLogonData.setReturnCode(
					LocalOptionConstant.LOGIN_FAIL);
				// end defect 10103
			}
			else
			{
				//  Get aSecurity object returned from the DB2 query 
				laSecurityData = (SecurityData) lvSecurity.elementAt(0);
				laLogonData.setReturnCode(
					LocalOptionConstant.LOGIN_SUCCESS);
				laLogonData.setSecurityData(laSecurityData);
			}

			int liReturnCode = laLogonData.getReturnCode();

			// defect 10103
			if ((liReturnCode == LocalOptionConstant.LOGIN_SUCCESS)
				|| (liReturnCode == LocalOptionConstant.LOGIN_FAIL))
			{
				// end defect 10103
				//  Log logon attempt to LOG_FUNC_TRANS table
				LogonFunctionTransaction laLogonFunctionTransaction =
					new LogonFunctionTransaction(laDBA);
				laLogonFunctionTransaction.setOfcIssuanceNo(
					laLogonData.getOfcIssuanceNo());
				laLogonFunctionTransaction.setSubstaId(
					laLogonData.getSubstaId());
				laLogonFunctionTransaction.setWsId(
					laLogonData.getWsId());
				laLogonFunctionTransaction.setSysDate(
					laRTSDate.getYYYYMMDDDate());
				laLogonFunctionTransaction.setSysTime(
					laRTSDate.get24HrTime());
				if (laLogonData.getSecurityData() == null
					|| laLogonData.getSecurityData().getEmpId() == null)
				{
					laLogonFunctionTransaction.setEmpId(MISSING);
				}
				else
				{
					laLogonFunctionTransaction.setEmpId(
						laLogonData.getSecurityData().getEmpId());
				}

				// Set SuccessfulIndi on Success 
				if (liReturnCode == LocalOptionConstant.LOGIN_SUCCESS)
				{
					laLogonFunctionTransaction.setSuccessfulIndi(1);
				}

				laLogonFunctionTransaction.setTransPostedLANIndi(1);
				laLogonFunctionTransaction.setTransPostedMfIndi(0);

				// defect 8959
				laLogonFunctionTransaction.setMfVersionNo(
					SystemProperty.getMainFrameVersion());
				// end defect 8959

				try
				{
					laLogonFunctionTransaction
						.insLogonFunctionTransaction(
						laLogonFunctionTransaction);
				}
				catch (RTSException aeRTSEx)
				{
					laDBA.endTransaction(DatabaseAccess.ROLLBACK);
					throw aeRTSEx;
				}
				laDBA.endTransaction(DatabaseAccess.COMMIT);
			}
			else
			{
				laDBA.endTransaction(DatabaseAccess.COMMIT);
			}

			laRTSDate = new RTSDate();
			laRTSDate =
				laRTSDate.getTimeZoneAdjustedDate(
					laLogonData.getTimeZone());

			laLogonData.setMainframeTime(
				String.valueOf(laRTSDate.get24HrTime()));

			String lsMFDate =
				Integer.toString(laRTSDate.getYYYYMMDDDate());

			laLogonData.setMainframeDate(
				lsMFDate.substring(4, 6)
					+ CommonConstant.STR_DASH
					+ lsMFDate.substring(6, 8)
					+ CommonConstant.STR_DASH
					+ lsMFDate.substring(0, 4));
			return laLogonData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		catch (NullPointerException aeNPE)
		{
			// catch null pointers and convert them to rtsexceptions
			throw new RTSException(RTSException.JAVA_ERROR, aeNPE);
		}
		catch (Exception aeEx)
		{
			// catch exceptions and convert them to rtsexceptions
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		finally
		{
			laDBA = null;
		}
	}

	/**
	 * Query from RTS_SUBSTA_SUBSCR for Local Options "Update Publishing"
	 *  
	 * @param  aaData Object 
	 * @return Object
	 * @throws RTSException  
	 */
	private Object publishingUpdate(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		GeneralSearchData laGSData = (GeneralSearchData) aaData;
		try
		{
			int liOfcIssuanceNo = laGSData.getIntKey1();

			// UOW #1 BEGIN  
			laDBA.beginTransaction();
			// Select from RTS_SUBSTA_SUBSCR 
			LocalOptionsReportsSQL laLOReportsSQL =
				new LocalOptionsReportsSQL(laDBA);
			Vector lvResults =
				laLOReportsSQL.qryPublishingReport(liOfcIssuanceNo);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			return lvResults;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Revise Credit Card Fee 
	 * 
	 * @param  aaData Object 
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object reviseCreditUpdate(Object aaData)
		throws com.txdot.isd.rts.services.exception.RTSException
	{
		Map laMap = (Map) aaData;
		Vector lvVector =
			(Vector) laMap.get(CreditCardFeesConstants.DATA);
		CreditCardFeeData laCreditCardFeeData =
			(CreditCardFeeData) laMap.get(
				CreditCardFeesConstants.SELECTED_RECORD);

		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			// UOW #1 BEGIN 
			laDBA.beginTransaction();

			// Update RTS_CRDT_CARD_FEE 
			CreditCardFee laCreditCardFee = new CreditCardFee(laDBA);
			laCreditCardFee.updCreditCardFee(laCreditCardFeeData);

			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laCreditCardFeeData.getOfcIssuanceNo(),
				laCreditCardFeeData.getSubstaId(),
				LocalOptionConstant.RTS_CRDT_CARD_FEE,
				laDBA);

			// Insert into RTS_ADMIN_LOG 
			insAdminLog(
				(AdministrationLogData) laMap.get(
					CreditCardFeesConstants.ADMIN),
				laDBA);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			return lvVector;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Revise dealer with new information based on new id
	 * 
	 * @param  aaData Object 
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object reviseDlr(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();

			// UOW #1 BEGIN 	
			// defect 10112 
			// Update RTS_DEALERS 
			Dealer laDealer = new Dealer(laDBA);
			Vector lvVector = (Vector) aaData;
			DealerData laDealerData = (DealerData) lvVector.get(0);
			laDealer.updDealer(laDealerData);

			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laDealerData.getOfcIssuanceNo(),
				laDealerData.getSubstaId(),
				LocalOptionConstant.RTS_DEALERS,
				laDBA);

			// Insert into RTS_ADMIN_LOG
			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Revise a payment account in the aaDatabase.
	 * 
	 * @param  aaData Object PaymentAccountData
	 * @return Object
	 * @throws RTSException 
	 */
	private Object revisePaymentAccount(Object aaData)
		throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();

		try
		{
			Vector lvVector = (Vector) aaData;

			// UOW #1 BEGIN
			laDBA.beginTransaction();
			// Update RTS_PYMNT_ACCT 
			PaymentAccount laPaymentAccount = new PaymentAccount(laDBA);
			PaymentAccountData laPaymentAccountData =
				(PaymentAccountData) lvVector.get(0);
			laPaymentAccount.updPaymentAccount(laPaymentAccountData);

			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laPaymentAccountData.getOfcIssuanceNo(),
				laPaymentAccountData.getSubstaId(),
				LocalOptionConstant.RTS_PYMNT_ACCT,
				laDBA);

			// Insert into RTS_ADMIN_LOG
			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Revise a publishing report data in the database
	 * 
	 * @param  aaData Object  
	 * @return Object
	 * @throws RTSException 
	 */
	private Object revisePub(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvVector = (Vector) aaData;
		PublishingReportData laPubData =
			(PublishingReportData) lvVector.get(0);
		try
		{
			// UOW #1 BEGIN 
			laDBA.beginTransaction();

			// Update RTS_SUBSTA_SUBSCR
			SubstationSubscription laSubstationSubscription =
				new SubstationSubscription(laDBA);
			laSubstationSubscription.updSubstationSubscription(
				(SubstationSubscriptionData) laPubData);

			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laPubData.getOfcIssuanceNo(),
				laPubData.getSubstaId(),
				LocalOptionConstant.RTS_SUBSTA_SUBSCR,
				laDBA);

			//	Insert into RTS_ADMIN_LOG
			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Revise the Lein holder information in the aaDatabase
	 *  
	 * @param  aaData Object  
	 * @return Object
	 * @throws RTSException 
	 */
	private Object revLien(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvVector = (Vector) aaData;
		LienholderData laLienholderData =
			(LienholderData) lvVector.get(0);

		try
		{
			// UOW #1 BEGIN 
			laDBA.beginTransaction();

			// Update RTS_LIENHOLDERS 
			Lienholder laLienHolders = new Lienholder(laDBA);
			laLienHolders.updLienholder(laLienholderData);

			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laLienholderData.getOfcIssuanceNo(),
				laLienholderData.getSubstaId(),
				LocalOptionConstant.RTS_LIENHOLDERS,
				laDBA);

			// Insert into RTS_ADMIN_LOG
			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Revise the subcontractor information in the aaDatabase
	 * 
	 * @param  aaData Object  
	 * @return Object
	 * @throws RTSException 
	 */
	private Object revSubcon(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvVector = (Vector) aaData;
		SubcontractorData laSubcontractorData =
			(SubcontractorData) lvVector.get(0);

		try
		{
			// UOW #1 BEGIN 
			laDBA.beginTransaction();

			// Update RTS_SUBCON
			Subcontractor laSubcontractor = new Subcontractor(laDBA);
			laSubcontractor.updSubcontractor(laSubcontractorData);

			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laSubcontractorData.getOfcIssuanceNo(),
				laSubcontractorData.getSubstaId(),
				LocalOptionConstant.RTS_SUBCON,
				laDBA);

			// Insert into RTS_ADMIN_LOG
			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END  
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * This method updates Misc for ServerPlusIndi, Supervisor Override 
	 * 
	 * @param  aaData Object  
	 * @return Object
	 * @throws RTSException 
	 */
	private Object updtMisc(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvVector = (Vector) aaData;
		MiscellaneousData laMiscData =
			(MiscellaneousData) lvVector.elementAt(
				CommonConstant.ELEMENT_0);

		try
		{
			// UOW #1 BEGIN 
			laDBA.beginTransaction();
			Miscellaneous laMisc = new Miscellaneous(laDBA);
			laMisc.updMiscellaneous(laMiscData);

			updateAdminCache(
				laMiscData.getOfcIssuanceNo(),
				laMiscData.getSubstaId(),
				LocalOptionConstant.RTS_MISC,
				laDBA);

			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * This method is used to get Supervisor Override Code
	 * 
	 * @param  aaData Vector  
	 * @return Object
	 * @throws RTSException 
	 */
	private Object supervisorLookup(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		Map laMap = (Map) aaData;
		int liOfcIssuanceNo =
			((Integer) laMap.get(AccountingConstant.OFC)).intValue();
		int liSubStaId =
			((Integer) laMap.get(AccountingConstant.SUB)).intValue();
		MiscellaneousData laMiscData = new MiscellaneousData();
		laMiscData.setOfcIssuanceNo(liOfcIssuanceNo);
		laMiscData.setSubstaId(liSubStaId);

		try
		{

			Log.write(Log.APPLICATION, this, START_DB_CALL_MISC);

			// UOW #1 BEGIN 
			// Select from RTS_MISC 
			laDBA.beginTransaction();
			Miscellaneous laMisc = new Miscellaneous(laDBA);
			String lsSupervisorOverride =
				CommonConstant.STR_SPACE_EMPTY;
			Vector laResultSet = laMisc.qryMiscellaneous(laMiscData);
			if (laResultSet != null && laResultSet.size() > 0)
			{
				laMiscData = (MiscellaneousData) laResultSet.get(0);
				if (laMiscData != null)
				{
					lsSupervisorOverride = laMiscData.getSupvOvrideCd();
				}
			}
			Log.write(Log.APPLICATION, this, SUCCESS_DB_CALL_MISC);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			return lsSupervisorOverride;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.APPLICATION, this, FAILED_DB_CALL_MISC);
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Update Admin Cache 
	 *
	 * @param aiOfcIssuanceNo
	 * @param aiSubstaId 
	 * @param asCacheTblName
	 * @throws RTSException 
	 */
	private void updateAdminCache(
		int aiOfcIssuanceNo,
		int aiSubstaId,
		String asCacheTblName,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		AdministrationCacheTable laAdminCacheTbl =
			new AdministrationCacheTable(aaDBA);
		laAdminCacheTbl.updAdminCache(
			aiOfcIssuanceNo,
			aiSubstaId,
			asCacheTblName);
	}

	/**
	 * Get Batch Report Management
	 * 
	 * @param  aaData Object 
	 * @throws RTSException 
	 */
	private Object getBatchRptMgmt(Object aaData)
		throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvReturn = new Vector();
		try
		{
			laDBA.beginTransaction();
			BatchReportManagementData  laData = 
			(BatchReportManagementData) aaData; 
			
			// UOW #1 BEGIN 	
			// Query RTS_BATCH_RPT_MGMT 
			BatchReportManagement laBatchRptMgmtSQL =
				new BatchReportManagement(laDBA);
			lvReturn =
				laBatchRptMgmtSQL.qryBatchReportManagement(laData);
			// UOW #1 END
			// defect 11180
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// end defect 11180
			return lvReturn;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Update Batch Report Management
	 * 
	 * @param  aaData Object 
	 * @throws RTSException 
	 */
	private Object updBatchRptMgmt(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();

			// UOW #1 BEGIN 	
			// Update RTS_BATCH_RPT_MGMT 
			BatchReportManagement laBatchRptMgmtSQL =
				new BatchReportManagement(laDBA);
			Vector lvVector = (Vector) aaData;
			BatchReportManagementData laBatchRptMgmtData =
				(BatchReportManagementData) lvVector.get(0);
			laBatchRptMgmtSQL.updBatchReportManagement(
				laBatchRptMgmtData);

			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laBatchRptMgmtData.getOfcIssuanceNo(),
				laBatchRptMgmtData.getSubstaId(),
				LocalOptionConstant.RTS_BATCH_RPT_MGMT,
				laDBA);

			// Insert into RTS_ADMIN_LOG
			insAdminLog((AdministrationLogData) lvVector.get(1), laDBA);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Update Credit Card Fees
	 *
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object updateCreditDB(Object aaData) throws RTSException
	{
		Map laMap = (Map) aaData;

		// ADD 
		if (laMap
			.get(CreditCardFeesConstants.TYPE)
			.equals(CreditCardFeesConstants.ADD))
		{
			return addCreditUpdate(aaData);
		}
		// DELETE 
		else if (
			laMap.get(CreditCardFeesConstants.TYPE).equals(
				CreditCardFeesConstants.DELETE))
		{
			return delCreditUpdate(aaData);
		}
		// REVISE 
		else
		{
			return reviseCreditUpdate(aaData);
		}
	}

	/**
	 * Process the RSPS Updates
	 * 
	 * @param  aaData (Vector)
	 * @return Object (Vector)
	 * @throws RTSException
	 */
	private Object updateRSPS(Object aaData) throws RTSException
	{
		// TODO Break into smaller methods (KPH)  
		Vector lvReturn = new Vector();
		DatabaseAccess laDBA = null;
		boolean lbSuccessful = false;

		if (aaData != null)
		{
			try
			{
				Vector lvProcessVector = (Vector) aaData;
				laDBA = new DatabaseAccess();

				// UOW #1 BEGIN 
				laDBA.beginTransaction();

				for (int i = 0; i < lvProcessVector.size(); i++)
				{
					RSPSUpdData laRSPSData =
						(RSPSUpdData) lvProcessVector.elementAt(i);
					RSPSWsStatusData laRSPSWsSts =
						(RSPSWsStatusData) laRSPSData
							.getRSPSWsStatusData();

					// defect 8379		
					// Vector lvUpdatesApplied =
					//	(Vector) laRSPSData.getSysUpdates();
					// end defect 8379

					// defect 8721
					// Update only on 1st element 
					if (i == 0)
					{
						// Update RTS_ADMIN_CACHE
						updateAdminCache(
							laRSPSWsSts.getOfcIssuanceNo(),
							0,
							LocalOptionConstant.RTS_RSPS_WS_STATUS,
							laDBA);
					}
					// end defect 8721

					// by pass record if location does not exist
					if (laRSPSWsSts
						.getLocIdCd()
						.equalsIgnoreCase(CommonConstant.POS_DTA))
					{
						DealerData laDealerData = new DealerData();
						laDealerData.setOfcIssuanceNo(
							laRSPSWsSts.getOfcIssuanceNo());
						laDealerData.setSubstaId(0);
						laDealerData.setId(laRSPSWsSts.getLocId());

						Dealer laDSQL = new Dealer(laDBA);
						Vector lvDealer =
							laDSQL.qryDealer(laDealerData);
						if (lvDealer.size() < 1)
						{
							continue;
						}
					}
					else
					{
						SubcontractorData laSubconData =
							new SubcontractorData();
						laSubconData.setOfcIssuanceNo(
							laRSPSWsSts.getOfcIssuanceNo());
						laSubconData.setSubstaId(0);

						// defect 10161 
						laSubconData.setId(laRSPSWsSts.getLocId());
						// end defect 10161 

						Subcontractor laSSQL = new Subcontractor(laDBA);
						Vector lvSubcons =
							laSSQL.qrySubcontractor(laSubconData);
						if (lvSubcons.size() < 1)
						{
							continue;
						}
					}

					// setup the processing time
					//Qry for correct time zone
					int liOfc = laRSPSWsSts.getOfcIssuanceNo();
					int liSubStaId = 0;

					// defect 10427 
					//Retrieve time zone
					//CacheManagerServerBusiness laCacheManagerServerBusiness =
					//	new CacheManagerServerBusiness();

					//Write to log that we are making a DB call
					//Log.write(
					//	Log.APPLICATION,
					//	this,
					//	TIME_ZONE_CALL_BEGIN);

					//	String lsTimeZone =
					//		laCacheManagerServerBusiness.getOfficeTimeZone(
					//			liOfc,
					//			liSubStaId);

					String lsTimeZone =
						OfficeTimeZoneCache.getTimeZone(liOfc);
					// end defect 10427

					// Qry for database time to get current time, 
					//  returned in Central time
					Miscellaneous laMiscellaneous =
						new Miscellaneous(laDBA);

					// Write to log that we are making a DB call
					Log.write(
						Log.APPLICATION,
						this,
						QRY_TIMESTAMP_BEGIN);

					String lsDatabaseDate =
						((RTSDate) laMiscellaneous
							.qryCurrentTimestamp(liOfc, liSubStaId))
							.getDB2Date();
					Log.write(Log.APPLICATION, this, QRY_TIMESTAMP_END);

					//Adjust current time based on time zone
					RTSDate laNewTimeStamp =
						new RTSDate(
							lsDatabaseDate).getTimeZoneAdjustedDate(
							lsTimeZone);

					RSPSWsStatus laRSPSWsSql = new RSPSWsStatus(laDBA);
					Vector lvRSPSWsData =
						laRSPSWsSql.qryRSPSWsStatus(laRSPSWsSts);

					if (lvRSPSWsData.size() < 1)
					{
						// set all the time fields 
						// all other data should already be set.
						laRSPSWsSts.setRSPSVersionTimeStmp(
							laNewTimeStamp);
						laRSPSWsSts.setRSPSJarSizeTimeStmp(
							laNewTimeStamp);
						laRSPSWsSts.setRSPSJarDateTimeStmp(
							laNewTimeStamp);
						laRSPSWsSts.setDbVersionTimeStmp(
							laNewTimeStamp);
						laRSPSWsSts.setDATLvlTimeStmp(laNewTimeStamp);
						laRSPSWsSts.setScanEngineTimeStmp(
							laNewTimeStamp);
						laRSPSWsSts.setLastProcsdTimeStmp(
							laNewTimeStamp);
						// insert a new row
						laRSPSWsSql.insRSPSWsStatus(laRSPSWsSts);
					}
					else
					{
						// set the appropriate fields for update
						RSPSWsStatusData laRSPSWsUpdateRow =
							(RSPSWsStatusData) lvRSPSWsData.elementAt(
								0);
						laRSPSWsUpdateRow.setLastProcsdTimeStmp(
							laNewTimeStamp);
						laRSPSWsUpdateRow.setLastProcsdHostName(
							laRSPSWsSts.getLastProcsdHostName());
						laRSPSWsUpdateRow.setLastProcsdUserName(
							laRSPSWsSts.getLastProcsdUserName());

						// defect 8155
						// Added this - the thought before was that this 
						// would not change so no need to update it 
						// everytime.
						laRSPSWsUpdateRow.setIPAddr(
							laRSPSWsSts.getIPAddr());
						// end defect 8155

						// check RSPSVersion
						if (laRSPSWsSts.getRSPSVersion() != null
							&& (laRSPSWsUpdateRow.getRSPSVersion() == null
								|| !laRSPSWsUpdateRow
									.getRSPSVersion()
									.trim()
									.equals(
									laRSPSWsSts
										.getRSPSVersion()
										.trim())))
						{
							laRSPSWsUpdateRow.setRSPSVersion(
								laRSPSWsSts.getRSPSVersion());
							laRSPSWsUpdateRow.setRSPSVersionTimeStmp(
								laNewTimeStamp);
						}

						// check RSPSJarSize
						if (laRSPSWsSts.getRSPSJarSize() != null
							&& (laRSPSWsUpdateRow.getRSPSJarSize() == null
								|| !laRSPSWsUpdateRow
									.getRSPSJarSize()
									.trim()
									.equals(
									laRSPSWsSts
										.getRSPSJarSize()
										.trim())))
						{
							laRSPSWsUpdateRow.setRSPSJarSize(
								laRSPSWsSts.getRSPSJarSize());
							laRSPSWsUpdateRow.setRSPSJarSizeTimeStmp(
								laNewTimeStamp);
						}

						// check RSPSJarDate
						if (laRSPSWsSts.getRSPSJarDate() != null
							&& (laRSPSWsUpdateRow.getRSPSJarDate() == null
								|| laRSPSWsUpdateRow
									.getRSPSJarDate()
									.getYear()
									!= laRSPSWsSts
										.getRSPSJarDate()
										.getYear()
								|| laRSPSWsUpdateRow
									.getRSPSJarDate()
									.getMonth()
									!= laRSPSWsSts
										.getRSPSJarDate()
										.getMonth()
								|| laRSPSWsUpdateRow
									.getRSPSJarDate()
									.getDate()
									!= laRSPSWsSts
										.getRSPSJarDate()
										.getDate()))
						{
							laRSPSWsUpdateRow.setRSPSJarDate(
								laRSPSWsSts.getRSPSJarDate());
							laRSPSWsUpdateRow.setRSPSJarDateTimeStmp(
								laNewTimeStamp);
						}

						// check DbVersion
						// defect 8314
						// Changed && to ||
						//if (laRSPSWsSts.getDbVersion() != null
						//	&& (laRSPSWsUpdateRow.getDbVersion() == null
						//		&& !laRSPSWsUpdateRow
						//			.getDbVersion()
						//			.trim()
						//			.equals(
						//			laRSPSWsSts
						//				.getDbVersion()
						//				.trim())))
						if (laRSPSWsSts.getDbVersion() != null
							&& (laRSPSWsUpdateRow.getDbVersion() == null
								|| !laRSPSWsUpdateRow
									.getDbVersion()
									.trim()
									.equals(
									laRSPSWsSts
										.getDbVersion()
										.trim())))
							// end defect 8314
						{
							laRSPSWsUpdateRow.setDbVersion(
								laRSPSWsSts.getDbVersion());
							laRSPSWsUpdateRow.setDbVersionTimeStmp(
								laNewTimeStamp);
						}
						// defect 8381
						// We want to start collecting the LogDate
						// check LogDate
						if (laRSPSWsSts.getLastRSPSProcsdTimeStmp()
							!= null
							&& (laRSPSWsUpdateRow
								.getLastRSPSProcsdTimeStmp()
								== null
								|| laRSPSWsUpdateRow
									.getLastRSPSProcsdTimeStmp()
									.getYear()
									!= laRSPSWsSts
										.getLastRSPSProcsdTimeStmp()
										.getYear()
								|| laRSPSWsUpdateRow
									.getLastRSPSProcsdTimeStmp()
									.getMonth()
									!= laRSPSWsSts
										.getLastRSPSProcsdTimeStmp()
										.getMonth()
								|| laRSPSWsUpdateRow
									.getLastRSPSProcsdTimeStmp()
									.getDate()
									!= laRSPSWsSts
										.getLastRSPSProcsdTimeStmp()
										.getDate()))
						{
							laRSPSWsUpdateRow
								.setLastRSPSProcsdTimeStmp(
								laRSPSWsSts
									.getLastRSPSProcsdTimeStmp());
						}
						// end defect 8381

						// check DatLvl
						// defect 8382
						// Datlvl was not being updated
						//if (laRSPSWsSts.getDATLvl() < 1
						if (laRSPSWsSts.getDATLvl() > 1
							&& laRSPSWsUpdateRow.getDATLvl()
								!= (laRSPSWsSts.getDATLvl()))
							// end defect 8382
						{
							laRSPSWsUpdateRow.setDATLvl(
								laRSPSWsSts.getDATLvl());
							laRSPSWsUpdateRow.setDATLvlTimeStmp(
								laNewTimeStamp);
						}

						// check scan engine
						if (laRSPSWsSts.getScanEngine() != null
							&& (laRSPSWsUpdateRow.getScanEngine() == null
								|| !laRSPSWsUpdateRow
									.getScanEngine()
									.equalsIgnoreCase(
									laRSPSWsSts.getScanEngine())))
						{
							laRSPSWsUpdateRow.setScanEngine(
								laRSPSWsSts.getScanEngine());
							laRSPSWsUpdateRow.setScanEngineTimeStmp(
								laNewTimeStamp);
						}

						// make sure the timestamps are all populated 
						// even if not used
						if (laRSPSWsUpdateRow.getRSPSVersionTimeStmp()
							== null)
						{
							laRSPSWsUpdateRow.setRSPSVersionTimeStmp(
								laNewTimeStamp);
						}
						if (laRSPSWsUpdateRow.getRSPSJarSizeTimeStmp()
							== null)
						{
							laRSPSWsUpdateRow.setRSPSJarSizeTimeStmp(
								laNewTimeStamp);
						}
						if (laRSPSWsUpdateRow.getRSPSJarDateTimeStmp()
							== null)
						{
							laRSPSWsUpdateRow.setRSPSJarDateTimeStmp(
								laNewTimeStamp);
						}
						if (laRSPSWsUpdateRow.getDbVersionTimeStmp()
							== null)
						{
							laRSPSWsUpdateRow.setDbVersionTimeStmp(
								laNewTimeStamp);
						}
						if (laRSPSWsUpdateRow.getDATLvlTimeStmp()
							== null)
						{
							laRSPSWsUpdateRow.setDATLvlTimeStmp(
								laNewTimeStamp);
						}
						if (laRSPSWsUpdateRow.getScanEngineTimeStmp()
							== null)
						{
							laRSPSWsSts.setScanEngineTimeStmp(
								laNewTimeStamp);
						}

						// update the row
						laRSPSWsSql.updRSPSWsStatus(laRSPSWsUpdateRow);
					}

					RSPSSysUpdateHistoryData laRSUH =
						new RSPSSysUpdateHistoryData();
					laRSUH.setOfcIssuanceNo(
						laRSPSData
							.getRSPSWsStatusData()
							.getOfcIssuanceNo());
					laRSUH.setRSPSId(
						laRSPSData.getRSPSWsStatusData().getRSPSId());

					// get what was already in the database
					RSPSSysUpdateHistory laRSPSSysUpdateHistory =
						new RSPSSysUpdateHistory(laDBA);
					Vector lvOldHistory =
						laRSPSSysUpdateHistory.qryRSPSSysWsUpdateHstry(
							laRSUH);
					Vector lvNewHistory = laRSPSData.getSysUpdates();
					boolean lbExists = false;

					for (int j = 0; j < lvNewHistory.size(); j++)
					{
						String laNewHistoryString =
							(String) lvNewHistory.elementAt(j);
						// check to see if it was already processed  
						lbExists = false;
						for (int k = 0; k < lvOldHistory.size(); k++)
						{
							RSPSSysUpdateHistoryData laOldHistData =
								(
									RSPSSysUpdateHistoryData) lvOldHistory
										.elementAt(
									k);
							if (laOldHistData
								.getSysUpdate()
								.trim()
								.equalsIgnoreCase(
									laNewHistoryString.trim()))
							{
								lbExists = true;
								break;
							}
						}

						// insert the new row.
						if (!lbExists)
						{
							RSPSSysUpdateHistoryData laNewHistory =
								new RSPSSysUpdateHistoryData();
							laNewHistory.setOfcIssuanceNo(
								laRSPSData
									.getRSPSWsStatusData()
									.getOfcIssuanceNo());
							laNewHistory.setRSPSId(
								laRSPSData
									.getRSPSWsStatusData()
									.getRSPSId());
							laNewHistory.setSysUpdate(
								laNewHistoryString);
							laNewHistory.setAppliedTimeStamp(
								laNewTimeStamp);
							// defect 7671
							laNewHistory.setDeleteIndi(0);
							// end defect 7671
							laRSPSSysUpdateHistory
								.insRSPSWsSysupdtHstry(
								laNewHistory);
						}
					}

					// defect 7671
					// check to see if any update history needs to be 
					// deleted

					// create boolean to determine of delete can be done
					boolean lbDeleteHistory = false;
					for (int j = 0; j < lvOldHistory.size(); j++)
					{
						// boolean is set to true.
						// if there is a match, turn the boolean to 
						// false to not delete.
						lbDeleteHistory = true;
						RSPSSysUpdateHistoryData laOldHistData =
							(
								RSPSSysUpdateHistoryData) lvOldHistory
									.elementAt(
								j);
						String lsHistoryUpdate =
							laOldHistData.getSysUpdate();
						for (int k = 0; k < lvNewHistory.size(); k++)
						{
							// check to see if we should keep this 
							// history entry
							if (lsHistoryUpdate
								.equalsIgnoreCase(
									(String) lvNewHistory.elementAt(
										k)))
							{
								lbDeleteHistory = false;
								break;
							}
						}

						// if delete is on, delete this entry
						// just setting the delteindi to 1
						if (lbDeleteHistory)
						{
							RSPSSysUpdateHistoryData lDeleteHistory =
								new RSPSSysUpdateHistoryData();
							lDeleteHistory.setOfcIssuanceNo(
								laRSPSData
									.getRSPSWsStatusData()
									.getOfcIssuanceNo());
							lDeleteHistory.setRSPSId(
								laRSPSData
									.getRSPSWsStatusData()
									.getRSPSId());
							lDeleteHistory.setSysUpdate(
								lsHistoryUpdate);
							lDeleteHistory.setAppliedTimeStamp(
								laNewTimeStamp);
							lDeleteHistory.setDeleteIndi(1);
							laRSPSSysUpdateHistory
								.updRSPSWsSysupdtHstry(
								lDeleteHistory);
						}

					}
					// end defect 7671
				}

				// we finished successfully.  no exceptions. 
				lbSuccessful = true;
				return lvReturn;
			}
			catch (RTSException aeRTSEx)
			{
				throw aeRTSEx;
			}
			finally
			{
				if (lbSuccessful)
				{
					laDBA.endTransaction(DatabaseAccess.COMMIT);
				}
				else
				{
					laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				}
			}
		}
		else
		{
			return aaData;
		}
	}

	/**
	 * Update Employee security info
	 * 
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object updtEmpSec(Object aaData) throws RTSException
	{
		Object laReturnData = null;
		Vector lvVector = (Vector) aaData;
		SecurityData laSecurityData = (SecurityData) lvVector.get(0);
		laReturnData = (Object) laSecurityData;

		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			Security laSecurity = new Security(laDBA);
			SecurityLog laSecurityLog = new SecurityLog(laDBA);
			// Set the ChngTimestmp for search.
			// commit delete before attempting insert.
			// make sure EmpId is not a dup.
			// Do not check if it is null.
			if (laSecurityData.getUserName() != null
				&& laSecurityData.getUserName().length() > 0)
			{
				SecurityData laChkForDupEmpId =
					(SecurityData) UtilityMethods.copy(laSecurityData);
				laChkForDupEmpId.setUserName(null);
				laChkForDupEmpId.setChngTimestmp(
					new RTSDate(1900, 0, 0));
				Vector lvSecChkDupEmpId =
					laSecurity.qrySecurity(laChkForDupEmpId);
				if (lvSecChkDupEmpId.size() > 0)
				{
					// check to see if it is a deleted row
					SecurityData laChkForDelete =
						(SecurityData) lvSecChkDupEmpId.elementAt(0);
					if (laChkForDelete.getDeleteIndi() == 0)
					{
						// check to see if this is a different user
						if (!(laChkForDelete.getOfcIssuanceNo()
							== laSecurityData.getOfcIssuanceNo()
							&& laChkForDelete.getSubstaId()
								== laSecurityData.getSubstaId()
							&& laChkForDelete
								.getUserName()
								.trim()
								.equalsIgnoreCase(
								laSecurityData.getUserName().trim())))
						{
							// throw an invalid userid error if this is
							// not a deleted employee
							// and it is a different user
							throw new RTSException(772);
						}
					}
					else
					{
						// delete the row for real by empid 
						//laSecurity.delSecurity(laChkForDupEmpId, true);
						laSecurity.delSecurity(laChkForDupEmpId, false);
						laDBA.endTransaction(DatabaseAccess.COMMIT);
						// UOW #1 END 
						// UOW #2 BEGIN 
						laDBA.beginTransaction();
					}
				}
			}
			int liSucc = laSecurity.updSecurity(laSecurityData);
			if (liSucc < 1)
			{
				laSecurity.insSecurity(laSecurityData);
			}
			SecurityLogData laSecurityLogData =
				(SecurityLogData) lvVector.get(1);
			laSecurityLog.insSecurityLog(laSecurityLogData);
			// defect 8721 
			// Update RTS_ADMIN_CACHE
			updateAdminCache(
				laSecurityData.getOfcIssuanceNo(),
				laSecurityData.getSubstaId(),
				LocalOptionConstant.RTS_SECURITY,
				laDBA);
			// end defect 8721 
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1/2 END 
			return laReturnData;
		}
		catch (RTSException aeRTSEx)
		{
			// add RollBack
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}
}
