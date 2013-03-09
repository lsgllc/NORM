package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.DocumentTypesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * DocumentTypes.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	04/04/2005	Remove parameter from qryDocumentTypes()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3   
 * K Harrell	04/03/2008	add TtlTrnsfrPnltyExmptCd to query
 * 							modify qryDocumentTypes()
 * 							defect 9583 Ver Defect POS A 
 * K Harrell	05/23/2008	remove TtlTrnsfrPnltyExmptCd from query
 * 							  as no longer required 
 * 							modify qryDocumentTypes()
 * 							defect 9583 Ver Defect POS A
 * K Harrell	06/20/2008	restore TtlTrnsfrPnltyExmptCd to query
 * 							modify qryDocumentTypes()
 * 							defect 9724 Ver Defect POS A
 * K Harrell	02/26/2009	Add ETtlAllowdIndi 
 * 							modify qryDocumentTypes() 
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	04/08/2009	Add DefaultETtlCd 
 *  						modify qryDocumentTypes() 
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	09/30/2011	Implement reference to StaticTablePilot
 * 							modify qryDocumentTypes() 
 * 							defect 11048 Ver 6.9.0   
 * K Harrell	02/01/2012	add DocTypeGrpCd 
 * 							modify qryDocument()
 * 							defect 11228 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_DOC_TYPES.
 * 
 * @version 6.10.0	02/01/2012	
 * @author Kathy Harrell 
 * @since			08/31/2001 14:30:10
 */

public class DocumentTypes extends DocumentTypesData
{
	DatabaseAccess caDA;

	/**
	 * DocumentTypes constructor comment.
	 * 
	 * @param aaDA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	public DocumentTypes(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_DOC_TYPES
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryDocumentTypes() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryDocumentTypes - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 11048
		String lsTableCreator = CommonConstant.RTS_TBCREATOR;
		if (SystemProperty.isStaticTablePilot())
		{
			lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		}
		
		// defect 11228 
		// add DocTypeGrpCd 
		
		// defect 9969
		// add ETtlAllowdIndi, DefaultETtlCd
		lsQry.append("SELECT " 
				+ "DocTypeCd, " 
				+ "DocTypeCdDesc, "
				+ "RegRecIndi, " 
				+ "TtlTrnsfrPnltyExmptCd, "
				+ "ETtlAllowdIndi, " 
				+ "DefaultETtlCd,  "
				+ "DocTypeGrpCd  "
				// + "FROM RTS.RTS_DOC_TYPES");
				+ "FROM "
				+ lsTableCreator + ".RTS_DOC_TYPES");
		// end defect 9969
		// end defect 11048
		// end defect 11228 

		try
		{
			Log.write(Log.SQL, this,
					" - qryDocumentTypes - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryDocumentTypes - SQL - End");

			while (lrsQry.next())
			{
				DocumentTypesData laDocumentTypesData = new DocumentTypesData();
				laDocumentTypesData.setDocTypeCd(caDA.getIntFromDB(
						lrsQry, "DocTypeCd"));
				laDocumentTypesData.setDocTypeCdDesc(caDA
						.getStringFromDB(lrsQry, "DocTypeCdDesc"));
				laDocumentTypesData.setRegRecIndi(caDA.getIntFromDB(
						lrsQry, "RegRecIndi"));
				laDocumentTypesData.setDocTypeCdDesc(caDA
						.getStringFromDB(lrsQry, "DocTypeCdDesc"));

				// defect 9724
				laDocumentTypesData.setTtlTrnsfrPnltyExmptCd(caDA
						.getStringFromDB(lrsQry,
								"TtlTrnsfrPnltyExmptCd"));
				// end defect 9724

				// defect 9969
				laDocumentTypesData.setETtlAllowdIndi(caDA
						.getIntFromDB(lrsQry, "ETtlAllowdIndi"));

				laDocumentTypesData.setDefaultETtlCd(caDA.getIntFromDB(
						lrsQry, "DefaultETtlCd"));
				// end defect 9969
				
				// defect 11228 
				laDocumentTypesData.setDocTypeGrpCd(caDA.getStringFromDB(
						lrsQry, "DocTypeGrpCd"));
				// end defect 11228 

				// Add element to the Vector
				lvRslt.addElement(laDocumentTypesData);
			} // End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryDocumentTypes - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - qryDocumentTypes - SQL Exception "
							+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - qryDocumentTypes - SQL Exception "
							+ aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	} // END OF QUERY METHOD
} // END OF CLASS
