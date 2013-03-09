package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.SubstationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * Substation.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/18/2001	Altered test for SubstaId to != -1
 * N Ting		09/18/2001	Altered test for WsIds to Integer.MIN_VALUE
 * K Harrell	12/07/2001	Removed BranchOfcId
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	08/17/2002	Added purgeSubstation
 *							defect 4601
 * K Harrell	06/19/2005  Java 1.4 Work	
 * 							services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeSubstation()
 * 							defect 9825 Ver Defect_POS_D    
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_SUBSTA
 * 
 * @version	Defect_POS_D	01/19/2009
 * @author 	Kathy Harrell
 * <br> Creation Date:		09/12/2001 13:33:27 
 */
public class Substation extends SubstationData
{
	DatabaseAccess caDA;
	/**
	 * Substation constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public Substation(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Delete from RTS.RTS_SUBSTA for Purge
	 *
	 * @param  aiNumDays int
	 * @return int 
	 * @throws RTSException 
	 */
	public int purgeSubstation(int aiNumDays) throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeSubstation - Begin");
		Vector lvValues = new Vector();
		String lsDel =
			"DELETE FROM RTS.RTS_SUBSTA WHERE DELETEINDI = 1 and "
				+ " days(Current Date) - days(ChngTimestmp) > ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiNumDays)));

			Log.write(Log.SQL, this, "purgeSubstation - SQL - Begin");
			// defect 9825
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "purgeSubstation - SQL - End");
			Log.write(Log.METHOD, this, "purgeSubstation - End");
			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeSubstation - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF Delete METHOD
	/**
	 * Method to Query RTS.RTS_SUBSTA
	 *
	 * @param  aaSubstationData  SubstationData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qrySubstation(SubstationData aaSubstationData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qrySubstation - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "SubstaName,"
				+ "SubstaSt,"
				+ "SubstaCity,"
				+ "SubstaZpCd,"
				+ "SubstaZpCdP4,"
				+ "DeleteIndi,"
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_SUBSTA where OfcIssuanceNo = ? ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstationData.getOfcIssuanceNo())));
			if (aaSubstationData.getSubstaId() != Integer.MIN_VALUE)
			{
				lsQry.append(" and SubstaId = ? ");
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSubstationData.getSubstaId())));
			}
			if (aaSubstationData.getChngTimestmp() == null)
			{
				lsQry.append(" and DeleteIndi = 0");
			}
			else
			{
				lsQry.append(" and ChngTimestmp > ?");
				lvValues.addElement(
					new DBValue(
						Types.TIMESTAMP,
						DatabaseAccess.convertToString(
							aaSubstationData.getChngTimestmp())));
			}
			lsQry.append(" order by 1,2 ");

			Log.write(Log.SQL, this, " - qrySubstation - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qrySubstation - SQL - End");

			while (lrsQry.next())
			{
				SubstationData laSubstationData = new SubstationData();
				laSubstationData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laSubstationData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laSubstationData.setSubstaName(
					caDA.getStringFromDB(lrsQry, "SubstaName"));
				laSubstationData.setSubstaSt(
					caDA.getStringFromDB(lrsQry, "SubstaSt"));
				laSubstationData.setSubstaCity(
					caDA.getStringFromDB(lrsQry, "SubstaCity"));
				laSubstationData.setSubstaZpCd(
					caDA.getStringFromDB(lrsQry, "SubstaZpCd"));
				laSubstationData.setSubstaZpCdP4(
					caDA.getStringFromDB(lrsQry, "SubstaZpCdP4"));
				laSubstationData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				laSubstationData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laSubstationData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qrySubstation - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySubstation - Exception " + leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to Query RTS.RTS_SUBSTA for SubstaName
	 * 
	 * @param  aiOfficeId int
	 * @param  aiSubstationId int
	 * @return String
	 * @throws RTSException
	 */
	public String qrySubstationId(int aiOfficeId, int aiSubstationId)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qrySubstationId - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		Vector lvValues = new Vector();

		String lsName = "";

		lsQry.append(
			"SELECT SubstaName "
				+ " FROM RTS.RTS_SUBSTA where OfcIssuanceNo = ? and "
				+ " SubstaId = ? ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiOfficeId)));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiSubstationId)));

			Log.write(
				Log.SQL,
				this,
				" - qrySubstationId - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qrySubstationId - SQL - End");

			while (lrsQry.next())
			{
				lsName = caDA.getStringFromDB(lrsQry, "SubstaName");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qrySubstationId - End ");
			return (lsName);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySubstationId - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
