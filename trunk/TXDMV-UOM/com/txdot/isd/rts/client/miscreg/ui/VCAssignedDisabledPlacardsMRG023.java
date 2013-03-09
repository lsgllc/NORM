package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCAssignedDisabledPlacardsMRG023.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * ---------------------------------------------------------------------
 */

/**
 * View controller for FrmAssignedDisabledPlacardsMRG023 
 *
 * @version	POS_Defect_B	10/27/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008
 */

public class VCAssignedDisabledPlacardsMRG023
	extends AbstractViewController
{
	public final static int MRG024 = 24;
	public final static int MRG025 = 25;
	public final static int MRG026 = 26;
	public final static int CHKIFVOIDABLE = 27;
	public final static int MRG028 = 28;
	public final static int MRG029 = 29;

	/**
	 * VCAssignedDisabledPlacardsMRG023 default constructor.
	 */
	public VCAssignedDisabledPlacardsMRG023()
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
			case CHKIFVOIDABLE :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						aaData =
							getMediator().processData(
								getModuleName(),
								MiscellaneousRegConstant.CHKIFVOIDABLE,
								aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case MRG024 :
				{
					setNextController(ScreenConstant.MRG024);
					setDirectionFlow(AbstractViewController.NEXT);

					try
					{
						getMediator().processData(
							getModuleName(),
							MiscellaneousRegConstant
								.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case MRG025 :
				{
					setNextController(ScreenConstant.MRG025);
					setDirectionFlow(AbstractViewController.NEXT);

					try
					{
						getMediator().processData(
							getModuleName(),
							MiscellaneousRegConstant
								.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case MRG026 :
				{
					setNextController(ScreenConstant.MRG026);
					setDirectionFlow(AbstractViewController.NEXT);

					try
					{
						getMediator().processData(
							getModuleName(),
							MiscellaneousRegConstant
								.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case MRG028 :
			{
				setNextController(ScreenConstant.MRG028);
				setDirectionFlow(AbstractViewController.NEXT);

				try
				{
					getMediator().processData(
						getModuleName(),
						MiscellaneousRegConstant
							.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}
			case MRG029 :
			{
				setNextController(ScreenConstant.MRG029);
				setDirectionFlow(AbstractViewController.NEXT);

				try
				{
					getMediator().processData(
						getModuleName(),
						MiscellaneousRegConstant
							.NO_DATA_TO_BUSINESS,
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
					setDirectionFlow(AbstractViewController.PREVIOUS);
					setPreviousController(ScreenConstant.MRG022);
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
					new FrmAssignedDisabledPlacardsMRG023(laRTSDialogBox));
			}
			else
			{
				setFrame(
					new FrmAssignedDisabledPlacardsMRG023(
						getMediator().getDesktop()));
			}
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		// Return from MiscRegServerBusiness
		// Have determined if available for void on Replace / Delete   
		else if (aaData instanceof Vector)
		{
			Vector lvVector = (Vector) aaData;

			boolean lbAvailableForVoid =
				((Boolean) (lvVector.elementAt(2))).booleanValue();

			if (lbAvailableForVoid)
			{
				String lsTransId = (String) lvVector.elementAt(3);
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						"This transaction must be voided.   Transaction Id: "
							+ lsTransId
							+ ".",
						"ERROR");
				leRTSEx.displayError(getFrame());
			}
			else
			{
				aaData = lvVector.firstElement();
				int liProcess =
					((Integer) lvVector.elementAt(1)).intValue();
				processData(liProcess, aaData);
			}
		}
	}
}