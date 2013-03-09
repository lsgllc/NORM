package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TransactionCodesData;
import com.txdot.isd.rts.services.data.TransactionControl;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * TransactionCodesCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/20/2005	Java 1.4 Work	
 * 							defect 7899 Ver 5.2.3
 * K Harrell	02/26/2009	add getTransCds(), getETtlTransCds(), 
 * 							 isETitleTransCd()
 * 							delete getTransCdVec()
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	04/14/2009	Only present POS Transactions 
 * 							modify getETitleTransCds() 
 * 							defect 9972 Ver Defect_POS_E 
 * K Harrell	06/20/2010	add getTransCdDesc()
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */
/**
 * The TransactionCodesCache class provides static method to 
 * retrieve a vector of TransactionCodesData objects based on a key
 *
 * <p>TransactionCodesCache is being initialized and populated by the 
 * CacheManager when the system starts up.  The data will be stored 
 * in memory and thus will be accessible until the system shuts down.
 * 
 * @version	 6.5.0			06/20/2010
 * @author	Sunil Govindappa 
 * <br>Creation Date: 		09/06/2001 13:30   
 */

public class TransactionCodesCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	* Transaction codes hashtable
	*/
	private static Hashtable shtTransCds = new Hashtable();

	private final static long serialVersionUID = -174669266455162715L;

	/**
	 * TransactionCodesCache default constructor.
	 */
	public TransactionCodesCache()
	{
		super();
	}

	/**
	 * Returns the function id of the Transaction Codes Cache
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.TRANSACTION_CODES_CACHE;
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
		return shtTransCds;
	}

	/**
	 * Get the Transaction Codes Data object based on Transaction Code
	 * 
	 * @param  asTransCd String
	 * @return TransactionCodesData
	 * @throws RTSException  
	 */
	public static TransactionCodesData getTransCd(String asTransCd)
		throws RTSException
	{
		Object laReturnData = shtTransCds.get(asTransCd);

		if (laReturnData != null)
		{
			return (TransactionCodesData) laReturnData;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Return TransCdDesc for a give TransCd 
	 * 
	 * @param  asTransCd String
	 * @return String 
	 * @throws RTSException  
	 */
	public static String getTransCdDesc(String asTransCd)
		throws RTSException
	{
		String lsTransCdDesc = new String();
		if (asTransCd != null)
		{
			Object laReturnData = shtTransCds.get(asTransCd);

			if (laReturnData != null)
			{
				lsTransCdDesc =
					((TransactionCodesData) laReturnData)
						.getTransCdDesc();
			}
		}
		return lsTransCdDesc != null ? lsTransCdDesc : new String();
	}

	/**
	 * 
	 * Is ETitle TransCd 
	 * 
	 * @param asTransCd
	 * @return boolean 
	 */
	public static boolean isETitleTransCd(String asTransCd)
	{
		boolean lbETitle = false;
		try
		{
			TransactionCodesData laData = getTransCd(asTransCd);
			lbETitle = laData.isETtlTrans();
		}
		catch (RTSException aeRTSEx)
		{
			System.out.println(
				"Error in TransactionCodesCache.isETitleTransCd()");
		}
		return lbETitle;

	}

	/**
	 * Return a vector of Transaction Code Data where TransCds support 
	 * ETitle
	 * 
	 * @return Vector
	 */
	public static Vector getETitleTransCds()
	{
		// Only POS transactions will be in Transaction Control 
		Hashtable lhtTransCtrl =
			com
				.txdot
				.isd
				.rts
				.services
				.common
				.Transaction
				.getTransactionControl();

		Vector lvETtlTransCds = new Vector();

		Vector lvTransCds = new Vector(shtTransCds.values());

		for (int i = 0; i < lvTransCds.size(); i++)
		{
			TransactionCodesData laData =
				(TransactionCodesData) lvTransCds.elementAt(i);

			if (laData.isETtlTrans()
				&& (TransactionControl) lhtTransCtrl.get(
					laData.getTransCd())
					!= null)
			{
				lvETtlTransCds.add(laData);
			}
		}
		return lvETtlTransCds;
	}

	/**
	 * Get a vector of Transaction Codes objects
	 * 
	 * @return Vector
	 */
	public static Vector getTransCds()
	{
		if (shtTransCds.size() == 0)
		{
			return null;
		}
		else
		{
			return new Vector(shtTransCds.values());

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
			Vector lvTransCds = new Vector();

			TransactionCodesData laData1 = new TransactionCodesData();
			laData1.setTransCd("AAA");
			laData1.setTransCdDesc("AAA desc");
			laData1.setRcptMsgCd(111);
			laData1.setVoidableTransIndi(1);
			laData1.setCumulativeTransCd(11);
			laData1.setFeeSourceCd(11);
			laData1.setCashDrawerIndi(1);
			laData1.setInvTransCdType(11);
			lvTransCds.addElement(laData1);

			TransactionCodesData laData2 = new TransactionCodesData();
			laData2.setTransCd("BBB");
			laData2.setTransCdDesc("BBB desc");
			laData2.setRcptMsgCd(222);
			laData2.setVoidableTransIndi(0);
			laData2.setCumulativeTransCd(22);
			laData2.setFeeSourceCd(22);
			laData2.setCashDrawerIndi(0);
			laData2.setInvTransCdType(22);
			lvTransCds.addElement(laData2);

			TransactionCodesCache laTransCdsCache =
				new TransactionCodesCache();
			laTransCdsCache.setData(lvTransCds);

			TransactionCodesData laData =
				TransactionCodesCache.getTransCd("AAA");

			System.out.println(
				"A1. Transaction Code Desc:" + laData.getTransCdDesc());

			Vector lvTransCdsData = TransactionCodesCache.getTransCds();

			int i = 0;

			for (Enumeration e = lvTransCdsData.elements();
				e.hasMoreElements();
				)
			{
				i++;
				laData = (TransactionCodesData) e.nextElement();
				System.out.println(
					"B"
						+ i
						+ ". Transaction Code Desc:"
						+ laData.getTransCdDesc());
			}

			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}
	/**
	 * Set the data of the cache to contain the elements of the vector
	 * being passed in.
	 * 
	 * @param  avTransCdsData
	 * @throws RTSException 
	 */
	public void setData(Vector avTransCdsData) throws RTSException
	{
		shtTransCds.clear();

		if (avTransCdsData == null)
		{
			return;
		}

		for (int i = 0; i < avTransCdsData.size(); i++)
		{
			TransactionCodesData laData =
				(TransactionCodesData) avTransCdsData.get(i);

			shtTransCds.put(laData.getTransCd(), laData);
		}
	}
	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable the hashtable
	 */
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtTransCds = ahtHashtable;
	}
}
