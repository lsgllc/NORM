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
 * VCStolenSRSTTL037.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
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
 * Ray Rowehl	08/12/2006	Change to use close instead of setVisible().
 * 							Let exception decide where to base MsgDialog
 * 							from.	
 * 							Remove unused code.
 * 							Some code clean up.
 * 							delete handleError()
 * 							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */

/**
 * VC used to capture the stolen and/or safety responsibility 
 * suspension indicators in status change.
 * 
 * @version	5.2.4			08/12/2006
 * @author	Todd Pederson
 * <br>Creation Date: 		08/22/2001 15:46:16 
 */

public class VCStolenSRSTTL037
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCStolenSRSTTL037 constructor comment.
	 */
	public VCStolenSRSTTL037()
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
	
// defect 8884
// remove this.
//	/**
//	 * Handles any errors that may occur
//	 * 
//	 * @param aeRTSEx RTSException
//	 */
//	public void handleError(
//		com.txdot.isd.rts.services.exception.RTSException aeRTSEx)
//	{
//		// Empty block of code
//	}
// end defect 8884

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
					// defect 8884
					// let exception decise where to base MsgDialog
					//aeRTSEx.displayError(getFrame());
					aeRTSEx.displayError();
					// end defect 8884
				}
				setDirectionFlow(AbstractViewController.FINAL);
				try
				{
					// defct 8884
					//if (getFrame() != null)
					//{
					// use close instead of setVisible
					close();
					//	setFrame().setVisible(false);
					//}
					// end defect 8884
					
					getMediator().processData(
						GeneralConstant.COMMON,
						CommonConstant.ADD_TRANS,
						laCompTransData);
				}
				catch (RTSException aeRTSEx)
				{
					// defect 8884
					// let exception decide where to base the MsgDialog
					//aeRTSEx.displayError(getFrame());
					aeRTSEx.displayError();
					// end defect 8884
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
						
					// defect 8884
					//if (getFrame() != null)
					//{
					// use close instead of setVisibleRTS
					close();
					//	getFrame().setVisibleRTS(false);
					//}
					// end defect 8884
				}
				catch (RTSException aeRTSEx)
				{
					// defect 8884
					// let exception decide where to base MsgDialog
					//aeRTSEx.displayError(getFrame());
					aeRTSEx.displayError();
					// end defect 8884
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
				setFrame(new FrmStolenSRSTTL037(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmStolenSRSTTL037(getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
