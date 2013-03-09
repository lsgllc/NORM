package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.SalesTaxCategoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * SalesTaxCategory.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/04/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qrySalesTaxCategory() 
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	10/31/2011	add TTLTRNSFRPNLTYEXMPTCD
 * 							modify qrySalesTaxCategory()
 * 							defect 11048 Ver 6.9.0                    
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_SALES_TAX_CATEGORY
 * 
 * @version	6.9.0		10/31/2011
 * @author 	Kathy Harrell
 * <br>Creation Date:	08/31/2001 15:27:21 
 */

public class SalesTaxCategory extends SalesTaxCategoryData
{
	DatabaseAccess caDA;
	/**
	 * SalesTaxCategory constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public SalesTaxCategory(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_SALES_TAX_CAT
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qrySalesTaxCategory()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qrySalesTaxCategory - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;
		
		// defect 11048
		// add TtlTrnsfrPnltyExmptCd
		lsQry.append(
			"SELECT "
				+ "SalesTaxCat,"
				+ "SalesTaxBegDate,"
				+ "SalesTaxEndDate,"
				+ "SalesTaxFee,"
				+ "SalesTaxPrcnt,"
				+ "SalesTaxPriceReqd,"
				+ "ExmptReasonsReqd,"
				+ "TradeInReqd, "
				+ "TtlTrnsfrPnltyExmptCd "
				+ "FROM RTS.RTS_SALES_TAX_CAT");
		// end defect 11048 

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qrySalesTaxCategory - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qrySalesTaxCategory - SQL - End");

			while (lrsQry.next())
			{
				SalesTaxCategoryData laSalesTaxCategoryData =
					new SalesTaxCategoryData();
				laSalesTaxCategoryData.setSalesTaxCat(
					caDA.getStringFromDB(lrsQry, "SalesTaxCat"));
				laSalesTaxCategoryData.setSalesTaxBegDate(
					caDA.getIntFromDB(lrsQry, "SalesTaxBegDate"));
				laSalesTaxCategoryData.setSalesTaxEndDate(
					caDA.getIntFromDB(lrsQry, "SalesTaxEndDate"));
				laSalesTaxCategoryData.setSalesTaxFee(
					caDA.getDollarFromDB(lrsQry, "SalesTaxFee"));
				laSalesTaxCategoryData.setSalesTaxPrcnt(
					caDA.getDollarFromDB(lrsQry, "SalesTaxPrcnt"));
				laSalesTaxCategoryData.setSalesTaxPriceReqd(
					caDA.getIntFromDB(lrsQry, "SalesTaxPriceReqd"));
				laSalesTaxCategoryData.setExmptReasonsReqd(
					caDA.getIntFromDB(lrsQry, "ExmptReasonsReqd"));
				laSalesTaxCategoryData.setTradeInReqd(
					caDA.getIntFromDB(lrsQry, "TradeInReqd"));
				
				// defect 11048 
				laSalesTaxCategoryData.setTtlTrnsfrPnltyExmptCd(
					caDA.getStringFromDB(lrsQry, "TtlTrnsfrPnltyExmptCd"));
				// end defect 11048 
				
				// Add element to the Vector
				lvRslt.addElement(laSalesTaxCategoryData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qrySalesTaxCategory - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySalesTaxCategory - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
