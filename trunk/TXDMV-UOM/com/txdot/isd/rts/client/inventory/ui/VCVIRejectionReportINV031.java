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
 * VCVIRejectionReportINV031.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * 
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV031 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version Special Plates		03/14/2006
 * @author	Min Wang
 * <br>Creation Date:	11/21/2007 9:07:15
 */

public class VCVIRejectionReportINV031
	extends AbstractViewController
{
	/**
	 * VCVIRejectionReportINV031 constructor comment.
	 */
	public VCVIRejectionReportINV031()
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
								.GENERATE_VI_REJECTION_REPORT,
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
					new FrmVIRejectionReportINV031(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmVIRejectionReportINV031(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
