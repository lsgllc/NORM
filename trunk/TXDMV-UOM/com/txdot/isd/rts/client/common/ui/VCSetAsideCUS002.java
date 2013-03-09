package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;

/*
 *
 * VCSetAsideCUS002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	03/08/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * B Hargrove	04/26/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * View Controller for Screen: Set Aside CUS002
 * 
 * @version	5.2.3		04/26/2005
 * @author	Nancy Ting
 * <br>Createion Date:	10/05/2001 10:56:30
 */
public class VCSetAsideCUS002
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCSetAsideCUS002 constructor.
	 */
	public VCSetAsideCUS002()
	{
		super();
	}
	/**
	 * Returns the Module name constant used by the RTSMediator to 
	 * pass the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return com
			.txdot
			.isd
			.rts
			.services
			.util
			.constants
			.GeneralConstant
			.COMMON;
	}
	/**
	 * Controls the screen flow from CUS002.  It passes the data to 
	 * the RTSMediator.
	 * 
	 * @param aiCommand int  
	 * @param aaData Object  
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setDirectionFlow(AbstractViewController.DESKTOP);
					try
					{
						getMediator().processData(
							getModuleName(),
							com
								.txdot
								.isd
								.rts
								.services
								.util
								.constants
								.CommonConstant
								.SET_ASIDE,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
						return;
					}
					getFrame().setVisibleRTS(false);
				}
				break;
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						getMediator().processData(
							getModuleName(),
							com
								.txdot
								.isd
								.rts
								.services
								.util
								.constants
								.CommonConstant
								.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
						return;
					}
					getFrame().setVisibleRTS(false);
				}
				break;
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables 
	 * needed by the VC, and sends the data to the frame.
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
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmSetAsideCUS002(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmSetAsideCUS002(getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
