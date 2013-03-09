package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCInventoryDeleteINV006.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 *							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/18/2005	Switch / case work
 * 							defect 7890 ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * The View Controller for the INV006 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.3		08/17/2005
 * @author	Charlie Walker
 * <br>Creation Date:	12/06/2001 09:19:45
 */
public class VCInventoryDeleteINV006 extends AbstractViewController
{

	/**
	 * Constant to define a command for the screen to communicate.
	 */
	public static final int YES = 14;

	/**
	 * Go to Frame Item Range Delete.
	 * Constant to define a command for the screen to communicate.
	 */
	public static final int GO_TO_INV025 = 25;

	/**
	 * Go to Frame Deletion Summary.
	 * Constant to define a command for the screen to communicate.
	 */
	public static final int GO_TO_INV026 = 26;

	/**
	 * VCInventoryDeleteINV006 constructor comment.
	 */
	public VCInventoryDeleteINV006()
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
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.CALCULATE_INVENTORY_UNKNOWN,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						(
							(FrmInventoryDeleteINV006) getFrame())
								.cbExThrown =
							true;
						// Pass all exceptions to the frame for handling.  
						// This allows for components to be turned red  
						// and gain focus.
						(
							(FrmInventoryDeleteINV006) getFrame())
								.procsExcptn(
							aeRTSEx);
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CANCEL);
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
			case YES :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
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
					break;
				}
			case GO_TO_INV026 :
				{
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
			case GO_TO_INV025 :
				{
					setNextController(ScreenConstant.INV025);
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
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
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
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
		if (aaData instanceof Vector)
		{
			Integer laInt =
				(Integer) ((Vector) aaData).get(
					CommonConstant.ELEMENT_0);
			if (laInt.intValue() == CommonConstant.ELEMENT_1)
			{
				processData(
					GO_TO_INV026,
					((Vector) aaData).get(CommonConstant.ELEMENT_1));
				return;
			}
			else if (laInt.intValue() == CommonConstant.ELEMENT_0)
			{
				RTSException leRTSExMsg = new RTSException(589);
				leRTSExMsg.displayError(getFrame());
				processData(
					GO_TO_INV025,
					((Vector) aaData).get(CommonConstant.ELEMENT_1));
				return;
			}
		}

		if (getFrame() == null)
		{
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmInventoryDeleteINV006(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmInventoryDeleteINV006(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}