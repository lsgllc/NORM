package com.txdot.isd.rts.client.registration.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
*
* VCModifyRegistrationChoicesREG024.java
*
* (c) Texas Department of Transportation 2001
* ---------------------------------------------------------------------
* Change History:
* Name			Date		Description
* ------------	-----------	--------------------------------------------
* B Hargrove	06/23/2005	Modify code for move to Java 1.4.
*							Bring code to standards.
*  	 						Use new getters\setters for frame, 
*							controller, etc.
* 							defect 7894 Ver 5.2.3 
* B Hargrove	07/20/2005	Refactor\Move 
* 							RegistrationModifyData class from
*							com.txdot.isd.rts.client.reg.ui to
*							com.txdot.isd.rts.services.data.
*							defect 7894 Ver 5.2.3
* B Hargrove	08/11/2006	Focus lost issue. 
* 							Use close() so that it does setVisibleRTS().
*							modify processData()
* 							defect 8884 Ver 5.2.4
* ---------------------------------------------------------------------
*/
/**
 * The View Controller for the REG024 screen.  It handles screen 
 * navigation and controls the visibility of its frame.
 * 
 * @version	5.2.4		08/11/2006
 * @author	Sai 
 * <br>Creation Date:	10/3/2001 16:15:31
 */

public class VCModifyRegistrationChoicesREG024
	extends AbstractViewController
{

  	private final static String TITLE_APPR_PERM_ADDL_WT = 
		 "RTS: Apprehended Permanent Additional Weight";
	private final static String TITLE_REG_CORRECT = 
		"RTS: Registration Correction";
	private final static String TITLE_VOL_PERM_ADDL_WT = 
		"RTS: Voluntary Permanent Additional Weight";
		
	/**
	 * VCModifyRegistrationChoicesREG024 constructor comment.
	 */
	public VCModifyRegistrationChoicesREG024()
	{
		super();
	}
	
	/**
	* @see com.txdot.isd.rts.client.general.ui.AbstractViewController
	* 
	* @return int
	*/
	public int getModuleName()
	{
		return GeneralConstant.REGISTRATION;
	}
	
	/**
	 * Controls the screen flow from REG024.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int A constant letting the VC know which action 
	 * to perform
	 * @param aaData Object The data from the frame
	 */
	public void processData(int aiCommand, Object aaData)
	{
		this.setData(aaData);
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
			{
				setNextController(ScreenConstant.KEY001);
				setDirectionFlow(AbstractViewController.NEXT);
				try
				{
					RegistrationModifyData laRegModData =
						(RegistrationModifyData) aaData;
					if (laRegModData.getRegModifyType() ==
						RegistrationConstant.REG_MODIFY_VOLUNTARY)
					{
						getMediator().getDesktop().setTitle(
							TITLE_VOL_PERM_ADDL_WT);
						setTransCode(TransCdConstant.PAWT);
					}
					else if (laRegModData.getRegModifyType() ==
						RegistrationConstant.REG_MODIFY_APPREHENDED)
					{
						getMediator().getDesktop().setTitle(
							TITLE_APPR_PERM_ADDL_WT);
						setTransCode(TransCdConstant.CORREG);
					}
					else if (laRegModData.getRegModifyType() ==
						RegistrationConstant.REG_MODIFY_REG)
					{
						getMediator().getDesktop().setTitle(
							TITLE_REG_CORRECT);
						setTransCode(TransCdConstant.CORREG);
					}
						getMediator().processData(
						getModuleName(),
						RegistrationConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisible(false);
				// end 8884
				break;
			}
			case AbstractViewController.CANCEL :
			{
				setDirectionFlow(AbstractViewController.CANCEL);
					try
				{
					getMediator().processData(
						getModuleName(),
						RegistrationConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisible(false);
				// end 8884
				break;
			}
		}
	}
	
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector A vector containing
	 *  the String names of the previous controllers in order
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
			RTSDialogBox laRTSDialogBox = getMediator().getParent();
			if (laRTSDialogBox != null)
			{
				setFrame(new FrmModifyRegistrationChoicesREG024(
					laRTSDialogBox));
			}
			else
			{
				setFrame(new FrmModifyRegistrationChoicesREG024(
					getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
