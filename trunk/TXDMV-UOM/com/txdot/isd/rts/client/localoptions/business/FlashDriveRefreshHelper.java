package com.txdot.isd.rts.client.localoptions.business;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.localoptions.ui.FrmRSPSStatusUpdateRSP001;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.refresh.RefreshProgressBarPanel;
import com.txdot.isd.rts.services.util.refresh.RefreshProperty;
import com.txdot.isd.rts.services.util.refresh.RefreshUtility;
/*
 * FlashDriveRefreshHelper.java
 *
 * (c) Texas Department of Transportation 2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	09/13/2004	new class
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	09/20/2004	Add Dialog to support exceptions
 *							defect 7135 Ver 5.2.1
 * Jeff S.		05/04/2005	This thread will let the calling frame
 * 							know when it is done.
 * 							modify run()
 * 							defect 8190 Ver. 5.2.2 Fix 4
 * Jeff S.		09/19/2005	Changed the call from RSPSRefreshUtiltity
 * 							to Refresh utility.  This class is in an
 * 							external jar.  Also changed the catch when
 * 							call refresh utility from Exception to
 * 							Throwable.  Replaced the progress.gif on
 * 							the update frame with a progress bar and 
 * 							added a call to reset the progress bar back
 * 							to it's original state when thread is done.
 * 							modify run()
 * 							defect 8014 Ver. 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This class runs the Flash Drive Refresh process as a thread.
 *
 * @version	5.2.3			09/19/2005
 * @author	Ray Rowehl
 * <br>Creation Date:		09/13/2004 08:11:11
 */
public class FlashDriveRefreshHelper implements Runnable
{
	/**
	 * Refresh Exclusion Vector
	 */
	Vector cvExcludeVector = null;
	/**
	 * RTSDialogBox that exceptions need to be show against.
	 */
	RTSDialogBox caDialog = null;

	private static final String PROCESSING_TITLE =
		" - Processing......";
	private static final String THREAD_NAME = "FlashRefresh";
	private static final String EMPTY_STRING = "";

	/**
	 * This is the default java constructor for FlashDriveRefreshHelper.
	 * This will call the normal constructor.
	 */
	public FlashDriveRefreshHelper()
	{
		this(new Vector(), null);
	}
	/**
	 * This is the normal constructor for FlashDriveRefreshHelper.
	 * Set the Exclude Vector and the Dialog values.
	 * 
	 * @param avRefreshVector Vector
	 * @param aaDialog RTSDialogBox
	 */
	public FlashDriveRefreshHelper(
		Vector avRefreshVector,
		RTSDialogBox aaDialog)
	{
		super();
		cvExcludeVector = avRefreshVector;
		caDialog = aaDialog;
	}
	/**
	 * This is a test method to ensure that the class starts ok.
	 * 
	 * @param aaarArgs String[]
	 */
	public static void main(String[] aaarArgs)
	{
		// Instantiate the class.
		FlashDriveRefreshHelper laFDRH =
			new FlashDriveRefreshHelper(new Vector(), null);

		// Start the thread
		Thread laThread = new Thread(laFDRH, THREAD_NAME);
		laThread.start();
	}
	/**
	 * Start the Flash Drive Refresh thread.
	 * <p>
	 * When an object implementing interface <code>Runnable</code> is
	 * used to create a thread, starting the thread causes the object's 
	 * <code>run</code> method to be called in that separately executing 
	 * thread. 
	 * <p>
	 * The general contract of the method <code>run</code> is that it 
	 * may take any action whatsoever.
	 *
	 * @see     java.lang.Thread#run()
	 */
	public void run()
	{
		// defect 8190
		// Added code to send a message back to the calling frame to
		// let the frame know that this thread is done.
		boolean lbError = false;
		FrmRSPSStatusUpdateRSP001 laFrmRSPSUpdate = null;
		String lsTitle = EMPTY_STRING;
		RefreshUtility lRRU = null;

		try
		{

			if (caDialog != null
				&& caDialog instanceof FrmRSPSStatusUpdateRSP001)
			{
				laFrmRSPSUpdate = (FrmRSPSStatusUpdateRSP001) caDialog;
				lsTitle = laFrmRSPSUpdate.getTitle();
				laFrmRSPSUpdate.setTitle(lsTitle + PROCESSING_TITLE);
				laFrmRSPSUpdate.enableFame(false);

				// defect 8014
				// Removed the 2 second sleep - not needed with progress
				// bar.
				// Added 2 second sleep so that everyone will see the
				// progress gif even if there are no updates. Approved
				// by VTR
				//Thread.sleep(2000);
				// end defect 8014
			}

			// defect 8014
			// Changed from RSPSRefreshUtiltity to RefreshUtility this
			// constructor accepts the vector of updates.
			//RSPSRefreshUtility lRRU = new RSPSRefreshUtility();
			lRRU =
				new RefreshUtility(
					RefreshProperty.TYPE_COUNTY_TO_FLASH,
					cvExcludeVector);
			//lRRU.loadFilesToFlashDrive(cvExcludeVector);
			lRRU.startRefresh();
		}
		catch (Throwable leThrowable)
		{
			lbError = true;
			// catch and log the exception.
			// can not throw back.
			//RTSException lRTSE = new RTSException(
			//						RTSException.JAVA_ERROR, lE);

			// It is safe in this situation to cast Throwable as 
			// Exception b/c this phase does not map a network drive 
			// which throws a throwable
			RTSException laRTSEx =
				new RTSException(RTSException.JAVA_ERROR, leThrowable);

			//display exception if dialog is available
			if (caDialog != null)
			{
				laRTSEx.displayError(caDialog);
			}

			leThrowable = null;
			laRTSEx = null;
			// end defect 8014
		}
		finally
		{
			if (laFrmRSPSUpdate != null)
			{
				laFrmRSPSUpdate.setTitle(lsTitle);

				if (!lbError)
				{
					laFrmRSPSUpdate.handleReturnFromRefresh();
				}
				// defect 8014
				// Added call to reset the progress bar panel back to 
				// the original state.
				if (lRRU != null)
				{
					// Reset ProgressBar panel back to original state
					RefreshProgressBarPanel.resetProgressBarPanel();
				}
				// end defect 8014
				laFrmRSPSUpdate.enableFame(true);
			}
		}
		// end defect 8190
	}
}