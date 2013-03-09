package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.VehicleBaseData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * InternetTransactionDeleteLog.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/03/2010	Created
 * 							defect 10421 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_ITRNT_TRANS_DEL_LOG   
 *
 * @version	POS_640			04/03/2010
 * @author	Kathy Harrell 
 * <br>Creation Date:		04/03/2010 11:19:17
 */
public class InternetTransactionDeleteLog
{
	private String csMethod = new String();

	private DatabaseAccess caDA;

	/**
	 * InternetTransactionDeleteLog constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public InternetTransactionDeleteLog(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Insert a record into RTS_ITRNT_TRANS_DEL_LOG for Declined,  
	 * Refund Approved (prior to deletion from RTS_ITRNT_TRANS) 
	 * 
	 * @param aaVehBaseData
	 * @return int 
	 * @throws RTSException
	 */
	public int insItrntTransDelLog(VehicleBaseData aaVehBaseData)
		throws RTSException
	{
		csMethod = " - insItrntTransDelLog";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		int liNumRows = 0;

		Vector lvValues = new Vector();

		String lsSQLIns =
			"INSERT INTO RTS.RTS_ITRNT_TRANS_DEL_LOG ("
				+ "TRANSCD,"
				+ "REGPLTNO,"
				+ "VIN,"
				+ "DOCNO,"
				+ "OFCISSUANCENO,"
				+ "ITRNTTIMESTMP,"
				+ "CNTYSTATUSCD,"
				+ "ITRNTTRACENO,"
				+ "SUBSTAID,"
				+ "TRANSWSID,"
				+ "TRANSAMDATE,"
				+ "TRANSTIME,"
				+ "ITRNTPYMNTSTATUSCD,"
				+ "PYMNTORDERID,"
				+ "PYMNTAMT,"
				+ "CNVNCFEE,"
				+ "INSCMPNYNAME,"
				+ "INSPLCYNO,"
				+ "INSAGENTNAME,"
				+ "INSPHONENO,"
				+ "INSPLCYSTARTDATE,"
				+ "INSPLCYENDDATE,"
				+ "CNTYREASNCD,"
				+ "CNTYREASNTXT,"
				+ "CNTYPRCSDTIMESTMP,"
				+ "SENDEMAILINDI,"
				+ "REGEXPMO,"
				+ "REGEXPYR,"
				+ "REGPLTAGE,"
				+ "VEHGROSSWT,"
				+ "VEHEMPTYWT,"
				+ "VEHTON,"
				+ "OWNRTTLNAME1,"
				+ "OWNRTTLNAME2,"
				+ "TTLISSUEDATE,"
				+ "FRSTNAME,"
				+ "MIDLNAME,"
				+ "LASTNAME,"
				+ "EMAILADDR,"
				+ "RECPNTNAME,"
				+ "RENWLMAILNGST1,"
				+ "RENWLMAILNGST2,"
				+ "RENWLMAILNGCITY,"
				+ "RENWLMAILNGSTATE,"
				+ "RENWLMAILNGZPCD,"
				+ "RENWLMAILNGZPCD4,"
				+ "CUSTPHONENO,"
				+ "IADDRSPCLPLTINDI,"
				+ "DELTIMESTMP) "
				+ "(SELECT "
				+ "TRANSCD,"
				+ "REGPLTNO,"
				+ "VIN,"
				+ "DOCNO,"
				+ "OFCISSUANCENO,"
				+ "ITRNTTIMESTMP,"
				+ "CNTYSTATUSCD,"
				+ "ITRNTTRACENO,"
				+ "SUBSTAID,"
				+ "TRANSWSID,"
				+ "TRANSAMDATE,"
				+ "TRANSTIME,"
				+ "ITRNTPYMNTSTATUSCD,"
				+ "PYMNTORDERID,"
				+ "PYMNTAMT,"
				+ "CNVNCFEE,"
				+ "INSCMPNYNAME,"
				+ "INSPLCYNO,"
				+ "INSAGENTNAME,"
				+ "INSPHONENO,"
				+ "INSPLCYSTARTDATE,"
				+ "INSPLCYENDDATE,"
				+ "CNTYREASNCD,"
				+ "CNTYREASNTXT,"
				+ "CNTYPRCSDTIMESTMP,"
				+ "SENDEMAILINDI,"
				+ "REGEXPMO,"
				+ "REGEXPYR,"
				+ "REGPLTAGE,"
				+ "VEHGROSSWT,"
				+ "VEHEMPTYWT,"
				+ "VEHTON,"
				+ "OWNRTTLNAME1,"
				+ "OWNRTTLNAME2,"
				+ "TTLISSUEDATE,"
				+ "FRSTNAME,"
				+ "MIDLNAME,"
				+ "LASTNAME,"
				+ "EMAILADDR,"
				+ "RECPNTNAME,"
				+ "RENWLMAILNGST1,"
				+ "RENWLMAILNGST2,"
				+ "RENWLMAILNGCITY,"
				+ "RENWLMAILNGSTATE,"
				+ "RENWLMAILNGZPCD,"
				+ "RENWLMAILNGZPCD4,"
				+ "CUSTPHONENO,"
				+ "IADDRSPCLPLTINDI,"
				+ "CURRENT TIMESTAMP FROM "
				+ "RTS.RTS_ITRNT_TRANS "
				+ "WHERE "
				+ "TRANSCD = 'IRENEW' AND "
				+ "REGPLTNO=? AND "
				+ "VIN=? AND "
				+ "DOCNO=? "
				+ "AND CNTYSTATUSCD IN ("
				+ CommonConstants.DECLINED_REFUND_APPROVED
				+ "))";

		lvValues.add(
			new DBValue(Types.VARCHAR, aaVehBaseData.getPlateNo()));

		lvValues.add(
			new DBValue(Types.VARCHAR, aaVehBaseData.getVin()));

		lvValues.add(
			new DBValue(Types.VARCHAR, aaVehBaseData.getDocNo()));

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			liNumRows =
				caDA.executeDBInsertUpdateDelete(lsSQLIns, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);

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
	} // End INSERT 

	/**
	 * Purge RTS_ITRNT_TRANS_DEL_LOG that are older than the number of days
	 * passed in.
	 * 
	 * @param aiDaysOld int
	 * @return int 
	 * @throws RTSException
	 */
	public int purgeItrntTransDelLog(int aiDaysOld)
		throws RTSException
	{
		csMethod = " - purgeItrntTransDelLog";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSQLDel =
			"delete from rts.rts_itrnt_trans_del_log "
				+ "where CntyPrcsdTimeStmp < (CURRENT TIMESTAMP - ? DAYS)";

		Vector lvValues = new Vector();

		try
		{
			lvValues.add(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiDaysOld)));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsSQLDel, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);

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
	}// End PURGE 
}
