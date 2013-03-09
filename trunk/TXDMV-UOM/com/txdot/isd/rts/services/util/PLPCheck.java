package com.txdot.isd.rts.services.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.data.ValidateInventoryPattern;
import com.txdot.isd.rts.services.exception.RTSException;

/*
 * 
 * PLPCheck.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		09/12/2005	New class
 *							defect 8396 Ver 5.2.3
 * Min Wang		01/28/2008	deprecate class
 * 							defect 9516 Ver 3_Amigos Prep
 * ---------------------------------------------------------------------
 */

/**
 * Small driver class to help check PLP for pattern match and to 
 * verify concept.
 *
 * @version	3_Amigos Prep	01/28/2008 
 * @author	Min Wang
 * <br>Creation Date:		09/21/2005 16:50:10 
 * @deprecated
 */

public class PLPCheck
{
	private static final String OUT_FILE_NAME = "pattern.txt";
	private static PrintWriter caOutFile;

	/**
	 * Method description
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		String lsFileName = "pltlist.txt";

		if (args.length > 0)
		{
			lsFileName = args[0];
		}

		// load cache for processing
		try
		{
			CacheManager.loadCache();
		}
		catch (RTSException aeRTSEx)
		{
			System.out.println("There was a problem with cache load");
			aeRTSEx.printStackTrace();
		}

		// start up the check
		PLPCheck laPLPC = new PLPCheck();
		try
		{
			// create the output file
			caOutFile =
				new java.io.PrintWriter(
					new java.io.FileOutputStream(OUT_FILE_NAME, false));
					
			Vector lvPlateList = laPLPC.getPlateList(lsFileName);
			// close the file
			caOutFile.close();
		}
		catch (Exception aeExc)
		{
			System.out.println("There was a problem");
			aeExc.printStackTrace();
		}
	}
	/**
	 * Read in the Plate List file and populate the plate list vector
	 * 
	 * @param asFileName String
	 * @return Vector
	 * @throws Exception
	 */
	private Vector getPlateList(String asFileName) throws Exception
	{
		Vector lvPlateList = new Vector();
		try
		{
			FileInputStream lFileInputStream =
				new FileInputStream(asFileName);
			BufferedReader lBufferedReader =
				new BufferedReader(
					new InputStreamReader(lFileInputStream));
			String lsBufRd;
			while ((lsBufRd = lBufferedReader.readLine()) != null)
			{
				lsBufRd = lsBufRd.trim();
				if (!lsBufRd.equals(""))
				{
					lvPlateList.add(lsBufRd);
					processPlate(lsBufRd);
				}
			}
			lBufferedReader.close();
			lFileInputStream.close();
			return lvPlateList;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Process the list of plates.
	 * 
	 * @param asPlate String
	 */
	private void processPlate(String asPlate)
	{
		try
		{
			//System.out.println("Checking " + asPlate);
			Vector lvHits =
				ValidateInventoryPattern.chkItmForPatternMatch(asPlate);
			if (lvHits.size() > 0)
			{
				System.out.println(" ");
				System.out.println(" ");
				System.out.println(
					"This plate matches at least one pattern "
						+ asPlate);

				caOutFile.println("");
				caOutFile.println("");
				caOutFile.println(
					"This plate matches at least one pattern "
						+ asPlate);
				caOutFile.flush();

				for (Iterator laHitItem = lvHits.iterator();
					laHitItem.hasNext();
					)
				{
					ItemCodesData laICD =
						(ItemCodesData) laHitItem.next();
					System.out.print(laICD.getItmCd() + " ");
					caOutFile.print(laICD.getItmCd() + " ");
					caOutFile.flush();

				}
			}
		}
		catch (Exception aeExc)
		{
			System.out.println(
				"There was a problem working an item " + asPlate);
			aeExc.printStackTrace();
		}
	}
}