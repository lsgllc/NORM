package com.txdot.isd.rts.services.cache;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.CommercialVehicleWeightsData;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * CommercialVehicleWeightsCache.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/06/2001	Add comments
 * C Walker		08/28/2001	Add comments
 * K Harrell	09/04/2001	Replace reference to 'dollar'
 * C Walker		09/05/2001	Add Hungarian Notation
 * J Kwik		10/23/2001	Modify setData() & getCommvehWts()argument
 * 							to String. 
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * The CommercialVehicleWeightsCache class provides static methods to 
 * retrieve a particular CommercialVehicleWeightsData object based 
 * on different input parameters.
 *
 * <p>CommercialVehicleWeightsCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version 5.2.3		06/17/2005 
 * @author	Charlie Walker
 * <br>Creation Date:	08/28/2001	 11:03:19
 */

public class CommercialVehicleWeightsCache
	extends GeneralCache
	implements Serializable
{

	/**
	* A hashtable of vectors with vehTon as key
	*/
	private static Hashtable shtCommVehWts = new Hashtable();

	private final static long serialVersionUID = -2966461694498880561L;
	/**
	 * CommercialVehicleWeightsCache constructor comment.
	 */
	public CommercialVehicleWeightsCache()
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
		return CacheConstant.COMMERCIAL_VEHICLE_WEIGHTS_CACHE;
	}
	/**
	 * Returns the CommercialVehicleWeightsData object based on the input
	 * vehTon.
	 *
	 * @param  aaDollarVehTon Dollar
	 * @return CommercialVehicleWeightsData 
	 */
	public static CommercialVehicleWeightsData getCommVehWts(Dollar aaDollarVehTon)
	{
		Object laObject = shtCommVehWts.get(aaDollarVehTon.toString());
		if (laObject != null)
		{
			return (CommercialVehicleWeightsData) laObject;
		}
		else
		{
			return null;
		}
	}
	/**
	 * Get the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @return Hashtable
	 */
	public java.util.Hashtable getHashtable()
	{
		return shtCommVehWts;
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
			Vector lvCommVehWtsData = new Vector();

			CommercialVehicleWeightsData laCommVehWtsData1 =
				new CommercialVehicleWeightsData();
			laCommVehWtsData1.setMinCaryngCap(1000);
			laCommVehWtsData1.setMinGrossWtAllowble(1111);
			laCommVehWtsData1.setVehTon(new Dollar(1.01));
			laCommVehWtsData1.setVehTonDesc("001 desc");
			lvCommVehWtsData.addElement(laCommVehWtsData1);

			CommercialVehicleWeightsData laCommVehWtsData2 =
				new CommercialVehicleWeightsData();
			laCommVehWtsData2.setMinCaryngCap(2000);
			laCommVehWtsData2.setMinGrossWtAllowble(2222);
			laCommVehWtsData2.setVehTon(new Dollar(2.02));
			laCommVehWtsData2.setVehTonDesc("002 desc");
			lvCommVehWtsData.addElement(laCommVehWtsData2);

			CommercialVehicleWeightsData laCommVehWtsData3 =
				new CommercialVehicleWeightsData();
			laCommVehWtsData3.setMinCaryngCap(3000);
			laCommVehWtsData3.setMinGrossWtAllowble(3333);
			laCommVehWtsData3.setVehTon(new Dollar(3.03));
			laCommVehWtsData3.setVehTonDesc("003 desc");
			lvCommVehWtsData.addElement(laCommVehWtsData3);

			CommercialVehicleWeightsData laCommVehWtsData4 =
				new CommercialVehicleWeightsData();
			laCommVehWtsData4.setMinCaryngCap(4000);
			laCommVehWtsData4.setMinGrossWtAllowble(4444);
			laCommVehWtsData4.setVehTon(new Dollar(4.04));
			laCommVehWtsData4.setVehTonDesc("004 desc");
			lvCommVehWtsData.addElement(laCommVehWtsData4);

			CommercialVehicleWeightsCache laCommVehWtsCache =
				new CommercialVehicleWeightsCache();
			laCommVehWtsCache.setData(lvCommVehWtsData);

			CommercialVehicleWeightsData laCommVehWtsData =
				CommercialVehicleWeightsCache.getCommVehWts(
					new Dollar(2.02));
			System.out.println(laCommVehWtsData.getVehTonDesc());
			System.out.println(laCommVehWtsData.getMinCaryngCap());
			System.out.println(
				laCommVehWtsData.getMinGrossWtAllowble());
			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}
	/**
	 * Clear and populate the hashtable with the vector 
	 * avCommVehWtsDataVector.
	 *
	 * @param avCommVehWtsDataVector Vector
	 */
	public void setData(Vector avCommVehWtsDataVector)
	{
		//reset the hashtable
		shtCommVehWts.clear();
		for (int i = 0; i < avCommVehWtsDataVector.size(); i++)
		{
			CommercialVehicleWeightsData laCommVehWtsData =
				(
					CommercialVehicleWeightsData) avCommVehWtsDataVector
						.get(
					i);
			shtCommVehWts.put(
				laCommVehWtsData.getVehTon().toString(),
				laCommVehWtsData);
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
	public void setHashtable(java.util.Hashtable ahtHashtable)
	{
		shtCommVehWts = ahtHashtable;
	}
}
