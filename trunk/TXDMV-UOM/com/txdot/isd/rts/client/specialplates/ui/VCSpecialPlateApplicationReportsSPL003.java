package com.txdot.isd.rts.client.specialplates.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;

/*
 * VCSpecialPlateApplicationReportsSPL003.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/11/2007	Created
 * 							defect 9085 Ver Special Plates  
 * J Rue		02/14/2007	Add Cancel function
 * 							modify processData()
 * 							defect 9086 Ver Special Plates
 * K Harrell	02/17/2007	Modify getModuleName(), processData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/27/2007	Renamed to SPL003    
 * ---------------------------------------------------------------------
 */

/**
 * VC Special Plate Application Reports	SPL003
 * 
 * @version	Special Plates	02/27/2007
 * @author	Kathy Harrell
 * <br>Creation Date:		02/11/2007 15:47:00
 */

public class VCSpecialPlateApplicationReportsSPL003
	extends AbstractViewController
{

	/**
	 * SpecialPlateApplicationSPL003 constructor comment.
	 */
	public VCSpecialPlateApplicationReportsSPL003()
	{
		super();
	}

	/**
	 * All subclasses must override this method to return their own
	 * module name.
	 * @return String
	 */
	public int getModuleName()
	{
		return GeneralConstant.SPECIALPLATES;
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the RTSMediator.
	 * 
	 * @param aiCommand int command so the Frame can communicate with VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setData(aaData);
					setNextController(ScreenConstant.RPR000);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							SpecialPlatesConstant
								.GENERATE_SPCL_PLT_APPL_REPORT,
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
	 * setView
	 * 
	 * @param Vector avPreviousControllers
	 * @param String asTransCode
	 * @param Object aaData
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDiagBox = getMediator().getParent();
			if (laRTSDiagBox != null)
				setFrame(
					new FrmSpecialPlateApplicationReportsSPL003(laRTSDiagBox));
			else
				setFrame(
					new FrmSpecialPlateApplicationReportsSPL003(
						getMediator().getDesktop()));
		}

		super.setView(avPreviousControllers, asTransCode, aaData);

	}

}
