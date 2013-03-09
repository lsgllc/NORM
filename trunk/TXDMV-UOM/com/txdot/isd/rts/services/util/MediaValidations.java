package com.txdot.isd.rts.services.util;

import com.txdot.isd.rts.client.general.ui.RTSDateField;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.data.ValidateInventoryPattern;
import com.txdot.isd.rts.services.exception.RTSException;

/*
 *
 * MediaValidations.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2004	5.2.0 Merge.  Imported new class.
 * 							Ver 5.2.0
 * J Rue		01/27/2005	Change year validation from CurrYr+2 to
 *							CurrYr + 3 unless January then CurrYr + 2
 *							modify validateYear()
 *							defect 7936 Ver 5.2.2
 * J Rue		01/28/2005	Clean up code
 *							modify validateYear()
 *							defect 7936 Ver 5.2.2
 * J Rue		05/23/2005	Remove try/catch where applicable for
 * 							NumberFormatException
 * 							add validateCntyNo{String}
 * 							modify validateMonth(), validateDocNo()
 * 							modify validateAuditTrailTransid()
 * 							modify validateIndi()
 * 							defect 8212 Ver 5.2.3
 * J Rue		06/01/2005	Additional code cleanup
 * 							defect 7898 Ver 5.2.3
 * J Rue		07/01/2005	Add parameters year/month for year 
 *							validation
 * 							add validateYear(String, int, int)
 * 							deprecate validateYear(String)
 * 							deprecate validateDocNo()
 *							defect 8260 Ver 5.2.3
 *J Rue			01/15/2009	Copy isItmBreak() from
 *							GenDealerTitlePreliminaryReport
 *							add isItmBreak()
 *							defect 9045 Ver Defect_POS_D
 *J Rue			01/23/2009	Make call to ValidateInventoryPattern.
 *							calcInvUnknown through services
 *							modify isItmBreak()
 *							defect 9045 Ver Defect_POS_D
 * B Hargrove	05/22/2009  Add Flashdrive option to DTA. Change 
 * 							verbiage from 'diskette'.
 * 							refactor\rename\modify:
 * 							DiskValidations / MediaValidations 
 * 							defect 10075 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */

/** 
 * This class provides validation methods for diskette values.
 * 
 * @version	Defect_POS_F	05/22/2009
 * @author	Michael Abernethy
 * <br>Creation Date: 		08/06/2002
 */

public class MediaValidations
{
	private final static java.lang.String DEFAULT = "";
	
	/**
	 * @value "FORM31"
	 */
	private static final String FORM31 = "FORM31";
	
	/**
	 * Validate auditTrailTransid
	 * 
	 * @param asAuditTrailTransid java.lang.String
	 * @return long
	 */
	public static long validateAuditTrailTransid(
		String asAuditTrailTransid)
	{
		// defect 8212
		//	Replace try/catch with isNumeric()
		long llRtn = 0;
		if (UtilityMethods.isNumeric(asAuditTrailTransid)
			&& asAuditTrailTransid.indexOf(".") == -1)
		{
			llRtn = Long.parseLong(asAuditTrailTransid);
		}
			
		return llRtn;
		// end defect 8212
	}
	/**
	 * Validate City
	 * 
	 * @param asStrCty java.lang.String
	 * @return java.lang.String 
	 */
	public static String validateCity(String asStrCty)
	{
		if (asStrCty != null && asStrCty.length() <= 19)
		{
			return asStrCty.trim();
		}
		return DEFAULT;
	}
	/**
	* Validate Country
	*  
	* @param asStrCnty java.lang.String
	* @return java.lang.String 
	*/
	public static String validateCountry(String asStrCnty)
	{
		String lsRtn = DEFAULT;
		if (asStrCnty != null && asStrCnty.length() <= 4)
		{
			lsRtn = asStrCnty.trim();
		}
		return lsRtn;
	}
	/**
	 * Validate DocNo
	 * 
	 * @param asDocNo java.lang.String
	 * @return long
	 * @deprecated
	 */
	public static long validateDocNo(String asDocNo)
	{
		// defect 8212
		//	Replace try/catch with isNumeric()
		long llRtn = 0;
		if (UtilityMethods.isNumeric(asDocNo)
			&& asDocNo.indexOf(".") == -1)
		{
			llRtn = Long.parseLong(asDocNo);
		}
		// end defect 8212
			
		return llRtn;
	}
	/**
	* Validate Date
	* 
	* @param asStrDt java.lang.String
	* @return boolean 
	*/
	public static boolean validateDtFormat(String asStrDt)
	{
		return com
			.txdot
			.isd
			.rts
			.client
			.general
			.ui
			.RTSDateField
			.isValidDate(
			asStrDt);
	}
	/**
	 * Validate indicator
	 * 
	 * @param asIndi java.lang.String
	 * @return int 
	 */
	public static int validateIndi(String asIndi)
	{
		// defect 8212
		//	Replace try/catch with isNumeric()
		int liRtn = 0;
		if (UtilityMethods.isNumeric(asIndi)
			&& asIndi.indexOf(".") == -1)
		{
			liRtn = Integer.parseInt(asIndi);
		}
		// end defect 8212
			
		return liRtn;
	}
	/**
	 * Validate Month
	 * 
	 * @param asStrMo String
	 * @return java.lang.String 
	 */
	public static String validateMonth(String asStrMo)
	{
		String lsRtn = "0";
		// defect 8212
		//	Remove the try/catch box
		//	Validate month is all numeric whole number
		//	else return 0
		if (asStrMo != null)
		{
			asStrMo.trim();
		
			// Replace try/catch with UtilityMethods.isNumeric()
			//	and check for a decimal point(-1 means no decimal point)
			//	and month is in the range 01 - 12
			if (UtilityMethods.isNumeric(asStrMo)
				&& asStrMo.indexOf(".") == -1
				&& RTSDateField.isMonthValid(Integer.parseInt(asStrMo)))
			{
				lsRtn = asStrMo;
			}
		}		
		// end defect 8212

		return lsRtn;
	}
	/**
	* Validate Name or Street
	* 
	* @param asName java.lang.String
	* @return java.lang.String 
	*/
	public static String validateNameOrSt(String asName)
	{
		if (asName != null && asName.length() <= 30)
		{
			return asName.trim();
		}
		return DEFAULT;
	}
	/**
	* Validate State
	* 
	* @param asStrSt java.lang.String
	* @return java.lang.String 
	*/
	public static String validateState(String asStrSt)
	{
		if (asStrSt != null && CommonValidations.isValidState(asStrSt))
		{
			return asStrSt.trim();
		}
		return DEFAULT;
	}
	/**
	* Validate Year using Dealer Transaction Date
	*	If parameter asStrYr is more than 3 years from aiCurrYear
	*	 return 0 
	*	else 
	*	 return asStrYr
	* 
	* @param asStrYr java.lang.String
	* @param aiMonth int
	* @return java.lang.String  
	*/
	public static String validateYear(String asStrYr, 
						int aiCurrYear, int aiCurrMonth)
	{
		String lsRtn = "0";
		//	Year must be 4 characters long
		if (asStrYr != null && asStrYr.length() == 4)
		{
			// Use UtilityMethods.isNumeric() to verify only numerics
			//	and check for a decimal point
			//		(-1 indicates no decimal point)
			if (UtilityMethods.isNumeric(asStrYr)
				&& asStrYr.indexOf(".") == -1)
			{
				asStrYr.trim();
				int liDlrYr = Integer.parseInt(asStrYr);
		
				// defect 7936
				//	If current month is January 
				//	then set current year back 1 year..
				if (aiCurrMonth == 1)
				{
					aiCurrYear--;
				}

				// If paramter year is <= 3 years from current year,
				//	set return value to parameter year
				if (liDlrYr <= aiCurrYear + 3)
				{
					lsRtn = asStrYr;
				}
				// end defect 7936
			}  
		}
		return lsRtn;
	}
	/**
	* Validate Year
	*	If parameter year is more than 3 years 
	*	 from current date return 0 
	*	else 
	*	 return parameter year
	* 
	* @param asStrYr java.lang.String
	* @return java.lang.String  
	* @deprecated
	*/
	public static String validateYear(String asStrYr)
	{
		try 
		{
			RTSDate laCurrDate = RTSDate.getCurrentDate();
			int liCurrYr = laCurrDate.getYear();
			int liYr     = Integer.parseInt(asStrYr);

			// defect 7936
			//	If current month is January 
			//	then set current year back 1 year..
			if (laCurrDate.getMonth() == 1)
			{
			liCurrYr--;
			}

			// If paramter year is more than 3 years from current year,
			//	set return value to 0
			//if (liYr >= liCurrYr && liYr <= liCurrYr + 2)
			if (liYr > liCurrYr + 3)
			{
				asStrYr = DEFAULT;
			}
			// end defect 7936
			}
			catch (NumberFormatException laNumFormatException)
			{
				asStrYr = DEFAULT;
		}
	return asStrYr;	
	}
	/**
	* Validate ZipCode
	* 
	* @param asZipCd java.lang.String
	* @return java.lang.String   
	*/
	public static String validateZp(String asZipCd)
	{
		String lsRtn = DEFAULT;
		if (asZipCd != null && asZipCd.length() == 5)
		{
			asZipCd.trim();
			// Is Zip code value all numerics
			if (UtilityMethods.isNumeric(asZipCd)
				&& asZipCd.indexOf(".") == -1)
			{
				int liZipCd = Integer.parseInt(asZipCd);
				// Is Zip Code value greater than 0
				if (liZipCd > 0)
				{
					lsRtn = asZipCd;
				}
			}	
		}
		return lsRtn;
	}
	/**
	* Validate ZipCode+4
	* 
	* @param asZipCdP4 java.lang.String
	* @return java.lang.String 
	*/
	public static String validateZp4(String asZipCdP4)
	{
		String lsRtn = DEFAULT;
		if (asZipCdP4 != null && asZipCdP4.length() == 4)
		{
			asZipCdP4.trim();
			// Is Zip code value all numerics
			if (UtilityMethods.isNumeric(asZipCdP4)
				&& asZipCdP4.indexOf(".") == -1)
			{
				int liZipCdP4 = Integer.parseInt(asZipCdP4);
				// Is Zip Code value greater than 0
				if (liZipCdP4 > 0)
				{
					lsRtn = asZipCdP4;
				}
			}	
		}
		return lsRtn;
	}
	/**
	* Validate County Number
	* 
	* @param asCnty java.lang.String
	* @return java.lang.String 
	*/
	public static String validateCntyNo(String asCnty)
	{
		String lsRtn = "0";
		if (UtilityMethods.isNumeric(asCnty)
			&& asCnty.indexOf(".") == -1)
		{
			int liIndi = Integer.parseInt(asCnty);
			if (liIndi > 0 && liIndi <= 254)
			{
				lsRtn = asCnty;
			}
		}	
		return lsRtn;	
	}

	/**
	 * Determine if a SKIP occurred in the Form31 number sequence.
	 *
	 * @param asNewForm31 String 
	 * @return boolean
	 */
	public static boolean isItmBreak(String asPrevForm31, String asCurrForm31)
	{
		ValidateInventoryPattern laValidateInventoryPattern =
			new ValidateInventoryPattern();
		InventoryAllocationUIData laInvAllocUIData =
			new InventoryAllocationUIData();
	
		// Initialize laInvAllocUIData with values needed for 
		//	calculations.
		laInvAllocUIData.setItmCd(FORM31);
		laInvAllocUIData.setInvItmNo(asPrevForm31);
		laInvAllocUIData.setInvItmYr(0);
		laInvAllocUIData.setInvQty(2);
		try
		{
			// Compute next Form31No from 
			// call ValidateInventoryPattern.calcInvUnknown using Form31No
			laInvAllocUIData =
				(
					InventoryAllocationUIData) laValidateInventoryPattern
						.calcInvUnknownVI(laInvAllocUIData);
	
			// Compare the calculated Form31No to the next Form31No
			//	to be printed.
			// If they do not match then return true to print skip.
			if (!asCurrForm31.equals(laInvAllocUIData.getInvItmEndNo()))
			{
				return true;
			}
		}
		catch (RTSException aeRTSException)
		{
			return false;
		}
		return false;
	}	
}
