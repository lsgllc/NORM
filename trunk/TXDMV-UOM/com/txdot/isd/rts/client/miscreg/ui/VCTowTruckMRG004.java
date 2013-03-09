package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCTowTruckMRG004.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	07/05/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 *							defect 7893 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */

/**
 * Miscellaneous Registration Tow Truck (MRG004) view controller class.
 *
 * @version	5.2.4			08/11/2006
 * @author	Joseph Kwik
 * <br>Creation Date:		06/26/2001 14:22:56
 */

public class VCTowTruckMRG004 extends AbstractViewController
{

	public final static int REDIRECT_IS_NEXT_VC_REG029 = 30;
	public final static int REDIRECT_NEXT_VC = 33;
	/**
	* VCRegistrationAdditionalInfoREG039 default constructor.
	*/
	public VCTowTruckMRG004()
	{
		super();
	}
	/**
	 * get Module Name MISCELLANEOUSREG
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.MISCELLANEOUSREG;
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
				try
				{
					// determine if next vc is reg029
					setDirectionFlow(AbstractViewController.CURRENT);
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

			case AbstractViewController.CANCEL :
			{
				setDirectionFlow(AbstractViewController.FINAL);
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
				// defect 8884
				// use close() so that it does setVisibleRTS()
				close();
				//getFrame().setVisible(false);
				// end 8884
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
						(CompleteTransactionData) lvIsNextVCREG029
							.get(1);
					if (lbGoToREG029.equals(Boolean.TRUE))
					{
						setNextController(ScreenConstant.REG029);
						setDirectionFlow(AbstractViewController.NEXT);
						getMediator().processData(
							GeneralConstant.COMMON,
							MiscellaneousRegConstant
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
						setDirectionFlow(AbstractViewController.NEXT);
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
				setFrame(new FrmTowTruckMRG004(laRTSDBox));
			}
			else
			{
				setFrame(new FrmTowTruckMRG004(getMediator().getDesktop()));
			}
		}
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
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
	}
}
