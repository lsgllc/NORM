package com.txdot.isd.rts.client.registration.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.data.SpecialPlatesRegisData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCPlateSelectionREG011.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	02/28/2007	Class created.
 * 							defect 9126 Ver Special Plates
 * K Harrell	03/04/2007	Testing with VI
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/18/2007	Renamed SPL005 to SPL004
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------
 */

/**
 * Plate Selection Screen (REG011) view controller class. It handles 
 * screen navigation and controls the visibility of its frame.
 * 
 * @version	Special Plates		06/18/2007
 * @author	Bill Hargrove
 * <br>Creation Date:			02/23/2007     11:18:00 
 */

public class VCPlateSelectionREG011 extends AbstractViewController
{
	public final static int SPL002 = 20;
	public final static int REDIRECT_IS_NEXT_VC_REG029 = 22;
	public final static int REDIRECT_NEXT_VC = 23;
	public final static int GET_NEXT_VI_ITEM_NO = 24;
	public final static int ADD_TRANS = 25;

	/**
	 * VCPlateSelectionREG011 constructor.
	 */
	public VCPlateSelectionREG011()
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
	 * Controls the screen flow from REG011.  It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int A constant letting the VC know which action
	 *  to perform
	 * @param aaData Object The data from the frame
	 * @see com.txdot.isd.rts.client.general.ui.AbstractViewController
	 */
	public void processData(int aiCommand, Object aaData)
	{
		this.setData(aaData);
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				{
					try
					{
						// determine if next vc is reg029
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.COMMON,
							CommonConstant.IS_NEXT_VC_REG029,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
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
			case SPL002 :
				{
					boolean lbMFDownSP = 
							((SpecialPlatesRegisData)
								(((VehicleInquiryData) aaData)
						.getMfVehicleData()
						.getSpclPltRegisData()))
						.isMFDownSP();
	 
					if (lbMFDownSP) 
					{
						setNextController(ScreenConstant.SPL004);
					}
					else
					{
						setNextController(ScreenConstant.SPL002);
					}
					setDirectionFlow(AbstractViewController.NEXT);

					try
					{
						getMediator().processData(
							getModuleName(),
							SpecialPlatesConstant.NO_DATA_TO_BUSINESS,
							aaData);
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
			case GET_NEXT_VI_ITEM_NO :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.DIRECT_CALL);
						aaData =
							getMediator().processData(
								GeneralConstant.SPECIALPLATES,
								InventoryConstant
									.INV_GET_NEXT_VI_ITEM_NO,
								aaData);

						processData(SPL002, aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
						getFrame().setData(aaData);
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
					// use close() so that it does setVisibleRTS()
					close();
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
		}
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
					setFrame(
						new FrmPlateSelectionREG011(laRTSDialogBox));
				}
				else
				{
					setFrame(
						new FrmPlateSelectionREG011(
							getMediator().getDesktop()));
				}
			}

			if (aaData == null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					"data is null",
					MiscellaneousRegConstant.ERROR_TITLE);
			}

			if (aaData instanceof Vector)
			{
				this.setData(aaData);
				this.setPreviousControllers(avPreviousControllers);
				this.setTransCode(asTransCode);
				getFrame().setController(this);
				getFrame().setData(aaData);
			}
			else if (aaData instanceof VehicleInquiryData)
			{
				super.setView(
					avPreviousControllers,
					asTransCode,
					aaData);
			}
			else if (aaData instanceof InventoryAllocationData)
			{
				this.setData(aaData);
				getFrame().setController(this);
				getFrame().setData(aaData);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}
}
