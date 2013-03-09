package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.PaymentTypeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * PaymentType.java
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
 * 							qryPaymentType()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                     
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_PAYMENT_TYPE
 * 
 * @version	5.2.3		06/19/2005
 * @author 	Kathy Harrell
 * <br>Creation Date:	08/31/2001 15:13:30  
 */

public class PaymentType extends PaymentTypeData
{
	DatabaseAccess caDA;
	/**
	 * PaymentType constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public PaymentType(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_PAYMENT_TYPE
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryPaymentType()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryPaymentType - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "PymntTypeCd,"
				+ "PymntTypeCdDesc,"
				+ "CntyUseIndi,"
				+ "CustUseIndi "
				+ "FROM RTS.RTS_PAYMENT_TYPE");
		try
		{
			Log.write(Log.SQL, this, " - qryPaymentType - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryPaymentType - SQL - End");

			while (lrsQry.next())
			{
				PaymentTypeData laPaymentTypeData =
					new PaymentTypeData();
				laPaymentTypeData.setPymntTypeCd(
					caDA.getIntFromDB(lrsQry, "PymntTypeCd"));
				laPaymentTypeData.setPymntTypeCdDesc(
					caDA.getStringFromDB(lrsQry, "PymntTypeCdDesc"));
				laPaymentTypeData.setCntyUseIndi(
					caDA.getIntFromDB(lrsQry, "CntyUseIndi"));
				laPaymentTypeData.setCustUseIndi(
					caDA.getIntFromDB(lrsQry, "CustUseIndi"));
				// Add element to the Vector
				lvRslt.addElement(laPaymentTypeData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryPaymentType - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryPaymentType - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS