package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.SpecialPlatePermitData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * SpecialPlatePermit.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/21/2010	Created
 * 							defect 10700 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 *  This class allows the user to access RTS_SPCL_PLT_PRMT
 *
 * @version	6.7.0			12/21/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		12/21/2010 11:49:17
 */
public class SpecialPlatePermit
{
	private String csMethod;
	private DatabaseAccess caDA;

	/**
	 * SpecialPlatePermit.java Constructor
	 * 
	 */
	public SpecialPlatePermit(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Delete from RTS.RTS_SPCL_PLT_PRMT
	 * 
	 * @param  aaSpecialPlatePermit	
	 * @throws RTSException
	 */
	public void delSpecialPlatePermit(SpecialPlatePermitData aaSpecialPlatePermitData)
		throws RTSException
	{
		csMethod = "delSpecialPlatePermit";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_SPCL_PLT_PRMT "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "CustSeqNo = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialPlatePermitData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialPlatePermitData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialPlatePermitData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialPlatePermitData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialPlatePermitData.getCustSeqNo())));

			if (aaSpecialPlatePermitData.getTransTime() != 0)
			{
				lsDel = lsDel + " AND TransTime = ? ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSpecialPlatePermitData.getTransTime())));
			}
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, "delSpecialPlatePermit - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Insert into RTS.RTS_SPCL_PLT_PRMT
	 * 
	 * @param  aaSpecialPlatePermitData SpecialPlatePermitData	
	 * @throws RTSException
	 */
	public void insSpecialPlatePermit(SpecialPlatePermitData aaSpclPltPrmtData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insSpecialPlatePermit - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_SPCL_PLT_PRMT("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "RegPltNo,"
				+ "EffDate,"
				+ "ExpDate,"
				+ "VIN,"
				+ "VehModlYr,"
				+ "VehMk,"
				+ "TransId)";

		lsIns =
			lsIns
				+ "VALUES ( "
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
				+ " ?)";

		try
		{
			// OFCISSUANCENO
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpclPltPrmtData.getOfcIssuanceNo())));
			// SUBSTAID			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpclPltPrmtData.getSubstaId())));
			// TRANSAMDATE
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpclPltPrmtData.getTransAMDate())));
			// TRANSWSID						
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpclPltPrmtData.getTransWsId())));
			// CUSTSEQNO						
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpclPltPrmtData.getCustSeqNo())));
			// TRANSTIME						
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpclPltPrmtData.getTransTime())));
			// REGPLTNO
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSpclPltPrmtData.getRegPltNo())));
			// RTSEFFDATE
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpclPltPrmtData.getEffDate())));
			// RTSEFFENDDATE
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpclPltPrmtData.getExpDate())));
			// VIN
			lvValues.addElement(
				new DBValue(Types.CHAR, aaSpclPltPrmtData.getVIN()));
			// VEHMODLYR
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpclPltPrmtData.getVehModlYr())));
			// VEHMK
			lvValues.addElement(
				new DBValue(Types.CHAR, aaSpclPltPrmtData.getVehMk()));
			// TRANSID
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaSpclPltPrmtData.getTransId()));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End ");
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
	 * Method to Delete from RTS.RTS_SPCL_PLT_PRMT for purge
	 *
	 * @param  aiPurgeAMDate int	
	 * @return int
	 * @throws RTSException 
	 */
	public int purgeSpecialPlatePermit(int aiPurgeAMDate)
		throws RTSException
	{
		csMethod = "purgeSpecialPlatePermit";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			" DELETE FROM RTS.RTS_SPCL_PLT_PRMT "
				+ " WHERE  "
				+ " TRANSAMDATE <= ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);

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
	} //END OF Delete METHOD

	/**
	 * Method to Query from RTS.RTS_SPCL_PLT_PRMT
	 *
	 * @param 	aaSpecialPlatePermitData SpecialPlatePermitData	
	 * @return  Vector 
	 * @throws 	RTSException 	
	 */
	public Vector qrySpecialPlatePermit(String asRegPltNo)
		throws RTSException
	{
		csMethod = "qrySpecialPlatePermit";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "A.OfcIssuanceNo,"
				+ "A.SubstaId,"
				+ "A.TransAMDate,"
				+ "A.TransWsId,"
				+ "A.CustSeqNo,"
				+ "A.TransTime,"
				+ "A.RegPltNo,"
				+ "B.RegPltCd,"
				+ "A.EffDate,"
				+ "A.ExpDate,"
				+ "A.VIN,"
				+ "A.VehModlYr,"
				+ "A.VehMk,"
				+ "B.PltOwnrName1,"
				+ "B.PltOwnrName2,"
				+ "A.TransId "
				+ "FROM RTS.RTS_SPCL_PLT_PRMT A,"
				+ "RTS.RTS_SPCL_PLT_TRANS_HSTRY B "
				+ "WHERE "
				+ "A.REGPLTNO = ? AND "
				+ "A.REGPLTNO = B.REGPLTNO AND "
				+ "A.OFCISSUANCENO = B.OFCISSUANCENO  AND "
				+ "A.SUBSTAID = B.SUBSTAID AND "
				+ "A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ "A.TRANSWSID = B.TRANSWSID AND "
				+ "A.CUSTSEQNO = B.CUSTSEQNO AND "
				+ "A.TRANSTIME = B.TRANSTIME AND "
				+ "B.TRANSCOMPLETEINDI = 1 AND "
				+ "B.VOIDEDTRANSINDI = 0 ");

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(asRegPltNo.trim())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{

				SpecialPlatePermitData laSpclPltPrmtData =
					new SpecialPlatePermitData();
				laSpclPltPrmtData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laSpclPltPrmtData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laSpclPltPrmtData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laSpclPltPrmtData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laSpclPltPrmtData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laSpclPltPrmtData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laSpclPltPrmtData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));
				laSpclPltPrmtData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laSpclPltPrmtData.setEffDate(
					caDA.getIntFromDB(lrsQry, "EffDate"));
				laSpclPltPrmtData.setExpDate(
					caDA.getIntFromDB(lrsQry, "ExpDate"));
				laSpclPltPrmtData.setVIN(
					caDA.getStringFromDB(lrsQry, "VIN"));
				laSpclPltPrmtData.setVehModlYr(
					caDA.getIntFromDB(lrsQry, "VehModlYr"));
				laSpclPltPrmtData.setVehMk(
					caDA.getStringFromDB(lrsQry, "VehMk"));
				laSpclPltPrmtData.setTransId(
					caDA.getStringFromDB(lrsQry, "TransId"));
				laSpclPltPrmtData.setPltOwnrName1(
					caDA.getStringFromDB(lrsQry, "PltOwnrName1"));
				laSpclPltPrmtData.setPltOwnrName2(
					caDA.getStringFromDB(lrsQry, "PltOwnrName2"));
				// Add element to the Vector
				lvRslt.addElement(laSpclPltPrmtData);
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
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD
} //END OF CLASS
