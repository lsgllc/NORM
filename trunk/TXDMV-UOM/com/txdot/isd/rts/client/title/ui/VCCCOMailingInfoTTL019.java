package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *
 * VCCCOMailingInfoTTL019.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/08/2005	VAJ to WASD Clean Up
 *							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/12/2005	comment out variables not used
 * 							modify pricessData()
 * 							defect 7898 Ver 7898
 * J Rue		04/12/2005	comment out variables not used
 *							Restore code from early clean up.
 * 							modify processData(), setData()
 * 							defect 7898 Ver 7898
 * J Rue		05/05/2005	Add code to complete CCO process.
 * 							modify setView()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                  
 * J Rue		08/23/2005	Clean up RTSException parameters
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.   
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbage.
 * 							defect 7898 Ver 5.2.3  
 * J Rue		07/28/2006	Restructure process flow from CURRENT to
 * 							DIRECT_CALL. This will prevent the 
 * 							application from passing through setView() 
 * 							multiple times.
 * 							add processTransaction()
 * 							modify processData(), setView()
 * 							defect 8600 Ver 5.2.4 
 * J Rue		08/02/2006	Comment out unused variables. Correct 
 * 							spelling errors.
 * 							modify processData(), processTransaction()
 * 							defect 8600 Ver 5.2.4
 * K Harrell	04/22/2007	Use SystemProperty.isRegion()
 * 							delete getOfcIssuanceCd()
 * 							modify processData()
 * 							defect 9085 Ver Special Plates   
 * K Harrell	03/11/2009	Not used w/ Salvage; reorganize processData(); 
 * 							Cleanup.
 * 							add ADD_TRANS
 * 							modify processData()
 * 							defect 9969  Ver Defect_POS_E
 * K Harrell	08/04/2009	refactored from VCCCOCCDOMailingInfoTTL019
 * 							defect 10112 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */

/**
 * View controller for the CCO Screen.
 *
 * @version	Defect_POS_F 	08/04/2009
 * @author	Marx Rajangam
 * <br>Creation Date:		08/22/2001 10:28:48
 */

public class VCCCOMailingInfoTTL019 extends AbstractViewController
{

	/**
	 * Points to the next VC. Required for allocating inventory. 
	 */
	public final static int REDIRECT_NEXT_VC = 7;

	// defect 9969 
	public final static int ADD_TRANS = 25;
	// end defect 9969 

	/**
	 * VCCCOMailingInfoTTL019 constructor comment.
	 */

	public VCCCOMailingInfoTTL019()
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
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the 
	 * RTSMediator.
	 * 
	 * @param aiCommand int 
	 * @param aaData Object 
	 */
	public void processData(int aiCommand, Object aaData)
	{
		// defect 9969 
		switch (aiCommand)
		{
			case ADD_TRANS :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					try
					{
						getMediator().processData(
							GeneralConstant.COMMON,
							CommonConstant.ADD_TRANS,
							aaData);
						break; 
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
				}
			case ENTER :
				{
					// Prompt for Inventory 
					processTransaction(aaData);
					break;
				}
			case CANCEL :
				{
					setDirectionFlow(AbstractViewController.CANCEL);
					try
					{
						close();
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

			case REDIRECT_NEXT_VC :
				{
					try
					{
						Vector lvNextVC = (Vector) aaData;
						String lsNextVCName = (String) lvNextVC.get(0);
						CompleteTransactionData laData =
							(CompleteTransactionData) lvNextVC.get(1);

						if (lsNextVCName != null)
						{
							setNextController(lsNextVCName);
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								GeneralConstant.COMMON,
								TitleConstant.NO_DATA_TO_BUSINESS,
								laData);
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
		}
		// end defect 9969 
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
		if (getFrame() == null)
		{
			com.txdot.isd.rts.client.general.ui.RTSDialogBox laRTSDBox =
				getMediator().getParent();
			if (laRTSDBox != null)
			{
				setFrame(new FrmCCOMailingInfoTTL019(laRTSDBox));
			}
			else
			{
				setFrame(
					new FrmCCOMailingInfoTTL019(
						getMediator().getDesktop()));
			}
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
	}

	/**
	 * 
	 * Process transaction
	 * 	Set the directional flow to DIRECT_CALL 
	 * 		(Process data, return to calling method).
	 * 	Make call to get next frame.
	 * 	Continue processing.
	 *
	 * @param aaComplTransData	CompleteTransactionData
	 */
	private void processTransaction(Object aaComplTransData)
	{
		try
		{
			CompleteTransactionData laComplTransData =
				(CompleteTransactionData) aaComplTransData;

			// Set Directional Flow
			setDirectionFlow(AbstractViewController.DIRECT_CALL);

			// Get next frame
			// Mediator returns a Vector: 
			//			Element 1 - String - Next frame
			//			Element 2 - CompleteTransactionData
			Vector lvRtnObj =
				(Vector) getMediator().processData(
					GeneralConstant.COMMON,
					CommonConstant.GET_NEXT_COMPLETE_TRANS_VC,
					laComplTransData);

			// Make call to next VC
			processData(REDIRECT_NEXT_VC, lvRtnObj);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}
}
