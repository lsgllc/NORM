package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.RegistrationAdditionalFeeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * RegistrationAdditionalFee.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/28/2005	Created
 *							defect 8104 Ver 5.2.2 Fix 4
 * K Harrell	04/04/2005	Remove parameter from 
 * 							qryRegistrationAdditionalFee()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                   
 * ---------------------------------------------------------------------
 */
 
/**
 * This class contains methods to access RTS_REG_ADDL_FEE
 * 
 * @version	5.2.3			06/19/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		03/28/2005	17:14:02 
 */

public class RegistrationAdditionalFee extends RegistrationAdditionalFeeData
{
	DatabaseAccess caDA;
/**
 * RegistrationAdditionalFee constructor comment.
 * 
 * @param  aaDA DatabaseAccess
 * @throws RTSException 
 */
public RegistrationAdditionalFee(DatabaseAccess aaDA) throws RTSException
{
	caDA = aaDA;
}
/**
 * Method to Query RTS_REG_ADDL_FEE
 *
 * @return Vector
 * @throws RTSException  	
 */
public Vector qryRegistrationAdditionalFee()
	throws RTSException
{
	Log.write(Log.METHOD, this, " - qryRegistrationAdditionalFee - Begin");
	
	StringBuffer lsQry = new StringBuffer();
	
	Vector lvRslt = new Vector();
	
	ResultSet lrsQry;
	
	lsQry.append(
		"SELECT "
			+ "RegClassCd,"
			+ "RTSEffDate,"
			+ "RTSEffEndDate,"
			+ "RegAddlFee,"
			+ "AddlFeeItmCd "
			+ "FROM RTS.RTS_REG_ADDL_FEE");
	try
	{
		Log.write(Log.SQL, this, " - qryRegistrationAdditionalFee - SQL - Begin");
		lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
		Log.write(Log.SQL, this, " - qryRegistrationAdditionalFee - SQL - End");
		
		while (lrsQry.next())
		{
			RegistrationAdditionalFeeData laRegistrationAdditionalFeeData = new RegistrationAdditionalFeeData();
			laRegistrationAdditionalFeeData.setRegClassCd(caDA.getIntFromDB(lrsQry, "RegClassCd"));
			laRegistrationAdditionalFeeData.setRTSEffDate(caDA.getIntFromDB(lrsQry, "RTSEffDate"));
			laRegistrationAdditionalFeeData.setRTSEffEndDate(
				caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
			laRegistrationAdditionalFeeData.setRegAddlFee(
				caDA.getDollarFromDB(lrsQry, "RegAddlFee"));
			laRegistrationAdditionalFeeData.setAddlFeeItmCd(
				caDA.getStringFromDB(lrsQry, "AddlFeeItmCd"));
			// Add element to the Vector
			lvRslt.addElement(laRegistrationAdditionalFeeData);
		} //End of While
		
		lrsQry.close();
		caDA.closeLastDBStatement();
		lrsQry = null;
		Log.write(Log.METHOD, this, " - qryRegistrationAdditionalFee - End ");
		return (lvRslt);
	}
	catch (SQLException leSQLEx)
	{
		Log.write(
			Log.SQL_EXCP,
			this,
			" - qryRegistrationAdditionalFee - SQL Exception " + leSQLEx.getMessage());
		throw new RTSException(RTSException.DB_ERROR, leSQLEx);
	}
} //END OF QUERY METHOD
} //END OF CLASS
