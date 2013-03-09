package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import com.txdot.isd.rts.services.data.RenewalEMailData;
import com.txdot.isd.rts.services.data.VoidUIData;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;

/*
 * EReminderBouncebackReport.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------------
 * B Brown		08/02/2010	Initial writing of class for Ereminder
 * 							project.
 * 							defect 10512 Ver POS_650
 * B Brown		08/04/2010	Add total to report and add constants
 * 							modify formatReport(), 
 * 								   generateColumnHeadings()
 * 							defect 10512 Ver POS_650
 * ---------------------------------------------------------------------
 */
/**
 * Prepares the EReminder Bounceback Report
 *
 * @version	POS_650  		08/04/2010
 * @author	Bob Brown 
 * <br>Creation Date:		08/02/2010 15:36:22
 */
public class EReminderBouncebackReport extends ReportTemplate
{
	// character locations for print
	private final static int siCharStart_EmailData = 2;
	private final static int siCharStart_CustNameData = 50;
	private final static int siCharStart_DateSentData = 72;
	private final static int siCharStart_DocNoData = 84;
	private final static int siCharStart_VinData = 105;
	private final static int siCharStart_PlateData =124;
	private final static int siCharLength_EmailData = 46;
	private final static int siCharLength_CustNameData = 20;
	private final static int siCharLength_DateSentData = 10;
	private final static int siCharLength_DocNoData = 17;
	private final static int siCharLength_VinData = 17;
	private final static int siCharLength_PlateData = 7;
	private final String ssColHdg_Customer_Hdg1 = "CUSTOMER";
	private final String ssColHdg_Customer_Name_Hdg1 = "CUSTOMER NAME";
	private final String ssColHdg_Date_Hdg1 = "DATE";
	private final String ssColHdg_DocNo_Hdg1 = "DOCNO";
	private final String ssColHdg_Vin_Hdg1 = "VIN";
	private final String ssColHdg_Plate_Hdg1 = "PLATE";	
	private final String ssColHdg_EmailAddress_Hdg2 = "EMAIL ADDRESS";
	private final String ssColHdg_Sent_Hdg2 = "SENT";
	
	private final static int siCharLength_Customer_Hdg1 = 8;
	private final static int siCharLength_Customer_Name_Hdg1 = 13;
	private final static int siCharLength_Date_Hdg1 = 4;
	private final static int siCharLength_DocNo_Hdg1 = 5;
	private final static int siCharLength_Vin_Hdg1 = 3;
	private final static int siCharLength_Plate_Hdg1 = 5;
	private final static int siCharLength_EmailAddress_Hdg2 = 13;
	private final static int siCharLength_Sent_Hdg2 = 4;

	private final String ssColHdg_EmailData_Dashes = 
		"----------------------------------------------";
	private final String ssColHdg_Customer_Name_Dashes = 
		"--------------------";
	private final String ssColHdg_Date_Dashes = 
		"----------";
	private final String ssColHdg_DocNo_Dashes = 
		"-----------------";
	private final String ssColHdg_Vin_Dashes = 
		"-----------------";
	private final String ssColHdg_Plate_Dashes = 
		"-------";
		
	private final String RTS = "REGISTRATION AND TITLE SYSTEM";
	private final String BOUNCE_BACK_REPORT = "eRENEWALS BOUNCE-BACK REPORT";	
	private final String EXCEPTION_ERROR =
		"Exception occurred in formatReport() of "
			+ "com.txdot.isd.rts.server.webapps.registrationrenewal.EReminderBouncebackReport";
	private final String TOTAL_BOUNCEBACKS = "TOTAL eRENEWALS BOUNCED BACK: ";		
	private int ciPrintLinesAvailable = 58;
	 
	/**
	 * EReminderBouncebackReport constructor
	 * 
	 * @param asRptString String
	 * @param aaRptProperties ReportProperties
	 */
	public EReminderBouncebackReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * format report: heading column heading, body, footer
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		boolean lbMoreRecords = true;
		int liNumberOfRecords = avResults.size();
		int liRecordIndex = 0; 
		String[] larrEmailAddress = new String[liNumberOfRecords];
		String[] larrCustomerName = new String[liNumberOfRecords];
		String[] larrDateSent = new String[liNumberOfRecords];
		String[] larrDocNo = new String[liNumberOfRecords];
		String[] larrVIN = new String[liNumberOfRecords];
		String[] larrPlate = new String[liNumberOfRecords];

		try
		{
			for (int i = 0; i < liNumberOfRecords; i++)
			{
				RenewalEMailData laRenewalEMailData =
								(RenewalEMailData) avResults.elementAt(i);
				larrEmailAddress[i] = laRenewalEMailData.getRecpntEMail();
				larrCustomerName[i] =
					String.valueOf(laRenewalEMailData.getWindwAddrName1());
				larrDateSent[i] =
					String.valueOf(laRenewalEMailData.getBatchDate());
				larrDocNo[i] =
					String.valueOf(laRenewalEMailData.getDocNo());
				larrVIN[i] =
					String.valueOf(laRenewalEMailData.getVin());
				larrPlate[i] =
					String.valueOf(laRenewalEMailData.getRegPltNo());	
			}

			//print report heading, column headings 
			generateColumnHeadings();

			while (lbMoreRecords)
			{
				for (int k = 0; k < ciPrintLinesAvailable - 1; k++)
				{
					if (liRecordIndex < liNumberOfRecords)
					{
						printDataline(
							larrEmailAddress[liRecordIndex],
							larrCustomerName[liRecordIndex],
							larrDateSent[liRecordIndex],
							larrDocNo[liRecordIndex], 
							larrVIN [liRecordIndex],
							larrPlate[liRecordIndex]);													
						liRecordIndex++;
					}
					else
					{
						lbMoreRecords = false;
					}
				} //end for loop: more data lines
				if (lbMoreRecords)
				{
					generateFooter();
					generateColumnHeadings();
				}      
			}
			
			caRpt.blankLines(3);
			caRpt.print(TOTAL_BOUNCEBACKS + liNumberOfRecords);
			 
		} 
		catch (Throwable aeEx)
		{
			System.err.println(EXCEPTION_ERROR);
			aeEx.printStackTrace();
		} 
		generateFooter(true);  
	}

	/**
	 * creates column heading and intermediate text
	 * 
	 * @param aaI int
	 */
	public void generateColumnHeadings()
	{
// 		generateHeader(lvHeader, lvCenter, lvTable);
		caRpt.print(caRptProps.getUniqueName());
		caRpt.center(caRpt.csName);
		caRpt.center(RTS);
		caRpt.center(BOUNCE_BACK_REPORT);

		//print column headings
		caRpt.blankLines(1);
		caRpt.print(ssColHdg_Customer_Hdg1, siCharStart_EmailData, siCharLength_Customer_Hdg1);
		caRpt.print(ssColHdg_Customer_Name_Hdg1, siCharStart_CustNameData, siCharLength_Customer_Name_Hdg1);
		caRpt.print(ssColHdg_Date_Hdg1, siCharStart_DateSentData, siCharLength_Date_Hdg1);
		caRpt.print(ssColHdg_DocNo_Hdg1, siCharStart_DocNoData, siCharLength_DocNo_Hdg1);
		caRpt.print(ssColHdg_Vin_Hdg1, siCharStart_VinData, siCharLength_Vin_Hdg1);
		caRpt.print(ssColHdg_Plate_Hdg1, siCharStart_PlateData, siCharLength_Plate_Hdg1);
		caRpt.blankLines(1);

		caRpt.print(ssColHdg_EmailAddress_Hdg2, siCharStart_EmailData, siCharLength_EmailAddress_Hdg2);
		caRpt.print(ssColHdg_Sent_Hdg2, siCharStart_DateSentData, siCharLength_Sent_Hdg2);
		caRpt.blankLines(1);
		
		caRpt.print(ssColHdg_EmailData_Dashes, siCharStart_EmailData, siCharLength_EmailData);
		caRpt.print(ssColHdg_Customer_Name_Dashes,  siCharStart_CustNameData, siCharLength_CustNameData);
		caRpt.print(ssColHdg_Date_Dashes, siCharStart_DateSentData, siCharLength_DateSentData);
		caRpt.print(ssColHdg_DocNo_Dashes, siCharStart_DocNoData, siCharLength_DocNoData);
		caRpt.print(ssColHdg_Vin_Dashes, siCharStart_VinData, siCharLength_VinData);
		caRpt.print(ssColHdg_Plate_Dashes, siCharStart_PlateData, siCharLength_PlateData);
		caRpt.blankLines(1);
	}


	/**
	 * starts the application to produce Void Report
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\RTS\\RPT\\VOID.TXT");
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReport.show();
			laFrmPreviewReport.setVisibleRTS(true);
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
	 * printDataline prints body text of Void Report
	 * 
	 * @param asVoidDescription String
	 * @param asTargetEveTransId String
	 * @param asTransId String
	 * @param asTargetInvTransId String
	 */
	private void printDataline(
								String asEmailAddress,
								String asCustomerName,
								String asDateSent,
								String asDocNo, 
								String asVIN,
								String asPlate) 
	{
		//print detail line
		caRpt.print(
			asEmailAddress,
			siCharStart_EmailData,
			siCharLength_EmailData);
		caRpt.print(
			asCustomerName,
			siCharStart_CustNameData,
			siCharLength_CustNameData);
		caRpt.print(
			asDateSent,
			siCharStart_DateSentData,
			siCharLength_DateSentData);
		caRpt.print(
			asDocNo,
			siCharStart_DocNoData,
			siCharLength_DocNoData);
		caRpt.print(
			asVIN,
			siCharStart_VinData,
			siCharLength_VinData);
		caRpt.print(
			asPlate,
			siCharStart_PlateData,
			siCharLength_PlateData);				

		caRpt.blankLines(1);
	}

	/** 
	 * queryData() mimics data from db.
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		//create container
		Vector avResults = new Vector();
		
		VoidUIData laEmailDataLine = new VoidUIData();

		//populate test rec
		laEmailDataLine.setVoidDescription("TITLE APPLICATION RECEIPT");
		laEmailDataLine.setTransId("16110036174111159");
		laEmailDataLine.setTargetEventTransId("16110036174111160");
		laEmailDataLine.setTargetInventoryTransId("16110036174111161");

		// replicate test rec hundred times
		for (int i = 0; i < 100; i++)
		{
			avResults.addElement(laEmailDataLine);
		}
		return avResults;
	}

	/* (non-Javadoc)
	 * @see com.txdot.isd.rts.services.reports.ReportTemplate#generateAttributes()
	 */
	public void generateAttributes()
	{
		// TODO Auto-generated method stub
		
	}
}
