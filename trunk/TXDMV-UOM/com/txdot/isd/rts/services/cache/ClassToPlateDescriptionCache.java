package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ClassToPlateDescriptionData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * ClassToPlateDescriptionCache.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/01/2005	Created
 * 							defect 8218 Ver 5.2.3
 * K Harrell	07/09/2007	add isSpecialPlateOnly()
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * The ClassToPlateDescriptionCache class provides static method to 
 * retrieve a vector of ClassToPlateDescriptionData objects 
 * based on a key
 *
 * <p>ClassToPlateDescriptionCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	Special Plates	07/09/2007
 * @author	Kathy Harrell
 * <br>Creation Date:		06/14/2005	15:08:00
 */
public class ClassToPlateDescriptionCache
	extends GeneralCache
	implements java.io.Serializable
{

	/**
	 * A hashtable of vectors 
	 */
	private static Hashtable shtClassToPltDesc = new Hashtable();

	private final static long serialVersionUID = -5229021349232661129L;

	/**
	 * ClassToPlateDescriptionCache default constructor.
	 */

	public ClassToPlateDescriptionCache()
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
		return CacheConstant.CLASS_TO_PLATE_DESCRIPTION_CACHE;
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
		return shtClassToPltDesc;
	}
	/** 
	 * 
	 * Is Special Plate Only
	 * 
	 * @param aiRegClassCd
	 * @return boolean
	 */
	public static boolean isSpecialPlateOnly(int aiRegClassCd)
	{
		boolean lbSpclPltOnly = true;
		Vector lvDataVector =
			getClassToPltDescs(
				aiRegClassCd,
				new RTSDate().getYYYYMMDDDate());

		if (lvDataVector != null)
		{
			for (int i = 0; i < lvDataVector.size(); i++)
			{
				ClassToPlateDescriptionData laData =
					(ClassToPlateDescriptionData) lvDataVector.get(i);
				String lsRegPltCd = laData.getRegPltCd();
				if (!PlateTypeCache.isSpclPlate(lsRegPltCd))
				{
					lbSpclPltOnly = false;
					break;
				}
			}
		}
		return lbSpclPltOnly;
	}
	/**
	 * Returns a vector of ClassToPlateDescriptionData objects whose 
	 * dates are effective for the given RegClassCd, Effective Date 
	 *
	 * @param  aiRegClassCd int
	 * @param  aiEffDate    int
	 * @return Vector  
	 */

	public static Vector getClassToPltDescs(
		int aiRegClassCd,
		int aiEffDate)
	{
		String lsPrimaryKey = "" + aiRegClassCd;

		Object laObj = shtClassToPltDesc.get(lsPrimaryKey);

		if (laObj instanceof Vector)
		{
			Vector lvDataVector = (Vector) laObj;

			Vector lvReturnVector = new Vector();

			for (int i = 0; i < lvDataVector.size(); i++)
			{
				ClassToPlateDescriptionData laData =
					(ClassToPlateDescriptionData) lvDataVector.get(i);
				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate()))
				{
					lvReturnVector.addElement(laData);
				}
			}

			if (lvReturnVector.size() == 0)
			{
				return null;
			}
			else
			{
				UtilityMethods.sort(lvReturnVector);
				return lvReturnVector;
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
	public static void main(String[] aarrArgs)
	{
		try
		{
			Vector lvClassToPltDescData = new Vector();

			ClassToPlateDescriptionData ClassToPltDescData1 =
				new ClassToPlateDescriptionData();
			ClassToPltDescData1.setRegClassCd(25);
			ClassToPltDescData1.setRegPltCd("PSP");
			ClassToPltDescData1.setItmCdDesc("Passenger Plate");
			ClassToPltDescData1.setRTSEffDate(20010101);
			ClassToPltDescData1.setRTSEffEndDate(20010229);

			lvClassToPltDescData.addElement(ClassToPltDescData1);

			ClassToPlateDescriptionData ClassToPltDescData2 =
				new ClassToPlateDescriptionData();
			ClassToPltDescData2.setRegClassCd(25);
			ClassToPltDescData2.setRegPltCd("PLP");
			ClassToPltDescData2.setItmCdDesc("Personalized Plate");
			ClassToPltDescData2.setRTSEffDate(20010101);
			ClassToPltDescData2.setRTSEffEndDate(20010229);

			lvClassToPltDescData.addElement(ClassToPltDescData2);

			ClassToPlateDescriptionData ClassToPltDescData3 =
				new ClassToPlateDescriptionData();
			ClassToPltDescData3.setRegClassCd(36);
			ClassToPltDescData3.setRegPltCd("PSP");
			ClassToPltDescData3.setItmCdDesc("Passenger Plate");
			ClassToPltDescData3.setRTSEffDate(20010101);
			ClassToPltDescData3.setRTSEffEndDate(20010229);

			lvClassToPltDescData.addElement(ClassToPltDescData3);

			ClassToPlateDescriptionData ClassToPltDescData4 =
				new ClassToPlateDescriptionData();
			ClassToPltDescData4.setRegClassCd(25);
			ClassToPltDescData4.setRegPltCd("PSP");
			ClassToPltDescData4.setItmCdDesc("Old Passenger Plate");
			ClassToPltDescData4.setRTSEffDate(19990101);
			ClassToPltDescData4.setRTSEffEndDate(19991231);

			lvClassToPltDescData.addElement(ClassToPltDescData4);

			ClassToPlateDescriptionCache laCache =
				new ClassToPlateDescriptionCache();
			laCache.setData(lvClassToPltDescData);

			Vector lvClassToPltDesc2 =
				ClassToPlateDescriptionCache.getClassToPltDescs(
					25,
					20010228);

			for (int i = 0; i < lvClassToPltDesc2.size(); i++)
			{
				ClassToPlateDescriptionData laClassToPltDescData =
					(
						ClassToPlateDescriptionData) lvClassToPltDesc2
							.elementAt(
						i);
				System.out.println(
					laClassToPltDescData.getRegClassCd()
						+ "---"
						+ laClassToPltDescData.getRegPltCd()
						+ "---"
						+ laClassToPltDescData.getItmCdDesc()
						+ "---"
						+ laClassToPltDescData.getRTSEffDate());
			}

			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}
	/**
	* Clear and populate the hashtable with the vector avClassToPltDesc
	* 
	* @param avClassToPltDesc Vector
	*/
	public void setData(Vector avClassToPltDesc)
	{
		//reset the hashtable
		shtClassToPltDesc.clear();
		for (int i = 0; i < avClassToPltDesc.size(); i++)
		{
			ClassToPlateDescriptionData laClassToPltDescData =
				(ClassToPlateDescriptionData) avClassToPltDesc.get(i);

			String lsPrimaryKey =
				"" + laClassToPltDescData.getRegClassCd();

			if (shtClassToPltDesc.containsKey(lsPrimaryKey))
			{
				Vector lvClassToPltDescData =
					(Vector) shtClassToPltDesc.get(lsPrimaryKey);
				lvClassToPltDescData.add(laClassToPltDescData);
			}
			else
			{
				Vector lvClassToPltDescData = new Vector();
				lvClassToPltDescData.addElement(laClassToPltDescData);
				shtClassToPltDesc.put(
					lsPrimaryKey,
					lvClassToPltDescData);
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
	 * 
	 */
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtClassToPltDesc = ahtHashtable;

	}
}
