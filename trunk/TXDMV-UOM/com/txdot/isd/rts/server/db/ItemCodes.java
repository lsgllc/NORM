package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * ItemCodes.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks 		07/12/2002 	Add call to closeLastStatement() after a 
 * 							query
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							modify qryItemCodes()
 * 							Ver 5.2.0
 * K Harrell	03/03/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qryItemCodes()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	10/26/2005	add qryItemCodes(String) from 
 * 							webapps.db.ItemCodes
 * 							deprecated qryItemCodes(String) 
 * 							defect 7889 Ver 5.2.3
 * K Harrell	09/28/2006	add functionality for piloting table data
 * 							modify qryItemCodes()
 * 				04/09/2007	reinstated 
 * 							defect 8900 / 9085 Ver Exempts
 * K Harrell	10/16/2007	do not reference pilot table data
 * 							modify qryItemCodes() 
 *							defect 9354 Ver Special Plates 2 	 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_ITEM_CODES
 * 
 * @version	Special Plates 2	10/16/2007 
 * @author 	Kathy Harrell
 * <br> Creation Date:			08/31/2001 17:14:19
 */

public class ItemCodes extends ItemCodesData
{
	DatabaseAccess caDA;

	/**
	 * ItemCodes constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess
	 * @throws RTSException
	 */
	public ItemCodes(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_ITEM_CODES
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryItemCodes() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryItemCodes - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 9354
		// Do not reference Pilot version
		// String lsTableCreator = CommonConstant.RTS_TBCREATOR;
		//		if (SystemProperty.isStaticTablePilot())
		//		{
		//			lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		//		}

		lsQry.append(
			"SELECT "
				+ "ItmCd,"
				+ "ItmCdDesc,"
				+ "ItmTrckngType,"
				+ "InvProcsngCd, "
				+ "PrntLocCd, "
				+ "PrintableIndi "
				+ "FROM "
				+ "RTS.RTS_ITEM_CODES");
				
		// end defect 9354

		try
		{
			Log.write(Log.SQL, this, " - qryItemCodes - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryItemCodes - SQL - End");

			while (lrsQry.next())
			{
				ItemCodesData laItemCodesData = new ItemCodesData();
				laItemCodesData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laItemCodesData.setItmCdDesc(
					caDA.getStringFromDB(lrsQry, "ItmCdDesc"));
				laItemCodesData.setItmTrckngType(
					caDA.getStringFromDB(lrsQry, "ItmTrckngType"));
				laItemCodesData.setInvProcsngCd(
					caDA.getIntFromDB(lrsQry, "InvProcsngCd"));
				laItemCodesData.setPrintableIndi(
					caDA.getIntFromDB(lrsQry, "PrintableIndi"));
				String lsCd = caDA.getStringFromDB(lrsQry, "PrntLocCd");
				if (lsCd == null || lsCd.trim().equals(""))
				{
					laItemCodesData.setPrntCd(0);
				}
				else
				{
					laItemCodesData.setPrntCd(lsCd.charAt(0));
				}
				// Add element to the Vector
				lvRslt.addElement(laItemCodesData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryItemCodes - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryItemCodes - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
