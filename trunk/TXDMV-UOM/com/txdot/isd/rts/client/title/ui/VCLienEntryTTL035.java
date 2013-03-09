package com.txdot.isd.rts.client.title.ui;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * 
 * VCLienEntryTTL035.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/08/2005	VAJ to WSAD Clean Up
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
 * Ray Rowehl	08/12/2006	Fix focus issue.
 * 							Also added a close after the return from
 * 							Enter.
 * 							Also remove junk code.
 * 							delete handleError()
 * 							modify processData()
 * 							defect 8884	Ver 5.2.4
 * J Rue 		08/16/2005	Comment out close()
 * 							modify processData()
 * 							defect 8898 Ver 5.2.4 
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: Lien Entry TTL035
 * 
 * @version	5.2.4			08/16/2006
 * @author: SMACHAV
 * <br>Creation Date: 		08/22/2001 11:17:31
 */

public class VCLienEntryTTL035
	extends com.txdot.isd.rts.client.general.ui.AbstractViewController
{
	public final static int LIENHLDR_DATA = 7;
	public final static int VTR_AUTH = 8;

	/**
	 * VCLienEntryTTL035 constructor comment.
	 */
	public VCLienEntryTTL035()
	{
		super();
	}

	/**
	 * All subclasses must override this method to return their own 
	 * module name
	 * 
	 * @return java.lang.String
	 */
	public int getModuleName()
	{
		return GeneralConstant.TITLE;
	}

	// defect 8884
	//	/**
	//	 * Handles any errors that may occur
	//	 * 
	//	 * @param asRTSEx com.txdot.isd.rts.client.util.exception.
	//	 * 				  RTSException
	//	 */
	//	public void handleError(
	//		com.txdot.isd.rts.services.exception.RTSException asRTSEx)
	//	{
	//	}
	// end defect 8884

	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator
	 * 
	 * @param aiCommand int
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case LIENHLDR_DATA :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						getMediator().processData(
							getModuleName(),
							TitleConstant.LIENHOLDER_DATA,
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

					setNextController(ScreenConstant.TTL008);
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

					// defect 8884
					// add a close here to clean up on the exit back 
					// Defect 8898
					//	Comment out and come back later to review 
					//	process. 
					//close();
					//	end defect 8898
					// end defect 8884

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

						// defect 8884
						// do a close instead of setVisible
						//if (getFrame() != null)
						//{
						//	getFrame().setVisible(false);
						//}
						// end defect 8884

					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}

					// defect 8884
					// do a close instead of a setVisible.
					// Also move to after the exception check.
					// Defect 8898
					//	Comment out and come back later to review 
					//	process. 
					//close();
					//	end defect 8898
					// end defect 8884

					break;

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
				setFrame(new FrmLienEntryTTL035(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmLienEntryTTL035(getMediator().getDesktop()));
			}
		}

		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
