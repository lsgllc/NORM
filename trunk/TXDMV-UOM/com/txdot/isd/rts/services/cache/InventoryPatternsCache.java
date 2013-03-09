package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.data.InventoryPatternsData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * InventoryPatternsCache.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 * Ray Rowehl	05/07/2007	Add method to detect the presence of an 
 * 							ISA Indicator.
 * 							add containsIsaPatternAndVIItmCd()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/27/2007	Make sure the vector is populated before
 * 							attempting to use it.
 * 							NullPtr check.  Found this in unusual 
 * 							testing from IVTRS working on PLP.
 * 							modify containsIsaPatternAndVIItmCd()
 * 							defect 9116 Ver Special Plates
 * K Harrell	08/29/2008	Add new method to return next valid year
 * 							add getNextValidYear()
 * 							defect 9522 Ver Defect_POS_B
 *----------------------------------------------------------------------
 */

/**
 * The InventoryPatternsCache class provides static methods to 
 * retrieve a list of InventoryPatternsData based on the item code 
 * input parameter.
 *
 * <p>InventoryPatternsCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version Defect_POS_B	08/29/2008
 * @author	Kathy Harrell
 * <br>Creation Date: 		08/23/2001 17:43:23
 */

public class InventoryPatternsCache
	extends GeneralCache
	implements java.io.Serializable
{

	public static int NO_YEAR = -1;

	private static Hashtable shtInvPatrns = new Hashtable();

	private final static long serialVersionUID = -7906308593456915806L;

	/**
	 * InventoryPatternsCache constructor comment.
	 */
	public InventoryPatternsCache()
	{
		super();
	}

	/**
	 * Returns a boolean indicating if the Vector of patterns has any
	 * patterns containing an ISA Indicator.
	 * 
	 * @param avPatternList Vector
	 * @return boolean
	 */
	public static boolean containsIsaPatternAndVIItmCd(Vector avPatternList)
	{
		boolean lbHasISA = false;

		// Make sure the pattern list is populated before attempting 
		// to use.
		if (avPatternList != null && avPatternList.size() > 0)
		{
			// Check the list for a pattern with an isa indi.
			for (Iterator laIter = avPatternList.iterator();
				laIter.hasNext();
				)
			{
				InventoryPatternsData laElement =
					(InventoryPatternsData) laIter.next();
				if (laElement.getISAIndi() == 1
					&& (laElement.getVIItmCd() != null
						&& laElement.getVIItmCd().trim().length() > 0))
				{
					lbHasISA = true;
					break;
				}
			}
		}

		return lbHasISA;
	}

	/**
	* Return the CacheConstant for this cache type
	* 
	* @return int 
	*/
	public int getCacheFunctionId()
	{
		return CacheConstant.INVENTORY_PATTERNS_CACHE;
	}

	/**
	 * Get the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtInvPatrns;
	}
	
	/**
	 * Return next valid year for plate if annual or 5 year   
	 *   If no record found where >= current year + 1, return maximum year
	 *
	 * @param  asItmCd String 
	 * @return int 
	 */
	public static int getNextValidYear(String asItmCd)
	{
		int liYear = Integer.MIN_VALUE;
		int liNextYear = new RTSDate().getYear() + 1;
		
		Object laObject = shtInvPatrns.get(asItmCd);

		if (laObject != null)
		{
			Vector lvInvPatrnsData = (Vector) laObject;
			
			for (int i = 0; i < lvInvPatrnsData.size(); i++)
			{
				InventoryPatternsData laInvPatData =
					(InventoryPatternsData) lvInvPatrnsData.elementAt(
						i);
						
				liYear = laInvPatData.getInvItmYr(); 
				
				if (liYear  >= liNextYear)
				{
					break;
				}
			}
		}
		return liYear;
	}

	/**
	 * Retrieve all inventory patterns given ItmCd and InvItmYr
	 *
	 * @param  asItmCd String 
	 * @param  aiInvItmYr int
	 * @return Vector
	 */
	public static Vector getInvPatrns(String asItmCd, int aiInvItmYr)
	{

		Object laObject = shtInvPatrns.get(asItmCd);

		//No data found.
		if (laObject == null)
		{
			return null;
		}

		Vector lvInvPatrnsData = (Vector) laObject;

		//Return all item codes for given Item Code.
		if (aiInvItmYr == NO_YEAR)
		{
			return lvInvPatrnsData;
		}
		else
		{
			Vector lvReturn = new Vector();

			for (int i = 0; i < lvInvPatrnsData.size(); i++)
			{
				InventoryPatternsData laData =
					(InventoryPatternsData) lvInvPatrnsData.get(i);
				if (laData.getInvItmYr() == aiInvItmYr)
				{
					lvReturn.addElement(laData);
				}
			}
			return lvReturn;
		}
	}

	/**
	 * Test main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			Vector lvData = new Vector();

			InventoryPatternsData laData1 = new InventoryPatternsData();
			laData1.setItmCd("144PT");
			laData1.setInvItmYr(0);
			laData1.setPatrnSeqCd(0);
			laData1.setInvItmPatrnCd(41);
			laData1.setInvItmNo("100R4");
			laData1.setInvItmEndNo("99999V4");
			laData1.setValidInvLtrs("RSTUV");
			lvData.addElement(laData1);

			InventoryPatternsData laData2 = new InventoryPatternsData();
			laData2.setItmCd("WS");
			laData2.setInvItmYr(2000);
			laData2.setPatrnSeqCd(0);
			laData2.setInvItmPatrnCd(4);
			laData2.setInvItmNo("1WA");
			laData2.setInvItmEndNo("99999999WA");
			laData2.setValidInvLtrs(" ");
			lvData.addElement(laData2);

			InventoryPatternsData laData3 = new InventoryPatternsData();
			laData3.setItmCd("WS");
			laData3.setInvItmYr(2001);
			laData3.setPatrnSeqCd(0);
			laData3.setInvItmPatrnCd(4);
			laData3.setInvItmNo("1WB");
			laData3.setInvItmEndNo("99999999WB");
			laData3.setValidInvLtrs(" ");
			lvData.addElement(laData3);

			InventoryPatternsData laData4 = new InventoryPatternsData();
			laData4.setItmCd("WS");
			laData4.setInvItmYr(2000);
			laData4.setPatrnSeqCd(1);
			laData4.setInvItmPatrnCd(4);
			laData4.setInvItmNo("1WO");
			laData4.setInvItmEndNo("99999999WO");
			laData4.setValidInvLtrs(" ");
			lvData.addElement(laData4);

			InventoryPatternsCache laCache =
				new InventoryPatternsCache();

			laCache.setData(lvData);

			String lsChkItmCd = "144PT";
			int liChkInvItmYr = 0;

			System.out.println(">> " + lsChkItmCd + liChkInvItmYr);

			lvData =
				InventoryPatternsCache.getInvPatrns(
					lsChkItmCd,
					liChkInvItmYr);

			for (int i = 0; i < lvData.size(); i++)
			{
				InventoryPatternsData laInvPatrnsData =
					(InventoryPatternsData) lvData.get(i);

				int liChkPatrnSeqCd = laInvPatrnsData.getPatrnSeqCd();
				int liChkInvItmPatrnCd =
					laInvPatrnsData.getInvItmPatrnCd();
				String lsChkInvItmNo = laInvPatrnsData.getInvItmNo();
				String lsChkInvItmEndNo =
					laInvPatrnsData.getInvItmEndNo();

				System.out.println(
					lsChkItmCd
						+ " "
						+ liChkInvItmYr
						+ " "
						+ liChkPatrnSeqCd
						+ " "
						+ liChkInvItmPatrnCd
						+ " "
						+ lsChkInvItmNo
						+ " "
						+ lsChkInvItmEndNo);
			}

			System.out.println("done");
		}

		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	* Populates hashtable from supplied vector.
	*
	* Routine will be called to populate hashtable from database.
	* 
	* @param avInventoryPatternsData Vector 
	*/
	public void setData(Vector avInventoryPatternsData)
	{
		//reset hash table
		shtInvPatrns.clear();

		for (int i = 0; i < avInventoryPatternsData.size(); i++)
		{
			InventoryPatternsData laData =
				(InventoryPatternsData) avInventoryPatternsData.get(i);

			String lsKey1 = laData.getItmCd();

			if (shtInvPatrns.containsKey(lsKey1))
			{
				Vector lvData = (Vector) shtInvPatrns.get(lsKey1);
				lvData.add(laData);
			}
			else
			{
				Vector lvData = new Vector();
				lvData.addElement(laData);
				shtInvPatrns.put(lsKey1, lvData);
			}
		}
	}

	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable Hashtable 
	 */
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtInvPatrns = ahtHashtable;
	}

	/**
	* Retrieve all inventory patterns  
	*
	* @return Vector
	*/
	public static Vector getAllInvPatrns()
	{
		Vector lvReturn = new Vector();

		for (Enumeration e = shtInvPatrns.elements();
			e.hasMoreElements();
			)
		{
			Vector lvInvPatrn = (Vector) e.nextElement();
			lvReturn.addAll(lvInvPatrn);
		}
		return lvReturn;
	}
}
