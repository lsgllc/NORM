package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

/*
 * VCReinstateDisabledPlacardMRG029.java
 *
 * (c) Texas Department of Motor Vehicles 2012
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/12/2012	Created
 * 							defect 11214 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/** 
 * View controller for FrmReinstateDisabledPlacardMRG029 
 *
 * 
 * @version	6.10.0		01/12/2012 
 * @author	Kathy Harrell 
 * @since				01/12/2012	17:16:17 
 */
public class VCReinstateDisabledPlacardMRG029 extends
		AbstractViewController
{
	public final static int REDIRECT = 30;

	/**
	 * VCReinstateDisabledPlacardMRG029 default constructor.
	 */
	public VCReinstateDisabledPlacardMRG029()
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
	 * @param aiCommand
	 *            int
	 * @param aaData
	 *            Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
		case AbstractViewController.ENTER:
		{
			try
			{
				setDirectionFlow(AbstractViewController.CURRENT);
				getMediator().processData(GeneralConstant.COMMON,
						CommonConstant.GET_NEXT_COMPLETE_TRANS_VC,
						aaData);
			}
			catch (RTSException aeRTSEx)
			{
				aeRTSEx.displayError(getFrame());
			}
			break;
		}

		case AbstractViewController.CANCEL:
		{
			setDirectionFlow(AbstractViewController.CANCEL);
			try
			{
				getMediator().processData(getModuleName(),
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

		case REDIRECT:
		{
			try
			{
				Vector lvNextVC = (Vector) aaData;
				String lsNextVCName = (String) lvNextVC.get(0);
				// first element is name of next controller
				CompleteTransactionData laData = (CompleteTransactionData) lvNextVC
				.get(1);
				if (lsNextVCName != null)
				{
					setNextController(lsNextVCName);
					setDirectionFlow(AbstractViewController.NEXT);
					getMediator()
					.processData(
							GeneralConstant.COMMON,
							MiscellaneousRegConstant.NO_DATA_TO_BUSINESS,
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
	 * @param avPreviousControllersVector
	 * @param asTransCode
	 *            String
	 * @param aaData
	 *            Object
	 */
	public void setView(Vector avPreviousControllers,
			String asTransCode, Object aaData)
	{

		if (getFrame() == null)
		{
			RTSDialogBox laRTSDialogBox = getMediator().getParent();
			if (laRTSDialogBox != null)
			{

				setFrame(new FrmReinstateDisabledPlacardMRG029(
						laRTSDialogBox));
			}
			else
			{
				setFrame(new FrmReinstateDisabledPlacardMRG029(
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
			super.setView(avPreviousControllers, asTransCode, aaData);
	}
}

