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
 * VCModifyInvoiceINV002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		07/03/2002	Handle going back to the mainmenu 
 *							when view report is not selected.
 *							modify processData()
 *							defect 4403
 * Ray Rowehl	02/26/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/03/2005	Correct location of super.setView()
 * 							modify setView()
 * 							delete cbInit
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/04/2005	change constant for INV028
 * 							modify processData()
 * 							defect 6965 Ver 5.2.3
 * Ray Rowehl	07/18/2005	Switch / case work
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/06/2005	Modify processData to remove duplicate 
 * 							function ids.
 * 							modify processData()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV002 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 * 
 * @version	5.2.3			08/17/2005
 * @author	Charlie Walker
 * <br>Creation Date:		10/11/2001 10:57:45
 */

public class VCModifyInvoiceINV002 extends AbstractViewController
{
	// Not used
	///**
	// * Flag that shows if INV002 need to be initialized
	// */
	//private boolean cbInit = true;
	
	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int MODIFY = 16;
	
	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int ADD = 17;
	
	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int DELETE = 18;
	
	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int GENERATE_RECEIVE_REPORT = 20;
	
	/**
	 * VCAllocationSummaryINV002 constructor comment.
	 */
	public VCModifyInvoiceINV002()
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
								.GENERATE_INVENTORY_RECEIVED_REPORT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
			case GENERATE_RECEIVE_REPORT :
				{
					setNextController(ScreenConstant.RPR000);
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.GENERATE_INVENTORY_RECEIVED_REPORT,
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
			case VCModifyInvoiceINV002.ADD :
				{
					setNextController(ScreenConstant.INV012);
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
					break;
				}
			case VCModifyInvoiceINV002.MODIFY :
				{
					// defect 6965
					setNextController(ScreenConstant.INV028);
					// end defect 6965
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
					break;
				}
			case VCModifyInvoiceINV002.DELETE :
				{
					setNextController(ScreenConstant.INV011);
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
					break;
				}
		}
	}
	
	/**
	 * Directs traffic to frame.
	 * If frame has not been created, create the frame.
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
				setFrame(new FrmModifyInvoiceINV002(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmModifyInvoiceINV002(
						getMediator().getDesktop()));
			}
		}
		// always call setview.
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
