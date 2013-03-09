package com.txdot.isd.rts.client.localoptions.ui;

import java.util.Calendar;
import java.util.Vector;
// defect 11322
//import com.txdot.isd.rts.client.desktop.DisclaimerWindow;
// end defect 11322
// defect 11323
//import com.txdot.isd.rts.client.desktop.Inactivity;
// end defect 11323
import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.cache.OfficeTimeZoneCache;
import com.txdot.isd.rts.services.data.LogonData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.localoptions.JniAdInterface;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;

/*
 * VCLogonSEC001.java
 * 
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			04/11/2002	Fixing Bug to make focus return to Name 
 *							textfield after Workstation Locked 
 *							CQU100003432
 * MAbs			04/12/2002  Fixing Bug to stop NullPointerException 
 *							when "I Disagree" is pressed in 
 *							Disclaimer Window 
 *							CQU100003469
 * RH       	06/13/2002  Retrieve time from WAS rather than 
 *							Mainframe  
 *							defect 3972
 * RHicks		10/30/2020	CQ4999 - DosCreateProcess rc=8 fix
 * Min Wang 	02/13/2003	Modified handleLoginReturn(). 
 *							Defect CQU100006464.
 * Ray Rowehl 	05/19/2003 	allow application to pick up user id 
 *							from system 
 *							modify handleLoginReturn(), 
 *								setView(), and processData()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/04/2004	Catch dll error and give it error 750.
 *							modify setView()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/18/2004	reflow java comments to match updated
 * 							java standards.
 * 							defect 6445 Ver 5.1.6
 * K Harrell	03/29/2004	remove OS dependent code
 *							handleLoginReturn(),setView(),
 *							processData()
 *							remove incorrectGuesses,shouldshow,NEW,
 *							FrmWorkstationLockedCTL007
 *							defect 6955 Ver 5.2.0
 * Ray Rowehl	04/12/2004	Add logging so we know the state of the user
 *							logon attempt.
 *							modify handleLoginReturn(), setView()
 *							defect 7006 Ver 5.1.6
 * Ray Rowehl	05/06/2004	Re-add some code that was taken out as part
 *							of 6955.  This code is needed to handle
 *							cancel from Disclaimer and CachedLogin
 *							modify handleLoginReturn(), processData()
 *							defect 6955 Ver 5.2.0
 * Ray Rowehl	05/17/2004	Merge 7030 into 5.2.0
 *							Turn on timer when showing the Disclaimer
 *							screen.  Also setup Disclaimer as frame to
 *							aid in cleanup.
 *							modify setView()
 *							defect 7030 Ver 5.2.0
 * Min Wang		02/28/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 *							defect 7891  Ver 5.2.3
 * Min Wang		09/08/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Jeff S.		06/28/2006	Added a call to a native method to fix the 
 * 							focus problem with the JRE.
 * 							modify setView()
 * 							defect 8756 Ver 5.2.3 
 * T Pederson	06/06/2007	Corrected error with constant CMD.  Renamed   
 * 							to CMDCHANGEDATE and changed from 
 * 							"cmd /c time " to "cmd /c date ".
 * 							defect 9160 Ver Special Plates
 * Ray Rowehl	07/13/2009	Add defaults to switch statements.
 * 							Use isDevStatus().
 * 							Use RTSException.YES in place of "2".
 * 							Log Exceptions on changing the date.
 * 							Reflow the Decline error checking in 
 * 							setView().
 * 							Show the empid on logoff.
 * 							Remove old defect markers and 
 * 							clean up some comments.  
 * 							add LOGIN_SUCCESS, UNEXPECTED_LOGON_RESULT,
 * 								WRONG_INSTANCE_TYPE_LOGONDATA
 * 							delete LOGIN_SUCCESS, 
 * 								LOGIN_SUCCESS_FROM_CACHE
 * 							modify handleLoginReturn(), processData(),
 * 								setView()
 * 							defect 10103 Ver POS_Defect_F 
 * K Harrell	04/04/2010	Set TimeZone w/ OfficeTimeZoneCache() 
 * 							Reorganize imports.
 * 							modify setView() 
 * 							defect 10427 Ver POS_640 
 * M Reyes		03/26/2012	Remove the Disclaimer
 * 							Modify setView()
 * 							defect 11322 Ver POS 7.0
 * M Reyes		04/02/2012	Remove application timeout
 * 							modify setView()
 * 							defect 11323 Ver POS 7.0
 * ---------------------------------------------------------------------
 */

/**
 * Controller for Logon SEC001
 * 
 * @version	POS_700				04/02/2012
 * @author	Michael Abernethy
 * <br>Creation date			03/18/2001 15:56:54
 */

public class VCLogonSEC001 extends AbstractViewController
{
	public static final String BAD_LOGIN = "Bad login for ";
	public static final String CHANGE_MSG =
		". Do you wish to change the ";

	public static final String CMDCHANGEDATE = "cmd /c date ";
	public static final String EXC_USER_ID =
		" Exception while getting the UserId";
	public static final String GOT_EXC = "Got an Exception ";
	// defect 10103
	//public static final int LOGIN_SUCCESS = 21;
	//public static final int LOGIN_SUCCESS_FROM_CACHE = 618;
	// end defect 10103
	public static final String MF_DATE = ". MF Date is ";
	public static final String NO_ACCEPT_LOGIN =
		"User did not accept logon ";
	public static final String PC_DATE = "PC Date is ";
	public static final String PC_TO_MF_DATE =
		"PC Date to the MF date?";
	public static final String STR_SPACE_DASH = " - ";
	public static final String SUCC_LOGIN = "Successful login for ";
	private static final String UNEXPECTED_LOGON_RESULT =
		"Unexpected Logon result from server ";
	// defect 10103
	private static final String WRONG_INSTANCE_TYPE_LOGONDATA =
		" aaReturnData is not of LogonData type!";
	// end defect 10103

	/**
	 * VCLogonSEC001 constructor comment.
	 */
	public VCLogonSEC001()
	{
		super();
	}

	/**
	 * Returns the Module name constant used by the RTSMediator to pass 
	 * the data to the appropriate Business Layer class.
	 * 
	 * @return String
	 */
	public int getModuleName()
	{
		return GeneralConstant.LOCAL_OPTIONS;
	}

	/**
	 * Get the client's date.
	 * 
	 * @return String
	 */
	public String getPCDate()
	{
		Calendar laCalendar = Calendar.getInstance();
		String lsMonth =
			Integer.toString(laCalendar.get(Calendar.MONTH) + 1);
		String lsDate = Integer.toString(laCalendar.get(Calendar.DATE));
		String lsYear = Integer.toString(laCalendar.get(Calendar.YEAR));
		if (lsDate.length() == 1)
		{
			lsDate = CommonConstant.STR_ZERO + lsDate;
		}
		if (lsMonth.length() == 1)
		{
			lsMonth = CommonConstant.STR_ZERO + lsMonth;
		}
		return lsMonth
			+ CommonConstant.STR_DASH
			+ lsDate
			+ CommonConstant.STR_DASH
			+ lsYear;
	}

	/**
	 * Handles the logon process depending on the status of Logon 
	 * attempt
	 * 
	 * @param aaReturnData Object
	 */
	private void handleLoginReturn(Object aaReturnData)
	{
		// defect 10103
		LogonData laLogonData = new LogonData();

		// make sure we are working with LogonData
		// If not, take the default exit.
		if (aaReturnData instanceof LogonData)
		{
			laLogonData = (LogonData) aaReturnData;
		}
		else
		{
			Log.write(
				Log.START_END,
				this,
				WRONG_INSTANCE_TYPE_LOGONDATA);
			laLogonData.setReturnCode(Integer.MIN_VALUE);
		}
		// end defect 10103
		if (laLogonData.getMainframeTime() != null)
		{
			try
			{
				if (laLogonData.getMainframeDate() != null)
				{
					// check to see if the PC date and the MF date are 
					// the same
					if (!isSameDate(laLogonData))
					{
						// Prompt user to change date to Server date
						RTSException leRTSEx =
							new RTSException(
								RTSException.CTL001,
								PC_DATE
									+ getPCDate()
									+ MF_DATE
									+ laLogonData.getMainframeDate()
									+ CHANGE_MSG
									+ PC_TO_MF_DATE,
								CommonConstant.STR_SPACE_EMPTY);
						// defect 10103
						int liRC = RTSException.YES;
						// end defect 10103

						// defect 10103
						if (SystemProperty.isDevStatus())
						{
							// end defect 10103
							liRC = leRTSEx.displayError(getFrame());
						}

						// change PC date if user selects yes
						// defect 10103
						if (liRC == RTSException.YES)
						{
							// end defect 10103
							Process laProcess =
								Runtime.getRuntime().exec(
									CMDCHANGEDATE
										+ laLogonData.getMainframeDate());
							laProcess.waitFor();
							laProcess = null;
						}
						leRTSEx = null;
					}
				}
			}
			catch (Exception aeEx)
			{
				// defect 10103
				// log the exception.
				RTSException leRtsEx =
					new RTSException(RTSException.JAVA_ERROR, aeEx);
				// set return code to failure.
				laLogonData.setReturnCode(
					LocalOptionConstant.LOGIN_FAIL);
				// end defect 10103
			}
		}
		switch (laLogonData.getReturnCode())
		{
			case LocalOptionConstant.LOGIN_SUCCESS :
				{
					// Log succesful login.
					RTSDate laRTSDate = RTSDate.getCurrentDate();
					String lsLogoutDate1 =
						laRTSDate
							+ STR_SPACE_DASH
							+ laRTSDate.getClockTime()
							+ STR_SPACE_DASH;
					String lsLogoutTxt1 =
						SUCC_LOGIN + laLogonData.getUserName();
					System.out.println(lsLogoutDate1 + lsLogoutTxt1);
					Log.write(
						Log.START_END,
						lsLogoutTxt1,
						lsLogoutTxt1);

					// defect 10103
					processData(
						LocalOptionConstant.LOGIN_SUCCESS,
						laLogonData.getSecurityData());
					// end defect 10103
					break;
				}
			case LocalOptionConstant.LOGIN_SUCCESS_FROM_CACHE :
				{
					// this is to handle network down login
					// Log succesful login.
					RTSDate laRTSDate = RTSDate.getCurrentDate();
					String lsLogoutDate1 =
						laRTSDate
							+ STR_SPACE_DASH
							+ laRTSDate.getClockTime()
							+ STR_SPACE_DASH;
					String lsLogoutTxt1 =
						SUCC_LOGIN + laLogonData.getUserName();
					System.out.println(lsLogoutDate1 + lsLogoutTxt1);
					Log.write(
						Log.START_END,
						lsLogoutTxt1,
						lsLogoutTxt1);

					// defect 10103
					RTSException leRTSEx =
						new RTSException(
							ErrorsConstant
								.ERR_NUM_LOGIN_SUCCESS_FROM_CACHE);
					// end defect 10103
					leRTSEx.displayError(getFrame());
					// defect 10103
					processData(
						LocalOptionConstant.LOGIN_SUCCESS,
						laLogonData.getSecurityData());
					// end defect 10103
					break;
				}
			case LocalOptionConstant.LOGIN_FAIL :
				{
					// Log unsuccesful login.
					RTSDate laRTSDate = RTSDate.getCurrentDate();
					String lsLogoutDate1 =
						laRTSDate
							+ STR_SPACE_DASH
							+ laRTSDate.getClockTime()
							+ STR_SPACE_DASH;
					String lsLogoutTxt1 =
						BAD_LOGIN + laLogonData.getUserName();
					System.out.println(lsLogoutDate1 + lsLogoutTxt1);
					Log.write(
						Log.START_END,
						lsLogoutTxt1,
						lsLogoutTxt1);

					// show the error message
					// defect 10103
					RTSException leRTSEx =
						new RTSException(
							ErrorsConstant.ERR_NUM_USERNAME_NOT_VALID);
					// end defect 10103
					leRTSEx.displayError(getFrame());
					UtilityMethods.doWindowsLogout(
						getMediator().getAppController());
					// defect 10103
					break;
				}
			default :
				{
					// This should never happen.
					// We specify the codes.
					// Document and exit out if it does.
					Log.write(
						Log.START_END,
						this,
						UNEXPECTED_LOGON_RESULT
							+ laLogonData.getReturnCode());
					UtilityMethods.doWindowsLogout(
						getMediator().getAppController());
				}
				// end defect 10103
		}
	}

	/**
	 * Compare system date to the login date returned from the server.
	 * 
	 * @param aaData LogonData  
	 * @return boolean
	 */
	private boolean isSameDate(LogonData aaData)
	{
		Calendar laCal = Calendar.getInstance();
		int liMfYear =
			Integer.parseInt(
				aaData.getMainframeDate().substring(6, 10));
		int liMfMonth =
			Integer.parseInt(aaData.getMainframeDate().substring(0, 2));
		int liMfDate =
			Integer.parseInt(aaData.getMainframeDate().substring(3, 5));
		int liPcYear = laCal.get(Calendar.YEAR);
		int liPcMonth = laCal.get(Calendar.MONTH) + 1;
		int liPcDate = laCal.get(Calendar.DATE);
		return (
			(liMfYear == liPcYear)
				&& (liMfMonth == liPcMonth)
				&& (liMfDate == liPcDate));
	}

	/**
	 * Handles data coming from their JDialogBox - inside the 
	 * subclasses implementation should be calls to fireRTSEvent() 
	 * to pass the data to the RTSMediator.
	 * 
	 * @param aiCommand int
	 * @param Object aaData
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			// defect 10103
			case LocalOptionConstant.LOGIN_SUCCESS :
				{
					// defect 10103
					setData(aaData);
					setDirectionFlow(AbstractViewController.DESKTOP);
					try
					{
						close();
						setFrame(null);
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(
							getMediator().getDesktop());
						// defect 10103
						Log.write(
							Log.START_END,
							this,
							"RTSException on Logon, Logging out!");
						UtilityMethods.doWindowsLogout(
							getMediator().getAppController());
						// end defect 10103
					}
					break;
				}
			case ENTER :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.LOGIN,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(
							getMediator().getDesktop());
						UtilityMethods.doWindowsLogout(
							getMediator().getAppController());
						return;
					}
					break;
				}
			case CANCEL :
				{
					// still need to process cancel
					UtilityMethods.doWindowsLogout(
						getMediator().getAppController());
					// defect 10103
					break;
				}
			default :
				{
					// This should never happen.
					// We specify the codes.
					// Document and exit out if it does.
					Log.write(
						Log.START_END,
						this,
						"Unexpected Logon ProcessData Command "
							+ aiCommand);
					UtilityMethods.doWindowsLogout(
						getMediator().getAppController());
				}
				// end defect 10103
		}
	}

	/**
	 * Creates the actual frame, stores the protected variables 
	 * needed by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers Vector  
	 * @param asTransCd String  
	 * @param aaData Object 
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCd,
		Object aaData)
	{
		if (aaData != null)
		{
			handleLoginReturn(aaData);
			return;
		}
		if (getFrame() == null)
		{
			// defect 11323
			// turn on the timer
			//getMediator()
			//	.getAppController()
			//	.getInactivity()
			//	.startTimer();
			// end defect 11323
			// defect 11322
			// make the disclaimer the frame to aid in cleanup
			//setFrame(new DisclaimerWindow(getMediator().getDesktop()));
			//getFrame().setVisibleRTS(true);

			//int liResponse =
			//((DisclaimerWindow) getFrame()).getReturnCode();
			//getFrame().dispose();
			// end defect 11322

			// Added call to fix the focus problem after login and the
			// disclaimer is disposed
			JniAdInterface.focusFix();

			setFrame(null);
			// defect 11322
			// logoff if user declines.
			// defect 10103
			// Treat anything that is not accept as a decline
			//if (liResponse != DisclaimerWindow.ACCEPT)
			//{
				// User has decided to cancel off Disclaimer
				//String lsLogoutMsg = NO_ACCEPT_LOGIN;
				//try
				//{
					// Use the UserName from the system to document
					// the decline.
					//lsLogoutMsg =
					//	lsLogoutMsg
					//		+ JniAdInterface.getWindowsUserName(0);
					// end defect 10103
				//}
				//catch (RTSException leRTSEx)
				//{
				//	lsLogoutMsg = GOT_EXC + leRTSEx;
				//}
				//RTSDate laRTSData = RTSDate.getCurrentDate();
				//String lsLogoutDate =
				//	laRTSData
				//		+ STR_SPACE_DASH
				//		+ laRTSData.getClockTime()
				//		+ STR_SPACE_DASH;
				//System.out.println(lsLogoutDate + lsLogoutMsg);
				//Log.write(Log.START_END, lsLogoutMsg, lsLogoutMsg);

				//processData(AbstractViewController.CANCEL, null);
				//return;
			//}
			// end of defect 11322

			// defect 10103
			// if (liResponse != DisclaimerWindow.ACCEPT)
			//{
			//	// Unexpected error from Disclaimer.  Do not logout..
			//	// processData(AbstractViewController.CANCEL, null);
			//	return;
			//}
			// end defect 10103
			// defect 11323
			// turn off the timer while we are processing logon
			// Inactivity.stopTimer();
			// end defect 11323

			// defect 10103
			// reflow this initialization sequence
			// LogonData laData = null;
			LogonData laData = new LogonData();
			// if (aaData == null)
			// {
			// 	laData = new LogonData();
			// }
			// else
			// {
			// 	laData = (LogonData) aaData;
			// }
			try
			{
				if (aaData != null && aaData instanceof LogonData)
				{
					laData = (LogonData) aaData;
				}
				// end defect 10103

				// pass an int to use the JNI Interface version of 
				// getuserid
				laData.setUserName(
					JniAdInterface.getWindowsUserName(0).toUpperCase());
				laData.setPassword(null);
				laData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				laData.setSubstaId(SystemProperty.getSubStationId());
				laData.setWsId(SystemProperty.getWorkStationId());
				
				// defect 10427 
				laData.setTimeZone(
					OfficeTimeZoneCache.getTimeZone(
						SystemProperty.getOfficeIssuanceNo()));
				// end defect 10427
				 
				// process the logon request from system user id

				setData(laData);
			}
			catch (RTSException aeRTSEx)
			{
				// catch the exception from get userid
				// display it and exit
				Log.write(Log.SQL_EXCP, this, EXC_USER_ID);
				if (getFrame() != null)
				{
					aeRTSEx.displayError(getFrame());
				}
				else
				{
					aeRTSEx.displayError(
						this.getMediator().getDesktop());
				}
				UtilityMethods.doWindowsLogout(
					this.getMediator().getAppController());
				return;
			}
			// catch link error related to dll and logout.
			catch (UnsatisfiedLinkError aeULE)
			{
				// defect 10103
				// Use error number constant
				RTSException leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_AD_DLL_MISSING);
				// Put the error on the desktop.
				leRTSEx.displayError(this.getMediator().getDesktop());
				//leRTSEx.displayError((JFrame) null);
				// end defect 10103

				// exit after showing the error
				UtilityMethods.doReboot();
				return;
			}
			processData(AbstractViewController.ENTER, getData());
			return;
		}
		setData(aaData);
		setPreviousControllers(avPreviousControllers);
		setTransCode(asTransCd);
		getFrame().setController(this);
		getFrame().setData(aaData);
	}
}
