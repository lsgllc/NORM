package com.txdot.isd.rts.client.systemcontrolbatch.ui;

import java.io.*;
import java.util.Vector;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/*
 *
 * JZip.java 
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7897 Ver 5.2.3 
 * Jeff S.		07/08/2005	Code Cleanup for Java 1.4.2 upgrade
 * S Johnston				modify 
 *							defect 7897 ver 5.2.3
 * Jeff S.		07/12/2005	Add String Constants.
 * 							defect 7897 ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * Zips the specified file into an output file
 *
 * @version	5.2.3			07/12/2005
 * @author	Michael Abernethy
 * <br>Creation Date:		10/17/2001 07:41:20
 */
public class JZip
{
	protected String csOutputName;
	protected Vector cvInputNames = null;
	protected int ciCompressMethod = ZipEntry.DEFLATED;
	/**
	 * String Constants
	 */
	private static final String THE_FILE = "The File '";
	private static final String THE_DIRECTORY = "The directory '";
	private static final String NO_READ = "' can't be read.";
	
	/**
	 * JZip constructor.
	 */
	public JZip()
	{
		// empty code block
	}
	/**
	 * Constructor
	 * 
	 * @param asOutputName String - output zip file name or
	 * 	unzip output path
	 * @param avInNames Vector - input files or directories
	 * 	names to zip or unzip
	 */
	public JZip(String asOutputName, Vector avInNames)
	{
		csOutputName = asOutputName;
		cvInputNames = avInNames;
	}
	/**
	 * setMethod - Set zip compress method type
	 * 
	 * @param aiMethod int - set method as ZipEntry.STORED to uncompress
	 *  or ZipEntry.DEFLATED to compress
	 */
	public void setMethod(int aiMethod)
	{
		if (aiMethod == ZipEntry.STORED
			|| aiMethod == ZipEntry.DEFLATED)
		{
			ciCompressMethod = aiMethod;
		}
	}
	/**
	 * unzip - Do unzipping by the graph which has been created
	 * 	by the constructor JZip(String, Vector).
	 * 
	 * @throws IOException if I/O error occurs
	 */
	public void unzip() throws IOException
	{
		String lsInputName = null;
		FileInputStream lpfsFis = null;
		for (int i = 0; i < cvInputNames.size(); ++i)
		{
			lsInputName = (String) cvInputNames.elementAt(i);
			lpfsFis = new FileInputStream(lsInputName);
			unzip(lpfsFis);
			lpfsFis.close();
		}
	}
	/**
	 * unzip - Do not call directly ... use unzip() no arguments
	 * 
	 * @param apfsIs InputStream
	 * @throws IOException if I/O error occurs
	 */
	private void unzip(InputStream apfsIs) throws IOException
	{
		FileOutputStream lpfsFos;
		File laFile;
		ZipInputStream lpfsZis = new ZipInputStream(apfsIs);
		ZipEntry laZipEntry = null;
		byte[] larrBuffer = new byte[1024];
		int liReadCount = 0;
		String lsOutputDirectory;
		if (csOutputName.endsWith(File.separator))
		{
			lsOutputDirectory = csOutputName;
		}
		else
		{
			lsOutputDirectory = csOutputName + File.separator;
		}
		while ((laZipEntry = lpfsZis.getNextEntry()) != null)
		{
			if (laZipEntry.isDirectory())
			{
				laFile =
					new File(lsOutputDirectory + laZipEntry.getName());
				if (!laFile.exists())
				{
					laFile.mkdir();
				}
			}
			else
			{
				lpfsFos =
					new FileOutputStream(
						lsOutputDirectory + laZipEntry.getName());
				while ((liReadCount = lpfsZis.read(larrBuffer)) != -1)
				{
					lpfsFos.write(larrBuffer, 0, liReadCount);
				}
				lpfsFos.close();
			}
		}
		lpfsZis.close();
	}
	/**
	 * Do unzipping by special output path and input stream.
	 * 
	 * @param asOutputDir String
	 * @param apfsIs InputStream
	 * @throws IOException if I/O error occurs
	 */
	public void unzip(String asOutputDir, InputStream apfsIs)
		throws IOException
	{
		this.csOutputName = asOutputDir;
		unzip(apfsIs);
	}
	/**
	 * Do zipping by the graph which has been created by the
	 * constructor JZip(String, Vector).
	 * 
	 * @throws IOException if I/O error occurs
	 */
	public void zip() throws IOException
	{
		ByteArrayOutputStream lpfsByteos = new ByteArrayOutputStream();
		BufferedOutputStream lpfsBos =
			new BufferedOutputStream(lpfsByteos);
		zip(lpfsBos);
		lpfsBos.close();

		FileOutputStream lpfsFout = new FileOutputStream(csOutputName);
		byte[] larrBytes = lpfsByteos.toByteArray();
		lpfsFout.write(larrBytes);
		lpfsByteos.close();
		lpfsFout.close();
	}
	/**
	 * zip - Do not call directly ... use zip() no arguments
	 * 
	 * @param apfsOs OutputStream
	 * @throws IOException if I/O error occurs
	 */
	private void zip(OutputStream apfsOs) throws IOException
	{
		ZipOutputStream lpfsZout = new ZipOutputStream(apfsOs);
		File laFile = null;
		String lsInputName = null;
		for (int i = 0; i < cvInputNames.size(); ++i)
		{
			lsInputName = (String) cvInputNames.elementAt(i);
			laFile = new File(lsInputName);
			if (laFile.isFile())
			{
				zipFile(lpfsZout, lsInputName);
			}
			else if (laFile.isDirectory())
			{
				if (!lsInputName.endsWith(File.separator))
				{
					lsInputName = lsInputName + File.separator;
				}
				ZipEntry laZent = new ZipEntry(lsInputName);
				laZent.setMethod(ciCompressMethod);
				if (ciCompressMethod == ZipEntry.STORED)
				{
					byte[] larrBuff = new byte[lsInputName.length()];
					System.arraycopy(
						lsInputName,
						0,
						larrBuff,
						0,
						lsInputName.length());
					CRC32 laCRC = new CRC32();
					laCRC.update(larrBuff);
					laZent.setSize(lsInputName.length());
					laZent.setCrc(laCRC.getValue());
				}
				lpfsZout.putNextEntry(laZent);
				lpfsZout.closeEntry();
				zipDirectory(lpfsZout, lsInputName, laFile);
			}
		}
		lpfsZout.close();
	}
	/**
	 * Do zipping by special output stream and input files or directories
	 * 
	 * @param apfsOs OutputStream
	 * @param avInputNames Vector
	 * @throws IOException if I/O error occurs
	 */
	public void zip(OutputStream apfsOs, Vector avInputNames)
		throws IOException
	{
		cvInputNames = avInputNames;
		zip(apfsOs);
	}
	/**
	 * zipDirectory
	 * 
	 * @param apfsZos ZipOutputStream
	 * @param asDirectoryPath String
	 * @param aaDirectoryFile File
	 * @throws IOException if I/O error occurs
	 */
	private void zipDirectory(
		ZipOutputStream apfsZos,
		String asDirectoryPath,
		File aaDirectoryFile)
		throws IOException
	{
		if (aaDirectoryFile.canRead())
		{
			String[] larrFileNames = aaDirectoryFile.list();
			File laFile = null;

			if (!asDirectoryPath.endsWith(File.separator))
			{
				asDirectoryPath = asDirectoryPath + File.separator;
			}
			for (int i = 0; i < larrFileNames.length; i++)
			{
				larrFileNames[i] = asDirectoryPath + larrFileNames[i];
				laFile = new File(larrFileNames[i]);
				if (laFile.isDirectory())
				{
					if (!larrFileNames[i].endsWith(File.separator))
					{
						larrFileNames[i] =
							larrFileNames[i] + File.separator;
					}
					ZipEntry laZent = new ZipEntry(larrFileNames[i]);
					laZent.setMethod(ciCompressMethod);
					if (ciCompressMethod == ZipEntry.STORED)
					{
						byte[] larrBuff =
							new byte[larrFileNames[i].length()];
						System.arraycopy(
							larrFileNames[i],
							0,
							larrBuff,
							0,
							larrFileNames[i].length());
						CRC32 laCRC = new CRC32();
						laCRC.update(larrBuff);
						laZent.setSize(larrFileNames[i].length());
						laZent.setCrc(laCRC.getValue());
					}
					apfsZos.putNextEntry(laZent);
					apfsZos.closeEntry();
					zipDirectory(apfsZos, larrFileNames[i], laFile);
				}
				else if (laFile.canRead())
				{
					zipFile(apfsZos, larrFileNames[i]);
				}
				else
				{
					throw new IOException(
						THE_FILE
							+ larrFileNames[i]
							+ NO_READ);
				}
			}
		}
		else
		{
			throw new IOException(
				THE_DIRECTORY
					+ asDirectoryPath
					+ NO_READ);
		}
	}
	/**
	 * zipFile
	 * 
	 * @param apfsZos ZipOutputStream
	 * @param asInputFileName String
	 * @throws IOException if I/O error occurs
	 */
	private void zipFile(
		ZipOutputStream apfsZos,
		String asInputFileName)
		throws IOException
	{
		byte[] larrBuffer = new byte[1024];
		int liReadCount = 0;
		FileInputStream lpfsFin = new FileInputStream(asInputFileName);
		ZipEntry laZent = new ZipEntry(asInputFileName);
		laZent.setMethod(ciCompressMethod);
		if (ciCompressMethod == ZipEntry.STORED)
		{
			File laFile = new File(asInputFileName);
			FileInputStream lpfsF = new FileInputStream(laFile);
			byte[] larrBuff = new byte[lpfsF.available()];
			lpfsF.read(larrBuff);
			lpfsF.close();
			CRC32 laCRC = new CRC32();
			laCRC.update(larrBuff);
			laZent.setSize(laFile.length());
			laZent.setCrc(laCRC.getValue());
		}
		apfsZos.putNextEntry(laZent);
		liReadCount = lpfsFin.read(larrBuffer);
		while (liReadCount != -1)
		{
			apfsZos.write(larrBuffer, 0, liReadCount);
			liReadCount = lpfsFin.read(larrBuffer);
		}
		lpfsFin.close();
		apfsZos.closeEntry();
	}
	/**
	 * Zip uncompressed
	 * 
	 * @throws IOException if I/O error occurs
	 */
	public void zipUncompressed() throws IOException
	{
		ciCompressMethod = ZipEntry.STORED;
		zip();
	}
}
