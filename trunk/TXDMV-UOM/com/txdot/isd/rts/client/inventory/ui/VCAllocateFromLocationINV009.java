package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCAllocateFromLocationINV009.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/03/2005	Remove unused fields
 * 							delete caAlloctnDbData
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	06/17/2005	More code cleanup
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This is the View Controller for screen INV009.
 *
 * @version 5.2.3			08/17/2005
 * @author	Charlie Walker
 * <br>Creation Date:		08/28/2001 18:19:45
 */

public class VCAllocateFromLocationINV009
	extends AbstractViewController
{
	// This field is not used
	///**
	// * Data object that holds the data for the Select FROM Entity combo
	// * box
	// */
	//private AllocationDbData caAlloctnDbData = new AllocationDbData();
	/**
	 * Flag that shows if the combo box data has been retrieved from 
	 * the db
	 */
	private boolean cbRcvdComboData = false;
	
	/**
	 * Flag that shows if INV009 need to be initialized
	 */
	private boolean cbInit = true;
	
	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int RETRIEVE_COMBO_DATA = 12;
	
	/**
	 * VCAllocateFromLocationINV009 constructor comment.
	 */
	public VCAllocateFromLocationINV009()
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
				setNextController(ScreenConstant.INV010);
				setData(aaData);
				setDirectionFlow(AbstractViewController.NEXT);
				try
				{
					getMediator().processData(
						getModuleName(),
						InventoryConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSex)
				{
					aeRTSex.displayError(getFrame());
				}
				break;
			}
			case AbstractViewController.CANCEL :
			{
				setData(aaData);
				setDirectionFlow(AbstractViewController.FINAL);
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
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
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
				catch (RTSException aeRTSEx)
				{
					if (getFrame() != null)
					{
						aeRTSEx.displayError(getFrame());
					}
					else
					{
						aeRTSEx.displayError(
							getMediator().getDesktop());
					}
				}
				return;
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
		Vector lvDataIn = new Vector();

		if (!cbRcvdComboData)
		{
			GeneralSearchData laGSData = new GeneralSearchData();
			laGSData.setIntKey1(SystemProperty.getOfficeIssuanceNo());
			laGSData.setIntKey2(SystemProperty.getSubStationId());
			cbRcvdComboData = true;

			processData(RETRIEVE_COMBO_DATA, laGSData);
			return;
		}
		else if (cbInit)
		{
			InventoryAllocationUIData laInvAlloctnUIData =
				new InventoryAllocationUIData();
			laInvAlloctnUIData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laInvAlloctnUIData.setSubstaId(
				SystemProperty.getSubStationId());

			lvDataIn.addElement(aaData);
			lvDataIn.addElement(laInvAlloctnUIData);

			if (getFrame() == null)
			{
				RTSDialogBox laRTSDB = getMediator().getParent();
				if (laRTSDB != null)
				{
					setFrame(
						new FrmAllocateFromLocationINV009(laRTSDB));
				}
				else
				{
					setFrame(
						new FrmAllocateFromLocationINV009(
							getMediator().getDesktop()));
				}

				cbInit = false;
				super.setView(
					avPreviousControllers,
					asTransCode,
					lvDataIn);
			}
			else if (!cbInit)
			{
				return;
			}
		}
	}
}