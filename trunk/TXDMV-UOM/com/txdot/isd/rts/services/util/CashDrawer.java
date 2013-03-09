package com.txdot.isd.rts.services.util;

import java.io.*;
import javax.comm.*;
import com.txdot.isd.rts.services.exception.*;

/*
 *
 * CashDrawer.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	10/24/2002	Add debug logging to track time to open 
 * 							drawer.
 *							defect 4872
 * Ray Rowehl	06/21/2005	Code cleanup.
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	11/08/2007	Only try to open the drawer if the 
 * 							comm port is defined.
 * 							modify open()
 * 							defect 9426 Ver Special Plates
 * B Woodson	07/28/2011	Code for USB cashdrawer
 * 							modify open()
 * 							modify MESSAGE
 * 							added CASH_DRAWER_USB_EXE_PATH 
 * 							defect 10916
 * ---------------------------------------------------------------------
 */

/**
 * Provides a method to open the Cash Drawer
 * 
 * @version 6.8.1 07/28/2011
 * @author Michael Abernethy <br>
 *         Creation Date: 09/06/2001 09:44:05
 */

public class CashDrawer
{
	/**
	 * 
	 */
	private static final String CASH_DRAWER_USB_EXE_PATH = "d:/rts/rtsappl/hiddemo6.exe";

	private final static int BAUD_RATE = 1200;

	private final static int TIMEOUT_TIME = 3000;

	private final static java.lang.String MESSAGE = "OPENSESAMEFORALADDINANDPRINCESSJASMINE"
			+ " AS WELL AS BILL HARGROVE, KATHY HARRELL, TODD PEDERSON, RICHARD PILON JR., MARK REYES, RAY ROWEHL, ROBIN TAYLOR, MIN WANG, AND BUCK WOODSON";

	private final static java.lang.String CASH_DRAWER_PORTNAME = SystemProperty
			.getCashdrawerPortName();

	private final static java.lang.String CASH_DRAWER_USB_PORTNAME = "USB";

	private static java.io.OutputStream laOutputStream;

	private static javax.comm.SerialPort laSerialPort;

	/**
	 * Opens the cash drawer
	 * 
	 * @throws RTSException
	 */
	public static void open() throws RTSException
	{
		// log that we are starting open of cash drawer
		Log.write(Log.DEBUG, new RTSDate(), " Opening CashDrawer");

		try
		{
			// defect 9426
			// Only attempt to open the drawer if it is defined.
			if (CASH_DRAWER_PORTNAME != null)
			{
				// end defect 9426

				// defect 10916
				if (CASH_DRAWER_PORTNAME
						.equalsIgnoreCase(CASH_DRAWER_USB_PORTNAME))
				{
					Runtime.getRuntime().exec(
							CASH_DRAWER_USB_EXE_PATH);
				} 
					else
				{
					// end defect 10916

					CommPortIdentifier laCommPort = CommPortIdentifier
							.getPortIdentifier(CASH_DRAWER_PORTNAME);
					laSerialPort = (SerialPort) laCommPort.open(
							"Cash Drawer", TIMEOUT_TIME);
					laOutputStream = new BufferedOutputStream(
							laSerialPort.getOutputStream());
					laSerialPort.setSerialPortParams(BAUD_RATE,
							SerialPort.DATABITS_8,
							SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
					laOutputStream.write(MESSAGE.getBytes());
					laOutputStream.close();
					laSerialPort.close();
				}
				// defect 9426
			} 
				else
			{
				Log.write(Log.DEBUG, new RTSDate(),
						" Bypassed Opening CashDrawer");
			}
			// end defect 9426
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
		catch (UnsupportedCommOperationException aeUCPEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeUCPEx);
		}
		// log that we are leaving the cash drawer open method
		finally
		{
			Log.write(Log.DEBUG, new RTSDate(),
					" Finished Opening CashDrawer");
		}
	}
}
