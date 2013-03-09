package com.txdot.isd.rts.client.reports.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * VCReprintStickerRPR004.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/26/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							New class. Ver 5.2.0
 * S Johnston	03/08/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify processData()
 *							defect 7896 Ver 5.2.3	  
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3 
 * S Johnston	05/31/2005	added displayError for empty catch
 * 							modify processData()
 * 							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * VC RPR004 is used for reprinting stickers
 * 
 * @version	5.2.3			05/31/2005 
 * @author	Michael Abernethy
 * <br>Creation Date:		08/26/2002 
 */
public class VCReprintStickerRPR004 extends AbstractViewController
{
	/**
	 * VCReprintStickerRPR004 constructor
	 */
	public VCReprintStickerRPR004()
	{
		super();
	}

	/**
	 * All subclasses must override this method to return their own
	 * module name.
	 * 
	 * @return String
	 */
	public int getModuleName()
	{
		return GeneralConstant.REPORTS;
	}

	/**
	 * All subclasses must override this method to handle data coming
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the
	 * RTSMediator.
	 * 
	 * @param aiCommand int the command so the Frame can communicate
	 * 	with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					this.setData(aaData);
					setNextController(ScreenConstant.RPR000);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							ReportConstant.REPRINT_STICKER_REPORT,
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
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						getMediator().processData(
							getModuleName(),
							FundsConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					// defect 7590
					// changed setVisible to setVisibleRTS
					getFrame().setVisibleRTS(false);
					// end defect 7590
					break;
				}
		}
	}

	/**
	 * Set View checks if frame = null, set the screen and pass control
	 * to the controller.
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
			Dialog laRD = getMediator().getParent();
			if (laRD != null)
			{
				setFrame(new FrmReprintStickerRPR004(laRD));
			}
			else
			{
				setFrame(
					new FrmReprintStickerRPR004(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
