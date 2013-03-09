package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.BusinessPartnerData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * BusinessPartner.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/07/2011	Created 
 * 							defect 10726 Ver 6.7.0 
 * K Harrell	04/08/2011	remove reference to BsnPartnerNo 
 * 							modify qryBsnPartner() 
 * 							defect 10798 Ver 6.7.1 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_BSN_PARTNER
 *
 * @version	6.7.1  			04/08/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/07/2011 10:36:17 
 */
public class BusinessPartner
{
	private String csMethod;
	private DatabaseAccess caDA;

	/**
	 * BusinessPartner constructor comment.
	 * 
	 * @param  aaDA 
	 * @throws RTSException 
	 */
	public BusinessPartner(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS_BSN_PARTNER 
	 *
	 * @return Vector 	
	 * @throws RTSException 
	 */
	public Vector qryBsnPartner() throws RTSException
	{
		csMethod = "qryBsnPartner";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "BsnPartnerId  ,"
				+ "BsnPartnerTypeCd ,"
				+ "RTSEffDate,"
				+ "RTSEffEndDate,"
				+ "VendorAgntId  ,"
				+ "ELienRdyIndi  ,"
				//+ "BsnPartnerNo  ,"
				+ "Name1  ,"
				+ "Name2  ,"
				+ "St1,"
				+ "St2,"
				+ "City,"
				+ "State,"
				+ "ZpCd,"
				+ "ZpCdP4,"
				+ "Cntry,"
				+ "EMail,"
				+ "Phone,"
				+ "OfcIssuanceNo "
				+ "FROM "
				+ "RTS.RTS_BSN_PARTNER");
		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				BusinessPartnerData laBsnPartnerData =
					new BusinessPartnerData();

				// int 
				laBsnPartnerData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laBsnPartnerData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
				laBsnPartnerData.setELienRdyIndi(
					caDA.getIntFromDB(lrsQry, "ELienRdyIndi"));
				laBsnPartnerData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));

				// String
				laBsnPartnerData.setBsnPartnerId(
					caDA.getStringFromDB(lrsQry, "BsnPartnerId"));
				laBsnPartnerData.setBsnPartnerTypeCd(
					caDA.getStringFromDB(lrsQry, "BsnPartnerTypeCd"));
				laBsnPartnerData.setVendorAgntId(
					caDA.getStringFromDB(lrsQry, "VendorAgntId"));
					
				// defect 10798
				//	laBsnPartnerData.setBsnPartnerNo(
				//	 caDA.getStringFromDB(lrsQry, "BsnPartnerNo"));
				// end defect 10798
				 
				AddressData laAddrData = new AddressData();
				laBsnPartnerData.setName1(
					caDA.getStringFromDB(lrsQry, "Name1"));
				laBsnPartnerData.setName2(
					caDA.getStringFromDB(lrsQry, "Name2"));
				laAddrData.setSt1(caDA.getStringFromDB(lrsQry, "St1"));
				laAddrData.setSt2(caDA.getStringFromDB(lrsQry, "St2"));
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
				laBsnPartnerData.setAddressData(laAddrData);
				laBsnPartnerData.setPhone(
					caDA.getStringFromDB(lrsQry, "Phone"));
				laBsnPartnerData.setEmail(
					caDA.getStringFromDB(lrsQry, "EMail"));

				// Add element to the Vector
				lvRslt.addElement(laBsnPartnerData);
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
				csMethod + " - SQL Exception " + aeSQLEx.getMessage());
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

	} //END OF QUERY METHOD
} //END OF CLASS
