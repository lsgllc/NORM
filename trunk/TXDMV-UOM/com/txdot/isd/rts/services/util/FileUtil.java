package com.txdot.isd.rts.services.util;

import java.io.*;
import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
// defect 7736
// defect 7898
//import com.txdot.isd.rts.services.util.constants.*;
// end defect 7898
// end defect 7736

/*
 *
 * FileUtil.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		12/10/2004	Add method to verify diskette is in A: drive
 *							and if DLRTITLE.DAT or SUBCON.DAT is on	disk.
 *							add import
 *							com.txdot.isd.rts.services.util.constants
 *							add checkADriveForDisk()
 *							defect 7736 Ver 5.2.2
 * K Harrell	12/16/2004	Formatting/JavaDoc/Variable Name Cleanup
 *							defect 7736 Ver 5.2.2 
 * J Rue			06/28/2005	Remove import,Code clean up
 *							defect 7898 Ver 5.2.3
 * J Rue		07/18/2005	Move local constants to class level
 * 							defect 7898 Ver 5.2.3
 * J Rue		12/13/2005	Shift code 1 tab spacing.
 * 							defect 7898 Ver 5.2.3
 * J Rue		12/17/2007	Log FileNotFoundException
 * 							modify copyFile()
 * 							defect 8513 Ver Defect-POS-A	
 * J Rue		08/26/2008	Add methods to copy/writer file to vector
 * 							add writeVectorToFile(), copyFileToVector()
 * 							defect 8984 Ver ELT_MfAccess
 * J Rue		10/09/2008	Change equals to equalsIgnoreCase
 * 							Add a close to end of processing
 * 							modify copyFileToVector(), 
 * 								writeVectorToFile()
 * 							defect 8984 Ver ELT_MfAccess
 * J Rue		10/16/2008	Delete existing file
 * 							modify writeVectorToFile()
 * 							defect 8984 Ver ELT_MfAccess
 * J Rue		12/17/2008	Copy code from defect 8984 10/09/2008 thru
 * 							10/16/2008
 * 							defect 8984 Ver Defect_POS_C
 * J Rue		02/13/2009	Delete file from folder (BuildFundsUpdate)
 * 							add deleteFile()
 * 							defect 8984 Ver Defect_POS_D
 * B Hargrove	06/03/2009  Add Flashdrive option to DTA.
 * 							add determineIfExternalDriveReady()
 * 							modify checkADrvForDisk(), copyFile()
 * 							defect 10075 Ver Defect_POS_F
 * J Rue		06/10/2009	Add message for deleting exist file and data
 * 							lost message if duplicate file is not deleted
 * 							modify writeVectorToFile()
 * 							defect 10080 Ver Defect_POS_F
 * J Rue		06/17/2009	Moved setDefaultMfVerCd() from UtilityMethods
 * 							add setDefaultMfVerCd()
 * 							defect 10080 Ver Defect_POS_F  
 * J Rue		10/14/2009	Initialize cbFileOpened = false
 * 							modify cbFileOpened
 * 							defect 10080 Ver Defect_POS_G
 * M Reyes		09/16/2011	Add external drive option B
 * 							modify determineIfExternalDriveReady()
 * 							defect 11002 Ver 6.8.1 
 * ---------------------------------------------------------------------
 */

/**
 * File Utility
 *
 * @version	6.8.1	09/16/2011
 * @author	Ashish Mahajan
 * <p>Creation Date:		10/29/2001 14:10:53
 */

public class FileUtil
{
	// Printwriter for writing into file.
	private static PrintWriter caPrintWriter = null;
	// Flag to test whether file is already opened.
	private static boolean cbFileOpened = false;
	// defect 10075
	// Use variable denoting diskette or flashdrive file
	//private static final String DTAFILE = "A:\\DLRTITLE.DAT";
	private static final String SUBCONFILE = "A:\\SUBCON.DAT";
	// end defect 10075
	private static final String DTATRANSCD = "DTA";
	private static final String ADRIVE = "A:\\";
	private static final String SUBCONTRANSCD = "SBRNW";

	// defect 10080
	//	BuildSendTrans and BuildFundsUpdtae class variables
	private final static String FILE_EXIST =
		"File exist. Do you want to over write this file?";
	private final static String COPY_FILE_UTILITY = "Copy File Utility";
	private final static String FILE_NOT_SAVED =
		"File will not be saved. Updates will be lost";
	
	// Set file location for server.cfg retrieval
	// BuildFundsUpdate and BuildSendTransaction
	private static final String SERVER_CONFIG = "cfg/server";
	private static final String FILE_EXTENSION = "cfg";
	private static final String MFINTERFACEVERSIONCD =
		"MFInterfaceVersionCode";
	// end defect 10080

	/**
	 * Determine if the file DLRTILE.DAT or SUBCON.DAT
	 * is on the A: drive.
	 * <ol>
	 * <li>Return true	if Diskette in the A: drive and 
	 * <li>  DLRTITLE.DAT or SUBCON.DAT on diskette
	 * <eol>
	 * 
	 * @param asTransCd java.lang.String
	 * @return boolean
	 */
	public static boolean checkADrvForDisk(String asTransCd)
	{
		boolean lbRtn = false;
		String lsFileName = "";
		if (asTransCd.startsWith(DTATRANSCD))
		{
			// defect 10075
			// Use variable denoting diskette or flashdrive file
			//lsFileName = DTAFILE;
			lsFileName = SystemProperty.getExternalMedia();
			// end defect 10075
		}
		else
		{
			if (asTransCd.equals(SUBCONTRANSCD))
			{
				lsFileName = SUBCONFILE;
			}
		}
		// Test for existence of file 	
		if (lsFileName != "")
		{
			java.io.File laFile = new java.io.File(lsFileName);
			if (laFile.exists())
			{
				// diskette in A drive and file exists 
				lbRtn = true;
			}
		}
		return lbRtn;
	}
	/**
	 * Copy all files from asOldPath to asNewPath
	 * 
	 * @param asOldPath java.lang.String
	 * @param asNewPath java.lang.String
	 * @throws RTSException
	 */
	public static void copyAllFiles(String asOldPath, String asNewPath)
		throws RTSException
	{
		File laFile = new File(asOldPath);
		File[] larrFile = laFile.listFiles();
		String lsCacheFileName = null;
		for (int liIndex = 0; liIndex < larrFile.length; liIndex++)
		{
			lsCacheFileName = larrFile[liIndex].getName();
			if (!larrFile[liIndex].isDirectory())
				copyFile(
					asOldPath + lsCacheFileName,
					asNewPath + lsCacheFileName);
		}
	}
	/**
	 * Copy file 
	 * 
	 * @param asFromFileName java.lang.String
	 * @param asToFileName java.lang.String
	 * @throws RTSException 
	 */
	public static void copyFile(
		String asFromFileName,
		String asToFileName)
		throws RTSException
	{
		File laFNew = null;
		File laFOld = null;
		laFNew = new File(asToFileName);
		laFOld = new File(asFromFileName);
		//If already exists delete the file
		if (laFNew.exists())
		{
			laFNew.delete();
		}
		try
		{
			FileInputStream lpfsFInp = new FileInputStream(laFOld);
			FileOutputStream lpfsFOut = new FileOutputStream(laFNew);
			int liIndex;
			while ((liIndex = lpfsFInp.read()) != -1)
			{
				lpfsFOut.write(liIndex);
			}
			lpfsFInp.close();
			lpfsFOut.close();
		}
		catch (FileNotFoundException aeFNFEX)
		{
			// defect 8513
			//	Log FileNotFoundException
			Log.write(Log.START_END, aeFNFEX, aeFNFEX.toString());
			// end defect 8513

			throw new RTSException(RTSException.FAILURE_MESSAGE,
			// defect 10075
			// Change verbiage to external media
			//"A:\\ file not found",
			"File not found on external media",
			// end defect 10075
			"ERROR");
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.FAILURE_MESSAGE,
			// defect 10075
			// Change verbiage to external media
			//"A:\\ file not found",
			"File not found on external media",
			// end defect 10075
			"ERROR");
		}
	}
	/**
	 * Write String to File  
	 *
	 * @param asFileName String
	 * @param asStr String
	 * @throws  RTSException
	 */
	public static void writeInFile(String asFileName, String asStr)
		throws RTSException
	{
		try
		{
			if (!cbFileOpened)
			{
				FileOutputStream lpfsFileOutputStream =
					new FileOutputStream(asFileName, true);
				caPrintWriter = new PrintWriter(lpfsFileOutputStream);
				cbFileOpened = true;
			}
			caPrintWriter.println(asStr);
			caPrintWriter.flush();
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}

	}
	/**
	 * Used by BuildSendTrans an BuildFundsUpdate
	 * Write Vector to File. Data will be appended to file  
	 *
	 * @param asFileName File name
	 * @param avData 	 data
	 * @throws  RTSException
	 */
	public static void writeVectorToFile(
		String asFileName,
		Vector avData)
		throws RTSException
	{
		try
		{
			//If already exists delete the file
			File laFNew = new File(asFileName);
			if (laFNew.exists())
			{
				RTSException leRTSEx1 =
					new RTSException(
						RTSException.CTL001,
						FILE_EXIST,
						COPY_FILE_UTILITY);
				int liAns = leRTSEx1.displayError();
				if (liAns == 2)
				{
					laFNew.delete();
				}
				else
				{
					RTSException leRTSEx2 =
						new RTSException(
							RTSException.INFORMATION_MESSAGE,
							FILE_NOT_SAVED,
							COPY_FILE_UTILITY);
					leRTSEx2.displayError();
				}
			}

			if (!cbFileOpened)
			{
				FileOutputStream lpfsFileOutputStream =
					new FileOutputStream(asFileName, true);
				caPrintWriter = new PrintWriter(lpfsFileOutputStream);
				cbFileOpened = true;
			}
			for (int liIndex = 0; liIndex < avData.size(); liIndex++)
			{
				caPrintWriter.println((String) avData.get(liIndex));
			}
			caPrintWriter.flush();
			caPrintWriter.close();
			cbFileOpened = false;

		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}

	}

	/**
	 * FileUtil constructor comment.
	 */
	public FileUtil()
	{
		super();
	}
	/**
	 * Reads a file. Move data to a vector
	 * 
	 * @param asFileName String Specifies the heading 
	 * @return Vector
	 */
	public static Vector copyFileToVector(String asFileName)
	{
		Vector lvFile = new Vector();

		try
		{
			//FileReader constructor to read SendTrans file. 
			// Write to beffered area). 
			FileReader laInpFile = new FileReader(asFileName);
			BufferedReader laBuffFile = new BufferedReader(laInpFile);
			String lsReadLine = CommonConstant.STR_SPACE_EMPTY;

			// Read SendTrans file
			while ((lsReadLine = laBuffFile.readLine()) != null)
			{
				// defetc 8984
				// Change equal to equalsIgnoreCase
				// Check for blank lines
				if (lsReadLine == null
					|| lsReadLine.trim().equalsIgnoreCase(
						CommonConstant.STR_SPACE_EMPTY))
				{
					continue;
				}
				// end defect 8984

				lvFile.add(lsReadLine);
			}

			// Close file
			laInpFile.close();
		}
		catch (java.io.IOException aeIOEx)
		{
			aeIOEx.printStackTrace();
		}

		return lvFile;
	}
	/**
	 * Delete file from folder. 
	 * 
	 * This method is used by MfFundsUpdate to remove test files.
	 * 
	 * @param asFileName java.lang.String
	 * @return boolean
	 * @throws RTSException 
	 */
	public static boolean deleteFile(String asFileName)
	{
		boolean lbRtn = false;
		File laFileToBeDeleted = null;
		laFileToBeDeleted = new File(asFileName);
		//If already exists delete the file
		if (laFileToBeDeleted.exists())
		{
			laFileToBeDeleted.delete();
			lbRtn = true;
		}
		return lbRtn;
	}
	/**
	 * This method determines which external media to use,  
	 * flash drive or diskette.
	 * 
	 * @param asFileName java.lang.String
	 * @throws RTSException 
	 */
	public static void determineIfExternalDriveReady(String asFileName)
		throws RTSException
	{	// defect 11002
		File laDisketteDriveA = new File(ADRIVE);
		String BDRIVE = "B:\\";
		File laDisketteDriveB = new File(BDRIVE);
		File laFlashDrive =
			new File(SystemProperty.getFlashDriveSourceDirectory());
		// Check to see if neither diskette nor flash drive is present
		if (!laDisketteDriveA.exists() && !laDisketteDriveB.exists() && !laFlashDrive.exists())
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_EXTERNAL_MEDIA_NOT_FOUND);
		}
		// Check to see if diskette and flash drive is present
		else if (laDisketteDriveA.exists() && !laDisketteDriveB.exists() && laFlashDrive.exists())
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_BOTH_DEVICES_IN_USE);
		}
		// Check to see if diskette drive is present
		else if (laDisketteDriveA.exists())
		{
			SystemProperty.setExternalMedia(ADRIVE + asFileName);
		}
		else if (laDisketteDriveB.exists())
		  {
		   SystemProperty.setExternalMedia(BDRIVE + asFileName);
		  }
		// end defect 11002

		else
		{
			SystemProperty.setExternalMedia(
				SystemProperty.getFlashDriveSourceDirectory()
					+ asFileName);
		}
	}

	/**
	 *  Used by BuildFundsUpdate and SendTransaction
	 *	Read server.cfg to get MFInterfaceVersionCode
	 *	(Note: MFInterfaceVersionCode is server in server.cfg and is
	 *		not called at start up.)
	 * 		Make call to get MfInterfaceVerCd from server.cfg
	 * @return String
	 */
	public static String setDefaultMfVerCd()
	{
		String lsMfInterfaceVerCd = CommonConstant.STR_SPACE_EMPTY;
	
		//  Read server.cfg to get MFInterfaceVersionCode
		//	(Note: MFInterfaceVersionCode is server in server.cfg and is
		//			not called at start up.)
		Vector lvData =
			FileUtil.copyFileToVector(
				SERVER_CONFIG + "." + FILE_EXTENSION);
		// Read through vector to find MFINTERFACEVERSIONCD match. 
		//	Return MfInterfaceVersionCode.
		if (lvData != null && lvData.size() > 0)
		{
			for (int liIndex = 0; liIndex < lvData.size(); liIndex++)
			{
				try
				{
					if (((String) lvData.get(liIndex)).length()
						> MFINTERFACEVERSIONCD.length()
						&& ((String) lvData.get(liIndex))
							.substring(0, MFINTERFACEVERSIONCD.length())
							.equals(MFINTERFACEVERSIONCD))
					{
						lsMfInterfaceVerCd =
							((String) lvData.get(liIndex)).substring(
								MFINTERFACEVERSIONCD.length() + 1);
					}
				}
				catch (StringIndexOutOfBoundsException leSIOBExc)
				{
					RTSException leRTSEx =
						new RTSException(
							RTSException.JAVA_ERROR,
							leSIOBExc);
					leRTSEx.displayError();
				}
			}
		}
		
		return lsMfInterfaceVerCd;
	}

}
