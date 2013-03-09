package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * VCTimedPermitVinKeySelectionMRG007.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/24/2010	Created
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/07/2010	Implement Error Constants
 * 							modify processData(), setView()  
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * VC for FrmTimedPermitVinKeySelectionMRG007
 *
 * @version	6.5.0 			07/07/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		05/24/2010  17:52:17
 */
public class VCTimedPermitVinKeySelectionMRG007
	extends AbstractViewController
{
	private final static int INQ004 = 22;
	public final static int MRG005 = 23;

	/**
	 * VCTimedPermitVinKeySelectionMRG007.java Constructor
	 * 
	 */
	public VCTimedPermitVinKeySelectionMRG007()
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
	 * Sets error message and navigation for Mainframe Down / DB Down 
	 * scenario
	 * 
	 * @param aaData 
	 */
	public void handleMFDown(Object aaData)
	{
		GeneralSearchData laGSD = (GeneralSearchData) aaData;
		new RTSException(
			ErrorsConstant
				.ERR_NUM_UNABLE_TO_RETRIEVE_CAN_PROCESS)
				.displayError(
			getFrame());
		PermitData laPrmtData = new PermitData();
		laPrmtData.setVin(laGSD.getKey2());
		laPrmtData.setNoMFRecs(1);
		processData(MRG005, laPrmtData);
	}

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
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						aaData =
							getMediator().processData(
								getModuleName(),
								MiscellaneousRegConstant.PRMTINQ,
								aaData);
					}
					catch (RTSException aeRTSEx)
					{
						handleMFDown(aaData);
					}
					break;
				}
			case INQ004 :
				{
					setNextController(ScreenConstant.INQ004);
					setDirectionFlow(AbstractViewController.NEXT);

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
			case MRG005 :
				{
					setNextController(ScreenConstant.MRG005);
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
					close();
					break;
				}
		}
	}

	/**
	 * This method is called by RTSMediator to display the frame.
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
			setTransCode(asTransCode);

			RTSDialogBox laRTSDialogBox = getMediator().getParent();
			if (laRTSDialogBox != null)
			{

				setFrame(
					new FrmTimedPermitVinKeySelectionMRG007(laRTSDialogBox));
			}
			else
			{
				setFrame(
					new FrmTimedPermitVinKeySelectionMRG007(
						getMediator().getDesktop()));
			}
		}

		if (aaData == null)
		{
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		else
		{
			if (aaData instanceof PermitData)
			{
				PermitData laPrmtData = (PermitData) aaData;

				if (laPrmtData.getNoMFRecs() == 0
					&& laPrmtData.isMFDown())
				{
					new RTSException(
						ErrorsConstant
							.ERR_NUM_UNABLE_TO_RETRIEVE_CAN_PROCESS)
							.displayError(
						getFrame());

					processData(MRG005, laPrmtData);
				}
				else if (laPrmtData.isByPrmtId())
				{
					processData(MRG005, laPrmtData);
				}
				else
				{
					if (laPrmtData.isMaxRecordsExceeded())
					{
						new RTSException(
							ErrorsConstant
								.ERR_NUM_MAXIMUM_NO_OF_ROWS_EXCEEDED)
								.displayError(
							getFrame());
					}
					processData(INQ004, laPrmtData);
				}
			}
			else if (aaData instanceof GeneralSearchData)
			{
				processData(ENTER, aaData);
			}
			else if (aaData instanceof VehicleInquiryData)
			{
				VehicleInquiryData laVehInqData =
					(VehicleInquiryData) aaData;

				if (laVehInqData.isMFDown())
				{
					new RTSException(
						ErrorsConstant
							.ERR_NUM_UNABLE_TO_RETRIEVE_CAN_PROCESS)
							.displayError(
						getFrame());

					processData(MRG005, new PermitData(laVehInqData));
				}
				else if (laVehInqData.getNoMFRecs() == 0)
				{
					new RTSException(
						ErrorsConstant
							.ERR_NUM_NO_RECORD_FOUND)
							.displayError(
						getFrame());

					processData(MRG005, new PermitData(laVehInqData));
				}
				else
				{
					if (laVehInqData.getNoMFRecs() == 1)
					{
						MFPartialData laMFPrtlData =
							new MFPartialData();

						TitleData laTitleData =
							laVehInqData
								.getMfVehicleData()
								.getTitleData();

						laMFPrtlData.setDocNo(laTitleData.getDocNo());
						laMFPrtlData.setOwnrTtlName(
							laVehInqData
								.getMfVehicleData()
								.getOwnerData()
								.getName1());
						laMFPrtlData.setRegExpMo(
							laVehInqData
								.getMfVehicleData()
								.getRegData()
								.getRegExpMo());
						laMFPrtlData.setRegExpYr(
							laVehInqData
								.getMfVehicleData()
								.getRegData()
								.getRegExpYr());
						laMFPrtlData.setRegPltNo(
							laVehInqData
								.getMfVehicleData()
								.getRegData()
								.getRegPltNo());
						laMFPrtlData.setVehMk(
							laVehInqData
								.getMfVehicleData()
								.getVehicleData()
								.getVehMk());
						laMFPrtlData.setVin(
							laVehInqData
								.getMfVehicleData()
								.getVehicleData()
								.getVin());
						laMFPrtlData.setVehModlYr(
							laVehInqData
								.getMfVehicleData()
								.getVehicleData()
								.getVehModlYr());
						Vector lvPartial = new Vector();
						lvPartial.add(laMFPrtlData);
						laVehInqData.setPartialDataList(lvPartial);
					}
					else if (laVehInqData.getNoMFRecs() > 100)
					{
						new RTSException(
							ErrorsConstant
								.ERR_NUM_TOO_MANY_RCDS_FOUND)
								.displayError(
							getFrame());
					}
					processData(INQ004, laVehInqData);
				}
			}
		}
	}
}
