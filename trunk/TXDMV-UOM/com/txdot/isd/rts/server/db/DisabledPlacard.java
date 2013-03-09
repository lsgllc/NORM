package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.webservices.dsabldplcrd.data.RtsDsabldPlcrdResponse;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * DisabledPlacard.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	10/30/2008	Use Transaction  VOIDEDTRANSINDI vs. 
 * 							Placard VOIDEDINDI 
 * 							modify qryReportDisabledPlacard()
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	11/08/2008	Modified SQL for Full Export 
 * 							modify qryExportDisabledPlacard()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/17/2008	Move addition of separator in building
 * 							export file. 
 * 							modify qryExportDisabledPlacard()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	09/07/2009	Add new Transcds for Disabled Placard 
 * 							modify qryReportDisabledPlacard()
 * 							defect 10133 Ver Defect_POS_F 
 * K Harrell	10/16/2009	delete qryExportDisabledPlacard(boolean)
 * 							add qryExportDisabledPlacard(boolean,int)
 * 							defect 9942 Ver Defect_POS_G 
 * K Harrell	09/25/2010	add query for Web Services 
 * 							add qryDisabledPlacardWeb() 
 * 							defect 10607 Ver 6.6.0   
 * K Harrell	10/23/2010	order by RTSEffDate desc 
 * 							modify qryDisabledPlacardWeb()
 * 							defect 10607 Ver 6.6.0 
 * K Harrell	01/11/2012	modify for Renew and Reinstate Placards 
 * 							modify qryDisabledPlacard(), 
 * 								qryReportDisabledPlacard()
 * 							defect 11214 Ver 6.10.0 
 * K Harrell	02/06/2012	add Delete Reason to Disabled Placard 
 * 							Report SQL
 * 							modify qryReportDisabledPlacard() 
 * 							defect 11279 Ver 6.10.0 
 * K Harrell	02/20/2012	add RTS_DSABLD_PLCRD_TRANS to join and 	
 * 							verify that not voided so that deleted shows
 * 							just once.  
 * 							defect 11214 Ver 6.10.0 
 * R Pilon		02/21/2012	changed method calls to bean RtsDsabldPlcrdResponse 
 * 							  setter methods
 * 							modify qryDisabledPlacardWeb()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_DSABLD_PLCRD 
 *
 * @version	6.10.0 			02/21/2012
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008 
 */
public class DisabledPlacard extends DisabledPlacardData
{
	DatabaseAccess caDA;

	// defect 10607 
	String csMethod = new String();
	// end defect 10607  

	/**
	 * DisabledPlacard constructor comment.
	 *
	 * @param  aaDA	DatabaseAccess 
	 * @throws RTSException
	 */
	public DisabledPlacard(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to build list of offices for "IN" clause
	 *
	 * @return String
	 * @throws RTSException 
	 */
	public String getOffices(Vector avOffices)
	{
		String lsOffices = "(" + avOffices.get(0);

		for (int i = 1; i < avOffices.size(); i++)
		{
			lsOffices =
				lsOffices + CommonConstant.STR_COMMA + avOffices.get(i);

		}
		return lsOffices + ") ";
	}
	/**
	 * Method to Query RTS.RTS_DSABLD_PLCRD, etc. for MVDI Export 
	 *
	 * @param  	abFull 
	 * @param	aiResComptCntyNo 
	 * @return 	String 
	 * @throws RTSException  	
	 */
	public String qryExportDisabledPlacard(
		boolean abFull,
		int aiResComptCntyNo)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryExportDisabledPlacard - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		String lsSelectPart1 =
			" SELECT D.CUSTIDNTYNO, D.CUSTID,D.CUSTIDTYPECD, "
				+ " CUSTIDTYPEDESC,D.INSTINDI,INSTNAME,DSABLDFRSTNAME, "
				+ " DSABLDLSTNAME,ST1, ST2,CITY, STATE, CNTRY, ZPCD, "
				+ " ZPCDP4, RESCOMPTCNTYNO, OFCNAME,INVITMNO, A.ACCTITMCD, "
				+ " ACCTITMCDDESC, A.RTSEFFDATE, A.RTSEXPMO, A.RTSEXPYR, "
				+ " A.DELETEINDI,A.VOIDEDINDI,A.DSABLDPLCRDIDNTYNO,B.TRANSTYPECD,"
				+ " COALESCE(H.DELREASNDESC,'  ') as DELREASNDESC, ";

		String lsSelectPart3 =
			" FROM "
				+ " RTS.RTS_DSABLD_PLCRD A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS C, "
				+ " RTS.RTS_DSABLD_PLCRD_CUST D, "
				+ " RTS.RTS_DSABLD_PLCRD_CUST_ID_TYPE E, "
				+ " RTS.RTS_ACCT_CODES F, "
				+ " RTS.RTS_OFFICE_IDS G, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " LEFT OUTER JOIN RTS.RTS_DSABLD_PLCRD_DEL_REASN H ON B.DELREASNCD = H.DELREASNCD "
				+ " WHERE "
				+ " A.DSABLDPLCRDIDNTYNO = B.DSABLDPLCRDIDNTYNO AND "
				+ " B.TRANSIDNTYNO = C.TRANSIDNTYNO AND "
				+ " C.CUSTIDNTYNO = D.CUSTIDNTYNO AND "
				+ " A.ACCTITMCD = F.ACCTITMCD AND "
				+ " D.CUSTIDTYPECD  = E.CUSTIDTYPECD AND "
				+ " D.RESCOMPTCNTYNO = G.OFCISSUANCENO AND "
				+ " C.TRANSTIMESTMP IS NOT NULL AND "
				+ " F.RTSEFFENDDATE = 99991231 AND ";

		// NEW TRANS, NOT VOIDED
		String lsTransSelectPart2 =
			" C.TRANSTIMESTMP AS CHANGETIMESTMP ";

		String lsTransPart4 =
			" C.VOIDEDTRANSINDI = 0 AND C.TRANSTIMESTMP >  "
				+ " (SELECT MAX(EXPTIMESTMP) FROM "
				+ " RTS.RTS_DSABLD_PLCRD_MVDI_EXP_HSTRY )";

		// TRANSACTIONS VOIDED SINCE LAST EXPORT
		String lsVoidSelectPart2 =
			" C.VOIDTRANSTIMESTMP AS CHANGETIMESTMP ";

		String lsVoidPart4 =
			"C.TRANSTIMESTMP <  "
				+ "(SELECT MAX(EXPTIMESTMP) FROM "
				+ " RTS.RTS_DSABLD_PLCRD_MVDI_EXP_HSTRY ) AND "
				+ " C.VOIDEDTRANSINDI = 1 AND "
				+ " C.VOIDTRANSTIMESTMP > "
				+ " (SELECT MAX(EXPTIMESTMP) FROM "
				+ " RTS.RTS_DSABLD_PLCRD_MVDI_EXP_HSTRY)";

		// CUSTOMER DATA CHANGED SINCE LAST EXPORT
		String lsCustSelectPart2 = " D.CHNGTIMESTMP AS CHANGETIMESTMP ";

		String lsCustPart4 =
			"C.TRANSTIMESTMP <  "
				+ "(SELECT MAX(EXPTIMESTMP) FROM "
				+ " RTS.RTS_DSABLD_PLCRD_MVDI_EXP_HSTRY ) AND "
				+ " C.VOIDEDTRANSINDI = 0 AND "
				+ " D.CHNGTIMESTMP > "
				+ " (SELECT MAX(EXPTIMESTMP) FROM "
				+ " RTS.RTS_DSABLD_PLCRD_MVDI_EXP_HSTRY)";

		String lsOrderBy = " ORDER BY DSABLDPLCRDIDNTYNO, 27 ";

		// If since last Export 
		if (!abFull)
		{
			lsQry.append(
				lsSelectPart1
					+ lsTransSelectPart2
					+ lsSelectPart3
					+ lsTransPart4
					+ " UNION ALL "
					+ lsSelectPart1
					+ lsVoidSelectPart2
					+ lsSelectPart3
					+ lsVoidPart4
					+ " UNION ALL "
					+ lsSelectPart1
					+ lsCustSelectPart2
					+ lsSelectPart3
					+ lsCustPart4
					+ lsOrderBy);
		}
		else
		{
			// defect 9942
			// Add ResComptCntyNo to each select 
			lsQry.append(
				lsSelectPart1
					+ lsTransSelectPart2
					+ lsSelectPart3
					+ " B.TRANSTYPECD = 0 AND A.DELETEINDI = 0 AND "
					+ " A.VOIDEDINDI = 0 AND C.VOIDEDTRANSINDI = 0 "
					+ " AND RESCOMPTCNTYNO = "
					+ aiResComptCntyNo
					+ " UNION "
					+ lsSelectPart1
					+ lsTransSelectPart2
					+ lsSelectPart3
					+ " B.TRANSTYPECD = 1 AND A.DELETEINDI = 1 AND "
					+ " A.VOIDEDINDI = 0 AND C.VOIDEDTRANSINDI = 0 "
					+ " AND RESCOMPTCNTYNO = "
					+ aiResComptCntyNo
					+ lsOrderBy);
			// end defect 9942 
		}

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryExportDisabledPlacard - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryExportDisabledPlacard - SQL - End");

			int liNumRows = 0;
			StringBuffer lsExportData = new StringBuffer();

			while (lrsQry.next())
			{
				if (liNumRows != 0)
				{
					lsExportData.append(
						CommonConstant.SYSTEM_LINE_SEPARATOR);
				}
				liNumRows++;
				DisabledPlacardExportReportData laDPExpRptData =
					new DisabledPlacardExportReportData();

				AddressData laDPAddrData = new AddressData();

				laDPExpRptData.setAddressData(laDPAddrData);

				laDPExpRptData.setCustIdntyNo(
					caDA.getIntFromDB(lrsQry, "CustIdntyNo"));

				laDPExpRptData.setCustId(
					caDA.getStringFromDB(lrsQry, "CustId"));

				laDPExpRptData.setCustIdTypeCd(
					caDA.getIntFromDB(lrsQry, "CustIdTypeCd"));

				laDPExpRptData.setCustIdTypeDesc(
					caDA.getStringFromDB(lrsQry, "CustIdTypeDesc"));

				laDPExpRptData.setInstIndi(
					caDA.getIntFromDB(lrsQry, "InstIndi"));

				laDPExpRptData.setInstName(
					caDA.getStringFromDB(lrsQry, "InstName"));

				laDPExpRptData.setDsabldFrstName(
					caDA.getStringFromDB(lrsQry, "DsabldFrstname"));

				laDPExpRptData.setDsabldLstName(
					caDA.getStringFromDB(lrsQry, "DsabldLstName"));

				laDPAddrData.setSt1(
					caDA.getStringFromDB(lrsQry, "St1"));

				laDPAddrData.setSt2(
					caDA.getStringFromDB(lrsQry, "St2"));

				laDPAddrData.setCity(
					caDA.getStringFromDB(lrsQry, "City"));

				laDPAddrData.setState(
					caDA.getStringFromDB(lrsQry, "State"));

				laDPAddrData.setCntry(
					caDA.getStringFromDB(lrsQry, "Cntry"));

				laDPAddrData.setZpcd(
					caDA.getStringFromDB(lrsQry, "ZpCd"));

				laDPAddrData.setZpcdp4(
					caDA.getStringFromDB(lrsQry, "ZpCdp4"));

				laDPExpRptData.setResComptCntyNo(
					caDA.getIntFromDB(lrsQry, "ResComptCntyNo"));

				laDPExpRptData.setOfcName(
					caDA.getStringFromDB(lrsQry, "OfcName"));

				laDPExpRptData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));

				laDPExpRptData.setAcctItmCd(
					caDA.getStringFromDB(lrsQry, "AcctItmCd"));

				laDPExpRptData.setAcctItmCdDesc(
					caDA.getStringFromDB(lrsQry, "AcctItmCdDesc"));

				laDPExpRptData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));

				laDPExpRptData.setRTSExpMo(
					caDA.getIntFromDB(lrsQry, "RTSExpMo"));

				laDPExpRptData.setRTSExpYr(
					caDA.getIntFromDB(lrsQry, "RTSExpYr"));

				laDPExpRptData.setTransTypeCd(
					caDA.getIntFromDB(lrsQry, "TransTypeCd"));

				laDPExpRptData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));

				laDPExpRptData.setVoidedIndi(
					caDA.getIntFromDB(lrsQry, "VoidedIndi"));

				laDPExpRptData.setDsabldPlcrdIdntyNo(
					caDA.getIntFromDB(lrsQry, "DsabldPlcrdIdntyNo"));

				String lsDelReasnDesc = "";

				if (laDPExpRptData.getDeleteIndi() == 1
					&& laDPExpRptData.getTransTypeCd() == 1)
				{
					lsDelReasnDesc =
						caDA.getStringFromDB(lrsQry, "DelReasnDesc");
				}

				laDPExpRptData.setDelReasnDesc(lsDelReasnDesc);
				lsExportData.append(laDPExpRptData.getExportData());
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryExportDisabledPlacard - End ");

			return lsExportData.toString();
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryExportDisabledPlacard - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryExportDisabledPlacard - RTS Exception "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query RTS.RTS_DSABLD_PLCRD
	 *
	 * @return Vector
	 * @throws RTSException  	
	 */
	public Vector qryDisabledPlacard(DisabledPlacardCustomerData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryDisabledPlacard - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;
		
		// defect 11214
		// Modify to include DelReasnCd to display on MRG023  
		lsQry.append(
			"SELECT "
				+ "DSABLDPLCRDIDNTYNO, "
				+ "ACCTITMCD, "
				+ "INVITMCD, "
				+ "INVITMNO,  "
				+ "RTSEFFDATE,  "
				+ "RTSEXPMO, "
				+ "RTSEXPYR,  "
				+ "0 as DELETEINDI,  "
				+ "-1 as DELREASNCD "
				+ " FROM RTS.RTS_DSABLD_PLCRD "
				+ " WHERE CUSTIDNTYNO = ?  AND "
				+ " COMPLETEINDI = 1 AND VOIDEDINDI = 0" 
				+ " AND DELETEINDI = 0 "
				+ " UNION "
				+ " SELECT "
				+ "A.DSABLDPLCRDIDNTYNO, "
				+ "ACCTITMCD, "
				+ "INVITMCD, "
				+ "INVITMNO,  "
				+ "RTSEFFDATE,  "
				+ "RTSEXPMO, "
				+ "RTSEXPYR,  "
				+ "1 AS DELETEINDI,  "
				+ "B.DELREASNCD "
				+ " FROM RTS.RTS_DSABLD_PLCRD A,"
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS C "
				+ " WHERE A.CUSTIDNTYNO = ?  AND "
				+ " A.COMPLETEINDI = 1 AND "
				+ " A.VOIDEDINDI = 0 AND"
				+ " A.DELETEINDI = 1 AND "
				+ " A.DSABLDPLCRDIDNTYNO =  B.DSABLDPLCRDIDNTYNO AND "
				+ " B.TRANSTYPECD = 1 AND "
				+ " C.TRANSIDNTYNO = B.TRANSIDNTYNO AND "
				+ " C.VOIDEDTRANSINDI = 0 AND " 
				+ " C.TRANSTIMESTMP IS NOT NULL"); 
		
		for (int i = 0; i<2; i++)
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaData.getCustIdntyNo())));
		}
		// end defect 11214 

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryDisabledPlacard - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryDisabledPlacard - SQL - End");

			int liNumRows = 0;

			while (lrsQry.next())
			{
				liNumRows++;
				DisabledPlacardData laDsabldPlcrdData =
					new DisabledPlacardData();

				laDsabldPlcrdData.setCustIdntyNo(
					aaData.getCustIdntyNo());

				laDsabldPlcrdData.setDsabldPlcrdIdntyNo(
					caDA.getIntFromDB(lrsQry, "DsabldPlcrdIdntyNo"));

				laDsabldPlcrdData.setAcctItmCd(
					caDA.getStringFromDB(lrsQry, "AcctItmCd"));

				laDsabldPlcrdData.setInvItmCd(
					caDA.getStringFromDB(lrsQry, "InvItmCd"));

				laDsabldPlcrdData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));

				laDsabldPlcrdData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));

				laDsabldPlcrdData.setRTSExpMo(
					caDA.getIntFromDB(lrsQry, "RTSExpMo"));

				laDsabldPlcrdData.setRTSExpYr(
					caDA.getIntFromDB(lrsQry, "RTSExpYr"));

				laDsabldPlcrdData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				
				// defect 11214 
				laDsabldPlcrdData.setDelReasnCd(
						caDA.getIntFromDB(lrsQry, "DelReasnCd"));
				// end defect 11214 

				// Add element to the Vector
				lvRslt.addElement(laDsabldPlcrdData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryDisabledPlacard - End ");

			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryDisabledPlacard - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		// defect 11214 
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryDisabledPlacard - SQL Exception "
					+ aeRTSEx.getMessage());
			throw aeRTSEx; 
		}
		// end defect 11214 
	} //END OF QUERY METHOD

	/**
	 * Method to Query RTS.RTS_DSABLD_PLCRD for Web Service 
	 *
	 * @param	asInvItmNo 
	 * @return 	Vector   
	 * @throws RTSException  	
	 */
	public Vector qryDisabledPlacardWeb(String asInvItmNo)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryDisabledPlacardWeb - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvReturn = new Vector();
		Vector lvValues = new Vector();

		ResultSet lrsQry;

		String lsSelectPart1 =
			" SELECT D.CUSTID,D.CUSTIDTYPECD, "
				+ " CUSTIDTYPEDESC,D.INSTINDI,INSTNAME,DSABLDFRSTNAME, "
				+ " DSABLDLSTNAME,ST1, ST2,CITY, STATE, CNTRY, ZPCD, "
				+ " ZPCDP4, RESCOMPTCNTYNO, OFCNAME,INVITMNO, A.ACCTITMCD, "
				+ " ACCTITMCDDESC, A.RTSEFFDATE, A.RTSEXPMO, A.RTSEXPYR, "
				+ " TRANSTYPECD,A.DELETEINDI,A.VOIDEDINDI,"
				+ " COALESCE(H.DELREASNDESC,'  ') as DELREASNDESC, ";

		String lsTransSelectPart2 =
			" C.TRANSTIMESTMP AS CHANGETIMESTMP ";

		String lsSelectPart3 =
			" FROM "
				+ " RTS.RTS_DSABLD_PLCRD A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS C, "
				+ " RTS.RTS_DSABLD_PLCRD_CUST D, "
				+ " RTS.RTS_DSABLD_PLCRD_CUST_ID_TYPE E, "
				+ " RTS.RTS_ACCT_CODES F, "
				+ " RTS.RTS_OFFICE_IDS G, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " LEFT OUTER JOIN RTS.RTS_DSABLD_PLCRD_DEL_REASN H ON B.DELREASNCD = H.DELREASNCD "
				+ " WHERE "
				+ " A.DSABLDPLCRDIDNTYNO = B.DSABLDPLCRDIDNTYNO AND "
				+ " B.TRANSIDNTYNO = C.TRANSIDNTYNO AND "
				+ " C.CUSTIDNTYNO = D.CUSTIDNTYNO AND "
				+ " A.ACCTITMCD = F.ACCTITMCD AND "
				+ " D.CUSTIDTYPECD  = E.CUSTIDTYPECD AND "
				+ " D.RESCOMPTCNTYNO = G.OFCISSUANCENO AND "
				+ " C.TRANSTIMESTMP IS NOT NULL AND "
				+ " F.RTSEFFENDDATE = 99991231 AND ";

		// defect 10607 
		String lsOrderBy = " ORDER BY 20 desc";
		// end defect 10607 

		lsQry.append(
			lsSelectPart1
				+ lsTransSelectPart2
				+ lsSelectPart3
				+ " B.TRANSTYPECD = 0 AND A.DELETEINDI = 0 AND "
				+ " A.VOIDEDINDI = 0 AND C.VOIDEDTRANSINDI = 0 "
				+ " AND INVITMNO = ? "
				+ " UNION "
				+ lsSelectPart1
				+ lsTransSelectPart2
				+ lsSelectPart3
				+ " B.TRANSTYPECD = 1 AND A.DELETEINDI = 1 AND "
				+ " A.VOIDEDINDI = 0 AND C.VOIDEDTRANSINDI = 0 "
				+ " AND INVITMNO = ? "
				+ lsOrderBy);

		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(asInvItmNo)));
				
		lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(asInvItmNo)));

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				RtsDsabldPlcrdResponse laRtsDsabldPlcrdRsp =
					new RtsDsabldPlcrdResponse();

				laRtsDsabldPlcrdRsp.setCustId(
					caDA.getStringFromDB(lrsQry, "CustId"));

				laRtsDsabldPlcrdRsp.setCustIdTypeCd(
					caDA.getIntFromDB(lrsQry, "CustIdTypeCd"));

				laRtsDsabldPlcrdRsp.setCustIdTypeDesc(
					caDA.getStringFromDB(lrsQry, "CustIdTypeDesc"));

				laRtsDsabldPlcrdRsp.setInstIndi(
					caDA.getIntFromDB(lrsQry, "InstIndi"));

				String lsInstName = 
					caDA.getStringFromDB(lrsQry, "InstName");
					
				String lsFName = caDA.getStringFromDB(lrsQry, "DsabldFrstname");
				
				String lsLstName = caDA.getStringFromDB(lrsQry, "DsabldLstName");
				
				if (laRtsDsabldPlcrdRsp.getInstIndi() != 1) 
				{
					laRtsDsabldPlcrdRsp.setFrstNameLastName(lsFName + " "+ lsLstName); 
				}
				else 
				{
					laRtsDsabldPlcrdRsp.setFrstNameLastName(lsInstName);
				}
				
				laRtsDsabldPlcrdRsp.setSt1(
					caDA.getStringFromDB(lrsQry, "St1"));

				laRtsDsabldPlcrdRsp.setSt2(
					caDA.getStringFromDB(lrsQry, "St2"));

				laRtsDsabldPlcrdRsp.setCity(
					caDA.getStringFromDB(lrsQry, "City"));

				String lsState = 
					caDA.getStringFromDB(lrsQry, "State");

				String lsCntry = caDA.getStringFromDB(lrsQry, "Cntry");
				
				if (UtilityMethods.isEmpty(lsState)) 
				{
					lsState = lsCntry; 
				}
				laRtsDsabldPlcrdRsp.setStateCntry(lsState); 

				String lsZpcd = 
					caDA.getStringFromDB(lrsQry, "ZpCd");
					
				String lsZpcdP4 = 
					caDA.getStringFromDB(lrsQry, "ZpCdp4");
					
				if (!UtilityMethods.isEmpty(lsZpcdP4))
				{
					lsZpcd = lsZpcd.trim() + lsZpcdP4.trim(); 	
				}
					
				// defect 11135
				laRtsDsabldPlcrdRsp.setZpCd(lsZpcd); 
				// end defect 11135

				laRtsDsabldPlcrdRsp.setResComptCntyNo(
					caDA.getIntFromDB(lrsQry, "ResComptCntyNo"));

				// defect 11135
				laRtsDsabldPlcrdRsp.setOfcName(
					caDA.getStringFromDB(lrsQry, "OfcName"));
				// end defect 11135

				laRtsDsabldPlcrdRsp.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));

				laRtsDsabldPlcrdRsp.setAcctItmCd(
					caDA.getStringFromDB(lrsQry, "AcctItmCd"));

				laRtsDsabldPlcrdRsp.setAcctItmCdDesc(
					caDA.getStringFromDB(lrsQry, "AcctItmCdDesc"));

				laRtsDsabldPlcrdRsp.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));

				// defect 11135
				laRtsDsabldPlcrdRsp.setRTSExpMo(
					caDA.getIntFromDB(lrsQry, "RTSExpMo"));

				laRtsDsabldPlcrdRsp.setRTSExpYr(
					caDA.getIntFromDB(lrsQry, "RTSExpYr"));
				// end defect 11135

				int liTransTypeCd = 
					caDA.getIntFromDB(lrsQry, "TransTypeCd");

				laRtsDsabldPlcrdRsp.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));

				String lsDelReasnDesc = "";

				if (laRtsDsabldPlcrdRsp.getDeleteIndi() == 1
					&& liTransTypeCd == 1)
				{
					lsDelReasnDesc =
						caDA.getStringFromDB(lrsQry, "DelReasnDesc");
				}

				laRtsDsabldPlcrdRsp.setDelReasnDesc(lsDelReasnDesc);
				
				lvReturn.add(laRtsDsabldPlcrdRsp);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + "  - End ");

			return lvReturn;
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
				csMethod + " - RTS Exception " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query RTS.RTS_DSABLD_PLCRD, etc. for Disabled 
	 * Placard Reporting 
	 *
	 * @return Vector
	 * @throws RTSException  	
	 */
	public Vector qryReportDisabledPlacard(DisabledPlacardUIData aaDPUIData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryReportDisabledPlacard - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvResult = new Vector();

		ResultSet lrsQry;
		RTSDate laBeginDate = aaDPUIData.getBeginDate();
		RTSDate laEndDate = aaDPUIData.getEndDate();
		String lsTransCd = "";
		// defect 10133 
		// String lsAdd = "'BPM', 'BTM', 'RPNM', 'RTNM',";
		// String lsRpl = "'RPBPM', 'RPBTM', 'RPRPNM', 'RPRTNM',";
		// String lsDel = "'DLBPM', 'DLBTM', 'DLRPNM', 'DLRTNM',";
		String lsAdd = "'BPM', 'BTM', 'RPNM', 'RTNM','TDC','PDC',";
		String lsRpl =
			"'RPBPM', 'RPBTM', 'RPRPNM', 'RPRTNM','RPLPDC','RPLTDC',";
		String lsDel =
			"'DLBPM', 'DLBTM', 'DLRPNM', 'DLRTNM','DELPDC','DELTDC',";
		// end defect 10133 
		
		// defect 11214 
		String lsRen = "'RENPDC',"; 
		String lsRei = "'REIPDC','REITDC',";
		
		lsTransCd =
			aaDPUIData.isAddTrans() ? lsTransCd + lsAdd : lsTransCd;
		lsTransCd =
			aaDPUIData.isRplTrans() ? lsTransCd + lsRpl : lsTransCd;
		lsTransCd =
			aaDPUIData.isDelTrans() ? lsTransCd + lsDel : lsTransCd;
		lsTransCd =
			aaDPUIData.isRenTrans() ? lsTransCd + lsRen : lsTransCd;
		lsTransCd =
			aaDPUIData.isReiTrans() ? lsTransCd + lsRei : lsTransCd;
		// end defect 11214 

		lsTransCd =
			" TRANSCD IN ("
				+ lsTransCd.substring(0, lsTransCd.length() - 1)
				+ ") AND ";

		// defect 11279 
		// Add DelReasnDesc & Left Outer Join to RTS.RTS_DSABLD_PLCRD_DEL_REASN
		lsQry.append(
			" SELECT C.TRANSCD,D.CUSTIDNTYNO, D.CUSTID,D.CUSTIDTYPECD, "
				+ " D.INSTINDI,INSTNAME,DSABLDFRSTNAME, "
				+ " DSABLDLSTNAME,ST1, ST2,CITY, STATE, CNTRY, ZPCD, "
				+ " ZPCDP4, RESCOMPTCNTYNO, INVITMNO, A.ACCTITMCD, "
				+ " ACCTITMCDDESC, A.RTSEFFDATE, A.RTSEXPMO, A.RTSEXPYR, "
				+ " C.TRANSWSID, C.TRANSAMDATE, C.TRANSTIME, C.TRANSEMPID, "
				+ " B.TRANSTYPECD, A.DELETEINDI,C.VOIDEDTRANSINDI,A.DSABLDPLCRDIDNTYNO, "
				+ " C.OFCISSUANCENO, C.TRANSIDNTYNO, H.DELREASNDESC FROM "
				+ " RTS.RTS_DSABLD_PLCRD A, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS C, "
				+ " RTS.RTS_DSABLD_PLCRD_CUST D, "
				+ " RTS.RTS_ACCT_CODES E, "
				+ " RTS.RTS_DSABLD_PLCRD_TRANS_TO_PLCRD B "
				+ " LEFT OUTER JOIN RTS.RTS_DSABLD_PLCRD_DEL_REASN H " 
				+ " ON B.DELREASNCD = H.DELREASNCD "
				+ " WHERE "
				+ " A.DSABLDPLCRDIDNTYNO = B.DSABLDPLCRDIDNTYNO AND "
				+ " B.TRANSIDNTYNO = C.TRANSIDNTYNO AND "
				+ " C.CUSTIDNTYNO = D.CUSTIDNTYNO AND "
				+ " A.ACCTITMCD = E.ACCTITMCD AND "
				+ " E.RTSEFFENDDATE = 99991231 AND "
				+ " C.TRANSTIMESTMP IS NOT NULL AND "
				+ lsTransCd
				+ " C.OFCISSUANCENO IN "
				+ getOffices(aaDPUIData.getSelectedOffices())
				+ " and TransAMDate between "
				+ laBeginDate.getAMDate()
				+ " and "
				+ laEndDate.getAMDate()
				+ " ORDER BY OFCISSUANCENO, TRANSAMDATE,TRANSWSID,TRANSTIME, "
				+ " TRANSIDNTYNO,TRANSTYPECD,INVITMNO ");
			// end defect 11279 

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryReportDisabledPlacard - SQL - Begin");
			lrsQry =
				caDA.executeDBQuery(lsQry.toString(), new Vector());
			Log.write(
				Log.SQL,
				this,
				" - qryReportDisabledPlacard - SQL - End");

			int liNumRows = 0;

			while (lrsQry.next())
			{
				liNumRows++;
				DisabledPlacardExportReportData laDPExpRptData =
					new DisabledPlacardExportReportData();

				AddressData laDPAddrData = new AddressData();

				laDPExpRptData.setAddressData(laDPAddrData);

				laDPExpRptData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));

				laDPExpRptData.setTransIdntyNo(
					caDA.getIntFromDB(lrsQry, "TransIdntyNo"));

				laDPExpRptData.setTransTypeCd(
					caDA.getIntFromDB(lrsQry, "TransTypeCd"));

				laDPExpRptData.setInstIndi(
					caDA.getIntFromDB(lrsQry, "InstIndi"));

				laDPExpRptData.setInstName(
					caDA.getStringFromDB(lrsQry, "InstName"));

				laDPExpRptData.setDsabldFrstName(
					caDA.getStringFromDB(lrsQry, "DsabldFrstname"));

				laDPExpRptData.setDsabldLstName(
					caDA.getStringFromDB(lrsQry, "DsabldLstName"));

				laDPAddrData.setSt1(
					caDA.getStringFromDB(lrsQry, "St1"));

				laDPAddrData.setSt2(
					caDA.getStringFromDB(lrsQry, "St2"));

				laDPAddrData.setCity(
					caDA.getStringFromDB(lrsQry, "City"));

				laDPAddrData.setState(
					caDA.getStringFromDB(lrsQry, "State"));

				laDPAddrData.setCntry(
					caDA.getStringFromDB(lrsQry, "Cntry"));

				laDPAddrData.setZpcd(
					caDA.getStringFromDB(lrsQry, "ZpCd"));

				laDPAddrData.setZpcdp4(
					caDA.getStringFromDB(lrsQry, "ZpCdp4"));

				laDPExpRptData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));

				int liTransWsId =
					caDA.getIntFromDB(lrsQry, "TransWsId");

				int liTransAMDate =
					caDA.getIntFromDB(lrsQry, "TransAMDate");

				int liTransTime =
					caDA.getIntFromDB(lrsQry, "TransTime");

				laDPExpRptData.setEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));

				laDPExpRptData.setTransId(
					UtilityMethods.getTransId(
						laDPExpRptData.getOfcIssuanceNo(),
						liTransWsId,
						liTransAMDate,
						liTransTime));

				laDPExpRptData.setResComptCntyNo(
					caDA.getIntFromDB(lrsQry, "ResComptCntyNo"));

				laDPExpRptData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));

				laDPExpRptData.setAcctItmCdDesc(
					caDA.getStringFromDB(lrsQry, "AcctItmCdDesc"));

				laDPExpRptData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));

				laDPExpRptData.setRTSExpMo(
					caDA.getIntFromDB(lrsQry, "RTSExpMo"));

				laDPExpRptData.setRTSExpYr(
					caDA.getIntFromDB(lrsQry, "RTSExpYr"));

				laDPExpRptData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));

				laDPExpRptData.setVoidedIndi(
					caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));
				
				// defect 11279 
				laDPExpRptData.setDelReasnDesc(
						caDA.getStringFromDB(lrsQry, "DelReasnDesc"));
				// end defect 11279 

				lvResult.add(laDPExpRptData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryReportDisabledPlacard - End ");

			return lvResult;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryReportDisabledPlacard - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryReportDisabledPlacard - RTS Exception "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD
}
