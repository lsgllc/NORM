package com.txdot.isd.rts.client.reports.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * VCPreviewReportRPR000.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Duggirala 	09/07/2001  Added comments
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3 
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify processData()
 *							defect 7896 Ver 5.2.3
 * J Zwiener	02/27/2009	Add Certified Lienholder constant;
 * 							modify setView()
 * 							modify processData()
 * 							defect 9968 Ver Defect_POS_E
 * K Harrell	06/15/2009	Use ReportConstant
 * 							add PRINT_RANGE 
 * 							modify processData()
 * 							defect 10086 Ver Defect_POS_F 
 * K Harrell	06/18/2009	Implement ReportConstants for Next Screen 
 * 							modify processData()
 * 							defect 10011 Ver Defect_POS_F
 * K Harrell	08/21/2009	CloseoutStatistics Report now uses 
 * 							FundsData to pass parameters. 
 * 							Streamline code.  
 * 							modify setView()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	10/13/2009  Implement Local Options Report Sort Options	
 * 							modify setView() 
 * 							defect 10250 Ver Defect_POS_G 
 * K Harrell	10/20/2009	Implement ReportConstant 
 * 							modify setView()
 * 							defect 10250 Ver Defect_POS_G 
 * ---------------------------------------------------------------------
 */
/**
 * @version	Defect_POS_G 	10/20/2009
 * @author	Rakesh Duggirala
 * <br>Creation Date:		07/03/2001 08:27:41
 */
public class VCPreviewReportRPR000 extends AbstractViewController
{
	public final int DEALER_REPORT = 30;
	public final int LIENHOLDER_REPORT = 40;
	public final int SUBCON_REPORT = 50;
	public final int REPRINT_REPORT = 60;
	public final int PUBLISHING_REPORT = 70;
	public final int EVENT_SECURITY_REPORT = 80;
	public final int EMPLOYEE_SECRTY_REPORT = 90;
	public final int SECRTY_CHANGE_REPORT = 100;
	public final int CLOSEOUT_REPORT = 110;
	// defect 9968
	public final int CERTFD_LIENHLDR_REPORT = 120;
	// end defect 9968

	// defect 10086 
	public final static int PRINT_RANGE = 117;
	// end defect 10086 

	/**
	 * VCPreviewReportRPR000 constructor
	 */
	public VCPreviewReportRPR000()
	{
		super();
	}

	/**
	 * getModuleName
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.REPORTS;
	}

	/**
	 * processData
	 * 
	 * @param aiCommand int
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		ReportSearchData laReportSearchData = new ReportSearchData();
		String lsNextScreen = laReportSearchData.getNextScreen();

		// Data can only be null, when keyPressed == ESC
		// defect 8628 
		// Closeout Statistics now uses FundsData 
		if (aaData != null && aaData instanceof ReportSearchData)
		{
			// end defect 8628 
			laReportSearchData = (ReportSearchData) aaData;
			lsNextScreen = laReportSearchData.getNextScreen();
		}
		switch (aiCommand)
		{
			// defect 10086 
			case PRINT_RANGE :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.RPR008);
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
					break;
				}
				// end defect 10086 
			case ENTER :
				{
					setData(aaData);
					// defect 10011
					// Use ReportConstant 
					//if (lsNextScreen.equals("NONE"))
					if (lsNextScreen
						.equals(
							ReportConstant.RPR000_NEXT_SCREEN_FINAL))
					{
						setDirectionFlow(AbstractViewController.FINAL);
					}
					//	else if (lsNextScreen.equals("VOID")
					//			|| lsNextScreen.equals("SUBCON-DRAFT"))
					else if (
						lsNextScreen.equals(
							ReportConstant
								.RPR000_NEXT_SCREEN_PREVIOUS))
					{
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						try
						{
							getMediator().processData(
								getModuleName(),
								ReportConstant.NO_DATA_TO_BUSINESS,
								laReportSearchData.getData());
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						getFrame().setVisibleRTS(false);
						break;
					}
					else if (
						lsNextScreen.equals(
							ReportConstant.RPR000_NEXT_SCREEN_CANCEL))
					{
						setDirectionFlow(AbstractViewController.CANCEL);
						try
						{
							getMediator().processData(
								getModuleName(),
								ReportConstant.NO_DATA_TO_BUSINESS,
								laReportSearchData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						getFrame().setVisibleRTS(false);
						break;
					}
					else
					{
						setDirectionFlow(AbstractViewController.NEXT);
						setNextController(lsNextScreen);
					}
					if (getFrame() != null)
					{
						getFrame().setVisibleRTS(false);
					}
					try
					{
						getMediator().processData(
							getModuleName(),
							ReportConstant.NO_DATA_TO_BUSINESS,
							laReportSearchData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case CANCEL :
				{
					//if (lsNextScreen.equals("NONE"))
					if (lsNextScreen
						.equals(
							ReportConstant.RPR000_NEXT_SCREEN_FINAL))
					{
						setDirectionFlow(AbstractViewController.FINAL);
					}
					else if (
						lsNextScreen.equals(
							ReportConstant
								.RPR000_NEXT_SCREEN_PREVIOUS))
						//lsNextScreen.equals("VOID")
						//	|| lsNextScreen.equals("SUBCON-DRAFT"))
					{
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						try
						{
							getMediator().processData(
								getModuleName(),
								ReportConstant.NO_DATA_TO_BUSINESS,
								laReportSearchData.getData());
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						getFrame().setVisibleRTS(false);
						break;
					}
					//else if (lsNextScreen.equals("RPR002"))
					else if (
						lsNextScreen.equals(
							ReportConstant.RPR000_NEXT_SCREEN_CANCEL))
					{
						setDirectionFlow(AbstractViewController.CANCEL);
						try
						{
							getMediator().processData(
								getModuleName(),
								ReportConstant.NO_DATA_TO_BUSINESS,
								laReportSearchData.getData());
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						getFrame().setVisibleRTS(false);
						break;
					}
					else
						setDirectionFlow(
							AbstractViewController.PREVIOUS);

					try
					{
						getMediator().processData(
							getModuleName(),
							ReportConstant.NO_DATA_TO_BUSINESS,
							laReportSearchData.getData());
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
			case HELP :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							ReportConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
			case DEALER_REPORT :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							LocalOptionConstant.GENERATE_DLR_RPT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case LIENHOLDER_REPORT :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							LocalOptionConstant.GENERATE_LIENHLDR_RPT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case SUBCON_REPORT :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							LocalOptionConstant.GENERATE_SUBCON_RPT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case REPRINT_REPORT :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.REPORTS,
							ReportConstant.GENERATE_REPRINT_REPORT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case PUBLISHING_REPORT :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							LocalOptionConstant.GENERATE_PUBLISHING_RPT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case EVENT_SECURITY_REPORT :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							LocalOptionConstant
								.GENERATE_EVENT_SECRTY_RPT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case EMPLOYEE_SECRTY_REPORT :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							LocalOptionConstant.GENERATE_EMP_SECRTY_RPT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case SECRTY_CHANGE_REPORT :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							LocalOptionConstant
								.GENERATE_SECRTY_CHNG_RPT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case CLOSEOUT_REPORT :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.FUNDS,
							FundsConstant
								.GENERATE_CLOSEOUT_STATISTICS_REPORT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// defect 9968
			case CERTFD_LIENHLDR_REPORT :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							LocalOptionConstant
								.GENERATE_CERTFD_LIENHLDR_RPT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// end defect 9968
		}
	}

	/**
	 * Set View checks if frame = null, set the screen and pass control
	 * to the controller.
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

		// defect 8628 
		// Restructure
		// Closeout Statistics now uses Funds Data
		// Eliminate multiple returns

		// For all Reports that have no input screen prior to report
		if (aaData == null && asTransCode != null)
		{
			if (asTransCode.equals(TransCdConstant.CLOSEOUT_STATS_RPT))
			{
				// Funds Reports use FundsData @ Server  
				FundsData laFundsData = new FundsData();
				laFundsData.setOfficeIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				laFundsData.setSubStationId(
					SystemProperty.getSubStationId());
				laFundsData.setWorkstationId(
					SystemProperty.getWorkStationId());
				laFundsData.setEmployeeId(
					SystemProperty.getCurrentEmpId());
				processData(CLOSEOUT_REPORT, laFundsData);
			}
			else
			{
				ReportSearchData laRptSearchData =
					new ReportSearchData();
				laRptSearchData.initForClient(ReportConstant.ONLINE);

				if (asTransCode.equals(TransCdConstant.PUBLISHING_RPT))
				{
					processData(PUBLISHING_REPORT, laRptSearchData);
				}
				else if (
					asTransCode.equals(TransCdConstant.REPRINT_RPT))
				{
					processData(REPRINT_REPORT, laRptSearchData);
				}
				// defect 10250 
				else if (
					asTransCode.equals(
						TransCdConstant.CERTFD_LIENHLDR_RPT_ID_SORT))
				{
					processData(
						CERTFD_LIENHLDR_REPORT,
						laRptSearchData);
				}
				else if (
					asTransCode.equals(
						TransCdConstant.CERTFD_LIENHLDR_RPT_NAME_SORT))
				{
					laRptSearchData.setIntKey4(
						ReportConstant.SORT_BY_NAME_INDI);
						
					processData(
						CERTFD_LIENHLDR_REPORT,
						laRptSearchData);
				}
				else if (
					asTransCode.equals(
						TransCdConstant.DEALER_RPT_ID_SORT))
				{
					processData(DEALER_REPORT, laRptSearchData);
				}
				else if (
					asTransCode.equals(
						TransCdConstant.DEALER_RPT_ALPHA_SORT))
				{
					laRptSearchData.setIntKey4(
						ReportConstant.SORT_BY_NAME_INDI);
						
					processData(DEALER_REPORT, laRptSearchData);
				}
				else if (
					asTransCode.equals(
						TransCdConstant.LIENHLDR_RPT_ID_SORT))
				{
					processData(LIENHOLDER_REPORT, laRptSearchData);
				}
				else if (
					asTransCode.equals(
						TransCdConstant.LIENHLDR_RPT_ALPHA_SORT))
				{
					laRptSearchData.setIntKey4(
						ReportConstant.SORT_BY_NAME_INDI);
						
					processData(LIENHOLDER_REPORT, laRptSearchData);
				}
				else if (
					asTransCode.equals(
						TransCdConstant.SUBCON_RPT_ID_SORT))
				{
					processData(SUBCON_REPORT, laRptSearchData);
				}
				else if (
					asTransCode.equals(
						TransCdConstant.SUBCON_RPT_NAME_SORT))
				{
					laRptSearchData.setIntKey4(
						ReportConstant.SORT_BY_NAME_INDI);
						
					processData(SUBCON_REPORT, laRptSearchData);
				}
				// end defect 10250 

				else if (
					asTransCode.equals(TransCdConstant.EVTSEC_RPT))
				{
					processData(EVENT_SECURITY_REPORT, laRptSearchData);
				}
				else if (
					asTransCode.equals(TransCdConstant.EMPSEC_RPT))
				{
					processData(
						EMPLOYEE_SECRTY_REPORT,
						laRptSearchData);
				}
				else if (
					asTransCode.equals(TransCdConstant.SECCHG_RPT))
				{
					processData(SECRTY_CHANGE_REPORT, laRptSearchData);
				}
			}
		}
		// Funds Reports will return ReportSearchData w/ IntKey = number
		//  of Reports w/ Data 
		else if (
			aaData instanceof ReportSearchData
				&& ((ReportSearchData) aaData).getIntKey3() == 0)
		{
			processData(ENTER, aaData);
		}
		else
		{
			if (getFrame() == null)
			{
				Dialog laRD = getMediator().getParent();
				if (laRD != null)
				{
					setFrame(new FrmPreviewReportRPR000(laRD));
				}
				else
				{
					setFrame(
						new FrmPreviewReportRPR000(
							getMediator().getDesktop()));
				}
			}
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
	}
}