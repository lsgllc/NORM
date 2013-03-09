package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * VCModifyItemOnInvoiceINV028.java
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
 * Ray Rowehl	04/04/2005	Rename for new frame number INV028
 * 							defect 6965 Ver 5.2.3
 * Ray Rowehl	07/18/2005	Switch / case work	
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
 
/**
 * The View Controller for the INV028 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.3		08/17/2005
 * @author: Charlie Walker
 * <br>Creation Date:	08/28/2001 18:19:45
 */

public class VCModifyItemOnInvoiceINV028 extends AbstractViewController
{

	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int CALCULATE = 11;
	
	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int YES = 14;
	
	/**
	 * VCModifyItemOnInvoiceINV028 constructor comment.
	 */
	public VCModifyItemOnInvoiceINV028()
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
					catch (RTSException aeRTSEx)
					{
						(
							(FrmModifyItemOnInvoiceINV028) getFrame())
								.cbExThrown =
							true;
						// Pass all exceptions to the frame for handling.  
						// This allows for components to be turned red 
						// and gain focus.
						(
							(FrmModifyItemOnInvoiceINV028) getFrame())
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
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
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
				setFrame(new FrmModifyItemOnInvoiceINV028(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmModifyItemOnInvoiceINV028(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
