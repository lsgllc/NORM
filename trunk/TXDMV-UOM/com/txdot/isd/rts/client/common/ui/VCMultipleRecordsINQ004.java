package com.txdot.isd.rts.client.common.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.PermitData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * VCMultipleRecordsINQ004.java
 *
 * (c) Texas Department of Transaportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/16/2002	Fix UAT defect 3524, added test for 
 *							parent when instantiating the frame.
 * J Kwik		04/16/2002	Fix UAT defect 3525, added test for 
 *							PAWT transCode for SINGLE_REC case in 
 *							processData method.
 * J Kwik		04/26/2002	Fix UAT defect 3678, moved setVisible 
 *							prior to calling mediator when the Enter 
 *							button is selected.
 * Ray Rowehl	01/05/2004	refer to constants as static in case 
 *							statements
 * 							modify processData()
 * 							defect 6730  Ver 5.1.5 fix 2
 * J Rue		12/08/2004	Return to VIN Key Selection if CANCEL
 *							Clean up prolog, method JavaDoc
 *							modify processData()
 *							defect 7451 Ver 5.2.2
 * Ray Rowehl	03/15/2005	RTS 5.2.3 code cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * T Pederson	10/21/2005	RTS 5.2.3 cleanup.
 * 							Removed case for help from processData. 
 * 							modify processData()
 * 							defect 7885 Ver 5.2.3
 * J Rue		04/24/2007	Replace setVisibleRTS(false) with close()
 * 							modify processData()
 * 							defect 8884 Ver Special Plates
 * J Rue		08/03/2007	Return to PREVIOUS screen (DTA008)
 * 							modify processData()
 * 							defect 9211 Ver Special Plates
 * J Rue		09/05/2007	Update comments
 * 							modify processData()
 * 							defect 9211 Ver Special Plates
 * K Harrell	05/21/2008 	Use 'SCOT' vs. 'SLVG'
 * 							No longer use  COA  
 * 							modify processData()
 * 							defect 9636, 9642 Ver 3 Amigos PH B
 * K Harrell	05/24/2010	add SINGLE_PRMT_INQ, MRG005, VTR_AUTH
 * 							add handleMFDown()
 * 							delete cbAlreadyChecked  
 * 							modify processData() 
 * 							defect 10491 Ver 6.5.0   
 * K Harrell	09/22/2010	remove "SINGLE_REC" case statement 
 * 							delete csVIN, SINGLE_REC 
 * 							modify processData() 
 * 							defect 10598 Ver 6.6.0 
 * K Harrell	12/26/2010	add ADD_TRANS 
 * 							modify processData()
 * 							defect 10700 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * View controller for Multiple Records screen.
 *
 * @version 6.7.0 			12/26/2010
 * @author  Joe Peters
 * <br>Creation Date:		09/06/2001 09:38:22 
 */

public class VCMultipleRecordsINQ004
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{

	// Constant
	// defect 10491 
	public static final int SINGLE_PRMT_INQ = 21;
	public static final int MRG005 = 22;
	public static final int VTR_AUTH = 23;
	// end defect 10491

	// defect 10700 
	public static final int ADD_TRANS = 24;
	// end defect 10700 

	/**
	 * VCMultipleRecordsINQ004 constructor comment.
	 */
	public VCMultipleRecordsINQ004()
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
		return GeneralConstant.COMMON;
	}

	/**
	 * Sets error message and navigation for Mainframe Down / DB Down 
	 * scenario
	 * 
	 * @param aaData
	 * @param aeRTSEx 
	 */
	public void handleMFDown(Object aaData, RTSException aeRTSEx)
	{
		boolean lbContinue =
			getTransCode().equals(TransCdConstant.PT72);

		if (aeRTSEx.getMsgType().equals(RTSException.DB_DOWN)
			|| aeRTSEx.getMsgType().equals(RTSException.SERVER_DOWN))
		{
			int liError =
				lbContinue
					? ErrorsConstant
						.ERR_NUM_UNABLE_TO_RETRIEVE_CAN_PROCESS
					: ErrorsConstant.ERR_NUM_RECORD_RETRIEVAL_DOWN;

			aeRTSEx.setCode(liError);
			aeRTSEx.displayError(getFrame());
		}
		else
		{
			aeRTSEx.displayError(getFrame());
		}
		if (lbContinue)
		{
			PermitData laPrmtData = new PermitData();
			if (aaData != null && aaData instanceof GeneralSearchData)
			{
				GeneralSearchData laGSD = (GeneralSearchData) aaData;
				laPrmtData.setVin(laGSD.getKey2());
				laPrmtData.setMFDwnCd(1);
			}
			laPrmtData.setNoMFRecs(1);
			processData(MRG005, laPrmtData);
		}
		else
		{
			processData(AbstractViewController.CANCEL, null);
		}
	}

	/**
	 * All subclasses must override this method to handle data 
	 * coming from their JDialogBox - inside the subclasses 
	 * implementation should be calls to fireRTSEvent() to pass
	 * the data to the RTSMediator.
	 * 
	 * @param aiCommand int 
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			// defect 10700 
			case ADD_TRANS :
				{
					if (getTransCode().equals(TransCdConstant.DPSPPT))
					{

						setDirectionFlow(
							AbstractViewController.DESKTOP);

						try
						{
							if (getFrame() != null)
							{
								getFrame().setVisibleRTS(false);
							}
							getMediator().processData(
								GeneralConstant.COMMON,
								CommonConstant.ADD_TRANS,
								aaData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						break;
					}
				}
				// end defect 10700 

			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					// defect 9211
					//	For DTA, return to PREVIOUS screen (DTA008).
					//
					//	Note: DTA returns a RTSException. DTA008 will
					//	handle the process in 
					//	FrmDealerTitleTransactionDTA008.setData().
					//	All other events will receive null as their 
					//	return object.
					if (UtilityMethods.isDTA(getTransCode()))
					{
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						setPreviousController(ScreenConstant.DTA008);
					}
					// end defect 9211
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
					close();
					break;
				}
			case AbstractViewController.ENTER :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					getFrame().setVisibleRTS(false);
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
				// defect 10491 
			case VTR_AUTH :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.CTL003);
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case SINGLE_PRMT_INQ :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					try
					{
						getMediator().processData(
							GeneralConstant.MISCELLANEOUSREG,
							MiscellaneousRegConstant.PRMTINQ,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						handleMFDown(aaData, aeRTSEx);
					}
					break;
				}
			case MRG005 :
				{
					setNextController(ScreenConstant.MRG005);
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
		}
	}

	/**
	 * Send the data to the frame.
	 * 
	 * @param avPreviousControllers Vector
	 * @param asTransCode String
	 * @param aaData Object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		RTSDialogBox laRTSDB = getMediator().getParent();
		if (laRTSDB != null)
		{
			setFrame(new FrmMultipleRecordsINQ004(laRTSDB));
		}
		else
		{
			setFrame(
				new FrmMultipleRecordsINQ004(
					getMediator().getDesktop()));
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
