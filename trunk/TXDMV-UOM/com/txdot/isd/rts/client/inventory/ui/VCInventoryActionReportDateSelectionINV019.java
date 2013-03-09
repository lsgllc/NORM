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
 * VCInventoryActionReportDateSelectionINV019.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C. Walker	03/26/2002	Added comments and code to make frames
 *							modal to each other
 * Ray Rowehl	02/25/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/04/2005	Rename from 20 to 19.
 * 							defect 6964 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV019 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version 5.2.3		08/17/2005
 * @author	Sunil Govindappa
 * <br>Creation Date:	11/21/2001 9:07:15
 */

public class VCInventoryActionReportDateSelectionINV019
	extends AbstractViewController
{
	/**
	 * VCInventoryHistoryItemSelectionINV019 constructor comment.
	 */
	public VCInventoryActionReportDateSelectionINV019()
	{
		super();
	}
	
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 * 
	 * @return java.lang.String
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
		try
		{
			switch (aiCommand)
			{
				case AbstractViewController.ENTER :
					{
						setData(aaData);
						setNextController(ScreenConstant.RPR000);
						setDirectionFlow(AbstractViewController.NEXT);
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.GENERATE_INVENTORY_ACTION_REPORT,
							aaData);
						break;
					}
				case CANCEL :
					{
						setDirectionFlow(AbstractViewController.FINAL);
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
						getMediator().processData(
							getModuleName(),
							InventoryConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
			}
		}
		catch (RTSException leRTSEx)
		{
			leRTSEx.displayError(getFrame());
		}
		catch (Exception aeEx)
		{
			RTSException leRTSEx2 =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSEx2.displayError(getFrame());
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
				setFrame(
					new FrmInventoryActionReportDateSelectionINV019(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmInventoryActionReportDateSelectionINV019(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
