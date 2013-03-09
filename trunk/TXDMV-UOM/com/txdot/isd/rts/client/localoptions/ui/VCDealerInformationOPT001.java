package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * VCDealerInformationOPT001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		02/22/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 *							defect 7891  Ver 5.2.3
 * Min Wang 	04/16/2005	remove unused method
 * 							delete handleError()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		09/06/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Jeff S.		06/23/2006	Used screen constant for CTL001 Title.
 * 							remove TXT_CONFM_MSG
 * 							modify processData()
 * 							defect 8756 Ver 5.2.3
 * --------------------------------------------------------------------- 
 */
/**
 * Controller for screen OPT001. 
 * 
 * @version	5.2.3		06/23/2006
 * @author 	Ashish Mahajan
 * <br>Creation Date:	09/05/2001 13:30:59	  
 */
public class VCDealerInformationOPT001 extends AbstractViewController
{
	public static final int ADD = 5;
	public static final int REVISE = 6;
	public static final int DELETE = 7;

	private static final String TXT_EXIT_MSG = "Exit to Main Menu?";
	// defect 8756
	// Used common constant for CTL001 title
	//private static final String TXT_CONFM_MSG =
	//	"CONFIRM ACTION    CTL001";
	// end defect 8756
	/**
	 * VCDealerInformationOPT001 constructor comment.
	 */
	public VCDealerInformationOPT001()
	{
		super();
	}
	/**
	 * Returns the Module name constant used by the RTSMediator to pass 
	 * the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.LOCAL_OPTIONS;
	}
	// defect 7891
	///**
	// * Handles any errors that may occur
	// * 
	// * @param aeRTSEx RTSException
	// */
	//public void handleError(RTSException aeRTSEx)
	//{
	//	// Do we use this?
	//}
	// end defect 7891
	/**
	 * Method overriding the base class method 
	 * Calls the mediator process data.
	 * Catch RTSException thrown by mediator
	 * 
	 * @param aiCommand int
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case SEARCH :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.GET_DLR_DATA_ONID,
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
					// this.data = aaData;
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.REVISE_DLR,
							aaData);
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case ADD :
				{
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.ADD_DLR,
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
					setData(aaData);
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.DEL_DLR,
							aaData);
						((FrmDealerInformationOPT001) getFrame())
							.refreshScreen();
					}
					catch (RTSException leRTSEx)
					{
						leRTSEx.displayError(getFrame());
					}
					break;
				}
			case CANCEL :
				{
					boolean lbCancel = false;
					if (((FrmDealerInformationOPT001) getFrame())
						.getDlrData()
						== null)
						lbCancel = true;
					else
					{
						// defect 8756
						// Used common constant for CTL001 title
						RTSException leRTSEx =
							new RTSException(
								RTSException.CTL001,
								TXT_EXIT_MSG,
								ScreenConstant.CTL001_FRM_TITLE);
						// end defect 8756
						int liRetCode =
							leRTSEx.displayError(getFrame());
						if (liRetCode == RTSException.YES)
						{
							lbCancel = true;
						}
						else
						{
							((FrmDealerInformationOPT001) getFrame())
								.refreshScreen();
						}
					}
					if (lbCancel)
					{
						setDirectionFlow(AbstractViewController.FINAL);
						try
						{
							getMediator().processData(
								getModuleName(),
								GeneralConstant.NO_DATA_TO_BUSINESS,
								aaData);
						}
						catch (RTSException leRTSEx)
						{
							leRTSEx.displayError(getFrame());
						}
						getFrame().setVisibleRTS(false);
					}
					break;
				}
		}
	}
	/**
	 * Creates the actual frame, stores the protected variables needed
	 *  by the VC, and sends the data to the frame.
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
			Dialog laDialog = getMediator().getParent();
			if (laDialog != null)
			{
				setFrame(new FrmDealerInformationOPT001(laDialog));
			}
			else
			{
				setFrame(
					new FrmDealerInformationOPT001(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
