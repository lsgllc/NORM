package com.txdot.isd.rts.client.registration.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCSubcontractorEntryREG006
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/26/2004	5.2.0 Merge. See PCR 34 comments. Formatted.
 * 							Version 5.2.0	   
 * B Hargrove	06/23/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 *  	 					Use new getters\setters for frame, 
 *							controller, etc.
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------
 */

/**
* VC SubcontractorEntryREG006
* 
* @version:	5.2.4	08/11/2006 
*  
* @author:	Nancy Ting
* <br>Creation Date:	10/08/2001
*/

public class VCSubcontractorEntryREG006
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public static final int GET_ALLOCATED_INV =
		RegistrationConstant.GET_SUBCON_ALLOCATED_INV;
	public static final int REG007_REDIRECT = 90;
	
	/**
	 * VCSubcontractorEntryREG006 constructor.
	 */
	public VCSubcontractorEntryREG006()
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
		return com.txdot.isd.rts.services.util.constants.
		GeneralConstant.REGISTRATION;
	}
	
	/**
	 * Controls the screen flow from REG006.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int A constant letting the VC know which action 
	 * to perform
	 * @param aaData Object The data from the frame
	
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
			{
				setDirectionFlow(AbstractViewController.NEXT);
				setNextController(ScreenConstant.REG007);
				try
				{
					if (getFrame()!= null)
					{
						// defect 8884
						// use close() so that it does setVisibleRTS()
						close();
						//getFrame().setVisible(false);
						// end 8884
					}
					getMediator().processData(getModuleName(),
						com.txdot.isd.rts.services.util.constants.
						RegistrationConstant.INIT_SUBCON_RENWL,aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}
			case AbstractViewController.CANCEL :
			{
				setDirectionFlow(AbstractViewController.FINAL);
				try
				{
					// defect 8884
					// use close() so that it does setVisibleRTS()
					close();
					//if (getFrame()!= null)
					//{
					//	getFrame().setVisible(false);
					//}
					// end 8884
					getMediator().processData(getModuleName(),
						com.txdot.isd.rts.services.util.constants.
						RegistrationConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}
			case GET_ALLOCATED_INV :
			{
				setDirectionFlow(AbstractViewController.CURRENT);
				try
				{
					getMediator().processData(getModuleName(),
						com.txdot.isd.rts.services.util.constants.
						RegistrationConstant.GET_SUBCON_ALLOCATED_INV,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}
			case REG007_REDIRECT :
			{
				setDirectionFlow(AbstractViewController.NEXT);
				setNextController(ScreenConstant.REG007);
				try
				{
					// defect 8884
					// use close() so that it does setVisibleRTS()
					close();
					//if (getFrame()!= null)
					//{
					//	getFrame().setVisible(false);
					//}
					// end 8884
					getMediator().processData(getModuleName(),
						com.txdot.isd.rts.services.util.constants.
						RegistrationConstant.RESTORE_SUBCON_BUNDLE,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}
			default :
			{
			}
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector A vector containing
	 *  the String names of the previous controllers in order
	 * @param aaTransCode String The TransCode
	 * @param aaData Object The data object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (SubconBundleManager.bundleExists())
		{
			processData(
				VCSubcontractorEntryREG006.REG007_REDIRECT,
				null);
		}
		else
		{
			if (getFrame()== null)
			{
				com.txdot.isd.rts.client.general.ui.RTSDialogBox 
					laRTSDialogBox = getMediator().getParent();
				if (laRTSDialogBox != null)
				{
					setFrame(new FrmSubcontractorEntryREG006(
						laRTSDialogBox));
				}
				else
				{
					setFrame(new FrmSubcontractorEntryREG006(
							getMediator().getDesktop()));
				}
			}
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
	}
}
