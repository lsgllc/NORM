package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.SecurityData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.localoptions.JniAdInterface;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * Security.java 
 *  
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	02/07/2002  Added order by to qryTransSecurity
 * K Harrell   	02/20/2002  Altered to preserve ReqSubstaid
 * K Harrell   	03/19/2002  Altered for AdminCache
 * R Hicks 		07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * K Harrell   	08/17/2002  Added purgeSecurity: CQU100004601
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * Ray Rowehl	02/10/2003	Add false to Password convertToString calls
 *							CQU100004735
 * Ray Rowehl	09/22/2003	Add handling for new AdUserId field
 *							Do not allow update for AdUserId
 *							Change handling of insert  and update to
 *							correctly work with EmpId and AdUserId.
 *							modified insSecurity(),	qrySecurity(),
 *							updSecurity()
 *							add delSecurity(SecurityData, boolean)
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	12/01/2003	Rename AdUserId to SysUserId
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	12/18/2003	Rename SysUserId to UserName
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	01/23/2003	Rename table column SysUserId to UserName
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/03/2004	Clean up documentation for delSecurity
 *							modify delSecurity(SecurityData, boolean)
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/09/2004	Do not use EmpId on search if we are
 *							using UserName.
 *							Do not write to AdminCache if it is
 *							a real delete.
 *							User UserName only for XP deletes
 *							since administrator can change EmpId.
 *							modify qrySecurity(),
 *							delSecurity(SecurityData),
 *							delSecurity(SecurityData, boolean)
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/12/2004	trim fields for search before using.  Do not
 *							allow spaces to effect key selection.
 *							modify qrySecurity()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/17/2004	Reformat to match updated Java Standards.
 * 							Change dba calls to be static as needed
 * 							Format code.
 * 							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/18/2004	Allow for prefix when checking for length
 *							of UserName to determine if it will be used
 *							in where clauses.
 *							modify delSecurity(SecurityData),
 *							qrySecurity(), updSecurity() 
 *							defect 6445 Ver 5.1.6
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Ver 5.2.0.
 * K Harrell	04/02/2004	remove references to password, etc.
 *							QuickCntrAccs, QuickCntrRptAccs
 *							modify insSecurity(),qrySecurity()
 *							defect 6955  Ver 5.2.0
 * Min Wang 	07/08/2004	Add RSPSUpdtAccs for RSPS status update
 *							modify insSecurity(), qrySecurity(), 
 *							updSecurity()
 *							defect 7310 Ver 5.2.1
 * Min Wang		11/16/2004	Improve employee delete for non-publishing.
 *							modify qrySecurity()
 *							defect 7462 Ver 5.2.2
 * K Harrell	03/04/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/19/2005  services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3
 * K Harrell	07/25/2005	Really remove all references to
 *							QuickCntrAccs,QuickCntrRptAccs
 *							modify qrySecurity() 
 *							defect 7878 Ver 5.2.2 Fix 6
 * K Harrell	09/25/2006	Add new Security columns, ExmptAuditRptAccs
 * 							ExmptAuthAccs
 * 							modify insSecurity(),qrySecurity(),
 * 							 updSecurity()
 * 						 	defect 8900 Ver Exempts
 * K Harrell	02/05/2007	add new Security columns SpclPltApplAccs,
 * 							SpclPltRenwPltAccs,SpclPltRevisePltAccs,
 * 							SpclPltResrvPltAccs,SpclPltUnAccptblPltAccs, 
 *							SpclPltDelPltAccs,SpclPltRptsAccs
 * 							modify insSecurity(),qrySecurity(),
 * 							 updSecurity()
 * 						 	defect 9085 Ver Special Plates
 * K Harrell	02/13/2007	add new column to calls SpclPltAccs
 * 							modify insSecurity(),qrySecurity(),
 * 							 updSecurity()
 * 							defect 9085 ver Special Plates   
 * B Hargrove	03/02/2007	Fixing typo : SpclPltApplAccs entered twice
 * 							instead of SpclPltAccs - SpclPltApplAccs. 
 * 							modify updSecurity()
 * 							defect 9085 ver Special Plates
 * K Harrell	08/28/2008	No longer update Admin Cache from this class
 * 							delete updAdminCache()
 * 							modify delSecurity(SecurityData),
 * 							 insSecurity(), updSecurity()
 * 							defect 8721 Ver Defect_POS_B  
 * K Harrell	09/10/2008	Update for DlrRptAccs, LienHldrRptAccs, 
 * 							 SubconRptAccs
 * 							modify qrySecurity(), insSecurity(), 
 * 							  updSecurity()
 * 							defect 9710 Ver Defect_POS_B
 * K Harrell	10/27/2008	Update for DsabldPlcrdRptAccs, 
 * 							 DsabldPlcrdInqAccs   
 * 							modify qrySecurity(), insSecurity(),
 * 							  updSecurity()
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeSecurity()
 * 							defect 9825 Ver Defect_POS_D   
 * B Hargrove	03/09/2009	Update for ETtlRptAccs 
 * 							modify qrySecurity(), insSecurity(),
 * 							  updSecurity()
 * 							defect 9960 Ver Defect_POS_E
 * Min Wang		08/07/2009	Add Private Law Enforcement Vehicle.
 * 							modify insSecurity(), QrySecurity(),
 * 							updSecurity()
 * 							defect 10153 Ver Defect_POS_F 
 * Min Wang		01/05/2011	Add Web Agent
 * 							modify insSecurity(), QrySecurity(),
 * 							updSecurity()
 * 							defect 10717 Ver POS_670
 * Min Wang		01/18/2011	modify insSecurity(), qrySecurity(),
 * 							updSecurity()
 * 							defect 10717 Ver POS_670
 * K Harrell	01/20/2011	add BatchRptMgmtAccs to SQL 
 * 							modify insSecurity(), qrySecurity(),
 * 							updSecurity()
 * 							defect 10701 Ver 6.7.0
 * K Harrell	05/30/2011	add ModfyTimedPrmtAccs to SQL 
 * 							modify insSecurity(), qrySecurity(),
 * 							updSecurity()
 * 							defect 10844 Ver 6.8.0
 * K Harrell	01/11/2012	add ExportAccs, DsabldPlcrdReinstateAccs
 * 							  to SQL 
 * 							modify insSecurity(), qrySecurity(),
 * 							updSecurity()
 * 							defect 11231 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to interact with database 
 *
 * @version	6.10.0			01/11/2012
 * @author	Kathy Harrell
 * <br>Creation Date:  		09/06/2001 19:00:49
 */

public class Security extends SecurityData
{
	DatabaseAccess caDA;

	/**
	 * Constructor
	 *
	 * @param  aaDA
	 * @throws RTSException
	 */
	public Security(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Update RTS.RTS_SECURITY for logical delete
	 * 
	 * @param  aaSecurityData  SecurityData	
	 * @throws RTSException
	 */
	public void delSecurity(SecurityData aaSecurityData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delSecurity - Begin");

		Vector lvValues = new Vector();

		int liSubstaId = qrySecuritySubstaId(aaSecurityData);
		// defect 6445
		// delete by UserName id it is available
		String lsDel =
			"UPDATE RTS.RTS_SECURITY SET DeleteIndi = 1, ChngTimestmp ="
				+ " Current Timestamp "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = "
				+ liSubstaId;
		// make this section conditional
		//+ " AND EmpId = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getOfcIssuanceNo())));
			// make this part conditional
			//lvValues.addElement(
			//    new DBValue(Types.CHAR, caDA.convertToString
			//   (aaSecurityData.getEmpId())));
			// remove this code and use the following if.
			// lvValues.addElement(
			// new DBValue(Types.CHAR, caDA.convertToString
			//   (aaSecurityData.getEmpId())));
			// Use UserName if it is not null or empty
			// have to check to see if the user is an rtsuser first.
			if (aaSecurityData.getUserName() != null
				&& ((aaSecurityData.getUserName().trim().length() > 4
					&& JniAdInterface.isRtsUserName(
						aaSecurityData.getUserName(),
						aaSecurityData.getOfcIssuanceNo())
					|| ((aaSecurityData.getUserName().trim().length() > 0
						&& !JniAdInterface.isRtsUserName(
							aaSecurityData.getUserName(),
							aaSecurityData.getOfcIssuanceNo()))))))
			{
				lsDel = lsDel + " and UserName = ?";
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSecurityData.getUserName())));
			}
			// use EmpId only if there is no UserName
			else
			{
				lsDel = lsDel + " and EmpId = ?";
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSecurityData.getEmpId())));
			}
			// end defect 6445
			Log.write(Log.SQL, this, "delSecurity - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "delSecurity - SQL - End");
			Log.write(Log.METHOD, this, "delSecurity - End");
			// defect 8721
			// updAdminCache(aaSecurityData, liSubstaId);
			// end defect 8721 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delSecurity - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Clean out RTS_SECURITY Row to allow for insert.
	 * <p>Field to use for delete is determined by the abUserName 
	 * boolean.
	 * <ul>
	 * <li>true - use the UserName for delete
	 * <li>false - use the EmpId for delete
	 * <eul>
	 *
	 * @param  aaSecurityData  SecurityData
	 * @param  abUserName boolean  - indicator for key for delete.	
	 * @throws RTSException 
	 */
	public void delSecurity(
		SecurityData aaSecurityData,
		boolean abUserName)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delSecurity - Begin");

		Vector lvValues = new Vector();

		int liSubstaId = qrySecuritySubstaId(aaSecurityData);

		try
		{
			String lsDel =
				"DELETE FROM RTS.RTS_SECURITY "
					+ "WHERE "
					+ "OfcIssuanceNo = ? AND "
					+ "SubstaId = "
					+ liSubstaId;

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getOfcIssuanceNo())));

			if (abUserName)
			{
				// use the username to do the delete
				lsDel = lsDel + " AND UserName = ? ";
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSecurityData.getUserName())));
			}
			else
			{
				// use empid to do the delete
				lsDel = lsDel + " AND EmpId = ? ";
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSecurityData.getEmpId())));
			}
			Log.write(Log.SQL, this, "delSecurity - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "delSecurity - SQL - End");
			Log.write(Log.METHOD, this, "delSecurity - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delSecurity - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Insert into RTS.RTS_SECURITY
	 *
	 * @param  aaSecurityData  SecurityData	
	 * @throws RTSException
	 */
	public void insSecurity(SecurityData aaSecurityData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insSecurity - Begin");
		Log.write(Log.METHOD, this, "insSecurity - First try Update");

		// First, try update
		int liNumRows = updSecurity(aaSecurityData);
		if (liNumRows == 0)
		{
			Log.write(
				Log.METHOD,
				this,
				"insSecurity - Next try Insert");

			int liSubstaId = qrySecuritySubstaId(aaSecurityData);

			// defect 11231 
			// add ExportAccs, DsabldPlcrdReinstateAccs  
			
			// defect 9831
			// add DsabldPlcrdRptAccs, DsabldPlcrdInqAccs 

			// defect 9710
			// add DlrRptAccs, LienHldrRptAccs, SubconRptAccs

			// defect 9960
			// add ETtlRptAccs  

			// defect 10153
			// add PrvateiLawEnfVehAccs

			// defect 10717
			// add WebAgntAccs

			// defect 10701 
			// add BatchRptMgmtAccs

			// defect 10844 
			// add ModfyTimedPrmtAccs 

			Vector lvValues = new Vector();

			String lsIns =
				"INSERT into RTS.RTS_SECURITY ("
					+ "OfcIssuanceNo,"
					+ "SubstaId,"
					+ "EmpId,"
					+ "UserName,"
					+ "EmpLastName,"
					+ "EmpFirstName,"
					+ "EmpMI,"
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
					+ "VoidTransAccs,"
					+ "CrdtCardFeeAccs,"
					+ "DlrRptAccs,"
					+ "LienHldrRptAccs,"
					+ "SubconRptAccs,"
					+ "DsabldPlcrdRptAccs,"
					+ "DsabldPlcrdInqAccs,"
					+ "ETtlRptAccs,"
					+ "PrivateLawEnfVehAccs,"
					+ "WebAgntAccs,"
					+ "ExportAccs,"
					+ "DsabldPlcrdReinstateAccs,"  
					+ "DeleteIndi,"
					+ "ChngTimestmp ) VALUES ( "
					+ " ?,"
					+ liSubstaId;
			// end defect 9710
			// end defect 9831
			// end defect 9960
			// end defect 10153
			// end defect 10717
			// end defect 10701

			String lsAdd = new String();

			//for (int i = 0; i < 103; i++)
			for (int i = 0; i < 105; i++)
			{
				lsAdd = lsAdd + " ,? ";
			}
			lsIns = lsIns + lsAdd + " , 0," + " Current Timestamp)";

			// end defect 10844 
			// end defect 11231
			
			try
			{
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getOfcIssuanceNo())));

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSecurityData.getEmpId())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSecurityData.getUserName())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSecurityData.getEmpLastName())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSecurityData.getEmpFirstName())));
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSecurityData.getEmpMI())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getAdminAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getAcctAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getAddrChngAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getAdjSalesTaxAccs())));

				// defect 10701 
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getBndedTtlCdAccs())));
				// end defect 10701 

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getBndedTtlCdAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getCancRegAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getCashOperAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getCCOAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getCntyRptsAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getCOAAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getCorrTtlRejAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getCustServAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getDelTtlInProcsAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getDlrAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getDlrTtlAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getDsabldPersnAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getDuplAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getEmpSecrtyAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getEmpSecrtyRptAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getExchAccs())));
				// defect 8900			
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getExmptAuditRptAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getExmptAuthAccs())));
				// end defect 8900 
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getFundsAdjAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getFundsBalAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getFundsInqAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getFundsMgmtAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getFundsRemitAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getHotCkCrdtAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getHotCkRedemdAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getInqAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getInvAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getInvAckAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getInvActionAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getInvAllocAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getInvDelAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getInvHldRlseAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getInvInqAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getInvProfileAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getItmSeizdAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getItrntRenwlAccs())));
				//PCR DRVED
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getIssueDrvsEdAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getJnkAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getLegalRestrntNoAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getLienHldrAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getLocalOptionsAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getMailRtrnAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getMiscRegAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getMiscRemksAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getModfyAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getModfyHotCkAccs())));

				// defect 10844 
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getModfyTimedPrmtAccs())));
				// end defect 10844 

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getNonResPrmtAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getPltNoAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getRefAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getRegnlColltnAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getRegOnlyAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getRegRefAmtAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getRenwlAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getReplAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getReprntRcptAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getReprntRptAccs())));
				// PCR 34 			
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getReprntStkrRptAccs())));
				// End PCR 34 
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getRgstrByAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getRptsAccs())));
				//defect 7310
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getRSPSUpdtAccs())));
				//end defect 7310

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getSalvAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getSecrtyAccs())));

				// defect 9085 
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getSpclPltAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getSpclPltApplAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getSpclPltRenwPltAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getSpclPltRevisePltAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getSpclPltResrvPltAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData
								.getSpclPltUnAccptblPltAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getSpclPltDelPltAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getSpclPltRptsAccs())));
				// end defect 9085

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getPrntImmedAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getStatusChngAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getStlnSRSAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getSubconAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getSubconRenwlAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getTempAddlWtAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getTimedPrmtAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getTowTrkAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getTtlApplAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getTtlRegAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getTtlRevkdAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getTtlSurrAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getVoidTransAccs())));
				// PCR 025
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getCrdtCardFeeAccs())));

				// defect 9710 
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getDlrRptAccs())));

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getLienHldrRptAccs())));

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getSubconRptAccs())));
				// end defect 9710 

				// defect 9831 
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getDsabldPlcrdRptAccs())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getDsabldPlcrdInqAccs())));
				// end defect 9831 

				// defect 9960 
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getETtlRptAccs())));
				// end defect 9960 

				// defect 10153 
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getPrivateLawEnfVehAccs())));
				// end defect 10153 

				// defect 10717 
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getWebAgntAccs())));
				// end defect 10717 

				// defect 11231 
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getExportAccs())));

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getDsabldPlcrdReInstateAccs())));
				// end defect 11231 
				
				Log.write(Log.SQL, this, "insSecurity - SQL - Begin");
				caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
				Log.write(Log.SQL, this, "insSecurity - SQL - End");
				Log.write(Log.METHOD, this, "insSecurity - End");

				// defect 8721
				// updAdminCache(aaSecurityData, liSubstaId);
				// end defect 8721 
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					"insSecurity - Exception - "
						+ aeRTSEx.getMessage());
				throw aeRTSEx;
			}
		} // if liNumRows = 0 
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_SECURITY for Purge
	 *
	 * @param  aiNumDays int
	 * @return int
	 * @throws RTSException
	 */
	public int purgeSecurity(int aiNumDays) throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeSecurity - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_SECURITY WHERE DELETEINDI = 1 and "
				+ "days(Current Date) - days(ChngTimestmp) > ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiNumDays)));
			Log.write(Log.SQL, this, "purgeSecurity - SQL - Begin");
			// defect 9825 
			// Return number of rows returned 	
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "purgeSecurity - SQL - End");
			Log.write(Log.METHOD, this, "purgeSecurity - End");
			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeSecurity - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query RTS.RTS_SECURITY
	 * 
	 * @param  aaSecurityData SecurityData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qrySecurity(SecurityData aaSecurityData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qrySecurity - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// Determine owning SubstaId
		int liSubstaId = qrySecuritySubstaId(aaSecurityData);
		int liReqSubstaId = aaSecurityData.getSubstaId();

		// defect 9831 
		// add DsabldPlcrdRptAccs, DsabldPlcrdInqAccs 

		// defect 9710
		// add DlrRptAccs, LienHldrRptAccs, SubconRptAccs

		// defect 9960
		// add ETtlRptAccs  

		// defect 10153
		// add PrivateLawEnfVehAccs

		// defect 10701 
		// add BatchRptMgmtAccs

		// defect 10717
		// add WebAgentAccs

		// defect 10844 
		// add ModfyTimedPrmtAccs 
		
		// defect 11231 
		// add ExportAccs, DsabldPlcrdReinstateAccs  
		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ liReqSubstaId
				+ " as SubstaId,"
				+ "EmpId,"
				+ "UserName,"
				+ "EmpLastName,"
				+ "EmpFirstName,"
				+ "EmpMI,"
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
				+ "VoidTransAccs,"
				+ "CrdtCardFeeAccs,"
				+ "DlrRptAccs,"
				+ "LienHldrRptAccs,"
				+ "SubconRptAccs,"
				+ "DsabldPlcrdRptAccs,"
				+ "DsabldPlcrdInqAccs,"
				+ "ETtlRptAccs,"
				+ "PrivateLawEnfVehAccs,"
				+ "WebAgntAccs,"
				+ "ExportAccs,"
				+ "DsabldPlcrdReinstateAccs,"
				+ "DeleteIndi,"
				+ "ChngTimestmp "
				+ "from RTS.RTS_SECURITY "
				+ "where "
				+ " OfcIssuanceNo = ? ");

		// end defect 9710
		// end defect 9831   
		// end defect 9960   
		// end defect 10153
		// end defect 10701 
		// end defect 10717
		// end defect 10844
		// end defect 11231 
		
		//defect 7462
		if (liSubstaId != Integer.MIN_VALUE)
		{
			lsQry.append(" and SubstaId = " + liSubstaId);
		}
		//end defect 7462
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaSecurityData.getOfcIssuanceNo())));
		// defect 6645
		// Use UserName if it is not null and EmpId is null or empty
		// have to check to see if the user is an rtsuser first.
		if (aaSecurityData.getUserName() != null
			&& ((aaSecurityData.getUserName().trim().length() > 4
				&& JniAdInterface.isRtsUserName(
					aaSecurityData.getUserName(),
					aaSecurityData.getOfcIssuanceNo())
				|| ((aaSecurityData.getUserName().trim().length() > 0
					&& !JniAdInterface.isRtsUserName(
						aaSecurityData.getUserName(),
						aaSecurityData.getOfcIssuanceNo()))))))
		{
			lsQry.append(" and UserName = ?");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityData.getUserName())));
		}
		// do not use empid if it is null
		// also do not use if we are using UserName
		else if (
			aaSecurityData.getEmpId() != null
				&& aaSecurityData.getEmpId().trim().length() > 0)
		{
			lsQry.append(" and EmpId = ?");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityData.getEmpId())));
		}
		// end defect 6645
		if (aaSecurityData.getChngTimestmp() == null)
		{
			lsQry.append(" and DeleteIndi = 0");
		}
		else
		{
			lsQry.append(" and ChngTimestmp > ?");
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaSecurityData.getChngTimestmp())));
		}
		lsQry.append(" order by EmpId ");
		try
		{
			Log.write(Log.SQL, this, " - qrySecurity - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qrySecurity - SQL - End");
			while (lrsQry.next())
			{
				SecurityData laSecurityData = new SecurityData();
				laSecurityData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laSecurityData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laSecurityData.setEmpId(
					caDA.getStringFromDB(lrsQry, "EmpId"));
				// defect 6445
				laSecurityData.setUserName(
					caDA.getStringFromDB(lrsQry, "UserName"));
				// end defect 6445
				laSecurityData.setEmpLastName(
					caDA.getStringFromDB(lrsQry, "EmpLastName"));
				laSecurityData.setEmpFirstName(
					caDA.getStringFromDB(lrsQry, "EmpFirstName"));
				laSecurityData.setEmpMI(
					caDA.getStringFromDB(lrsQry, "EmpMI"));
				laSecurityData.setAdminAccs(
					caDA.getIntFromDB(lrsQry, "AdminAccs"));
				laSecurityData.setAcctAccs(
					caDA.getIntFromDB(lrsQry, "AcctAccs"));
				laSecurityData.setAddrChngAccs(
					caDA.getIntFromDB(lrsQry, "AddrChngAccs"));
				laSecurityData.setAdjSalesTaxAccs(
					caDA.getIntFromDB(lrsQry, "AdjSalesTaxAccs"));
				// defect 10701 
				laSecurityData.setBatchRptMgmtAccs(
					caDA.getIntFromDB(lrsQry, "BatchRptMgmtAccs"));
				// end defect 10701 
				laSecurityData.setBndedTtlCdAccs(
					caDA.getIntFromDB(lrsQry, "BndedTtlCdAccs"));
				laSecurityData.setCancRegAccs(
					caDA.getIntFromDB(lrsQry, "CancRegAccs"));
				laSecurityData.setCashOperAccs(
					caDA.getIntFromDB(lrsQry, "CashOperAccs"));
				laSecurityData.setCCOAccs(
					caDA.getIntFromDB(lrsQry, "CCOAccs"));
				laSecurityData.setCntyRptsAccs(
					caDA.getIntFromDB(lrsQry, "CntyRptsAccs"));
				laSecurityData.setCOAAccs(
					caDA.getIntFromDB(lrsQry, "COAAccs"));
				laSecurityData.setCorrTtlRejAccs(
					caDA.getIntFromDB(lrsQry, "CorrTtlRejAccs"));
				laSecurityData.setCustServAccs(
					caDA.getIntFromDB(lrsQry, "CustServAccs"));
				laSecurityData.setDelTtlInProcsAccs(
					caDA.getIntFromDB(lrsQry, "DelTtlInProcsAccs"));
				laSecurityData.setDlrAccs(
					caDA.getIntFromDB(lrsQry, "DlrAccs"));
				laSecurityData.setDlrTtlAccs(
					caDA.getIntFromDB(lrsQry, "DlrTtlAccs"));
				laSecurityData.setDsabldPersnAccs(
					caDA.getIntFromDB(lrsQry, "DsabldPersnAccs"));
				// defect 9831 
				laSecurityData.setDsabldPlcrdRptAccs(
					caDA.getIntFromDB(lrsQry, "DsabldPlcrdRptAccs"));
				laSecurityData.setDsabldPlcrdInqAccs(
					caDA.getIntFromDB(lrsQry, "DsabldPlcrdInqAccs"));
				// end defect 9831 
				laSecurityData.setDuplAccs(
					caDA.getIntFromDB(lrsQry, "DuplAccs"));
				laSecurityData.setEmpSecrtyAccs(
					caDA.getIntFromDB(lrsQry, "EmpSecrtyAccs"));
				laSecurityData.setEmpSecrtyRptAccs(
					caDA.getIntFromDB(lrsQry, "EmpSecrtyRptAccs"));
				laSecurityData.setExchAccs(
					caDA.getIntFromDB(lrsQry, "ExchAccs"));
				// defect 8900
				laSecurityData.setExmptAuditRptAccs(
					caDA.getIntFromDB(lrsQry, "ExmptAuditRptAccs"));
				laSecurityData.setExmptAuthAccs(
					caDA.getIntFromDB(lrsQry, "ExmptAuthAccs"));
				// end defect 8900	
				laSecurityData.setFundsAdjAccs(
					caDA.getIntFromDB(lrsQry, "FundsAdjAccs"));
				laSecurityData.setFundsBalAccs(
					caDA.getIntFromDB(lrsQry, "FundsBalAccs"));
				laSecurityData.setFundsInqAccs(
					caDA.getIntFromDB(lrsQry, "FundsInqAccs"));
				laSecurityData.setFundsMgmtAccs(
					caDA.getIntFromDB(lrsQry, "FundsMgmtAccs"));
				laSecurityData.setFundsRemitAccs(
					caDA.getIntFromDB(lrsQry, "FundsRemitAccs"));
				laSecurityData.setHotCkCrdtAccs(
					caDA.getIntFromDB(lrsQry, "HotCkCrdtAccs"));
				laSecurityData.setHotCkRedemdAccs(
					caDA.getIntFromDB(lrsQry, "HotCkRedemdAccs"));
				laSecurityData.setInqAccs(
					caDA.getIntFromDB(lrsQry, "InqAccs"));
				laSecurityData.setInvAccs(
					caDA.getIntFromDB(lrsQry, "InvAccs"));
				laSecurityData.setInvAckAccs(
					caDA.getIntFromDB(lrsQry, "InvAckAccs"));
				laSecurityData.setInvActionAccs(
					caDA.getIntFromDB(lrsQry, "InvActionAccs"));
				laSecurityData.setInvAllocAccs(
					caDA.getIntFromDB(lrsQry, "InvAllocAccs"));
				laSecurityData.setInvDelAccs(
					caDA.getIntFromDB(lrsQry, "InvDelAccs"));
				laSecurityData.setInvHldRlseAccs(
					caDA.getIntFromDB(lrsQry, "InvHldRlseAccs"));
				laSecurityData.setInvInqAccs(
					caDA.getIntFromDB(lrsQry, "InvInqAccs"));
				laSecurityData.setInvProfileAccs(
					caDA.getIntFromDB(lrsQry, "InvProfileAccs"));
				laSecurityData.setItmSeizdAccs(
					caDA.getIntFromDB(lrsQry, "ItmSeizdAccs"));
				laSecurityData.setItrntRenwlAccs(
					caDA.getIntFromDB(lrsQry, "ItrntRenwlAccs"));
				laSecurityData.setIssueDrvsEdAccs(
					caDA.getIntFromDB(lrsQry, "IssueDrvsEdAccs"));
				laSecurityData.setJnkAccs(
					caDA.getIntFromDB(lrsQry, "JnkAccs"));
				laSecurityData.setLegalRestrntNoAccs(
					caDA.getIntFromDB(lrsQry, "LegalRestrntNoAccs"));
				laSecurityData.setLienHldrAccs(
					caDA.getIntFromDB(lrsQry, "LienHldrAccs"));
				laSecurityData.setLocalOptionsAccs(
					caDA.getIntFromDB(lrsQry, "LocalOptionsAccs"));
				laSecurityData.setMailRtrnAccs(
					caDA.getIntFromDB(lrsQry, "MailRtrnAccs"));
				laSecurityData.setMiscRegAccs(
					caDA.getIntFromDB(lrsQry, "MiscRegAccs"));
				laSecurityData.setMiscRemksAccs(
					caDA.getIntFromDB(lrsQry, "MiscRemksAccs"));
				laSecurityData.setModfyAccs(
					caDA.getIntFromDB(lrsQry, "ModfyAccs"));
				laSecurityData.setModfyHotCkAccs(
					caDA.getIntFromDB(lrsQry, "ModfyHotCkAccs"));
				// defect 10844 
				laSecurityData.setModfyTimedPrmtAccs(
					caDA.getIntFromDB(lrsQry, "ModfyTimedPrmtAccs"));
				// end defect 10844 
				laSecurityData.setNonResPrmtAccs(
					caDA.getIntFromDB(lrsQry, "NonResPrmtAccs"));
				laSecurityData.setPltNoAccs(
					caDA.getIntFromDB(lrsQry, "PltNoAccs"));
				laSecurityData.setRefAccs(
					caDA.getIntFromDB(lrsQry, "RefAccs"));
				laSecurityData.setRegnlColltnAccs(
					caDA.getIntFromDB(lrsQry, "RegnlColltnAccs"));
				laSecurityData.setRegOnlyAccs(
					caDA.getIntFromDB(lrsQry, "RegOnlyAccs"));
				laSecurityData.setRegnlColltnAccs(
					caDA.getIntFromDB(lrsQry, "RegnlColltnAccs"));
				laSecurityData.setRegRefAmtAccs(
					caDA.getIntFromDB(lrsQry, "RegRefAmtAccs"));
				laSecurityData.setRenwlAccs(
					caDA.getIntFromDB(lrsQry, "RenwlAccs"));
				laSecurityData.setReplAccs(
					caDA.getIntFromDB(lrsQry, "ReplAccs"));
				laSecurityData.setReprntRcptAccs(
					caDA.getIntFromDB(lrsQry, "ReprntRcptAccs"));
				laSecurityData.setReprntRptAccs(
					caDA.getIntFromDB(lrsQry, "ReprntRptAccs"));
				laSecurityData.setReprntStkrRptAccs(
					caDA.getIntFromDB(lrsQry, "ReprntStkrRptAccs"));
				laSecurityData.setRgstrByAccs(
					caDA.getIntFromDB(lrsQry, "RgstrByAccs"));
				laSecurityData.setRptsAccs(
					caDA.getIntFromDB(lrsQry, "RptsAccs"));
				laSecurityData.setRSPSUpdtAccs(
					caDA.getIntFromDB(lrsQry, "RSPSUpdtAccs"));
				laSecurityData.setSalvAccs(
					caDA.getIntFromDB(lrsQry, "SalvAccs"));
				laSecurityData.setSecrtyAccs(
					caDA.getIntFromDB(lrsQry, "SecrtyAccs"));
				// defect 9085
				laSecurityData.setSpclPltAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltAccs"));
				laSecurityData.setSpclPltApplAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltApplAccs"));
				laSecurityData.setSpclPltRenwPltAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltRenwPltAccs"));
				laSecurityData.setSpclPltRevisePltAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltRevisePltAccs"));
				laSecurityData.setSpclPltResrvPltAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltResrvPltAccs"));
				laSecurityData.setSpclPltUnAccptblPltAccs(
					caDA.getIntFromDB(
						lrsQry,
						"SpclPltUnAccptblPltAccs"));
				laSecurityData.setSpclPltDelPltAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltDelPltAccs"));
				laSecurityData.setSpclPltRptsAccs(
					caDA.getIntFromDB(lrsQry, "SpclPltRptsAccs"));
				// end defect 9085
				laSecurityData.setPrntImmedAccs(
					caDA.getIntFromDB(lrsQry, "PrntImmedAccs"));
				laSecurityData.setStatusChngAccs(
					caDA.getIntFromDB(lrsQry, "StatusChngAccs"));
				laSecurityData.setStlnSRSAccs(
					caDA.getIntFromDB(lrsQry, "StlnSRSAccs"));
				laSecurityData.setSubconAccs(
					caDA.getIntFromDB(lrsQry, "SubconAccs"));
				laSecurityData.setSubconRenwlAccs(
					caDA.getIntFromDB(lrsQry, "SubconRenwlAccs"));
				laSecurityData.setTempAddlWtAccs(
					caDA.getIntFromDB(lrsQry, "TempAddlWtAccs"));
				laSecurityData.setTimedPrmtAccs(
					caDA.getIntFromDB(lrsQry, "TimedPrmtAccs"));
				laSecurityData.setTowTrkAccs(
					caDA.getIntFromDB(lrsQry, "TowTrkAccs"));
				laSecurityData.setTtlApplAccs(
					caDA.getIntFromDB(lrsQry, "TtlApplAccs"));
				laSecurityData.setTtlRegAccs(
					caDA.getIntFromDB(lrsQry, "TtlRegAccs"));
				laSecurityData.setTtlRevkdAccs(
					caDA.getIntFromDB(lrsQry, "TtlRevkdAccs"));
				laSecurityData.setTtlSurrAccs(
					caDA.getIntFromDB(lrsQry, "TtlSurrAccs"));
				laSecurityData.setVoidTransAccs(
					caDA.getIntFromDB(lrsQry, "VoidTransAccs"));
				laSecurityData.setCrdtCardFeeAccs(
					caDA.getIntFromDB(lrsQry, "CrdtCardFeeAccs"));

				// defect 9710 
				laSecurityData.setDlrRptAccs(
					caDA.getIntFromDB(lrsQry, "DlrRptAccs"));
				laSecurityData.setLienHldrRptAccs(
					caDA.getIntFromDB(lrsQry, "LienHldrRptAccs"));
				laSecurityData.setSubconRptAccs(
					caDA.getIntFromDB(lrsQry, "SubconRptAccs"));
				// end defect 9710 
				// defect 9960 
				laSecurityData.setETtlRptAccs(
					caDA.getIntFromDB(lrsQry, "ETtlRptAccs"));
				// end defect 9960 

				// defect 10153
				laSecurityData.setPrivateLawEnfVehAccs(
					caDA.getIntFromDB(lrsQry, "PrivateLawEnfVehAccs"));
				// end defect 10153

				// defect 10717
				laSecurityData.setWebAgntAccs(
					caDA.getIntFromDB(lrsQry, "WebAgntAccs"));
				// end defect 10717
				
				// defect 11231
				laSecurityData.setExportAccs(
					caDA.getIntFromDB(lrsQry, "ExportAccs"));
				laSecurityData.setDsabldPlcrdReInstateAccs(
						caDA.getIntFromDB(lrsQry, "DsabldPlcrdReInstateAccs"));
				// end defect 11231

				laSecurityData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				laSecurityData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));

				// Add element to the Vector
				lvRslt.addElement(laSecurityData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qrySecurity - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySecurity - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qrySecurity - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Determine SubstaId for RTS.RTS_SECURITY
	 * 
	 * @param  aaSecurityiData SecurityData
	 * @return Vector 	
	 */
	public int qrySecuritySubstaId(SecurityData aaSecurityData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qrySecuritySubstaId - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvValues = new Vector();
		ResultSet lrsQry;
		int liSubstaId = Integer.MIN_VALUE;
		lsQry.append(
			"SELECT TblSubstaId  "
				+ " from RTS.RTS_SUBSTA_SUBSCR "
				+ "where "
				+ " TblName = 'RTS_SECURITY' and "
				+ " OfcIssuanceNo = ? and  "
				+ " SubstaId = ? ");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaSecurityData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaSecurityData.getSubstaId())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qrySecuritySubstaId - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qrySecuritySubstaId - SQL - End");
			while (lrsQry.next())
			{
				liSubstaId = caDA.getIntFromDB(lrsQry, "TblSubstaId");
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qrySecuritySubstaId - End ");
			if (liSubstaId == Integer.MIN_VALUE)
			{
				liSubstaId = aaSecurityData.getSubstaId();
			}
			return (liSubstaId);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySecuritySubstaId - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Select Employees w/ Transactions
	 * 
	 * @param  aaSecurityData SecurityData
	 * @return Vector
	 * @throws RTSException  	
	 */
	public Vector qryTransSecurity(SecurityData aaSecurityData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryTransSecurity - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet lrsQry;
		lsQry.append(
			"Select Distinct TransEmpId as EmpId, EmpLastName,"
				+ "EmpFirstName,EmpMI "
				+ " from RTS.RTS_TRANS A, RTS.RTS_SECURITY B "
				+ " where "
				+ " A.TransEmpId = B.EmpId and "
				+ " A.OfcIssuanceNo = ? and "
				+ " A.SubstaId = ?  and "
				+ " A.OfcIssuanceNo = B.OfcIssuanceNo and "
				+ " A.SubstaId = B.SubstaId ");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaSecurityData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaSecurityData.getSubstaId())));
		lsQry.append(
			"UNION ALL "
				+ " Select Distinct TransEmpId as EmpId, EmpLastName,"
				+ "EmpFirstName,EmpMI "
				+ " from RTS.RTS_TRANS A, RTS.RTS_SECURITY B , "
				+ "RTS.RTS_SUBSTA_SUBSCR C  "
				+ " where "
				+ " A.TransEmpId = B.EmpId and "
				+ " A.OfcIssuanceNo = ? and "
				+ " A.SubstaId = ?  and "
				+ " A.OfcIssuanceNo = C.OfcIssuanceNo and "
				+ " A.SubstaId = C.SubstaId and "
				+ " A.OfcIssuanceNo = B.OfcIssuanceNo and "
				+ " B.SubstaId = C.TblSubstaId and "
				+ " C.TblName = 'RTS_SECURITY' ORDER BY 1 ");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaSecurityData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaSecurityData.getSubstaId())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryTransSecurity - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryTransSecurity - SQL - End");
			while (lrsQry.next())
			{
				SecurityData laSecurityData = new SecurityData();
				laSecurityData.setEmpId(
					caDA.getStringFromDB(lrsQry, "EmpId"));
				laSecurityData.setEmpLastName(
					caDA.getStringFromDB(lrsQry, "EmpLastName"));
				laSecurityData.setEmpFirstName(
					caDA.getStringFromDB(lrsQry, "EmpFirstName"));
				laSecurityData.setEmpMI(
					caDA.getStringFromDB(lrsQry, "EmpMI"));
				// Add element to the Vector
				lvRslt.addElement(laSecurityData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryTransSecurity - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransSecurity - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_SECURITY
	 *
	 * @param  aaSecurityData SecurityData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updSecurity(SecurityData aaSecurityData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updSecurity - Begin");

		Vector lvValues = new Vector();

		int liSubstaId = qrySecuritySubstaId(aaSecurityData);
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

		String lsUpd =
			"UPDATE RTS.RTS_SECURITY SET "
				+ "EmpId = ?, "
				+ "UserName = ?, "
				+ "EmpLastName = ?, "
				+ "EmpFirstName = ?, "
				+ "EmpMI = ?, "
				+ "AdminAccs = ?, "
				+ "AcctAccs = ?, "
				+ "AddrChngAccs = ?, "
				+ "AdjSalesTaxAccs = ?, "
				+ "BatchRptMgmtAccs = ?, "
				+ "BndedTtlCdAccs = ?, "
				+ "CancRegAccs = ?, "
				+ "CashOperAccs = ?, "
				+ "CCOAccs = ?, "
				+ "CntyRptsAccs = ?, "
				+ "COAAccs = ?, "
				+ "CorrTtlRejAccs = ?, "
				+ "CustServAccs = ?, "
				+ "DelTtlInProcsAccs = ?, "
				+ "DlrAccs = ?, "
				+ "DlrTtlAccs = ?, "
				+ "DsabldPersnAccs = ?, "
				+ "DuplAccs = ?, "
				+ "EmpSecrtyAccs = ?, "
				+ "EmpSecrtyRptAccs = ?, "
				+ "ExchAccs = ?, "
				+ "ExmptAuditRptAccs = ? ,"
				+ "ExmptAuthAccs = ? ,"
				+ "FundsAdjAccs = ?, "
				+ "FundsBalAccs = ?, "
				+ "FundsInqAccs = ?, "
				+ "FundsMgmtAccs = ?, "
				+ "FundsRemitAccs = ?, "
				+ "HotCkCrdtAccs = ?, "
				+ "HotCkRedemdAccs = ?, "
				+ "InqAccs = ?, "
				+ "InvAccs = ?, "
				+ "InvAckAccs = ?, "
				+ "InvActionAccs = ?, "
				+ "InvAllocAccs = ?, "
				+ "InvDelAccs = ?, "
				+ "InvHldRlseAccs = ?, "
				+ "InvInqAccs = ?, "
				+ "InvProfileAccs = ?, "
				+ "ItmSeizdAccs = ?, "
				+ "ItrntRenwlAccs = ?, "
				+ "IssueDrvsEdAccs = ?, "
				+ "JnkAccs = ?, "
				+ "LegalRestrntNoAccs = ?, "
				+ "LienHldrAccs = ?, "
				+ "LocalOptionsAccs = ?, "
				+ "MailRtrnAccs = ?, "
				+ "MiscRegAccs = ?, "
				+ "MiscRemksAccs = ?, "
				+ "ModfyAccs = ?, "
				+ "ModfyHotCkAccs = ?, "
				+ "ModfyTimedPrmtAccs = ?,"
				+ "NonResPrmtAccs = ?, "
				+ "PltNoAccs = ?, "
				+ "RefAccs = ?, "
				+ "RegnlColltnAccs = ?,"
				+ "RegOnlyAccs = ?, "
				+ "RegRefAmtAccs = ?, "
				+ "RenwlAccs = ?, "
				+ "ReplAccs = ?, "
				+ "ReprntRcptAccs = ?, "
				+ "ReprntRptAccs = ?, "
				+ "ReprntStkrRptAccs = ?, "
				+ "RgstrByAccs = ?, "
				+ "RptsAccs = ?, "
				+ "RSPSUpdtAccs = ?, "
				+ "SalvAccs = ?, "
				+ "SecrtyAccs = ?, "
				+ "SpclPltAccs = ?, "
				+ "SpclPltApplAccs = ?, "
				+ "SpclPltRenwPltAccs = ?, "
				+ "SpclPltRevisePltAccs = ?, "
				+ "SpclPltResrvPltAccs = ?, "
				+ "SpclPltUnAccptblPltAccs = ?, "
				+ "SpclPltDelPltAccs = ?, "
				+ "SpclPltRptsAccs = ?, "
				+ "PrntImmedAccs = ?, "
				+ "StatusChngAccs = ?, "
				+ "StlnSRSAccs = ?, "
				+ "SubconAccs = ?, "
				+ "SubconRenwlAccs = ?, "
				+ "TempAddlWtAccs = ?, "
				+ "TimedPrmtAccs = ?, "
				+ "TowTrkAccs = ?, "
				+ "TtlApplAccs = ?, "
				+ "TtlRegAccs = ?, "
				+ "TtlRevkdAccs = ?, "
				+ "TtlSurrAccs = ?, "
				+ "VoidTransAccs = ?, "
				+ "CrdtCardFeeAccs = ?, "
				+ "DlrRptAccs = ? ,"
				+ "LienHldrRptAccs = ?,"
				+ "SubconRptAccs = ?,"
				+ "DsabldPlcrdRptAccs =?,"
				+ "DsabldPlcrdInqAccs =?,"
				+ "ETtlRptAccs =?,"
				+ "PrivateLawEnfVehAccs=?,"
				+ "WebAgntAccs = ?, "
				+ "ExportAccs = ?, "
				+ "DsabldPlcrdReinstateAccs = ?, "
				+ "DeleteIndi = 0, "
				+ "ChngTimestmp = CURRENT TIMESTAMP "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = ? ";
		// end defect 10153
		// end defect 9710 
		// end defect 9831 
		// end defect 9960 
		// end defect 10701 
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityData.getEmpId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityData.getUserName())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityData.getEmpLastName())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityData.getEmpFirstName())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaSecurityData.getEmpMI())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getAdminAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getAcctAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getAddrChngAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getAdjSalesTaxAccs())));
			// defect 10701 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getBatchRptMgmtAccs())));
			// end defect 10701  
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getBndedTtlCdAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getCancRegAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getCashOperAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getCCOAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getCntyRptsAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getCOAAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getCorrTtlRejAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getCustServAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getDelTtlInProcsAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getDlrAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getDlrTtlAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getDsabldPersnAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getDuplAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getEmpSecrtyAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getEmpSecrtyRptAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getExchAccs())));
			// defect 8900
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getExmptAuditRptAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getExmptAuthAccs())));
			// end defect 8900
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getFundsAdjAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getFundsBalAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getFundsInqAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getFundsMgmtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getFundsRemitAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getHotCkCrdtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getHotCkRedemdAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getInqAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getInvAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getInvAckAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getInvActionAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getInvAllocAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getInvDelAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getInvHldRlseAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getInvInqAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getInvProfileAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getItmSeizdAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getItrntRenwlAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getIssueDrvsEdAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getJnkAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getLegalRestrntNoAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getLienHldrAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getLocalOptionsAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getMailRtrnAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getMiscRegAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getMiscRemksAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getModfyAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getModfyHotCkAccs())));

			// defect 10844 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getModfyTimedPrmtAccs())));
			// end defect 10844 

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getNonResPrmtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getPltNoAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getRefAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getRegnlColltnAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getRegOnlyAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getRegRefAmtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getRenwlAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getReplAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getReprntRcptAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getReprntRptAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getReprntStkrRptAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getRgstrByAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getRptsAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getRSPSUpdtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSalvAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSecrtyAccs())));

			// defect 9085 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSpclPltAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSpclPltApplAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSpclPltRenwPltAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSpclPltRevisePltAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSpclPltResrvPltAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSpclPltUnAccptblPltAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSpclPltDelPltAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSpclPltRptsAccs())));
			// end defect 9085

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getPrntImmedAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getStatusChngAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getStlnSRSAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSubconAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSubconRenwlAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getTempAddlWtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getTimedPrmtAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getTowTrkAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getTtlApplAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getTtlRegAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getTtlRevkdAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getTtlSurrAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getVoidTransAccs())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getCrdtCardFeeAccs())));

			// defect 9710 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getDlrRptAccs())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getLienHldrRptAccs())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getSubconRptAccs())));
			// end defect 9710 

			// defect 9831 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getDsabldPlcrdRptAccs())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getDsabldPlcrdInqAccs())));
			// end defect 9831 

			// defect 9960 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getETtlRptAccs())));
			// end defect 9960 

			// defect 10153 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getPrivateLawEnfVehAccs())));
			// end defect 10153 

			// defect 10717 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getWebAgntAccs())));
			// end defect 10717 
			
			// defect 11231 
			lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getExportAccs())));
			lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaSecurityData.getDsabldPlcrdReInstateAccs())));
			// end defect 11231 
			
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaSecurityData.getOfcIssuanceNo())));

			// defect 8721 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(liSubstaId)));
			// end defect 8721 

			// defect 6645
			// remove this code and use the following if.
			// lvValues.addElement(
			// new DBValue(Types.CHAR, caDA.convertToString
			//               (aaSecurityData.getEmpId())));
			// Use UserName if it is not null or empty
			// have to check to see if the user is an rtsuser first.
			if (aaSecurityData.getUserName() != null
				&& ((aaSecurityData.getUserName().trim().length() > 4
					&& JniAdInterface.isRtsUserName(
						aaSecurityData.getUserName(),
						aaSecurityData.getOfcIssuanceNo())
					|| ((aaSecurityData.getUserName().trim().length() > 0
						&& !JniAdInterface.isRtsUserName(
							aaSecurityData.getUserName(),
							aaSecurityData.getOfcIssuanceNo()))))))
			{
				lsUpd = lsUpd + " and UserName = ?";
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSecurityData.getUserName())));
			}
			// use EmpId is there is no UserName
			else
			{
				lsUpd = lsUpd + " and EmpId = ?";
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaSecurityData.getEmpId())));
			}
			//+ " and EmpId = ? ";
			Log.write(Log.SQL, this, "updSecurity - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updSecurity - SQL - End");
			// only write the admin log if the update was done.
			//			
			//if (liNumRows != 0)
			//{
			//	updAdminCache(aaSecurityData, liSubstaId);
			//}
			// end defect 6445
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updSecurity - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
