package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;

/*
 *
 * VCSecurityAccessRightsRegSEC006.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang 	02/25/2005	Make basic RTS 5.2.3 changes
 * 							organize imports, format source
 * 							defect 7891 Ver 5.2.3
 * Min Wang		04/16/2005	remove unused method
 * 							delete handleError()
 * 							defect 7891 Ver 5.2.3
 * --------------------------------------------------------------------- 
 */
/**
 * Controller for Security Access Rights Reg SEC006 screen 
 * 
 * @version	5.2.3		04/16/2005
 * @author 	Administrator
 * <br>Creation Date:	08/13/2001 15:22:12 	  
 */
public class VCSecurityAccessRightsRegSEC006
	extends AbstractViewController
{
	/**
	 * VCSecurityAccessRightsRegSEC006 constructor comment.
	 */
	public VCSecurityAccessRightsRegSEC006()
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
// defect 7891 
//	/**
//	 * Handles any errors that may occur
//	 * 
//	 * @param aeRTSEx RTSException
//	 */
//	public void handleError(RTSException aeRTSEx)
//	{
//		// empty code block
//	}
// end defect 7891
	/**
	 * Handles data coming from their JDialogBox - inside the 
	 * subclasses implementation should be calls to fireRTSEvent() to
	 * pass the data to the RTSMediator.
	 * 
	 * @param aiCommand int
	 * @param aaData Object  
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
			case ENTER :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.PREVIOUS);
					try
					{
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
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
			if (laRTSDB != null)
			{
				setFrame(new FrmSecurityAccessRightsRegSEC006(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmSecurityAccessRightsRegSEC006(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
