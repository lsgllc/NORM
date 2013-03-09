package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.VehicleClassSpclPltTypeDescriptionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * VehicleClassSpclPltTypeDesc.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/10/2007	Created
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/15/2007	Added check for MFGPNDINGINDI
 * 							modify qryVehicleClassSpclPltTypeDesc()
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/23/2007	Removed retrieval of unneeded columns
 * 							Added union for PLPDLR and PLPDLRMC
 * 							modify qryVehicleClassSpclPltTypeDesc()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/18/2007	add functionality for piloting table data
 * 							modify qryVehicleClassSpclPltTypeDesc()
 *							defect 9085 Ver Special Plates
 * K Harrell	10/16/2007	Add use of Pilot RTS_PLT_TYPE, remove use of
 * 							pilot CLASS_TO_PLT, pilot RTS_ITEM_CODES  
 * 							modify qryVehicleClassSpclPltTypeDesc() 
 * 							defect 9354 Ver Special Plates 2 	
 * K Harrell	01/17/2008	Remove reference to PILOT qualifier
 * 							modify qryVehicleClassSpclPltTypeDesc()
 * 							defect 9525 Ver 3 Amigos Prep 	
 * B Hargrove	07/11/2008	Add pilot for RTS_PLT_TYPE.  
 * 							modify qryVehicleClassSpclPltTypeDesc()
 * 							defect 9689 Ver MyPlates_POS	
 * K Harrell	09/30/2011	Remove reference to pilot qualifier
 * 							modify qryVehicleClassSpclPltTypeDesc()
 * 							defect 11048 Ver 6.9.0   
 * K Harrell	10/28/2011	Restore reference to pilot qualifier
 * 							modify qryVehicleClassSpclPltTypeDesc() 
 * 							defect 11061 Ver 6.9.0             
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access join of RTS_PLT_TYPE,
 * 	RTS_REGIS_CLASS, RTS_ITEM_CODES, CLASS_TO_PLT 
 * 
 * @version	6.9.0				10/28/2011
 * @author	Kathy Harrell
 * @since 						02/10/2007 18:27:00
 */
public class VehicleClassSpclPltTypeDesc
	extends VehicleClassSpclPltTypeDescriptionData
{
	DatabaseAccess caDA;
	
	/**
	 * VehicleClassSpclPltTypeDesc constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public VehicleClassSpclPltTypeDesc(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_PLT_TYPE, joined to 
	 * 		RTS_REGIS_CLASS, RTS_ITEM_CODES, CLASS_TO_PLT
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryVehicleClassSpclPltTypeDesc() throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryVehicleClassSpclPltTypeDesc - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 11061
		// Restore reference to PILOT qualifier
		String lsTableCreator = CommonConstant.RTS_TBCREATOR;

		if (SystemProperty.isStaticTablePilot())
		 {
		 		lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		 }

		lsQry.append(
			"SELECT DISTINCT "
				+ " C.VEHCLASSCD,"
				+ " A.REGPLTCD,"
				+ " B.ITMCDDESC AS REGPLTCDDESC,"
				+ " A.RTSEFFDATE,"
				+ " A.RTSEFFENDDATE "
				+ " FROM "
				//+ " RTS.RTS_PLT_TYPE A, "
				+ lsTableCreator + ".RTS_PLT_TYPE A, "
				+ " RTS.RTS_ITEM_CODES B,"
				//+ " RTS.RTS_REGIS_CLASS C, "
				+ lsTableCreator + ".RTS_REGIS_CLASS C, "
				+ lsTableCreator + ".RTS_CLASS_TO_PLT D"
				+ " WHERE "
				+ " C.REGCLASSCD = D.REGCLASSCD AND "
				+ " D.REGPLTCD = A.REGPLTCD AND "
				+ " A.REGPLTCD = B.ITMCD AND "
				+ " A.RTSEFFDATE = "
				+ " (SELECT MAX(RTSEFFDATE) FROM "
				//+ " RTS.RTS_PLT_TYPE H "
				+ lsTableCreator + ".RTS_PLT_TYPE H "
				+ " WHERE H.REGPLTCD = A.REGPLTCD)"
				+ " AND C.RTSEFFDATE = "
				+ " (SELECT MAX(RTSEFFDATE) FROM "
				//RTS.RTS_REGIS_CLASS J "
				+ lsTableCreator + ".RTS_REGIS_CLASS J "
				+ " WHERE J.VEHCLASSCD =  C.VEHCLASSCD AND "
				+ " J.REGCLASSCD = C.REGCLASSCD) AND"
				+ " D.RTSEFFDATE = "
				+ " (SELECT MAX(RTSEFFDATE) FROM "
				+ lsTableCreator + ".RTS_CLASS_TO_PLT K "
				+ " WHERE K.REGCLASSCD = D.REGCLASSCD AND K.REGPLTCD = D.REGPLTCD ) "
				+ " AND A.PLTOWNRSHPCD <> 'V' "
				+ " AND EXISTS (SELECT * FROM RTS.RTS_ORG_NO M WHERE "
				+ " A.BASEREGPLTCD = M.BASEREGPLTCD AND M.MFGPNDINGINDI = 0)"
				+ " UNION "
				+ " SELECT DISTINCT 'OTHER' as VEHCLASSCD,REGPLTCD,B.ITMCDDESC AS REGPLTCDDESC,"
				+ "	A.RTSEFFDATE, A.RTSEFFENDDATE  FROM "
				//+ " RTS.RTS_PLT_TYPE A,"
				+ lsTableCreator + ".RTS_PLT_TYPE A,"
				+ " RTS.RTS_ITEM_CODES B WHERE ITMCD LIKE 'PLPDLR%' AND "
				+ " A.REGPLTCD = B.ITMCD AND "
				+ " A.RTSEFFDATE = "
				+ " (SELECT MAX(RTSEFFDATE) FROM "
				//+ " RTS.RTS_PLT_TYPE H "
				+ lsTableCreator + ".RTS_PLT_TYPE H "
				+ " WHERE H.REGPLTCD = A.REGPLTCD)"
				+ " ORDER BY 1,2 ");
		// end defect 11061  


		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryVehicleClassSpclPltTypeDesc - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryVehicleClassSpclPltTypeDesc - SQL - End");
			while (lrsQry.next())
			{
				VehicleClassSpclPltTypeDescriptionData laVehClassSpclPltTypeDescriptionData =
					new VehicleClassSpclPltTypeDescriptionData();
				laVehClassSpclPltTypeDescriptionData.setVehClassCd(
					caDA.getStringFromDB(lrsQry, "VehClassCd"));
				laVehClassSpclPltTypeDescriptionData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laVehClassSpclPltTypeDescriptionData.setRegPltCdDesc(
					caDA.getStringFromDB(lrsQry, "RegPltCdDesc"));
				laVehClassSpclPltTypeDescriptionData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laVehClassSpclPltTypeDescriptionData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
				// Add element to the Vector
				lvRslt.addElement(laVehClassSpclPltTypeDescriptionData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryVehicleClassSpclPltTypeDesc - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryVehicleClassSpclPltTypeDesc - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	}
}
