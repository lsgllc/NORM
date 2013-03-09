package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.RegistrationClassFeeGroupData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * RegistrationClassFeeGroup.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/07/2011	Created
 * 							defect 10695 Ver 6.7.0 
 * K Harrell	01/11/2011	RegClassFeeGrpCd to String
 * 							defect 10695 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class defines the SQL statements to interface with 
 * 	RTS_REG_CLASS_FEE_GRP
 *
 * @version	6.7.0			01/11/2011	
 * @author	Kathy Harrell
 * <br>Creation Date:		12/08/2010 16:18:17 
 */
public class RegistrationClassFeeGroup
{
	DatabaseAccess caDA;

	private String csMethod = new String();

	/**
	 * RegistrationClassFeeGroup.java Constructor
	 * 
	 */
	public RegistrationClassFeeGroup(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to Query RTS.RTS_REG_CLASS_FEE_GRP
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryRegistrationClassFeeGroup() throws RTSException
	{
		csMethod = "qryRegistrationClassFeeGroup";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "RegClassFeeGrpCd,"
				+ "BegWtRnge,"
				+ "EndWtRnge,"
				+ "RegClassGrpRegFee "
				+ "FROM RTS.RTS_REG_CLASS_FEE_GRP");

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				RegistrationClassFeeGroupData laRegClassGrpFeeData =
					new RegistrationClassFeeGroupData();

				laRegClassGrpFeeData.setRegClassFeeGrpCd(
					caDA.getStringFromDB(lrsQry, "RegClassFeeGrpCd"));

				laRegClassGrpFeeData.setBegWtRnge(
					caDA.getIntFromDB(lrsQry, "BegWtRnge"));

				laRegClassGrpFeeData.setEndWtRnge(
					caDA.getIntFromDB(lrsQry, "EndWtRnge"));

				laRegClassGrpFeeData.setRegClassGrpRegFee(
					caDA.getDollarFromDB(lrsQry, "RegClassGrpRegFee"));

				// Add element to the Vector
				lvRslt.addElement(laRegClassGrpFeeData);
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
				csMethod + " - SQL Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

}
