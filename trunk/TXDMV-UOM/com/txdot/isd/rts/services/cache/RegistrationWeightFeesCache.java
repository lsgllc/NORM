package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.RegistrationWeightFeesData;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * RegistrationWeightFeesCache.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Tulsiani	08/28/2001	Added Javadoc comments
 * J Kwik		04/09/2002	Default tire type to "P"
 *							if empty tire type argument for getRegWtFee()
 *							defect 2814
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3  
 * K Harrell	09/14/2005  Determine if RegClassCd can use Solid Tires
 * 							add SOLID_TIRE_TYPE
 * 							add canUseSolidTires() 
 * 							defect 8310 Ver 5.2.3 			 
 * ---------------------------------------------------------------------
 */

/**
 * The RegistrationWeightFeesCache class provides static methods to 
 * retrieve a particular or a list of RegistrationWeightFeesData based 
 * on different input parameters.
 *
 * <p>Registration Weight Fees Cache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	5.2.3		09/14/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:	08/24/2001 16:44:52
 */

public class RegistrationWeightFeesCache
	extends GeneralCache
	implements java.io.Serializable
{
	private final static String DEFAULT_TIRE_TYPE = "P";
	private final static String SOLID_TIRE_TYPE = "S";

	private static Hashtable shtRegWtFees;

	private final static long serialVersionUID = -7328865683693928146L;
	/**
	 * RegistrationWeightFeesCache constructor comment.
	 */
	public RegistrationWeightFeesCache()
	{
		super();
		shtRegWtFees = new Hashtable();
	}
	/**
	 * Return Cache Constant for Cache
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.REGISTRATION_WEIGHT_FEES_CACHE;
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
		return shtRegWtFees;
	}

	/**
	 * Return boolean to denote that RegClassCd can use Solid Tire. 
	 * 
	 * @param aiRegClassCd int
	 * @return boolean 
	 */
	public static boolean canUseSolidTires(int aiRegClassCd)
	{
		Integer laRegClassCd = new Integer(aiRegClassCd);
		Vector lvData = (Vector) shtRegWtFees.get(laRegClassCd);

		boolean lbCanUseSolidTires = false;
		int liEffDate = new RTSDate().getYYYYMMDDDate();

		if (lvData != null)
		{
			for (int i = 0; i < lvData.size(); i++)
			{
				RegistrationWeightFeesData laData =
					(RegistrationWeightFeesData) lvData.get(i);

				if ((liEffDate >= laData.getRTSEffDate())
					&& (liEffDate <= laData.getRTSEffEndDate())
					&& laData.getTireTypeCd().equals(SOLID_TIRE_TYPE))
				{
					lbCanUseSolidTires = true;
					break;
				}
			}
		}
		return lbCanUseSolidTires;
	}
	/**
	 * Return Vector of RegistrationWeightFeesData Objects based 
	 * upon aiRegClassCd, aiEffDate, aiVehGrossWt
	 * 
	 * @param aiRegClassCd int
	 * @param aiEffDate int
	 * @param aiVehGrossWt int
	 * @return Vector 
	 */
	public static Vector getRegWtFee(
		int aiRegClassCd,
		int aiEffDate,
		int aiVehGrossWt)
	{

		Integer laRegClassCd = new Integer(aiRegClassCd);
		Vector lvData = (Vector) shtRegWtFees.get(laRegClassCd);
		Vector lvReturn = new Vector();
		if (lvData != null)
		{
			for (int i = 0; i < lvData.size(); i++)
			{
				RegistrationWeightFeesData laData =
					(RegistrationWeightFeesData) lvData.get(i);
				if (laData != null)
				{
					if ((aiEffDate >= laData.getRTSEffDate())
						&& (aiEffDate <= laData.getRTSEffEndDate())
						&& (aiVehGrossWt >= laData.getBegWtRnge())
						&& (aiVehGrossWt <= laData.getEndWtRnge()))
					{
						lvReturn.add(laData);
					}
				}
			}
			if (lvReturn.size() > 0)
			{
				return lvReturn;
			}
			else
			{
				return null;
			}

		}
		else
		{
			return null;
		}
	}
	/**
	 * Gets the RegistrationWeightFeesData object.  Returns null if 
	 * object does not exist.
	 * 
	 * @param aiRegClassCd int
	 * @param asTireTypeCd String
	 * @param aiEffDate int
	 * @param aiVehGrossWt int
	 * @return RegistrationWeightFeesData 
	 */

	public static RegistrationWeightFeesData getRegWtFee(
		int aiRegClassCd,
		String aiTireTypeCd,
		int aiEffDate,
		int aiVehGrossWt)
	{

		Integer laRegClassCd = new Integer(aiRegClassCd);
		Vector lvData = (Vector) shtRegWtFees.get(laRegClassCd);
		RegistrationWeightFeesData laData = null;
		boolean lbDataFound = false;
		if (aiTireTypeCd == null || aiTireTypeCd.equals(""))
		{
			aiTireTypeCd = DEFAULT_TIRE_TYPE;
		}
		if (lvData != null)
		{
			for (int i = 0; i < lvData.size(); i++)
			{
				laData = (RegistrationWeightFeesData) lvData.get(i);
				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate())
					&& (aiVehGrossWt >= laData.getBegWtRnge())
					&& (aiVehGrossWt <= laData.getEndWtRnge())
					&& (aiTireTypeCd
						.equals((laData.getTireTypeCd()).trim())))
				{
					lbDataFound = true;
					break;
				}
			}
			if (lbDataFound)
			{
				return laData;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
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
			RegistrationWeightFeesData laData1 =
				new RegistrationWeightFeesData();
			laData1.setBegWtRnge(1);
			laData1.setEndWtRnge(1);
			laData1.setRegClassCd(1);
			laData1.setRTSEffDate(1);
			laData1.setRTSEffEndDate(1);
			laData1.setTireTypeCd("P");
			laData1.setTireTypeFee(new Dollar("1"));
			lvData.addElement(laData1);
			RegistrationWeightFeesData laData2 =
				new RegistrationWeightFeesData();
			laData2.setBegWtRnge(1);
			laData2.setEndWtRnge(1);
			laData2.setRegClassCd(5);
			laData2.setRTSEffDate(2);
			laData2.setRTSEffEndDate(2);
			laData2.setTireTypeCd("S");
			laData2.setTireTypeFee(new Dollar("2"));
			lvData.addElement(laData2);
			RegistrationWeightFeesData laData3 =
				new RegistrationWeightFeesData();
			laData3.setBegWtRnge(1);
			laData3.setEndWtRnge(1);
			laData3.setRegClassCd(6);
			laData3.setRTSEffDate(2);
			laData3.setRTSEffEndDate(2);
			laData3.setTireTypeCd("P");
			laData3.setTireTypeFee(new Dollar("2"));
			lvData.addElement(laData3);
			RegistrationWeightFeesCache laRegistrationWeightFeesCache =
				new RegistrationWeightFeesCache();
			laRegistrationWeightFeesCache.setData(lvData);
			RegistrationWeightFeesData laData =
				RegistrationWeightFeesCache.getRegWtFee(6, "", 2, 1);
			System.out.println("Tire type: " + laData.getTireTypeCd());
			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}
	/**
	 * Clear and populate the hashtable with the vector 
	 *  avRegistrationRenewalsData
	 *
	 * @param avRegistrationWeightFeesData Vector
	 */
	public void setData(Vector avRegistrationWeightFeesData)
	{
		//reset the hashtable
		shtRegWtFees.clear();
		for (int i = 0; i < avRegistrationWeightFeesData.size(); i++)
		{
			RegistrationWeightFeesData laData =
				(
					RegistrationWeightFeesData) avRegistrationWeightFeesData
						.get(
					i);
			int liRegClassCd = laData.getRegClassCd();
			Integer laKey = new Integer(liRegClassCd);
			if (shtRegWtFees.containsKey(laKey))
			{
				Vector lvData = (Vector) shtRegWtFees.get(laKey);
				lvData.add(laData);
			}
			else
			{
				Vector lvData = new Vector();
				lvData.add(laData);
				shtRegWtFees.put(laKey, lvData);
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
	public void setHashtable(Hashtable aHashtable)
	{
		shtRegWtFees = aHashtable;
	}
}
