package com.txdot.isd.rts.client.desktop;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.UIManager;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSMediator;
import com.txdot.isd.rts.client.help.business.MessageHandler;
import com.txdot.isd.rts.client.systemcontrolbatch.business.RTSMainGUI;
import com.txdot.isd.rts.client.systemcontrolbatch.business.ThreadMessenger;
import com.txdot.isd.rts.services.cache.MiscellaneousCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.MiscellaneousData;
import com.txdot.isd.rts.services.data.SecurityData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.localoptions.JniAdInterface;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ProjectDictionary;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.event.CommEvent;
import com.txdot.isd.rts.services.util.event.CommListener;
import com.txdot.isd.rts.services.util.event.ThreadEvent;

/*
 * 
 * RTSApplicationController.java	
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * RS CHANDEL  	11/28/2001	Added code for hour glass.
 * MAbs			05/14/2002  Making RTSDesktop unable to be closed 
 * 							defect 3857
 * Ray Rowehl	07/27/2002	Reset caDesktop on 
 *							java errors in actionPerformed.  We are 
 *							going back to the caDesktop at that point.
 *							defect 4526 
 * RHicks		09/03/2002	Changed method of instantiating 
 *							BarCodeScanner
 * RHicks		09/05/2002	Changed priority of BarCodeScanner thread
 * RHicks       10/02/2002  Made changes to start the application with 
 * 							DB down
 * 							defect 4800 
 * Ray Rowehl	06/15/2003	When bringing up Server, do not assume DB 
 * 							is also up.  This is under the control of 
 * 							sendcache.  Also need to trigger send cache
 *  						if DB Down.
 *							modify handleCommEvent()
 *							defect 6110
 * K Harrell	10/16/2003	On DB Down/Server Down, do not fire 
 * 							Sendcache if DB Down
 * 							add isDBReady()
 *							modify handleCommEvent()
 *							defect 6614 Ver 5.1.5 Fix 1
 * K Harrell	10/27/2003	use isDBReady() to handle lights when Comm 
 * 							event fired.
 *							change handleCommEvent()
 *							defect 6614 Ver 5.1.5 Fix 1
 * Ray Rowehl	10/28/2003	Rename the db and server status booleans so
 * 							they make more sense and so the field name 
 * 							is not confused with the getters.
 * 							add cbServerStatus, cbDBStatus 
 * 							add setDBStatus(), setServerStatus()
 *							delete isServerUp, isDBUp fields
 *							modify isDBUp(), isServerUp(), isDBReady(), 
 *							 handleCommEvent()
 *							defect 6360 Ver 5.1.5 Fix1
 * Ray Rowehl	09/03/2003	handle logoff menu requests on Windows.
 *							Add a getter for caDesktop so we can call its
 * 							public methods.
 *							Change so DeskTop can not be minimized when
 *							running Production.
 *							Code formatting and redid some method 
 *							javadocs.
 *							add getDesktop()
 *							modify actionPerformed(), run(), 
 *							showLoginScreen(), windowIconified()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	01/28/2004	Return the RTSMediator so we can find out 
 *							if there are any controllers active.
 *							add getMediator()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/17/2004	Reformat to match updated Java Standards
 * 							defect 6445 Ver 5.1.6
 * Min Wang		04/20/2004  Fixed losing focus in production mode, 
 *							if the user grabs the title bar of the 
 *							caDesktop, the caDesktop can lose focus under 
 *							certain situations. This continues even if 
 *							the user attempts to realign the caDesktop 
 *							to its original postion.
 *                          modify windowIconified()
 * 							defect 7023 Ver 5.1.6
 * Min Wang		04/30/2004	Found that the "uncaught nullpointer" was
 *							coming from WaitCursorEventQueue.  By
 *							removing the call to this class, we avoid
 *							the nullpouinter.  This class was used to
 *							turn on the wait cursor.  Mediator also
 *							attempts to turn on the wait cursor.  Will
 *							review later to make sure wait cursor is
 *							handled correctly.
 *							modify run()
 *							defect 6416 Ver 5.1.6
 * Ray Rowehl	05/28/2004	Add a new boolean to keep track of when
 *							the DeskTop is in the foreground.  This
 *							uses the WindowListener to track.
 *							add cbDeskTopInForeGround,
 *							isDeskTopInForeGround(),
 * 							setDeskTopInForeGround()
 *							modify windowActivated(),
 *							windowDeactivated
 *							defect 7057 Ver 5.1.6 Fix 1
 * Ray Rowehl	02/05/2005	Change import for Transaction
 * 							modify import
 * 							modify isDBReady()
 * 							defect 7705 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7899  Ver 5.2.3
 * K Harrell	07/12/2005	Remove reference to Quick Counter
 * 							modify actionPerformed() 
 * 							defect 7878 Ver 5.2.3   
 * B Hargrove	08/23/2005	Java 1.4 code changes. Format, Hungarian
 * 							notation, change strings to constants,
 * 							bring code to standards. 
 * 							defect 7885 Ver 5.2.3
 * K Harrell	08/28/2005	Modified reference to 
 * 							Transaction.svCacheQueue to use 
 * 							getCacheQueue()
 * 							modify isDBReady()
 * 							defect 7885 Ver 5.2.3 
 * Jeff S.		09/14/2005	Add the call to remove the splash screen
 * 							before the desktop is set to visible.
 * 							modify run()
 * 							defect 8372 Ver 5.2.3
 * Ray Rowehl	10/13/2005	Kill Inactivity properly.
 * 							modify finalize()
 * 							defect 8405 Ver 5.2.3
 * K Harrell	10/20/2005	modify calls to displayError((JFrame) null)
 * 							to displayError()
 * 							modify actionPerformed()
 * 							defect 8270 Ver 5.2.3
 * K Harrell	10/24/2005	Do not repaint menu for changed server/db
 * 							status if Pending Transaction Screen visible
 * 							Removed parameter for isDBReady() in call to
 * 							 Desktop.repaintMenuItems()
 * 							add repaintMenuIfNotPending() 
 * 							modify handleCommEvent()
 * 							rename cbDBStatus to cbDBUp
 * 							defect 8337 Ver 5.2.3    
 * Jeff S.		12/12/2005	When Desktop is activated and the pending
 * 							panel is not visible we need to put focus
 * 							on the Desktop menu bar.  This fixed the 
 * 							problem with the hot keys not working.
 * 							modify windowActivated()
 * 							defect 7885 Ver 5.2.3
 * K Harrell	12/16/2005	Test if button enabled before requestFocus().
 * 							modify windowActivated()
 * 							defect 7885 Ver 5.2.3 
 * Jeff S.		01/10/2006	Corrected 12/16/2005 change to allow hot 
 * 							keys to work.
 * 							modify windowActivated()
 * 							defect 7885 Ver 5.2.3
 * Jeff S.		06/28/2006	Added a method used to call a native method 
 * 							to fix the focus problem with the JRE.
 * 							Sun Internal Review ID (704517).
 * 							modify windowActivated()
 * 							defect 8756 Ver 5.2.3
 * J Ralph		09/19/2006	Force a best effort garbage collection for
 * 							memory leak issues.
 * 							modify windowActivated()
 * 							defect 8844 Ver 5.2.5
 * Jeff S.		10/12/2006	Add the ability to launch IE from the 
 * 							desktop. Task 42 - Launch Exempt agency list
 * 							add LAUNCH
 * 							modify actionPerformed()
 * 							defect 8900 Ver Exempts
 * Jeff S.		10/16/2006	Cleanup constant.
 * 							remove LAUNCH
 * 							defect 8900 Ver Exempts
 * Jeff S.		04/02/2007	Added call to reset the message count so 
 * 							that when a new user logs in they will be
 * 							notified of unread messagess.
 * 							modify showLoginScreen()
 * 							defect 7768 Ver Broadcast Message
 * R Pilon		03/29/2012	Remove the splash screen when RTS comes up.
 * 							modify run()
 * 							defect 11317 Ver 7.0.0
 * Mark R.		04/02/2012	Remove application timeout
 * 							modify actionperformed(), finalized() and
 * 							run().
 * 							defect 11323 Ver POS 7.0
 * R Pilon		04/11/2012	Modify logic to handle data base up event 
 * 							  from SendCache.
 * 							modify handleCommEvent(), isDBReady()
 * 							add isTransCacheEmpty()
 * 							defect 11073 Ver 7.0.0
 * Mark R.		04/17/2012	Allow minimize for Desktop
 * 							modify windowIconified()
 * 							defect 11335 Ver 7.0.0
 * R Pilon		05/14/2012	Surround fireThreadEvent with try/catch and exit
 * 							  when exception caught.
 * 							modify handleCommEvent()
 * 							defect 11073 Ver 7.0.0
 * R Pilon		05/24/2012	Check that caBarcodeScanner is not null before 
 * 							  calling close().
 * 							modify finalize()
 * 							defect 11071 Ver 7.0.0
 * ---------------------------------------------------------------------
 */

/**
 * The main thread for the gui application.  It is responsible for 
 * starting the RTSDesktop and handling all events off the caDesktop, 
 * which it then passes to the RTSMediator.	
 *
 * @version POS_700				05/14/2012
 * @author  Michael Abernethy
 * <br>Creation Date: 			11/28/2001 12:06:58 
 */

public class RTSApplicationController
	extends ThreadMessenger
	implements ActionListener, Runnable, WindowListener, CommListener
{
	
	// boolean
	private boolean cbDeskTopInForeGround = false;
	private boolean cbWorking;

	// boolean - static 	
	private static boolean cbServerPlus;
	private static boolean cbDBUp = false;
	//private static boolean cbServerUp = true;

	// Object
	private BarCodeScanner caBarcodeScanner;
	// defect 11323
	//private Inactivity caInactivity;
	// end defect 11323
	private RTSDeskTop caDesktop;
	private RTSMediator caRTSMediator;
	private RTSException ceBarcodeException;

	//Constants
	private final static String ADDR_CHG =
		"Address Change/Print Renewal";
	private final static String DUP_RCPT = "Duplicate Receipt";
	private final static String EXCHANGE = "Exchange";
	private final static String LOGOFF = "Log Off";
	private final static String MODIFY = "Modify";
	private final static String REG_TTL_SYSTEM =
		"Registration and Title System";
	private final static String RENEWAL = "Renewal";
	private final static String REPL = "Replacement";
	private final static String RTS = "RTS: ";
	private final static String RTS_REGIS = "RTS: Registration ";
	private final static String SUBCON_RENEW = "Subcontractor Renewal";
	private final static String WINDOW_ICONIFIED = "Window Iconified";

	/**
	 * Creates the RTSApplicationController
	 */
	public RTSApplicationController()
	{
		super();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param ActionEvent aaAE
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (cbWorking)
		{
			return;
		}
		else
		{
			cbWorking = true;
		}
		try
		{
			// defect 11323
			//Inactivity.stopTimer();
			// end defect 11323
			caRTSMediator.cleanseStacks();
			String lsCommand = aaAE.getActionCommand();
			Object laData = null;
			// Handle events from the PendingTransPanel specifically
			if (aaAE.getSource() instanceof PendingTransPanel)
			{
				laData =
					((PendingTransPanel) aaAE.getSource()).getData();
				lsCommand =
					lsCommand.substring(
						lsCommand.indexOf(
							PendingTransPanel.PENDING_MARKER)
							+ PendingTransPanel.PENDING_MARKER.length(),
						lsCommand.length());
			}
			// Change the title on the RTSDesktop with some exceptions
			// defect 8900
			// Task 42 - Added check for if we are launching the 
			// browser.  We do not want to change the title of the 
			// desktop.
			if (aaAE.getSource() instanceof JMenuItem
				&& lsCommand.indexOf(
					ProjectDictionary.LAUNCH + RTSDeskTop.DELIM)
					== -1)
			{
			// end defect 8900
				JMenuItem laMenuItem = (JMenuItem) aaAE.getSource();
				String lsNewTitle = laMenuItem.getText();
				if (lsNewTitle.equals(RENEWAL)
					|| lsNewTitle.equals(DUP_RCPT)
					|| lsNewTitle.equals(EXCHANGE)
					|| lsNewTitle.equals(REPL)
					|| lsNewTitle.equals(SUBCON_RENEW)
					|| lsNewTitle.equals(ADDR_CHG))
				{
					caDesktop.setTitle(
						RTS_REGIS + laMenuItem.getText());
				}
				else if (lsNewTitle.equals(MODIFY))
				{
					caDesktop.setTitle(REG_TTL_SYSTEM);
				}
				// defect 6445
				// set the title as Registration and Title System
				else if (lsNewTitle.equals(LOGOFF))
				{
					caDesktop.setTitle(REG_TTL_SYSTEM);
				}
				// end defect 6445
				else
				{
					caDesktop.setTitle(RTS + laMenuItem.getText());
				}
			}
			// Get the transcode from the menu if it exists
			String lsControllerName = CommonConstant.STR_SPACE_EMPTY;
			String lsTransCode = CommonConstant.STR_SPACE_EMPTY;
			if (lsCommand.indexOf(RTSDeskTop.DELIM) > -1)
			{
				lsControllerName =
					lsCommand.substring(
						0,
						lsCommand.indexOf(RTSDeskTop.DELIM));
				lsTransCode =
					lsCommand.substring(
						lsCommand.indexOf(RTSDeskTop.DELIM) + 1,
						lsCommand.length());
			}
			else
			{
				lsControllerName = lsCommand;
				lsTransCode = CommonConstant.STR_SPACE_EMPTY;
			}
			// defect 6445
			// logoff or reboot if that menu item has been selected
			if (lsCommand.equals(ScreenConstant.LOGOFF))
			{
				// clear the menu bar so the next user can not do 
				// anything until login
				caDesktop.setData(new SecurityData());
				// Enable logon bar so user can logon later.
				caDesktop.getmenuItemLogon().setEnabled(true);
				// do the Windows Logout
				UtilityMethods.doWindowsLogout(this);
			}
			// defect 8900
			// Task 42 - Used to launch the Exempt Agency List
			// Launching html or pdf page from menu item.
			else if (lsControllerName.equals(ProjectDictionary.LAUNCH))
			{
				RTSHelp.displayPage(lsTransCode);
			}
			// end defect 8900
			// This section was commented out after review with VTR.
			// We will use cad to invoke reboot.
			//else if (command.equals(com.txdot.isd.rts.services.util.
			//				constants.ScreenConstant.REBOOT))
			//{
			//	// reboot the workstation
			//	isWorking = false;
			//	UtilityMethods.doReboot();
			//}
			else
			{
				Vector lvPreviousControllers = new Vector(10);
				// Create the desired VC and pass control of the 
				// application to the RTSMediator
				Class laTemp = Class.forName(lsControllerName);
				AbstractViewController laController =
					(AbstractViewController) laTemp.newInstance();
				caRTSMediator.setCurrentController(
					laController,
					lvPreviousControllers,
					lsTransCode,
					laData);
			}
			// end defect 6445
		}
		catch (ClassNotFoundException aeCNFEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeCNFEx);
			// defect 8270
			leRTSEx.displayError();
			// end defect 8270 
			// defect 4526
			// reset caDesktop on java errors
			caDesktop.reset();
			// end defect 4526
		}
		catch (InstantiationException aeIEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeIEx);
			// defect 8270 
			leRTSEx.displayError();
			// end defect 8270 
			// defect 4526
			// reset caDesktop on java errors
			caDesktop.reset();
			// end defect 4526
		}
		catch (IllegalAccessException aeIAEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeIAEx);
			// defect 8270	
			leRTSEx.displayError();
			// end defect 8270
			// defect 4526
			// reset caDesktop on java errors
			caDesktop.reset();
			// end defect 4526
		}
		// defect 6445
		// catch link error related to dll and logout.
		catch (UnsatisfiedLinkError aeULEEx)
		{
			RTSException leRTSEx = new RTSException(388);
			// defect 8270 
			leRTSEx.displayError();
			// end defect 8270  
			// exit after showing the error
			UtilityMethods.doReboot();
		}
		// end defect 6445
		finally
		{
			cbWorking = false;
		}
	}
	/**
	 * Determine server plus
	 */
	private void determineServerPlus()
	{
		try
		{
			int liOfcIssuanceNo = SystemProperty.getOfficeIssuanceNo();
			int liSubStaId = SystemProperty.getSubStationId();
			MiscellaneousData laMiscData =
				MiscellaneousCache.getMisc(liOfcIssuanceNo, liSubStaId);
			if (laMiscData.getServerPlusIndi() != 0)
			{
				cbServerPlus = true;
			}
			else
			{
				cbServerPlus = false;
			}
		}
		catch (RTSException aeRTSEx)
		{
			cbServerPlus = false;
		}
	}
	/**
	 * Frees memory
	 */
	public void finalize()
	{
		// defect 11071
		if (caBarcodeScanner != null)
		{
			caBarcodeScanner.close();
		}
		// end defect 11071
		
		// defect 8405 
		// set Barcode scanner to null
		caBarcodeScanner = null;
		// defect 11323
		// kill Inactivity
		// getInactivity().kill();
		// caInactivity = null;
		// end defect 8405
		// end defect 11323

		caDesktop.setVisible(false);
		caDesktop.dispose();
		caDesktop = null;
		caRTSMediator = null;
	}
	/**
	 * Returns the graph of the BarCodeScanner in this application
	 * 
	 * @return BarCodeScanner
	 * @throws RTSException
	 */
	public BarCodeScanner getBarCodeScanner() throws RTSException
	{
		if (caBarcodeScanner == null)
		{
			throw ceBarcodeException;
		}
		return caBarcodeScanner;
	}
	/**
	 * Get Desktop.
	 * 
	 * @return RTSDeskTop
	 */
	public RTSDeskTop getDesktop()
	{
		return caDesktop;
	}
	/**
	 * Returns the graph of the Inactivity thread
	 * 
	 * @return Inactivity
	 */
	// defect 11323
	//public Inactivity getInactivity()
	//{
	//	return caInactivity;
	//}
	// end defect 11323
	/**
	 * Returns the RTSMediator.
	 *  
	 * @return RTSMediator
	 */
	public RTSMediator getMediator()
	{
		return caRTSMediator;
	}
	/**
	 * Handles CommEvents sent by the Comm layer or SendCache.
	 * 
	 * @param CommEvent aaCE
	 */
	public void handleCommEvent(
		com.txdot.isd.rts.services.util.event.CommEvent aaCE)
	{
		switch (aaCE.getCommEvent())
		{
			// defect 8337
			// Never used 
			//	case CommEvent.SERVER_UP :
			//		{
			//			setServerUp(true);
			//			if (caDesktop != null)
			//			{
			//				caDesktop.setRcdRetrievalIconStatus(true);
			//				repaintMenuIfNotPending();
			//			}
			//			break;
			//		}
			case CommEvent.SERVER_DOWN :
				{
					if (caDesktop != null)
					{
						caDesktop.setRcdRetrievalIconStatus(false);

						// If !isDBUP(), SendCache has already started; 
						if (isDBUp())
						{
							caDesktop.setDataServerIconStatus(false);
							setDBUp(false);
							// defect 11073
							// TODO - ADD NEW LOGIC TO GET THE HOSTNAME FROM 
							// SOMEWHERE....SHOULD NOT BE HARDCODED AS localhost!!!
							try
							{
								fireThreadEvent(new ThreadEvent(
										ThreadEvent.SEND_CACHE), "localhost",
										SystemProperty.getRemoteListenerPortBE());
							}
							catch (RTSException leRTSEx)
							{
								// Unable to connect to ServerSocket for 
								// RTSMainBE, exiting application
								Log.write(Log.START_END, this, 
										"Unable to connect to RTSMainBE to send "
										+ "ThreadEvent.SERVER_DOWN event.  " 
										+ "Performing System.exit(0)");
								leRTSEx.printStackTrace();
								// TODO - WANT TO GIVE THE USER A POPUP WITH THE 'SYSTEM ERROR' 
								// MSG....WHAT IS THE BEST SOLUTION FOR THIS ONE?
								RTSException leRTSEx2 = new RTSException(
										RTSException.SYSTEM_ERROR,
										"A System Error has occurred. \n\nPlease " +
										"contact your System Administrator.",
										"System Error");
								leRTSEx2.displayError();
								System.exit(0);
							}
							// end defect 11073
							repaintMenuIfNotPending();
						}

					}
					break;
				}
			case CommEvent.MF_DOWN :
				{
					if (caDesktop != null)
					{
						caDesktop.setRcdRetrievalIconStatus(false);
					}
					break;
				}
			case CommEvent.MF_UP :
				{
					if (caDesktop != null)
					{
						caDesktop.setRcdRetrievalIconStatus(true);
					}
					break;
				}
			case CommEvent.DB_DOWN :
				{
					// If !isDBUP(), SendCache has already started; 
					if (isDBUp())
					{
						setDBUp(false);
						if (caDesktop != null)
						{
							repaintMenuIfNotPending();
							caDesktop.setDataServerIconStatus(false);
							// defect 11073
							// TODO - ADD NEW LOGIC TO GET THE HOSTNAME FROM 
							// SOMEWHERE....SHOULD NOT BE HARDCODED AS localhost!!!
							try
							{
								fireThreadEvent(
										new ThreadEvent(ThreadEvent.SEND_CACHE),
										"localhost", SystemProperty
										.getRemoteListenerPortBE());
							}
							catch (RTSException leRTSEx)
							{
								// Unable to connect to ServerSocket for 
								// RTSMainBE, exiting application
								// TODO - HOW SHOULD THIS BE LOGGED
								System.out.println("Connect refused from " +
										"RTSMainBE.  Exiting application.");
								leRTSEx.displayError();
								System.exit(0);
							}
							// end defect 11073
						}
					}
					break;
				}
			case CommEvent.DB_UP :
				{
					setDBUp(true);
					// defect 11073
					if (isTransCacheEmpty())
					{
						if (caDesktop != null)
						{
							repaintMenuIfNotPending();	
							caDesktop.setDataServerIconStatus(isDBReady());
						}
					}
					else
					{
						// TODO - HOW SHOULD THIS BE LOGGED...OR SHOULD IT 
						// BE LOGGED
						System.out.println("Transaction cache queue contains "
								+ "transactions.  Clearing Transaction cache "
								+ "and firing thread event to wake SendCache.");
						Transaction.getCacheQueue().clear();
						// TODO - ADD NEW LOGIC TO GET THE HOSTNAME FROM 
						// SOMEWHERE....SHOULD NOT BE HARDCODED AS localhost!!!
						try
						{
							fireThreadEvent(
									new ThreadEvent(ThreadEvent.SEND_CACHE),
									"localhost", SystemProperty
									.getRemoteListenerPortBE());
						}
						catch (RTSException leRTSEx)
						{
							// Unable to connect to ServerSocket for RTSMainBE, 
							// exiting application
							// TODO - HOW SHOULD THIS BE LOGGED
							System.out.println("Connect refused from " +
									"RTSMainBE.  Exiting application.");
							leRTSEx.displayError();
							System.exit(0);
						}
					}
					// end defect 11073
					break;
				}
		}
		// end defect 8337
	}
	/**
	 * Returns whether the DB connection is up && 
	 * Transaction.getCacheQueue().size() = 0
	 * 
	 * @return boolean
	 */
	public static boolean isDBReady()
	{
		// defect 7885 
		// defect 11073
		return (isDBUp() && isTransCacheEmpty());
		// end defect 11073
		// end defect 7885 

	}
	/**
	 * Returns whether the DB connection is up or not
	 * 
	 * @return boolean
	 */
	public static boolean isDBUp()
	{
		return cbDBUp;
	}
	/**
	 * Returns whether the DeskTop is in the foreground or not.
	 * 
	 * @return boolean
	 */
	public boolean isDeskTopInForeGround()
	{
		return cbDeskTopInForeGround;
	}
	/**
	 * Check to see if PendingTransPanel is visible.
	 * 
	 * @return boolean
	 */
	public boolean isPendingTransVisible()
	{
		return caDesktop.getPendingTransPanel().isVisible();
	}
	/**
	 * Returns whether the computer is a Server Plus
	 * 
	 * @return boolean
	 */
	public static boolean isServerPlus()
	{
		return cbServerPlus;
	}

	/**
	 * Determine if the Transaction's svCacheQueue Vector contains any
	 * transaction still in the queue.
	 * 
	 * @return boolean
	 */
	private static boolean isTransCacheEmpty()
	{
		return Transaction.getCacheQueue().size() == 0 ? true : false;
	}

	/**
	 * Starts Application Controller in stand alone mode.
	 *  
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		RTSApplicationController laRTS = new RTSApplicationController();
		Thread laT = new Thread(laRTS);
		laT.start();
	}
	/**
	 * This is the main startup for the client side of RTS POS.
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
		//Determine whether this computer is ServerPlus
		determineServerPlus();

		// defect 6416
		// do not call this class anymore.  it caused nullpointers.
		//Code added by RS Chandel for changing cursor to hour glass
		//EventQueue waitQueue = new WaitCursorEventQueue(100);
		//RSC commenting only for testing. Should be uncommented.
		//Toolkit.getDefaultToolkit().getSystemEventQueue().push(
		//	waitQueue);
		// code change ends
		// end defect 6416

		//Code added by RS Chandel for changing the Font
		setUIFont();
		caDesktop = new RTSDeskTop();
		caDesktop.setData(new SecurityData());
		caDesktop.addActionListener(this);
		caDesktop.addWindowListener(this);

		// defect 8372
		// set the splash screen to not visible before 
		// showing the desktop
		// defect 11317
//		RTSMain.showSplashScreen(false);
		RTSMainGUI.showSplashScreen(false);
		// end defect 11317
		// end defect 8372

		caDesktop.setVisible(true);
		caDesktop.getPendingTransPanel().addActionListener(this);
		com.txdot.isd.rts.services.communication.Comm.addCommListener(
			this);
		try
		{
			caBarcodeScanner = new BarCodeScanner();
		}
		catch (RTSException aeRTSEx)
		{
			ceBarcodeException = aeRTSEx;
		}
		Thread laBCSThread = new Thread(caBarcodeScanner);
		laBCSThread.setDaemon(true);
		laBCSThread.setPriority(Thread.MAX_PRIORITY);
		laBCSThread.start();
		caRTSMediator = new RTSMediator(this, caDesktop);

		// While SendCache is processing, don't allow the user to login
		while (com
			.txdot
			.isd
			.rts
			.client
			.systemcontrolbatch
			.business
			.SendCache
			.isProcessing())
		{
			continue;
		}
		// defect 11323
		// defect 6445
		// start caInactivity before showing Disclaimer on Windows
		//caInactivity = new Inactivity(this);
		//Thread laT = new Thread(caInactivity);
		//laT.start();
		//caInactivity.startTimer();
		// end defect 11323
		// Show disclaimer screen	
		   showLoginScreen();
		// end defect 6445
		// Sit and wait until notified to stop by RTSMain
		synchronized (this)
		{
			try
			{
				this.wait();
			}
			catch (InterruptedException aeIEx)
			{
				// just end if this exception occurs.
			}
		}
	}
	/**
	 * Set the status for the DB connection
	 * 
	 * @param abDBUp boolean
	 */
	public static void setDBUp(boolean abDBUp)
	{
		cbDBUp = abDBUp;
	}

	/**
	 * 
	 * Repaints Menu according to isPendingTransVisible() 
	 *  
	 */
	private void repaintMenuIfNotPending()
	{
		if (!isPendingTransVisible())
		{
			caDesktop.repaintMenuItems(
				caDesktop.getSecurityData(),
				isDBReady());
		}
	}
	/**
	 * Sets the foreground boolean.
	 * 
	 * @param abDeskTopInForeGround boolean 
	 */
	public void setDeskTopInForeGround(boolean abDeskTopInForeGround)
	{
		cbDeskTopInForeGround = abDeskTopInForeGround;
	}
	/**
	 * Set the UI Font from SystemProperty
	 */
	public static void setUIFont()
	{
		//
		// sets the default font for all Swing components.
		// ex. 
		// new javax.swing.plaf.FontUIResource("Serif",Font.ITALIC,12));
		// This method sets only font and does not change style or size.
		// It uses the default style and size.
		javax.swing.plaf.FontUIResource laF = null;
		javax.swing.plaf.FontUIResource laLF = null;
		if ((SystemProperty.getFontName() != null
			&& !SystemProperty.getFontName().equals(
				CommonConstant.STR_SPACE_EMPTY))
			&& (SystemProperty.getFontSize() != null
				&& !SystemProperty.getFontSize().equals(
					CommonConstant.STR_SPACE_EMPTY)))
		{
			String lsFontName = SystemProperty.getFontName();
			// defect 7885 
			//int liFontSize =
			//	Integer.parseInt(SystemProperty.getFontSize());
			// end defect 7885 

			java.util.Enumeration laKeys =
				UIManager.getDefaults().keys();
			while (laKeys.hasMoreElements())
			{
				Object laKey = laKeys.nextElement();
				Object laValue = UIManager.get(laKey);
				if (laValue instanceof javax.swing.plaf.FontUIResource)
				{
					laLF = (javax.swing.plaf.FontUIResource) laValue;
					int liSize = laLF.getSize();
					int liStyle = laLF.getStyle();
					laF =
						new javax.swing.plaf.FontUIResource(
							lsFontName,
							liStyle,
							liSize);
					UIManager.put(laKey, laF);
				}
			}
		}
	}

	/**
	 * Make the Logon screen pop up
	 */
	public void showLoginScreen()
	{
		if (caDesktop != null)
		{
			// do not do repaint on Windows
			//caDesktop.repaint();
			// defect 6445
			// force menu items to be disabled.
			//caDesktop.repaintMenuItems(null, isDBReady());
			caDesktop.setData(new SecurityData());
			com
				.txdot
				.isd
				.rts
				.services
				.util
				.SystemProperty
				.setCurrentEmpId(
				CommonConstant.STR_SPACE_EMPTY);
			// defect 7768
			// Reset the message count back to 0 so that a new
			// user will be notified that there are unread messages.
			MessageHandler.resetForNewUser();
			// end defect 7768
			cbWorking = false;
			// end defect 6445
			actionPerformed(
				new ActionEvent(
					this,
					ActionEvent.ACTION_PERFORMED,
					ScreenConstant.SEC001));
		}
	}
	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 * 
	 * @param WindowEvent aaWE
	 */
	public void windowActivated(java.awt.event.WindowEvent aaWE)
	{
		// defect 7057
		// mark that DeskTop is active
		setDeskTopInForeGround(true);
		// end defect 7057

		caDesktop.repaint();	
		
		// defect 8756
		JniAdInterface.focusFix();
		// end defect 8756
		
		if (caDesktop.getPendingTransPanel().isVisible())
		{
			if (caDesktop
				.getPendingTransPanel()
				.getEnterButton()
				.isEnabled())
			{
				caDesktop
					.getPendingTransPanel()
					.getEnterButton()
					.requestFocus();
			}
			else if (
				caDesktop
					.getPendingTransPanel()
					.getCancelButton()
					.isEnabled())
			{
				caDesktop
					.getPendingTransPanel()
					.getCancelButton()
					.requestFocus();
			}
			else if (
				caDesktop
					.getPendingTransPanel()
					.getSetAsideButton()
					.isEnabled())
			{
				caDesktop
					.getPendingTransPanel()
					.getSetAsideButton()
					.requestFocus();
			}
// Needs to be the else of is caDesktop.getPendingTransPanel().isVisible()
//			// defect 7885
//			// This was added b/c focus was getting lost and causing hot
//			// keys to not work.
//			else
//			{
//				caDesktop.requestFocusMenuBar();
//			}
//			// end defect 7885
		}
		// defect 7885
		// This was added b/c focus was getting lost and causing hot
		// keys to not work.
		else
		{
			caDesktop.requestFocusMenuBar();
			// defect 8844
			// Force a full best effort garbage collection to reclaim memory
			// allocated by discarded/unused objects for quick reuse.
			// System.gc() needs to be run within the else statement b/c
			// a screen flicker can occur on the Pending Trans Panel.
			System.gc();
			// end defect 8844
		}
		// end defect 7885
	}
	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 * 
	 * @param aaWE java.awt.event.WindowEvent
	 */
	public void windowClosed(java.awt.event.WindowEvent aaWE)
	{
	}
	/**
	 * Invoked when the user attempts to close the window
	 * from the window's system menu.  If the program does not 
	 * explicitly hide or dispose the window while processing 
	 * this event, the window close operation will be cancelled.
	 * <p>
	 * Window Close is disabled in DeskTop.
	 * 
	 * @param aaWE java.awt.event.WindowEvent
	 */
	public void windowClosing(java.awt.event.WindowEvent aaWE)
	{
		//caInactivity.kill();
		//if (caBarcodeScanner != null)
		//	caBarcodeScanner.close();
		//caDesktop.setVisible(false);
		//caDesktop.dispose();
		//caDesktop = null;
		//caRTSMediator = null;
	}
	/**
	 * Invoked when a window is no longer the user's active
	 * window, which means that keyboard events will no longer
	 * be delivered to the window or its subcomponents.
	 * 
	 * @param aaWE java.awt.event.WindowEvent
	 */
	public void windowDeactivated(java.awt.event.WindowEvent aaWE)
	{
		// defect 7057
		// mark that DeskTop is active
		setDeskTopInForeGround(false);
		// end defect 7057
	}
	/**
	 * Invoked when a window is changed from a minimized
	 * to a normal state.
	 * 
	 * @param aaWE java.awt.event.WindowEvent
	 */
	public void windowDeiconified(java.awt.event.WindowEvent aaWE)
	{
	}
	/**
	 * Invoked when a window is changed from a normal to a
	 * minimized state. For many platforms, a minimized window 
	 * is displayed as the icon specified in the window's 
	 * iconImage property.
	 * <p>
	 * When attempting to minimize the application in production,
	 * set the frame state back to normal.  
	 * This has the affect of the window minimizing and then
	 * popping right back up again.
	 * <p>
	 * @see Frame#setIconImage
	 * 
	 * @param aaWE java.awt.event.WindowEvent
	 */
	public void windowIconified(java.awt.event.WindowEvent aaWE)
	{
		// defect 6445
		// do not allow user to keep application iconized if in 
		// production
		// defect 11335
		//if (SystemProperty.getProdStatus() == 0)
		//{
			//defect 7023
			//caDesktop.setLocation(0, 0);
			//caDesktop.setState(Frame.NORMAL);
			//caDesktop.requestFocus();
			//end defect 7023
		//}
		//else
		//{
			System.out.println(WINDOW_ICONIFIED);
		//}
		// end defect 6445
		// end defect 11335
	}
	/**
	 * Invoked the first time a window is made visible.
	 * 
	 * @param aaWE java.awt.event.WindowEvent
	 */
	public void windowOpened(java.awt.event.WindowEvent aaWE)
	{
		// empty code block
	}
}
