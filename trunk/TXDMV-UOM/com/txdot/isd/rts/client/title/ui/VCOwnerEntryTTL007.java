package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.data.SpecialPlatesRegisData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * 
 * VCOwnerEntryTTL007.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/10/2005	VAJ to WSAD Clean Up
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
 * T Pederson	02/15/2007	Added code to handle Special Plates button. 
 *							modify processData()
 * 							defect 9123 Ver Special Plates
 * K Harrell	04/17/2007	Go to SPL004 if Special Plate defined in 
 * 							MF Down
 * 							modify processData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/18/2007	Renamed SPL005 to SPL004
 * 							defect 9085 Ver Special Plates  
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: Owner Entry TTL007
 * 
 * @version	Special Plates	06/18/2007
 * @author	SMACHAV
 * <br>Creation Date: 		08/22/2001 11:35:18
 */

public class VCOwnerEntryTTL007
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public static final int VTR_AUTH = 6;
	public static final int SPECIAL_PLATES = 34;
	/**
	 * VCOwnerEntryTTL007 constructor comment.
	 */
	public VCOwnerEntryTTL007()
	{
		super();
	}
	/**
	 * All subclasses must override this method to return their own 
	 * module name
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
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator
	 * 
	 * @param aiCommand int
	 * @param aaData java.lang.Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setData(aaData);
					VehicleInquiryData laVehData =
						(VehicleInquiryData) aaData;

					//copy the data object
					TitleValidObj laValidObj =
						(TitleValidObj) laVehData.getValidationObject();
					DealerTitleData laDlrTtlData =
						(DealerTitleData) laValidObj.getDlrTtlData();

					VehicleInquiryData laCopyVehData =
						(VehicleInquiryData) UtilityMethods.copy(
							laVehData);

					laValidObj =
						(TitleValidObj) laCopyVehData
							.getValidationObject();
					laValidObj.setDlrTtlData(laDlrTtlData);

					if (((FrmOwnerEntryTTL007) getFrame()).
						isRecordLien())
					{
						setNextController(ScreenConstant.TTL035);
					}
					else
					{
						setNextController(ScreenConstant.TTL008);
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
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);

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
					getFrame().setVisibleRTS(false);
					break;

				}
			case SPECIAL_PLATES :
				{
					try
					{
						VehicleInquiryData laVehData =
												(VehicleInquiryData) aaData;
												
						SpecialPlatesRegisData laSpclPltRegisData =
							laVehData
								.getMfVehicleData()
								.getSpclPltRegisData();
								
						// defect 9085 		
						if (laSpclPltRegisData.isMFDownSP())
						{
							setNextController(
								ScreenConstant.SPL004);
						}
						else
						{
							setNextController(
								ScreenConstant.SPL002);
						}
						// end defect 9085 
						
						setDirectionFlow(
							AbstractViewController.NEXT);
						getMediator().processData(
							getModuleName(),
							TitleConstant
								.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					finally
					{
						break;
					}
				}
			case VTR_AUTH :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.CTL010);
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
			case FINAL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
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

					if (getFrame() != null)
					{
						getFrame().setVisibleRTS(false);
					}
					break;
				}
			case HELP :
				break;
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
				setFrame(new FrmOwnerEntryTTL007(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmOwnerEntryTTL007(getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
