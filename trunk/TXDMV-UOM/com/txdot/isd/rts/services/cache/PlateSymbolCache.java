package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.PlateSymbolData;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * PlateSymbolCache.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2010	Created
 * 							defect 10366 Ver POS_640
 * K Harrell	09/14/2010  add ISASYMBOL
 * 							add getPlateSymbolsNoISA(), isPlateSymbol()
 * 							modify getPlateSymbols() 
 * 							defect 10571 Ver 6.6.0  
 * ---------------------------------------------------------------------
 */

/**
 * The PlateSymbolCache class provides static methods to 
 * retrieve a PlateSymbolData object/objects.
 *
 * <p>PlateSymbolCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	6.6.0 		09/14/2010
 * @author	Kathy Harrell
 * <br>Creation Date:	03/24/2010
 */
public class PlateSymbolCache
	extends GeneralCache
	implements java.io.Serializable
{
	// defect 10571 
	public static final String ISASYMBOL = "%";
	// end defect 10571 

	// long 
	static final long serialVersionUID = 6258675439916370101L;
	
	/**
	* A hashtable of Objects with PltSymCd as key
	*/
	private static Hashtable shtPlateSymbol = new Hashtable();
	
	/** 
	 * Return Plate Symbol Date for given Plate Symbol Code
	 * 
	 * @param asPltSymCd
	 * @return PlateSymbolData 
	 */
	public static PlateSymbolData getPlateSymbolData(String asPltSymCd)
	{
		PlateSymbolData laPltSymData = null;

		if (asPltSymCd != null)
		{
			laPltSymData =
				(PlateSymbolData) shtPlateSymbol.get(asPltSymCd.trim());
		}
		return laPltSymData;
	}

	/**
	 * Return Vector of Plate Symbols Data  
	 * 
	 * @return Vector 
	 */
	public static Vector getPlateSymbols()
	{
		// defect 10571
		// Return sorted Vector 
		Vector lvReturn = new Vector(shtPlateSymbol.values());
		UtilityMethods.sort(new Vector(shtPlateSymbol.values()));
		return lvReturn;
		// end defect 10571 
	}

	/** 
	 * Return Vector of all Plate Symbols
	 * 
	 * @return Vector 
	 */
	public static Vector getPlateSymbolsNoISA()
	{
		Vector lvReturn = new Vector();
		Vector lvAllSymbols = getPlateSymbols();

		for (int i = 0; i < lvAllSymbols.size(); i++)
		{
			PlateSymbolData laData =
				(PlateSymbolData) lvAllSymbols.elementAt(i);
			if (!laData.getPltSymCd().equals(ISASYMBOL))
			{
				lvReturn.add(laData);
			}
		}
		return lvReturn;
	}

	/**
	 * Return boolean to denote if PlateSymbol 
	 * 
	 * @param asPltSym
	 * @return boolean 
	 */
	public static boolean isPlateSymbol(String asPltSym)
	{
		return shtPlateSymbol != null
			&& shtPlateSymbol.containsKey(asPltSym);
	}

	/**
	 * PlateSymbolCache constructor comment.
	 */
	public PlateSymbolCache()
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
		return CacheConstant.PLATE_SYMBOL_CACHE;
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
		return shtPlateSymbol;
	}

	/**
	 * Clear and populate the hashtable with the vector 
	 * PlateSymbolData
	 * 
	 * @param avData Vector 
	 */
	public void setData(Vector avData)
	{
		//reset data
		shtPlateSymbol.clear();
		for (int i = 0; i < avData.size(); i++)
		{
			PlateSymbolData laData = (PlateSymbolData) avData.get(i);

			shtPlateSymbol.put(laData.getPltSymCd(), laData);
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
		shtPlateSymbol = ahtHashtable;
	}
}
