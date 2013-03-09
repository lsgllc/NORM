package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.ProductServiceData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * ProductService.java
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
 *							delete delProductService(),
 *							updProductService(),insProductService()
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from 
 * 							qryProductService()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	12/08/2010	use ItmCd, YrReqdIndi vs. 
 * 							 InvItmCd, YrRqrdIndi
 * 							modify qryProductService() 
 * 							defect 10695 Ver 6.7.0                    
 * B Hargrove	01/10/2012	add IRPIndi
 * 							modify qryProductService()
 * 							defect 11218 Ver 6.10.0 			 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_PRDCT_SRVC
 * 
 * @version	6.10.0		01/10/2012
 * @author 	Kathy Harrell
 * <br>Creation Date:	01/04/2002 12:32:21
 */

public class ProductService extends ProductServiceData
{
	private String csMethod = new String();

	DatabaseAccess cDA;

	/**
	 * ProductService constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public ProductService(DatabaseAccess aDA) throws RTSException
	{
		cDA = aDA;
	}

	/**
	 * Method to Query RTS.RTS_PRDCT_SRVC
	 * 
	 * @return  Vector 
	 * @throws RTSException
	 */
	public Vector qryProductService() throws RTSException
	{
		csMethod = "qryProductService";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 10695
		// defect 11218
		lsQry.append(
			"SELECT "
				+ "PrdctSrvcDesc,"
				+ "AcctItmCd,"
				+ "ItmCd,"
				+ "FxdChrgIndi,"
				+ "YrReqdIndi,"
				+ "CrdtItmIndi, "
				+ "IRPIndi "
				+ "FROM RTS.RTS_PRDCT_SRVC  ");
		// end defect 11218
		// end defect 10695 

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = cDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, csMethod + "  - SQL - End");

			while (lrsQry.next())
			{
				ProductServiceData laProductServiceData =
					new ProductServiceData();
				laProductServiceData.setPrdctSrvcDesc(
					cDA.getStringFromDB(lrsQry, "PrdctSrvcDesc"));
				laProductServiceData.setAcctItmCd(
					cDA.getStringFromDB(lrsQry, "AcctItmCd"));
				laProductServiceData.setFxdChrgIndi(
					cDA.getIntFromDB(lrsQry, "FxdChrgIndi"));
					
				// defect 10695 
				laProductServiceData.setItmCd(
					cDA.getStringFromDB(lrsQry, "ItmCd"));
				laProductServiceData.setYrReqdIndi(
					cDA.getIntFromDB(lrsQry, "YrReqdIndi"));
				// end defect 10695 
				
				laProductServiceData.setCrdtItmIndi(
					cDA.getIntFromDB(lrsQry, "CrdtItmIndi"));
				// defect 11218
				laProductServiceData.setIRPIndi(
					cDA.getIntFromDB(lrsQry, "IRPIndi"));
				// end defect 11218
				// Add element to the Vector
				lvRslt.addElement(laProductServiceData);
			} //End of While

			lrsQry.close();
			cDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD
} //END OF CLASS
