/**
 * 
 */
package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * VCRightsofSurvivorshipTTL010.java
 *
 * (c) Texas Department of Motor Vehicles 2012
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/13/2012	Created
 * 							defect 10827 Ver 6.10.0 
 * K Harrell	02/10/2012	Clear saved data for TTL010 
 * 							modify processData() 
 * 							defect 10827 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/** 
 * Manage the Rights of Survivorship TTL010 screen.
 * 
 * @version	6.10.0		02/10/2012
 * @author	Kathy Harrell 
 * @since				01/13/2012		17:38:17 
 */
public class VCRightsofSurvivorshipTTL010
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCRightsofSurvivorshipTTL010 constructor comment.
	 */
	public VCRightsofSurvivorshipTTL010()
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
		return GeneralConstant.TITLE;
	}
	
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int 
	 * 	the command so the Frame can communicate with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, java.lang.Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
			{	
				setNextController(ScreenConstant.TTL011);
				setDirectionFlow(AbstractViewController.NEXT);
				try
				{
					getMediator().closeVault(
							ScreenConstant.TTL010,
							null);
					
					getMediator().processData(
						getModuleName(),
						MiscellaneousRegConstant
							.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}
			case CANCEL :
			{
				setDirectionFlow(AbstractViewController.CANCEL);
				try
				{
					getMediator().processData(
						getModuleName(),
						TitleConstant.NO_DATA_TO_BUSINESS,
						aaData);
					
					close();
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError();
				}
				break;
			}
		}
	}
	
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param cvPreviousControllers java.util.Vector
	 * @param asTransCode java.lang.String
	 * @param aaData java.lang.Object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDBox =
				getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmRightsofSurvivorshipTTL010());
			}
			else
			{
				setFrame(
					new FrmRightsofSurvivorshipTTL010(getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
