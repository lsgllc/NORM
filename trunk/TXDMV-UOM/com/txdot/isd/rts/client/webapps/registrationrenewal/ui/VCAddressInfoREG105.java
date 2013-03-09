package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;
/*
 *
 * VCAddressInfoREG105.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		02/28/2005	Get code up to standards.
 * 							Changed setvisible to setVisibleRTS.
 * 							modify processData()
 *							defect 7888 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7888 Ver 5.2.3 
 * Jeff S.		06/17/2005	Renamed class to have the frame number.
 * 							defect 7889 Ver 5.2.3
 * K Harrell	03/03/2010	Remove try/catch. Add ENTER.
 * 							Modify CANCEL case to use DirectionFlow: 
 * 							 CANCEL. Implement close() vs. setVisible().
 * 							modify processData() 
 * 							defect 10372 Ver POS_640 
 * ---------------------------------------------------------------------
 */
/**
 * VC for FrmAddressInfoREG105.
 *
 * @version	POS_640		03/03/2010
 * @author	GDONOSO
 * <br>Creation Date:	11/27/2001 19:29:45
 */
public class VCAddressInfoREG105 extends AbstractViewController
{

	/**
	 * VCAddressInfoREG105 constructor comment.
	 */
	public VCAddressInfoREG105()
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
		return GeneralConstant.INTERNET_REG_REN_PROCESSING;
	}
	
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int - Command so the Frame can communicate 
	 * 							with the VC
	 * @param data Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		// defect 10372 
		// Remove try/catch 
		// Add ENTER.
		// Modify CANCEL case to use DirectionFlow: CANCEL. 
		// Implement close() vs. setVisible(). 
 		switch (aiCommand)
		{
			case ENTER :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);

					try
					{
						getMediator().processData(
							getModuleName(),
							RegRenProcessingConstants.NO_DATA,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					close();
					break;
				}
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
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					close();
					break;
				}
		}
		// end defect 10372 
	}

	/**
	 * Retrieves apporpriate data if necessary, stores variables, 
	 * displays frame, and passes control.
	 *
	 * @param avPreviousControllers vector containing the names 
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
			if (getMediator().getParent() != null)
			{
				setFrame(
					new FrmAddressInfoREG105(
						getMediator().getParent()));
			}
			else
			{
				setFrame(
					new FrmAddressInfoREG105(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
