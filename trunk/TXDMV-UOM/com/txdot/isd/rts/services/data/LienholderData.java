package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.services.util.Displayable;
/*
 *
 * LienholderData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/03/2009	Created to replace LienholdersData   
 * 							Implement w/ AdminNameAddressData
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/17/2009	add isPopulated()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	10/16/2009	Modify for ShowCache
 * 							add Displayable, getAttributes()
 * 							defect 10191 Ver Defect_POS_G      
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get/set methods for 
 * LienholdersData
 * 
 * @version	Defect_POS_G 	10/16/2009
 * @author	Kathy Harrell
 * <br>Creation Date: 		07/03/2009 
 */
public class LienholderData
	extends AdminNameAddressData
	implements Serializable, Comparable
// defect 10191 
, Displayable
// end defect 10191 
{

	// boolean
	private boolean cbVTRAuth = false;

	// int 
	private int ciLienDate;
	private final static long serialVersionUID = 601147390096351483L;

	/**
	 * LienholderData constructor comment.
	 */
	public LienholderData()
	{
		super();
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

	/**
	 * Return value of LienDate
	 * 
	 * @return int
	 */
	public int getLienDate()
	{
		return ciLienDate;
	}

	/** 
	 * Return Vector of subset of data 
	 *  -- Lienholder Name, City/State/Cntry/Zip
	 * 
	 * @return Vector 
	 */
	public final Vector getLienHldrSubsetDataVector()
	{
		Vector lvVector = new Vector();
		lvVector.add(getName1());
		lvVector.add(getAddressData().getCityStateCntryZip());
		return lvVector;
	}

	/**
	 * 
	 * Return boolean to determine if Object is populated 
	 * 
	 * @return boolean 
	 */
	public boolean isPopulated()
	{
		return (ciLienDate != 0 || super.isPopulated());
	}

	/**
	 * Return value of VTRAuth
	 * 
	 * @return boolean
	 */
	public boolean isVTRAuth()
	{
		return cbVTRAuth;
	}

	/**
	 * Set value of LienData
	 * 
	 * @param aiLienDate
	 */
	public void setLienDate(int aiLienDate)
	{
		ciLienDate = aiLienDate;
	}

	/**
	 * Set value of VTRAuth
	 * 
	 * @param abVTRAuth boolean
	 */
	public void setVTRAuth(boolean abVTRAuth)
	{
		cbVTRAuth = abVTRAuth;
	}
}
