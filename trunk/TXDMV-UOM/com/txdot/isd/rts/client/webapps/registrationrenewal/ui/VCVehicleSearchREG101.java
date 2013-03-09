package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.webapps.util.constants.
		RegRenProcessingConstants;

/*
 *
 * VCVehicleSearchREG101.java
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
 * Jeff S.		08/16/2005	Used new screen constants that match the 
 * 							frame number.
 * 							modify processData()
 * 							defect 7889 Ver 5.2.3 
 * B Brown		01/28/2009	Display the FrmSearchResultsREG102 screen 
 * 							only when a "searched for" record is found.
 * 							Otherwise, display a no records found
 * 							message. 
 * 							modify processData()
 * 							defect 8765 Ver Defect_POS_D
 * B Brown		01/30/2009	Change the implementation for displaying
 * 							the no record found messsage to calling
 * 							RTSMediator with the "Current" parm first,	 
 * 							and catching the returned info in setData.
 * 							Then handle the error message screen or the 
 * 							REG102 presentation call in setView. 
 * 							modify processData(), setView()
 * 							defect 8765 Ver Defect_POS_D
 * ---------------------------------------------------------------------
 */
/**
 * VC for FrmVehicleSearchREG101.
 *
 * @version	5.2.3		08/16/2005
 * @author	Administrator
 * <p>Creation Date:	11/27/01 11:56:17 AM
 */
public class VCVehicleSearchREG101 extends AbstractViewController
{
	// defect 8765
	private final static int RECORDS_FOUND = 29;
	// end defect 8765
	/**
	 * VCVehicleSearchREG101 constructor
	 */
	public VCVehicleSearchREG101()
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
	 * @param aiCommand int the command so the Frame can communicate
	 * 	with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		try
		{
			switch (aiCommand)
			{
				case ENTER:
				{
					setData(aaData);
					// defect 8765
//					setNextController(ScreenConstant.REG102);
//					setDirectionFlow(AbstractViewController.NEXT);
					setDirectionFlow(AbstractViewController.CURRENT);
					// end defect 8765
					try
					{
						getMediator().processData(getModuleName(),
							RegRenProcessingConstants.GET_PROC_VEHICLE,
							aaData);
					}
					catch (RTSException leEx)
					{
						leEx.displayError(getFrame());
					}
					break;
				}
				// defect 8765
				case RECORDS_FOUND:
				{
					try
					{
						setNextController(ScreenConstant.REG102);
						setDirectionFlow(AbstractViewController.NEXT);
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leEx)
					{
						leEx.displayError(getFrame());
					}
					break;
				}
				// end defect 8765
				case CANCEL:
				{
					// defect 7889
					// Changed setVisible to setVisibleRTS
					//getFrame().setVisible(false);
					getFrame().setVisibleRTS(false);
					// end defect 7889
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						getMediator().processData(getModuleName(),
							RegRenProcessingConstants.NO_DATA, aaData);
					}
					catch (RTSException leEx)
					{
						leEx.displayError(getFrame());
					}
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
	public void setView(Vector avPreviousControllers,
		String asTransCode,	Object aaData)
	{
		if (getFrame() == null)
		{
			setFrame(new FrmVehicleSearchREG101(getMediator().getDesktop() ));
		}
		// defect 8765
		else if (aaData instanceof Hashtable)
		{
			Vector lvRetVal =
				(Vector) ((Hashtable) aaData).get("ReturnValues");
			if (lvRetVal.isEmpty())
			{
				(new RTSException(182)).displayError(getFrame());
			}
			else
			{
				processData(RECORDS_FOUND, aaData);
			}
		}
		// end defect 8765
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
