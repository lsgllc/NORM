package com.txdot.isd.rts.services.util;

import java.util.Vector;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.ErrorMessagesCache;
import com.txdot.isd.rts.services.cache.InventoryPatternsCache;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * InventoryPatternTest.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Clean up
 *							defect 7890 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3  
 * Ray Rowehl	07/18/2005	Constants work
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	07/23/2005	Use services instead of using server to do 
 * 							calculations.
 * 							defect 7890 Ver 5.2.3
 * K Harrell	10/20/2005	RTSApplicationController refactoring
 * 							renamed DBStatus to DBUp
 * 							defect 8337 Ver 5.2.3 
 * Ray Rowehl	01/12/2009	Change Inv Max Qty so that we can test 
 * 							the bigger ranges.
 * 							modify main()
 * 							defect 9714 Ver Defect_POS_D
 * Ray Rowehl	01/14/2009	Reflow the printing logic to have consistent 
 * 							results and reduce maintenance.
 * 							add TXT_STARS_EYE_CATCHER
 * 							add printRecord()
 * 							modify calcInventory()
 * 							defect 9714 Ver Defect_POS_D	              
 * ---------------------------------------------------------------------
 */

/**
 * Check Inventory Calculations by running all Inventory Patterns.
 * Results stored to a file that can be reviewed later.
 * <p> Note that some patterns do not compute because the range is too 
 * large.
 * 
 * @version	Defect_POS_D	01/14/2009
 * @author	Sai Marachapu
 * <br>Creation Date:		06/12/2002 09:17:35
 */

public class InventoryPatternTest
{
	private static String csOutFileName;
	
	private static final String DEFAULT_FILE_NAME = "d:\\PatTest.txt";
	private static final String DEFAULT_INVID = "0";

	private static final int DEFAULT_OFFICE = 270;
	private static final int DEFAULT_SUBSTA = 0;
	private static final String MSG_ERR_REVERSE_CALC_FAILURE =
		"Error: Reverse End No Calculation doesn't match";
	private static final String MSG_EXPECTED_PATTERN_ERRORS =
		"26 patterns are expected to get range errors..";
	private static final String MSG_TOTAL_ERROR_COUNT =
		"Total Error Count: ";
	private static final String STR_COLON = ":";
	private static final String STR_PERIOD = ".";
	private static final String TXT_DATE_SPACE = "Date ";
	private static final String TXT_ERROR_CODE = "Error code";

	private static final String TXT_STARS_EYE_CATCHER = "*****";
	private static final String TXT_START_OF_RUN =
		"Start of Comparision Run.";
	private static final String TXT_TIME_SPACES = " Time ";
	private static final String TXT_UNSPECIFIED_ERROR =
		"Unspecified Error";
	
	/**
	 * Main method to lauch class
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			// defect 9714
			// override the max value for this testing.
			InventoryConstant.setMaxQty(Integer.MAX_VALUE);
			// end defect 9714

			// initialize the patterns cache
			Vector lvDataSet =
				(Vector) Comm.sendToServer(
					GeneralConstant.GENERAL,
					CacheConstant.INVENTORY_PATTERNS_CACHE,
					new GeneralSearchData());

			//populate cache
			InventoryPatternsCache laInventoryPatternsCache =
				new InventoryPatternsCache();
			laInventoryPatternsCache.setData(lvDataSet);

			// initialize the error messages cache
			Vector lvDataSet2 =
				(Vector) Comm.sendToServer(
					GeneralConstant.GENERAL,
					CacheConstant.ERROR_MESSAGES_CACHE,
					new GeneralSearchData());

			//populate cache
			ErrorMessagesCache laErrorMessagesCache =
				new ErrorMessagesCache();
			laErrorMessagesCache.setData(lvDataSet2);

			if (aarrArgs.length > CommonConstant.ELEMENT_0)
			{
				csOutFileName = aarrArgs[CommonConstant.ELEMENT_0];
			}
			else
			{
				csOutFileName = new String(DEFAULT_FILE_NAME);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
		}

		InventoryPatternTest laIPT = new InventoryPatternTest();
		laIPT.calcInventory();
	}

	/**
	 * Loop through all the patterns and do a calcUnknown for range and
	 * then do a calcUnknown for end number using the previously 
	 * calculated number.
	 */
	private void calcInventory()
	{
		int liLoopCount = 0;
		int liErrCount = 0;

		Vector lvInvPatterns = InventoryPatternsCache.getAllInvPatrns();
		UtilityMethods.sort(lvInvPatterns);

		try
		{
			CacheManager.loadCache();

			java.io.PrintWriter laOutFile =
				new java.io.PrintWriter(
					new java.io.FileOutputStream(csOutFileName, true));

			// Print out a Start of run header.
			RTSDate laDate = new RTSDate();
			laOutFile.println(TXT_START_OF_RUN);
			laOutFile.println(
				TXT_DATE_SPACE
					+ laDate.toString()
					+ TXT_TIME_SPACES
					+ laDate.getClockTime());
			laOutFile.println(CommonConstant.STR_SPACE_EMPTY);

			RTSApplicationController.setDBUp(true);

			for (int i = DEFAULT_SUBSTA; i < lvInvPatterns.size(); i++)
			{
				liLoopCount = i + 1;

				InventoryPatternsData laIPD =
					(InventoryPatternsData) lvInvPatterns.get(i);

				InventoryAllocationUIData laIAUID =
					new InventoryAllocationUIData();

				// Use a region to do the calculations.  
				// They handle all items.
				laIAUID.setOfcIssuanceNo(DEFAULT_OFFICE);
				laIAUID.setSubstaId(DEFAULT_SUBSTA);
				laIAUID.setInvId(DEFAULT_INVID);

				laIAUID.setItmCd(laIPD.getItmCd());
				laIAUID.setInvItmYr(laIPD.getInvItmYr());
				laIAUID.setInvItmNo(laIPD.getInvItmNo());
				laIAUID.setInvItmEndNo(laIPD.getInvItmEndNo());
				laIAUID.setPatrnSeqCd(laIPD.getPatrnSeqCd());
				laIAUID.setCalcInv(false);

				// ******************************
				// defect 9714
				// add spacing so these line up more evenly.
				System.out.println(
					UtilityMethods.addPadding(
						String.valueOf(liLoopCount),
						4,
						CommonConstant.STR_SPACE_ONE)
						+ CommonConstant.STR_SPACE_ONE
						+ UtilityMethods.addPaddingRight(
							laIAUID.getItmCd(),
							8,
							CommonConstant.STR_SPACE_ONE)
						+ CommonConstant.STR_SPACE_ONE
						+ UtilityMethods.addPadding(
							String.valueOf(laIPD.getPatrnSeqCd()),
							2,
							CommonConstant.STR_SPACE_ONE)
						+ CommonConstant.STR_SPACE_ONE
						+ UtilityMethods.addPaddingRight(
							laIAUID.getInvItmNo(),
							InventoryConstant.MAX_ITEM_LENGTH,
							CommonConstant.STR_SPACE_ONE));

				
				// move up to make it common.
				ValidateInventoryPattern laVIP =
					new ValidateInventoryPattern();
				// end defect 9714
				
				//Calculate Quantity
				try
				{

					laIAUID =
						(
							InventoryAllocationUIData) laVIP
								.calcInvUnknown(
							laIAUID);

				}
				catch (RTSException aeRTSEx)
				{
					// defect 9714
					printRecord(
						liLoopCount,
						laOutFile,
						laIAUID.getItmCd(),
						laIAUID.getPatrnSeqCd(),
						laIAUID.getInvItmYr(),
						laIAUID.getInvItmNo(),
						laIAUID.getInvItmEndNo(),
						CommonConstant.STR_SPACE_ONE,
						aeRTSEx);
					// end defect 9714

					liErrCount++;
					continue;
				}

				// print out the results of the qty try.
				// defect 9714
				printRecord(
					liLoopCount,
					laOutFile,
					laIAUID.getItmCd(),
					laIAUID.getPatrnSeqCd(),
					laIAUID.getInvItmYr(),
					laIAUID.getInvItmNo(),
					laIAUID.getInvItmEndNo(),
					String.valueOf(laIAUID.getInvQty()),
					null);
				// end defect 9714

				// Calculate End No based on qty
				String lsInvEndNo = laIAUID.getInvItmEndNo();
				laIAUID.setCalcInv(false);
				try
				{
					laIAUID =
						(
							InventoryAllocationUIData) laVIP
								.calcInvUnknown(
							laIAUID);
				}
				catch (RTSException aeRTSEx)
				{
					// defect 9714
					printRecord(
						liLoopCount,
						laOutFile,
						laIAUID.getItmCd(),
						laIAUID.getPatrnSeqCd(),
						laIAUID.getInvItmYr(),
						laIAUID.getInvItmNo(),
						String.valueOf(laIAUID.getInvQty()),
						CommonConstant.STR_SPACE_ONE,
						aeRTSEx);
					// end defect 9714

					liErrCount++;
					continue;
				}

				// defect 9714
				printRecord(
					liLoopCount,
					laOutFile,
					laIAUID.getItmCd(),
					laIAUID.getPatrnSeqCd(),
					laIAUID.getInvItmYr(),
					laIAUID.getInvItmNo(),
					String.valueOf(laIAUID.getInvQty()),
					laIAUID.getInvItmEndNo(),
					null);
				// end defect 9714

				// make sure the end number calculated matches 
				// the end number computed
				if (!lsInvEndNo.equals(laIAUID.getInvItmEndNo()))
				{
					laOutFile.println(MSG_ERR_REVERSE_CALC_FAILURE);
					liErrCount = liErrCount + 1;
					laOutFile.println();
				}

				// Calculate End No for half quantity
				laIAUID.setInvItmNo(laIPD.getInvItmNo());
				laIAUID.setInvItmEndNo(CommonConstant.STR_SPACE_ONE);
				laIAUID.setInvQty(laIAUID.getInvQty() / 2);
				laIAUID.setCalcInv(false);
				try
				{
					laIAUID =
						(
							InventoryAllocationUIData) laVIP
								.calcInvUnknown(
							laIAUID);
				}
				catch (RTSException aeRTSEx)
				{
					// defect 9714
					printRecord(
						liLoopCount,
						laOutFile,
						laIAUID.getItmCd(),
						laIAUID.getPatrnSeqCd(),
						laIAUID.getInvItmYr(),
						laIAUID.getInvItmNo(),
						String.valueOf(laIAUID.getInvQty()),
						CommonConstant.STR_SPACE_ONE,
						aeRTSEx);
					// end defect 9714
					liErrCount = liErrCount + 1;
					continue;
				}

				// defect 9714
				printRecord(
					liLoopCount,
					laOutFile,
					laIAUID.getItmCd(),
					laIAUID.getPatrnSeqCd(),
					laIAUID.getInvItmYr(),
					laIAUID.getInvItmNo(),
					String.valueOf(laIAUID.getInvQty()),
					laIAUID.getInvItmEndNo(),
					null);
				// end defect 9714

				laOutFile.println();
			}

			laOutFile.println(MSG_TOTAL_ERROR_COUNT + liErrCount);
			laOutFile.println(CommonConstant.STR_SPACE_ONE);
			laOutFile.println(MSG_EXPECTED_PATTERN_ERRORS);
			laOutFile.flush();
			laOutFile.close();
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	 * Print the result line.
	 * 
	 * @param aiLoopCount
	 * @param aaOutFile
	 * @param asItmCd
	 * @param aiPatrnSeqCd
	 * @param aiItmYr
	 * @param asBeginNo
	 * @param asParm2
	 * @param asResult
	 * @param aeRTSEx
	 */
	private void printRecord(
		int aiLoopCount,
		java.io.PrintWriter aaOutFile,
		String asItmCd,
		int aiPatrnSeqCd,
		int aiItmYr,
		String asBeginNo,
		String asParm2,
		String asResult,
		RTSException aeRTSEx)
	{
		String lsPrintLine =
			UtilityMethods.addPadding(
				String.valueOf(aiLoopCount),
				4,
				CommonConstant.STR_SPACE_ONE)
				+ STR_PERIOD
				+ CommonConstant.STR_SPACE_ONE
				+ UtilityMethods.addPaddingRight(
					asItmCd,
					8,
					CommonConstant.STR_SPACE_ONE)
				+ CommonConstant.STR_SPACE_ONE
				+ UtilityMethods.addPadding(
					String.valueOf(aiPatrnSeqCd),
					2,
					CommonConstant.STR_SPACE_ONE)
				+ CommonConstant.STR_SPACE_ONE
				+ UtilityMethods.addPadding(
					String.valueOf(aiItmYr),
					4,
					CommonConstant.STR_SPACE_ONE)
				+ CommonConstant.STR_SPACE_ONE
				+ UtilityMethods.addPadding(
					asBeginNo,
					InventoryConstant.MAX_ITEM_LENGTH,
					CommonConstant.STR_SPACE_ONE)
				+ CommonConstant.STR_SPACE_ONE
				+ UtilityMethods.addPadding(
					asParm2,
					InventoryConstant.MAX_ITEM_LENGTH,
					CommonConstant.STR_SPACE_ONE)
				+ STR_COLON
				+ CommonConstant.STR_DASH
				+ CommonConstant.STR_SPACE_ONE
				+ UtilityMethods.addPadding(
					asResult,
					InventoryConstant.MAX_ITEM_LENGTH,
					CommonConstant.STR_SPACE_ONE);

		aaOutFile.println(lsPrintLine);

		if (aeRTSEx != null)
		{
			ErrorMessagesData laEMD =
				ErrorMessagesCache.getErrMsg(aeRTSEx.getCode());
			// Got an Error on this one
			if (laEMD != null)
			{
				lsPrintLine =
					TXT_STARS_EYE_CATCHER
						+ UtilityMethods.addPadding(
							String.valueOf(aeRTSEx.getCode()),
							3,
							CommonConstant.STR_SPACE_ONE)
						+ STR_COLON
						+ CommonConstant.STR_SPACE_ONE
						+ laEMD.getErrMsgDesc();
			}
			else
			{
				lsPrintLine =
					TXT_STARS_EYE_CATCHER
						+ TXT_ERROR_CODE
						+ STR_COLON
						+ CommonConstant.STR_SPACE_ONE
						+ UtilityMethods.addPadding(
							String.valueOf(aeRTSEx.getCode()),
							3,
							CommonConstant.STR_SPACE_ONE)
						+ STR_COLON
						+ CommonConstant.STR_SPACE_ONE
						+ TXT_UNSPECIFIED_ERROR;
			}
			aaOutFile.println(lsPrintLine);
			aaOutFile.println();
		}
		aaOutFile.flush();
	}
}
