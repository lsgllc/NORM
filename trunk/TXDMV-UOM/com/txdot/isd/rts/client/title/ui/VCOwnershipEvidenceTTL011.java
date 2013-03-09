package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;

import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * 
 * VCOwnershipEvidenceTTL011.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell    05/08/2003  Defect 6085 HQ should show CTL001 screen, 
 * 							should not show PMT004
 *                          method processData()
 * K Harrell    05/22/2003  Defect 6159 HQ should prompt for inventory 
 * 							on Correct Title Rejection
 *                          method processData()
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
 * K Harrell	03/09/2007	Use SystemProperty.isHQ()
 * 							modify processData()
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * VC to capture the ownership evidence submitted with the title 
 * application.
 *
 * @version	Special Plates	03/09/2007
 * @author	Marx Rajangam
 * <br>Creation Date:		8/22/01 11:36:15
 */

public class VCOwnershipEvidenceTTL011 extends AbstractViewController
{
	public final static int REDIRECT_IS_NEXT_VC_REG029 = 6;
	public final static int REDIRECT_NEXT_VC = 7;
	private boolean lbShouldDisplay;
	
	/**
	 * VCOwnershipEvidenceTTL011 constructor comment.
	 */
	public VCOwnershipEvidenceTTL011()
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
	 * @param leRTSEx RTSException
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
			case ENTER :
				{
					if (getTransCode().equals(TransCdConstant.STATJK))
					{
						//Complete the transaction and be done
						CompleteTitleTransaction laTtlTrans =
							new CompleteTitleTransaction(
								(VehicleInquiryData) aaData,
								getTransCode());
						CompleteTransactionData laCompTransData = null;
						try
						{
							laCompTransData =
								laTtlTrans.doCompleteTransaction();
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						setDirectionFlow(
							AbstractViewController.DESKTOP);
						try
						{
							getMediator().processData(
								GeneralConstant.COMMON,
								CommonConstant.ADD_TRANS,
								laCompTransData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						break;
					}
					else if (
						getTransCode().equals(TransCdConstant.CORTTL)
							|| getTransCode().equals(
								TransCdConstant.REJCOR))
					{
						//Complete the transaction and be done
						CompleteTitleTransaction laTtlTrans =
							new CompleteTitleTransaction(
								(VehicleInquiryData) aaData,
								getTransCode());
						CompleteTransactionData laCompTransData = null;
						try
						{
							laCompTransData =
								laTtlTrans.doCompleteTransaction();
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						// HQ should not go the the PMT004
						// defect 9085 
						// Use SystemProperty.isHQ()
						//if (CommonValidations
						//	.isHq(SystemProperty.getOfficeIssuanceNo())
						if (SystemProperty.isHQ()
							&& laCompTransData.getInvItemCount() == 0)
						{
							setDirectionFlow(
								AbstractViewController.DESKTOP);
							try
							{
								getMediator().processData(
									GeneralConstant.COMMON,
									CommonConstant.ADD_TRANS,
									laCompTransData);
							}
							catch (RTSException aeRTSEx)
							{
								aeRTSEx.displayError(getFrame());
							}
							break;
						}
						else
						{
							setDirectionFlow(
								AbstractViewController.CURRENT);
							try
							{
								getMediator().processData(
									GeneralConstant.COMMON,
									CommonConstant.IS_NEXT_VC_REG029,
									laCompTransData);
							}
							catch (RTSException aeRTSEx)
							{
								aeRTSEx.displayError(getFrame());
							}
							break;
						}
						// end defect 9085 
					}
					else
					{
						setData(aaData);
						VehicleInquiryData laVehData =
							(VehicleInquiryData) aaData;
						//copy the data object
						TitleValidObj laValidObj =
							(TitleValidObj) laVehData
								.getValidationObject();
						DealerTitleData laDlrTtlData =
							(DealerTitleData)laValidObj.getDlrTtlData();
						VehicleInquiryData aCopyVehData =
							(VehicleInquiryData) UtilityMethods.copy(
								laVehData);
						laValidObj =
							(TitleValidObj) aCopyVehData
								.getValidationObject();
						laValidObj.setDlrTtlData(laDlrTtlData);
						setNextController(ScreenConstant.TTL012);
						lbShouldDisplay = false;
						setDirectionFlow(AbstractViewController.NEXT);
						try
						{
							getMediator()
								.processData(
									getModuleName(),
									TitleConstant.NO_DATA_TO_BUSINESS,
							/* data*/
							aCopyVehData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						break;
					}
				}
			case CANCEL :
				{
					setDirectionFlow( AbstractViewController.CANCEL);
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
			case REDIRECT_IS_NEXT_VC_REG029 :
				{
					try
					{
						Vector lvIsNextVCREG029 = (Vector) aaData;
						Boolean lbGoToREG029 =
							(Boolean) lvIsNextVCREG029.get(0);
						// first element is flag whether to go to REG029
						CompleteTransactionData laData =
							(CompleteTransactionData) 
								lvIsNextVCREG029.get(1);
						if (lbGoToREG029.equals(Boolean.TRUE))
						{
							setNextController(ScreenConstant.REG029);
							lbShouldDisplay = false;
							setDirectionFlow(
								AbstractViewController.NEXT);
							getMediator().processData(
								GeneralConstant.COMMON,
								TitleConstant.NO_DATA_TO_BUSINESS,
								laData);
						}
						else
						{
							// determine next vc if NOT reg029
							setDirectionFlow(
								AbstractViewController.CURRENT);
							getMediator().processData(
								GeneralConstant.COMMON,
								CommonConstant
									.GET_NEXT_COMPLETE_TRANS_VC,
								((Vector) aaData).elementAt(1));
						}
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
						// first element is name of next controller 
						CompleteTransactionData laData =
							(CompleteTransactionData) lvNextVC.get(1);
						if (lsNextVCName != null)
						{
							setNextController(lsNextVCName);
							setDirectionFlow(
								AbstractViewController.NEXT);
							lbShouldDisplay = false;
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
			com.txdot.isd.rts.client.general.ui.RTSDialogBox rd =
				getMediator().getParent();
			if (rd != null)
			{
				setFrame(new FrmOwnershipEvidenceTTL011(rd));
			}
			else
			{
				setFrame(new FrmOwnershipEvidenceTTL011(
						getMediator().getDesktop()));
			}
			lbShouldDisplay = true;
		}

		setData(aaData);
		setPreviousControllers(avPreviousControllers);
		setTransCode(asTransCode);

		getFrame().setController(this);
		getFrame().setData(aaData);
		if (lbShouldDisplay)
		{
			getFrame().setVisibleRTS(true);
		}

	}
}
