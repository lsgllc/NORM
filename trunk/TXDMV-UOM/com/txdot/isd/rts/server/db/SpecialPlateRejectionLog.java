package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.SpecialPlateRejectionLogData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * SpecialPlateRejectionLog.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/06/2007	Created
 *							defect 9805 Ver Special Plates
 * Ray Rowehl	02/19/2007	Work on insert.
 * 							add insSpecialPlateRejectionLog()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	03/20/2007	Modify the qry to not include office in the 
 * 							where clause.  We expect that only HQ will
 * 							run this report.
 * 							modify qrySpecialPlateRejectionLog()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	04/12/2007	Add ErrMsgDesc to the qry result for the 
 * 							report.
 * 							modify qrySpecialPlateRejectionLog()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/03/2007	Modify the query to handle the condition
 * 							where the error message does not exist.
 * 							In this case, return an empty string.
 * 							modify qrySpecialPlateRejectionLog()
 * 							defect 9116 Ver Special Plates
 * K Harrell	01/05/2009	Assign TransEmpId to "LGNERR" if null || 
 * 							TransEmpId.trim().length() == 0 on insert. 
 * 							modify insSpecialPlateRejectionLog()    
 *	 						defect 9847 Ver Defect_POS_D
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeSpecialPlateRejectionLog()
 * 							defect 9825 Ver Defect_POS_D      
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * SpecialPlateRejectionLog  
 * 
 * @version	Defect_POS_D 	01/19/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		02/06/2007  13:41:00  
 */

public class SpecialPlateRejectionLog
{
	DatabaseAccess caDA;

	/**
	 * SpecialPlateRejectionLog constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess
	 * @throws RTSException
	 */
	public SpecialPlateRejectionLog(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_SPCL_PLT_REJ_LOG
	 * 
	 * @param  aaSpecialPlateRejectionLogData SpecialPlateRejectionLogData	
	 * @throws RTSException
	 */
	public void insSpecialPlateRejectionLog(SpecialPlateRejectionLogData aaSpecialPlateRejectionLogData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insSpecialPlateRejectionLog - Begin");

		Vector lvValues = new Vector();

		// defect 9847 
		if (aaSpecialPlateRejectionLogData.getTransEmpId() == null
			|| aaSpecialPlateRejectionLogData
				.getTransEmpId()
				.trim()
				.length()
				== 0)
		{
			aaSpecialPlateRejectionLogData.setTransEmpId(
				CommonConstant.DEFAULT_TRANSEMPID);
		}
		// end defect 9847 

		String lsIns =
			"INSERT into RTS.RTS_SPCL_PLT_REJ_LOG("
				+ "InvItmNo,"
				+ "ReqTimeStmp,"
				+ "MfgPltNo,"
				+ "ItrntReqIndi,"
				+ "RegPltNo,"
				+ "ReqIpAddr,"
				+ "OfcIssuanceNo,"
				+ "TransWsId,"
				+ "TransAmDate,"
				+ "TransEmpId,"
				+ "RejReasnCd"
				+ ")";

		lsIns =
			lsIns
				+ "VALUES ( "
				+ " ?,"
				+ " CURRENT TIMESTAMP,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?"
				+ ")";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSpecialPlateRejectionLogData.getInvItmNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSpecialPlateRejectionLogData.getMfgPltNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialPlateRejectionLogData
							.getItrntReqIndi())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSpecialPlateRejectionLogData.getRegPltNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSpecialPlateRejectionLogData
							.getReqIPAddr())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialPlateRejectionLogData
							.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialPlateRejectionLogData
							.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialPlateRejectionLogData
							.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSpecialPlateRejectionLogData
							.getTransEmpId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialPlateRejectionLogData
							.getRejReasnCd())));

			Log.write(
				Log.SQL,
				this,
				"insSpecialPlateRejectionLog - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insSpecialPlateRejectionLog - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insSpecialPlateRejectionLog - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insSpecialPlateRejectionLog - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_SPCL_PLT_REJ_LOG for purge
	 *
	 * @param  aiPurgeAMDate int
	 * @return int	
	 * @throws RTSException 
	 */
	public int purgeSpecialPlateRejectionLog(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeSpecialPlateRejectionLog - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			" DELETE FROM RTS.RTS_SPCL_PLT_REJ_LOG  "
				+ " WHERE  "
				+ " TRANSAMDATE <= ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));

			Log.write(
				Log.SQL,
				this,
				"purgeSpecialPlateRejectionLog - SQL - Begin");
				
			// defect 9825 
			// Return number of rows purged 
			int liNumRows = caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeSpecialPlateRejectionLog - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeSpecialPlateRejectionLog - End");
			return liNumRows; 
			// end defect 9825 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeSpecialPlateRejectionLog - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query from RTS.RTS_SPCL_PLT_REJ_LOG
	 *
	 * @param 	aaSpecialPlateRejectionLogData SpecialPlateRejectionLogData	
	 * @return  Vector 
	 * @throws 	RTSException 	
	 */
	public Vector qrySpecialPlateRejectionLog(SpecialPlateRejectionLogData aaSpecialPlateRejectionLogData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qrySpecialPlateRejectionLog - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "InvItmNo, "
				+ "ReqTimestmp, "
				+ "RegPltNo, "
				+ "MfgPltNo, "
				+ "ItrntReqIndi, "
				+ "ReqIPAddr, "
				+ "OfcIssuanceNo, "
				+ "TransAMDate, "
				+ "TransWsId, "
				+ "TransEmpId, "
				+ "RejReasnCd, "
				+ "ErrMsgDesc "
				+ "FROM RTS.RTS_SPCL_PLT_REJ_LOG A, "
				+ "RTS.RTS_ERR_MSGS B "
				+ "WHERE "
				+ "TransAMDate = ? "
				+ "AND A.RejReasnCd = B.ErrMSgNo ");

		lsQry.append("UNION ");

		lsQry.append(
			"SELECT "
				+ "InvItmNo, "
				+ "ReqTimestmp, "
				+ "RegPltNo, "
				+ "MfgPltNo, "
				+ "ItrntReqIndi, "
				+ "ReqIPAddr, "
				+ "OfcIssuanceNo, "
				+ "TransAMDate, "
				+ "TransWsId, "
				+ "TransEmpId, "
				+ "RejReasnCd, "
				+ "'' AS ErrMsgDesc "
				+ "FROM RTS.RTS_SPCL_PLT_REJ_LOG A "
				+ "WHERE "
				+ "TransAMDate = ? "
				+ "AND A.RejReasnCd NOT IN "
				+ "(SELECT errmsgno FROM rts.rts_err_msgs) ");

		lsQry.append(
			"ORDER BY TransAMDate, OfcIssuanceNo, InvItmNo, ReqTimestmp");

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialPlateRejectionLogData
							.getTransAMDate())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSpecialPlateRejectionLogData
							.getTransAMDate())));

			Log.write(
				Log.SQL,
				this,
				" - qrySpecialPlateRejectionLog - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qrySpecialPlateRejectionLog - SQL - End");

			while (lrsQry.next())
			{

				SpecialPlateRejectionLogData laSpecialPlateRejectionLogData =
					new SpecialPlateRejectionLogData();
				laSpecialPlateRejectionLogData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laSpecialPlateRejectionLogData.setReqTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ReqTimestmp"));
				laSpecialPlateRejectionLogData.setMfgPltNo(
					caDA.getStringFromDB(lrsQry, "MfgPltNo"));
				laSpecialPlateRejectionLogData.setItrntReqIndi(
					caDA.getIntFromDB(lrsQry, "ItrntReqIndi"));
				laSpecialPlateRejectionLogData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));
				laSpecialPlateRejectionLogData.setReqIPAddr(
					caDA.getStringFromDB(lrsQry, "ReqIPAddr"));
				laSpecialPlateRejectionLogData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laSpecialPlateRejectionLogData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laSpecialPlateRejectionLogData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laSpecialPlateRejectionLogData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laSpecialPlateRejectionLogData.setRejReasnCd(
					caDA.getIntFromDB(lrsQry, "RejReasnCd"));
				laSpecialPlateRejectionLogData.setErrMsgDesc(
					caDA.getStringFromDB(lrsQry, "ErrMsgDesc"));

				// Add element to the Vector
				lvRslt.addElement(laSpecialPlateRejectionLogData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qrySpecialPlateRejectionLog - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySpecialPlateRejectionLog - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
