package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.DisabledPlacardCustomerIdTypeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * DisabledPlacardCustomerIdType.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created
 * 							defect 9831 Ver Defect_POS_B 
 * ---------------------------------------------------------------------
 */

/**
 * Calls to RTS_DSABLD_PLCRD_CUST_ID_TYPE 
 *
 * @version	Defect_POS_B	10/27/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008 
 */
public class DisabledPlacardCustomerIdType
	extends DisabledPlacardCustomerIdTypeData
{
	
	DatabaseAccess caDA;

	/**
	 * DisabledPlacardCustomerIdType constructor comment.
	 *
	 * @param  aaDA	DatabaseAccess 
	 * @throws RTSException
	 */
	public DisabledPlacardCustomerIdType(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_DSABLD_PLCRD_CUST_ID_TYPE
	 *
	 * @return Vector
	 * @throws RTSException  	
	 */
	public Vector qryDisabledPlacardCustomerIdType()
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryDisabledPlacardCustomerIdType - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "CUSTIDTYPECD,"
				+ "CUSTIDTYPEDSPLYORDR,"
				+ "CUSTIDTYPEDESC, "
				+ "INSTINDI "
				+ " FROM RTS.RTS_DSABLD_PLCRD_CUST_ID_TYPE");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryDisabledPlacardCustomerIdType - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryDisabledPlacardCustomerIdType - SQL - End");

			while (lrsQry.next())
			{
				DisabledPlacardCustomerIdTypeData laDPCustIdTypeData =
					new DisabledPlacardCustomerIdTypeData();

				laDPCustIdTypeData.setCustIdTypeCd(
					caDA.getIntFromDB(lrsQry, "CustIdTypeCd"));

				laDPCustIdTypeData.setCustIdTypeDsplyOrdr(
					caDA.getIntFromDB(lrsQry, "CustIdTypeDsplyOrdr"));

				laDPCustIdTypeData.setCustIdTypeDesc(
					caDA.getStringFromDB(lrsQry, "CustIdTypeDesc"));

				laDPCustIdTypeData.setInstIndi(
					caDA.getIntFromDB(lrsQry, "InstIndi"));

				// Add element to the Vector
				lvRslt.addElement(laDPCustIdTypeData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryDisabledPlacardCustomerIdType - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryDisabledPlacardCustomerIdType - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
}
