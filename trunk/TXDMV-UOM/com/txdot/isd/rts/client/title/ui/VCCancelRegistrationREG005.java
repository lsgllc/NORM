package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *
 * VCCancelRegistrationREG005.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/08/2005	VAJ to WASD Clean Up
 *							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.   
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbage.
 * 							defect 7898 Ver 5.2.3    
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * VC for VCCancelRegistrationREG005
 *
 * @version	5.2.3			11/09/2005
 * @author	Administrator
 * <br>Creation Date:		8/22/01 3:46:16
 */

public class VCCancelRegistrationREG005
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCStolenSRSTTL037 constructor comment.
	 */
	public VCCancelRegistrationREG005()
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
		return GeneralConstant.TITLE;
	}
	/**
	 * Handles any errors that may occur
	 * @param aeRTSEx com.txdot.isd.rts.client.util.exception.
	 * 					RTSException
	 */
	public void handleError(
		com.txdot.isd.rts.services.exception.RTSException aeRTSEx)
	{
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param asCommand int the command so the Frame can communicate 
	 * 					with the VC
	 * @param aaData Object the data
	 */
	public void processData(int asCommand, java.lang.Object aaData)
	{
		switch (asCommand)
		{
			case ENTER :
				{
					//Complete the transaction and be done
					CompleteTitleTransaction laTtlTrans =
						new CompleteTitleTransaction(
							(VehicleInquiryData) aaData,
							getTransCode());
					CompleteTransactionData laCompTransData = null;
					try
					{
						laCompTransData =
							laTtlTrans.doCompleteTransaction();
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					setDirectionFlow(AbstractViewController.DESKTOP);
					try
					{
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
						getMediator().processData(
							GeneralConstant.COMMON,
							CommonConstant.ADD_TRANS,
							laCompTransData);
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
				break;
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector
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
			com.txdot.isd.rts.client.general.ui.RTSDialogBox 
				laRTSDBoxrd = getMediator().getParent();
			if (laRTSDBoxrd != null)
			{
				setFrame(new FrmCancelRegistrationREG005(laRTSDBoxrd));
			}
			else
			{
				setFrame(new FrmCancelRegistrationREG005(
						getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
