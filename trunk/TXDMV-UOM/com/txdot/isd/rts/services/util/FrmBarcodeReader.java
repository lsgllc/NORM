package com.txdot.isd.rts.services.util;

import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.RTSButton;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.OrganizationNumberCache;
import com.txdot.isd.rts.services.data.PlateBarCodeData;
import com.txdot.isd.rts.services.data.RenewalBarCodeData;
import com.txdot.isd.rts.services.data.StickerPrintingBarCodeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.event.BarCodeListener;
import com.txdot.isd.rts.services.util.event.CommListener;
import com.txdot.isd.rts.services.util.event.ThreadListener;

/*
 *
 * FrmBarcodeReader.java
 *
 * (c) Texas Department of Transportation 2004 
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		06/08/2004	Barcode utility to read Renewal and 
 *							Printed Sticker barcodes.
 *							defect 7108 Ver 5.2.1
 * J Rue		06/18/2004	Not printing raw barcode data when 
 *							RTSException is 	caught.
 *							add addRTSExceptBarCodeDataToDialog(),
 *							    setRenewalExceptData()
 *							defect 7108 Ver 5.2.1
 * J Rue		06/23/2004	Add Plate data to Barcode Reader
 *							add addPlateBarCodeDataToDialog(),
 *							    turnOnOffPlate()
 *							defect 7108 Ver 5.2.1
 * B Hargrove	09/15/2004	Changes to allow for all 15 acct items and
 *							fees for Version 4 Barcode (previously was
 *							only 7 acct items and fees).
 *							add fields in VCE; generate required methods
 *							add and\or modify Class constants and vars
 *							modify addRTSExceptBarCodeDataToDialog(),
 *							clearcStkrPrntBarCodeData(), clearData(),
 *							clearFields(), createTestData(),
 *							getUnformattedItmpriceData(),
 *							handleRenwlBarCode(), setRenewalExceptData(),
 *							turnOnOffrAcctItmFees()
 *							defect 7293 Ver 5.2.1
 * B Hargrove	09/16/2004	Added code to handle Plate barcode scans.
 *							This code had been written earlier by Jeff
 *							Rue but somehow was lost over the summer.
 *							add addPlateBarCodedata(),
 *							addPlateBarCodeDataToDialog(),
 *							modify frame to add Plate fields
 *							defect 7293 Ver 5.2.1
 * K Harrell	04/13/2007	Update for Barcode Version 5.  Remove 
 * 							checks for Version 03.
 * 							Further cleanup planned. 
 * 				  			defect 9085 Ver Special Plates  
 * J Rue		10/03/2007	Add total fees
 * 							add ivjlblTotalFees, ivjtxtTotalFees,
 * 							  getstcLblTotalFees(), gettxtTotalFees(),
 * 							  gettxtTotalFees(), getstcLblTotalFees()
 * 							modify addRewlBarCodeDataToDialog(), 
 * 							  clearFields(), 
 * 							  getFrmBarcodeReaderContentPane1(),
 * 							  turnOnOffAcctItmFees()
 * 							defect 9086 Ver Renewal Notices
 * J Rue		10/03/2007	Add NewPlatesRequiredCode
 * 							add ivjlblNewPlatesRequiredCode,
 * 							  ivjtxtNewPlatesRequiredCode,
 * 							  getstcLblNewPlatesRequiredCode(), 
 * 							  gettxtNewPlatesRequiredCode(),
 * 							modify addRewlBarCodeDataToDialog(), 
 * 							  clearFields(), 
 * 							  getFrmBarcodeReaderContentPane1(),
 * 							  turnOnOffAcctItmFees()
 * 							defect 9086 Ver Renewal Notices
 * T Pederson	12/18/2009	Update for Barcode Version 6.
 * 				  			defect 10303 Ver Defect_POS_H  
 * K Harrell	06/20/2010	Handle new Barcode Fields regarding 
 * 							visibility. (added in 9086/10303)   
 * 							Further cleanup remains. 
 * 							add TurnOnOffNewRenwlFlds() 
 * 							modify turnOffOnStkrRenwl(), 
 * 							  turnOnOffPrntCntyNo()
 * 							defect 10464 Ver 6.5.0  
 * K Harrell	07/05/2010	additional cleanup; more remains - unused 
 * 							code, etc.  
 * 							defect 10464 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */
/**
 * This class provides methods for reading and display multiple
 *		types on barcodes.
 *  
 * @version	6.5.0 			07/05/2010
 * @author 	Jeff Rue
 * <br>Creation Date:		06/08/2004 
 */
public class FrmBarcodeReader
	extends JFrame
	implements
		BarCodeListener,
		CommListener,
		ThreadListener,
		ActionListener
		//,FocusListener,
		//ItemListener
{
	//	RTSButton
	private RTSButton ivjbtnClear = null;
	private RTSButton ivjbtnClose = null;

	// JPanel
	private JPanel ivjbtnPanel = null;
	private JPanel ivjFrmBarcodeReaderContentPane1 = null;
	private JLabel ivjlblTotalFees = null;
	private JLabel ivjstcLblAcctItmCd1 = null;
	private JLabel ivjstcLblAcctItmCd10 = null;
	private JLabel ivjstcLblAcctItmCd11 = null;
	private JLabel ivjstcLblAcctItmCd12 = null;
	private JLabel ivjstcLblAcctItmCd13 = null;
	private JLabel ivjstcLblAcctItmCd14 = null;
	private JLabel ivjstcLblAcctItmCd15 = null;
	private JLabel ivjstcLblAcctItmCd2 = null;
	private JLabel ivjstcLblAcctItmCd3 = null;
	private JLabel ivjstcLblAcctItmCd4 = null;
	private JLabel ivjstcLblAcctItmCd5 = null;
	private JLabel ivjstcLblAcctItmCd6 = null;
	private JLabel ivjstcLblAcctItmCd7 = null;
	private JLabel ivjstcLblAcctItmCd8 = null;
	private JLabel ivjstcLblAcctItmCd9 = null;
	private JLabel ivjstcLblAddlSetIndi = null;
	private JLabel ivjstcLblAuditTrailTransId = null;
	private JLabel ivjstcLblBarcodeRawHeader = null;
	private JLabel ivjstcLblBkSlash = null;
	private JLabel ivjstcLblBkSlash1 = null;
	private JLabel ivjstcLblBkSlash2 = null;
	private JLabel ivjstcLblBkSlash3 = null;
	private JLabel ivjstcLblCntyNo = null;
	private JLabel ivjstcLblDocNo = null;
	private JLabel ivjstcLblHeader = null;
	private JLabel ivjstcLblNewPlatesRequiredCode = null;
	private JLabel ivjstcLblNextPltExpMoYr = null;
	private JLabel ivjstcLblNextRegExpMoYr = null;
	private JLabel ivjstcLblOrgNoOrgName = null;
	private JLabel ivjstcLblPlateItemYr = null;
	private JLabel ivjstcLblPlateRegPltCd = null;
	private JLabel ivjstcLblPlateStkrNo = null;
	private JLabel ivjstcLblPlateValidityTerm = null;
	private JLabel ivjstcLblPltExpMoYr = null;
	private JLabel ivjstcLblPrntCntyNo = null;
	private JLabel ivjstcLblPrntRegClassCd = null;
	private JLabel ivjstcLblPrntRegPltCd = null;
	private JLabel ivjstcLblPrntRegPltNo = null;
	private JLabel ivjstcLblPrntRegStkrCd = null;
	private JLabel ivjstcLblRegClassCd = null;
	private JLabel ivjstcLblRegExpDate = null;
	private JLabel ivjstcLblRegExpMoYr = null;
	private JLabel ivjstcLblRegPltCd = null;
	private JLabel ivjstcLblRegPltNo = null;
	private JLabel ivjstcLblRegStkrCd = null;
	private JLabel ivjstcLblResCompCntyNo = null;
	private JLabel ivjstcLblStkrPrntDate = null;
	private JLabel ivjstcLblTotalFees = null;
	private JLabel ivjstcLblType = null;
	private JLabel ivjstcLblTypeOfBarCode = null;
	private JLabel ivjstcLblVersion = null;
	private JLabel ivjstcLblVIN = null;
	private JLabel ivjstcLblWsId = null;
	private JTextField ivjtxtAcctItmCd1 = null;
	private JTextField ivjtxtAcctItmCd10 = null;
	private JTextField ivjtxtAcctItmCd11 = null;
	private JTextField ivjtxtAcctItmCd12 = null;
	private JTextField ivjtxtAcctItmCd13 = null;
	private JTextField ivjtxtAcctItmCd14 = null;
	private JTextField ivjtxtAcctItmCd15 = null;
	private JTextField ivjtxtAcctItmCd2 = null;
	private JTextField ivjtxtAcctItmCd3 = null;
	private JTextField ivjtxtAcctItmCd4 = null;
	private JTextField ivjtxtAcctItmCd5 = null;
	private JTextField ivjtxtAcctItmCd6 = null;
	private JTextField ivjtxtAcctItmCd7 = null;
	private JTextField ivjtxtAcctItmCd8 = null;
	private JTextField ivjtxtAcctItmCd9 = null;
	private JTextField ivjtxtAddlSetIndi = null;
	private JTextField ivjtxtAuditTrailCd = null;
	private JTextArea ivjtxtABarcodeRaw = null;
	private JTextField ivjtxtBarcodeType = null;
	private JTextField ivjtxtBarcodeVersion = null;
	private JTextField ivjtxtCntyNo = null;
	private JTextField ivjtxtDocType = null;
	private JTextField ivjtxtItmPrice1 = null;
	private JTextField ivjtxtItmPrice10 = null;
	private JTextField ivjtxtItmPrice11 = null;
	private JTextField ivjtxtItmPrice12 = null;
	private JTextField ivjtxtItmPrice13 = null;
	private JTextField ivjtxtItmPrice14 = null;
	private JTextField ivjtxtItmPrice15 = null;
	private JTextField ivjtxtItmPrice2 = null;
	private JTextField ivjtxtItmPrice3 = null;
	private JTextField ivjtxtItmPrice4 = null;
	private JTextField ivjtxtItmPrice5 = null;
	private JTextField ivjtxtItmPrice6 = null;
	private JTextField ivjtxtItmPrice7 = null;
	private JTextField ivjtxtItmPrice8 = null;
	private JTextField ivjtxtItmPrice9 = null;
	private JTextField ivjtxtNewPlatesRequiredCode = null;
	private JTextField ivjtxtNextPltExpMo = null;
	private JTextField ivjtxtNextPltExpYr = null;
	private JTextField ivjtxtNextRegExpMo = null;
	private JTextField ivjtxtNextRegExpYr = null;
	private JTextField ivjtxtOrgName = null;
	private JTextField ivjtxtOrgNo = null;
	private JTextField ivjtxtPlateItemYr = null;
	private JTextField ivjtxtPlateRegPltCd = null;
	private JTextField ivjtxtPlateStkrNo = null;
	private JTextField ivjtxtPlateValidityTerm = null;
	private JTextField ivjtxtPltExpMo = null;
	private JTextField ivjtxtPltExpYr = null;
	private JTextField ivjtxtPrntCntyNo = null;
	private JTextField ivjtxtPrntRegClassCd = null;
	private JTextField ivjtxtPrntRegPltCd = null;
	private JTextField ivjtxtPrntRegPltNo = null;
	private JTextField ivjtxtPrntRegStkrCd = null;
	private JTextField ivjtxtPrntWsId = null;
	private JTextField ivjtxtRegClassCd = null;
	private JTextField ivjtxtRegExpDate = null;
	private JTextField ivjtxtRegExpMo = null;
	private JTextField ivjtxtRegExpYr = null;
	private JTextField ivjtxtRegPltCd = null;
	private JTextField ivjtxtRegPltNo = null;
	private JTextField ivjtxtRegStkrCd = null;
	private JTextField ivjtxtResCompCntyNo = null;
	private JTextField ivjtxtStkrPrntDate = null;
	private JTextField ivjtxtVIN = null;


	//	RTSException
	private RTSException barcodeException;
	
	// Barcode Classes
	private BarCodeScanner barcodescanner;
	
	// String 
	public String barcodeType;
	public String barcodeVersion;

	private RenewalBarCodeData caRenewalBarCodeData =
		new RenewalBarCodeData();
	private int ciRnwlExceptErrFld = 0;

	//****  Plate Barcode  ****
	private PlateBarCodeData cPlateBarCodeData = new PlateBarCodeData();
	
	// Initialize raw data for Item Prices
	public String csItmPrice1 = "";
	public String csItmPrice10 = "";
	public String csItmPrice11 = "";
	public String csItmPrice12 = "";
	public String csItmPrice13 = "";
	public String csItmPrice14 = "";
	public String csItmPrice15 = "";
	public String csItmPrice2 = "";
	public String csItmPrice3 = "";
	public String csItmPrice4 = "";
	public String csItmPrice5 = "";
	public String csItmPrice6 = "";
	public String csItmPrice7 = "";
	public String csItmPrice8 = "";
	public String csItmPrice9 = "";

	private StickerPrintingBarCodeData cStkrPrntBarCodeData =
		new StickerPrintingBarCodeData();
	
	// Initialize variables
	private java.util.Vector renewalQueue = new Vector();
	
	private Thread threadBarCode;
	private final static int PLATE_ITEMCD_END = 11;
	private final static int PLATE_ITEMNO_END = 23;
	private final static int PLATE_ITEMYR_END = 15;
	private final static int PLATE_VERSION_END = 3;
	private final static int PLATE_VERSION_START = 1;
	
	private final static String PLTBARCODE = "PLATE BARCODE";
	private final static String RENEWAL = "    RENEWAL";

	private final static String RTSEXCEPTION =
		"!!! SCAN DATA ERROR !!!";
	private final static String SCANERROR =
		"Barcode data is not captured properly. Please check your work and continue scanning!";
	private final static String SCANREADY = "READY TO SCAN";
	private final static String STKRPRNT = "STICKER PRINTING";
	private final static String UNKNOWNFORMAT = "UNKNOWN FORMAT";

	/**
	 * Starts the application.
	 * 
	 * @param args an array of command-line arguments
	 */
	public static void main(String[] args)
	{
		try
		{
			// Frame setup
			FrmBarcodeReader aFrmBarcodeReader;
			aFrmBarcodeReader = new FrmBarcodeReader();
			aFrmBarcodeReader
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});

			java.awt.Insets insets = aFrmBarcodeReader.getInsets();
			aFrmBarcodeReader.setSize(
				aFrmBarcodeReader.getWidth()
					+ insets.left
					+ insets.right,
				aFrmBarcodeReader.getHeight()
					+ insets.top
					+ insets.bottom);

			/***   Determine if serial port is 
					 in use by another application   ***/
			try
			{
				javax.comm.CommPortIdentifier commPort =
					javax.comm.CommPortIdentifier.getPortIdentifier(
						SystemProperty.getBarcodePort());
				javax.comm.SerialPort serialPort =
					(javax.comm.SerialPort) commPort.open(
						"Barcode Scanner",
						30);
				serialPort.close();

			}
			catch (javax.comm.NoSuchPortException e)
			{
				String lsErrMsgNoPort =
					" NoSuchPort, Verify port is available.";
				System.err.println(lsErrMsgNoPort);
				return;
			}
			CacheManager.loadCache();
			// Set data 
			aFrmBarcodeReader.setData(null);
			// Make frame visible
			aFrmBarcodeReader.setVisible(true);
		}
		catch (javax.comm.PortInUseException e)
		{
			String lsErrMsgPortInUse =
				" PortInUse, RTSII may have this port locked.";
			System.err.println(lsErrMsgPortInUse);
			return;
		}
		catch (Throwable exception)
		{
			System.err.println(
				"Exception occurred in main() of javax.swing.JFrame");
			exception.printStackTrace(System.out);
		}
	}

	/**
	 * BarcodeReader constructor comment.
	 */
	public FrmBarcodeReader()
	{
		super();
		initialize();
	}
	
	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(ActionEvent e)
	{
		// Close application
		if (e.getSource() == getbtnClose())
		{
			System.exit(0);
		}
		// Clear all display fields
		if (e.getSource() == getbtnClear())
		{
			clearFields();
			clearStkrPrntBarCodeData();
			getstcLblTypeOfBarCode().setText("");
		}
		// Determine if thread exist
		if (threadBarCode.isAlive())
		{
			System.out.println("Barcode thread active");
		}
	}
	
	/**
	 * Parses the barcode string into a PlateBarCodeData object.
	 * 
	 * @param barCodeString 
	 * @return Object 
	 */
	private Object addPlateBarCodedata(String barCodeString)
	{
		PlateBarCodeData data = new PlateBarCodeData();
		String dataString = new String(barCodeString);

		// Set Type of Scan
		getstcLblTypeOfBarCode().setText(PLTBARCODE);
		getstcLblTypeOfBarCode().repaint();

		// Set Bar Code raw data to text fields
		gettxtABarcodeRaw().setText(barcodescanner.getRawBarcodeData());
		gettxtABarcodeRaw().repaint();

		//	 Set Data for barcode type and version
		gettxtBarcodeType().setText(cPlateBarCodeData.getType());
		gettxtBarcodeVersion().setText(cPlateBarCodeData.getVersion());
		data.setVersion(
			dataString.substring(
				PLATE_VERSION_START,
				PLATE_VERSION_END));
		data.setItemCd(
			dataString.substring(PLATE_VERSION_END, PLATE_ITEMCD_END));
		if (dataString
			.substring(PLATE_ITEMCD_END, PLATE_ITEMYR_END)
			.trim()
			.equals(""))
			data.setItemYr("0");
		else
			data.setItemYr(
				dataString.substring(
					PLATE_ITEMCD_END,
					PLATE_ITEMYR_END));
		data.setItemNo(
			dataString.substring(PLATE_ITEMYR_END, PLATE_ITEMNO_END));
		return data;
	}

	/**
	 * Parses the barcode string into a PlateBarCodeData object.
	 * 
	 */
	private void addPlateBarCodeDataToDialog()
	{
		PlateBarCodeData data = new PlateBarCodeData();

		// Set Type of Scan
		getstcLblTypeOfBarCode().setText(PLTBARCODE);
		getstcLblTypeOfBarCode().repaint();

		// Set Bar Code raw data to text fields
		gettxtABarcodeRaw().setText(barcodescanner.getRawBarcodeData());
		gettxtABarcodeRaw().repaint();

		//	 Set Data for barcode type and version
		gettxtBarcodeType().setText(cPlateBarCodeData.getType());
		gettxtBarcodeVersion().setText(cPlateBarCodeData.getVersion());
		gettxtPlateRegPltCd().setText(cPlateBarCodeData.getItemCd());
		gettxtPlateStkrNo().setText(cPlateBarCodeData.getItemNo());
		gettxtPlateItemYr().setText(cPlateBarCodeData.getItemYr());

		// Turn on/off the appropriate fields	
		turnOffOnStkrRenwl(false);
		turnOnOffCommonFlds(false);
		turnOnOffPlate(true);
	}

	/**
	 * Parses the barcode string into a PlateBarCodeData object.
	 * 
	 * @param barCodeString 
	 * @return Object
	 */
	private Object addPlateBarCodeDataToDialog(String barCodeString)
	{
		PlateBarCodeData data = new PlateBarCodeData();
		String dataString = new String(barCodeString);

		// Set Type of Scan
		getstcLblTypeOfBarCode().setText(PLTBARCODE);
		getstcLblTypeOfBarCode().repaint();

		// Set Bar Code raw data to text fields
		gettxtABarcodeRaw().setText(barcodescanner.getRawBarcodeData());
		gettxtABarcodeRaw().repaint();

		//	 Set Data for barcode type and version
		gettxtBarcodeType().setText(cPlateBarCodeData.getType());
		gettxtBarcodeVersion().setText(cPlateBarCodeData.getVersion());
		data.setVersion(
			dataString.substring(
				PLATE_VERSION_START,
				PLATE_VERSION_END));
		data.setItemCd(
			dataString.substring(PLATE_VERSION_END, PLATE_ITEMCD_END));
		if (dataString
			.substring(PLATE_ITEMCD_END, PLATE_ITEMYR_END)
			.trim()
			.equals(""))
			data.setItemYr("0");
		else
			data.setItemYr(
				dataString.substring(
					PLATE_ITEMCD_END,
					PLATE_ITEMYR_END));
		data.setItemNo(
			dataString.substring(PLATE_ITEMYR_END, PLATE_ITEMNO_END));
		return data;
	}

	/**
	 * Populate display fields for Renewal barcode.
	 * 
	 */
	public void addRewlBarCodeDataToDialog()
	{
		// Set Type of Scan
		getstcLblTypeOfBarCode().setText(RENEWAL);
		getstcLblTypeOfBarCode().repaint();

		// Set Bar Code raw data to text fields
		gettxtABarcodeRaw().setText(barcodescanner.getRawBarcodeData());
		gettxtABarcodeRaw().repaint();

		//	 Set Data for barcode type and version
		gettxtBarcodeVersion().setText(
			caRenewalBarCodeData.getVersion());
		gettxtBarcodeType().setText(caRenewalBarCodeData.getType());

		// Set data for Account Item Codes
		gettxtAcctItmCd1().setText(
			caRenewalBarCodeData.getAcctItmCd1());
		gettxtAcctItmCd2().setText(
			caRenewalBarCodeData.getAcctItmCd2());
		gettxtAcctItmCd3().setText(
			caRenewalBarCodeData.getAcctItmCd3());
		gettxtAcctItmCd4().setText(
			caRenewalBarCodeData.getAcctItmCd4());
		gettxtAcctItmCd5().setText(
			caRenewalBarCodeData.getAcctItmCd5());
		gettxtAcctItmCd6().setText(
			caRenewalBarCodeData.getAcctItmCd6());
		gettxtAcctItmCd7().setText(
			caRenewalBarCodeData.getAcctItmCd7());
		gettxtAcctItmCd8().setText(
			caRenewalBarCodeData.getAcctItmCd8());
		gettxtAcctItmCd9().setText(
			caRenewalBarCodeData.getAcctItmCd9());
		gettxtAcctItmCd10().setText(
			caRenewalBarCodeData.getAcctItmCd10());
		gettxtAcctItmCd11().setText(
			caRenewalBarCodeData.getAcctItmCd11());
		gettxtAcctItmCd12().setText(
			caRenewalBarCodeData.getAcctItmCd12());
		gettxtAcctItmCd13().setText(
			caRenewalBarCodeData.getAcctItmCd13());
		gettxtAcctItmCd14().setText(
			caRenewalBarCodeData.getAcctItmCd14());
		gettxtAcctItmCd15().setText(
			caRenewalBarCodeData.getAcctItmCd15());
		// Set data for Item Price
		getUnformattedItmpriceData();
		gettxtItmPrice1().setText(csItmPrice1);
		gettxtItmPrice2().setText(csItmPrice2);
		gettxtItmPrice3().setText(csItmPrice3);
		gettxtItmPrice4().setText(csItmPrice4);
		gettxtItmPrice5().setText(csItmPrice5);
		gettxtItmPrice6().setText(csItmPrice6);
		gettxtItmPrice7().setText(csItmPrice7);
		gettxtItmPrice8().setText(csItmPrice8);
		gettxtItmPrice9().setText(csItmPrice9);
		gettxtItmPrice10().setText(csItmPrice10);
		gettxtItmPrice11().setText(csItmPrice11);
		gettxtItmPrice12().setText(csItmPrice12);
		gettxtItmPrice13().setText(csItmPrice13);
		gettxtItmPrice14().setText(csItmPrice14);
		gettxtItmPrice15().setText(csItmPrice15);
		// defect 8096
		gettxtNewPlatesRequiredCode().setText(
			caRenewalBarCodeData.getNewPltsReqdCd());
		// defect 9086
		try
		{
			totalFees(caRenewalBarCodeData);
			getlblTotalFees().setText(
				caRenewalBarCodeData.getRenwlPrice().toString());
		}
		catch (RTSException leRTSEx)
		{
			// TODO handle RTSException
		}
		// Set data for Registration 
		gettxtRegExpMo().setText(caRenewalBarCodeData.getRegExpMo());
		gettxtRegExpYr().setText(caRenewalBarCodeData.getRegExpYr());
		gettxtRegPltCd().setText(caRenewalBarCodeData.getRegPltCd());
		gettxtRegPltNo().setText(caRenewalBarCodeData.getRegPltNo());
		gettxtRegStkrCd().setText(caRenewalBarCodeData.getRegStkrCd());
		gettxtRegClassCd().setText(
			String.valueOf(caRenewalBarCodeData.getRegClassCd()));

		// defect 9085
		if (caRenewalBarCodeData
			.getVersion()
			.equals(BarCodeScanner.BARCODE_VERSION05))
		{
			// defect 10303 
			setBarcodeVersion5Data();
			gettxtNextRegExpMo().setText("");
			gettxtNextRegExpYr().setText("");
			gettxtPltExpMo().setText("");
			gettxtPltExpYr().setText("");
			gettxtNextPltExpMo().setText("");
			gettxtNextPltExpYr().setText("");
			gettxtPlateValidityTerm().setText("");
		}
		else if (
			caRenewalBarCodeData.getVersion().equals(
				BarCodeScanner.BARCODE_VERSION06))
		{
			setBarcodeVersion5Data();

			gettxtNextRegExpMo().setText(
				caRenewalBarCodeData.getRegNextExpMo());
			gettxtNextRegExpYr().setText(
				caRenewalBarCodeData.getRegNextExpYr());
			gettxtPltExpMo().setText(
				caRenewalBarCodeData.getPltExpMo());
			gettxtPltExpYr().setText(
				caRenewalBarCodeData.getPltExpYr());
			gettxtNextPltExpMo().setText(
				caRenewalBarCodeData.getPltNextExpMo());
			gettxtNextPltExpYr().setText(
				caRenewalBarCodeData.getPltNextExpYr());
			gettxtPlateValidityTerm().setText(
				caRenewalBarCodeData.getPltValidityTerm());
		}
		else
		{
			gettxtOrgNo().setText("");
			gettxtOrgName().setText("");
			gettxtAddlSetIndi().setText("");
			gettxtNextRegExpMo().setText("");
			gettxtNextRegExpYr().setText("");
			gettxtPltExpMo().setText("");
			gettxtPltExpYr().setText("");
			gettxtNextPltExpMo().setText("");
			gettxtNextPltExpYr().setText("");
			gettxtPlateValidityTerm().setText("");

		}
		// end defect 10303 
		// end defect 9085 

		gettxtVIN().setText(caRenewalBarCodeData.getVin());
		// Set data for Additional info
		gettxtAuditTrailCd().setText(
			caRenewalBarCodeData.getAuditTrailTransId());
		gettxtCntyNo().setText(caRenewalBarCodeData.getCntyNo());
		gettxtDocType().setText(caRenewalBarCodeData.getDocNo());
		// Set Fields
		turnOnOffPrntCntyNo(false);

		//****  Plate Barcode  ****
		turnOnOffPlate(false);
		turnOnOffCommonFlds(true);
		//****  End Plate Barcode  ****
	}

	/**
	 * Populate display fields for Sticker Printing or Renewal barcode.
	 * 
	 * @throws RTSException 
	 */
	public void addRTSExceptBarCodeDataToDialog() throws RTSException
	{
		// Set Type of Scan
		String lsType =
			barcodescanner.getRawBarcodeData().substring(0, 1);
		if (lsType.equals("X"))
		{
			addStrkPrntBarCodeDataToDialog();
		}
		if (lsType.equals("R"))
		{
			setRenewalExceptData();
			addRewlBarCodeDataToDialog();
		}

		getstcLblTypeOfBarCode().setText(RTSEXCEPTION);

	}

	/**
	 * Populate display fields for Sticker printing barcode.
	 * 
	 */
	public void addStrkPrntBarCodeDataToDialog() throws RTSException
	{
		// Set Type of Scan
		getstcLblTypeOfBarCode().setText(STKRPRNT);
		gettxtBarcodeType().setText(cStkrPrntBarCodeData.getType());
		gettxtBarcodeVersion().setText(
			cStkrPrntBarCodeData.getVersion());

		// Set Bar Code raw data to text fields
		gettxtABarcodeRaw().setText(barcodescanner.getRawBarcodeData());
		gettxtABarcodeRaw().repaint();

		// Registration/Vehicle Information	
		gettxtPrntCntyNo().setText(
			cStkrPrntBarCodeData.getPrntCntyNo());
		gettxtCntyNo().setText(cStkrPrntBarCodeData.getResCompCntyNo());
		gettxtDocType().setText(cStkrPrntBarCodeData.getDocNo());
		gettxtVIN().setText(cStkrPrntBarCodeData.getVIN());
		gettxtResCompCntyNo().setText(
			cStkrPrntBarCodeData.getResCompCntyNo());
		gettxtPrntWsId().setText(cStkrPrntBarCodeData.getWorkStatId());
		gettxtPrntCntyNo().setText(
			cStkrPrntBarCodeData.getPrntCntyNo());
		;
		gettxtStkrPrntDate().setText(
			cStkrPrntBarCodeData.getStkrPrntDate());
		gettxtPrntRegPltCd().setText(
			cStkrPrntBarCodeData.getRegisPltCd());
		gettxtPrntRegStkrCd().setText(
			cStkrPrntBarCodeData.getRegisStkrCd());
		gettxtPrntRegPltNo().setText(
			cStkrPrntBarCodeData.getRegPltNo());
		gettxtRegExpDate().setText(
			cStkrPrntBarCodeData.getRegExpDate());
		gettxtPrntRegClassCd().setText(
			cStkrPrntBarCodeData.getRegClassCd());

		// Turn on/off the appropriate fields	
		turnOnOffPrntCntyNo(true);

		//****  Plate Barcode  ****
		turnOnOffCommonFlds(true);

		//Add Plate
		turnOnOffPlate(false);
		// End Plate
		//****  End Plate Barcode  ****
	}

	/**
	 * Invoked when a barcode is scanned.
	 * 
	 * @param e com.txdot.isd.rts.sevices.util.event.BarCodeEvent
	 */
	public void barCodeScanned(
		com.txdot.isd.rts.services.util.event.BarCodeEvent e)
	{
		// Gets barcode data
		Object barCodeData = e.getBarCodeData();
		try
		{
			if (barCodeData instanceof RenewalBarCodeData)
			{
				//****  Renewal Barcode  ****
				caRenewalBarCodeData = (RenewalBarCodeData) barCodeData;
				addRewlBarCodeDataToDialog();
			}
			else if (barCodeData instanceof StickerPrintingBarCodeData)
			{
				//****  Sticker Barcode  ****
				cStkrPrntBarCodeData =
					(StickerPrintingBarCodeData) barCodeData;
				addStrkPrntBarCodeDataToDialog();
			}

			else if (barCodeData instanceof PlateBarCodeData)
			{
				//****  Plate Barcode  ****
				cPlateBarCodeData = (PlateBarCodeData) barCodeData;
				addPlateBarCodeDataToDialog();
			}
			else if (barCodeData instanceof RTSException)
			{
				// 06/18/2004
				// Set Bar Code raw data to text fields
				//gettxtBarcodeRaw().setText(barcodescanner.getRawBarcodeData());
				addRTSExceptBarCodeDataToDialog();
				//displayScannerMsg();
				// End 06/18/2004
			}

			else
			{
				// Set Type of Scan
				getstcLblTypeOfBarCode().setText(UNKNOWNFORMAT);
				getstcLblTypeOfBarCode().repaint();

				// Set Bar Code raw data to text fields
				gettxtABarcodeRaw().setText(
					barcodescanner.getRawBarcodeData());
				turnOnOffCommonFlds(false);
				turnOnOffPlate(false);
				turnOnOffPrntCntyNo(false);
				turnOffOnStkrRenwl(false);
			}
		}
		catch (RTSException rtsException)
		{
			if (rtsException.getMessage() != null)
			{
				RTSException ex = new RTSException();
				ex.displayError(this);
				ex.getFirstComponent().requestFocus();
			}
			else
			{
				rtsException.displayError(this);
			}
		}
	}

	/**
	 * Insert the method's description here.
	 * 
	 */
	private void clearStkrPrntBarCodeData()
	{
		// Renewal cStkrPrntBarCodeData
		caRenewalBarCodeData = new RenewalBarCodeData();
		caRenewalBarCodeData.setCntyNo("");
		caRenewalBarCodeData.setRegExpMo("");
		caRenewalBarCodeData.setRegExpYr("");
		caRenewalBarCodeData.setItmPrice1(new Dollar("2.22"));
		caRenewalBarCodeData.setItmPrice2(new Dollar("3.33"));
		caRenewalBarCodeData.setItmPrice3(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice4(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice5(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice6(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice7(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice8(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice9(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice10(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice11(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice12(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice13(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice14(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice15(new Dollar("0.00"));
		caRenewalBarCodeData.setAcctItmCd1("");
		caRenewalBarCodeData.setAcctItmCd2("");
		caRenewalBarCodeData.setAcctItmCd3("");
		caRenewalBarCodeData.setAcctItmCd4("");
		caRenewalBarCodeData.setAcctItmCd5("");
		caRenewalBarCodeData.setAcctItmCd6("");
		caRenewalBarCodeData.setAcctItmCd7("");
		caRenewalBarCodeData.setAcctItmCd8("");
		caRenewalBarCodeData.setAcctItmCd9("");
		caRenewalBarCodeData.setAcctItmCd10("");
		caRenewalBarCodeData.setAcctItmCd11("");
		caRenewalBarCodeData.setAcctItmCd12("");
		caRenewalBarCodeData.setAcctItmCd13("");
		caRenewalBarCodeData.setAcctItmCd14("");
		caRenewalBarCodeData.setAcctItmCd15("");
		caRenewalBarCodeData.setAuditTrailTransId("");
		caRenewalBarCodeData.setDocNo("");
		caRenewalBarCodeData.setRegClassCd(0);
		caRenewalBarCodeData.setRegPltNo("");
		caRenewalBarCodeData.setRegStkrCd("");
		caRenewalBarCodeData.setRegPltCd("");
		caRenewalBarCodeData.setVin("");

		// Sticker Print cStkrPrntBarCodeData
		cStkrPrntBarCodeData = new StickerPrintingBarCodeData();
		cStkrPrntBarCodeData.setType("");
		cStkrPrntBarCodeData.setVersion("");
		// Save Information
		cStkrPrntBarCodeData.setDocNo("");
		cStkrPrntBarCodeData.setVIN("");
		cStkrPrntBarCodeData.setResCompCntyNo("");
		cStkrPrntBarCodeData.setWorkStatId("");
		cStkrPrntBarCodeData.setPrntCntyNo("");
		cStkrPrntBarCodeData.setStkrPrntDate("");
		cStkrPrntBarCodeData.setRegisPltCd("");
		cStkrPrntBarCodeData.setRegisStkrCd("");
		cStkrPrntBarCodeData.setRegPltNo("");
		cStkrPrntBarCodeData.setRegExpDate("");
		cStkrPrntBarCodeData.setRegClassCd("");
	}

	/**
	 * Insert the method's description here.
	 * 
	 */
	private void clearData()
	{
		caRenewalBarCodeData = new RenewalBarCodeData();
		caRenewalBarCodeData.setCntyNo("170");
		caRenewalBarCodeData.setRegExpMo("12");
		caRenewalBarCodeData.setRegExpYr("2001");
		caRenewalBarCodeData.setItmPrice1(new Dollar("2.22"));
		caRenewalBarCodeData.setItmPrice2(new Dollar("3.33"));
		caRenewalBarCodeData.setItmPrice3(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice4(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice5(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice6(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice7(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice8(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice9(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice10(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice11(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice12(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice13(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice14(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice15(new Dollar("0.00"));
		caRenewalBarCodeData.setAcctItmCd1("CLSTKR");
		caRenewalBarCodeData.setAcctItmCd2("YGFARMER");
		caRenewalBarCodeData.setAcctItmCd3("");
		caRenewalBarCodeData.setAcctItmCd4("");
		caRenewalBarCodeData.setAcctItmCd5("");
		caRenewalBarCodeData.setAcctItmCd6("");
		caRenewalBarCodeData.setAcctItmCd7("");
		caRenewalBarCodeData.setAcctItmCd8("");
		caRenewalBarCodeData.setAcctItmCd9("");
		caRenewalBarCodeData.setAcctItmCd10("");
		caRenewalBarCodeData.setAcctItmCd11("");
		caRenewalBarCodeData.setAcctItmCd12("");
		caRenewalBarCodeData.setAcctItmCd13("");
		caRenewalBarCodeData.setAcctItmCd14("");
		caRenewalBarCodeData.setAcctItmCd15("");
		caRenewalBarCodeData.setAuditTrailTransId("");
		caRenewalBarCodeData.setDocNo("");
		caRenewalBarCodeData.setRegClassCd(25);
		caRenewalBarCodeData.setRegPltNo("PLTNO");
		caRenewalBarCodeData.setRegStkrCd("PSP");
		caRenewalBarCodeData.setRegPltCd("TKP");
		caRenewalBarCodeData.setVin("asdfsadf2r234");
		//cFeeDataMail = handleMailFeesData("MAIL");
	}

	/**
	 * Clear all fields.
	 * 
	 */
	public void clearFields()
	{ // Raw Data
		gettxtABarcodeRaw().setText("");
		getstcLblTypeOfBarCode().setText(SCANREADY);

		// Shared Fields
		gettxtDocType().setText("");
		gettxtVIN().setText("");

		// Account Item Codes
		gettxtAcctItmCd1().setText("");
		gettxtAcctItmCd2().setText("");
		gettxtAcctItmCd3().setText("");
		gettxtAcctItmCd4().setText("");
		gettxtAcctItmCd5().setText("");
		gettxtAcctItmCd6().setText("");
		gettxtAcctItmCd7().setText("");
		gettxtAcctItmCd8().setText("");
		gettxtAcctItmCd9().setText("");
		gettxtAcctItmCd10().setText("");
		gettxtAcctItmCd11().setText("");
		gettxtAcctItmCd12().setText("");
		gettxtAcctItmCd13().setText("");
		gettxtAcctItmCd14().setText("");
		gettxtAcctItmCd15().setText("");
		gettxtPrntCntyNo().setText("");
		gettxtItmPrice1().setText("");

		// Item Prices
		gettxtItmPrice1().setText("");
		gettxtItmPrice2().setText("");
		gettxtItmPrice3().setText("");
		gettxtItmPrice4().setText("");
		gettxtItmPrice5().setText("");
		gettxtItmPrice6().setText("");
		gettxtItmPrice7().setText("");
		gettxtItmPrice8().setText("");
		gettxtItmPrice9().setText("");
		gettxtItmPrice10().setText("");
		gettxtItmPrice11().setText("");
		gettxtItmPrice12().setText("");
		gettxtItmPrice13().setText("");
		gettxtItmPrice14().setText("");
		gettxtItmPrice15().setText("");
		getlblTotalFees().setText("");

		// Registration
		gettxtAuditTrailCd().setText("");
		gettxtBarcodeVersion().setText("");
		gettxtBarcodeType().setText("");
		gettxtCntyNo().setText("");
		gettxtRegExpMo().setText("");
		gettxtRegExpYr().setText("");
		gettxtRegExpDate().setText("");
		//getstcLblBkSlash().setText("");
		gettxtRegPltCd().setText("");
		gettxtRegPltNo().setText("");
		gettxtRegStkrCd().setText("");
		gettxtRegClassCd().setText("");
		gettxtPrntRegClassCd().setText("");
		// defect 9085 
		gettxtOrgName().setText("");
		gettxtOrgNo().setText("");
		gettxtAddlSetIndi().setText("");
		// end defect 9085
		// defect 9086
		gettxtNewPlatesRequiredCode().setText("");
		// end defect 9086 

		// defect 10303 
		gettxtNextRegExpMo().setText("");
		gettxtNextRegExpYr().setText("");
		gettxtPltExpMo().setText("");
		gettxtPltExpYr().setText("");
		gettxtNextPltExpMo().setText("");
		gettxtNextPltExpYr().setText("");
		gettxtPlateValidityTerm().setText("");
		// end defect 10303 

		// Sticker Printing
		gettxtStkrPrntDate().setText("");
		gettxtResCompCntyNo().setText("");
		gettxtPrntCntyNo().setText("");
		gettxtPrntRegPltCd().setText("");
		gettxtPrntRegPltNo().setText("");
		gettxtPrntRegStkrCd().setText("");
		gettxtPrntWsId().setText("");

		//****  Plate Barcode
		// Plate
		gettxtPlateItemYr().setText("");
		gettxtPlateRegPltCd().setText("");
		gettxtPlateStkrNo().setText("");
		//****  End Plate Barcode
	}

	/**
	 * Insert the method's description here.
	 * 
	 */
	private void createTestData()
	{
		caRenewalBarCodeData = new RenewalBarCodeData();
		caRenewalBarCodeData.setCntyNo("170");
		caRenewalBarCodeData.setRegExpMo("12");
		caRenewalBarCodeData.setRegExpYr("2001");
		caRenewalBarCodeData.setItmPrice1(new Dollar("2.22"));
		caRenewalBarCodeData.setItmPrice2(new Dollar("3.33"));
		caRenewalBarCodeData.setItmPrice3(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice4(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice5(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice6(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice7(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice8(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice9(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice10(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice11(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice12(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice13(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice14(new Dollar("0.00"));
		caRenewalBarCodeData.setItmPrice15(new Dollar("0.00"));
		caRenewalBarCodeData.setAcctItmCd1("CLSTKR");
		caRenewalBarCodeData.setAcctItmCd2("YGFARMER");
		caRenewalBarCodeData.setAcctItmCd3("");
		caRenewalBarCodeData.setAcctItmCd4("");
		caRenewalBarCodeData.setAcctItmCd5("");
		caRenewalBarCodeData.setAcctItmCd6("");
		caRenewalBarCodeData.setAcctItmCd7("");
		caRenewalBarCodeData.setAcctItmCd8("");
		caRenewalBarCodeData.setAcctItmCd9("");
		caRenewalBarCodeData.setAcctItmCd10("");
		caRenewalBarCodeData.setAcctItmCd11("");
		caRenewalBarCodeData.setAcctItmCd12("");
		caRenewalBarCodeData.setAcctItmCd13("");
		caRenewalBarCodeData.setAcctItmCd14("");
		caRenewalBarCodeData.setAcctItmCd15("");
		caRenewalBarCodeData.setAuditTrailTransId("");
		caRenewalBarCodeData.setDocNo("");
		caRenewalBarCodeData.setRegClassCd(25);
		caRenewalBarCodeData.setRegPltNo("PLTNO");
		caRenewalBarCodeData.setRegStkrCd("PSP");
		caRenewalBarCodeData.setRegPltCd("TKP");
		caRenewalBarCodeData.setVin("asdfsadf2r234");
		//cFeeDataMail = handleMailFeesData("MAIL");
	}

	/**
	 * Display scanner msg
	 * 
	 */
	private void displayScannerMsg()
	{
		renewalQueue.clear();
		new RTSException(
			RTSException.FAILURE_MESSAGE,
			SCANERROR,
			"").displayError(
			this);
	}
//
//	/**
//	 * Invoked when a component gains the keyboard focus.
//	 */
//	public void focusGained(java.awt.event.FocusEvent e)
//	{
//	}
//
//	/**
//	 * Invoked when a component loses the keyboard focus.
//	 */
//	public void focusLost(java.awt.event.FocusEvent e)
//	{
//	}

	/**
	 * Returns the graph of the BarCodeScanner in this application
	 * @return BarCodeScanner
	 */
	public BarCodeScanner getBarCodeScanner() throws RTSException
	{
		if (barcodescanner == null)
		{
			throw barcodeException;
		}
		return barcodescanner;
	}

	/**
	 * Return the btnClear property value.
	 * @return RTSButton
	 */
	private RTSButton getbtnClear()
	{
		if (ivjbtnClear == null)
		{
			try
			{
				ivjbtnClear =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnClear.setName("btnClear");
				ivjbtnClear.setManagingFocus(false);
				ivjbtnClear.setText("Clear");
				ivjbtnClear.setMaximumSize(
					new java.awt.Dimension(65, 25));
				ivjbtnClear.setActionCommand("Clear");
				ivjbtnClear.setBounds(18, 9, 99, 25);
				ivjbtnClear.setMinimumSize(
					new java.awt.Dimension(65, 25));
				// user code begin {1}
				ivjbtnClear.addActionListener(this);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnClear;
	}

	/**
	 * Return the btnClose property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnClose()
	{
		if (ivjbtnClose == null)
		{
			try
			{
				ivjbtnClose =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnClose.setName("btnClose");
				ivjbtnClose.setManagingFocus(false);
				ivjbtnClose.setText("Close");
				ivjbtnClose.setMaximumSize(
					new java.awt.Dimension(67, 25));
				ivjbtnClose.setActionCommand("Close");
				ivjbtnClose.setBounds(126, 9, 101, 25);
				ivjbtnClose.setMinimumSize(
					new java.awt.Dimension(67, 25));
				// user code begin {1}
				ivjbtnClose.addActionListener(this);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnClose;
	}

	/**
	 * Return the btnPanel property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getbtnPanel()
	{
		if (ivjbtnPanel == null)
		{
			try
			{
				ivjbtnPanel = new javax.swing.JPanel();
				ivjbtnPanel.setName("btnPanel");
				ivjbtnPanel.setLayout(null);
				ivjbtnPanel.setBounds(411, 511, 249, 40);
				ivjbtnPanel.setMinimumSize(
					new java.awt.Dimension(391, 51));
				getbtnPanel().add(
					getbtnClear(),
					getbtnClear().getName());
				getbtnPanel().add(
					getbtnClose(),
					getbtnClose().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnPanel;
	}

	/**
	 * Return the FrmBarcodeReaderContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getFrmBarcodeReaderContentPane1()
	{
		if (ivjFrmBarcodeReaderContentPane1 == null)
		{
			try
			{
				ivjFrmBarcodeReaderContentPane1 =
					new javax.swing.JPanel();
				ivjFrmBarcodeReaderContentPane1.setName(
					"FrmBarcodeReaderContentPane1");
				ivjFrmBarcodeReaderContentPane1.setLayout(null);
				ivjFrmBarcodeReaderContentPane1.setMinimumSize(
					new java.awt.Dimension(0, 0));
				getFrmBarcodeReaderContentPane1().add(
					getbtnPanel(),
					getbtnPanel().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblBarcodeRawHeader(),
					getstcLblBarcodeRawHeader().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtABarcodeRaw(),
					gettxtABarcodeRaw().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblType(),
					getstcLblType().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblVersion(),
					getstcLblVersion().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblDocNo(),
					getstcLblDocNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblVIN(),
					getstcLblVIN().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAuditTrailTransId(),
					getstcLblAuditTrailTransId().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblRegPltCd(),
					getstcLblRegPltCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblRegStkrCd(),
					getstcLblRegStkrCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblRegPltNo(),
					getstcLblRegPltNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblRegExpDate(),
					getstcLblRegExpDate().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblCntyNo(),
					getstcLblCntyNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd1(),
					getstcLblAcctItmCd1().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd2(),
					getstcLblAcctItmCd2().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd3(),
					getstcLblAcctItmCd3().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd4(),
					getstcLblAcctItmCd4().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd5(),
					getstcLblAcctItmCd5().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd6(),
					getstcLblAcctItmCd6().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd7(),
					getstcLblAcctItmCd7().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtBarcodeType(),
					gettxtBarcodeType().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtBarcodeVersion(),
					gettxtBarcodeVersion().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtDocType(),
					gettxtDocType().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtVIN(),
					gettxtVIN().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAuditTrailCd(),
					gettxtAuditTrailCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtRegPltCd(),
					gettxtRegPltCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtRegStkrCd(),
					gettxtRegStkrCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtRegPltNo(),
					gettxtRegPltNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtRegExpDate(),
					gettxtRegExpDate().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtCntyNo(),
					gettxtCntyNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice1(),
					gettxtItmPrice1().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice2(),
					gettxtItmPrice2().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice3(),
					gettxtItmPrice3().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice4(),
					gettxtItmPrice4().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice5(),
					gettxtItmPrice5().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice6(),
					gettxtItmPrice6().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice7(),
					gettxtItmPrice7().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd8(),
					getstcLblAcctItmCd8().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd9(),
					getstcLblAcctItmCd9().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd10(),
					getstcLblAcctItmCd10().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd11(),
					getstcLblAcctItmCd11().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd12(),
					getstcLblAcctItmCd12().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd13(),
					getstcLblAcctItmCd13().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd14(),
					getstcLblAcctItmCd14().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd1(),
					gettxtAcctItmCd1().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd2(),
					gettxtAcctItmCd2().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd3(),
					gettxtAcctItmCd3().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd4(),
					gettxtAcctItmCd4().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd5(),
					gettxtAcctItmCd5().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd6(),
					gettxtAcctItmCd6().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd7(),
					gettxtAcctItmCd7().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblPrntRegClassCd(),
					getstcLblPrntRegClassCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtPrntRegClassCd(),
					gettxtPrntRegClassCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblPrntCntyNo(),
					getstcLblPrntCntyNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtPrntCntyNo(),
					gettxtPrntCntyNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblRegExpMoYr(),
					getstcLblRegExpMoYr().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtRegExpMo(),
					gettxtRegExpMo().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtRegExpYr(),
					gettxtRegExpYr().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblBkSlash(),
					getstcLblBkSlash().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblHeader(),
					getstcLblHeader().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblTypeOfBarCode(),
					getstcLblTypeOfBarCode().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblResCompCntyNo(),
					getstcLblResCompCntyNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtResCompCntyNo(),
					gettxtResCompCntyNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblWsId(),
					getstcLblWsId().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtPrntWsId(),
					gettxtPrntWsId().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblStkrPrntDate(),
					getstcLblStkrPrntDate().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtStkrPrntDate(),
					gettxtStkrPrntDate().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblPrntRegPltCd(),
					getstcLblPrntRegPltCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtPrntRegPltCd(),
					gettxtPrntRegPltCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtPrntRegStkrCd(),
					gettxtPrntRegStkrCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblPrntRegStkrCd(),
					getstcLblPrntRegStkrCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblPrntRegPltNo(),
					getstcLblPrntRegPltNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtPrntRegPltNo(),
					gettxtPrntRegPltNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtRegClassCd(),
					gettxtRegClassCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblRegClassCd(),
					getstcLblRegClassCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblAcctItmCd15(),
					getstcLblAcctItmCd15().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd8(),
					gettxtAcctItmCd8().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd9(),
					gettxtAcctItmCd9().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd10(),
					gettxtAcctItmCd10().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd11(),
					gettxtAcctItmCd11().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd12(),
					gettxtAcctItmCd12().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd13(),
					gettxtAcctItmCd13().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd14(),
					gettxtAcctItmCd14().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtAcctItmCd15(),
					gettxtAcctItmCd15().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice8(),
					gettxtItmPrice8().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice9(),
					gettxtItmPrice9().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice10(),
					gettxtItmPrice10().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice11(),
					gettxtItmPrice11().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice12(),
					gettxtItmPrice12().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice13(),
					gettxtItmPrice13().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice14(),
					gettxtItmPrice14().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtItmPrice15(),
					gettxtItmPrice15().getName());
				// defect 9086
				getFrmBarcodeReaderContentPane1().add(
					getstcLblTotalFees(),
					getstcLblTotalFees().getName());
				getFrmBarcodeReaderContentPane1().add(
					getlblTotalFees(),
					getlblTotalFees().getName());
				// end defect 9086
				getFrmBarcodeReaderContentPane1().add(
					getstcLblPlateRegPltCd(),
					getstcLblPlateRegPltCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblPlateStkrNo(),
					getstcLblPlateStkrNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					getstcLblPlateItemYr(),
					getstcLblPlateItemYr().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtPlateRegPltCd(),
					gettxtPlateRegPltCd().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtPlateStkrNo(),
					gettxtPlateStkrNo().getName());
				getFrmBarcodeReaderContentPane1().add(
					gettxtPlateItemYr(),
					gettxtPlateItemYr().getName());
				// user code begin {1}
				ivjFrmBarcodeReaderContentPane1.add(
					gettxtOrgNo(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					getstcLblOrgNoOrgName(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					gettxtOrgName(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					getstcLblAddlSetIndi(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					gettxtAddlSetIndi(),
					null);
				//defect 9086
				ivjFrmBarcodeReaderContentPane1.add(
					getstcLblNewPlatesRequiredCode(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					gettxtNewPlatesRequiredCode(),
					null);
				// end defect 9086
				ivjFrmBarcodeReaderContentPane1.add(
					getstcLblPltExpMoYr(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					getstcLblNextRegExpMoYr(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					getstcLblNextPltExpMoYr(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					gettxtNextRegExpMo(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					gettxtPltExpMo(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					gettxtNextPltExpMo(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					getstcLblBkSlash1(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					getstcLblBkSlash2(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					getstcLblBkSlash3(),
					null);
				// user code end
				ivjFrmBarcodeReaderContentPane1.add(
					gettxtNextRegExpYr(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					gettxtPltExpYr(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					gettxtNextPltExpYr(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					getstcLblPlateValidityTerm(),
					null);
				ivjFrmBarcodeReaderContentPane1.add(
					gettxtPlateValidityTerm(),
					null);
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFrmBarcodeReaderContentPane1;
	}
	
	/**
	 * Return the ivjlblTotalFees property value.
	 * 
	 * @return JLabel 
	 */
	private JLabel getlblTotalFees()
	{
		if (ivjlblTotalFees == null)
		{
			try
			{
				ivjlblTotalFees = new JLabel();
				ivjlblTotalFees.setName("ivjlblTotalFees");
				ivjlblTotalFees.setBounds(709, 492, 75, 15);
				ivjlblTotalFees.setMinimumSize(
					new java.awt.Dimension(34, 14));
				// make the text bold
				ivjlblTotalFees.setFont(
					new java.awt.Font(
						UtilityMethods.getDefaultFont(),
						java.awt.Font.BOLD,
						UtilityMethods.getDefaultFontSize()));
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblTotalFees;
	}

	/**
	 * Return the ivjstcLblAcctItmCd1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd1()
	{
		if (ivjstcLblAcctItmCd1 == null)
		{
			try
			{
				ivjstcLblAcctItmCd1 = new JLabel();
				ivjstcLblAcctItmCd1.setName("ivjstcLblAcctItmCd1");
				ivjstcLblAcctItmCd1.setText("AcctItmCd,Price01:");
				ivjstcLblAcctItmCd1.setBounds(37, 331, 109, 15);
				ivjstcLblAcctItmCd1.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd1;
	}

	/**
	 * Return the ivjstcLblAcctItmCd10 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd10()
	{
		if (ivjstcLblAcctItmCd10 == null)
		{
			try
			{
				ivjstcLblAcctItmCd10 = new JLabel();
				ivjstcLblAcctItmCd10.setName("ivjstcLblAcctItmCd10");
				ivjstcLblAcctItmCd10.setText("AcctItmCd,Price10:");
				ivjstcLblAcctItmCd10.setBounds(450, 372, 109, 14);
				ivjstcLblAcctItmCd10.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd10;
	}

	/**
	 * Return the ivjstcLblAcctItmCd11 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd11()
	{
		if (ivjstcLblAcctItmCd11 == null)
		{
			try
			{
				ivjstcLblAcctItmCd11 = new JLabel();
				ivjstcLblAcctItmCd11.setName("ivjstcLblAcctItmCd11");
				ivjstcLblAcctItmCd11.setText("AcctItmCd,Price11:");
				ivjstcLblAcctItmCd11.setBounds(450, 394, 109, 14);
				ivjstcLblAcctItmCd11.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd11;
	}

	/**
	 * Return the ivjstcLblAcctItmCd12 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd12()
	{
		if (ivjstcLblAcctItmCd12 == null)
		{
			try
			{
				ivjstcLblAcctItmCd12 = new JLabel();
				ivjstcLblAcctItmCd12.setName("ivjstcLblAcctItmCd12");
				ivjstcLblAcctItmCd12.setText("AcctItmCd,Price12:");
				ivjstcLblAcctItmCd12.setBounds(450, 414, 109, 14);
				ivjstcLblAcctItmCd12.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd12;
	}

	/**
	 * Return the ivjstcLblAcctItmCd13 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd13()
	{
		if (ivjstcLblAcctItmCd13 == null)
		{
			try
			{
				ivjstcLblAcctItmCd13 = new JLabel();
				ivjstcLblAcctItmCd13.setName("ivjstcLblAcctItmCd13");
				ivjstcLblAcctItmCd13.setText("AcctItmCd,Price13:");
				ivjstcLblAcctItmCd13.setBounds(450, 434, 109, 14);
				ivjstcLblAcctItmCd13.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd13;
	}

	/**
	 * Return the ivjstcLblAcctItmCd14 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd14()
	{
		if (ivjstcLblAcctItmCd14 == null)
		{
			try
			{
				ivjstcLblAcctItmCd14 = new JLabel();
				ivjstcLblAcctItmCd14.setName("ivjstcLblAcctItmCd14");
				ivjstcLblAcctItmCd14.setText("AcctItmCd,Price14:");
				ivjstcLblAcctItmCd14.setBounds(450, 453, 109, 14);
				ivjstcLblAcctItmCd14.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd14;
	}
	
	/**
	 * Return the ivjstcLblAcctItmCd15 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd15()
	{
		if (ivjstcLblAcctItmCd15 == null)
		{
			try
			{
				ivjstcLblAcctItmCd15 = new JLabel();
				ivjstcLblAcctItmCd15.setSize(109, 14);
				ivjstcLblAcctItmCd15.setName("ivjstcLblAcctItmCd15");
				ivjstcLblAcctItmCd15.setText("AcctItmCd,Price15:");
				ivjstcLblAcctItmCd15.setLocation(450, 473);
				ivjstcLblAcctItmCd15.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd15;
	}

	/**
	 * Return the ivjstcLblAcctItmCd2 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd2()
	{
		if (ivjstcLblAcctItmCd2 == null)
		{
			try
			{
				ivjstcLblAcctItmCd2 = new JLabel();
				ivjstcLblAcctItmCd2.setName("ivjstcLblAcctItmCd2");
				ivjstcLblAcctItmCd2.setText("AcctItmCd,Price02:");
				ivjstcLblAcctItmCd2.setBounds(37, 352, 109, 14);
				ivjstcLblAcctItmCd2.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd2;
	}

	/**
	 * Return the ivjstcLblAcctItmCd3 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd3()
	{
		if (ivjstcLblAcctItmCd3 == null)
		{
			try
			{
				ivjstcLblAcctItmCd3 = new JLabel();
				ivjstcLblAcctItmCd3.setName("ivjstcLblAcctItmCd3");
				ivjstcLblAcctItmCd3.setText("AcctItmCd,Price03:");
				ivjstcLblAcctItmCd3.setBounds(37, 372, 109, 14);
				ivjstcLblAcctItmCd3.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd3;
	}
	
	/**
	 * Return the ivjstcLblAcctItmCd4 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd4()
	{
		if (ivjstcLblAcctItmCd4 == null)
		{
			try
			{
				ivjstcLblAcctItmCd4 = new JLabel();
				ivjstcLblAcctItmCd4.setName("ivjstcLblAcctItmCd4");
				ivjstcLblAcctItmCd4.setText("AcctItmCd,Price04:");
				ivjstcLblAcctItmCd4.setBounds(37, 394, 109, 14);
				ivjstcLblAcctItmCd4.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd4;
	}

	/**
	 * Return the ivjstcLblAcctItmCd5 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd5()
	{
		if (ivjstcLblAcctItmCd5 == null)
		{
			try
			{
				ivjstcLblAcctItmCd5 = new JLabel();
				ivjstcLblAcctItmCd5.setName("ivjstcLblAcctItmCd5");
				ivjstcLblAcctItmCd5.setText("AcctItmCd,Price05:");
				ivjstcLblAcctItmCd5.setBounds(37, 414, 109, 14);
				ivjstcLblAcctItmCd5.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd5;
	}
	
	/**
	 * Return the ivjstcLblAcctItmCd6 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd6()
	{
		if (ivjstcLblAcctItmCd6 == null)
		{
			try
			{
				ivjstcLblAcctItmCd6 = new JLabel();
				ivjstcLblAcctItmCd6.setName("ivjstcLblAcctItmCd6");
				ivjstcLblAcctItmCd6.setText("AcctItmCd,Price06:");
				ivjstcLblAcctItmCd6.setBounds(37, 434, 109, 14);
				ivjstcLblAcctItmCd6.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd6;
	}
	
	/**
	 * Return the ivjstcLblAcctItmCd7 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd7()
	{
		if (ivjstcLblAcctItmCd7 == null)
		{
			try
			{
				ivjstcLblAcctItmCd7 = new JLabel();
				ivjstcLblAcctItmCd7.setSize(109, 14);
				ivjstcLblAcctItmCd7.setName("ivjstcLblAcctItmCd7");
				ivjstcLblAcctItmCd7.setText("AcctItmCd,Price07:");
				ivjstcLblAcctItmCd7.setLocation(37, 453);
				ivjstcLblAcctItmCd7.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd7;
	}
	
	/**
	 * Return the ivjstcLblAcctItmCd8 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd8()
	{
		if (ivjstcLblAcctItmCd8 == null)
		{
			try
			{
				ivjstcLblAcctItmCd8 = new JLabel();
				ivjstcLblAcctItmCd8.setName("ivjstcLblAcctItmCd8");
				ivjstcLblAcctItmCd8.setText("AcctItmCd,Price08:");
				ivjstcLblAcctItmCd8.setBounds(450, 331, 109, 15);
				ivjstcLblAcctItmCd8.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd8;
	}
	
	/**
	 * Return the ivjstcLblAcctItmCd9 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAcctItmCd9()
	{
		if (ivjstcLblAcctItmCd9 == null)
		{
			try
			{
				ivjstcLblAcctItmCd9 = new JLabel();
				ivjstcLblAcctItmCd9.setName("ivjstcLblAcctItmCd9");
				ivjstcLblAcctItmCd9.setText("AcctItmCd,Price09:");
				ivjstcLblAcctItmCd9.setBounds(450, 352, 109, 14);
				ivjstcLblAcctItmCd9.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAcctItmCd9;
	}

	/**
	 * This method initializes ivjstcLblAddlSetIndi
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAddlSetIndi()
	{
		if (ivjstcLblAddlSetIndi == null)
		{
			ivjstcLblAddlSetIndi = new JLabel();
			ivjstcLblAddlSetIndi.setSize(67, 15);
			ivjstcLblAddlSetIndi.setText("AddlSetIndi:");
			ivjstcLblAddlSetIndi.setLocation(79, 492);
			ivjstcLblAddlSetIndi.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblAddlSetIndi;
	}
	
	/**
	 * Return the ivjstcLblAuditTrailTransId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAuditTrailTransId()
	{
		if (ivjstcLblAuditTrailTransId == null)
		{
			try
			{
				ivjstcLblAuditTrailTransId = new JLabel();
				ivjstcLblAuditTrailTransId.setName("ivjstcLblAuditTrailTransId");
				ivjstcLblAuditTrailTransId.setText("AuditTrailTransId:");
				ivjstcLblAuditTrailTransId.setBounds(130, 255, 101, 14);
				ivjstcLblAuditTrailTransId.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAuditTrailTransId;
	}
	
	/**
	 * Return the ivjstcLblBarcodeRawHeader property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBarcodeRawHeader()
	{
		if (ivjstcLblBarcodeRawHeader == null)
		{
			try
			{
				ivjstcLblBarcodeRawHeader = new JLabel();
				ivjstcLblBarcodeRawHeader.setName("ivjstcLblBarcodeRawHeader");
				ivjstcLblBarcodeRawHeader.setFont(
					new java.awt.Font("Arial", 1, 12));
				ivjstcLblBarcodeRawHeader.setText("Barcode Raw Data:");
				ivjstcLblBarcodeRawHeader.setBounds(130, 27, 111, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblBarcodeRawHeader;
	}
	
	/**
	 * Return the ivjstcLblBkSlash property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBkSlash()
	{
		if (ivjstcLblBkSlash == null)
		{
			try
			{
				ivjstcLblBkSlash = new JLabel();
				ivjstcLblBkSlash.setName("ivjstcLblBkSlash");
				ivjstcLblBkSlash.setText("/");
				ivjstcLblBkSlash.setBounds(558, 216, 9, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblBkSlash;
	}

	/**
	 * This method initializes ivjstcLblBkSlash1
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBkSlash1()
	{
		if (ivjstcLblBkSlash1 == null)
		{
			ivjstcLblBkSlash1 = new JLabel();
			ivjstcLblBkSlash1.setBounds(756, 218, 8, 14);
			ivjstcLblBkSlash1.setText("/");
		}
		return ivjstcLblBkSlash1;
	}

	/**
	 * This method initializes ivjstcLblBkSlash2
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBkSlash2()
	{
		if (ivjstcLblBkSlash2 == null)
		{
			ivjstcLblBkSlash2 = new JLabel();
			ivjstcLblBkSlash2.setBounds(177, 513, 8, 14);
			ivjstcLblBkSlash2.setText("/");
		}
		return ivjstcLblBkSlash2;
	}

	/**
	 * This method initializes ivjstcLblBkSlash3
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBkSlash3()
	{
		if (ivjstcLblBkSlash3 == null)
		{
			ivjstcLblBkSlash3 = new JLabel();
			ivjstcLblBkSlash3.setBounds(177, 532, 8, 14);
			ivjstcLblBkSlash3.setText("/");
		}
		return ivjstcLblBkSlash3;
	}
	
	/**
	 * Return the ivjstcLblCntyNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCntyNo()
	{
		if (ivjstcLblCntyNo == null)
		{
			try
			{
				ivjstcLblCntyNo = new JLabel();
				ivjstcLblCntyNo.setName("ivjstcLblCntyNo");
				ivjstcLblCntyNo.setText("CntyNo:");
				ivjstcLblCntyNo.setBounds(472, 235, 46, 14);
				ivjstcLblCntyNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblCntyNo;
	}

	/**
	 * Return the ivjstcLblDocNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDocNo()
	{
		if (ivjstcLblDocNo == null)
		{
			try
			{
				ivjstcLblDocNo = new JLabel();
				ivjstcLblDocNo.setName("ivjstcLblDocNo");
				ivjstcLblDocNo.setAlignmentX(
					java.awt.Component.RIGHT_ALIGNMENT);
				ivjstcLblDocNo.setText("DocNo:");
				ivjstcLblDocNo.setBounds(188, 216, 43, 15);
				ivjstcLblDocNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblDocNo;
	}

	/**
	 * Return the ivjstcLblHeader property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblHeader()
	{
		if (ivjstcLblHeader == null)
		{
			try
			{
				ivjstcLblHeader = new JLabel();
				ivjstcLblHeader.setName("ivjstcLblHeader");
				ivjstcLblHeader.setFont(new java.awt.Font("Arial", 1, 18));
				ivjstcLblHeader.setText("Bar Code Data");
				ivjstcLblHeader.setBounds(312, 12, 131, 28);
				ivjstcLblHeader.setForeground(java.awt.Color.blue);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblHeader;
	}

	/**
	 * This method initializes ivjstcLblAddlSetIndi
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblNewPlatesRequiredCode()
	{
		if (ivjstcLblNewPlatesRequiredCode == null)
		{
			ivjstcLblNewPlatesRequiredCode = new JLabel();
			ivjstcLblNewPlatesRequiredCode.setSize(151, 15);
			ivjstcLblNewPlatesRequiredCode.setText(
				"NewPlatesRequiredCode:");
			ivjstcLblNewPlatesRequiredCode.setLocation(187, 492);
			ivjstcLblNewPlatesRequiredCode.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblNewPlatesRequiredCode;
	}

	/**
	 * This method initializes ivjstcLblNextPltExpMoYr
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblNextPltExpMoYr()
	{
		if (ivjstcLblNextPltExpMoYr == null)
		{
			ivjstcLblNextPltExpMoYr = new JLabel();
			ivjstcLblNextPltExpMoYr.setBounds(43, 532, 103, 14);
			ivjstcLblNextPltExpMoYr.setText("NextPltExpMo/Yr:");
			ivjstcLblNextPltExpMoYr.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblNextPltExpMoYr;
	}
	
	/**
	 * This method initializes ivjstcLblNextRegExpMoYr
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblNextRegExpMoYr()
	{
		if (ivjstcLblNextRegExpMoYr == null)
		{
			ivjstcLblNextRegExpMoYr = new JLabel();
			ivjstcLblNextRegExpMoYr.setBounds(618, 218, 108, 14);
			ivjstcLblNextRegExpMoYr.setText("NextRegExpMo/Yr:");
			ivjstcLblNextRegExpMoYr.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblNextRegExpMoYr;
	}

	/**
	 * This method initializes ivjstcLblOrgNoOrgName
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOrgNoOrgName()
	{
		if (ivjstcLblOrgNoOrgName == null)
		{
			ivjstcLblOrgNoOrgName = new JLabel();
			ivjstcLblOrgNoOrgName.setSize(102, 14);
			ivjstcLblOrgNoOrgName.setText("OrgNo / OrgName:");
			ivjstcLblOrgNoOrgName.setLocation(44, 473);
			ivjstcLblOrgNoOrgName.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblOrgNoOrgName;
	}
	
	/**
	 * Return the ivjstcLblPlateItemYr property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateItemYr()
	{
		if (ivjstcLblPlateItemYr == null)
		{
			try
			{
				ivjstcLblPlateItemYr = new JLabel();
				ivjstcLblPlateItemYr.setName("lblPlateItemYr");
				ivjstcLblPlateItemYr.setText("Plate Year:");
				ivjstcLblPlateItemYr.setBounds(528, 312, 65, 14);
				ivjstcLblPlateItemYr.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblPlateItemYr;
	}

	/**
	 * Return the ivjstcLblPlateRegPltCd property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateRegPltCd()
	{
		if (ivjstcLblPlateRegPltCd == null)
		{
			try
			{
				ivjstcLblPlateRegPltCd = new JLabel();
				ivjstcLblPlateRegPltCd.setName("ivjstcLblPlateRegPltCd");
				ivjstcLblPlateRegPltCd.setText("Plate Code:");
				ivjstcLblPlateRegPltCd.setBounds(164, 311, 67, 14);
				ivjstcLblPlateRegPltCd.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblPlateRegPltCd;
	}
	
	/**
	 * Return the ivjstcLblPlateStkrNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateStkrNo()
	{
		if (ivjstcLblPlateStkrNo == null)
		{
			try
			{
				ivjstcLblPlateStkrNo = new JLabel();
				ivjstcLblPlateStkrNo.setName("ivjstcLblPlateStkrNo");
				ivjstcLblPlateStkrNo.setText("Plate Number:");
				ivjstcLblPlateStkrNo.setBounds(320, 312, 82, 14);
				ivjstcLblPlateStkrNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblPlateStkrNo;
	}

	/**
	 * This method initializes ivjstcLblPlateValidityTerm
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateValidityTerm()
	{
		if (ivjstcLblPlateValidityTerm == null)
		{
			ivjstcLblPlateValidityTerm = new JLabel();
			ivjstcLblPlateValidityTerm.setBounds(230, 514, 108, 14);
			ivjstcLblPlateValidityTerm.setText("PlateValidityTerm:");
			ivjstcLblPlateValidityTerm.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblPlateValidityTerm;
	}

	/**
	 * This method initializes ivjstcLblPltExpMoYr
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPltExpMoYr()
	{
		if (ivjstcLblPltExpMoYr == null)
		{
			ivjstcLblPltExpMoYr = new JLabel();
			ivjstcLblPltExpMoYr.setBounds(69, 513, 77, 14);
			ivjstcLblPltExpMoYr.setText("PltExpMo/Yr:");
			ivjstcLblPltExpMoYr.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblPltExpMoYr;
	}

	/**
	 * Return the ivjstcLblPrntCntyNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPrntCntyNo()
	{
		if (ivjstcLblPrntCntyNo == null)
		{
			try
			{
				ivjstcLblPrntCntyNo = new JLabel();
				ivjstcLblPrntCntyNo.setName("ivjstcLblPrntCntyNo");
				ivjstcLblPrntCntyNo.setText("Print County Number:");
				ivjstcLblPrntCntyNo.setBounds(108, 297, 123, 12);
				ivjstcLblPrntCntyNo.setVisible(true);
				ivjstcLblPrntCntyNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblPrntCntyNo;
	}
	
	/**
	 * Return the ivjstcLblPrntRegClassCd property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPrntRegClassCd()
	{
		if (ivjstcLblPrntRegClassCd == null)
		{
			try
			{
				ivjstcLblPrntRegClassCd = new JLabel();
				ivjstcLblPrntRegClassCd.setName("ivjstcLblPrntRegClassCd");
				ivjstcLblPrntRegClassCd.setText("RegClassCd:");
				ivjstcLblPrntRegClassCd.setBounds(445, 276, 73, 15);
				ivjstcLblPrntRegClassCd.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblPrntRegClassCd;
	}

	/**
	 * Return the ivjstcLblPrntRegPltCd property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPrntRegPltCd()
	{
		if (ivjstcLblPrntRegPltCd == null)
		{
			try
			{
				ivjstcLblPrntRegPltCd = new JLabel();
				ivjstcLblPrntRegPltCd.setName("ivjstcLblPrntRegPltCd");
				ivjstcLblPrntRegPltCd.setText("RegPltCd:");
				ivjstcLblPrntRegPltCd.setBounds(460, 196, 58, 14);
				ivjstcLblPrntRegPltCd.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblPrntRegPltCd;
	}
	
	/**
	 * Return the ivjstcLblPrntRegPltNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPrntRegPltNo()
	{
		if (ivjstcLblPrntRegPltNo == null)
		{
			try
			{
				ivjstcLblPrntRegPltNo = new JLabel();
				ivjstcLblPrntRegPltNo.setName("ivjstcLblPrntRegPltNo");
				ivjstcLblPrntRegPltNo.setText("RegPltNo:");
				ivjstcLblPrntRegPltNo.setBounds(461, 235, 57, 14);
				ivjstcLblPrntRegPltNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblPrntRegPltNo;
	}

	/**
	 * Return the ivjstcLblPrntRegStkrCd property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPrntRegStkrCd()
	{
		if (ivjstcLblPrntRegStkrCd == null)
		{
			try
			{
				ivjstcLblPrntRegStkrCd = new JLabel();
				ivjstcLblPrntRegStkrCd.setName("ivjstcLblPrntRegStkrCd");
				ivjstcLblPrntRegStkrCd.setText("RegStkrCd:");
				ivjstcLblPrntRegStkrCd.setBounds(452, 216, 66, 14);
				ivjstcLblPrntRegStkrCd.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblPrntRegStkrCd;
	}
	
	/**
	 * Return the ivjstcLblRegClassCd property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegClassCd()
	{
		if (ivjstcLblRegClassCd == null)
		{
			try
			{
				ivjstcLblRegClassCd = new JLabel();
				ivjstcLblRegClassCd.setName("ivjstcLblRegClassCd");
				ivjstcLblRegClassCd.setText("RegClassCd:");
				ivjstcLblRegClassCd.setBounds(445, 255, 73, 15);
				ivjstcLblRegClassCd.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblRegClassCd;
	}

	/**
	 * Return the ivjstcLblRegExpDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegExpDate()
	{
		if (ivjstcLblRegExpDate == null)
		{
			try
			{
				ivjstcLblRegExpDate = new JLabel();
				ivjstcLblRegExpDate.setName("ivjstcLblRegExpDate");
				ivjstcLblRegExpDate.setText("RegExpDate:");
				ivjstcLblRegExpDate.setBounds(443, 255, 75, 14);
				ivjstcLblRegExpDate.setVisible(true);
				ivjstcLblRegExpDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblRegExpDate;
	}

	/**
	 * Return the ivjstcLblRegExpMoYr property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegExpMoYr()
	{
		if (ivjstcLblRegExpMoYr == null)
		{
			try
			{
				ivjstcLblRegExpMoYr = new JLabel();
				ivjstcLblRegExpMoYr.setName("ivjstcLblRegExpMoYr");
				ivjstcLblRegExpMoYr.setText("RegExpMo/Yr:");
				ivjstcLblRegExpMoYr.setBounds(439, 216, 79, 14);
				ivjstcLblRegExpMoYr.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblRegExpMoYr;
	}

	/**
	 * Return the ivjstcLblRegPltCd property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegPltCd()
	{
		if (ivjstcLblRegPltCd == null)
		{
			try
			{
				ivjstcLblRegPltCd = new JLabel();
				ivjstcLblRegPltCd.setName("ivjstcLblRegPltCd");
				ivjstcLblRegPltCd.setText("RegPltCd:");
				ivjstcLblRegPltCd.setBounds(173, 276, 58, 14);
				ivjstcLblRegPltCd.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblRegPltCd;
	}

	/**
	 * Return the ivjstcLblRegPltNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegPltNo()
	{
		if (ivjstcLblRegPltNo == null)
		{
			try
			{
				ivjstcLblRegPltNo = new JLabel();
				ivjstcLblRegPltNo.setName("ivjstcLblRegPltNo");
				ivjstcLblRegPltNo.setText("RegPltNo:");
				ivjstcLblRegPltNo.setBounds(461, 196, 57, 14);
				ivjstcLblRegPltNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblRegPltNo;
	}

	/**
	 * Return the ivjstcLblRegStkrCd property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegStkrCd()
	{
		if (ivjstcLblRegStkrCd == null)
		{
			try
			{
				ivjstcLblRegStkrCd = new JLabel();
				ivjstcLblRegStkrCd.setName("ivjstcLblRegStkrCd");
				ivjstcLblRegStkrCd.setText("RegStkrCd:");
				ivjstcLblRegStkrCd.setBounds(452, 176, 66, 14);
				ivjstcLblRegStkrCd.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblRegStkrCd;
	}

	/**
	 * Return the ivjstcLblResCompCntyNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblResCompCntyNo()
	{
		if (ivjstcLblResCompCntyNo == null)
		{
			try
			{
				ivjstcLblResCompCntyNo = new JLabel();
				ivjstcLblResCompCntyNo.setName("ivjstcLblResCompCntyNo");
				ivjstcLblResCompCntyNo.setText("ResComptCntyNo:");
				ivjstcLblResCompCntyNo.setBounds(125, 255, 106, 14);
				ivjstcLblResCompCntyNo.setVisible(true);
				ivjstcLblResCompCntyNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblResCompCntyNo;
	}

	/**
	 * Return the ivjstcLblStkrPrntDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblStkrPrntDate()
	{
		if (ivjstcLblStkrPrntDate == null)
		{
			try
			{
				ivjstcLblStkrPrntDate = new JLabel();
				ivjstcLblStkrPrntDate.setName("ivjstcLblStkrPrntDate");
				ivjstcLblStkrPrntDate.setText("Sticker Print Date:");
				ivjstcLblStkrPrntDate.setBounds(413, 176, 105, 14);
				ivjstcLblStkrPrntDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblStkrPrntDate;
	}
	
	/**
	 * Label for Total Fees.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTotalFees()
	{
		if (ivjstcLblTotalFees == null)
		{
			try
			{
				ivjstcLblTotalFees = new JLabel();
				ivjstcLblTotalFees.setSize(78, 14);
				ivjstcLblTotalFees.setName("ivjstcLblTotalFees");
				ivjstcLblTotalFees.setText("TOTAL FEES:");
				ivjstcLblTotalFees.setLocation(610, 492);
				ivjstcLblTotalFees.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblTotalFees;
	}

	/**
	 * Return the ivjstcLblType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblType()
	{
		if (ivjstcLblType == null)
		{
			try
			{
				ivjstcLblType = new JLabel();
				ivjstcLblType.setName("ivjstcLblType");
				ivjstcLblType.setText("Barcode Type:");
				ivjstcLblType.setBounds(148, 176, 83, 15);
				ivjstcLblType.setHorizontalAlignment(SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblType;
	}

	/**
	 * Return the ivjstcLblTypeOfBarCode property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTypeOfBarCode()
	{
		if (ivjstcLblTypeOfBarCode == null)
		{
			try
			{
				ivjstcLblTypeOfBarCode = new JLabel();
				ivjstcLblTypeOfBarCode.setName("ivjstcLblTypeOfBarCode");
				ivjstcLblTypeOfBarCode.setFont(
					new java.awt.Font("Arial", 1, 18));
				ivjstcLblTypeOfBarCode.setAlignmentX(
					java.awt.Component.CENTER_ALIGNMENT);
				ivjstcLblTypeOfBarCode.setText("");
				ivjstcLblTypeOfBarCode.setBounds(257, 144, 244, 20);
				ivjstcLblTypeOfBarCode.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblTypeOfBarCode;
	}

	/**
	 * Return the ivjstcLblVersion property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVersion()
	{
		if (ivjstcLblVersion == null)
		{
			try
			{
				ivjstcLblVersion = new JLabel();
				ivjstcLblVersion.setName("ivjstcLblVersion");
				ivjstcLblVersion.setText("Barcode Version:");
				ivjstcLblVersion.setBounds(131, 196, 100, 15);
				ivjstcLblVersion.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblVersion;
	}

	/**
	 * Return the ivjstcLblVIN property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVIN()
	{
		if (ivjstcLblVIN == null)
		{
			try
			{
				ivjstcLblVIN = new JLabel();
				ivjstcLblVIN.setName("ivjstcLblVIN");
				ivjstcLblVIN.setText("VIN:");
				ivjstcLblVIN.setBounds(206, 235, 25, 15);
				ivjstcLblVIN.setHorizontalAlignment(SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblVIN;
	}

	/**
	 * Return the ivjstcLblWsId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblWsId()
	{
		if (ivjstcLblWsId == null)
		{
			try
			{
				ivjstcLblWsId = new JLabel();
				ivjstcLblWsId.setName("ivjstcLblWsId");
				ivjstcLblWsId.setText("Workstation Id:");
				ivjstcLblWsId.setBounds(131, 277, 100, 12);
				ivjstcLblWsId.setVisible(true);
				ivjstcLblWsId.setHorizontalAlignment(SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblWsId;
	}

	/**
	 * Return the txtAcctItmCd1 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd1()
	{
		if (ivjtxtAcctItmCd1 == null)
		{
			try
			{
				ivjtxtAcctItmCd1 = new JTextField();
				ivjtxtAcctItmCd1.setName("txtAcctItmCd1");
				ivjtxtAcctItmCd1.setBounds(152, 331, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd1;
	}

	/**
	 * Return the txtAcctItmCd10 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd10()
	{
		if (ivjtxtAcctItmCd10 == null)
		{
			try
			{
				ivjtxtAcctItmCd10 = new JTextField();
				ivjtxtAcctItmCd10.setName("txtAcctItmCd10");
				ivjtxtAcctItmCd10.setBounds(568, 371, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd10;
	}

	/**
	 * Return the txtAcctItmCd11 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd11()
	{
		if (ivjtxtAcctItmCd11 == null)
		{
			try
			{
				ivjtxtAcctItmCd11 = new JTextField();
				ivjtxtAcctItmCd11.setName("txtAcctItmCd11");
				ivjtxtAcctItmCd11.setBounds(568, 394, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd11;
	}

	/**
	 * Return the txtAcctItmCd12 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd12()
	{
		if (ivjtxtAcctItmCd12 == null)
		{
			try
			{
				ivjtxtAcctItmCd12 = new JTextField();
				ivjtxtAcctItmCd12.setName("txtAcctItmCd12");
				ivjtxtAcctItmCd12.setBounds(568, 414, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd12;
	}

	/**
	 * Return the txtAcctItmCd13 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd13()
	{
		if (ivjtxtAcctItmCd13 == null)
		{
			try
			{
				ivjtxtAcctItmCd13 = new JTextField();
				ivjtxtAcctItmCd13.setName("txtAcctItmCd13");
				ivjtxtAcctItmCd13.setBounds(568, 434, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd13;
	}

	/**
	 * Return the txtAcctItmCd14 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd14()
	{
		if (ivjtxtAcctItmCd14 == null)
		{
			try
			{
				ivjtxtAcctItmCd14 = new JTextField();
				ivjtxtAcctItmCd14.setName("txtAcctItmCd14");
				ivjtxtAcctItmCd14.setBounds(568, 453, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd14;
	}

	/**
	 * Return the txtAcctItmCd15 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd15()
	{
		if (ivjtxtAcctItmCd15 == null)
		{
			try
			{
				ivjtxtAcctItmCd15 = new JTextField();
				ivjtxtAcctItmCd15.setName("txtAcctItmCd15");
				ivjtxtAcctItmCd15.setBounds(568, 473, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd15;
	}

	/**
	 * Return the txtAcctItmCd2 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd2()
	{
		if (ivjtxtAcctItmCd2 == null)
		{
			try
			{
				ivjtxtAcctItmCd2 = new JTextField();
				ivjtxtAcctItmCd2.setName("txtAcctItmCd2");
				ivjtxtAcctItmCd2.setBounds(152, 351, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd2;
	}

	/**
	 * Return the txtAcctItmCd3 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd3()
	{
		if (ivjtxtAcctItmCd3 == null)
		{
			try
			{
				ivjtxtAcctItmCd3 = new JTextField();
				ivjtxtAcctItmCd3.setName("txtAcctItmCd3");
				ivjtxtAcctItmCd3.setBounds(152, 371, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd3;
	}

	/**
	 * Return the txtAcctItmCd4 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd4()
	{
		if (ivjtxtAcctItmCd4 == null)
		{
			try
			{
				ivjtxtAcctItmCd4 = new JTextField();
				ivjtxtAcctItmCd4.setName("txtAcctItmCd4");
				ivjtxtAcctItmCd4.setBounds(152, 393, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd4;
	}

	/**
	 * Return the txtAcctItmCd5 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd5()
	{
		if (ivjtxtAcctItmCd5 == null)
		{
			try
			{
				ivjtxtAcctItmCd5 = new JTextField();
				ivjtxtAcctItmCd5.setName("txtAcctItmCd5");
				ivjtxtAcctItmCd5.setBounds(152, 413, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd5;
	}

	/**
	 * Return the txtAcctItmCd6 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd6()
	{
		if (ivjtxtAcctItmCd6 == null)
		{
			try
			{
				ivjtxtAcctItmCd6 = new JTextField();
				ivjtxtAcctItmCd6.setName("txtAcctItmCd6");
				ivjtxtAcctItmCd6.setBounds(152, 433, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd6;
	}
	/**
	 * Return the txtAcctItmCd7 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd7()
	{
		if (ivjtxtAcctItmCd7 == null)
		{
			try
			{
				ivjtxtAcctItmCd7 = new JTextField();
				ivjtxtAcctItmCd7.setName("txtAcctItmCd7");
				ivjtxtAcctItmCd7.setBounds(152, 452, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd7;
	}

	/**
	 * Return the txtAcctItmCd8 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd8()
	{
		if (ivjtxtAcctItmCd8 == null)
		{
			try
			{
				ivjtxtAcctItmCd8 = new JTextField();
				ivjtxtAcctItmCd8.setName("txtAcctItmCd8");
				ivjtxtAcctItmCd8.setBounds(568, 331, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd8;
	}

	/**
	 * Return the txtAcctItmCd9 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAcctItmCd9()
	{
		if (ivjtxtAcctItmCd9 == null)
		{
			try
			{
				ivjtxtAcctItmCd9 = new JTextField();
				ivjtxtAcctItmCd9.setName("txtAcctItmCd9");
				ivjtxtAcctItmCd9.setBounds(568, 352, 120, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAcctItmCd9;
	}

	/**
	 * This method initializes ivjtxtAddlSetIndi
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAddlSetIndi()
	{
		if (ivjtxtAddlSetIndi == null)
		{
			ivjtxtAddlSetIndi = new JTextField();
			ivjtxtAddlSetIndi.setSize(23, 15);
			ivjtxtAddlSetIndi.setLocation(152, 492);
		}
		return ivjtxtAddlSetIndi;
	}

	/**
	 * Return the txtAuditTrailCd property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtAuditTrailCd()
	{
		if (ivjtxtAuditTrailCd == null)
		{
			try
			{
				ivjtxtAuditTrailCd = new JTextField();
				ivjtxtAuditTrailCd.setName("txtAuditTrailCd");
				ivjtxtAuditTrailCd.setBounds(236, 255, 148, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtAuditTrailCd;
	}

	/**
	 * Return the txtBarcodeRaw property value.
	 * 
	 * @return JTextArea
	 */
	private JTextArea gettxtABarcodeRaw()
	{
		if (ivjtxtABarcodeRaw == null)
		{
			try
			{
				ivjtxtABarcodeRaw = new javax.swing.JTextArea();
				ivjtxtABarcodeRaw.setName("txtBarcodeRaw");
				ivjtxtABarcodeRaw.setLineWrap(true);
				ivjtxtABarcodeRaw.setWrapStyleWord(true);
				ivjtxtABarcodeRaw.setBounds(129, 52, 493, 82);
				ivjtxtABarcodeRaw.setRequestFocusEnabled(false);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtABarcodeRaw;
	}

	/**
	 * Return the txtBarcodeType property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtBarcodeType()
	{
		if (ivjtxtBarcodeType == null)
		{
			try
			{
				ivjtxtBarcodeType = new JTextField();
				ivjtxtBarcodeType.setName("txtBarcodeType");
				ivjtxtBarcodeType.setBounds(236, 176, 32, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtBarcodeType;
	}

	/**
	 * Return the txtBarcodeVersion property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtBarcodeVersion()
	{
		if (ivjtxtBarcodeVersion == null)
		{
			try
			{
				ivjtxtBarcodeVersion = new JTextField();
				ivjtxtBarcodeVersion.setName("txtBarcodeVersion");
				ivjtxtBarcodeVersion.setBounds(236, 196, 33, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtBarcodeVersion;
	}

	/**
	 * Return the txtCntyNo property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtCntyNo()
	{
		if (ivjtxtCntyNo == null)
		{
			try
			{
				ivjtxtCntyNo = new JTextField();
				ivjtxtCntyNo.setName("txtCntyNo");
				ivjtxtCntyNo.setBounds(521, 235, 51, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtCntyNo;
	}

	/**
	 * Return the txtDocType property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtDocType()
	{
		if (ivjtxtDocType == null)
		{
			try
			{
				ivjtxtDocType = new JTextField();
				ivjtxtDocType.setName("txtDocType");
				ivjtxtDocType.setBounds(236, 216, 148, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtDocType;
	}

	/**
	 * Return the txtItmPrice1 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice1()
	{
		if (ivjtxtItmPrice1 == null)
		{
			try
			{
				ivjtxtItmPrice1 = new JTextField();
				ivjtxtItmPrice1.setName("txtItmPrice1");
				ivjtxtItmPrice1.setBounds(296, 331, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice1;
	}

	/**
	 * Return the txtItmPrice10 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice10()
	{
		if (ivjtxtItmPrice10 == null)
		{
			try
			{
				ivjtxtItmPrice10 = new JTextField();
				ivjtxtItmPrice10.setName("txtItmPrice10");
				ivjtxtItmPrice10.setBounds(709, 372, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice10;
	}
	/**
	 * Return the txtItmPrice11 property value.
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice11()
	{
		if (ivjtxtItmPrice11 == null)
		{
			try
			{
				ivjtxtItmPrice11 = new JTextField();
				ivjtxtItmPrice11.setName("txtItmPrice11");
				ivjtxtItmPrice11.setBounds(709, 394, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice11;
	}

	/**
	 * Return the txtItmPrice12 property value.
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice12()
	{
		if (ivjtxtItmPrice12 == null)
		{
			try
			{
				ivjtxtItmPrice12 = new JTextField();
				ivjtxtItmPrice12.setName("txtItmPrice12");
				ivjtxtItmPrice12.setBounds(709, 414, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice12;
	}

	/**
	 * Return the txtItmPrice13 property value.
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice13()
	{
		if (ivjtxtItmPrice13 == null)
		{
			try
			{
				ivjtxtItmPrice13 = new JTextField();
				ivjtxtItmPrice13.setName("txtItmPrice13");
				ivjtxtItmPrice13.setBounds(709, 434, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice13;
	}

	/**
	 * Return the txtItmPrice14 property value.
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice14()
	{
		if (ivjtxtItmPrice14 == null)
		{
			try
			{
				ivjtxtItmPrice14 = new JTextField();
				ivjtxtItmPrice14.setName("txtItmPrice14");
				ivjtxtItmPrice14.setBounds(709, 453, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice14;
	}

	/**
	 * Return the txtItmPrice15 property value.
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice15()
	{
		if (ivjtxtItmPrice15 == null)
		{
			try
			{
				ivjtxtItmPrice15 = new JTextField();
				ivjtxtItmPrice15.setName("txtItmPrice15");
				ivjtxtItmPrice15.setBounds(709, 473, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice15;
	}

	/**
	 * Return the txtItmPrice2 property value.
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice2()
	{
		if (ivjtxtItmPrice2 == null)
		{
			try
			{
				ivjtxtItmPrice2 = new JTextField();
				ivjtxtItmPrice2.setName("txtItmPrice2");
				ivjtxtItmPrice2.setBounds(296, 351, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice2;
	}

	/**
	 * Return the txtItmPrice3 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice3()
	{
		if (ivjtxtItmPrice3 == null)
		{
			try
			{
				ivjtxtItmPrice3 = new JTextField();
				ivjtxtItmPrice3.setName("txtItmPrice3");
				ivjtxtItmPrice3.setBounds(296, 371, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice3;
	}

	/**
	 * Return the txtItmPrice4 property value.
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice4()
	{
		if (ivjtxtItmPrice4 == null)
		{
			try
			{
				ivjtxtItmPrice4 = new JTextField();
				ivjtxtItmPrice4.setName("txtItmPrice4");
				ivjtxtItmPrice4.setBounds(296, 393, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice4;
	}

	/**
	 * Return the txtItmPrice5 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice5()
	{
		if (ivjtxtItmPrice5 == null)
		{
			try
			{
				ivjtxtItmPrice5 = new JTextField();
				ivjtxtItmPrice5.setName("txtItmPrice5");
				ivjtxtItmPrice5.setBounds(296, 413, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice5;
	}

	/**
	 * Return the txtItmPrice6 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice6()
	{
		if (ivjtxtItmPrice6 == null)
		{
			try
			{
				ivjtxtItmPrice6 = new JTextField();
				ivjtxtItmPrice6.setName("txtItmPrice6");
				ivjtxtItmPrice6.setBounds(296, 433, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice6;
	}

	/**
	 * Return the txtItmPrice7 property value.
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice7()
	{
		if (ivjtxtItmPrice7 == null)
		{
			try
			{
				ivjtxtItmPrice7 = new JTextField();
				ivjtxtItmPrice7.setName("txtItmPrice7");
				ivjtxtItmPrice7.setBounds(296, 452, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice7;
	}

	/**
	 * Return the txtItmPrice8 property value.
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice8()
	{
		if (ivjtxtItmPrice8 == null)
		{
			try
			{
				ivjtxtItmPrice8 = new JTextField();
				ivjtxtItmPrice8.setName("txtItmPrice8");
				ivjtxtItmPrice8.setBounds(709, 331, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice8;
	}

	/**
	 * Return the txtItmPrice9 property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtItmPrice9()
	{
		if (ivjtxtItmPrice9 == null)
		{
			try
			{
				ivjtxtItmPrice9 = new JTextField();
				ivjtxtItmPrice9.setName("txtItmPrice9");
				ivjtxtItmPrice9.setBounds(709, 352, 75, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtItmPrice9;
	}

	/**
	 * This method initializes ivjtxtNewPlatesRequiredCode
	 * 
	 * @return RTSTextArea
	 */
	private JTextField gettxtNewPlatesRequiredCode()
	{
		if (ivjtxtNewPlatesRequiredCode == null)
		{
			try
			{
				ivjtxtNewPlatesRequiredCode = new JTextField();
				ivjtxtNewPlatesRequiredCode.setName(
					"NewPlatesRequiredCode");
				ivjtxtNewPlatesRequiredCode.setBounds(341, 493, 23, 15);
				// make the text bold
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtNewPlatesRequiredCode;
	}

	/**
	 * This method initializes ivjtxtNextPltExpMo
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtNextPltExpMo()
	{
		if (ivjtxtNextPltExpMo == null)
		{
			ivjtxtNextPltExpMo = new JTextField();
			ivjtxtNextPltExpMo.setBounds(152, 532, 23, 15);
		}
		return ivjtxtNextPltExpMo;
	}

	/**
	 * This method initializes ivjtxtNextPltExpYr
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtNextPltExpYr()
	{
		if (ivjtxtNextPltExpYr == null)
		{
			ivjtxtNextPltExpYr = new JTextField();
			ivjtxtNextPltExpYr.setBounds(182, 532, 40, 15);
		}
		return ivjtxtNextPltExpYr;
	}

	/**
	 * This method initializes ivjtxtNextRegExpMo
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtNextRegExpMo()
	{
		if (ivjtxtNextRegExpMo == null)
		{
			ivjtxtNextRegExpMo = new JTextField();
			ivjtxtNextRegExpMo.setBounds(730, 218, 23, 15);
		}
		return ivjtxtNextRegExpMo;
	}

	/**
	 * This method initializes ivjtxtNextRegExpYr
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtNextRegExpYr()
	{
		if (ivjtxtNextRegExpYr == null)
		{
			ivjtxtNextRegExpYr = new JTextField();
			ivjtxtNextRegExpYr.setBounds(763, 218, 40, 15);
		}
		return ivjtxtNextRegExpYr;
	}

	/**
	 * This method initializes ivjtxtOrgName
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtOrgName()
	{
		if (ivjtxtOrgName == null)
		{
			ivjtxtOrgName = new JTextField();
			ivjtxtOrgName.setSize(230, 15);
			ivjtxtOrgName.setLocation(188, 472);
		}
		return ivjtxtOrgName;
	}

	/**
	 * This method initializes ivjtxtOrgNo
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtOrgNo()
	{
		if (ivjtxtOrgNo == null)
		{
			ivjtxtOrgNo = new JTextField();
			ivjtxtOrgNo.setSize(34, 15);
			ivjtxtOrgNo.setLocation(152, 472);
		}
		return ivjtxtOrgNo;
	}

	/**
	 * Return the txtPlateItemNo property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtPlateItemYr()
	{
		if (ivjtxtPlateItemYr == null)
		{
			try
			{
				ivjtxtPlateItemYr = new JTextField();
				ivjtxtPlateItemYr.setName("txtPlateItemYr");
				ivjtxtPlateItemYr.setBounds(598, 311, 47, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtPlateItemYr;
	}

	/**
	 * Return the txtPlateRegPltNp property value.
	 * @return JTextField 
	 */
	private JTextField gettxtPlateRegPltCd()
	{
		if (ivjtxtPlateRegPltCd == null)
		{
			try
			{
				ivjtxtPlateRegPltCd = new JTextField();
				ivjtxtPlateRegPltCd.setName("txtPlateRegPltCd");
				ivjtxtPlateRegPltCd.setBounds(236, 311, 70, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtPlateRegPltCd;
	}

	/**
	 * Return the txtPlateStkrNo property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtPlateStkrNo()
	{
		if (ivjtxtPlateStkrNo == null)
		{
			try
			{
				ivjtxtPlateStkrNo = new JTextField();
				ivjtxtPlateStkrNo.setName("txtPlateStkrNo");
				ivjtxtPlateStkrNo.setBounds(407, 311, 70, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtPlateStkrNo;
	}

	/**
	 * This method initializes ivjtxtPlateValidityTerm
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtPlateValidityTerm()
	{
		if (ivjtxtPlateValidityTerm == null)
		{
			ivjtxtPlateValidityTerm = new JTextField();
			ivjtxtPlateValidityTerm.setBounds(341, 513, 23, 15);
		}
		return ivjtxtPlateValidityTerm;
	}

	/**
	 * This method initializes ivjtxtPltExpMo
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtPltExpMo()
	{
		if (ivjtxtPltExpMo == null)
		{
			ivjtxtPltExpMo = new JTextField();
			ivjtxtPltExpMo.setBounds(152, 513, 23, 15);
		}
		return ivjtxtPltExpMo;
	}

	/**
	 * This method initializes ivjtxtPltExpYr
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtPltExpYr()
	{
		if (ivjtxtPltExpYr == null)
		{
			ivjtxtPltExpYr = new JTextField();
			ivjtxtPltExpYr.setBounds(182, 513, 40, 15);
		}
		return ivjtxtPltExpYr;
	}

	/**
	 * Return the txtPrntCntyNo property value.
	 * @return JTextField 
	 */
	private JTextField gettxtPrntCntyNo()
	{
		if (ivjtxtPrntCntyNo == null)
		{
			try
			{
				ivjtxtPrntCntyNo = new JTextField();
				ivjtxtPrntCntyNo.setName("txtPrntCntyNo");
				ivjtxtPrntCntyNo.setBounds(236, 296, 51, 15);
				ivjtxtPrntCntyNo.setVisible(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtPrntCntyNo;
	}

	/**
	 * Return the txtPrntRegClasasCd property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtPrntRegClassCd()
	{
		if (ivjtxtPrntRegClassCd == null)
		{
			try
			{
				ivjtxtPrntRegClassCd = new JTextField();
				ivjtxtPrntRegClassCd.setName("txtPrntRegClassCd");
				ivjtxtPrntRegClassCd.setBounds(521, 276, 56, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtPrntRegClassCd;
	}

	/**
	 * Return the txtPrntRegPltCd property value.
	 * @return JTextField 
	 */
	private JTextField gettxtPrntRegPltCd()
	{
		if (ivjtxtPrntRegPltCd == null)
		{
			try
			{
				ivjtxtPrntRegPltCd = new JTextField();
				ivjtxtPrntRegPltCd.setName("txtPrntRegPltCd");
				ivjtxtPrntRegPltCd.setBounds(523, 196, 88, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtPrntRegPltCd;
	}

	/**
	 * Return the txtPrntRegPltNo property value.
	 * @return JTextField 
	 */
	private JTextField gettxtPrntRegPltNo()
	{
		if (ivjtxtPrntRegPltNo == null)
		{
			try
			{
				ivjtxtPrntRegPltNo = new JTextField();
				ivjtxtPrntRegPltNo.setName("txtPrntRegPltNo");
				ivjtxtPrntRegPltNo.setBounds(523, 235, 91, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtPrntRegPltNo;
	}

	/**
	 * Return the txtPrntRegStkrCd property value.
	 * @return JTextField 
	 */
	private JTextField gettxtPrntRegStkrCd()
	{
		if (ivjtxtPrntRegStkrCd == null)
		{
			try
			{
				ivjtxtPrntRegStkrCd = new JTextField();
				ivjtxtPrntRegStkrCd.setName("txtPrntRegStkrCd");
				ivjtxtPrntRegStkrCd.setBounds(523, 216, 88, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtPrntRegStkrCd;
	}

	/**
	 * Return the txtPrntWsId property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtPrntWsId()
	{
		if (ivjtxtPrntWsId == null)
		{
			try
			{
				ivjtxtPrntWsId = new JTextField();
				ivjtxtPrntWsId.setName("txtPrntWsId");
				ivjtxtPrntWsId.setBounds(236, 276, 88, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtPrntWsId;
	}

	/**
	 * Return the txtRegClassCd property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtRegClassCd()
	{
		if (ivjtxtRegClassCd == null)
		{
			try
			{
				ivjtxtRegClassCd = new JTextField();
				ivjtxtRegClassCd.setName("txtRegClassCd");
				ivjtxtRegClassCd.setBounds(521, 255, 56, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtRegClassCd;
	}

	/**
	 * Return the txtRegExpMo property value.
	 * @return JTextField 
	 */
	private JTextField gettxtRegExpDate()
	{
		if (ivjtxtRegExpDate == null)
		{
			try
			{
				ivjtxtRegExpDate = new JTextField();
				ivjtxtRegExpDate.setName("txtRegExpDate");
				ivjtxtRegExpDate.setBounds(521, 255, 70, 15);
				ivjtxtRegExpDate.setVisible(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtRegExpDate;
	}

	/**
	 * Return the txtRegExpMo property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtRegExpMo()
	{
		if (ivjtxtRegExpMo == null)
		{
			try
			{
				ivjtxtRegExpMo = new JTextField();
				ivjtxtRegExpMo.setName("txtRegExpMo");
				ivjtxtRegExpMo.setBounds(521, 216, 31, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtRegExpMo;
	}

	/**
	 * Return the txtRegExpYr property value.
	 * @return JTextField 
	 */
	private JTextField gettxtRegExpYr()
	{
		if (ivjtxtRegExpYr == null)
		{
			try
			{
				ivjtxtRegExpYr = new JTextField();
				ivjtxtRegExpYr.setName("txtRegExpYr");
				ivjtxtRegExpYr.setText("");
				ivjtxtRegExpYr.setBounds(568, 216, 42, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtRegExpYr;
	}

	/**
	 * Return the txtRegPltCd property value.
	 * @return JTextField 
	 */
	private JTextField gettxtRegPltCd()
	{
		if (ivjtxtRegPltCd == null)
		{
			try
			{
				ivjtxtRegPltCd = new JTextField();
				ivjtxtRegPltCd.setName("txtRegPltCd");
				ivjtxtRegPltCd.setBounds(236, 276, 88, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtRegPltCd;
	}

	/**
	 * Return the txtRegPltNo property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtRegPltNo()
	{
		if (ivjtxtRegPltNo == null)
		{
			try
			{
				ivjtxtRegPltNo = new JTextField();
				ivjtxtRegPltNo.setName("txtRegPltNo");
				ivjtxtRegPltNo.setBounds(521, 196, 91, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtRegPltNo;
	}

	/**
	 * Return the txtRegStkrCd property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtRegStkrCd()
	{
		if (ivjtxtRegStkrCd == null)
		{
			try
			{
				ivjtxtRegStkrCd = new JTextField();
				ivjtxtRegStkrCd.setName("txtRegStkrCd");
				ivjtxtRegStkrCd.setBounds(521, 176, 43, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtRegStkrCd;
	}

	/**
	 * Return the txtResCompCntyNo property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtResCompCntyNo()
	{
		if (ivjtxtResCompCntyNo == null)
		{
			try
			{
				ivjtxtResCompCntyNo = new JTextField();
				ivjtxtResCompCntyNo.setName("txtResCompCntyNo");
				ivjtxtResCompCntyNo.setBounds(236, 255, 51, 15);
				ivjtxtResCompCntyNo.setVisible(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtResCompCntyNo;
	}

	/**
	 * Return the txtStkrPrntDate property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtStkrPrntDate()
	{
		if (ivjtxtStkrPrntDate == null)
		{
			try
			{
				ivjtxtStkrPrntDate = new JTextField();
				ivjtxtStkrPrntDate.setName("txtStkrPrntDate");
				ivjtxtStkrPrntDate.setBounds(523, 176, 64, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtStkrPrntDate;
	}

	/**
	 * Return the txtVIN property value.
	 * 
	 * @return JTextField 
	 */
	private JTextField gettxtVIN()
	{
		if (ivjtxtVIN == null)
		{
			try
			{
				ivjtxtVIN = new JTextField();
				ivjtxtVIN.setName("txtVIN");
				ivjtxtVIN.setBounds(236, 235, 148, 15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtVIN;
	}

	/**
	 * Set Item Prices from BarcodeRaw Data.
	 * 
	 */
	private void getUnformattedItmpriceData()
	{
		String lsBarCodeStr = barcodescanner.getRawBarcodeData();

		csItmPrice1 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_ACCT15_END,
				BarCodeScanner.RENEWAL_PRICE1_END);
		csItmPrice2 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE1_END,
				BarCodeScanner.RENEWAL_PRICE2_END);
		csItmPrice3 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE2_END,
				BarCodeScanner.RENEWAL_PRICE3_END);
		csItmPrice4 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE3_END,
				BarCodeScanner.RENEWAL_PRICE4_END);
		csItmPrice5 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE4_END,
				BarCodeScanner.RENEWAL_PRICE5_END);
		csItmPrice6 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE5_END,
				BarCodeScanner.RENEWAL_PRICE6_END);
		csItmPrice7 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE6_END,
				BarCodeScanner.RENEWAL_PRICE7_END);
		csItmPrice8 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE7_END,
				BarCodeScanner.RENEWAL_PRICE8_END);
		csItmPrice9 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE8_END,
				BarCodeScanner.RENEWAL_PRICE9_END);
		csItmPrice10 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE9_END,
				BarCodeScanner.RENEWAL_PRICE10_END);
		csItmPrice11 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE10_END,
				BarCodeScanner.RENEWAL_PRICE11_END);
		csItmPrice12 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE11_END,
				BarCodeScanner.RENEWAL_PRICE12_END);
		csItmPrice13 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE12_END,
				BarCodeScanner.RENEWAL_PRICE13_END);
		csItmPrice14 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE13_END,
				BarCodeScanner.RENEWAL_PRICE14_END);
		csItmPrice15 =
			lsBarCodeStr.substring(
				BarCodeScanner.RENEWAL_PRICE14_END,
				BarCodeScanner.RENEWAL_PRICE15_END);

	}

	/**
	 * Invoked when a CommEvent occurs
	 * @param e com.txdot.isd.rts.services.util.event.CommEvent
	 */
	public void handleCommEvent(
		com.txdot.isd.rts.services.util.event.CommEvent e)
	{
	}

	/**
	 * Called whenever the part throws an exception.
	 * @param exception Throwable
	 */
	private void handleException(Throwable exception)
	{

		/* Uncomment the following lines to print uncaught exceptions to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
	}

	/**
	 * Populate screen data based on Renewal bar code data
	 * 
	 * @param aStickerBarCodeData com.txdot.isd.rts.client.registration.ui.StickerBarCodeData
	 */
	private void handleStkrPrntBarCode(StickerPrintingBarCodeData aStkrPrntBarCodeData)
		throws RTSException
	{
		try
		{
			synchronized (this)
			{
				// check if any parsing error
				if (aStkrPrntBarCodeData.getResCompCntyNo() == null
					|| aStkrPrntBarCodeData
						.getResCompCntyNo()
						.trim()
						.equals(
						"")
					|| aStkrPrntBarCodeData.getRegExpDate() == null
					|| aStkrPrntBarCodeData.getRegExpDate().trim().equals(
						""))
				{
					System.out.println("info is null");
					displayScannerMsg();
				}
				else
				{
					try
					{
						int i =
							Integer.parseInt(
								aStkrPrntBarCodeData
									.getResCompCntyNo());
						if (i == 0)
						{
							displayScannerMsg();
						}
					}
					catch (NumberFormatException e)
					{
						displayScannerMsg();
					}
				}
				//if plate number is OLDPLTX pop up mesage
				if (aStkrPrntBarCodeData.getRegisPltCd() != null
					&& aStkrPrntBarCodeData.getRegisPltCd().equals(
						"OLDPLTX"))
				{
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						"Plate code is OLDPLTX.  Please use renewal event to continue.",
						"");
				}
				cStkrPrntBarCodeData = aStkrPrntBarCodeData;
				//automatically add transaction if everything is filled out 
				//automaticAdd();
			}
			if (renewalQueue.size() > 0)
			{
				StickerPrintingBarCodeData data =
					(StickerPrintingBarCodeData) renewalQueue.remove(0);
				handleStkrPrntBarCode(data);
			}
		}
		finally
		{
		}
	}

	/**
	 * Invoked when a thread event should occur.
	 * @param e com.txdot.isd.rts.services.util.event.ThreadEvent
	 */
	public void handleThreadEvent(
		com.txdot.isd.rts.services.util.event.ThreadEvent e)
	{
	}

	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName("FrmBarcodeReader");
			setDefaultCloseOperation(2);
			setBounds(new java.awt.Rectangle(0, 0, 818, 630));
			setSize(822, 590);
			setTitle("Barcode Reader");
			setContentPane(getFrmBarcodeReaderContentPane1());
		}
		catch (Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

//	/**
//	 * Invoked when an item has been selected or deselected.
//	 * The code written for this method performs the operations
//	 * that need to occur when an item is selected (or deselected).
//	 */
//	public void itemStateChanged(ItemEvent e)
//	{
//		System.out.println("Barcode read ItemListener");
//
//	}

	/**
	 * Sets Renewal data screen objects for Barcode Version 5 
	 * 
	 */
	public void setBarcodeVersion5Data()
	{
		String lsRegPltCd = caRenewalBarCodeData.getRegPltCd().trim();
		String lsOrgNo = caRenewalBarCodeData.getOrgNo();
		gettxtOrgNo().setText(lsOrgNo);
		String lsOrgName =
			OrganizationNumberCache.getOrgName(lsRegPltCd, lsOrgNo);
		gettxtOrgName().setText(lsOrgName);
		int liAddlSetIndi = caRenewalBarCodeData.getAddlSetIndi();
		gettxtAddlSetIndi().setText("" + liAddlSetIndi);
	}

	/**
	 * All subclasses must implement this method - 
	 *	it sets the data on the screen and is how the controller 
	 *	relays information to the view
	 */
	public void setData(Object dataObject)
	{
		try
		{ // Set Barcode scan thread 
			barcodescanner = new BarCodeScanner();
			threadBarCode = new Thread(barcodescanner);
			threadBarCode.setDaemon(true);
			threadBarCode.setPriority(Thread.MAX_PRIORITY);
			barcodescanner.addBarCodeListener(this);
			barcodescanner = getBarCodeScanner();
			threadBarCode.start();

			clearFields();
			turnOnOffPrntCntyNo(false);
		}
		catch (RTSException rtsException) // 5827
		{
			rtsException.displayError(this);
		}
	}

	/**
	 * Add Renewal data to data object. BarcodeScanner does not pass 
	 * RenewalBarCodeData.
	 * 
	 * @throws RTSException 
	 */
	public void setRenewalExceptData() throws RTSException
	{
		caRenewalBarCodeData = new RenewalBarCodeData();
		String csRenewalBarCodeDataString =
			new String(barcodescanner.getRawBarcodeData());
		caRenewalBarCodeData.setType(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.BARCODE_TYPE_START,
				BarCodeScanner.BARCODE_TYPE_END));
		caRenewalBarCodeData.setVersion(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.BARCODE_TYPE_END,
				BarCodeScanner.BARCODE_VERSION_END));
		caRenewalBarCodeData.setDocNo(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_DOCNO_START,
				BarCodeScanner.RENEWAL_DOCNO_END));
		caRenewalBarCodeData.setVin(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_DOCNO_END,
				BarCodeScanner.RENEWAL_VIN_END));
		caRenewalBarCodeData.setAuditTrailTransId(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_VIN_END,
				BarCodeScanner.RENEWAL_AUDIT_END));
		caRenewalBarCodeData.setRegPltCd(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_AUDIT_END,
				BarCodeScanner.RENEWAL_REGPLTCD_END));
		caRenewalBarCodeData.setRegStkrCd(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_REGPLTCD_END,
				BarCodeScanner.RENEWAL_STKRCD_END));
		caRenewalBarCodeData.setRegPltNo(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_STKRCD_END,
				BarCodeScanner.RENEWAL_REGPLTNO_END));
		caRenewalBarCodeData.setRegExpMo(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_REGPLTNO_END,
				BarCodeScanner.RENEWAL_EXPMO_END));
		caRenewalBarCodeData.setRegExpYr(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_EXPMO_END,
				BarCodeScanner.RENEWAL_EXPYR_END));
		caRenewalBarCodeData.setCntyNo(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_EXPYR_END,
				BarCodeScanner.RENEWAL_CNTYNO_END));
		caRenewalBarCodeData.setAcctItmCd1(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_CNTYNO_END,
				BarCodeScanner.RENEWAL_ACCT1_END));
		caRenewalBarCodeData.setAcctItmCd2(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT1_END,
				BarCodeScanner.RENEWAL_ACCT2_END));
		caRenewalBarCodeData.setAcctItmCd3(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT2_END,
				BarCodeScanner.RENEWAL_ACCT3_END));
		caRenewalBarCodeData.setAcctItmCd4(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT3_END,
				BarCodeScanner.RENEWAL_ACCT4_END));
		caRenewalBarCodeData.setAcctItmCd5(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT4_END,
				BarCodeScanner.RENEWAL_ACCT5_END));
		caRenewalBarCodeData.setAcctItmCd6(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT5_END,
				BarCodeScanner.RENEWAL_ACCT6_END));
		caRenewalBarCodeData.setAcctItmCd7(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT6_END,
				BarCodeScanner.RENEWAL_ACCT7_END));
		caRenewalBarCodeData.setAcctItmCd8(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT7_END,
				BarCodeScanner.RENEWAL_ACCT8_END));
		caRenewalBarCodeData.setAcctItmCd9(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT8_END,
				BarCodeScanner.RENEWAL_ACCT9_END));
		caRenewalBarCodeData.setAcctItmCd10(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT9_END,
				BarCodeScanner.RENEWAL_ACCT10_END));
		caRenewalBarCodeData.setAcctItmCd11(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT10_END,
				BarCodeScanner.RENEWAL_ACCT11_END));
		caRenewalBarCodeData.setAcctItmCd12(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT11_END,
				BarCodeScanner.RENEWAL_ACCT12_END));
		caRenewalBarCodeData.setAcctItmCd13(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT12_END,
				BarCodeScanner.RENEWAL_ACCT13_END));
		caRenewalBarCodeData.setAcctItmCd14(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT13_END,
				BarCodeScanner.RENEWAL_ACCT14_END));
		caRenewalBarCodeData.setAcctItmCd15(
			csRenewalBarCodeDataString.substring(
				BarCodeScanner.RENEWAL_ACCT14_END,
				BarCodeScanner.RENEWAL_ACCT15_END));
		try
		{
			if (caRenewalBarCodeData.getItmPrice1() != null)
			{
				ciRnwlExceptErrFld = 1;
				caRenewalBarCodeData.setItmPrice1(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_ACCT15_END,
							BarCodeScanner.RENEWAL_PRICE1_END)));
			}
			if (caRenewalBarCodeData.getItmPrice2() != null)
			{
				ciRnwlExceptErrFld = 2;
				caRenewalBarCodeData.setItmPrice2(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE1_END,
							BarCodeScanner.RENEWAL_PRICE2_END)));
			}
			if (caRenewalBarCodeData.getItmPrice3() != null)
			{
				ciRnwlExceptErrFld = 3;
				caRenewalBarCodeData.setItmPrice3(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE2_END,
							BarCodeScanner.RENEWAL_PRICE3_END)));
			}
			if (caRenewalBarCodeData.getItmPrice4() != null)
			{
				ciRnwlExceptErrFld = 4;
				caRenewalBarCodeData.setItmPrice4(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE3_END,
							BarCodeScanner.RENEWAL_PRICE4_END)));
			}
			if (caRenewalBarCodeData.getItmPrice5() != null)
			{
				ciRnwlExceptErrFld = 5;
				caRenewalBarCodeData.setItmPrice5(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE4_END,
							BarCodeScanner.RENEWAL_PRICE5_END)));
			}
			if (caRenewalBarCodeData.getItmPrice6() != null)
			{
				ciRnwlExceptErrFld = 6;
				caRenewalBarCodeData.setItmPrice6(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE5_END,
							BarCodeScanner.RENEWAL_PRICE6_END)));
			}
			if (caRenewalBarCodeData.getItmPrice7() != null)
			{
				ciRnwlExceptErrFld = 7;
				caRenewalBarCodeData.setItmPrice7(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE6_END,
							BarCodeScanner.RENEWAL_PRICE7_END)));
			}
			if (caRenewalBarCodeData.getItmPrice8() != null)
			{
				ciRnwlExceptErrFld = 8;
				caRenewalBarCodeData.setItmPrice8(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE7_END,
							BarCodeScanner.RENEWAL_PRICE8_END)));
			}
			if (caRenewalBarCodeData.getItmPrice9() != null)
			{
				ciRnwlExceptErrFld = 9;
				caRenewalBarCodeData.setItmPrice9(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE8_END,
							BarCodeScanner.RENEWAL_PRICE9_END)));
			}
			if (caRenewalBarCodeData.getItmPrice10() != null)
			{
				ciRnwlExceptErrFld = 10;
				caRenewalBarCodeData.setItmPrice10(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE9_END,
							BarCodeScanner.RENEWAL_PRICE10_END)));
			}
			if (caRenewalBarCodeData.getItmPrice11() != null)
			{
				ciRnwlExceptErrFld = 11;
				caRenewalBarCodeData.setItmPrice11(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE10_END,
							BarCodeScanner.RENEWAL_PRICE11_END)));
			}
			if (caRenewalBarCodeData.getItmPrice12() != null)
			{
				ciRnwlExceptErrFld = 12;
				caRenewalBarCodeData.setItmPrice12(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE11_END,
							BarCodeScanner.RENEWAL_PRICE12_END)));
			}
			if (caRenewalBarCodeData.getItmPrice13() != null)
			{
				ciRnwlExceptErrFld = 13;
				caRenewalBarCodeData.setItmPrice13(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE12_END,
							BarCodeScanner.RENEWAL_PRICE13_END)));
			}
			if (caRenewalBarCodeData.getItmPrice14() != null)
			{
				ciRnwlExceptErrFld = 14;
				caRenewalBarCodeData.setItmPrice14(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE13_END,
							BarCodeScanner.RENEWAL_PRICE14_END)));
			}
			if (caRenewalBarCodeData.getItmPrice15() != null)
			{
				ciRnwlExceptErrFld = 15;
				caRenewalBarCodeData.setItmPrice15(
					new Dollar(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_PRICE14_END,
							BarCodeScanner.RENEWAL_PRICE15_END)));
			}
			// RegClassCd
			ciRnwlExceptErrFld = 16;
			caRenewalBarCodeData.setRegClassCd(
				Integer.parseInt(
					csRenewalBarCodeDataString.substring(
						BarCodeScanner.RENEWAL_PRICE15_END,
						BarCodeScanner.RENEWAL_REGCLASS_END)));

			// OrgNo, AddlSetIndi 
			if (caRenewalBarCodeData
				.getVersion()
				.equals(BarCodeScanner.BARCODE_VERSION05))
			{
				ciRnwlExceptErrFld = 17;
				String lsOrgNo =
					csRenewalBarCodeDataString.substring(
						BarCodeScanner.RENEWAL_REGCLASS_END,
						BarCodeScanner.RENEWAL_ORGNO_END);
				if (UtilityMethods.isAllZeros(lsOrgNo))
				{
					lsOrgNo = "";
				}
				caRenewalBarCodeData.setOrgNo(lsOrgNo);
				ciRnwlExceptErrFld = 18;
				caRenewalBarCodeData.setAddlSetIndi(
					Integer.parseInt(
						csRenewalBarCodeDataString.substring(
							BarCodeScanner.RENEWAL_ORGNO_END,
							BarCodeScanner.RENEWAL_ADDLSETINDI_END)));
			}

		}

		catch (NumberFormatException aeNFEx)
		{
			String lsErrMsg = "";
			lsErrMsg =
				"Item Price " + String.valueOf(ciRnwlExceptErrFld);

			System.out.println(
				"Number Format error, RenewalExceptionData");
			System.out.println("	refer to: " + lsErrMsg);
		}
	}

	/**
	 * Invoked when the target of the listener has changed its state.
	 *
	 * @param e  a ChangeEvent object
	 */
	public void stateChanged(javax.swing.event.ChangeEvent e)
	{
	}

	/**
	 * Returns a String that represents the value of this object.
	 * @return a string representation of the receiver
	 */
	public String toString()
	{
		// Insert code to print the receiver here.
		// This implementation forwards the message to super. You may replace or supplement this.
		return super.toString();
	}

	/**
	 * Total fees from RenewalBarCodeData
	 * 
	 * @param aRenewalBarCodeData RenewalBarCodeData
	 */
	private void totalFees(RenewalBarCodeData aRenewalBarCodeData)
		throws RTSException
	{
		try
		{
			synchronized (this)
			{
				Dollar ldFees = new Dollar("0.00");
				ldFees = ldFees.add(aRenewalBarCodeData.getItmPrice1());
				ldFees = ldFees.add(aRenewalBarCodeData.getItmPrice2());
				ldFees = ldFees.add(aRenewalBarCodeData.getItmPrice3());
				ldFees = ldFees.add(aRenewalBarCodeData.getItmPrice4());
				ldFees = ldFees.add(aRenewalBarCodeData.getItmPrice5());
				ldFees = ldFees.add(aRenewalBarCodeData.getItmPrice6());
				ldFees = ldFees.add(aRenewalBarCodeData.getItmPrice7());
				ldFees = ldFees.add(aRenewalBarCodeData.getItmPrice8());
				ldFees = ldFees.add(aRenewalBarCodeData.getItmPrice9());
				ldFees =
					ldFees.add(aRenewalBarCodeData.getItmPrice10());
				ldFees =
					ldFees.add(aRenewalBarCodeData.getItmPrice11());
				ldFees =
					ldFees.add(aRenewalBarCodeData.getItmPrice12());
				ldFees =
					ldFees.add(aRenewalBarCodeData.getItmPrice13());
				ldFees =
					ldFees.add(aRenewalBarCodeData.getItmPrice14());
				ldFees =
					ldFees.add(aRenewalBarCodeData.getItmPrice15());
				caRenewalBarCodeData.setRenwlPrice(ldFees);
			}
		}
		finally
		{
		}
	}

	/**
	 * Turn on / off Sticker and Renewal fields.
	 * Creation date: (6/2/2004 10:41:28 AM)
	 */
	public void turnOffOnStkrRenwl(boolean abShow)
	{
		// Show\Hide registration info
		getstcLblRegStkrCd().setVisible(abShow);
		gettxtRegStkrCd().setVisible(abShow);
		getstcLblStkrPrntDate().setVisible(abShow);
		gettxtStkrPrntDate().setVisible(abShow);
		getstcLblRegPltNo().setVisible(abShow);
		gettxtRegPltNo().setVisible(abShow);
		getstcLblPrntRegPltCd().setVisible(abShow);
		gettxtPrntRegPltCd().setVisible(abShow);
		getstcLblRegExpMoYr().setVisible(abShow);
		gettxtRegExpMo().setVisible(abShow);
		getstcLblBkSlash().setVisible(abShow);
		gettxtRegExpYr().setVisible(abShow);
		getstcLblPrntRegStkrCd().setVisible(abShow);
		gettxtPrntRegStkrCd().setVisible(abShow);
		getstcLblCntyNo().setVisible(abShow);
		gettxtCntyNo().setVisible(abShow);
		getstcLblPrntRegPltNo().setVisible(abShow);
		gettxtPrntRegPltNo().setVisible(abShow);
		getstcLblAuditTrailTransId().setVisible(abShow);
		gettxtAuditTrailCd().setVisible(abShow);
		getstcLblResCompCntyNo().setVisible(abShow);
		gettxtResCompCntyNo().setVisible(abShow);
		getstcLblRegExpDate().setVisible(abShow);
		gettxtRegExpDate().setVisible(abShow);
		getstcLblRegClassCd().setVisible(abShow);
		gettxtRegClassCd().setVisible(abShow);
		getstcLblRegPltCd().setVisible(abShow);
		gettxtRegPltCd().setVisible(abShow);
		getstcLblWsId().setVisible(abShow);
		gettxtPrntWsId().setVisible(abShow);
		getstcLblPrntRegClassCd().setVisible(abShow);
		gettxtPrntRegClassCd().setVisible(abShow);
		getstcLblPrntCntyNo().setVisible(abShow);
		gettxtPrntCntyNo().setVisible(abShow);

		// Turn off AcctItmCds and ItmPrices
		turnOnOffAcctItmFees(abShow);

		// defect 10464 
		turnOnOffNewRenwlFlds(abShow);
		// end defect 10464 
	}

	/**
	 * Turn on / off Account Item Codes and Item Prices.
	 * Creation date: (6/2/2004 10:41:28 AM)
	 */
	public void turnOnOffAcctItmFees(boolean abShow)
	{
		getstcLblAcctItmCd1().setVisible(abShow);
		gettxtAcctItmCd1().setVisible(abShow);
		gettxtItmPrice1().setVisible(abShow);
		getstcLblAcctItmCd2().setVisible(abShow);
		gettxtAcctItmCd2().setVisible(abShow);
		gettxtItmPrice2().setVisible(abShow);
		getstcLblAcctItmCd3().setVisible(abShow);
		gettxtAcctItmCd3().setVisible(abShow);
		gettxtItmPrice3().setVisible(abShow);
		getstcLblAcctItmCd4().setVisible(abShow);
		gettxtAcctItmCd4().setVisible(abShow);
		gettxtItmPrice4().setVisible(abShow);
		getstcLblAcctItmCd5().setVisible(abShow);
		gettxtAcctItmCd5().setVisible(abShow);
		gettxtItmPrice5().setVisible(abShow);
		getstcLblAcctItmCd6().setVisible(abShow);
		gettxtAcctItmCd6().setVisible(abShow);
		gettxtItmPrice6().setVisible(abShow);
		getstcLblAcctItmCd7().setVisible(abShow);
		gettxtAcctItmCd7().setVisible(abShow);
		gettxtItmPrice7().setVisible(abShow);

		getstcLblAcctItmCd8().setVisible(abShow);
		gettxtAcctItmCd8().setVisible(abShow);
		gettxtItmPrice8().setVisible(abShow);
		getstcLblAcctItmCd9().setVisible(abShow);
		gettxtAcctItmCd9().setVisible(abShow);
		gettxtItmPrice9().setVisible(abShow);
		getstcLblAcctItmCd10().setVisible(abShow);
		gettxtAcctItmCd10().setVisible(abShow);
		gettxtItmPrice10().setVisible(abShow);
		getstcLblAcctItmCd11().setVisible(abShow);
		gettxtAcctItmCd11().setVisible(abShow);
		gettxtItmPrice11().setVisible(abShow);
		getstcLblAcctItmCd12().setVisible(abShow);
		gettxtAcctItmCd12().setVisible(abShow);
		gettxtItmPrice12().setVisible(abShow);
		getstcLblAcctItmCd13().setVisible(abShow);
		gettxtAcctItmCd13().setVisible(abShow);
		gettxtItmPrice13().setVisible(abShow);
		getstcLblAcctItmCd14().setVisible(abShow);
		gettxtAcctItmCd14().setVisible(abShow);
		gettxtItmPrice14().setVisible(abShow);
		getstcLblAcctItmCd15().setVisible(abShow);
		gettxtAcctItmCd15().setVisible(abShow);
		gettxtItmPrice15().setVisible(abShow);
		getstcLblTotalFees().setVisible(abShow);
		getlblTotalFees().setVisible(abShow);
	}

	/**
	 * Turn on / off Barcode Tyoe, Version and VIN .
	 * 
	 * @param abShow
	 */
	public void turnOnOffCommonFlds(boolean abShow)
	{
		getstcLblType().setVisible(abShow);
		gettxtBarcodeType().setVisible(abShow);
		getstcLblVersion().setVisible(abShow);
		gettxtBarcodeVersion().setVisible(abShow);
		getstcLblDocNo().setVisible(abShow);
		gettxtDocType().setVisible(abShow);
		getstcLblVIN().setVisible(abShow);
		gettxtVIN().setVisible(abShow);
	}

	/**
	 * Turn OnOffNewRenewalFields
	 * 
	 * @param abShow
	 */
	private void turnOnOffNewRenwlFlds(boolean abShow)
	{
		// Org Name/No
		getstcLblOrgNoOrgName().setVisible(abShow);
		gettxtOrgNo().setVisible(abShow);
		gettxtOrgName().setVisible(abShow);

		// Add'l Set  
		getstcLblAddlSetIndi().setVisible(abShow);
		gettxtAddlSetIndi().setVisible(abShow);

		// New Plates Required Cd 
		getstcLblNewPlatesRequiredCode().setVisible(abShow);
		gettxtNewPlatesRequiredCode().setVisible(abShow);

		// Plt Exp Mo/Yr 
		getstcLblPltExpMoYr().setVisible(abShow);
		getstcLblBkSlash1().setVisible(abShow);
		gettxtPltExpMo().setVisible(abShow);
		gettxtPltExpYr().setVisible(abShow);

		// Plate Validity Term 
		getstcLblPlateValidityTerm().setVisible(abShow);
		gettxtPlateValidityTerm().setVisible(abShow);

		// Next Plate Exp Mo/Yr 
		getstcLblNextPltExpMoYr().setVisible(abShow);
		getstcLblBkSlash2().setVisible(abShow);
		gettxtNextPltExpMo().setVisible(abShow);
		gettxtNextPltExpYr().setVisible(abShow);

		// Next Reg Exp Mo/Yr 
		getstcLblNextRegExpMoYr().setVisible(abShow);
		getstcLblBkSlash3().setVisible(abShow);
		gettxtNextRegExpMo().setVisible(abShow);
		gettxtNextRegExpYr().setVisible(abShow);

	}

	/**
	 * Turn on / off Plate .
	 * 
	 * @param abShow
	 */
	public void turnOnOffPlate(boolean abShow)
	{
		getstcLblPlateRegPltCd().setVisible(abShow);
		gettxtPlateRegPltCd().setVisible(abShow);
		getstcLblPlateStkrNo().setVisible(abShow);
		gettxtPlateStkrNo().setVisible(abShow);
		getstcLblPlateItemYr().setVisible(abShow);
		gettxtPlateItemYr().setVisible(abShow);
	}

	/**
	 * Turn on / off Print County Number.
	 * 
	 * @param abShow
	 */
	public void turnOnOffPrntCntyNo(boolean abShow)
	{
		boolean lbHide = false;
		boolean lbShow = true;

		if (abShow)
		{
			// Show Print County Number/ Hide Audit Trail-RegEcp Mo/Yr
			getstcLblPrntCntyNo().setVisible(abShow);
			gettxtPrntCntyNo().setVisible(abShow);
			getstcLblRegExpDate().setVisible(abShow);
			gettxtRegExpDate().setVisible(abShow);
			gettxtResCompCntyNo().setVisible(abShow);
			getstcLblResCompCntyNo().setVisible(abShow);
			gettxtStkrPrntDate().setVisible(abShow);
			getstcLblStkrPrntDate().setVisible(abShow);
			gettxtPrntWsId().setVisible(abShow);
			getstcLblWsId().setVisible(abShow);
			gettxtPrntCntyNo().setVisible(abShow);
			getstcLblPrntCntyNo().setVisible(abShow);
			gettxtPrntRegPltCd().setVisible(abShow);
			getstcLblPrntRegPltCd().setVisible(abShow);
			gettxtPrntRegPltNo().setVisible(abShow);
			getstcLblPrntRegPltNo().setVisible(abShow);
			gettxtPrntRegStkrCd().setVisible(abShow);
			getstcLblPrntRegStkrCd().setVisible(abShow);
			gettxtPrntRegClassCd().setVisible(abShow);
			getstcLblPrntRegClassCd().setVisible(abShow);

			getstcLblAuditTrailTransId().setVisible(lbHide);
			gettxtAuditTrailCd().setVisible(lbHide);
			getstcLblRegExpMoYr().setVisible(lbHide);
			gettxtRegExpMo().setVisible(lbHide);
			getstcLblBkSlash().setVisible(lbHide);
			gettxtRegExpYr().setVisible(lbHide);
			getstcLblRegPltCd().setVisible(lbHide);
			gettxtRegPltCd().setVisible(lbHide);
			getstcLblRegPltNo().setVisible(lbHide);
			gettxtRegPltNo().setVisible(lbHide);
			getstcLblRegStkrCd().setVisible(lbHide);
			gettxtRegStkrCd().setVisible(lbHide);
			getstcLblCntyNo().setVisible(lbHide);
			gettxtCntyNo().setVisible(lbHide);
			gettxtRegClassCd().setVisible(lbHide);
			getstcLblRegClassCd().setVisible(lbHide);
			// Turn off AcctItmCds and ItmPrices
			turnOnOffAcctItmFees(lbHide);

			// defect 10464 
			turnOnOffNewRenwlFlds(lbHide);
			// end defect 10464 

		}
		else
		{ // Hide Print County Number/ Show Audit Trail-RegEcp Mo/Yr
			getstcLblPrntCntyNo().setVisible(abShow);
			gettxtPrntCntyNo().setVisible(abShow);
			getstcLblRegExpDate().setVisible(abShow);
			gettxtRegExpDate().setVisible(abShow);
			gettxtResCompCntyNo().setVisible(abShow);
			getstcLblResCompCntyNo().setVisible(abShow);
			gettxtStkrPrntDate().setVisible(abShow);
			getstcLblStkrPrntDate().setVisible(abShow);
			gettxtPrntWsId().setVisible(abShow);
			getstcLblWsId().setVisible(abShow);
			gettxtPrntCntyNo().setVisible(abShow);
			getstcLblPrntCntyNo().setVisible(abShow);
			gettxtPrntRegPltCd().setVisible(abShow);
			getstcLblPrntRegPltCd().setVisible(abShow);
			gettxtPrntRegPltNo().setVisible(abShow);
			getstcLblPrntRegPltNo().setVisible(abShow);
			gettxtPrntRegStkrCd().setVisible(abShow);
			getstcLblPrntRegStkrCd().setVisible(abShow);
			gettxtPrntRegClassCd().setVisible(abShow);
			getstcLblPrntRegClassCd().setVisible(abShow);

			getstcLblRegExpMoYr().setVisible(lbShow);
			gettxtRegExpMo().setVisible(lbShow);
			gettxtRegExpYr().setVisible(lbShow);
			getstcLblBkSlash().setVisible(lbShow);
			getstcLblAuditTrailTransId().setVisible(lbShow);
			gettxtAuditTrailCd().setVisible(lbShow);
			getstcLblRegPltCd().setVisible(lbShow);
			gettxtRegPltCd().setVisible(lbShow);
			getstcLblRegPltNo().setVisible(lbShow);
			gettxtRegPltNo().setVisible(lbShow);
			getstcLblRegStkrCd().setVisible(lbShow);
			gettxtRegStkrCd().setVisible(lbShow);
			getstcLblCntyNo().setVisible(lbShow);
			gettxtCntyNo().setVisible(lbShow);
			gettxtRegClassCd().setVisible(lbShow);
			getstcLblRegClassCd().setVisible(lbShow);
			// Turn off AcctItmCds and ItmPrices
			turnOnOffAcctItmFees(lbShow);

			// defect 10464 
			turnOnOffNewRenwlFlds(lbShow);
			// end defect 10464  
		}
	}
	
	 /** 
	  * Called whenever the value of the selection changes.
	  * @param e the event that characterizes the change.
	  */
	public void valueChanged(javax.swing.event.ListSelectionEvent e)
	{
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
