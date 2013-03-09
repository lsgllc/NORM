package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.SubcontractorData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * Subcontractor.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/18/2001	Altered test for SubconId to != -1
 * N Ting		09/18/2001	Altered test for WsIds to Integer.MIN_VALUE
 * K Harrell	09/27/2001	Corrected Delete Method
 * K Harrell	02/20/2002	Altered to preserve ReqSubstaid
 * K Harrell	03/19/2002	Updated for AdminCache
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	08/17/2002	add purgeSubcontractor()
 *							defect 4601
 * K Harrell	06/19/2005  services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3
 * K Harrell	11/13/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3  
 * K Harrell	08/28/2008	No longer update Admin Cache from this class
 * 							delete updAdminCache()
 * 							modify delSubcontractor(),
 * 							 insSubcontractor(), updSubcontractor()
 * 							defect 8721 Ver Defect_POS_B
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeSubcontractor()
 * 							defect 9825 Ver Defect_POS_D   
 * K Harrell	10/13/2009	Implement Local Options Report Sort Options
 * 							add qrySubcontractor(SubcontractorData,boolean)
 * 							modify qrySubcontractor(SubcontractorData)
 * 							defect 10250 Ver Defect_POS_G
 * K Harrell	10/20/2009	Implement ReportConstant 
 * 							modify qrySubcontractor(SubcontractorData)
 * 							defect 10250 Ver Defect_POS_G
 * K Harrell	02/18/2010	Implement new SubcontractorData() 
 * 							modify qrySubcontractor(SubcontractorData), 
 * 							  insSubcontractor(SubcontractorData) 
 * 							defect 10250 Ver POS_640             
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_SUBCON
 * 
 * @version	POS_640			02/18/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/12/2002 13:02:07 
 */

public class Subcontractor extends SubcontractorData
{
	DatabaseAccess caDA;

	/**
	 * Subcontractor constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public Subcontractor(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to update RTS.RTS_SUBCON for logical delete
	 * 
	 * @param  aaSubcontractorData	SubcontractorData
	 * @throws RTSException
	 */
	public void delSubcontractor(SubcontractorData aaSubcontractorData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delSubcontractor - Begin");

		Vector lvValues = new Vector();

		int liSubstaId = qrySubcontractorSubstaId(aaSubcontractorData);

		String lsDel =
			"UPDATE RTS.RTS_SUBCON SET DeleteIndi = 1, "
				+ "ChngTimestmp = Current Timestamp "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = "
				+ liSubstaId
				+ " AND SubconId = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubcontractorData.getOfcIssuanceNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubcontractorData.getId())));

			Log.write(Log.SQL, this, "delSubcontractor - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "delSubcontractor - SQL - End");
			Log.write(Log.METHOD, this, "delSubcontractor - End");
			// defect 8721 
			// updAdminCache(aaSubcontractorData, liSubstaId);
			// end defect 8721 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delSubcontractor - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Insert into RTS.RTS_SUBCON
	 *
	 * @param  aaSubcontractorData SubcontractorData
	 * @throws RTSException 
	 */
	public void insSubcontractor(SubcontractorData aaSubcontractorData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insSubcontractor - Begin");
		Log.write(
			Log.METHOD,
			this,
			"insSubcontractor - First Try Update");

		int liNumRows = updSubcontractor(aaSubcontractorData);

		if (liNumRows == 0)
		{
			int liSubstaId =
				qrySubcontractorSubstaId(aaSubcontractorData);

			Vector lvValues = new Vector();

			// defect 10161 
			// Implement new SubcontractorData 
			String lsIns =
				"INSERT into RTS.RTS_SUBCON("
					+ "OfcIssuanceNo,"
					+ "SubstaId,"
					+ "SubconId,"
					+ "SubconName,"
					+ "SubconName2,"
					+ "SubconSt,"
					+ "SubconSt2,"
					+ "SubconCity,"
					+ "SubconZpCd,"
					+ "SubconZpCdP4,"
					+ "DeleteIndi,"
					+ "ChngTimestmp ) VALUES ( "
					+ " ?,"
					+ liSubstaId
					+ " ,?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " 0,"
					+ " CURRENT TIMESTAMP)";
			// end defect 10161 
			try
			{
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSubcontractorData.getOfcIssuanceNo())));

				// defect 10161
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSubcontractorData.getId())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSubcontractorData.getName1())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSubcontractorData.getName2())));

				AddressData laAddrData =
					aaSubcontractorData.getAddressData();

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laAddrData.getSt1())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laAddrData.getSt2())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laAddrData.getCity())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laAddrData.getZpcd())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laAddrData.getZpcdp4())));
				// end defect 10161 

				Log.write(
					Log.SQL,
					this,
					"insSubcontractor - SQL - Begin");
				caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
				Log.write(
					Log.SQL,
					this,
					"insSubcontractor - SQL - End");
				Log.write(Log.METHOD, this, "insSubcontractor - End");
				// defect 8721 
				// updAdminCache(aaSubcontractorData, liSubstaId);
				// end defect 8721 
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					"insSubcontractor - Exception - "
						+ aeRTSEx.getMessage());
				throw aeRTSEx;
			}
		} // if liNumRows = 0
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_SUBCON for Purge
	 * 
	 * @param  aiNumDays  int
	 * @return int
	 * @throws RTSException  
	 */
	public int purgeSubcontractor(int aiNumDays) throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeSubcontractor - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_SUBCON WHERE DELETEINDI = 1 AND"
				+ " days(Current Date) - days(ChngTimestmp) > ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiNumDays)));
			Log.write(
				Log.SQL,
				this,
				"purgeSubcontractor - SQL - Begin");

			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "purgeSubcontractor - SQL - End");
			Log.write(Log.METHOD, this, "purgeSubcontractor - End");
			return liNumRows;
			// end defect 9825 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeSubcontractor - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query RTS.RTS_SUBCON
	 * 
	 * @param  aaSubcontractorData  SubcontractorData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qrySubcontractor(SubcontractorData aaSubcontractorData)
		throws RTSException
	{
		// defect 10250 
		// Sort by Id 
		return qrySubcontractor(
			aaSubcontractorData,
			ReportConstant.SORT_BY_ID);
		// end defect 10250 
	}

	/**
	 * Method to Query RTS.RTS_SUBCON
	 * 
	 * @param  aaSubcontractorData  SubcontractorData	
		 * @param  abIdSort boolean 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qrySubcontractor(
		SubcontractorData aaSubcontractorData,
		boolean abIdSort)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qrySubcontractor - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		int liReqSubstaId = aaSubcontractorData.getSubstaId();

		int liSubstaId = qrySubcontractorSubstaId(aaSubcontractorData);

		// defect 10161 
		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ liReqSubstaId
				+ " as SubstaId,"
				+ "SubconId,"
				+ "SubconName,"
				+ "SubconName2,"
				+ "SubconSt,"
				+ "SubconSt2,"
				+ "SubconCity,"
				+ "SubconZpCd,"
				+ "SubconZpCdP4,"
				+ "DeleteIndi,"
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_SUBCON WHERE "
				+ " OfcIssuanceNo = ? AND "
				+ " SubstaId = "
				+ liSubstaId);
		// end defect 10161 

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaSubcontractorData.getOfcIssuanceNo())));

		if (aaSubcontractorData.getId() != Integer.MIN_VALUE)
		{
			lsQry.append(" AND SubconId = ?");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubcontractorData.getId())));
		}

		if (aaSubcontractorData.getChngTimestmp() == null)
		{
			lsQry.append(" AND DeleteIndi = 0 ");
		}
		else
		{
			lsQry.append(" AND ChngTimestmp > ?");
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaSubcontractorData.getChngTimestmp())));
		}

		lsQry.append(" order by 1,2,");

		String lsSortBy = abIdSort ? "SubconId" : "SubconName";

		lsQry.append(lsSortBy);

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qrySubcontractor - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qrySubcontractor - SQL - End");

			while (lrsQry.next())
			{
				SubcontractorData laSubcontractorData =
					new SubcontractorData();
				laSubcontractorData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laSubcontractorData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));

				// defect 10161
				laSubcontractorData.setId(
					caDA.getIntFromDB(lrsQry, "SubconId"));
				laSubcontractorData.setName1(
					caDA.getStringFromDB(lrsQry, "SubconName"));
				laSubcontractorData.setName2(
					caDA.getStringFromDB(lrsQry, "SubconName2"));
				AddressData laAddrData =
					laSubcontractorData.getAddressData();
				laAddrData.setSt1(
					caDA.getStringFromDB(lrsQry, "SubconSt"));
				laAddrData.setSt2(
					caDA.getStringFromDB(lrsQry, "SubconSt2"));
				laAddrData.setCity(
					caDA.getStringFromDB(lrsQry, "SubconCity"));
				laAddrData.setZpcd(
					caDA.getStringFromDB(lrsQry, "SubconZpCd"));
				laAddrData.setZpcdp4(
					caDA.getStringFromDB(lrsQry, "SubconZpCdP4"));
				// end defect 10161 

				laSubcontractorData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				laSubcontractorData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laSubcontractorData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qrySubcontractor - End ");
			return (lvRslt);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySubcontractor - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySubcontractor - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Determine SubstaId of Subcontractor Data
	 * 
	 * @param  aaSubcontractorData  SubcontractorData
	 * @return int
	 * @throws RTSException 
	 */
	public int qrySubcontractorSubstaId(SubcontractorData aaSubcontractorData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qrySubcontractorSubstaId - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		int liSubstaId = Integer.MIN_VALUE;

		lsQry.append(
			"SELECT TblSubstaId  "
				+ " from RTS.RTS_SUBSTA_SUBSCR "
				+ "where "
				+ " TblName = 'RTS_SUBCON' AND "
				+ " OfcIssuanceNo = ? AND  "
				+ " SubstaId = ? ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaSubcontractorData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaSubcontractorData.getSubstaId())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qrySubcontractorSubstaId - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qrySubcontractorSubstaId - SQL - End");

			while (lrsQry.next())
			{
				liSubstaId = caDA.getIntFromDB(lrsQry, "TblSubstaId");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qrySubcontractorSubstaId - End ");
			if (liSubstaId == Integer.MIN_VALUE)
			{
				liSubstaId = aaSubcontractorData.getSubstaId();
			}
			return (liSubstaId);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySubcontractorSubstaId - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_SUBCON
	 * 
	 * @param  aaSubcontractorData  SubcontractorData
	 * @return int
	 * @throws RTSException
	 */
	public int updSubcontractor(SubcontractorData aaSubcontractorData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updSubcontractor - Begin");

		Vector lvValues = new Vector();

		int liSubstaId = qrySubcontractorSubstaId(aaSubcontractorData);

		// defect 10161 
		String lsUpd =
			"UPDATE RTS.RTS_SUBCON SET "
				+ "SubconId = ?, "
				+ "SubconName = ?, "
				+ "SubconName2 = ?, "
				+ "SubconSt = ?, "
				+ "SubconSt2 = ?, "
				+ "SubconCity = ?, "
				+ "SubconZpCd = ?, "
				+ "SubconZpCdP4 = ?, "
				+ "DeleteIndi = 0, "
				+ "ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = "
				+ liSubstaId
				+ " AND SubconId = ? ";
		// end defect 10161 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubcontractorData.getId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSubcontractorData.getName1())));

			// defect 10161 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSubcontractorData.getName2())));
			AddressData laAddrData =
				aaSubcontractorData.getAddressData();
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getSt1())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getSt2())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getCity())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getZpcd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getZpcdp4())));
			// defect 10161 

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubcontractorData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSubcontractorData.getId())));
			Log.write(Log.SQL, this, "updSubcontractor - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updSubcontractor - SQL - End");
			Log.write(Log.METHOD, this, "updSubcontractor - End");
			// defect 8721 
			// updAdminCache(aaSubcontractorData, liSubstaId);
			// end defect 8721 
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updSubcontractor - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
