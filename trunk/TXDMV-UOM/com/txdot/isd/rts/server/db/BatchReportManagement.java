package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.BatchReportManagementData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * BatchReportManagement.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/14/2011	Created
 * 							defect 10701 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * SQL for RTS.RTS_BATCH_RPT_MGMT
 *
 * @version	6.7.0 			01/14/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/14/2011 16:18:17
 */
public class BatchReportManagement
{
	private String csMethod = new String();
	DatabaseAccess caDA;

	/**
	 * BatchReportManagement.java Constructor
	 * 
	 */
	public BatchReportManagement(DatabaseAccess aaDA)
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_BATCH_RPT_MGMT
	 *
	 * @param  aaBRMData
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryBatchReportManagement(BatchReportManagementData aaBRMData)
		throws RTSException
	{
		csMethod = "qryBatchReportManagement";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		boolean lbOmitTimestmp = aaBRMData.getChngTimestmp() == null;

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "a.RptNumber,"
				+ "AutoPrntIndi, "
				+ "B.RptDesc,"
				+ "B.RptFileName, "
				+ "A.DeleteIndi,"
				+ "A.ChngTimestmp "
				+ "from RTS.RTS_BATCH_RPT_MGMT A,"
				+ "RTS.RTS_REPORTS B "
				+ "where B.BatchRptBaseIndi = 1 and "
				+ "A.RptNumber = B.RptNumber and "
				+ "OfcIssuanceNo = ? and "
				+ "SubstaId = ? "); 
				 

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaBRMData.getOfcIssuanceNo())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaBRMData.getSubstaId())));

		if (!UtilityMethods.isEmpty(aaBRMData.getFileName()))
		{
			lsQry.append(" AND RptFileName = ? ");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaBRMData.getFileName())));
		}
		if (lbOmitTimestmp)
		{
			lsQry.append(" AND DeleteIndi = 0 ");
		}
		else
		{
			lsQry.append(" AND ChngTimestmp > ?");
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaBRMData.getChngTimestmp())));
		}
		lsQry.append(" order by RptDesc");
		
		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				BatchReportManagementData laBRMData =
					new BatchReportManagementData();
				laBRMData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laBRMData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laBRMData.setRptDesc(
					caDA.getStringFromDB(lrsQry, "RptDesc"));
				laBRMData.setFileName(
					caDA.getStringFromDB(lrsQry, "RptFileName"));
				laBRMData.setRptNumber(
					caDA.getIntFromDB(lrsQry, "RptNumber"));
				laBRMData.setAutoPrntIndi(
					caDA.getIntFromDB(lrsQry, "AutoPrntIndi"));
				laBRMData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				if (!lbOmitTimestmp)
				{
					laBRMData.setChngTimestmp(
						caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				}
				// Add element to the Vector 
				lvRslt.addElement(laBRMData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_BATCH_RPT_MGMT
	 * 
	 * @param  aaBRMData	
	 * @return int		
	 * @throws RTSException 
	 */
	public int updBatchReportManagement(BatchReportManagementData aaBRMData)
		throws RTSException
	{
		csMethod = "updBatchReportManagement";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_BATCH_RPT_MGMT SET AutoPrntIndi = ?, "
				+ "Chngtimestmp = '"
				+ aaBRMData.getChngTimestmp().getTimestamp()
				+ "' WHERE "
				+ "OfcIssuanceNo = ? and "
				+ "SubstaId = ? and "
				+ "RptNumber = ?";

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaBRMData.getAutoPrntIndi())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaBRMData.getOfcIssuanceNo())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaBRMData.getSubstaId())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaBRMData.getRptNumber())));
		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
}
