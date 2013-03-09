package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * TransactionInventoryDetailData.java 
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	11/11/2001  Added ItmCdDesc
 * C Walker		02/06/2002	Added SortingFlag
 * M Wang		06/29/2002	Updated TransactionInventoryDetailData
 *							CompareTo method. Added part of transId to 
 *							compare key	to improve sorting 
 *							(TransWsId, TransAmDate, TransTime).
 *							defect 4241
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							add ciReprntCount and associated get/set 
 * 							methods.
 * 							note: rename reprintCount to ciReprntCount 
 * 							to match standards
 * 							Ver 5.2.0
 * K Harrell	10/17/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3	
 * K Harrell 	06/14/2010 	add csTransId, get/set methods
 * 							defect 10505 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * TransactionInventoryDetailData
 *  
 * @version	6.5.0 			06/14/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/21/2001 
 */

public class TransactionInventoryDetailData
	implements Serializable, Displayable, Comparable
{
	// int
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciTransAMDate;
	protected int ciTransWsId;
	protected int ciCustSeqNo;
	protected int ciTransTime;

	protected int ciCacheTransAMDate; // SendCache
	protected int ciCacheTransTime; // SendCache
	protected int ciDelInvReasnCd;
	protected int ciDetailStatusCd;
	protected int ciInvItmReorderLvl;
	protected int ciInvItmTrckngOfc;
	protected int ciInvItmYr;
	protected int ciInvQty;
	protected int ciIssueMisMatchIndi;
	protected int ciOfcInvReorderQty;
	protected int ciReprntCount;

	// String
	protected String csDelInvReasnTxt;
	protected String csInvEndNo;
	protected String csInvId;
	protected String csInvItmNo;
	protected String csInvLocIdCd;
	protected String csItmCd;
	protected String csItmCdDesc;
	/**
	 * This is used in the compareTo method to determine how to sort 
	 * the data returned from the db for the Inquiry History Report.
	 */
	protected String csSortingFlag;
	protected String csTransCd;

	// defect 10505 
	protected String csTransId;
	// end defect 10505

	// Constants 
	private final static int LENGTH_INVID = 7;
	private final static int LENGTH_ITMCD = 8;
	private final static int LENGTH_INVITMYR = 4;
	private final static int LENGTH_INVITMNO = 10;
	private final static int LENGTH_TRANSWSID = 3;
	private final static int LENGTH_TRANSAMDATE = 5;
	private final static int LENGTH_TRANSTIME = 6;
	private final static int LENGTH_TRANSCD = 6;

	private final static long serialVersionUID = 5256165480355437832L;
	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
	 * <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
	 * all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
	 * class that implements the <tt>Comparable</tt> interface and violates
	 * this condition should clearly indicate this fact.  The recommended
	 * language is "Note: this class has a natural ordering that is
	 * inconsistent with equals."
	 * 
	 * @param   aaObject Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object aaObject)
	{
		// set up the keys for the object that is passed in
		TransactionInventoryDetailData laInvInqReportData =
			(TransactionInventoryDetailData) aaObject;
		String lsInvInqReportDataKey1 = laInvInqReportData.getInvId();
		String lsInvInqReportDataKey2 = laInvInqReportData.getItmCd();
		String lsInvInqReportDataKey3 =
			String.valueOf(laInvInqReportData.getInvItmYr());
		String lsInvInqReportDataKey4 =
			String.valueOf(laInvInqReportData.getInvItmNo());
		String lsInvInqReportDataKey5 =
			String.valueOf(laInvInqReportData.getTransWsId());
		String lsInvInqReportDataKey6 =
			String.valueOf(laInvInqReportData.getTransAMDate());
		String lsInvInqReportDataKey7 =
			String.valueOf(laInvInqReportData.getTransTime());
		String lsInvInqReportDataKey8 = laInvInqReportData.getTransCd();

		String lsKey1 = getInvId();
		String lsKey2 = getItmCd();
		String lsKey3 = String.valueOf(getInvItmYr());
		String lsKey4 = getInvItmNo();
		String lsKey5 = String.valueOf(getTransWsId());
		String lsKey6 = String.valueOf(getTransAMDate());
		String lsKey7 = String.valueOf(getTransTime());
		String lsKey8 = getTransCd();

		// Pad the itmcodes & transcds with zeros to the right of the item code.  Need to do this b/c
		// if pad to the left (like constructPrimaryKey) they are not in alphabetical order
		lsInvInqReportDataKey2 =
			UtilityMethods.addPaddingRight(
				lsInvInqReportDataKey2,
				LENGTH_ITMCD,
				CommonConstant.STR_ZERO);

		lsInvInqReportDataKey5 =
			UtilityMethods.addPaddingRight(
				lsInvInqReportDataKey5,
				LENGTH_TRANSWSID,
				CommonConstant.STR_ZERO);

		lsKey2 =
			UtilityMethods.addPaddingRight(
				lsKey2,
				LENGTH_ITMCD,
				CommonConstant.STR_ZERO);

		lsKey5 =
			UtilityMethods.addPaddingRight(
				lsKey5,
				LENGTH_TRANSWSID,
				CommonConstant.STR_ZERO);

		// set up the keys for this current object, so we can do a comparison.  Note this is not the object that was passed in.
		// it is the current object that is containing the results of the sort

		try
		{
			if (laInvInqReportData
				.getSortingFlag()
				.equals(InventoryConstant.EMP)
				|| laInvInqReportData.getSortingFlag().equals(
					InventoryConstant.WS)
				|| laInvInqReportData.getSortingFlag().equals(
					InventoryConstant.DLR)
				|| laInvInqReportData.getSortingFlag().equals(
					InventoryConstant.SUBCON))
			{
				String lsPrimaryKey =
					UtilityMethods.constructPrimaryKey(
						new String[] {
							lsInvInqReportDataKey1,
							lsInvInqReportDataKey2,
							lsInvInqReportDataKey3,
							lsInvInqReportDataKey4,
							lsInvInqReportDataKey5,
							lsInvInqReportDataKey6,
							lsInvInqReportDataKey7,
							lsInvInqReportDataKey8,
							},
						new int[] {
							LENGTH_INVID,
							LENGTH_ITMCD,
							LENGTH_INVITMYR,
							LENGTH_INVITMNO,
							LENGTH_TRANSWSID,
							LENGTH_TRANSAMDATE,
							LENGTH_TRANSTIME,
							LENGTH_TRANSCD });

				String lsThisPrimaryKey =
					UtilityMethods.constructPrimaryKey(
						new String[] {
							lsKey1,
							lsKey2,
							lsKey3,
							lsKey4,
							lsKey5,
							lsKey6,
							lsKey7,
							lsKey8 },
						new int[] {
							LENGTH_INVID,
							LENGTH_ITMCD,
							LENGTH_INVITMYR,
							LENGTH_INVITMNO,
							LENGTH_TRANSWSID,
							LENGTH_TRANSAMDATE,
							LENGTH_TRANSTIME,
							LENGTH_TRANSCD });

				return lsThisPrimaryKey.compareTo(lsPrimaryKey);

			}

			String lsPrimaryKey =
				UtilityMethods.constructPrimaryKey(
					new String[] {
						lsInvInqReportDataKey2,
						lsInvInqReportDataKey3,
						lsInvInqReportDataKey4,
						lsInvInqReportDataKey5,
						lsInvInqReportDataKey6,
						lsInvInqReportDataKey7,
						lsInvInqReportDataKey8,
						},
					new int[] {
						LENGTH_ITMCD,
						LENGTH_INVITMYR,
						LENGTH_INVITMNO,
						LENGTH_TRANSWSID,
						LENGTH_TRANSAMDATE,
						LENGTH_TRANSTIME,
						LENGTH_TRANSCD });

			String lsThisPrimaryKey =
				UtilityMethods.constructPrimaryKey(
					new String[] {
						lsKey2,
						lsKey3,
						lsKey4,
						lsKey5,
						lsKey6,
						lsKey7,
						lsKey8 },
					new int[] {
						LENGTH_ITMCD,
						LENGTH_INVITMYR,
						LENGTH_INVITMNO,
						LENGTH_TRANSWSID,
						LENGTH_TRANSAMDATE,
						LENGTH_TRANSTIME,
						LENGTH_TRANSCD });

			// set up the keys for this current object, so we can do a 
			// comparison.  Note this is not the object that was passed 
			// in.  It is the current object that is containing the 
			// results of the sort

			return lsThisPrimaryKey.compareTo(lsPrimaryKey);
		}
		catch (Exception aeEx)
		{
		}
		return 0;
	}
	/**
	 * Method used to return field attributes
	 * 
	 * @return Map
	 */
	public Map getAttributes()
	{
		HashMap lhmHash = new HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHash.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException aeIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;
	}
	/**
	 * Return the value of CacheTransAMDate 
	 * 
	 * @return int
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}
	/**
	 * Return the value of CacheTransTime
	 * 
	 * @return int
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}
	/**
	 * Returns the value of CustSeqNo
	 * 
	 * @return  int 
	 */
	public final int getCustSeqNo()
	{
		return ciCustSeqNo;
	}
	/**
	 * Returns the value of DelInvReasnCd
	 * 
	 * @return  int 
	 */
	public final int getDelInvReasnCd()
	{
		return ciDelInvReasnCd;
	}
	/**
	 * Returns the value of DelInvReasnTxt
	 * 
	 * @return  String 
	 */
	public final String getDelInvReasnTxt()
	{
		return csDelInvReasnTxt;
	}
	/**
	 * Returns the value of DetailStatusCd
	 * 
	 * @return  int 
	 */
	public final int getDetailStatusCd()
	{
		return ciDetailStatusCd;
	}
	/**
	 * Returns the value of InvEndNo
	 * 
	 * @return  String 
	 */
	public final String getInvEndNo()
	{
		return csInvEndNo;
	}
	/**
	 * Returns the value of InvId
	 * 
	 * @return  String 
	 */
	public final String getInvId()
	{
		return csInvId;
	}
	/**
	 * Returns the value of InvItmNo
	 * 
	 * @return  String 
	 */
	public final String getInvItmNo()
	{
		return csInvItmNo;
	}
	/**
	 * Returns the value of InvItmReorderLvl
	 * 
	 * @return  int 
	 */
	public final int getInvItmReorderLvl()
	{
		return ciInvItmReorderLvl;
	}
	/**
	 * Returns the value of InvItmTrckngOfc
	 * 
	 * @return  int 
	 */
	public final int getInvItmTrckngOfc()
	{
		return ciInvItmTrckngOfc;
	}
	/**
	 * Returns the value of InvItmYr
	 * 
	 * @return  int 
	 */
	public final int getInvItmYr()
	{
		return ciInvItmYr;
	}
	/**
	 * Returns the value of InvLocIdCd
	 * 
	 * @return  String 
	 */
	public final String getInvLocIdCd()
	{
		return csInvLocIdCd;
	}
	/**
	 * Returns the value of InvQty
	 * 
	 * @return  int 
	 */
	public final int getInvQty()
	{
		return ciInvQty;
	}
	/**
	 * Returns the value of IssueMisMatchIndi
	 * 
	 * @return  int 
	 */
	public final int getIssueMisMatchIndi()
	{
		return ciIssueMisMatchIndi;
	}
	/**
	 * Returns the value of ItmCd
	 * 
	 * @return  String 
	 */
	public final String getItmCd()
	{
		return csItmCd;
	}
	/**
	* Returns the value of ItmCdDesc
	* 
	* @return  String 
	*/
	public final String getItmCdDesc()
	{
		return csItmCdDesc;
	}
	/**
	 * Returns the value of OfcInvReorderQty
	 * 
	 * @return  int 
	 */
	public final int getOfcInvReorderQty()
	{
		return ciOfcInvReorderQty;
	}
	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return  int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Return the value of SortingFlag
	 * @return String
	 */
	public String getSortingFlag()
	{
		return csSortingFlag;
	}
	/**
	 * Returns the value of SubstaId
	 * 
	 * @return  int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}
	/**
	 * Returns the value of TransAMDate
	 * 
	 * @return  int 
	 */
	public final int getTransAMDate()
	{
		return ciTransAMDate;
	}
	/**
	 * Returns the value of TransCd
	 * 
	 * @return  String 
	 */
	public final String getTransCd()
	{
		return csTransCd;
	}
	/**
	* Returns the value of TransId
	* 
	* @return String 
	*/
	public String getTransId()
	{
		if (csTransId == null || csTransId.trim().length() == 0)
		{
			csTransId =
				UtilityMethods.getTransId(
					ciOfcIssuanceNo,
					ciTransWsId,
					ciTransAMDate,
					ciTransTime);
		}
		return csTransId;
	}
	/**
	 * Returns the value of TransTime
	 * 
	 * @return  int 
	 */
	public final int getTransTime()
	{
		return ciTransTime;
	}
	/**
	 * Returns the value of TransWsId
	 * 
	 * @return  int 
	 */
	public final int getTransWsId()
	{
		return ciTransWsId;
	}
	/**
	 * Return the value of CacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate int
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}
	/**
	 * Return the value of CacheTransTime
	 * 
	 * @param aiCacheTransTime int
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
	}
	/**
	 * This method sets the value of CustSeqNo.
	 * 
	 * @param aiCustSeqNo   int 
	 */
	public final void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}
	/**
	 * This method sets the value of DelInvReasnCd.
	 * 
	 * @param aiDelInvReasnCd   int 
	 */
	public final void setDelInvReasnCd(int aiDelInvReasnCd)
	{
		ciDelInvReasnCd = aiDelInvReasnCd;
	}
	/**
	 * This method sets the value of DelInvReasnTxt.
	 * 
	 * @param asDelInvReasnTxt   String 
	 */
	public final void setDelInvReasnTxt(String asDelInvReasnTxt)
	{
		csDelInvReasnTxt = asDelInvReasnTxt;
	}
	/**
	 * This method sets the value of DetailStatusCd.
	 * 
	 * @param aiDetailStatusCd   int 
	 */
	public final void setDetailStatusCd(int aiDetailStatusCd)
	{
		ciDetailStatusCd = aiDetailStatusCd;
	}
	/**
	 * This method sets the value of InvEndNo.
	 * 
	 * @param asInvEndNo   String 
	 */
	public final void setInvEndNo(String asInvEndNo)
	{
		csInvEndNo = asInvEndNo;
	}
	/**
	 * This method sets the value of InvId.
	 * 
	 * @param asInvId   String 
	 */
	public final void setInvId(String asInvId)
	{
		csInvId = asInvId;
	}
	/**
	 * This method sets the value of InvItmNo.
	 * 
	 * @param asInvItmNo   String 
	 */
	public final void setInvItmNo(String asInvItmNo)
	{
		csInvItmNo = asInvItmNo;
	}
	/**
	 * This method sets the value of InvItmReorderLvl.
	 * 
	 * @param aiInvItmReorderLvl   int 
	 */
	public final void setInvItmReorderLvl(int aiInvItmReorderLvl)
	{
		ciInvItmReorderLvl = aiInvItmReorderLvl;
	}
	/**
	 * This method sets the value of InvItmTrckngOfc.
	 * 
	 * @param aiInvItmTrckngOfc   int 
	 */
	public final void setInvItmTrckngOfc(int aiInvItmTrckngOfc)
	{
		ciInvItmTrckngOfc = aiInvItmTrckngOfc;
	}
	/**
	 * This method sets the value of InvItmYr.
	 * 
	 * @param aiInvItmYr   int 
	 */
	public final void setInvItmYr(int aiInvItmYr)
	{
		ciInvItmYr = aiInvItmYr;
	}
	/**
	 * This method sets the value of InvLocIdCd.
	 * 
	 * @param aiInvLocIdCd   String 
	 */
	public final void setInvLocIdCd(String aiInvLocIdCd)
	{
		csInvLocIdCd = aiInvLocIdCd;
	}
	/**
	 * This method sets the value of InvQty.
	 * 
	 * @param aiInvQty   int 
	 */
	public final void setInvQty(int aiInvQty)
	{
		ciInvQty = aiInvQty;
	}
	/**
	 * This method sets the value of IssueMisMatchIndi.
	 * 
	 * @param asIssueMisMatchIndi   int 
	 */
	public final void setIssueMisMatchIndi(int asIssueMisMatchIndi)
	{
		ciIssueMisMatchIndi = asIssueMisMatchIndi;
	}
	/**
	* This method sets the value of ItmCd.
	* @param asItmCd   String 
	*/
	public final void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}
	/**
	 * This method sets the value of ItmCdDesc.
	 * 
	 * @param asItmCdDesc   String 
	 */
	public final void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}
	/**
	 * This method sets the value of OfcInvReorderQty.
	 * 
	 * @param aiOfcInvReorderQty   int 
	 */
	public final void setOfcInvReorderQty(int aiOfcInvReorderQty)
	{
		ciOfcInvReorderQty = aiOfcInvReorderQty;
	}
	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo   int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Return the value of 
	 * 
	 * @param aSortingFlag String
	 */
	public void setSortingFlag(String asSortingFlag)
	{
		csSortingFlag = asSortingFlag;
	}
	/**
	 * This method sets the value of SubstaId.
	 * 
	 * @param aiSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	/**
	 * This method sets the value of TransAMDate.
	 * 
	 * @param aiTransAMDate   int 
	 */
	public final void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}
	/**
	 * This method sets the value of TransCd.
	 * 
	 * @param asTransCd   String 
	 */
	public final void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}
	/**
	 * Sets the value of TransId
	 * 
	 * @param asTransid  
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}
	/**
	 * This method sets the value of TransTime.
	 * 
	 * @param aiTransTime   int 
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}
	/**
	 * This method sets the value of TransWsId.
	 * 
	 * @param aiTransWsId   int 
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}
	/**
	 * Returns the value of ReprntCount
	 * 
	 * @return int
	 */
	public int getReprntCount()
	{
		return ciReprntCount;
	}

	/**
	 * Sets the value of ReprntCount
	 * 
	 * @param aiReprntCount
	 */
	public void setReprntCount(int aiReprntCount)
	{
		ciReprntCount = aiReprntCount;
	}

}
