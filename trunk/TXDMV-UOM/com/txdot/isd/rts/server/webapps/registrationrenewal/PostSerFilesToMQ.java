package com.txdot.isd.rts.server.webapps.registrationrenewal;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.txdot.isd.rts.services.webapps.util.constants.*;
import com.txdot.isd.rts.services.data.RenewalShoppingCart;
import com.txdot.isd.rts.services.webapps.communication.Comm;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;

/*
 *
 * RenewalSerializer.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B. Brown		02/09/2009	Initial write. This class from cloned from
 * 							txdotrts.registrationrenewal.
 * 							RenewalSerializer
 *							defect XXXX Ver Defect_POS_D
 * ---------------------------------------------------------------------
 */

/**
 * Serialize and deserialize the Registration Renewal shopping cart.
 * When the posting into MQ fails, the cart is serialized 
 * into a file and at a later time deserialize them and post
 * into MQ when it is up.
 * 
 * This class was most recently used on the POS side to recover from
 * MQ being not operations after a TxO SUnday maintenance window.
 * Bearingpoint personnel got the serialized renewal files from the
 * /usr/local/bea/domains/BP_Txdot directory on our 2 RTS/TxO app 
 * servers (S1 and S2) and put them on the 204.66.40.185 machine in a 
 * directory called /serfiles.
 * The file were then put on a T drive directory (T:\RTS II Team\
 * POS Team Notes\IVTRS\serfiles), and that directory
 * passed to this classes main method via running the main method as an 
 * application out of WSAD, and pointing WSAD to P0RTSDB. The main 
 * method would then process all the ser fiels and apply them to our POS
 * database.   
 * After updating, the files are supposed to be renamed to "updated" as 
 * the first part of the file name, but not all files were renamed.
 * We will ask BP to delete all these ser files after they have been 
 * processed. 
 *
 * @version	Ver 5.10.1		02/09/2009
 * @author	Bob Brown
 * <br>Creation Date:		10/16/2001 16:46:37 
 */
public class PostSerFilesToMQ
{
	private static String ssUpdatedPrefix = "updated_";
	private static String ssFilenameSuffix = ".ser";
	private static File ssSerFilesDir =
		new File("T:\\RTS II Team\\" +
			"POS Team Notes\\IVTRS\\serfiles\\");
		
	/*
	 * Correct the objects in the shopping cart on the fly if there 
	 * is any error in the objects and save the cart if necessary.
	 * The content of the method is unkown before hand.
	 */
	//private void correctAndSave(RenewalShoppingCart aCart)
	//{
		// CompleteRegRenData lRegData=(CompleteRegRenData)
		// aRenewalShoppingCart.elementAt(0);
		// lRegData.getVehInsuranceData().setPolicyEndDt("20030102");		
		// write("D:\\rts\\newobj.ser", aRenewalShoppingCart);
	//}
	
	/**
	 * This method renames the serialized file
	 *
	 * @param aaRenewalResult Object
	 * @param aaPath String
	 * @param aaFileName String
	 */
	
	private void handleUpdateResult(
		Object aaRenewalResult,
		String aaPath,
		String aaFileName)
	{
		if (aaRenewalResult instanceof Boolean)
		{
			boolean lbUpdateSuccessful =
				((Boolean) aaRenewalResult).booleanValue();
				
			if (lbUpdateSuccessful)
			{
				File laFile = new File
								(aaPath + File.separator + aaFileName);
				File laToFile =
					new File(
						aaPath
							+ File.separator
							+ ssUpdatedPrefix
							+ aaFileName);
				laFile.renameTo(laToFile);
				stdOut(
					"PostSerFilesToMQ, Update of "
						+ laFile
						+ " was successful");
			}
			else
				stdOut(
					"PostSerFilesToMQ, Update of "
						+ aaPath
						+ File.separator
						+ aaFileName
						+ " was unsuccessful");

		}
		else
		{
			stdOut("PostSerFilesToMQ, error occurred, returned " +				   "object not boolean.");
		}
	}
	
	/**
	 * This is the main method 
	 *
	 * @param aarrArgs String[]
	 */
	
	public static void main(String[] aarrArgs)
	{
		if (aarrArgs.length != 1)
		{
			System.out.println(
				"Usage: java PostSerFilesToMQ PathToUpdate");
		}
		else
		{
			new PostSerFilesToMQ().
				updateSerializedRenewal(aarrArgs[0]);
		}
	}
	
	/**
	 * This method reads the serialized file
	 *
	 * @param asFileName String
	 * @return Object
	 */
	
	public Object read(String asFileName)
	{
		Object laObj = null;
		try
		{
			FileInputStream laFIS = new FileInputStream(asFileName);
			ObjectInputStream laOIS = new ObjectInputStream(laFIS);
			laObj = laOIS.readObject();
		}
		catch (IOException leIOE)
		{
			leIOE.printStackTrace();
		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
		}
		return laObj;
	}
	
	/**
	 * This method writes the parameter
	 *
	 * @param asMessage String
	 */
	
	private static void stdOut(String asMessage)
	{
		System.out.println
			("[" + new java.util.Date() + "] " + asMessage);
	}
	
	/**
	 * This method obtains the renewla result for serialization
	 *
	 * @param aaRenewalShoppingCart RenewalShoppingCart
	 * @param asPath String
	 * @param asFile String
	 */
	
	private void updateRenewal(
		RenewalShoppingCart aaRenewalShoppingCart,
		String asPath,
		String asFile)
	{
		try
		{
			Object laRenewalResult =
				Comm.sendToServer(
					CommonConstants.REGISTRATION_RENEWAL,
					RegistrationRenewalConstants.DO_REG_RENEWAL,
					aaRenewalShoppingCart);

			handleUpdateResult(laRenewalResult, asPath, asFile);
		}
		catch (Throwable leT)
		{
			stdOut(
				"Error occurred while updating "
					+ asPath
					+ File.separator
					+ asFile);
		}
	}

	/**
	 * Update the renewals of those objects stored in 
	 * the files which are in the specified path. 
	 * 
	 * @param asPath String
	 */
	
	private void updateSerializedRenewal(String asPath)
	{
		File laFile = new File(asPath);
		if (!laFile.exists() || !laFile.isDirectory())
		{
			stdOut("Error, no such path (directory) exists");
			return;
		}

		File[] larrFiles = laFile.listFiles(new FilenameFilter()
		{
			public boolean accept(File aaDir, String asName)
			{
				boolean lbAccept = true;
				try
				{
					File laFile =
						new File(
							aaDir.getCanonicalPath()
								+ File.separator
								+ asName);
								
					if (!laFile.isFile())
					{
						lbAccept = false;
					}
				}
				catch (IOException leIOE)
				{
					lbAccept = false;
				}
				
				if (!asName.endsWith(ssFilenameSuffix)
					|| asName.startsWith(ssUpdatedPrefix))
					{
						lbAccept = false;
					}
					
				return lbAccept;
			}
		});

		for (int liX = 0; liX < larrFiles.length; ++liX)
		{
			try
			{
				Object laRenewal = 
					read(larrFiles[liX].getCanonicalPath());
				if (laRenewal instanceof RenewalShoppingCart)
				{
					try
					{
						updateRenewal(
							(RenewalShoppingCart) laRenewal,
							asPath,
							larrFiles[liX].getName());
					}
					catch (Exception leEx)
					{
						leEx.printStackTrace();
					}
				}
			}
			catch (IOException leIOE)
			{
				leIOE.printStackTrace();
			}
		}
	}
	
	/**
	 * This method writes the serialized file
	 *
	 * @param aaObj Object
	 * @param asFileName String
	 * @throws Throwable
	 */
	
	public void write(String asFileName, Object aaObj) throws Throwable
	{
		try
		{
			FileOutputStream laFOS = new FileOutputStream(asFileName);
			ObjectOutputStream laOOS = new ObjectOutputStream(laFOS);
			laOOS.writeObject(aaObj);
			laOOS.close();
			laFOS.close();
		}
		catch (IOException leIOE)
		{
			leIOE.printStackTrace();
			throw leIOE;
		}
		catch (Throwable leT)
		{
			leT.printStackTrace();
			throw leT;
		}
	}
}
