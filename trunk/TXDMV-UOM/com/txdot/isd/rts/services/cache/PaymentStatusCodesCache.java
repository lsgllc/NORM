package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.PaymentStatusCodesData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * PaymentStatusCodesCache.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Duggirala	08/24/2001	setData() method created
 * R Duggirala	08/29/2001	Add comments
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3  			 
 * ---------------------------------------------------------------------
 */
/**
 * The PaymentStatusCodesCache class provides static method to 
 * retrieve a particular PaymentStatusCodesData based 
 * on a key
 *
 * <p>PaymentStatusCodesCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version 5.2.3		06/17/2005 
 * @author	Rakesh Duggirala
 * <br>Creation Date: 
 */

public class PaymentStatusCodesCache
	extends GeneralCache
	implements java.io.Serializable
{

	/**
	* A hashtable of vectors with pymntStatusCd as key
	*/
	private static Hashtable shtPymntStatCds = new Hashtable();

	public final static String REMITTED = "R";
	public final static String DEPOSITED = "D";
	public final static String VOIDED = "V";

	private final static long serialVersionUID = -5999291916701004423L;
	
	/**
	* Default PaymentStatusCodesCache constructor.
	*/

	public PaymentStatusCodesCache()
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
		return CacheConstant.PAYMENT_STATUS_CODES_CACHE;
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
		return shtPymntStatCds;
	}
	/**
	 * Returns the PaymentStatusCodesData object based on the input pymntStatusCd.
	 *
	 * @param asPymntStatusCd String
	 * @return PaymentStatusCodesData 
	 */
	public static PaymentStatusCodesData getPymntStatusCd(String asPymntStatusCd)
	{
		Object laObject = shtPymntStatCds.get(asPymntStatusCd);

		if (laObject != null)
		{
			return (PaymentStatusCodesData) laObject;
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

			PaymentStatusCodesData laData1 =
				new PaymentStatusCodesData();
			laData1.setPymntStatusCd("a");
			laData1.setPymntStatusDesc("a desc");
			lvData.addElement(laData1);

			PaymentStatusCodesData laData2 =
				new PaymentStatusCodesData();
			laData2.setPymntStatusCd("b");
			laData2.setPymntStatusDesc("b desc");
			lvData.addElement(laData2);

			PaymentStatusCodesData laData3 =
				new PaymentStatusCodesData();
			laData3.setPymntStatusCd("c");
			laData3.setPymntStatusDesc("c desc");
			lvData.addElement(laData3);

			PaymentStatusCodesData laData4 =
				new PaymentStatusCodesData();
			laData4.setPymntStatusCd("d");
			laData4.setPymntStatusDesc("d desc");
			lvData.addElement(laData4);

			PaymentStatusCodesCache laPaymentStatusCodesCache =
				new PaymentStatusCodesCache();

			laPaymentStatusCodesCache.setData(lvData);

			PaymentStatusCodesData laData =
				PaymentStatusCodesCache.getPymntStatusCd("d");

			System.out.println(
				laData.getPymntStatusCd()
					+ "---"
					+ laData.getPymntStatusDesc());
			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}
	/**
	 * Clear and populate the hashtable with the vector 
	 * avPymntStatCdsData
	 * 
	 * @param avPymntStatCdsData Vector 
	 */
	public void setData(Vector avPymntStatCdsData)
	{

		shtPymntStatCds.clear();
		for (int i = 0; i < avPymntStatCdsData.size(); i++)
		{
			PaymentStatusCodesData laData =
				(PaymentStatusCodesData) avPymntStatCdsData.get(i);
			String lsPrimaryKey = laData.getPymntStatusCd();
			shtPymntStatCds.put(lsPrimaryKey, laData);

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
		shtPymntStatCds = ahtHashtable;
	}
}
