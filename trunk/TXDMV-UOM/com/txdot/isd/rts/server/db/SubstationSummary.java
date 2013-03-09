package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.SubstationSummaryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * SubstationSummary.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/19/2001	Added purge
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 * 							query
 * K Harrell	10/18/2002	Substation Summary Performance
 * 							defect 4912 
 * K Harrell	06/30/2005	Java 1.4 Work
 * 							delete qrySubstationSummary(SubstationSummaryData)
 *							defect 7899 Ver 5.2.3
 * K Harrell	11/11/2005	Accept passed int vs. 
 * 							SubstationSummaryData Object.
 * 							modify purgeSubstationSummary() 
 * 							defect 8386 Ver 5.2.3  
 * K Harrell	11/09/2006	Modify purge for consistency; Use <= vs. <
 * 							modify purgeSubstationSummary() 
 * 							defect 9100 Ver Exempts
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeSubstationSummary()
 * 							defect 9825 Ver Defect_POS_D      
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_SUBSTA_SUMMARY
 * 
 * @version	Defect_POS_D 	01/19/2009 
 * @author	Kathy Harrell
 * <br>Creation Date:		09/17/2001 17:47:30 
 */

public class SubstationSummary extends SubstationSummaryData
{
	DatabaseAccess caDA;
	public SubstationSummary(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	* Method to Delete from RTS.RTS_SUBSTA_SUMMARY 
	* 
	* @param  aaSustaSumData SubstationSummaryData	
	* @throws RTSException 	
	*/

	public void delSubstationSummary(SubstationSummaryData aaSubstaSumData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delSubstationSummary - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_SUBSTA_SUMMARY "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubStaId = ? AND "
				+ "SummaryEffDate = ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSumData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSumData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSumData.getSummaryEffDate())));

			Log.write(
				Log.SQL,
				this,
				"delSubstationSummary - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"delSubstationSummary - SQL - End");

			Log.write(Log.METHOD, this, "delSubstationSummary - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delSubstationSummary - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD
	/**
	* Method to Insert into RTS.RTS_SUBSTA_SUMMARY
	* 
	* @param  aaSustaSumData SubstationSummaryData	
	* @throws RTSException 	
	*/
	public void insSubstationSummary(SubstationSummaryData aaSubstaSumData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insSubstationSummary - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_SUBSTA_SUMMARY ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "SummaryEffDate, "
				+ "SummaryTimestmp) "
				+ " VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " Current Timestamp) ";

		try
		{

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSumData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSumData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSumData.getSummaryEffDate())));

			Log.write(
				Log.SQL,
				this,
				"insSubstationSummary - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insSubstationSummary - SQL - End");

			Log.write(Log.METHOD, this, "insSubstationSummary - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insSubstationSummary - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
	/**
	* Method to Delete from RTS.RTS_SUBSTA_SUMMARY for Purge
	* 
	* @param  aiPurgeAMDate int	
	* @return int 
	* @throws RTSException 	
	*/

	public int purgeSubstationSummary(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeSubstationSummary - Begin");

		Vector lvValues = new Vector();

		// defect 9011
		// use "<=" vs. "<" 
		String lsDel =
			"DELETE FROM RTS.RTS_SUBSTA_SUMMARY A WHERE SummaryEffDate <= ? ";
		// end defect 9011 

		try
		{
			// defect 8423
			// use passed int 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));
			// end defect 8423		

			Log.write(
				Log.SQL,
				this,
				"purgeSubstationSummary - SQL - Begin");

			// defect 9825 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);

			Log.write(
				Log.SQL,
				this,
				"purgeSubstationSummary - SQL - End");

			Log.write(Log.METHOD, this, "purgeSubstationSummary - End");

			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeSubstationSummary - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD
	/**
	* Method to Query RTS.RTS_SUBSTA_SUMMARY for Max(SummaryEffDate)
	*   Used in CountyWide 
	* 
	* @param  liOfcNo int
	* @return int
	* @throws RTSException 	
	*/

	public int qrySubstationSummary(int liOfcNo) throws RTSException
	{
		Log.write(Log.METHOD, this, " - qrySubstationSummary - Begin");
		StringBuffer lsQry = new StringBuffer();

		int liSummaryEffDate = 0;

		ResultSet lrsQry;
		lsQry.append(
			"SELECT "
				+ "Max(SummaryEffDate) as SummaryEffDate "
				+ "FROM RTS.RTS_SUBSTA_SUMMARY where "
				+ "OFCISSUANCENO = "
				+ liOfcNo);
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qrySubstationSummary - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryMaxSubstationSummary - SQL - End");

			while (lrsQry.next())
			{
				liSummaryEffDate =
					caDA.getIntFromDB(lrsQry, "SummaryEffDate");
			} //End of While 

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qrySubstationSummary - End ");
			return (liSummaryEffDate);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySubstationSummary - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	* Method to update RTS.RTS_SUBSTA_SUMMARY
	* 
	* @param  aaSubstaSumData SubstationSummaryData	
	* @throws RTSException 	
	*/
	public void updSubstationSummary(SubstationSummaryData aaSubstaSumData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updSubstationSummary - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_SUBSTA_SUMMARY SET "
				+ "SummaryTimestmp = Current Timestamp "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubStaId = ?      AND "
				+ "SummaryEffDate = ? ";

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSumData.getOfcIssuanceNo())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSumData.getSubstaId())));
			//3 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSumData.getSummaryEffDate())));

			Log.write(
				Log.SQL,
				this,
				"updSubstationSummary - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updSubstationSummary - SQL - End");

			Log.write(Log.METHOD, this, "updSubstationSummary - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updSubstationSummary - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
