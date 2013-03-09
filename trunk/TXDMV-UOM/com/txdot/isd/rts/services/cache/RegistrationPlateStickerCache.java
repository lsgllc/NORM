package com.txdot.isd.rts.services.cache;

import java.util.Vector;

import com.txdot.isd.rts.services.data.PlateToStickerData;

/*
 * RegistrationPlateStickerCache.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Duggirala	08/24/2001	added setData()
 * R Duggirala	08/29/2001	Add comments
 * N Ting		05/31/2002	defect 4132 
 * K Harrell	06/12/2005	Redefined usage to access new cache objects 
 * 							ClassToPlateCache,PlateToStickerCache
 * 							deleted getCachefunctionId(),getHashtable(),
 * 							setData(),setHashtable(),main()
 * 							modify getPltStkrs(int,String,int),
 * 								getPltStkrs(int,String,int,String)
 * 							defect 8218 Ver 5.2.3
 * K Harrell	06/20/2005	Added new main() to test
 * 							add'l Java 1.4 Work   
 * 							defect 8218/7899 Ver 5.2.3  
 *----------------------------------------------------------------------
 */

/**
 * The RegistrationPlateStickerCache class provides access to  
 * ClassToPlateCache, PlateToStickerCache 
 * 
 * @version 5.2.3		06/20/2005
 * @author  K Harrell 
 * <br>Creation Date: 	02/24/2001
 */

public class RegistrationPlateStickerCache
{

	/**
	* RegistrationPlateStickerCache constructor comment.
	*/

	public RegistrationPlateStickerCache()
	{
		super();
	}

	/**
	 * Returns a vector of PlateToStickerData objects whose dates are 
	 * effective for given parms.
	 *
	 * @param  aiRegClassCd int
	 * @param  asRegPlateCd String
	 * @param  aiEffDate    int
	 * @return Vector
	 */

	public static Vector getPltStkrs(
		int aiRegClassCd,
		String asRegPltCd,
		int aiEffDate)
	{
		Vector lvReturn =
			ClassToPlateCache.getClassToPlate(
				aiRegClassCd,
				asRegPltCd,
				aiEffDate);

		if (lvReturn != null)
		{
			lvReturn =
				PlateToStickerCache.getPltToStkrs(
					asRegPltCd,
					aiEffDate);
		}
		return lvReturn;

	}
	/**
	 * Returns a vector of PlateToStickerData objects whose dates are 
	 * effective for given parms.
	 *
	 * @param  aiRegClassCd int
	 * @param  asRegPltCd	String
	 * @param  aiEffDate    int
	 * @param  asRegStkrCd  String 
	 * @return Vector
	 */

	public static Vector getPltStkrs(
		int aiRegClassCd,
		String asRegPltCd,
		int aiEffDate,
		String asRegStkrCd)
	{
		Vector lvReturn =
			ClassToPlateCache.getClassToPlate(
				aiRegClassCd,
				asRegPltCd,
				aiEffDate);

		if (lvReturn != null)
		{
			lvReturn =
				PlateToStickerCache.getPltToStkrs(
					asRegPltCd,
					asRegStkrCd,
					aiEffDate);
		}
		return lvReturn;
	}
	/**
	 * Test calls from RegistrationPlateStickerCache to 
	 *   ClassToPlateCache and PlateToStickerCache
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{

		try
		{
			CacheManager.loadCache();

			Vector lvPltToStkrData =
				RegistrationPlateStickerCache.getPltStkrs(
					25,
					"PSP",
					20040101);

			for (int i = 0; i < lvPltToStkrData.size(); i++)
			{
				PlateToStickerData laPltToStkrData =
					(PlateToStickerData) lvPltToStkrData.elementAt(i);
				System.out.println(
					laPltToStkrData.getRegPltCd()
						+ "---"
						+ laPltToStkrData.getRTSEffDate()
						+ "----"
						+ laPltToStkrData.getRegStkrCd());
			}

			lvPltToStkrData =
				RegistrationPlateStickerCache.getPltStkrs(
					25,
					"PSP",
					20040101,
					"US");

			for (int i = 0; i < lvPltToStkrData.size(); i++)
			{
				PlateToStickerData laPltToStkrData =
					(PlateToStickerData) lvPltToStkrData.elementAt(i);
				System.out.println(
					laPltToStkrData.getRegPltCd()
						+ "---"
						+ laPltToStkrData.getRTSEffDate()
						+ "----"
						+ laPltToStkrData.getRegStkrCd());
			}

			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}

}
