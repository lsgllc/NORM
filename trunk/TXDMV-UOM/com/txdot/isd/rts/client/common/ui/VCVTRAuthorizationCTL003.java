package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 * 
 * VCVTRAuthorizationCTL003.java
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
 * K Harrell	06/14/2010	add PRMT
 * 							modify processData()
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * VC VTRAuthorization CTL003
 * 
 * @version	6.5.0 		06/14/2010
 * @author	Nancy Ting
 * <br>Creation Date:	10/08/2001 11:54:00 
 */
public class VCVTRAuthorizationCTL003 extends AbstractViewController
{
	public static final int PRMT = 23;

	/**
	 * VCVTRAuthorizationCTL003 constructor.
	 */
	public VCVTRAuthorizationCTL003()
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
		return GeneralConstant.COMMON;
	}
	/**
	 * Controls the screen flow from CTL003.  It passes the data to 
	 * the RTSMediator.
	 * 
	 * @param aiCommand int  
	 * @param aaData Object  
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			// defectg 10491 
			case PRMT :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
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
					getFrame().setVisibleRTS(false);
					break;

				}
				// end defect 10491 
			case AbstractViewController.ENTER :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
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
					getFrame().setVisibleRTS(false);
					break;
				}
			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
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
					getFrame().setVisibleRTS(false);
					break;
				}
			default :
				{
					// empty code block
				}
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
				setFrame(new FrmVTRAuthorizationCTL003(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmVTRAuthorizationCTL003(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
