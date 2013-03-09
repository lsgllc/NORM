package com.txdot.isd.rts.server.db;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.OrganizationNumberData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * OrganizationNumber.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/31/2007	Created
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/08/2007	Added join to RTS_PLT_GRP_ID to retrieve
 * 							 Organization Name  (PltGrpDesc)
 * 							Only retrieve where MfgPndingIndi = 0
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/16/2010	Add TermBasedIndi 
 * 							modify qryOrgNo()
 * 							defect 10366 Ver POS_640
 * J Zwiener	01/24/2011	Add RestyleAcctItmCd
 * 							modify qryOrgNo()
 * 							defect 10627 Ver POS_670
 * J Zwiener	02/01/2011	Add CrossoverIndi, CrossoverPosDate
 * 							modify qryOrgNo()
 * 							defect 10704 Ver POS_670
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_ORG_NO 
 *
 * @version	POS_670 		02/01/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/31/2007 17:25:00
 */
public class OrganizationNumber extends OrganizationNumberData
{
	DatabaseAccess caDA;

	/**
	 * OrganizationNumber constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public OrganizationNumber(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS_ORG_NO 
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryOrgNo() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryOrgNo - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 10627 & 10704
		// add RestyleAcctItmCd, CrossoverIndi, CrossoverPosDate
		// defect 10366
		// add TermBasedIndi
		lsQry.append(
			"Select "
				+ " BASEREGPLTCD,"
				+ " ORGNO,"
				+ " PLTGRPDESC as ORGNODESC,"
				+ " RTSEFFDATE,"
				+ " RTSEFFENDDATE,"
				+ " ACCTITMCD,"
				+ " MFGPNDINGINDI,"
				+ " SPONSORPLTGRPID,"
				+ " SUNSETDATE, "
				+ " TERMBASEDINDI, "
				+ " RESTYLEACCTITMCD, "
				+ " CROSSOVERINDI, "
				+ " CROSSOVERPOSDATE "
				+ " FROM RTS.RTS_ORG_NO A,"
				+ " RTS.RTS_PLT_GRP_ID B WHERE "
				+ " A.SPONSORPLTGRPID = "
				+ " B.PLTGRPID AND MFGPNDINGINDI =0");
		// end defect 10366
		// end defect 10627 & 10704

		try
		{
			Log.write(Log.SQL, this, " - qryOrgNo - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryOrgNo - SQL - End");

			while (lrsQry.next())
			{
				OrganizationNumberData laOrganizationNumberData =
					new OrganizationNumberData();
				laOrganizationNumberData.setBaseRegPltCd(
					caDA.getStringFromDB(lrsQry, "BASEREGPLTCD"));
				laOrganizationNumberData.setOrgNo(
					caDA.getStringFromDB(lrsQry, "ORGNO"));
				laOrganizationNumberData.setOrgNoDesc(
					caDA.getStringFromDB(lrsQry, "ORGNODESC"));
				laOrganizationNumberData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEFFDATE"));
				laOrganizationNumberData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEFFENDDATE"));
				laOrganizationNumberData.setAcctItmCd(
					caDA.getStringFromDB(lrsQry, "ACCTITMCD"));
				laOrganizationNumberData.setMfgPndingIndi(
					caDA.getIntFromDB(lrsQry, "MFGPNDINGINDI"));
				laOrganizationNumberData.setSponsorPltGrpId(
					caDA.getStringFromDB(lrsQry, "SPONSORPLTGRPID"));
				laOrganizationNumberData.setSunsetDate(
					caDA.getIntFromDB(lrsQry, "SUNSETDATE"));

				// defect 10366 	
				laOrganizationNumberData.setTermBasedIndi(
					caDA.getIntFromDB(lrsQry, "TERMBASEDINDI"));
				// end defect 10366 
				
				// defect 10627 & 10704
				laOrganizationNumberData.setRestyleAcctItmCd(
					caDA.getStringFromDB(lrsQry, "RESTYLEACCTITMCD"));
				laOrganizationNumberData.setCrossoverIndi(
					caDA.getIntFromDB(lrsQry, "CROSSOVERINDI"));
				laOrganizationNumberData.setCrossoverPosDate(
					caDA.getIntFromDB(lrsQry, "CROSSOVERPOSDATE"));
				// end defect 10627 & 10704

				// Add element to the Vector
				lvRslt.addElement(laOrganizationNumberData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryOrgNo - End ");
			return (lvRslt);
		}
		// defect 10366 
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"- qryOrgNo - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		// end defect 10366
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryOrgNo - SQL Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS 
