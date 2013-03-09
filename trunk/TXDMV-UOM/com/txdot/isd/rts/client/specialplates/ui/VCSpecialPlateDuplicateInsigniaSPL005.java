package com.txdot.isd.rts.client.specialplates.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;

/* 
 * VCSpecialPlateDuplicateInsigniaSPL005.java
 * 
 * (c) Texas Department of Transportation  2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/21/2010	Created
 * 							defect 10700  Ver 6.7.0  
 * ---------------------------------------------------------------------
 */
/**
 * VC SpecialPlateDuplicateInsigniaSPL005
 * 
 * @version	6.7.0			12/21/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		12/21/2010 15:44:00
 */
public class VCSpecialPlateDuplicateInsigniaSPL005
	extends AbstractViewController
{
	// Constants
	public final static int REC_FOUND = 21;

	/**
	 * VCSpecialPlateDuplicateInsigniaSPL005 constructor comment.
	 */
	public VCSpecialPlateDuplicateInsigniaSPL005()
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
		return GeneralConstant.SPECIALPLATES;
	}
	
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the RTSMediator.
	 * 
	 * @param aiCommand int command so the Frame can communicate with VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							SpecialPlatesConstant.GET_DUPL_INSIG,
							aaData);

					}
					catch (RTSException aeRTSEx)
					{
						if (aeRTSEx
							.getMsgType()
							.equals(RTSException.SERVER_DOWN)
							|| aeRTSEx.getMsgType().equals(
								RTSException.MF_DOWN)
							|| aeRTSEx.getMsgType().equals(
								RTSException.DB_DOWN))
						{
							handle_MF_DB_Down();
						}
						else
						{
							aeRTSEx.displayError(getFrame());
						}
					}
					break;
				}
			case REC_FOUND :
				{
					setNextController(ScreenConstant.INQ004);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						setData(aaData);
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
			case CommonConstant.NOT_FOUND :
				{
					RTSException leRTSEx = null;
					leRTSEx = new RTSException(57);
					leRTSEx.displayError(getFrame());
					break;
				}
			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					close();
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							null);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			default :
				{
				}
		}
	}
	
	/**
	 * Sets error message and navigation for Mainframe Down / DB Down 
	 * scenario
	 */
	public void handle_MF_DB_Down()
	{
		RTSException leRTSEx = new RTSException(618);
		leRTSEx.displayError(getFrame());
		return;
	}

	/**
	 * setView
	 * 
	 * @param Vector avPreviousControllers
	 * @param String asTransCode
	 * @param Object aaData
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDiagBox = getMediator().getParent();
			if (laRTSDiagBox != null)
			{
				setFrame(
					new FrmSpecialPlateDuplicateInsigniaSPL005(laRTSDiagBox));
			}
			else
			{
				setFrame(
					new FrmSpecialPlateDuplicateInsigniaSPL005(
						getMediator().getDesktop()));
			}
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		// Returned Record from DB 
		else
		{
			if (aaData != null && aaData instanceof Vector)
			{
				Vector lvReturn = (Vector) aaData;
				if (lvReturn.size() == 0)
				{
					processData(CommonConstant.NOT_FOUND, aaData);
				}
				else
				{
					processData(REC_FOUND, aaData);
				}
			}
		}
	}

}