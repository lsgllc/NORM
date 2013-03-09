package com.txdot.isd.rts.client.funds.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCReportSelectionFUN007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	06/30/2005	Modify code for move to Java 1.4.
 *							modify for WSAD. Bring code to standards.
 * 	 						Use new getters\setters for frame, 
 *							controller, etc. Remove any unused methods,
 *							variables, arguments, imports.
 * 							Chg '/**' to '/*' to begin prolog.
 * 							defect 7886 Ver 5.2.3 
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */

/**
 * VCCash Drawer Selection Screen FUN007
 * View conrtoller handles the control operation of screens.
 *
 * @version	5.2.4		08/11/2006
 * @author	Bobby Tulsiani
 * <br>Creation Date:	09/05/2001 13:30:59
 */

public class VCReportSelectionFUN007 extends AbstractViewController
{
	public final static int DATE_RANGE = 9;
	/**
	 * VCReportSelectionFUN007 constructor comment.
	 */
	public VCReportSelectionFUN007()
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
		return GeneralConstant.FUNDS;
	}
	
	// defect 7886
	// remove unused method
	///**
	// * Handles any errors that may occur
	// * 
	// * @param RTSException leRTSEx
	// */
	//public void handleError(
	//	com.txdot.isd.rts.services.exception.RTSException leRTSEx)
	//{
	//}
	// end defect 7886
	
	/**
	 * All subclasses must override this method to handle data coming
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the
	 * RTSMediator
	 *
	 * @param aiCommand int
	 * @param aaData Object 
	 */

	public void processData(int aiCommand, Object aaData)
	{
		FundsData laDataB = (FundsData) aaData;
		switch (aiCommand)
		{
			//Goto report viewer if display reports option is selected 
			// defect 7886
			//case (ENTER) :
			case (AbstractViewController.ENTER) :
			{
			// end defect 7886
				this.setData(aaData);
				if (laDataB.getFundsReportData().isDisplayReports())
				{
					// defect 7886
					//nextController = ScreenConstant.RPR000;
					//directionFlow = AbstractViewController.NEXT;
					setNextController(ScreenConstant.RPR000);
					setDirectionFlow(AbstractViewController.NEXT);
					// end defect 7886
					try
					{

						// defect 7886
						//mediator.processData(
						getMediator().processData(
						// end defect 7886
							getModuleName(),
							FundsConstant.DISPLAY_REPORTS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
				}
				//Else goto FUN013
				else
				{
					// defect 7886
					//nextController = ScreenConstant.FUN013;
					//directionFlow = AbstractViewController.NEXT;
					setNextController(ScreenConstant.FUN013);
					setDirectionFlow(AbstractViewController.NEXT);
					// end defect 7886
					try
					{

						// defect 7886
						//mediator.processData(
						getMediator().processData(
						// end defect 7886
							getModuleName(),
							FundsConstant.PRINT_REPORTS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
				}
				break;

				//Cancel screen, and goto previous one
			// defect 7886
			}
			//case (CANCEL) :
			case (AbstractViewController.CANCEL) :
			{
				//directionFlow = AbstractViewController.PREVIOUS;
				setDirectionFlow(AbstractViewController.PREVIOUS);
				// end defect 7886
				try
				{
					// defect 7886
					//mediator.processData(
					getMediator().processData(
					// end defect 7886
						getModuleName(),
						FundsConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisibleRTS(false);
				// end 8884
				break;
			}
			//Get single workstation to display on table
			case (FundsConstant.GET_CASH_DRAWER) :
			{
				this.setData(aaData);
				// defect 7886
				//directionFlow = AbstractViewController.CURRENT;
				setDirectionFlow(AbstractViewController.CURRENT);
				// end defect 7886
				try
				{
					// defect 7886
					//mediator.processData(
					getMediator().processData(
					// end defect 7886
						getModuleName(),
						FundsConstant.GET_CASH_DRAWER,
						aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				if (getFrame() != null)
				{
					// defect 8884
					// use close() so that it does setVisibleRTS()
					close();
					//getFrame().setVisible(false);
					// end 8884
				}
				break;
			}
			//Fire Date Range Screen FUN003
			case (DATE_RANGE) :
			{
				// defect 7886
				//nextController = ScreenConstant.FUN003;
				//directionFlow = AbstractViewController.NEXT;
				setNextController(ScreenConstant.FUN003);
				setDirectionFlow(AbstractViewController.NEXT);
				// end defect 7886
				try
				{
					// defect 7886
					//mediator.processData(
					getMediator().processData(
					// end defect 7886
						getModuleName(),
						FundsConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException leRTSEx)
				{
					leRTSEx.displayError(getFrame());
				}
				break;
			}
			// defect 7886
			// Help is launched from the frame
			//case (HELP) :
			//	{
			//		// empty code block
			//	}
			// end defect 7886
		}
	}
	/**
	 * Retrieves apporpriate data if necessary, displays frame, 
	 * and passes control.
	 * 
	 * @param avPreviousControllers java.util.Vector containing the 
	 *   names of the previous controllers 
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
	 */

	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		FundsData laFundsData = (FundsData) aaData;

		//Get user workstation data, if workstation is not a server
		//(FUN001 not displayed)
		if (laFundsData.getCashDrawers() == null
			&& laFundsData.getFundsReportData().getEntity()
				== FundsConstant.CASH_DRAWER)
		{
			this.setTransCode(asTransCode);
			processData(FundsConstant.GET_CASH_DRAWER, laFundsData);
			return;
		}
		//After returning single drawer set it into selected cash drawers vector
		else if (laFundsData.getSelectedCashDrawers() == null
				&& laFundsData.getFundsReportData().getEntity()
				== FundsConstant.CASH_DRAWER)
		{
			laFundsData.setSelectedCashDrawers(
				laFundsData.getCashDrawers());
		}
		//If frame has not been displayed	
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDiagBox = getMediator().getParent();
			if (laRTSDiagBox != null)
			{
				setFrame(new FrmReportSelectionFUN007(
					laRTSDiagBox));
			}
			else
			{
				setFrame(new FrmReportSelectionFUN007(
					getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
