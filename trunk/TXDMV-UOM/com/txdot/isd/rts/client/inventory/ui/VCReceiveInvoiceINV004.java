package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.MFInventoryAllocationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCReceiveInvoiceINV004.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		05/12/2004	Comment out frame.setCursor().
 *							The cursor will be set on the active 
 *							gui component (RTSMediator.processData()).
 *							modify processData()
 *							defect 7053 Ver 5.1.6 Fix 1
 * Ray Rowehl	02/26/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/18/2005	Clean out old code.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV004 screen. It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.3			08/17/2005
 * @author	Charlie Walker
 * <br>Creation Date:		10/09/2001 18:19:45
 */

public class VCReceiveInvoiceINV004 extends AbstractViewController
{
	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int RETRIEVE_SUBSTATION_DATA = 15;
	
	/**
	 * Flag that shows if the substation data has been retrieved from
	 * the db
	 */
	private boolean cbRcvdSubstaData = false;
	
	/**
	 * Flag that shows if INV004 has been initialized
	 */
	private boolean cbInit = false;
	
	/**
	 * VCReceiveInvoiceINV004 constructor comment.
	 */
	public VCReceiveInvoiceINV004()
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
					setNextController(ScreenConstant.INV002);
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.GET_INVENTORY_FROM_MF,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						// Pass all exceptions to the frame for handling.  
						// This allows for components to be turned red
						// and gain focus.
						(
							(FrmReceiveInvoiceINV004) getFrame())
								.procsExcptn(
							aeRTSEx);
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.FINAL);
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
			case RETRIEVE_SUBSTATION_DATA :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.RETRIEVE_SUBSTATION_DATA,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					return;
				}
		}
	}
	
	/**
	 * All subclasses must override this method to handle data coming
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
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
		Vector lvDataIn = new Vector();

		if (!cbRcvdSubstaData)
		{
			GeneralSearchData laGSData = new GeneralSearchData();
			laGSData.setIntKey1(SystemProperty.getOfficeIssuanceNo());
			laGSData.setIntKey2(SystemProperty.getSubStationId());
			cbRcvdSubstaData = true;

			processData(RETRIEVE_SUBSTATION_DATA, laGSData);
			return;
		}
		else if (!cbInit)
		{
			MFInventoryAllocationData laMFInvAlloctnData =
				new MFInventoryAllocationData();
			lvDataIn.addElement(aaData);
			lvDataIn.addElement(laMFInvAlloctnData);

			if (getFrame() == null)
			{
				RTSDialogBox laRTSDB = getMediator().getParent();
				if (laRTSDB != null)
				{
					setFrame(new FrmReceiveInvoiceINV004(laRTSDB));
				}
				else
				{
					setFrame(
						new FrmReceiveInvoiceINV004(
							getMediator().getDesktop()));
				}
			}
			cbInit = true;
			super.setView(avPreviousControllers, asTransCode, lvDataIn);
		}
		else if (cbInit)
		{
			return;
		}
	}
}
