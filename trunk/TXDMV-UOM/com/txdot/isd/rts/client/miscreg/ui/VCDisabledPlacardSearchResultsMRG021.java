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
 * VCDisabledPlacardSearchResultsMRG021.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	11/01/2008	Modify to handle Server/DB Exceptions.  
 * 							 Moved different handling of Inquiry Event 
 * 							 to Frame. 
 * 							add handle_DB_Server_Down 
 * 							modify processData()  
 * 							defect 9831 Ver POS_Defect_B    
 * ---------------------------------------------------------------------
 */

/**
 * View controller for FrmDisabledPlacardSearchResultsMRG021 
 *
 * @version	POS_Defect_B	11/01/2008 
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008
 */

public class VCDisabledPlacardSearchResultsMRG021
	extends AbstractViewController
{
	private String csTransCd = "";
	public final static int MRG022 = 22;
	public final static int INPROCS = 23;

	/**
	 * VCDisabledPlacardSearchResultsMRG021 default constructor.
	 */
	public VCDisabledPlacardSearchResultsMRG021()
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
			case INPROCS :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.DIRECT_CALL);

						getMediator().processData(
							getModuleName(),
							MiscellaneousRegConstant.SETINPROCS,
							aaData);
						processData(MRG022, aaData);
					}
					catch (RTSException aeRTSEx)
					{
						handle_DB_Server_Down(aeRTSEx);
					}
					break;
				}

			case MRG022 :
				{
					try
					{
						setNextController(ScreenConstant.MRG022);
						setDirectionFlow(AbstractViewController.NEXT);
						close();

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
					close();
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
			csTransCd = asTransCode;

			RTSDialogBox laRTSDialogBox = getMediator().getParent();
			if (laRTSDialogBox != null)
			{

				setFrame(
					new FrmDisabledPlacardSearchResultsMRG021(laRTSDialogBox));
			}
			else
			{
				setFrame(
					new FrmDisabledPlacardSearchResultsMRG021(
						getMediator().getDesktop()));

			}
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
	}
}
