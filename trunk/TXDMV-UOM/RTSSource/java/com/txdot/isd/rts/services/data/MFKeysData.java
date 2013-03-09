package com.txdot.isd.rts.services.data;import com.txdot.isd.rts.services.util.Log;import com.txdot.isd.rts.services.util.UtilityMethods;/* * MFKeysData.java * * (c) Texas Department of Transportation 2010 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	07/29/2010	Created * 							defect 10462 Ver 6.5.0  * K Harrell	08/12/2010	added writeKeysToSysErrLog() * 							defect 10462 Ver 6.5.0  * --------------------------------------------------------------------- *//** * This Data Object is used exclusively to create MFRequestData object *  from the parameters passed in getMFResponse() in MFAccess.  *  * The resulting MFRequestData object will be inserted into DB2 for  *   MF Request Tracking.   * * @version	6.5.0			08/12/2010 * @author	K Harrell * <br>Creation Date:		07/29/2010 *//* &MFKeysData& */public class MFKeysData{/* &MFKeysData'csReqKey& */	String csReqKey;/* &MFKeysData'csDocNo& */	String csDocNo;/* &MFKeysData'csOwnrId& */	String csOwnrId;/* &MFKeysData'csHOOPSRegPltNo& */	String csHOOPSRegPltNo;/* &MFKeysData'ciSpclRegId& */	int ciSpclRegId;/* &MFKeysData'csVIN& */	String csVIN;/* &MFKeysData'ciComptCntyNo& */	int ciComptCntyNo;/* &MFKeysData'csFundsRptDate& */	String csFundsRptDate;/* &MFKeysData'csRptngDate& */	String csRptngDate;/* &MFKeysData'csFundsDueDate& */	String csFundsDueDate;/* &MFKeysData'csFundsPymntDate& */	String csFundsPymntDate;/* &MFKeysData'ciTraceNo& */	int ciTraceNo;/* &MFKeysData'csCkNo& */	String csCkNo;/* &MFKeysData'csInvcNo& */	String csInvcNo;/* &MFKeysData'ciTierCd& */	int ciTierCd;/* &MFKeysData'csPrmtIssuanceId& */	String csPrmtIssuanceId;/* &MFKeysData'csPrmtNo& */	String csPrmtNo;/* &MFKeysData'csCustLstName& */	String csCustLstName;/* &MFKeysData'csCustBsnName& */	String csCustBsnName;/* &MFKeysData'ciBeginDate& */	int ciBeginDate;/* &MFKeysData'ciEndDate& */	int ciEndDate;	/**	 * MFKeysData.java Constructor	 * @param asMFReqKey  String Mainframe request key type for search	 * @param asDocNo String Document number for vehicle (on server)	 * @param asOwnrId String Tax Id (not used for look up anymore) 	 * @param asHOOPSRegPltNo String Registration Plate Number	 * @param aiSpclRegId int Special PLates Reg Id	 * @param asVIN String VIN number of vehicle	 * @param aiComptCntyNo int Claim Compt County Number	 * @param asFundsRptDate String Funds Report Date	 * @param asRptngDate String Reporting Date	 * @param asFundsDueDate String Funds Due Date	 * @param asFundsPymntDate String Funds Payment Date	 * @param aiTraceNo int Trace Number (not used anymore)	 * @param asCkNo String Check number (not used anymore) 	 * @param asInvcNo String Invoice Number 	 * @param aiTierCd	 * @param asPrmtNo	 * @param asPrmtVIN	 * @param asCustLstName	 * @param asCustBsnName	 * @param aiBeginDate	 * @param aiEndDate 	 *//* &MFKeysData.MFKeysData& */	public MFKeysData(		String asReqKey,		String asDocNo,		String asOwnrId,		String asHOOPSRegPltNo,		int aiSpclRegId,		String asVIN,		int aiComptCntyNo,		String asFundsRptDate,		String asRptngDate,		String asFundsDueDate,		String asFundsPymntDate,		int aiTraceNo,		String asCkNo,		String asInvcNo,		int aiTierCd,		String asPrmtIssuanceId,		String asPrmtNo,		String asCustLstName,		String asCustBsnName,		int aiBeginDate,		int aiEndDate)	{		super();		csReqKey = asReqKey;		csDocNo = asDocNo;		csOwnrId = asOwnrId;		csHOOPSRegPltNo = asHOOPSRegPltNo;		ciSpclRegId = aiSpclRegId;		csVIN = asVIN;		ciComptCntyNo = aiComptCntyNo;		csFundsRptDate = asFundsRptDate;		csRptngDate = asRptngDate;		csFundsDueDate = asFundsDueDate;		csFundsPymntDate = asFundsPymntDate;		ciTraceNo = aiTraceNo;		csCkNo = asCkNo;		csInvcNo = asInvcNo;		ciTierCd = aiTierCd;		csPrmtIssuanceId = asPrmtIssuanceId;		csPrmtNo = asPrmtNo;		csCustLstName = asCustLstName;		csCustBsnName = asCustBsnName;		ciBeginDate = aiBeginDate;		ciEndDate = aiEndDate;	}	/**	 * Return MFRequestData Object	 * 	 * @return MFRequestData	 *//* &MFKeysData.getMFReqData& */	public MFRequestData getMFReqData()	{		MFRequestData laMFReqData = new MFRequestData();		laMFReqData.setReqKey(csReqKey);		laMFReqData.setParm1(csDocNo);		laMFReqData.setParm1(csOwnrId);		laMFReqData.setParm1(csHOOPSRegPltNo);		laMFReqData.setParm1(csVIN);		laMFReqData.setParm1(csCkNo);		laMFReqData.setParm1(csInvcNo);		laMFReqData.setParm1(csPrmtIssuanceId);		laMFReqData.setParm1(csPrmtNo);		laMFReqData.setParm1(csCustLstName);		laMFReqData.setParm1(csCustBsnName);		if (ciSpclRegId != 0)		{			laMFReqData.setParm1("" + ciSpclRegId);		}		if (ciTraceNo != 0)		{			laMFReqData.setParm1("" + ciTraceNo);		}		if (UtilityMethods.isEmpty(laMFReqData.getParm1())			&& ciComptCntyNo != 0)		{			laMFReqData.setParm1("" + ciComptCntyNo);		}		laMFReqData.setTierCd(ciTierCd);		laMFReqData.setDateParm1(csFundsRptDate);		laMFReqData.setDateParm1(csRptngDate);		laMFReqData.setDateParm1(csFundsDueDate);		laMFReqData.setDateParm1(csFundsPymntDate);		laMFReqData.setDateParm1(ciBeginDate);		laMFReqData.setDateParm2(ciEndDate);		return laMFReqData;	}	/** 	 * Print MF Key Data if null/invalid 	 * 	 * @param asErrMsg 	 *//* &MFKeysData.writeKeysToSysErrLog& */	public void writeKeysToSysErrLog(String asErrMsg)	{		String lsKeys =			"MfAccess Error: Invalid Length in MFKey Setup: "				+ asErrMsg				+ "MFReqKey: "				+ csReqKey				+ ", DocNo: "				+ csDocNo				+ ", OwnerId: "				+ csOwnrId				+ ", RegPltNo: "				+ csHOOPSRegPltNo				+ ", SpclRegId: "				+ ciSpclRegId				+ ", VIN: "				+ csVIN				+ ", ComptCntyNo: "				+ ciComptCntyNo				+ ", FundsRptDate: "				+ csFundsRptDate				+ ", RptngDate: "				+ csRptngDate				+ ", FundsDueDate: "				+ csFundsDueDate				+ ", FundsPymntDate: "				+ csFundsPymntDate				+ ", TraceNo: "				+ ciTraceNo				+ ", CheckNo: "				+ csCkNo				+ ", InvcNo: "				+ csInvcNo				+ ", TierCd: "				+ ciTierCd				+ ", PrmtIssuanceId: "				+ csPrmtIssuanceId				+ ", PrmtNo: "				+ csPrmtNo				+ ", CustLstName: "				+ csCustLstName				+ ", CustBsnName: "				+ csCustBsnName				+ ", BeginDate: "				+ ciBeginDate				+ ", EndDate: "				+ ciEndDate;		System.err.println(lsKeys);		Log.write(Log.SQL_EXCP, this, lsKeys);	}}/* #MFKeysData# */