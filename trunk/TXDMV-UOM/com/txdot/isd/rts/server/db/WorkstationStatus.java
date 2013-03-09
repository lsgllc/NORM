package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WorkstationStatusData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * WorkstationStatus.java
 *
 * (c) Texas Department of Transportation 2004.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/05/2004	Created Class - This updates the
 *							RTS_WS_Status table with the workstation
 *							status such as version and version date.
 *							defect 6918 Ver 5.1.6
 * K Harrell	03/18/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3 
 * K Harrell	08/12/2009	Work for new DB2 Driver
 * 							modify updWorkstationStatus()
 * 							defect 10164 Ver Defect_POS_E'
 * K Harrell	04/03/2010	add csMethod, implemented CommonConstant
 * 							add insWorkstationStatus(), 
 * 							 qryWorkstationStatus(), 
 * 							 insWorkstationStatusLog() 
 * 							modify updWorkstationStatus() 
 * 							defect 8087 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This class inserts record if does not already exist, inserts into 
 * RTS_WS_STATUS_LOG if any configuration data change prior to update 
 * and updates LastRestartTstmp if no Configuration Change.
 * 
 * Note:  ChngTimestmp refers to Config Data Change Timestamp as 
 *        LastRestartTstmp is updated with each restart.   
 *  
 * @version	 POS_640		04/03/2010
 * @author  Kathy Harrell
 * @author	Jeff Seifert
 * <b>Creation Date:		03/05/2004 15:18:33
 */

public class WorkstationStatus extends WorkstationStatusData
{
	private DatabaseAccess caDA;

	// defect 8087 
	private String csMethod = new String();
	// end defect 8087 

	/**
	 * WorkstationStatus constructor comment.
	 */
	public WorkstationStatus()
	{
		super();
	}

	/**
	 * WorkstationStatus constructor passing DatabaseAccess.
	 *
	 * @param aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public WorkstationStatus(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_WS_STATUS
	 * 
	 * @param  aaWksStatusData 		
	 * @throws RTSException 	
	 */
	public void insWorkstationStatus(WorkstationStatusData aaWksStatusData)
		throws RTSException
	{
		csMethod = " - insWorkstationStatus";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT INTO RTS.RTS_WS_STATUS( "
				+ "OFCISSUANCENO, "
				+ "SUBSTAID,"
				+ "WSID,"
				+ "RTSVersion, "
				+ "RTSVersionDate,  "
				+ "CPName ,"
				+ "CDSrvrCPName ,"
				+ "CDSrvrIndi,"
				+ "JarSize ,"
				+ "ServletHost,"
				+ "ServletPort , "
				+ "LastRestartTstmp,"
				+ "Chngtimestmp) "
				+ " VALUES ("
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
				+ " ? )";

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWksStatusData.getOfcIssuanceNo())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWksStatusData.getSubStaId())));
			// 3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWksStatusData.getWSid())));
			// 4 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWksStatusData.getRTSVersion())));
			// 5 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWksStatusData
							.getRTSVersionRTSDate()
							.toString())));
			// 6 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWksStatusData.getCPName())));
			// 7 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWksStatusData.getCDSrvrCPName())));

			// 8
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWksStatusData.isCDSrvr() ? 1 : 0)));
			// 9
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWksStatusData.getJarSize())));
			// 10
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWksStatusData.getServletHost())));
			// 11
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWksStatusData.getServletPort())));
			// 12 
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaWksStatusData.getLastRestartTstmp())));
			//	13
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaWksStatusData.getLastRestartTstmp())));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			Log.write(Log.METHOD, this, CommonConstant.SQL_METHOD_END);
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
	} //END OF INSERT METHOD

	/** 
	 * Query RTS_WS_STATUS 
	 * 
	 * @param aaWksStatusData
	 * @return WorkstationStatusData 
	 * @throws RTSException
	 */
	public WorkstationStatusData qryWorkstationStatus(WorkstationStatusData aaWksStatusData)
		throws RTSException
	{
		csMethod = " - qryWorkstationStatus";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		StringBuffer lsQry = new StringBuffer();

		WorkstationStatusData laDBWksStatusData = null;

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "WsId,"
				+ "RTSVersion,"
				+ "RTSVersionDate,"
				+ "CPName,"
				+ "CDSrvrCPName,"
				+ "CDSrvrIndi,"
				+ "JarSize,"
				+ "ServletHost,"
				+ "ServletPort, "
				+ "LastRestartTstmp "
				+ "FROM RTS.RTS_WS_STATUS "
				+ "Where "
				+ "OfcIssuanceNo = ? "
				+ "and SubstaId = ? "
				+ "and WsId = ? ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWksStatusData.getOfcIssuanceNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWksStatusData.getSubStaId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWksStatusData.getWSid())));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (lrsQry.next())
			{
				laDBWksStatusData = new WorkstationStatusData();
				laDBWksStatusData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laDBWksStatusData.setSubStaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laDBWksStatusData.setWSid(
					caDA.getIntFromDB(lrsQry, "WsId"));
				laDBWksStatusData.setRTSVersion(
					caDA.getStringFromDB(lrsQry, "RTSVersion"));
				RTSDate laVersionDate =
					caDA.getRTSDateFromDB(lrsQry, "RTSVersionDate");
				laDBWksStatusData.setRTSVersionDate(
					laVersionDate.toString());
				laDBWksStatusData.setCPName(
					caDA.getStringFromDB(lrsQry, "CPName"));
				laDBWksStatusData.setCDSrvrCPName(
					caDA.getStringFromDB(lrsQry, "CDSrvrCPName"));
				laDBWksStatusData.setJarSize(
					caDA.getLongFromDB(lrsQry, "JarSize"));
				laDBWksStatusData.setServletHost(
					caDA.getStringFromDB(lrsQry, "ServletHost"));
				laDBWksStatusData.setServletPort(
					caDA.getIntFromDB(lrsQry, "ServletPort"));
				int liCDSrvrIndi =
					caDA.getIntFromDB(lrsQry, "CDSrvrIndi");
				laDBWksStatusData.setCDSrvr(liCDSrvrIndi == 1);
				break;
			} //End of While
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, CommonConstant.SQL_METHOD_END);
			return laDBWksStatusData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
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
	} //END OF QUERY METHOD 	

	/**
	 * Method to update a RTS.RTS_WS_Status for a given workstation
	 * Only do the update if the RTSVersion or RTSVersioDate is null
	 * or different than the current DB values.
	 *
	 * @param  aaWorkstationStatusData	WorkstationStatusData	
	 * @return Boolean 
	 * @throws RTSException 	
	 */
	public Boolean updWorkstationStatus(WorkstationStatusData aaWksStatusData)
		throws RTSException
	{
		// defect 8087 
		csMethod = " - updWorkstationStatus";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		boolean lbConfigChange = true;

		String lsUpd = new String();
		Vector lvValues = new Vector();

		// Query existing Data 
		WorkstationStatusData laDBWksData =
			qryWorkstationStatus(aaWksStatusData);

		// Insert if New Record 
		if (laDBWksData == null)
		{
			insWorkstationStatus(aaWksStatusData);
		} // New Record 
		else
		{
			lbConfigChange =
				laDBWksData.compareTo(aaWksStatusData) != 0;

			try
			{
				if (lbConfigChange)
				{
					// Copy existing record to RTS_WS_STATUS_LOG
					WorkstationStatusLog laWsStatusLog =
						new WorkstationStatusLog(caDA);

					laWsStatusLog.insWorkstationStatusLog(laDBWksData);

					// Update Current Record 
					lsUpd =
						"UPDATE RTS.RTS_WS_STATUS set "
							+ "RTSVersion = ?, "
							+ "RTSVersionDate = ?, "
							+ "CPName = ?,"
							+ "CDSrvrCPName = ?,"
							+ "CDSrvrIndi = ?,"
							+ "JarSize = ?,"
							+ "ServletHost = ?,"
							+ "ServletPort =? , "
							+ "LastRestartTstmp = ?,"
							+ "Chngtimestmp = ? "
							+ "WHERE OfcIssuanceno = ? AND "
							+ "SubStaId = ? AND "
							+ "WsId = ?";

					// 1 
					lvValues.addElement(
						new DBValue(
							Types.CHAR,
							DatabaseAccess.convertToString(
								aaWksStatusData.getRTSVersion())));
					// 2 
					lvValues.addElement(
						new DBValue(
							Types.CHAR,
							DatabaseAccess.convertToString(
								aaWksStatusData
									.getRTSVersionRTSDate()
									.toString())));
					// 3 
					lvValues.addElement(
						new DBValue(
							Types.CHAR,
							DatabaseAccess.convertToString(
								aaWksStatusData.getCPName())));
					// 4 
					lvValues.addElement(
						new DBValue(
							Types.CHAR,
							DatabaseAccess.convertToString(
								aaWksStatusData.getCDSrvrCPName())));

					// 5 
					lvValues.addElement(
						new DBValue(
							Types.INTEGER,
							DatabaseAccess.convertToString(
								aaWksStatusData.isCDSrvr() ? 1 : 0)));
					// 6
					lvValues.addElement(
						new DBValue(
							Types.INTEGER,
							DatabaseAccess.convertToString(
								aaWksStatusData.getJarSize())));
					// 7
					lvValues.addElement(
						new DBValue(
							Types.CHAR,
							DatabaseAccess.convertToString(
								aaWksStatusData.getServletHost())));
					// 8
					lvValues.addElement(
						new DBValue(
							Types.INTEGER,
							DatabaseAccess.convertToString(
								aaWksStatusData.getServletPort())));
					// 9 
					lvValues.addElement(
						new DBValue(
							Types.TIMESTAMP,
							DatabaseAccess.convertToString(
								aaWksStatusData
									.getLastRestartTstmp())));
					//	10 
					lvValues.addElement(
						new DBValue(
							Types.TIMESTAMP,
							DatabaseAccess.convertToString(
								aaWksStatusData
									.getLastRestartTstmp())));
					// 11
					lvValues.addElement(
						new DBValue(
							Types.INTEGER,
							DatabaseAccess.convertToString(
								aaWksStatusData.getOfcIssuanceNo())));
					// 12
					lvValues.addElement(
						new DBValue(
							Types.INTEGER,
							DatabaseAccess.convertToString(
								aaWksStatusData.getSubStaId())));
					// 13 
					lvValues.addElement(
						new DBValue(
							Types.INTEGER,
							DatabaseAccess.convertToString(
								aaWksStatusData.getWSid())));
				}
				else
				{
					lsUpd =
						"Update RTS.RTS_WS_STATUS set "
							+ "LastRestartTstmp = ? "
							+ "WHERE OfcIssuanceno = ? AND "
							+ "SubStaId = ? AND "
							+ "WsId = ? ";

					lvValues = new Vector();

					// 1
					lvValues.addElement(
						new DBValue(
							Types.TIMESTAMP,
							DatabaseAccess.convertToString(
								aaWksStatusData
									.getLastRestartTstmp())));
					// 2
					lvValues.addElement(
						new DBValue(
							Types.INTEGER,
							DatabaseAccess.convertToString(
								aaWksStatusData.getOfcIssuanceNo())));
					// 3 
					lvValues.addElement(
						new DBValue(
							Types.INTEGER,
							DatabaseAccess.convertToString(
								aaWksStatusData.getSubStaId())));
					// 4 
					lvValues.addElement(
						new DBValue(
							Types.INTEGER,
							DatabaseAccess.convertToString(
								aaWksStatusData.getWSid())));
				}

				Log.write(
					Log.SQL,
					this,
					csMethod + CommonConstant.SQL_BEGIN);

				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

				Log.write(
					Log.SQL,
					this,
					csMethod + CommonConstant.SQL_END);

				Log.write(
					Log.METHOD,
					this,
					csMethod + CommonConstant.SQL_METHOD_END);
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
			catch (Exception aeEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					csMethod
						+ CommonConstant.SQL_EXCEPTION
						+ aeEx.getMessage());
				throw new RTSException(RTSException.SYSTEM_ERROR, aeEx);
			}
		} // Existing Record	   
		return new Boolean(lbConfigChange);
		// end defect 8087 
	} // END UPDATE METHOD 
}
