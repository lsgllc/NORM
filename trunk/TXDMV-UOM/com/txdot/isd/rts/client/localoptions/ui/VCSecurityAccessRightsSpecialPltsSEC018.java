package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;

/*
 * VCSecurityAccessRightsSpecialPltsSEC018.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Reyes		02/16/2007	Created;
 * 							defect 9124 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Controller for Security Access Rights Spl Plts screen SEC018
 *
 * @version	Special Plates	02/16/2007
 * @author	Mark Reyes
 * <br>Creation Date:		02/16/2007 15:37:00
 */
public class VCSecurityAccessRightsSpecialPltsSEC018
	extends AbstractViewController
{

	/**
	 * VCSecurityAccessRightsSpecialPltsSEC018.java Constructor
	 * 
	 * 
	 */
	public VCSecurityAccessRightsSpecialPltsSEC018()
	{
		super();
	}

	/** 
	 * Returns the Module name constant used by the RTSMediator to pass
	 * the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.LOCAL_OPTIONS;
	}

	/**
	 * Handles data coming from their JDialogBox - inside the subclasses
	 * implementation should be calls to fireRTSEvent() to pass the data
	 * to the RTSMediator
	 * 
	 * @param aiCommand int
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case CANCEL	:
			{
				setDirectionFlow(AbstractViewController.CANCEL);
				try
				{
					getMediator().processData(
						getModuleName(),
						LocalOptionConstant.NO_DATA_TO_BUSINESS,
						getData());
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				getFrame().setVisibleRTS(false);
				break;
			}
			case ENTER	:
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.PREVIOUS);
					try
					{
						if(getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
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
	 * Creates the actual frame, stores the protected variables needed
	 * by the VC, and sends the data to the frame.
	 *  
	 * @param previousController vector
	 * @param transCode String
	 * @param data Object
	 */
		public void setView(
			Vector avPreviousControllers,
			String asTransCode,
			Object aaData)
		{
			if(getFrame() == null)
			{
				Dialog laDialog = getMediator().getParent();
				if(laDialog != null)
				{
					setFrame(
					new FrmSecurityAccessRightsSpecialPltsSEC018(laDialog));	
				}
				else
				{
					setFrame(
						new FrmSecurityAccessRightsSpecialPltsSEC018(
						getMediator().getDesktop()));
				}
			}
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
}


