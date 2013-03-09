package com.txdot.isd.rts.services.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.comm.*;

import com.txdot.isd.rts.services.data.PlateBarCodeData;
import com.txdot.isd.rts.services.data.RenewalBarCodeData;
import com.txdot.isd.rts.services.data.StickerBarCodeData;
import com.txdot.isd.rts.services.data.StickerPrintingBarCodeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.event.BarCodeEvent;
import com.txdot.isd.rts.services.util.event.BarCodeListener;
/*
 *
 * BarCodeScanner.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting       04/15/2002  Fix defect CQU100003462
 * M Abs        05/06/2002  Allowing for maching gunning of barcode
 * 							CQU100003758
 * M Abs        05/09/2002  Reset buffer when a read error occurs 
 * 							CQU100003844
 * M Abs        05/13/2002  Fixing for subcon
 * M Abs        05/14/2002  Improved performance
 * M Abs        05/2520/02  Finalized performance
 * M Abs        06/10/2002  Fix for CQU100004237
 * R Hicks		09/05/2002  Make class thread safe
 * J Rue		06/08/2004	Add Sticker Printing barcode scan.
 *							Format all methods. 
 *							Remove all unnecessary spaces from 
 *								existing methods.
 *							add makeStickerPrinterData()
 *							add getter/setter for csRawBarcodeData
 *							modify makeRenewalData()
 *							defect 7108 Ver 5.2.1
 * B Hargrove	07/29/2004	Add fields to hold 15 acct items and fees
 *							and allow 3 char RegClassCd for Version 4.
 *							add Class variables, setFieldPositions() 
 *							modify Class variables, makeRenewalData(),
 *							serialEvent() 
 *							defect 7348 Ver 5.2.1
 * B Hargrove	09/15/2004	Change class variable definitions for
 *							barcode field positions to 'public' to
 *							allow use by FrmBarcodeReader. 
 *							modify Class variables
 *							defect 7293 Ver 5.2.1
 * B Hargrove	09/17/2004	Modify constants for length of Plate,
 *							Sticker, and Sticker Print barcodes.
 *							The read of type and version removes
 *							3 bytes from barcodestring before it is
 *                          compared to barcode length constant.
 *							See: serialEvent().
 *							modify PLATE_LENGTH, STICKER_LENGTH,
 *							STKRPRNT_LENGTH
 *							defect 7293 Ver 5.2.1
 * B Hargrove	09/15/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * Jeff S.		01/17/2006	Made change to return out of serialEvent()
 * 							when character 10 or New Line is the first
 * 							Character in the buffer.  This helps with 
 * 							the scanner problem.
 * 							add REMOVED_CHAR_10
 * 							modify serialEvent()
 * 							defect 8512 Ver 5.2.2 fix 8
 * K Harrell	04/12/2007	Removing code for R03, adding R05
 *  						delete RENEWAL_LENGTH, setFieldPositions() 
 *							add BARCODE_VERSION05, R05_REMAINING_LENGTH, 
 *							 RENEWAL_ORGNO_END, RENEWAL_ADDLSETINDI_END, 
 *							 R04_REMAINING_LENGTH, R05_REMAINING_LENGTH
 * 							modify BUFFER_SIZE, RENEWAL_ACCTX_END, 
 * 							 RENEWAL_PRICEX_END  (where X = 1-15) 
 *							modify makeRenewalData(), serialEvent() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/23/2007	Code changes for NewPltsReqdCd 
 * 							add RENEWAL_NEWPLTSREQDCD_END
 * 							modify BUFFER_SIZE, R05_REMAINING_LENGTH 
 * 							modify makeRenewalData() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/17/2007  Prologue update for 4/12/2007
 * 							defect 9263 Ver Special Plates 2  
 * T Pederson	01/04/2010 	Removing code for barcode version 04.
 * 							Add fields for barcode version 06.
 * 							delete BARCODE_VERSION04, 
 * 							R04_REMAINING_LENGTH									
 *							add BARCODE_VERSION06, RENEWAL_NEXTEXPMO_END,
 *							RENEWAL_NEXTEXPYR_END, 
 *							RENEWAL_PLTSVLDTYTERM_END, 
 *							RENEWAL_PLTEXPMO_END, RENEWAL_PLTEXPYR_END,
 *							RENEWAL_PLTNEXTEXPMO_END,
 *							RENEWAL_PLTNEXTEXPYR_END,
 *							and R06_REMAINING_LENGTH.
 * 							modify BUFFER_SIZE 
 *							modify serialEvent, makeRenewalData, 
 *							defect 10303  Ver Defect_POS_H
 * K Harrell	06/20/2010	modify STKRPRNT_LENGTH, STKRPRNT_REG_CLASS_END
 * 							defect 10464 Ver 6.5.0 
 *----------------------------------------------------------------------
 */
/**
 * The BarCodeScanner is reponsible for handling the input from the 
 * physical barcode scanner and interpreting it and parsing the input 
 * into the proper data object.
 * <p>Classes wishing to use the barcode scanner should make a call to 
 * mediator.getAppController().getBarCodeScanner().  
 * They can then catch BarCodeEvents thrown by the BarCodeScanner.
 * <p>The portname should be changed for every system, since that value 
 * is always computer specific.  
 *
 * @version	6.5.0 				06/20/2010
 * @author 	Michael Abernethy
 * <br>Creation Date:			09/10/2001 
 * 
 */

public class BarCodeScanner
	implements Runnable, SerialPortEventListener
{

	private final static String MSG_DISPATCHING =
		"BarCodeScanner :: Dispatching BarCodeEvent : ";
	private final static String MSG_ERR_PROCESSED = "Error processed";
	private final static String MSG_SUSPENDED = "Scanning suspended...";
	private final static String MSG_THREAD_STARTED =
		"BarCodeScanner :: BarcodeScanner Thread started...";
	private final static String MSG_WAITING =
		"BarCodeScanner :: Waiting for a BarCode Event...";

	private final static String STR_BARCODE_SCANNER = "Barcode Scanner";
	private final static String STR_BUFFER_SIZE = "Buffer Size = ";
	private final static String STR_PLATE = "Plate = ";

	// defect 8512
	private static final String REMOVED_CHAR_10 =
		"Removed Char 10 from Barcode Data.";
	// end defect 8512

	// The portname that the barcode scanner is connected to.
	private final static String BARCODE_SCANNER_PORTNAME =
		SystemProperty.getBarcodePort();

	// Fields that define the length in characters of each barcode type.  
	private final static int PLATE_LENGTH = 20;
	private final static int STICKER_LENGTH = 22;

	// The timeout waiting for the serial port to respond
	private final static int TIMEOUT_TIME = 3000;

	// The size of the buffer in bytes
	// defect 10303
	// defect 9085 
	//private final static int BUFFER_SIZE = 320;
	//private final static int BUFFER_SIZE = 326;
	private final static int BUFFER_SIZE = 346;
	// end defect 9085 
	// end defect 10303

	// The baud rate of the bar code scanner
	private final static int BAUD_RATE = 38400;

	// String variables to construct a Plate data type
	private final static int PLATE_VERSION_START = 1;
	private final static int PLATE_VERSION_END = 3;
	private final static int PLATE_ITEMCD_END = 11;
	private final static int PLATE_ITEMYR_END = 15;
	private final static int PLATE_ITEMNO_END = 23;

	// String variables to construct a Sticker data type
	private final static int STICKER_VERSION_START = 1;
	private final static int STICKER_VERSION_END = 3;
	private final static int STICKER_ITEMCD_END = 11;
	private final static int STICKER_ITEMYR_END = 15;
	private final static int STICKER_ITEMNO_END = 25;
	// defect 7108
	// Added variables for Barcode testing
	private String csRawBarcodeData = "";
	private final static int STKRPRNT = 3;
	private final static int TYPE_START = 0;
	private final static int VERSION_START = 1;
	private final static int VERSION_END = 3;

	private final static String STKRPRNT_STRING = "X";
	// defect 10464 
	//private final static int STKRPRNT_LENGTH = 94;
	private final static int STKRPRNT_LENGTH = 95;
	// end defect 10464
	private final static int STKRPRNT_DOCNO_START = 3;
	private final static int STKRPRNT_DOCNO_END = 20;
	private final static int STKRPRNT_VIN_END = 42;
	private final static int STKRPRNT_RESCOMPCNTYNO_END = 45;
	private final static int STKRPRNT_WSID_END = 55;
	private final static int STKRPRNT_PRTCNTYNO_END = 58;
	private final static int STKRPRNT_STKRPRTDATE_END = 66;
	private final static int STKRPRNT_REGPLTCD_END = 74;
	private final static int STKRPRNT_REGSTKRCD_END = 82;
	private final static int STKRPRNT_REGPLTNO_END = 89;
	private final static int STKRPRNT_REGEXPDATE_END = 95;
	// end defect 7108
	
	// defect 10464
	// RegClasCd to 3 digits
	//private final static int STKRPRNT_REGCLASS_END = 97;  
	private final static int STKRPRNT_REGCLASS_END = 98;
	// end defect 10464 

	// The 3 different types of Barcodes in the RTSII system.
	public final static int RENEWAL = 0;
	public final static int STICKER = 1;
	public final static int PLATE = 2;
	public final static int ERROR = -1;

	public final static int BARCODE_TYPE_START = 0;
	public final static int BARCODE_TYPE_END = 1;
	public final static int BARCODE_VERSION_END = 2;
	public final static int RENEWAL_DOCNO_START = 3;
	public final static int RENEWAL_DOCNO_END = 20;
	public final static int RENEWAL_VIN_END = 42;
	public final static int RENEWAL_AUDIT_END = 59;
	public final static int RENEWAL_REGPLTCD_END = 67;
	public final static int RENEWAL_STKRCD_END = 75;
	public final static int RENEWAL_REGPLTNO_END = 82;
	public final static int RENEWAL_EXPMO_84 = 84;
	public final static int RENEWAL_EXPMO_END = 84;
	public final static int RENEWAL_EXPYR_END = 88;
	public final static int RENEWAL_CNTYNO_END = 91;
	public final static int RENEWAL_ACCT1_END = 99;
	public final static int RENEWAL_ACCT2_END = 107;
	public final static int RENEWAL_ACCT3_END = 115;
	public final static int RENEWAL_ACCT4_END = 123;
	public final static int RENEWAL_ACCT5_END = 131;
	public final static int RENEWAL_ACCT6_END = 139;
	public final static int RENEWAL_ACCT7_END = 147;
	public final static int RENEWAL_ACCT8_END = 155;
	public final static int RENEWAL_ACCT9_END = 163;
	public final static int RENEWAL_ACCT10_END = 171;
	public final static int RENEWAL_ACCT11_END = 179;
	public final static int RENEWAL_ACCT12_END = 187;
	public final static int RENEWAL_ACCT13_END = 195;
	public final static int RENEWAL_ACCT14_END = 203;
	public final static int RENEWAL_ACCT15_END = 211;
	public final static int RENEWAL_PRICE1_END = 218;
	public final static int RENEWAL_PRICE2_END = 225;
	public final static int RENEWAL_PRICE3_END = 232;
	public final static int RENEWAL_PRICE4_END = 239;
	public final static int RENEWAL_PRICE5_END = 246;
	public final static int RENEWAL_PRICE6_END = 253;
	public final static int RENEWAL_PRICE7_END = 260;
	public final static int RENEWAL_PRICE8_END = 267;
	public final static int RENEWAL_PRICE9_END = 274;
	public final static int RENEWAL_PRICE10_END = 281;
	public final static int RENEWAL_PRICE11_END = 288;
	public final static int RENEWAL_PRICE12_END = 295;
	public final static int RENEWAL_PRICE13_END = 302;
	public final static int RENEWAL_PRICE14_END = 309;
	public final static int RENEWAL_PRICE15_END = 316;
	public final static int RENEWAL_REGCLASS_END = 319;

	// defect 9085 
	// defect 10303
	//public final static String BARCODE_VERSION04 = "04";
	// end defect 10303
	public final static String BARCODE_VERSION05 = "05";
	public final static int RENEWAL_ORGNO_END = 323;
	public final static int RENEWAL_ADDLSETINDI_END = 324;
	public final static int RENEWAL_NEWPLTSREQDCD_END = 325;
	// defect 10303
	//private static int R04_REMAINING_LENGTH = 316;
	// end defect 10303
	private static int R05_REMAINING_LENGTH = 322;
	// end defect 9085 

	// defect 10303
	public final static String BARCODE_VERSION06 = "06";
	public final static int RENEWAL_NEXTEXPMO_END = 327;
	public final static int RENEWAL_NEXTEXPYR_END = 331;
	public final static int RENEWAL_PLTVLDTYTERM_END = 333;
	public final static int RENEWAL_PLTEXPMO_END = 335;
	public final static int RENEWAL_PLTEXPYR_END = 339;
	public final static int RENEWAL_PLTNEXTEXPMO_END = 341;
	public final static int RENEWAL_PLTNEXTEXPYR_END = 345;
	private static int R06_REMAINING_LENGTH = 342;
	// end defect 10303

	private final static String STICKER_STRING = "S";
	private final static String PLATE_STRING = "P";
	private final static String RENEWAL_STRING = "R";
	private String csTypeOfScan = "";
	private String csBarCodeVersion = "";


	//private BarCodeThread barcodeThread;
	private static Vector cvScanQueue = new Vector();
	private static BarCodeScanner caBarCodeScanner = null;
	private static boolean cbAcceptingScans = true;

	/**
	 * An graph of the serial port.
	 */
	private SerialPort caSerialPort;

	/**
	 * An graph of the input stream.
	 */
	private BufferedInputStream caInputStream;

	/**
	 * A vector of all the Listeners.
	 */
	private Vector cvListeners;

	/**
	 * Creates a BarCodeScanner
	 * 
	 * @throws RTSException
	 */
	public BarCodeScanner() throws RTSException
	{
		// this.type = type;
		cvListeners = new Vector();
		try
		{
			// Set up the barcode scanner
			CommPortIdentifier laCommPort =
				CommPortIdentifier.getPortIdentifier(
					BARCODE_SCANNER_PORTNAME);
			caSerialPort =
				(SerialPort) laCommPort.open(
					STR_BARCODE_SCANNER,
					TIMEOUT_TIME);
			caInputStream =
				new BufferedInputStream(caSerialPort.getInputStream());
			caSerialPort.setInputBufferSize(512000);
			System.out.println(
				STR_BUFFER_SIZE + caSerialPort.getInputBufferSize());
			caSerialPort.addEventListener(this);
			caSerialPort.notifyOnDataAvailable(true);
			caSerialPort.setSerialPortParams(
				BAUD_RATE,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
			caBarCodeScanner = this;
		}
		catch (NoSuchPortException aeNSPEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeNSPEx);
		}
		catch (PortInUseException aePIUEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aePIUEx);
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}
		catch (java.util.TooManyListenersException aeTMLEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeTMLEx);
		}
		catch (UnsupportedCommOperationException aeUCOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeUCOEx);
		}
	}

	/**
	 * Adds a listener for BarCodeEvents
	 * 
	 * @param aaBarCodeListener BarCodeListener
	 */
	public void addBarCodeListener(BarCodeListener aaBarCodeListener)
	{
		if (!cvListeners.contains(aaBarCodeListener))
		{
			cvListeners.add(aaBarCodeListener);
		}
	}

	/**
	 * Alter Scan Queue
	 *  
	 * @param aaEvent Object (SerialPortEvent)
	 * @param abClear
	 * @return Object
	 */
	private Object alterScanQueue(Object aaEvent, boolean abClear)
	{
		Object laResult = null;

		//  If event is not null, add event to end of the Scan Queue.
		if (aaEvent != null)
		{
			synchronized (this)
			{
				cvScanQueue.add(aaEvent);
				notify();
			}
		}
		//  otherwise, remove the first element from the Scan Queue
		//  and return it.
		else
		{
			if (abClear)
			{
				synchronized (this)
				{
					cvScanQueue.removeAllElements();
					cbAcceptingScans = true;
					notify();
				}
			}
			else
			{
				synchronized (this)
				{
					if (cvScanQueue.size() > 0)
					{
						laResult = cvScanQueue.elementAt(0);
						cvScanQueue.removeElementAt(0);
					}
				}
			}
		}
		return laResult;
	}

	/**
	 * Clear Scan Queue
	 */
	public static void clearScanQueue()
	{
		cbAcceptingScans = false;
		caBarCodeScanner.alterScanQueue(null, true);
	}

	/**
	 * Closes the BarCodeScanner port
	 */
	public void close()
	{
		try
		{
			caInputStream.close();
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}
		caSerialPort.close();
	}

	/**
	 * Closes all the open streams and ports and deallocates memory.
	 */
	protected void finalize()
	{
		cvListeners.removeAllElements();
		cvListeners = null;
		caInputStream = null;
		caSerialPort.close();
		caSerialPort = null;
	}

	/**
	 * Barcode data from scan.
	 *  
	 * @return String
	 */
	public String getRawBarcodeData()
	{
		return csRawBarcodeData;
	}

	/**
	 * Test runs the barcode scanner
	 * 
	 * @param aaArgs String[]
	 */
	public static void main(String[] aaArgs)
	{
		try
		{
			BarCodeScanner laBarCodeScanner = new BarCodeScanner();
		}
		catch (RTSException aeRTSEx)
		{
			// empty code block
		}
	}

	/**
	 * Parses the barcode string into a PlateBarCodeData object.
	 * 
	 * @param asBarCodeString String
	 * @return Object 
	 */
	private Object makePlateData(String asBarCodeString)
	{
		PlateBarCodeData laPlateBarCodeData = new PlateBarCodeData();
		try
		{
			String laDataString = new String(asBarCodeString);

			//  Retain Type and Version for barcode scan
			laPlateBarCodeData.setType(
				laDataString.substring(TYPE_START, VERSION_START));

			laPlateBarCodeData.setVersion(
				laDataString.substring(
					PLATE_VERSION_START,
					PLATE_VERSION_END));
			laPlateBarCodeData.setItemCd(
				laDataString.substring(
					PLATE_VERSION_END,
					PLATE_ITEMCD_END));
			if (laDataString
				.substring(PLATE_ITEMCD_END, PLATE_ITEMYR_END)
				.trim()
				.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				laPlateBarCodeData.setItemYr(CommonConstant.STR_ZERO);
			}
			else
			{
				laPlateBarCodeData.setItemYr(
					laDataString.substring(
						PLATE_ITEMCD_END,
						PLATE_ITEMYR_END));
			}
			laPlateBarCodeData.setItemNo(
				laDataString.substring(
					PLATE_ITEMYR_END,
					PLATE_ITEMNO_END));
			csTypeOfScan = CommonConstant.STR_SPACE_EMPTY;
			return laPlateBarCodeData;
		}
		catch (Exception aeEx)
		{
			try
			{
				caInputStream.close();
				caInputStream =
					new BufferedInputStream(
						caSerialPort.getInputStream());
			}
			catch (IOException aeIOEx)
			{
				return aeIOEx;
			}
			return aeEx;
		}
	}

	/**
	 * Parses the barcode string into a RenewalBarCodeData object.
	 * 
	 * @param asBarCodeString String
	 * @return Object
	 */
	private Object makeRenewalData(String asBarCodeString)
	{
		RenewalBarCodeData laRenewalBarCodeData =
			new RenewalBarCodeData();
		try
		{
			String laDataString = new String(asBarCodeString);
			//  Retain Type and Version for barcode scan
			laRenewalBarCodeData.setType(
				laDataString.substring(TYPE_START, VERSION_START));
			laRenewalBarCodeData.setVersion(
				laDataString.substring(VERSION_START, VERSION_END));
			laRenewalBarCodeData.setDocNo(
				laDataString.substring(
					RENEWAL_DOCNO_START,
					RENEWAL_DOCNO_END));
			laRenewalBarCodeData.setVin(
				laDataString.substring(
					RENEWAL_DOCNO_END,
					RENEWAL_VIN_END));
			laRenewalBarCodeData.setAuditTrailTransId(
				laDataString.substring(
					RENEWAL_VIN_END,
					RENEWAL_AUDIT_END));
			laRenewalBarCodeData.setRegPltCd(
				laDataString.substring(
					RENEWAL_AUDIT_END,
					RENEWAL_REGPLTCD_END));
			laRenewalBarCodeData.setRegStkrCd(
				laDataString.substring(
					RENEWAL_REGPLTCD_END,
					RENEWAL_STKRCD_END));
			laRenewalBarCodeData.setRegPltNo(
				laDataString.substring(
					RENEWAL_STKRCD_END,
					RENEWAL_REGPLTNO_END));
			laRenewalBarCodeData.setRegExpMo(
				laDataString.substring(
					RENEWAL_REGPLTNO_END,
					RENEWAL_EXPMO_END));
			laRenewalBarCodeData.setRegExpYr(
				laDataString.substring(
					RENEWAL_EXPMO_END,
					RENEWAL_EXPYR_END));
			laRenewalBarCodeData.setCntyNo(
				laDataString.substring(
					RENEWAL_EXPYR_END,
					RENEWAL_CNTYNO_END));

			// AcctItmCd (1-15) 		
			laRenewalBarCodeData.setAcctItmCd1(
				laDataString.substring(
					RENEWAL_CNTYNO_END,
					RENEWAL_ACCT1_END));
			laRenewalBarCodeData.setAcctItmCd2(
				laDataString.substring(
					RENEWAL_ACCT1_END,
					RENEWAL_ACCT2_END));
			laRenewalBarCodeData.setAcctItmCd3(
				laDataString.substring(
					RENEWAL_ACCT2_END,
					RENEWAL_ACCT3_END));
			laRenewalBarCodeData.setAcctItmCd4(
				laDataString.substring(
					RENEWAL_ACCT3_END,
					RENEWAL_ACCT4_END));
			laRenewalBarCodeData.setAcctItmCd5(
				laDataString.substring(
					RENEWAL_ACCT4_END,
					RENEWAL_ACCT5_END));
			laRenewalBarCodeData.setAcctItmCd6(
				laDataString.substring(
					RENEWAL_ACCT5_END,
					RENEWAL_ACCT6_END));
			laRenewalBarCodeData.setAcctItmCd7(
				laDataString.substring(
					RENEWAL_ACCT6_END,
					RENEWAL_ACCT7_END));
			laRenewalBarCodeData.setAcctItmCd8(
				laDataString.substring(
					RENEWAL_ACCT7_END,
					RENEWAL_ACCT8_END));
			laRenewalBarCodeData.setAcctItmCd9(
				laDataString.substring(
					RENEWAL_ACCT8_END,
					RENEWAL_ACCT9_END));
			laRenewalBarCodeData.setAcctItmCd10(
				laDataString.substring(
					RENEWAL_ACCT9_END,
					RENEWAL_ACCT10_END));
			laRenewalBarCodeData.setAcctItmCd11(
				laDataString.substring(
					RENEWAL_ACCT10_END,
					RENEWAL_ACCT11_END));
			laRenewalBarCodeData.setAcctItmCd12(
				laDataString.substring(
					RENEWAL_ACCT11_END,
					RENEWAL_ACCT12_END));
			laRenewalBarCodeData.setAcctItmCd13(
				laDataString.substring(
					RENEWAL_ACCT12_END,
					RENEWAL_ACCT13_END));
			laRenewalBarCodeData.setAcctItmCd14(
				laDataString.substring(
					RENEWAL_ACCT13_END,
					RENEWAL_ACCT14_END));
			laRenewalBarCodeData.setAcctItmCd15(
				laDataString.substring(
					RENEWAL_ACCT14_END,
					RENEWAL_ACCT15_END));

			// ItmPrice (1-15) 
			laRenewalBarCodeData.setItmPrice1(
				new Dollar(
					laDataString.substring(
						RENEWAL_ACCT15_END,
						RENEWAL_PRICE1_END)));
			laRenewalBarCodeData.setItmPrice2(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE1_END,
						RENEWAL_PRICE2_END)));
			laRenewalBarCodeData.setItmPrice3(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE2_END,
						RENEWAL_PRICE3_END)));
			laRenewalBarCodeData.setItmPrice4(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE3_END,
						RENEWAL_PRICE4_END)));
			laRenewalBarCodeData.setItmPrice5(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE4_END,
						RENEWAL_PRICE5_END)));
			laRenewalBarCodeData.setItmPrice6(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE5_END,
						RENEWAL_PRICE6_END)));
			laRenewalBarCodeData.setItmPrice7(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE6_END,
						RENEWAL_PRICE7_END)));

			laRenewalBarCodeData.setItmPrice8(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE7_END,
						RENEWAL_PRICE8_END)));
			laRenewalBarCodeData.setItmPrice9(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE8_END,
						RENEWAL_PRICE9_END)));
			laRenewalBarCodeData.setItmPrice10(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE9_END,
						RENEWAL_PRICE10_END)));
			laRenewalBarCodeData.setItmPrice11(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE10_END,
						RENEWAL_PRICE11_END)));
			laRenewalBarCodeData.setItmPrice12(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE11_END,
						RENEWAL_PRICE12_END)));
			laRenewalBarCodeData.setItmPrice13(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE12_END,
						RENEWAL_PRICE13_END)));
			laRenewalBarCodeData.setItmPrice14(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE13_END,
						RENEWAL_PRICE14_END)));
			laRenewalBarCodeData.setItmPrice15(
				new Dollar(
					laDataString.substring(
						RENEWAL_PRICE14_END,
						RENEWAL_PRICE15_END)));

			// RegClassCd 
			laRenewalBarCodeData.setRegClassCd(
				Integer.parseInt(
					laDataString.substring(
						RENEWAL_PRICE15_END,
						RENEWAL_REGCLASS_END)));

			// defect 9085 		
			// R05 - Add OrgNo, AddlSetIndi
			//       Return "" for OrgNo if "0000"  
			// defect 10303
			//if (laRenewalBarCodeData
			//	.getVersion()
			//	.equals(BARCODE_VERSION05))
			//{
				String lsOrgNo =
					laDataString.substring(
						RENEWAL_REGCLASS_END,
						RENEWAL_ORGNO_END);

				if (UtilityMethods.isAllZeros(lsOrgNo))
				{
					lsOrgNo = "";
				}
				laRenewalBarCodeData.setOrgNo(lsOrgNo);
				laRenewalBarCodeData.setAddlSetIndi(
					Integer.parseInt(
						laDataString.substring(
							RENEWAL_ORGNO_END,
							RENEWAL_ADDLSETINDI_END)));

				// NewPltReqdCd 
				laRenewalBarCodeData.setNewPltsReqdCd(
					laDataString.substring(
						RENEWAL_ADDLSETINDI_END,
						RENEWAL_NEWPLTSREQDCD_END));
			//}
			// end defect 10303
			// end defect 9085 

			// defect 10303
			if (laRenewalBarCodeData
				.getVersion()
				.equals(BARCODE_VERSION06))
			{
				laRenewalBarCodeData.setRegNextExpMo(
					laDataString.substring(
						RENEWAL_NEWPLTSREQDCD_END,
						RENEWAL_NEXTEXPMO_END));
				laRenewalBarCodeData.setRegNextExpYr(
					laDataString.substring(
						RENEWAL_NEXTEXPMO_END,
						RENEWAL_NEXTEXPYR_END));
				laRenewalBarCodeData.setPltValidityTerm(
					laDataString.substring(
						RENEWAL_NEXTEXPYR_END,
						RENEWAL_PLTVLDTYTERM_END));
				laRenewalBarCodeData.setPltExpMo(
					laDataString.substring(
						RENEWAL_PLTVLDTYTERM_END,
						RENEWAL_PLTEXPMO_END));
				laRenewalBarCodeData.setPltExpYr(
					laDataString.substring(
						RENEWAL_PLTEXPMO_END,
						RENEWAL_PLTEXPYR_END));
				laRenewalBarCodeData.setPltNextExpMo(
					laDataString.substring(
						RENEWAL_PLTEXPYR_END,
						RENEWAL_PLTNEXTEXPMO_END));
				laRenewalBarCodeData.setPltNextExpYr(
					laDataString.substring(
						RENEWAL_PLTNEXTEXPMO_END,
						RENEWAL_PLTNEXTEXPYR_END));
			}
			// end defect 10303

			csTypeOfScan = CommonConstant.STR_SPACE_EMPTY;
			if (laRenewalBarCodeData
				.getRegExpMo()
				.trim()
				.equals(CommonConstant.STR_SPACE_EMPTY)
				|| laRenewalBarCodeData.getRegExpMo().trim().equals(
					CommonConstant.STR_ZERO))
			{
				caInputStream.close();
				caInputStream =
					new BufferedInputStream(
						caSerialPort.getInputStream());
				asBarCodeString = asBarCodeString.substring(1);
				return makeRenewalData(asBarCodeString);
			}
			return laRenewalBarCodeData;
		}
		catch (Exception aeEx)
		{
			try
			{
				caInputStream.close();
				caInputStream =
					new BufferedInputStream(
						caSerialPort.getInputStream());
			}
			catch (IOException aeIOEx)
			{
				return new RTSException();
			}
			return new RTSException();
		}
	}
	/**
	 * Parses the barcode string into a StickerBarCodeData object.
	 * 
	 * @param asBarCodeString String
	 * @return Object
	 */
	private Object makeStickerData(String asBarCodeString)
	{
		StickerBarCodeData laStickerBarCodeData =
			new StickerBarCodeData();
		try
		{
			String laDataString = new String(asBarCodeString);
			laStickerBarCodeData.setVersion(
				laDataString.substring(
					STICKER_VERSION_START,
					STICKER_VERSION_END));
			laStickerBarCodeData.setItemCd(
				laDataString.substring(
					STICKER_VERSION_END,
					STICKER_ITEMCD_END));
			if (laDataString
				.substring(STICKER_ITEMCD_END, STICKER_ITEMYR_END)
				.trim()
				.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				laStickerBarCodeData.setItemYr(CommonConstant.STR_ZERO);
			}
			else
			{
				laStickerBarCodeData.setItemYr(
					laDataString.substring(
						STICKER_ITEMCD_END,
						STICKER_ITEMYR_END));
			}
			laStickerBarCodeData.setItemNo(
				laDataString.substring(
					STICKER_ITEMYR_END,
					STICKER_ITEMNO_END));
			csTypeOfScan = CommonConstant.STR_SPACE_EMPTY;
			return laStickerBarCodeData;
		}
		catch (Exception aeEx)
		{
			try
			{
				caInputStream.close();
				caInputStream =
					new BufferedInputStream(
						caSerialPort.getInputStream());
			}
			catch (IOException aeIOEx)
			{
				return aeIOEx;
			}
			return aeEx;
		}
	}

	/**
	 * Parses barcode string into a StickerPrintingBarCodeData object.
	 * 
	 * @param asBarCodeString String
	 * @return Object
	 */
	private Object makeStickerPrinterData(String asBarCodeString)
	{
		StickerPrintingBarCodeData laStickerPrintingBarCodeData =
			new StickerPrintingBarCodeData();
		try
		{
			// Save Type and Version
			String laDataString = new String(asBarCodeString);
			laStickerPrintingBarCodeData.setType(
				laDataString.substring(TYPE_START, VERSION_START));
			laStickerPrintingBarCodeData.setVersion(
				laDataString.substring(VERSION_START, VERSION_END));

			// Save Information
			laStickerPrintingBarCodeData.setDocNo(
				laDataString.substring(
					STKRPRNT_DOCNO_START,
					STKRPRNT_DOCNO_END));
			laStickerPrintingBarCodeData.setVIN(
				laDataString.substring(
					STKRPRNT_DOCNO_END,
					STKRPRNT_VIN_END));
			laStickerPrintingBarCodeData.setResCompCntyNo(
				laDataString.substring(
					STKRPRNT_VIN_END,
					STKRPRNT_RESCOMPCNTYNO_END));
			laStickerPrintingBarCodeData.setWorkStatId(
				laDataString.substring(
					STKRPRNT_RESCOMPCNTYNO_END,
					STKRPRNT_WSID_END));
			laStickerPrintingBarCodeData.setPrntCntyNo(
				laDataString.substring(
					STKRPRNT_WSID_END,
					STKRPRNT_PRTCNTYNO_END));
			laStickerPrintingBarCodeData.setStkrPrntDate(
				laDataString.substring(
					STKRPRNT_PRTCNTYNO_END,
					STKRPRNT_STKRPRTDATE_END));
			laStickerPrintingBarCodeData.setRegisPltCd(
				laDataString.substring(
					STKRPRNT_STKRPRTDATE_END,
					STKRPRNT_REGPLTCD_END));
			laStickerPrintingBarCodeData.setRegisStkrCd(
				laDataString.substring(
					STKRPRNT_REGPLTCD_END,
					STKRPRNT_REGSTKRCD_END));
			laStickerPrintingBarCodeData.setRegPltNo(
				laDataString.substring(
					STKRPRNT_REGSTKRCD_END,
					STKRPRNT_REGPLTNO_END));
			laStickerPrintingBarCodeData.setRegExpDate(
				laDataString.substring(
					STKRPRNT_REGPLTNO_END,
					STKRPRNT_REGEXPDATE_END));
			laStickerPrintingBarCodeData.setRegClassCd(
				laDataString.substring(
					STKRPRNT_REGEXPDATE_END,
					STKRPRNT_REGCLASS_END));
			csTypeOfScan = CommonConstant.STR_SPACE_EMPTY;
		}
		catch (Exception aeEx)
		{
			try
			{
				caInputStream.close();
				caInputStream =
					new BufferedInputStream(
						caSerialPort.getInputStream());
			}
			catch (IOException aeIOEx)
			{
				return new RTSException();
			}
		}
		return laStickerPrintingBarCodeData;
	}
	/**
	 * Removes a listener for BarCodeEvents
	 * 
	 * @param aaBarCodeListener BarCodeListener
	 */
	public void removeBarCodeListener(BarCodeListener aaBarCodeListener)
	{
		cvListeners.remove(aaBarCodeListener);
	}

	/**
	 * Thread that reads barcode.
	 */
	public void run()
	{
		System.out.println(MSG_THREAD_STARTED);
		while (true)
		{
			if (cbAcceptingScans)
			{
				Object laEventData = alterScanQueue(null, false);
				if (laEventData == null)
				{
					System.out.println(MSG_WAITING);
					synchronized (this)
					{
						try
						{
							wait();
						}
						catch (InterruptedException aeIEx)
						{
							// empty code block
						}
					}
				}
				else
				{
					System.out.println(
						MSG_DISPATCHING
							+ laEventData.getClass().getName());
					int liSize = cvListeners.size();
					int i = 0;
					while (i < liSize)
					{
						(
							(BarCodeListener) cvListeners.get(
								i)).barCodeScanned(
							new BarCodeEvent(laEventData));
						i++;
					}
				}
			}
			else
			{
				System.out.println(MSG_SUSPENDED);
				synchronized (this)
				{
					try
					{
						wait();
					}
					catch (InterruptedException aeIEx)
					{
						// empty code block
					}
				}
			}
		}
	}

	/**
	 * Captures input from the Serial Port.
	 * 
	 * @param aaSerialPortEvent SerialPortEvent
	 */
	public void serialEvent(SerialPortEvent aaSerialPortEvent)
	{
		int liLength = 0;
		int liType = 0;
		String lsBarCodeString = null;
		Object laEventData = null;
		if (aaSerialPortEvent.getEventType()
			== SerialPortEvent.DATA_AVAILABLE)
		{
			try
			{
				// Create a buffer to read in the data
				byte[] laBuffer = new byte[BUFFER_SIZE];
				// Read in the first byte to determine what type of 
				// barcode was just read
				// defect 7348
				// Next two bytes to determine what version of barcode 
				// was just read
				if (caInputStream.available() > 1
					&& csTypeOfScan.equals(
						CommonConstant.STR_SPACE_EMPTY))
				{
					//byte[] marker = new byte[1];
					byte[] laMarker = new byte[3];
					caInputStream.read(laMarker);
					csTypeOfScan = new String(laMarker, 0, 1);
					csBarCodeVersion = new String(laMarker, 1, 2);
				}
				if (csTypeOfScan.equals(RENEWAL_STRING))
				{
					int liTempLength = 0;

					// These lengths are 3 bytes less than 'real' barcode length
					// because of type and version (ie: R04) has been read\removed
					// defect 10303
					if (csBarCodeVersion.equals(BARCODE_VERSION06))
					{
						// Real Length = 345
						liTempLength = R06_REMAINING_LENGTH;
					}
					// end defect 10303
					else if (csBarCodeVersion.equals(BARCODE_VERSION05))
					{
						// Real Length = 325
						liTempLength = R05_REMAINING_LENGTH;
					}
					// defect 10303
					//else if (
					//	csBarCodeVersion.equals(BARCODE_VERSION04))
					//{
					//	// Real Length = 319
					//	liTempLength = R04_REMAINING_LENGTH;
					//}
					// end defect 10303
					liLength = liTempLength;
					liType = RENEWAL;
				}
				else if (csTypeOfScan.equals(PLATE_STRING))
				{
					liLength = PLATE_LENGTH;
					liType = PLATE;
				}
				else if (csTypeOfScan.equals(STICKER_STRING))
				{
					liLength = STICKER_LENGTH;
					liType = STICKER;
				}
				// defect 7108
				// Test for Sticker Printing Barcode Type
				else if (csTypeOfScan.equals(STKRPRNT_STRING))
				{
					liLength = STKRPRNT_LENGTH;
					liType = STKRPRNT;
				}
				// end defect 7108
				else
				{
					liLength = 0;
					liType = ERROR;
				}
				// If a partial barcode was read, return so the buffer 
				// will be filled with the entire barcode information
				if (caInputStream.available() < liLength)
				{
					return;
				}
				// read in the barcode information
				while (caInputStream.available() > 0)
				{
					caInputStream.read(laBuffer);
					break;
				}
				lsBarCodeString = new String(laBuffer, 0, liLength);

				// defect 8512
				// An unknown variable has caused this method to be 
				// called an extra time and the caInputStream only has 
				// one byte in it.  This byte is Character 10 or 
				// New Line.  If we run into this situation we need to 
				// return.
				if (laBuffer[0] == 10)
				{
					System.out.println(REMOVED_CHAR_10);
					return;
				}
				// end defect 8512

				lsBarCodeString =
					csTypeOfScan + csBarCodeVersion + lsBarCodeString;
				setRawBarcodeData(lsBarCodeString);

				// determine the type of barcode, and then create the dataobject
				switch (liType)
				{
					case PLATE :
						{
							laEventData =
								makePlateData(lsBarCodeString);
							break;
						}
					case RENEWAL :
						{
							laEventData =
								makeRenewalData(lsBarCodeString);
							break;
						}
					case STICKER :
						{
							laEventData =
								makeStickerData(lsBarCodeString);
							break;
						}
						// defect 7108
						// Copy barcode data to data object
					case STKRPRNT :
						{
							laEventData =
								makeStickerPrinterData(lsBarCodeString);
							break;
						}
						// end defect 7108
					default :
						{
							System.out.println(MSG_ERR_PROCESSED);
							laEventData = new RTSException();
							csTypeOfScan =
								CommonConstant.STR_SPACE_EMPTY;
							break;
						}
				}
				Object laBarCodeData = laEventData;
				if (laBarCodeData instanceof RenewalBarCodeData)
				{
					RenewalBarCodeData laRenewalBarCodeData =
						(RenewalBarCodeData) laBarCodeData;
					System.out.println(
						STR_PLATE + laRenewalBarCodeData.getRegPltNo());
				}
				alterScanQueue(laEventData, false);
			}
			catch (IOException aeIOEx)
			{
				RTSException leRTSEx =
					new RTSException(RTSException.JAVA_ERROR, aeIOEx);
				leRTSEx.displayError((javax.swing.JFrame) null);
			}
		}
	}

	/**
	 * Barcode data from scan.
	 *  
	 * @param asNewBarcodeRaw String
	 */
	public void setRawBarcodeData(String asNewBarcodeRaw)
	{
		csRawBarcodeData = asNewBarcodeRaw;
	}

}
