package com.txdot.isd.rts.client.accounting.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.AccountingConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCCustomerNameINQ007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	09/07/2001  Added comments 
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	07/26/2005	Java 1.4 Work 
 * 							defect 7899 Ver 5.2.3
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()  
 * 							defect 8884 Ver 5.2.4
 * K Harrell	05/19/2007	Handle "Cancel" when prior screen INQ005
 * 							modify processData()
 * 							defect 9085 Special Plates 
 * R Pilon		06/14/2011	Implement Special Plate Inquiry
 * 							modify processData()
 * 							defect 10820 Ver 6.8.0
 * --------------------------------------------------------------------- 
 */
/**
 * The View Controller for the INQ007 screen.  It handles screen 
 * navigation and controls the visibility of its frame.
 *
 * @version	Special Plates	05/19/2007 
 * @author	Michael Abernethy
 * <br>Creation Date:		08/29/2001 10:19:11 
 */
public class VCCustomerNameINQ007 extends AbstractViewController
{
	/**
	 * Creates a VCCustomerNameINQ007.
	 */
	public VCCustomerNameINQ007()
	{
		super();
	}
	/**
	 * Returns the Module name constant used by the RTSMediator to pass
	 * the data to the appropriate Business Layer class.
	 */
	public int getModuleName()
	{
		return GeneralConstant.ACCOUNTING;
	}
	/**
	 * Controls the screen flow from INQ007.  It passes the data to 
	 * the RTSMediator.
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
					setNextController(ScreenConstant.PMT004);
					setDirectionFlow(AbstractViewController.NEXT);

					if (aaData instanceof CompleteTransactionData)
					{
						setNextController(ScreenConstant.PMT004);
					}
					else
					{
						setNextController(ScreenConstant.ACC006);
					}
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.NO_DATA_TO_BUSINESS,
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
					// defect 9085 
					// Interpret as Cancel on REG003 && INQ005 
					String lsPrevController =
						(String) getPreviousControllers().get(
							getPreviousControllers().size() - 1);

					if (lsPrevController.equals(ScreenConstant.REG003)
						|| lsPrevController.equals(ScreenConstant.INQ005)
						// defect 10820
						|| lsPrevController.equals(ScreenConstant.SPL002))
						// end defect 10820
					{
						setDirectionFlow(AbstractViewController.CANCEL);
					}
					else
					{
						setDirectionFlow(AbstractViewController.FINAL);
					}
					// end defect 9085 
					
					// defect 8884
					// use close() so that it does setVisibleRTS()
					close();
					//getFrame().setVisible(false);
					// end 8884
					try
					{
						getMediator().processData(
							getModuleName(),
							AccountingConstant.NO_DATA_TO_BUSINESS,
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
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector 
	 * @param asTransCode String 
	 * @param aaData Object 
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			java.awt.Dialog laDialog = getMediator().getParent();
			if (laDialog == null)
			{
				setFrame(
					new FrmCustomerNameINQ007(
						getMediator().getDesktop()));
			}
			else
			{
				setFrame(new FrmCustomerNameINQ007(laDialog));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
