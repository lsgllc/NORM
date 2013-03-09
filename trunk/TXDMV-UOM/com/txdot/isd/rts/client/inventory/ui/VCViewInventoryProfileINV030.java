package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.InventoryProfileData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCViewInventoryProfileINV030.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C. Walker	03/26/2002	Added comments and code to make frames
 *							modal to each other
 * Ray Rowehl	03/19/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * Jeff S.		06/28/2006	Changed the Direction Flow for cancel from
 * 							previous to final because the desktop was
 * 							not being reset.
 * 							modify processData()
 * 							defect 8837 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for the INV030 screen.  It handles screen 
 * navigation and controls the visibility of it's frame.
 *
 * @version	5.2.3		06/28/2006
 * @author	Sai Machavarapu
 * <br>Creation Date:	10/04/2001 13:19:51
 */

public class VCViewInventoryProfileINV030
	extends AbstractViewController
{
	/**
	 * Constant to define a command for the screen to communicate with
	 * the VC
	 */
	public static final int PRINT = 20;
	
	private boolean cbInit = true;

	InventoryProfileData caIPData = null;
	public String csEntity = InventoryConstant.CHAR_C;
	
	/**
	 * VCViewInventoryProfileINV030 constructor comment.
	 */
	public VCViewInventoryProfileINV030()
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
			case PRINT :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.RPR000);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant
								.GENERATE_INVENTORY_PROFILE_REPORT,
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
					// defect 8837
					// Using FINAL so mediator will reset the title
					// on the desktop
					//setDirectionFlow(AbstractViewController.PREVIOUS);
					setDirectionFlow(AbstractViewController.FINAL);
					// end defect 8837
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

			case InventoryConstant.GET_INVENTORY_PROFILE :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
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
					break;
				}
		}
	}
	
	/**
	 * Directs traffic to frame.
	 * Creates frame if not already done.
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
				setFrame(new FrmViewInventoryProfileINV030(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmViewInventoryProfileINV030(
						getMediator().getDesktop()));
			}
		}
		if (caIPData == null)
		{
			caIPData = new InventoryProfileData();
			caIPData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			caIPData.setSubstaId(SystemProperty.getSubStationId());
			if (aaData == null)
			{
				caIPData.setEntity(InventoryConstant.CHAR_C);
			}
			else
			{
				caIPData.setEntity((String) aaData);
				csEntity = (String) aaData;
			}
			caIPData.setId(null);
			caIPData.setItmCd(null);
			caIPData.setInvItmYr(Integer.MIN_VALUE);
			processData(
				InventoryConstant.GET_INVENTORY_PROFILE,
				caIPData);
			return;
		}
		else
		{
			if (cbInit)
			{
				cbInit = false;
				Vector lvDataIn = new Vector();
				lvDataIn.add(csEntity);
				lvDataIn.add(aaData);
				super.setView(
					avPreviousControllers,
					asTransCode,
					lvDataIn);
				return;
			}
			else
			{
				getFrame().setData(aaData);
			}
		}
	}
}
