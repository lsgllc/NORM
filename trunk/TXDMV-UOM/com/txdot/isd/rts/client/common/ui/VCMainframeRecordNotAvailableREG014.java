package com.txdot.isd.rts.client.common.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * VCMainframeRecordNotAvailableREG014.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	03/15/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * B Hargrove	04/26/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * Controller for the MainframeRecordNotAvailableREG014 frame.
 * 
 * @version	5.2.3		04/26/2005
 * @author	Joseph Peters
 * <br>Creation Date:	10/19/2001 17:36:17
 */
public class VCMainframeRecordNotAvailableREG014
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	public static final int CONFIRM_DOC_NO = 20;

	/**
	 * VCMainframeRecordNotAvailableREG014 constructor comment.
	 */
	public VCMainframeRecordNotAvailableREG014()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their 
	 * own module name.
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
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
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
					setDirectionFlow(AbstractViewController.NEXT);
					if (getTransCode().equals(TransCdConstant.RENEW)
						|| getTransCode().equals(TransCdConstant.EXCH)
						|| getTransCode().equals(TransCdConstant.CORREG)
						|| getTransCode().equals(TransCdConstant.PAWT)
						|| getTransCode().equals(TransCdConstant.REPL)
						|| getTransCode().equals(TransCdConstant.DUPL)
						|| getTransCode().equals(TransCdConstant.ADDR))
					{
						setNextController(ScreenConstant.REG033);
					}
					else if (
						getTransCode().equals(TransCdConstant.TITLE))
					{
						setNextController(ScreenConstant.TTL002);
					}
					else if (
						getTransCode().equals(TransCdConstant.TAWPT))
					{
						setNextController(ScreenConstant.MRG010);
					}
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}

			case VCMainframeRecordNotAvailableREG014.CONFIRM_DOC_NO :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.CTL006);
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}

			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							null);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
		}
	}
	/**
	 * Sets the frame's view
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
			Dialog laRTSDB = getMediator().getParent();
			if (laRTSDB == null)
			{
				setFrame(
					new FrmMainframeRecordNotAvailableREG014(
						getMediator().getDesktop()));
			}
			else
			{
				setFrame(
					new FrmMainframeRecordNotAvailableREG014(laRTSDB));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
