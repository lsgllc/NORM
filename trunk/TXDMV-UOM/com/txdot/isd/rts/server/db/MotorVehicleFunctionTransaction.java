package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.LienholderData;
import com
	.txdot
	.isd
	.rts
	.services
	.data
	.MotorVehicleFunctionTransactionData;
import com.txdot.isd.rts.services.data.OwnerData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * MotorVehicleFunctionTransaction.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell    10/18/2001  Added Columns to qry for SendTrans
 *                           (OfcIssuanceCd,TransEmpId)
 * K Harrell	10/19/2001  Added purgeMotorVehicleFunctionTransaction
 * K Harrell 	10/23/2001 	Deleted updMotorVehicleFunctionTransaction
 *                          Added voidMotorVehicleFunctionTransaction
 * K Harrell  	11/11/2001  Removed OfcIssuanceNo,SubstaId from Purge
 * K Harrell   	03/27/2002  Removed vin from 
 *							voidMotorVehicleFunctionTransaction
 * K Harrell    04/02/2002  Altered qry for SubstaId 12/
 * 							OfcIssuanceNo 291
 * R Hicks      07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * K Harrell	10/27/2004	Alter delete stmt to only regard TransAMDate
 *							modify purgeMotorVehicleFunctionTransaction()
 *							defect 7684  Ver 5.2.2
 * K Harrell	03/30/2005	Java 1.4 Work
 *							Remove unnecessary imports. 
 *							defect 7899 Ver 5.2.3 
 * K Harrell	11/11/2005	Accept passed int vs. 
 * 							MotorVehicleFunctionTransaction Object
 * 							modify purgeMotorVehicleFunctionTransaction() 
 * 							defect 8423 Ver 5.2.3
 * K Harrell	09/28/2006	New Columns for Exempts Project
 * 							add HoopsRegPltNo,PltBirthDate,VehValue,
 * 							EmissionSalesTax,LemonLawIndi to SQL
 * 							modify insMotorVehicleFunctionTransaction(),
 * 							 qryMotorVehicleFunctionTransaction() 
 * 							defects 8901,8902,8903 Ver Exempts
 * K Harrell	10/19/2006	Do not include join in SendTrans query for
 * 							OfcIssuanceno 291 and Substaid 12 
 * 							Implemented in SendTrans. 
 * 							modify qryMotorVehicleFunctionTransaction()
 * 							defect 8981 Ver Exempts
 * J Rue		04/03/2008	add ChildSupportIndi, Disssociated, V21PltId, 
 * 							  V21VTNId, VTNSource, TtlSignDate, 
 * 							  TtlTrnsfrPnltyExmptCd, TtlTrnsfrEntCd, 
 * 							  ETtlCd, PrismLvlCd
 * 							modify insMotorVehicleFunctionTransaction(),
 * 								qryMotorVehicleFunctionTransaction()
 * 							defect 9581 Ver Defect POS A
 * B Hargrove	04/04/2008	Comment out references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe (see: defect 9557).
 * 							modify insMotorVehicleFunctionTransaction(),
 * 							qryMotorVehicleFunctionTransaction()
 * 							defect 9631 Ver Defect POS A
 * K Harrell	05/21/2008	delete TtlTrnsfrPnltyExmptCd from 
 * 							 query, insert
 * 							modify insMotorVehicleFunctionTransaction(),
 * 								qryMotorVehicleFunctionTransaction()
 * 							defect 9581 Ver Defect POS A
 * K Harrell	06/20/2008	Restore TtlTrnsfrPnltyExmptCd to  
 * 							 query, insert
 * 							modify insMotorVehicleFunctionTransaction(),
 * 								qryMotorVehicleFunctionTransaction()
 * 							defect 9581 Ver Defect POS A
 * K Harrell	11/08/2008	Add method to query MVFuncTrans to populate 
 * 							Disabled Placard Tables for load testing. 
 * 							add qryMotorVehicleFunctionTransaction(int,int,int)
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeMotorVehicleFunctionTransaction()
 * 							defect 9825 Ver Defect_POS_D
 * K Harrell	02/25/2009	Add new ELT columns 
 * 							 PERMLIENHLDRID1,PERMLIENHLDRID2,
 * 							 PERMLIENHLDRID3,LIENRLSEDATE1,
 * 							 LIENRLSEDATE2,LIENRLSEDATE3,UTVMISLBLINDI,
 * 							 VTRREGEMRGCD1,VTRREGEMRGCD2,VTRTTLEMRGCD1,
 * 							 VTRTTLEMRGCD2
 * 							modify insMotorVehicleFunctionTransaction(),
 *     						 qryMotorVehicleFunctionTransaction
 * 								(MotorVehicleFunctionTransactionData)
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	07/02/2009	Implement new OwnerData, etc. 
 * 							modify insMotorVehicleFuncitonTransaction(), 
 * 							  qryMotorVehicleFuncitonTransaction
 * 								 (MotorVehicleFunctionTransactionData),
 *  						defect 10112 Ver Defect_POS_F 
 * K Harrell	09/15/2009	Correct setting of LemonLawIndi for SendTrans
 * 							modify  qryMotorVehicleFuncitonTransaction
 * 								 (MotorVehicleFunctionTransactionData)
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	10/14/2009	Remove reference to PrivacyOptCd.  Hardcoded
 * 							in SendTrans 
 * 							modify  qryMotorVehicleFunctionTransaction
 * 								 (MotorVehicleFunctionTransactionData), 
 * 								insMotorVehicleFunctionTransaction()
 * 							defect 10246 Ver Defect_POS_G 
 * K Harrell	03/09/2010	Delete query for Disabled Placard Stress Test
 * 							delete qryMotorVehicleFunctionTransaction(int,int,int)
 * 							defect 10210 Ver POS_640
 * K Harrell	03/23/2010	Implement PvtLawEnfVehCd, NonTtlGolfCartCd 
 * 							 vs. VTRTtlEmrgCd1, VTRTtlEmrgCd2
 * 							Implement VTRTtlEmrgCd3, VTRTtlEmrgCd4,
 * 							 SOReelectionIndi, RecpntEMail 
 * 							modify qryMotorVehicleFunctionTransaction(), 
 * 								insMotorVehicleFunctionTransaction()
 * 							defect 10366 Ver POS_640  
 * K Harrell 	06/14/2010 	add TransId to insert
 * 							modify insMotorVehicleFunctionTransaction()
 * 							defect 10505 Ver 6.5.0
 * K Harrell	06/15/2010	add EMailRenwlReqCd, remove reference to 
 * 							 SOReelectionIndi
 * 							modify qryMotorVehicleFunctionTransaction(),
 * 							  insMotorVehicleFunctionTransaction()
 * 							defect 10508 Ver 6.5.0 
 * K Harrell	10/03/2010	add TtlExmnIndi 
 * 							modify insMotorVehicleFunctionTransaction(),
 * 							  qryMotorVehicleFunctionTransaction()
 * 							defect 10013 Ver 6.6.0
 * K Harrell	01/05/2011	add VehMjrColorCd, VehMnrColorCd
 *  	 					modify insMotorVehicleFunctionTransaction(),
 * 							  qryMotorVehicleFunctionTransaction()
 * 							defect 10712 Ver 6.7.0 
 * K Harrell	10/25/2011	add ETtlPrntDate to SQL 
 *  	 					modify insMotorVehicleFunctionTransaction(),
 * 							  qryMotorVehicleFunctionTransaction()
 * 							defect 10841 Ver 6.9.0  
 * K Harrell	01/10/2012	add SurvShpRightsName1, SurvShpRightsName2,
 * 							 AddlSurvivorIndi, ExportIndi, SalvIndi
 * 							 to SQL
 * 							modify insMotorVehicleFunctionTransaction(),
 * 							  qryMotorVehicleFunctionTransaction()
 * 							defect 11231 Ver 6.10.0  
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to interact with database
 * (RTS_MV_FUNC_TRANS)
 *
 * @version	6.10.0 			01/10/2012
 * @author	Kathy Harrell
 * @since 					09/18/2001 17:04:08 
 */

public class MotorVehicleFunctionTransaction
	extends MotorVehicleFunctionTransactionData
{
	DatabaseAccess caDA;

	/**
	 * MotorVehicleFunctionTransaction constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public MotorVehicleFunctionTransaction(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Delete from RTS.RTS_MV_FUNC_TRANS
	 *
	 * @param  aaMVFuncTransData MotorVehicleFunctionTransactionData	
	 * @throws RTSException
	 */
	public void delMotorVehicleFunctionTransaction(MotorVehicleFunctionTransactionData aaMVFuncTransData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"delMotorVehicleFunctionTransaction - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_MV_FUNC_TRANS "
				+ "WHERE OfcIssuanceNo = ? and "
				+ "SubstaId = ? and "
				+ "TransAMDate = ? and "
				+ "TransWsId = ? and "
				+ "CustSeqNo = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getCustSeqNo())));
			if (aaMVFuncTransData.getTransTime() != 0)
			{
				lsDel = lsDel + " and TransTime = ? ";
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaMVFuncTransData.getTransTime())));
			}

			Log.write(
				Log.SQL,
				this,
				"delMotorVehicleFunctionTransaction - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"delMotorVehicleFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"delMotorVehicleFunctionTransaction - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delMotorVehicleFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Insert into RTS.RTS_MV_FUNC_TRANS
	 *
	 * @param  aaMVFuncTransData MotorVehicleFunctionTransactionData	
	 * @throws RTSException
	 */
	public void insMotorVehicleFunctionTransaction(MotorVehicleFunctionTransactionData aaMVFuncTransData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insMotorVehicleFunctionTransaction - Begin");

		Vector lvValues = new Vector();
		
		// defect 11231 
		// add SurvShpRightsName1, SurvShpRightsName2, AddlSurvivorIndi, 
		// ExportIndi, SalvIndi

		// defect 10841 
		// add ETtlPrntDate 
		
		// defect 10712 
		// add VehMjrColorCd, VehMnrColorCd 

		// defect 10013 
		// add TtlExmnIndi 

		// defect 10508
		// add EMailRenwlReqCd, remove SOReElectionIndi 

		// defect 10505 
		// add TransId 

		// defect 10366
		// add PvtLawEnfVehCd, NonTtlGolfCartCd, VTRTtlEmrgCd3, 
		//   VTRTtlEmrgCd4, S0ReelectionIndi, RecpntEMail 
		// delete VTRTtlEmrgCd1, VTRTtlEmrgCd2 

		String lsIns =
			"INSERT into RTS.RTS_MV_FUNC_TRANS ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "TransAMDate,"
				+ "TransWsId,"
				+ "CustSeqNo,"
				+ "TransTime,"
				+ "TransCd,"
				+ "TransId,"
				+ "MfDwnCd,"
				+ "VoidedTransIndi,"
				+ "CustActulRegFee,"
				+ "NotfyngCity,"
				+ "ResComptCntyNo,"
				+ "ReplicaVehMk,"
				+ "ReplicaVehModlYr,"
				+ "RegClassCd,"
				+ "RegEffDate,"
				+ "RegExpMo,"
				+ "RegExpYr,"
				+ "RegPltAge,"
				+ "RegPltCd,"
				+ "RegPltNo,"
				+ "RegPltOwnrName,"
				+ "RegRefAmt,"
				+ "RegStkrCd,"
				+ "RecpntName,"
				+ "RecpntLstName,"
				+ "RecpntFstName,"
				+ "RecpntMI,"
				+ "RenwlMailngSt1,"
				+ "RenwlMailngSt2,"
				+ "RenwlMailngCity,"
				+ "RenwlMailngState,"
				+ "RenwlMailngZPCd,"
				+ "RenwlMailngZPCdP4,"
				+ "BatchNo,"
				+ "BndedTtlCd,"
				+ "CCOIssueDate,"
				+ "DocNo,"
				+ "DocTypeCd,"
				+ "LegalRestrntNo,"
				+ "OthrStateCntry,"
				+ "OwnrshpEvidCd,"
				+ "PrevOwnrName,"
				+ "PrevOwnrCity,"
				+ "PrevOwnrState,"
				+ "TrlrType,"
				+ "TtlApplDate,"
				+ "TtlProcsCd,"
				+ "OldDocNo,"
				+ "TtlRejctnDate,"
				+ "TtlRejctnOfc,"
				+ "TtlIssueDate,"
				+ "AuditTrailTransId,"
				+ "SalesTaxDate,"
				+ "SalesTaxPdAmt,"
				+ "SalesTaxCat,"
				+ "TaxPdOthrState,"
				+ "TtlVehLocSt1,"
				+ "TtlVehLocSt2,"
				+ "TtlVehLocCity,"
				+ "TtlVehLocState,"
				+ "TtlVehLocZpCd,"
				+ "TtlVehLocZpCdP4,"
				+ "TireTypeCd,"
				+ "TradeInVehYr,"
				+ "TradeInVehMk,"
				+ "TradeInVehVin,"
				+ "VehBdyType,"
				+ "VehBdyVin,"
				+ "VehCaryngCap,"
				+ "VehClassCd,"
				+ "VehEmptyWt,"
				+ "VehGrossWt,"
				+ "VehLngth,"
				+ "VehMk,"
				+ "VehModl,"
				+ "VehModlYr,"
				+ "VehOdmtrBrnd,"
				+ "VehOdmtrReadng,"
				+ "VehSalesPrice,"
				+ "VehSoldDate,"
				+ "VehTon,"
				+ "VehTradeInAllownce,"
				+ "VehUnitNo,"
				+ "VIN,"
				+ "OwnrId,"
				+ "OwnrTtlName1,"
				+ "OwnrTtlName2,"
				+ "OwnrLstName,"
				+ "OwnrFstName,"
				+ "OwnrMI,"
				+ "OwnrSt1,"
				+ "OwnrSt2,"
				+ "OwnrCity,"
				+ "OwnrState,"
				+ "OwnrZpCd,"
				+ "OwnrZpCdP4,"
				+ "OwnrCntry,"
				+ "Lien1Date,"
				+ "LienHldr1Name1,"
				+ "LienHldr1Name2,"
				+ "LienHldr1St1,"
				+ "LienHldr1St2,"
				+ "LienHldr1City,"
				+ "LienHldr1State,"
				+ "LienHldr1ZpCd,"
				+ "LienHldr1ZpCdP4,"
				+ "LienHldr1Cntry,"
				+ "Lien2Date,"
				+ "LienHldr2Name1,"
				+ "LienHldr2Name2,"
				+ "LienHldr2St1,"
				+ "LienHldr2St2,"
				+ "LienHldr2City,"
				+ "LienHldr2State,"
				+ "LienHldr2ZpCd,"
				+ "LienHldr2ZpCdP4,"
				+ "LienHldr2Cntry,"
				+ "Lien3Date,"
				+ "LienHldr3Name1,"
				+ "LienHldr3Name2,"
				+ "LienHldr3St1,"
				+ "LienHldr3St2,"
				+ "LienHldr3City,"
				+ "LienHldr3State,"
				+ "LienHldr3ZpCd,"
				+ "LienHldr3ZpCdP4,"
				+ "LienHldr3Cntry,"
				+ "JnkCd,"
				+ "JnkDate,"
				+ "OthrGovtTtlNo,"
				+ "SalvYardNo,"
				+ "AuthCd,"
				+ "ComptCntyNo,"
				+ "DlrGDN,"
				+ "IMCNo,"
				+ "OwnrSuppliedPltNo,"
				+ "OwnrSuppliedExpYr,"
				+ "OwnrSuppliedStkrNo,"
				+ "SalesTaxExmptCd,"
				+ "SubconId,"
				+ "SubconIssueDate,"
				+ "SurrTtlDate,"
				+ "TtlNoMf,"
				+ "AddlLienRecrdIndi,"
				+ "AddlTradeInIndi,"
				+ "AgncyLoandIndi,"
				+ "DieselIndi,"
				+ "DOTStndrdsIndi,"
				+ "DPSSaftySuspnIndi,"
				+ "DPSStlnIndi,"
				+ "ExmptIndi,"
				+ "FloodDmgeIndi,"
				+ "FxdWtIndi,"
				+ "GovtOwndIndi,"
				+ "HvyVehUseTaxIndi,"
				+ "InspectnWaivedIndi,"
				+ "LienNotRlsedIndi,"
				+ "Lien2NotRlsedIndi,"
				+ "Lien3NotRlsedIndi,"
				+ "PltsSeizdIndi,"
				+ "PriorCCOIssueIndi,"
				+ "PrmtReqrdIndi,"
				+ "RecondCd,"
				+ "RecontIndi,"
				+ "RenwlMailRtrnIndi,"
				+ "RenwlYrMsmtchIndi,"
				+ "SpclPltProgIndi,"
				+ "StkrSeizdIndi,"
				+ "SurvshpRightsIndi,"
				+ "TtlRevkdIndi,"
				+ "VINErrIndi,"
				+ "FileTierCd,"
				+ "RegHotCkIndi,"
				+ "TtlHotCkIndi,"
				+ "RegInvldIndi,"
				+ "SalvStateCntry,"
				+ "EmissionSourceCd,"
				+ "ClaimComptCntyNo,"
				+ "TotalRebateAmt,"
				+ "EmissionSalesTax,"
				+ "HoopsRegPltNo,"
				+ "LemonLawIndi,"
				+ "PltBirthDate,"
				+ "VehValue,"
				+ "ChildSupportIndi,"
				+ "DissociateCd,"
				+ "V21PltId,"
				+ "V21VTNId,"
				+ "VTNSource,"
				+ "TtlSignDate,"
				+ "TtlTrnsfrEntCd,"
				+ "TtlTrnsfrPnltyExmptCd,"
				+ "ETtlCd,"
				+ "PrismLvlCd,"
				+ "PermLienhldrId1,"
				+ "PermLienhldrId2,"
				+ "PermLienhldrId3,"
				+ "LienRlseDate1,"
				+ "LienRlseDate2,"
				+ "LienRlseDate3,"
				+ "UTVMisLblIndi,"
				+ "VTRRegEmrgCd1,"
				+ "VTRRegEmrgCd2,"
				+ "PvtLawEnfVehCd,"
				+ "NonTtlGolfCartCd,"
				+ "VTRTtlEmrgCd3,"
				+ "VTRTtlEmrgCd4,"
				+ "EMailRenwlReqCd, "
				+ "RecpntEMail, "
				+ "TtlExmnIndi, "
				+ "VehMjrColorCd, "
				+ "VehMnrColorCd, "
				+ "ETtlPrntDate, "
				+ "SurvShpRightsName1, "
				+ "SurvShpRightsName2, "
				+ "AddlSurvivorIndi, "
				+ "ExportIndi,"
				+ "SalvIndi "
				+ " )"
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
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,?,?,?,?,?,?,?,?,?,?,"
				+ " ?,?,?,?,?,"
				+ "?,?,?,?,? )";
		// end defect 10366 
		// end defect 10505 
		// end defect 10508
		// end defect 10013  
		// end defect 10712 
		// end defect 10841 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOfcIssuanceNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSubstaId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTransAMDate())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTransWsId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getCustSeqNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTransTime())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTransCd())));

			// defect 10505 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaMVFuncTransData.getTransId()));
			// end defect 10505

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getMfDwnCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVoidedTransIndi())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getCustActulRegFee())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getNotfyngCity())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getResComptCntyNo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getReplicaVehMk())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getReplicaVehModlYr())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRegClassCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRegEffDate())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRegExpMo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRegExpYr())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRegPltAge())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRegPltCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRegPltNo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRegPltOwnrName())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRegRefAmt())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRegStkrCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRecpntName())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRecpntLstName())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRecpntFstName())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRecpntMI())));

			// defect 10112 
			// Use AddressData object 
			AddressData laRenwlAddr =
				aaMVFuncTransData.getRenewalAddrData();

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laRenwlAddr.getSt1())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laRenwlAddr.getSt2())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laRenwlAddr.getCity())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laRenwlAddr.getState())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laRenwlAddr.getZpcd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laRenwlAddr.getZpcdp4())));
			// end defect 10112 

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getBatchNo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getBndedTtlCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getCCOIssueDate())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getDocNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getDocTypeCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getLegalRestrntNo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOthrStateCntry())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOwnrshpEvidCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getPrevOwnrName())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getPrevOwnrCity())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getPrevOwnrState())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTrlrType())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTtlApplDate())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTtlProcsCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOldDocNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTtlRejctnDate())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTtlRejctnOfc())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTtlIssueDate())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getAuditTrailTransId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSalesTaxDate())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSalesTaxPdAmt())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSalesTaxCat())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTaxPdOthrState())));

			// defect 10112 			
			AddressData laVehLocAddr =
				aaMVFuncTransData.getVehLocAddrData();

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laVehLocAddr.getSt1())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laVehLocAddr.getSt2())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laVehLocAddr.getCity())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laVehLocAddr.getState())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laVehLocAddr.getZpcd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laVehLocAddr.getZpcdp4())));
			// end defect 10112 

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTireTypeCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTradeInVehYr())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTradeInVehMk())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTradeInVehVin())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehBdyType())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehBdyVin())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehCaryngCap())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehClassCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehEmptyWt())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehGrossWt())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehLngth())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehMk())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehModl())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehModlYr())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehOdmtrBrnd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehOdmtrReadng())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehSalesPrice())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehSoldDate())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehTon())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehTradeInAllownce())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehUnitNo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVIN())));

			OwnerData laOwnrData = aaMVFuncTransData.getOwnerData();

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laOwnrData.getOwnrId())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laOwnrData.getName1())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laOwnrData.getName2())));
			// end defect 10112  

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOwnrLstName())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOwnrFstName())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOwnrMI())));

			// defect 10112 
			AddressData laOwnrAddress = laOwnrData.getAddressData();

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laOwnrAddress.getSt1())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laOwnrAddress.getSt2())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laOwnrAddress.getCity())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laOwnrAddress.getState())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laOwnrAddress.getZpcd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laOwnrAddress.getZpcdp4())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						laOwnrAddress.getCntry())));

			for (int i = 1; i <= 3; i++)
			{
				LienholderData laLienData =
					aaMVFuncTransData.getLienholderData(new Integer(i));

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							laLienData.getLienDate())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laLienData.getName1())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laLienData.getName2())));

				AddressData laAddressData = laLienData.getAddressData();

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laAddressData.getSt1())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laAddressData.getSt2())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laAddressData.getCity())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laAddressData.getState())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laAddressData.getZpcd())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laAddressData.getZpcdp4())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							laAddressData.getCntry())));
			}

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getJnkCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getJnkDate())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOthrGovtTtlNo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSalvYardNo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getAuthCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getComptCntyNo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getDlrGDN())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getIMCNo())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOwnrSuppliedPltNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOwnrSuppliedExpYr())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOwnrSuppliedStkrNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSalesTaxExmptCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSubconId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSubconIssueDate())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSurrTtlDate())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTtlNoMf())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getAddlLienRecrdIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getAddlTradeInIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getAgncyLoandIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getDieselIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getDOTStndrdsIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getDPSSaftySuspnIndi())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getDPSStlnIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getExmptIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getFloodDmgeIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getFxdWtIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getGovtOwndIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getHvyVehUseTaxIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getInspectnWaivedIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getLienNotRlsedIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getLien2NotRlsedIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getLien3NotRlsedIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getPltsSeizdIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getPriorCCOIssueIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getPrmtReqrdIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRecondCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRecontIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRenwlMailRtrnIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRenwlYrMsmtchIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSpclPltProgIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getStkrSeizdIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSurvshpRightsIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTtlRevkdIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVINErrIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getFileTierCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRegHotCkIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTtlHotCkIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRegInvldIndi())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSalvStateCntry())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getEmissionSourceCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getClaimComptCntyNo())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTotalRebateAmt())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getEmissionSalesTax())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getHoopsRegPltNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getLemonLawIndi())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getPltBirthDate())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehValue())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getChildSupportIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getDissociateCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getV21PltId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getV21VTNId())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVTNSource())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTtlSignDate())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTtlTrnsfrEntCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTtlTrnsfrPnltyExmptCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getETtlCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getPrismLvlCd())));

			// defect 9969 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getPermLienHldrId1())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getPermLienHldrId2())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getPermLienHldrId3())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getLienRlseDate1())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getLienRlseDate2())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getLienRlseDate3())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getUTVMislblIndi())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVTRRegEmrgCd1())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVTRRegEmrgCd2())));

			// defect 10366
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getPvtLawEnfVehCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getNonTtlGolfCartCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVTRTtlEmrgCd3())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVTRTtlEmrgCd4())));

			// defect 10508
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getEMailRenwlReqCd())));
			// end defect 10508

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getRecpntEMail())));
			// end defect 10366 

			// defect 10013 
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTtlExmnIndi())));
			// end defect 10013 

			// defect 10712 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehMjrColorCd())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getVehMnrColorCd())));
			// end defect 10712 
			
			// defect 10841 
			lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaMVFuncTransData.getETtlPrntDate())));
			// end defect 10841
			
			// defect 11231 
			lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaMVFuncTransData.getSurvShpRightsName1())));
			lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaMVFuncTransData.getSurvShpRightsName2())));
			lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaMVFuncTransData.getAddlSurvivorIndi())));
			lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaMVFuncTransData.getExportIndi())));
			lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaMVFuncTransData.getSalvIndi())));
			// end defect 11231 

			Log.write(
				Log.SQL,
				this,
				"insMotorVehicleFunctionTransaction - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insMotorVehicleFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"insMotorVehicleFunctionTransaction - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insMotorVehicleFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_MV_FUNC_TRANS for Purge
	 *
	 * @param  aiPurgeAMDate int	
	 * @return int
	 * @throws RTSException
	 */
	public int purgeMotorVehicleFunctionTransaction(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeMotorVehicleFunctionTransaction - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			" DELETE FROM RTS.RTS_MV_FUNC_TRANS A "
				+ " WHERE  "
				+ " TRANSAMDATE <= ? ";

		try
		{
			// defect 8423
			// Used passed int
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));
			// end defect 8423			
			Log.write(
				Log.SQL,
				this,
				"purgeMotorVehicleFunctionTransaction - SQL - Begin");

			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeMotorVehicleFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeMotorVehicleFunctionTransaction - End");
			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeMotorVehicleFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query RTS.RTS_MV_FUNC_TRANS
	 *
	 * @param  aaMVFuncTransData MotorVehicleFunctionTransactionData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryMotorVehicleFunctionTransaction(MotorVehicleFunctionTransactionData aaMVFuncTransData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryMotorVehicleFunctionTransaction - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;
		// defect 11231 
		// add SurvShpRightsName1, SurvShpRightsName2, AddlSurvivorIndi, 
		// ExportIndi, SalvIndi
		
		// defect 10841 
		// add ETtlPrntDate 
		
		// defect 10712 
		// add VehMjrColorCd, VehMnrColorCd 

		// defect 10013 
		// add TtlExmnIndi 

		// defect 10508
		// add EMailRenwlReqCd, delete SOReelectionIndi

		// defect 10366  
		// add PvtLawEnfVehCd, NonTtlGolfCartCd, VTRTtlEmrgCd3, 
		// VTRTtlEmrgCd4, SOReelectionIndi, RecpntEMail 
		lsQry.append(
			"SELECT "
				+ "A.OfcIssuanceNo,"
				+ "B.OfcIssuanceCd,"
				+ "A.SubstaId,"
				+ "A.TransAMDate,"
				+ "A.TransWsId,"
				+ "A.CustSeqNo,"
				+ "A.TransTime,"
				+ "A.TransCd,"
				+ "C.TransEmpId,"
				+ "MfDwnCd,"
				+ "A.VoidedTransIndi,"
				+ "CustActulRegFee,"
				+ "NotfyngCity,"
				+ "ResComptCntyNo,"
				+ "ReplicaVehMk,"
				+ "ReplicaVehModlYr,"
				+ "A.RegClassCd,"
				+ "RegEffDate,"
				+ "RegExpMo,"
				+ "RegExpYr,"
				+ "RegPltAge,"
				+ "RegPltCd,"
				+ "A.RegPltNo,"
				+ "RegPltOwnrName,"
				+ "RegRefAmt,"
				+ "RegStkrCd,"
				+ "RecpntName,"
				+ "RecpntLstName,"
				+ "RecpntFstName,"
				+ "RecpntMI,"
				+ "RenwlMailngSt1,"
				+ "RenwlMailngSt2,"
				+ "RenwlMailngCity,"
				+ "RenwlMailngState,"
				+ "RenwlMailngZPCd,"
				+ "RenwlMailngZPCdP4,"
				+ "BatchNo,"
				+ "BndedTtlCd,"
				+ "CCOIssueDate,"
				+ "A.DocNo,"
				+ "DocTypeCd,"
				+ "LegalRestrntNo,"
				+ "OthrStateCntry,"
				+ "OwnrshpEvidCd,"
				+ "PrevOwnrName,"
				+ "PrevOwnrCity,"
				+ "PrevOwnrState,"
				+ "TrlrType,"
				+ "TtlApplDate,"
				+ "TtlProcsCd,"
				+ "OldDocNo,"
				+ "TtlRejctnDate,"
				+ "TtlRejctnOfc,"
				+ "TtlIssueDate,"
				+ "AuditTrailTransId,"
				+ "SalesTaxDate,"
				+ "SalesTaxPdAmt,"
				+ "SalesTaxCat,"
				+ "TaxPdOthrState,"
				+ "TtlVehLocSt1,"
				+ "TtlVehLocSt2,"
				+ "TtlVehLocCity,"
				+ "TtlVehLocState,"
				+ "TtlVehLocZpCd,"
				+ "TtlVehLocZpCdP4,"
				+ "A.TireTypeCd,"
				+ "TradeInVehYr,"
				+ "TradeInVehMk,"
				+ "TradeInVehVin,"
				+ "A.VehBdyType,"
				+ "VehBdyVin,"
				+ "A.VehCaryngCap,"
				+ "VehClassCd,"
				+ "A.VehEmptyWt,"
				+ "A.VehGrossWt,"
				+ "VehLngth,"
				+ "A.VehMk,"
				+ "A.VehModl,"
				+ "A.VehModlYr,"
				+ "VehOdmtrBrnd,"
				+ "VehOdmtrReadng,"
				+ "VehSalesPrice,"
				+ "VehSoldDate,"
				+ "VehTon,"
				+ "VehTradeInAllownce,"
				+ "A.VehUnitNo,"
				+ "A.VIN,"
				+ "A.OwnrId,"
				+ "OwnrTtlName1,"
				+ "OwnrTtlName2,"
				+ "OwnrLstName,"
				+ "OwnrFstName,"
				+ "OwnrMI,"
				+ "OwnrSt1,"
				+ "OwnrSt2,"
				+ "OwnrCity,"
				+ "OwnrState,"
				+ "OwnrZpCd,"
				+ "OwnrZpCdP4,"
				+ "OwnrCntry,"
				+ "Lien1Date,"
				+ "LienHldr1Name1,"
				+ "LienHldr1Name2,"
				+ "LienHldr1St1,"
				+ "LienHldr1St2,"
				+ "LienHldr1City,"
				+ "LienHldr1State,"
				+ "LienHldr1ZpCd,"
				+ "LienHldr1ZpCdP4,"
				+ "LienHldr1Cntry,"
				+ "Lien2Date,"
				+ "LienHldr2Name1,"
				+ "LienHldr2Name2,"
				+ "LienHldr2St1,"
				+ "LienHldr2St2,"
				+ "LienHldr2City,"
				+ "LienHldr2State,"
				+ "LienHldr2ZpCd,"
				+ "LienHldr2ZpCdP4,"
				+ "LienHldr2Cntry,"
				+ "Lien3Date,"
				+ "LienHldr3Name1,"
				+ "LienHldr3Name2,"
				+ "LienHldr3St1,"
				+ "LienHldr3St2,"
				+ "LienHldr3City,"
				+ "LienHldr3State,"
				+ "LienHldr3ZpCd,"
				+ "LienHldr3ZpCdP4,"
				+ "LienHldr3Cntry,"
				+ "JnkCd,"
				+ "JnkDate,"
				+ "OthrGovtTtlNo,"
				+ "SalvYardNo,"
				+ "AuthCd,"
				+ "ComptCntyNo,"
				+ "DlrGDN,"
				+ "IMCNo,"
				+ "OwnrSuppliedPltNo,"
				+ "OwnrSuppliedExpYr,"
				+ "OwnrSuppliedStkrNo,"
				+ "SalesTaxExmptCd,"
				+ "SubconId,"
				+ "SubconIssueDate,"
				+ "SurrTtlDate,"
				+ "TtlNoMf,"
				+ "AddlLienRecrdIndi,"
				+ "AddlTradeInIndi,"
				+ "AgncyLoandIndi,"
				+ "A.DieselIndi,"
				+ "DOTStndrdsIndi,"
				+ "DPSSaftySuspnIndi,"
				+ "DPSStlnIndi,"
				+ "ExmptIndi,"
				+ "FloodDmgeIndi,"
				+ "FxdWtIndi,"
				+ "GovtOwndIndi,"
				+ "HvyVehUseTaxIndi,"
				+ "InspectnWaivedIndi,"
				+ "LienNotRlsedIndi,"
				+ "Lien2NotRlsedIndi,"
				+ "Lien3NotRlsedIndi,"
				+ "PltsSeizdIndi,"
				+ "PriorCCOIssueIndi,"
				+ "PrmtReqrdIndi,"
				+ "RecondCd,"
				+ "RecontIndi,"
				+ "RenwlMailRtrnIndi,"
				+ "RenwlYrMsmtchIndi,"
				+ "SpclPltProgIndi,"
				+ "StkrSeizdIndi,"
				+ "SurvshpRightsIndi,"
				+ "TtlRevkdIndi,"
				+ "VINErrIndi,"
				+ "FileTierCd,"
				+ "RegHotCkIndi,"
				+ "TtlHotCkIndi,"
				+ "RegInvldIndi,"
				+ "SalvStateCntry,"
				+ "EmissionSourceCd,"
				+ "ClaimComptCntyNo,"
				+ "TotalRebateAmt,"
				+ "EmissionSalesTax,"
				+ "HoopsRegPltNo,"
				+ "LemonLawIndi,"
				+ "PltBirthDate,"
				+ "VehValue,"
				+ "ChildSupportIndi,"
				+ "DissociateCd,"
				+ "V21PltId,"
				+ "V21VTNId,"
				+ "VTNSource,"
				+ "TtlSignDate,"
				+ "TtlTrnsfrEntCd,"
				+ "TtlTrnsfrPnltyExmptCd,"
				+ "ETtlCd,"
				+ "PrismLvlCd, "
				+ "PermLienhldrId1, "
				+ "PermLienhldrId2, "
				+ "PermLienhldrId3, "
				+ "LienRlseDate1, "
				+ "LienRlseDate2, "
				+ "LienRlseDate3, "
				+ "UTVMislblIndi,"
				+ "VTRRegEmrgCd1,"
				+ "VTRRegEmrgCd2,"
				+ "PvtLawEnfVehCd,"
				+ "NonTtlGolfCartCd,"
				+ "VTRTtlEmrgCd3,"
				+ "VTRTtlEmrgCd4,"
				+ "EMailRenwlReqCd, "
				+ "RecpntEMail, "
				+ "TtlExmnIndi, "
				+ "VehMjrColorCd,"
				+ "VehMnrColorCd, "
				+ "ETtlPrntDate,"
				+ "SurvShpRightsName1, "
				+ "SurvShpRightsName2, "
				+ "AddlSurvivorIndi, "
				+ "ExportIndi,"
				+ "SalvIndi "
				+ "FROM "
				+ "RTS.RTS_MV_FUNC_TRANS A, "
				+ "RTS.RTS_TRANS_HDR B, "
				+ "RTS.RTS_TRANS C "
				+ "WHERE "
				+ "A.OfcIssuanceNo = ? and "
				+ "A.SubstaId = ? and "
				+ "A.TransAMDate = ? and "
				+ "A.TransWsId = ? and "
				+ "A.CustSeqNo = ? and "
				+ "A.TransTime = ? and "
				+ "A.OfcIssuanceno = B.OfcIssuanceNo and "
				+ "A.SubstaId = B.SubstaId and "
				+ "A.TransAMDate = B.TransAMDate and "
				+ "A.TransWsId = B.TransWsId and "
				+ "A.CustSeqNo = B.CustSeqNo and "
				+ "A.OfcIssuanceno = C.OfcIssuanceNo and "
				+ "A.SubstaId = C.SubstaId and "
				+ "A.TransAMDate = C.TransAMDate and "
				+ "A.TransWsId = C.TransWsId and "
				+ "A.CustSeqNo = C.CustSeqNo and "
				+ "A.TransTime = C.TransTime ");
		// end defect 10366 
		// end defect 10508 
		// end defect 10013 
		// end defect 10712 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTransTime())));

			Log.write(
				Log.SQL,
				this,
				" - qryMotorVehicleFunctionTransaction - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryMotorVehicleFunctionTransaction - SQL - End");

			while (lrsQry.next())
			{
				MotorVehicleFunctionTransactionData laMVFuncTransData =
					new MotorVehicleFunctionTransactionData();
				laMVFuncTransData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laMVFuncTransData.setOfcIssuanceCd(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceCd"));
				laMVFuncTransData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laMVFuncTransData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laMVFuncTransData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laMVFuncTransData.setCustSeqNo(
					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
				laMVFuncTransData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laMVFuncTransData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laMVFuncTransData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laMVFuncTransData.setMfDwnCd(
					caDA.getIntFromDB(lrsQry, "MfDwnCd"));
				laMVFuncTransData.setVoidedTransIndi(
					caDA.getIntFromDB(lrsQry, "VoidedTransIndi"));
				laMVFuncTransData.setCustActulRegFee(
					caDA.getDollarFromDB(lrsQry, "CustActulRegFee"));
				laMVFuncTransData.setNotfyngCity(
					caDA.getStringFromDB(lrsQry, "NotfyngCity"));
				laMVFuncTransData.setResComptCntyNo(
					caDA.getIntFromDB(lrsQry, "ResComptCntyNo"));
				laMVFuncTransData.setReplicaVehMk(
					caDA.getStringFromDB(lrsQry, "ReplicaVehMk"));
				laMVFuncTransData.setReplicaVehModlYr(
					caDA.getIntFromDB(lrsQry, "ReplicaVehModlYr"));
				laMVFuncTransData.setRegClassCd(
					caDA.getIntFromDB(lrsQry, "RegClassCd"));
				laMVFuncTransData.setRegEffDate(
					caDA.getIntFromDB(lrsQry, "RegEffDate"));
				laMVFuncTransData.setRegExpMo(
					caDA.getIntFromDB(lrsQry, "RegExpMo"));
				laMVFuncTransData.setRegExpYr(
					caDA.getIntFromDB(lrsQry, "RegExpYr"));
				laMVFuncTransData.setRegPltAge(
					caDA.getIntFromDB(lrsQry, "RegPltAge"));
				laMVFuncTransData.setRegPltCd(
					caDA.getStringFromDB(lrsQry, "RegPltCd"));
				laMVFuncTransData.setRegPltNo(
					caDA.getStringFromDB(lrsQry, "RegPltNo"));
				laMVFuncTransData.setRegPltOwnrName(
					caDA.getStringFromDB(lrsQry, "RegPltOwnrName"));
				laMVFuncTransData.setRegRefAmt(
					caDA.getDollarFromDB(lrsQry, "RegRefAmt"));
				laMVFuncTransData.setRegStkrCd(
					caDA.getStringFromDB(lrsQry, "RegStkrCd"));
				laMVFuncTransData.setRecpntLstName(
					caDA.getStringFromDB(lrsQry, "RecpntLstName"));
				laMVFuncTransData.setRecpntFstName(
					caDA.getStringFromDB(lrsQry, "RecpntFstName"));
				laMVFuncTransData.setRecpntMI(
					caDA.getStringFromDB(lrsQry, "RecpntMI"));

				laMVFuncTransData.setRecpntName(
					caDA.getStringFromDB(lrsQry, "RecpntName"));

				// defect 10112	
				AddressData laRenwlAddr =
					laMVFuncTransData.getRenewalAddrData();
				//laMVFuncTransData.setRenwlMailngSt1(
				laRenwlAddr.setSt1(
					caDA.getStringFromDB(lrsQry, "RenwlMailngSt1"));
				//laMVFuncTransData.setRenwlMailngSt2(
				laRenwlAddr.setSt2(
					caDA.getStringFromDB(lrsQry, "RenwlMailngSt2"));
				// laMVFuncTransData.setRenwlMailngCity(
				laRenwlAddr.setCity(
					caDA.getStringFromDB(lrsQry, "RenwlMailngCity"));
				//laMVFuncTransData.setRenwlMailngState(
				laRenwlAddr.setState(
					caDA.getStringFromDB(lrsQry, "RenwlMailngState"));
				// laMVFuncTransData.setRenwlMailngZPCd(
				laRenwlAddr.setZpcd(
					caDA.getStringFromDB(lrsQry, "RenwlMailngZPCd"));
				//laMVFuncTransData.setRenwlMailngZPCdP4(
				laRenwlAddr.setZpcdp4(
					caDA.getStringFromDB(lrsQry, "RenwlMailngZPCdP4"));
				// end defect 10112 

				laMVFuncTransData.setBatchNo(
					caDA.getStringFromDB(lrsQry, "BatchNo"));
				laMVFuncTransData.setBndedTtlCd(
					caDA.getStringFromDB(lrsQry, "BndedTtlCd"));
				laMVFuncTransData.setCCOIssueDate(
					caDA.getIntFromDB(lrsQry, "CCOIssueDate"));
				laMVFuncTransData.setDocNo(
					caDA.getStringFromDB(lrsQry, "DocNo"));
				laMVFuncTransData.setDocTypeCd(
					caDA.getIntFromDB(lrsQry, "DocTypeCd"));
				laMVFuncTransData.setLegalRestrntNo(
					caDA.getStringFromDB(lrsQry, "LegalRestrntNo"));
				laMVFuncTransData.setOthrStateCntry(
					caDA.getStringFromDB(lrsQry, "OthrStateCntry"));
				laMVFuncTransData.setOwnrshpEvidCd(
					caDA.getIntFromDB(lrsQry, "OwnrshpEvidCd"));
				laMVFuncTransData.setPrevOwnrName(
					caDA.getStringFromDB(lrsQry, "PrevOwnrName"));
				laMVFuncTransData.setPrevOwnrCity(
					caDA.getStringFromDB(lrsQry, "PrevOwnrCity"));
				laMVFuncTransData.setPrevOwnrState(
					caDA.getStringFromDB(lrsQry, "PrevOwnrState"));
				laMVFuncTransData.setTrlrType(
					caDA.getStringFromDB(lrsQry, "TrlrType"));
				laMVFuncTransData.setTtlApplDate(
					caDA.getIntFromDB(lrsQry, "TtlApplDate"));
				laMVFuncTransData.setTtlProcsCd(
					caDA.getStringFromDB(lrsQry, "TtlProcsCd"));
				laMVFuncTransData.setOldDocNo(
					caDA.getStringFromDB(lrsQry, "OldDocNo"));
				laMVFuncTransData.setTtlRejctnDate(
					caDA.getIntFromDB(lrsQry, "TtlRejctnDate"));
				laMVFuncTransData.setTtlRejctnOfc(
					caDA.getIntFromDB(lrsQry, "TtlRejctnOfc"));
				laMVFuncTransData.setTtlIssueDate(
					caDA.getIntFromDB(lrsQry, "TtlIssueDate"));
				laMVFuncTransData.setAuditTrailTransId(
					caDA.getStringFromDB(lrsQry, "AuditTrailTransId"));
				laMVFuncTransData.setSalesTaxDate(
					caDA.getIntFromDB(lrsQry, "SalesTaxDate"));
				laMVFuncTransData.setSalesTaxPdAmt(
					caDA.getDollarFromDB(lrsQry, "SalesTaxPdAmt"));
				laMVFuncTransData.setSalesTaxCat(
					caDA.getStringFromDB(lrsQry, "SalesTaxCat"));
				laMVFuncTransData.setTaxPdOthrState(
					caDA.getDollarFromDB(lrsQry, "TaxPdOthrState"));

				// defect 10112 
				AddressData laVehLocAddr =
					laMVFuncTransData.getVehLocAddrData();
				//laMVFuncTransData.setTtlVehLocSt1(
				laVehLocAddr.setSt1(
					caDA.getStringFromDB(lrsQry, "TtlVehLocSt1"));

				//laMVFuncTransData.setTtlVehLocSt2(
				laVehLocAddr.setSt2(
					caDA.getStringFromDB(lrsQry, "TtlVehLocSt2"));

				//laMVFuncTransData.setTtlVehLocCity(
				laVehLocAddr.setCity(
					caDA.getStringFromDB(lrsQry, "TtlVehLocCity"));

				// laMVFuncTransData.setTtlVehLocState(
				laVehLocAddr.setState(
					caDA.getStringFromDB(lrsQry, "TtlVehLocState"));

				// laMVFuncTransData.setTtlVehLocZpCd(
				laVehLocAddr.setZpcd(
					caDA.getStringFromDB(lrsQry, "TtlVehLocZpCd"));

				//laMVFuncTransData.setTtlVehLocZpCdP4(
				laVehLocAddr.setZpcdp4(
					caDA.getStringFromDB(lrsQry, "TtlVehLocZpCdP4"));
				// end defect 10112 

				laMVFuncTransData.setTireTypeCd(
					caDA.getStringFromDB(lrsQry, "TireTypeCd"));
				laMVFuncTransData.setTradeInVehYr(
					caDA.getIntFromDB(lrsQry, "TradeInVehYr"));
				laMVFuncTransData.setTradeInVehMk(
					caDA.getStringFromDB(lrsQry, "TradeInVehMk"));
				laMVFuncTransData.setTradeInVehVin(
					caDA.getStringFromDB(lrsQry, "TradeInVehVin"));
				laMVFuncTransData.setVehBdyType(
					caDA.getStringFromDB(lrsQry, "VehBdyType"));
				laMVFuncTransData.setVehBdyVin(
					caDA.getStringFromDB(lrsQry, "VehBdyVin"));
				laMVFuncTransData.setVehCaryngCap(
					caDA.getIntFromDB(lrsQry, "VehCaryngCap"));
				laMVFuncTransData.setVehClassCd(
					caDA.getStringFromDB(lrsQry, "VehClassCd"));
				laMVFuncTransData.setVehEmptyWt(
					caDA.getIntFromDB(lrsQry, "VehEmptyWt"));
				laMVFuncTransData.setVehGrossWt(
					caDA.getIntFromDB(lrsQry, "VehGrossWt"));
				laMVFuncTransData.setVehLngth(
					caDA.getIntFromDB(lrsQry, "VehLngth"));
				laMVFuncTransData.setVehMk(
					caDA.getStringFromDB(lrsQry, "VehMk"));
				laMVFuncTransData.setVehModl(
					caDA.getStringFromDB(lrsQry, "VehModl"));
				laMVFuncTransData.setVehModlYr(
					caDA.getIntFromDB(lrsQry, "VehModlYr"));
				laMVFuncTransData.setVehOdmtrBrnd(
					caDA.getStringFromDB(lrsQry, "VehOdmtrBrnd"));
				laMVFuncTransData.setVehOdmtrReadng(
					caDA.getStringFromDB(lrsQry, "VehOdmtrReadng"));
				laMVFuncTransData.setVehSalesPrice(
					caDA.getDollarFromDB(lrsQry, "VehSalesPrice"));
				laMVFuncTransData.setVehSoldDate(
					caDA.getIntFromDB(lrsQry, "VehSoldDate"));
				laMVFuncTransData.setVehTon(
					caDA.getDollarFromDB(lrsQry, "VehTon"));
				laMVFuncTransData.setVehTradeInAllownce(
					caDA.getDollarFromDB(lrsQry, "VehTradeInAllownce"));
				laMVFuncTransData.setVehUnitNo(
					caDA.getStringFromDB(lrsQry, "VehUnitNo"));
				laMVFuncTransData.setVIN(
					caDA.getStringFromDB(lrsQry, "VIN"));
				laMVFuncTransData.setOwnrLstName(
					caDA.getStringFromDB(lrsQry, "OwnrLstName"));
				laMVFuncTransData.setOwnrFstName(
					caDA.getStringFromDB(lrsQry, "OwnrFstName"));
				laMVFuncTransData.setOwnrMI(
					caDA.getStringFromDB(lrsQry, "OwnrMI"));

				// defect 10112 
				OwnerData laOwnrData = laMVFuncTransData.getOwnerData();

				//laMVFuncTransData.setOwnrId(
				laOwnrData.setOwnrId(
					caDA.getStringFromDB(lrsQry, "OwnrId"));

				//laMVFuncTransData.setOwnrTtlName1(
				laOwnrData.setName1(
					caDA.getStringFromDB(lrsQry, "OwnrTtlName1"));

				//laMVFuncTransData.setOwnrTtlName2(
				laOwnrData.setName2(
					caDA.getStringFromDB(lrsQry, "OwnrTtlName2"));

				AddressData laOwnrAddr = laOwnrData.getAddressData();

				//laMVFuncTransData.setOwnrSt1(
				laOwnrAddr.setSt1(
					caDA.getStringFromDB(lrsQry, "OwnrSt1"));

				//laMVFuncTransData.setOwnrSt2(
				laOwnrAddr.setSt2(
					caDA.getStringFromDB(lrsQry, "OwnrSt2"));

				//laMVFuncTransData.setOwnrCity(
				laOwnrAddr.setCity(
					caDA.getStringFromDB(lrsQry, "OwnrCity"));

				//laMVFuncTransData.setOwnrState(
				laOwnrAddr.setState(
					caDA.getStringFromDB(lrsQry, "OwnrState"));

				//laMVFuncTransData.setOwnrZpCd(
				laOwnrAddr.setZpcd(
					caDA.getStringFromDB(lrsQry, "OwnrZpCd"));

				//laMVFuncTransData.setOwnrZpCdP4(
				laOwnrAddr.setZpcdp4(
					caDA.getStringFromDB(lrsQry, "OwnrZpCdP4"));

				//laMVFuncTransData.setOwnrCntry(
				laOwnrAddr.setCntry(
					caDA.getStringFromDB(lrsQry, "OwnrCntry"));

				for (int i = 1; i <= 3; i++)
				{
					LienholderData laLien = new LienholderData();
					AddressData laAddr = laLien.getAddressData();
					laLien.setLienDate(
						caDA.getIntFromDB(lrsQry, "Lien" + i + "Date"));
					laLien.setName1(
						caDA.getStringFromDB(
							lrsQry,
							"LienHldr" + i + "Name1"));
					laLien.setName2(
						caDA.getStringFromDB(
							lrsQry,
							"LienHldr" + i + "Name2"));
					laAddr.setSt1(
						caDA.getStringFromDB(
							lrsQry,
							"LienHldr" + i + "St1"));
					laAddr.setSt2(
						caDA.getStringFromDB(
							lrsQry,
							"LienHldr" + i + "St2"));
					laAddr.setCity(
						caDA.getStringFromDB(
							lrsQry,
							"LienHldr" + i + "City"));
					laAddr.setState(
						caDA.getStringFromDB(
							lrsQry,
							"LienHldr" + i + "State"));
					laAddr.setZpcd(
						caDA.getStringFromDB(
							lrsQry,
							"LienHldr" + i + "ZpCd"));
					laAddr.setZpcdp4(
						caDA.getStringFromDB(
							lrsQry,
							"LienHldr" + i + "ZpCdP4"));
					laAddr.setCntry(
						caDA.getStringFromDB(
							lrsQry,
							"LienHldr" + i + "Cntry"));
					laMVFuncTransData.setLienholderData(
						new Integer(i),
						laLien);
				}
				// end defect 10112 

				laMVFuncTransData.setJnkCd(
					caDA.getIntFromDB(lrsQry, "JnkCd"));
				laMVFuncTransData.setJnkDate(
					caDA.getIntFromDB(lrsQry, "JnkDate"));
				laMVFuncTransData.setOthrGovtTtlNo(
					caDA.getStringFromDB(lrsQry, "OthrGovtTtlNo"));
				laMVFuncTransData.setSalvYardNo(
					caDA.getStringFromDB(lrsQry, "SalvYardNo"));
				laMVFuncTransData.setAuthCd(
					caDA.getStringFromDB(lrsQry, "AuthCd"));
				laMVFuncTransData.setComptCntyNo(
					caDA.getIntFromDB(lrsQry, "ComptCntyNo"));
				laMVFuncTransData.setDlrGDN(
					caDA.getStringFromDB(lrsQry, "DlrGDN"));
				laMVFuncTransData.setIMCNo(
					caDA.getDollarFromDB(lrsQry, "IMCNo"));
				laMVFuncTransData.setOwnrSuppliedPltNo(
					caDA.getStringFromDB(lrsQry, "OwnrSuppliedPltNo"));
				laMVFuncTransData.setOwnrSuppliedExpYr(
					caDA.getIntFromDB(lrsQry, "OwnrSuppliedExpYr"));
				laMVFuncTransData.setOwnrSuppliedStkrNo(
					caDA.getStringFromDB(lrsQry, "OwnrSuppliedStkrNo"));
				laMVFuncTransData.setSalesTaxExmptCd(
					caDA.getIntFromDB(lrsQry, "SalesTaxExmptCd"));
				laMVFuncTransData.setSubconId(
					caDA.getIntFromDB(lrsQry, "SubconId"));
				laMVFuncTransData.setSubconIssueDate(
					caDA.getIntFromDB(lrsQry, "SubconIssueDate"));
				laMVFuncTransData.setSurrTtlDate(
					caDA.getIntFromDB(lrsQry, "SurrTtlDate"));
				laMVFuncTransData.setTtlNoMf(
					caDA.getStringFromDB(lrsQry, "TtlNoMf"));
				laMVFuncTransData.setAddlLienRecrdIndi(
					caDA.getIntFromDB(lrsQry, "AddlLienRecrdIndi"));
				laMVFuncTransData.setAddlTradeInIndi(
					caDA.getIntFromDB(lrsQry, "AddlTradeInIndi"));
				laMVFuncTransData.setAgncyLoandIndi(
					caDA.getIntFromDB(lrsQry, "AgncyLoandIndi"));
				laMVFuncTransData.setDieselIndi(
					caDA.getIntFromDB(lrsQry, "DieselIndi"));
				laMVFuncTransData.setDOTStndrdsIndi(
					caDA.getIntFromDB(lrsQry, "DOTStndrdsIndi"));
				laMVFuncTransData.setDPSSaftySuspnIndi(
					caDA.getIntFromDB(lrsQry, "DPSSaftySuspnIndi"));
				laMVFuncTransData.setDPSStlnIndi(
					caDA.getIntFromDB(lrsQry, "DPSStlnIndi"));
				laMVFuncTransData.setExmptIndi(
					caDA.getIntFromDB(lrsQry, "ExmptIndi"));
				laMVFuncTransData.setFloodDmgeIndi(
					caDA.getIntFromDB(lrsQry, "FloodDmgeIndi"));
				laMVFuncTransData.setFxdWtIndi(
					caDA.getIntFromDB(lrsQry, "FxdWtIndi"));
				laMVFuncTransData.setGovtOwndIndi(
					caDA.getIntFromDB(lrsQry, "GovtOwndIndi"));
				laMVFuncTransData.setHvyVehUseTaxIndi(
					caDA.getIntFromDB(lrsQry, "HvyVehUseTaxIndi"));
				laMVFuncTransData.setInspectnWaivedIndi(
					caDA.getIntFromDB(lrsQry, "InspectnWaivedIndi"));
				laMVFuncTransData.setLienNotRlsedIndi(
					caDA.getIntFromDB(lrsQry, "LienNotRlsedIndi"));
				laMVFuncTransData.setLien2NotRlsedIndi(
					caDA.getIntFromDB(lrsQry, "Lien2NotRlsedIndi"));
				laMVFuncTransData.setLien3NotRlsedIndi(
					caDA.getIntFromDB(lrsQry, "Lien3NotRlsedIndi"));

				// defect 10246 
				// laMVFuncTransData.setPrivacyOptCd(
				// 	caDA.getIntFromDB(lrsQry, "PrivacyOptCd"));
				// end defect 10246 

				laMVFuncTransData.setPltsSeizdIndi(
					caDA.getIntFromDB(lrsQry, "PltsSeizdIndi"));
				laMVFuncTransData.setPriorCCOIssueIndi(
					caDA.getIntFromDB(lrsQry, "PriorCCOIssueIndi"));
				laMVFuncTransData.setPrmtReqrdIndi(
					caDA.getIntFromDB(lrsQry, "PrmtReqrdIndi"));
				laMVFuncTransData.setRecondCd(
					caDA.getIntFromDB(lrsQry, "RecondCd"));
				laMVFuncTransData.setRecontIndi(
					caDA.getIntFromDB(lrsQry, "RecontIndi"));
				laMVFuncTransData.setRenwlMailRtrnIndi(
					caDA.getIntFromDB(lrsQry, "RenwlMailRtrnIndi"));
				laMVFuncTransData.setRenwlYrMsmtchIndi(
					caDA.getIntFromDB(lrsQry, "RenwlYrMsmtchIndi"));
				laMVFuncTransData.setSpclPltProgIndi(
					caDA.getIntFromDB(lrsQry, "SpclPltProgIndi"));
				laMVFuncTransData.setStkrSeizdIndi(
					caDA.getIntFromDB(lrsQry, "StkrSeizdIndi"));
				laMVFuncTransData.setSurvshpRightsIndi(
					caDA.getIntFromDB(lrsQry, "SurvshpRightsIndi"));
				laMVFuncTransData.setTtlRevkdIndi(
					caDA.getIntFromDB(lrsQry, "TtlRevkdIndi"));
				laMVFuncTransData.setVINErrIndi(
					caDA.getIntFromDB(lrsQry, "VINErrIndi"));
				laMVFuncTransData.setFileTierCd(
					caDA.getIntFromDB(lrsQry, "FileTierCd"));
				laMVFuncTransData.setRegHotCkIndi(
					caDA.getIntFromDB(lrsQry, "RegHotCkIndi"));
				laMVFuncTransData.setTtlHotCkIndi(
					caDA.getIntFromDB(lrsQry, "TtlHotCkIndi"));
				laMVFuncTransData.setRegInvldIndi(
					caDA.getIntFromDB(lrsQry, "RegInvldIndi"));
				laMVFuncTransData.setSalvStateCntry(
					caDA.getStringFromDB(lrsQry, "SalvStateCntry"));
				laMVFuncTransData.setEmissionSourceCd(
					caDA.getStringFromDB(lrsQry, "EmissionSourceCd"));
				laMVFuncTransData.setClaimComptCntyNo(
					caDA.getIntFromDB(lrsQry, "ClaimComptCntyNo"));
				laMVFuncTransData.setTotalRebateAmt(
					caDA.getDollarFromDB(lrsQry, "TotalRebateAmt"));
				laMVFuncTransData.setEmissionSalesTax(
					caDA.getDollarFromDB(lrsQry, "EmissionSalesTax"));
				laMVFuncTransData.setHoopsRegPltNo(
					caDA.getStringFromDB(lrsQry, "HoopsRegPltNo"));
				laMVFuncTransData.setLemonLawIndi(
					caDA.getIntFromDB(lrsQry, "LemonLawIndi"));
				laMVFuncTransData.setPltBirthDate(
					caDA.getIntFromDB(lrsQry, "PltBirthDate"));
				laMVFuncTransData.setVehValue(
					caDA.getDollarFromDB(lrsQry, "VehValue"));
				laMVFuncTransData.setChildSupportIndi(
					caDA.getIntFromDB(lrsQry, "ChildSupportIndi"));
				laMVFuncTransData.setDissociateCd(
					caDA.getIntFromDB(lrsQry, "DissociateCd"));
				laMVFuncTransData.setV21PltId(
					caDA.getIntFromDB(lrsQry, "V21PltId"));
				laMVFuncTransData.setV21VTNId(
					caDA.getIntFromDB(lrsQry, "V21VTNId"));
				laMVFuncTransData.setVTNSource(
					caDA.getStringFromDB(lrsQry, "VTNSource"));
				laMVFuncTransData.setTtlSignDate(
					caDA.getIntFromDB(lrsQry, "TtlSignDate"));
				laMVFuncTransData.setTtlTrnsfrEntCd(
					caDA.getStringFromDB(lrsQry, "TtlTrnsfrEntCd"));
				laMVFuncTransData.setTtlTrnsfrPnltyExmptCd(
					caDA.getStringFromDB(
						lrsQry,
						"TtlTrnsfrPnltyExmptCd"));
				laMVFuncTransData.setETtlCd(
					caDA.getIntFromDB(lrsQry, "ETtlCd"));
				laMVFuncTransData.setPrismLvlCd(
					caDA.getStringFromDB(lrsQry, "PrismLvlCd"));

				// defect 9969 
				laMVFuncTransData.setPermLienHldrId1(
					caDA.getStringFromDB(lrsQry, "PermLienhldrId1"));
				laMVFuncTransData.setPermLienHldrId2(
					caDA.getStringFromDB(lrsQry, "PermLienhldrId2"));
				laMVFuncTransData.setPermLienHldrId3(
					caDA.getStringFromDB(lrsQry, "PermLienhldrId3"));
				laMVFuncTransData.setLienRlseDate1(
					caDA.getIntFromDB(lrsQry, "LienRlseDate1"));
				laMVFuncTransData.setLienRlseDate2(
					caDA.getIntFromDB(lrsQry, "LienRlseDate2"));
				laMVFuncTransData.setLienRlseDate3(
					caDA.getIntFromDB(lrsQry, "LienRlseDate3"));
				laMVFuncTransData.setUTVMislblIndi(
					caDA.getIntFromDB(lrsQry, "UTVMislblIndi"));
				laMVFuncTransData.setVTRRegEmrgCd1(
					caDA.getStringFromDB(lrsQry, "VTRRegEmrgCd1"));
				laMVFuncTransData.setVTRRegEmrgCd2(
					caDA.getStringFromDB(lrsQry, "VTRRegEmrgCd2"));

				// defect 10366 
				laMVFuncTransData.setPvtLawEnfVehCd(
					caDA.getStringFromDB(lrsQry, "PvtLawEnfVehCd"));
				laMVFuncTransData.setNonTtlGolfCartCd(
					caDA.getStringFromDB(lrsQry, "NonTtlGolfCartCd"));
				laMVFuncTransData.setVTRTtlEmrgCd3(
					caDA.getStringFromDB(lrsQry, "VTRTtlEmrgCd3"));
				laMVFuncTransData.setVTRTtlEmrgCd4(
					caDA.getStringFromDB(lrsQry, "VTRTtlEmrgCd4"));
				// defect 10508 
				laMVFuncTransData.setEMailRenwlReqCd(
					caDA.getIntFromDB(lrsQry, "EMailRenwlReqCd"));
				// end defect 10508 
				laMVFuncTransData.setRecpntEMail(
					caDA.getStringFromDB(lrsQry, "RecpntEMail"));
				// end defect 10366 

				// defect 10013  
				laMVFuncTransData.setTtlExmnIndi(
					caDA.getIntFromDB(lrsQry, "TtlExmnIndi"));
				// end defect 10013  

				// defect 10712 
				laMVFuncTransData.setVehMjrColorCd(
					caDA.getStringFromDB(lrsQry, "VehMjrColorCd"));
				laMVFuncTransData.setVehMnrColorCd(
					caDA.getStringFromDB(lrsQry, "VehMnrColorCd"));
				// end defect 10712
				
				// defect 10841
				laMVFuncTransData.setETtlPrntDate(
						caDA.getIntFromDB(lrsQry, "ETtlPrntDate"));
				// end defect 10841 
				
				// defect 11231 
				laMVFuncTransData.setSurvShpRightsName1(
						caDA.getStringFromDB(lrsQry, "SurvShpRightsName1"));
				laMVFuncTransData.setSurvShpRightsName2(
						caDA.getStringFromDB(lrsQry, "SurvShpRightsName2"));
				laMVFuncTransData.setAddlSurvivorIndi(
						caDA.getIntFromDB(lrsQry, "AddlSurvivorIndi"));
				laMVFuncTransData.setExportIndi(
						caDA.getIntFromDB(lrsQry, "ExportIndi"));
				laMVFuncTransData.setSalvIndi(
						caDA.getIntFromDB(lrsQry, "SalvIndi"));
				// end defect 11231 
				
				// Add element to the Vector
				lvRslt.addElement(laMVFuncTransData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryMotorVehicleFunctionTransaction - End ");
			return (lvRslt);
		}
		// defect 10366 
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"- qryMotorVehicleFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryMotorVehicleFunctionTransaction - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	//	/**
	//	 * Method to Query RTS.RTS_MV_FUNC_TRANS given TransAMDate, StartTime,
	//	 *   EndTime.  This method is not used in production - but by Disabled
	//	 *   Placard Stress Test. 
	//	 *
	//	 * @param  aiTransAMDate
	//	 * @param  aiStartTime
	//	 * @param  aiEndTime
	//	 * @return Vector
	//	 * @throws RTSException 
	//	 */
	//	public Vector qryMotorVehicleFunctionTransaction(
	//		int aiTransAMDate,
	//		int aiStartTime,
	//		int aiEndTime)
	//		throws RTSException
	//	{
	//		Log.write(
	//			Log.METHOD,
	//			this,
	//			" - qryMotorVehicleFunctionTransaction - Begin");
	//
	//		StringBuffer lsQry = new StringBuffer();
	//
	//		Vector lvRslt = new Vector();
	//
	//		Vector lvValues = new Vector();
	//
	//		ResultSet lrsQry;
	//
	//		lsQry.append(
	//			"SELECT "
	//				+ "A.OfcIssuanceNo,"
	//				+ "A.SubstaId,"
	//				+ "A.TransAMDate,"
	//				+ "A.TransWsId,"
	//				+ "A.CustSeqNo,"
	//				+ "A.TransTime,"
	//				+ "A.TransCd,"
	//				+ "OwnrTtlName1,"
	//				+ "OwnrTtlName2,"
	//				+ "OwnrSt1,"
	//				+ "OwnrSt2,"
	//				+ "OwnrCity,"
	//				+ "OwnrState,"
	//				+ "OwnrZpCd,"
	//				+ "OwnrZpCdP4,"
	//				+ "OwnrCntry, "
	//				+ "ResComptCntyNo "
	//				+ "FROM "
	//				+ "RTS.RTS_MV_FUNC_TRANS A "
	//				+ "WHERE TRANSAMDATE = ? AND TRANSTIME between ? "
	//				+ " and ? "
	//				+ " ORDER BY TRANSAMDATE,TRANSTIME ");
	//
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(aiTransAMDate)));
	//
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(aiStartTime)));
	//
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(aiEndTime)));
	//
	//		try
	//		{
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryMotorVehicleFunctionTransaction - SQL - Begin");
	//			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryMotorVehicleFunctionTransaction - SQL - End");
	//
	//			while (lrsQry.next())
	//			{
	//				MotorVehicleFunctionTransactionData laMVFuncTransData =
	//					new MotorVehicleFunctionTransactionData();
	//				laMVFuncTransData.setOfcIssuanceNo(
	//					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
	//				laMVFuncTransData.setSubstaId(
	//					caDA.getIntFromDB(lrsQry, "SubstaId"));
	//				laMVFuncTransData.setTransAMDate(
	//					caDA.getIntFromDB(lrsQry, "TransAMDate"));
	//				laMVFuncTransData.setTransWsId(
	//					caDA.getIntFromDB(lrsQry, "TransWsId"));
	//				laMVFuncTransData.setCustSeqNo(
	//					caDA.getIntFromDB(lrsQry, "CustSeqNo"));
	//				laMVFuncTransData.setTransTime(
	//					caDA.getIntFromDB(lrsQry, "TransTime"));
	//				laMVFuncTransData.setTransCd(
	//					caDA.getStringFromDB(lrsQry, "TransCd"));
	//
	//				// defect 10112 
	//				OwnerData laOwnrData = laMVFuncTransData.getOwnerData();
	//
	//				//laMVFuncTransData.setOwnrId(
	//				laOwnrData.setOwnrId(
	//					caDA.getStringFromDB(lrsQry, "OwnrId"));
	//
	//				//laMVFuncTransData.setOwnrTtlName1(
	//				laOwnrData.setName1(
	//					caDA.getStringFromDB(lrsQry, "OwnrTtlName1"));
	//
	//				//laMVFuncTransData.setOwnrTtlName2(
	//				laOwnrData.setName2(
	//					caDA.getStringFromDB(lrsQry, "OwnrTtlName2"));
	//
	//				AddressData laOwnrAddr = laOwnrData.getAddressData();
	//
	//				//laMVFuncTransData.setOwnrSt1(
	//				laOwnrAddr.setSt1(
	//					caDA.getStringFromDB(lrsQry, "OwnrSt1"));
	//
	//				//laMVFuncTransData.setOwnrSt2(
	//				laOwnrAddr.setSt2(
	//					caDA.getStringFromDB(lrsQry, "OwnrSt2"));
	//
	//				//laMVFuncTransData.setOwnrCity(
	//				laOwnrAddr.setCity(
	//					caDA.getStringFromDB(lrsQry, "OwnrCity"));
	//
	//				//laMVFuncTransData.setOwnrState(
	//				laOwnrAddr.setState(
	//					caDA.getStringFromDB(lrsQry, "OwnrState"));
	//
	//				//laMVFuncTransData.setOwnrZpCd(
	//				laOwnrAddr.setZpcd(
	//					caDA.getStringFromDB(lrsQry, "OwnrZpCd"));
	//
	//				//laMVFuncTransData.setOwnrZpCdP4(
	//				laOwnrAddr.setZpcdp4(
	//					caDA.getStringFromDB(lrsQry, "OwnrZpCdP4"));
	//
	//				//laMVFuncTransData.setOwnrCntry(
	//				laOwnrAddr.setCntry(
	//					caDA.getStringFromDB(lrsQry, "OwnrCntry"));
	//
	//				// end defect 10112 
	//
	//				laMVFuncTransData.setResComptCntyNo(
	//					caDA.getIntFromDB(lrsQry, "ResComptCntyNo"));
	//				// Add element to the Vector
	//				lvRslt.addElement(laMVFuncTransData);
	//			} //End of While
	//
	//			lrsQry.close();
	//			caDA.closeLastDBStatement();
	//			lrsQry = null;
	//			Log.write(
	//				Log.METHOD,
	//				this,
	//				" - qryMotorVehicleFunctionTransaction - End ");
	//			return (lvRslt);
	//		}
	//		catch (SQLException aeSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				" - qryMotorVehicleFunctionTransaction - Exception "
	//					+ aeSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				"qryMotorVehicleFunctionTransaction - Exception - "
	//					+ aeRTSEx.getMessage());
	//			throw aeRTSEx;
	//		}
	//	} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_MV_FUNC_TRANS for Void
	 * 
	 * @param aaMVFuncTransData	MotorVehicleFunctionTransactionData
	 * @throws RTSException 
	 */
	public void voidMotorVehicleFunctionTransaction(MotorVehicleFunctionTransactionData aaMVFuncTransData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"voidMotorVehicleFunctionTransaction - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_MV_FUNC_TRANS SET "
				+ "VoidedTransIndi = 1 "
				+ "WHERE OfcIssuanceNo = ? and "
				+ "SubstaId = ? and TransAMDate = ? "
				+ "and TransWsId = ? and CustSeqNo = ? and TransTime = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getCustSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaMVFuncTransData.getTransTime())));
			Log.write(
				Log.SQL,
				this,
				"voidMotorVehicleFunctionTransaction - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"voidMotorVehicleFunctionTransaction - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"voidMotorVehicleFunctionTransaction - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"voidMotorVehicleFunctionTransaction - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
