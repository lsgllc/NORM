package com.txdot.isd.rts.client.specialplates.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.specialplates.business.SpecialPlatesClientUtilityMethods;

import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.cache.VehicleClassSpclPltTypeDescCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.*;

/* 
 * VCSpecialPlateApplicationSPL001.java
 * 
 * (c) Texas Department of Transportation  2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/11/2007	Created
 * 							defect 9085 Ver Special Plates 
 * J Rue		02/14/2007	turn of Cancel
 * 							modify processData()
 * 							defect 9086 Ver special Plates
 * J Rue		02/23/2007	Save original VehicleInquiryData
 * 							add getOrigVehInqData(), getOrigVehInqData()
 * 							modify setView(), processData()
 * 							defect 9086 Ver Special Plates
 * J Rue		03/22/2007	Make direct call for MF call
 * 							Move setView() into code block. This will
 * 							the process flow will not go through 
 * 							setView() needlessly
 * 							modify processData(), setView()
 * 							defect 9086 Ver Special Plates
 * K Harrell	04/20/2007	working...
 * 							Handle MF Down, DB Down
 * 							add handle_MF_DB_DOWN()
 * 							RTSException if no plate types for VehClassCd
 * 							delete getOrigVehInqData(), getOrigVehInqData(),
 * 							  handleDBDown(),handleMainframeDown()
 * 							modify processData() 
 * 							defect 9085 Special Plates
 * K Harrell	05/17/2007	Add logic for comparing InvItmYr on 
 * 							Customer Supplied
 * 							add duplicateCheck()
 * 							modify setView() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/23/2007	Modify VI calls for new VI architecture
 * 							modify processData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/20/2007	Additional work on Customer Supplied
 * 							modify setView(), duplicateCheck()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/09/2007	Correct handling MF Down errors 
 * 							modify processData(), setView()
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/22/2007	Issue Inventory Flow modified to that 
 * 							of Customer Supplied
 * 							add cbCustSupplied_Issue_Inv
 * 							delete csRequestType 
 * 							modify duplicateCheck(), processData(), 
 * 							 setView()
 * 							defect 9386 Ver Special Plates 2
 * K Harrell	10/28/2007	Modify so that neither Issue Inventory nor
 * 							 Customer supply call Special Plates 
 * 							 Utility Methods after duplicateCheck() 
 * 							modify setView()
 * 							defect 9396 Ver Special Plates 2
 * K Harrell	01/07/2009 Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify setView()  
 *        					defect 9864 Ver Defect_POS_D
 * ---------------------------------------------------------------------
 */
/**
 * VC Special Plate Application SPL001
 * 
 * @version	Defect_POS_D	01/07/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		02/11/2007 15:44:00
 */
public class VCSpecialPlateApplicationSPL001
	extends AbstractViewController
{
	// Constants
	public final static int SPL002 = 20;
	public final static int GET_NEXT_VI_ITEM_NO = 21;
	public final static int GET_SP_REC = 23;
	private final static int MULT_RECS = 24;
	private final static int ONE_HUNDRED = 100;

	// Object 
	private VehicleInquiryData caOrigVehInqData;
	private PlateTypeData caPltTypeData;

	// defect 9386
	// private String csRequestType;
	// boolean 
	private boolean cbCustSupplied_IssueInv = false;
	// end defect 9386 

	/**
	 * SpecialPlateApplicationSPL001 constructor comment.
	 */
	public VCSpecialPlateApplicationSPL001()
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
		return GeneralConstant.SPECIALPLATES;
	}
	/**
	 * All subclasses must override this method to handle data coming 
	 * from their JDialogBox - inside the subclasses implementation
	 * should be calls to fireRTSEvent() to pass the data to the RTSMediator.
	 * 
	 * @param aiCommand int command so the Frame can communicate with VC
	 * @param aaData Object the data
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case ENTER :
				{
					setNextController(ScreenConstant.SPL002);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						getMediator().processData(
							getModuleName(),
							SpecialPlatesConstant.NO_DATA_TO_BUSINESS,
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
			case SPL002 :
				{
					setNextController(ScreenConstant.SPL002);
					setDirectionFlow(AbstractViewController.NEXT);

					try
					{
						getMediator().processData(
							getModuleName(),
							SpecialPlatesConstant.NO_DATA_TO_BUSINESS,
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
				// Get Next VI Item, Validate PLP	
			case InventoryConstant.INV_GET_NEXT_VI_ITEM_NO :
			case InventoryConstant.INV_VI_VALIDATE_PER_PLT :
			case InventoryConstant
				.INV_VI_UPDATE_INV_STATUS_CD_RECOVER :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.DIRECT_CALL);
						try
						{
							aaData =
								getMediator().processData(
									getModuleName(),
									aiCommand,
									aaData);
						}
						catch (RTSException aeRTSEx)
						{
							if (aeRTSEx.getCode()
								== ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB
								&& aiCommand
									== InventoryConstant
										.INV_VI_UPDATE_INV_STATUS_CD)
							{
								processData(
									InventoryConstant.INV_VI_INSERT_ROW,
									aaData);
							}
							else
							{
								throw (aeRTSEx);
							}
							break;
						}
						processData(SPL002, aaData);
					}
					catch (RTSException aeRTSEx)
					{
						if (aeRTSEx
							.getMsgType()
							.equals(RTSException.SERVER_DOWN)
							|| aeRTSEx.getMsgType().equals(
								RTSException.MF_DOWN)
							|| aeRTSEx.getMsgType().equals(
								RTSException.DB_DOWN))
						{
							handle_MF_DB_Down(aiCommand);
						}
						else
							aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case GET_SP_REC :
				{
					try
					{
						setDirectionFlow(
							AbstractViewController.CURRENT);
						Object laObj = aaData;
						// Call MF for record
						if (aaData instanceof Vector)
						{
							caOrigVehInqData =
								(VehicleInquiryData)
									((Vector) aaData).elementAt(
									1);
							// defect 9386
							String lsRequestType =
								caOrigVehInqData
									.getMfVehicleData()
									.getSpclPltRegisData()
									.getRequestType();

							cbCustSupplied_IssueInv =
								lsRequestType.equals(
									SpecialPlatesConstant
										.ISSUE_FROM_INVENTORY)
									|| lsRequestType.equals(
										SpecialPlatesConstant
											.CUSTOMER_SUPPLIED);
							// end defect 9386 
							laObj = ((Vector) aaData).elementAt(0);
						}

						getMediator().processData(
							getModuleName(),
							SpecialPlatesConstant.GET_VEH,
							laObj);

					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case MULT_RECS :
				{
					setNextController(ScreenConstant.INQ004);
					setDirectionFlow(AbstractViewController.NEXT);
					VehicleInquiryData laVehData =
						(VehicleInquiryData) aaData;
					if (laVehData
						.getPartialSpclPltsDataList()
						.firstElement()
						instanceof MFPartialSpclPltData)
					{
						if (laVehData
							.getPartialSpclPltsDataList()
							.size()
							> ONE_HUNDRED)
						{
							RTSException leRTSEx =
								new RTSException(148);
							leRTSEx.displayError(getFrame());

							break;
						}
					}
					try
					{
						setData(aaData);
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case CommonConstant.NOT_FOUND :
				{
					RTSException leRTSEx = null;
					leRTSEx = new RTSException(57);
					leRTSEx.displayError(getFrame());
					break;
				}
				// Cancel - Return to the Desktop
			case AbstractViewController.CANCEL :
				{
					setDirectionFlow(AbstractViewController.FINAL);
					close();
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							null);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			default :
				{
				}
		}
	}
	/**
	 * Sets error message and navigation for Mainframe Down / DB Down 
	 * scenario
	 * 
	 */
	public void handle_MF_DB_Down(int aiCommand)
	{
		RTSException leRTSEx = new RTSException(01);

		if (aiCommand != GET_SP_REC)
		{
			leRTSEx = new RTSException(618);
		}
		leRTSEx.displayError(getFrame());
		return;
	}
	/**
	 * confirmContinue
	 * 
	 * @return boolean 
	 */
	private boolean confirmContinue(Object aaData)
	{
		boolean lbContinue = true;
		if (aaData != null && aaData instanceof VehicleInquiryData)
		{
			VehicleInquiryData laVehInqData =
				(VehicleInquiryData) aaData;

			String lsVehClassCd =
				laVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.getVehClassCd();

			Vector lvVector =
				VehicleClassSpclPltTypeDescCache
					.getVehClassRegClassDescs(
					lsVehClassCd);

			if (lvVector == null || lvVector.size() == 0)
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.CTL001,
						"No plate types are available at this "
							+ "location for VehClassCd "
							+ lsVehClassCd
							+ ". Do you wish to continue?",
						ScreenConstant.CTL001_FRM_TITLE);
				int liResponse = leRTSEx.displayError(getFrame());
				if (liResponse == RTSException.NO)
				{
					lbContinue = false;
				}
			}
		}

		return lbContinue;
	}
	/**
	 * setView
	 * 
	 * @param Vector avPreviousControllers
	 * @param String asTransCode
	 * @param Object aaData
	 */
	public void setView(
		Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		if (getFrame() == null)
		{
			boolean lbContinue = true;
			RTSDialogBox laRTSDiagBox = getMediator().getParent();
			if (laRTSDiagBox != null)
			{
				if (confirmContinue(aaData))
				{
					setFrame(
						new FrmSpecialPlateApplicationSPL001(laRTSDiagBox));
				}
				else
				{
					processData(AbstractViewController.CANCEL, null);
					lbContinue = false;
				}
			}
			else
			{
				setFrame(
					new FrmSpecialPlateApplicationSPL001(
						getMediator().getDesktop()));
			}
			if (lbContinue)
			{
				super.setView(
					avPreviousControllers,
					asTransCode,
					aaData);
			}
		}
		//  Get record from mainframe
		else
		{
			try
			{
				// If data is null, then return from INQ004
				if (aaData == null)
				{
					return;
				}
				// If mainframe has been searched, VehicleInquiryData is 
				// returned and processing for next screen is run
				else if (aaData instanceof GeneralSearchData)
				{
					processData(GET_SP_REC, aaData);
				}
				else
				{
					VehicleInquiryData laInqData =
						(VehicleInquiryData) aaData;

					if (laInqData.getMfDown() == 1)
					{
						handle_MF_DB_Down(GET_SP_REC);
					}
					else
					{
						SpecialPlatesRegisData laSpclPltRegisData =
							caOrigVehInqData
								.getMfVehicleData()
								.getSpclPltRegisData();

						int liInvItmYr =
							laSpclPltRegisData.getInvItmYr();

						String csRegPltCd =
							laSpclPltRegisData.getRegPltCd();

						caPltTypeData =
							PlateTypeCache.getPlateType(csRegPltCd);

						// No MF Records 
						if (laInqData.getNoMFRecs() == 0)
						{
							// defect 9386
							if (SystemProperty.getProdStatus()
								!= SystemProperty.APP_PROD_STATUS)
							{
								System.out.println("No Record Found");
							}
							if (!cbCustSupplied_IssueInv)
							{
								processData(
									CommonConstant.NOT_FOUND,
									laInqData);
							}
							// end defect 9386 

							// No Rcd Found on Customer Supplied / Issue Inventory 
							// Validate / Reserve in Virtual Inventory 
							else
							{
								int liFcnId =
									InventoryConstant
										.INV_VI_UPDATE_INV_STATUS_CD_RECOVER;
								if (caPltTypeData.getUserPltNoIndi()
									== 1)
								{
									liFcnId =
										InventoryConstant
											.INV_VI_VALIDATE_PER_PLT;
								}
								processData(liFcnId, caOrigVehInqData);
							}
						}
						// MF Records Returned 
						else
						{
							// defect 9386 
							// Throw exception if:
							//   Customer Supplied || Issue From Inventory
							//      && DuplsAllowdCd == 0  
							if (cbCustSupplied_IssueInv
								&& caPltTypeData.getDuplsAllowdCd() == 0)
							{
								throw new RTSException(
									RTSException.FAILURE_MESSAGE,
									"A record currently exists with the same plate number.",
									"ERROR");
							}
							// end defect 9386 
							else
							{
								if (laInqData.getNoMFRecs() > 1)
								{
									processData(MULT_RECS, laInqData);
								}
								// Record Found
								else if (laInqData.getNoMFRecs() == 1)
								{
									// 9396
									// Reorganize so that Issue from 
									// Inventory & Customer Supplied do
									// not call Special Plates Utility
									// Methods
									//   
									// defect 9386   
									// Call to VI if Customer Supplied
									//  or Issue from Inventory
									if (SystemProperty.getProdStatus()
										!= SystemProperty.APP_PROD_STATUS)
									{
										// defect 9864 
										// Modified in refactor of 
										// RegExpMo/Yr to PltExpMo/Yr  
										System.out.println(
											"Record Found: "
												+ laInqData
													.getMfVehicleData()
													.getSpclPltRegisData()
													.getRegPltCd()
												+ " "
												+ laInqData
													.getMfVehicleData()
													.getSpclPltRegisData()
													.getPltExpYr());
										// end defect 9864 
									}
									if (cbCustSupplied_IssueInv)
									{
										duplicateCheck(
											laInqData,
											liInvItmYr);

										int liFcnId =
											InventoryConstant
												.INV_VI_UPDATE_INV_STATUS_CD_RECOVER;

										if (caPltTypeData
											.getUserPltNoIndi()
											== 1)
										{
											liFcnId =
												InventoryConstant
													.INV_VI_VALIDATE_PER_PLT;
										}
										processData(
											liFcnId,
											caOrigVehInqData);
									}
									// Plate Ownership Change or Reserved 
									else
									{
										SpecialPlatesClientUtilityMethods
											.verifyRecordApplicable(
											caOrigVehInqData,
											laInqData,
											asTransCode);

										SpecialPlatesClientUtilityMethods
											.saveSPData(
											caOrigVehInqData,
											laInqData);

										processData(
											SPL002,
											caOrigVehInqData);
									}
									// end defect 9386 /9396 
								}
							}
						}
					}
				}
			}
			catch (RTSException aeRTSException)
			{
				aeRTSException.setBeep(RTSException.BEEP);
				aeRTSException.displayError(getFrame());
			}
		}
	}
	/**
	 * Handle duplicates for Annual Plates  
	 * 
	 * @param aaInqData
	 * @param aiInvItmYr 
	 * @throws RTSException 
	 */
	private void duplicateCheck(
		VehicleInquiryData aaInqData,
		int aiInvItmYr)
		throws RTSException
	{
		// defect 9386 
		if (caPltTypeData.getDuplsAllowdCd() == 1
			&& cbCustSupplied_IssueInv)
		{
			if (aaInqData.getNoMFRecs() == 1
				&& aaInqData.getMfVehicleData() != null)
			{
				SpecialPlatesRegisData laNewSpclPltRegisData =
					aaInqData.getMfVehicleData().getSpclPltRegisData();
				if (laNewSpclPltRegisData != null)
				{
					int liRegExpYr =
						laNewSpclPltRegisData.getPltExpYr();
					if (liRegExpYr == aiInvItmYr)
					{
						throw new RTSException(
							RTSException.FAILURE_MESSAGE,
							"A "
								+ aiInvItmYr
								+ " record "
								+ " exists with the same plate number.",
							"ERROR");
					}
				}
			}
		}
		// end defect 9386 
	}
}