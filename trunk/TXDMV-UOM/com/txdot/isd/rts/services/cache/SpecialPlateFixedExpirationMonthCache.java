package com.txdot.isd.rts.services.cache;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.SpecialPlateFixedExpirationMonthData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CacheConstant;

/*
 * SpecialPlateFixedExpirationMonthCache.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/26/2007	Created
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * The SpecialPlateFixedExpirationMonthCache class provides static methods to 
 * retrieve a particular SpecialPlateFixedExpirationMonthData.
 *
 * <p>SpecialPlateFixedExpirationMonthCache is being initialized and 
 * populated by the CacheManager when the system starts up.  The data 
 * will be stored in memory and thus will be accessible until the system
 * shuts down.
 *
 * @version Special Plates	03/26/2007
 * @author 	Kathy Harrell
 * <br>Creation Date: 		03/26/2007 16:45:00
 */

public class SpecialPlateFixedExpirationMonthCache
	extends AdminCache
	implements java.io.Serializable
{
	private static Hashtable shtSpecialPlateFixedExpirationMonth =
		new Hashtable();

	static final long serialVersionUID = -7628462073166561987L;

	/**
	 * SpecialPlateFixedExpirationMonthCache default constructor
	 */
	public SpecialPlateFixedExpirationMonthCache()
	{
		super();
	}
	/**
	 * Get the cache function id
	 *
	 * @return int 
	 */
	public int getCacheFunctionId()
	{
		return CacheConstant.SPCL_PLT_FXD_EXP_MO;
	}
	/**
	 * Get the internally stored hashtable
	 * 
	 * @return Hashtable 
	 */
	public Hashtable getHashtable()
	{
		return shtSpecialPlateFixedExpirationMonth;
	}

	/**
	 * Get the SpecialPlateFixedExpirationMonth data based on RegPltCd
	 * 
	 * @param  asRegPltCd 
	 * @return SpecialPlateFixedExpirationMonthData
	 */
	public static SpecialPlateFixedExpirationMonthData getRegPltCd(String asRegPltCd)
	{
		SpecialPlateFixedExpirationMonthData laReturnData = null;
		int liEffDate = new RTSDate().getYYYYMMDDDate();

		if (shtSpecialPlateFixedExpirationMonth
			.containsKey(asRegPltCd))
		{
			Vector lvData =
				(Vector) shtSpecialPlateFixedExpirationMonth.get(
					asRegPltCd);

			for (int i = 0; i < lvData.size(); i++)
			{
				SpecialPlateFixedExpirationMonthData laSpclPltFxdExpMoData =
					(SpecialPlateFixedExpirationMonthData) lvData.get(
						i);

				if ((liEffDate
					>= laSpclPltFxdExpMoData.getRTSEffDate())
					&& (liEffDate
						<= laSpclPltFxdExpMoData.getRTSEffEndDate()))
				{
					laReturnData = laSpclPltFxdExpMoData;
					break;
				}
			}
		}

		return laReturnData;
	}

	/**
	 * Clear and populate the hashtable with the vector
	 *
	 * @param avSpecialPlateFixedExpirationMonth Vector
	 * @throws RTSException
	 */
	public void setData(Vector avSpclPltFxdExpMo) throws RTSException
	{
		//reset data
		shtSpecialPlateFixedExpirationMonth.clear();

		for (int i = 0; i < avSpclPltFxdExpMo.size(); i++)
		{
			SpecialPlateFixedExpirationMonthData laSpclPltFxdExpMoData =
				(
					SpecialPlateFixedExpirationMonthData) avSpclPltFxdExpMo
						.get(
					i);
			String lsRegPltCd = laSpclPltFxdExpMoData.getRegPltCd();

			if (shtSpecialPlateFixedExpirationMonth
				.containsKey(lsRegPltCd))
			{
				Vector lvData =
					(Vector) shtSpecialPlateFixedExpirationMonth.get(
						lsRegPltCd);
				lvData.add(laSpclPltFxdExpMoData);
			}
			else
			{
				Vector lvData = new Vector();
				lvData.add(laSpclPltFxdExpMoData);
				shtSpecialPlateFixedExpirationMonth.put(
					lsRegPltCd,
					lvData);
			}
		}
	}
	/**
	 * Set the hashtable
	 * 
	 * @param ahtSpecialPlateFixedExpirationMonth Hashtable
	 */
	public void setHashtable(Hashtable ahtSpecialPlateFixedExpirationMonth)
	{
		shtSpecialPlateFixedExpirationMonth =
			ahtSpecialPlateFixedExpirationMonth;
	}
}
