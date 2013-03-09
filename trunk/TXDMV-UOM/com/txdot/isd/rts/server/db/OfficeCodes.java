package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.OfficeCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * OfficeCodes.java
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
 * K Harrell	04/04/2005	Remove parameter from qryOfficeCodes()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                           
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_OFFICE_CDS 
 * 
 * @version	5.2.3		06/194/2005
 * @author 	Kathy Harrell
 * <br>Creation Date:	08/31/2001 16:34:02 
 */

public class OfficeCodes extends OfficeCodesData
{
	DatabaseAccess caDA;
	/**
	 * OfficeCodes constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public OfficeCodes(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_OFFICE_IDS
	 * 
	 * @return Vector
	 * @throws RTSException 	
	 */
	public Vector qryOfficeCodes()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryOfficeCodes - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceCd,"
				+ "OfcIssuanceCdDesc "
				+ "FROM RTS.RTS_OFFICE_CDS");
		try
		{
			Log.write(Log.SQL, this, " - qryOfficeCodes - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryOfficeCodes - SQL - End");

			while (lrsQry.next())
			{
				OfficeCodesData laOfficeCodesData =
					new OfficeCodesData();
				laOfficeCodesData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laOfficeCodesData.setOfcIssuanceCdDesc(
					caDA.getStringFromDB(lrsQry, "OfcIssuanceCdDesc"));
				// Add element to the Vector
				lvRslt.addElement(laOfficeCodesData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryOfficeCodes - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryOfficeCodes - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
