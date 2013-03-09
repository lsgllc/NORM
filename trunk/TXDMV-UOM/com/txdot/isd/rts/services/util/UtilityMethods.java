package com.txdot.isd.rts.services.util;

import java.awt.Component;
import java.awt.Container;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com.txdot.isd.rts.client.general.business.BusinessInterface;
import com.txdot.isd.rts.client.general.ui.RTSDateField;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com.txdot.isd.rts.client.general.ui.RTSPhoneField;
import com.txdot.isd.rts.client.general.ui.RTSTimeField;
import com.txdot.isd.rts.client.systemcontrolbatch.business.RTSMain;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * UtilityMethods.java
 *
 * (c) Texas Department of Transportation 2001 
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		08/21/2001	Add the arrayToVector method
 * N Ting		08/28/2001	Add contructPrimaryKey method
 * M Abernethy 	09/13/2001 	Add copy
 * M Abernethy 	09/13/2001 	Add sort
 * R Hicks		09/20/2001	Add convertToHex and convertFromHex 
 *							methods
 * N Ting		10/02/2001	Add addPadding
 * R Hicks      11/13/2001 	Changed ASCII_LIMIT to allow encrypt / 
 *							decrypt of lower case values.
 * R Duggirala  12/10/2001  Add method to Save Reports on local 
 *							machine
 * C Walker		02/06/2002	Add addPaddingRight
 * N Ting		04/16/2002	Add Beep method
 * Min Wang     11/04/2002	Add rtsPow().
 * S Govind		12/16/2002	Added isMFUP().
 * Ray Rowehl	02/10/2003	remove trim from password methods
 *							defect CQU100004735
 * Ray Rowehl	05/27/2003	Add methods to support the new XP 
 *							Logon scheme
 *							added methods doWindowsLogout(), 
 *										doReboot(), 
 *										getSystemUserid(),
 *										isWindowsPlatform.
 *							defect 6445  Ver 5.1.6
 * Jeff S.		08/21/2003	XP Defect - When saving reports all
 *							since iterations need to be in upercase 
 *							XP can handle both upercase and 
 *							lowercase filenames. saveReport() 
 *							defect 6214 ver 5.1.6
 * Ray Rowehl	10/20/2003	Added a new getSystemUserId with a 
 *							parameter of an int.  This method uses 
 *							the jni interface to get the user id.
 *							added getSystemUserId(int)
 *							deprecated getSystemUserId()
 *							Cleaned Javadocs and formatted code.
 *							defect 6445  Ver 5.1.6
 * Ray Rowehl	12/12/2003	Changing Windows reboot command to be 
 *							"reboot".  Developed a new exe to do the 
 *							reboot.  "System" does not have the
 *							authority to issue shutdown -r.
 *							defect 6445  Ver 5.1.6
 * Ray Rowehl	12/30/2003	Moved getSystemUserId to JniAdInterface
 *							since it fits in with that class.
 *							delete getSystemUserId
 *							defect 6445  Ver 5.1.6
 * Ray Rowehl	01/12/2004	Add code to lookup Office Code.
 *							add getOfficeCode()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/05/2004	Add System exit to doReboot.
 *							modify doReboot()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	04/09/2004	Improve Logout to wait until the user
 *							is completely logged out before
 *							displaying the Disclaimer screen.
 *							Also add some logging so we know what
 *							is happening.
 *							add CHKLOGOFF_COMMAND
 *							modify doWindowsLogout()
 *							defect 7006 Ver 5.1.6
 * K Harrell	03/28/2004	Remove references to isWindows
 * 							deprecate isWindows()
 *							modify displayURL()
 *							modify doShutdown()
 *							defect 6955 Ver 5.2.0
 * K Harrell	03/31/2004	Verify filename conforms to Funds
 *							report naming conventions.
 *							add FUNDS_FILENAME_PREFIX_LENGTH
 *							add FUNDS_FILE_EXTENSION
 *							modify saveReport()
 *							defect 6995 Ver 5.2.0
 * K Harrell	04/02/2004	Add methods for checking type of office
 *							add isRegion(),isHeadquarters(),
 *							 isCounty()
 *							add HEADQUARTERS_OFFICE,
 *							 REGION_OFFICE, COUNTY_OFFICE
 *							defect     Ver 5.2.0
 * Ray Rowehl	04/20/2004	Clean out controller before logging out.
 *							modify doWindowsLogout
 *							defect 7022 Ver 5.1.6
 * K Harrell	05/07/2004	create method to modify string to
 *							replace single quote with 2 single
 *							quotes.
 *							add quote()
 *							defect 7000 Ver 5.2.0
 * Jeff S.		05/26/2004	Added string function to search a string for
 *							all instances of the search value and
 *							replace with the replace value.
 *							add replaceString(String, String, String)
 *							defect 7078 Ver 5.2.0
 * Jeff S.		05/27/2004	Moved String utilitiy method here from
 *							PCLUtilities Class.
 *							add padFields(String, int, String)
 *							add removeChar(String, char)
 *							defect 7107 Ver 5.2.0
 * K Harrell	06/06/2004	Remove padFields(), removeChar()
 *							No longer required.
 *							defect 7150 Ver 5.2.0
 * Jeff S.		08/02/2004	Add copyFile to copy files.
 * Ray Rowehl				add copyFile(file, file), 
 *								copyFile(file, file, boolean)
 *							defect 7135 Ver 5.2.1
 * Jeff S.		12/03/2004	Modify saveReport() so that if a filename
 *							that was longer than 7 without the extention
 *							was passed the filename would be shortened.
 *							This is only done when iterations are saved.
 *							modify saveReport(String, String, int)
 *							defect 7702 Ver 5.2.2
 * J Rue		12/10/2004	Determine if character string is numeric.
 *							add isNumeric()
 *							defect 6844 Ver 5.2.2
 * Ray Rowehl	02/08/2005	Change import for Transaction
 *							modify doWindowsLogout()
 *							defect 7705 Ver 5.2.3
 * J Rue		05/24/2005	New method that will return a boolean if the 
 * 							whole and decimal numbers are digits
 * 							New method that checks for "-" that precedes
 * 							a string
 * 							Clean up code
 * 							add isNumeric(String), isNumeric(float)
 * 							add checkForNegativeNum(String)
 * 							delete isDoublePositive(), 
 * 							isDollarPositive(), isIntPositive
 * 							defect 6777, 7772, 7773, 7898 Ver 5.2.3
 * J Rue		06/01/2005	Depercate unused methods
 * 							Change checkForNegativeNum(String) to 
 * 							isNegative(String)
 * 							deprcate displayURL(String asURL)
 * 							defect 6777, 7898 Ver 5.2.3
 * Jeff S.		06/06/2005	Handled closing of files.
 * 							modify shrinkLogFile()
 * 							defect 8219 Ver 5.1.6
 * Jeff S.		06/28/2005	In 8219 I added a printstacktrace() to the
 * 							catch block in shrinkLogFiles and I noticed
 * 							that if the batch.log does not exist we are
 * 							getting an error.  I added a check to make
 * 							sure the log exists before we attempt to 
 * 							shrink.
 * 							modify shrinkLogFile()
 * 							defect 8232 Ver 5.2.3
 * Ray Rowehl	08/12/2005	Add getters for font name and font size for
 * 							use in places where we want to make text
 * 							bold.
 * 							add getDefaultFont(), getDefaultFontSize()
 * 							defect 7890 Ver 5.2.3
 * K Harrell	09/29/2005	add getTransId()
 * 							delete isWindowsPlatform()
 * 							defect 7889 Ver 5.2.3 
 * Jeff S.		01/17/2006  Added redirect of standard err and out to a
 * 							log file.  Since our normal System.out does
 * 							not go to the console we have to use the 
 * 							original System.out that was saved in 
 * 							RTSMain.
 * 							modify beep()
 * 							defect 8372 Ver 5.2.3
 * Jeff S.		02/01/2006	Fixed Logout problem.  Deprecated 
 * 							showWebPage() b/c it called displayURL() and
 * 							it was deprecated.  showWebPage() was not
 * 							being used by anyone.
 * 							add WINDOWS_LOGOUT_COMMAND
 * 							deprecate showWebPage()
 * 							modify doWindowsLogout()
 * 							defect 7889 Ver 5.2.3
 * J Rue		08/15/2006	Move close controller to before 
 * 							cleanseStack()
 * 							modify doWindowsLogout()
 * 							defect 8851 Ver 5.2.4
 * J Rue		02/08/2007	isSpecialPlates to check all transcds
 * 							add isSpecialPlates()
 * 							defect 9086 Ver Special Plates
 * K Harrell	03/09/2007	delete isHeadquarters(), isRegion(), isCounty()
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/27/2007	add isAllZeros()
 * 					 		defect 9085 Ver Special Plates
 * K Harrell	04/18/2007	add getEventType()
 * 							modify isSpecialPlates() to use getEventType()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/15/2007	add createSpclPltMfgInfoMsg()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/01/2007	add isSPAPPL()
 * 							defect 9085 Ver Special Plates
 * Ray Rowehl	07/20/2007	Only shrink the log if the lines existing
 * 							are greater than the lines to keep.
 * 							modify shrinkLogFile()
 * 							defect 9116 Ver Special Plates
 * K Harrell	08/03/2007	add rTrim()
 * 							defect 9085 Ver Special Plates   
 * K Harrell	10/21/2008	add isDsabldPlcrdEvent()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	03/17/2009	add removeLeadingZeros()
 * 							defect 9974 Ver Defect_POS_E 
 * J Rue		04/17/2009	Retrieve MfInterfaceVersionCode in server.cfg
 * 							for BuildFundsUpdate and BuildSendTransaction
 * 							add setDefaultMfVerCd(), SERVER_CONFIG,
 * 								FILE_EXTENSION, MFINTERFACEVERSIONCD
 * 							defect 9965 Ver Defect_POS_E
 * J Rue		06/16/2009	Move setDefaultMfVerCd() to FileUtil class
 * 							delete setDefaultMfVerCd()
 * 							defect 10080 Ver Defect_POS_F
 * K Harrell	06/19/2009	saveReports() now static, synchronized
 * 							delete setWaitCursor()
 * 							modify saveReports()
 * 							defect 10023 Ver Defect_POS_F
 * K Harrell	07/13/2009	add trimRTSInputField()
 * 							defect 10127 Ver Defect_POS_F 
 * K Harrell	08/16/2009	add addPCLandSaveReport()
 * 							modify saveReport() (comments)
 * 							defect 8628 Ver Defect_POS_F 	
 * K Harrell	08/24/2009	add isNewTitleTransCd()
 * 							defect 10148 Ver Defect_POS_E'/F
 * K Harrell	10/16/2009	add addAttributesForObject()
 * 							defect 10191 Ver Defect_POS_G 
 * K Harrell	12/15/2009	add getNewDTADlrTtlDataVectorWithSkip()
 * 							modify isMFUp()
 * 							delete UtilityMethods() (constructor) 
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	04/10/2010	add isTransCdValidForSRFuncLookup() 
 * 							defect 9585 Ver POS_640
 * K Harrell	05/24/2010	add isPermitApplication()
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	07/14/2010	add printsPermit()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	05/29/2011	modify printsPermit() 
 * 							defect 10844 Ver 6.8.0 
 * R Pilon		06/22/2011	modify isTransCdValidForSRFuncLookup()
 * 							defect 10820 Ver 6.8.0 
 * Ray Rowehl	07/28/2011	Remove the time and comment parameters from
 * 							the logout command.  Windows 7 does not like 
 * 							them.
 * 							modify WINDOWS_LOGOUT_COMMAND
 * 							defect 10961 Ver 6.8.0
 * M Reyes		09/06/2011	Changed the reboot command and shutdown parms.
 * 							defect 10983 Ver 6.8.1 
 * B Woodson    11/05/2011  added nullSafe
 *                          defect 11052 - VTR-275 Form Project @ POS
 * K Harrell	11/14/2011	add isVTR275TransCd(), isVehInqTransCd(), 
 * 							getVehInqTransCd()
 * 							defect 11052 Ver 6.9.0 
 * K Harrell	12/05/2011	add isSalvageDocTypeCd()
 * 							defect 11051 Ver 6.9.0
 * M Reyes		03/28/2011	Change logout to System Exit(0)
 * 							modify doWindowsLogout()
 * 							defect 11324 Ver 7.0.0 
 * Ray Rowehl	04/19/2012	Add trimControlPoint for trimming down
 * 							workstation names to fit on the databases.
 * 							add trimControlPoint()
 * 							modify main()
 * 							defect 11320 Ver POS_700 
 * Ray Rowehl	06/26/2012	Modify beep to use the toolkit all the time 
 * 							on client side.
 * 							modify beep()
 * 							defect 11398 Ver POS_700
 * ---------------------------------------------------------------------
 */

/**
 * The UtilityMethods class provides static helper methods for
 * the RTS Application.
 * 
 * @version	POS_700 		06/26/2012
 * @author	Nancy Ting
 * @since					08/28/2001 16:14:19
 */

public class UtilityMethods
{
	/**  ASCII character adjustment value used by encryption / 
	 * decryption algortihms **/
	public final static int ASCII_ADJUSTMENT = 146;

	/**  ASCII character limit value used by encryption / 
	 * decryption algortihms **/
	public final static int ASCII_LIMIT = 122;

	/**
	 * This is the XP Command to make sure the user is logged out of the
	 *  workstation.
	 */
	private final static String CHKLOGOFF_COMMAND = "ChkLogoff";
	private final static String FUNDS_FILE_EXTENSION = "RPT";

	private final static int FUNDS_FILENAME_PREFIX_LENGTH = 4;

	/**
	 * This is the XP Command to shutdown the workstation.
	 */
	// defect 10983
	private final static String REBOOT_COMMAND = "shutdown -r -t 0 -f";
	// end defect 10983

	//  The following constants are used to perform encryption / 
	//	decryption
	/**  Shift value used by encryption / decryption algortihms **/
	public final static int SHIFT_VALUE = 31;

	public final static int TECH_HELP = 0;
	/**
	 * This value is used to define the default font
	 */
	private static final String TXT_FONT_GETTER = "TextPane.font";
	public final static int USER_HELP = 1;
	/**
	 * This is the XP Command to logout of OS.
	 */
	// defect 10961
	// Remove the time and comment parameters for win 7
	// defect 10983
	private final static String WINDOWS_LOGOUT_COMMAND =
		"shutdown -l -f";
		//"shutdown -l -t 1 -c \"Requested by RTS\"";
	// end defect 10961
	// end defect 10983

	/**
	 * addAttributesForObject 
	 * 
	 * @param asName
	 * @param aaMap
	 * @param ahmHash 
	 */
	public static void addAttributesForObject(
		String asName,
		Map aaMap,
		HashMap ahmHash)
	{
		Iterator j = aaMap.entrySet().iterator();

		int liMapSize = aaMap.size();

		for (int k = 0; k < liMapSize; k++)
		{
			Map.Entry laMapE = (Map.Entry) j.next();
			ahmHash.put(
				asName.substring(2) + "." + laMapE.getKey(),
				laMapE.getValue());
		}
	}

	/**
	 * This method takes three arguments: a string, an integer that 
	 * specifies the length of the element in the string, and the 
	 * padding required.  It will then size the stringaccording to the 
	 * length specified. 
	 *
	 * <P>If the size of the string is less than the specified length, 
	 *  the string will be padded with asPad.<br>
	 * If the size of the string is greater than the length specified, 
	 * the string will truncated.<br>
	 *
	 * <P>Example:
	 * String result = UtilityMethod.addPadding("AAA", 5, " ");
	 * result will be equal to "AAA  "
	 * 
	 * @param asStr String
	 * @param aiPadLength int
	 * @return String
	 */
	public static String addPadding(
		String asStr,
		int aiPadLength,
		String asPad)
	{
		StringBuffer lsStringBuffer = new StringBuffer();
		// determine how to treat the string
		if (asStr.length() == aiPadLength)
		{
			// if the length is the same already, just return the string
			lsStringBuffer.append(asStr);
		}
		else if (asStr.length() < aiPadLength)
		{
			// pad with padding then append the string
			for (int liIndex = 0;
				liIndex < aiPadLength - asStr.length();
				liIndex++)
			{
				lsStringBuffer.append(asPad);
			}
			lsStringBuffer.append(asStr);
		}
		else
		{
			// truncate the string to the right length
			lsStringBuffer.append(asStr.substring(0, aiPadLength));
		}
		return lsStringBuffer.toString();
	}

	/**
	 * This method takes three arguments: an array of string, an array 
	 * of integer that specifies the length of each corresponding 
	 * element in the string array, and the padding required.  It will 
	 * then sized each of the element of the string array according 
	 * to the length specified by the corresponding element in the 
	 * integer array, and return a concatenation of the string elements.
	 *
	 * <P>If the size of the element in the string array is less than 
	 * that specified in the corresponding element in integer array, the
	 * element will be padded with asPad.<br>
	 * If the size of the element in the string array is greater than 
	 * that specified in the corresponding element in integer array, the
	 *  element will truncated.<br>
	 *
	 * <P>Example:
	 * String result = 
	 *  UtilityMethod.addPadding(new String[]{
	 * 		"aaa","bbb","ccc"}, new int[]{2,3,4}, "0");
	 * result will be equal to aabbb0ccc
	 * 
	 * @param aarrStr String[]
	 * @param aarrPadLength int[]
	 * @param asPad String
	 * @return String
	 */
	public static String addPadding(
		String[] aarrStr,
		int[] aarrPadLength,
		String asPad)
	{
		StringBuffer lsStringBuffer = new StringBuffer();
		for (int liIndex = 0; liIndex < aarrStr.length; liIndex++)
		{
			if (aarrStr[liIndex].length() == aarrPadLength[liIndex])
			{
				lsStringBuffer.append(aarrStr[liIndex]);
			}
			else if (
				aarrStr[liIndex].length() < aarrPadLength[liIndex])
			{
				for (int liJIndex = 0;
					liJIndex
						< aarrPadLength[liIndex]
							- aarrStr[liIndex].length();
					liJIndex++)
				{
					lsStringBuffer.append(asPad);
				}
				lsStringBuffer.append(aarrStr[liIndex]);
			}
			else
			{
				lsStringBuffer.append(
					aarrStr[liIndex].substring(
						0,
						aarrPadLength[liIndex]));
			}
		}
		return lsStringBuffer.toString();
	}
	/**
	 * This method takes three arguments: a string, an integer that 
	 * specifies the length of the element in the string, and the 
	 * padding required.  It will then size the string according to the 
	 * length specified. 
	 *
	 * <P>If the size of the string is less than the specified length, 
	 *  the string will be padded to the right with asPad.<br>
	 * If the size of the string is greater than the length specified, 
	 * the string will truncated.<br>
	 *
	 * <P>Example:
	 * String result = UtilityMethod.addPadding("AAA", 5, " ");
	 * result will be equal to "  AAA"
	 * 
	 * @param asStr String
	 * @param aiPadLength int
	 * @param asPad String
	 * @return String
	 */
	public static String addPaddingRight(
		String asStr,
		int aiPadLength,
		String asPad)
	{
		StringBuffer lsStringBuffer = new StringBuffer();
		// determine how to treat the string
		if (asStr.length() == aiPadLength)
		{
			// if the length is the same already, just return the string
			lsStringBuffer.append(asStr);
		}
		else if (asStr.length() < aiPadLength)
		{
			// append the string then pad with padding
			lsStringBuffer.append(asStr);
			for (int liIndex = 0;
				liIndex < aiPadLength - asStr.length();
				liIndex++)
			{
				lsStringBuffer.append(asPad);
			}
		}
		else
		{
			// truncate the string to the right length
			lsStringBuffer.append(asStr.substring(0, aiPadLength));
		}
		return lsStringBuffer.toString();
	}
	/**
	 * This method takes three arguments: an array of string, an array 
	 * of integer that specifies the length of each corresponding 
	 * element in the string array, and the padding required.  It will 
	 * then sized each of the element of the string array according to 
	 * the length specified by the corresponding element in the integer 
	 * array, and return a concatenation of the string elements with the
	 * padding after the string array.
	 *
	 * <P>If the size of the element in the string array is less than 
	 * that specified in the corresponding element in integer array, the
	 * element will be padded with asPad.<br>
	 * If the size of the element in the string array is greater than 
	 * that specified in the corresponding element in integer array, an 
	 * exception will be thrown.<br>
	 *
	 * <P>Example:
	 * String result = UtilityMethod.addPadding(new String[]{
	 * 	"aaa","bbb","ccc"}, new int[]{2,3,4}, "0");
	 * result will be equal to aabbbccc0
	 * 
	 * @param aarrStr String[]
	 * @param aarrPadLength int[]
	 * @param asPad padding
	 * @return String
	 */
	public static String addPaddingRight(
		String[] aarrStr,
		int[] aarrPadLength,
		String asPad)
	{
		int liLen = aarrStr.length;
		StringBuffer lsRetVal = new StringBuffer();
		for (int liIndex = 0; liIndex < liLen; liIndex++)
		{
			String lsStr = aarrStr[liIndex];
			int liReqLen = aarrPadLength[liIndex] - lsStr.length();
			StringBuffer lsSB = new StringBuffer();
			lsSB.append(lsStr);
			for (int j = 0; j < liReqLen; j++)
			{
				lsSB.append(asPad);
			}
			lsRetVal.append(lsSB.toString());
		}
		return lsRetVal.toString();
	}

	/**
	 * Convert an array to a Vector
	 *
	 * @param aarrAnyObjArray Object[]
	 * @return Vector
	 */
	public static Vector arrayToVector(Object[] aarrAnyObjArray)
	{
		if (aarrAnyObjArray == null)
		{
			return null;
		}
		java.util.Vector lvReturnVector = new java.util.Vector();
		for (int liIndex = 0;
			liIndex < aarrAnyObjArray.length;
			liIndex++)
		{
			lvReturnVector.add(liIndex, aarrAnyObjArray[liIndex]);
		}
		return lvReturnVector;
	}

	/**
	 * Creates a beep sound
	 */
	public static void beep()
	{
		// defect 8372
		// The java console is being redirected to a file in prod staus 
		// of production, so we have to send the beep to the original 
		// System.out printStream.  The server side does not redirect 
		// the Standard error and out so we can send the beep just like
		// we did before.  If we are not in production then we will use
		// the Toolkit.beep(). This allows some sound to emit when in
		// WSAD.  The problem is that you must have speakers to hear it.
		
		if (!Comm.isServer())
		{
			// defect 11398
			// use Toolkit only for beeps on client side
			
			// if (SystemProperty.getProdStatus()
			//	== SystemProperty.APP_PROD_STATUS)
			// {
			//	RTSMain.ORIG_SYSTEM_OUT.println("\007");
			//	RTSMain.ORIG_SYSTEM_OUT.flush();
			// }
			// else
			// {
				// end defect 11398
				// Leave this as fully qualified so that server
				// side won't fail (classdef not found)
				java.awt.Toolkit.getDefaultToolkit().beep();
				// defect 11398
			//}
			// end defect 11398
		}
		else
		{
			System.out.println("\007");
			System.out.flush();
		}
		// end defect 8372
	}

	/**
	 * Create the primary key for the internal hashtable stored in Cache
	 * classes where primary key of the database consists of more than 
	 * one key.
	 *
	 * <p>aarrStrArray should contain the string values in the order to be
	 * concatenated.  The result string will be a concatenation of all 
	 * the elements in the array delimited by #
	 *
	 * <p>Example:<br>
	 * To create a primary key from a database that composed of:<br>
	 * - office issuance no of type "int"<br>
	 * - substation id of type "int"<br>
	 * - employee id of type "String" <br><br>
	 * String cachePrimaryKey = UtilityMethods.constructPrimaryKey(
	 * 						new String[]{String.valueOf(ofcIssuanceNo), 
	 *							String.valueOf(subStaId), empId });
	 * 
	 * @param aarrStrArray String[]
	 * @return String
	 */
	public static String constructPrimaryKey(String[] aarrStrArray)
	{
		String lsReturnString = "";
		for (int liIndex = 0; liIndex < aarrStrArray.length; liIndex++)
		{
			lsReturnString += aarrStrArray[liIndex] + "#";
		}
		return lsReturnString;
	}

	/**
	 * Create the primary key for the internal hashtable stored in Cache 
	 * classes where primary key of the database consists of more than 
	 * one key.
	 *
	 * <p>aarrStrArray should contain the string values in the order to 
	 * be concatenated.  The corresponding array element in 
	 * aarrStrLengthArray specifies the length of the each aarrStrArray 
	 * element.If the length specified is larger than the aarrStrArray 
	 * element, the aarrStrArray element will be padding with zeroes in 
	 * front of the string before concatenation happens; if the length 
	 * specified is shorter than the aarrStrArray element, the 
	 * aarrStrArray element will be truncated to the size specified with
	 * the trailing characters being cut off.
	 *
	 * <p>Example:<br>
	 * To create a primary key from a database that composed of:<br>
	 * - office issuance no of type "int"<br>
	 * - substation id of type "int"<br>
	 * - employee id of type "String" <br><br>
	 * String cachePrimaryKey = UtilityMethods.constructPrimaryKey(
	 * 						new String[]{String.valueOf(ofcIssuanceNo), 
	 *							String.valueOf(subStaId), empId }, 
	 * 								new int[]{3,2,3});
	 * 
	 * @param aarrStrArray String[]
	 * @param aarrStrLengthArray int[]
	 * @see constructPrimaryKey(String[] aarrStrArray)
	 * @return String
	 * @throws RTSException
	 */
	public static String constructPrimaryKey(
		String[] aarrStrArray,
		int[] aarrStrLengthArray)
		throws RTSException
	{
		if (aarrStrArray.length != aarrStrLengthArray.length)
		{
			throw new RTSException(
				RTSException.JAVA_ERROR,
				new Exception(
					"asStrArray and aarrStrLengthArray "
						+ "is not the same length"));
		}
		String lsReturnString = "";
		for (int liIndex = 0; liIndex < aarrStrArray.length; liIndex++)
		{
			int lsStrlength = aarrStrArray[liIndex].length();
			if (lsStrlength < aarrStrLengthArray[liIndex])
			{
				//padding required
				String lsPadding = "";
				for (int liJIndex = 0;
					liJIndex
						< (aarrStrLengthArray[liIndex] - lsStrlength);
					liJIndex++)
				{
					lsPadding += "0";
				}
				lsReturnString += lsPadding + aarrStrArray[liIndex];
			}
			else if (lsStrlength > aarrStrLengthArray[liIndex])
			{
				//truncation required
				lsReturnString
					+= aarrStrArray[liIndex].substring(
						0,
						aarrStrLengthArray[liIndex]);
			}
			else
			{
				lsReturnString += aarrStrArray[liIndex];
			}
		}
		return lsReturnString;
	}

	/**
	 * Converts from Hex.
	 * 
	 * @param asData String
	 * @return String
	 */
	public static String convertFromHex(String asData)
	{
		String lsHex;
		Integer liCharValue;
		StringBuffer lsMyStr = new StringBuffer();
		int liMyInt = 0;
		for (int liCounter = 0;
			liCounter < asData.length();
			liCounter += 2)
		{
			lsHex = asData.substring(liCounter, liCounter + 2);
			liCharValue = Integer.valueOf(lsHex, 16);
			liMyInt = liCharValue.intValue();
			lsMyStr.append((char) liMyInt);
		}
		return lsMyStr.toString();
	}

	/**
	 * Converts to Hex.
	 * 
	 * @param asData String
	 * @return String
	 */
	public static String convertToHex(String asData)
	{
		StringBuffer lsHex = new StringBuffer();
		int liIntValue;
		for (int liIndex = 0; liIndex < asData.length(); liIndex++)
		{
			liIntValue = asData.charAt(liIndex);
			lsHex.append(Integer.toHexString(liIntValue));
		}
		return new String(lsHex.toString());
	}

	/**
	 * Object copy.
	 * 
	 * @param aaToCopy Object
	 * @return Object
	 */
	public static Object copy(Object aaToCopy)
	{
		try
		{
			ByteArrayOutputStream lpfsBAOS =
				new ByteArrayOutputStream();
			ObjectOutputStream lpfsOS =
				new ObjectOutputStream(lpfsBAOS);
			lpfsOS.writeObject(aaToCopy);
			lpfsOS.flush();
			ByteArrayInputStream lpfsIS =
				new ByteArrayInputStream(lpfsBAOS.toByteArray());
			ObjectInputStream lpfsIN = new ObjectInputStream(lpfsIS);
			Object laToReturn = lpfsIN.readObject();
			lpfsBAOS.close();
			lpfsOS.close();
			lpfsIS.close();
			lpfsIN.close();
			return laToReturn;
		}
		catch (IOException aeIOEx)
		{
			aeIOEx.printStackTrace();
			return null;
		}
		catch (ClassNotFoundException aeCNFEx)
		{
			aeCNFEx.printStackTrace();
			return null;
		}
	}

	/**
	 * Copies src file to dst file.
	 * If the dst file does not exist, it is created
	 *
	 * @param aaSrc File
	 * @param aaDst File
	 * @throws IOException
	 */
	public static void copyFile(File aaSrc, File aaDst)
		throws IOException
	{
		copyFile(aaSrc, aaDst, false);
	}

	/**
	 * Copies src file to dst file.
	 * If the dst file does not exist, it is created.
	 * If the boolean is true, delete the old file.
	 *
	 * @param aaSrc File
	 * @param aaDst File
	 * @param abDelete boolean
	 * @throws IOException
	 */
	public static void copyFile(
		File aaSrc,
		File aaDst,
		boolean abDelete)
		throws IOException
	{
		InputStream lpfsInFile = new FileInputStream(aaSrc);
		OutputStream lpfsOutFile = new FileOutputStream(aaDst);

		// Transfer bytes from in to out
		byte[] larrBuf = new byte[1024];
		int liLen;

		while ((liLen = lpfsInFile.read(larrBuf)) > 0)
		{
			lpfsOutFile.write(larrBuf, 0, liLen);
		}

		lpfsOutFile.flush();

		lpfsInFile.close();

		aaDst.setLastModified(aaSrc.lastModified());

		// delete if indicated by boolean
		if (abDelete)
		{
			aaSrc.delete();
		}

		lpfsOutFile.close();
	}

	//	/**
	//	 * Sets the wait cursor.
	//	 * 
	//	 * @param aaParent JDialog
	//	 * @param abWaitCursorOn boolean
	//	 */
	//	public void setWaitCursor(
	//		javax.swing.JDialog aaParent,
	//		boolean abWaitCursorOn)
	//	{
	//		if (abWaitCursorOn)
	//		{
	//			aaParent.setCursor(
	//				new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
	//		}
	//		else
	//		{
	//			aaParent.setCursor(
	//				new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
	//		}
	//	}

	/**
	 * Shows the Special Plates Regis Data Mfg Msg
	 * 
	 * @param aaSpclPltRegisData
	 */
	public static RTSException createSpclPltMfgInfoMsg(SpecialPlatesRegisData aaSpclPltRegisData)
	{
		String lsMfgDate = "" + aaSpclPltRegisData.getMFGDate();

		RTSException leRTSEx =
			new RTSException(
				RTSException.INFORMATION_MESSAGE,
				"A Plate Manufacture Request will be included with "
					+ " this transaction. "
					+ "  "
					+ "The Manufacture Request Date is "
					+ lsMfgDate.substring(4, 6)
					+ "/"
					+ lsMfgDate.substring(6, 8)
					+ "/"
					+ lsMfgDate.substring(0, 4)
					+ ".",
				"Plate Manufacture Request");
		leRTSEx.setBeep(RTSException.BEEP);
		return leRTSEx;
	}

	/**
	 * decrypt an RTS II Password / Supervisor Override Code 
	 * 
	 * @param asPassword String
	 * @return String
	 */
	public static String decryptPassword(String asPassword)
	{
		boolean lbToggle = true;
		char[] larrPW = new char[asPassword.length()];
		asPassword.getChars(0, asPassword.length(), larrPW, 0);
		for (int liIndex = 0; liIndex < larrPW.length; liIndex++)
		{
			if (lbToggle)
			{
				larrPW[liIndex] =
					(char) ((int) larrPW[liIndex] - SHIFT_VALUE);
				if ((int) larrPW[liIndex] > ASCII_LIMIT)
				{
					larrPW[liIndex] =
						(char) ((int) larrPW[liIndex]
							- ASCII_ADJUSTMENT);
				}
				lbToggle = false;
			}
			else
			{
				larrPW[liIndex] =
					(char) ((int) larrPW[liIndex] + SHIFT_VALUE);
				if ((int) larrPW[liIndex] > ASCII_LIMIT)
				{
					larrPW[liIndex] =
						(char) ((int) larrPW[liIndex]
							- ASCII_ADJUSTMENT);
				}
				lbToggle = true;
			}
		}
		return new String(larrPW);
	}

	public static void disableAllFields(Component aaComponent)
	{
		if (aaComponent instanceof RTSInputField
			|| aaComponent instanceof RTSDateField
			|| aaComponent instanceof RTSTimeField
			|| aaComponent instanceof JComboBox
			|| aaComponent instanceof JRadioButton
			|| aaComponent instanceof RTSPhoneField
			|| aaComponent instanceof JCheckBox)
		{
			aaComponent.setEnabled(false);

		}
		else if (aaComponent instanceof Container)
		{
			Component[] larrComponent =
				((Container) aaComponent).getComponents();

			for (int i = 0; i < larrComponent.length; i++)
			{
				disableAllFields(larrComponent[i]);
			}
		}
	}

	/**
	 * Display the url passed.
	 * 
	 * @param asURL String
	 * @deprecated
	 */
	public static void displayURL(String asURL)
	{
		// defect 6955
		//boolean windows = isWindowsPlatform();
		//String lsCMD = null;
		// defect 7898
		//	Comment out try/catch. Catch not reachable
		//try
		//{
		//if (windows)
		//{
		// cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
		//lsCMD = WIN_PATH + " " + WIN_FLAG + " " + asURL;
		// defect 7898
		//	Variable p is not used
		//Process p = Runtime.getRuntime().exec(lsCMD);
		// end defect 7898
		//}
		//else
		//{
		// Under Unix, Netscape has to be running for the "-remote"
		// command to work.  So, we try sending the command and
		// check for an exit value.  If the exit command is 0,
		// it worked, otherwise we need to start the browser.
		// cmd = 'netscape -remote openURL(
		//	http://www.javaworld.com)'
		//cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")";
		//Process p = Runtime.getRuntime().exec(cmd);
		//try
		//{
		// wait for exit code -- if it's 0, command worked,
		// otherwise we need to start the browser up.
		//int exitCode = p.waitFor();
		//if (exitCode != 0)
		//{
		// Command failed, start up the browser
		// cmd = 'netscape http://www.javaworld.com'
		//cmd = UNIX_PATH + " " + url;
		//p = Runtime.getRuntime().exec(cmd);
		//}
		//}
		//catch (InterruptedException x)
		//{
		//	System.err.println(
		//		"Error bringing up browser, cmd='" + cmd + "'");
		//	System.err.println("Caught: " + x);
		//}
		//}
		// end defect 6955
		//}
		//catch (IOException x)
		//{
		// couldn't exec browser
		//System.err.println("Could not invoke browser, command=" 
		//	+ lsCMD);
		//System.err.println("Caught: " + x);
		//}
		// end defect 7898
	}

	/**
	 * This method will do a workstation Reboot.
	 */
	public static void doReboot()
	{
		try
		{
			// in non prod status, print a message so we know call was 
			//	made
			if (SystemProperty.getProdStatus() > 0)
			{
				System.out.println("Development call to do reboot cmd");
				// exit the app when reboot is called.
				// this more closely simulates production.
				System.exit(0);
			}
			else
			{
				// do a reboot only in "Production"
				System.out.println("Production call to do reboot cmd");
				Log.write(
					Log.START_END,
					new RTSException(),
					" About to reboot");
				Process laProcess =
					Runtime.getRuntime().exec(
						"cmd.exe /c " + REBOOT_COMMAND);
				laProcess.waitFor();
			}
		}
		catch (Exception aeEx)
		{
			// If we came here, we should just exit application
			aeEx.printStackTrace();
			Log.write(
				Log.START_END,
				aeEx,
				" Exiting application because of problems while exiting");
			System.exit(0);
		}
	}

	/**
	 * This method will do a workstation shutdown.
	 */
	public static void doShutdown()
	{
		System.out.println("About to reboot");
		doReboot();
	}

	/**
	 * This method will reset the Desktop and do a Windows Logout.
	 * 
	 * @param aaAppController RTSApplicationController
	 */
	public static void doWindowsLogout(RTSApplicationController aaAppController)
	{
		try
		{
			try
			{
				// only reset the transaction if we are at the desktop.
				if (!aaAppController.isPendingTransVisible())
				{
					// defect 7705
					com
						.txdot
						.isd
						.rts
						.services
						.common
						.Transaction
						.reset();
					// end defect 7705
				}
			}
			catch (Exception aeEx)
			{
				Log.write(
					Log.START_END,
					aeEx,
					" Error when reseting transaction at logoff");
			}

			// defect 8851
			// Close controller if not null
			if (aaAppController.getMediator().getController() != null)
			{
				aaAppController.getMediator().getController().close();
			}
			// end defect 8851
			// Clean up all open windows.
			aaAppController.getMediator().cleanseStacks();

			// defect 7009
			// clean out the controller before logging out.
			//aaAppController.getMediator().getController().
			//	setAcceptingInput(false);

			//defect 8851
			//	Move line to before cleanseStacks()
			//aaAppController.getMediator().getController().close();
			// end defect 8851

			// end defect 7009

			// defect 7022
			// kill the browser before the next user logs in 
			RTSHelp.releaseBrowser();
			// end defect 7022

			// defect 7006
			// reflow to use new ChkLogoff so we do not
			// show disclaimer until user is completely logged off.

			// in non prod status, print a message so we know call was made
			if (SystemProperty.getProdStatus() > 0)
			{
				RTSDate laRTSDate = RTSDate.getCurrentDate();
				System.out.println(
					laRTSDate
						+ " - "
						+ laRTSDate.getClockTime()
						+ "Development call to do logout cmd");
				// defect 11324
				System.exit(0);
				// end defect 11324
			}
			else
			{
				// do a logout only in "Production"
				RTSDate laRTSDate = RTSDate.getCurrentDate();
				String lsLogoutDate1 =
					laRTSDate
						+ " - "
						+ laRTSDate.getClockTime()
						+ " - ";
				String lsLogoutTxt1 =
					"Production call to do logout cmd for "
						+ com
							.txdot
							.isd
							.rts
							.services
							.localoptions
							.JniAdInterface
							.getUser();
				System.out.println(lsLogoutDate1 + lsLogoutTxt1);
				Log.write(Log.START_END, lsLogoutTxt1, lsLogoutTxt1);
				// defect 11324
				System.exit(0);
				// defect 7889
				// Added back b/c it was removed in version 23
				// request the logout
				//Process laP1 =
				//	Runtime.getRuntime().exec(
				//		"cmd.exe /c " + WINDOWS_LOGOUT_COMMAND);
				//int liJunkCode = laP1.waitFor();
				// end defect 7889

				// execute the command to wait for the logout to occur.
				//Process laP2 =
				//	Runtime.getRuntime().exec(
				//		"cmd.exe /c " + CHKLOGOFF_COMMAND);
				//int liRC = laP2.waitFor();

				// report if there was a problem
				// we sometimes get 128 but works ok.  Just ignore.
				//if (liRC != 0 && liRC != 128)
				//{
				//	laRTSDate = RTSDate.getCurrentDate();
				//	String lsLogoutDateE =
				//		laRTSDate
				//			+ " - "
				//			+ laRTSDate.getClockTime()
				//			+ " - ";
				//	String lsLogoutTxtE =
				//		"There was a logout problem.  RC = " + liRC;
				//	System.out.println(lsLogoutDateE + lsLogoutTxtE);
				//	Log.write(
				//		Log.START_END,
				//		lsLogoutTxtE,
				//		lsLogoutTxtE);
				//}

				// report that the user has logged out.
				//laRTSDate = RTSDate.getCurrentDate();
				//String lsLogoutDate2 =
				//	laRTSDate
				//		+ " - "
				//		+ laRTSDate.getClockTime()
				//		+ " - ";
				//String lsLogoutTxt2 = "User is logged out";
				//System.out.println(lsLogoutDate2 + lsLogoutTxt2);
				//Log.write(Log.START_END, lsLogoutTxt2, lsLogoutTxt2);

				// sleep for 1 second to make sure 
				// logout is completely resolved
				//Thread.sleep(1000);
				// end defect 11324
			}
			// end defect 7006

			// this comes after the call to do the logout.
			aaAppController.showLoginScreen();
		}
		catch (Exception aeEx)
		{
			// If we came here, we should just exit application
			aeEx.printStackTrace();
			Log.write(
				Log.START_END,
				aeEx,
				" Exiting application because of an error while "
					+ "logging out");
			System.exit(0);
		}
	}

	/**
	 * Encrypt an RTS II asPassword.
	 * 
	 * @param asPassword String
	 * @return String
	 */
	public static String encryptPassword(String asPassword)
	{
		boolean lbToggle = true;

		char[] larrPW = new char[asPassword.length()];
		asPassword.getChars(0, asPassword.length(), larrPW, 0);
		for (int liIndex = 0; liIndex < larrPW.length; liIndex++)
		{
			if (lbToggle)
			{
				larrPW[liIndex] =
					(char) ((int) larrPW[liIndex] + SHIFT_VALUE);
				lbToggle = false;
			}
			else
			{
				larrPW[liIndex] =
					(char) ((int) larrPW[liIndex] - SHIFT_VALUE);
				lbToggle = true;
			}
		}
		return new String(larrPW);
	}

	/**
	 * Look up the default Font name for the default look and feel
	 * and return it.
	 * 
	 * <p>This will allow developers to code bold statements 
	 * and ensure that they use the default font.
	 * 
	 * @return String
	 */
	public static String getDefaultFont()
	{
		String lsFont = null;

		// get the current look and feel
		UIDefaults laUID = UIManager.getLookAndFeel().getDefaults();

		// get the font for TextPanes
		FontUIResource laFUIR =
			(FontUIResource) laUID.get(TXT_FONT_GETTER);

		// get the font name
		lsFont = laFUIR.getName();

		return lsFont;
	}

	/**
	 * Look up the default Font size for the default look and feel
	 * and return it.
	 * 
	 * <p>This will allow developers to code bold statements 
	 * and ensure that they use the default font size.
	 * 
	 * <p>Returns not found if there is not size returned.
	 * 
	 * @return int
	 */
	public static int getDefaultFontSize()
	{
		int liFontSize = CommonConstant.NOT_FOUND;

		// get the current look and feel
		UIDefaults laUID = UIManager.getLookAndFeel().getDefaults();

		// get the font for TextPanes
		FontUIResource laFUIR =
			(FontUIResource) laUID.get(TXT_FONT_GETTER);

		// get the font name
		liFontSize = laFUIR.getSize();

		return liFontSize;
	}

	/**
	 * 
	 * Return Event Type
	 * 
	 * @param asTransCd
	 * @return String
	 */
	public static String getEventType(String asTransCd)
	{
		String lsEventType = new String();

		Hashtable lhtTransCtrl =
			com
				.txdot
				.isd
				.rts
				.services
				.common
				.Transaction
				.getTransactionControl();

		TransactionControl lsTransCtl =
			(TransactionControl) lhtTransCtrl.get(asTransCd);

		if (lsTransCtl != null)
		{
			lsEventType = lsTransCtl.getEventType();
		}
		return lsEventType;
	}

	/**
	 * Return new vector of DTA transactions w/ "SKIP" 
	 * 
	 * @param avDTATrans
	 * @return Vector
	 */
	public static Vector getNewDTADlrTtlDataVectorWithSkip(Vector avDTATrans)
	{
		Vector lvWithSkip = (Vector) UtilityMethods.copy(avDTATrans);
		String lsPrevForm31No = CommonConstant.STR_SPACE_EMPTY;
		DealerTitleData laSkipDlrTtlData =
			new DealerTitleData(TitleConstant.DTA_SKIP_LABEL);

		// Determine "SKIP" if 
		//		Current or Prev Form31No not empty 
		//		Current or Prev Form31No are not equal 
		//		Form31No Prev_Next numbers not in next sequential 
		//			order
		//		Not first record

		for (int i = 0; i < lvWithSkip.size(); i++)
		{
			DealerTitleData laCurrDlrTtlData =
				(DealerTitleData) lvWithSkip.get(i);

			String lsForm31No = laCurrDlrTtlData.getForm31No().trim();

			// If break in Form31 Numbers, insert "SKIP" record
			if (lsForm31No.length() != 0
				&& lsPrevForm31No.length() != 0
				&& lsForm31No.compareTo(lsPrevForm31No) != 0
				&& MediaValidations.isItmBreak(lsPrevForm31No, lsForm31No)
				&& i != 0)
			{
				lvWithSkip.insertElementAt(
					UtilityMethods.copy(laSkipDlrTtlData),
					i);
				i++;
			}
			lsPrevForm31No = lsForm31No;
		}
		return lvWithSkip;
	}

	/**
	 * Look up the Office Issuance Code for an Office.
	 * <p>
	 * Looks up the Office Code using cache.
	 * Returns the appropriate Office Code for the county.
	 * Office Code is initialized to -1.
	 * A return of -1 means we were unsuccessful looking up the office.
	 * <p>
	 * 
	 * @param aiOfcIssuanceNo int
	 * @return int
	 */
	public static int getOfficeCode(int aiOfcIssuanceNo)
	{
		int liOfcCode = -1;
		OfficeIdsData laOfcId =
			OfficeIdsCache.getOfcId(aiOfcIssuanceNo);
		if (laOfcId != null)
		{
			liOfcCode = laOfcId.getOfcIssuanceCd();
		}
		return liOfcCode;
	}

	/**
	 * Get the TransId
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiTransWsId int
	 * @param aiTransAMDate int
	 * @param aiTransTime 
	 * @return String  
	 */
	public static String getTransId(
		int aiOfcIssuanceNo,
		int aiTransWsId,
		int aiTransAMDate,
		int aiTransTime)
	{
		return UtilityMethods.addPadding(
			new String[] {
				String.valueOf(aiOfcIssuanceNo),
				String.valueOf(aiTransWsId),
				String.valueOf(aiTransAMDate),
				String.valueOf(aiTransTime)},
			new int[] {
				CommonConstant.LENGTH_OFFICE_ISSUANCENO,
				CommonConstant.LENGTH_TRANS_WSID,
				CommonConstant.LENGTH_TRANSAMDATE,
				CommonConstant.LENGTH_TRANS_TIME },
			CommonConstant.STR_ZERO);

	}

	/**
	 * 
	 * Is String All "0" or null or Empty String  
	 * 
	 * @param asString
	 * @return boolean 
	 */
	public static boolean isAllZeros(String asString)
	{
		boolean lbReturn = true;
		if (asString != null)
		{
			asString = asString.trim().replaceAll("0", "");
			if (asString.length() != 0)
			{
				lbReturn = false;
			}
		}
		return lbReturn;
	}

	/** 
	 * Return boolean to denote if should lookup Special Plate 
	 * Data from RTS_SR_FUNC_TRANS as part of Special Plates MF Lookup 
	 * 
	 * @param asTransCd
	 * @return boolean 
	 */
	public static boolean isTransCdValidForSRFuncLookup(String asTransCd)
	{
		boolean lbReturn = false;

		if (asTransCd != null)
		{
			asTransCd = asTransCd.trim();

			// defect 10820 
			// Include SPINQ 
			lbReturn =
				asTransCd.equals(TransCdConstant.TITLE)
					|| asTransCd.equals(TransCdConstant.REJCOR)
					|| asTransCd.equals(TransCdConstant.RENEW)
					|| asTransCd.equals(TransCdConstant.EXCH)
					|| asTransCd.equals(TransCdConstant.VEHINQ)
					// defect 10820
					|| asTransCd.equals(TransCdConstant.SPINQ);
					// end defect 10820 
		}
		return lbReturn;	}

	/**
	 * Return boolean to denote if TransCd is Disabled Placard Event
	 * 
	 * @param asTransCd
	 * @return boolean 
	 */
	public static boolean isDsabldPlcrdEvent(String asTransCd)
	{
		String lsEventType = getEventType(asTransCd);
		return lsEventType.equals(TransCdConstant.ADD_DP_EVENT_TYPE)
			|| lsEventType.equals(TransCdConstant.DEL_DP_EVENT_TYPE)
			|| lsEventType.equals(TransCdConstant.REI_DP_EVENT_TYPE)
			|| lsEventType.equals(TransCdConstant.REN_DP_EVENT_TYPE)
			|| lsEventType.equals(TransCdConstant.RPL_DP_EVENT_TYPE);
	}

	/**
	 * Returns if the workstation does DTA.
	 * 
	 * @param asTransCode String
	 * @return boolean
	 */
	public static boolean isDTA(String asTransCode)
	{
		return (
			asTransCode.equals(TransCdConstant.DTAORD)
				|| asTransCode.equals(TransCdConstant.DTAORK)
				|| asTransCode.equals(TransCdConstant.DTANTD)
				|| asTransCode.equals(TransCdConstant.DTANTK));
	}

	/**
	 * Returns boolean to denote if Empty String
	 * 
	 * @param asString
	 */
	public static boolean isEmpty(String asString)
	{
		return asString == null || asString.trim().length() == 0;
	}

	/**
	 * Returns if the Mainframe is up or not.
	 * 
	 * @return boolean
	 */
	public static boolean isMFUP()
	{
		// defect 10290 
		// Implement isMFUp(); Reorganize for single return; 
		boolean lbMFUp = false;
		try
		{
			GeneralSearchData laGSD = new GeneralSearchData();
			laGSD.setKey1("REGPLTNO");
			laGSD.setKey2("BOGUS");
			laGSD.setKey3("VEHINQ");
			laGSD.setIntKey1(1);

			BusinessInterface laBusinessInterface =
				new BusinessInterface();

			VehicleInquiryData laVehInqData =
				(VehicleInquiryData) laBusinessInterface.processData(
					6,
					1,
					laGSD);

			lbMFUp = laVehInqData.isMFUp();
		}
		catch (Exception aeExc)
		{
			aeExc.printStackTrace();
		}
		return lbMFUp;
		// end defect 10290 
	}
	/**
	 * 
	* Check for "-" (negitive number) precedes a number
	*
	* @param asAmt String
	* @return boolean
	*/
	public static boolean isNegative(String asAmt)
	{
		boolean lbRtn = false;

		//	Determine if "-" precedes a number (negative).
		if (asAmt.substring(0, 1).equals("-"))
		{
			lbRtn = true;
		}

		return lbRtn;
	}

	/** 
	  * Return boolean to denote if TransCd represents a new Title
	  * 
	  * @param asTransCd
	  * @return boolean 
	  */
	public static boolean isNewTitleTransCd(String asTransCd)
	{
		return asTransCd.equals(TransCdConstant.TITLE)
			|| asTransCd.equals(TransCdConstant.NONTTL)
			|| asTransCd.equals(TransCdConstant.SCOT)
			|| asTransCd.equals(TransCdConstant.NRCOT)
			|| asTransCd.equals(TransCdConstant.COA)
			|| isDTA(asTransCd);
	}

	/**
	* Searches for non-numeric in a float
	*
	* <ul>
	* <li>Returns <b>false</b> if non-numeric found.
	* <li>Returns <b>ture</b>  if input string is numeric
	* </ul>
	*
	* @param afValue float 
	* @return boolean
	*/
	public static boolean isNumeric(float afValue)
	{
		// Set to true 
		boolean lbRtn = true;
		String lsValue = String.valueOf(afValue);

		// Check if input string is null or empty.
		//	If not, then evaluate each character.
		if (lsValue != null && lsValue.length() > 0)
		{
			// Check each digit. If the character is not
			// a digit or a decimal point, return false.
			for (int liIndex = 0;
				liIndex < lsValue.length();
				liIndex++)
			{
				if (lsValue.indexOf(".") != 0)
				{
					if (!Character
						.isDigit((char) lsValue.charAt(liIndex)))
					{
						lbRtn = false;
						break;
					}
				}
			}
		}
		else
		{
			// if input string is null or empty 
			//  return false.	
			lbRtn = false;
		}

		return lbRtn;
	}

	/**
	 * Searches for non-numeric in a string, return false
	 * This method will handle both strings and floats
	 * If number is a negative, will return false
	 *
	 * <ul>
	 * <li>Returns <b>false</b> if non-numeric found.
	 * <li>Returns <b>true</b>  if input string is numeric
	 * </ul>
	 *
	 * @param asValue String
	 * @return boolean
	 */
	public static boolean isNumeric(String asValue)
	{
		// Set to true 
		boolean lbRtn = true;
		String lsDecimalNum = "";
		int liDecIndex = 0;

		// Check if input string is null or empty.
		//	If not, then evaluate each character.
		if (asValue != null && asValue.length() > 0)
		{
			// Check for a decimal point
			//	Capture whole and decimal number
			if ((liDecIndex = asValue.indexOf(".")) != -1
				&& asValue.length() > liDecIndex + 1
				&& asValue.indexOf(".", liDecIndex + 1) == -1)
			{
				lsDecimalNum = asValue.substring(liDecIndex + 1);
				asValue = asValue.substring(0, liDecIndex);
			}
			// Check each digit. If the character is not
			// a digit, return false.
			boolean lbLoop = true;
			while (lbLoop)
			{
				for (int liIndex = 0;
					liIndex < asValue.length();
					liIndex++)
				{
					if (!Character
						.isDigit((char) asValue.charAt(liIndex)))
					{
						lbRtn = false;
						break;
					}
				}
				if (lsDecimalNum.length() != 0)
				{
					asValue = lsDecimalNum;
					lsDecimalNum = "";
				}
				lbLoop = false;
			}
		}
		else
		{
			// if input string is null or empty 
			//  return false.	
			lbRtn = false;
		}

		return lbRtn;
	}

	/**
	 * is Permit Application 
	 * 
	 * @param asTransCd String
	 * @return boolean
	 */
	public static boolean isPermitApplication(String asTransCd)
	{
		return (
			asTransCd != null
				&& (asTransCd.equals(TransCdConstant.PT144)
					|| asTransCd.equals(TransCdConstant.PT72)
					|| asTransCd.equals(TransCdConstant.PT30)
					|| asTransCd.equals(TransCdConstant.OTPT)
					|| asTransCd.equals(TransCdConstant.FDPT)));
	}

	/**
	 * is Special Plates Application Event 
	 * 
	 * @param String asTransCd 
	 * @return boolean
	 */
	public static boolean isSPAPPL(String asTransCd)
	{
		return (
			asTransCd.equals(TransCdConstant.SPAPPL)
				|| asTransCd.equals(TransCdConstant.SPAPPO)
				|| asTransCd.equals(TransCdConstant.SPAPPI)
				|| asTransCd.equals(TransCdConstant.SPAPPC)
				|| asTransCd.equals(TransCdConstant.SPAPPR));

	}

	/**
	 * Is TransCd any special plates
	 *
	 * @param asTransCd		String
	 * @return boolean
	 */
	public static boolean isSpecialPlates(String asTransCd)
	{
		return getEventType(asTransCd).equals(
			TransCdConstant.SPCLPLT_EVENT_TYPE);
	}
	
	/** 
	 * Is Salvage,NRCOT, COA DocTypeCd 
	 */
	public static boolean isSalvageTypeDocTypeCd(int aiDocTypeCd)
	{
		return aiDocTypeCd== DocTypeConstant.SALVAGE_CERTIFICATE_NO_REGIS || 
		aiDocTypeCd== DocTypeConstant.SALVAGE_CERTIFICATE ||
		aiDocTypeCd== DocTypeConstant.CERTIFICATE_OF_AUTHORITY_NO_REGIS  ||
		aiDocTypeCd== DocTypeConstant.CERTIFICATE_OF_AUTHORITY ||
		aiDocTypeCd== DocTypeConstant.SALV_TITLE_DAMAGED_NO_REG||
		aiDocTypeCd== DocTypeConstant.SALV_TITLE_DAMAGED ||
		aiDocTypeCd== DocTypeConstant.NONREPAIR_95_PLUS_LOSS_NO_REG ||
		aiDocTypeCd== DocTypeConstant.NONREPAIR_95_PLUS_LOSS;
	}
	
	/**
	 * Is VTR 275 TransCd 
	 * 
	 * @param asTransCd
	 * @return boolean 
	 */
	public static boolean isVTR275TransCd(String asTransCd)
	{
		return getEventType(asTransCd).equals(TransCdConstant.VEHINQ_EVENT_TYPE)
		&& !asTransCd.equals(TransCdConstant.VEHINQ); 
	}
	
	/**
	 * Is Vehicle Inquiry Type TransCd 
	 * 
	 * @param asTransCd
	 * @return boolean 
	 */
	public static boolean isVehInqTransCd(String asTransCd)
	{
		return getEventType(asTransCd).equals(TransCdConstant.VEHINQ_EVENT_TYPE);
	}
	
	/**
	 * Return VehInqT ransCd
	 *  
	 * @param aiPrintOption
	 * @return String 
	 */
	public static String getVehInqTransCd(int aiPrintOption)
	{
		String lsTransCd = new String(); 

		switch(aiPrintOption)
		{
			case (VehicleInquiryData.VIEW_AND_CERTFD):
			{
				lsTransCd = TransCdConstant.VDSCN; 
				break; 
			}
			case (VehicleInquiryData.VIEW_AND_CERTFD_AND_CHARGE):
			{
				lsTransCd = TransCdConstant.VDSC; 
				break; 
			}
			case (VehicleInquiryData.VIEW_AND_NONCERTFD):
			{
				lsTransCd = TransCdConstant.VDSN; 
				break; 
			}
			case (VehicleInquiryData.VIEW_AND_NONCERTFD_AND_CHARGE):
			{
				lsTransCd = TransCdConstant.VDS; 
				break; 
			}
			default:
			{
				lsTransCd = TransCdConstant.VEHINQ; 
			}
		}
		return lsTransCd; 
	}
	
	/**
	 * This is used for testing.
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		// defect 11320
		// displayURL("http://java.sun.com");
		UtilityMethods laUM = new UtilityMethods();
		System.out
				.println("Regular XP    Control Point -     R0110000  - "
						+ laUM.trimControlPoint("R0110000"));
		System.out
				.println("Regular WIN 7 Control Point - RTS-R0110000  - "
						+ laUM.trimControlPoint("RTS-R0110000"));
		System.out
		.println("Regular DMV Workstation Name        - DMV-015151-ws - "
				+ laUM.trimControlPoint("DMV-015151-ws"));
		System.out
		.println("Regular internal ip address         - 10.48.11.55   - "
				+ laUM.trimControlPoint("10.48.11.55"));
		System.out
		.println("Old style RTS ip address            - 144.48.11.55  - "
				+ laUM.trimControlPoint("144.48.11.55"));
		System.out
		.println("RTS Server HostName Test     		  - WT-RTS-TS1    - "
				+ laUM.trimControlPoint("WT-RTS-TS1"));
		System.out
		.println("RTS Server HostName Prod     		  - WT-RTS-APPL   - "
				+ laUM.trimControlPoint("WT-RTS-APPL"));
		System.out
		.println("RTS Server ip     		          - 144.45.112.5  - "
				+ laUM.trimControlPoint("144.45.112.5"));
		System.out
		.println("Old test RTS Control Point          - POS-TEST4     - "
				+ laUM.trimControlPoint("POS-TEST4"));
		System.out
		.println("Like an old test RTS Control Point  - PS-TEST41     - "
				+ laUM.trimControlPoint("PS-TEST41"));
		System.out
		.println("Unusual workstation name            - RogerRamjet   - "
				+ laUM.trimControlPoint("RogerRamjet"));
		// end defect 11320
	}
	
	/**
	 * Used to prevent NullPointerExceptions for null intolerant code 
	 *  such as java.awt.Graphics.drawString(String str,
	 *                                          int x,
	 *                                          int y)
	 * @param asString
	 * @return
	 */public static String nullSafe(String asString)
	{
		if (asString == null)
		{
			return "";
		}
		return asString;
	}

	/**
	 * Replaces single quote with two single quotes
	 * Required for passing strings with imbedded quotes to SQL 
		 * 
	 * @param asString String
	 * @return String
	 */

	public static String quote(String asString)
	{
		String lsNewString = new String();
		for (int liIndex = 0; liIndex < asString.length(); liIndex++)
		{
			String lsNextChar =
				asString.substring(liIndex, liIndex + 1);
			if (lsNextChar.equals("'"))
			{
				lsNextChar = "''";
			}
			lsNewString = lsNewString + lsNextChar;
		}
		return lsNewString;
	}

	/**
	 * Prints Permit 
	 * 
	 * @param asTransCd String
	 * @return boolean
	 */
	public static boolean printsPermit(String asTransCd)
	{
		// defect 10844 
		return asTransCd != null
			&& (isPermitApplication(asTransCd)
				|| asTransCd.equals(TransCdConstant.PRMDUP)
				|| asTransCd.equals(TransCdConstant.MODPT));
		// end defect 10844 
	}

	/** 
	 * Remove Leading Zeros
	 * 
	 * @param asString
	 * @return String
	 */
	public static String removeLeadingZeros(String asString)
	{
		int i = 0;
		if (asString != null)
		{
			char[] larrChars = asString.toCharArray();

			for (; i < asString.length(); i++)
			{
				if (larrChars[i] != '0')
				{
					break;
				}
			}
		}
		return (i == 0) ? asString : asString.substring(i);
	}

	/**
	 * Searches a string for
	 * all instances of the search value and
	 * replaces it with the replace value.
	 * 
	 * @param asOriginal String
	 * @param asSearchValue String
	 * @param asReplace String
	 * @return String
	 */
	public static String replaceString(
		String asOriginalStr,
		String asSearchValue,
		String asReplaceStr)
	{
		while (true)
		{
			int liIndexofSearch = asOriginalStr.indexOf(asSearchValue);
			if (liIndexofSearch > -1)
			{
				asOriginalStr =
					asOriginalStr.substring(0, liIndexofSearch)
						+ asReplaceStr
						+ asOriginalStr.substring(
							liIndexofSearch + asSearchValue.length(),
							asOriginalStr.length());
			}
			else
			{
				break;
			}
		}
		return asOriginalStr;
	}

	/**
	 * Remove the trailing blanks only
	 * 
	 * @param asString
	 * @return String
	 */
	public static String rTrim(String asString)
	{
		return asString.replaceAll("\\s+$", "");
	}

	/**
	* This method returns the int value of one int 
	* raised to the power of a second int.
	* <p>
	* This method is to be used in place of math.pow.
	* It is uses long.  This method uses int.
	* <p>
	* <ul>
	* <li>Any int to the power of 0 returns a 1.
	* <li>Any int to the power of 1 returns that int.
	* <li>Any int to the power of power returns base raised to that power.
	* </ul>
	* 
	* @param aiBase int
	* @param aiPower int
	* @return int
	*/
	public static int rtsPow(int aiBase, int aiPower)
	{
		// return value
		// Set to 1 to start with power of 0
		int liRetValue = 1;
		// loop through to increase the base by the power
		for (int liIndex = 0; liIndex < aiPower; liIndex++)
		{
			liRetValue = liRetValue * aiBase;
		}
		return liRetValue;
	}

	/**
	 * Save Report
	 * 
	 * On Entry: 
	 *     Key1 = FormattedRptData
	 *     Key2 = FileName
	 *     Key3 = ReportTitle
	 *  IntKey1 = NumberOfCopies
	 *  IntKey2 = OrientationCd
	 * 
	 * On Exit: 
	 *     Key1 = Generated File Name 
	 *   
	 * @param aaData
	 * @return Vector 
	 * @throws RTSException
	 */
	public static Vector addPCLandSaveReport(Object aaData)
		throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;

		Vector lvReports = new Vector();

		String lsPageProps = Print.getDefaultPageProps();

		if (laRptSearchData.getIntKey2() == ReportConstant.LANDSCAPE)
		{
			lsPageProps =
				lsPageProps.substring(0, 2)
					+ Print.getPRINT_LANDSCAPE()
					+ lsPageProps.substring(2);
		}

		String lsRpt =
			lsPageProps
				+ Print.getPRINT_TRAY_2()
				+ CommonConstant.SYSTEM_LINE_SEPARATOR
				+ laRptSearchData.getKey1();

		String lsFileName =
			saveReport(
				lsRpt,
				laRptSearchData.getKey2(),
				laRptSearchData.getIntKey1());

		laRptSearchData.setKey1(lsFileName);
		lvReports.add(laRptSearchData);
		return lvReports;
	}

	/**
	 * This method is used to save all reports. This also handles
	 * saving iterations of reports.
	 *
	 * @param asStr String
	 * @param asRptName String
	 * @param aiNumFiles int
	 * @throws RTSException
	 */
	public static synchronized String saveReport(
		String asStr,
		String asRptName,
		int aiNumFiles)
		throws RTSException
	{
		// Directory Listing 
		File[] larrFile = null;

		File laFile = null;
		String lsFileName = "";

		// Manage Names of Files Found 
		Hashtable lhtFileNames = new Hashtable();

		// For Workstation Funds Reports
		String lsWsId =
			UtilityMethods.addPadding(
				"" + SystemProperty.getWorkStationId(),
				3,
				"0");

		// Use to manage Renaming 	
		int liMaxVersionNoFound = 0;
		int liMaxVersionNo = 0;

		// Rename if Multiple Iterations and matching File Names Found 
		boolean lbRename = false;

		// aiNumFiles = 0 for Funds Workstation Reports.  
		// Still, liMaxVersionNo = 6 
		if (aiNumFiles != 1)
		{
			liMaxVersionNo = 6;

			// defect 10012
			// No longer true in Windows  
			// defect 7702
			// report filename length can only be 12 with .RPT so
			// the filename cannot be more than 8.  Since a number
			// is added to the end then anything greater than 7 needs
			// to be removed.
			//	if (asRptName.length() > 7)
			//	{
			//		asRptName = asRptName.substring(0, 7);
			//	}
			// end defect 7702
			// end defect 10012  
		}

		try
		{
			laFile = new File(SystemProperty.getReportsDirectory());

			// Guarantee Reports Directory Exists 
			if (!laFile.exists())
			{
				laFile.mkdir();
			}

			larrFile = laFile.listFiles();

			// For every item in Reports Directory 
			for (int i = 0; i < larrFile.length; i++)
			{
				//If directory, ignore
				if (larrFile[i].isDirectory())
				{
					continue;
				}

				String lsRptDirObjectName = larrFile[i].getName();

				int liDotPos = lsRptDirObjectName.indexOf(".");

				//File does not have any extension, ignore
				if (liDotPos == -1)
				{
					continue;
				}

				String lsCurrentFileName =
					lsRptDirObjectName.substring(0, liDotPos);

				// defect 6995
				// Determine file extension 
				String lsFileExtension = "";
				if (lsRptDirObjectName.length() > liDotPos)
				{
					lsFileExtension =
						lsRptDirObjectName.substring(liDotPos + 1);
				}
				// end defect 6995 

				if (aiNumFiles == ReportConstant.RPT_1_COPY
					&& lsCurrentFileName.toUpperCase().equals(asRptName))
				{
					larrFile[i].delete();
					break;
				}
				// If 7 copies and starts w/ "asRptName", VersionNo is 
				// last character in the name   (Batch, Subcon Rpt, etc.) 
				else if (
					aiNumFiles == ReportConstant.RPT_7_COPIES
						&& lsCurrentFileName.toUpperCase().startsWith(
							asRptName))
				{
					String lsVersionNo =
						lsRptDirObjectName.substring(
							asRptName.length(),
							liDotPos);

					// Online Reports start with same characters, 
					//  end with "ONLN" 
					if (lsVersionNo != null
						&& !lsVersionNo.equals("")
						&& !lsVersionNo.equalsIgnoreCase("N"))
					{
						lhtFileNames.put(
							new Integer(lsVersionNo),
							larrFile[i]);

						int liVersionNo =
							new Integer(lsVersionNo).intValue();

						// Maintain Maximum Version for Renaming  
						if (liMaxVersionNoFound < liVersionNo)
						{
							liMaxVersionNoFound = liVersionNo;
						}
					}
					// I do not believe the following 2 else statements
					// are required. 
					else if (lsVersionNo.equalsIgnoreCase("N"))
					{
						lhtFileNames.put("n", larrFile[i]);
					}
					else
					{
						lhtFileNames.put(new Integer(0), larrFile[i]);
					}
					lbRename = true;
				}
				// Workstation Funds Reports start w/ WsId 
				//   4th character is the VersionNo  
				else
				{
					// defect 6995
					// Ensure that
					//   - filename length is correct
					//   - filename prefix matches the workstation id
					//   - file extension ends with "RPT"
					if (lsCurrentFileName
						.toUpperCase()
						.endsWith(asRptName)
						&& lsCurrentFileName.startsWith(lsWsId)
						&& lsCurrentFileName.length()
							== asRptName.length()
								+ FUNDS_FILENAME_PREFIX_LENGTH
						&& lsFileExtension.equalsIgnoreCase(
							FUNDS_FILE_EXTENSION))
					{
						// Get Version Number for Funds Reports 
						String lsVersionNo =
							lsRptDirObjectName.substring(3, 4);

						if (lsVersionNo != null
							&& !lsVersionNo.equals(""))
						{
							lhtFileNames.put(
								new Integer(lsVersionNo),
								larrFile[i]);

							int liVersionNo =
								new Integer(lsVersionNo).intValue();

							if (liMaxVersionNoFound < liVersionNo)
							{
								liMaxVersionNoFound = liVersionNo;
							}
						}
						// And this ? 
						else
						{
							lhtFileNames.put(
								new Integer(0),
								larrFile[i]);
						}
						lbRename = true;
					}
					// end defect 6995	
				}
			}
			//Rename/delete  files
			if (lbRename)
			{
				for (int j = liMaxVersionNoFound; j >= 0; j--)
				{
					File laRptFile =
						(File) lhtFileNames.get(new Integer(j));

					if (laRptFile == null || j > liMaxVersionNo + 1)
					{
						continue;
					}
					// If 7th version (suffix of 6) 
					if (j == liMaxVersionNo)
					{
						laRptFile.delete();
					}
					else
					{
						// liMaxVersionNo = 6, aiNumFiles = 7 
						//   if !Funds Workstation Report and 
						//   keep multiple versions  
						if (liMaxVersionNo == 6 && aiNumFiles == 7)
						{
							// defect 6214 (XP) 
							laRptFile.renameTo(
							// All iterations need to be renamed to upercase
							//	and not lowercase .RPT
							// new File(SystemProperty.getReportsDirectory()
							//	+ asRptName + (liJIndex + 1) + ".rpt"));
							new File(
								SystemProperty.getReportsDirectory()
									+ asRptName
									+ (j + 1)
									+ ".RPT"));
						}
						// aiNumFiles = 0 for Funds Workstation Reports 
						else
						{
							laRptFile.renameTo(
								new File(
									SystemProperty
										.getReportsDirectory()
										+ lsWsId
										+ (j + 1)
										+ asRptName
										+ ".RPT"));
						}
					}
				}
			}
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		// Create new file.
		try
		{
			if (aiNumFiles == ReportConstant.RPT_7_COPIES)
			{
				lsFileName =
					SystemProperty.getReportsDirectory()
						+ asRptName
						+ "0.RPT";
			}
			else
			{
				if (aiNumFiles == ReportConstant.RPT_1_COPY)
				{
					lsFileName =
						SystemProperty.getReportsDirectory()
							+ asRptName
							+ ".RPT";
				}
				// Funds Workstation Reports 
				else
				{
					lsFileName =
						SystemProperty.getReportsDirectory()
							+ lsWsId
							+ "0"
							+ asRptName
							+ ".RPT";
				}
			}
			FileOutputStream lpfsFileOutputStream =
				new FileOutputStream(lsFileName, true);
			PrintWriter laPrintWriter =
				new PrintWriter(lpfsFileOutputStream);
			laPrintWriter.write(asStr);
			laPrintWriter.close();
			lpfsFileOutputStream.close();
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}
		return lsFileName;
	}

	/**
	 * This method will shrink the file only if it's over the max lines.
	 * The file will be cut down to the offset so that so of the content
	 * that was there will still be there.
	 * 
	 * @param asFileFame String
	 */
	public static synchronized void shrinkLogFile(String asFileFame)
	{
		final int MAX_OFFSET = 4096;
		final int LINES_TO_KEEP = 1000;
		FileInputStream lpfsFI = null;
		BufferedReader laIn = null;
		FileOutputStream lpfsFO = null;
		PrintWriter laOut = null;

		try
		{
			// defect 8232
			// Added check to make sure that the log file exists before
			// we try to shrink it.
			if (new File(asFileFame).exists())
			{
				int liMAX_SIZE =
					SystemProperty.getMaxLogFileSize() * 1000;
				lpfsFI = new FileInputStream(asFileFame);
				laIn =
					new BufferedReader(new InputStreamReader(lpfsFI));
				if (lpfsFI.available() > liMAX_SIZE - MAX_OFFSET)
				{
					Vector lvLines = new Vector();
					String lsLine = "";
					while ((lsLine = laIn.readLine()) != null)
					{
						lvLines.add(lsLine);
					}
					lpfsFI.close();
					lpfsFO = new FileOutputStream(asFileFame, false);
					laOut = new PrintWriter(lpfsFO);
					int liSize = lvLines.size();

					// Shrink the number of lines if greater than 
					// LINES_TO_KEEP.
					if (liSize > LINES_TO_KEEP)
					{
						for (int liIndex = (liSize - LINES_TO_KEEP);
							liIndex < liSize;
							liIndex++)
						{
							laOut.println(lvLines.get(liIndex));
						}
					}
					// defect 8219
					// All closing is done in the finally
					//out.flush();
					//out.close();
					// end defect 8219
				}
			}
			else
			{
				System.out.println("File not found " + asFileFame);
			}
			// end defect 8232
		}
		catch (IOException aeIOEX)
		{
			// defect 8219
			// Added so that we can check the system out log to see
			// any io errors
			aeIOEX.printStackTrace();
			// end defect 8219
		}
		// defect 8219
		// Added finally so that we could handle all file closing
		finally
		{
			if (laIn != null)
			{
				try
				{
					laIn.close();
				}
				catch (Exception aeEx)
				{
				}
			}
			if (lpfsFI != null)
			{
				try
				{
					lpfsFI.close();
				}
				catch (Exception aeEx)
				{
				}
			}
			if (laOut != null)
			{
				try
				{
					laOut.flush();
					laOut.close();
				}
				catch (Exception aeEx)
				{
				}
			}
			if (lpfsFO != null)
			{
				try
				{
					lpfsFO.close();
				}
				catch (Exception aeEx)
				{
				}
			}
		}
		// end defect 8219
	}

	/**
	 * This method sorts the passed vector.
	 * 
	 * @param avVect Vector
	 */
	public static void sort(Vector avVect)
	{
		Collections.sort(avVect);
	}

	/**
	 * Trim Components - recursive   
	 */
	public static void trimRTSInputField(Component aaComponent)
	{
		if (aaComponent instanceof RTSInputField
			&& aaComponent.isEnabled())
		{
			((RTSInputField) aaComponent).setText(
				((RTSInputField) aaComponent).getText().trim());
		}
		else if (aaComponent instanceof Container)
		{
			Component[] larrComponent =
				((Container) aaComponent).getComponents();

			for (int i = 0; i < larrComponent.length; i++)
			{
				trimRTSInputField(larrComponent[i]);
			}
		}
	}
	
	/**
	 * Trim the RTS Control Point to understandable parts for database 
	 * handling.
	 * 
	 * @param asControlPoint
	 * @return String
	 */
	public String trimControlPoint(String asControlPoint)
	{
		String lsReturnString = "";
		
		if (asControlPoint.substring(3, 4).equals("-"))
		{
			// if this is an RTS WIN7 Control Point or a DMV workstation
			// remove the prefix.
			lsReturnString = asControlPoint.substring(4);
		}
		else if (asControlPoint.substring(0,3).equals("10."))
		{
			// 10.* is internal network.
			// remove the first two stansas.
			lsReturnString = asControlPoint.substring(3);
			int liPtr = lsReturnString.indexOf(".");
			lsReturnString = lsReturnString.substring(liPtr + 1);
		}
		else if (asControlPoint.substring(0, 3).equalsIgnoreCase("WT-"))
		{
			// if this is an RTS Server
			lsReturnString = asControlPoint.substring(3);
		}
		else if (asControlPoint.substring(0, 7).equalsIgnoreCase("144.45."))
		{
			// if this is an RTS Server
			lsReturnString = asControlPoint.substring(7);
		}
		else
		{
			// Just copy it.
			lsReturnString = asControlPoint;
		}
		
		if (lsReturnString.length() > 8)
		{
			// trim the length to 7.
			lsReturnString = lsReturnString.substring(0, 8);
		}
			
		return lsReturnString;
	}
}