package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.OwnershipEvidenceCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * OwnershipEvidenceCodes.java
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
 * 							qryOwnershipEvidenceCodes()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	04/03/2008	add TtlTrnsfrPnltyExmptCd to query
 * 							modify qryOwnershipEvidenceCodes()
 * 							defect 9583 Ver Defect POS A
 * K Harrell	05/21/2008	remove TtlTrnsfrPnltyExmptCd from query
 *               			modify qryOwnershipEvidenceCodes()
 * 							defect 9583 Ver Defect POS A
 * K Harrell	06/20/2008	restore TtlTrnsfrPnltyExmptCd to query
 * 							modify qryOwnershipEvidenceCodes()
 * 							defect 9724 Ver Defect POS A 
 * K Harrell	09/30/2011	Implement reference to StaticTablePilot
 * 							modify qryOwnershipEvidenceCodes() 
 * 							defect 11048 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_OWNR_EVID_CDS
 * 
 * @version	6.9.0 			09/30/2011
 * @author 	Kathy Harrell
 * <br>Creation Date:		09/04/2001 09:48:12 
 */

public class OwnershipEvidenceCodes extends OwnershipEvidenceCodesData
{
	DatabaseAccess caDA;
	
	/**
	 * OwnershipEvidenceCodes constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public OwnershipEvidenceCodes(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to Query RTS.RTS_OWNR_EVID_CDS
	 *
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryOwnershipEvidenceCodes() throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryOwnershipEvidenceCodes - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;
		
		// defect 11048 
		String lsTableCreator = CommonConstant.RTS_TBCREATOR;
		
		if (SystemProperty.isStaticTablePilot())
		{
			lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		}

		// defect 9724 
		// add TtlTrnsfrPnltyExmptCd
		lsQry.append(
			"SELECT "
				+ "OwnrshpEvidCd,"
				+ "OwnrshpEvidCdDesc,"
				+ "EvidFreqSortNo,"
				+ "EvidImprtnceSortNo,"
				+ "EvidSurrCd,"
				+ "BndedTtlCdReqd,"
				+ "PriorCCOReqd, "
				+ "TtlTrnsfrPnltyExmptCd "
				//+ "FROM RTS.RTS_OWNR_EVID_CDS");
				+ "FROM "
				+ lsTableCreator + ".RTS_OWNR_EVID_CDS");
		// end defect 9724
		// end defect 11048
		
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryOwnershipEvidenceCodes - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryOwnershipEvidenceCodes - SQL - End");

			while (lrsQry.next())
			{
				OwnershipEvidenceCodesData laOwnershipEvidenceCodesData =
					new OwnershipEvidenceCodesData();
				laOwnershipEvidenceCodesData.setOwnrshpEvidCd(
					caDA.getIntFromDB(lrsQry, "OwnrshpEvidCd"));
				laOwnershipEvidenceCodesData.setOwnrshpEvidCdDesc(
					caDA.getStringFromDB(lrsQry, "OwnrshpEvidCdDesc"));
				laOwnershipEvidenceCodesData.setEvidFreqSortNo(
					caDA.getIntFromDB(lrsQry, "EvidFreqSortNo"));
				laOwnershipEvidenceCodesData.setEvidImprtnceSortNo(
					caDA.getIntFromDB(lrsQry, "EvidImprtnceSortNo"));
				laOwnershipEvidenceCodesData.setEvidSurrCd(
					caDA.getStringFromDB(lrsQry, "EvidSurrCd"));
				laOwnershipEvidenceCodesData.setBndedTtlCdReqd(
					caDA.getIntFromDB(lrsQry, "BndedTtlCdReqd"));
				laOwnershipEvidenceCodesData.setPriorCCOReqd(
					caDA.getIntFromDB(lrsQry, "PriorCCOReqd"));
				// defect 9724 
				laOwnershipEvidenceCodesData.setTtlTrnsfrPnltyExmptCd(
					caDA.getStringFromDB(
						lrsQry,
						"TtlTrnsfrPnltyExmptCd"));
				// end defect 9724 
				// Add element to the Vector
				lvRslt.addElement(laOwnershipEvidenceCodesData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryOwnershipEvidenceCodes - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryOwnershipEvidenceCodes - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS