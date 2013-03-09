package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.InventoryInquiryUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCInventoryInquirySelectionINV027.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		11/23/2004	Allowing to enter after error message
 *							modify processData(), setView()
 *							defect 7421 Ver 5.2.2
 * Ray Rowehl	02/26/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	07/18/2005	More constants work
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV027 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.3 			08/17/2005
 * @author	Charlie Walker
 * <br>Creation Date:		11/30/2001 15:19:45
 */

public class VCInventoryInquirySelectionINV027
	extends AbstractViewController
{
	/**
	 * VCAllocateFromLocationINV027 constructor comment.
	 */
	public VCInventoryInquirySelectionINV027()
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
					setNextController(ScreenConstant.INV022);
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.RETRIEVE_INVENTORY_ITEMS_FOR_INQUIRY,
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
					//defect 7421
					if (getFrame() != null)
					{
						getFrame().setVisibleRTS(false);
					}
					//end defect 7421
					break;
				}
		}
	}

	/**
	 * Directs traffic to frame.
	 * Create frame if it does not exist.
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
		if (aaData == null)
		{
			return;
		}
		InventoryInquiryUIData laIIUID =
			(InventoryInquiryUIData) ((Vector) aaData).get(
				CommonConstant.ELEMENT_0);
		Vector lvEntityList =
			(Vector) ((Vector) aaData).get(CommonConstant.ELEMENT_1);

		// If no Entity data found on the db, then throw exception 
		// and return to INV021
		if (lvEntityList.size() == CommonConstant.ELEMENT_0)
		{
			String lsMsgType = RTSException.WARNING_MESSAGE;
			String lsMsg = CommonConstant.STR_SPACE_EMPTY;
			String lsMsgTtl = ErrorsConstant.MSG_NO_RECORDS_FOUND;

			if (laIIUID.getInvInqBy().equals(InventoryConstant.DLR))
			{
				lsMsg = ErrorsConstant.MSG_NO_DEALERS_EXIST;
			}
			else if (
				laIIUID.getInvInqBy().equals(InventoryConstant.SUBCON))
			{
				lsMsg = ErrorsConstant.MSG_NO_SUBCONS_EXIST;
			}
			RTSException leRTSExMsg =
				new RTSException(lsMsgType, lsMsg, lsMsgTtl);
			//defect 7421
			leRTSExMsg.displayError(getMediator().getParent());
			processData(AbstractViewController.CANCEL, null);
			//end defect 7421
			return;
		}
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(
					new FrmInventoryInquirySelectionINV027(
						laRTSDB,
						laIIUID.getInvInqBy()));
			}
			else
			{
				setFrame(
					new FrmInventoryInquirySelectionINV027(
						getMediator().getDesktop(),
						laIIUID.getInvInqBy()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
