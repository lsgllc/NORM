package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.ErrorMessagesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * ErrorMessages.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	04/04/2005	Remove parameter from qryErrorMessages()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3                        
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_ERR_MSGS.
 *
 * @version	5.2.3		06/30/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:	08/31/2001 14:31:49 
 */

public class ErrorMessages extends ErrorMessagesData
{
	DatabaseAccess caDA;
	/**
	 * ErrorMessages constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public ErrorMessages(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_ERR_MSGS
	 *
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryErrorMessages()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryErrorMessages - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "ErrMsgNo,"
				+ "ErrMsgType,"
				+ "ErrMsgCat,"
				+ "ErrMsgDesc,"
				+ "PCErrLogIndi,"
				+ "MFErrLogIndi "
				+ "FROM RTS.RTS_ERR_MSGS");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryErrorMessages - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryErrorMessages - SQL - End");

			while (lrsQry.next())
			{
				ErrorMessagesData laErrorMessagesData =
					new ErrorMessagesData();
				laErrorMessagesData.setErrMsgNo(
					caDA.getIntFromDB(lrsQry, "ErrMsgNo"));
				laErrorMessagesData.setErrMsgType(
					caDA.getStringFromDB(lrsQry, "ErrMsgType"));
				laErrorMessagesData.setErrMsgCat(
					caDA.getStringFromDB(lrsQry, "ErrMsgCat"));
				laErrorMessagesData.setErrMsgDesc(
					caDA.getStringFromDB(lrsQry, "ErrMsgDesc"));
				laErrorMessagesData.setPCErrLogIndi(
					caDA.getIntFromDB(lrsQry, "PCErrLogIndi"));
				laErrorMessagesData.setMFErrLogIndi(
					caDA.getIntFromDB(lrsQry, "MFErrLogIndi"));
				// Add element to the Vector
				lvRslt.addElement(laErrorMessagesData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryErrorMessages - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryErrorMessages - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
