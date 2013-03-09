package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TaxExemptCodeData;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * TaxExemptCodes.java  
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3  		
 * K Harrell	10/10/2011	add getTaxExmptCd()
 * 							defect 11047 Ver 6.9.0 	 
 * ---------------------------------------------------------------------
 */
/**
 * The TaxExemptCodes  class provides static methods to 
 * retrieve a particular or a list of SubstationData based 
 * on different input parameters.
 *
 * <p>TaxExemptCodes  is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	6.9.0		10/10/2011
 * @author	Ashish Mahajan
 * <br>Creation Date: 	08/14/2001 15:35:33 
 */

public class TaxExemptCodeCache
	extends GeneralCache
	implements java.io.Serializable
{

	private static Hashtable shtTaxExemptCds = new Hashtable();

	private final static long serialVersionUID = 7646984956387929718L;
	
	/**
	 * TaxExmptCdCache constructor
	 */
	public TaxExemptCodeCache()
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
		return CacheConstant.TAX_EXEMPT_CODE_CACHE;
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
		return shtTaxExemptCds;
	}
	/**
	 * get vector of TaxExemptCodeData Objects
	 */
	public static Vector getTaxExmptCds()
	{
		Vector lvReturn = new Vector();

		for (Enumeration e = shtTaxExemptCds.elements();
			e.hasMoreElements();
			)
		{
			TaxExemptCodeData laData =
				(TaxExemptCodeData) e.nextElement();
			lvReturn.add(laData);

		}
		return lvReturn;

	}
	/**
	 * Clear and populate the hashtable with the vector 
	 * avTaxExemptData
	 * 
	 * @param avTaxExemptData Vector
	 */
	public void setData(Vector avTaxExemptData)
	{
		//reset data
		shtTaxExemptCds.clear();
		
		for (int i = 0; i < avTaxExemptData.size(); i++)
		{
			TaxExemptCodeData laData =
				(TaxExemptCodeData) avTaxExemptData.get(i);
				
			Integer laPrimaryKey =
				new Integer(laData.getSalesTaxExmptCd());
				
			shtTaxExemptCds.put(laPrimaryKey, laData);
		}

	}
	
	/**
	 * Return TaxExemptCodeData for TaxExempt Code
	 * 
	 * @param aiTaxExmptCd 
	 * @return TaxExemptCodeData
	 */
	public static TaxExemptCodeData getTaxExmptCd(int aiTaxExmptCd)
	{
		TaxExemptCodeData laTaxExmptCdData = null; 
		
		if (shtTaxExemptCds.containsKey(new Integer(aiTaxExmptCd)))
		{
			laTaxExmptCdData = (TaxExemptCodeData) shtTaxExemptCds.get(new Integer(aiTaxExmptCd));
		}
		return laTaxExmptCdData; 
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
		shtTaxExemptCds = ahtHashtable;
	}
}
