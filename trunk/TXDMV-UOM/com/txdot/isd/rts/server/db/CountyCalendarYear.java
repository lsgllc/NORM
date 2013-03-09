package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.CountyCalendarYearData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * CountyCalendarYear.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	08/05/2003	Terp; Add CntyTerpStatusCd 
 *                          modify qryCountyCalendarYear()
 *							defect 6447
 * K Harrell	04/04/2005	Remove parameter from qryCountyCalendarYear()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3                       
 * B Hargrove	07/10/2008	There is a new column for Mobility Fee 
 * 							(NOT the same as Title TERP Mobility Fee  
 * 							from defect 8552). 
 * 							modify qryCountyCalendarYear() 
 * 							defect 9728 Ver MyPlates_POS
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to access RTS_CNTY_CALNDR_YR
 *
 * @version	MyPlates_POS	07/10/2008 
 * @author	Kathy Harrell
 * <br>Creation Date:		08/31/2001
 */

public class CountyCalendarYear extends CountyCalendarYearData
{
	DatabaseAccess caDA;
	/**
	 * CountyCalendarYear constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public CountyCalendarYear(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_CNTY_CALNDR_YR
	 *
	 * @return  Vector
	 * @throws  RTSException 
	 */
	public Vector qryCountyCalendarYear()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryCountyCalendarYear - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defec t 9728
		// add MobilityFee
		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "FscalYr,"
				+ "CntyRdMilesNo,"
				+ "CntyRdBrdgFee,"
				+ "ChildSaftyFee,"
				+ "SalesTaxCrdt,"
				+ "CntySizeCd, "
				+ "CntyTERPStatusCd, "
				+ "MobilityFee "
				+ "FROM RTS.RTS_CNTY_CALNDR_YR");
		// end defect 9728
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryCountyCalendarYear - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryCountyCalendarYear - SQL - End");

			while (lrsQry.next())
			{
				CountyCalendarYearData lCountyCalendarYearData =
					new CountyCalendarYearData();
				lCountyCalendarYearData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				lCountyCalendarYearData.setFscalYr(
					caDA.getIntFromDB(lrsQry, "FscalYr"));
				lCountyCalendarYearData.setCntyRdMilesNo(
					caDA.getIntFromDB(lrsQry, "CntyRdMilesNo"));
				lCountyCalendarYearData.setCntyRdBrdgFee(
					caDA.getDollarFromDB(lrsQry, "CntyRdBrdgFee"));
				lCountyCalendarYearData.setChildSaftyFee(
					caDA.getDollarFromDB(lrsQry, "ChildSaftyFee"));
				lCountyCalendarYearData.setSalesTaxCrdt(
					caDA.getDoubleFromDB(lrsQry, "SalesTaxCrdt"));
				lCountyCalendarYearData.setCntySizeCd(
					caDA.getStringFromDB(lrsQry, "CntySizeCd"));
				lCountyCalendarYearData.setCntyTERPStatusCd(
					caDA.getStringFromDB(lrsQry, "CntyTERPStatusCd"));
				// defect 9728
				// add MobilityFee
				lCountyCalendarYearData.setMobilityFee(
					caDA.getDollarFromDB(lrsQry, "MobilityFee"));
				// end defect 9728
				// Add element to the Vector
				lvRslt.addElement(lCountyCalendarYearData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryCountyCalendarYear - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCountyCalendarYear - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
