package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCDeletionSummaryINV026.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/19/2005	Work on Switch / case
 * 							defect 7890 ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV026 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.3		08/17/2005
 * @author	Charlie Walker
 * <br>Creation Date:	12/05/2001 19:19:45
 */

public class VCDeletionSummaryINV026 extends AbstractViewController
{

	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int DELETE = 21;
	
	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int GENERATE_DELETE_REPORT = 22;
	
	/**
	 * VCDeletionSummaryINV026 constructor comment.
	 */
	public VCDeletionSummaryINV026()
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
		return GeneralConstant.INVENTORY;
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
			case AbstractViewController.ENTER :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.PRINT_INVENTORY_DEL_REPORT,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					if (((FrmDeletionSummaryINV026) getFrame())
						.isCancelEnabled())
					{
						setData(aaData);
						setDirectionFlow(
							AbstractViewController.DESKTOP);
						try
						{
							getMediator().processData(
								getModuleName(),
								InventoryConstant.NO_DATA_TO_BUSINESS,
								aaData);
						}
						catch (RTSException leRTSEx)
						{
							leRTSEx.displayError(getFrame());
						}
						getFrame().setVisibleRTS(false);
					}
					break;
				}
			case DELETE :
				{
					setNextController(ScreenConstant.INV006);
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case GENERATE_DELETE_REPORT :
				{
					setNextController(ScreenConstant.RPR000);
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.GENERATE_INVENTORY_DEL_REPORT,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
		}
	}
	
	/**
	 * Sets which frame to call.
	 *
	 * @param avPreviousControllers Vector
	 * @param asTransCode String  
	 * @param aaData Object the data
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmDeletionSummaryINV026(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmDeletionSummaryINV026(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
