package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TransactionData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K. Harrell   10/17/2001  Changed double to Dollar
 * K. Harrell   10/18/2001  Added columns for SendTrans
 * K Harrell	04/19/2004	Added ciRprStkOfcIssuanceNo,
 *							ciRprStkTransWsId, ciRprStkTransAMDate,
 *							ciRprStkTransTime & associated get/set
 *							methods. 
 *							defect 7018 Ver 5.2.0
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell	06/09/2010	add ciAddPrmtRecIndi, ciDelPrmtRecIndi, 
 * 							 get/set methods
 * 							defect 10491 Ver 6.5.0 
 * K Harrell 	06/14/2010 	add csTransId, csCustNoKey, get/set methods
 * 							defect 10505 Ver 6.5.0 
 * K Harrell	07/21/2010	add csVoidTransId, get/set methods
 * 							defect 10505 Ver 6.5.0 
 * K Harrell	09/29/2010	modify setCustName1()
 * 							defect 10616 Ver 6.5.0 
 * K Harrell	06/30/2012	add implements Comparable
 * 							add compareTo()
 * 							defect 11073 Ver 7.0.0
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * TransactionData. 
 *
 * @version	7.0.0			06/30/2012
 * @author	Kathy Harrell
 * <br> Creation Date:	
 */

public class TransactionData implements Serializable, Displayable
// defect 11073 
, Comparable
// end defect 11073 
{
	// int 
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciTransAMDate;
	protected int ciTransWsId;
	protected int ciCustSeqNo;
	protected int ciTransTime;

	protected int ciBranchOfcId; // SendTrans
	protected int ciCashWsId; // SendTrans
	protected int ciIncomplTransIndi; // SendTrans
	protected int ciOfcIssuanceCd; // SendTrans
	protected int ciTransPostedLANIndi; // SendTrans
	protected int ciVersionCd; // SendTrans

	protected int ciBsnDate;
	protected int ciCacheTransAMDate; // SendCache
	protected int ciCacheTransTime; // SendCache   
	protected int ciDieselIndi;
	protected int ciEffDate;
	protected int ciEffTime;
	protected int ciExpDate;
	protected int ciExpTime;
	protected int ciProcsdByMailIndi;
	protected int ciRegClassCd;
	protected int ciRegPnltyChrgIndi;
	protected int ciRprStkOfcIssuanceNo;
	protected int ciRprStkTransAMDate;
	protected int ciRprStkTransTime;
	protected int ciRprStkTransWsId;
	protected int ciTransPostedMfIndi;
	protected int ciVehCaryngCap;
	protected int ciVehEmptyWt;
	protected int ciVehGrossWt;
	protected int ciVehModlYr;
	protected int ciVoidedTransIndi;
	protected int ciVoidOfcIssuanceNo;
	protected int ciVoidTransAMDate;
	protected int ciVoidTransTime;
	protected int ciVoidTransWsId;

	// Object 
	protected Dollar caBsnDateTotalAmt;

	// String 
	protected String csCustName1;
	protected String csCustName2;
	protected String csCustLstName; //Not Used
	protected String csCustFstName; //Not Used 
	protected String csCustMIName; //Not Used 
	protected String csCustSt1;
	protected String csCustSt2;
	protected String csCustCity;
	protected String csCustState;
	protected String csCustZpCd;
	protected String csCustZpCdP4;
	protected String csCustCntry;
	protected String csDLSCertNo;
	protected String csDocNo;
	protected String csOwnrId;
	protected String csRegPltNo;
	protected String csRegStkrNo;
	protected String csTireTypeCd;
	protected String csTransCd;
	protected String csTransEmpId;
	protected String csVehBdyType;
	protected String csVehMk;
	protected String csVehRegState;
	protected String csVehUnitNo;
	protected String csVIN;
	protected String csVoidTransCd;

	// defect 10491 
	protected int ciAddPrmtRecIndi;
	protected int ciDelPrmtRecIndi;
	// end defect 10491 

	// defect 10505 
	protected String csTransId;
	protected String csCustNoKey;
	protected String csVoidTransId;
	// end defect 10505

	private final static long serialVersionUID = -3551815408237316549L;

	/**
	 * Transaction constructor comment.
	 *
	 */
	public TransactionData()
	{
		super();
	}
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
	 * @param   aaObject Object
	 * @return  int
	 */
	public int compareTo(Object aaObject)
	{
		return (
			getCompareDateTime().compareTo(
				((TransactionData) aaObject).getCompareDateTime()));
	}
	
	/**
	 * Gets value of ciAddPrmtRecIndi
	 * 
	 * @return int
	 */
	public int getAddPrmtRecIndi()
	{
		return ciAddPrmtRecIndi;
	}

	/**
	 * Method used to return field attributes. 
	 * 
	 * @return Map
	 */
	public Map getAttributes()
	{
		HashMap lhmHashMap = new HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHashMap.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException aeIllAccEx)
			{
				continue;
			}
		}
		return lhmHashMap;
	}
	/**
	 * Return value of BranchOfcId
	 * 
	 * @return int
	 */
	public int getBranchOfcId()
	{
		return ciBranchOfcId;
	}
	/**
	 * Returns the value of BsnDate
	 * 
	 * @return  int 
	 */
	public final int getBsnDate()
	{
		return ciBsnDate;
	}
	/**
	 * Returns the value of BsnDateTotalAmt
	 * 
	 * @return  Dollar 
	 */
	public final Dollar getBsnDateTotalAmt()
	{
		return caBsnDateTotalAmt;
	}
	/**
	 * Return value of CacheTransAMDate
	 * 
	 * @return int
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}
	/**
	 * Return value of CacheTransTime
	 * 
	 * @return int
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}
	/**
	 * Return value of CashWsId
	 * 
	 * @return int
	 */
	public int getCashWsId()
	{
		return ciCashWsId;
	}
	/**
	 * Return Double of TransAMDate * 1000000.0 + getCentralTime());
	 * 
	 * @return Double  
	 */
	private Double getCompareDateTime()
	{
		return new Double(getTransAMDate()* 1000000.0 + getTransTime());
	}
	
	/**
	 * Returns the value of CustCity
	 * 
	 * @return  String 
	 */
	public final String getCustCity()
	{
		return csCustCity;
	}
	/**
	 * Returns the value of CustCntry
	 * 
	 * @return  String 
	 */
	public final String getCustCntry()
	{
		return csCustCntry;
	}
	/**
	 * Returns the value of CustFstName
	 * 
	 * @return  String 
	 */
	public final String getCustFstName()
	{
		return csCustFstName;
	}
	/**
	 * Returns the value of CustLstName
	 * 
	 * @return  String 
	 */
	public final String getCustLstName()
	{
		return csCustLstName;
	}
	/**
	 * Returns the value of CustMIName
	 * 
	 * @return  String 
	 */
	public final String getCustMIName()
	{
		return csCustMIName;
	}
	/**
	 * Returns the value of CustName1
	 * 
	 * @return  String 
	 */
	public final String getCustName1()
	{
		return csCustName1;
	}
	/**
	 * Returns the value of CustName2
	 * 
	 * @return  String 
	 */
	public final String getCustName2()
	{
		return csCustName2;
	}

	/**
	 * Returns the value of CustNoKey
	 * 
	 * @return String 
	 */
	public String getCustNoKey()
	{
		if (csCustNoKey == null || csCustNoKey.trim().length() == 0)
		{
			csCustNoKey =
				UtilityMethods.addPadding(
					new String[] {
						String.valueOf(ciOfcIssuanceNo),
						String.valueOf(ciTransWsId),
						String.valueOf(ciTransAMDate),
						String.valueOf(ciCustSeqNo)},
					new int[] {
						CommonConstant.LENGTH_OFFICE_ISSUANCENO,
						CommonConstant.LENGTH_TRANS_WSID,
						CommonConstant.LENGTH_TRANSAMDATE,
						CommonConstant.LENGTH_CUSTSEQNO },
					CommonConstant.STR_ZERO);

		}
		return csCustNoKey;
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
	 * Returns the value of CustSt1
	 * 
	 * @return  String 
	 */
	public final String getCustSt1()
	{
		return csCustSt1;
	}
	/**
	 * Returns the value of CustSt2
	 * 
	 * @return  String 
	 */
	public final String getCustSt2()
	{
		return csCustSt2;
	}
	/**
	 * Returns the value of CustState
	 * 
	 * @return  String 
	 */
	public final String getCustState()
	{
		return csCustState;
	}
	/**
	 * Returns the value of CustZpCd
	 * 
	 * @return  String 
	 */
	public final String getCustZpCd()
	{
		return csCustZpCd;
	}
	/**
	 * Returns the value of CustZpCdP4
	 * 
	 * @return  String 
	 */
	public final String getCustZpCdP4()
	{
		return csCustZpCdP4;
	}

	/**
	 * Gets value of ciDelPrmtRecIndi
	 * 
	 * @return int
	 */
	public int getDelPrmtRecIndi()
	{
		return ciDelPrmtRecIndi;
	}

	/**
	 * Returns the value of DieselIndi
	 * 
	 * @return  int 
	 */
	public final int getDieselIndi()
	{
		return ciDieselIndi;
	}
	/**
	 * Returns the value of DLSCertNo
	 * 
	 * @return  String 
	 */
	public final String getDLSCertNo()
	{
		return csDLSCertNo;
	}
	/**
	 * Returns the value of DocNo
	 * 
	 * @return  String 
	 */
	public final String getDocNo()
	{
		return csDocNo;
	}
	/**
	 * Returns the value of EffDate
	 * 
	 * @return  int 
	 */
	public final int getEffDate()
	{
		return ciEffDate;
	}
	/**
	 * Returns the value of EffTime
	 * 
	 * @return  int 
	 */
	public final int getEffTime()
	{
		return ciEffTime;
	}
	/**
	 * Returns the value of ExpDate
	 * 
	 * @return  int 
	 */
	public final int getExpDate()
	{
		return ciExpDate;
	}
	/**
	 * Returns the value of ExpTime
	 * 
	 * @return  int 
	 */
	public final int getExpTime()
	{
		return ciExpTime;
	}
	/**
	 * Return value of IncomplTransIndi
	 * @return int
	 */
	public int getIncomplTransIndi()
	{
		return ciIncomplTransIndi;
	}
	/**
	 * Return value of OfcIssuanceCd
	 * 
	 * @return int
	 */
	public int getOfcIssuanceCd()
	{
		return ciOfcIssuanceCd;
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
	 * Returns the value of OwnrId
	 * 
	 * @return  String 
	 */
	public final String getOwnrId()
	{
		return csOwnrId;
	}
	/**
	 * Returns the value of ProcsdByMailIndi
	 * 
	 * @return  int 
	 */
	public final int getProcsdByMailIndi()
	{
		return ciProcsdByMailIndi;
	}
	/**
	 * Returns the value of RegClassCd
	 * 
	 * @return  int 
	 */
	public final int getRegClassCd()
	{
		return ciRegClassCd;
	}
	/**
	 * Returns the value of RegPltNo
	 * 
	 * @return  String 
	 */
	public final String getRegPltNo()
	{
		return csRegPltNo;
	}
	/**
	 * Returns the value of RegPnltyChrgIndi
	 * 
	 * @return  int 
	 */
	public final int getRegPnltyChrgIndi()
	{
		return ciRegPnltyChrgIndi;
	}
	/**
	 * Returns the value of RegStkrNo
	 * 
	 * @return  String 
	 */
	public final String getRegStkrNo()
	{
		return csRegStkrNo;
	}
	/**
	 * Returns the value of RprStkOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getRprStkOfcIssuanceNo()
	{
		return ciRprStkOfcIssuanceNo;
	}
	/**
	 * Returns the value of RprStkTransAMDate
	 * 
	 * @return int
	 */
	public int getRprStkTransAMDate()
	{
		return ciRprStkTransAMDate;
	}
	/**
	 * Returns the value of RprStkTransTime
	 * 
	 * @return int
	 */
	public int getRprStkTransTime()
	{
		return ciRprStkTransTime;
	}
	/**
	 * Returns the value of RprStkTransTime
	 * 
	 * @return int
	 */
	public int getRprStkTransWsId()
	{
		return ciRprStkTransWsId;
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
	 * Returns the value of TireTypeCd
	 * 
	 * @return  String 
	 */
	public final String getTireTypeCd()
	{
		return csTireTypeCd;
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
	 * Returns the value of TransEmpId
	 * 
	 * @return  String 
	 */
	public final String getTransEmpId()
	{
		return csTransEmpId;
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
	 * Return value of TransPostedLANIndi
	 * 
	 * @return int
	 */
	public int getTransPostedLANIndi()
	{
		return ciTransPostedLANIndi;
	}
	/**
	 * Returns the value of TransPostedMfIndi
	 * 
	 * @return  int 
	 */
	public final int getTransPostedMfIndi()
	{
		return ciTransPostedMfIndi;
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
	 * Returns the value of VehBdyType
	 * 
	 * @return  String 
	 */
	public final String getVehBdyType()
	{
		return csVehBdyType;
	}
	/**
	 * Returns the value of VehCaryngCap
	 * 
	 * @return  int 
	 */
	public final int getVehCaryngCap()
	{
		return ciVehCaryngCap;
	}
	/**
	 * Returns the value of VehEmptyWt
	 * 
	 * @return  int 
	 */
	public final int getVehEmptyWt()
	{
		return ciVehEmptyWt;
	}
	/**
	 * Returns the value of VehGrossWt
	 * 
	 * @return  int 
	 */
	public final int getVehGrossWt()
	{
		return ciVehGrossWt;
	}
	/**
	 * Returns the value of VehMk
	 * 
	 * @return  String 
	 */
	public final String getVehMk()
	{
		return csVehMk;
	}
	/**
	 * Returns the value of VehModlYr
	 * 
	 * @return  int 
	 */
	public final int getVehModlYr()
	{
		return ciVehModlYr;
	}
	/**
	 * Returns the value of VehRegState
	 * 
	 * @return  String 
	 */
	public final String getVehRegState()
	{
		return csVehRegState;
	}
	/**
	 * Returns the value of VehUnitNo
	 * 
	 * @return  String 
	 */
	public final String getVehUnitNo()
	{
		return csVehUnitNo;
	}
	/**
	 * Return value of VersionCd
	 * 
	 * @return int
	 */
	public int getVersionCd()
	{
		return ciVersionCd;
	}
	/**
	 * Returns the value of VIN
	 * 
	 * @return  String 
	 */
	public final String getVIN()
	{
		return csVIN;
	}

	/**
	 * Return VoidTransId
	 * 
	 * @return String 
	 */
	public String getVoidTransId()
	{
		return csVoidTransId;
	}

	/**
	 * Returns the value of VoidedTransIndi
	 * 
	 * @return  int 
	 */
	public final int getVoidedTransIndi()
	{
		return ciVoidedTransIndi;
	}
	/**
	 * Returns the value of VoidOfcIssuanceNo
	 * 
	 * @return  int 
	 */
	public final int getVoidOfcIssuanceNo()
	{
		return ciVoidOfcIssuanceNo;
	}
	/**
	 * Returns the value of VoidTransAMDate
	 * 
	 * @return  int 
	 */
	public final int getVoidTransAMDate()
	{
		return ciVoidTransAMDate;
	}
	/**
	 * Returns the value of VoidTransCd
	 * 
	 * @return  String 
	 */
	public final String getVoidTransCd()
	{
		return csVoidTransCd;
	}
	/**
	 * Returns the value of VoidTransTime
	 * 
	 * @return  int 
	 */
	public final int getVoidTransTime()
	{
		return ciVoidTransTime;
	}
	/**
	 * Returns the value of VoidTransWsId
	 * 
	 * @return  int 
	 */
	public final int getVoidTransWsId()
	{
		return ciVoidTransWsId;
	}

	/**
	 * Sets value of ciAddPrmtRecIndi
	 * 
	 * @param aiAddPrmtRecIndi
	 */
	public void setAddPrmtRecIndi(int aiAddPrmtRecIndi)
	{
		ciAddPrmtRecIndi = aiAddPrmtRecIndi;
	}

	/**
	 * Set value of BranchOfcId
	 * 
	 * @param aiBranchOfcId int
	 */
	public void setBranchOfcId(int aiBranchOfcId)
	{
		ciBranchOfcId = aiBranchOfcId;
	}
	/**
	 * This method sets the value of BsnDate.
	 * 
	 * @param aiBsnDate   int 
	 */
	public final void setBsnDate(int aiBsnDate)
	{
		ciBsnDate = aiBsnDate;
	}
	/**
	 * This method sets the value of BsnDateTotalAmt.
	 * 
	 * @param aaBsnDateTotalAmt   Dollar 
	 */
	public final void setBsnDateTotalAmt(Dollar aaBsnDateTotalAmt)
	{
		caBsnDateTotalAmt = aaBsnDateTotalAmt;
	}
	/**
	 * Set value of CacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate int
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}
	/**
	 * Set value of CacheTransTime
	 * 
	 * @param aiCacheTransTime int
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
	}
	/**
	 * Set value of CashWsId
	 * 
	 * @param aiCashWsId int
	 */
	public void setCashWsId(int aiCashWsId)
	{
		ciCashWsId = aiCashWsId;
	}
	/**
	 * This method sets the value of CustCity.
	 * 
	 * @param asCustCity   String 
	 */
	public final void setCustCity(String asCustCity)
	{
		csCustCity = asCustCity;
	}
	/**
	 * This method sets the value of CustCntry.
	 * 
	 * @param asCustCntry   String 
	 */
	public final void setCustCntry(String asCustCntry)
	{
		csCustCntry = asCustCntry;
	}
	/**
	 * This method sets the value of CustFstName.
	 * 
	 * @param asCustFstName   String 
	 */
	public final void setCustFstName(String asCustFstName)
	{
		csCustFstName = asCustFstName;
	}
	/**
	 * This method sets the value of CustLstName.
	 * 
	 * @param asCustLstName   String 
	 */
	public final void setCustLstName(String asCustLstName)
	{
		csCustLstName = asCustLstName;
	}
	/**
	 * This method sets the value of CustMIName.
	 * 
	 * @param asCustMIName   String 
	 */
	public final void setCustMIName(String asCustMIName)
	{
		csCustMIName = asCustMIName;
	}
	/**
	 * This method sets the value of CustName1.
	 * 
	 * @param asCustName1   String 
	 */
	public final void setCustName1(String asCustName1)
	{
		csCustName1 = asCustName1;
		
		// defect 10616 
		if (csCustName1 != null && csCustName1.length() > 30)
		{
			csCustName1 = csCustName1.substring(1,30); 
		}
		// end defect 10616 
	}
	
	/**
	 * This method sets the value of CustName2.
	 * 
	 * @param asCustName2   String 
	 */
	public final void setCustName2(String asCustName2)
	{
		csCustName2 = asCustName2;
	}
	/**
	 * Set value of CustNoKey 
	 * 
	 * @param asCustNoKey
	 */
	public void setCustNoKey(String asCustNoKey)
	{
		csCustNoKey = asCustNoKey;
	}

	/**
	 * This method sets the value of CustSeqNo.
	 * 
	 * @param asCustSeqNo   int 
	 */
	public final void setCustSeqNo(int asCustSeqNo)
	{
		ciCustSeqNo = asCustSeqNo;
	}
	/**
	 * This method sets the value of CustSt1.
	 * 
	 * @param asCustSt1   String 
	 */
	public final void setCustSt1(String asCustSt1)
	{
		csCustSt1 = asCustSt1;
	}
	/**
	 * This method sets the value of CustSt2.
	 * 
	 * @param asCustSt2   String 
	 */
	public final void setCustSt2(String asCustSt2)
	{
		csCustSt2 = asCustSt2;
	}
	/**
	 * This method sets the value of CustState.
	 * 
	 * @param asCustState   String 
	 */
	public final void setCustState(String asCustState)
	{
		csCustState = asCustState;
	}
	/**
	 * This method sets the value of CustZpCd.
	 * 
	 * @param asCustZpCd   String 
	 */
	public final void setCustZpCd(String asCustZpCd)
	{
		csCustZpCd = asCustZpCd;
	}
	/**
	 * This method sets the value of CustZpCdP4.
	 * 
	 * @param asCustZpCdP4   String 
	 */
	public final void setCustZpCdP4(String asCustZpCdP4)
	{
		csCustZpCdP4 = asCustZpCdP4;
	}

	/**
	 * Sets value of ciDelPrmtRecIndi
	 * 
	 * @param aiDelPrmtRecIndi
	 */
	public void setDelPrmtRecIndi(int aiDelPrmtRecIndi)
	{
		ciDelPrmtRecIndi = aiDelPrmtRecIndi;
	}

	/**
	 * This method sets the value of DieselIndi.
	 * 
	 * @param aiDieselIndi   int 
	 */
	public final void setDieselIndi(int aiDieselIndi)
	{
		ciDieselIndi = aiDieselIndi;
	}
	/**
	 * This method sets the value of DLSCertNo.
	 * 
	 * @param asDLSCertNo   String 
	 */
	public final void setDLSCertNo(String asDLSCertNo)
	{
		csDLSCertNo = asDLSCertNo;
	}
	/**
	 * This method sets the value of DocNo.
	 * 
	 * @param asDocNo   String 
	 */
	public final void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}
	/**
	 * This method sets the value of EffDate.
	 * 
	 * @param aiEffDate   int 
	 */
	public final void setEffDate(int aiEffDate)
	{
		ciEffDate = aiEffDate;
	}
	/**
	 * This method sets the value of EffTime.
	 * @param aiEffTime   int 
	 */
	public final void setEffTime(int aiEffTime)
	{
		ciEffTime = aiEffTime;
	}
	/**
	 * This method sets the value of ExpDate.
	 * 
	 * @param aiExpDate   int 
	 */
	public final void setExpDate(int aiExpDate)
	{
		ciExpDate = aiExpDate;
	}
	/**
	 * This method sets the value of ExpTime.
	 * 
	 * @param aiExpTime   int 
	 */
	public final void setExpTime(int aiExpTime)
	{
		ciExpTime = aiExpTime;
	}
	/**
	 * Set value of IncomplTransIndi
	 * 
	 * @param aIncomplTransIndi int
	 */
	public void setIncomplTransIndi(int aiIncomplTransIndi)
	{
		ciIncomplTransIndi = aiIncomplTransIndi;
	}
	/**
	 * Set value of OfcIssuanceCd
	 * 
	 * @param aOfcIssuanceCd int
	 */
	public void setOfcIssuanceCd(int aiOfcIssuanceCd)
	{
		ciOfcIssuanceCd = aiOfcIssuanceCd;
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
	 * This method sets the value of OwnrId.
	 * 
	 * @param asOwnrId   String 
	 */
	public final void setOwnrId(String asOwnrId)
	{
		csOwnrId = asOwnrId;
	}
	/**
	 * This method sets the value of ProcsdByMailIndi.
	 * 
	 * @param aiProcsdByMailIndi   int 
	 */
	public final void setProcsdByMailIndi(int aiProcsdByMailIndi)
	{
		ciProcsdByMailIndi = aiProcsdByMailIndi;
	}
	/**
	 * This method sets the value of RegClassCd.
	 * 
	 * @param aiRegClassCd   int 
	 */
	public final void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}
	/**
	 * This method sets the value of RegPltNo.
	 * 
	 * @param asRegPltNo   String 
	 */
	public final void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}
	/**
	 * This method sets the value of RegPnltyChrgIndi.
	 * 
	 * @param aiRegPnltyChrgIndi   int 
	 */
	public final void setRegPnltyChrgIndi(int aiRegPnltyChrgIndi)
	{
		ciRegPnltyChrgIndi = aiRegPnltyChrgIndi;
	}
	/**
	 * This method sets the value of RegStkrNo.
	 * 
	 * @param aRegStkrNo   String 
	 */
	public final void setRegStkrNo(String aRegStkrNo)
	{
		csRegStkrNo = aRegStkrNo;
	}
	/**
	 * This method sets the value of RprStkOfcIssuanceNo.
	 * 
	 * @param aiRprStkOfcIssuanceNo int
	 */
	public void setRprStkOfcIssuanceNo(int aiRprStkOfcIssuanceNo)
	{
		ciRprStkOfcIssuanceNo = aiRprStkOfcIssuanceNo;
	}
	/**
	 * This method sets the value of RprStkTransAMDate
	 * 
	 * @param aiRprStkTransAMDate int
	 */
	public void setRprStkTransAMDate(int aiRprStkTransAMDate)
	{
		ciRprStkTransAMDate = aiRprStkTransAMDate;
	}
	/**
	 * This method sets the value of RprStkTransTime
	 * 
	 * @param aiRprStkTransTime int
	 */
	public void setRprStkTransTime(int aiRprStkTransTime)
	{
		ciRprStkTransTime = aiRprStkTransTime;
	}
	/**
	 * This method sets the value of RprStkTransWsId
	 * 
	 * @param aiRprStkTransWsId int
	 */
	public void setRprStkTransWsId(int aiRprStkTransWsId)
	{
		ciRprStkTransWsId = aiRprStkTransWsId;
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
	 * This method sets the value of TireTypeCd.
	 * 
	 * @param asTireTypeCd   String 
	 */
	public final void setTireTypeCd(String asTireTypeCd)
	{
		csTireTypeCd = asTireTypeCd;
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
	 * This method sets the value of TransEmpId.
	 * 
	 * @param asTransEmpId   String 
	 */
	public final void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
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
	 * Set value of TransPostedLANIndi
	 * 
	 * @param iaTransPostedLANIndi int
	 */
	public void setTransPostedLANIndi(int aiTransPostedLANIndi)
	{
		ciTransPostedLANIndi = aiTransPostedLANIndi;
	}
	/**
	 * This method sets the value of TransPostedMfIndi.
	 * 
	 * @param aiTransPostedMfIndi   int 
	 */
	public final void setTransPostedMfIndi(int aiTransPostedMfIndi)
	{
		ciTransPostedMfIndi = aiTransPostedMfIndi;
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
	 * This method sets the value of VehBdyType.
	 * 
	 * @param asVehBdyType   String 
	 */
	public final void setVehBdyType(String asVehBdyType)
	{
		csVehBdyType = asVehBdyType;
	}
	/**
	 * This method sets the value of VehCaryngCap.
	 * 
	 * @param aiVehCaryngCap   int 
	 */
	public final void setVehCaryngCap(int aiVehCaryngCap)
	{
		ciVehCaryngCap = aiVehCaryngCap;
	}
	/**
	 * This method sets the value of VehEmptyWt.
	 * 
	 * @param aiVehEmptyWt   int 
	 */
	public final void setVehEmptyWt(int aiVehEmptyWt)
	{
		ciVehEmptyWt = aiVehEmptyWt;
	}
	/**
	 * This method sets the value of VehGrossWt.
	 * 
	 * @param aiVehGrossWt   int 
	 */
	public final void setVehGrossWt(int aiVehGrossWt)
	{
		ciVehGrossWt = aiVehGrossWt;
	}
	/**
	 * This method sets the value of VehMk.
	 * 
	 * @param asVehMk   String 
	 */
	public final void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}
	/**
	 * This method sets the value of VehModlYr.
	 * 
	 * @param aiVehModlYr   int 
	 */
	public final void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}
	/**
	 * This method sets the value of VehRegState.
	 * 
	 * @param aVehRegState   String 
	 */
	public final void setVehRegState(String aVehRegState)
	{
		csVehRegState = aVehRegState;
	}
	/**
	 * This method sets the value of VehUnitNo.
	 * 
	 * @param asVehUnitNo   String 
	 */
	public final void setVehUnitNo(String asVehUnitNo)
	{
		csVehUnitNo = asVehUnitNo;
	}
	/**
	 * Set value of VersionCd
	 * 
	 * @param aiVersionCd int
	 */
	public void setVersionCd(int aiVersionCd)
	{
		ciVersionCd = aiVersionCd;
	}
	/**
	 * This method sets the value of VIN.
	 * 
	 * @param asVIN   String 
	 */
	public final void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}
	/**
	 * This method sets the value of VoidedTransIndi.
	 * 
	 * @param aiVoidedTransIndi   int 
	 */
	public final void setVoidedTransIndi(int aiVoidedTransIndi)
	{
		ciVoidedTransIndi = aiVoidedTransIndi;
	}
	/**
	 * This method sets the value of VoidOfcIssuanceNo.
	 * 
	 * @param aiVoidOfcIssuanceNo   int 
	 */
	public final void setVoidOfcIssuanceNo(int aiVoidOfcIssuanceNo)
	{
		ciVoidOfcIssuanceNo = aiVoidOfcIssuanceNo;
	}
	/**
	 * This method sets the value of VoidTransAMDate.
	 * 
	 * @param aiVoidTransAMDate   int 
	 */
	public final void setVoidTransAMDate(int aiVoidTransAMDate)
	{
		ciVoidTransAMDate = aiVoidTransAMDate;
	}
	/**
	 * This method sets the value of VoidTransCd.
	 * 
	 * @param asVoidTransCd   String 
	 */
	public final void setVoidTransCd(String asVoidTransCd)
	{
		csVoidTransCd = asVoidTransCd;
	}

	/**
	 * Set VoidTransId
	 * 
	 * @param asVoidTransId
	 */
	public void setVoidTransId(String asVoidTransId)
	{
		csVoidTransId = asVoidTransId;
	}

	/**
	 * This method sets the value of VoidTransTime.
	 * 
	 * @param aiVoidTransTime   int 
	 */
	public final void setVoidTransTime(int aiVoidTransTime)
	{
		ciVoidTransTime = aiVoidTransTime;
	}
	/**
	 * This method sets the value of VoidTransWsId.
	 * 
	 * @param aiVoidTransWsId   int 
	 */
	public final void setVoidTransWsId(int aiVoidTransWsId)
	{
		ciVoidTransWsId = aiVoidTransWsId;
	}

}
