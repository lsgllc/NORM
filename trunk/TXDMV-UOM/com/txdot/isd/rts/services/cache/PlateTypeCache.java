package com.txdot.isd.rts.services.cache;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.data.PlateTypeData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;

/*
 * PlateTypeCache.java 
 * 
 * (c) Texas Department of Transportation  2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/31/2007 	Created	
 * 							defect 9085 Ver Special Plates
 * B Hargrove	02/09/2007 	Added two methods to determine if a plate is
 * 							a Special Plate and to determine if a plate
 * 							is an Out of Scope Plate.
 * 							add	isOutOfScopePlate(), isSpclPlate() 
 * 							defect 9126 Ver Special Plates
 * K Harrell	04/24/2007	Added check to ItemCodesCache if not found
 * 							in PlateTypeCache
 * 							modify isOutOfScopePlateType()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/09/2007	Provide option to exclude PLPDLRxx plates
 * 							add getVectorOfSpecialPlateTypes(int,int,String,boolean)
 * 							add EXCLUDE_PLPDLR 
 * 							defect 9085 Ver Special Plates 
 * B Hargrove	06/13/2008 	Add a method to determine if this
 * 							plate type allows 'Customer Supplied' on
 * 							REG029 screen (ie: 'OLDPLT2').
 * 							add	isCustSuppliedAllowed() 
 * 							defect 9529 Ver MyPlates_POS
 * B Hargrove	07/11/2008 	Added method to determine if RegPltCd is a
 * 							'Vendor Plate'.
 * 							add	isVendorPlate() 
 * 							defect 9689 Ver MyPlates_POS
 * K Harrell	04/04/2011	add isAnnualPlate() 
 * 							defect 10785 Ver 6.7.1
 *----------------------------------------------------------------------
 */

/**
 * The PlateTypeCache class provides static methods to 
 * retrieve a particular PlateTypeData object based 
 * on different input parameters.
 *
 * <p>PlateTypeCache is being initialized and populated by
 * the CacheManager when the system starts up.  The data will be
 * stored in memory and thus will be accessible until the system
 * shuts down.
 * 
 * @version	MyPlates_POS	04/04/2011
 * @author	Kathy Harrell
 * <br>Creation Date: 		01/31/2007	18:47:00
 */
public class PlateTypeCache
	extends GeneralCache
	implements Serializable
{
	private static boolean EXCLUDE_PLPDLR = true;

	static final long serialVersionUID = -3404848320012369539L;
	/**
	 * A hashtable of vectors with regPltCd as key
	 */
	private static Hashtable shtPlateType = new Hashtable();

	/**
	 * PlateTypeCache constructor comment.
	 */
	public PlateTypeCache()
	{
		super();
	}
	/**
	 * Return the CacheConstant for this cache type.
	 * 
	 * @return int
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.PLT_TYPE_CACHE;
	}

	/**
	 * Returns the PlateTypeData object based on the input
	 * aiRegClassCd and aiEffDate.
	 *
	 * @param asRegPltCd String
	 * @param aiEffDate int
	 * @return PlateTypeData 
	 */

	public static PlateTypeData getPlateType(
		String asRegPltCd,
		int aiEffDate)
	{
		PlateTypeData laPlateTypeDataReturn = null;

		if (shtPlateType.containsKey(asRegPltCd))
		{
			Vector lvPlateTypeVector =
				(Vector) shtPlateType.get(asRegPltCd);

			for (int i = 0; i < lvPlateTypeVector.size(); i++)
			{
				PlateTypeData laPlateTypeData =
					(PlateTypeData) lvPlateTypeVector.get(i);

				if ((aiEffDate >= laPlateTypeData.getRTSEffDate())
					&& (aiEffDate <= laPlateTypeData.getRTSEffEndDate()))
				{
					laPlateTypeDataReturn = laPlateTypeData;
					break;
				}
			}
		}
		return laPlateTypeDataReturn;
	}

	/**
	 * 
	 * Returns the PlateTypeData object based on the input
	 * aiRegClassCd and current date
	 * 
	 * @param asRegPltCd
	 * @return PlateTypeData 
	 */
	public static PlateTypeData getPlateType(String asRegPltCd)
	{
		return getPlateType(
			asRegPltCd,
			new RTSDate().getYYYYMMDDDate());
	}
	/**
	 * Returns a vector of all Special Plates Data Objects
	 * based on the input aiRegClassCd and aiEffDate
	 *
	 * @param aiEffDate int
	 * @param aiOfcIssuanceCd
	 * @param asEventCd 
	 * @return Vector 
	 */

	public static Vector getVectorOfSpecialPlateTypes(
		int aiEffDate,
		int aiOfcIssuanceCd,
		String asEventCd)
	{
		return getVectorOfSpecialPlateTypes(
			aiEffDate,
			aiOfcIssuanceCd,
			asEventCd,
			!EXCLUDE_PLPDLR);
	}

	/**
	 * Returns a vector of all Special Plates Data Objects
	 * based on the input aiRegClassCd and aiEffDate
	 *
	 * @param aiEffDate int
	 * @param aiOfcIssuanceCd
	 * @param asEventCd 
	 * @return Vector 
	 */

	public static Vector getVectorOfSpecialPlateTypes(
		int aiEffDate,
		int aiOfcIssuanceCd,
		String asEventCd,
		boolean abOmitPLPDlr)
	{
		// Return vector for Special Plates
		Vector lvSpecialPlatesData = new Vector();

		// Vector of all Plate Types  
		Vector lvAllPlateType = new Vector(shtPlateType.values());

		for (int i = 0; i < lvAllPlateType.size(); i++)
		{
			Vector lvEachPltType = (Vector) lvAllPlateType.get(i);

			for (int j = 0; j < lvEachPltType.size(); j++)
			{
				PlateTypeData laPlateTypeData =
					(PlateTypeData) lvEachPltType.get(j);

				if (!(abOmitPLPDlr
					&& laPlateTypeData.getRegPltCd().startsWith(
						"PLPDLR")))
				{
					if ((aiEffDate >= laPlateTypeData.getRTSEffDate())
						&& (aiEffDate
							<= laPlateTypeData.getRTSEffEndDate())
						&& (laPlateTypeData.getPltOwnrshpCd() != null)
						&& (!laPlateTypeData
							.getPltOwnrshpCd()
							.equals(SpecialPlatesConstant.VEHICLE)))
					{
						String lsAuthCd =
							laPlateTypeData.getLocAuthCd(
								aiOfcIssuanceCd);
						if (lsAuthCd.equals(asEventCd)
							|| lsAuthCd.equals(
								SpecialPlatesConstant
									.BOTH_ORDER_AND_REGIS_EVENTS))
						{
							lvSpecialPlatesData.add(laPlateTypeData);
						}
					}
				}
			}
		}
		return lvSpecialPlatesData;
	}

	/**
	 * Returns a vector of all Special Plates Data Objects
	 * based on the input aiEffDate
	 *
	 * @param aiEffDate
	 * @return Vector 
	 */

	public static Vector getAllSpecialPlateTypes(int aiEffDate)
	{
		// Return vector for Special Plates
		Vector lvSpecialPlatesData = new Vector();

		// Vector of all Plate Types  
		Vector lvAllPlateType = new Vector(shtPlateType.values());

		for (int i = 0; i < lvAllPlateType.size(); i++)
		{
			Vector lvEachPltType = (Vector) lvAllPlateType.get(i);

			for (int j = 0; j < lvEachPltType.size(); j++)
			{
				PlateTypeData laPlateTypeData =
					(PlateTypeData) lvEachPltType.get(j);

				if ((aiEffDate >= laPlateTypeData.getRTSEffDate())
					&& (aiEffDate <= laPlateTypeData.getRTSEffEndDate())
					&& (laPlateTypeData.getPltOwnrshpCd() != null)
					&& (!laPlateTypeData
						.getPltOwnrshpCd()
						.equals(SpecialPlatesConstant.VEHICLE)))
				{

					lvSpecialPlatesData.add(laPlateTypeData);
				}
			}
		}
		return lvSpecialPlatesData;
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
		return shtPlateType;
	}

	/**
	 * Determines if Plate can be Customer Supplied
	 * 
	 * @param asRegPltCd 
	 * @return boolean 
	 */
	public static boolean isCustSuppliedAllowed(String asRegPltCd)
	{
		boolean lbCustSuppliedAllowed = false;
		PlateTypeData laPlateTypeData = getPlateType(asRegPltCd);

		if (laPlateTypeData != null)
		{

			if (laPlateTypeData.getUserPltNoIndi() == 1
				&& laPlateTypeData.getPltOwnrshpCd().equals(
					SpecialPlatesConstant.VEHICLE))
			{
				lbCustSuppliedAllowed = true;
			}
		}
		return lbCustSuppliedAllowed;
	}

	/**
	 * Determines if Plate is Annual Plate 
	 * 
	 * @param asRegPltCd 
	 * @return boolean 
	 */
	public static boolean isAnnualPlt(String asRegPltCd)
	{
		boolean lbAnnualPlt = false;

		PlateTypeData laPlateTypeData = getPlateType(asRegPltCd);

		if (laPlateTypeData != null)
		{
			lbAnnualPlt = laPlateTypeData.getAnnualPltIndi() == 1;
		}
		return lbAnnualPlt;
	}

	/**
	 * Determines if RegPltCd is Out of Scope plate
	 *
	 * @param asRegPltCd String
	 * @return boolean 
	 */

	public static boolean isOutOfScopePlate(String asRegPltCd)
	{
		return isOutOfScopePlate(
			asRegPltCd,
			SystemProperty.getOfficeIssuanceCd(),
			SpecialPlatesConstant.REGIS_TYPE_EVENTS);
	}

	/**
	 * Determines if Record is Available for Processing
	 * 
	 * @param asRegPltCd 
	 * @param aiOfcIssuanceCd
	 * @param asEventCd 
	 * @return boolean 
	 */

	public static boolean isOutOfScopePlate(
		String asRegPltCd,
		int aiOfcIssuanceCd,
		String asEventCd)
	{
		boolean lbOutOfScopePlate = false;

		PlateTypeData laPlateTypeData = getPlateType(asRegPltCd);

		if (laPlateTypeData != null)
		{
			String lsAuthCd =
				laPlateTypeData.getLocAuthCd(aiOfcIssuanceCd);

			if (!lsAuthCd.equals(asEventCd)
				&& !lsAuthCd.equals(
					SpecialPlatesConstant.BOTH_ORDER_AND_REGIS_EVENTS))
			{
				lbOutOfScopePlate = true;
			}
		}
		else
		{
			ItemCodesData laItmCdData =
				ItemCodesCache.getItmCd(asRegPltCd);

			if (laItmCdData != null
				&& laItmCdData.getInvProcsngCd() == 3)
			{
				lbOutOfScopePlate = true;
			}
		}
		return lbOutOfScopePlate;
	}
	/**
	 * Determines if RegPltCd is a Special Plate
	 *
	 * @param asRegPltCd String
	 * @return boolean 
	 */

	public static boolean isSpclPlate(String asRegPltCd)
	{
		boolean lbSpclPlate = false;

		if (asRegPltCd != null)
		{
			PlateTypeData laPlateTypeData = getPlateType(asRegPltCd);

			if (laPlateTypeData != null
				&& laPlateTypeData.getPltOwnrshpCd() != null
				&& !laPlateTypeData.getPltOwnrshpCd().equals(
					SpecialPlatesConstant.VEHICLE))
			{
				lbSpclPlate = true;
			}
		}
		return lbSpclPlate;
	}

	/**
	 * Determines if RegPltCd is a Vendor Plate
	 *
	 * @param asRegPltCd String
	 * @return boolean 
	 */

	public static boolean isVendorPlate(String asRegPltCd)
	{
		boolean lbVendorPlate = false;

		if (asRegPltCd != null)
		{
			PlateTypeData laPlateTypeData = getPlateType(asRegPltCd);

			if (laPlateTypeData != null
				&& laPlateTypeData.getVendorPltIndi() == 1)
			{
				lbVendorPlate = true;
			}
		}
		return lbVendorPlate;
	}

	/**
	 * Test main.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			Vector lvPlateTypeData = new Vector();

			PlateTypeData laPlateTypeData1 = new PlateTypeData();
			laPlateTypeData1.setRegPltCd("PSP");
			laPlateTypeData1.setRTSEffDate(20070201);
			laPlateTypeData1.setRTSEffEndDate(20070230);
			laPlateTypeData1.setMandPltReplAge(7);
			laPlateTypeData1.setOptPltReplAge(5);
			lvPlateTypeData.addElement(laPlateTypeData1);

			PlateTypeData laPlateTypeData2 = new PlateTypeData();
			laPlateTypeData2.setRegPltCd("PLP");
			laPlateTypeData2.setRTSEffDate(20070101);
			laPlateTypeData2.setRTSEffEndDate(20070130);
			laPlateTypeData2.setMandPltReplAge(7);
			laPlateTypeData2.setOptPltReplAge(5);
			lvPlateTypeData.addElement(laPlateTypeData2);

			PlateTypeCache laPlateTypeCache = new PlateTypeCache();
			laPlateTypeCache.setData(lvPlateTypeData);

			PlateTypeData laPlateTypeData =
				PlateTypeCache.getPlateType("PSP", 20070228);
			System.out.println(laPlateTypeData.getRegPltCd());
			System.out.println(laPlateTypeData.getRTSEffDate());
			System.out.println(laPlateTypeData.getRTSEffEndDate());
			System.out.println(laPlateTypeData.getMandPltReplAge());
			System.out.println(laPlateTypeData.getOptPltReplAge());
			System.out.println("done");
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}

	/**
	 * Clear and populate the hashtable with the vector avPlateTypeData
	 * 
	 * @param avPlateTypeData Vector
	 */
	public void setData(Vector avPlateTypeData)
	{
		//reset the hashtable
		shtPlateType.clear();
		for (int i = 0; i < avPlateTypeData.size(); i++)
		{
			PlateTypeData laPlateTypeData =
				(PlateTypeData) avPlateTypeData.get(i);

			String lsPrimaryKey = laPlateTypeData.getRegPltCd();

			if (shtPlateType.containsKey(lsPrimaryKey))
			{
				Vector lvPlateTypeData =
					(Vector) shtPlateType.get(lsPrimaryKey);
				lvPlateTypeData.add(laPlateTypeData);
			}
			else
			{
				Vector lvPlateTypeDataVector = new Vector();
				lvPlateTypeDataVector.add(laPlateTypeData);
				shtPlateType.put(lsPrimaryKey, lvPlateTypeDataVector);
			}
		}
		return;
	}

	/**
	 * Set the internally stored Hashtable.
	 *
	 * <P> Class that inherits from Admin cache is required
	 * to implement this method.
	 * 
	 * @param ahtHashtable Hashtable
	 */
	public void setHashtable(Hashtable ahtHashtable)
	{
		shtPlateType = ahtHashtable;
	}
}
