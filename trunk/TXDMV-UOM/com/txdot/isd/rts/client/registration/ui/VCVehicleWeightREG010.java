package com.txdot.isd.rts.client.registration.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

/*
 *
 * VCVehicleWeightREG010.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	06/23/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 *  						Use new getters\setters for frame, 
 *							controller, etc.
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------
 */

/**
 * Registration Vehicle Weight (REG010) view controller class.  It 
 * handles screen navigation and controls the visibility of its frame.
* 
* @version:	5.2.4	08/11/2006 
*  
* @author:	Joseph Kwik
* <br>Creation Date:	08/28/2001 17:12:37
*/

public class VCVehicleWeightREG010 extends AbstractViewController
{
	private VehicleInquiryData caOrigVehInqData;
	
	/**
	 * VCVehicleWeightREG010 default constructor.
	 */
	public VCVehicleWeightREG010()
	{
		super();
	}
	
	/**
	 * @see com.txdot.isd.rts.client.general.ui.AbstractViewController
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.REGISTRATION;
	}
	
	/**
	 * Return VehicleInquiryData.
	 * 
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData getOrigVehInqData()
	{
		return caOrigVehInqData;
	}
	
	/**
	 * Controls the screen flow from REG010.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int A constant letting the VC know which action 
	 * to perform
	 * @param aaData Object The data from the frame
	 * @see com.txdot.isd.rts.client.general.ui.AbstractViewController
	 */
	public void processData(int aiCommand, Object aaData)
	{
		// here decide what the next screen is going to be
		switch (aiCommand)
		{
			case ENTER :
			{
				try
				{
					setDirectionFlow(AbstractViewController.
						PREVIOUS);
					getMediator().processData(
						getModuleName(),
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

			case CANCEL :
			{
				setDirectionFlow(AbstractViewController.CANCEL);
				try
				{
					getMediator().processData(
						getModuleName(),
						RegistrationConstant.NO_DATA_TO_BUSINESS,
						getOrigVehInqData());
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
				setFrame(new FrmVehicleWeightREG010(laRTSDialogBox));
			}
			else
			{
				setFrame(new FrmVehicleWeightREG010(getMediator().
					getDesktop()));
			}
		}

		try
		{
			if (aaData == null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					"data is null",
					MiscellaneousRegConstant.ERROR_TITLE);
			}
			else
			{
				setOrigVehInqData(
					(VehicleInquiryData) UtilityMethods.copy(aaData));

				super.setView(avPreviousControllers, asTransCode, 
					aaData);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}
}
