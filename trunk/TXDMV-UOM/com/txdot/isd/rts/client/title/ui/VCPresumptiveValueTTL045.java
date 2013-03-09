package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *
 * VCPresumptiveValueTTL044.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * T Pederson	09/08/2006	New class.
 * 							defect 8926 Ver 5.2.5
 * ---------------------------------------------------------------------
 */

/**
 * VC used to display presumptive value. 
 * 
 * @version	5.2.5			09/08/2006
 * @author	Todd Pederson
 * <br>Creation Date: 		09/08/2006 11:06:10 
 */

public class VCPresumptiveValueTTL045
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCPresumptiveValueTTL044 constructor comment.
	 */
	public VCPresumptiveValueTTL045()
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
				setFrame(new FrmPresumptiveValueTTL045(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmPresumptiveValueTTL045(
						getMediator().getDesktop()));
			}
		}
		
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
