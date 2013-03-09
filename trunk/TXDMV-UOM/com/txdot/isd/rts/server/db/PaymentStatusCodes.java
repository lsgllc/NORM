package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.PaymentStatusCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * PaymentStatusCodes.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/03/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from 
 * 							qryPaymentStatusCodes()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                    
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_PYMNT_STAT_CDS
 * 
 * @version	5.2.3		06/19/2005
 * @author 	Kathy Harrell
 * <br> Creation Date:	08/31/2001 15:15:58  
 */

public class PaymentStatusCodes extends PaymentStatusCodesData
{
	DatabaseAccess caDA;
	/**
	 * PaymentStatusCodes constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public PaymentStatusCodes(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_PYMNT_STAT_CDS
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryPaymentStatusCodes()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryPaymentStatusCodes - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "PymntStatusCd,"
				+ "PymntStatusDesc "
				+ "FROM RTS.RTS_PYMNT_STAT_CDS");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryPaymentStatusCodes - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryPaymentStatusCodes - SQL - End");

			while (lrsQry.next())
			{
				PaymentStatusCodesData laPaymentStatusCodesData =
					new PaymentStatusCodesData();
				laPaymentStatusCodesData.setPymntStatusCd(
					caDA.getStringFromDB(lrsQry, "PymntStatusCd"));
				laPaymentStatusCodesData.setPymntStatusDesc(
					caDA.getStringFromDB(lrsQry, "PymntStatusDesc"));
				// Add element to the Vector
				lvRslt.addElement(laPaymentStatusCodesData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryPaymentStatusCodes - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryPaymentStatusCodes - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS