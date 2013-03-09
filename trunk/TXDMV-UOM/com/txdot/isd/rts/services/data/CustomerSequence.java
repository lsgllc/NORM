package com.txdot.isd.rts.services.data;

import java.io.*;
import java.util.*;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * CustomerSequence.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * T Pederson	02/26/2009	Added error message for incorrect format of  
 * 							custseq.dat. If error occurs when trying   
 * 							to read the file, automatically try to 
 * 							recreate the file only if it does not exist.
 * 							Added close after reading file.
 * 							modify readFile()
 * 							defect 8349 Ver Defect_POS_D 
 * ---------------------------------------------------------------------
 */

/** 
 * Singleton class that manages the custseq.dat file.
 * <P>custseq.dat file contains a sequence number and date.  At the time
 * when the first transaction is performed, this class will be loaded.
 * It will check if the date is not today.  If the date is not today, it
 * will rewrite the file with the current date and reset the customer
 * sequence number to 0.  Otherwise, it will simply load the customer
 * sequence number in memory. 
 * 
 * @version	Defect_POS_D	02/26/2009 
 * @author	Nancy Ting
 * <br>Creation Date:	09/26/2001 18:10:16 
 */

public class CustomerSequence implements java.io.Serializable
{
	// int
	protected int ciCustSeqNo;

	// Object 
	protected RTSDate caDate;
	private static CustomerSequence saCustomerSequence = null;

	// Constants
	private final static String DATE_SEPARATOR = "/";
	private final static String DELIMITER = ",";
	private final static String FILE_NAME = "custseq.dat";

	private final static long serialVersionUID = 6225842145083729722L;
	/**
	 * CustomerSequence constructor.
	 * 
	 * @throws RTSException 
	 */
	protected CustomerSequence() throws RTSException
	{
		super();
		boolean lbSuccess = readFile();

		if (!lbSuccess)
		{
			//the file is not there
			//create a file of previous year's date
			caDate = new RTSDate();
			caDate.set(
				caDate.getYear() - 1,
				caDate.getMonth(),
				caDate.getDate());
			writeFile(false);
		}

	}
	/**
	 * Get the customer sequence number
	 * 
	 * @return int
	 */
	public int getCustSeqNo()
	{
		return ciCustSeqNo;
	}
	/**
	 * Get date
	 * 
	 * @return RTSDate
	 */
	public RTSDate getDate()
	{
		return caDate;
	}
	/**
	 * Get an graph of CustomerSequence
	 * 
	 * @return CustomerSequence
	 * @throws RTSException 
	 */
	public static CustomerSequence getInstance() throws RTSException
	{
		if (saCustomerSequence == null)
		{
			saCustomerSequence = new CustomerSequence();
		}
		return saCustomerSequence;
	}
	/**
	 * Increment the customer sequence number in memory and save it on 
	 * file
	 * 
	 * @throws RTSException
	 */
	public void increment() throws RTSException
	{
		++ciCustSeqNo;
		writeFile(false);
	}
	/**
	 * Read custseq.dat file and load data in memory
	 * 
	 * @return  boolean
	 * @throws  RTSException
	 */
	private boolean readFile() throws RTSException
	{
		boolean lbSuccess = true;
		try
		{
			//Read the Customer Sequence File
			BufferedReader laBufferedInputStream =
				new BufferedReader(new FileReader(FILE_NAME));
			String lsFirstLine = laBufferedInputStream.readLine();
			// defect 8349
			laBufferedInputStream.close();
			// end defect 8349
			int liIndex = lsFirstLine.indexOf(DELIMITER);
			if (liIndex != -1)
			{
				ciCustSeqNo =
					Integer.parseInt(
						lsFirstLine.substring(0, liIndex).trim());
				String lsDate =
					lsFirstLine.substring(liIndex + 1).trim();
				StringTokenizer laStringTokenizer =
					new StringTokenizer(lsDate, DATE_SEPARATOR);
				int liDateSepCount = laStringTokenizer.countTokens();
				if (liDateSepCount == 3)
				{
					String lsMonth = laStringTokenizer.nextToken();
					String lsDay = laStringTokenizer.nextToken();
					String lsYear = laStringTokenizer.nextToken();
					caDate =
						new RTSDate(
							Integer.parseInt(lsYear),
							Integer.parseInt(lsMonth),
							Integer.parseInt(lsDay));
				}
				else
				// defect 8349
				{
				//	throw new RTSException(
				//		RTSException.FAILURE_MESSAGE,
				//		"Customer Sequence file format incorrect",
				//		"Error");
					RTSException leRTSEx = new RTSException(800);
					throw leRTSEx;
				}
				// end defect 8349
			}
			else
			// defect 8349
			{
			//	throw new RTSException(
			//		RTSException.FAILURE_MESSAGE,
			//		"Customer Sequence file format incorrect",
			//		"Error");
				RTSException leRTSEx = new RTSException(800);
				throw leRTSEx;
			}
			// end defect 8349
		}
		catch (FileNotFoundException aeFileNotFoundException)
		{
			RTSException leRTSEx = new RTSException(225);
			leRTSEx.displayError((javax.swing.JDialog) null);
			//logging only
			lbSuccess = false;
		}
		// defect 8349
		catch (IOException aeIOEx)
		{
			RTSException leRTSEx = new RTSException(232);
			throw leRTSEx;
			//RTSException leRTSEx = new RTSException(232);
			//rtsException.displayError((javax.swing.JDialog) null);
			////logging only
			//lbSuccess = false;
		}
		catch (NullPointerException aeNullPointerException)
		{
			RTSException leRTSEx = new RTSException(800);
			throw leRTSEx;
		}
		catch (NumberFormatException aeNumberFormatException)
		{
			RTSException leRTSEx = new RTSException(800);
			throw leRTSEx;
		}
		// end defect 8349
		return lbSuccess;
	}
	/**
	 * Reset the customer sequence number to zero and date to today and 
	 * write to file.
	 * 
	 * @throws RTSException 
	 */
	public void reset() throws RTSException
	{
		writeFile(true);
	}
	/**
	 * set customer sequence number
	 * 
	 * @param aiCustSeqNo int
	 */
	public void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}
	/**
	 * Set date
	 *
	 * @param aaDate RTSDate
	 */
	public void setDate(RTSDate aaDate)
	{
		caDate = aaDate;
	}
	/**
	 * Write the contents of this class to the custseq.dat file
	 * 
	 * @param  abInitialize boolean
	 * @throws RTSException
	 */
	private void writeFile(boolean abInitialize) throws RTSException
	{
		if (abInitialize)
		{
			ciCustSeqNo = 0;
			caDate = new RTSDate();
		}
		try
		{

			BufferedWriter laBufferedWriter =
				new BufferedWriter(new FileWriter(FILE_NAME));
			laBufferedWriter.write(
				ciCustSeqNo + DELIMITER + caDate.toString());
			laBufferedWriter.flush();
			laBufferedWriter.close();
		}
		catch (IOException aeIOEx)
		{
			RTSException leRTSEx = new RTSException(226);
			throw leRTSEx;
		}

	}
}
