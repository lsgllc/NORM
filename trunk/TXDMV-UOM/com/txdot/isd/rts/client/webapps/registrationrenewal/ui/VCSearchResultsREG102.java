package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;
/*
 *
 * VCSearchResultsREG102.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		02/23/2005	Get code up to standards.
 * 							Changed setvisible to setVisibleRTS.
 * 							modify processData()
 *							defect 7889 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * Jeff S.		06/17/2005	Renamed class to have the frame number.
 * 							defect 7889 Ver 5.2.3
 * Jeff S.		08/16/2005	Used new screen constants that match the 
 * 							frame number.
 * 							modify processData()
 * 							defect 7889 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * VC for FrmSearchResultsREG102.
 *
 * @version	5.2.3		08/16/2005
 * @author	GDONOSO
 * <p>Creation Date:	11/27/2001	14:57:40
 */
public class VCSearchResultsREG102 extends AbstractViewController
{
	/**
	 * VCSearchResultsREG102 constructor comment.
	 */
	public VCSearchResultsREG102()
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
		try
		{
			switch (aiCommand)
			{
				case ENTER :
					{
						setData(aaData);
						setNextController(ScreenConstant.REG103);
						setDirectionFlow(AbstractViewController.NEXT);
						setPreviousController(ScreenConstant.REG102);
						try
						{
							getMediator().processData(
								getModuleName(),
								RegRenProcessingConstants.NO_DATA,
								getData());
						}
						catch (RTSException leRTSEx)
						{
							leRTSEx.displayError(getFrame());
						}
						break;
					}

				case CANCEL :
					{
						// defect 7889
						// Changed setVisible to setVisibleRTS
						//getFrame().setVisible(false);
						getFrame().setVisibleRTS(false);
						// end defect 7889
						setDirectionFlow(AbstractViewController.CANCEL);
						try
						{
							getMediator().processData(
								getModuleName(),
								RegRenProcessingConstants.NO_DATA,
								aaData);
						}
						catch (RTSException leRTSEx)
						{
							leRTSEx.displayError(getFrame());
						}
						break;
					}
			}
		}
		catch (Exception leRTSEx)
		{
		}
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
					new FrmSearchResultsREG102(getMediator().getParent()));
			}
			else
			{
				setFrame(
					new FrmSearchResultsREG102(getMediator().getDesktop()));
			}
		}
		
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}