package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCPaymentAccountUpdateOPT004.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		02/28/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 *							defect 7891  Ver 5.2.3
 * Min Wang		09/08/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Jeff S.		06/23/2006	Used screen constant for CTL001 Title.
 * 							remove TXT_CONFM_MSG
 * 							modify processData()
 * 							defect 8756 Ver 5.2.3 
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * --------------------------------------------------------------------- 
 */

/**
 * Controller for Payment Account Update screen OPT004 
 * 
 * @version	5.2.4		08/11/2006
 * @author 	Administrator
 * <br>Creation Date:	11/13/2001 19:06:20 	  
 */

public class VCPaymentAccountUpdateOPT004
	extends AbstractViewController
{
	private static final String TXT_EXIT_MSG = "Exit to Main Menu?";
	// defect 8756
	// Used common constant for CTL001 title
	//private static final String TXT_CONFM_MSG =
	//	"CONFIRM ACTION    CTL001";
	// end defect 8756
	
	/**
	 * VCPaymentAccountUpdateOPT004 constructor comment.
	 */
	public VCPaymentAccountUpdateOPT004()
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
		return GeneralConstant.LOCAL_OPTIONS;
	}

	/**
	 * Handles data coming from their JDialogBox - inside the subclasses
	 * implementation should be calls to fireRTSEvent() to pass the data
	 * to the RTSMediator.
	 * 
	 * @param aiCommand int
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case LocalOptionConstant.GET_PAYMENT_ACCOUNTS :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.GET_PAYMENT_ACCOUNTS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case LocalOptionConstant.ADD_PAYMENT_ACCOUNT :
				{
					setData(aaData);
					setNextController(ScreenConstant.OPT005);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case LocalOptionConstant.REVISE_PAYMENT_ACCOUNT :
				{
					setData(aaData);
					setNextController(ScreenConstant.OPT005);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case LocalOptionConstant.DELETE_PAYMENT_ACCOUNT :
				{
					setData(aaData);
					setNextController(ScreenConstant.OPT005);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case CANCEL :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						// defect 8756
						// Used common constant for CTL001 title
						RTSException leRTSEx =
							new RTSException(
								RTSException.CTL001,
								TXT_EXIT_MSG,
								ScreenConstant.CTL001_FRM_TITLE);
						// end defect 8756
						
						int liRetCode =
							leRTSEx.displayError(getFrame());
						if (liRetCode == RTSException.YES)
							getMediator().processData(
								getModuleName(),
								GeneralConstant.NO_DATA_TO_BUSINESS,
								aaData);
						else
						{
							//frame.gettblPymtAccts().requestFocus();
							break;
						}
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
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
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers Vector 
	 * @param asTransCd String 
	 * @param  aaData object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCd,
		Object aaData)
	{
		if (getFrame() == null)
		{
			Dialog laDialog = getMediator().getParent();
			if (laDialog != null)
			{
				setFrame(new FrmPaymentAccountUpdateOPT004(laDialog));
			}
			else
			{
				setFrame(
					new FrmPaymentAccountUpdateOPT004(
						getMediator().getDesktop()));
			}
			super.setView(avPreviousControllers, asTransCd, aaData);
		}
		else
		{
			getFrame().setData(aaData);
		}
	}
}
