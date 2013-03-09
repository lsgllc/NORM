package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * 
 * VCLienDisplayTTL005.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * 
 * ---------------------------------------------------------------------
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		06/28/2005	Code clean up.
 * 							Remove unused imports, variables
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.   
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbage.
 * 							defect 7898 Ver 5.2.3    
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/06/2007	Add code to cancel.
 * 							modify processData()
 * 							defect 9086 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * View Controller TTL005: Existing liens on Title
 * 
 * @version Special Plates	04/06/2007	
 * @author: Ashish Mahajan
 * <br>Creation Date: 		8/22/01 11:15:41
 */

public class VCLienDisplayTTL005
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	/**
	 * VCLienDisplayTTL005 constructor comment.
	 */
	public VCLienDisplayTTL005()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 * 
	 * @return java.lang.String
	 */
	public int getModuleName()
	{
		return GeneralConstant.TITLE;
	}
	/**
	 * Handles any errors that may occur
	 * 
	 * @param aeRTSEx RTSException
	 */
	public void handleError(
		com.txdot.isd.rts.services.exception.RTSException aeRTSEx)
	{
		// empty block of code
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
		if (getFrame() != null)
		{
			getFrame().setVisibleRTS(false);
		}
		try
		{
			switch (aiCommand)
			{
				case ENTER :
					{
						setData(aaData);
						VehicleInquiryData vehData =
							(VehicleInquiryData) aaData;
						String strTransCd = getTransCode();

						if (strTransCd.equals(TransCdConstant.VEHINQ))
						{
							setDirectionFlow(
								AbstractViewController.CANCEL);
							try
							{
								getMediator().processData(
									GeneralConstant.INQUIRY,
									TitleConstant.NO_DATA_TO_BUSINESS,
									aaData);
							}
							catch (RTSException aeRTSEx)
							{
								aeRTSEx.displayError(getFrame());
							}
						}
						else
						{
							//copy the data object
							TitleValidObj laValidObj =
								(TitleValidObj) vehData
									.getValidationObject();
							laValidObj.setDoneTTL005(true);
							try
							{
								setDirectionFlow(
									AbstractViewController.CANCEL);
								getMediator().processData(
									getModuleName(),
									TitleConstant.NO_DATA_TO_BUSINESS,
									aaData);
								if (getFrame() != null)
								{
									getFrame().setVisibleRTS(false);
								}
							}
							catch (RTSException aeRTSEx)
							{
								aeRTSEx.displayError(getFrame());
							}
						}
						break;
					}
				case CANCEL :
					{
						setDirectionFlow(AbstractViewController.CANCEL);
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
						break;
					}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}

	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector A vector containing
	 *  the String names of the previous controllers in order
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDBox =
				getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmLienDisplayTTL005(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmLienDisplayTTL005(
						getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
