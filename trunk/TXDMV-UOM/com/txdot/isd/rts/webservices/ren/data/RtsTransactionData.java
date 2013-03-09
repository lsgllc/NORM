package com.txdot.isd.rts.webservices.ren.data;

import java.util.Calendar;
import java.util.Date;

/*
 * RtsTransactionData.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/07/2011	Initial load.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/14/2011	Add TransId
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/15/2011	Add the TransTimeStmp.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	03/28/2011	Change the name on ReqIpAddr to 
 * 							WebSessionId.
 * 							add csWebSessionId
 * 							delete cbCitationIndi, csReqIpAddr
 * 							add getWebSessionId(), setWebSessionId()
 * 							delete getReqIpAddr(), setReqIpAddr(), 
 * 								isCitationIndi(), setCitationIndi()
 * 							defect 10673 Ver 6.7.1
 * K McKee      11/02/2011  add csUserName,ciAgntIdntyNo get/set methods
 *                          defect 11145 Ver 6.9.0
 * R Pilon		02/02/2012	Add missing setter method to prevent web service 
 * 							  validation error.
 * 							add setAccptTimestmpDate()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Stores data about transaction processing for Web Agent.
 *
 * @version	6.10.0			02/02/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		01/07/2011 12:37:42
 */

public class RtsTransactionData
{
	private Calendar caAccptTimestmp;
	
	private boolean cbAccptVehIndi;
	private boolean cbAgncyVoidIndi;
	private boolean cbCntyVoidIndi;
	
	private int ciAgncyBatchIdntyNo;
	// defect 11145
	private int ciAgntIdntyNo;
	// end defect 11145
	private int ciAgntSecrtyIdntyNo;
	private int ciSAVReqId;
	private int ciSubconId;
	
	private String csAuditTrailTransId = "";
	private String csBarCdVersionNo = "";
	private String csKeyTypeCd = "";
	private String csPosTransId = "";
	private String csWebSessionId = "";
	//	 defect 11145
 	private String csUserName="";
 	// end defect 11145

	/**
	 * Get the Accept Timestmp 
	 * 
	 * @return Calendar
	 */
	public Calendar getAccptTimestmp()
	{
		return caAccptTimestmp;
	}

	/**
	 * Get the Accept Timestamp Date.
	 * 
	 * @return Date
	 */
	public Date getAccptTimestmpDate()
	{
		if (caAccptTimestmp != null)
		{
			return caAccptTimestmp.getTime();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Get the Agency Batch Indicator.
	 * 
	 * @return int
	 */
	public int getAgncyBatchIdntyNo()
	{
		return ciAgncyBatchIdntyNo;
	}


	/**
	 * @return the AgntIdntyNo
	 */
	public int getAgntIdntyNo()
	{
		return ciAgntIdntyNo;
	}

	/**
	 * Get Agent Security Identity Number.
	 * 
	 * @return int
	 */
	public int getAgntSecrtyIdntyNo()
	{
		return ciAgntSecrtyIdntyNo;
	}

	/**
	 * Get the Audit Trail Transid.
	 * 
	 * @return String
	 */
	public String getAuditTrailTransId()
	{
		return csAuditTrailTransId;
	}

	/**
	 * Get the Bar Code Version Number.
	 * 
	 * <p>Empty if not scanned.
	 * 
	 * @return String
	 */
	public String getBarCdVersionNo()
	{
		return csBarCdVersionNo;
	}

	/**
	 * Get the Key Type Code.
	 * 
	 * @return String
	 */
	public String getKeyTypeCd()
	{
		return csKeyTypeCd;
	}

	/**
	 * Get the POS Transaction Id.
	 * 
	 * @return String
	 */
	public String getPosTransId()
	{
		return csPosTransId;
	}

	/**
	 * Get the Identity Number for this renewal request.
	 * 
	 * @return int
	 */
	public int getSAVReqId()
	{
		return ciSAVReqId;
	}

	/**
	 * Get the Subcon Id.
	 * 
	 * @return int
	 */
	public int getSubconId()
	{
		return ciSubconId;
	}

	/**
	 * Get the User's name
	 * 
	 * @return the UserName
	 */
	public String getUserName()
	{
		return csUserName;
	}

	
	/**
	 * Get the Requestor's Web Session Id.
	 * 
	 * @return String
	 */
	public String getWebSessionId()
	{
		return csWebSessionId;
	}

	/**
	 * Get the Vehicle Accepted Indicator.
	 * 
	 * @return boolean
	 */
	public boolean isAccptVehIndi()
	{
		return cbAccptVehIndi;
	}

	/**
	 * Get the Request was Voided by the Agency Indicator.
	 * 
	 * @return boolean
	 */
	public boolean isAgncyVoidIndi()
	{
		return cbAgncyVoidIndi;
	}

	/**
	 * Get the Request was Voided by the County Indicator.
	 * 
	 * @return boolean
	 */
	public boolean isCntyVoidIndi()
	{
		return cbCntyVoidIndi;
	}

	/**
	 * Set the Accept Timestamp.
	 * 
	 * @param aaAccptTimestmp
	 */
	public void setAccptTimestmp(Calendar aaAccptTimestmp)
	{
		caAccptTimestmp = aaAccptTimestmp;
	}

	/**
	 * Set the Accept Timestamp from Date.
	 * 
	 * @param aaAccptTimestmp
	 */
	public void setAccptTimestmpDate(Date aaAccptTimestmpDate)
	{
		if (aaAccptTimestmpDate != null)
		{
			caAccptTimestmp.setTime(aaAccptTimestmpDate);
		}
	}

	/**
	 * Set the Vehicle Accepted Indicator.
	 * 
	 * @param abAccptVehIndi
	 */
	public void setAccptVehIndi(boolean abAccptVehIndi)
	{
		cbAccptVehIndi = abAccptVehIndi;
	}

	/**
	 * Set the Agency Batch Indicator.
	 * 
	 * @param aiAgncyBatchIdntyNo
	 */
	public void setAgncyBatchIdntyNo(int aiAgncyBatchIdntyNo)
	{
		ciAgncyBatchIdntyNo = aiAgncyBatchIdntyNo;
	}

	/**
	 * Set the Voided by Agency Indicator.
	 * 
	 * @param abAgncyVoidIndi
	 */
	public void setAgncyVoidIndi(boolean abAgncyVoidIndi)
	{
		cbAgncyVoidIndi = abAgncyVoidIndi;
	}

	/**
	 * @param aiAgntIdntyNo the AgntIdntyNo to set
	 */
	public void setAgntIdntyNo(int aiAgntIdntyNo)
	{
		this.ciAgntIdntyNo = aiAgntIdntyNo;
	}
	
	/**
	 * Set the Agenct Security Indentity Number.
	 * 
	 * @param aiAgntSecrtyIdntyNo
	 */
	public void setAgntSecrtyIdntyNo(int aiAgntSecrtyIdntyNo)
	{
		ciAgntSecrtyIdntyNo = aiAgntSecrtyIdntyNo;
	}

	/**
	 * Set the Audit Trail TransId.
	 * 
	 * @param asAuditTrailTransId
	 */
	public void setAuditTrailTransId(String asAuditTrailTransId)
	{
		csAuditTrailTransId = asAuditTrailTransId;
	}

	/**
	 * Set the Bar Code Version Number.
	 * 
	 * @param asBarCdVersionNo
	 */
	public void setBarCdVersionNo(String asBarCdVersionNo)
	{
		csBarCdVersionNo = asBarCdVersionNo;
	}

	/**
	 * Set the Voided by County Indicator.
	 * 
	 * @param abCntyVoidIndi
	 */
	public void setCntyVoidIndi(boolean abCntyVoidIndi)
	{
		cbCntyVoidIndi = abCntyVoidIndi;
	}

	/**
	 * Set the Key Type Code.
	 * 
	 * @param asKeyTypeCd
	 */
	public void setKeyTypeCd(String asKeyTypeCd)
	{
		csKeyTypeCd = asKeyTypeCd;
	}

	/**
	 * Set the POS Transaction Id.
	 * 
	 * @param asPosTransId
	 */
	public void setPosTransId(String asPosTransId)
	{
		csPosTransId = asPosTransId;
	}

	/**
	 * Set the Identity Number for the Renewal Request.
	 * 
	 * @param aiSAVReqId
	 */
	public void setSAVReqId(int aiSAVReqId)
	{
		ciSAVReqId = aiSAVReqId;
	}

	/**
	 * Set the Subcon Id.
	 * 
	 * @param aiSubconId
	 */
	public void setSubconId(int aiSubconId)
	{
		ciSubconId = aiSubconId;
	}

	/**
	 * Set the User's name
	 * 
	 * @param asUserName the UserName to set
	 */
	public void setUserName(String asUserName)
	{
		this.csUserName = asUserName;
	}
	
	/**
	 * Set the Requestor's Web Session Id.
	 *
	 * @param asWebSessionId
	 */
	public void setWebSessionId(String asWebSessionId)
	{
		csWebSessionId = asWebSessionId;
	}

}
