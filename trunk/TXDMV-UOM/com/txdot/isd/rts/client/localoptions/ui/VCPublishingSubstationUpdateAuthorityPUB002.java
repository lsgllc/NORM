package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;

/*
 *
 * VCPublishingSubstationUpdateAuthorityPUB002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		03/03/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 *							defect 7891  Ver 5.2.3
 * --------------------------------------------------------------------- 
 */

/**
 * Controller for Publishing Substation Update Authority screen PUB002 
 * 
 * @version	5.2.3		03/03/2005
 * @author 	Ashish Mahajan
 * <br>Creation Date:	10/10/2001 11:12:38 	  
 */
public class VCPublishingSubstationUpdateAuthorityPUB002
	extends AbstractViewController
{
	public static final int REVISE = 5;
	/**
	 * VCPublishingSubstationUpdateAuthorityPUB002 constructor comment.
	 */
	public VCPublishingSubstationUpdateAuthorityPUB002()
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
		return GeneralConstant.LOCAL_OPTIONS;
	}
	/**
	 * Handles data coming from their JDialogBox - inside the subclasses 
	 * implementation should be calls to fireRTSEvent() to pass the data 
	 * to the RTSMediator.
	 * 
	 * @param aiCommand int
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		try
		{
			switch (aiCommand)
			{
				case CANCEL :
					{
						setDirectionFlow(AbstractViewController.FINAL);
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
						getFrame().setVisibleRTS(false);
						break;
					}
				case REVISE :
					{
						//this.data = data;
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.REVISE_PUB,
							aaData);
						break;
					}
				case SEARCH :
					{
						setData(aaData);
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.PUB_UPDT,
							aaData);
						break;
					}
			}
		}
		catch (RTSException leRTSEx)
		{
			leRTSEx.displayError(getFrame());
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
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
			Dialog laDialog = getMediator().getParent();
			if (laDialog != null)
			{
				setFrame(
					new FrmPublishingSubstationUpdateAuthorityPUB002(laDialog));
			}
			else
			{
				setFrame(
					new FrmPublishingSubstationUpdateAuthorityPUB002(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
