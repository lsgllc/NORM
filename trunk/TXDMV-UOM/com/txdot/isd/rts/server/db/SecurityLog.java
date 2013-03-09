package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.SecurityLogData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * SecurityLogData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell  	10/16/2001  Removed DeleteIndi,ChngTimestmp
 * K Harrell   	10/18/2001  Altered delSecurityLog to purgeSecurityLog
 * K Harrell    01/11/2002  Removed PltToOwnrAccs, DelPltToOwnrAccs, 
 *                          RejctnAccs,FundsAckAccs,InvAdjAccs
 *                          Added ItrntRenwlAccs, RegnlColltnAccs
 * K Harrell    01/24/2002  Ordered the qry
 * R Hicks      07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * Ray Rowehl	09/22/2003	Added support for new UserName
 *							modified insSecurityLog, qrySecurityLog()
 *							defect 6445 Ver 5.1.6
 * K Harrell	01/23/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add references to ReprntStkrRptAccs
 * 							Ver 5.2.0	
 * K Harrell	04/02/2004	remove references to QuickCntrAccs, 
 *							QuickCntrRptAccs
 *							modify insSecurityLog(),qrySecurityLog()
 *							defect 6955  Ver 5.2.0 
 * Min Wang 	07/08/2004	Add RSPSUpdtAccs for RSPS status update
 *							modify insSecurityLog(), qrySecurityLog(), 
 *							defect 7310 Ver 5.2.1
 * K Harrell	03/04/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	07/27/2005	Really remove all references to
 *							QuickCntrAccs,QuickCntrRptAccs
 *							modify qrySecurityLog() 
 *							defect 7878 Ver 5.2.2 Fix 6
 * K Harrell	11/11/2005	Accept passed int vs. 
 * 							SecurityLogData Object.
 * 							modify purgeSecurityLog() 
 * 							defect 8423 Ver 5.2.3
 * K Harrell	09/25/2006	Add new SecurityLog columns, 
 * 							ExmptAuditRptAccs, ExmptAuthAccs
 * 							modify insSecurityLog(),qrySecurityLog()
 * 						 	defect 8900 Ver Exempts 
 * K Harrell	02/05/2007	add new Security columns SpclPltApplAccs,
 * 							SpclPltRenwPltAccs,SpclPltRevisePltAccs,
 * 							SpclPltResrvPltAccs,SpclPltUnAccptblPltAccs, 
 *							SpclPltDelPltAccs,SpclPltRptsAccs
 * 							modify insSecurityLog(),qrySecurityLog()
 * 						 	defect 9085 Ver Special Plates
 * K Harrell	02/13/2007	add new column to calls SpclPltAccs
 * 							modify insSecurityLog(),qrySecurityLog()
 * 							defect 9085 ver Special Plates 
 * M Reyes		04/25/2007	added SpclPltAccs
 * 							modify qrySecurityLog()
 * 							defect 9124 ver Special Plates
 * K Harrell	09/10/2008	Update for DlrRptAccs, LienHldrRptAccs, 
 * 							 SubconRptAccs
 * 							modify qrySecurityLog(), insSecurityLog() 
 * 							  updSecurityLog()
 * 							defect 9710 Ver Defect_POS_B
 * K Harrell	10/27/2008	Update for DsabldPlcrdRptAccs, 
 * 								DsabldPlcrdInqAccs    
 * 							modify qrySecurityLog(), insSecurityLog()
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeSecurityLog()
 * 							defect 9825 Ver Defect_POS_D  
 * B Hargrove	03/09/2009	Update for ETtlRptAccs 
 * 							modify qrySecurityLog(), insSecurityLog()
 * 							defect 9960 Ver Defect_POS_E 
 * Min Wang		08/07/2009	Add Private Law Enforcement Vehicle.
 * 							modify insSecurity(), qrySecurity(),
 * 							defect 10153 Ver Defect_POS_F 
 * Min Wang		01/05/2011	Add Web Agent.
 * 							modify insSecurity(), qrySecurity(),
 * 							defect 10717 Ver POS_670
 * Min Wang		01/05/2011	Add Web Agent.
 * 							modify insSecurity(), qrySecurity(),
 * 							defect 10717 Ver POS_670
 * Min Wang		01/14/2011	modify insSecurity(), qrySecurity(),
 * 							defect 10717 Ver POS_670
 * K Harrell	01/20/2011	add BatchRptMgmtAccs to SQL 
 * 							modify insSecurityLog(), qrySecurityLog()
 * 							defect 10701 Ver 6.7.0 
 * K Harrell	05/30/2011	add ModfyTimedPrmtAccs to SQL 
 * 							modify insSecurityLog(), qrySecurityLog()
 * 							defect 10844 Ver 6.8.0
 * K Harrell	01/11/2012	add ExportAccs,DsabldPlcrdReinstateAccs 
 * 							 to SQL 
 * 							modify insSecurityLog(), qrySecurityLog()
 * 							defect 11231 Ver 6.10.0  
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to interact with database
 *  
 * @version	6.10.0 			01/11/2012
 * @author	Kathy Harrell
 * <br>Creation Date:		09/17/2001
 */
public class SecurityLog extends SecurityLogData
{
	DatabaseAccess caDA;

	/**
	 * Constructor
	 */
	public SecurityLog(DatabaseAccess aaDA) throws RTSException
	{
		super();
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_SECURITY_LOG 
	 * 
	 * @param  aaSecurityLogData  SecurityLogData	
	 * @throws RTSException 	
	 */
	public void insSecurityLog(SecurityLogData aaSecurityLogData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insSecurityLog - Begin");
		Vector lvValues = new Vector();

		// defect 9831 
		// add DsabldPlcrdRptAccs, DsabldPlcrdInqAccs 

		// defect 9710 
		// add DlrRptAccs, LienHldrRptAccs, SubconRptAccs 

		// defect 9960 
		// add ETtlRptAccs 

		// defect 10153
		// add PrivateLawEnfVehAccs

		// defect 10717
		// add WebAgntAccs

		// defect 10701 
		// add BatchRptMgmtAccs 

		// defect 10844 
		// add ModfyTimedPrmtAccs 
		
		// defect 11231 
		// add ExportAccs, DsabldPlcrdReinstateAccs  
		String lsIns =
			"INSERT into RTS.RTS_SECURITY_LOG ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "EmpId,"
				+ "UserName,"
				+ "EmpLastName,"
				+ "EmpFirstName,"
				+ "EmpMI,"
				+ "TransWsId,"
				+ "TransAMDate,"
				+ "TransTime,"
				+ "UpdtngEmpId,"
				+ "UpdtActn,"
				+ "AdminAccs,"
				+ "AcctAccs,"
				+ "AddrChngAccs,"
				+ "AdjSalesTaxAccs,"
				+ "BatchRptMgmtAccs,"
				+ "BndedTtlCdAccs,"
				+ "CancRegAccs,"
				+ "CashOperAccs,"
				+ "CCOAccs,"
				+ "CntyRptsAccs,"
				+ "COAAccs,"
				+ "CorrTtlRejAccs,"
				+ "CustServAccs,"
				+ "DelTtlInProcsAccs,"
				+ "DlrAccs,"
				+ "DlrTtlAccs,"
				+ "DsabldPersnAccs,"
				+ "DuplAccs,"
				+ "EmpSecrtyAccs,"
				+ "EmpSecrtyRptAccs,"
				+ "ExchAccs,"
				+ "ExmptAuditRptAccs,"
				+ "ExmptAuthAccs,"
				+ "FundsAdjAccs,"
				+ "FundsBalAccs,"
				+ "FundsInqAccs,"
				+ "FundsMgmtAccs,"
				+ "FundsRemitAccs,"
				+ "HotCkCrdtAccs,"
				+ "HotCkRedemdAccs,"
				+ "InqAccs,"
				+ "InvAccs,"
				+ "InvAckAccs,"
				+ "InvActionAccs,"
				+ "InvAllocAccs,"
				+ "InvDelAccs,"
				+ "InvHldRlseAccs,"
				+ "InvInqAccs,"
				+ "InvProfileAccs,"
				+ "ItmSeizdAccs,"
				+ "ItrntRenwlAccs,"
				+ "IssueDrvsEdAccs,"
				+ "JnkAccs,"
				+ "LegalRestrntNoAccs,"
				+ "LienHldrAccs,"
				+ "LocalOptionsAccs,"
				+ "MailRtrnAccs,"
				+ "MiscRegAccs,"
				+ "MiscRemksAccs,"
				+ "ModfyAccs,"
				+ "ModfyHotCkAccs,"
				+ "ModfyTimedPrmtAccs,"
				+ "NonResPrmtAccs,"
				+ "PltNoAccs,"
				+ "RefAccs,"
				+ "RegnlColltnAccs,"
				+ "RegOnlyAccs,"
				+ "RegRefAmtAccs,"
				+ "RenwlAccs,"
				+ "ReplAccs,"
				+ "ReprntRcptAccs,"
				+ "ReprntRptAccs,"
				+ "RgstrByAccs,"
				+ "RptsAccs,"
				+ "RSPSUpdtAccs,"
				+ "SalvAccs,"
				+ "SecrtyAccs,"
				+ "SpclPltAccs,"
				+ "SpclPltApplAccs,"
				+ "SpclPltRenwPltAccs,"
				+ "SpclPltRevisePltAccs,"
				+ "SpclPltResrvPltAccs,"
				+ "SpclPltUnAccptblPltAccs,"
				+ "SpclPltDelPltAccs,"
				+ "SpclPltRptsAccs,"
				+ "PrntImmedAccs,"
				+ "StatusChngAccs,"
				+ "StlnSRSAccs,"
				+ "SubconAccs,"
				+ "SubconRenwlAccs,"
				+ "TempAddlWtAccs,"
				+ "TimedPrmtAccs,"
				+ "TowTrkAccs,"
				+ "TtlApplAccs,"
				+ "TtlRegAccs,"
				+ "TtlRevkdAccs,"
				+ "TtlSurrAccs,"
				+ "VoidTransAccs,"
				+ "CrdtCardFeeAccs,"
				+ "DlrRptAccs,"
				+ "LienHldrRptAccs,"
				+ "SubconRptAccs,"
				+ "ReprntStkrRptAccs,"
				+ "DsabldPlcrdRptAccs,"
				+ "DsabldPlcrdInqAccs,"
				+ "ETtlRptAccs,"
				+ "PrivateLawEnfVehAccs,"
				+ "WebAgntAccs,"
				+ "ExportAccs,"
				+ "DsabldPlcrdReinstateAccs,"
				+ "DeleteIndi,"
				+ "ChngTimestmp) VALUES ( ";

		// end defect 9960 
		// end defect 9710 
		// end defect 9831 
		// end defect 10135
		// end defect 10717
		// end defect 10701 
		

		String lsAdd = new String();
		
		//for (int i = 0; i < 110; i++)		
		for (int i = 0; i < 112; i++)
		{
			lsAdd = lsAdd + "?,";
		}
		lsIns = lsIns + lsAdd + " 0," + " Current Timestamp)";

		// end defect 10844 
		// end defect 11231 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSubstaId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getEmpId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getUserName())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getEmpLastName())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getEmpFirstName())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getEmpMI())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getTransTime())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getUpdtngEmpId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getUpdtActn())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getAdminAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getAcctAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getAddrChngAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getAdjSalesTaxAccs())));

			// defect 10701 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getBatchRptMgmtAccs())));
			// end defect 10701 

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getBndedTtlCdAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getCancRegAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getCashOperAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getCCOAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getCntyRptsAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getCOAAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getCorrTtlRejAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getCustServAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getDelTtlInProcsAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getDlrAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getDlrTtlAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getDsabldPersnAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getDuplAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getEmpSecrtyAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getEmpSecrtyRptAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getExchAccs())));
			// defect 8900			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getExmptAuditRptAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getExmptAuthAccs())));
			// end defect 8900
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getFundsAdjAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getFundsBalAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getFundsInqAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getFundsMgmtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getFundsRemitAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getHotCkCrdtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getHotCkRedemdAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getInqAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getInvAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getInvAckAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getInvActionAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getInvAllocAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getInvDelAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getInvHldRlseAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getInvInqAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getInvProfileAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getItmSeizdAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getItrntRenwlAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getIssueDrvsEdAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getJnkAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getLegalRestrntNoAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getLienHldrAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getLocalOptionsAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getMailRtrnAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getMiscRegAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getMiscRemksAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getModfyAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getModfyHotCkAccs())));

			// defect 10844 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getModfyTimedPrmtAccs())));
			// end defect 10844 

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getNonResPrmtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getPltNoAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getRefAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getRegnlColltnAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getRegOnlyAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getRegRefAmtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getRenwlAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getReplAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getReprntRcptAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getReprntRptAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getRgstrByAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getRptsAccs())));
			//defect 7310
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getRSPSUpdtAccs())));
			//end defect 7310
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSalvAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSecrtyAccs())));

			// defect 9085 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSpclPltAccs())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSpclPltApplAccs())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSpclPltRenwPltAccs())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSpclPltRevisePltAccs())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSpclPltResrvPltAccs())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData
							.getSpclPltUnAccptblPltAccs())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSpclPltDelPltAccs())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSpclPltRptsAccs())));

			// end defect 9085

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getPrntImmedAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getStatusChngAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getStlnSRSAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSubconAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSubconRenwlAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getTempAddlWtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getTimedPrmtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getTowTrkAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getTtlApplAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getTtlRegAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getTtlRevkdAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getTtlSurrAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getVoidTransAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getCrdtCardFeeAccs())));

			// defect 9710 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getDlrRptAccs())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getLienHldrRptAccs())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getSubconRptAccs())));
			// end defect 9710 

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getReprntStkrRptAccs())));

			// defect 9831 			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getDsabldPlcrdRptAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getDsabldPlcrdInqAccs())));
			// end defect 9831 

			// defect 9960 			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getETtlRptAccs())));
			// end defect 9960 

			// defect 10153			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getPrivateLawEnfVehAccs())));
			// end defect 10153 

			// defect 10717			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getWebAgntAccs())));
			// end defect 10717 
			
			// defect 11231 			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityLogData.getExportAccs())));
			lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityLogData.getDsabldPlcrdReInstateAccs())));
			// end defect 11231
			
			Log.write(Log.SQL, this, "insSecurityLog - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, "insSecurityLog - SQL - End");
			Log.write(Log.METHOD, this, "insSecurityLog - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insSecurityLog - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	* NO UPDATE METHOD
	*/
	/**
	* Method to Delete from RTS.RTS_SECURITY_LOG for Purge
	* 
	* @param  aiPurgeAMDate int	
	* @return int
	* @throws RTSException 	
	*/
	public int purgeSecurityLog(int aiPurgeAMDate) throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeSecurityLog - Begin");
		Vector lvValues = new Vector();
		String lsDel =
			"DELETE FROM RTS.RTS_SECURITY_LOG WHERE TRANSAMDATE <= ? ";
		try
		{
			// defect 8423
			// use passed int 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiPurgeAMDate)));
			// end defect 8423

			Log.write(Log.SQL, this, "purgeSecurityLog - SQL - Begin");

			// defect 9825 
			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "purgeSecurityLog - SQL - End");
			Log.write(Log.METHOD, this, "purgeSecurityLog - End");
			return liNumRows;
			// end defect 9825 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeSecurityLog - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	* Method to Query RTS.RTS_SECURITY_LOG 
	* 
	* @param  aaSecurityLogData SecurityLogData	
	* @return Vector 	
	*/
	public Vector qrySecurityLog(SecurityLogData aSecurityLogData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qrySecurityLog - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet lrsQry;

		// defect 9831 
		// add DsabldPlcrdRptAccs, DsabldPlcrdInqAccs 
		// defect 9960 
		// add ETtlRptAccs 
		// defect 9960 
		// defect 10153
		// add PrivateLawEnfVehAccs 
		// defect 10717
		// add WebAgntAccs
		// defect 10701 
		// add BatchRptMgmtAccs
		// defect 10844 
		// add ModfyTimedPrmtAccs
		
		// defect 11231 
		// add ExportAccs 
		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "UserName,"
				+ "EmpId,"
				+ "EmpLastName,"
				+ "EmpFirstName,"
				+ "EmpMI,"
				+ "TransWsId,"
				+ "TransAMDate,"
				+ "TransTime,"
				+ "UpdtngEmpId,"
				+ "UpdtActn,"
				+ "AdminAccs,"
				+ "AcctAccs,"
				+ "AddrChngAccs,"
				+ "AdjSalesTaxAccs,"
				+ "BndedTtlCdAccs,"
				+ "BatchRptMgmtAccs,"
				+ "CancRegAccs,"
				+ "CashOperAccs,"
				+ "CCOAccs,"
				+ "CntyRptsAccs,"
				+ "COAAccs,"
				+ "CorrTtlRejAccs,"
				+ "CustServAccs,"
				+ "DelTtlInProcsAccs,"
				+ "DlrAccs,"
				+ "DlrTtlAccs,"
				+ "DsabldPersnAccs,"
				+ "DuplAccs,"
				+ "EmpSecrtyAccs,"
				+ "EmpSecrtyRptAccs,"
				+ "ExchAccs,"
				+ "ExmptAuditRptAccs,"
				+ "ExmptAuthAccs,"
				+ "FundsAdjAccs,"
				+ "FundsBalAccs,"
				+ "FundsInqAccs,"
				+ "FundsMgmtAccs,"
				+ "FundsRemitAccs,"
				+ "HotCkCrdtAccs,"
				+ "HotCkRedemdAccs,"
				+ "InqAccs,"
				+ "InvAccs,"
				+ "InvAckAccs,"
				+ "InvActionAccs,"
				+ "InvAllocAccs,"
				+ "InvDelAccs,"
				+ "InvHldRlseAccs,"
				+ "InvInqAccs,"
				+ "InvProfileAccs,"
				+ "ItmSeizdAccs,"
				+ "ItrntRenwlAccs,"
				+ "IssueDrvsEdAccs, "
				+ "JnkAccs,"
				+ "LegalRestrntNoAccs,"
				+ "LienHldrAccs,"
				+ "LocalOptionsAccs,"
				+ "MailRtrnAccs,"
				+ "MiscRegAccs,"
				+ "MiscRemksAccs,"
				+ "ModfyAccs,"
				+ "ModfyHotCkAccs,"
				+ "ModfyTimedPrmtAccs,"
				+ "NonResPrmtAccs,"
				+ "PltNoAccs,"
				+ "RefAccs,"
				+ "RegnlColltnAccs,"
				+ "RegOnlyAccs,"
				+ "RegRefAmtAccs,"
				+ "RenwlAccs,"
				+ "ReplAccs,"
				+ "ReprntRcptAccs,"
				+ "ReprntRptAccs,"
				+ "ReprntStkrRptAccs,"
				+ "RgstrByAccs,"
				+ "RptsAccs,"
				+ "RSPSUpdtAccs,"
				+ "SalvAccs,"
				+ "SecrtyAccs,"
				+ "SpclPltAccs,"
				+ "SpclPltApplAccs,"
				+ "SpclPltRenwPltAccs,"
				+ "SpclPltRevisePltAccs,"
				+ "SpclPltResrvPltAccs,"
				+ "SpclPltUnAccptblPltAccs,"
				+ "SpclPltDelPltAccs,"
				+ "SpclPltRptsAccs,"
				+ "PrntImmedAccs,"
				+ "StatusChngAccs,"
				+ "StlnSRSAccs,"
				+ "SubconAccs,"
				+ "SubconRenwlAccs,"
				+ "TempAddlWtAccs,"
				+ "TimedPrmtAccs,"
				+ "TowTrkAccs,"
				+ "TtlApplAccs,"
				+ "TtlRegAccs,"
				+ "TtlRevkdAccs,"
				+ "TtlSurrAccs,"
				+ "VoidTransAccs, "
				+ "DlrRptAccs,"
				+ "LienHldrRptAccs,"
				+ "SubconRptAccs,"
				+ "DsabldPlcrdRptAccs,"
				+ "DsabldPlcrdInqAccs,"
				+ "CrdtCardFeeAccs,"
				+ "ETtlRptAccs,"
				+ "PrivateLawEnfVehAccs,"
				+ "WebAgntAccs, "
				+ "ExportAccs, "
				+ "DsabldPlcrdReinstateAccs "
				+ "FROM RTS.RTS_SECURITY_LOG WHERE OfcIssuanceNo = ? "
				+ " and SubstaId = ? ORDER BY EMPID, TRANSAMDATE,"
				+ " TRANSTIME ");
		// end defect 9960 
		// end defect 9701 
		// end defect 9831 
		// end defect 10153
		// end defect 10717
		// end defect 10701
		// end defect 10844 
		// end defect 11231 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aSecurityLogData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aSecurityLogData.getSubstaId())));
			Log.write(Log.SQL, this, " - qrySecurityLog - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qrySecurityLog - SQL - End");
			while (lrsQry.next())
			{
				SecurityLogData laSecurityLogData =
					new SecurityLogData();
				laSecurityLogData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laSecurityLogData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laSecurityLogData.setUserName(
					caDA.getStringFromDB(lrsQry, "UserName"));
				laSecurityLogData.setEmpId(
					caDA.getStringFromDB(lrsQry, "EmpId"));
				laSecurityLogData.setEmpLastName(
					caDA.getStringFromDB(lrsQry, "EmpLastName"));
				laSecurityLogData.setEmpFirstName(
					caDA.getStringFromDB(lrsQry, "EmpFirstName"));
				laSecurityLogData.setEmpMI(
					caDA.getStringFromDB(lrsQry, "EmpMI"));
				laSecurityLogData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laSecurityLogData.setTransAMDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laSecurityLogData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laSecurityLogData.setUpdtngEmpId(
					caDA.getStringFromDB(lrsQry, "UpdtngEmpId"));
				laSecurityLogData.setUpdtActn(
					caDA.getStringFromDB(lrsQry, "UpdtActn"));
				laSecurityLogData.setAdminAccs(
					caDA.getIntFromDB(lrsQry, "AdminAccs"));
				laSecurityLogData.setAcctAccs(
					caDA.getIntFromDB(lrsQry, "AcctAccs"));
				laSecurityLogData.setAddrChngAccs(
					caDA.getIntFromDB(lrsQry, "AddrChngAccs"));
				laSecurityLogData.setAdjSalesTaxAccs(
					caDA.getIntFromDB(lrsQry, "AdjSalesTaxAccs"));
				// defect 10701 
				laSecurityLogData.setBatchRptMgmtAccs(
					caDA.getIntFromDB(lrsQry, "BatchRptMgmtAccs"));
				// end defect 10701 
				laSecurityLogData.setBndedTtlCdAccs(
					caDA.getIntFromDB(lrsQry, "BndedTtlCdAccs"));
				laSecurityLogData.setCancRegAccs(
					caDA.getIntFromDB(lrsQry, "CancRegAccs"));
				laSecurityLogData.setCashOperAccs(
					caDA.getIntFromDB(lrsQry, "CashOperAccs"));
				laSecurityLogData.setCCOAccs(
					caDA.getIntFromDB(lrsQry, "CCOAccs"));
				laSecurityLogData.setCntyRptsAccs(
					caDA.getIntFromDB(lrsQry, "CntyRptsAccs"));
				laSecurityLogData.setCOAAccs(
					caDA.getIntFromDB(lrsQry, "COAAccs"));
				laSecurityLogData.setCorrTtlRejAccs(
					caDA.getIntFromDB(lrsQry, "CorrTtlRejAccs"));
				laSecurityLogData.setCustServAccs(
					caDA.getIntFromDB(lrsQry, "CustServAccs"));
				laSecurityLogData.setDelTtlInProcsAccs(
					caDA.getIntFromDB(lrsQry, "DelTtlInProcsAccs"));
				laSecurityLogData.setDlrAccs(
					caDA.getIntFromDB(lrsQry, "DlrAccs"));
				laSecurityLogData.setDlrTtlAccs(
					caDA.getIntFromDB(lrsQry, "DlrTtlAccs"));
				laSecurityLogData.setDsabldPersnAccs(
					caDA.getIntFromDB(lrsQry, "DsabldPersnAccs"));
				laSecurityLogData.setDuplAccs(
					caDA.getIntFromDB(lrsQry, "DuplAccs"));
				laSecurityLogData.setEmpSecrtyAccs(
					caDA.getIntFromDB(lrsQry, "EmpSecrtyAccs"));
				laSecurityLogData.setEmpSecrtyRptAccs(
					caDA.getIntFromDB(lrsQry, "EmpSecrtyRptAccs"));
				laSecurityLogData.setExchAccs(
					caDA.getIntFromDB(lrsQry, "ExchAccs"));
				// defect 8900 	
				laSecurityLogData.setExmptAuditRptAccs(
					caDA.getIntFromDB(lrsQry, "ExmptAuditRptAccs"));
				laSecurityLogData.setExmptAuthAccs(
					caDA.getIntFromDB(lrsQry, "ExmptAuthAccs"));
				// end defect 8900
				laSecurityLogData.setFundsAdjAccs(
					caDA.getIntFromDB(lrsQry, "FundsAdjAccs"));
				laSecurityLogData.setFundsBalAccs(
					caDA.getIntFromDB(lrsQry, "FundsBalAccs"));
				laSecurityLogData.setFundsInqAccs(
					caDA.getIntFromDB(lrsQry, "FundsInqAccs"));
				laSecurityLogData.setFundsMgmtAccs(
					caDA.getIntFromDB(lrsQry, "FundsMgmtAccs"));
				laSecurityLogData.setFundsRemitAccs(
					caDA.getIntFromDB(lrsQry, "FundsRemitAccs"));
				laSecurityLogData.setHotCkCrdtAccs(
					caDA.getIntFromDB(lrsQry, "HotCkCrdtAccs"));
				laSecurityLogData.setHotCkRedemdAccs(
					caDA.getIntFromDB(lrsQry, "HotCkRedemdAccs"));
				laSecurityLogData.setInqAccs(
					caDA.getIntFromDB(lrsQry, "InqAccs"));
				laSecurityLogData.setInvAccs(
					caDA.getIntFromDB(lrsQry, "InvAccs"));
				laSecurityLogData.setInvAckAccs(
					caDA.getIntFromDB(lrsQry, "InvAckAccs"));
				laSecurityLogData.setInvActionAccs(
					caDA.getIntFromDB(lrsQry, "InvActionAccs"));
				laSecurityLogData.setInvAllocAccs(
					caDA.getIntFromDB(lrsQry, "InvAllocAccs"));
				laSecurityLogData.setInvDelAccs(
					caDA.getIntFromDB(lrsQry, "InvDelAccs"));
				laSecurityLogData.setInvHldRlseAccs(
					caDA.getIntFromDB(lrsQry, "InvHldRlseAccs"));
				laSecurityLogData.setInvInqAccs(
					caDA.getIntFromDB(lrsQry, "InvInqAccs"));
				laSecurityLogData.setInvProfileAccs(
					caDA.getIntFromDB(lrsQry, "InvProfileAccs"));
				laSecurityLogData.setItmSeizdAccs(
					caDA.getIntFromDB(lrsQry, "ItmSeizdAccs"));
				laSecurityLogData.setItrntRenwlAccs(
					caDA.getIntFromDB(lrsQry, "ItrntRenwlAccs"));
				laSecurityLogData.setIssueDrvsEdAccs(
					caDA.getIntFromDB(lrsQry, "IssueDrvsEdAccs"));
				laSecurityLogData.setJnkAccs(
					caDA.getIntFromDB(lrsQry, "JnkAccs"));
				laSecurityLogData.setLegalRestrntNoAccs(
					caDA.getIntFromDB(lrsQry, "LegalRestrntNoAccs"));
				laSecurityLogData.setLienHldrAccs(
					caDA.getIntFromDB(lrsQry, "LienHldrAccs"));
				laSecurityLogData.setLocalOptionsAccs(
					caDA.getIntFromDB(lrsQry, "LocalOptionsAccs"));
				laSecurityLogData.setMailRtrnAccs(
					caDA.getIntFromDB(lrsQry, "MailRtrnAccs"));
				laSecurityLogData.setMiscRegAccs(
					caDA.getIntFromDB(lrsQry, "MiscRegAccs"));
				laSecurityLogData.setMiscRemksAccs(
					caDA.getIntFromDB(lrsQry, "MiscRemksAccs"));
				laSecurityLogData.setModfyAccs(
					caDA.getIntFromDB(lrsQry, "ModfyAccs"));
				laSecurityLogData.setModfyHotCkAccs(
					caDA.getIntFromDB(lrsQry, "ModfyHotCkAccs"));
				// defect 10844 
				laSecurityLogData.setModfyTimedPrmtAccs(
					caDA.getIntFromDB(lrsQry, "ModfyTimedPrmtAccs"));
				// end defect 10844
				laSecurityLogData.setNonResPrmtAccs(
					caDA.getIntFromDB(lrsQry, "NonResPrmtAccs"));
				laSecurityLogData.setPltNoAccs(
					caDA.getIntFromDB(lrsQry, "PltNoAccs"));
				laSecurityLogData.setRefAccs(
					caDA.getIntFromDB(lrsQry, "RefAccs"));
				laSecurityLogData.setRegnlColltnAccs(
					caDA.getIntFromDB(lrsQry, "RegnlColltnAccs"));
				laSecurityLogData.setRegOnlyAccs(
					caDA.getIntFromDB(lrsQry, "RegOnlyAccs"));
				laSecurityLogData.setRegRefAmtAccs(
					caDA.getIntFromDB(lrsQry, "RegRefAmtAccs"));
				laSecurityLogData.setRenwlAccs(
					caDA.getIntFromDB(lrsQry, "RenwlAccs"));
				laSecurityLogData.setReplAccs(
					caDA.getIntFromDB(lrsQry, "ReplAccs"));
				laSecurityLogData.setReprntRcptAccs(
					caDA.getIntFromDB(lrsQry, "ReprntRcptAccs"));
				laSecurityLogData.setReprntRptAccs(
					caDA.getIntFromDB(lrsQry, "ReprntRptAccs"));
				laSecurityLogData.setRgstrByAccs(
					caDA.getIntFromDB(lrsQry, "RgstrByAccs"));
				laSecurityLogData.setRptsAccs(
					caDA.getIntFromDB(lrsQry, "RptsAccs"));
				//defect 7310
				laSecurityLogData.setRSPSUpdtAccs(
					caDA.getIntFromDB(lrsQry, "RSPSUpdtAccs"));
				//end defect 7310
				laSecurityLogData.setSalvAccs(
					caDA.getIntFromDB(lrsQry, "SalvAccs"));
				laSecurityLogData.setSecrtyAccs(
					caDA.getIntFromDB(lrsQry, "SecrtyAccs"));
				laSecurityLogData.setSpclPltAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltAccs"));
				laSecurityLogData.setSpclPltApplAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltApplAccs"));
				laSecurityLogData.setSpclPltRenwPltAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltRenwPltAccs"));
				laSecurityLogData.setSpclPltRevisePltAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltRevisePltAccs"));
				laSecurityLogData.setSpclPltResrvPltAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltResrvPltAccs"));
				laSecurityLogData.setSpclPltUnAccptblPltAccs(
					caDA.getIntFromDB(
						lrsQry,
						"SpclPltUnAccptblPltAccs"));
				laSecurityLogData.setSpclPltDelPltAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltDelPltAccs"));
				laSecurityLogData.setSpclPltRptsAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltRptsAccs"));

				laSecurityLogData.setPrntImmedAccs(
					caDA.getIntFromDB(lrsQry, "PrntImmedAccs"));
				laSecurityLogData.setStatusChngAccs(
					caDA.getIntFromDB(lrsQry, "StatusChngAccs"));
				laSecurityLogData.setStlnSRSAccs(
					caDA.getIntFromDB(lrsQry, "StlnSRSAccs"));
				laSecurityLogData.setSubconAccs(
					caDA.getIntFromDB(lrsQry, "SubconAccs"));
				laSecurityLogData.setSubconRenwlAccs(
					caDA.getIntFromDB(lrsQry, "SubconRenwlAccs"));
				laSecurityLogData.setTempAddlWtAccs(
					caDA.getIntFromDB(lrsQry, "TempAddlWtAccs"));
				laSecurityLogData.setTimedPrmtAccs(
					caDA.getIntFromDB(lrsQry, "TimedPrmtAccs"));
				laSecurityLogData.setTowTrkAccs(
					caDA.getIntFromDB(lrsQry, "TowTrkAccs"));
				laSecurityLogData.setTtlApplAccs(
					caDA.getIntFromDB(lrsQry, "TtlApplAccs"));
				laSecurityLogData.setTtlRegAccs(
					caDA.getIntFromDB(lrsQry, "TtlRegAccs"));
				laSecurityLogData.setTtlRevkdAccs(
					caDA.getIntFromDB(lrsQry, "TtlRevkdAccs"));
				laSecurityLogData.setTtlSurrAccs(
					caDA.getIntFromDB(lrsQry, "TtlSurrAccs"));
				laSecurityLogData.setVoidTransAccs(
					caDA.getIntFromDB(lrsQry, "VoidTransAccs"));
				laSecurityLogData.setCrdtCardFeeAccs(
					caDA.getIntFromDB(lrsQry, "CrdtCardFeeAccs"));

				// defect 9710 
				laSecurityLogData.setDlrRptAccs(
					caDA.getIntFromDB(lrsQry, "DlrRptAccs"));
				laSecurityLogData.setLienHldrRptAccs(
					caDA.getIntFromDB(lrsQry, "LienHldrRptAccs"));
				laSecurityLogData.setSubconRptAccs(
					caDA.getIntFromDB(lrsQry, "SubconRptAccs"));
				// end defect 9710 

				laSecurityLogData.setReprntStkrRptAccs(
					caDA.getIntFromDB(lrsQry, "ReprntStkrRptAccs"));

				// defect 9831 
				laSecurityLogData.setDsabldPlcrdRptAccs(
					caDA.getIntFromDB(lrsQry, "DsabldPlcrdRptAccs"));
				laSecurityLogData.setDsabldPlcrdInqAccs(
					caDA.getIntFromDB(lrsQry, "DsabldPlcrdInqAccs"));
				// end defect 9831 

				// defect 9960 
				laSecurityLogData.setETtlRptAccs(
					caDA.getIntFromDB(lrsQry, "ETtlRptAccs"));
				// end defect 9960 

				// defect 10153
				laSecurityLogData.setPrivateLawEnfVehAccs(
					caDA.getIntFromDB(lrsQry, "PrivateLawEnfVehAccs"));
				// end defect 10153

				// defect 10717
				laSecurityLogData.setWebAgntAccs(
					caDA.getIntFromDB(lrsQry, "WebAgntAccs"));
				// end defect 10717
				
				// defect 11231
				laSecurityLogData.setExportAccs(
					caDA.getIntFromDB(lrsQry, "ExportAccs"));
				laSecurityLogData.setDsabldPlcrdReInstateAccs(
						caDA.getIntFromDB(lrsQry, "DsabldPlcrdReInstateAccs"));
				// end defect 11231

				// Add element to the Vector
				lvRslt.addElement(laSecurityLogData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qrySecurityLog - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySecurityLog - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySecurityLog - Exception "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD
} //END OF CLASS
