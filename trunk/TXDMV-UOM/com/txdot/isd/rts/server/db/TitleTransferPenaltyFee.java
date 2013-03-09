package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TitleTransferPenaltyFeeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * TitleTransferPenaltyFee.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/02/2008	Created
 * 							defect 9583 Ver Defect POS A
 * K Harrell	05/28/2008	Renamed *WorkDays* to *CalndrDays* 
 * 							modify qryTitleTransferPenaltyFee()
 * 							defect 9583 Ver Defect POS A
 * K Harrell	09/30/2011	Implement reference to StaticTablePilot
 * 							modify qryTitleTransferPenaltyFee()
 * 							defect 11048 Ver 6.9.0     
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_TTL_TRNSFR_PNLTY_FEE
 * 
 * @version 6.9.0			09/30/2011
 * @author K Harrell
 * @since 					04/02/2008 17:13:00
 */
public class TitleTransferPenaltyFee
{
	DatabaseAccess caDA;

	/**
	 * TitleTransferPenaltyFee constructor comment.
	 * 
	 * @param aaDA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	public TitleTransferPenaltyFee(DatabaseAccess aaDA)
			throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_TTL_TRNSFR_PNLTY_FEE
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryTitleTransferPenaltyFee() throws RTSException
	{
		Log.write(Log.METHOD, this,
				" - qryTitleTransferPenaltyFee - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;
		
		// defect 11048
		String lsTableCreator = CommonConstant.RTS_TBCREATOR;

		if (SystemProperty.isStaticTablePilot())
		{
			lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		}
		lsQry.append("SELECT TtlTrnsfrEntCd," + "RTSEffDate,"
				+ "RTSEffEndDate," + "BegCalndrDaysCount,"
				+ "EndCalndrDaysCount," + "BaseTtlTrnsfrPnltyAmt,"
				+ "DelnqntPrortnIncrmnt, " + "AddlTtlTrnsfrPnltyAmt "
				+ "FROM "
				// + RTS.RTS_TTL_TRNSFR_PNLTY_FEE");
				+ lsTableCreator + ".RTS_TTL_TRNSFR_PNLTY_FEE");
		// end defect 11048
		
		try
		{
			Log.write(Log.SQL, this,
					" - qryTitleTransferPenaltyFee - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);

			Log.write(Log.SQL, this,
					" - qryTitleTransferPenaltyFee - SQL - End");

			while (lrsQry.next())
			{
				TitleTransferPenaltyFeeData laTtlTrnsfrPnltyFeeData = new TitleTransferPenaltyFeeData();

				laTtlTrnsfrPnltyFeeData.setTtlTrnsfrEntCd(caDA
						.getStringFromDB(lrsQry, "TtlTrnsfrEntCd"));

				laTtlTrnsfrPnltyFeeData.setRTSEffDate(caDA
						.getIntFromDB(lrsQry, "RTSEffDate"));

				laTtlTrnsfrPnltyFeeData.setRTSEffEndDate(caDA
						.getIntFromDB(lrsQry, "RTSEffEndDate"));

				laTtlTrnsfrPnltyFeeData.setBegCalndrDaysCount(caDA
						.getIntFromDB(lrsQry, "BegCalndrDaysCount"));

				laTtlTrnsfrPnltyFeeData.setEndCalndrDaysCount(caDA
						.getIntFromDB(lrsQry, "EndCalndrDaysCount"));

				laTtlTrnsfrPnltyFeeData.setBaseTtlTrnsfrPnltyAmt(caDA
						.getDollarFromDB(lrsQry,
								"BaseTtlTrnsfrPnltyAmt"));

				laTtlTrnsfrPnltyFeeData.setDelnqntPrortnIncrmnt(caDA
						.getIntFromDB(lrsQry, "DelnqntPrortnIncrmnt"));

				laTtlTrnsfrPnltyFeeData.setAddlPnltyAmt(caDA
						.getDollarFromDB(lrsQry,
								"AddlTtlTrnsfrPnltyAmt"));

				// Add element to the Vector
				lvRslt.addElement(laTtlTrnsfrPnltyFeeData);
			} // End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this,
					" - qryTitleTransferPenaltyFee - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - qryTitleTransferPenaltyFee - SQL Exception "
							+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} // END OF QUERY METHOD
}
