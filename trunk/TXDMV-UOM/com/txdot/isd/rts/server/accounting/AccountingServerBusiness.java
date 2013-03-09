package com.txdot.isd.rts.server.accounting;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.server.db.PaymentAccount;

import com.txdot.isd.rts.services.cache.PaymentTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.AccountingConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
/* 
 * AccountingServerBusiness.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			04/18/2002	Fixed bugs for too many records going to MF 
 * 							in FundsRemittance 
 * 							defect 3570
 * MAbs			04/24/2002	Fixed bugs for too many records going to MF 
 * 							in FundsRemittance 
 * 							defect 3570
 * MAbs			05/29/2002	Changed MfAccess to lazy instantiation
 * MAbs			07/12/2002	Changed RTSException to avoid 
 * 							NullPointerException
 * K. Harrell 	11/21/2002  Inquiry by Funds Due Date not 
 * 							showing data when no payments
 * 							defect 5053 
 * Ray Rowehl	02/03/2003	Pass Client Host Name to 
 *							MainFrame
 *							defect 4588
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7884 Ver 5.2.3
 * K Harrell	05/19/2005	FundsPaymentDataList Object renaming
 * 							modify searchPymntRecs()
 * 							defect 7899 Ver 5.2.3  
 * K Harrell	07/02/2009	Remove 2nd parameter on MF call 
 * 							modify remitFundsDueRecs(), 
 * 							  retrieveFundsDueSmryRecs(), 
 * 							  searchPymntRecs() 
 * 							defect 10112 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */
/**
 * Accounting Server Business
 * 
 * @version	Defect_POS_F	07/02/2009
 * @author	Michael Abernethy
 * <br>Creation Date:		08/13/2001 08:56:51
 */
public class AccountingServerBusiness
{
	// String 
	private String csClientHost = "Unknown";

	/**
	 * AccountingServerBusiness constructor comment.
	 */
	public AccountingServerBusiness()
	{
		super();
	}

	/**
	 * AccountingServerBusiness constructor comment.
	 * 
	 * @param String asClientHost
	 */
	public AccountingServerBusiness(String asClientHost)
	{
		super();
		csClientHost = asClientHost;
	}

	/**
	 * processData
	 *  
	 * @param  aiModule int
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
		switch (aiFunctionId)
		{
			case AccountingConstant.REMIT_FUNDS_DUE_RECORDS :
				return remitFundsDueRecs(aaData);

			case AccountingConstant
				.RETRIEVE_FUNDS_DUE_SUMMARY_RECORDS :
				return retrieveFundsDueSmryRecs(aaData);

			case AccountingConstant.SEARCH_PAYMENT_RECORDS :
				return searchPymntRecs(aaData);

			case AccountingConstant.VOID_PAYMENT :
				return voidPymnt(aaData);
		}
		return null;
	}

	/**
	 * remitFundsDueRecs
	 *  
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object remitFundsDueRecs(Object aaData) throws RTSException
	{
		Map laMap = (Map) aaData;
		FundsUpdateData laUpdateData =
			(FundsUpdateData) laMap.get("DATA");
		MfAccess laMfAccess = new MfAccess(csClientHost);

		// defect 10112 
		// 2nd parameter not used, not needed
		// String lsMfCode = (String) laMap.get("MF");
		int liTraceNo = laMfAccess.sendFundsUpdateToMF(laUpdateData);
		//, lsMfCode);
		// end defect 10112

		if (liTraceNo == Integer.MIN_VALUE)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"An error has occurred on the MF.  Nothing will "
					+ "be written to the DB or MF",
				"MF Error");
		}
		laUpdateData.setTraceNo(liTraceNo);

		CompleteTransactionData laTransData =
			new CompleteTransactionData();
		laTransData.setFundsUpdate(laUpdateData);
		if (laUpdateData.getPaymentTypeCd() == PaymentTypeCache.EFT)
			laTransData.setTransCode(TransCdConstant.EFTFND);
		else
			laTransData.setTransCode(TransCdConstant.FNDREM);
		return laTransData;
	}

	/**
	 * retrieveFundsDueSmryRecs
	 *  
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object retrieveFundsDueSmryRecs(Object aaData)
		throws RTSException
	{
		Map laMap = (Map) aaData;
		int liOfcIssuanceNo =
			((Integer) laMap.get(AccountingConstant.OFC)).intValue();
		int liSubId =
			((Integer) laMap.get(AccountingConstant.SUB)).intValue();

		//Get the records from the mainframe
		MfAccess laMfAccess = new MfAccess(csClientHost);
		// defect 10112 
		// 2nd parameter not used, not needed 
		//String lsMfCode = (String) laMap.get("MF");
		FundsDueDataList laDataList =
			laMfAccess.retrieveFundsRemittanceRecord(liOfcIssuanceNo);
		// ,lsMfCode);
		// end defect 10112 

		//Get payment account information from the database
		DatabaseAccess laDBAccess = null;
		Vector lvPaymentAccounts = null;

		try
		{
			Log.write(
				Log.APPLICATION,
				this,
				"Starting DB call to PaymentAccount");
			laDBAccess = new DatabaseAccess();
			laDBAccess.beginTransaction();
			PaymentAccount laPayAccount =
				new PaymentAccount(laDBAccess);
			PaymentAccountData laTempData = new PaymentAccountData();
			laTempData.setOfcIssuanceNo(liOfcIssuanceNo);
			laTempData.setSubstaId(liSubId);
			lvPaymentAccounts =
				laPayAccount.qryPaymentAccount(laTempData);
			Log.write(
				Log.APPLICATION,
				this,
				"Successful DB call to PaymentAccount");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.APPLICATION,
				this,
				"Failed DB call to PaymentAccount");
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess.endTransaction(DatabaseAccess.NONE);
		}

		laDataList.setPaymentAccounts(lvPaymentAccounts);

		// Fill in the setDueAmount, which is equal to 
		// entDueAmount - fundsReceivedAmount
		for (int i = 0; i < laDataList.getFundsDue().size(); i++)
		{
			FundsDueData laTempData2 =
				(FundsDueData) laDataList.getFundsDue().get(i);
			laTempData2.setDueAmount(
				laTempData2.getEntDueAmount().subtract(
					laTempData2.getFundsReceivedAmount()));
		}

		return laDataList;
	}

	/**
	 * searchPymntRecs
	 *  
	 * @return Object
	 * @param aaData Object
	 * @throws RTSException 
	 */
	private Object searchPymntRecs(Object aaData) throws RTSException
	{
		GeneralSearchData laSearchData = (GeneralSearchData) aaData;

		MfAccess laMfAccess = new MfAccess(csClientHost);

		// defect 10112 
		// 2nd parameter not used, not needed 
		// String lsMfCode = laSearchData.getKey3();		
		FundsPaymentDataList laDataList =
			laMfAccess.retrieveFundsPaymentDataList(laSearchData);
		// ,lsMfCode);
		// end defect 10112 

		// defect 
		// added " && dataList.getVectorFundsDue().size() == 0"
		if (laDataList.getFundsPymnt().size() == 0
			&& laDataList.getFundsDue().size() == 0)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"No records found",
				"No records found");
		}

		int k, liLoc;
		int liLeft = 0;
		int liRight = laDataList.getFundsPymnt().size() - 1;

		for (k = liLeft + 1; k <= liRight; k++)
		{
			FundsPaymentData laFundsPayData =
				(FundsPaymentData) laDataList.getFundsPymnt().get(k);
			liLoc = k;
			while (liLeft < liLoc
				&& laFundsPayData.getFundsPaymentDate().compareTo(
					((FundsPaymentData) laDataList
						.getFundsPymnt()
						.get(liLoc - 1))
						.getFundsPaymentDate())
					< 0)
			{
				laDataList.getFundsPymnt().set(
					liLoc,
					laDataList.getFundsPymnt().get(liLoc - 1));
				liLoc--;
			}
			laDataList.getFundsPymnt().set(liLoc, laFundsPayData);
		}

		if (laDataList.getFundsPymnt().size() > 200)
		{
			int liCutOff = 201;
			FundsPaymentData laCutOffData =
				(FundsPaymentData) laDataList.getFundsPymnt().get(200);
			while (liCutOff < laDataList.getFundsPymnt().size())
			{
				FundsPaymentData laTempData =
					(FundsPaymentData) laDataList.getFundsPymnt().get(
						liCutOff);
				if (laTempData
					.getTraceNo()
					.equals(laCutOffData.getTraceNo()))
				{
					liCutOff++;
					continue;
				}
				else
					break;
			}
			laDataList.setFundsPymnt(
				new Vector(
					laDataList.getFundsPymnt().subList(
						0,
						liCutOff - 1)));
			laDataList.setTooManyRecords(true);
		}

		HashMap lhmMap = new HashMap();

		// CQU10005053 (Removed check for dataList.getVector().size >0 

		lhmMap.put(AccountingConstant.DATA, laDataList);

		return lhmMap;
	}

	/**
	 * voidPymnt
	 *  
	 * @return Object
	 * @param aaData Object
	 * @throws RTSException
	 */
	private Object voidPymnt(Object aaData) throws RTSException
	{
		HashMap lhmMap = (HashMap) aaData;
		FundsUpdateData laUpdateData =
			(FundsUpdateData) lhmMap.get(
				AccountingConstant.UPDATE_DATA);
		MfAccess laMfAccess = new MfAccess(csClientHost);

		// defect 10112 
		// 2nd parameter not used, not needed
		// String lsMfCode = (String) lhmMap.get("MF");
		int liReturnCode = laMfAccess.sendFundsUpdateToMF(laUpdateData);
		// , lsMfCode);
		// end defect 10112 

		if (liReturnCode != Integer.MIN_VALUE)
		{
			HashMap lhmReturnMap = new HashMap();
			lhmReturnMap.put(
				AccountingConstant.TRACE_NO,
				new Integer(laUpdateData.getTraceNo()));
			lhmReturnMap.put(
				AccountingConstant.PAYMENT_COMPLETE,
				new Boolean(true));
			return lhmReturnMap;
		}
		else
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"An error occurred while processing your request."
					+ "  Please contact Help Desk",
				"Error");
		}
	}
}
