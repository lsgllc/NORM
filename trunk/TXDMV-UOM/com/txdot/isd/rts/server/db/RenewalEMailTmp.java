package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.RenewalEMailData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * RenewalEMailTmp.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/12/2010	Created
 * 							defect 10508 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows access to RTS_RENWL_EMAIL_TMP
 *
 * @version	6.5.0			07/12/2010
 * @author	Kathy Harrell	
 * <br>Creation Date:		07/12/2010 16:19:17
 */
public class RenewalEMailTmp
{
	private String csMethod;
	private DatabaseAccess caDA;

	/**
	 * RenewalEMailTmp.java Constructor
	 * 
	 */
	public RenewalEMailTmp(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to delete from RTS.RTS_RENWL_EMAIL_TMP
	 * 
	 * @throws RTSException 
	 */
	public int delRenewalEMailTmp() throws RTSException
	{
		csMethod = "delRenewalEMailTmp";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsDel = "DELETE from RTS.RTS_RENWL_EMAIL_TMP";

		Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

		try
		{
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, new Vector());

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
	 * Method to update RTS.RTS_RENWL_EMAIL_TMP
	 * 
	 * @param aaRenwlEMailData
	 * @throws RTSException 
	 */
	public int updRenewalEMailTmp(RenewalEMailData aaRenwlEMailData)
		throws RTSException
	{
		csMethod = "upRenewalEMailTmp";

		Log.write(Log.METHOD, this, csMethod + " - Begin");
		Vector lvValues = new Vector();

		String lsUpd =
			"Update RTS.RTS_RENWL_EMAIL_TMP "
				+ " SET PROCSTIMESTMP = CURRENT TIMESTAMP "
				+ " WHERE "
				+ " BATCHDATE = '" 
				+ aaRenwlEMailData.getBatchDate().toString()
				+ "' AND DOCNO  = ? ";

		// BatchDate
//		lvValues.addElement(
//			new DBValue(
//				Types.CHAR,
//				DatabaseAccess.convertToString(
//					aaRenwlEMailData.getBatchDate().toString())));

		// DocNo
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaRenwlEMailData.getDocNo())));

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

	/**
	 * Method to insert into RTS.RTS_RENWL_EMAIL_TMP 
	 * 
	 * @throws RTSException 
	 */
	public void insRenewalEMailTmp(Vector aaVector) throws RTSException
	{
		csMethod = "insRenewalEMailTmp";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		RenewalEMailData laRenwlEMailData = null;

		for (int i = 0; i < aaVector.size(); i++)
		{
			try
			{
				laRenwlEMailData =
					(RenewalEMailData) aaVector.elementAt(i);

				Vector lvValues = new Vector();

				String lsIns =
					"INSERT into RTS.RTS_RENWL_EMAIL_TMP("
						+ "BatchDate,"
						+ "DocNo,"
						+ "Vin,"
						+ "RegPltNo,"
						+ "VehClassCd,"
						+ "VehBdyType,"
						+ "VehMk,"
						+ "VehModl,"
						+ "VehModlYr,"
						+ "RegPltCd,"
						+ "RegExpMo,"
						+ "RegStkrCd,"
						+ "ResComptCntyNo,"
						+ "OrgNo,"
						+ "RegClassCd,"
						+ "ExmptIndi,"
						+ "RegRenwlExpYr,"
						+ "RecpntEMail,"
						+ "WindwAddrName1,"
						+ "WindwAddrName2,"
						+ "ERenwlRteIndi) "
						+ " VALUES ( "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ?, "
						+ " ? )";

				// BatchDate
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getBatchDate().toString())));

				// DocNo
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getDocNo())));
				// Vin
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getVin())));

				// RegPltNo 
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getRegPltNo())));

				// VehClassCd 
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getVehClassCd())));

				// VehBdyType 
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getVehBdyType())));

				// VehMk 
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getVehMk())));

				// VehModl 
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getVehModl())));

				// VehModlYr
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getVehModlYr())));

				// RegPltCd
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getRegPltCd())));

				// RegExpMo
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getRegExpMo())));

				// RegStkrCd
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getRegStkrCd())));

				// ResComptCntyNo
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getResComptCntyNo())));

				// OrgNo
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getOrgNo())));

				// RegClassCd
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getRegClassCd())));

				// ExmptIndi
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getExmptIndi())));

				// RegRenwlExpYr
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getRegRenwlExpYr())));

				// RecpntEMail
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getRecpntEMail())));

				// WindwAddrName1
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getWindwAddrName1())));

				// WindwAddrName2
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getWindwAddrName2())));

				// ERenwlRteIndi
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							laRenwlEMailData.getERenwlRteIndi())));

				Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

				caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

				Log.write(Log.SQL, this, csMethod + " - SQL - End");
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					csMethod
						+ " - Exception - "
						+ aeRTSEx.getMessage());

				logInsertError(laRenwlEMailData);

				throw aeRTSEx;
			}
			catch (NullPointerException aeNPEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					csMethod + " - Exception - " + aeNPEx.getMessage());

				logInsertError(laRenwlEMailData);

				RTSException leRTSEx = new RTSException();
				leRTSEx.setDetailMsg(aeNPEx.getMessage());
				leRTSEx.setMessage(
					"NullPointerException while inserting into "
						+ "RTS_RENWL_EMAIL_TMP");
				throw leRTSEx;
			}
		}
		Log.write(Log.METHOD, this, csMethod + " - End");

	} //END OF INSERT METHOD

	/**
	 * Log Details of Error on Insert
	 * 
	 * @param laRenwlEMailData
	 */
	private void logInsertError(RenewalEMailData laRenwlEMailData)
	{
		Log.write(
			Log.SQL_EXCP,
			this,
			"BatchDate = "
				+ laRenwlEMailData.getBatchDate().toString());

		Log.write(
			Log.SQL_EXCP,
			this,
			"DocNo = " + laRenwlEMailData.getDocNo());

		Log.write(
			Log.SQL_EXCP,
			this,
			"VIN = " + laRenwlEMailData.getVin());

		Log.write(
			Log.SQL_EXCP,
			this,
			"RegPltNo = " + laRenwlEMailData.getRegPltNo());

		Log.write(
			Log.SQL_EXCP,
			this,
			"ExpMo/Yr = "
				+ laRenwlEMailData.getRegExpMo()
				+ "/"
				+ laRenwlEMailData.getRegRenwlExpYr());
	}
}
