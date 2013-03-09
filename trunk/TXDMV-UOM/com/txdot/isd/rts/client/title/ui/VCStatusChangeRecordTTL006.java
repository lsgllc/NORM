package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * VCStatusChangeRecordTTL006.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
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
 * ---------------------------------------------------------------------
 */

/**
 * VC used to display the vehicle record that the status 
 * change will be performed on.
 * 
 * @version	5.2.3			11/09/2005
 * @author: Administrator
 * <br>Creation Date: 		8/22/01 3:45:07 
*/

public class VCStatusChangeRecordTTL006
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public static final int VTR_AUTH = 7;
	/**
	 * VCStatusChangeRecordTTL006 constructor comment.
	 */
	public VCStatusChangeRecordTTL006()
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
	 * Handles any errors that may occur
	 * 
	 * @param aeRTSEx RTSException
	 */
	public void handleError(
		com.txdot.isd.rts.services.exception.RTSException aeRTSEx)
	{
		// Empty block of code
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int 
	 * 	the command so the Frame can communicate with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, java.lang.Object aaData)
	{
		switch (aiCommand)
		{
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
						if (getFrame() != null)
						{
							getFrame().setVisibleRTS(false);
						}

					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
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
			case ENTER :
				{
					setData(aaData);
					VehicleInquiryData laVehData =
						(VehicleInquiryData) aaData;

					//copy the data object
					VehicleInquiryData laCopyVehData =
						(VehicleInquiryData) UtilityMethods.copy(
							laVehData);

					int liRadioSel =
						((FrmStatusChangeRecordTTL006) getFrame())
							.getSelectedRadioButton();

					switch (liRadioSel)
					{
						case (FrmStatusChangeRecordTTL006.VEH_JUNKED) :
						{
							setNextController(ScreenConstant.TTL028);
							setTransCode(TransCdConstant.STATJK);
							break;
						}
						case (FrmStatusChangeRecordTTL006.TTL_SURR) :
						{
							setNextController(ScreenConstant.TTL029);
							setTransCode(TransCdConstant.STAT);
							break;
						}
						case (FrmStatusChangeRecordTTL006.MISC_REM) :
						{
							setNextController(ScreenConstant.TTL030);
							setTransCode(TransCdConstant.STAT);
							break;
						}
						case (FrmStatusChangeRecordTTL006.STOLEN_SRS) :
						{
							setNextController(ScreenConstant.TTL037);
							setTransCode(TransCdConstant.STAT);
							break;
						}
						case (FrmStatusChangeRecordTTL006.REG_REFUND) :
						{
							setNextController(ScreenConstant.REG004);
							setTransCode(TransCdConstant.STATRF);
							break;
						}
						case (FrmStatusChangeRecordTTL006.CANCEL_REG) :
						{
							setNextController(ScreenConstant.REG005);
							setTransCode(TransCdConstant.REGIVD);
							break;
						}
						default :
						{
							setNextController(ScreenConstant.TTL028);
							setTransCode(TransCdConstant.STATJK);
							break;
						}
					}
					setDirectionFlow(AbstractViewController.NEXT);

					try
					{
						getMediator()
							.processData(
								getModuleName(),
								TitleConstant.NO_DATA_TO_BUSINESS,
						/* data*/
						laCopyVehData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case HELP :
			{
				break;
			}
		}

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
		if (getFrame() == null)
		{
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDBox =
				getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmStatusChangeRecordTTL006(laRTSDBox));
			}
			else
			{
				setFrame(new FrmStatusChangeRecordTTL006(
						getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
