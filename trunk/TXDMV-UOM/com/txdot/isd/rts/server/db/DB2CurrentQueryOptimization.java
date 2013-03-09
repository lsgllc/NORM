package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * DB2CurrentQueryOptimization
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/23/2010	created
 * 							defect 10413 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * Class to manage retrieving/setting DB2 Optimization Level
 *
 * @version	6.4.0 			03/23/2010 
 * @author	Kathy Harrell
 * <br>Creation Date:		03/23/2010  12:35:00
 */
public class DB2CurrentQueryOptimization
{
	DatabaseAccess caDA;

	private String csMethod = new String();

	/**
	 * DB2CurrentQueryOptimization constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public DB2CurrentQueryOptimization(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to set DB2 Current Query Optimization
	 *
	 * @param  aiOptimizationLevel	
	 * @throws RTSException 
	 */
	public void setDB2CurrentQueryOptimization(int aiOptimizationLevel)
		throws RTSException
	{
		csMethod = "setDB2CurrentQueryOptimization";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsSet =
			"Set Current Query Optimization "
				+ aiOptimizationLevel;
		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsSet, new Vector());
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to get DB2 Current Query Optimization 
	 *
	 * @return  int 
	 * @throws 	RTSException 
	 */
	public int getDB2CurrentQueryOptimization() throws RTSException
	{
		csMethod = "getDB2CurrentQueryOptimization";

		ResultSet lrsQry;

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsQry =
			"Select Current Query Optimization as OptLvl from SYSIBM.SYSDUMMY1";

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());
				
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			int liOptLvl = 0;

			while (lrsQry.next())
			{
				liOptLvl = caDA.getIntFromDB(lrsQry, "OptLvl");
			}
			
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return liOptLvl;
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
	} 
}  
