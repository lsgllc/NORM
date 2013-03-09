package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * VCDeleteItemFromInvoiceINV011.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Cleanup
 *							organize imports, format source,
 *							rename fields
 *							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/04/2005	Rename INV015 frame to be INV028
 * 							defect 6965 Ver 5.2.3
 * Ray Rowehl	07/19/2005	Work on Switch / case
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
 
/**
 * The View Controller for the INV011 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.3 		08/17/2005
 * @author	Charlie Walker
 * <br>Creation Date:	10/17/2001 18:19:45
 */

public class VCDeleteItemFromInvoiceINV011
	extends AbstractViewController
{
	/**
	 * Constant to define a command for the screen to communicate with 
	 * the VC
	 */
	public static final int YES = 14;
	
	/**
	 * VCDeleteItemFromInvoiceINV011 constructor comment.
	 */
	public VCDeleteItemFromInvoiceINV011()
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
								.ADD_MODIFY_DELETE_INVENTORY_ITEM,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						(
							(FrmDeleteItemFromInvoiceINV011) getFrame())
								.cbExThrown =
							true;
						leRTSEx.displayError(getFrame());
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
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
			case VCModifyItemOnInvoiceINV028.YES :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.PREVIOUS);
					boolean lbVisible = false;
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.ADD_MODIFY_DELETE_INVENTORY_ITEM,
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
				setFrame(new FrmDeleteItemFromInvoiceINV011(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmDeleteItemFromInvoiceINV011(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
