package com.txdot.isd.rts.server.miscreg;

import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.miscellaneous.GenDisabledPlacardReport;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

import com.txdot.isd.rts.server.common.business.VehicleInquiry;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.server.db.*;
import com.txdot.isd.rts.server.reports.ReportsServerBusiness;

/*
 * MiscRegServerBusiness.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/21/2008	Created 
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	11/09/2008	add populateDsabldPlcrdDataFromMvFunc() for 
 * 							 performance testing. 
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/17/2008	Writing extraneous InProcs Record on 
 * 							No Record Found. 
 * 							queryDisabledPlacardCustInfo() 
 * 							defect 9871 Ver Defect_POS_B
 * K Harrell	11/25/2008	Need to assign Substaid on qryTransaction() 
 * 							modify chkIfVoidable() 
 * 							defect 9873 Ver Defect_POS_B
 * K Harrell	06/28/2009	Implement MotorVehicleFunctionTransactionData.
 * 							 setOwnerData(). Sort members. 
 * 							modify populateDsabldPlrdDataFromMvFund() 
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	07/25/2009	Remove reference to MobilityDsabldIndi
 * 							modify populateDsabldPlcrdDataFromMvFunc()
 * 							defect 10133 Ver Defect_POS_F
 * K Harrell	08/17/2009	Implement new ReportSearchData constructor
 * 							modify genDisabledPlacardReport() 
 * 							defect 8628 Ver Defect_POS_F
 * Min Wang		12/17/2009	fix wrong report id on Disabled Placard 
 * 							Report.
 * 							modify genDisabledPlacardReport()
 * 							defect 10276 Ver Defect_POS_H
 * K Harrell	03/09/2010	Remove Disabled Placard Stress Test loading
 * 							delete populateDsabldPlcrdDataFromMvFunc()
 * 							defect 10210 Ver POS_640
 * K Harrell	05/25/2010	Modified for new Timed Permit Processing 
 * 							add getPrmt() 
 * 							defect 10491 Ver 6.5.0   
 * K Harrell	08/11/2010	Set IntKey4(1) when Search Archive
 * 							modify getPrmt() 
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	09/27/2010	Modify getPrmt() & no longer modify the 
 * 							TransCd to "Title" when search by VIN
 * 							modify getPrmt() 
 * 							defect 10598 Ver 6.6.0 
 * K Harrell	06/19/2011	Modify for MODPT 
 * 							modify getPrmt() 
 * 							defect 10844 Ver 6.8.0 
 * K Harrell	07/12/2011	Check not null MfVehicleData, VehicleData	
 * 							when getting VehTypeCd 
 * 							modify getPrmt() 
 * 							defect 10844 Ver 6.8.0 
 * K Harrell	08/10/2011	Reset InProcsIndi for returned record if same
 * 							OfcissuanceNo/WsId and No Transaction in Process
 * 							modify queryDisabledPlacardCustInfo() 
 * 							defect 10973 Ver 6.8.1 
 * ---------------------------------------------------------------------
 */

/**
 * The MiscRegServerBusiness dispatch the incoming request to the function
 * request on the server side for Miscellaneous Registration events. It also
 * returns the result back to the caller
 * 
 * @version 6.8.1 		08/10/2011	
 * @author Kathy Harrell 
 * @since				10/21/2008
 */
public class MiscRegServerBusiness
{

	private String csClientHost = "Unknown";

	/**
	 * MiscRegServerBusiness constructor comment.
	 */
	public MiscRegServerBusiness()
	{
		super();
	}

	/**
	 * MiscRegServerBusiness constructor comment.
	 * 
	 * @param asClientHost
	 *            String
	 */
	public MiscRegServerBusiness(String asClientHost)
	{
		super();
		csClientHost = asClientHost;
	}

	/**
	 * Determine if transaction to add placard is available to be voided.
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object chkIfVoidable(Object aaData) throws RTSException
	{
		Vector lvVector = (Vector) aaData;
		// 0 element - aaData
		// 1 element - aiCommand
		DisabledPlacardCustomerData laDPCustData = (DisabledPlacardCustomerData) lvVector
				.elementAt(0);
		DatabaseAccess laDBA = null;
		DisabledPlacardTransactionData laDPTransData = null;
		String lsTransId = CommonConstant.STR_SPACE_EMPTY;
		boolean lbVoidable = false;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			DisabledPlacardTransaction laDPTrans = new DisabledPlacardTransaction(
					laDBA);

			laDPTransData = laDPTrans
					.qryDsabldPlcrdTransForDelete(laDPCustData);

			if (laDPTransData != null)
			{
				// Determine if Transaction has been posted.
				Transaction laTrans = new Transaction(laDBA);

				TransactionData laTransData = new TransactionData();
				laTransData.setOfcIssuanceNo(laDPTransData
						.getOfcIssuanceNo());
				laTransData.setCustSeqNo(laDPTransData.getCustSeqNo());
				laTransData.setTransWsId(laDPTransData.getTransWsId());
				laTransData.setTransAMDate(laDPTransData
						.getTransAMDate());
				// defect 9873
				laTransData.setSubstaId(laDPTransData.getSubstaId());
				// end defect 9873
				laTransData.setTransTime(laDPTransData.getTransTime());

				lsTransId = UtilityMethods.getTransId(laTransData
						.getOfcIssuanceNo(),
						laTransData.getTransWsId(), laTransData
								.getTransAMDate(), laTransData
								.getTransTime());

				Vector lvTransData = laTrans
						.qryTransaction(laTransData);

				if (lvTransData.size() != 0)
				{
					laTransData = (TransactionData) lvTransData
							.elementAt(0);
					if (laTransData.getTransPostedMfIndi() == 0)
					{
						lbVoidable = true;
					} else
					{
						GeneralSearchData laGeneralSearchData = new GeneralSearchData();
						laGeneralSearchData.setKey1(lsTransId);
						MfAccess laMFA = new MfAccess(csClientHost);

						try
						{
							int liTransPresentOnMF = laMFA
									.voidTransactions(laGeneralSearchData);

							lbVoidable = liTransPresentOnMF != 0;
						}
						// Catch Mainframe down scenario
						catch (RTSException aeRTSEx)
						{
							if (aeRTSEx.getMsgType().equals(
									RTSException.MF_DOWN))
							{
								throw new RTSException(331);
							}
							throw aeRTSEx;
						}
					}
				}
			}
			lvVector.add(new Boolean(lbVoidable));
			if (lbVoidable)
			{
				lvVector.add(lsTransId);
			}
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return lvVector;
		} catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		} finally
		{
			laDBA = null;
		}
	}

	/**
	 * Get Permit
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object getPrmt(Object aaData) throws RTSException
	{
		GeneralSearchData laGSD = (GeneralSearchData) aaData;
		String lsTransCd = laGSD.getKey3();
		boolean lbContinue = lsTransCd.equals(TransCdConstant.PT72);
		boolean lbDocNoVehSearch = laGSD.getKey1().equals(
				CommonConstant.DOC_NO);
		boolean lbByPrmtId = laGSD.getKey1().equals(
				CommonConstant.PRMT_PRMTID);
		PermitData laPrmtData = new PermitData();
		Object laReturn = laPrmtData;

		// defect 10844
		boolean lbModify = lsTransCd.equals(TransCdConstant.MODPT);

		if (lsTransCd.equals(TransCdConstant.RPRPRM))
		{
			String lsTransId = laGSD.getKey4();
			DatabaseAccess laDBA = new DatabaseAccess();
			PermitTransaction laPrmtSQL = new PermitTransaction(laDBA);
			laDBA.beginTransaction();
			int liCount = laPrmtSQL
					.qryUnpostedPermitTransaction(lsTransId);
			laDBA.endTransaction(DatabaseAccess.COMMIT);

			if (liCount > 0)
			{
				laPrmtData = new PermitData();
				laPrmtData.setPrmtIssuanceId(lsTransId);
				laPrmtData.setAuditTrailTransId(lsTransId);
				laPrmtData.setNoMFRecs(1);
				return laPrmtData;
			}
		}
		// end defect 10844

		if (!lbDocNoVehSearch)
		{
			String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
			try
			{
				MfAccess laMFA = new MfAccess(csClientHost);

				lsMfResponse = laMFA.retrievePermit(laGSD);

				if (!UtilityMethods.isEmpty(lsMfResponse))
				{
					int liNoOfRecs = rtnNoOfRecsMF(lsMfResponse);

					if (liNoOfRecs != 0)
					{
						laPrmtData = laMFA
								.setMFPermitDataFromMfResponse(
										lsMfResponse, liNoOfRecs);

						if (lsTransCd.equals(TransCdConstant.PRMDUP))
						{
							laPrmtData.setChrgFeeIndi(laGSD
									.getIntKey4());
							laPrmtData.setPrmDupTrans(true);
						} else if (lbByPrmtId)
						{
							laPrmtData.setNo30DayPrmts(laGSD
									.getIntKey3());
						}

						// defect 10844
						// Query RTS POS DB for other ModPT transactions
						// against same PermitIssuanceId
						if (lbModify)
						{
							laPrmtData.setModPtTrans(true);
							DatabaseAccess laDBA = new DatabaseAccess();
							ModifyPermitTransactionHistory laModPrmtSQL = new ModifyPermitTransactionHistory(
									laDBA);
							try
							{
								laDBA.beginTransaction();
								laPrmtData
										.setPriorModTransList(laModPrmtSQL
												.qryModifyPermitTransaction(laPrmtData
														.getPrmtIssuanceId()));
								laDBA
										.endTransaction(DatabaseAccess.COMMIT);
							} catch (RTSException aeRTSEx)
							{
								try
								{
									laDBA
											.endTransaction(DatabaseAccess.COMMIT);
								} catch (RTSException aeRTSEx1)
								{

								}
							}
						}
						// end defect 10844

						laPrmtData.setByPrmtId(lbByPrmtId);
						lbContinue = false;
					}
				}
			} catch (RTSException aeRTSEx)
			{
				laPrmtData.setMFDwnCd(1);
			}

			// defect 10844
			laPrmtData.setVehTypeCd();
			// end defect 10844

			laReturn = laPrmtData;
		}
		if (lbContinue)
		{
			VehicleInquiry laVehInq = new VehicleInquiry();

			// If Permit Application Search, reset Search Key
			if (!lbDocNoVehSearch)
			{
				laGSD.setKey1(CommonConstant.VIN);
			}
			laGSD.setIntKey2(CommonConstant.SEARCH_ACTIVE_INACTIVE);

			// defect 10598
			// laGSD.setKey3(TransCdConstant.TITLE);
			VehicleInquiryData laVehInqData = laVehInq.getVeh(laGSD);
			// end defect 10598

			// defect 10844
			String lsVehTypeCd = new String();
			if (laVehInqData.getMfVehicleData() != null
					&& laVehInqData.getMfVehicleData().getVehicleData() != null)
			{
				lsVehTypeCd = laVehInqData.getMfVehicleData()
						.getVehicleData().getVehTypeCd();
			}
			// end defect 10844

			if (laVehInqData.getNoMFRecs() == 0)
			{
				laGSD.setIntKey2(CommonConstant.SEARCH_ARCHIVE);
				laGSD.setIntKey4(1);
				Vector lvSearch = new Vector();
				lvSearch.add(laVehInqData);
				lvSearch.add(laGSD);
				laVehInqData = laVehInq.getVeh(lvSearch);
				if (laGSD.getKey1().equals(CommonConstant.VIN)
						&& laVehInqData.getNoMFRecs() == 0)
				{
					laVehInqData.getMfVehicleData().getVehicleData()
							.setVin(laGSD.getKey2());

					// defect 10844
					laVehInqData.getMfVehicleData().getVehicleData()
							.setVehTypeCd(lsVehTypeCd);
					// end defect 10844
				}
			}
			laReturn = laVehInqData;
		}
		return laReturn;
	}

	/**
	 * genDisabledPlacardReport
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object genDisabledPlacardReport(Object aaData)
			throws RTSException
	{
		Vector lvExData = (Vector) aaData;
		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// defect 8628
			DisabledPlacardUIData laRptData = (DisabledPlacardUIData) lvExData
					.get(0);
			AdministrationLogData laLogData = (AdministrationLogData) lvExData
					.get(1);
			// UOW #1 BEGIN
			// ADMIN LOG
			AdministrationLog laAdminLog = new AdministrationLog(
					laDBAccess);
			laDBAccess.beginTransaction();
			laAdminLog.insAdministrationLog(laLogData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END
			// REPORT PROPERTIES
			ReportsServerBusiness laRptSrvrBus = new ReportsServerBusiness();
			// UOW #2 BEGIN
			// defect 10276
			ReportProperties laReportProperties = laRptSrvrBus
					.initReportProperties(laLogData
							.getReportSearchData(), laDBAccess,
					// ReportConstant.RPT_5051_REPORT_ID);
							ReportConstant.RPT_8001_REPORT_ID);
			// end defect 10176
			// UOW #2 END
			laReportProperties
					.setPageOrientation(ReportConstant.LANDSCAPE);
			// UOW #3 BEGIN
			DisabledPlacard laDsabldPlcrd = new DisabledPlacard(
					laDBAccess);
			laDBAccess.beginTransaction();
			Vector lvRptData = laDsabldPlcrd
					.qryReportDisabledPlacard(laRptData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END
			GenDisabledPlacardReport laGrp = new GenDisabledPlacardReport(
					ReportConstant.RPT_8001_REPORT_TITLE,
					laReportProperties, laRptData);
			laGrp.formatReport(lvRptData);
			// RETURN REPORT DATA/ATTRIBUTES
			return new ReportSearchData(laGrp.getFormattedReport()
					.toString(), ReportConstant.RPT_8001_ONLN_FILENAME,
					ReportConstant.RPT_8001_REPORT_TITLE,
					ReportConstant.RPT_1_COPY, ReportConstant.LANDSCAPE);
			// end defect 8628
		} catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Insert Disabled Customer Information
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object insertDisabledPlacardCustomerInfo(Object aaData)
			throws RTSException
	{
		DatabaseAccess laDBA = null;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			DisabledPlacardCustomer laDsbldPlcrdCust = new DisabledPlacardCustomer(
					laDBA);
			DisabledPlacardCustomerData laDPCustData = (DisabledPlacardCustomerData) aaData;
			laDPCustData = laDsbldPlcrdCust
					.insDisabledPlacardCustomer(laDPCustData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return laDPCustData;
		} catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		} finally
		{
			laDBA = null;
		}
	}

	// /**
	// * Populate Dsabled Placard Data from MvFuncTrans
	// *
	// * @param aaData
	// * @return
	// * @throws RTSException
	// */
	// private Object populateDsabldPlcrdDataFromMvFunc()
	// throws RTSException
	// {
	// DatabaseAccess laDBA = null;
	// DisabledPlacardCustomerData laDPCustData =
	// new DisabledPlacardCustomerData();
	// MotorVehicleFunctionTransactionData laMVFuncTransData =
	// new MotorVehicleFunctionTransactionData();
	//
	// try
	// {
	// laDBA = new DatabaseAccess();
	// laDBA.beginTransaction();
	//
	// MotorVehicleFunctionTransaction laMVFuncTransSQL =
	// new MotorVehicleFunctionTransaction(laDBA);
	//
	// Vector lvTrans =
	// laMVFuncTransSQL.qryMotorVehicleFunctionTransaction(
	// 39736,
	// 100000,
	// 190000);
	//
	// laDBA.endTransaction(DatabaseAccess.COMMIT);
	//
	// for (int i = 0; i < lvTrans.size(); i++)
	// {
	// laMVFuncTransData =
	// (
	// MotorVehicleFunctionTransactionData) lvTrans
	// .elementAt(
	// i);
	// String lsTransCd = laMVFuncTransData.getTransCd();
	// if (!lsTransCd.equals("INVVD")
	// && !lsTransCd.equals("SBRNW")
	// && !lsTransCd.equals("SPRSRV")
	// && !lsTransCd.equals("SPUNAC"))
	// {
	// try
	// {
	// laDBA.beginTransaction();
	// String lsCustId =
	// UtilityMethods.getTransId(
	// laMVFuncTransData.getOfcIssuanceNo(),
	// laMVFuncTransData.getTransWsId(),
	// laMVFuncTransData.getTransAMDate(),
	// laMVFuncTransData.getTransWsId());
	// lsCustId =
	// lsCustId.substring(0, 9)
	// + lsCustId.substring(12, 17);
	// laDPCustData =
	// new DisabledPlacardCustomerData();
	// laDPCustData.setCustId(lsCustId);
	// // Use AbstractValue 1 to 8
	// int liCustIdType = i % 8 + 1;
	// laDPCustData.setCustIdTypeCd(liCustIdType);
	// if (liCustIdType == 5)
	// {
	// laDPCustData.setInstIndi(1);
	// // defect 10112
	// laDPCustData.setInstName(
	// laMVFuncTransData
	// .getOwnerData()
	// .getName1());
	// // end defect 10112
	// }
	// else
	// {
	// laDPCustData.setInstIndi(0);
	// // defect 10112
	// String lsName =
	// laMVFuncTransData
	// .getOwnerData()
	// .getName1();
	// // end defect 10112
	// StringTokenizer lsStrTkn =
	// new StringTokenizer(lsName);
	// String[] larrName = new String[10];
	// int j = 0;
	// while (lsStrTkn.hasMoreTokens())
	// {
	// larrName[j] = lsStrTkn.nextToken();
	// j = j + 1;
	// }
	//
	// if (j >= 2)
	// {
	// laDPCustData.setDsabldLstName(
	// larrName[j - 1]);
	// laDPCustData.setDsabldFrstName(
	// larrName[0]);
	// }
	// else if (j == 1)
	// {
	// laDPCustData.setDsabldLstName(
	// larrName[0]);
	// laDPCustData.setDsabldFrstName("Fred");
	// }
	// laDPCustData.setDsabldMI("X");
	// }
	// // defect 10112
	// laDPCustData.setAddressData(
	// (AddressData) laMVFuncTransData
	// .getOwnerData()
	// .getAddressData());
	//
	// //laDPCustData.setAddressData(new AddressData());
	// //laDPCustData.getAddressData().setSt1(
	// // laMVFuncTransData.getOwnrSt1());
	// //laDPCustData.getAddressData().setSt2(
	// // laMVFuncTransData.getOwnrSt2() == null
	// // ? ""
	// // : laMVFuncTransData.getOwnrSt2());
	// // laDPCustData.getAddressData().setCity(
	// // laMVFuncTransData.getOwnrCity());
	// // laDPCustData.getAddressData().setState(
	// // laMVFuncTransData.getOwnrState());
	// // laDPCustData.getAddressData().setCntry(
	// // laMVFuncTransData.getOwnrCntry() == null
	// // ? ""
	// // : laMVFuncTransData.getOwnrCntry());
	// // laDPCustData.getAddressData().setZpcd(
	// // laMVFuncTransData.getOwnrZpCd());
	// // laDPCustData.getAddressData().setZpcdp4(
	// // laMVFuncTransData.getOwnrZpCdP4() == null
	// // ? ""
	// // : laMVFuncTransData.getOwnrZpCdP4());
	// // end defect 10112
	//
	// laDPCustData.setResComptCntyNo(
	// laMVFuncTransData.getResComptCntyNo());
	// laDPCustData.setPermDsabldIndi(1);
	//
	// // defect 10133
	// //laDPCustData.setMobltyDsabldIndi(1);
	// // end defect 10133
	//
	// laDPCustData.setResComptCntyNo(
	// laMVFuncTransData.getResComptCntyNo());
	// DisabledPlacardCustomer laDPCust =
	// new DisabledPlacardCustomer(laDBA);
	//
	// laDPCustData =
	// laDPCust.insDisabledPlacardCustomer(
	// laDPCustData,
	// true);
	//
	// Vector lvDPData = new Vector();
	// DisabledPlacardData laDPData =
	// new DisabledPlacardData();
	//							
	// // defect 10133
	// laDPData.setAcctItmCd("PDC");
	// // end defect 10133
	//						
	// laDPData.setInvItmCd("PDC");
	// laDPData.setRTSEffDate(20081010);
	// laDPData.setRTSExpMo(10);
	// laDPData.setRTSExpYr(2012);
	// laDPData.setCompleteIndi(1);
	// laDPData.setInvItmNo("P" + i);
	// lvDPData.add(laDPData);
	// laDPData.setCustIdntyNo(
	// laDPCustData.getCustIdntyNo());
	//
	// DisabledPlacardTransactionData laDPTransData =
	// new DisabledPlacardTransactionData();
	// laDPTransData.setOfcIssuanceNo(
	// laMVFuncTransData.getOfcIssuanceNo());
	// laDPTransData.setSubstaId(
	// laMVFuncTransData.getSubstaId());
	// laDPTransData.setTransWsId(
	// laMVFuncTransData.getTransWsId());
	// laDPTransData.setTransAMDate(
	// laMVFuncTransData.getTransAMDate());
	// laDPTransData.setTransTime(
	// laMVFuncTransData.getTransTime());
	// laDPTransData.setCustSeqNo(
	// laMVFuncTransData.getCustSeqNo());
	// laDPTransData.setCustIdntyNo(
	// laDPCustData.getCustIdntyNo());
	//
	// laDPTransData.setTransCd("BPM");
	// laDPTransData.setTransEmpId("RTSGENX");
	// RTSDate laRTSDate =
	// new RTSDate(
	// RTSDate.AMDATE,
	// laDPTransData.getTransAMDate());
	// laRTSDate.setTime(laDPTransData.getTransTime());
	// laDPTransData.setTransTimestmp(laRTSDate);
	// laDPTransData.setDsabldPlcrd(lvDPData);
	// DisabledPlacardTransaction laDPTransSQL =
	// new DisabledPlacardTransaction(laDBA);
	// laDPTransSQL.insDisabldPlcrdTrans(
	// laDPTransData);
	// laDBA.endTransaction(DatabaseAccess.COMMIT);
	// }
	// catch (RTSException aeRTSEx)
	// {
	// laDBA.endTransaction(DatabaseAccess.ROLLBACK);
	// }
	// }
	// }
	// return null;
	// }
	// catch (RTSException aeRTSEx)
	// {
	// laDBA.endTransaction(DatabaseAccess.ROLLBACK);
	// throw aeRTSEx;
	// }
	// finally
	// {
	// laDBA = null;
	// }
	// }
	/**
	 * Process data directs the server business method calls
	 * 
	 * @param aiModule
	 *            int
	 * @param aiFunctionId
	 *            int
	 * @param aaData
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object processData(int aiModule, int aiFunctionId,
			Object aaData) throws RTSException
	{
		switch (aiFunctionId)
		{
		case MiscellaneousRegConstant.SEARCH:
			return queryDisabledPlacardCustInfo(aaData);
		case MiscellaneousRegConstant.INSERT:
			return insertDisabledPlacardCustomerInfo(aaData);
		case MiscellaneousRegConstant.UPDATE:
			return updateDisabledPlacardCustInfo(aaData);
		case MiscellaneousRegConstant.CHKIFVOIDABLE:
			return chkIfVoidable(aaData);
		case MiscellaneousRegConstant.SETINPROCS:
			return setDisabledPlacardCustInProcs(aaData);
		case MiscellaneousRegConstant.GENERATE_DSABLD_PLCRD_REPORT:
			return genDisabledPlacardReport(aaData);
		case MiscellaneousRegConstant.PRMTINQ:
			return getPrmt(aaData);
		}
		return null;
	}

	/**
	 * Query Disabled Placard Customer Information
	 * 
	 * @param aaData
	 * @return
	 * @throws RTSException
	 */
	private Object queryDisabledPlacardCustInfo(Object aaData)
			throws RTSException
	{
		Vector lvVector = new Vector();
		Vector lvData = new Vector();
		DatabaseAccess laDBA = null;
		boolean lbNoRecordFound = false;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			DisabledPlacardCustomer laDPCust = new DisabledPlacardCustomer(
					laDBA);
			DisabledPlacardCustomerData laOrigDPCustData = (DisabledPlacardCustomerData) aaData;
			lvVector = laDPCust
					.qryDisabledPlacardCustomer(laOrigDPCustData);
			lvData = (Vector) lvVector.elementAt(1);
			if (lvData.size() > 0)
			{
				for (int i = 0; i < lvData.size(); i++)
				{
					DisabledPlacardCustomerData laDPCustData = (DisabledPlacardCustomerData) lvData
							.elementAt(i);
					// Capture Placard Data at the same time
					if (!laDPCustData.isNoRecordFound())
					{
						lbNoRecordFound = false;
						DisabledPlacard laDsabldPlcrd = new DisabledPlacard(
								laDBA);
						Vector lvPlcrd = laDsabldPlcrd
								.qryDisabledPlacard(laDPCustData);
						UtilityMethods.sort(lvPlcrd);
						laDPCustData.setDsabldPlcrd(lvPlcrd);
					} else
					{
						laDPCustData.setDsabldPlcrd(new Vector());
						// defect 9871
						lbNoRecordFound = true;
						// end defect 9871
					}
				}
			}
			if (!laOrigDPCustData.getTransCd().equals(
					MiscellaneousRegConstant.INQ))
			{
				if (lvData.size() == 1 && !lbNoRecordFound)
				{
					DisabledPlacardCustomerData laDPCustData = (DisabledPlacardCustomerData) lvData
							.elementAt(0);
					
					// defect 10973 
					// Reset InProcsIndi for returned record if same 
					// OfcissuanceNo/WsId and No Transaction in Process. 
					if (laDPCustData.getInProcsIndi() == 1)
					{
						laOrigDPCustData.setInProcsIdntyNo(laDPCustData
								.getInProcsIdntyNo());
						laOrigDPCustData.setCustIdntyNo(laDPCustData
								.getCustIdntyNo());

						if (!laDPCust
								.qryDisabldPlacardInProcs(laOrigDPCustData))
						{
							laDPCustData.setInProcsIndi(0);
						}
					}
					// end defect 10973 

					// Only Set InProcess if Not Already in Process
					if (laDPCustData.getInProcsIndi() == 0)
					{
						laDPCustData.setOfcIssuanceNo(laOrigDPCustData
								.getOfcIssuanceNo());
						laDPCustData
								.setWsId(laOrigDPCustData.getWsId());
						laDPCustData.setEmpId(laOrigDPCustData
								.getEmpId());
						laDPCust
								.setInProcsDisabledPlacardCustomer(laDPCustData);
					}
				}
			}
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return lvVector;
		} catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		} finally
		{
			laDBA = null;
		}
	}

	/**
	 * Set Disabled Placard Customer In Procs
	 * 
	 * @param aaData
	 * @return
	 * @throws RTSException
	 */
	private Object setDisabledPlacardCustInProcs(Object aaData)
			throws RTSException
	{
		DatabaseAccess laDBA = null;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			DisabledPlacardCustomer laDsbldPlcrdCust = new DisabledPlacardCustomer(
					laDBA);
			DisabledPlacardCustomerData laDPCustData = (DisabledPlacardCustomerData) aaData;
			laDsbldPlcrdCust
					.setInProcsDisabledPlacardCustomer(laDPCustData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return null;
		} catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		} finally
		{
			laDBA = null;
		}
	}

	/**
	 * Update Disabled Placard Customer Info
	 * 
	 * @param aaData
	 * @return
	 * @throws RTSException
	 */
	private Object updateDisabledPlacardCustInfo(Object aaData)
			throws RTSException
	{
		DatabaseAccess laDBA = null;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			DisabledPlacardCustomer laDsbldPlcrdCust = new DisabledPlacardCustomer(
					laDBA);
			DisabledPlacardCustomerData laDPCustData = (DisabledPlacardCustomerData) aaData;
			laDsbldPlcrdCust.updDisabledPlacardCustomer(laDPCustData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			return null;
		} catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		} finally
		{
			laDBA = null;
		}
	}

	/**
	 * Set Number Of records from MFAccess call
	 * 
	 * @param asMfResponse
	 *            String - Header/Vehicle data
	 * @return int
	 */
	private int rtnNoOfRecsMF(String asMfResponse)
	{
		MfAccess laMfAccess = new MfAccess();
		int liNoOfRecs = 0;
		final int NO_OF_RECS_OFFSET = 145;
		final int NO_OF_RECS_LENGTH = 3;
		liNoOfRecs = Integer.valueOf(
				laMfAccess.getStringFromZonedDecimal(asMfResponse
						.substring(NO_OF_RECS_OFFSET, NO_OF_RECS_OFFSET
								+ NO_OF_RECS_LENGTH))).intValue();

		return liNoOfRecs;
	}

}