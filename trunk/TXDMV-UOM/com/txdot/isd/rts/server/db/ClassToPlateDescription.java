package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.ClassToPlateDescriptionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * ClassToPltDescription.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/14/2005	Created
 * 							defect 8218  Ver 5.2.3 
 * K Harrell	04/18/2007	add functionality for piloting table data
 * 							modify qryClassToPlateDescription() 
 *							defect 9085 Ver Special Plates
 * K Harrell	10/16/2007	do not reference pilot table data
 * 							modify qryClassToPlateDescription() 
 *							defect 9354 Ver Special Plates 2 	 		
 * B Hargrove	09/23/2010	Add new DfltPltCdIndi column
 * 							modify qryClassToPlateDescription()
 * 							defect 10600 Ver 6.6.0
 * K Harrell	10/28/2011	Restore reference to Pilot 
 * 							modify qryClassToPlateDescription()
 * 							defect 11061 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access results of RTS.RTS_CLASS_TO_PLT
 * joined to RTS.RTS_ITEM_CODES.
 *
 * @version	6.9.0				10/28/2011
 * @author	Kathy Harrell
 * <br>Creation Date:			06/14/2005	16:22:00
 */
public class ClassToPlateDescription
{
	DatabaseAccess caDA;

	/**
	 * ClassToPlateDescription constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public ClassToPlateDescription(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_CLASS_TO_PLT joined to
	 * RTS.RTS_ITEM_CODES 
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryClassToPlateDescription() throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryClassToPlateDescription - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;


		// defect 11061
		// Restore reference to Pilot
		String lsTableCreator = CommonConstant.RTS_TBCREATOR;  
		if (SystemProperty.isStaticTablePilot())
		{
			lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		}
		// end defect 11061 

		// defect 10600
		// add DfltPltCdIndi
		lsQry.append(
			"SELECT Distinct "
				+ "RegClassCd,"
				+ "RegPltCd,"
				+ "ItmCdDesc, "
				+ "DfltPltCdIndi,"
				+ "RTSEffDate,"
				+ "RTSEffEndDate "
				+ "FROM "
				+ lsTableCreator + ".RTS_CLASS_TO_PLT A, "
				+ "RTS.RTS_ITEM_CODES B "
				+ "WHERE A.REGPLTCD = B.ITMCD ORDER BY 1,2,3");
		// end defect 10600				

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryClassToPlateDescription - SQL - Begin");

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			Log.write(
				Log.SQL,
				this,
				" - qryClassToPlateDescription - SQL - End");

			while (lrsQry.next())
			{
				ClassToPlateDescriptionData laClassToPlateDescData =
					new ClassToPlateDescriptionData();
				laClassToPlateDescData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laClassToPlateDescData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laClassToPlateDescData.setItmCdDesc(
					caDA.getStringFromDB(lrsQry, "ItmCdDesc"));
				// defect 10600 
				// Add DfltPltCdIndi 
				laClassToPlateDescData.setDfltPltCdIndi( 
					caDA.getIntFromDB(lrsQry,"DfltPltCdIndi"));
				// end defect 10600  
				laClassToPlateDescData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laClassToPlateDescData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));

				// Add element to the Vector
				lvRslt.addElement(laClassToPlateDescData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryClassToPlateDescription - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryClassToPlateDescription - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}

	} //END OF QUERY METHOD
}
