package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * VCAllocateItemINV013.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Clean up
 * 							organize imports, format source
 *							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/19/2005	Work on Switch / case
 * 							defect 7890 ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3.
 * ---------------------------------------------------------------------
 */

/**
 * VC for frame INV013
 * 
 * @version	5.2.3		08/17/2005 
 * @author	Charlie Walker
 * <br>Creation Date:	08/28/2001 18:19:45
 */

public class VCAllocateItemINV013 extends AbstractViewController
{
	/**
	 * Constant to define a command for the screen to communicate 
	 * with the VC (Calculate)
	 */
	public static final int CALCULATE = 11;
	
	/**
	 * Constant to define a command for the screen to communicate 
	 * with the VC (YES)
	 */
	public static final int YES = 14;
	
	/**
	 * VCAllocateItemINV013 constructor comment.
	 */
	public VCAllocateItemINV013()
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
	 * @param data Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.CALCULATE_INVENTORY_UNKNOWN,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						((FrmAllocateItemINV013) getFrame()).cbExThrown =
							true;

						// Pass all exceptions to the frame for handling.
						// This allows for components to be turned red
						// and gain focus.
						(
							(FrmAllocateItemINV013) getFrame())
								.procsExcptn(
							leRTSEx);
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
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
			case VCAllocateItemINV013.YES :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.PREVIOUS);
					boolean lbVisible = false;
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.ALLOC_INVENTORY_ITEMS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
						lbVisible = true;
					}
					if (!lbVisible)
					{
						getFrame().setVisibleRTS(false);
					}
					break;
				}
		}
	}
	
	/**
	 * All subclasses must override this method to handle data coming
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
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
				setFrame(new FrmAllocateItemINV013(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmAllocateItemINV013(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
