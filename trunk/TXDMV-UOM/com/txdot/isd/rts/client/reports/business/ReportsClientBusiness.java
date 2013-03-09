package com.txdot.isd.rts.client.reports.business;

import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Print;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * ReportsClientBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Btulsiani	05/06/2002		Code to print a duplicate if user 
 *							selects Void Report (#3782)
 * Ray Rowehl	06/13/2002	Always print from tray 2
 * Nancy Ting				CQU100004249
 * Jeff S. 		07/06/2003  Added barcode to Title Package report 
 *							and had to handle the printing 
 *							differently by calling a Print method to 
 *							create a temp file with the barcode PCL
 *							then print and delete the temp file - 
 *							printReports()
 *							defect 3994	
 * Min Wang		06/30/2003	handle Void Report to print dup.
 *							modify printReports()
 *							defect 6252
 * Jeff S.		02/23/2004	Changed from FTP to LPR printing. Removed
 *							call to open and close FTP connection.
 *							Replaced calls to genBarCodeReceiptFile()
 *							and genTempDuplFile(String,String) with
 *							direct sendToPrinter() calls and let the
 *							printDocManager handle printing of dups and
 *							barcode files.(Void rpt and TittlePack rpt)
 *							modify printReports(Object)
 *							defect 6848, 6898 Ver 5.1.6
 * K Harrell	03/18/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify processData()
 * 							add reprintStickerReport()
 * 							Ver 5.2.0
 * Jeff S.		05/26/2004	Removed call to 
 *							Print.getDefaultPrinterProps() b/c it is not
 *							being used anymore.
 *							modify saveReports(Object)
 *							defect 7078 Ver 5.2.0
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3 
 * S Johnston	06/29/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * J Ralph		09/29/2006	Added case 6 FunctionId for Exempt ExportData
 * 							TODO create case 6 FunctionId constant							
 * 							modify processData()
 * 							defect 8900 Ver 5.3.0
 * J Ralph		10/18/2006  FunctionId constant added for Exempt Audit 
 * 							ReportConstants.GENERATE_EXEMPT_AUDIT_EXPORT
 * 							modify processData()
 * 							defect 8900 Ver Exempts
 * K Harrell	03/20/2009	Add case statement for GENERATE_ETITLE_EXPORT
 * 							add Landscape support for ETitle Report. 
 * 							modify processData(), saveReports() 
 * 							defect 9972 Ver Defect_POS_E 
 * K Harrell	08/16/2009	Implement UtilityMethods.addPCLandSaveReport(),
 * 							delete saveReports()
 * 							modify reprintStickerReport(), processData()  
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	06/05/2011  Add logic for Fraud Export 
 * 							modify processData()
 * 							defect 10900 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */
/**
 * Handles the creation of the reports for the Reports Module.
 *
 * @version	6.8.0 			06/05/2011
 * @author	Rakesh Duggirala
 * <br>Creation Date:		09/04/2001 18:36:23
 */
public class ReportsClientBusiness
{
	/**
	 * ReportsClientBusiness constructor
	 */
	public ReportsClientBusiness()
	{
		super();
	}

	/**
	 * Method that prints the generated reports.
	 * 
	 * @param aaData Object The reports, as a vector, to be saved in the 
	 *		form of ReportSearchData.
	 * @return Vector of reports with each in the form of 
	 *		ReportSearchData.
	 * @throws RTSException Thrown when there is an error.
	 */
	public Vector printReports(Object aaData) throws RTSException
	{
		Print laPrint = new Print();
		Vector lvReportsList = (Vector) aaData;

		for (int i = 0; i < lvReportsList.size(); i++)
		{
			GeneralSearchData laGenSearchData =
				(GeneralSearchData) lvReportsList.get(i);
			if ((laGenSearchData.getKey1() != null)
				&& !(laGenSearchData.getKey1()).equals(""))
			{
				String lsFileName = laGenSearchData.getKey1();
				// defect 3994
				// Added barcode to Title Package Report needed to
				// generate PCL for the barcode if this report is the
				// Title Package Report. If the file name's first four
				// character are TTLP then it contains a barcode so then
				// the barcode PCL needes to be generated
				// defect 6848, 6898
				// added void to the if statement  Void report duplicate
				// printing was handled below
				String lsParsedFilename =
					laPrint.parseFileName(lsFileName, 4);
				if (lsParsedFilename
					.equalsIgnoreCase(TransCdConstant.TTLP)
					|| lsParsedFilename.equalsIgnoreCase(
						TransCdConstant.VOID))
				{
					// Now passing the first 4 characters of the
					// filename as the trans code for both Tittle
					// Package and Void both of these reports are
					// special cases because they either contain a
					// barcode or duplicates are needed
					laPrint.sendToPrinter(lsFileName, lsParsedFilename);
					// end defect 6848, 6898
				}
				else
				{
					laPrint.sendToPrinter(lsFileName);
				}
				// end defect 3994
			}
		}
		return lvReportsList;
	}

	/**
	 * Used to pass the module name, function id, and data from the UI
	 * to the server.
	 *
	 * @param aiModule int Represents which module is making the call.
	 * @param aiFunctionId int Internal function id.
	 * @param aaData Object The data.
	 * @return Object The data being passed back from the server.
	 * @throws RTSException Thrown when there is an error.
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		switch (aiFunctionId)
		{
			case ReportConstant.NO_DATA_TO_BUSINESS :
				{
					return aaData;
				}
			case ReportConstant.GENERATE_REPRINT_REPORT :
				{
					return aaData;
				}
			case ReportConstant.PRINT_REPRINT_REPORT :
				{
					return printReports(aaData);
				}
			case ReportConstant.REPRINT_STICKER_REPORT :
				{
					return reprintStickerReport(
						aiModule,
						aiFunctionId,
						aaData);
				}
			// Intentionally dropping through
			// defect 10900 
			case ReportConstant.GENERATE_FRAUD_EXPORT :
			// end defect 10900  
			case ReportConstant.GENERATE_ETITLE_EXPORT :
			case ReportConstant.GENERATE_EXEMPT_AUDIT_EXPORT :
				{
					return Comm.sendToServer(
						aiModule,
						aiFunctionId,
						aaData);
				}
			default :
				{
					// defect 8628
					return UtilityMethods.addPCLandSaveReport(
						Comm.sendToServer(
							aiModule,
							aiFunctionId,
							aaData));
					// end defect 8628 
				}
		}
	}

	/**
	 * reprintStickerReport
	 * 
	 * @param aiModule int Represents which module is making the call.
	 * @param aiFunctionId int Internal function id.
	 * @param aaData Object The data.
	 * @return Object The data being passed back from the server.
	 * @throws RTSException Thrown when there is an error.
	 */
	private Object reprintStickerReport(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		Map laMap = (Map) aaData;
		laMap.put(
			"OFC",
			new Integer(SystemProperty.getOfficeIssuanceNo()));
		laMap.put("SUB", new Integer(SystemProperty.getSubStationId()));
		laMap.put("WS", new Integer(SystemProperty.getWorkStationId()));
		laMap.put("EMP", SystemProperty.getCurrentEmpId());
		Object laTemp =
			Comm.sendToServer(aiModule, aiFunctionId, laMap);
			
		// defect 8628 
		return UtilityMethods.addPCLandSaveReport(laTemp);
		// end defect 8628 
	}


//	/**
//	 * Method to save the reports to the report directory.
//	 * 
//	 * @param aaData Object The report to be saved in the form of 
//	 *		ReportSearchData.
//	 * @return Vector of reports with each in the form of 
//	 *		ReportSearchData.
//	 * @throws RTSException Thrown when there is an error.
//	 */
//	private Vector saveReports(Object aaData) throws RTSException
//	{
//		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
//		UtilityMethods laUtil = new UtilityMethods();
//		Print laPrint = new Print();
//		Vector lvReports = new Vector();
//
//		String lsPageProps = laPrint.getDefaultPageProps();
//
//		// defect 9972
//		// Add support for LandScape for ETitle Report 
//		if (laRptSearchData.getIntKey2() == ReportConstant.LANDSCAPE)
//		{
//			lsPageProps =
//				lsPageProps.substring(0, 2)
//					+ Print.getPRINT_LANDSCAPE()
//					+ lsPageProps.substring(2);
//		}
//		// end defect 9972
//
//		// defect 4249
//		// Always print from tray 2
//		String lsRpt =
//			lsPageProps
//				+ Print.getPRINT_TRAY_2()
//				+ System.getProperty("line.separator")
//				+ laRptSearchData.getKey1();
//		// end defect 4249
//
//		String lsFileName =
//			laUtil.saveReport(
//				lsRpt,
//				laRptSearchData.getKey2(),
//				laRptSearchData.getIntKey1());
//		laRptSearchData.setKey1(lsFileName);
//		lvReports.add(laRptSearchData);
//		return lvReports;
//	}


}