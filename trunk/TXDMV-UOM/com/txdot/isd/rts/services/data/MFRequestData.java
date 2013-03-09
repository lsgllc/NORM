package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * MFRequest.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/29/2010	Created
 * 							defect 10462 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
* This Data class contains attributes and get and set methods for 
 * MFRequestData
 *
 * @version	6.5.0		07/29/2010	
 * @author	Kathy Harrell
 * <br>Creation Date:	07/29/2010	18:20:17
 */
public class MFRequestData
{
	private RTSDate caDateParm1;
	private RTSDate caDateParm2;
	private RTSDate caReqTimeStmp;
	private RTSDate caRespTimestmp;
	
	private int ciErrMsgCd;
	private int ciMFReqIdntyNo;
	private int ciRetryNo;
	private int ciSuccessfulIndi;
	private int ciTierCd;
	
	private String csCICSTransId;
	private String csParm1;
	private String csReqKey;

	/**
	 * MFRequestData.java Constructor
	 * 
	 */
	public MFRequestData()
	{
		super();
	}

	/**
	 * Get value of csCICSTransId
	 * 
	 * @return String
	 */
	public String getCICSTransId()
	{
		return csCICSTransId;
	}

	/**
	 * Get value of caDateParm1
	 * 
	 * @return RTSDate
	 */
	public RTSDate getDateParm1()
	{
		return caDateParm1;
	}

	/**
	 * Get value of caDateParm2
	 * 
	 * @return RTSDate
	 */
	public RTSDate getDateParm2()
	{
		return caDateParm2;
	}

	/**
	 * Get value of ciErrMsgCd
	 * 
	 * @return int 
	 */
	public int getErrMsgCd()
	{
		return ciErrMsgCd;
	}

	/**
	 * Get value of ciMFReqIdntyNo
	 * 
	 * @return int
	 */
	public int getMFReqIdntyNo()
	{
		return ciMFReqIdntyNo;
	}

	/**
	 * Get value of csParm1
	 * 
	 * @return String
	 */
	public String getParm1()
	{
		return csParm1;
	}

	/**
	 * Get value of csReqKey
	 * 
	 * @return String
	 */
	public String getReqKey()
	{
		return csReqKey;
	}

	/**
	 * Get value of caReqTimeStmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getReqTimeStmp()
	{
		return caReqTimeStmp;
	}

	/**
	 * Get value of caRespTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getRespTimestmp()
	{
		return caRespTimestmp;
	}

	/**
	 * Get value of ciRetryNo
	 * 
	 * @return int
	 */
	public int getRetryNo()
	{
		return ciRetryNo;
	}

	/**
	 * Get value of ciSuccessfulIndi
	 * 
	 * @return int 
	 */
	public int getSuccessfulIndi()
	{
		return ciSuccessfulIndi;
	}

	/**
	 * Get value of ciTierCd
	 * 
	 * @return int
	 */
	public int getTierCd()
	{
		return ciTierCd;
	}

	/**
	 * Set value of csCICSTransId
	 * 
	 * @param asCICSTransId
	 */
	public void setCICSTransId(String asCICSTransId)
	{
		csCICSTransId = asCICSTransId;
	}
	/**
	 * 
	 * Set value of caDateParm1
	 * 
	 * @param aiDateParm1 
	 */
	public void setDateParm1(int aiDateParm1)
	{
		setDateParm1("" + aiDateParm1);

	}

	/**
	 * Set value of caDateParm1
	 * 
	 * @param aaDateParm1
	 */
	public void setDateParm1(RTSDate aaDateParm1)
	{
		caDateParm1 = aaDateParm1;
	}
	
	/**
	 * 
	 * Set value of caDateParm1
	 * 
	 * @param csDateParm1
	 */
	public void setDateParm1(String csDateParm1)
	{
		if (!UtilityMethods.isEmpty(csDateParm1))
		{
			try
			{
				int liDateParm1 = Integer.parseInt(csDateParm1);
				if (liDateParm1 != 0)
				{
					caDateParm1 =
						new RTSDate(RTSDate.YYYYMMDD, liDateParm1);
				}
			}
			catch (Exception aeRTSEx)
			{
			}
		}
	}

	/**
	 * Set value of caDateParm2
	 * 
	 * @param ciDateParm2
	 */
	public void setDateParm2(int ciDateParm2)
	{
		setDateParm2("" + ciDateParm2);

	}

	/**
	 * Set value of caDateParm2
	 * 
	 * @param aaDateParm2
	 */
	public void setDateParm2(RTSDate aaDateParm2)
	{
		caDateParm2 = aaDateParm2;
	}
	
	/**
	 * Set value of caDateParm2
	 * 
	 * @param csDate
	 */
	public void setDateParm2(String csDateParm2)
	{
		if (!UtilityMethods.isEmpty(csDateParm2))
		{
			try
			{
				int liDateParm2 = Integer.parseInt(csDateParm2);
				if (liDateParm2 != 0)
				{
					caDateParm2 =
						new RTSDate(RTSDate.YYYYMMDD, liDateParm2);
				}
			}
			catch (Exception aeRTSEx)
			{

			}
		}
	}

	/**
	 * Set value of ciErrMsgCd
	 * 
	 * @param aiErrMsgCd
	 */
	public void setErrMsgCd(int aiErrMsgCd)
	{
		ciErrMsgCd = aiErrMsgCd;
	}

	/**
	 * Set value of ciMFReqIdntyNo
	 * 
	 * @param aiMFReqIdntyNo
	 */
	public void setMFReqIdntyNo(int aiMFReqIdntyNo)
	{
		ciMFReqIdntyNo = aiMFReqIdntyNo;
	}

	/**
	 * Set value of csParm1
	 * 
	 * @param asParm1
	 */
	public void setParm1(String asParm1)
	{
		if (!UtilityMethods.isEmpty(asParm1))
		{
			csParm1 = asParm1;
		}
	}

	/**
	 * Set value of csReqKey
	 * 
	 * @param asReqKey
	 */
	public void setReqKey(String asReqKey)
	{
		csReqKey = asReqKey;
	}

	/**
	 * Set value of caReqTimeStmp
	 * 
	 * @param aaReqTimeStmp
	 */
	public void setReqTimeStmp(RTSDate aaReqTimeStmp)
	{
		caReqTimeStmp = aaReqTimeStmp;
	}

	/**
	 * Set value of caRespTimestmp
	 * 
	 * @param aaRespTimestmp
	 */
	public void setRespTimestmp(RTSDate aaRespTimestmp)
	{
		caRespTimestmp = aaRespTimestmp;
	}

	/**
	 * Set value of ciRetryNo
	 * 
	 * @param aiRetryNo
	 */
	public void setRetryNo(int aiRetryNo)
	{
		ciRetryNo = aiRetryNo;
	}

	/**
	 * Set value of ciSuccessfulIndi
	 * 
	 * @param aiSuccessfulIndi
	 */
	public void setSuccessfulIndi(int aiSuccessfulIndi)
	{
		ciSuccessfulIndi = aiSuccessfulIndi;
	}

	/**
	 * Set value of ciTierCd
	 * 
	 * @param aiTierCd
	 */
	public void setTierCd(int aiTierCd)
	{
		ciTierCd = aiTierCd;
	}
}
