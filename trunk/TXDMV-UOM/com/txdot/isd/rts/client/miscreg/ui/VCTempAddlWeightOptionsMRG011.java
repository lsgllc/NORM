package com.txdot.isd.rts.client.miscreg.ui;

//import com.txdot.isd.rts.client.common.business.CompleteTransactionData;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCTempAddlWeightOptionsMRG011.java
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
 * Miscellaneous Registration Temporary Additional Weight Options 
 * (MRG011) view controller class.
 *
 * @version	5.2.4			08/11/2006
 * @author	Joseph Kwik
 * <br>Creation Date:		06/26/2001 14:22:56
 */

public class VCTempAddlWeightOptionsMRG011
	extends AbstractViewController
{
	private final static String ERRMSG_DATA_IS_NULL = "data is null";

	private CompleteTransactionData caOrigCompTransData;
	/**
	* VCTempAddlWeightOptionsMRG011 default constructor.
	*/
	public VCTempAddlWeightOptionsMRG011()
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
	 * Get Orig Comp Trans Data
	 * 
	 * @return CompleteTransactionData
	 */
	private CompleteTransactionData getOrigCompTransData()
	{
		return caOrigCompTransData;
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
				setNextController(ScreenConstant.PMT004);
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
				setDirectionFlow(AbstractViewController.PREVIOUS);
				try
				{
					getMediator().processData(
						getModuleName(),
						MiscellaneousRegConstant
							.NO_DATA_TO_BUSINESS,
						getOrigCompTransData());
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
		}
	}
	/**
	 * Set Orig Comp Trans Data
	 * 
	 * @param aaNewOrigCompTransData CompleteTransactionData
	 */
	private void setOrigCompTransData(CompleteTransactionData 
		aaNewOrigCompTransData)
	{
		caOrigCompTransData = aaNewOrigCompTransData;
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
				setFrame(new FrmTempAddlWeightOptionsMRG011(laRTSDBox));
			}
			else
			{
				setFrame(new FrmTempAddlWeightOptionsMRG011(
					getMediator().getDesktop()));
			}
		}
		try
		{
			if (aaData == null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					ERRMSG_DATA_IS_NULL,
					MiscellaneousRegConstant.ERROR_TITLE);
			}
			else
			{
				if (aaData instanceof CompleteTransactionData)
				{
					setOrigCompTransData(
						(CompleteTransactionData) UtilityMethods.copy(
							aaData));
					super.setView(avPreviousControllers, asTransCode, 
						aaData);
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}
}
