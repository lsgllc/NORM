package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.data.InventoryProfileData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCInventoryProfileINV016.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C. Walker	03/26/2002	Added comments and code to make frames
 *							modal to each other
 * Min Wang     07/16/2004	Fix no profile showing when sets
 *							profile in central.
 *							modify setWiew()
 *							defect 7334 Ver 5.2.0
 * Ray Rowehl	02/26/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/03/2005	Remove laDate from setView().  It is not
 * 							used.
 * 							modify setView()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/18/2005	More constants work.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3	
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV016 screen.  It handles screen
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.3			08/17/2005
 * @author	Sai Machavarapu
 * <br>Creation Date:		10/04/2001 10:47:55
 */

public class VCInventoryProfileINV016 extends AbstractViewController
{
	/**
	 * Flag that shows if the combo box data has been retrieved from 
	 * the db
	 */
	private boolean cbRcvdComboData = false;
	
	/**
	 * Flag that shows if INV009 need to be initialized
	 */
	private boolean cbInit = true;
	
	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int VIEW = 20;

	public static int ciFuncId = 0;
	
	/**
	 * VCInventoryProfileINV016 constructor comment.
	 */
	public VCInventoryProfileINV016()
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
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case InventoryConstant.ADD_INVENTORY_PROFILE :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.ADD_INVENTORY_PROFILE,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
				}
				break;

			case InventoryConstant.REVISE_INVENTORY_PROFILE :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.REVISE_INVENTORY_PROFILE,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
				}
				break;

			case InventoryConstant.DELETE_INVENTORY_PROFILE :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.DELETE_INVENTORY_PROFILE,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
				}
				break;

			case VIEW :
				{
					setData(aaData);
					setNextController(ScreenConstant.INV030);
					setDirectionFlow(AbstractViewController.NEXT);
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
				}
				break;

			case AbstractViewController.CANCEL :
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

			case InventoryConstant
				.RETRIEVE_INVENTORY_PROFILE_DISPLAY_DATA :
				setDirectionFlow(AbstractViewController.CURRENT);
				try
				{
					getMediator().processData(
						getModuleName(),
						InventoryConstant
							.RETRIEVE_INVENTORY_PROFILE_DISPLAY_DATA,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				return;

			case InventoryConstant.GET_INVENTORY_PROFILE :
				setDirectionFlow(AbstractViewController.CURRENT);
				ciFuncId = InventoryConstant.GET_INVENTORY_PROFILE;
				try
				{
					getMediator().processData(
						getModuleName(),
						InventoryConstant.GET_INVENTORY_PROFILE,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(getFrame());
				}
				return;
		}
	}
	
	/**
	 * Directs traffic to the frame.
	 * Creates the frame if it has not been already.
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
				setFrame(new FrmInventoryProfileINV016(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmInventoryProfileINV016(
						getMediator().getDesktop()));
			}
		}

		Vector lvSetUpData = new Vector();
		Vector lvSetUpData2 = new Vector();
		if (!cbRcvdComboData)
		{
			GeneralSearchData laGSData = new GeneralSearchData();
			laGSData.setIntKey1(SystemProperty.getOfficeIssuanceNo());
			laGSData.setIntKey2(SystemProperty.getSubStationId());

			InventoryProfileData laIPData = new InventoryProfileData();
			laIPData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laIPData.setSubstaId(SystemProperty.getSubStationId());
			laIPData.setEntity(InventoryConstant.CHAR_C);
			laIPData.setId(CommonConstant.STR_ZERO);

			laIPData.setItmCd(InventoryConstant.DEFAULT_ABBR);
			laIPData.setInvItmYr(0);

			lvSetUpData2.addElement(laGSData);
			lvSetUpData2.addElement(laIPData);

			cbRcvdComboData = true;
			ciFuncId = 0;

			processData(
				InventoryConstant
					.RETRIEVE_INVENTORY_PROFILE_DISPLAY_DATA,
				lvSetUpData2);
			return;
		}
		else
		{
			if (cbInit)
			{
				InventoryAllocationUIData laInvAlloctnUIData =
					new InventoryAllocationUIData();
				laInvAlloctnUIData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				laInvAlloctnUIData.setSubstaId(
					SystemProperty.getSubStationId());

				Vector lvTemp = (Vector) aaData;
				if (lvTemp.get(CommonConstant.ELEMENT_0) == null)
				{
					lvSetUpData.addElement(null);
				}
				else
				{
					lvSetUpData.addElement(
						lvTemp.get(CommonConstant.ELEMENT_0));
				}
				lvSetUpData.addElement(
					lvTemp.get(CommonConstant.ELEMENT_1));
				lvSetUpData.addElement(laInvAlloctnUIData);

				cbInit = false;
				ciFuncId = 0;
				super.setView(
					avPreviousControllers,
					asTransCode,
					lvSetUpData);
				return;
			}
			else if (
				ciFuncId == InventoryConstant.GET_INVENTORY_PROFILE)
			{
				getFrame().setData(aaData);
				ciFuncId = 0;
				return;
			}
		}
	}
}
