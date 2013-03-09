package com.txdot.isd.rts.client.funds.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CashWorkstationCloseOutData;
import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCCloseOutWarningFUN014.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	06/30/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 * 							defect 7886 Ver 5.2.3
 * K Harrell	03/17/2006 	Logic streamline
 * 							modify setView()
 * 							defect 8623 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 *							delete close() (use close() from 
 *							AbstractViewController) 
 * 							defect 8884 Ver 5.2.4
 * K Harrell	08/16/2009	Implement SystemProperty.isClientServer()
 * 							modify processData()  
 * 							defect 8628 Ver Defect_POS_F   
 * ---------------------------------------------------------------------
 */

/**
 * CloseOut Warning FUN014
 * View conrtoller handles the control operation of screens.
 *
 * @version	Defect_POS_F 	08/16/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class VCCloseOutWarningFUN014 extends AbstractViewController
{
	public final static int YES = 9;
	public final static int NO = 10;
	public final static int CLOSE_SCREEN = 11;
	public final static int GOTO_FUN002 = 12;

	/**
	 * VCCloseOutWarningFUN014 constructor comment.
	 */
	public VCCloseOutWarningFUN014()
	{
		super();
	}
	
	// defect 8884
	// Use the close() from AbstractViewController
	///**
	// * Close the frame.
	// */
	//public void close()
	//{
	//	if (getFrame() != null)
	//	{
	//		getFrame().setVisibleRTS(false);
	//		getFrame().dispose();
	//	}
	//}
	// end 8884
	
	/**
	 * All subclasses must override this method to return their own
	 * module name=FUNDS
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.FUNDS;
	}

	/**
	 * Handle the user actions.  Upon pressing "Yes", the control should
	 * be passed to screen FUN002.  "No" button destroys screen.
	 *   
	 * @param aiCommand int
	 * @param aaData Object 
	 */

	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			//Go to FUN002
			case (YES) :
			{
				this.setData(aaData);
				setDirectionFlow(NEXT);
				setNextController(ScreenConstant.FUN002);
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}
				//implemented to handle the ESC key 	
			case (CANCEL) :
			{
				if (SystemProperty.isClientServer())
				{
					FundsData laFundsData = new FundsData();
					processData(NO, laFundsData);
				}
				else
				{
					processData(CLOSE_SCREEN, aaData);
				}
				break;
			}
				//Go to previous screen 	
			case (NO) :
			{
				setDirectionFlow(CANCEL);
				try
				{
					if (getFrame() != null)
					{
						// defect 8884
						// use close() so that it does setVisibleRTS()
						close();
						//getFrame().setVisibleRTS(false);
						// end 8884
					}
					getMediator().processData(
						getModuleName(),
						FundsConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
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
			//Get single workstation data to post on table	
			case (FundsConstant.GET_CASH_DRAWER) :
			{
				this.setData(aaData);
				setDirectionFlow(CURRENT);
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant.GET_CASH_DRAWER,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
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
				//Go directly to FUN002 without displaying FUN014
			case (GOTO_FUN002) :
			{
				this.setData(aaData);
				setDirectionFlow(NEXT);
				setNextController(ScreenConstant.FUN002);
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}
				//Close all screens if workstation not a server
			case (CLOSE_SCREEN) :
			{
				this.setData(aaData);
				setDirectionFlow(FINAL);
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant.NO_DATA_TO_BUSINESS,
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
		}
	}
	/**
	 * Retrieves apporpriate data if necessary, displays frame, and
	 * passes control.
	 * 
	 * @param avPreviousControllers java.util.Vector containing the 
	 * names of the previous controllers 
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
	 */

	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		RTSDialogBox laRTSDiagBox = getMediator().getParent();
		if (laRTSDiagBox != null)
		{
			setFrame(new FrmCloseOutWarningFUN014(laRTSDiagBox));
		}
		else
		{
			setFrame(
				new FrmCloseOutWarningFUN014(
					getMediator().getDesktop()));
		}

		// If aaData == null, WORKSTATION; FUN001 not displayed 
		// Get single cash drawer data
		if (aaData == null)
		{
			this.setTransCode(asTransCode);
			FundsData laFundsData = new FundsData();
			processData(FundsConstant.GET_CASH_DRAWER, laFundsData);
			return;
		}
		else
		{
			FundsData laFundsData = (FundsData) aaData;
			if (laFundsData.getSelectedCashDrawers() == null)
			{
				laFundsData.setSelectedCashDrawers(
					laFundsData.getCashDrawers());
			}

			// Initalize that no rows are closed out, and if any are
			// close on Today's date, set to true inside of loop	
			boolean lbClosedOutRows = false;
			int i = 0;
			do
			{
				//create an object to store each selected row	
				CashWorkstationCloseOutData laRow =
					(CashWorkstationCloseOutData) laFundsData
						.getSelectedCashDrawers()
						.get(
						i);
				RTSDate laCloseOutDate = laRow.getCloseOutEndTstmp();
				if (laCloseOutDate.getDate()
					== RTSDate.getCurrentDate().getDate()
					&& laCloseOutDate.getMonth()
						== RTSDate.getCurrentDate().getMonth()
					&& laCloseOutDate.getYear()
						== RTSDate.getCurrentDate().getYear())
				{
					lbClosedOutRows = true;
				}
				i++;
			}
			//break loop while lbClosedOutRows is false and still
			//drawers to check
			while (!lbClosedOutRows
				&& i < laFundsData.getSelectedCashDrawers().size());

			// If going forward in screen navigation (ie. FUN002 not
			// displayed)
			if (!lbClosedOutRows && !laFundsData.isDisplayedFUN002())
			{
				this.setTransCode(asTransCode);
				processData(GOTO_FUN002, laFundsData);
				return;
			}

			// If going backward in screen navigation, and machine is
			// not server
			if (laFundsData.isDisplayedFUN002())
			{
				this.setTransCode(asTransCode);
				
				if (SystemProperty.isClientServer())
				{
					processData(NO, laFundsData);
				}
				else
				{
					processData(CLOSE_SCREEN, laFundsData);
				}
				return;
			}
			else if (lbClosedOutRows == true)
			{
				super.setView(
					avPreviousControllers,
					asTransCode,
					aaData);
			}
		}

	}
}
