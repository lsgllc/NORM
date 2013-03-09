package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCAllocationSummaryINV014.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/03/2005	Remove cbInit.  It is not used.
 * 							delete cbInit
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	07/19/2005	Work on Switch / case.
 * 							Work on constants.
 * 							defect 7890 ver 5.2.3
 * Ray Rowehl	08/17/2005	Add white space between methods.
 * 							defect 7890 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * ---------------------------------------------------------------------
 */

/**
 * This is the View Controller for screen INV014.
 *
 * @version	5.2.4			08/11/2006
 * @author	Charlie Walker	
 * <br>Creation Date: 		08/28/2001 18:19:45
 */

public class VCAllocationSummaryINV014 extends AbstractViewController
{
	// Not used
	///**
	// * Flag that shows if INV010 need to be initialized
	// */
	//private boolean cbInit = true;
	/**
	 * Constant to define a command for the screen to communicate with 
	 * the VC
	 */
	public static final int ALLOCATE = 10;

	public static final int GENERATE_ALLOCATION_REPORT = 20;

	/**
	 * VCAllocationSummaryINV014 constructor comment.
	 */
	public VCAllocationSummaryINV014()
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
		int liFunctnId = 0;

		switch (aiCommand)
		{
			// For the case when the user does not want to view the report
			case AbstractViewController.ENTER :
				{
					// Determine if any items have been allocated.  If 
					// yes, then create the report and return to INV009.  
					// If no, just return to INV009.
					if (((Vector) ((Vector) aaData)
						.get(CommonConstant.ELEMENT_1))
						.size()
						> CommonConstant.ELEMENT_0)
					{
						liFunctnId =
							InventoryConstant
								.PRINT_INVENTORY_ALLOCATION_REPORT;
					}
					else
					{
						liFunctnId =
							InventoryConstant.NO_DATA_TO_BUSINESS;
					}

					setData(aaData);
					setDirectionFlow(AbstractViewController.PREVIOUS);
					try
					{
						if (getFrame() != null)
						{
							// defect 8884
							// use close() so that it does setVisibleRTS()
							close();
							//getFrame().setVisibleRTS(false);
							// end 8884
						}

						getMediator().processData(
							getModuleName(),
							liFunctnId,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					if (((FrmAllocationSummaryINV014) getFrame())
						.isCancelEnabled())
					{
						setData(aaData);
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						try
						{
							if (getFrame() != null)
							{
								// defect 8884
								// use close() so that it does setVisibleRTS()
								close();
								//getFrame().setVisible(false);
								// end 8884
							}
							getMediator().processData(
								getModuleName(),
								InventoryConstant.NO_DATA_TO_BUSINESS,
								aaData);
						}
						catch (RTSException leRTSEx)
						{
							leRTSEx.displayError(getFrame());
						}
					}
					break;
				}
			case VCAllocationSummaryINV014.ALLOCATE :
				{
					setNextController(ScreenConstant.INV013);
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							InventoryConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCAllocationSummaryINV014.GENERATE_ALLOCATION_REPORT :
				{
					// For the case when the user does want to view the 
					// report
					// Determine if any items have been allocated.  
					// If yes, then create the report and preview it.  
					// If no, display error 596 and return to INV009.
					if (((Vector) ((Vector) aaData)
						.get(CommonConstant.ELEMENT_1))
						.size()
						> CommonConstant.ELEMENT_0)
					{
						setNextController(ScreenConstant.RPR000);
						setData(aaData);
						setDirectionFlow(AbstractViewController.NEXT);
						liFunctnId =
							InventoryConstant
								.GENERATE_INVENTORY_ALLOCATION_REPORT;
					}
					else
					{
						RTSException leRTSEx =
							new RTSException(
								ErrorsConstant
									.ERR_NUM_NO_TRANS_NO_REPORT);
						leRTSEx.displayError(getFrame());

						setData(aaData);
						setDirectionFlow(
							AbstractViewController.PREVIOUS);
						liFunctnId =
							InventoryConstant.NO_DATA_TO_BUSINESS;
					}

					try
					{
						if (getFrame() != null
							&& ((Vector) ((Vector) aaData)
								.get(CommonConstant.ELEMENT_1))
								.size()
								== CommonConstant.ELEMENT_0)
						{
							// defect 8884
							// use close() so that it does setVisibleRTS()
							close();
							//getFrame().setVisible(false);
							// end 8884
						}

						getMediator().processData(
							getModuleName(),
							liFunctnId,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
		}
	}

	/**
	 * Sets which frame to call.
	 *
	 * @param aaPreviousControllers Vector
	 * @param asTransCode String  
	 * @param aaData Object  
	 */
	public void setView(
		Vector aaPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmAllocationSummaryINV014(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmAllocationSummaryINV014(
						getMediator().getDesktop()));
			}
		}
		super.setView(aaPreviousControllers, asTransCode, aaData);
	}
}
