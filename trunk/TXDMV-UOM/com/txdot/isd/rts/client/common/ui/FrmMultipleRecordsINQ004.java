package com.txdot.isd.rts.client.common.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.common.business.CommonUtil;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * FrmMultipleRecordsINQ004.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * N Ting		04/16/2002	Fix UAT defect 3513, added PrintOptions in 
 * 							GeneralSearchData
 * J Kwik		04/16/2002	Fix UAT defect 3524, added constructed 
 * 							with JDialog argument
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()    
 * MAbs/TP		06/05/2002	Added ownerid to searchData 
 * 							defect 4159
 * B Arredondo	01/21/2003	Change preferred width of VIN to 185 so 
 * 							the whole VIN will be displayed.
 *							modify setMultPartialRecs(),setMultRegRecs()
 *							defect 5213   
 * Ray Rowehl	08/13/2003	copy over ExemptIndi when processing 
 * 							partial reg data
 *							modify actionPerformed()
 *							defect 6195
 * B Hargrove	01/16/2004	copy over ClaimComptCntyNo and 
 * 							EmissionSourceCd when processing partial  
 *                          reg data.
 *							modify actionPerformed()
 *							defect 6706 Rel 5.1.5 fix 2.
 * Ray Rowehl	02/18/2005	Change reference to CommonUtil
 * 							cleanup code
 * 							defect 7705 Ver 5.2.3
 * T Pederson	03/15/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	03/31/2005	Removed setNextFocusableComponent.
 * 							defect 7885 Ver 5.2.3 
 * K Harrell	05/03/2005	Remove reference to RegistrationMiscData
 * 							modify actionPerformed() 
 * 							defect 8188 Ver 5.2.3 
 * S Johnston	06/20/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							defect 8240 Ver 5.2.3
 * Ray Rowehl	06/28/2005	Set the key3 of GSD to current transcode
 * 							so MfAccess can refer to it.
 * 							modify actionPerformed()
 * 							defect 8213 Ver 5.2.3
 * T Pederson	07/29/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	08/26/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * J Rue		02/09/2007	add Mult Special Plates
 * 							add setMultPartialSpclPltsRecs()
 * 							modify setData()
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/09/2007	add SpclRegId
 * 							add setMultPartialSpclPltsRecs()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/21/2007	Change else if to else
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates
 * K Harrell	04/03/2007	add Key3, IntKey2 to GSD for Special Plates
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * J Rue		04/23/2007	Add RegPltCd to frame
 * 							modify setData(), 
 * 							modify setMultPartialSpclPltsRecs()
 * 							defect 9086 Ver Special Plates
 * J Rue		05/01/2007	Add SpclDocNo to frame
 * 							modify setData(), 
 * 							modify setMultPartialSpclPltsRecs()
 * 							defect 9086 Ver Special Plates
 * J Rue		05/29/2007	Add sort to compare expiration periods
 * 							modify setData()
 * 							defect 9086 Ver Special Plates
 * J Rue		06/08/2007	Get SpecialPlateRegisData matching to the 
 * 							RegistrationData
 * 							add getSpclPltRegisData()
 * 							modify actionPefformed()
 * 							defect 9086 Ver Special Plates
 * J Rue		08/06/2007	Return a RTSException for Cancel
 * 							modify actionPerformed()
 * 							defect 9211 Ver Special Plates
 * J Rue		09/05/2007	Update comments
 * 							modify actionPerformed()
 * 							defect 9211 Ver Special Plates
 * K Harrell	05/21/2008 	Use 'SCOT' vs. 'SLVG'
 * 							No longer use  COA  
 * 							modify setData()
 * 							defect 9636, 9642 Ver 3 Amigos PH B 
 * B Hargrove	01/21/2009  Need to capture the Insurance Verification
 * 							result.
 * 							modify actionPerformed()
 * 							defect 9691 Ver Defect_POS_D
 * K Harrell	03/26/2010	"Records Not Applicable" checkbox should be
 * 							disabled/not visible for "REJCOR" 
 * 							(Correct Title Rejection)
 * 							modify setData() 
 * 							defect 10416 Ver POS_640 
 * K Harrell	05/24/2010	add ivjlblRecords, get method 
 * 							add caPrmtData, caTableMultPrmtRecs,
 * 							 csTransCd, PERMIT_TITLE  
 * 							add setMultPrmtRecs(), verifyIndicators()
 * 							modify actionPerformed(), setData(),
 * 							 getchkRecrdsNotApplcble(),
 * 							 getFrmMultipleRecordsINQ004ContentPane1()  
 * 							defect 10491 Ver 6.5.0
 * K Harrell	06/30/2010 	add TXT_REC_NOT_APPL
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	07/14/2010 	Present Error Message if Expired on Duplicate.
 * 							Custom msg if 999. 
 * 							modify actionPerformed() 
 * 							defect 10491 Ver 6.5.0
 * K Harrell	07/15/2010	Present DocNo in setMultPartialRecs() 
 * 							modify setMultPartialRecs()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	08/11/2010	add AutoResizeLastColumn 
 * 							modify setMultiPartialSpclPltsRecs() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	09/22/2010	Implement "ENTER" vs. "SINGLE_REC" 
 * 							modify actionPerformed()
 * 							defect 10598 Ver 6.6.0 
 * K Harrell	12/27/2010	modify for Special Plate Permit (Insignia) 
 * 							add caTableMultSpclPltPrmtRecs, 
 * 							  cvSpclPltPrmtData
 *							add SPCL_PLT_PRMT_TITLE
 *							add setMultSpclPltPrmtRecs() 
 *							modify actionPerformed(), setData()
 *							defect 10700 Ver 6.7.0
 * K Harrell	06/19/2011	modify for Timed Permit modification 
 * 							modify setData()
 * 							defect 10844 Ver 6.8.0  
 * B Woodson	02/10/2012	modify actionPerformed()
 * 							defect 11228 Ver 6.10.0
 *----------------------------------------------------------------------
 */
/**
 * Screen MultipleRecords INQ004
 *
 * @version 6.8.0 			02/10/2012
 * @author	Joe Peters
 * <br>Creation Date:		06/27/2001 13:43:58
 */

public class FrmMultipleRecordsINQ004
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjbtnPanel = null;
	private JCheckBox ivjchkRecrdsNotApplcble = null;
	private JPanel ivjFrmMultipleRecordsINQ004ContentPane1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjtblMultRecs = null;

	// Object
	private CommonUtil caUtil = new CommonUtil();
	private RegistrationData caRegData = new RegistrationData();
	private VehicleInquiryData caVehicleInfo;
	private TMINQ004 caTableMultRecs;
	private TMINQ004REG caTableMultRegRecs;
	private TMINQ004SpclPlts caTableMultSpclPltRecs;

	// defect 10700
	private Vector cvSpclPltPrmtData;
	private TMINQ004SpclPltPrmt caTableMultSpclPltPrmtRecs;
	private final static String SPCL_PLT_PRMT_TITLE =
		"Special Plate Duplicate Insignia Search Results   INQ004";
	// end defect 10700 

	// defect 10491
	private String csTransCd;
	boolean cbPrmtAppl;
	private PermitData caPrmtData;
	private TMINQ004Permit caTableMultPrmtRecs;
	private final static String PERMIT_TITLE =
		"Permit Search Results   INQ004";
	private JLabel ivjlblRecords = null;
	private final static String TXT_REC_NOT_APPL =
		"Record not applicable";
	// end defect 10491 

	// Text Constants 
	private final static String FRM_NAME_INQ004 =
		"FrmMultipleRecordsINQ004";
	private final static String FRM_TITLE_INQ004 =
		"Multiple Records       INQ004";
	private final static String TXT_RECS_NOT_APPL =
		"Records not applicable";
	private final static String PLATE_NO = "Plate No";

	// String 
	private String csOwnerId;

	/**
	 * FrmMultipleRecords constructor
	 */
	public FrmMultipleRecordsINQ004()
	{
		super();
		initialize();
	}

	/**
	 * FrmMultipleRecords constructor
	 * 
	 * @param aaParent JDialog
	 */
	public FrmMultipleRecordsINQ004(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmMultipleRecords constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmMultipleRecordsINQ004(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		/**
		 * Code to prevent multiple button clicks
		 */
		if (!startWorking())
		{
			return;
		}
		try
		{
			if (aaAE.getSource() == getbtnPanel().getBtnEnter())
			{
				// defect 10700 
				if (csTransCd.equals(TransCdConstant.DPSPPT))
				{
					SpecialPlatePermitData laSpclPltPrmtData =
						(
							SpecialPlatePermitData) cvSpclPltPrmtData
								.elementAt(
							gettblMultRecs().getSelectedRow());

					RTSDate laExpDate =
						new RTSDate(
							RTSDate.YYYYMMDD,
							laSpclPltPrmtData.getExpDate());

					if (laExpDate.getAMDate()
						< new RTSDate().getAMDate())
					{
						new RTSException(
							ErrorsConstant
								.ERR_NUM_EXPIRED_INSIGNIA)
								.displayError(
							this);
					}
					else
					{

						String lsRegPltNo =
							laSpclPltPrmtData.getRegPltNo();
						String lsPltDesc =
							ItemCodesCache.getItmCdDesc(
								laSpclPltPrmtData.getRegPltCd());
						lsRegPltNo =
							UtilityMethods.isEmpty(lsPltDesc)
								? lsRegPltNo
								: lsPltDesc + " " + lsRegPltNo;

						String lsMsg =
							"Do you wish to reprint Insignia "
								+ "for "
								+ lsRegPltNo
								+ "?";

						RTSException leRTSEx =
							new RTSException(
								RTSException.CTL001,
								lsMsg,
								ScreenConstant.CTL001_FRM_TITLE);
						int liResponse = leRTSEx.displayError(this);
						if (liResponse != RTSException.NO)
						{
							CompleteTransactionData laCTData =
								new CompleteTransactionData();
							laCTData.setTransCode(csTransCd);
							laCTData.setSpclPltPrmtData(
								laSpclPltPrmtData);
							getController().processData(
								VCMultipleRecordsINQ004.ADD_TRANS,
								laCTData);
						}
					}
				}
				else
				{
					// end defect 10700 
					GeneralSearchData laSearchData =
						new GeneralSearchData();

					if (getchkRecrdsNotApplcble().isSelected())
					{
						// defect 10491 
						if (cbPrmtAppl)
						{
							PermitData laPrmtData = new PermitData();
							String lsVIN = new String();

							// Note that VIN is #2 column if Permit or 
							// Vehicle Data  
							lsVIN =
								((String) gettblMultRecs()
									.getModel()
									.getValueAt(
										gettblMultRecs()
											.getSelectedRow(),
										CommonConstant
											.INQ004PRMT_COL_VIN));

							laPrmtData.setVin(lsVIN);

							getController().processData(
								VCMultipleRecordsINQ004.MRG005,
								laPrmtData);
						}
						else
						{
							// end defect 10491 
							caVehicleInfo.setNoMFRecs(0);
							MFVehicleData laMFData =
								new MFVehicleData();
							laMFData.setOwnerData(new OwnerData());
							laMFData.setRegData(new RegistrationData());
							laMFData.setVctSalvage(
								new java.util.Vector());
							laMFData.getVctSalvage().add(
								new SalvageData());
							laMFData.setTitleData(new TitleData());
							laMFData.setVehicleData(new VehicleData());
							caVehicleInfo.setMfVehicleData(laMFData);

							String lsVIN =
								((String) gettblMultRecs()
									.getModel()
									.getValueAt(
										gettblMultRecs()
											.getSelectedRow(),
										2));
							caVehicleInfo
								.getMfVehicleData()
								.getVehicleData()
								.setVin(
								lsVIN);

							// defect 10598 
							getController()
								.processData(
									VCMultipleRecordsINQ004.ENTER,
							// SINGLE_REC,
							caVehicleInfo);
							// end defect 10598 
						}
					}
					// defect 10491 
					else if (
						gettblMultRecs().getModel()
							== caTableMultPrmtRecs)
					{
						Vector lvPartial =
							caPrmtData.getPartialPrmtDataList();

						int li30DayCount = 0;
						boolean lbContinue = true;

						if (cbPrmtAppl)
						{
							for (int i = 0; i < lvPartial.size(); i++)
							{
								MFPartialPermitData laMFPartialPrmtData =
									(
										MFPartialPermitData) lvPartial
											.elementAt(
										i);
								String lsItmCd =
									laMFPartialPrmtData.getItmCd();

								if (lsItmCd
									.equals(
										MiscellaneousRegConstant
											.ITMCD_30PT)
									|| lsItmCd.equals(
										MiscellaneousRegConstant
											.ITMCD_30MCPT))
								{
									li30DayCount++;
								}
							}
							caPrmtData.setNo30DayPrmts(li30DayCount);
						}
						// defect 10844 
						else if (
							csTransCd.equals(TransCdConstant.MODPT)
								&& caPrmtData.getNoMFRecs() == 1
								&& caPrmtData.isIssuedByBulkPrmtVendor())
						{
							new RTSException(
								ErrorsConstant
									.ERR_NUM_CANNOT_MODIFY_VENDOR_ISSUED)
									.displayError(
								this);
							lbContinue = false;
						}
						else if (
							csTransCd.equals(TransCdConstant.PRMDUP)
								|| csTransCd.equals(
									TransCdConstant.MODPT))
						{
							// end defect 10844 

							int liSelectedRow =
								gettblMultRecs().getSelectedRow();

							MFPartialPermitData laMFPartialPrmtData =
								(MFPartialPermitData) lvPartial.get(
									liSelectedRow);

							if (laMFPartialPrmtData.isExpired())
							{
								RTSDate laDate =
									laMFPartialPrmtData
										.getRTSExpDateTime();

								String lsAppend =
									" (EXPIRED "
										+ laDate.toString()
										+ " "
										+ laDate.getTimeSS()
										+ ")";

								new RTSException(
									ErrorsConstant
										.ERR_NUM_PERMIT_HAS_EXPIRED,
									new String[] {
										 lsAppend }).displayError(
									this);
								lbContinue = false;
							}
						}
						if (lbContinue)
						{
							if (caPrmtData.getNoMFRecs() == 1)
							{
								getController().processData(
									VCMultipleRecordsINQ004.MRG005,
									caPrmtData);
							}
							else
							{
								int liSelectedRow =
									gettblMultRecs().getSelectedRow();

								MFPartialPermitData laMFPartialPrmtData =
									(
										MFPartialPermitData) lvPartial
											.get(
										liSelectedRow);

								String lsPrmtIssuanceId =
									laMFPartialPrmtData
										.getPrmtIssuanceId();
								laSearchData.setKey1(
									CommonConstant.PRMT_PRMTID);
								laSearchData.setKey2(lsPrmtIssuanceId);
								laSearchData.setKey3(
									getController().getTransCode());
								laSearchData.setIntKey3(li30DayCount);
								laSearchData.setIntKey4(
									caPrmtData.getChrgFeeIndi());
								getController().processData(
									VCMultipleRecordsINQ004
										.SINGLE_PRMT_INQ,
									laSearchData);
							}
						}
					}
					// end defect 10491 
					else
					{
						if (gettblMultRecs().getModel()
							== caTableMultRegRecs)
						{
							String lsExpMo =
								((String) gettblMultRecs()
									.getModel()
									.getValueAt(
										gettblMultRecs()
											.getSelectedRow(),
										4));
							String lsExpYr =
								((String) gettblMultRecs()
									.getModel()
									.getValueAt(
										gettblMultRecs()
											.getSelectedRow(),
										5));
							for (int i = 0;
								i
									< caVehicleInfo
										.getPartialDataList()
										.size();
								i++)
							{
								caRegData =
									(RegistrationData) caVehicleInfo
										.getPartialDataList()
										.get(
										i);
								if (Integer
									.toString(caRegData.getRegExpMo())
									.equals(lsExpMo)
									&& Integer.toString(
										caRegData
											.getRegExpYr())
											.equals(
										lsExpYr))
								{
									// copy full information over to partial 
									// before copying back in.
									caRegData.setExmptIndi(
										caVehicleInfo
											.getMfVehicleData()
											.getRegData()
											.getExmptIndi());

									caRegData.setClaimComptCntyNo(
										caVehicleInfo
											.getMfVehicleData()
											.getRegData()
											.getClaimComptCntyNo());
									caRegData.setEmissionSourceCd(
										caVehicleInfo
											.getMfVehicleData()
											.getRegData()
											.getEmissionSourceCd());
									// defect 9691
									// Set the Insurance Verification result
									caRegData.setInsuranceVerified(
										caVehicleInfo
											.getMfVehicleData()
											.getRegData()
											.getInsuranceVerified());
									// end defect 9691								

									// Set RegistrationData
									caVehicleInfo
										.getMfVehicleData()
										.setRegData(
										caRegData);

									// defect 9086
									// Set Special Plates Data
									if (caRegData.getSpclRegId() > 0)
									{
										caVehicleInfo
											.getMfVehicleData()
											.setSpclPltRegisData(
											getSpclPltRegisData(caRegData));
									}
									// end defect 9086		
									break;
								}
							}
							caUtil.validateVehWts(caVehicleInfo);

							// defect 10598 
							caVehicleInfo.setNoMFRecs(1);

							getController()
								.processData(
									AbstractViewController.ENTER,
							//	SINGLE_REC,
							caVehicleInfo);
							// end defect 10598 
						}
						else
						{
							// defect 9086
							//	Check if SpclPlts. Get SpclRegId
							if (gettblMultRecs().getModel()
								== caTableMultSpclPltRecs)
							{
								//	Get first name of cloumn
								//	Plate No is unique to Special Plates
								if (gettblMultRecs()
									.getModel()
									.getColumnName(0)
									.equals(PLATE_NO))
								{
									int liSpclRegId =
										(
											(int)
												(
													(MFPartialSpclPltData) caVehicleInfo
											.getPartialSpclPltsDataList()
											.get(
												gettblMultRecs()
													.getSelectedRow()))
											.getSpclRegId());
									laSearchData.setKey1(
										CommonConstant.SPCL_REG_ID);
									laSearchData.setKey2(
										String.valueOf(liSpclRegId));
									// defect 9085
									laSearchData.setKey3(
										getController().getTransCode());
									// Search only Special Plates Regis Data
									laSearchData.setIntKey2(
										CommonConstant
											.SEARCH_SPECIAL_PLATES);
									// Save Print Options
									laSearchData.setIntKey1(
										caVehicleInfo
											.getPrintOptions());
									// end defect 9086 
									getController().processData(
										AbstractViewController.ENTER,
										laSearchData);
								}
							}
							else
							{
								// Permit Transaction; Search by VIN; 
								//   No corresponding Permit records 
								if (cbPrmtAppl
									&& caVehicleInfo.getNoMFRecs() == 1)
								{
									//defect 11228
									if (caVehicleInfo
											.getMfVehicleData()
											.getTitleData()
											.getExportIndi() == 1)
									{
										RTSException leRTSEx1 = new RTSException(
												ErrorsConstant.ERR_NUM_ACTION_INVALID_EXPORT); 
										leRTSEx1.displayError(this); 
										return;
									}
									//defect 11228
									
									if (verifyIndicators())
									{
										PermitData laPrmtData =
											new PermitData(caVehicleInfo);

										getController().processData(
											VCMultipleRecordsINQ004
												.MRG005,
											laPrmtData);
									}
								}
								else
								{
									// get DocNo
									String lsDocNo =
										((String) gettblMultRecs()
											.getModel()
											.getValueAt(
												gettblMultRecs()
													.getSelectedRow(),
												5));
									laSearchData.setKey1(
										CommonConstant.DOC_NO);
									laSearchData.setKey2(lsDocNo);
									// defect 8213
									// Set the transcode for MFAccess.
									laSearchData.setKey3(
										getController().getTransCode());
									// end defect 8213
									if (csOwnerId != null)
									{
										laSearchData.setKey4(csOwnerId);
									}
									laSearchData.setIntKey1(
										caVehicleInfo
											.getPrintOptions());

									getController().processData(
										AbstractViewController.ENTER,
										laSearchData);
								}
							}
						}
					} // end defect 9086
				}
			}
			// end defect 9086
			else if (aaAE.getSource() == getbtnPanel().getBtnCancel())
			{
				// defect 9211
				//	Return a RTSException for Cancel. DTA will handle
				//	 the process in FrmDealerTitleTransactionDTA008.setData().
				//	All other events will receive null as their return 
				//	object.
				getController().processData(
					AbstractViewController.CANCEL,
					new RTSException());
				// end defect 9211
			}
			else if (aaAE.getSource() == getbtnPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INQ004);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the btnPanel property value
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getbtnPanel()
	{
		if (ivjbtnPanel == null)
		{
			try
			{
				ivjbtnPanel = new ButtonPanel();
				// defect 10491 
				ivjbtnPanel.setBounds(225, 226, 212, 41);
				// end defect 10491 
				ivjbtnPanel.setName("btnPanel");
				ivjbtnPanel.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjbtnPanel.addActionListener(this);
				ivjbtnPanel.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnPanel;
	}

	/**
	 * Return the chkRecrdsNotApplcble property value
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkRecrdsNotApplcble()
	{
		if (ivjchkRecrdsNotApplcble == null)
		{
			try
			{
				ivjchkRecrdsNotApplcble = new JCheckBox();
				// defect 10491 
				ivjchkRecrdsNotApplcble.setBounds(241, 197, 187, 24);
				// end defect 10491 
				ivjchkRecrdsNotApplcble.setName("chkRecrdsNotApplcble");
				ivjchkRecrdsNotApplcble.setMnemonic('n');
				ivjchkRecrdsNotApplcble.setText(TXT_RECS_NOT_APPL);
				ivjchkRecrdsNotApplcble.setHorizontalAlignment(
					SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkRecrdsNotApplcble;
	}

	/**
	 * Return the FrmMultipleRecordsINQ004ContentPane1 property value
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmMultipleRecordsINQ004ContentPane1()
	{
		if (ivjFrmMultipleRecordsINQ004ContentPane1 == null)
		{
			try
			{
				ivjFrmMultipleRecordsINQ004ContentPane1 = new JPanel();

				ivjFrmMultipleRecordsINQ004ContentPane1.setName(
					"FrmMultipleRecordsINQ004ContentPane1");

				// defect 10491 
				// From GridBagLayout to null  
				ivjFrmMultipleRecordsINQ004ContentPane1.setLayout(null);
				// end defect 10491 

				ivjFrmMultipleRecordsINQ004ContentPane1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmMultipleRecordsINQ004ContentPane1.setMinimumSize(
					new Dimension(0, 0));

				ivjFrmMultipleRecordsINQ004ContentPane1.add(
					getJScrollPane1(),
					null);
				ivjFrmMultipleRecordsINQ004ContentPane1.add(
					getchkRecrdsNotApplcble(),
					null);
				ivjFrmMultipleRecordsINQ004ContentPane1.add(
					getbtnPanel(),
					null);

				// defect 10491 
				ivjFrmMultipleRecordsINQ004ContentPane1.add(
					getlblRecords(),
					null);
				// end defect 10491 
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmMultipleRecordsINQ004ContentPane1;
	}

	/**
	 * Return the JScrollPane1 property value
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				getJScrollPane1().setViewportView(gettblMultRecs());
				// user code begin {1}
				ivjJScrollPane1.setBounds(20, 36, 616, 148);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * This method initializes ivjlblRecords
	 * 
	 * @return JLabel
	 */
	private JLabel getlblRecords()
	{
		if (ivjlblRecords == null)
		{
			ivjlblRecords = new JLabel();
			ivjlblRecords.setBounds(23, 11, 189, 20);
		}
		return ivjlblRecords;
	}

	/**
	 * Return the tblMultRecs property value
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblMultRecs()
	{
		if (ivjtblMultRecs == null)
		{
			try
			{
				ivjtblMultRecs = new RTSTable();
				ivjtblMultRecs.setName("tblMultRecs");
				getJScrollPane1().setColumnHeaderView(
					ivjtblMultRecs.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				// defect 10491 
				ivjtblMultRecs.setAutoResizeMode(
					JTable.AUTO_RESIZE_OFF);
				//javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				// end defect 10491 
				ivjtblMultRecs.setShowVerticalLines(false);
				ivjtblMultRecs.setShowHorizontalLines(false);
				ivjtblMultRecs.setIntercellSpacing(new Dimension(0, 0));
				ivjtblMultRecs.setBounds(0, 0, 200, 200);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblMultRecs;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
	}

	/**
	 * Initialize the class
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(FRM_NAME_INQ004);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(667, 301);
			setTitle(FRM_TITLE_INQ004);
			setContentPane(getFrmMultipleRecordsINQ004ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmMultipleRecordsINQ004 laFrmMultipleRecords;
			laFrmMultipleRecords = new FrmMultipleRecordsINQ004();
			laFrmMultipleRecords.setModal(true);
			laFrmMultipleRecords.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmMultipleRecords.show();
			Insets laInsets = laFrmMultipleRecords.getInsets();
			laFrmMultipleRecords.setSize(
				laFrmMultipleRecords.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmMultipleRecords.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmMultipleRecords.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		// defect 10491
		csTransCd = getController().getTransCode();
		cbPrmtAppl = csTransCd.equals(TransCdConstant.PT72);

		// defect 10844 
		boolean lbPrmtTrans =
			cbPrmtAppl
				|| csTransCd.equals(TransCdConstant.PRMDUP)
				|| csTransCd.equals(TransCdConstant.MODPT)
				|| csTransCd.equals(MiscellaneousRegConstant.PRMINQ);
		// end defect 10844 

		if (aaData != null)
		{
			if (lbPrmtTrans)
			{
				setTitle(PERMIT_TITLE);
			}
			// defect 10700 
			if (aaData instanceof Vector
				&& ((Vector) aaData).elementAt(0)
					instanceof SpecialPlatePermitData)
			{
				cvSpclPltPrmtData = (Vector) aaData;
				setTitle(SPCL_PLT_PRMT_TITLE);
				setMultSpclPltPrmtRecs();
				caTableMultSpclPltPrmtRecs.add(cvSpclPltPrmtData);
			}
			else
				// end defect 10700 
				if (aaData instanceof PermitData)
				{
					caPrmtData = (PermitData) aaData;

					if (caPrmtData.getPartialPrmtDataList().size()
						== 0)
					{
						caPrmtData.initPartialsForOne();
					}

					Vector lvPartialPrmtData =
						caPrmtData.getPartialPrmtDataList();

					setMultPrmtRecs();

					caTableMultPrmtRecs.add(lvPartialPrmtData);

					// Sort by Name  
					UtilityMethods.sort(lvPartialPrmtData);

					int liNumRecords = caPrmtData.getNoMFRecs();
					int liPartials =
						caPrmtData.getPartialPrmtDataList().size();

					if (liNumRecords != 1)
					{
						String lsLabel = "Records: ";
						String lsNumber = "" + liPartials;
						if (liPartials != liNumRecords)
						{
							String lsNumRecords = "" + liNumRecords;
							if (liNumRecords == 999)
							{
								lsNumRecords = " 999+";
							}
							lsNumber = lsNumber + " of " + lsNumRecords;
						}
						lsLabel = lsLabel + lsNumber;
						getlblRecords().setText(lsLabel);
					}
				}
				else
				{
					// end defect 10491 
					caVehicleInfo = (VehicleInquiryData) aaData;
					csOwnerId = caVehicleInfo.getOwnerId();
					Vector lvPartialData =
						caVehicleInfo.getPartialDataList();

					Vector lvPartialSpclPltsData =
						caVehicleInfo.getPartialSpclPltsDataList();

					if (lvPartialSpclPltsData != null
						&& !caVehicleInfo
							.getPartialSpclPltsDataList()
							.isEmpty()
						&& caVehicleInfo
							.getPartialSpclPltsDataList()
							.elementAt(
							0)
							instanceof MFPartialSpclPltData)
					{
						setMultPartialSpclPltsRecs();
						// defect 9086
						// Compare expiration periods
						UtilityMethods.sort(lvPartialSpclPltsData);
						// end defect 9086
						caTableMultSpclPltRecs.add(
							lvPartialSpclPltsData);

						// TODO Isn't this handled below  
						getchkRecrdsNotApplcble().setVisible(false);
						getchkRecrdsNotApplcble().setEnabled(false);
					}
					// end defect 9086

					// check for null pointer
					else if (
						lvPartialData != null
							&& !caVehicleInfo
								.getPartialDataList()
								.isEmpty()
							&& caVehicleInfo
								.getPartialDataList()
								.elementAt(
								0)
								instanceof RegistrationData)
					{
						setMultRegRecs();
						caTableMultRegRecs.add(caVehicleInfo);
					}
					else
					{
						setMultPartialRecs();
						caTableMultRecs.add(lvPartialData);
					}
				}
			gettblMultRecs().setRowSelectionInterval(0, 0);

			// defect 10491 
			// Add PT72 
			// defect 10416 
			// Remove REJCOR 
			// defect 9636, 9642
			// Remove SLVG
			String lsTransCd = getController().getTransCode();

			if (!cbPrmtAppl
				&& !lsTransCd.equals(
					TransCdConstant.TITLE)
				// && !lsTransCd.equals(TransCdConstant.REJCOR)
				&& !lsTransCd.equals(TransCdConstant.DTAORD)
				&& !lsTransCd.equals(TransCdConstant.COA)
				&& !lsTransCd.equals(
				//TransCdConstant.SLVG))
			TransCdConstant.SCOT))
			{
				// end defect 10491 
				getchkRecrdsNotApplcble().setEnabled(false);
				getchkRecrdsNotApplcble().setVisible(false);
			}
			else if (gettblMultRecs().getRowCount() == 1)
			{
				getchkRecrdsNotApplcble().setText(TXT_REC_NOT_APPL);
			}
		}
	}

	/**
	 * This method resets the table per multiple records
	 */
	public void setMultPartialRecs()
	{
		ivjtblMultRecs.setModel(new TMINQ004());
		caTableMultRecs = (TMINQ004) ivjtblMultRecs.getModel();

		TableColumn laTCYear =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(0));
		// defect 10491
		// Modify fields lengths to accommodate DocNo 
		//laTCYear.setPreferredWidth(45);
		laTCYear.setPreferredWidth(35);
		TableColumn laTCMake =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(1));
		//laTCMake.setPreferredWidth(45);
		laTCMake.setPreferredWidth(40);
		TableColumn laTCVin =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(2));
		//laTCVin.setPreferredWidth(185);
		laTCVin.setPreferredWidth(170);
		TableColumn laTCPlt =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(3));
		//laTCPlt.setPreferredWidth(70);
		laTCPlt.setPreferredWidth(65);
		TableColumn laTCOwner =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(4));
		//laTCOwner.setPreferredWidth(247);
		laTCOwner.setPreferredWidth(220);
		TableColumn laTCDocNo =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(5));
		//laTCDocNo.setPreferredWidth(0);
		laTCDocNo.setPreferredWidth(130);
		// end defect 10491 

		ivjtblMultRecs.setSelectionMode(
			ListSelectionModel.SINGLE_SELECTION);
		ivjtblMultRecs.init();
		laTCYear.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCMake.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCVin.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCPlt.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCOwner.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCDocNo.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		getJScrollPane1().setHorizontalScrollBarPolicy(
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/**
	 * This method resets the table per multiple records, 
	 *	Special Plates
	 */
	public void setMultPartialSpclPltsRecs()
	{
		ivjtblMultRecs.setModel(new TMINQ004SpclPlts());
		caTableMultSpclPltRecs =
			(TMINQ004SpclPlts) ivjtblMultRecs.getModel();
		ivjtblMultRecs.setAutoResizeMode(
			javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
		TableColumn laTCRegPltNo =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(0));
		laTCRegPltNo.setPreferredWidth(60);
		TableColumn laTCRegExpMo =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(1));
		laTCRegExpMo.setPreferredWidth(50);
		TableColumn laTCRegExpYr =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(2));
		laTCRegExpYr.setPreferredWidth(50);
		TableColumn laTCOwnrName =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(3));
		laTCOwnrName.setPreferredWidth(200);
		TableColumn laTCRegPltCd =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(4));
		laTCRegPltCd.setPreferredWidth(90);
		TableColumn laTCSpclDocNo =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(5));
		laTCSpclDocNo.setPreferredWidth(121);
		ivjtblMultRecs.setSelectionMode(
			ListSelectionModel.SINGLE_SELECTION);

		ivjtblMultRecs.init();
		laTCRegPltNo.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCRegExpMo.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.CENTER));
		laTCRegExpYr.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.CENTER));
		laTCOwnrName.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCRegPltCd.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.CENTER));
		laTCOwnrName.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		getJScrollPane1().setHorizontalScrollBarPolicy(
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/**
	 * This method resets the table per Registration multiple records
	 */
	public void setMultRegRecs()
	{
		ivjtblMultRecs.setModel(new TMINQ004REG());
		caTableMultRegRecs = (TMINQ004REG) ivjtblMultRecs.getModel();

		TableColumn laTCYear =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(0));
		laTCYear.setPreferredWidth(35);
		TableColumn laTCMake =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(1));
		laTCMake.setPreferredWidth(40);
		TableColumn laTCVin =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(2));
		laTCVin.setPreferredWidth(185);
		TableColumn laTCPlate =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(3));
		laTCPlate.setPreferredWidth(60);
		TableColumn laTCExpMo =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(4));
		laTCExpMo.setPreferredWidth(50);
		TableColumn laTCExpYr =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(5));
		laTCExpYr.setPreferredWidth(50);
		TableColumn laTCOwner =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(6));
		laTCOwner.setPreferredWidth(250);
		ivjtblMultRecs.setSelectionMode(
			ListSelectionModel.SINGLE_SELECTION);
		ivjtblMultRecs.init();
		laTCYear.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCMake.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCVin.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCPlate.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCExpMo.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCExpYr.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCOwner.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		getJScrollPane1().setHorizontalScrollBarPolicy(
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/**
	 * This method resets the table per Permit multiple records
	 */
	public void setMultPrmtRecs()
	{
		ivjtblMultRecs.setBounds(0, 0, 200, 400);
		ivjtblMultRecs.setModel(new TMINQ004Permit());

		caTableMultPrmtRecs =
			(TMINQ004Permit) ivjtblMultRecs.getModel();

		// VehModlYr 
		TableColumn laTCYear =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(0));
		laTCYear.setPreferredWidth(35);

		// VehMk 
		TableColumn laTCMake =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(1));
		laTCMake.setPreferredWidth(40);

		// VIN		
		TableColumn laTCVin =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(2));
		laTCVin.setPreferredWidth(170);

		// PERMIT NO 
		TableColumn laTCPermitNo =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(3));
		laTCPermitNo.setPreferredWidth(60);

		// ItmCd 
		TableColumn laTCItmCd =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(4));
		laTCItmCd.setPreferredWidth(55);

		// Exp Mo/Yr 
		TableColumn laTCExpMoYr =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(5));
		laTCExpMoYr.setPreferredWidth(70);

		// Owner 
		TableColumn laTCOwner =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(6));
		laTCOwner.setPreferredWidth(250);

		ivjtblMultRecs.setSelectionMode(
			ListSelectionModel.SINGLE_SELECTION);

		ivjtblMultRecs.init();

		laTCYear.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCMake.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCVin.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCPermitNo.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCItmCd.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCExpMoYr.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCOwner.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));

		getJScrollPane1().setHorizontalScrollBarPolicy(
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/**
	 * This method resets the table per Permit multiple records
	 */
	public void setMultSpclPltPrmtRecs()
	{
		ivjtblMultRecs.setBounds(0, 0, 200, 400);
		ivjtblMultRecs.setModel(new TMINQ004SpclPltPrmt());

		caTableMultSpclPltPrmtRecs =
			(TMINQ004SpclPltPrmt) ivjtblMultRecs.getModel();

		// PlateNo 
		TableColumn laTCPltNo =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(0));
		laTCPltNo.setPreferredWidth(70);

		// Exp Date 
		TableColumn laTCExpDate =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(1));
		laTCExpDate.setPreferredWidth(70);

		// VehModlYr 
		TableColumn laTCYear =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(2));
		laTCYear.setPreferredWidth(70);

		// VehMk 
		TableColumn laTCMake =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(3));
		laTCMake.setPreferredWidth(70);

		// VIN
		TableColumn laTCVin =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(4));
		laTCVin.setPreferredWidth(170);

		// OWNER NAME
		TableColumn laOwnrName =
			ivjtblMultRecs.getColumn(ivjtblMultRecs.getColumnName(5));
		laOwnrName.setPreferredWidth(300);

		ivjtblMultRecs.setSelectionMode(
			ListSelectionModel.SINGLE_SELECTION);

		ivjtblMultRecs.init();

		laTCYear.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCMake.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCVin.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCExpDate.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		laTCPltNo.setCellRenderer(
			ivjtblMultRecs.setColumnAlignment(RTSTable.LEFT));
		getJScrollPane1().setHorizontalScrollBarPolicy(
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/**
	 * Find the matching Special Plates Regis Data to the Regis file
	 * 
	 * @param aaRegisData	RegistrationData
	 * @return SpecialPlatesRegisData
	 */
	private SpecialPlatesRegisData getSpclPltRegisData(RegistrationData aaRegisData)
	{
		int liSpclRegId = aaRegisData.getSpclRegId();
		SpecialPlatesRegisData laSpclPltRegisData =
			new SpecialPlatesRegisData();

		// Find SpclRegId  match.
		// Save object
		for (int liIndex = 0;
			liIndex < caVehicleInfo.getPartialSpclPltsDataList().size();
			liIndex++)
		{
			if (caVehicleInfo.getPartialSpclPltsDataList().get(liIndex)
				instanceof SpecialPlatesRegisData)
			{
				laSpclPltRegisData =
					(SpecialPlatesRegisData) caVehicleInfo
						.getPartialSpclPltsDataList()
						.get(
						liIndex);
				if (laSpclPltRegisData.getSpclRegId() == liSpclRegId)
				{
					break;
				}
			}

		}
		return laSpclPltRegisData;
	}

	/**
	 * Verify Indicators
	 *
	 * @return boolean
	 */
	private boolean verifyIndicators()
	{
		boolean lbRet = true;

		if (caVehicleInfo.hasHardStops(getController().getTransCode()))
		{
			if (!caVehicleInfo.hasAuthCode())
			{
				getController().processData(
					VCMultipleRecordsINQ004.VTR_AUTH,
					caVehicleInfo);

				lbRet = caVehicleInfo.hasAuthCode();
			}
		}
		return lbRet;
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"