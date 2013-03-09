package com.txdot.isd.rts.client.registration.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCOwnerAddressREG033.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	06/23/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 *  	 					Use new getters\setters for frame, 
 *							controller, etc.
 *  	 					Remove unused methods.
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	07/19/2005	Refactor\Move
 * 							RegistrationValidationData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * J Rue		08/02/2007	Get previous controller to determine path 
 * 							when MF down.
 * 							modify processData()
 * 							defect 9086 Ver Special Plates
 * ---------------------------------------------------------------------
 */
/**
 * Owner Addresses (REG033) view controller class.  It handles screen 
 * navigation and controls the visibility of its frame.
 *
 * @version	Special Plates	08/02/2007
 * @author	Joseph Kwik
 * <br>Creation Date:		08/28/2001 17:19:11
 */

public class VCOwnerAddressREG033 extends AbstractViewController
{
	private VehicleInquiryData caOrigVehInqData = null;
	private int ciMfDown;
	public final static int VTR_SSN_AUTHORIZATION = 20;
	public final static String DATA_IS_NULL = "data is null";

	// Vectors
	Vector cvPrevControllers = new Vector();

	/**
	* VCOwnerAddressREG033 default constructor.
	*/
	public VCOwnerAddressREG033()
	{
		super();
	}

	// defect 7894
	// remove unused method	
	///**
	// * Return mainframe available indicator.
	// * 
	// * @return int
	// */
	//private int getMfDown()
	//{
	//	return ciMfDown;
	//}
	// end defect 7894

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
	 * @return VehicleInquiryData Object
	 */
	public VehicleInquiryData getOrigVehInqData()
	{
		return caOrigVehInqData;
	}

	/**
	 * Controls the screen flow from REG033.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int A constant letting the VC know which action 
	 * to perform
	 * @param aaData Object The data from the frame
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				{
					// defect 9086
					// Get previous controller to determine path when MF
					//	down.
					String lsPrevController =
						(String) cvPrevControllers.get(
							cvPrevControllers.size() - 1);
					// MF up or previous screen is REG003
					if (ciMfDown == 0
						|| lsPrevController.equals(ScreenConstant.REG003))
					{
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
					}
					// end defect 9086
					else
					{
						setNextController(ScreenConstant.REG003);
						setDirectionFlow(AbstractViewController.NEXT);
					}
					try
					{
						getMediator().processData(
							getModuleName(),
							RegistrationConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					// defect 8884
					// use close() so that it does setVisibleRTS()
					close();
					//getFrame().setVisible(false);
					//end 8884
					break;
				}

			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
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

			case VTR_SSN_AUTHORIZATION :
				{
					try
					{
						setNextController(ScreenConstant.CTL010);
						setDirectionFlow(AbstractViewController.NEXT);
						(
							(RegistrationValidationData)
								((VehicleInquiryData) aaData)
								.getValidationObject())
								.setVTRAuth(
							true);
						getMediator().processData(
							getModuleName(),
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

	// defect 7894
	// remove unused methods	
	///**
	// * Set mainframe available indicator.
	// * 
	// * @param aiNewMfDown int
	// */
	//private void setMfDown(int aiNewMfDown)
	//{
	//	ciMfDown = aiNewMfDown;
	//}

	///**
	// * Set original VehicleInquiryData.
	// * 
	// * @param aaNewVehInqData VehicleInquiryData
	// */
	//private void setOrigVehInqData(VehicleInquiryData aaNewVehInqData)
	//{
	//	caOrigVehInqData = aaNewVehInqData;
	//}
	// end defect 7894

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
		try
		{
			if (getFrame() == null)
			{
				RTSDialogBox laRTSDialogBox = getMediator().getParent();
				if (laRTSDialogBox != null)
				{
					setFrame(new FrmOwnerAddressREG033(laRTSDialogBox));
				}
				else
				{
					setFrame(
						new FrmOwnerAddressREG033(
							getMediator().getDesktop()));
				}
			}

			if (aaData == null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					DATA_IS_NULL,
					MiscellaneousRegConstant.ERROR_TITLE);
			}

			if (aaData instanceof VehicleInquiryData)
			{
				ciMfDown = ((VehicleInquiryData) aaData).getMfDown();
				cvPrevControllers = avPreviousControllers;
				if (ciMfDown == 0)
				{
					// if mf is available
					boolean lbVTR =
						(
							(RegistrationValidationData)
								((VehicleInquiryData) aaData)
							.getValidationObject())
							.isVTRAuth();
					if (lbVTR)
					{
						(
							(RegistrationValidationData)
								((VehicleInquiryData) aaData)
								.getValidationObject())
								.setVTRAuth(
							false);
						this.setData(aaData);
						this.setPreviousControllers(
							avPreviousControllers);
						this.setTransCode(asTransCode);
						getFrame().setController(this);
						return;
					}
					else
					{
						super.setView(
							avPreviousControllers,
							asTransCode,
							aaData);
					}
				}
				else
				{
					// mf is unavailable
					super.setView(
						avPreviousControllers,
						asTransCode,
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
