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
 * VCDisabledPersonInformationMRG022.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	11/01/2008	Modify to handle Server/DB Exceptions
 * 							add handle_DB_Server_Down 
 * 							modify processData()  
 * 							defect 9831 Ver POS_Defect_B  
 * ---------------------------------------------------------------------
 */

/**
 * View controller for FrmDisabledPersonInformationMRG022
 *
 * @version	POS_Defect_B	11/01/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008
 */

public class VCDisabledPersonInformationMRG022
	extends AbstractViewController
{
	public final static int INSERT = 20;
	public final static int UPDATE = 21;
	public final static int MRG023 = 23;
	public final static int RELEASE = 24;
	public final static int RELEASECANCEL = 25;

	/**
	 * VCDisabledPersonInformationMRG022 default constructor.
	 */
	public VCDisabledPersonInformationMRG022()
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
	 * Sets error message and navigation for Mainframe Down / DB Down 
	 * scenario
	 */
	public void handle_DB_Server_Down(RTSException aeRTSEx)
	{
		if (aeRTSEx.getMsgType().equals(RTSException.DB_DOWN)
			|| aeRTSEx.getMsgType().equals(RTSException.SERVER_DOWN))
		{
			aeRTSEx.setCode(618);
			aeRTSEx.displayError(getFrame());
		}
		else
		{
			aeRTSEx.displayError(getFrame());
		}
		processData(AbstractViewController.CANCEL, null);
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
			case MiscellaneousRegConstant.INSERT :
				{
					setData(aaData);
					setDirectionFlow(
						AbstractViewController.DIRECT_CALL);
					try
					{
						aaData =
							getMediator().processData(
								getModuleName(),
								aiCommand,
								aaData);

						processData(MRG023, aaData);
					}
					catch (RTSException aeRTSEx)
					{
						handle_DB_Server_Down(aeRTSEx);
					}
					break;
				}
			case MiscellaneousRegConstant.UPDATE :
				{
					setData(aaData);
					setDirectionFlow(
						AbstractViewController.DIRECT_CALL);
					try
					{
						getMediator().processData(
							getModuleName(),
							aiCommand,
							aaData);

						processData(MRG023, aaData);
					}
					catch (RTSException aeRTSEx)
					{
						processData(RELEASE, aaData);
					}
					break;
				}
			case MRG023 :
				{
					setNextController(ScreenConstant.MRG023);
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
			case RELEASE :
				{
					setData(aaData);
					setDirectionFlow(
						AbstractViewController.DIRECT_CALL);
					try
					{
						getMediator().processData(
							getModuleName(),
							MiscellaneousRegConstant.RESETINPROCS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						handle_DB_Server_Down(aeRTSEx);
					}
					break;
				}
			case RELEASECANCEL :
				{
					setData(aaData);
					setDirectionFlow(
						AbstractViewController.DIRECT_CALL);
					try
					{
						getMediator().processData(
							getModuleName(),
							MiscellaneousRegConstant.RESETINPROCS,
							aaData);

						processData(
							AbstractViewController.CANCEL,
							null);
					}
					catch (RTSException aeRTSEx)
					{
						handle_DB_Server_Down(aeRTSEx);
					}
					break;

				}
			case AbstractViewController.CANCEL :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						setPreviousController(ScreenConstant.MRG020);
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
					new FrmDisabledPersonInformationMRG022(laRTSDialogBox));
			}
			else
			{
				setFrame(
					new FrmDisabledPersonInformationMRG022(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
