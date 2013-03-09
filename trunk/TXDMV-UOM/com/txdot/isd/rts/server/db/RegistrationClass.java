package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.RegistrationClassData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * RegistrationClass.java
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
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from 
 * 							qryRegistrationClass()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	05/17/2005	rename RegistrationClassData.getDiesleReqd()
 * 							to getDieselReqd()
 * 							defect 7786  Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3 
 * K Harrell	04/03/2008	add TtlTrnsfrPnltyExmptCd to query
 * 							modify qryRegistrationClass()
 * 							defect 9583 Ver Defect POS A
 * K Harrell	05/23/2008	remove TtlTrnsfrPnltyExmptCd from query
 * 							  as no longer required.
 * 							modify qryRegistrationClass()
 * 							defect 9583 Ver Defect POS A 
 * K Harrell	06/20/2008	restore TtlTrnsfrPnltyExmptCd to query
 * 							modify qryRegistrationClass()
 * 							defect 9724 Ver Defect POS A 
 * B Hargrove	09/23/2010	Add new DfltRegClassCdIndi column
 * 							modify qryRegistrationClass()
 * 							defect 10600 Ver 6.6.0
 * K Harrell	09/30/2011	Implement reference to StaticTablePilot
 * 							modify qryRegistrationClass()
 * 							defect 11048 Ver 6.9.0   
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_REGIS_CLASS
 * 
 * @version	6.9.0			09/30/2011
 * @author 	Kathy Harrell
 * @since					08/31/2001 17:22:23 
 */

public class RegistrationClass extends RegistrationClassData
{
	DatabaseAccess caDA;

	/**
	 * RegistrationClass constructor comment.
	 *
	 * @param  aaaDA DatabaseAccess
	 * @throws RTSException
	 */
	public RegistrationClass(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_REGIS_CLASS
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryRegistrationClass() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryRegistrationClass - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 11048 
		String lsTableCreator = CommonConstant.RTS_TBCREATOR;
		
		if (SystemProperty.isStaticTablePilot())
		{
			lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		}

		// defect 10600
		// add DfltRegClassCdIndi 
		lsQry.append(
			"SELECT "
				+ "VehClassCd,"
				+ "RegClassCd,"
				+ "VINAIndi,"
				+ "BdyVinReqd,"
				+ "OdmtrReqd,"
				+ "EmptyWtReqd,"
				+ "CaryngCapReqd,"
				+ "TonReqd,"
				+ "FxdWtReqd,"
				+ "LngthReqd,"
				+ "WidthReqd, "
				+ "TrlrTypeReqd,"
				+ "ToknTrlrFeeReqd,"
				+ "DieselReqd,"
				+ "PrmtToMoveReqd,"
				+ "TrlrWtValidReqd,"
				+ "CaryngCapValidReqd,"
				+ "TrlrCapValidReqd,"
				+ "HvyVehUseWt,"
				+ "FarmTrlrMinWt,"
				+ "FarmTrlrMaxWt,"
				+ "DfltRegClassCdIndi ,"
				+ "RTSEffDate,"
				+ "RTSEffEndDate, "
				+ "TtlTrnsfrPnltyExmptCd "
				+ "FROM " 
				// "RTS.RTS_REGIS_CLASS
				+ lsTableCreator + ".RTS_REGIS_CLASS");
		// end defect 10600
		// end defect 11048 

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryRegistrationClass - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryRegistrationClass - SQL - End");

			while (lrsQry.next())
			{
				RegistrationClassData laRegistrationClassData =
					new RegistrationClassData();
				laRegistrationClassData.setVehClassCd(
					caDA.getStringFromDB(lrsQry, "VehClassCd"));
				laRegistrationClassData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laRegistrationClassData.setVINAIndi(
					caDA.getIntFromDB(lrsQry, "VINAIndi"));
				laRegistrationClassData.setBdyVinReqd(
					caDA.getIntFromDB(lrsQry, "BdyVinReqd"));
				laRegistrationClassData.setOdmtrReqd(
					caDA.getIntFromDB(lrsQry, "OdmtrReqd"));
				laRegistrationClassData.setEmptyWtReqd(
					caDA.getIntFromDB(lrsQry, "EmptyWtReqd"));
				laRegistrationClassData.setCaryngCapReqd(
					caDA.getIntFromDB(lrsQry, "CaryngCapReqd"));
				laRegistrationClassData.setTonReqd(
					caDA.getIntFromDB(lrsQry, "TonReqd"));
				laRegistrationClassData.setFxdWtReqd(
					caDA.getIntFromDB(lrsQry, "FxdWtReqd"));
				laRegistrationClassData.setLngthReqd(
					caDA.getIntFromDB(lrsQry, "LngthReqd"));
				laRegistrationClassData.setWidthReqd(
					caDA.getIntFromDB(lrsQry, "WidthReqd"));
				laRegistrationClassData.setTrlrTypeReqd(
					caDA.getIntFromDB(lrsQry, "TrlrTypeReqd"));
				laRegistrationClassData.setToknTrlrFeeReqd(
					caDA.getIntFromDB(lrsQry, "ToknTrlrFeeReqd"));
				laRegistrationClassData.setDieselReqd(
					caDA.getIntFromDB(lrsQry, "DieselReqd"));
				laRegistrationClassData.setPrmtToMoveReqd(
					caDA.getIntFromDB(lrsQry, "PrmtToMoveReqd"));
				laRegistrationClassData.setTrlrWtValidReqd(
					caDA.getIntFromDB(lrsQry, "TrlrWtValidReqd"));
				laRegistrationClassData.setCaryngCapValidReqd(
					caDA.getIntFromDB(lrsQry, "CaryngCapValidReqd"));
				laRegistrationClassData.setTrlrCapValidReqd(
					caDA.getIntFromDB(lrsQry, "TrlrCapValidReqd"));
				laRegistrationClassData.setHvyVehUseWt(
					caDA.getIntFromDB(lrsQry, "HvyVehUseWt"));
				laRegistrationClassData.setFarmTrlrMinWt(
					caDA.getIntFromDB(lrsQry, "FarmTrlrMinWt"));
				laRegistrationClassData.setFarmTrlrMaxWt(
					caDA.getIntFromDB(lrsQry, "FarmTrlrMaxWt"));
				// defect 10600 
				// Add DfltRegClassCdIndi 
				laRegistrationClassData.setDfltRegClassCdIndi( 
					caDA.getIntFromDB(lrsQry,"DfltRegClassCdIndi"));
				// end defect 10600  
				laRegistrationClassData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laRegistrationClassData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
				// defect 9724 
				laRegistrationClassData.setTtlTrnsfrPnltyExmptCd(
					caDA.getStringFromDB(
						lrsQry,
						"TtlTrnsfrPnltyExmptCd"));
				// end defect 9724 
				// Add element to the Vector
				lvRslt.addElement(laRegistrationClassData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryRegistrationClass - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryRegistrationClass - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
