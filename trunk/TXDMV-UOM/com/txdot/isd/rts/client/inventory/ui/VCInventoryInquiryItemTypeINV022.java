package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.ErrorMessagesData;
import com.txdot.isd.rts.services.data.InventoryInquiryUIData;
import com.txdot.isd.rts.services.exception.MsgDialog;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCInventoryInquiryItemTypeINV022.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/26/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/18/2005	Constants work
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * Min Wang		04/04/2007	add Msg for virtual inventory report.
 * 							add lsWarningOK
 * 							modify processData()
 * 							defect 9117 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV022 screen.  It handles screen
 * navigation and controls the visibility of its frame.
 * 
 * @version	Special Plates		04/04/2007
 * @author	Charlie Walker
 * <br>Creation Date:	12/01/2001 15:29:45
 */

public class VCInventoryInquiryItemTypeINV022
	extends AbstractViewController
{
	// defect 9117
	String lsWarningOK = "warning_OK";
	// end defect 9117
	/**
	 * VCInventoryInquiryItemTypeINV022 constructor comment.
	 */
	public VCInventoryInquiryItemTypeINV022()
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
		return GeneralConstant.INVENTORY;
	}

	/**
	 * All subclasses must override this method to handle data coming
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
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
					setNextController(ScreenConstant.RPR000);
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.GENERATE_INVENTORY_INQUIRY_REPORT,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{

						int liOkYes = 0;
						Vector lvdata = (Vector) aaData;
						InventoryInquiryUIData laInvUIdata =
							(InventoryInquiryUIData) lvdata.get(0);
						// defect 9117
						if (laInvUIdata.getRptType().equals(
														InventoryConstant.CUR_VIRTUAL))
						{
							MsgDialog laMsgD = new MsgDialog(getFrame(),
													true,
													lsWarningOK,
													aeRTSEx.getMsgType(),
													aeRTSEx.getMessage(),
													0,
													false);
							laMsgD.setVisibleMsg(true);
							liOkYes = laMsgD.getReturnStatus();
						}
						else
						{
							liOkYes = aeRTSEx.displayError(getFrame());
						}
						// end defect 9117
						if ((aeRTSEx.getCode()
							== ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB
							&& liOkYes == RTSException.OK)
							|| liOkYes == RTSException.NO)
						{
							setData(aaData);
							setDirectionFlow(
								AbstractViewController.FINAL);
							try
							{
								getMediator().processData(
									getModuleName(),
									InventoryConstant
										.NO_DATA_TO_BUSINESS,
									aaData);
							}
							catch (RTSException aeRTSEx2)
							{
								aeRTSEx2.displayError(getFrame());
							}
						}
						else if (liOkYes == RTSException.YES)
						{
							Vector lvDataIn = (Vector) aaData;
							InventoryInquiryUIData laIIUID =
								(InventoryInquiryUIData) lvDataIn.get(
									CommonConstant.ELEMENT_0);
							laIIUID.setExceptionReport(1);
							setData(aaData);
							try
							{
								getMediator().processData(
									getModuleName(),
									InventoryConstant
										.GENERATE_INVENTORY_INQUIRY_REPORT,
									aaData);
							}
							catch (RTSException aeRTSEx2)
							{
								aeRTSEx2.displayError(getFrame());
							}
						}
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.PREVIOUS);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}
		}
	}

	/**
	 * Directs traffic to frame.
	 * If frame has not been created, create it
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
				setFrame(
					new FrmInventoryInquiryItemTypeINV022(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmInventoryInquiryItemTypeINV022(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
