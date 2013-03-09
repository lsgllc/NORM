package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.IndicatorDescriptionsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * IndicatorDescriptions.java 
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
 * K Harrell	04/04/2005	Remove parameter from 
 * 							qryIndicatorDescriptions()
 * 							defect 7846 Ver 5.2.3      
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/22/2010	add ERenwlRteIndi
 * 							modify qryIndicatorDescriptions()
 * 							defect 10514 Ver 6.5.0                    
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_INDI_DESCS.
 *
 * @version	6.5.0 		06/22/2010
 * @author	Kathy Harrell
 * <br>Creation Date:	08/31/2001 14:43:35 
 */

public class IndicatorDescriptions extends IndicatorDescriptionsData
{
	DatabaseAccess caDA;
	/**
	 * IndicatorDescriptions constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public IndicatorDescriptions(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_INDI_DESCS
	 * 
	 * @return Vector
	 * @throws RTSException  	
	 */
	public Vector qryIndicatorDescriptions() throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryIndicatorDescriptions - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 10514 
		lsQry.append(
			"SELECT "
				+ "IndiName,"
				+ "IndiFieldValue,"
				+ "IndiDesc,"
				+ "IndiRcptPriority,"
				+ "IndiCCOPriority,"
				+ "IndiSalvPriority,"
				+ "IndiScrnPriority,"
				+ "JnkCdSysIndi, "
				+ "ERenwlRteIndi "
				+ "FROM RTS.RTS_INDI_DESCS");
		// end defect 10514 

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryIndicatorDescriptions - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryIndicatorDescriptions - SQL - End");

			while (lrsQry.next())
			{
				IndicatorDescriptionsData laIndicatorDescriptionsData =
					new IndicatorDescriptionsData();
				laIndicatorDescriptionsData.setIndiName(
					caDA.getStringFromDB(lrsQry, "IndiName"));
				laIndicatorDescriptionsData.setIndiFieldValue(
					caDA.getStringFromDB(lrsQry, "IndiFieldValue"));
				laIndicatorDescriptionsData.setIndiDesc(
					caDA.getStringFromDB(lrsQry, "IndiDesc"));
				laIndicatorDescriptionsData.setIndiRcptPriority(
					caDA.getIntFromDB(lrsQry, "IndiRcptPriority"));
				laIndicatorDescriptionsData.setIndiCCOPriority(
					caDA.getIntFromDB(lrsQry, "IndiCCOPriority"));
				laIndicatorDescriptionsData.setIndiSalvPriority(
					caDA.getIntFromDB(lrsQry, "IndiSalvPriority"));
				laIndicatorDescriptionsData.setIndiScrnPriority(
					caDA.getIntFromDB(lrsQry, "IndiScrnPriority"));
				laIndicatorDescriptionsData.setJnkCdSysIndi(
					caDA.getIntFromDB(lrsQry, "JnkCdSysIndi"));

				// defect 10514
				laIndicatorDescriptionsData.setERenwlRteIndi(
					caDA.getIntFromDB(lrsQry, "ERenwlRteIndi"));
				// end defect 10514 

				// Add element to the Vector
				lvRslt.addElement(laIndicatorDescriptionsData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryIndicatorDescriptions - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIndicatorDescriptions - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
