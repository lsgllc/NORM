package com.txdot.isd.rts.client.accounting.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.FundsDueDataList;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.MFLogError;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.AccountingConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCFundsDueSummaryACC017.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	09/07/2001  Added comments 
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	12/10/2004	General formatting, javadoc cleanup 
 *							Replace code for HelpDesk prompt with call to
 *							new AccountingHelpDeskAssistant routine
 *							delete trcode, helpDeskDisplayed
 *							add csTransCd
 *							modify processData(), setView() 
 *							defect 6913 Ver 5.2.2
 * K Harrell	03/02/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3 
 * Ray Rowehl	03/21/2005	Use getters and setters to access parent 
 * 							fields
 * 							defect 7884 Ver 5.2.3
 * K Harrell	04/27/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3
 * K Harrell	05/19/2005	FundsDueData Object renaming
 * 							modify processData()
 * 							defect 7889 Ver 5.2.3 
 * --------------------------------------------------------------------- 
 */
/**
 * The View Controller for the ACC017 screen.  It handles screen 
 * navigation and controls the visibility of its frame.
 *
 * @version	5.2.3		05/19/2005  
 * @author	Michael Abernethy
 * <br>Creation Date:	07/03/2001 08:24:55 
 */
public class VCFundsDueSummaryACC017 extends AbstractViewController
{
	// String 
	private String csTransCd;

	// Vector 
	private Vector cvPrevControllers;

	// Constants 
	public final static int REMIT_FUNDS = 20;
	public final static int RETRIEVE_RECS = 21;

	/**
	 * Creates a VCFundsDueSummaryACC017.
	 */
	public VCFundsDueSummaryACC017()
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
	 * Controls the screen flow from ACC017.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand	int 
	 * @param aaData 	Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setData(aaData);
					setNextController(ScreenConstant.ACC018);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.NO_DATA_TO_BUSINESS,
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
						getMediator().processData(
							getModuleName(),
							AccountingConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
			case REMIT_FUNDS :
				{
					setData(aaData);
					setNextController(ScreenConstant.ACC020);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case RETRIEVE_RECS :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						java.util.Map lmMap = new java.util.HashMap();
						lmMap.put(
							AccountingConstant.SUB,
							new Integer(
								SystemProperty.getSubStationId()));
						lmMap.put("MF", MFLogError.getErrorString());
						// defect 6913
						// Replace inline code with call to promptHelpDesk()
						// Prompt for County number if at Help Desk
						//if (SystemProperty
						//		.getHelpDeskFlag()
						//		.equals("ON"))
						//	{
						//		String ofcIssuanceId =
						//			JOptionPane.showInputDialog(
						//				mediator.getParent(),
						//				"County No: ",
						//				"Help Desk Assistant",
						//				JOptionPane.QUESTION_MESSAGE);
						//		map.put(
						//			AccountingConstant.OFC,
						//			new Integer(ofcIssuanceId));
						//	}
						Integer liOfcNo =
							new Integer(
								SystemProperty.getOfficeIssuanceNo());
						if (SystemProperty
							.getHelpDeskFlag()
							.equals("ON"))
						{
							if (AccountingHelpDeskAssistant
								.promptHelpDesk())
							{
								liOfcNo =
									new Integer(
										System.getProperty(
											"HelpDeskOfcId"));
							}
						}
						lmMap.put(AccountingConstant.OFC, liOfcNo);
						// end defect 6913
						getMediator().processData(
							getModuleName(),
							AccountingConstant
								.RETRIEVE_FUNDS_DUE_SUMMARY_RECORDS,
							lmMap);
					}
					catch (RTSException aeRTSEx)
					{
						// If MF Down or Server down, just show empty ACC017
						if (aeRTSEx
							.getMsgType()
							.equals(RTSException.MF_DOWN)
							|| aeRTSEx.getMsgType().equals(
								RTSException.SERVER_DOWN))
						{
							FundsDueDataList laEmptyFundsDueData =
								new FundsDueDataList();
							laEmptyFundsDueData.setFundsDue(
								new java.util.Vector());
							setView(
								cvPrevControllers,
								csTransCd,
								laEmptyFundsDueData);
						}
						else
						{
							aeRTSEx.displayError(getFrame());
						}
					}
					break;
				}
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed by the
	 * VC, and sends the data to the frame.
	 *
	 * @param avPreviousControllers	Vector 
	 * @param asTransCd            	String
	 * @param aaData              	Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCd,
		Object aaData)
	{
		cvPrevControllers = avPreviousControllers;
		csTransCd = asTransCd;
		if (aaData == null)
		{
			processData(RETRIEVE_RECS, aaData);
			return;
		}
		if (getFrame() == null)
		{
			java.awt.Dialog laDialog = getMediator().getParent();
			if (laDialog == null)
			{
				setFrame(
					new FrmFundsDueSummaryACC017(
						getMediator().getDesktop()));
			}
			else
			{
				setFrame(new FrmFundsDueSummaryACC017(laDialog));
			}
		}
		super.setView(avPreviousControllers, asTransCd, aaData);
	}
}
