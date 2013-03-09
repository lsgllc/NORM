package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.SpecialPlateFixedExpirationMonthData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * SpecialPlateFixedExpirationMonth
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/26/2007	Created
 *							defect 9085 Ver Special Plates
 * K Harrell	04/20/2007	add functionality for piloting table data
 * 							modify qrySpecialPlateFixedExpirationMonth()
 *							defect 9085 Ver Special Plates
 * K Harrell	10/14/2007	Add use of Pilot.RTS_PLT_TYPE, remove use of
 * 							pilot CLASS_TO_PLT 
 * 							modify qrySpecialPlateFixedExpirationMonth() 
 * 							defect 9354 Ver Special Plates 2
 * K Harrell	01/17/2008	Remove reference to Pilot qualifier
 * 							modify qrySpecialPlateFixedExpirationMonth()
 * 							defect 9525 Ver 3 Amigos Prep  		
 * B Hargrove	07/11/2008	Add pilot for RTS_PLT_TYPE.  
 * 							modify qrySpecialPlateFixedExpirationMonth()
 * 							defect 9689 Ver MyPlates_POS	
 * K Harrell	09/30/2011	Remove reference to Pilot qualifier
 * 							modify qrySpecialPlateFixedExpirationMonth()
 * 							defect 11048 Ver 6.9.0  
 * K Harrell	10/28/2011	Restore reference to Pilot qualifier
 * 							modify qrySpecialPlateFixedExpirationMonth()
 * 							defect 11061 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for
 * SpecialPlateTransactionHistory
 * 
 * @version 6.9.0		10/28/2011
 * @author Kathy Harrell 
 * @since				03/26/2007 16:21:00
 */
public class SpecialPlateFixedExpirationMonth
{

	DatabaseAccess caDA;

	/**
	 * SpecialPlateFixedExpirationMonth constructor comment.
	 * 
	 * @param aaDA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	public SpecialPlateFixedExpirationMonth(DatabaseAccess aaDA)
			throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query from RTS.RTS_SPCL_PLT_TRANS_HSTRY
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qrySpecialPlateFixedExpirationMonth()
			throws RTSException
	{
		Log.write(Log.METHOD, this,
				" - qrySpecialPlateFixedExpirationMonth - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		String lsTableCreator = CommonConstant.RTS_TBCREATOR;
		
		// defect 11061
		 if (SystemProperty.isStaticTablePilot())
		 {
			 lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		 }
		// end defect 11061

		lsQry
				.append("SELECT DISTINCT A.RTSEFFDATE,A.RTSEFFENDDATE,A.REGPLTCD,"
						+ " FXDEXPMO FROM "
						+ lsTableCreator
						+ ".RTS_CLASS_TO_PLT A, RTS.RTS_COMMON_FEES B , "
						// + " RTS.RTS_PLT_TYPE C WHERE "
						+ lsTableCreator
						+ ".RTS_PLT_TYPE C WHERE "
						+ " A.REGCLASSCD = B.REGCLASSCD AND "
						+ " A.REGPLTCD = C.REGPLTCD  AND "
						+ " B.FXDEXPMO <> 0 AND "
						+ " C.PLTOWNRSHPCD NOT IN ('V') AND "
						+ " A.RTSEFFENDDATE = B.RTSEFFENDDATE AND "
						+ " B.RTSEFFENDDATE = C.RTSEFFENDDATE ORDER BY 3,1");

		try
		{
			Log.write(Log.SQL, this,
							" - qrySpecialPlateFixedExpirationMonth - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this,
							" - qrySpecialPlateFixedExpirationMonth - SQL - End");

			while (lrsQry.next())
			{
				SpecialPlateFixedExpirationMonthData laSpecialPlateFixedExpirationMonthData = new SpecialPlateFixedExpirationMonthData();
				laSpecialPlateFixedExpirationMonthData
						.setRTSEffDate(caDA.getIntFromDB(lrsQry,
								"RTSEffDate"));
				laSpecialPlateFixedExpirationMonthData
						.setRTSEffEndDate(caDA.getIntFromDB(lrsQry,
								"RTSEffEndDate"));
				laSpecialPlateFixedExpirationMonthData.setFxdExpMo(caDA
						.getIntFromDB(lrsQry, "FxdExpMo"));
				laSpecialPlateFixedExpirationMonthData.setRegPltCd(caDA
						.getStringFromDB(lrsQry, "RegPltCd"));
				// Add element to the Vector
				lvRslt
						.addElement(laSpecialPlateFixedExpirationMonthData);
			} // End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this,
					" - qrySpecialPlateFixedExpirationMonth - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - qrySpecialPlateFixedExpirationMonth - Exception "
							+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} // END OF QUERY METHOD
} // END OF CLASS
