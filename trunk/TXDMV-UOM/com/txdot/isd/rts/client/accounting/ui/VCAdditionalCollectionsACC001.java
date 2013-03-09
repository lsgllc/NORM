package com.txdot.isd.rts.client.accounting.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.AccountingConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCAdditionalCollectionsACC001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	09/07/2001  Added comments
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	03/02/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3
 * Ray Rowehl	03/21/2005	use getters and setters for parent fields
 * 							defect 7884 Ver 5.2.3
 * K Harrell	07/26/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3
 * --------------------------------------------------------------------- 
 */
/**
 * The View Controller for the ACC001 screen.  It handles screen 
 * navigation and controls the visibility of its frame.
 *
 * @version	5.2.3		07/26/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	07/03/2001 07:52:44
 */
public class VCAdditionalCollectionsACC001
	extends AbstractViewController
{
	// Constant 
	// Constant to retrieve the account codes
	public final static int RETRIEVE_CODES = 20;
	
	/**
	 * Creates a VCAdditionalCollectionsACC001.
	 */
	public VCAdditionalCollectionsACC001()
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
		return GeneralConstant.ACCOUNTING;
	}
	/**
	 * Controls the screen flow from ACC001.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand	int 
	 * @param aaData 	Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setData(aaData);
					setNextController(ScreenConstant.PMT004);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.NO_DATA_TO_BUSINESS,
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
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
			case RETRIEVE_CODES :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.RETRIEVE_ACCOUNT_CODES,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers	Vector  
	 * @param asTransCode 			String
	 * @param aaData 				Object 
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (aaData == null)
		{
			setTransCode(asTransCode);
			processData(RETRIEVE_CODES, aaData);
			return;
		}
		if (getFrame() == null)
		{
			java.awt.Dialog laDialog = getMediator().getParent();
			if (laDialog == null)
			{
				setFrame(
					new FrmAdditionalCollectionsACC001(
						getMediator().getDesktop()));
			}
			else
			{
				setFrame(new FrmAdditionalCollectionsACC001(laDialog));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
