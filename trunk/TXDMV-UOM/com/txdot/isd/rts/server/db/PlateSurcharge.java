package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.PlateSurchargeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * PlateSurcharge.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/31/2007	Created
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/08/2007	Missing set of ApplIndi 
 * 							modify qryPlateSurcharge()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	10/14/2007	Use Pilot for PltSurcharge Data
 * 							modify qryPlateSurcharge()
 * 							defect 9354 Ver Special Plates 2
 * K Harrell	01/17/2007	Remove reference to PILOT qualifier 
 * 							modify qryPlateSurcharge()
 * 							defect 9525 Ver 3 Amigos Prep   
 * K Harrell	02/03/2010	Add PltValidityTerm
 * 							modify qryPlateSurcharge()
 * 							defect 10366 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_PLT_SURCHARGE 
 *
 * @version	POS_640 		02/03/2010 
 * @author	Kathy Harrell
 * <br>Creation Date:		01/31/2007 17:25:00
 */
public class PlateSurcharge extends PlateSurchargeData
{
	DatabaseAccess caDA;

	/**
	 * PlateSurcharge constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public PlateSurcharge(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS_PLT_SURCHARGE 
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryPlateSurcharge() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryPlateSurcharge - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 10366
		// Add PltValidityTerm  
		lsQry.append(
			"SELECT "
				+ " REGPLTCD,"
				+ " ORGNO,"
				+ " ADDLSETINDI,"
				+ " APPLINDI,"
				+ " RTSEFFDATE,"
				+ " RTSEFFENDDATE,"
				+ " ACCTITMCD,"
				+ " PLTSURCHARGEFEE, "
				+ " PLTVALIDITYTERM "
				+ " FROM "
				+ " RTS.RTS_PLT_SURCHARGE");
		// end defect 10366 

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryPlateSurcharge - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);

			Log.write(
				Log.SQL,
				this,
				" - qryPlateSurcharge - SQL - End");

			while (lrsQry.next())
			{
				PlateSurchargeData laPlateSurchargeData =
					new PlateSurchargeData();
				laPlateSurchargeData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laPlateSurchargeData.setOrgNo(
					caDA.getStringFromDB(lrsQry, "ORGNO"));
				laPlateSurchargeData.setAddlSetIndi(
					caDA.getIntFromDB(lrsQry, "ADDLSETINDI"));
				laPlateSurchargeData.setApplIndi(
					caDA.getIntFromDB(lrsQry, "APPLINDI"));
				laPlateSurchargeData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEFFDATE"));
				laPlateSurchargeData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEFFENDDATE"));
				laPlateSurchargeData.setAcctItmCd(
					caDA.getStringFromDB(lrsQry, "ACCTITMCD"));
				laPlateSurchargeData.setPltSurchargeFee(
					caDA.getDollarFromDB(lrsQry, "PLTSURCHARGEFEE"));

				// defect 10366 
				laPlateSurchargeData.setPltValidityTerm(
					caDA.getIntFromDB(lrsQry, "PLTVALIDITYTERM"));
				// end defect 10366 

				// Add element to the Vector
				lvRslt.addElement(laPlateSurchargeData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryPlateSurcharge - End ");
			return (lvRslt);
		}
		// defect 10366 
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"- qryPlateSurcharge - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		// end defect 10366
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryPlateSurcharge - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS 