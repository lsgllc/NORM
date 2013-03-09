package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.PresumptiveValueData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCNoTitleRecordTTL004.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
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
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.   
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbage.
 * 							defect 7898 Ver 5.2.3    
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * T Pederson	09/08/2006	Added case for presumptive value to 
 * 							processData()
 *							modify processData()
 * 							defect 8926 Ver 5.2.5
 * K Harrell	09/14/2006	Add code for handling exceptions during
 * 							presumptive value call 
 * 							modify processData()
 * 							defect 8926 Ver 5.2.5	
 * T Pederson	09/15/2006	Changed direction flow to DIRECT_CALL. 
 *							modify processData()
 * 							defect 8926 Ver 5.2.5
 * K Harrell	07/11/2010	add VTR_AUTH
 * 							modify processData() 
 * 							defect 10507 Ver 6.5.0 
 * ---------------------------------------------------------------
 */

/**
 * VC used to capture vehicle information of a new vehicle
 * applying for title.  
 * 
 * @version 6.5.0			07/11/2010
 * @author	Administrator
 * <br>Creation Date: 		08/22/01 11:33:56
 */

public class VCNoTitleRecordTTL004
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	// defect 8926
	public final static int PRESUMP_VAL = 22;
	// end defect 8926

	// defect 10507 
	public final static int VTR_AUTH = 23;
	// end defect 10507 

	/**
	 * VCNoTitleRecordTTL004 constructor comment.
	 */
	public VCNoTitleRecordTTL004()
	{
		super();
	}

	/**
	 * All subclasses must overrid20e this method to return their own 
	 * module name.
	 * 
	 * @return int
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
		// @ Empty block of code
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
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			// defect 10507 
			case VTR_AUTH :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.CTL003);
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
			// end defect 10507
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					setPreviousController(ScreenConstant.TTL002);
					try
					{
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}

						if (getTransCode()
							.equals(TransCdConstant.DTANTD)
							|| getTransCode().equals(
								TransCdConstant.DTANTK)
							|| getTransCode().equals(
								TransCdConstant.DTAORD)
							|| getTransCode().equals(
								TransCdConstant.DTAORK))
						{
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								new RTSException());
						}
						else
						{
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								aaData);
						}

					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case ENTER :
				{
					try
					{
						setData(aaData);
						VehicleInquiryData laVehData =
							(VehicleInquiryData) aaData;

						//copy the data object
						VehicleInquiryData laCopyVehData =
							(VehicleInquiryData) UtilityMethods.copy(
								laVehData);
						setNextController(ScreenConstant.TTL007);
						setDirectionFlow(AbstractViewController.NEXT);
						getMediator()
							.processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
						/* data*/
						laCopyVehData);
					}
					catch (RTSException aeRTSEx)
					{
						// Empty catch box
					}
					break;
				}
				// defect 8926
			case PRESUMP_VAL :
				{
					try
					{
						setNextController(ScreenConstant.TTL004);
						setDirectionFlow(
							AbstractViewController.DIRECT_CALL);

						Object laObject =
							getMediator().processData(
								getModuleName(),
								TitleConstant.GET_PRIVATE_PARTY_VALUE,
								aaData);
						if (laObject instanceof PresumptiveValueData)
						{
							setDirectionFlow(
								AbstractViewController.NEXT);
							setNextController(ScreenConstant.TTL045);
							getMediator().processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
								(PresumptiveValueData) laObject);
						}
						else
						{
							RTSException leRTSEx =
								new RTSException(
									ErrorsConstant
										.ERR_NUM_SPV_SVC_UNAVAILABLE);
							leRTSEx.displayError(getFrame());
						}
					}
					catch (RTSException aeRTSEx)
					{
						RTSException leRTSEx =
							new RTSException(
								ErrorsConstant
									.ERR_NUM_SPV_SVC_UNAVAILABLE);
						leRTSEx.displayError(getFrame());
					}
					break;
				}
				// end defect 8926
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
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDBox =
				getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmNoTitleRecordTTL004(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmNoTitleRecordTTL004(
						getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
