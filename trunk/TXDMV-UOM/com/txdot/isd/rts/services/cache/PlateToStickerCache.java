package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.PlateToStickerData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * PlateToStickerCache.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Created 
 * 							defect 8218 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The PlateToStickerCache class provides static method to 
 * retrieve a vector of PlateToStickerCache objects based upon key 
 *
 * <p>PlateToStickerCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	5.2.3 			06/19/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		06/14/2005	16:37:00
 */
public class PlateToStickerCache
	extends GeneralCache
	implements java.io.Serializable
{

	private final static long serialVersionUID = -1444501084264022134L;
	/**
	 * A hashtable of vectors 
	 */
	private static Hashtable shtPltToStkr = new Hashtable();

	/**
	 * PlateToStickerCache default constructor.
	 */

	public PlateToStickerCache()
	{
		super();
	}
	/**
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int
	 */

	public int getCacheFunctionId()
	{
		return CacheConstant.PLATE_TO_STICKER_CACHE;
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
		return shtPltToStkr;
	}
	/**
	 * Returns a vector of PlateToSticker objects whose dates are 
	 * effective for the given RegPltCd,EffectiveDate 
	 *
	 * @param  asRegPltCd  String
	 * @param  aiEffDate   int
	 * @return Vector  
	 */

	public static Vector getPltToStkrs(
		String asRegPltCd,
		int aiEffDate)
	{
		Object laObj = shtPltToStkr.get(asRegPltCd);

		if (laObj instanceof Vector)
		{
			Vector lvDataVector = (Vector) laObj;

			Vector lvReturnVector = new Vector();

			for (int i = 0; i < lvDataVector.size(); i++)
			{
				PlateToStickerData laPltToStkrData =
					(PlateToStickerData) lvDataVector.get(i);
				if ((aiEffDate >= laPltToStkrData.getRTSEffDate())
					&& (aiEffDate <= laPltToStkrData.getRTSEffEndDate()))
				{
					lvReturnVector.addElement(laPltToStkrData);
				}
			}

			if (lvReturnVector.size() == 0)
			{
				return null;
			}
			else
			{
				return lvReturnVector;
			}

		}
		else
		{
			return null;
		}
	}
	/**
	 * Returns a vector of PlateToSticker objects whose dates are 
	 * effective for the given RegPltCd, RegStkrCd,Effective Date 
	 *
	 * @param  aiRegPltCd  String
	 * @param  asRegStkrCd String
	 * @param  aiEffDate   int
	 * @return Vector  
	 */

	public static Vector getPltToStkrs(
		String asRegPltCd,
		String asRegStkrCd,
		int aiEffDate)
	{
		Object laObj = shtPltToStkr.get(asRegPltCd);

		if (laObj instanceof Vector)
		{
			Vector lvDataVector = (Vector) laObj;

			Vector lvReturn = new Vector();

			for (int i = 0; i < lvDataVector.size(); i++)
			{
				PlateToStickerData laData =
					(PlateToStickerData) lvDataVector.get(i);
				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate())
					&& (asRegStkrCd.equals(laData.getRegStkrCd())))
				{
					lvReturn.addElement(laData);
				}
			}

			if (lvReturn.size() == 0)
			{
				return null;
			}
			else
			{
				return lvReturn;
			}

		}
		else
		{
			return null;
		}
	}
	/**
	 * Main Method
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			Vector lvPltToStkrData = new Vector();

			PlateToStickerData laPltToStkrData1 =
				new PlateToStickerData();
			laPltToStkrData1.setRegPltCd("PSP");
			laPltToStkrData1.setRegStkrCd("WS");
			laPltToStkrData1.setAcctItmCd("WS");
			laPltToStkrData1.setRTSEffDate(19990101);
			laPltToStkrData1.setRTSEffEndDate(20010228);

			lvPltToStkrData.addElement(laPltToStkrData1);

			PlateToStickerData laPltToStkrData2 =
				new PlateToStickerData();
			laPltToStkrData2.setRegPltCd("PLP");
			laPltToStkrData2.setRegStkrCd("WS");
			laPltToStkrData2.setAcctItmCd("WS");
			laPltToStkrData2.setRTSEffDate(19990101);
			laPltToStkrData2.setRTSEffEndDate(99991231);

			lvPltToStkrData.addElement(laPltToStkrData2);

			PlateToStickerData laPltToStkrData3 =
				new PlateToStickerData();
			laPltToStkrData3.setRegPltCd("PSP");
			laPltToStkrData3.setRegStkrCd("WS");
			laPltToStkrData3.setAcctItmCd("WS");
			laPltToStkrData3.setRTSEffDate(20010229);
			laPltToStkrData3.setRTSEffEndDate(99991231);

			lvPltToStkrData.addElement(laPltToStkrData3);

			PlateToStickerData laPltToStkrData4 =
				new PlateToStickerData();
			laPltToStkrData4.setRegPltCd("CP");
			laPltToStkrData4.setRegStkrCd(" ");
			laPltToStkrData4.setAcctItmCd("CP");
			laPltToStkrData4.setRTSEffDate(19990101);
			laPltToStkrData4.setRTSEffEndDate(99991231);

			lvPltToStkrData.addElement(laPltToStkrData4);

			PlateToStickerCache laCache = new PlateToStickerCache();
			laCache.setData(lvPltToStkrData);

			// Query With Sticker Code
			Vector lvPltToStkr2 =
				PlateToStickerCache.getPltToStkrs(
					"PSP",
					"WS",
					19990201);

			for (int i = 0; i < lvPltToStkr2.size(); i++)
			{
				PlateToStickerData laPltToStkrData =
					(PlateToStickerData) lvPltToStkr2.elementAt(i);

				System.out.println(
					laPltToStkrData.getRegPltCd()
						+ "---"
						+ laPltToStkrData.getRegStkrCd()
						+ "---"
						+ laPltToStkrData.getAcctItmCd()
						+ "---"
						+ laPltToStkrData.getRTSEffDate());
			}

			// Query With No Sticker Code 
			lvPltToStkr2 =
				PlateToStickerCache.getPltToStkrs("CP", 19990201);

			for (int i = 0; i < lvPltToStkr2.size(); i++)
			{
				PlateToStickerData laPltToStkrData =
					(PlateToStickerData) lvPltToStkr2.elementAt(i);

				System.out.println(
					laPltToStkrData.getRegPltCd()
						+ "---"
						+ laPltToStkrData.getRegStkrCd()
						+ "---"
						+ laPltToStkrData.getRegPltCd()
						+ "---"
						+ laPltToStkrData.getAcctItmCd()
						+ "---"
						+ laPltToStkrData.getRTSEffDate());
			}

			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}
	/**
	* Clear and populate the hashtable with the vector avPltToStkr
	* 
	* @param avPltToStkr Vector
	*/
	public void setData(Vector avPltToStkr)
	{
		//reset the hashtable
		shtPltToStkr.clear();

		for (int i = 0; i < avPltToStkr.size(); i++)
		{
			PlateToStickerData laPltToStkrData =
				(PlateToStickerData) avPltToStkr.get(i);

			String lsPrimaryKey = laPltToStkrData.getRegPltCd();

			if (shtPltToStkr.containsKey(lsPrimaryKey))
			{
				Vector lvPltToStkrData =
					(Vector) shtPltToStkr.get(lsPrimaryKey);
				lvPltToStkrData.add(laPltToStkrData);
			}
			else
			{
				Vector lvPltToStkrData = new Vector();
				lvPltToStkrData.addElement(laPltToStkrData);
				shtPltToStkr.put(lsPrimaryKey, lvPltToStkrData);
			}
		}

	} //setData
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
		shtPltToStkr = ahtHashtable;

	}
}
