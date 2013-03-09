package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCFeesDuePMT004.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							modify processData()
 * 							Ver 5.2.0	
 * Ray Rowehl	03/17/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3 
 * J Rue		03/24/2005	Change DTA008A to DTA008
 * 							modify processData()
 * 							defect 6963,7898 Ver 5.2.3
 * J Rue		04/25/2005	Add if statement to check if frame is 
 * 							visible
 * 							modify processData()
 * 							defect 8138 Ver 5.2.3
 * B Hargrove	05/22/2009  Add Flashdrive option to DTA. Change 
 * 							verbiage from 'diskette'.
 * 							DTA_DISK_WRITE (refactored\renamed)
 * 							became DTA_MEDIA_WRITE.
 * 							modify processData() 
 * 							defect 10075 Ver Defect_POS_F  
 * B Hargrove	07/13/2009  Add Voluntary Veteran Fund process.
 * 							add ciPMT004PymntType,VET_FUND, 
 * 							getPMT004PymntType(), setPMT004PymntType()
 * 							modify processData() 
 * 							defect 10122 Ver Defect_POS_F
 * K Harrell	12/16/2009	Cleanup. Implement close()
 * 							modify processData()  
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	09/13/2010 	add PREVIEWRECEIPT
 * 							modify processData() 
 * 							defect 10590 Ver 6.6.0 
 * B Hargrove	08/04/2011  Add Parks and Wildlife Fund process.
 * 							add PARKS_FUND
 * 							modify processData() 
 * 							defect 10965 Ver 6.8.1
 * ---------------------------------------------------------------------
 */
/**
 * View Controller for Screen: Fees Due PMT004
 * 
 * @version	6.8.1			08/04/2011
 * @author	Nancy Ting
 * <br>Creation Date:		09/13/2001 
 */
public class VCFeesDuePMT004 extends AbstractViewController
{
	public static final int MISC_FEES = 20;
	public static final int CREDIT = 21;
	public static final int CONT_DTA = 22;
	// defect 10122
	public static final int VET_FUND = 23;
	private static int ciPMT004PymntType;
	// end defect 10122
	// defect 10590 
	public static final int PREVIEWRECEIPT = 24;
	// end defect 10590 
	// defect 10965
	public static final int PARKS_FUND = 25;
	// end defect 10965

	/**
	 * VCFeesDuePMT004 constructor.
	 */
	public VCFeesDuePMT004()
	{
		super();
	}

	/**
	 * Returns the Module name constant used by the RTSMediator to pass
	 * the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.COMMON;
	}

	/**
	 * Returns the value of payment type selected on PMT004
	 *
	 * @return  int 
	 */
	public static int getPMT004PymntType()
	{
		return ciPMT004PymntType;
	}

	/**
	 * Controls the screen flow from PMT004. It passes the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int 
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		// defect 10122
		// Set payment type code for PMT002
		setPMT004PymntType(aiCommand);
		// end defect 10122

		switch (aiCommand)
		{
			case MISC_FEES :
				setNextController(ScreenConstant.PMT002);
				setDirectionFlow(AbstractViewController.NEXT);
				try
				{
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

			case CREDIT :
				setNextController(ScreenConstant.PMT003);
				setDirectionFlow(AbstractViewController.NEXT);
				try
				{
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

				// defect 10122
			case VET_FUND :
				setNextController(ScreenConstant.PMT002);
				setDirectionFlow(AbstractViewController.NEXT);
				try
				{
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
				// end defect 10122
				
				// defect 10965
			case PARKS_FUND :
				setNextController(ScreenConstant.PMT002);
				setDirectionFlow(AbstractViewController.NEXT);
				try
				{
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
				// end defect 10965

			case CANCEL :
				setDirectionFlow(AbstractViewController.CANCEL);
				try
				{
					// defect 10290 
					close();
					// end defect 10290 
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
			case ENTER :
				{
					setDirectionFlow(AbstractViewController.DESKTOP);
					try
					{
						// defect 10290 
						close();
						// end defect 10290 

						getMediator().processData(
							getModuleName(),
							CommonConstant.ADD_TRANS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			// defect 10590
			case PREVIEWRECEIPT :
				{
					setNextController(ScreenConstant.RPR000);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
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
			// end defect 10590 
			case CONT_DTA :
				setDirectionFlow(AbstractViewController.PREVIOUS);
				setPreviousController(ScreenConstant.DTA008);

				try
				{
					// defect 10290 
					close();
					// end defect 10290 

					getMediator().processData(
						getModuleName(),
						CommonConstant.DTA_ADD_TRANS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				break;
			default :
				{
					// empty code block
				}
		}
	}

	/**
	 * This method sets the value of payment type selected on PMT004
	 *
	 * @param aiPMT004PymntType   int 
	 */
	private void setPMT004PymntType(int aiPMT004PymntType)
	{
		ciPMT004PymntType = aiPMT004PymntType;
	}

	/**
	 * Creates the actual frame, stores the protected variables needed
	 * by the VC, and sends the data to the frame.
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
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmFeesDuePMT004(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmFeesDuePMT004(getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
