package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TitleTERPPercentData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * TitleTERPPercentCache.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work	
 * 							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Cache Class for Title TERP Percent 
 *
 * @version	5.2.3		06/19/2005
 * @author	Kathy Harrell 
 * <br>Creation Date: 	07/30/2003 20:10:33  
 */

public class TitleTERPPercentCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	* TtlTERPPrcnt hashtable
	*/
	private static Hashtable shtTtlTERPPrcnt = new Hashtable();

	private final static long serialVersionUID = -7462209162029465880L;

	/**
	 * TitleTERPPercentCache default constructor.
	 */
	public TitleTERPPercentCache()
	{
		super();
	}

	/**
	 * Get Cache FunctionId for TitleTerpPercent
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.TITLE_TERP_PERCENT_CACHE;
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
		return shtTtlTERPPrcnt;
	}
	/**
	 * Get a vector of Title TERP Percent objects
	 * 
	 * @return Vector
	 */
	public static Vector getTtlTERPPrcntVec()
	{

		if (shtTtlTERPPrcnt.size() == 0)
		{
			return null;
		}
		else
		{
			Vector lvReturn = new Vector();
			for (Enumeration e = shtTtlTERPPrcnt.elements();
				e.hasMoreElements();
				)
			{
				lvReturn.addElement(e.nextElement());

			}
			return lvReturn;

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
			Vector lvInput = new Vector();
			TitleTERPPercentData laTTPD1 = new TitleTERPPercentData();

			laTTPD1.setSalesTaxCat("LEASING");
			laTTPD1.setBegModlYrRnge(0);
			laTTPD1.setEndModlYrRnge(1996);
			laTTPD1.setBegDieselWtRnge(14001);
			laTTPD1.setEndDieselWtRnge(100000);
			laTTPD1.setDieselIndi(1);
			laTTPD1.setTtlTERPPrcnt(new Dollar("2.50"));
			laTTPD1.setTERPPrcntAcctItmCd("SLXTXEMI");
			laTTPD1.setRTSEffDate(20030701);
			laTTPD1.setRTSEffEndDate(99991231);
			lvInput.addElement(laTTPD1);

			TitleTERPPercentData laTTPD2 = new TitleTERPPercentData();
			laTTPD2.setSalesTaxCat("LEASING");
			laTTPD2.setBegModlYrRnge(1997);
			laTTPD2.setEndModlYrRnge(9999);
			laTTPD2.setBegDieselWtRnge(14001);
			laTTPD2.setEndDieselWtRnge(100000);
			laTTPD2.setDieselIndi(1);
			laTTPD2.setTtlTERPPrcnt(new Dollar("1.50"));
			laTTPD2.setTtlTERPPrcnt(new Dollar("1.5"));
			laTTPD2.setTERPPrcntAcctItmCd("SLXTXEP1");
			laTTPD2.setRTSEffDate(20030701);
			laTTPD2.setRTSEffEndDate(99991231);
			lvInput.addElement(laTTPD2);
			
			TitleTERPPercentData laTTPD3 = new TitleTERPPercentData();
			laTTPD3.setSalesTaxCat("NEW RESIDT");
			laTTPD3.setBegModlYrRnge(0);
			laTTPD3.setEndModlYrRnge(1996);
			laTTPD3.setBegDieselWtRnge(14001);
			laTTPD3.setEndDieselWtRnge(100000);
			laTTPD3.setDieselIndi(1);
			laTTPD3.setTtlTERPPrcnt(new Dollar("2.5"));
			laTTPD3.setTERPPrcntAcctItmCd("SLXTXEMI");
			laTTPD3.setRTSEffDate(20030701);
			laTTPD3.setRTSEffEndDate(99991231);
			lvInput.addElement(laTTPD3);

			TitleTERPPercentData laTTPD4 = new TitleTERPPercentData();
			laTTPD4.setSalesTaxCat("NEW RESIDT");
			laTTPD4.setBegModlYrRnge(1997);
			laTTPD4.setEndModlYrRnge(9999);
			laTTPD4.setBegDieselWtRnge(14001);
			laTTPD4.setEndDieselWtRnge(100000);
			laTTPD4.setDieselIndi(1);
			laTTPD4.setTtlTERPPrcnt(new Dollar("1.5"));
			laTTPD4.setTERPPrcntAcctItmCd("SLXTXEP1");
			laTTPD4.setRTSEffDate(20030701);
			laTTPD4.setRTSEffEndDate(99991231);
			lvInput.addElement(laTTPD4);

			TitleTERPPercentData laTTPD5 = new TitleTERPPercentData();
			laTTPD5.setSalesTaxCat("SALES/USE");
			laTTPD5.setBegModlYrRnge(0);
			laTTPD5.setEndModlYrRnge(1996);
			laTTPD5.setBegDieselWtRnge(14001);
			laTTPD5.setEndDieselWtRnge(100000);
			laTTPD5.setDieselIndi(1);
			laTTPD5.setTtlTERPPrcnt(new Dollar("2.5"));
			laTTPD5.setTERPPrcntAcctItmCd("SLXTXEMI");
			laTTPD5.setRTSEffDate(20030701);
			laTTPD5.setRTSEffEndDate(99991231);
			lvInput.addElement(laTTPD5);
			
			TitleTERPPercentData laTTPD6 = new TitleTERPPercentData();
			laTTPD6.setSalesTaxCat("SALES/USE");
			laTTPD6.setBegModlYrRnge(1997);
			laTTPD6.setEndModlYrRnge(9999);
			laTTPD6.setBegDieselWtRnge(14001);
			laTTPD6.setEndDieselWtRnge(100000);
			laTTPD6.setDieselIndi(1);
			laTTPD6.setTtlTERPPrcnt(new Dollar("1.5"));
			laTTPD6.setTERPPrcntAcctItmCd("SLXTXEP1");
			laTTPD6.setRTSEffDate(20030701);
			laTTPD6.setRTSEffEndDate(99991231);
			lvInput.addElement(laTTPD6);

			TitleTERPPercentCache laTTPCache =
				new TitleTERPPercentCache();
			laTTPCache.setData(lvInput);

			TitleTERPPercentData laTTPD =
				TitleTERPPercentCache.getTERPPrcnt(
					"LEASING",
					20030815,
					1,
					2005,
					15000);
					
			System.out.println("");
					
			System.out.println(
				"A"
					+ 1
					+ ") "
					+ laTTPD.getTtlTERPPrcnt()
					+ " "
					+ laTTPD.getTERPPrcntAcctItmCd());

			Vector lvTTPD = TitleTERPPercentCache.getTtlTERPPrcntVec();
			int i = 0;
			for (Enumeration e = lvTTPD.elements();
				e.hasMoreElements();
				)
			{
				Vector lvTTPD2 = (Vector) e.nextElement();
				for (Enumeration e1 = lvTTPD2.elements();
					e1.hasMoreElements();
					)
				{
					i++;
					TitleTERPPercentData laTTPDx =
						(TitleTERPPercentData) e1.nextElement();
					System.out.println(
						"B"
							+ i
							+ ") "
							+ laTTPDx.getBegModlYrRnge()
							+ " "
							+ laTTPDx.getBegDieselWtRnge()
							+ laTTPDx.getDieselIndi()
							+ " "
							+ laTTPDx.getTtlTERPPrcnt()
							+ " "
							+ laTTPDx.getTERPPrcntAcctItmCd());
				}
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
	* @param  avTransCdsDataVector Vector
	* @throws RTSException 
	*/
	public void setData(Vector avTtlTERPPrcntDataVector)
		throws RTSException
	{
		shtTtlTERPPrcnt.clear();
		
		if (avTtlTERPPrcntDataVector == null)
		{
			return;
		}
		
		for (int i = 0; i < avTtlTERPPrcntDataVector.size(); i++)
		{
			TitleTERPPercentData laTtlTERPPrcntData =
				(TitleTERPPercentData) avTtlTERPPrcntDataVector.get(i);
				
			String lsSalesTaxCat = laTtlTERPPrcntData.getSalesTaxCat();
			
			int liDieselIndi = laTtlTERPPrcntData.getDieselIndi();
			
			String lsPrimaryKey =
				UtilityMethods.constructPrimaryKey(
					new String[] {
						lsSalesTaxCat,
						String.valueOf(liDieselIndi)});
						
			if (shtTtlTERPPrcnt.containsKey(lsPrimaryKey))
			{
				Vector lvData =
					(Vector) shtTtlTERPPrcnt.get(lsPrimaryKey);
				lvData.add(laTtlTERPPrcntData);
			}
			else
			{
				Vector lvData = new Vector();
				lvData.addElement(laTtlTERPPrcntData);
				shtTtlTERPPrcnt.put(lsPrimaryKey, lvData);
			}
		}
		return;
	}

	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from General cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable Hashtable
	*/
	public void setHashtable(java.util.Hashtable ahtHashtable)
	{
		shtTtlTERPPrcnt = ahtHashtable;
	}

	/**
	 * Returns the TitleTERPPercentData object 
	 * 
	 * @param asSalesTaxCat String
	 * @param aiEffDate int
	 * @param aiDieselIndi int
	 * @param aiModlYr int
	 * @param aiDieselWt int 
	 * @return TitleTERPPercentData
	 */
	public static TitleTERPPercentData getTERPPrcnt(
		String asSalesTaxCat,
		int aiEffDate,
		int aiDieselIndi,
		int aiModlYr,
		int aiDieselWt)
	{
		TitleTERPPercentData laReturnData = null;
		
		String lsPrimaryKey =
			UtilityMethods.constructPrimaryKey(
				new String[] {
					asSalesTaxCat,
					String.valueOf(aiDieselIndi)});
					
		if (shtTtlTERPPrcnt.containsKey(lsPrimaryKey))
		{
			Vector lvTtlTERPPrcnt =
				(Vector) shtTtlTERPPrcnt.get(lsPrimaryKey);

			for (int i = 0; i < lvTtlTERPPrcnt.size(); i++)
			{
				TitleTERPPercentData laData =
					(TitleTERPPercentData) lvTtlTERPPrcnt.get(i);
				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate())
					&& (aiModlYr >= laData.getBegModlYrRnge())
					&& (aiModlYr <= laData.getEndModlYrRnge())
					&& (aiDieselWt >= laData.getBegDieselWtRnge())
					&& (aiDieselWt <= laData.getEndDieselWtRnge()))
				{
					laReturnData = laData;
					break;
				}
			}
		}
		return laReturnData;
	}
}
