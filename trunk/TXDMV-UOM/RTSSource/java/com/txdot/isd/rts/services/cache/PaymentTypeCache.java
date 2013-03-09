package com.txdot.isd.rts.services.cache;import java.util.Hashtable;import java.util.Vector;import com.txdot.isd.rts.services.data.PaymentTypeData;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.UtilityMethods;import com.txdot.isd.rts.services.util.constants.CacheConstant;/* * PaymentTypeCache.java * * (c) Texas Department of Transportation 2001 *  * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	06/17/2005	Java 1.4 Cleanup * 							defect 7899 Ver 5.2.3  			  * --------------------------------------------------------------------- *//** * The PaymentTypeCache class provides static methods to  * retrieve a list of PaymentTypeData based  * on different input parameters. * * <p>PaymentTypeCache is being initialized and populated by * the CacheManager when the system starts up.  The data will be * stored in memory and thus will be accessible until the system * shuts down. *  * @version	5.2.3		06/17/2005 * @author	Todd Pederson * <br>Creation Date:	08/14/2001 15:28:54   * *//* &PaymentTypeCache& */public class PaymentTypeCache	extends GeneralCache	implements java.io.Serializable{	/**	* A hashtable of vectors with pymntType as key	*//* &PaymentTypeCache'shtPymntTypes& */	private static Hashtable shtPymntTypes = new Hashtable();	/**	* Flags for getPymntTypes	*//* &PaymentTypeCache'GET_ALL& */	public static final int GET_ALL = 0;	/**	* A flag for getPymntTypes method.  	* Gets all the data where	* CustUseIndi equals 1	*//* &PaymentTypeCache'CUST_USE& */	public static final int CUST_USE = 1;	/**	* A flag for getPymntTypes method.  	* Gets all the data where	* CntyUseIndi equals 1	*//* &PaymentTypeCache'CNTY_USE& */	public static final int CNTY_USE = 2;/* &PaymentTypeCache'CASH& */	public final static int CASH = 1;/* &PaymentTypeCache'CHECK& */	public final static int CHECK = 2;/* &PaymentTypeCache'CHARGE& */	public final static int CHARGE = 3;/* &PaymentTypeCache'TRAVELERS_CHECK& */	public final static int TRAVELERS_CHECK = 4;/* &PaymentTypeCache'MONEY_ORDER& */	public final static int MONEY_ORDER = 5;/* &PaymentTypeCache'CASHIERS_CHECK& */	public final static int CASHIERS_CHECK = 6;/* &PaymentTypeCache'EFT& */	public final static int EFT = 7;/* &PaymentTypeCache'APP_CREDIT& */	public final static int APP_CREDIT = 8;/* &PaymentTypeCache'serialVersionUID& */	private final static long serialVersionUID = 2169719192074146035L;	/**	 * PymntTypeCache constructor comment.	 *//* &PaymentTypeCache.PaymentTypeCache& */	public PaymentTypeCache()	{		super();	}	/**	 * Return the CacheConstant for this cache type	 * 	 * @return int	 *//* &PaymentTypeCache.getCacheFunctionId& */	public int getCacheFunctionId()	{		return CacheConstant.PAYMENT_TYPE_CACHE;	}	/**	 * Get the internally stored Hashtable.	 *	 * <P> Class that inherits from Admin cache is required	 * to implement this method.	 * 	 * @return Hashtable	 *//* &PaymentTypeCache.getHashtable& */	public Hashtable getHashtable()	{		return shtPymntTypes;	}	/**	 * Gets an object of PaymentTypeData based on aiPaymentTypeCd 	 * 	 * @param aiPaymentTypeCd int	 * @return Object 	 *//* &PaymentTypeCache.getPymntTypeFromCd& */	public static PaymentTypeData getPymntTypeFromCd(int aiPaymentTypeCd)	{		Vector lvData = new Vector(shtPymntTypes.values());		PaymentTypeData laPaymentTypeData = null;		for (int i = 0; i < lvData.size(); i++)		{			PaymentTypeData laData = (PaymentTypeData) lvData.get(i);			if (laData.getPymntTypeCd() == aiPaymentTypeCd)			{				laPaymentTypeData = laData;				break;			}		}		return laPaymentTypeData;	}	/**	 * Gets a vector of PaymentTypeData based on different query parameters.  	 *	 * <P>Example:<br>	 * Vector listOfPaymentTypeData = PaymentTypeCache.get(PaymentTypeCache.CUST_USE);<br>	 * In the example above, a vector of PaymentTypeData objects will be returned	 * whose custuseindi equals 1.	 *	 * @param aiFlag int	 * @return Vector 	 * @throws RTSException  	 *//* &PaymentTypeCache.getPymntTypes& */	public static Vector getPymntTypes(int aiFlag) throws RTSException	{		Vector lvData = new Vector(shtPymntTypes.values());		if (aiFlag == GET_ALL)		{			UtilityMethods.sort(lvData);			return lvData;		}		else if (aiFlag == CUST_USE)		{			Vector lvReturn = new Vector();			for (int i = 0; i < lvData.size(); i++)			{				PaymentTypeData laData =					(PaymentTypeData) lvData.get(i);				if (laData.getCustUseIndi() == 1)				{					lvReturn.add(laData);				}			}			UtilityMethods.sort(lvReturn);			return lvReturn;		}		else if (aiFlag == CNTY_USE)		{			Vector lvReturn = new Vector();			for (int i = 0; i < lvData.size(); i++)			{				PaymentTypeData laData =					(PaymentTypeData) lvData.get(i);				if (laData.getCntyUseIndi() == 1)				{					lvReturn.add(laData);				}			}			UtilityMethods.sort(lvReturn);			return lvReturn;		}		else		{			throw new RTSException(				RTSException.JAVA_ERROR,				new Exception("Illegal Flag"));		}	}	/**	 * Test main	 * 	 * @param aarrArgs String[]	 *//* &PaymentTypeCache.main& */	public static void main(String[] aarrArgs)	{		try		{			Vector lvData = new Vector();			PaymentTypeData laData1 = new PaymentTypeData();			laData1.setPymntTypeCd(1);			laData1.setPymntTypeCdDesc("CASH");			laData1.setCntyUseIndi(0);			laData1.setCustUseIndi(1);			lvData.addElement(laData1);			PaymentTypeData laData2 = new PaymentTypeData();			laData2.setPymntTypeCd(2);			laData2.setPymntTypeCdDesc("CHECK");			laData2.setCntyUseIndi(1);			laData2.setCustUseIndi(1);			lvData.addElement(laData2);			PaymentTypeData laData3 = new PaymentTypeData();			laData3.setPymntTypeCd(3);			laData3.setPymntTypeCdDesc("CHARGE");			laData3.setCntyUseIndi(0);			laData3.setCustUseIndi(1);			lvData.addElement(laData3);			PaymentTypeData laData4 = new PaymentTypeData();			laData4.setPymntTypeCd(7);			laData4.setPymntTypeCdDesc("EFT");			laData4.setCntyUseIndi(1);			laData4.setCustUseIndi(0);			lvData.addElement(laData4);			PaymentTypeCache laPaymentTypeCache =				new PaymentTypeCache();			laPaymentTypeCache.setData(lvData);			lvData = PaymentTypeCache.getPymntTypes(2);			PaymentTypeData laData = null;			for (int i = 0; i < lvData.size(); i++)			{				laData = (PaymentTypeData) lvData.get(i);				System.out.println(laData.getPymntTypeCdDesc());			}			laData.getPymntTypeCdDesc();			System.out.println("done");		}		catch (Exception aeEx)		{			aeEx.printStackTrace();		}	}	/**	 * Clear and populate the hashtable with the vector 	 * lvPaymentTypeData	 * 	 * @param avPaymentTypeData Vector 	 *//* &PaymentTypeCache.setData& */	public void setData(Vector avPaymentTypeData)	{		//reset data		shtPymntTypes.clear();		for (int i = 0; i < avPaymentTypeData.size(); i++)		{			PaymentTypeData laData =				(PaymentTypeData) avPaymentTypeData.get(i);			shtPymntTypes.put(				new Integer(laData.getPymntTypeCd()),				laData);		}	}	/**	 * Set the internally stored Hashtable.	 *	 * <P> Class that inherits from Admin cache is required	 * to implement this method.	 * 	 * @param ahtHashtable Hashtable	 *//* &PaymentTypeCache.setHashtable& */	public void setHashtable(Hashtable ahtHashtable)	{		shtPymntTypes = ahtHashtable;	}}/* #PaymentTypeCache# */