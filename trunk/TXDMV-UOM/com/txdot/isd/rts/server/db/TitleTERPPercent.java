package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.TitleTERPPercentData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * TitleTERPPercent.java
 *
 * (c) Texas Department of Transportation 2003
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/18/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qryTitleTERPPercent()	
 * 							defect 7846 Ver 5.2.3  
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3  
 * ---------------------------------------------------------------------
 */
/**
 * This class allows the user to access RTS_TTL_TERP_PRCNT
 * 
 * @version	5.2.3		06/19/2005
 * @author 	Kathy Harrell
 * <br>Creation Date:	07/30/2003 21:31:59 
 */
public class TitleTERPPercent extends TitleTERPPercentData
{
	DatabaseAccess caDA;
	/**
	 * TitleTERPPercent constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public TitleTERPPercent(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_TTL_TERP_Percent
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryTitleTERPPercent()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryTitleTERPPercent - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT SalesTaxCat,"
				+ "RTSEffDate,"
				+ "RTSEffEndDate,"
				+ "BegModlYrRnge,"
				+ "EndModlYrRnge,"
				+ "DieselIndi,"
				+ "BegDieselWtRnge,"
				+ "EndDieselWtRnge,"
				+ "TtlTERPPrcnt,"
				+ "TERPPrcntAcctItmCd "
				+ "FROM RTS.RTS_TTL_TERP_PRCNT");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryTitleTERPPercent - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryTitleTERPPercent - SQL - End");

			while (lrsQry.next())
			{
				TitleTERPPercentData laTitleTERPPercentData =
					new TitleTERPPercentData();
				laTitleTERPPercentData.setSalesTaxCat(
					caDA.getStringFromDB(lrsQry, "SalesTaxCat"));
				laTitleTERPPercentData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laTitleTERPPercentData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
				laTitleTERPPercentData.setBegModlYrRnge(
					caDA.getIntFromDB(lrsQry, "BegModlYrRnge"));
				laTitleTERPPercentData.setEndModlYrRnge(
					caDA.getIntFromDB(lrsQry, "EndModlYrRnge"));
				laTitleTERPPercentData.setDieselIndi(
					caDA.getIntFromDB(lrsQry, "DieselIndi"));
				laTitleTERPPercentData.setBegDieselWtRnge(
					caDA.getIntFromDB(lrsQry, "BegDieselWtRnge"));
				laTitleTERPPercentData.setEndDieselWtRnge(
					caDA.getIntFromDB(lrsQry, "EndDieselWtRnge"));
				laTitleTERPPercentData.setTtlTERPPrcnt(
					caDA.getDollarFromDB(lrsQry, "TtlTERPPrcnt"));
				laTitleTERPPercentData.setTERPPrcntAcctItmCd(
					caDA.getStringFromDB(lrsQry, "TERPPrcntAcctItmCd"));
				// Add element to the Vector
				lvRslt.addElement(laTitleTERPPercentData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTitleTERPPercent - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTitleTERPPercent - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
}