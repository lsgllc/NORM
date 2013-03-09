package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.registration.business.RegistrationClientUtilityMethods;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * VCSpecialPlateMFRecordNotAvailableSPL004.java
 * 
 * (c) Texas Department of Transportation  2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		04/13/2007 	Add code to process transaction
 * 							modify setView(), processData()
 * 							defect 9086 Ver Special Plates
 * J Rue		04/26/2007	Set return to previous screen on Enter
 * 							processData()
 * 							defect 9086 Ver Special Plates
 * K Harrell	04/27/2007	Replacement Work
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/18/2007	Renamed from SPL005 to SPL004
 * 							modify handleMainframeDown()
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: SPL004
 *
 * @version Special Plates	06/18/2007
 * @author	Jeff Rue
 * <br>Creation Date: 		04/13/2007 12:35:15 
 *
 */
public class VCSpecialPlateMFRecordNotAvailableSPL004
	extends AbstractViewController
{

	// String 
	private String csTransCd = new String();

	// Constants  
	public final static int REDIRECT_IS_NEXT_VC_REG029 = 22;
	public final static int REDIRECT_NEXT_VC = 23;
	public final static int ADD_TRANS = 25;
	public final static int REDIRECT = 30;
	private final static String ERRMSG_DATA_IS_NULL = "data is null";
	private static final String ERRMSG_DATA_MISS =
		"Data missing for NextVC";
	private static final String ERRMSG_ERROR = "ERROR";

	private Vector cvPreviousControllers = new Vector();

	/**
	 * VCSpecialPlateMFRecordNotAvailableSPL004 constructor comment.
	 */
	public VCSpecialPlateMFRecordNotAvailableSPL004()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own 
	 * module name.
	 *
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.COMMON;
	}
	/**
	 * Handles data coming from their JDialogBox
	 *
	 * @param aiCommand int
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		int liNumPrevControllers = cvPreviousControllers.size();
		String lsPrevScreen =
			(String) cvPreviousControllers.get(
				liNumPrevControllers - 1);

		switch (aiCommand)
		{
			// defect 9085
			case AbstractViewController.ENTER :
				{
					// If Replacement and from REG011, complete 
					// transaction. 
					if (csTransCd.equals(TransCdConstant.REPL)
						&& lsPrevScreen.equals(ScreenConstant.REG011))
					{
						CompleteTransactionData laCTData =
							buildCompleteTransactionData(aaData);
						if (isAddTrans((VehicleInquiryData) aaData))
						{
							RTSException leRTSEx =
								new RTSException(
									RTSException.CTL001,
									CommonConstant
										.TXT_COMPLETE_TRANS_QUESTION,
									ScreenConstant.CTL001_FRM_TITLE);
							int liResponse =
								leRTSEx.displayError(
									getMediator().getParent());
							if (liResponse == RTSException.YES)
							{

								processData(ADD_TRANS, laCTData);
							}
						}
						else
						{
							setDirectionFlow(
								AbstractViewController.CURRENT);

							try
							{
								getMediator().processData(
									GeneralConstant.COMMON,
									CommonConstant.IS_NEXT_VC_REG029,
									laCTData);
							}
							catch (RTSException aeRTSEx)
							{
								aeRTSEx.displayError(getFrame());
							}
						}
					}
					else
					{
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						try
						{
							getMediator().processData(
								getModuleName(),
								CommonConstant.NO_DATA_TO_BUSINESS,
								aaData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
					}
					break;
				}
			case ADD_TRANS :
				{

					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
						aaData = buildCompleteTransactionData(aaData);
						getMediator().processData(
							GeneralConstant.COMMON,
							CommonConstant.ADD_TRANS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case InventoryConstant.INV_VI_RELEASE_HOLD :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						aaData =
							getMediator().processData(
								GeneralConstant.INVENTORY,
								InventoryConstant.INV_VI_RELEASE_HOLD,
								aaData);
					}
					catch (RTSException aeRTSEx)
					{
						if (aeRTSEx.getCode()
							!= ErrorsConstant
								.ERR_NUM_VI_ITEM_NOT_RESERVED)
						{
							handleRTSException(aeRTSEx);
						}
					}
					break;
				}
			case REDIRECT :
				{
					try
					{
						Vector lvNextVC = (Vector) aaData;
						String lsNextVCName = (String) lvNextVC.get(0);
						CompleteTransactionData laData =
							(CompleteTransactionData) lvNextVC.get(1);
						if (lsNextVCName != null)
						{
							setNextController(lsNextVCName);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								GeneralConstant.COMMON,
								MiscellaneousRegConstant
									.NO_DATA_TO_BUSINESS,
								laData);
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case REDIRECT_IS_NEXT_VC_REG029 :
				{
					try
					{
						Vector lvIsNextVCREG029 = (Vector) aaData;
						Boolean lbGoToREG029 =
							(Boolean) lvIsNextVCREG029.get(0);
						// first element is flag whether to go to REG029
						CompleteTransactionData laData =
							(
								CompleteTransactionData) lvIsNextVCREG029
									.get(
								1);
						if (lbGoToREG029.equals(Boolean.TRUE))
						{
							setNextController(ScreenConstant.REG029);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								GeneralConstant.COMMON,
								RegistrationConstant
									.NO_DATA_TO_BUSINESS,
								laData);
						}
						else
						{
							// determine next vc if NOT reg029
							setDirectionFlow(
								AbstractViewController.CURRENT);
							getMediator().processData(
								GeneralConstant.COMMON,
								CommonConstant
									.GET_NEXT_COMPLETE_TRANS_VC,
								((Vector) aaData).elementAt(1));
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case REDIRECT_NEXT_VC :
				{
					try
					{
						Vector lvNextVC = (Vector) aaData;
						String lsNextVCName = (String) lvNextVC.get(0);
						// first element is name of next controller
						CompleteTransactionData laData =
							(CompleteTransactionData) lvNextVC.get(1);
						if (lsNextVCName != null)
						{
							setNextController(lsNextVCName);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								GeneralConstant.COMMON,
								RegistrationConstant
									.NO_DATA_TO_BUSINESS,
								laData);
						}
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

					// If not off REG003, TTL007 nor Replacement
					if (!csTransCd.equals(TransCdConstant.REPL)
						&& !lsPrevScreen.equals(ScreenConstant.REG003)
						&& !lsPrevScreen.equals(ScreenConstant.TTL007))
					{
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						setPreviousController(ScreenConstant.REG008);
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
					getFrame().setVisibleRTS(false);
					break;
				}
		}
	}
	/**
	 * Determine if should go directly to AddTrans 
	 */
	private boolean isAddTrans(VehicleInquiryData aaVehInqData)
	{
		boolean lbHQ = SystemProperty.isHQ();
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();
		return (lbHQ && laRegValidData.getInvItms().size() == 0);
	}
	/**
	 * Create new CompleteTransactionata 
	 */

	private CompleteTransactionData buildCompleteTransactionData(Object aaObject)
	{
		CompleteTransactionData laCompTransData;
		if (aaObject instanceof VehicleInquiryData)
		{
			VehicleInquiryData laVehInqData =
				(VehicleInquiryData) aaObject;

			if (csTransCd.equals(TransCdConstant.REPL))
			{
				RegistrationValidationData laRegValidData =
					(RegistrationValidationData) laVehInqData
						.getValidationObject();

				laCompTransData =
					RegistrationClientUtilityMethods.prepFees(
						laVehInqData,
						laRegValidData.getOrigVehInqData());

				MFVehicleData laMFVehData =
					laCompTransData.getVehicleInfo();

				int liRegExpYr = laMFVehData.getRegData().getRegExpYr();

				SpecialPlatesRegisData laSpclPltRegisData =
					laVehInqData
						.getMfVehicleData()
						.getSpclPltRegisData();

				laCompTransData.setOwnrSuppliedPltNo(
					laSpclPltRegisData.getRegPltNo());

				laMFVehData.getRegData().setOwnrSuppliedExpYr(
					liRegExpYr);

				laMFVehData.getRegData().setPrevPltNo(
					laMFVehData.getRegData().getRegPltNo());

			}
			else
			{
				laCompTransData = new CompleteTransactionData();
				laCompTransData.setVehicleInfo(
					laVehInqData.getMfVehicleData());
				laCompTransData.setOrgVehicleInfo(
					laVehInqData.getMfVehicleData());
				laCompTransData.setTransCode(csTransCd);
			}
		}
		else
		{
			laCompTransData = (CompleteTransactionData) aaObject;

		}
		return laCompTransData;
	}
	/** 
	 * The following handles DB or Server Down conditions for:
	 *   - Validation of Reserve / Unacceptable Inventory 
	 *   - Release of Inventory on Hold 
	 * 
	 * @param aeRTSEx
	 */
	private void handleRTSException(RTSException aeRTSEx)
	{
		if (aeRTSEx.getMsgType().equals(RTSException.DB_DOWN)
			|| aeRTSEx.getMsgType().equals(RTSException.SERVER_DOWN))
		{
			aeRTSEx.setCode(618);
			aeRTSEx.displayError(getFrame());
			processData(AbstractViewController.CANCEL, null);
		}
		else
		{
			aeRTSEx.displayError(getFrame());
		}
	}
	/**
	 * Set View checks if frame = null, set the screen and pass control
	 * to the controller.
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
		try
		{
			if (getFrame() == null)
			{
				csTransCd = asTransCode;
				RTSDialogBox laRTSDB = getMediator().getParent();
				if (laRTSDB == null)
				{
					setFrame(
						new FrmSpecialPlateMFRecordNotAvailableSPL004(
							getMediator().getDesktop()));
				}
				else
				{
					setFrame(
						new FrmSpecialPlateMFRecordNotAvailableSPL004(laRTSDB));
				}
				cvPreviousControllers = avPreviousControllers;
				super.setView(
					avPreviousControllers,
					asTransCode,
					aaData);
			}
			if (aaData == null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					ERRMSG_DATA_IS_NULL,
					MiscellaneousRegConstant.ERROR_TITLE);
			}
			else if (aaData instanceof Vector)
			{

				Vector lvNextVC = (Vector) aaData;
				if (lvNextVC != null)
				{
					if (lvNextVC.size() == 2)
					{
						if (lvNextVC.get(0) instanceof String)
						{
							processData(REDIRECT, lvNextVC);
						}
						else if (lvNextVC.get(0) instanceof Boolean)
						{
							processData(
								REDIRECT_IS_NEXT_VC_REG029,
								lvNextVC);

						}
						else
						{
							RTSException leRTSEx =
								new RTSException(
									RTSException.FAILURE_MESSAGE,
									ERRMSG_DATA_MISS,
									ERRMSG_ERROR);
							leRTSEx.displayError(getFrame());
							return;
						}
					}
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			RTSDialogBox laTSDialogBox = getMediator().getParent();
			if (laTSDialogBox != null)
			{
				aeRTSEx.displayError(laTSDialogBox);
			}
			else
			{
				aeRTSEx.displayError(getFrame());
			}
			processData(CANCEL, null);
		}
	}

}
