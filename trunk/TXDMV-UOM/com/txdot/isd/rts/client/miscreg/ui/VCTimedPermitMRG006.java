package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.PermitData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCTimedPermitMRG006.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	07/15/2005	Modify code for move to Java 1.4.
 *  	 					Use new getters\setters for frame, 
 *							controller, etc. Bring code to standards.
 *							defect 7893 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * K Harrell	05/24/2010	Update for new Timed Permit Processing 
 * 							delete ONE_TRIP_PERMIT, THIRTY_DAY_PERMIT,
 * 							 REDIRECT_IS_NEXT_VC_REG029,
 * 							 REDIRECT_NEXT_VC, ERRMSG_DATA_IS_NULL
 * 							modify processData(), setView()   
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	07/07/2010	Add logic for HQ processing - does not go 
 * 							to Fees Due. 
 * 							add ADD_TRANS
 * 							modify processData() 
 * 							defect 10491 Ver 6.5.0
 * K Harrell	10/13/2010	include retry on DB Error SQL0911  
 * 							modify processData()
 * 							defect 10625 Ver 6.5.0   
 * ---------------------------------------------------------------------
 */

/**
 * Miscellaneous Registration Timed Permit (MRG006) view controller 
 * class.
 *
 * @version	6.5.0  			10/13/2010
 * @author	Joseph Kwik
 * <br>Creation Date:		06/26/2001 14:22:56
 */

public class VCTimedPermitMRG006 extends AbstractViewController
{
	private static final String ERRMSG_DATA_MISS =
		"Data missing for NextVC";
	private static final String ERRMSG_ERROR = "ERROR";
	public final static int REDIRECT = 22;

	// defect 10491
	public final static int ADD_TRANS = 23;
	// private final static String ERRMSG_DATA_IS_NULL = "data is null";
	// public final static int ONE_TRIP_PERMIT = 20;
	// public final static int THIRTY_DAY_PERMIT = 21;
	// public final static int REDIRECT_IS_NEXT_VC_REG029 = 23;
	// public final static int REDIRECT_NEXT_VC = 24; 
	// end defect 10491

	/**
	* VCTimedPermitMRG006 default constructor.
	*/
	public VCTimedPermitMRG006()
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
	 * The following handles DB or Server Down conditions for:
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
		}
		else
		{
			aeRTSEx.displayError(getFrame());
		}
	}

	/**
	 * Process Data
	 * 
	 * @param aiCommand int
	 * @param aiaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
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
				// defect 10491 
			case ADD_TRANS :
				{
					setDirectionFlow(AbstractViewController.FINAL);
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
					break;
				}
			case AbstractViewController.FINAL :
			case AbstractViewController.CANCEL :
				{
					//setDirectionFlow(AbstractViewController.CANCEL);
					setDirectionFlow(aiCommand);
					// end defect 10491 

					try
					{
						getMediator().processData(
							getModuleName(),
							MiscellaneousRegConstant
								.NO_DATA_TO_BUSINESS,
							null);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					close();
					break;
				}
				// defect 10491 
			case InventoryConstant.INV_VI_RELEASE_HOLD :
				{
					// defect 10625 
					// Retry on DB2 -911 Error 
					for (int i = 0; i < 3; i++)
					{
						try
						{
							setDirectionFlow(
								AbstractViewController.DIRECT_CALL);
							aaData =
								getMediator().processData(
									getModuleName(),
									InventoryConstant
										.INV_VI_RELEASE_HOLD,
									aaData);
							break;
						}
						catch (RTSException aeRTSEx)
						{
							if ((aeRTSEx
								.getDetailMsg()
								.indexOf("SQL0911")
								>= 0
								|| aeRTSEx.getDetailMsg().indexOf(
									"SQLCODE=-911")
									>= 0)
								&& i < 2)
							{
								try
								{
									Thread.sleep(1000);
								}
								catch (InterruptedException aeIntEx)
								{
									System.err.println(
										aeIntEx.getMessage());
								}
							}
							else
							{
								if (i != 2)
								{
									if (aeRTSEx.getCode()
										!= ErrorsConstant
											.ERR_NUM_VI_ITEM_NOT_RESERVED)
									{
										handleRTSException(aeRTSEx);
									}
								}
							}
						}
					}
					// end defect 10625 

					break;
				}
				// end defect 10491 
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

				// defect 10491 

				//			case THIRTY_DAY_PERMIT :
				//				{
				//					try
				//					{
				//						setDirectionFlow(AbstractViewController.NEXT);
				//						setNextController(ScreenConstant.MRG001);
				//						getMediator().processData(
				//							getModuleName(),
				//							MiscellaneousRegConstant
				//								.NO_DATA_TO_BUSINESS,
				//							aaData);
				//					}
				//					catch (RTSException aeRTSEx)
				//					{
				//						aeRTSEx.displayError(getFrame());
				//					}
				//					break;
				//				}
				//			case ONE_TRIP_PERMIT :
				//				{
				//					try
				//					{
				//						setDirectionFlow(AbstractViewController.NEXT);
				//						setNextController(ScreenConstant.MRG008);
				//						getMediator().processData(
				//							getModuleName(),
				//							MiscellaneousRegConstant
				//								.NO_DATA_TO_BUSINESS,
				//							aaData);
				//					}
				//					catch (RTSException aeRTSEx)
				//					{
				//						aeRTSEx.displayError(getFrame());
				//					}
				//					break;
				//				}
				//			case REDIRECT_IS_NEXT_VC_REG029 :
				//				{
				//					try
				//					{
				//						Vector lvIsNextVCREG029 = (Vector) aaData;
				//						Boolean lbGoToREG029 =
				//							(Boolean) lvIsNextVCREG029.get(0);
				//						// first element is flag whether to go to REG029
				//						CompleteTransactionData laData =
				//							(
				//								CompleteTransactionData) lvIsNextVCREG029
				//									.get(
				//								1);
				//						if (lbGoToREG029.equals(Boolean.TRUE))
				//						{
				//							setNextController(ScreenConstant.REG029);
				//							setDirectionFlow(
				//								AbstractViewController.NEXT);
				//							getMediator().processData(
				//								GeneralConstant.COMMON,
				//								RegistrationConstant
				//									.NO_DATA_TO_BUSINESS,
				//								laData);
				//						}
				//						else
				//						{
				//							// determine next vc if NOT reg029
				//							setDirectionFlow(
				//								AbstractViewController.CURRENT);
				//							getMediator().processData(
				//								GeneralConstant.COMMON,
				//								CommonConstant
				//									.GET_NEXT_COMPLETE_TRANS_VC,
				//								((Vector) aaData).elementAt(1));
				//						}
				//					}
				//					catch (RTSException aeRTSEx)
				//					{
				//						aeRTSEx.displayError(getFrame());
				//					}
				//					break;
				//				}
				//			case REDIRECT_NEXT_VC :
				//				{
				//					try
				//					{
				//						Vector lvNextVC = (Vector) aaData;
				//						String lsNextVCName = (String) lvNextVC.get(0);
				//						// first element is name of next controller
				//						CompleteTransactionData laData =
				//							(CompleteTransactionData) lvNextVC.get(1);
				//						if (lsNextVCName != null)
				//						{
				//							setNextController(lsNextVCName);
				//							setDirectionFlow(
				//								AbstractViewController.NEXT);
				//							getMediator().processData(
				//								GeneralConstant.COMMON,
				//								RegistrationConstant
				//									.NO_DATA_TO_BUSINESS,
				//								laData);
				//						}
				//					}
				//					catch (RTSException aeRTSEx)
				//					{
				//						aeRTSEx.displayError(getFrame());
				//					}
				//					break;
				//				}
				// end defect 10491 
		}
	}

	/**
	 * This method is called by RTSMediator to display the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector
	 * @param asTransCode String
	 * @param aaData Object
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
				setFrame(new FrmTimedPermitMRG006(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmTimedPermitMRG006(
						getMediator().getDesktop()));
			}
		}
		// defect 10491 
		if (aaData != null)
		{
			if (aaData instanceof PermitData)
			{
				super.setView(
					avPreviousControllers,
					asTransCode,
					aaData);
			}
			else
				// end defect 10491  
				if (aaData instanceof Vector)
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
							// defect 10491 
							//	else if (
							//			lvNextVC.get(0) instanceof Boolean)
							//		{
							//			processData(
							//				REDIRECT_IS_NEXT_VC_REG029,
							//				lvNextVC);
							//		}
							// end defect 10491 
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
	}
}
