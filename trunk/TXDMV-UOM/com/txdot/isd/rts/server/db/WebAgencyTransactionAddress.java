/**
 * 
 */
package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.WebAgencyTransactionAddressData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 * WebAgencyTransactionAddress.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	11/03/2011	Created
 * 							defect 11137 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/** 
 * Provides methods to access RTS_WEB_AGNCY_TRANS_ADDR 
 * 
 * @version	6.9.0 		11/03/2011	 
 * @author	Kathy Harrell 
 * @since				11/03/2011	16:05:17  
 */
public class WebAgencyTransactionAddress
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgencyTransactionAddress Constructor
	 * 
	 */
	public WebAgencyTransactionAddress(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}
	
	/** 
	 * Method to Insert into RTS.RTS_WEB_AGNCY_TRANS_ADDR  
	 *
	 * @param  aaData	WebAgencyTransactionAddressData
	 * @throws RTSException 
	 */
	public void insWebAgencyTransactionAddress(WebAgencyTransactionAddressData aaData)
		throws RTSException
	{
		csMethod = "insWebAgencyTransactionAddress ";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");
		String lsValues = new String();
		
		// For 9 parameters 
		lsValues = "?";
		
		for (int i = 0; i < 8; i++)
		{
			lsValues = "?," + lsValues;
		}

		String lsIns =
			"INSERT into RTS.RTS_WEB_AGNCY_TRANS_ADDR("
				+ "SavReqId,"
				+ "AddrTypeCd,"
				+ "St1,"
				+ "St2,"
				+ "City,"
				+ "State,"
				+ "ZpCd,"
				+ "ZpCdP4,"
				+ "Cntry)"
				+ " VALUES ("
				+ lsValues
				+ ")";
		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getSavReqId())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getAddrTypeCd())));
			
			AddressData laAddrData = aaData.getAddrData();
			
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
						DatabaseAccess.convertToString(
							laAddrData.getCntry())));
			

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
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
	 * Method to Query RTS_WEB_AGNCY_TRANS_ADDR 
	 * 		
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public WebAgencyTransactionAddressData qryWebAgencyTransactionAddr(
		int aiSavReqId,
		String asAddrTypeCd)
		throws RTSException
	{
		csMethod = "qryWebAgencyTransactionAddr";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		WebAgencyTransactionAddressData laWATransAddrData = null; 
		
		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "SavReqId,"
				+ "AddrTypeCd,"
				+ "St1,"
				+ "St2,"
				+ "City,"
				+ "State,"
				+ "ZpCd,"
				+ "ZpCdP4,"
				+ "Cntry "
				+ "from RTS.RTS_WEB_AGNCY_TRANS_ADDR  "
				+ " WHERE "
				+ " SavReqId = ? and "
				+ " AddrTypeCd = ? "); 

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aiSavReqId)));
		
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						asAddrTypeCd)));
		
		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				AddressData laAddrData = new AddressData(); 
				
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
				laAddrData.setCntry(
						caDA.getStringFromDB(lrsQry, "Cntry"));
				laWATransAddrData = new WebAgencyTransactionAddressData(aiSavReqId,asAddrTypeCd,laAddrData);
				break; 
			} 

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return laWATransAddrData;
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
	
}
