package com.txdot.isd.rts.client.registration.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCRegistrationCorrectionREG025.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		05/21/2002	If headquarters then display message whether
 * 							to complete trans and don't display Fees Due
 * 							PMT004 window
 *							defect 4008
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7899 Ver 5.2.3                  
 * B Hargrove	07/20/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 * 							defect 7894 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * K Harrell	04/15/2007	Use SystemProperty.isHQ()
 * 							modify processData()
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * Registration Additional Info (REG025) view controller class.  It 
 * handles screen navigation and controls the visibility of its frame.
 *
 * @version	Special Plates	04/15/2007
 * @author	Joseph Kwik
 * <br>Creation Date:		10/29/2001 17:41:06
 */

public class VCRegistrationCorrectionREG025
	extends AbstractViewController
{

	private VehicleInquiryData caOrigVehInqData;
	public final static int SUPV_OVRIDE = 20;
	public final static String DATA_IS_NULL = "data is null";
	public final static String VALIDATION_OBJECT_IS_NULL =
		"Validation object is null";

	/**
	* VCRegistrationAdditionalInfoREG039 default constructor.
	*/
	public VCRegistrationCorrectionREG025()
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
	 * Return original VehicleInquiryData.
	 * 
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData getOrigVehInqData()
	{
		return caOrigVehInqData;
	}

	/**
	 * Controls the screen flow from REG025.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int 
	 * @param aaData Object 
	 * @see com.txdot.isd.rts.client.general.ui.AbstractViewController
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				{
					try
					{
						// defect 9085 
						// Use SystemProperty.isHQ())
						if (SystemProperty.isHQ())
						{
							if (Transaction.getTransactionHeaderData()
								!= null)
							{
								setDirectionFlow(
									AbstractViewController.DESKTOP);
							}
							else
							{
								setDirectionFlow(
									AbstractViewController.FINAL);
							}
							getMediator().processData(
								GeneralConstant.COMMON,
								CommonConstant.ADD_TRANS,
								aaData);
						}
						else
						{
							setDirectionFlow(
								AbstractViewController.NEXT);
							setNextController(ScreenConstant.PMT004);
							getMediator().processData(
								getModuleName(),
								RegistrationConstant
									.NO_DATA_TO_BUSINESS,
								aaData);
						}
						// end defect 9085
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
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
							caOrigVehInqData);
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

			case SUPV_OVRIDE :
				{
					try
					{
						setNextController(ScreenConstant.CTL004);
						setDirectionFlow(AbstractViewController.NEXT);
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
	/**
	 * Set original VehicleInquiryData.
	 * 
	 * @param aaNewOrigVehInqData VehicleInquiryData
	 */
	public void setOrigVehInqData(VehicleInquiryData aaNewOrigVehInqData)
	{
		caOrigVehInqData = aaNewOrigVehInqData;
	}

	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector A vector containing 
	 * the String names of the previous controllers in order
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
				setFrame(
					new FrmRegistrationCorrectionREG025(laRTSDialogBox));
			}
			else
			{
				setFrame(
					new FrmRegistrationCorrectionREG025(
						getMediator().getDesktop()));
			}
		}

		try
		{
			if (aaData == null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					DATA_IS_NULL,
					MiscellaneousRegConstant.ERROR_TITLE);
			}
			else
			{
				if (caOrigVehInqData == null)
				{
					setOrigVehInqData(
						(VehicleInquiryData) UtilityMethods.copy(
							aaData));
					super.setView(
						avPreviousControllers,
						asTransCode,
						aaData);
				}

				VehicleInquiryData laVehInqData =
					(VehicleInquiryData) aaData;

				if (laVehInqData.getValidationObject() == null)
				{
					throw new RTSException(
						RTSException.FAILURE_MESSAGE,
						VALIDATION_OBJECT_IS_NULL,
						MiscellaneousRegConstant.ERROR_TITLE);
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}
}
