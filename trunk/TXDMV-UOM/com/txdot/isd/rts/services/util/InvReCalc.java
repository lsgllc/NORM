package com.txdot.isd.rts.services.util;

import java.io.PrintWriter;
import java.util.Vector;

import com.ibm.ejs.util.CBuff;
import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.data.TransactionCacheData;
import com.txdot.isd.rts.services.data.TransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 * 
 * InvReCalc
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		8/25/2002	Fixed defect(CQU100004668) added new class.
 * Min Wang		1/06/2003	change to detect range overlap
 * Ray Rowehl				defect CQU100005201.
 * Min Wang		12/04/2003  Changed text to say update is needed.
 *							Modified reCalcInventory()
 *							defect 6618 Version 5.1.5 Fix2  
 * Min Wang		12/15/2003  Pass parameters OfcIssuanceNo, ItmCd, and 
 * 							filename into call for InvReCalc 
 * 							(these parameters are optional).
 *							Modified main() and reCalcInventory().
 * Min Wang		01/12/2004  Pass parameter boolean to make 
 * 							System.out.println() optional.
 *                          Add New method printString().
 * 							Defect 6745  Version 5.1.5  Fix2
 * Ray Rowehl	04/26/2005	Clean up for RTS 5.2.3
 * 							defect 7890 Ver 5.2.3 
 * Min Wang		06/24/2005	Make changes to allow updates to be run 
 * 							based on parameters entered.
 * 							add csRequestId, csUserName
 * 							modify main(), reCalcInventory()
 * 							defect 8244 Ver 5.2.2 Fix 5
 * Ray Rowehl	07/22/2005	Constants work
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	09/07/2007	Add Inv_Virtual functionality.  This
 * 							requires reflowing what parameters are 
 * 							passed and in what order.
 * 							add sbSplit, sbVirtual
 * 							modify main(), reCalcInventory()
 * 							defect 9116 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * This class calculates the inventory pattern sequence number and 
 * quantity, then compares  this to the original sequence number and 
 * quantity from allocation.  If they are not equal, report the error.
 * Also, check for Inventory Overlaps.
 * 
 * <p>This routine can now be run against Virtual Inventory.
 * 
 * <p>
 * The input arguments are:
 * <ol>
 * <li>args[0] - Adds office id to the command line.
 * <li>args[1] - Adds inventory item number to the command line.
 * <li>args[2] - Adds output file name to the command line.
 * <li>args[3] - Adds boolean to determine if request is for Virtual
 * <li>args[4] - Adds boolean to the command line to make 
 * System.out.println() optional.
 * <li>args[5] - Adds the User who is running ReCalc
 * <li>args[6] - Adds the Request Identification sent to DBA (CDC Number)
 * <li>args[7] - Adds boolean indicating that we wish to split ranges 
 * into individual rows for each item
 * <eol>
 * 
 * <p>
 * This is inventory tool for RTS developer use as needed.
 * The Developer can use the tool to check for problems with the 
 * inventory item number, quantity, or pattern sequence number. 
 * This tool can also be used to check for Inventory Overlaps.
 * 
 * <p>
 * DBA can use this tool to actually fix calculation problems.
 * 
 * <p>
 * Argument Descriptions
 * <ul>
 * <li>Run state wide or selected county.
 * <li>Can run for a selected item or all items.
 * <li>Can specify out put file.
 * <li>This run will be on the Virtual Inventory table instead of 
 * Inventory Allocation.
 * <li>Can turn on console for debugging purpose.
 * <li>User Id of person running ReCalc.  Verified against system.
 * <li>Request Id.
 * <li>This run will result in the splitting of ranges into rows for 
 * each individual item. 
 * <eol>
 * 
 * <p>
 * @version	Special Plates	09/07/2007
 * @author	Min Wang
 * <br>Creation Date:		08/25/2002 09:17:35
 */

public class InvReCalc
{
	private static final String DEFAULT_FILE_NAME = "invrecal.txt";
	private static final String ERR_MSG_GOT_AN_ERR = "Got an error ";
	private static final String ERR_MSG_GOT_RTSEX = "Got a RTS error ";
	private static final String ERR_MSG_NOT_VALID_OFFICE =
		"It is supposed to be a valid RTS Office Id!!! :-(";
	private static final String MSG_END_RUN =
		"End of ReCalculation Run.";
	private static final String MSG_NEEDS_UPDATE =
		"Pattern Seq Number, Qty, or Pattern Seq Cd needs to be "
			+ "Updated!!";
	private static final String MSG_NO_RECS_FOUND = "No Records found";
	private static final String MSG_OVERLAP_ROW_COUNT =
		"Overlap Row Count ";
	private static final String MSG_OVERLAP = "This row has overlap!!";
	private static final String MSG_START_RUN =
		"Start of Recalculation Run -  Office ";
	private static final String MSG_TOTAL_ERR_COUNT =
		"Total Error Count: ";
	private static final String MSG_TOTAL_OVERLAP_COUNT =
		"Total Overlap Count: ";
	private static final String MSG_UPDATE_DONE =
		"Pattern Seq Number, Qty, or Pattern Seq Cd has been Updated!!";
	private static final String STR_DATE_SPACE = "Date ";
	private static final String STR_ROW_COUNT = "RowCount: ";
	private static final String STR_TIME_SPACES = " Time ";
	private static final String STR_TRUE = "true";
	private static final String STR_ZERO = "0";
	private static final String TXT_ALL_ITEMS = " - All Items ";
	private static final String TXT_ITEM = " - Item ";
	private static final String TXT_ORIGINAL_ROW = "Original Row: ";
	private static final String TXT_UPDATED_ROW = "Updated Row:  ";
	private static final String TXT_USERNAME_PROPERTY = "user.name";

	private static final int DEFAULT_FIRST_OFFICE = 1;
	private static final int DEFAULT_LAST_OFFICE = 276;

	private static boolean sbSysPrn = false;

	private static String ssItmCd;
	private static String ssOfcIssuanceNo;
	private static String ssOutFileName;
	private static String ssRequestId = "";
	private static String ssUserName = "";

	private static PrintWriter saOutFile;

	// defect 9116
	private static boolean sbSplit = false;
	private static boolean sbVirtual = false;
	// end defect 9116

	/**
	 * Start up Recalc Program.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{

		try
		{
			Vector lvOfcIds = new Vector();

			// Verify and set Ofcissuanceno 
			if (aarrArgs.length > CommonConstant.ELEMENT_0)
			{
				ssOfcIssuanceNo = aarrArgs[CommonConstant.ELEMENT_0];
				if (ssOfcIssuanceNo.length()
					== CommonConstant.ELEMENT_0)
				{
					ssOfcIssuanceNo = STR_ZERO;
				}
				try
				{
					// this is just a check to see if it will fail
					Integer.parseInt(ssOfcIssuanceNo);
				}
				catch (NumberFormatException nbe)
				{
					System.out.println(ERR_MSG_NOT_VALID_OFFICE);
					ssOfcIssuanceNo = STR_ZERO;
				}
			}
			else
			{
				ssOfcIssuanceNo = STR_ZERO;
			}

			// set the Item Code
			if (aarrArgs.length > CommonConstant.ELEMENT_1)
			{
				ssItmCd = aarrArgs[CommonConstant.ELEMENT_1].trim();
				if (ssItmCd.length() == CommonConstant.ELEMENT_0)
				{
					ssItmCd = CommonConstant.STR_SPACE_EMPTY;
				}
			}
			else
			{
				ssItmCd = CommonConstant.STR_SPACE_EMPTY;
			}

			// Set the output file
			if (aarrArgs.length > CommonConstant.ELEMENT_2)
			{
				ssOutFileName =
					aarrArgs[CommonConstant.ELEMENT_2].trim();
				if (ssOutFileName.length() == CommonConstant.ELEMENT_0)
				{
					ssOutFileName = DEFAULT_FILE_NAME;
				}
			}
			else
			{
				ssOutFileName = DEFAULT_FILE_NAME;
			}

			// Set the boolean for running Virtual Inventory
			if (aarrArgs.length > CommonConstant.ELEMENT_3)
			{
				sbVirtual =
					aarrArgs[CommonConstant
						.ELEMENT_3]
						.equalsIgnoreCase(
						STR_TRUE);
			}
			else
			{
				sbVirtual = false;
			}

			// Set the boolean that controls writing to the console
			if (aarrArgs.length > CommonConstant.ELEMENT_4)
			{
				sbSysPrn =
					aarrArgs[CommonConstant
						.ELEMENT_4]
						.equalsIgnoreCase(
						STR_TRUE);
			}
			else
			{
				sbSysPrn = false;
			}

			// set user name
			if (aarrArgs.length > CommonConstant.ELEMENT_5)
			{
				ssUserName = aarrArgs[CommonConstant.ELEMENT_5].trim();
				if (!ssUserName
					.equalsIgnoreCase(
						System
							.getProperty(TXT_USERNAME_PROPERTY)
							.trim()))
				{
					ssUserName = CommonConstant.STR_SPACE_EMPTY;
				}
			}

			// set the request id
			if (aarrArgs.length > CommonConstant.ELEMENT_6)
			{
				ssRequestId = aarrArgs[CommonConstant.ELEMENT_6].trim();
			}

			// set the split boolean
			if (aarrArgs.length > CommonConstant.ELEMENT_7)
			{
				sbSplit =
					aarrArgs[CommonConstant
						.ELEMENT_7]
						.equalsIgnoreCase(
						STR_TRUE);
			}

			// creates the vector of office ids
			if (!ssOfcIssuanceNo.equals(STR_ZERO))
			{
				lvOfcIds.add(ssOfcIssuanceNo);
			}
			else
			{
				if (!sbVirtual)
				for (int liCountyNo = DEFAULT_FIRST_OFFICE;
					liCountyNo <= DEFAULT_LAST_OFFICE;
					liCountyNo++)
				{
					lvOfcIds.add(Integer.toString(liCountyNo));
				}
				else
				{
					lvOfcIds.add("0");
				}
			}

			// create the output file
			saOutFile =
				new java.io.PrintWriter(
					new java.io.FileOutputStream(ssOutFileName, false));

			// loop through the vector of offices	
			for (int i = 0; i < lvOfcIds.size(); i++)
			{
				ssOfcIssuanceNo = (String) lvOfcIds.elementAt(i);

				// Print out a Start of run header.
				RTSDate laDate = new RTSDate();

				// Add RequestId and UserName to Report Title
				if (ssItmCd.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					printString(
						MSG_START_RUN
							+ ssOfcIssuanceNo
							+ TXT_ALL_ITEMS
							+ ssRequestId
							+ CommonConstant.STR_SPACE_ONE
							+ ssUserName);
				}
				else if (
					!ssItmCd.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					printString(
						MSG_START_RUN
							+ ssOfcIssuanceNo
							+ TXT_ITEM
							+ ssItmCd
							+ CommonConstant.STR_SPACE_ONE
							+ ssRequestId
							+ CommonConstant.STR_SPACE_ONE
							+ ssUserName);
				}

				printString(
					STR_DATE_SPACE
						+ laDate.toString()
						+ STR_TIME_SPACES
						+ laDate.getClockTime());
				printString(CommonConstant.STR_SPACE_EMPTY);
				
				reCalcInventory();
			}

			RTSDate laDate2 = new RTSDate();
			printString(MSG_END_RUN);
			printString(
				STR_DATE_SPACE
					+ laDate2.toString()
					+ STR_TIME_SPACES
					+ laDate2.getClockTime());
			printString(CommonConstant.STR_SPACE_EMPTY);

			// close the file
			saOutFile.close();
			sbSysPrn = false;
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	 * Handle printing for recalc routine.
	 *  
	 * @param asString String
	 */
	private static void printString(String asString)
	{
		saOutFile.println(asString);
		saOutFile.flush();
		if (sbSysPrn)
		{
			System.out.println(asString);
		}
	}

	/**
	 * Loop through all the patterns and do a calcUnknown for range and
	 * then do a calcUnknown for end number using the previously 
	 * calculated number.
	 *  
	 * @return Object 
	 */
	private static void reCalcInventory()
	{
		try
		{
			// this is client side code
			Comm.setIsServer(false);
			
			// Signal that the database connection is up
			RTSApplicationController.setDBUp(true);
			
			// send the request to get the related inventory
			Vector lvVector = new Vector();
			lvVector.add(ssOfcIssuanceNo);
			lvVector.add(ssItmCd);
			lvVector.add(new Boolean(sbVirtual));
			
			lvVector =
				(Vector) Comm.sendToServer(
					GeneralConstant.INVENTORY,
					InventoryConstant.RECALC_GET_INV,
					lvVector);

			int liErrCount = 0;
			int liRowCount = 0;
			int liRowsWithDups = 0;

			for (int i = 0; i < lvVector.size(); i++)
			{
				liRowCount = liRowCount + 1;
				InventoryAllocationData laIADOrig =
					(InventoryAllocationData) lvVector.get(i);
				InventoryAllocationData laIADNew =
					new InventoryAllocationData();
				int liOrigQty = laIADOrig.getInvQty();

				try
				{
					// create vector to send request for recalc check
					Vector lvSend = new Vector();
					
					// for virtual inventory, we need to make sure the
					// viitmcd is copied over to the itmcd.
					if (laIADOrig.getItmCd() == null)
					{
						if (laIADOrig.getViItmCd() != null)
						{
							laIADOrig.setItmCd(laIADOrig.getViItmCd());
						}
					}
					
					lvSend.addElement(laIADOrig);

					if (ssUserName.length() > 0)
					{
						lvSend.add(ssUserName);
					}
					if (ssRequestId.length() > 0)
					{
						lvSend.add(ssRequestId);
					}

					// do recalc check 
					Vector lvReturn =
						(Vector) Comm.sendToServer(
							GeneralConstant.INVENTORY,
							InventoryConstant.RECALC_CHECK_INV,
							lvSend);

					// get calculated object out of vector
					laIADNew =
						(InventoryAllocationUIData) lvReturn.elementAt(
							CommonConstant.ELEMENT_0);

					if (sbSysPrn)
					{
						if (liOrigQty == laIADNew.getInvQty()
							&& laIADOrig.getPatrnSeqNo()
								== laIADNew.getPatrnSeqNo()
							&& laIADOrig.getPatrnSeqCd()
								== laIADNew.getPatrnSeqCd()
							&& lvReturn.size() == DEFAULT_FIRST_OFFICE)
						{
							// Reflow report line to have multiple lines
							System.out.println(
								STR_ROW_COUNT
									+ liRowCount
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADOrig.getOfcIssuanceNo()
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADOrig.getSubstaId()
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADOrig.getItmCd()
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADOrig.getInvItmNo()
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADOrig.getInvItmEndNo()
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADOrig.getPatrnSeqNo()
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADOrig.getPatrnSeqCd()
									+ CommonConstant.STR_SPACE_ONE
									+ liOrigQty);
							System.out.println(
								STR_ROW_COUNT
									+ liRowCount
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADNew.getOfcIssuanceNo()
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADOrig.getSubstaId()
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADNew.getItmCd()
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADNew.getInvItmNo()
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADNew.getInvItmEndNo()
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADNew.getPatrnSeqNo()
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ laIADNew.getPatrnSeqCd()
									+ CommonConstant.STR_SPACE_ONE
									+ laIADNew.getInvQty());
							// end defect 8244	
						}
					}

					// if origQty is not equal newQty or origPatrnSeqNo 
					// is not equal newPatrnSeqNo, then print.
					if (liOrigQty != laIADNew.getInvQty()
						|| laIADOrig.getPatrnSeqNo()
							!= laIADNew.getPatrnSeqNo()
						|| laIADOrig.getPatrnSeqCd()
							!= laIADNew.getPatrnSeqCd()
						|| lvReturn.size() > CommonConstant.ELEMENT_0)
					{
						printString(CommonConstant.STR_SPACE_ONE);
						if (sbSysPrn)
						{
							System.out.println(
								STR_ROW_COUNT + liRowCount);
						}
						// send orignal object to file
						printString(
							TXT_ORIGINAL_ROW
								+ laIADOrig.getOfcIssuanceNo()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADOrig.getSubstaId()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADOrig.getItmCd()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADOrig.getInvItmNo()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADOrig.getInvItmEndNo()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADOrig.getPatrnSeqNo()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADOrig.getPatrnSeqCd()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ liOrigQty);
						printString(
							TXT_UPDATED_ROW
								+ laIADNew.getOfcIssuanceNo()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADOrig.getSubstaId()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADNew.getItmCd()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADNew.getInvItmNo()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADNew.getInvItmEndNo()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADNew.getPatrnSeqNo()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADNew.getPatrnSeqCd()
								+ CommonConstant.STR_SPACE_ONE
								+ CommonConstant.STR_SPACE_ONE
								+ laIADNew.getInvQty());
					}

					// Report if the row was updated
					if (laIADOrig.getPatrnSeqNo()
						!= laIADNew.getPatrnSeqNo()
						|| liOrigQty != laIADNew.getInvQty()
						|| laIADOrig.getPatrnSeqCd()
							!= laIADNew.getPatrnSeqCd())
					{

						if (ssRequestId.trim().length() < 0)
						{
							printString(MSG_NEEDS_UPDATE);
						}
						else
						{
							printString(MSG_UPDATE_DONE);
						}

						liErrCount = liErrCount + 1;
					}

					if (lvReturn.size() > CommonConstant.ELEMENT_1)
					{
						// now check for any overlap ranges
						Vector lvOverlap =
							(Vector) lvReturn.elementAt(
								CommonConstant.ELEMENT_1);
						if (lvOverlap.size()
							> CommonConstant.ELEMENT_1)
						{
							printString(CommonConstant.STR_SPACE_EMPTY);
							printString(MSG_OVERLAP);

							liRowsWithDups = liRowsWithDups + 1;

							// show each range in the vector 
							for (int j = 0; j < lvOverlap.size(); j++)
							{
								InventoryAllocationData lOverlapRow =
									(
										InventoryAllocationData) lvOverlap
											.elementAt(
										j);

								if (!(lOverlapRow.getOfcIssuanceNo()
									== laIADOrig.getOfcIssuanceNo()
									&& lOverlapRow.getSubstaId()
										== laIADOrig.getSubstaId()
									&& lOverlapRow.getItmCd().equals(
										laIADOrig.getItmCd())
									&& lOverlapRow.getInvItmNo().equals(
										laIADOrig.getInvItmNo())))
								{
									printString(
										MSG_OVERLAP_ROW_COUNT
											+ j
											+ CommonConstant
												.STR_SPACE_ONE
											+ CommonConstant
												.STR_SPACE_ONE
											+ lOverlapRow
												.getOfcIssuanceNo()
											+ CommonConstant
												.STR_SPACE_ONE
											+ CommonConstant
												.STR_SPACE_ONE
											+ lOverlapRow.getSubstaId()
											+ CommonConstant
												.STR_SPACE_ONE
											+ CommonConstant
												.STR_SPACE_ONE
											+ lOverlapRow.getItmCd()
											+ CommonConstant
												.STR_SPACE_ONE
											+ CommonConstant
												.STR_SPACE_ONE
											+ lOverlapRow.getInvItmNo()
											+ CommonConstant
												.STR_SPACE_ONE
											+ CommonConstant
												.STR_SPACE_ONE
											+ lOverlapRow
												.getInvItmEndNo()
											+ CommonConstant
												.STR_SPACE_ONE
											+ CommonConstant
												.STR_SPACE_ONE
											+ lOverlapRow.getPatrnSeqNo()
											+ CommonConstant
												.STR_SPACE_ONE
											+ CommonConstant
												.STR_SPACE_ONE
											+ lOverlapRow.getInvQty());
								}
							}
						}
						printString(STR_ROW_COUNT + liRowCount);
					}
				}
				catch (RTSException aeRTSEx)
				{
					liErrCount = liErrCount + 1;
					printString(CommonConstant.STR_SPACE_EMPTY);

					if (aeRTSEx.getCode() != 0)
					{
						printString(
							ERR_MSG_GOT_RTSEX + aeRTSEx.getCode());
					}
					else
					{
						printString(
							ERR_MSG_GOT_RTSEX + aeRTSEx.getDetailMsg());
					}
					printString(
						STR_ROW_COUNT
							+ liRowCount
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_SPACE_ONE
							+ laIADOrig.getOfcIssuanceNo()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_SPACE_ONE
							+ laIADOrig.getSubstaId()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_SPACE_ONE
							+ laIADOrig.getItmCd()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_SPACE_ONE
							+ laIADOrig.getInvItmNo()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_SPACE_ONE
							+ laIADOrig.getInvItmEndNo()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_SPACE_ONE
							+ laIADOrig.getPatrnSeqNo()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_SPACE_ONE
							+ laIADOrig.getInvQty()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_SPACE_ONE
							+ laIADNew.getInvQty());
				}
			}
			if (lvVector.size() < 1)
			{
				printString(MSG_NO_RECS_FOUND);
			}
			printString(CommonConstant.STR_SPACE_EMPTY);
			printString(MSG_TOTAL_ERR_COUNT + liErrCount);
			printString(MSG_TOTAL_OVERLAP_COUNT + liRowsWithDups);
			printString(CommonConstant.STR_SPACE_EMPTY);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
			printString(
				ERR_MSG_GOT_RTSEX
					+ aeRTSEx.getCode()
					+ CommonConstant.STR_SPACE_ONE
					+ aeRTSEx.getDetailMsg());
		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
			printString(ERR_MSG_GOT_AN_ERR + leEx.getMessage());
		}
	}
}
