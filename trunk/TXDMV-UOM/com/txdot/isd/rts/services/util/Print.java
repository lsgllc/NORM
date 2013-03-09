package com.txdot.isd.rts.services.util;

import java.io.*;
import java.math.BigDecimal;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.AssignedWorkstationIdsData;
import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.PrintDocumentData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.PrintDocManager;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.SystemControlBatchConstant;

/*
 * Print.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Raj.	11/07/2001	created the method
 * Marx Raj.	11/14/2001	Added code to print receipts
 * Marx Raj.	12/18/2001	Changed the size of barcode and
 *							adding the barcode text BEFORE
 *							the PCL for barcode (to print text
 *							below the lines).
 * R Hicks		03/01/2002	Remote print using FTP rather than LPR
 * MAbs			04/23/2002	If FTP printing fails, don't stop 
 *							application.  Just print error to batchlog 
 *							CQU100003632
 * BTulsiani	04/29/2002	Modified ftp connection, to not check for 
 *							"local printer" (bug #3683)
 * S Govindappa	04/29/2002	CQU100003464 Changed genTempDuplFile
 *							(String,String) to make duplicate receipts 
 *							print from Tray2
 * Marx Raj.	04/30/2002	Fixed printing multiple transids in DTA
 *							transactions under Barcode.
 * S Govindappa	05/07/2002	CQU100003792 Changed genTempDuplFile(String) 
 *							to make duplicate receipts print from Tray2 
 *							when PrintImmediate is ON
 * Ray Rowehl	10/24/2002	CQU100004872. Add logging for debug.
 * RHicks		10/30/2020	CQ4999 - DosCreateProcess rc=8 fix
 * Ray Rowehl	01/14/2003	CQU100005211 - use FTPUser instead of DBUser 
 *							in openFTPConnection
 * Ray Rowehl	02/20/2003	CQU100005252 - set up openFtpConnection to
 *							use passed parameters to connect to a 
 *							specified workstation.
 * Min Wang		03/20/2003	Modified genTempDuplFile(). CQU100005781 
 * J Rue		04/30/2003	Defect 6032, Create method call 
 * 							getPatchCode() that will generate the patch 
 * 							code on the SCOT and NRCOT receipts.
 *							Modify initializeEnv(),
 *							New method setPRINT_SLVG_TOP_MARGIN and 
 *							getPatchCode()
 * Min Wang		06/17/2003	re-merge defect 5781
 *							Add getDTAPageSettings()
 *								
 * Jeff S.		07/18/2003	Added feature to handle the printing of 
 * 							barcodes on reports for the Tittle Package 
 * 							Report. 
 *							genBarCodeReceiptFile(), writeOutput(), 
 *							barCodeTo2Of5x()
 *							added translateToPCLReports(),
 *							added parseFileName() - Defect# 3994
 * Min Wang		06/30/2003	add handling for Void dup file
 *							add method getVoidPageProps()
 *							modify genTempDuplFile(string, string)
 *							defect 6252
 * Jeff S.		02/23/2004	Modified entry to Print Class. FTP printing 
 * 							is now LPR printing for XP. sendToPrinter() 
 * 							is the method to call to execute a print 
 * 							job. The PrintDocManager should be used to 
 * 							handle dups and barcode files.  A 
 * 							ping(String) method was created since 
 * 							openFtpConnection() was used before to check 
 *							if the WS was online.  Moved what was done 
 *							in closeFtpConnection() to 
 *							sendToPrinter( , , , ).
 *							copied sendToPrinter(String,String) to 
 *							sendToPrinter_old(String,String) and 
 *								deprecated it
 *							add sendToPrinter(String)
 *							add sendToPrinter(String, String)
 *							add sendToPrinter(String, String, int, int)
 *							add setConnectionStatus(int, int)
 *							add ping(String), printCerts(), 
 *								printReports(), printReceipts()
 *							add executePrint(String)
 *							add isBatch(int, int)
 *							deprecate sendToLocalPrinter(String)
 *							deprecate printReceipt(String)
 *							deprecate openFtpConnection(int, int)
 *							deprecate openFtpConnection()
 *							deprecate closeFtpConnectionForBatch()
 *							deprecate closeFtpConnection()
 *							deprecate genTempDuplFile(String)
 *							deprecate sendToNetworkPrinter(String)
 *							modify genBarCodeReceiptFile(String, String)
 *							modify genTempDuplFile(String, String)
 *							defect 6648, 6698 Ver 5.1.6
 * Jeff S.		03/01/2004	Added PrintStatus to handle printing instead
 *							of using prodStatus.
 *							modify executePrint(String), 
 *							printReports(), printReceipts(), 
 *								printCerts()
 *							defect 6771 Ver 5.1.6
 * Jeff S.		03/11/2004	Added Logging for print exceptions
 *							modify setConnectionStatus(int, int)
 *							modify sendToPrinter()
 *							defect 6648, 6698 Ver 5.1.6
 * Jeff S.		04/27/2004	Comment out all references to FTP so that
 *							ftp package can be moved into another 
 *							project.
 *							comment out closeFtpConnection()
 *							comment out sendToNetworkPrinter(String)
 *							comment out openFtpConnection(int, int)
 *							comment out openFtpConnection()
 *							comment out closeFtpConnectionForBatch()
 *							defect 7045 Ver 5.1.6
 * Jeff S.		05/06/2004	Removed the code that was commented out when
 *							moving the ftp package.  Removed deprecated
 *							methods.
 *							defect 7045 Ver 5.2.0
 * Jeff S.		05/12/2004	Removed the Class level variables that
 *							where used by the ReceiptTemplate class to
 *							generate the sticker layout and apply PCL to
 *							receipts and reports.  All PCL is created by
 *							calling the PCLUtilities class.
 *							modify initializeEnv()
 *							defect 7079 Ver 5.2.0
 * Jeff S.		05/13/2004	Found a problem with the merge from 5.1.6 to 
 *							5.2.0.  A new field was added to barcode tag
 *							and was not handled when TTLP RPT is 
 *							printed.
 *							deprecate translateToPCL()
 *							modify writeOutput()
 *							modify Print()
 *							5.2.0 Merge
 * Jeff S.		05/14/2004	Make print return exception so that if there
 * 							was an exception durring printing the 
 * 							sticker the prntinvqty filed would not get 
 * 							updated.
 *							add cbReturnException
 *							modify sendToPrinter()
 *							add setReturnException(boolean)
 *							add isExceptionReturned()
 *							defect 7067 Ver. 5.2.0
 * Jeff S.		05/26/2004	In 5.2.0 a designated print tray was 
 * 							specified in the PCL Header of each report. 
 * 							This was causing the dupicate receipt to not
 * 							page correctly.
 *							modify getDefaultPageProps()
 *							modify genTempDuplFile(String, String)
 *							deprecate getVoidPageProps()
 *							deprecate genBarCodeDuplFile(String)
 *							deprecate getDefaultPrinterProps()
 *							deprecate getDTAPageSettings()
 *							deprecate getDupHeaderLength()
 *							deprecate getDuplicateHeader()
 *							deprecate getHeaderLength()
 *							deprecate getHeaderString()
 *							deprecate setDupHeaderLength(int)
 *							deprecate setHeaderLength(int)
 *							deprecate writeDupCopyOutput()
 *							defect 7078 Ver. 5.2.0
 * Jeff S.		06/03/2004	Barcode Receipt files where printing on 
 * 							Sticker paper (tray3).  This is b/c the 
 * 							barcode is combined with the original and 
 * 							the original contains a tray3 definition.  
 * 							All tray definitions are now replaced with a
 * 							tray2 definition.
 *							modify writeOutput(,,,)
 *							defect 7141 Ver. 5.2.0
 * Jeff S.		06/28/2004	Certificates are special cases and contains 
 *							a barcode sent to tray1. Do not remove tray1 
 *							definitions and replace them with tray 2 as 
 *							was done with 7141.
 *							modify writeOutput(,,,)
 *							defect 7184, 7227 Ver. 5.2.0
 * Ray Rowehl	09/03/2004	Do not try to display exception if on server
 *							prevent null pointer for the new properties.
 *							add DEFAULTSTICKERSETTINGS, 
 *								DEFAULTSTICKERTRAY
 *							modify initializeEnv()
 *							defect 7530 Ver 5.2.1
 * Jeff S.		09/27/2004	Error msg misspelled.
 *							modify ping(String)
 *							defect 7562 Ver. 5.2.1
 * Jeff S.		12/30/2004	Added printer reset PCL used on all of the
 *							certificates.
 *							add getPRINT_RESET()
 *							delete deprecated getVoidPageProps()
 *							delete deprecated barCodeTo2Of5x
 *							(String, double, boolean)
 *							delete deprecated barCodeTo2Of5x
 *							(String, double, String)
 *							delete deprecated genBarCodeDuplFile(String)
 *							delete deprecated getDefaultPrinterProps()
 *							delete deprecated getDTAPageSettings()
 *							delete deprecated getDupHeaderLength()
 *							delete deprecated getDuplicateHeader()
 *							delete deprecated getHeaderLength()
 *							delete deprecated getHeaderString()
 *							delete deprecated setDupHeaderLength(int)
 *							delete deprecated setHeaderLength(int)
 *							delete deprecated translateToPCL()
 *							delete deprecated writeDupCopyOutput
 *							(String, String, String, String)
 *							defect 7826 Ver 5.2.2
 * Jeff S.		01/04/2005	When deprecated method barCodeTo2Of5x
 *							(String,double,boolean) was removed it 
 *							caused an error b/c it was being referenced 
 *							in writeOutput.
 *							modify writeOutput(,,,)
 *							defect 7863 Ver 5.2.2
 * Jeff S.		04/04/2005	Error msg misspelled.  First time through 
 * 							caught only one of the words. Cleaned up 
 * 							class.
 *							modify setConnectionStatus()
 *							delete deprecated main()
 *							defect 7562 Ver. 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3 
 * S Johnston	07/01/2005  Java 1.4 Cleanup \ Static initialization fix
 * 							add getSTICKER_SETTINGS(), 
 * 							setSTICKER_SETTINGS()
 * 							modify the static block to the bottom of the
 * 							static class level variables, and modified
 * 							ssSTICKER_SETTINGS to be private
 * 							defect 7896 Ver 5.2.3
 * K Harrell	10/19/2005	renamed static final variables
 * 							cbPING_STATUS_AVALIABLE,cbPING_STATUS_NOT_AVALIABLE,
 * 							PING_RESULT_AVALIABLE,PING_RESULT_NOT_AVALIABLE,
 * 							csPingTimeout,csPingNumTries 
 * 							implement displayError()
 * 							modify initializeEnv(),
 * 							 sendToPrinter(String,String,int,int) 
 * 							defect 6456 Ver 5.2.3
 * J Zwiener	12/08/2005	add getPRINT_TRAY_STICKER 
 * 							defect 7896 Version 5.2.3  
 * J Rue		05/29/2008	Make copy of translateToPCL() save as
 * 							translateToPCLReceipts to handle both 
 * 							Receipts and Certificates
 * 							add translateToPCLReceipts()
 * 							add LS_RECEIPTS, LS_CERTIFICATES
 * 							MODIFY barCodeTo2Of5x()
 *							deprecate translateToPCL()
 * 							defect 9668 Ver Defect_POS_A
 * J Rue		06/18/2008	Copy code from 05/29/2008 05:04:20 
 * 							The 05/30/2008 11:18:05 contain sorted 
 * 							methods and variables that caused printing
 * 							issues.
 * 							defect 9668 Ver Defect_POS_A
 * T Pederson	07/10/2008	Added rtsLocalPrint method to print to the   
 * 							USB port instead of copying to prn.
 * 							add rtsLocalPrint()
 * 							modify executePrint()
 *							defect 9272 Ver MyPlates_POS
 * T Pederson	09/25/2008	Changed logging of printer name from sysout   
 * 							to LOG.write.
 * 							modify rtsLocalPrint()
 *							defect 9797 Ver Defect_POS_B
 * K Harrell	06/19/2009	make getDefaultPageProps() static
 * 							delete COPY_COMMAND, PRN_COMMAND
 * 							modify getDefaultPageProps() 
 * 							defect 10023 Ver Defect_POS_F 
 * K Harrell	12/15/2009	Corrected typo:  diabled => disabled
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	06/30/2010	added ssPERMIT_TOP_MARGIN, get method
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/08/2010	Print Permit even when PrintStatusCd = 1
 * 							modify printReceipts()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/14/2010	implement UtilityMethods.printsPermits()
 * 							modify printsReceipts()
 * 							defect 10491 Ver 6.5.0 
 * Min Wang		07/21/2010	Using custom size for CCO.
 * 							add ssCUSTOM, getCUSTOM_SIZE()
 * 							delete ssPRINT_PAGE_LENGTH_22,
 * 							ssPRINT_PAGE_LENGTH_29, ssPRINT_PAGE_LENGTH_48,
 * 							ssPRINT_PAGE_LENGTH_56, ssPRINT_PAGE_LENGTH_88 
 * 							getPRINT_PAGE_LENGTH_22(),getPRINT_PAGE_LENGTH_29(),
 * 							getPRINT_PAGE_LENGTH_48(),getPRINT_PAGE_LENGTH_56(),
 * 							getPRINT_PAGE_LENGTH_88()
 * 							modify initializeEnv()
 * 							defect 10398 Ver 6.5.0
 * K Harrell	12/26/2010	add TEMP_PERMIT_FILE
 * 							modify genBarCodeReceiptFile(), 
 * 							 printReceipts()
 * 							defect 10700 Ver 6.7.0  
 * ---------------------------------------------------------------------
 */
/**
 * Used for printing Receipts and Reports. This class reads the 
 * printer.cfg file to set the printing environment, default fonts etc. 
 * Then, prints the documents.
 *
 * @version	6.7.0 			12/26/2010
 * @author	Marx Rajangam
 * <br>Creation Date:		11/07/2001
 */
public class Print
{
	// defect 6648, 6698
	// Changed from FTP_SERVER to LPR_SERVER
	private static final int LPR_SERVER = 1;
	private static final int LOCAL_PRINTER = 2;
	private static final int NO_PRINTER = 0;
	// Changed to conform to standards
	private String csPrinterName = "";
	// Added csServerName
	private String csServerName = "";
	// Changed to follow standards
	private int ciConnectionStatus = NO_PRINTER;
	// Added file extensions
	private static final String EXTENSION_RECEIPTS = ".RCP";
	private static final String EXTENSION_CERTS = ".CRT";
	private static final String TEMP_BARCODE_FILE = "tempbar.tmp";

	// defect 10700 
	private static final String TEMP_PERMIT_FILE = "tempprmt.tmp";
	// end defect 10700 

	private static final String CMD_EXE = "cmd.exe /c ";
	private static final String LPR_COMMAND = "lpr ";
	private static final String SWITCH_SERVER = "-S ";
	private static final String SWITCH_PRINTER = " -P ";
	private String csErrorMsg = "";
	// end defect 6648, 6698

	// defect 9797
	private static final String PRINTER_EQ_TXT = "Printer = ";
	// end defect 9797

	// defect 7067
	// Make print return exception so that if there was an exception
	// during printing the sticker the prntinvqty field would not get
	// updated.
	private boolean cbReturnException = false;
	// end defect 7067

	private String csBarcodeTrans = null;
	private static String carrBarCodeValues[] = new String[100];
	private static final String START_CODE = "0000";
	private static final String END_CODE = "100";
	/**
	 * The Escape Character value in PCL
	 */
	private static final String ESC_CHAR = "\u001b";
	/**
	 * Height of the barcode generated. 
	 * 1/2 inch in decipoints
	 * decipoints = dots * 720/dpi
	 * private static final int HBAR = 360;
	 */
	private static final int HBAR = 415;
	// defect 9668
	/**
	 * Position Trans code. 
	 * Set "line.separator"
	 * Receipts
	 */
	private static final int LS_RECEIPTS = 55;
	/**
	 * Position Trans code. 
	 * Set "line.separator"
	 * Certificates
	 */
	private static final int LS_CERTIFICATES = 49;
	// end defect 9668
	/**
	 * Width of the narrow bar code line. The thicker barcode line 
	 * is a multiple of 
	 * this number. Hence, this number controls the width of the barcode.
	 * 7.2 = 3 dots for narrow bar width at 300 dpi
	 * decipoints = dots * 720/dpi
	 * 	//private static final double WBAR = 7.2;
	 */
	private static final double WBAR = 10.1;
	/**
	 * The properties for printing
	 */
	private String csFILE_NAME = "";
	private String csOUT_FILE = "";
	private String csDUP_OUT_FILE = "";
	private static String ssWATERMARK_FILE = "";
	private static String ssPRINTER_NAME = "";
	private static String ssTRAY_PCL = "";
	private static String ssCOPIES_PCL = "";
	private static String ssDUP_COPIES_PCL = "";
	private static String ssORIENTATION_PCL = "";
	private static String ssLINE_SPACING_PCL = "";
	private static String ssTOP_MARGIN_PCL = "";
	private static String ssLEFT_MARGIN_PCL = "";
	/**
	 * Properties file name for printing specifics
	 */
	private static final String PROPERTIES_NAME = "cfg/printer.cfg";
	/**
	 * String pattern is used to denote the esc character. 
	 * The escape character cannot be used in the properties txt file.
	 */
	private static final String ESC_IN_PROPS = "csEscChar";
	private static String ssPRINT_ELITE = "";
	private static String ssPRINT_LQ10 = "";
	private static String ssPRINT_10CPI = "";
	private static String ssPRINT_12CPI = "";
	private static String ssPRINT_BOLD = "";
	private static String ssPRINT_NORMAL = "";
	private static String ssPRINT_CONDENSED = "";
	private static String ssPRINT_PICA = "";
	private static String ssPRINT_LASER = "";
	private static String ssPRINT_HDRLASER = "";
	private static String ssPRINT_LINE_SPACING_1_6 = "";
	private static String ssPRINT_HORHDRSPC12 = "";
	private static String ssPRINT_HORHDRSPC6 = "";
	private static String ssPRINT_LINE_SPACING_1_8 = "";
	// defect 10398
	//private static String ssPRINT_PAGE_LENGTH_22 = "";
	//private static String ssPRINT_PAGE_LENGTH_29 = "";
	//private static String ssPRINT_PAGE_LENGTH_48 = "";
	//private static String ssPRINT_PAGE_LENGTH_56 = "";
	//private static String ssPRINT_PAGE_LENGTH_88 = "";
	private static String ssCUSTOM_SIZE = "";
	// end defect 10398
	private static String ssPRINT_ADVANCE_PAPER_9 = "";
	private static String ssPRINT_TOP_MARGIN = "";
	private static String ssPRINT_LEFT_MARGIN_10 = "";
	private static String ssPRINT_TRAY_1 = "";
	private static String ssPRINT_TRAY_2 = "";
	private static String ssPRINT_TRAY_3 = "";
	private static String ssPRINT_COA_LEFT_MARGIN = "";
	private static String ssPRINT_COA_TOP_MARGIN = "";
	private static String ssPRINT_LANDSCAPE = "";
	// defect 6032
	private static String ssPRINT_SLVG_TOP_MARGIN = "";
	// end defect 6032	

	// defect 10491 
	private static String ssPERMIT_TOP_MARGIN =
		ssPRINT_LINE_SPACING_1_8 + ESC_CHAR + "&l2E";
	// end defect 10491 

	/**
	 * BarCode Tag beginning - Loaded from the properties file
	 * Usually is <b><Barcode</b>
	 */
	private static String ssBARCODE_TAG_BEGIN = "";
	/** 
	 * The end of barcode tag. Loaded from the properties file
	 * Usually is <b>/></b>
	 */
	private static String ssBARCODE_TAG_END = "";
	/**
	 * The number of original copies to be printed
	 */
	private static int siCopies = 1;
	/** 
	 * Number of duplicatie copies to be printed
	 */
	private static int siDupCopies = 0;

	/** 
	 * Error codes used 
	 */
	//private static int siOPEN_RECEIPT_FILE_FAILED = 241;
	private static int WRITE_RECEIPT_FAILED = 243;
	/**
	 * To add the barcode prefix (line breaks and spaces) as required. 
	 */
	private boolean cbBarcodePrefixExists = false;
	// defect 6848, 6898
	// Added ping to check to see if a Workstation is online
	// Ping Status
	/**
	 * Ping Workstation Online 
	 */
	private final static boolean PING_STATUS_AVAILABLE = true;
	/**
	 * Ping Workstation Offline 
	 */
	private final static boolean PING_STATUS_NOT_AVAILABLE = false;
	/**
	 * Ping Timmeout in milliseconds
	 */
	private static final String PING_TIMEOUT = "2000";
	/**
	 * Ping number of tries
	 */
	private static final String PING_NUM_TRIES = "1";
	/**
	 * Ping Command
	 */
	private static final String PING = "ping ";
	private static final String SWITCH_NUM_TRIES = " -n ";
	private static final String SWITCH_TIME_OUT = " -w ";
	/**
	 * Ping Results
	 */
	private static final String PING_RESULT_AVAILABLE = "Reply from";
	private static final String PING_RESULT_NOT_AVAILABLE =
		"Ping request could not find host";
	private static final String PING_RESULT_TIME_OUT =
		"Request timed out.";

	// defect 7530
	/**
	 * Default value for Sticker Printing PCL
	 * <p>This value should come from the print.cfg
	 * property ssSTICKER_SETTINGS.
	 */
	private static final String DEFAULTSTICKERSETTINGS =
		"csEscChar(8UcsEscChar(s0p16h10v0s0b3TcsEscChar&l4CcsEscChar&a15L";

	/**
	 * Default value for Sticker Tray.
	 * <p>This value should come from the print.cfg
	 * property ssPRINT_TRAY_STICKER.
	 */
	private static final String DEFAULTSTICKERTRAY = "csEscChar&l5H";
	// end defect 7530
	private static String ssSTICKER_SETTINGS = "";
	private static String ssPRINT_TRAY_STICKER = "";

	// **** This static block must be placed after all class level 
	// variables have been initialized ****
	static {
		initializeEnv();
	}

	// End PCR 34 

	/**
	 * Constructor.
	 */
	public Print()
	{
		super();
		// This was not here in 5.1.6 we need to investigate why
		initializeEnv();
	}

	/**
	 * Constructor. Should be called with the full path of filename 
	 * that needs to be printed. 
	 * 
	 * @param asInputFileName String
	 * @throws RTSException
	 */
	public Print(String asInputFileName) throws RTSException
	{
		csFILE_NAME = asInputFileName;
		FileInputStream lpfsFIS = null;
		try
		{
			//create the default filenames
			lpfsFIS = new FileInputStream(PROPERTIES_NAME);
			Properties laPrp = new Properties();
			laPrp.load(lpfsFIS);
			//csFILE_NAME = prp.getProperty("FILE_NAME");
			csOUT_FILE = laPrp.getProperty("OUT_FILE");
			csDUP_OUT_FILE = laPrp.getProperty("DUP_OUT_FILE");
			ssWATERMARK_FILE = laPrp.getProperty("WATERMARK_FILE");
			ssPRINTER_NAME = laPrp.getProperty("PRINTER_NAME");
		}
		catch (MissingResourceException leMREx)
		{
			//MRE.printStackTrace();
			RTSException laRTSE =
				new RTSException(RTSException.SYSTEM_ERROR, leMREx);
			throw laRTSE;
		}
		catch (Exception leEx)
		{
			RTSException laRTSE =
				new RTSException(RTSException.SYSTEM_ERROR, leEx);
			throw laRTSE;
			//e.printStackTrace(); 
		}
		finally
		{
			try
			{
				lpfsFIS.close();
			}
			catch (Exception leEx)
			{
				RTSException laRTSE =
					new RTSException(RTSException.SYSTEM_ERROR, leEx);
				throw laRTSE;
				//ex.printStackTrace();
			}
		}
	}

	/**
	 * Calculates the checksum required for the barcode. The checksum 
	 * for barcode is calculated as follows:  A single digit number  is 
	 * obtained by adding all digits in the barcode repeatedly. The 
	 * difference between this number and the numeral 10 is the checksum
	 * of the barcode.
	 *
	 * <p> Here is a simple example: Assume that the barcode is 
	 * <code>11223344556677</code>. Then the checksum would be 
	 * calculated as follows:
	 * <ul>
	 * 		<li> Temp <code> = 1 + 1 + 2 + 2 + 3 + 3 + 4 + 4 + 5 + 5 + 6 
	 *											+ 6 + 7 + 7 = 56 </code> 
	 * 		<li> Temp <code> = 5 + 6 = 11 </code> 
	 * 		<li> Temp <code> = 1 + 1 = 2 </code> 
	 *								(This is the single digit number)
	 * 		<li> Checksum <code>  = 10 - 2 = 8. </code> 
	 *						(The checksum for the above barcode is 8). 
	 * </ul> 
	 * 
	 * calculate_checksum:
	 *
	 * Step 1:
	 * Identify even and odd positioned characters in the message
	 * with the right hand message character ALWAYS defined as an
	 * even positioned character.
	 * 
	 * Step 2:
	 * Sum the numeric values of the odd positioned characters 
	 *
	 * Step 3:
	 * Sum the numeric values of the even positioned characters
	 * and multiply the total value by 3. 
	 *
	 * Step 4:
	 * Sum the odd and event totals from steps 2 and 3. 
	 * 
	 * Step 5:
	 * Determine the smallest number which, when added to the sum
	 * in step 4, will result in a multiple of 10. This number is
	 * the value of the checksum character
	 *
	 * Step 6:
	 * Determine whether the total number of characters (message
	 * plus checksum) is odd or even. If odd, and a leading, non
	 * significant zero to the message to produce an even number
	 * of characters.
	 * 
	 * @param asBarCodeText String
	 * @return String
	 */
	private String addChecksum(String asBarCodeText)
	{
		StringBuffer laBarCodeTextWithChecksum =
			new StringBuffer(asBarCodeText);
		final int liCHECKSUM_NO = 10;
		char lchBufferInt = '0';
		/**
		 * Step 1
		 */
		laBarCodeTextWithChecksum.reverse();
		int liEvenNoValue = 0;
		int liOddNoValue = 0;
		String lsBarCodeTextWithChecksum =
			laBarCodeTextWithChecksum.toString().trim();
		/**
		 * Step 2
		 * this is the sum of the odd positioned chars in the CURRENT 
		 * string hence it is the sum of the even positioned chars in 
		 * the original string (original string has been reversed) 
		 */
		for (int i = 0; i < lsBarCodeTextWithChecksum.length(); i += 2)
		{
			String lsTest =
				lsBarCodeTextWithChecksum.substring(i, i + 1);
			liEvenNoValue
				+= Integer.parseInt(
					lsBarCodeTextWithChecksum.substring(i, i + 1));
		}
		/**
		 * Step 3
		 * this is the sum of the even positioned chars in the CURRENT 
		 * string hence it is the sum of the odd positioned chars in the 
		 * original string (original string has been reversed) 
		 */
		for (int i = 1; i < lsBarCodeTextWithChecksum.length(); i += 2)
		{
			liOddNoValue
				+= Integer.parseInt(
					lsBarCodeTextWithChecksum.substring(i, i + 1));
		}
		liOddNoValue *= 3;
		/**
		 * Step 4 
		 */
		int liCheckSum = liEvenNoValue + liOddNoValue;
		/** 
		 * Step 5 
		 */
		liCheckSum = liCheckSum % liCHECKSUM_NO;
		if (liCheckSum != 0)
		{
			liCheckSum = liCHECKSUM_NO - liCheckSum;
		}
		laBarCodeTextWithChecksum.reverse();
		laBarCodeTextWithChecksum.append(liCheckSum);
		/** 
		 * Step 6
		 */
		lsBarCodeTextWithChecksum =
			laBarCodeTextWithChecksum.toString();
		if ((lsBarCodeTextWithChecksum.length() % 2) != 0)
		{
			laBarCodeTextWithChecksum.insert(0, lchBufferInt);
		}
		return laBarCodeTextWithChecksum.toString();
	}

	/**
	 * Converts the barcode text into a barcode binary string. Takes 
	 * in the barcode text and the cpi (character per inch) value.
	 * 
	 * @param asBarCodeText String
	 * @param adCPI String
	 * @param asType String
	 * @param abShouldStickerPrint boolean
	 * @return String
	 */
	private String barCodeTo2Of5x(
		String asBarCodeText,
		double adCPI,
		String asType,
		boolean abShouldStickerPrint)
	{
		if (adCPI == 0)
		{
			adCPI = 10;
		}
		// initialize the binary string values in the array
		initialize();
		// Get the checksum of the barcode and add it to the barcode 
		// values
		String lsBarCodeWithCheckSum = addChecksum(asBarCodeText);
		// Create the barcode binary equivalent of the barcode numbers
		String lsBarCodeTextWithBitString =
			createBitString(lsBarCodeWithCheckSum);
		// Convert the obtained long binary string from previous step 
		// to PCL to print
		// Defect# 3994
		// Added barcode to Title Package Report - Uses the lsType
		// to decide to treat filename as report or a receipt
		// and handle the barcode PCL conversion properly
		String lsPCLString = "";
		// If this filename to be printed is a report
		if (asType.equalsIgnoreCase("rpt"))
		{
			lsPCLString =
				translateToPCLReports(
					lsBarCodeTextWithBitString,
					asBarCodeText,
					adCPI,
					lsBarCodeWithCheckSum);
		}
		// else treat it like a receipt
		else
		{
			// defect 9668
			//	translateToPCLReceipts() will both Receipts and 
			//	Certificates.
			// PCR 34	
			//			lsPCLString =
			//				translateToPCL(
			//					lsBarCodeTextWithBitString,
			//					asBarCodeText,
			//					adCPI,
			//					lsBarCodeWithCheckSum,
			//					abShouldStickerPrint);
			lsPCLString =
				translateToPCLReceipts(
					lsBarCodeTextWithBitString,
					asBarCodeText,
					adCPI,
					lsBarCodeWithCheckSum,
					abShouldStickerPrint,
					asType);
			// end defect 9668
			// End PCR 34
		}
		return lsPCLString;
	}

	/**
	 * Creates the binary barcode string from the barcode numbers. Picks
	 * up the binary values in the binary value array using the barcode 
	 * numbers (2 at a time) as the index. Assumes that barcode numbers 
	 * with checksum will always have even number of digits. 
	 *
	 * @param asBarCodeText String
	 * @return String
	 */
	private String createBitString(String asBarCodeText)
	{
		//add start code to result
		StringBuffer lsBarCodeTextWithBitString =
			new StringBuffer(START_CODE);
		//replace all decimal number pairs by bits
		for (int i = 0; i < asBarCodeText.length(); i += 2)
		{
			int liIndex =
				Integer.parseInt(asBarCodeText.substring(i, i + 2));
			lsBarCodeTextWithBitString.append(
				carrBarCodeValues[liIndex]);
		}
		//add end_code to result
		lsBarCodeTextWithBitString.append(END_CODE);

		return lsBarCodeTextWithBitString.toString();
	}

	/**
	 * This method was sendToNetworkPrinter(String).  The only
	 * change is to use LPR printing instead of FTP when
	 * connection status == LPR_SERVER.
	 *
	 * @param asFileName String
	 * @throws RTSException
	 */
	private void executePrint(String asFileName) throws RTSException
	{
		try
		{
			// defect 4872
			Log.write(Log.DEBUG, this, " Begin Print" + asFileName);
			String lsLPROutput = "";
			String lsLPRError = "";
			// defect 6771
			// Changed from ProdStatus to PrintStatus
			int liPrintStatus = SystemProperty.getPrintStatus();
			// Use Prod Status to determine if System.out's are needed
			int liProdStatus = SystemProperty.getProdStatus();
			//If printstatus = 2, then don't print anything
			if (liPrintStatus == 2)
			{
				System.out.println(
					"PrintStatus = 2  Printing Disabled......");
			}
			// end defect 6771
			// Changed to meet standards connectionStatus
			else if (ciConnectionStatus == LOCAL_PRINTER)
			{
				// defect 9272
				rtsLocalPrint(asFileName);
				//				Process laProcess =
				//					Runtime.getRuntime().exec(
				//						CMD_EXE
				//							+ COPY_COMMAND
				//							+ asFileName
				//							+ PRN_COMMAND);
				//				laProcess.waitFor();
				//				lsLPROutput = readLPROutput(laProcess);
				//				lsLPRError = readLPRError(laProcess);
				// end defect 9272
				// Only print line if not production status
				if (liProdStatus > 0)
				{
					System.out.println(
						"COPY PRINT COMMAND...SERVERNAME= "
							+ csServerName
							+ " PRINTERNAME= "
							+ csPrinterName);
				}
			}
			// Changed to meet standards connectionStatus
			else if (ciConnectionStatus == LPR_SERVER)
			{
				// defect 6898, 6848
				// Change the ftp command to LPR command
				//ftp.putBinaryFile(fileName, printerName);
				Process laProcess =
					Runtime.getRuntime().exec(
						CMD_EXE
							+ LPR_COMMAND
							+ SWITCH_SERVER
							+ csServerName
							+ SWITCH_PRINTER
							+ csPrinterName
							+ " \""
							+ asFileName
							+ "\"");
				laProcess.waitFor();
				lsLPROutput = readLPROutput(laProcess);
				lsLPRError = readLPRError(laProcess);
				// Only print line if not production status
				if (liProdStatus > 0)
				{
					System.out.println(
						"LPR PRINT COMMAND...SERVERNAME= "
							+ csServerName
							+ " PRINTERNAME= "
							+ csPrinterName);
				}
			}
		}
		// Removed because FTP is not used anymore so IOException would 
		// never be called
		//catch (IOException ioe)
		//{
		//	//ioe.printStackTrace();
		//	RTSException laRTSE = new RTSException(
		//						RTSException.JAVA_ERROR, ioe);
		//	laRTSE.displayError((JDialog) null);
		//}
		// end defect 6898, 6848
		catch (Exception leEx)
		{
			RTSException laRTSE =
				new RTSException(RTSException.JAVA_ERROR, leEx);
			throw laRTSE;
		}
		finally
		{
			// defect 4872
			Log.write(Log.DEBUG, this, " Finished Print" + asFileName);
		}
	}

	/**
	 * Prints the document. Also prints duplicate copies if needed
	 * ( number of copies and number of duplicate copies obtained
	 * from properties file) 
	 * 
	 * @param asFileName String The actual filename to be printed
	 * @param asBarCodeName String Name of the tempfile to be created
	 * @throws RTSException
	 */
	private void genBarCodeReceiptFile(
		String asFileName,
		String asBarCodeName)
		throws RTSException
	{
		String lsInputFileName = asFileName;
		String lsOutputFileName = asBarCodeName;
		String lsHeader = "";
		try
		{
			//get input file path
			File laInputFile = new File(lsInputFileName);

			// defect 10700 
			if (laInputFile.exists())
			{
				// end defect 10700 

				String lsPath = laInputFile.getAbsolutePath();
				//remove the string from / at the end of the path to get 
				//just the path
				int liDirEnd = lsPath.lastIndexOf("\\");
				lsPath = lsPath.substring(0, liDirEnd + 1);
				// +1 as we need the "\" symbol too
				//change path for all the intermediate files
				lsOutputFileName = lsPath + lsOutputFileName;
				//create the output file
				// Defect# 3994
				// Added barcode to Title Package Report - added file 
				// extension to handle the barcode PCL conversion 
				// differently for reports - was passing "" pageprops but 
				// wasn't using it anymore
				String lsFileExtension =
					asFileName.substring(asFileName.length() - 3);
				writeOutput(
					lsInputFileName,
					lsOutputFileName,
					lsFileExtension,
					lsHeader);
				// End 3994
				// defect 6848, 6898
				// Changed to executePrint()
				//sendToNetworkPrinter(lsOutputFileName);
				executePrint(lsOutputFileName);
				// end defect 6848, 6898
				File laTempInputFile = new File(lsOutputFileName);
				laTempInputFile.delete();

				// defect 10700 
			}
			// end defect 10700 
		}
		catch (Exception leEx)
		{
			RTSException laRTSE =
				new RTSException(RTSException.SYSTEM_ERROR, leEx);
			throw laRTSE;
		}
	}

	/**
	 * Create a duplicate file of the original and send
	 * it to the printer.
	 *
	 * 	1. Create temp directory.
	 * 	2. Copy orginal to a temp file in the temp directory.
	 * 	3. Parse the temp file for the PCL header to be placed at the
	 *		top of the duplicate.
	 * 	4. Concatenate the PCLHeader + WatermarkFile + 
	 *		(tempRcpt - (PCL Header))
	 * 	5. Print Duplicate.
	 * 	6. Delete temp duplicate files.
	 *
	 * @param asOriginal String
	 * @param asTransCd String
	 * @throws RTSException
	 */
	private void genTempDuplFile(String asOriginal, String asTransCd)
		throws RTSException
	{
		try
		{
			String lsTempString =
				SystemProperty.getReceiptsDirectory()
					+ "temp\\dupltemp.rcp";
			String lsTempRcpt =
				SystemProperty.getReceiptsDirectory()
					+ "temp\\rcpttemp.rcp";
			ssWATERMARK_FILE =
				SystemProperty.getRTSAppDirectory()
					+ "cfg\\duplicat.prn";

			// defect 7078
			// Generating duplicate was giving mixed results b/c the PCL
			// Header was not being pulled corectly from the top of the 
			// original

			String lsDupHeader = "";
			//String lsDupHeader =
			//	ESC_CHAR + "E" + getPRINT_TRAY_2() + ssPRINT_TOP_MARGIN;
			//defect 5781
			//if (TransCdConstant.VOID.equals(aTransCd))
			//{
			//	lsDupHeader =
			//		getDefaultPageProps() 
			//			+ getPRINT_LINE_SPACING_1_8() 
			//			+ getPRINT_TRAY_2();
			//	lsDupHeader =
			//		getDefaultPageProps() 
			//			+ getPRINT_LINE_SPACING_1_8() 
			//			+ getPRINT_TRAY_2();
			//	//lsDupHeader = getVoidPageProps();
			//	//lsDupHeader = "E&l1H";
			//	lsDupHeader = ESC_CHAR + "E" + getPRINT_TRAY_2();
			//}
			//end defect 5781
			//if (TransCdConstant.ADLCOL.equals(aTransCd)
			//	|| aTransCd.equals("DLRCOMPL")
			//	|| aTransCd.equals("DLRPRELM"))
			//{
			//	//lsDupHeader = "  "; //getDefaultPrinterProps();
			//	lsDupHeader = "  ";
			//}

			// Creating temp dir if it does not exist
			File laTemp1 =
				new File(
					SystemProperty.getReceiptsDirectory() + "temp");
			if (!laTemp1.exists())
			{
				laTemp1.mkdir();
			}

			// Creates a temp original in the temp directory
			File laFile = new File(asOriginal);
			RandomAccessFile laInputFile =
				new RandomAccessFile(asOriginal, "r");
			FileOutputStream laFileOut =
				new FileOutputStream(lsTempRcpt, true);
			OutputStreamWriter laOut =
				new OutputStreamWriter(laFileOut);
			BufferedWriter laBuff = new BufferedWriter(laOut);

			// remove the PCL header from the top of the original
			// copy the contents of output file to the dup input file 
			// minus the PCL header

			//int liHeaderLength = lsDupHeader.length();
			String lsLine = null;
			boolean lbFlag = false;
			while ((lsLine = laInputFile.readLine()) != null)
			{
				if (lbFlag == false && lsLine.length() > 0)
				{
					// Get the location of the PCL header by using the
					// first index of character "("
					int liHeaderLength = lsLine.indexOf("(") - 1;
					// Get the PCL header from the first line to apply 
					// at the top of the tempString file
					lsDupHeader = lsLine.substring(0, liHeaderLength);

					// Search for tray3 and replace with tray 2
					lsDupHeader =
						UtilityMethods.replaceString(
							lsDupHeader,
							getPRINT_TRAY_3(),
							getPRINT_TRAY_2());
					// Search for tray1 and replace with tray 2
					lsDupHeader =
						UtilityMethods.replaceString(
							lsDupHeader,
							getPRINT_TRAY_1(),
							getPRINT_TRAY_2());

					// Remove the PCL header from the first line
					lsLine = lsLine.substring(liHeaderLength);
					lbFlag = true;
				}
				laBuff.write(lsLine);
				laBuff.newLine();
			}
			//close the files in use
			laInputFile.close();
			laBuff.flush();
			laBuff.close();
			laFileOut.close();

			// Copy the dupheader (PCL header) that was taken from the 
			// top of the original and write it to the tempString file
			FileOutputStream laFileOutStream =
				new FileOutputStream(lsTempString, true);
			OutputStreamWriter laOutStreamWtr =
				new OutputStreamWriter(laFileOutStream);
			BufferedWriter laBuffWtr =
				new BufferedWriter(laOutStreamWtr);
			laBuffWtr.write(lsDupHeader);
			laBuffWtr.flush();
			laBuffWtr.close();
			// end defect 7078

			// Merge the three files into one binary file that will be sent
			// to the printer
			Process laProcess =
				Runtime.getRuntime().exec(
					"cmd.exe /c copy "
						+ lsTempString
						+ "+"
						+ ssWATERMARK_FILE
						+ "+"
						+ lsTempRcpt
						+ " /B "
						+ lsTempString);
			laProcess.waitFor();
			String lsLPROutput = readLPROutput(laProcess);
			String lsLPRError = readLPRError(laProcess);

			// defect 6848, 6898
			//sendToNetworkPrinter(tempString);
			// Changed to executePrint()
			executePrint(lsTempString);
			// end defect 6848, 6898

			//delete the intermediate files created
			File laDupFormatInputFile = new File(lsTempString);
			laDupFormatInputFile.delete();
			File laTempInputFile = new File(lsTempRcpt);
			laTempInputFile.delete();
		}
		catch (Exception leEx)
		{
			RTSException laRTSE =
				new RTSException(WRITE_RECEIPT_FAILED);
			throw laRTSE;
			// defect 6848, 6898
			// Throw back to sendToPrinter(,,,) it will choose to 
			// display or throw it back
			//laRTSE.displayError((JDialog) null);
			// end defect 6848, 6898
		}
	}

	/**
	 * Creates and returns the barcode tag using the barcode text value 
	 * and the CPI value. 
	 *
	 * @param lsBarcodeValue The numeric value of barcode
	 * @param ldCPI The character per inch value that sizes the barcode 
	 *		output
	 * @return String
	 */
	public static String getBARCODE_TAG(
		String lsBarcodeValue,
		double ldCPI)
	{
		return getBARCODE_TAG(lsBarcodeValue, ldCPI, false);
	}

	/**
	 * PCR 34
	 * 
	 * @param lsBarcodeValue String
	 * @param ldCPI double
	 * @param boolean
	 * @return String
	*/
	public static String getBARCODE_TAG(
		String lsBarcodeValue,
		double ldCPI,
		boolean lbShouldStickerPrint)
	{
		String lsBarCode = " value=\'" + lsBarcodeValue + "\'";
		String lsCPI = " cpi=\'" + Double.toString(ldCPI) + "\'";
		String strStickerPrint =
			" sticker=\'" + lbShouldStickerPrint + "\' ";
		String lsBarCodeTag =
			ssBARCODE_TAG_BEGIN
				+ lsBarCode
				+ lsCPI
				+ strStickerPrint
				+ ssBARCODE_TAG_END;
		return lsBarCodeTag;
	}

	/**
	 * Get the BARCODE Trans.
	 *
	 * @return String
	 */
	public String getBarcodeTrans()
	{
		return csBarcodeTrans;
	}

	/**
	 * Get the default PCL page properties.
	 *
	 * @return String
	 */
	public static String getDefaultPageProps()
	{
		//String lsPageProperties = 
		//	getPRINT_TOP_MARGIN() 
		//		+ getPRINT_CONDENSED();
		//Fix for CQU100005786
		//String lsPageProperties =
		//	getPRINT_TOP_MARGIN() 
		//		+ getPRINT_TRAY_2() 
		//		+ getPRINT_CONDENSED();
		//	End Fix for CQU100005786

		// defect 7078
		// Set default line spacing to use getPRINT_LINE_SPACING_1_8() 
		// because when a duplicate is printed and no line spacing was 
		// specified then the duplicate would drop off on another page.
		// The duplicate PCL was using a larger line spacing than 
		// expected.
		String lsPageProperties =
			getPRINT_TOP_MARGIN()
				+ getPRINT_TRAY_2()
				+ getPRINT_CONDENSED()
				+ getPRINT_LINE_SPACING_1_8();
		// end defect 7078

		return lsPageProperties;
	}

	/**
	 * Returns the number of duplicate copies to be printed
	 *
	 * @return int
	 */
	public static int getDupCopies()
	{
		return siDupCopies;
	}

	/**
	 * Return the output filename.
	 * 
	 * @return String
	 */
	public String getDupOutFileName()
	{
		return csDUP_OUT_FILE;
	}

	/**
	 * Get the Input File Name.
	 * 
	 * @return String
	 */
	public String getInputFileName()
	{
		return csFILE_NAME;
	}

	/**
	 * Get the Output File Name
	 * 
	 * @return String
	 */
	public String getOutputFileName()
	{
		return csOUT_FILE;
	}

	/**
	 * Create the patch code for selected receipts.
	 *
	 * @return String
	 */
	public static String getPatchCode()
	{
		String patchCode =
			"&f0S&a0V&a3600H*c1530H*c576V*c0P&a144V"
				+ "*c58V*c1P&a259V*c58V*c1P&a374V*c58V*c1P&f1S";
		return patchCode;
	}

	/**
	 * Returns the PCL for 10 PCI 
	 *
	 * @return String
	 */
	public final static String getPRINT_10CPI()
	{
		return ssPRINT_10CPI;
	}

	/**
	 * Returns the PCL for 12 PCI 
	 * 
	 * @return String
	 */
	public final static String getPRINT_12CPI()
	{
		return ssPRINT_12CPI;
	}

	/**
	 * Returns the PCL for advancing paper
	 * 
	 * @return String
	 */
	public final static String getPRINT_ADVANCE_PAPER_9()
	{
		return ssPRINT_ADVANCE_PAPER_9;
	}

	/**
	 * Returns PCL for bold
	 * 
	 * @return String
	 */
	public final static String getPRINT_BOLD()
	{
		return ssPRINT_BOLD;
	}

	/**
	 * Returns PCL for printing COA document's left margin 
	 *  
	 * @return String
	 */
	public final static String getPRINT_COA_LEFT_MARGIN()
	{
		return ssPRINT_COA_LEFT_MARGIN;
	}

	/**
	 * Returns the PCL for COA documents top margin
	 * 
	 * @return String
	 */
	public final static String getPRINT_COA_TOP_MARGIN()
	{
		return ssPRINT_COA_TOP_MARGIN;
	}

	/**
	 * Returns the PCL for condensed printing
	 * 
	 * @return String
	 */
	public final static String getPRINT_CONDENSED()
	{
		return ssPRINT_CONDENSED;
	}

	/**
	 * Returns PCL for ELITE printing
	 * 
	 * @return String
	 */
	public final static String getPRINT_ELITE()
	{
		return ssPRINT_ELITE;
	}

	/**
	 * Returns the PCL for printing HDLASER
	 * 
	 * @return String
	 */
	public static String getPRINT_HDRLASER()
	{
		return ssPRINT_HDRLASER;
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @return String
	 */
	public static String getPRINT_HORHDRSPC12()
	{
		return ssPRINT_HORHDRSPC12;
	}

	/**
	 * Insert the method's description here.
	 *
	 * @return String
	 */
	public static String getPRINT_HORHDRSPC6()
	{
		return ssPRINT_HORHDRSPC6;
	}

	/**
	 * Returns PCL for setting landscape printing
	 * 
	 * @return String
	 */
	public final static String getPRINT_LANDSCAPE()
	{
		return ssPRINT_LANDSCAPE;
	}

	/**
	 * Returns PCL for laser printing
	 *
	 * @return String
	 */
	public static String getPRINT_LASER()
	{
		return ssPRINT_LASER;
	}

	/**
	 * Returns PCL for setting left margin to be 10 CPI
	 * 
	 * @return String
	 */
	public final static String getPRINT_LEFT_MARGIN_10()
	{
		return ssPRINT_LEFT_MARGIN_10;
	}

	/**
	 * Returns PCL for setting line spacing to be 1/6 of an inch. 
	 * 
	 * @return String
	 */
	public final static String getPRINT_LINE_SPACING_1_6()
	{
		return ssPRINT_LINE_SPACING_1_6;
	}

	/**
	 * Returns PCL for setting line spacing to be 1/8th of an inch
	 * 
	 * @return String
	 */
	public final static String getPRINT_LINE_SPACING_1_8()
	{
		return ssPRINT_LINE_SPACING_1_8;
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @return String
	 */
	public final static String getPRINT_LQ10()
	{
		return ssPRINT_LQ10;
	}

	/**
	 * Returns PCL to set normal printing
	 * 
	 * @return jString
	 */
	public final static String getPRINT_NORMAL()
	{
		return ssPRINT_NORMAL;
	}
	// defect 10398
	//	/**
	//	 * Returns PCL to set page length to be 22''
	//	 * 
	//	 * @return String
	//	 */
	//	public final static String getPRINT_PAGE_LENGTH_22()
	//	{
	//		return ssPRINT_PAGE_LENGTH_22;
	//	}
	//
	//	/**
	//	 *  Returns PCL to set page length to be 29''
	//	 * 
	//	 * @return String
	//	 */
	//	public final static String getPRINT_PAGE_LENGTH_29()
	//	{
	//		return ssPRINT_PAGE_LENGTH_29;
	//	}
	//
	//	/**
	//	 *  Returns PCL to set page length to be 48''
	//	 * 
	//	 * @return String
	//	 */
	//	public final static String getPRINT_PAGE_LENGTH_48()
	//	{
	//		return ssPRINT_PAGE_LENGTH_48;
	//	}
	//
	//	/**
	//	 *  Returns PCL to set page length to be 56''
	//	 * 
	//	 * @return java.lang.String
	//	 */
	//	public final static String getPRINT_PAGE_LENGTH_56()
	//	{
	//		return ssPRINT_PAGE_LENGTH_56;
	//	}

	//	/**
	//	 *  Returns PCL to set page length to be 88''
	//	 * 
	//	 * @return String
	//	 */
	//	public final static String getPRINT_PAGE_LENGTH_88()
	//	{
	//		return ssPRINT_PAGE_LENGTH_88;
	//	}

	/**
	 *  Returns custom page size
	 * 
	 * @return java.lang.String
	 */
	public final static String getCUSTOM_SIZE()
	{
		return ssCUSTOM_SIZE;
	}
	// end defect 10398

	/**
	 * Returns PCL to set font to PICA
	 * 
	 * @return String
	 */
	public final static String getPRINT_PICA()
	{
		return ssPRINT_PICA;
	}

	/**
	 * Returns the PCL that resets the printer.
	 * 
	 * @return String
	 */
	public static String getPRINT_RESET()
	{
		return ESC_CHAR + "E";
	}

	/**
	 * Returns the PCL for Salvage documents top margin.
	 *
	 * @return String
	 */
	public static String getPRINT_SLVG_TOP_MARGIN()
	{
		return ssPRINT_SLVG_TOP_MARGIN;
	}

	/**
	 * Returns PCL to set the top margin
	 * 
	 * @return String
	 */
	public final static String getPRINT_TOP_MARGIN()
	{
		return ESC_CHAR + "E" + ssPRINT_TOP_MARGIN;
	}

	/**
	 *  Returns PCL to set the top margin for Permits 
	 * 
	 * @return String
	*/
	public final static String getPERMIT_TOP_MARGIN()
	{
		return ESC_CHAR + "E" + ssPERMIT_TOP_MARGIN;
	}

	/**
	 * Returns PCL to set printer tray 1 as the active tray
	 * 
	 * @return String
	 */
	public final static String getPRINT_TRAY_1()
	{
		return ssPRINT_TRAY_1;
	}

	/**
	 * Returns PCL to set printer tray 2 as the active tray
	 * 
	 * @return String
	 */
	public final static String getPRINT_TRAY_2()
	{
		return ssPRINT_TRAY_2;
	}

	/**
	 * Returns PCL to set printer tray 3 as the active tray
	 * 
	 * @return String
	 */
	public final static String getPRINT_TRAY_3()
	{
		return ssPRINT_TRAY_3;
	}

	/**
	 * getSTICKER_SETTINGS
	 * 
	 * @return String
	 */
	public static String getSTICKER_SETTINGS()
	{
		return ssSTICKER_SETTINGS;
	}

	/**
	 * setSTICKER_SETTINGS
	 * 
	 * @param asStickerSettings String
	 */
	public static void setSTICKER_SETTINGS(String asStickerSettings)
	{
		ssSTICKER_SETTINGS = asStickerSettings;
	}

	/**
	 * Intializes the array that contains the coding for converting 
	 * barcode text values to actual barcode strings (lines). 
	 */
	private void initialize()
	{
		/**
		* These values copied from Steve Hasket's program s447600.cmd
		*/
		carrBarCodeValues[0] = "0000111100";
		carrBarCodeValues[1] = "0100101001";
		carrBarCodeValues[2] = "0001101001";
		carrBarCodeValues[3] = "0101101000";
		carrBarCodeValues[4] = "0000111001";
		carrBarCodeValues[5] = "0100111000";
		carrBarCodeValues[6] = "0001111000";
		carrBarCodeValues[7] = "0000101101";
		carrBarCodeValues[8] = "0100101100";
		carrBarCodeValues[9] = "0001101100";
		carrBarCodeValues[10] = "1000010110";
		carrBarCodeValues[11] = "1100000011";
		carrBarCodeValues[12] = "1001000011";
		carrBarCodeValues[13] = "1101000010";
		carrBarCodeValues[14] = "1000010011";
		carrBarCodeValues[15] = "1100010010";
		carrBarCodeValues[16] = "1001010010";
		carrBarCodeValues[17] = "1000000111";
		carrBarCodeValues[18] = "1100000110";
		carrBarCodeValues[19] = "1001000110";
		carrBarCodeValues[20] = "0010010110";
		carrBarCodeValues[21] = "0110000011";
		carrBarCodeValues[22] = "0011000011";
		carrBarCodeValues[23] = "0111000010";
		carrBarCodeValues[24] = "0010010011";
		carrBarCodeValues[25] = "0110010010";
		carrBarCodeValues[26] = "0011010010";
		carrBarCodeValues[27] = "0010000111";
		carrBarCodeValues[28] = "0110000110";
		carrBarCodeValues[29] = "0011000110";
		carrBarCodeValues[30] = "1010010100";
		carrBarCodeValues[31] = "1110000001";
		carrBarCodeValues[32] = "1011000001";
		carrBarCodeValues[33] = "1111000000";
		carrBarCodeValues[34] = "1010010001";
		carrBarCodeValues[35] = "1110010000";
		carrBarCodeValues[36] = "1011010000";
		carrBarCodeValues[37] = "1010000101";
		carrBarCodeValues[38] = "1110000100";
		carrBarCodeValues[39] = "1011000100";
		carrBarCodeValues[40] = "0000110110";
		carrBarCodeValues[41] = "0100100011";
		carrBarCodeValues[42] = "0001100011";
		carrBarCodeValues[43] = "0101100010";
		carrBarCodeValues[44] = "0000110011";
		carrBarCodeValues[45] = "0100110010";
		carrBarCodeValues[46] = "0001110010";
		carrBarCodeValues[47] = "0000100111";
		carrBarCodeValues[48] = "0100100110";
		carrBarCodeValues[49] = "0001100110";
		carrBarCodeValues[50] = "1000110100";
		carrBarCodeValues[51] = "1100100001";
		carrBarCodeValues[52] = "1001100001";
		carrBarCodeValues[53] = "1101100000";
		carrBarCodeValues[54] = "1000110001";
		carrBarCodeValues[55] = "1100110000";
		carrBarCodeValues[56] = "1001110000";
		carrBarCodeValues[57] = "1000100101";
		carrBarCodeValues[58] = "1100100100";
		carrBarCodeValues[59] = "1001100100";
		carrBarCodeValues[60] = "0010110100";
		carrBarCodeValues[61] = "0110100001";
		carrBarCodeValues[62] = "0011100001";
		carrBarCodeValues[63] = "0111100000";
		carrBarCodeValues[64] = "0010110001";
		carrBarCodeValues[65] = "0110110000";
		carrBarCodeValues[66] = "0011110000";
		carrBarCodeValues[67] = "0010100101";
		carrBarCodeValues[68] = "0110100100";
		carrBarCodeValues[69] = "0011100100";
		carrBarCodeValues[70] = "0000011110";
		carrBarCodeValues[71] = "0100001011";
		carrBarCodeValues[72] = "0001001011";
		carrBarCodeValues[73] = "0101001010";
		carrBarCodeValues[74] = "0000011011";
		carrBarCodeValues[75] = "0100011010";
		carrBarCodeValues[76] = "0001011010";
		carrBarCodeValues[77] = "0000001111";
		carrBarCodeValues[78] = "0100001110";
		carrBarCodeValues[79] = "0001001110";
		carrBarCodeValues[80] = "1000011100";
		carrBarCodeValues[81] = "1100001001";
		carrBarCodeValues[82] = "1001001001";
		carrBarCodeValues[83] = "1101001000";
		carrBarCodeValues[84] = "1000011001";
		carrBarCodeValues[85] = "1100011000";
		carrBarCodeValues[86] = "1001011000";
		carrBarCodeValues[87] = "1000001101";
		carrBarCodeValues[88] = "1100001100";
		carrBarCodeValues[89] = "1001001100";
		carrBarCodeValues[90] = "0010011100";
		carrBarCodeValues[91] = "0110001001";
		carrBarCodeValues[92] = "0011001001";
		carrBarCodeValues[93] = "0111001000";
		carrBarCodeValues[94] = "0010011001";
		carrBarCodeValues[95] = "0110011000";
		carrBarCodeValues[96] = "0011011000";
		carrBarCodeValues[97] = "0010001101";
		carrBarCodeValues[98] = "0110001100";
		carrBarCodeValues[99] = "0011001100";
	}

	/**
	 * Initializes the print environment by loading the default 
	 * properties from the print properties file. 
	 */
	public static void initializeEnv()
	{
		FileInputStream lpfsFileInput = null;
		try
		{
			//create the default filenames
			lpfsFileInput = new FileInputStream(PROPERTIES_NAME);
			Properties lsPrp = new Properties();
			lsPrp.load(lpfsFileInput);
			ssTRAY_PCL = ESC_CHAR + "&l4H";
			if (lsPrp.getProperty("TRAY").equals("1"))
			{
				ssTRAY_PCL = ESC_CHAR + "&l4H";
			}
			else if (lsPrp.getProperty("TRAY").equals("2"))
			{
				ssTRAY_PCL = ESC_CHAR + "&l1H";
			}
			else if (lsPrp.getProperty("TRAY").equals("3"))
			{
				ssTRAY_PCL = ESC_CHAR + "&l5H";
			}
			ssCOPIES_PCL =
				ESC_CHAR + "&l" + lsPrp.getProperty("COPIES") + "X";
			siCopies = Integer.parseInt(lsPrp.getProperty("COPIES"));
			ssDUP_COPIES_PCL =
				ESC_CHAR + "&l" + lsPrp.getProperty("DUP_COPIES") + "X";
			siDupCopies =
				Integer.parseInt(lsPrp.getProperty("DUP_COPIES"));
			ssORIENTATION_PCL =
				ESC_CHAR
					+ "&l"
					+ lsPrp.getProperty("ORIENTATION")
					+ "O";
			ssLINE_SPACING_PCL =
				ESC_CHAR
					+ "&l"
					+ lsPrp.getProperty("LINE_SPACING")
					+ "D";
			ssTOP_MARGIN_PCL =
				ESC_CHAR + "&l" + lsPrp.getProperty("TOP_MARGIN") + "E";
			ssLEFT_MARGIN_PCL =
				ESC_CHAR
					+ "&a"
					+ lsPrp.getProperty("LEFT_MARGIN")
					+ "L";
			//printer properties
			//private static final String csD600_PRINTER_EMULATION = 
			//								csD600_PCL5;
			ssPRINT_ELITE =
				ESC_CHAR + insertEsc(lsPrp.getProperty("PRINT_ELITE"));
			ssPRINT_LQ10 =
				ESC_CHAR + insertEsc(lsPrp.getProperty("PRINT_LQ10"));
			ssPRINT_10CPI = ESC_CHAR + lsPrp.getProperty("PRINT_10CPI");
			ssPRINT_12CPI = ESC_CHAR + lsPrp.getProperty("PRINT_12CPI");
			ssPRINT_BOLD = ESC_CHAR + lsPrp.getProperty("PRINT_BOLD");
			ssPRINT_NORMAL =
				ESC_CHAR + lsPrp.getProperty("PRINT_NORMAL");
			ssPRINT_CONDENSED =
				ESC_CHAR
					+ insertEsc(lsPrp.getProperty("PRINT_CONDENSED"));
			ssPRINT_PICA =
				ESC_CHAR + insertEsc(lsPrp.getProperty("PRINT_PICA"));
			ssPRINT_LINE_SPACING_1_6 =
				ESC_CHAR + lsPrp.getProperty("PRINT_LINE_SPACING_1_6");
			ssPRINT_LASER =
				ESC_CHAR + insertEsc(lsPrp.getProperty("PRINT_LASER"));
			ssPRINT_HDRLASER =
				ESC_CHAR
					+ insertEsc(lsPrp.getProperty("PRINT_HDRLASER"));
			ssPRINT_LINE_SPACING_1_8 =
				ESC_CHAR + lsPrp.getProperty("PRINT_LINE_SPACING_1_8");
			// defect 10398
			//			ssPRINT_PAGE_LENGTH_22 =
			//				ESC_CHAR + lsPrp.getProperty("PRINT_PAGE_LENGTH_22");
			//			ssPRINT_PAGE_LENGTH_29 =
			//				ESC_CHAR + lsPrp.getProperty("PRINT_PAGE_LENGTH_29");
			//			ssPRINT_PAGE_LENGTH_48 =
			//				ESC_CHAR + lsPrp.getProperty("PRINT_PAGE_LENGTH_48");
			//			ssPRINT_PAGE_LENGTH_56 =
			//				ESC_CHAR + lsPrp.getProperty("PRINT_PAGE_LENGTH_56");
			//			ssPRINT_PAGE_LENGTH_88 =
			//				ESC_CHAR + lsPrp.getProperty("PRINT_PAGE_LENGTH_88");
			ssCUSTOM_SIZE = ESC_CHAR + "&l101A";
			// end defect 10398	
			ssPRINT_ADVANCE_PAPER_9 = "";
			ssPRINT_TOP_MARGIN =
				ssPRINT_LINE_SPACING_1_8
					+ ESC_CHAR
					+ lsPrp.getProperty("PRINT_TOP_MARGIN");
			ssPRINT_LEFT_MARGIN_10 =
				ESC_CHAR + lsPrp.getProperty("PRINT_LEFT_MARGIN");
			ssPRINT_TRAY_1 =
				ESC_CHAR + lsPrp.getProperty("PRINT_TRAY_1");
			ssPRINT_TRAY_2 =
				ESC_CHAR + lsPrp.getProperty("PRINT_TRAY_2");
			ssPRINT_TRAY_3 =
				ESC_CHAR + lsPrp.getProperty("PRINT_TRAY_3");
			ssPRINT_COA_LEFT_MARGIN =
				ESC_CHAR + lsPrp.getProperty("PRINT_COA_LEFT_MARGIN");
			ssPRINT_COA_TOP_MARGIN =
				ESC_CHAR + lsPrp.getProperty("PRINT_COA_TOP_MARGIN");
			ssPRINT_LANDSCAPE =
				ESC_CHAR + lsPrp.getProperty("PRINT_LANDSCAPE");
			//get barcode tag beginning and ending
			ssBARCODE_TAG_BEGIN =
				lsPrp.getProperty("BARCODE_TAG_BEGIN");
			ssBARCODE_TAG_END = lsPrp.getProperty("BARCODE_TAG_END");
			ssPRINT_HORHDRSPC12 =
				ESC_CHAR + lsPrp.getProperty("PRINT_HORHDRSPC12");
			ssPRINT_HORHDRSPC6 =
				ESC_CHAR + lsPrp.getProperty("PRINT_HORHDRSPC6");

			// defect 7079
			// Moved to PCLUtilities - Built on the fly
			// Sticker printing
			//WS_VIN = insertAllEsc(prp.getProperty("WS_VIN"));
			//WS_BARCODE = insertAllEsc(prp.getProperty("WS_BARCODE"));
			//WS_EXP = insertAllEsc(prp.getProperty("WS_EXP"));
			//WS_COUNTY = insertAllEsc(prp.getProperty("WS_COUNTY"));
			//WS_PLATE = insertAllEsc(prp.getProperty("WS_PLATE"));
			//PL_VIN = insertAllEsc(prp.getProperty("PL_VIN"));
			//PL_BARCODE = insertAllEsc(prp.getProperty("PL_BARCODE"));
			//PL_EXP = insertAllEsc(prp.getProperty("PL_EXP"));
			//PL_COUNTY = insertAllEsc(prp.getProperty("PL_COUNTY"));
			//PL_PLATE = insertAllEsc(prp.getProperty("PL_PLATE"));
			// end defect 7079

			// defect 7530
			// use a default when the property is missing
			// get the Sticker Printing Settings
			String lsStickerSettingProperty =
				lsPrp.getProperty("STICKER_SETTINGS");
			if (lsStickerSettingProperty != null)
			{
				ssSTICKER_SETTINGS =
					insertAllEsc(lsStickerSettingProperty);
			}
			else
			{
				ssSTICKER_SETTINGS =
					insertAllEsc(DEFAULTSTICKERSETTINGS);
			}

			// get the Print Tray Sticker Setting
			String lsPrintTraySticker =
				lsPrp.getProperty("PRINT_TRAY_STICKER");
			if (lsPrintTraySticker != null)
			{
				ssPRINT_TRAY_STICKER = insertAllEsc(lsPrintTraySticker);
			}
			else
			{
				ssPRINT_TRAY_STICKER = insertAllEsc(DEFAULTSTICKERTRAY);
			}
			// end defect 7530
		}
		catch (MissingResourceException leMREx)
		{
			//MRE.printStackTrace();
			RTSException laRTSE =
				new RTSException(RTSException.SYSTEM_ERROR, leMREx);
			//throw laRTSE;
			// defect 7530
			if (!Comm.isServer())
			{
				// on client display the error
				// defect 6456 
				//laRTSE.displayError((JDialog) null);
				laRTSE.displayError();
				// end defect 6456 
			}
			else
			{
				// on server, write exception to log
				laRTSE.writeExceptionToLog();
			}
			// end defect 7530
		}
		catch (Exception leEx)
		{
			RTSException laRTSE =
				new RTSException(RTSException.SYSTEM_ERROR, leEx);
			//throw laRTSE;
			// defect 7530
			if (!Comm.isServer())
			{
				// on client display the error
				// defect 6456 
				//laRTSE.displayError((JDialog) null);
				laRTSE.displayError();
				// end defect 6456 
			}
			else
			{
				// on server, write exception to log
				laRTSE.writeExceptionToLog();
			}
			// end defect 7530 
			//e.printStackTrace(); 
		}
		finally
		{
			try
			{
				lpfsFileInput.close();
			}
			catch (Exception leEx)
			{
				//ex.printStackTrace();
				RTSException laRTSE =
					new RTSException(RTSException.SYSTEM_ERROR, leEx);
				// defect 7530
				if (!Comm.isServer())
				{
					// on client display the error
					// defect 6456 
					//laRTSE.displayError((JDialog) null);
					laRTSE.displayError();
					// end defect 6456
				}
				else
				{
					// on server, write exception to log
					laRTSE.writeExceptionToLog();
				}
				// end defect 7530 
				//throw laRTSE; 
			}
		}
	}

	/**
	 * Replaces all the esc character representation strings in the txt 
	 * file with the actual esc char.
	 *
	 * @param asStrInput String
	 * @return String
	 */
	private static String insertAllEsc(String asStrInput)
	{
		while (asStrInput.indexOf(ESC_IN_PROPS) > -1)
		{
			int liIndex = asStrInput.indexOf(ESC_IN_PROPS);
			asStrInput =
				asStrInput.substring(0, liIndex)
					+ ESC_CHAR
					+ asStrInput.substring(
						liIndex + ESC_IN_PROPS.length());
		}
		return asStrInput;
	}

	/**
	 * Replaces the esc character representation string in the txt file 
	 * with the actual esc char. 
	 * 
	 * @param asInput String
	 * @return String
	 */
	private static String insertEsc(String asInput)
	{
		StringBuffer laOutput = new StringBuffer(asInput);
		int liBeginPos = asInput.indexOf(ESC_IN_PROPS);
		int liEndPos = liBeginPos + ESC_IN_PROPS.length();
		laOutput.replace(liBeginPos, liEndPos, ESC_CHAR);
		return laOutput.toString();
	}

	/**
	 * This returns if batch is calling Print.
	 * 
	 * @return boolean
	 * @param aRedirectSubsta int
	 * @param aRedirectWsid int
	 * @throws RTSException
	 */
	private boolean isBatch(int aiRedirectSubsta, int aiRedirectWsid)
		throws RTSException
	{
		// All batch processes pass a value greater than 
		// Integer.MIN_VALUE for the redirect Substation and Workstation
		if (aiRedirectSubsta > Integer.MIN_VALUE
			&& aiRedirectWsid > Integer.MIN_VALUE)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Returns if exceptions will be returned when issuing a print
	 * command.
	 * 
	 * @return boolean
	 */
	public boolean isReturnException()
	{
		return this.cbReturnException;
	}

	/**
	 * This method parses the filename sent.
	 *
	 * @param asFileName String
	 * @param aiSizeReturned int
	 * @return String
	 */
	public String parseFileName(String asFileName, int aiSizeReturned)
	{
		String lsBeginFileName = "";
		if (asFileName != null && asFileName != "")
		{
			File laInputFile = new File(asFileName);
			String lsPath = laInputFile.getAbsolutePath();
			// Remove the string from / at the end of the
			// path to get just the path
			int liDirEnd = lsPath.lastIndexOf("\\");
			// +1 as we need the "\" symbol too
			liDirEnd = liDirEnd + 1;
			if (asFileName.length() - liDirEnd >= aiSizeReturned)
			{
				// Get the actual filename without the path
				lsBeginFileName = asFileName.substring(liDirEnd);
				// Get the first x amount of characters of the filename
				lsBeginFileName =
					lsBeginFileName.substring(0, aiSizeReturned);
			}
			else
			{
				return lsBeginFileName;
			}
			return lsBeginFileName;
		}
		else
		{
			return lsBeginFileName;
		}
	}

	/**
	 * Ping returns true or false depending if the control point
	 * that is passed is online or not. <p>Research turned up that
	 * with JRE 1.4.2 there is a better way to ping a machine.</p>
	 *
	 * @param asControlPoint String
	 * @return boolean
	 */
	private boolean ping(String asControlPoint)
	{
		try
		{
			if (asControlPoint == null || asControlPoint.equals(""))
			{
				return PING_STATUS_NOT_AVAILABLE;
			}
			String lsLPROutput = "";
			String lsLPRError = "";
			Process laProcess =
				Runtime.getRuntime().exec(
					PING
						+ asControlPoint
						+ SWITCH_NUM_TRIES
						+ PING_NUM_TRIES
						+ SWITCH_TIME_OUT
						+ PING_TIMEOUT);
			laProcess.waitFor();
			lsLPROutput = readLPROutput(laProcess);
			lsLPRError = readLPRError(laProcess);
			// Added both error and output together just in case the 
			// result is not in one or the other.  Had to add " " to 
			// the beginning so that the indexOf would not ever return 
			// 0 when the result String actually contains the search 
			// string.
			lsLPROutput = " " + lsLPROutput + " " + lsLPRError;
			// Added check for the diferent types of results to help 
			// with debuging
			if (lsLPROutput.indexOf(PING_RESULT_AVAILABLE) > 0)
			{
				return PING_STATUS_AVAILABLE;
			}
			else if (lsLPROutput.indexOf(PING_RESULT_TIME_OUT) > 0)
			{
				return PING_STATUS_NOT_AVAILABLE;
			}
			else if (
				lsLPROutput.indexOf(PING_RESULT_NOT_AVAILABLE) > 0)
			{
				return PING_STATUS_NOT_AVAILABLE;
			}
			else
			{
				return PING_STATUS_NOT_AVAILABLE;
			}
		}
		catch (Exception leEx)
		{
			// defect 7562
			// failed misspelled
			Log.write(
				Log.SQL_EXCP,
				this,
				" Ping failed to connect to "
					+ asControlPoint
					+ " b/c of an Exception - "
					+ leEx.toString());
			// end defect 7562
			return PING_STATUS_NOT_AVAILABLE;
		}
	}

	/**
	 * This method will look up the transCd and determine if dups
	 * or a barcode is needed to be generated.
	 * This is what sendToPrinter() did before.
	 * 
	 * @param aaPrintDoc PrintDocumentData
	 * @param asFileName String
	 * @param asTransCd String
	 * @throws RTSException
	 */
	private void printCerts(
		PrintDocumentData aaPrintDoc,
		String asFileName,
		String asTransCd)
		throws RTSException
	{
		// defect 6771
		// Changed from ProdStatus to PrintStatus
		int liPrintStatus = SystemProperty.getPrintStatus();
		// end defect 6771
		// defect 6234
		// VTR wanted barcode document sent to the pritner first 
		// for .crt's
		if (aaPrintDoc.isBarCode())
		{
			// defect 6771
			// Changed from ProdStatus to PrintStatus
			// If there is only a barcode file let it through
			// if there is an original and a barcode dont print barcode
			if (liPrintStatus == 1
				&& aaPrintDoc.isOriginal()) // end defect 6771
			{
				System.out.println(
					"PrintStatus = 1   Duplicate Printing "
						+ "Disabled .....");
			}
			else
			{
				String lsTemp =
					asFileName.substring(0, asFileName.length() - 4);
				lsTemp = lsTemp + "B.crt";
				genBarCodeReceiptFile(lsTemp, TEMP_BARCODE_FILE);
			}
		}
		if (aaPrintDoc.isOriginal())
		{
			executePrint(asFileName);
		}
		if (aaPrintDoc.isFirstDuplicate())
		{
			// defect 6771
			// Changed from ProdStatus to PrintStatus
			if (liPrintStatus == 1) // defect 6771
			{
				System.out.println(
					"PrintStatus = 1   Duplicate Printing "
						+ "Disabled .....");
			}
			else
			{
				genTempDuplFile(asFileName, asTransCd);
			}
		}
		if (aaPrintDoc.isSecondDuplicate())
		{
			// defect 6771
			// Changed from ProdStatus to PrintStatus
			if (liPrintStatus == 1) // defect 6771
			{
				System.out.println(
					"PrintStatus = 1   Duplicate Printing "
						+ "Disabled .....");
			}
			else
			{
				genTempDuplFile(asFileName, asTransCd);
			}
		}
	}

	/**
	 * This method will look up the transCd and determine if dups
	 * or a barcode is needed to be generated.
	 * This is what sendToPrinter() did before.
	 * 
	 * @param aaPrintDoc PrintDocumentData
	 * @param asFileName String
	 * @param asTransCd String
	 * @throws RTSException
	 */
	private void printReceipts(
		PrintDocumentData aaPrintDoc,
		String asFileName,
		String asTransCd)
		throws RTSException
	{
		// defect 6771
		// Changed from ProdStatus to PrintStatus
		int liPrintStatus = SystemProperty.getPrintStatus();
		// end defect 6771
		if (aaPrintDoc.isOriginal())
		{
			executePrint(asFileName);
		}
		if (aaPrintDoc.isFirstDuplicate())
		{
			// defect 6771
			// Changed from ProdStatus to PrintStatus
			if (liPrintStatus == 1) // end defect 6771
			{
				System.out.println(
					"PrintStatus = 1   Duplicate Printing "
						+ "Disabled .....");
			}
			else
			{
				genTempDuplFile(asFileName, asTransCd);
			}
		}
		if (aaPrintDoc.isSecondDuplicate())
		{
			// defect 6771
			// Changed from ProdStatus to PrintStatus
			if (liPrintStatus == 1) // end defect 6771
			{
				System.out.println(
					"PrintStatus = 1   Duplicate Printing "
						+ "Disabled .....");
			}
			else
			{
				genTempDuplFile(asFileName, asTransCd);
			}
		}
		if (aaPrintDoc.isBarCode())
		{
			// defect 10491
			// Print Permit even if PrintStatusCd = 1 (No Duplicate) 

			// If there is only a barcode file let it through
			// if there is an original and a barcode don't print barcode
			if (liPrintStatus == 1
				&& !(UtilityMethods.printsPermit(asTransCd))
				&& aaPrintDoc.isOriginal())
			{
				// end defect 10491 
				System.out.println(
					"PrintStatus = 1   Duplicate Printing "
						+ "Disabled .....");
			}
			else
			{
				String lsTemp =
					asFileName.substring(0, asFileName.length() - 4);
				// defect 10700 
				// Use CommonConstant.BARCODE_SUFFIX
				lsTemp =
					lsTemp + CommonConstant.BARCODE_SUFFIX + ".rcp";
				// end defect 10700 
				genBarCodeReceiptFile(lsTemp, TEMP_BARCODE_FILE);
			}
		}
		// defect 10700 
		if (aaPrintDoc.isPermit())
		{
			String lsTemp =
				asFileName.substring(0, asFileName.length() - 4);
			lsTemp = lsTemp + CommonConstant.PERMIT_SUFFIX + ".rcp";
			genBarCodeReceiptFile(lsTemp, TEMP_PERMIT_FILE);
		}
		// end defect 10700 
	}

	/**
	 * This method will look up the transCd and determine if dups
	 * or a barcode is needed to be generated.
	 * The only report at this time that has a barcode is Tittle Package
	 * This is what sendToPrinter() did before.
	 * 
	 * @param aaPrintDoc PrintDocumentData
	 * @param asFileName String
	 * @param asTransCd String
	 * @throws RTSException
	 */
	private void printReports(
		PrintDocumentData aaPrintDoc,
		String asFileName,
		String asTransCd)
		throws RTSException
	{
		// defect 6771
		// Changed from ProdStatus to PrintStatus
		int liPrintStatus = SystemProperty.getPrintStatus();
		// end defect 6771
		if (aaPrintDoc.isOriginal())
		{
			executePrint(asFileName);
		}
		if (aaPrintDoc.isFirstDuplicate())
		{
			// defect 6771
			// Changed from ProdStatus to PrintStatus
			if (liPrintStatus == 1) // end defect 6771
			{
				System.out.println(
					"PrintStatus = 1   Duplicate Printing "
						+ "Disabled .....");
			}
			else
			{
				genTempDuplFile(asFileName, asTransCd);
			}
		}
		if (aaPrintDoc.isSecondDuplicate())
		{
			// defect 6771
			// Changed from ProdStatus to PrintStatus
			if (liPrintStatus == 1) // end defect 6771
			{
				System.out.println(
					"PrintStatus = 1   Duplicate Printing "
						+ "Disabled .....");
			}
			else
			{
				genTempDuplFile(asFileName, asTransCd);
			}
		}
		if (aaPrintDoc.isBarCode())
		{
			// defect 6771
			// Changed from ProdStatus to PrintStatus
			// If there is only a barcode file let it through
			// if there is an original and a barcode dont print barcode
			if (liPrintStatus == 1
				&& aaPrintDoc.isOriginal()) // end defect 6771
			{
				System.out.println(
					"PrintStatus = 1   Duplicate Printing "
						+ "Disabled .....");
			}
			else
			{
				// Just send the filename that was passed.  Reports
				// usually don't have an original
				genBarCodeReceiptFile(asFileName, TEMP_BARCODE_FILE);
			}
		}
	}

	/**
	 * Returns the error message sent back to local LPR process by the
	 * <code>lpd</code> daemon on the print server. 
	 * 
	 * @param laLPRProcess Process
	 * @return String
	 * @throws RTSException
	 */
	private String readLPRError(Process laLPRProcess)
		throws RTSException
	{
		try
		{
			//create the result holder
			StringBuffer lsLPRError = new StringBuffer();
			/** 
			 * create reader to read error message from lpd on remote 
			 * server read the process' error stream to receive the 
			 * error message
			 */
			BufferedReader laBufferedReader =
				new BufferedReader(
					new InputStreamReader(
						laLPRProcess.getErrorStream()));
			//read all lines in the error message 
			String lsLine = "";
			while (true)
			{
				lsLine = laBufferedReader.readLine();
				if (lsLine == null)
				{
					break;
				}
				if (lsLine.length() == 0)
				{
					continue;
				}
				lsLPRError.append(lsLine);
			}
			//print the error message to stdout
			// System.out.println("Err " + laLPRError.toString());
			//return the error message
			return lsLPRError.toString();
		}
		catch (IOException leIOEx)
		{
			RTSException laRTSE =
				new RTSException(RTSException.SYSTEM_ERROR, leIOEx);
			throw laRTSE;
		}
	}

	/**
	 * Returns the output message sent back to local LPR process by the
	 * <code>lpd</code> daemon on the print server. 
	 * 
	 * @param laLPRProcess Process
	 * @return String
	 * @throws RTSException
	 */
	private String readLPROutput(Process laLPRProcess)
		throws RTSException
	{
		try
		{
			//create the result holder
			StringBuffer lsLPROutput = new StringBuffer();
			/** 
			 * create reader to read output message from lpd on remote 
			 * server. read the local process' input stream to receive 
			 * the output message from the server
			 */
			BufferedReader laBufferedReader =
				new BufferedReader(
					new InputStreamReader(
						laLPRProcess.getInputStream()));
			//read all lines in the error message 
			String lsLine = "";
			while (true)
			{
				lsLine = laBufferedReader.readLine();
				if (lsLine == null)
				{
					break;
				}
				if (lsLine.length() == 0)
				{
					continue;
				}
				lsLPROutput.append(lsLine);
			}
			//print the error message to stdout
			// System.out.println("Err " + laLPROutput.toString());
			//return the error message
			return lsLPROutput.toString();
		}
		catch (IOException leIOEx)
		{
			RTSException laRTSE =
				new RTSException(RTSException.SYSTEM_ERROR, leIOEx);
			throw laRTSE;
		}
	}

	/**
	 * Handles Local Printing.
	 * 
	 * @param asFileName
	 * @throws RTSException
	 */
	private static void rtsLocalPrint(String asFileName)
		throws RTSException
	{
		FileInputStream laInputFile;

		// establish connection to file
		try
		{
			laInputFile = new FileInputStream(asFileName);
		}
		catch (FileNotFoundException aeFNFEx)
		{
			RTSException laRTSEx =
				new RTSException(
					RTSException.JAVA_ERROR,
					"FILE NOT FOUND",
					"FILE PRINTING ERROR");
			throw laRTSEx;
		}

		DocFlavor laFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

		// setup the document
		Doc laDoc = new SimpleDoc(laInputFile, laFlavor, null);

		PrintRequestAttributeSet laPRAS =
			new HashPrintRequestAttributeSet();

		// find the default printer
		PrintService laPrintServices =
			PrintServiceLookup.lookupDefaultPrintService();

		// Set up the job and print it
		if (laPrintServices != null)
		{
			// defect 9797
			Log.write(
				Log.DEBUG,
				laPrintServices,
				PRINTER_EQ_TXT + laPrintServices.getName());
			// end defect 9797
			DocPrintJob laPrintJob = laPrintServices.createPrintJob();
			try
			{
				laPrintJob.print(laDoc, laPRAS);
			}
			catch (PrintException aePEx)
			{
				RTSException laRTSEx =
					new RTSException(
						RTSException.JAVA_ERROR,
						"FILE PRINT ERROR: " + aePEx.toString(),
						"FILE PRINTING ERROR");
				throw laRTSEx;
			}
		}
		else
		{
			Log.write(
				Log.SQL_EXCP,
				"Log To File",
				"No Printer Found!!");
		}
	}

	/**
	 * All calls to the printer must go through one of the 
	 * sendToPrinter() methods. This sendToPrinter() is for when you 
	 * only need to pass the filename.  This method calls 
	 * sendToPrinter() passing the filename and a transCd of null.
	 *
	 * The filename must contain the full path to file that is to 
	 * be printed.
	 *
	 * @param asFileName String
	 * @exception RTSException
	 */
	public void sendToPrinter(String asFileName) throws RTSException
	{
		sendToPrinter(asFileName, null);
	}

	/**
	 * All calls to the printer must go through one of the 
	 * sendToPrinter() methods. This sendToPrinter() is for when you 
	 * only need to pass the filename and a transCd.  This method calls 
	 * sendToPrinter() passing filename, transCd and Integer.MIN_VALUE 
	 * for both redirect substation and redirect workstation.
	 *
	 * The filename must contain the full path to file that is to be 
	 * printed.
	 *
	 * sendToPrinter(String,String,int,int) is looking for either
	 * Integer.MIN_VALUE or a Substation number and Workstation number
	 * to redirect the print to.  These values are mainly used by 
	 * Batch to redirect CountyWide.
	 *
	 * @param asFileName String
	 * @param asTransCd String
	 * @throws RTSException
	 */
	public void sendToPrinter(String asFileName, String asTransCd)
		throws RTSException
	{
		sendToPrinter(
			asFileName,
			asTransCd,
			Integer.MIN_VALUE,
			Integer.MIN_VALUE);
	}

	/**
	 * All calls to the printer must go through one of the 
	 * sendToPrinter() methods. This sendToPrinter() is used when a 
	 * printout needs to be redirected to another workstation.
	 *
	 * The filename must contain the full path to file that is to 
	 * be printed. he transCd determines whether duplicates or a barcode
	 * gets printed.
	 *
	 * This method is looking for either Integer.MIN_VALUE or a 
	 * Substation number and Workstation number to redirect the print 
	 * to.  These values are mainly used by Batch to redirect 
	 * CountyWide.
	 *
	 * @param asFileName String
	 * @param asTransCd String
	 * @param aiSubsta int
	 * @param aiWsid int
	 * @throws RTSException
	 */
	public void sendToPrinter(
		String asFileName,
		String asTransCd,
		int aiRedirectSubsta,
		int aiRedirectWsid)
		throws RTSException
	{
		try
		{
			// This method sets the connection status.
			setConnectionStatus(aiRedirectSubsta, aiRedirectWsid);
			// Added this, no need to go excecute print if 
			// connectionStatus == NO_PRINTER.  This was done in 
			// closeFtpConnectionForBatch(int,int) and 
			// closeFtpConnection()
			if (ciConnectionStatus == NO_PRINTER)
			{
				// What was done in closeFTPConnection();
				// Display the error  when not batch
				RTSException laRTSEx =
					new RTSException(
						RTSException.JAVA_ERROR,
						"UNABLE TO SEND THIS FILE TO THE PRINTER",
						"FILE PRINTING ERROR");
				throw laRTSEx;
			}
			// Added to make sure there are no class caste exceptions
			// if TransCd is null then executePrint() and return 
			// naturally
			if (asTransCd == null || asTransCd == "")
			{
				executePrint(asFileName);
			}
			else
			{
				PrintDocumentData laPrintDoc =
					(PrintDocumentData) PrintDocManager.getPrintProps(
						asTransCd);
				if (laPrintDoc == null)
				{
					// log write
					executePrint(asFileName);
				}
				else
				{
					// added check for file type, this way different 
					// print calls can be done for the different types 
					// of files
					String lsFileExtension =
						asFileName.substring(asFileName.length() - 4);
					if (lsFileExtension
						.equalsIgnoreCase(EXTENSION_RECEIPTS))
					{
						// call to print Receipts
						printReceipts(
							laPrintDoc,
							asFileName,
							asTransCd);
					}
					else if (
						lsFileExtension.equalsIgnoreCase(
							EXTENSION_CERTS))
					{
						// call to print certificates
						printCerts(laPrintDoc, asFileName, asTransCd);
					}
					// Else EXTENSION_REPORTS
					else
					{
						// call to print reports
						printReports(laPrintDoc, asFileName, asTransCd);
					}
				}
			}
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" RTSException when trying "
					+ "to print - "
					+ leRTSEx.toString());
			if (csErrorMsg.equals(""))
			{
				leRTSEx.setDetailMsg(leRTSEx.toString());
			}
			else
			{
				leRTSEx.setDetailMsg(csErrorMsg);
			}
			// defect 7067
			// Make print return exception so that if there was an 
			// exception during printing the sticker the prntinvqty 
			// field would not get updated.
			//if (isBatch(aRedirectSubsta, aRedirectWsid))
			if (isBatch(aiRedirectSubsta, aiRedirectWsid)
				|| isReturnException())
				// end defect 7067
			{
				// Throw errors that are from batch back so that batch
				// can write to the log that the filename didn't get 
				// printed.
				throw leRTSEx;
			}
			else
			{
				// When not batch display error
				// defect 6456
				//leRTSEx.displayError((JDialog) null);
				leRTSEx.displayError();
				// end defect 6456
			}
		}
	}

	/**
	 * Sets the new barcode tag.
	 * 
	 * @param asBARCODE_TAG_BEGIN String
	 */
	static void setBARCODE_TAG_BEGIN(String asBARCODE_TAG_BEGIN)
	{
		ssBARCODE_TAG_BEGIN = asBARCODE_TAG_BEGIN;
	}

	/**
	 * Set the barcode tag end.
	 *
	 * @param asBARCODE_TAG_END String
	 */
	static void setBARCODE_TAG_END(String asBARCODE_TAG_END)
	{
		ssBARCODE_TAG_END = asBARCODE_TAG_END;
	}

	/**
	 * Set the barcode trans.
	 *
	 * @param asBarcodeTrans String
	 */
	public void setBarcodeTrans(int asBarcodeTrans)
	{
		StringBuffer lsTemp = new StringBuffer();
		int liCounter = 55;
		if (asBarcodeTrans == 1)
		{
			liCounter = 49;
		}
		for (int i = 1; i < liCounter; i++)
		{
			lsTemp.append(" ");
		}
		csBarcodeTrans =
			System.getProperty("line.separator")
				+ System.getProperty("line.separator")
				+ System.getProperty("line.separator")
				+ System.getProperty("line.separator")
				+ System.getProperty("line.separator")
				+ System.getProperty("line.separator")
				+ System.getProperty("line.separator")
				+ System.getProperty("line.separator")
				+ System.getProperty("line.separator")
				+ lsTemp.toString();
		cbBarcodePrefixExists = true;
	}

	/**
	 * This method sets the connection status.
	 * This was done in <code>openFTPConnection() && 
	 * openFtpConnection(int, int)</code>
	 * <p>Integer.MIN_VALUE is passed for both parameters when redirect 
	 * is not needed.</p>
	 *
	 * @param aiRedirectSubsta int
	 * @param aiRedirectWsid int
	 * @throws RTSException
	 */
	private void setConnectionStatus(
		int aiRedirectSubsta,
		int aiRedirectWsid)
		throws RTSException
	{
		try
		{
			int liOfcIssuanceNo = SystemProperty.getOfficeIssuanceNo();
			int liSubStaId = SystemProperty.getSubStationId();
			int liWkId = SystemProperty.getWorkStationId();
			String lsCPName = "";
			// Check to see if Batch
			if (isBatch(aiRedirectSubsta, aiRedirectWsid))
			{
				// Check to see if going to server is needed
				// if they are == going to cache
				if (aiRedirectSubsta == liSubStaId)
				{
					// if Local WsId == Redirect Printert WsId
					// no need to do anything both Substa and 
					// WsId's are ==
					if (liWkId == aiRedirectWsid)
					{
						// This means that a local printer command will 
						// be issued
						ciConnectionStatus = LOCAL_PRINTER;
					}
					else
					{
						liWkId = aiRedirectWsid;
						// Get the cpname from cache since the 
						// substation was the same as the local 
						// workstation
						AssignedWorkstationIdsData lAssgnWkIdData =
							AssignedWorkstationIdsCache.getAsgndWsId(
								liOfcIssuanceNo,
								liSubStaId,
								liWkId);
						lsCPName = lAssgnWkIdData.getCPName();
					}
				}
				// Since the redirect is trying to cross a substation 
				// we must go to the server to GET_WS_IDS and get then 
				// cpname from the list returned
				else
				{
					liSubStaId = aiRedirectSubsta;
					liWkId = aiRedirectWsid;
					// Setup the search keys
					GeneralSearchData laGSD = new GeneralSearchData();
					laGSD.setIntKey1(liOfcIssuanceNo);
					laGSD.setIntKey2(liSubStaId);
					// Going to server to GET_WS_IDS
					Vector lvWSList =
						(Vector) Comm.sendToServer(
							GeneralConstant.SYSTEMCONTROLBATCH,
							SystemControlBatchConstant.GET_WS_IDS,
							laGSD);
					if (lvWSList.size() > 0)
					{
						// get the selected workstation
						for (int i = 0; i < lvWSList.size(); i++)
						{
							AssignedWorkstationIdsData laWSID =
								(
									AssignedWorkstationIdsData) lvWSList
										.get(
									i);
							if (laWSID.getWsId() == liWkId)
							{
								lsCPName = laWSID.getCPName();
								break;
							}
						}
					}
					else
					{
						// set Connection Status to no printer if 
						// list length returned from server is 0
						ciConnectionStatus = NO_PRINTER;
						csErrorMsg =
							csErrorMsg
								+ "Redirect outside the "
								+ "current substation was "
								+ "unsucessfull.\n";
						return;
					}
				}
			}
			// Not Batch no redirect use what is in set print 
			// destination
			else
			{
				// Going to cache to get Assigned Ws
				AssignedWorkstationIdsData lAssgnWkIdData =
					AssignedWorkstationIdsCache.getAsgndWsId(
						liOfcIssuanceNo,
						liSubStaId,
						liWkId);
				// Get the redirect Workstation number
				int liPrinterWkId = lAssgnWkIdData.getRedirPrtWsId();
				// Lookup the cpname for the redirect Workstation number
				AssignedWorkstationIdsData laPrinterData =
					AssignedWorkstationIdsCache.getAsgndWsId(
						liOfcIssuanceNo,
						liSubStaId,
						liPrinterWkId);
				lsCPName = laPrinterData.getCPName();
				// "144.45.193.184#LPT1"
				// If Local Workstation ID == Redirect Printert WsId
				if (liWkId == liPrinterWkId)
				{
					// This means that a local printer command will 
					// be issued
					ciConnectionStatus = LOCAL_PRINTER;
				}
			}
			if (ciConnectionStatus != LOCAL_PRINTER)
			{
				// Changed this from local variable to class variable
				csServerName =
					lsCPName.substring(0, lsCPName.indexOf("#"));
				// Conformed variable name to standards was printerName
				csPrinterName =
					lsCPName.substring(
						lsCPName.indexOf("#") + 1,
						lsCPName.length());
				// Method to ping machine, if cannot connect set 
				// connectionStatus to NO_PRINTER
				if (ping(csServerName))
				{
					// Changed from FTP_SERVER to LPR_SERVER
					ciConnectionStatus = LPR_SERVER;
				}
				// Machine is online
				else
				{
					ciConnectionStatus = NO_PRINTER;
					// defect 7562
					// mispelled word failed.
					csErrorMsg =
						csErrorMsg
							+ "Attempted connection to machine "
							+ csServerName
							+ " Failed.\n";
					// defect 7562
				}
			}
		}
		catch (Exception leEx)
		{
			ciConnectionStatus = NO_PRINTER;
			csErrorMsg =
				csErrorMsg
					+ "Exception setting printer "
					+ "connections status - "
					+ leEx.toString()
					+ "\n";
			Log.write(
				Log.SQL_EXCP,
				this,
				" Connection Status set to "
					+ "NO_PRINTER b/c of an Exception - "
					+ leEx.toString());
		}
	}

	/**
	 * Set dup coppies.
	 * 
	 * @param aiDupCopies int
	 */
	public static void setDupCopies(int aiDupCopies)
	{
		siDupCopies = aiDupCopies;
	}

	/**
	 * Set HDRLASER PCL.
	 * 
	 * @param asPRINT_HDRLASER String
	 */
	static void setPRINT_HDRLASER(String asPRINT_HDRLASER)
	{
		ssPRINT_HDRLASER = asPRINT_HDRLASER;
	}

	/**
	 * Set PCL.
	 *
	 * @param asPRINT_HORHDRSPC12 String
	 */
	public static void setPRINT_HORHDRSPC12(String asPRINT_HORHDRSPC12)
	{
		ssPRINT_HORHDRSPC12 = asPRINT_HORHDRSPC12;
	}

	/**
	 * Set PCL.
	 *
	 * @param asPRINT_HORHDRSPC6 String
	 */
	public static void setPRINT_HORHDRSPC6(String asPRINT_HORHDRSPC6)
	{
		ssPRINT_HORHDRSPC6 = asPRINT_HORHDRSPC6;
	}

	/**
	 * Set Laser PCL.
	 * 
	 * @param asPRINT_LASER String
	 */
	static void setPRINT_LASER(String asPRINT_LASER)
	{
		ssPRINT_LASER = asPRINT_LASER;
	}

	/**
	 * Returns the PCL for Salvage documents top margin.
	 *
	 * @param asPRINT_SLVG_TOP_MARGIN String
	 */
	public static void setPRINT_SLVG_TOP_MARGIN(String asPRINT_SLVG_TOP_MARGIN)
	{
		if (asPRINT_SLVG_TOP_MARGIN == null)
		{
			ssPRINT_SLVG_TOP_MARGIN = ESC_CHAR + "&l4E";
		}
		else
		{
			ssPRINT_SLVG_TOP_MARGIN =
				ESC_CHAR + asPRINT_SLVG_TOP_MARGIN;
		}
	}

	/**
	 * Use this method to tell the print class to return all
	 * exceptions and not display them.
	 *
	 * @param abReturnException boolean
	 */
	public void setReturnException(boolean abReturnException)
	{
		cbReturnException = abReturnException;
	}

	//	/**
	//	* Converts the bitstring to barcode lines and adds the clear text 
	//	* Barcode
	//	*
	//	* Pertinent PCL (all units in decipoints, 720 decipoints/in.
	//	* ----------------------------------------------------------
	//	* PUSH/POP CAP 	Esc&f#S 0, 1
	//	* Move Horizontal 	Esc*a#H (-32767) - (+32767)
	//	* Move Vertical 	Esc*a#V (-32767) - (+32767)
	//	* Fill Rectangle	Esc*c#P 0-5 (0-black, 1- white)
	//	* Rect Horizontal 	Esc*c#H 0-32767
	//	* Rect Vertical 	Esc*c#V 0-32767
	//	* 
	//	* @param asBarCodeTextWithBitString String The bitstring for barcode
	//	* @param asBarCodeText String The clear text for barcode
	//	* @param ladCPI int Chars per inch (used for barcode)
	//	* @param asBarCodeTextWithCheckSum String
	//	* @param abShouldStickerPrint boolean
	//	* @return String
	//	* @deprecated
	//	*/
	//	private String translateToPCL(
	//		String asBarCodeTextWithBitString,
	//		String asBarCodeText,
	//		double adCPI,
	//		String asBarCodeTextWithCheckSum,
	//		boolean abShouldStickerPrint)
	//	{
	//		//create the return object
	//		StringBuffer lsPCLString = new StringBuffer("");
	//		// 1/2 inch bar code + seperator space below text
	//		int liHBox = HBAR + 60;
	//		// PCR 34
	//		if (abShouldStickerPrint)
	//		{
	//			liHBox = HBAR - 130;
	//		}
	//		// END PCR 34
	//		double ldWBox = 0;
	//		for (int i = 0; i < asBarCodeTextWithBitString.length(); i++)
	//		{
	//			if (asBarCodeTextWithBitString.charAt(i) == '0')
	//			{
	//				ldWBox += WBAR;
	//			}
	//			else
	//			{
	//				ldWBox += 3 * WBAR;
	//			}
	//		}
	//		//set rectangle height
	//		lsPCLString.append(ESC_CHAR + "*c" + liHBox + "V");
	//		boolean lbisFilledRectangle = false;
	//		double ldWBox_Temp = ldWBox;
	//		//scale down to two digits after the decimal point
	//		BigDecimal laWBox_Temp = new BigDecimal(ldWBox);
	//		laWBox_Temp = laWBox_Temp.setScale(2, BigDecimal.ROUND_HALF_UP);
	//		lsPCLString =
	//			lsPCLString.append(ESC_CHAR + "*c" + laWBox_Temp + "H");
	//		lsPCLString = lsPCLString.append(ESC_CHAR + "*c0P");
	//		//print the barcode 
	//		for (int i = asBarCodeTextWithBitString.length() - 1;
	//			i > -1;
	//			i--)
	//		{
	//			int libar =
	//				Integer.parseInt(
	//					asBarCodeTextWithBitString.substring(i, i + 1));
	//			ldWBox_Temp = ldWBox_Temp - WBAR * (1 + 2 * libar);
	//			if (lbisFilledRectangle)
	//			{
	//				//draw filled rectangle with width  determined by 
	//				//bitstring
	//				laWBox_Temp = new BigDecimal(ldWBox_Temp);
	//				laWBox_Temp =
	//					laWBox_Temp.setScale(2, BigDecimal.ROUND_HALF_UP);
	//				lsPCLString.append(ESC_CHAR + "*c" + laWBox_Temp + "H");
	//				lsPCLString.append(ESC_CHAR + "*c0P");
	//				lbisFilledRectangle = false;
	//			}
	//			else
	//			{
	//				//draw empty triangle with witdth determined by 
	//				//bitstring
	//				laWBox_Temp = new BigDecimal(ldWBox_Temp);
	//				laWBox_Temp =
	//					laWBox_Temp.setScale(2, BigDecimal.ROUND_HALF_UP);
	//				lsPCLString.append(ESC_CHAR + "*c" + laWBox_Temp + "H");
	//				lsPCLString.append(ESC_CHAR + "*c1P");
	//				lbisFilledRectangle = true;
	//			}
	//		}
	//		//clear text area
	//		lsPCLString.append(ESC_CHAR + "*c60V");
	//		BigDecimal laWBox = new BigDecimal(ldWBox);
	//		laWBox = laWBox.setScale(2, BigDecimal.ROUND_HALF_UP);
	//		lsPCLString.append(ESC_CHAR + "*c" + laWBox + "H");
	//		lsPCLString.append(ESC_CHAR + "*c1P");
	//		//print encoded string centereed under barcode (assume 15 cpi)
	//		//calculate box width in characters
	//		int liNoOfChar = (int) (ldWBox * adCPI) / 720;
	//		int liNoOfBlank = (liNoOfChar - asBarCodeText.length()) / 2;
	//		//add spacing 
	//		for (int i = 0; i < liNoOfBlank; i++)
	//		{
	//			lsPCLString.append(" ");
	//		}
	//		//add the cleartext (the number) barcode
	//		// laPCLString.append(lsBarCodeTextWithCheckSum);
	//		//add spacing
	//		for (int i = 1; i < liNoOfBlank + 1; i++)
	//		{
	//			lsPCLString.append(" ");
	//		}
	//		StringBuffer temp = new StringBuffer();
	//		// In case of NRCOT and CPROT, the barcode already exits
	//		// Refer <code>Transaction</code>class, 
	//		// <code>printDocument</code> method (they call 
	//		// <code>setBarCodeTrans</code> method of this class.
	//		//
	//		// For all other transcation types, we create the Buffer with 
	//		// spaces and the line breaks. 
	//		if (!cbBarcodePrefixExists)
	//		{
	//			for (int i = 1; i < 55; i++)
	//			{
	//				temp.append(" ");
	//			}
	//			//<code>csBarCodeTrans</code> cleared to make sure that the 
	//			//transids are not appeneded in a multiple transaction 
	//			//scenario (DTA). 
	//			csBarcodeTrans = "";
	//			csBarcodeTrans =
	//				System.getProperty("line.separator")
	//					+ System.getProperty("line.separator")
	//					+ System.getProperty("line.separator")
	//					+ System.getProperty("line.separator")
	//					+ System.getProperty("line.separator")
	//					+ System.getProperty("line.separator")
	//					+ temp.toString();
	//		}
	//		csBarcodeTrans =
	//			csBarcodeTrans
	//				+ asBarCodeTextWithCheckSum.substring(0, 4)
	//				+ " "
	//				+ asBarCodeTextWithCheckSum.substring(4, 8)
	//				+ " "
	//				+ asBarCodeTextWithCheckSum.substring(8, 12)
	//				+ " "
	//				+ asBarCodeTextWithCheckSum.substring(12, 16)
	//				+ " "
	//				+ asBarCodeTextWithCheckSum.substring(
	//					16,
	//					asBarCodeTextWithCheckSum.length());
	//		//return the result
	//		return lsPCLString.toString() + csBarcodeTrans;
	//	}

	/**
	* Converts the bitstring to barcode lines and adds the clear text 
	* Barcode
	*
	* Pertinent PCL (all units in decipoints, 720 decipoints/in.
	* ----------------------------------------------------------
	* PUSH/POP CAP 	Esc&f#S 0, 1
	* Move Horizontal 	Esc*a#H (-32767) - (+32767)
	* Move Vertical 	Esc*a#V (-32767) - (+32767)
	* Fill Rectangle	Esc*c#P 0-5 (0-black, 1- white)
	* Rect Horizontal 	Esc*c#H 0-32767
	* Rect Vertical 	Esc*c#V 0-32767
	* 
	 * Copied translateToPCL
	 * made minor changes
	 * to make it work for 
	 * Receipts and Certificates
	 * 
	* @param asBarCodeTextWithBitString String The bitstring for barcode
	* @param asBarCodeText String The clear text for barcode
	* @param ladCPI int Chars per inch (used for barcode)
	* @param asBarCodeTextWithCheckSum String
	* @param abShouldStickerPrint boolean
	* @return String
	*/
	private String translateToPCLReceipts(
		String asBarCodeTextWithBitString,
		String asBarCodeText,
		double adCPI,
		String asBarCodeTextWithCheckSum,
		boolean abShouldStickerPrint,
		String asType)
	{
		//create the return object
		StringBuffer lsPCLString = new StringBuffer("");
		// 1/2 inch bar code + seperator space below text
		//int liHBox = HBAR + 60;
		int liHBox = HBAR + 60;
		// PCR 34
		if (abShouldStickerPrint)
		{
			liHBox = HBAR - 130;
		}
		// END PCR 34
		double ldWBox = 0;
		for (int i = 0; i < asBarCodeTextWithBitString.length(); i++)
		{
			if (asBarCodeTextWithBitString.charAt(i) == '0')
			{
				ldWBox += WBAR;
			}
			else
			{
				ldWBox += 3 * WBAR;
			}
		}
		//set rectangle height
		lsPCLString.append(ESC_CHAR + "*c" + liHBox + "V");
		boolean lbisFilledRectangle = false;
		double ldWBox_Temp = ldWBox;
		//scale down to two digits after the decimal point
		BigDecimal laWBox_Temp = new BigDecimal(ldWBox);
		laWBox_Temp = laWBox_Temp.setScale(2, BigDecimal.ROUND_HALF_UP);
		lsPCLString =
			lsPCLString.append(ESC_CHAR + "*c" + laWBox_Temp + "H");
		lsPCLString = lsPCLString.append(ESC_CHAR + "*c0P");
		//print the barcode 
		for (int i = asBarCodeTextWithBitString.length() - 1;
			i > -1;
			i--)
		{
			int libar =
				Integer.parseInt(
					asBarCodeTextWithBitString.substring(i, i + 1));
			ldWBox_Temp = ldWBox_Temp - WBAR * (1 + 2 * libar);
			if (lbisFilledRectangle)
			{
				//draw filled rectangle with width  determined by 
				//bitstring
				laWBox_Temp = new BigDecimal(ldWBox_Temp);
				laWBox_Temp =
					laWBox_Temp.setScale(2, BigDecimal.ROUND_HALF_UP);
				lsPCLString.append(ESC_CHAR + "*c" + laWBox_Temp + "H");
				lsPCLString.append(ESC_CHAR + "*c0P");
				lbisFilledRectangle = false;
			}
			else
			{
				//draw empty triangle with witdth determined by 
				//bitstring
				laWBox_Temp = new BigDecimal(ldWBox_Temp);
				laWBox_Temp =
					laWBox_Temp.setScale(2, BigDecimal.ROUND_HALF_UP);
				lsPCLString.append(ESC_CHAR + "*c" + laWBox_Temp + "H");
				lsPCLString.append(ESC_CHAR + "*c1P");
				lbisFilledRectangle = true;
			}
		}
		//clear text area
		lsPCLString.append(ESC_CHAR + "*c60V");
		BigDecimal laWBox = new BigDecimal(ldWBox);
		laWBox = laWBox.setScale(2, BigDecimal.ROUND_HALF_UP);
		lsPCLString.append(ESC_CHAR + "*c" + laWBox + "H");
		lsPCLString.append(ESC_CHAR + "*c1P");
		//print encoded string centereed under barcode (assume 15 cpi)
		//calculate box width in characters
		int liNoOfChar = (int) (ldWBox * adCPI) / 720;
		int liNoOfBlank = (liNoOfChar - asBarCodeText.length()) / 2;
		//add spacing 
		for (int i = 0; i < liNoOfBlank; i++)
		{
			lsPCLString.append(" ");
		}
		//add the cleartext (the number) barcode
		// laPCLString.append(lsBarCodeTextWithCheckSum);
		//add spacing
		for (int i = 1; i < liNoOfBlank + 1; i++)
		{
			lsPCLString.append(" ");
		}
		StringBuffer temp = new StringBuffer();
		// In case of NRCOT and CPROT, the barcode already exits
		// Refer <code>Transaction</code>class, 
		// <code>printDocument</code> method (they call 
		// <code>setBarCodeTrans</code> method of this class.
		//
		// For all other transcation types, we create the Buffer with 
		// spaces and the line breaks. 
		if (!cbBarcodePrefixExists)
		{
			StringBuffer lsTemp = new StringBuffer();
			//<code>csBarCodeTrans</code> cleared to make sure that the 
			//transids are not appeneded in a multiple transaction 
			//scenario (DTA). 
			//	Position the TransId for Receipts
			if (asType.equalsIgnoreCase("rcp"))
			{
				for (int i = 1; i < LS_RECEIPTS; i++)
				{
					temp.append(" ");
					csBarcodeTrans = "";
					csBarcodeTrans =
						System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ temp.toString();
				}
			}
			// defect 9668
			//	Position the TransId for Certificates
			else if (asType.equalsIgnoreCase("crt"))
			{
				for (int i = 1; i < LS_CERTIFICATES; i++)
				{
					lsTemp.append(" ");
					csBarcodeTrans =
						System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ System.getProperty("line.separator")
							+ lsTemp.toString();
				}

			}
		}
		csBarcodeTrans =
			csBarcodeTrans
				+ asBarCodeTextWithCheckSum.substring(0, 4)
				+ " "
				+ asBarCodeTextWithCheckSum.substring(4, 8)
				+ " "
				+ asBarCodeTextWithCheckSum.substring(8, 12)
				+ " "
				+ asBarCodeTextWithCheckSum.substring(12, 16)
				+ " "
				+ asBarCodeTextWithCheckSum.substring(
					16,
					asBarCodeTextWithCheckSum.length());
		//return the result
		return lsPCLString.toString() + csBarcodeTrans;
	}

	/**
	 * Converts the bitstring to barcode lines and adds the clear text 
	 * Barcode
	 *
	 * Pertinent PCL (all units in decipoints, 720 decipoints/in.
	 * ----------------------------------------------------------
	 * PUSH/POP CAP 	Esc&f#S 0, 1
	 * Move Horizontal 	Esc*a#H (-32767) - (+32767)
	 * Move Vertical 	Esc*a#V (-32767) - (+32767)
	 * Fill Rectangle	Esc*c#P 0-5 (0-black, 1- white)
	 * Rect Horizontal 	Esc*c#H 0-32767
	 * Rect Vertical 	Esc*c#V 0-32767
	 * 
	 * Copied translateToPCL
	 * made minor changes
	 * to make it just like RTSI
	 * 
	 * @param asBarCodeTextWithBitString String The bitstring 
	 *		for barcode
	 * @param asBarCodeText String The clear text for barcode
	 * @param aiCPI int Chars per inch (used for barcode)
	 * @param asBarCodeTextWithCheckSum String
	 * @return String
	 */
	private String translateToPCLReports(
		String asBarCodeTextWithBitString,
		String asBarCodeText,
		double adCPI,
		String asBarCodeTextWithCheckSum)
	{
		//create the return object
		StringBuffer lsPCLString = new StringBuffer("");
		// 1/2 inch bar code + seperator space below text
		int liHBox = 360 + 60;
		double ldWBox = 0;
		for (int i = 0; i < asBarCodeTextWithBitString.length(); i++)
		{
			if (asBarCodeTextWithBitString.charAt(i) == '0')
			{
				ldWBox += 7.2;
			}
			else
			{
				ldWBox += 3 * 7.2;
			}
		}
		//set rectangle height
		lsPCLString.append(ESC_CHAR + "*c" + liHBox + "V");
		boolean lbisFilledRectangle = false;
		double ldWBox_Temp = ldWBox;
		//scale down to two digits after the decimal point
		BigDecimal laWBox_Temp = new BigDecimal(ldWBox);
		laWBox_Temp = laWBox_Temp.setScale(1, BigDecimal.ROUND_HALF_UP);
		lsPCLString =
			lsPCLString.append(ESC_CHAR + "*c" + laWBox_Temp + "H");
		lsPCLString = lsPCLString.append(ESC_CHAR + "*c0P");
		//print the barcode 
		for (int i = asBarCodeTextWithBitString.length() - 1;
			i > -1;
			i--)
		{
			int libar =
				Integer.parseInt(
					asBarCodeTextWithBitString.substring(i, i + 1));
			ldWBox_Temp = ldWBox_Temp - 7.2 * (1 + 2 * libar);
			if (lbisFilledRectangle)
			{
				//draw filled rectangle with width  determined by 
				//bitstring
				laWBox_Temp = new BigDecimal(ldWBox_Temp);
				laWBox_Temp =
					laWBox_Temp.setScale(1, BigDecimal.ROUND_HALF_UP);
				lsPCLString.append(ESC_CHAR + "*c" + laWBox_Temp + "H");
				lsPCLString.append(ESC_CHAR + "*c0P");
				lbisFilledRectangle = false;
			}
			else
			{
				//draw empty triangle with witdth determined by 
				//bitstring
				laWBox_Temp = new BigDecimal(ldWBox_Temp);
				laWBox_Temp =
					laWBox_Temp.setScale(1, BigDecimal.ROUND_HALF_UP);
				lsPCLString.append(ESC_CHAR + "*c" + laWBox_Temp + "H");
				lsPCLString.append(ESC_CHAR + "*c1P");
				lbisFilledRectangle = true;
			}
		}
		//clear text area
		lsPCLString.append(ESC_CHAR + "*c60V");
		BigDecimal laWBox = new BigDecimal(ldWBox);
		laWBox = laWBox.setScale(1, BigDecimal.ROUND_HALF_UP);
		lsPCLString.append(ESC_CHAR + "*c" + laWBox + "H");
		lsPCLString.append(ESC_CHAR + "*c1P");
		//print encoded string under barcode (assume 15 cpi)
		//calculate box width in characters
		int liNoOfChar = (int) (ldWBox * adCPI) / 720;
		int liNoOfBlank = (liNoOfChar - asBarCodeText.length()) / 2;
		lsPCLString.append(asBarCodeTextWithCheckSum);
		//add spacing
		for (int i = 1; i < liNoOfBlank + 1; i++)
		{
			lsPCLString.append(" ");
		}
		return lsPCLString.toString();
	}

	/**
	 * Writes the output in the specified output file. If there is a 
	 * barcode text value in the input file, replaces it with the actual
	 * barcode by setting a graphics area and by drawing lines in that 
	 * area. 
	 * 
	 * @param asInputFileName String
	 * @param asOutputFileName String
	 * @param asType String
	 * @param asHeader String Either header (or) duplicate header
	 * @return boolean
	 * @throws RTSException
	 */
	private boolean writeOutput(
		String asInputFileName,
		String asOutputFileName,
		String asType,
		String asHeader)
		throws RTSException
	{
		try
		{
			//open the input and output files
			BufferedReader laInputFile =
				new BufferedReader(new FileReader(asInputFileName));
			PrintWriter laOutputFile =
				new PrintWriter(new FileWriter(asOutputFileName), true);
			//initialize 
			String lsLine = null; //read from input file
			String lsBarCodeTag = null;
			//Barcode tag that is looked for in the input
			String lsBarCodeText = null;
			//the barcode (from barcode tag)
			double ldcpi = 0;
			//cpi value that comes as a argument in barcode tag.
			int liBarCodeBeginLocation = Integer.MIN_VALUE;
			int liBarCodeEndLocation = Integer.MIN_VALUE;
			//print the header to the output file
			//laOutputFile.print(lsHeader);
			//print the document properties to output file 
			//laOutputFile.println(lsDocProperties);
			//process all the lines in the input file

			// defect 7141
			// Stripping tray definition out of the report and replacing
			// it with tray2 definition so that the barcode file will 
			// never come out of tray3 (sticker paper tray)
			boolean lbReplaceTray = true;
			while ((lsLine = laInputFile.readLine()) != null)
			{
				// don't want to check for tray definition once it was 
				// already found
				if (lbReplaceTray)
				{
					String lsReplaceString = lsLine;
					// Search for tray3 and replace with tray 2
					lsReplaceString =
						UtilityMethods.replaceString(
							lsReplaceString,
							getPRINT_TRAY_3(),
							getPRINT_TRAY_2());
					// defect 7184, 7227
					// do not remove tray one definition b/c salvage 
					// receipts are sent to tray1.  Search for tray1 and
					// replace with tray 2
					//lsReplaceString = UtilityMethods.replaceString(
					//			lsReplaceString, 
					//			getPRINT_TRAY_1(), 
					//			getPRINT_TRAY_2());
					// end defect 7184, 7227
					if (!lsReplaceString.equals(lsLine))
					{
						lsLine = lsReplaceString;
						lbReplaceTray = false;
					}
				}
				// end defect 7141

				//look for barcode tag location
				liBarCodeBeginLocation =
					lsLine.indexOf(ssBARCODE_TAG_BEGIN);
				//if barcode tag found in the line
				if (liBarCodeBeginLocation != -1)
				{
					//flush all chars before the barcode tag
					laOutputFile.print(
						lsLine.substring(0, liBarCodeBeginLocation));
					//Build the complete tag
					liBarCodeEndLocation =
						lsLine.indexOf(ssBARCODE_TAG_END);
					//if the end of barcode is on the same line
					//then get the barcode
					if (liBarCodeEndLocation != -1)
					{
						lsBarCodeTag =
							lsLine.substring(
								liBarCodeBeginLocation,
								liBarCodeEndLocation);
						//get the value and cpi
						StringTokenizer laBarCodeTokenizer =
							new StringTokenizer(lsBarCodeTag, "'");
						String lsDummy = laBarCodeTokenizer.nextToken();
						lsBarCodeText = laBarCodeTokenizer.nextToken();
						lsDummy = laBarCodeTokenizer.nextToken();
						ldcpi =
							Double.parseDouble(
								laBarCodeTokenizer.nextToken());
						// PCR 34
						lsDummy = laBarCodeTokenizer.nextToken();
						String lsBool;

						// b/c title package uses this method for it's 
						// barcode and it's barcode tag does not have 
						// sticker='true' it would cause an exception 
						// when calling nextToken()
						if (laBarCodeTokenizer.hasMoreTokens())
						{
							lsBool = laBarCodeTokenizer.nextToken();
						}
						else
						{
							lsBool = "false";
						}

						boolean lbShouldStickerPrint =
							new Boolean(lsBool).booleanValue();
						//build the pcl for the barcode text and cpi
						// defect 3994
						// Added barcode to Title Package Report - 
						// passing file type in order to handle the 
						// barcode on reports properly
						//String lsBarCodePCL = barCodeTo2Of5x(
						//				lsBarCodeText, ldcpi, lsType);
						// end defect 3994
						// END PCR 34
						String lsBarCodePCL =
							barCodeTo2Of5x(
								lsBarCodeText,
								ldcpi,
								asType,
								lbShouldStickerPrint);
						laOutputFile.print(lsBarCodePCL);
						//print the remainder of the line
						laOutputFile.println(
							lsLine.substring(
								liBarCodeEndLocation + 2,
								lsLine.length()));
					}
					//if the tag does not end on the same line, pick up
					//from next line
					else
					{
						//get everything till the end of the first line
						lsBarCodeTag =
							lsLine.substring(liBarCodeBeginLocation);
						//read the next line
						lsLine = laInputFile.readLine();
						//get the ending position
						liBarCodeEndLocation = lsLine.indexOf(">");
						//add the string till the end to the tag
						lsBarCodeTag =
							lsLine.substring(0, liBarCodeEndLocation);
						//get the value and cpi
						StringTokenizer laBarCodeTokenizer =
							new StringTokenizer(lsBarCodeTag, "'");
						String lsDummy = laBarCodeTokenizer.nextToken();
						lsBarCodeText = laBarCodeTokenizer.nextToken();
						lsDummy = laBarCodeTokenizer.nextToken();
						ldcpi =
							Double.parseDouble(
								laBarCodeTokenizer.nextToken());
						// PCR 34
						lsDummy = laBarCodeTokenizer.nextToken();
						String lsBool;

						// b/c title package uses this method for it's 
						// barcode and it's barcode tag does not have 
						// sticker='true' it would cause an exception 
						// when calling nextToken()
						if (laBarCodeTokenizer.hasMoreTokens())
						{
							lsBool = laBarCodeTokenizer.nextToken();
						}
						else
						{
							lsBool = "false";
						}

						boolean lbShouldStickerPrint =
							new Boolean(lsBool).booleanValue();
						// END PCR 34
						// defect 3994
						// Added barcode to Title Package Report - 
						// passing file type in order to handle the 
						// barcode on reports properly
						// String lsBarCodePCL = barCodeTo2Of5x(
						//				lsBarCodeText, ldcpi, lsType);
						// end defect 3994
						//build the pcl for the barcode text and cpi
						// defect 7863
						// was referencing a deprecated method and 
						// changed to the correct method call.
						//String lsBarCodePCL =
						//	barCodeTo2Of5x(
						//			lsBarCodeText, 
						//			ldcpi, 
						//			lbShouldStickerPrint);
						String lsBarCodePCL =
							barCodeTo2Of5x(
								lsBarCodeText,
								ldcpi,
								asType,
								lbShouldStickerPrint);
						// end defect 7863
						laOutputFile.print(lsBarCodePCL);
						//print the remainder of the 2nd line
						laOutputFile.println(
							lsLine.substring(
								liBarCodeEndLocation,
								lsLine.length()));
					}
				}
				else
				{
					//if barcode not found in line, just copy the line 
					//to output
					laOutputFile.println(lsLine);
				}
				lsLine = null;
			}
			//close the files that are opened for processing
			laInputFile.close();
			laOutputFile.close();

			return true;

		} //end of printing 
		catch (IOException leIOEx)
		{
			//ioe.printStackTrace();
			RTSException laRTSE =
				new RTSException(RTSException.SYSTEM_ERROR, leIOEx);
			throw laRTSE;
			//return false; 
		}
		// moved before catch
		//return true;
	}

	/**
	 * @return ssPRINT_TRAY_STICKER string
	 */
	public static String getPRINT_TRAY_STICKER()
	{
		return ssPRINT_TRAY_STICKER;
	}
}