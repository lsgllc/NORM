package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ExemptAuditData;
import com.txdot.isd.rts.services.data.ExemptAuditUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * ExemptAudit.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/28/2006	Created for Exempt Project
 * 							defect 8900 Exempts
 * K Harrell	10/02/2006	Enhance SQL to address null PriorRegClassCd,
 * 							PriorRegPltCd	
 * 							add updExemptAudit()
 * 							modify qryExportExemptAudit(),
 * 							  qryReportExemptAudit() 
 * 							defect 8900 Exempts
 * J Ralph		10/11/2006	Modified String lsOffices creation
 * 							modify getOffices()
 * 							defect 8900 Ver Exempts
 * K Harrell	10/16/2006	Added missing population of OfcIssuanceNo
 * 							modify qryReportExemptAudit()
 * 							defect 8900 Ver Exempts 
 * J Ralph		10/18/2006	Merge prolong adjustment
 * 							defect 8900 Ver Exempts
 * K Harrell	10/24/2006	Add new join component for SBRNW 
 * 							modify qryReportExemptAudit()  
 * 							defect 8900 Ver Exempts
 * K Harrell	11/15/2006	Add new column, MfDwnCd, to all queries and
 * 							insert. Add VoidedTransIndi to Report Query.
 * 							Do not exclude voided data from Report.  
 * 							modify COLUMN_NAME_ARRAY,
 * 							  insExemptAudit(),
 * 							  qryExemptAudit(),qryExportExemptAudit(),
 * 							  qryReportExemptAudit()
 * 							defect 9017 Ver Exempts
 * K Harrell	11/30/2006	Correct "order by" in Report Query to 
 * 							order by TransId  (OfcIssuanceno,
 * 							TransWsId, TransAMDate, TransTime) 
 * 							modify qryReportExemptAudit()
 * 							defect 8900 Ver Exempts
 * K Harrell	10/28/2007	Use Coalesce/Left Outer Join for Report & 
 * 							 Export
 * 							modify qryExportExemptAudit(), 
 * 							 qryReportExemptAudit() 
 * 							defect 9391 Ver Special Plates 2
 * K Harrell	01/05/2009	Assign TransEmpId to "LGNERR" if null || 
 * 							TransEmpId.trim().length() == 0 on insert. 
 * 							modify insExemptAudit()    
 *	 						defect 9847 Ver Defect_POS_D
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeExemptAudit()
 * 							defect 9825 Ver Defect_POS_D 
 * K Harrell	03/18/2010	Removed unused method. 
 * 							delete qryExemptAudit() 
 * 							defect 10239  Ver POS_640
 * K Harrell 	06/14/2010 	add csTransId to SQL 
 * 							modify insExemptAudit(), qryReportExemptAudit()
 *        					defect 10505 Ver 6.5.0 
 * K Harrell	10/14/2010	TransId not included in SQL 
 * 							modify qryReportExemptAudit()
 * 							defect 10628 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_EXMPT_AUDIT
 *
 * @version	6.5.0 			10/14/2010	
 * @author	Kathy Harrell
 * <br>Creation Date:		09/28/2006 15:44:00
 */
public class ExemptAudit extends ExemptAuditData
{
	DatabaseAccess caDA;

	// defect 9017
	// add MfDwnCd to column hdrs
	private final static String[] COLUMN_NAME_ARRAY =
		{
			"TransDate",
			"TransId",
			"OfcIssuanceNo",
			"SubstaId",
			"TransAMDate",
			"TransWsId",
			"CustSeqNo",
			"TransTime",
			"TransCd",
			"TransEmpId",
			"VoidedTransIndi",
			"OwnrTtlName1",
			"OwnrTtlName2",
			"LienHldr1Name1",
			"ExmptIndi",
			"PriorExmptIndi",
			"DocNo",
			"VIN",
			"VehClassCd",
			"VehMk",
			"VehModlYr",
			"RegClassCd",
			"RegClassCdDesc",
			"PriorRegClassCd",
			"PriorRegClassCdDesc",
			"RegPltCd",
			"RegPltCdDesc",
			"PriorRegPltCd",
			"PriorRegPltCdDesc",
			"RegPltNo",
			"PriorRegPltNo",
			"RegExpMo",
			"RegExpYr",
			"TtlFeeIndi",
			"MfDwnCd" };
	// end defect 9017 

	/**
	 * ExemptAudit constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public ExemptAudit(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to return String Buffer 
	 *    - preface with ","
	 *    - add quotes around String
	 *
	 * @throws RTSException 
	 */
	public String getStringToAppend(String asData)
	{
		String lsData =
			CommonConstant.STR_COMMA
				+ CommonConstant.STR_DOUBLE_QUOTE
				+ asData
				+ CommonConstant.STR_DOUBLE_QUOTE;
		return lsData;
	}

	/**
	 * Method to update RTS.RTS_EXMPT_AUDIT with VoidedTransIndi
	 * 
	 * @param  aaExemptAuditData ExemptAuditData	
	 * @throws RTSException 	
	 */
	public void voidExemptAudit(ExemptAuditData aaExemptAuditData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "voidExemptAudit - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_EXMPT_AUDIT SET "
				+ "VoidedTransIndi = 1 "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "TransTime  = ? AND "
				+ "TransCompleteIndi = 1 ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransTime())));

			Log.write(Log.SQL, this, "voidExemptAudit - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "voidExemptAudit - SQL - End");
			Log.write(Log.METHOD, this, "voidExemptAudit - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"voidExemptAudit - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF VOID METHOD

	/**
	 * Method to DELETE from RTS.RTS_EXMPT_AUDIT 
	 * 
	 * @param  aaExemptAuditData ExemptAuditData	
	 * @throws RTSException 	
	 */
	public void delExemptAudit(ExemptAuditData aaExemptAuditData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delExemptAudit - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"DELETE from RTS.RTS_EXMPT_AUDIT WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? AND "
				+ "TransCompleteIndi = 0 ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getCustSeqNo())));

			if (aaExemptAuditData.getTransTime() != 0)
			{
				lsUpd = lsUpd + " AND TransTime = ? ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaExemptAuditData.getTransTime())));

			}

			Log.write(Log.SQL, this, "delExemptAudit - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "delExemptAudit - SQL - End");
			Log.write(Log.METHOD, this, "delExemptAudit - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delExemptAudit - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF DELETE METHOD

	/**
	 * Method to build initial delimited string of column headers
	 *
	 * @throws RTSException 
	 */
	public StringBuffer getColumnHdrs()
	{
		StringBuffer lsDelimitedHdrs = new StringBuffer();
		lsDelimitedHdrs.append(
			CommonConstant.STR_DOUBLE_QUOTE
				+ "TransDate"
				+ CommonConstant.STR_DOUBLE_QUOTE);

		for (int i = 1; i < COLUMN_NAME_ARRAY.length; i++)
		{
			lsDelimitedHdrs.append(
				getStringToAppend(COLUMN_NAME_ARRAY[i]));
		}
		return lsDelimitedHdrs;
	}

	/**
	 * Method to build list of offices for "IN" clause
	 *
	 * @return String
	 * @throws RTSException 
	 */
	public String getOffices(Vector avOffices)
	{
		// defect 8900
		// Remove casting Vector as Integer
		// String lsOffices =
		//		"( " + ((Integer) avOffices.get(0)).intValue();
		String lsOffices = "(" + avOffices.get(0);
		// end defect 8900
		for (int i = 1; i < avOffices.size(); i++)
		{
			lsOffices =
				lsOffices + CommonConstant.STR_COMMA + avOffices.get(i);

		}
		return lsOffices + ") ";
	}

	/**
	 * Method to retrieve data from RTS.RTS_EXMPT_AUDIT for 
	 * Exempt Audit Report.  Returns a Vector of ExemptAuditData 
	 * Objects. 
	 *
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryReportExemptAudit(ExemptAuditUIData aaExmptAuditUIData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryReportExemptAudit - Begin");
		RTSDate laBeginDate = aaExmptAuditUIData.getBeginDate();
		RTSDate laEndDate = aaExmptAuditUIData.getEndDate();
		String lsOffices =
			getOffices(aaExmptAuditUIData.getSelectedOffices());

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;
		// defect 9391 
		// Use DB2 Coalesce and Left Outer Join vs. Unions
		lsQry.append(
			"SELECT "
				+ "OfcissuanceNo,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "TransTime,"
				+ "ExmptIndi,"
				+ "PriorExmptIndi,"
				+ "COALESCE(D.RegClassCdDesc,' ') as RegClassCdDesc,"
				+ "COALESCE (E.RegClassCdDesc,' ') as PriorRegClassCdDesc,"
				+ "TransCd,"
				+ "TransEmpId,"
				+ "COALESCE(B.ItmCdDesc,' ') as RegPltCdDesc,"
				+ "COALESCE(C.ItmCdDesc,' ') as PriorRegPltCdDesc,"
				+ "RegPltNo,"
				+ "PriorRegPltNo,"
				+ "OwnrTtlName1,"
				+ "OwnrTtlName2, "
				+ "VoidedTransIndi,"
				+ "MfDwnCd "
				+ "from RTS.RTS_EXMPT_AUDIT A "
				+ "LEFT OUTER JOIN RTS.RTS_ITEM_CODES B ON A.REGPLTCD = B.ITMCD "
				+ "LEFT OUTER JOIN RTS.RTS_ITEM_CODES C ON A.PRIORREGPLTCD = C.ITMCD "
				+ "LEFT OUTER JOIN RTS.RTS_COMMON_FEES D ON A.REGCLASSCD = D.REGCLASSCD AND D.RTSEFFENDDATE = 99991231 "
				+ "LEFT OUTER JOIN RTS.RTS_COMMON_FEES E ON A.PRIORREGCLASSCD = E.REGCLASSCD AND E.RTSEFFENDDATE = 99991231 "
				+ "WHERE "
				+ " TransCompleteIndi = 1 AND "
				+ "TransAMDate between "
				+ laBeginDate.getAMDate()
				+ " and "
				+ laEndDate.getAMDate()
				+ " and OfcIssuanceNo in "
				+ lsOffices
				+ " Order by 1,3,2,4");

		// Where populated as follows:
		//	 - RegClassCd  		(yes)
		//	 - RegPltCd   	 	(yes)
		//   - PriorRegClassCd	(yes)
		//   - PriorRegPltCd 	(yes)

		// defect 9017 
		// - add MfDwnCd, VoidedTransIndi to select clauses
		// - do not qualify on VoidedTransIndi  
		//		lsQry.append(
		//			"SELECT "
		//				+ "OfcissuanceNo,"
		//				+ "TransAMDate,"
		//				+ "TransWsId,"
		//				+ "TransTime,"
		//				+ "ExmptIndi,"
		//				+ "PriorExmptIndi,"
		//				+ "D.RegClassCdDesc as RegClassCdDesc,"
		//				+ "E.RegClassCdDesc as PriorRegClassCdDesc,"
		//				+ "TransCd,"
		//				+ "TransEmpId,"
		//				+ "B.ItmCdDesc as RegPltCdDesc,"
		//				+ "C.ItmCdDesc as PriorRegPltCdDesc,"
		//				+ "RegPltNo,"
		//				+ "PriorRegPltNo,"
		//				+ "OwnrTtlName1,"
		//				+ "OwnrTtlName2, "
		//				+ "VoidedTransIndi,"
		//				+ "MfDwnCd "
		//				+ "from RTS.RTS_EXMPT_AUDIT A, "
		//				+ "RTS.RTS_ITEM_CODES B, "
		//				+ "RTS.RTS_ITEM_CODES C, "
		//				+ "RTS.RTS_COMMON_FEES D, "
		//				+ "RTS.RTS_COMMON_FEES E "
		//				+ "WHERE "
		//				+ " TransCompleteIndi = 1 AND "
		//				+ " A.REGPLTCD = B.ITMCD AND "
		//				+ " A.PRIORREGPLTCD = C.ITMCD AND "
		//				+ " A.REGCLASSCD = D.REGCLASSCD AND "
		//				+ " D.RTSEFFDATE = "
		//				+ " (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES F "
		//				+ " WHERE F.REGCLASSCD = A.REGCLASSCD ) AND "
		//				+ " A.PRIORREGCLASSCD = E.REGCLASSCD AND "
		//				+ " E.RTSEFFDATE = "
		//				+ " (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES G "
		//				+ " WHERE G.REGCLASSCD = A.PRIORREGCLASSCD) AND "
		//				+ "TransAMDate between "
		//				+ laBeginDate.getAMDate()
		//				+ " and "
		//				+ laEndDate.getAMDate()
		//				+ " and OfcIssuanceNo in "
		//				+ lsOffices);
		//		// DRVED, New Title 
		//		//	Where populated as follows:
		//		//	 - RegClassCd  		(yes)
		//		//	 - RegPltCd   	 	(yes)
		//		//  - PriorRegClassCd	 (no)
		//		//  - PriorRegPltCd 	 (no)
		//		lsQry.append(
		//			"UNION ALL "
		//				+ "SELECT "
		//				+ "OfcissuanceNo,"
		//				+ "TransAMDate,"
		//				+ "TransWsId,"
		//				+ "TransTime,"
		//				+ "ExmptIndi,"
		//				+ "PriorExmptIndi,"
		//				+ "D.RegClassCdDesc as RegClassCdDesc,"
		//				+ "' ' as PriorRegClassCdDesc,"
		//				+ "TransCd,"
		//				+ "TransEmpId,"
		//				+ "B.ItmCdDesc as RegPltCdDesc,"
		//				+ "' ' as PriorRegPltCdDesc,"
		//				+ "RegPltNo,"
		//				+ "PriorRegPltNo,"
		//				+ "OwnrTtlName1,"
		//				+ "OwnrTtlName2, "
		//				+ "VoidedTransIndi,"
		//				+ "MfDwnCd "
		//				+ "from RTS.RTS_EXMPT_AUDIT A, "
		//				+ "RTS.RTS_ITEM_CODES B, "
		//				+ "RTS.RTS_COMMON_FEES D "
		//				+ "WHERE "
		//				+ " TransCompleteIndi = 1 AND "
		//				+ " A.REGPLTCD = B.ITMCD AND "
		//				+ " A.REGCLASSCD = D.REGCLASSCD AND "
		//				+ " D.RTSEFFDATE = (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES F "
		//				+ " WHERE F.REGCLASSCD = A.REGCLASSCD ) AND "
		//				+ " A.PRIORREGCLASSCD = 0 AND "
		//				+ " A.PRIORREGPLTCD is NULL AND "
		//				+ " TransAMDate between "
		//				+ laBeginDate.getAMDate()
		//				+ " and "
		//				+ laEndDate.getAMDate()
		//				+ " and OfcIssuanceNo in "
		//				+ lsOffices);
		//		// SBRNW 
		//		// Where populated as follows:
		//		//	 - RegClassCd  		(yes)
		//		//	 - RegPltCd   	 	(no)
		//		//   - PriorRegClassCd	(no)
		//		//   - PriorRegPltCd 	(no)
		//		lsQry.append(
		//			"UNION ALL "
		//				+ "SELECT "
		//				+ "OfcissuanceNo,"
		//				+ "TransAMDate,"
		//				+ "TransWsId,"
		//				+ "TransTime,"
		//				+ "ExmptIndi,"
		//				+ "PriorExmptIndi,"
		//				+ "D.RegClassCdDesc as RegClassCdDesc,"
		//				+ "' ' as PriorRegClassCdDesc,"
		//				+ "TransCd,"
		//				+ "TransEmpId,"
		//				+ "' '  as RegPltCdDesc,"
		//				+ "' ' as PriorRegPltCdDesc,"
		//				+ "RegPltNo,"
		//				+ "PriorRegPltNo,"
		//				+ "OwnrTtlName1,"
		//				+ "OwnrTtlName2, "
		//				+ "VoidedTransIndi,"
		//				+ "MfDwnCd "
		//				+ "from RTS.RTS_EXMPT_AUDIT A, "
		//				+ "RTS.RTS_COMMON_FEES D "
		//				+ "WHERE "
		//				+ " TransCompleteIndi = 1 AND "
		//				+ " A.REGPLTCD is null  AND "
		//				+ " A.REGCLASSCD = D.REGCLASSCD AND "
		//				+ " D.RTSEFFDATE = (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES F "
		//				+ " WHERE F.REGCLASSCD = A.REGCLASSCD ) AND "
		//				+ " A.PRIORREGCLASSCD = 0 AND "
		//				+ " A.PRIORREGPLTCD is NULL AND "
		//				+ " TransAMDate between "
		//				+ laBeginDate.getAMDate()
		//				+ " and "
		//				+ laEndDate.getAMDate()
		//				+ " and OfcIssuanceNo in "
		//				+ lsOffices);
		//		//Where populated as follows:
		//		//	 - RegClassCd  		(yes)
		//		//	 - RegPltCd   	 	(yes)
		//		//   - PriorRegClassCd	 (no)
		//		//   - PriorRegPltCd 	(yes)
		//		lsQry.append(
		//			" UNION ALL "
		//				+ "SELECT "
		//				+ "OfcissuanceNo,"
		//				+ "TransAMDate,"
		//				+ "TransWsId,"
		//				+ "TransTime,"
		//				+ "ExmptIndi,"
		//				+ "PriorExmptIndi,"
		//				+ "D.RegClassCdDesc as RegClassCdDesc,"
		//				+ "'  ' as PriorRegClassCdDesc,"
		//				+ "TransCd,"
		//				+ "TransEmpId,"
		//				+ "B.ItmCdDesc as RegPltCdDesc,"
		//				+ "C.ItmCdDesc as PriorRegPltCdDesc,"
		//				+ "RegPltNo,"
		//				+ "PriorRegPltNo,"
		//				+ "OwnrTtlName1,"
		//				+ "OwnrTtlName2, "
		//				+ "VoidedTransIndi,"
		//				+ "MfDwnCd "
		//				+ "from RTS.RTS_EXMPT_AUDIT A, "
		//				+ "RTS.RTS_ITEM_CODES B, "
		//				+ "RTS.RTS_ITEM_CODES C, "
		//				+ "RTS.RTS_COMMON_FEES D "
		//				+ "WHERE "
		//				+ " TransCompleteIndi = 1 AND "
		//				+ " A.REGPLTCD = B.ITMCD AND "
		//				+ " A.PRIORREGPLTCD = C.ITMCD AND "
		//				+ " A.REGCLASSCD = D.REGCLASSCD AND "
		//				+ " D.RTSEFFDATE = (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES F "
		//				+ " WHERE F.REGCLASSCD = A.REGCLASSCD ) AND "
		//				+ " A.PRIORREGCLASSCD = 0 AND "
		//				+ "TransAMDate between "
		//				+ laBeginDate.getAMDate()
		//				+ " and "
		//				+ laEndDate.getAMDate()
		//				+ " and OfcIssuanceNo in "
		//				+ lsOffices);
		//		//	Where populated as follows:
		//		//	 - RegClassCd  		(yes)
		//		//	 - RegPltCd   	 	(yes)
		//		//   - PriorRegClassCd	(yes)
		//		//   - PriorRegPltCd 	 (no)
		//		lsQry.append(
		//			" UNION ALL "
		//				+ "SELECT "
		//				+ "OfcissuanceNo,"
		//				+ "TransAMDate,"
		//				+ "TransWsId,"
		//				+ "TransTime,"
		//				+ "ExmptIndi,"
		//				+ "PriorExmptIndi,"
		//				+ "D.RegClassCdDesc as RegClassCdDesc,"
		//				+ "E.RegClassCdDesc as PriorRegClassCdDesc,"
		//				+ "TransCd,"
		//				+ "TransEmpId,"
		//				+ "B.ItmCdDesc as RegPltCdDesc,"
		//				+ "'  ' as PriorRegPltCdDesc,"
		//				+ "RegPltNo,"
		//				+ "PriorRegPltNo,"
		//				+ "OwnrTtlName1,"
		//				+ "OwnrTtlName2, "
		//				+ "VoidedTransIndi,"
		//				+ "MfDwnCd "
		//				+ "from RTS.RTS_EXMPT_AUDIT A, "
		//				+ "RTS.RTS_ITEM_CODES B, "
		//				+ "RTS.RTS_COMMON_FEES D, "
		//				+ "RTS.RTS_COMMON_FEES E "
		//				+ "WHERE "
		//				+ " TransCompleteIndi = 1 AND "
		//				+ " A.REGPLTCD = B.ITMCD AND "
		//				+ " A.PRIORREGPLTCD IS NULL AND "
		//				+ " A.REGCLASSCD = D.REGCLASSCD AND "
		//				+ " D.RTSEFFDATE = (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES F "
		//				+ " WHERE F.REGCLASSCD = A.REGCLASSCD ) AND "
		//				+ " A.PRIORREGCLASSCD = E.REGCLASSCD AND "
		//				+ " E.RTSEFFDATE = (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES G "
		//				+ " WHERE G.REGCLASSCD = A.PRIORREGCLASSCD) AND "
		//				+ "TransAMDate between "
		//				+ laBeginDate.getAMDate()
		//				+ " and "
		//				+ laEndDate.getAMDate()
		//				+ " and OfcIssuanceNo in "
		//				+ lsOffices
		//				+ " Order by 1,3,2,4");
		//		// end defect 9017
		// end defect 9391 

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryReportExemptAudit - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryReportExemptAudit - SQL - End");

			while (lrsQry.next())
			{
				ExemptAuditData laExemptAuditData =
					new ExemptAuditData();

				// defect 10628 
				int liOfc = caDA.getIntFromDB(lrsQry, "OfcIssuanceNo");
				int liTransWsId =
					caDA.getIntFromDB(lrsQry, "TransWsId");
				int liTransAMDate =
					caDA.getIntFromDB(lrsQry, "TransAMDate");
				int liTransTime =
					caDA.getIntFromDB(lrsQry, "TransTime");

				laExemptAuditData.setTransId(
					UtilityMethods.getTransId(
						liOfc,
						liTransWsId,
						liTransAMDate,
						liTransTime));

				laExemptAuditData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));

				//	laExemptAuditData.setTransId(
				//		caDA.getStringFromDB(lrsQry, "TransId"));
				// end defect 10628  

				laExemptAuditData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));

				laExemptAuditData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));

				laExemptAuditData.setRegClassCdDesc(
					caDA.getStringFromDB(lrsQry, "RegClassCdDesc"));

				laExemptAuditData.setPriorRegClassCdDesc(
					caDA.getStringFromDB(
						lrsQry,
						"PriorRegClassCdDesc"));

				laExemptAuditData.setRegPltCdDesc(
					caDA.getStringFromDB(lrsQry, "RegPltCdDesc"));

				laExemptAuditData.setPriorRegPltCdDesc(
					caDA.getStringFromDB(lrsQry, "PriorRegPltCdDesc"));

				laExemptAuditData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));

				laExemptAuditData.setPriorRegPltNo(
					caDA.getStringFromDB(lrsQry, "PriorRegPltNo"));

				laExemptAuditData.setOwnrTtlName1(
					caDA.getStringFromDB(lrsQry, "OwnrTtlName1"));

				laExemptAuditData.setOwnrTtlName2(
					caDA.getStringFromDB(lrsQry, "OwnrTtlName2"));

				laExemptAuditData.setExmptIndi(
					caDA.getIntFromDB(lrsQry, "ExmptIndi"));

				laExemptAuditData.setPriorExmptIndi(
					caDA.getIntFromDB(lrsQry, "PriorExmptIndi"));

				// defect 9017 
				// Select VoidedTransIndi, MfDwnCd for Report 	
				laExemptAuditData.setVoidedTransIndi(
					caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));

				laExemptAuditData.setMfDwnCd(
					caDA.getIntFromDB(lrsQry, "MfDwnCd"));
				// end defect 9017 

				// Add element to the Vector
				lvRslt.addElement(laExemptAuditData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryReportExemptAudit - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryReportExemptAudit - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	// defect 10239 
	//	/**
	//	 * Method to query from RTS.RTS_EXMPT_AUDIT
	//	 *
	//	 * @return Vector
	//	 * @throws RTSException 
	//	 */
	//	public Vector qryExemptAudit(ExemptAuditUIData aaExmptAuditUIData)
	//		throws RTSException
	//	{
	//		Log.write(Log.METHOD, this, " - qryExemptAudit - Begin");
	//		RTSDate laBeginDate = aaExmptAuditUIData.getBeginDate();
	//		RTSDate laEndDate = aaExmptAuditUIData.getEndDate();
	//		String lsOffices =
	//			getOffices(aaExmptAuditUIData.getSelectedOffices());
	//
	//		StringBuffer lsQry = new StringBuffer();
	//
	//		Vector lvRslt = new Vector();
	//
	//		ResultSet lrsQry;
	//
	//		lsQry.append(
	//			"SELECT "
	//				+ "OfcissuanceNo,"
	//				+ "SubstaId,"
	//				+ "TransAMDate,"
	//				+ "TransWsId,"
	//				+ "CustSeqNo,"
	//				+ "TransTime,"
	//				+ "ExmptIndi,"
	//				+ "PriorExmptIndi,"
	//				+ "RegClassCd,"
	//				+ "PriorRegClassCd,"
	//				+ "RegExpMo,"
	//				+ "RegExpYr,"
	//				+ "TtlFeeIndi,"
	//				+ "VehModlYr,"
	//				+ "VoidedTransIndi,"
	//				+ "TransCd,"
	//				+ "TransEmpId,"
	//				+ "RegPltCd,"
	//				+ "PriorRegPltCd,"
	//				+ "RegPltNo,"
	//				+ "PriorRegPltNo,"
	//				+ "DocNo,"
	//				+ "VehClassCd,"
	//				+ "VehMk,"
	//				+ "VIN,"
	//				+ "OwnrTtlName1,"
	//				+ "OwnrTtlName2,"
	//				+ "LienHldr1Name1, "
	//				+ "MfDwnCd "
	//				+ "from RTS.RTS_EXMPT_AUDIT  "
	//				+ "WHERE "
	//				+ " TransCompleteIndi = 1 AND "
	//				+ " TransAMDate between "
	//				+ laBeginDate.getAMDate()
	//				+ " and "
	//				+ laEndDate.getAMDate()
	//				+ " and OfcIssuanceNo in "
	//				+ lsOffices);
	//
	//		try
	//		{
	//			Log.write(Log.SQL, this, " - qryExemptAudit - SQL - Begin");
	//			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
	//			Log.write(Log.SQL, this, " - qryExemptAudit - SQL - End");
	//
	//			while (lrsQry.next())
	//			{
	//				ExemptAuditData laExemptAuditData =
	//					new ExemptAuditData();
	//				laExemptAuditData.setOfcIssuanceNo(
	//					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
	//				laExemptAuditData.setSubstaId(
	//					caDA.getIntFromDB(lrsQry, "SubstaId"));
	//				laExemptAuditData.setTransAMDate(
	//					caDA.getIntFromDB(lrsQry, "TransAMDate"));
	//				laExemptAuditData.setTransWsId(
	//					caDA.getIntFromDB(lrsQry, "TransWsId"));
	//				laExemptAuditData.setCustSeqNo(
	//					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
	//				laExemptAuditData.setTransTime(
	//					caDA.getIntFromDB(lrsQry, "TransTime"));
	//				laExemptAuditData.setTransEmpId(
	//					caDA.getStringFromDB(lrsQry, "TransEmpId"));
	//				laExemptAuditData.setTransCd(
	//					caDA.getStringFromDB(lrsQry, "TransCd"));
	//				laExemptAuditData.setVoidedTransIndi(
	//					caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));
	//				laExemptAuditData.setRegClassCd(
	//					caDA.getIntFromDB(lrsQry, "RegClassCd"));
	//				laExemptAuditData.setRegExpMo(
	//					caDA.getIntFromDB(lrsQry, "RegExpMo"));
	//				laExemptAuditData.setRegExpYr(
	//					caDA.getIntFromDB(lrsQry, "RegExpYr"));
	//				laExemptAuditData.setRegPltCd(
	//					caDA.getStringFromDB(lrsQry, "RegPltCd"));
	//				laExemptAuditData.setPriorRegPltCd(
	//					caDA.getStringFromDB(lrsQry, "PriorRegPltCd"));
	//				laExemptAuditData.setRegPltNo(
	//					caDA.getStringFromDB(lrsQry, "RegPltNo"));
	//				laExemptAuditData.setPriorRegPltNo(
	//					caDA.getStringFromDB(lrsQry, "PriorRegPltNo"));
	//				laExemptAuditData.setDocNo(
	//					caDA.getStringFromDB(lrsQry, "DocNo"));
	//				laExemptAuditData.setVehClassCd(
	//					caDA.getStringFromDB(lrsQry, "VehClassCd"));
	//				laExemptAuditData.setVehMk(
	//					caDA.getStringFromDB(lrsQry, "VehMk"));
	//				laExemptAuditData.setVehModlYr(
	//					caDA.getIntFromDB(lrsQry, "VehModlYr"));
	//				laExemptAuditData.setVIN(
	//					caDA.getStringFromDB(lrsQry, "VIN"));
	//				laExemptAuditData.setOwnrTtlName1(
	//					caDA.getStringFromDB(lrsQry, "OwnrTtlName1"));
	//				laExemptAuditData.setOwnrTtlName2(
	//					caDA.getStringFromDB(lrsQry, "OwnrTtlName2"));
	//				laExemptAuditData.setLienHldr1Name1(
	//					caDA.getStringFromDB(lrsQry, "LienHldr1Name1"));
	//				laExemptAuditData.setExmptIndi(
	//					caDA.getIntFromDB(lrsQry, "ExmptIndi"));
	//				laExemptAuditData.setPriorExmptIndi(
	//					caDA.getIntFromDB(lrsQry, "PriorExmptIndi"));
	//				laExemptAuditData.setPriorRegClassCd(
	//					caDA.getIntFromDB(lrsQry, "PriorRegClassCd"));
	//				laExemptAuditData.setTtlFeeIndi(
	//					caDA.getIntFromDB(lrsQry, "TtlFeeIndi"));
	//				// defect 9017 
	//				laExemptAuditData.setMfDwnCd(
	//					caDA.getIntFromDB(lrsQry, "MfDwnCd"));
	//				// end defect 9017 
	//				// Add element to the Vector
	//				lvRslt.addElement(laExemptAuditData);
	//			} //End of While
	//
	//			lrsQry.close();
	//			caDA.closeLastDBStatement();
	//			lrsQry = null;
	//			Log.write(Log.METHOD, this, " - qryExemptAudit - End ");
	//			return (lvRslt);
	//		}
	//		catch (SQLException aeSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				" - qryExemptAudit - SQL Exception "
	//					+ aeSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	//		}
	//	} //END OF QUERY METHOD
	// end defect 10239 

	/**
	* Method to Export from RTS.RTS_EXMPT_AUDIT
	*
	* @return Vector
	* @throws RTSException 
	*/
	public String qryExportExemptAudit(ExemptAuditUIData aaExmptAuditUIData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryExportExemptAudit - Begin");
		RTSDate laBeginDate = aaExmptAuditUIData.getBeginDate();
		RTSDate laEndDate = aaExmptAuditUIData.getEndDate();
		String lsOffices =
			getOffices(aaExmptAuditUIData.getSelectedOffices());

		StringBuffer lsQry = new StringBuffer();
		StringBuffer lsExportData = new StringBuffer();

		ResultSet lrsQry;
		//	defect 9391 
		// Use DB2 Coalesce and Left Outer Join vs. Unions
		lsQry.append(
			"SELECT "
				+ "OfcissuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "ExmptIndi,"
				+ "PriorExmptIndi,"
				+ "A.RegClassCd,"
				+ "COALESCE(D.RegClassCdDesc,' ') as RegClassCdDesc,"
				+ "PriorRegClassCd,"
				+ "COALESCE(E.RegClassCdDesc,' ') as PriorRegClassCdDesc,"
				+ "RegExpMo,"
				+ "RegExpYr,"
				+ "TtlFeeIndi,"
				+ "VehModlYr,"
				+ "VoidedTransIndi,"
				+ "TransCd,"
				+ "TransEmpId,"
				+ "RegPltCd,"
				+ "COALESCE(B.ItmCdDesc,' ') as RegPltCdDesc,"
				+ "PriorRegPltCd,"
				+ "COALESCE(C.ItmCdDesc,' ') as PriorRegPltCdDesc,"
				+ "RegPltNo,"
				+ "PriorRegPltNo,"
				+ "DocNo,"
				+ "VehClassCd,"
				+ "VehMk,"
				+ "VIN,"
				+ "OwnrTtlName1,"
				+ "OwnrTtlName2,"
				+ "LienHldr1Name1, "
				+ "MfDwnCd "
				+ "from RTS.RTS_EXMPT_AUDIT A "
				+ "LEFT OUTER JOIN RTS.RTS_ITEM_CODES B ON A.REGPLTCD = B.ITMCD "
				+ "LEFT OUTER JOIN RTS.RTS_ITEM_CODES C ON A.PRIORREGPLTCD = C.ITMCD "
				+ "LEFT OUTER JOIN RTS.RTS_COMMON_FEES D ON A.REGCLASSCD = D.REGCLASSCD AND D.RTSEFFENDDATE = 99991231 "
				+ "LEFT OUTER JOIN RTS.RTS_COMMON_FEES E ON A.PRIORREGCLASSCD = E.REGCLASSCD AND E.RTSEFFENDDATE = 99991231 "
				+ "WHERE "
				+ " TransCompleteIndi = 1 AND "
				+ "TransAMDate between "
				+ laBeginDate.getAMDate()
				+ " and "
				+ laEndDate.getAMDate()
				+ " and OfcIssuanceNo in "
				+ lsOffices
				+ " Order by 1,4,3,6");

		//		// Where populated as follows:
		//		//	 - RegClassCd  		(yes)
		//		//	 - RegPltCd   	 	(yes)
		//		// 	 - PriorRegClassCd	(yes)
		//		// 	 - PriorRegPltCd 	(yes)
		//		lsQry.append(
		//			"SELECT "
		//				+ "OfcissuanceNo,"
		//				+ "SubstaId,"
		//				+ "TransAMDate,"
		//				+ "TransWsId,"
		//				+ "CustSeqNo,"
		//				+ "TransTime,"
		//				+ "ExmptIndi,"
		//				+ "PriorExmptIndi,"
		//				+ "A.RegClassCd,"
		//				+ "D.RegClassCdDesc as RegClassCdDesc,"
		//				+ "PriorRegClassCd,"
		//				+ "E.RegClassCdDesc as PriorRegClassCdDesc,"
		//				+ "RegExpMo,"
		//				+ "RegExpYr,"
		//				+ "TtlFeeIndi,"
		//				+ "VehModlYr,"
		//				+ "VoidedTransIndi,"
		//				+ "TransCd,"
		//				+ "TransEmpId,"
		//				+ "RegPltCd,"
		//				+ "B.ItmCdDesc as RegPltCdDesc,"
		//				+ "PriorRegPltCd,"
		//				+ "C.ItmCdDesc as PriorRegPltCdDesc,"
		//				+ "RegPltNo,"
		//				+ "PriorRegPltNo,"
		//				+ "DocNo,"
		//				+ "VehClassCd,"
		//				+ "VehMk,"
		//				+ "VIN,"
		//				+ "OwnrTtlName1,"
		//				+ "OwnrTtlName2,"
		//				+ "LienHldr1Name1, "
		//				+ "MfDwnCd "
		//				+ "from RTS.RTS_EXMPT_AUDIT A, "
		//				+ "RTS.RTS_ITEM_CODES B, "
		//				+ "RTS.RTS_ITEM_CODES C, "
		//				+ "RTS.RTS_COMMON_FEES D, "
		//				+ "RTS.RTS_COMMON_FEES E "
		//				+ "WHERE "
		//				+ " TransCompleteIndi = 1 AND "
		//				+ " A.REGPLTCD = B.ITMCD AND "
		//				+ " A.PRIORREGPLTCD = C.ITMCD AND "
		//				+ " A.REGCLASSCD = D.REGCLASSCD AND "
		//				+ " D.RTSEFFDATE = (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES F "
		//				+ " WHERE F.REGCLASSCD = A.REGCLASSCD ) AND "
		//				+ " A.PRIORREGCLASSCD = E.REGCLASSCD AND "
		//				+ " E.RTSEFFDATE = (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES G "
		//				+ " WHERE G.REGCLASSCD = A.PRIORREGCLASSCD) AND "
		//				+ "TransAMDate between "
		//				+ laBeginDate.getAMDate()
		//				+ " and "
		//				+ laEndDate.getAMDate()
		//				+ " and OfcIssuanceNo in "
		//				+ lsOffices);
		//		// Where populated as follows:
		//		//	 - RegClassCd  		(yes)
		//		//	 - RegPltCd   	 	(yes)
		//		// 	 - PriorRegClassCd	 (no)
		//		// 	 - PriorRegPltCd 	 (no)
		//		lsQry.append(
		//			" UNION ALL "
		//				+ "SELECT "
		//				+ "OfcissuanceNo,"
		//				+ "SubstaId,"
		//				+ "TransAMDate,"
		//				+ "TransWsId,"
		//				+ "CustSeqNo,"
		//				+ "TransTime,"
		//				+ "ExmptIndi,"
		//				+ "PriorExmptIndi,"
		//				+ "A.RegClassCd,"
		//				+ "D.RegClassCdDesc as RegClassCdDesc,"
		//				+ "PriorRegClassCd,"
		//				+ "' ' as PriorRegClassCdDesc,"
		//				+ "RegExpMo,"
		//				+ "RegExpYr,"
		//				+ "TtlFeeIndi,"
		//				+ "VehModlYr,"
		//				+ "VoidedTransIndi,"
		//				+ "TransCd,"
		//				+ "TransEmpId,"
		//				+ "RegPltCd,"
		//				+ "B.ItmCdDesc as RegPltCdDesc,"
		//				+ "' ' as PriorRegPltCd,"
		//				+ "' ' as PriorRegPltCdDesc,"
		//				+ "RegPltNo,"
		//				+ "PriorRegPltNo,"
		//				+ "DocNo,"
		//				+ "VehClassCd,"
		//				+ "VehMk,"
		//				+ "VIN,"
		//				+ "OwnrTtlName1,"
		//				+ "OwnrTtlName2,"
		//				+ "LienHldr1Name1, "
		//				+ "MfDwnCd "
		//				+ "from RTS.RTS_EXMPT_AUDIT A, "
		//				+ "RTS.RTS_ITEM_CODES B, "
		//				+ "RTS.RTS_COMMON_FEES D "
		//				+ "WHERE "
		//				+ " TransCompleteIndi = 1 AND "
		//				+ " A.REGPLTCD = B.ITMCD AND "
		//				+ " A.REGCLASSCD = D.REGCLASSCD AND "
		//				+ " D.RTSEFFDATE = (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES F "
		//				+ " WHERE F.REGCLASSCD = A.REGCLASSCD ) AND "
		//				+ " A.PRIORREGCLASSCD = 0 AND "
		//				+ " A.PRIORREGPLTCD is NULL AND "
		//				+ " TransAMDate between "
		//				+ laBeginDate.getAMDate()
		//				+ " and "
		//				+ laEndDate.getAMDate()
		//				+ " and OfcIssuanceNo in "
		//				+ lsOffices);
		//
		//		// SBRNW 	
		//		//	Where populated as follows:
		//		//	 - RegClassCd  		(yes)
		//		//	 - RegPltCd   	 	 (no)
		//		//  - PriorRegClassCd	 (no)
		//		//  - PriorRegPltCd 	 (no)
		//		lsQry.append(
		//			" UNION ALL "
		//				+ "SELECT "
		//				+ "OfcissuanceNo,"
		//				+ "SubstaId,"
		//				+ "TransAMDate,"
		//				+ "TransWsId,"
		//				+ "CustSeqNo,"
		//				+ "TransTime,"
		//				+ "ExmptIndi,"
		//				+ "PriorExmptIndi,"
		//				+ "A.RegClassCd,"
		//				+ "D.RegClassCdDesc as RegClassCdDesc,"
		//				+ "PriorRegClassCd,"
		//				+ "' ' as PriorRegClassCdDesc,"
		//				+ "RegExpMo,"
		//				+ "RegExpYr,"
		//				+ "TtlFeeIndi,"
		//				+ "VehModlYr,"
		//				+ "VoidedTransIndi,"
		//				+ "TransCd,"
		//				+ "TransEmpId,"
		//				+ "' ' as RegPltCd,"
		//				+ "' ' as RegPltCdDesc,"
		//				+ "' ' as PriorRegPltCd,"
		//				+ "' ' as PriorRegPltCdDesc,"
		//				+ "RegPltNo,"
		//				+ "PriorRegPltNo,"
		//				+ "DocNo,"
		//				+ "VehClassCd,"
		//				+ "VehMk,"
		//				+ "VIN,"
		//				+ "OwnrTtlName1,"
		//				+ "OwnrTtlName2,"
		//				+ "LienHldr1Name1, "
		//				+ "MfDwnCd "
		//				+ "from RTS.RTS_EXMPT_AUDIT A, "
		//				+ "RTS.RTS_COMMON_FEES D "
		//				+ "WHERE "
		//				+ " TransCompleteIndi = 1 AND "
		//				+ " A.REGCLASSCD = D.REGCLASSCD AND "
		//				+ " D.RTSEFFDATE = (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES F "
		//				+ " WHERE F.REGCLASSCD = A.REGCLASSCD ) AND "
		//				+ " A.PRIORREGCLASSCD = 0 AND "
		//				+ " A.REGPLTCD is NULL AND "
		//				+ " A.PRIORREGPLTCD is NULL AND "
		//				+ " TransAMDate between "
		//				+ laBeginDate.getAMDate()
		//				+ " and "
		//				+ laEndDate.getAMDate()
		//				+ " and OfcIssuanceNo in "
		//				+ lsOffices);
		//		// Where populated as follows:
		//		//	- RegClassCd  		(yes)
		//		//	- RegPltCd   	 	(yes)
		//		//  - PriorRegClassCd	 (no)
		//		//  - PriorRegPltCd 	 (yes)
		//		lsQry.append(
		//			"UNION ALL "
		//				+ "SELECT "
		//				+ "OfcissuanceNo,"
		//				+ "SubstaId,"
		//				+ "TransAMDate,"
		//				+ "TransWsId,"
		//				+ "CustSeqNo,"
		//				+ "TransTime,"
		//				+ "ExmptIndi,"
		//				+ "PriorExmptIndi,"
		//				+ "A.RegClassCd,"
		//				+ "D.RegClassCdDesc as RegClassCdDesc,"
		//				+ "PriorRegClassCd,"
		//				+ "' ' as PriorRegClassCdDesc,"
		//				+ "RegExpMo,"
		//				+ "RegExpYr,"
		//				+ "TtlFeeIndi,"
		//				+ "VehModlYr,"
		//				+ "VoidedTransIndi,"
		//				+ "TransCd,"
		//				+ "TransEmpId,"
		//				+ "RegPltCd,"
		//				+ "B.ItmCdDesc as RegPltCdDesc,"
		//				+ "PriorRegPltCd,"
		//				+ "C.ItmCdDesc as PriorRegPltCdDesc,"
		//				+ "RegPltNo,"
		//				+ "PriorRegPltNo,"
		//				+ "DocNo,"
		//				+ "VehClassCd,"
		//				+ "VehMk,"
		//				+ "VIN,"
		//				+ "OwnrTtlName1,"
		//				+ "OwnrTtlName2,"
		//				+ "LienHldr1Name1, "
		//				+ "MfDwnCd "
		//				+ "from RTS.RTS_EXMPT_AUDIT A, "
		//				+ "RTS.RTS_ITEM_CODES B, "
		//				+ "RTS.RTS_ITEM_CODES C, "
		//				+ "RTS.RTS_COMMON_FEES D "
		//				+ "WHERE "
		//				+ " TransCompleteIndi = 1 AND "
		//				+ " A.REGPLTCD = B.ITMCD AND "
		//				+ " A.PRIORREGPLTCD = C.ITMCD AND "
		//				+ " A.REGCLASSCD = D.REGCLASSCD AND "
		//				+ " D.RTSEFFDATE = (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES F "
		//				+ " WHERE F.REGCLASSCD = A.REGCLASSCD ) AND "
		//				+ " A.PRIORREGCLASSCD = 0 AND "
		//				+ "TransAMDate between "
		//				+ laBeginDate.getAMDate()
		//				+ " and "
		//				+ laEndDate.getAMDate()
		//				+ " and OfcIssuanceNo in "
		//				+ lsOffices);
		//		// Where populated as follows:
		//		//	- RegClassCd  		(yes)
		//		//	- RegPltCd   	 	(yes)
		//		//  - PriorRegClassCd	(yes)
		//		//  - PriorRegPltCd 	 (no)
		//		lsQry.append(
		//			" UNION ALL "
		//				+ "SELECT "
		//				+ "OfcissuanceNo,"
		//				+ "SubstaId,"
		//				+ "TransAMDate,"
		//				+ "TransWsId,"
		//				+ "CustSeqNo,"
		//				+ "TransTime,"
		//				+ "ExmptIndi,"
		//				+ "PriorExmptIndi,"
		//				+ "A.RegClassCd,"
		//				+ "D.RegClassCdDesc as RegClassCdDesc,"
		//				+ "PriorRegClassCd,"
		//				+ "E.RegClassCdDesc as PriorRegClassCdDesc,"
		//				+ "RegExpMo,"
		//				+ "RegExpYr,"
		//				+ "TtlFeeIndi,"
		//				+ "VehModlYr,"
		//				+ "VoidedTransIndi,"
		//				+ "TransCd,"
		//				+ "TransEmpId,"
		//				+ "RegPltCd,"
		//				+ "B.ItmCdDesc as RegPltCdDesc,"
		//				+ "' ' as PriorRegPltCd,"
		//				+ "' ' as PriorRegPltCdDesc,"
		//				+ "RegPltNo,"
		//				+ "PriorRegPltNo,"
		//				+ "DocNo,"
		//				+ "VehClassCd,"
		//				+ "VehMk,"
		//				+ "VIN,"
		//				+ "OwnrTtlName1,"
		//				+ "OwnrTtlName2,"
		//				+ "LienHldr1Name1, "
		//				+ "MfDwnCd "
		//				+ "from RTS.RTS_EXMPT_AUDIT A, "
		//				+ "RTS.RTS_ITEM_CODES B, "
		//				+ "RTS.RTS_COMMON_FEES D, "
		//				+ "RTS.RTS_COMMON_FEES E "
		//				+ "WHERE "
		//				+ " TransCompleteIndi = 1 AND "
		//				+ " A.REGPLTCD = B.ITMCD AND "
		//				+ " A.PRIORREGPLTCD IS NULL AND "
		//				+ " A.REGCLASSCD = D.REGCLASSCD AND "
		//				+ " D.RTSEFFDATE = (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES F "
		//				+ " WHERE F.REGCLASSCD = A.REGCLASSCD ) AND "
		//				+ " A.PRIORREGCLASSCD = E.REGCLASSCD AND "
		//				+ " E.RTSEFFDATE = (SELECT MAX(RTSEFFDATE) FROM RTS.RTS_COMMON_FEES G "
		//				+ " WHERE G.REGCLASSCD = A.PRIORREGCLASSCD) AND "
		//				+ "TransAMDate between "
		//				+ laBeginDate.getAMDate()
		//				+ " and "
		//				+ laEndDate.getAMDate()
		//				+ " and OfcIssuanceNo in "
		//				+ lsOffices
		//				+ " Order by 1,4,3,6");
		// end defect 9391 

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryExportExemptAudit - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryExportExemptAudit - SQL - End");

			/******************************************************
			 *         WARNING, WARNING, WARNING
			 ******************************************************
			 *   THE ORDER OF THESE COLUMNS MUST MATCH THE ORDER OF 
			 *   FIELDS IN COLUMN_NAME_ARRAY !!!!
			 *******************************************************/
			boolean lbFirst = true;

			while (lrsQry.next())
			{
				if (lbFirst)
				{
					lsExportData = getColumnHdrs();
					lbFirst = false;
				}
				// New Line 
				lsExportData.append(
					CommonConstant.SYSTEM_LINE_SEPARATOR);

				// To Build TransId 	
				int liOfc = caDA.getIntFromDB(lrsQry, "OfcIssuanceNo");
				int liTransWsId =
					caDA.getIntFromDB(lrsQry, "TransWsId");
				int liTransAMDate =
					caDA.getIntFromDB(lrsQry, "TransAMDate");
				int liTransTime =
					caDA.getIntFromDB(lrsQry, "TransTime");

				// "TransDate"  		
				RTSDate laCalcDate =
					new RTSDate(RTSDate.AMDATE, liTransAMDate);

				lsExportData.append(
					CommonConstant.STR_DOUBLE_QUOTE
						+ laCalcDate.toString()
						+ CommonConstant.STR_DOUBLE_QUOTE);

				// Build TransId String  	
				String lsTransId =
					UtilityMethods.getTransId(
						liOfc,
						liTransWsId,
						liTransAMDate,
						liTransTime);

				// TransId
				lsExportData.append(getStringToAppend(lsTransId));

				// OfcIssuanceNo  
				lsExportData.append(CommonConstant.STR_COMMA + liOfc);

				// SubstaId
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "SubstaId"));

				// TransAMDate		
				lsExportData.append(
					CommonConstant.STR_COMMA + liTransAMDate);

				// TransWsId 
				lsExportData.append(
					CommonConstant.STR_COMMA + liTransWsId);

				// CustSeqNo 
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "CustSeqNo"));

				// TransTime 		
				lsExportData.append(
					CommonConstant.STR_COMMA + liTransTime);

				// TransCd 	
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "TransCd")));

				// TransEmpId 
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "TransEmpId")));

				// VoidedTransIndi 
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));

				// OwnrTtlName1
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "OwnrTtlName1")));

				// OwnrTtlName2					
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "OwnrTtlName2")));

				// LienHldr1Name1 					
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(
							lrsQry,
							"LienHldr1Name1")));

				// ExmptIndi 
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "ExmptIndi"));

				// PriorExmptIndi 		
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "PriorExmptIndi"));
				// DocNo
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "DocNo")));

				// VIN 		
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "VIN")));

				// VehClassCd 	
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "VehClassCd")));

				// VehMk 	
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "VehMk")));

				// VehModlYr 	
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "VehModlYr"));

				// RegClassCd 			
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "RegClassCd"));

				// RegClassCdDesc 		
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(
							lrsQry,
							"RegClassCdDesc")));

				// PriorRegClassCd 
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "PriorRegClassCd"));

				// PriorRegClassCdDesc
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(
							lrsQry,
							"PriorRegClassCdDesc")));

				// RegPltCd 
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "RegPltCd")));

				// RegPltCdDesc 	
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "RegPltCdDesc")));

				// PriorRegPltCd
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "PriorRegPltCd")));

				// PriorRegPltCdDesc 
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(
							lrsQry,
							"PriorRegPltCdDesc")));

				// RegPltNo
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "RegPltNo")));

				// PriorRegPltNo
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "PriorRegPltNo")));

				// RegExpMo
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "RegExpMo"));

				// RegExpYr
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "RegExpYr"));

				// TtlFeeIndi 
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "TtlFeeIndi"));

				// MfDwnCd 
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "MfDwnCd"));

			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryExportExemptAudit - End ");
			return lsExportData.toString();
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryExportExemptAudit - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	* Method to Insert into RTS.RTS_EXMPT_AUDIT
	* 
	* @param  aaExemptAuditData  aaExemptAuditData	
	* @throws RTSException 	
	*/
	public void insExemptAudit(ExemptAuditData aaExemptAuditData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insExemptAudit - begin");

		Vector lvValues = new Vector();

		// defect 9847 
		if (aaExemptAuditData.getTransEmpId() == null
			|| aaExemptAuditData.getTransEmpId().trim().length() == 0)
		{
			aaExemptAuditData.setTransEmpId(
				CommonConstant.DEFAULT_TRANSEMPID);
		}
		// end defect 9847 

		// defect 9017 
		// add MfDwnCd 
		String lsIns =
			"INSERT into RTS.RTS_EXMPT_AUDIT  ("
				+ "OfcissuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "ExmptIndi,"
				+ "PriorExmptIndi,"
				+ "RegClassCd,"
				+ "PriorRegClassCd,"
				+ "RegExpMo,"
				+ "RegExpYr,"
				+ "TtlFeeIndi,"
				+ "VehModlYr,"
				+ "VoidedTransIndi,"
				+ "TransCd,"
				+ "TransEmpId,"
				+ "RegPltCd,"
				+ "PriorRegPltCd,"
				+ "RegPltNo,"
				+ "PriorRegPltNo,"
				+ "DocNo,"
				+ "VehClassCd,"
				+ "VehMk,"
				+ "VIN,"
				+ "OwnrTtlName1,"
				+ "OwnrTtlName2,"
				+ "TransId,"
				+ "LienHldr1Name1,"
				+ "TransCompleteIndi,"
				+ "MfDwnCd) VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " 0,"
				+ " ?)";
		// end defect 9017 

		try
		{
			// OfcIssuanceNo 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getOfcIssuanceNo())));

			// SubstaId
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getSubstaId())));

			// TransAMDate 			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransAMDate())));

			// TransWsId 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransWsId())));

			// CustSeqNo 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getCustSeqNo())));

			// TransTime 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransTime())));

			// ExmptIndi 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getExmptIndi())));

			// PriorExmptIndi 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getPriorExmptIndi())));

			// RegClassCd 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getRegClassCd())));

			// PriorRegClassCd 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getPriorRegClassCd())));

			// RegExpMo 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getRegExpMo())));

			// RegExpYr 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getRegExpYr())));

			// TtlFeeIndi 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTtlFeeIndi())));

			// VehModlYr
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getVehModlYr())));

			// VoidedTransIndi 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getVoidedTransIndi())));

			// TransCd
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransCd())));

			// TransEmpId
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransEmpId())));

			// RegPltCd						
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getRegPltCd())));

			// PriorRegPltCd
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getPriorRegPltCd())));

			// RegPltNo
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getRegPltNo())));

			// PriorRegPltNo
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getPriorRegPltNo())));

			// DocNo
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getDocNo())));
			// VehClassCd
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getVehClassCd())));

			// VehMk
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getVehMk())));
			// VIN
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getVIN())));

			// OwnrTtlName1
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getOwnrTtlName1())));

			// OwnrTtlName2
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getOwnrTtlName2())));

			// TransId
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransId())));
			// end defect 10505 

			// LienHldr1Name1 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getLienHldr1Name1())));

			// defect 9017 
			// MfDwnCd 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getMfDwnCd())));
			// end defect 9017 

			Log.write(Log.SQL, this, "insExemptAudit - SQL - begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, "insExemptAudit - SQL - end");
			Log.write(Log.METHOD, this, "insExemptAudit - end");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insExemptAudit - - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
	/**
	 * Method to Delete from RTS.RTS_EXMPT_AUDIT for Purge
	 * Delete only for Purge
	 * 
	 * @param  aiPurgeAMDate int	
	 * @return int
	 * @throws RTSException
	 */
	public int purgeExemptAudit(int aiPurgeAMDate) throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeExemptAudit - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_EXMPT_AUDIT WHERE TransAMDate <= ?  ";

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiPurgeAMDate)));

		try
		{
			Log.write(Log.SQL, this, "purgeExemptAudit - SQL - Begin");

			// defect 9825
			// Return number of rows purged
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "purgeExemptAudit - SQL - End");
			Log.write(Log.METHOD, this, "purgeExemptAudit - End");
			return liNumRows;
			// end defect 9825 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeExemptAudit - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD
	/**
	 * Method to update RTS.RTS_EXMPT_AUDIT with TransCompleteIndi
	 * 
	 * @param  aaExemptAuditData ExemptAuditData	
	 * @throws RTSException 	
	 */
	public void updExemptAudit(ExemptAuditData aaExemptAuditData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updExemptAudit - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_EXMPT_AUDIT SET TransCompleteIndi  = 1 "
				+ " WHERE "
				+ " OfcIssuanceNo = ? AND "
				+ " SubstaId = ? AND "
				+ " TransAMDate = ? AND "
				+ " TransWsId = ? AND "
				+ " CustSeqNo = ? ";
		try
		{

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaExemptAuditData.getCustSeqNo())));

			Log.write(Log.SQL, this, "updExemptAudit - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updExemptAudit - SQL - End");
			Log.write(Log.METHOD, this, "updExemptAudit - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updExemptAudit - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
}
