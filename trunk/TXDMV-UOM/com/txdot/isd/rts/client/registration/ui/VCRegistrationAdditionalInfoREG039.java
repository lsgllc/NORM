package com.txdot.isd.rts.client.registration.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCRegistrationAdditionalInfoREG039.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	07/19/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 *  	 					Use new getters\setters for frame, 
 *							controller, etc.
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */
/**
 * Registration Additional Info (REG039) view controller class. It 
 * handles screen navigation and controls the visibility of its frame.
 *
 * @version	5.2.4		08/11/2006
 * @author	Joseph Kwik
 * <br>Creation Date:	10/8/2001 11:54:00
 */

public class VCRegistrationAdditionalInfoREG039
	extends AbstractViewController
{
	public final static int INIT_ADDL_INFO = 20;
	public final static int VEHICLE_WEIGHT = 22;
	public final static int CHANGE_REG = 23;
	public final static String DATA_IS_NULL = "data is null";
	
	private VehicleInquiryData caOrigVehInqData;

	/**
	* VCRegistrationAdditionalInfoREG039 default constructor.
	*/
	public VCRegistrationAdditionalInfoREG039()
	{
		super();
	}
	
	/**
	 * Get Module Name
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.REGISTRATION;
	}
	
	/**
	 * Return original VehicleInquiryData.
	 * 
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData getOrigVehInqData()
	{
		return caOrigVehInqData;
	}
	
	/**
	 * Controls the screen flow from REG039.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int A constant letting the VC know which action 
	 * to perform
	 * @param aaData Object The data from the frame
	 * @see com.txdot.isd.rts.client.general.ui.AbstractViewController
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
			{
				setDirectionFlow(AbstractViewController.PREVIOUS);
				try
				{
					getMediator().processData(getModuleName(),
						RegistrationConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisible(false);
				// end 8884
				break;
			}

			case AbstractViewController.CANCEL :
			{
				setDirectionFlow(AbstractViewController.CANCEL);
				try
				{
					getMediator().processData(getModuleName(),
						RegistrationConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisible(false);
				// end 8884
				break;
			}

			case INIT_ADDL_INFO :
			{
				setDirectionFlow(AbstractViewController.CURRENT);
				try
				{
					getMediator().processData(getModuleName(),
						RegistrationConstant.INIT_ADDL_INFO,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}

			case VEHICLE_WEIGHT :
			{
				setDirectionFlow(AbstractViewController.NEXT);
				try
				{
					setNextController(ScreenConstant.REG010);
					setDirectionFlow(AbstractViewController.NEXT);
					getMediator().processData(getModuleName(),
						RegistrationConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}

			case CHANGE_REG :
			{
				setDirectionFlow(AbstractViewController.NEXT);
				((VehicleInquiryData) aaData).setShouldGoPrevious(
					true);
				try
				{
					setNextController(ScreenConstant.REG008);
					setDirectionFlow(AbstractViewController.NEXT);
					getMediator().processData(getModuleName(),
						RegistrationConstant.NO_DATA_TO_BUSINESS,
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
	 * Set original VehicleInquiryData.
	 * 
	 * @param aaNewOrigVehInqData VehicleInquiryData
	 */
	public void setOrigVehInqData(VehicleInquiryData 
		aaNewOrigVehInqData)
	{
		caOrigVehInqData = aaNewOrigVehInqData;
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
			RTSDialogBox laRTSDialogBox = getMediator().getParent();
			if (laRTSDialogBox != null)
			{
				setFrame(new FrmRegistrationAdditionalInfoREG039(
					laRTSDialogBox));
			}
			else
			{
				setFrame(new FrmRegistrationAdditionalInfoREG039(
					getMediator().getDesktop()));
			}
		}

		try
		{
			if (aaData == null)
			{
				throw new RTSException(RTSException.FAILURE_MESSAGE,
					DATA_IS_NULL,
					MiscellaneousRegConstant.ERROR_TITLE);
			}
			else
			{
				VehicleInquiryData laVehInqData =
					(VehicleInquiryData) aaData;
				if (laVehInqData.getValidationObject() == null)
				{
					throw new RTSException(RTSException.FAILURE_MESSAGE,
						DATA_IS_NULL,
						MiscellaneousRegConstant.ERROR_TITLE);
				}
				else
				{
					super.setView(avPreviousControllers, asTransCode, 
						aaData);
				}

			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}
}
