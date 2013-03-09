package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.InvalidLetterCache;
import com.txdot.isd.rts.services.cache.InventoryPatternsCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;

/*
 *
 * ValidateInventoryPattern.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/15/2002 	Catching NumberFormatExceptions in long inv 
 *							numbers CQU100003927
 * MAbs			06/06/2002	Checked error codes in validateAlpha 
 *							CQU100004209
 * Ray Rowehl	06/10/2002	Handle constants in the alpha sections.
 *							CQU100004254
 * Ray Rowehl	06/24/2002	Provide a better fix to constants.
 *							CQU100004302
 * Min Wang	    07/07/2003  Reflow Server Business to reduce the number 
 *							of quiries required to issue an item.
 *                          Moved calcInvNo(), calcInvEndNo(), 
 *							calcInvUnknow(), consolidateAlpha(), 
 *                          and findInvldLtrCombos() from 
 * 							server.inventory.
 *							InventoryServerBusiness into
 *                 			services.data.ValidateInventoryPattern. 
 *                          Modified calcInvNo() and calcInvEndNo() to 
 * 							use setCalcInv() to see if calculation 
 *                          is needed. Also changed these methods from 
 *							private into public.
 *                          Defect 6076.
 * Min Wang		05/07/2004  Fix that user is not able to accept the 
 *							following plate pattern bb1 
 *							(Protect and Serve) on. 
 *							modify validateAlpha()
 *							defect 6902 Ver 5.2.0
 * Min Wang		05/11/2004  Fix Calculation problem on the end number 
 *							for APPRTK.
 *							modify calcInvEndNo()
 *							defect 6617 Ver 5.2.0	
 * Ray Rowehl	04/05/2005	Fix problem with interpretation of an 
 * 							error number.  011 got interpreted as 9!
 * 							modify getInvNoPatrn()
 * 							defect 8015 Ver 5.2.3
 * Min Wang		04/20/2005	Fix Calculation problem on the end number 
 *							for truck plates.
 *							modify calcInvEndNo()
 *							defect 7918	Ver 5.2.3
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3	
 * Ray Rowehl	07/25/2005	Implement serializable to run with 
 * 							InventoryPatternTest.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/02/2005	Java 1.4 code cleanup.
 * 							work on constants.
 * 							extract a new method.
 * 							add populateVectorWithDefaultValues()
 * 							defect 7889 Ver 5.2.3	
 * Ray Rowehl	08/16/2005	Use CommonConstants for ints and letters
 * 							defect 7890 Ver 5.2.3	
 * Min Wang		09/28/2005	Add check for item code matches
 * 							add chkItmForPatternMatch()
 * 							defect 8396 Ver 5.2.3 
 * Min Wang		02/16/2007	pass InvAllocData to ValidateItmNoInput 
 * 							for Special Plates.
 * 							Rename InventoryAllocationUIData to 
 * 							InventoryAllocationData due to 
 * 							ComputeInventoryData object 
 * 							renaming.
 * 							modify validateItmNoInput()
 * 							defect 9117 Ver Special Plates	
 * Min Wang		03/08/2007	Fix receiving 2 different patterns 
 * 							in one receive.
 * 							modify calcInvUnknown()
 * 							defect 9136 Ver Special Plates
 * Ray Rowehl	03/12/2007	Allow qty to go to int max.
 * 							modify calcInvUnknown()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/08/2007	Change calculation method for reformulating 
 * 							the alpha component.
 * 							modify calcInvEndNo(), calcInvNo(),
 * 								decimalizeAlpha(), findInvldLtrCombos()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/09/2007	Assume that NumberFormatEx's in calcInvNo
 * 							are due to pattern errors.  Throw an 
 * 							RTSException.
 * 							Have ValidateItemNoInput check PLP's to 	
 * 							ensure they do not match any patterns.
 *							modify calcInvNo(), validateItmNoInput() 
 *							defect 9116 Ver Special Plates
 * Ray Rowehl	05/17/2007	If no patterns found for the year specified,
 * 							check to see if any pattern will work.
 * 							modify chkItmForPatternMatch()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/25/2007	Modify the plp check to more properly
 * 							check to see of the plate is user defined.
 * 							delete OLD_PLATE, PERSONALIZED_PLATE,
 * 								 RADIO_OPERATOR_PLATE
 * 							modify chkIfItmPLPOrOLDPLTOrROP()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/30/2007	Change the error number on PLP pattern match
 * 							to use the generic error number.
 * 							modify validateItmNoInput()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/01/2007	Add method to check to see if the item 
 * 							code allows duplicates.
 * 							add chkIfItmAllowsDups()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/26/2007	Review data setup for VI handling.
 * 							modify calcInvUnknownVI()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/03/2007	Do not check for patterns on ROP plates.
 * 							modify validateItmNoInput()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/06/2007	Clean up duplication of checks for negative
 * 							quantity.
 * 							modify calcInvUnknown()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	08/03/2007	At least log PLP rejections on pattern to
 * 							rtsapp.log!
 * 							modify validateItmNoInput()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	08/14/2007	VTR would like to see the 1004 error message
 * 							instead of 1010 when a plp matches a pattern. 
 * 							modify validateItmNoInput()
 * 							defect 9252 Ver Special Plates
 * Min Wang		01/22/2008	add pattern detection for Hoops version of 
 * 							plate number.
 * 							modify validateItmNoInput()
 * 							defect 9516 3_Amigos Prep
 * Min Wang		01/29/2008	Add an interfacing method to handle 
 * 							checking for Hoops pattern match.
 * 							add getMatchingPatterns()
 * 							modify validateItmNoInput()
 * 							defect 9516 3_Amigos Prep
 * Ray Rowehl	01/08/2009	Change invalid letter mid range check to 
 * 							use an adjusted value if the "min" is not 
 * 							"BBBB" if the alpha length is longer than 3.
 * 							modify findInvldLtrCombos()
 * 							defect 9714 Ver Defect_POS_D
 * Ray Rowehl	01/12/2009	Add handling of end range.
 * 							modify findInvldLtrCombos()
 * 							defect 9714 Ver Defect_POS_D
 * Ray Rowehl	02/25/2009	Remove changes.  It does not help.
 * 							modify findInvldLtrCombos()
 * 							defect 9714 Ver Defect_POS_D
 * Ray Rowehl	08/11/2009	Modify invalid letter combo handling for 
 * 							ranges where the begin number does not begin
 * 							with just the first letter of valid letters.
 * 							For example, there is an FRP pattern 
 * 							that starts with BXSB01.
 * 							modify findInvldLtrCombos()
 * 							defect 9714 Ver Defect_POS_F
 * Min Wang		10/08/2009  Allow table entries for the two position 
 * 							invalid letter combos to work.
 * 							delete COMBINATION_PLATE,
 * 							TRUCK_PLATE
 * 							modify findInvldLtrCombos()
 * 							defect 10248 Ver Defect_POS_G
 * Min Wang		10/16/2009	Remove constant handling when the alpha 
 * 							position is calculated.
 * 							modify calcInvEndNo()
 * 							defect 10247 Ver Defect_POS_G
 * Min Wang		10/20/2009	Do walkthru clean up.
 * 							defect 10248 Ver Defect_POS_G
 * ---------------------------------------------------------------------
 */

/**
 * Contains methods used for validating inventory patterns, checking for
 * personalized plates, and inventory number decimalization.
 * 
 * <p>This class is not part of the server because it needs to be used 
 * by validate inventory when the database or server is down.
 *
 * @version	Defect_POS_G	10/20/2009
 * @author	Charlie Walker
 * @author	Ray Rowehl
 * @author 	Min Wang
 * <br>Creation Date:		11/20/2001 15:40:12
 */

public class ValidateInventoryPattern implements Serializable
{
	private static final char CHAR_ZERO = '0';

	private static final String ALPHA_POS_3_STR = "3";
	private static final String ALPHA_POS_2_STR = "2";
	private static final String ALPHA_POS_1_STR = "1";
	// defect 10248
	// private static final String COMBINATION_PLATE = "CP";
    // end 10248
	private static final String MSG_CALCINVENDNO_BEGIN =
		"calcInvEndNo - Begin";
	private static final String MSG_CALCINVENDNO_END =
		"calcInvEndNo - End";
	private static final String MSG_CALCINVNO_BEGIN =
		"calcInvNo - Begin";
	private static final String MSG_CALCINVNO_END = "calcInvNo - End";
	private static final String MSG_CALCINVUNKNOWN_BEGIN =
		"calcInvUnknown - Begin";
	private static final String MSG_CALCINVUNKNOWN_END =
		"calcInvUnknown - End";
	private static final String MSG_CONSOLIDATEALPHA_BEGIN =
		"consolidateAlpha - Begin";
	private static final String MSG_CONSOLIDATEALPHA_END =
		"consolidateAlpha - End";
	private static final String MSG_FINDINVLDLTRCOMBOS_BEGIN =
		"findInvldLtrCombos - Begin";
	private static final String MSG_FINDINVLDLTRCOMBOS_END =
		"findInvldLtrCombos - End";

	// defect 9116
	// no longer used
	//private static final String PERSONALIZED_PLATE = "PLP";
	//private static final String OLD_PLATE = "OLDPLT";
	private static final String RADIO_OPERATOR_PLATE = "ROP";
	// end defect 9116
	// defect 10248
	//private static final String TRUCK_PLATE = "TKP";
	// end defect 10248
	private static final String PATTERN_IS_ALPHA = "A";
	private static final String PATTERN_IS_NUMBER = "N";

	private static final int ALPHA_POS_1 = 1;
	private static final int ALPHA_POS_2 = 2;
	private static final int ALPHA_POS_3 = 3;
	private static final int ALPHA_HIGHEST_POSITION = 3;
	private static final int DEFAULT_ITEM_VECTOR_SIZE = 11;
	private static final int NUM_POS_1 = 4;
	private static final int NUM_POS_2 = 5;
	private static final int NUM_POS_3 = 6;

	private final static long serialVersionUID = -773845545298527859L;

	/**
	 * ValidateInventoryPattern constructor comment.
	 */
	public ValidateInventoryPattern()
	{
		super();
	}

	/**
	 * Used to calculate the InvItmEndNo from the InvItmNo and the 
	 * InvQty.  
	 * <br>Note: The method ValidateItmNoInput needs to be called 
	 * prevous to this method.
	 * 
	 * These vectors holds data about the inventory pattern
	 * <ol>
	 * <li>Element  0 - (String) the alphanumeric inventory pattern 
	 *                for that item
	 * <li>Element  1 - (String) the first group of characters
	 * <li>Element  2 - (String) the second group of characters
	 * <li>Element  3 - (String) the third group of characters
	 * <li>Element  4 - (String) the first group of numeric characters
	 * <li>Element  5 - (String) the second group of numeric characters
	 * <li>Element  6 - (String) the third group of numeric characters
	 * <li>Element  7 - (String) the inventory item number
	 * <li>Element  8 - (Integer) the decimalized value for the first 
	 *                 group of characters
	 * <li>Element  9 - (Integer) the decimalized value for the second
	 *                 group of characters
	 * <li>Element 10 - (Integer) the decimalized value for the third
	 *                group of characters
	 * <eol>
	 *
	 * @param aaComputeInvData ComputeInventoryData
	 * @return Object
	 * @throws RTSException  
	 */
	public Object calcInvEndNo(ComputeInventoryData aaComputeInvData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_CALCINVENDNO_BEGIN);

		Vector lvInvNoPatrn = aaComputeInvData.getInvNoPatrn();
		Vector lvMinInvNoPatrn = aaComputeInvData.getMinInvNoPatrn();
		Vector lvMaxInvNoPatrn = aaComputeInvData.getMaxInvNoPatrn();
		Vector lvEndInvNoPatrn =
			(Vector) UtilityMethods.copy(
				aaComputeInvData.getInvNoPatrn());
		Vector lvOrigInvNoPatrn = aaComputeInvData.getOrigInvNoPatrn();
		Vector lvOrigMinInvNoPatrn =
			aaComputeInvData.getOrigMinInvNoPatrn();
		Vector lvOrigMaxInvNoPatrn =
			aaComputeInvData.getOrigMaxInvNoPatrn();

		//Begin calculation, setCalcInv(false)
		aaComputeInvData.getInvAlloctnData().setCalcInv(false);
		try
		{
			if (ValidateInventoryPattern
				.chkIfItmPLPOrOLDPLTOrROP(
					aaComputeInvData.getInvAlloctnData().getItmCd()))
			{
				String lsInvItmNo =
					aaComputeInvData.getInvAlloctnData().getInvItmNo();
				aaComputeInvData.getInvAlloctnData().setInvItmEndNo(
					lsInvItmNo);
			}
			else
			{
				// Declare the variables used in the end number 
				// calculation
				ValidateInventoryPattern laValidateInvPatrn =
					new ValidateInventoryPattern();
				String lsInvItmPatrnCd =
					String.valueOf(
						aaComputeInvData
							.getInvPatrnsData()
							.getInvItmPatrnCd());
				int liPatrnCd = 0;
				String lsTmpAlphaPatrnCd = new String();
				double ldBase =
					aaComputeInvData
						.getInvPatrnsData()
						.getValidInvLtrs()
						.length();
				int liDecimalAlpha = 0;
				int liDecimalMinAlpha = 0;
				int liDecimalMaxAlpha = 0;
				int liNum = 0;
				int liNumMin = 0;
				int liNumMax = 0;
				int liAlphaInvalid = 0;
				int liRangeAlphaInvalid = 0;
				int liModValue = 0;
				int liDivValue = 0;
				//defect 7918
				//int liAmount = 0;
				//int liMaxValue = 0;
				//defect 7918
				String lsAlphaChar = new String();
				String lsCalcInvItmNo = new String();
				boolean lbNegativeQty = false;

				// Calculate the end number using the begin number and 
				// quantity
				if (aaComputeInvData.getInvAlloctnData().getInvQty()
					>= 0)
				{
					lbNegativeQty = false;
					liDivValue =
						aaComputeInvData
							.getInvAlloctnData()
							.getInvQty()
							- 1;
				}
				else
				{
					lbNegativeQty = true;
					liDivValue =
						-1
							* aaComputeInvData
								.getInvAlloctnData()
								.getInvQty();
				}

				for (int i = 0; i < lsInvItmPatrnCd.length() + 1; i++)
				{
					if (liDivValue == 0)
					{
						break;
					}
					else if (i >= lsInvItmPatrnCd.length())
					{
						RTSException leRTSEx =
							new RTSException(
								ErrorsConstant
									.ERR_NUM_COMPUTED_NUMBER_INVALID);
						throw leRTSEx;
					}

					liPatrnCd =
						Integer.parseInt(
							lsInvItmPatrnCd.substring(i, i + 1));

					// Test if strings can/should be consolidated
					if (liPatrnCd <= ALPHA_HIGHEST_POSITION)
					{
						Vector lvReturnData =
							new Vector(
								consolidateAlpha(
									aaComputeInvData,
									i,
									lsInvItmPatrnCd,
									liPatrnCd));
						liPatrnCd =
							((Integer) lvReturnData
								.get(CommonConstant.ELEMENT_0))
								.intValue();
						i =
							((Integer) lvReturnData
								.get(CommonConstant.ELEMENT_1))
								.intValue();
						lsTmpAlphaPatrnCd =
							(String) lvReturnData.get(
								CommonConstant.ELEMENT_2);
					}

					// Remove constants before Decimalizing the string
					String lsItmAlpha = CommonConstant.STR_SPACE_EMPTY;
					String lsMinAlpha = CommonConstant.STR_SPACE_EMPTY;
					String lsMaxAlpha = CommonConstant.STR_SPACE_EMPTY;
					int liConstantIndx = 0;
					// loop through the letters to place constants

					// Test which value to increment/decrement
					switch (liPatrnCd)
					{
						case ALPHA_POS_1 :
							{
								// Check if the decimalized value for Alpha,
								// AlphaMin, and AlphaMax need to be 
								//  calculated
								// set up position for alpha 1 
								//  decimalization if needed
								if (lvInvNoPatrn.size()
									< liPatrnCd
										+ CommonConstant.ELEMENT_8)
								{
									populateVectorWithDefaultValues(
										lvInvNoPatrn,
										lvMinInvNoPatrn,
										lvMaxInvNoPatrn,
										lvOrigInvNoPatrn,
										lvOrigMinInvNoPatrn,
										lvOrigMaxInvNoPatrn,
										liPatrnCd
											+ CommonConstant.ELEMENT_7);
								}
								// Use new decimalization routine.  
								//  pass the min and max also.
								lsItmAlpha =
									(String) lvInvNoPatrn.get(
										liPatrnCd);
								lsMinAlpha =
									(String) lvMinInvNoPatrn.get(
										liPatrnCd);
								lsMaxAlpha =
									(String) lvMaxInvNoPatrn.get(
										liPatrnCd);

								liDecimalAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsItmAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalAlpha));
								lvOrigInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalAlpha));
								liDecimalMinAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsMinAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvMinInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMinAlpha));
								lvOrigMinInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMinAlpha));
								liDecimalMaxAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsMaxAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvMaxInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMaxAlpha));
								lvOrigMaxInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMaxAlpha));
								liDecimalAlpha =
									((Integer) lvInvNoPatrn
										.get(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7))
										.intValue();
								liDecimalMinAlpha =
									((Integer) lvMinInvNoPatrn
										.get(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7))
										.intValue();
								liDecimalMaxAlpha =
									((Integer) lvMaxInvNoPatrn
										.get(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7))
										.intValue();

								// Test if invalid letter combinations
								// need to be found
								Vector lvInvldLtrIndx =
									findInvldLtrCombos(
										aaComputeInvData,
										liPatrnCd);
								liAlphaInvalid =
									((Integer) lvInvldLtrIndx
										.get(CommonConstant.ELEMENT_0))
										.intValue();
								liRangeAlphaInvalid =
									((Integer) lvInvldLtrIndx
										.get(CommonConstant.ELEMENT_1))
										.intValue();

								// Calculate the Div/Mod 
								int liAlphaLen =
									((String) lvMaxInvNoPatrn
										.get(liPatrnCd))
										.length();
								int liAlphaNum =
									liDecimalMaxAlpha
										- liDecimalMinAlpha
										- liRangeAlphaInvalid
										+ 1;
								liModValue = liDivValue % liAlphaNum;
								liDivValue = liDivValue / liAlphaNum;
								if (!lbNegativeQty)
								{
									liDecimalAlpha =
										liDecimalAlpha + liModValue;
									if (liDecimalAlpha
										> liDecimalMaxAlpha)
									{
										liDivValue = liDivValue + 1;
										liDecimalAlpha =
											liDecimalAlpha
												- (liDecimalMaxAlpha + 1);
									}
								}
								else
								{
									liDecimalAlpha =
										liDecimalAlpha - liModValue;
									if (liDecimalAlpha
										< liDecimalMinAlpha)
									{
										liDivValue = liDivValue + 1;
										liDecimalAlpha =
											-1
												* (liDecimalAlpha
													- liDecimalMinAlpha
													+ 1);
									}
								}
								// Add invalid combos until correct # 
								// for Positive or Negative
								if (!lbNegativeQty)
								{
									liDecimalMinAlpha =
										((Integer) lvOrigInvNoPatrn
											.get(
												liPatrnCd
													+ CommonConstant
														.ELEMENT_7))
											.intValue();
									while (liDecimalMinAlpha
										!= liDecimalAlpha)
									{
										lvInvNoPatrn.set(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7,
											new Integer(liDecimalAlpha));
										lvMinInvNoPatrn.set(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7,
											new Integer(liDecimalMinAlpha));
										lvInvldLtrIndx =
											findInvldLtrCombos(
												aaComputeInvData,
												liPatrnCd);
										liAlphaInvalid =
											((Integer) lvInvldLtrIndx
												.get(
													CommonConstant
														.ELEMENT_0))
												.intValue();
										liDecimalMinAlpha =
											liDecimalAlpha;
										liDecimalAlpha =
											liDecimalAlpha
												+ liAlphaInvalid;
									}
								}
								else
								{
									liDecimalMaxAlpha =
										((Integer) lvOrigInvNoPatrn
											.get(
												liPatrnCd
													+ CommonConstant
														.ELEMENT_7))
											.intValue();
									while (liDecimalMaxAlpha
										!= liDecimalAlpha)
									{
										lvInvNoPatrn.set(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7,
											new Integer(liDecimalAlpha));
										lvMaxInvNoPatrn.set(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7,
											new Integer(liDecimalMaxAlpha));
										lvInvldLtrIndx =
											findInvldLtrCombos(
												aaComputeInvData,
												liPatrnCd);
										liAlphaInvalid =
											((Integer) lvInvldLtrIndx
												.get(
													CommonConstant
														.ELEMENT_0))
												.intValue();
										liDecimalMaxAlpha =
											liDecimalAlpha;
										liDecimalAlpha =
											liDecimalAlpha
												- liAlphaInvalid;
									}
								}

								// Reformulate the alpha string
								// reset the alphachar before building 
								// it
								lsAlphaChar =
									CommonConstant.STR_SPACE_EMPTY;
								liConstantIndx = -1;
								for (int j = (liAlphaLen - 1);
									j > -1;
									j--)
								{
									liConstantIndx = liConstantIndx + 1;

									// defect 9116
									// we have a calulation problem here
									// when using a base of 22.
									// a base of 20 works fine.
									// int liTmp =
									//	(int) (liDecimalAlpha
									//	/ (float) Math.exp(
									//	j * Math.log(ldBase)));
									// liDecimalAlpha =
									//	(int) Math.round(
									//		liDecimalAlpha
									//		% Math.exp(
									//		j
									//		* Math.log(
									//		ldBase)));

									int liTmp =
										(int) (liDecimalAlpha
											/ Math.pow(ldBase, j));
									liDecimalAlpha =
										(int) Math.round(
											liDecimalAlpha
												% Math.pow(ldBase, j));
									// end defect 9116

									String lsValidInvLtrs =
										((String) aaComputeInvData
											.getInvPatrnsData()
											.getValidInvLtrs());

									// defect 10247
									// We should not check for constants
									// here.
									// Constants are defined as not being
									// calculated.
									// This causes alpha build errors.
									//if (((String) lvMinInvNoPatrn
									//	.get(liPatrnCd))
									//	.charAt(liConstantIndx)
									//	== (
									//		(
									//			String) lvMaxInvNoPatrn
									//				.get(
									//			liPatrnCd)).charAt(
									//		liConstantIndx))
									//{
									//	lsAlphaChar =
									//		lsAlphaChar
									//			+ (
									//				(
									//					String) lvMinInvNoPatrn
									//						.get(
									//					liPatrnCd))
									//						.charAt(
									//				liConstantIndx);
									//}
									//else
									// end defect 10247 
									if (
										lsValidInvLtrs.length() > 0)
									{
										if (liTmp < 0)
										{
											System.out.println(
												"What am I doing? "
													+ liTmp);
											liTmp = 0;
											// TODO what is going on here?
										}
										lsAlphaChar =
											lsAlphaChar
												+ lsValidInvLtrs
													.substring(
													liTmp,
													liTmp + 1);
									}

								}
								lvEndInvNoPatrn.set(
									liPatrnCd,
									lsAlphaChar);
								break;
							}
						case ALPHA_POS_2 :
							{
								// Check if the decimalized value for 
								// Alpha, AlphaMin, and AlphaMax need 
								// to be calculated if lvInvNoPatrn 
								// length is less than liPatrnCd + 7 - 1, 
								// add an element with the value of 0.
								// set up position for alpha 1 
								// decimalization if needed
								if (lvInvNoPatrn.size()
									< liPatrnCd
										+ CommonConstant.ELEMENT_7)
								{
									populateVectorWithDefaultValues(
										lvInvNoPatrn,
										lvMinInvNoPatrn,
										lvMaxInvNoPatrn,
										lvOrigInvNoPatrn,
										lvOrigMinInvNoPatrn,
										lvOrigMaxInvNoPatrn,
										liPatrnCd
											+ CommonConstant.ELEMENT_6);
								}
								// set up position for alpha 2 
								// decimalization if needed
								if (lvInvNoPatrn.size()
									< liPatrnCd
										+ CommonConstant.ELEMENT_8)
								{
									populateVectorWithDefaultValues(
										lvInvNoPatrn,
										lvMinInvNoPatrn,
										lvMaxInvNoPatrn,
										lvOrigInvNoPatrn,
										lvOrigMinInvNoPatrn,
										lvOrigMaxInvNoPatrn,
										liPatrnCd
											+ CommonConstant.ELEMENT_7);
								}

								// Use new decimalization routine
								lsItmAlpha =
									(String) lvInvNoPatrn.get(
										liPatrnCd);
								lsMinAlpha =
									(String) lvMinInvNoPatrn.get(
										liPatrnCd);
								lsMaxAlpha =
									(String) lvMaxInvNoPatrn.get(
										liPatrnCd);

								// Test if invalid letter combinations
								// need to be found
								Vector lvInvldLtrIndx =
									findInvldLtrCombos(
										aaComputeInvData,
										liPatrnCd);

								liDecimalAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsItmAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalAlpha));
								lvOrigInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalAlpha));
								liDecimalMinAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsMinAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvMinInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMinAlpha));
								lvOrigMinInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMinAlpha));
								liDecimalMaxAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsMaxAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvMaxInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMaxAlpha));
								lvOrigMaxInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMaxAlpha));

								liDecimalAlpha =
									((Integer) lvInvNoPatrn
										.get(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7))
										.intValue();
								liDecimalMinAlpha =
									((Integer) lvMinInvNoPatrn
										.get(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7))
										.intValue();
								liDecimalMaxAlpha =
									((Integer) lvMaxInvNoPatrn
										.get(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7))
										.intValue();

								// Test if invalid letter combinations
								// need to be found
								lvInvldLtrIndx =
									findInvldLtrCombos(
										aaComputeInvData,
										liPatrnCd);
								liAlphaInvalid =
									((Integer) lvInvldLtrIndx
										.get(CommonConstant.ELEMENT_0))
										.intValue();
								liRangeAlphaInvalid =
									((Integer) lvInvldLtrIndx
										.get(CommonConstant.ELEMENT_1))
										.intValue();

								// Calculate the Div/Mod
								int liAlphaLen =
									((String) lvMaxInvNoPatrn
										.get(liPatrnCd))
										.length();
								int liAlphaNum =
									liDecimalMaxAlpha
										- liDecimalMinAlpha
										- liRangeAlphaInvalid
										+ 1;
								liModValue = liDivValue % liAlphaNum;
								liDivValue = liDivValue / liAlphaNum;
								if (!lbNegativeQty)
								{
									liDecimalAlpha =
										liDecimalAlpha + liModValue;
									if (liDecimalAlpha
										> liDecimalMaxAlpha)
									{
										liDivValue = liDivValue + 1;
										liDecimalAlpha =
											liDecimalAlpha
												- (liDecimalMaxAlpha + 1);
									}
								}
								else
								{
									liDecimalAlpha =
										liDecimalAlpha - liModValue;
									if (liDecimalAlpha
										< liDecimalMinAlpha)
									{
										liDivValue = liDivValue + 1;

										if (liDecimalAlpha < 0)
										{
											//liDecimalAlpha is negative, 
											// so just add it
											liDecimalAlpha =
												liDecimalAlpha
													+ liDecimalMaxAlpha
													+ 1;
										}
										else
										{
											liDecimalAlpha =
												-1
													* (liDecimalAlpha
														- liDecimalMinAlpha
														+ 1);
										}
									}
								}

								// Add invalid combos until correct # 
								// for Positive or Negative
								if (!lbNegativeQty)
								{
									liDecimalMinAlpha =
										((Integer) lvOrigInvNoPatrn
											.get(
												liPatrnCd
													+ CommonConstant
														.ELEMENT_7))
											.intValue();
									while (liDecimalMinAlpha
										!= liDecimalAlpha)
									{
										lvInvNoPatrn.set(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7,
											new Integer(liDecimalAlpha));
										lvMinInvNoPatrn.set(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7,
											new Integer(liDecimalMinAlpha));
										lvInvldLtrIndx =
											findInvldLtrCombos(
												aaComputeInvData,
												liPatrnCd);
										liAlphaInvalid =
											((Integer) lvInvldLtrIndx
												.get(
													CommonConstant
														.ELEMENT_0))
												.intValue();
										liDecimalMinAlpha =
											liDecimalAlpha;
										liDecimalAlpha =
											liDecimalAlpha
												+ liAlphaInvalid;
									}
								}
								else
								{
									liDecimalMaxAlpha =
										((Integer) lvOrigInvNoPatrn
											.get(
												liPatrnCd
													+ CommonConstant
														.ELEMENT_7))
											.intValue();
									while (liDecimalMaxAlpha
										!= liDecimalAlpha)
									{
										lvInvNoPatrn.set(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7,
											new Integer(liDecimalAlpha));
										lvMaxInvNoPatrn.set(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7,
											new Integer(liDecimalMaxAlpha));
										lvInvldLtrIndx =
											findInvldLtrCombos(
												aaComputeInvData,
												liPatrnCd);
										liAlphaInvalid =
											((Integer) lvInvldLtrIndx
												.get(
													CommonConstant
														.ELEMENT_0))
												.intValue();
										liDecimalMaxAlpha =
											liDecimalAlpha;
										liDecimalAlpha =
											liDecimalAlpha
												- liAlphaInvalid;
									}
								}

								// Reformulate the alpha string
								// reset the alphachar before building it
								lsAlphaChar =
									CommonConstant.STR_SPACE_EMPTY;
								liConstantIndx = -1;
								for (int j = (liAlphaLen - 1);
									j > -1;
									j--)
								{
									liConstantIndx = liConstantIndx + 1;

									// defect 9116
									//	int liTmp =
									//		(int) (liDecimalAlpha
									//		/ (float) Math.exp(
									//		j * Math.log(ldBase)));
									//	liDecimalAlpha =
									//		(int) Math.round(
									//		liDecimalAlpha
									//		% Math.exp(
									//			j
									//		* Math.log(
									//		ldBase)));
									int liTmp =
										(int) (liDecimalAlpha
											/ Math.pow(ldBase, j));
									liDecimalAlpha =
										(int) Math.round(
											liDecimalAlpha
												% Math.pow(ldBase, j));
									// end defect 9116

									String lsValidInvLtrs =
										((String) aaComputeInvData
											.getInvPatrnsData()
											.getValidInvLtrs());

									// defect 10247
									// do not consider this a constant
									//if (((String) lvMinInvNoPatrn
									//	.get(liPatrnCd))
									//	.charAt(liConstantIndx)
									//	== (
									//		(
									//			String) lvMaxInvNoPatrn
									//				.get(
									//			liPatrnCd)).charAt(
									//		liConstantIndx))
									//{
									//	lsAlphaChar =
									//		lsAlphaChar
									//			+ (
									//				(
									//					String) lvMinInvNoPatrn
									//						.get(
									//					liPatrnCd))
									//						.charAt(
									//				liConstantIndx);
									//}
									//else
									// end defect 10247
									if (
										lsValidInvLtrs.length() > 0)
									{
										lsAlphaChar =
											lsAlphaChar
												+ lsValidInvLtrs
													.substring(
													liTmp,
													liTmp + 1);
									}
								}
								lvEndInvNoPatrn.set(
									liPatrnCd,
									lsAlphaChar);
								break;
							}
						case ALPHA_POS_3 :
							{
								// Check if the decimalized value for 
								// Alpha, AlphaMin, and AlphaMax need 
								// to be calculated
								// set up position for alpha 1 
								// decimalization if needed
								if (lvInvNoPatrn.size()
									< liPatrnCd
										+ CommonConstant.ELEMENT_6)
								{
									populateVectorWithDefaultValues(
										lvInvNoPatrn,
										lvMinInvNoPatrn,
										lvMaxInvNoPatrn,
										lvOrigInvNoPatrn,
										lvOrigMinInvNoPatrn,
										lvOrigMaxInvNoPatrn,
										liPatrnCd
											+ CommonConstant.ELEMENT_5);
								}
								// set up position for alpha 2 decimalization 
								//  if needed
								if (lvInvNoPatrn.size()
									< liPatrnCd
										+ CommonConstant.ELEMENT_7)
								{
									populateVectorWithDefaultValues(
										lvInvNoPatrn,
										lvMinInvNoPatrn,
										lvMaxInvNoPatrn,
										lvOrigInvNoPatrn,
										lvOrigMinInvNoPatrn,
										lvOrigMaxInvNoPatrn,
										liPatrnCd
											+ CommonConstant.ELEMENT_6);
								}
								// set up position for alpha 3 decimalization
								//  if needed
								if (lvInvNoPatrn.size()
									< liPatrnCd
										+ CommonConstant.ELEMENT_8)
								{
									populateVectorWithDefaultValues(
										lvInvNoPatrn,
										lvMinInvNoPatrn,
										lvMaxInvNoPatrn,
										lvOrigInvNoPatrn,
										lvOrigMinInvNoPatrn,
										lvOrigMaxInvNoPatrn,
										liPatrnCd
											+ CommonConstant.ELEMENT_7);
								}

								// use new decimalization routine
								lsItmAlpha =
									(String) lvInvNoPatrn.get(
										liPatrnCd);
								lsMinAlpha =
									(String) lvMinInvNoPatrn.get(
										liPatrnCd);
								lsMaxAlpha =
									(String) lvMaxInvNoPatrn.get(
										liPatrnCd);

								liDecimalAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsItmAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());

								// Test if invalid letter combinations
								// need to be found
								Vector lvInvldLtrIndx =
									findInvldLtrCombos(
										aaComputeInvData,
										liPatrnCd);

								lvInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalAlpha));
								lvOrigInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalAlpha));
								liDecimalMinAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsMinAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvMinInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMinAlpha));
								lvOrigMinInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMinAlpha));
								liDecimalMaxAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsMaxAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvMaxInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMaxAlpha));
								lvOrigMaxInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMaxAlpha));

								liDecimalAlpha =
									((Integer) lvInvNoPatrn
										.get(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7))
										.intValue();
								liDecimalMinAlpha =
									((Integer) lvMinInvNoPatrn
										.get(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7))
										.intValue();
								liDecimalMaxAlpha =
									((Integer) lvMaxInvNoPatrn
										.get(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7))
										.intValue();

								// Test if invalid letter combinations need 
								//  to be found
								lvInvldLtrIndx =
									findInvldLtrCombos(
										aaComputeInvData,
										liPatrnCd);
								liAlphaInvalid =
									((Integer) lvInvldLtrIndx
										.get(CommonConstant.ELEMENT_0))
										.intValue();
								liRangeAlphaInvalid =
									((Integer) lvInvldLtrIndx
										.get(CommonConstant.ELEMENT_1))
										.intValue();

								// Calculate the Div/Mod
								int liAlphaLen =
									((String) lvMaxInvNoPatrn
										.get(liPatrnCd))
										.length();
								int liAlphaNum =
									liDecimalMaxAlpha
										- liDecimalMinAlpha
										- liRangeAlphaInvalid
										+ 1;
								liModValue = liDivValue % liAlphaNum;
								liDivValue = liDivValue / liAlphaNum;
								if (!lbNegativeQty)
								{
									liDecimalAlpha =
										liDecimalAlpha + liModValue;
									if (liDecimalAlpha
										> liDecimalMaxAlpha)
									{
										liDivValue = liDivValue + 1;
										liDecimalAlpha =
											liDecimalAlpha
												- liDecimalMaxAlpha;
									}
								}
								else
								{
									liDecimalAlpha =
										liDecimalAlpha - liModValue;
									if (liDecimalAlpha
										< liDecimalMinAlpha)
									{
										liDivValue = liDivValue + 1;
										liDecimalAlpha =
											-1
												* (liDecimalAlpha
													- liDecimalMinAlpha
													+ 1);
									}
								}
								// Add invalid combos until correct # for 
								//  Positive or Negative
								if (!lbNegativeQty)
								{
									liDecimalMinAlpha =
										((Integer) lvOrigInvNoPatrn
											.get(
												liPatrnCd
													+ CommonConstant
														.ELEMENT_7))
											.intValue();
									while (liDecimalMinAlpha
										!= liDecimalAlpha)
									{
										lvInvNoPatrn.set(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7,
											new Integer(liDecimalAlpha));
										lvMinInvNoPatrn.set(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7,
											new Integer(liDecimalMinAlpha));
										lvInvldLtrIndx =
											findInvldLtrCombos(
												aaComputeInvData,
												liPatrnCd);
										liAlphaInvalid =
											((Integer) lvInvldLtrIndx
												.get(
													CommonConstant
														.ELEMENT_0))
												.intValue();
										liDecimalMinAlpha =
											liDecimalAlpha;
										liDecimalAlpha =
											liDecimalAlpha
												+ liAlphaInvalid;
									}
								}
								else
								{
									liDecimalMaxAlpha =
										((Integer) lvOrigInvNoPatrn
											.get(
												liPatrnCd
													+ CommonConstant
														.ELEMENT_7))
											.intValue();
									while (liDecimalMaxAlpha
										!= liDecimalAlpha)
									{
										lvInvNoPatrn.set(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7,
											new Integer(liDecimalAlpha));
										lvMaxInvNoPatrn.set(
											liPatrnCd
												+ CommonConstant
													.ELEMENT_7,
											new Integer(liDecimalMaxAlpha));
										lvInvldLtrIndx =
											findInvldLtrCombos(
												aaComputeInvData,
												liPatrnCd);
										liAlphaInvalid =
											((Integer) lvInvldLtrIndx
												.get(
													CommonConstant
														.ELEMENT_0))
												.intValue();
										liDecimalMaxAlpha =
											liDecimalAlpha;
										liDecimalAlpha =
											liDecimalAlpha
												- liAlphaInvalid;
									}
								}

								// Reformulate the alpha string
								// reset the alphachar before building it
								lsAlphaChar =
									CommonConstant.STR_SPACE_EMPTY;
								liConstantIndx = -1;
								for (int j = (liAlphaLen - 1);
									j > -1;
									j--)
								{
									liConstantIndx = liConstantIndx + 1;

									// defect 9116
									//int liTmp =
									//	(int) (liDecimalAlpha
									//		/ (float) Math.exp(
									//			j * Math.log(ldBase)));
									//liDecimalAlpha =
									//	(int) Math.round(
									//		liDecimalAlpha
									//			% Math.exp(
									//				j
									//					* Math.log(
									//						ldBase)));

									int liTmp =
										(int) (liDecimalAlpha
											/ Math.pow(ldBase, j));
									liDecimalAlpha =
										(int) Math.round(
											liDecimalAlpha
												% Math.pow(ldBase, j));

									// end defect 9116

									String lsValidInvLtrs =
										((String) aaComputeInvData
											.getInvPatrnsData()
											.getValidInvLtrs());
									// defect 10247
									//if (((String) lvMinInvNoPatrn
									//	.get(liPatrnCd))
									//	.charAt(liConstantIndx)
									//	== (
									//		(
									//			String) lvMaxInvNoPatrn
									//				.get(
									//			liPatrnCd)).charAt(
									//		liConstantIndx))
									//{
									//	lsAlphaChar =
									//		lsAlphaChar
									//			+ (
									//				(
									//					String) lvMinInvNoPatrn
									//						.get(
									//					liPatrnCd))
									//						.charAt(
									//				liConstantIndx);
									//}
									//else
									// end defect 10247 
									if (
										lsValidInvLtrs.length() > 0)
									{
										lsAlphaChar =
											lsAlphaChar
												+ lsValidInvLtrs
													.substring(
													liTmp,
													liTmp + 1);
									}
								}
								lvEndInvNoPatrn.set(
									liPatrnCd,
									lsAlphaChar);
								break;
							}
						case NUM_POS_1 :
							{
								liNum =
									Integer.parseInt(
										(String) lvInvNoPatrn.get(
											liPatrnCd));
								liNumMin =
									Integer.parseInt(
										(String) lvMinInvNoPatrn.get(
											liPatrnCd));
								liNumMax =
									Integer.parseInt(
										(String) lvMaxInvNoPatrn.get(
											liPatrnCd));

								// Calculate the Div/Mod
								liModValue =
									liDivValue
										% (liNumMax - liNumMin + 1);
								liDivValue =
									liDivValue
										/ (liNumMax - liNumMin + 1);
								if (!lbNegativeQty)
								{
									liNum = liNum + liModValue;
									if (liNum > liNumMax)
									{
										liDivValue = liDivValue + 1;
										liNum =
											liNum
												- liNumMax
												+ liNumMin
												- 1;
									}
								}
								else
								{
									liNum = liNum - liModValue;
									if (liNum < liNumMin)
									{
										liDivValue = liDivValue + 1;
										liNum = liNum - liNumMin + 1;
										liNum = liNumMax - liNum;
									}
								}
								lvEndInvNoPatrn.set(
									liPatrnCd,
									Integer.toString(liNum));
								break;
							}
						case NUM_POS_2 :
							{
								liNum =
									Integer.parseInt(
										(String) lvInvNoPatrn.get(
											liPatrnCd));
								liNumMin =
									Integer.parseInt(
										(String) lvMinInvNoPatrn.get(
											liPatrnCd));
								liNumMax =
									Integer.parseInt(
										(String) lvMaxInvNoPatrn.get(
											liPatrnCd));

								// Calculate the Div/Mod
								liModValue =
									liDivValue
										% (liNumMax - liNumMin + 1);
								liDivValue =
									liDivValue
										/ (liNumMax - liNumMin + 1);
								if (!lbNegativeQty)
								{
									liNum = liNum + liModValue;
									if (liNum > liNumMax)
									{
										liDivValue = liDivValue + 1;
										liNum =
											liNum
												- liNumMax
												+ liNumMin
												- 1;
									}
								}
								else
								{
									liNum = liNum - liModValue;
									if (liNum < liNumMin)
									{
										liDivValue = liDivValue + 1;
										liNum = liNum - liNumMin + 1;
										liNum = liNumMax - liNum;
									}
								}
								lvEndInvNoPatrn.set(
									liPatrnCd,
									Integer.toString(liNum));
								break;
							}
						case NUM_POS_3 :
							{
								liNum =
									Integer.parseInt(
										(String) lvInvNoPatrn.get(
											liPatrnCd));
								liNumMin =
									Integer.parseInt(
										(String) lvMinInvNoPatrn.get(
											liPatrnCd));
								liNumMax =
									Integer.parseInt(
										(String) lvMaxInvNoPatrn.get(
											liPatrnCd));

								// Calculate the Div/Mod
								liModValue =
									liDivValue
										% (liNumMax - liNumMin + 1);
								liDivValue =
									liDivValue
										/ (liNumMax - liNumMin + 1);
								if (!lbNegativeQty)
								{
									liNum = liNum + liModValue;
									if (liNum > liNumMax)
									{
										liDivValue = liDivValue + 1;
										liNum =
											liNum
												- liNumMax
												+ liNumMin
												- 1;
									}
								}
								else
								{
									liNum = liNum - liModValue;
									if (liNum < liNumMin)
									{
										liDivValue = liDivValue + 1;
										liNum = liNum - liNumMin + 1;
										liNum = liNumMax - liNum;
									}
								}
								lvEndInvNoPatrn.set(
									liPatrnCd,
									Integer.toString(liNum));
								break;
							}
					}

					// Test if strings should be parsed
					if (liPatrnCd <= ALPHA_POS_3
						&& lsAlphaChar.length() > 0)
					{
						int liTmpAlphaPatrnCdLen =
							lsTmpAlphaPatrnCd.length();
						for (int j = 0;
							j < liTmpAlphaPatrnCdLen;
							j = j + 2)
						{
							String lsChar2 =
								lsTmpAlphaPatrnCd.substring(0, 2);
							lsTmpAlphaPatrnCd =
								lsTmpAlphaPatrnCd.substring(2);
							if (lsChar2
								.substring(0, 1)
								.equals(ALPHA_POS_1_STR))
							{
								lvEndInvNoPatrn.set(
									CommonConstant.ELEMENT_1,
									lsAlphaChar.substring(
										0,
										Integer.parseInt(
											lsChar2.substring(1, 2))));
							}
							else if (
								lsChar2.substring(0, 1).equals(
									ALPHA_POS_2_STR))
							{
								lvEndInvNoPatrn.set(
									CommonConstant.ELEMENT_2,
									lsAlphaChar.substring(
										0,
										Integer.parseInt(
											lsChar2.substring(1, 2))));
							}
							else if (
								lsChar2.substring(0, 1).equals(
									ALPHA_POS_3_STR))
							{
								lvEndInvNoPatrn.set(
									CommonConstant.ELEMENT_3,
									lsAlphaChar.substring(
										0,
										Integer.parseInt(
											lsChar2.substring(1, 2))));
							}
							if (lsTmpAlphaPatrnCd
								.equals(CommonConstant.STR_SPACE_EMPTY))
							{
								break;
							}

							lsAlphaChar =
								lsAlphaChar.substring(
									Integer.parseInt(
										lsChar2.substring(1, 2)));
						}
						// Test if Alpha1Orig in the InvItmPatrnCd
						if (lsInvItmPatrnCd.indexOf(ALPHA_POS_1_STR)
							== -1)
						{
							lvEndInvNoPatrn.set(
								1,
								(String) lvOrigInvNoPatrn.get(
									CommonConstant.ELEMENT_1));
						}
					}
				}

				// Assemble the calculated inventory number
				if ((((String) lvInvNoPatrn
					.get(CommonConstant.ELEMENT_0))
					.substring(0, 1))
					.equals(PATTERN_IS_NUMBER))
				{
					if (!((String) lvInvNoPatrn
						.get(CommonConstant.ELEMENT_4))
						.equals(CommonConstant.STR_SPACE_EMPTY))
					{
						// Check if number should have leading zeros
						if (((String) lvMinInvNoPatrn
							.get(CommonConstant.ELEMENT_4))
							.length()
							== ((String) lvMaxInvNoPatrn
								.get(CommonConstant.ELEMENT_4))
								.length())
						{
							while (((String) lvMinInvNoPatrn
								.get(CommonConstant.ELEMENT_4))
								.length()
								> ((String) lvEndInvNoPatrn
									.get(CommonConstant.ELEMENT_4))
									.length())
							{
								lvEndInvNoPatrn.set(
									CommonConstant.ELEMENT_4,
									(CHAR_ZERO
										+ ((String) lvEndInvNoPatrn
											.get(
												CommonConstant
													.ELEMENT_4))));
							}
							lsCalcInvItmNo =
								lsCalcInvItmNo
									+ (String) lvEndInvNoPatrn.get(
										CommonConstant.ELEMENT_4);
						}
						else
						{
							lsCalcInvItmNo =
								lsCalcInvItmNo
									+ (String) lvEndInvNoPatrn.get(
										CommonConstant.ELEMENT_4);
						}
						if (!((String) lvInvNoPatrn.get(1))
							.equals(CommonConstant.STR_SPACE_EMPTY))
						{
							lsCalcInvItmNo
								+= (String) lvEndInvNoPatrn.get(
									CommonConstant.ELEMENT_1);

							if (!((String) lvInvNoPatrn
								.get(CommonConstant.ELEMENT_5))
								.equals(CommonConstant.STR_SPACE_EMPTY))
							{
								// Check if number should have leading zeros
								if (((String) lvMinInvNoPatrn
									.get(CommonConstant.ELEMENT_5))
									.length()
									== ((String) lvMaxInvNoPatrn
										.get(CommonConstant.ELEMENT_5))
										.length())
								{
									while (((String) lvMinInvNoPatrn
										.get(CommonConstant.ELEMENT_5))
										.length()
										> ((String) lvEndInvNoPatrn
											.get(
												CommonConstant
													.ELEMENT_5))
											.length())
									{
										lvEndInvNoPatrn.set(
											CommonConstant.ELEMENT_5,
											(CHAR_ZERO
												+ (
													(String) lvEndInvNoPatrn
													.get(
														CommonConstant
															.ELEMENT_5))));
									}
									lsCalcInvItmNo =
										lsCalcInvItmNo
											+ (
												String) lvEndInvNoPatrn
													.get(
												CommonConstant
													.ELEMENT_5);
								}
								else
									lsCalcInvItmNo =
										lsCalcInvItmNo
											+ (
												String) lvEndInvNoPatrn
													.get(
												CommonConstant
													.ELEMENT_5);

								if (!((String) lvInvNoPatrn
									.get(CommonConstant.ELEMENT_2))
									.equals(
										CommonConstant
											.STR_SPACE_EMPTY))
								{
									lsCalcInvItmNo =
										lsCalcInvItmNo
											+ (
												String) lvEndInvNoPatrn
													.get(
												CommonConstant
													.ELEMENT_2);

									if (!((String) lvInvNoPatrn
										.get(CommonConstant.ELEMENT_6))
										.equals(
											CommonConstant
												.STR_SPACE_EMPTY))
									{
										// Check if number should have 
										// leading zeros
										if (((String) lvMinInvNoPatrn
											.get(
												CommonConstant
													.ELEMENT_6))
											.length()
											== ((String) lvMaxInvNoPatrn
												.get(
													CommonConstant
														.ELEMENT_6))
												.length())
										{
											while (
												(
													(String) lvMinInvNoPatrn
												.get(
													CommonConstant
														.ELEMENT_6))
												.length()
												> (
													(String) lvEndInvNoPatrn
													.get(
														CommonConstant
															.ELEMENT_6))
													.length())
											{
												lvEndInvNoPatrn.set(
													CommonConstant
														.ELEMENT_6,
													(CHAR_ZERO
														+ (
															(String) lvEndInvNoPatrn
															.get(
																CommonConstant
																	.ELEMENT_6))));
											}
											lsCalcInvItmNo =
												lsCalcInvItmNo
													+ (
														String) lvEndInvNoPatrn
															.get(
														CommonConstant
															.ELEMENT_6);
										}
										else
											lsCalcInvItmNo =
												lsCalcInvItmNo
													+ (
														String) lvEndInvNoPatrn
															.get(
														CommonConstant
															.ELEMENT_6);

										if (!((String) lvInvNoPatrn
											.get(
												CommonConstant
													.ELEMENT_1))
											.equals(
												CommonConstant
													.STR_SPACE_EMPTY))
											lsCalcInvItmNo =
												lsCalcInvItmNo
													+ (
														String) lvEndInvNoPatrn
															.get(
														CommonConstant
															.ELEMENT_1);
									}
								}
							}
						}
					}
					lvEndInvNoPatrn.set(
						CommonConstant.ELEMENT_7,
						lsCalcInvItmNo);
				}
				else if (
					(
						(
							(String) lvInvNoPatrn.get(
								CommonConstant.ELEMENT_0)).substring(
							0,
							1)).equals(
						PATTERN_IS_ALPHA))
				{
					if (!((String) lvInvNoPatrn
						.get(CommonConstant.ELEMENT_1))
						.equals(CommonConstant.STR_SPACE_EMPTY))
					{
						lsCalcInvItmNo =
							lsCalcInvItmNo
								+ (String) lvEndInvNoPatrn.get(
									CommonConstant.ELEMENT_1);

						if (!((String) lvInvNoPatrn
							.get(CommonConstant.ELEMENT_4))
							.equals(CommonConstant.STR_SPACE_EMPTY))
						{
							// Check if number should have leading zeros
							if (((String) lvMinInvNoPatrn
								.get(CommonConstant.ELEMENT_4))
								.length()
								== ((String) lvMaxInvNoPatrn
									.get(CommonConstant.ELEMENT_4))
									.length())
							{
								while (((String) lvMinInvNoPatrn
									.get(CommonConstant.ELEMENT_4))
									.length()
									> ((String) lvEndInvNoPatrn
										.get(CommonConstant.ELEMENT_4))
										.length())
								{
									lvEndInvNoPatrn.set(
										CommonConstant.ELEMENT_4,
										(CHAR_ZERO
											+ ((String) lvEndInvNoPatrn
												.get(
													CommonConstant
														.ELEMENT_4))));
								}
								lsCalcInvItmNo =
									lsCalcInvItmNo
										+ (String) lvEndInvNoPatrn.get(
											CommonConstant.ELEMENT_4);
							}
							else
								lsCalcInvItmNo =
									lsCalcInvItmNo
										+ (String) lvEndInvNoPatrn.get(
											CommonConstant.ELEMENT_4);

							if (!((String) lvInvNoPatrn
								.get(CommonConstant.ELEMENT_2))
								.equals(CommonConstant.STR_SPACE_EMPTY))
							{
								lsCalcInvItmNo =
									lsCalcInvItmNo
										+ (String) lvEndInvNoPatrn.get(
											CommonConstant.ELEMENT_2);

								if (!((String) lvInvNoPatrn
									.get(CommonConstant.ELEMENT_5))
									.equals(
										CommonConstant
											.STR_SPACE_EMPTY))
								{
									// Check if number should have 
									// leading zeros
									if (((String) lvMinInvNoPatrn
										.get(CommonConstant.ELEMENT_5))
										.length()
										== ((String) lvMaxInvNoPatrn
											.get(
												CommonConstant
													.ELEMENT_5))
											.length())
									{
										while (
											((String) lvMinInvNoPatrn
											.get(
												CommonConstant
													.ELEMENT_5))
											.length()
											> ((String) lvEndInvNoPatrn
												.get(
													CommonConstant
														.ELEMENT_5))
												.length())
										{
											lvEndInvNoPatrn.set(
												CommonConstant
													.ELEMENT_5,
												(CHAR_ZERO
													+ (
														(String) lvEndInvNoPatrn
														.get(
															CommonConstant
																.ELEMENT_5))));
										}
										lsCalcInvItmNo =
											lsCalcInvItmNo
												+ (
													String) lvEndInvNoPatrn
														.get(
													CommonConstant
														.ELEMENT_5);
									}
									else
									{
										lsCalcInvItmNo =
											lsCalcInvItmNo
												+ (
													String) lvEndInvNoPatrn
														.get(
													CommonConstant
														.ELEMENT_5);
									}
									if (!((String) lvInvNoPatrn
										.get(CommonConstant.ELEMENT_3))
										.equals(
											CommonConstant
												.STR_SPACE_EMPTY))
									{
										lsCalcInvItmNo =
											lsCalcInvItmNo
												+ (
													String) lvEndInvNoPatrn
														.get(
													CommonConstant
														.ELEMENT_3);

										if (!((String) lvInvNoPatrn
											.get(
												CommonConstant
													.ELEMENT_6))
											.equals(
												CommonConstant
													.STR_SPACE_EMPTY))
										{
											// Check if number should 
											// have leading zeros
											if (
												(
													(String) lvMinInvNoPatrn
												.get(
													CommonConstant
														.ELEMENT_6))
												.length()
												== (
													(String) lvMaxInvNoPatrn
													.get(
														CommonConstant
															.ELEMENT_6))
													.length())
											{
												while (
													(
														(String) lvMinInvNoPatrn
													.get(
														CommonConstant
															.ELEMENT_6))
													.length()
													> (
														(String) lvEndInvNoPatrn
														.get(
															CommonConstant
																.ELEMENT_6))
														.length())
												{
													lvEndInvNoPatrn
														.set(
														CommonConstant
															.ELEMENT_6,
														(CHAR_ZERO
															+ (
																(String) lvEndInvNoPatrn
																.get(
																	CommonConstant
																		.ELEMENT_6))));
												}
												lsCalcInvItmNo =
													lsCalcInvItmNo
														+ (
															String) lvEndInvNoPatrn
																.get(
															CommonConstant
																.ELEMENT_6);
											}
											else
											{
												lsCalcInvItmNo =
													lsCalcInvItmNo
														+ (
															String) lvEndInvNoPatrn
																.get(
															CommonConstant
																.ELEMENT_6);
											}
										}
									}
								}
							}
						}
					}
					lvEndInvNoPatrn.set(
						CommonConstant.ELEMENT_7,
						lsCalcInvItmNo);
				}
				else
				{
					lvEndInvNoPatrn.set(
						CommonConstant.ELEMENT_7,
						lsCalcInvItmNo);
				}
				aaComputeInvData.getInvAlloctnData().setInvItmEndNo(
					(String) lvEndInvNoPatrn.get(
						CommonConstant.ELEMENT_7));
			}

			// This flag is used only for inventory allocation to show
			// that the calculation was successful.
			aaComputeInvData.getInvAlloctnData().setCalcInv(true);

			// set up the end patrnseqno
			aaComputeInvData.getInvAlloctnData().setEndPatrnSeqNo(
				aaComputeInvData.getInvAlloctnData().getPatrnSeqNo()
					+ aaComputeInvData.getInvAlloctnData().getInvQty()
					- 1);

			return aaComputeInvData.getInvAlloctnData();
		}
		catch (RTSException aeRTSEx)
		{
			//When error occurs, setCalcInv(false)
			aaComputeInvData.getInvAlloctnData().setCalcInv(false);
			throw aeRTSEx;
		}
		finally
		{
			Log.write(Log.METHOD, this, MSG_CALCINVENDNO_END);
		}
	}

	/**
	 * Used to calculate the decimalized inventory number as the 
	 * PatrnSeqNo.  
	 * <p>Note: The method ValidateItmNoInput needs to be called 
	 * prevous to this method.
	 * 
	 * <p>The Vectors in this method hold data about the inventory 
	 * pattern
	 * <ul>
	 * <li> Element  0 - (String) the alphanumeric inventory pattern 
	 * for that item
	 * <li> Element  1 - (String) the first group of characters
	 * <li> Element  2 - (String) the second group of characters
	 * <li> Element  3 - (String) the third group of characters
	 * <li> Element  4 - (String) the first group of numeric characters
	 * <li> Element  5 - (String) the second group of numeric characters
	 * <li> Element  6 - (String) the third group of numeric characters
	 * <li> Element  7 - (String) the inventory item number
	 * <li> Element  8 - (Integer) the decimalized value for the first 
	 * group of characters
	 * <li> Element  9 - (Integer) the decimalized value for the second
	 * group of characters
	 * <li>Element 10 - (Integer) the decimalized value for the third 
	 * group of characters
	 * <eul>
	 * 
	 * @param aaComputeInvData ComputeInventoryData
	 * @return Object 
	 * @throws RTSException  
	 */
	public Object calcInvNo(ComputeInventoryData aaComputeInvData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_CALCINVNO_BEGIN);

		Vector lvInvNoPatrn = aaComputeInvData.getInvNoPatrn();
		Vector lvMinInvNoPatrn = aaComputeInvData.getMinInvNoPatrn();
		Vector lvMaxInvNoPatrn = aaComputeInvData.getMaxInvNoPatrn();
		Vector lvOrigInvNoPatrn = aaComputeInvData.getOrigInvNoPatrn();
		Vector lvOrigMinInvNoPatrn =
			aaComputeInvData.getOrigMinInvNoPatrn();
		Vector lvOrigMaxInvNoPatrn =
			aaComputeInvData.getOrigMaxInvNoPatrn();

		//Begin calculation, setCalcInv(false)
		aaComputeInvData.getInvAlloctnData().setCalcInv(false);

		ValidateInventoryPattern laValidateInvPatrn =
			new ValidateInventoryPattern();
		String lsInvItmPatrnCd =
			String.valueOf(
				aaComputeInvData.getInvPatrnsData().getInvItmPatrnCd());
		int liPatrnCd = 0;
		int liDecimalAlpha = 0;
		int liDecimalMinAlpha = 0;
		int liDecimalMaxAlpha = 0;
		int liAlphaInvalid = 0;
		int liRangeAlphaInvalid = 0;
		int liIncrementValue = 0;
		int liIncrementMaxValue = 0;
		int liAmount = 0;
		int liMaxValue = 0;

		try
		{
			if (ValidateInventoryPattern
				.chkIfItmPLPOrOLDPLTOrROP(
					aaComputeInvData.getInvAlloctnData().getItmCd()))
			{
				aaComputeInvData.getInvAlloctnData().setPatrnSeqNo(0);
			}
			else
			{
				// remove constants before Decimalizing the string
				String lsItmAlpha = null;
				String lsMinAlpha = null;
				String lsMaxAlpha = null;

				// Compute decimalized value for the item code
				for (int i = 0; i < lsInvItmPatrnCd.length(); i++)
				{
					liPatrnCd =
						Integer.parseInt(
							lsInvItmPatrnCd.substring(i, i + 1));
					liIncrementValue = 0;
					liIncrementMaxValue = 0;

					// Test if strings can/should be consolidated
					if (liPatrnCd <= ALPHA_HIGHEST_POSITION)
					{
						Vector lvReturnData =
							new Vector(
								consolidateAlpha(
									aaComputeInvData,
									i,
									lsInvItmPatrnCd,
									liPatrnCd));
						liPatrnCd =
							((Integer) lvReturnData
								.get(CommonConstant.ELEMENT_0))
								.intValue();
						i =
							((Integer) lvReturnData
								.get(CommonConstant.ELEMENT_1))
								.intValue();
					}

					// Test which value to increment and calculate 
					// increment value and increment max value
					switch (liPatrnCd)
					{
						case ALPHA_POS_1 :
							{
								liAlphaInvalid = 0;
								liRangeAlphaInvalid = 0;

								// set up position for alpha 1 
								//  decimalization if needed
								if (lvInvNoPatrn.size()
									< liPatrnCd
										+ CommonConstant.ELEMENT_8)
								{
									populateVectorWithDefaultValues(
										lvInvNoPatrn,
										lvMinInvNoPatrn,
										lvMaxInvNoPatrn,
										lvOrigInvNoPatrn,
										lvOrigMinInvNoPatrn,
										lvOrigMaxInvNoPatrn,
										liPatrnCd
											+ CommonConstant.ELEMENT_7);
								}
								// pass min and max to new decimalization
								//  routines
								lsItmAlpha =
									(String) lvInvNoPatrn.get(
										liPatrnCd);
								lsMinAlpha =
									(String) lvMinInvNoPatrn.get(
										liPatrnCd);
								lsMaxAlpha =
									(String) lvMaxInvNoPatrn.get(
										liPatrnCd);

								// Calculate decimalized value for Alpha, 
								//  AlphaMin, and AlphaMax
								liDecimalAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsItmAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalAlpha));
								lvOrigInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalAlpha));
								liDecimalMinAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsMinAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvMinInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMinAlpha));
								lvOrigMinInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMinAlpha));
								liDecimalMaxAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsMaxAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvMaxInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMaxAlpha));
								lvOrigMaxInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMaxAlpha));

								// Test if invalid letter combinations
								// need to be found
								Vector lvInvldLtrIndx =
									findInvldLtrCombos(
										aaComputeInvData,
										liPatrnCd);
								liAlphaInvalid =
									((Integer) lvInvldLtrIndx
										.get(CommonConstant.ELEMENT_0))
										.intValue();
								liRangeAlphaInvalid =
									((Integer) lvInvldLtrIndx
										.get(CommonConstant.ELEMENT_1))
										.intValue();

								liIncrementValue =
									liDecimalAlpha
										- liDecimalMinAlpha
										- liAlphaInvalid
										+ 1;
								liIncrementMaxValue =
									liDecimalMaxAlpha
										- liDecimalMinAlpha
										- liRangeAlphaInvalid
										+ 1;
								break;
							}
						case ALPHA_POS_2 :
							{
								liAlphaInvalid = 0;
								liRangeAlphaInvalid = 0;

								// set up position for alpha 1 
								//  decimalization if needed
								if (lvInvNoPatrn.size()
									< liPatrnCd
										+ CommonConstant.ELEMENT_7)
								{
									populateVectorWithDefaultValues(
										lvInvNoPatrn,
										lvMinInvNoPatrn,
										lvMaxInvNoPatrn,
										lvOrigInvNoPatrn,
										lvOrigMinInvNoPatrn,
										lvOrigMaxInvNoPatrn,
										liPatrnCd
											+ CommonConstant.ELEMENT_6);
								}
								// set up position for alpha 2 
								//  decimalization if needed
								if (lvInvNoPatrn.size()
									< liPatrnCd
										+ CommonConstant.ELEMENT_8)
								{
									populateVectorWithDefaultValues(
										lvInvNoPatrn,
										lvMinInvNoPatrn,
										lvMaxInvNoPatrn,
										lvOrigInvNoPatrn,
										lvOrigMinInvNoPatrn,
										lvOrigMaxInvNoPatrn,
										liPatrnCd
											+ CommonConstant.ELEMENT_7);
								}

								// pass min and max to new Decimalization 
								//  routine
								lsItmAlpha =
									(String) lvInvNoPatrn.get(
										liPatrnCd);
								lsMinAlpha =
									(String) lvMinInvNoPatrn.get(
										liPatrnCd);
								lsMaxAlpha =
									(String) lvMaxInvNoPatrn.get(
										liPatrnCd);

								// Calculate decimalized value for Alpha,
								//  AlphaMin, and AlphaMax
								liDecimalAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsItmAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());

								lvInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalAlpha));
								lvOrigInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalAlpha));
								liDecimalMinAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsMinAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());

								lvMinInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMinAlpha));
								lvOrigMinInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMinAlpha));
								liDecimalMaxAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsMaxAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());

								lvMaxInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMaxAlpha));
								lvOrigMaxInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMaxAlpha));

								// Test if invalid letter combinations
								// need to be found
								Vector lvInvldLtrIndx =
									findInvldLtrCombos(
										aaComputeInvData,
										liPatrnCd);
								liAlphaInvalid =
									((Integer) lvInvldLtrIndx
										.get(CommonConstant.ELEMENT_0))
										.intValue();
								liRangeAlphaInvalid =
									((Integer) lvInvldLtrIndx
										.get(CommonConstant.ELEMENT_1))
										.intValue();

								liIncrementValue =
									liDecimalAlpha
										- liDecimalMinAlpha
										- liAlphaInvalid
										+ 1;
								liIncrementMaxValue =
									liDecimalMaxAlpha
										- liDecimalMinAlpha
										- liRangeAlphaInvalid
										+ 1;
								break;
							}
						case ALPHA_POS_3 :
							{
								liAlphaInvalid = 0;
								liRangeAlphaInvalid = 0;

								// set up position for alpha 1 
								//  decimalization if needed
								if (lvInvNoPatrn.size()
									< liPatrnCd
										+ CommonConstant.ELEMENT_6)
								{
									populateVectorWithDefaultValues(
										lvInvNoPatrn,
										lvMinInvNoPatrn,
										lvMaxInvNoPatrn,
										lvOrigInvNoPatrn,
										lvOrigMinInvNoPatrn,
										lvOrigMaxInvNoPatrn,
										liPatrnCd
											+ CommonConstant.ELEMENT_5);
								}
								// set up position for alpha 2 
								//  decimalization if needed
								if (lvInvNoPatrn.size()
									< liPatrnCd
										+ CommonConstant.ELEMENT_7)
								{
									populateVectorWithDefaultValues(
										lvInvNoPatrn,
										lvMinInvNoPatrn,
										lvMaxInvNoPatrn,
										lvOrigInvNoPatrn,
										lvOrigMinInvNoPatrn,
										lvOrigMaxInvNoPatrn,
										liPatrnCd
											+ CommonConstant.ELEMENT_6);
								}
								// set up position for alpha 3 
								//  decimalization if needed
								if (lvInvNoPatrn.size()
									< liPatrnCd
										+ CommonConstant.ELEMENT_8)
								{
									populateVectorWithDefaultValues(
										lvInvNoPatrn,
										lvMinInvNoPatrn,
										lvMaxInvNoPatrn,
										lvOrigInvNoPatrn,
										lvOrigMinInvNoPatrn,
										lvOrigMaxInvNoPatrn,
										liPatrnCd
											+ CommonConstant.ELEMENT_7);
								}
								//end of fix
								// pass Min and Max to new decimalization routine
								lsItmAlpha =
									(String) lvInvNoPatrn.get(
										liPatrnCd);
								lsMinAlpha =
									(String) lvMinInvNoPatrn.get(
										liPatrnCd);
								lsMaxAlpha =
									(String) lvMaxInvNoPatrn.get(
										liPatrnCd);

								// Calculate decimalized value for Alpha, 
								// AlphaMin, and AlphaMax
								liDecimalAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsItmAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalAlpha));
								lvOrigInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalAlpha));
								liDecimalMinAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsMinAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvMinInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMinAlpha));
								lvOrigMinInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMinAlpha));
								liDecimalMaxAlpha =
									laValidateInvPatrn.decimalizeAlpha(
										lsMaxAlpha,
										lsMinAlpha,
										lsMaxAlpha,
										aaComputeInvData
											.getInvPatrnsData());
								lvMaxInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMaxAlpha));
								lvOrigMaxInvNoPatrn.set(
									liPatrnCd
										+ CommonConstant.ELEMENT_7,
									new Integer(liDecimalMaxAlpha));

								// Test if invalid letter combinations need
								// to be found
								Vector lvInvldLtrIndx =
									findInvldLtrCombos(
										aaComputeInvData,
										liPatrnCd);
								liAlphaInvalid =
									((Integer) lvInvldLtrIndx
										.get(CommonConstant.ELEMENT_0))
										.intValue();
								liRangeAlphaInvalid =
									((Integer) lvInvldLtrIndx
										.get(CommonConstant.ELEMENT_1))
										.intValue();

								liIncrementValue =
									liDecimalAlpha
										- liDecimalMinAlpha
										- liAlphaInvalid
										+ 1;
								liIncrementMaxValue =
									liDecimalMaxAlpha
										- liDecimalMinAlpha
										- liRangeAlphaInvalid
										+ 1;
								break;
							}
						case NUM_POS_1 :
							{
								int liNumHolder =
									Integer.parseInt(
										(String) lvInvNoPatrn.get(
											liPatrnCd));
								int liNumMin =
									Integer.parseInt(
										(String) lvMinInvNoPatrn.get(
											liPatrnCd));
								int liNumEndLen =
									((String) lvMaxInvNoPatrn
										.get(liPatrnCd))
										.length();
								liIncrementValue =
									liNumHolder - liNumMin + 1;
								// defect 9116
								//Double lDblIncrementMaxValue =
								//	new Double(
								//		Math.exp(
								//			liNumEndLen * Math.log(10))
								//			- liNumMin);
								Double lDblIncrementMaxValue =
									new Double(
										Math.pow(10, liNumEndLen)
											- liNumMin);
								// end defect 9116
								liIncrementMaxValue =
									lDblIncrementMaxValue.intValue();
								break;
							}
						case NUM_POS_2 :
							{
								int liNumHolder =
									Integer.parseInt(
										(String) lvInvNoPatrn.get(
											liPatrnCd));
								int liNumMin =
									Integer.parseInt(
										(String) lvMinInvNoPatrn.get(
											liPatrnCd));
								int liNumEndLen =
									((String) lvMaxInvNoPatrn
										.get(liPatrnCd))
										.length();
								liIncrementValue =
									liNumHolder - liNumMin + 1;
								// defect 9116
								//Double laIncrementMaxValue =
								//	new Double(
								//		Math.exp(
								//			liNumEndLen * Math.log(10))
								//			- liNumMin);
								Double laIncrementMaxValue =
									new Double(
										Math.pow(10, liNumEndLen)
											- liNumMin);
								// end defect 9116
								liIncrementMaxValue =
									laIncrementMaxValue.intValue();
								break;
							}
						case NUM_POS_3 :
							{
								int liNumHolder =
									Integer.parseInt(
										(String) lvInvNoPatrn.get(
											liPatrnCd));
								int liNumMin =
									Integer.parseInt(
										(String) lvMinInvNoPatrn.get(
											liPatrnCd));
								int liNumEndLen =
									((String) lvMaxInvNoPatrn
										.get(liPatrnCd))
										.length();
								liIncrementValue =
									liNumHolder - liNumMin + 1;
								// defect 9116
								//Double laIncrementMaxValue =
								//	new Double(
								//		Math.exp(
								//			liNumEndLen * Math.log(10))
								//			- liNumMin);
								Double laIncrementMaxValue =
									new Double(
										Math.pow(10, liNumEndLen)
											- liNumMin);
								// end defect 9116
								liIncrementMaxValue =
									laIncrementMaxValue.intValue();
								break;
							}
					}

					// Increase amount
					if (i == 0)
					{
						liAmount = liIncrementValue;
						liMaxValue = liIncrementMaxValue;
					}
					else
					{
						liAmount =
							liAmount
								+ (liIncrementValue * liMaxValue)
								- liMaxValue;
						liMaxValue = liMaxValue * liIncrementMaxValue;
					}
				}

				// Store the begin decimalized number in the data object
				aaComputeInvData.getInvAlloctnData().setPatrnSeqNo(
					liAmount);
			}

			// This flag is used only for inventory allocation to show 
			// that the calculation was successful.
			aaComputeInvData.getInvAlloctnData().setCalcInv(true);

			return aaComputeInvData.getInvAlloctnData();
		}
		catch (RTSException aeRTSEx)
		{
			//When error occurs, setCalcInv(false)
			aaComputeInvData.getInvAlloctnData().setCalcInv(false);
			throw aeRTSEx;
		}
		catch (NumberFormatException aeNFE)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeNFE);
			leRTSEx.setCode(ErrorsConstant.ERR_NUM_COULD_NOT_COMPUTE);
			throw leRTSEx;
		}
		finally
		{
			Log.write(Log.METHOD, this, MSG_CALCINVNO_END);
		}
	}

	/**
	 * Used to calculate the patrnseqno, patrnseqcd and either the 
	 * inventory end number or the quantity.
	 *
	 * <p>Paramter aaData is an InventoryAllocationUIData object where 
	 * the required fields are:
	 * <ul>
	 * <li>ItmCd - The item code for the inventory
	 * <li>InvItmYr - The inventory year
	 * <li>InvItmNo - The inventory begin number of the item range
	 * <p>In addition to these required fields, one of the 
	 * following fields need to be set
	 * <ul>
	 * <li>InvQty - The quantity
	 * <li>InvItmEndNo - The inventory end number of the item range
	 * <eul>
	 * <eul>
	 * 
	 * @param aaData Object 
	 * @return Object 
	 * @throws RTSException  
	 */
	public Object calcInvUnknown(Object aaData) throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_CALCINVUNKNOWN_BEGIN);

		InventoryAllocationUIData laInvAlloctnUIData =
			(InventoryAllocationUIData) UtilityMethods.copy(aaData);
		InventoryAllocationUIData laInvAlloctnUIDataEnd =
			(InventoryAllocationUIData) UtilityMethods.copy(aaData);
		ComputeInventoryData laComputeInvData =
			new ComputeInventoryData();
		ComputeInventoryData laComputeInvDataII =
			new ComputeInventoryData();
		ValidateInventoryPattern laValidateInvPatrn =
			new ValidateInventoryPattern();
		int liInvQty = 0;

		try
		{
			// Test if inventory quantity needs to be calculated from 
			// the begin and end item number
			if (laInvAlloctnUIData.getInvQty() == 0
				&& !laInvAlloctnUIData.getInvItmEndNo().equals(
					CommonConstant.STR_SPACE_EMPTY))
			{
				// Calculate the begin pattern sequence number
				laComputeInvData =
					laValidateInvPatrn.validateItmNoInput(
						laInvAlloctnUIData);
				laInvAlloctnUIData =
					(InventoryAllocationUIData) calcInvNo(laComputeInvData);
				// Calculate the end pattern sequence number
				laInvAlloctnUIDataEnd.setInvItmNo(
					laInvAlloctnUIDataEnd.getInvItmEndNo());
				laComputeInvData =
					laValidateInvPatrn.validateItmNoInput(
						laInvAlloctnUIDataEnd);
				laInvAlloctnUIDataEnd =
					(InventoryAllocationUIData) calcInvNo(laComputeInvData);
				// defect 9136
				if (laInvAlloctnUIData.getPatrnSeqCd()
					!= laInvAlloctnUIDataEnd.getPatrnSeqCd())
				{
					RTSException leRTSEx =
						new RTSException(
							ErrorsConstant
								.ERR_NUM_COMPUTED_NUMBER_INVALID);
					throw leRTSEx;
				}
				// end defect 9136
				// Calculate the inventory
				// set the endpatrnseqno
				laInvAlloctnUIData.setEndPatrnSeqNo(
					laInvAlloctnUIDataEnd.getPatrnSeqNo());

				liInvQty =
					laInvAlloctnUIDataEnd.getPatrnSeqNo()
						- laInvAlloctnUIData.getPatrnSeqNo()
						+ 1;

				// defect 9116
				//// If the quantity is negative throw error 10.
				//if (liInvQty <= InventoryConstant.MIN_QTY)
				//{
				//	RTSException leRTSEx =
				//		new RTSException(
				//			ErrorsConstant
				//				.ERR_NUM_COMPUTED_NUMBER_INVALID);
				//	throw leRTSEx;
				//}
				// If the quantity does not fall within the min and max
				// range, throw error ERR_NUM_INVALID_QTY.
				// allow the qty to go to the max possible int.
				if (liInvQty <= InventoryConstant.MIN_QTY
					|| liInvQty > InventoryConstant.INV_MAX_COMPUTED_QTY)
				{
					// end defect 9116
					RTSException laRTSEx =
						new RTSException(
							ErrorsConstant.ERR_NUM_INVALID_QTY);
					throw laRTSEx;
				}
				// Prepare return data
				laInvAlloctnUIData.setInvQty(liInvQty);
			}
			// The inventory end item needs to be calculated from the
			// begin item number and the quantity
			else
			{
				// If the quantity is not valid, throw error 
				// ERR_NUM_INVALID_QTY.
				// defect 9116
				// allow the qty to go to int max.
				if (laInvAlloctnUIData.getInvQty()
					<= InventoryConstant.MIN_QTY
					|| laInvAlloctnUIData.getInvQty()
						> InventoryConstant.MAX_QTY)
				{
					// end defect 9116
					RTSException leRTSEx =
						new RTSException(
							ErrorsConstant.ERR_NUM_INVALID_QTY);
					throw leRTSEx;
				}

				// Calculate the end item number from the begin item 
				// number and quantity
				laComputeInvData =
					laValidateInvPatrn.validateItmNoInput(
						laInvAlloctnUIData);
				laComputeInvDataII =
					(ComputeInventoryData) UtilityMethods.copy(
						laComputeInvData);
				laInvAlloctnUIData =
					(InventoryAllocationUIData) calcInvNo(laComputeInvData);

				// if qty is one, just use the begin patrnseqno.
				// otherwise calculate.	
				if (laInvAlloctnUIData.getInvQty() != 1)
				{
					laComputeInvDataII.setInvAlloctnData(
						laInvAlloctnUIData);
					laInvAlloctnUIData =
						(InventoryAllocationUIData) calcInvEndNo(laComputeInvDataII);
				}
				else
				{
					laInvAlloctnUIData.setInvItmEndNo(
						laInvAlloctnUIData.getInvItmNo());
					laInvAlloctnUIData.setEndPatrnSeqNo(
						laInvAlloctnUIData.getPatrnSeqNo());
				}
			}
			return laInvAlloctnUIData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(Log.METHOD, this, MSG_CALCINVUNKNOWN_END);
		}
	}

	/**
	 * Method description
	 * 
	 * @param aaData
	 * @return
	 * @throws RTSException
	 */
	public Object calcInvUnknownVI(InventoryAllocationData aaData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_CALCINVUNKNOWN_BEGIN);
		InventoryAllocationUIData laInvAllocUIData =
			new InventoryAllocationUIData();

		try
		{
			laInvAllocUIData.setCalcInv(aaData.isCalcInv());
			laInvAllocUIData.setItrntReq(aaData.isItrntReq());
			laInvAllocUIData.setISA(aaData.isISA());
			laInvAllocUIData.setUserPltNo(aaData.isUserPltNo());
			laInvAllocUIData.setVirtual(aaData.isVirtual());
			laInvAllocUIData.setEndPatrnSeqNo(
				aaData.getEndPatrnSeqNo());
			laInvAllocUIData.setInvItmYr(aaData.getInvItmYr());
			laInvAllocUIData.setInvQty(aaData.getInvQty());
			laInvAllocUIData.setInvStatusCd(aaData.getInvStatusCd());
			laInvAllocUIData.setOfcIssuanceNo(
				aaData.getOfcIssuanceNo());
			laInvAllocUIData.setPatrnSeqCd(aaData.getPatrnSeqCd());
			laInvAllocUIData.setPatrnSeqNo(aaData.getPatrnSeqNo());
			laInvAllocUIData.setSubstaId(aaData.getSubstaId());
			laInvAllocUIData.setTransAmDate(aaData.getTransAmDate());
			laInvAllocUIData.setTransTime(aaData.getTransTime());
			laInvAllocUIData.setTransWsId(aaData.getTransWsId());
			laInvAllocUIData.setInvcNo(aaData.getInvcNo());
			laInvAllocUIData.setInvId(aaData.getInvId());
			laInvAllocUIData.setInvItmEndNo(aaData.getInvItmEndNo());
			laInvAllocUIData.setInvItmNo(aaData.getInvItmNo());
			laInvAllocUIData.setInvLocIdCd(aaData.getInvLocIdCd());
			laInvAllocUIData.setItmCd(aaData.getItmCd());
			laInvAllocUIData.setTransEmpId(aaData.getTransEmpId());
			laInvAllocUIData.setRequestorIpAddress(
				aaData.getRequestorIpAddress());
			laInvAllocUIData.setRequestorRegPltNo(
				aaData.getRequestorRegPltNo());
			laInvAllocUIData.setViItmCd(aaData.getViItmCd());
			laInvAllocUIData.setInvHoldTimeStmp(
				aaData.getInvHoldTimeStmp());
			laInvAllocUIData.setCustSupplied(aaData.isCustSupplied());
			laInvAllocUIData.setHoopsRegPltNo(
				aaData.getHoopsRegPltNo());
			laInvAllocUIData.setMfgPltNo(aaData.getMfgPltNo());

			InventoryAllocationData laInvAlloctnData =
				(InventoryAllocationData) calcInvUnknown(
					(Object) laInvAllocUIData);

			return laInvAlloctnData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			Log.write(Log.METHOD, this, MSG_CALCINVUNKNOWN_END);
		}
	}

	/**
	 * This method returns a boolean indicating if the Plate 
	 * Number is provided allows duplicates.  
	 * If so, we do not have to validate if there are duplicates.
	 *
	 * @param asItmCd String 
	 * @return boolean 
	 */
	public static boolean chkIfItmAllowsDups(String asItmCd)
	{
		PlateTypeData laPltType = PlateTypeCache.getPlateType(asItmCd);
		if (laPltType != null)
		{
			return (laPltType.getDuplsAllowdCd() > 0);
		}
		else
		{
			return false;
		}
	}

	/**
	 * This method returns a boolean indicating if the Plate 
	 * Number is provided by the User or the system.
	 * 
	 * <p>Personalized Plates, ROP's, and Old Plates typically have 
	 * plate numbers provided by the User.  
	 * This is indicated by the User Plate Indicator of 1.
	 * 
	 * <p>All other plates follow a pattern defined in the system.
	 * 
	 * <p>Former methodology:
	 * <br>Check if the inventory item is of type PLP, OLDPLT, or ROP.
	 * <br>True if any of these plate types.
	 * <br>False if it is anything else.
	 *
	 * @param asItmCd String 
	 * @return boolean 
	 */
	public static boolean chkIfItmPLPOrOLDPLTOrROP(String asItmCd)
	{
		// defect 9116
		//boolean lbReturn = false;

		PlateTypeData laPltType = PlateTypeCache.getPlateType(asItmCd);
		if (laPltType != null)
		{
			return (laPltType.getUserPltNoIndi() == 1);
		}
		else
		{
			return false;
		}

		// we do not need to do it this way any more.
		//if (asItmCd.startsWith(PERSONALIZED_PLATE))
		//{
		//	lbReturn = true;
		//}
		//else if (asItmCd.startsWith(OLD_PLATE))
		//{
		//	lbReturn = true;
		//}
		//else if (asItmCd.startsWith(RADIO_OPERATOR_PLATE))
		//{
		//	lbReturn = true;
		//}

		//return lbReturn;
		// end defect 9116
	}

	/**
	 * Accept an Item and check to see if it matches any patterns.
	 * If there is a match, place that pattern in the return Vector.
	 * Every pattern that matches is added to the Vector.
	 * 
	 * @param asItem String - Item to be checked
	 * @return Vector
	 * @throws RTSException
	 */
	public static Vector chkItmForPatternMatch(String asItem)
		throws RTSException
	{
		Vector lvFoundPatterns = new Vector();
		try
		{
			Vector lvItemCodes = ItemCodesCache.getItmCdsForSpcPltChk();
			for (Iterator laNextIC = lvItemCodes.iterator();
				laNextIC.hasNext();
				)
			{
				ItemCodesData laItemCode =
					(ItemCodesData) laNextIC.next();

				InventoryAllocationUIData laIAUID =
					new InventoryAllocationUIData();
				laIAUID.setInvItmNo(asItem);
				laIAUID.setInvItmEndNo(asItem);
				laIAUID.setItmCd(laItemCode.getItmCd());
				ValidateInventoryPattern laVIP =
					new ValidateInventoryPattern();
				try
				{
					ComputeInventoryData laCID =
						laVIP.validateItmNoInput(laIAUID);
					if (laCID.getValidItmNo())
					{
						lvFoundPatterns.add(laItemCode);
					}
				}
				catch (RTSException aeRTSEx)
				{
					if (aeRTSEx.getCode()
						== ErrorsConstant.ERR_NUM_ITM_YEAR_NOT_VALID)
					{
						// if the item code and year combination was not 
						// found, try pulling for all years
						try
						{
							laIAUID.setInvItmYr(-1);
							ComputeInventoryData laCID =
								laVIP.validateItmNoInput(laIAUID);
							if (laCID.getValidItmNo())
							{
								lvFoundPatterns.add(laItemCode);
							}
						}
						catch (RTSException aeRTSEx2)
						{
							// not sure yet
						}
					}
					else
					{
						// do nothing on this exception.
						// we actually want an exception.
					}
				}
			}

		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeEx);
		}
		return lvFoundPatterns;
	}
	
	/**
	 * Test if the alpha strings can/should be consolidated
	 * 
	 * <p>Returns a vector where the elements are:
	 * <ol>
	 * <li>The new PatrnCd.
	 * <li>The new Position.
	 * <li>The TmpAlphaPatrnCd.
	 * <eol>
	 * 
	 * @param aaComputeInvData ComputeInventoryData
	 * @param aiPosition int
	 * @param asInvItmPatrnCd String
	 * @param aiPatrnCd int
	 * @return Vector
	 * @throws RTSException  
	 */
	public Vector consolidateAlpha(
		ComputeInventoryData aaComputeInvData,
		int aiPosition,
		String asInvItmPatrnCd,
		int aiPatrnCd)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_CONSOLIDATEALPHA_BEGIN);

		Vector lvInvNoPatrn = aaComputeInvData.getInvNoPatrn();
		Vector lvMinInvNoPatrn = aaComputeInvData.getMinInvNoPatrn();
		Vector lvMaxInvNoPatrn = aaComputeInvData.getMaxInvNoPatrn();

		String lsTmpAlpha = new String();
		String lsTmpMinAlpha = new String();
		String lsTmpMaxAlpha = new String();
		int liTmpLen = 0;
		String lsTmpAlphaPatrnCd = new String();
		// keep track of the smallest pattern code seen to return to 
		// caller.  initialize to 4
		int liSmallestPattern = ALPHA_POS_3 + 1;

		do
		{
			switch (aiPatrnCd)
			{
				case ALPHA_POS_1 :
					lsTmpAlpha =
						(String) lvInvNoPatrn.get(aiPatrnCd)
							+ lsTmpAlpha;
					lsTmpMinAlpha =
						(String) lvMinInvNoPatrn.get(aiPatrnCd)
							+ lsTmpMinAlpha;
					lsTmpMaxAlpha =
						(String) lvMaxInvNoPatrn.get(aiPatrnCd)
							+ lsTmpMaxAlpha;
					liTmpLen =
						((String) lvMinInvNoPatrn.get(aiPatrnCd))
							.length()
							+ liTmpLen;
					lsTmpAlphaPatrnCd =
						String.valueOf(aiPatrnCd)
							+ String.valueOf(
								((String) lvMinInvNoPatrn
									.get(aiPatrnCd))
									.length())
							+ lsTmpAlphaPatrnCd;
					// set SmallestPattern to 1 always here
					liSmallestPattern = ALPHA_POS_1;
					break;

				case ALPHA_POS_2 :
					lsTmpAlpha =
						(String) lvInvNoPatrn.get(aiPatrnCd)
							+ lsTmpAlpha;
					lsTmpMinAlpha =
						(String) lvMinInvNoPatrn.get(aiPatrnCd)
							+ lsTmpMinAlpha;
					lsTmpMaxAlpha =
						(String) lvMaxInvNoPatrn.get(aiPatrnCd)
							+ lsTmpMaxAlpha;
					liTmpLen =
						((String) lvMinInvNoPatrn.get(aiPatrnCd))
							.length()
							+ liTmpLen;
					lsTmpAlphaPatrnCd =
						String.valueOf(aiPatrnCd)
							+ String.valueOf(
								((String) lvMinInvNoPatrn
									.get(aiPatrnCd))
									.length())
							+ lsTmpAlphaPatrnCd;
					// set SmallestPattern to 2 if it is bigger than 2 
					//  currently
					if (liSmallestPattern > ALPHA_POS_2)
					{
						liSmallestPattern = ALPHA_POS_2;
					}
					break;

				case ALPHA_POS_3 :
					lsTmpAlpha =
						(String) lvInvNoPatrn.get(aiPatrnCd)
							+ lsTmpAlpha;
					lsTmpMinAlpha =
						(String) lvMinInvNoPatrn.get(aiPatrnCd)
							+ lsTmpMinAlpha;
					lsTmpMaxAlpha =
						(String) lvMaxInvNoPatrn.get(aiPatrnCd)
							+ lsTmpMaxAlpha;
					liTmpLen =
						((String) lvMinInvNoPatrn.get(aiPatrnCd))
							.length()
							+ liTmpLen;
					lsTmpAlphaPatrnCd =
						String.valueOf(aiPatrnCd)
							+ String.valueOf(
								((String) lvMinInvNoPatrn
									.get(aiPatrnCd))
									.length())
							+ lsTmpAlphaPatrnCd;
					// set SmallestPattern to 3 if it is bigger than 3 
					//  currently
					if (liSmallestPattern > ALPHA_POS_3)
					{
						liSmallestPattern = ALPHA_POS_3;
					}
					break;
			}

			if (aiPosition + 1 >= asInvItmPatrnCd.length()
				|| Integer.parseInt(
					asInvItmPatrnCd.substring(
						aiPosition + 1,
						aiPosition + 2))
					> ALPHA_POS_3)
				break;

			aiPosition = aiPosition + 1;
			aiPatrnCd =
				Integer.parseInt(
					asInvItmPatrnCd.substring(
						aiPosition,
						aiPosition + 1));
		}
		while (aiPatrnCd < ALPHA_POS_3);

		// return the SmallestPattern code seen
		// used to always return a 1..
		aiPatrnCd = liSmallestPattern;

		Vector lvReturnAlpha = new Vector();
		lvReturnAlpha.add(new Integer(aiPatrnCd));
		lvReturnAlpha.add(new Integer(aiPosition));
		lvReturnAlpha.add(lsTmpAlphaPatrnCd);

		lvInvNoPatrn.setElementAt(lsTmpAlpha, aiPatrnCd);
		lvMinInvNoPatrn.setElementAt(lsTmpMinAlpha, aiPatrnCd);
		lvMaxInvNoPatrn.setElementAt(lsTmpMaxAlpha, aiPatrnCd);

		Log.write(Log.METHOD, this, MSG_CONSOLIDATEALPHA_END);

		return lvReturnAlpha;
	}

	/**
	 * Used to calculate the decimalized value of a group of characters.
	 * <p>
	 * Include Min and Max Strings to check for constants.
	 * If a character is a constant, set its value to 0.
	 * 
	 * @param asAlpha 		String 	The group of characters.
	 * @param asMinAlpha	String	Min Characters
	 * @param asMaxAlpha	String	Max Characters
	 * @param aInvPatrnsData InventoryPatternsData
	 * @return int  
	 * @throws RTSException  
	 */
	public int decimalizeAlpha(
		String asAlpha,
		String asMinAlpha,
		String asMaxAlpha,
		InventoryPatternsData aaInvPatrnsData)
		throws RTSException
	{
		double ldBase = aaInvPatrnsData.getValidInvLtrs().length();
		int liAlphaLen = asAlpha.length();
		int liDecimalAlpha = 0;
		int liCharIndx = -1;
		String lsAlphaChar = null;
		String lsMinAlphaChar = null;
		String lsMaxAlphaChar = null;

		for (int j = 0; j < liAlphaLen; j++)
		{
			// parse out the character from the string
			lsAlphaChar = asAlpha.substring(j, j + 1);
			// try to get the decimal value of this charater
			liCharIndx =
				aaInvPatrnsData.getValidInvLtrs().indexOf(lsAlphaChar);
			// if the CharIndx is -1, check for constant.  
			// Otherwise the Char is invalid.
			if (liCharIndx == -1)
			{
				// parse out the min and max characters
				lsMinAlphaChar = asMinAlpha.substring(j, j + 1);
				lsMaxAlphaChar = asMaxAlpha.substring(j, j + 1);
				// check to see if this is a constant
				if (lsMinAlphaChar.equals(lsMaxAlphaChar)
					&& lsAlphaChar.equals(lsMaxAlphaChar))
				{
					// if it is, set the decimal value to zero
					liDecimalAlpha = 0;
					// also set the index to 0.
					liCharIndx = 0;
				}
				else
				{
					// Return -1.  Not a valid character for this pattern.
					liDecimalAlpha = -1;
					break;
				}
			}
			// LongAlphaValue is based on where the character is in the
			// range of valid characters.  It is incremented by its 
			// relative position in the overall string.
			// defect 9116
			//Long lLongAlphaValue =
			//	new Long(
			//		liCharIndx
			//			* Math.round(
			//				Math.exp(
			//					(liAlphaLen - (j + 1))
			//						* Math.log(ldBase))));
			Long laLongAlphaValue =
				new Long(
					liCharIndx
						* Math.round(
							Math.pow(ldBase, (liAlphaLen - (j + 1)))));
			// end defect 9116
			// add the char's decimal value to the overall string's value.
			liDecimalAlpha =
				liDecimalAlpha + laLongAlphaValue.intValue();
		}
		return liDecimalAlpha;
	}

	/**
	 * Determines the values for which the decimalized inventory range
	 * needs to be adjusted to take into account the invalid letter 
	 * combinations.
	 * 
	 * <p>The Vector returned has the following two elements:
	 * <ol>
	 * <li>AlphaInvalid - Integer
	 * <li>RangeAlphaInvalid - Integer
	 * <eol>
	 *  
	 * <p>Pass the Min and Max into the new Decimalization Routine.
	 * 
	 * <p>These vectors holds data about the inventory pattern
	 *   Element  0 - (String) the alphanumeric inventory pattern 
	 * 					for that item
	 *   Element  1 - (String) the first group of characters
	 *   Element  2 - (String) the second group of characters
	 *   Element  3 - (String) the third group of characters
	 *   Element  4 - (String) the first group of numeric characters
	 *   Element  5 - (String) the second group of numeric characters
	 *   Element  6 - (String) the third group of numeric characters
	 *   Element  7 - (String) the inventory item number
	 *   Element  8 - (Integer) the decimalized value for the first
	 * 					group of characters
	 *   Element  9 - (Integer) the decimalized value for the second
	 * 					group of characters
	 *   Element 10 - (Integer) the decimalized value for the third
	 * 					group of characters
	 *  
	 * @param aComputeInvData ComputeInventoryData
	 * @param aiPatrnCd int
	 * @return Vector 
	 * @throws RTSException 
	 */
	public Vector findInvldLtrCombos(
		ComputeInventoryData aaComputeInvData,
		int aiPatrnCd)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_FINDINVLDLTRCOMBOS_BEGIN);

		Vector lvInvNoPatrn = aaComputeInvData.getInvNoPatrn();
		Vector lvMinInvNoPatrn = aaComputeInvData.getMinInvNoPatrn();
		Vector lvMaxInvNoPatrn = aaComputeInvData.getMaxInvNoPatrn();
		Vector lvOrigMinInvNoPatrn =
			aaComputeInvData.getOrigMinInvNoPatrn();
		Vector lvOrigMaxInvNoPatrn =
			aaComputeInvData.getOrigMaxInvNoPatrn();

		ValidateInventoryPattern laValidateInvPatrn =
			new ValidateInventoryPattern();
		boolean lbNegativeQty = false;
		String lsItmCd =
			aaComputeInvData.getInvAlloctnData().getItmCd();
		String lsAlpha = (String) lvInvNoPatrn.get(aiPatrnCd);

		// Get the Min and Max to pass into new decimalization routine
		String lsMinAlpha = (String) lvMinInvNoPatrn.get(aiPatrnCd);
		String lsMaxAlpha = (String) lvMaxInvNoPatrn.get(aiPatrnCd);

		int liAlphaLen = lsAlpha.length();
		double ldBase =
			aaComputeInvData
				.getInvPatrnsData()
				.getValidInvLtrs()
				.length();
		int liDecimalAlpha =
			((Integer) lvInvNoPatrn
				.get(aiPatrnCd + CommonConstant.ELEMENT_7))
				.intValue();
		int liDecimalMinAlpha =
			((Integer) lvMinInvNoPatrn
				.get(aiPatrnCd + CommonConstant.ELEMENT_7))
				.intValue();
		int liDecimalMinOrigAlpha =
			((Integer) lvOrigMinInvNoPatrn
				.get(aiPatrnCd + CommonConstant.ELEMENT_7))
				.intValue();
		int liDecimalMaxAlpha =
			((Integer) lvMaxInvNoPatrn
				.get(aiPatrnCd + CommonConstant.ELEMENT_7))
				.intValue();
		String lsInvldLtrCombo = new String();
		int liDecimalInvldLtr = 0;
		int liAlphaInvalid = 0;
		int liRangeAlphaInvalid = 0;

		if (aaComputeInvData.getInvAlloctnData().getInvQty() >= 0)
		{
			lbNegativeQty = false;
		}
		else
		{
			lbNegativeQty = true;
		}
		// defect 10248
		//Allow invalids to work for 2 letter combinations per table 
		// entries.
		// This if statement is not really needed.  
		//if (liAlphaLen >= 2
		//	|| !(liAlphaLen == 2
		//		&& !lsItmCd.equals(TRUCK_PLATE)
		//		&& !lsItmCd.equals(COMBINATION_PLATE)))
		//{
			// allow all 2 letter alpha to come in here.  
			// Use the table entries to determine what invalids apply.
			// Do not restrict to TKP and CP.
			if (liAlphaLen == 2)
			//	&& (lsItmCd.equals(TRUCK_PLATE)
			//		|| lsItmCd.equals(COMBINATION_PLATE)))
			{
				Vector lvInvldLtr = new Vector();
				// note that cache returns null if no values found.
				//lvInvldLtr.addAll(
				//	InvalidLetterCache.getInvldLtrs(lsItmCd));
				Vector lvItemInvalidLtrs = 
					InvalidLetterCache.getInvldLtrs(lsItmCd);
				if (lvItemInvalidLtrs != null)
				{
					lvInvldLtr.addAll(lvItemInvalidLtrs);
				}
				//end defect 10248

				for (int k = 0; k < lvInvldLtr.size(); k++)
				{
					lsInvldLtrCombo =
						((InvalidLetterData) lvInvldLtr.get(k))
							.getInvldLtrCombo();

					// pass min and max into new decimalization routine
					liDecimalInvldLtr =
						laValidateInvPatrn.decimalizeAlpha(
							lsInvldLtrCombo,
							lsMinAlpha,
							lsMaxAlpha,
							aaComputeInvData.getInvPatrnsData());

					// Test if Alpha Invalid positive or negative
					if (!lbNegativeQty
						&& liDecimalInvldLtr <= liDecimalAlpha
						&& liDecimalInvldLtr > liDecimalMinAlpha)
					{
						liAlphaInvalid = liAlphaInvalid + 1;
					}
					else if (
						lbNegativeQty
							&& liDecimalInvldLtr >= liDecimalAlpha
							&& liDecimalInvldLtr < liDecimalMaxAlpha)
					{
						liAlphaInvalid = liAlphaInvalid + 1;
					}

					// Test if Alpha Invalid for the range
					if (liDecimalInvldLtr <= liDecimalMaxAlpha
						&& liDecimalInvldLtr >= liDecimalMinAlpha)
					{
						liRangeAlphaInvalid = liRangeAlphaInvalid + 1;
					}
				}
			}

			// Test if invalid letter combinations in range for exact 
			//  3 letters
			else if (liAlphaLen == 3)
			{
				Vector lvInvldLtr = new Vector();
				lvInvldLtr.addAll(
					InvalidLetterCache.getInvldLtrs(
						CommonConstant.STR_SPACE_EMPTY));
				for (int k = 0; k < lvInvldLtr.size(); k++)
				{
					lsInvldLtrCombo =
						((InvalidLetterData) lvInvldLtr.get(k))
							.getInvldLtrCombo();

					// pass min and max into new decimalization routine
					liDecimalInvldLtr =
						laValidateInvPatrn.decimalizeAlpha(
							lsInvldLtrCombo,
							lsMinAlpha,
							lsMaxAlpha,
							aaComputeInvData.getInvPatrnsData());

					// Test if Alpha Invalid positive
					if (!lbNegativeQty
						&& liDecimalInvldLtr <= liDecimalAlpha
						&& liDecimalInvldLtr > liDecimalMinAlpha)
					{
						liAlphaInvalid = liAlphaInvalid + 1;
					}
					else if (
						lbNegativeQty
							&& liDecimalInvldLtr >= liDecimalAlpha
							&& liDecimalInvldLtr < liDecimalMaxAlpha)
					{
						liAlphaInvalid = liAlphaInvalid + 1;
					}

					// Test if Alpha Invalid for the range
					if (liDecimalInvldLtr <= liDecimalMaxAlpha
						&& liDecimalInvldLtr >= liDecimalMinAlpha)
					{
						liRangeAlphaInvalid = liRangeAlphaInvalid + 1;
					}
				}
			}
			// Test if invalid letter combinations in range for more 
			//  than 3 letters (addition)
			else if (liAlphaLen > 3 && !lbNegativeQty)
			{
				int liAlphaOrigMinValue =
					((Integer) lvOrigMinInvNoPatrn.get(aiPatrnCd + 7))
						.intValue();
				int liAlphaOrigHolder3MinValue2 = 0;
				// Calculate left side with increment
				int liHolder = liDecimalAlpha;
				int liHolder2 = liDecimalMinAlpha;
				int liHolder3 = liDecimalMaxAlpha;
				int liAlphaHolder3Value = 0;
				int liAlphaHolder3MinValue = 0;
				int liAlphaHolder3MaxValue = 0;

				// Calculate left side value
				for (int j = (liAlphaLen - 1); j > 0; j--)
				{

					liAlphaHolder3Value
						+= (liHolder / Math.round(Math.pow(ldBase, j)))
						* Math.round(Math.pow(ldBase, (j - 1)));

					liHolder =
						new Long(
							liHolder % Math.round(Math.pow(ldBase, j)))
							.intValue();

					liAlphaHolder3MinValue
						+= (liHolder2 / Math.round(Math.pow(ldBase, j)))
						* Math.round(Math.pow(ldBase, (j - 1)));

					liHolder2 =
						new Long(
							liHolder2
								% Math.round(Math.pow(ldBase, j)))
							.intValue();

					liAlphaHolder3MaxValue
						+= (liHolder3 / Math.round(Math.pow(ldBase, j)))
						* Math.round(Math.pow(ldBase, (j - 1)));

					liHolder3 =
						new Long(
							liHolder3
								% Math.round(Math.pow(ldBase, j)))
							.intValue();
				}

				// Format AlphaValueIncrement for addition
				liHolder =
					new Double(liAlphaOrigMinValue % ldBase).intValue();
				int liAlphaMaxValueIncrement =
					new Double((liDecimalMaxAlpha % ldBase) + 1)
						.intValue();
				int liAlphaValueIncrement =
					liAlphaMaxValueIncrement - liHolder;

				// Calculate right side with increment
				int liAlphaHolder3Value2 =
					new Long(
						liDecimalAlpha
							% Math.round(
								Math.pow(ldBase, (liAlphaLen - 1))))
						.intValue();

				int liAlphaHolder3MinValue2 =
					new Long(
						liDecimalMinAlpha
							% Math.round(
								Math.pow(ldBase, (liAlphaLen - 1))))
						.intValue();

				// Test if AlphaOriginalMinValue = AlphaMinValue
				// defect 10248
				// add braces per standards
				if (liAlphaOrigMinValue == liDecimalMinAlpha)
				{
					liAlphaOrigHolder3MinValue2 =
						liAlphaHolder3MinValue2;
				}
				// end defect 10248
				int liAlphaHolder3MaxValue2 =
					new Long(
						liDecimalMaxAlpha
							% Math.round(
								Math.pow(ldBase, (liAlphaLen - 1))))
						.intValue();

				int liAlphaValueIncrement2 =
					new Long(
						(liDecimalAlpha
							/ Math.round(
								Math.pow(ldBase, (liAlphaLen - 1))))
							- (liDecimalMinAlpha
								/ Math.round(
									Math.pow(
										ldBase,
										(liAlphaLen - 1)))))
						.intValue()
						+ 1;

				int liAlphaMaxValueIncrement2 =
					new Long(
						(liDecimalMaxAlpha
							/ Math.round(
								Math.pow(ldBase, (liAlphaLen - 1))))
							- (liDecimalMinOrigAlpha
								/ Math.round(
									Math.pow(
										ldBase,
										(liAlphaLen - 1)))))
						.intValue()
						+ 1;

				// defect 9714
				// create min for checking mid range invalids.
				int liMidRangeMinValue = 0;

				int liMidRangeIncrementValue = liAlphaValueIncrement2;

				String lsCheckMinAlphaRight =
									aaComputeInvData
										.getInvPatrnsData()
										.getValidInvLtrs()
										.substring(
										0,
										1);

				lsCheckMinAlphaRight =
									UtilityMethods.addPadding(
										lsCheckMinAlphaRight,
										lsMinAlpha.length(),
										lsCheckMinAlphaRight);

				if (lsMinAlpha.equals(lsCheckMinAlphaRight))
				{
					liMidRangeMinValue = liAlphaHolder3MinValue2;
				}
				else
				{
					liMidRangeMinValue =
										laValidateInvPatrn
											.decimalizeAlpha(
											lsCheckMinAlphaRight,
											lsCheckMinAlphaRight,
											lsMaxAlpha,
											aaComputeInvData
												.getInvPatrnsData());
				}
				// end defect 9714

				// Test if in invalid range for More than 3 addition
				Vector lvInvldLtr = new Vector();
				lvInvldLtr.addAll(
					InvalidLetterCache.getInvldLtrs(
						CommonConstant.STR_SPACE_EMPTY));
				for (int k = 0; k < lvInvldLtr.size(); k++)
				{
					lsInvldLtrCombo =
						((InvalidLetterData) lvInvldLtr.get(k))
							.getInvldLtrCombo();

					// pass min and max into new decimalization routine
					liDecimalInvldLtr =
						laValidateInvPatrn.decimalizeAlpha(
							lsInvldLtrCombo,
							lsMinAlpha,
							lsMaxAlpha,
							aaComputeInvData.getInvPatrnsData());

					int liInvalid1Flag = 0;
					// Test if Alpha Invalid for first 3 addition
					if (liDecimalInvldLtr <= liAlphaHolder3Value
						&& liDecimalInvldLtr > liAlphaHolder3MinValue)
					{
						liAlphaInvalid =
							liAlphaInvalid + liAlphaValueIncrement;
						liInvalid1Flag = 1;
					}
					// Test if Invalid1Flag = 0
					if (liInvalid1Flag <= 0)
					{
						liInvalid1Flag = -3;
					}

					// Find alpha invalid for last 3 addition positive 
					// (end range)
					// defect 9714
					//	if (liDecimalInvldLtr != liAlphaHolder3MinValue
					//	 && liDecimalInvldLtr <= liAlphaHolder3Value2
					//	 && liDecimalInvldLtr > liAlphaHolder3MinValue2)
					if (liDecimalInvldLtr != liAlphaHolder3MinValue
						&& liDecimalInvldLtr <= liAlphaHolder3Value2)
					{
						// If this number starts with a letter greater
						// than the min letter, consider liMidRangeMinValue.
						// If this number starts with the min letter,
						// only consider liAlphaHolder3MinValue2.
						if ((liAlphaValueIncrement2 > 1 
						 && liDecimalInvldLtr > liMidRangeMinValue)
						 || liDecimalInvldLtr > liAlphaHolder3MinValue2)
						{
							// end defect 9714
							liAlphaInvalid = liAlphaInvalid + 1;
							liInvalid1Flag = liInvalid1Flag + 1;
						}
					}

					// Find alpha invalid for last 3 addition positive 
					// (begin range)
					if (liAlphaValueIncrement2 > 1
						&& liDecimalInvldLtr != liAlphaHolder3MaxValue
						&& liDecimalInvldLtr <= liAlphaHolder3MaxValue2
						&& liDecimalInvldLtr > liAlphaHolder3MinValue2)
					{
						liAlphaInvalid = liAlphaInvalid + 1;
						liInvalid1Flag = liInvalid1Flag + 1;
					}

					// defect 9714 
					// Use new mid range value here.
					// ==============================================
					// Test if invalid in middle range addition
					// modified handling of increment2 
					// rejected if statement
					// if (liMidRangeIncrementValue > 2
					//	&& liDecimalInvldLtr >= liAlphaOrigHolder3MinValue2
					//	&& liDecimalInvldLtr <= liAlphaHolder3MaxValue2)
					if (liAlphaValueIncrement2 > 2
						&& liDecimalInvldLtr
							>= liMidRangeMinValue
						&& liDecimalInvldLtr <= liAlphaHolder3MaxValue2)
					{
						// end defect 9714
						liAlphaInvalid =
							liAlphaInvalid + liAlphaValueIncrement2 - 2;
						liInvalid1Flag = liInvalid1Flag + 1;
					}

					// Test if duplicate needs to be removed for alpha 
					// addition/subtraction
					// defect 10248
					// add braces per standards
					if (lsInvldLtrCombo
						.substring(lsInvldLtrCombo.length() - 1)
						.equals(lsInvldLtrCombo.substring(1, 2))
						&& lsInvldLtrCombo.substring(
							lsInvldLtrCombo.length() - 1).equals(
							lsInvldLtrCombo.substring(0, 1))
						&& liInvalid1Flag >= 2)
					{
						liAlphaInvalid -= 1;
					}
					// end defect 10248
					liInvalid1Flag = 0;
					// Test if Alpha Invalid for first 3 max
					if (liDecimalInvldLtr <= liAlphaHolder3MaxValue
						&& liDecimalInvldLtr >= liAlphaHolder3MinValue)
					{
						liRangeAlphaInvalid =
							liRangeAlphaInvalid
								+ liAlphaMaxValueIncrement;
						liInvalid1Flag = 1;
					}
					// Test if Invalid1Flag = 0
					if (liInvalid1Flag <= 0)
					{
						liInvalid1Flag = -3;
					}
					// Find alpha invalid for last 3 max
					// Test if last invalid in start range max
					if (liDecimalInvldLtr <= liAlphaHolder3MaxValue2
						&& liDecimalInvldLtr >= liAlphaHolder3MinValue2)
					{
						liRangeAlphaInvalid = liRangeAlphaInvalid + 1;
						liInvalid1Flag = liInvalid1Flag + 1;
					}
					// Test if last invalid in end range max
					if (liAlphaValueIncrement2 >= 2
						&& liDecimalInvldLtr >= liAlphaHolder3MinValue2
						&& liDecimalInvldLtr <= liAlphaHolder3MaxValue2)
					{
						liRangeAlphaInvalid = liRangeAlphaInvalid + 1;
						liInvalid1Flag = liInvalid1Flag + 1;
					}
					// Test if last invalid in middle range max
					if (liAlphaValueIncrement2 > 2
						&& liDecimalInvldLtr >= liAlphaHolder3MinValue2
						&& liDecimalInvldLtr <= liAlphaHolder3MaxValue2)
					{
						liRangeAlphaInvalid =
							liRangeAlphaInvalid
								+ liAlphaMaxValueIncrement2
								- 2;
						liInvalid1Flag = liInvalid1Flag + 1;
					}
					// Test if duplicate needs to be removed for alpha max
					if (lsInvldLtrCombo
						.substring(lsInvldLtrCombo.length() - 1)
						.equals(lsInvldLtrCombo.substring(1, 2))
						&& lsInvldLtrCombo.substring(
							lsInvldLtrCombo.length() - 1).equals(
							lsInvldLtrCombo.substring(0, 1))
						&& liInvalid1Flag >= 2)
					{
						liRangeAlphaInvalid = liRangeAlphaInvalid - 1;
					}
				}
			}

			//////////////////////////////////////////////////////////
			// Test if invalid letter combinations in range for more 
			// than 3 letters (subtraction)
			else if (liAlphaLen > 3 && lbNegativeQty)
			{
				int liAlphaOrigMaxValue =
					((Integer) lvOrigMaxInvNoPatrn.get(aiPatrnCd + 7))
						.intValue();
				int liAlphaOrigHolder3MaxValue2 = 0;
				// Calculate left side with increment
				int liHolder = liDecimalAlpha;
				int liHolder2 = liDecimalMinAlpha;
				int liHolder3 = liDecimalMaxAlpha;
				int liAlphaHolder3Value = 0;
				int liAlphaHolder3MinValue = 0;
				int liAlphaHolder3MaxValue = 0;
				// Calculate right side value
				for (int j = (liAlphaLen - 1); j > 0; j--)
				{
					// casting problems if I add the field to the 
					// right of the equal sign.  leave as is for now.

					liAlphaHolder3Value
						+= (liHolder / Math.round(Math.pow(ldBase, j)))
						* Math.round(Math.pow(ldBase, (j - 1)));

					liHolder =
						new Long(
							liHolder % Math.round(Math.pow(ldBase, j)))
							.intValue();

					liAlphaHolder3MinValue
						+= (liHolder2 / Math.round(Math.pow(ldBase, j)))
						* Math.round(Math.pow(ldBase, (j - 1)));

					liHolder2 =
						new Long(
							liHolder2
								% Math.round(Math.pow(ldBase, j)))
							.intValue();

					liAlphaHolder3MaxValue
						+= (liHolder3 / Math.round(Math.pow(ldBase, j)))
						* Math.round(Math.pow(ldBase, (j - 1)));

					liHolder3 =
						new Long(
							liHolder3
								% Math.round(Math.pow(ldBase, j)))
							.intValue();
				}

				// Format AlphaValueIncrement for subtraction
				liHolder =
					new Double(liDecimalMinAlpha % ldBase).intValue();
				int liAlphaValueIncrement =
					new Double(liAlphaOrigMaxValue % ldBase).intValue();
				liAlphaValueIncrement =
					liAlphaValueIncrement - liHolder + 1;
				// Format AlphaValueIncrementA for subtraction
				liHolder =
					new Double(liDecimalAlpha % ldBase).intValue();
				int liAlphaValueIncrementA =
					new Double((liAlphaOrigMaxValue % ldBase))
						.intValue();
				liAlphaValueIncrementA =
					liAlphaValueIncrementA - liHolder + 1;
				// Format AlphaValueIncrementB for subtraction
				liHolder =
					new Double(liDecimalAlpha % ldBase).intValue();
				int liAlphaValueIncrementB =
					new Double((liAlphaOrigMaxValue % ldBase))
						.intValue();
				liAlphaValueIncrementB =
					liAlphaValueIncrementB - liHolder + 1;
				// Format AlphaValueIncrementC for subtraction
				liHolder =
					new Double(liDecimalMinAlpha % ldBase).intValue();
				int liAlphaValueIncrementC =
					new Double((liDecimalAlpha % ldBase)).intValue();
				liAlphaValueIncrementC =
					liAlphaValueIncrementC - liHolder + 1;

				// Calculate right side with increment for subtraction

				int liAlphaHolder3Value2 =
					new Long(
						liDecimalAlpha
							% Math.round(
								Math.pow(ldBase, (liAlphaLen - 1))))
						.intValue();

				int liAlphaHolder3MaxValue2 =
					new Long(
						liDecimalMaxAlpha
							% Math.round(
								Math.pow(ldBase, (liAlphaLen - 1))))
						.intValue();
				// Test if AlphaOriginalMinValue = AlphaMinValue
				if (liAlphaOrigMaxValue == liDecimalMaxAlpha)
				{
					liAlphaOrigHolder3MaxValue2 =
						liAlphaHolder3MaxValue2;
				}

				int liAlphaHolder3MinValue2 =
					new Long(
						liDecimalMinAlpha
							% Math.round(
								Math.pow(ldBase, (liAlphaLen - 1))))
						.intValue();

				int liAlphaValueIncrement2 =
					new Long(
						(liDecimalMaxAlpha
							/ Math.round(
								Math.pow(ldBase, (liAlphaLen - 1)))
							- (liDecimalAlpha
								/ Math.round(
									Math.pow(
										ldBase,
										(liAlphaLen - 1))))))
						.intValue()
						+ 1;

				// Test if in invalid range for More than 3 subtraction
				Vector lvInvldLtr = new Vector();
				lvInvldLtr.addAll(InvalidLetterCache.getInvldLtrs(""));
				for (int k = 0; k < lvInvldLtr.size(); k++)
				{
					lsInvldLtrCombo =
						((InvalidLetterData) lvInvldLtr.get(k))
							.getInvldLtrCombo();
					// pass min and max into new decimalization routine
					liDecimalInvldLtr =
						laValidateInvPatrn.decimalizeAlpha(
							lsInvldLtrCombo,
							lsMinAlpha,
							lsMaxAlpha,
							aaComputeInvData.getInvPatrnsData());

					int liInvalid1Flag = 0;
					// Test if Alpha Invalid for first 3 subtraction
					if (liDecimalInvldLtr < liAlphaHolder3MaxValue
						&& liDecimalInvldLtr >= liAlphaHolder3Value)
					{
						liAlphaInvalid =
							liAlphaInvalid + liAlphaValueIncrement;
						liInvalid1Flag = 1;
					}
					// Test if Invalid1Flag = 0
					if (liInvalid1Flag <= 0)
					{
						liInvalid1Flag = -3;
					}
					// Find alpha invalid for last 3 addition subtraction
					// Test if last invalid in start range subtraction
					if (liDecimalInvldLtr < liAlphaHolder3MaxValue2
						&& liDecimalInvldLtr >= liAlphaHolder3Value2)
					{
						liAlphaInvalid = liAlphaInvalid + 1;
						liInvalid1Flag = liInvalid1Flag + 1;
					}

					// Test if last invalid in middle range subtraction
					if (liAlphaValueIncrement2 >= 2
						&& liDecimalInvldLtr >= liAlphaHolder3MinValue2
						&& liDecimalInvldLtr
							<= liAlphaOrigHolder3MaxValue2)
					{
						liAlphaInvalid =
							liAlphaInvalid + liAlphaValueIncrement2 - 1;
						liInvalid1Flag = liInvalid1Flag + 1;
					}
					// Test if duplicate needs to be removed for alpha 
					// addition/subtraction 
					if (lsInvldLtrCombo
						.substring(lsInvldLtrCombo.length() - 1)
						.equals(lsInvldLtrCombo.substring(1, 2))
						&& lsInvldLtrCombo.substring(
							lsInvldLtrCombo.length() - 1).equals(
							lsInvldLtrCombo.substring(0, 1))
						&& liInvalid1Flag >= 2)
					{
						liAlphaInvalid = liAlphaInvalid - 1;
					}
				}
		// defect 10248
		//	}
		// end defect 10248
		}

		Vector lvReturnData = new Vector(CommonConstant.ELEMENT_2);
		lvReturnData.add(new Integer(liAlphaInvalid));
		lvReturnData.add(new Integer(liRangeAlphaInvalid));

		Log.write(Log.METHOD, this, MSG_FINDINVLDLTRCOMBOS_END);

		return lvReturnData;
	}

	/**
	 * Do check for pattern match and then see if there needs to 
	 * be a pattern match check on the Hoops version of the 
	 * item number.
	 * 
	 * @param laInvAlloctnData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector getMatchingPatterns(InventoryAllocationData laInvAlloctnData)
		throws RTSException
	{
		Vector lvMatchingPatterns =
			ValidateInventoryPattern.chkItmForPatternMatch(
				laInvAlloctnData.getInvItmNo());
		laInvAlloctnData.setHoopsRegPltNo(
			CommonValidations.convert_i_and_o_to_1_and_0(
				laInvAlloctnData.getInvItmNo()));

		// if the first check did not find a pattern and		
		// if the hoops plate number is different, check it for match
		// on hoops.
		if (lvMatchingPatterns.size() < 1
			&& !laInvAlloctnData.getInvItmNo().equals(
				laInvAlloctnData.getHoopsRegPltNo()))
		{
			Vector lvMatchingPatterns2 =
				ValidateInventoryPattern.chkItmForPatternMatch(
					laInvAlloctnData.getHoopsRegPltNo());
			if (lvMatchingPatterns2 != null
				&& lvMatchingPatterns2.size() > 0)
			{
				// hoops has a pattern match, use it.
				lvMatchingPatterns = lvMatchingPatterns2;
			}
		}
		return lvMatchingPatterns;
	}

	/**
	 * Populate the selected position of the vectors with the 
	 * default value.
	 * 
	 * @param avInvNoPatrn
	 * @param avMinInvNoPatrn
	 * @param avMaxInvNoPatrn
	 * @param avOrigInvNoPatrn
	 * @param avOrigMinInvNoPatrn
	 * @param avOrigMaxInvNoPatrn
	 * @param aiPosition
	 */
	private void populateVectorWithDefaultValues(
		Vector avInvNoPatrn,
		Vector avMinInvNoPatrn,
		Vector avMaxInvNoPatrn,
		Vector avOrigInvNoPatrn,
		Vector avOrigMinInvNoPatrn,
		Vector avOrigMaxInvNoPatrn,
		int aiPosition)
	{
		avInvNoPatrn.add(aiPosition, new Integer(0));
		avOrigInvNoPatrn.add(aiPosition, new Integer(0));
		avMinInvNoPatrn.add(aiPosition, new Integer(0));
		avOrigMinInvNoPatrn.add(aiPosition, new Integer(0));
		avMaxInvNoPatrn.add(aiPosition, new Integer(0));
		avOrigMaxInvNoPatrn.add(aiPosition, new Integer(0));
	}

	/**
	 * Given an inventory number calculates the alpha and numeric 
	 * groupings and their pattern.
	 * 
	 * <p>Examples:
	 * <ul>
	 * <li>69WD -> Alpha1 = WD; Numc1 = 69; InvNoPatrn = NA
	 * <li>B10LTR -> Alpha1 = B; Alpha2 = LTR; Numc1 = 10; 
	 * InvNoPatrn = ANA
	 * <li>CCC098 -> Alpha1 = CCC; Numc1 = 098; InvNoPatrn = AN
	 * <eul>
	 * 
	 * <p>The Vector returned has the following elements:
	 * <ol>
	 * <li>(String) the alphanumeric inventory pattern for that item
	 * <li>(String) the first group of characters
	 * <li>(String) the second group of characters
	 * <li>(String) the third group of characters
	 * <li>(String) the first group of numeric characters
	 * <li>(String) the second group of numeric characters
	 * <li>(String) the third group of numeric characters
	 * <li>(String) the inventory item number
	 * <eol>
	 * 
	 * @param asInvItmNo String 
	 * @return Vector 
	 * @throws RTSException Thrown when there is an error.
	 */
	private Vector getInvNoPatrn(String asInvItmNo) throws RTSException
	{
		//String lsAlphabet = new String(VALID_CHARACTERS);
		//String lsIntegers = new String(VALID_NUMBERS);
		String lsPrevChar = new String();
		String lsInvNoPatrn = new String();
		String lsNumc1 = new String();
		String lsNumc2 = new String();
		String lsNumc3 = new String();
		String lsAlpha1 = new String();
		String lsAlpha2 = new String();
		String lsAlpha3 = new String();

		for (int i = 0; i < asInvItmNo.length(); i++)
		{
			String lSubStr = asInvItmNo.substring(i, i + 1);
			if (CommonConstant.VALID_INTS.indexOf(lSubStr) > -1)
			{
				if (lsPrevChar.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					lsNumc1 = lsNumc1 + lSubStr;
					lsInvNoPatrn = lsInvNoPatrn + PATTERN_IS_NUMBER;
				}
				else if (lsPrevChar.equals(PATTERN_IS_NUMBER))
				{
					if (lsNumc2.equals(CommonConstant.STR_SPACE_EMPTY)
						&& lsNumc3.equals(CommonConstant.STR_SPACE_EMPTY))
					{
						lsNumc1 = lsNumc1 + lSubStr;
					}
					else if (
						lsNumc3.equals(CommonConstant.STR_SPACE_EMPTY))
					{
						lsNumc2 = lsNumc2 + lSubStr;
					}
					else
					{
						lsNumc3 = lsNumc3 + lSubStr;
					}
				}
				else if (lsPrevChar.equals(PATTERN_IS_ALPHA))
				{
					if (lsNumc1.equals(CommonConstant.STR_SPACE_EMPTY))
					{
						lsNumc1 = lsNumc1 + lSubStr;
					}
					else if (
						lsNumc2.equals(CommonConstant.STR_SPACE_EMPTY))
					{
						lsNumc2 = lsNumc2 + lSubStr;
					}
					else
					{
						lsNumc3 = lsNumc3 + lSubStr;
					}
					lsInvNoPatrn = lsInvNoPatrn + PATTERN_IS_NUMBER;
				}
				lsPrevChar = PATTERN_IS_NUMBER;
			}
			else if (
				CommonConstant.VALID_LETTERS.indexOf(lSubStr) > -1)
			{
				if (lsPrevChar.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					lsAlpha1 = lsAlpha1 + lSubStr;
					lsInvNoPatrn = lsInvNoPatrn + PATTERN_IS_ALPHA;
				}
				else if (lsPrevChar.equals(PATTERN_IS_ALPHA))
				{
					if (lsAlpha2.equals(CommonConstant.STR_SPACE_EMPTY)
						&& lsAlpha3.equals(
							CommonConstant.STR_SPACE_EMPTY))
					{
						lsAlpha1 = lsAlpha1 + lSubStr;
					}
					else if (
						lsAlpha3.equals(
							CommonConstant.STR_SPACE_EMPTY))
					{
						lsAlpha2 = lsAlpha2 + lSubStr;
					}
					else
					{
						lsAlpha3 = lsAlpha3 + lSubStr;
					}
				}
				else if (lsPrevChar.equals(PATTERN_IS_NUMBER))
				{
					if (lsAlpha1
						.equals(CommonConstant.STR_SPACE_EMPTY))
					{
						lsAlpha1 = lsAlpha1 + lSubStr;
					}
					else if (
						lsAlpha2.equals(
							CommonConstant.STR_SPACE_EMPTY))
					{
						lsAlpha2 = lsAlpha2 + lSubStr;
					}
					else
					{
						lsAlpha3 = lsAlpha3 + lSubStr;
					}
					lsInvNoPatrn = lsInvNoPatrn + PATTERN_IS_ALPHA;
				}
				lsPrevChar = PATTERN_IS_ALPHA;
			}
			else
			{
				// defect 8015
				// remove leading "0"
				RTSException lRTSException =
					new RTSException(
						ErrorsConstant.ERR_NUM_COULD_NOT_COMPUTE);
				// end defect 8015
				throw lRTSException;
			}
		}

		Vector lvInvNoPatrn = new Vector(CommonConstant.ELEMENT_8);
		lvInvNoPatrn.add(CommonConstant.ELEMENT_0, lsInvNoPatrn);
		lvInvNoPatrn.add(CommonConstant.ELEMENT_1, lsAlpha1);
		lvInvNoPatrn.add(CommonConstant.ELEMENT_2, lsAlpha2);
		lvInvNoPatrn.add(CommonConstant.ELEMENT_3, lsAlpha3);
		lvInvNoPatrn.add(CommonConstant.ELEMENT_4, lsNumc1);
		lvInvNoPatrn.add(CommonConstant.ELEMENT_5, lsNumc2);
		lvInvNoPatrn.add(CommonConstant.ELEMENT_6, lsNumc3);
		lvInvNoPatrn.add(CommonConstant.ELEMENT_7, asInvItmNo);

		return lvInvNoPatrn;
	}

	/**
	 * Given the inventory number patterns and a row from the 
	 * RTS_INV_PATTERNS table, checks to see if the Alpha inventory 
	 * groups contains the same pattern.
	 * 
	 * <p>Boolean returned is based on if pattern matches the 
	 * table pattern.
	 * <br>True means it matches.
	 * <br>False means it did not match.
	 * 
	 * @param avInvNoPatrn Vector 
	 * @param avMinInvNoPatrn Vector 
	 * @param avMaxInvNoPatrn Vector 
	 * @param aiIndx int
	 * @param aaInvPattrnsData InventoryPatternsData
	 * @return boolean  
	 * @throws RTSException 
	 */
	private boolean validateAlpha(
		Vector avInvNoPatrn,
		Vector avMinInvNoPatrn,
		Vector avMaxInvNoPatrn,
		int aiIndx,
		InventoryPatternsData aaInvPattrnsData)
		throws RTSException
	{
		boolean lbReturn = true;

		if (aaInvPattrnsData.getValidInvLtrs().length() != 0)
		{
			// make sure the length of the max string is not less 
			// than the item string length.  It is is return false
			if (((String) avInvNoPatrn.get(aiIndx)).length()
				> ((String) avMaxInvNoPatrn.get(aiIndx)).length()
				|| ((String) avInvNoPatrn.get(aiIndx)).length()
					< ((String) avMinInvNoPatrn.get(aiIndx)).length())
			{
				return false;
			}

			// extract the strings for easier handling.
			String lsAlpha = (String) avInvNoPatrn.get(aiIndx);
			String lsMinAlpha = (String) avMinInvNoPatrn.get(aiIndx);
			String lsMaxAlpha = (String) avMaxInvNoPatrn.get(aiIndx);

			try
			{
				// TODO What are we doing here?
				// Pass Min and Max into Decimalization for constant check.
				// decimalize the strings for comparision.
				int liDecimalAlpha =
					decimalizeAlpha(
						lsAlpha,
						lsMinAlpha,
						lsMaxAlpha,
						aaInvPattrnsData);
				int liDecimalMinAlpha =
					decimalizeAlpha(
						lsMinAlpha,
						lsMinAlpha,
						lsMaxAlpha,
						aaInvPattrnsData);
				int liDecimalMaxAlpha =
					decimalizeAlpha(
						lsMaxAlpha,
						lsMinAlpha,
						lsMaxAlpha,
						aaInvPattrnsData);

				// if all three are -1, it should return false 
				if (liDecimalAlpha >= liDecimalMinAlpha
					&& liDecimalAlpha <= liDecimalMaxAlpha)
				{
					lbReturn = true;
				}
				else
				{
					lbReturn = false;
				}
			}
			catch (RTSException aeRTSEx)
			{
				throw aeRTSEx;
			}
		}
		else if (
			!((String) avMinInvNoPatrn.get(aiIndx)).equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			if (!((String) avMinInvNoPatrn.get(aiIndx))
				.equals((String) avInvNoPatrn.get(aiIndx)))
			{
				boolean lbValid = true;

				for (int i = 0;
					(i < ((String) avInvNoPatrn.get(aiIndx)).length())
						&& (i
							< ((String) avMinInvNoPatrn.get(aiIndx))
								.length());
					i++)
				{
					char lcChar =
						((String) avInvNoPatrn.get(aiIndx)).charAt(i);
					char lcMinChar =
						((String) avMinInvNoPatrn.get(aiIndx)).charAt(
							i);
					char lcMaxChar =
						((String) avMaxInvNoPatrn.get(aiIndx)).charAt(
							i);

					if (lcChar >= lcMinChar && lcChar <= lcMaxChar)
					{
						continue;
					}
					else
					{
						lbValid = false;
						break;
					}
				}

				lbReturn = lbValid;
			}
			else
			{
				lbReturn = true;
			}
		}

		return lbReturn;
	}

	/**
	 * Used to validate the inventory number and also determines the 
	 * PatrnSeqCd.  When calculating the inventory range this is 
	 * required.  However, this can also be used as a stand along 
	 * method that can be used to validate the inventory number entered.
	 * In which case, the return object can be ignored.  If no exception
	 * is thrown, the inventory number is valid.  Otherwise, an 
	 * exception will be thrown.
	 *
	 * @param aaInvAllocData InvAllocData 
	 * @return ComputeInventoryData
	 * @throws RTSException 
	 */
	public ComputeInventoryData validateItmNoInput(InventoryAllocationData aaInvAllocData)
		throws RTSException
	{
		boolean lbValidItm = false;
		boolean lbInvldCharFlg = false;
		Vector lvInvPatrns = new Vector();
		InventoryPatternsData laInvPatrnData =
			new InventoryPatternsData();
		InventoryAllocationData laInvAlloctnData = aaInvAllocData;
		ComputeInventoryData laComputeInvData =
			new ComputeInventoryData();
		Vector lvInvNoPatrn = new Vector(DEFAULT_ITEM_VECTOR_SIZE);
		Vector lvMinInvNoPatrn = new Vector(DEFAULT_ITEM_VECTOR_SIZE);
		Vector lvMaxInvNoPatrn = new Vector(DEFAULT_ITEM_VECTOR_SIZE);

		try
		{
			// Convert the entered item numbers to uppercase.
			laInvAlloctnData.setInvItmNo(
				laInvAlloctnData.getInvItmNo().toUpperCase());
			if (laInvAlloctnData.getInvItmEndNo() != null)
			{
				laInvAlloctnData.setInvItmEndNo(
					laInvAlloctnData.getInvItmEndNo().toUpperCase());
			}

			if (chkIfItmPLPOrOLDPLTOrROP(laInvAlloctnData.getItmCd()))
			{
				laInvAlloctnData.setPatrnSeqCd(0);

				// defect 9116
				PlateTypeData laPltType =
					PlateTypeCache.getPlateType(
						laInvAlloctnData.getItmCd());

				// For any plate where the Owner does not actually 
				// provide a plate, make sure it does not match a 
				// pattern.
				// ROP also does not get checked..
				if (!laPltType
					.getNeedsProgramCd()
					.equals(SpecialPlatesConstant.OWNER)
					&& !laInvAlloctnData.getItmCd().equals(
						RADIO_OPERATOR_PLATE))
				{
					// defect 9516
					Vector lvMatchingPatterns =
						getMatchingPatterns(laInvAlloctnData);

					if (lvMatchingPatterns.size() > 0)
					{
						// At least write an message to the log.
						if (!Comm.isServer())
						{
							ItemCodesData laIPDError = null;

							laIPDError =
								(
									ItemCodesData) lvMatchingPatterns
										.elementAt(
									0);

							String lsMessage =
								"Personalized plate matches a pattern "
									+ ErrorsConstant
										.ERR_NUM_VI_PER_MATCHES_PTRN
									+ " "
									+ laIPDError.getItmCd()
									+ " "
									+ laInvAlloctnData.getInvItmNo();
							Log.write(
								Log.APPLICATION,
								laInvAlloctnData,
								lsMessage);
						}
						// end defect 9516
						laInvAlloctnData.setErrorCode(
							ErrorsConstant.ERR_NUM_VI_PER_MATCHES_PTRN);
						// defect 9252
						throw new RTSException(
							ErrorsConstant.ERR_NUM_VI_PER_MATCHES_PTRN);
						//		.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
						// end defect 9252
					}
				}
				// end defect 9116
			}
			else
			{
				// Calculate the inventory item number pattern
				lvInvNoPatrn.addAll(
					getInvNoPatrn(laInvAlloctnData.getInvItmNo()));

				// Retrieve all the inventory patterns data for the 
				// given item code and year
				if (InventoryPatternsCache
					.getInvPatrns(
						laInvAlloctnData.getItmCd(),
						laInvAlloctnData.getInvItmYr())
					!= null)
				{
					lvInvPatrns.addAll(
						InventoryPatternsCache.getInvPatrns(
							laInvAlloctnData.getItmCd(),
							laInvAlloctnData.getInvItmYr()));

					// Validate that the year is valid for that item number
					if (lvInvPatrns.size() < CommonConstant.ELEMENT_1)
					{
						RTSException leRTSException =
							new RTSException(
								ErrorsConstant
									.ERR_NUM_ITM_YEAR_NOT_VALID);
						throw leRTSException;
					}
				}
				else
				{
					RTSException leRTSException =
						new RTSException(
							ErrorsConstant
								.ERR_NUM_ITM_CD_YR_NOT_IN_PATTERNS);
					throw leRTSException;
				}

				// Loop to find the which patrnseqcd the entered 
				// inventory number matches.
				// If no matches, throw error 11.
				for (int i = 0; i < lvInvPatrns.size(); i++)
				{
					// Calculate the inventory item number pattern for 
					// the begin and end numbers in the INV_PATTERNS 
					// table 
					lvMinInvNoPatrn.removeAllElements();
					lvMaxInvNoPatrn.removeAllElements();
					lvMinInvNoPatrn.addAll(
						getInvNoPatrn(
							((InventoryPatternsData) lvInvPatrns
								.get(i))
								.getInvItmNo()));
					lvMaxInvNoPatrn.addAll(
						getInvNoPatrn(
							((InventoryPatternsData) lvInvPatrns
								.get(i))
								.getInvItmEndNo()));

					if (((String) lvInvNoPatrn
						.get(CommonConstant.ELEMENT_0))
						.equals(
							(String) lvMinInvNoPatrn.get(
								CommonConstant.ELEMENT_0)))
					{
						if (!validateNumc(lvInvNoPatrn,
							lvMinInvNoPatrn,
							lvMaxInvNoPatrn,
							NUM_POS_1))
						{
							continue;
						}
						if (!validateNumc(lvInvNoPatrn,
							lvMinInvNoPatrn,
							lvMaxInvNoPatrn,
							NUM_POS_2))
						{
							continue;
						}
						if (!validateNumc(lvInvNoPatrn,
							lvMinInvNoPatrn,
							lvMaxInvNoPatrn,
							NUM_POS_3))
						{
							continue;
						}
						if (!validateAlpha(lvInvNoPatrn,
							lvMinInvNoPatrn,
							lvMaxInvNoPatrn,
							ALPHA_POS_1,
							(InventoryPatternsData) lvInvPatrns.get(
								i)))
						{
							continue;
						}
						if (!validateAlpha(lvInvNoPatrn,
							lvMinInvNoPatrn,
							lvMaxInvNoPatrn,
							ALPHA_POS_2,
							(InventoryPatternsData) lvInvPatrns.get(
								i)))
						{
							continue;
						}
						if (!validateAlpha(lvInvNoPatrn,
							lvMinInvNoPatrn,
							lvMaxInvNoPatrn,
							ALPHA_POS_3,
							(InventoryPatternsData) lvInvPatrns.get(
								i)))
						{
							continue;
						}
						lbValidItm = true;
						laInvPatrnData =
							(InventoryPatternsData) lvInvPatrns.get(i);
						laInvAlloctnData.setPatrnSeqCd(
							laInvPatrnData.getPatrnSeqCd());
						break;
					}
				}
				if (!lbValidItm)
				{
					RTSException leRTSException =
						new RTSException(
							ErrorsConstant.ERR_NUM_COULD_NOT_COMPUTE);
					throw leRTSException;
				}

				// Determine if there are any invalid letter 
				// combinations in the item number.  
				// If yes, then throw error 11.
				Vector lvInvldLtr = new Vector();
				lvInvldLtr.addAll(
					InvalidLetterCache.getInvldLtrs(
						CommonConstant.STR_SPACE_EMPTY));
				if (InvalidLetterCache
					.getInvldLtrs(laInvAlloctnData.getItmCd())
					!= null)
				{
					lvInvldLtr.addAll(
						InvalidLetterCache.getInvldLtrs(
							laInvAlloctnData.getItmCd()));
				}
				
				String lsAlpha123 =
					(String) lvInvNoPatrn.get(CommonConstant.ELEMENT_1)
						+ (String) lvInvNoPatrn.get(
							CommonConstant.ELEMENT_2)
						+ (String) lvInvNoPatrn.get(
							CommonConstant.ELEMENT_3);
				
				for (int i = 0; i < lvInvldLtr.size(); i++)
				{
					InvalidLetterData lInvldLtrData =
						(InvalidLetterData) lvInvldLtr.get(i);
					if (!lInvldLtrData
						.getItmCd()
						.equals(laInvAlloctnData.getItmCd())
						&& lsAlpha123.indexOf(
							lInvldLtrData.getInvldLtrCombo())
							>= 0)
					{
						lbInvldCharFlg = true;
						break;
					}
					else if (
						lInvldLtrData.getItmCd().equals(
							laInvAlloctnData.getItmCd())
							&& lsAlpha123.length() <= 2
							&& lsAlpha123.indexOf(
								lInvldLtrData.getInvldLtrCombo())
								>= 0)
					{
						lbInvldCharFlg = true;
						break;
					}
					else
					{
						lbInvldCharFlg = false;
						continue;
					}
				}
				if (lbInvldCharFlg == true)
				{
					RTSException leRTSEx =
						new RTSException(
							ErrorsConstant.ERR_NUM_COULD_NOT_COMPUTE);
					throw leRTSEx;
				}
				
				// Determine if the Alpha length is correct. 
				// If not, then throw error 11.
				
				if (lsAlpha123.length()
					!= ((String) lvMinInvNoPatrn
						.get(CommonConstant.ELEMENT_1))
						.length()
						+ ((String) lvMinInvNoPatrn
							.get(CommonConstant.ELEMENT_2))
							.length()
						+ ((String) lvMinInvNoPatrn
							.get(CommonConstant.ELEMENT_3))
							.length())
				{
					RTSException leRTSEx =
						new RTSException(
							ErrorsConstant.ERR_NUM_COULD_NOT_COMPUTE);
					throw leRTSEx;
				}
				for (int i = 1; i < 4; i++)
				{
					if (lvInvNoPatrn.get(i) != null
						&& ((String) lvInvNoPatrn.get(i)).length()
						!= ((String) lvMinInvNoPatrn.get(i)).length())
					{
						RTSException leRTSEx =
							new RTSException(
								ErrorsConstant
								.ERR_NUM_COULD_NOT_COMPUTE);
								throw leRTSEx;
					}
				}
				
				// Determine if there are leading zeros in item codes 
				// where the item code length varies.  
				// If yes, then throw error 11.
				for (int i = NUM_POS_1; i <= NUM_POS_3; i++)
				{
					if ((((String) lvMinInvNoPatrn.get(i)).length()
						!= ((String) lvMaxInvNoPatrn.get(i)).length())
						&& ((((String) lvInvNoPatrn.get(i))
							.substring(0, 1))
							.equals(CommonConstant.STR_ZERO)))
					{
						RTSException leRTSEx =
							new RTSException(DEFAULT_ITEM_VECTOR_SIZE);
						throw leRTSEx;
					}
				}
			}

			// Put together return object
			laComputeInvData.setInvAlloctnData(laInvAlloctnData);
			laComputeInvData.setInvPatrnsData(laInvPatrnData);
			laComputeInvData.setInvNoPatrn(lvInvNoPatrn);
			laComputeInvData.setMinInvNoPatrn(lvMinInvNoPatrn);
			laComputeInvData.setMaxInvNoPatrn(lvMaxInvNoPatrn);
			laComputeInvData.setOrigInvNoPatrn(
				(Vector) UtilityMethods.copy(lvInvNoPatrn));
			laComputeInvData.setOrigMinInvNoPatrn(
				(Vector) UtilityMethods.copy(lvMinInvNoPatrn));
			laComputeInvData.setOrigMaxInvNoPatrn(
				(Vector) UtilityMethods.copy(lvMaxInvNoPatrn));
			laComputeInvData.setValidItmNo(true);
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}

		return laComputeInvData;
	}

	/**
	 * Given the inventory number patterns,
	 * checks to see if the Numberic inventory groups contains the same 
	 * pattern.
	 *
	 * @param avInvNoPatrn Vector 
	 * @param avMinInvNoPatrn Vector 
	 * @param avMaxInvNoPatrn Vector 
	 * @param aiIndx int
	 * @return boolean
	 * @throws RTSException 
	 */
	private boolean validateNumc(
		Vector avInvNoPatrn,
		Vector avMinInvNoPatrn,
		Vector avMaxInvNoPatrn,
		int aiIndx)
	{
		boolean lbReturn = true;

		if (!((String) avInvNoPatrn.get(aiIndx))
			.equals(CommonConstant.STR_SPACE_EMPTY)
			&& !((String) avMinInvNoPatrn.get(aiIndx)).equals(
				CommonConstant.STR_SPACE_EMPTY)
			&& !((String) avMaxInvNoPatrn.get(aiIndx)).equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			int liN1 = 0;
			int liMinN1 = 0;
			int liMaxN1 = 0;
			try
			{
				liN1 =
					Integer.parseInt((String) avInvNoPatrn.get(aiIndx));
				liMinN1 =
					Integer.parseInt(
						(String) avMinInvNoPatrn.get(aiIndx));
				liMaxN1 =
					Integer.parseInt(
						(String) avMaxInvNoPatrn.get(aiIndx));
			}
			catch (NumberFormatException aeNFE)
			{
				lbReturn = false;
			}

			if ((liN1 >= liMinN1)
				&& (liN1 <= liMaxN1)
				&& (((String) avInvNoPatrn.get(aiIndx)).length()
					>= ((String) avMinInvNoPatrn.get(aiIndx)).length())
				&& (((String) avInvNoPatrn.get(aiIndx)).length()
					<= ((String) avMaxInvNoPatrn.get(aiIndx)).length()))
			{
				lbReturn = true;
			}
			else
			{
				lbReturn = false;
			}
		}
		return lbReturn;
	}
}
