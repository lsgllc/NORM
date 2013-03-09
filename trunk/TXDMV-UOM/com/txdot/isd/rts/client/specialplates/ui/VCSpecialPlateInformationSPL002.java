package com.txdot.isd.rts.client.specialplates.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.registration.business.RegistrationClientUtilityMethods;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/* 
 * VCSpecialPlateInformationSPL002.java
 * 
 * (c) Texas Department of Transportation  2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/25/2003	Original 
 * K Harrell	02/11/2007	Renamed
 * 							defect 9085 Ver Special Plates 
 * J Rue		02/15/2007	Add Cancel function
 * 							Add Enter function, go to Previous
 * 							modify processData()
 * 							defect 9086 Ver Special Plates
 * J Rue		03/26/2007	Set screen path for Cancel
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates
 * K Harrell	05/23/2007	Modify VI calls for new VI architecture
 * 							modify processData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/01/2007	Use UtilityMethods.isSPAPPL() vs. check for
 * 							SPAPPL 
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/05/2007	Only setEnterOnSPL002 if prior screen KEY002
 * 							modify processData()
 * 							defect 9339 Ver Special Plates
 * K Harrell	11/05/2007	Move setEnterOnSPL002 to VCKEY002 to share
 * 							w/ SPL004. 
 * 							modify processData()
 * 							defect 9389 Ver Special Plates 2   	 
 * R Pilon		06/10/2011	Implement Special Plate Inquiry
 * 							add INQ007
 * 							modify processData()
 * 							defect 10820 Ver 6.8.0
 * ---------------------------------------------------------------------
 */
/**
 * VC Special Plate Information SPL002
 * 
 * @version	6.8.0				06/10/2011
 * @author	Jeff Rue
 * @author 	Kathy Harrell
 * <br>Creation Date:			03/26/2003 09:22:28
 */
public class VCSpecialPlateInformationSPL002
	extends AbstractViewController
{

	// Constants
	private static final String ERRMSG_DATA_MISS =
		"Data missing for NextVC";
	private static final String ERRMSG_ERROR = "ERROR";
	public final static int ENTER_FEES = 20;
	public final static int REDIRECT_IS_NEXT_VC_REG029 = 22;
	public final static int REDIRECT_NEXT_VC = 23;
	public final static int ADD_TRANS = 25;
	// defect 10820
	public final static int INQ007 = 28;
	// end defect 10820
	public final static int REDIRECT = 30;
	public final static int RETURN_SPCL_PLT = 35;
	public final static int COMPLETE_REPL = 40;

	private final static String ERRMSG_DATA_IS_NULL = "data is null";

	// String
	String csTransCd = CommonConstant.STR_SPACE_EMPTY;

	// Vector
	Vector cvPreviousControllers = new Vector();

	/**
	 * SpecialPlateInformationSPL002 constructor comment.
	 */
	public VCSpecialPlateInformationSPL002()
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
		return GeneralConstant.SPECIALPLATES;
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
	 * 
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
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the RTSMediator.
	 * 
	 * @param aiCommand int command so the Frame can communicate with VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, java.lang.Object aaData)
	{
		int liVctSize = cvPreviousControllers.size();
		String lsPrevCntrl = "";
		if (liVctSize != 0)
		{
			lsPrevCntrl =
				(String) cvPreviousControllers.get(liVctSize - 1);
		}

		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				{
					// If Replacement and from REG011, complete 
					// transaction. 
					if (csTransCd.equals(TransCdConstant.REPL)
						&& lsPrevCntrl.equals(ScreenConstant.REG011))
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
					// defect 10820
					// if special plates inquiry - temporary trans code 
					else if (getTransCode()
							.equals(TransCdConstant.SPINQ))
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
						try
						{
							getMediator().processData(
								GeneralConstant.COMMON,
								CommonConstant.ADD_TRANS,
								aaData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
					}
					// end defect 10820
					else
					{
						// This case statement is called for NON Special 
						//	Plates events only.
						// Pass control back to previous VC 
						// Only Special Plates data in VehInqData object 
						// 	is returned
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						getFrame().setVisibleRTS(false);

						// defect 9389 
						// Move to VCKEY002 
						// defect 9339 
						// Only set if prior screen KEY002 
						//						if (lsPrevCntrl.equals(ScreenConstant.KEY002))
						//						{
						//							((VehicleInquiryData) aaData)
						//								.getMfVehicleData()
						//								.getSpclPltRegisData()
						//								.setEnterOnSPL002(true);
						//						}
						// end defect 9339 
						// end defect 9389  
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

			case ENTER_FEES :
				{
					setData(aaData);

					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.COMMON,
							CommonConstant.GET_NEXT_COMPLETE_TRANS_VC,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case InventoryConstant.INV_VI_VALIDATE_PER_PLT :
			case InventoryConstant
				.INV_VI_UPDATE_INV_STATUS_CD_RECOVER :
				{
					setDirectionFlow(
						AbstractViewController.DIRECT_CALL);
					try
					{
						aaData =
							getMediator().processData(
								getModuleName(),
								aiCommand,
								aaData);
					}
					catch (RTSException aeRTSEx)
					{
						handleRTSException(aeRTSEx);
						break;
					}
					processData(ADD_TRANS, aaData);
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
					close();
					if (UtilityMethods.isSPAPPL(csTransCd))
					{
						setDirectionFlow(AbstractViewController.CANCEL);
					}
					else if (UtilityMethods.isSpecialPlates(csTransCd))
					{
						setDirectionFlow(AbstractViewController.FINAL);
					} 
					// defect 10820
					// special plates inquiry - temporary trans code 
					else if (csTransCd.equals(TransCdConstant.SPINQ))
					{
						setDirectionFlow(AbstractViewController.FINAL);
					}
					// end defect 10820
					else
					{

						// If Cancel if from REG003 || TTL007
						if (csTransCd.equals(TransCdConstant.REPL)
							|| lsPrevCntrl.equals(ScreenConstant.REG003)
							|| lsPrevCntrl.equals(ScreenConstant.TTL007))
						{
							setDirectionFlow(
								AbstractViewController.CANCEL);
						}
						else
						{
							// Return to REG008
							setPreviousController(
								ScreenConstant.REG008);
							setDirectionFlow(
								AbstractViewController.PREVIOUS);
						}
					}
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

					break;
				}
			// defect 10820
			// special plates inquiry - temporary trans code 
			case INQ007 :
				{
					try
					{
						// set the trans code to vehicle inquiry (VEHINQ) 
						// for processing fees and storing trans in DB
						setTransCode(TransCdConstant.VEHINQ);

						setNextController(ScreenConstant.INQ007);
						setDirectionFlow(AbstractViewController.NEXT);
						getMediator().processData(
							getModuleName(),
							RegistrationConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						// set transaction code back to special plates 
						// inquiry (SPINQ)
						setTransCode(TransCdConstant.SPINQ);
						
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			// end defect 10820
		}
	}

	/**
	 * setView
	 * 
	 * @param Vector avPreviousControllers
	 * @param String asTransCode
	 * @param Object aaData
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
				RTSDialogBox laRTSDiagBox = getMediator().getParent();

				if (laRTSDiagBox != null)
				{
					setFrame(
						new FrmSpecialPlateInformationSPL002(laRTSDiagBox));
				}
				else
				{
					setFrame(
						new FrmSpecialPlateInformationSPL002(
							getMediator().getDesktop()));
				}
				csTransCd = asTransCode;
				cvPreviousControllers = avPreviousControllers;
				super.setView(
					avPreviousControllers,
					asTransCode,
					aaData);
				return;
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
							processData(
								VCSpecialPlateInformationSPL002
									.REDIRECT,
								lvNextVC);
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
