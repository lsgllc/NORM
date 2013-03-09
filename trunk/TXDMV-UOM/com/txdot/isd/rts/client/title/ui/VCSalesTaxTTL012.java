package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * 
 * VCSalesTaxTTL012.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/10/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		05/18/2005	comment out unused variables
 * 							defect 7898 Ver 5.2.3
 * J Rue		11/07/2005	Update incomplete method headers.   
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbage.
 * 							defect 7898 Ver 5.2.3    
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * K Harrell	10/21/2007	A little cleanup.
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	05/25/2008	Add processing for Supervisor Overrid Code
 * 							add SUPV_OVRIDE
 * 							modify processData() 
 * 							defect 9584 Ver 3 Amigos PH B 
 * ---------------------------------------------------------------------
 */

/**
 * VC used to capture sales tax information used by fee calculations.
 *
 * @version	3 Amigos PH B	05/25/2008
 * @author	Todd Pederson
 * <br>Creation Date:		08/22/2001 11:38:03
 */

public class VCSalesTaxTTL012 extends AbstractViewController
{
	public final static int REDIRECT_IS_NEXT_VC_REG029 = 6;
	public final static int REDIRECT_NEXT_VC = 7;
	// defect 9584 
	public final static int SUPV_OVRIDE = 24;
	// end defect 9584 

	/**
	 * VCSalesTaxTTL012 constructor comment.
	 */
	public VCSalesTaxTTL012()
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
	 * Handles any errors that may occur
	 * 
	 * @param aeRTSEx RTSException
	 */
	public void handleError(RTSException aeRTSEx)
	{
		// Empty block of code
	}
	
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int 
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);

					try
					{
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

			case CANCEL :
				{
					boolean lbIsDTA = false;
					if (getTransCode().equals(TransCdConstant.DTANTD)
						|| getTransCode().equals(TransCdConstant.DTANTK)
						|| getTransCode().equals(TransCdConstant.DTAORD)
						|| getTransCode().equals(TransCdConstant.DTAORK))
					{
						lbIsDTA = true;
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						setPreviousController(ScreenConstant.TTL008);
					}
					else
					{
						setDirectionFlow(AbstractViewController.CANCEL);
					}
					try
					{
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
						if (lbIsDTA)
						{
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								new RTSException());
						}
						else
						{
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								aaData);
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				
				// defect 9584 
			case SUPV_OVRIDE :
				{
					try
					{
						setNextController(ScreenConstant.CTL004);
						setDirectionFlow(AbstractViewController.NEXT);
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
				// end defect 9584
				 
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
								TitleConstant.NO_DATA_TO_BUSINESS,
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
								TitleConstant.NO_DATA_TO_BUSINESS,
								laData);
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
	 * @param cvPreviousControllers Vector
	 * @param csTransCode String
	 * @param aaData Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDBox = getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmSalesTaxTTL012(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmSalesTaxTTL012(getMediator().getDesktop()));
			}

			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		else
		{
			setData(aaData);
			setPreviousControllers(avPreviousControllers);
			setTransCode(asTransCode);

			getFrame().setController(this);
			getFrame().setData(aaData);
		}
	}
}
