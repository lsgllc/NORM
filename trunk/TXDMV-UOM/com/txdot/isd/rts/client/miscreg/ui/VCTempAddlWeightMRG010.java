package com.txdot.isd.rts.client.miscreg.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.TimedPermitData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCTempAddlWeightMRG010.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	07/05/2005	Modify code for move to Java 1.4 (VAJ->WSAD)
 * 							Bring code to standards.
 *							Remove unused methods
 *							defect 7893 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */

/**
 * Miscellaneous Registration Temporary Additional Weight (MRG010) 
 * view controller class.
 *
 * @version	5.2.4			08/11/2006
 * @author	Joseph Kwik
 * <br>Creation Date:		06/26/2001 14:22:56
 */

public class VCTempAddlWeightMRG010 extends AbstractViewController
{

	private VehicleInquiryData caOrigVehInqData;
	public final static int SUPV_OVRIDE = 20;
	public final static int INIT_TEMP_ADDL_WEIGHT = 21;
	public final static int VTR_AUTHORIZATION = 23;
	private boolean cbInitTempAddlWtHasError = false;
	/**
	* VCRegistrationAdditionalInfoREG039 default constructor.
	*/
	public VCTempAddlWeightMRG010()
	{
		super();
	}
	/**
	 * Get Module Name MISCELLANEOUSREG
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.MISCELLANEOUSREG;
	}
	/**
	 * Get Orig Veh Inquiry Data
	 * 
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData getOrigVehInqData()
	{
		return caOrigVehInqData;
	}
	
	// defect 7893
	// remove unused method
	///**
	// * Insert the method's description here.
	// * 
	// * @return boolean
	// */
	//private boolean isInitTempAddlWtHasError()
	//{
	//	return cbInitTempAddlWtHasError;
	//}
	// end defect 7893
	
	/**
	 * Process Data
	 * 
	 * @param aiCommand int
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
			{
				setNextController(ScreenConstant.MRG011);
				setDirectionFlow(AbstractViewController.NEXT);
				try
				{
					getMediator().processData(
						getModuleName(),
						MiscellaneousRegConstant
							.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}

			case AbstractViewController.CANCEL :
			{
				// intentionally falling through
			}
			case AbstractViewController.FINAL :
			{
				setDirectionFlow(AbstractViewController.FINAL);
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisible(false);
				// end 8884
				try
				{
					getMediator().processData(
						getModuleName(),
						MiscellaneousRegConstant
							.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			}
			case INIT_TEMP_ADDL_WEIGHT :
			{
				setDirectionFlow(AbstractViewController.CURRENT);
				try
				{
					getMediator().processData(
						getModuleName(),
						MiscellaneousRegConstant
							.INIT_TEMP_ADDL_WEIGHT,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getMediator().
						getDesktop());
					cbInitTempAddlWtHasError = true;
				}
				break;
			}
			case VTR_AUTHORIZATION :
			{
				try
				{
					setNextController(ScreenConstant.CTL003);
					setDirectionFlow(AbstractViewController.NEXT);
					getMediator().processData(
						getModuleName(),
						MiscellaneousRegConstant
							.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
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
						MiscellaneousRegConstant
							.NO_DATA_TO_BUSINESS,
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
	
	// defect 7893
	// remove unused method
	///**
	// * Insert the method's description here.
	// * 
	// * @param abNewInitTempAddlWtHasError boolean
	// */
	//private void setInitTempAddlWtHasError(boolean 
	//	abNewInitTempAddlWtHasError)
	//{
	//	cbInitTempAddlWtHasError = abNewInitTempAddlWtHasError;
	//}
	// end defect 7893
	
	/**
	 * Set Original Vehicle Inquiry Data.
	 * 
	 * @param aaNewOrigVehInqData VehicleInquiryData
	 */
	public void setOrigVehInqData(VehicleInquiryData 
		aaNewOrigVehInqData)
	{
		caOrigVehInqData = aaNewOrigVehInqData;
	}
	/**
	 * This method is called by RTSMediator to display the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector
	 * @param asTransCode String
	 * @paramt aaData Objec
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDBox = getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmTempAddlWeightMRG010(laRTSDBox));
			}
			else
			{
				setFrame(new FrmTempAddlWeightMRG010(
					getMediator().getDesktop()));
			}
		}
		if ((aaData instanceof VehicleInquiryData)
			&& ((VehicleInquiryData) aaData).getValidationObject() != null
			&& ((VehicleInquiryData) aaData).getValidationObject()
				instanceof TimedPermitData)
		{
			return; //back from INIT_TEMP_ADDL_WEIGHT
		}
 
		if (aaData instanceof VehicleInquiryData)
		{
			TimedPermitData laTimedPrmtData = new TimedPermitData();

			if (aaData instanceof VehicleInquiryData)
			{
				//setOrigVehInqData((VehicleInquiryData)
				//	UtilityMethods.copy(data));
				VehicleInquiryData laVehInqData =
					(VehicleInquiryData) aaData;
				laVehInqData.setValidationObject(laTimedPrmtData);
				processData(INIT_TEMP_ADDL_WEIGHT, laVehInqData);
			}
			if (cbInitTempAddlWtHasError)
			{
				processData(AbstractViewController.FINAL, aaData);
			}
			else
			{
				super.setView(avPreviousControllers, asTransCode, aaData);
			}
		}
	}
}
