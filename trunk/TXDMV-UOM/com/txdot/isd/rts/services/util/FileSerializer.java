/**
 * 
 */
package com.txdot.isd.rts.services.util;

import java.io.*;

/*
 * FileSerializer.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * B Woodson	10/14/2011	defect 11052 - VTR-275 Form Project @ POS
 * ---------------------------------------------------------------------
 */

/** 
 * Useful for passing any type of file from server to client (or vice versa)
 * as a byte[] when added to a serializable collection such as a Vector
 * 
 * @version	6.9.0 		10/14/2011 
 * @author	BWOODS-C 
 * @Creation Date:		10/14/2011  
 */
public class FileSerializer
{

	/**
	 * Useful for testing
	 * @param args
	 */
	public static void main(String[] args)
	{
		FileSerializer laFileSerializer = new FileSerializer();
		
		try
		{
			byte[] arrBytes = laFileSerializer.serialize("D://backup/11052 - VTR-275 Form Project @ POS//Certified Sample.pdf");
			
			laFileSerializer.deserialize("D://backup//11052 - VTR-275 Form Project @ POS//test.pdf", arrBytes);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Deserializes byte[] into file
	 * @param asFileName
	 * @param aarrBytes
	 * @throws IOException
	 */
	public void deserialize (String asFileName, byte[] aarrBytes) throws IOException
	{
		File laFile = new File(asFileName);
		FileOutputStream aaFileOutputStream = null;
		
		try
		{
			aaFileOutputStream = new FileOutputStream(laFile);			
			aaFileOutputStream.write(aarrBytes);
		}
		finally
		{
			if (aaFileOutputStream != null)
			{
				try
				{
					aaFileOutputStream.close();
				}
				catch (IOException e)
				{
					// ignore
				} 
				aaFileOutputStream = null;
			}
		}
	}
	
	/**
	 * Serializes file into byte[]
	 * @param asFileName
	 * @return
	 * @throws IOException
	 */
	public byte[] serialize (String asFileName) throws IOException 
	{
		File laFile = new File(asFileName);
		int iFileLength = (int) laFile.length();
		FileInputStream aaFileInputStream = null;
		byte[] arrBytes = null; //can be tested with isArray instead of instanceof
		
		try
		{
			aaFileInputStream = new FileInputStream(laFile);
			arrBytes = new byte[iFileLength];
			
			//fill the array
			aaFileInputStream.read(arrBytes);
		}
		finally
		{
			if (aaFileInputStream != null)
			{
				try
				{
					aaFileInputStream.close();
				}
				catch (IOException e)
				{
					// ignore
				}
				aaFileInputStream = null;
			}
		}
		
		return arrBytes;
	}

}
