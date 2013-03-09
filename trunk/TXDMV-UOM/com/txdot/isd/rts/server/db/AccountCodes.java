package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.AccountCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * AccountCodes.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	04/04/2005	Remove parameter from qryAccountCodes()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	05/11/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3 
 * K Harrell	01/19/2011	add BaseFee 
 * 							modify qryAccountCodes()
 * 							defect 10741 Ver 6.7.0                 	  
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_ACCOUNT_CODES
 *
 * @version	6.7.0 		01/19/2011
 * @author	Kathy Harrell
 * <br>Creation Date:	08/30/2001 15:57:59
 */

public class AccountCodes
	extends com.txdot.isd.rts.services.data.AccountCodesData
{
	protected DatabaseAccess caDA;

	/**
	 * AccountCodes constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public AccountCodes(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to query RTS.RTS_ACCT_CODES
	 *
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryAccountCodes() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryAccountCodes - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "AcctItmCd,"
				+ "RTSEffDate,"
				+ "RTSEffEndDate,"
				+ "AcctItmCdDesc,"
				+ "ShortAcctItmCd,"
				+ "AcctProcsCd,"
				+ "RedemdAcctItmCd,"
				+ "MiscFee,"
				+ "PrmtFeePrcnt,"
				+ "PrmtFlatFee,"
				+ "PrmtStndrdExpTime,"
				+ "PrmtValidtyPeriod,"
				+ "CrdtAllowdIndi,"
				+ "AcctItmGrpCd,"
				+ "AcctItmCrdtIndi,"
				+ "PayableTypeCd,"
				+ "RenwlPrntSmryCd, "
				+ "BaseFee "
				+ "FROM RTS.RTS_ACCT_CODES ");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryAccountCodes - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryAccountCodes - SQL - End");

			while (lrsQry.next())
			{
				AccountCodesData laAccountCodesData =
					new AccountCodesData();
				laAccountCodesData.setAcctItmCd(
					caDA.getStringFromDB(lrsQry, "AcctItmCd"));
				laAccountCodesData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laAccountCodesData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
				laAccountCodesData.setAcctItmCdDesc(
					caDA.getStringFromDB(lrsQry, "AcctItmCdDesc"));
				laAccountCodesData.setShortAcctItmCd(
					caDA.getIntFromDB(lrsQry, "ShortAcctItmCd"));
				laAccountCodesData.setAcctProcsCd(
					caDA.getStringFromDB(lrsQry, "AcctProcsCd"));
				laAccountCodesData.setRedemdAcctItmCd(
					caDA.getStringFromDB(lrsQry, "RedemdAcctItmCd"));
				laAccountCodesData.setMiscFee(
					caDA.getDollarFromDB(lrsQry, "MiscFee"));
				laAccountCodesData.setPrmtFeePrcnt(
					caDA.getDollarFromDB(lrsQry, "PrmtFeePrcnt"));
				laAccountCodesData.setPrmtFlatFee(
					caDA.getDollarFromDB(lrsQry, "PrmtFlatFee"));
				laAccountCodesData.setPrmtStndrdExpTime(
					caDA.getIntFromDB(lrsQry, "PrmtStndrdExpTime"));
				laAccountCodesData.setPrmtValidtyPeriod(
					caDA.getIntFromDB(lrsQry, "PrmtValidtyPeriod"));
				laAccountCodesData.setCrdtAllowdIndi(
					caDA.getIntFromDB(lrsQry, "CrdtAllowdIndi"));
				laAccountCodesData.setAcctItmGrpCd(
					caDA.getIntFromDB(lrsQry, "AcctItmGrpCd"));
				laAccountCodesData.setAcctItmCrdtIndi(
					caDA.getIntFromDB(lrsQry, "AcctItmCrdtIndi"));
				laAccountCodesData.setPayableTypeCd(
					caDA.getIntFromDB(lrsQry, "PayableTypeCd"));
				laAccountCodesData.setRenwlPrntSmryCd(
					caDA.getStringFromDB(lrsQry, "RenwlPrntSmryCd"));
					
				// defect 10741 
				laAccountCodesData.setBaseFee(
					caDA.getDollarFromDB(lrsQry, "BaseFee"));
				// end defect 10741 
				
				// Add element to the Vector
				lvRslt.addElement(laAccountCodesData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryAccountCodes - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryAccountCodes - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD	
}
