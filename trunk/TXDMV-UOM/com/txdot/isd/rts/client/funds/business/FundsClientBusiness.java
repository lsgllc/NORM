package com.txdot.isd.rts.client.funds.business;

import java.util.Vector;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Print;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * FundsClientBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		02/23/2004	Changed from FTP to LPR printing. Removed
 *							call to open and close FTP connection.
 *							modify printReports(ReportSearchData)
 *							defect 6848, 6898 Ver 5.1.6
 * Jeff S.		05/26/2004	Removed call to 
 *							Print.getDefaultPrinterProps() b/c it is not
 *							being used anymore.
 *							modify saveReports(Object)
 *							defect 7078 Ver 5.2.0
 * K Harrell	10/25/2004	Remove reference to isFirstTransactionOfDay()
 *							modify clsOut()
 *							defect 7581 Ver 5.2.2
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3    
 * B Hargrove	07/11/2005	Modify code for move to Java 1.4.
 *							Bring code to standards
 *  	 					Remove unused variable.
 * 							defect 7886 Ver 5.2.3
 * K Harrell	10/24/2005	Constant renaming. Closeout and 
 * 							Countywide. 
 * 							defect 8379 Ver 5.2.3
 * K Harrell	03/17/2006	Do not pass info to create 'CLSOUT' trans
 * 							add WORKSTATION_CD, SERVER_CD 
 * 							modify clsOut()
 * 							rename checkIsServer() to isServer()  
 * 							defect 8623 Ver 5.2.3
 * K Harrell	05/23/2006	Restoring CLSOUT transaction to CLSOUT 
 * 							process
 * 							modify clsOut()
 * 							defect 8798 Ver 5.2.3
 * K Harrell	10/09/2006	Pass CloseoutendTstmp in vector to Server
 * 							to reflect client timestmp vs. server
 * 							modify clsOut()
 * 							defect 7824 Ver Fall_Admin_Tables   
 * Jeff S.		10/10/2006	Checked code in for Kathy.
 * 							defect 7824 Ver Fall_Admin_Tables 
 * K Harrell	08/26/2009	Implement UtilityMethods.addPCLandSaveReport(),
 * 							 Cleanup.  Used ReportConstant for the 
 * 							 interesting "0" for copies flag. 
 * 							 Implement SystemProperty.isClientServer()
 * 							delete LINE_SEPARATOR
 * 							delete isServer()
 * 							modify processData(), saveReports()  
 * 							defect 8628 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */

/**
 * Client business passes control to server side.
 *
 * @version	Defect_POS_F		08/26/2009 
 * @author	Bobby Tulsiani
 * <br>Creation Date:			09/06/2001 13:30:59
 */

public class FundsClientBusiness
{

	/**
	 * FundsClientBusiness constructor comment.
	 */

	public FundsClientBusiness()
	{
		super();
	}

	/**
	 * Method sets up Transaction Header Data for the closeout
	 * 
	 * @param aaFundsData FundsData 
	 * @return Object
	 * @throws RTSException
	 */
	private Object clsOut(FundsData aaFundsData) throws RTSException
	{
		// defect 8798 
		// Restore code to create CLSOUT transaction (remove defect 8623) 
		// Create vector to pass FundsData & Transaction Data
		Vector lvData = new Vector();
		Transaction laTransaction =
			new Transaction(TransCdConstant.CLSOUT);
		CompleteTransactionData laCompleteTransactionData =
			new CompleteTransactionData();
		// if First Transaction of day, need to account for it
		laTransaction.setUpFirstTransactionOfDay();
		laCompleteTransactionData.setTransCode(TransCdConstant.CLSOUT);
		TransactionHeaderData laTransactionHeaderData =
			laTransaction.addTransHeader(
				laCompleteTransactionData,
				true);
		lvData.addElement(laTransactionHeaderData);
		lvData.addElement(aaFundsData);
		// defect 7824 
		// Pass client for closeoutendtstmp 
		lvData.addElement((new RTSDate()).getDB2Date());
		// end defect 7824

		//Generate Payment Report, and send data
		return Comm.sendToServer(
			GeneralConstant.FUNDS,
			FundsConstant.GENERATE_PAYMENT_REPORT,
			lvData);
		// end defect 8798
	}

	/**
	 * Method calls print report functions for all reports returned from 
	 * the fundsSeverSide
	 * 
	 * @param aaRptSearchData ReportSearchData 
	 * @return Object
	 * @throws RTSException
	 */
	private Object printReports(ReportSearchData aaRptSearchData)
		throws RTSException
	{
		Print laPrint = new Print();
		Vector lvReportsList = (Vector) aaRptSearchData.getVector();

		//If no reports to print, exit	
		if (aaRptSearchData.getIntKey3() == 0)
		{
			return aaRptSearchData;
		}

		for (int i = 0; i < lvReportsList.size(); i++)
		{
			GeneralSearchData laGenSearchData =
				(GeneralSearchData) lvReportsList.get(i);
			if ((laGenSearchData.getKey1() != null)
				&& !(laGenSearchData.getKey1()).equals(""))
			{
				String lsFileName = laGenSearchData.getKey1();

				laPrint.sendToPrinter(lsFileName);
			}
		}
		return aaRptSearchData;
	}

	/**
	 * Method process data.
	 * 
	 * @param aiModule int 
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		ReportSearchData laRptSearchData = new ReportSearchData();
		switch (aiFunctionId)
		{
			case FundsConstant.NO_DATA_TO_BUSINESS :
				{
					return aaData;
				}
			case FundsConstant.PRINT_REPORTS :
				{
					laRptSearchData =
						saveReports(
							Comm.sendToServer(
								aiModule,
								aiFunctionId,
								aaData));
					return printReports(laRptSearchData);
				}
			case FundsConstant.DISPLAY_REPORTS :
				{
					return saveReports(
						Comm.sendToServer(
							aiModule,
							aiFunctionId,
							aaData));
				}
			case FundsConstant.CLOSE_OUT_FOR_DAY :
				{
					laRptSearchData =
						saveReports(
							(ReportSearchData) clsOut(
								(FundsData) aaData));
					return printReports(laRptSearchData);
				}
				// defect 8628 
				// Intentionally falling through 
			case FundsConstant.GENERATE_COUNTYWIDE_REPORT :
			case FundsConstant.GENERATE_SUBSTATION_SUMMARY_REPORT :
			case FundsConstant.GENERATE_CLOSEOUT_STATISTICS_REPORT :
				{
					return UtilityMethods.addPCLandSaveReport(
						Comm.sendToServer(
							aiModule,
							aiFunctionId,
							aaData));
				}
				// end defect 8628
			default :
				{
					return Comm.sendToServer(
						aiModule,
						aiFunctionId,
						aaData);
				}
		}
	}

	/**
	 * Method calls save and display report functions for all reports 
	 * returned from the FundsServerSide
	 * 
	 * @param aaData Object
	 * @return ReportSearchData
	 * @throws RTSException
	 */
	private ReportSearchData saveReports(Object aaData)
		throws RTSException
	{
		ReportSearchData laGroupRptSearchData =
			(ReportSearchData) aaData;

		Vector lvReports = new Vector();

		Vector lvReportsList =
			(Vector) laGroupRptSearchData.getVector();

		// Flag to check if report has data
		int liSetFlag = 0;

		// For each report that has data, save report
		for (int i = 0; i < lvReportsList.size(); i++)
		{
			ReportSearchData laRptSearchData =
				(ReportSearchData) lvReportsList.get(i);

			if ((laRptSearchData.getKey1() != null)
				&& !(laRptSearchData.getKey1()).equals(""))
			{
				// defect 8628
				// This is a flag for Funds Workstation Reports
				//laRptSearchData.setIntKey1(0);
				laRptSearchData.setIntKey1(
					ReportConstant.RPT_COPIES_FUNDS_FILENAME_BY_WSID);

				Vector lvRptSearchData =
					UtilityMethods.addPCLandSaveReport(laRptSearchData);

				if (lvRptSearchData != null
					&& lvRptSearchData.size() > 0)
				{
					lvReports.addElement(lvRptSearchData.elementAt(0));
				}
				// end defect 8628  
				liSetFlag++;
			}
		}
		laGroupRptSearchData.setVector(lvReports);
		laGroupRptSearchData.setIntKey3(liSetFlag);

		return laGroupRptSearchData;
	}
}
