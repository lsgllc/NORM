package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.TreeMap;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.CountyData;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * OfficeIds.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks 		12/21/2001  Added support for Phase 2
 * K Harrell	01/07/2002	Removed EFTIndi from 
 *							qryOfficeIds(OfficeIdsData)
 *                         	Deleted method updOfficeIds  
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/02/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qryOfficeIds()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	10/27/2005	add qryCounties() 
 * 							defect 7889 Ver 5.2.3 
 * Ray Rowehl	05/29/2008	Add query to retreive Office Ids for a
 * 							selected Office Code.
 * 							deprecated qryOfficeIds()
 * 							add qryOfficeIds(int)
 * 							defect 9677 Ver MyPlates_POS   
 * K Harrell	03/19/2010	deleted qryOfficeIds() 
 * 							defect 10239 Ver POS_640 
 * Ray Rowehl	10/05/2010	Add a guard against pulling office 257 when
 * 							when just selecting by office code.
 * 							Also removed commented out code from 10239.
 * 							modify qryOfficeIds(int)
 * 							defect 10168 Ver POS 660                       
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_OFFICE_CDS 
 * 
 * @version	POS 660 		10/05/2010
 * @author 	Kathy Harrell
 * <br>Creation Date:		08/31/2001 13:57:15
 */
public class OfficeIds extends OfficeIdsData
{
	DatabaseAccess caDA;

	/**
	 * OfficeIds constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public OfficeIds(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_OFFICE_IDS for given office (at Server)
	 * 
	 * @param  aiOfficeId  int
	 * @return Vector
	 * @throws RTSException 
	 */
	public String qryOfficeId(int aiOfficeId) throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryOfficeId - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		Vector lvValues = new Vector();

		String lsName = "";

		lsQry.append(
			"SELECT "
				+ "OfcName "
				+ "FROM RTS.RTS_OFFICE_IDS "
				+ " WHERE OfcIssuanceNo = ?");
		try
		{
			Log.write(Log.SQL, this, " - qryOfficeId - SQL - Begin");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiOfficeId)));
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryOfficeId - SQL - End");

			while (lrsQry.next())
			{
				lsName = caDA.getStringFromDB(lrsQry, "OfcName");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryOfficeId - End ");
			return (lsName);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryOfficeId - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query RTS.RTS_OFFICE_IDS for a selected Office Code.
	 * 
	 * <p>If Office Code is less than 1, get all offices.
	 * 
	 * @parameter aiOfficeCd 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryOfficeIds(int aiOfficeCd) throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryOfficeIds - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "OfcIssuanceCd,"
				+ "OfcName,"
				+ "RegnlOfcId,"
				+ "TXDOTCntyNo,"
				+ "OfcSt,"
				+ "OfcCity,"
				+ "OfcZpCd,"
				+ "OfcZpCdP4,"
				+ "TacName,"
				+ "TacPhoneNo,"
				+ "TacAcctNo,"
				+ "DefrdRemitCd,"
				+ "PhysOfcLoc,"
				+ "EFTAccntCd,"
				+ "EMailAddr,"
				+ "OprtgHrs,"
				+ "OprtgDays,"
				+ "ItrntRenwlIndi "
				+ "FROM RTS.RTS_OFFICE_IDS");

		// If the office code passed is greater than 0, we are 
		// restricting by Office Issuance Code
		if (aiOfficeCd > 0)
		{
			// defect 10618
			// take out office 257 if it exists.
			lsQry.append(" WHERE OfcIssuanceCd = " 
				+ aiOfficeCd
				+ " AND OfcIssuanceNo <> 257");
			// end defect 10618
		}

		lsQry.append(" ORDER BY OfcIssuanceNo");

		try
		{
			Log.write(Log.SQL, this, " - qryOfficeIds - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryOfficeIds - SQL - End");

			while (lrsQry.next())
			{
				OfficeIdsData laOfficeIdsData = new OfficeIdsData();
				laOfficeIdsData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laOfficeIdsData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laOfficeIdsData.setOfcName(
					caDA.getStringFromDB(lrsQry, "OfcName"));
				laOfficeIdsData.setRegnlOfcId(
					caDA.getIntFromDB(lrsQry, "RegnlOfcId"));
				laOfficeIdsData.setTXDOTCntyNo(
					caDA.getIntFromDB(lrsQry, "TxDOTCntyNo"));
				laOfficeIdsData.setOfcSt(
					caDA.getStringFromDB(lrsQry, "OfcSt"));
				laOfficeIdsData.setOfcCity(
					caDA.getStringFromDB(lrsQry, "OfcCity"));
				laOfficeIdsData.setOfcZpCd(
					caDA.getIntFromDB(lrsQry, "OfcZpCd"));
				laOfficeIdsData.setOfcZpCdP4(
					caDA.getStringFromDB(lrsQry, "OfcZpCdP4"));
				laOfficeIdsData.setTacName(
					caDA.getStringFromDB(lrsQry, "TacName"));
				laOfficeIdsData.setTacPhoneNo(
					caDA.getStringFromDB(lrsQry, "TacPhoneNo"));
				laOfficeIdsData.setTacAcctNo(
					caDA.getStringFromDB(lrsQry, "TacAcctNo"));
				laOfficeIdsData.setDefrdRemitCd(
					caDA.getStringFromDB(lrsQry, "DefrdRemitCd"));
				laOfficeIdsData.setPhysOfcLoc(
					caDA.getStringFromDB(lrsQry, "PhysOfcLoc"));
				laOfficeIdsData.setEFTAccntCd(
					caDA.getIntFromDB(lrsQry, "EFTAccntCd"));
				laOfficeIdsData.setEMailAddr(
					caDA.getStringFromDB(lrsQry, "EMailAddr"));
				laOfficeIdsData.setOprtgHrs(
					caDA.getStringFromDB(lrsQry, "OprtgHrs"));
				laOfficeIdsData.setOprtgDays(
					caDA.getStringFromDB(lrsQry, "OprtgDays"));
				laOfficeIdsData.setItrntRenwlIndi(
					caDA.getIntFromDB(lrsQry, "ItrntRenwlIndi"));
				// Add element to the Vector
				lvRslt.addElement(laOfficeIdsData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryOfficeIds - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryOfficeIds - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	}

	/**
	 * Query the county list from the TxDOT POS database,
	 * put it into a hashtable, using the OfcIssuanceNo(countyNo)
	 * as the key.
	 * 
	 * @return TreeMap
	 * @throws RTSException
	 */
	public TreeMap qryCounties() throws RTSException
	{

		Log.write(Log.METHOD, this, "qryCounty - Start");
		// Select the county information
		// make the result order by ofcIssuanceNo ascending
		String lsSqlQry =
			"Select "
				+ "OfcIssuanceNo, "
				+ "OfcName, "
				+ "TacName, "
				+ "TacPhoneNo, "
				+ "OfcSt, "
				+ "OfcCity, "
				+ "OfcZpCd, "
				+ "OfcZpCdp4, "
				+ "EMAILADDR, "
				+ "OPRTGHRS, "
				+ "OPRTGDAYS, "
				+ "ITRNTRENWLINDI "
				+ "from RTS.RTS_OFFICE_IDS where OfcIssuanceNo "
				+ "<256 Order by OfcIssuanceNo";

		TreeMap ltmItmCountyList = new TreeMap();
		ResultSet lrsResultSet = null;
		try
		{
			Log.write(Log.SQL, this, " - qryCounties - SQL - Begin");
			lrsResultSet = caDA.executeDBQuery(lsSqlQry.toString());
			Log.write(Log.SQL, this, " - qryCounties - SQL - End");

			while (lrsResultSet.next())
			{
				// database field names
				CountyData laCountyData = new CountyData();
				laCountyData.setName(
					caDA.getStringFromDB(lrsResultSet, "OfcName"));
				laCountyData.setTACName(
					caDA.getStringFromDB(lrsResultSet, "TacName"));
				laCountyData.setPhoneNo(
					caDA.getStringFromDB(lrsResultSet, "TacPhoneNo"));

				AddressData lAddrData = new AddressData();
				lAddrData.setSt1(
					caDA.getStringFromDB(lrsResultSet, "OfcSt"));
				// lAddrData.setSt2(
				//		caDA.getStringFromDB(rs, "St2"));
				lAddrData.setCity(
					caDA.getStringFromDB(lrsResultSet, "OfcCity"));
				// lAddrData.setState(
				//		caDA.getStringFromDB(rs, "State"));
				lAddrData.setZpcd(
					caDA.getStringFromDB(lrsResultSet, "OfcZpCd"));
				lAddrData.setZpcdp4(
					caDA.getStringFromDB(lrsResultSet, "OfcZpCdp4"));

				laCountyData.setAddress(lAddrData);
				laCountyData.setEmail(
					caDA.getStringFromDB(lrsResultSet, "EmailAddr"));
				laCountyData.setHoursOfOperation(
					caDA.getStringFromDB(lrsResultSet, "OPRTGHRS"));
				laCountyData.setDaysOfOperation(
					caDA.getStringFromDB(lrsResultSet, "OPRTGDAYS"));
				laCountyData.setInternetIndi(
					caDA.getIntFromDB(lrsResultSet, "ITRNTRENWLINDI"));

				// use OfcIssuanceNo as the key.
				String lsOfcIssuanceNo =
					caDA.getStringFromDB(lrsResultSet, "OfcIssuanceNo");
				ltmItmCountyList.put(
					new Integer(lsOfcIssuanceNo),
					laCountyData);
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCounties - Exception " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCounties - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		finally
		{
			if (lrsResultSet != null)
			{
				try
				{
					lrsResultSet.close();
				}
				catch (Exception aeEx)
				{
				}
			}
		}
		if (ltmItmCountyList.size() == 0)
		{
			throw new RTSException(
				RTSException.JAVA_ERROR,
				new NullPointerException("The county list is null !!!"));
		}

		Log.write(
			Log.METHOD,
			this,
			"qryCounties - End: county list size="
				+ ltmItmCountyList.size());

		return ltmItmCountyList;
	}
} //END OF CLASS
