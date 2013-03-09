package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 *
 * ItemCodesCache.java
 *
 * (c) Texas Department of Transportation 2001
 *                        
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							modify getItmCds(int,int,String)
 * 							add getItmCds(int,int,String,boolean)
 *		 					Ver 5.2.0
 * K Harrell	06/20/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3
 * Min Wang		09/10/2005 	add getItmCdsForSpcPltChk()
 * 							defect 8396 Ver 5.2.3	
 * Min Wang		04/03/2007	get all inventory item codes.
 * 							add getAllInvItemCds()
 * 							defect 9117 Ver Special Plates
 * K Harrell	04/10/2010	sorted methods 
 * 							add getItmCdDesc()
 * 							defect 10441 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * The ItemCodesCache class provides static methods to 
 * retrieve a list of ItemCodesData based on the item code 
 * input parameter.
 *
 * <p>ItemCodesCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	POS_640 		04/10/2010
 * @author	Kathy Harrell
 * <br>Creation Date: 
 */
public class ItemCodesCache
	extends GeneralCache
	implements java.io.Serializable
{
	private static Hashtable shtItmCds = new Hashtable();

	// Constant = 0; When aiFlag = PROCSNGCD, retrieve by invProcsngCd 
	public static final int PROCSNGCD = 0;

	// Constant = 1; When aiFlag = TRCKNGTYPE, retrieve by itmTrckngType 
	public static final int TRCKNGTYPE = 1;

	// Constant = 2; When aiFlag = BOTH, retrieve by itmTrckngType and 
	// invProcsngCd  
	public static final int BOTH = 2;

	public static final char PLATE_STICKER = 'R';
	public static final char WINDSHIELD_STICKER = 'L';

	private final static long serialVersionUID = -5744658341011265137L;

	/**
	 * ItemCodesCache constructor comment.
	 */
	public ItemCodesCache()
	{
		super();
	}

	/**
	 * Retrieve all inventory items  
	 *
	 * @return Vector
	 */
	public static Vector getAllInvItemCds() throws RTSException
	{
		Vector lvReturn = new Vector();

		for (Enumeration e = shtItmCds.elements();
			e.hasMoreElements();
			)
		{
			ItemCodesData laData = (ItemCodesData) e.nextElement();
			lvReturn.add(laData);
		}
		return lvReturn;
	}

	/**
	 * Returns an ItemCodesData object based upon input parameter asItmCd.
	 *
	 * @param  asItmCd  String 
	 * @return ItemCodesData 
	 */
	public static ItemCodesData getItmCd(String asItmCd)
	{
		Object laObject = shtItmCds.get(asItmCd);
		if (laObject != null)
		{
			return (ItemCodesData) laObject;
		}
		else
		{
			return null;
		}
	}

	/** 
	 * Return Item Code Description for given Item Code 
	 * 
	 * @param asItmCd
	 * @return String 
	 */
	public static String getItmCdDesc(String asItmCd)
	{
		String lsDesc = new String();
		ItemCodesData laData = getItmCd(asItmCd);
		if (laData != null)
		{
			lsDesc = laData.getItmCdDesc();
		}
		return lsDesc;
	}

	/**
	* Returns a vector of item codes based upon the input parameters. 
	*
	* If aiFlag = 0,retrieve based upon invProcsngCd; 
	* If aiFlag = 1,retrieve based upon itmTrckngType
	*
	* @param aiFlag int        
	* @param aiInvProcsngCd  int
	* @param asItmTrckngType String
	* @return Vector 
	*/
	public static Vector getItmCds(
		int aiFlag,
		int aiInvProcsngCd,
		String asItmTrckngType)
		throws RTSException
	{
		return getItmCds(
			aiFlag,
			aiInvProcsngCd,
			asItmTrckngType,
			false);
	}

	/**
	 * Return item codes given parameters
	 * 
	 * @param aiFlag int
	 * @param aiInvProcsngCd int
	 * @param asItmTrckngType String
	 * @param abDisregardStickers boolean
	 * @return Vector 
	 * @throws RTSException
	 */
	public static Vector getItmCds(
		int aiFlag,
		int aiInvProcsngCd,
		String asItmTrckngType,
		boolean abDisregardPrintable)
		throws RTSException
	{
		Vector lvItmCds = new Vector(shtItmCds.values());
		Vector lvReturn = new Vector();

		if (aiFlag == PROCSNGCD)
		{
			for (int i = 0; i < lvItmCds.size(); i++)
			{
				ItemCodesData laData = (ItemCodesData) lvItmCds.get(i);
				if (laData.getInvProcsngCd() == aiInvProcsngCd)
				{
					if (abDisregardPrintable && laData.isPrintable())
						continue;
					lvReturn.add(laData);
				}
			}
			return lvReturn;
		}
		else if (aiFlag == TRCKNGTYPE)
		{
			for (int i = 0; i < lvItmCds.size(); i++)
			{
				ItemCodesData laData = (ItemCodesData) lvItmCds.get(i);
				if (laData.getItmTrckngType().equals(asItmTrckngType))
				{
					if (abDisregardPrintable && laData.isPrintable())
						continue;
					lvReturn.add(laData);
				}
			}
			UtilityMethods.sort(lvReturn);
			return lvReturn;
		}
		else if (aiFlag == BOTH)
		{
			for (int i = 0; i < lvItmCds.size(); i++)
			{
				ItemCodesData laData = (ItemCodesData) lvItmCds.get(i);
				if (laData.getItmTrckngType().equals(asItmTrckngType)
					&& (laData.getInvProcsngCd() == aiInvProcsngCd))
				{
					if (abDisregardPrintable && laData.isPrintable())
					{
						continue;
					}
					lvReturn.add(laData);
				}
			}
			UtilityMethods.sort(lvReturn);
			return lvReturn;
		}
		else
		{
			throw new RTSException(
				RTSException.JAVA_ERROR,
				new Exception("Illegal Flag"));
		}
	}

	/**
	 * Return all Item Codes.
	 * 
	 * @return Vector
	 */
	public static Vector getItmCdsForSpcPltChk()
	{
		Vector lvReturnVector = new Vector();
		Vector lvProspectItems = new Vector(shtItmCds.values());
		for (Iterator laCounter = lvProspectItems.iterator();
			laCounter.hasNext();
			)
		{
			ItemCodesData laItemCode = (ItemCodesData) laCounter.next();
			if (laItemCode.getPrintableIndi() == 0
				&& ((laItemCode.getItmCd().length() > 2
					&& !laItemCode.getItmCd().substring(
						0,
						3).equalsIgnoreCase(
						"PLP")
					&& !laItemCode.getItmCd().substring(
						0,
						3).equalsIgnoreCase(
						"ROP")
					&& !laItemCode.getItmCd().substring(
						0,
						3).equalsIgnoreCase(
						"OLD"))
					|| (laItemCode.getItmCd().length() < 3)))
			{
				lvReturnVector.add(laItemCode);
			}
		}
		return lvReturnVector;
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
			Vector lvItmCdsData = new Vector();
			ItemCodesData liItmCdsData1 = new ItemCodesData();
			liItmCdsData1.setItmCd("144PT");
			liItmCdsData1.setItmCdDesc("144PT DESC");
			liItmCdsData1.setItmTrckngType(" ");
			liItmCdsData1.setInvProcsngCd(1);
			lvItmCdsData.addElement(liItmCdsData1);

			ItemCodesData liItmCdsData2 = new ItemCodesData();
			liItmCdsData2.setItmCd("WS");
			liItmCdsData2.setItmCdDesc("WS DESC");
			liItmCdsData2.setItmTrckngType("S");
			liItmCdsData2.setInvProcsngCd(1);
			lvItmCdsData.addElement(liItmCdsData2);

			ItemCodesData liItmCdsData3 = new ItemCodesData();
			liItmCdsData3.setItmCd("CMP");
			liItmCdsData3.setItmCdDesc("CMOH PLT");
			liItmCdsData3.setItmTrckngType("P");
			liItmCdsData3.setInvProcsngCd(3);
			lvItmCdsData.addElement(liItmCdsData3);

			ItemCodesData liItmCdsData4 = new ItemCodesData();
			liItmCdsData4.setItmCd("US");
			liItmCdsData4.setItmCdDesc("US DESC");
			liItmCdsData4.setItmTrckngType("S");
			liItmCdsData4.setInvProcsngCd(1);
			lvItmCdsData.addElement(liItmCdsData4);

			ItemCodesCache laItmCdsCache = new ItemCodesCache();
			laItmCdsCache.setData(lvItmCdsData);

			System.out.println(">>GET 144PT");
			ItemCodesData laItmCdsData =
				ItemCodesCache.getItmCd("144PT");
			String lsChkDesc = laItmCdsData.getItmCdDesc();
			System.out.println(lsChkDesc);

			System.out.println(">>GET WS");
			laItmCdsData = ItemCodesCache.getItmCd("WS");
			lsChkDesc = laItmCdsData.getItmCdDesc();
			System.out.println(lsChkDesc);

			int iInvProcsngCd = 3;
			System.out.println(">>GET INVPROCSNGCD = " + iInvProcsngCd);
			Vector lvData =
				ItemCodesCache.getItmCds(PROCSNGCD, iInvProcsngCd, " ");
			for (int i = 0; i < lvData.size(); i++)
			{
				laItmCdsData = (ItemCodesData) lvData.get(i);
				lsChkDesc = laItmCdsData.getItmCdDesc();
				System.out.println(lsChkDesc);
			}

			String sItmTrckngType = "X";
			System.out.println(">>GET TRCKNGTYPE = " + sItmTrckngType);
			lvData = ItemCodesCache.getItmCds(1, 0, sItmTrckngType);
			if (lvData.size() == 0)
			{
				System.out.println("Found Nothing");
			}
			for (int i = 0; i < lvData.size(); i++)
			{
				laItmCdsData = (ItemCodesData) lvData.get(i);
				lsChkDesc = laItmCdsData.getItmCdDesc();
				System.out.println(lsChkDesc);
			}
			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	* Return the CacheConstant for this cache type
	* 
	* @return int 
	*/
	public int getCacheFunctionId()
	{
		return CacheConstant.ITEM_CODES_CACHE;
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
		return shtItmCds;
	}

	/**
	 * Populates hashtable from supplied vector.
	 *
	 * Routine will be called to populate hashtable from database.
	 * 
	 * @param avItemCodesData Vector 
	 */
	public void setData(Vector avItemCodesData)
	{
		//reset the hashtable
		shtItmCds.clear();

		for (int i = 0; i < avItemCodesData.size(); i++)
		{
			ItemCodesData laData =
				(ItemCodesData) avItemCodesData.get(i);
			shtItmCds.put(new String(laData.getItmCd()), laData);
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
		shtItmCds = ahtHashtable;
	}
}
