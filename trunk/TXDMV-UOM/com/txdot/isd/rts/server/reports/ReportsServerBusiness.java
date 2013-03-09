package com.txdot.isd.rts.server.reports;

import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.reports.*;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.server.db.*;

/*
 * ReportsServerBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Listberger	07/16/2002	Modified code to pass in ReportSearchData 
 *							object to set the transaction date from the 
 *							form to the report header.  Modified method
 *							genQuickCounterReport.
 * JRue/		08/21/2002	Defect 4594, modified method 
 * BArredondo				genSalesTaxReport by changing
 *							getVector() to getVectorFundsDue().
 * K Harrell	01/28/2004	5.2.0 Merge.  See PCR 34 comments.
 *							deprecate genQuickCounterReport()
 *							add genReprintStickerReport()
 * 							Ver 5.2.0
 * K Harrell	07/30/2004	pass OfcIssuanceNo in qry so that regions 
 *							return data only in their regions.
 *							delete genQuickCounterReport() 
 *							modify genReprintStickerReport()
 *							modify processData() 
 *							defect 7385  Ver 5.2.1
 * S Johnston	05/09/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3
 * K Harrell	05/19/2005	FundsPaymentDataList element renaming
 * 							Also, moved Commit outside of finally 
 * 							block so that report not generated w/in
 * 							uow.  
 * 							defect 7899 Ver 5.2.3  	
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * K Harrell	10/17/2005	ReportsSQL moved from services.reports.reports
 * 							to server.db.TitlePackageReportSQL
 * 							Java 1.4 Cleanup 
 * 							defect 7896,7899 Ver 5.2.3
 * K Harrell	11/07/2005	Use ReportConstants to reference number of 
 * 							copies
 * 							modify genReprintStickerReport(),
 * 							genSalesTaxReport(),genTitlePackageReport()
 * 							defect 8379 Ver 5.2.3
 * J Ralph		09/29/2006	Add genExemptAuditData(), genExemptAuditReport()
 * 							modify processData()
 * 							Need to add report constants
 * 							defect 8900 Ver 5.3.0
 * J Ralph		10/20/2006  Added Exempt Audit Export/Report constants
 * 							and Admin logging
 * 							ReportConstant.GENERATE_EXEMPT_AUDIT_EXPORT,
 * 							ReportConstant.GENERATE_EXEMPT_AUDIT_REPORT
 * 							modify processdata(), genExemptAuditData(),
 * 							       genExemptAuditReport() 
 * 							defect 8900 Ver Exempts
 * K Harrell	03/20/2009	Add ETitle Report / Export Functionality 
 * 							modify processData(), genElectronicTitleData(),
 * 							 genElectronicTitleReport()
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	07/10/2009	Remove 2nd parameter where not used
 * 							modify genSalesTaxReport()
 * 							defect 10112 Ver Defect_POS_F  
 * K Harrell	08/17/2009	Online Title Package in Landscape. 
 * 							Cleanup class.
 * 							add initReportProperties() 
 * 							modify genElectronicTitleData(), 
 * 							 genElectronicTitleReport(), genExemptAuditData(),
 * 							 genExemptAuditReport(),genReprintStickerReport(), 
 * 							 genSalesTaxReport9),genTitlePackageReport()
 * 							defect 8628,10142 Ver Defect_POS_F
 * K Harrell	08/21/2009	Add logic for AdminLogData for ReprintSticker
 * 							Report. 
 * 							modify genReprintStickerReport()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	06/05/2011	add logic for Fraud Report/Export
 * 							add genFraudData(), getFraudReport()  
 * 							modify processData()
 * 							defect 10900 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */
/**
 * Handles the creation of the reports for the Reports Module.
 *
 * @version	6.8.0 			06/10/2011
 * @author 	Rakesh Duggirala
 * <br>Creation Date:		09/04/2001
 */
public class ReportsServerBusiness
{
	/**
	 * ReportsServerBusiness constructor
	 */
	public ReportsServerBusiness()
	{
		super();
	}

	/**
	 * genElectronicTitleData
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object genElectronicTitleData(Object aaData)
		throws RTSException
	{
		Vector lvExData = (Vector) aaData;

		// defect 8628 
		ElectronicTitleHistoryUIData laETtlHstryUIData =
			(ElectronicTitleHistoryUIData) lvExData.get(0);

		AdministrationLogData laAdminLogData =
			(AdministrationLogData) lvExData.get(1);

		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// UOW #1 BEGIN
			// ADMIN LOG 
			laDBAccess.beginTransaction();
			AdministrationLog laAdminLog =
				new AdministrationLog(laDBAccess);
			laAdminLog.insAdministrationLog(laAdminLogData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			// UOW #2 BEGIN 
			// ETITLE HSTRY 
			ElectronicTitleHistory laETtlHstry =
				new ElectronicTitleHistory(laDBAccess);
			laDBAccess.beginTransaction();
			String lsData =
				laETtlHstry.qryExportElectronicTitleHistory(
					laETtlHstryUIData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END 
			// end defect 8628 

			return lsData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * genElectronicTitleReportReport
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object genElectronicTitleReport(Object aaData)
		throws RTSException
	{
		Vector lvExData = (Vector) aaData;

		// defect 8628  
		ElectronicTitleHistoryUIData laETtlHstryUIData =
			(ElectronicTitleHistoryUIData) lvExData.get(0);

		AdministrationLogData laAdminLogData =
			(AdministrationLogData) lvExData.get(1);

		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// UOW #1 BEGIN 
			// ADMIN LOG
			laDBAccess.beginTransaction();
			AdministrationLog laAdminLog =
				new AdministrationLog(laDBAccess);
			laAdminLog.insAdministrationLog(laAdminLogData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			// UOW #2 BEGIN
			// REPORT PROPERTIES  
			ReportProperties laRptProps =
				initReportProperties(
					laAdminLogData.getReportSearchData(),
					laDBAccess,
					ReportConstant.RPT_5071_ETITLE_REPORT_ID);
			// UOW #2 END
			laRptProps.setPageOrientation(ReportConstant.LANDSCAPE);

			// UOW #3 BEGIN
			// ETITLE HSTRY
			ElectronicTitleHistory laETtlHstry =
				new ElectronicTitleHistory(laDBAccess);

			laDBAccess.beginTransaction();

			Vector lvRptData =
				laETtlHstry.qryElectronicTitleHistoryReport(
					laETtlHstryUIData);

			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END 

			// GEN REPORT 	
			GenElectronicTitleReport laGrp =
				new GenElectronicTitleReport(
					ReportConstant.RPT_5071_ETITLE_REPORT_TITLE,
					laRptProps,
					laETtlHstryUIData);
			laGrp.formatReport(lvRptData);

			// RETURN REPORT DATA/ATTRIBUTES 
			return new ReportSearchData(
				laGrp.getFormattedReport().toString(),
				ReportConstant.RPT_5071_ETITLE_REPORT_FILENAME,
				ReportConstant.RPT_5071_ETITLE_REPORT_TITLE,
				ReportConstant.RPT_1_COPY,
				ReportConstant.LANDSCAPE);
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * genExemptAuditData
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object genExemptAuditData(Object aaData)
		throws RTSException
	{
		Vector lvExData = (Vector) aaData;

		// defect 8628 
		ExemptAuditUIData laExmptAuditUIData =
			(ExemptAuditUIData) lvExData.get(0);

		AdministrationLogData laAdminLogData =
			(AdministrationLogData) lvExData.get(1);

		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// UOW #1 BEGIN
			// ADMIN LOG 
			laDBAccess.beginTransaction();
			AdministrationLog laAdminLog =
				new AdministrationLog(laDBAccess);
			laAdminLog.insAdministrationLog(laAdminLogData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			// UOW #2 BEGIN 
			// EXEMPT AUDIT 
			ExemptAudit laExemptAuditDataAccess =
				new ExemptAudit(laDBAccess);
			laDBAccess.beginTransaction();

			String lsData =
				laExemptAuditDataAccess.qryExportExemptAudit(
					laExmptAuditUIData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END
			// end defect 8628 
			return lsData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * genExemptAuditReport
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object genExemptAuditReport(Object aaData)
		throws RTSException
	{
		Vector lvExData = (Vector) aaData;

		// defect 8628 
		ExemptAuditUIData laExmptAuditUIData =
			(ExemptAuditUIData) lvExData.get(0);

		AdministrationLogData laAdminLogData =
			(AdministrationLogData) lvExData.get(1);

		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// UOW #1 BEGIN 
			// ADMIN LOG
			laDBAccess.beginTransaction();
			AdministrationLog laAdminLog =
				new AdministrationLog(laDBAccess);
			laAdminLog.insAdministrationLog(laAdminLogData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);

			// UOW #2 BEGIN 
			// REPORT PROPERTIES  
			ReportProperties laReportProperties =
				initReportProperties(
					laAdminLogData.getReportSearchData(),
					laDBAccess,
					ReportConstant.RPT_5051_REPORT_ID);
			// UOW #2 END 

			// EXEMPT AUDIT
			// UOW #3 BEGIN   
			ExemptAudit laExemptAuditRptAccess =
				new ExemptAudit(laDBAccess);
			laDBAccess.beginTransaction();
			Vector lvRptData =
				laExemptAuditRptAccess.qryReportExemptAudit(
					laExmptAuditUIData);

			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END 

			// GEN REPORT  
			GenExemptAuditReport laGrp =
				new GenExemptAuditReport(
					ReportConstant.RPT_5051_REPORT_TITLE,
					laReportProperties,
					laExmptAuditUIData);
			laGrp.formatReport(lvRptData);

			// RETURN REPORT DATA/ATTRIBUTES 
			return new ReportSearchData(
				laGrp.getFormattedReport().toString(),
				ReportConstant.RPT_5051_FILENAME,
				ReportConstant.RPT_5051_REPORT_TITLE,
				ReportConstant.RPT_1_COPY,
				ReportConstant.PORTRAIT);
			// end defect 8628
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}
	/**
	 * genFraudData
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object genFraudData(Object aaData) throws RTSException
	{
		Vector lvExData = (Vector) aaData;

		FraudUIData laFraudUIData = (FraudUIData) lvExData.get(0);

		AdministrationLogData laAdminLogData =
			(AdministrationLogData) lvExData.get(1);

		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// UOW #1 BEGIN
			// ADMIN LOG 
			laDBAccess.beginTransaction();
			AdministrationLog laAdminLog =
				new AdministrationLog(laDBAccess);
			laAdminLog.insAdministrationLog(laAdminLogData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			// UOW #2 BEGIN 
			FraudLog laFraudSQL = new FraudLog(laDBAccess);
			laDBAccess.beginTransaction();
			String lsData = laFraudSQL.qryExportFraudLog(laFraudUIData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END 
			return lsData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}
	/**
	 * genFraudReport
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object genFraudReport(Object aaData) throws RTSException
	{

		Vector lvData = (Vector) aaData;

		FraudUIData laFraudUIData = (FraudUIData) lvData.get(0);

		AdministrationLogData laAdminLogData =
			(AdministrationLogData) lvData.get(1);

		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// UOW #1 BEGIN
			// ADMIN LOG 
			laDBAccess.beginTransaction();
			AdministrationLog laAdminLog =
				new AdministrationLog(laDBAccess);
			laAdminLog.insAdministrationLog(laAdminLogData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			// UOW #2 BEGIN 
			// REPORT PROPERTIES  
			ReportProperties laReportProperties =
				initReportProperties(
					laAdminLogData.getReportSearchData(),
					laDBAccess,
					ReportConstant.FRAUD_REPORT_ID);
			// UOW #2 END 

			// UOW #3 BEGIN 
			FraudLog laFraudSQL = new FraudLog(laDBAccess);
			laDBAccess.beginTransaction();
			Vector lvRptData = laFraudSQL.qryReportFraudLog(laFraudUIData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END

			// GEN REPORT  
			GenFraudReport laGrp =
				new GenFraudReport(
					ReportConstant.FRAUD_REPORT_TITLE,
					laReportProperties,
					laFraudUIData);
			laGrp.formatReport(lvRptData);

			// RETURN REPORT DATA/ATTRIBUTES 
			return new ReportSearchData(
				laGrp.getFormattedReport().toString(),
				ReportConstant.FRAUD_REPORT_FILENAME,
				ReportConstant.FRAUD_REPORT_TITLE,
				ReportConstant.RPT_1_COPY,
				ReportConstant.PORTRAIT);
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**	
	 * genReprintStickerReport
	 * 
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object genReprintStickerReport(Object aaData)
		throws RTSException
	{
		// defect 8628    
		Map laMap = (Map) aaData;

		ReportSearchData laRptSearchData = new ReportSearchData();
		int liOfcNo = ((Integer) laMap.get("OFC")).intValue();
		laRptSearchData.setIntKey1(liOfcNo);
		laRptSearchData.setIntKey2(
			((Integer) laMap.get("SUB")).intValue());
		laRptSearchData.setIntKey3(
			((Integer) laMap.get("WS")).intValue());
		laRptSearchData.setKey1((String) laMap.get("EMP"));

		RTSDate laBeginDate =
			(RTSDate) laMap.get(ReportConstant.BEGIN_DATE);
		RTSDate laEndDate =
			(RTSDate) laMap.get(ReportConstant.END_DATE);

		ReprintData laReprintData = new ReprintData();
		laReprintData.setOfcIssuanceNo(liOfcNo);

		AdministrationLogData laAdminLogData =
			(AdministrationLogData) laMap.get(ReportConstant.ADMIN_LOG);

		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{

			// Add Logging to AdministrationLog  
			// UOW #1 BEGIN 
			// ADMIN LOG
			laDBAccess.beginTransaction();
			AdministrationLog laAdminLog =
				new AdministrationLog(laDBAccess);
			laAdminLog.insAdministrationLog(laAdminLogData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			// UOW #2 BEGIN 
			// REPORT PROPERTIES 
			ReportProperties laReportProperties =
				initReportProperties(
					laRptSearchData,
					laDBAccess,
					"RTS.POS.5251");
			// UOW #2 END 

			// UOW #3 BEGIN 
			// REPRINT STICKER 
			laDBAccess.beginTransaction();
			ReprintSticker laReprintSticker =
				new ReprintSticker(laDBAccess);
			Vector lvRptData =
				laReprintSticker.qryAllReprintSticker(
					laReprintData,
					laBeginDate,
					laEndDate);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END 

			// GEN REPORT 
			GenReprintStickerReport laReport =
				new GenReprintStickerReport(
					"REPRINT STICKER REPORT",
					laReportProperties);
			laReport.formatReport(lvRptData, laMap);

			// RETURN REPORT DATA/ATTRIBUTES 
			return new ReportSearchData(
				laReport.getFormattedReport().toString(),
				ReportConstant.REPRINT_STICKER_REPORT_FILENAME,
				ReportConstant.REPRINT_STICKER_REPORT_TITLE,
				ReportConstant.RPT_7_COPIES,
				ReportConstant.PORTRAIT);
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Sets up the data to generate the Sales Tax Report.
	 * The ReportSearchData (aaData) is populated as follows
	 *     <br>IntKey1() - The office issuance number
	 *     <br>IntKey2() - The substation id
	 *     <br>IntKey3() - The workstation id
	 *     <br>Key1() - The current user
	 * The object returned will be the completed Sales Tax report in
	 * the form of ReportSearchData.
	 *
	 * @param aaData Object 
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object genSalesTaxReport(Object aaData) throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;

		// defect 8628 
		DatabaseAccess laDBAccess = new DatabaseAccess();
		ReportProperties laRptProps = new ReportProperties();

		try
		{
			// UOW #1 BEGIN
			// REPORT PROPERTIES 
			laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBAccess,
					"RTS.POS.5061");
			// UOW #1 END 

			//	Initialized for Insert 
			AdministrationLog laAdminLog =
				new AdministrationLog(laDBAccess);
			AdministrationLogData laLogData =
				new AdministrationLogData(laRptSearchData);
			laLogData.setAction("Report");
			laLogData.setEntity("SalesTaxAlloc");
			laLogData.setEntityValue(
				laRptSearchData.getDate1().toString());

			// UOW #2 BEGIN 
			laDBAccess.beginTransaction();
			laAdminLog.insAdministrationLog(laLogData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END 
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		MfAccess laMfa = new MfAccess();
		FundsPaymentDataList laFundsPaymentDataList = null;
		try
		{
			laRptSearchData.setKey1(CommonConstant.FUNDS_REPORT_DATE);
			laFundsPaymentDataList = new FundsPaymentDataList();

			// defect 10112 
			// 2nd parameter removed, not used  
			laFundsPaymentDataList =
				laMfa.retrieveFundsPaymentDataList(laRptSearchData);
			// end defect 10112
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getMsgType().equals(RTSException.MF_DOWN))
			{
				throw new RTSException(
					ErrorsConstant.ERR_NUM_RECORD_RETRIEVAL_DOWN);
			}
		}

		// GEN REPORT 
		GenSalesTaxAllocationReport laGpr =
			new GenSalesTaxAllocationReport(
				ReportConstant.SALES_TAX_ALLOCATION_REPORT_TITLE,
				laRptProps);
		laGpr.formatReport(laFundsPaymentDataList.getFundsDue());

		// defect 8628 
		// RETURN REPORT DATA/ATTRIBUTES 
		return new ReportSearchData(
			laGpr.getFormattedReport().toString(),
			ReportConstant.SALES_TAX_ALLOCATION_REPORT_FILENAME,
			ReportConstant.SALES_TAX_ALLOCATION_REPORT_TITLE,
			ReportConstant.RPT_1_COPY,
			ReportConstant.PORTRAIT);
		// end defect 8628
	}

	/**
	 * Sets up the data to generate the Title Package Report.
	 * The ReportSearchData (aaData) is populated as follows
	 *     <br>IntKey1() - The office issuance number
	 *     <br>IntKey2() - The substation id
	 *     <br>IntKey3() - The workstation id
	 *     <br>Key1() - The current user
	 * The object returned will be the completed Title Package report in
	 * the form of ReportSearchData.
	 * 
	 * @param aaData Object 
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object genTitlePackageReport(Object aaData)
		throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;

		// defect 8628, 10142 
		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// UOW #1 BEGIN
			// REPORT PROPERTIES 
			ReportProperties laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBAccess,
					"RTS.POS.5911");
			// UOW #1 END  

			// TITLE PACKAGE
			// UOW #2 BEGIN  
			TitlePackageReportSQL laReportsSQL =
				new TitlePackageReportSQL(laDBAccess);
			laDBAccess.beginTransaction();

			Vector lvRptData =
				laReportsSQL.qryTitlePackageReport(laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END

			// GEN REPORT
			GenTitlePackageReport laGpr =
				new GenTitlePackageReport(
					ReportConstant.TITLE_PACKAGE_REPORT_TITLE,
					laRptProps);

			Vector lvReportInput = new Vector();
			lvReportInput.addElement(lvRptData);
			// WsIds  
			lvReportInput.addElement(laRptSearchData.getVector());
			// Report Date (AMDate to String)   
			lvReportInput.addElement(laRptSearchData.getKey2());

			laGpr.formatReport(lvReportInput);

			// RETURN REPORT DATA/ATTRIBUTES 
			return new ReportSearchData(
				laGpr.getFormattedReport().toString(),
				ReportConstant.TITLE_PACKAGE_REPORT_ONLINE_FILENAME,
				ReportConstant.TITLE_PACKAGE_REPORT_TITLE,
				ReportConstant.RPT_1_COPY,
				ReportConstant.PORTRAIT);
			// end defect 8628, 10142
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/** 
	 * Init ReportProperties
	 * 
	 * @param aaDBAccess
	 * @param aaAdminLogData
	 * @return ReportProperties
	 * @throws RTSException
	 */
	public ReportProperties initReportProperties(
		ReportSearchData laRptSearchData,
		DatabaseAccess aaDBAccess,
		String asReportId)
		throws RTSException
	{

		int liOfcIssuanceNo = laRptSearchData.getIntKey1();
		int liSubstaId = laRptSearchData.getIntKey2();
		int liWorkstationId = laRptSearchData.getIntKey3();
		String lsRequestedBy = laRptSearchData.getKey1();

		// UOW START
		aaDBAccess.beginTransaction();

		// OFCNAME 
		OfficeIds laOfficeName = new OfficeIds(aaDBAccess);
		String lsOfcName = laOfficeName.qryOfficeId(liOfcIssuanceNo);

		// SUBSTANAME
		Substation laSubsta = new Substation(aaDBAccess);
		String lsSubstaName =
			laSubsta.qrySubstationId(liOfcIssuanceNo, liSubstaId);

		aaDBAccess.endTransaction(DatabaseAccess.COMMIT);
		// UOW END 

		ReportProperties laRptProperties =
			new ReportProperties(asReportId);
		laRptProperties.setOfficeIssuanceName(lsOfcName);
		laRptProperties.setSubstationName(lsSubstaName);
		laRptProperties.setWorkstationId(liWorkstationId);
		laRptProperties.setRequestedBy(lsRequestedBy);
		laRptProperties.setSubstationId(liSubstaId);
		laRptProperties.setOfficeIssuanceId(liOfcIssuanceNo);
		return laRptProperties;
	}

	/**
	 * Used to call the private methods of the reports server business.
	 *
	 * @param aiModule int 
	 * @param aiFunctionId int
	 * @param aaData Object
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
			case ReportConstant.GENERATE_SALES_TAX_REPORT :
				{
					return genSalesTaxReport(aaData);
				}
			case ReportConstant.GENERATE_TITLE_PACKAGE_REPORT :
				{
					return genTitlePackageReport(aaData);
				}
			case ReportConstant.REPRINT_STICKER_REPORT :
				{
					return genReprintStickerReport(aaData);
				}
			case ReportConstant.GENERATE_EXEMPT_AUDIT_EXPORT :
				{
					return genExemptAuditData(aaData);
				}
			case ReportConstant.GENERATE_EXEMPT_AUDIT_REPORT :
				{
					return genExemptAuditReport(aaData);
				}
				// defect 10900
			case ReportConstant.GENERATE_FRAUD_EXPORT :
				{
					return genFraudData(aaData);
				}
			case ReportConstant.GENERATE_FRAUD_REPORT :
				{
					return genFraudReport(aaData);
				}
				// end defect 10900
				// defect 9972
			case ReportConstant.GENERATE_ETITLE_REPORT :
				{
					return genElectronicTitleReport(aaData);
				}
			case ReportConstant.GENERATE_ETITLE_EXPORT :
				{
					return genElectronicTitleData(aaData);
				}
				// end defect 9972  
		}
		return null;
	}
}
