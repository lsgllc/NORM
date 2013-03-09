package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.OwnershipEvidenceCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * OwnershipEvidenceCodesCache.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		09/06/2001	Convert to Hungarian notation.
 * K Harrell	06/17/2005	Java 1.4 Cleanup
 * 							defect 7899 Ver 5.2.3	
 * K Harrell	06/20/2008	add method to only retrieve row for one
 * 							OwnrEvidCd 
 * 							add getOwnrEvidCd()
 * 							defect 9724 Ver Defect POS A
 * K Harrell	11/20/2011	add getOwnrEvidCd() 
 * 							defect 11052 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */
/**
 * OwnershipEvidenceCodesCache class extends GeneralCache.
 * 
 * @version 6.9.0  		11/20/2011
 * @author	Joseph Kwik
 * <br>Creation Date:	08/02/2001 10:06:39 
 */

public class OwnershipEvidenceCodesCache extends GeneralCache
{

	/**
	* Sort by constants used in argument of getOwnrEvidCds() method.
	*/
	public static final int SORT_BY_EVIDFREQSORTNO = 0;
	public static final int SORT_BY_EVIDIMPRTNCDSORTNO = 1;

	/**
	* Filter by constants used in argument of getOwnrEvidCdsByEvidSurrCd()
	* method.
	*/
	public static final int GET_B_AND_T = 0;
	public static final int GET_B_AND_J = 1;

	private static Hashtable shtOwnrEvidCds = new Hashtable();

	private final static long serialVersionUID = -668920588801658427L;

	/**
	 * OwnershipEvidenceCodesCache constructor comment.  Calls super();
	 */
	public OwnershipEvidenceCodesCache()
	{
		super();
	}
	/**
	 * Implements the GeneralCache.getCacheFunctionId() abstract method.
	 * Gets the CacheConstant.OWNER_EVIDENCE_CODES_CACHE.
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.OWNER_EVIDENCE_CODES_CACHE;
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
		return shtOwnrEvidCds;
	}

	/**
	 * Return OwnershipEvidenceCodesData for the associated aiOwnrEvidCd
	 * 
	 * Returns null if no object exists.
	 * 
	 * @param aiOwnrEvidCd
	 * @return String  
	 */
	public static OwnershipEvidenceCodesData getOwnrEvidCd (int aiOwnrEvidCd)
	{
		OwnershipEvidenceCodesData laReturnData = null; 
		
		Object laData =
			shtOwnrEvidCds.get(new Integer(aiOwnrEvidCd));

		if (laData != null)
		{
			laReturnData =
				(OwnershipEvidenceCodesData) laData;
		}
		return laReturnData;
	}

	/**
	 * Gets the owner evidence codes data object vector using
	 * SORT_BY_EVIDFREQSORTNO or SORT_BY_EVIDIMPRTNCDSORTNO for the 
	 * argument.
	 * 
	 * Returns null if no objects exist.
	 * 
	 * @param aiSortBy
	 * @return Vector  
	 */
	public static Vector getOwnrEvidCds(int aiSortBy)
		throws RTSException
	{
		if (aiSortBy != SORT_BY_EVIDFREQSORTNO
			&& aiSortBy != SORT_BY_EVIDIMPRTNCDSORTNO)
		{
			throw new RTSException(
				RTSException.JAVA_ERROR,
				new Exception(
					"OwnershipEvidenceCodesCache->getOwnrEvidCds:  "
						+ aiSortBy
						+ " does not exist"));
		}

		Vector lvReturn = new Vector();

		Enumeration e = shtOwnrEvidCds.elements();
		while (e.hasMoreElements())
		{
			OwnershipEvidenceCodesData data =
				(OwnershipEvidenceCodesData) e.nextElement();
			lvReturn.addElement(data);
		}

		insertSort(lvReturn, aiSortBy);

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
	 * Return TtlTrnsfrPnltyExmptCd for the associated aiOwnrEvidCd
	 * 
	 * Returns Empty String if no objects exist.
	 * 
	 * @param aiOwnrEvidCd
	 * @return String  
	 */
	public static String getTtlTrnsfrPnltyExmptCd(int aiOwnrEvidCd)
	{
		String lsExemptCd = CommonConstant.STR_SPACE_EMPTY;

		Object laReturnData =
			shtOwnrEvidCds.get(new Integer(aiOwnrEvidCd));

		if (laReturnData != null)
		{
			OwnershipEvidenceCodesData laOwnrEvidData =
				(OwnershipEvidenceCodesData) laReturnData;
			lsExemptCd = laOwnrEvidData.getTtlTrnsfrPnltyExmptCd();
		}
		return lsExemptCd;
	}

	/**
	 * Gets the owner evidence codes data object vector using GET_B_AND_J 
	 * or GET_B_AND_T for the argument.
	 * Sorts the resulting vector by evidFreqSortNo attribute's values. 
	 * Returns null if no objects exist.
	 * 
	 * @param  aiFilterBy int 
	 * @return Vector 
	 * @throws RTSException
	 */
	public static Vector getOwnrEvidCdsByEvidSurrCd(int aiFilterBy)
		throws RTSException
	{
		if (aiFilterBy != GET_B_AND_J && aiFilterBy != GET_B_AND_T)
		{
			throw new RTSException(
				RTSException.JAVA_ERROR,
				new Exception(
					"OwnershipEvidenceCodesCache->getOwnrEvidCdsByEvidSurrCd:  "
						+ aiFilterBy
						+ " does not exist"));
		}

		Vector lvReturn = new Vector();
		Enumeration e = shtOwnrEvidCds.elements();
		if (aiFilterBy == GET_B_AND_T)
		{
			while (e.hasMoreElements())
			{
				OwnershipEvidenceCodesData laData =
					(OwnershipEvidenceCodesData) e.nextElement();
				if (laData.getEvidSurrCd().equals("B")
					|| laData.getEvidSurrCd().equals("T"))
				{
					lvReturn.addElement(laData);
				}
			}
		}
		else if (aiFilterBy == GET_B_AND_J)
		{
			while (e.hasMoreElements())
			{
				OwnershipEvidenceCodesData laData =
					(OwnershipEvidenceCodesData) e.nextElement();
				if (laData.getEvidSurrCd().equals("B")
					|| laData.getEvidSurrCd().equals("J"))
				{
					lvReturn.addElement(laData);
				}
			}
		}

		insertSort(lvReturn, SORT_BY_EVIDFREQSORTNO);

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
	 * Sorts the incoming vector based upon the specified attribute.
	 * 
	 * @param avData Vector
	 * @param aiSortBy int 
	 */
	private static void insertSort(Vector avData, int aiSortBy)
	{
		// SORT_BY_EVIDIMPRTNCDSORTNO = 0;
		// SORT_BY_EVIDFREQSORTNO = 1;

		int k, loc;
		int i;
		int left = 0;
		int right = avData.size() - 1;

		if (aiSortBy == SORT_BY_EVIDIMPRTNCDSORTNO)
		{
			for (k = left + 1; k <= right; k++)
			{
				OwnershipEvidenceCodesData laData =
					(OwnershipEvidenceCodesData) avData.get(k);
				loc = k;

				i = laData.getEvidImprtnceSortNo();
				while (left < loc
					&& i
						< ((OwnershipEvidenceCodesData) avData
							.get(loc - 1))
							.getEvidImprtnceSortNo())
				{
					avData.set(loc, avData.get(loc - 1));
					loc--;
				}
				avData.set(loc, laData);
			}
		}
		else if (aiSortBy == SORT_BY_EVIDFREQSORTNO)
		{

			for (k = left + 1; k <= right; k++)
			{
				OwnershipEvidenceCodesData laData =
					(OwnershipEvidenceCodesData) avData.get(k);
				loc = k;

				i = laData.getEvidFreqSortNo();
				while (left < loc
					&& i
						< ((OwnershipEvidenceCodesData) avData
							.get(loc - 1))
							.getEvidFreqSortNo())
				{
					avData.set(loc, avData.get(loc - 1));
					loc--;
				}
				avData.set(loc, laData);
			}
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

			OwnershipEvidenceCodesData laData1 =
				new OwnershipEvidenceCodesData();
			laData1.setOwnrshpEvidCd(100);
			laData1.setEvidImprtnceSortNo(10);
			laData1.setEvidFreqSortNo(11);
			laData1.setEvidSurrCd("B");
			lvData.addElement(laData1);

			OwnershipEvidenceCodesData laData2 =
				new OwnershipEvidenceCodesData();
			laData2.setOwnrshpEvidCd(101);
			laData2.setEvidImprtnceSortNo(1);
			laData2.setEvidFreqSortNo(9);
			laData2.setEvidSurrCd("T");
			lvData.addElement(laData2);

			OwnershipEvidenceCodesData laData3 =
				new OwnershipEvidenceCodesData();
			laData3.setOwnrshpEvidCd(102);
			laData3.setEvidImprtnceSortNo(5);
			laData3.setEvidFreqSortNo(13);
			laData3.setEvidSurrCd("B");
			lvData.addElement(laData3);

			OwnershipEvidenceCodesData laData4 =
				new OwnershipEvidenceCodesData();
			laData4.setOwnrshpEvidCd(103);
			laData4.setEvidImprtnceSortNo(2);
			laData4.setEvidFreqSortNo(1);
			laData4.setEvidSurrCd("J");
			lvData.addElement(laData4);

			OwnershipEvidenceCodesCache laCache =
				new OwnershipEvidenceCodesCache();
			laCache.setData(lvData);

			lvData =
				OwnershipEvidenceCodesCache.getOwnrEvidCds(
					OwnershipEvidenceCodesCache
						.SORT_BY_EVIDIMPRTNCDSORTNO);

			for (int i = 0; i < lvData.size(); i++)
			{

				OwnershipEvidenceCodesData laData =
					(OwnershipEvidenceCodesData) lvData.get(i);

				System.out.println(laData.getEvidImprtnceSortNo());
			}

			lvData =
				OwnershipEvidenceCodesCache.getOwnrEvidCds(
					OwnershipEvidenceCodesCache.SORT_BY_EVIDFREQSORTNO);

			for (int i = 0; i < lvData.size(); i++)
			{

				OwnershipEvidenceCodesData laData =
					(OwnershipEvidenceCodesData) lvData.get(i);

				System.out.println(laData.getEvidFreqSortNo());
			}

			lvData =
				OwnershipEvidenceCodesCache.getOwnrEvidCdsByEvidSurrCd(
					OwnershipEvidenceCodesCache.GET_B_AND_J);

			System.out.println(
				"\n*** test getOwnrEvidCdsByEvidSurr(GET_B_AND_J)");
			for (int i = 0; i < lvData.size(); i++)
			{

				OwnershipEvidenceCodesData laData =
					(OwnershipEvidenceCodesData) lvData.get(i);

				System.out.println(
					laData.getEvidFreqSortNo()
						+ " evidSurrCd: "
						+ laData.getEvidSurrCd());
			}

			lvData =
				OwnershipEvidenceCodesCache.getOwnrEvidCdsByEvidSurrCd(
					OwnershipEvidenceCodesCache.GET_B_AND_T);

			System.out.println(
				"\n*** test getOwnrEvidCdsByEvidSurr(GET_B_AND_T)");
			for (int i = 0; i < lvData.size(); i++)
			{

				OwnershipEvidenceCodesData laData =
					(OwnershipEvidenceCodesData) lvData.get(i);

				System.out.println(
					laData.getEvidFreqSortNo()
						+ " evidSurrCd: "
						+ laData.getEvidSurrCd());
			}

			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	 * Implements the GeneralCache.setData() abstract method.
	 * Populates OwnershipEvidenceCodesData vector into a hashtable.
	 * 
	 * @param avOwnershipEvidenceCodesData Vector
	 */
	public void setData(Vector avOwnershipEvidenceCodesData)
	{
		//reset the hashtable
		shtOwnrEvidCds.clear();
		for (int i = 0; i < avOwnershipEvidenceCodesData.size(); i++)
		{
			OwnershipEvidenceCodesData laData =
				(
					OwnershipEvidenceCodesData) avOwnershipEvidenceCodesData
						.get(
					i);
			shtOwnrEvidCds.put(
				new Integer(laData.getOwnrshpEvidCd()),
				laData);
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
		shtOwnrEvidCds = ahtHashtable;
	}
}
