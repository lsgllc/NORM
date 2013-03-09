package com.txdot.isd.rts.client.common.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;

/*
 *
 * VCVTRAuthorizationSSNCTL010.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	03/07/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * B Hargrove	04/26/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * VC VTRAuthorization SSNCTL010
 * 
 * @version	5.2.3		04/26/2005
 * @author	Nancy Ting
 * <br>Creation Date:	10/08/2001 11:54:00
 */
public class VCVTRAuthorizationSSNCTL010
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	/**
	 * VCVTRAuthorizationCTL003 constructor.
	 */
	public VCVTRAuthorizationSSNCTL010()
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
	 * Controls the screen flow from CTL010.  It passes the data to 
	 * the RTSMediator.
	 *   
	 * @param aiCommand int  
	 * @param aaData Object  
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				setDirectionFlow(AbstractViewController.PREVIOUS);
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
				}
				getFrame().setVisibleRTS(false);
				break;
			case AbstractViewController.CANCEL :
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
				}
				getFrame().setVisibleRTS(false);
				break;
			default :
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
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDB =
				getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmVTRAuthorizationSSNCTL010(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmVTRAuthorizationSSNCTL010(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
