package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.CommonFeesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * CommonFees.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks      07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * K Harrell	01/16/2003	Added InsProofReqrdIndi
 *							defect 5271
 * K Harrell	04/04/2005	Remove references to REGADDLFEE, ADDLFEEITMCD
 *							modify qryCommonFees()
 *							defect 8104 Ver 5.2.2 Fix 4
 * K Harrell	04/04/2005	Remove parameter from qryCommonFees()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	01/13/2006	Add EmissionsPrcnt
 *							modify qryCommonFees()
 * 							defect 8514 Ver 5.2.3
 * K Harrell	09/24/2007	Use Pilot version of RTS_COMMON_FEES
 * 							modify qryCommonFees()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	10/16/2007	do not reference pilot table data
 * 							modify qryCommonFees() 
 *							defect 9354 Ver Special Plates 2 	 
 * B Hargrove	01/06/2009	Add TempAddlWtAllowdIndi 
 * 							modify qryCommonFees()
 * 							defect 9129 Ver Defect_POS_D 
 * K Harrell	12/08/2010	add RegClassFeeGrpCd 	
 * 							modify qryCommonFees()
 * 							defect 10695 Ver 6.7.0
 * K Harrell	01/07/2011	add csMethod 
 * 							add IVTRSRegNotAllowdIndi, RegClassMinWt, 
 * 							 RegClassMaxWt
 *    						  modify qryCommonFees()
 * 							defect 10695 Ver 6.7.0
 * K Harrell	01/12/2011	RegClassFeeGrpCd to String
 * 							defect 10695 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_COMMON_FEES
 * 
 * @version	6.7.0  				01/12/2011
 * @author	Kathy Harrell
 * <br>Creation Date:			08/31/2001 17:05:02 
 */

public class CommonFees extends CommonFeesData
{
	// defect 10695
	private String csMethod;
	// end defect 10695 

	DatabaseAccess caDA;

	/**
	 * CommonFees constructor comment.
	 * 
	 * @param  aaDA 
	 * @throws RTSException 
	 */
	public CommonFees(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS_COMMON_FEES
	 *
	 * @return Vector 	
	 * @throws RTSException 
	 */
	public Vector qryCommonFees() throws RTSException
	{
		csMethod = "qryCommonFees";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 10695 
		// add RegClassFeeGrpCd, IVTRSRegNotAllowdIndi, RegClassMinWt, 
		//   RegClassMaxWt,SPVCkReqrdIndi

		// defect 9354
		// Do not reference Pilot version
		// String lsTableCreator = CommonConstant.RTS_TBCREATOR;
		//		if (SystemProperty.isStaticTablePilot())
		//		{
		//			lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		//		}

		// defect 9129
		// add TempAddlWtAllowdIndi
		lsQry.append(
			"SELECT "
				+ "RegClassCd,"
				+ "RTSEffDate,"
				+ "RTSEffEndDate,"
				+ "RegClassCdDesc,"
				+ "FeeCalcCat,"
				+ "MaxallowbleRegMo,"
				+ "RegPrortnIncrmnt,"
				+ "RegPnltyFeePrcnt,"
				+ "RegPeriodLngth,"
				+ "MinRegFee,"
				+ "ReflectnFee,"
				+ "RegFee,"
				+ "DieselFeePrcnt,"
				+ "DieselChrgTonIndi,"
				+ "AddOnFeeExmptIndi,"
				+ "PltToOwnrIndi,"
				+ "RegTrnsfrIndi,"
				+ "MaxMYrPeriodLngth,"
				+ "MoAdjOptIndi,"
				+ "FxdExpMo,"
				+ "FxdExpYr, "
				+ "EmissionsPrcnt, "
				+ "InsProofReqrdIndi, "
				+ "TempAddlWtAllowdIndi, "
				+ "RegClassFeeGrpCd, "
				+ "IVTRSRegNotAllowdIndi, "
				+ " RegClassMinWt, "
				+ " RegClassMaxWt, "
				+ " SPVCkReqrdIndi "
				+ "FROM "
				+ "RTS.RTS_COMMON_FEES");
		// end defect 9129
		// end defect 9354
		// end defect 10695 

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				CommonFeesData laCommonFeesData = new CommonFeesData();
				laCommonFeesData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laCommonFeesData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laCommonFeesData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
				laCommonFeesData.setRegClassCdDesc(
					caDA.getStringFromDB(lrsQry, "RegClassCdDesc"));
				laCommonFeesData.setFeeCalcCat(
					caDA.getIntFromDB(lrsQry, "FeeCalcCat"));
				laCommonFeesData.setMaxallowbleRegMo(
					caDA.getIntFromDB(lrsQry, "MaxallowbleRegMo"));
				laCommonFeesData.setRegPrortnIncrmnt(
					caDA.getIntFromDB(lrsQry, "RegPrortnIncrmnt"));
				laCommonFeesData.setRegPnltyFeePrcnt(
					caDA.getIntFromDB(lrsQry, "RegPnltyFeePrcnt"));
				laCommonFeesData.setRegPeriodLngth(
					caDA.getIntFromDB(lrsQry, "RegPeriodLngth"));
				laCommonFeesData.setMinRegFee(
					caDA.getDollarFromDB(lrsQry, "MinRegFee"));
				laCommonFeesData.setReflectnFee(
					caDA.getDollarFromDB(lrsQry, "ReflectnFee"));
				laCommonFeesData.setRegFee(
					caDA.getDollarFromDB(lrsQry, "RegFee"));
				laCommonFeesData.setDieselFeePrcnt(
					caDA.getIntFromDB(lrsQry, "DieselFeePrcnt"));
				laCommonFeesData.setDieselChrgTonIndi(
					caDA.getIntFromDB(lrsQry, "DieselChrgTonIndi"));
				laCommonFeesData.setAddOnFeeExmptIndi(
					caDA.getIntFromDB(lrsQry, "AddOnFeeExmptIndi"));
				laCommonFeesData.setPltToOwnrIndi(
					caDA.getIntFromDB(lrsQry, "PltToOwnrIndi"));
				laCommonFeesData.setRegTrnsfrIndi(
					caDA.getIntFromDB(lrsQry, "RegTrnsfrIndi"));
				laCommonFeesData.setMaxMYrPeriodLngth(
					caDA.getIntFromDB(lrsQry, "MaxMYrPeriodLngth"));
				laCommonFeesData.setMoAdjOptIndi(
					caDA.getIntFromDB(lrsQry, "MoAdjOptIndi"));
				laCommonFeesData.setFxdExpMo(
					caDA.getIntFromDB(lrsQry, "FxdExpMo"));
				laCommonFeesData.setFxdExpYr(
					caDA.getIntFromDB(lrsQry, "FxdExpYr"));
				//	defect 8514 
				laCommonFeesData.setEmissionsPrcnt(
					caDA.getDollarFromDB(lrsQry, "EmissionsPrcnt"));
				// end defect 8514	
				laCommonFeesData.setInsProofReqrdIndi(
					caDA.getIntFromDB(lrsQry, "InsProofReqrdIndi"));
				// defect 9129
				laCommonFeesData.setTempAddlWtAllowdIndi(
					caDA.getIntFromDB(lrsQry, "TempAddlWtAllowdIndi"));
				// end defect 9129

				// defect 10695 
				laCommonFeesData.setRegClassFeeGrpCd(
					caDA.getStringFromDB(lrsQry, "RegClassFeeGrpCd"));
				laCommonFeesData.setIVTRSRegNotAllowdIndi(
					caDA.getIntFromDB(lrsQry, "IVTRSRegNotAllowdIndi"));
				laCommonFeesData.setRegClassMinWt(
					caDA.getIntFromDB(lrsQry, "RegClassMinWt"));
				laCommonFeesData.setRegClassMaxWt(
					caDA.getIntFromDB(lrsQry, "RegClassMaxWt"));
				laCommonFeesData.setSPVCkReqrdIndi(
					caDA.getIntFromDB(lrsQry, "SPVCkReqrdIndi"));
				// end defect 10695 

				// Add element to the Vector
				lvRslt.addElement(laCommonFeesData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - SQL Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
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
