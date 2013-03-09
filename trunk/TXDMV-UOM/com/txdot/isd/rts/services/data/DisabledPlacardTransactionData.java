package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * DisabledPlacardTransactionData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	11/01/2008	add csVoidTransEmpId, get/set methods
 * 							defect 9831 Ver POS_Defect_B 
 * K Harrell	10/16/2009	Modify for ShowCache
 * 							add DisabledPlacardTransactionData(); 
 * 							modify getAttributes(), getDsabldPlcrd() 
 * 							defect 10191 Ver Defect_POS_G     
 * K Harrell	06/14/2010	add csTransId, get/set methods
 * 							defect 10505 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * DisabledPlacardTransactionData.
 *
 * @version	6.5.0 			06/14/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008
 */
public class DisabledPlacardTransactionData
	implements Serializable, Displayable
{
	RTSDate caTransTimestmp;
	int ciCacheTransAMDate;
	int ciCacheTransTime;
	int ciCustIdntyNo;
	int ciCustSeqNo;
	int ciOfcIssuanceNo;
	int ciSubstaId;
	int ciTransAMDate;
	int ciTransIdntyNo;
	int ciTransTime;
	int ciTransWsId;
	int ciVoidedTransIndi;
	String csTransCd;

	// defect 10505 
	String csTransId;
	// end defect 10505 

	String csTransEmpId;
	String csVoidTransEmpId;
	String csVoidTransId;
	Vector cvDsabldPlcrd;

	static final long serialVersionUID = -4940907496876211772L;

	/**
	 * Constructor
	 */
	public DisabledPlacardTransactionData()
	{
		super();
		cvDsabldPlcrd = new Vector();
	}

	/**
	 * Returns attributes for display in ShowCache
	 * 
	 * @return HashSet
	 */
	public Map getAttributes()
	{
		HashMap lhmHash = new HashMap();

		Field[] larrFields = this.getClass().getDeclaredFields();

		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				// defect 10191
				String lsFieldName = larrFields[i].getName();

				if (lsFieldName.equals("cvDsabldPlcrd"))
				{
					for (int j = 0; j < cvDsabldPlcrd.size(); j++)
					{
						UtilityMethods.addAttributesForObject(
							"xxDsabldPlcrdData" + (j + 1),
							((DisabledPlacardData) getDsabldPlcrd()
								.elementAt(j))
								.getAttributes(),
							lhmHash);
					}
				}
				else
				{
					lhmHash.put(lsFieldName, larrFields[i].get(this));
				}
				// end defect 10191
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;
	}

	/**
	 * Get value of ciCacheTransAMDate
	 * 
	 * @return  int
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}

	/**
	 * Get value of ciCacheTransTime
	 * 
	 * @return int
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}

	/**
	 * Get value of ciCustIdntyNo
	 * 
	 * @return int
	 */
	public int getCustIdntyNo()
	{
		return ciCustIdntyNo;
	}

	/**
	 * Get value of ciCustSeqNo
	 * 
	 * @return int
	 */
	public int getCustSeqNo()
	{
		return ciCustSeqNo;
	}

	/**
	 * Get value of ciCustSeqNo
	 * 
	 * @return Vector
	 */
	public Vector getDsabldPlcrd()
	{
		// defect 10191 
		if (cvDsabldPlcrd == null)
		{
			setDsabldPlcrd(new Vector());
		}
		// end defect 10191 
		return cvDsabldPlcrd;
	}

	/**
	 * Get value of ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Get value of ciSubstaId
	 * 
	 * @return int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Get value of ciTransAMDate
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Get value of csTransCd
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Get value of csTransEmpId
	 * 
	 * @return String
	 */
	public String getTransEmpId()
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
	 * Get value of ciTransIdntyNo
	 * 
	 * @return int
	 */
	public int getTransIdntyNo()
	{
		return ciTransIdntyNo;
	}

	/**
	 * Get value of ciTransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Get value of caTransTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getTransTimestmp()
	{
		return caTransTimestmp;
	}

	/**
	 * Get value of ciTransWsId
	 * 
	 * @return int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Get value of ciVoidedTransIndi
	 * 
	 * @return int
	 */
	public int getVoidedTransIndi()
	{
		return ciVoidedTransIndi;
	}

	/**
	 * Get value of EmpId performing Void
	 * 
	 * @return String
	 */
	public String getVoidTransEmpId()
	{
		return csVoidTransEmpId;
	}

	/**
	 * Get value of csVoidTransId
	 * 
	 * @return String
	 */
	public String getVoidTransId()
	{
		return csVoidTransId;
	}

	/**
	 * Set value of ciCacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}

	/**
	 * Set value of ciCacheTransTime
	 * 
	 * @param aiCacheTransTime
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
	}

	/**
	 * Set value of ciCustIdntyNo
	 * 
	 * @param aiCustIdntyNo
	 */
	public void setCustIdntyNo(int aiCustIdntyNo)
	{
		ciCustIdntyNo = aiCustIdntyNo;
	}

	/**
	 * Set value of ciCustSeqNo
	 * 
	 * @param aiCustSeqNo
	 */
	public void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}

	/**
	 * Set value of cvDsabldPlcrd
	 * 
	 * @param avDsabldPlcrd
	 */
	public void setDsabldPlcrd(Vector avDsabldPlcrd)
	{
		cvDsabldPlcrd = avDsabldPlcrd;
	}

	/**
	 * Set value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Set value of ciSubstaId
	 * 
	 * @param aiSubstaId
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}

	/**
	 * Set value of ciTransAMDate
	 * 
	 * @param aiTransAMDate
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * Set value of csTransCd
	 * 
	 * @param asTransCd
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * Set value of csTransEmpId
	 * 
	 * @param asTransEmpId
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}

	/**
	 * Set AbstractValue of TransId
	 * 
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}

	/**
	 * Set value of ciTransIdntyNo
	 * 
	 * @param aiTransIdntyNo
	 */
	public void setTransIdntyNo(int aiTransIdntyNo)
	{
		ciTransIdntyNo = aiTransIdntyNo;
	}

	/**
	 * Set value of ciTransTime
	 * 
	 * @param aiTransTime
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * Set value of caTransTimestmp
	 * 
	 * @param aaTransTimestmp
	 */
	public void setTransTimestmp(RTSDate aaTransTimestmp)
	{
		caTransTimestmp = aaTransTimestmp;
	}

	/**
	 * Set value of ciTransWsId
	 * 
	 * @param aiTransWsId
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * Set value of ciVoidedTransIndi
	 * 
	 * @param aiVoidedTransIndi
	 */
	public void setVoidedTransIndi(int aiVoidedTransIndi)
	{
		ciVoidedTransIndi = aiVoidedTransIndi;
	}

	/**
	 * Set value of EmpId performing Void
	 * 
	 * @param asVoidTransEmpId
	 */
	public void setVoidTransEmpId(String asVoidTransEmpId)
	{
		csVoidTransEmpId = asVoidTransEmpId;
	}

	/**
	 * Set value of csVoidTransId
	 * 
	 * @param asVoidTransId
	 */
	public void setVoidTransId(String asVoidTransId)
	{
		csVoidTransId = asVoidTransId;
	}

}
