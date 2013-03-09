package com.txdot.isd.rts.client.miscreg.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.PermitData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCTimedPermitTypesMRG005.java
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
 * K Harrell	05/24/2010	delete RETRIEVE_DATA 
 * 							delete caOrigVehInqData, getOrigVehInqData(), 
 * 							 setOrigVehInqData()
 * 							add handleDBDown() 
 * 							modify setView(), processData() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/19/2010	new formula for Permit No calculation in 
 * 							DB Down
 * 							modify handleDBDown()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	10/13/2010	include retry on DB Error SQL0911  
 * 							modify processData()
 * 							defect 10625 Ver 6.5.0 
 * K Harrell	05/29/2011	add MRG006, modify processData()
 * 							defect 10844 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * Miscellaneous Registration Timed Permit Types (MRG005) view 
 * controller class.
 *
 * @version	6.8.0 			05/29/2011
 * @author	Joseph Kwik
 * <br>Creation Date:		06/26/2001 14:22:56
 */

public class VCTimedPermitTypesMRG005 extends AbstractViewController
{

	// defect 10844
	public final static int MRG006 = 20;
	// end defect 10844 

	/**
	 * VCTimedPermitTypesMRG005 default constructor.
	 */
	public VCTimedPermitTypesMRG005()
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
	 * Sets error message and navigation for DB Down 
	 * scenario
	 * 
	 * @param aaData 
	 */
	public void handleDBDown(Object aaData)
	{
		RTSDate laDate = new RTSDate();
		PermitData laPrmtData = (PermitData) aaData;
		int liAppend =
			((SystemProperty.getWorkStationId()
				+ SystemProperty.getSubStationId() * 100
				+ SystemProperty.getOfficeIssuanceNo() * 1000)
				* laDate.getAMDate())
				+ laDate.get24HrTime();
		String lsAppend = "" + liAppend;
		String lsItmNo =
			"9" + lsAppend.substring(lsAppend.length() - 5);
		lsItmNo = lsItmNo + "D";
		laPrmtData.setPrmtNo(lsItmNo);
		laPrmtData.setVIAllocData(null);
	}

	/**
	 * Process Data
	 * 
	 * @param aiCommand int
	 * @param Object aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		// defect 10844 
		switch (aiCommand)
		{
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
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// end defect 10844 
			case AbstractViewController.ENTER :
				{
					setNextController(ScreenConstant.MRG006);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							MiscellaneousRegConstant.INIT_TIMED_PERMIT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
				// defect 10491 
			case InventoryConstant.INV_GET_NEXT_VI_ITEM_NO :
				{
					// defect 10625 
					// Retry on DB2 -911 Error 
					for (int i = 0; i < 3; i++)
					{
						try
						{
							setDirectionFlow(
								AbstractViewController.DIRECT_CALL);

							aaData =
								getMediator().processData(
									getModuleName(),
									aiCommand,
									aaData);
							break;
						}
						catch (RTSException aeRTSEx)
						{
							if ((aeRTSEx
								.getDetailMsg()
								.indexOf("SQL0911")
								>= 0
								|| aeRTSEx.getDetailMsg().indexOf(
									"SQLCODE=-911")
									>= 0)
								&& i < 2)
							{
								try
								{
									Thread.sleep(1000);
								}
								catch (InterruptedException aeIntEx)
								{
									System.err.println(
										aeIntEx.getMessage());
								}
							}
							else
							{
								handleDBDown(aaData);

							}
						}
					}
					// end defect 10625 
					processData(ENTER, aaData);
					break;
				}
				// end defect 10491 
			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
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
					close();
					break;
				}

				// defect 10491 
				//			case RETRIEVE_DATA :
				//				{
				//					setDirectionFlow(AbstractViewController.CURRENT);
				//					try
				//					{
				//						getMediator().processData(
				//							GeneralConstant.COMMON,
				//							CommonConstant.GET_TIME_PERMIT,
				//							aaData);
				//					}
				//					catch (RTSException aeRTSEx)
				//					{
				//						aeRTSEx.displayError(getFrame());
				//					}
				//					break;
				//				}
				// end defect 10491 
		}
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
				setFrame(new FrmTimedPermitTypesMRG005(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmTimedPermitTypesMRG005(
						getMediator().getDesktop()));
			}
		}
		// defect 10491 
		//		if (aaData == null)
		//		{
		//			processData(RETRIEVE_DATA, aaData);
		//			return;
		//		}
		//		else
		//		{
		//			if (aaData instanceof TimedPermitData
		//				|| aaData instanceof String)
		//			{
		//				super.setView(
		//					avPreviousControllers,
		//					asTransCode,
		//					aaData);
		//			}
		//		}

		if (aaData != null && aaData instanceof PermitData)
		{
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		// end defect 10491 
	}
}
