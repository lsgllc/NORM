package com.txdot.isd.rts.services.util;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.RTSDateField;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.cache.CertifiedLienholderCache;
import com.txdot.isd.rts.services.cache.CommercialVehicleWeightsCache;
import com.txdot.isd.rts.services.cache.PostalStateCache;
import com.txdot.isd.rts.services.cache.RegistrationClassCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.TitleTypes;

/*
 *
 * CommonValidations.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		09/25/2001	Add state validation method
 * M Wang		08/20/2002	(CQU100004629)Modified isStringWithSSN() for
 *							Lienholder Updates.
 * J Rue		05/21/2003	Add VehModlYr to common validations
 *							defect 6130
 * K Harrell	07/30/2004	Add isRegion()
 *							defect 7385  Ver 5.2.1 
 * Ray Rowehl	06/21/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * S Johnston	06/22/2005	Fixed import of OfficeIdsCache &&
 * 							Code cleanup
 * 							no defect Ver 5.2.3
 * T Pederson	09/28/2006	Added convert_i_and_o_to_1_and_0() as 
 * 							non-static method to combine
 * 							convert_i_to_1() and convert_o_to_0().
 * 							add convert_i_and_o_to_1_and_0()
 * 							deprecate convert_i_to_1(), convert_o_to_0()
 * 							defect 8902 Ver Exempts
 * J Rue		01/30/2007	Add test for all special plates by 
 * 							trans codes.
 * 							add isSpecialPlatesAll()
 * 							defect 9086 Ver Special PLates
 * K Harrell	03/09/2007	delete isCnty(), isHQ(),isRegion()
 * 							defect 9085 Ver Special Plates 
 * J Rue 		03/23/2007	Calculate Registration window
 * 							add isInRenewalWindow()
 * 							defect 9086 Ver Special Plates
 * K Harrell	04/01/2007	Make static
 * 							modify isInRenewalWindow(),
 * 							convert_i_and_o_to_1_and_0()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/20/2007	add isValidEMail(String)
 * 							defect 9085 Ver special Plates
 * K Harrell	11/15/2007	Modify InRenewalWindow() so that don't have
 * 							 to check if Expired first.
 * 							modify isInRenewalWindow()
 * 							defect 9368 Ver Special Plates 2  
 * B Hargrove	07/11/2008	Add new method to determine if plate
 * 							is expired. Cannot renew vehicle with 
 * 							expired Vendor Plate.
 * 							add isPlateExpired()
 * 							defect 9689 Ver MyPlates_POS
 * K Harrell	08/29/2008	Add Military APO/FPO "states": AA, AE, AP
 * 							modify STATE_LIST 
 * 							defect 9155 Ver Defect_POS_B   
 * K Harrell	09/03/2008	delete isState() 
 * 							defect 9811 Ver Defect_POS_B
 * K Harrell	06/15/2009	add REG_EXPRESSION_PRINT_RANGE 
 * 							add isValidPrintRange() 
 * 							defect 10086 Ver Defect_POS_F
 * K Harrell	07/16/2009	Streamline code for Veh Modl Yr validation 
 * 							add isValidYearModel(String)
 * 							modify isInvalidYearModel(), 
 * 							  isValidYearModel(int)
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	12/17/009	add addRTSExceptionForInvalidMFCharacters(), 
 * 							 VALID_MF_CHARS
 * 							delete CommonValidations() (constructor)
 * 							defect 10299 Ver Defect_POS_H 
 * K Harrell	12/17/2009	Add additional valid 'states' 
 * 							modify STATE_LIST
 * 							defect 10306 Ver Defect_POS_H
 * K Harrell	12/28/2009	add isValidForMF()
 * 							modify addRTSExceptionForInvalidMFCharacters()
 * 							defect 10299 Ver Defect_POS_H
 * K Harrell	01/11/2010	Removed 'new' states for defect 10306 
 * 							defect 10306 Ver Defect_POS_H 
 * K Harrell	03/24/2010	Use PostalStateCache (from RTS_POSTAL_STATE)
 * 							 vs. HardCode State  
 * 							delete STATE_LIST
 * 							modify isValidState() 
 * 							defect 10396 Ver POS_640 
 * K Harrell	01/12/2011	add validateLienDate()
 * 							defect 10631 Ver 6.7.0 
 * K Harrell	10/15/2011	add addRTSExceptionForInvalidCntryStateCntry()
 * 							defect 11004 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */
/**
 * Contains static methods for common validations shared across
 * different modules
 *
 * @version	6.9.0 	 			10/15/2011
 * @author	Ashish Mahajan
 * <br>Creation Date:			09/21/2001 12:39:34
 */
public class CommonValidations
{

	private static final String REG_EXPRESSION_EMAIL =
		"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*$";

	// defect 10299
	private static final String VALID_MF_CHARS =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZ#():;?,.&*/+'";
	// end defect 10299

	// defect 10086 
	// Max Integer: 2147483647; limiting number of digits to 9  
	// to avoid errors on parseInt  	 
	private static final String REG_EXPRESSION_PRINT_RANGE =
		"\\s*\\d{1,9}+\\s*(((,|;|-)\\s*\\d{1,9}\\s*)?)+";
	// end defect 10086

	private static final int MAX_GROSS_WEIGHT = 80000;
	private static final int EIGHTEEN_EIGHTY = 1880;
	
	/**
	 * Adds new RTSException for Invalid Cntry or State/Cntry 
	 * RTSInput fields 
	 * 
	 * @param avRTSInputField
	 * @param aeRTSEx 
	 */
	public static void addRTSExceptionForInvalidCntryStateCntry(
			RTSInputField laField,
			RTSException aeRTSEx,
			boolean abRequired)
	{
		if (laField != null && laField instanceof RTSInputField)
		{
			if (laField.isEnabled())
			{
				if (laField.isEmpty())
				{
					if (abRequired)
					{
						aeRTSEx.addException(
								new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
								laField);
					}
				}
				else
				{
					String lsInput = laField.getText().trim();
					Vector lvInvalidChar = new Vector();

					for (int j = 0; j < lsInput.length(); j++)
					{
						char lchChar = lsInput.charAt(j);

						if (!Character.isLetter(lchChar) 
							|| Character.isDigit(lchChar))
						{
							String lsChar = new String(); 
							if (Character.isSpaceChar(lchChar)) 
							{
								lsChar = "' '";
							}
							else
							{
								lsChar = "" + lchChar;
							}
							if (!lvInvalidChar.contains(lsChar))
							{
								lvInvalidChar.add(lsChar);
							}
						}
					}
					if (!lvInvalidChar.isEmpty())
					{
						String lsInvalidChar = new String();
						int k = 0;
						for (; k < lvInvalidChar.size(); k++)
						{
							lsInvalidChar =
								lsInvalidChar
								+ lvInvalidChar.elementAt(k)
								+ " ";
						}
						String lsPrefix =
							"The following character"
							+ ((k == 1)
									? " is invalid: "
											: "s are invalid: ");
						lsInvalidChar =
							lsPrefix
							+ lsInvalidChar.substring(
									0,
									lsInvalidChar.length() - 1);
						String[] larrStrArray = new String[1];

						larrStrArray[0] = lsInvalidChar;

						aeRTSEx.addException(
								new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID, larrStrArray),
								laField);
					}
					else
					{
						if (lsInput.length() <2)
						{
							aeRTSEx.addException(
									new RTSException(ErrorsConstant.ERR_NUM_MIN_FIELD_LENGTH_2),
									laField);
						}
						
					}
				}
			}
		}
	}
	
	/**
	 * Adds new RTSException for Invalid MF Characters
	 * 
	 * @param avRTSInputField
	 * @param aeRTSEx 
	 */
	public static void addRTSExceptionForInvalidMFCharacters(
		Vector avRTSInputField,
		RTSException aeRTSEx)
	{
		if (avRTSInputField != null && !avRTSInputField.isEmpty())
		{
			for (int i = 0; i < avRTSInputField.size(); i++)
			{
				Object laObj = avRTSInputField.elementAt(i);

				if (laObj != null && laObj instanceof RTSInputField)
				{
					RTSInputField laField = (RTSInputField) laObj;

					if (laField.isEnabled() && !laField.isEmpty())
					{
						String lsInput = laField.getText();
						Vector lvInvalidChar = new Vector();

						for (int j = 0; j < lsInput.length(); j++)
						{
							char lchChar = lsInput.charAt(j);

							if (!Character.isDigit(lchChar)
								&& !Character.isSpaceChar(lchChar)
								&& lchChar != CommonConstant.CHAR_DASH)
							{
								String lsChar = "" + lchChar;

								// TODO Consider Regular Expression 
								if (VALID_MF_CHARS.indexOf(lsChar) < 0
									&& !lvInvalidChar.contains(lsChar))
								{
									// Special handling for HTML 
									if (lsChar.equals("<"))
									{
										lsChar = "&lt;";
									}
									lvInvalidChar.add(lsChar);
								}
							}
						}
						if (!lvInvalidChar.isEmpty())
						{
							String lsInvalidChar = new String();
							int k = 0;
							for (; k < lvInvalidChar.size(); k++)
							{
								lsInvalidChar =
									lsInvalidChar
										+ lvInvalidChar.elementAt(k)
										+ " ";
							}
							String lsPrefix =
								"The following character"
									+ ((k == 1)
										? " is invalid: "
										: "s are invalid: ");
							lsInvalidChar =
								lsPrefix
									+ lsInvalidChar.substring(
										0,
										lsInvalidChar.length() - 1);
							String[] larrStrArray = new String[1];

							larrStrArray[0] = lsInvalidChar;

							aeRTSEx.addException(
									new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID, 
											larrStrArray),
											laField);
						}
					}

				}
			}
		}
	}
	
	/**
	 * Calculates a vehicles gross weight based on the empty weight and 
	 * carrying capacity passed in.
	 *  
	 * @param asVehEmptyWt String
	 * @param asVehCarryingCap String
	 * @return int
	 */
	public static int calcGrossWeight(
		String asVehEmptyWt,
		String asVehCarryingCap)
	{
		int liVehGrossWt;
		if (asVehEmptyWt.equals(""))
		{
			asVehEmptyWt = "0";
		}
		if (asVehCarryingCap.equals(""))
		{
			asVehCarryingCap = "0";
		}
		liVehGrossWt =
			(Integer.parseInt(asVehEmptyWt)
				+ Integer.parseInt(asVehCarryingCap));
		return liVehGrossWt;
	}

	/**
	 * Convert "i" to "1" for VIN processing
	 * 
	 * @param asStrInput String
	 * @return String
	 * @deprecated
	 */
	public static String convert_i_to_1(String asStrInput)
	{
		if (asStrInput != null)
		{
			asStrInput = asStrInput.replace('i', '1');
			asStrInput = asStrInput.replace('I', '1');
		}

		return asStrInput;
	}

	/**
	 * Convert "o" to "0" for VIN processing
	 * 
	 * @param asStrInput - String
	 * @return String
	 * @deprecated
	 */
	public static String convert_o_to_0(String asStrInput)
	{
		if (asStrInput != null)
		{
			asStrInput = asStrInput.replace('o', '0');
			asStrInput = asStrInput.replace('O', '0');
		}
		return asStrInput;
	}

	/**
	 * Convert "i" to "1" and "o" to "0" for input string
	 * 
	 * @param asStrInput String
	 * @return String
	 */
	public static String convert_i_and_o_to_1_and_0(String asStrInput)
	{
		if (asStrInput != null)
		{
			asStrInput = asStrInput.replace('i', '1');
			asStrInput = asStrInput.replace('I', '1');
			asStrInput = asStrInput.replace('o', '0');
			asStrInput = asStrInput.replace('O', '0');
		}

		return asStrInput;
	}

	/**
	 * Compute the Title Type String.
	 * 
	 * @param aiTtlIndi int
	 * @return String
	 */
	public static String getTtlTypeString(int aiTtlIndi)
	{
		switch (aiTtlIndi)
		{
			case TitleTypes.INT_ORIGINAL :
				{
					return TitleTypes.STR_ORIGINAL;
				}
			case TitleTypes.INT_CORRECTED :
				{
					return TitleTypes.STR_CORRECTED;
				}
			case TitleTypes.INT_REGPURPOSE :
				{
					return TitleTypes.STR_REGPURPOSE;
				}
			case TitleTypes.INT_NONTITLED :
				{
					return TitleTypes.STR_NONTITLED;
				}
			default :
				{
					return null;
				}
		}
	}

	/**
	 * Is Invalid Year Model 
	 *  
	 * @param asVehModlYr 
	 * @return boolean 
	 */
	public static boolean isInvalidYearModel(String asVehModlYr)
	{
		// defect 10127 
		return !isValidYearModel(asVehModlYr);
		// end defect 10127 
	}

	/**
	 * Has the Plate expired.
	 *  
	 * @param aiMonth int
	 * @param aiYear int
	 * @param aiTransDate int
	 * @return boolean
	 */
	public static boolean isPlateExpired(
		int aiMonth,
		int aiYear,
		int aiTransDate)
	{
		int liCurrDate = RTSDate.getCurrentDate().getYYYYMMDDDate();
		int liExpDate = 0;
		int liExpDatePlusOne = 0;

		if (aiYear != 0 && aiMonth != 0)
		{
			if (aiMonth != 12)
			{
				liExpDatePlusOne =
					(aiYear * 10000) + ((aiMonth + 1) * 100) + 1;
			}
			else
			{
				liExpDatePlusOne = ((aiYear + 1) * 10000) + 100 + 1;
			}

			RTSDate lRTSExpDate =
				(new RTSDate(RTSDate.YYYYMMDD, liExpDatePlusOne)).add(
					RTSDate.DATE,
					-1);
			liExpDate = lRTSExpDate.getYYYYMMDDDate();
		}
		if (aiTransDate == 0)
		{
			//set the Transaction Date to current date
			aiTransDate = liCurrDate;
		}
		//Expired if transaction date is less than today or expiration date
		if (liCurrDate > liExpDate || aiTransDate > liExpDate)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Has the Registration expired.
	 *  
	 * @param aiMonth int
	 * @param aiYear int
	 * @return boolean
	 */
	public static boolean isRegistrationExpired(
		int aiMonth,
		int aiYear)
	{
		boolean lbRet = false;

		//check if current year is greater than year of expiration
		if (RTSDate.getCurrentDate().getYear() > aiYear)
		{
			lbRet = true;
		}
		else
		{
			// check if the current month is greater than month of 
			// expiration
			if ((RTSDate.getCurrentDate().getMonth() > aiMonth)
				&& (RTSDate.getCurrentDate().getYear() >= aiYear))
			{
				lbRet = true;
			}
		}
		return lbRet;
	}

	/**
	 * Has the Registration expired.
	 *  
	 * @param aiExpMth - int
	 * @param aiExpYr - int
	 * @param aiTransDate - int
	 * @return boolean
	 */
	public static boolean isRegistrationExpired(
		int aiExpMth,
		int aiExpYr,
		int aiTransDate)
	{
		int liExpDate = 0;
		int liExpDatePlusOne = 0;

		if (aiExpYr != 0 && aiExpMth != 0)
		{
			if (aiExpMth != 12)
			{
				liExpDatePlusOne =
					(aiExpYr * 10000) + ((aiExpMth + 1) * 100) + 1;
			}
			else
			{
				liExpDatePlusOne = ((aiExpYr + 1) * 10000) + 100 + 1;
			}

			RTSDate lRTSExpDate =
				(new RTSDate(RTSDate.YYYYMMDD, liExpDatePlusOne)).add(
					RTSDate.DATE,
					-1);
			liExpDate = lRTSExpDate.getYYYYMMDDDate();
		}
		if (aiTransDate == 0)
		{
			//set the Transaction Date to current date
			int liCurrDate = RTSDate.getCurrentDate().getYYYYMMDDDate();
			aiTransDate = liCurrDate;
		}
		//check if transaction date is greater than expiration date
		if (aiTransDate > liExpDate)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Is the string suspected of having a Social Security Number
	 * in it?
	 *  
	 * @param asString String
	 * @return boolean
	 */
	public static boolean isStringWithSSN(String asString)
	{
		boolean lbIsSSN = false;
		int liDigitCount = 0;
		if (asString != null)
		{
			for (int i = 0; i < asString.length(); i++)
			{
				char chCurr = asString.charAt(i);
				if (Character.isDigit(chCurr))
				{
					liDigitCount++;
				}
				//defect4629
				else if (
					chCurr == ' '
						|| chCurr == '#'
						|| chCurr == '-'
						|| chCurr == '/')
				{
					//do not count iDigitCount.
				}
				//end defect4629
				else
				{
					liDigitCount = 0;
				}
				if (liDigitCount == 9)
				{
					lbIsSSN = true;
					break;
				}
			}
		}
		return lbIsSSN;
	}
	
	/** 
	 * Is Valid for Country or StateCntry
	 * 
	 * @param avValues 
	 * @return boolean
	 */
	public static boolean isValidCntryStateCntry(Vector avValues)
	{
		boolean lbValid = true;

		if (avValues != null)
		{
			for (int i = 0; i < avValues.size(); i++)
			{
				Object laObj = avValues.elementAt(i);

				if (laObj != null && laObj instanceof String)
				{
					String lsFieldValue = (String) laObj;

					for (int j = 0; j < lsFieldValue.length(); j++)
					{
						char lchChar = lsFieldValue.charAt(j);

						lbValid = Character.isLetter(lchChar);
						
					}
				}

			}
		}
		return lbValid;
	}

	/**
	 * Return boolean to denote if supplied string is 
	 * Valid EMail Address
	 * 
	 * @param asEMail 
	 * @return boolean 
	 */
	public static boolean isValidEMail(String asEMail)
	{
		boolean lbValid = true;
		if (asEMail != null)
		{
			asEMail = asEMail.trim();
			if (asEMail.length() != 0)
			{
				lbValid =
					(asEMail.indexOf("@") >= 0)
						&& (asEMail.indexOf(".") >= 0);
				if (lbValid)
				{
					lbValid = asEMail.matches(REG_EXPRESSION_EMAIL);
				}
			}
		}
		return lbValid;
	}

	/**
	 * Return boolean to denote if supplied string is 
	 * Valid Print Range
	 * 
	 * @param asPrintRange 
	 * @return boolean 
	 */
	public static boolean isValidPrintRange(String asPrintRange)
	{
		boolean lbValid = false;
		if (asPrintRange != null)
		{
			asPrintRange = asPrintRange.trim();
			if (asPrintRange.length() != 0)
			{
				lbValid =
					asPrintRange.matches(REG_EXPRESSION_PRINT_RANGE);
			}
		}
		return lbValid;
	}

	/** 
	 * Is Valid for MF
	 * 
	 * @param avValues 
	 * @return boolean
	 */
	public static boolean isValidForMF(Vector avValues)
	{
		boolean lbValid = true;

		if (avValues != null)
		{
			for (int i = 0; i < avValues.size(); i++)
			{
				Object laObj = avValues.elementAt(i);

				if (laObj != null && laObj instanceof String)
				{
					String lsFieldValue = (String) laObj;

					for (int j = 0; j < lsFieldValue.length(); j++)
					{
						char lchChar = lsFieldValue.charAt(j);

						if (!Character.isDigit(lchChar)
							&& !Character.isSpaceChar(lchChar)
							&& lchChar != CommonConstant.CHAR_DASH)
						{
							String lsChar = "" + lchChar;

							if (VALID_MF_CHARS.indexOf(lsChar) < 0)
							{
								lbValid = false;
								break;
							}
						}
					}
				}

			}
		}
		return lbValid;
	}

	/**
	 * Check to see if state is valid.
	 * 
	 * @param asStateAbrvn String
	 * @return boolean
	 */
	public static boolean isValidState(String asStateAbrvn)
	{
		return PostalStateCache.isValidState(asStateAbrvn);
	}

	/**
	 * Validate the vehicle model year.
	 *  
	 * @param aiVehModlYr int
	 */
	public static boolean isValidYearModel(int aiVehModlYr)
	{
		// defect 10127
		return aiVehModlYr > EIGHTEEN_EIGHTY
			&& aiVehModlYr <= (RTSDate.getCurrentDate().getYear()) + 2;
		// end defect 10127 
	}

	/**
	 * Validate the vehicle model year.
	 *  
	 * @param asVehModlYr 
	 */
	public static boolean isValidYearModel(String asVehModlYr)
	{
		boolean lbReturn = false;

		try
		{
			lbReturn = isValidYearModel(Integer.parseInt(asVehModlYr));
		}
		catch (NumberFormatException aeNFEx)
		{
		}
		return lbReturn;
	}

	/**
	 * Verifies vehicle tonnage requirements.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 * @throws RTSException 
	 */
	public static void verifyVehTon(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();

		VehicleData laVehData =
			aaVehInqData.getMfVehicleData().getVehicleData();

		// Test if carrying capacity validation is NOT required
		RegistrationClassData laRegClassData =
			RegistrationClassCache.getRegisClass(
				laVehData.getVehClassCd(),
				laRegData.getRegClassCd(),
				aaVehInqData.getRTSEffDt());

		int liRetVal = laRegClassData.getCaryngCapValidReqd();
		if (liRetVal != 0)
		{
			// Test if gross weight is at maximum allowed
			if (laRegData.getVehGrossWt() != MAX_GROSS_WEIGHT)
			{
				// Test for minimum carrying capacity based on tonnage
				Dollar laVehTon = laVehData.getVehTon();
				CommercialVehicleWeightsData lCommVehWts =
					CommercialVehicleWeightsCache.getCommVehWts(
						laVehTon);
				Vector lvExceptionVector = new Vector();
				if (lCommVehWts == null)
				{
					lvExceptionVector.addElement(new RTSException(201));
					lvExceptionVector.addElement(new RTSException(200));
					if (lvExceptionVector.size() > 0)
					{
						throw new RTSException(lvExceptionVector);
					}
				}
				else
				{
					// Test if carrying capacity is at least minimum 
					//	allowed capacity
					if (laRegData.getVehCaryngCap()
						< lCommVehWts.getMinCaryngCap())
					{
						throw new RTSException(200);
					}
				}
			}
		}
	}

	/**
	 * Verifies if current date is within range for renewal..
	 * (Note: Refer to:
	 * 		RegistrationSpecialExemptions.verifyRenwlDateRange())
	 * 
	 * @param aiMonth 	int
	 * @param aiYear 	int
	 * @return boolean 
	 */
	public static boolean isInRenewalWindow(int aiMonth, int aiYear)
	{
		// defect 9368 
		// Modify to match Fees 
		boolean lbWithinWindow = false;

		int liCurrentMonths =
			(RTSDate.getCurrentDate().getYear() * 12)
				+ RTSDate.getCurrentDate().getMonth();

		int liExpirationMonths = (aiYear * 12) + aiMonth;

		if ((liExpirationMonths - liCurrentMonths < 3)
			&& (liExpirationMonths - liCurrentMonths >= 0))
		{
			lbWithinWindow = true;
		}
		return lbWithinWindow;
		// end defect 9368 
	}
	/** 
	 * Validate Lien Date
	 * 
	 * @param aaDateField
	 * @param asPermLienhldrId
	 * @param aeRTSEx 
	 */
	public static void validateLienDate(
		RTSDateField aaDateField,
		String asPermLienhldrId,
		RTSException aeRTSEx)
	{
		int liMinRTSEffDate =
			CertifiedLienholderCache.getMinRTSEffDate(asPermLienhldrId);

		if (aaDateField.getDate().getYYYYMMDDDate() < liMinRTSEffDate)
		{
			String[] larrStrArray = new String[1];

			larrStrArray[0] =
				" THE EARLIEST POSSIBLE LIEN DATE IS "
					+ new RTSDate(RTSDate.YYYYMMDD, liMinRTSEffDate)
						.toString()
					+ ".";

			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_LIEN_DATE_INVALID,
					larrStrArray),
				aaDateField);
		}
	}
}
