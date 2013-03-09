package com.txdot.isd.rts.services.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;

/*
 *
 * LoadTester.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/27/2005	Code Cleanup.
 * 							JavaDoc standards
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Allows simple load testing of server side application.
 *
 * @version	5.2.3			06/27/2005
 * @author	RS Chandel
 * <br>Creation Date:		01/09/2002 16:11:40
 */

public class LoadTester implements Runnable
{
	private static final String TXT_END_LOAD =
		"----End Load running----";
	private static final String TXT_POST = "POST";
	private static final String TXT_RUNNING_PASS = "Running pass: ";
	private static final String TXT_DATA = "Data=";
	private static final String TXT_INV_ALLOC =
		"ModuleName=12&FunctionId=2&Data=";
	private static final String TXT_INV_CALC_UNKNOWN =
		"ModuleName=12&FunctionId=3&Data=";
	private static final String TXT_MODULE_FUNCTION =
		"ModuleName=6&FunctionId=4&Data=";
	private static final String MSG_CLASS_USAGE =
		"usage: LoadTester filename, iterationcount, start cust seq " +		"num, Num Thread, TEST_INV";
	private static final String STR_TEST_INV = "TEST_INV";
	private static final String MSG_GOT_NEXT_SEQ_NUMBER = "Called";
	private static final String FILE_INVENTORY_DAT = "inventory.dat";

	private static boolean sbTestInv = false;

	private static String SERVLET_HOST =
		SystemProperty.getServletHost();
	private static String SERVLET_PORT =
		SystemProperty.getServletPort();
	private static String SERVLET_NAME =
		SystemProperty.getServletName();

	private static int siInit = 0;
	private static int siInvIndex = 0;
	static int siNumTrial = 0;
	static int siStartNum = 0;
	static int siThreadNum = 0;

	static String ssInvtest = "";

	static java.util.Vector svVct = new Vector();
	private static Vector saInvVct = new Vector();

	/**
	 * LoadTester constructor comment.
	 */
	public LoadTester()
	{
		super();
	}
	/**
	 * Get the next available Inventory Item.
	 * 
	 * @return String
	 * @throws IOException
	 */
	public synchronized static String getNextInventoryNum()
		throws IOException
	{
		if (siInit == 0)
		{
			//Load
			File laFile = new File(FILE_INVENTORY_DAT);
			FileInputStream laFIS = new FileInputStream(laFile);
			BufferedReader laBRIn =
				new BufferedReader(new InputStreamReader(laFIS));
			String lsLineIn = null;
			while ((lsLineIn = laBRIn.readLine()) != null)
			{
				saInvVct.add(lsLineIn);
			}
			laBRIn.close();
			laFIS.close();

			siInit = -1;
		}
		String lsReturnStr = (String) saInvVct.elementAt(siInvIndex);
		siInvIndex++;
		return lsReturnStr;
	}
	/**
	 * Get the next sequence number.
	 * 
	 * @return int
	 */
	public synchronized static int getNextSeq()
	{
		System.out.println(MSG_GOT_NEXT_SEQ_NUMBER);
		siStartNum = siStartNum + 1;
		return siStartNum;
	}
	/**
	 * Starts the test.
	 * 
	 * @param aarrArgs - String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			if (aarrArgs != null)
			{
				if (aarrArgs.length > 4)
				{
					File laFile = new File(aarrArgs[0]);
					FileInputStream laFIS = new FileInputStream(laFile);
					BufferedReader laBRIn =
						new BufferedReader(
							new InputStreamReader(laFIS));
					String lsLineIn = null;
					while ((lsLineIn = laBRIn.readLine()) != null)
					{
						svVct.add(lsLineIn);
					}
					laBRIn.close();
					laFIS.close();
					siNumTrial =
						Integer.valueOf(aarrArgs[1]).intValue();
					siStartNum =
						Integer.valueOf(aarrArgs[2]).intValue();
					siThreadNum =
						Integer.valueOf(aarrArgs[3]).intValue();
					ssInvtest = aarrArgs[4];
					if (ssInvtest.equals(STR_TEST_INV))
					{
						sbTestInv = true;
					}

					for (int i = 0; i < siThreadNum; i++)
					{
						LoadTester laLoadTest = new LoadTester();
						Thread laThread = new Thread(laLoadTest);
						laThread.start();
					}
				}
				else
				{
					System.out.println(MSG_CLASS_USAGE);
					System.exit(0);
				}
			}
		}

		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}
	/**
	 * Process Request
	 * 
	 * @return String
	 * @param str java.lang.String
	 */
	public String processCustSeqNum(String lsStr)
	{

		String lsModuleStr = TXT_MODULE_FUNCTION;
		int j = lsStr.indexOf(lsModuleStr);
		//Its not post trans, return the original string.
		if (j == -1)
		{
			return lsStr;
		}

		Vector lvVect = new Vector();
		//It is post trans increment cust seq num
		try
		{

			int i = lsStr.indexOf(TXT_DATA);
			String lsData = lsStr.substring(i + 5);
			Object laObj =
				com
					.txdot
					.isd
					.rts
					.services
					.communication
					.Comm
					.StringToObj(
					lsData);
			Object laToObj = null;
			if (laObj instanceof java.util.Vector)
			{
				lvVect = (Vector) laObj;
				for (int k = 0; k < lvVect.size(); k++)
				{
					Object laDbObj = lvVect.elementAt(k);
					if (laDbObj != null)
					{
						if (laDbObj instanceof Vector)
							//com.txdot.isd.rts.services.data.TransactionCacheData)
						{
							Vector lvVct1 = ((Vector) laDbObj);
							int liNextNum = getNextSeq();
							for (int l = 0; l < lvVct1.size(); l++)
							{
								laToObj = lvVct1.elementAt(l);
								if (laToObj
									instanceof TransactionCacheData)
								{
									laObj =
										(
											(TransactionCacheData) laToObj)
											.getObj();
								}

								if (laObj
									instanceof FundFunctionTransactionData)
								{
									(
										(
											FundFunctionTransactionData) laObj)
												.setCustSeqNo(
										liNextNum);
								}
								else if (
									laObj
										instanceof InventoryFunctionTransactionData)
								{
									(
										(
											InventoryFunctionTransactionData) laObj)
												.setCustSeqNo(
										liNextNum);
								}
								else if (
									laObj
										instanceof MotorVehicleFunctionTransactionData)
								{
									(
										(
											MotorVehicleFunctionTransactionData) laObj)
												.setCustSeqNo(
										liNextNum);
								}
								else if (
									laObj instanceof TransactionData)
								{
									(
										(
											TransactionData) laObj)
												.setCustSeqNo(
										liNextNum);
								}
								else if (
									laObj
										instanceof TransactionFundsDetailData)
								{
									(
										(
											TransactionFundsDetailData) laObj)
												.setCustSeqNo(
										liNextNum);
								}
								else if (
									laObj
										instanceof TransactionHeaderData)
								{
									(
										(
											TransactionHeaderData) laObj)
												.setCustSeqNo(
										liNextNum);
								}
								else if (
									laObj
										instanceof TransactionInventoryDetailData)
								{
									(
										(
											TransactionInventoryDetailData) laObj)
												.setCustSeqNo(
										liNextNum);
								}
								else if (
									laObj
										instanceof TransactionPaymentData)
								{
									(
										(
											TransactionPaymentData) laObj)
												.setCustSeqNo(
										liNextNum);
								}

							}
						}
						else
						{
							return lsStr;
						}
					}

				}
			}
			lsStr =
				lsModuleStr
					+ com
						.txdot
						.isd
						.rts
						.services
						.communication
						.Comm
						.objToString(
						lvVect);
			System.out.println(siStartNum);

		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
		}

		return (lsStr);
	}
	/**
	 * Process Inventory requests
	 * 
	 * @return String
	 * @param asStr - String
	 * @param asInvNum - String
	 */
	public String processForInventory(String asStr, String asInvNum)
	{
		//String modstr = TXT_INV_CALC_UNKNOWN;
		int j = asStr.indexOf(TXT_INV_CALC_UNKNOWN);

		//modstr = TXT_INV_ALLOC;
		int k = asStr.indexOf(TXT_INV_ALLOC);

		//Its not inv alloc, return the original string.
		if (j == -1 && k == -1)
		{
			return asStr;
		}

		try
		{
			int i = asStr.indexOf(TXT_DATA);
			String laData = asStr.substring(i + 5);
			Object laObj =
				com
					.txdot
					.isd
					.rts
					.services
					.communication
					.Comm
					.StringToObj(
					laData);
			//Object to = null;
			if (laObj instanceof InventoryAllocationUIData)
			{
				((InventoryAllocationUIData) laObj).setInvItmEndNo(
					asInvNum);
				((InventoryAllocationUIData) laObj).setInvItmNo(
					asInvNum);

				asStr =
					TXT_INV_CALC_UNKNOWN
						+ com
							.txdot
							.isd
							.rts
							.services
							.communication
							.Comm
							.objToString(
							laObj);

			}
			if (laObj instanceof Vector)
			{
				Vector lvVct = (Vector) laObj;
				for (int y = 0; y < lvVct.size(); y++)
				{
					Object laOY = lvVct.elementAt(y);
					if (laOY instanceof InventoryAllocationUIData)
					{
						(
							(
								InventoryAllocationUIData) laOY)
									.setInvItmEndNo(
							asInvNum);
						((InventoryAllocationUIData) laOY).setInvItmNo(
							asInvNum);

					}
					if (laOY instanceof TransactionHeaderData)
					{
						int z = getNextSeq();
						((TransactionHeaderData) laOY).setCustSeqNo(z);
					}
				}
				asStr =
					TXT_INV_ALLOC
						+ com
							.txdot
							.isd
							.rts
							.services
							.communication
							.Comm
							.objToString(
							laObj);
			}

		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
		return asStr;
	}
	/**
	 * When an object implementing interface <code>Runnable</code> is 
	 * used to create a thread, starting the thread causes the object's 
	 * <code>run</code> method to be called in that separately executing 
	 * thread. 
	 * 
	 * <p>The general contract of the method <code>run</code> is that 
	 * it may take any action whatsoever.
	 *
	 * @see     java.lang.Thread#run()
	 */
	public void run()
	{
		try
		{
			URL lURL =
				new URL(
					"http://"
						+ SERVLET_HOST
						+ ":"
						+ SERVLET_PORT
						+ "/"
						+ SERVLET_NAME);
			//boolean getinv = true;
			String lsInvNum = "";
			for (int liTrial = 0; liTrial < siNumTrial; liTrial++)
			{
				System.out.println(TXT_RUNNING_PASS + liTrial);
				if (sbTestInv)
				{
					lsInvNum = getNextInventoryNum();
				}

				for (int i = 0; i < svVct.size(); i++)
				{
					StringBuffer lsStringBufferInp = new StringBuffer();
					HttpURLConnection laURLConnection =
						(HttpURLConnection) lURL.openConnection();
					laURLConnection.setRequestMethod(TXT_POST);
					laURLConnection.setAllowUserInteraction(false);
					laURLConnection.setDoOutput(true);

					PrintWriter laPWOut =
						new PrintWriter(
							laURLConnection.getOutputStream());
					String lsStr = (String) svVct.elementAt(i);
					lsStr = processCustSeqNum(lsStr);
					//this code will be exceuted for inventory allocation event.
					if (sbTestInv)
					{
						lsStr = processForInventory(lsStr, lsInvNum);
					}
					laPWOut.println(lsStr);
					laPWOut.flush();
					laPWOut.close();

					BufferedReader laBufferedReader =
						new BufferedReader(
							new InputStreamReader(
								laURLConnection.getInputStream()));
					String lsRes;
					while (true)
					{
						lsRes = laBufferedReader.readLine();
						if (lsRes == null)
						{
							break;
						}
						if (lsRes.length() == 0)
						{
							continue;
						}
						lsStringBufferInp.append(lsRes);
					}

					Object laObj =
						com
							.txdot
							.isd
							.rts
							.services
							.communication
							.Comm
							.StringToObj(
							lsStringBufferInp.toString());

					if (laObj instanceof Exception)
					{
						((Exception) laObj).printStackTrace();
					}
				}
			}
			System.out.println(TXT_END_LOAD);

		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}

	}
}
