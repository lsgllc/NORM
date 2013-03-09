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
 * VCLienHolderInformationOPT002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		02/25/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 *							defect 7891  Ver 5.2.3
 * Min Wang		04/16/2005	remove unused method
 * 							delete handleError()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		09/08/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Jeff S.		06/23/2006	Used screen constant for CTL001 Title.
 * 							remove TXT_CONFM_MSG
 * 							modify processData()
 * 							defect 8756 Ver 5.2.3  
 * B Hargrove	08/11/2006	Focus lost issue. 
 * 							Use close() so that it does setVisibleRTS().
 *							modify processData()
 * 							defect 8884 Ver 5.2.4
 * --------------------------------------------------------------------- 
 */

/**
 * Controller for screen OPT002. 
 * 
 * @version	5.2.4		08/11/2006
 * @author 	Ashish Mahajan
 * <br>Creation Date:	09/5/2001 13:30:59	  
 */

public class VCLienHolderInformationOPT002
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public static final int ADD = 5;
	public static final int REVISE = 6;
	public static final int DELETE = 7;
	public static final int VTR_AUTH = 8;

	private static final String TXT_EXIT_MSG = "Exit to Main Menu?";
	// defect 8756
	// Used common constant for CTL001 title
	//private static final String TXT_CONFM_MSG =
	//	"CONFIRM ACTION    CTL001";
	// end defect 8756
	
	/**
	 * VCLienHolderInformationOPT002 constructor comment.
	 */
	public VCLienHolderInformationOPT002()
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
	//	/**
	//	 * Handles any errors that may occur
	//	 * 
	//	 * @param aeRTSEx  RTSException
	//	 */
	//	public void handleError(RTSException aeRTSEx)
	//	{
	//		// Do we use this?
	//	}
	// end defect 7891
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to 
	 * the RTSMediator
	 * 
	 * @param aiCommand int
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		try
		{
			switch (aiCommand)
			{
				case SEARCH :
					{
						setData(aaData);
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.GET_LIENHLDR_DATA_ONID,
							aaData);
						break;
					}
				case REVISE :
					{
						setData(aaData);
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.REVISE_LIENHLDR,
							aaData);
						break;
					}
				case ADD :
					{
						setData(aaData);
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.ADD_LIENHLDR,
							aaData);
						break;
					}
				case DELETE :
					{
						setData(aaData);
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							LocalOptionConstant.DEL_LIENHLDR,
							aaData);
						break;
					}
				case CANCEL :
					{
						boolean lbCancel = false;
						if (
							(
								(FrmLienHolderInformationOPT002) getFrame())
							.getLienData()
							== null)
						{
							lbCancel = true;
						}
						else
						{
							// defect 8756
							// Used common constant for CTL001 title
							RTSException leRTSXe =
								new RTSException(
									RTSException.CTL001,
									TXT_EXIT_MSG,
									ScreenConstant.CTL001_FRM_TITLE);
							// end defect 8756
							int liRetCode =
								leRTSXe.displayError(getFrame());
							if (liRetCode == RTSException.YES)
							{
								lbCancel = true;
							}
							else
							{
								(
									(FrmLienHolderInformationOPT002) getFrame())
									.refreshScreen();
							}
						}
						if (lbCancel)
						{
							setDirectionFlow(
								AbstractViewController.FINAL);
							getMediator().processData(
								getModuleName(),
								GeneralConstant.NO_DATA_TO_BUSINESS,
								getData());
							// defect 8884
							// use close() so that it does setVisibleRTS()
							close();
							//getFrame().setVisible(false);
							// end 8884
						}
						break;
					}
				case VTR_AUTH :
					{
						setDirectionFlow(AbstractViewController.NEXT);
						setNextController(ScreenConstant.CTL010);
						getMediator().processData(
							getModuleName(),
							GeneralConstant.NO_DATA_TO_BUSINESS,
							aaData);
						break;
					}
			}
		}
		catch (RTSException leRTSEx)
		{
			leRTSEx.displayError(getFrame());
		}
		catch (Exception leEx)
		{
			//TODO Do we use this?
		}
	}

	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
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
				setFrame(new FrmLienHolderInformationOPT002(laDialog));
			}
			else
			{
				setFrame(
					new FrmLienHolderInformationOPT002(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
