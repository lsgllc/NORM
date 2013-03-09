package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ClassToPlateData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * ClassToPlateCache.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/14/2005	Created
 * 							defect 8218 Ver 5.2.3 
 * K Harrell	06/19/2007	add getPLPDLRRegClassCd
 * 							add PER_DLR_REGCLASSCD, PER_DLR_MC_REGCLASSCD 
 * 							defect 9085 Ver Special Plates 
 * K Harrell	01/17/2008	add isPTOEligible()
 * 							defect 9524 Ver 3 Amigos Prep 
 * B Hargrove	01/17/2008	add getClassToPlateVec()
 * 							defect 9502 Ver 3 Amigos Prep 
 * B Hargrove	01/14/2008	Add method to return a vector of all
 * 							Class To Plate rows with RTSEffEndDate = 
 * 							99991231.
 * 							add getClassToPlateVec()
 * 							defect 9502 Ver 3_Amigos_PH_A 
 * K Harrell	01/17/2008	add isPTOEligible()
 * 							defect 9524 Ver 3 Amigos Prep 
 * ---------------------------------------------------------------------
 */

/**
 * The ClassToPlateCache class provides static method to retrieve a 
 * vector of  ClassToPlateData objects based on a key
 *
 * <p>ClassToPlateCache is being initialized and populated by the 
 * CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version	3 Amigos Prep	01/17/2008 
 * @author	Kathy Harrell
 * <br>Creation Date:		06/14/2005 17:24:00
 */
public class ClassToPlateCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	 * A hashtable of vectors with regClassCd,regpltcd as key
	 */
	private static Hashtable shtClassToPlate = new Hashtable();

	// defect 9085 
	private static int PER_DLR_REGCLASSCD = 79;
	private static int PER_DLR_MC_REGCLASSCD = 80;
	// end defect 9085 

	private final static long serialVersionUID = 3293510730068844773L;

	/**
	 * ClassToPlateCache constructor comment.
	 */
	public ClassToPlateCache()
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
		return CacheConstant.CLASS_TO_PLATE_CACHE;
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
		return shtClassToPlate;
	}
	
	/**
	 * Return associated RegClassCd (79 || 80) for PLPDLR, PLPDLRMC
	 *
	 * @param asRegPltCd String 
	 * @return int
	 */
	public static int getPLPDLRRegClassCd(String asRegPltCd)
	{
		Vector lvVector = null;
		int liToday = new RTSDate().getYYYYMMDDDate();
		int liRegClassCd = 0;

		for (int i = PER_DLR_REGCLASSCD;
			i <= PER_DLR_MC_REGCLASSCD;
			i++)
		{
			lvVector = getClassToPlate(i, asRegPltCd, liToday);
			if (lvVector != null && lvVector.size() > 0)
			{
				liRegClassCd = i;
				break;
			}
		}
		return liRegClassCd;
	}

	/** 
	 * Return boolean to denote if RegClassCd/RegPltCd is PTO Eligible
	 * 
	 * @param aiRegClassCd
	 * @param asRegPlateCd
	 * @return boolean 
	 */
	public static boolean isPTOEligible(
		int aiRegClassCd,
		String asRegPlateCd)
	{
		boolean lbReturn = false;
		int liToday = new RTSDate().getYYYYMMDDDate();
		Vector lvClassToPltData =
			getClassToPlate(aiRegClassCd, asRegPlateCd, liToday);
		if (lvClassToPltData != null)
		{
			for (int i = 0; i < lvClassToPltData.size(); i++)
			{
				ClassToPlateData laClassToPltData =
					(ClassToPlateData) lvClassToPltData.get(i);
				if (laClassToPltData.getPTOElgbleIndi() == 1)
				{
					lbReturn = true;
					break;
				}
			}
		}
		return lbReturn;
	}

	/**
	 * Return vector of all values for given RegClassCd, RegPltCd,
	 * Effective Date  
	 *
	 * @param aiRegClassCd int
	 * @param asRegPlateCd String 
	 * @param aiEffDate    int 
	 * @return Vector
	 */
	public static Vector getClassToPlate(
		int aiRegClassCd,
		String asRegPlateCd,
		int aiEffDate)
	{
		Vector lvReturn = new Vector();

		String lsPrimaryKey =
			UtilityMethods.constructPrimaryKey(
				new String[] {
					String.valueOf(aiRegClassCd),
					asRegPlateCd });

		if (shtClassToPlate.containsKey(lsPrimaryKey))
		{
			Vector lvClassToPlate =
				(Vector) shtClassToPlate.get(lsPrimaryKey);

			for (int i = 0; i < lvClassToPlate.size(); i++)
			{
				ClassToPlateData laData =
					(ClassToPlateData) lvClassToPlate.get(i);

				if (aiEffDate >= laData.getRTSEffDate()
					&& aiEffDate <= laData.getRTSEffEndDate())
				{
					lvReturn.addElement(laData);
				}
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
	
	/**
	 * Get a vector of Class To Plate objects
	 * Returns only 'lastest' row (99991231)
	 * 
	 * @return Vector
	 */
	public static Vector getClassToPlateVec()
	{
		Vector lvReturn = new Vector();
		String lsPrimaryKey = new String();
 
		for (Enumeration laEnum = shtClassToPlate.keys();
				laEnum.hasMoreElements();
			)
		{
		 	lsPrimaryKey = (String) laEnum.nextElement();
 
		 	Vector lvClassToPlate =
		  		(Vector) shtClassToPlate.get(lsPrimaryKey);
 
		 	for (int i = 0; i < lvClassToPlate.size(); i++)
		 	{
		  		ClassToPlateData laData =
		   			(ClassToPlateData) lvClassToPlate.get(i);
 
				if (laData.getRTSEffEndDate() == 99991231)
		  		{
		   			lvReturn.addElement(laData);
		  		}
		 	}
		}
		return lvReturn;
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
			Vector lvClassToPlateData = new Vector();

			ClassToPlateData laClassToPlateData1 =
				new ClassToPlateData();
			laClassToPlateData1.setRegClassCd(25);
			laClassToPlateData1.setRegPltCd("PSP");
			laClassToPlateData1.setReplPltCd("PSP");
			laClassToPlateData1.setRTSEffDate(20010101);
			laClassToPlateData1.setRTSEffEndDate(99991231);
			laClassToPlateData1.setPTOElgbleIndi(1);
			lvClassToPlateData.addElement(laClassToPlateData1);

			ClassToPlateData laClassToPlateData2 =
				new ClassToPlateData();
			laClassToPlateData2.setRegClassCd(25);
			laClassToPlateData2.setRegPltCd("PSP");
			laClassToPlateData2.setReplPltCd("PLP");
			laClassToPlateData2.setRTSEffDate(20010101);
			laClassToPlateData2.setRTSEffEndDate(99991231);
			laClassToPlateData2.setPTOElgbleIndi(1);
			lvClassToPlateData.addElement(laClassToPlateData2);

			ClassToPlateData laClassToPlateData3 =
				new ClassToPlateData();
			laClassToPlateData3.setRegClassCd(35);
			laClassToPlateData3.setRegPltCd("TKP");
			laClassToPlateData3.setReplPltCd("TKP");
			laClassToPlateData3.setRTSEffDate(20010301);
			laClassToPlateData3.setRTSEffEndDate(200103030);
			laClassToPlateData3.setPTOElgbleIndi(1);
			lvClassToPlateData.addElement(laClassToPlateData3);

			ClassToPlateData laClassToPlateData4 =
				new ClassToPlateData();
			laClassToPlateData4.setRegClassCd(35);
			laClassToPlateData4.setRegPltCd("TKP");
			laClassToPlateData4.setReplPltCd("PLP");
			laClassToPlateData4.setRTSEffDate(20010401);
			laClassToPlateData4.setRTSEffEndDate(200104030);
			laClassToPlateData4.setPTOElgbleIndi(0);
			lvClassToPlateData.addElement(laClassToPlateData4);

			ClassToPlateCache laClassToPlateCache =
				new ClassToPlateCache();
			laClassToPlateCache.setData(lvClassToPlateData);

			// Testing, Testing 
			// Find Replacement Plates 
			Vector lvClassToPlateData2 =
				ClassToPlateCache.getClassToPlate(25, "PSP", 20050602);

			for (int i = 0; i < lvClassToPlateData2.size(); i++)
			{
				int j = i + 1;
				ClassToPlateData laClassToPlateData =
					(ClassToPlateData) lvClassToPlateData2.elementAt(i);
				System.out.println("--" + j + "--");
				System.out.println(laClassToPlateData.getRegClassCd());
				System.out.println(laClassToPlateData.getRegPltCd());
				System.out.println(laClassToPlateData.getReplPltCd());
				System.out.println(laClassToPlateData.getRTSEffDate());
				System.out.println(laClassToPlateData.getPTOElgbleIndi());
				System.out.println(
					laClassToPlateData.getRTSEffEndDate());
			}
			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}

	/**
	 * Clear and populate the hashtable with Vector avClassToPlateData
	 * 
	 * @param avClassToPlateData Vector
	 */
	public void setData(Vector avClassToPlateData)
	{
		//reset the hashtable
		shtClassToPlate.clear();

		for (int i = 0; i < avClassToPlateData.size(); i++)
		{
			ClassToPlateData laClassToPlateData =
				(ClassToPlateData) avClassToPlateData.get(i);

			String lsRegClassCd =
				"" + laClassToPlateData.getRegClassCd();

			String lsRegPltCd = laClassToPlateData.getRegPltCd();

			String lsPrimaryKey =
				UtilityMethods.constructPrimaryKey(
					new String[] { lsRegClassCd, lsRegPltCd });

			if (shtClassToPlate.containsKey(lsPrimaryKey))
			{
				Vector lvClassToPlateData =
					(Vector) shtClassToPlate.get(lsPrimaryKey);
				lvClassToPlateData.add(laClassToPlateData);
			}
			else
			{
				Vector lvClassToPlateData = new Vector();
				lvClassToPlateData.addElement(laClassToPlateData);
				shtClassToPlate.put(lsPrimaryKey, lvClassToPlateData);
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
		shtClassToPlate = ahtHashtable;
	}

}
