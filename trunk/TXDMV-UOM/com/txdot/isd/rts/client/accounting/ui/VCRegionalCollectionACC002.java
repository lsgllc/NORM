package com.txdot.isd.rts.client.accounting.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.AccountingConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCRegionalCollectionACC002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 *  Change History:
 *  Name        Date        Description
 * ------------	-----------	-------------------------------------------- 
 * 	MAbs		04/24/2002	Inventory was not prompting 
 * 							defect 3451
 *	Jeff S.		10/28/2003	Regional Coll. not allowed in client mode.
 *							Made changes to processData() to first try 
 *							the	connection to see if it is available.  
 *							If not then	send trans through without going
 *							to server.  This was done by passing HashMap
 *							(data object and RTSException). 
 *							modify setView(), processData()
 *							defect 6543 Ver 5.1.5 Fix 1
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	03/02/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3 
 * Ray Rowehl	03/21/2005	Use getters and setters to access parent 
 * 							fields
 * 							defect 7884 Ver 5.2.3
 * K Harrell	05/20/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * The View Controller for the ACC002 screen.  It handles screen 
 * navigation and controls the visibility of its frame.
 * 
 * @version	5.2.3		05/20/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	12/18/2001 09:27:04
 */
public class VCRegionalCollectionACC002 extends AbstractViewController
{
	// Constants 
	public final static int INV = 20;
	public final static int INV_NO_BUSINESS = 21;

	public final static String TRANS_DATA = "TRANS_DATA";
	public final static String EXCEPTION = "EXCEPTION";
	/**
	 * VCRegionalCollectionACC002 constructor comment.
	 */
	public VCRegionalCollectionACC002()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 * 
	 * @return int 
	 */
	public int getModuleName()
	{
		return GeneralConstant.ACCOUNTING;
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand	int 
	 * @param aaData 	Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		// defect 6543
		// Added map to pass between (this) and AccountingClientBusiness
		// to allow processing in DB Down Mode
		Map laMap = new HashMap();
		// end defect 6543
		switch (aiCommand)
		{
			case ENTER :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.PMT004);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case INV :
				{
					try
					{
						// defect 6543
						// Going to AccountingClientBusiness but returning
						// here with either a new data object
						// or the original data object and an exception 
						// within a map.
						laMap.put(TRANS_DATA, aaData);
						// If DB is ready goto server but if not then 
						// call setview passing map which will call
						// INV_NO_BUSINESS without displaying a message
						if (RTSApplicationController.isDBUp())
						{
							setDirectionFlow(
								AbstractViewController.CURRENT);
							getMediator().processData(
								getModuleName(),
								AccountingConstant.REGIONAL_INVENTORY,
								laMap);
						}
						else
						{
							// DB is down
							setView(
								getPreviousControllers(),
								getTransCode(),
								laMap);
							return;
						}
						// end defect 6543
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// defect 6543
				// The new map returned from AccountingClientBusiness is
				// checked for a RTSException.
				// If there was an exception it is displayed and the 
				// INV007 screen is called passing it the new TransData 
				// object pulled from the map.
			case INV_NO_BUSINESS :
				{
					try
					{
						laMap = (Map) aaData;
						CompleteTransactionData laTransData =
							(CompleteTransactionData) laMap.get(
								TRANS_DATA);
						if (laMap.containsKey(EXCEPTION))
						{
							RTSException leRTSExceptionReturn =
								(RTSException) laMap.get(EXCEPTION);
							if (leRTSExceptionReturn
								.getMsgType()
								.equals(RTSException.DB_DOWN)
								|| leRTSExceptionReturn
									.getMsgType()
									.equals(
									RTSException.SERVER_DOWN))
							{
								leRTSExceptionReturn.setCode(618);
							}
							leRTSExceptionReturn.displayError(
								getFrame());
						}
						setDirectionFlow(AbstractViewController.NEXT);
						setNextController(ScreenConstant.INV007);
						getMediator().processData(
							getModuleName(),
							AccountingConstant.NO_DATA_TO_BUSINESS,
							laTransData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// end defect 6543
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers	Vector 
	 * @param asTransCode 			String
	 * @param aaData 				Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			java.awt.Dialog laDialog = getMediator().getParent();
			if (laDialog == null)
			{
				setFrame(
					new FrmRegionalCollectionACC002(
						getMediator().getDesktop()));
			}
			else
			{
				setFrame(new FrmRegionalCollectionACC002(laDialog));
			}
		}
		// defect 6543
		// Check to see if the data object is a Map.  If it is then call
		// processData passing the map and the command to process the INV
		// request with NO_DATA_TO_BUSINESS.
		try
		{
			Map laMap = (Map) aaData;
			if (laMap != null && laMap.containsKey(TRANS_DATA))
			{
				processData(INV_NO_BUSINESS, laMap);
				return;
			}
		}
		catch (ClassCastException aeEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSEx.displayError(getFrame());
			return;
		}
		// end defect 6543
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
