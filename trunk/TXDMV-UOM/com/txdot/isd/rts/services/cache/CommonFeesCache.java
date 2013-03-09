package com.txdot.isd.rts.services.cache;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.CommonFeesData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * CommonFeesCache.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C Walker		08/28/2001	Add comments
 * C Walker		09/05/2001	Add hungarian notation
 * K Harrell	06/19/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 * K Harrell	10/07/2006 	Add methods for ease of use	
 * 							add isStandardExempt(), isSeasonalAg
 * 							defect 8900 Ver 5.3.0
 * B Hargrove	05/14/2007 	After further review, decided what we really
 * 							want to check for is 'Reg period lenghth = 0'
 * 							(ie: undefined reg period) rather than
 * 							'FeeCalcCat = 4' (ie: no regis fees).	
 * 							modify isStandardExempt()
 * 							defect 9126 Ver Special Plates
 * B Hargrove	11/29/2007 	Add method to determine if Proof of Insurance
 *  						is required.	
 * 							add isInsuranceRequired()
 * 							defect 9469 Ver FRVP
 *----------------------------------------------------------------------
 */

/**
 * The CommonFeesCache class provides static methods to 
 * retrieve a particular CommonFeesData object based 
 * on different input parameters.
 *
 * <p>CommonFeesCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	FRVP			 11/28/2007  
 * @author	Charlie Walker
 * <br>Creation Date: 
 */
public class CommonFeesCache
	extends GeneralCache
	implements Serializable
{
	/**
	* A hashtable of vectors with regClassCd as key
	*/
	private static Hashtable shtCommonFees = new Hashtable();

	private final static long serialVersionUID = -4829174135156909105L;

	/**
	 * CommonFeesCache constructor comment.
	 */
	public CommonFeesCache()
	{
		super();
	}
	/**
	 * Return the CacheConstant for this cache type.
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.COMMON_FEES_CACHE;
	}

	/**
	 * Returns the CommonFeesData object based on the input
	 * aiRegClassCd and aiEffDate.
	 *
	 * @param aiRegClassCd int
	 * @param aiEffDate int
	 * @return CommonFeesData 
	 */

	public static CommonFeesData getCommonFee(
		int aiRegClassCd,
		int aiEffDate)
	{
		CommonFeesData laCommonFeesDataReturn = null;
		if (shtCommonFees.containsKey(new Integer(aiRegClassCd)))
		{
			Vector lvCommonFeesVector =
				(Vector) shtCommonFees.get(new Integer(aiRegClassCd));

			for (int i = 0; i < lvCommonFeesVector.size(); i++)
			{
				CommonFeesData laCommonFeesData =
					(CommonFeesData) lvCommonFeesVector.get(i);

				if ((aiEffDate >= laCommonFeesData.getRTSEffDate())
					&& (aiEffDate <= laCommonFeesData.getRTSEffEndDate()))
				{
					laCommonFeesDataReturn = laCommonFeesData;
					break;
				}
			}
		}
		return laCommonFeesDataReturn;
	}
	/**
	 * Returns the RegClassCdDesc based on the input
	 * aiRegClassCd and aiEffDate.
	 *
	 * @param aiRegClassCd int
	 * @param aiEffDate int
	 * @return CommonFeesData 
	 */
	public static String getRegClassCdDesc(
		int aiRegClassCd,
		int aiEffDate)
	{
		String lsRegClassCdDesc = new String();
		
		CommonFeesData laCommonFeesData = 
			getCommonFee(aiRegClassCd, aiEffDate);
		
		if (laCommonFeesData != null)
		{
			lsRegClassCdDesc = laCommonFeesData.getRegClassCdDesc();
		}
		return lsRegClassCdDesc;
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
		return shtCommonFees;
	}

	/**
	 * Test main.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			Vector lvCommonFeesData = new Vector();

			CommonFeesData laCommonFeesData1 = new CommonFeesData();
			laCommonFeesData1.setRegClassCd(1000);
			laCommonFeesData1.setRTSEffDate(20010101);
			laCommonFeesData1.setRTSEffEndDate(200101030);
			laCommonFeesData1.setRegClassCdDesc("1000 desc");
			laCommonFeesData1.setFeeCalcCat(1);
			lvCommonFeesData.addElement(laCommonFeesData1);

			CommonFeesData laCommonFeesData2 = new CommonFeesData();
			laCommonFeesData2.setRegClassCd(2000);
			laCommonFeesData2.setRTSEffDate(20010202);
			laCommonFeesData2.setRTSEffEndDate(200102030);
			laCommonFeesData2.setRegClassCdDesc("2000 desc");
			laCommonFeesData2.setFeeCalcCat(2);
			lvCommonFeesData.addElement(laCommonFeesData2);

			CommonFeesData laCommonFeesData3 = new CommonFeesData();
			laCommonFeesData3.setRegClassCd(3000);
			laCommonFeesData3.setRTSEffDate(20010301);
			laCommonFeesData3.setRTSEffEndDate(200103030);
			laCommonFeesData3.setRegClassCdDesc("3000 desc");
			laCommonFeesData3.setFeeCalcCat(3);
			lvCommonFeesData.addElement(laCommonFeesData3);

			CommonFeesData laCommonFeesData4 = new CommonFeesData();
			laCommonFeesData4.setRegClassCd(4000);
			laCommonFeesData4.setRTSEffDate(20010401);
			laCommonFeesData4.setRTSEffEndDate(200104030);
			laCommonFeesData4.setRegClassCdDesc("4000 desc");
			laCommonFeesData4.setFeeCalcCat(4);
			lvCommonFeesData.addElement(laCommonFeesData4);

			CommonFeesCache laCommonFeesCache = new CommonFeesCache();
			laCommonFeesCache.setData(lvCommonFeesData);

			CommonFeesData laCommonFeesData =
				CommonFeesCache.getCommonFee(2000, 20010228);
			System.out.println(laCommonFeesData.getRegClassCd());
			System.out.println(laCommonFeesData.getRTSEffDate());
			System.out.println(laCommonFeesData.getRTSEffEndDate());
			System.out.println(laCommonFeesData.getRegClassCdDesc());
			System.out.println(laCommonFeesData.getFeeCalcCat());
			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}
	
  	/**
	 * Determines if Proof of Insurance is required for RegClassCd 
	 *
	 * @param aiRegClassCd int
	 * @return boolean 
	 */

	public static boolean isInsuranceRequired(int aiRegClassCd)
	{
		boolean lbInsuranceRequired = false;

		CommonFeesData laCommonFeesData =
			getCommonFee(aiRegClassCd, new RTSDate().getYYYYMMDDDate());

		if (laCommonFeesData != null
			&& laCommonFeesData.getInsProofReqrdIndi() == 1)
		{
			lbInsuranceRequired = true;
		}
		return lbInsuranceRequired;
	}

 	/**
	 * Determines if RegClassCd is SeasonalAg
	 *
	 * @param aiRegClassCd int
	 * @return boolean 
	 */

	public static boolean isSeasonalAg(int aiRegClassCd)
	{
		boolean lbSeasonalAg = false;

		CommonFeesData laCommonFeesData =
			getCommonFee(aiRegClassCd, new RTSDate().getYYYYMMDDDate());

		if (laCommonFeesData != null
			&& laCommonFeesData.getRegPeriodLngth() == 1)
		{
			lbSeasonalAg = true;
		}
		return lbSeasonalAg;
	}
	/**
	 * Determines if RegClassCd is Standard Exempt
	 *
	 * @param aiRegClassCd int
	 * @return boolean 
	 */

	public static boolean isStandardExempt(int aiRegClassCd)
	{
		boolean lbExempt = false;

		CommonFeesData laCommonFeesData =
			getCommonFee(aiRegClassCd, new RTSDate().getYYYYMMDDDate());

		// defect 9126
		// check for RegPeriodLngth = 0 (undefined regis period) rather
		// than FeeCalcCat = 4 (no regis fees)			
		//if (laCommonFeesData != null
		//	&& laCommonFeesData.getFeeCalcCat() == 4)
		if (laCommonFeesData != null
			&& laCommonFeesData.getRegPeriodLngth() == 0)
		{
			lbExempt = true;
		}
		// end defect 9126
		
		return lbExempt;
	}
	/**
	 * Clear and populate the hashtable with the vector avCommonFeesData
	 * 
	 * @param avCommonFeesDataVector java.util.Vector
	 */
	public void setData(Vector avCommonFeesData)
	{
		//reset the hashtable
		shtCommonFees.clear();
		for (int i = 0; i < avCommonFeesData.size(); i++)
		{
			CommonFeesData laCommonFeesData =
				(CommonFeesData) avCommonFeesData.get(i);
			int liPrimaryKey = laCommonFeesData.getRegClassCd();
			if (shtCommonFees.containsKey(new Integer(liPrimaryKey)))
			{
				Vector lvCommonFeesData =
					(Vector) shtCommonFees.get(
						new Integer(liPrimaryKey));
				lvCommonFeesData.add(laCommonFeesData);
			}
			else
			{
				Vector lvCommonFeesDataVector = new Vector();
				lvCommonFeesDataVector.addElement(laCommonFeesData);
				shtCommonFees.put(
					new Integer(liPrimaryKey),
					lvCommonFeesDataVector);
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
		shtCommonFees = ahtHashtable;
	}
}
