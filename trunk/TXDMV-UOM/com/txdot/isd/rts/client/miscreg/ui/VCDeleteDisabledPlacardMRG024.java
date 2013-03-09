package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

/*
 * VCDeleteDisabledPlacardMRG024.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 *  K Harrell	10/27/2008	Created.
 * 							defect 9831 Ver Defect_POS_B 
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for FrmDeleteDisabledPlacardMRG024
 *
 * @version Defect_POS_B 	10/27/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008 
 */
public class VCDeleteDisabledPlacardMRG024
	extends AbstractViewController
{
	public final static int REDIRECT = 30;

	/**
	 * VCDeleteDisabledPlacardMRG024 default constructor.
	 */
	public VCDeleteDisabledPlacardMRG024()
	{
		super();
	}

	/**
	 * Get Module Name MISCELLANEOUSREG
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.MISCELLANEOUSREG;
	}

	/**
	 * Process Data
	 * 
	 * @param aiCommand int
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.COMMON,
							CommonConstant.GET_NEXT_COMPLETE_TRANS_VC,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						getMediator().processData(
							getModuleName(),
							RegistrationConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					close();
					break;
				}
			case REDIRECT :
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
								MiscellaneousRegConstant
									.NO_DATA_TO_BUSINESS,
								laData);
						}
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
	 * This method is called by RTSMediator to display the frame.
	 * 
	 * @param avPreviousControllers Vector
	 * @param asTransCode String
	 * @param aaData Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDialogBox = getMediator().getParent();
			if (laRTSDialogBox != null)
			{

				setFrame(
					new FrmDeleteDisabledPlacardMRG024(laRTSDialogBox));
			}
			else
			{
				setFrame(
					new FrmDeleteDisabledPlacardMRG024(
						getMediator().getDesktop()));

			}
		}
		if (aaData instanceof Vector)
		{
			this.setData(aaData);
			this.setPreviousControllers(avPreviousControllers);
			this.setTransCode(asTransCode);
			getFrame().setController(this);
			getFrame().setData(aaData);
		}
		else
		{
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
	}

}
