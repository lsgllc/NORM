package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 *
 * LogonFunctionTransactionData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/17/2001	Added TransPostedLANIndi for SendTrans
 * K Harrell	04/13/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * Ray Rowehl	09/27/2006	Add MfVersionNo to help in SendTrans Queries.
 * 							add ciMfVersionNo
 * 							defect 8959 Ver FallAdminTables
 * K Harrell	09/12/2011	add MISSING
 * 							add LogonFunctionTransactionData(), 
 * 							  LogonFunctionTransactionData(LogonData) 
 * 							add ciCacheTransAMDate, ciCacheTransTime, 
 * 							 get/set methods 
 * 							defect 10994 Ver 6.8.1 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for
 * LogonFunctionTransactionData
 * 
 * @version 6.8.1  		09/12/2011
 * @author Marx Rajang.. 
 * @since 				09/17/2001
 */

public class LogonFunctionTransactionData implements Serializable,
		Displayable
{
	// int
	protected int ciMfVersionNo;
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciSuccessfulIndi;
	protected int ciSysDate;
	protected int ciSysTime;
	protected int ciTransPostedLANIndi; // SendTrans
	protected int ciTransPostedMfIndi;
	protected int ciWsId;

	protected String csEmpId;
	
	// defect 10994
	protected int ciCacheTransAMDate;
	protected int ciCacheTransTime;
	
	private static final String MISSING = "Missing";
	
	// end defect 10994 
	private final static long serialVersionUID = -3858791272981271602L;


	/**
	 * LogonFunctionTransactionData constructor comment.
	 */
	public LogonFunctionTransactionData()
	{
		super();
	}

	/**
	 * LogonFunctionTransactionData constructor comment.
	 */
	public LogonFunctionTransactionData(LogonData aaLogonData)
	{
		super();
		setOfcIssuanceNo(aaLogonData.getOfcIssuanceNo());
		setSubstaId(aaLogonData.getSubstaId());
		setWsId(aaLogonData.getWsId());
		setSysDate(new RTSDate().getYYYYMMDDDate());
		setSysTime(new RTSDate().get24HrTime());
		
		if (aaLogonData.getSecurityData() == null || 
				UtilityMethods.isEmpty(aaLogonData.getSecurityData().getEmpId()))
		{
			setEmpId(MISSING); 
		}
		else
		{
			setEmpId(aaLogonData.getSecurityData().getEmpId()); 
		}
	}

	/**
	 * Returns attributes for display in ShowCache
	 * 
	 * @return Map
	 */
	public Map getAttributes()
	{
		HashMap lhmHash = new HashMap();
		Field[] arrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < arrFields.length; i++)
		{
			try
			{
				lhmHash.put(arrFields[i].getName(), arrFields[i]
						.get(this));
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;
	}

	/**
	 * @return the ciCacheTransAMDate
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}

	/**
	 * 
	 * @return ciCacheTransTime
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}

	/**
	 * Returns the value of EmpId
	 * 
	 * @return String
	 */
	public final String getEmpId()
	{
		return csEmpId;
	}

	/**
	 * Get the MfVersionNo.
	 * 
	 * @return int
	 */
	public int getMfVersionNo()
	{
		return ciMfVersionNo;
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
	 * Returns the value of SubstaId
	 * 
	 * @return int
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Returns the value of SuccessfulIndi
	 * 
	 * @return int
	 */
	public final int getSuccessfulIndi()
	{
		return ciSuccessfulIndi;
	}

	/**
	 * Returns the value of SysDate
	 * 
	 * @return int
	 */
	public final int getSysDate()
	{
		return ciSysDate;
	}

	/**
	 * Returns the value of SysTime
	 * 
	 * @return int
	 */
	public final int getSysTime()
	{
		return ciSysTime;
	}

	/**
	 * Returns the value of TransPostedLANIndi
	 * 
	 * @return int
	 */
	public final int getTransPostedLANIndi()
	{
		return ciTransPostedLANIndi;
	}

	/**
	 * Returns the value of TransPostedMfIndi
	 * 
	 * @return int
	 */
	public final int getTransPostedMfIndi()
	{
		return ciTransPostedMfIndi;
	}

	/**
	 * Returns the value of WsId
	 * 
	 * @return int
	 */
	public final int getWsId()
	{
		return ciWsId;
	}

	/**
	 * Set value of ciCacheTransAMDate
	 * 
	 * @param ciCacheTransAMDate
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}

	/**
	 * Set value of ciCacheTransTime
	 * 
	 * @param ciCacheTransTime
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
	}

	/**
	 * This method sets the value of EmpId.
	 * 
	 * @param asEmpId
	 *            String
	 */
	public final void setEmpId(String asEmpId)
	{
		csEmpId = asEmpId;
	}

	/**
	 * Set the MfVersionNo.
	 * 
	 * @param aiMfVersionNo
	 */
	public void setMfVersionNo(int aiMfVersionNo)
	{
		ciMfVersionNo = aiMfVersionNo;
	}

	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo
	 *            int
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * This method sets the value of SubstaId.
	 * 
	 * @param aiSubstaId
	 *            int
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}

	/**
	 * This method sets the value of SuccessfulIndi.
	 * 
	 * @param aiSuccessfulIndi
	 *            int
	 */
	public final void setSuccessfulIndi(int aiSuccessfulIndi)
	{
		ciSuccessfulIndi = aiSuccessfulIndi;
	}

	/**
	 * This method sets the value of SysDate.
	 * 
	 * @param aiSysDate
	 *            int
	 */
	public final void setSysDate(int aiSysDate)
	{
		ciSysDate = aiSysDate;
	}

	/**
	 * This method sets the value of SysTime.
	 * 
	 * @param aiSysTime
	 *            int
	 */
	public final void setSysTime(int aiSysTime)
	{
		ciSysTime = aiSysTime;
	}

	/**
	 * This method sets the value of TransPostedLANIndi.
	 * 
	 * @param aiTransPostedLANIndi
	 *            int
	 */
	public final void setTransPostedLANIndi(int aiTransPostedLANIndi)
	{
		ciTransPostedLANIndi = aiTransPostedLANIndi;
	}

	/**
	 * This method sets the value of TransPostedMfIndi.
	 * 
	 * @param aiTransPostedMfIndi
	 *            int
	 */
	public final void setTransPostedMfIndi(int aiTransPostedMfIndi)
	{
		ciTransPostedMfIndi = aiTransPostedMfIndi;
	}

	/**
	 * This method sets the value of WsId.
	 * 
	 * @param aiWsId
	 *            int
	 */
	public final void setWsId(int aiWsId)
	{
		ciWsId = aiWsId;
	}
}