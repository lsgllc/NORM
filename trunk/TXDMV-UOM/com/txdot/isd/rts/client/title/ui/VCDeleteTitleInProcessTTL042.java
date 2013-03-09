package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import javax.swing.JFrame;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * 
 * VCDeleteTitleInProcessTTL042.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell    04/23/2003	Confirmation screen had no title
 *                          modify processData()
 * 							defect 5705 
 * J Rue		05/08/2003	Set exception to display frame. 
 *							method processData()
 *							defect 6091 
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * K Harrell	05/03/2005	Remove reference to RegistrationMiscData
 * 							modify processData()
 * 							defect 8188 Ver 5.2.3 
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3
 * Jeff S.		06/22/2006	Used screen constant for CTL001 Title.
 * 							remove CONFIRM_ACTION
 * 							modify processData()
 * 							defect 8756 Ver 5.2.3
 * B Hargrove	04/01/2008	Change all occurrences of 'progress' to   
 * 							'process' (ie: javadoc for constructor.
 * 							VCDeleteTitleinProgressTTL042 
 * 							defect 8786 Ver Defect POS A
 * K Harrell	07/02/2009	Implement new OwnerData
 * 							modify processData()
 * 							defect 10112 Ver Defect_POS_F 
 * K HaRRELL	10/04/2010	modify processData(), setView() 
 * 							defect 10598 Ver 6.6.0  
 * ---------------------------------------------------------------------
 **/

/**
 * View controller for the DELTIP screen 
 *
 * @version	6.6.0  			10/04/2010
 * @author	Administrator
 * <br>Creation Date:		08/22/2001 11:12:42
 */

public class VCDeleteTitleInProcessTTL042
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	// Constants
	private final static String UNABLE_TIP =
		"Unable to cast to TIP data";

	private final static String DEL_TTL_IN_PROCESS =
		"Delete Title In Process";

	/**
	 * VCDeleteTitleinProcessTTL042 constructor comment.
	 */

	public VCDeleteTitleInProcessTTL042()
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
		return GeneralConstant.TITLE;
	}

	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int 
	 * 		the command so the Frame can communicate with the VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		//this process data processes data from the dialog box
		//set the transCode
		setTransCode(TransCdConstant.DELTIP);

		switch (aiCommand)
		{
			case ENTER :
				{
					//TitleInProcessData to VehInqData
					try
					{
						VehicleInquiryData laVehInqData =
							new VehicleInquiryData();
						MFVehicleData laMFVehicleData =
							new MFVehicleData();
						VehicleData laVehicleData = new VehicleData();
						RegistrationData laRegistrationData =
							new RegistrationData();
						TitleData laTitleData = new TitleData();
						SalvageData laSalvageData = new SalvageData();
						OwnerData laOwnerData = new OwnerData();
						laMFVehicleData.setTitleData(laTitleData);
						laMFVehicleData.setOwnerData(laOwnerData);
						laMFVehicleData.setVehicleData(laVehicleData);
						laMFVehicleData.setRegData(laRegistrationData);
						laMFVehicleData.setVctSalvage(new Vector());
						laMFVehicleData.getVctSalvage().add(
							laSalvageData);

						laVehInqData.setMfVehicleData(laMFVehicleData);
						TitleInProcessData laTIPData =
							(TitleInProcessData) aaData;

						//put all data in Veh Inq Data
						laVehInqData
							.getMfVehicleData()
							.getTitleData()
							.setDocNo(
							laTIPData.getDocNo());
						laVehInqData
							.getMfVehicleData()
							.getVehicleData()
							.setVehMk(
							laTIPData.getVehMk());
						laVehInqData
							.getMfVehicleData()
							.getVehicleData()
							.setVin(
							laTIPData.getVIN());
						laVehInqData
							.getMfVehicleData()
							.getOwnerData()
							.setName1(
							laTIPData.getOwnrTtlName1());
						laVehInqData
							.getMfVehicleData()
							.getTitleData()
							.setOwnrShpEvidCd(
							laTIPData.getOwnrshpEvidCd());
						laVehInqData
							.getMfVehicleData()
							.getVehicleData()
							.setDpsStlnIndi(
							laTIPData.getDPSStlnIndi());

						// display CTL001
						RTSException leRTSEx =
							new RTSException(
								RTSException.CTL001,
								DEL_TTL_IN_PROCESS,
								ScreenConstant.CTL001_FRM_TITLE);

						// defect 6091
						// Display as frame
						int liRet = 0;
						if (getFrame() != null)
						{
							liRet = leRTSEx.displayError(getFrame());
						}
						else
						{
							liRet =
								leRTSEx.displayError(
									getMediator().getParent());
						}
						// end defect 6091

						leRTSEx = null;
						if (liRet == RTSException.YES)
						{
							//Complete the transaction and be done
							CompleteTitleTransaction laTtlTrans =
								new CompleteTitleTransaction(
									laVehInqData,
									getTransCode());
							CompleteTransactionData laCompTransData =
								null;
							try
							{
								laCompTransData =
									laTtlTrans.doCompleteTransaction();
								setDirectionFlow(
									AbstractViewController.PREVIOUS);
								if (getFrame() != null)
								{
									getFrame().setVisibleRTS(false);
								}
								getMediator().processData(
									GeneralConstant.COMMON,
									CommonConstant.ADD_TRANS,
									laCompTransData);
							}
							catch (RTSException aeRTSEx)
							{
								aeRTSEx.displayError(getFrame());
							}
						}
						//if no,
						else if (liRet == RTSException.NO)
						{
							return;
						}
					}
					catch (Exception aeEx)
					{
						RTSException leRTSE =
							new RTSException(UNABLE_TIP, aeEx);
						//  Should we use getFrame() for JFrame
						leRTSE.displayError((JFrame) null);
						return;
					}
					break;
				}
			case CANCEL :
				{
					// defect 10598 
					//setDirectionFlow(AbstractViewController.PREVIOUS);
					setDirectionFlow(AbstractViewController.CANCEL);
					// end defect 10598 

					try
					{
						getMediator().processData(
							getModuleName(),
							TitleConstant.NO_DATA_TO_BUSINESS,
							aaData);
						// defect 10598 
						close();
						// end defect 10598 
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
						return;
					}
					break;
				}
		}
	}

	/**
	 * Creates the form and sets the data objects and controllers
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
		// defect 10598 
		// Evaluation of TitleInProcessData 
		// now occurs in KEY007 setView()  
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDBox = getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmDeleteTitleinProcessTTL042(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmDeleteTitleinProcessTTL042(
						getMediator().getDesktop()));
			}
		}
		super.setView(avPreviousControllers, asTransCode, aaData);
		// end defect 10598 
	}
}
