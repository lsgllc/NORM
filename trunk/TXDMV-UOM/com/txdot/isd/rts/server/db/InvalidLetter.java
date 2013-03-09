package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.InvalidLetterData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * InvalidLetter.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/02/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qryInvalidLetter()
 * 							defect 7846 Ver 5.2.3      
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                   
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_INVALID_LETTER.
 *
 * @version	5.2.3		06/19/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:	08/31/2001 17:10:04 
 */

public class InvalidLetter extends InvalidLetterData
{
	DatabaseAccess caDA;
	/**
	 * InvalidLetter constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public InvalidLetter(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_INVALID_LETTER
	 *
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryInvalidLetter()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryInvalidLetter - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT ItmCd,InvldLtrCombo FROM RTS.RTS_INVALID_LETTER");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryInvalidLetter - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryInvalidLetter - SQL - End");

			while (lrsQry.next())
			{
				InvalidLetterData laInvalidLetterData =
					new InvalidLetterData();
				laInvalidLetterData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laInvalidLetterData.setInvldLtrCombo(
					caDA.getStringFromDB(lrsQry, "InvldLtrCombo"));
				// Add element to the Vector
				lvRslt.addElement(laInvalidLetterData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryInvalidLetter - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInvalidLetter - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
