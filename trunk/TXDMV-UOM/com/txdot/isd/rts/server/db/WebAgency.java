package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.WebAgencyData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.webservices.agncy.data.RtsWebAgncy;

/*
 * WebAgency.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708 Ver 6.7.0 
 * K Harrell	01/10/2011	Renamings per standards 
 * 							defect 10708 Ver 6.7.0  
 * Ray Rowehl	01/23/2011	Modify Query to allow different input values.
 * 							add qryWebAgency(WebAgencyData)
 * 							delete qryWebAgency(int)
 * 							defect 10718 Ver 6.7.0
 * Ray Rowehl	01/24/2011	Add a new method to do a combined query for 
 * 							the service.  The desire is to have a 
 * 							combined object to display.
 * 							add qryWebAgencyService(WebAgencyData)
 * 							defect 10718 Ver 6.7.0
 * Kathy McKee  08/03/2011  Added upper to Location Name Select
 * 							defect 10718 Ver 6.8.1
 * Kathy McKee  08/18/2011  Add  delWebAgency()
 * 							defect 10729 Ver 6.8.1
 * Kathy McKee  09/02/2011  Modified qryWebAgencyService()
 *                          Added code to check for TXCMV HQ type
 * 							defect 10729 Ver 6.8.1
 * Kathy McKee  10/20/2011  refactor  qryWebAgencyService() to qryWebAgencies()
 *                          defect 11151 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_WEB_AGENCY
 *
 * @version	6.9.0.			10/20/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 20:28:17
 */

public class WebAgency
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgency.java Constructor
	 * 
	 */
	public WebAgency(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_WEB_AGNCY 
	 *
	 * @param  aaData	WebAgencyData	
	 * @throws RTSException 
	 */
	public WebAgencyData insWebAgency(WebAgencyData aaData)
		throws RTSException
	{
		csMethod = "insWebAgency";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsIns =
			"INSERT into RTS.RTS_WEB_AGNCY("
				+ "AGNCYTYPECD,"
				+ "NAME1,"
				+ "NAME2,"
				+ "ST1,"
				+ "ST2,"
				+ "CITY,"
				+ "STATE,"
				+ "ZPCD,"
				+ "ZPCDP4,"
				+ "PHONE,"
				+ "EMAIL,"
				+ "CNTCTNAME,"
				+ "INITOFCNO,"
				+ "DELETEINDI,"
				+ "UPDTNGAGNTIDNTYNO, "
				+ "CHNGTIMESTMP) "
				+ " VALUES ( "
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
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " CURRENT TIMESTAMP)";
		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getAgncyTypeCd())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getName1())));
			// 3
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getName2())));

			AddressData laAddrData = aaData.getAddressData();

			// 4
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getSt1())));
			// 5
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getSt2())));
			// 6
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getCity())));
			// 7
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getState())));
			// 8
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getZpcd())));
			// 9
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getZpcdp4())));
			// 10
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getPhone())));
			// 11
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getEMail())));
			// 12
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getCntctName())));
			// 13
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getInitOfcNo())));
			// 14
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));
			// 15
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getUpdtngAgntIdntyNo())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			ResultSet lrsQry;
			String lsSel =
				"Select IDENTITY_VAL_LOCAL() as AgncyIdntyNo from "
					+ " RTS.RTS_WEB_AGNCY";

			lrsQry = caDA.executeDBQuery(lsSel, null);
			int liIdntyNo = 0;

			while (lrsQry.next())
			{
				liIdntyNo = caDA.getIntFromDB(lrsQry, "AgncyIdntyNo");
				aaData.setAgncyIdntyNo(liIdntyNo);
				break;
			}

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
			return aaData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
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
	} //END OF INSERT METHOD

	/**
	 * Purge RTS_WEB_AGNCY
	 * 
	 * @param aiPurgeDays int
	 * @return int
	 * @throws RTSException
	 */
	public int purgeWebAgency(int aiPurgeDays) throws RTSException
	{
		csMethod = "purgeWebAgency";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlDelData =
			"DELETE FROM RTS.RTS_WEB_AGNCY "
				+ " WHERE DELETEINDI = 1 AND "
				+ " CHNGTIMESTMP "
				+ " < (CURRENT TIMESTAMP - ? DAYS)";

		Vector lvValues = new Vector();

		lvValues.add(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiPurgeDays)));

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsSqlDelData,
					lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * Method to Query RTS_WEB_AGNCY
	 *
	 * @param	aaWebAgencyData
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgency(WebAgencyData aaWebAgencyData)
		throws RTSException
	{
		csMethod = "qryWebAgency";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "AGNCYIDNTYNO,"
				+ "AGNCYTYPECD,"
				+ "NAME1,"
				+ "NAME2,"
				+ "ST1,"
				+ "ST2,"
				+ "CITY,"
				+ "STATE,"
				+ "ZPCD,"
				+ "ZPCDP4,"
				+ "PHONE,"
				+ "EMAIL,"
				+ "CNTCTNAME,"
				+ "INITOFCNO,"
				+ "DELETEINDI,"
				+ "CHNGTIMESTMP "
				+ "from RTS.RTS_WEB_AGNCY ");

		// Use the appropriate where clause
		if (aaWebAgencyData.getName1() != null
			&& aaWebAgencyData.getAddressData().getCity() != null
			&& aaWebAgencyData.getName1().length() > 0
			&& aaWebAgencyData.getAddressData().getCity().length() > 0)
		{
			// Use Agency Name and City.  Assume partial search on name.
			lsQry.append(" WHERE upper(NAME1) LIKE ? " + " AND upper(CITY) = ? ");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebAgencyData.getName1())));
		}
		if (aaWebAgencyData.getName1() != null
			&& aaWebAgencyData.getName1().length() > 0)
		{
			// Use Agency Name if provided.  Assume partial search.
			lsQry.append(" WHERE upper(NAME1) LIKE ? ");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebAgencyData.getName1())));
		}
		else if (
			aaWebAgencyData.getAddressData().getCity() != null
				&& aaWebAgencyData.getAddressData().getCity().length()
					> 0)
		{
			// Use City if provided
			lsQry.append(" WHERE upper(CITY) = ? ");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebAgencyData.getAddressData().getCity())));
		}
		else if (
			aaWebAgencyData.getAddressData().getZpcd() != null
				&& aaWebAgencyData.getAddressData().getZpcd().length()
					> 0)
		{
			// use Zipcode if provided
			lsQry.append(" WHERE ZPCD = ? ");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWebAgencyData.getAddressData().getZpcd())));
		}
		else if (aaWebAgencyData.getAgncyIdntyNo() > 0)
		{
			// Use identity number if it is provided and nothing else 
			// provided
			lsQry.append(" WHERE AGNCYIDNTYNO = ? ");

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWebAgencyData.getAgncyIdntyNo())));
		}

		lsQry.append(" ORDER BY AGNCYIDNTYNO");

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgencyData laData = new WebAgencyData();

				laData.setAgncyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyIdntyNo"));

				laData.setAgncyTypeCd(
					caDA.getStringFromDB(lrsQry, "AgncyTypeCd"));

				laData.setName1(
					caDA.getStringFromDB(lrsQry, "Name1"));

				laData.setName2(
					caDA.getStringFromDB(lrsQry, "Name2"));
				
				laData.setPhone(
					caDA.getStringFromDB(lrsQry, "Phone"));

				laData.setEMail(
					caDA.getStringFromDB(lrsQry, "EMail"));

				laData.setCntctName(
					caDA.getStringFromDB(lrsQry, "CntctName"));

				laData.setInitOfcNo(
					caDA.getIntFromDB(lrsQry, "InitOfcNo"));

				laData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));

				laData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				
				//	defect 11151

				AddressData laAddrData = laData.getAddressData();

				laAddrData.setSt1(
					caDA.getStringFromDB(lrsQry, "St1"));

				laAddrData.setSt2(
					caDA.getStringFromDB(lrsQry, "St2"));

				laAddrData.setCity(
					caDA.getStringFromDB(lrsQry, "City"));

				laAddrData.setState(
					caDA.getStringFromDB(lrsQry, "State"));

				laAddrData.setZpcd(
					caDA.getStringFromDB(lrsQry, "ZpCd"));

				laAddrData.setZpcdp4(
					caDA.getStringFromDB(lrsQry, "ZpCdP4"));
				
				laData.setAddressData(laAddrData);
				
				// end defect 11151
	
				// Add element to the Vector
				lvRslt.addElement(laData);
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
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query RTS_WEB_AGNCY for a list of agencies
	 *
	 * @param	aaWebAgencyData
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgencies(RtsWebAgncy aaRtsWebAgncy)
		throws RTSException
	{
		 
			csMethod = "qryWebAgencies";

			Log.write(Log.METHOD, this, csMethod + " - Begin");

			StringBuffer lsQry = new StringBuffer();

			Vector lvRslt = new Vector();

			Vector lvValues = new Vector();

			ResultSet lrsQry;
			
			// defect 11151 
			
			// Removed RTS.RTS_WEB_AGNCY_AUTH, RTS.RTS_WEB_AGNCY_AUTH_CFG,  
			// and RTS.RTS_OFFICE_IDS columns from the select

			lsQry.append(
				"SELECT UNIQUE "
					+ "A.AGNCYIDNTYNO,"
					+ "AGNCYTYPECD,"
					+ "NAME1,"
					+ "NAME2,"
					+ "ST1,"
					+ "ST2,"
					+ "CITY,"
					+ "STATE,"
					+ "ZPCD,"
					+ "ZPCDP4,"
					+ "PHONE,"
					+ "EMAIL,"
					+ "CNTCTNAME,"
					+ "INITOFCNO,"
					+ "A.CHNGTIMESTMP "
					+ "from RTS.RTS_WEB_AGNCY A, "
					+ "RTS.RTS_WEB_AGNCY_AUTH B, "
					+ "RTS.RTS_WEB_AGNCY_AUTH_CFG C, "
					+ "RTS.RTS_OFFICE_IDS D "
					+ "WHERE A.AGNCYIDNTYNO = B.AGNCYIDNTYNO "
					+ "AND B.AGNCYAUTHIDNTYNO = C.AGNCYAUTHIDNTYNO "
					+ "AND A.DELETEINDI = 0 "
					+ "AND B.DELETEINDI = 0 "
					+ "AND C.DELETEINDI = 0 "
					+ "AND B.OFCISSUANCENO = D.OFCISSUANCENO "
					);
			// end defect 11151
			// defect 10729
			if (aaRtsWebAgncy.getAgncyTypeCd()!= null &&
				aaRtsWebAgncy.getAgncyTypeCd().equalsIgnoreCase("1")){
				lsQry.append("AND AGNCYTYPECD IN ('1','2','3') ");
			}else{
				
				if (aaRtsWebAgncy.getAgncyAuthCfgs() != null){
				// Use the appropriate where clause
					//	defect 11151
					if (aaRtsWebAgncy.getAgncyAuthCfgs()[0].getOfcIssuanceNo() > 0)
					//	end defect 11151
					{
					// Use the OfcIssuanceNo
						lsQry.append("AND B.OFCISSUANCENO = ? ");
						lvValues.addElement(
							new DBValue(
								Types.INTEGER,
									DatabaseAccess.convertToString(
										aaRtsWebAgncy.getAgncyAuthCfgs()[0].getOfcIssuanceNo())));
					}
					
				}
			}
			// end defect 10729
			if (aaRtsWebAgncy.getName1() != null  &&
				   aaRtsWebAgncy.getName1().trim().length() > 0)
			{
	 			// Use Agency Name if provided.  Assume partial search.
				lsQry.append("AND upper(NAME1) LIKE  ? ");
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
							DatabaseAccess.convertToString(
								aaRtsWebAgncy.getName1().toUpperCase().trim() + "%")));
			}
			if (aaRtsWebAgncy.getCity() != null){
				if( aaRtsWebAgncy.getCity().length() > 0)
				{
					// Use City if provided
					lsQry.append("AND upper(CITY) LIKE ? ");
					lvValues.addElement(
						new DBValue(
							Types.CHAR,
							DatabaseAccess.convertToString(
									aaRtsWebAgncy.getCity().toUpperCase().trim())));
				}
			}
			if (
				aaRtsWebAgncy.getZpCd() != null
					&& aaRtsWebAgncy.getZpCd().length() > 0)
			{
				// use Zipcode if provided
				lsQry.append("AND ZPCD = ? ");
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaRtsWebAgncy.getZpCd())));
			}

			if (aaRtsWebAgncy.getAgncyIdntyNo() > 0)
			{
				// Use identity number if it is provided and nothing else 
				// provided
				lsQry.append("AND A.AGNCYIDNTYNO = ? ");

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaRtsWebAgncy.getAgncyIdntyNo())));
			}

			lsQry.append(" ORDER BY A.AGNCYIDNTYNO");

			try
			{
				Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
				lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
				Log.write(Log.SQL, this, csMethod + " - SQL - End");

				while (lrsQry.next())
				{
					RtsWebAgncy laData = new RtsWebAgncy();

					laData.setAgncyIdntyNo(
						caDA.getIntFromDB(lrsQry, "AgncyIdntyNo"));

					laData.setAgncyTypeCd(
						caDA.getStringFromDB(lrsQry, "AgncyTypeCd"));

					laData.setName1(
						caDA.getStringFromDB(lrsQry, "Name1"));

					laData.setName2(
						caDA.getStringFromDB(lrsQry, "Name2"));

					laData.setSt1(
						caDA.getStringFromDB(lrsQry, "St1"));

					laData.setSt2(
						caDA.getStringFromDB(lrsQry, "St2"));

					laData.setCity(
						caDA.getStringFromDB(lrsQry, "City"));

					laData.setState(
						caDA.getStringFromDB(lrsQry, "State"));

					laData.setZpCd(
						caDA.getStringFromDB(lrsQry, "ZpCd"));

					laData.setZpCdP4(
						caDA.getStringFromDB(lrsQry, "ZpCdP4"));

					laData.setPhone(
						caDA.getStringFromDB(lrsQry, "Phone"));

					laData.setEMail(
						caDA.getStringFromDB(lrsQry, "EMail"));

					laData.setCntctName(
						caDA.getStringFromDB(lrsQry, "CntctName"));

					laData.setInitOfcNo(
						caDA.getIntFromDB(lrsQry, "InitOfcNo"));

					laData.setChngTimeStmpAgncy(
						(caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"))
							.getCalendar());

					// Add element to the Vector
					lvRslt.addElement(laData);
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
					csMethod + " - Exception " + aeSQLEx.getMessage());
				throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					csMethod + " - Exception " + aeRTSEx.getMessage());
				throw aeRTSEx;
			}
		} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_WEB_AGNCY
	 * 
	 * @param  aaData WebAgencyData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updWebAgency(WebAgencyData aaData) throws RTSException
	{
		csMethod = "updWebAgency";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNCY "
					+ " SET "
					+ " Name1 = ?, "
					+ " Name2 = ?, "
					+ " St1 = ?, "
					+ " St2 = ?, "
					+ " City = ?, "
					+ " State = ?, "
					+ " ZpCd = ?, "
					+ " ZpCdP4 = ?, "
					+ " Phone = ?, "
					+ " EMail = ?, "
					+ " CntctName = ?, "
					+ " DeleteIndi = ?, "
					+ " UPDTNGAGNTIDNTYNO = ?, "
					+ " ChngTimestmp = Current Timestamp "
					+ " where "
					+ " AgncyIdntyNo = ?";

			// 1
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getName1())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getName2())));

			AddressData laAddrData = aaData.getAddressData();

			// 3
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getSt1())));
			// 4
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getSt2())));
			// 5
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getCity())));
			// 6 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getState())));
			// 7
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getZpcd())));
			// 8
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laAddrData.getZpcdp4())));
			// 9
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getPhone())));
			// 10
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(aaData.getEMail())));
			
			// 11
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getCntctName())));
			// 12
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));
			// 13
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getUpdtngAgntIdntyNo())));
			// 14
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyIdntyNo())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + "  - End");

			return (liNumRows);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + "  - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
 
	
	/**
	 * Method to delete RTS.RTS_WEB_AGNCY
	 * 
	 * @param  aaData WebAgencyData	
	 * @throws RTSException 
	 */
	// defect 10729  
	public void delWebAgency(WebAgencyData aaData) throws RTSException
	{

		Vector lvValues = new Vector();
		csMethod = "delWebAgency";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsUpd = new StringBuffer();
		try
		{
			// Update the agency delete indicator
			lsUpd.append("UPDATE RTS.RTS_WEB_AGNCY A  "
					+ " SET DeleteIndi = 1, "
					+ " ChngTimestmp = Current Timestamp "
					+ " where  AgncyIdntyNo = ?");

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaData
							.getAgncyIdntyNo())));
			lsUpd.append(" AND NOT EXISTS (SELECT * FROM RTS.RTS_WEB_AGNCY_AUTH B "
					+ "WHERE A.AGNCYIDNTYNO = B.AGNCYIDNTYNO  "
					+ "AND DELETEINDI = 0)");

			Log.write(Log.SQL, this, csMethod
					+ "Update Delete INDI - SQL - Begin");

			caDA
				.executeDBInsertUpdateDelete(lsUpd.toString(),
					lvValues);

			Log.write(Log.SQL, this, csMethod
					+ " Update Delete INDI - SQL - End");
			Log.write(Log.METHOD, this, csMethod + "  - End");

		} catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this, csMethod + " - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		 
	} //END OF Delete METHOD

	//	end defect 10729 
}
