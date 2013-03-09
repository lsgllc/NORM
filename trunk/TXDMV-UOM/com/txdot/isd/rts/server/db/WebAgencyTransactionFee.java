package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.FeesData;
import com.txdot.isd.rts.services.data.WebAgencyTransactionFeeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebAgencyTransactionFee.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/10/2011	Created 
 * 							defect 10708 Ver 6.7.0 
 * Ray Rowehl	02/15/2011	Corrected data types on insert.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/20/2011	Add method to get Fee Totals for a Batch.
 * 							add qryWebAgencyTransactionFeeTot()
 * 							defect 10673 Ver 6.7.0
 * K Harrell	03/22/2011	modify insWebAgencyTransactionFee() 
 * 							defect 10768 Ver 6.7.1 
 * ---------------------------------------------------------------------
 */

/**
 * Provides methods to access RTS_WEB_AGNCY_TRANS_FEE
 *
 * @version	6.7.1			03/22/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/10/2011 13:08:17 
 */

public class WebAgencyTransactionFee
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgencyTransactionFee Constructor
	 * 
	 */
	public WebAgencyTransactionFee(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_WEB_AGNCY_TRANS_FEE
	 *
	 * @param aaFeesData FeesData
	 * @param aiSavReqId int  
	 * @throws RTSException 
	 */
	public void insWebAgencyTransactionFee(
		FeesData aaFeesData,
		int aiSavReqId)
		throws RTSException
	{
		csMethod = "insWebAgencyTransactionFee";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_WEB_AGNCY_TRANS_FEE("
				+ "AcctItmCd,"
				+ "ItmPrice,"
				+ "SavReqId )"
				+ " VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ? )";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaFeesData.getAcctItmCd())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaFeesData.getItemPrice())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiSavReqId)));

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
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_WEB_AGNCY_TRANS_FEE
	 * 
	 * @return int
	 * @throws RTSException  
	 */
	public int purgeWebAgencyTransactionFee() throws RTSException
	{
		csMethod = "purgeWebAgencyTransactionFee";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_WEB_AGNCY_TRANS_FEE A WHERE NOT EXISTS( "
				+ " Select * from RTS.RTS_WEB_AGNCY_TRANS B WHERE "
				+ " A.SAVREQID = B.SAVREQID)";

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + "  - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query RTS.RTS_WEB_AGENCY_TRANS_FEES
	 * 
	 * @param  aiSavReqId 	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryWebAgencyTransactionFee(int aiSavReqId)
		throws RTSException
	{
		csMethod = "qryWebAgencyTransactionFee";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "AgncyTransFeeIdntyNo,"
				+ "AcctItmCd,"
				+ "ItmPrice,"
				+ "SavReqId "
				+ "FROM RTS.RTS_WEB_AGNCY_TRANS_FEE WHERE "
				+ " SavReqId  = ? "
				+ " Order by AgncyTransFeeIdntyNo ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiSavReqId)));

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgencyTransactionFeeData laData =
					new WebAgencyTransactionFeeData();

				laData.setAgncyTransFeeIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyTransFeeIdntyNo"));

				laData.setAcctItmCd(
					caDA.getStringFromDB(lrsQry, "AcctItmCd"));

				laData.setItmPrice(
					caDA.getDollarFromDB(lrsQry, "ItmPrice"));

				laData.setSavReqId(
					caDA.getIntFromDB(lrsQry, "SavReqId"));

				// Add element to the Vector
				lvRslt.addElement(laData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (lvRslt);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + "  - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query RTS.RTS_WEB_AGENCY_TRANS_FEES to get the Total Fees.
	 * 
	 * @param  aiBatchIdntyNo 	
	 * @return double
	 * @throws RTSException 
	 */
	public double qryWebAgencyTransactionFeeTot(int aiBatchIdntyNo)
		throws RTSException
	{
		csMethod = "qryWebAgencyTransactionFeeTot";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		double ldTotalAmt = 0.00;

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "SUM(ItmPrice) as TotalAmt "
				+ "FROM RTS.RTS_WEB_AGNCY_TRANS_FEE A, " 				
				+ "RTS.RTS_WEB_AGNCY_TRANS B " 
				+ "WHERE A.SavReqId = B.SavReqId AND "
				+ "B.AGNCYBATCHIDNTYNO = ? AND "
				+ "ACCPTVEHINDI = 1 AND " 
				+ "AGNCYVOIDINDI = 0 AND "
				+ "CNTYVOIDINDI = 0");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiBatchIdntyNo)));

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				// There should only be one row.
				ldTotalAmt = caDA.getDoubleFromDB(lrsQry, "TotalAmt");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return ldTotalAmt;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + "  - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
}
