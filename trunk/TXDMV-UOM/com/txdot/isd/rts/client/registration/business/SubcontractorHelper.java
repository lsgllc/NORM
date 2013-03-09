package com.txdot.isd.rts.client.registration.business;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.data.CommonFeesData;
import com.txdot.isd.rts.services.data.SubcontractorRenewalCacheData;
import com.txdot.isd.rts.services.exception.RTSException;

/*
 *
 * SubcontractorHelper.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 *							New class. Ver 5.2.0	 
 * B Hargrove	04/28/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7894 Ver 5.2.3
 * K Harrell	10/17/2007  add CMOH_REGCLASSCD,FO_REGCLASSCD,
 * 							  PLPDLR_REGCLASSCD, PLPDLRMC_REGCLASSCD	
 * 							add validateRegClassCd()
 * 							defect 9362 Ver Special Plates 2
 * ---------------------------------------------------------------
 */
/**
 * This class contains common methods that are used on client
 * and server side of subcontractor renewal
 * 
 * @version	Ver Special Plates 2	10/17/2007 
 * @author	Nancy Ting
 * <br>Creation Date:				08/15/2002 	
 */
public class SubcontractorHelper
{
	public static final String DUPL_DOC = "Duplicate Doc No: ";
	public static final String DUPL_VIN = "Duplicate VIN: ";
	//Masks
	public static final String PLT_MASK = "OOOOOOON";
	public static final String STKR_MASK = "#######9CC";
	//Error messages
	public static final String ERROR_TITLE = "ERROR!";
	public static final String ERROR_MSG1 =
		"Incorrect Field Entry. \n Please re-renter.";
	public static final String ERROR_MSG2 =
		"Please enter a valid number.";
	public static final String BAR_CODE_INV_ERROR =
		"A PROBLEM HAS OCCURRED WHILE SCANNING INVENTORY, ENTER BY KEYBOARD.";
	public static final String DIFF_CNTY =
		"Renewal Notice is not for this county.";

	// defect 9362 
	private static final int CMOH_REGCLASSCD = 9;
	private static final int FO_REGCLASSCD = 65;
	private static final int PLPDLR_REGCLASSCD = 79;
	private static final int PLPDLRMC_REGCLASSCD = 80;
	// end defect 9362 

	/**
	 * SubcontractorHelper constructor comment.
	 */
	public SubcontractorHelper()
	{
		super();
	}
	/**
	 * Check if the held plate is identical
	 * 
	 * @param aaSubconCacheData SubcontractorRenewalCacheData
	 * @return boolean 
	 */
	public static boolean checkIfHeldPltIsIdentical(SubcontractorRenewalCacheData aaSubconCacheData)
	{
		String lsHeldInvNo = null;
		String lsEnteredInvNo = null;
		if (aaSubconCacheData.getHeldInvPlt() != null)
		{
			lsHeldInvNo =
				aaSubconCacheData.getHeldInvPlt().getInvItmNo();
		}
		lsEnteredInvNo =
			aaSubconCacheData.getTempSubconRenewalData().getNewPltNo();
		if (lsEnteredInvNo != null
			&& !lsEnteredInvNo.trim().equals("")
			&& lsHeldInvNo != null
			&& !lsHeldInvNo.trim().equals(""))
		{
			if (lsEnteredInvNo.equals(lsHeldInvNo))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Insert element into hashtable and increment the count accordingly
	 * 
	 * @param ahtHashtable Hashtable
	 * @param asKey String
	 */
	public static void counterhashTablePut(
		Hashtable ahtHashtable,
		String asKey)
	{
		if (ahtHashtable.containsKey(asKey))
		{
			Integer liCount = (Integer) ahtHashtable.get(asKey);
			int liIntCount = liCount.intValue();
			++liIntCount;
			ahtHashtable.put(asKey, new Integer(liIntCount));
		}
		else
		{
			ahtHashtable.put(asKey, new Integer(1));
		}
	}
	/**
	 * Get the first set of duplicate trans numbers
	 * 
	 * @param ahtHashtable Hashtable
	 * @return Set
	 */
	public static Set getFirstDuplicateSet(Hashtable ahtHashtable)
	{
		Set laKeySet = ahtHashtable.keySet();
		Iterator liIterator = laKeySet.iterator();
		while (liIterator.hasNext())
		{
			Set laSet = (Set) ahtHashtable.get(liIterator.next());
			if (laSet.size() > 1)
			{
				return laSet;
			}
		}
		return null;
	}
	/**
	 * Insert element into hashtable and set the number count of such 
	 * element
	 * 
	 * @param ahtHashtable Hashtable
	 * @param asKey String
	 * @param aaTransNum Object 
	 */
	public static void hashTablePut(
		Hashtable ahtHashtable,
		String asKey,
		Object aaTransNum)
	{
		if (ahtHashtable.containsKey(asKey))
		{
			HashSet lhsTransNumSet = (HashSet) ahtHashtable.get(asKey);
			lhsTransNumSet.add(aaTransNum);
			ahtHashtable.put(asKey, lhsTransNumSet);
		}
		else
		{
			HashSet lhsTransNumSet = new HashSet();
			lhsTransNumSet.add(aaTransNum);
			ahtHashtable.put(asKey, lhsTransNumSet);
		}
	}
	/**
	 * Remove element from hashtable and set the number count of such 
	 * element
	 * 
	 * @param ahtHashtable Hashtable
	 * @param asKey String
	 * @param aiTransNum Integer
	 */
	public static void hashTableRemove(
		Hashtable ahtHashtable,
		String asKey,
		Integer aiTransNum)
	{
		if (ahtHashtable.containsKey(asKey))
		{
			Set laTransNumSet = (Set) ahtHashtable.get(asKey);
			if (laTransNumSet.size() == 1)
			{
				ahtHashtable.remove(asKey);
			}
			else
			{
				laTransNumSet.remove(aiTransNum);
				ahtHashtable.put(asKey, laTransNumSet);
			}
		}
	}
	/**
	 * 
	 * Validate Subcon RegClassCd
	 * 
	 * @param aiRegClassCd
	 * @param aiEffDate 
	 * @return boolean 
	 */
	public static boolean validateRegClassCd(
		int aiRegClassCd,
		int aiEffDate)
	{
		CommonFeesData laCommonFeesData =
			CommonFeesCache.getCommonFee(aiRegClassCd, aiEffDate);

		return !(
			laCommonFeesData == null
				|| laCommonFeesData.getFeeCalcCat() == 0
				|| laCommonFeesData.getRegPeriodLngth() == 0
				|| aiRegClassCd == CMOH_REGCLASSCD
				|| aiRegClassCd == FO_REGCLASSCD
				|| aiRegClassCd == PLPDLR_REGCLASSCD
				|| aiRegClassCd == PLPDLRMC_REGCLASSCD);
	}
	/**
	 * Validate duplicates for current plate
	 *
	 * @param aaSubconCache SubcontractorRenewalCacheData 
	 * @param asPlateNo String 
	 * @param abIsDiskModify boolean 
	 * @return RTSException
	 */
	public static RTSException validateDuplCurrPlt(
		SubcontractorRenewalCacheData aaSubconCache,
		String asPlateNo,
		boolean abIsDiskModify)
	{
		Hashtable lhtCurrPlt = aaSubconCache.getTransCurrPltNo();
		if (lhtCurrPlt.containsKey(asPlateNo))
		{
			if (abIsDiskModify)
			{
				Set laSet = (Set) lhtCurrPlt.get(asPlateNo);
				if (laSet.size() == 1)
				{
					if (aaSubconCache
						.getRecordModifyIndex()
						.equals(laSet.iterator().next()))
					{
						return null;
					}
					else
					{
						return new RTSException(635);
					}
				}
				else
				{
					return new RTSException(635);
				}
			}
			else
			{
				return new RTSException(635);
			}
		}
		return null;
	}
	/**
	 * Validate duplicates for doc no
	 *
	 * @param aaSubconCache SubcontractorRenewalCacheData 
	 * @param asDocNo String 
	 * @param abIsDiskModify  boolean 
	 * @return RTSException
	 */
	public static RTSException validateDuplDocNo(
		SubcontractorRenewalCacheData aaSubconCache,
		String asDocNo,
		boolean abIsDiskModify)
	{
		Hashtable lhtDocNo = aaSubconCache.getTransDocNo();
		if (lhtDocNo.containsKey(asDocNo))
		{
			if (abIsDiskModify)
			{
				Set laSet = (Set) lhtDocNo.get(asDocNo);
				if (laSet.size() == 1)
				{
					if (aaSubconCache
						.getRecordModifyIndex()
						.equals(laSet.iterator().next()))
					{
						return null;
					}
					else
					{
						return new RTSException(
							RTSException.FAILURE_MESSAGE,
							DUPL_DOC + asDocNo,
							"ERROR");
					}
				}
				else
				{
					return new RTSException(
						RTSException.FAILURE_MESSAGE,
						DUPL_DOC + asDocNo,
						"ERROR");
				}
			}
			else
			{
				return new RTSException(
					RTSException.FAILURE_MESSAGE,
					DUPL_DOC + asDocNo,
					"ERROR");
			}
		}
		return null;
	}
	/**
	 * Validate duplicates for current plate
	 *
	 * @param aaSubconCache  SubcontractorRenewalCacheData 
	 * @param asNewPlateNo String 
	 * @param abIsDiskModify boolean 
	 * @return RTSException
	 */
	public static RTSException validateDuplNewPlt(
		SubcontractorRenewalCacheData aaSubconCache,
		String asNewPlateNo,
		boolean abIsDiskModify)
	{
		Hashtable lhtCurrPlt = aaSubconCache.getTransNewPltNo();
		if (lhtCurrPlt.containsKey(asNewPlateNo))
		{
			if (abIsDiskModify)
			{
				Set laSet = (Set) lhtCurrPlt.get(asNewPlateNo);
				if (laSet.size() == 1)
				{
					if (aaSubconCache
						.getRecordModifyIndex()
						.equals(laSet.iterator().next()))
					{
						return null;
					}
					else
					{
						return new RTSException(633);
					}
				}
				else
				{
					return new RTSException(633);
				}
			}
			else
			{
				return new RTSException(633);
			}
		}
		return null;
	}
	/**
	 * Validate duplicates for VIN
	 *
	 * @param aaSubconCache SubcontractorRenewalCacheData 
	 * @param asVIN String 
	 * @param abIsDiskModify boolean 
	 * @return RTSException
	 */
	public static RTSException validateDuplVIN(
		SubcontractorRenewalCacheData aaSubconCache,
		String asVIN,
		boolean abIsDiskModify)
	{
		Hashtable lhtVIN = aaSubconCache.getTransVIN();
		if (lhtVIN.containsKey(asVIN))
		{
			if (abIsDiskModify)
			{
				Set laSet = (Set) lhtVIN.get(asVIN);
				if (laSet.size() == 1)
				{
					if (aaSubconCache
						.getRecordModifyIndex()
						.equals(laSet.iterator().next()))
					{
						return null;
					}
					else
					{
						return new RTSException(
							RTSException.FAILURE_MESSAGE,
							DUPL_VIN + asVIN,
							"ERROR");
					}
				}
				else
				{
					return new RTSException(
						RTSException.FAILURE_MESSAGE,
						DUPL_VIN + asVIN,
						"ERROR");
				}
			}
			else
			{
				return new RTSException(
					RTSException.FAILURE_MESSAGE,
					DUPL_VIN + asVIN,
					"ERROR");
			}
		}
		return null;
	}
	/**
	 * Validate duplicates for current plate
	 *
	 * @param aaSubconCache SubcontractorRenewalCacheData 
	 * @param asMo String 
	 * @return RTSException
	 */
	public static RTSException validateMo(String asMo)
	{
		int liMo = Integer.parseInt(asMo);
		if (!(liMo > 0 && liMo < 13))
		{
			return new RTSException(
				RTSException.FAILURE_MESSAGE,
				ERROR_MSG2,
				ERROR_TITLE);
		}
		return null;
	}
	/**
	 * Validate plate and sticker entry using masks
	 * 
	 * @param asMask String
	 * @param asPltStkrEntry String
	 * @return boolean 
	 */
	public static boolean validatePltStkrEntry(
		String asMask,
		String asPltStkrEntry)
	{
		if (asPltStkrEntry.length() > asMask.length())
		{
			return false;
		}
		int liMaskIndex = asMask.length() - 1;
		for (int i = asPltStkrEntry.length() - 1;
			i >= 0;
			i--, liMaskIndex--)
		{
			char lcMask = asMask.charAt(liMaskIndex);
			char lcStr = asPltStkrEntry.charAt(i);
			int liStr = (int) lcStr;
			switch (lcMask)
			{
				case ('N') :
					if (!((liStr >= 48 && liStr <= 57)
						|| (liStr >= 65 && liStr <= 90)
						|| (liStr >= 97 && liStr <= 122)))
					{
						return false;
					}
					break;
				case ('O') :
					if (!(liStr == 0
						|| (liStr >= 48 && liStr <= 57)
						|| (liStr >= 65 && liStr <= 90)
						|| (liStr >= 97 && liStr <= 122)))
					{
						return false;
					}
					break;
				case ('#') :
					if (!(liStr == 0 || (liStr >= 48 && liStr <= 57)))
					{
						return false;
					}
					break;
				case ('9') :
					if (!(liStr >= 48 && liStr <= 57))
					{
						return false;
					}
					break;
				case ('C') :
					if (!((liStr >= 65 && liStr <= 90)
						|| (liStr >= 97 && liStr <= 122)))
					{
						return false;
					}
					break;
			}
		}
		return true;
	}
}
