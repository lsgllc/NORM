package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WebAgencyBatchStatusData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebAgencyBatchStatus.java 
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708 Ver 6.7.0 
 * K Harrell 	01/05/2011 	Renamings per standards 
 *        					defect 10708 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_WEB_AGNCY_BATCH_STATUS 
 *
 * @version	6.7.0			12/28/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 20:28:17
 */

public class WebAgencyBatchStatus
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgencyBatchStatus Constructor
	 * 
	 */
	public WebAgencyBatchStatus(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS_WEB_AGNCY_BATCH_STATUS
	 *
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgencyBatchStatus() throws RTSException
	{
		csMethod = "qryWebAgencyBatchStatus";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "BATCHSTATUSCD,"
				+ "BATCHSTATUSDESC "
				+ "from RTS.RTS_WEB_AGNCY_BATCH_STATUS ");

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgencyBatchStatusData laData =
					new WebAgencyBatchStatusData();
				laData.setBatchStatusCd(
					caDA.getStringFromDB(lrsQry, "BatchStatusCd"));

				laData.setBatchStatusDesc(
					caDA.getStringFromDB(lrsQry, "BatchStatusDesc"));

				// Add element to the Vector
				lvRslt.addElement(laData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

} //END OF CLASS 
