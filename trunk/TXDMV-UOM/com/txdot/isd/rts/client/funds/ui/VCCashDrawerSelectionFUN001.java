package com.txdot.isd.rts.client.funds.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * VCCashDrawerSelectionFUN001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	07/12/2005	Modify code for move to Java 1.4.
 *							modify for WSAD. Bring code to standards.
 * 	 						Use new getters\setters for frame, 
 *							controller, etc. Remove any unused methods,
 *							variables, arguments, imports.
 * 							Chg '/**' to '/*' to begin prolog.
 * 							defect 7886 Ver 5.2.3
 * K Harrell	03/17/2006	Streamline tests for next screen
 * 							modify processData(),setView()
 * 							defect 8623 Ver 5.2.3
 * K Harrell	08/16/2009	Implement SystemProperty.isClientServer()
 * 							modify setView() 
 * 							defect 8628 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */

/**
 * VCCash Drawer Selection Screen FUN001
 * View conrtoller handles the control operation of screens.
 *
 * @version	Defect_POS_F	08/16/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class VCCashDrawerSelectionFUN001 extends AbstractViewController
{
	public final static int SKIP_FUN001 = 5;

	/**
	 * VCCashDrawerSelectionFUN001 constructor comment.
	 */
	public VCCashDrawerSelectionFUN001()
	{
		super();
	}
	/**
	 * Close the frame.
	 */
	public void close()
	{
		if (getFrame() != null)
		{
			getFrame().setVisibleRTS(false);
			getFrame().dispose();
		}
	}
	/**
	 * All subclasses must override this method to return
	 * their own module name=FUNDS
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.FUNDS;
	}

	/**
	 * All subclasses must override this method to handle data coming
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fire RTSEvent() to pass the data to the
	 * RTSMediator.
	 *
	 * @param aiCommand int
	 * @param aaData Object 
	 */

	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case (ENTER) :
				this.setData(aaData);
				setDirectionFlow(AbstractViewController.NEXT);

				//If  CLOSEOUT, go to FUN014
				if (getTransCode().equals(TransCdConstant.CLOSE))
				{
					setNextController(ScreenConstant.FUN014);
				}
				// REPORTS: go to FUN007
				// defect 8623 
				// else	if (!getTransCode().equals(TransCdConstant.CLOSE))
				// end defect 8623     
				else
				{
					setNextController(ScreenConstant.FUN007);
				}
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

			case (CANCEL) :
				//If in Closeout, FUN001 closes all screens
				if (getTransCode().equals(TransCdConstant.CLOSE))
				{
					setDirectionFlow(AbstractViewController.FINAL);
					getFrame().setVisibleRTS(false);
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
				// REPORTS: FUN001 returns to prior screen
				// defect 8623 
				// else if (!getTransCode().equals(TransCdConstant.CLOSE))
				// end defect 8623 
				else
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					getFrame().setVisibleRTS(false);
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

				//Get data from DB to send to FrmFUN001
			case (FundsConstant.GET_ALL_CASH_DRAWERS) :
				setDirectionFlow(AbstractViewController.CURRENT);
				try
				{
					getMediator().processData(
						getModuleName(),
						FundsConstant.GET_ALL_CASH_DRAWERS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;

			// SERVER: Skip FUN001
			case (SKIP_FUN001) :
				setDirectionFlow(AbstractViewController.NEXT);

				if (getTransCode().equals(TransCdConstant.CLOSE))
				{
					setNextController(ScreenConstant.FUN014);
				}
				// defect 8623 
				// else if (!getTransCode().equals(TransCdConstant.CLOSE))
				// end defect 8623 
				else
				{
					setNextController(ScreenConstant.FUN007);
				}
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

			case (FINAL) :
				setNextController(ScreenConstant.FUN006);
				setDirectionFlow(AbstractViewController.PREVIOUS);
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

			default :
				{
					// empty code block
				}
		}

	}
	/**
	 * Retrieves appropriate data if necessary, stores variables,
	 * displays frame, and passes control.
	 *
	 * @param avPreviousControllers Vector containing the 
	 *   names of the previous controllers 
	 * @param asTransCode String The TransCode
	 * @param aaData Object 
	 */

	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		//Display frame
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDiag = getMediator().getParent();
			if (laRTSDiag != null)
			{
				setFrame(new FrmCashDrawerSelectionFUN001(laRTSDiag));
			}
			else
			{
				setFrame(
					new FrmCashDrawerSelectionFUN001(
						getMediator().getDesktop()));
			}
		}

		// CLOSEOUT 
		if (asTransCode.equals(TransCdConstant.CLOSE))
		{
			// defect 8628 
			if (SystemProperty.isClientServer())
			{
				// end defect 8628 
				//If data has not been retrieved, get from DB
				if (aaData == null)
				{
					this.setTransCode(asTransCode);
					FundsData laFundsData = new FundsData();
					processData(
						FundsConstant.GET_ALL_CASH_DRAWERS,
						laFundsData);
					return;
				}
			}
			else
			{
				this.setTransCode(asTransCode);
				processData(SKIP_FUN001, aaData);
				return;
			}

			super.setView(avPreviousControllers, asTransCode, aaData);

		}
		// NOT CLOSEOUT
		else
		{
			FundsData laFundsData = (FundsData) aaData;

			// defect 8628  
			if (SystemProperty.isClientServer())
			{
				// end defect 8628  
				//If data has not been retrieved
				if (laFundsData.getCashDrawers() == null)
				{
					this.setTransCode(asTransCode);
					processData(
						FundsConstant.GET_ALL_CASH_DRAWERS,
						laFundsData);
					return;
				}
			}
			// WORKSTATION && ! FUN007 
			else if (!laFundsData.isDisplayedFUN007())
			{
				this.setTransCode(asTransCode);
				processData(SKIP_FUN001, aaData);
				return;
			}
			// WORKSTATION: FUN007 is displayed, hit "CANCEL" on FUN001
			else
			{
				this.setTransCode(asTransCode);
				processData(FINAL, aaData);
				return;
			}

			super.setView(avPreviousControllers, asTransCode, aaData);

		}

	}
}
