package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CreditCardFeesConstants;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCMaintainCreditCardFeesOPT006.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	01/17/2005	LocalOptionConstant.RETREIVE_CREDIT_FEES
 *							replaced with constant with corrected
 *							spelling.
 *							modify directCall()
 *							defect 7902 Ver 5.2.3 
 * Min Wang		02/28/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 *							defect 7891  Ver 5.2.3
 * --------------------------------------------------------------------- 
 */
/**
 * Controller for screen OPT006. 
 * 
 * @version	5.2.3		02/28/2005
 * @author 	Michael Abernethy
 * <p>Creation Date:	04/25/2002 10:45:36	  
 */
public class VCMaintainCreditCardFeesOPT006
	extends AbstractViewController
{
	public final static int ADD = 20;
	public final static int REVISE = 21;
	public final static int RETRIEVE = 22;
	/**
	 * VCMaintainCreditCardFeesOPT006 constructor comment.
	 */
	public VCMaintainCreditCardFeesOPT006()
	{
		super();
	}
	/**
	 * Direct Call for Retrieve Credit Card Fees
	 * 
	 * @param  aiCommand int
	 * @return Object
	 * @throws RTSException 
	 */
	public Object directCall(int aiCommand) throws RTSException
	{
		switch (aiCommand)
		{
			case RETRIEVE :
				{
					Map laMap = new HashMap();
					laMap.put(
						CreditCardFeesConstants.OFC,
						new Integer(
							SystemProperty.getOfficeIssuanceNo()));
					laMap.put(
						CreditCardFeesConstants.SUB,
						new Integer(SystemProperty.getSubStationId()));
					setDirectionFlow(DIRECT_CALL);
					// defect 7902
					// replace with correctly spelled constant
					return getMediator().processData(
						GeneralConstant.LOCAL_OPTIONS,
						LocalOptionConstant.RETRIEVE_CREDIT_FEES,
						laMap);
					// end defect 7902
				}
		}
		return null;
	}
	/**
	 * All subclasses must override this method to return 
	 * their own module name.
	 * 
	 * @return int 
	 */
	public int getModuleName()
	{
		return GeneralConstant.LOCAL_OPTIONS;
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to 
	 * the RTSMediator.
	 * 
	 * @param aiCommand int
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ADD :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.OPT007);
					try
					{
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case DELETE :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.OPT007);
					try
					{
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case REVISE :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.OPT007);
					try
					{
						getMediator().processData(
							GeneralConstant.LOCAL_OPTIONS,
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					getFrame().setVisibleRTS(false);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.NO_DATA_TO_BUSINESS,
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
	 * Set View
	 * 
	 * @param avPreviousControllers Vector
	 * @param asTransCd String
	 * @param aaData  Object
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCd,
		Object aaData)
	{
		if (aaData == null)
		{
			try
			{
				aaData = directCall(RETRIEVE);
			}
			catch (RTSException leRTSEx)
			{
				leRTSEx.displayError(getMediator().getParent());
			}
		}
		if (getFrame() == null)
		{
			Dialog laRD = getMediator().getParent();
			if (laRD != null)
			{
				setFrame(new FrmMaintainCreditCardFeesOPT006(laRD));
			}
			else
			{
				setFrame(
					new FrmMaintainCreditCardFeesOPT006(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCd, aaData);
	}
}
