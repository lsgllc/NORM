package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * InternetSummary.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K. Harrell   07/20/2003  Created
 * 							defect 6271
 * Kathy&Bob    08/01/2003  continued defect 6271 work
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3
 * K Harrell 	09/29/2005	Moved from webapps.db 
 * 							defect 7889 Ver 5.2.3
 * K Harrell	11/13/2005	Streamline calls for Interent Purge
 * 							add purgeItrntSummary() 
 * 							defect 8385 Ver 5.2.3  
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeItrntSummary()
 * 							defect 9825 Ver Defect_POS_D    
 * K Harrell	09/15/2009	Update for SQL Logging 
 * 							defect 10164 Ver Defect_POS_F   
 *----------------------------------------------------------------------
 */

/**
 * This method contains methods to interact with database 
 *  
 * @version	Defect_POS_F 	09/15/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		07/20/2003 17:41:41
 */
public class InternetSummary
{
	String csMethod = new String();

	DatabaseAccess caDA;
	/**
	 * InternetSummary constructor comment.
	 */
	public InternetSummary() throws RTSException
	{
		caDA = new DatabaseAccess();
	}
	/**
	 * InternetSummary constructor comment.
	 * 
	 * @param DatabaseAccess aaDA 
	 * @throws RTSException
	 */
	public InternetSummary(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Purge from RTS_ITRNT_SUMMARY
	 *  
	 * @param aiMonths int
	 * @return int 
	 * @throws RTSException 
	 */
	public int purgeItrntSummary(int aiMonths) throws RTSException
	{
		csMethod = "purgeItrntSummary()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		int liCurrentYr = RTSDate.getCurrentDate().getYear();
		int liCurrentMo = RTSDate.getCurrentDate().getMonth();
		int liCurrentMonths = liCurrentYr * 12 + liCurrentMo;
		// 
		int liDelYr = (int) (liCurrentMonths - aiMonths) / 12;
		int liDelMo = (liCurrentMonths - aiMonths) % 12;
		int liDelYrMo = liDelYr * 100 + liDelMo;

		String sqlDelData =
			"delete from rts.rts_itrnt_summary where TransYrMo < ?";

		Vector lvValues = new Vector();

		try
		{
			lvValues.add(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(liDelYrMo)));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(sqlDelData, lvValues);
				
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
			
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}
	/**
	* Update Internet Summary
	*  
	* @param asTransCd String
	* @param aiOfcissuanceNo int 
	* @throws RTSException
	*/
	public void updItrntSummary(String asTransCd, int aiOfcissuanceNo)
		throws RTSException
	{
		csMethod = "updItrntSummary()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();
		try
		{
			int liYear = RTSDate.getCurrentDate().getYear();
			int liMonth = RTSDate.getCurrentDate().getMonth();
			int liTransYrMo = liYear * 100 + liMonth;

			String lsUpd =
				"Update RTS.RTS_ITRNT_SUMMARY set TransCount = "
					+ "TransCount + 1 where OfcIssuanceNo = ? and TransCd"
					+ " = ? and TransYrMo = ?";

			String lsIns =
				"INSERT into RTS.RTS_ITRNT_SUMMARY("
					+ "OfcIssuanceNo,"
					+ "TransCd,"
					+ "TransYrMo,"
					+ "TransCount) "
					+ "values (?,?,?,1)";

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					String.valueOf(aiOfcissuanceNo)));
			lvValues.addElement(new DBValue(Types.CHAR, asTransCd));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					String.valueOf(liTransYrMo)));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			// First, try to Update 
			int liNumrows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			// If unable to Update, then Insert; 
			if (liNumrows == 0)
			{
				caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			}
			
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	} //END OF UPDATE METHOD
}
