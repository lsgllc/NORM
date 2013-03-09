package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCAdditionalSalesTaxScreenTTL033.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		02/17/2005	VAJ to WASD Clean Up
 *							defect 7898 Ver 5.2.3
 * J Rue		02/22/2005	Add AbstractViewController getters and 
 * 							setters to class.
 * 							modify processData()
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
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
 * VC for FRMdditionalSalesTaxScreenTTL033
 *
 * @version	5.2.3			11/09/2005
 * @author	T Pederson
 * <br>Creation Date:		8/22/01 10:21:03
 */

public class VCAdditionalSalesTaxScreenTTL033
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCAdditionalSalesTaxScreenTTL033 constructor comment.
	 */
	public VCAdditionalSalesTaxScreenTTL033()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own 
	 * module name
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
		// empty code block
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int
	 * @param aaData Object
	 */
	public void processData(int aiCommand, java.lang.Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					//Complete the transaction and be done
					// defect 7898
					//	Change directionFlow to setDirectionFlow()
					setDirectionFlow(AbstractViewController.NEXT);
					// defect 7898
					try
					{
						//				if(frame != null)
						//					frame.setVisible(false);
						// defect 7898
						//	Change nextController to setnextController()
						setNextController(ScreenConstant.PMT004);
						// defect 7898
						//	Change mediator to getMediator()
						getMediator().processData(
							GeneralConstant.COMMON,
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
						// end defect 7898
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case CANCEL :
				{
					// defect 7898
					//	Change directionFlow to setDirectionFlow()
					setDirectionFlow(AbstractViewController.FINAL);
					// end defect 7898
					try
					{
						// defect 7898
						//	Change frame to getFrame
						//	and mediator to getMediator()
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
						
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
						// end defect 7898
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
			// defwect 7898
			//	Change mediator to getMediator()
			//	and frame to setFrame()
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDB =
				getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(
					new FrmAdditionalSalesTaxScreenTTL033(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmAdditionalSalesTaxScreenTTL033(
						getMediator().getDesktop()));
			}
			// end defct 7898
			asTransCode = TransCdConstant.ADLSTX;
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}

}
