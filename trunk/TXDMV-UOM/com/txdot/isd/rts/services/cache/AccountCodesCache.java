package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AccountCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * AccountCodesCache.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		08/24/2001	setData() method change
 * N Ting		08/27/2001	Add comments
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3
 * K Harrell	07/01/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3
 * B Hargrove	07/11/2008	Add constant for Vendor Plate. 
 * 							add VENDOR = 'V'
 * 							defect 9689 Ver Defect MyPlates_POS
 * K Harrell	02/03/2011	add getMultiple() 
 * 							defect 10741 Ver 6.7.0 
 * B Hargrove	09/30/2011	Add constant for Obsolete. 
 * 							add OBSOLETE = 'O'
 * 							defect 10418 Ver 6.9.0
 *----------------------------------------------------------------------
 */

/**
 * The AccountCodesCache class provides static methods to 
 * retrieve a particular or a list of AccountCodesData based 
 * on different input parameters.
 *
 * <p>AccountCodesCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 *  
 * @version	6.9.0 			09/30/2011
 * @author	Marx Rajangam
 * <br>Creation Date:		08/28/2001  16:21:14  
 */

public class AccountCodesCache
	extends GeneralCache
	implements java.io.Serializable
{
	/**
	* A hashtable of vectors with acctItmCd as key
	*/
	private static Hashtable shtAcctCds = new Hashtable();

	/**
	* A flag for getAcctCds method.  
	* Gets all the data after the effective date
	*/
	public static final int GET_ALL = 0;

	/**
	* A flag for getAcctCds method.  
	* Gets all the data after the effective date where
	* procsCd equals to the input acctCdVar
	*/
	public static final int PROCSCD_EQ_CDVAR = 1;

	/**
	* A flag for getAcctCds method.  
	* Gets all the data after the effective date where
	* itmCd equals to the input acctCdVar
	*/
	public static final int ITMCD_EQ_CDVAR = 2;

	/**
	* A flag for getAcctCds method.  
	* Gets all the data after the effective date where
	* itmCd starts with "TAWPT"
	*/
	public static final int ITMCD_LIKE_CDVAR = 3;

	/**
	* A flag for getAcctCds method.
	* Not used directly by users, only used internally by getAcctCDsforRedemd
	*/
	private final static int REDEMD = 4;
	public final static String HOT_CHECK = "H";
	public final static String REFUND = "R";
	public final static String REGIONAL = "G";
	// defect 9689
	public final static String VENDOR = "V";
	// end defect 9689
	// defect 10418
	public final static String OBSOLETE = "O";
	// end defect 10418

	private final static long serialVersionUID = -3884489533974214049L;

	/**
	 * Default AccountCodesCache constructor.
	 */
	public AccountCodesCache()
	{
		super();
	}

	/**
	 * Used by getAcctCds to determined whether the particular 
	 * AccountCodesData object's date is effective as well 
	 * as redemdAcctItmCd equals to blank.
	 *
	 * @param aiEffDate int
	 * @param aaAccountCodesData AccountCodesData
	 * @return boolean 
	 */
	private static boolean elementGetAcctCdsforRedemd(
		int aiEffDate,
		AccountCodesData aaAccountCodesData)
	{

		if (((aiEffDate >= aaAccountCodesData.getRTSEffDate())
			&& (aiEffDate <= aaAccountCodesData.getRTSEffEndDate()))
			&& ((aaAccountCodesData.getRedemdAcctItmCd() == null)
				|| (aaAccountCodesData.getRedemdAcctItmCd().equals(""))))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Used by getAcctCds to determined whether the particular 
	 * AccountCodesData object's date is effective as well as acctItmCd 
	 * equals to the input asAcctCdVar.
	 *
	 * @param aiEffDate int
	 * @param aaAccountCodesData AccountCodesData
	 * @param asAcctCdVar String 
	 * @return boolean 
	 */
	private static boolean elementGetAcctItmCdEqCdVar(
		int aiEffDate,
		AccountCodesData aaAccountCodesData,
		String asAcctCdVar)
	{

		if (((aiEffDate >= aaAccountCodesData.getRTSEffDate())
			&& (aiEffDate <= aaAccountCodesData.getRTSEffEndDate()))
			&& (asAcctCdVar.equals(aaAccountCodesData.getAcctItmCd())))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Used by getAcctCds to determined whether the particular 
	 * AccountCodesData object's date is effective as well as acctItmCd 
	 * starts with "TAWPT".
	 *
	 * @param aiEffDate int
	 * @param aaAccountCodesData AccountCodesData
	 * @return boolean 
	 */
	private static boolean elementGetAcctItmCdLikeCdVar(
		int aiEffDate,
		AccountCodesData aaAccountCodesData)
	{

		if (((aiEffDate >= aaAccountCodesData.getRTSEffDate())
			&& (aiEffDate <= aaAccountCodesData.getRTSEffEndDate()))
			&& ((aaAccountCodesData.getAcctItmCd() != null)
				&& aaAccountCodesData.getAcctItmCd().startsWith("TAWPT")))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Used by getAcctCds to determined whether the particular 
	 * AccountCodesData object's date is effective.
	 *
	 * @param aiEffDate int
	 * @param aaAccountCodesData AccountCodesData
	 * @return boolean 
	 */
	private static boolean elementGetAll(
		int aiEffDate,
		AccountCodesData aaAccountCodesData)
	{

		if ((aiEffDate >= aaAccountCodesData.getRTSEffDate())
			&& (aiEffDate <= aaAccountCodesData.getRTSEffEndDate()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * Used by getAcctCds to determined whether the particular 
	 * AccountCodesData object's date is effective as well as 
	 * acctProcsCd equals the input asAcctCdVar
	 *
	 * @param aiEffDate int
	 * @param aaAccountCodesData AccountCodesData
	 * @param asAcctCdVar String 
	 * @return boolean 
	 */
	private static boolean elementGetProcscdCdEqCdVar(
		int aiEffDate,
		AccountCodesData aaAccountCodesData,
		String asAcctCdVar)
	{

		if (((aiEffDate >= aaAccountCodesData.getRTSEffDate())
			&& (aiEffDate <= aaAccountCodesData.getRTSEffEndDate()))
			&& (asAcctCdVar.equals(aaAccountCodesData.getAcctProcsCd())))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Returns the AccountCodesData object based on the input 
	 * asAcctItmCd and aiEffDate.
	 *
	 * @param asAcctItmCd String
	 * @param aiEffDate int
	 * @return AccountCodesData 
	 */
	public static AccountCodesData getAcctCd(
		String asAcctItmCd,
		int aiEffDate)
	{
		AccountCodesData laAccountCodesData = null;
		if (shtAcctCds.containsKey(asAcctItmCd))
		{
			Vector lvAcctItm = (Vector) shtAcctCds.get(asAcctItmCd);
			for (int i = 0; i < lvAcctItm.size(); i++)
			{
				AccountCodesData laData =
					(AccountCodesData) lvAcctItm.get(i);
				if ((aiEffDate >= laData.getRTSEffDate())
					&& (aiEffDate <= laData.getRTSEffEndDate()))
				{
					laAccountCodesData = laData;
					break;
				}
			}

		}
		return laAccountCodesData;
	}

	/**
	 * Gets a vector of AccountCodesData based on different query parameters.  
	 *
	 * <P>Example:<br>
	 * Vector listOfAccountCodeData = AccountCodesCache.get("001", 
	 * 20010830, AccountCodesCache.ITMCD_EQ_CDVAR);<br>
	 * In the example above, a vector of AccountCodesData objects will 
	 * be returned whose item code is 001, and has effective date range 
	 * covering 2001/08/30.
	 * 
	 * aiFlag is one of the following:
	 * <ul> 
	 * <li>GET_ALL (asAcctCdVar not required)
	 * <li>ITMCD_EQ_CDVAR 
	 * <li>ITMCD_LIKE_CDVAR (asAcctCdVar not required)
	 * <li>PROCSCD_EQ_CDVAR
	 * <eul>
	 * 
	 * asAcctCdVar is either the item code or the process code 
	 *
	 * @param asAcctCdVar String
	 * @param aiEffDate int 
	 * @param aiFlag int
	 * @return Vector 
	 * @throws RTSException
	 */
	public static Vector getAcctCds(
		String asAcctCdVar,
		int aiEffDate,
		int aiFlag)
		throws RTSException
	{
		Vector lvAccountCodesData = new Vector();

		for (Enumeration laE1 = shtAcctCds.elements();
			laE1.hasMoreElements();
			)
		{
			Vector lvAcctItmRow = (Vector) laE1.nextElement();
			for (int i = 0; i < lvAcctItmRow.size(); i++)
			{
				AccountCodesData laAccountCodesData =
					(AccountCodesData) lvAcctItmRow.get(i);
				boolean lbAddElement = false;
				switch (aiFlag)
				{
					case GET_ALL :
						lbAddElement =
							elementGetAll(
								aiEffDate,
								laAccountCodesData);
						break;
					case PROCSCD_EQ_CDVAR :
						lbAddElement =
							elementGetProcscdCdEqCdVar(
								aiEffDate,
								laAccountCodesData,
								asAcctCdVar);
						break;
					case ITMCD_EQ_CDVAR :
						lbAddElement =
							elementGetAcctItmCdEqCdVar(
								aiEffDate,
								laAccountCodesData,
								asAcctCdVar);
						break;
					case ITMCD_LIKE_CDVAR :
						lbAddElement =
							elementGetAcctItmCdLikeCdVar(
								aiEffDate,
								laAccountCodesData);
						break;
					case REDEMD :
						lbAddElement =
							elementGetAcctCdsforRedemd(
								aiEffDate,
								laAccountCodesData);
						break;
					default :
						throw new RTSException(
							RTSException.JAVA_ERROR,
							new Exception(
								"AccountCodesCache->getAcctCds: aiflag "
									+ aiFlag
									+ " does not exist"));
				}

				if (lbAddElement)
				{
					lvAccountCodesData.addElement(laAccountCodesData);
				}
			}
		}
		if (lvAccountCodesData.size() == 0)
		{
			return null;
		}
		else
		{
			return lvAccountCodesData;
		}
	}

	/**
	 * Returns a vector of AccountCodesData objects whose redemdAcctItmCd
	 *  is blank or null and the date is effective.
	 *
	 * @param aiEffDate int
	 * @return Vector 
	 * @throws RTSException
	 */
	public static Vector getAcctCdsforRedemd(int aiEffDate)
		throws RTSException
	{
		return getAcctCds("", aiEffDate, REDEMD);
	}

	/**
	 * Return the CacheConstant for this cache type
	 * 
	 * @return int one of the constants defined in CacheConstant.
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.ACCOUNT_CODES_CACHE;
	}
	/**
	 * Get the internally stored Hashtable.
	 *
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtAcctCds;
	}
	
	/**
	 * Get Multiple of Base Fee
	 * 
	 * @param asAcctItmCd
	 * @param adFee 
	 * @return double 
	 */
	public static double getMultiple(
		String asAcctItmCd,
		double adFee)
	{
		double  ldMultiple = -1; 
		
		AccountCodesData laData =
			getAcctCd(asAcctItmCd, new RTSDate().getYYYYMMDDDate());

		if (laData.getBaseFee().compareTo(new Dollar(0)) != 0)
		{
			double ldBase =
				new Double(laData.getBaseFee().toString())
					.doubleValue();
			if (adFee % ldBase == 0)
			{
				ldMultiple = adFee/ldBase; 	
			}
		}
		return ldMultiple;
	}

	/**
	 * Test main
	 * 
	 * @param args String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			Vector lvInputVec = new Vector();

			AccountCodesData laD1 = new AccountCodesData();
			laD1.setAcctItmCd("001");
			laD1.setAcctItmCdDesc("001 desc");
			laD1.setAcctProcsCd("11");
			laD1.setRedemdAcctItmCd("111");
			laD1.setRTSEffDate(20010101);
			laD1.setRTSEffEndDate(20010328);
			lvInputVec.addElement(laD1);

			AccountCodesData laD2 = new AccountCodesData();
			laD2.setAcctItmCd("002");
			laD2.setAcctItmCdDesc("002 desc");
			laD2.setAcctProcsCd("221");
			laD2.setRedemdAcctItmCd("");
			laD2.setRTSEffDate(20010200);
			laD2.setRTSEffEndDate(20010922);
			lvInputVec.addElement(laD2);

			AccountCodesData laD4 = new AccountCodesData();
			laD4.setAcctItmCd("001");
			laD4.setAcctItmCdDesc("004 desc");
			laD4.setAcctProcsCd("221");
			laD4.setRedemdAcctItmCd("111");
			laD4.setRTSEffDate(20010200);
			laD4.setRTSEffEndDate(20010922);
			lvInputVec.addElement(laD4);

			AccountCodesData laD3 = new AccountCodesData();
			laD3.setAcctItmCd("003");
			laD3.setAcctItmCdDesc("003 desc");
			laD3.setAcctProcsCd("11");
			laD3.setRedemdAcctItmCd("");
			laD3.setRTSEffDate(20010201);
			laD3.setRTSEffEndDate(20010401);
			lvInputVec.addElement(laD3);

			AccountCodesCache laCache = new AccountCodesCache();
			laCache.setData(lvInputVec);

			AccountCodesData laData =
				AccountCodesCache.getAcctCd("001", 20010128);
			laData.getAcctItmCdDesc();
			//Vector dataVec = AccountCodesCache.getAcctCdsforRedemd(20010222);
			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}
	/**
	 * Clear and populate the hashtable with the vector
	 *  
	 * @param avAccountCodeData Vector
	 */
	public void setData(Vector avAccountCodeData)
	{
		//reset data
		shtAcctCds.clear();
		for (int i = 0; i < avAccountCodeData.size(); i++)
		{
			AccountCodesData laAccountCodesData =
				(AccountCodesData) avAccountCodeData.get(i);
			String lsPrimaryKey = laAccountCodesData.getAcctItmCd();
			if (shtAcctCds.containsKey(lsPrimaryKey))
			{
				Vector lvAccountCodes =
					(Vector) shtAcctCds.get(lsPrimaryKey);
				lvAccountCodes.add(laAccountCodesData);
			}
			else
			{
				Vector lvAccountCodes = new Vector();
				lvAccountCodes.addElement(laAccountCodesData);
				shtAcctCds.put(lsPrimaryKey, lvAccountCodes);
			}
		}
	}
	/**
	 * Set the internally stored Hashtable.
	 * 
	 * @param ahtHashtable Hashtable
	 */
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtAcctCds = ahtHashtable;
	}
}
