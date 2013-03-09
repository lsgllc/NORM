package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.WebAgencyTypeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebAgencyType.java
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
 * SQL class for RTS_WEB_AGNCY_TYPE 
 *
 * @version	6.7.0			01/05/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 20:28:17
 */

public class WebAgencyType
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgencyType Constructor
	 * 
	 */
	public WebAgencyType(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS_WEB_AGENCY_TYPE
	 *
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgencyType() throws RTSException
	{
		csMethod = "qryWebAgencyType";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "AGNCYTYPECD,"
				+ "AGNCYTYPEDESC "
				+ "from RTS.RTS_WEB_AGNCY_TYPE ");

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgencyTypeData laData =
					new WebAgencyTypeData();
				laData.setAgncyTypeCd(
					caDA.getStringFromDB(lrsQry, "AgncyTypeCd"));

				laData.setAgncyTypeDesc(
					caDA.getStringFromDB(lrsQry, "AgncyTypeDesc"));

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
