package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 * 
 * VCEnterRegistrationREG029.java
 *  
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		09/26/2002	For defect #4730, added a check in method 
 *							processData for caCompleteTransactionData.
 *							getRegFeesData().getVectFees() is empty. 
 *							This occurs when doing a registration 
 *							exchange to a special plate. If there are 
 *							no fees, we want to add the transaction to 
 *							the trans tables and go back to the desktop.
 * K Harrell	04/25/2003	Defect 6022. Special Plates did not prompt 
 *							for inventory when all not customer 
 *							supplied.
 *							method processData().
 * T Pederson	12/20/2004	Remove check for owner supplied sticker 
 *							number since it was removed in defect 7042.
 *							modify processData()
 *							defect 7498 Ver 5.2.2
 * T Pederson	03/15/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	04/26/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3  
 * T Pederson	10/24/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * K Harrell	02/05/2007	Use PlateTypeCache vs. 
 * 								RegistrationRenewalsCache
 * 							modify processData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/01/2007	As HQ now goes through REG029, force 
 * 							through GET_NEXT_COMPLETE_TRANS_VC for 
 * 							assignment of Reg Exp Mo/Yr. 
 * 							Note:  PMT004 will be shown.
 * 							delete HQ_SPEC_PLTS
 * 							modify proessData()   
 * 							defect 9085 Ver Special Plates    
 * ---------------------------------------------------------------------
 */
/**
 * View Controller for Screen: Enter Registration REG029
 * 
 * @version	Special Plates	08/01/2007
 * @author	Nancy Ting
 * <br>Creation Date:		09/13/2001 09:34:18
 * 
 */
public class VCEnterRegistrationREG029 extends AbstractViewController
{
	// Constants
	public static final int RECAL_FEES = 50;
	public static final int REDIRECT = 51;
	//private static final int HQ_SPEC_PLTS = 291;

	// Object
	private CompleteTransactionData caCompleteTransactionData = null;

	/**
	 * VCEnterRegistrationREG029 constructor
	 */
	public VCEnterRegistrationREG029()
	{
		super();
	}
	/**
	 * Returns the Module name constant used by the RTSMediator to 
	 * pass the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.COMMON;
	}
	/**
	 * Controls the screen flow from REG029.  It passes the data to the 
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
				// defect 4730  
				caCompleteTransactionData =
					(CompleteTransactionData) aaData;
				// defect 6022 - Prompt for Sticker in HQ291 when not specified.
				//  Added check for AnnualPltIndi=1 || (AnnualPltIndi = 0 and Sticker populated

				// defect 9085 
				// As REG029 is always shown for Special Plates, we cannot
				// simply add the transaction, must process 
				// GET_NEXT_COMPLETE_TRANS_VC for assignment of Reg Exp 
				// Mo. 
				// 
				// This is no longer needed.  Maintained for Historical
				// purposes.  KPH  08/01/2007 
				// PlateTypeCache replaces RegistrationRenewalsCache
				//				PlateTypeData laPlateTypeData =
				//					PlateTypeCache.getPlateType(
				//						caCompleteTransactionData
				//							.getVehicleInfo()
				//							.getRegData()
				//							.getRegPltCd());

				//				RegistrationRenewalsData laRegistrationRenewalsData =
				//					RegistrationRenewalsCache.getRegRenwl(
				//						caCompleteTransactionData
				//							.getVehicleInfo()
				//							.getRegData()
				//							.getRegPltCd());

				// defect 7498
				// Remove check for owner supplied sticker number since
				// it was removed in defect 7042
				//				if ((SystemProperty.getOfficeIssuanceNo()
				//					== HQ_SPEC_PLTS)
				//					&& (laPlateTypeData.getAnnualPltIndi() == 1))
				//					//|| (lRegistrationRenewalsData.getAnnualPltIndi() == 0
				//					//&& !(caCompleteTransactionData.getOwnrSuppliedStkrNo().equals("")))))
				//					// end defect 7498
				//				{
				//					setDirectionFlow(AbstractViewController.DESKTOP);
				//					try
				//					{
				//						if (getFrame() != null)
				//						{
				//							getFrame().setVisibleRTS(false);
				//						}
				//						getMediator().processData(
				//							getModuleName(),
				//							CommonConstant.ADD_TRANS,
				//							aaData);
				//					}
				//					catch (RTSException aeRTSEx)
				//					{
				//						aeRTSEx.displayError(getFrame());
				//					}
				//				}
				//				// end defect 6022 
				//				// end defect 4730 change
				//				else
				//				{
				setDirectionFlow(AbstractViewController.CURRENT);
				try
				{
					getMediator().processData(
						getModuleName(),
						CommonConstant.GET_NEXT_COMPLETE_TRANS_VC,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				//}
				// end defect 9085
				break;
			case AbstractViewController.CANCEL :
				setDirectionFlow(AbstractViewController.CANCEL);
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
				getFrame().setVisibleRTS(false);
				break;
			case RECAL_FEES :
				setDirectionFlow(AbstractViewController.CURRENT);
				try
				{
					getMediator().processData(
						getModuleName(),
						CommonConstant.CAL_FEES,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			case REDIRECT :
				setDirectionFlow(AbstractViewController.NEXT);
				Vector lvData = (Vector) aaData;
				setNextController((String) lvData.get(0));
				CompleteTransactionData caCompleteTransactionData =
					(CompleteTransactionData) lvData.get(1);
				try
				{
					getMediator().processData(
						getModuleName(),
						CommonConstant.NO_DATA_TO_BUSINESS,
						caCompleteTransactionData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			default :
				{
					// empty code block
				}
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
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
			RTSDialogBox laRTSDiaBox = getMediator().getParent();
			if (laRTSDiaBox != null)
			{
				setFrame(new FrmEnterRegistrationREG029(laRTSDiaBox));
			}
			else
			{
				setFrame(
					new FrmEnterRegistrationREG029(
						getMediator().getDesktop()));
			}
		}

		if (!(aaData instanceof Vector))
		{
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		else
		{
			setData(aaData);
			setPreviousControllers(avPreviousControllers);
			setTransCode(asTransCode);
			getFrame().setController(this);
			getFrame().setData(aaData);
		}
	}
}
