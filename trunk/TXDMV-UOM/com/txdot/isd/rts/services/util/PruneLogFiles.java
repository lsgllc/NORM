package com.txdot.isd.rts.services.util;
import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
/*
 *
 * PruneLogFiles.java
 *
 * (c) Texas Department of Transportation 2002.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * RHicks		12/03/2002	Creation of class for defect 5118
 * Jeff S.		10/06/2005	Added list of files to be pruned to the cls.
 * 							Altered class to be able to be called from 
 * 							a another class.  Before only main() could
 * 							start this process.  This process will now
 * 							be called from RTSMain.
 * 							add NO_FILES_MSG, pruneLogFiles()
 * 							remove csAppLogFile, csBatchLogFile, 
 * 								csSendCacheLogFile, csRefreshLogFile
 * 							deprecate alterFileName(), convertNumber()
 * 							defect 8366, 8367 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * RTS II PruneLogFiles
 * 
 * This class is used to prune all of the log files that are used by
 * RTS II.  The list of files to be pruned are stored in the rtscls.cfg
 * file.
 * 
 * @version	5.2.3			10/06/2005
 * @author	Richard Hicks
 * <br>Creation Date:		12/02/2002	15:52:09
 */
public class PruneLogFiles
{
	// defect 8366, 8367
	private static final String NO_FILES_MSG =
		"There are no files to prune. Check CLS file.";
	//private String csAppLogFile;
	//private String csBatchLogFile;
	//private String csSendCacheLogFile;
	//private String csRefreshLogFile;
	// end defect 8366, 8367
	static private Hashtable monthsTxt;
	static {
		monthsTxt = new Hashtable();
		monthsTxt.put("1", "Jan");
		monthsTxt.put("2", "Feb");
		monthsTxt.put("3", "Mar");
		monthsTxt.put("4", "Apr");
		monthsTxt.put("5", "May");
		monthsTxt.put("6", "Jun");
		monthsTxt.put("7", "Jul");
		monthsTxt.put("8", "Aug");
		monthsTxt.put("9", "Sep");
		monthsTxt.put("10", "Oct");
		monthsTxt.put("11", "Nov");
		monthsTxt.put("12", "Dec");
	}

	/**
	 * PruneLogFiles constructor comment.
	 */
	public PruneLogFiles()
	{
		super();
	}
	/**
	 * Returns the string value of a number padded with a zero.  If
	 * the number is 1-9 then 01-09 is returned anything greater than
	 * 9 will return the string value of that number.
	 * 
	 * @param liNum int
	 * @return String
	 * @deprecated
	 */
	private static String convertNumber(int liNum)
	{
		switch (liNum)
		{
			case 1 :
			case 2 :
			case 3 :
			case 4 :
			case 5 :
			case 6 :
			case 7 :
			case 8 :
			case 9 :
				return CommonConstant.STR_ZERO + liNum;
			default :
				return String.valueOf(liNum);
		}
	}
	/**
	 * This method is used to test the prune log class.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		// defect 8366
		// Moved code to load files to prune into the pruneLogFiles 
		// class
		// not needed b/c each getter has this.
		//SystemProperty.initialize();
		//PruneLogFiles laPlf = new PruneLogFiles();

		//Vector lvFilesToBePruned = new Vector();
		//lvFilesToBePruned.add(
		//	laPlf.csAppLogFile = SystemProperty.getLogFileName());
		//lvFilesToBePruned.add(
		//	laPlf.csSendCacheLogFile =
		//		SystemProperty.getSendCacheLogFileName());
		//lvFilesToBePruned.add(
		//	laPlf.csBatchLogFile =
		//		SystemProperty.getBatchLogFileName());
		//lvFilesToBePruned.add("REFRESH");

		//for (int i = 0; i < lvFilesToBePruned.size(); i++)
		//{
		//	String lsFilename =
		//		laPlf.alterFileName(
		//			(String) lvFilesToBePruned.elementAt(i));
		//	laPlf.puneLogFile(lsFilename);
		//}
		//lvFilesToBePruned.removeAllElements();
		//lvFilesToBePruned = null;
		PruneLogFiles.pruneLogFiles();
		// end defect 8366
	}
	/**
	 * Method that will do the actual prune work.  This method will 
	 * load all of the files to prune and search through each line of 
	 * the log file and look for the graph of the purge date.  When
	 * the purge date is not found that line is removed from the final 
	 * lines.  If the purge date is never found then the log file is not
	 * pruned.  Once the prune date is found a writer will write back 
	 * the remaining log file after the purge date that was found. 
	 */
	public static void pruneLogFiles()
	{
		// defect 8366, 8367
		// Changed to use RTSDate since date had deprecated methods.
		// Obtain the purge date - 7 days from todays date
		//Calendar today = Calendar.getInstance();
		//long llMs = today.getTime().getTime();
		//Date laPurgeDate = new Date(llMs - (7 * 24 * 60 * 60 * 1000));
		//int liPurgeMonth = laPurgeDate.getMonth() + 1;
		//int liPurgeDay = laPurgeDate.getDate();
		RTSDate laRTSDate = new RTSDate().add(RTSDate.DATE, -7);
		int liPurgeMonth = laRTSDate.getMonth();
		int liPurgeDay = laRTSDate.getDate();

		// two different ways to scan the log - 08/31 or Aug 31
		// Using
			//convertNumber(liPurgeMonth)
		String lsComp1 =
			UtilityMethods.addPadding(
				String.valueOf(liPurgeMonth),
				2,
				CommonConstant.STR_ZERO)
				+ CommonConstant.STR_SLASH
				+ UtilityMethods.addPadding(
					String.valueOf(liPurgeDay),
					2,
					CommonConstant.STR_ZERO);
				//+ convertNumber(liPurgeDay);
		
		String lsComp2 =
			monthsTxt.get(String.valueOf(liPurgeMonth))
				+ CommonConstant.STR_SPACE_ONE
				+ UtilityMethods.addPadding(
					String.valueOf(liPurgeDay),
					2,
					CommonConstant.STR_ZERO);
				//+ convertNumber(liPurgeDay);

		// Moved this from main to this method so that other classes
		// could call this method to activate prune.
		// Get all of the files to prune and loop through them
		Vector lvFileToPrune = SystemProperty.getFilesToPrune();

		// If there are no files then log it
		if (lvFileToPrune.size() == 0)
		{
			Log.write(Log.SQL_EXCP, null, NO_FILES_MSG);
		}

		for (int liFile = 0; liFile < lvFileToPrune.size(); liFile++)
		{
			try
			{
				// If the log file does not exist then continue
				if (!new File(String
					.valueOf(lvFileToPrune.get(liFile)))
					.exists())
				{
					continue;
				}
				// This will hold all the lines of the log file
				Vector lvLines = new Vector();

				// Read in the log file and load it into the vector of 
				// lines
				//FileInputStream lpfsInput =
				//	new FileInputStream(
				//		SystemProperty.getRTSAppDirectory()
				//			+ asFilename
				//			+ "."
				//			+ SystemProperty.getLogFileExt());
				FileInputStream lpfsInput =
					new FileInputStream(
						String.valueOf(lvFileToPrune.get(liFile)));
				BufferedReader labuffReader =
					new BufferedReader(
						new InputStreamReader(lpfsInput));
				String lsLine = null;
				while ((lsLine = labuffReader.readLine()) != null)
				{
					lvLines.add(lsLine);
				}
				labuffReader.close();
				lpfsInput.close();

				// Loop through the vector of lines and look for the 
				// purge date. If the purge date is not found then 
				// remove the line from the vector. Once the purge date 
				// is found break from the loop. We will be left with 
				// the lines after the purge date.
				while (lvLines.size() > 0)
				{
					lsLine = (String) lvLines.elementAt(0);
					if ((lsLine.startsWith(lsComp1))
						|| (lsLine.indexOf(lsComp2) > -1))
					{
						break;
					}
					lvLines.removeElementAt(0);
				}

				// If lines are 0 then the log was empty or the purge 
				// date was not found
				if (lvLines.size() > 0)
				{
					// Write the remaining log lines after the purge
					// date back to the log
					//FileOutputStream lpfsOutputStream =
					//	new FileOutputStream(
					//		SystemProperty.getRTSAppDirectory()
					//			+ asFilename
					//			+ "."
					//			+ SystemProperty.getLogFileExt(),
					//		false);
					FileOutputStream lpfsOutputStream =
						new FileOutputStream(
							String.valueOf(lvFileToPrune.get(liFile)),
							false);
					BufferedWriter laBuffWriter =
						new BufferedWriter(
							new OutputStreamWriter(lpfsOutputStream));
					for (int i = 0; i < lvLines.size(); i++)
					{
						laBuffWriter.write(
							(String) lvLines.elementAt(i));
						laBuffWriter.newLine();
					}
					laBuffWriter.flush();
					laBuffWriter.close();
					lpfsOutputStream.close();
				}
				// Remove all of the elements from the lines vector for 
				// the next log to prune.
				lvLines.removeAllElements();
				lvLines = null;
			}
			catch (Exception leEx)
			{
				RTSException leRTSEx =
					new RTSException(RTSException.JAVA_ERROR, leEx);
			}
		}
		lvFileToPrune.removeAllElements();
		lvFileToPrune = null;
		// end defect 8366, 8367
	}
	/**
	 * This method will remove the ".".
	 * 
	 * @return String
	 * @param asFilename String
	 * @deprecated
	 */
	public String alterFileName(String asFilename)
	{
		//Just remove the .
		if (asFilename.indexOf(CommonConstant.STR_PERIOD) != -1)
		{
			return asFilename.substring(0, asFilename.length() - 1);
		}
		else
		{
			return asFilename;
		}
	}
}