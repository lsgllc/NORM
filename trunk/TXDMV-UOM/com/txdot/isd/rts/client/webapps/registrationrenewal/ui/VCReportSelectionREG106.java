package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;

/*
 *
 * VCReportSelectionREG106.java
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
 * K Harrell	02/09/2009	Modify to use processData() to create and 
 * 							present report vs. just present.  Previously
 * 							created w/in Frame.  
 * 							add REPORTID_ELEMENT_NUM
 * 							modify processData(); 
 * 							defect 9935 Ver Defect_POS_D 
 * ---------------------------------------------------------------------
 */
 
/**
 * VC for FrmReportSelectionREG106.
 *
 * @version	Defect_POS_D	02/09/2009
 * @author	Administrator
 * <br>Creation Date:		11/27/2001 19:29:45
 */
public class VCReportSelectionREG106 extends AbstractViewController
{
	// defect 9935 
	private final static int REPORTID_ELEMENT_NUM = 0;
	// end defect 9935  

	/**
	 * VCReportSelectionREG106 constructor
	 */
	public VCReportSelectionREG106()
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
				case CANCEL :
					{
						getFrame().setVisibleRTS(false);
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
						break;
					}
				case ENTER :
					{
						setData(aaData);
						setNextController(ScreenConstant.RPR000);
						setDirectionFlow(AbstractViewController.NEXT);
						try
						{
							// defect 9935 
							ReportSearchData laData =
								(ReportSearchData)
									((Vector) aaData).elementAt(
									REPORTID_ELEMENT_NUM);
							int liReportId = laData.getIntKey4();

							getMediator().processData(
								getModuleName(),
								liReportId,
								aaData);
							// end defect 9935 
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
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
	 * Retrieves appropriate data if necessary, stores variables, 
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
			setFrame(
				new FrmReportSelectionREG106(
					getMediator().getDesktop()));
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
