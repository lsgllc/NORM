package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.DMVTRMessageData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * DMVTRMessage.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	04/04/2005	Remove parameter from qryDMVTRMessage()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3                      
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_DMVTR_MSG
 *
 * @version	5.2.3		06/30/2005
 * @author	Kathy Harrell
 * <br>Creation Date:	08/31/2001 14:29:09  
 */

public class DMVTRMessage extends DMVTRMessageData
{
	DatabaseAccess caDA;
	/**
	 * DMVTRMessage constructor comment.
	 *
	 * @param  aaDA	DatabaseAccess 
	 * @throws RTSException
	 */
	public DMVTRMessage(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_DMVTR_MSG
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryDMVTRMessage()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryDMVTRMessage - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "RTSDMVTRMsg1,"
				+ "RTSDMVTRMsg2,"
				+ "RTSDMVTRMsg3 "
				+ "FROM RTS.RTS_DMVTR_MSG");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryDMVTRMessage - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryDMVTRMessage - SQL - End");

			while (lrsQry.next())
			{
				DMVTRMessageData laDMVTRMessageData =
					new DMVTRMessageData();
				laDMVTRMessageData.setRTSDMVTRMsg1(
					caDA.getStringFromDB(lrsQry, "RTSDMVTRMsg1"));
				laDMVTRMessageData.setRTSDMVTRMsg2(
					caDA.getStringFromDB(lrsQry, "RTSDMVTRMsg2"));
				laDMVTRMessageData.setRTSDMVTRMsg3(
					caDA.getStringFromDB(lrsQry, "RTSDMVTRMsg3"));
				// Add element to the Vector
				lvRslt.addElement(laDMVTRMessageData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryDMVTRMessage - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryDMVTRMessage - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
