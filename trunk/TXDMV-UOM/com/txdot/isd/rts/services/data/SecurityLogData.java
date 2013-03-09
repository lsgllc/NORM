package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;

/*
 *
 * SecurityLogData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell    10/16/2001  Removed references to ChngTimestmp, 
 * 							DeleteIndi
 * K Harrell    01/09/2002  Removed DelPltToOwnr,FundsAck,InvAdj,
 * 							PltToOwnr,Rejctn
 *                          Added ItrntRenwlAccs, RegnlColltnAccs
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * Ray Rowehl	09/22/2003	Added support for new field ADUserId
 *							added csAdUserId, getAdUserId(), setAdUserId
 *							modify SecurityLogData()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	12/01/2003	Rename AdUserId to SysUserId.
 *							This is to avoid confusing POS Programmers.
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	12/18/2003	Rename SysUserId to UserName.
 *							This is to avoid confusing POS Programmers.
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/17/2004	Reformat to match updated Java Standards
 * 							defect 6445 Ver 5.1.6
 * K Harrell	01/22/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add ciReprntStkrRptAccs (modified from 
 * 							 reprintStickerRptAccs)
 * 							Add associated set/get methods
 * 							Ver 5.2.0
 * K Harrell	03/20/2004	5.1.6 Merge
 * 							added UserName, associated set/get 
 * 							methods
 * 							Ver 5.2.0	
 * K Harrell	04/02/2004	removed ciQuickCntrAccs, ciQuickCntrRptAccs
 *							deleted associated get/set methods
 *							modify SecurityLogData(SecurityData secData)
 *							defect 6995  Ver 5.2.0
 * Min Wang		07/08/2004	Add ciRSPSUpdtAccs for RSPS status update
 *							modify SecurityLogData()
 *							defect 7310 Ver 5.2.1
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data
 *							defect 7899 Ver 5.2.3
 * K Harrell	09/25/2006	Variable additions,get/set methods for
 * 							Exempts
 * 							add ciExmptAuditRptAccs, ciExmptAuthAccs
 * 							add getExmptAuditRptAccs(),
 * 								setExmptAuditRptAccs(),
 * 								getExmptAuthAccs(),
 * 								setExmptAuthAccs()
 * 							modify SecurityLogData(SecurityData)
 * 							defect 8900 Ver Exempts
 * K Harrell	02/05/2007  Changes for Special Plates 
 * 							add ciSpclPltApplAccs,ciSpclPltRenwPltAccs,
 * 							ciSpclPltRevisePltAccs,ciSpclPltResrvPltAccs, 
 *							ciSpclPltUnAccptblPltAccs, 
 *							ciSpclPltDelPltAccs,ciSpclPltRptsAccs, 
 *							plus get/set methods,
 *							SecurityLogData(SecurityData)
 *							defect 9085 ver Special Plates
 * K Harrell	02/13/2007	add ciSpclPltAccs, getSpclPltAccs(),	
 * 							 setSpclPltAccs() 
 * 							defect 9085 ver Special Plates
 * K Harrell	09/10/2008	add ciDlrRptAccs, ciLienHldrRptAccs,
 * 							 ciSubconRptAccs, get/set methods
 * 							defect 9710 Ver Defect_POS_B
 * K Harrell	10/27/2008	add ciDsabldPlcrdRptAccs, 
 * 							 ciDsabldPlcrdInqAccs, get/set methods
 * 							modify SecurityLogData(SecurityData)
 * 							defect 9831 Ver Defect_POS_B       
 * B Hargrove	02/24/2009	add ciETtlRptAccs, get/set methods
 * 							modify SecurityLogData(SecurityData)
 * 							defect 9960 Ver Defect_POS_E  
 * Min Wang		08/10/2009	add ciPrivateLawEnfVehAccs, 
 * 							getPrivateLawEnfVehAccs(), 
 * 							setPrivateLawEnfVehAccs()
 * 							defect 10153 Ver Defect_POS_F
 * Min Wang		08/20/2008	modify SecurityLogData().
 * 							defect 10153 Ver Defect_POS_F
 * Min Wang		01/04/2011	add ciWebAgentAccs, getWebAgentAccs(),
 * 							setWebAgentAccs()
 * 							defect 10717 Ver POS_760
 * Min Wang		01/18/2011	modify SecurityLogData()
 * 							defect 10717 Ver POS_760
 * Min Wang		01/14/2011	delete ciWebAgentAccs, getWebAgentAccs(),
 * 							setWebAgentAccs()
 * 							add ciWebAgntAccs, getWebAgntAccs(),
 * 							setWebAgntAccs()
 * 							defect 10717 Ver POS_760
 * K Harrell	01/20/2011  add ciBatchRptMgmtAccs, get/set methods
 * 							modify SecurityLogData(SecurityData)
 * 							defect 10701 Ver 6.7.0 
 * K Harrell	05/28/2011	add ciModfyTimedPrmtAccs, get/set methods
 * 							modify SecurityLogData(SecurityData)
 * 							defect 10844 Ver 6.8.0  
 * K Harrell	06/17/2011	add isRegion(), isHQ() 
 * 							defect 10900 Ver 6.8.0
 * K Harrell	01/11/2012	add ciExportAccs, ciDsabldPlcrdReInstateAccs, 
 * 	B Woodson					 get/set methods
 * 							modify SecurityLogData(SecurityData)
 *  						defect 11231 Ver 6.10.0 
 * K Harrell	02/02/2012	Javadoc Cleanup 
 * 							defect 11231 Ver 6.10.0  
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * SecurityLogData
 * 
 * @version 6.10.0 			02/02/2012
 * @author 	Kathy Harrell
 * <br>Creation Date:		09/17/2001
 */
public class SecurityLogData implements Serializable
{
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected String csEmpId;
	protected String csUserName; // 6445

	protected int ciTransAMDate;
	protected int ciTransTime;
	// int 
	protected int ciAcctAccs;
	protected int ciAddrChngAccs;
	protected int ciAdjSalesTaxAccs;

	protected int ciAdminAccs;
	// defect 10701 
	protected int ciBatchRptMgmtAccs;
	// end defect 10701 
	protected int ciBndedTtlCdAccs;
	protected int ciCancRegAccs;
	protected int ciCashOperAccs;
	protected int ciCCOAccs;
	protected int ciCntyRptsAccs;
	protected int ciCOAAccs;
	protected int ciCorrTtlRejAccs;
	protected int ciCrdtCardFeeAccs;
	protected int ciCustServAccs;
	protected int ciDelTtlInProcsAccs;
	protected int ciDlrAccs;
	protected int ciDlrTtlAccs;
    protected int ciDsabldPersnAccs;
	protected int ciDsabldPlcrdInqAccs;
	// defect 11231 
	protected int ciDsabldPlcrdReInstateAccs;
	// end defect 11231 
	protected int ciDsabldPlcrdRptAccs;
	protected int ciDuplAccs;
	protected int ciEmpSecrtyAccs;
	protected int ciEmpSecrtyRptAccs;
	protected int ciExchAccs;
	protected int ciExmptAuditRptAccs;
	protected int ciExmptAuthAccs;
	// defect 11231 
	protected int ciExportAccs;
	// end defect 11231 
	protected int ciETtlRptAccs;
	protected int ciFundsAdjAccs;
	protected int ciFundsBalAccs;
	protected int ciFundsInqAccs;
	protected int ciFundsMgmtAccs;
	protected int ciFundsRemitAccs;
	protected int ciHotCkCrdtAccs;
	protected int ciHotCkRedemdAccs;
	protected int ciInqAccs;
	protected int ciInvAccs;
	protected int ciInvAckAccs;
	protected int ciInvActionAccs;
	protected int ciInvAllocAccs;
	protected int ciInvDelAccs;
	protected int ciInvHldRlseAccs;
	protected int ciInvInqAccs;
	protected int ciInvProfileAccs;
	protected int ciIssueDrvsEdAccs;
	protected int ciItmSeizdAccs;
	protected int ciItrntRenwlAccs;
	protected int ciJnkAccs;
	protected int ciLegalRestrntNoAccs;
	protected int ciLienHldrAccs;
	protected int ciLocalOptionsAccs;
	protected int ciMailRtrnAccs;
	protected int ciMiscRegAccs;
	protected int ciMiscRemksAccs;
	protected int ciModfyAccs;
	protected int ciModfyHotCkAccs;
	// defect 10844 
	protected int ciModfyTimedPrmtAccs;
	// end defect 10844 
	protected int ciNonResPrmtAccs;
	protected int ciPltNoAccs;
	protected int ciPrntImmedAccs;
	protected int ciRefAccs;
	protected int ciRegnlColltnAccs;
	protected int ciRegOnlyAccs;
	protected int ciRegRefAmtAccs;
	protected int ciRenwlAccs;
	protected int ciReplAccs;
	protected int ciReprntRcptAccs;
	protected int ciReprntRptAccs;
	protected int ciReprntStkrRptAccs;
	protected int ciRgstrByAccs;
	protected int ciRptsAccs;
	protected int ciRSPSUpdtAccs;
	protected int ciSalvAccs;
	protected int ciSecrtyAccs;
	protected int ciStatusChngAccs;
	protected int ciStlnSRSAccs;
	protected int ciSubconAccs;
	protected int ciSubconRenwlAccs;
	protected int ciTempAddlWtAccs;
	protected int ciTimedPrmtAccs;
	protected int ciTowTrkAccs;
	protected int ciTransWsId;
	protected int ciTtlApplAccs;
	protected int ciTtlRegAccs;
	protected int ciTtlRevkdAccs;

	protected int ciTtlSurrAccs;
	protected int ciVoidTransAccs;
	// String 
	protected String csEmpFirstName;
	protected String csEmpLastName;
	protected String csEmpMI;

	protected String csUpdtActn;
	protected String csUpdtngEmpId;
	// defect 9085
	protected int ciSpclPltAccs;
	protected int ciSpclPltApplAccs;
	protected int ciSpclPltRenwPltAccs;
	protected int ciSpclPltRevisePltAccs;
	protected int ciSpclPltResrvPltAccs;
	protected int ciSpclPltUnAccptblPltAccs;

	protected int ciSpclPltDelPltAccs;
	protected int ciSpclPltRptsAccs;
	// end defect 9085 
	// defect 9710 
	protected int ciDlrRptAccs;

	protected int ciLienHldrRptAccs;

	protected int ciSubconRptAccs;
	// end defect 9710 

	// defect 10153
	protected int ciPrivateLawEnfVehAccs;
	// end defect 10153

	// defect 10717
	protected int ciWebAgntAccs;
	// end defect 10717

	private final static long serialVersionUID = -7977817155640979045L;
	/**
	 * Constructor SecurityLogData()
	 * 
	 */
	public SecurityLogData()
	{
	}
	/**
	 * Constructor SecurityLogData w/ SecurityData parm
	 * 
	 * @param aaSecurityData SecurityData
	 */
	public SecurityLogData(SecurityData aaSecurityData)
	{
		if (aaSecurityData != null)
		{
			setAcctAccs(aaSecurityData.getAcctAccs());
			setAddrChngAccs(aaSecurityData.getAddrChngAccs());
			setAdjSalesTaxAccs(aaSecurityData.getAdjSalesTaxAccs());
			setAdminAccs(aaSecurityData.getAdminAccs());
			setBndedTtlCdAccs(aaSecurityData.getBndedTtlCdAccs());
			setCancRegAccs(aaSecurityData.getCancRegAccs());
			setCashOperAccs(aaSecurityData.getCashOperAccs());
			setCCOAccs(aaSecurityData.getCCOAccs());
			setCntyRptsAccs(aaSecurityData.getCntyRptsAccs());
			setCOAAccs(aaSecurityData.getCCOAccs());
			setCorrTtlRejAccs(aaSecurityData.getCorrTtlRejAccs());
			setCrdtCardFeeAccs(aaSecurityData.getCrdtCardFeeAccs());
			setCustServAccs(aaSecurityData.getCustServAccs());
			setDelTtlInProcsAccs(aaSecurityData.getDelTtlInProcsAccs());
			setDlrAccs(aaSecurityData.getDlrAccs());
			setDlrTtlAccs(aaSecurityData.getDlrTtlAccs());
			setDsabldPersnAccs(aaSecurityData.getDsabldPersnAccs());
			setDuplAccs(aaSecurityData.getDuplAccs());
			setEmpFirstName(aaSecurityData.getEmpFirstName());
			setEmpId(aaSecurityData.getEmpId());
			setUserName(aaSecurityData.getUserName()); // Defect 6445 
			setEmpLastName(aaSecurityData.getEmpLastName());
			setEmpMI(aaSecurityData.getEmpMI());
			setEmpSecrtyAccs(aaSecurityData.getEmpSecrtyAccs());
			setEmpSecrtyRptAccs(aaSecurityData.getEmpSecrtyRptAccs());
			setExchAccs(aaSecurityData.getExchAccs());
			// defect 8900
			setExmptAuditRptAccs(aaSecurityData.getExmptAuditRptAccs());
			setExmptAuthAccs(aaSecurityData.getExmptAuthAccs());
			// end defect 8900
			setFundsAdjAccs(aaSecurityData.getFundsAdjAccs());
			setFundsBalAccs(aaSecurityData.getFundsBalAccs());
			setFundsInqAccs(aaSecurityData.getFundsInqAccs());
			setFundsMgmtAccs(aaSecurityData.getFundsMgmtAccs());
			setFundsRemitAccs(aaSecurityData.getFundsRemitAccs());
			setHotCkCrdtAccs(aaSecurityData.getHotCkCrdtAccs());
			setHotCkRedemdAccs(aaSecurityData.getHotCkRedemdAccs());
			setInqAccs(aaSecurityData.getInqAccs());
			setInvAccs(aaSecurityData.getInvAccs());
			setInvAckAccs(aaSecurityData.getInvAckAccs());
			setInvActionAccs(aaSecurityData.getInvActionAccs());
			setInvAllocAccs(aaSecurityData.getInvAllocAccs());
			setInvDelAccs(aaSecurityData.getInvDelAccs());
			setInvHldRlseAccs(aaSecurityData.getInvHldRlseAccs());
			setInvInqAccs(aaSecurityData.getInvInqAccs());
			setInvProfileAccs(aaSecurityData.getInvProfileAccs());
			setItmSeizdAccs(aaSecurityData.getItmSeizdAccs());
			setItrntRenwlAccs(aaSecurityData.getItrntRenwlAccs());
			setJnkAccs(aaSecurityData.getJnkAccs());
			setLegalRestrntNoAccs(
				aaSecurityData.getLegalRestrntNoAccs());
			setLienHldrAccs(aaSecurityData.getLienHldrAccs());
			setLocalOptionsAccs(aaSecurityData.getLocalOptionsAccs());
			setMailRtrnAccs(aaSecurityData.getMailRtrnAccs());
			setMiscRegAccs(aaSecurityData.getMiscRegAccs());
			setMiscRemksAccs(aaSecurityData.getMiscRemksAccs());
			setModfyAccs(aaSecurityData.getModfyAccs());
			setModfyHotCkAccs(aaSecurityData.getModfyHotCkAccs());
			// defect 10844 
			setModfyTimedPrmtAccs(
				aaSecurityData.getModfyTimedPrmtAccs());
			// end defect 10844  
			setNonResPrmtAccs(aaSecurityData.getNonResPrmtAccs());
			setOfcIssuanceNo(aaSecurityData.getOfcIssuanceNo());
			setPltNoAccs(aaSecurityData.getPltNoAccs());
			setPrntImmedAccs(aaSecurityData.getPrntImmedAccs());
			setRefAccs(aaSecurityData.getRefAccs());
			setRegnlColltnAccs(aaSecurityData.getRegnlColltnAccs());
			setRegOnlyAccs(aaSecurityData.getRegOnlyAccs());
			setRegRefAmtAccs(aaSecurityData.getRegRefAmtAccs());
			setRenwlAccs(aaSecurityData.getRenwlAccs());
			setReplAccs(aaSecurityData.getReplAccs());
			setReprntRcptAccs(aaSecurityData.getReprntRcptAccs());
			setReprntRptAccs(aaSecurityData.getReprntRptAccs());
			setRgstrByAccs(aaSecurityData.getRgstrByAccs());
			setRptsAccs(aaSecurityData.getRptsAccs());
			setRSPSUpdtAccs(aaSecurityData.getRSPSUpdtAccs());
			setSalvAccs(aaSecurityData.getSalvAccs());
			setSecrtyAccs(aaSecurityData.getSecrtyAccs());
			setStatusChngAccs(aaSecurityData.getStatusChngAccs());
			setStlnSRSAccs(aaSecurityData.getStlnSRSAccs());
			setSubconAccs(aaSecurityData.getSubconAccs());
			setSubconRenwlAccs(aaSecurityData.getSubconRenwlAccs());
			setSubstaId(aaSecurityData.getSubstaId());
			setTempAddlWtAccs(aaSecurityData.getTempAddlWtAccs());
			setTimedPrmtAccs(aaSecurityData.getTimedPrmtAccs());
			setTowTrkAccs(aaSecurityData.getTowTrkAccs());
			setTtlApplAccs(aaSecurityData.getTtlApplAccs());
			setTtlRegAccs(aaSecurityData.getTtlRegAccs());
			setTtlRevkdAccs(aaSecurityData.getTtlRevkdAccs());
			setTtlSurrAccs(aaSecurityData.getTtlSurrAccs());
			setVoidTransAccs(aaSecurityData.getVoidTransAccs());
			setIssueDrvsEdAccs(aaSecurityData.getIssueDrvsEdAccs());
			// defect 9085 
			setSpclPltAccs(aaSecurityData.getSpclPltAccs());
			setSpclPltApplAccs(aaSecurityData.getSpclPltApplAccs());
			setSpclPltRenwPltAccs(
				aaSecurityData.getSpclPltRenwPltAccs());
			setSpclPltRevisePltAccs(
				aaSecurityData.getSpclPltRevisePltAccs());
			setSpclPltResrvPltAccs(
				aaSecurityData.getSpclPltResrvPltAccs());
			setSpclPltUnAccptblPltAccs(
				aaSecurityData.getSpclPltUnAccptblPltAccs());
			setSpclPltDelPltAccs(aaSecurityData.getSpclPltDelPltAccs());
			setSpclPltRptsAccs(aaSecurityData.getSpclPltRptsAccs());
			// end defect 9085
			// defect 9710
			setDlrRptAccs(aaSecurityData.getDlrRptAccs());
			setLienHldrRptAccs(aaSecurityData.getLienHldrRptAccs());
			setSubconRptAccs(aaSecurityData.getSubconRptAccs());
			// end defect 9710
			// defect 9831 
			setDsabldPlcrdRptAccs(
				aaSecurityData.getDsabldPlcrdRptAccs());
			setDsabldPlcrdInqAccs(
				aaSecurityData.getDsabldPlcrdInqAccs());
			// end defect 9831  
			// defect 9960 
			setETtlRptAccs(aaSecurityData.getETtlRptAccs());
			// end defect 9960  
			// defect 10153
			setPrivateLawEnfVehAccs(
				aaSecurityData.getPrivateLawEnfVehAccs());
			// end defect 10153
			//defect 10717
			setWebAgntAccs(aaSecurityData.getWebAgntAccs());
			// end defect 10717

			// defect 10701 
			setBatchRptMgmtAccs(aaSecurityData.getBatchRptMgmtAccs());
			// end defect 10701 
			
			// defect 11231 
			setExportAccs(aaSecurityData.getExportAccs());
			setDsabldPlcrdReInstateAccs(aaSecurityData.getDsabldPlcrdReInstateAccs()); 
			// end defect 11231 
		}
	}

	/**
	 * Returns the value of AcctAccs
	 * 
	 * @return  int 
	 */
	public final int getAcctAccs()
	{
		return ciAcctAccs;
	}

	/**
	 * Returns the value of AddrChngAccs
	 * 
	 * @return  int 
	 */
	public final int getAddrChngAccs()
	{
		return ciAddrChngAccs;
	}

	/**
	 * Returns the value of AdjSalesTaxAccs
	 * 
	 * @return  int 
	 */
	public final int getAdjSalesTaxAccs()
	{
		return ciAdjSalesTaxAccs;
	}

	/**
	 * Returns the value of AdminAccs
	 * 
	 * @return  int 
	 */
	public final int getAdminAccs()
	{
		return ciAdminAccs;
	}

	/**
	 * Get value of ciBatchRptMgmtAccs
	 * 
	 * @return int
	 */
	public int getBatchRptMgmtAccs()
	{
		return ciBatchRptMgmtAccs;
	}

	/**
	 * Returns the value of BndedTtlCdAccs
	 * 
	 * @return  int 
	 */
	public final int getBndedTtlCdAccs()
	{
		return ciBndedTtlCdAccs;
	}

	/**
	 * Returns the value of CancRegAccs
	 * 
	 * @return  int 
	 */
	public final int getCancRegAccs()
	{
		return ciCancRegAccs;
	}

	/**
	 * Returns the value of CashOperAccs
	 * 
	 * @return  int 
	 */
	public final int getCashOperAccs()
	{
		return ciCashOperAccs;
	}

	/**
	 * Returns the value of CCOAccs
	 * 
	 * @return  int 
	 */
	public final int getCCOAccs()
	{
		return ciCCOAccs;
	}

	/**
	 * Returns the value of CntyRptsAccs
	 * 
	 * @return  int 
	 */
	public final int getCntyRptsAccs()
	{
		return ciCntyRptsAccs;
	}

	/**
	 * Returns the value of COAAccs
	 * 
	 * @return  int 
	 */
	public final int getCOAAccs()
	{
		return ciCOAAccs;
	}

	/**
	 * Returns the value of CorrTtlRejAccs
	 * 
	 * @return  int 
	 */
	public final int getCorrTtlRejAccs()
	{
		return ciCorrTtlRejAccs;
	}

	/**
	 * Returns the value of CrdtCardFeeAccs
	 * 
	 * @return  int 
	 */
	public final int getCrdtCardFeeAccs()
	{
		return ciCrdtCardFeeAccs;
	}

	/**
	 * Returns the value of CustServAccs
	 * 
	 * @return  int 
	 */
	public final int getCustServAccs()
	{
		return ciCustServAccs;
	}

	/**
	 * Returns the value of DelTtlInProcsAccs
	 * 
	 * @return  int 
	 */
	public final int getDelTtlInProcsAccs()
	{
		return ciDelTtlInProcsAccs;
	}

	/**
	 * Returns the value of DlrAccs
	 * 
	 * @return  int 
	 */
	public final int getDlrAccs()
	{
		return ciDlrAccs;
	}

	/**
	 * Returns value of DlrRptAccs
	 * 
	 * @return int 
	 */
	public int getDlrRptAccs()
	{
		return ciDlrRptAccs;
	}

	/**
	 * Returns the value of DlrTtlAccs
	 * 
	 * @return  int 
	 */
	public final int getDlrTtlAccs()
	{
		return ciDlrTtlAccs;
	}

	/**
	 * Returns the value of DsabldPersnAccs
	 * 
	 * @return  int 
	 */
	public final int getDsabldPersnAccs()
	{
		return ciDsabldPersnAccs;
	}

	/**
	 * Return value of ciDsabldPlcrdInqAccs
	 * 
	 * @return int
	 */
	public int getDsabldPlcrdInqAccs()
	{
		return ciDsabldPlcrdInqAccs;
	}

	/**
	 * Get value of DsabldPlcrdReInstateAccs
	 * 
	 * @return int
	 */
	public int getDsabldPlcrdReInstateAccs()
	{
		return ciDsabldPlcrdReInstateAccs;
	}

	/**
	 * Get value of DsabldPlcrdRptAccs
	 * 
	 * @return int
	 */
	public int getDsabldPlcrdRptAccs()
	{
		return ciDsabldPlcrdRptAccs;
	}

	/**
	 * Returns the value of DuplAccs
	 * 
	 * @return  int 
	 */
	public final int getDuplAccs()
	{
		return ciDuplAccs;
	}

	/**
	 * Returns the value of EmpFirstName
	 * 
	 * @return  String 
	 */
	public final String getEmpFirstName()
	{
		return csEmpFirstName;
	}

	/**
	 * Returns the value of EmpId
	 * 
	 * @return  String 
	 */
	public final String getEmpId()
	{
		return csEmpId;
	}

	/**
	 * Returns the value of EmpLastName
	 * 
	 * @return  String 
	 */
	public final String getEmpLastName()
	{
		return csEmpLastName;
	}

	/**
	 * Returns the value of EmpMI
	 * 
	 * @return  String 
	 */
	public final String getEmpMI()
	{
		return csEmpMI;
	}

	/**
	 * Returns the value of EmpSecrtyAccs
	 * 
	 * @return  int 
	 */
	public final int getEmpSecrtyAccs()
	{
		return ciEmpSecrtyAccs;
	}

	/**
	 * Returns the value of EmpSecrtyRptAccs
	 * 
	 * @return  int 
	 */
	public final int getEmpSecrtyRptAccs()
	{
		return ciEmpSecrtyRptAccs;
	}
	/**
	 * Returns the value of ETtlRptAccs
	 * 
	 * @return  int 
	 */
	public final int getETtlRptAccs()
	{
		return ciETtlRptAccs;
	}
	/**
	 * Returns the value of ExchAccs
	 * 
	 * @return  int 
	 */
	public final int getExchAccs()
	{
		return ciExchAccs;
	}

	/**
	 * Returns the value of ExmptAuditRptAccs
	 * @return  int 
	 */
	public final int getExmptAuditRptAccs()
	{
		return ciExmptAuditRptAccs;
	}

	/**
	 * Returns the value of ExmptAuthAccs
	 * @return  int 
	 */
	public final int getExmptAuthAccs()
	{
		return ciExmptAuthAccs;
	}

	/**
	 * Returns the value of ciExportAccs
	 * @return  int 
	 */
	public final int getExportAccs()
	{
		return ciExportAccs;
	}

	/**
	 * Returns the value of FundsAdjAccs
	 * 
	 * @return  int 
	 */
	public final int getFundsAdjAccs()
	{
		return ciFundsAdjAccs;
	}

	/**
	 * Returns the value of FundsBalAccs
	 * 
	 * @return  int 
	 */
	public final int getFundsBalAccs()
	{
		return ciFundsBalAccs;
	}

	/**
	 * Returns the value of FundsInqAccs
	 * 
	 * @return  int 
	 */
	public final int getFundsInqAccs()
	{
		return ciFundsInqAccs;
	}

	/**
	 * Returns the value of FundsMgmtAccs
	 * 
	 * @return  int 
	 */
	public final int getFundsMgmtAccs()
	{
		return ciFundsMgmtAccs;
	}

	/**
	 * Returns the value of FundsRemitAccs
	 * 
	 * @return  int 
	 */
	public final int getFundsRemitAccs()
	{
		return ciFundsRemitAccs;
	}

	/**
	 * Returns the value of HotCkCrdtAccs
	 * 
	 * @return  int 
	 */
	public final int getHotCkCrdtAccs()
	{
		return ciHotCkCrdtAccs;
	}

	/**
	 * Returns the value of HotCkRedemdAccs
	 * 
	 * @return  int 
	 */
	public final int getHotCkRedemdAccs()
	{
		return ciHotCkRedemdAccs;
	}

	/**
	 * Returns the value of InqAccs
	 * 
	 * @return  int 
	 */
	public final int getInqAccs()
	{
		return ciInqAccs;
	}

	/**
	 * Returns the value of InvAccs
	 * 
	 * @return  int 
	 */
	public final int getInvAccs()
	{
		return ciInvAccs;
	}

	/**
	 * Returns the value of InvAckAccs
	 * 
	 * @return  int 
	 */
	public final int getInvAckAccs()
	{
		return ciInvAckAccs;
	}

	/**
	 * Returns the value of InvActionAccs
	 * 
	 * @return  int 
	 */
	public final int getInvActionAccs()
	{
		return ciInvActionAccs;
	}

	/**
	 * Returns the value of InvAllocAccs
	 * 
	 * @return  int 
	 */
	public final int getInvAllocAccs()
	{
		return ciInvAllocAccs;
	}

	/**
	 * Returns the value of InvDelAccs
	 * 
	 * @return  int 
	 */
	public final int getInvDelAccs()
	{
		return ciInvDelAccs;
	}

	/**
	 * Returns the value of InvHldRlseAccs
	 * 
	 * @return  int 
	 */
	public final int getInvHldRlseAccs()
	{
		return ciInvHldRlseAccs;
	}

	/**
	 * Returns the value of InvInqAccs
	 * 
	 * @return  int 
	 */
	public final int getInvInqAccs()
	{
		return ciInvInqAccs;
	}

	/**
	 * Returns the value of InvProfileAccs
	 * 
	 * @return  int 
	 */
	public final int getInvProfileAccs()
	{
		return ciInvProfileAccs;
	}

	/**
	 * Returns the value of IssueDrvsEdAccs
	 * 
	 * @return int
	 */
	public int getIssueDrvsEdAccs()
	{
		return ciIssueDrvsEdAccs;
	}

	/**
	 * Returns the value of ItmSeizdAccs
	 * 
	 * @return  int 
	 */
	public final int getItmSeizdAccs()
	{
		return ciItmSeizdAccs;
	}

	/**
	 * Returns the value of ItrntRenwlAccs
	 * 
	 * @return int
	 */
	public int getItrntRenwlAccs()
	{
		return ciItrntRenwlAccs;
	}

	/**
	 * Returns the value of JnkAccs
	 * 
	 * @return  int 
	 */
	public final int getJnkAccs()
	{
		return ciJnkAccs;
	}

	/**
	 * Returns the value of LegalRestrntNoAccs
	 * 
	 * @return  int 
	 */
	public final int getLegalRestrntNoAccs()
	{
		return ciLegalRestrntNoAccs;
	}

	/**
	 * Returns the value of LienHldrAccs
	 * 
	 * @return  int 
	 */
	public final int getLienHldrAccs()
	{
		return ciLienHldrAccs;
	}

	/**
	 * Returns value of LienHldrRptAccs
	 * 
	 * @return int 
	 */
	public int getLienHldrRptAccs()
	{
		return ciLienHldrRptAccs;
	}

	/**
	 * Returns the value of LocalOptionsAccs
	 * 
	 * @return  int 
	 */
	public final int getLocalOptionsAccs()
	{
		return ciLocalOptionsAccs;
	}

	/**
	 * Returns the value of MailRtrnAccs
	 * 
	 * @return  int 
	 */
	public final int getMailRtrnAccs()
	{
		return ciMailRtrnAccs;
	}

	/**
	 * Returns the value of MiscRegAccs
	 * 
	 * @return  int 
	 */
	public final int getMiscRegAccs()
	{
		return ciMiscRegAccs;
	}

	/**
	 * Returns the value of MiscRemksAccs
	 * 
	 * @return  int 
	 */
	public final int getMiscRemksAccs()
	{
		return ciMiscRemksAccs;
	}

	/**
	 * Returns the value of ModfyAccs
	 * 
	 * @return  int 
	 */
	public final int getModfyAccs()
	{
		return ciModfyAccs;
	}

	/**
	 * Returns the value of ModfyHotCkAccs
	 * 
	 * @return  int 
	 */
	public final int getModfyHotCkAccs()
	{
		return ciModfyHotCkAccs;
	}

	/**
	 * Get value of ciModfyTimedPrmtAccs
	 * 
	 * @return int
	 */
	public int getModfyTimedPrmtAccs()
	{
		return ciModfyTimedPrmtAccs;
	}

	/**
	 * Returns the value of NonResPrmtAccs
	 * 
	 * @return  int 
	 */
	public final int getNonResPrmtAccs()
	{
		return ciNonResPrmtAccs;
	}

	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return  int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Returns the value of PltNoAccs
	 * 
	 * @return  int 
	 */
	public final int getPltNoAccs()
	{
		return ciPltNoAccs;
	}

	/**
	 * Return value of ciPrivateLawEnfVehAccs
	 * 
	 * @return int
	 */
	public int getPrivateLawEnfVehAccs()
	{
		return ciPrivateLawEnfVehAccs;
	}

	/**
	 * Returns the value of PrntImmedAccs
	 * 
	 * @return  int 
	 */
	public final int getPrntImmedAccs()
	{
		return ciPrntImmedAccs;
	}

	/**
	 * Returns the value of RefAccs
	 * 
	 * @return  int 
	 */
	public final int getRefAccs()
	{
		return ciRefAccs;
	}

	/**
	 * Returns the value of RegnlColltnAccs
	 * 
	 * @return int
	 */
	public int getRegnlColltnAccs()
	{
		return ciRegnlColltnAccs;
	}

	/**
	 * Returns the value of RegOnlyAccs
	 * 
	 * @return  int 
	 */
	public final int getRegOnlyAccs()
	{
		return ciRegOnlyAccs;
	}

	/**
	 * Returns the value of RegRefAmtAccs
	 * 
	 * @return  int 
	 */
	public final int getRegRefAmtAccs()
	{
		return ciRegRefAmtAccs;
	}

	/**
	 * Returns the value of RenwlAccs
	 * 
	 * @return  int 
	 */
	public final int getRenwlAccs()
	{
		return ciRenwlAccs;
	}

	/**
	 * Returns the value of ReplAccs
	 * 
	 * @return  int 
	 */
	public final int getReplAccs()
	{
		return ciReplAccs;
	}

	/**
	 * Returns the value of ReprntRcptAccs
	 * 
	 * @return  int 
	 */
	public final int getReprntRcptAccs()
	{
		return ciReprntRcptAccs;
	}

	/**
	 * Returns the value of ReprntRptAccs
	 * 
	 * @return  int 
	 */
	public final int getReprntRptAccs()
	{
		return ciReprntRptAccs;
	}

	/**
	 * Returns the value of ReprntStkrRptAccs
	 * 
	 * @return int
	 */
	public int getReprntStkrRptAccs()
	{
		return ciReprntStkrRptAccs;
	}

	/**
	 * Returns the value of RgstrByAccs
	 * 
	 * @return  int 
	 */
	public final int getRgstrByAccs()
	{
		return ciRgstrByAccs;
	}

	/**
	 * Returns the value of RptsAccs
	 * 
	 * @return  int 
	 */
	public final int getRptsAccs()
	{
		return ciRptsAccs;
	}

	/**
	 * Returns the value of RSPSUpdtAccs
	 * 
	 * @return  int 
	 */
	public final int getRSPSUpdtAccs()
	{
		return ciRSPSUpdtAccs;
	}

	/**
	 * Returns the value of SalvAccs
	 * 
	 * @return  int 
	 */
	public final int getSalvAccs()
	{
		return ciSalvAccs;
	}

	/**
	 * Returns the value of SecrtyAccs
	 * 
	 * @return  int 
	 */
	public final int getSecrtyAccs()
	{
		return ciSecrtyAccs;
	}

	/**
	 * Returns value of ciSpclPltAccs
	 * 
	 * @return int
	 */
	public int getSpclPltAccs()
	{
		return ciSpclPltAccs;
	}

	/**
	 * Returns value of ciSpclPltApplAccs
	 * 
	 * @return int
	 */
	public int getSpclPltApplAccs()
	{
		return ciSpclPltApplAccs;
	}

	/**
	 * Returns value of ciSpclPltDelPltAccs
	 * 
	 * @return int
	 */
	public int getSpclPltDelPltAccs()
	{
		return ciSpclPltDelPltAccs;
	}

	/**
	 * Returns value of ciSpclPltRenwPltAccs 
	 * 
	 * @return int
	 */
	public int getSpclPltRenwPltAccs()
	{
		return ciSpclPltRenwPltAccs;
	}

	/**
	 * Returns value of ciSpclPltResrvPltAccs
	 * 
	 * @return int
	 */
	public int getSpclPltResrvPltAccs()
	{
		return ciSpclPltResrvPltAccs;
	}

	/**
	 * Returns value of ciSpclPltRevisePltAccs
	 * 
	 * @return int
	 */
	public int getSpclPltRevisePltAccs()
	{
		return ciSpclPltRevisePltAccs;
	}

	/**
	 * Returns value of ciSpclPltRptsAccs
	 * 
	 * @return int
	 */
	public int getSpclPltRptsAccs()
	{
		return ciSpclPltRptsAccs;
	}

	/**
	 * Returns value of ciSpclPltUnAccptblPltAccs
	 * 
	 * @return int
	 */
	public int getSpclPltUnAccptblPltAccs()
	{
		return ciSpclPltUnAccptblPltAccs;
	}

	/**
	 * Returns the value of StatusChngAccs
	 * 
	 * @return  int 
	 */
	public final int getStatusChngAccs()
	{
		return ciStatusChngAccs;
	}

	/**
	 * Returns the value of StlnSRSAccs
	 * 
	 * @return  int 
	 */
	public final int getStlnSRSAccs()
	{
		return ciStlnSRSAccs;
	}

	/**
	 * Returns the value of SubconAccs
	 * 
	 * @return  int 
	 */
	public final int getSubconAccs()
	{
		return ciSubconAccs;
	}

	/**
	 * Returns the value of SubconRenwlAccs
	 * 
	 * @return  int 
	 */
	public final int getSubconRenwlAccs()
	{
		return ciSubconRenwlAccs;
	}

	/**
	 * Returns value of SubconRptAccs
	 * 
	 * @return int 
	 */
	public int getSubconRptAccs()
	{
		return ciSubconRptAccs;
	}

	/**
	 * Returns the value of SubstaId
	 * 
	 * @return  int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}
	/**
	 * Returns the value of TempAddlWtAccs
	 * 
	 * @return  int 
	 */
	public final int getTempAddlWtAccs()
	{
		return ciTempAddlWtAccs;
	}

	/**
	 * Returns the value of TimedPrmtAccs
	 * 
	 * @return  int 
	 */
	public final int getTimedPrmtAccs()
	{
		return ciTimedPrmtAccs;
	}
	/**
	 * Returns the value of TowTrkAccs
	 * 
	 * @return  int 
	 */
	public final int getTowTrkAccs()
	{
		return ciTowTrkAccs;
	}

	/**
	 * Returns the value of TransAMDate
	 * 
	 * @return  int 
	 */
	public final int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Returns the value of TransTime
	 * 
	 * @return  int 
	 */
	public final int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Returns the value of TransWsId
	 * 
	 * @return  int 
	 */
	public final int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Returns the value of TtlApplAccs
	 * 
	 * @return  int 
	 */
	public final int getTtlApplAccs()
	{
		return ciTtlApplAccs;
	}

	/**
	 * Returns the value of TtlRegAccs
	 * 
	 * @return  int 
	 */
	public final int getTtlRegAccs()
	{
		return ciTtlRegAccs;
	}

	/**
	 * Returns the value of TtlRevkdAccs
	 * 
	 * @return  int 
	 */
	public final int getTtlRevkdAccs()
	{
		return ciTtlRevkdAccs;
	}

	/**
	 * Returns the value of TtlSurrAccs
	 * 
	 * @return  int 
	 */
	public final int getTtlSurrAccs()
	{
		return ciTtlSurrAccs;
	}

	/**
	 * Returns the value of UpdtActn
	 * 
	 * @return  String 
	 */
	public final String getUpdtActn()
	{
		return csUpdtActn;
	}

	/**
	 * Returns the value of UpdtngEmpId
	 * 
	 * @return  String 
	 */
	public final String getUpdtngEmpId()
	{
		return csUpdtngEmpId;
	}

	/**
	 * Returns the value of UserName
	 * 
	 * @return  String 
	 */
	public final String getUserName()
	{
		return csUserName;
	}

	/**
	 * Returns the value of VoidTransAccs
	 * 
	 * @return  int 
	 */
	public final int getVoidTransAccs()
	{
		return ciVoidTransAccs;
	}

	/**
	 * Returns the value of WebAgntAccs
	 * @return  int 
	 */
	public final int getWebAgntAccs()
	{
		return ciWebAgntAccs;
	}
	/** 
	 * Return boolean to donote if from HQ
	 * 
	 * @return boolean 
	 */
	public final boolean isHQ()
	{
		return OfficeIdsCache.isHQ(ciOfcIssuanceNo);
	}

	/** 
	 * Return boolean to donote if from Region
	 * 
	 * @return boolean 
	 */
	public final boolean isRegion()
	{
		return OfficeIdsCache.isRegion(ciOfcIssuanceNo);
	}

	/**
	 * This method sets the value of AcctAccs.
	 * 
	 * @param aiAcctAccs   int 
	 */
	public final void setAcctAccs(int aiAcctAccs)
	{
		ciAcctAccs = aiAcctAccs;
	}

	/**
	 * This method sets the value of AddrChngAccs.
	 * 
	 * @param aiAddrChngAccs   int 
	 */
	public final void setAddrChngAccs(int aiAddrChngAccs)
	{
		ciAddrChngAccs = aiAddrChngAccs;
	}

	/**
	 * This method sets the value of AdjSalesTaxAccs.
	 * 
	 * @param aiAdjSalesTaxAccs   int 
	 */
	public final void setAdjSalesTaxAccs(int aiAdjSalesTaxAccs)
	{
		ciAdjSalesTaxAccs = aiAdjSalesTaxAccs;
	}

	/**
	 * This method sets the value of AdminAccs.
	 * 
	 * @param aiAdminAccs   int 
	 */
	public final void setAdminAccs(int aiAdminAccs)
	{
		ciAdminAccs = aiAdminAccs;
	}

	/**
	 * Set value of ciBatchRptMgmtAccs
	 * 
	 * @param aiBatchRptMgmtAccs
	 */
	public void setBatchRptMgmtAccs(int aiBatchRptMgmtAccs)
	{
		ciBatchRptMgmtAccs = aiBatchRptMgmtAccs;
	}

	/**
	 * This method sets the value of BndedTtlCdAccs.
	 * 
	 * @param aiBndedTtlCdAccs   int 
	 */
	public final void setBndedTtlCdAccs(int aiBndedTtlCdAccs)
	{
		ciBndedTtlCdAccs = aiBndedTtlCdAccs;
	}

	/**
	 * This method sets the value of CancRegAccs.
	 * 
	 * @param aiCancRegAccs   int 
	 */
	public final void setCancRegAccs(int aiCancRegAccs)
	{
		ciCancRegAccs = aiCancRegAccs;
	}

	/**
	 * This method sets the value of CashOperAccs.
	 * 
	 * @param aiCashOperAccs   int 
	 */
	public final void setCashOperAccs(int aiCashOperAccs)
	{
		ciCashOperAccs = aiCashOperAccs;
	}

	/**
	 * This method sets the value of CCOAccs.
	 * 
	 * @param aiCCOAccs   int 
	 */
	public final void setCCOAccs(int aiCCOAccs)
	{
		ciCCOAccs = aiCCOAccs;
	}

	/**
	 * This method sets the value of CntyRptsAccs.
	 * 
	 * @param aiCntyRptsAccs   int 
	 */
	public final void setCntyRptsAccs(int aiCntyRptsAccs)
	{
		ciCntyRptsAccs = aiCntyRptsAccs;
	}

	/**
	 * This method sets the value of COAAccs.
	 * 
	 * @param aiCOAAccs   int 
	 */
	public final void setCOAAccs(int aiCOAAccs)
	{
		ciCOAAccs = aiCOAAccs;
	}

	/**
	 * This method sets the value of CorrTtlRejAccs.
	 * 
	 * @param aiCorrTtlRejAccs   int 
	 */
	public final void setCorrTtlRejAccs(int aiCorrTtlRejAccs)
	{
		ciCorrTtlRejAccs = aiCorrTtlRejAccs;
	}

	/**
	* This method sets the value of CrdtCardFeeAccs
	* 
	* @param aiCrdtCardFeeAccs   int 
	*/
	public final void setCrdtCardFeeAccs(int aiCrdtCardFeeAccs)
	{
		ciCrdtCardFeeAccs = aiCrdtCardFeeAccs;
	}

	/**
	 * This method sets the value of CustServAccs.
	 * 
	 * @param aiCustServAccs   int 
	 */
	public final void setCustServAccs(int aiCustServAccs)
	{
		ciCustServAccs = aiCustServAccs;
	}
	
	/**
	 * This method sets the value of DelTtlInProcsAccs.
	 * 
	 * @param aiDelTtlInProcsAccs   int 
	 */
	public final void setDelTtlInProcsAccs(int aiDelTtlInProcsAccs)
	{
		ciDelTtlInProcsAccs = aiDelTtlInProcsAccs;
	}
	/**
	 * This method sets the value of DlrAccs.
	 * 
	 * @param aiDlrAccs   int 
	 */
	public final void setDlrAccs(int aiDlrAccs)
	{
		ciDlrAccs = aiDlrAccs;
	}

	/**
	 * Sets value of DlrRptAccs
	 * 
	 * @param aiDlrRptAccs
	 */
	public void setDlrRptAccs(int aiDlrRptAccs)
	{
		ciDlrRptAccs = aiDlrRptAccs;
	}

	/**
	 * This method sets the value of DlrTtlAccs.
	 * 
	 * @param aiDlrTtlAccs   int 
	 */
	public final void setDlrTtlAccs(int aiDlrTtlAccs)
	{
		ciDlrTtlAccs = aiDlrTtlAccs;
	}

	/**
	 * This method sets the value of DsabldPersnAccs.
	 * 
	 * @param aiDsabldPersnAccs   int 
	 */
	public final void setDsabldPersnAccs(int aiDsabldPersnAccs)
	{
		ciDsabldPersnAccs = aiDsabldPersnAccs;
	}

	/**
	 * Set value of ciDsabldPlcrdInqAccs
	 * 
	 * @param aiDsabldPlcrdInqAccs
	 */
	public void setDsabldPlcrdInqAccs(int aiDsabldPlcrdInqAccs)
	{
		ciDsabldPlcrdInqAccs = aiDsabldPlcrdInqAccs;
	}

	/**
	 *  Set value of DsabldPlcrdReInstateAccs
	 * 
	 * @param aiDsabldPlcrdReInstateAccs 
	 */
	public void setDsabldPlcrdReInstateAccs(int aiDsabldPlcrdReInstateAccs)
	{
		ciDsabldPlcrdReInstateAccs =aiDsabldPlcrdReInstateAccs;
	}

	/**
	 * Set value of DsabldPlcrdRptAccs
	 * 
	 * @param aiDsabldPlcrdRptAccs
	 */
	public void setDsabldPlcrdRptAccs(int aiDsabldPlcrdRptAccs)
	{
		ciDsabldPlcrdRptAccs = aiDsabldPlcrdRptAccs;
	}

	/**
	 * This method sets the value of DuplAccs.
	 * 
	 * @param aiDuplAccs   int 
	 */
	public final void setDuplAccs(int aiDuplAccs)
	{
		ciDuplAccs = aiDuplAccs;
	}

	/**
	 * This method sets the value of EmpFirstName.
	 * 
	 * @param asEmpFirstName   String 
	 */
	public final void setEmpFirstName(String asEmpFirstName)
	{
		csEmpFirstName = asEmpFirstName;
	}

	/**
	 * This method sets the value of EmpId.
	 * 
	 * @param asEmpId   String 
	 */
	public final void setEmpId(String asEmpId)
	{
		csEmpId = asEmpId;
	}

	/**
	 * This method sets the value of EmpLastName.
	 * 
	 * @param asEmpLastName   String 
	 */
	public final void setEmpLastName(String asEmpLastName)
	{
		csEmpLastName = asEmpLastName;
	}

	/**
	 * This method sets the value of EmpMI.
	 * 
	 * @param asEmpMI   String 
	 */
	public final void setEmpMI(String asEmpMI)
	{
		csEmpMI = asEmpMI;
	}

	/**
	 * This method sets the value of EmpSecrtyAccs.
	 * 
	 * @param aiEmpSecrtyAccs   int 
	 */
	public final void setEmpSecrtyAccs(int aiEmpSecrtyAccs)
	{
		ciEmpSecrtyAccs = aiEmpSecrtyAccs;
	}

	/**
	 * This method sets the value of EmpSecrtyRptAccs.
	 * 
	 * @param aiEmpSecrtyRptAccs   int 
	 */
	public final void setEmpSecrtyRptAccs(int aiEmpSecrtyRptAccs)
	{
		ciEmpSecrtyRptAccs = aiEmpSecrtyRptAccs;
	}

	/**
	 * This method sets the value of ETtlRptAccs.
	 * 
	 * @param aiETtlRptAccs   int 
	 */
	public final void setETtlRptAccs(int aiETtlRptAccs)
	{
		ciETtlRptAccs = aiETtlRptAccs;
	}

	/**
	 * This method sets the value of ExchAccs.
	 * 
	 * @param aiExchAccs   int 
	 */
	public final void setExchAccs(int aiExchAccs)
	{
		ciExchAccs = aiExchAccs;
	}

	/**
	 * This method sets the value of ExmptAuditRptAccs.
	 * @param aiExmptAuditRptAccs   int 
	 */
	public final void setExmptAuditRptAccs(int aiExmptAuditRptAccs)
	{
		ciExmptAuditRptAccs = aiExmptAuditRptAccs;
	}

	/**
	 * This method sets the value of ExmptAuthAccs.
	 * @param aiExmptAuthAccs   int 
	 */
	public final void setExmptAuthAccs(int aiExmptAuthAccs)
	{
		ciExmptAuthAccs = aiExmptAuthAccs;
	}

	/**
	 * This method sets the value of ExportAccs.
	 * @param aiExportAccs   int 
	 */
	public final void setExportAccs(int aiExportAccs)
	{
		ciExportAccs = aiExportAccs;
	}

	/**
	 * This method sets the value of FundsAdjAccs.
	 * 
	 * @param aiFundsAdjAccs   int 
	 */
	public final void setFundsAdjAccs(int aiFundsAdjAccs)
	{
		ciFundsAdjAccs = aiFundsAdjAccs;
	}
	/**
	 * This method sets the value of FundsBalAccs.
	 * 
	 * @param aiFundsBalAccs   int 
	 */
	public final void setFundsBalAccs(int aiFundsBalAccs)
	{
		ciFundsBalAccs = aiFundsBalAccs;
	}

	/**
	 * This method sets the value of FundsInqAccs.
	 * 
	 * @param aiFundsInqAccs   int 
	 */
	public final void setFundsInqAccs(int aiFundsInqAccs)
	{
		ciFundsInqAccs = aiFundsInqAccs;
	}

	/**
	 * This method sets the value of FundsMgmtAccs.
	 * 
	 * @param aiFundsMgmtAccs   int 
	 */
	public final void setFundsMgmtAccs(int aiFundsMgmtAccs)
	{
		ciFundsMgmtAccs = aiFundsMgmtAccs;
	}

	/**
	 * This method sets the value of FundsRemitAccs.
	 * 
	 * @param aiFundsRemitAccs   int 
	 */
	public final void setFundsRemitAccs(int aiFundsRemitAccs)
	{
		ciFundsRemitAccs = aiFundsRemitAccs;
	}

	/**
	 * This method sets the value of HotCkCrdtAccs.
	 * 
	 * @param aiHotCkCrdtAccs   int 
	 */
	public final void setHotCkCrdtAccs(int aiHotCkCrdtAccs)
	{
		ciHotCkCrdtAccs = aiHotCkCrdtAccs;
	}

	/**
	 * This method sets the value of HotCkRedemdAccs.
	 * 
	 * @param aiHotCkRedemdAccs   int 
	 */
	public final void setHotCkRedemdAccs(int aiHotCkRedemdAccs)
	{
		ciHotCkRedemdAccs = aiHotCkRedemdAccs;
	}

	/**
	 * This method sets the value of InqAccs.
	 * 
	 * @param aiInqAccs   int 
	 */
	public final void setInqAccs(int aiInqAccs)
	{
		ciInqAccs = aiInqAccs;
	}

	/**
	 * This method sets the value of InvAccs.
	 * 
	 * @param aiInvAccs   int 
	 */
	public final void setInvAccs(int aiInvAccs)
	{
		ciInvAccs = aiInvAccs;
	}
	/**
	 * This method sets the value of InvAckAccs.
	 * 
	 * @param aiInvAckAccs   int 
	 */
	public final void setInvAckAccs(int aiInvAckAccs)
	{
		ciInvAckAccs = aiInvAckAccs;
	}
	/**
	 * This method sets the value of InvActionAccs.
	 * 
	 * @param aiInvActionAccs   int 
	 */
	public final void setInvActionAccs(int aiInvActionAccs)
	{
		ciInvActionAccs = aiInvActionAccs;
	}

	/**
	 * This method sets the value of InvAllocAccs.
	 * 
	 * @param aiInvAllocAccs   int 
	 */
	public final void setInvAllocAccs(int aiInvAllocAccs)
	{
		ciInvAllocAccs = aiInvAllocAccs;
	}

	/**
	 * This method sets the value of InvDelAccs.
	 * 
	 * @param aiInvDelAccs   int 
	 */
	public final void setInvDelAccs(int aiInvDelAccs)
	{
		ciInvDelAccs = aiInvDelAccs;
	}

	/**
	 * This method sets the value of InvHldRlseAccs.
	 * 
	 * @param aiInvHldRlseAccs   int 
	 */
	public final void setInvHldRlseAccs(int aiInvHldRlseAccs)
	{
		ciInvHldRlseAccs = aiInvHldRlseAccs;
	}

	/**
	 * This method sets the value of InvInqAccs.
	 * 
	 * @param aiInvInqAccs   int 
	 */
	public final void setInvInqAccs(int aiInvInqAccs)
	{
		ciInvInqAccs = aiInvInqAccs;
	}

	/**
	 * This method sets the value of InvProfileAccs.
	 * 
	 * @param aiInvProfileAccs   int 
	 */
	public final void setInvProfileAccs(int aiInvProfileAccs)
	{
		ciInvProfileAccs = aiInvProfileAccs;
	}

	/**
	 * This method sets the value of IssueDrvsEdAccs
	 * 
	 * @param aiIssueDrvsEdAccs int
	 */
	public void setIssueDrvsEdAccs(int aiIssueDrvsEdAccs)
	{
		ciIssueDrvsEdAccs = aiIssueDrvsEdAccs;
	}

	/**
	 * This method sets the value of ItmSeizdAccs.
	 * 
	 * @param aiItmSeizdAccs   int 
	 */
	public final void setItmSeizdAccs(int aiItmSeizdAccs)
	{
		ciItmSeizdAccs = aiItmSeizdAccs;
	}

	/**
	 * This method sets the value of 
	 * 
	 * @param aiItrntRenwlAccs int
	 */
	public void setItrntRenwlAccs(int aiItrntRenwlAccs)
	{
		ciItrntRenwlAccs = aiItrntRenwlAccs;
	}

	/**
	 * This method sets the value of JnkAccs.
	 * 
	 * @param aiJnkAccs   int 
	 */
	public final void setJnkAccs(int aiJnkAccs)
	{
		ciJnkAccs = aiJnkAccs;
	}

	/**
	 * This method sets the value of LegalRestrntNoAccs.
	 * 
	 * @param aiLegalRestrntNoAccs   int 
	 */
	public final void setLegalRestrntNoAccs(int aiLegalRestrntNoAccs)
	{
		ciLegalRestrntNoAccs = aiLegalRestrntNoAccs;
	}

	/**
	 * This method sets the value of LienHldrAccs.
	 * 
	 * @param aiLienHldrAccs   int 
	 */
	public final void setLienHldrAccs(int aiLienHldrAccs)
	{
		ciLienHldrAccs = aiLienHldrAccs;
	}

	/**
	 * Sets value of LienHldrRptAccs
	 * 
	 * @param aiLienHldrRptAccs
	 */
	public void setLienHldrRptAccs(int aiLienHldrRptAccs)
	{
		ciLienHldrRptAccs = aiLienHldrRptAccs;
	}

	/**
	 * This method sets the value of LocalOptionsAccs.
	 * 
	 * @param aiLocalOptionsAccs   int 
	 */
	public final void setLocalOptionsAccs(int aiLocalOptionsAccs)
	{
		ciLocalOptionsAccs = aiLocalOptionsAccs;
	}

	/**
	 * This method sets the value of MailRtrnAccs.
	 * 
	 * @param aiMailRtrnAccs   int 
	 */
	public final void setMailRtrnAccs(int aiMailRtrnAccs)
	{
		ciMailRtrnAccs = aiMailRtrnAccs;
	}

	/**
	 * This method sets the value of MiscRegAccs.
	 * 
	 * @param aiMiscRegAccs   int 
	 */
	public final void setMiscRegAccs(int aiMiscRegAccs)
	{
		ciMiscRegAccs = aiMiscRegAccs;
	}

	/**
	 * This method sets the value of MiscRemksAccs.
	 * 
	 * @param aiMiscRemksAccs   int 
	 */
	public final void setMiscRemksAccs(int aiMiscRemksAccs)
	{
		ciMiscRemksAccs = aiMiscRemksAccs;
	}

	/**
	 * This method sets the value of ModfyAccs.
	 * 
	 * @param aiModfyAccs   int 
	 */
	public final void setModfyAccs(int aiModfyAccs)
	{
		ciModfyAccs = aiModfyAccs;
	}

	/**
	 * This method sets the value of ModfyHotCkAccs.
	 * 
	 * @param aiModfyHotCkAccs   int 
	 */
	public final void setModfyHotCkAccs(int aiModfyHotCkAccs)
	{
		ciModfyHotCkAccs = aiModfyHotCkAccs;
	}

	/**
	 * Set value of ciModfyTimedPrmtAccs
	 * 
	 * @param aiModfyTimedPrmtAccs
	 */
	public void setModfyTimedPrmtAccs(int aiModfyTimedPrmtAccs)
	{
		ciModfyTimedPrmtAccs = aiModfyTimedPrmtAccs;
	}

	/**
	 * This method sets the value of NonResPrmtAccs.
	 * 
	 * @param aiNonResPrmtAccs   int 
	 */
	public final void setNonResPrmtAccs(int aiNonResPrmtAccs)
	{
		ciNonResPrmtAccs = aiNonResPrmtAccs;
	}

	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo   int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * This method sets the value of PltNoAccs.
	 * 
	 * @param aiPltNoAccs   int 
	 */
	public final void setPltNoAccs(int aiPltNoAccs)
	{
		ciPltNoAccs = aiPltNoAccs;
	}

	/**
	 * Set value of ciPrivateLawEnfVehAccs
	 * 
	 * @param aiPrivateLawEnfVehAccs
	 */
	public void setPrivateLawEnfVehAccs(int aiPrivateLawEnfVehAccs)
	{
		ciPrivateLawEnfVehAccs = aiPrivateLawEnfVehAccs;
	}

	/**
	 * This method sets the value of PrntImmedAccs.
	 * 
	 * @param aiPrntImmedAccs   int 
	 */
	public final void setPrntImmedAccs(int aiPrntImmedAccs)
	{
		ciPrntImmedAccs = aiPrntImmedAccs;
	}

	/**
	 * This method sets the value of RefAccs.
	 * 
	 * @param aiRefAccs   int 
	 */
	public final void setRefAccs(int aiRefAccs)
	{
		ciRefAccs = aiRefAccs;
	}

	/**
	 * This method sets the value of 
	 * 
	 * @param aiRegnlColltnAccs int
	 */
	public void setRegnlColltnAccs(int aiRegnlColltnAccs)
	{
		ciRegnlColltnAccs = aiRegnlColltnAccs;
	}

	/**
	 * This method sets the value of RegOnlyAccs.
	 * 
	 * @param aiRegOnlyAccs   int 
	 */
	public final void setRegOnlyAccs(int aiRegOnlyAccs)
	{
		ciRegOnlyAccs = aiRegOnlyAccs;
	}

	/**
	 * This method sets the value of RegRefAmtAccs.
	 * 
	 * @param aiRegRefAmtAccs   int 
	 */
	public final void setRegRefAmtAccs(int aiRegRefAmtAccs)
	{
		ciRegRefAmtAccs = aiRegRefAmtAccs;
	}

	/**
	 * This method sets the value of RenwlAccs.
	 * 
	 * @param aiRenwlAccs   int 
	 */
	public final void setRenwlAccs(int aiRenwlAccs)
	{
		ciRenwlAccs = aiRenwlAccs;
	}

	/**
	 * This method sets the value of ReplAccs.
	 * 
	 * @param aiReplAccs   int 
	 */
	public final void setReplAccs(int aiReplAccs)
	{
		ciReplAccs = aiReplAccs;
	}

	/**
	 * This method sets the value of ReprntRcptAccs.
	 * 
	 * @param aiReprntRcptAccs   int 
	 */
	public final void setReprntRcptAccs(int aiReprntRcptAccs)
	{
		ciReprntRcptAccs = aiReprntRcptAccs;
	}

	/**
	 * This method sets the value of ReprntRptAccs.
	 * 
	 * @param aiReprntRptAccs   int 
	 */
	public final void setReprntRptAccs(int aiReprntRptAccs)
	{
		ciReprntRptAccs = aiReprntRptAccs;
	}

	/**
	* This method sets the value of 
	* 
	* @param aiReprintStickerRptAccs int
	*/
	public void setReprntStkrRptAccs(int aiReprntStkrRptAccs)
	{
		ciReprntStkrRptAccs = aiReprntStkrRptAccs;
	}

	/**
	 * This method sets the value of RgstrByAccs.
	 * 
	 * @param aiRgstrByAccs   int 
	 */
	public final void setRgstrByAccs(int aiRgstrByAccs)
	{
		ciRgstrByAccs = aiRgstrByAccs;
	}

	/**
	 * This method sets the value of RptsAccs.
	 * 
	 * @param aiRptsAccs   int 
	 */
	public final void setRptsAccs(int aiRptsAccs)
	{
		ciRptsAccs = aiRptsAccs;
	}

	/**
	 * This method sets the value of RSPSUpdtAccs.
	 * 
	 * @param aiRSPSUpdtAccs   int 
	 */
	public final void setRSPSUpdtAccs(int aiRSPSUpdtAccs)
	{
		ciRSPSUpdtAccs = aiRSPSUpdtAccs;
	}

	/**
	 * This method sets the value of SalvAccs.
	 * 
	 * @param aiSalvAccs   int 
	 */
	public final void setSalvAccs(int aiSalvAccs)
	{
		ciSalvAccs = aiSalvAccs;
	}

	/**
	 * This method sets the value of SecrtyAccs.
	 * 
	 * @param aiSecrtyAccs   int 
	 */
	public final void setSecrtyAccs(int aiSecrtyAccs)
	{
		ciSecrtyAccs = aiSecrtyAccs;
	}

	/**
	 * Sets value of ciSpclPltAccs
	 * 
	 * @param aiSpclPltAccs
	 */
	public void setSpclPltAccs(int aiSpclPltAccs)
	{
		ciSpclPltAccs = aiSpclPltAccs;
	}

	/**
	 * Sets value of ciSpclPltApplAccs
	 * 
	 * @param aiSpclPltApplAccs
	 */
	public void setSpclPltApplAccs(int aiSpclPltApplAccs)
	{
		ciSpclPltApplAccs = aiSpclPltApplAccs;
	}

	/**
	 * Sets value of ciSpclPltDelPltAccs
	 * 
	 * @param aiSpclPltDelPltAccs
	 */
	public void setSpclPltDelPltAccs(int aiSpclPltDelPltAccs)
	{
		ciSpclPltDelPltAccs = aiSpclPltDelPltAccs;
	}

	/**
	 * Sets value of ciSpclPltRenwPltAccs
	 * 
	 * @param aiSpclPltRenwPltAccs
	 */
	public void setSpclPltRenwPltAccs(int aiSpclPltRenwPltAccs)
	{
		ciSpclPltRenwPltAccs = aiSpclPltRenwPltAccs;
	}

	/**
	 * Sets value of ciSpclPltResrvPltAccs
	 * 
	 * @param aiSpclPltResrvPltAccs
	 */
	public void setSpclPltResrvPltAccs(int aiSpclPltResrvPltAccs)
	{
		ciSpclPltResrvPltAccs = aiSpclPltResrvPltAccs;
	}

	/**
	 * Sets value of ciSpclPltRevisePltAccs 
	 * 
	 * @param aiSpclPltRevisePltAccs
	 */
	public void setSpclPltRevisePltAccs(int aiSpclPltRevisePltAccs)
	{
		ciSpclPltRevisePltAccs = aiSpclPltRevisePltAccs;
	}
	/**
	 * Sets value of ciSpclPltRptsAccs
	 * 
	 * @param aiSpclPltRptsAccs
	 */
	public void setSpclPltRptsAccs(int aiSpclPltRptsAccs)
	{
		ciSpclPltRptsAccs = aiSpclPltRptsAccs;
	}
	/**
	 * Sets value of ciSpclPltUnAccptblPltAccs
	 * 
	 * @param aiSpclPltUnAccptblPltAccs
	 */
	public void setSpclPltUnAccptblPltAccs(int aiSpclPltUnAccptblPltAccs)
	{
		ciSpclPltUnAccptblPltAccs = aiSpclPltUnAccptblPltAccs;
	}

	/**
	 * This method sets the value of StatusChngAccs.
	 * 
	 * @param aiStatusChngAccs   int 
	 */
	public final void setStatusChngAccs(int aiStatusChngAccs)
	{
		ciStatusChngAccs = aiStatusChngAccs;
	}

	/**
	 * This method sets the value of StlnSRSAccs.
	 * 
	 * @param aiStlnSRSAccs   int 
	 */
	public final void setStlnSRSAccs(int aiStlnSRSAccs)
	{
		ciStlnSRSAccs = aiStlnSRSAccs;
	}

	/**
	 * This method sets the value of SubconAccs.
	 * 
	 * @param aiSubconAccs   int 
	 */
	public final void setSubconAccs(int aiSubconAccs)
	{
		ciSubconAccs = aiSubconAccs;
	}

	/**
	 * This method sets the value of SubconRenwlAccs.
	 * 
	 * @param aiSubconRenwlAccs   int 
	 */
	public final void setSubconRenwlAccs(int aiSubconRenwlAccs)
	{
		ciSubconRenwlAccs = aiSubconRenwlAccs;
	}

	/**
	 * Sets value of SubconRptAccs
	 * 
	 * @param aiSubconRptAccs
	 */
	public void setSubconRptAccs(int aiSubconRptAccs)
	{
		ciSubconRptAccs = aiSubconRptAccs;
	}

	/**
	 * This method sets the value of SubstaId.
	 * 
	 * @param aiSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	/**
	 * This method sets the value of TempAddlWtAccs.
	 * 
	 * @param aiTempAddlWtAccs   int 
	 */
	public final void setTempAddlWtAccs(int aiTempAddlWtAccs)
	{
		ciTempAddlWtAccs = aiTempAddlWtAccs;
	}

	/**
	 * This method sets the value of TimedPrmtAccs.
	 * 
	 * @param aiTimedPrmtAccs   int 
	 */
	public final void setTimedPrmtAccs(int aiTimedPrmtAccs)
	{
		ciTimedPrmtAccs = aiTimedPrmtAccs;
	}

	/**
	 * This method sets the value of TowTrkAccs.
	 * 
	 * @param aiTowTrkAccs   int 
	 */
	public final void setTowTrkAccs(int aiTowTrkAccs)
	{
		ciTowTrkAccs = aiTowTrkAccs;
	}

	/**
	 * This method sets the value of TransAMDate.
	 * 
	 * @param aiTransAMDate   int 
	 */
	public final void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * This method sets the value of TransTime.
	 * 
	 * @param aiTransTime   int 
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * This method sets the value of TransWsId.
	 * 
	 * @param aiTransWsId   int 
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}
	/**
	 * This method sets the value of TtlApplAccs.
	 * 
	 * @param aiTtlApplAccs   int 
	 */
	public final void setTtlApplAccs(int aiTtlApplAccs)
	{
		ciTtlApplAccs = aiTtlApplAccs;
	}

	/**
	 * This method sets the value of TtlRegAccs.
	 * 
	 * @param aiTtlRegAccs   int 
	 */
	public final void setTtlRegAccs(int aiTtlRegAccs)
	{
		ciTtlRegAccs = aiTtlRegAccs;
	}
	/**
	 * This method sets the value of TtlRevkdAccs.
	 * 
	 * @param aiTtlRevkdAccs   int 
	 */
	public final void setTtlRevkdAccs(int aiTtlRevkdAccs)
	{
		ciTtlRevkdAccs = aiTtlRevkdAccs;
	}

	/**
	 * This method sets the value of TtlSurrAccs.
	 * 
	 * @param aiTtlSurrAccs   int 
	 */
	public final void setTtlSurrAccs(int aiTtlSurrAccs)
	{
		ciTtlSurrAccs = aiTtlSurrAccs;
	}

	/**
	 * This method sets the value of UpdtActn.
	 * 
	 * @param asUpdtActn   String 
	 */
	public final void setUpdtActn(String asUpdtActn)
	{
		csUpdtActn = asUpdtActn;
	}

	/**
	 * This method sets the value of UpdtngEmpId.
	 * 
	 * @param asUpdtngEmpId   String 
	 */
	public final void setUpdtngEmpId(String asUpdtngEmpId)
	{
		csUpdtngEmpId = asUpdtngEmpId;
	}

	/**
	 * This method sets the value of UserName.
	 * 
	 * @param asUserName  String 
	 */
	public final void setUserName(String asUserName)
	{
		csUserName = asUserName;
	}
	/**
	 * This method sets the value of VoidTransAccs.
	 * 
	 * @param aiVoidTransAccs   int 
	 */
	public final void setVoidTransAccs(int aiVoidTransAccs)
	{
		ciVoidTransAccs = aiVoidTransAccs;
	}
	// defect 10717
	/**
	 * Set value of ciWebAgntAccs
	 * 
	 * @param aiWebAgntAccs
	 */
	public void setWebAgntAccs(int aiWebAgntAccs)
	{
		ciWebAgntAccs = aiWebAgntAccs;
	}
}