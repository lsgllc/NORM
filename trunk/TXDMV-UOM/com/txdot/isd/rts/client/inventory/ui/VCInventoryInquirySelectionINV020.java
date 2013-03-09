package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.InventoryInquiryUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCInventoryInquirySelectionINV020.java
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
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3 
 * Ray Rowehl	07/18/2005	More constants work
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3   
 * K Harrell	05/07/2007	use SystemProperty.getOfficeIssuanceCd() 
 *     	   	 				modify processData()
 *     	   	 				defect 9085 Ver Special Plates    	   	  
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV020 screen.  It handles screen
 * navigation and controls the visibility of it's frame.
 * 
 * @version	Special Plates	05/07/2007
 * @author	Charlie Walker
 * <br>Creation Date:		11/20/2001 11:19:45
 */

public class VCInventoryInquirySelectionINV020
	extends AbstractViewController
{
	/**
	 * VCAllocateFromLocationINV020 constructor comment.
	 */
	public VCInventoryInquirySelectionINV020()
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
					int liFuncId = 0;
					if (!((InventoryInquiryUIData) aaData)
						.getInvInqBy()
						.equals(InventoryConstant.SPECIFIC_ITM))
					{
						setNextController(ScreenConstant.INV021);
						liFuncId =
							InventoryConstant.NO_DATA_TO_BUSINESS;
					}
					else
					{
						Vector lvDataIn =
							new Vector(CommonConstant.ELEMENT_3);
						lvDataIn.addElement(aaData);
						lvDataIn.addElement(new Vector());

						// Store the OfcIssuanceCd. Used in determining 
						// which item codes to display on INV023.
						// defect 9085 
						// Use SystemProperty.getOfficeIssuanceCd() 
						// OfficeIdsData laOfcIds =
						//		OfficeIdsCache.getOfcId(
						//		SystemProperty.getOfficeIssuanceNo());
						GeneralSearchData laGSD =
							new GeneralSearchData();
						//laGSD.setIntKey1(laOfcIds.getOfcIssuanceCd());
						laGSD.setIntKey1(SystemProperty.getOfficeIssuanceCd());
						// end defect 9085 
						lvDataIn.addElement(laGSD);

						aaData = lvDataIn;
						setNextController(ScreenConstant.INV023);
						liFuncId =
							InventoryConstant
								.RETRIEVE_INVENTORY_ITEMS_FOR_INQUIRY;
					}
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							liFuncId,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.DESKTOP);
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
	 * Directs traffix to the frame.
	 * Creates frame if it has not already been created.
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
		InventoryInquiryUIData laInvInqUIData =
			new InventoryInquiryUIData();
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(
					new FrmInventoryInquirySelectionINV020(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmInventoryInquirySelectionINV020(
						getMediator().getDesktop()));
			}
		}
		super.setView(
			avPreviousControllers,
			asTransCode,
			laInvInqUIData);
	}
}
