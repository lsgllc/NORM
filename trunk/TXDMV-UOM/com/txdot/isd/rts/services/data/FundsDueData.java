package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * FundsDueData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			09/07/2001  Added comments
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * FundsDueData contains all the information needed for a Funds 
 * Remittance record.
 * 
 * @version	5.2.3		05/19/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	07/17/2001 17:12:37  
 */

public class FundsDueData implements java.io.Serializable
{
	// Object
	private Dollar caDueAmount;
	private Dollar caEntDueAmount;
	private Dollar caFundsReceivedAmount;
	private Dollar caRemitAmount;
	private RTSDate caFundsDueDate;
	private RTSDate caFundsReportDate;
	private RTSDate caReportingDate;

	// String 
	private String csComptCountyNo;
	private String csFundsCategory;
	private String csFundsReceivingEntity;

	private final static long serialVersionUID = 8642254967556439373L;
	/**
	 * Creates a FundsDueData.
	 */
	public FundsDueData()
	{
		super();
	}
	/**
	 * Returns the Compt County Number.
	 */
	public String getComptCountyNo()
	{
		return csComptCountyNo;
	}
	/**
	 * Returns the Due Amount.
	 */
	public Dollar getDueAmount()
	{
		if (caDueAmount == null)
		{
			return new Dollar("0.00");
		}
		else
		{
			return caDueAmount;
		}
	}
	/**
	 * Returns the Ent Due Amount.
	 * 
	 * @return Dollar 
	 */
	public Dollar getEntDueAmount()
	{
		if (caEntDueAmount == null)
		{
			return new Dollar("0.00");
		}
		else
		{
			return caEntDueAmount;
		}
	}
	/**
	 * Returns the Funds Category.
	 * 
	 * @return String
	 */
	public String getFundsCategory()
	{
		if (csFundsCategory == null)
		{
			return "";
		}
		else
		{
			return csFundsCategory;
		}
	}
	/**
	 * Returns the Funds Due Date.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getFundsDueDate()
	{
		return caFundsDueDate;
	}
	/**
	 * Returns the Funds Received Amount.
	 * 
	 * @return Dollar
	 */
	public Dollar getFundsReceivedAmount()
	{
		if (caFundsReceivedAmount == null)
		{
			return new Dollar("0.00");
		}
		else
		{
			return caFundsReceivedAmount;
		}
	}
	/**
	 * Returns the Funds Receiving Entity.
	 */
	public String getFundsReceivingEntity()
	{
		return csFundsReceivingEntity;
	}
	/**
	 * Returns the Funds Report Date.
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getFundsReportDate()
	{
		return caFundsReportDate;
	}
	/**
	 * Returns the Remit Amount.
	 * 
	 * @return Dollar 
	 */
	public Dollar getRemitAmount()
	{
		if (caRemitAmount == null)
		{
			return new Dollar("0.00");
		}
		else
		{
			return caRemitAmount;
		}
	}
	/**
	 * Returns the Reporting Date.
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getReportingDate()
	{
		return caReportingDate;
	}
	/**
	 * Sets the Compt County Number.
	 * 
	 * @param asComptCountyNo String
	 */
	public void setComptCountyNo(String asComptCountyNo)
	{
		csComptCountyNo = asComptCountyNo;
	}
	/**
	 * Sets the Due Amount.
	 * 
	 * @param aaDueAmount  Dollar
	 */
	public void setDueAmount(Dollar aaDueAmount)
	{
		caDueAmount = aaDueAmount;
	}
	/**
	 * Sets the Ent Due Amount.
	 * 
	 * @param aaEntDueAmount  Dollar 
	 */
	public void setEntDueAmount(Dollar aaEntDueAmount)
	{
		caEntDueAmount = aaEntDueAmount;
	}
	/**
	 * Sets the Funds Category.
	 * 
	 * @param aaFundsCategory  String
	 */
	public void setFundsCategory(String aaFundsCategory)
	{
		csFundsCategory = aaFundsCategory;
	}
	/**
	 * Sets the Funds Due Date.
	 * 
	 * @param aaFundsDueDate  RTSDate
	 */
	public void setFundsDueDate(RTSDate aaFundsDueDate)
	{
		caFundsDueDate = aaFundsDueDate;
	}
	/**
	 * Sets the Funds Received Amount.
	 * 
	 * @param aaFundsReceivedAmount  Dollar
	 */
	public void setFundsReceivedAmount(Dollar aaFundsReceivedAmount)
	{
		caFundsReceivedAmount = aaFundsReceivedAmount;
	}
	/**
	 * Sets the Funds Receiving Entity.
	 * 
	 * @param asFundsReceivingEntity  String
	 */
	public void setFundsReceivingEntity(String asFundsReceivingEntity)
	{
		csFundsReceivingEntity = asFundsReceivingEntity;
	}
	/**
	 * Sets the Funds Report Date.
	 * 
	 * @param aaFundsReportDate  RTSDate 
	 */
	public void setFundsReportDate(RTSDate aaFundsReportDate)
	{
		caFundsReportDate = aaFundsReportDate;
	}
	/**
	 * Sets the Remit Amount.
	 * 
	 * @param aaRemitAmount  Dollar 
	 */
	public void setRemitAmount(Dollar aaRemitAmount)
	{
		caRemitAmount = aaRemitAmount;
	}
	/**
	 * Sets the Reporting Date.
	 * 
	 * @param aaReportingDate  RTSDate
	 */
	public void setReportingDate(RTSDate aaReportingDate)
	{
		caReportingDate = aaReportingDate;
	}
}
