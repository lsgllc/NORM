package com.txdot.isd.rts.client.registration.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCStickerSelectionREG001.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
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
 * Registration Sticker Selection (REG001) view controller class. It  
 * handles screen navigation and controls the visibility of its frame.
 * 
 * 
 * @version	5.2.4		08/11/2006
 * @author	Joseph Kwik
 * <br>Creation Date:	10/03/2001     15:26:28 
 */

public class VCStickerSelectionREG001 extends AbstractViewController
{
	// defect 9085
	public final static int PLATE_SELECTION = 19;
	// end defect 9085
	public final static int REDIRECT_IS_NEXT_VC_REG029 = 22;
	public final static int REDIRECT_NEXT_VC = 23;

	/**
	 * VCReplacementChoicesREG016 constructor.
	 */
	public VCStickerSelectionREG001()
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
	 * Controls the screen flow from REG016.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int A constant letting the VC know which action
	 *  to perform
	 * @param aaData Object The data from the frame
	 * @see com.txdot.isd.rts.client.general.ui.AbstractViewController
	 */
	public void processData(int aiCommand, Object aaData)
	{
		this.setData(aaData);
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				{
					try
					{
						// determine if next vc is reg029
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.COMMON,
							CommonConstant.IS_NEXT_VC_REG029,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				//	defect 9085
			case PLATE_SELECTION :
				{
					setNextController(ScreenConstant.REG011);
					setDirectionFlow(AbstractViewController.NEXT);
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
					break;
				}
				// end defect 9085

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

			case REDIRECT_IS_NEXT_VC_REG029 :
				{
					try
					{
						Vector lvIsNextVCREG029 = (Vector) aaData;
						Boolean lbGoToREG029 =
							(Boolean) lvIsNextVCREG029.get(0);
						// first element is flag whether to go to REG029
						CompleteTransactionData laData =
							(
								CompleteTransactionData) lvIsNextVCREG029
									.get(
								1);
						if (lbGoToREG029.equals(Boolean.TRUE))
						{
							setNextController(ScreenConstant.REG029);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								GeneralConstant.COMMON,
								RegistrationConstant
									.NO_DATA_TO_BUSINESS,
								laData);
						}
						else
						{
							// determine next vc if NOT reg029
							setDirectionFlow(
								AbstractViewController.CURRENT);
							getMediator().processData(
								GeneralConstant.COMMON,
								CommonConstant
									.GET_NEXT_COMPLETE_TRANS_VC,
								((Vector) aaData).elementAt(1));
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case REDIRECT_NEXT_VC :
				{
					try
					{
						Vector lvNextVC = (Vector) aaData;
						String lsNextVCName = (String) lvNextVC.get(0);
						// first element is name of next controller
						CompleteTransactionData laData =
							(CompleteTransactionData) lvNextVC.get(1);
						if (lsNextVCName != null)
						{
							setNextController(lsNextVCName);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								GeneralConstant.COMMON,
								RegistrationConstant
									.NO_DATA_TO_BUSINESS,
								laData);
						}
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
	 *  the String names of the previous controllers in order
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		try
		{
			if (getFrame() == null)
			{
				RTSDialogBox laRTSDialogBox = getMediator().getParent();
				if (laRTSDialogBox != null)
				{
					setFrame(
						new FrmStickerSelectionREG001(laRTSDialogBox));
				}
				else
				{
					setFrame(
						new FrmStickerSelectionREG001(
							getMediator().getDesktop()));
				}
			}

			if (aaData == null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					"data is null",
					MiscellaneousRegConstant.ERROR_TITLE);
			}

			if (aaData instanceof Vector)
			{
				this.setData(aaData);
				this.setPreviousControllers(avPreviousControllers);
				this.setTransCode(asTransCode);
				getFrame().setController(this);
				getFrame().setData(aaData);
			}
			else if (aaData instanceof VehicleInquiryData)
			{
				super.setView(
					avPreviousControllers,
					asTransCode,
					aaData);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}
}
