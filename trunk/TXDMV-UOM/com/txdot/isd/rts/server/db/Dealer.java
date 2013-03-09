package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.DealerData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * Dealer.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell  	09/10/2001  All:Use Current Timestamp vs. variable
 *                          Select: Where DeleteIndi = 0 
 *                          Comment out references to timestamp variables
 * K Harrell    09/18/2001 	Altered test for DealerId to != -1
 * N Ting		09/18/2001	Altered test for WsIds to Integer.MIN_VALUE
 * K Harrell    12/04/2001	Added (again) Current Timestamp
 * K Harrell	02/20/2002	Altered to preserve ReqSubstaid
 * K Harrell 	03/19/2002	Altered to Update AdminCache
 * R Hicks 		07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * K Harrell	08/16/2002  add purgeDealers() 
 *							defect	4601
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	05/12/2008	add Name2,St2,State,Cntry columns to SQL
 * 							modify insDealers(),qryDealers(),updDealers() 
 * 							defect 9654 Ver 3 Amigos PH B
 * K Harrell	08/28/2008	No longer update Admin Cache from this class
 * 							delete updAdminCache()
 * 							modify delDealers(),
 * 							 insDealers(),updDealers()
 * 							defect 8721 Ver Defect_POS_B  
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeDealers()
 * 							defect 9825 Ver Defect_POS_D
 * K Harrell	06/25/2009	Implement new DealerData
 * 							defect 10112 Ver Defect_POS_F     
 * K Harrell	10/13/2009	Implement Local Options Report Sort Options
 * 							add qryDealer(DealerData,boolean)
 * 							modify qryDealer(DealerData)
 * 							defect 10250 Ver Defect_POS_G  
 * K Harrell	10/20/2009	Implement ReportConstant 
 * 							modify qryDealer(DealerData)
 * 							defect 10250 Ver Defect_POS_G        
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to access RTS_DEALERS 
 *
 * @version	Defect_POS_G 	10/20/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		09/10/2001 12:58:31 
 */
public class Dealer extends DealerData
{
	DatabaseAccess caDA;

	/**
	 * Dealer constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public Dealer(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Delete from RTS.RTS_DEALERS
	 *
	 * @param  aaDealerData	DealerData	
	 * @throws RTSException 
	 */
	public void delDealer(DealerData aaDealerData) throws RTSException
	{
		Log.write(Log.METHOD, this, "delDealer - Begin");
		Vector lvValues = new Vector();

		int liSubstaId = qryDealerSubstaId(aaDealerData);

		String lsDel =
			"UPDATE RTS.RTS_DEALERS SET DeleteIndi = 1, "
				+ " ChngTimestmp = CURRENT TIMESTAMP "
				+ " WHERE "
				+ " OfcIssuanceNo = ? AND "
				+ " SubstaId = ? AND "
				+ " DealerId = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaDealerData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liSubstaId)));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaDealerData.getId())));

			Log.write(Log.SQL, this, "delDealer - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "delDealer - SQL - End");
			Log.write(Log.METHOD, this, "delDealer - End");

			// defect 8721 
			// updAdminCache(aaDealersData, liSubstaId);
			// end defect 8721
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delDealer - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Insert into RTS.RTS_DEALERS
	 *
	 * @param  aaDealerData	DealerData	
	 * @throws RTSException 
	 */
	public void insDealer(DealerData aaDealerData) throws RTSException
	{
		Log.write(Log.METHOD, this, "insDealer - Begin");

		Log.write(Log.METHOD, this, "insDealer - First Try Update");

		int liNumRows = updDealer(aaDealerData);

		if (liNumRows == 0)
		{
			int liSubstaId = qryDealerSubstaId(aaDealerData);

			Vector lvValues = new Vector();

			Log.write(Log.METHOD, this, "insDealer - Next Try Insert");

			// defect 9654 
			String lsIns =
				"INSERT into RTS.RTS_DEALERS("
					+ "OfcIssuanceNo,"
					+ "SubstaId,"
					+ "DealerId,"
					+ "DealerName,"
					+ "DealerName2,"
					+ "DealerSt,"
					+ "DealerSt2,"
					+ "DealerCity,"
					+ "DealerState,"
					+ "DealerCntry,"
					+ "DealerZpCd,"
					+ "DealerZpCdP4,"
					+ "DealerContact,"
					+ "DealerPhoneNo,"
					+ "DeleteIndi,"
					+ "ChngTimestmp ) VALUES ( "
					+ " ?,"
					+ liSubstaId
					+ " , ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ " 0,"
					+ " CURRENT TIMESTAMP )";

			try
			{
				// 1
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaDealerData.getOfcIssuanceNo())));
				// 2
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaDealerData.getId())));
				// 3
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaDealerData.getName1())));
				// 4
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaDealerData.getName2())));
				// 5
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaDealerData.getAddressData().getSt1())));
				// 6
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaDealerData.getAddressData().getSt2())));
				// 7
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaDealerData.getAddressData().getCity())));
				// 8
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaDealerData.getAddressData().getState())));
				// 9
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaDealerData.getAddressData().getCntry())));
				// 10
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaDealerData.getAddressData().getZpcd())));
				// 11
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaDealerData
								.getAddressData()
								.getZpcdp4())));
				// 12
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaDealerData.getContact())));
				// 13
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaDealerData.getPhoneNo())));
				// end defect 9654

				Log.write(Log.SQL, this, "insDealer - SQL - Begin");
				caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
				Log.write(Log.SQL, this, "insDealer - SQL - End");
				Log.write(Log.METHOD, this, "insDealer - End");

				// defect 8721 
				// updAdminCache(aaDealersData, liSubstaId);
				// end defect 8721 
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					"insDealers - Exception - " + aeRTSEx.getMessage());
				throw aeRTSEx;
			}
		} // END IF EQUAL 0
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_DEALERS for Purge
	 *
	 * @param  aiNumDays	int
	 * @return int  
	 * @throws RTSException 
	 */
	public int purgeDealer(int aiNumDays) throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeDealer - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_DEALERS WHERE DELETEINDI = 1 "
				+ " AND days(Current Date) - days(ChngTimestmp) > ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiNumDays)));

			Log.write(Log.SQL, this, "purgeDealer - SQL - Begin");
			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "purgeDealer - SQL - End");
			Log.write(Log.METHOD, this, "purgeDealer - End");
			return liNumRows;
			// end defect 9825 
		}

		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeDealer - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query all Dealer for OfcIssuanceNo, SubstaId
	 * 
	 * @param aaDealerData DealerData
	 * @return Vector 
	 * @throws RTSException
	 */
	public Vector qryDealer(DealerData aaDealerData)
		throws RTSException
	{
		// defect 10250 
		// Sort by Id 
		return qryDealer(aaDealerData, ReportConstant.SORT_BY_ID);
		// end defect 10250 
	}
	
	/**
	 * Method to Query all Dealer for OfcIssuanceNo, SubstaId
	 *
	 * @param 	aaDealerData DealerData
	 * @param   abIdSort boolean 
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryDealer(DealerData aaDealerData, boolean abIdSort)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryDealer - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		int liSubstaId = qryDealerSubstaId(aaDealerData);

		int liReqSubstaId = aaDealerData.getSubstaId();

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ liReqSubstaId
				+ " as Substaid,"
				+ "DealerId,"
				+ "DealerName,"
				+ "DealerName2,"
				+ "DealerSt,"
				+ "DealerSt2,"
				+ "DealerCity,"
				+ "DealerState,"
				+ "DealerCntry,"
				+ "DealerZpCd,"
				+ "DealerZpCdP4,"
				+ "DealerContact,"
				+ "DealerPhoneNo,"
				+ "DeleteIndi,"
				+ "ChngTimestmp "
				+ "from RTS.RTS_DEALERS "
				+ "where "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = "
				+ liSubstaId);

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaDealerData.getOfcIssuanceNo())));

		if (aaDealerData.getId() != Integer.MIN_VALUE)
		{
			lsQry.append(" AND DealerId = ?");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaDealerData.getId())));
		}

		if (aaDealerData.getChngTimestmp() == null)
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
						aaDealerData.getChngTimestmp())));
		}

		lsQry.append(" order by 1,2,");

		String lsSortBy = abIdSort ? "DealerId" : "DealerName,DealerName2";

		lsQry.append(lsSortBy);

		try
		{
			Log.write(Log.SQL, this, " - qryDealer - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryDealer - SQL - End");

			while (lrsQry.next())
			{
				DealerData laDealersData = new DealerData();
				laDealersData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laDealersData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laDealersData.setId(
					caDA.getIntFromDB(lrsQry, "DealerId"));
				laDealersData.setName1(
					caDA.getStringFromDB(lrsQry, "DealerName"));
				laDealersData.setName2(
					caDA.getStringFromDB(lrsQry, "DealerName2"));

				laDealersData.getAddressData().setCity(
					caDA.getStringFromDB(lrsQry, "DealerCity"));
				laDealersData.getAddressData().setSt1(
					caDA.getStringFromDB(lrsQry, "DealerSt"));
				laDealersData.getAddressData().setSt2(
					caDA.getStringFromDB(lrsQry, "DealerSt2"));
				laDealersData.getAddressData().setState(
					caDA.getStringFromDB(lrsQry, "DealerState"));

				laDealersData.getAddressData().setCntry(
					caDA.getStringFromDB(lrsQry, "DealerCntry"));

				laDealersData.getAddressData().setZpcd(
					caDA.getStringFromDB(lrsQry, "DealerZpCd"));
				laDealersData.getAddressData().setZpcdp4(
					caDA.getStringFromDB(lrsQry, "DealerZpCdP4"));
				laDealersData.setContact(
					caDA.getStringFromDB(lrsQry, "DealerContact"));
				laDealersData.setPhoneNo(
					caDA.getStringFromDB(lrsQry, "DealerPhoneNo"));
				laDealersData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				laDealersData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laDealersData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryDealer - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryDealer - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to determine owning substaid for dealers
	 *
	 * @param  aaDealerData  DealerData	
	 * @return int
	 * @throws RTSException 
	 */
	public int qryDealerSubstaId(DealerData aaDealerData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryDealerSubstaId - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		int liSubstaId = Integer.MIN_VALUE;

		lsQry.append(
			"SELECT TblSubstaId  "
				+ " from RTS.RTS_SUBSTA_SUBSCR "
				+ " where "
				+ " TblName = 'RTS_DEALERS' AND "
				+ " OfcIssuanceNo = ? AND  "
				+ " SubstaId = ? ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaDealerData.getOfcIssuanceNo())));

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaDealerData.getSubstaId())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryDealerSubstaId - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryDealerSubstaId - SQL - End");

			while (lrsQry.next())
			{
				liSubstaId = caDA.getIntFromDB(lrsQry, "TblSubstaId");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryDealerSubstaid - End ");

			if (liSubstaId == Integer.MIN_VALUE)
			{
				liSubstaId = aaDealerData.getSubstaId();
			}
			return (liSubstaId);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryDealer - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_DEALERS
	 * 
	 * @param  aaDealerData	DealerData	
	 * @return int		
	 * @throws RTSException 
	 */
	public int updDealer(DealerData aaDealerData) throws RTSException
	{
		Log.write(Log.METHOD, this, "updDealer - Begin");

		Vector lvValues = new Vector();

		int liSubstaId = qryDealerSubstaId(aaDealerData);

		// defect 9654
		String lsUpd =
			"UPDATE RTS.RTS_DEALERS SET "
				+ "DealerName = ?, "
				+ "DealerName2 = ?, "
				+ "DealerSt = ?, "
				+ "DealerSt2 = ?, "
				+ "DealerCity = ?, "
				+ "DealerState = ?, "
				+ "DealerCntry = ?, "
				+ "DealerZpCd = ?, "
				+ "DealerZpCdP4 = ?, "
				+ "DealerContact = ?, "
				+ "DealerPhoneNo = ?, "
				+ "DeleteIndi = 0, "
				+ "ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = "
				+ liSubstaId
				+ " AND DealerId = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDealerData.getName1())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDealerData.getName2())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDealerData.getAddressData().getSt1())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDealerData.getAddressData().getSt2())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDealerData.getAddressData().getCity())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDealerData.getAddressData().getState())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDealerData.getAddressData().getCntry())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDealerData.getAddressData().getZpcd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDealerData.getAddressData().getZpcdp4())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDealerData.getContact())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaDealerData.getPhoneNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaDealerData.getOfcIssuanceNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaDealerData.getId())));
			// end defect 9654

			Log.write(Log.SQL, this, "updDealer - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updDealer - SQL - End");
			Log.write(Log.METHOD, this, "updDealer - End");

			// defect 8721 
			// updAdminCache(aaDealersData, liSubstaId);
			// end defect 8721

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updDealer - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS 
