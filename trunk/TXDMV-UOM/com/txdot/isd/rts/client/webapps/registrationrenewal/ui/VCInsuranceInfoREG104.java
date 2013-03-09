package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;

/*
 *
 * VCInsuranceInfoREG104.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Johnston	02/24/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							Changed setvisible to setVisibleRTS.
 * 							modify processData()
 *							defect 7889 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * Jeff S.		06/17/2005	Renamed class to have the frame number.
 * 							defect 7889 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * VC for FrmInsuranceInfoREG104.
 *
 * @version	5.2.3		06/17/2005
 * @author	Administrator
 * <p>Creation Date:	11/27/2001 19:29:45
 */
public class VCInsuranceInfoREG104 extends AbstractViewController
{
	/**
	 * VCInsuranceInfoREG104 constructor
	 */
	public VCInsuranceInfoREG104()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own
	 * module name.
	 * 
	 * @return int - The Module Name
	 */
	public int getModuleName()
	{
		return GeneralConstant.INTERNET_REG_REN_PROCESSING;
	}
	/**
	 * All subclasses must override this method to handle data coming
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the
	 * RTSMediator.
	 * 
	 * @param iaCommand int the command so the Frame can communicate
	 * 	with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		try
		{
			switch (aiCommand)
			{
				case CANCEL :
					{
						setDirectionFlow(AbstractViewController.CANCEL);
						try
						{
							getMediator().processData(
								getModuleName(),
								RegRenProcessingConstants.NO_DATA,
								aaData);
						}
						catch (RTSException leEx)
						{
							leEx.displayError(getFrame());
						}
						// defect 7889
						// Changed setVisible to setVisibleRTS
						//getFrame().setVisible(false);
						getFrame().setVisibleRTS(false);
						// end defect 7889
						break;
					}
			}
		}
		catch (Exception leEx)
		{
			// empty code block
		}
	}
	/**
	 * Retrieves apporpriate data if necessary, stores variables, 
	 * displays frame, and passes control.
	 *
	 * @param avPreviousControllers Vector containing the names 
	 * 								of the previous controllers 
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
			RTSDialogBox laRTSDBox = getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmInsuranceInfoREG104(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmInsuranceInfoREG104(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}