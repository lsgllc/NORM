package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * VCInventoryItemRangeDeleteINV025.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/26/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/18/2005	Constants work
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3	
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV025 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.3		08/17/2005
 * @author	Charlie Walker
 * <br>Creation Date:	12/05/2001 19:19:45
 */

public class VCInventoryItemRangeDeleteINV025
	extends AbstractViewController
{

	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int DELETE = 21;
	
	/**
	 * VCInventoryItemRangeDeleteINV025 constructor comment.
	 */
	public VCInventoryItemRangeDeleteINV025()
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
					setDirectionFlow(AbstractViewController.PREVIOUS);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.DELETE_INVENTORY_ITEM,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
			case AbstractViewController.CANCEL :
				{
					Vector lvDataOut =
						new Vector(CommonConstant.ELEMENT_2);
					lvDataOut.add(
						new Integer(CommonConstant.ELEMENT_1));
					lvDataOut.add(null);
					aaData = lvDataOut;

					setData(aaData);
					setDirectionFlow(AbstractViewController.PREVIOUS);
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
				setFrame(
					new FrmInventoryItemRangeDeleteINV025(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmInventoryItemRangeDeleteINV025(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
