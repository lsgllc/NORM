package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * TitleTransferPenaltyFeeData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/02/2008	Created
 * 							defect 9583 Ver Defect POS A
 * K Harrell	05/21/2008	Renamed *WorkDays* to *CalndrDays*
 * 							add ciCalndrDaysCount, ciEndCalndrDaysCount,
 * 							 get/set methods 
 * 							delete ciBegWorkDaysCount, ciEndWorkDaysCount, 
 * 							 get/set methods  
 * 							defect 9583 Ver Defect POS A   
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * TitleTransferPenaltyFeeData
 *
 * @version	Defect POS A		05/21/2008
 * @author	K Harrell
 * <br>Creation Date:			04/02/2008 16:10:00
 */
public class TitleTransferPenaltyFeeData implements Serializable
{

	private String csTtlTrnsfrEntCd;

	private int ciRTSEffDate;
	private int ciRTSEffEndDate;
	private int ciBegCalndrDaysCount;
	private int ciEndCalndrDaysCount;
	private int ciDelnqntPrortnIncrmnt;

	private Dollar caBaseTtlTrnsfrPnltyAmt;
	private Dollar caAddlPnltyAmt;

	static final long serialVersionUID = -6670033295649452742L;

	/**
	 * Return value of caAddlPnltyAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getAddlPnltyAmt()
	{
		return caAddlPnltyAmt;
	}

	/**
	 * Return value of caBaseTtlTrnsfrPnltyAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getBaseTtlTrnsfrPnltyAmt()
	{
		return caBaseTtlTrnsfrPnltyAmt;
	}

	/**
	 * Return value of ciBegCalndrDaysCount
	 * 
	 * @return int
	 */
	public int getBegCalndrDaysCount()
	{
		return ciBegCalndrDaysCount;
	}

	/**
	 * Return value of ciDelnqntPrortnIncrmnt
	 * 
	 * @return int
	 */
	public int getDelnqntPrortnIncrmnt()
	{
		return ciDelnqntPrortnIncrmnt;
	}

	/**
	 * Return value of ciEndCalndrDaysCount
	 * 
	 * @return int
	 */
	public int getEndCalndrDaysCount()
	{
		return ciEndCalndrDaysCount;
	}

	/**
	 * Return value of ciRTSEffDate
	 * 
	 * @return int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}

	/**
	 * Return value of ciRTSEffEndDate
	 * 
	 * @return int
	 */
	public int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}

	/**
	 * Return value of String 
	 * 
	 * @return String
	 */
	public String getTtlTrnsfrEntCd()
	{
		return csTtlTrnsfrEntCd;
	}

	/**
	 * Set value of caAddlPnltyAmt
	 * 
	 * @param aaAddlPnltyAmt
	 */
	public void setAddlPnltyAmt(Dollar aaAddlPnltyAmt)
	{
		caAddlPnltyAmt = aaAddlPnltyAmt;
	}

	/**
	 * Set value of caBaseTtlTrnsfrPnltyAmt
	 * 
	 * @param aaBaseTtlTrnsfrPnltyAmt
	 */
	public void setBaseTtlTrnsfrPnltyAmt(Dollar aaBaseTtlTrnsfrPnltyAmt)
	{
		caBaseTtlTrnsfrPnltyAmt = aaBaseTtlTrnsfrPnltyAmt;
	}

	/**
	 * Set value of ciBegCalndrDaysCount 
	 * 
	 * @param aiBegCalndrDaysCount
	 */
	public void setBegCalndrDaysCount(int aiBegCalndrDaysCount)
	{
		ciBegCalndrDaysCount = aiBegCalndrDaysCount;
	}

	/**
	 * Set value of ciDelnqntPrortnIncrmnt
	 * 
	 * @param aiDelnqntPrortnIncrmnt
	 */
	public void setDelnqntPrortnIncrmnt(int aiDelnqntPrortnIncrmnt)
	{
		ciDelnqntPrortnIncrmnt = aiDelnqntPrortnIncrmnt;
	}

	/**
	 * Set value of ciEndCalndrDaysCount
	 * 
	 * @param aiEndCalndrDaysCount
	 */
	public void setEndCalndrDaysCount(int aiEndCalndrDaysCount)
	{
		ciEndCalndrDaysCount = aiEndCalndrDaysCount;
	}

	/**
	 * Set value of ciRTSEffDate
	 * 
	 * @param aiRTSEffDate
	 */
	public void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}

	/**
	 * Set value of ciRTSEffEndDate
	 * 
	 * @param aiRTSEffEndDate
	 */
	public void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}

	/**
	 * Set value of csTtlTrnsfrEntCd
	 * 
	 * @param asTtlTrnsfrEntCd
	 */
	public void setTtlTrnsfrEntCd(String asTtlTrnsfrEntCd)
	{
		csTtlTrnsfrEntCd = asTtlTrnsfrEntCd;
	}
}
