package com.txdot.isd.rts.client.accounting.business;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.client.accounting.ui.VCRegionalCollectionACC002;
import com.txdot.isd.rts.client.common.business.CommonClientBusiness;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.cache.PaymentStatusCodesCache;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.accounting.GenFundsRemittance;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * AccountingClientBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name		Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			09/21/2001	Added comments
 * MAbs			04/18/2002	Fixed bugs for too many records going 
 *							to MF in FundsRemittance 
 *							defect 3570
 * MAbs			04/24/2002	Inventory was not prompting 
 * 							defect 3451
 * Jeff S.		10/28/2003	Regional Coll. not allowed in client mode.
 *							Passing a map as a data object to 
 *							regionalInventory() If there is an exception
 *							(DB down,Server Down) then add the exception
 *							to the map and return it. If no exception 
 *							then update the map with the new Data object 
 *							and return the map.
 *							Changes: regionalInventory()
 *							defect 6543 Ver 5.1.5 Fix 1
 * Jeff S.		02/23/2004	Changed from FTP to LPR printing. Removed
 *							call to open and close FTP connection.
 *							modify printReports(Vector)
 *							defect 6848, 6898 Ver 5.1.6
 * Jeff S.		05/26/2004	Removed call to 
 *							Print.getDefaultPrinterProps() b/c it is not
 *							being used anymore.
 *							modify saveReports(ReportSearchData)
 *							defect 7078 Ver 5.2.0
 * K Harrell	04/27/2005	Java 1.4 Work
 * 							deprecated printReports()
 * 							defect 7884 Ver 5.2.3
 * K Harrell	05/19/2005	renaming of elements within 
 * 							FundsPymntDataList Object 
 * 							modify searchPaymentRecords()
 * 							defect 7899 Ver 5.2.3
 * K Harrell	07/21/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3  
 * K Harrell	03/09/2007	Implement SystemProperty.isRegion()
 * 							modify retrieveAcctCds()
 * 							defect 9085 Ver Special Plates 
 * B Hargrove	07/11/2008	Add check for Vendor Plate. Remove these
 * 							from list of returned account items for
 * 							Additional Collections. 
 * 							modify retrieveAcctCds()
 * 							defect 9689 Ver Defect MyPlates_POS
 * K Harrell	06/18/2009	Use ReportConstant for nextScreen 
 * 							modify voidPayment() 
 * 							defect 10011 Ver Defect_POS_F 
 * K Harrell	08/16/2009	Implement UtilityMethods.addPCLandSaveReport(),
 * 							 Cleanup. 
 * 							add addTrans()  
 * 							delete saveReports(), processHotCheckCredit(), 
 * 							 deductHotCheckCredit(), itemSeized(), 
 * 							 processRefund() 
 * 							modify processData(), voidPayment(), 
 * 							 remitFundsDueRecords() 
 * 							defect 8628 Ver Defect_POS_F 
 * B Hargrove	10/06/2011	Add check for Obsolete. Remove these
 * 							from list of returned account items for
 * 							Additional Collections. 
 * 							modify retrieveAcctCds()
 * 							defect 10418 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * The AccountingClientBusiness handles two tasks:
 * <ul><li>Deciding whether the function being processed can be handled 
 * on the client side, or has to go to the server side.  If it has to be
 * handled on the server side, it passes on the function to the Comm 
 * class which returns the new data object. <li>If it can be handled on 
 * the client side, it provides functions that can do that and return 
 * the new data object.  All of the business logic of functions that 
 * can be handled on the client side is found in this class.
 * </ul>
 *
 * @version Ver 6.9.0 	10/06/2011
 * @author 	Michael Abernethy
 * <br>Creation Date:		07/17/2001 16:48:22
 *
 */

public class AccountingClientBusiness
{
	// 1st three characters of Credit Account Codes
	private final static String CRD = "CRD";
	private final static String CRE = "CRE";
	private final static String DATA = "DATA";
	private final static String ERRMSG_EFT_ACCT =
		"Before remitting EFT funds, you must first set up at least"
			+ " one EFT account using \'Payment Account Updates\' under"
			+ " Local Options";
	private final static String ERRMSG_MISSING_ACCT =
		"Missing EFT Account";
	private final static String FNDREM = "FNDREM";
	private final static String MF = "MF";
	private final static String RPTNAME_FUNDS_REMIT =
		"FUNDS REMITTANCE VERIFICATION REPORT";
	private final static String RPTNO_2311 = "RTS.POS.2311";
	private final static String VOID = "VOID";

	/**
	 * Creates an AccountingClientBusiness.
	 */
	public AccountingClientBusiness()
	{
		super();
	}

	/**
	 * Add transaction for Accounting Events
	 *   (Deduct Hot Check, Hot Check, Item Seized, Refund)  
	 *
	 * @param  aaObject	Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object addTrans(Object aaObject)
		throws RTSException
	{
		CommonClientBusiness laCommonClientBusiness =
			new CommonClientBusiness();

		laCommonClientBusiness.processData(
			GeneralConstant.COMMON,
			CommonConstant.ADD_TRANS,
			aaObject);

		return aaObject;
	}

	/**
	 * This method serves as the entry point into the 
	 * AccountingClientBusiness class.  This method will be called by 
	 * the BusinessInterface and will parse the command based on the 
	 * functionId. It will then decide what to do by either calling an 
	 * internal method for any business logic that can be handled by 
	 * the client, or the Comm layer for logic that requires work to be
	 * done on the server side.
	 *
	 * @param  aiModuleName int 
	 * @param  aiFunctionId int 
	 * @param  aaObject 	Object 
	 * @return Object 
	 * @throws RTSException 
	 */
	public Object processData(
		int aiModuleName,
		int aiFunctionId,
		Object aaObject)
		throws RTSException
	{
		switch (aiFunctionId)
		{
			case AccountingConstant.REMIT_FUNDS_DUE_RECORDS :
				{
					return remitFundsDueRecords(
						aiModuleName,
						aiFunctionId,
						aaObject);
				}
			case AccountingConstant
				.RETRIEVE_FUNDS_DUE_SUMMARY_RECORDS :
				{
					return retrieveFundsDueRecords(
						aiModuleName,
						aiFunctionId,
						aaObject);
				}
			case AccountingConstant.RETRIEVE_ACCOUNT_CODES :
				{
					return retrieveAcctCds();
				}
			case AccountingConstant.SEARCH_PAYMENT_RECORDS :
				{
					return searchPaymentRecords(
						aiModuleName,
						aiFunctionId,
						aaObject);
				}
			case AccountingConstant.VOID_PAYMENT :
				{
					return voidPayment(
						aiModuleName,
						aiFunctionId,
						aaObject);
				}
			case AccountingConstant.REGIONAL_INVENTORY :
				{
					return regionalInventory(aaObject);
				}
			// defect 10123 
			// Intentionally dropping through
			case AccountingConstant.ITEM_SEIZED : 
			case AccountingConstant.PROCESS_DEDUCT_HOT_CHECK_CREDIT :
			case AccountingConstant.PROCESS_HOT_CHECK_CREDIT :
			case AccountingConstant.PROCESS_REFUND :
				{
					return addTrans(aaObject);
				}
			// end defect 10123 
			default :
				{
					return null;
				}
		}
	}

	/**
	 * Issue Inventory for Regional Collections 
	 *
	 * @param  aaObject	Object 
	 * @return Object
	 * @throws RTSException
	 */
	private Object regionalInventory(Object aaObject)
		throws RTSException
	{
		// defect 6543
		// Make the data object of map type and extract the "TRANS_DATA".
		// Call Comm.sendtoServer() like before.  If there is an exception 
		// (DB Down, Server Down) then add the exception to the map and 
		// return the map.  If there are no exceptions then update the map 
		// with then new "TRANS_DATA" and return the map.
		Map lhmMap = new HashMap();

		try
		{
			lhmMap = (Map) aaObject;
			CompleteTransactionData laCTData =
				(CompleteTransactionData) lhmMap.get(
					VCRegionalCollectionACC002.TRANS_DATA);

			Object laInvObj =
				Comm.sendToServer(
					GeneralConstant.INVENTORY,
					InventoryConstant.ISSUE_INVENTORY,
					laCTData);

			// Put new Data Object into map if it is not null
			if (laInvObj != null)
			{
				// Update the map with the new Data Object
				lhmMap.put(
					VCRegionalCollectionACC002.TRANS_DATA,
					laInvObj);
			}
			return lhmMap;
		}
		catch (RTSException aeRTSEx)
		{
			lhmMap.put(VCRegionalCollectionACC002.EXCEPTION, aeRTSEx);
			return lhmMap;
		}
		catch (ClassCastException aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		// end defect 6543	
	}

	/**
	 * Remits Funds Due records to the MF and then to the DB, and then 
	 * prints a report.
	 *
	 * @param  aiModuleName	int
	 * @param  aiFunctionId	int 
	 * @param  aaObject  	Object 
	 * @return Object
	 * @throws RTSException
	 * ---------------------------------------------------------------------
	 */
	private Object remitFundsDueRecords(
		int aiModuleName,
		int aiFunctionId,
		Object aaObject)
		throws RTSException
	{
		FundsUpdateData laFundsUpdateData = (FundsUpdateData) aaObject;
		Map laMap = new HashMap();
		laMap.put(DATA, laFundsUpdateData);
		laMap.put(MF, MFLogError.getErrorString());
		Object laObject =
			Comm.sendToServer(aiModuleName, aiFunctionId, laMap);

		CommonClientBusiness laCommonClientBusiness =
			new CommonClientBusiness();

		laObject =
			laCommonClientBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.ADD_TRANS,
				laObject);

		// defect 8628 
		ReportProperties laRptProps = new ReportProperties(RPTNO_2311);
		laRptProps.initClientInfo();
		// end defect 8628 

		GenFundsRemittance laGenFundsRemittance =
			new GenFundsRemittance(RPTNAME_FUNDS_REMIT, laRptProps);

		Vector laVector = new Vector();
		laVector.add(laObject);

		laGenFundsRemittance.formatReport(laVector);

		// defect 8628
		// Key1 = FormattedRptData
		// Key2 = FileName
		// Key3 = ReportTitle
		// IntKey1 = NumberOfCopies
		// IntKey2 = OrientationCd 
		ReportSearchData laRptSearchData =
			new ReportSearchData(
				laGenFundsRemittance.getFormattedReport().toString(),
				FNDREM,
				RPTNAME_FUNDS_REMIT,
				ReportConstant.RPT_7_COPIES,
				ReportConstant.PORTRAIT);
		return UtilityMethods.addPCLandSaveReport(laRptSearchData);
		// end defect 8628
	}

	/**
	 * Handles retrieving the Account Codes for the ACC001 screen's 
	 * dropdown list.  It retrieves all of the account codes, gets rid of 
	 * the code's ACC001 is not interested in, and then sorts the account 
	 * codes before returning them.
	 *
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object retrieveAcctCds() throws RTSException
	{
		// get account codes from the cache, excluding those we don't 
		// care about
		Vector lvAccountCodes = new Vector();

		RTSDate laToday = RTSDate.getCurrentDate();

		lvAccountCodes =
			AccountCodesCache.getAcctCds(
				"",
				laToday.getYYYYMMDDDate(),
				AccountCodesCache.GET_ALL);

		int i = 0;

		// If workstation is a region, get only regional account codes - 
		// for Regional Collections
		// defect 9085 
		//if (UtilityMethods.isRegion())
		if (SystemProperty.isRegion())
		{
			while (i < lvAccountCodes.size())
			{
				AccountCodesData laAcctCodesData =
					(AccountCodesData) lvAccountCodes.get(i);
				String lsCompareString =
					laAcctCodesData.getAcctProcsCd().toUpperCase();
				if (!lsCompareString
					.equals(AccountCodesCache.REGIONAL))
				{
					lvAccountCodes.remove(i);
					continue;
				}
				i++;
			}
		}
		// Get all others, for Additional Collections
		else
		{
			while (i < lvAccountCodes.size())
			{
				AccountCodesData laAcctCodesData =
					(AccountCodesData) lvAccountCodes.get(i);
				String lsCompareString =
					laAcctCodesData.getAcctProcsCd().toUpperCase();

				// defect 9689
				// Add check for Vendor Plates
				// defect 10418
				// Add check for Obsolete
				if (lsCompareString.equals(AccountCodesCache.HOT_CHECK)
					|| lsCompareString.equals(AccountCodesCache.REFUND)
					|| lsCompareString.equals(AccountCodesCache.REGIONAL)
					|| lsCompareString.equals(AccountCodesCache.VENDOR)
					|| lsCompareString.equals(AccountCodesCache.OBSOLETE)					
					|| laAcctCodesData.getAcctItmCd().startsWith(CRE))
				{
					// end defect 10418
					// end defect 9689
					lvAccountCodes.remove(i);
					continue;
				}
				i++;
			}
		}
		// end defect 9085 

		Collections.sort(lvAccountCodes);

		return lvAccountCodes;
	}

	/**
	 * Retrieves Funds Due records from the MF
	 *
	 * @param  aiModuleName	int
	 * @param  aiFunctionId	int
	 * @param  aaObject		Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object retrieveFundsDueRecords(
		int aiModuleName,
		int aiFunctionId,
		Object aaObject)
		throws RTSException
	{
		FundsDueDataList aaFundsDueDataList =
			(FundsDueDataList) Comm.sendToServer(
				aiModuleName,
				aiFunctionId,
				aaObject);

		// If the workstation needs to set up EFT accounts and hasn't, 
		// throw an exception
		int liEFTAccntCd =
			OfficeIdsCache
				.getOfcId(SystemProperty.getOfficeIssuanceNo())
				.getEFTAccntCd();

		if ((aaFundsDueDataList.getPaymentAccounts() == null
			|| aaFundsDueDataList.getPaymentAccounts().size() == 0)
			&& liEFTAccntCd != 0)
		{
			throw new RTSException(
				RTSException.INFORMATION_MESSAGE,
				ERRMSG_EFT_ACCT,
				ERRMSG_MISSING_ACCT);
		}

		return aaFundsDueDataList;
	}

//	/**
//	 * Saves the printed report to the local machine
//	 * @param  aaRptSearchData	ReportSearchData
//	 * @return Object
//	 * @throws RTSException
//	 */
//	private Object saveReports(ReportSearchData aaRptSearchData)
//		throws RTSException
//	{
//		Vector lvReports = new Vector();
//
//		// defect 8628 
//		// Assumes Portrait
//		String lsRpt =
//			Print.getDefaultPageProps()
//				+ CommonConstant.SYSTEM_LINE_SEPARATOR
//				+ aaRptSearchData.getKey1();
//
//		String lsFileName =
//			UtilityMethods.saveReport(
//				lsRpt,
//				aaRptSearchData.getKey2(),
//				aaRptSearchData.getIntKey1());
//		// end defect 8628 
//
//		aaRptSearchData.setKey1(lsFileName);
//		lvReports.add(aaRptSearchData);
//		return lvReports;
//	}

	/**
	 * Searches the Funds Payment records on the MF
	 *
	 * @param  aiModuleName	int  
	 * @param  aiFunctionId	int 
	 * @param  aaObject		Object  
	 * @return Object
	 */
	private Object searchPaymentRecords(
		int aiModuleName,
		int aiFunctionId,
		Object aaObject)
		throws RTSException
	{
		Map laMap =
			(Map) Comm.sendToServer(
				aiModuleName,
				aiFunctionId,
				aaObject);

		FundsPaymentDataList laFundsPymntDataList =
			(FundsPaymentDataList) laMap.get(AccountingConstant.DATA);

		Vector lvFundsPymnt = laFundsPymntDataList.getFundsPymnt();

		for (int i = 0; i < lvFundsPymnt.size(); i++)
		{
			FundsPaymentData laFundsPymntData =
				(FundsPaymentData) lvFundsPymnt.get(i);
			String lsPymntStatusCd =
				laFundsPymntData.getPaymentStatusCode();

			PaymentStatusCodesData laPymntStatusCodesData =
				PaymentStatusCodesCache.getPymntStatusCd(
					lsPymntStatusCd);

			laFundsPymntData.setPaymentStatusDesc(
				laPymntStatusCodesData.getPymntStatusDesc());
		}

		return laMap;
	}

	/**
	 * Voids a Funds Payment records in Funds Inquiry.
	 *
	 * @param  aiModuleName	int
	 * @param  aiFunctionId	int
	 * @param  aaObject 	Object
	 * @return Object 
	 * @throws RTSException
	 */
	private Object voidPayment(
		int aiModuleName,
		int aiFunctionId,
		Object aaObject)
		throws RTSException
	{
		Map laDataMap = (Map) aaObject;
		laDataMap.put(MF, MFLogError.getErrorString());
		FundsUpdateData laFundsUpdateData =
			(FundsUpdateData) laDataMap.get(
				AccountingConstant.UPDATE_DATA);
		Object laObject =
			Comm.sendToServer(aiModuleName, aiFunctionId, laDataMap);
		Map laMap = (Map) laObject;
		CompleteTransactionData laCTData =
			new CompleteTransactionData();
		laCTData.setTransCode(TransCdConstant.VOIDFD);
		laCTData.setFundsUpdate(laFundsUpdateData);
		CommonClientBusiness laCommonClientBusiness =
			new CommonClientBusiness();

		laCommonClientBusiness.processData(
			GeneralConstant.COMMON,
			CommonConstant.ADD_TRANS,
			laCTData);

		// defect 8628 
		ReportProperties laRptProps = new ReportProperties(RPTNO_2311);
		laRptProps.initClientInfo();
		// end defect 8628 

		GenFundsRemittance laGenFundsRemittance =
			new GenFundsRemittance(RPTNAME_FUNDS_REMIT, laRptProps);

		Vector lvVector = new Vector();
		lvVector.add(laCTData);
		lvVector.add(VOID);

		laGenFundsRemittance.formatReport(lvVector);

		// defect 8628
		// Key1 = FormattedRptData
		// Key2 = FileName
		// Key3 = ReportTitle
		// IntKey1 = NumberOfCopies
		// IntKey2 = OrientationCd 
		ReportSearchData laRptSearchData =
			new ReportSearchData(
				laGenFundsRemittance.getFormattedReport().toString(),
				FNDREM,
				RPTNAME_FUNDS_REMIT,
				ReportConstant.RPT_7_COPIES,
				ReportConstant.PORTRAIT);
		// end defect 8628 
				
		laRptSearchData.setData(laMap);
		
		// defect 10011 
		laRptSearchData.setNextScreen(
			ReportConstant.RPR000_NEXT_SCREEN_PREVIOUS);
		// end defect 10011
		
		// defect 8628 
		return UtilityMethods.addPCLandSaveReport(laRptSearchData);
		// end defect 8628
	}
}
