package com.txdot.isd.rts.services.util;

import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

/* 
 * BuildSendTransSleep.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		11/18/2008	New class
 * 							Set timer for updates not alllied
 * 							defect 8984 Ver MyPlatesII
 * J Rue		07/21/2009	Move clas from RTS project to RCCPI project
 * 							defect 10140 Ver Defecf_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * Utility to sleep for BuildSendTrans
 *
 * @version	MyPlatesII		07/21/2009
 * @author Jeff Rue	
 * <p>Creation Date:		11/18/2008 07:45:35
 */
public class BuildSendTransSleep
	extends RTSDialogBox
	implements Runnable
{
	// int
	int ciSleepTime = 5000;

	/**
	 * RTSDialogBox that exceptions need to be show against.
	 */
	RTSDialogBox caDialog = null;

	/**
	 * Constructor
	 */
	public BuildSendTransSleep()
	{
		super();
	}

	/**
	 * Constructor to pass info from calling program
	 */
	public BuildSendTransSleep(RTSDialogBox aaDialog)
	{
		super();
		caDialog = aaDialog;
	}

	public static void main(String[] args)
	{
	}
	/**
	 * Make Rexec call to get info
	 * 
	 * @return boolean
	 */
	public static boolean executeProgram()
	{
		return true;
	}
	/**
	 * Run
	 */
	public void run()
	{
		System.out.println("Sleeping - " + String.valueOf(ciSleepTime));
		BuildSendTransaction laBldSendTrans = null;
		laBldSendTrans = (BuildSendTransaction) caDialog;

		// Set frame info from thread
		try
		{
			Thread.sleep(ciSleepTime);
		}
		catch (InterruptedException aeIExc)
		{
			aeIExc.printStackTrace();
		}
		laBldSendTrans.gettxtlblUpdtsNotApplied().setVisible(false);
		System.out.println("Waking up");
	}
	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		// empty block of code
	}
}
