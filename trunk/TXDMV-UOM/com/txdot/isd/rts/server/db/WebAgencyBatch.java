package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.WebAgencyBatchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * WebAgencyBatch.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708 Ver 6.7.0
 * K Harrell 	01/05/2011 	Renamings per standards 
 *        					defect 10708 Ver 6.7.0 
 * Ray Rowehl	02/16/2011	Add BatchStatusCd to where clause in qry.
 * 							change qryWebAgencyBatch() 
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/23/2011	Make status update flexible to handle both
 * 							Submit and Approval.
 * 							change updWebAgencyBatch(WebAgencyBatchData)
 * 							defect 10673 Ver 6.7.0
 * K Harrell	03/21/2011	add updWebAgencyBatchForLogicalLock()	
 * 							modify updWebAgencyBatch(WebAgencyBatchData)
 * 							defect 10768 Ver 6.7.1 
 * K Harrell	03/28/2011	Update for new data elements
 * 							delete qryMaxWebAgencyBatch() 
 * 							modify qryWebAgencyBatch()  
 * 							defect 10785 Ver 6.7.1 
 * K Harrell	04/05/2011 	Implement Search Date Criteria
 * 							modify qryWebAgencyBatch() 
 * 							defect 10785 Ver 6.7.1 
 * K Harrell	04/12/2011	modify updWebAgencyBatch()
 * 							defect 10785 Ver 6.7.1 
 * K McKee	    08/15/2011	add qryPendingWebAgencyBatch()
 * 							defect 10769 Ver 6.8.1 
 * K McKee	    08/22/2011	removed qryPendingWebAgencyBatch()
 * 							added updPendingWebAgencyBatch()
 * 							defect 10769 Ver 6.8.1 
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_WEB_AGNCY_BATCH 
 *
 * @version	6.8.1			08/15/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 20:28:17
 */

public class WebAgencyBatch
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgencyBatch.java Constructor
	 * 
	 */
	public WebAgencyBatch(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_WEB_AGNCY_BATCH 
	 *
	 * @param  aaData	WebAgencyAuthData
	 * @return WebAgencyBatchData
	 * @throws RTSException 
	 */
	public WebAgencyBatchData insWebAgencyBatch(WebAgencyBatchData aaData)
		throws RTSException
	{
		csMethod = "insWebAgencyBatch";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsIns =
			"INSERT into RTS.RTS_WEB_AGNCY_BATCH("
				+ "OfcIssuanceNo,"
				+ "AgncyIdntyNo,"
				+ "BatchInitTimestmp,"
				+ "BatchStatusCd) "
				+ " VALUES ( "
				+ " ?,"
				+ " ?,"
				+ "CURRENT TIMESTAMP,"
				+ " ?)";

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getOfcIssuanceNo())));

			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyIdntyNo())));

			// 3
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getBatchStatusCd())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			ResultSet lrsQry;
			String lsSel =
				"Select IDENTITY_VAL_LOCAL() as AgncyBatchIdntyNo from "
					+ " RTS.RTS_WEB_AGNCY_BATCH";

			lrsQry = caDA.executeDBQuery(lsSel, null);
			int liIdntyNo = 0;

			while (lrsQry.next())
			{
				liIdntyNo =
					caDA.getIntFromDB(lrsQry, "AgncyBatchIdntyNo");
				aaData.setAgncyBatchIdntyNo(liIdntyNo);
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
	 * Purge RTS_WEB_AGNCY_BATCH
	 * 
	 * @param aiPurgeDays int
	 * @return int
	 * @throws RTSException
	 */
	public int purgeWebAgencyBatch(int aiPurgeDays) throws RTSException
	{
		csMethod = "purgeWebAgencyBatch";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlDelData =
			"DELETE FROM RTS.RTS_WEB_AGNCY_BATCH "
				+ " WHERE "
				+ " BATCHCOMPLETETIMESTMP "
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
	 * Method to Query RTS_WEB_AGNCY_BATCH
	 *
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgencyBatchForApprove(WebAgencyBatchData aaWABatchData)
		throws RTSException
	{
		csMethod = "qryWebAgencyBatchForApprove";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "AGNCYBATCHIDNTYNO,"
				+ "AGNCYIDNTYNO,"
				+ "OFCISSUANCENO,"
				+ "BATCHINITTIMESTMP,"
				+ "BATCHCLOSETIMESTMP,"
				+ "BATCHSUBMITTIMESTMP,"
				+ "BATCHAPPRVTIMESTMP,"
				+ "BATCHCOMPLETETIMESTMP,"
				+ "SUBMITAGNTSECRTYIDNTYNO,"
				+ "TRANSWSID,"
				+ "TRANSAMDATE,"
				+ "TRANSEMPID,"
				+ "CUSTSEQNO,"
				+ "BATCHSTATUSCD  "
				+ "from RTS.RTS_WEB_AGNCY_BATCH ");

		boolean lbWhere = false;
		String lsAdd = new String();

		if (aaWABatchData.getOfcIssuanceNo() > 0)
		{
			lbWhere = true;
			lsAdd = " OFCISSUANCENO = ?";
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWABatchData.getOfcIssuanceNo())));

		}
		if (aaWABatchData.getAgncyBatchIdntyNo() > 0)
		{

			lsAdd =
				lsAdd
					+ (lbWhere ? " AND " : "")
					+ " AGNCYBATCHIDNTYNO = ?";

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWABatchData.getAgncyBatchIdntyNo())));
			lbWhere = true;

		}
		if (aaWABatchData.getAgncyIdntyNo() > 0)
		{
			lsAdd =
				lsAdd + (lbWhere ? " AND " : "") + " AGNCYIDNTYNO = ?";
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWABatchData.getAgncyIdntyNo())));
			lbWhere = true;
		}

		// defect 10670
		if (!UtilityMethods.isEmpty(aaWABatchData.getBatchStatusCd()))
		{
			lsAdd =
				lsAdd + (lbWhere ? " AND " : "") + " BATCHSTATUSCD = ?";
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWABatchData.getBatchStatusCd())));
			lbWhere = true;
		}
		// end defect 10670

		if (lbWhere)
		{
			lsQry.append("WHERE " + lsAdd);
		}

		lsQry.append(" ORDER BY AGNCYBATCHIDNTYNO");

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgencyBatchData laData = new WebAgencyBatchData();

				laData.setAgncyBatchIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyBatchIdntyNo"));

				laData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));

				laData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laData.setBatchStatusCd(
					caDA.getStringFromDB(lrsQry, "BatchStatusCd"));
				laData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));

				laData.setAgncyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyIdntyNo"));

				laData.setSubmitAgntSecrtyIdntyNo(
					caDA.getIntFromDB(
						lrsQry,
						"SubmitAgntSecrtyIdntyNo"));

				laData.setBatchInitTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "BatchInitTimestmp"));

				laData.setBatchCloseTimestmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"BatchCloseTimestmp"));

				laData.setBatchSubmitTimestmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"BatchSubmitTimestmp"));

				laData.setBatchApprvTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ApprvTimestmp"));

				laData.setBatchCompleteTimestmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"BatchCompleteTimestmp"));

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
	 * Method to Query RTS_WEB_AGNCY_BATCH
	 *
	 * @param aaWABatchData
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgencyBatch(WebAgencyBatchData aaWABatchData)
		throws RTSException
	{
		return qryWebAgencyBatch(aaWABatchData, true);
	}

	/**
	 * Method to Query RTS_WEB_AGNCY_BATCH
	 *
	 * @param aaWABatchData
	 * @param abTransExist 
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgencyBatch(
		WebAgencyBatchData aaWABatchData,
		boolean abTransExist)
		throws RTSException
	{
		csMethod = "qryWebAgencyBatch";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "AGNCYBATCHIDNTYNO,"
				+ "AGNCYIDNTYNO,"
				+ "OFCISSUANCENO,"
				+ "BATCHINITTIMESTMP,"
				+ "BATCHCLOSETIMESTMP,"
				+ "BATCHSUBMITTIMESTMP,"
				+ "BATCHAPPRVTIMESTMP,"
				+ "BATCHCOMPLETETIMESTMP,"
				+ "SUBMITAGNTSECRTYIDNTYNO,"
				+ "TRANSWSID,"
				+ "TRANSAMDATE,"
				+ "TRANSEMPID,"
				+ "CUSTSEQNO,"
				+ "BATCHSTATUSCD  "
				+ "from RTS.RTS_WEB_AGNCY_BATCH A ");

		boolean lbWhere = false;
		String lsAdd = new String();

		if (aaWABatchData.getOfcIssuanceNo() > 0)
		{
			lbWhere = true;
			lsAdd = " OFCISSUANCENO = ?";
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWABatchData.getOfcIssuanceNo())));

		}
		if (aaWABatchData.getAgncyBatchIdntyNo() > 0)
		{

			lsAdd =
				lsAdd
					+ (lbWhere ? " AND " : "")
					+ " AGNCYBATCHIDNTYNO = ?";

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWABatchData.getAgncyBatchIdntyNo())));
			lbWhere = true;

		}
		if (aaWABatchData.getAgncyIdntyNo() > 0)
		{
			lsAdd =
				lsAdd + (lbWhere ? " AND " : "") + " AGNCYIDNTYNO = ?";
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaWABatchData.getAgncyIdntyNo())));
			lbWhere = true;
		}

		// defect 10670
		if (!UtilityMethods.isEmpty(aaWABatchData.getBatchStatusCd()))
		{
			lsAdd =
				lsAdd + (lbWhere ? " AND " : "") + " BATCHSTATUSCD = ?";
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaWABatchData.getBatchStatusCd())));
			lbWhere = true;
		}
		// end defect 10670

		if (aaWABatchData.getBatchCompleteTimestmp() != null)
		{
			lsAdd =
				lsAdd
					+ (lbWhere ? " AND " : "")
					+ " BATCHCOMPLETETIMESTMP IS NULL ";
			lbWhere = true;
		}

		if (aaWABatchData.getSearchStartDate() != null)
		{
			String lsStartDate =
				"'"
					+ aaWABatchData.getSearchStartDate().toString()
					+ "' ";

			lsAdd =
				lsAdd
					+ (lbWhere ? " AND " : "")
					+ " DATE(BATCHINITTIMESTMP) >= "
					+ lsStartDate;
			lbWhere = true;
		}

		if (aaWABatchData.getSearchEndDate() != null)
		{
			String lsEndDate =
				"'"
					+ aaWABatchData.getSearchEndDate().toString()
					+ "' ";

			lsAdd =
				lsAdd
					+ (lbWhere ? " AND " : "")
					+ " DATE(BATCHINITTIMESTMP) <=  "
					+ lsEndDate;

			lbWhere = true;
		}

		if (lbWhere)
		{
			lsQry.append("WHERE " + lsAdd);

			if (abTransExist)
			{
				lsQry.append(
					" and exists (select * from RTS.RTS_WEB_AGNCY_TRANS B "
						+ " WHERE A.AGNCYBATCHIDNTYNO = B.AGNCYBATCHIDNTYNO "
						+ " AND B.ACCPTVEHINDI = 1)");
			}
		}

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgencyBatchData laData = new WebAgencyBatchData();

				laData.setAgncyBatchIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyBatchIdntyNo"));
				laData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laData.setBatchStatusCd(
					caDA.getStringFromDB(lrsQry, "BatchStatusCd"));
				laData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laData.setAgncyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyIdntyNo"));
				laData.setSubmitAgntSecrtyIdntyNo(
					caDA.getIntFromDB(
						lrsQry,
						"SubmitAgntSecrtyIdntyNo"));
				laData.setBatchInitTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "BatchInitTimestmp"));
				laData.setBatchCloseTimestmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"BatchCloseTimestmp"));
				laData.setBatchSubmitTimestmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"BatchSubmitTimestmp"));
				laData.setBatchApprvTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ApprvTimestmp"));
				laData.setBatchCompleteTimestmp(
					caDA.getRTSDateFromDB(
						lrsQry,
						"BatchCompleteTimestmp"));

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
	 * Method to update RTS.RTS_WEB_AGNCY_BATCH
	 * 
	 * @param  aaData WebAgencyBatchData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updWebAgencyBatch(WebAgencyBatchData aaData)
		throws RTSException
	{
		csMethod = "updWebAgencyBatch";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			StringBuffer lsUpd =
				new StringBuffer("UPDATE RTS.RTS_WEB_AGNCY_BATCH SET BatchStatusCd = ? ");

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getBatchStatusCd())));

			if (aaData.getBatchCloseTimestmp() != null)
			{
				lsUpd.append(", BatchCloseTimestmp = ? ");
				lvValues.addElement(
					new DBValue(
						Types.TIMESTAMP,
						DatabaseAccess.convertToString(
							aaData.getBatchCloseTimestmp())));
			}
			if (aaData.getBatchSubmitTimestmp() != null)
			{
				lsUpd.append(", BatchSubmitTimestmp = ? ");
				lvValues.addElement(
					new DBValue(
						Types.TIMESTAMP,
						DatabaseAccess.convertToString(
							aaData.getBatchSubmitTimestmp())));

				lsUpd.append(", SubmitAgntSecrtyIdntyNo = ? ");

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaData.getSubmitAgntSecrtyIdntyNo())));

			}
			if (aaData.getBatchApprvTimestmp() != null)
			{
				lsUpd.append(", BatchApprvTimestmp = ? ");
				lvValues.addElement(
					new DBValue(
						Types.TIMESTAMP,
						DatabaseAccess.convertToString(
							aaData.getBatchApprvTimestmp())));
			}

			if (aaData.getBatchCompleteTimestmp() != null)
			{
				lsUpd.append(", BatchCompleteTimestmp = ? ");
				lvValues.addElement(
					new DBValue(
						Types.TIMESTAMP,
						DatabaseAccess.convertToString(
							aaData.getBatchCompleteTimestmp())));
			}
			if (!UtilityMethods.isEmpty(aaData.getTransEmpId()))
			{
				lsUpd.append(", TransEmpId = ? ");
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaData.getTransEmpId())));
			}
			if (aaData.getTransAMDate() != 0)
			{
				lsUpd.append(", TransAMDate = ? ");
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaData.getTransAMDate())));
			}
			if (aaData.getTransWsId() != Integer.MIN_VALUE)
			{
				lsUpd.append(", TransWsId = ? ");
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaData.getTransWsId())));
			}

			if (aaData.getCustSeqNo() >= 0)
			{
				lsUpd.append(", CustSeqNo = ? ");
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaData.getCustSeqNo())));
			}

			lsUpd.append("WHERE AgncyBatchIdntyNo = ?");

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyBatchIdntyNo())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsUpd.toString(),
					lvValues);

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
	 * Method to update RTS.RTS_WEB_AGNCY_BATCH
	 * 
	 * @param  aaData WebAgencyBatchData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updWebAgencyBatchCustSeqNo(WebAgencyBatchData aaData)
		throws RTSException
	{
		csMethod = "updWebAgencyBatchCustSeqNo";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNCY_BATCH SET CustSeqNo = ? "
					+ "WHERE AgncyBatchIdntyNo = ?";

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getCustSeqNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyBatchIdntyNo())));

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
	 * Update RTS.RTS_WEB_AGNCY_BATCH for Logical Lock
	 * 
	 * @param  aaData WebAgencyBatchData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updWebAgencyBatchForLogicalLock(WebAgencyBatchData aaData)
		throws RTSException
	{
		csMethod = "updWebAgencyBatchForLogicalLock";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNCY_BATCH SET OfcIssuanceNo = ?, "
					+ "AgncyIdntyNo = ? "
					+ "WHERE AgncyBatchIdntyNo = ?";

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getOfcIssuanceNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyIdntyNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyBatchIdntyNo())));

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
	 * Method to Query non submitted RTS_WEB_AGNCY_BATCH for
	 * a specified AGNCYIDNTYNO and OFCISSUANCENO
	 *
	 * @param aaWABatchData
	 * @return  int
	 * @throws 	RTSException 
	 */
	public int updPendingWebAgencyBatch(
		WebAgencyBatchData aaWABatchData)
		throws RTSException
	{
		csMethod = "updPendingWebAgencyBatch";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();
			
		StringBuffer lsUpd =
			new StringBuffer("UPDATE RTS.RTS_WEB_AGNCY_BATCH SET " +
					"BATCHSUBMITTIMESTMP = Current Timestamp, " + 
					"BatchStatusCd = ?  ");

		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString("S")));
		lsUpd.append(
			   " WHERE BATCHSUBMITTIMESTMP IS NULL ");

	
		lsUpd.append( "AND  OFCISSUANCENO = ?");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaWABatchData.getOfcIssuanceNo())));
		
		lsUpd.append( " AND AGNCYIDNTYNO = ?");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaWABatchData.getAgncyIdntyNo())));
	
		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsUpd.toString(),
					lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
		
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (liNumRows);
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
