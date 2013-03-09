package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.common.business.CommonUtil;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.title.ui.TitleValidObj;

import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.MFLogError;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * VCVinKeySelectionKEY006.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/11/2002	Added Beep
 *							defect 3444 
 * MAbs			05/02/2002	Added Wait cursor
 *							defect 3564
 * MAbs			05/14/2002	Changed wait cursor on cancel 
 * 							defect 3904
 * T Pederson	05/15/2002	Changed display of error 85 to use desktop
 * 							if frame null 
 *							defect 3910
 * MAbs			05/17/2002	Changed wait cursor after Salvage
 * MAbs/TP		06/05/2002	MultiRecs in Archive CQU100004019
 * MAbs			06/14/2002	OS/2 Flash fix 
 *							defect 4283
 * BArred/
 * SGovind		07/31/2002	Added code to correct null 
 * 							pointer exception.
 *							defect 4536
 * J Rue		10/24/2002	COA, add code to goto TTL015  
 * 							if Plate Key Selected. method processData()
 *							Refer to client.title.ui.FrmCOATTL015
 *							defect 4937 
 * Ray Rowehl	04/30/2003	Do not call the frame's reset method for 
 * 							Salvage any more.
 *							method processData
 *							defect 5933.
 * Ray Rowehl	07/16/2003	Handle Server Down just like MF Down.
 *							modify processData
 *							defect 6348
 * Min Wang		07/17/2003  Do not reset 
 * 							UserSuppliedOwnerData if it is found.
 *							modify processData().
 *							defect 6239. 
 * J Rue		12/08/2004	If annual and new expiration month/year 
 * 							is entered and no plate (annual)
 *							return error message 659.
 *							modified validateDTA008DataFromMFData()
 *							defect 6652, Ver 5.1.5.2
 * Ray Rowehl	01/05/2004	Refer to constants as static
 * 							modified processData(), assignMFDownData(), 
 * 							setView()
 * 							defect 6596  Ver 5.1.5.2
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify validateDTA0008DataFromMFData()
 *							JavaDoc Cleanup. Ver 5.2.0 	
 * Min Wang		05/12/2004	Comment out frame.setCursor().
 *							The cursor will be set on the active 
 *							gui component (RTSMediator.processData()).
 *							modify processData()
 *							defect 7053 Ver 5.1.6 Fix 1	
 * J Rue		08/25/2004	DealerTitleData.NewRegExpMo() and 
 *							DealerTitleData.NewRegExpYr() were converted 
 *							to integer.
 *							method validateDTA008DataFromMFData()
 *							defect 7496 Ver 5.2.1
 * J Rue		11/12/2004	Allow process to continue if Dealer DocNo
 *							does not match MF DocNo.
 *							method setView()
 *							defect 6804 Ver 5.2.2
 * B Hargrove	11/16/2004	Set Owner ID ONLY if Owner ID was entered
 *							and no Owner data was found for that ID.
 *							see also: client.title.ui.FRMSalvageTTL016.
 *							           doCntryStateTxtField()
 *							method processData()
 *							defect 6689 Ver 5.2.2
 * J Rue		11/17/2004	Add comment to decsribe problem 
 *							if VehicleInquiryData is not returned.
 *							setView()
 *							defect 5995 Ver 5.2.2
 * B Hargrove	11/18/2004	Fixed comments for defect 6689.
 *							method processData()
 *							defect 6689 Ver 5.2.2
 * J Rue		12/08/2004	DTA- Return to DTA008 screen if cancel from
 *							Multiple Records screen INQ004
 *							Update the Prolog
 *							modify setView()
 *							defect 7451 Ver 5.2.2
 * J Rue		12/08/2004	If DTA/No Record Found and DocNo exist
 *							set to empty
 *							Update the Prolog, JavaDoc and 
 *							set Hungarian Notation
 *							modify validateDTA008DataFromMFData()
 *							defect 7700 Ver 5.2.2
 * Ray Rowehl	02/08/2005	Change import for transaction.
 * 							Found that CommonConstant, 
 * 							CommonClientUIConstants,
 * 							SystemProperty
 * 							were not used, delete.
 * 							delete cc, cuc, cSysProp
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * Ray Rowehl	03/07/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * B Hargrove	03/21/2005	Use new getters\setters for frame, 
 *							controller, etc.
 *							defect 7885 Ver 5.2.3
 * J Rue		03/23/2005	Change DTA008a to DTA008
 * 							Cleanup code
 * 							modify handleMainframeDown(), processData()
 * 							defect 6963 Ver 5.2.3
 * J Zwiener	06/10/2005	Lienhdr3 is left null in TitleData object 
 * 							causing NPE in receipt generation 
 * 							modify createInquiryObject()
 * 							defect 6102 Ver 5.2.3
 * K Harrell	06/20/2005	Add'l Cleanup 
 * 							defect 7885 Ver 5.2.3
 * K Harrell	12/30/2005	Test getMfVehicleData().getUserSuppliedOwnerData()
 *							== null prior to test for OwnerId == null
 *							Restored "dropped" break for SLVG  
 *							modify processData() 
 *							defect 8500 Ver 5.2.2 Fix 8
 * K Harrell	02/13/2006	MF Down indicator is not set correctly  	
 * 							modify assignMFDownData(),
 * 							validateDTA008DataFromMFData()
 * 							defect 6861 Ver 5.2.3   
 * Jeff S.		06/26/2006	Used screen constant for CTL001 Title.
 * 							remove CTL001_TITLE_STR
 * 							modify processData()
 * 							defect 8756 Ver 5.2.3
 * K Harrell	02/05/2007	Use PlateTypeCache vs. 
 *							  RegistrationRenewalsCache
 *							modify validateDTA008DataFromMFData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/22/2007	Use SystemProperty.isCounty()
 * 							modify handleMainframeDown(), processData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/29/2007	Final Tune-up
 * 							defect 9085 Ver Special Plates
 * J Rue		12/14/2007	Save RegInvldIndi to DTARegInvldIndi
 * 							modify validateDTA008DataFromMFData()
 * 							defect 8329 Ver DEFECT-POS-A
 * K Harrell	05/21/2008	Use 'SCOT' vs. 'SLVG'
 * 							No longer use COA  
 * 							modify processData(), handleMainframeDown()
 * 							defect 9636 Ver 3 Amigos PH B
 * J Rue		10/22/2008	Add if statement to display message for 
 * 							cancel plate if search key by RegPltNo
 * 							modify setView()
 * 							defect 9329 Ver Defect_POS_B  
 * J Rue		10/24/2008	Replace "REGPLTNO" with CommonConstant
 * 							REGPLTNO.
 * 							modify setView()
 * 							defect 9329  Ver Defect_POS_B
 * B Hargrove	06/08/2009	Remove all 'Cancelled Sticker' references.
 * 							modify processData(), setView()
 * 							defect 9953 Ver Defect_POS_F
 * K Harrell	06/29/2009	Accommodate new handling of LienholderData,
 * 							OwnerData 
 * 							modify createInquiryObject() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/20/2009	Use UtilityMethods.isEmpty() to determine
 * 							absence of data; 
 * 							modify processData() 
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	12/16/2009	delete HEADQUARTERS_ID, GET_SAVED_VEHICLE,
 * 							  GET_OWNER_INFORMATION
 * 							refactor caGeneralSearchData to caGSD 
 * 							modify assignMFDownData(), setView(), 
 * 							 createInquiryObject()   
 * 							defect 10290 Ver Defect_POS_H 
 * Min Wang		03/19/2010	do not close frame when Multiple Records 
 * 							and SCOT.  
 *							modify processData()
 *							defect 8561 Ver POS_640
 * Min Wang		03/31/2010	Do not close the frame when returning from 
 * 							INQ004.
 * 							modify	processData()
 * 							defect 10426 Ver POS_640
 * K Harrell	09/22/2010	add INPROCS_TRANS 
 * 							modify processData(), setView() 
 * 							defect 10598 Ver 6.6.0 
 * M Reyes		09/28/2010  modify setView()
 * 							comment out defect 9329. Prevented CTL005
 * 							from displaying.
 * 							defect 10538 Ver 6.6.0
 * K Harrell	11/11/2010	Error msg 27 displaying twice if InProcess
 * 							trans and last 4 DocNo don't match 
 * 							add cbDocNoMismatchErrDisplayed
 * 							modify setView()
 * 							defect 10654 Ver 6.6.0 
 * K Harrell	05/09/2011	In Process Transactions not presented if 
 * 							record in Archive. 
 * 							modify setView() 
 * 							defect 10786 Ver 6.7.1 
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: Vin Key Selection KEY006
 *
 * @version	6.7.1			05/09/2011
 * @author	Joe Peters
 * <br>Creation Date:		09/11/2001 21:35:39 
 */

public class VCVinKeySelectionKEY006 extends AbstractViewController
{
	// defect 10598 
	public static final int INPROCS_TRANS = 27;
	// end defect 10598

	// defect 10654 
	private boolean cbDocNoMismatchErrDisplayed = false;
	// end defect 10654  

	// boolean 
	private boolean cbAlreadyAskedArchive;

	// int
	public int ciMfSearched = 0;

	// Object 
	// defect 10290 
	private GeneralSearchData caGSD = null;
	// end defect 10290 
	private VehicleInquiryData caCancelledRecord =
		new VehicleInquiryData();
	private CommonUtil caUtil = new CommonUtil();

	// Vector 
	private Vector cvCancelledArchiveInfo = new Vector();

	// Constants
	private static final String CANCELED_PLATE_STR =
		" ** CANCELLED PLATE **";

	private static final String CHECK_ARCHIVE_QUESTION_STR =
		"Record not found in Active or\n Inactive file.\n Do you "
			+ "want to check Archive file?\n";
	public static final int MF_DOWN = 20;
	public static final int SEARCH_ARCHIVE = 21;
	public static final int MULT_RECS = 22;
	public static final int SINGLE_REC = 23;
	public static final int NO_REC_FOUND = 24;
	public static final int ARCHIVE_REC_FOUND = 25;
	public static final int PLATE_KEY = 26;

	// defect 10290
	//	public static final int GET_SAVED_VEHICLE = 27;
	//	public static final int GET_OWNER_INFORMATION = 28;
	// public static final int HEADQUARTERS_ID = 1;
	// end defect 10290 

	/**
	 * VCVinKeySelectionKEY006 constructor comment.
	 */
	public VCVinKeySelectionKEY006()
	{
		super();
	}

	/**
	 * Responsible for assigning inquiry info to data object
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData assignMFDownData(VehicleInquiryData aaVehInqData)
	{
		if (aaVehInqData.getMfVehicleData() == null)
		{
			// defect 10290
			createInquiryObject(aaVehInqData);
			// end defect 10290 
		}

		if (caGSD.getKey1().equals(CommonConstant.REG_PLATE_NO))
		{
			aaVehInqData.getMfVehicleData().getRegData().setRegPltNo(
				caGSD.getKey2());
		}
		// defect 10290 
		// Never used from KEY006 
		//		else if (
		//			caGSD.getKey1().equals(
		//				CommonConstant.REG_STICKER_NO))
		//		{
		//			aaVehInqData.getMfVehicleData().getRegData().setRegStkrNo(
		//				caGSD.getKey2());
		//		}
		// end defect 10290 
		else if (caGSD.getKey1().equals(CommonConstant.VIN))
		{
			aaVehInqData.getMfVehicleData().getVehicleData().setVin(
				caGSD.getKey2());
		}
		else if (caGSD.getKey1().equals(CommonConstant.DOC_NO))
		{
			aaVehInqData.getMfVehicleData().getTitleData().setDocNo(
				caGSD.getKey2());
		}

		RTSDate laRTSEffDt = new RTSDate();
		aaVehInqData.setRTSEffDt(laRTSEffDt.getYYYYMMDDDate());

		// defect 6861 
		// Set flag to denote MF down 
		aaVehInqData.getMfVehicleData().setFromMF(false);
		// end defect 6861 

		return aaVehInqData;
	}

	/**
	 * Creates Vehicle Inquiry Object
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 */
	public void createInquiryObject(VehicleInquiryData aaVehInqData)
	{
		MFVehicleData laMFVehData = new MFVehicleData();
		RegistrationData laRegData = new RegistrationData();
		TitleData laTtlData = new TitleData();
		AddressData laRnwlAddrData = new AddressData();
		AddressData laTtlVehAddrData = new AddressData();
		OwnerData laOwnrData = new OwnerData();
		VehicleData laVehicleData = new VehicleData();
		laRegData.setRenwlMailAddr(laRnwlAddrData);
		laTtlData.setTtlVehAddr(laTtlVehAddrData);
		laMFVehData.setRegData(laRegData);
		laMFVehData.setTitleData(laTtlData);
		laMFVehData.setVehicleData(laVehicleData);
		laMFVehData.setOwnerData(laOwnrData);
		laMFVehData.setVehicleData(laVehicleData);
		aaVehInqData.setMfVehicleData(laMFVehData);
		// defect 10290 
		// return aaVehInqData;
		// end defect 10290  
	}

	/**
	 * Returns the Module name constant used by the RTSMediator 
	 * to pass the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.COMMON;
	}

	/**
	 * Sets error message and navigation for Mainframe Down scenario
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 */
	public void handleMainframeDown(VehicleInquiryData aaVehInqData)
	{
		try
		{
			if (!SystemProperty.isCounty())
			{
				RTSException leRTSEx = new RTSException(1);
				leRTSEx.displayError(getFrame());
				setDirectionFlow(AbstractViewController.CANCEL);
			}
			// end defect 9085 
			else if (getTransCode().equals(TransCdConstant.TITLE))
			{
				setNextController(ScreenConstant.TTL040);
				setDirectionFlow(AbstractViewController.NEXT);
				RTSException leRTSEx = new RTSException(20);
				leRTSEx.displayError(getFrame());
				setData(aaVehInqData);
				aaVehInqData = assignMFDownData(aaVehInqData);
			}
			// defect 10290 
			else if (UtilityMethods.isDTA(getTransCode()))
			{
				// end defect 10290 
				setNextController(ScreenConstant.TTL002);
				setDirectionFlow(AbstractViewController.NEXT);
				RTSException leRTSEx = new RTSException(1);
				leRTSEx.displayError(getFrame());
				setData(aaVehInqData);
				aaVehInqData = assignMFDownData(aaVehInqData);
				RTSException leRTSEx2 = null;
				boolean lbChngDirFlow = false;

				TitleValidObj laTtlValidObj =
					(TitleValidObj) aaVehInqData.getValidationObject();

				DealerTitleData laDlrTitleData =
					((FrmVinKeySelectionKEY006) getFrame())
						.getDlrTtlData();

				// defect 10290 
				// Vector no longer used 						
				// Vector lvDlrTitleList =
				//	((FrmVinKeySelectionKEY006) getFrame())
				//		.getDlrTitlDataObjs();
				// end defect 10290 

				//check for data from MF/DTA008
				leRTSEx2 =
					validateDTA008DataFromMFData(
						laDlrTitleData,
						aaVehInqData);

				if (leRTSEx2 != null)
				{
					leRTSEx2.displayError(getFrame());
					lbChngDirFlow = true;
				}
				else
				{
					laTtlValidObj = new TitleValidObj();
					laTtlValidObj.setDlrTtlData(laDlrTitleData);
					aaVehInqData.setValidationObject(laTtlValidObj);
				}

				setDirectionFlow(
					lbChngDirFlow
						? AbstractViewController.CANCEL
						: AbstractViewController.NEXT);

				try
				{
					setData(aaVehInqData);
					getMediator().processData(
						getModuleName(),
						CommonConstant.NO_DATA_TO_BUSINESS,
						aaVehInqData);
				}
				catch (RTSException aeRTSEx3)
				{
					aeRTSEx3.displayError(getFrame());
				}
				if (getFrame() != null)
				{
					getFrame().setVisibleRTS(false);
				}
				return;
			}
			// defect 9636,9642  
			else if (getTransCode().equals(TransCdConstant.REJCOR))
			{
				RTSException leRTSEx = new RTSException(1);
				leRTSEx.displayError(getFrame());
				setDirectionFlow(AbstractViewController.CANCEL);
			}
			// end defect 9636, 9642  
			getMediator().processData(
				getModuleName(),
				CommonConstant.NO_DATA_TO_BUSINESS,
				aaVehInqData);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(getFrame());
		}
	}

	/**
	 * Controls the screen flow from KEY006.  It passes the data to 
	 * the RTSMediator.  
	 * 
	 * @param aiCommand int  
	 * @param aaData Object  
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			// defect 10598
			case INPROCS_TRANS :
				{
					setNextController(ScreenConstant.INQ002);
					setDirectionFlow(AbstractViewController.NEXT);
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
				// end defect 10598 
			case AbstractViewController.ENTER :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					try
					{
						caGSD = (GeneralSearchData) aaData;

						//set flag so that system knows Mainframe has 
						//been searched
						ciMfSearched = 1;

						//set flag so that system knows that Archive 
						//has not been searched yet
						caGSD.setIntKey2(
							CommonConstant.SEARCH_ACTIVE_INACTIVE);

						//Send Trans Code to the Server
						caGSD.setKey3(getTransCode());
						caGSD.setKey5(MFLogError.getErrorString());
						getMediator().processData(
							getModuleName(),
							CommonConstant.GET_VEH,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						if (aeRTSEx
							.getMsgType()
							.equals(RTSException.SERVER_DOWN))
						{
							VehicleInquiryData laVehInqData =
								new VehicleInquiryData();
							laVehInqData.setMfDown(1);

							// defect 6348
							// handle server down just like mf down
							processData(
								VCVinKeySelectionKEY006.MF_DOWN,
								laVehInqData);
							return;
							//handleMainframeDown(inqData);
							// end defect 6348
						}
						else
						{
							aeRTSEx.displayError(getFrame());
						}
					}
					break;
				}
			case AbstractViewController.CANCEL :
				{
					close();

					setDirectionFlow(
						UtilityMethods.isDTA(getTransCode())
							? AbstractViewController.CANCEL
							: AbstractViewController.FINAL);
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

			case VCVinKeySelectionKEY006.MF_DOWN :
				{
					VehicleInquiryData laVehData =
						new VehicleInquiryData();
					laVehData = (VehicleInquiryData) aaData;
					handleMainframeDown(laVehData);
					close();
					break;
				}
			case VCVinKeySelectionKEY006.PLATE_KEY :
				{
					setNextController(ScreenConstant.KEY008);
					setDirectionFlow(AbstractViewController.NEXT);
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
			case VCVinKeySelectionKEY006.MULT_RECS :
				{
					setNextController(ScreenConstant.INQ004);
					setDirectionFlow(AbstractViewController.NEXT);
					VehicleInquiryData laVehData =
						new VehicleInquiryData();
					laVehData = (VehicleInquiryData) aaData;
					if (laVehData.getPartialDataList().firstElement()
						instanceof MFPartialData)
					{
						if (laVehData.getPartialDataList().size()
							> 100)
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
						// defect 10426
						// defect 8561
						//if (!getTransCode().equals(TransCdConstant.SCOT))
						//{
						//	close();
						//}
						// end defect 8561
						// end defect 10426
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

			case VCVinKeySelectionKEY006.NO_REC_FOUND :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					RTSException leRTSEx = new RTSException(57);
					leRTSEx.displayError(getFrame());
				}
			case VCVinKeySelectionKEY006.SEARCH_ARCHIVE :
				{
					boolean lbUseObj = false;
					try
					{
						if (getTransCode()
							.equals(TransCdConstant.REJCOR))
						{
							RTSException leRTSEx = new RTSException(17);
							leRTSEx.displayError(getFrame());
							setDirectionFlow(
								AbstractViewController.FINAL);
							getMediator().processData(
								getModuleName(),
								CommonConstant.NO_DATA_TO_BUSINESS,
								null);
							break;
						}
						else
						{
							VehicleInquiryData laVehInqData =
								(VehicleInquiryData) aaData;

							if (laVehInqData
								.getMfVehicleData()
								.getRegData()
								.getCancPltIndi()
								!= 1)
							{
								int liRet = 0;
								if (!cbAlreadyAskedArchive)
								{
									RTSException leRTSEx =
										new RTSException(
											RTSException
												.INFORMATION_VERIFICATION,
											CHECK_ARCHIVE_QUESTION_STR
												+ caGSD.getKey1()
												+ "="
												+ caGSD.getKey2(),
											ScreenConstant
												.CTL001_FRM_TITLE);

									leRTSEx.setBeep(RTSException.BEEP);
									liRet =
										leRTSEx.displayError(
											getFrame());
									cbAlreadyAskedArchive = true;
									caGSD.setIntKey4(1);
								}
								else
								{
									liRet = RTSException.YES;
								}
								if (liRet == RTSException.YES)
								{
									setDirectionFlow(
										AbstractViewController.CURRENT);

									caGSD.setIntKey2(
										CommonConstant.SEARCH_ARCHIVE);

									//TODO This seems unnecessary 
									// Title, SCOT and DTA are all 
									// that's left. 
									if (caGSD
										.getKey1()
										.equals(CommonConstant.VIN)
										&& (UtilityMethods
											.isDTA(getTransCode())
											|| caGSD.getKey3().equals(
												TransCdConstant.TITLE)
											|| caGSD.getKey3().equals(
												TransCdConstant.SCOT)))
									{
										lbUseObj = true;

									}
								}
								else if (liRet == RTSException.NO)
								{
									if (caGSD.getKey1() != null)
									{
										if (caGSD
											.getKey1()
											.equals(CommonConstant.VIN))
										{
											laVehInqData
												.getMfVehicleData()
												.getVehicleData()
												.setVin(
												caGSD.getKey2());
										}
										else if (
											caGSD.getKey1().equals(
												CommonConstant
													.REG_PLATE_NO))
										{
											laVehInqData
												.getMfVehicleData()
												.getRegData()
												.setRegPltNo(
												caGSD.getKey2());
										}
									}

									cbAlreadyAskedArchive = false;

									if (getTransCode()
										.equals(TransCdConstant.TITLE))
									{
										setNextController(
											ScreenConstant.TTL002);

										getFrame().setVisibleRTS(false);

										setDirectionFlow(
											AbstractViewController
												.NEXT);

										getMediator().processData(
											getModuleName(),
											CommonConstant
												.NO_DATA_TO_BUSINESS,
											aaData);
										break;
									}
									else if (
										getTransCode().equals(
											TransCdConstant.SCOT))
									{
										// defect 6239
										// do not replace 
										// UserSuppliedOwnerData if it 
										//	was found
										if (laVehInqData
											.getMfVehicleData()
											.getUserSuppliedOwnerData()
											== null)
										{
											laVehInqData
												.getMfVehicleData()
												.setUserSuppliedOwnerData(
												new OwnerData());

											laVehInqData
												.getMfVehicleData()
												.getUserSuppliedOwnerData()
												.setOwnrId(
												caGSD.getKey4());
										}
										// end defect 6239

										setNextController(
											ScreenConstant.TTL016);

										setDirectionFlow(
											AbstractViewController
												.NEXT);

										getMediator().processData(
											getModuleName(),
											CommonConstant
												.NO_DATA_TO_BUSINESS,
											aaData);
										// defect 8500	
										break;
										// end defect 8500 
									}
									// DTA is really all that is left
									else if (
										UtilityMethods.isDTA(
											getTransCode()))
									{
										setNextController(
											ScreenConstant.TTL002);

										DealerTitleData laDlrTitleData =
											(
												(FrmVinKeySelectionKEY006) getFrame())
												.getDlrTtlData();

										if (caGSD
											.getKey1()
											.equals(CommonConstant.VIN))
										{
											laDlrTitleData
												.getMFVehicleData()
												.getVehicleData()
												.setVin(
												caGSD.getKey2());
										}
										else if (
											caGSD.getKey1().equals(
												CommonConstant
													.REG_PLATE_NO))
										{
											laDlrTitleData
												.getMFVehicleData()
												.getRegData()
												.setRegPltNo(
												caGSD.getKey2());
										}
										//check for data from 
										//	MF/DTA008

										boolean lbChngDirFlow = false;

										RTSException leRTSEx =
											validateDTA008DataFromMFData(
												laDlrTitleData,
												laVehInqData);

										if (leRTSEx != null)
										{
											leRTSEx.displayError(
												getFrame());
											lbChngDirFlow = true;
										}
										//end check for data from 
										//	MF/DTA008

										TitleValidObj laTtlValidObj =
											new TitleValidObj();

										laTtlValidObj.setDlrTtlData(
											laDlrTitleData);

										laVehInqData
											.setValidationObject(
											laTtlValidObj);

										setDirectionFlow(
											lbChngDirFlow
												? AbstractViewController
													.CANCEL
												: AbstractViewController
													.NEXT);
										try
										{
											setData(aaData);

											getFrame().setVisibleRTS(
												false);

											if (lbChngDirFlow)
											{
												getMediator()
													.processData(
													getModuleName(),
													CommonConstant
														.NO_DATA_TO_BUSINESS,
													leRTSEx);
											}
											else
											{
												getMediator()
													.processData(
													getModuleName(),
													CommonConstant
														.NO_DATA_TO_BUSINESS,
													aaData);
											}
										}
										catch (RTSException aeRTSEx2)
										{
											aeRTSEx2.displayError(
												getFrame());
										}
										break;
									}
									// defect 10290 
									// Removed other else - formerly for 
									// COA 
									// end defect 10290 
								}
								else if (liRet == RTSException.CANCEL)
								{
									cbAlreadyAskedArchive = false;

									// defect 10290 
									// This is Bizarre!  
									//	if (caGSD
									//		.getKey1()
									//		.equals(CommonConstant.VIN))
									//	{
									//		(
									//			(FrmVinKeySelectionKEY006) getFrame())
									//				.setVinText(
									//			caGSD.getKey2());
									//	}
									//	(
									//		(FrmVinKeySelectionKEY006) getFrame())
									//			.setVinCheckBox(
									//		false);
									// end defect 10290 
									return;

								}
							}
							else
							{
								setDirectionFlow(
									AbstractViewController.CURRENT);
								caGSD.setIntKey2(
									CommonConstant.SEARCH_ARCHIVE);
								caGSD.setKey5(
									MFLogError.getErrorString());
							}
							// Search Archive - lvInput returns VINA
							//  Information 
							if (lbUseObj)
							{
								Vector lvInput = new Vector();
								lvInput.addElement(laVehInqData);
								lvInput.addElement(caGSD);
								getMediator().processData(
									getModuleName(),
									CommonConstant.GET_VEH,
									lvInput);
							}
							else
							{
								getMediator().processData(
									getModuleName(),
									CommonConstant.GET_VEH,
									caGSD);
							}
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					};
					break;
				}
			case VCVinKeySelectionKEY006.ARCHIVE_REC_FOUND :
				{
					setNextController(ScreenConstant.CTL005);
					setDirectionFlow(AbstractViewController.NEXT);
					try
					{
						cvCancelledArchiveInfo.addElement(
							caCancelledRecord);
						cvCancelledArchiveInfo.addElement(
							(VehicleInquiryData) aaData);
						cvCancelledArchiveInfo.addElement(caGSD);
						caCancelledRecord = null;
						//Clear out Cancelled record info
						getFrame().setVisibleRTS(false);
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							cvCancelledArchiveInfo);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}
			case VCVinKeySelectionKEY006.SINGLE_REC :
				{
					boolean lbChngDirFlow = false;
					RTSException leRTSExHolder = null;

					// Salvage 
					if (getTransCode().equals(TransCdConstant.SCOT))
					{
						TitleData laTtlData =
							((VehicleInquiryData) aaData)
								.getMfVehicleData()
								.getTitleData();

						if (laTtlData != null
							&& !UtilityMethods.isEmpty(
								laTtlData.getTtlProcsCd()))
						{
							RTSException leRTSEx = new RTSException(85);
							leRTSEx.displayError(getFrame());
							// TODO KPH
							// Do not do Final!  

							//RTSDialogBox laRTSDB =
							//	getMediator().getParent();
							//	if (laRTSDB != null)
							//	{
							//		leRTSEx.displayError(laRTSDB);
							//	}
							//	else
							//	{
							//		leRTSEx.displayError(
							//getMediator().getDesktop());
							//	}
							//	setDirectionFlow(
							//		AbstractViewController.FINAL);
							//	try
							//	{
							//		getMediator().processData(
							//getModuleName(),
							//CommonConstant.NO_DATA_TO_BUSINESS,
							//aaData);
							//	}
							//	catch (RTSException aeRTSEx2)
							//	{
							//		aeRTSEx2.displayError(getFrame());
							//	}
							//	break;
						}
						else
						{
							// defect 6689 
							// Set User ID ONLY if ID was entered and no 
							// ID info was found)
							//if (((VehicleInquiryData) data).
							//	getMfVehicleData().
							//	getUserSuppliedOwnerData() == null)
							if (caGSD.getKey4() != null)
							{
								// defect 8500
								// Test getMfVehicleData().getUserSuppliedOwnerData()
								//  == null
								// defect 10112
								OwnerData laUserData =
									((VehicleInquiryData) aaData)
										.getMfVehicleData()
										.getUserSuppliedOwnerData();

								if (laUserData == null
									|| (UtilityMethods
										.isEmpty(
											laUserData.getOwnrId())))
								{
									// end defect 10112 
									((VehicleInquiryData) aaData)
										.getMfVehicleData()
										.setUserSuppliedOwnerData(
											new OwnerData());
									((VehicleInquiryData) aaData)
										.getMfVehicleData()
										.getUserSuppliedOwnerData()
										.setOwnrId(caGSD.getKey4());
								}
								// end defect 8500	
							}
							// end defect 6689

							cbAlreadyAskedArchive = false;
							setNextController(ScreenConstant.TTL016);
							setDirectionFlow(
								AbstractViewController.NEXT);
							try
							{
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
					}
					else
					{
						//default screen
						setNextController(ScreenConstant.TTL002);

						if (UtilityMethods.isDTA(getTransCode()))
						{
							TitleValidObj laTtlValidObj =
								(TitleValidObj)
									((VehicleInquiryData) aaData)
									.getValidationObject();

							if (laTtlValidObj == null)
							{
								laTtlValidObj = new TitleValidObj();
								(
									(
										VehicleInquiryData) aaData)
											.setValidationObject(
									laTtlValidObj);
							}
							DealerTitleData laDlrTitleData =
								((FrmVinKeySelectionKEY006) getFrame())
									.getDlrTtlData();

							//set vin
							if (caGSD
								.getKey1()
								.equals(CommonConstant.VIN))
							{
								laDlrTitleData
									.getMFVehicleData()
									.getVehicleData()
									.setVin(
									caGSD.getKey2());
							}
							else
							{
								MFVehicleData laMfVeh =
									((VehicleInquiryData) aaData)
										.getMfVehicleData();

								laDlrTitleData
									.getMFVehicleData()
									.getVehicleData()
									.setVin(
									laMfVeh.getVehicleData().getVin());
							}

							//check for data from MF/DTA008
							leRTSExHolder =
								validateDTA008DataFromMFData(
									laDlrTitleData,
									(VehicleInquiryData) aaData);

							if (leRTSExHolder != null)
							{
								leRTSExHolder.displayError(getFrame());
								lbChngDirFlow = true;
							}
							// end check for data from MF/DTA008
							laTtlValidObj.setDlrTtlData(laDlrTitleData);
						}
						else
						{
							if (caGSD != null)
							{
								if (caGSD
									.getKey1()
									.equals(CommonConstant.VIN))
								{
									((VehicleInquiryData) aaData)
										.getMfVehicleData()
										.getVehicleData()
										.setVin(caGSD.getKey2());
								}
								else if (
									caGSD.getKey1().equals(
										CommonConstant.REG_PLATE_NO))
								{
									((VehicleInquiryData) aaData)
										.getMfVehicleData()
										.getRegData()
										.setRegPltNo(caGSD.getKey2());
								}
							}
						}

						setDirectionFlow(
							lbChngDirFlow
								? AbstractViewController.CANCEL
								: AbstractViewController.NEXT);

						try
						{
							setData(aaData);
							getFrame().setVisibleRTS(false);
							Object laObj =
								lbChngDirFlow ? leRTSExHolder : aaData;

							cbAlreadyAskedArchive = false;
							getMediator().processData(
								getModuleName(),
								CommonConstant.NO_DATA_TO_BUSINESS,
								laObj);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}

						break;
					}
				}
		}
	}

	/**
	 * Creates the actual frame, stores the protected variables 
	 * needed by the VC, and sends the data to the frame.
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
		//If mainframe has not been searched yet, set the Frame
		if (aaData == null)
		{
			setTransCode(asTransCode);

			if (getFrame() == null)
			{
				// If this is not a cumulative transaction go ahead and
				// set the frame.
				setFrame(
					new FrmVinKeySelectionKEY006(
						getMediator().getDesktop()));
				//csTransCd = asTransCode;
				super.setView(
					avPreviousControllers,
					asTransCode,
					aaData);
			}
			// defect 7451
			//	Check for DTA, return to previous screen.
			//	DTA will return to DTA008. All other events return to
			//  the Main Menu
			//  Note: All DTA transactions TransCds are DTAORD until
			//  after the Title Type. At that point the DTA TransCd 
			//  will be set to one of the following:
			//		           DTAORD, DTAORK, DTANTD, DTANTK
			else if (UtilityMethods.isDTA(asTransCode))
			{
				setDirectionFlow(AbstractViewController.PREVIOUS);
				processData(CANCEL, getData());
			}
			// end defect 7451
			return;
		}
		if (getFrame() == null)
		{
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmVinKeySelectionKEY006(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmVinKeySelectionKEY006(
						getMediator().getDesktop()));
			}
			setTransCode(asTransCode);
			super.setView(avPreviousControllers, asTransCode, aaData);
		}
		else if (aaData instanceof GeneralSearchData)
		{
			processData(ENTER, aaData);
		}
		else
		{
			//  If mainframe has been searched, VehicleInquiryData is 
			//  returned and processing for next screen is run

			// defect 5995, 
			// If VehicleInquiryData is not returned, will throw 
			//  ClassCastException.  This happens when DTA sends a 
			//  DealerTitleData object and a ClassCastException	is 
			//  thrown, sc r315891
			//	Need to have better understanding what to be in this 
			//  scenario
			// end defect 5995

			// defect 10290 
			// refactor laInqDat to laVehInqData 
			VehicleInquiryData laVehInqData =
				(VehicleInquiryData) aaData;

			String lsFrmLast4 =
				FrmVinKeySelectionKEY006.getLast4Digits();

			if (laVehInqData.getMfDown() == 1)
			{
				processData(MF_DOWN, laVehInqData);
				return;
			}
			// defect 10654 
			// add check to ensure don't show message twice 
			else if (
				!cbDocNoMismatchErrDisplayed
					&& laVehInqData.getMfVehicleData() != null
					&& !UtilityMethods.isEmpty(lsFrmLast4))
			{
				// end defect 10654 
				String lsDocNo =
					laVehInqData
						.getMfVehicleData()
						.getTitleData()
						.getDocNo();

				if (!UtilityMethods.isEmpty(lsDocNo)
					&& !lsFrmLast4.equals(lsDocNo.substring(13, 17)))
				{
					RTSException leRTSEx = new RTSException(27);
					RTSDialogBox laRTSDB = getMediator().getParent();

					// defect 6804
					//  If the frame is null (Title) the application
					//	 will return to KEY006 screen. 
					//	There is no parent for Title.
					//
					//  DTA parent is FrmDealerTitleTransactionDTA008().
					//
					// The assumption is if there is a parent then we 
					//	are DTA.

					// defect 10654
					cbDocNoMismatchErrDisplayed =
						UtilityMethods.isDTA(asTransCode);
					// end defect 10654 

					if (laRTSDB != null)
					{
						leRTSEx.displayError(laRTSDB);
					}
					else
					{
						leRTSEx.displayError(
							getMediator().getDesktop());
						return;
					}
					//return
					// end defect 6804
				}
			}
			//else
			if (laVehInqData.getMfVehicleData() != null
				&& (laVehInqData
					.getMfVehicleData()
					.getRegData()
					.getCancPltIndi()
					== 1)
				&& laVehInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ACTIVE_INACTIVE)
			{
				// defect 10538
				// defect 9329
				//	Add if statement to display message for cancel plate
				//	 if search key by RegPltNo
				//				if (caGSD
				//					.getKey1()
				//					.equals(CommonConstant.REG_PLATE_NO))
				//				{
				//					RTSException leRTSExHolder3 = null;
				//					leRTSExHolder3 = new RTSException(729);
				//					leRTSExHolder3.displayError(getFrame());
				//					return;
				//					// end defect 9329
				//				}
				//				else
				//				{
				caCancelledRecord = laVehInqData;
				processData(SEARCH_ARCHIVE, laVehInqData);
				//}
				// end defect 10538
			}
			else if (laVehInqData.getNoMFRecs() > 1)
			{
				processData(MULT_RECS, laVehInqData);
			}
			else if (
				(laVehInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ACTIVE_INACTIVE)
					&& (laVehInqData.getNoMFRecs() == 1)
					&& (caCancelledRecord.getMfVehicleData() == null))
			{
				// defect 10598 
				if (laVehInqData.hasInProcsTrans())
				{
					processData(INPROCS_TRANS, laVehInqData);
				}
				else
				{
					caUtil.validateVehWts(laVehInqData);
					processData(SINGLE_REC, laVehInqData);
				}
			}
			else if (
				(laVehInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ACTIVE_INACTIVE)
					&& (laVehInqData.getNoMFRecs() == 0))
			{
				if (laVehInqData.hasInProcsTrans())
				{
					processData(INPROCS_TRANS, laVehInqData);
				}
				else
				{
					processData(SEARCH_ARCHIVE, laVehInqData);
				}
				// end defect 10598
			}
			else if (
				(laVehInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ARCHIVE)
					&& (laVehInqData.getNoMFRecs() == 0)
					&& (caCancelledRecord == null
						|| caCancelledRecord.getMfVehicleData() == null))
			{
				caUtil.validateVehWts(laVehInqData);
				if (caGSD.getKey1().equals(CommonConstant.VIN))
				{
					laVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.setVin(
						caGSD.getKey2());
				}
				processData(SINGLE_REC, laVehInqData);
			}
			else if (
				(laVehInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ARCHIVE)
					&& (laVehInqData.getNoMFRecs() == 1)
					&& (caCancelledRecord.getMfVehicleData() == null))
			{
				// defect 10786 
				if (laVehInqData.hasInProcsTrans())
				{
					processData(INPROCS_TRANS, laVehInqData);
				}
				else
				{

					caUtil.validateVehWts(laVehInqData);
					processData(SINGLE_REC, laVehInqData);
				}
				// end defect 10786 
			}
			else if (
				(laVehInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ARCHIVE)
					&& (laVehInqData.getNoMFRecs() == 0)
					&& (laVehInqData.getPartialDataList() == null
						|| laVehInqData.getPartialDataList().size() == 0)
					&& (caCancelledRecord
						.getMfVehicleData()
						.getRegData()
						.getCancPltIndi()
						== 1))
			{
				caCancelledRecord
					.getMfVehicleData()
					.getOwnerData()
					.setName1(
					CANCELED_PLATE_STR);
				processData(SINGLE_REC, caCancelledRecord);
			}
			else if (
				(laVehInqData.getSearchArchiveIndi()
					== CommonConstant.SEARCH_ARCHIVE)
					&& (caCancelledRecord
						.getMfVehicleData()
						.getRegData()
						.getCancPltIndi()
						== 1))
			{
				caCancelledRecord
					.getMfVehicleData()
					.getOwnerData()
					.setName1(
					CANCELED_PLATE_STR);
				processData(ARCHIVE_REC_FOUND, laVehInqData);
			}
			// end defect 10290 
		}
	}

	/**
	 * Validate DTA008 Data from Main Frame
	 * 
	 * @param aaDealerTitleData DealerTitleData
	 * @param aaVehicleInquiryData VehicleInquiryData
	 * @return RTSException 
	 */
	public static RTSException validateDTA008DataFromMFData(
		DealerTitleData aaDlrTtlData,
		VehicleInquiryData aaVehInqData)
	{
		RTSException leRTSEx = null;
		String lsNewPlateNo = "";
		int liNewRegExpMo = 0;
		int liNewRegExpYr = 0;

		//clean up data	
		if (aaDlrTtlData.getNewPltNo() != null)
		{
			lsNewPlateNo = aaDlrTtlData.getNewPltNo().trim();
		}
		liNewRegExpMo = aaDlrTtlData.getNewRegExpMo();
		liNewRegExpYr = aaDlrTtlData.getNewRegExpYr();

		// check mainframe and data entered in DTA008
		MFVehicleData laMFVehData = null;

		if (aaVehInqData.getNoMFRecs() > 0)
		{
			laMFVehData = aaVehInqData.getMfVehicleData();
			// defect 8329
			//	Check RegInvldIndi, save for TTL003 indicators
			if (laMFVehData.getRegData().getRegInvldIndi() == 1)
			{
				laMFVehData.getRegData().setDTARegInvlIndi(
					laMFVehData.getRegData().getRegInvldIndi());
			}
			// end defect 8329
		}
		else
		{
			laMFVehData = aaDlrTtlData.getMFVehicleData();

			// defect 6861
			// Setting boolean from original request 
			// MfVehicleData should never be null! 
			if (aaVehInqData.getMfVehicleData() != null)
			{
				laMFVehData.setFromMF(
					aaVehInqData.getMfVehicleData().isFromMF());
			}
			// end defect 6861 
		}

		String lsRegPltCd = laMFVehData.getRegData().getRegPltCd();
		if (lsRegPltCd == null)
		{
			//default reg plate code "PSP"
			lsRegPltCd = InventoryConstant.DEFAULT_ABBR;
		}
		PlateTypeData laPlateTypeData =
			PlateTypeCache.getPlateType(lsRegPltCd);

		if (laPlateTypeData != null
			&& laPlateTypeData.getAnnualPltIndi() == 1)
		{
			// if plate not entered but month and year entered, error
			if (lsNewPlateNo.length() == 0
				&& liNewRegExpMo > 0
				&& liNewRegExpYr > 0)
			{
				leRTSEx = new RTSException(659);
			}
		}
		else
		{
			//Not an annual plate
			if (aaVehInqData.getNoMFRecs() == 0
				&& aaVehInqData.getMfDown() == 0
				&& lsNewPlateNo.length() == 0)
			{
				leRTSEx = new RTSException(659);
			}
		}

		// defect 7700
		// Clear DocNo from Dealer if record not found
		if (leRTSEx == null
			&& aaVehInqData.getNoMFRecs() == 0
			&& aaVehInqData.isMFUp())
		{
			aaDlrTtlData.getMFVehicleData().getTitleData().setDocNo("");
		}
		// end defect 7700
		return leRTSEx;
	}
}
