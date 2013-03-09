package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *
 * VCDealerMediaContentsDTA007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Todd			04/25/2002	Added code so that Exception's parent is 
 *							set properly.CQU100003592 
 * T Pederson	05/17/2002	Added setHelpURL to setView() so that help 
 *							button would go to correct section in users 
 *							guide help    
 * B Arredondo	12/18/2002	Fixing Defect 5147. Made changes in 
 * 							setView() to use the constant defined in 
 * 							GeneralConstant class for displaying DTA004 
 * 							dialog box
 * J Rue		08/13/2004	Comment call to Sticker Receipt Report
 *                          Sticker/Receipt Report call is made from 
 *							FrmDealerMediaContentsDTA007.
 *							actionPerformed()
 * 							modify processData()
 *							defect 7429 Ver 5.2.1
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/23/2005	Change ScreenConstant from DTA008a to DTA008
 * 							Cleanup leftover codeing
 * 							modify processData()
 * 							defect 6963,7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * B Hargrove	05/26/2009  Add Flashdrive option to DTA. Change title 
 * 							and screen verbiage from 'diskette'.
 * 							refactor\rename\modify:
 * 							VCDealerDisketteContentsDTA007 / 
 * 							VCDealerMediaContentsDTA007
 *                   		modify setView() 
 * 							defect 10075 Ver Defect_POS_F  
 * K Harrell	11/30/2009	Add 'current' processing to generate 
 * 						 	 RSPS Sticker Report. 
 * 							DTA Cleanup.  
 * 							add PRINT_RSPS_STKR_RPT
 * 							modify processData() 
 * 							defect 10290 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: Dealer External Media Contents DTA007
 *
 * @version	Defect_POS_H		11/30/2009
 * @author	Ashish Mahajan
 * <br>Creation Date:			08/22/2001 10:59:15
 */

public class VCDealerMediaContentsDTA007 extends AbstractViewController
{
	private static String STRMSG =
		"The Dealer Title Application file has been \n successfully "
			+ "copied to this workstation."
			+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			+ "Press \"Enter\" to continue.</p>";
	public static final int PRINT_AND_CONTINUE = 80;
	public static final int NO_PRINT_AND_CONTINUE = 81;
	public static final int PRINT_AND_RETURN = 82;

	// defect 10290 
	public static final int PRINT_RSPS_STKR_RPT = 83;
	// end defect 10290 

	/**
	 * VCDealerMediaContentsDTA007 constructor.
	 */
	public VCDealerMediaContentsDTA007()
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
	 * Controls the screen flow from DTA007.  It passes the data to the
	 *  RTSMediator.
	 * 
	 * @param aiCommand int A constant letting the VC know which action 
	 * 						to perform
	 * @param aaData Object The data from the frame
	 */
	public void processData(int aiCommand, java.lang.Object aaData)
	{
		switch (aiCommand)
		{
			// defect 10290 
			case PRINT_RSPS_STKR_RPT :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant
								.GENERATE_STICKER_RECEIPT_REPORT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// end defect 10290 
			case PRINT_AND_CONTINUE :
				{
					setNextController(ScreenConstant.DTA008);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						close();
						getMediator().processData(
							getModuleName(),
							TitleConstant
								.GENERATE_PRELIMINARY_DEALER_REPORT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case NO_PRINT_AND_CONTINUE :
				{
					setNextController(ScreenConstant.DTA008);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						close();
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case PRINT_AND_RETURN :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						close();
						getMediator().processData(
							getModuleName(),
							TitleConstant
								.GENERATE_PRELIMINARY_DEALER_REPORT,
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
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						close();
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
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
	 * @param avPreviousControllers Vector 
	 * 	A vector containing the String names of the previous 
	 * 	controllers in order
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
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
				setFrame(new FrmDealerMediaContentsDTA007(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmDealerMediaContentsDTA007(
						getMediator().getDesktop()));
			}
		}
		if (aaData != null)
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.INFORMATION_VALIDATION,
					STRMSG,
					GeneralConstant.DTA004);

			leRTSEx.setHelpURL(RTSHelp.DTA004);

			RTSDialogBox laRTSDBox = getMediator().getParent();

			int liRet = 0;
			if (laRTSDBox != null)
			{
				liRet = leRTSEx.displayError(getFrame());
			}
			else
			{
				liRet =
					leRTSEx.displayError(getMediator().getDesktop());
			}
			if (liRet == RTSException.ENTER)
			{
				super.setView(
					avPreviousControllers,
					asTransCode,
					aaData);
			}
			else if (liRet == RTSException.CANCEL)
			{
				processData(CANCEL, aaData);
			}
		}
	}
}
