package com.txdot.isd.rts.client.systemcontrolbatch.ui;

import java.io.IOException;
import java.util.Vector;

import com.txdot.isd.rts.services.util.SystemProperty;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.SystemControlBatchConstant;

/*
 *
 * ZipCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Change History:
 * Name        	Date        Description
 * Min Wang		11/11/2002	Fixed CQU100004746. Modified getValueAt() 
 * 							to add Void as a valid transaction type.
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7897 Ver 5.2.3
 * Jeff S.		07/08/2005	Code Cleanup for Java 1.4.2 upgrade
 * S Johnston				modify 
 *							defect 7897 ver 5.2.3 
 * Jeff S.		07/12/2005	Added String Constants.
 * 							defect 7897 ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * Zips the transaction cache into a file called cache.zip
 * 
 * @version	5.2.3			07/12/2005
 * @author	Michael Abernethy
 * <br>Creation Date: 		10/16/2001 15:56:29
 */
public class ZipCache
{
	/**
	 * String Constants
	 */
	private static final String NEW_LINE = "\n";
	/**
	 * ZipCache constructor.
	 */
	public ZipCache()
	{
		Vector lvTemp = new Vector();
		lvTemp.add(SystemProperty.getTransactionDirectory());
		JZip laJZip =
			new JZip(SystemControlBatchConstant.OUTPUT_FILE, lvTemp);
		try
		{
			laJZip.zip();
		}
		catch (IOException aeEX)
		{
			System.err.println(
				SystemControlBatchConstant.ERROR_MESSAGE
					+ NEW_LINE
					+ aeEX.getMessage());
		}
	}
	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs String[] an array of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		ZipCache laZipCache = new ZipCache();
	}
}
