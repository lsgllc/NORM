package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.PlateToStickerData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 * PlateToSticker.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/14/2005	Created
 * 							defect 8218 Ver 5.2.3 	
 * B Hargrove	09/23/2010	Add new DfltStkrCdIndi column
 * 							modify qryPlateToSticker()
 * 							defect 10600 Ver 6.6.0
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_PLT_TO_STKR
 *
 * @version	Ver 6.6.0		09/23/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		06/14/2005	16:51:00
 */
public class PlateToSticker extends PlateToStickerData
{
	DatabaseAccess caDA;

	/**
	 * ClassToPlate constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public PlateToSticker(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_PlT_TO_STKR 
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryPlateToSticker() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryPlateToSticker - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 10600
		// add DfltStkrCdIndi 
		lsQry.append(
			"SELECT "
				+ "RegPltCd, "
				+ "RegStkrCd, "
				+ "ItmCdDesc,"
				+ "A.DfltStkrCdIndi,"
				+ "RTSEffDate, "
				+ "RTSEffEndDate, "
				+ "AcctItmCd "
				+ "FROM RTS.RTS_PLT_TO_STKR A, RTS.RTS_ITEM_CODES B "
				+ "WHERE A.REGSTKRCD = B.ITMCD "
				+ "UNION "
				+ "SELECT "
				+ "RegPltCd, "
				+ "RegStkrCd, "
				+ "' ' AS ItmCdDesc,"
				+ "A.DfltStkrCdIndi,"
				+ "RTSEffDate, "
				+ "RTSEffEndDate, "
				+ "AcctItmCd "
				+ "FROM RTS.RTS_PLT_TO_STKR A WHERE NOT EXISTS "
				+ "(SELECT * FROM RTS.RTS_ITEM_CODES B "
				+ "WHERE A.REGSTKRCD = B.ITMCD) "
				+ "ORDER BY 1,2,3,4");
		// end defect 10600

		try
		{

			Log.write(
				Log.SQL,
				this,
				" - qryPlateToSticker - SQL - Begin");
			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());
			Log.write(
				Log.SQL,
				this,
				" - qryPlateToSticker - SQL - End");

			while (lrsQry.next())
			{
				PlateToStickerData laPlateToStickerData =
					new PlateToStickerData();
				laPlateToStickerData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laPlateToStickerData.setRegStkrCd(
					caDA.getStringFromDB(lrsQry, "RegStkrCd"));
				laPlateToStickerData.setAcctItmCd(
					caDA.getStringFromDB(lrsQry, "AcctItmCd"));
				laPlateToStickerData.setRegStkrCdDesc(
					caDA.getStringFromDB(lrsQry, "ItmCdDesc"));
				// defect 10600 
				// Add DfltStkrCdIndi 
				laPlateToStickerData.setDfltStkrCdIndi( 
					caDA.getIntFromDB(lrsQry,"DfltStkrCdIndi"));
				// end defect 10600  
				laPlateToStickerData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laPlateToStickerData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));

				// Add element to the Vector
				lvRslt.addElement(laPlateToStickerData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryPlateToSticker - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryPlateToSticker - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
}
