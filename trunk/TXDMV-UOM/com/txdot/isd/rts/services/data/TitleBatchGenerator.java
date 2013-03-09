package com.txdot.isd.rts.services.data;

import java.io.*;
import java.util.StringTokenizer;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * TitleBatchGenerator.java  
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		07/31/2002	Updated code to verify the ttlBatch date 
 * 							in TTLBATCH.DAT to ensure it is current. 
 * 							modify TitleBatchGenerator(), readFile()
 *							defect 4533 
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3			 	
 * T Pederson	02/26/2009	Added error messages specific to  
 * 							ttlbatch.dat. If error occurs when trying   
 * 							to read the file, automatically try to 
 * 							recreate the file. Added close after 
 * 							reading file.
 * 							modify readFile(), writeFile(), 
 * 							TitleBatchGenerator()
 * 							defect 7418 Ver Defect_POS_D 
 * ---------------------------------------------------------------------
 */

/**
 * Singleton class used to handle batch count and batch number.
 * <P>The batch number, count, and date is stored in ttlbatch.dat.
 * Every day, the batch number, count starts with 1.  At each
 * increment, the count increments by 1.  After the count
 * reaches 100, the batch number will be incremented and the count is reset
 * to 1.
 * Usage:
 * TitleBatchGenerator.getInstance.increment();
 * TitleBatchGenerator.getInstance.getBatchNo();
 * TitleBatchGenerator.getInstance.getCount();
 * 
 * @version	Defect_POS_D		02/26/2009 
 * @author	Nancy Ting	
 * <br>Creation Date:		09/26/2001 18:10:16
 */
public class TitleBatchGenerator implements java.io.Serializable
{
	// int 
	private int ciCount;
	private int ciBatchNo;

	// Object 
	private RTSDate caDate;
	private static TitleBatchGenerator saTitleBatchGenerator = null;

	// Constant
	public static final int BATCH_COUNT = 100;
	private final static String FILE_NAME = "ttlbatch.dat";
	private final static String DELIMITER = ",";
	private final static String DATE_SEPARATOR = "/";

	private final static long serialVersionUID = 6225842145083729722L;
	/**
	 * CustomerSequence constructor comment.
	 * 
	 * @throws RTSException 
	 */
	protected TitleBatchGenerator() throws RTSException
	{
		super();
		boolean lbSuccess = readFile();
		// defect 7418
		//if (!lbSuccess || isStaleDate())
		if (!lbSuccess)
		{
			writeFile(true);
		}
		// end defect 7418
	}
	/**
	 * Get batch number
	 * 
	 * @return int
	 */
	public int getBatchNo()
	{
		return ciBatchNo;
	}
	/**
	 * Get count
	 * 
	 * @return int
	 */
	public int getCount()
	{
		return ciCount;
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
	 * Get the TitleBatchGenerator graph
	 * 
	 * @return CustomerSequence
	 * @throws RTSException 
	 */
	public static TitleBatchGenerator getInstance() throws RTSException
	{

		saTitleBatchGenerator = new TitleBatchGenerator();

		return saTitleBatchGenerator;
	}
	/**
	 * increment
	 * 
	 * @throws RTSException 
	 */
	public void increment() throws RTSException
	{
		++ciCount;
		if (ciCount > BATCH_COUNT)
		{
			ciBatchNo++;
			ciCount = 1;
		}
		writeFile(false);
	}
	/**
	 * Determine if the date stored is stale
	 * 
	 * @return boolean
	 */
	private boolean isStaleDate()
	{
		if (caDate == null)
		{
			return true;
		}

		RTSDate laRTSDateToday = new RTSDate();

		if (!((laRTSDateToday.getDate() == caDate.getDate())
			&& (laRTSDateToday.getMonth() == caDate.getMonth())
			&& (laRTSDateToday.getYear() == caDate.getYear())))
		{
			return true;
		}
		return false;
	}
	/**
	 * Return value of 
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			for (int i = 1; i < 50; i++)
			{
				System.out.println(
					TitleBatchGenerator.getInstance().getCount()
						+ "\t"
						+ TitleBatchGenerator.getInstance().getBatchNo());
				TitleBatchGenerator.getInstance().increment();

			}
		}
		catch (RTSException leRTSEx)
		{
			leRTSEx.printStackTrace();
		}

	}
	/**
	 * Read the ttlbatch.dat file and load data in memory
	 * 
	 * @return boolean
	 * @throws RTSException 
	 */
	private boolean readFile() throws RTSException
	{
		boolean lbSuccess = true;
		try
		{
			// defect 7418
			// Removed this code so that a FileNotFoundException can
			// be caught and logged
			//File laFile = new File(FILE_NAME);
			//if (!laFile.exists())
			//{
			//	return false;
			//}
			// end defect 7418

			//Read the Title Batch File
			BufferedReader laBufferedInputStream =
				new BufferedReader(new FileReader(FILE_NAME));
			String lsFirstLine = laBufferedInputStream.readLine();
			// defect 7418
			laBufferedInputStream.close();
			// end defect 7418
			StringTokenizer laStrTokenizer =
				new StringTokenizer(lsFirstLine, DELIMITER);
			if (laStrTokenizer.countTokens() == 3)
			{
				ciBatchNo =
					Integer.parseInt(laStrTokenizer.nextToken().trim());
				ciCount =
					Integer.parseInt(laStrTokenizer.nextToken().trim());
				String lsDate = laStrTokenizer.nextToken().trim();

				StringTokenizer lDateTokenizer =
					new StringTokenizer(lsDate, DATE_SEPARATOR);
				int liDateSepCount = lDateTokenizer.countTokens();
				if (liDateSepCount == 3)
				{
					String lsMonth = lDateTokenizer.nextToken();
					String lsDay = lDateTokenizer.nextToken();
					String lsYear = lDateTokenizer.nextToken();
					caDate =
						new RTSDate(
							Integer.parseInt(lsYear),
							Integer.parseInt(lsMonth),
							Integer.parseInt(lsDay));

					// if the date from TtlBatch.dat is not current, 
					// create a new TtlBatch.dat file
					// defect 7418
					if (isStaleDate())
					{
						lbSuccess = false;
						//writeFile(true);
					}
					// end defect 7418
				}
				else
				// defect 7418
				{
					//throw new RTSException(
					//	RTSException.FAILURE_MESSAGE,
					//	"Title Batch file format incorrect",
					//	"Error");
					RTSException leRTSEx = new RTSException(803);
					leRTSEx.displayError((javax.swing.JDialog) null);
					//logging only
					lbSuccess = false;
				}
				// end defect 7418
			}
			else
			// defect 7418
			{
				//throw new RTSException(
				//	RTSException.FAILURE_MESSAGE,
				//	"Title Batch file format incorrect",
				//	"Error");
				RTSException leRTSEx = new RTSException(803);
				leRTSEx.displayError((javax.swing.JDialog) null);
				//logging only
				lbSuccess = false;
			}
			// end defect 7418
		}
		// defect 7418
		//catch (FileNotFoundException leFileNotFoundException)
		//{
		//	RTSException leRTSEx = new RTSException(225);
		//	leRTSEx.displayError((javax.swing.JDialog) null);
		//	//logging only
		//	lbSuccess = false;
		//}
		catch (FileNotFoundException aeFileNotFoundException)
		{
			RTSException leRTSEx = new RTSException(802);
			leRTSEx.displayError((javax.swing.JDialog) null);
			//logging only
			lbSuccess = false;
		}
		//catch (IOException leIOEx)
		//{
		//	RTSException leRTSEx = new RTSException(232);
		//	leRTSEx.displayError((javax.swing.JDialog) null);
		//	//logging only
		//	lbSuccess = false;
		//}
		catch (IOException aeIOEx)
		{
			RTSException leRTSEx = new RTSException(801);
			leRTSEx.displayError((javax.swing.JDialog) null);
			//logging only
			lbSuccess = false;
		}
		catch (NullPointerException aeNullPointerException)
		{
			RTSException leRTSEx = new RTSException(803);
			leRTSEx.displayError((javax.swing.JDialog) null);
			//logging only
			lbSuccess = false;
		}
		catch (NumberFormatException aeNumberFormatException)
		{
			RTSException leRTSEx = new RTSException(803);
			leRTSEx.displayError((javax.swing.JDialog) null);
			//logging only
			lbSuccess = false;
		}
		// end defect 7418

		return lbSuccess;
	}
	/**
	 * reset count and batch count to 0
	 * 
	 * @throws RTSException 
	 */
	public void reset() throws RTSException
	{
		writeFile(true);
	}
	/**
	 * Set batch number
	 * 
	 * @param aiBatchNo int
	 */
	public void setBatchNo(int aiBatchNo)
	{
		ciBatchNo = aiBatchNo;
	}
	/**
	 * Set count
	 * 
	 * @param aiCount int
	 */
	public void setCount(int aiCount)
	{
		ciCount = aiCount;
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
	 * Write file to ttlbatch.dat
	 * 
	 * @param abInit boolean
	 * @throws RTSException
	 */
	private void writeFile(boolean abInit) throws RTSException
	{
		if (abInit)
		{
			ciBatchNo = 1;
			ciCount = 1;
		}
		try
		{
			caDate = new RTSDate();
			BufferedWriter laBufferedWriter =
				new BufferedWriter(new FileWriter(FILE_NAME));
			laBufferedWriter.write(
				ciBatchNo
					+ DELIMITER
					+ ciCount
					+ DELIMITER
					+ caDate.toString());
			laBufferedWriter.flush();
			laBufferedWriter.close();
		}
		catch (IOException leIOEx)
		{
			// defect 7418
			//RTSException leRTSEx = new RTSException(226);
			RTSException leRTSEx = new RTSException(804);
			throw leRTSEx;
			// end defect 7418
		}
	}
}
