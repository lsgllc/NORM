package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.DisabledPlacardCustomerData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.SystemControlBatchConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * DisabledPlacardCustomer.java

 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	11/08/2008	Terminate retrieval after Max Records 
 * 							selected. 
 * 							modify qryDisabledPlacardcustomer()
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	11/09/2008 	Add method to facilitate DataLoad 
 * 							add insDisabledPlacardCustomer(DisabledPlacardCustomerData,boolean)
 * 							modify insDisabledPlacardCustomer(DisabledPlacardCustomerData)
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/12/2008	Correct typos
 * 							modify resetAllInProcsDisabledPlacardCustomer()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/17/2008	Not resetting Existing Customer 
 * 							modify resetInProcsDisabledPlacardCustomer()
 * 							defect 9871 Ver Defect_POS_B
 * K Harrell	01/21/2009	For Purge: 
 * 							 Set DeleteIndi when no valid placards exist:
 * 								30 days; Delete when no placard exist: 
 * 							   180 days. 
 * 							add DELETEDINDI 
 * 							add purgeSetDelIndiDisabledPlacardCustomer,
 * 							  purgeDisabledPlacardCustomer(), 
 * 							  constructNumRows()
 * 							modify qryDisabledPlacardCustomer()  
 * 							defect 9825 Ver Defect_POS_D
 * K Harrell	07/25/2009	add MOBILITY_NOT_APPLICABLE
 * 							modify qryDisabledPlacardCustomer(), 
 * 							 insDisabledPlacardCustomer(
 *							   DisabledPlacardCustomerData,boolean)
 *							 updDisabledPlacardCustomer(), 
 *							 insDisabledPlacardCustomerHistory()
 *							defect 10133 Ver Defect_POS_F
 * K Harrell	03/12/2010	Delete method used only for Stress Test 
 * 							delete insDisabledPlacardCustomer
 * 							 (DisabledPlacardCustomerData,boolean)
 * 							modify insDisabledPlacardCustomer
 * 							(DisabledPlacardCustomerData)
 * 							defect 10210 Ver POS_640
 * K Harrell	08/10/2011  add csMethod 
 * 							add qryDisabledPlacardInProcs() 
 * 			   				defect 10973 Ver 6.8.1 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_DSABLD_PLCRD_CUST,
 * RTS_DSABLD_PLCRD_CUST_HSTRY, RTS_DSABLD_PLCRD_CUST_IN_PROCS
 * 
 * @version 6.8.1 	08/10/2011
 * @author Kathy Harrell 
 * @since			10/27/2008
 */
public class DisabledPlacardCustomer extends
		DisabledPlacardCustomerData
{
	DatabaseAccess caDA;

	// defect 10133
	// Disabled Placard Mobility Not Applicable
	private static final int MOBILITY_NOT_APPLICABLE = 9;

	// end defect 10133

	// defect 9889
	private static final int DELETEDINDI = 1;

	// end defect 9889

	// defect 10973
	private String csMethod = new String();

	// end defect 10973

	/**
	 * DisabledPlacardCustomer constructor comment.
	 * 
	 * @param aaDA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	public DisabledPlacardCustomer(DatabaseAccess aaDA)
			throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_DSABLD_PLCRD_CUST
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryDisabledPlacardCustomer(
			DisabledPlacardCustomerData aaData) throws RTSException
	{
		Log.write(Log.METHOD, this,
				" - qryDisabledPlacardCustomer - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// defect 10133
		// Remove MOBLTYDSABLDINDI
		lsQry.append("SELECT " + "A.CUSTIDNTYNO, " + "CUSTID, "
				+ "CUSTIDTYPECD, " + "INSTINDI, " + "INSTNAME, "
				+ "DSABLDFRSTNAME, " + "DSABLDMI, " + "DSABLDLSTNAME, "
				+ "ST1, " + "ST2, " + "CITY, " + "RESCOMPTCNTYNO, "
				+ "STATE, " + "CNTRY, " + "ZPCD, " + "ZPCDP4, "
				+ "EMAIL, "
				+ "PHONE, "
				+ "DSABLDVETPLTINDI, "
				+ "PERMDSABLDINDI, "
				// + "MOBLTYDSABLDINDI,"
				+ "DSABLDPLTINDI, " + "CREATETIMESTMP, "
				+ "A.DELETEINDI, " + "CHNGTIMESTMP,  "
				+ "INPROCSINDI,  " + "INPROCSIDNTYNO  "
				+ " FROM RTS.RTS_DSABLD_PLCRD_CUST A");
		// end defect 10133

		String lsWhere = "";

		if (aaData.getSearchType() == MiscellaneousRegConstant.CUST_ID)
		{
			lsWhere = " Where CustId = ? ";

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaData.getCustId())));

		}
		else if (aaData.getSearchType() == MiscellaneousRegConstant.DISABLED_NAME)
		{
			lsWhere = " Where ( ";
			String lsFrstName = aaData.getDsabldFrstName();
			String lsMI = aaData.getDsabldMI();
			String lsLstName = aaData.getDsabldLstName();
			boolean lbEmpty = true;

			if (lsFrstName != null && lsFrstName.trim().length() != 0)
			{
				lsWhere = lsWhere + " DsabldFrstName like '"
						+ UtilityMethods.quote(lsFrstName.trim())
						+ "%'";
				lbEmpty = false;
			}
			if (lsMI != null && lsMI.trim().length() != 0)
			{
				lsWhere = lbEmpty ? lsWhere : lsWhere + " and ";
				lsWhere = lsWhere + " DsabldMI = ? ";
				lbEmpty = false;

				lvValues.addElement(new DBValue(Types.CHAR,
						DatabaseAccess.convertToString(aaData
								.getDsabldMI())));
			}
			if (lsLstName != null && lsLstName.trim().length() != 0)
			{
				lsWhere = lbEmpty ? lsWhere : lsWhere + " and ";
				lsWhere = lsWhere + " DsabldLstName like '"
						+ UtilityMethods.quote(lsLstName.trim()) + "%'";
			}
			lsWhere = lsWhere + ")";
		}
		else if (aaData.getSearchType() == MiscellaneousRegConstant.INSTITUTION_NAME)
		{
			lsWhere = " Where InstName like '%"
					+ UtilityMethods.quote(aaData.getInstName().trim())
					+ "%'";
		}
		else if (aaData.getSearchType() == MiscellaneousRegConstant.PLACARD_NUMBER)
		{
			lsWhere = " Where EXISTS (SELECT INVITMNO FROM RTS.RTS_DSABLD_PLCRD B "
					+ " WHERE B.INVITMNO = ? AND "
					+ " B.COMPLETEINDI = 1 AND B.VOIDEDINDI = 0 AND "
					+ " A.CUSTIDNTYNO = B.CUSTIDNTYNO) ";

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaData.getPlcrdNo())));
		}

		// defect 9889
		lsQry.append(lsWhere + " AND DELETEINDI = 0 ");
		// end defect 9889

		try
		{
			Log.write(Log.SQL, this,
					" - qryDisabledPlacardCustomer - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this,
					" - qryDisabledPlacardCustomer - SQL - End");
			int liNumRows = 0;
			boolean lbMaxRowCountExceeded = false;

			while (lrsQry.next())
			{
				liNumRows++;
				if (liNumRows > SystemProperty.getDsabldPlcrdMaxRcds())
				{
					lbMaxRowCountExceeded = true;
					break;
				}
				DisabledPlacardCustomerData laDPCustData = new DisabledPlacardCustomerData();
				laDPCustData.setAddressData(new AddressData());
				laDPCustData.setCustIdntyNo(caDA.getIntFromDB(lrsQry,
						"CustIdntyNo"));

				laDPCustData.setCustId(caDA.getStringFromDB(lrsQry,
						"CustId"));

				laDPCustData.setCustIdTypeCd(caDA.getIntFromDB(lrsQry,
						"CustIdTypeCd"));

				laDPCustData.setInstIndi(caDA.getIntFromDB(lrsQry,
						"InstIndi"));

				laDPCustData.setInstName(caDA.getStringFromDB(lrsQry,
						"InstName"));

				laDPCustData.setDsabldFrstName(caDA.getStringFromDB(
						lrsQry, "DsabldFrstName"));
				laDPCustData.setDsabldMI(caDA.getStringFromDB(lrsQry,
						"DsabldMI"));
				laDPCustData.setDsabldLstName(caDA.getStringFromDB(
						lrsQry, "DsabldLstName"));

				laDPCustData.getAddressData().setSt1(
						caDA.getStringFromDB(lrsQry, "St1"));
				laDPCustData.getAddressData().setSt2(
						caDA.getStringFromDB(lrsQry, "St2"));
				laDPCustData.getAddressData().setCity(
						caDA.getStringFromDB(lrsQry, "City"));
				laDPCustData.setResComptCntyNo(caDA.getIntFromDB(
						lrsQry, "ResComptCntyNo"));
				laDPCustData.getAddressData().setState(
						caDA.getStringFromDB(lrsQry, "State"));
				laDPCustData.getAddressData().setCntry(
						caDA.getStringFromDB(lrsQry, "Cntry"));
				laDPCustData.getAddressData().setZpcd(
						caDA.getStringFromDB(lrsQry, "ZpCd"));
				laDPCustData.getAddressData().setZpcdp4(
						caDA.getStringFromDB(lrsQry, "ZpCdP4"));
				laDPCustData.setEMail(caDA.getStringFromDB(lrsQry,
						"EMail"));
				laDPCustData.setPhone(caDA.getStringFromDB(lrsQry,
						"Phone"));
				laDPCustData.setDsabldVetPltIndi(caDA.getIntFromDB(
						lrsQry, "DsabldVetPltIndi"));
				laDPCustData.setPermDsabldIndi(caDA.getIntFromDB(
						lrsQry, "PermDsabldIndi"));
				// defect 10133
				// laDPCustData.setMobltyDsabldIndi(
				// caDA.getIntFromDB(lrsQry, "MobltyDsabldIndi"));
				// end defect 10133
				laDPCustData.setDsabldPltIndi(caDA.getIntFromDB(lrsQry,
						"DsabldPltIndi"));
				laDPCustData.setCreateTimestmp(caDA.getRTSDateFromDB(
						lrsQry, "CreateTimestmp"));
				laDPCustData.setChngTimestmp(caDA.getRTSDateFromDB(
						lrsQry, "ChngTimestmp"));
				laDPCustData.setDeleteIndi(caDA.getIntFromDB(lrsQry,
						"DeleteIndi"));
				laDPCustData.setInProcsIdntyNo(caDA.getIntFromDB(
						lrsQry, "InProcsIdntyNo"));
				laDPCustData.setInProcsIndi(caDA.getIntFromDB(lrsQry,
						"InProcsIndi"));
				laDPCustData.setSearchType(aaData.getSearchType());
				// Add element to the Vector
				lvRslt.addElement(laDPCustData);
			} // End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this,
					" - qryDisabledPlacardCustomer - End ");

			if (liNumRows == 0
					&& aaData.getSearchType() == MiscellaneousRegConstant.CUST_ID)
			{
				aaData.setNoRecordFound(true);
				lvRslt.add(aaData);
			}
			Vector lvVector = new Vector();
			lvVector.addElement(new Boolean(lbMaxRowCountExceeded));
			lvVector.addElement(lvRslt);
			return (lvVector);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - qryDisabledPlacardCustomer - SQL Exception "
							+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"qryDisabledPlacardCustomer - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} // END OF QUERY METHOD

	/**
	 * Method to Insert into RTS.RTS_DSABLD_PLCRD_CUST
	 * 
	 * @param aaDPCustData
	 * @return DisabledPlacardCustomerData
	 * @throws RTSException
	 */
	public DisabledPlacardCustomerData insDisabledPlacardCustomer(
			DisabledPlacardCustomerData aaDPCustData)
			throws RTSException
	{
		// defect 10210
		// return insDisabledPlacardCustomer(aaDPCustData, false);

		Log.write(Log.METHOD, this,
				"insDisabledPlacardCustomer - Begin");

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		String lsIns = "INSERT INTO RTS.RTS_DSABLD_PLCRD_CUST( "
				+ "CUSTID, " + "CUSTIDTYPECD," + "INSTINDI,"
				+ "INSTNAME, " + "DSABLDFRSTNAME, " + "DSABLDMI, "
				+ "DSABLDLSTNAME, " + "ST1, " + "ST2, " + "CITY, "
				+ "RESCOMPTCNTYNO, " + "STATE, " + "CNTRY, " + "ZPCD, "
				+ "ZPCDP4, " + "EMAIL, " + "PHONE, "
				+ "DSABLDVETPLTINDI, " + "PERMDSABLDINDI, "
				+ "MOBLTYDSABLDINDI," + "DSABLDPLTINDI, "
				+ "CREATETIMESTMP, " + "DELETEINDI, "
				+ "CHNGTIMESTMP ) " + " VALUES (" + " ?," + " ?,"
				+ " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?,"
				+ " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?,"
				+ " ?," + " ?," + " ?," + " ?," + " ?,"
				+ " Current Timestamp," + " 0," + " Current Timestamp)";
		try
		{
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getCustId())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getCustIdTypeCd())));

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getInstIndi())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getInstName())));
			lvValues
					.addElement(new DBValue(Types.CHAR, DatabaseAccess
							.convertToString(aaDPCustData
									.getDsabldFrstName())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getDsabldMI())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getDsabldLstName())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getSt1())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getSt2())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getCity())));

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getResComptCntyNo())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getState())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getCntry())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getZpcd())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getZpcdp4())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getEMail())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getPhone())));

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getDsabldVetPltIndi())));
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getPermDsabldIndi())));

			// defect 10133
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(MOBILITY_NOT_APPLICABLE)));
			// end defect 10133

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getDsabldPltIndi())));

			Log.write(Log.SQL, this,
					"insDisabledPlacardCustomer - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			String lsSel = "Select IDENTITY_VAL_LOCAL() as CustIdntyNo  from "
					+ " RTS.RTS_DSABLD_PLCRD_CUST";

			lrsQry = caDA.executeDBQuery(lsSel, null);
			int liIdntyNo = 0;

			while (lrsQry.next())
			{
				liIdntyNo = caDA.getIntFromDB(lrsQry, "CustIdntyNo");
				aaDPCustData.setCustIdntyNo(liIdntyNo);
				break;
			} // End of While

			aaDPCustData.setNoRecordFound(false);

			// Also insert into History table
			insDisabledPlacardCustomerHistory(aaDPCustData);
			setInProcsDisabledPlacardCustomer(aaDPCustData);

			Log.write(Log.SQL, this,
					"insDisabledPlacardCustomer - SQL - End");
			Log.write(Log.METHOD, this,
					"insDisabledPlacardCustomer - End");
			return aaDPCustData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - insDisabledPlacardCustomer - SQL Exception "
							+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"insDisabledPlacardCustomer - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		// end defect 10210
	} // END OF INSERT METHOD

	/**
	 * Query if same workstation should continue
	 * 
	 * @param aaData
	 * @return boolean
	 * @throws RTSException
	 */
	public boolean qryDisabldPlacardInProcs(
			DisabledPlacardCustomerData aaData) throws RTSException
	{
		boolean lbInProcs = true;
		csMethod = "qryDisabldPlacardInProcs";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		int liOfcIssuanceNo = -1;
		int liWsId = -1;
		StringBuffer lsQry1 = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry1.append("SELECT OFCISSUANCENO,WSID FROM "
				+ " RTS.RTS_DSABLD_PLCRD_CUST_IN_PROCS WHERE "
				+ " INPROCSIDNTYNO = ? ");

		lvValues.addElement(new DBValue(Types.INTEGER, DatabaseAccess
				.convertToString(aaData.getInProcsIdntyNo())));

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry1.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			int liNumRows = 0;

			while (lrsQry.next())
			{
				liNumRows++;

				liOfcIssuanceNo = caDA.getIntFromDB(lrsQry,
						"OfcIssuanceNo");

				liWsId = (caDA.getIntFromDB(lrsQry, "WsId"));
				break;

			} // End of While

			if (liNumRows == 1
					&& liOfcIssuanceNo == aaData.getOfcIssuanceNo()
					&& liWsId == aaData.getWsId())
			{
				int liTransAMDate = new RTSDate().getAMDate(); 
				
				StringBuffer lsQry2 = new StringBuffer();
				lsQry2.append("SELECT COUNT(*) as COUNTTRANS FROM "
						+ "RTS.RTS_DSABLD_PLCRD_TRANS "
						+ "WHERE CUSTIDNTYNO = ? AND "
						+ "OFCISSUANCENO = ? AND "
						+ "TRANSWSID = ? AND "
						+ "TRANSAMDATE = ? AND "
						+ "TRANSTIMESTMP IS NULL ");

				lvValues = new Vector();

				lvValues.addElement(new DBValue(Types.INTEGER,
						DatabaseAccess.convertToString(aaData
								.getCustIdntyNo())));
				lvValues.addElement(new DBValue(Types.INTEGER,
						DatabaseAccess.convertToString(aaData
								.getOfcIssuanceNo())));
				lvValues.addElement(new DBValue(Types.INTEGER,
						DatabaseAccess
								.convertToString(aaData.getWsId())));
				lvValues.addElement(new DBValue(Types.INTEGER,
						DatabaseAccess
								.convertToString(liTransAMDate)));
				
				Log.write(Log.SQL, this, csMethod
						+ " (Trans) - SQL - Begin");
				lrsQry = caDA.executeDBQuery(lsQry2.toString(),
						lvValues);
				Log.write(Log.SQL, this, csMethod
						+ "(Trans) - SQL - End");

				int liTrans = 0;
				while (lrsQry.next())
				{
					liTrans = caDA.getIntFromDB(lrsQry, "COUNTTRANS");
					break;

				} // End of While
				lbInProcs = liTrans != 0;
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod = " - End ");

			return lbInProcs;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, csMethod
					+ " - SQL Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this, csMethod + " - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	// /**
	// *
	// * Method to Insert into RTS.RTS_DSABLD_PLCRD_CUST
	// *
	// * @param aaDPCustData
	// * @param abDataLoad
	// * @return DisabledPlacardCustomerData
	// * @throws RTSException
	// */
	// public DisabledPlacardCustomerData insDisabledPlacardCustomer
	// (DisabledPlacardCustomerData aaDPCustData, boolean abDataLoad)
	// throws RTSException
	// {
	// ...
	// }

	/**
	 * Method to set RTS_DSABLD_PLCRD_CUST record in processs
	 * 
	 * @param aaDPCust
	 * @throws RTSException
	 */
	public void setInProcsDisabledPlacardCustomer(
			DisabledPlacardCustomerData aaDPCustData)
			throws RTSException
	{
		ResultSet lrsQry;

		Log.write(Log.METHOD, this,
				"setInProcsDisabledPlacardCustomer - Begin");

		Vector lvValues1 = new Vector();
		Vector lvValues2 = new Vector();

		String lsIns = "INSERT INTO RTS.RTS_DSABLD_PLCRD_CUST_IN_PROCS"
				+ "(CUSTIDNTYNO,OFCISSUANCENO,WSID,EMPID,INPROCSTIMESTMP) VALUES( "
				+ " ?, " + " ?, " + " ?, " + " ?, "
				+ " Current Timestamp )";

		lvValues1.addElement(new DBValue(Types.INTEGER, DatabaseAccess
				.convertToString(aaDPCustData.getCustIdntyNo())));
		lvValues1.addElement(new DBValue(Types.INTEGER, DatabaseAccess
				.convertToString(aaDPCustData.getOfcIssuanceNo())));
		lvValues1.addElement(new DBValue(Types.INTEGER, DatabaseAccess
				.convertToString(aaDPCustData.getWsId())));
		lvValues1.addElement(new DBValue(Types.CHAR, DatabaseAccess
				.convertToString(aaDPCustData.getEmpId())));

		String lsUpd = "UPDATE RTS.RTS_DSABLD_PLCRD_CUST "
				+ "SET INPROCSINDI = 1, INPROCSIDNTYNO =  ? "
				+ " WHERE CUSTIDNTYNO = ? ";

		try
		{
			Log.write(Log.SQL, this,
					"setInProcsDisabledPlacardCustomer - SQL - Begin");

			int liNumRows1 = caDA.executeDBInsertUpdateDelete(lsIns,
					lvValues1);

			String lsSel = "Select IDENTITY_VAL_LOCAL() as InProcsIdntyNo  from "
					+ " RTS.RTS_DSABLD_PLCRD_CUST_IN_PROCS";

			lrsQry = caDA.executeDBQuery(lsSel, null);
			int liIdntyNo = 0;

			while (lrsQry.next())
			{
				liIdntyNo = caDA.getIntFromDB(lrsQry, "InProcsIdntyNo");

				aaDPCustData.setInProcsIdntyNo(liIdntyNo);
				break;
			} // End of While

			lvValues2.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getInProcsIdntyNo())));

			lvValues2.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getCustIdntyNo())));

			int liNumRows2 = caDA.executeDBInsertUpdateDelete(lsUpd,
					lvValues2);

			Log.write(Log.SQL, this,
					"setInProcsDisabledPlacardCustomer - SQL - End");

			Log.write(Log.METHOD, this,
					"setInProcsDisabledPlacardCustomer - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"setInProcsDisabledPlacardCustomer - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - setInProcsDisabledPlacardCustomer - SQL Exception "
							+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}

	}

	/**
	 * Method to reset RTS_DSABLD_PLCRD_CUST record in processs
	 * 
	 * @param aaDPCust
	 * @throws RTSException
	 */
	public void resetInProcsDisabledPlacardCustomer(
			DisabledPlacardCustomerData aaDPCustData)
			throws RTSException
	{
		Log.write(Log.METHOD, this,
				" - resetInProcsDisabledPlacardCustomer - Begin");

		StringBuffer lsQry = new StringBuffer();

		DisabledPlacardCustomerData laDPCustData = new DisabledPlacardCustomerData();
		Vector lvValues1 = new Vector();
		Vector lvValues2 = new Vector();

		ResultSet lrsQry;

		lsQry.append("SELECT " + "INPROCSINDI," + "INPROCSIDNTYNO  "
				+ " FROM RTS.RTS_DSABLD_PLCRD_CUST "
				+ " WHERE CUSTIDNTYNO = ?  ");

		lvValues2.addElement(new DBValue(Types.INTEGER, DatabaseAccess
				.convertToString(aaDPCustData.getCustIdntyNo())));

		try
		{
			Log
					.write(Log.SQL, this,
							" - resetInProcsDisabledPlacardCustomer - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues2);
			Log
					.write(Log.SQL, this,
							" - resetInProcsDisabledPlacardCustomer - SQL - End");

			while (lrsQry.next())
			{
				laDPCustData = new DisabledPlacardCustomerData();
				laDPCustData.setCustIdntyNo(aaDPCustData
						.getCustIdntyNo());
				laDPCustData.setInProcsIdntyNo(caDA.getIntFromDB(
						lrsQry, "InProcsIdntyNo"));
				laDPCustData.setInProcsIndi(caDA.getIntFromDB(lrsQry,
						"InProcsIndi"));
				laDPCustData.setCustIdntyNo(aaDPCustData
						.getCustIdntyNo());
				break;
			} // End of While

			if (laDPCustData.getInProcsIdntyNo() != 0)
			{
				String lsUpd1 = "Update RTS.RTS_DSABLD_PLCRD_CUST_IN_PROCS "
						+ " SET COMPLTIMESTMP = Current Timestamp where "
						+ " InProcsIdntyNo = ? ";

				// defect 9871
				lvValues1.addElement(new DBValue(Types.INTEGER,
						DatabaseAccess.convertToString(laDPCustData
								.getInProcsIdntyNo())));
				// end defect 9871

				String lsUpd2 = "Update RTS.RTS_DSABLD_PLCRD_CUST "
						+ " SET InprocsIndi = 0, InProcsIdntyNo = 0 "
						+ " where CustIdntyNo =  ? ";

				int liNumRows1 = caDA.executeDBInsertUpdateDelete(
						lsUpd1, lvValues1);

				int liNumRows2 = caDA.executeDBInsertUpdateDelete(
						lsUpd2, lvValues2);
			}
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this,
					" - resetInProcsDisabledPlacardCustomer - End ");

		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - resetInProcsDisabledPlacardCustomer - SQL Exception "
							+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"resetInProcsDisabledPlacardCustomer - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to reset RTS_DSABLD_PLCRD_CUST record in processs
	 * 
	 * @param aaDPCust
	 * @throws RTSException
	 */
	public int resetAllInProcsDisabledPlacardCustomer()
			throws RTSException
	{
		Log.write(Log.METHOD, this,
				" - resetAllInProcsDisabledPlacardCustomer - Begin");

		String lsUpd1 = "Update RTS.RTS_DSABLD_PLCRD_CUST_IN_PROCS "
				+ " SET COMPLTIMESTMP = CURRENT TIMESTAMP WHERE COMPLTIMESTMP "
				+ " IS NULL";

		String lsUpd2 = "Update RTS.RTS_DSABLD_PLCRD_CUST "
				+ " SET INPROCSINDI = 0 , INPROCSIDNTYNO = 0 WHERE "
				+ " INPROCSINDI = 1";

		try
		{
			Log
					.write(Log.SQL, this,
							" - resetAllInProcsDisabledPlacardCustomer - SQL - Begin");

			int liNumRows1 = caDA.executeDBInsertUpdateDelete(lsUpd1,
					new Vector());

			int liNumRows2 = caDA.executeDBInsertUpdateDelete(lsUpd2,
					new Vector());

			Log
					.write(Log.SQL, this,
							" - resetInProcsDisabledPlacardCustomer - SQL - End");

			caDA.closeLastDBStatement();

			Log.write(Log.METHOD, this,
					" - resetAllInProcsDisabledPlacardCustomer - End ");
			return liNumRows2;

		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"resetAllInProcsDisabledPlacardCustomer - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to set DeleteIndi in RTS.RTS_DSABLD_PLCRD_CUST where no valid
	 * placards exist.
	 * 
	 * @param aaPurgeDate
	 * @return int
	 * @throws RTSException
	 */
	public int purgeSetDelIndiDisabledPlacardCustomer(
			RTSDate aaPurgeDate) throws RTSException
	{
		Log.write(Log.METHOD, this,
				"purgeSetDelIndiDisabledPlacardCustomer- Begin");

		String lsWhere = " WHERE ChngTimestmp <= '"
				+ aaPurgeDate.getTimestamp()
				+ "' and not exists (SELECT * FROM RTS.RTS_DSABLD_PLCRD B"
				+ " WHERE A.CustIdntyNo = B.CustIdntyNo and VOIDEDINDI = 0 )";

		// Log change in History Table
		String lsIns = "INSERT INTO RTS.RTS_DSABLD_PLCRD_CUST_HSTRY ("
				+ "CUSTIDNTYNO, " + "CUSTID, " + "CUSTIDTYPECD, "
				+ "INSTINDI, " + "INSTNAME, " + "DSABLDFRSTNAME, "
				+ "DSABLDMI, " + "DSABLDLSTNAME, " + "ST1, " + "ST2, "
				+ "CITY, " + "RESCOMPTCNTYNO, " + "STATE, " + "CNTRY, "
				+ "ZPCD, " + "ZPCDP4, " + "EMAIL, " + "PHONE, "
				+ "DSABLDVETPLTINDI, " + "PERMDSABLDINDI, "
				+ "MOBLTYDSABLDINDI," + "DSABLDPLTINDI, "
				+ "DELETEINDI, " + "CHNGTIMESTMP,  "
				+ "OFCISSUANCENO, " + "WSID, " + "EMPID )"
				+ "( SELECT " + " CUSTIDNTYNO, " + "CUSTID, "
				+ "CUSTIDTYPECD, " + "INSTINDI, " + "INSTNAME, "
				+ "DSABLDFRSTNAME, " + "DSABLDMI, " + "DSABLDLSTNAME, "
				+ "ST1, " + "ST2, " + "CITY, " + "RESCOMPTCNTYNO, "
				+ "STATE, " + "CNTRY, " + "ZPCD, " + "ZPCDP4, "
				+ "EMAIL, " + "PHONE, " + "DSABLDVETPLTINDI, "
				+ "PERMDSABLDINDI, " + "MOBLTYDSABLDINDI,"
				+ "DSABLDPLTINDI, " + DELETEDINDI + ","
				+ "CURRENT TIMESTAMP,  "
				+ SystemControlBatchConstant.DP_PURGE_OFCISSUANCENO
				+ ","
				+ SystemControlBatchConstant.DP_PURGE_OFCISSUANCENO
				+ "," + " '"
				+ SystemControlBatchConstant.DP_PURGE_EMPID
				+ "' FROM RTS.RTS_DSABLD_PLCRD_CUST A " + lsWhere + ")";

		String lsUpdt = "UPDATE RTS.RTS_DSABLD_PLCRD_CUST A set DeleteIndi = 1,"
				+ " CHNGTIMESTMP = CURRENT TIMESTAMP " + lsWhere;

		try
		{
			Log
					.write(Log.SQL, this,
							"purgeSetDelIndiDisabledPlacardCustomer- SQL - Begin");

			int liNumRows0 = caDA.executeDBInsertUpdateDelete(lsIns,
					new Vector());

			int liNumRows = caDA.executeDBInsertUpdateDelete(lsUpdt,
					new Vector());

			Log
					.write(Log.SQL, this,
							"purgeSetDelIndiDisabledPlacardCustomer- SQL - End");

			Log.write(Log.METHOD, this,
					"purgeSetDelIndiDisabledPlacardCustomer- End");

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"purgeDisabledPlacardCustomer- Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to Delete from RTS.RTS_DSABLD_PLCRD_CUST where deleteindi = 1 and
	 * no placards exist
	 * 
	 * @param aaPurgeDate
	 * @return int
	 * @throws RTSException
	 */
	public int purgeDisabledPlacardCustomer(RTSDate aaPurgeDate)
			throws RTSException
	{
		Log.write(Log.METHOD, this,
				"purgeDisabledPlacardCustomer- Begin");

		// Foreign keys will delete from Hstry & InProcs
		String lsDel = "DELETE FROM RTS.RTS_DSABLD_PLCRD_CUST A WHERE ChngTimestmp <= '"
				+ aaPurgeDate.getTimestamp()
				+ "' and deleteindi = 1 and not exists "
				+ " (SELECT * FROM RTS.RTS_DSABLD_PLCRD B "
				+ " WHERE A.CustIdntyNo = B.CustIdntyNo )";

		try
		{
			Log.write(Log.SQL, this,
					"purgeDisabledPlacardCustomer- SQL - Begin");

			int liNumRows = caDA.executeDBInsertUpdateDelete(lsDel,
					new Vector());

			Log.write(Log.SQL, this,
					"purgeDisabledPlacardCustomer- SQL - End");
			Log.write(Log.METHOD, this,
					"purgeDisabledPlacardCustomer- End");

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"purgeDisabledPlacardCustomer- Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to update RTS.RTS_DSABLD_PLCRD_CUST
	 * 
	 * @param aaDPCust
	 * @return int
	 * @throws RTSException
	 */
	public int updDisabledPlacardCustomer(
			DisabledPlacardCustomerData aaDPCust) throws RTSException
	{
		Log.write(Log.METHOD, this,
				"updDisabledPlacardCustomer - Begin");

		Vector lvValues = new Vector();

		// defect 10133
		String lsUpd = "UPDATE RTS.RTS_DSABLD_PLCRD_CUST "
				+ "SET ST1= ?, " + "ST2= ?, " + "CITY= ?, "
				+ "RESCOMPTCNTYNO = ?, " + "STATE= ?, " + "CNTRY= ?, "
				+ "ZPCD= ?, " + "ZPCDP4= ?, " + "EMAIL= ?, "
				+ "PHONE= ?, "
				+ "DSABLDVETPLTINDI= ?, "
				+ "PERMDSABLDINDI= ?, "
				// + "MOBLTYDSABLDINDI = ?, "
				+ "DSABLDPLTINDI = ?, "
				+ "CHNGTIMESTMP = CURRENT TIMESTAMP "
				+ "WHERE CUSTIDNTYNO = ? ";
		// end defect 10133

		AddressData laAddrData = aaDPCust.getAddressData();

		lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
				.convertToString(laAddrData.getSt1())));
		lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
				.convertToString(laAddrData.getSt2())));
		lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
				.convertToString(laAddrData.getCity())));
		lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
				.convertToString(aaDPCust.getResComptCntyNo())));
		lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
				.convertToString(laAddrData.getState())));
		lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
				.convertToString(laAddrData.getCntry())));
		lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
				.convertToString(laAddrData.getZpcd())));
		lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
				.convertToString(laAddrData.getZpcdp4())));
		lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
				.convertToString(aaDPCust.getEMail())));
		lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
				.convertToString(aaDPCust.getPhone())));
		lvValues.addElement(new DBValue(Types.INTEGER, DatabaseAccess
				.convertToString(aaDPCust.getDsabldVetPltIndi())));
		lvValues.addElement(new DBValue(Types.INTEGER, DatabaseAccess
				.convertToString(aaDPCust.getPermDsabldIndi())));
		// defect 10133
		// lvValues.addElement(
		// new DBValue(
		// Types.INTEGER,
		// DatabaseAccess.convertToString(
		// aaDPCust.getMobltyDsabldIndi())));
		// end defect 10133

		lvValues.addElement(new DBValue(Types.INTEGER, DatabaseAccess
				.convertToString(aaDPCust.getDsabldPltIndi())));
		lvValues.addElement(new DBValue(Types.INTEGER, DatabaseAccess
				.convertToString(aaDPCust.getCustIdntyNo())));

		try
		{
			Log.write(Log.SQL, this,
					"updDisabledPlacardCustomer - SQL - Begin");

			int liNumRows = caDA.executeDBInsertUpdateDelete(lsUpd,
					lvValues);

			// Also insert into History table
			insDisabledPlacardCustomerHistory(aaDPCust);

			Log.write(Log.SQL, this,
					"updDisabledPlacardCustomer - SQL - End");
			Log.write(Log.METHOD, this,
					"updDisabledPlacardCustomer - End");
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"updDisabledPlacardCustomer - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} // END OF Update METHOD

	/**
	 * Method to Insert into RTS.RTS_DSABLD_PLCRD_CUST_HSTRY
	 * 
	 * @param aaDPCustData
	 * @throws RTSException
	 */
	public void insDisabledPlacardCustomerHistory(
			DisabledPlacardCustomerData aaDPCustData)
			throws RTSException
	{
		Log.write(Log.METHOD, this,
				"insDisabledPlacardCustomerHstry - Begin");

		Vector lvValues = new Vector();

		String lsIns = "INSERT INTO RTS.RTS_DSABLD_PLCRD_CUST_HSTRY( "
				+ "CUSTIDNTYNO, " + "CUSTID, " + "CUSTIDTYPECD,"
				+ "INSTINDI," + "INSTNAME, " + "DSABLDFRSTNAME, "
				+ "DSABLDMI, " + "DSABLDLSTNAME, " + "ST1, " + "ST2, "
				+ "CITY, " + "RESCOMPTCNTYNO, " + "STATE, " + "CNTRY, "
				+ "ZPCD, " + "ZPCDP4, " + "EMAIL, " + "PHONE, "
				+ "DSABLDVETPLTINDI, " + "PERMDSABLDINDI, "
				+ "MOBLTYDSABLDINDI," + "DSABLDPLTINDI, "
				+ "DELETEINDI, " + "CHNGTIMESTMP," + "OFCISSUANCENO,"
				+ "WSID," + "EMPID )" + " VALUES (" + " ?," + " ?,"
				+ " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?,"
				+ " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?,"
				+ " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " 0,"
				+ " Current Timestamp," + " ?," + " ?," + " ? )";
		try
		{
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getCustIdntyNo())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getCustId())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getCustIdTypeCd())));

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getInstIndi())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getInstName())));

			lvValues
					.addElement(new DBValue(Types.CHAR, DatabaseAccess
							.convertToString(aaDPCustData
									.getDsabldFrstName())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getDsabldMI())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getDsabldLstName())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getSt1())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getSt2())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getCity())));

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getResComptCntyNo())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getState())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getCntry())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getZpcd())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getAddressData()
							.getZpcdp4())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getEMail())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getPhone())));

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getDsabldVetPltIndi())));

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getPermDsabldIndi())));

			// defect 10133
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(MOBILITY_NOT_APPLICABLE)));
			// end defect 10133

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getDsabldPltIndi())));

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getOfcIssuanceNo())));

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aaDPCustData
							.getWsId())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaDPCustData.getEmpId())));

			Log.write(Log.SQL, this,
					"insDisabledPlacardCustomerHistory - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this,
					"insDisabledPlacardCustomerHistory - SQL - End");
			Log.write(Log.METHOD, this,
					"insDisabledPlacardCustomerHistory - End");
		}

		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"insDisabledPlacardCustomerHistory - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} // END OF INSERT METHOD
}
