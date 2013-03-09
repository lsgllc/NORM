package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.PermitData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * VCTimedPermitInquiryMRG002.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/24/2010	Created.
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/07/2010	add Error Constants
 * 							modify processData(), setView()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	05/28/2011	modify setView() 
 * 							defect 10844 Ver 6.8.0
 * K Harrell	06/19/2011	add INQ009
 * 							modify setView(), processData()
 * 							defect 10844 Ver 6.8.0  
 * K Harrell	06/27/2011	Current can also modify between Motorcycle
 * 							 and Regular on One Trip Permit, 30 Day
 * 							modify setView() 
 * 							defect 10844 Ver 6.8.0   
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for FrmTimedPermitInquiryMRG002.java
 *
 * @version	6.8.0			06/27/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		05/24/2010 10:35:17
 */
public class VCTimedPermitInquiryMRG002 extends AbstractViewController
{
	private final static int INQ004 = 22;
	private final static int MRG006 = 23;
	private final static int MRG005 = 24;
	
	// defect 10844 
	private final static int INQ008 = 25;
	// end defect 10844 

	/**
	 * VCTimedPermitInquiryMRG002.java Constructor
	 * 
	 */
	public VCTimedPermitInquiryMRG002()
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
	 * Sets error message and navigation for Mainframe Down / DB Down 
	 * scenario
	 */
	public void handleMFDown(RTSException aeRTSEx)
	{
		if (aeRTSEx.getMessage().equals(RTSException.DB_DOWN)
			|| aeRTSEx.getMsgType().equals(RTSException.SERVER_DOWN))
		{
			aeRTSEx.setCode(
				ErrorsConstant.ERR_NUM_RECORD_RETRIEVAL_DOWN);
		}
		aeRTSEx.displayError(getFrame());

		processData(AbstractViewController.CANCEL, null);
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

					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						aaData =
							getMediator().processData(
								getModuleName(),
								MiscellaneousRegConstant.PRMTINQ,
								aaData);
					}
					catch (RTSException aeRTSEx)
					{
						handleMFDown(aeRTSEx);
					}
					break;
				}
				// defect 10844 
			case INQ008 :
				{
					setNextController(ScreenConstant.INQ008);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						setData(aaData);
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// end defect 10844 
			case INQ004 :
				{
					setNextController(ScreenConstant.INQ004);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						setData(aaData);
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case MRG006 :
				{
					setNextController(ScreenConstant.MRG006);
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
						handleMFDown(aeRTSEx);
					}
					break;
				}
				// defect 10844 
			case MRG005 :
				{
					setNextController(ScreenConstant.MRG005);
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
						handleMFDown(aeRTSEx);
					}
					break;
				}
				// end defect 10844 
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
					close();
					break;
				}
		}
	}

	/**
	 * This method is called by RTSMediator to display the frame.
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
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDialogBox = getMediator().getParent();
			if (laRTSDialogBox != null)
			{
				setFrame(
					new FrmTimedPermitInquiryMRG002(laRTSDialogBox));
			}
			else
			{
				setFrame(
					new FrmTimedPermitInquiryMRG002(
						getMediator().getDesktop()));

			}
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		else
		{
			if (aaData instanceof PermitData)
			{
				PermitData laPrmtData = (PermitData) aaData;

				if (laPrmtData.getNoMFRecs() == 0)
				{
					if (laPrmtData.isMFDown())
					{
						new RTSException(
							ErrorsConstant
								.ERR_NUM_RECORD_RETRIEVAL_DOWN)
								.displayError(
							getFrame());

						processData(
							AbstractViewController.CANCEL,
							null);
					}
					else
					{
						// No Record Found 
						new RTSException(
							ErrorsConstant
								.ERR_NUM_NO_RECORD_FOUND)
								.displayError(
							getFrame());
					}
				}
				else if (laPrmtData.getNoMFRecs() == 1)
				{
					// defect 10844 
					if (asTransCode.equals(TransCdConstant.MODPT))
					{
						// Cannot Process || Must Show 
						if (laPrmtData.isExpired()
							|| laPrmtData.isIssuedByBulkPrmtVendor())
						{
							processData(INQ004, laPrmtData);
						}
						else if (laPrmtData.hasPriorModTrans())
						{
							processData(INQ008, laPrmtData);
						}
						else
						{
							// If One Trip or 30 Day 
							// Can modify target Vehicle Type 
							if (laPrmtData.isOTPT()
								|| laPrmtData.is30DayPT())
							{
								processData(MRG005, laPrmtData);
							}
							else
							{
								processData(MRG006, laPrmtData);
							}
						}
					}
					else
						// end defect 10844 
						if (asTransCode.equals(TransCdConstant.PRMDUP)
							&& laPrmtData.isExpired())
						{
							processData(INQ004, laPrmtData);
						}
						else
						{
							processData(MRG006, laPrmtData);
						}
				}
				else
				{
					if (laPrmtData.isMaxRecordsExceeded())
					{
						// "Refine Query" 
						new RTSException(
							ErrorsConstant
								.ERR_NUM_MAXIMUM_NO_OF_ROWS_EXCEEDED_REFINE)
								.displayError(
							getFrame());
					}
					processData(INQ004, laPrmtData);
				}
			}

		}
	}
}