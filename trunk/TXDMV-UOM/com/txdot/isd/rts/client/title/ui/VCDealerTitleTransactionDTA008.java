package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *
 * VCDealerTitleTransactionDTA008.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/16/2004	change reference to isIsRecordRejected() to
 *							isRecordRejected()
 *							Formatting/JavaDoc/Variable Name Cleanup
 *							modify processData() 
 *							defect 7736 Ver 5.2.2 
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/23/2005	Change ScreenConstant from DTA008a to DTA008
 * 							Bring class up to RTS standards
 * 							modify processData()
 * 							defect 6963,7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.   
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbage.
 * 							defect 7898 Ver 5.2.3    
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/09/2006	Add setVisible(false) after setView() to 
 * 							ensure the FRM..DTA008 is not visible()
 * 							modify setView()
 * 							defect 8851 Ver 5.2.3
 * J Rue		08/11/2006	Add getFrame() != null to conditional
 * 							modify setView()
 * 							defect 8851 Ver 5.2.4
 * Ray Rowehl	08/11/2006	Add a comment on drop through of CANCEL.
 * K Harrell	12/15/2009	DTA Cleanup 
 * 							delete handleError() 
 * 							modify processData() 
 * 							defect 10290 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: Dealer Diskette Contents DTA007
 *
 * @version	Defect_POS_H	12/15/2009
 * @author	Ashish Mahajan
 * <br>Creation Date:		08/22/2001 11:06:26  
 */

public class VCDealerTitleTransactionDTA008
	extends AbstractViewController
{
	public static final int REJECT_ENT = 10;
	public static final int DO_PAYMENT = 11;
	private boolean cbShouldDisplay = true;
	
	/**
	 * VCDealerTitleTransactionDTA008 constructor.
	 */
	public VCDealerTitleTransactionDTA008()
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
	 * Controls the screen flow from DTA008a.  It passes the data to the
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
			case ENTER :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						if (aaData != null
							&& ((DealerTitleData) aaData)
								.isRecordRejected())
						{
							setDirectionFlow(
								AbstractViewController.CURRENT);
						}
						else
						{
							setNextController(ScreenConstant.KEY006);
							setDirectionFlow(
								AbstractViewController.NEXT);
						}
						// defect 10290 
						//	DealerDataContainer laDlrContainer =
						//		new DealerDataContainer();
						//	Vector lvDlrTtlData = null;
						//	if (getFrame() != null)
						//	{
						//		lvDlrTtlData =
						//			((FrmDealerTitleTransactionDTA008) 
						//				getFrame()).getDlrTitleDataVector();
						//		//set information in vault
						//		getMediator().closeVault(
						//			ScreenConstant.DTA008,
						//			lvDlrTtlData);
						//	}
						//	laDlrContainer.setDealerTitleDataObjs(
						//		lvDlrTtlData);
						//	laDlrContainer.setDlrTtldata(
						//		(DealerTitleData) aaData);
						// end defect 10290 
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
			case CANCEL :
				{
					// empty code block.
					// note that this just drops through to REJECT_ENT!
				}
			case REJECT_ENT :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						// defect 10290
						close();
						// end defect 10290 

						getMediator().processData(
							GeneralConstant.COMMON,
							CommonConstant.CANCEL_TRANS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case DO_PAYMENT :
				{
					setDirectionFlow(AbstractViewController.DESKTOP);
					cbShouldDisplay = false;
					try
					{
						// defect 10290 
						close();
						// end defect 10290 

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
	 * @param avPreviousControllers Vector Containing names of previous 
	 * 		controllers in order
	 * @param asTransCd String The TransCode
	 * @param aaData Object The data object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCd,
		Object aaData)
	{
		if (getFrame() == null)
		{
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRd =
				getMediator().getParent();
				
			if (laRd != null)
			{
				setFrame(new FrmDealerTitleTransactionDTA008(laRd));
			}
			else
			{
				setFrame(
					new FrmDealerTitleTransactionDTA008(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCd, aaData);

		// defect 8851
		//	If pending trans screen is visible ensure Frm...DTA008 is 
		//	disposed and set to null
		if (getMediator()
			.getDesktop()
			.getPendingTransPanel()
			.isVisible()
			&& getFrame() != null)
		{
			getFrame().setVisibleRTS(false);
			setFrame(null);
		}
		// end defect 8851

	}
}
