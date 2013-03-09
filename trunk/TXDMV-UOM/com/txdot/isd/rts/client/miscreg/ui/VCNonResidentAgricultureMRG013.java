package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

/*
 *
 * VCNonResidentAgricultureMRG013.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	03/18/2005	Modify code for move to WSAD from VAJ.
 *							modify for Java 1.4 \ WSAD
 *							defect 7893 Ver 5.2.3
 * B Hargrove	07/05/2005	Use new getters\setters for frame, 
 *							controller, etc. Bring code to standards.
 *							Remove unused imports.
 *							defect 7893 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */

/**
* Miscellaneous Registration Non-Resident Agriculture Permit (MRG013) 
* view controller class.
 *
 * @version	5.2.4			08/11/2006
 * @author	Joseph Kwik
 * <br>Creation Date:		06/26/2001 14:22:56
 */

public class VCNonResidentAgricultureMRG013
	extends AbstractViewController
{
	public final static int REDIRECT = 30;

	/**
	* VCRegistrationAdditionalInfoREG039 default constructor.
	*/
	public VCNonResidentAgricultureMRG013()
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
					// defect 8884
					// use close() so that it does setVisibleRTS()
					close();
					//getFrame().setVisible(false);
					// end 8884
					break;
				}

			case REDIRECT :
				{
					try
					{
						Vector lvNextVC = (Vector) aaData;
						// first element is name of next controller 
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

		}
	}
	/**
	 * This method is called by RTSMediator to display the frame.
	 * 
	 * @param avPreviousControllersjava.util.Vector
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

				setFrame(
					new FrmNonResidentAgricultureMRG013(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmNonResidentAgricultureMRG013(
						getMediator().getDesktop()));
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