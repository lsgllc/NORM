package com.txdot.isd.rts.services.util;

/*
 * 
 * DBBatchProcess.java
 * 
 * (c) Texas Department of Transportation 2002
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	11/11/2002	Added writes to BatchLog
 * Ray Rowehl	10/19/2007	Write script names to system.out.
 * 							Also refactor the names.
 * 							modify callDBBackup1(),
 * 								callDBBackup2()
 * 							defect 9381 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Launches the DB2 Backup scripts from WAS environment.
 *
 * @version	Special Plates	10/19/2007
 * @author	Richard Hicks
 * <br>Creation Date:		06/14/2002 14:28:11
 */

public class DBBatchProcess
{

	/**
	 * call the first database backup script. 
	 * 
	 * @return boolean
	 */
	public static boolean callDBBackup1()
	{
		boolean lbResult = false;
		//  Read in initialization file and set values
		DB2BatchProperties laDbProps = new DB2BatchProperties();
		laDbProps.setProperties();

		BatchLog.write("Start DB Backup Script1 ");

		RemoteExecution laRexec =
			new RemoteExecution(
				System.getProperty(DB2BatchProperties.DB2_HOSTNAME),
				System.getProperty(DB2BatchProperties.DB2_USERID),
				System.getProperty(DB2BatchProperties.DB2_PASSWORD));

		String lsReturnString =
			laRexec.executeProgram(
				System.getProperty(DB2BatchProperties.DB2_SCRIPT1));

		if ((lsReturnString == null)
			|| !(lsReturnString.trim().equalsIgnoreCase("exit 0")))
		{
			// defect 9381
			System.out.println(
				"Error executing script1. "
					+ System.getProperty(DB2BatchProperties.DB2_SCRIPT1));
			System.out.println(
				"Script host "
					+ System.getProperty(
						DB2BatchProperties.DB2_HOSTNAME));
			System.out.println(
				"Script user "
					+ System.getProperty(DB2BatchProperties.DB2_USERID));
			// end defect 9381

			BatchLog.write("Error executing DB Backup Script1.");
			lbResult = false;
		}
		else
		{
			System.out.println("Script execution successful.");
			BatchLog.write("DB Backup Script1 execution successful.");
			lbResult = true;
		}

		return lbResult;
	}

	/**
	 * Run second db backup script.
	 * 
	 * @return boolean
	 */
	public static boolean callDBBackup2()
	{
		boolean lbResult = false;

		BatchLog.write("Start DB Backup Script2");

		RemoteExecution laRexec =
			new RemoteExecution(
				System.getProperty(DB2BatchProperties.DB2_HOSTNAME),
				System.getProperty(DB2BatchProperties.DB2_USERID),
				System.getProperty(DB2BatchProperties.DB2_PASSWORD));

		String lsReturnString =
			laRexec.executeProgram(
				System.getProperty(DB2BatchProperties.DB2_SCRIPT2));

		if ((lsReturnString == null)
			|| !(lsReturnString.trim().equalsIgnoreCase("exit 0")))
		{
			// defect 9381
			System.out.println(
				"Error executing script. "
					+ DB2BatchProperties.DB2_SCRIPT2);
			System.out.println(
				"Script host "
					+ System.getProperty(
						DB2BatchProperties.DB2_HOSTNAME));
			System.out.println(
				"Script user "
					+ System.getProperty(DB2BatchProperties.DB2_USERID));
			// end defect 9381

			BatchLog.write("Error executing DB Backup Script2.");
			lbResult = false;
		}
		else
		{
			System.out.println("Script execution successful.");
			BatchLog.write("DB Backup Script2 execution successful.");
			lbResult = true;
		}

		return lbResult;
	}
}
