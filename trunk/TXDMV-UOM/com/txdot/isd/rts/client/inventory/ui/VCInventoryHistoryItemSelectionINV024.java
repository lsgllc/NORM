package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.InventoryHistoryUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCInventoryHistoryItemSelectionINV024.java
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
 * Ray Rowehl	07/18/2005	Switch / case work
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/16/2005	Add white space between methods
 * 							defect 7890 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV024 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.4		08/11/2006	
 * @author	Sunil Govindappa
 * <br>Creation Date:	11/21/2001 9:07:15
 */

public class VCInventoryHistoryItemSelectionINV024
	extends AbstractViewController
{

	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int GET_INV_ITEMS = 7;
	
	/**
	 * VCInventoryHistoryItemSelectionINV024 constructor comment.
	 */
	public VCInventoryHistoryItemSelectionINV024()
	{
		super();
	}
	
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 * 
	 * @return String
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
		try
		{

			switch (aiCommand)
			{
				case GET_INV_ITEMS :
					{
						setData(aaData);
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							InventoryConstant.RETRIEVE_INV_ITEMS,
							getData());
						break;
					}
				case CANCEL :
					{
						setData(aaData);
						setDirectionFlow(AbstractViewController.CANCEL);
						getMediator().processData(
							getModuleName(),
							InventoryConstant.NO_DATA_TO_BUSINESS,
							getData());
						// defect 8884
						// use close() so that it does setVisibleRTS()
						close();
						//getFrame().setVisible(false);
						// end 8884
						break;
					}
				case ENTER :
					{
						setData(aaData);
						setNextController(ScreenConstant.RPR000);
						setDirectionFlow(AbstractViewController.NEXT);
						if (((InventoryHistoryUIData) ((Vector) aaData)
							.get(CommonConstant.ELEMENT_0))
							.getReceiveHisReportIndi()
							== 1)
						{
							getMediator().processData(
								getModuleName(),
								InventoryConstant
									.GENERATE_RECEIVE_INVENTORY_HISTORY_REPORT,
								aaData);
						}
						else
						{
							getMediator().processData(
								getModuleName(),
								InventoryConstant
									.GENERATE_DELETE_INVENTORY_HISTORY_REPORT,
								aaData);
						}
						// defect 8884
						// use close() so that it does setVisibleRTS()
						close();
						//getFrame().setVisibleRTS(false);
						// end 8884
						break;
					}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
		catch (Exception aeEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSEx.displayError(getFrame());
		}
	}
	
	/**
	 * Set View checks if frame = null, sets the screen and pass 
	 * control to the controller.
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
					new FrmInventoryHistoryItemSelectionINV024(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmInventoryHistoryItemSelectionINV024(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
