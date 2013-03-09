package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * VCSalvageTTL016.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	04/29/2003	When processing enter and going back to 
 * 							KEY006, set the frame visible false.
 *							Change in processData.
 *							Defect 5933
 * J Rue		05/10/2003	Defect 6104, The frame display exceeds the 
 * 							view display.
 *							Set frame = RTSDialogBox. modify setView()
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.   
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbage.
 * 							defect 7898 Ver 5.2.3    
 * J Rue		11/09/2005 	Organize Imports
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	08/12/2006	Use close() instead of setVisible().
 * 							Also change exception displays to let
 * 							exception decide where to base the MsgDialog
 * 							from.
 * 							modify processData()
 * 							defect 8884 Ver 5.2.4
 * K Harrell	04/26/2008	Modify default TransCd to 'SCOT' as no longer
 * 							perform SLVG - plus some cleanup 
 * 							modify setView()  
 * 							defect 9636 Ver 3 Amigos PH B 
 * ---------------------------------------------------------------------
 */

/**
 * Manage the Savage TTL016 screen.
 * 
 * @version	3 Amigos PH B	04/26/2008 
 * @author	Todd Peterson
 * <br>Creation Date: 		8/22/01 11:42:33 
 */

public class VCSalvageTTL016
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public static final int VTR_AUTH = 7;
	public final static int MAILING = 20;
	public final static int LIEN_ENTRY = 21;
	private boolean cbIsInitialized;
	
	/**
	 * VCSalvageTTL016 constructor comment.
	 */
	public VCSalvageTTL016()
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
		return GeneralConstant.TITLE;
	}
	
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator. 
	 * 
	 * @param aiCand int
	 * 		 the and so the Frame can communicate with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, java.lang.Object aaData)
	{
		switch (aiCommand)
		{
			case CANCEL :
			{
				setDirectionFlow(AbstractViewController.CANCEL);
				try
				{
					setNextController(ScreenConstant.KEY006);
					close();
					getMediator().processData(
						getModuleName(),
						TitleConstant.NO_DATA_TO_BUSINESS,
						aaData);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError();
				}
				break;

			}

			case VTR_AUTH :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.CTL003);
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case MAILING :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.TTL013);
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case LIEN_ENTRY :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.TTL001);
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case ENTER :
				{
					setDirectionFlow(AbstractViewController.CANCEL);

					// set the frame visible false before returning to 
					//	mediator
					setNextController(ScreenConstant.KEY006);
					close();
					
					try
					{
						getMediator().processData(
							GeneralConstant.TITLE,
							TitleConstant.SALVAGE,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError();

					}
					break;
				}

		}

	}
	
	/**
	 * Set TransCd.
	 * 
	 * @param asTransCd java.lang.String
	 */
	public void setTransCd(String asTransCd)
	{
		setTransCode(asTransCd);
	}
	
	/**
	 * Creates the actual frame, stores the protected variables needed 
	 * by the VC, and sends the data to the frame.
	 * 
	 * @param avPreviousControllers java.util.Vector
	 * @param asTransCode java.lang.String
	 * @param aaData java.lang.Object
	 */
	public void setView(
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (cbIsInitialized)
		{
			setData(aaData);
			return;
		}
		asTransCode = TransCdConstant.SCOT;
		
		if (getFrame() == null)
		{

			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDBox =
				getMediator().getParent();
			if (laRTSDBox == null)
			{
				setFrame(new FrmSalvageTTL016(
					getMediator().getDesktop()));
			}
			else
			{
				setFrame(new FrmSalvageTTL016(laRTSDBox));
			}
		}

		cbIsInitialized = true;
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
