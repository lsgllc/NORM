package com.txdot.isd.rts.client.accounting.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.AccountingConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCFundsKeySelectionKEY021.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	09/07/2001  Added comments 
 * MAbs			05/16/2002	Added beeps to exception
 * 							defect 3909
 * RHicks   	07/29/2002	Add help desk support  
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	12/07/2004	General formatting, javadoc cleanup 
 *							Replace code for HelpDesk prompt with call to
 *							new AccountingHelpDeskAssistant routine
  *							delete import for jOptionPane
 *							delete helpDeskDisplayed
 *							modify setView()
 *							defect 6913 Ver 5.2.2
 * K Harrell	03/02/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3 
 * Ray Rowehl	03/21/2005	Use getters and setters to access parent 
 * 							fields
 * 							defect 7884 Ver 5.2.3
 * K Harrell	04/27/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3
 * --------------------------------------------------------------------- 
 */
/**
 * The View Controller for the KEY021 screen.  It handles screen 
 * navigation and controls the visibility of its frame.
 *
 * @version	5.2.3		04/27/2005 
 * @author	Michael Abernethy
 * <br>Creation Date:	07/18/2001 11:17:28
 */
public class VCFundsKeySelectionKEY021 extends AbstractViewController
{
	// Constant
	public final static int TRACE = 20;
	public final static int PAYMENT = 21;
	public final static int FUNDS = 22;
	public final static int CHECK = 23;
	
	/**
	 * Creates a VCFundsKeySelectionKEY021.
	 */
	public VCFundsKeySelectionKEY021()
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
		return GeneralConstant.ACCOUNTING;
	}
	/**
	 * Controls the screen flow from ACC001.  It passes the data to the
	 * RTSMediator.
	 *
	 * @param aiCommand	int
	 * @param aaData 	Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case TRACE :
				{
					setData(aaData);
					setNextController(ScreenConstant.ACC022);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.SEARCH_PAYMENT_RECORDS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.setBeep(RTSException.BEEP);
						if (leRTSEx
							.getMsgType()
							.equals(RTSException.MF_DOWN)
							|| leRTSEx.getMsgType().equals(
								RTSException.SERVER_DOWN))
						{
							leRTSEx.displayError(getFrame());
							processData(CANCEL, null);
						}
						else
						{
							leRTSEx.displayError(getFrame());
						}
					}
					break;
				}
			case PAYMENT :
				{
					setData(aaData);
					setNextController(ScreenConstant.ACC023);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.SEARCH_PAYMENT_RECORDS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.setBeep(RTSException.BEEP);
						if (leRTSEx
							.getMsgType()
							.equals(RTSException.MF_DOWN)
							|| leRTSEx.getMsgType().equals(
								RTSException.SERVER_DOWN))
						{
							leRTSEx.displayError(getFrame());
							processData(CANCEL, null);
						}
						else
						{
							leRTSEx.displayError(getFrame());
						}
					}
					break;
				}
			case FUNDS :
				{
					setData(aaData);
					setNextController(ScreenConstant.ACC024);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.SEARCH_PAYMENT_RECORDS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.setBeep(RTSException.BEEP);
						if (leRTSEx
							.getMsgType()
							.equals(RTSException.MF_DOWN)
							|| leRTSEx.getMsgType().equals(
								RTSException.SERVER_DOWN))
						{
							leRTSEx.displayError(getFrame());
							processData(CANCEL, null);
						}
						else
						{
							leRTSEx.displayError(getFrame());
						}
					}
					break;
				}
			case CHECK :
				{
					setData(aaData);
					setNextController(ScreenConstant.ACC023);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.SEARCH_PAYMENT_RECORDS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.setBeep(RTSException.BEEP);
						if (leRTSEx
							.getMsgType()
							.equals(RTSException.MF_DOWN)
							|| leRTSEx.getMsgType().equals(
								RTSException.SERVER_DOWN))
						{
							leRTSEx.displayError(getFrame());
							processData(CANCEL, null);
						}
						else
						{
							leRTSEx.displayError(getFrame());
						}
					}
					break;
				}
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers	Vector
	 * @param asTransCd 		    String
	 * @param aaData 				Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCd,
		Object aaData)
	{
		if (getFrame() == null)
		{
			java.awt.Dialog laDialog = getMediator().getParent();
			if (laDialog == null)
			{
				setFrame(
					new FrmFundsKeySelectionKEY021(
						getMediator().getDesktop()));
			}
			else
			{
				setFrame(new FrmFundsKeySelectionKEY021(laDialog));
			}
		}
		// defect 6913
		// Replace inline code with call to promptHelpDesk()
		// Prompt for County number if at Help Desk
		//if (SystemProperty.getHelpDeskFlag().equals("ON")
		//		&& !helpDeskDisplayed)
		//	{
		//		String ofcIssuanceId =
		//			JOptionPane.showInputDialog(
		//				mediator.getParent(),
		//				"County No: ",
		//				"Help Desk Assistant",
		//				JOptionPane.QUESTION_MESSAGE);
		//		System.setProperty("HelpDeskOfcId", ofcIssuanceId);
		//		helpDeskDisplayed = true;
		//	}
		if (SystemProperty.getHelpDeskFlag().equals("ON"))
		{
			AccountingHelpDeskAssistant.promptHelpDesk();
		}
		super.setView(avPreviousControllers, asTransCd, aaData);
		// end defect 6913 
	}
}
