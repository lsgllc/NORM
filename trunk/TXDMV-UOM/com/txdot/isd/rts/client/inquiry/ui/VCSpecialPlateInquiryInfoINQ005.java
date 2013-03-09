package com.txdot.isd.rts.client.inquiry.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;

/*
 * VCSpecialPlateInquiryInfoINQ005.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		04/05/2007	Start building setView and processData
 * 							modify setView(), processData()
 * 							defect 9086 Ver Special Plates
 * K Harrell	05/19/2007	Screen rename
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * Controller for screen INQ005. Displays Special Plate Inquiry Info
 *
 * @version	Special Plates	04/05/2007
 * @author	Todd Pederson
 * <br>Creation Date:		02/20/2007 11:32:38
 */
public class VCSpecialPlateInquiryInfoINQ005
	extends AbstractViewController
{
	// int
	public final static int INQ007 = 28;

	/**
	 * VCSpecialPlateInquiryInfoINQ005 constructor
	 */
	public VCSpecialPlateInquiryInfoINQ005()
	{
		super();
	}
	/**
	 * Return boolean designating whether SpecialPltVehInquiry Only 
	 */
	private boolean isVehInqSpclPltOnly(Object aaData)
	{
		boolean lbSpclPltOnly = false;
		MFVehicleData laMFVehData = null;

		if (aaData instanceof CompleteTransactionData)
		{
			laMFVehData =
				(MFVehicleData) ((CompleteTransactionData) aaData)
					.getVehicleInfo();
		}
		else if (aaData instanceof VehicleInquiryData)
		{
			laMFVehData =
				((VehicleInquiryData) aaData).getMfVehicleData();
		}
		if (laMFVehData != null)
		{

			lbSpclPltOnly = laMFVehData.isSPRecordOnlyVehInq();
		}
		return lbSpclPltOnly;
	}
	/**
	 * Return "INQUIRY" as the module name
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.INQUIRY;
	}
	/**
	 * Process data for the screen. Called by the screen.
	 * 
	 * @param aiCommand int the command so the Frame can communicate
	 * 		with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		try
		{
			int liFunctionId =
				SpecialPlatesConstant.NO_DATA_TO_BUSINESS;

			// If Special Plate Only Veh Inquiry
			boolean lbSpclPltVehInq = isVehInqSpclPltOnly(aaData);

			switch (aiCommand)
			{
				case ENTER :
					{
						// If Vehicle Inquiry - Only Splate
						if (lbSpclPltVehInq)
						{
							// If More Trans - go to Pending Transaction
							// Screen 
							if (Transaction.getTransactionHeaderData()
								!= null)
							{
								setDirectionFlow(
									AbstractViewController.DESKTOP);
							}
							// !More Trans, finalize transaction
							else
							{
								setDirectionFlow(
									AbstractViewController.FINAL);
								liFunctionId = CommonConstant.ADD_TRANS;
							}
						}
						else
						{
							setDirectionFlow(
								AbstractViewController.CANCEL);
						}
						try
						{
							getMediator().processData(
								GeneralConstant.COMMON,
								liFunctionId,
								aaData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						break;
					}
				case INQ007 :
					{
						try
						{
							setNextController(ScreenConstant.INQ007);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								getModuleName(),
								SpecialPlatesConstant
									.NO_DATA_TO_BUSINESS,
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
						if (lbSpclPltVehInq)
						{
							setDirectionFlow(
								AbstractViewController.FINAL);
						}
						else
						{
							setDirectionFlow(
								AbstractViewController.CANCEL);
						}

						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
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
	 * Set the view for current controller. Instantiate the frame
	 * object for this controller and set it to the frame variable.
	 * 
	 * @param aaPreviousControllers Vector
	 * @param asTransCode String
	 * @param aaData Object
	 */
	public void setView(
		Vector aaPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox rd = getMediator().getParent();
			if (rd != null)
			{
				setFrame(new FrmSpecialPlateInquiryInfoINQ005(rd));
			}
			else
			{
				setFrame(
					new FrmSpecialPlateInquiryInfoINQ005(
						getMediator().getDesktop()));
			}
		}
		super.setView(aaPreviousControllers, asTransCode, aaData);
	}

}
