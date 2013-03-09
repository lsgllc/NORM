package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.ClassToPlateData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * ClassToPlate.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/14/2005	Created
 * 							defect 8218  Ver 5.2.3 
 * K Harrell	04/18/2007	add functionality for piloting table data
 * 							modify qryClassToPlate() 
 *							defect 9085 Ver Special Plates 	
 * K Harrell	10/16/2007	do not reference pilot table data
 * 							modify qryClassToPlate() 
 *							defect 9354 Ver Special Plates 2
 * K Harrell	01/17/2008	Add New PTOElgbleIndi column
 * 							modify qryClassToPlate() 
 * 							defect 9524 Ver 3 Amigos Prep  						
 * B Hargrove	09/23/2010	Add new DfltPltCdIndi column
 * 							modify qryClassToPlate()
 * 							defect 10600 Ver 6.6.0
 * K Harrell	10/28/2011	Restore reference to pilot table 
 * 							modify qryClassToPlate()
 * 							defect 11061 Ver 6.9.0
 * K Harrell	01/10/2012	add VTPElgbleIndi 
 * 							modify qryClassToPlate() 
 * 							defect 11231 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS.RTS_CLASS_TO_PLT. 
 *
 * @version	6.10.0				01/10/2012
 * @author	Kathy Harrell
 * <br>Creation Date:			06/14/2005 15:50:07 
 */

public class ClassToPlate extends ClassToPlateData
{
	DatabaseAccess caDA;

	/**
	 * ClassToPlate constructor comment.
	 * 
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException 
	 */
	public ClassToPlate(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to Query RTS.RTS_CLASS_TO_PLT 
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryClassToPlate() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryClassToPlate - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 9524 
		// Add PTOElgbleIndi 
		
		// defect 11061
		String lsTableCreator = CommonConstant.RTS_TBCREATOR;  
		if (SystemProperty.isStaticTablePilot())
		{
			lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		}
		// end defect 11061
		
		// defect 11231 
		// Add VTPElgbleIndi 
		
		
		// defect 10600
		// add DfltPltCdIndi
		lsQry.append(
			"SELECT "
				+ "RegClassCd, "
				+ "RegPltCd, "
				+ "ReplPltCd, "
				+ "PTOElgbleIndi,"
				+ "DfltPltCdIndi,"
				+ "VTPElgbleIndi," 
				+ "RTSEffDate, "
				+ "RTSEffEndDate "
				+ "FROM "
				+ lsTableCreator
				+ ".RTS_CLASS_TO_PLT "
				+ "ORDER BY 1,2,3,4");
		// end defect 10600
		// end defect 9524 
		// end defect 11231 

		try
		{

			Log.write(
				Log.SQL,
				this,
				" - qryClassToPlate - SQL - Begin");

			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());

			Log.write(Log.SQL, this, " - qryClassToPlate - SQL - End");

			while (lrsQry.next())
			{
				ClassToPlateData laClassToPlateData =
					new ClassToPlateData();
				laClassToPlateData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laClassToPlateData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laClassToPlateData.setReplPltCd(
					caDA.getStringFromDB(lrsQry, "ReplPltCd"));
				// defect 9524 
				// Add PTOElgbleIndi 
				laClassToPlateData.setPTOElgbleIndi( 
					caDA.getIntFromDB(lrsQry,"PTOElgbleIndi"));
				// end defect 9524  
				// defect 10600 
				// Add DfltPltCdIndi 
				laClassToPlateData.setDfltPltCdIndi( 
					caDA.getIntFromDB(lrsQry,"DfltPltCdIndi"));
				// end defect 10600  
				
				// defect 11231  
				// Add VTPElgbleIndi 
				laClassToPlateData.setVTPElgbleIndi( 
					caDA.getIntFromDB(lrsQry,"VTPElgbleIndi"));
				// end defect 11231  
				
				laClassToPlateData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laClassToPlateData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));

				// Add element to the Vector
				lvRslt.addElement(laClassToPlateData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryClassToPlate - End ");
			return lvRslt;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryClassToPlate - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryClassToPlate - Exception "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}

	} //END OF QUERY METHOD
}
