package com.txdot.isd.rts.client.registration.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCOwnerRegistrationREG032.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * B Hargrove	07/19/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 *  	 					Use new getters\setters for frame, 
 *							controller, etc.
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */
/**
 * VC OwnerRegistration REG032
 *
 * @version	5.2.4		08/11/2006
 * @author	Nancy Ting
 * <br>Creation Date:	10/8/2001 11:54:00
 */

public class VCOwnerRegistrationREG032
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VC ModifySubcontractorRenewal REG009
	 */
	public VCOwnerRegistrationREG032()
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
	 * Controls the screen flow from REG009.  It passes the data to the 
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
			case AbstractViewController.CANCEL :
			{
				setDirectionFlow(AbstractViewController.FINAL);
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisible(false);
				// end 8884
				try
				{
					getMediator().processData(getModuleName(),
						RegistrationConstant.NO_DATA_TO_BUSINESS,
						null);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
					break;
			}

			case AbstractViewController.ENTER :
			{
				setDirectionFlow(AbstractViewController.NEXT);
				setNextController(ScreenConstant.INV007);
				try
				{
					getMediator().processData(getModuleName(),
						RegistrationConstant.PROCESS_ISSUE_INV,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}

		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector A vector containing 
	 * the String names of the previous controllers in order
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
			com.txdot.isd.rts.client.general.ui.RTSDialogBox 
				laRTSDialogBox = getMediator().getParent();

			if (laRTSDialogBox != null)
			{
				setFrame(new FrmOwnerRegistrationREG032(laRTSDialogBox));
			}
			else
			{
				setFrame(new FrmOwnerRegistrationREG032(
					getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
