package com.txdot.isd.rts.server.common.business;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.*;
import com.txdot.isd.rts.server.inventory.InventoryServerBusiness;
import com.txdot.isd.rts.server.misc.MiscServerBusiness;
import com
	.txdot
	.isd
	.rts
	.server
	.registration
	.SubcontractorRenewalServerBusiness;

/*
 *
 * CommonServerBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/24/2002	check for null for parsing integer in 
 * 							postTrans()
 * 							defect 3643 
 * B Brown		05/03/2002	For Phase 2:
 *                          fixed duplicate trans_hdr key insert problem 
 * 							by making sure variable today gets loaded in 
 * 							the lastTransactionDate static variable, and  
 * 							fixes the "not throwing the 290-0-999 not
 *                          found in assgnd_ws_ids table error", which is 
 * 							now presented generically in the browser . 
 * 							See method procsInternetAddrChng.
 * 							defect 3587 
 * MAbs			05/22/2002  when error occurs in DB during
 * 							inv allocation when send cache is running, 
 * 							automatically
 * 							defect 3996  
 * MAbs/TP		06/05/2002	MultiRecs in Archive CQU100004019 make it "U"
 * Ray Rowehl	08/22/2002 	PCR 42
 *							Add handling to postTrans for 
 *							InternetTransData
 *							Comment out to be implemented later!!
 *							Added back 9/20/2002.  Ray.
 *							defect 3700
 * Sunil		09/11/2002	add handling for 182 in postTrans for 
 * 							ProcessInventoryData.
 *							defect 4731
 * Ray Rowehl	09/12/2002	split the inventory range when processing  
 * 							cache and doing a inventory delete for issue 
 * 							in postTrans.
 *							Defect 4734
 * Ray Rowehl	09/19/2002	Added handling for inventory locked in 
 * 							postTrans.
 *							This is needed for DB up procesing.
 *							defect 4734
 * Ray Rowehl	09/19/2002	Added 607 to handling in postTrans per Kathy.
 *							defect 4731 
 * Min Wang		09/24/2002  Add handling for inventory issued is cached to 
 *							be sent to server when processing Server down  
 *							and processInventoryData is stored correctly 
 *							inventory. 
 *							defect 4734
 * S Govindappa 10/02/2002	add handling for 605,593 and 594 error in 
 * 							postTrans() for ProcessInventoryData.
 *							defect 4731
 * S Govindappa 10/02/2002	Fixing defect 4800. Add handling for 605,593 
 *                          and 594 error n postTrans() for 
 * 							SubcontractorRenewalCacheData
 * S Govindappa 10/31/2002  Release all the held 
 * 							inventory on Cancel in Subcon event.
 *							Made changes to postTrans() method to release 
 *							the held inventory for Subcon.
 *							defect 4879 
 * Min Wang		10/07/2002	Update postTrans() to allow updates for 
 * 							InventoryFuncTrans, FundFuncTrans,
 *							MotorVehicleFuncTrans and Transaction to  
 *							support void. 
 *							defect 4746
 * K Harrell	02/19/2003	Added log statements for DB	Errors/Rollback.
 * 							defect 5259  
 * Ray Rowehl	02/03/2003	Pass in ClientHost to pass to MFAccess
 *							defect 4588
 * K Harrell   	03/05/2003  Throw exceptions on Inventory Delete.
 * 							defect 5259  
 * K Harrell   	03/17/2003 	Conditional call to  
 * 							UPDATE_INVENTORY_STATUS_CD
 * 							defect 5811  
 * K Harrell   	03/31/2003  PostTrans: Set FromSendCache when 
 * 							InvLocIdCd = 'X'
 * 							defect 5443  
 * Ray Rowehl 	05/13/2003	Change ProcessInventoryData processing to 
 * K Harrell				reduce the number of calls to 
 * Min Wang					InventoryServerBusiness.
 *  						modify postTrans
 *							defect 6077
 * Ray Rowehl	05/21/2003	Change postTrans to check for nullPointer on 
 * 							InvLocIdCd.
 *							defect 6156
 * Ray Rowehl	07/09/2003	Make postTrans handle rollback scenario 
 *							better update postTrans()
 *							defect 6110
 * K Harrell   	07/20/2003	Update Internet Summary table for IADDR,IRNR
 *                          altered method procsInternetAddrChng
 * 							defect 6271 
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							add reprintSticker()
 * 							modify postTrans() handle reprintData in 
 * 							cacheVector.
 * 							Ver 5.2.0	
 * K Harrell	07/02/2004	Check for ReprintStickerTransData for
 *							Reprint Sticker request
 *							modify postTrans()
 *							defect 7284  Ver 5.2.0
 * K Harrell	08/30/2004	RealTime update of RTS_Reprnt_Stkr for
 *							RSPS devices
 *							modify reprintSticker()
 *							defect 7349  Ver 5.2.1
 * K Harrell	10/10/2004	Modify references from bbxPrint to RSPSPrint
 *							renamed reprintSticker() to insRSPSData
 *							modify postTrans(),processData()
 *							defect 7608 Ver 5.2.1
 * K Harrell	10/10/2004	Use ReprintData.ReprntQty vs. PrntQty -1
 *							modify insRSPSData()
 *							defect 7597  Ver 5.2.1
 * K Harrell	10/15/2004	Modified check for SQL0803
 *							modify insRSPSData()
 * 							defect 7639  Ver 5.2.1
 * Ray Rowehl	02/08/2005	Change package references to Transaction
 * 							Also formatted source.
 * 							modify procsInternetAddrChng(), procsTrans()
 * 							defect 7705 Ver 5.2.3 
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3
 * K Harrell	07/05/2005  Java 1.4 Work 
 * 							deprecated checkNextAvail()
 * 							defect 7885 Ver 5.2.3  
 * K Harrell	08/19/2005	Modified implementation for IADDR & IRNR
 * 							add setupItrntBackOffice()
 * 							deprecated deleteSelectedTrans(),
 * 							deleteTrans() 
 * 							modify procsInternetAddr()
 * 							defect 8285 Ver 5.2.3  
 * Ray Rowehl	08/23/2005	Move MFerrorData to services.data.
 * 							Do code cleanup to remove warnings.
 * 							Work on constants.
 * 							defect 7705 Ver 5.2.3
 * K Harrell	10/07/2005	Refactoring work for Internet components
 * 							modify procsTrans() 
 * 							defect 7889 Ver 5.2.3   
 * Ray Rowehl	08/02/2006	Allow IADDR to retry on StaleConnection.
 * 							modify setupItrntBackOfcTransHdr()
 * 							defect 8869 Ver 5.2.4   
 * Ray Rowehl	08/03/2006	Change where client is added to message.
 * 							modify setupItrntBackOfcTransHdr()
 * 							defect 8869 Ver 5.2.4   
 * Ray Rowehl	08/04/2006	Rework change slightly.  Need to continue to 
 * 							avoid follow-on logic.  Also no reason to 
 * 							add the client information to the 
 * 							exception since it will not show.
 * 							Add log write on the StaleConnection.
 * 							modify setupItrntBackOfcTransHdr()
 * 							defect 8869 Ver 5.2.4
 * K Harrell	10/07/2006	Enable processing of ExemptAuditData
 * 							modify postTrans()
 * 							defect 8900 Exempts 
 * K Harrell	10/19/2006	Remove Stale Connection Exception Handling
 * 							code from setupItrntBackOfcTransHdr()
 * 							defect 8989 Ver 5.2.6/Exempts
 * Ray Rowehl	02/12/2007	Modify ProcessInventory to use a numeric 
 * 							TransWsId.
 * 							modify postTrans()
 * 							defect 9116 Ver Special Plates
 * K Harrell	02/25/2007	Modify to handle transaction deletion w/ 
 * 							new RTS_SR_FUNC_TRANS, RTS_SPCL_PLT_TRANS_HSTRY
 * 							modify deleteSelectedTrans(), deleteTrans(),
 * 							   postTrans()
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/28/2007	Modify to handle Update of VI
 * 							modify postTrans()
 * 							defect 9085 Ver Special Plates  
 * K Harrell	05/21/2007	Modify to handle IAPPL 
 * 							add setupIAPPLBackOfcTransHdr(),
 * 							  procsIAPPL() 
 * 							modify processData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/05/2007	Added call to update VI TransTime
 * 							modify postTrans()
 * 							defect 9085 Ver Special Plates    	          
 * Ray Rowehl	06/06/2007	Modify ISB Updates for VI to also pass
 * 							the DBA to keep the unit of work together.  
 * 							modify postTrans()
 * 							defect 9116 Ver Special Plates
 * K Harrell	06/12/2007	Throw exception if not "No Record Found"
 * 							in VI processing
 * 							modify postTrans()
 * 							defect 9085 Ver Special Plates 
 * Ray Rowehl	06/20/2007	Modify update of transtime or update of vi 
 * 							to ignore more types of exceptions before 
 * 							deciding to throw the orginal exception.
 * 							modify postTrans()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/23/2007	Check for 1002 error when doing VI delete.
 * 							(Void)	
 * 							modify postTrans()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/25/2007	Match up Inventory and Common on which 
 * 							exceptions will cause a rollback and 
 * 							serious error.
 * 							modify postTrans()
 * 							defect 9116 Ver Special Plates
 * Jeff S.		06/27/2007	Updated procsIAPPL() to mark the SP internet
 * 							trans record as completed and with the audit
 * 							trail trans id of the POS transaction.
 * 							add completeIAPPLInv()
 * 							modify procsIAPPL()
 * 							defect 9121 Ver Special Plates
 * K Harrell	08/10/2007	Update VI w/ Transaction Time
 * 							modify completeIAPPLInv()
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/04/2007	add the creation of SR_FUNC_TRANS for IADDR
 * 							modify procsInternetAddrChang()
 * 							defect 9315 Ver Special Plates  
 * Ray Rowehl	11/27/2007	Add Log4j processing to pass to MfAccess.
 * 							add saLog4jCommonServerBus
 * 							change CommonServerBusiness(),
 * 								CommonServerBusiness(String)
 * 							defect 9441 Ver ?
 * Bob Brown	01/24/2008  Make sure the current date is used to update
 * 							inventory allocation transamdate for IAPPL's
 * 							modify completeIAPPLInv()
 * 							defect 9467 Ver Tres Amigos Prep	 
 * B Hargrove	01/31/2008	Modify to handle V21VTN
 * 							(Vision 21 Vehicle Transaction Notification)
 * 							add setupV21VTNBackOfcTransHdr(),
 * 							  procsV21VTN() 
 * 							modify processData()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * B Hargrove	02/07/2008	Modify to handle V21PLD
 * 							(Vision 21 Plate Disposition)
 * 							add setupV21PLDBackOfcTransHdr(),
 * 							procsV21PLD() 
 * 							modify processData()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/13/2008	Add TransCode shift for V21 depending on 
 * 							transcode provided.
 * 							modify procsV21VTN()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * B Hargrove	02/26/2008	Add setting TransTime in Transaction
 * 							Header data (will use in tracking
 * 							V21 request to RTS transaction.
 * 							modify procsV21VTN(),procsV21PLD()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * K Harrell	04/07/2008	Add case stmt to process V21PLD
 * 							modify processData()
 * 							defect 9582 Ver Defect POS A
 * K Harrell	06/26/2008	Return Transaction Header object for V21PLD
 * 							to write in V21 Tracking Tables 
 * 							modify procsV21PLD()
 * 							defect 9582 Ver Defect POS A 
 * B Brown		06/17/2008	Add processing for MyPlates
 * 							transcd's VPAPPL and VPAPPR.
 * 							add procsVendorPlates()
 * 							modify processData()
 * 							add PROC_VENDOR_PLATES
 * 							defect 9711 Ver MyPlates_POS.
 * B Brown		06/17/2008	Genericize the trans header processing for 
 * 							MyPlates transcd's VPAPPL and VPAPPR.
 * 							add setupVendorBackOfcTransHdr()
 * 							delete setupVPAPPLBackOfcTransHdr(),
 * 							setupVPAPPRBackOfcTransHdr()
 * 							defect 9711 Ver MyPlates_POS.
 * B Brown		07/07/2008	Use SystemProperty for Ofcissuanceno and  
 * 							transWsId. Also, get the transcd for the 
 * 							trans header insert from CompletetransData.
 *							modify setupVendorBackOfcTransHdr(), 
 *								procsVendorPlates()
 * 							defect 9711 Ver MyPlates_POS.
 * B Brown		07/15/2008	Use SystemProperty for Ofcissuanceno and  
 * 							transWsId for inventory processing also.
 * 							modify setupVendorBackOfcTransHdr(), 
 * 								completeVendorInv()
 * 							defect 9711 Ver MyPlates_POS.
 * K Harrell	07/16/2008	Added code for 04/07/2008 entry above. 
 * 							defect 9582 Ver MyPlates_POS  
 * K Harrell	10/21/2008	Add logic for Disabled Placard Transaction
 * 							Processing
 * 							modify postTrans() 
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/01/2008	Add logic for Disabled Placard Customer 
 * 							 Reset In Process 
 * 							modify postTrans()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	03/20/2009	Add logic for Electronic Title History 
 * 							modify postTrans()
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	08/11/2009	Modifications for new DB2 Driver 
 * 							modify insRSPSData() 
 * 							defect 10164 Ver Defect_POS_E' 
 * K Harrell	12/18/2009	Do not throw exception on Special Plate 
 * 							Application Delete if from SendCache
 * 							modify postTrans()
 * 							defect 10307 Ver Defect_POS_H
 * K Harrell	03/12/2010	Update case statements for unused calls.
 * 							Add 'missing' case statement for Transaction
 * 							 tables w/ 'break' 
 * 							modify postTrans()  
 * 							defect 10239 Ver POS_640  
 * Ray Rowehl	03/19/2010	Allow passing of DBA into procsVendorPlates()
 * 							modify procsVendorPlates()
 * 							defect 10401 Ver 6.4.0
 * K Harrell	03/31/2010	Add case statement for WorkstationStatusData 
 * 							 - for later implementation.  
 * 							modify postTrans()
 * 							defect 8087 Ver POS_640
 * Ray Rowehl	04/29/2010	Set the PLP indi based on input instead of 
 * 							always true 
 * 							modify completeVendorInv()
 * 							defect 10401 Ver 6.4.0
 * K Harrell	05/25/2010	add Permit Transaction Processing
 * 							modify postTrans()
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	06/30/2010	add void processing for Permit Transaction
 * 							modify postTrans()
 * 							defect 10491 Ver 6.5.0 
 * B Brown		12/08/2010	Add code for processing IADDRE transcd for
 * 							eReminder project.
 * 							add procChngeEReminder()
 * 							modify processData(), 
 * 							setupItrntBackOfcTransHdr(),
 * 							procsInternetAddrChng()
 * 							defect 10610 Ver 6.7.0
 * K Harrell	12/23/2010	add processing for Special Plate Permit
 * 							modify postTrans(), deleteTrans()  
 * 							defect 10700 Ver 6.7.0 
 * K Harrell	01/20/2011	assign TransTime from Client
 * 							modify procsTrans()
 * 							defect 10674 Ver 6.7.0 
 * K Harrell	01/21/2011  add procsWebSubTrans()
 * 							modify processData() 
 * 							defect 10734 Ver 6.7.0  
 * K Harrell	02/23/2011	only assign TransTime for Reprint 
 * 							Transactions - the only transactions from 
 * 							Transaction Cache, RPRSTK, RPRPRM
 * 							modify procsTrans() 
 * 							defect 10756 Ver 6.7.0 
 * K Harrell	04/12/2011	add new error message constants 
 * 							modify procsWebSubTrans() 
 * 							defect 10734 Ver 6.7.1
 * K Harrell	05/29/2011	add logic for FraudLog insert
 * 							modify postTrans()
 * 							defect 10865 Ver 6.8.0
 * K Harrell	05/31/2011	add logic for ModifyPermitTransactionHistory
 * 							modify postTrans()
 * 							defect 10844 Ver 6.8.0
 * K Harrell	09/12/2011	add logic for posting LogFuncTrans
 * 							modify postTrans()
 * 							defect 10994 Ver 6.8.1 
 * K Harrell	11/05/2011	modify procsWebSubTrans() 
 * 							defect 11137 Ver 6.9.0 
 * ---------------------------------------------------------------------
 * 				
 */

/**
 * The CommonServerBusiness dispatch the incoming request to
 * the function request on the server side.  
 * It also returns the result back to the caller
 *
 * @version	6.9.0  			11/05/2011
 * @author	Joseph Peters
 * @since					08/22/2001
 */

public class CommonServerBusiness
{
	private VehicleInquiry caVehInq;
	private String csClientHost = CommonConstant.TXT_UNKNOWN;
	// defect 9441
	public static org.apache.log4j.Logger saLog4jCommonServerBus;
	// end defect 9441

	/**
	 * CommonServerBusiness constructor.
	 */
	public CommonServerBusiness()
	{
		super();
		caVehInq = new VehicleInquiry(csClientHost);

		// defect 9441
		// start the log if needed
		if (saLog4jCommonServerBus == null)
		{
			try
			{
				saLog4jCommonServerBus =
					Logger.getLogger("CommonServerBusiness");

				if (saLog4jCommonServerBus == null)
				{
					saLog4jCommonServerBus = Logger.getRootLogger();
				}
			}
			catch (Exception aeEx)
			{
				System.err.println("CommonServerBusiness init problem");
				aeEx.printStackTrace();
			}
		}
		// end defect 9441
	}

	/**
	 * CommonServerBusiness constructor.
	 */
	public CommonServerBusiness(String asClientHost)
	{
		super();
		csClientHost = asClientHost;
		caVehInq = new VehicleInquiry(csClientHost);

		// defect 9441
		// start the log if needed
		if (saLog4jCommonServerBus == null)
		{
			try
			{
				saLog4jCommonServerBus =
					Logger.getLogger("CommonServerBusiness");

				if (saLog4jCommonServerBus == null)
				{
					saLog4jCommonServerBus = Logger.getRootLogger();
				}
			}
			catch (Exception aeEx)
			{
				System.err.println("CommonServerBusiness init problem");
				aeEx.printStackTrace();
			}
		}
		// end defect 9441

	}

	/**
	 * Log Mainframe error
	 *  
	 * @param aaObj Object 
	 * @return Object
	 * @throws RTSException
	 */
	private Object appMfErrLog(Object aaObj) throws RTSException
	{
		com.txdot.isd.rts.server.dataaccess.MfAccess laMf =
			new com.txdot.isd.rts.server.dataaccess.MfAccess(
				csClientHost);
		laMf.logError(
			(com.txdot.isd.rts.services.data.MFErrorData) aaObj);
		return null;
	}

	/**
	 * Delete Selected Transaction
	 * 
	 * @param aaObj Object 
	 * @return Object
	 * 
	 * @deprecated
	 */
	private Object deleteSelectedTrans(Object aaObj)
		throws RTSException
	{
		Vector lvIn = (Vector) aaObj;
		DatabaseAccess laDBAccess = null;
		laDBAccess =
			(DatabaseAccess) lvIn.get(CommonConstant.ELEMENT_0);
		boolean lbCreateDb = false;
		if (laDBAccess == null)
		{
			lbCreateDb = true;
			laDBAccess = new DatabaseAccess();
			laDBAccess.beginTransaction();
		}
		Vector lvTransactionKey =
			(Vector) lvIn.get(CommonConstant.ELEMENT_1);
		boolean lbIsSuccessful = false;
		try
		{
			if (lvTransactionKey != null
				&& lvTransactionKey.size() > 0)
			{
				for (int i = 0; i < lvTransactionKey.size(); i++)
				{
					TransactionKey laTransactionKey =
						(TransactionKey) lvTransactionKey.get(i);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_FUNDFUNC_START
							+ csClientHost);

					//FundFuncTrans
					FundFunctionTransactionData laFundFunctionTransactionData =
						new FundFunctionTransactionData();
					laFundFunctionTransactionData.setOfcIssuanceNo(
						laTransactionKey.getOfcIssuanceNo());
					laFundFunctionTransactionData.setSubstaId(
						laTransactionKey.getSubstaId());
					laFundFunctionTransactionData.setTransAMDate(
						laTransactionKey.getTransAMDate());
					laFundFunctionTransactionData.setTransWsId(
						laTransactionKey.getTransWsId());
					laFundFunctionTransactionData.setCustSeqNo(
						laTransactionKey.getCustSeqNo());
					laFundFunctionTransactionData.setTransTime(
						laTransactionKey.getTransTime());
					FundFunctionTransaction laFundFunctionTransaction =
						new FundFunctionTransaction(laDBAccess);
					laFundFunctionTransaction
						.delFundFunctionTransaction(
						laFundFunctionTransactionData);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_FUNDFUNC_SUCCESS
							+ csClientHost);

					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_TR_FDS_DETAIL_START
							+ csClientHost);
					//TransactionFundsDetail
					TransactionFundsDetailData laTransactionFundsDetailData =
						new TransactionFundsDetailData();
					laTransactionFundsDetailData.setOfcIssuanceNo(
						laTransactionKey.getOfcIssuanceNo());
					laTransactionFundsDetailData.setSubstaId(
						laTransactionKey.getSubstaId());
					laTransactionFundsDetailData.setTransAMDate(
						laTransactionKey.getTransAMDate());
					laTransactionFundsDetailData.setTransWsId(
						laTransactionKey.getTransWsId());
					laTransactionFundsDetailData.setCustSeqNo(
						laTransactionKey.getCustSeqNo());
					laTransactionFundsDetailData.setTransTime(
						laTransactionKey.getTransTime());
					TransactionFundsDetail laTransactionFundsDetail =
						new TransactionFundsDetail(laDBAccess);
					laTransactionFundsDetail.delTransactionFundsDetail(
						laTransactionFundsDetailData);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant
							.MSG_DB_CALL_TR_FDS_DETAIL_SUCCESS
							+ csClientHost);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_INV_FUNC_START
							+ csClientHost);

					//InventoryFunctionTransaction
					InventoryFunctionTransactionData laInventoryFunctionTransactionData =
						new InventoryFunctionTransactionData();
					laInventoryFunctionTransactionData
						.setOfcIssuanceNo(
						laTransactionKey.getOfcIssuanceNo());
					laInventoryFunctionTransactionData.setSubstaId(
						laTransactionKey.getSubstaId());
					laInventoryFunctionTransactionData.setTransAMDate(
						laTransactionKey.getTransAMDate());
					laInventoryFunctionTransactionData.setTransWsId(
						laTransactionKey.getTransWsId());
					laInventoryFunctionTransactionData.setCustSeqNo(
						laTransactionKey.getCustSeqNo());
					laInventoryFunctionTransactionData.setTransTime(
						laTransactionKey.getTransTime());
					InventoryFunctionTransaction laInventoryFunctionTransaction =
						new InventoryFunctionTransaction(laDBAccess);
					laInventoryFunctionTransaction
						.delInventoryFunctionTransaction(
						laInventoryFunctionTransactionData);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_INV_FUNC_SUCCESS
							+ csClientHost);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_TR_INV_DTL_START
							+ csClientHost);

					//TransactionInventoryDetail
					TransactionInventoryDetailData laTransactionInventoryDetailData =
						new TransactionInventoryDetailData();
					laTransactionInventoryDetailData.setOfcIssuanceNo(
						laTransactionKey.getOfcIssuanceNo());
					laTransactionInventoryDetailData.setSubstaId(
						laTransactionKey.getSubstaId());
					laTransactionInventoryDetailData.setTransAMDate(
						laTransactionKey.getTransAMDate());
					laTransactionInventoryDetailData.setTransWsId(
						laTransactionKey.getTransWsId());
					laTransactionInventoryDetailData.setCustSeqNo(
						laTransactionKey.getCustSeqNo());
					laTransactionInventoryDetailData.setTransTime(
						laTransactionKey.getTransTime());
					TransactionInventoryDetail laTransactionInventoryDetail =
						new TransactionInventoryDetail(laDBAccess);
					laTransactionInventoryDetail
						.delTransactionInventoryDetail(
						laTransactionInventoryDetailData);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_TR_INV_DTL_SUCCESS
							+ csClientHost);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_MV_FUNC_START
							+ csClientHost);

					//MotorVehicleFunctionTransaction
					MotorVehicleFunctionTransactionData laMotorVehicleFunctionTransactionData =
						new MotorVehicleFunctionTransactionData();
					laMotorVehicleFunctionTransactionData
						.setOfcIssuanceNo(
						laTransactionKey.getOfcIssuanceNo());
					laMotorVehicleFunctionTransactionData.setSubstaId(
						laTransactionKey.getSubstaId());
					laMotorVehicleFunctionTransactionData
						.setTransAMDate(
						laTransactionKey.getTransAMDate());
					laMotorVehicleFunctionTransactionData.setTransWsId(
						laTransactionKey.getTransWsId());
					laMotorVehicleFunctionTransactionData.setCustSeqNo(
						laTransactionKey.getCustSeqNo());
					laMotorVehicleFunctionTransactionData.setTransTime(
						laTransactionKey.getTransTime());
					MotorVehicleFunctionTransaction laMotorVehicleFunctionTransaction =
						new MotorVehicleFunctionTransaction(laDBAccess);
					laMotorVehicleFunctionTransaction
						.delMotorVehicleFunctionTransaction(
						laMotorVehicleFunctionTransactionData);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_MV_FUNC_SUCCESS
							+ csClientHost);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_TRANS_START
							+ csClientHost);
					//Transaction
					TransactionData laTransactionData =
						new TransactionData();
					laTransactionData.setOfcIssuanceNo(
						laTransactionKey.getOfcIssuanceNo());
					laTransactionData.setSubstaId(
						laTransactionKey.getSubstaId());
					laTransactionData.setTransAMDate(
						laTransactionKey.getTransAMDate());
					laTransactionData.setTransWsId(
						laTransactionKey.getTransWsId());
					laTransactionData.setCustSeqNo(
						laTransactionKey.getCustSeqNo());
					laTransactionData.setTransTime(
						laTransactionKey.getTransTime());
					com
						.txdot
						.isd
						.rts
						.server
						.db
						.Transaction laTransaction =
						new com.txdot.isd.rts.server.db.Transaction(
							laDBAccess);
					laTransaction.delTransaction(laTransactionData);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_TRANS_SUCCESS
							+ csClientHost);

					// defect 8900 		
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_EXMPTAUDIT_START
							+ csClientHost);
					ExemptAuditData laExemptAuditData =
						new ExemptAuditData();
					laExemptAuditData.setOfcIssuanceNo(
						laTransactionKey.getOfcIssuanceNo());
					laExemptAuditData.setSubstaId(
						laTransactionKey.getSubstaId());
					laExemptAuditData.setTransAMDate(
						laTransactionKey.getTransAMDate());
					laExemptAuditData.setTransWsId(
						laTransactionKey.getTransWsId());
					laExemptAuditData.setCustSeqNo(
						laTransactionKey.getCustSeqNo());
					laExemptAuditData.setTransTime(
						laTransactionKey.getTransTime());
					ExemptAudit laExemptAudit =
						new ExemptAudit(laDBAccess);
					laExemptAudit.delExemptAudit(laExemptAuditData);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_EXMPTAUDIT_SUCCESS
							+ csClientHost);
					// end defect 8900 

					// defect 9805 
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_SR_FUNC_START
							+ csClientHost);
					SpecialRegistrationFunctionTransactionData laSpecRegisFuncData =
						new SpecialRegistrationFunctionTransactionData();
					laSpecRegisFuncData.setOfcIssuanceNo(
						laTransactionKey.getOfcIssuanceNo());
					laSpecRegisFuncData.setSubstaId(
						laTransactionKey.getSubstaId());
					laSpecRegisFuncData.setTransAMDate(
						laTransactionKey.getTransAMDate());
					laSpecRegisFuncData.setTransWsId(
						laTransactionKey.getTransWsId());
					laSpecRegisFuncData.setCustSeqNo(
						laTransactionKey.getCustSeqNo());
					laSpecRegisFuncData.setTransTime(
						laTransactionKey.getTransTime());
					SpecialRegistrationFunctionTransaction laSpecRegisFuncTrans =
						new SpecialRegistrationFunctionTransaction(laDBAccess);
					laSpecRegisFuncTrans
						.delSpecialRegistrationFunctionTransaction(
						laSpecRegisFuncData);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_SR_FUNC_SUCCESS
							+ csClientHost);

					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_SP_TRANS_HSTRY_START
							+ csClientHost);
					SpecialPlateTransactionHistoryData laSpclPltTransHstryData =
						new SpecialPlateTransactionHistoryData();
					laSpclPltTransHstryData.setOfcIssuanceNo(
						laTransactionKey.getOfcIssuanceNo());
					laSpclPltTransHstryData.setSubstaId(
						laTransactionKey.getSubstaId());
					laSpclPltTransHstryData.setTransAMDate(
						laTransactionKey.getTransAMDate());
					laSpclPltTransHstryData.setTransWsId(
						laTransactionKey.getTransWsId());
					laSpclPltTransHstryData.setCustSeqNo(
						laTransactionKey.getCustSeqNo());
					laSpclPltTransHstryData.setTransTime(
						laTransactionKey.getTransTime());

					SpecialPlateTransactionHistory laSpclPltTransHstry =
						new SpecialPlateTransactionHistory(laDBAccess);
					laSpclPltTransHstry
						.delSpecialPlateTransactionHistory(
						laSpclPltTransHstryData);
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_SP_TRANS_HSTRY_START
							+ csClientHost);
					// end defect 9805

					lbIsSuccessful = true;
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_ALL_SUCCESS
							+ csClientHost);
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				CommonConstant.MSG_FAILED_DB_CALL + csClientHost);
			throw aeRTSEx;
		}
		finally
		{
			if (lbCreateDb)
			{
				if (lbIsSuccessful)
				{
					laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				}
				else
				{
					laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
					Log.write(
						Log.SQL_EXCP,
						this,
						CommonConstant.MSG_ROLLBACK_ISSUED
							+ csClientHost);
				}
			}
		}
		return new Integer(1);
	}

	/**
	 * Delete Transaction
	 * 
	 * @param aaObj Object 
	 * @return Object
	 * @throws RTSException
	 * 
	 * @deprecated
	 */
	private Object deleteTrans(Object aaObj) throws RTSException
	{
		TransactionHeaderData laTransactionHeaderData =
			(TransactionHeaderData) aaObj;
		DatabaseAccess laDBAccess = new DatabaseAccess();
		boolean lbIsSuccessful = false;
		try
		{
			laDBAccess.beginTransaction();
			//Release Issued Inventory for Customer Transaction
			//Transaction Header
			TransactionHeader laTransactionHeader =
				new TransactionHeader(laDBAccess);
			laTransactionHeader.delTransactionHeader(
				laTransactionHeaderData);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_FUNDFUNC_START
					+ csClientHost);
			//FundFuncTrans
			FundFunctionTransactionData laFundFunctionTransactionData =
				new FundFunctionTransactionData();
			laFundFunctionTransactionData.setOfcIssuanceNo(
				laTransactionHeaderData.getOfcIssuanceNo());
			laFundFunctionTransactionData.setSubstaId(
				laTransactionHeaderData.getSubstaId());
			laFundFunctionTransactionData.setTransAMDate(
				laTransactionHeaderData.getTransAMDate());
			laFundFunctionTransactionData.setTransWsId(
				laTransactionHeaderData.getTransWsId());
			laFundFunctionTransactionData.setCustSeqNo(
				laTransactionHeaderData.getCustSeqNo());
			FundFunctionTransaction laFundFunctionTransaction =
				new FundFunctionTransaction(laDBAccess);
			laFundFunctionTransaction.delFundFunctionTransaction(
				laFundFunctionTransactionData);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_FUNDFUNC_SUCCESS
					+ csClientHost);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_TR_FDS_DETAIL_START
					+ csClientHost);
			//TransactionFundsDetail
			TransactionFundsDetailData laTransactionFundsDetailData =
				new TransactionFundsDetailData();
			laTransactionFundsDetailData.setOfcIssuanceNo(
				laTransactionHeaderData.getOfcIssuanceNo());
			laTransactionFundsDetailData.setSubstaId(
				laTransactionHeaderData.getSubstaId());
			laTransactionFundsDetailData.setTransAMDate(
				laTransactionHeaderData.getTransAMDate());
			laTransactionFundsDetailData.setTransWsId(
				laTransactionHeaderData.getTransWsId());
			laTransactionFundsDetailData.setCustSeqNo(
				laTransactionHeaderData.getCustSeqNo());
			TransactionFundsDetail laTransactionFundsDetail =
				new TransactionFundsDetail(laDBAccess);
			laTransactionFundsDetail.delTransactionFundsDetail(
				laTransactionFundsDetailData);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_TR_FDS_DETAIL_SUCCESS
					+ csClientHost);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_INV_FUNC_START
					+ csClientHost);
			//InventoryFunctionTransaction
			InventoryFunctionTransactionData laInventoryFunctionTransactionData =
				new InventoryFunctionTransactionData();
			laInventoryFunctionTransactionData.setOfcIssuanceNo(
				laTransactionHeaderData.getOfcIssuanceNo());
			laInventoryFunctionTransactionData.setSubstaId(
				laTransactionHeaderData.getSubstaId());
			laInventoryFunctionTransactionData.setTransAMDate(
				laTransactionHeaderData.getTransAMDate());
			laInventoryFunctionTransactionData.setTransWsId(
				laTransactionHeaderData.getTransWsId());
			laInventoryFunctionTransactionData.setCustSeqNo(
				laTransactionHeaderData.getCustSeqNo());
			InventoryFunctionTransaction laInventoryFunctionTransaction =
				new InventoryFunctionTransaction(laDBAccess);
			laInventoryFunctionTransaction
				.delInventoryFunctionTransaction(
				laInventoryFunctionTransactionData);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_INV_FUNC_SUCCESS
					+ csClientHost);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_TR_INV_DTL_START
					+ csClientHost);
			//TransactionInventoryDetail
			TransactionInventoryDetailData laTransactionInventoryDetailData =
				new TransactionInventoryDetailData();
			laTransactionInventoryDetailData.setOfcIssuanceNo(
				laTransactionHeaderData.getOfcIssuanceNo());
			laTransactionInventoryDetailData.setSubstaId(
				laTransactionHeaderData.getSubstaId());
			laTransactionInventoryDetailData.setTransAMDate(
				laTransactionHeaderData.getTransAMDate());
			laTransactionInventoryDetailData.setTransWsId(
				laTransactionHeaderData.getTransWsId());
			laTransactionInventoryDetailData.setCustSeqNo(
				laTransactionHeaderData.getCustSeqNo());
			TransactionInventoryDetail laTransactionInventoryDetail =
				new TransactionInventoryDetail(laDBAccess);
			laTransactionInventoryDetail.delTransactionInventoryDetail(
				laTransactionInventoryDetailData);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_TR_INV_DTL_SUCCESS
					+ csClientHost);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_MV_FUNC_START
					+ csClientHost);
			//MotorVehicleFunctionTransaction
			MotorVehicleFunctionTransactionData laMotorVehicleFunctionTransactionData =
				new MotorVehicleFunctionTransactionData();
			laMotorVehicleFunctionTransactionData.setOfcIssuanceNo(
				laTransactionHeaderData.getOfcIssuanceNo());
			laMotorVehicleFunctionTransactionData.setSubstaId(
				laTransactionHeaderData.getSubstaId());
			laMotorVehicleFunctionTransactionData.setTransAMDate(
				laTransactionHeaderData.getTransAMDate());
			laMotorVehicleFunctionTransactionData.setTransWsId(
				laTransactionHeaderData.getTransWsId());
			laMotorVehicleFunctionTransactionData.setCustSeqNo(
				laTransactionHeaderData.getCustSeqNo());
			MotorVehicleFunctionTransaction laMotorVehicleFunctionTransaction =
				new MotorVehicleFunctionTransaction(laDBAccess);
			laMotorVehicleFunctionTransaction
				.delMotorVehicleFunctionTransaction(
				laMotorVehicleFunctionTransactionData);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_MV_FUNC_SUCCESS
					+ csClientHost);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_TRANS_START + csClientHost);
			//Transaction
			TransactionData laTransactionData = new TransactionData();
			laTransactionData.setOfcIssuanceNo(
				laTransactionHeaderData.getOfcIssuanceNo());
			laTransactionData.setSubstaId(
				laTransactionHeaderData.getSubstaId());
			laTransactionData.setTransAMDate(
				laTransactionHeaderData.getTransAMDate());
			laTransactionData.setTransWsId(
				laTransactionHeaderData.getTransWsId());
			laTransactionData.setCustSeqNo(
				laTransactionHeaderData.getCustSeqNo());
			com.txdot.isd.rts.server.db.Transaction laTransaction =
				new com.txdot.isd.rts.server.db.Transaction(laDBAccess);
			laTransaction.delTransaction(laTransactionData);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_TRANS_SUCCESS
					+ csClientHost);
			// defect 8900 		
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_EXMPTAUDIT_START
					+ csClientHost);
			ExemptAuditData laExemptAuditData = new ExemptAuditData();
			laExemptAuditData.setOfcIssuanceNo(
				laTransactionHeaderData.getOfcIssuanceNo());
			laExemptAuditData.setSubstaId(
				laTransactionHeaderData.getSubstaId());
			laExemptAuditData.setTransAMDate(
				laTransactionHeaderData.getTransAMDate());
			laExemptAuditData.setTransWsId(
				laTransactionHeaderData.getTransWsId());
			laExemptAuditData.setCustSeqNo(
				laTransactionHeaderData.getCustSeqNo());
			ExemptAudit laExemptAudit = new ExemptAudit(laDBAccess);
			laExemptAudit.delExemptAudit(laExemptAuditData);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_EXMPTAUDIT_SUCCESS
					+ csClientHost);
			// end defect 8900 

			// defect 9805 
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_SR_FUNC_START
					+ csClientHost);
			SpecialRegistrationFunctionTransactionData laSpecRegisFuncData =
				new SpecialRegistrationFunctionTransactionData();
			laSpecRegisFuncData.setOfcIssuanceNo(
				laTransactionHeaderData.getOfcIssuanceNo());
			laSpecRegisFuncData.setSubstaId(
				laTransactionHeaderData.getSubstaId());
			laSpecRegisFuncData.setTransAMDate(
				laTransactionHeaderData.getTransAMDate());
			laSpecRegisFuncData.setTransWsId(
				laTransactionHeaderData.getTransWsId());
			laSpecRegisFuncData.setCustSeqNo(
				laTransactionHeaderData.getCustSeqNo());

			SpecialRegistrationFunctionTransaction laSpecRegisFuncTrans =
				new SpecialRegistrationFunctionTransaction(laDBAccess);
			laSpecRegisFuncTrans
				.delSpecialRegistrationFunctionTransaction(
				laSpecRegisFuncData);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_SR_FUNC_SUCCESS
					+ csClientHost);

			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_SP_TRANS_HSTRY_START
					+ csClientHost);
			SpecialPlateTransactionHistoryData laSpclPltTransHstryData =
				new SpecialPlateTransactionHistoryData();
			laSpclPltTransHstryData.setOfcIssuanceNo(
				laTransactionHeaderData.getOfcIssuanceNo());
			laSpclPltTransHstryData.setSubstaId(
				laTransactionHeaderData.getSubstaId());
			laSpclPltTransHstryData.setTransAMDate(
				laTransactionHeaderData.getTransAMDate());
			laSpclPltTransHstryData.setTransWsId(
				laTransactionHeaderData.getTransWsId());
			laSpclPltTransHstryData.setCustSeqNo(
				laTransactionHeaderData.getCustSeqNo());
			SpecialPlateTransactionHistory laSpclPltTransHstry =
				new SpecialPlateTransactionHistory(laDBAccess);
			laSpclPltTransHstry.delSpecialPlateTransactionHistory(
				laSpclPltTransHstryData);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_SP_TRANS_HSTRY_START
					+ csClientHost);
			// end defect 9805

			// defect 10700 
			SpecialPlatePermitData laSpclPltPermitData =
				new SpecialPlatePermitData();
			laSpclPltPermitData.setOfcIssuanceNo(
				laTransactionHeaderData.getOfcIssuanceNo());
			laSpclPltPermitData.setSubstaId(
				laTransactionHeaderData.getSubstaId());
			laSpclPltPermitData.setTransAMDate(
				laTransactionHeaderData.getTransAMDate());
			laSpclPltPermitData.setTransWsId(
				laTransactionHeaderData.getTransWsId());
			laSpclPltPermitData.setCustSeqNo(
				laTransactionHeaderData.getCustSeqNo());
			SpecialPlatePermit laSpclPltPrmtSQL =
				new SpecialPlatePermit(laDBAccess);
			laSpclPltPrmtSQL.delSpecialPlatePermit(laSpclPltPermitData);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_SPCL_PLT_PRMT_START
					+ csClientHost);
			// end defect 10700 '
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_ALL_SUCCESS + csClientHost);
			lbIsSuccessful = true;
			return new Integer(1);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				CommonConstant.MSG_FAILED_DB_CALL);
			throw aeRTSEx;
		}
		finally
		{
			if (lbIsSuccessful)
			{
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			}
			else
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				Log.write(
					Log.SQL_EXCP,
					this,
					CommonConstant.MSG_ROLLBACK_ISSUED + csClientHost);
			}
		}
	}

	/**
	 * Get the MiscellaneousData information from database
	 * 
	 * @param aaObj Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getMiscData(Object aaObj) throws RTSException
	{
		GeneralSearchData laGeneralSearchData =
			(GeneralSearchData) aaObj;
		DatabaseAccess laDatabaseAccess = new DatabaseAccess();
		try
		{
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_START_DB_CALL_FOR_MISC
					+ csClientHost);
			laDatabaseAccess.beginTransaction();
			Miscellaneous laMiscellaneous =
				new Miscellaneous(laDatabaseAccess);
			MiscellaneousData laMiscellaneousData =
				new MiscellaneousData();
			laMiscellaneousData.setOfcIssuanceNo(
				laGeneralSearchData.getIntKey1());
			laMiscellaneousData.setSubstaId(
				laGeneralSearchData.getIntKey2());
			Vector lvMiscellaneousData =
				laMiscellaneous.qryMiscellaneous(laMiscellaneousData);
			if (lvMiscellaneousData.size() == CommonConstant.ELEMENT_1)
			{
				laMiscellaneousData =
					(MiscellaneousData) lvMiscellaneousData.get(
						CommonConstant.ELEMENT_0);
			}
			else
			{
				//throw exception
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					lvMiscellaneousData.size()
						+ CommonConstant.TXT_MISC_DATA_OFFICE_ERR
						+ laGeneralSearchData.getIntKey1()
						+ CommonConstant.TXT_AND_SUBSTA_COLON
						+ laGeneralSearchData.getIntKey2(),
					CommonConstant.STR_ERROR);
			}
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_FOR_MISC_SUCCESS
					+ csClientHost);
			return laMiscellaneousData;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				CommonConstant.MSG_DB_CALL_FOR_MISC_UNSUCCESS
					+ csClientHost);
			throw aeRTSEx;
		}
		finally
		{
			laDatabaseAccess.endTransaction(DatabaseAccess.NONE);
		}
	}

	/**
	 * Query the TransactionPayment Information
	 * 
	 * @param aaObj Object 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getTransPayment(Object aaObj) throws RTSException
	{
		TransactionPaymentData laTransactionPaymentData =
			(TransactionPaymentData) aaObj;
		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_TRANS_PYMNT_START
					+ csClientHost);
			Vector lvReturn = null;
			TransactionPayment laTransactionPayment =
				new TransactionPayment(laDBAccess);
			laDBAccess.beginTransaction();
			lvReturn =
				laTransactionPayment.qryTransactionPayment(
					laTransactionPaymentData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_TRANS_PYMNT_COMPLETE
					+ csClientHost);
			return lvReturn;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_TRANS_PYMNT_ERROR
					+ csClientHost);
			throw aeRTSEx;
		}
	}

	/**
	 * Calls VehicleInquiry to get MFVehicle from mainframe
	 * 
	 * @param aaObj Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getVeh(Object aaObj) throws RTSException
	{
		try
		{
			saLog4jCommonServerBus.info("Getting Vehicle");
			return caVehInq.getVeh(aaObj);
		}
		finally
		{
			saLog4jCommonServerBus.info("Got vehicle");
		}
	}

	/**
	 * Insert into RSPSPrint / ReprintSticker from RSPS Diskette
	 *
	 * renamed from reprintSticker 
	 *  
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object insRSPSData(Object aaData) throws RTSException
	{
		DatabaseAccess laDBAccess = null;
		boolean lbSuccessful = true;
		ReprintData laReprintData = new ReprintData();
		try
		{
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_REPRINT_STICKER_START
					+ csClientHost);
			laReprintData = (ReprintData) aaData;
			laDBAccess = new DatabaseAccess();
			laDBAccess.beginTransaction();
			RSPSPrint laRSPSPrint = new RSPSPrint(laDBAccess);
			laRSPSPrint.insRSPSPrint(laReprintData);
			// Realtime update of RTS_REPRNT_STKR
			int liReprntQty = laReprintData.getReprntQty();
			if (liReprntQty > 0)
			{
				if (laReprintData
					.getOrigin()
					.equals(CommonConstant.RSPS_SUB))
				{
					laReprintData.setOrigin(
						CommonConstant.REPORT_RSPS_SUB);
				}
				else
				{
					laReprintData.setOrigin(
						CommonConstant.REPORT_RSPS_DTA);
				}
				laReprintData.setPrntQty(liReprntQty);
				Vector lvVector = new Vector();
				lvVector.add(laReprintData);
				ReprintSticker laReprntStkr =
					new ReprintSticker(laDBAccess);
				laReprntStkr.insReprintSticker(lvVector);
			}
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_REPRINT_STICKER_SUCCESS);
			return laReprintData;
		}
		catch (RTSException aeRTSEx)
		{
			lbSuccessful = false;
			// OK if SQL0803  (duplicate) 
			// defect 10164 
			if (aeRTSEx
				.getDetailMsg() //.indexOf(CommonConstant.TXT_SQL0803)
				.indexOf(CommonConstant.DUPLICATE_KEY_EXCEPTION)
				> CommonConstant.NOT_FOUND)
			{
				// end defect 10164 
				return laReprintData;
			}
			else
			{
				Log.write(
					Log.APPLICATION,
					this,
					CommonConstant.MSG_DB_CALL_REPRINT_STICKER_FAILED);
				throw aeRTSEx;
			}
		}
		finally
		{
			if (lbSuccessful == true)
			{
				laDBAccess.endTransaction(DatabaseAccess.NONE);
			}
			else
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
		}
	}

	/**
	 * multiArchive
	 *  
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException
	 */
	private Object multiArchive(Object aaData) throws RTSException
	{
		return caVehInq.getMultiRecsFromArchive(aaData);
	}

	/**
	 * Post the Transaction to the database 
	 * 
	 * @param aaObj Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object postTrans(Object aaObj) throws RTSException
	{
		DatabaseAccess laDBAccess = null;
		boolean lbContainsDB = false;
		boolean lbIsSuccessful = false;

		//return vector for Inventory
		Vector lvInventory = new Vector();
		try
		{
			Vector lvInput = (Vector) aaObj;

			laDBAccess =
				(DatabaseAccess) lvInput.get(CommonConstant.ELEMENT_0);

			Vector lvVector =
				(Vector) lvInput.get(CommonConstant.ELEMENT_1);

			if (laDBAccess == null)
			{
				laDBAccess = new DatabaseAccess();
				laDBAccess.beginTransaction();
			}
			else
			{
				lbContainsDB = true;
			}
			for (int i = 0; i < lvVector.size(); i++)
			{
				TransactionCacheData laData =
					(TransactionCacheData) lvVector.get(i);
				Object laDBObj = laData.getObj();

				if (laDBObj instanceof FundFunctionTransactionData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_FUNDFUNC_START
							+ csClientHost);
					FundFunctionTransaction laTrans =
						new FundFunctionTransaction(laDBAccess);

					FundFunctionTransactionData laTransData =
						(FundFunctionTransactionData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans.delFundFunctionTransaction(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans.insFundFunctionTransaction(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								// defect 10239  
								// doesn't happen 
								// laTrans.updFundFunctionTransaction(
								// 	transData);
								// end defect 10239 
								break;
							}
						case TransactionCacheData.VOID :
							{
								laTrans.voidFundFunctionTransaction(
									laTransData);
								break;
							}
					}
				}
				else if (
					laDBObj
						instanceof InventoryFunctionTransactionData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_INV_FUNC_START
							+ csClientHost);

					InventoryFunctionTransaction laTrans =
						new InventoryFunctionTransaction(laDBAccess);

					InventoryFunctionTransactionData laTransData =
						(InventoryFunctionTransactionData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans
									.delInventoryFunctionTransaction(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans
									.insInventoryFunctionTransaction(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								// doesn't happen 
								break;
							}
						case TransactionCacheData.VOID :
							{
								laTrans
									.voidInventoryFunctionTransaction(
									laTransData);
								break;
							}
					}
				}
				else if (
					laDBObj
						instanceof MotorVehicleFunctionTransactionData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_MV_FUNC_START
							+ csClientHost);
					MotorVehicleFunctionTransaction laTrans =
						new MotorVehicleFunctionTransaction(laDBAccess);

					MotorVehicleFunctionTransactionData laTransData =
						(MotorVehicleFunctionTransactionData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans
									.delMotorVehicleFunctionTransaction(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans
									.insMotorVehicleFunctionTransaction(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								// doesn't happen 
								break;
							}
						case TransactionCacheData.VOID :
							{
								laTrans
									.voidMotorVehicleFunctionTransaction(
									laTransData);
								break;
							}
					}
				}
				// defect 10491 
				else if (laDBObj instanceof PermitTransactionData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_PRMT_TRANS_START
							+ csClientHost);

					PermitTransaction laTrans =
						new PermitTransaction(laDBAccess);

					PermitTransactionData laTransData =
						(PermitTransactionData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans.delPermitTransaction(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans.insPermitTransaction(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								// doesn't happen 
								break;
							}
						case TransactionCacheData.VOID :
							{
								laTrans.voidPermitTransaction(
									laTransData);
								break;
							}
					}
				}
				// end defect 10491 
				// defect 10700  
				else if (laDBObj instanceof SpecialPlatePermitData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_SPCL_PLT_PRMT_START
							+ csClientHost);

					SpecialPlatePermit laTrans =
						new SpecialPlatePermit(laDBAccess);

					SpecialPlatePermitData laTransData =
						(SpecialPlatePermitData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans.delSpecialPlatePermit(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans.insSpecialPlatePermit(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								// doesn't happen 
								break;
							}
						case TransactionCacheData.VOID :
							{
								// doesn't happen
								break;
							}
					}
				}
				// end defect 10700 
				// defect 9831
				else if (
					laDBObj instanceof DisabledPlacardTransactionData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant
							.MSG_DB_CALL_DSABLD_PLCRD_TRANS_START
							+ csClientHost);

					DisabledPlacardTransaction laTrans =
						new DisabledPlacardTransaction(laDBAccess);

					DisabledPlacardTransactionData laTransData =
						(DisabledPlacardTransactionData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans.delDsabldPlcrdTrans(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans.insDisabldPlcrdTrans(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								laTrans.updDsabldPlcrdTrans(
									laTransData);
								break;
							}
						case TransactionCacheData.VOID :
							{
								laTrans.voidDsabldPlcrdTrans(
									laTransData);
								break;
							}
					}
				}
				else if (
					laDBObj instanceof DisabledPlacardCustomerData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant
							.MSG_DB_CALL_DSABLD_PLCRD_CUST_START
							+ csClientHost);

					DisabledPlacardCustomer laTrans =
						new DisabledPlacardCustomer(laDBAccess);

					DisabledPlacardCustomerData laTransData =
						(DisabledPlacardCustomerData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.RESETINPROCESS :
							{
								laTrans
									.resetInProcsDisabledPlacardCustomer(
									laTransData);
								break;
							}
					}
				}
				// end defect 9831 
				// defect 9085 
				else if (
					laDBObj
						instanceof SpecialRegistrationFunctionTransactionData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_SR_FUNC_START
							+ csClientHost);

					SpecialRegistrationFunctionTransaction laTrans =
						new SpecialRegistrationFunctionTransaction(laDBAccess);

					SpecialRegistrationFunctionTransactionData transData =
						(SpecialRegistrationFunctionTransactionData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans
									.delSpecialRegistrationFunctionTransaction(
									transData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans
									.insSpecialRegistrationFunctionTransaction(
									transData);
								break;
							}
							// defect 10239 
						case TransactionCacheData.UPDATE :
							{
								// doesn't happen
								break;
							}
						case TransactionCacheData.VOID :
							{
								// doesn't happen
								break;
							}
							// end defect 10239 
					}
				}
				//	defect 9085 
				else if (
					laDBObj
						instanceof SpecialPlateTransactionHistoryData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_SP_TRANS_HSTRY_START
							+ csClientHost);
					SpecialPlateTransactionHistory laTrans =
						new SpecialPlateTransactionHistory(laDBAccess);

					SpecialPlateTransactionHistoryData laTransData =
						(SpecialPlateTransactionHistoryData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans
									.delSpecialPlateTransactionHistory(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans
									.insSpecialPlateTransactionHistory(
									laTransData);
								break;
							}
						case TransactionCacheData.VOID :
							{
								laTrans
									.voidSpecialPlateTransactionHistory(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								laTrans
									.updSpecialPlateTransactionHistory(
									laTransData);
								break;
							}
					}
				}
				// end defect 9085 

				// defect 10844 
				else if (
					laDBObj
						instanceof ModifyPermitTransactionHistoryData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant
							.MSG_DB_CALL_MOD_PRMT_TRANS_HSTRY_START
							+ csClientHost);

					ModifyPermitTransactionHistory laTransSQL =
						new ModifyPermitTransactionHistory(laDBAccess);

					ModifyPermitTransactionHistoryData laTransData =
						(ModifyPermitTransactionHistoryData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTransSQL
									.delModifyPermitTransactionHistory(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTransSQL
									.insModifyPermitTransactionHistory(
									laTransData);
								break;
							}
						case TransactionCacheData.VOID :
							{
								laTransSQL
									.voidModifyPermitTransactionHistory(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								laTransSQL
									.updModifyPermitTransactionHistory(
									laTransData);
								break;
							}
					}
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant
							.MSG_DB_CALL_MOD_PRMT_TRANS_HSTRY_SUCCESS
							+ csClientHost);
				}
				// end defect 10844 

				// defect 9972
				else if (laDBObj instanceof ElectronicTitleHistoryData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_ETTL_HSTRY_START
							+ csClientHost);

					ElectronicTitleHistory laTrans =
						new ElectronicTitleHistory(laDBAccess);

					ElectronicTitleHistoryData laTransData =
						(ElectronicTitleHistoryData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans.delElectronicTitleHistory(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans.insElectronicTitleHistory(
									laTransData);
								break;
							}
						case TransactionCacheData.VOID :
							{
								laTrans.voidElectronicTitleHistory(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								laTrans.updElectronicTitleHistory(
									laTransData);
								break;
							}
					}
				}
				// end defect 9972		

				else if (laDBObj instanceof TransactionData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_TRANS_START
							+ csClientHost);
					com.txdot.isd.rts.server.db.Transaction laTrans =
						new com.txdot.isd.rts.server.db.Transaction(
							laDBAccess);
					TransactionData laTransData =
						(TransactionData) laDBObj;
					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans.delTransaction(laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans.insTransaction(laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								// doesn't happen 
								break;
							}
						case TransactionCacheData.VOID :
							{
								laTrans.voidTransaction(laTransData);
								break;
							}
					}
				}
				else if (laDBObj instanceof TransactionFundsDetailData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_TR_FDS_DETAIL_START
							+ csClientHost);
					TransactionFundsDetail laTrans =
						new TransactionFundsDetail(laDBAccess);
					TransactionFundsDetailData laTransData =
						(TransactionFundsDetailData) laDBObj;
					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans.delTransactionFundsDetail(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans.insTransactionFundsDetail(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								// defect 10239
								// doesn't happen
								//laTrans.updTransactionFundsDetail(
								//	laTransData);
								// defect 10239 
								break;
							}
							// defect 10239
						case TransactionCacheData.VOID :
							{
								// doesn't happen
								break;
							}
							// end defect 10239 
					}
				}
				else if (laDBObj instanceof TransactionHeaderData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_TRANS_HDR_START
							+ csClientHost);
					TransactionHeader laTrans =
						new TransactionHeader(laDBAccess);
					TransactionHeaderData laTransData =
						(TransactionHeaderData) laDBObj;
					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans.delTransactionHeader(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans.insTransactionHeader(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								switch (laTransData.getDbTypeIndi())
								{
									case TransactionHeaderData
										.TIMESTMP :
										{
											laTrans
												.updTransTimeTransactionHeader(
												laTransData);
											break;
										}
									case TransactionHeaderData
										.TRANS_NAME :
										{
											laTrans
												.updTransactionHeader(
												laTransData);
											break;
										}
								}
								break;
							}
							// defect 10239 
						case TransactionCacheData.VOID :
							{
								// doesn't happen 
								break;
							}
							// end defect 10239 
					}
				}
				else if (
					laDBObj instanceof TransactionInventoryDetailData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_TR_INV_DTL_START
							+ csClientHost);
					TransactionInventoryDetail laTrans =
						new TransactionInventoryDetail(laDBAccess);

					TransactionInventoryDetailData laTransData =
						(TransactionInventoryDetailData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laTrans.delTransactionInventoryDetail(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans.insTransactionInventoryDetail(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								// defect 10239
								// doesn't happen
								// laTrans.updTransactionInventoryDetail(
								//		laTransData);
								// end defect 10239
								break;
							}
							// defect 10239 
						case TransactionCacheData.VOID :
							{
								// doesn't happen 
								break;
							}
							// end defect 10239 
					}
				}
				else if (laDBObj instanceof TransactionPaymentData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_TRANS_PYMNT_START
							+ csClientHost);

					TransactionPayment laTrans =
						new TransactionPayment(laDBAccess);

					TransactionPaymentData laTransData =
						(TransactionPaymentData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								// defect 10239
								// doesn't happen 
								// laTrans.delTransactionPayment(
								// 		laTransData);
								// end defect 10239 
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laTrans.insTransactionPayment(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								// doesn't happen 
								break;
							}
							// defect 10239 
						case TransactionCacheData.VOID :
							{
								// doesn't happen 
								break;
							}
							// end defect 10239 
					}
				}
				else if (laDBObj instanceof AssignedWorkstationIdsData)
				{
					AssignedWorkstationIdsData laTransData =
						(AssignedWorkstationIdsData) laDBObj;

					AssignedWorkstationIds laTrans =
						new AssignedWorkstationIds(laDBAccess);

					//always update
					laTrans.updAssignedWorkstationIds(laTransData);
				}
				// defect 10865 
				else if (laDBObj instanceof FraudLogData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_FRAUD_LOG_START
							+ csClientHost);

					FraudLog laTrans = new FraudLog(laDBAccess);

					FraudLogData laTransData = (FraudLogData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.INSERT :
							{
								laTrans.insFraudLog(laTransData);
								break;
							}

					}
					Log.write(
							Log.APPLICATION,
							this,
							CommonConstant
								.MSG_DB_CALL_FRAUD_LOG_SUCCESS
								+ csClientHost);
				}
				// end defect 10865 
				// defect 8087
				// For Future Use  
				// Only from SendCache 
				else if (laDBObj instanceof WorkstationStatusData)
				{
					//		WorkstationStatusData laTransData =
					//			(WorkstationStatusData) laDBObj;
					//
					//		WorkstationStatus laTrans =
					//			new WorkstationStatus(laDBAccess);
					//
					//		try
					//		{
					//			//always update
					//			laTrans.updWorkstationStatus(laTransData);
					//		}
					//		catch (RTSException aeRTSEx) 
					//		{
					//			// If there is bogus data in the cfg file, 
					//			//  do not throw Exception.   
					//		}
				}
				// end defect 8087 
				else if (laDBObj instanceof MFVehicleData)
				{
					//for internet
					switch (laData.getProcName())
					{
						case TransactionCacheData.UPDATE :
							{
								MFVehicleData laTransData =
									(MFVehicleData) laDBObj;
								InternetTransaction laItrntTrans =
									new InternetTransaction(laDBAccess);

								laItrntTrans.updateCntyStatusCd(
									TransCdConstant.IRENEW,
									com
										.txdot
										.isd
										.rts
										.services
										.webapps
										.util
										.constants
										.CommonConstants
										.APPROVED,
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								InternetTransaction laIRen =
									new InternetTransaction(laDBAccess);
								laIRen.updateTXInfo(laData);
								break;
							}
						case TransactionCacheData.DELETE :
							{
								MFVehicleData laTransData =
									(MFVehicleData) laDBObj;
								InternetTransaction laItrntTrans =
									new InternetTransaction(laDBAccess);
								laItrntTrans.updateCntyStatusCd(
									TransCdConstant.IRENEW,
									com
										.txdot
										.isd
										.rts
										.services
										.webapps
										.util
										.constants
										.CommonConstants
										.NEW,
									laTransData);
								break;
							}
							// defect 10239 
						case TransactionCacheData.VOID :
							{
								// doesn't happen 
								break;
							}
							// end defect 10239 
					}
				}

				else if (laDBObj instanceof ProcessInventoryData)
				{
					// delete Inventory when completing tranasctions
					// also update Inventory Status for SubCon
					InventoryServerBusiness laInventoryServerBusiness =
						new InventoryServerBusiness();
					ProcessInventoryData laTransData =
						(ProcessInventoryData) laDBObj;
					// if invlocidcd = null, make it an "x"
					if (laTransData.getInvLocIdCd() == null)
					{
						laTransData.setInvLocIdCd(
							InventoryConstant.DB_DOWN);
						laTransData.setInvId(
							CommonConstant.STR_SPACE_EMPTY);
					}
					// Transaction could have been created while 
					// SendCache processing
					if (laTransData
						.getInvLocIdCd()
						.equals(InventoryConstant.DB_DOWN))
					{
						laData.setFromSendCache(true);
					}
					//If the QTY is strange, set it to 0 and let 
					//inventory compute it.
					if (laTransData.getInvQty() > 1
						&& laData.isFromSendCache())
					{
						laTransData.setInvQty(0);
					}
					Vector lvIn = new Vector();
					lvIn.addElement(laDBAccess);
					lvIn.addElement(laTransData);
					Vector lvReturn = null;
					ProcessInventoryData laProcessInventoryData = null;
					switch (laData.getProcName())
					{
						// update for SubCon or canceling transactions
						case TransactionCacheData.UPDATE :
							{
								try
								{
									lvReturn =
										(
											Vector) laInventoryServerBusiness
												.processData(
											GeneralConstant.INVENTORY,
											InventoryConstant
												.UPDATE_INVENTORY_STATUS_CD,
											lvIn);
									if (lvReturn != null
										&& lvReturn.size()
											> CommonConstant.ELEMENT_0)
									{
										laProcessInventoryData =
											(
												ProcessInventoryData) lvReturn
													.get(
												CommonConstant
													.ELEMENT_0);
										lvInventory.addElement(
											laProcessInventoryData);
									}
									else
									{
										//check if inventory does not 
										//exist, then do not fail
										throw new RTSException(
											RTSException
												.FAILURE_MESSAGE,
											CommonConstant
												.MSG_POSTTRANS_UPDATE_FAILED
												+ csClientHost,
											CommonConstant.STR_ERROR);
									}
								}
								catch (RTSException aeRTSEx)
								{
									if (aeRTSEx.getCode()
										!= ErrorsConstant
											.ERR_NUM_NO_RECORDS_IN_DB)
									{
										//throw e;
									}
								}
								break;
							}
							// delete Inventory 
						case TransactionCacheData.DELETE :
							{
								// If this is a printable item, do not 
								// have InventoryServerBusiness deal 
								// with it.
								if (StickerPrintingUtilities
									.isStickerPrintable(laTransData))
								{
									break;
								}
								// Only query DB if we are fromSendCache
								if (laData.isFromSendCache())
								{
									// Read from the DB to see if item 
									// is in DB
									try
									{
										lvReturn =
											(
												Vector) laInventoryServerBusiness
													.processData(
												GeneralConstant
													.INVENTORY,
												InventoryConstant
													.GET_INVENTORY_RANGE_IN_DB,
												lvIn);
									}
									catch (RTSException aeRTSEx)
									{
										// don't throw exceptions now - 
										// we can't have "X" on the 
										// TR_INV_DETAIL database
										// so everything must be changed
										// to a "U" and then update the 
										// TR_INV_DETAIL
										// we're masking that the record
										//  wasn't found
										// Defect 5259 - Must throw 
										// Exception (e.g. SQL0911) 
										if (aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_NO_RECORDS_IN_DB
											&& aeRTSEx.getCode()
												!= ErrorsConstant
													.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN
											&& aeRTSEx.getCode()
												!= ErrorsConstant
													.ERR_NUM_ITEM_ON_HOLD
											&& aeRTSEx.getCode()
												!= ErrorsConstant
													.ERR_NUM_INCOMPLETE_RANGE
											&& aeRTSEx.getCode()
												!= ErrorsConstant
													.ERR_NUM_PATRN_SEQ_NOT_RIGHT)
										{
											throw aeRTSEx;
										}
									}
									laProcessInventoryData =
										laTransData;
									// if it wasn't found, update the 
									// InvLocIdCd to U
									if (lvReturn == null
										|| lvReturn.size()
											== CommonConstant.ELEMENT_0)
									{
										laProcessInventoryData
											.setInvLocIdCd(
											InventoryConstant
												.NOT_FOUND);
										laProcessInventoryData
											.setInvId(
											CommonConstant
												.STR_SPACE_ONE);
										lvInventory.addElement(
											laProcessInventoryData);
									}
									else
									{
										laProcessInventoryData
											.setInvLocIdCd(
											(
												(ProcessInventoryData) lvReturn
												.get(
													CommonConstant
														.ELEMENT_0))
												.getInvLocIdCd());
										laProcessInventoryData
											.setInvId(
											(
												(ProcessInventoryData) lvReturn
												.get(
													CommonConstant
														.ELEMENT_0))
												.getInvId());
									}
									// Update record on the 
									// RTS_TR_INV_DETAIL table
									TransactionInventoryDetail laTransDetail =
										new TransactionInventoryDetail(laDBAccess);
									TransactionInventoryDetailData laTransDetailData =
										new TransactionInventoryDetailData();
									laTransDetailData.setInvLocIdCd(
										laProcessInventoryData
											.getInvLocIdCd());
									laTransDetailData.setInvId(
										laProcessInventoryData
											.getInvId());
									laTransDetailData.setOfcIssuanceNo(
										laProcessInventoryData
											.getOfcIssuanceNo());
									laTransDetailData.setSubstaId(
										laProcessInventoryData
											.getSubstaId());
									laTransDetailData.setTransAMDate(
										laProcessInventoryData
											.getTransAMDate());

									// defect 9116
									// TransWsId is now an int
									//if (laProcessInventoryData
									//	.getTransWsId()
									//	== null
									//	|| laProcessInventoryData
									//		.getTransWsId()
									//		.trim()
									//		.equals(
									//		CommonConstant
									//			.STR_SPACE_EMPTY))
									//{
									//	laTransDetailData.setTransWsId(
									//		0);
									//}
									//else
									//{
									//	laTransDetailData.setTransWsId(
									//		Integer.parseInt(
									//			laProcessInventoryData
									//				.getTransWsId()));
									//}
									laTransDetailData.setTransWsId(
										laProcessInventoryData
											.getTransWsId());
									// end defect 9116

									laTransDetailData.setTransTime(
										laProcessInventoryData
											.getTransTime());
									laTransDetailData.setItmCd(
										laProcessInventoryData
											.getItmCd());
									laTransDetailData.setInvItmYr(
										laProcessInventoryData
											.getInvItmYr());
									laTransDetailData.setInvItmNo(
										laProcessInventoryData
											.getInvItmNo());
									laTransDetailData.setInvEndNo(
										laProcessInventoryData
											.getInvItmEndNo());
									laTransDetail
										.updTransactionInventoryDetailForCache(
										laTransDetailData);

								}
								// end fromSendCache
								// Delete the Inventory
								laProcessInventoryData = laTransData;
								try
								{
									lvInventory.addElement(
										laProcessInventoryData);
									laInventoryServerBusiness
										.processData(
										GeneralConstant.INVENTORY,
										InventoryConstant
											.DELETE_FOR_ISSUE_INVENTORY,
										lvIn);
								}
								catch (RTSException aeRTSEx)
								{
									// eat the exception if this is 182, 593, 594, 605, or 607
									if (aeRTSEx.getCode()
										!= ErrorsConstant
											.ERR_NUM_NO_RECORDS_IN_DB
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_ITEM_ON_HOLD
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_INCOMPLETE_RANGE
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_PATRN_SEQ_NOT_RIGHT)
									{
										throw aeRTSEx;
									}
								}
								// end defect 6077
								break;
							} //end ProcessInventoryData delete
					}
				} // end ProcessInventoryData
				// defect 9085
				// Do not need the !ProcessInventoryData; just in case
				// moved within method  
				else if (
					laDBObj instanceof InventoryAllocationData
						&& !(laDBObj instanceof ProcessInventoryData))
				{
					InventoryServerBusiness laInventoryServerBusiness =
						new InventoryServerBusiness();

					InventoryAllocationData laTransData =
						(InventoryAllocationData) laDBObj;

					InventoryAllocationData laInventoryAllocationData =
						null;

					switch (laData.getProcName())
					{
						// Update INV_VIRTUAL when completing transaction
						case TransactionCacheData.UPDATE :
							{
								try
								{
									// defect 9116
									// pass the DBA to ISB.
									Vector lvInvRequest = new Vector(2);
									lvInvRequest.add(laDBAccess);
									lvInvRequest.add(laTransData);
									laInventoryAllocationData =
										(
											InventoryAllocationData) laInventoryServerBusiness
												.processData(
											GeneralConstant.INVENTORY,
											InventoryConstant
												.INV_VI_ITEM_APPLICATION_COMPLETE,
											lvInvRequest);
									// laTransData);
									// end defect 9116

									if (laInventoryAllocationData
										== null)
									{
										//check if inventory does not 
										//exist, then do not fail
										throw new RTSException(
											RTSException
												.FAILURE_MESSAGE,
											CommonConstant
												.MSG_POSTTRANS_UPDATE_FAILED
												+ csClientHost,
											CommonConstant.STR_ERROR);
									}
								}
								catch (RTSException aeRTSEx)
								{
									if (aeRTSEx.getCode()
										!= ErrorsConstant
											.ERR_NUM_NO_RECORDS_IN_DB
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_ITEM_ON_HOLD
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_INCOMPLETE_RANGE
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_PATRN_SEQ_NOT_RIGHT
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_VI_NEXTITEM_UNAVAILABLE)
									{
										throw aeRTSEx;
									}
								}
								break;
							}
							// Update INV_VIRTUAL TransTime when adding transaction
						case TransactionCacheData.UPDVITRANSTIME :
							{
								try
								{
									// defect 9116
									// pass the DBA to ISB.
									Vector lvInvRequest = new Vector(2);
									lvInvRequest.add(laDBAccess);
									lvInvRequest.add(laTransData);
									laInventoryAllocationData =
										(
											InventoryAllocationData) laInventoryServerBusiness
												.processData(
											GeneralConstant.INVENTORY,
											InventoryConstant
												.INV_VI_UPDATE_TRANSTIME,
											lvInvRequest);
									//laTransData);
									// end defect 9116

									if (laInventoryAllocationData
										== null)
									{
										//check if inventory does not 
										//exist, then do not fail
										throw new RTSException(
											RTSException
												.FAILURE_MESSAGE,
											CommonConstant
												.MSG_POSTTRANS_UPDATE_FAILED
												+ csClientHost,
											CommonConstant.STR_ERROR);
									}
								}
								catch (RTSException aeRTSEx)
								{
									if (aeRTSEx.getCode()
										!= ErrorsConstant
											.ERR_NUM_NO_RECORDS_IN_DB
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_ITEM_ON_HOLD
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_INCOMPLETE_RANGE
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_PATRN_SEQ_NOT_RIGHT
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_VI_NEXTITEM_UNAVAILABLE)
									{
										throw aeRTSEx;
									}
								}
								break;
							}
							// end defect 9085
							// Update INV_VIRTUAL when cancelling a transaction 
						case TransactionCacheData.DELETE :
							{
								try
								{
									// defect 9116
									// pass the DBA to ISB.
									Vector lvInvRequest = new Vector(2);
									lvInvRequest.add(laDBAccess);
									lvInvRequest.add(laTransData);
									laInventoryAllocationData =
										(
											InventoryAllocationData) laInventoryServerBusiness
												.processData(
											GeneralConstant.INVENTORY,
											InventoryConstant
												.INV_VI_RELEASE_HOLD,
											lvInvRequest);
									//laTransData);
									// end defect 9116

									if (laInventoryAllocationData
										== null)
									{
										//check if inventory does not 
										//exist, then do not fail
										throw new RTSException(
											RTSException
												.FAILURE_MESSAGE,
											CommonConstant
												.MSG_POSTTRANS_UPDATE_FAILED
												+ csClientHost,
											CommonConstant.STR_ERROR);
									}
								}
								catch (RTSException aeRTSEx)
								{
									if (aeRTSEx.getCode()
										!= ErrorsConstant
											.ERR_NUM_NO_RECORDS_IN_DB
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_VI_NEXTITEM_UNAVAILABLE
										&& aeRTSEx.getCode()
											!= ErrorsConstant
												.ERR_NUM_VI_ITEM_NOT_RESERVED)
									{
										// defect 10307 
										if (laData.isFromSendCache())
										{
											String lsTransId =
												UtilityMethods
													.getTransId(
													laTransData
														.getOfcIssuanceNo(),
													laTransData
														.getTransWsId(),
													laTransData
														.getTransAmDate(),
													laTransData
														.getTransTime());
											Log.write(
												Log.SQL_EXCP,
												this,
												"SPAPPL Delete Error Ignored:  TransId "
													+ lsTransId
													+ "  InvItmNo "
													+ laTransData
														.getInvItmNo()
													+ "  Error Code "
													+ aeRTSEx.getCode());
										}
										else
										{
											throw aeRTSEx;
										}
										// end defect 10307
									}
								}
								break;
							}
					}
				}
				// end defect 9085 
				else if (
					laDBObj instanceof SubcontractorRenewalCacheData)
				{
					SubcontractorRenewalServerBusiness laSubcontractorRenewalServerBusiness =
						new SubcontractorRenewalServerBusiness();

					SubcontractorRenewalCacheData laTransData =
						(SubcontractorRenewalCacheData) laDBObj;

					Vector lvIn = new Vector();
					lvIn.addElement(laDBAccess);
					lvIn.addElement(laTransData);
					try
					{
						switch (laData.getProcName())
						{
							//cancel selected subcon
							case (TransactionCacheData.UPDATE) :
								{
									laSubcontractorRenewalServerBusiness
										.processData(
										GeneralConstant.REGISTRATION,
										RegistrationConstant
											.DEL_SELECTED_SUBCON_RENWL_RECORD,
										lvIn);
									break;
									//cancel Subcon
								}
							case (TransactionCacheData.DELETE) :
								{
									laSubcontractorRenewalServerBusiness
										.processData(
										GeneralConstant.REGISTRATION,
										RegistrationConstant
											.CANCEL_SUBCON,
										lvIn);
									break;
									//end Subcon Trans (by releasing 
									//held inventory)
								}
							case (TransactionCacheData.INSERT) :
								{
									laSubcontractorRenewalServerBusiness
										.processData(
										GeneralConstant.REGISTRATION,
										RegistrationConstant
											.CANCEL_HELD_SUBCON,
										lvIn);
								}
						}
					}
					catch (RTSException aeRTSEx)
					{
						if (aeRTSEx.getCode()
							!= ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB
							&& aeRTSEx.getCode()
								!= ErrorsConstant
									.ERR_NUM_ITEM_ON_HOLD_CAN_NOT_RETURN
							&& aeRTSEx.getCode()
								!= ErrorsConstant.ERR_NUM_ITEM_ON_HOLD
							&& aeRTSEx.getCode()
								!= ErrorsConstant
									.ERR_NUM_INCOMPLETE_RANGE)
						{
							throw aeRTSEx;
						}
					}
				}
				else if (laDBObj instanceof TransactionKey)
				{
					//release inventory
					releaseInvForDeleteTrans(
						(TransactionKey) laDBObj,
						laDBAccess);
				}

				// defect 7705
				// this was never used
				//else if (laDBObj instanceof CompleteTransactionData)
				//{
				//	//for internet group
				//	//CompleteTransactionData laTransData =
				//	//	(CompleteTransactionData) laDBObj;
				//	//Internet people is writing this method!!!!
				//}
				// end defect 7705

				// Modify call to reference renamed insRSPSData()
				else if (laDBObj instanceof ReprintData)
				{
					//reprintSticker(dbObj);
					insRSPSData(laDBObj);
				}
				// Use ReprintStickerTransData vs. HashMap
				else if (laDBObj instanceof ReprintStickerTransData)
				{
					MiscServerBusiness laMSB = new MiscServerBusiness();
					laMSB.processData(
						GeneralConstant.MISC,
						MiscellaneousConstant.UPDATE_REPRINT_STICKER,
						laDBObj);
				}
				else if (laDBObj instanceof InternetTransactionData)
				{
					// This is to process InternetTransData
					InternetTransactionData laInternetTransData =
						(InternetTransactionData) laDBObj;
					InternetTransaction laInternetRegRenProc =
						new InternetTransaction(laDBAccess);
					laInternetRegRenProc.updateRenewal(
						laInternetTransData);
				}
				//	defect 8900 
				else if (laDBObj instanceof ExemptAuditData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_EXMPTAUDIT_START
							+ csClientHost);

					ExemptAudit laExmptAudit =
						new ExemptAudit(laDBAccess);

					ExemptAuditData laTransData =
						(ExemptAuditData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.DELETE :
							{
								laExmptAudit.delExemptAudit(
									laTransData);
								break;
							}
						case TransactionCacheData.INSERT :
							{
								laExmptAudit.insExemptAudit(
									laTransData);
								break;
							}
						case TransactionCacheData.VOID :
							{
								laExmptAudit.voidExemptAudit(
									laTransData);
								break;
							}
						case TransactionCacheData.UPDATE :
							{
								laExmptAudit.updExemptAudit(
									laTransData);
								break;
							}
					}
				}
				//end defect 8900
				// defect 10994 
				else if (laDBObj instanceof LogonFunctionTransactionData)
				{
					Log.write(
						Log.APPLICATION,
						this,
						CommonConstant.MSG_DB_CALL_LOG_FUNC_TRANS_START
							+ csClientHost);

					LogonFunctionTransaction laLogFuncTrans =
						new LogonFunctionTransaction(laDBAccess);

					LogonFunctionTransactionData laTransData =
						(LogonFunctionTransactionData) laDBObj;

					switch (laData.getProcName())
					{
						case TransactionCacheData.INSERT :
							{
								laLogFuncTrans.insLogonFunctionTransaction(
									laTransData);
								break;
							}
						}
					Log.write(
							Log.APPLICATION,
							this,
							CommonConstant
								.MSG_DB_CALL_LOG_FUNC_TRANS_SUCCESS
								+ csClientHost);
				}
				//end defect 10994
			}
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_ALL_SUCCESS + csClientHost);
			lbIsSuccessful = true;
			lvInventory.trimToSize();
			if (lvInventory.size() > CommonConstant.ELEMENT_0)
			{
				return lvInventory;
			}
			else
			{
				return aaObj;
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				CommonConstant.MSG_FAILED_DB_CALL + csClientHost);
			throw aeRTSEx;
		}
		finally
		{
			if (lbIsSuccessful && !lbContainsDB)
			{
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			}
			else if (!lbIsSuccessful)
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				Log.write(
					Log.SQL_EXCP,
					this,
					CommonConstant.MSG_ROLLBACK_ISSUED + csClientHost);
			}
		}
	}

	/**
	 * Dispatch method calls according to function ids
	 *  
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaObj Object
	 * @return Object 
	 * @throws RTSException
	 */
	public Object processData(
		int aiModuleName,
		int aiFunctionId,
		Object aaObj)
		throws RTSException
	{
		switch (aiFunctionId)
		{
			case CommonConstant.GET_VEH :
				{
					return getVeh(aaObj);
				}
			case CommonConstant.GET_MISC_DATA :
				{
					return getMiscData(aaObj);
				}
			case CommonConstant.POST_TRANS :
				{
					return postTrans(aaObj);
				}
			case CommonConstant.CANCEL_TRANS :
				{
					return deleteTrans(aaObj);
				}
			case CommonConstant.GET_TRANS_PAYMENT :
				{
					return getTransPayment(aaObj);
				}
			case CommonConstant.CANCEL_SELECTED_TRANS :
				{
					return deleteSelectedTrans(aaObj);
				}
			case CommonConstant.PROC_INTERNET_ADDR_CHNG :
				{
					return procsInternetAddrChng(aaObj);
				}
				// defect 9085 
			case CommonConstant.PROC_IAPPL :
				{
					return procsIAPPL(aaObj);
				}
				// end defect 9085 
				// defect 9711
			case CommonConstant.PROC_VENDOR_PLATES :
				{
					return procsVendorPlates(aaObj);
				}
				// end defect 9711
				// defect 9502 
			case CommonConstant.PROC_V21VTN :
				{
					return procsV21VTN(aaObj);
				}
				// end defect 9502
				// defect 9582 
			case CommonConstant.PROC_V21PLD :
				{
					return procsV21PLD(aaObj);
				}
				// end defect 9582 
			case CommonConstant.PROC_TRANS :
				{
					return procsTrans(aaObj);
				}
			case CommonConstant.APP_MF_ERR_LOG :
				{
					return appMfErrLog(aaObj);
				}
			case CommonConstant.MULTI_ARCHIVE :
				{
					return multiArchive(aaObj);
				}
			case CommonConstant.REPRINT_STICKER :
				{
					// Modify call to reference renamed insRSPSData()
					return insRSPSData(aaObj);
				}
			case CommonConstant.PROC_EREMINDER :
				{
					return procChngeEReminder(aaObj);
				}
			case CommonConstant.PROC_WEB_SUB :
				{
					return procsWebSubTrans(aaObj);
				}
			default :
				{
					Log.write(
						Log.SQL_EXCP,
						this,
						CommonConstant.TXT_FUNCTION_NOT_FOUND
							+ aiModuleName
							+ CommonConstant.STR_SPACE_ONE
							+ aiFunctionId
							+ CommonConstant.STR_SPACE_ONE
							+ csClientHost);
					return null;
				}
		}
	}

	/**
	 * This method creates the TransactionHeaderData object for
	 * IADDR and IRNR transactions.  If first transaction of the day, 
	 * the TransactionHeader is inserted. 
	 * 
	 * @throws RTSException
	 *  
	 */
	private TransactionHeaderData setupItrntBackOfcTransHdr(int aiWorkStationId)
		throws RTSException
	{
		// Setup Back Office TransHeader for Internet IRNR/IADDR
		RTSDate laRTSDate = new RTSDate();
		int liOfcIssuanceno = SystemProperty.getOfficeIssuanceNo();
		OfficeIdsData laOfficeIdsData =
			OfficeIdsCache.getOfcId(liOfcIssuanceno);
		if (laOfficeIdsData == null)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				CommonConstant.MSG_OFFICEIDS_NULL,
				CommonConstant.STR_ERROR);
		}
		TransactionHeaderData laTransHdrData =
			new TransactionHeaderData();
		laTransHdrData.setOfcIssuanceNo(liOfcIssuanceno);
		laTransHdrData.setOfcIssuanceCd(
			laOfficeIdsData.getOfcIssuanceCd());
		laTransHdrData.setSubstaId(SystemProperty.getSubStationId());
		// defect 10610
		//laTransHdrData.setTransWsId(SystemProperty.getWorkStationId());
		laTransHdrData.setTransWsId(aiWorkStationId);
		// end defect 10610
		laTransHdrData.setTransAMDate(laRTSDate.getAMDate());
		laTransHdrData.setCustSeqNo(0);
		laTransHdrData.setVersionCd(
			SystemProperty.getMainFrameVersion());
		laTransHdrData.setTransEmpId(SystemProperty.getCurrentEmpId());
		laTransHdrData.setCashWsId(SystemProperty.getWorkStationId());
		laTransHdrData.setTransName(
			CommonConstant.ITRNT_BACKOFFICE_TRANSNAME);
		laTransHdrData.setFeeSourceCd(0);
		laTransHdrData.setTransTimestmp(laRTSDate);

		// Insert Back Office TransHeader for Internet IRNR/IADDR
		// if not previously inserted 
		// defect 8989
		DatabaseAccess laDatabaseAccess = new DatabaseAccess();
		int liNumRows = Integer.MIN_VALUE;

		try
		{
			TransactionHeader laTransHdr =
				new TransactionHeader(laDatabaseAccess);

			laDatabaseAccess.beginTransaction();

			liNumRows =
				laTransHdr.insItrntBackOfcTransHdr(laTransHdrData);

			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);

			if (liNumRows == CommonConstant.ELEMENT_1)
			{
				Log.write(
					Log.APPLICATION,
					this,
					CommonConstant.TXT_INSERT_IADDR_IRNR_TRANS_HDR
						+ laRTSDate.getAMDate());
			}
		}
		catch (RTSException aeRTSEx1)
		{
			laDatabaseAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.TXT_INSERT_IADDR_IRNR_TRANS_HDR_ERROR
					+ laRTSDate.getAMDate());
		}
		// end defect 8989
		finally
		{
			return laTransHdrData;
		}
	}
	/**
	 * This method creates the TransactionHeaderData object for
	 * IAPPL transactions. If first transaction of the day, 
	 * the TransactionHeader is inserted. 
	 * 
	 * @throws RTSException
	 *  
	 */
	private TransactionHeaderData setupIAPPLBackOfcTransHdr()
		throws RTSException
	{
		// Setup Back Office TransHeader for Internet IRNR/IADDR
		RTSDate laRTSDate = new RTSDate();
		OfficeIdsData laOfficeIdsData =
			OfficeIdsCache.getOfcId(
				SpecialPlatesConstant.IAPPL_OFCISSUANCENO);
		if (laOfficeIdsData == null)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				CommonConstant.MSG_OFFICEIDS_NULL,
				CommonConstant.STR_ERROR);
		}
		TransactionHeaderData laTransHdrData =
			new TransactionHeaderData();
		laTransHdrData.setOfcIssuanceNo(
			SpecialPlatesConstant.IAPPL_OFCISSUANCENO);
		laTransHdrData.setOfcIssuanceCd(
			laOfficeIdsData.getOfcIssuanceCd());
		laTransHdrData.setSubstaId(
			SpecialPlatesConstant.IAPPL_SUBSTAID);
		laTransHdrData.setTransWsId(
			SpecialPlatesConstant.IAPPL_TRANSWSID);
		laTransHdrData.setTransAMDate(laRTSDate.getAMDate());
		laTransHdrData.setCustSeqNo(0);
		laTransHdrData.setVersionCd(
			SystemProperty.getMainFrameVersion());
		laTransHdrData.setTransEmpId(SpecialPlatesConstant.IAPPL_EMPID);
		laTransHdrData.setCashWsId(
			SpecialPlatesConstant.IAPPL_TRANSWSID);
		laTransHdrData.setTransName(
			SpecialPlatesConstant.IAPPL_BACK_OFFICE_TRANS);
		laTransHdrData.setFeeSourceCd(0);
		laTransHdrData.setTransTimestmp(laRTSDate);

		// Insert Back Office TransHeader for Internet IAPPL 
		DatabaseAccess laDatabaseAccess = new DatabaseAccess();
		int liNumRows = Integer.MIN_VALUE;

		try
		{
			TransactionHeader laTransHdr =
				new TransactionHeader(laDatabaseAccess);

			laDatabaseAccess.beginTransaction();

			liNumRows =
				laTransHdr.insItrntBackOfcTransHdr(laTransHdrData);

			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);

			if (liNumRows == CommonConstant.ELEMENT_1)
			{
				Log.write(
					Log.APPLICATION,
					this,
					"Insert IAPPL Transaction Header"
						+ laRTSDate.getAMDate());
			}
		}
		catch (RTSException aeRTSEx1)
		{
			laDatabaseAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.write(
				Log.APPLICATION,
				this,
				"Insert IAPPL Transaction Header Error"
					+ laRTSDate.getAMDate());
		}
		finally
		{
			return laTransHdrData;
		}
	}

	/**
	 * This method creates the TransactionHeaderData object for
	 * VPAPPL/VPAPPR transactions. If first transaction of the day, 
	 * the TransactionHeader is inserted. 
	 * 
	 * @throws RTSException
	 *  
	 */
	private TransactionHeaderData setupVendorBackOfcTransHdr()
		throws RTSException
	{
		// Setup Back Office TransHeader for Internet IRNR/IADDR
		RTSDate laRTSDate = new RTSDate();
		// defect 9711
		//		OfficeIdsData laOfficeIdsData =
		//			OfficeIdsCache.getOfcId(
		//				SpecialPlatesConstant.VENDOR_OFCISSUANCENO);
		OfficeIdsData laOfficeIdsData =
			OfficeIdsCache.getOfcId(
				SystemProperty.getVpOfcIssuanceNo());
		// end defect 9711		
		if (laOfficeIdsData == null)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				CommonConstant.MSG_OFFICEIDS_NULL,
				CommonConstant.STR_ERROR);
		}
		TransactionHeaderData laTransHdrData =
			new TransactionHeaderData();
		// defect 9711	
		//		laTransHdrData.setOfcIssuanceNo(
		//			SpecialPlatesConstant.VENDOR_OFCISSUANCENO);
		laTransHdrData.setOfcIssuanceNo(
			SystemProperty.getVpOfcIssuanceNo());
		// end defect 9711	
		laTransHdrData.setOfcIssuanceCd(
			laOfficeIdsData.getOfcIssuanceCd());
		laTransHdrData.setSubstaId(
			SpecialPlatesConstant.VENDOR_SUBSTAID);
		// defect 9711	
		//		laTransHdrData.setTransWsId(
		//			SpecialPlatesConstant.VENDOR_TRANSWSID);
		laTransHdrData.setTransWsId(SystemProperty.getVpWsId());
		// end defect 9711	
		laTransHdrData.setTransAMDate(laRTSDate.getAMDate());
		laTransHdrData.setCustSeqNo(0);
		laTransHdrData.setVersionCd(
			SystemProperty.getMainFrameVersion());
		laTransHdrData.setTransEmpId(
			SpecialPlatesConstant.VENDOR_EMPID);
		laTransHdrData.setCashWsId(
			SpecialPlatesConstant.VENDOR_TRANSWSID);
		laTransHdrData.setTransName(
			SpecialPlatesConstant.VENDOR_BACK_OFFICE_TRANS);
		laTransHdrData.setFeeSourceCd(0);
		laTransHdrData.setTransTimestmp(laRTSDate);

		// Insert Back Office TransHeader for Internet VPAPPL 
		DatabaseAccess laDatabaseAccess = new DatabaseAccess();
		int liNumRows = Integer.MIN_VALUE;

		try
		{
			TransactionHeader laTransHdr =
				new TransactionHeader(laDatabaseAccess);

			laDatabaseAccess.beginTransaction();

			liNumRows =
				laTransHdr.insItrntBackOfcTransHdr(laTransHdrData);

			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);

			if (liNumRows == CommonConstant.ELEMENT_1)
			{
				Log.write(
					Log.APPLICATION,
					this,
					"Insert VPAPPL Transaction Header"
						+ laRTSDate.getAMDate());
			}
		}
		catch (RTSException aeRTSEx1)
		{
			laDatabaseAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.write(
				Log.APPLICATION,
				this,
				"Insert VPAPPL Transaction Header Error"
					+ laRTSDate.getAMDate());
		}
		finally
		{
			return laTransHdrData;
		}
	}

	/**
	 * 
	 * Process Internet Special Plates Application
	 * 
	 * @param aaObj
	 * @return Object 
	 * @throws RTSException
	 */
	private Object procsIAPPL(Object aaObj) throws RTSException
	{
		if (!(aaObj instanceof SpecialPlateItrntTransData))
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				CommonConstant.TXT_INPUT_TYPE_INVALID,
				CommonConstant.STR_ERROR);
		}
		CompleteTransactionData laCompleteTransactionData =
			new CompleteTransactionData();

		laCompleteTransactionData.setupForIAPPL(
			(SpecialPlateItrntTransData) aaObj);

		Vector lvVector = new Vector();

		com
			.txdot
			.isd
			.rts
			.services
			.common
			.Transaction laTransactionEngine =
			new com.txdot.isd.rts.services.common.Transaction(
				TransCdConstant.IAPPL);

		// Since the IAPPL transactions are run from the server,
		// need to see if this  is the first transaction of the day.  
		// If it is, then set up the first  transaction of the day.

		TransactionHeaderData laTransactionHeaderData =
			setupIAPPLBackOfcTransHdr();

		//Move data to Trans Table
		laCompleteTransactionData =
			laTransactionEngine.populateTrans(
				lvVector,
				laCompleteTransactionData,
				laTransactionHeaderData);

		//Move data to TrFdsDetail/FundFuncTrns 
		laTransactionEngine.populateFundFuncTrnsTrFdsDetail(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		//Move data to MvFuncTrans
		laTransactionEngine.populateMvTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		// Move data to SRFuncTrans
		laTransactionEngine.populateSRFuncTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		//Start a Transaction
		DatabaseAccess laDBAccess = new DatabaseAccess();
		laDBAccess.beginTransaction();
		Vector lvInputTrans = new Vector();
		lvInputTrans.addElement(laDBAccess);
		lvInputTrans.addElement(lvVector);

		boolean lbIsSuccessful = false;
		try
		{
			postTrans(lvInputTrans);
			SpecialPlateItrntTransData laSPItrntTransData =
				(SpecialPlateItrntTransData) aaObj;
			completeIAPPLInv(
				laDBAccess,
				laSPItrntTransData,
				laTransactionEngine.getTransTime());
			// Update the Internet Special Plt Transaction w/ 
			// Transaction Time and Then again as completed
			InternetSpecialPlateTransaction laItrntSPTransAccess =
				new InternetSpecialPlateTransaction(laDBAccess);
			laSPItrntTransData.setAuditTrailTransId(
				UtilityMethods.getTransId(
					laTransactionHeaderData.getOfcIssuanceNo(),
					laTransactionHeaderData.getTransWsId(),
					laTransactionHeaderData.getTransAMDate(),
					laTransactionEngine.getTransTime()));
			laItrntSPTransAccess.completeSPItrntTrans(
				laSPItrntTransData);

			lbIsSuccessful = true;

			return new Boolean(lbIsSuccessful);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"IAPPL transaction failed " + csClientHost);
			throw aeRTSEx;
		}
		finally
		{
			if (lbIsSuccessful)
			{
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			}
			else
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				Log.write(
					Log.SQL_EXCP,
					this,
					CommonConstant.MSG_ROLLBACK_ISSUED + csClientHost);
			}

		}
	}

	/**
	 * 
	 * Process Vendor Special Plates Application
	 * 
	 * @param aaObj
	 * @return Object 
	 * @throws RTSException
	 */
	private Object procsVendorPlates(Object aaObj) throws RTSException
	{
		// defect 10401
		// When called from RtsTransService, we need to pass in a DBA.
		DatabaseAccess laDBAccess;
		boolean lbPassedDBA = false;
		SpecialPlateItrntTransData laObject;
		if (aaObj instanceof Vector)
		{
			laDBAccess = (DatabaseAccess) ((Vector) aaObj).elementAt(0);
			lbPassedDBA = true;
			laObject =
				(SpecialPlateItrntTransData)
					((Vector) aaObj).elementAt(
					1);
		}
		else if (aaObj instanceof SpecialPlateItrntTransData)
		{
			laDBAccess = new DatabaseAccess();
			laDBAccess.beginTransaction();
			laObject = (SpecialPlateItrntTransData) aaObj;
		}
		else
		{
			// end defect 10401
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				CommonConstant.TXT_INPUT_TYPE_INVALID,
				CommonConstant.STR_ERROR);
		}
		CompleteTransactionData laCompleteTransactionData =
			new CompleteTransactionData();

		// defect 10401
		laCompleteTransactionData.setupForVENDOR(laObject);
		//	(SpecialPlateItrntTransData) aaObj);
		// end defect 10401

		Vector lvVector = new Vector();

		com
			.txdot
			.isd
			.rts
			.services
			.common
			.Transaction laTransactionEngine =
			null;

		TransactionHeaderData laTransactionHeaderData = null;

		//		SpecialPlateItrntTransData laSpecialPlateItrntTransData =
		//			(SpecialPlateItrntTransData) aaObj;

		// Since the VPAPPL/VPAPPR transactions are all gathered once a 
		// day, need to see if this  is the first vENDOR transaction of 
		// the day.  
		// If it is, then set up the first  transaction of the day.
		// for both VPAPPL/VPAPPR transactions, using VPAPPL as the 
		// transcd
		// defect 9711
		laTransactionEngine =
			new com.txdot.isd.rts.services.common.Transaction(
				laCompleteTransactionData.getTransCode());
		// end defect 9711	
		laTransactionHeaderData = setupVendorBackOfcTransHdr();

		//laTransactionHeaderData.		

		//Move data to Trans Table
		laCompleteTransactionData =
			laTransactionEngine.populateTrans(
				lvVector,
				laCompleteTransactionData,
				laTransactionHeaderData);

		//Move data to TrFdsDetail/FundFuncTrns 
		laTransactionEngine.populateFundFuncTrnsTrFdsDetail(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		//Move data to MvFuncTrans
		laTransactionEngine.populateMvTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		// Move data to SRFuncTrans
		laTransactionEngine.populateSRFuncTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		//Start a Transaction
		// defect 10401
		//	laDBAccess = new DatabaseAccess();
		//	laDBAccess.beginTransaction();
		// end defect 10401
		Vector lvInputTrans = new Vector();
		lvInputTrans.addElement(laDBAccess);
		lvInputTrans.addElement(lvVector);

		boolean lbIsSuccessful = false;
		try
		{
			postTrans(lvInputTrans);
			// defect 10401
			SpecialPlateItrntTransData laSPItrntTransData = laObject;
			//	(SpecialPlateItrntTransData) aaObj;
			// end defect 10401
			completeVendorInv(
				laDBAccess,
				laSPItrntTransData,
				laTransactionEngine.getTransTime());
			// Update the Internet Special Plt Transaction w/ 
			// Transaction Time and Then again as completed
			InternetSpecialPlateTransaction laItrntSPTransAccess =
				new InternetSpecialPlateTransaction(laDBAccess);
			laSPItrntTransData.setAuditTrailTransId(
				UtilityMethods.getTransId(
					laTransactionHeaderData.getOfcIssuanceNo(),
					laTransactionHeaderData.getTransWsId(),
					laTransactionHeaderData.getTransAMDate(),
					laTransactionEngine.getTransTime()));
			laItrntSPTransAccess.completeSPItrntTrans(
				laSPItrntTransData);

			lbIsSuccessful = true;

			return new Boolean(lbIsSuccessful);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"IAPPL transaction failed " + csClientHost);
			throw aeRTSEx;
		}
		finally
		{
			// defect 10401
			if (!lbPassedDBA && lbIsSuccessful)
			{
				// if local dba and succesful, commit
				// end defect 10401
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// defect 10401
			}
			else if (lbPassedDBA && lbIsSuccessful)
			{
				// TODO I think we don't want to do anything.
				// if passed dba and successful, let caller decide commit.
				// end defect 10401
			}
			else
			{
				// if not successful, we always try rollback.
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				Log.write(
					Log.SQL_EXCP,
					this,
					CommonConstant.MSG_ROLLBACK_ISSUED + csClientHost);
			}
		}
	}

	/**
	 * Marks the IAPPL Inventory as completed and updates the transtime
	 * to match that of the transaction.
	 * 
	 * @param aaDBAccess DatabaseAccess
	 * @param aaSPItrntTransData SpecialPlateItrntTransData
	 * @param aiTransTime int
	 * @throws RTSException
	 */
	private void completeIAPPLInv(
		DatabaseAccess aaDBAccess,
		SpecialPlateItrntTransData aaSPItrntTransData,
		int aiTransTime)
		throws RTSException
	{
		InventoryAllocationData laInvAllocData =
			new InventoryAllocationData();
		laInvAllocData.setItmCd(aaSPItrntTransData.getRegPltCd());
		laInvAllocData.setInvItmNo(aaSPItrntTransData.getRegPltNo());
		laInvAllocData.setInvQty(1);
		laInvAllocData.setInvItmEndNo(aaSPItrntTransData.getRegPltNo());
		laInvAllocData.setInvItmYr(0);
		laInvAllocData.setISA(
			aaSPItrntTransData.isISAIndi() == 1 ? true : false);
		laInvAllocData.setUserPltNo(
			aaSPItrntTransData.isPLPIndi() == 1 ? true : false);
		laInvAllocData.setMfgPltNo(aaSPItrntTransData.getMfgPltNo());
		laInvAllocData.setItrntReq(true);
		laInvAllocData.setOfcIssuanceNo(291);
		laInvAllocData.setTransWsId(999);
		laInvAllocData.setTransEmpId(TransCdConstant.IAPPL);
		laInvAllocData.setRequestorIpAddress(
			aaSPItrntTransData.getReqIPAddr());
		laInvAllocData.setRequestorRegPltNo(
			CommonConstant.STR_SPACE_EMPTY);
		// defect 9467
		laInvAllocData.setTransAmDate(new RTSDate().getAMDate());
		// end defect 9467		
		laInvAllocData.setTransTime(aiTransTime);
		InventoryServerBusiness laInvServBus =
			new InventoryServerBusiness();
		Vector lvInvBussData = new Vector();
		lvInvBussData.add(CommonConstant.ELEMENT_0, aaDBAccess);
		lvInvBussData.add(CommonConstant.ELEMENT_1, laInvAllocData);

		laInvAllocData =
			(InventoryAllocationData) laInvServBus.processData(
				GeneralConstant.COMMON,
				InventoryConstant.INV_VI_UPDATE_TRANSTIME,
				lvInvBussData);

		laInvAllocData =
			(InventoryAllocationData) laInvServBus.processData(
				GeneralConstant.COMMON,
				InventoryConstant.INV_VI_ITEM_APPLICATION_COMPLETE,
				lvInvBussData);

		if (laInvAllocData.getErrorCode() != 0)
		{
			throw new RTSException(laInvAllocData.getErrorCode());
		}
	}

	/**
	 * Marks the IAPPL Inventory as completed and updates the transtime
	 * to match that of the transaction.
	 * 
	 * @param aaDBAccess DatabaseAccess
	 * @param aaSPItrntTransData SpecialPlateItrntTransData
	 * @param aiTransTime int
	 * @throws RTSException
	 */
	private void completeVendorInv(
		DatabaseAccess aaDBAccess,
		SpecialPlateItrntTransData aaSPItrntTransData,
		int aiTransTime)
		throws RTSException
	{
		if (aaSPItrntTransData.getSpclRegId() < 1)
		{
			InventoryAllocationData laInvAllocData =
				new InventoryAllocationData();
			laInvAllocData.setItmCd(aaSPItrntTransData.getRegPltCd());
			laInvAllocData.setInvItmNo(
				aaSPItrntTransData.getRegPltNo());
			laInvAllocData.setInvQty(1);
			laInvAllocData.setInvItmEndNo(
				aaSPItrntTransData.getRegPltNo());
			laInvAllocData.setInvItmYr(0);
			laInvAllocData.setISA(
				aaSPItrntTransData.isISAIndi() == 1 ? true : false);
			// defect 10401
			laInvAllocData.setUserPltNo(
				aaSPItrntTransData.isPLPIndi() == 1 ? true : false);
			// end defect 10401

			laInvAllocData.setMfgPltNo(
				aaSPItrntTransData.getMfgPltNo());
			laInvAllocData.setItrntReq(true);
			// defect 9711
			//		laInvAllocData.setOfcIssuanceNo(294);
			//		laInvAllocData.setTransWsId(999);
			laInvAllocData.setOfcIssuanceNo(
				SystemProperty.getVpOfcIssuanceNo());
			laInvAllocData.setTransWsId(SystemProperty.getVpWsId());
			// end defect 9711
			laInvAllocData.setTransEmpId(
				aaSPItrntTransData.getTransEmpID());

			laInvAllocData.setRequestorIpAddress(
				aaSPItrntTransData.getReqIPAddr());
			laInvAllocData.setRequestorRegPltNo(
				CommonConstant.STR_SPACE_EMPTY);
			// defect 9467
			laInvAllocData.setTransAmDate(new RTSDate().getAMDate());
			// end defect 9467		
			laInvAllocData.setTransTime(aiTransTime);
			InventoryServerBusiness laInvServBus =
				new InventoryServerBusiness();
			Vector lvInvBussData = new Vector();
			lvInvBussData.add(CommonConstant.ELEMENT_0, aaDBAccess);
			lvInvBussData.add(CommonConstant.ELEMENT_1, laInvAllocData);

			laInvAllocData =
				(InventoryAllocationData) laInvServBus.processData(
					GeneralConstant.COMMON,
					InventoryConstant.INV_VI_UPDATE_TRANSTIME,
					lvInvBussData);

			laInvAllocData =
				(InventoryAllocationData) laInvServBus.processData(
					GeneralConstant.COMMON,
					InventoryConstant.INV_VI_ITEM_APPLICATION_COMPLETE,
					lvInvBussData);

			if (laInvAllocData.getErrorCode() != 0)
			{
				throw new RTSException(laInvAllocData.getErrorCode());
			}
		}
	}

	/**
	 * This method processes two types of Trans Codes: IADDR and IRNR
	 *  
	 * @param aaObj Object
	 * @return Object 
	 * @throws RTSException
	 */
	private Object procsInternetAddrChng(Object aaObj)
		throws RTSException
	{
		if (!(aaObj instanceof CompleteTransactionData))
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				CommonConstant.TXT_INPUT_TYPE_INVALID,
				CommonConstant.STR_ERROR);
		}
		CompleteTransactionData laCompleteTransactionData =
			(CompleteTransactionData) aaObj;
		String lsTransCd = laCompleteTransactionData.getTransCode();
		Vector lvVector = new Vector();

		com
			.txdot
			.isd
			.rts
			.services
			.common
			.Transaction laTransactionEngine =
			new com.txdot.isd.rts.services.common.Transaction(
				lsTransCd);

		// Since the Internet Address transactions are run from the server,
		// need to see if this  is the first transaction of the day.  
		// If it is, then set up the first  transaction of the day.

		// defect 8285
		// defect 10610
		//		TransactionHeaderData laTransactionHeaderData =
		//			setupItrntBackOfcTransHdr();
		TransactionHeaderData laTransactionHeaderData =
			setupItrntBackOfcTransHdr(
				SystemProperty.getWorkStationId());
		// end defect 10610	
		// end defect 8285 

		//Move data to Trans Table
		laCompleteTransactionData =
			laTransactionEngine.populateTrans(
				lvVector,
				laCompleteTransactionData,
				laTransactionHeaderData);

		//Move data to MvFuncTrans
		laTransactionEngine.populateMvTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		// defect 9315 
		// Add SRFuncTrans processing for IADDR 
		// Move data to SRFuncTrans
		laTransactionEngine.populateSRFuncTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);
		// end defect 9315 

		//Start a Transaction
		DatabaseAccess laDBAccess = new DatabaseAccess();
		laDBAccess.beginTransaction();
		Vector lvInputTrans = new Vector();
		lvInputTrans.addElement(laDBAccess);
		lvInputTrans.addElement(lvVector);

		boolean lbIsSuccessful = false;
		try
		{
			postTrans(lvInputTrans);

			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_INET_TRANS_START
					+ csClientHost);

			//Populate internet transaction table
			InternetTransaction laItrntTrans =
				new InternetTransaction(laDBAccess);

			// check to see if a record already exists in the table
			if (laItrntTrans
				.recordExists(
					lsTransCd,
					laCompleteTransactionData.getOrgVehicleInfo()))
			{
				// delete the record
				laItrntTrans.delItrntAddr(
					lsTransCd,
					laCompleteTransactionData.getOrgVehicleInfo());
			}
			// Insert, passing modified MFVehicleData object
			laItrntTrans.insItrntAddr(
				lsTransCd,
				laCompleteTransactionData.getVehicleInfo());

			// Update Internet Summary table for IADDR
			InternetSummary laItrntSummary =
				new InternetSummary(laDBAccess);
			int liOfcissuanceNo =
				laCompleteTransactionData
					.getVehicleInfo()
					.getRegData()
					.getResComptCntyNo();
			laItrntSummary.updItrntSummary(lsTransCd, liOfcissuanceNo);

			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_INET_TRANS_SUCCESS
					+ csClientHost);
			lbIsSuccessful = true;
			return new Boolean(true);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				CommonConstant.MSG_DB_CALL_INET_TRANS_FAILED
					+ csClientHost);
			throw aeRTSEx;
		}
		finally
		{
			if (lbIsSuccessful)
			{
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			}
			else
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				Log.write(
					Log.SQL_EXCP,
					this,
					CommonConstant.MSG_ROLLBACK_ISSUED + csClientHost);
			}
		}
	}

	/**
	 * This method processes Trans Code: IADDRE
	 *  
	 * @param aaObj Object
	 * @return Object 
	 * @throws RTSException
	 */
	private Object procChngeEReminder(Object aaObj) throws RTSException
	{
		if (!(aaObj instanceof CompleteTransactionData))
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				CommonConstant.TXT_INPUT_TYPE_INVALID,
				CommonConstant.STR_ERROR);
		}
		CompleteTransactionData laCompleteTransactionData =
			(CompleteTransactionData) aaObj;
		String lsTransCd = laCompleteTransactionData.getTransCode();
		Vector lvVector = new Vector();

		com
			.txdot
			.isd
			.rts
			.services
			.common
			.Transaction laTransactionEngine =
			new com.txdot.isd.rts.services.common.Transaction(
				lsTransCd);

		// Since the EReminder transactions are run from the server,
		// need to see if this  is the first transaction of the day.  
		// If it is, then set up the first  transaction of the day.
		TransactionHeaderData laTransactionHeaderData =
			setupItrntBackOfcTransHdr(998);

		//Move data to Trans Table
		laCompleteTransactionData =
			laTransactionEngine.populateTrans(
				lvVector,
				laCompleteTransactionData,
				laTransactionHeaderData);

		//Move data to MvFuncTrans
		laTransactionEngine.populateMvTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		// defect 9315 
		// Add SRFuncTrans processing for IADDR 
		// Move data to SRFuncTrans
		laTransactionEngine.populateSRFuncTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);
		// end defect 9315 

		//Start a Transaction
		DatabaseAccess laDBAccess = new DatabaseAccess();
		laDBAccess.beginTransaction();
		Vector lvInputTrans = new Vector();
		lvInputTrans.addElement(laDBAccess);
		lvInputTrans.addElement(lvVector);

		boolean lbIsSuccessful = false;
		try
		{
			postTrans(lvInputTrans);

			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_INET_TRANS_START
					+ csClientHost);

			//Populate internet transaction table
			InternetTransaction laItrntTrans =
				new InternetTransaction(laDBAccess);

			// check to see if a record already exists in the table
			if (laItrntTrans
				.recordExists(
					lsTransCd,
					laCompleteTransactionData.getOrgVehicleInfo()))
			{
				// delete the record
				laItrntTrans.delItrntAddr(
					lsTransCd,
					laCompleteTransactionData.getOrgVehicleInfo());
			}

			// Insert, passing modified MFVehicleData object
			laItrntTrans.insItrntAddr(
				lsTransCd,
				laCompleteTransactionData.getVehicleInfo());

			// Update Internet Summary table for IADDR
			InternetSummary laItrntSummary =
				new InternetSummary(laDBAccess);
			int liOfcissuanceNo =
				laCompleteTransactionData
					.getVehicleInfo()
					.getRegData()
					.getResComptCntyNo();
			laItrntSummary.updItrntSummary(lsTransCd, liOfcissuanceNo);

			Log.write(
				Log.APPLICATION,
				this,
				CommonConstant.MSG_DB_CALL_INET_TRANS_SUCCESS
					+ csClientHost);
			lbIsSuccessful = true;
			return new Boolean(true);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				CommonConstant.MSG_DB_CALL_INET_TRANS_FAILED
					+ csClientHost);
			throw aeRTSEx;
		}
		finally
		{
			if (lbIsSuccessful)
			{
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			}
			else
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				Log.write(
					Log.SQL_EXCP,
					this,
					CommonConstant.MSG_ROLLBACK_ISSUED + csClientHost);
			}
		}
	}

	/**
	 * Process Server side Transaction calls
	 * 
	 * The passed Object is a vector of 3 elements:<br>
	 * <ol>
	 * <li> TransactionHeaderData<br>
	 * <li> CompleteTransactionData<br>
	 * <li> Database Connection
	 * <eol>
	 * 
	 * @param aaObj Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object procsTrans(Object aaObj) throws RTSException
	{
		Vector lvInput = (Vector) aaObj;
		TransactionHeaderData laTransactionHeaderData =
			(TransactionHeaderData) lvInput.get(
				CommonConstant.ELEMENT_0);
		CompleteTransactionData laCompleteTransactionData =
			(CompleteTransactionData) lvInput.get(
				CommonConstant.ELEMENT_1);
		DatabaseAccess laDatabaseAccess =
			(DatabaseAccess) lvInput.get(CommonConstant.ELEMENT_2);
		Vector lvVector = new Vector();

		// defect 10674, 10756
		String lsTransCd = laCompleteTransactionData.getTransCode();

		com
			.txdot
			.isd
			.rts
			.services
			.common
			.Transaction laTransactionEngine =
			new com.txdot.isd.rts.services.common.Transaction(
				lsTransCd);

		// Assign Transaction Time where assigned on client
		if (lsTransCd.equals(TransCdConstant.RPRSTK)
			|| lsTransCd.equals(TransCdConstant.RPRPRM))
		{
			laTransactionEngine.setTransTime(
				laTransactionHeaderData.getTransTime());
		}
		// end defect 10674, 10756 

		//Move data to Trans Table
		laCompleteTransactionData =
			laTransactionEngine.populateTrans(
				lvVector,
				laCompleteTransactionData,
				laTransactionHeaderData);

		//Move data to MvFuncTrans
		laTransactionEngine.populateMvTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		//Move data to TrInvDetail and InvFuncTrans
		laTransactionEngine.populateInvFuncTransTrInvDetail(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		//Move data to FundsFunc and TrFdsDetail
		laTransactionEngine.populateFundFuncTrnsTrFdsDetail(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);
		Vector lvTransInput = new Vector();
		lvTransInput.addElement(laDatabaseAccess);
		lvTransInput.addElement(lvVector);

		processData(
			GeneralConstant.COMMON,
			CommonConstant.POST_TRANS,
			lvTransInput);

		// defect 7889
		// Modified to use new method from UtilityMethods 
		laCompleteTransactionData.setTransId(
			UtilityMethods.getTransId(
				laTransactionHeaderData.getOfcIssuanceNo(),
				laTransactionHeaderData.getTransWsId(),
				laTransactionHeaderData.getTransAMDate(),
				laTransactionEngine.getTransTime()));
		// end defect 7889 		
		return laCompleteTransactionData;
	}

	/** 
	 * Process WebSub Transaction Request 
	 * 
	 * @param aaData
	 * @throws RTSException
	 */
	private Object procsWebSubTrans(Object aaData) throws RTSException
	{
		DatabaseAccess laDBA = new DatabaseAccess();
		Dollar ldTransTotal = new Dollar(0);

		Hashtable laHashApprvBatch = (Hashtable) aaData;
		WebAgencyBatchData laWABatchData =
			(WebAgencyBatchData) laHashApprvBatch.get("BATCHDATA");
		AssignedWorkstationIdsData laAssgndWsIdsData =
			(AssignedWorkstationIdsData) laHashApprvBatch.get(
				"ASSGNDWSDATA");
		WebAgencyData laWAgncyData =
			(WebAgencyData) laHashApprvBatch.get("WEBAGNCYDATA");

		// TODO May substitute new Boolean(false) for 
		// throwing RTSException 

		try
		{
			/**
			 * Pull data from RTS_WEB_AGNCY_TRANS  
			 */
			WebAgencyTransactionData laWATransData =
				new WebAgencyTransactionData();
			laWATransData.setAgncyBatchIdntyNo(
				laWABatchData.getAgncyBatchIdntyNo());
			WebAgencyTransaction laWATransSQL =
				new WebAgencyTransaction(laDBA);

			// UOW #1 
			laDBA.beginTransaction();
			Vector lvWATransData =
				laWATransSQL.qryWebAgencyTransaction(
					laWATransData,
					true,
					true);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			/**
			 * Throw Exception if No Web Agency Trans Found: 2327 
			 */
			if (lvWATransData == null || lvWATransData.isEmpty())
			{
				throw new RTSException(
					ErrorsConstant
						.ERR_NUM_WEBAGNT_AGENCY_BATCH_HAS_NO_TRANS);
			}

			TransactionHeaderData laTransHdrData =
				new TransactionHeaderData();
			laTransHdrData.setOfcIssuanceNo(
				laWABatchData.getOfcIssuanceNo());
			laTransHdrData.setTransWsId(laWABatchData.getTransWsId());
			laTransHdrData.setTransAMDate(
				laWABatchData.getTransAMDate());
			laTransHdrData.setCustSeqNo(laWABatchData.getCustSeqNo());
			laTransHdrData.setSubstaId(laAssgndWsIdsData.getSubstaId());
			laTransHdrData.setOfcIssuanceCd(
				laAssgndWsIdsData.getOfcIssuanceCd());

			Vector lvTransHdrData = new Vector();
			TransactionHeader laTransHdrSQL =
				new TransactionHeader(laDBA);

			/**
			 * If CustSeqNo >= 7000, determine if row 
			 *   exists in RTS_TRANS_HDR   
			 */
			if (laTransHdrData.getCustSeqNo()
				> CommonConstant.MAX_POS_CUSTSEQNO)
			{
				// UOW #2 - Query Transaction Header  
				laDBA.beginTransaction();
				lvTransHdrData =
					laTransHdrSQL.qryTransactionHeader(laTransHdrData);
				laDBA.endTransaction(DatabaseAccess.COMMIT);
				// UOW #2 END
			}

			WebAgencyBatch laWABatchSQL = new WebAgencyBatch(laDBA);

			// RTS_TRANS_HDR record Found 
			if (lvTransHdrData != null && !lvTransHdrData.isEmpty())
			{
				laTransHdrData =
					(TransactionHeaderData) lvTransHdrData.elementAt(0);

				/**
				 * Throw Exception if Found TransHdr Data 
				 * doesn't match: 2334 
				 */
				if (!laTransHdrData
					.getTransEmpId()
					.equals(laWABatchData.getTransEmpId())
					|| laTransHdrData.getCashWsId()
						!= laAssgndWsIdsData.getCashWsId()
					|| laTransHdrData.getFeeSourceCd()
						!= FundsConstant.FEE_SOURCE_SUBCON
					|| laTransHdrData.getVersionCd()
						!= SystemProperty.getMainFrameVersion()
					|| laTransHdrData.getOfcIssuanceCd()
						!= laAssgndWsIdsData.getOfcIssuanceCd())
				{
					throw new RTSException(
						ErrorsConstant
							.ERR_NUM_WEBAGNT_INVALID_TRANS_HDR);
				}

				/**
				 * If TransHdr row exists and complete, 
				 *   Processing is Complete   
				 */
				if (laTransHdrData.getTransTimestmp() != null)
				{
					return new Boolean(true);
				}
			}
			/**
			 * Throw Exception if invalid CustSeqNo: 2334 
			 */
			else
			{
				if (laTransHdrData.getCustSeqNo() != Integer.MIN_VALUE
					&& laTransHdrData.getCustSeqNo() != 0)
				{
					throw new RTSException(
						ErrorsConstant
							.ERR_NUM_WEBAGNT_INVALID_TRANS_HDR);
				}

				/**
				 * Insert new RTS_TRANS_HDR w/ Next CustSeqNo >=7000
				 *  
				 * Update RTS_WEB_AGNCY_BATCH w/ CustSeqNo    
				 */
				laTransHdrData.setTransEmpId(
					laWABatchData.getTransEmpId());
				laTransHdrData.setCashWsId(
					laAssgndWsIdsData.getCashWsId());
				laTransHdrData.setFeeSourceCd(
					FundsConstant.FEE_SOURCE_SUBCON);
				laTransHdrData.setVersionCd(
					SystemProperty.getMainFrameVersion());
				laTransHdrData.setOfcIssuanceCd(
					laAssgndWsIdsData.getOfcIssuanceCd());
				laTransHdrData.setTransName(laWAgncyData.getName1());

				// UOW #3
				laDBA.beginTransaction();
				laTransHdrData =
					laTransHdrSQL.insNextWebAgntTransHdr(
						laTransHdrData);
				laWABatchData.setCustSeqNo(
					laTransHdrData.getCustSeqNo());
				laWABatchData.setBatchCompleteTimestmp(new RTSDate());
				laWABatchSQL.updWebAgencyBatchCustSeqNo(laWABatchData);
				laDBA.endTransaction(DatabaseAccess.COMMIT);
				// UOW #3 END
			}

			/**
			 * Get Next TransTime for WebAgencyTrans for
			 *   given OfcIssuanceNo, TransWsId, TransAMDate 
			 *  
			 * Update RTS_WEB_AGNCY_BATCH w/ CustSeqNo    
			 */
			// UOW #4 
			Transaction laTransSQL = new Transaction(laDBA);
			laDBA.beginTransaction();
			int liTransTime =
				laTransSQL.getNextWebAgntTransTime(laTransHdrData);

			// TransTime will be updated in loop below 
			laTransHdrData.setTransTime(liTransTime);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #4 END 

			/**
			 * For all transactions for RTS_WEB_AGNCY_TRANS
			 *    If not Voided by Agncy or County
			 *        - Collect Fees 
			 *        - If Transaction not yet created
			 *            - Insert Transaction
			 *  
			 */
			for (int i = 0; i < lvWATransData.size(); i++)
			{
				laWATransData =
					(WebAgencyTransactionData) lvWATransData.elementAt(
						i);

				if (laWATransData.getAgncyVoidIndi() == 0
					&& laWATransData.getCntyVoidIndi() == 0)
				{
					WebAgencyTransactionFee laWATransFeeSQL =
						new WebAgencyTransactionFee(laDBA);

					// UOW #4 + (2i -1) BEGIN 
					laDBA.beginTransaction();
					Vector lvWATransFeeData =
						laWATransFeeSQL.qryWebAgencyTransactionFee(
							laWATransData.getSavReqId());
					laDBA.endTransaction(DatabaseAccess.COMMIT);
					// UOW #4 + (2i -1) END 

					// Sum Total Fees for Payment  
					if (lvWATransFeeData != null
						&& !lvWATransFeeData.isEmpty())
					{
						for (int j = 0;
							j < lvWATransFeeData.size();
							j++)
						{
							WebAgencyTransactionFeeData laWATransFeeData =
								(
									WebAgencyTransactionFeeData) lvWATransFeeData
										.elementAt(
									j);
							ldTransTotal =
								ldTransTotal.add(
									laWATransFeeData.getItmPrice());
						}
					}

					if (laWATransData.needsTrans())
					{
						laTransHdrData.setTransTime(
							laTransHdrData.getTransTime() + 1);
						
						// defect 11137 
						// UOW #4a + (2i -1) BEGIN 
						WebAgencyTransactionAddress laWATransAddrSQL =
							new WebAgencyTransactionAddress(laDBA);
						
						// Owner Address 
						laDBA.beginTransaction();
						WebAgencyTransactionAddressData laWATransOwnrAddrData =  
							laWATransAddrSQL.qryWebAgencyTransactionAddr(
								laWATransData.getSavReqId(),RegistrationConstant.OWNR_ADDR_TYPE_CD);
						
						WebAgencyTransactionAddressData laWATransRcpntAddrData =  
							laWATransAddrSQL.qryWebAgencyTransactionAddr(
								laWATransData.getSavReqId(),RegistrationConstant.RCPNT_ADDR_TYPE_CD);
						laDBA.endTransaction(DatabaseAccess.COMMIT);
						// UOW #4a + (2i -1) END 
						
						CompleteTransactionData laCTData =
							new CompleteTransactionData(
								laWATransData,
								lvWATransFeeData,
								laWATransOwnrAddrData,
								laWATransRcpntAddrData);
						// end defect 11137
						
						Vector lvVector = new Vector();

						com
							.txdot
							.isd
							.rts
							.services
							.common
							.Transaction laTransEngine =
							new com
								.txdot
								.isd
								.rts
								.services
								.common
								.Transaction(
								TransCdConstant.WRENEW);

						laTransEngine.setTransTime(
							laTransHdrData.getTransTime());

						//Move data to Trans Table
						laCTData =
							laTransEngine.populateTrans(
								lvVector,
								laCTData,
								laTransHdrData);

						//Move data to TrFdsDetail/FundFuncTrns 
						laTransEngine.populateFundFuncTrnsTrFdsDetail(
							lvVector,
							laCTData,
							laTransHdrData);

						// Move data to TrInvDetail/InvFuncTrans 
						laTransEngine.populateInvFuncTransTrInvDetail(
							lvVector,
							laCTData,
							laTransHdrData);

						//Move data to MvFuncTrans
						laTransEngine.populateMvTrans(
							lvVector,
							laCTData,
							laTransHdrData);

						// Move data to SRFuncTrans
						laTransEngine.populateSRFuncTrans(
							lvVector,
							laCTData,
							laTransHdrData);

						// Post Transaction 

						// UOW #4 + 2i 
						laDBA.beginTransaction();
						Vector lvInputTrans = new Vector();
						lvInputTrans.addElement(laDBA);
						lvInputTrans.addElement(lvVector);
						postTrans(lvInputTrans);
						laWATransData.setTransId(
							UtilityMethods.getTransId(
								laTransHdrData.getOfcIssuanceNo(),
								laTransHdrData.getTransWsId(),
								laTransHdrData.getTransAMDate(),
								laTransHdrData.getTransTime()));
						laWATransSQL.updWebAgencyTransaction(
							laWATransData);
						laDBA.endTransaction(DatabaseAccess.COMMIT);
						// UOW #4 + 2i End
					}
				}
			}

			/**
			 * Complete Processing:   (Single UOW) 
			 *  - Insert Payment
			 *  - Complete Trans Header 
			 *  - Update BatchStatusCd/BatchCompleteTimestamp 
			 *  - Update Special Plates Trans History 
			 */
			TransactionPaymentData laTransPaymentData =
				new TransactionPaymentData(laTransHdrData);
			laTransPaymentData.setPymntNo("1");
			laTransPaymentData.setPymntTypeCd(2);
			laTransPaymentData.setPymntTypeAmt(ldTransTotal);

			laTransHdrData.setTransTimestmp(new RTSDate());
			laWABatchData.setBatchStatusCd("A");
			laWABatchData.setBatchCompleteTimestmp(new RTSDate());
			SpecialPlateTransactionHistoryData laSpclPltTransHstryData =
				new SpecialPlateTransactionHistoryData(laTransHdrData);

			TransactionPayment laTransPaySQL =
				new TransactionPayment(laDBA);
			SpecialPlateTransactionHistory laSpclPltTransHstrySQL =
				new SpecialPlateTransactionHistory(laDBA);

			// UOW #10  
			laDBA.beginTransaction();
			laTransPaySQL.insTransactionPayment(laTransPaymentData);
			laTransHdrSQL.updTransTimeTransactionHeader(laTransHdrData);
			laWABatchSQL.updWebAgencyBatch(laWABatchData);
			laSpclPltTransHstrySQL.updSpecialPlateTransactionHistory(
				laSpclPltTransHstryData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			// UOW #10 END 

			return new Boolean(true);
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		catch (Exception aeEx)
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_APPROVAL_NOT_SUCCESSFUL);
		}
	}

	/**
	 * Release inventory issued when Transaction is being deleted
	 *  
	 * @param aaTransactionKey TransactionKeyData
	 * @param aaDba DatabaseAccess 
	 * @return Object
	 * @throws RTSException
	 */
	private Object releaseInvForDeleteTrans(
		TransactionKey aaTransactionKey,
		DatabaseAccess aaDba)
		throws RTSException
	{
		//select on inventory
		TransactionInventoryDetailData laTransactionInventoryDetailData =
			new TransactionInventoryDetailData();
		laTransactionInventoryDetailData.setOfcIssuanceNo(
			aaTransactionKey.getOfcIssuanceNo());
		laTransactionInventoryDetailData.setSubstaId(
			aaTransactionKey.getSubstaId());
		laTransactionInventoryDetailData.setTransAMDate(
			aaTransactionKey.getTransAMDate());
		laTransactionInventoryDetailData.setTransWsId(
			aaTransactionKey.getTransWsId());
		laTransactionInventoryDetailData.setCustSeqNo(
			aaTransactionKey.getCustSeqNo());
		TransactionInventoryDetail laTransactionInventoryDetail =
			new TransactionInventoryDetail(aaDba);
		Vector lvTransactionInventoryDetail =
			laTransactionInventoryDetail
				.qryTransactionInventoryDetailSet(
				laTransactionInventoryDetailData);
		if (lvTransactionInventoryDetail != null
			&& lvTransactionInventoryDetail.size()
				> CommonConstant.ELEMENT_0)
		{
			InventoryServerBusiness laInventoryServerBusiness =
				new InventoryServerBusiness();
			//release inventory
			for (int i = 0;
				i < lvTransactionInventoryDetail.size();
				i++)
			{
				TransactionInventoryDetailData laTrInvDetailData =
					(
						TransactionInventoryDetailData) lvTransactionInventoryDetail
							.get(
						i);
				ProcessInventoryData laProcessInventoryData =
					new ProcessInventoryData();
				laProcessInventoryData.setOfcIssuanceNo(
					laTrInvDetailData.getOfcIssuanceNo());
				laProcessInventoryData.setSubstaId(
					laTrInvDetailData.getSubstaId());
				laProcessInventoryData.setItmCd(
					laTrInvDetailData.getItmCd());
				laProcessInventoryData.setInvItmYr(
					laTrInvDetailData.getInvItmYr());
				laProcessInventoryData.setInvItmNo(
					laTrInvDetailData.getInvItmNo());
				laProcessInventoryData.setInvQty(1);
				laProcessInventoryData.setInvStatusCd(
					InventoryConstant.HOLD_INV_NOT);

				if (StickerPrintingUtilities
					.isStickerPrintable(laProcessInventoryData))
				{
					continue;
				}

				Vector lvIn = new Vector();
				lvIn.addElement(aaDba);
				lvIn.addElement(laProcessInventoryData);
				try
				{
					laInventoryServerBusiness.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant.UPDATE_INVENTORY_STATUS_CD,
						lvIn);
				}
				catch (RTSException aeRTSEx)
				{
					// Do not throw exception if inventory not found	            
					if (aeRTSEx.getCode()
						!= ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB)
					{
						throw aeRTSEx;
					}
				}
			}
		}
		return null;
	}

	/**
	 * This method creates the TransactionHeaderData object for
	 * V21VTN transactions. If first transaction of the day, 
	 * the TransactionHeader is inserted. 
	 * 
	 * @throws RTSException
	 *  
	 */
	private TransactionHeaderData setupV21VTNBackOfcTransHdr()
		throws RTSException
	{
		// Setup Back Office TransHeader for V21VTN
		RTSDate laRTSDate = new RTSDate();
		OfficeIdsData laOfficeIdsData =
			OfficeIdsCache.getOfcId(CommonConstant.V21_OFCISSUANCENO);
		if (laOfficeIdsData == null)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				CommonConstant.MSG_OFFICEIDS_NULL,
				CommonConstant.STR_ERROR);
		}
		TransactionHeaderData laTransHdrData =
			new TransactionHeaderData();
		laTransHdrData.setOfcIssuanceNo(
			CommonConstant.V21_OFCISSUANCENO);
		laTransHdrData.setOfcIssuanceCd(
			laOfficeIdsData.getOfcIssuanceCd());
		laTransHdrData.setSubstaId(CommonConstant.V21_SUBSTAID);
		laTransHdrData.setTransWsId(CommonConstant.V21VTN_TRANSWSID);
		laTransHdrData.setTransAMDate(laRTSDate.getAMDate());
		laTransHdrData.setCustSeqNo(0);
		laTransHdrData.setVersionCd(
			SystemProperty.getMainFrameVersion());
		laTransHdrData.setTransEmpId(CommonConstant.V21VTN_EMPID);
		laTransHdrData.setCashWsId(CommonConstant.V21VTN_TRANSWSID);
		laTransHdrData.setTransName(
			CommonConstant.V21VTN_BACK_OFFICE_TRANS);
		laTransHdrData.setFeeSourceCd(0);
		laTransHdrData.setTransTimestmp(laRTSDate);

		// Insert Back Office TransHeader for V21VTN 
		DatabaseAccess laDatabaseAccess = new DatabaseAccess();
		int liNumRows = Integer.MIN_VALUE;

		try
		{
			TransactionHeader laTransHdr =
				new TransactionHeader(laDatabaseAccess);

			laDatabaseAccess.beginTransaction();

			liNumRows =
				laTransHdr.insItrntBackOfcTransHdr(laTransHdrData);

			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);

			if (liNumRows == CommonConstant.ELEMENT_1)
			{
				Log.write(
					Log.APPLICATION,
					this,
					"Insert V21VTN Transaction Header"
						+ laRTSDate.getAMDate());
			}
		}
		catch (RTSException aeRTSEx1)
		{
			laDatabaseAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.write(
				Log.APPLICATION,
				this,
				"Insert V21VTN Transaction Header Error"
					+ laRTSDate.getAMDate());
		}
		finally
		{
			return laTransHdrData;
		}
	}
	/**
	 * Process Vision 21 Vehicle Transaction Notification
	 * 
	 * @param  avDataIn
	 * @return Object 
	 * @throws RTSException
	 */
	private Object procsV21VTN(Object avDataIn) throws RTSException
	{
		if (!(avDataIn instanceof Vector)
			|| ((Vector) avDataIn).size() != 2)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				CommonConstant.TXT_INPUT_TYPE_INVALID,
				CommonConstant.STR_ERROR);
		}

		// VehicleInquiryData laVID = (VehicleInquiryData) aaObj;
		VehicleInquiryData laVID =
			(VehicleInquiryData) ((Vector) avDataIn).elementAt(0);
		String lsTransCode = (String) ((Vector) avDataIn).elementAt(1);

		CompleteTransactionData laCompleteTransactionData =
			new CompleteTransactionData();

		// Assign MFVehicleData for CompleteTransData   
		laCompleteTransactionData.setVehicleInfo(
			laVID.getMfVehicleData());

		// Assign TransCd 
		laCompleteTransactionData.setTransCode(lsTransCode);

		Vector lvVector = new Vector();

		com
			.txdot
			.isd
			.rts
			.services
			.common
			.Transaction laTransactionEngine =
			new com.txdot.isd.rts.services.common.Transaction(
				lsTransCode);

		// Since the V21VTN transactions are run from the server,
		// need to see if this  is the first transaction of the day.  
		// If it is, then set up the first  transaction of the day.

		TransactionHeaderData laTransactionHeaderData =
			setupV21VTNBackOfcTransHdr();

		laTransactionHeaderData.setTransTime(
			laTransactionEngine.getTransTime());

		//Move data to Trans Table
		laCompleteTransactionData =
			laTransactionEngine.populateTrans(
				lvVector,
				laCompleteTransactionData,
				laTransactionHeaderData);

		//Move data to TrFdsDetail/FundFuncTrns 
		laTransactionEngine.populateFundFuncTrnsTrFdsDetail(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		//Move data to MvFuncTrans
		laTransactionEngine.populateMvTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		// Move data to SRFuncTrans
		laTransactionEngine.populateSRFuncTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		//Start a Transaction
		DatabaseAccess laDBAccess = new DatabaseAccess();
		laDBAccess.beginTransaction();
		Vector lvInputTrans = new Vector();
		lvInputTrans.addElement(laDBAccess);
		lvInputTrans.addElement(lvVector);

		boolean lbIsSuccessful = false;
		try
		{

			postTrans(lvInputTrans);

			lbIsSuccessful = true;

			// return the trans header data to extract the trans id
			return laTransactionHeaderData;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"V21VTN transaction failed " + csClientHost);
			throw aeRTSEx;
		}
		finally
		{
			if (lbIsSuccessful)
			{
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			}
			else
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				Log.write(
					Log.SQL_EXCP,
					this,
					CommonConstant.MSG_ROLLBACK_ISSUED + csClientHost);
			}

		}
	}

	/**
	 * This method creates the TransactionHeaderData object for
	 * V21PLD transactions. If first transaction of the day, 
	 * the TransactionHeader is inserted. 
	 * 
	 * @throws RTSException
	 *  
	 */
	private TransactionHeaderData setupV21PLDBackOfcTransHdr()
		throws RTSException
	{
		// Setup Back Office TransHeader for V21VTN
		RTSDate laRTSDate = new RTSDate();
		OfficeIdsData laOfficeIdsData =
			OfficeIdsCache.getOfcId(CommonConstant.V21_OFCISSUANCENO);
		if (laOfficeIdsData == null)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				CommonConstant.MSG_OFFICEIDS_NULL,
				CommonConstant.STR_ERROR);
		}
		TransactionHeaderData laTransHdrData =
			new TransactionHeaderData();
		laTransHdrData.setOfcIssuanceNo(
			CommonConstant.V21_OFCISSUANCENO);
		laTransHdrData.setOfcIssuanceCd(
			laOfficeIdsData.getOfcIssuanceCd());
		laTransHdrData.setSubstaId(CommonConstant.V21_SUBSTAID);
		laTransHdrData.setTransWsId(CommonConstant.V21PLD_TRANSWSID);
		laTransHdrData.setTransAMDate(laRTSDate.getAMDate());
		laTransHdrData.setCustSeqNo(0);
		laTransHdrData.setVersionCd(
			SystemProperty.getMainFrameVersion());
		laTransHdrData.setTransEmpId(CommonConstant.V21PLD_EMPID);
		laTransHdrData.setCashWsId(CommonConstant.V21PLD_TRANSWSID);
		laTransHdrData.setTransName(
			CommonConstant.V21PLD_BACK_OFFICE_TRANS);
		laTransHdrData.setFeeSourceCd(0);
		laTransHdrData.setTransTimestmp(laRTSDate);

		// Insert Back Office TransHeader for V21PLD 
		DatabaseAccess laDatabaseAccess = new DatabaseAccess();
		int liNumRows = Integer.MIN_VALUE;

		try
		{
			TransactionHeader laTransHdr =
				new TransactionHeader(laDatabaseAccess);

			laDatabaseAccess.beginTransaction();

			liNumRows =
				laTransHdr.insItrntBackOfcTransHdr(laTransHdrData);

			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);

			if (liNumRows == CommonConstant.ELEMENT_1)
			{
				Log.write(
					Log.APPLICATION,
					this,
					"Insert V21PLD Transaction Header"
						+ laRTSDate.getAMDate());
			}
		}
		catch (RTSException aeRTSEx1)
		{
			laDatabaseAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.write(
				Log.APPLICATION,
				this,
				"Insert V21PLD Transaction Header Error"
					+ laRTSDate.getAMDate());
		}
		finally
		{
			return laTransHdrData;
		}
	}

	/**
	 * Process Vision 21 Plate Disposition
	 * 
	 * @param aaObj
	 * @return Object 
	 * @throws RTSException
	 */
	private Object procsV21PLD(Object aaObj) throws RTSException
	{
		if (!(aaObj instanceof VehicleInquiryData))
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				CommonConstant.TXT_INPUT_TYPE_INVALID,
				CommonConstant.STR_ERROR);
		}

		VehicleInquiryData laVID = (VehicleInquiryData) aaObj;

		CompleteTransactionData laCompleteTransactionData =
			new CompleteTransactionData();

		// Assign MFVehicleData for CompleteTransData   
		laCompleteTransactionData.setVehicleInfo(
			laVID.getMfVehicleData());

		// Assign TransCd 
		laCompleteTransactionData.setTransCode(TransCdConstant.V21PLD);

		Vector lvVector = new Vector();

		com
			.txdot
			.isd
			.rts
			.services
			.common
			.Transaction laTransactionEngine =
			new com.txdot.isd.rts.services.common.Transaction(
				TransCdConstant.V21PLD);

		// Since the V21PLD transactions are run from the server,
		// need to see if this  is the first transaction of the day.  
		// If it is, then set up the first  transaction of the day.

		TransactionHeaderData laTransactionHeaderData =
			setupV21PLDBackOfcTransHdr();

		laTransactionHeaderData.setTransTime(
			laTransactionEngine.getTransTime());

		//Move data to Trans Table
		laCompleteTransactionData =
			laTransactionEngine.populateTrans(
				lvVector,
				laCompleteTransactionData,
				laTransactionHeaderData);

		//Move data to TrFdsDetail/FundFuncTrns 
		laTransactionEngine.populateFundFuncTrnsTrFdsDetail(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		//Move data to MvFuncTrans
		laTransactionEngine.populateMvTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		// Move data to SRFuncTrans
		laTransactionEngine.populateSRFuncTrans(
			lvVector,
			laCompleteTransactionData,
			laTransactionHeaderData);

		//Start a Transaction
		DatabaseAccess laDBAccess = new DatabaseAccess();
		laDBAccess.beginTransaction();
		Vector lvInputTrans = new Vector();
		lvInputTrans.addElement(laDBAccess);
		lvInputTrans.addElement(lvVector);

		boolean lbIsSuccessful = false;
		try
		{
			postTrans(lvInputTrans);

			lbIsSuccessful = true;

			// defect 9582
			// return Transaction Header to extract TransId   
			//return new Boolean(lbIsSuccessful);
			return laTransactionHeaderData;
			// end defect 9582 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"V21PLD transaction failed " + csClientHost);
			throw aeRTSEx;
		}
		finally
		{
			if (lbIsSuccessful)
			{
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			}
			else
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
				Log.write(
					Log.SQL_EXCP,
					this,
					CommonConstant.MSG_ROLLBACK_ISSUED + csClientHost);
			}

		}
	}
}
