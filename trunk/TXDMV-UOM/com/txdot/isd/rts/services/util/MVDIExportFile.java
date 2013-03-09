package com.txdot.isd.rts.services.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
 * MVDIExportFile.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/23/2008	Created.
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/02/2008	Added Zip function
 * 							modify write()
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	10/16/2009	add reInitFiles()
 * 							add write(String,String,boolean)
 * 							delete write(String,String)
 * 							defect 9942 Ver Defect_POS_G 
 * ---------------------------------------------------------------------
 */

/**
 * A Class to write to MVDIExport
 *
 * @version	Defect_POS_G	10/16/2009
 * @author	K Harrell
 * <br>Creation Date:		10/23/2008
 */
public class MVDIExportFile
{
	private static final String ZIP_EXTENSION = ".zip";

	/**
	 * MVDIExportFile constructor.
	 */
	public MVDIExportFile()
	{
		super();
	}

	/**
	 * Delete the Existing File/Zip File
	 * 
	 * @param asFileName
	 */
	public synchronized static void reInitFiles(String asFileName)
	{
		String lsOutFileName = asFileName + ZIP_EXTENSION;
		File laExportFile = new File(asFileName);
		File laZipExportFile = new File(lsOutFileName);

		// Delete the Unzipped / Zipped Files  
		if (laExportFile.exists())
		{
			laExportFile.delete();
		}
		if (laZipExportFile.exists())
		{
			laZipExportFile.delete();
		}
	}

	//	/**
	//	 * Writes messages to the MVDIExport file
	//	 * 
	//	 * @param asExportData - String
	//	 * @return boolean 
	//	 */
	//	public synchronized static boolean write(
	//		String asExportData,
	//		String asFileName)
	//	{
	//		boolean lbSuccessfulWrite = false;
	//		try
	//		{
	//			String lsOutFileName = asFileName + ZIP_EXTENSION;
	//			File laExportFile = new File(asFileName);
	//			File laZipExportFile = new File(lsOutFileName);
	//
	//			// Delete the Unzipped / Zipped Files  
	//			if (laExportFile.exists())
	//			{
	//				laExportFile.delete();
	//			}
	//			if (laZipExportFile.exists())
	//			{
	//				laZipExportFile.delete();
	//			}
	//
	//			// Write the Unzipped File 
	//			FileOutputStream laFile =
	//				new FileOutputStream(asFileName, true);
	//			PrintWriter laPWOut = new PrintWriter(laFile);
	//			laPWOut.println(asExportData);
	//			laPWOut.flush();
	//			laPWOut.close();
	//			laFile.close();
	//
	//			// Create/Write the Unzipped file 
	//			byte[] lbBuff = new byte[1024];
	//
	//			ZipOutputStream laOutFile =
	//				new ZipOutputStream(
	//					new FileOutputStream(lsOutFileName));
	//
	//			FileInputStream laIn = new FileInputStream(asFileName);
	//
	//			// Add ZIP entry to output stream.
	//			laOutFile.putNextEntry(new ZipEntry(asFileName));
	//
	//			// Transfer bytes from the file to the ZIP file
	//			int liLen;
	//			while ((liLen = laIn.read(lbBuff)) > 0)
	//			{
	//				laOutFile.write(lbBuff, 0, liLen);
	//			}
	//
	//			// Complete the entry
	//			laOutFile.closeEntry();
	//			laOutFile.close();
	//			laIn.close();
	//
	//			lbSuccessfulWrite = true;
	//		}
	//		catch (IOException aeIOEx)
	//		{
	//			System.out.println(
	//				"Got an IOException writing to the "
	//					+ "MVDIExport File ");
	//		}
	//		return lbSuccessfulWrite;
	//	}

	/**
	 * Writes messages to the MVDIExport file
	 * 
	 * @param asExportData - String
	 * @param asFileName
	 * @param abFinal 
	 * @return boolean 
	 */
	public synchronized static boolean write(
		String asExportData,
		String asFileName,
		boolean abFinal)
	{
		boolean lbSuccessfulWrite = false;
		try
		{
			String lsOutFileName = asFileName + ZIP_EXTENSION;

			// Write the Unzipped File 
			FileOutputStream laFile =
				new FileOutputStream(asFileName, true);
			PrintWriter laPWOut = new PrintWriter(laFile);
			laPWOut.println(asExportData);
			laPWOut.flush();
			laPWOut.close();
			laFile.close();

			if (abFinal)
			{
				// Create/Write the Unzipped file 
				byte[] lbBuff = new byte[1024];

				ZipOutputStream laOutFile =
					new ZipOutputStream(
						new FileOutputStream(lsOutFileName));

				FileInputStream laIn = new FileInputStream(asFileName);

				// Add ZIP entry to output stream.
				laOutFile.putNextEntry(new ZipEntry(asFileName));

				// Transfer bytes from the file to the ZIP file
				int liLen;
				while ((liLen = laIn.read(lbBuff)) > 0)
				{
					laOutFile.write(lbBuff, 0, liLen);
				}

				// Complete the entry
				laOutFile.closeEntry();
				laOutFile.close();
				laIn.close();
			}

			lbSuccessfulWrite = true;
		}
		catch (IOException aeIOEx)
		{
			System.out.println(
				"Got an IOException writing to the "
					+ "MVDIExport File ");
		}
		return lbSuccessfulWrite;
	}
}
