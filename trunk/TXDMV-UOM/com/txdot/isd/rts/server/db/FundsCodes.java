package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.FundsCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * FundsCodes.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/27/2008	Created
 * 							defect 6949 Ver Defect POS A 	 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS.RTS_FUNDS_CODES 
 *
 * @version	Defect POS A	03/27/2008
 * @author	K Harrell
 * <br>Creation Date:		03/27/2008  10:18:00 
 */
public class FundsCodes extends FundsCodesData
{
	DatabaseAccess caDA;

	/**
	 * FundsCodes constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public FundsCodes(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_FUNDS_CODES 
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryFundsCodes() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryFundsCodes - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "FUNDSCAT, "
				+ "FUNDSRCVNGENT, "
				+ "ENTPRCNT, "
				+ "COMMSNREDUCTNAMT, "
				+ "FEEDEFRMNTCD, "
				+ "FUNDSCATTYPE, "
				+ "FUNDSCATDESC, "
				+ "ENTGRPID, "
				+ "HOTCKSTATCAT, "
				+ "CRDTAPPRHNDCNTYINDI "
				+ "FROM RTS.RTS_FUNDS_CODES ");

		try
		{

			Log.write(Log.SQL, this, " - qryFundsCodes - SQL - Begin");

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			Log.write(Log.SQL, this, " - qryFundsCodes - SQL - End");

			while (lrsQry.next())
			{
				FundsCodesData laFundsCodesData = new FundsCodesData();

				laFundsCodesData.setFundsCat(
					caDA.getStringFromDB(lrsQry, "FundsCat"));

				laFundsCodesData.setFundsRcvngEnt(
					caDA.getStringFromDB(lrsQry, "FundsRcvngEnt"));

				laFundsCodesData.setEntPrcnt(
					caDA.getDoubleFromDB(lrsQry, "EntPrcnt"));

				laFundsCodesData.setCommsnReductnAmt(
					caDA.getDollarFromDB(lrsQry, "CommsnReductnAmt"));

				laFundsCodesData.setFeeDefrmntCd(
					caDA.getStringFromDB(lrsQry, "FeeDefrmntCd"));

				laFundsCodesData.setFundsCatType(
					caDA.getStringFromDB(lrsQry, "FundsCatType"));

				laFundsCodesData.setFundsCatDesc(
					caDA.getStringFromDB(lrsQry, "FundsCatDesc"));

				laFundsCodesData.setEntGrpId(
					caDA.getIntFromDB(lrsQry, "EntGrpId"));

				laFundsCodesData.setHotCkStatCat(
					caDA.getStringFromDB(lrsQry, "HotCkStatCat"));

				laFundsCodesData.setCrdtApprhndCntyIndi(
					caDA.getIntFromDB(lrsQry, "CrdtApprhndCntyIndi"));

				// Add element to the Vector
				lvRslt.addElement(laFundsCodesData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryFundsCodes - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryFundsCodes - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}

	} //END OF QUERY METHOD
}
