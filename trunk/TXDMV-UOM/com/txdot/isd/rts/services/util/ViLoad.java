package com.txdot.isd.rts.services.util;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com
	.txdot
	.isd
	.rts
	.client
	.inventory
	.business
	.InventoryClientBusiness;
import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.data.ValidateInventoryPattern;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 * ViLoad.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	04/04/2007	New class
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/18/2007	Add boolean to switch processing from load
 * 							to delete.
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/09/2007	Add a boolean to indicate whether or not
 * 							this is an ISA item.
 * 							add cbISA
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/27/2007	Add options to name output file and determine
 * 							if we write to system out.
 * 							add DEFAULT_OUTFILE
 * 							modify ViLoad(), main()
 * Min Wang	09/07/2007		Enable delete function. Do split range by 
 * 							call update status cd.
 * 							modify main(), processItem()
 * 							defect 9117 Ver Special Plate
 * ---------------------------------------------------------------------
 */

/**
 * Receives a VI Item Code and input file.  From this input, VI Rows 
 * are formed and either inserted or deleted depending on the delete
 * boolean.
 * 
 * <p>Usage: ViLoad ViItmCd FileName InvoiceNumber OutFile 
 * boolean boolean boolean
 *
 * @version	Special Plates	09/07/2007 
 * @author	Ray Rowehl
 * <br>Creation Date:		04/04/2007 07:25:11
 */
public class ViLoad
{
	private static String DEFAULT_OUTFILE = "ViLoad.txt";
	private static String ssOutFileName;
	private static boolean sbSysPrn = false;
	private static PrintWriter caOutFile;

	private String csViItmCode;
	private String csFileName;
	private String csInvoiceNumber;
	private boolean cbDelete = false;
	private boolean cbISA = false;
	private String csEmployee;
	private String csWorkStation;

	InventoryClientBusiness caICB;

	/**
	 * ViLoad.java Constructor
	 * 
	 * @param asViItmCode
	 * @param asFileName
	 * @param asInvoiceNumber
	 * @param asFileOut
	 * @param abISA
	 * @param abSystemOut
	 * @param abDelete
	 */
	public ViLoad(
		String asViItmCode,
		String asFileName,
		String asInvoiceNumber,
		String asFileOut,
		boolean abISA,
		boolean abSystemOut,
		boolean abDelete)
	{
		super();

		csViItmCode = asViItmCode;
		csFileName = asFileName;
		csInvoiceNumber = asInvoiceNumber;
		cbISA = abISA;
		sbSysPrn = abSystemOut;
		cbDelete = abDelete;
		ssOutFileName = asFileOut;

		csEmployee = System.getProperty("user.name");
		if (csEmployee.length() > 8)
		{
			csEmployee = csEmployee.substring(0, 7);
		}

		try
		{
			csWorkStation =
				java.net.InetAddress.getLocalHost().getHostName();

			// check for a prefix and remove 
			int liIndex = csWorkStation.indexOf("-");
			if (liIndex > -1)
			{
				csWorkStation = csWorkStation.substring(liIndex + 1);
			}

			// trim to size
			if (csWorkStation.length() > 8)
			{
				csWorkStation = csWorkStation.substring(0, 8);
			}
		}
		catch (UnknownHostException e)
		{
			csWorkStation = "UNKNOWN";
			e.printStackTrace();
		}

		// Set db up flag
		RTSApplicationController.setDBUp(true);

		// create interface object
		caICB = new InventoryClientBusiness();
	}

	/**
	 * Starts up the load routine.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		// create place holder
		ViLoad laLoad = null;
			//new ViLoad(
			//	"none",
			//	"none",
			//	"none",
			//	DEFAULT_OUTFILE,
			//	false,
			//	true,
			//	false);

		try
		{
			if (aarrArgs.length == 3)
			{
				// default to non-ISA and load processing
				// setup the class level fields
				laLoad =
					new ViLoad(
						(String) aarrArgs[0],
						(String) aarrArgs[1],
						(String) aarrArgs[2],
						DEFAULT_OUTFILE,
						false,
						true,
						false);
			}
			else if (aarrArgs.length == 4)
			{
				// Output File.
				// setup the class level fields
				laLoad =
					new ViLoad(
						(String) aarrArgs[0],
						(String) aarrArgs[1],
						(String) aarrArgs[2],
						(String) aarrArgs[3],
						false,
						true,
						false);
			}
			else if (aarrArgs.length == 5)
			{
				// Output File.
				// user chooses ISA.
				// setup the class level fields
				laLoad =
					new ViLoad(
						(String) aarrArgs[0],
						(String) aarrArgs[1],
						(String) aarrArgs[2],
						(String) aarrArgs[3],
						(((String) aarrArgs[4])
							.trim()
							.equalsIgnoreCase("true")),
						true,
						false);
			}
			else if (aarrArgs.length == 6)
			{
				// Output File.
				// user chooses ISA.
				// user console output.
				// setup the class level fields
				laLoad =
					new ViLoad(
						(String) aarrArgs[0],
						(String) aarrArgs[1],
						(String) aarrArgs[2],
						(String) aarrArgs[3],
						(((String) aarrArgs[4])
							.trim()
							.equalsIgnoreCase("true")),
						(((String) aarrArgs[5])
							.trim()
							.equalsIgnoreCase("true")),
						false);
			}
			else if (aarrArgs.length == 7)
			{
				//				System.out.println(
				//					"Delete is not enabled at this time");
				//				System.exit(0);
				// Output File.
				// user chooses ISA.
				// user console output.
				// User specifies if we are doing DELETE
				// setup the class level fields
				laLoad =
					new ViLoad(
						(String) aarrArgs[0],
						(String) aarrArgs[1],
						(String) aarrArgs[2],
						(String) aarrArgs[3],
						(((String) aarrArgs[4])
							.trim()
							.equalsIgnoreCase("true")),
						(((String) aarrArgs[5])
							.trim()
							.equalsIgnoreCase("true")),
						(((String) aarrArgs[6])
							.trim()
							.equalsIgnoreCase("true")));
			}
			else
			{
				// improper choice, exit.
				System.out.println(
					"This class only takes three to seven parameters.");
				System.out.println(
					"Usage: ViLoad ItemCode FileName InvoiceNumber "
						+ "OutPutFile ISA boolean Console boolean "
						+ "Delete boolean");

				System.exit(16);
			}

			// create the output file
			caOutFile =
				new java.io.PrintWriter(
					new java.io.FileOutputStream(ssOutFileName, false));

			// Print out a Start of run header.
			RTSDate laDate = new RTSDate();

			if (!laLoad.cbDelete)
			{
				printString(
					"Start of VI Load Run - "
						+ "Item "
						+ laLoad.csViItmCode
						+ CommonConstant.STR_SPACE_ONE
						+ laLoad.csInvoiceNumber
						+ CommonConstant.STR_SPACE_ONE
						+ laLoad.csEmployee
						+ " Insert Mode");
			}
			else
			{
				printString(
					"Start of VI Load Run - "
						+ "Item "
						+ laLoad.csViItmCode
						+ CommonConstant.STR_SPACE_ONE
						+ laLoad.csInvoiceNumber
						+ CommonConstant.STR_SPACE_ONE
						+ laLoad.csEmployee
						+ " DELETE Mode");
			}

			printString(
				"Date "
					+ laDate.toString()
					+ " Time "
					+ laDate.getClockTime());

			printString(CommonConstant.STR_SPACE_EMPTY);

			// load cache for calculations
			CacheManager.loadCache();

			// start the load
			laLoad.getItems();

			// Print out end of run trailer
			RTSDate laDate2 = new RTSDate();

			printString(CommonConstant.STR_SPACE_EMPTY);
			printString("End of VI Load Run");
			printString(
				"Date "
					+ laDate2.toString()
					+ " Time "
					+ laDate2.getClockTime());

			printString(CommonConstant.STR_SPACE_EMPTY);

			// just a message to say it is done.
			System.out.println("Done");

			// close the file
			caOutFile.close();
		}
		catch (Exception aeEx)
		{
			System.out.println("Crashed!!!!");
			aeEx.printStackTrace();
		}
	}

	/**
	 * Create the InventoryAllocationData object to be loaded.
	 * 
	 * @param asBegNo String
	 * @param asEndNo String
	 * @return InventoryAllocationData
	 * @throws RTSException
	 */
	private InventoryAllocationData createObject(
		String asBegNo,
		String asEndNo)
		throws RTSException
	{
		// do the initial object create
		InventoryAllocationData laIAD = new InventoryAllocationData();
		laIAD.setInvItmNo(asBegNo);
		laIAD.setInvItmEndNo(asEndNo);
		laIAD.setItmCd(csViItmCode);
		laIAD.setISA(cbISA);
		laIAD.setViItmCd(csViItmCode);
		laIAD.setOfcIssuanceNo(291);
		laIAD.setItrntReq(false);

		laIAD.setInvcNo(csInvoiceNumber);
		laIAD.setTransEmpId(csEmployee);
		laIAD.setRequestorIpAddress(csWorkStation);

		// calculate
		ValidateInventoryPattern laVIP = new ValidateInventoryPattern();
		laIAD = (InventoryAllocationData) laVIP.calcInvUnknownVI(laIAD);

		// this must be set after the calculation
		laIAD.setInvcDate((new RTSDate()).getYYYYMMDDDate());

		// return the verified object
		return laIAD;
	}

	/**
	 * Open the input file and begin processing the items.
	 */
	private void getItems()
	{
		try
		{
			FileInputStream laFileInputStream =
				new FileInputStream(csFileName);
			BufferedReader laBufferedReader =
				new BufferedReader(
					new InputStreamReader(laFileInputStream));
			String lsBufRd;
			while ((lsBufRd = laBufferedReader.readLine()) != null)
			{
				// parse out the begin number
				int liIndex = lsBufRd.indexOf(",");
				if (liIndex < 0)
				{
					// this is not a valid string in!  Skip it.
					continue;
				}

				String lsBeginNo = lsBufRd.substring(0, liIndex);
				lsBufRd = lsBufRd.substring(liIndex + 1);

				// parse out the end number
				// if we are deleting, there will not be a quantity.
				liIndex = lsBufRd.indexOf(",");
				String lsEndNo = "";
				if (liIndex > -1)
				{
					lsEndNo = lsBufRd.substring(0, liIndex);
					lsBufRd = lsBufRd.substring(liIndex + 1);
				}
				else
				{
					lsEndNo = lsBufRd.substring(0, liIndex);
					lsBufRd = "0";
				}

				// parse out the quantity
				String lsQty = lsBufRd.trim();

				processItem(lsBeginNo, lsEndNo, lsQty);
			}
			laBufferedReader.close();
			laFileInputStream.close();
		}
		catch (FileNotFoundException aeFNFE)
		{
			printString("Input File was not found! " + csFileName);
		}
		catch (IOException aeIOE)
		{
			printString("Had a problem reading the file.");
		}
	}

	/**
	 * Process the Item to insert.
	 * 
	 * @param asBeginNo
	 * @param asEndNo
	 * @param asQty
	 */
	private void processItem(
		String asBeginNo,
		String asEndNo,
		String asQty)
	{
		try
		{
			// get the object created
			InventoryAllocationData laIAD =
				createObject(asBeginNo, asEndNo);

			if (!this.cbDelete)
			{
				// check the quantities to ensure they match up.
				try
				{
					int liPassedQty = Integer.parseInt(asQty);
					if (liPassedQty != laIAD.getInvQty())
					{
						printString(
							"Quantity did not match up for "
								+ asBeginNo
								+ " Passed Qty "
								+ asQty
								+ " Computed Qty "
								+ laIAD.getInvQty());
						throw new RTSException(10);
					}
				}
				catch (NumberFormatException aeNFE)
				{
					printString(
						"Quantity did not parse for "
							+ asBeginNo
							+ " Passed Qty "
							+ asQty
							+ " Computed Qty "
							+ laIAD.getInvQty());
					throw new RTSException(10);
				}

				// TODO check for intersection on Inv Allocation
				// TODO check for intersection on Inv Virtual

				// insert the row
				laIAD =
					(InventoryAllocationData) caICB.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant.INV_VI_INSERT_ROW,
						laIAD);

				if (sbSysPrn)
				{
					System.out.println(
						"Item Loaded " + asBeginNo + " " + asEndNo);
				}
			}
			else
			{
				Vector lvDeleteData =
					(Vector) caICB.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant.INV_VI_UPDATE_INV_STATUS_CD,
						laIAD);

				for (Iterator laIter = lvDeleteData.iterator();
					laIter.hasNext();
					)
				{
					laIAD =
						(InventoryAllocationData) laIter.next();

					// delete the row
					laIAD =
						(InventoryAllocationData) caICB.processData(
							GeneralConstant.INVENTORY,
							InventoryConstant.INV_VI_DELETE_ITEM,
							laIAD);

					printString(
						"Item Deleted " + asBeginNo + " " + asEndNo);
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			printString("Could Not Load an Item, skipped!!");
			printString(
				"Begin Number "
					+ asBeginNo
					+ " End Number "
					+ asEndNo
					+ " Qty "
					+ asQty);
			if (aeRTSEx.getCode() > 0)
			{
				printString("Error Code " + aeRTSEx.getCode());
			}
			aeRTSEx.printStackTrace();
		}
	}

	/**
	 * Handle printing for recalc routine.
	 *  
	 * @param asString String
	 */
	private static void printString(String asString)
	{
		caOutFile.println(asString);
		caOutFile.flush();
		if (sbSysPrn)
		{
			System.out.println(asString);
		}
	}
}
