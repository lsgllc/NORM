package com.txdot.isd.rts.server.webapps.common.business;

import java.io.*;

/*
 * SerializeToDisk.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * Serialize To Disk
 *  
 * @version	5.2.3		05/04/2005
 * @author	Administrator
 * <br>Creation Date:	10/17/01 16:04:02
 */
public class SerializeToDisk
{
	/**
	 * SerializeToDisk constructor comment.
	 */
	public SerializeToDisk()
	{
		super();
	}
	/**
	 * Read File
	 *  
	 * @return Object
	 * @param String asFileName
	 */
	public static Object read(String asFileName)
	{
		try
		{
			FileInputStream laFIS = new FileInputStream(asFileName);
			ObjectInputStream laIn = new ObjectInputStream(laFIS);
			Object laObj = laIn.readObject();
			laIn.close();
			laFIS.close();
			return laObj;
		}
		catch (Exception leRTSEx)
		{
			return null;
		}
	}

	/**
	 * Write to File
	 *  
	 * @return boolean
	 * @param String asFileName
	 * @param Object aaObj
	 */
	public static boolean write(String asFileName, Object aaObj)
	{

		try
		{
			FileOutputStream laFOS = new FileOutputStream(asFileName);
			ObjectOutputStream laOut = new ObjectOutputStream(laFOS);
			laOut.writeObject(aaObj);
			laOut.close();
			laFOS.close();
			return true;
		}
		catch (Exception leRTSEx)
		{
			return false;
		}

	}
}
