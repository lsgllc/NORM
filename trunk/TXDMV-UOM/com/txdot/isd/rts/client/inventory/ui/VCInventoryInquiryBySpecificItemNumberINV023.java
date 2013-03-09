package com.txdot.isd.rts.client.inventory.ui;

import java.util.*;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.services.exception.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCInventoryInquiryBySpecificItemNumberINV023.java
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
 * Ray Rowehl	07/17/2005	Case / switch cleanup
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3	
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV023 screen.  It handles screen
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.3		08/17/2005
 * @author	Charlie Walker
 * <br>Creation Date:	12/01/2001 18:29:45
 */

public class VCInventoryInquiryBySpecificItemNumberINV023
	extends AbstractViewController
{
	/**
	 * VCInventoryInquiryItemTypeINV022 constructor comment.
	 */
	public VCInventoryInquiryBySpecificItemNumberINV023()
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
					setNextController(ScreenConstant.RPR000);
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.GENERATE_INVENTORY_INQUIRY_REPORT,
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
		}
	}
	
	/**
	 * Routes traffic to the frame.
	 * If the frame has not been created, create it.
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
					new FrmInventoryInquiryBySpecificItemNumberINV023(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmInventoryInquiryBySpecificItemNumberINV023(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
