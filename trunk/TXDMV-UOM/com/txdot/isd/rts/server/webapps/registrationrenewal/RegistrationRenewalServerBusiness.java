package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.common.Fees;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.EmailUtil;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.CommonConstants;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.MessageConstants;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.RegistrationRenewalConstants;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetData;
import com.txdot.isd.rts.server.db.InternetTransaction;
import com.txdot.isd.rts.server.db.InternetTransactionDeleteLog;
import com.txdot.isd.rts.server.db.OfficeIds;
import com.txdot.isd.rts.server.general.CacheManagerServerBusiness;
import com.txdot.isd.rts.server.systemcontrolbatch.Purge;
import com
	.txdot
	.isd
	.rts
	.server
	.webapps
	.common
	.business
	.CommonEligibility;
import com.txdot.isd.rts.server.webapps.common.business.SearchVehicle;
import com.txdot.isd.rts.server.webapps.common.business.VehicleBase;
import com.txdot.isd.rts.server.webapps.common.business.VehicleDesc;
import com.txdot.isd.rts.server.webapps.util.Log;
import com.txdot.isd.rts.server.webapps.util.MQLog;

/*
 *
 * RegistrationRenewalServerBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * ------------	-----------	--------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Clifford		09/25/2002	PCR44--insurance not required, added get
 *                          InsuranceRequired()   
 * Clifford		01/21/2003	PCR44--insurance not required, table 
 *                          driven.
 * K Harrell	07/20/2003 	Defect 6227 - Purge modifications
 *                          modified deleteDeclinedRefundApproved()
 *                          modified deleteApproved()
 *                          modified deleteUnpaid()
 *                          added deleteSummary()
 * K Harrell	10/30/2003	Altered the days to retain the approved
 *                          RTS_ITRNT_TRANS
 *							data from Purge trans to PurgeInetRenewal
 *							modified deleteApproved()
 *							defect 6659.
 * B Brown		03/01/2004	Changed doBatchPayment(obj),
 *                          to call BatchProcess.processFailed
 *                          Payments(),
 *                          passing an integer.
 *							defect 6604. Ver 5.1.6
 * Bob Brown	06/24/2004	Added log write statements when MQ objects
 *                          are rolled back and not updated to the DB.
 *							modify doRenewal(RenewalShoppingCart)
 *							defect 7168. Ver 5.2.1
 * Ray Rowehl	02/08/2005	Change import for Fees
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * Jeff S.		02/25/2005	Get code up to standards. Changed a 
 * 							non-static call to a static call.
 * 							modify delExists(), doQry(), doRenewal(),
 * 								insertRenewal(), purge(), qryCounty(), 
 * 								recordExists()
 *							defect 7889 Ver 5.2.3
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3  
 * Bob Brown	09/28/2005	TitleData needs to be null before Complete
 *                          TransData is inserted into the database for
 *							5.2.3 client processing.
 *							Also, clean up hard-coded values.
 *							add CNTYSTATUSCD, RIGHT_PAREN, LEFT_PAREN,
 *							COMMA, NOT, IN, SPACE 
 * 							modify getAndInsertData(int,
 * 							VehicleInquiryData)
 *							defect 8378 Ver 5.2.2 Fix 7
 * K Harrell	09/28/2005	Allow systemcontrolbatch to make calls 
 * 							directly to purge, eliminate need for 
 * 							deleteApproved, etc.
 * 							deprecate deleteApproved(),deleteUnpaid(),
 * 							 deleteDeclinedRefundApproved(),
 * 							 deleteSummary() 
 * 							delete purge(int,int) 
 * 							rename purge(int) to purgeSummary() 
 * 							defect 8385 Ver 5.2.3
 * K Harrell	10/06/2005	Java 1.4 Cleanup 
 * 							renamed qryCounty() to qryCounties()
 * 							defect 7889 Ver 5.2.3 
 * B Brown      01/25/2006  Present the internet user with a "Recently
 *       					Renewed" message if their vehicle on the POS
 *       					DB has a cntystatuscd = 1 (unpaid)
 *       					and the itrntpymntstatuscd is not null - 
 *       					Still trying to collect fees.
 *       					modify getAndInsertData(int, 
 * 							VehicleInquiryData)
 *                          defect 8365 Ver 5.2.2 fix 8.
 * B Brown      03/21/2006  Allow the internet user to complete a 
 * 							renewal	when their refund has been approved.
 *       					modify getAndInsertData(int, 
 * 							VehicleInquiryData)
 *                          defect 8630 Ver 5.2.2 fix 8. 
 * B Brown		04/28/2006  Add 2 testing functions for Phase 2:
 * 							MQ_TEST_QRY, and DO_PURGE
 * 							modify processData(int, int, Object)
 * 							defect 8554 Ver 5.2.3
 * Ray Rowehl	08/02/2006	Retry on StaleConnection if getting a record.
 * 							modify recordExists() 
 * 							defect 8869 Ver 5.2.4 
 * Ray Rowehl	08/03/2006	Add the client to the message appropriately.
 * 							modify recordExists() 
 * 							defect 8869 Ver 5.2.4  
 * Ray Rowehl	08/04/2006	Log the StaleConnection
 * 							modify recordExists() 
 * 							defect 8869 Ver 5.2.4     
 * K Harrell	02/01/2007	Use PlateTypeCache vs. 
 * 								RegistrationRenewalsCache
 *							prepInv() 							
 * 							defect 9085 Ver Special Plates
 * B Brown		07/16/2007	Add check for MFVehicleData.isSpclPlt() and
 * 							set setSpclPltChrgFeeIndi = 1
 *							modify getVecFees() 							
 * 							defect 9119 Ver Special Plates 
 * B Brown		07/27/2007	Return expired message when reg is expired
 *							modify getVeh() 							
 * 							defect 9107 Ver Special Plates 
 * B Brown		07/31/2007	Set the regttladlinfodata.getChrgindi = 1
 * 							for IRENEW 
 *							modify getVecFees() 							
 * 							defect 9119 Ver Special Plates  
 * B Hargrove	11/30/2007	For FRVP, we also need to check whether
 * 							proof of insurance is required. Created new
 * 							method isInsuranceRequired() in 
 * 							services.cache.CommonFeesCache. Use this
 * 							method here instead of getInsuranceRequired().
 * 							deprecated getInsuranceRequired()  
 *							modify getRenewalData() 							
 * 							defect 9469 Ver FRVP  
 * B Hargrove	04/14/2008	Changed comment location. Deleted
 * 							getInsuranceRequired() instead of deprecate. 
 * 							deleted getInsuranceRequired()  
 *							modify getRenewalData() 							
 * 							defect 9469 Ver FRVP  
 * B Hargrove	12/14/2007	Remove unused variable CNTYSTATUSCD. 
 *							delete CNTYSTATUSCD 							
 * 							defect 8871 Ver Defect POS A  
 * K Harrell	07/03/2009	modify getAndInsertData()
 * 							defect 10112 Ver Defect_POS_F
 * B Brown		11/20/2009	Code write statements to capture data 
 * 							causing NumberFormatExceptions.
 * 							modify getVeh()
 * 							defect 9495 Ver Defect_POS_H 
 * K Harrell	04/03/2010	add logic to insert into record w/ 
 * 							 CntyStatusCd = 7 from RTS_ITRNT_TRANS into
 * 							 RTS_ITRNT_TRANS_DEL_LOG prior to 
 * 							 deleting and then inserting new record.
 * 							modify delExists()
 * 							defect 10421 Ver POS_640
 * B Brown		08/12/2010	Add Catch for NumberFormatExceptions
 * 							modify getVeh()
 * 							defect 10512 Ver POS_650 
 * B Brown		10/06/2010	Add check for pending RENEW and SBRNW for
 * 							the same plate number being currently
 * 							processed
 * 							modify getVeh()
 * 							defect 10608 Ver POS_660
 * B Brown		01/18/2011	Return MaxAllowRegMos in CompleteRegRenData
 * 							so IVTRS can determine if the vehicle 
 * 							entered can pay for 2 or 3 years 
 * 							registration online. Also update fees and
 * 							regis exp Mo/Yr in completeTransactionData 
 * 							when multiple regis years are chosen. 
 * 							add updateFees(), transformVehRegFeesData()
 * 							modify getVeh(), processData()
 * 							defect 10714 Ver POS_670
 * B Brown		02/25/2011	For vehicles with a special plate, add a
 * 							check for the difference between the special 
 * 							plate exp yr/mo and vehicle exp yr/mo to
 * 							determine how many months to allow the
 * 							vehicle to register for.
 * 							defect 10714 Ver POS_670
 * K Harrell	06/10/2011	Prevent IRENEW if WRENEW complete or in 
 * 							process
 * 							modify getVeh() 
 * 							defect 10882 Ver 6.8.0 
 * B Brown		06/14/2011	Add itmqty > 1 for IRENEW's with new 
 * 							expiration date 2 or 3 years in advance.
 * 							modify updateFees(),
 * 							transformVehRegFeesData()
 * 							defect 10901 Ver POS_680	
 * S Carlin		09/07/2011	Return vehicle colors list.
 * 							defect 10985 Ver 6.8.1
 * B Brown		10/03/2011	Correct update from common fees to Complete
 * 							RegRenData. Use getMaxallowbleRegMo() instead
 * 							of getMaxMYrPeriodLngth().
 * 							Adding this code back in as it was not merged.
 * 							modify getVeh()
 * 							defect 10901 Ver POS_681.
 * S Carlin		11/01/2011	Add check to validate shopping cart with what's in
 * 							the database.  If there is a difference, update the database.
 * 							defect 10945 Ver 6.9.0
 * B Brown		11/09/2011	Make sure to allow 24 or 36 months of special 
 * 							plate renewal when plate and vehicle expiration
 * 							are in sync and it's a 1-year plate.
 * 							modify getVeh()
 * 							defect 10957 Ver POS_690
 * B Brown		11/16/2011  MVFuncTrans CustActualRegfee needs to updated
 * 							with the total fees paid for multi-year
 * 							IRENEW registration.
 * 							modify updateFees() 
 * 							defect 10912 Ver POS_690
 * S Carlin		01/11/2012  recordExists() method changed to break out of
 * 							loop if query is successful the first time
 * 							defect 11111 Ver POS_6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * RegistrationRenewalServerBusiness provides the Server Methods for 
 * Internet Registration Renewal transactions.
 * 
 * @version 6.10.0			01/11/2012
 * @author	Clifford Chen 
 * <br>Creation Date:		10/08/2001 12:22:27
 */

public class RegistrationRenewalServerBusiness
{
	/**
	* Holds the CompleteTransactionData after the fees are calculated.
	* Only getMethod is provided for this field.
	*/
	private CompleteTransactionData caCompTransData;
	private final static String NOT = "NOT";
	private final static String IN = "IN";
	private final static String LEFT_PAREN = "(";
	private final static String RIGHT_PAREN = ")";
	private final static String COMMA = ",";
	private final static String SPACE = " ";
	private final static String UNAUTHORIZED = "UnAuthorized";
	private final static String NO_SUCH_FUNCTION_ID =
		"No such functionId:";
	private final static int COMBINATION_REGCLASSCD = 10;
	// end defect 8378

	//defect 10945
	public static final String PROD_DATABASE = "P0RTSDB",
								FROM_EMAIL = "TSD-RTS-POS@txdmv.gov",
								TO_EMAIL = "TSD-RTS-POS@txdmv.gov";
	//end defect 10945

	/**
	 * RegistrationRenewalServerBusiness constructor comment.
	 */
	public RegistrationRenewalServerBusiness()
	{
		super();
	}

	/**
	 * Add the Item to Inventory.
	 * 
	 * @param avInvData Vector
	 * @param asInvItmCd String
	 * @param aiOfcIssuanceNo int
	 */
	private void addItmCdToInv(
		Vector avInvData,
		String asInvItmCd,
		int aiOfcIssuanceNo)
	{

		ProcessInventoryData laInvData = new ProcessInventoryData();
		laInvData.setOfcIssuanceNo(aiOfcIssuanceNo);
		laInvData.setItmCd(asInvItmCd);
		laInvData.setInvQty(1);
		avInvData.add(laInvData);
	}

	/**
	 * Insert into Log and then Delete 
	 * 
	 * @param aaVehBaseData VehicleBaseData
	 * @throws RTSException
	 */
	private void delExists(VehicleBaseData aaVehBaseData)
		throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// UOW #1 BEGIN
			laDBAccess.beginTransaction();

			// defect 10421 
			// Insert into Itrnt_Trans_Del_Log from Itrnt_Trans 
			//   if CntyStatusCd = 7 before deleting from Itrnt_Trans
			// (CommonConstants.DECLINED_REFUND_APPROVED = 7)   
			InternetTransactionDeleteLog laItrntTransDelLog =
				new InternetTransactionDeleteLog(laDBAccess);

			laItrntTransDelLog.insItrntTransDelLog(aaVehBaseData);
			// end defect 10421  

			// Delete from Itrnt_Trans
			InternetTransaction laItrntTrans =
				new InternetTransaction(laDBAccess);
			laItrntTrans.delItrntRenew(aaVehBaseData);

			// Delete from Itrnt_Data 
			InternetData laItrntData = new InternetData(laDBAccess);
			laItrntData.delItrntData(aaVehBaseData);

			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Do all of Batch.
	 * 
	 * @param aaData Object
	 * @return Object
	 */
	public Object doBatchAll(Object aaData)
	{
		if (!isAllowedTxOBatch(aaData))
		{
			return UNAUTHORIZED;
		}

		BatchProcess laBatch = new BatchProcess();
		boolean lbIsSuccessful = laBatch.processBatch();
		return String.valueOf(lbIsSuccessful);
	}

	/**
	 * Do the batch Email.
	 * 
	 * @param aaData Object
	 * @return Object
	 */
	public Object doBatchEmail(Object aaData)
	{
		if (!isAllowedTxOBatch(aaData))
		{
			return UNAUTHORIZED;
		}

		BatchProcess laBatch = new BatchProcess();
		boolean lbIsSuccessful = laBatch.sendEmail();
		return String.valueOf(lbIsSuccessful);
	}

	/**
	 * Do the Batch Payment.
	 * 
	 * @param aaData Object
	 * @return Object
	 */
	public Object doBatchPayment(Object aaData)
	{
		if (!isAllowedTxOBatch(aaData))
		{
			return UNAUTHORIZED;
		}

		BatchProcess laBatch = new BatchProcess();
		// defect 6604
		boolean lbSuccessful =
			laBatch.processFailedPayments(
				BatchProcess.PROCESS_FAILED_CONV_FEES);
		if (lbSuccessful)
		{
			lbSuccessful =
				laBatch.processFailedPayments(
					BatchProcess.PROCESS_FAILED_OTHER_FEES);
		}
		// end defect 6604
		return String.valueOf(lbSuccessful);
	}

	/**
	 * Do Batch Refund.
	 * 
	 * @param aaData Object
	 * @return Object
	 */
	public Object doBatchRefund(Object aaData)
	{
		if (!isAllowedTxOBatch(aaData))
		{
			return UNAUTHORIZED;
		}

		BatchProcess laBatch = new BatchProcess();
		boolean lbSuccessful = laBatch.processFailedRefunds();
		return String.valueOf(lbSuccessful);
	}

	/**
	 * Do Batch Status.
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws Exception
	 */
	public Object doBatchStatus(Object aaData) throws Exception
	{
		if (!isAllowedTxOBatch(aaData))
		{
			return UNAUTHORIZED;
		}

		BatchProcess laBatch = new BatchProcess();
		boolean lbSuccessful = false;
		try
		{
			lbSuccessful = laBatch.processStatuses();
		}
		catch (Exception aeEx)
		{
			throw aeEx;
		}
		return String.valueOf(lbSuccessful);
	}

	/**
	 * Do the Query that is passed in.
	 * 
	 * @param lsQuery Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object doQry(Object lsQuery) throws RTSException
	{
		String lsData = (String) lsQuery;
		int liSep = lsData.indexOf(";");
		String lsAuth = lsData.substring(0, liSep);
		lsData = lsData.substring(liSep + 1);
		lsData = lsData.toUpperCase();

		String lsHead =
			"<html><head><title>Query</title></head><body>"
				+ "<h3>"
				+ lsData
				+ "</h3><table width=100% border=1>";
		String lsTail = "</table></body></html>";

		String lsResult = "No result";
		if (!isAllowedTxOBatch(lsAuth))
		{
			lsResult = UNAUTHORIZED;
		}
		else if (!lsData.startsWith("SELECT"))
		{
			lsResult = "<tr><td>Not implemented</td></tr>";
		}
		else
		{
			DatabaseAccess laDBAccess = new DatabaseAccess();
			InternetTransaction laItrntTrans =
				new InternetTransaction(laDBAccess);

			try
			{
				// UOW #1 BEGIN 
				laDBAccess.beginTransaction();
				lsResult = laItrntTrans.qryRenewal(lsData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #1 END 
			}
			catch (RTSException aeRTSEx)
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				throw aeRTSEx;
			}
		}
		return lsHead + lsResult + lsTail;
	}

	/**
	 * Update all the registration renewal records (for one user 
	 * session, i.e., what is in one shopping cart) in the POS database,
	 * after the registration renewal records have been paid by the 
	 * internet user through the TexasOnline site application.
	 * 
	 * @param aaRenewalCart RenewalShoppingCart
	 * @return Object
	 * @throws RTSException
	 */
	private boolean doRenewal(RenewalShoppingCart aaRenewalCart)
		throws RTSException
	{
		boolean lbResult = false;
		if (aaRenewalCart.size() > 0)
		{
			DatabaseAccess laDBAccess = new DatabaseAccess();
			InternetTransaction laItrntTrans =
				new InternetTransaction(laDBAccess);
			// defect 7168
			CompleteRegRenData laCompRegRenData = null;
			VehicleUserData laVehUserData = null;
			// end defect 7168

			try
			{
				// try updating each vehicle in the shopping cart
				// if an exception occurs, rollback all vehicle updates

				// UOW #1 BEGIN 
				laDBAccess.beginTransaction();
				for (int i = 0; i < aaRenewalCart.size(); i++)
				{
					// retrieve the record first
					laCompRegRenData =
						(CompleteRegRenData) aaRenewalCart.elementAt(i);
					// truncate the names if necessary
					laVehUserData = laCompRegRenData.getVehUserData();
					if (laVehUserData.getLastName() != null
						&& laVehUserData.getLastName().length() > 0)
					{
						laVehUserData.setRecipientName(
							truncateNames(
								laVehUserData.getFirstName(),
								laVehUserData.getMiddleName(),
								laVehUserData.getLastName()));
					}
					// update one record
					laItrntTrans.updRenewal(laCompRegRenData);
				}
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #1 END 
				lbResult = true;
			}
			catch (RTSException aeRTSEx)
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);

				// Phase1 MQseries code will call this method.
				// when error in updating output error to a log file.
				MQLog.error(aeRTSEx);
				// Log.write(Log.SQL_EXCP, this, e.getMessage());

				// defect 7168
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"dba.endTransaction(dba.ROLLBACK) for the "
						+ "following trans");
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"Regis trace no = " + aaRenewalCart.getTraceNo());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"Veh plate no = "
						+ laCompRegRenData.getVehBaseData().getPlateNo());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"Veh vin = "
						+ laCompRegRenData.getVehBaseData().getVin());
				//com.txdot.isd.rts.server.webapps.util.MQLog.error(
				//	"Veh doc no = " + 
				//	lCompRegRenData.getVehBaseData().getDocNo());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"Veh Owner Name = "
						+ laVehUserData.getFirstName()
						+ " "
						+ laVehUserData.getMiddleName()
						+ " "
						+ laVehUserData.getLastName());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"e-mail Address : " + laVehUserData.getEmail());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"Phone Number   : "
						+ laVehUserData.getPhoneNumber());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"Itrntpymntstatuscd = "
						+ laCompRegRenData.getItrntPymntStatusCd());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"Payment Order ID = "
						+ laCompRegRenData.getPymntOrderId());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"Total fees = " + aaRenewalCart.getFeesTotal());
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"End of rollback info");
				com.txdot.isd.rts.server.webapps.util.MQLog.error(
					"=================================");
				// end defect 7168
				throw aeRTSEx;
			}
		}
		return lbResult;
	}

	/**
	 * Get the data from the found vehicle inquiry and insert 
	 * a record in the database if necessary
	 * 
	 * @param aiCountyNo int
	 * @param aaVehInquiryData VehicleInquiryData
	 * @return Object
	 * @throws RTSException
	 */
	private Object getAndInsertData(
		int aiCountyNo,
		VehicleInquiryData aaVehInquiryData)
		throws RTSException
	{

		CompleteRegRenData laCompRegRenData =
			getRenewalData(aiCountyNo, aaVehInquiryData);
		try
		{
			// defect 8378
			Vector lvRenewed =
				recordExists(
					laCompRegRenData.getVehBaseData(),
					NOT
						+ SPACE
						+ IN
						+ LEFT_PAREN
						+ CommonConstants.UNPAID
						+ COMMA
						+ CommonConstants.DECLINED_REFUND_APPROVED
						+ RIGHT_PAREN);
			// end defect 8378	
			boolean lbRenewed =
				((Boolean) lvRenewed.elementAt(0)).booleanValue();

			if (lbRenewed)
			{
				// statuses other than unpaid or 
				// declined_refund_approved
				return MessageConstants.MSG_RECENTLY_RENEWED;
			}
			else
			{
				// defect 8378
				Vector lvAllowedTransExist =
					recordExists(
						laCompRegRenData.getVehBaseData(),
						IN
							+ LEFT_PAREN
							+ CommonConstants.UNPAID
							+ COMMA
							+ CommonConstants.DECLINED_REFUND_APPROVED
							+ RIGHT_PAREN);
				// end defect 8378	
				boolean lbAllowedTransExist =
					((Boolean) lvAllowedTransExist.elementAt(0))
						.booleanValue();

				if (lbAllowedTransExist)
				{
					// defect 8365 
					// check to see if it's unpaid still trying to collect funds
					String lsItrntpymntstatuscd =
						(String) lvAllowedTransExist.elementAt(2);

					// defect 8630
					String lsCntyStatusCd =
						(String) lvAllowedTransExist.elementAt(1);
					if (lsItrntpymntstatuscd != null
						&& lsCntyStatusCd.equals(
							Integer.toString(CommonConstants.UNPAID)))
					{
						return MessageConstants.MSG_RENEWAL_IN_PROCESS;
					}
					// end defect 8630

					// String lsCntyStatusCd=(String)lvAllowedTransExist.elementAt(1); 
					// end defect 8365 
					laCompRegRenData.setAttribute(
						"CNTYSTATUSCD",
						lsCntyStatusCd);
					// if there is already unpaid or declined refund approved record in the database,
					// need to delete it.    
					delExists(laCompRegRenData.getVehBaseData());
				}

			}
			// defect 10112 
			// defect 8378
			// for transition to 5.2.3, 
			// the 5.2.2.X client must null out all Lienholder data  
			// to process IRENEW's in 5.2.3
			//			caCompTransData
			//				.getVehicleInfo()
			//				.getTitleData()
			//				.setLienHolder1(
			//				null);
			//			caCompTransData
			//				.getVehicleInfo()
			//				.getTitleData()
			//				.setLienHolder2(
			//				null);
			//			caCompTransData
			//				.getVehicleInfo()
			//				.getTitleData()
			//				.setLienHolder3(
			//				null);
			// end defect 8378
			// end defect 10112 
			
			// defect 10957
			// default itme quantity to 1
			RegFeesData laRFD = caCompTransData.getRegFeesData();
			if (laRFD != null)
			{
				Vector lvFees = laRFD.getVectFees();
				Vector lvNewFees = new Vector();
				FeesData laFD = null;
				if (lvFees != null)
				{
					for (int i = 0; i < lvFees.size(); i++)
					{
						laFD = (FeesData) lvFees.elementAt(i);
						laFD.setItmQty(1);
						lvNewFees.add(laFD);
					}
					laRFD.setVectFees(lvNewFees);
					caCompTransData.setRegFeesData(laRFD);
				}
			}	
			// end defect 10957
			insertRenewal(laCompRegRenData);
			return laCompRegRenData;
		}
		catch (RTSException aeRTSEx)
		{
			// Log.write(Log.SQL_EXCP, this, e.getMessage()); 
			Log.error(aeRTSEx);
			// cannot query the database for this.
			throw aeRTSEx;
		}
	}

	/**
	 * Get teh Renewal Data.
	 * 
	 * @param liCountyNo int
	 * @param aaVehInquiryData VehicleInquiryData
	 * @return CompleteRegRenData
	 */
	private CompleteRegRenData getRenewalData(
		int liCountyNo,
		VehicleInquiryData aaVehInquiryData)
	{
		// Set VehicleBaseData
		// There is a found fout owner county no 
		// which may be different from countyNo passed in
		VehicleBaseData laVehBaseData =
			VehicleBase.setVehicleBaseData(aaVehInquiryData);

		// countyNo can be changed, just replace the old one.
		laVehBaseData.setOwnerCountyNo(String.valueOf(liCountyNo));

		// Set VehicleDescData
		VehicleDescData laVehDescData =
			VehicleDesc.setVehicleDescData(aaVehInquiryData);

		// Calculate Fees, fees calculation also stores the
		// CompleteTransactionData with fees.	 
		Vector lavVehRegFeesData =
			getVecFees(liCountyNo, aaVehInquiryData);

		// Make and fill CompleteRegRenData
		CompleteRegRenData laCompRegRenData = new CompleteRegRenData();
		laCompRegRenData.setVehBaseData(laVehBaseData);
		laCompRegRenData.setVehDescData(laVehDescData);
		laCompRegRenData.setVehRegFeesData(lavVehRegFeesData);

		int liRegClassCd =
			aaVehInquiryData
				.getMfVehicleData()
				.getRegData()
				.getRegClassCd();
		laCompRegRenData.setInsuranceRequired(
		// defect 9469
		// use CommonFeesCache method
		//getInsuranceRequired(aaVehInquiryData));
		CommonFeesCache.isInsuranceRequired(liRegClassCd));
		// end defect 9469

		return laCompRegRenData;
	}

	/**
	 * Get the Vector of Fees.
	 * 
	 * @param aiCountyNo int
	 * @param aaData VehicleInquiryData
	 * @return Vector
	 */
	private Vector getVecFees(
		int aiCountyNo,
		VehicleInquiryData aaVehInquiryData)
	{

		CompleteTransactionData laCompleteTransData =
			new CompleteTransactionData();

		// for this Office (county)	
		laCompleteTransData.setOfcIssuanceNo(aiCountyNo);

		// vehicle just retrieved from mainframe
		MFVehicleData laOrigMFVehicleData =
			aaVehInquiryData.getMfVehicleData();

		// If Special Plates RegisData attached, 
		//    set Charge Special Plate Fee
		// defect 9119
		if (laOrigMFVehicleData.isSpclPlt())
		{
			laOrigMFVehicleData
				.getSpclPltRegisData()
				.setSpclPltChrgFeeIndi(
				1);
		}
		// end defect 9119
		// give it to complete transaction data
		laCompleteTransData.setOrgVehicleInfo(laOrigMFVehicleData);

		// copy a MFVehicleData
		MFVehicleData laNewMFVehicleData =
			(MFVehicleData) UtilityMethods.copy(laOrigMFVehicleData);
		//int liNewRegExpYear =
		//			lOrigMFVehicleData.getRegData().getRegExpYr()+1;
		// set new rex year
		//lNewMFVehicleData.getRegData().setRegExpYr(liNewRegExpYear);

		// give new, changed one
		// here, both are the same
		laCompleteTransData.setVehicleInfo(laNewMFVehicleData);

		// This should be in the phase 1 Fee calculation code, but for 
		// now (05/31/2002) it is here.
		if (laOrigMFVehicleData.getRegData().getRegClassCd()
			!= COMBINATION_REGCLASSCD)
		{
			laCompleteTransData
				.getRegTtlAddlInfoData()
				.setNoChrgRegEmiFeeIndi(
				1);
		}
		else
		{
			laCompleteTransData
				.getRegTtlAddlInfoData()
				.setNoChrgRegEmiFeeIndi(
				0);
		}

		Vector lvInv = prepInv(aiCountyNo, aaVehInquiryData);
		laCompleteTransData.setInvItms(lvInv);
		laCompleteTransData.setInvItemCount(lvInv.size());
		laCompleteTransData.setNoMFRecs(aaVehInquiryData.getNoMFRecs());
		// required by Sunil 02/07/2002

		// 	get the fees			
		Fees laFees = new Fees();

		// defect 9119
		// make sure additional reg fees get charged for IRENEW
		laCompleteTransData.getRegTtlAddlInfoData().setChrgFeeIndi(1);
		// defect 9119

		CompleteTransactionData laCompleteFeeTransData =
			laFees.calcFees(
				TransCdConstant.IRENEW,
				laCompleteTransData);
		RegFeesData laRegFeesData =
			laCompleteFeeTransData.getRegFeesData();
		Vector lvFees = laRegFeesData.getVectFees();

		// save the complete transaction data with fees info for 
		// database insertion later
		caCompTransData = laCompleteFeeTransData;

		Vector lvFeesNewForm = transformVecFees(lvFees);
		return lvFeesNewForm;

	}

	/**
	 * Get the Vehicle.
	 * 
	 * @param aaVehBaseData Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object getVeh(VehicleBaseData aaVehBaseData)
		throws RTSException
	{

		try
		{

			SearchVehicle laSearcher = new SearchVehicle(aaVehBaseData);
			Object laVehData = laSearcher.getVehicle();

			if (laVehData instanceof String)
			{
				// error, not single vehicle found
				return laVehData;
			}

			if (laVehData instanceof VehicleInquiryData)
			{
				// single vehicle found
				VehicleInquiryData laVehicleInquiryData =
					(VehicleInquiryData) laVehData;

				// defect 10608
				Vector lvInprocess =
					laVehicleInquiryData.getInProcsTransDataList();
				for (int x = 0; x < lvInprocess.size(); x++)
				{
					InProcessTransactionData laInProcessData =
						(InProcessTransactionData) lvInprocess.get(x);

					// defect 10882 	
					// transcd for IRENEW / SBRNW / WRENEW 
					if (laInProcessData
						.getTransCd()
						.equals(TransCdConstant.RENEW)
						|| laInProcessData.getTransCd().equals(
							TransCdConstant.SBRNW)
						|| laInProcessData.getTransCd().equals(
							TransCdConstant.WRENEW))
					{
						// end defect 10882 
						return MessageConstants.MSG_RECENTLY_RENEWED;
					}
				}
				// end defect 10608 	

				RegistrationEligibility laRegEligibility =
					new RegistrationEligibility(laVehicleInquiryData);
				if (laRegEligibility.isEligible())
				{
					int liCountyNo =
						Integer.parseInt(
							aaVehBaseData.getOwnerCountyNo());

					Object laObj =
						getAndInsertData(
							liCountyNo,
							laVehicleInquiryData);

					// defect 10714		
					if (laObj instanceof CompleteRegRenData)
					{
						if (laVehicleInquiryData
							.getMfVehicleData()
							.isSpclPlt())
						{
							// defect 10957
							if (PlateTypeCache
									.isVendorPlate(laVehicleInquiryData
											.getMfVehicleData()
											.getSpclPltRegisData()
											.getRegPltCd()))
							{
								((CompleteRegRenData) laObj)
										.setVendorPlate(true);
							}
							// end defect 10957
							System.out.println(
								"regPLTcode  = "
									+ laVehicleInquiryData
										.getMfVehicleData()
										.getSpclPltRegisData()
										.getRegPltCd()
									+ " "
									+ "INVcd  = "
									+ laVehicleInquiryData
										.getMfVehicleData()
										.getSpclPltRegisData()
										.getInvCd()
									+ " "
									+ "SPLfee  = "
									+ laVehicleInquiryData
										.getMfVehicleData()
										.getSpclPltRegisData()
										.getSpclFee());

							if (laVehicleInquiryData
								.getMfVehicleData()
								.getRegData()
								.getRegExpYr()
								== laVehicleInquiryData
									.getMfVehicleData()
									.getSpclPltRegisData()
									.getOrigPltExpYr()
								&& laVehicleInquiryData
									.getMfVehicleData()
									.getRegData()
									.getRegExpMo()
									== laVehicleInquiryData
										.getMfVehicleData()
										.getSpclPltRegisData()
										.getOrigPltExpMo())
							{
								// defect 19057
								if (laVehicleInquiryData
									.getMfVehicleData()
									.getSpclPltRegisData()
									.getPltValidityTerm()
									== 1)
								{
									(
										(
											CompleteRegRenData) laObj)
												.setAllowMulitplySPFee(
										true);
								}
								// end defect 10957
							}
							else
							{
								RTSDate laRegisExpireDate =
									new RTSDate(
										1,
										((laVehicleInquiryData
											.getMfVehicleData()
											.getSpclPltRegisData()
											.getOrigPltExpYr()
											* 10000)
											+ (laVehicleInquiryData
												.getMfVehicleData()
												.getSpclPltRegisData()
												.getOrigPltExpMo()
												* 100
												+ 1)));

								int liMoDiff =
									laRegisExpireDate
										.getMonthsDifference(
										new RTSDate(
											1,
											((laVehicleInquiryData
												.getMfVehicleData()
												.getRegData()
												.getRegExpYr()
												* 10000)
												+ ((laVehicleInquiryData
													.getMfVehicleData()
													.getRegData()
													.getRegExpMo()
													* 100
													+ 1)))));

								//								System.out.println("SP exp yr = " + laVehicleInquiryData.getMfVehicleData().getSpclPltRegisData().getOrigPltExpYr());
								//								System.out.println("SP exp mo = " + laVehicleInquiryData.getMfVehicleData().getSpclPltRegisData().getOrigPltExpMo());
								System.out.println(
									"liMoDiff = " + liMoDiff);
								if (liMoDiff > 35)
								{
									(
										(
											CompleteRegRenData) laObj)
												.setMaxAllowRegMos(
										36);
								}
								else if (liMoDiff > 23)
								{
									(
										(
											CompleteRegRenData) laObj)
												.setMaxAllowRegMos(
										24);
								}
								else
								{
									(
										(
											CompleteRegRenData) laObj)
												.setMaxAllowRegMos(
										12);
								}
							}

						}
						if (((CompleteRegRenData) laObj)
							.getMaxAllowRegMos()
							== 0)
						{
							int liEffectiveExpDatePlusOne = 0;
							if (laVehicleInquiryData
								.getMfVehicleData()
								.getRegData()
								.getRegExpYr()
								!= 0
								&& laVehicleInquiryData
									.getMfVehicleData()
									.getRegData()
									.getRegExpMo()
									!= 0)
							{
								if (laVehicleInquiryData
									.getMfVehicleData()
									.getRegData()
									.getRegExpMo()
									!= 12)
								{
									liEffectiveExpDatePlusOne =
										(laVehicleInquiryData
											.getMfVehicleData()
											.getRegData()
											.getRegExpYr()
											* 10000)
											+ ((laVehicleInquiryData
												.getMfVehicleData()
												.getRegData()
												.getRegExpMo()
												+ 1)
												* 100)
											+ 1;
								}
								else
								{
									liEffectiveExpDatePlusOne =
										((laVehicleInquiryData
											.getMfVehicleData()
											.getRegData()
											.getRegExpYr()
											+ 1)
											* 10000)
											+ 100
											+ 1;
								}
							}
							CommonFeesData laCommonFeesData =
								CommonFeesCache.getCommonFee(
									laVehicleInquiryData
										.getMfVehicleData()
										.getRegData()
										.getRegClassCd(),
									liEffectiveExpDatePlusOne);
							((CompleteRegRenData) laObj)
									.setMaxAllowRegMos(laCommonFeesData
											.getMaxMYrPeriodLngth());
						}
					}
					// end defect 10714
					return laObj;
				}
				else
				{
					// defect 9107
					CommonEligibility laComElig =
						new CommonEligibility(laVehicleInquiryData);
					if (laComElig.isExpiredRegistration())
					{
						return MessageConstants.MSG_REG_EXPIRED;
					}
					else
					{
						// end defect 9107	
						//return laRegEligibility.getMessage();
						return MessageConstants
							.MSG_NOT_ELIGIBLE_FOR_REG_RENEWAL;
					}
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			// defect 9495
			// will write to /usr/WebSphere5/AppServer/Production_SvrGrp/
			// rtsapperrp2.log on PAS1 or PAS2
			Log.error(
				"Internet Vehicle retrieve error for the following:");
			Log.error(aeRTSEx.getDetailMsg());
			Log.error("PlateNo: " + aaVehBaseData.getPlateNo());
			Log.error("VIN: " + aaVehBaseData.getVin());
			Log.error(
				"OwnerCountyNo: " + aaVehBaseData.getOwnerCountyNo());
			// end defect 9495

			throw aeRTSEx;
		}
		// defect 10512
		catch (Exception aeEx)
		{
			Log.error(
				"Internet Vehicle retrieve Exception for the "
					+ "following:");
			Log.error(aeEx.getMessage());
			Log.error("PlateNo: " + aaVehBaseData.getPlateNo());
			Log.error("VIN: " + aaVehBaseData.getVin());
			Log.error(
				"OwnerCountyNo: " + aaVehBaseData.getOwnerCountyNo());
			RTSException leRTSEx = new RTSException();
			if (aaVehBaseData.getOwnerCountyNo() == null)
			{
				leRTSEx.setCode(ErrorsConstant.ERR_NUM_INVALID_COUNTY);
			}
			leRTSEx.setMessage(aeEx.getMessage());
			leRTSEx.setDetailMsg(
				"Possible Internet county number session variable issue");
			throw leRTSEx;
		}
		// end defect 10512
		// should not come to here
		return MessageConstants.MSG_SYSTEM_ERROR;
	}

	/**
	 * Insert an internet registration renewal record to POS database, 
	 * done when a vehicle is retrieved from mainframe. The status of 
	 * the record is 'UNPAID'.
	 * 
	 * @param aaCompRegRenData CompleteRegRenData
	 * @throws RTSException
	 */
	private void insertRenewal(CompleteRegRenData aaCompRegRenData)
		throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		InternetTransaction laItrntTrans =
			new InternetTransaction(laDBAccess);
		InternetData laItrntData = new InternetData(laDBAccess);

		try
		{
			// UOW #1 BEGIN 
			laDBAccess.beginTransaction();

			// Insert into Internet Trans
			laItrntTrans.insItrntRenew(aaCompRegRenData);

			// Insert into Internet Data 
			laItrntData.insItrntData(caCompTransData, aaCompRegRenData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 
		}
		catch (RTSException aeRTSEx)
		{
			try
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				throw aeRTSEx;
			}
			catch (RTSException aeRTSEx2)
			{
				throw aeRTSEx2;
			}
		}
	}

	/**
	 * Is TXO Batch Allowed.
	 * 
	 * @param aaData Object
	 * @return boolean
	 */
	private boolean isAllowedTxOBatch(Object aaData)
	{
		String lsAuth = "TxDOT:RTSII Phase2 06/26/2002";
		String lsFromAuth = (String) aaData;

		return (lsFromAuth != null && lsFromAuth.indexOf(lsAuth) == 0);

		//		if (lsFromAuth == null)
		//		{
		//			return false;
		//		}
		//
		//		if (lsFromAuth.indexOf(lsAuth) == 0)
		//		{
		//			return true;
		//		}
		//		else
		//		{
		//			return false;
		//		}
	}
	/**
	 * This is the main that is used for testing.
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{

		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);
		//RegistrationRenewalServerBusiness laServerBusiness =
		//	new RegistrationRenewalServerBusiness();
		//laServerBusiness.deleteUnpaid();
		//laServerBusiness.deleteApproved();
		//laServerBusiness.deleteDeclinedRefundApproved();

		//try{
		//	// Testing query
		//	VehicleBaseData lVehBaseData=new VehicleBaseData();
		//
		//	// MF vehicle search OK, Fees not yet.
		// 	lVehBaseData.setPlateNo("AIMEE"); // mailreturn
		// 	//lVehBaseData.setPlateNo("SRR17S");// salvage
		//
		// 	lVehBaseData.setOwnerCountyNo("210"); 
		//	RegistrationRenewalServerBusiness 	lRegRenServerBusiness =new RegistrationRenewalServerBusiness();
		//	Object lObj=lRegRenServerBusiness.getVeh(lVehBaseData);	
		//	System.out.println(lObj);
		//}catch(RTSException e){
		//	e.printStackTrace();
		//}

		//CompleteRegRenData lCompRegRenData=new CompleteRegRenData();

		// ////////////////	
		//VehicleBaseData lVehBaseData=new VehicleBaseData();	
		//lVehBaseData.setDocNo("aDocNo");
		//lVehBaseData.setPlateNo("PlateNo");
		//lVehBaseData.setVin("aVin");
		//lVehBaseData.setOwnerCountyNo("150");
		//lCompRegRenData.setVehBaseData(lVehBaseData);

		// ////////////////
		//VehicleInsuranceData lVehInsuranceData=new VehicleInsuranceData();
		//lVehInsuranceData.setCompanyName("a Insurance Company");
		//lVehInsuranceData.setPolicyNo("policy no");
		//lVehInsuranceData.setAgentName("Agent Name");
		//lVehInsuranceData.setPhoneNo("5120000000");
		//lVehInsuranceData.setPolicyStartDt("20001001");
		//lVehInsuranceData.setPolicyEndDt("20021001");
		//lCompRegRenData.setVehInsuranceData(lVehInsuranceData);

		// /////////////////////
		//VehicleUserData lVehUserData=new VehicleUserData();
		//lVehUserData.setFirstName("FirstName");
		//lVehUserData.setMiddleName("MidName");
		//lVehUserData.setLastName("LastName"); 
		// 	AddressData lAddrData=new AddressData();
		// 	lAddrData.setSt1("Street 1");
		// 	lAddrData.setSt2("Street 2");
		// 	lAddrData.setCity("Austin");
		// 	lAddrData.setState("TX");
		// 	lAddrData.setZpcd("78000");
		// 	lAddrData.setZpcdp4("0000");
		// 	lVehUserData.setAddress(lAddrData);
		//lVehUserData.setTraceNumber("traceNo123");
		//lVehUserData.setEmail("user@machine.group.com");
		//lVehUserData.setRenewalDateTime( new RTSDate( (new java.util.Date()).getTime() ) );
		//lVehUserData.setPhoneNumber("5120000000");
		//lVehUserData.setRecipientName("FirstName LastName");
		//lCompRegRenData.setVehUserData(lVehUserData);

		///////////////////////////
		//Vector lvVehRegFeesData=new Vector();
		//VehicleRegFeesData lRegFeesData1=new VehicleRegFeesData();
		//lRegFeesData1.setAcctItemCd("AcctCd1");
		//lRegFeesData1.setItemPrice( (float)30.00 );
		//VehicleRegFeesData lRegFeesData2=new VehicleRegFeesData();
		//lRegFeesData2.setAcctItemCd("AcctCd2");
		//lRegFeesData2.setItemPrice( (float)60.00 );
		//lvVehRegFeesData.add(lRegFeesData1);
		//lvVehRegFeesData.add(lRegFeesData2);
		//lCompRegRenData.setVehRegFeesData(lvVehRegFeesData);

		// ////////////////////////
		//VehicleDescData lVehDescData=new VehicleDescData();
		//lVehDescData.setModelYr("2000 Accord");
		//lVehDescData.setMake("Honda");
		//lVehDescData.setExpMo("12");
		//lVehDescData.setExpYr("2002");
		//lVehDescData.setPlateAge("3");
		//lVehDescData.setEmptyWeight("3000");		
		//lVehDescData.setCarryingCapacity("5");
		//lVehDescData.setGrossWeight("3500");
		//lVehDescData.setTonnage("1");
		//lVehDescData.setTitleIssDt("20001001");
		//lVehDescData.setOwnerName("owner name"); 	
		//lCompRegRenData.setVehDescData(lVehDescData);

		// //////////////////////	
		//Vector lvInternetRegistrationRenewal=new Vector();
		//lvInternetRegistrationRenewal.add(lCompRegRenData);

		// //////////////////////
		//RegistrationRenewalServerBusiness processor=new RegistrationRenewalServerBusiness();
		//try{
		// 	Object lObj=processor.processData(RegistrationRenewalConstants.DO_REG_RENEWAL, lvInternetRegistrationRenewal);
		//	System.out.println(lObj);
		//}catch(Exception e){
		//	e.printStackTrace();
		//}
	}
	/**
	 * Prepare a vector of ProcessInvetoryData for fees calculation.
	 * 
	 * @param aiCountyNo int
	 * @param aaVehInquiryData VehicleInquiryData
	 * @return Vector
	 */
	private Vector prepInv(
		int aiCountyNo,
		VehicleInquiryData aaVehInquiryData)
	{

		String lsRegPltCd =
			aaVehInquiryData
				.getMfVehicleData()
				.getRegData()
				.getRegPltCd();
		String lsRegStkrCd =
			aaVehInquiryData
				.getMfVehicleData()
				.getRegData()
				.getRegStkrCd();
		// defect 9085 
		// PlateTypeCache replaces RegistrationRenewalsCache

		PlateTypeData laPlateTypeData =
			PlateTypeCache.getPlateType(lsRegPltCd);

		//RegistrationRenewalsData laRegRenewalData =
		//	RegistrationRenewalsCache.getRegRenwl(lsRegPltCd);

		int liNewPltReplIndi = 0;
		int liNewStkrReplIndi = 0;

		Vector lvInvData = new Vector();

		if (laPlateTypeData.getAnnualPltIndi() != 0)
		{
			liNewPltReplIndi = 1;
		}
		else
		{
			liNewStkrReplIndi = 1;
		}
		// end defect 9085 
		// add sticker to inventory list if required
		if (liNewStkrReplIndi == 1)
		{
			addItmCdToInv(lvInvData, lsRegStkrCd, aiCountyNo);

		}
		// add plate to inventory list if required
		if (liNewPltReplIndi == 1)
		{
			addItmCdToInv(lvInvData, lsRegPltCd, aiCountyNo);
		}

		return lvInvData;
	}
	/**
	 * Process Data 
	 * 
	 * @param aiModule int
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object processData(
		int aiModuleName,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		try
		{
			// System.out.println("RegistrationRenewalServerBusiness," +
			//			processData,Enter,"+moduleName+":"+functionId);
			// Log.write("RegistrationRenewalServerBusiness," +
			//			processData,Enter,"+moduleName+":"+functionId);
			switch (aiFunctionId)
			{
				case RegistrationRenewalConstants.GET_REG_REN_VEHICLE :
					{
						return getVeh((VehicleBaseData) aaData);
					}
					// defect 10714	
				case RegistrationRenewalConstants
					.UPDATE_ITRNT_DATA_FEES :
					{
						return new Boolean(
							updateFees((CompleteRegRenData) aaData));
					}
					// end defect 10714	
				case RegistrationRenewalConstants.DO_REG_RENEWAL :
					{
						RenewalShoppingCart laRenewalShoppingCart =
							(RenewalShoppingCart) aaData;
						
						//defect 10945
						if(laRenewalShoppingCart.getNeedToCompareFees()){
							// get items from shopping cart
							java.util.List laCartItems = getShoppingCartFeeItems(laRenewalShoppingCart);
							// get corresponding items from rts_itrnt_data
							java.util.List laDBItems = getDatabaseFeeItems(laCartItems);
							//compare the items
							compareCartItemsToDBItems(laCartItems, laDBItems, laRenewalShoppingCart.getTraceNo());
						}
						//end defect 10945

						return new Boolean(
							doRenewal(laRenewalShoppingCart));
					}
				case RegistrationRenewalConstants.QUERY_COUNTY :
					{
						// Query the county list, no param is needed,
						// will return the whole list of the counties.
						// This operation will be called infrequently,
						// not for every renewal vehicle search. The 
						// result will be cached in the TexasOnline site. 
						return qryCounties();
					}
				case RegistrationRenewalConstants.TXO_DO_BATCH_REFUND :
					{
						return doBatchRefund(aaData);
					}
				case RegistrationRenewalConstants
					.TXO_DO_BATCH_PAYMENT :
					{
						return doBatchPayment(aaData);
					}
				case RegistrationRenewalConstants.TXO_DO_BATCH_EMAIL :
					{
						return doBatchEmail(aaData);
					}
				case RegistrationRenewalConstants.TXO_DO_BATCH_STATUS :
					{
						return doBatchStatus(aaData);
					}
				case RegistrationRenewalConstants.TXO_DO_BATCH_ALL :
					{
						return doBatchAll(aaData);
					}
				case RegistrationRenewalConstants.DO_REG_REN_QRY :
					{
						return doQry(aaData);
					}

					// defect 8554	
				case RegistrationRenewalConstants.MQ_TEST_QRY :
					{
						return getMQTestData(aaData);
					}

				case RegistrationRenewalConstants.DO_PURGE :
					// UnPaid Internet Record Purge 	
					Purge laPurge = new Purge();
					String lsParam = (String) aaData;
					try
					{
						return Boolean.valueOf(
							laPurge.purgeIRenewItrntDataItrntTrans(
								Integer.parseInt(lsParam)));
					}
					catch (Exception leEx)
					{
						throw leEx;
					}
					// end defect 8554			

				//defect 10985
				case RegistrationRenewalConstants.GET_VEHICLE_COLORS :
					{
					CacheManagerServerBusiness laCacheManagerServerBusiness =
						new CacheManagerServerBusiness();
					return laCacheManagerServerBusiness.processData(
							GeneralConstant.GENERAL, CacheConstant.VEHICLE_COLOR_CACHE, aaData);
					}
				//end defect 10985

				default :
					{
						throw new RTSException(
							RTSException.JAVA_ERROR,
							new Exception(
								"RegistrationRenewalServerBusiness - "
									+ NO_SUCH_FUNCTION_ID
									+ aiFunctionId));
					}
			}
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
	}
	
	/**
	 * Get the registration fees for each vehicle from the shopping cart
	 * 
	 * @param laRenewalShoppingCart
	 * @return
	 * @throws RTSException
	 */
	private java.util.List getShoppingCartFeeItems(RenewalShoppingCart laRenewalShoppingCart)
		throws RTSException{
		
		java.util.List laCartItems = new java.util.ArrayList();
		
		for(int i=0; i<laRenewalShoppingCart.size(); i++){
			CompleteRegRenData laCompleteRegRenData = (CompleteRegRenData)laRenewalShoppingCart.elementAt(i);
			laCartItems.add(laCompleteRegRenData);
		}
	
		return laCartItems;
	}
	
	/**
	 * Get the registration fees for each vehicle in the shopping cart from rts_itrnt_data
	 * 
	 * @param aaCartItems
	 * @return
	 * @throws RTSException
	 */
	private java.util.List getDatabaseFeeItems(java.util.List aaCartItems) 
		throws RTSException{
		
		java.util.List laDBItems = new java.util.ArrayList();
		DatabaseAccess laDBAccess = new DatabaseAccess();
		InternetData laItrntData = new InternetData(laDBAccess);

		java.util.Iterator iterator = aaCartItems.iterator();
		while(iterator.hasNext()){
			CompleteRegRenData laCompleteRegRenData = (CompleteRegRenData)iterator.next();
			laDBAccess.beginTransaction();
			CompleteTransactionData laCompleteTransactionData = laItrntData.qryCompleteTransactionData(laCompleteRegRenData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			laDBItems.add(laCompleteTransactionData);
		}
		
		return laDBItems;
	}
	
	/**
	 * Compare the vehicle fees from the shopping cart to the fees in rts_itrnt_data
	 * 
	 * @param aaCartItems
	 * @param aaDBItems
	 * @throws RTSException 
	 * @throws RTSException
	 */
	private void compareCartItemsToDBItems(java.util.List aaCartItems, java.util.List aaDBItems, String asTraceNo) 
		throws RTSException 
		{
		
		java.util.Iterator laCartItemsIterator = aaCartItems.iterator();
		CompleteTransactionData[] laDBItemsArray = (CompleteTransactionData[])aaDBItems.toArray(new CompleteTransactionData[aaDBItems.size()]);
		
		StringBuffer lsEmailText = null;

		//loop through cart items
		while(laCartItemsIterator.hasNext()){
			CompleteRegRenData laCompleteRegRenData = (CompleteRegRenData)laCartItemsIterator.next();
			String lsPlateNo = laCompleteRegRenData.getVehBaseData().getPlateNo();
			String lsVin = laCompleteRegRenData.getVehBaseData().getVin();

			//loop through db items
			for(int i=0; i<laDBItemsArray.length; i++){
				
				//if plate and vin match, compare fees
				if(lsPlateNo.equals(laDBItemsArray[i].getVehicleInfo().getRegData().getRegPltNo()) &&
					lsVin.equals(laDBItemsArray[i].getVehicleInfo().getVehicleData().getVin())){

					//convert to same object type
					java.util.Vector lvFeesData =
						transformVehRegFeesData(
							laCompleteRegRenData.getVehRegFeesData(),
							laCompleteRegRenData.getNewRegExpYr(),
							laDBItemsArray[i].getOrgVehicleInfo().getRegData().getRegExpYr());

					//compare fees
					boolean feesAreDifferent = areFeesDifferent(lvFeesData, laDBItemsArray[i].getRegFeesData().getVectFees());

					//if there is a difference, update db, append data to email text
					if(feesAreDifferent){
						//append info to email body
						if(lsEmailText==null){
							lsEmailText = new StringBuffer();
						}
						else{
							lsEmailText.append("<hr>");
						}
						lsEmailText.append("RegPltNo = ").append(lsPlateNo).append("<br>");
						lsEmailText.append("Vin = ").append(lsVin).append("<br>");
						lsEmailText.append("TraceNo = ").append(asTraceNo).append("<br>");
						lsEmailText.append("ExpMo/ExpYr = ").append(laCompleteRegRenData.getVehDescData().getExpMo()).append("/");
						lsEmailText.append(laCompleteRegRenData.getVehDescData().getExpYr()).append("<br>");
						lsEmailText.append("NewRegExpYr = ").append(laCompleteRegRenData.getNewRegExpYr()).append("<br>");
						lsEmailText.append("MaxAllowRegMos = ").append(laCompleteRegRenData.getMaxAllowRegMos()).append("<br>");

						//add fee details to message
						lsEmailText.append(feeDetails(lvFeesData, "Fees from shopping cart: "));
						lsEmailText.append(feeDetails(laDBItemsArray[i].getRegFeesData().getVectFees(), "Fees from database: "));

						//update fees data
						this.updateFees(laCompleteRegRenData);
						Log.info("Fees mismatch update for plate no "+lsPlateNo);
					}
				}
			}
		}
		
		if(lsEmailText!=null){
			//send email
			StringBuffer lsEmailSubject = new StringBuffer();
			if (SystemProperty.getDatasource().equals(PROD_DATABASE)) {
				lsEmailSubject.append("Production: ");
			}
			else {
				lsEmailSubject.append("Test: ");
			}
			lsEmailSubject.append("IVTRS Internet Renewal fees mismatch");
			EmailUtil emailUtil = new EmailUtil();
			try
			{
				Log.error(lsEmailText.toString());
				emailUtil.sendEmail(FROM_EMAIL, TO_EMAIL, lsEmailSubject.toString(), lsEmailText.toString());
			}
			catch (Exception e)
			{
				Log.error(e.getMessage());
			}	
		}
	}

	
	/**
	 * @param aaFeesData
	 * @param asLabel
	 * @return
	 */
	private String feeDetails(java.util.List aaFeesData, String asLabel){
		java.util.Iterator cartFees = aaFeesData.iterator();
		StringBuffer lsEmailText = new StringBuffer("<br>").append(asLabel);
		lsEmailText.append("<table><tr><td>AcctItmCd</td><td>ItemPrice</td><td>ItmQty</td></tr>");
		while(cartFees.hasNext()){
			FeesData feesData = (FeesData)cartFees.next();
			lsEmailText.append("<tr><td>").append(feesData.getAcctItmCd()).append("</td>");
			lsEmailText.append("<td>").append(feesData.getItemPrice()).append("</td>");
			lsEmailText.append("<td>").append(feesData.getItmQty()).append("</td></tr>");
		}
		lsEmailText.append("</table>");

		return lsEmailText.toString();
	}

	
	/**
	 * @param alCartFeeItems
	 * @param alDBFeeItems
	 * @return
	 */
	private boolean areFeesDifferent(java.util.List alCartFeeItems, java.util.List alDBFeeItems){

		if(alCartFeeItems.size()!=alDBFeeItems.size())
			return true;

		//loop through fees from cart
		for(int i=0; i<alCartFeeItems.size(); i++){
			FeesData cartItem = (FeesData)alCartFeeItems.get(i);
			//loop through fees from db
			for(int j=0; j<alDBFeeItems.size(); j++){
				FeesData dbItem = (FeesData)alDBFeeItems.get(j);
				//if item code matches compare price and quantity
				if(cartItem.getAcctItmCd().equals(dbItem.getAcctItmCd())){
					if( !cartItem.getItemPrice().equals(dbItem.getItemPrice()) ||
						(cartItem.getItmQty()!=dbItem.getItmQty()) )
						return true;
				}
			}
		}

		return false;
	}

	//	/**
	//	 * Purge
	//	 * 
	//	 * @param aiMonths int
	//	 * @return boolean
	//	 */
	//	public boolean purgeItrntSummary(int aiMonths)
	//	{
	//		boolean lbResult = false;
	//		DatabaseAccess laDBAccess = new DatabaseAccess();
	//		try
	//		{
	//			// UOW #1 BEGIN 
	//			InternetRegistrationRenewal laItrntRegRenwl =
	//				new InternetRegistrationRenewal(laDBAccess);
	//			laDBAccess.beginTransaction();
	//			laItrntRegRenwl.purgeSummary(aiMonths);
	//			// UOW #1 END 
	//			lbResult = true;
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			try
	//			{
	//				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
	//				BatchLog.error(aeRTSEx);
	//			}
	//			catch (RTSException aeRTSEx2)
	//			{
	//			}
	//		}
	//		return lbResult;
	//	}
	/**
	 * Purge
	 * 
	 * @param aiCntyStatusCd int
	 * @param aiDataDays int
	 * @param aiTransDays int
	 * @return boolean
	 */
	//	public boolean purgeItrntDataItrntTrans(
	//		int aiCntyStatusCd,
	//		int aiDataDays,
	//		int aiTransDays)
	//	{
	//		boolean lbResult = false;
	//		DatabaseAccess laDBAccess = new DatabaseAccess();
	//		try
	//		{
	//			InternetRegistrationRenewal laItrntRegRenwl =
	//				new InternetRegistrationRenewal(laDBAccess);
	//			// UOW #1 BEGIN 	
	//			laDBAccess.beginTransaction();
	//
	//			// The sequence is IMPORTANT,
	//			// purgeData must be called before purgeTrans.
	//			// The data table has no timestamp, it is
	//			// from the trans table.
	//			laItrntRegRenwl.purgeItrntData(aiCntyStatusCd, aiDataDays);
	//			laItrntRegRenwl.purgeTrans(aiCntyStatusCd, aiTransDays);
	//			// UOW #1 END 
	//			lbResult = true;
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			try
	//			{
	//				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
	//				BatchLog.error(aeRTSEx);
	//			}
	//			catch (RTSException aeRTSEx2)
	//			{
	//			}
	//		}
	//		return lbResult;
	//	}
	/**
	 * Query the county list from the TxDOT POS database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object qryCounties() throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// UOW #1 BEGIN 
			laDBAccess.beginTransaction();
			OfficeIds laOfficeIds = new OfficeIds(laDBAccess);
			TreeMap ltmCountyList = laOfficeIds.qryCounties();
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 
			return ltmCountyList;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.error(aeRTSEx);
			throw aeRTSEx;
		}
	}

	/**
	 * Query the county list from the TxDOT POS database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private boolean updateFees(CompleteRegRenData aaCompleteRegRenData)
		throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		InternetData laItrntData = new InternetData(laDBAccess);
		InternetTransaction laItrntTrans =
			new InternetTransaction(laDBAccess);


		boolean lbGoodUpdate = false;
		try
		{
			// UOW #1 BEGIN 
			laDBAccess.beginTransaction();
			CompleteTransactionData laCompleteTransactionData =
				laItrntData.qryCompleteTransactionData(
					aaCompleteRegRenData);
			laDBAccess.endTransaction(DatabaseAccess.NONE);
			// UOW #1 END 
			// defect 10901
//			Vector lvFeesData =
//				transformVehRegFeesData(
//					aaCompleteRegRenData.getVehRegFeesData());						
			Vector lvFeesData =
				transformVehRegFeesData(
					aaCompleteRegRenData.getVehRegFeesData(),
					aaCompleteRegRenData.getNewRegExpYr(),
					laCompleteTransactionData.getOrgVehicleInfo().getRegData().getRegExpYr());
			// end defect 10901		
			laCompleteTransactionData.getRegFeesData().setVectFees(
				lvFeesData);
			NumberFormat laNumberFormat = NumberFormat.getInstance();
			laNumberFormat.setMinimumIntegerDigits(2);
			laCompleteTransactionData.getRegFeesData().setExpMoYrMin(
				laNumberFormat.format(
					Integer.parseInt(
						aaCompleteRegRenData
							.getVehDescData()
							.getExpMo()))
					+ "/"
					+ aaCompleteRegRenData.getNewRegExpYr());
			if (laCompleteTransactionData.getVehicleInfo().isSpclPlt()

				&& laCompleteTransactionData
					.getVehicleInfo()
					.getSpclPltRegisData()
					.getPltValidityTerm()
					== 1)
			{
				laCompleteTransactionData.setSpclPlateNoMoCharge(
					(aaCompleteRegRenData.getNewRegExpYr()
						- Integer.parseInt(
							aaCompleteRegRenData
								.getVehDescData()
								.getExpYr()))
						* 12);
				laCompleteTransactionData
					.getVehicleInfo()
					.getSpclPltRegisData()
					.setPltExpMo(
						Integer.parseInt(
							aaCompleteRegRenData
								.getVehDescData()
								.getExpMo()));
				laCompleteTransactionData
					.getVehicleInfo()
					.getSpclPltRegisData()
					.setPltExpYr(aaCompleteRegRenData.getNewRegExpYr());
			}
			// defect 10912
//			System.out.println("Before == laCompleteTransactionData.getVehicleInfo().getRegData().getCustActlRegFee() = " +
//					laCompleteTransactionData.getVehicleInfo().getRegData().getCustActlRegFee());
			double ldTotal = 0.0;
			for (int i = 0; i < lvFeesData.size(); ++i)
			{
				FeesData laFeesData =
					(FeesData) lvFeesData.elementAt(i);
				// defect 11241
				//if (laFeesData.getAcctItmCd().equals("MAIL"))
				if (laFeesData.getAcctItmCd().equals(AcctCdConstant.MAIL_CODE) ||
					laFeesData.getAcctItmCd().equals(AcctCdConstant.VET_FUND_CODE) ||
					laFeesData.getAcctItmCd().equals(AcctCdConstant.PARKS_FUND_CODE))
				// end defect 11241	
				{
					continue;
				}
				ldTotal += Double.parseDouble(laFeesData.getItemPrice().toString());
			}
			laCompleteTransactionData.getVehicleInfo().getRegData().setCustActlRegFee(new Dollar(ldTotal));
//			System.out.println("After == laCompleteTransactionData.getVehicleInfo().getRegData().getCustActlRegFee() = " +
//					laCompleteTransactionData.getVehicleInfo().getRegData().getCustActlRegFee());
			// end defect 10912
			// UOW #2 BEGIN
			laDBAccess.beginTransaction();
			lbGoodUpdate =
				laItrntData.updateFees(
					aaCompleteRegRenData,
					laCompleteTransactionData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END 

		}
		catch (RTSException aeRTSEx)
		{
			try
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				throw aeRTSEx;
			}
			catch (RTSException aeRTSEx2)
			{
				throw aeRTSEx2;
			}
		}
		return lbGoodUpdate;
	}

	/**
	 * Record Exists.
	 * 
	 * @param aaVehBaseData VehicleBaseData
	 * @param asCountyStatus String
	 * @return Vector
	 * @throws RTSException
	 */
	private Vector recordExists(
		VehicleBaseData aaVehBaseData,
		String asCountyStatus)
		throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		Vector lbResult = new Vector();
		lbResult.add(new Boolean(false));
		try
		{
			// defect 8869
			InternetTransaction laItrntTrans;
			// retry if we get a StaleConnection
			for (int i = 0; i < 2; i++)
			{
				try
				{
					// orginal code
					// UOW #1 BEGIN 
					laDBAccess.beginTransaction();
					laItrntTrans = new InternetTransaction(laDBAccess);
					lbResult =
						laItrntTrans.qryRenewal(
							aaVehBaseData,
							asCountyStatus);
					// end original code
					// defect 11111
					break;
					// end defect 11111
				}
				catch (RTSException aeRTSEx1)
				{
					if (aeRTSEx1.getDetailMsg() != null
						&& aeRTSEx1.getDetailMsg().indexOf(
							"StaleConnection")
							> -1)
					{
						// log the StaleConnection
						aeRTSEx1.writeExceptionToLog();
						// re-establish connection.
						laDBAccess = new DatabaseAccess();
						laDBAccess.beginTransaction();
						laItrntTrans =
							new InternetTransaction(laDBAccess);
					}
					else
					{
						// add the client host to the message detail
						aeRTSEx1.setDetailMsg(
							aeRTSEx1.getDetailMsg()
								+ CommonConstant.SYSTEM_LINE_SEPARATOR
								+ "Client "
								+ "IRENEW");
						throw aeRTSEx1;
					}
				}
			}
			// end defect 8869

			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		return lbResult;
	}

	/**
	 * transform fees in phase 1 format to phase 2 format
	 * 
	 * @param avRegFeesData Vector
	 * @return Vector
	 */
	private Vector transformVecFees(Vector avRegFeesData)
	{
		Vector lvTransformedFees = new Vector();
		// 
		for (int i = 0; i < avRegFeesData.size(); ++i)
		{
			FeesData laFeesData = (FeesData) avRegFeesData.elementAt(i);
			VehicleRegFeesData laVehRegFeesData =
				new VehicleRegFeesData();
			laVehRegFeesData.setAcctItemCd(laFeesData.getAcctItmCd());
			laVehRegFeesData.setAcctItemDesc(laFeesData.getDesc());
			laVehRegFeesData.setItemPrice(
				Float.parseFloat(laFeesData.getItemPrice().toString()));
			lvTransformedFees.add(laVehRegFeesData);
		}
		return lvTransformedFees;
	}

	/**
	 * transform fees in phase 1 format to phase 2 format
	 * 
	 * @param avRegFeesData Vector
	 * @return Vector
	 */
	private Vector transformVehRegFeesData(
		Vector avRegFeesData,
		int aiNewRegExpYr,
		int aiCurrRegExpYrPlusOne)
	{
		Vector lvTransformedFees = new Vector();
		// 
		for (int i = 0; i < avRegFeesData.size(); ++i)
		{
			VehicleRegFeesData laVehRegFeesData =
				(VehicleRegFeesData) avRegFeesData.elementAt(i);
			FeesData laFeesData = new FeesData();
			laFeesData.setAcctItmCd(laVehRegFeesData.getAcctItemCd());
			laFeesData.setDesc(laVehRegFeesData.getAcctItemDesc());
			// defect 10901
			if (laFeesData.getAcctItmCd().equals("WS") ||
				laFeesData.getAcctItmCd().equals("MAIL") ||
				laFeesData.getAcctItmCd().equals("US") ||
				laFeesData.getAcctItmCd().startsWith("SPL") ||
				laFeesData.getAcctItmCd().equals("VET-FUND") ||
				laFeesData.getAcctItmCd().equals("STPARK"))
			{
				laFeesData.setItmQty(1);
			}
			else
			{
				laFeesData.setItmQty(aiNewRegExpYr - aiCurrRegExpYrPlusOne);
			}
			// end defect 10901
			laFeesData.setItemPrice(
				new Dollar(
					Double.parseDouble(
						Float.toString(
							laVehRegFeesData.getItemPrice()))));
			lvTransformedFees.add(laFeesData);
		}
		return lvTransformedFees;
	}
	/**
	 * Truncate Names.
	 * 
	 * @param asFirstName
	 * @param asMiddleName
	 * @param asLastName
	 * @return
	 */
	private String truncateNames(
		String asFirstName,
		String asMiddleName,
		String asLastName)
	{
		final int MAX_RECIPIENT_NAME_LENGTH = 30;
		String lsRecipientName = null;
		if (asMiddleName == null || asMiddleName.length() <= 0)
		{
			if (asFirstName.length() + asLastName.length()
				> MAX_RECIPIENT_NAME_LENGTH - 1)
				// need to truncate
				asFirstName =
					asFirstName.substring(
						0,
						MAX_RECIPIENT_NAME_LENGTH
							- 1
							- asLastName.length());

			lsRecipientName = asFirstName + " " + asLastName;
		}
		else
		{
			if (asFirstName.length()
				+ asMiddleName.length()
				+ asLastName.length()
				> MAX_RECIPIENT_NAME_LENGTH - 2)
			{
				// need to truncate	 						 		
				if (asLastName.length() == 27
					|| asLastName.length() == 28)
				{
					// preserve last name, one or two for first, none 
					// for middlename
					asFirstName =
						asFirstName.substring(
							0,
							MAX_RECIPIENT_NAME_LENGTH
								- 1
								- asLastName.length());
					lsRecipientName = asFirstName + " " + asLastName;
				}
				else
				{
					// last <=26
					// preserve last name, first name second, middle 
					// name third

					// consider one for middle name, see how many for 
					// first name.
					int liFirstNameLength =
						MAX_RECIPIENT_NAME_LENGTH
							- 3
							- asLastName.length();
					// first name may not be that long.
					liFirstNameLength =
						liFirstNameLength > asFirstName.length()
							? asFirstName.length()
							: liFirstNameLength;
					asFirstName =
						asFirstName.substring(0, liFirstNameLength);

					// middle name can be long
					int liMiddleNameLength =
						MAX_RECIPIENT_NAME_LENGTH
							- 2
							- asLastName.length()
							- asFirstName.length();
					if (liMiddleNameLength > asMiddleName.length())
						liMiddleNameLength = asMiddleName.length();

					asMiddleName =
						asMiddleName.substring(0, liMiddleNameLength);
					lsRecipientName =
						asFirstName
							+ " "
							+ asMiddleName
							+ " "
							+ asLastName;
				}
			}
			else
			{
				// not truncation needed
				lsRecipientName =
					asFirstName + " " + asMiddleName + " " + asLastName;
			}
		}
		return lsRecipientName.toUpperCase();
	}
	/**
	 * This method gets a vector of lvInternetRegRecData from qryRenewalForMQTest
	 * @param aaWhereClause Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getMQTestData(Object aaWhereClause)
		throws RTSException
	{

		Vector lvInternetRegRecData = new Vector();

		DatabaseAccess laDA = new DatabaseAccess();
		boolean lbSuccess = false;

		Hashtable loHt = new Hashtable();

		try
		{
			laDA.beginTransaction();
			InternetTransaction laInternetTransaction =
				new InternetTransaction(laDA);

			loHt.put("WhereClause", (String) aaWhereClause);
			lvInternetRegRecData =
				laInternetTransaction.qryItrntRenewForMQTest(loHt);
			lbSuccess = true;
		}

		catch (RTSException laRTSE)
		{
			laRTSE.printStackTrace();
			throw laRTSE;
		}

		finally
		{
			try
			{
				if (lbSuccess)
					laDA.endTransaction(DatabaseAccess.COMMIT);
				else
					laDA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException laRTSE)
			{
				laRTSE.printStackTrace();
				throw laRTSE;
			}
		}

		return lvInternetRegRecData;

	}
}
