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
 * VCInventoryHistoryCountyINV015.java
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
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV015 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.3		08/17/2005
 * @author	Sunil Govindappa
 * <br>Creation date:	11/16/2001 11:26:37
 */

public class VCInventoryHistoryCountyINV015
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int GET_COUNTIES = 7;
	
	/**
	 * VCInventoryHistoryCountyINV015 constructor comment.
	 */
	public VCInventoryHistoryCountyINV015()
	{
		super();
	}
	
	/**
	 * All subclasses must override this method to return their own
	 * module name.
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
						setNextController(ScreenConstant.INV024);
						setData(aaData);
						setDirectionFlow(AbstractViewController.NEXT);
						getMediator().processData(
							getModuleName(),
							InventoryConstant.NO_DATA_TO_BUSINESS,
							aaData);
						break;
					}
				case GET_COUNTIES :
					{
						setData(aaData);
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.RETRIEVE_REGIONAL_COUNTIES,
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
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
		catch (Exception aeEx)
		{
			RTSException leRTSEx2 =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSEx2.displayError(getFrame());
		}
	}
	
	/**
	 * Set View checks if frame = null, sets the screen and passes 
	 * control to the controller.
	 *
	 * @param avPreviousControllers Vector
	 * @param asTransCode String  
	 * @param aaData Object the data
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox leRTSDB = getMediator().getParent();
			if (leRTSDB != null)
			{
				setFrame(new FrmInventoryHistoryCountyINV015(leRTSDB));
			}
			else
			{
				setFrame(
					new FrmInventoryHistoryCountyINV015(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
