package com.txdot.isd.rts.client.general.ui;

import java.awt.Component;
import java.util.HashMap;
import java.util.Vector;
// defect 11323
//import com.txdot.isd.rts.client.desktop.Inactivity;
// end defect 11323
import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com.txdot.isd.rts.client.desktop.RTSDeskTop;
import com.txdot.isd.rts.client.general.business.BusinessInterface;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * 
 * RTSMediator.java
 *
 * (c) Texas Department of Transportation   2001 
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		03/02/2002  Fixed controller tear down 
 *							in processData()
 * J Kwik		03/29/2002  Get parent when displaying error msg for 
 *							transaction error per Chandel.
 * N Ting		04/17/2002	defect 3529
 * MAbs			04/18/2002	Wait cursor code added
 * MAbs			05/6/2002	Started inactivity timer after login 
 *							defect 3781
 * MAbs			05/10/2002	defect 3781
 * MAbs			06/26/2002	defect 4369
 * Ray Rowehl	07/27/2002	defect 4526 - Reset desktop and cleanup 
 *							when catching java
 *							errors in processData.
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * Min Wang		11/08/2002	Modified ProcessData() to handle hung up 
 *							when server is down. CQU100004973.
 * S Govindappa 02/04/2002  Fixed defect # 5392. Made changes in 
 *							processData(..) to remove the changes done 
 *							for fixing defect# 4973 in server down mode.
 * Min Wang		02/13/2003	Modified ProcessData() to handle hung up 
 *							when server is down for Inventory. 
 *							defect 4973
 * K Harrell    03/17/2003  processData, only call Transaction.reset 
 * 							if not from Payment Screen
 *							defect 5807  
 * Ray Rowehl	01/28/2004	Return a boolean indicating if there is an 
 *							active controller.
 *							Also formatted code and added descriptive 
 *							names to methods. 
 *							add hasActiveEvent()
 *							defect 6445  Ver 5.1.6
 * Min Wang		02/03/2004	If it is Windows Platform, do not set the 
 *							focus on the previous frame's enter button.
 *							modify setFocus()
 *							defect 6847  Ver 5.1.6
 * Ray Rowehl	02/04/2004	Allow hasActiveEvent to return false if
 *							we are on Disclaimer frame
 *							modify hasActiveEvent()
 *							defect 6445  Ver 5.1.6
 * Ray Rowehl	02/17/2004	Reformat to match updated Java Standards
 * 							defect 6445 Ver 5.1.6
 * Ray Rowehl	03/04/2004	Update to turn on timer more appropriately
 *							modify processData()
 *							defect 6445 Ver 5.1.6
 * Min Wang     03/11/2004  Set the focus on the previous frame's enter
 *                          button in case of cancel when enter button 
 *                          is enabled and it is not subcon.
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							add hardcancel(). 
 *							Ver 5.2.0
 * Ray Rowehl	04/20/2004	Add getController so we can do clean up from
 *							logout.
 *							Also change how timer is set on when 
 *							returning to DeskTop.
 *							merged into 5.2.0
 *							add getController()
 *							modify hasActiveEvent(), processData()
 *							defect 7009 Ver 5.2.0
 * Min Wang		05/12/2004	The cursor will be set on the active gui 
 *							component.
 *							add setWaitCursor()
 *							modify processData()
 *							defect 7053 Ver 5.1.6 Fix 1
 * Ray Rowehl	05/28/2004	Do not allow the timeout if the DeskTop is
 *							not in the ForeGround.
 *							modify hasActiveEvent()
 *							defect 7057 Ver 5.1.6 Fix 1
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify processData()
 * 							defect 7705 Ver 5.2.3
 * T Pederson	02/11/2005	Change setVisible to setVisibleRTS for 
 * 							Java 1.4.
 * 							modify processData()
 * 							defect 7701 Ver 5.2.3
 * Ray Rowehl	03/19/2005	Use getFrame() to get to the frame fromm 
 * 							the controller
 * 							modify setWaitCursor()
 * 							defect 8018 Ver 5.2.3
 * B Hargrove	08/19/2005	Java 1.4 code changes. Format,
 * 							change strings to constants, etc. 
 * 							defect 7885 Ver 5.2.3 
 * Ray Rowehl	08/30/2005	Change logging on check for active event 
 * 							to reduce the amount of normal logging.
 * 							modify hasActiveEvent()
 * 							defect 8341 Ver 5.2.3
 * K Harrell	11/16/2005	Do not issue reset() when catch 
 * 							exception from FRM CUS002 (Set Aside)
 * 							add FRM_CUS002 
 * 							modify processData() 
 * 							defect 6456 Ver 5.2.3 
 * Ray Rowehl	08/01/2006	Change cleanseStacks to clean out objects.
 * 							modify cleanseStacks()
 * 							defect 8851 Ver 5.2.4
 * JRue			08/11/2006	Move the setFocus() in processData() to end 
 * 							of if blocks.
 * Ray Rowehl				modify processData()
 * 							defect 8851 Ver 5.2.4 
 * T Pederson	09/15/2006	Added check for module and function when
 * 							DB down exception is caught in processData.
 * 							add shouldShowDBException()
 *							modify processData()
 * 							defect 8926 Ver 5.2.5
 * K Harrell	05/23/2007	Add INV_VI_UPDATE_INV_STATUS_CD_RECOVER,
 *							INV_GET_NEXT_VI_ITEM_NO,
 *							INV_VI_VALIDATE_PER_PLT to the scenarios 
 *							where dbdown
 *							modify shouldShowDBException
 *							defect 9085 Ver Special Plates
 * K Harrell	07/31/2007	ShutAllWindows on DBDown for Special Plates
 * 							Reports
 * 							modify processData(), shouldShutAllWindows()
 * 							defect 9085 Ver Special Plates
 * K Harrell	11/01/2008	ShutAllWindows on DBDown for Disabled Placard
 * 							Reports.  Show DB Exception for MiscReg.   
 * 							modify shouldShutAllWindows(),
 * 							  shouldShowDBException() 
 * 							defect 9831 Ver Defect_POS_B
 * M Reyes		3/27/2012	Remove application timeout
 * 							Modify setView(), closeallwindows()
 * 							defect 11323 Ver POS 7.0
 * ---------------------------------------------------------------------
 */

/**
 * The RTSMediator handles all screen flow in the application
 *
 * @version POS_700			03/27/2012
 * @author  Michael Abernethy
 * <br>Creation Date: 		09/12/2001 13:55:55
 */
public class RTSMediator
{
	private AbstractViewController caController;
	private BusinessInterface caBusinessInterface;
	private RTSApplicationController caRTSAppController;
	private RTSDeskTop caDesktop;
	private Object caDataObject;

	private Vector cvControllerStack;
	private Vector cvPreviousControllers;

	private String csTransCode;

	private HashMap chmVault;

	// Constants
	private final static int DEFAULT_CONTROLLER_STACK_SIZE = 10;

	private final static String ERRMSG_ACTIVE_EVENT_CALLED =
		" has Active Event was called.";
	private final static String ERRMSG_DESKTOP_NOT_FOREGROUND =
		" DeskTop was not in the ForeGround on TimeOut check";
	private final static String ERRMSG_EXIT_TO_DESKTOP =
		" Exiting back to Desktop";
	// defect 6456 
	private final static String FRM_CUS002 = "FrmSetAsideCUS002";
	// end defect 6456
	private final static String FRM_PMT001 = "FrmPaymentPMT001";

	/**
	 * Creates the RTSMediator.
	 * 
	 * @param aaRtsAppController RTSApplicationController
	 * @param aaDesktop RTSDeskTop
	 */
	public RTSMediator(
		RTSApplicationController aaRtsAppController,
		RTSDeskTop aaDesktop)
	{
		super();
		caRTSAppController = aaRtsAppController;
		caDesktop = aaDesktop;
		chmVault = new HashMap();
		cvControllerStack = new Vector(DEFAULT_CONTROLLER_STACK_SIZE);
		caBusinessInterface = new BusinessInterface();
	}

	/**
	 * Clear the cvControllerStack.
	 */
	public void cleanseStacks()
	{
		for (int i = 0; i < cvControllerStack.size(); i++)
		{
			AbstractViewController laAvc =
				(AbstractViewController) cvControllerStack.get(i);
			if (laAvc.getFrame() == null)
			{
				cvControllerStack.remove(i);
				cvPreviousControllers.remove(i);
			}
		}
		// defect 8851
		// Set variables to null
		caDataObject = null;
		caController = null;
		chmVault = null;
		chmVault = new HashMap();
		// UtilityMethods.printMemoryAndDoGC();
		// end defect 8851
	}

	/**
	 * Close all windows.
	 */
	private void closeAllWindows()
	{
		cvPreviousControllers.removeAllElements();
		caController.setAcceptingInput(false);
		caController.close();
		while (cvControllerStack.size() > CommonConstant.ELEMENT_0)
		{
			caController =
				(AbstractViewController) cvControllerStack.remove(
					cvControllerStack.size() - 1);
			caController.setAcceptingInput(false);
			caController.close();
		}
		// defect 11323
		// caRTSAppController.getInactivity().startTimer();
		// end defect 11323
	}

	/**
	 * Store the object in the vault.
	 *
	 * @param asKey String
	 * @param aaValue Object
	 * @return Object
	 */
	public Object closeVault(String asKey, Object aaValue)
	{
		return chmVault.put(asKey, UtilityMethods.copy(aaValue));
	}

	/**
	 * Set all objects to null on exit.
	 */
	protected final void finalize()
	{
		caBusinessInterface = null;
		caController = null;
		caDataObject = null;
		csTransCode = null;
		cvControllerStack.removeAllElements();
		cvPreviousControllers.removeAllElements();
		cvControllerStack = null;
		cvPreviousControllers = null;
	}

	/**
	 * return the reference to the caRtsAppController.
	 * 
	 * @return RTSApplicationController
	 */
	public RTSApplicationController getAppController()
	{
		return caRTSAppController;
	}

	/**
	 * Returns Controller.
	 *
	 * @return AbstractViewController
	 */
	public AbstractViewController getController()
	{
		return caController;
	}

	/**
	 * Return the reference to desktop.
	 * 
	 * @return RTSDeskTop
	 */
	public RTSDeskTop getDesktop()
	{
		return caDesktop;
	}

	/**
	 * Get the first controller of this event.
	 * 
	 * @return RTSDialogBox
	 */
	public RTSDialogBox getParent()
	{
		if (cvControllerStack != null)
		{
			if (cvControllerStack.size() > CommonConstant.ELEMENT_0)
			{
				int j = cvControllerStack.size() - 1;
				while (j >= 0)
				{
					AbstractViewController laAv =
						(
							AbstractViewController) cvControllerStack
								.elementAt(
							j);
					if (laAv.getFrame() != null
						&& laAv.getFrame().isVisible())
					{
						return laAv.getFrame();
					}
					j = j - 1;
				}
			}
		}
		return null;
	}

	/**
	 * Forces a cancel of a frame by removing it from the controller
	 * stack.
	 */
	public void hardCancel()
	{
		if (cvControllerStack.size() > CommonConstant.ELEMENT_0)
		{
			caController =
				(AbstractViewController) cvControllerStack.remove(
					cvControllerStack.size() - 1);
			cvPreviousControllers.remove(
				cvPreviousControllers.size() - 1);
		}
	}

	/**
	 * Returns a boolean indicating if there is an active controller.
	 * 
	 * @return  boolean
	 */
	public boolean hasActiveEvent()
	{
		boolean lbHasActiveController = false;

		// check to see if controller is active
		if (caController != null && caController.isAcceptingInput())
		{
			// If we are not showing the Disclaimer,
			// go ahead and return true.
			String lsClassName = caController.getClass().getName();
			if (!lsClassName.equalsIgnoreCase(ScreenConstant.SEC001))
			{
				lbHasActiveController = true;
			}
		}
		if (cvControllerStack.size() > 0)
		{
			// If we are not showing the Disclaimer,
			// go ahead and return true.
			String lsClassName = caController.getClass().getName();
			if (!lsClassName.equalsIgnoreCase(ScreenConstant.SEC001))
			{
				lbHasActiveController = true;
			}
		}
		// defect 8341
		// change the logging level on these messages.
		// We may want to see this in a debug situation 
		// If not in an event, check to see if the 
		// DeskTop is in the ForeGround.
		// If not, set the boolean to true since we
		// are probably in some other application.
		if (!lbHasActiveController)
		{
			if (!caRTSAppController.isDeskTopInForeGround())
			{
				Log.write(
					Log.APPLICATION,
					this,
					ERRMSG_DESKTOP_NOT_FOREGROUND);
				lbHasActiveController = true;
			}
		}
		// log that hasActiveEvent was called
		Log.write(Log.APPLICATION, this, ERRMSG_ACTIVE_EVENT_CALLED);
		// end defect 8341
		return lbHasActiveController;
	}

	/**
	 * Get the requested object out of the vault.
	 * 
	 * @param asKey String
	 * @return Object
	 */
	public Object openVault(String asKey)
	{
		return UtilityMethods.copy(chmVault.get(asKey));
	}

	/**
	 * This is the traffic cop of the application while in events.
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	public Object processData(
		int aiModuleName,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		try
		{
			if (aiFunctionId != GeneralConstant.NO_DATA_TO_BUSINESS)
			{
				try
				{
					// call method to turn on wait cursor
					setWaitCursor(true);
					caDataObject =
						caBusinessInterface.processData(
							aiModuleName,
							aiFunctionId,
							aaData);
				}
				catch (RTSException aeRTSEx)
				{
					if (caController.getFrame() == null)
					{
						caDesktop.reset();
					}
					if (aeRTSEx
						.getMsgType()
						.equals(RTSException.TR_ERROR))
					{
						// add the following per Chandel.
						RTSDialogBox laRTSDial = getParent();
						if (laRTSDial != null)
						{
							aeRTSEx.displayError(laRTSDial);
						}
						else
						{
							aeRTSEx.displayError(caDesktop);
						}
						// defect 6456 
						// added the check for !FRM_CUS002 
						// Only call 
						// Transaction.reset if not from Payment && 
						// not from Set Aside  
						if (!caController
							.getFrame()
							.getName()
							.equals(FRM_PMT001)
							&& !caController.getFrame().getName().equals(
								FRM_CUS002))
							// end defect 6456 		
						{
							com
								.txdot
								.isd
								.rts
								.services
								.common
								.Transaction
								.reset();
						}
						closeAllWindows();
						caDesktop.reset();
						return null;
					}
					else if (
						aeRTSEx.getMsgType().equals(
							RTSException.DB_DOWN))
					{
						// defect 8926
						shouldShowDBException(
							aiModuleName,
							aiFunctionId,
							aeRTSEx);
						// end defect 8926
						if (shouldShutAllWindows(aiModuleName,
							aiFunctionId))
						{
							closeAllWindows();
							caDesktop.reset();
							return null;
						}
					}
					else if (
						aeRTSEx.getMsgType().equals(
							RTSException.SERVER_DOWN))
					{
						if (shouldShutAllWindows(aiModuleName,
							aiFunctionId))
						{
							aeRTSEx.setCode(618);
							aeRTSEx.displayError(caDesktop);
							closeAllWindows();
							caDesktop.reset();
							return null;
						}
						else
						{
							throw aeRTSEx;
						}
					}
					else
					{
						throw aeRTSEx;
					}
				}
				finally
				{
					// call method to turn off wait cursor
					setWaitCursor(false);
				}
			}
			else
			{
				caDataObject = aaData;
			}
			try
			{
				if (caController.getDirectionFlow()
					== AbstractViewController.DIRECT_CALL)
				{
					return caDataObject;
				}
				else if (
					caController.getDirectionFlow()
						== AbstractViewController.DESKTOP)
				{
					// defect 11323
					// if going to pending trans, do not turn on timer.
					//if (aaData != null
					//	&& aaData
					//		instanceof com
					//			.txdot
					//			.isd
					//			.rts
					//			.services
					//			.data
					//			.CompleteTransactionData)
					//{
					//	Inactivity.stopTimer();
					//}
					//else
					//{
						// turn on the timer when going to desktop.
					//	if (caRTSAppController != null
					//		&& caRTSAppController.getInactivity() != null)
					//	{
					//		caRTSAppController
					//			.getInactivity()
					//			.startTimer();
					//	}
					//}
					// end defect 11323
					caDesktop.setData(caDataObject);
					chmVault.clear();
					caDesktop.reset();
					cvPreviousControllers.removeAllElements();
					caController.setAcceptingInput(false);
					caController.close();
					while (cvControllerStack.size()
						> CommonConstant.ELEMENT_0)
					{
						caController =
							(
								AbstractViewController) cvControllerStack
									.remove(
								cvControllerStack.size() - 1);
						caController.setAcceptingInput(false);
						caController.close();
					}
					return null;
				}
				else if (
					caController.getDirectionFlow()
						== AbstractViewController.CANCEL)
				{
					if (cvControllerStack.size()
						> CommonConstant.ELEMENT_0)
					{
						// defect 8851
						// Make sure current controller is closed before
						// we move on.
						caController.close();
						//caController = null;
						// end defect 8851
						caController =
							(
								AbstractViewController) cvControllerStack
									.remove(
								cvControllerStack.size() - 1);
						cvPreviousControllers.remove(
							cvPreviousControllers.size() - 1);
						// defect 8851
						// Move from begin of if block to end
						setFocus(cvControllerStack);
						// end defect 8851
						return null;
					}
					else
					{
						caDesktop.reset();
						// defect 11323
						// set the timer on
						// we are reseting the desktop
						//caRTSAppController.getInactivity().startTimer();
						// end defect 11323
						return null;
					}
				}
				else if (
					caController.getDirectionFlow()
						== AbstractViewController.PREVIOUS)
				{
					if (caController.getPreviousController() != null)
					{
						String lsController =
							caController.getPreviousController();
						AbstractViewController laCurrController = null;
						while (cvControllerStack.size()
							> CommonConstant.ELEMENT_0)
						{
							laCurrController =
								(
									AbstractViewController) cvControllerStack
										.get(
									cvControllerStack.size() - 1);
							Class lcClass = laCurrController.getClass();
							String lsCurrController =
								(String) lcClass.getName();
							if (lsController.equals(lsCurrController))
							{
								caController =
									(
										AbstractViewController) cvControllerStack
											.remove(
										cvControllerStack.size() - 1);
								cvPreviousControllers.remove(
									cvPreviousControllers.size() - 1);
								setCurrentController(
									caController,
									cvPreviousControllers,
									caController.getTransCode(),
									caDataObject);
								// defect 8851
								//	Move from begin of if block to end
								setFocus(cvControllerStack);
								// end defect 8851
								break;
							}
							else
							{
								laCurrController.getFrame()
								// defect 7701
								// Change setVisible to setVisibleRTS
								.setVisibleRTS(
								//.setVisible(
								// end defect 7701
								false);
								//laCurrController.setFrame(null);
								//laCurrController = null;

								// defect 8851
								laCurrController.close();
								// end defect 8851
								cvControllerStack.remove(
									cvControllerStack.size() - 1);
								cvPreviousControllers.remove(
									cvPreviousControllers.size() - 1);
							}
						}
						return null;
					}
					else
					{
						if (cvControllerStack.size()
							> CommonConstant.ELEMENT_0)
						{
							// defect 8851
							// make sure current controller is closed before
							// we move on.
							caController.close();
							//caController = null;
							// end defect 8851
							caController =
								(
									AbstractViewController) cvControllerStack
										.remove(
									cvControllerStack.size() - 1);
							cvPreviousControllers.remove(
								cvPreviousControllers.size() - 1);
							setCurrentController(
								caController,
								cvPreviousControllers,
								caController.getTransCode(),
								caDataObject);
							// defect 8851
							// Move from begin of if block to end
							setFocus(cvControllerStack);
							// end defect 8851
						}
						return null;
					}
				}
				else if (
					caController.getDirectionFlow()
						== AbstractViewController.CURRENT)
				{
					setCurrentController(
						caController,
						cvPreviousControllers,
						caController.getTransCode(),
						caDataObject);
					return null;
				}
				else if (
					caController.getDirectionFlow()
						== AbstractViewController.NEXT)
				{
					cvPreviousControllers.add(
						caController.getClass().getName());
					cvControllerStack.add(caController);
					String lsTempTransCode =
						caController.getTransCode();
					String lsNextScreen =
						caController.getNextController();
					Class laTemp = Class.forName(lsNextScreen);
					caController =
						(AbstractViewController) laTemp.newInstance();
					setCurrentController(
						caController,
						cvPreviousControllers,
						lsTempTransCode,
						caDataObject);
					return null;
				}
				else if (
					caController.getDirectionFlow()
						== AbstractViewController.FINAL)
				{
					chmVault.clear();
					// defect 11323
					// set the timer on
					// we are reseting the desktop
					//caRTSAppController.getInactivity().startTimer();
					// end defect 11323
					caDesktop.reset();
					closeAllWindows();
					return null;
				}
				return null;
			}
			catch (ClassNotFoundException aeCNFEx)
			{
				RTSException leRTSException =
					new RTSException(RTSException.JAVA_ERROR, aeCNFEx);
				if (caController.getFrame() != null
					&& caController.getFrame().isVisible())
				{
					leRTSException.displayError(
						caController.getFrame());
				}
				else
				{
					leRTSException.displayError(caDesktop);
				}
				// we are going back to desktop on the error!
				Log.write(
					Log.SQL_EXCP,
					aeCNFEx,
					ERRMSG_EXIT_TO_DESKTOP);
				// clear everything out on java errors
				// defect 7705
				// change package for Transaction
				com.txdot.isd.rts.services.common.Transaction.reset();
				// end defect 7705
				closeAllWindows();
				// defect 11323
				// set the timer on
				// we are reseting the desktop
				//caRTSAppController.getInactivity().startTimer();
				// end defect 11323
				caDesktop.reset();
			}
			catch (InstantiationException aeIEx)
			{
				RTSException leRTSException =
					new RTSException(RTSException.JAVA_ERROR, aeIEx);
				// defect 7009
				// use desktop to display error if frame is not visible
				if (caController.getFrame() != null
					&& caController.getFrame().isVisible())
				{
					leRTSException.displayError(
						caController.getFrame());
				}
				else
				{
					leRTSException.displayError(caDesktop);
				}
				// we are going back to desktop on the error!
				Log.write(Log.SQL_EXCP, aeIEx, ERRMSG_EXIT_TO_DESKTOP);
				// clear everything out on java errors
				// defect 7705
				// change package for Transaction
				com.txdot.isd.rts.services.common.Transaction.reset();
				// end defect 7705
				closeAllWindows();
				// defect 11323
				// set the timer on
				// we are reseting the desktop
				//caRTSAppController.getInactivity().startTimer();
				// end defect 11323
				caDesktop.reset();
			}
			catch (IllegalAccessException aeIAEx)
			{
				RTSException leRTSException =
					new RTSException(RTSException.JAVA_ERROR, aeIAEx);
				// defect 7009
				// use desktop to display error if frame is not visible
				if (caController.getFrame() != null
					&& caController.getFrame().isVisible())
				{
					leRTSException.displayError(
						caController.getFrame());
				}
				else
				{
					leRTSException.displayError(caDesktop);
				}
				// we are going back to desktop on the error!
				Log.write(Log.SQL_EXCP, aeIAEx, ERRMSG_EXIT_TO_DESKTOP);
				// end defect 7009
				// clear everything out on java errors
				// defect 7705 
				// change package for Transaction
				com.txdot.isd.rts.services.common.Transaction.reset();
				// end defect 7705
				closeAllWindows();
				// defect 11323
				// set the timer on
				// we are reseting the desktop
				//caRTSAppController.getInactivity().startTimer();
				// end defect 11323
				caDesktop.reset();
			}
			catch (Exception aeEx)
			{
				RTSException leRTSException =
					new RTSException(RTSException.JAVA_ERROR, aeEx);
				// defect 7009
				// use desktop to display error if frame is not visible
				if (caController.getFrame() != null
					&& caController.getFrame().isVisible())
				{
					leRTSException.displayError(
						caController.getFrame());
				}
				else
				{
					leRTSException.displayError(caDesktop);
				}
				// we are going back to desktop on the error!
				Log.write(Log.SQL_EXCP, aeEx, ERRMSG_EXIT_TO_DESKTOP);
				// end defect 7009
				// clear everything out on java errors
				// defect 7705 
				// change package for Transaction
				com.txdot.isd.rts.services.common.Transaction.reset();
				// end defect 7705
				closeAllWindows();
				// defect 11323
				// set the timer on
				// we are reseting the desktop
				//caRTSAppController.getInactivity().startTimer();
				// end defect 11323
				caDesktop.reset();
			}
			catch (Throwable aeThrowable)
			{
				// Need to follow up with appropriate RTSException 
				// changes.
				RTSException leRTSException =
					new RTSException(
						RTSException.JAVA_ERROR,
						(Exception) aeThrowable);
				// use desktop to display error if frame is not visible
				if (caController.getFrame() != null
					&& caController.getFrame().isVisible())
				{
					leRTSException.displayError(
						caController.getFrame());
				}
				else
				{
					leRTSException.displayError(caDesktop);
				}
				// we are going back to desktop on the error!
				Log.write(
					Log.SQL_EXCP,
					aeThrowable,
					ERRMSG_EXIT_TO_DESKTOP);
				// clear everything out on java errors
				com.txdot.isd.rts.services.common.Transaction.reset();
				closeAllWindows();
				// set the timer on
				// we are reseting the desktop
				// defect 11323
				//caRTSAppController.getInactivity().startTimer();
				// end defect 11323
				caDesktop.reset();
			}
		}
		finally
		{
			// empty code block
			// we just wanted to enclose the above code in a try.
		}
		return null;
	}

	/**
	 * This function is called by both RTSMediator and MenuController 
	 * and handles setting the information before instantiating the
	 * ViewController.
	 * 
	 * @param aaAVController AbstractViewController
	 * @param avPreviousControllers Vector
	 * @param asTransCode String
	 * @param aaDataObject Object
	 */
	public void setCurrentController(
		AbstractViewController aaAVController,
		Vector avPreviousControllers,
		String asTransCode,
		Object aaDataObject)
	{
		caController = aaAVController;
		caDataObject = aaDataObject;
		cvPreviousControllers = avPreviousControllers;
		csTransCode = asTransCode;
		aaAVController.setMediator(this);
		aaAVController.setView(
			avPreviousControllers,
			asTransCode,
			aaDataObject);
	}

	/**
	 * Method to set the focus on the previous frame's enter button in 
	 * case of cancel.
	 * 
	 * @param avVct Vector
	 */
	private void setFocus(Vector avVct)
	{
		if (avVct == null || avVct.size() <= CommonConstant.ELEMENT_0)
		{
			return;
		}
		RTSDialogBox laRTSDiaBox = null;
		int i = 1;
		int len = avVct.size();
		while (i <= len)
		{
			AbstractViewController lvAVC =
				(AbstractViewController) avVct.elementAt(len - i);
			i++;
			laRTSDiaBox = lvAVC.getFrame();
			if (laRTSDiaBox != null && laRTSDiaBox.isVisible())
			{
				Component laComp = laRTSDiaBox.getEnterButtonForFocus();
				// Set the focus on the previous frame's enter button 
				// in case of cancel when enter button is enabled and 
				// it is not subcon.
				if (laComp != null
					&& !(caController
						.getTransCode()
						.equals(TransCdConstant.SBRNW)))
				{
					if (laComp.isEnabled())
					{
						laComp.requestFocus();
					}
				}
				break;
			}
		}
	}

	/**
	 * This method sets up the cursor depending on the situation.
	 * The boolean determines which kind of cursor to set.
	 * The cursor will be set on the active gui component
	 * 
	 * @param aSwitch boolean
	 */
	private void setWaitCursor(boolean abSwitch)
	{
		// declare the cursor as default
		java.awt.Cursor laCursor =
			new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR);
		// set up the wait cursor if requested
		if (abSwitch)
		{
			laCursor = new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR);
		}
		// set up the component to be desktop as a default
		Component laComponent = getDesktop();
		// if current controller and frame are active,
		// use them as the basis for setting cursor
		// use getFrame()
		if (caController != null
			&& caController.getFrame() != null
			&& caController.getFrame().isVisible())
		{
			laComponent = caController.getFrame();
		}
		else
		{
			// try getting the parent to display ther cursor
			RTSDialogBox laFrame = getParent();
			if (laFrame != null)
			{
				laComponent = laFrame;
			}
		}
		// set the cursor to the screen
		laComponent.setCursor(laCursor);
		getDesktop().setCursor(laCursor);
	}

	// defect 8926
	/**
	 * Determines if the module/function should display DB down error 
	 * or throw the exception.
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aeRTSEx RTSException
	 * @throws RTSException
	 */
	private void shouldShowDBException(
		int aiModuleName,
		int aiFunctionId,
		RTSException aeRTSEx)
		throws RTSException
	{
		// defect 9831
		// Add MISCELLANEOUSREG 
		// defect 9085 
		if ((aiModuleName == GeneralConstant.TITLE
			&& aiFunctionId == TitleConstant.GET_PRIVATE_PARTY_VALUE)
			|| (aiModuleName == GeneralConstant.SPECIALPLATES
				&& (aiFunctionId
					== InventoryConstant
						.INV_VI_UPDATE_INV_STATUS_CD_RECOVER
					|| aiFunctionId
						== InventoryConstant.INV_GET_NEXT_VI_ITEM_NO
					|| aiFunctionId
						== InventoryConstant.INV_VI_VALIDATE_PER_PLT))
			|| (aiModuleName == GeneralConstant.MISCELLANEOUSREG))
		{
			// end defect 9085			
			// end defect 9831 
			throw new RTSException(618);
		}
		else
		{
			aeRTSEx.setCode(618);
			aeRTSEx.displayError(caDesktop);
		}
	}

	/**
	 * Determines if the module goes to the DB or not and whether it 
	 * should shut the window down mid-operation
	 * 
	 * @param aiModuleName int
	 * @return boolean
	 */
	private boolean shouldShutAllWindows(
		int aiModuleName,
		int aiFunctionId)
	{
		boolean lbShut = false;
		// defect 9831 
		// add MISCELLANEOUSREG / GENERATE_DSABLD_PLCRD_REPORT
		if (aiModuleName == GeneralConstant.LOCAL_OPTIONS
			|| aiModuleName == GeneralConstant.INVENTORY
			|| aiModuleName == GeneralConstant.FUNDS
			|| aiModuleName == GeneralConstant.MISC
			|| aiModuleName == GeneralConstant.REPORTS
			|| (aiModuleName == GeneralConstant.SPECIALPLATES
				&& aiFunctionId
					== SpecialPlatesConstant
						.GENERATE_SPCL_PLT_APPL_REPORT)
			|| (aiModuleName == GeneralConstant.MISCELLANEOUSREG
				&& aiFunctionId
					== MiscellaneousRegConstant
						.GENERATE_DSABLD_PLCRD_REPORT)
			|| aiModuleName == GeneralConstant.ACCOUNTING)
		{
			lbShut = true;
		}
		// end defect 9831 
		return lbShut;
	}
}
