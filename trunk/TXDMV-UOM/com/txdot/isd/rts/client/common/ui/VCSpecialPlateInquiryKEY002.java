package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.specialplates.business.SpecialPlatesClientUtilityMethods;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/* 
 * VCSpecialPlateInquiryKEY002.java
 * 
 * (c) Texas Department of Transportation  2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		02/09/2007	New class
 * 							defect 9086 Ver Special Plates
 * B Hargrove	04/12/2007	Capture Customer Supplied indicator from
 * 							FrmSpecialPlateInquiryKEY002.
 * 							add captureCustomerSupplied()
 * 							modify moveSPDataToVehInqData()
 * 							defect 9126 Ver Special Plates
 * J Rue		04/13/2007	New method to build a Vehicle Inquiry Data
 * 							object
 * 							add createInquiryObject()
 * 							defect 9086 Ver Special Plates
 * J Rue		05/01/2007	Clean up mainframe down
 * 							modify handleMainframeDown()
 * 							defect 9086 Ver Special Plates  
 * B Hargrove	05/22/2007	After further review, remove Customer 
 * 							Supplied indicator from
 * 							FrmSpecialPlateInquiryKEY002. Un-do changes
 * 							I made on 04/12.
 * 							modify moveSPDataToVehInqData()
 * 							defect 9126 Ver Special Plates
 * K Harrell	06/01/2007	Use UtilityMethods.isSPAPPL() vs. check for
 * 							SPAPPL 
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/18/2007	Renamed SPL005 to SPL004
 * 							modify handleMainframeDown()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/07/2007	Display 1004 when error 1010 thrown from 
 * 							validate inventory no.
 * 							modify handleMainframeDown()
 * 							defect 9206
 * K Harrell	11/05/2007	Set EnterOnSPL002 based upon controller 
 * 							flow for MF Down.  Validate Special Plates
 * 							specified on SPL004. 
 * 							modify createInquiryObject(), setView()
 * 							defect 9460 Ver Special Plates 2 
 * K Harrell	11/26/2007	Add assignment of EnterOnSPL002 = true when 
 * 							#MF Records = 0 (for Title) 
 * 							modify setView()
 * 							defect 9460 Ver Special Plates 2
 * K Harrell	12/04/2007	Corrected above defect number from 9389 to 
 * 							9460. Refactor parameter name to standards.
 * 							modify moveSPDataToVehInqData()   
 * 							defect 9460 Ver Special Plates 2  
 * ---------------------------------------------------------------------
 */
/**
 * View Controller KEY002: Special Plates Inquiry
 * 
 * @version	Special Plates 2	12/04/2007
 * @author	Jeff Rue
 * <br>Creation Date:			02/09/2007 07:09:09
 */

public class VCSpecialPlateInquiryKEY002 extends AbstractViewController
{
	// int
	public int ciMfSearched = 0;

	// String 
	private String csTransCd;
	private String csRegPltNo;

	// Object 
	private VehicleInquiryData caVehInqData = null;
	private VehicleInquiryData caSavedVehInqData = null;
	private GeneralSearchData caGeneralSearchData = null;

	// Constants 
	public static final int MF_DOWN = 20;
	public static final int MULT_RECS = 21;
	public static final int NO_REC_FOUND = 22;
	public static final int SINGLE_REC = 24;
	public static final int SP_DELETE = 25;
	public static final int GET_SAME_SPCL_PLT = 26;
	public static final int PREVIOUS_CONTROLLER = 27;
	private static final int ONE_HUNDRED = 100;

	/**
	 * VCSpecialPlateInquiryKEY002 Constructor 
	 */
	public VCSpecialPlateInquiryKEY002()
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
		return GeneralConstant.SPECIALPLATES;
	}

	/**
	 * Sets error message and navigation for Mainframe Down scenario
	 * 
	 */
	public void handleMainframeDown()
	{
		try
		{
			// Display Exception for MF Down 
			if (UtilityMethods.isSpecialPlates(getTransCode()))
			{
				RTSException leRTSEx = new RTSException(01);
				leRTSEx.displayError(getFrame());
				processData(AbstractViewController.CANCEL, null);
			}
			else
			{
				setNextController(ScreenConstant.SPL004);
				setDirectionFlow(AbstractViewController.NEXT);
				VehicleInquiryData laInqData =
					(VehicleInquiryData) createInquiryObject();
				laInqData.setMfDown(1);
				SpecialPlatesClientUtilityMethods.verifyValidPltNo(
					laInqData.getMfVehicleData().getSpclPltRegisData());
				ciMfSearched = 1;
				RTSException leRTSEx = new RTSException(20);
				leRTSEx.displayError(getFrame());
				getMediator().processData(
					getModuleName(),
					CommonConstant.NO_DATA_TO_BUSINESS,
					laInqData);

			}
		}
		catch (RTSException aeRTSEx)
		{
			// defect 9206
			if (aeRTSEx.getCode()
				== ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE)
			{
				aeRTSEx.setCode(
					ErrorsConstant.ERR_NUM_VI_PER_MATCHES_PTRN);
			}
			// end defect 9206 			
			aeRTSEx.displayError(getFrame());
		}

	}

	/**
	 * Controls the screen flow from KEY002.  It passes the data to the
	 * RTSMediator.
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
					setDirectionFlow(AbstractViewController.CURRENT);
					caGeneralSearchData = (GeneralSearchData) aaData;
					try
					{
						csRegPltNo =
							((GeneralSearchData) aaData).getKey2();
						getMediator().processData(
							getModuleName(),
							CommonConstant.GET_VEH,
							aaData);

					}
					catch (RTSException aeRTSEx)
					{
						if (aeRTSEx
							.getMsgType()
							.equals(RTSException.SERVER_DOWN)
							|| aeRTSEx.getMsgType().equals(
								RTSException.MF_DOWN)
							|| aeRTSEx.getMsgType().equals(
								RTSException.DB_DOWN))
						{
							handleMainframeDown();
						}
						else
						{
							aeRTSEx.displayError(getFrame());
						}
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					String lsTransCd = getTransCode();
					if (UtilityMethods.isSpecialPlates(lsTransCd)
						&& !UtilityMethods.isSPAPPL(lsTransCd))
					{
						setDirectionFlow(AbstractViewController.FINAL);
					}
					else
					{
						setDirectionFlow(AbstractViewController.CANCEL);
					}
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							null);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					close();
					break;

				}
			case VCSpecialPlateInquiryKEY002.MF_DOWN :
				{
					handleMainframeDown();
					break;
				}
			case VCSpecialPlateInquiryKEY002.MULT_RECS :
				{
					setNextController(ScreenConstant.INQ004);
					setDirectionFlow(AbstractViewController.NEXT);
					VehicleInquiryData laVehData =
						new VehicleInquiryData();
					laVehData = (VehicleInquiryData) aaData;
					if (laVehData
						.getPartialSpclPltsDataList()
						.firstElement()
						instanceof MFPartialSpclPltData)
					{
						if (laVehData
							.getPartialSpclPltsDataList()
							.size()
							> ONE_HUNDRED)
						{
							RTSException leRTSEx =
								new RTSException(148);
							leRTSEx.displayError(getFrame());

							break;
						}
					}
					try
					{
						setData(aaData);
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCSpecialPlateInquiryKEY002.NO_REC_FOUND :
				{
					RTSException leRTSEx = null;
					leRTSEx = new RTSException(57);
					leRTSEx.displayError(getFrame());
					break;
				}

			case VCSpecialPlateInquiryKEY002.SINGLE_REC :
				{
					VehicleInquiryData laVehInqData =
						(VehicleInquiryData) aaData;
					setNextController(ScreenConstant.SPL002);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						moveSPDataToVehInqData(laVehInqData);
						ciMfSearched = 1;
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							caVehInqData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCSpecialPlateInquiryKEY002.GET_SAME_SPCL_PLT :
				{
					caVehInqData = (VehicleInquiryData) aaData;
					processData(SINGLE_REC, caVehInqData);
					break;
				}
			case VCSpecialPlateInquiryKEY002.PREVIOUS_CONTROLLER :
				{
					try
					{
						// Move SP data to original VehInqData object
						VehicleInquiryData laOrigVehInqData =
							SpecialPlatesClientUtilityMethods
								.saveSPData(
								caSavedVehInqData,
								(VehicleInquiryData) aaData);

						caVehInqData = (VehicleInquiryData) aaData;
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							laOrigVehInqData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					close();
					break;
				}
		}

	}

	/**
	 * Move Special Plates data to VehicleInquiryData if SpclRegId exist
	 * or TransCd is SPRSVR or SPUNC
	 * 
	 * @param aaVehInqData	
	 */
	private void moveSPDataToVehInqData(VehicleInquiryData aaVehInqData)
	{
		// Check for spclRegId and TransCds
		if ((!(UtilityMethods.isSpecialPlates(csTransCd)))
			|| aaVehInqData
				.getMfVehicleData()
				.getSpclPltRegisData()
				.getSpclRegId()
				!= 0
			|| (csTransCd.equals(TransCdConstant.SPRSRV)
				|| csTransCd.equals(TransCdConstant.SPUNAC)))
		{
			// Set MfVehicleData if caVehInqData = null
			caVehInqData = new VehicleInquiryData();
			caVehInqData.setMfVehicleData(new MFVehicleData());
			caVehInqData.getMfVehicleData().setSpclPltRegisData(
				aaVehInqData.getMfVehicleData().getSpclPltRegisData());
				
			// defect 9126
			//captureCustomerSupplied(caVehInqData);
			// end defect 9126 
		}
	}

	/**
	 * Creates Inquiry Object
	 * 
	 * @return VehicleInquiryData 
	 */
	private VehicleInquiryData createInquiryObject()
	{
		VehicleInquiryData laVehInqData = null;

		if (!UtilityMethods.isSpecialPlates(getTransCode()))
		{
			SpecialPlatesRegisData laSpclPltRegisData =
				new SpecialPlatesRegisData();
			laVehInqData = new VehicleInquiryData();
			if (caSavedVehInqData.getNoMFRecs() != 0)
			{
				MFVehicleData laMFVehData = new MFVehicleData();
				laVehInqData.setMfVehicleData(laMFVehData);
			}
			else
			{
				laVehInqData = caSavedVehInqData;
			}
			laVehInqData.getMfVehicleData().setSpclPltRegisData(
				laSpclPltRegisData);

			// defect 9460
			// Now set after display on SPL004.  
			// laSpclPltRegisData.setMFDownSP(true);
			// end defect 9660

			laSpclPltRegisData.setRegPltNo(csRegPltNo);
			laSpclPltRegisData.setRegPltCd(
				caSavedVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegPltCd()
					.trim());
		}
		return laVehInqData;
	}

	/**
	 * Creates the actual frame, stores the protected variables 
	 * needed by the VC, and sends the data to the frame.
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
		//If mainframe has not been searched yet, set the Frame
		try
		{
			csTransCd = asTransCode;
			if (getFrame() == null)
			{
				RTSDialogBox laRTSDB = getMediator().getParent();
				if (laRTSDB != null)
				{
					setFrame(new FrmSpecialPlateInquiryKEY002(laRTSDB));
				}
				else
				{
					setFrame(
						new FrmSpecialPlateInquiryKEY002(
							getMediator().getDesktop()));
				}
				if (aaData != null)
				{
					caSavedVehInqData =
						(VehicleInquiryData) UtilityMethods.copy(
							aaData);
				}

				super.setView(
					avPreviousControllers,
					asTransCode,
					aaData);
				return;
			}
			// Get record from mainframe
			else if (aaData instanceof GeneralSearchData)
			{
				processData(AbstractViewController.ENTER, aaData);
				return;
			}
			// If data is null, then return from INQ004
			if (aaData == null)
			{
				return;
			}
			// If mainframe has been searched, VehicleInquiryData is 
			// returned and processing for next screen is run
			else
			{
				VehicleInquiryData laInqData =
					(VehicleInquiryData) aaData;

				// MF has been searched. Control was passed back to this VC.
				if (ciMfSearched == 1)
				{
					// defect 9460
					// shouldGoPrevious is false on initial Special Plate
					//  record setup in MF Down 
					if (caSavedVehInqData.shouldGoPrevious())
					{
						if (laInqData
							.getMfVehicleData()
							.getSpclPltRegisData()
							.isMFDownSP())
						{
							SpecialPlatesClientUtilityMethods
								.verifyRecordApplicable(
								caSavedVehInqData,
								laInqData,
								getTransCode());
						}
						laInqData
							.getMfVehicleData()
							.getSpclPltRegisData()
							.setEnterOnSPL002(
							true);
					}
					// Setup Record for Title, No Record Found 
					else if (
						laInqData.getNoMFRecs() == 0
							&& UtilityMethods.getEventType(
								csTransCd).equals(
								TransCdConstant.TTL_EVENT_TYPE))
					{
						laInqData
							.getMfVehicleData()
							.getSpclPltRegisData()
							.setEnterOnSPL002(
							true);
					}
					// end defect 9460 
					processData(PREVIOUS_CONTROLLER, laInqData);
				}
				// Mainframe down
				else if (laInqData.getMfDown() == 1)
				{
					processData(MF_DOWN, laInqData);
				}
				// Multi-Records
				else if (laInqData.getNoMFRecs() > 1)
				{
					processData(MULT_RECS, laInqData);
				}
				// No Record Found
				else if (laInqData.getNoMFRecs() == 0)
				{
					// Build Record for SPRSRV, SPUNAC
					if (asTransCode.equals(TransCdConstant.SPRSRV)
						|| asTransCode.equals(TransCdConstant.SPUNAC))
					{
						laInqData =
							SpecialPlatesClientUtilityMethods
								.createVehInqData(
								csTransCd,
								csRegPltNo);
						processData(SINGLE_REC, laInqData);
					}
					else
					{
						processData(NO_REC_FOUND, laInqData);
					}
				}
				// Record Found
				else if (laInqData.getNoMFRecs() == 1)
				{
					SpecialPlatesClientUtilityMethods
						.verifyRecordApplicable(
						caSavedVehInqData,
						laInqData,
						asTransCode);
					processData(SINGLE_REC, laInqData);
				}
			}
		}
		catch (RTSException aeRTSException)
		{
			aeRTSException.displayError(getFrame());
			processData(CANCEL, null);
		}
	}
}
