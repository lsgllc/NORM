package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ElectronicTitleHistoryData;
import com.txdot.isd.rts.services.data.ElectronicTitleHistoryUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * ElectronicTitleHistory.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/20/2009	Created
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	06/14/2010	add TransId
 * 							modify insElectronicTitleHistory(), 
 * 							 qryElectronicTitleHistoryReport() 
 * 							defect 10505 Ver 6.5.0 
 * --------------------------------------------------------------------- 	
 */

/**
 *  This class allows user to access RTS_ETTL_HSTRY 
 *
 * @version	6.5.0 			06/14/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		03/20/2009
 */
public class ElectronicTitleHistory
{
	DatabaseAccess caDA;

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
			"PermLienhldrId",
			"RegPltNo",
			"VIN",
			"OwnrTtlName1",
			"OwnrTtlName2",
			"VoidedTransIndi",
			"CertfdLienhldrName1",
			"CertfdLienhldrName2" };

	/**
	 * ElectronicTitleHistory constructor comment.
	 *
	 * @param  aaDA	DatabaseAccess 
	 * @throws RTSException
	 */
	public ElectronicTitleHistory(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

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
	 * Method to Delete RTS.RTS_ETTL_HSTRY
	 * 
	 * @param  aaETtlHstryData ElectronicTitleHistoryData	
	 * @throws RTSException
	 */
	public void delElectronicTitleHistory(ElectronicTitleHistoryData aaETtlHstryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"delElectronicTitleHistory - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"Delete from RTS.RTS_ETTL_HSTRY "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ?  AND "
				+ "CustSeqNo = ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getCustSeqNo())));

			if (aaETtlHstryData.getTransTime() != 0)
			{
				lsDel = lsDel + " AND TransTime = ? ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaETtlHstryData.getTransTime())));
			}

			Log.write(
				Log.SQL,
				this,
				"delElectronicTitleHistory - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);

			Log.write(
				Log.SQL,
				this,
				"delElectronicTitleHistory - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"delElectronicTitleHistory - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delElectronicTitleHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
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
	 * Method to build IN clause for int Column if selected 
	 *
	 * @param asColumnName
	 * @param avInt 
	 * @return String
	 * @throws RTSException 
	 */
	public String getIntInClause(String asColumnName, Vector avInt)
	{
		String lsResult = "";
		if (avInt != null && avInt.size() > 0)
		{
			lsResult = " AND " + asColumnName + " in (" + avInt.get(0);

			for (int i = 1; i < avInt.size(); i++)
			{
				lsResult =
					lsResult + CommonConstant.STR_COMMA + avInt.get(i);

			}
			lsResult = lsResult + ") ";
		}
		return lsResult;
	}

	/**
	 * Method to build IN clause for String Column if selected 
	 *
	 * @param asColumnName
	 * @param avColumnValues
	 * @return String
	 * @throws RTSException 
	 */
	public String getStringInClause(
		String asColumnName,
		Vector avColumnValues)
	{
		String lsResult = "";

		if (avColumnValues != null && avColumnValues.size() > 0)
		{
			lsResult =
				" AND "
					+ asColumnName
					+ " in "
					+ "("
					+ CommonConstant.STR_SINGLE_QUOTE
					+ avColumnValues.get(0)
					+ CommonConstant.STR_SINGLE_QUOTE;

			for (int i = 1; i < avColumnValues.size(); i++)
			{
				lsResult =
					lsResult
						+ CommonConstant.STR_COMMA
						+ CommonConstant.STR_SINGLE_QUOTE
						+ (String) avColumnValues.get(i)
						+ CommonConstant.STR_SINGLE_QUOTE;

			}
			lsResult = lsResult + ") ";
		}
		return lsResult;
	}

	/**
	 * Method to Insert into RTS.RTS_ETTL_HSTRY
	 * 
	 * @param  aaETtlHstryData ElectronicTitleHistoryData	
	 * @throws RTSException
	 */
	public void insElectronicTitleHistory(ElectronicTitleHistoryData aaETtlHstryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insElectronicTitleHistory - Begin");

		Vector lvValues = new Vector();

		if (UtilityMethods.isEmpty(aaETtlHstryData.getTransEmpId()))
		{
			aaETtlHstryData.setTransEmpId(
				CommonConstant.DEFAULT_TRANSEMPID);
		}

		// defect 10505 
		// Add TransId 
		String lsIns =
			"INSERT into RTS.RTS_ETTL_HSTRY("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "TransEmpId,"
				+ "RegPltNo,"
				+ "PermLienhldrId,"
				+ "VIN,"
				+ "OwnrTtlName1,"
				+ "OwnrTtlName2,"
				+ "TransId,"
				+ "TransCompleteIndi,"
				+ "VoidedTransIndi)";

		lsIns =
			lsIns
				+ "VALUES ( "
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
				+ " 0)";
		// end defect 10505 
		try
		{

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransTime())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransCd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransEmpId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getRegPltNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getPermLienhldrId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getVIN())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getOwnrTtlName1())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getOwnrTtlName2())));

			// defect 10505 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransId())));
			// end defect 10505 

			Log.write(
				Log.SQL,
				this,
				"insElectronicTitleHistory - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insElectronicTitleHistory - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insElectronicTitleHistory - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insElectronicTitleHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_ETTL_HSTRY for purge
	 *
	 * @param  aiPurgeAMDate int	
	 * @return int
	 * @throws RTSException 
	 */
	public int purgeElectronicTitleHistory(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeElectronicTitleHistory - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			" DELETE FROM RTS.RTS_ETTL_HSTRY  "
				+ " WHERE  "
				+ " TRANSAMDATE <= ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));

			Log.write(
				Log.SQL,
				this,
				"purgeElectronicTitleHistory - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeElectronicTitleHistory - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeElectronicTitleHistory - End");
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeElectronicTitleHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query from RTS.RTS_ETTL_HSTRY
	 *
	 * @param 	aaETtlHstryUIData ElectronicTitleHistoryUIData	
	 * @return  Vector 
	 * @throws 	RTSException 	
	 */
	public Vector qryElectronicTitleHistoryReport(ElectronicTitleHistoryUIData aaETtlHstryUIData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryElectronicTitleHistoryReport - Begin");
		RTSDate laBeginDate = aaETtlHstryUIData.getBeginDate();
		RTSDate laEndDate = aaETtlHstryUIData.getEndDate();

		String lsOffices =
			getIntInClause(
				"A.OfcissuanceNo",
				aaETtlHstryUIData.getSelectedOffices());

		String lsPermLienhldrIds =
			getStringInClause(
				"A.PermLienhldrId",
				aaETtlHstryUIData.getSelectedPermLienhldrId());

		String lsTransCds =
			getStringInClause(
				"A.TransCd",
				aaETtlHstryUIData.getSelectedTransCd());

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// defect 10505 
		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "TransEmpId,"
				+ "A.PermLienhldrId,"
				+ "RegPltNo, "
				+ "VIN,"
				+ "OwnrTtlName1,"
				+ "OwnrTtlName2,"
				+ "TransId,"
				+ "VoidedTransIndi,"
				+ "CertfdLienhldrName1,"
				+ "CertfdLienhldrName2 "
				+ "FROM RTS.RTS_ETTL_HSTRY A, "
				+ "RTS.RTS_CERTFD_LIENHLDR B "
				+ "Where "
				+ "A.PermLienhldrId  = B.PermLienhldrId AND "
				+ "B.RTSEFFENDDATE = 99991231 AND "
				+ "A.TransAMDate between "
				+ laBeginDate.getAMDate()
				+ " and "
				+ laEndDate.getAMDate()
				+ lsPermLienhldrIds
				+ lsOffices
				+ lsTransCds
				+ " and TransCompleteIndi = 1 "
				+ " Order by Ofcissuanceno,TransAMDate,TransWsId,TransTime");
		// end defect 10505 

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryElectronicTitleHistoryReport - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryElectronicTitleHistoryReport - SQL - End");

			while (lrsQry.next())
			{
				ElectronicTitleHistoryData laETtlHstryData =
					new ElectronicTitleHistoryData();
				laETtlHstryData.setOfcissuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laETtlHstryData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laETtlHstryData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laETtlHstryData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laETtlHstryData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laETtlHstryData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laETtlHstryData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laETtlHstryData.setVIN(
					caDA.getStringFromDB(lrsQry, "VIN"));
				laETtlHstryData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laETtlHstryData.setPermLienhldrId(
					caDA.getStringFromDB(lrsQry, "PermLienhldrId"));
				laETtlHstryData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));
				laETtlHstryData.setOwnrTtlName1(
					caDA.getStringFromDB(lrsQry, "OwnrTtlName1"));
				laETtlHstryData.setOwnrTtlName2(
					caDA.getStringFromDB(lrsQry, "OwnrTtlName2"));
				// defect 10505 
				laETtlHstryData.setTransId(
					caDA.getStringFromDB(lrsQry, "TransId"));
				// end defect 10505 
				laETtlHstryData.setCertfdLienHldrName1(
					caDA.getStringFromDB(
						lrsQry,
						"CertfdLienHldrName1"));
				laETtlHstryData.setCertfdLienHldrName2(
					caDA.getStringFromDB(
						lrsQry,
						"CertfdLienHldrName2"));
				laETtlHstryData.setVoidedTransIndi(
					caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));
				// Add element to the Vector
				lvRslt.addElement(laETtlHstryData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryElectronicTitleHistoryReport - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryElectronicTitleHistoryReport - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryElectronicTitleHistoryReport - RTS Exception "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD	

	/**
	* Method to Export from RTS.RTS_ETTL_HSTRY
	*
	* @return Vector
	* @throws RTSException 
	*/
	public String qryExportElectronicTitleHistory(ElectronicTitleHistoryUIData aaETtlHstryUIData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryExportElectronicTitleHistory - Begin");
		RTSDate laBeginDate = aaETtlHstryUIData.getBeginDate();
		RTSDate laEndDate = aaETtlHstryUIData.getEndDate();
		StringBuffer lsExportData = new StringBuffer();

		String lsOffices =
			getIntInClause(
				"A.OfcissuanceNo",
				aaETtlHstryUIData.getSelectedOffices());

		String lsPermLienhldrIds =
			getStringInClause(
				"A.PermLienhldrId",
				aaETtlHstryUIData.getSelectedPermLienhldrId());

		String lsTransCds =
			getStringInClause(
				"A.TransCd",
				aaETtlHstryUIData.getSelectedTransCd());

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "TransEmpId,"
				+ "A.PermLienhldrId,"
				+ "RegPltNo, "
				+ "VIN,"
				+ "OwnrTtlName1,"
				+ "OwnrTtlName2,"
				+ "VoidedTransIndi,"
				+ "CertfdLienhldrName1,"
				+ "CertfdLienhldrName2  "
				+ "FROM RTS.RTS_ETTL_HSTRY A, "
				+ "RTS.RTS_CERTFD_LIENHLDR B  "
				+ "WHERE A.PermLienhldrId  = B.PermLienhldrId AND "
				+ "B.RTSEFFENDDATE = (Select max(RTSEffEndDate) from "
				+ "RTS.RTS_CERTFD_LIENHLDR C WHERE B.PermLienhldrId = "
				+ " C.PermLienhldrId ) AND "
				+ "A.TransAMDate between "
				+ laBeginDate.getAMDate()
				+ " and "
				+ laEndDate.getAMDate()
				+ lsPermLienhldrIds
				+ lsOffices
				+ lsTransCds
				+ " and TransCompleteIndi = 1 "
				+ " Order by Ofcissuanceno,TransAMDate,PermLienhldrId ");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryExportElectronicTitleHistory - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryExportElectronicTitleHistory - SQL - End");
			Log.write(
				Log.SQL,
				this,
				" - qryExportElectronicTitleHistory - SQL - End");

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

				// PermLienhldrId 
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(
							lrsQry,
							"PermLienhldrId")));

				// RegPltNo 
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "RegPltNo")));

				// VIN 
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "VIN")));

				// OwnrTtlName1
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "OwnrTtlName1")));

				// OwnrTtlName2					
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(lrsQry, "OwnrTtlName2")));

				// VoidedTransIndi 
				lsExportData.append(
					CommonConstant.STR_COMMA
						+ caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));

				// CertfdLienhldrName1 					
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(
							lrsQry,
							"CertfdLienhldrName1")));

				// CertfdLienhldrName2 					
				lsExportData.append(
					getStringToAppend(
						caDA.getStringFromDB(
							lrsQry,
							"CertfdLienhldrName2")));

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
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryExportExemptAudit - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD	

	/**
	 * Method to mark RTS.RTS_ETTL_HSTRY as complete
	 * 
	 * @param  aaETtlHstryData ElectronicTitleHistoryData	
	 * @throws RTSException
	 */
	public void updElectronicTitleHistory(ElectronicTitleHistoryData aaETtlHstryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updSpecialPlateTransactionHistory - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_ETTL_HSTRY "
				+ " SET TRANSCOMPLETEINDI = 1 WHERE  "
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
						aaETtlHstryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransWsId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getCustSeqNo())));
			Log.write(
				Log.SQL,
				this,
				"updElectronicTitleHistory - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(
				Log.SQL,
				this,
				"updElectronicTitleHistory - SQL - End");

			Log.write(
				Log.METHOD,
				this,
				"updElectronicTitleHistory - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updElectronicTitleHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF UPDATE METHOD

	/**
	 * Method to VOID RTS.RTS_ETTL_HSTRY
	 * 
	 * @param  aaETtlHstryData ElectronicTitleHistoryData	
	 * @throws RTSException
	 */
	public void voidElectronicTitleHistory(ElectronicTitleHistoryData aaETtlHstryData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"voidElectronicTitleHistory - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_ETTL_HSTRY "
				+ " SET VOIDEDTRANSINDI= 1 "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "Transtime = ? AND "
				+ "TransCompleteIndi = 1 ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransWsId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaETtlHstryData.getTransTime())));

			Log.write(
				Log.SQL,
				this,
				"voidElectronicTitleHistory - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(
				Log.SQL,
				this,
				"voidElectronicTitleHistory - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"voidElectronicTitleHistory - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"voidElectronicTitleHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF VOID METHOD	
}