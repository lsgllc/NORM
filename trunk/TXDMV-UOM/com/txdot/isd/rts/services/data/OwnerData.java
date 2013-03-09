package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;

/*
 * OwnerData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * B. Brown		08/19/2005  Regain removed fields needed due to Complete
 * 							TransactionData being stored in table 
 * 							rts_itrnt_data.
 * 							add cOwnrAddr
 * 							add getCOwnerAddr() 
 * 							modify getOwnerAddr() 			
 *							defect 7899 Ver 5.2.3 
 * J Rue		08/19/2005	Switch deprecated methods
 *							getOwnrAddr(), getCOwnrAddr()
 *							refer to defect 7889
 *							deprecate getCOwnrAddr()
 *							defect 7898 Ver 5.2.3
 * K Harrell	05/21/2007	Removed fields from 7899 by design
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/03/2009	add getOwnerNameVector()
 * 							modify OwnerData()
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	07/03/2009	Implement NameAddressData
 * 							add getName1(), getName2()  (IRENEW) 
 * 							delete caOwnrAddr, get/set methods 
 * 							 getOwnerNameVector()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	10/14/2009	delete ciPrivacyOptCd, get/set methods
 * 							modify OwnerData()  
 * 					    	defect 10246 Ver Defect_POS_G
 * K Harrell	10/16/2009	Modify for ShowCache
 * 							add Displayable, getAttributes() 
 * 							defect 10191 Ver Defect_POS_G
 * K Harrell	06/09/2010	delete csOwnrTtlName1, csOwnrTtlName2
 * 							delete getName1(), getName2() 
 * 							defect 10491 Ver 6.5.0  
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get and set methods for 
 * OneTripData 
 * 
 * @version	6.5.0  			06/09/2010
 * @author	Administrator
 * <br>Creation Date:		08/22/2001 10:42:16   
 */

public class OwnerData extends NameAddressData implements Serializable
// defect 10191
, Displayable
// end defect 10191 
{
	// defect 10246
	//private int ciPrivacyOptCd;
	// end defect 10246 

	// IRENEW Only 
	// TODO Remove in release after POS_DEFECT_F 
	//private String csOwnrTtlName1 = new String();
	//private String csOwnrTtlName2 = new String();
	// End IRENEW Only 

	// String 
	private String csOwnrId;

	private final static long serialVersionUID = -6742896659549296508L;

	/**
	 * OwnerData constructor comment.
	 */
	public OwnerData()
	{
		super();

		// defect 10112 
		csOwnrId = new String();
		// end defect 10112

		// defect 10246 
		// ciPrivacyOptCd = CommonConstant.DEFAULT_PRIVACY_OPT_CD;
		// end defect 10246 
	}

	/**
	 * Get Object field attributes
	 * 
	 * @return Map
	 */
	public Map getAttributes()
	{
		Map lhmHash = new java.util.HashMap();

		Field[] larrFields = this.getClass().getDeclaredFields();

		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHash.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		NameAddressData laNAData = new NameAddressData(this);
		lhmHash.putAll(laNAData.getAttributes());
		return lhmHash;
	}

	//	defect 10491
	//	/** 
	//	 * getName1() method for those IRENEW objects.  
	//	 * Can be removed in release after 6.1.0 
	//	 * 
	//	 * @return String  
	//	 */
	//	public String getName1()
	//	{

	//		if (UtilityMethods.isEmpty(super.getName1()))
	//		{
	//			setName1(csOwnrTtlName1);
	//		}
	//	return super.getName1();
	//}

	//	/** 
	//	 * getName2() method for those IRENEW objects.  
	//	 * Can be removed in release after 6.1.0 
	//	 * 
	//	 * @return String  
	//	 */
	//	public String getName2()
	//	{
	//		if (UtilityMethods.isEmpty(super.getName2()))
	//		{
	//			setName2(csOwnrTtlName2);
	//		}
	//		return super.getName2();
	//	}
	// end defect 10491

	/**
	 * Set value of OwnrId
	 * 
	 * @param asOwnrId String
	 */
	public void setOwnrId(String asOwnrId)
	{
		csOwnrId = asOwnrId;
	}

	/**
	 * Return value of OwnrId
	 * 
	 * @return String
	 */
	public String getOwnrId()
	{
		return csOwnrId;
	}

	//	/**
	//	 * Return value of PrivacyOptCd
	//	 * 
	//	 * @return int
	//	 */
	//	public int getPrivacyOptCd()
	//	{
	//		return ciPrivacyOptCd;
	//	}

	//	/**
	//	 * Set value of PrivacyOptCd
	//	 * 
	//	 * @param aiPrivacyOptCd int
	//	 */
	//	public void setPrivacyOptCd(int aiPrivacyOptCd)
	//	{
	//		ciPrivacyOptCd = aiPrivacyOptCd;
	//	}
}
