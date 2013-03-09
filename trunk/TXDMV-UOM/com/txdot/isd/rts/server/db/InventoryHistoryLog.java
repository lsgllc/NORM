package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.InventoryHistoryLogData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * InventoryHistoryLog.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks      07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * Min Wang	   	01/17/2003	Modified updInventoryHistoryLog() to only 
 *							update selected ofcissuanceNo.Defect 
 *							defect 5111
 * K Harrell	06/04/2004	Modify SQL. Concatenate LastInsertDate in
 *							update string 
 *							modify updInventoryHistoryLog()
 *							defect 7104   Ver 5.2.0
 * K Harrell	03/03/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	10/12/2009	add calling Regional Office
 * 							modify qryInventoryHistoryLog() 
 * 							defect 10207 Ver Defect_POS_G   
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to interact with database
 *
 * @version	Defect_POS_G	10/12/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		09/17/2001	16:40:26
 */

public class InventoryHistoryLog extends InventoryHistoryLogData
{
	DatabaseAccess caDA;
	/**
	 * InventoryHistoryLog constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */

	public InventoryHistoryLog(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_INV_HSTRY_LOG join to RTS.RTS_OFFICE_IDS
	 * 
	 * @param  aaInventoryHistoryLogData  InventoryHistoryLogData	
	 * @return Vector
	 * @throws RTSException 
	 */

	public Vector qryInventoryHistoryLog(InventoryHistoryLogData aaInventoryHistoryLogData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryHistoryLog - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// defect 10207 
		lsQry.append(
			"SELECT "
				+ " A.OfcIssuanceNo,OFCNAME,"
				+ " LastInsertDate "
				+ " FROM RTS.RTS_INV_HSTRY_LOG A, "
				+ " RTS.RTS_OFFICE_IDS B  where "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " B.RegnlOfcId = ? "
				+ " UNION "
				+ " SELECT "
				+ " A.OfcIssuanceNo,OFCNAME,"
				+ " LastInsertDate "
				+ " FROM RTS.RTS_INV_HSTRY_LOG A, "
				+ " RTS.RTS_OFFICE_IDS B  where "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " B.OFCISSUANCENO = ? "
				+ " ORDER BY 1 ");
		try
		{
			for (int i = 0; i < 2; i++)
			{
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryHistoryLogData
								.getOfcIssuanceNo())));
			}
			// end defect 10207 

			Log.write(
				Log.SQL,
				this,
				" - qryInventoryHistoryLog - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryHistoryLog - SQL - End");

			while (lrsQry.next())
			{
				InventoryHistoryLogData laInventoryHistoryLogData =
					new InventoryHistoryLogData();
				laInventoryHistoryLogData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laInventoryHistoryLogData.setOfcName(
					caDA.getStringFromDB(lrsQry, "OfcName"));
				laInventoryHistoryLogData.setLastInsertDate(
					caDA.getRTSDateFromDB(lrsQry, "LastInsertDate"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryHistoryLogData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryHistoryLog - End ");
			return (lvRslt);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryHistoryLog - Exception "
					+ aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryHistoryLog - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to update RTS.RTS_INV_HSTRY_LOG
	 * 
	 * @param  aaInventoryHistoryLogData  InventoryHistoryLogData	
	 * @throws RTSException 
	 */
	public void updInventoryHistoryLog(InventoryHistoryLogData aaInventoryHistoryLogData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updInventoryHistoryLog - Begin");

		Vector lvValues = new Vector();

		// Add where clause to just update the selected office
		// defect 7104
		// concatenate LastInsertDate() into update statement 
		String lsUpd =
			"UPDATE RTS.RTS_INV_HSTRY_LOG SET LastInsertDate = '"
				+ aaInventoryHistoryLogData.getLastInsertDate()
				+ "' WHERE OFCISSUANCENO = ?";
		try
		{
			//lvValues.addElement(
			//    new DBValue(
			//        Types.TIMESTAMP,
			//        DatabaseAccess.convertToString(aaInventoryHistoryLogData.getLastInsertDate())));
			// end defect 7104
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaInventoryHistoryLogData.getOfcIssuanceNo())));

			Log.write(
				Log.SQL,
				this,
				"updInventoryHistoryLog - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(
				Log.SQL,
				this,
				"updInventoryHistoryLog - SQL - End");
			Log.write(Log.METHOD, this, "updInventoryHistoryLog - End");
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInventoryHistoryLog - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
