package com.txdot.isd.rts.services.cache;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com
	.txdot
	.isd
	.rts
	.services
	.data
	.VehicleClassSpclPltTypeDescriptionData;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;

/*
 * VehicleClassSpclPltTypeDescCache.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/10/2007	Created
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/26/2007	Exclude VehClassCd not in Scope
 * 							defect 9085 Ver Special Plates 
 * K Harrell	04/11/2007	add isValidVehPltCombo()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/16/2007	add getVehClassRegClassDesc(String,int)
 * 	B. Brown				defect 9119,9085 Ver Special Plates
 * K Harrell	01/11/2011	exclude test for Effective Date 
 * 							modify getVehClassRegClassDesc(String,int) 
 * 							defect 10695 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
* The VehicleClassSpclPltTypeDescCache class provides static method to 
* retrieve a vector of VehicleClassSpclPltTypeDescriptionData objects based 
* on a key of vehClassCd
*
* <p>VehicleClassSpclPltTypeDescCache is being initialized and 
* populated by the CacheManager when the system starts up.  The data 
* will be stored in memory and thus will be accessible until the system
* shuts down.
*
* @version	6.7.0			01/11/2011
* @author	Kathy Harrell
* <br>Creation Date:		02/10/2007	19:24:00
*/
public class VehicleClassSpclPltTypeDescCache
	extends GeneralCache
	implements java.io.Serializable
{

	/**
	* A hashtable of vectors with vehClassCd as key
	*/

	private static Hashtable shtVehClassSpclPltTypeDesc =
		new Hashtable();

	static final long serialVersionUID = -8847150400699694598L;

	/**
	 * VehicleClassSpclPltTypeDescCache constructor comment.
	 */
	public VehicleClassSpclPltTypeDescCache()
	{
		super();
	}
	/**
	 * Returns the function id of the VehicleClassSpclPltTypeDescCache
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{

		return CacheConstant.VEH_CLASS_SPCL_PLT_TYPE_DESC_CACHE;
	}
	/**
	 * Get the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @return Hashtable
	 */
	public Hashtable getHashtable()
	{
		return shtVehClassSpclPltTypeDesc;
	}

	/**
	* Returns a vector of VehicleClassSpclPltTypeDescriptionData objects 
	* whose dates are effective.
	*
	* @param asVehClassCd String 
	* @return Vector 
	*/
	public static Vector getVehClassRegClassDescs(String asVehClassCd)
	{
		return getVehClassRegClassDescs(
			asVehClassCd,
			SystemProperty.getOfficeIssuanceCd());
	}
	/**
	* Returns a vector of VehicleClassSpclPltTypeDescriptionData objects 
	* whose dates are effective.
	*
	* @param asVehClassCd String 
	* @param aiOfcIssuanceCd in 
	* @return Vector 
	*/

	public static Vector getVehClassRegClassDescs(
		String asVehClassCd,
		int aiOfcIssuanceCd)
	{
		Object laObj = shtVehClassSpclPltTypeDesc.get(asVehClassCd);

		// defect 10695 
		// int liEffDate = new RTSDate().getYYYYMMDDDate();
		// end defect 10695

		Vector lvReturn = new Vector();

		if (laObj instanceof Vector)
		{
			Vector lvData = (Vector) laObj;

			lvReturn = new Vector();

			for (int i = 0; i < lvData.size(); i++)
			{
				VehicleClassSpclPltTypeDescriptionData laData =
					(
						VehicleClassSpclPltTypeDescriptionData) lvData
							.get(
						i);
						
				boolean lbOutOfScope =
					PlateTypeCache.isOutOfScopePlate(
						laData.getRegPltCd(),
						aiOfcIssuanceCd,
						SpecialPlatesConstant.ORDER_TYPE_EVENTS);
						
				// defect 10695 
				// If Not Out of Scope 
				// Add Element  
				if (!lbOutOfScope)
					//	&& liEffDate >= laData.getRTSEffDate()
					//	&& liEffDate <= laData.getRTSEffEndDate())
				{
					// end defect 10695 
					lvReturn.addElement(laData);
				}
			} // end for (int i 
		} // if (laObj...

		return lvReturn;
	}
	
	/**
	 * Return boolean designating if Special Plate RegPltCd is valid for 
	 * VehClassCd 
	 * 
	 * @param asVehClassCd
	 * @param asRegPltCd 
	 * @return boolean
	 */
	public static boolean isValidVehPltCombo(
		String asVehClassCd,
		String asRegPltCd)
	{
		boolean lbValid = false;
		Vector lvData =
			getVehClassRegClassDescs(
				asVehClassCd,
				SpecialPlatesConstant.INTERNET_OFCISSUANCECD);
		;
		if (lvData != null && lvData.size() != 0)
		{
			for (int i = 0; i < lvData.size(); i++)
			{
				VehicleClassSpclPltTypeDescriptionData laData =
					(
						VehicleClassSpclPltTypeDescriptionData) lvData
							.get(
						i);
				if (laData
					.getRegPltCd()
					.trim()
					.equals(asRegPltCd.trim()))
				{
					lbValid = true;
					break;
				}
			}
		}
		return lbValid;
	}
	/**
	* Returns a vector of strings of vehClassCds   
	*
	* @return Vector 
	*/

	public static Vector getAllVehClassWithSpclPlt()
	{
		Vector lvVector = new Vector();
		String lsElement = new String();

		for (Enumeration laEnum = shtVehClassSpclPltTypeDesc.keys();
			laEnum.hasMoreElements();
			)
		{
			lsElement = (String) laEnum.nextElement();

			// Return vector only if there exist plates available 
			// for the location 
			Vector lvPlates = getVehClassRegClassDescs(lsElement);
			if (lvPlates.size() > 0)
			{
				lvVector.addElement(lsElement);
			}
		}
		UtilityMethods.sort(lvVector);
		return lvVector;
	}
	/**
	 * Clear and populate the hashtable with the vector 
	 * 
	 * @param avVehClassRegClassData Vector 
	 */

	public void setData(Vector avVehClassSpclPltTypeDesc)
	{
		shtVehClassSpclPltTypeDesc.clear();
		for (int i = 0; i < avVehClassSpclPltTypeDesc.size(); i++)
		{
			VehicleClassSpclPltTypeDescriptionData laData =
				(
					VehicleClassSpclPltTypeDescriptionData) avVehClassSpclPltTypeDesc
						.get(
					i);

			String lsVehClassCd = laData.getVehClassCd().trim();
			if (shtVehClassSpclPltTypeDesc.containsKey(lsVehClassCd))
			{
				Vector lvData =
					(Vector) shtVehClassSpclPltTypeDesc.get(
						lsVehClassCd);
				lvData.add(laData);
			}
			else
			{
				Vector lvData = new Vector();
				lvData.addElement(laData);
				shtVehClassSpclPltTypeDesc.put(lsVehClassCd, lvData);
			}
		}

	}
	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable the hashtable
	 */
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtVehClassSpclPltTypeDesc = ahtHashtable;
	}
}
