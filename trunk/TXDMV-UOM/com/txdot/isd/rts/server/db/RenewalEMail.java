package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.RenewalEMailData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 * RenewalEMail.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/12/2010	Created
 * 							defect 10514 Ver 6.5.0 
 * B Brown		07/30/2010  Add code for EReminder 
 * 							Add qryProcsTimeStmp(), updRenewalEMail(),
 * 								qryRecpntEMail()
 * 							defect 10512 Ver 6.5.0	
 * ---------------------------------------------------------------------
 */

/**
 * This class allows access to RTS_RENWL_EMAIL 
 *
 * @version	6.5.0			07/30/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		07/12/2010 16:08:17
 */
public class RenewalEMail
{
	private String csMethod;
	private DatabaseAccess caDA;

	/**
	 * RenewalEMail.java Constructor
	 * 
	 */
	public RenewalEMail(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to insert into RTS.RTS_RENWL_EMAIL from 
	 *     RTS.RTS_RENWL_EMAIL_TMP
	 * 
	 * @throws RTSException 
	 */
	public int insRenewalEMail() throws RTSException
	{
		csMethod = "insRenewalEMail";
		
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsIns =
			"Insert into RTS.RTS_RENWL_EMAIL "
				+ " (Select * from RTS.RTS_RENWL_EMAIL_TMP) ";

		Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
		
		try
		{
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsIns, new Vector());

			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			return liNumRows;

		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Purge from RTS_RENWL_EMAIL 
	 *  
	 * @param aaPurgeDate 
	 * @return int 
	 * @throws RTSException 
	 */
	public int purgeRenewalEMail(RTSDate aaPurgeDate)
		throws RTSException
	{
		csMethod = "purgeRenewalEMail";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsDel =
			"DELETE FROM RTS.RTS_RENWL_EMAIL WHERE "
				+ "BATCHDATE <= '"
				+ aaPurgeDate.toString()
				+ "'";

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, new Vector());

			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
	/**
	 * Return vector of RenwlEmailData for email process 
	 * 
	 * @return Vector 
	 * @throws RTSException 
	 */
	public Vector qryProcsTimeStmp() throws RTSException
	{
		csMethod = "qryProcsTimeStmp";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		Vector lvValues = new Vector();

		lsQry.append(
			"SELECT * "
				+ " from RTS.RTS_RENWL_EMAIL "
				+ " where ProcsTimestmp is null ");
				
		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(Log.SQL, this, csMethod = " - SQL - End");

			while (lrsQry.next())
			{
				RenewalEMailData laRenewalEMailData = new RenewalEMailData();
				laRenewalEMailData.setBatchDate(
					caDA.getRTSDateFromDB(lrsQry, "BatchDate"));
				laRenewalEMailData.setDocNo(
					caDA.getStringFromDB(lrsQry, "DocNo"));
				laRenewalEMailData.setVin(
					caDA.getStringFromDB(lrsQry, "Vin"));
				laRenewalEMailData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltno"));											
				laRenewalEMailData.setVehClassCd(
					caDA.getStringFromDB(lrsQry, "VehClassCd"));
				laRenewalEMailData.setVehBdyType(
					caDA.getStringFromDB(lrsQry, "VehBdyType"));	
				laRenewalEMailData.setVehMk(
					caDA.getStringFromDB(lrsQry, "VehMk"));
				laRenewalEMailData.setVehModl(
					caDA.getStringFromDB(lrsQry, "VehModl"));
				laRenewalEMailData.setVehModlYr(
					caDA.getIntFromDB(lrsQry, "VehModlYr"));
									
				laRenewalEMailData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laRenewalEMailData.setRegExpMo(
					caDA.getIntFromDB(lrsQry, "RegExpMo"));
				laRenewalEMailData.setRegStkrCd(
					caDA.getStringFromDB(lrsQry, "RegStkrCd"));
				laRenewalEMailData.setResComptCntyNo(
					caDA.getIntFromDB(lrsQry, "ResComptCntyNo"));
				laRenewalEMailData.setOrgNo(
					caDA.getStringFromDB(lrsQry, "OrgNo"));
				laRenewalEMailData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laRenewalEMailData.setExmptIndi(
					caDA.getIntFromDB(lrsQry, "ExmptIndi"));
				laRenewalEMailData.setRegRenwlExpYr(
					caDA.getIntFromDB(lrsQry, "RegRenwlExpYr"));
				laRenewalEMailData.setRecpntEMail(
					caDA.getStringFromDB(lrsQry, "RecpntEMail"));
				laRenewalEMailData.setWindwAddrName1(
					caDA.getStringFromDB(lrsQry, "WindwAddrName1"));
				laRenewalEMailData.setWindwAddrName2(
					caDA.getStringFromDB(lrsQry, "WindwAddrName2"));
				laRenewalEMailData.setERenwlRteIndi(
					caDA.getIntFromDB(lrsQry, "ERenwlRteIndi"));
				laRenewalEMailData.setProcsTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ProcsTimestmp"));	
				lvValues.add(laRenewalEMailData);																																																																					
			}
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			return lvValues;
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
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	}
	
	/**
	 * Return vector of RenwlEmailData for email process 
	 * 
	 * @return Vector 
	 * @throws RTSException 
	 */
	public Vector qryRecpntEMail(String asEmailAddress) throws RTSException
	{
		csMethod = "qryRecpntEMail";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		Vector lvValues = new Vector();

		lsQry.append(
			"select * from rts.rts_renwl_email a where a.recpntemail = '"
				+ asEmailAddress + "'"
				+ " and a.batchdate = (select max(batchdate) from "
				+ " rts.rts_renwl_email b where a.recpntemail = b.recpntemail)");
				
		Vector lvRenewalEMailData = new Vector();		
				
		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);

			Log.write(Log.SQL, this, csMethod = " - SQL - End");

			while (lrsQry.next())
			{
				RenewalEMailData laRenewalEMailData = new RenewalEMailData();
				laRenewalEMailData.setBatchDate(
					caDA.getRTSDateFromDB(lrsQry, "BatchDate"));
				laRenewalEMailData.setDocNo(
					caDA.getStringFromDB(lrsQry, "DocNo"));
				laRenewalEMailData.setVin(
					caDA.getStringFromDB(lrsQry, "Vin"));
				laRenewalEMailData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltno"));											
				laRenewalEMailData.setVehClassCd(
					caDA.getStringFromDB(lrsQry, "VehClassCd"));
				laRenewalEMailData.setVehBdyType(
					caDA.getStringFromDB(lrsQry, "VehBdyType"));	
				laRenewalEMailData.setVehMk(
					caDA.getStringFromDB(lrsQry, "VehMk"));
				laRenewalEMailData.setVehModl(
					caDA.getStringFromDB(lrsQry, "VehModl"));
				laRenewalEMailData.setVehModlYr(
					caDA.getIntFromDB(lrsQry, "VehModlYr"));
									
				laRenewalEMailData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laRenewalEMailData.setRegExpMo(
					caDA.getIntFromDB(lrsQry, "RegExpMo"));
				laRenewalEMailData.setRegStkrCd(
					caDA.getStringFromDB(lrsQry, "RegStkrCd"));
				laRenewalEMailData.setResComptCntyNo(
					caDA.getIntFromDB(lrsQry, "ResComptCntyNo"));
				laRenewalEMailData.setOrgNo(
					caDA.getStringFromDB(lrsQry, "OrgNo"));
				laRenewalEMailData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laRenewalEMailData.setExmptIndi(
					caDA.getIntFromDB(lrsQry, "ExmptIndi"));
				laRenewalEMailData.setRegRenwlExpYr(
					caDA.getIntFromDB(lrsQry, "RegRenwlExpYr"));
					
				laRenewalEMailData.setRecpntEMail(asEmailAddress);
				
				laRenewalEMailData.setWindwAddrName1(
					caDA.getStringFromDB(lrsQry, "WindwAddrName1"));
				laRenewalEMailData.setWindwAddrName2(
					caDA.getStringFromDB(lrsQry, "WindwAddrName2"));
				laRenewalEMailData.setERenwlRteIndi(
					caDA.getIntFromDB(lrsQry, "ERenwlRteIndi"));
				laRenewalEMailData.setProcsTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ProcsTimestmp"));
				lvRenewalEMailData.add(laRenewalEMailData);																																																																					
			}
			
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			return lvRenewalEMailData;
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
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	}
	
	/**
	 * Method to update RTS.RTS_RENWL_EMAIL_TMP
	 * 
	 * @param asDocno
	 * @throws RTSException 
	 * @return int
	 */
	public int updRenewalEMail(String asDocNo)
		throws RTSException
	{
		csMethod = "updRenewalEMail";

		Log.write(Log.METHOD, this, csMethod + " - Begin");
		Vector lvValues = new Vector();

		String lsUpd =
			"Update RTS.RTS_RENWL_EMAIL "
				+ " SET PROCSTIMESTMP = CURRENT TIMESTAMP "
				+ " WHERE "
				+ " DocNo = '" 
				+   asDocNo + "'";


		// DocNo
//		lvValues.addElement(
//			new DBValue(
//				Types.CHAR,
//				DatabaseAccess.convertToString(
//					aaRenwlEMailData.getDocNo())));

		Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

		try
		{
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			return liNumRows;

		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
}
