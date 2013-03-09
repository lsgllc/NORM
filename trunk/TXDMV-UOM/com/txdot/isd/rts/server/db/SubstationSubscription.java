package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.SubstationSubscriptionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * SubstationSubscription.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/18/2002	Updated for AdminCache
 * K Harrell	05/02/2002	PCR 3749-Corrected updSubstationSubscription
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 * 							query
 * K Harrell	03/18/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3
 * K Harrell	05/13/2008	Add capability to select for all county 
 * 							substations 
 * 							modify qrySubstationSubscription()
 * 							defect 9488 Ver POS Defect A   
 * K Harrell	09/02/2008	No longer update Admin Cache from this class
 * 							modify updSubstationSubscription()
 * 							defect 8721 Ver Defect_POS_B
 * K Harrell	03/01/2010	Remove ChngTimestmp parameter when query
 * 							  SubstationsSubscription 
 * 							modify qrySubstationSubscription() 
 * 							defect 10186 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_SUBSTA_SUBSCR
 * 
 * @version	POS_640			03/01/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/12/2001 15:49:38 
 */

public class SubstationSubscription extends SubstationSubscriptionData
{
	DatabaseAccess caDA;

	public SubstationSubscription(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	* Method to Query RTS.RTS_SUBSTA_SUBSCR
	* 
	* @param  aaSubstaSubscrData  SubstationSubscriptionData	
	* @return Vector
	* @throws RTSException 	
	*/

	public Vector qrySubstationSubscription(SubstationSubscriptionData aaSubstaSubscrData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qrySubstationSubscription - Begin");
		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();

		ResultSet lrsQry;
		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TblName,"
				+ "TblSubstaId,"
				+ "TblUpdtIndi,"
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_SUBSTA_SUBSCR where OfcIssuanceno = ? ");

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSubscrData.getOfcIssuanceNo())));

			if (aaSubstaSubscrData.getSubstaId() != Integer.MIN_VALUE)
			{
				lsQry.append(" and SubstaId = ? ");

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSubstaSubscrData.getSubstaId())));
			}

			// defect 10186 
			//	if (aaSubstaSubscrData.getChngTimestmp() != null)
			//	{
			//		lsQry.append(" and ChngTimestmp > ? ");
			//		lvValues.addElement(
			//			new DBValue(
			//				Types.TIMESTAMP,
			//				DatabaseAccess.convertToString(
			//					aaSubstaSubscrData.getChngTimestmp())));
			//			}
			// end defect 10186

			lsQry.append(" order by 1,2,3 ");

			Log.write(
				Log.SQL,
				this,
				" - qrySubstationSubscription - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(
				Log.SQL,
				this,
				" - qrySubstationSubscription - SQL - End");
			while (lrsQry.next())
			{
				SubstationSubscriptionData laSubstationSubscriptionData =
					new SubstationSubscriptionData();
				laSubstationSubscriptionData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laSubstationSubscriptionData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laSubstationSubscriptionData.setTblName(
					caDA.getStringFromDB(lrsQry, "TblName"));
				laSubstationSubscriptionData.setTblSubstaId(
					caDA.getIntFromDB(lrsQry, "TblSubstaId"));
				laSubstationSubscriptionData.setTblUpdtIndi(
					caDA.getIntFromDB(lrsQry, "TblUpdtIndi"));
				laSubstationSubscriptionData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laSubstationSubscriptionData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qrySubstationSubscription - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySubstationSubscription - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	* Method to Query RTS_SUBSTA_SUBSCR
	* 
	* @param 	aaGeneralSearchData  GeneralSearchData	
	* @return   Vector 	
	* @throws	RTSException
	*/
	public int qrySubstationSubscriptionSecurity(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qrySubstationSubscriptionSecurity - Begin");
		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		int liTblSubstaId = 999;

		ResultSet lrsQry;
		lsQry.append(
			"SELECT TblSubstaId "
				+ "FROM RTS.RTS_SUBSTA_SUBSCR where OfcIssuanceno = ? "
				+ "and SubstaId = ? and TblName = 'RTS_SECURITY' ");

		try
		{
			// OfcIssuanceNo
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
						
			//SummaryEffDate
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey2())));
						
			Log.write(
				Log.SQL,
				this,
				" - qrySubstationSubscriptionSecurity - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(
				Log.SQL,
				this,
				" - qrySubstationSubscriptionSecurity - SQL - End");
			while (lrsQry.next())
			{
				liTblSubstaId =
					caDA.getIntFromDB(lrsQry, "TblSubstaId");

			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;

			if (liTblSubstaId == 999)
			{
				liTblSubstaId = aaGeneralSearchData.getIntKey2();
			}
			Log.write(
				Log.METHOD,
				this,
				" - qrySubstationSubscriptionSecurity - End ");
			return (liTblSubstaId);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySubstationSubscriptionSecurity - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD


	/**
	* Method to update RTS_SUBSTA_SUBSCR
	* 
	* @param  aaSubstaSubscrData SubstationSubscriptionData	
	* @throws RTSException 	
	*/
	public void updSubstationSubscription(SubstationSubscriptionData aaSubstaSubscrData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updSubstationSubscription - Begin");
		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_SUBSTA_SUBSCR SET "
				+ "TblUpdtIndi = ?, "
				+ "ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TblName = ?  AND "
				+ "TblSubstaId = ? ";

		try
		{

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSubscrData.getTblUpdtIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSubscrData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSubscrData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSubstaSubscrData.getTblName())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubstaSubscrData.getTblSubstaId())));

			Log.write(
				Log.SQL,
				this,
				"updSubstationSubscription - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updSubstationSubscription - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updSubstationSubscription - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updSubstationSubscription - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
