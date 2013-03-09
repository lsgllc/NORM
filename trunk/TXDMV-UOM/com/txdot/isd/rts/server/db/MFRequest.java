package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.MFRequestData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * MFRequest.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/29/2010	Created
 * 							defect 10462 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_MF_REQ
 *
 * @version	6.5.0			07/29/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		07/29/2010	18:45:17
 */
public class MFRequest
{
	private DatabaseAccess caDA;

	private String csMethod;

	/**
	 * MFRequest.java Constructor
	 * 
	 */
	public MFRequest(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_MF_REQ
	 *
	 * @param  aaMFReqData		
	 * @throws RTSException 
	 */
	public void insMFRequest(MFRequestData aaMFReqData)
		throws RTSException
	{
		csMethod = "insMFRequest";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");
		String lsReqDate =
			"'" + aaMFReqData.getReqTimeStmp().getDB2Date() + "'";
		String lsRespDate =
			"'" + aaMFReqData.getRespTimestmp().getDB2Date() + "'";
		RTSDate laDateParm1 = aaMFReqData.getDateParm1();
		RTSDate laDateParm2 = aaMFReqData.getDateParm2();
		String lsDateParm1 =
			laDateParm1 == null ? null : laDateParm1.toString();
		String lsDateParm2 =
			laDateParm2 == null ? null : laDateParm2.toString();

		String lsIns =
			"INSERT into RTS.RTS_MF_REQ("
				+ "CICSTRANSID,"
				+ "REQKEY,"
				+ "PARM1,"
				+ "DATEPARM1,"
				+ "DATEPARM2,"
				+ "TIERCD,"
				+ "RETRYNO,"
				+ "SUCCESSFULINDI,"
				+ "ERRMSGCD,"
				+ "REQDATE,"
				+ "REQTIMESTMP,"
				+ "RESPTIMESTMP)"
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
				+ lsReqDate
				+ ","
				+ lsRespDate
				+ ")";

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMFReqData.getCICSTransId())));

			// 2
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMFReqData.getReqKey())));

			// 3
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMFReqData.getParm1())));
			// 4
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(lsDateParm1)));
			// 5
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(lsDateParm2)));

			// 6
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaMFReqData.getTierCd())));

			// 7
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaMFReqData.getRetryNo())));

			// 8
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaMFReqData.getSuccessfulIndi())));

			// 9
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaMFReqData.getErrMsgCd())));

			// 10
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMFReqData.getReqTimeStmp().toString())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
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

	} // END INSERT METHOD 

	/**
	 * Method to move prior day's requests to RTS.RTS_MF_REQ_HSTRY
	 *  then delete same from RTS.RTS_MF_REQ
	 * 
	 * Using "<=" in case miss day(s) of batch. 
	 *
	 * @return int 	
	 * @throws RTSException 
	 */
	public int purgeMFReq() throws RTSException
	{
		csMethod = "purgeMFReq";
		Log.write(Log.METHOD, this, csMethod + " - Begin");
		Vector lvValues = new Vector();

		String lsStmt1 =
			"INSERT INTO RTS.RTS_MF_REQ_HSTRY ("
				+ " SELECT * FROM RTS.RTS_MF_REQ "
				+ " WHERE REQDATE <= CURRENT DATE - 1 DAY)";
		String lsStmt2 =
			"DELETE FROM RTS.RTS_MF_REQ WHERE REQDATE <= "
				+ " CURRENT DATE - 1 DAY";

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsStmt1, lvValues);
			caDA.executeDBInsertUpdateDelete(lsStmt2, lvValues);

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
	} //END OF Delete METHOD
}
