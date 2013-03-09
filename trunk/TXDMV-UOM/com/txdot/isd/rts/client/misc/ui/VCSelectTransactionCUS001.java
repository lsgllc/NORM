package com.txdot.isd.rts.client.misc.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.TransactionHeaderData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCSelectTransactionCUS001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		09/30/2002	DTA should not be completed, should be 
 * 							canceled.
 *							Added handling in processData to show 
 *							message and then go back to desktop.
 *							defect 4757.
 * Ray Rowehl	02/08/2005	Change package for Transaction
 * 							modify processData()
 * 							defect 7705 Ver 5.2.3
 * J Zwiener	03/01/2005	Java 1.4
 *							defect 7892 Ver 5.2.3
 * J Zwiener	03/21/2005	Use new getters\setters for frame, 
 *							controller, etc.
 *							defect 7892 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * J Rue		12/14/2007	Update rejection of transaction message for
 * 							DTA
 * 							modify processData()
 * 							defect 7996 Ver DEFECT-POS-A
 * ---------------------------------------------------------------------
 */

/** 
 * VCCash Drawer Selection Screen CUS001
 * View controller handles the control operation of screens.
 * 
 * @version	DEFECT-POS-A 	12/14/2007
 * @author 	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class VCSelectTransactionCUS001
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCSelectTransactionCUS001 constructor comment.
	 */
	public VCSelectTransactionCUS001()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 * @return String
	 */
	public int getModuleName()
	{
		return GeneralConstant.MISC;
	}
	/**
	 * Handles any errors that may occur
	 * @param aeRTSEx RTSException
	 */
	public void handleError(RTSException aeRTSEx)
	{
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int 
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			//Complete transaction, handel subcon differntly, 
			// else just call CompletePendingTrans   
			case (ENTER) :

				if (((TransactionHeaderData) aaData).isSubconTrans())
				{
					if (!com
						.txdot
						.isd
						.rts
						.client
						.registration
						.ui
						.SubconBundleManager
						.bundleExists())
					{
						new RTSException(
							RTSException.FAILURE_MESSAGE,
							"The data of the Subcontractor Transaction"
								+ " is not in a consistent state.\n"
								+ "The transaction will be deleted.",
							"").displayError(
							getFrame());
						setDirectionFlow(
							AbstractViewController.DESKTOP);
						try
						{
							// defect 7705
							// change package reference to Transaction
							com
								.txdot
								.isd
								.rts
								.services
								.common
								.Transaction
								.setTransactionHeaderData(
								(TransactionHeaderData) aaData);
							// end defect 7705
							getMediator().processData(
								GeneralConstant.COMMON,
								CommonConstant.CANCEL_TRANS,
								aaData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
					}

					else
					{
						setDirectionFlow(AbstractViewController.NEXT);
						setNextController(ScreenConstant.REG007);
						try
						{
							getMediator().processData(
								GeneralConstant.REGISTRATION,
								com
									.txdot
									.isd
									.rts
									.services
									.util
									.constants
									.RegistrationConstant
									.RESTORE_SUBCON_BUNDLE,
								null);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						if (getFrame() != null)
						{
							// defect 8884
							// use close() so that it does setVisibleRTS()
							close();
							//getFrame().setVisible(false);
							// end 8884
						}
					}

				}
				else
				{
					//This is not a Subcon Bundle.
					setDirectionFlow(AbstractViewController.DESKTOP);

					// defect 4757 
					// Handle DTA.  DTA Transactions must be canceled!
					if (((TransactionHeaderData) aaData)
						.getFeeSourceCd()
						== 3)
					{
						// defect 7996
						//	Per VTR, update message
						// Old message
//						new RTSException(
//							RTSException.FAILURE_MESSAGE,
//							"Unable to complete Dealer Title in"
//								+ "\nthis version of RTS. This group of"
//								+ "\nDealer Transactions will be deleted.",
//								"NOTICE").displayError(getFrame());
						// New message
						new RTSException(
							RTSException.FAILURE_MESSAGE,
							"Unable to complete Dealer Title."
							+ " This group of Dealer Transactions will"
							+ " be deleted.",
								"NOTICE").displayError(getFrame());
						// end defect 7996
						try
						{
							// defect 7705
							// change package reference for Transaction
							com
								.txdot
								.isd
								.rts
								.services
								.common
								.Transaction
								.setTransactionHeaderData(
								(TransactionHeaderData) aaData);
							getMediator().processData(
								GeneralConstant.COMMON,
								CommonConstant.CANCEL_TRANS,
								aaData);
							// end defect 7705
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
					} // end defect 4757
					else
					{
						// all other transactions come here
						try
						{
							getMediator().processData(
								getModuleName(),
								MiscellaneousConstant
									.COMPLETE_PENDING_TRANS,
								aaData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
					}
				}
				break;

				//Close all screens
			case (CANCEL) :
				setDirectionFlow(AbstractViewController.FINAL);
				try
				{
					getMediator().processData(
						getModuleName(),
						MiscellaneousConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisible(false);
				// end 8884
				break;

				//Retrieve all pending transactions
			case (MiscellaneousConstant.GET_PENDING_TRANSACTIONS) :
				setDirectionFlow(AbstractViewController.CURRENT);
				try
				{
					getMediator().processData(
						getModuleName(),
						MiscellaneousConstant.GET_PENDING_TRANSACTIONS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;

			case (HELP) :
				{
				}
		}
	}

	/**
	 * Set View checks if frame = null, set the screen and pass control
	 * to the controller.
	 *
	 * @param avPreviousControllers Vector
	 * @param asTransCode String
	 * @param aaData Object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (aaData == null)
		{ //Retrieve records
			TransactionHeaderData laTransHeaderData =
				new TransactionHeaderData();
			laTransHeaderData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laTransHeaderData.setSubstaId(
				SystemProperty.getSubStationId());
			laTransHeaderData.setTransWsId(
				SystemProperty.getWorkStationId());
			laTransHeaderData.setTransAMDate(new RTSDate().getAMDate());
			processData(
				MiscellaneousConstant.GET_PENDING_TRANSACTIONS,
				laTransHeaderData);
			return;
		}
		//Display frame
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmSelectTransactionCUS001(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmSelectTransactionCUS001(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
