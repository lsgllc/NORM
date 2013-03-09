package com.txdot.isd.rts.client.registration.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
*
* VCCopyFailureREG053.java
*
* (c) Texas Department of Transportation 2001
* ---------------------------------------------------------------------
* Change History:
* Name			Date		Description
* ------------	-----------	--------------------------------------------
* K Harrell		01/26/2004	5.2.0 Merge.  New Class. 
* 							Ver 5.2.0	  
* B Hargrove	06/23/2005	Modify code for move to Java 1.4.
*  	 						Use new getters\setters for frame, 
*							controller, etc.
*  							Bring code to standards.
* 							defect 7894 Ver 5.2.3 
* B Hargrove	08/11/2006	Focus lost issue. 
* 							Use close() so that it does setVisibleRTS().
*							modify processData()
* 							defect 8884 Ver 5.2.4
* ---------------------------------------------------------------------
*/
/**
 * View Controller for Screen: Copy Failure REG053
 * 
 * @version	5.2.4		08/11/2006
 * @author	Ashish Mahajan
 * <p>Creation Date:	10/31/2001
 */

public class VCCopyFailureREG053
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public static final int MANUAL_ENTRY = 5;
	
	/**
	* VCCopyFailureREG053 constructor.
	*/
	public VCCopyFailureREG053()
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
		return GeneralConstant.REGISTRATION;
	}
	
	/**
	 * Controls the screen flow from REG053.  
	 * It passes the data to the RTSMediator.
	 * @param aiCommand int A constant letting the VC know 
	 * 						which action to perform
	 * @param aaData Object The data from the frame
	 */
	public void processData(int aiCommand, Object aaData)
	{
		try
		{
			switch (aiCommand)
			{
				case ENTER :
					{
						break;
					}
				case CANCEL :
					{
						setDirectionFlow(AbstractViewController.FINAL);
						if (getFrame() != null)
						{
							// defect 8884
							// use close() so that it does setVisibleRTS()
							close();
							//getFrame().setVisible(false);
							// end 8884
						}
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
						break;
					}
				case MANUAL_ENTRY :
					{
						setDirectionFlow(AbstractViewController.NEXT);
						setNextController(ScreenConstant.REG006);
						if (getFrame() != null)
						{
							// defect 8884
							// use close() so that it does setVisibleRTS()
							close();
							//getFrame().setVisible(false);
							// end 8884
						}
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							null);
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
			RTSException leRTSEx = new RTSException("", aeEx);
			leRTSEx.displayError(getFrame());
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
			com.txdot.isd.rts.client.general.ui.
			RTSDialogBox laRTSDialogBox =
				getMediator().getParent();
			if (laRTSDialogBox != null)
			{
				setFrame(new FrmCopyFailureREG053(laRTSDialogBox));
			}
			else
			{
				setFrame(new FrmCopyFailureREG053(
					getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
