package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCItemsOnHoldINV017.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C. Walker	03/26/2002	Added comments and code to make frames
 *							modal to each other
 * Ray Rowehl	02/26/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/03/2005	Remove cbRcvdComboData.  It is not used.
 * 							delete cbRcvdComboData
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/18/2005	Switch / case work
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV017 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.3			08/17/2005
 * @author	Sai Machavarapu
 * <br>Creation Date:		11/30/2001 15:55:07
 */

public class VCItemsOnHoldINV017 extends AbstractViewController
{
	// not used
	//private boolean cbRcvdComboData = false;
	/**
	 * Flag that shows if INV017 need to be initialized
	 */
	private boolean cbInit = true;
	
	private boolean cbItmHldData = true;
	private int ciOperation = 0;

	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public final static int HOLD_INVENTORY_ITEM = 18;
	
	public final static int GENERATE_HOLD_REPORT = 22;
	
	/**
	 * VCItemsOnHoldINV017 constructor comment.
	 */
	public VCItemsOnHoldINV017()
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
			case InventoryConstant.GET_INVENTORY_ALLOCATION_DATA :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.GET_INVENTORY_ALLOCATION_DATA,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case HOLD_INVENTORY_ITEM :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.INV018);
					ciOperation = HOLD_INVENTORY_ITEM;
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case InventoryConstant.UPDATE_INVENTORY_STATUS_CD :
				{
					setData(aaData);
					ciOperation =
						InventoryConstant.UPDATE_INVENTORY_STATUS_CD;
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.UPDATE_INVENTORY_STATUS_CD,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case AbstractViewController.ENTER :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.PRINT_INVENTORY_ITEMS_ONHOLD_REPORT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case GENERATE_HOLD_REPORT :
				{
					setNextController(ScreenConstant.RPR000);
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.GENERATE_INVENTORY_ITEMS_ONHOLD_REPORT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					if (((FrmItemsOnHoldINV017) getFrame())
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
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						getFrame().setVisibleRTS(false);
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
	 * @param aaData Object  
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
				setFrame(new FrmItemsOnHoldINV017(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmItemsOnHoldINV017(
						getMediator().getDesktop()));
			}
		}

		if (cbItmHldData)
		{
			cbItmHldData = false;
			InventoryAllocationData laIAData =
				new InventoryAllocationData();
			laIAData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laIAData.setSubstaId(SystemProperty.getSubStationId());
			laIAData.setInvStatusCd(1);
			processData(
				InventoryConstant.GET_INVENTORY_ALLOCATION_DATA,
				laIAData);
		}
		else if (cbInit)
		{
			cbInit = false;
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		else if (ciOperation == HOLD_INVENTORY_ITEM)
		{
			ciOperation = 0;
			getFrame().setData(aaData);
		}
	}
}
