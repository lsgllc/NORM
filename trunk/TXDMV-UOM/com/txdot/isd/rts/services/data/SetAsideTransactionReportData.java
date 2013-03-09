package com.txdot.isd.rts.services.data;

/*
 * SetAsideTransactionReportData.java
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896	Ver 5.2.3
 * K Harrell	10/17/2005	Moved from services.reports to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3  
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods
 * for SetAsideTransactionReportData
 *
 * @version	5.2.3 			10/17/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/20/2001 13:40:40
 */
public class SetAsideTransactionReportData
{
	private int ciOfcIssuanceNo;
	private int ciTransWsId;
	private int ciTransAMDate;
	private int ciTransTime;
	private int ciCustSeqNo;
	private String csTransCd;
	private String csCustName1;
	private String csVIN;

	/**
	 * Returns the value of CustName1
	 * 
	 * @return String 
	 */
	public final String getCustName1()
	{
		return csCustName1;
	}

	/**
	 * Returns the value of CustSeqNo
	 * 
	 * @return int 
	 */
	public final int getCustSeqNo()
	{
		return ciCustSeqNo;
	}

	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Returns the value of TransAMDate
	 * 
	 * @return int 
	 */
	public final int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Returns the value of TransCd
	 * 
	 * @return String 
	 */
	public final String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Returns the value of TransTime
	 * 
	 * @return int 
	 */
	public final int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Returns the value of TransWsId
	 * 
	 * @return int 
	 */
	public final int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Returns the value of VIN
	 * 
	 * @return String 
	 */
	public final String getVIN()
	{
		return csVIN;
	}

	/**
	 * This method sets the value of CustName1.
	 * 
	 * @param asCustName1 String 
	 */
	public final void setCustName1(String asCustName1)
	{
		csCustName1 = asCustName1;
	}

	/**
	 * This method sets the value of CustSeqNo.
	 * 
	 * @param aiCustSeqNo int 
	 */
	public final void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}

	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * This method sets the value of TransAMDate.
	 * 
	 * @param aiTransAMDate int 
	 */
	public final void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * This method sets the value of TransCd.
	 * 
	 * @param asTransCd String 
	 */
	public final void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * This method sets the value of TransTime.
	 * 
	 * @param aiTransTime int 
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * This method sets the value of TransWsId.
	 * 
	 * @param aiTransWsId int 
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * This method sets the value of VIN.
	 * 
	 * @param asVIN String 
	 */
	public final void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}
}