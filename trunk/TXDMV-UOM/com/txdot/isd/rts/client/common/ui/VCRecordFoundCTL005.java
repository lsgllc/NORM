package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * VCRecordFoundCTL005.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs/TP		06/05/2002	MultiRecs in Archive 
 * 							defect 4019
 * Ray Rowehl	04/28/2003	in processData, if case is SEARCH_ARCHIVE,
 *							nextController should be TTL018.
 *							defect 5794
 * Ray Rowehl	03/09/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	05/20/2005	If the data coming in is GeneralSearchData.
 * 							we need to do another search before 
 * 							attempting to show the results.
 * 							modify setView()
 * 							defect 6742 Ver 5.2.3
 * Ray Rowehl	06/13/2005	When canceling, see if this is CCO.  If we 
 * 							are doing CCO at Headquarters, we want to 
 * 							go back to the KEY001 screen.
 * 							modify processData()
 * 							defect 6691 Ver 5.2.3
 * Ray Rowehl	07/05/2005	Use TransCdConst to do comparision for CCO
 * 							defect 7885 Ver 5.2.3
 * Jeff S.		02/02/2006	Removed code that moved focus to the KEY001
 * 							input field when there was an error.  This
 * 							is handled in KEY001.
 * 							modify processData()
 * 							defect 7885 Ver 5.2.3
 * K Harrell	03/09/2007	Implement System AbstractProperty.isHQ()
 * 							modify processData()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	05/21/2008 	Use 'SCOT' vs. 'SLVG'
 * 							No longer use COA 
 * 							modify processData()
 * 							defect 9636, 9642 Ver 3 Amigos PH B 
 * B Hargrove	01/28/2009  Comment out all the 'special code' for
 * 							Refund, Veh Inq, HotChk, etc to allow for
 * 							normal processing of 'No record found'
 * 							pop-up message. The pop-up frame will now  
 * 							correctly have focus.
 *							Note: Refund is a little differenct because
 *							it continues with  'Customer Name INQ007' 
 *							even if no record found. 						
 * 							modify processData()
 * 							defect 9043 Ver Defect_POS_D
 * B Hargrove	06/08/2009	Remove all 'Cancelled Sticker' references.
 * 							modify processData()
 * 							defect 9953 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * View Controller for Screen: Record Found CTL005
 * 
 * @version	Defect_POS_F	06/08/2009 
 * @author	Joseph Peters
 * <br>Creation Date:		09/05/2001 09:56:32
 */
public class VCRecordFoundCTL005 extends AbstractViewController
{
	public static final int SEARCH_ARCHIVE = 80;
	public static final int MULTI = 81;
	public static final int MULTI_MULTI = CommonConstant.MULTI_MULTI;
	public static final int MULTI_SINGLE = CommonConstant.MULTI_SINGLE;

	/**
	 * VCRecordFoundCTL005 constructor.
	 */
	public VCRecordFoundCTL005()
	{
		super();
	}

	/**
	 * Create VehicleInquiryData
	 * 
	 * @return VehicleInquiryData
	 * @param aaGSD GeneralSearchData
	 */
	private VehicleInquiryData createVehInqData(GeneralSearchData aaGSD)
	{
		VehicleInquiryData laVehicleInquiryData =
			new VehicleInquiryData();
		MFVehicleData laMFVehicleData = new MFVehicleData();
		RegistrationData laRegistrationData = new RegistrationData();
		laMFVehicleData.setRegData(laRegistrationData);
		VehicleData laVehicleData = new VehicleData();
		laMFVehicleData.setVehicleData(laVehicleData);
		laVehicleInquiryData.setMfVehicleData(laMFVehicleData);
		TitleData laTitleData = new TitleData();
		laMFVehicleData.setTitleData(laTitleData);
		if (aaGSD.getKey1().equals(CommonConstant.REG_PLATE_NO))
		{
			laRegistrationData.setRegPltNo(aaGSD.getKey2());
		}
		else if (aaGSD.getKey1().equals(CommonConstant.VIN))
		{
			laVehicleData.setVin(aaGSD.getKey2());
		}
		else if (aaGSD.getKey1().equals(CommonConstant.DOC_NO))
		{
			laTitleData.setDocNo(aaGSD.getKey2());
		}
		else if (aaGSD.getKey1().equals(CommonConstant.REG_STICKER_NO))
		{
			laRegistrationData.setRegStkrNo(aaGSD.getKey2());
		}
		return laVehicleInquiryData;
	}

	/**
	 * Returns the Module name constant used by the RTSMediator to 
	 * pass the data to the appropriate Business Layer class.
	 * 
	 * @return int
	 */
	public int getModuleName()
	{
		return GeneralConstant.COMMON;
	}

	/**
	 * Controls the screen flow from CTL005.  It passes the data to 
	 * the RTSMediator.
	 * 
	 * @param aiCommand int 
	 * @param aaData Object
	 */
	public void processData(int aiCommand, Object aaData)
	{
		switch (aiCommand)
		{
			case AbstractViewController.ENTER :
				{

					if (getTransCode().equals(TransCdConstant.TITLE)
						|| getTransCode().equals(TransCdConstant.CORTTL))
					{
						setDirectionFlow(AbstractViewController.NEXT);
						setNextController(ScreenConstant.TTL002);
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
						getFrame().setVisibleRTS(false);
					}
					// defect 9043
					// Do not need any special coding for most of these 
					// events. 'No record found' popup did not have focus.
					else if (
						getTransCode().equals(TransCdConstant.REFUND))
					{
						new RTSException(57).displayError(getFrame());
						getFrame().setVisibleRTS(false);
						setDirectionFlow(AbstractViewController.NEXT);
						setNextController(ScreenConstant.INQ007);
						try
						{
							// Need to get to the GeneralSearchData in
							// the caData object vector to save either
							// the RegPltNo, VIN, or DocNo (actually, it
							// can only be search by Plate No to return
							// both a Cancelled Plate + Archive).
							GeneralSearchData laGSD = 
								new GeneralSearchData();
							Vector lvCaData = (Vector) this.getData();
							for (int i = 0; i < lvCaData.size(); i++)
							{
								if (lvCaData.elementAt(i) instanceof
									GeneralSearchData)
								{
									laGSD = (GeneralSearchData) 
										lvCaData.elementAt(i);
								}
							}

							getMediator().processData(
								getModuleName(),
								CommonConstant.NO_DATA_TO_BUSINESS,
								// Calling createVehInqData() to clear
								// everything, then populate RegPltNo,
								// VIN, or DocNo for 'no record found'. 
								//createVehInqData(
								//	(GeneralSearchData) aaData));
								createVehInqData(
									(GeneralSearchData) laGSD));
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
						getFrame().setVisibleRTS(false);
					}
					else
					{
//						boolean lbCancelAllScreen = false;
//						if (getTransCode()
//							.equals(TransCdConstant.VEHINQ)
//							|| getTransCode().equals(
//								TransCdConstant.TAWPT)
//							|| getTransCode().equals(TransCdConstant.STAT)
//							|| getTransCode().equals(
//								TransCdConstant.HOTCK)
//							|| getTransCode().equals(
//								TransCdConstant.CKREDM)
//							|| getTransCode().equals(
//								TransCdConstant.HOTDED)
//							|| getTransCode().equals(
//								TransCdConstant.HCKITM))
//						{
//							lbCancelAllScreen = true;
//						}
//
//						getFrame().setVisibleRTS(false);
//						if (lbCancelAllScreen)
//						{
//							setDirectionFlow(
//								AbstractViewController.FINAL);
//							try
//							{
//								getMediator().processData(
//									getModuleName(),
//									CommonConstant.NO_DATA_TO_BUSINESS,
//									aaData);
//							}
//							catch (RTSException aeRTSEx)
//							{
//								aeRTSEx.displayError(getFrame());
//							}
//						}
//
						new RTSException(57).displayError(getFrame());
//						if (!lbCancelAllScreen)
//						{
							setDirectionFlow(
								AbstractViewController.CANCEL);
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
//						}
					// end defect 9043
					}
					break;
				}

			case AbstractViewController.CANCEL :
				{
					try
					{
						// defect 9085 
						// Use System AbstractProperty.isHQ()
						if (getTransCode().equals(TransCdConstant.CCO)
							&& SystemProperty.isHQ())
							// && UtilityMethods.isHeadquarters())
							// end defect 9085 
						{
							setDirectionFlow(
								AbstractViewController.PREVIOUS);
						}
						else
						{
							setDirectionFlow(
								AbstractViewController.FINAL);
						}
						getMediator().processData(
							getModuleName(),
							CommonConstant.NO_DATA_TO_BUSINESS,
							null);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					getFrame().setVisibleRTS(false);
					break;
				}

			case SEARCH_ARCHIVE :
				{

					if (getTransCode().equals(TransCdConstant.TAWPT))
					{
						setDirectionFlow(AbstractViewController.FINAL);
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

						new RTSException(58).displayError(getFrame());

					}
					else
					{
						setDirectionFlow(AbstractViewController.NEXT);
						if (getTransCode()
							.equals(TransCdConstant.TITLE))
						{
							setNextController(ScreenConstant.TTL002);
						}
						else if (
							getTransCode().equals(
								TransCdConstant.STAT))
						{
							setNextController(ScreenConstant.TTL006);
						}
						// defect 5794
						// new if block to send control to TTL018 if 
						// this is CCO
						else if (
							getTransCode().equals(TransCdConstant.CCO))
						{
							setNextController(ScreenConstant.TTL018);
						}
						// end defect 5794 
						else
						{
							setNextController(ScreenConstant.REG003);
						}
						try
						{
							((GeneralSearchData) aaData).setIntKey2(
								CommonConstant.SEARCH_ARCHIVE);
							getMediator().processData(
								getModuleName(),
								CommonConstant.SEARCH_ARCHIVE,
								aaData);
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(getFrame());
						}
					}
					getFrame().setVisibleRTS(false);
					break;
				}

			case MULTI :
				{
					setDirectionFlow(AbstractViewController.CURRENT);
					((VehicleInquiryData) aaData).setValidationObject(
						getTransCode());
					try
					{
						getMediator().processData(
							getModuleName(),
							CommonConstant.MULTI_ARCHIVE,
							aaData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(getFrame());
					}
					break;
				}

			case MULTI_MULTI :
				{
					setDirectionFlow(AbstractViewController.NEXT);
					setNextController(ScreenConstant.INQ004);
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

			case MULTI_SINGLE :
				{
					VehicleInquiryData laVehData =
						new VehicleInquiryData();
					String lsTransCd = getTransCode();
					laVehData = (VehicleInquiryData) aaData;
					{
						if (laVehData.getSpecialOwner() == 1)
						{
							if (lsTransCd
								.equals(TransCdConstant.VEHINQ)
								|| lsTransCd.equals(
									TransCdConstant.DRVED))
							{
								setNextController(
									ScreenConstant.REG002);
							}
							else if (
								laVehData
									.getMfVehicleData()
									.getRegData()
									.getCancPltIndi()
									// defect 9953
									== 1)
									//== 1
									//|| laVehData
									//	.getMfVehicleData()
									//	.getRegData()
									//	.getCancStkrIndi()
									//	== 1)
									// end defect 9953
							{
								if (lsTransCd
									.equals(TransCdConstant.REFUND))
								{
									RTSException leRTSEx =
										new RTSException(57);
									leRTSEx.displayError(getFrame());
									setNextController(
										ScreenConstant.INQ007);
									setDirectionFlow(
										AbstractViewController.NEXT);
									getFrame().setVisibleRTS(false);
									try
									{
										getMediator().processData(
											getModuleName(),
											CommonConstant
												.NO_DATA_TO_BUSINESS,
											laVehData);
									}
									catch (RTSException aeRTSEx2)
									{
										aeRTSEx2.displayError(
											getFrame());
									}
									break;
								}
								else
								{
									RTSException leRTSEx = null;
									if (lsTransCd
										.equals(TransCdConstant.HOTCK)
										|| lsTransCd.equals(
											TransCdConstant.HOTDED)
										|| lsTransCd.equals(
											TransCdConstant.CKREDM))
									{
										leRTSEx = new RTSException(537);
									}
									else if (
										lsTransCd.equals(
											TransCdConstant.HCKITM))
									{
										leRTSEx = new RTSException(537);
									}
									else
									{
										leRTSEx = new RTSException(729);
									}
									leRTSEx.displayError(getFrame());
									// defect 7885
									// Not needed here since we are moving 
									// focus anytime we go to action 
									// performed
									//(
									//	(FrmInquiryKeySelectionKEY001) getFrame())
									//	.focusTxtField();
									// end defect 7885
									break;
								}
							}
							else
							{
								RTSException leRTSEx = null;
								if (lsTransCd
									.equals(TransCdConstant.REFUND))
								{
									leRTSEx = new RTSException(57);
								}
								else
								{
									leRTSEx = new RTSException(149);
								}
								leRTSEx.displayError(getFrame());
								// defect 7885
								// Not needed here since we are moving 
								// focus anytime we go to action 
								// performed
								//(
								//	(FrmInquiryKeySelectionKEY001) getFrame())
								//	.focusTxtField();
								// end defect 7885
								break;
							}
						}
						else if (
							lsTransCd.equals(TransCdConstant.REJCOR))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.CORREG))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.DUPL))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.EXCH))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.VEHINQ))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.PAWT))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.TAWPT))
						{
							setNextController(ScreenConstant.MRG010);
						}
						else if (
							lsTransCd.equals(TransCdConstant.STAT))
						{
							setNextController(ScreenConstant.TTL006);
						}
						else if (
							lsTransCd.equals(TransCdConstant.RENEW))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.REPL))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.SBRNW))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.ADDR))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (lsTransCd.equals(TransCdConstant.CCO))
						{
							setNextController(ScreenConstant.TTL018);
						}
						else if (
							lsTransCd.equals(TransCdConstant.HOTCK))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.REFUND))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.CKREDM))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.HOTDED))
						{
							setNextController(ScreenConstant.REG003);
						}
						else if (
							lsTransCd.equals(TransCdConstant.HCKITM))
						{
							setNextController(ScreenConstant.REG003);
						}
						// defect 9636 
						else if (
							lsTransCd.equals(TransCdConstant.SCOT))
							//lsTransCd.equals(TransCdConstant.SLVG))
						{
							setNextController(ScreenConstant.TTL016);
						}
						// end defect 9636 
						else if (
							lsTransCd.equals(TransCdConstant.STAT))
						{
							setNextController(ScreenConstant.TTL006);
						}
						else if (
							lsTransCd.equals(TransCdConstant.DTAORD))
						{
							setNextController(ScreenConstant.TTL002);
						}
						else if (
							lsTransCd.equals(TransCdConstant.TITLE))
						{
							setNextController(ScreenConstant.TTL002);
						}
						else if (
							lsTransCd.equals(TransCdConstant.REJCOR))
						{
							setNextController(ScreenConstant.TTL002);
						}
						// defect 9642 
						// COA transcd assigned after record found in 
						// Salvage Event
						//else if (lsTransCd.equals(TransCdConstant.COA))
						//{
						//	setNextController(ScreenConstant.TTL015);
						//}
						// end defect 9642 
						setDirectionFlow(AbstractViewController.NEXT);
						try
						{
							setData(aaData);
							if (!lsTransCd.equals(TransCdConstant.CCO))
							{
								getFrame().setVisibleRTS(false);
							}
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
		java.util.Vector avPreviousControllers,
		String asTransCode,
		Object aaData)
	{
		// defect 6742
		// if we have a GSD coming in, try searching archive again.
		// assume it has been set from inq004
		if (aaData instanceof GeneralSearchData)
		{
			processData(SEARCH_ARCHIVE, aaData);
			return;
		}
		// end defect 6742

		if (aaData instanceof VehicleInquiryData)
		{
			VehicleInquiryData laVehInq = (VehicleInquiryData) aaData;
			if (laVehInq.getMultiArchiveStatus() != 0)
			{
				processData(laVehInq.getMultiArchiveStatus(), aaData);
				return;
			}
		}

		if (getFrame() == null)
		{
			RTSDialogBox laRTSDB = getMediator().getParent();
			if (laRTSDB != null)
			{
				setFrame(new FrmRecordFoundCTL005(laRTSDB));
			}
			else
			{
				setFrame(
					new FrmRecordFoundCTL005(
						getMediator().getDesktop()));
			}
		}
		// defect 6691
		else if (aaData == null)
		{
			// if data is null, treat as a cancel from previous screen 
			processData(AbstractViewController.CANCEL, null);
			return;
		}
		// end defect 6691
		super.setView(avPreviousControllers, asTransCode, aaData);
	}
}
