package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCAllocateToLocationINV010
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
 * Ray Rowehl	04/03/2005	Remove fields that are not used.
 * 							delete caAlloctnDbData, caInvAlloctnUIData
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/19/2005	Work on Switch / case
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
 
/**
 * This is the View Controller for screen INV010.
 * 
 * @version	5.2.3			08/17/2005
 * @author	Charlie Walker
 * <br>Creation Date: 		08/28/2001 18:19:45
 */

public class VCAllocateToLocationINV010 extends AbstractViewController
{
	// These fields are not used
	///**
	// * Data object that holds the data for the Select FROM Entity combo
	// * box
	// */
	//private AllocationDbData caAlloctnDbData = new AllocationDbData();
	///**
	// * Data object that holds the data from IVN009
	// */
	//private InventoryAllocationUIData caInvAlloctnUIData =
	//	new InventoryAllocationUIData();
	/**
	 * int that indicates that we should keep processing.
	 */
	private static final int CONTINUE = 0;
	
	/**
	 * int that indicates that we should cancel back to previous screen.
	 */
	private static final int CANCEL_BACK = 1;
	
	/**
	 * Flag that shows if INV010 need to be initialized
	 */
	private boolean cbInit = true;
	
	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int RETRIEVE_COMBO_DATA = 13;
	
	/**
	 * VCAllocateToLocationINV010 constructor comment.
	 */
	public VCAllocateToLocationINV010()
	{
		super();
	}
	
	/**
	 * All subclasses must override this method to return their 
	 * own module name.
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
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				{
					setNextController(ScreenConstant.INV014);
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
			case AbstractViewController.CANCEL :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.PREVIOUS);
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
			case RETRIEVE_COMBO_DATA :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.RETRIEVE_ALLOCATION_DISPLAY_DATA,
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
	 * @param asTransCode Object  
	 * @param aaData Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (aaData == null)
		{
			return;
		}

		if (aaData instanceof ReportSearchData)
		{
			processData(AbstractViewController.CANCEL, aaData);
			return;
		}
		else if (aaData instanceof Vector)
		{
			int liBackToBeg = CONTINUE;

			Vector lvDataIn = (Vector) aaData;
			if (lvDataIn.size() == CommonConstant.ELEMENT_3)
			{
				liBackToBeg =
					((Integer) lvDataIn.get(CommonConstant.ELEMENT_2))
						.intValue();
			}

			if (liBackToBeg == CANCEL_BACK)
			{
				processData(AbstractViewController.CANCEL, aaData);
				return;
			}
			else if (cbInit)
			{
				if (getFrame() == null)
				{
					RTSDialogBox laRTSDB = getMediator().getParent();
					if (laRTSDB != null)
					{
						setFrame(
							new FrmAllocateToLocationINV010(laRTSDB));
					}
					else
					{
						setFrame(
							new FrmAllocateToLocationINV010(
								getMediator().getDesktop()));
					}

					cbInit = false;
					super.setView(
						avPreviousControllers,
						asTransCode,
						aaData);
				}
				else if (!cbInit)
				{
					return;
				}
			}
		}
	}
}
