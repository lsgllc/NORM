package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.MiscellaneousData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * LogonFunctionTransaction.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/18/2002	Added update of Admin_Cache
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * Ray Rowehl	02/10/2003	Add false to SupervisorOverrideCode to avoid
 *							trimming.
 *							defect 4735
 * K Harrell	03/03/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3    
 * K Harrell	08/31/2008	Update for consistent RTS_ADMIN_CACHE 
 * 							processing
 * 							modify updMiscellaneous()
 * 							defect 8721 Ver Defect_POS_B                
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_MISC 
 * 
 * @version	Defect_POS_B	08/31/2008
 * @author 	Kathy Harrell
 * <br> Creation Date:		09/06/2001 18:27:28 
 */

public class Miscellaneous extends MiscellaneousData
{
	DatabaseAccess caDA;
	/**
	 * Miscellaneous constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public Miscellaneous(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to determine Current Timestamp
	 *
	 * @param  aiOfcNo		int 
	 * @param  aiSubstaId	int
	 * @return RTSDate
	 * @throws RTSException
	 */
	public RTSDate qryCurrentTimestamp(int aiOfcNo, int aiSubstaId)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryCurrentTimestamp - Begin");

		StringBuffer lsQry = new StringBuffer();

		RTSDate laCurrentTimestamp = new RTSDate();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "Current Timestamp as CurrentTimestamp "
				+ "FROM RTS.RTS_MISC WHERE OfcIssuanceNo = "
				+ aiOfcNo
				+ " and SubstaId = "
				+ aiSubstaId);
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryCurrentTimestamp - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryCurrentTimestamp - SQL - End");

			while (lrsQry.next())
			{
				laCurrentTimestamp =
					caDA.getRTSDateFromDB(lrsQry, "CurrentTimestamp");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryCurrentTimestamp - End ");
			return (laCurrentTimestamp);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" -qryCurrentTimestamp - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to Query RTS.RTS_MISC
	 * 
	 * @param  aaMiscellaneousData MiscellaneousData	
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryMiscellaneous(MiscellaneousData aaMiscellaneousData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryMiscellaneous - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "SupvOvrideCd,"
				+ "ServerPlusIndi,"
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_MISC WHERE OfcIssuanceNo = ? and "
				+ "SubstaId = ? ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMiscellaneousData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMiscellaneousData.getSubstaId())));

			if (aaMiscellaneousData.getChngTimestmp() != null)
			{
				lsQry.append("and ChngTimestmp > ?");
				lvValues.addElement(
					new DBValue(
						Types.TIMESTAMP,
						DatabaseAccess.convertToString(
							aaMiscellaneousData.getChngTimestmp())));
			}
			Log.write(
				Log.SQL,
				this,
				" - qryMiscellaneous - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryMiscellaneous - SQL - End");

			while (lrsQry.next())
			{
				MiscellaneousData laMiscellaneousData =
					new MiscellaneousData();
				laMiscellaneousData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laMiscellaneousData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laMiscellaneousData.setSupvOvrideCd(
					caDA.getStringFromDB(
						lrsQry,
						"SupvOvrideCd",
						false));
				laMiscellaneousData.setServerPlusIndi(
					caDA.getIntFromDB(lrsQry, "ServerPlusIndi"));
				laMiscellaneousData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laMiscellaneousData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryMiscellaneous - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryMiscellaneous - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
	/**
	 * Method to update RTS.RTS_MISC
	 * 
	 * @param  aaMiscellaneousData MiscellaneousData
	 * @throws RTSException 
	 */
	public void updMiscellaneous(MiscellaneousData aaMiscellaneousData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updMiscellaneous - Begin");

		Vector lvValues = new Vector();

		Vector lvValues1 = new Vector();

		String lsUpd1 = "UPDATE RTS.RTS_MISC SET ";
		String lsUpd2 =
			"ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? ";

		// defect 8721 
		//
		//		String lsUpd3 =
		//			"Update RTS.RTS_ADMIN_CACHE set ChngTimestmp = "
		//				+ "CURRENT TIMESTAMP "
		//				+ "WHERE "
		//				+ "CacheTblName = 'RTS_MISC' and "
		//				+ "OfcIssuanceNo = ? AND "
		//				+ "SubstaId = ? ";
		// end defect 8721 
		try
		{
			if (aaMiscellaneousData.getSupvOvrideCd() != null)
			{
				lsUpd1 = lsUpd1 + "SupvOvrideCd = ?,";
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaMiscellaneousData.getSupvOvrideCd(),
							false)));
			}
			else
			{
				lsUpd1 = lsUpd1 + "ServerPlusIndi = ?, ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaMiscellaneousData.getServerPlusIndi())));
			}
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMiscellaneousData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMiscellaneousData.getSubstaId())));
			lvValues1.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMiscellaneousData.getOfcIssuanceNo())));
			lvValues1.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMiscellaneousData.getSubstaId())));

			String lsUpd = lsUpd1 + lsUpd2;

			Log.write(Log.SQL, this, "updMiscellaneous - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updMiscellaneous - SQL - End");

			Log.write(Log.METHOD, this, "updMiscellaneous - End");

			// defect 8721 
			//			Log.write(
			//				Log.SQL,
			//				this,
			//				"updAdminCache for updMiscellaneous - SQL - Begin");
			//			caDA.executeDBInsertUpdateDelete(lsUpd3, lvValues1);
			//			Log.write(
			//				Log.SQL,
			//				this,
			//				"updAdminCache for updMiscellaneous - SQL - End");
			//			Log.write(
			//				Log.METHOD,
			//				this,
			//				"updAdminCache for updMiscellaneous - End");
			// end defect 8721 
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updMiscellaneous - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS 