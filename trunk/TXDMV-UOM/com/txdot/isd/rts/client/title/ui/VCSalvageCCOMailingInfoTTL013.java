package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * 
 * VCSalvageCCOMailingInfoTTL013.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/10/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		11/07/2005	Update incomplete method headers.   
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbage.
 * 							defect 7898 Ver 5.2.3    
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/06/2008	Always return to TTL016 on cancel
 * 							modify processData()
 * 							defect 9636 Ver POS Defect A 
 * ---------------------------------------------------------------------
 */

/**
 * VC used to capture mailing address for CCO 
 *
 * @version	POS Defect A	06/06/2008
 * @author	Michael Abernethy
 * <br>Creation Date:		08/22/200101 11:39:02 
 */

public class VCSalvageCCOMailingInfoTTL013
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	/**
	 * VCSalvageCCOMailingInfoTTL013 constructor comment.
	 */
	public VCSalvageCCOMailingInfoTTL013()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.TITLE;
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int 
	 * 	the command so the Frame can communicate with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, java.lang.Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					// defect 9636 
					// Always return to TTL016 
					//				if (((VehicleInquiryData) aaData).
					//					isSalvageLienIndi() == true)
					//				{
					//					setDirectionFlow(AbstractViewController.NEXT);
					//					setNextController(ScreenConstant.TTL001);
					//				}
					//				else
					//				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					if (getFrame() != null)
					{
						getFrame().setVisibleRTS(false);
					}
					//}
					// end defect 9636 
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					((VehicleInquiryData) aaData).setShouldGoPrevious(
						true);
					try
					{
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param cvPreviousControllers java.util.Vector
	 * @param asTransCode java.lang.String
	 * @param aaData java.lang.Object
	 */
	public void setView(
		java.util.Vector acPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			java.awt.Dialog laDialog = getMediator().getParent();
			if (laDialog == null)
			{
				setFrame(
					new FrmSalvageCCOMailingInfoTTL013(
						getMediator().getDesktop()));
			}
			else
			{
				setFrame(new FrmSalvageCCOMailingInfoTTL013(laDialog));
			}
		}
		super.setView(acPreviousControllers, asTransCode, aaData);
	}
}
