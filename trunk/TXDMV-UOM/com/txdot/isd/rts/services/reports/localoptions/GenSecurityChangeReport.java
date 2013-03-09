package com.txdot.isd.rts.services.reports.localoptions;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.data.SecurityLogData;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 * GenSecurityChangeReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	09/12/2001	New Class
 * Ray Rowehl	10/03/2001	Start using SecurityLogData
 *							Also move to Local Options
 * Ray Rowehl	01/23/2002	Fix action taken description.
 *							correct date and time printing.
 *							defect 1117
 * S Govindappa	07/05/2002	merged PCR25 changes to display  
 * 							credit card fee info in the report
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * Min Wang		07/02/2003	Fix missing "END OF REPORT" on the 
 *                          last page of Security Change Report
 * 							(RTS.POS.4003).
 * 							modify formatReport() 
 * 							defect 6191
 * Min Wang		10/02/2003  Added User Name field to report.
 *							modify queryData() to setUserName() 
 *							for running stand alone.
 *				02/02/2004	modify formatReport() and queryData() 
 *							defect 6616. Ver 5.1.6
 * K Harrell	03/21/2004	Merge 5.2.0.  See PCR 34 comments.
 *							modify buildSecurityMatrix()
 * 							Ver 5.2.0 
 * K Harrell	04/02/2004	Removed reference to QuickCntrAccs &
 *							QuickCntrRptAccs
 *							modify queryData()
 *							defect 6955 Ver 5.2.0
 * Min Wang 	05/26/2004  Correct misspelled word: Miscellaneous.
 *							Increase MISC REGISTRATION TABLE length.
 *							modify formatReport()
 *							defect 7114 Ver 5.2.0
 * Min Wang		07/12/2004	Add reference to RSPSUpdtAccs
 *							modify buildSecurityMatrix()
 *							defect 7311 Ver 5.2.1
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * M Reyes		10/12/2006	Added references to ExmptAuditRptAccs &
 * 							ExmptAuthAccs
 * 							modify buildSecurityMatrix()
 * 							defect 8900 Ver Exempts
 * M Reyes		04/25/2007	Added references to SpclPltAccs,
 * 							SpclPltApplAccs, SpclPltRenewPltAccs,
 * 							SpclPltRevisePltAccs, SpclPltResrvPltAccs,
 * 							SpclPltUnAccptblPltAccs, SpclPltDelPltAccs,
 * 							SpclPltRptsAccs
 * 							modify formatReport(), buildSecurityMatrix()
 * 							defect 9124 Ver Special Plates
 * K Harrell	09/10/2008	Add references to DlrRptAccs, LienHldrRptAccs,
 * 							SubconRptAccs.  Change "LIENHLDRS" to 
 * 							"LIENHLDR" as others are singular  
 * 							modify buildSecurityMatrix() 
 * 							defect 9710 Ver Defect_POS_B
 * K Harrell	10/27/2008	Add reference to DsabldPlcrdRptAccs, 
 * 							  DsabldPlcrdInqAccs  
 * 							modify buildSecurityMatrix() 
 * 							defect 9831 Ver Defect_POS_B   
 * B Hargrove	03/09/2009	Update for ETtlRptAccs 
 * 							modify buildSecurityMatrix()
 * 							defect 9960 Ver Defect_POS_E
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F    
 * Min Wang		08/21/2009	Added references to PrivateLawEnfVehAccs
 * 							modify buildSecurityMatrix()
 * 							defect 10153 Ver Defect_POS_F
 * Min Wang		06/22/2010	do not present Subcontractor Renewal Access 
 * 							if use has access to Subcon Update and Report.
 * 							modify buildSecurityMatrix()		
 * 							defect 10497  Ver 6.7.0
 * Min Wang		01/13/2011	Added references to WebAgentAccs
 * 							modify buildSecurityMatrix()
 * 							defect 10717 Ver 6.7.0
 * Min Wang		01/18/2011	Added references to WebAgntAccs
 * 							delete WebAgentAccs
 * 							modify buildSecurityMatrix()
 * 							defect 10717 Ver 6.7.0
 * K Harrell	01/20/2011	Add reference to BatchRptMgmt 
 * 							modify buildSecurityMatrix() 
 * 							defect 10701 Ver 6.7.0 
 * Min Wang		01/24/2011	modify buildSecurityMatrix()
 * 							defect 10717 Ver 6.7.0
 * K Harrell	05/28/2011	add reference to ModfyTimedPrmtAccs
 * 							modify buildSecurityMatrix()
 * 							defect 10844 Ver 6.8.0  
 * K Harrell	06/17/2011	ReportsAccs should always be 'on' 
 * 							add reference to SuspectedFraudReport
 * 							modify buildSecurityMatrix()
 * 							defect 10900 Ver 6.8.0  
 * K Harrell	01/11/2012	Add reference to ExportAccs,
 * 							  DsabldPlacardReinstateAccs
 * 							modify buildSecurityMatrix()
 * 							defect 11231 Ver 6.10.0  
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the Security Change Report.
 *
 * @version 6.10.0 			01/11/2012
 * @author  Ray Rowehl
 * <br>Creation date:		09/12/2001 01:55:55
 */
public class GenSecurityChangeReport extends ReportTemplate
{

	/**
	 * GenSecurityChangeReport constructor
	 */
	public GenSecurityChangeReport()
	{
		super();
	}

	/**
	 * GenSecurityChangeReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public GenSecurityChangeReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}

	/**
	 * Build the Matrix to show security allowed for a person.
	 * 
	 * @param laDataline SecurityLogData
	 * @return Vector
	 */
	private Vector buildSecurityMatrix(SecurityLogData aaDataline)
	{
		Vector lvBigMatrix = new Vector();
		int liCount = 0;

		// defect 10717
		//Vector lvRegOnly = new Vector(13);
		Vector lvRegOnly = new Vector(14);
		// end defect 10717
		if (aaDataline.getRegOnlyAccs() == 1)
		{
			lvRegOnly.addElement("Registration Only".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getItrntRenwlAccs() == 1)
		{
			lvRegOnly.addElement("Internet Renewal".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getRenwlAccs() == 1)
		{
			lvRegOnly.addElement("Renewal".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getDuplAccs() == 1)
		{
			lvRegOnly.addElement("Duplicate Receipt".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getExchAccs() == 1)
		{
			lvRegOnly.addElement("Exchange".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getReplAccs() == 1)
		{
			lvRegOnly.addElement("Replacement".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getModfyAccs() == 1)
		{
			lvRegOnly.addElement("Modify".toUpperCase());
			liCount = liCount + 1;
		}
		// defect 10497
		//		if (aaDataline.getSubconAccs() == 1)
		//		{
		//			lvRegOnly.addElement("Subcon Renewal".toUpperCase());
		//			liCount = liCount + 1;
		//		}
		//		defect 10717
		if (aaDataline.getWebAgntAccs() == 1)
		{
			lvRegOnly.addElement("WebAgent".toUpperCase());
		}
		// end defect 10717
		if (aaDataline.getSubconRenwlAccs() == 1)
		{
			lvRegOnly.addElement("Subcon Renewal".toUpperCase());
			liCount = liCount + 1;
		}
		// end defect 10497
		if (aaDataline.getIssueDrvsEdAccs() == 1)
		{
			lvRegOnly.addElement("Issue Driver Ed Plate".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getAddrChngAccs() == 1)
		{
			lvRegOnly.addElement("ADDRCHNG/PRNT RENWL".toUpperCase());
			liCount = liCount + 1;
		}
		//	if (dataline.getPltToOwnrAccs() == 1)  
		//  this is how it works in AM
		//	{
		//		lvRegOnly.addElement("ISS/Driver Educ. Plt");
		//		liCount = liCount + 1;
		//	}
		//	if (dataline.getDelPltToOwnrAccs() == 1)  
		//  this is how it works in AM
		//	{
		//		lvRegOnly.addElement("DEL/REV DRV. Educ. Plt");
		//		liCount = liCount + 1;
		//	}
		// 
		// PCR 34 / defect 6955
		// Remove Quick Counter Event 
		// if (dataline.getQuickCntrAccs() == 1)
		// {
		//     lvRegOnly.addElement("Quick Counter Renewal".toUpperCase());
		//     liCount = liCount + 1;
		// }
		// End PCR 34 / defect 6955
		if (liCount == 0)
		{
			lvRegOnly.addElement("(None)");
		}
		lvBigMatrix.addElement(lvRegOnly);
		liCount = 0;
		// defect 10153
		// defect 8900
		//Vector lvTitle = new Vector(13);
		//Vector lvTitle = new Vector(14);
		// end defect 8900
		Vector lvTitle = new Vector(15);
		// end defect 10153
		if (aaDataline.getTtlRegAccs() == 1)
		{
			lvTitle.addElement("TITLE/REGISTRATION");
			liCount = liCount + 1;
		}
		if (aaDataline.getTtlApplAccs() == 1)
		{
			lvTitle.addElement("Title Application".toUpperCase());
			liCount = liCount + 1;
		}
		// defect 8900
		if (aaDataline.getExmptAuthAccs() == 1)
		{
			lvTitle.addElement("  Exempt Authority".toUpperCase());
			liCount = liCount + 1;
		}
		// end defect 8900
		// defect 10153
		if (aaDataline.getPrivateLawEnfVehAccs() == 1)
		{
			lvTitle.addElement("  Private Law Enf Veh".toUpperCase());
			liCount = liCount + 1;
		}
		// end defect 10153
		if (aaDataline.getCorrTtlRejAccs() == 1)
		{
			lvTitle.addElement("Correct TTL Rejctn".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getDlrTtlAccs() == 1)
		{
			lvTitle.addElement("Dealer Titles".toUpperCase());
			liCount = liCount + 1;
		}
		/*if (dataline.getRejctnAccs() == 1)
		{
			lvTitle.addElement("Reject/Release");
			liCount = liCount + 1;
		}
		*/
		if (aaDataline.getCCOAccs() == 1)
		{
			lvTitle.addElement("CCO");
			liCount = liCount + 1;
		}
		if (aaDataline.getCOAAccs() == 1)
		{
			lvTitle.addElement("COA");
			liCount = liCount + 1;
		}
		if (aaDataline.getSalvAccs() == 1)
		{
			lvTitle.addElement("Salvage".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getDelTtlInProcsAccs() == 1)
		{
			lvTitle.addElement("Del TTL in Process".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getAdjSalesTaxAccs() == 1)
		{
			lvTitle.addElement("Additional Sales Tax".toUpperCase());
			liCount = liCount + 1;
		}
		if (liCount == 0)
		{
			lvTitle.addElement("(None)");
		}
		lvBigMatrix.addElement(lvTitle);
		liCount = 0;
		// defect 11231 
		//Vector lvStatus = new Vector(13);
		Vector lvStatus = new Vector(14);
		// end defect 11231 
		if (aaDataline.getStatusChngAccs() == 1)
		{
			lvStatus.addElement("Status Change".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getJnkAccs() == 1)
		{
			lvStatus.addElement("Vehicle Junked".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getTtlSurrAccs() == 1)
		{
			lvStatus.addElement("Title Surrender".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getStlnSRSAccs() == 1)
		{
			lvStatus.addElement("STOLEN/SRS");
			liCount = liCount + 1;
		}
		if (aaDataline.getCancRegAccs() == 1)
		{
			lvStatus.addElement("Cancel Reg".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getRegRefAmtAccs() == 1)
		{
			lvStatus.addElement("Registration Refund".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getMailRtrnAccs() == 1)
		{
			lvStatus.addElement("Mail Return".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getRgstrByAccs() == 1)
		{
			lvStatus.addElement("Register By".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getMiscRemksAccs() == 1)
		{
			lvStatus.addElement("MISC Remarks".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getTtlRevkdAccs() == 1)
		{
			lvStatus.addElement("  Title Revoked".toUpperCase());
			liCount = liCount + 1;
		}
		
		// defect 11231 
		if (aaDataline.getExportAccs() == 1)
		{
			lvStatus.addElement("  EXPORT ");
			liCount = liCount + 1;
		}
		// end defect 11231 
		
		if (aaDataline.getBndedTtlCdAccs() == 1)
		{
			lvStatus.addElement("  Bonded Title Code".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getLegalRestrntNoAccs() == 1)
		{
			lvStatus.addElement("  Legal Restrnt No".toUpperCase());
			liCount = liCount + 1;
		}
		
		if (liCount == 0)
		{
			lvStatus.addElement("(None)");
		}
		lvBigMatrix.addElement(lvStatus);
		liCount = 0;
		Vector lvInquiry = new Vector(13);
		if (aaDataline.getInqAccs() == 1)
		{
			lvInquiry.addElement("Inquiry".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getInqAccs() == 1)
			// no seperate item. check AM.
		{
			lvInquiry.addElement("Vehicle Information".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getInqAccs() == 1)
		{
			lvInquiry.addElement("Plate Options".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getPltNoAccs() == 1)
		{
			lvInquiry.addElement("Plate No".toUpperCase());
			liCount = liCount + 1;
		}
		if (liCount == 0)
		{
			lvInquiry.addElement("(None)");
		}
		lvBigMatrix.addElement(lvInquiry);
		liCount = 0;

		// defect 11231  
		//Vector lvMiscReg = new Vector(14);
		Vector lvMiscReg = new Vector(15);
		// end defect 11231 
		if (aaDataline.getMiscRegAccs() == 1)
		{
			lvMiscReg.addElement(
				"Miscellaneous Registration".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getTimedPrmtAccs() == 1)
		{
			lvMiscReg.addElement("Timed Permit".toUpperCase());
			liCount = liCount + 1;
		}
		// defect 10844 
		if (aaDataline.getModfyTimedPrmtAccs() == 1)
		{
			lvMiscReg.addElement("TIMED PERMIT - MODIFY");
			liCount = liCount + 1;
		}
		// end defect 10844 

		if (aaDataline.getTempAddlWtAccs() == 1)
		{
			lvMiscReg.addElement("Temp Addl Weight".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getNonResPrmtAccs() == 1)
		{
			lvMiscReg.addElement("Non Res AG Permit".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getTowTrkAccs() == 1)
		{
			lvMiscReg.addElement("Tow Truck".toUpperCase());
			liCount = liCount + 1;
		}
		// defect 9831 
		if (aaDataline.getDsabldPlcrdInqAccs() == 1)
		{
			lvMiscReg.addElement("DISABLED PLACARD INQUIRY");
			liCount = liCount + 1;
		}
		if (aaDataline.getDsabldPersnAccs() == 1)
		{
			lvMiscReg.addElement("DISABLED PLACARD MGMT");
			liCount = liCount + 1;
		}
		// defect 11231 
		if (aaDataline.getDsabldPlcrdReInstateAccs() == 1)
		{
			lvMiscReg.addElement("   PLACARD REINSTATE");
			liCount = liCount + 1;
		}
		// end defect 11231 
		if (aaDataline.getDsabldPlcrdRptAccs() == 1)
		{
			lvMiscReg.addElement("DISABLED PLACARD REPORT");
			liCount = liCount + 1;
		}
		// end defect 9831

		if (liCount == 0)
		{
			lvMiscReg.addElement("(None)");
		}
		lvBigMatrix.addElement(lvMiscReg);
		// defect 9124
		liCount = 0;
		Vector lvSpclPlts = new Vector(13);
		if (aaDataline.getSpclPltAccs() == 1)
		{
			lvSpclPlts.addElement("SPECIAL PLATES");
			liCount = liCount + 1;
		}
		if (aaDataline.getSpclPltApplAccs() == 1)
		{
			lvSpclPlts.addElement("APPLICATION");
			liCount = liCount + 1;
		}
		if (aaDataline.getSpclPltRenwPltAccs() == 1)
		{
			lvSpclPlts.addElement("RENEW PLATE ONLY");
			liCount = liCount + 1;
		}
		if (aaDataline.getSpclPltRevisePltAccs() == 1)
		{
			lvSpclPlts.addElement("REVISE");
			liCount = liCount + 1;
		}
		if (aaDataline.getSpclPltResrvPltAccs() == 1)
		{
			lvSpclPlts.addElement("RESERVE");
			liCount = liCount + 1;
		}
		if (aaDataline.getSpclPltUnAccptblPltAccs() == 1)
		{
			lvSpclPlts.addElement("UNACCEPTABLE");
			liCount = liCount + 1;
		}
		if (aaDataline.getSpclPltDelPltAccs() == 1)
		{
			lvSpclPlts.addElement("DELETE");
			liCount = liCount + 1;
		}
		if (aaDataline.getSpclPltRptsAccs() == 1)
		{
			lvSpclPlts.addElement("REPORTS");
			liCount = liCount + 1;
		}
		if (liCount == 0)
		{
			lvSpclPlts.addElement("(None)");
		}
		lvBigMatrix.addElement(lvSpclPlts);
		// end defect 9124
		liCount = 0;
		Vector lvMisc = new Vector(7);
		if (aaDataline.getReprntRcptAccs() == 1
			|| // added misc to security
		aaDataline.getVoidTransAccs()
				== 1
			|| aaDataline.getPrntImmedAccs() == 1)
		{
			lvMisc.addElement("Miscellaneous".toUpperCase());
			liCount = liCount + 1;
		}
		if (aaDataline.getReprntRcptAccs() == 1)
		{
			lvMisc.addElement("REPRINT RECEIPT");
			liCount = liCount + 1;
		}
		if (aaDataline.getReprntRcptAccs() == 1)
		{
			lvMisc.addElement("COMPLETE VEH TRANS");
			liCount = liCount + 1;
		}
		if (aaDataline.getVoidTransAccs() == 1)
		{
			lvMisc.addElement("VOID TRANSACTION");
			liCount = liCount + 1;
		}
		if (aaDataline.getReprntRcptAccs() == 1)
		{
			lvMisc.addElement("SET PRINT DESTINATION");
			liCount = liCount + 1;
		}
		if (aaDataline.getPrntImmedAccs() == 1)
		{
			lvMisc.addElement("PRINT IMMEDIATE");
			liCount = liCount + 1;
		}
		if (liCount == 0)
		{
			lvMisc.addElement("(None)");
		}
		lvBigMatrix.addElement(lvMisc);
		liCount = 0;

		Vector lvRpts = new Vector(8);

		// defect 10900
		// All now have Reports Access
		// Actually don't have to use liCount

		//if (aaDataline.getRptsAccs() == 1)
		//{
		// end defect 10900
		lvRpts.addElement("REPORTS");
		liCount = liCount + 1;
		//}
		if (aaDataline.getCntyRptsAccs() == 1)
		{
			lvRpts.addElement("SALES TAX ALLOCATION");
			liCount = liCount + 1;
		}
		if (aaDataline.getReprntRptAccs() == 1)
		{
			lvRpts.addElement("REPRINT REPORTS");
			liCount = liCount + 1;
		}
		// defect 10900 
		//if (aaDataline.getRptsAccs() == 1)
		if (!aaDataline.isHQ())
			// end defect 10900	
		{
			// Title Package always on if County || Region
			lvRpts.addElement("TITLE PACKAGE REPORT");
			liCount = liCount + 1;
		}
		// PCR 34 / defect 6955
		//        - Add Reprint Sticker Report 
		//        - Remove Quick Counter Report 
		if (aaDataline.getReprntStkrRptAccs() == 1)
		{
			lvRpts.addElement("REPRINT STICKER REPORT");
			liCount = liCount + 1;
		}
		// defect 8900
		if (aaDataline.getExmptAuditRptAccs() == 1)
		{
			lvRpts.addElement("EXEMPT AUDIT REPORT");
			liCount = liCount + 1;
		}
		// end defect 8900
		// defect 9960
		if (aaDataline.getETtlRptAccs() == 1)
		{
			lvRpts.addElement("ELECTRONIC TITLE REPORT");
			liCount = liCount + 1;
		}
		// end defect 9960

		// defect 10900
		if (aaDataline.isRegion() || aaDataline.isHQ())
		{
			lvRpts.addElement("SUSPECTED FRAUD REPORT");
			liCount = liCount + 1;
		}
		// end defect 10900 

		if (liCount == 0)
		{
			lvRpts.addElement("(None)");
		}
		lvBigMatrix.addElement(lvRpts);
		liCount = 0;
		// defect 10701 
		//Vector lvLocalOpt = new Vector(9);
		Vector lvLocalOpt = new Vector(10);
		// end defect 10701 

		if (aaDataline.getLocalOptionsAccs() == 1)
		{
			lvLocalOpt.addElement("LOCAL OPTIONS");
			liCount = liCount + 1;
		}
		// defect 9710 
		// Add Logic for Rpt Only Access
		if (aaDataline.getDlrAccs() == 1)
		{
			lvLocalOpt.addElement("DEALER UPDTS & RPT");
			liCount = liCount + 1;
		}
		else if (aaDataline.getDlrRptAccs() == 1)
		{
			lvLocalOpt.addElement("DEALER RPT");
			liCount = liCount + 1;
		}
		if (aaDataline.getSubconAccs() == 1)
		{
			lvLocalOpt.addElement("SUBCON UPDTS & RPT");
			liCount = liCount + 1;
		}
		else if (aaDataline.getSubconRptAccs() == 1)
		{
			lvLocalOpt.addElement("SUBCON RPT");
			liCount = liCount + 1;
		}
		if (aaDataline.getLienHldrAccs() == 1)
		{
			lvLocalOpt.addElement("LIENHLDR UPDTS & RPT");
			liCount = liCount + 1;
		}
		else if (aaDataline.getLienHldrRptAccs() == 1)
		{
			lvLocalOpt.addElement("LIENHLDR RPT");
			liCount = liCount + 1;
		}
		// end defect 9710 
		if (aaDataline.getCrdtCardFeeAccs() == 1)
		{
			lvLocalOpt.addElement("CREDIT CARD FEE");
			liCount = liCount + 1;
		}

		//defect 7311
		if (aaDataline.getRSPSUpdtAccs() == 1)
		{
			lvLocalOpt.addElement("RSPS STATUS UPDTS");
			liCount = liCount + 1;
		}
		//end defect 7311

		// defect 10701 
		if (aaDataline.getBatchRptMgmtAccs() == 1)
		{
			lvLocalOpt.addElement("BATCH RPT MGMT");
			liCount = liCount + 1;
		}
		//end defect 10701

		if (aaDataline.getSecrtyAccs() == 1)
		{
			lvLocalOpt.addElement("SECURITY");
			liCount = liCount + 1;
		}
		if (aaDataline.getEmpSecrtyAccs() == 1)
		{
			lvLocalOpt.addElement("  EMP SECURITY");
			liCount = liCount + 1;
		}
		if (aaDataline.getEmpSecrtyRptAccs() == 1)
		{
			lvLocalOpt.addElement("  EMP RPT SECURITY");
			liCount = liCount + 1;
		}

		if (aaDataline.getAdminAccs() == 1)
		{
			lvLocalOpt.addElement("ADMINISTRATION");
			liCount = liCount + 1;
		}
		if (liCount == 0)
		{
			lvLocalOpt.addElement("(None)");
		}
		lvBigMatrix.addElement(lvLocalOpt);
		liCount = 0;
		Vector lvAccounting = new Vector(7);
		if (aaDataline.getAcctAccs() == 1)
		{
			lvAccounting.addElement("ACCOUNTING");
			liCount = liCount + 1;
		}
		if (aaDataline.getFundsRemitAccs() == 1)
		{
			lvAccounting.addElement("FUNDS REMITTANCE");
			liCount = liCount + 1;
		}
		/*	if (dataline.getFundsAckAccs() == 1)	
			{
				lvAccounting.addElement("VTR FUNDS ACKMNT");
				liCount = liCount + 1;
			}
		*/
		if (aaDataline.getFundsInqAccs() == 1)
		{
			lvAccounting.addElement("FUNDS INQUIRY");
			liCount = liCount + 1;
		}
		if (aaDataline.getRefAccs() == 1)
		{
			lvAccounting.addElement("REFUND");
			liCount = liCount + 1;
		}
		if (aaDataline.getHotCkCrdtAccs() == 1)
		{
			lvAccounting.addElement("HOT CHECK CREDIT");
			liCount = liCount + 1;
		}
		if (aaDataline.getHotCkRedemdAccs() == 1)
		{
			lvAccounting.addElement("HOT CHECK REDEMD");
			liCount = liCount + 1;
		}
		if (aaDataline.getModfyHotCkAccs() == 1)
		{
			lvAccounting.addElement("DEDUCT HOT CHECK");
			liCount = liCount + 1;
		}
		if (aaDataline.getItmSeizdAccs() == 1)
		{
			lvAccounting.addElement("ITEMS SEIZED");
			liCount = liCount + 1;
		}
		if (aaDataline.getFundsAdjAccs() == 1)
		{
			// lvAccounting.addElement("VTR FUNDS ADJ"); // if hq or reg
			lvAccounting.addElement("ADDL COLLECTIONS"); // if county
			liCount = liCount + 1;
		}
		if (aaDataline.getRegnlColltnAccs() == 1)
		{
			lvAccounting.addElement("REGIONAL COLLECTIONS");
			liCount = liCount + 1;
		}
		if (liCount == 0)
		{
			lvAccounting.addElement("(None)");
		}
		lvBigMatrix.addElement(lvAccounting);
		liCount = 0;
		Vector lvInventory = new Vector(7);
		if (aaDataline.getInvAccs() == 1)
		{
			lvInventory.addElement("INVENTORY");
			liCount = liCount + 1;
		}
		if (aaDataline.getInvAllocAccs() == 1)
		{
			lvInventory.addElement("ALLOCATE");
			liCount = liCount + 1;
		}
		if (aaDataline.getInvDelAccs() == 1)
		{
			lvInventory.addElement("DELETE");
			liCount = liCount + 1;
		}
		if (aaDataline.getInvInqAccs() == 1)
		{
			lvInventory.addElement("INQUIRY");
			liCount = liCount + 1;
		}
		if (aaDataline.getInvAckAccs() == 1)
		{
			lvInventory.addElement("ACKNOWLEDGMENT RECEIPT");
			liCount = liCount + 1;
		}
		if (aaDataline.getInvProfileAccs() == 1)
		{
			lvInventory.addElement("PROFILE");
			liCount = liCount + 1;
		}
		if (aaDataline.getInvHldRlseAccs() == 1)
		{
			lvInventory.addElement("HOLD/RELEASE");
			liCount = liCount + 1;
		}
		/*if (dataline.getInvAdjAccs() == 1)
		{
			lvInventory.addElement("ADJUSTMENT");
			liCount = liCount + 1;
		}
		*/
		if (aaDataline.getInvActionAccs() == 1)
		{
			lvInventory.addElement("INVENTORY ACTION REPORT");
			liCount = liCount + 1;
		}
		if (liCount == 0)
		{
			lvInventory.addElement("(None)");
		}
		lvBigMatrix.addElement(lvInventory);
		liCount = 0;
		Vector lvFunds = new Vector(7);
		if (aaDataline.getCashOperAccs() == 1
			|| aaDataline.getFundsBalAccs() == 1
			|| aaDataline.getFundsMgmtAccs() == 1)
		{
			lvFunds.addElement("FUNDS");
			liCount = liCount + 1;
		}
		if (aaDataline.getCashOperAccs() == 1)
		{
			lvFunds.addElement("CASH DRW OPS");
			liCount = liCount + 1;
		}
		if (aaDataline.getFundsBalAccs() == 1)
		{
			lvFunds.addElement("FUNDS BAL");
			liCount = liCount + 1;
		}
		if (aaDataline.getFundsMgmtAccs() == 1)
		{
			lvFunds.addElement("FUNDS MGMT");
			liCount = liCount + 1;
		}
		if (liCount == 0)
		{
			lvFunds.addElement("(None)");
		}
		lvBigMatrix.addElement(lvFunds);
		return lvBigMatrix;
	}

	/**
	 * Format the data into the report
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		SecurityLogData laDataline = new SecurityLogData();
		Vector lvHeader = new Vector();
		// no additional header information
		Vector lvTable = new Vector();
		Vector lvBigMatrix = new Vector();
		Vector lvRegOnly = new Vector();
		Vector lvTitle = new Vector();
		Vector lvStatus = new Vector();
		Vector lvInquiry = new Vector();
		Vector lvMiscReg = new Vector();
		// defect 9124
		Vector lvSpclPlts = new Vector();
		// end defect 9124
		Vector lvMisc = new Vector();
		Vector lvRpts = new Vector();
		Vector lvLocalOpt = new Vector();
		Vector lvAccounting = new Vector();
		Vector lvInventory = new Vector();
		Vector lvFunds = new Vector();
		int i = 0;
		if (avResults.size() > 0)
		{
			while (i < avResults.size()) //Loop through the results
			{
				generateHeader(lvHeader, lvTable);
				lvBigMatrix.clear();
				lvRegOnly.clear();
				lvTitle.clear();
				lvStatus.clear();
				lvInquiry.clear();
				lvMiscReg.clear();
				// defect 9124
				lvSpclPlts.clear();
				// end defect 9124
				lvMisc.clear();
				lvRpts.clear();
				lvLocalOpt.clear();
				lvAccounting.clear();
				lvInventory.clear();
				lvFunds.clear();
				laDataline = (SecurityLogData) avResults.elementAt(i);
				lvBigMatrix = this.buildSecurityMatrix(laDataline);
				lvRegOnly = (Vector) lvBigMatrix.elementAt(0);
				lvTitle = (Vector) lvBigMatrix.elementAt(1);
				lvStatus = (Vector) lvBigMatrix.elementAt(2);
				lvInquiry = (Vector) lvBigMatrix.elementAt(3);
				lvMiscReg = (Vector) lvBigMatrix.elementAt(4);
				// defect 9124
				lvSpclPlts = (Vector) lvBigMatrix.elementAt(5);
				// end defect 9124
				lvMisc = (Vector) lvBigMatrix.elementAt(6);
				lvRpts = (Vector) lvBigMatrix.elementAt(7);
				lvLocalOpt = (Vector) lvBigMatrix.elementAt(8);
				lvAccounting = (Vector) lvBigMatrix.elementAt(9);
				lvInventory = (Vector) lvBigMatrix.elementAt(10);
				lvFunds = (Vector) lvBigMatrix.elementAt(11);
				lvRegOnly.trimToSize();
				lvTitle.trimToSize();
				lvStatus.trimToSize();
				lvInquiry.trimToSize();
				lvMiscReg.trimToSize();
				// defect 9124
				lvSpclPlts.trimToSize();
				// end defect 9124
				lvMisc.trimToSize();
				lvRpts.trimToSize();
				lvLocalOpt.trimToSize();
				lvAccounting.trimToSize();
				lvInventory.trimToSize();
				lvFunds.trimToSize();
				caRpt.blankLines(1);
				caRpt.print("EMPLOYEE NAME:", 2, 15);
				caRpt.print(
					laDataline.getEmpLastName()
						+ ", "
						+ laDataline.getEmpFirstName());
				if (laDataline.getEmpMI().length() > 0)
				{
					caRpt.print(" " + laDataline.getEmpMI() + ".");
				}
				//defect 6616
				//add user name
				caRpt.print("USER NAME:", 55, 11);
				caRpt.print(laDataline.getUserName(), 25, 11);
				caRpt.print("EMPLOYEE ID:", 100, 13);
				caRpt.print(laDataline.getEmpId(), 113, 7);
				//end defect 6616
				caRpt.blankLines(2);
				caRpt.print("ACTION TAKEN:  ", 2, 16);
				// record was inserted
				if (laDataline.getUpdtActn().equals("I"))
				{
					caRpt.print("Insert");
				}
				// record was updated
				if (laDataline.getUpdtActn().equals("U"))
				{
					caRpt.print("Update");
				}
				// record was deleted
				if (laDataline.getUpdtActn().equals("D"))
				{
					caRpt.print("Delete");
				}
				caRpt.print("BY:", 29, 3);
				caRpt.print(laDataline.getUpdtngEmpId(), 35, 7);
				// handle Date and Time Formatting by using RTSDate
				RTSDate laTransDate =
					new RTSDate(
						RTSDate.AMDATE,
						laDataline.getTransAMDate());
				// only print the if it is there
				if (laDataline.getTransTime() > 0)
				{
					laTransDate.setTime(laDataline.getTransTime());
				}
				caRpt.print("DATE:", 50, 5);
				caRpt.print(laTransDate.toString(), 57, 10);
				caRpt.print("TIME:", 71, 5);
				// only print the if it is there
				if (laDataline.getTransTime() > 0)
				{
					caRpt.print(laTransDate.getClockTime(), 78, 8);
				}
				caRpt.nextLine();
				caRpt.nextLine();
				caRpt.print("ACCESS ALLOWED FOR:", 2, 20);
				caRpt.blankLines(2);
				caRpt.print("REG ONLY TABLE", 2, 25);
				caRpt.print("TITLE/REGISTRATION TABLE", 27, 25);
				caRpt.print("STATUS CHANGE TABLE", 53, 25);
				caRpt.print("INQUIRY TABLE", 79, 25);
				caRpt.print("MISC REGISTRATION TABLE", 105, 25);
				caRpt.nextLine();
				caRpt.blankLines(2);
				for (int l = 0; l < 13; l++)
				{
					if (l < lvRegOnly.size())
					{
						caRpt.print(
							lvRegOnly.elementAt(l).toString(),
							2,
							25);
					}
					if (l < lvTitle.size())
					{
						caRpt.print(
							lvTitle.elementAt(l).toString(),
							27,
							25);
					}
					if (l < lvStatus.size())
					{
						caRpt.print(
							lvStatus.elementAt(l).toString(),
							53,
							25);
					}
					if (l < lvInquiry.size())
					{
						caRpt.print(
							lvInquiry.elementAt(l).toString(),
							79,
							25);
					}
					if (l < lvMiscReg.size())
					{
						// defect 7114
						caRpt.print(
							lvMiscReg.elementAt(l).toString(),
							105,
							26);
						// the length was 25 before
						// end defect 7114
					}
					caRpt.blankLines(1);
				}
				caRpt.blankLines(1);
				// defect 9124
				caRpt.print("SPECIAL PLATES TABLE", 2, 25);
				// end defect 9124
				caRpt.print("MISCELLANEOUS TABLE", 27, 25);
				caRpt.print("REPORTS TABLE", 53, 25);
				caRpt.print("LOCAL OPTIONS TABLE", 79, 25);
				caRpt.print("ACCOUNTING TABLE", 105, 25);
				caRpt.blankLines(2);
				for (int l = 0; l < 13; l++)
				{
					// defect 9124
					if (l < lvSpclPlts.size())
					{
						caRpt.print(
							lvSpclPlts.elementAt(l).toString(),
							2,
							25);
					}
					// end defect 9124
					if (l < lvMisc.size())
					{
						caRpt.print(
							lvMisc.elementAt(l).toString(),
							27,
							25);
					}
					if (l < lvRpts.size())
					{
						caRpt.print(
							lvRpts.elementAt(l).toString(),
							53,
							25);
					}
					if (l < lvLocalOpt.size())
					{
						caRpt.print(
							lvLocalOpt.elementAt(l).toString(),
							79,
							25);
					}
					if (l < lvAccounting.size())
					{
						caRpt.print(
							lvAccounting.elementAt(l).toString(),
							105,
							25);
					}
					caRpt.blankLines(1);
				}
				caRpt.blankLines(1);
				// defect 9124
				caRpt.print("INVENTORY TABLE", 2, 25);
				// end defect 9124
				caRpt.print("FUNDS TABLE", 27, 25);
				caRpt.blankLines(2);
				for (int l = 0; l < 13; l++)
				{
					// defect 9124
					if (l < lvInventory.size())
					{
						caRpt.print(
							lvInventory.elementAt(l).toString(),
							2,
							25);
					}
					// end defect 9124
					if (l < lvFunds.size())
					{
						caRpt.print(
							lvFunds.elementAt(l).toString(),
							27,
							25);
					}
					caRpt.blankLines(1);
				}
				caRpt.blankLines(1);

				// defect 7114
				caRpt.println(
					" Note: If either Registration, Title/Registration, "
						+ "Miscellaneous Reg., or Inquiry");
				// end defect 7114

				caRpt.println(
					"       accesses are turned on, then "
						+ "Customer Service is also turned on");

				// defect 8628
				i = i + 1;
				//	if (i >= avResults.size())
				//	{
				//		this.caRpt.nextLine();
				//		generateEndOfReport();
				//	}
				//	generateFooter();
				generateFooter(i >= avResults.size());
				// end defect 8628 

			}
		}
		else
		{
			generateHeader(lvHeader, lvTable);
			caRpt.blankLines(1);
			generateNoRecordFoundMsg();
			// defect 8628 
			//generateEndOfReport();
			//generateFooter();
			generateFooter(true);
			// end defect 8628  
		}
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class 
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.4003");
		GenSecurityChangeReport laGSCR =
			new GenSecurityChangeReport(
				"Security Change Report",
				laRptProps);
		// Generating Demo data to display.
		String lsQuery = "Select * FROM RTS.RTS_Security_Log";
		Vector lvQueryResults = laGSCR.queryData(lsQuery);
		laGSCR.formatReport(lvQueryResults);
		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\SECCHG.RPT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}
		laPout.print(laGSCR.caRpt.getReport().toString());
		laPout.close();
		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport = new FrmPreviewReport("c:\\SECCHG.RPT");
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReport.show();
			// defect 7590
			// change setVisible to setVisibleRTS
			laFrmPreviewReport.setVisibleRTS(true);
			// end defect 7590
			// One way to Print Report.
			// Process p = Runtime.getRuntime().exec(
			// "cmd.exe /c copy c:\\TitlePkgRpt.txt prn");
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of "
					+ "com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Run query and return results.
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// This is faked data
		Vector lvData = new Vector();
		SecurityLogData laDataline = new SecurityLogData();
		laDataline.setOfcIssuanceNo(161);
		laDataline.setSubstaId(1);
		//defect 6616
		laDataline.setUserName("RROWEHL");
		//end defect 6616
		laDataline.setEmpId("RROWEHL");
		laDataline.setEmpLastName("Rowehl");
		laDataline.setEmpFirstName("Ray");
		laDataline.setEmpMI("R");
		laDataline.setAcctAccs(1);
		laDataline.setAddrChngAccs(1);
		laDataline.setAdjSalesTaxAccs(1);
		laDataline.setAdminAccs(1);
		laDataline.setBndedTtlCdAccs(1);
		laDataline.setCancRegAccs(1);
		laDataline.setCashOperAccs(1);
		laDataline.setCCOAccs(1);
		laDataline.setCOAAccs(1);
		laDataline.setCorrTtlRejAccs(1);
		laDataline.setCustServAccs(1);
		//dataline.setDelPltToOwnrAccs(1);
		laDataline.setDelTtlInProcsAccs(1);
		laDataline.setDlrAccs(1);
		laDataline.setDlrTtlAccs(1);
		laDataline.setDsabldPersnAccs(1);
		laDataline.setDuplAccs(1);
		laDataline.setEmpSecrtyAccs(1);
		laDataline.setEmpSecrtyRptAccs(1);
		laDataline.setExchAccs(1);
		//dataline.setFundsAckAccs(1) ;
		laDataline.setFundsAdjAccs(1);
		laDataline.setFundsBalAccs(1);
		laDataline.setFundsInqAccs(1);
		laDataline.setFundsMgmtAccs(1);
		laDataline.setFundsRemitAccs(1);
		laDataline.setHotCkCrdtAccs(1);
		laDataline.setHotCkRedemdAccs(1);
		laDataline.setInqAccs(1);
		laDataline.setInvAccs(1);
		laDataline.setInvAckAccs(1);
		laDataline.setInvActionAccs(1);
		//dataline.setInvAdjAccs(1);
		laDataline.setInvAllocAccs(1);
		laDataline.setInvDelAccs(1);
		laDataline.setInvHldRlseAccs(1);
		laDataline.setInvInqAccs(1);
		laDataline.setInvProfileAccs(1);
		laDataline.setItmSeizdAccs(1);
		laDataline.setJnkAccs(1);
		laDataline.setLegalRestrntNoAccs(1);
		laDataline.setLienHldrAccs(1);
		laDataline.setLocalOptionsAccs(1);
		laDataline.setMailRtrnAccs(1);
		laDataline.setMiscRegAccs(1);
		laDataline.setMiscRemksAccs(1);
		laDataline.setModfyAccs(1);
		laDataline.setModfyHotCkAccs(1);
		laDataline.setNonResPrmtAccs(1);
		laDataline.setPltNoAccs(1);
		//dataline.setPltToOwnrAccs(1) ;
		laDataline.setPrntImmedAccs(1);
		// defect 6955
		//dataline.setQuickCntrAccs(1);
		//dataline.setQuickCntrRptAccs(1);
		// end defect 6955
		laDataline.setRefAccs(1);
		laDataline.setRegOnlyAccs(1);
		laDataline.setRegRefAmtAccs(1);
		//dataline.setRejctnAccs(1) ;
		laDataline.setRenwlAccs(1);
		laDataline.setReplAccs(1);
		laDataline.setReprntRcptAccs(1);
		laDataline.setReprntRptAccs(1);
		// PCR 34
		laDataline.setReprntStkrRptAccs(1);
		// End PCR 34 
		laDataline.setRgstrByAccs(1);
		laDataline.setRptsAccs(1);
		laDataline.setSalvAccs(1);
		laDataline.setSecrtyAccs(1);
		laDataline.setStatusChngAccs(1);
		laDataline.setStlnSRSAccs(1);
		laDataline.setSubconAccs(1);
		laDataline.setSubconRenwlAccs(1);
		laDataline.setTempAddlWtAccs(1);
		laDataline.setTimedPrmtAccs(1);
		laDataline.setTowTrkAccs(1);
		laDataline.setTtlApplAccs(1);
		laDataline.setTtlRegAccs(1);
		laDataline.setTtlRevkdAccs(1);
		laDataline.setTtlSurrAccs(1);
		laDataline.setVoidTransAccs(1);
		laDataline.setUpdtActn("U");
		laDataline.setUpdtngEmpId("USERID1");
		laDataline.setTransAMDate(37163);
		laDataline.setTransTime(101010);
		lvData.addElement(laDataline);
		SecurityLogData laDataline2 = new SecurityLogData();
		laDataline2.setOfcIssuanceNo(161);
		laDataline2.setSubstaId(1);
		laDataline2.setEmpId("BADSEED");
		laDataline2.setEmpLastName("Seed");
		laDataline2.setEmpFirstName("Bad");
		laDataline2.setEmpMI("R");
		laDataline2.setAcctAccs(0);
		laDataline2.setAddrChngAccs(0);
		laDataline2.setAdjSalesTaxAccs(0);
		laDataline2.setAdminAccs(0);
		laDataline2.setBndedTtlCdAccs(0);
		laDataline2.setCancRegAccs(0);
		laDataline2.setCashOperAccs(0);
		laDataline2.setCCOAccs(0);
		laDataline2.setCOAAccs(0);
		laDataline2.setCorrTtlRejAccs(0);
		laDataline2.setCustServAccs(0);
		//dataline2.setDelPltToOwnrAccs(0);
		laDataline2.setDelTtlInProcsAccs(0);
		laDataline2.setDlrAccs(0);
		laDataline2.setDlrTtlAccs(0);
		laDataline2.setDsabldPersnAccs(0);
		laDataline2.setDuplAccs(0);
		laDataline2.setEmpSecrtyAccs(0);
		laDataline2.setEmpSecrtyRptAccs(0);
		laDataline2.setExchAccs(0);
		//dataline2.setFundsAckAccs(0) ;
		laDataline2.setFundsAdjAccs(0);
		laDataline2.setFundsBalAccs(0);
		laDataline2.setFundsInqAccs(0);
		laDataline2.setFundsMgmtAccs(0);
		laDataline2.setFundsRemitAccs(0);
		laDataline2.setHotCkCrdtAccs(0);
		laDataline2.setHotCkRedemdAccs(0);
		laDataline2.setInqAccs(0);
		laDataline2.setInvAccs(0);
		laDataline2.setInvAckAccs(0);
		laDataline2.setInvActionAccs(0);
		//dataline2.setInvAdjAccs(0);
		laDataline2.setInvAllocAccs(0);
		laDataline2.setInvDelAccs(0);
		laDataline2.setInvHldRlseAccs(0);
		laDataline2.setInvInqAccs(0);
		laDataline2.setInvProfileAccs(0);
		laDataline2.setItmSeizdAccs(0);
		laDataline2.setJnkAccs(0);
		laDataline2.setLegalRestrntNoAccs(0);
		laDataline2.setLienHldrAccs(0);
		laDataline2.setLocalOptionsAccs(0);
		laDataline2.setMailRtrnAccs(0);
		laDataline2.setMiscRegAccs(0);
		laDataline2.setMiscRemksAccs(0);
		laDataline2.setModfyAccs(0);
		laDataline2.setModfyHotCkAccs(0);
		laDataline2.setNonResPrmtAccs(0);
		laDataline2.setPltNoAccs(0);
		//dataline2.setPltToOwnrAccs(0) ;
		laDataline2.setPrntImmedAccs(0);
		// defect 6955
		//dataline2.setQuickCntrAccs(0);
		//dataline2.setQuickCntrRptAccs(0);
		// end defect 6955
		laDataline2.setRefAccs(0);
		laDataline2.setRegOnlyAccs(0);
		laDataline2.setRegRefAmtAccs(0);
		//dataline2.setRejctnAccs(0) ;
		laDataline2.setRenwlAccs(0);
		laDataline2.setReplAccs(0);
		laDataline2.setReprntRcptAccs(0);
		laDataline2.setReprntRptAccs(0);
		laDataline2.setRgstrByAccs(0);
		laDataline2.setRptsAccs(0);
		laDataline2.setSalvAccs(0);
		laDataline2.setSecrtyAccs(0);
		laDataline2.setStatusChngAccs(0);
		laDataline2.setStlnSRSAccs(0);
		laDataline2.setSubconAccs(0);
		laDataline2.setSubconRenwlAccs(0);
		laDataline2.setTempAddlWtAccs(0);
		laDataline2.setTimedPrmtAccs(0);
		laDataline2.setTowTrkAccs(0);
		laDataline2.setTtlApplAccs(0);
		laDataline2.setTtlRegAccs(0);
		laDataline2.setTtlRevkdAccs(0);
		laDataline2.setTtlSurrAccs(0);
		laDataline2.setVoidTransAccs(0);
		laDataline2.setUpdtActn("A");
		laDataline2.setUpdtngEmpId("USERID1");
		laDataline2.setTransAMDate(37163);
		laDataline2.setTransTime(101010);
		lvData.addElement(laDataline2);
		return lvData;
	}
}
