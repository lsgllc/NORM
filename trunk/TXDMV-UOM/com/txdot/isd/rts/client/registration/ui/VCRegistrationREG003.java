package com.txdot.isd.rts.client.registration.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCRegistrationREG003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		09/19/2001	Modify per first code review.
 * J Kwik		04/12/2002	Do not use frame for displayError when 
 * 							frame is not visible use 
 * 							mediator.getDesktop().
 *							defect 3452
 * J Kwik		05/01/2002	Fix OS2 window flash in setView - 
 * 							check if mediator's parent is null
 *							when calling displayError.
 * J Kwik		05/21/2002	Fix defect CQU100004008 - If headquarters
 * 							then display message whether to complete 
 * 							trans and don't display Fees Due PMT004 
 * 							window
 * K Harrell    03/27/2003  Fix defect CQU100004840 - Would bypass 
 * 							REG025 on Modify if from CTL005 screen 
 * 							(Cancelled/Archive)
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import	
 * 							defect 7705 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7899  Ver 5.2.3                  
 * B Hargrove	06/23/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 *  	 					Use new getters\setters for frame, 
 *							controller, etc.
 * 	 						Remove unused methods, variables. Add 
 * 							If-Else braces.
 * 							defect 7894 Ver 5.2.3
 * B Hargrove	07/20/2005	Refactor\Move 
 * 							RegistrationModifyData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * K Harrell	12/20/2005	Vehicle Inquiry should update Pending
 * 							Transaction Screen
 * 							defect 7894 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * T Pederson	02/15/2007	Added code to handle Special Plates button. 
 *							modify processData()
 * 							defect 9123 Ver Special Plates
 * J Rue		02/15/2007	Backout Special Plates SPL002 function
 * 							modify processData()
 * 							defect 9086 Ver Special Plates
 * K Harrell	03/04/2007	Modified for Special Plates 
 * 							add PLATE_SELECTION 		
 * 							modify processData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/15/2007	Present Screen SPL004 vs SPL002 if in MF Down 	
 * 							modify processData() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/18/2007	Renamed SPL005 to SPL004 
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/04/2009	remove INQ008 reference
 * 							delete INQ008
 * 							modify processData()
 * 							defect 10112 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * Registration (REG003) view controller class.  It handles screen 
 * navigation and controls the visibility of its frame.
 *
 * @version	Defect_POS_F	08/04/2009
 * @author	Joseph Kwik
 * <br>Creation Date:		08/28/2001 08:27:41
 */

public class VCRegistrationREG003 extends AbstractViewController
{
	// defect 9085 
	public final static int PLATE_SELECTION = 19;
	// end defect 9085 
	public final static int OWNER_ADDRESS = 20;
	public final static int ADDITIONAL_INFO = 21;
	public final static int INIT_REG003 = 22;
	public final static int VTR_AUTHORIZATION = 23;
	public final static int SUPV_OVRIDE = 24;
	public final static int ACC004 = 25;
	public final static int ACC005 = 26;
	public final static int ACC006 = 27;
	public final static int INQ007 = 28;
	public final static int REPL_CHOICES = 29;
	public final static int REDIRECT_IS_NEXT_VC_REG029 = 30;
	public final static int REG_CORRTN = 31;
	public final static int STKR_CHOICES = 32;
	public final static int REDIRECT_NEXT_VC = 33;
	public final static int SPECIAL_PLATES = 34;
	// defect 10112
	//public final static int INQ008 = 40;
	// end defect 10112 
	public final static int TTL005 = 41;

	// boolean
	private boolean cbInitReg003HasError = false;

	// Object 
	private VehicleInquiryData caOrigVehInqData;

	/**
	 * VCRegistrationREG003 default constructor.
	 */
	public VCRegistrationREG003()
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
	 * Controls the screen flow from REG003.  It passes the data to the 
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
			case ENTER :
				{
					try
					{
						// If Addr || Veh Inquiry
						if (getTransCode().equals(TransCdConstant.ADDR)
							|| (getTransCode()
								.equals(TransCdConstant.VEHINQ)))
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
						else if (
							getTransCode().equals(TransCdConstant.DUPL)
								|| getTransCode().equals(
									TransCdConstant.PAWT))
						{
							if (SystemProperty.isHQ())
							{
								if (Transaction
									.getTransactionHeaderData()
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
								setNextController(
									ScreenConstant.PMT004);
								getMediator().processData(
									getModuleName(),
									RegistrationConstant
										.NO_DATA_TO_BUSINESS,
									aaData);
							}
						}
						else if (
							caOrigVehInqData.getValidationObject()
								instanceof RegistrationModifyData
								&& (
									(RegistrationModifyData) caOrigVehInqData
									.getValidationObject())
									.getRegModifyType()
									== RegistrationConstant
										.REG_MODIFY_APPREHENDED)
						{
							if (SystemProperty.isHQ())
							{
								if (Transaction
									.getTransactionHeaderData()
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
								// Modify - Apprehended
								setDirectionFlow(
									AbstractViewController.NEXT);
								setNextController(
									ScreenConstant.PMT004);
								getMediator().processData(
									getModuleName(),
									RegistrationConstant
										.NO_DATA_TO_BUSINESS,
									aaData);
							}
						}
						else
						{
							// determine if next vc is reg029
							setDirectionFlow(
								AbstractViewController.CURRENT);
							getMediator().processData(
								GeneralConstant.COMMON,
								CommonConstant.IS_NEXT_VC_REG029,
								aaData);
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case CANCEL :
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
							RegistrationConstant.NO_DATA_TO_BUSINESS,
							getOrigVehInqData());
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case INIT_REG003 :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							RegistrationConstant.INIT_REG003,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						//e.displayError(getFrame());  Cannot use frame 
						//if it is not visible.
						aeRTSEx.displayError(
							getMediator().getDesktop());
						cbInitReg003HasError = true;
					}
					break;
				}
			case OWNER_ADDRESS :
				{
					try
					{
						if (getTransCode()
							.equals(TransCdConstant.VEHINQ))
						{
							setNextController(ScreenConstant.INQ006);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								getModuleName(),
								RegistrationConstant
									.NO_DATA_TO_BUSINESS,
								aaData);
						}
						else
						{
							setNextController(ScreenConstant.REG033);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								getModuleName(),
								RegistrationConstant
									.NO_DATA_TO_BUSINESS,
								aaData);
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					finally
					{
						break;
					}
				}
				//	defect 9085
			case PLATE_SELECTION :
				{
					setNextController(ScreenConstant.REG011);
					setDirectionFlow(AbstractViewController.NEXT);
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
					break;
				}
				// end defect 9085
			case ADDITIONAL_INFO :
				{
					try
					{
						if (getTransCode()
							.equals(TransCdConstant.VEHINQ))
						{
							setNextController(ScreenConstant.INQ003);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								getModuleName(),
								RegistrationConstant
									.NO_DATA_TO_BUSINESS,
								aaData);
						}
						else
						{
							setNextController(ScreenConstant.REG039);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								getModuleName(),
								RegistrationConstant.INIT_ADDL_INFO,
								aaData);
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					finally
					{
						break;
					}
				}
			case SPECIAL_PLATES :
				{
					try
					{
						if (getTransCode()
							.equals(TransCdConstant.VEHINQ))
						{
							setNextController(ScreenConstant.INQ005);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								getModuleName(),
								RegistrationConstant
									.NO_DATA_TO_BUSINESS,
								aaData);
						}
						else
						{
							VehicleInquiryData laVehInqData =
								(VehicleInquiryData) aaData;

							SpecialPlatesRegisData laSpclPltRegisData =
								laVehInqData
									.getMfVehicleData()
									.getSpclPltRegisData();
							if (laSpclPltRegisData.isMFDownSP())
							{
								setNextController(
									ScreenConstant.SPL004);
							}
							else
							{
								setNextController(
									ScreenConstant.SPL002);
							}
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								getModuleName(),
								RegistrationConstant
									.NO_DATA_TO_BUSINESS,
								aaData);
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					finally
					{
						break;
					}
				}
			case VTR_AUTHORIZATION :
				{
					try
					{
						setNextController(ScreenConstant.CTL003);
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
			case ACC004 :
				{
					try
					{
						setNextController(ScreenConstant.ACC004);
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
			case ACC005 :
				{
					try
					{
						setNextController(ScreenConstant.ACC005);
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
			case ACC006 :
				{
					try
					{
						setNextController(ScreenConstant.ACC006);
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
			case INQ007 :
				{
					try
					{
						setNextController(ScreenConstant.INQ007);
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
				// defect 10112 
				// Not used 
				//			case INQ008 :
				//				{
				//					try
				//					{
				//						setNextController(ScreenConstant.INQ008);
				//						setDirectionFlow(AbstractViewController.NEXT);
				//						getMediator().processData(
				//							getModuleName(),
				//							RegistrationConstant.NO_DATA_TO_BUSINESS,
				//							aaData);
				//					}
				//					catch (RTSException aeRTSEx)
				//					{
				//						aeRTSEx.displayError(getFrame());
				//					}
				//					break;
				//				}
				// end defect 10112 
			case TTL005 :
				{
					try
					{
						setNextController(ScreenConstant.TTL005);
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
			case REPL_CHOICES :
				{
					try
					{
						setNextController(ScreenConstant.REG016);
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
			case REG_CORRTN :
				{
					try
					{
						setNextController(ScreenConstant.REG025);
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
			case STKR_CHOICES :
				{
					try
					{
						setNextController(ScreenConstant.REG001);
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
			default :
				{
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
	 *  the String names of the previous controllers in order
	 * @param asTransCode String The TransCode
	 * @param aaData Object The data object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDialogBox = getMediator().getParent();
			if (laRTSDialogBox != null)
			{
				setFrame(new FrmRegistrationREG003(laRTSDialogBox));
			}
			else
			{
				setFrame(
					new FrmRegistrationREG003(
						getMediator().getDesktop()));
			}
		}
		try
		{
			if (aaData instanceof Vector)
			{
				this.setData(aaData);
				this.setPreviousControllers(avPreviousControllers);
				this.setTransCode(asTransCode);
				getFrame().setController(this);
				getFrame().setData(aaData);
			}
			else
			{
				if (caOrigVehInqData != null)
				{
					//test if 'Enter' from Owner Address
					VehicleInquiryData laVehInqData =
						(VehicleInquiryData) aaData;
					RegistrationValidationData laRegValidData =
						(RegistrationValidationData) laVehInqData
							.getValidationObject();
					if (laRegValidData.isInitREG003())
					{
						laRegValidData.setInitREG003(false);
						return;
						//already back from REG_CONTROLLER; return to 
						//outer setView()
					}
					super.setView(
						avPreviousControllers,
						asTransCode,
						aaData);
					return;
				}
				setOrigVehInqData(
					(VehicleInquiryData) UtilityMethods.copy(aaData));
				VehicleInquiryData laVehInqData =
					(VehicleInquiryData) aaData;
				if (laVehInqData.getValidationObject() == null
					|| laVehInqData.getValidationObject()
						instanceof RegistrationModifyData
					|| laVehInqData.getMfDown() == 1)
				{
					this.setTransCode(asTransCode);
					RegistrationValidationData laRegValidData =
						new RegistrationValidationData();
					laRegValidData.setTransCode(asTransCode);
					// CQU10004820 - Setup for REG0025
					boolean lbIsModify =
						(laVehInqData.getValidationObject()
							instanceof RegistrationModifyData);
					if (!lbIsModify
						&& getMediator().getDesktop().getTitle().equals(
							"RTS: Registration Correction"))
					{
						laRegValidData.setRegModify(
							RegistrationConstant.REG_MODIFY_REG);
					}
					// check if registration modify events; check for 
					//RegistrationModifyData
					if (lbIsModify)
					{
						RegistrationModifyData laRegModData =
							(RegistrationModifyData) laVehInqData
								.getValidationObject();
						laRegValidData.setRegModify(
							laRegModData.getRegModifyType());
					}
					int liCaryngCap =
						laVehInqData
							.getMfVehicleData()
							.getRegData()
							.getVehGrossWt()
							- laVehInqData
								.getMfVehicleData()
								.getVehicleData()
								.getVehEmptyWt();
					laVehInqData
						.getMfVehicleData()
						.getRegData()
						.setVehCaryngCap(
						liCaryngCap);
					laRegValidData.setOrigCaryngCap(liCaryngCap);
					// for vehicle weight form
					//lRegValidData.setOrigCaryngCap(lVehInqData.
					//getMfVehicleData().getRegData().
					//getVehCaryngCap()); 
					// for vehicle weight form
					laVehInqData.setValidationObject(laRegValidData);
					if (asTransCode.equals(TransCdConstant.RENEW)
						|| asTransCode.equals(TransCdConstant.DUPL)
						|| asTransCode.equals(TransCdConstant.EXCH)
						|| asTransCode.equals(TransCdConstant.CORREG)
						|| asTransCode.equals(TransCdConstant.PAWT)
						|| asTransCode.equals(TransCdConstant.RNR)
						|| asTransCode.equals(TransCdConstant.REPL)
						|| asTransCode.equals(TransCdConstant.ADDR))
					{
						processData(INIT_REG003, laVehInqData);
						if (cbInitReg003HasError)
						{
							processData(CANCEL, laVehInqData);
							// reset desktop title
							return;
						}
						Vector lvRTSExceptions =
							laRegValidData.getRTSExceptions();
						if (lvRTSExceptions.size() > 0)
						{
							throw new RTSException(lvRTSExceptions);
						}
					}
					super.setView(
						avPreviousControllers,
						asTransCode,
						aaData);
				}
				else
				{
					// empty code block
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
			super.setView(
				getPreviousControllers(),
				asTransCode,
				aaData);
		}
	}
}
