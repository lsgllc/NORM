package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.OwnerData;
import com.txdot.isd.rts.services.data.SpecialPlateTransactionHistoryData;
import com.txdot.isd.rts.services.data.SpecialPlateUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * SpecialPlateTransactionHistory.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/06/2007	Created
 *							defect 9085 Ver Special Plates
 * K Harrell	02/25/2007	add getOffices(),
 * 							  qrySpecialPlateApplicationReport()
 * 							modify insSpecialPlateTransHstry()
 * 							defect 9085 Ver Special Plates
 * T Pederson	04/10/2007	Changed sql to check for personalized plates
 * 							when requested. 
 * 							modify qrySpecialPlateApplicationReport()
 * 							defect 9123 Ver Special Plates
 * K Harrell	05/23/2007	add ItrntTraceNo to SQL
 * 							modify insSpecialPlateTransactionHistory(),
 * 							 qrySpecialPlateApplicationReport(),
 * 							 qrySpecialPlateTransactionHistory()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/01/2007	Modify to include new SPAPPL transcd
 * 							modify qrySpecialPlateApplicationReport()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/03/2007	Modify to check ofcissuanceno as well
 * 							as rescomptcntyno and order by plate 
 * 							description within county. 
 *						    modify qrySpecialPlateApplicationReport()
 *							defect 9207 Ver Special Plates
 * K Harrell	08/05/2007	Request "no trim" on selection of MfgPltNo
 * 						   	modify qrySpecialPlateApplicationReport()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	09/13/2007	Mark IAPPL, IADDR trans as complete on 
 * 							insert.
 * 							modify insSpecialPlateTransactionHistory()
 * 							defect 9315 Ver Special Plates
 * K Harrell	10/14/2007	Add use of Pilot.RTS_PLT_TYPE 
 * 							modify qrySpecialPlateApplicationReport()
 * 							defect 9354 Ver Special Plates 2
 * K Harrell	01/17/2008	Remove reference to PILOT qualifier
 * 					     	modify qrySpecialPlateApplicationReport()
 * 							defect 9525 Ver 3 Amigos Prep 
 * B Hargrove	07/11/2008	Add pilot for RTS_PLT_TYPE.  
 * 							modify qrySpecialPlateApplicationReport()
 * 							defect 9689 Ver MyPlates_POS
 * K Harrell	08/07/2008	Add IRNR, VPAPPL, VPAPPR to transactions
 * 							which insert completed records
 * 							modify inSpecialPlateTransactionHistory()
 * 							defect 9790 Ver MyPlates_POS  	
 * K Harrell	08/07/2008	Add VPAPPL, VPAPPR transcds to query for 
 * 							MyPlates
 * 							modify qrySpecialPlateApplicationReport()
 * 							defect 9789 Ver MyPlates_POS  
 * K Harrell	01/05/2009	Assign TransEmpId to "LGNERR" if null || 
 * 							TransEmpId.trim().length() == 0 on insert. 
 * 							modify insSpecialPlateTransactionHistory()    
 *	 						defect 9847 Ver Defect_POS_D
 * K Harrell	01/07/2009  Modified in refactor of 
 * 							 SpecialPlateTransactionHistoryData
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify insSpecialPlateTransactionHistory(),
 * 				 			 qrySpecialPlateTransactionHistory(),
 * 							 qrySpecialPlateTransactionReport()	
 *        					defect 9864 Ver Defect_POS_D
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify SpecialPlateTransactionHistory()
 * 							defect 9825 Ver Defect_POS_D
 * K Harrell	07/02/2009	Implement new OwnerData
 * 							delete qrySpecialPlateTransactionHistory() 
 * 							modify insSpeclPltTransactionHistory(), 
 * 							 qrySpecialPlateApplicationReport()
 * 							defect 10112 Ver Defect_POS_F   
 * J Zwiener	09/04/2009	Add new fields to Spcl_Plt_Trans_Hstry
 * 							modify insSpecialPlateTransactionHistory()
 * 							defect 10097 Ver Defect_POS_F
 * K Harrell	02/17/2010	Add ResrvReasnCd, MktngAllowdIndi, AuctnPltIndi, 
 *							 AuctnPdAmt, PltValidityTerm
 * 							modify insSpecialPlateTransactionHistory(), 
 * 							 qrySpecialPlateApplicationReport()
 * 							defect 10366 Ver POS_640    
 * K Harrell 	06/14/2010 	add TransId to insert
 * 							modify insSpecialPlateTransactionHistory() 
 * 							defect 10505 Ver 6.5.0 
 * K Harrell	09/30/2011	Remove reference to Pilot qualifier
 * 							modify qrySpecialPlateApplicationReport()()
 * 							defect 11048 Ver 6.9.0
 * K Harrell	10/08/2011	Use Max RTSEffEndDate for Inquiry against 
 * 							RTS_PLT_TYPE 
 * 							modify qrySpecialPlateApplicationReport()
 * 							defect 10802 Ver 6.9.0 
 * K Harrell	10/28/2011	Restore reference to Pilot qualifier
 * 							modify qrySpecialPlateApplicationReport()
 * 							defect 11061 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for
 * SpecialPlateTransactionHistory
 * 
 * @version 6.9.0 		10/28/2011
 * @author Kathy Harrell
 * @since 				02/06/2007 13:41:00
 */

public class SpecialPlateTransactionHistory
{
	DatabaseAccess caDA;

	/**
	 * SpecialPlateTransactionHistory constructor comment.
	 * 
	 * @param aaDA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	public SpecialPlateTransactionHistory(DatabaseAccess aaDA)
			throws RTSException
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
			lsOffices = lsOffices + CommonConstant.STR_COMMA
					+ avOffices.get(i);

		}
		return lsOffices + ") ";
	}

	/**
	 * Method to Delete from RTS.RTS_SPCL_PLT_TRANS_HSTRY
	 * 
	 * @param aaSpecialPlateTransactionHistoryData
	 *            SpecialPlateTransactionHistoryData
	 * @throws RTSException
	 */
	public void delSpecialPlateTransactionHistory(
			SpecialPlateTransactionHistoryData aaSpecialPlateTransactionHistoryData)
			throws RTSException
	{
		Log.write(Log.METHOD, this,
				"delSpecialPlateTransactionHistory - Begin");

		Vector lvValues = new Vector();

		String lsDel = "DELETE FROM RTS.RTS_SPCL_PLT_TRANS_HSTRY "
				+ "WHERE " + "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? AND " + "TransAMDate = ? AND "
				+ "TransWsId = ? AND " + "CustSeqNo = ? ";
		try
		{
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getOfcIssuanceNo())));
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getSubstaId())));
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getTransAMDate())));
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getTransWsId())));
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getCustSeqNo())));

			if (aaSpecialPlateTransactionHistoryData.getTransTime() != 0)
			{
				lsDel = lsDel + " AND TransTime = ? ";
				lvValues
						.addElement(new DBValue(
								Types.INTEGER,
								DatabaseAccess
										.convertToString(aaSpecialPlateTransactionHistoryData
												.getTransTime())));
			}
			Log.write(Log.SQL, this,
					"delSpecialPlateTransactionHistory - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this,
					"delSpecialPlateTransactionHistory - SQL - End");
			Log.write(Log.METHOD, this,
					"delSpecialPlateTransactionHistory - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"delSpecialPlateTransactionHistory - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} // END OF Delete METHOD

	/**
	 * Method to VOID RTS.RTS_SPCL_PLT_TRANS_HSTRY
	 * 
	 * @param aaSpecialPlateTransactionHistoryData
	 *            SpecialPlateTransactionHistoryData
	 * @throws RTSException
	 */
	public void voidSpecialPlateTransactionHistory(
			SpecialPlateTransactionHistoryData aaSpecialPlateTransactionHistoryData)
			throws RTSException
	{
		Log.write(Log.METHOD, this,
				"voidSpecialPlateTransactionHistory - Begin");

		Vector lvValues = new Vector();

		String lsUpd = "UPDATE RTS.RTS_SPCL_PLT_TRANS_HSTRY "
				+ " SET VOIDEDTRANSINDI= 1 " + "WHERE "
				+ "OfcIssuanceNo = ? AND " + "SubstaId = ? AND "
				+ "TransAMDate = ? AND " + "TransWsId = ? AND "
				+ "Transtime = ? AND " + "TransCompleteIndi = 1 ";
		try
		{
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getOfcIssuanceNo())));
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getSubstaId())));
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getTransAMDate())));
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getTransWsId())));

			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getTransTime())));
			Log.write(Log.SQL, this,
					"voidSpecialPlateTransactionHistory - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this,
					"voidSpecialPlateTransactionHistory - SQL - End");
			Log.write(Log.METHOD, this,
					"voidSpecialPlateTransactionHistory - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"voidSpecialPlateTransactionHistory - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} // END OF VOID METHOD

	/**
	 * Method to mark RTS.RTS_SPCL_PLT_TRANS_HSTRY as complete
	 * 
	 * @param aaSpecialPlateTransactionHistoryData
	 *            SpecialPlateTransactionHistoryData
	 * @throws RTSException
	 */
	public void updSpecialPlateTransactionHistory(
			SpecialPlateTransactionHistoryData aaSpecialPlateTransactionHistoryData)
			throws RTSException
	{
		Log.write(Log.METHOD, this,
				"updSpecialPlateTransactionHistory - Begin");

		Vector lvValues = new Vector();

		String lsUpd = "UPDATE RTS.RTS_SPCL_PLT_TRANS_HSTRY "
				+ " SET TRANSCOMPLETEINDI = 1 WHERE  "
				+ " OfcIssuanceNo = ? AND " + " SubstaId = ? AND "
				+ " TransAMDate = ? AND " + " TransWsId = ? AND "
				+ " CustSeqNo = ? ";
		try
		{
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getOfcIssuanceNo())));
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getSubstaId())));
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getTransAMDate())));
			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getTransWsId())));

			lvValues
					.addElement(new DBValue(
							Types.INTEGER,
							DatabaseAccess
									.convertToString(aaSpecialPlateTransactionHistoryData
											.getCustSeqNo())));
			Log.write(Log.SQL, this,
					"updSpecialPlateTransactionHistory - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(Log.SQL, this,
					"updSpecialPlateTransactionHistory - SQL - End");

			Log.write(Log.METHOD, this,
					"updSpecialPlateTransactionHistory - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"updSpecialPlateTransactionHistory - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} // END OF UPDATE METHOD

	/**
	 * Method to Insert into RTS.RTS_SPCL_PLT_TRANS_HSTRY
	 * 
	 * @param aaSpecialPlateTransactionHistoryData
	 *            SpecialPlateTransactionHistoryData
	 * @throws RTSException
	 */
	public void insSpecialPlateTransactionHistory(
			SpecialPlateTransactionHistoryData aaSpclPltTransHstryData)
			throws RTSException
	{
		Log.write(Log.METHOD, this,
				"insSpecialPlateTransactionHistory - Begin");

		Vector lvValues = new Vector();

		// defect 9847
		if (aaSpclPltTransHstryData.getTransEmpId() == null
				|| aaSpclPltTransHstryData.getTransEmpId().trim()
						.length() == 0)
		{
			aaSpclPltTransHstryData
					.setTransEmpId(CommonConstant.DEFAULT_TRANSEMPID);
		}
		// end defect 9847

		// defect 9315 , 9790
		// Mark as complete for IAPPL, IADDR, IRNR, VPAPPL, VPAPPR
		String lsTransCd = aaSpclPltTransHstryData.getTransCd().trim();

		int liTransCompleteIndi = (lsTransCd
				.equals(TransCdConstant.IAPPL)
				|| lsTransCd.equals(TransCdConstant.IADDR)
				|| lsTransCd.equals(TransCdConstant.IRNR)
				|| lsTransCd.equals(TransCdConstant.VPAPPL) || lsTransCd
				.equals(TransCdConstant.VPAPPR)) ? 1 : 0;

		// defect 10505
		// Add TransId

		// defect 10366
		// Add ResrvReasnCd, MktngAllowdIndi, AuctnPltIndi,
		// AuctnPdAmt, PltValidityTerm

		// defect 10097
		// Add PriorPltExpMo, PriorPltExpYr, PltFee, and PLPFee
		// defect 9864
		// Use PltExpMo/Yr vs. RegExpMo/Yr
		String lsIns = "INSERT into RTS.RTS_SPCL_PLT_TRANS_HSTRY("
				+ "OfcIssuanceNo," + "SubstaId," + "TransAMDate,"
				+ "TransWsId," + "CustSeqNo," + "TransTime,"
				+ "TransCd," + "TransId," + "TransEmpId," + "RegPltCd,"
				+ "OrgNo," + "RegPltNo," + "MfgPltNo," + "PltExpMo,"
				+ "PltExpYr," + "PltOwnrName1," + "PltOwnrName2,"
				+ "PltOwnrSt1," + "PltOwnrSt2," + "PltOwnrCity,"
				+ "PltOwnrState," + "PltOwnrZpCd," + "PltOwnrZpCd4,"
				+ "PltOwnrCntry," + "PltOwnrPhone," + "PltOwnrEMail,"
				+ "ISAIndi," + "ChrgSpclPltFeeIndi,"
				+ "ResComptCntyNo," + "ItrntTraceNo,"
				+ "PriorPltExpMo," + "PriorPltExpYr," + "PltFee,"
				+ "PLPFee," + "ResrvReasnCd," + "MktngAllowdIndi,"
				+ "AuctnPltIndi," + "AuctnPdAmt," + "PltValidityTerm,"
				+ "TransCompleteIndi," + "VoidedTransIndi)";
		// end defect 9864

		lsIns = lsIns + "VALUES ( " + " ?," + " ?," + " ?," + " ?,"
				+ " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?,"
				+ " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?,"
				+ " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?,"
				+ " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?,"
				+ " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?,"
				+ liTransCompleteIndi + " ,0)";
		// end defect 9315, 9790, 10097
		// end defect 10366
		// end defect 10505

		try
		{
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getOfcIssuanceNo())));
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getSubstaId())));
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getTransAMDate())));
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getTransWsId())));
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getCustSeqNo())));
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getTransTime())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaSpclPltTransHstryData
							.getTransCd())));

			// defect 10505
			lvValues.addElement(new DBValue(Types.CHAR,
					aaSpclPltTransHstryData.getTransId()));
			// end defect 10505

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaSpclPltTransHstryData
							.getTransEmpId())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaSpclPltTransHstryData
							.getRegPltCd())));
			lvValues.addElement(new DBValue(Types.CHAR,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getOrgNo())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaSpclPltTransHstryData
							.getRegPltNo())));
			lvValues.addElement(new DBValue(Types.CHAR,
					aaSpclPltTransHstryData.getMfgPltNo()));

			// defect 9864
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getPltExpMo())));
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getPltExpYr())));
			// end defect 9864

			// defect 10112
			OwnerData laPltOwnrData = aaSpclPltTransHstryData
					.getPltOwnerData();

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(laPltOwnrData.getName1())));
			// aaSpclPltTransHstryData.getPltOwnrName1())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(laPltOwnrData.getName2())));
			// aaSpclPltTransHstryData.getPltOwnrName2())));

			AddressData laPltAddrData = laPltOwnrData.getAddressData();

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(laPltAddrData.getSt1())));
			// aaSpclPltTransHstryData.getPltOwnrSt1())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(laPltAddrData.getSt2())));
			// aaSpclPltTransHstryData.getPltOwnrSt2())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(laPltAddrData.getCity())));
			// aaSpclPltTransHstryData.getPltOwnrCity())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(laPltAddrData.getState())));
			// aaSpclPltTransHstryData.getPltOwnrState())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(laPltAddrData.getZpcd())));
			// aaSpclPltTransHstryData.getPltOwnrZpCd())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(laPltAddrData.getZpcdp4())));
			// aaSpclPltTransHstryData.getPltOwnrZpCd4())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(laPltAddrData.getCntry())));
			// aaSpclPltTransHstryData.getPltOwnrCntry())));
			// end defect 10112

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaSpclPltTransHstryData
							.getPltOwnrPhone())));

			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaSpclPltTransHstryData
							.getPltOwnrEMail())));

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getISAIndi())));

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getChrgSpclPltFeeIndi())));

			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getResComptCntyNo())));
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaSpclPltTransHstryData
							.getItrntTraceNo())));

			// defect 10097
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getPriorPltExpMo())));
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getPriorPltExpYr())));
			lvValues.addElement(new DBValue(Types.DECIMAL,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getPltFee())));
			lvValues.addElement(new DBValue(Types.DECIMAL,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getPLPFee())));
			// end defect 10097
			// defect 10366
			// Add ResrvReasnCd, MktngAllowdIndi, AuctnPltIndi,
			// AuctnPdAmt, PltValidityTerm
			lvValues.addElement(new DBValue(Types.CHAR, DatabaseAccess
					.convertToString(aaSpclPltTransHstryData
							.getResrvReasnCd())));
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getMktngAllowdIndi())));
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getAuctnPltIndi())));
			lvValues.addElement(new DBValue(Types.DECIMAL,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getAuctnPdAmt())));
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess
							.convertToString(aaSpclPltTransHstryData
									.getPltValidityTerm())));
			// end defect 10366

			Log.write(Log.SQL, this,
					"insSpecialPlateTransactionHistory - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this,
					"insSpecialPlateTransactionHistory - SQL - End");
			Log.write(Log.METHOD, this,
					"insSpecialPlateTransactionHistory - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"insSpecialPlateTransactionHistory - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} // END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_SPCL_PLT_TRANS_HSTRY for purge
	 * 
	 * @param aiPurgeAMDate
	 *            int
	 * @return int
	 * @throws RTSException
	 */
	public int purgeSpecialPlateTransactionHistory(int aiPurgeAMDate)
			throws RTSException
	{
		Log.write(Log.METHOD, this,
				"purgeSpecialPlateTransactionHistory - Begin");

		Vector lvValues = new Vector();

		String lsDel = " DELETE FROM RTS.RTS_SPCL_PLT_TRANS_HSTRY  "
				+ " WHERE  " + " TRANSAMDATE <= ? ";

		try
		{
			lvValues.addElement(new DBValue(Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));

			Log
					.write(Log.SQL, this,
							"purgeSpecialPlateTransactionHistory - SQL - Begin");
			// defect 9825
			// Return number of rows purged
			int liNumRows = caDA.executeDBInsertUpdateDelete(lsDel,
					lvValues);
			Log.write(Log.SQL, this,
					"purgeSpecialPlateTransactionHistory - SQL - End");
			Log.write(Log.METHOD, this,
					"purgeSpecialPlateTransactionHistory - End");
			return liNumRows;
			// end defect 9825
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"purgeSpecialPlateTransactionHistory - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} // END OF Delete METHOD

	// /**
	// * Method to Query from RTS.RTS_SPCL_PLT_TRANS_HSTRY
	// *
	// * @param aaSpecialPlateTransactionHistoryData
	// SpecialPlateTransactionHistoryData
	// * @return Vector
	// * @throws RTSException
	// */
	// public Vector
	// qrySpecialPlateTransactionHistory(SpecialPlateTransactionHistoryData
	// aaSpecialPlateTransactionHistoryData)
	// throws RTSException
	// {
	// Log.write(
	// Log.METHOD,
	// this,
	// " - qrySpecialPlateTransactionHistory - Begin");
	//
	// StringBuffer lsQry = new StringBuffer();
	//
	// Vector lvRslt = new Vector();
	//
	// Vector lvValues = new Vector();
	//
	// ResultSet lrsQry;
	//
	// // defect 9864
	// // Use PltExpMo/Yr vs. RegExpMo/Yr
	//
	// lsQry.append(
	// "SELECT "
	// + "OfcIssuanceNo,"
	// + "SubstaId,"
	// + "TransAMDate,"
	// + "TransWsId,"
	// + "CustSeqNo,"
	// + "TransTime,"
	// + "TransCd,"
	// + "TransEmpId,"
	// + "RegPltCd,"
	// + "OrgNo,"
	// + "RegPltNo,"
	// + "MfgPltNo,"
	// + "PltExpMo,"
	// + "PltExpYr,"
	// + "PltOwnrName1,"
	// + "PltOwnrName2,"
	// + "PltOwnrSt1,"
	// + "PltOwnrSt2,"
	// + "PltOwnrCity,"
	// + "PltOwnrState,"
	// + "PltOwnrZpCd,"
	// + "PltOwnrZpCd4,"
	// + "PltOwnrCntry,"
	// + "PltOwnrPhone,"
	// + "PltOwnrEMail,"
	// + "ISAIndi,"
	// + "ChrgSpclPltFeeIndi,"
	// + "ResComptCntyNo,"
	// + "TransCompleteIndi, "
	// + "VoidedTransIndi, "
	// + "ItrntTraceNo "
	// + "FROM RTS.RTS_SPCL_PLT_TRANS_HSTRY "
	// + "WHERE "
	// + "A.OfcIssuanceNo = ? AND "
	// + "A.TransAMDate = ? ");
	// // end defect 9864
	//
	// try
	// {
	// lvValues.addElement(
	// new DBValue(
	// Types.INTEGER,
	// DatabaseAccess.convertToString(
	// aaSpecialPlateTransactionHistoryData
	// .getOfcIssuanceNo())));
	// lvValues.addElement(
	// new DBValue(
	// Types.INTEGER,
	// DatabaseAccess.convertToString(
	// aaSpecialPlateTransactionHistoryData
	// .getTransAMDate())));
	//
	// Log.write(
	// Log.SQL,
	// this,
	// " - qrySpecialPlateTransactionHistory - SQL - Begin");
	// lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
	// Log.write(
	// Log.SQL,
	// this,
	// " - qrySpecialPlateTransactionHistory - SQL - End");
	//
	// while (lrsQry.next())
	// {
	//
	// SpecialPlateTransactionHistoryData laSpecialPlateTransactionHistoryData =
	// new SpecialPlateTransactionHistoryData();
	// laSpecialPlateTransactionHistoryData.setOfcIssuanceNo(
	// caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
	// laSpecialPlateTransactionHistoryData.setSubstaId(
	// caDA.getIntFromDB(lrsQry, "SubstaId"));
	// laSpecialPlateTransactionHistoryData.setTransAMDate(
	// caDA.getIntFromDB(lrsQry, "TransAMDate"));
	// laSpecialPlateTransactionHistoryData.setTransWsId(
	// caDA.getIntFromDB(lrsQry, "TransWsId"));
	// laSpecialPlateTransactionHistoryData.setCustSeqNo(
	// caDA.getIntFromDB(lrsQry, "CustSeqNo"));
	// laSpecialPlateTransactionHistoryData.setTransTime(
	// caDA.getIntFromDB(lrsQry, "TransTime"));
	// laSpecialPlateTransactionHistoryData.setTransCd(
	// caDA.getStringFromDB(lrsQry, "TransCd"));
	// laSpecialPlateTransactionHistoryData.setTransEmpId(
	// caDA.getStringFromDB(lrsQry, "TransEmpId"));
	// laSpecialPlateTransactionHistoryData.setRegPltCd(
	// caDA.getStringFromDB(lrsQry, "RegPltCd"));
	// laSpecialPlateTransactionHistoryData.setOrgNo(
	// caDA.getStringFromDB(lrsQry, "OrgNo"));
	// laSpecialPlateTransactionHistoryData.setRegPltNo(
	// caDA.getStringFromDB(lrsQry, "RegPltNo"));
	//
	// // defect 9864
	// laSpecialPlateTransactionHistoryData.setPltExpMo(
	// caDA.getIntFromDB(lrsQry, "PltExpMo"));
	// laSpecialPlateTransactionHistoryData.setPltExpYr(
	// caDA.getIntFromDB(lrsQry, "PltExpYr"));
	// // end defect 9864
	//
	// laSpecialPlateTransactionHistoryData.setMfgPltNo(
	// caDA.getStringFromDB(lrsQry, "MfgPltNo", false));
	// laSpecialPlateTransactionHistoryData.setPltOwnrName1(
	// caDA.getStringFromDB(lrsQry, "PltOwnrName1"));
	// laSpecialPlateTransactionHistoryData.setPltOwnrName2(
	// caDA.getStringFromDB(lrsQry, "PltOwnrName2"));
	// laSpecialPlateTransactionHistoryData.setPltOwnrSt1(
	// caDA.getStringFromDB(lrsQry, "PltOwnrSt1"));
	// laSpecialPlateTransactionHistoryData.setPltOwnrSt2(
	// caDA.getStringFromDB(lrsQry, "PltOwnrSt2"));
	// laSpecialPlateTransactionHistoryData.setPltOwnrCity(
	// caDA.getStringFromDB(lrsQry, "PltOwnrCity"));
	// laSpecialPlateTransactionHistoryData.setPltOwnrState(
	// caDA.getStringFromDB(lrsQry, "PltOwnrState"));
	// laSpecialPlateTransactionHistoryData.setPltOwnrState(
	// caDA.getStringFromDB(lrsQry, "PltOwnrState"));
	// laSpecialPlateTransactionHistoryData.setPltOwnrZpCd(
	// caDA.getStringFromDB(lrsQry, "PltOwnrZpCd"));
	// laSpecialPlateTransactionHistoryData.setPltOwnrZpCd4(
	// caDA.getStringFromDB(lrsQry, "PltOwnrZpCd4"));
	// laSpecialPlateTransactionHistoryData.setPltOwnrCntry(
	// caDA.getStringFromDB(lrsQry, "PltOwnrCntry"));
	// laSpecialPlateTransactionHistoryData.setPltOwnrPhone(
	// caDA.getStringFromDB(lrsQry, "PltOwnrPhone"));
	// laSpecialPlateTransactionHistoryData.setPltOwnrEMail(
	// caDA.getStringFromDB(lrsQry, "PltOwnrEmail"));
	// laSpecialPlateTransactionHistoryData.setPltOwnrCntry(
	// caDA.getStringFromDB(lrsQry, "PltOwnrCntry"));
	// laSpecialPlateTransactionHistoryData.setItrntTraceNo(
	// caDA.getStringFromDB(lrsQry, "ItrntTraceNo"));
	// laSpecialPlateTransactionHistoryData
	// .setChrgSpclPltFeeIndi(
	// caDA.getIntFromDB(lrsQry, "ChrgSpclPltFeeIndi"));
	// laSpecialPlateTransactionHistoryData.setResComptCntyNo(
	// caDA.getIntFromDB(lrsQry, "ResComptCntyNo"));
	// laSpecialPlateTransactionHistoryData
	// .setTransCompleteIndi(
	// caDA.getIntFromDB(lrsQry, "TransCompleteIndi"));
	// laSpecialPlateTransactionHistoryData
	// .setVoidedTransIndi(
	// caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));
	//
	// // Add element to the Vector
	// lvRslt.addElement(laSpecialPlateTransactionHistoryData);
	// } //End of While
	//
	// lrsQry.close();
	// caDA.closeLastDBStatement();
	// lrsQry = null;
	// Log.write(
	// Log.METHOD,
	// this,
	// " - qrySpecialPlateTransactionHistory - End ");
	// return (lvRslt);
	// }
	// catch (SQLException aeSQLEx)
	// {
	// Log.write(
	// Log.SQL_EXCP,
	// this,
	// " - qrySpecialPlateTransactionHistory - Exception "
	// + aeSQLEx.getMessage());
	// throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	// }
	// } //END OF QUERY METHOD

	/**
	 * Method to Query from RTS.RTS_SPCL_PLT_TRANS_HSTRY
	 * 
	 * @param aaSpecialPlateTransactionHistoryData
	 *            SpecialPlateTransactionHistoryData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qrySpecialPlateApplicationReport(
			SpecialPlateUIData aaSpclPlateUIData) throws RTSException
	{
		Log.write(Log.METHOD, this,
				" - qrySpecialPlateApplicationReport - Begin");
		RTSDate laBeginDate = aaSpclPlateUIData.getBeginDate();
		RTSDate laEndDate = aaSpclPlateUIData.getEndDate();
		String lsOffices = getOffices(aaSpclPlateUIData
				.getSelectedOffices());
		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		String lsPLPWhereClause = null;
		if (aaSpclPlateUIData.isPersonalizedOnly())
		{
			lsPLPWhereClause = " and UserPltNoIndi = 1 ";
		}
		else
		{
			lsPLPWhereClause = "";
		}

		// Use reference to PILOT qualifier
		String lsTableCreator = CommonConstant.RTS_TBCREATOR;
		
		// defect 11061
		 if (SystemProperty.isStaticTablePilot())
		 {
			 lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		 }
		// end defect 11061
		
		// defect 9864
		// Use PltExpMo/Yr vs. RegExpMo/Yr

		// defect 9789
		// Add VPAPPL, VPAPPR transcds to list for MyPlates

		// defect 9207
		// Add check Ofcissuanceno as well as Rescomptcntyno, order by
		// plate description w/in ResComptCntyNo

		// defect 10366
		// Add ResrvReasnCd, MktngAllowdIndi, AuctnPltIndi,
		// AuctnPdAmt, PltValidityTerm

		// defect 10802
		// Use Max(RTSEffEndDate) for query against RTS_PLT_TYPE 
		lsQry.append("SELECT " + "OfcIssuanceNo," + "SubstaId,"
				+ "TransAMDate," + "TransWsId," + "CustSeqNo,"
				+ "TransTime," + "TransCd," + "TransEmpId,"
				+ "A.RegPltCd," + "ItmCdDesc," + "A.OrgNo,"
				+ "RegPltNo," + "MfgPltNo," + "PltExpMo," + "PltExpYr,"
				+ "PltOwnrName1,"
				+ "PltOwnrName2,"
				+ "PltOwnrSt1,"
				+ "PltOwnrSt2,"
				+ "PltOwnrCity,"
				+ "PltOwnrState,"
				+ "PltOwnrZpCd,"
				+ "PltOwnrZpCd4,"
				+ "PltOwnrCntry,"
				+ "PltOwnrPhone,"
				+ "PltOwnrEMail,"
				+ "ISAIndi,"
				+ "ChrgSpclPltFeeIndi,"
				+ "ResComptCntyNo,"
				+ "TransCompleteIndi, "
				+ "VoidedTransIndi, "
				+ "ItrntTraceNo, "
				+ "ResrvReasnCd,"
				+ "MktngAllowdIndi,"
				+ "AuctnPltIndi,"
				+ "AuctnPdAmt, "
				+ " PltValidityTerm "
				+ "FROM RTS.RTS_SPCL_PLT_TRANS_HSTRY A,"
				+ lsTableCreator + ".RTS_PLT_TYPE B ,"
				+ "RTS.RTS_ITEM_CODES C " + "WHERE "
				+ "A.REGPLTCD = B.REGPLTCD AND "
				+ "A.REGPLTCD = C.ITMCD AND "
				//+ "B.RTSEFFENDDATE = 99991231 AND "
				+ "B.RTSEFFENDDATE = " 
				+ " (SELECT MAX(RTSEFFENDDATE) FROM "
				+ lsTableCreator +
				".RTS_PLT_TYPE D"
				+ " WHERE D.REGPLTCD = "
				+ " B.REGPLTCD) AND "
				+ " A.TransAMDate between " + laBeginDate.getAMDate()
				+ " and " + laEndDate.getAMDate()
				+ " and (A.ResComptCntyNo in " + lsOffices
				+ " or A.OfcIssuanceNo in " + lsOffices + " )"
				+ " and A.TransCd in ('SPAPPL', 'SPAPPR',"
				+ " 'SPAPPO', 'SPAPPC', 'SPAPPI',"
				+ " 'IAPPL', 'VPAPPL', 'VPAPPR')"
				+ " and TransCompleteIndi = 1" + lsPLPWhereClause
				+ " Order by ResComptCntyNo, ItmCdDesc,TransAMDate,"
				+ " SubstaId, TransWsId ");
		// end defect 9207
		// end defect 9789
		// end defect 9689
		// end defect 9864
		// end defect 10366
		// end defect 10802 

		try
		{

			Log
					.write(Log.SQL, this,
							" - qrySpecialPlateTransactionHistory - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this,
					" - qrySpecialPlateTransactionHistory - SQL - End");

			while (lrsQry.next())
			{

				SpecialPlateTransactionHistoryData laSpclPltTransHstryData = new SpecialPlateTransactionHistoryData();
				laSpclPltTransHstryData.setOfcIssuanceNo(caDA
						.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laSpclPltTransHstryData.setSubstaId(caDA.getIntFromDB(
						lrsQry, "SubstaId"));
				laSpclPltTransHstryData.setTransAMDate(caDA
						.getIntFromDB(lrsQry, "TransAMDate"));
				laSpclPltTransHstryData.setTransWsId(caDA.getIntFromDB(
						lrsQry, "TransWsId"));
				laSpclPltTransHstryData.setCustSeqNo(caDA.getIntFromDB(
						lrsQry, "CustSeqNo"));
				laSpclPltTransHstryData.setTransTime(caDA.getIntFromDB(
						lrsQry, "TransTime"));
				laSpclPltTransHstryData.setTransCd(caDA
						.getStringFromDB(lrsQry, "TransCd"));
				laSpclPltTransHstryData.setTransEmpId(caDA
						.getStringFromDB(lrsQry, "TransEmpId"));
				laSpclPltTransHstryData.setRegPltCd(caDA
						.getStringFromDB(lrsQry, "RegPltCd"));
				laSpclPltTransHstryData.setOrgNo(caDA.getStringFromDB(
						lrsQry, "OrgNo"));
				laSpclPltTransHstryData.setRegPltNo(caDA
						.getStringFromDB(lrsQry, "RegPltNo"));

				// defect 9864
				laSpclPltTransHstryData.setPltExpMo(caDA.getIntFromDB(
						lrsQry, "PltExpMo"));
				laSpclPltTransHstryData.setPltExpYr(caDA.getIntFromDB(
						lrsQry, "PltExpYr"));
				// end defect 9864

				laSpclPltTransHstryData.setMfgPltNo(caDA
						.getStringFromDB(lrsQry, "MfgPltNo", false));

				// defect 10112
				OwnerData laPltOwnrData = laSpclPltTransHstryData
						.getPltOwnerData();

				// laSpclPltTransHstryData.setPltOwnrName1(
				laPltOwnrData.setName1(caDA.getStringFromDB(lrsQry,
						"PltOwnrName1"));

				// laSpclPltTransHstryData.setPltOwnrName2(
				laPltOwnrData.setName2(caDA.getStringFromDB(lrsQry,
						"PltOwnrName2"));

				AddressData laPltAddrData = laPltOwnrData
						.getAddressData();

				// laSpclPltTransHstryData.setPltOwnrSt1(
				laPltAddrData.setSt1(caDA.getStringFromDB(lrsQry,
						"PltOwnrSt1"));

				// laSpclPltTransHstryData.setPltOwnrSt2(
				laPltAddrData.setSt2(caDA.getStringFromDB(lrsQry,
						"PltOwnrSt2"));

				// laSpclPltTransHstryData.setPltOwnrCity(
				laPltAddrData.setCity(caDA.getStringFromDB(lrsQry,
						"PltOwnrCity"));

				// laSpclPltTransHstryData.setPltOwnrState(
				laPltAddrData.setState(caDA.getStringFromDB(lrsQry,
						"PltOwnrState"));

				// laSpclPltTransHstryData.setPltOwnrZpCd(
				laPltAddrData.setZpcd(caDA.getStringFromDB(lrsQry,
						"PltOwnrZpCd"));

				// laSpclPltTransHstryData.setPltOwnrZpCd4(
				laPltAddrData.setZpcdp4(caDA.getStringFromDB(lrsQry,
						"PltOwnrZpCd4"));

				// laSpclPltTransHstryData.setPltOwnrCntry(
				laPltAddrData.setCntry(caDA.getStringFromDB(lrsQry,
						"PltOwnrCntry"));
				// end defect 10112

				laSpclPltTransHstryData.setPltOwnrPhone(caDA
						.getStringFromDB(lrsQry, "PltOwnrPhone"));
				laSpclPltTransHstryData.setPltOwnrEMail(caDA
						.getStringFromDB(lrsQry, "PltOwnrEmail"));
				laSpclPltTransHstryData.setItrntTraceNo(caDA
						.getStringFromDB(lrsQry, "ItrntTraceNo"));
				laSpclPltTransHstryData.setChrgSpclPltFeeIndi(caDA
						.getIntFromDB(lrsQry, "ChrgSpclPltFeeIndi"));
				laSpclPltTransHstryData.setResComptCntyNo(caDA
						.getIntFromDB(lrsQry, "ResComptCntyNo"));
				laSpclPltTransHstryData.setTransCompleteIndi(caDA
						.getIntFromDB(lrsQry, "TransCompleteIndi"));
				laSpclPltTransHstryData.setVoidedTransIndi(caDA
						.getIntFromDB(lrsQry, "VoidedTransIndi"));

				// defect 10366
				// Add ResrvReasonCd, MktngAllowdIndi, AuctnPltIndi,
				// AuctnPdAmt, PltValidityTerm
				laSpclPltTransHstryData.setResrvReasnCd(caDA
						.getStringFromDB(lrsQry, "ResrvReasnCd"));
				laSpclPltTransHstryData.setMktngAllowdIndi(caDA
						.getIntFromDB(lrsQry, "MktngAllowdIndi"));
				laSpclPltTransHstryData.setAuctnPltIndi(caDA
						.getIntFromDB(lrsQry, "AuctnPltIndi"));
				laSpclPltTransHstryData.setAuctnPdAmt(caDA
						.getDollarFromDB(lrsQry, "AuctnPdAmt"));
				laSpclPltTransHstryData.setPltValidityTerm(caDA
						.getIntFromDB(lrsQry, "PltValidityTerm"));
				// end defect 10366

				// Add element to the Vector
				lvRslt.addElement(laSpclPltTransHstryData);
			} // End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this,
					" - qrySpecialPlateTransactionHistory - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - qrySpecialPlateTransactionHistory - Exception "
							+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this,
					"qrySpecialPlateTransactionHistory - Exception - "
							+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} // END OF QUERY METHOD
} // END OF CLASS
