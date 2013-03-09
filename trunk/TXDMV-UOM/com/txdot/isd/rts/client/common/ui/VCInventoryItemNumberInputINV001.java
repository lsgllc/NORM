package com.txdot.isd.rts.client.common.ui;

import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.ProcessInventoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCInventoryItemNumberInputINV001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/13/2005	JavaDoc/Formatting/Variable Name Cleanup
 *							add getShouldDisplay()
 *							Ver 5.2.3
 * T Pederson	07/29/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * The View Controller for INV001
 *
 * @version	5.2.3		07/29/2005 
 * @author	Michael Abernethy
 * <br>Creation Date:	10/25/2001 13:56:38 
 */

public class VCInventoryItemNumberInputINV001
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	// boolean 
	private boolean cbShouldDisplay;
	
	// Constants
	public final static int VALIDATION_FAILED = 22;
	public final static int ALLOCATION_FAILED = 21;
	public final static int VALIDATION_SUCCESSFUL = 23;
	public final static int DTA = 24;
	public final static int ALREADY_ISSUED = 25;
	
	private final static String CANCEL_TXT = "CANCEL";
	private final static String DATA = "DATA";
	private final static String EXCEPTION = "EXCEPTION";
	private final static String INV_DATA = "INV_DATA";
	private final static String RET_CODE = "RETURN_CODE";
	
	/**
	 * VCInventoryItemNumberInputINV001 constructor comment.
	 */
	public VCInventoryItemNumberInputINV001()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own module name.
	 *
	 * @return int 
	 */
	public int getModuleName()
	{
		return com
			.txdot
			.isd
			.rts
			.services
			.util
			.constants
			.GeneralConstant
			.COMMON;
	}
	/**
	 * Return value of cbShouldDisplay
	 * 
	 * @return boolean
	 */
	private boolean getShouldDisplay()
	{
		return cbShouldDisplay;
	}
	/**
	 * Handle Return Code
	 * 
	 * @param aaMap java.util.Map
	 */
	private void handleReturnCode(Map aaMap)
	{
		int liReturnCode =
			((Integer) aaMap.get(RET_CODE)).intValue();

		RTSDialogBox laRD = null;

		if (getFrame().isVisible())
		{
			laRD = getFrame();
		}
		else
		{
			laRD = getMediator().getParent();
		}

		switch (liReturnCode)
		{
			case ALLOCATION_FAILED :
				{
					setShouldDisplay(true);
					processData(ALLOCATION_FAILED, aaMap);
					break;
				}

			case VALIDATION_FAILED :
				{
					setShouldDisplay(true);
					RTSException leRTSEx =
						(RTSException) aaMap.get(EXCEPTION);
					if (leRTSEx.getCode() == 593)
					{
						leRTSEx.setCode(594);
					}
					leRTSEx.displayError(laRD);
					break;
				}

			case VALIDATION_SUCCESSFUL :
				{
					setShouldDisplay(false);
					processData(
						VALIDATION_SUCCESSFUL,
						aaMap.get(DATA));
					break;
				}

			case ALREADY_ISSUED :
				{
					RTSException leRTSEx =
						(RTSException) aaMap.get(EXCEPTION);
					int liYesNo = leRTSEx.displayError(laRD);
					if (liYesNo == RTSException.YES)
					{
						setShouldDisplay(false);
						processData(
							VALIDATION_SUCCESSFUL,
							aaMap.get(DATA));
					}
					else
					{
						ProcessInventoryData laProcInvData =
							(ProcessInventoryData) aaMap.get(
								INV_DATA);
						CompleteTransactionData laTransData =
							(CompleteTransactionData) aaMap.get(DATA);
						laTransData.getInvItms().remove(laProcInvData);
						laTransData.getAllocInvItms().remove(
							laProcInvData);
						setShouldDisplay(true);
					}
					break;
				}

		}
	}
	/**
	 * All subclasses must override this method to handle data coming from their
	 * JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the RTSMediator.
	 *
	 * @param aiCommand int
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.VALIDATE,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					Map lhmMap = new HashMap();
					lhmMap.put(
						DATA,
						((FrmInventoryItemNumberInputINV001) getFrame())
							.getCompleteTransactionData());
					lhmMap.put(CANCEL_TXT, new Boolean(true));
					try
					{
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							lhmMap);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case VALIDATION_SUCCESSFUL :
				{
					setDirectionFlow(AbstractViewController.PREVIOUS);
					try
					{
						close();
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case ALLOCATION_FAILED :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.INV029);
					try
					{
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case DTA :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.INV029);
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.VALIDATE,
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
	 * Set AbstractValue of cbShouldDisplay
	 * 
	 * @param abShouldDisplay boolean
	 */
	private void setShouldDisplay(boolean abShouldDisplay)
	{
		this.cbShouldDisplay = abShouldDisplay;
	}
	/**
	 * Set View
	 * 
	 * @param avPreviousControllers java.util.Vector
	 * @param asTransCode	String
	 * @param aaData 	Object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRD = getMediator().getParent();
			if (laRD != null)
			{
				setFrame(new FrmInventoryItemNumberInputINV001(laRD));
			}
			else
			{
				setFrame(
					new FrmInventoryItemNumberInputINV001(
						getMediator().getDesktop()));
			}
		}
		HashMap lhmMap = (HashMap) aaData;
		if (lhmMap != null && lhmMap.get(RET_CODE) != null)
		{
			handleReturnCode(lhmMap);
			return;
		}
		else
		{
			setShouldDisplay(true);
		}

		setData(aaData);
		setPreviousControllers(avPreviousControllers);
		setTransCode(asTransCode);

		getFrame().setController(this);
		getFrame().setData(aaData);
		if (getShouldDisplay())
		{
			getFrame().setVisibleRTS(true);
		}
	}
}
