package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.NameAddressData;
import com.txdot.isd.rts.services.data.WebAgentSecurityData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 * WebAgentSecurity.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708 Ver 6.7.0
 * K Harrell	01/05/2011	Renamings per standards 
 * 							defect 10708 Ver 6.7.0 
 * K Harrell	01/10/2011	add SubmitBatchAccs 
 * 				   			defect 10708 Ver 6.7.0
 * K Harrell	02/02/2011	add qryWebAgentSecurity(int)
 * 							*** May cosolidate  
 * 							defect 10708 Ver 6.7.0
 * K Harrell	03/31/2011	add join to RTS_WEB_AGNT, RTS_WEB_AGNCY 
 * 							modify qryWebAgentSecurity() 
 * 							defect 10785 Ver 6.7.1     
 * K Harrell	04/06/2011	modify join to exclude RTS_WEB_AGNT
 * 							remove reference to DMVUSERINDI
 * 							modify qryWebAgentSecurity() 
 * 							defect 10785 Ver 6.7.1     	
 * K Harrell	04/26/2011	remove reference to DashAccs 
 * 							defect 10708 Ver 6.7.0 
 * Ray Rowehl	06/15/2011	Make agncyIdntyNo optional.
 * 							modify qryWebAgentSecurity(int,int)
 * 							defect 10718 Ver 6.8.0	
 * Ray Rowehl	06/28/2011	Make agntIdntyNo optional.
 * 							modify qryWebAgentSecurity(int,int)
 * 							defect 10718 Ver 6.8.0	
 * Ray Rowehl	06/30/2011	The column is AGNTAUTHACCS, not USERAUTHACCS
 * 							modify insWebAgentSecurity()
 * 							defect 10718 Ver 6.8.0			
 * Kathy McKEe  07/01/2011  Added SubmitBatchAccs	 
 * 							modify updWebAgentSecurity()
 * 							defect 10718 Ver 6.8.0	
 * Kathy McKee	09/01/2011	Added UpdtngAgntIdntyNo
 * 							method updWebAgentSecurity()
 * 							defect 10729 Ver 6.8.1
 * Kathy McKee	09/07/2011	Added method qryAgntIdntyNo
 * 							defect 10729 Ver 6.8.1
 * Kathy McKee  09/28/2011  Added qryWebAgentSecurityForAgent 
 * 							added method updWebAgentSecurity()
 * 							defect 10729 Ver 6.8.1
 * Kathy McKee  11/03/2011  Modify qryWebAgentSecurityForAgent
 * 							added Agency Address
 * 	                        defect 11146 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_WEB_AGENT_SECURITY 
 *
 * @version	6.9.0			11/03/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 20:28:17
 */

public class WebAgentSecurity
{
	private DatabaseAccess caDA;
	private String csMethod;

	/**
	 * WebAgentSecurity Constructor
	 * 
	 */
	public WebAgentSecurity(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS_WEB_AGNT_SECURITY
	 *
	 * @param aiAgntIdntyNo 
	 * @param aiAgncyIdntyNo
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgentSecurity(
		int aiAgntIdntyNo,
		int aiAgncyIdntyNo)
		throws RTSException
	{
		csMethod = "qryWebAgentSecurity";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "AGNTSECRTYIDNTYNO,"
				+ "AGNTIDNTYNO,"
				+ "AGNCYIDNTYNO,"
				+ "AGNCYAUTHACCS,"
				+ "AGNTAUTHACCS,"
				+ "AGNCYINFOACCS,"
				+ "BATCHACCS,"
				+ "RENWLACCS,"
				+ "REPRNTACCS,"
				+ "VOIDACCS,"
				+ "RPTACCS,"
				+ "APRVBATCHACCS,"
				+ "SUBMITBATCHACCS,"
				+ "DELETEINDI,"
				+ "CHNGTIMESTMP "
				+ "from RTS.RTS_WEB_AGNT_SECURITY "
				+ "WHERE DELETEINDI = 0 ");
				
		if (aiAgntIdntyNo > 0)
		{			lsQry.append("AND AGNTIDNTYNO  = ? ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiAgntIdntyNo)));
		}

		if (aiAgncyIdntyNo > 0)
		{
			lsQry.append("AND AGNCYIDNTYNO  = ?");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiAgncyIdntyNo)));
		}

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgentSecurityData laData =
					new WebAgentSecurityData();

				laData.setAgntSecrtyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntSecrtyIdntyNo"));

				laData.setAgntIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntIdntyNo"));

				laData.setAgncyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyIdntyNo"));

				laData.setAgncyAuthAccs(
					caDA.getIntFromDB(lrsQry, "AgncyAuthAccs"));

				laData.setAgntAuthAccs(
					caDA.getIntFromDB(lrsQry, "AgntAuthAccs"));

				laData.setAgncyInfoAccs(
					caDA.getIntFromDB(lrsQry, "AgncyInfoAccs"));

				laData.setBatchAccs(
					caDA.getIntFromDB(lrsQry, "BatchAccs"));

				laData.setRenwlAccs(
					caDA.getIntFromDB(lrsQry, "RenwlAccs"));

				laData.setReprntAccs(
					caDA.getIntFromDB(lrsQry, "ReprntAccs"));

				laData.setVoidAccs(
					caDA.getIntFromDB(lrsQry, "VoidAccs"));

				laData.setRptAccs(
					caDA.getIntFromDB(lrsQry, "RptAccs"));

				laData.setAprvBatchAccs(
					caDA.getIntFromDB(lrsQry, "AprvBatchAccs"));

				laData.setSubmitBatchAccs(
					caDA.getIntFromDB(lrsQry, "SubmitBatchAccs"));

				laData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));

				laData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));

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
	 * Method to Query RTS_WEB_AGNT_SECURITY
	 *
	 * @param aiAgntSecrtyIdntyNo 
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgentSecurity(int aiAgntSecrtyIdntyNo)
		throws RTSException
	{
		csMethod = "qryWebAgentSecurity";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "A.AGNTSECRTYIDNTYNO,"
				+ "A.AGNTIDNTYNO,"
				+ "A.AGNCYIDNTYNO,"
				+ "A.AGNCYAUTHACCS,"
				+ "A.AGNTAUTHACCS,"
				+ "A.AGNCYINFOACCS,"
				+ "A.BATCHACCS,"
				+ "A.RENWLACCS,"
				+ "A.REPRNTACCS,"
				+ "A.VOIDACCS,"
				+ "A.RPTACCS,"
				+ "A.APRVBATCHACCS,"
				+ "A.SUBMITBATCHACCS,"
				+ "A.DELETEINDI,"
				+ "A.CHNGTIMESTMP, "
				+ "B.AGNCYTYPECD "
				+ "from RTS.RTS_WEB_AGNT_SECURITY A,"
				+ " RTS.RTS_WEB_AGNCY B "
				+ "WHERE A.AGNTSECRTYIDNTYNO  = ? AND "
				+ "A.AGNCYIDNTYNO = B.AGNCYIDNTYNO ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiAgntSecrtyIdntyNo)));

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgentSecurityData laData =
					new WebAgentSecurityData();

				laData.setAgntSecrtyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntSecrtyIdntyNo"));

				laData.setAgntIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntIdntyNo"));

				laData.setAgncyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyIdntyNo"));

				laData.setAgncyAuthAccs(
					caDA.getIntFromDB(lrsQry, "AgncyAuthAccs"));

				laData.setAgntAuthAccs(
					caDA.getIntFromDB(lrsQry, "AgntAuthAccs"));

				laData.setAgncyInfoAccs(
					caDA.getIntFromDB(lrsQry, "AgncyInfoAccs"));

				laData.setBatchAccs(
					caDA.getIntFromDB(lrsQry, "BatchAccs"));

				laData.setRenwlAccs(
					caDA.getIntFromDB(lrsQry, "RenwlAccs"));

				laData.setReprntAccs(
					caDA.getIntFromDB(lrsQry, "ReprntAccs"));

				laData.setVoidAccs(
					caDA.getIntFromDB(lrsQry, "VoidAccs"));

				laData.setRptAccs(
					caDA.getIntFromDB(lrsQry, "RptAccs"));

				laData.setAprvBatchAccs(
					caDA.getIntFromDB(lrsQry, "AprvBatchAccs"));

				laData.setSubmitBatchAccs(
					caDA.getIntFromDB(lrsQry, "SubmitBatchAccs"));

				laData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));

				laData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));

				laData.setAgncyTypeCd(
					caDA.getStringFromDB(lrsQry, "AgncyTypeCd"));

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
	 * Method to Query RTS_WEB_AGNT_SECURITY
	 *
	 * @param aiAgntIdntyNo 
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryWebAgentSecurityForAgent(int aiAgntIdntyNo)
		throws RTSException
	{
		csMethod = "qryWebAgentSecurityForAgent";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();
		
		// defect 11146 -- added Agency name and address

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "A.AGNTSECRTYIDNTYNO,"
				+ "A.AGNTIDNTYNO,"
				+ "A.AGNCYIDNTYNO,"
				+ "A.AGNCYAUTHACCS,"
				+ "A.AGNTAUTHACCS,"
				+ "A.AGNCYINFOACCS,"
				+ "A.BATCHACCS,"
				+ "A.RENWLACCS,"
				+ "A.REPRNTACCS,"
				+ "A.VOIDACCS,"
				+ "A.RPTACCS,"
				+ "A.APRVBATCHACCS,"
				+ "A.SUBMITBATCHACCS,"
				+ "A.DELETEINDI,"
				+ "A.CHNGTIMESTMP, "
				+ "B.AGNCYTYPECD, "
				+ "B.NAME1,"  
				+ "B.NAME2,"
				+ "B.ST1,"
				+ "B.ST2,"
				+ "B.CITY,"
				+ "B.STATE,"
				+ "B.ZPCD,"
				+ "B.ZPCDP4 "
				+ "from RTS.RTS_WEB_AGNT_SECURITY A,"
				+ " RTS.RTS_WEB_AGNCY B "
				+ "WHERE A.AGNTIDNTYNO  = ?  "
				+ " AND A.DELETEINDI = 0"
				+ " AND B.DELETEINDI = 0"
				+ " AND A.AGNCYIDNTYNO = B.AGNCYIDNTYNO ")
				;

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiAgntIdntyNo)));

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgentSecurityData laData =
					new WebAgentSecurityData();

				laData.setAgntSecrtyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntSecrtyIdntyNo"));

				laData.setAgntIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgntIdntyNo"));

				laData.setAgncyIdntyNo(
					caDA.getIntFromDB(lrsQry, "AgncyIdntyNo"));

				laData.setAgncyAuthAccs(
					caDA.getIntFromDB(lrsQry, "AgncyAuthAccs"));

				laData.setAgntAuthAccs(
					caDA.getIntFromDB(lrsQry, "AgntAuthAccs"));

				laData.setAgncyInfoAccs(
					caDA.getIntFromDB(lrsQry, "AgncyInfoAccs"));

				laData.setBatchAccs(
					caDA.getIntFromDB(lrsQry, "BatchAccs"));

				laData.setRenwlAccs(
					caDA.getIntFromDB(lrsQry, "RenwlAccs"));

				laData.setReprntAccs(
					caDA.getIntFromDB(lrsQry, "ReprntAccs"));

				laData.setVoidAccs(
					caDA.getIntFromDB(lrsQry, "VoidAccs"));

				laData.setRptAccs(
					caDA.getIntFromDB(lrsQry, "RptAccs"));

				laData.setAprvBatchAccs(
					caDA.getIntFromDB(lrsQry, "AprvBatchAccs"));

				laData.setSubmitBatchAccs(
					caDA.getIntFromDB(lrsQry, "SubmitBatchAccs"));

				laData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));

				laData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));

				laData.setAgncyTypeCd(
					caDA.getStringFromDB(lrsQry, "AgncyTypeCd"));
				
				
				// defect 11146
							
				NameAddressData laAddrNameData = new NameAddressData();

				laAddrNameData.getAddressData().setSt1(
					caDA.getStringFromDB(lrsQry, "St1"));

				laAddrNameData.getAddressData().setSt2(
					caDA.getStringFromDB(lrsQry, "St2"));

				laAddrNameData.getAddressData().setCity(
					caDA.getStringFromDB(lrsQry, "City"));

				laAddrNameData.getAddressData().setState(
					caDA.getStringFromDB(lrsQry, "State"));

				laAddrNameData.getAddressData().setZpcd(
					caDA.getStringFromDB(lrsQry, "ZpCd"));

				laAddrNameData.getAddressData().setZpcdp4(
					caDA.getStringFromDB(lrsQry, "ZpCdP4"));
				
				laAddrNameData.setName1( 
					caDA.getStringFromDB(lrsQry, "NAME1"));
				
				laAddrNameData.setName2( 
					caDA.getStringFromDB(lrsQry, "NAME2"));
				
				laData.setAgncyNameAddress(laAddrNameData);
				
				// end defect 11146

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
	 * Method to Insert into RTS.RTS_WEB_AGNT_SECURITIY 
	 *
	 * @param  aaData	WebAgentSecurityData	
	 * @throws RTSException 
	 */
	public WebAgentSecurityData insWebAgentSecurity(WebAgentSecurityData aaData)
		throws RTSException
	{
		csMethod = "insWebAgentSecurity";

		Vector lvValues = new Vector();

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		String lsIns =
			"INSERT into RTS.RTS_WEB_AGNT_SECURITY("
				+ "AGNTIDNTYNO,"
				+ "AGNCYIDNTYNO,"
				+ "AGNCYAUTHACCS,"
				+ "AGNTAUTHACCS,"
				+ "AGNCYINFOACCS,"
				+ "BATCHACCS,"
				+ "RENWLACCS,"
				+ "REPRNTACCS,"
				+ "VOIDACCS,"
				+ "RPTACCS,"
				+ "APRVBATCHACCS,"
				+ "SUBMITBATCHACCS,"
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
				+ " CURRENT TIMESTAMP)";

		try
		{
			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgntIdntyNo())));
			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyIdntyNo())));
			// 3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyAuthAccs())));

			// 4
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgntAuthAccs())));

			// 5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyInfoAccs())));

			// 6
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getBatchAccs())));
			// 7
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getRenwlAccs())));
			// 8
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getReprntAccs())));
			// 9
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getVoidAccs())));
			// 10
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getRptAccs())));
			// 11
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAprvBatchAccs())));

			// 12
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getSubmitBatchAccs())));

			// 13
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));

			// 14
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getUpdtngAgntIdntyNo())));
			
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			ResultSet lrsQry;
			String lsSel =
				"Select IDENTITY_VAL_LOCAL() as AgntSecrtyIdntyNo from "
					+ " RTS.RTS_WEB_AGNT_SECURITY";

			lrsQry = caDA.executeDBQuery(lsSel, null);
			int liIdntyNo = 0;

			while (lrsQry.next())
			{
				liIdntyNo =
					caDA.getIntFromDB(lrsQry, "AgntSecrtyIdntyNo");
				aaData.setAgntSecrtyIdntyNo(liIdntyNo);
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
	 * Method to update RTS.RTS_WEB_AGNT_SECURITY
	 * 
	 * @param  aaData WebAgentSecurityData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updWebAgentSecurity(WebAgentSecurityData aaData)
		throws RTSException
	{
		csMethod = "updWebAgentSecurity";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNT_SECURITY "
					+ "SET "
					+ "AGNCYAUTHACCS = ?,"
					+ "AGNTAUTHACCS = ?,"
					+ "AGNCYINFOACCS = ?,"
					+ "BATCHACCS = ?,"
					+ "RENWLACCS = ?,"
					+ "REPRNTACCS= ?,"
					+ "VOIDACCS= ?,"
					+ "RPTACCS= ?,"
					+ "APRVBATCHACCS= ?,"
					+ "SUBMITBATCHACCS= ?,"
					+ " DeleteIndi = ?, "
					+ "UpdtngAgntIdntyNo = ?, "					
					+ " ChngTimestmp = Current Timestamp "
					+ " where "
					+ " AgntSecrtyIdntyNo = ?";

			// 1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyAuthAccs())));

			// 2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgntAuthAccs())));

			// 3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgncyInfoAccs())));
		
			// 4
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getBatchAccs())));
			// 5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getRenwlAccs())));
			// 6
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getReprntAccs())));
			// 7
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getVoidAccs())));
			// 8
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getRptAccs())));
			// 9
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAprvBatchAccs())));
			// 10
			lvValues.addElement(
			    new DBValue(
			    	Types.INTEGER,
			    	DatabaseAccess.convertToString(
			    		aaData.getSubmitBatchAccs())));
			// 11
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getDeleteIndi())));
			// 12
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getUpdtngAgntIdntyNo())));
			// 13
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaData.getAgntSecrtyIdntyNo())));

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
	 * Method to Query RTS_WEB_AGNT_SECURITY for the AgntIdntyNo.
	 *
	 * @param int aiAgntSecrtyIdntyNo 
	 * @return  Vector
	 * @throws 	RTSException 
	 */
	public Vector qryAgntIdntyNo(int aiAgntSecrtyIdntyNo)
		throws RTSException
	{
		csMethod = "qryAgntIdntyNo";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "AGNTIDNTYNO "
				+ "from RTS.RTS_WEB_AGNT_SECURITY  "
				+ "WHERE AGNTSECRTYIDNTYNO  = ? ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiAgntSecrtyIdntyNo)));

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				WebAgentSecurityData laData =
					new WebAgentSecurityData();

				laData.setAgntIdntyNo(
					caDA.getIntFromDB(lrsQry, "AGNTIDNTYNO"));
	
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
	 * Method to delete RTS.RTS_WEB_AGNT_SECURITY
	 * 
	 * @param  int aiAgntSecrtyIdntyNo 
	 * @return int
	 * @throws RTSException 
	 */
	public int delWebAgentSecurity(int aiAgncyIdntyNo)
		throws RTSException
	{
		csMethod = "delWebAgentSecurity";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		try
		{
			String lsUpd =
				"UPDATE RTS.RTS_WEB_AGNT_SECURITY "
					+ "SET "
					+ " DeleteIndi = 1, "			
					+ " ChngTimestmp = Current Timestamp "
					+ " where "
					+ " DeleteIndi = 0 AND " 
					+ " AgncyIdntyNo = ?";
			// 1

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiAgncyIdntyNo)));

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
	} //END OF Delete METHOD
}
