package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TitleTransferPenaltyExemptCodeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * TitleTransferPenaltyExemptCode.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/22/2008	Recreated 
 * 							defect 9583 Ver Defect POS A  
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_TTL_TRNSFR_PNLTY_EXMPT_CD
 *
 * @version	Defect POS A	06/22/2008
 * @author	K Harrell
 * <br>Creation Date:		06/22/2008	15:58:00
 */
public class TitleTransferPenaltyExemptCode
{
	DatabaseAccess caDA;

	/**
	 * TitleTransferPenaltyExemptCode constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public TitleTransferPenaltyExemptCode(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_TTL_TRNSFR_PNLTY_EXMPT_CD
	 *
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryTitleTransferPenaltyExmptCd() throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryTitleTransferPenaltyExmptCd - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT TtlTrnsfrPnltyExmptCd,"
				+ "TtlTrnsfrPnltyExmptDesc "
				+ "FROM RTS.RTS_TTL_TRNSFR_PNLTY_EXMPT_CD");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryTitleTransferPenaltyExmptCd - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);

			Log.write(
				Log.SQL,
				this,
				" - qryTitleTransferPenaltyExmptCd - SQL - End");

			while (lrsQry.next())
			{
				TitleTransferPenaltyExemptCodeData laTtlTrnsfrPnltyExmptCdData =
					new TitleTransferPenaltyExemptCodeData();

				laTtlTrnsfrPnltyExmptCdData.setTtlTrnsfrPnltyExmptCd(
					caDA.getStringFromDB(
						lrsQry,
						"TtlTrnsfrPnltyExmptCd"));

				laTtlTrnsfrPnltyExmptCdData.setTtlTrnsfrPnltyExmptDesc(
					caDA.getStringFromDB(
						lrsQry,
						"TtlTrnsfrPnltyExmptDesc"));

				// Add element to the Vector
				lvRslt.addElement(laTtlTrnsfrPnltyExmptCdData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTitleTransferPenaltyExmptCd - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTitleTransferPenaltyExmptCd - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
}
