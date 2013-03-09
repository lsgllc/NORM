package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * 
 * VCManualEntryDTA009.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * 
 * ---------------------------------------------------------------------
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/23/2005	Change class number from 008b to 009.
 * 							class/super VCManualEntryDTA009
 * 							modify setView(), processData()
 * 							defect 6963 Ver 5.2.3
 * J Rue		03/30/2005	Apply changes from walkthrough review
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * ---------------------------------------------------------------------
 */ 
 
/**
 * View Controller DTA009: Manual Entry DTA
 * 
 * @version 5.2.3			11/09/2005	
 * @author: Ashish Mahajan
 * <br>Creation Date: 		8/22/01 11:26:00
 */

public class VCManualEntryDTA009
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCManualEntryDTA009 constructor.
	 */
	public VCManualEntryDTA009()
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
		return GeneralConstant.TITLE;
	}
	/**
	 * Handles any errors that may occur
	 * 
	 * @param aeRTSEx RTSException
	 */
	public void handleError(
		com.txdot.isd.rts.services.exception.RTSException aeRTSEx)
	{
		// Empty block of code  handleError(RTSExecption)
	}
	/**
	 * Controls the screen flow from DTA009.  It passes the data to the
	 *  RTSMediator.
	 * 
	 * @param aiCommand int 
	 * 	A constant letting the VC know which action to perform
	 * @param aaData Object The data from the frame
	 */
	public void processData(int aiCommand, java.lang.Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
			{
				setDirectionFlow(AbstractViewController.NEXT);
				// defect 6963
				//	Set nextController to Dealer Transaction DTA008
				//setNextController(ScreenConstant.DTA008a);
				setNextController(ScreenConstant.DTA008);
				// end defect 6963
				try
				{
					getMediator().processData(
						getModuleName(),
						TitleConstant.PROCS_KEYBRD,
						aaData);
					if (getFrame() != null)
					{
						getFrame().setVisibleRTS(false);
					}
				}
				catch (RTSException aeRTSEx)
				{
					try
					{
						if (aeRTSEx
							.getMsgType()
							.equals(RTSException.DB_DOWN))
						{
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								aaData);
						}
						else if (
							aeRTSEx.getMsgType().equals(
								RTSException.SERVER_DOWN))
						{
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								aaData);
						}
						else
						{
							aeRTSEx.displayError(getFrame());
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								aaData);
						}
					}
					catch (RTSException aeRTSExc)
					{
						aeRTSExc.displayError(getFrame());
					}
				}
				break;
			}
			case CANCEL :
			{
				setDirectionFlow(AbstractViewController.FINAL);
				try
				{
					getMediator().processData(
						getModuleName(),
						GeneralConstant.NO_DATA_TO_BUSINESS,
						aaData);
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}
			case HELP :
			{
				break;
			}

		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector 
	 * 	A vector containing the String names of the previous controllers
	 * 	in order
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
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
				setFrame(new FrmManualEntryDTA009(laRTSDBox));
			}
			else
			{
				setFrame(new FrmManualEntryDTA009(
				getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
