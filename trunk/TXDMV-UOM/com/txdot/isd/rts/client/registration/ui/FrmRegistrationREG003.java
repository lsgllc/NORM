package com.txdot.isd.rts.client.registration.ui;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.registration.business.RegistrationClientBusiness;
import com.txdot.isd.rts.client.registration.business.RegistrationClientUtilityMethods;
import com.txdot.isd.rts.client.registration.business.RegistrationSpecialExemptions;
import com.txdot.isd.rts.client.registration.business.RegistrationVerify;
import com.txdot.isd.rts.client.title.ui.TitleClientUtilityMethods;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmRegistrationREG003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik	 	09/19/2001	Modified per first code review document
 * J Kwik		04/18/2002	Global change for startWorking() and 
 *							doneWorking()
 * J Kwik		04/25/2002	lengthen name fields
 *							defect 3672 
 * MAbs			05/14/2002	Making tonnage field larger CQU100003903
 * J Kwik		05/21/2002	If headquarters display message whether to 
 *							complete trans and don't display PMT004
 *							defect 4008 
 * MAbs			05/31/2002	Fixed NullPointer in Disabled when MF down 
 *							defect 4183
 * RArredondo	07/01/2002	Change font size to dialog.
 *							defect 4336
 * J Rue		08/22/2002	Lengthen the Body VIN display field.
 *							defect 4652 
 * R Taylor		10/23/2002  Set RenwlMailRtrnIndi=0 for Renewal trans
 *							in CompleteRenwlTrans()  
 *							defect 4915 
 * B Arredondo	12/16/2002	Made changes for the 
 *							user help guide so had to make changes
 *							in actionPerformed().
 *							defect 5147 
 * B Brown     	01/07/2003	changed this mesage in validateDisabldPlt():
 *                          "Vehicle tonnage cannot be greater than two 
 *							tons for Disabled PersonOld or Disabled Veteran
 *							Plates" from a system error level message to 
 *							RTSException.FAILURE_MESSAGE.
 *							defect 5165
 * K Harrell    04/29/2003  HQ Replacement for exempt vehicle 
 *							will process
 *                          modify completeReplTrans()
 *							defect 6001 
 * K Harrell    05/01/2003  HQ VehInq, print option 2 should not go to 
 *							INQ008 screen
 *           				modify actionPerformed()
 *							defect 6057 
 * K Harrell    05/07/2003  HQ Replacement will process non-special 
 *							plate replacements.
 *        					modify actionPerformed()
 *							defect 6097 
 * K Harrell	06/18/2003  5.1.3/5.2.0 Merge/Format
 * J Zwiener	07/20/2004	Weight area cluttered
 *							modify REG003 via VCE
 *							defect 7146
 * J Zwiener	08/20/2004	Default text ("Registration Class & Type)
 *							appearing when no associated regis rec.
 *							modify REG003 via VCE - set Text prop to " "
 *							defect 7468 Ver 5.2.1
 * K Harrell	01/03/2005	For RegClassCd 25, 26, validate weight on
 *							Enter.
 *							Reorganize declarations
 *							JavaDoc/Formatting/Variable Name Cleanup 
 *							add import ...title.ui.*;
 *							add validateEmptyWt()
 *							modify actionPerformed()
 *							defect 5657  Ver 5.2.2 
 * B Hargrove	06/23/2005	Modify code for move to Java 1.4.
 * 							Bring code to standards. Remove unused
 * 							variables and methods.
 *  						modify keyPressed()  (arrow key handling is
 * 							done in ButtonPanel).
 * 							delete implements KeyListener
 *							defect 7894 Ver 5.2.3
 * B Hargrove	07/19/2005	Refactor\Move 
 * 							RegistrationClientUtilityMethods class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.client.reg.business.
 *							defect 7894 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * B Hargrove	12/07/2005	Modify with visual editor to enlarge buttons 
 * 							for Lien\Owner Addr\Addl Info so that all of
 * 							cursor is visible. Make display of Indidescs
 * 							so top of next line of text does not show. 
 * 							modify buttons in visual editor,
 * 							getlstIndiDescription()						 
 * 							defect 7264 Ver 5.2.3
 * J Zwiener	12/20/2005	Do not show msg rts735 "You must go to
 *							additional information to select reason for
 *							expired registration." when regclass is
 *							Seasonal Ag as the reason is defaulted to
 *							valid reason and disabled.
 *							modify validateRegExpired()
 *							defect 8448  Ver 5.2.2 Fix 7
 * J Zwiener	12/20/2005	Not setting RegExpiredReason when Season Ag
 *							which causes incorrect fees
 *							modify validateRegExpired()
 *							defect 8474  Ver 5.2.2 Fix 7
 * Jeff S.		06/22/2006	Used screen constant for CTL001 Title.
 * 							remove TITLE_CTL001, MSG_CONFIRM_ACT
 * 							modify actionPerformed(), completeTrans()
 * 							defect 8756 Ver 5.2.3
 * Min Wang		10/12/2006	Modify to handle regpltage getter.
 * 							modify setData()
 * 							defect 8901 Ver Exempts
 * K Harrell	10/16/2006	Send copied VehInqData Object to REG039
 * 							add getVehInqDataCopy() 
 * 							delete getBuilderData() 
 * 							modify actionPerformed()
 * 							defect 6874 Ver Exempts
 * K Harrell	10/19/2006	Expand test for Invalid Weight
 * 							modify validateEmptyWt()
 * 							defect 8986  Ver Exempts
 * K Harrell	11/01/2006	delete private set methods for class objects
 * 							delete setMFVehicleData(),
 *							 setOrigVehInqData(), setOwnerData(),
 *							 setRegistrationData(),setRegValidData(),
 *							 setTitleData(),setVehicleData(),
 *							 setVehicleInquiryData()
 *							defect 8900 Ver Exempts
 * K Harrell	02/05/2007 	Use PlateTypeCache vs. 
 * 								RegistrationRenewalsCache
 * 							modify prepInv, handleReplAnnualPlt(),
 * 							 completeReplTrans()
 * 							defect 9085 Ver Special Plates 
 * T Pederson	02/12/2007	Added organization name and Special Plate 
 * 							Information button to screen.  
 * 							modify setData(), actionPerformed(). 
 * 							defect 9123 Ver Special Plates
 * K Harrell	02/21/2007	If SpecialPlate attached to vehicle, use
 * 							SpecialPlate PltBirthData vs. Registration
 * 							Data
 * 							defect 9085 Ver Special Plate
 * J Rue		02/28/2007	Check for SpclPlrRegisData for null to 
 * 							enable SpecPltInformation button
 * 							defect 9086 Ver Special Plates
 * K Harrell	03/03/2007	Consolidate SpecialPlate logic.
 * 							modify setData()
 *  						defects 9085/9086/9123 Ver Special Plates
 * K Harrell	03/04/2007	Add code necessary to process Annual Special
 * 							Plates 
 * 							modify setData(), handleReplAnnualPlt(),
 * 							 completeReplTrans(), actionPerformed() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/27/2007	use SystemProperty.isHQ(), 
 * 							SystemProperty.isCounty() throughout 
 * 							application.
 * 							Organization Name not showing if 
 * 							 OrgNo blank
 * 							modify setData() 
 * 							defect 9085 Ver Special Plates  
 * J Rue		03/29/2007	Populate RegPltNo with SP-RegPltNo
 * 							modify setData()
 * 							defect 9086 Ver Special Plates
 * K Harrell	04/09/2007	Reset Special Plates button if not SpclPlt
 * 							modify setData()
 * 							defect 9086 Ver Special Plates   
 * B Hargrove	04/16/2007	Do not set plate inventory if Customer
 * 							Supplied was checked on frame KEY002.
 * 							modify prepInv()
 * 							defect 9126 Ver Special Plates 
 * K Harrell	04/20/2007	Include test of valid Special Plate 
 * 							modify validateClassPltStkrCombo() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/27/2007	Use CommonConstant.TXT_COMPLETE_TRANS_QUESTION
 * 							Added code for assigning Manufacture for 
 * 							Special Plate. 
 * 							modify completeTrans(), prepInv()
 * 							defect 9085 Ver Special Plates
 * B Hargrove	05/07/2007	For Special Plate, set the 'charge spcl plt
 * 							fee' indi. Renewal may not go through SPL002
 * 							so indi would never get set.
 * 							modify setData()
 * 							defect 9126 Ver Special Plates
 * K Harrell	05/21/2007	Check for NeedsProgramCd = "L" vs. 
 * 							PltOwnrshpCd = "E" 
 * 							modify prepInv()
 * 							defect 9084 Ver Special Plates
 * K Harrell	05/29/2007	Update to handle removal of customer supplied	
 * 							Add SpclPltRegisData to objects
 * 							copied prior to REG039. 
 * 						 	modify prepInv(), getVehInqDataCopy() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/31/2007	Increase field size for organization to 	
 * 							accommodate "CONSERVATION-LGMOUTHBASS" 
 * 							modify via Visual Editor
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/07/2007	Augment handling of cursor movement keys to 
 * 							accommodate new "Special Plate Information" 
 * 							button.
 * 							modify keyPressed()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/04/2007	Add Inventory if Renewing Annual Plt.
 * 							modify prepInv()
 * 							defect 9223 Ver Special Plates   
 * K Harrell	10/04/2007	Do not set MfgIndi if Current and Annual 
 * 							modify prepInv()
 * 					 		defect 9223 Ver Special Plates
 * K Harrell	10/14/2007	Do not set MfgIndi if Sunsetted
 * 							modify prepInv()
 * 							defect 9299 Ver Special Plates 2 
 * Min Wang		12/04/2007	Add FRVP Insurance Panel
 * 							modify setData()
 * 							defect 9459 Ver FRVP
 * Min Wang		04/16/2008	add getlblInsuranceVerified(),
 * 								getpnlInsurance()
 *							modify getFrmRegistrationREG003ControlPanel()
 *							defect 9459 Ver FRVP
 * Min Wang		04/22/2008	resize the panels for Owner info by Visual
 * 							editor.
 * 							defect 9459 Ver FRVP 
 * B Hargrove	07/15/2008 	Edit for cannot Renew expired Vendor Plate
 * 							(expired as of start of new regis period).
 * 							Show warning message (process can continue)
 * 							for Modify expired Vendor Plate (expired 
 * 							as of start of today).
 * 							add validateVendorPlt()
 * 							modify actionPerformed() 
 * 							defect 9689 Ver MyPlates_POS
 * K Harrell	09/05/2008	Consolidate Soft Stop Processing.
 * 							Use RTSInputField constants for input types.
 * 							Add'l class cleanup. 
 * 							add isOutOfCounty() 
 * 							modify procsStops(), completeExchTrans(),
 * 							 completePawtTrans(), completeRenwlTrans() 
 * 							defect 7860 Ver Defect_POS_B 
 * K Harrell	01/07/2009 	Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify prepInv(), validateVendorPlt()  
 *        					defect 9864 Ver Defect_POS_D
 * K Harrell	01/08/2009	Remove reference to RegisData.setOrgNo() 
 * 							modify setData() 
 * 							defect 9912 Ver Defect_POS_D
 * K Harrell	02/18/2009	Present msg if SpecialPlate Address is to 
 * 							be changed. 
 * 							modify actionPerformed()
 * 							defect 9893 Ver Defect_POS_D 
 * K Harrell	02/26/2009	Modify screen to increase size of Remarks 
 * 							panel via Visual Composition. Use 
 * 							CommonConstant.FONT_JLIST constant to set 
 * 							remarks font.   
 * 							modify getlstIndiDescription(), 
 * 							  windowActivated() 
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	03/09/2009	Use new TitleData method to determine if  
 * 							associated Lien  
 * 							modify setData()
 * 							defect 9971 Ver Defect_POS_E 
 * K Harrell	04/01/2009	Use CommonConstant.MAX_INDI_NO_SCROLL
 * 							Class cleanup. 
 * 							modify windowActivated()
 * 							defect 9971 Ver Defect_POS_E  
 * K Harrell	07/01/2009	Implement new OwnerData.  Sorted members.
 * 							modify actionPerformed(),setData(),
 * 							 isOutOfCounty()  
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	08/06/2009	Cleanup methods called from
 * 							add validateRegEvents(), validateOwnerAddress(), 
 * 							setResComptCntyNo()  
 * 							delete ciMFDown 
 * 							modify completeAddrChngTrans(),
 * 							 completeCorrtnTrans(), completeExchTrans(),
 * 							 completeRenwlTrans(), completeReplTrans(),
 * 							 completeTrans()  
 * 							 actionPerformed(), validateClassPltStkrCombo(),
 * 							 validateDisabldPlt(), validateHvyVehUseTax(),
 * 							 validateMinGrossWt(), validateRegExpired(), 
 * 							 validateVendorPlt(), validateRegEvents(), 
 * 							 validateEmptyWt()  
 * 							defect 10127 Ver Defect_POS_F 
 * K Harrell	08/10/2009	implement isValidAddress()  
 * 							modify validateOwnerAddressInfo() 
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	09/09/2009	Move assignment of RescomptCntyNo prior to 
 * 							validatation. Check Soft Stop in Exchange 
 * 							modify completeExchTrans()  
 * 							  completeReplTrans(), completePawtTrans()
 * 							defect 10127 Defect_POS_F
 * K Harrell	12/29/2009	Force to REG033 if invalid MF characters 
 * 							modify validateOwnerAddressInfo()
 * 							defect 10299 Ver Defect_POS_H
 * B Hargrove	01/06/2010  check for null SpecialPlateRegisData in 
 * 							validateVendorPlt()	so we don't get 
 * 							nullpointer on unlinked or purged VP. 
 * 							see also: RegistrationVerify verifyValidSpclPlt()
 * 							modify validateVendorPlt()
 * 							defect 10327 Ver Defect_POS_H 
 * B Hargrove	01/21/2010  Add check for 'is reg expired'; if so,
 * 							use today's date to set effective date in
 * 							validate Vendor Plate. 
 * 							modify validateVendorPlt()
 * 							defect 10331 Ver Defect_POS_H 
 * B Hargrove	04/01/2010  New Vendor Plate rules: you can Renew with
 * 							expired VP. They will be pro-rated (at least)  
 * 							up to the new Reg expiration. 
 * 							modify validateRegEvents(
 * 							deprecate validateVendorPlt()
 * 							defect 10357 Ver Rel 6.4.0
 * K Harrell	04/07/2010	Validate Vehicle Empty Weight on REG003
 * 							as only validates on REG039 if select 
 * 							"Change Weight". 
 * 							add validateVehWeight() 
 * 							modify completeRenwlTrans(), 
 * 							  completeExchTrans() 
 * 							defect 10440 Ver POS_640 
 * K Harrell	06/03/2010	Only validate if not Fixed Weight 
 * 							modify validateVehWeight() 
 * 							defect 10499 Ver POS_640 
 * K Harrell	07/11/2010	Force to REG033 if EMail address is invalid
 * 							or if EMailRenwlCd = 1 and EMail address is 
 * 							empty
 * 							modify validateOwnerAddressInfo()
 * 							defect 10508 Ver 6.5.0 
 * Min Wang 	07/22/2010	Force to REG039 if verified tow truck 
 * 							certificate is not selected.
 * 							add validateTowTruckCert()
 * 							modify validateRegEvents()
 * 							defect 10007 Ver 6.5.0
 * K Harrell	07/28/2010	Ensure Indicators reevaluated if remove/add
 * 							 Special Plate.  At this time, "Election 
 * 							 Pending Indi" is the only Indicator for 
 * 							 SpecialPlateRegisData.  
 * 							add cbFirstIndiEval  
 * 							modify getIndicators()
 * 							defect 10507 Ver 6.5.0 
 * Min Wang		08/02/2010  Verify Tow Truck Certificate
 * 							add validateTowTruckCert()
 * 							modify validateRegEvents()
 * 							defect 10007 Ver 6.5.0
 * B Hargrove	10/11/2010  For Token Trailer, do NOT print sticker.
 * 							Add check for Token Trailer (33): it's not 
 * 							an annual plt but still do NOT set 
 * 							liNewStkrReplIndi = 1.
 * 							modify prepInv()
 * 							defect 10623 Ver Rel 6.6.0
 * B Hargrove 	10/29/2010  Token Trailer now does not print sticker.
 * 							Comment out all changes. They want to store 
 * 							inv detail, just don't print sticker.
 * 							modify prepInv()
 * 							defect 10623 Ver 6.6.0
 * T Pederson	06/10/2011	Added vehicle color drop down boxes 
 * 							add ivjcomboMajorColor, ivjcomboMinorColor,
 * 							ivjstcLblColorMajor, ivjstcLblColorMinor,
 * 							MAJORCOLOR, MINORCOLOR, 
 * 							MSG_VEHICLE_COLOR_NOT_ADDED
 * 							add getcomboMajorColor(), getcomboMinorColor(),
 * 							populateMajorColor(), populateMinorColor(),
 * 							populateVehColorVector(),
 * 							getMajorColorCdSelected(), 
 * 							getMinorColorCdSelected(),
 * 							isMajorColorSameAsMinorColor(),
 * 							isMinorColorSelectedMajorColorNotSelected(),
 * 							setDataToDataObject(), validateVehColor()
 * 							modify setData(), actionPerformed(),
 * 							validateRegEvents()
 * 							defect 10830 Ver 6.8.0
 * K Harrell	10/15/2011	add validateGrossWt()
 * 							delete validateEmptyWt()
 * 							modify validateRegEvents()
 * 							defect 10959 Ver 6.9.0 
 * K Harrell	11/22/2011	Accommodate VTR275 
 * 							add setupForNoRecordFound() 
 * 							modify actionPerformed(), setData()  
 * 							defect 11052 Ver 6.9.0
 * K Harrell	12/09/2011	Only validate weight if EmptyWtReqd
 * 							modify validateGrossWt() 
 * 							defect 11183 Ver 6.9.0  
 * K Harrell	12/12/2011	Set CTData NoMFRecs for HQ 
 * 							modify actionPerformed()
 * 							defect 11169 Ver 6.9.0  
 * K Harrell	02/01/2012	Capacity/Body VIN removed by mistake 
 * 							add CAPACITY, BODY_VIN 
 * 							modify getstcLblCapacity(),
 * 							 getstcLblBodyVin()
 * 							defect 11271 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to present a summary of the vehicle, title, 
 * registration and owner information.
 *
 * This form also allows the user to request:
 *  1) review of the lienholder information
 *  2) alter/review owner addresses 
 *  3) alter/review additional information.
 *
 * @version	6.9.0 			02/01/2012
 * @author	Joseph Kwik
 * <br>Creation Date:		06/25/2001 13:48:11
 */
public class FrmRegistrationREG003
	extends RTSDialogBox
	implements ActionListener, WindowListener
{
	// REG003 objects
	private RTSButton ivjbtnAdditionalInformation = null;
	private RTSButton ivjbtnLien = null;
	private RTSButton ivjbtnOwnerAddress = null;
	private RTSButton ivjbtnSpecPltInformation = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmRegistrationREG003ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JPanel ivjJPanel4 = null;
	private JPanel ivjJPanel5 = null;
	private JPanel ivjJPanel6 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblAge = null;
	private JLabel ivjlblBodyVin = null;
	private JLabel ivjlblCapacity = null;
	private JLabel ivjlblClass = null;
	private JLabel ivjlblCounty = null;
	private JLabel ivjlblDocTypeCdDesc = null;
	private JLabel ivjlblDocumentNo = null;
	private JLabel ivjlblEmpty = null;
	private JLabel ivjlblExpiresMonth = null;
	private JLabel ivjlblExpiresYear = null;
	private JLabel ivjlblGross = null;
	private JLabel ivjlblInsuranceVerified = null;
	private JLabel ivjlblLength = null;
	private JLabel ivjlblMake = null;
	private JLabel ivjlblOdometer = null;
	private JLabel ivjlblOrg = null;
	private JLabel ivjlblOwnerId = null;
	private JLabel ivjlblOwnerName1 = null;
	private JLabel ivjlblOwnerName2 = null;
	private JLabel ivjlblPlate = null;
	private JLabel ivjlblSlash = null;
	private JLabel ivjlblTitleIssueDate = null;
	private JLabel ivjlblTons = null;
	private JLabel ivjlblTrlrType = null;
	private JLabel ivjlblType = null;
	private JLabel ivjlblVehClass = null;
	private JLabel ivjlblVehModel = null;
	private JLabel ivjlblVehType = null;
	private JLabel ivjlblVin = null;
	private JLabel ivjlblYear = null;
	private JList ivjlstIndiDescription = null;
	private JPanel ivjpnlInsurance = null;
	private JLabel ivjstcLblAge = null;
	private JLabel ivjstcLblBodyVin = null;
	private JLabel ivjstcLblCapacity = null;
	private JLabel ivjstcLblClass = null;
	private JLabel ivjstcLblClass2 = null;
	private JLabel ivjstcLblCounty = null;
	private JLabel ivjstcLblDocumentNo = null;
	private JLabel ivjstcLblEmpty = null;
	private JLabel ivjstcLblExpires = null;
	private JLabel ivjstcLblGross = null;
	private JLabel ivjstcLblId = null;
	private JLabel ivjstcLblIssued = null;
	private JLabel ivjstcLblLength = null;
	// defect 10830 
	private JLabel ivjstcLblColorMajor = null;
	private JLabel ivjstcLblColorMinor = null;
	// end defect 10830 
	private JLabel ivjstcLblOdometer = null;
	private JLabel ivjstcLblOrg = null;
	private JLabel ivjstcLblPlate = null;
	private JLabel ivjstcLblTons = null;
	private JLabel ivjstcLblTrlrType = null;
	private JLabel ivjstcLblType = null;
	// defect 10830 
	private JComboBox ivjcomboMajorColor = null;
	private JComboBox ivjcomboMinorColor = null;
	private Vector cvVehColor = new Vector();
	// end defect 10830 

	//	boolean 
	private boolean cbContinueWithReplacement = false;
	private boolean cbMFUp = true;

	// defect 10507 
	private boolean cbFirstIndiEval = true;
	// end defect 10507 

	// int
	private int ciButtonFocus = 0;
	private int ciNumIndis = 0;

	// String 
	private String csTransCd = new String();

	// Objects 
	private CompleteTransactionData caCompTransData = null;
	private MFVehicleData caMFVehicleData = null;
	private VehicleInquiryData caOrigVehInqData;
	private OwnerData caOwnerData;
	private RegistrationData caRegistrationData = null;
	private RegistrationValidationData caRegValidData = null;
	private TitleData caTitleData;
	private VehicleData caVehicleData = null;
	private VehicleInquiryData caVehicleInquiryData = null;

	// Constants  
	private final static String ADDL_INFO = "Additional Information";
	private final static String AGE = "Age:";
	private final static String CLASS = "Class:";
	private final static String CNTY = "County:";
	
	// defect 11271 
	private final static String BODY_VIN = "Body VIN:";
	private final static String CAPACITY = "Capacity:";
	// end defect 11271 
	// defect 11152
	//	private final static String DEFLT_AGE = "4";
	//	private final static String DEFLT_BODY_VIN = "SADFW34234";
	//	private final static String DEFLT_CNTY = "15  Bexar";
	//	private final static String DEFLT_DATE = "10/04/2002";
	//	private final static String DEFLT_DOCNO = "32423432423";
	//	private final static String DEFLT_DOCTYPE = "REGULAR TITLE";
	//	private final static String DEFLT_LEN = "111";
	//	private final static String DEFLT_MAKE = "Amer";
	//	private final static String DEFLT_MO = "12";
	//	private final static String DEFLT_MODEL = "Model";
	//	private final static String DEFLT_ODOMETER = "100000";
	//	private final static String DEFLT_OWNR_ID = "363738487";
	//	private final static String DEFLT_OWNR_NAME1 =
	//		"RTS2 APPLICATION SYSTEM 123456";
	//	private final static String DEFLT_OWNR_NAME2 = "Owner Name 2";
	//	private final static String DEFLT_PLTNO = "AAA-111";
	//	private final static String DEFLT_TONS = "20.00";
	//	private final static String DEFLT_TRLR_TYPE = "Hitch";
	//	private final static String DEFLT_VEH_CLASS = "Pass";
	//	private final static String DEFLT_VEH_TYPE = "4D";
	//	private final static String DEFLT_VIN = "SXPEIU332CISS";
	//	private final static String DEFLT_WEIGHT = "3200";
	//	private final static String DEFLT_YR = "2004";
	// end defect 11152  
	
	private final static String DEFLT_SLASH = "/";
	private final static String DEFLT_ZERO = "0";
	private final static String DEFLT_ZERO_TONS = "0.00";
	private final static String DISABLED_PERSON_PLT = "DPP";
	private final static Dollar DISABLED_PLT_MAX_TONNAGE =
		new Dollar("2.00");
	private final static String DISABLED_VETERAN_PLT = "DVPF";
	private final static String DOCUMENT = "Document:";
	private final static String DOCUMENT_NO = "Document No:";
	private final static String EMPTY = "Empty:";
	private final static String ERRMSG_DATA_MISS =
		"Data missing for IsNextVCREG029";
	private final static String ERRMSG_ERROR = "ERROR";
	private final static String ERRMSG_VALIDATE_CAP =
		"Validate carrying capacity";
	private final static String ERRMSG_VEH_TONS =
		"Vehicle tonnage cannot be greater than two tons for "
			+ "Disabled PersonOld or Disabled Veteran Plates";
	private final static String EXPIRES = "Expires:";
	private final static String GROSS = "Gross:";
	private final static String ID = "Id:";
	private final static String ISSUED = "Issued:";
	private final static String LEN = "Length:";
	private final static String LIENS = "Lien(s)";
	private final static String MSG_DYWT_CANCEL =
		"Are you sure you want to Cancel?\nIf yes, changes will not"
			+ " be saved.";
	private final static String MSG_IS_REC_CORRECT =
		"Is this the correct registration record for which the "
			+ "refund needs to be applied?";
	// defect 10830 
	private final static String MSG_VEHICLE_COLOR_NOT_ADDED =
		"Vehicle color has not been added.  Do you "
			+ "want to add?";
	// end defect 10830 
	private final static String ODOMETER = "Odometer:";
	private final static String OWNR = "Owner:";
	private final static String OWNR_ADDR = "Owner Address";
	private final static String PLATE = "Plate:";
	private final static String REGIS = "Registration:";
	private final static String SELECT = "Select if needed:";
	private final static String TITLE_REG003 =
		"Registration          REG003";
	private final static String TONS = "Tons:";
	private final static String TRLR_TYPE = "Trlr Type:";
	private final static String TYPE = "Type:";
	private final static String VEHICLE = "Vehicle:";
	private final static String WEIGHT = "Weight:";
	// defect 10830 
	private final static int ZERO = 0;

	private static final String MAJORCOLOR = "Major Color:";
	private static final String MINORCOLOR = "Minor Color:";
	// end defect 10830 

	/**
	 * main entrypoint, starts the part when it is run as an application
	 *
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmRegistrationREG003 laFrmRegistrationREG003;
			laFrmRegistrationREG003 = new FrmRegistrationREG003();
			laFrmRegistrationREG003.setModal(true);
			laFrmRegistrationREG003
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			VehicleInquiryData laVID = new VehicleInquiryData();
			MFVehicleData laMFVD = new MFVehicleData();
			VehicleData laVD = new VehicleData();
			TitleData laTD = new TitleData();
			OwnerData laOD = new OwnerData();
			RegistrationData laRD = new RegistrationData();
			laMFVD.setVehicleData(laVD);
			laMFVD.setTitleData(laTD);
			laMFVD.setOwnerData(laOD);
			laMFVD.setRegData(laRD);
			laVID.setMfVehicleData(laMFVD);
			laFrmRegistrationREG003.setData(laVID);
			laFrmRegistrationREG003.show();
			java.awt.Insets laInsets =
				laFrmRegistrationREG003.getInsets();
			laFrmRegistrationREG003.setSize(
				laFrmRegistrationREG003.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmRegistrationREG003.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmRegistrationREG003.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmRegistrationREG003 constructor.
	 */
	public FrmRegistrationREG003()
	{
		super();
		initialize();
	}

	/**
	 * FrmRegistrationREG003 constructor with parent.
	 *
	 * @param aaParent Dialog
	 */
	public FrmRegistrationREG003(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmRegistrationREG003 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmRegistrationREG003(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			clearAllColor(this);

			if (aaAE.getSource() == ivjbtnLien)
			{
				getController().processData(
					VCRegistrationREG003.TTL005,
					caVehicleInquiryData);
			}
			else if (aaAE.getSource() == ivjbtnOwnerAddress)
			{
				getController().processData(
					VCRegistrationREG003.OWNER_ADDRESS,
					caVehicleInquiryData);
				ciButtonFocus = VCRegistrationREG003.OWNER_ADDRESS;
			}
			else if (aaAE.getSource() == ivjbtnAdditionalInformation)
			{
				// defect 6874
				getController().processData(
					VCRegistrationREG003.ADDITIONAL_INFO,
					getVehInqDataCopy());
				// end defect 6874

				ciButtonFocus = VCRegistrationREG003.ADDITIONAL_INFO;
			}
			// defect 9123
			else if (aaAE.getSource() == ivjbtnSpecPltInformation)
			{
				getController().processData(
					VCRegistrationREG003.SPECIAL_PLATES,
					caVehicleInquiryData);
			}
			// end defect 9123
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 9893 
				RegistrationVerify.verifySpclPltAddrUpdtType(
					caMFVehicleData);
				// end defect 9893 

				if (UtilityMethods
					.getEventType(csTransCd)
					.equals(TransCdConstant.REG_EVENT_TYPE))
				{
					// defect 10830
					setDataToDataObject();
					// end defect 10830
					if (validateRegEvents())
					{
						getController().processData(
							VCRegistrationREG003.ENTER,
							caCompTransData);

					}
				}

				// Accounting Events
				// In REG003 when TransCode is HOTCK, HOTDED, or CKREDM 
				//    forward the MFVehData to ACC004 and 
				// if the TransCode is HCKITM 
				//    forward the data to ACC005.
				else if (
					csTransCd.equals(TransCdConstant.HOTCK)
						|| csTransCd.equals(TransCdConstant.HOTDED)
						|| csTransCd.equals(TransCdConstant.CKREDM))
				{
					getController().processData(
						VCRegistrationREG003.ACC004,
						caMFVehicleData);
				}
				else if (csTransCd.equals(TransCdConstant.HCKITM))
				{
					getController().processData(
						VCRegistrationREG003.ACC005,
						caMFVehicleData);
				}
				else if (csTransCd.equals(TransCdConstant.REFUND))
				{
					// When the confirmation pops up with "Are you sure
					// this is the right record" 
					// if they select "Yes" 
					//    forward the MFVeh object to ACC006.
					// If they select "No", 
					//    make the next screen INQ007 with no data object
					// defect 8756
					// Used common constant for CTL001 title
					RTSException leRTSException =
						new RTSException(
							RTSException.CTL001,
							MSG_IS_REC_CORRECT,
							ScreenConstant.CTL001_FRM_TITLE);
					// end defect 8756
					int liReturnCode =
						leRTSException.displayError(this);
					if (liReturnCode == RTSException.YES)
					{
						getController().processData(
							VCRegistrationREG003.ACC006,
							caMFVehicleData);
					}
					else if (liReturnCode == RTSException.NO)
					{
						MFVehicleData laMFVehicleData =
							new MFVehicleData();
						RegistrationData laRegistrationData =
							new RegistrationData();
						laRegistrationData.setRegPltNo(
							caMFVehicleData.getRegData().getRegPltNo());
						laMFVehicleData.setRegData(laRegistrationData);
						laMFVehicleData.setDoNotBuildMvFunc(true);
						getController().processData(
							VCRegistrationREG003.INQ007,
							laMFVehicleData);
					}
				}
				// VEHINQ events
				// defect 10112 
				// Do not show "Customer Name" INQ007 if HQ 
				else if (csTransCd.equals(TransCdConstant.VEHINQ))
				{
					int liPrintOption =
						caVehicleInquiryData.getPrintOptions();
					
					// defect 11052					
					if (!SystemProperty.isHQ() && 
							liPrintOption!= VehicleInquiryData.VIEW_ONLY)
							//		&& (liPrintOption
							//			== VehicleInquiryData.VIEW_AND_PRINT
							//			|| liPrintOption
							//			== VehicleInquiryData
							//			.CHARGE_FEE_VIEW_AND_PRINT))
					{
						getController().processData(
							VCRegistrationREG003.INQ007,
							caVehicleInquiryData);
					}
					// If HQ & any option (View, View & Print, Print Only) 
					// or  !HQ & View Only 
					else
					{
						CompleteTransactionData laCTData =
							new CompleteTransactionData();
						laCTData.setOrgVehicleInfo(
							getOrigVehInqData().getMfVehicleData());
						laCTData.setVehicleInfo(caMFVehicleData);
						// defect 11169 
						laCTData.setNoMFRecs(getOrigVehInqData().getNoMFRecs()); 
						// end defect 11169 
						// defect 11052 
						laCTData.setTransCode(
								UtilityMethods.getVehInqTransCd(liPrintOption));
							//getController().getTransCode());
						// end defect 11052 
						
						laCTData.setPrintOptions(liPrintOption);
						
						getController().processData(
							AbstractViewController.ENTER,
							laCTData);
						// end defect 11052
					}
				}
				// end defect 10112
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				if (!handleCancel())
				{
					return;
				}
				// 
				if (csTransCd.equals(TransCdConstant.RENEW)
					&& caOrigVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegInvldIndi()
						== 1)
				{
					caRegistrationData =
						caOrigVehInqData
							.getMfVehicleData()
							.getRegData();
				}
				getController().processData(
					AbstractViewController.CANCEL,
					caMFVehicleData);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				displayHelp();
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Complete address change/print renewal transaction
	 *
	 */
	private void completeAddrChngTrans()
	{
		// Clear mail return indicators
		caRegistrationData.setRenwlMailRtrnIndi(0);

		// Determine if renewal notice requested
		if (caRegValidData.isRenwlReq())
		{
			//Assign TransCd$:="RNR"
			caRegValidData.setTransCode(TransCdConstant.RNR);
		}
	}

	/**
	 * Complete registration correction transaction
	 *
	 * @return boolean 
	 */
	private boolean completeCorrtnTrans()
	{
		// defect 10127 
		setResComptCntyNo();

		getController().processData(
			VCRegistrationREG003.REG_CORRTN,
			caVehicleInquiryData);

		//return caRegValidData.isEnterOnRegCorrection();
		return false;
		// end defect 10127 
	}

	/**
	 * Complete duplicate receipt transaction
	 *
	 * @return boolean  
	 */
	private boolean completeDuplTrans()
	{
		return procsSoftStops();
	}

	/**
	 * Complete exchange transaction
	 *
	 * @return boolean
	 */
	private boolean completeExchTrans()
	{
		// defect 10127 
		// Reorganize 
		// Test if class/plate/sticker status OK
		boolean lbReturn = caRegValidData.getVehClassOK() == 1;

		if (lbReturn)
		{
			//	set initial county number to local office number
			setResComptCntyNo();

			// defect 10440
			// Add weight validation 
			//lbReturn = procsSoftStops();
			lbReturn = procsSoftStops() && validateVehWeight();
			// end defect 10440 

			if (lbReturn)
			{

				// Set new plate indicator to yes
				caRegValidData.setNewPltReplIndi(1);

				//	determine inventory items needed
				prepInv();

				caRegistrationData.setPrevExpMo(
					caOrigVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegExpMo());
				caRegistrationData.setPrevExpYr(
					caOrigVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegExpYr());
			}
		}
		else
		{
			// 92
			new RTSException(
				ErrorsConstant
					.ERR_NUM_REG_CHG_REQD_GO_TO_ADDL_INFO)
					.displayError(
				this);

			getController().processData(
				VCRegistrationREG003.ADDITIONAL_INFO,
				caVehicleInquiryData);
		}
		return lbReturn;
		// end defect 10127 
	}

	/**
	 * Complete PAWT transaction 
	 *
	 * @return boolean 
	 */
	private boolean completePawtTrans()
	{
		boolean lbReturn = true;

		if (caRegValidData.isVehWtStatusOK())
		{
			setResComptCntyNo();

			lbReturn = procsSoftStops();

			if (lbReturn)
			{

				// Set to CORREGX for Appreh Add'l Wt; Test TransCd$=
				// "CORREG"&ModifyChoice=2
				if ((caRegValidData
					.getTransCode()
					.equals(TransCdConstant.CORREG))
					&& (caRegValidData.getRegModify()
						== RegistrationConstant.REG_MODIFY_APPREHENDED))
				{
					caRegValidData.setTransCode(
						TransCdConstant.CORREGX);
				}
			}
		}
		else
		{
			lbReturn = false;
			// 423
			new RTSException(
				ErrorsConstant
					.ERR_NUM_VEH_WT_CHG_REQD_GO_TO_ADDL_INFO)
					.displayError(
				this);

			getController().processData(
				VCRegistrationREG003.ADDITIONAL_INFO,
				caVehicleInquiryData);
		}

		return lbReturn;
	}

	/**
	 * Complete renewal transaction
	 *
	 * @return boolean
	 */
	private boolean completeRenwlTrans()
	{
		setResComptCntyNo();

		// defect 10127 
		boolean lbReturn =
			procsSoftStops()
				&& validateClassPltStkrCombo()
				&& validateMinGrossWt()
			// defect 10440 
	&& validateVehWeight()
			// end defect 10440
	&& validateHvyVehUseTax();

		if (lbReturn)
		{
			try
			{
				RegistrationVerify.verifyValidSpclPlt(
					caVehicleInquiryData,
					getController().getTransCode());
			}
			catch (RTSException aeRTSEx)
			{
				lbReturn = false;

				aeRTSEx.displayError(this);

				getController().processData(
					VCRegistrationREG003.ADDITIONAL_INFO,
					caVehicleInquiryData);
			}
		}
		if (lbReturn)
		{

			// determine inventory items needed
			prepInv();

			caRegistrationData.setPrevExpMo(
				caOrigVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegExpMo());

			caRegistrationData.setPrevExpYr(
				caOrigVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegExpYr());

			caRegistrationData.setEmissionSourceCd("");

			caRegistrationData.setRenwlMailRtrnIndi(0);
		}
		return lbReturn;

		// end defect 10127 
	}

	/**
	 * Complete replacement transaction
	 *
	 * @return boolean 
	 */
	private boolean completeReplTrans()
	{
		boolean lbReturn = true;

		if (!SystemProperty.isHQ()
			&& RegistrationSpecialExemptions.verifyPltTypeScope(
				caRegistrationData)
				== -1)
		{
			lbReturn = false;
			// 48
			new RTSException(
				ErrorsConstant
					.ERR_NUM_REPL_CANNOT_BE_PERFORMED)
					.displayError(
				this);
		}

		lbReturn = procsSoftStops();

		if (lbReturn)
		{
			// Continue with multi-year or annual replacement
			// Determine if plate is multi-year from cache
			PlateTypeData laPlateTypeData =
				PlateTypeCache.getPlateType(
					caRegistrationData.getRegPltCd());

			if (laPlateTypeData.getAnnualPltIndi() == 0)
			{
				// multi-year scenario
				getController().processData(
					VCRegistrationREG003.REPL_CHOICES,
					caVehicleInquiryData);

				// if cancelled from replacement choices frame
				// keep REG003 visible.
				lbReturn = caRegValidData.isEnterOnReplChoices();
			}
			else
			{
				// handle replacement of annual plate
				handleReplAnnualPlt();
			}
		}
		return lbReturn;
	}

	/**
	 * Determine which event transaction to complete and call the
	 * appropriate method.
	 * 
	 * @return boolean
	 */
	private boolean completeTrans()
	{
		boolean lbComplete = true;
		String lsTransCd = caRegValidData.getTransCode();

		if (lsTransCd.equals(TransCdConstant.RENEW))
		{
			lbComplete = completeRenwlTrans();
		}
		else if (lsTransCd.equals(TransCdConstant.REPL))
		{
			lbComplete =
				completeReplTrans() && cbContinueWithReplacement;
		}
		else if (lsTransCd.equals(TransCdConstant.DUPL))
		{
			lbComplete = completeDuplTrans();
		}
		else if (lsTransCd.equals(TransCdConstant.EXCH))
		{
			lbComplete = completeExchTrans();
		}
		else if (
			lsTransCd.equals(TransCdConstant.PAWT)
				|| (lsTransCd.equals(TransCdConstant.CORREG)
					&& caRegValidData.getRegModify()
						== RegistrationConstant.REG_MODIFY_APPREHENDED))
		{
			lbComplete = completePawtTrans();
		}
		else if (
			lsTransCd.equals(TransCdConstant.CORREG)
				&& caRegValidData.getRegModify()
					== RegistrationConstant.REG_MODIFY_REG)
		{
			lbComplete = completeCorrtnTrans();
		}
		else if (lsTransCd.equals(TransCdConstant.ADDR))
		{
			completeAddrChngTrans();
		}

		if (lbComplete)
		{
			if (caRegValidData.getNewPltReplIndi() == 1)
			{
				caRegistrationData.setPrevPltNo(
					caRegValidData
						.getOrigVehInqData()
						.getMfVehicleData()
						.getRegData()
						.getRegPltNo());
			}

			cbContinueWithReplacement = false;

			prepFees();

			// Reset RegPltNo if originally reg invalid for Renewal
			//  (Restoring for RTS_TRANS population)  
			if (lsTransCd.equals(TransCdConstant.RENEW)
				&& caRegValidData
					.getOrigVehInqData()
					.getMfVehicleData()
					.getRegData()
					.getRegInvldIndi()
					!= 0
				&& caRegistrationData.getRegPltNo() != null
				&& caRegistrationData.getRegPltNo().trim().equals(""))
			{
				caRegistrationData.setRegPltNo(
					caRegValidData
						.getOrigVehInqData()
						.getMfVehicleData()
						.getRegData()
						.getRegPltNo());
			}
			// defect 4008 
			else if (
				lsTransCd.equals(TransCdConstant.DUPL)
					|| lsTransCd.equals(TransCdConstant.PAWT)
					|| lsTransCd.equals(TransCdConstant.CORREGX)
					|| lsTransCd.equals(TransCdConstant.CORREG))
			{
				if (SystemProperty.isHQ())
				{
					RTSException leRTSEx =
						new RTSException(
							RTSException.CTL001,
							CommonConstant.TXT_COMPLETE_TRANS_QUESTION,
							ScreenConstant.CTL001_FRM_TITLE);

					lbComplete =
						(leRTSEx.displayError(this)
							== RTSException.YES);
				}
			}
			// end defect 4008
		}
		return lbComplete;
	}

	/**
	 * Display Help
	 *
	 */
	public void displayHelp()
	{
		if (csTransCd.equals(TransCdConstant.RENEW))
		{
			if (UtilityMethods.isMFUP())
			{
				RTSHelp.displayHelp(RTSHelp.REG003A);
			}
			else
			{
				RTSHelp.displayHelp(RTSHelp.REG003N);
			}
		}
		else if (csTransCd.equals(TransCdConstant.DUPL))
		{
			RTSHelp.displayHelp(RTSHelp.REG003B);
		}
		else if (csTransCd.equals(TransCdConstant.EXCH))
		{
			if (UtilityMethods.isMFUP())
			{
				RTSHelp.displayHelp(RTSHelp.REG003C);
			}
			else
			{
				RTSHelp.displayHelp(RTSHelp.REG003P);
			}
		}
		else if (csTransCd.equals(TransCdConstant.REPL))
		{
			if (UtilityMethods.isMFUP())
			{
				RTSHelp.displayHelp(RTSHelp.REG003D);
			}
			else
			{
				RTSHelp.displayHelp(RTSHelp.REG003O);
			}
		}
		else if (csTransCd.equals(TransCdConstant.PAWT))
		{
			if (UtilityMethods.isMFUP())
			{
				RTSHelp.displayHelp(RTSHelp.REG003E);
			}
			else
			{
				RTSHelp.displayHelp(RTSHelp.REG003Q);
			}
		}
		else if (csTransCd.equals(TransCdConstant.ADDR))
		{
			RTSHelp.displayHelp(RTSHelp.REG003G);
		}
		else if (csTransCd.equals(TransCdConstant.VEHINQ))
		{
			RTSHelp.displayHelp(RTSHelp.REG003H);
		}
		else if (csTransCd.equals(TransCdConstant.HOTCK))
		{
			RTSHelp.displayHelp(RTSHelp.REG003I);
		}
		else if (csTransCd.equals(TransCdConstant.HOTDED))
		{
			RTSHelp.displayHelp(RTSHelp.REG003J);
		}
		else if (csTransCd.equals(TransCdConstant.CKREDM))
		{
			RTSHelp.displayHelp(RTSHelp.REG003K);
		}
		else if (csTransCd.equals(TransCdConstant.HCKITM))
		{
			RTSHelp.displayHelp(RTSHelp.REG003L);
		}
		else if (csTransCd.equals(TransCdConstant.REFUND))
		{
			RTSHelp.displayHelp(RTSHelp.REG003M);
		}
		else if (
			csTransCd.equals(TransCdConstant.CORREG)
				&& caRegValidData.getRegModify()
					== RegistrationConstant.REG_MODIFY_APPREHENDED)
		{
			if (UtilityMethods.isMFUP())
			{
				RTSHelp.displayHelp(RTSHelp.REG003S);
			}
			else
			{
				RTSHelp.displayHelp(RTSHelp.REG003R);
			}
		}
		else if (csTransCd.equals(TransCdConstant.CORREG))
		{
			if (UtilityMethods.isMFUP())
			{
				RTSHelp.displayHelp(RTSHelp.REG003F);
			}
			else
			{
				RTSHelp.displayHelp(RTSHelp.REG003T);
			}
		}
	}

	/**
	 * Return the btnAdditionalInformation property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnAdditionalInformation()
	{
		if (ivjbtnAdditionalInformation == null)
		{
			try
			{
				ivjbtnAdditionalInformation =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnAdditionalInformation.setName(
					"btnAdditionalInformation");
				ivjbtnAdditionalInformation.setMnemonic(
					java.awt.event.KeyEvent.VK_A);
				ivjbtnAdditionalInformation.setText(ADDL_INFO);
				ivjbtnAdditionalInformation.setMaximumSize(
					new java.awt.Dimension(159, 25));
				ivjbtnAdditionalInformation.setActionCommand(ADDL_INFO);
				ivjbtnAdditionalInformation.setMinimumSize(
					new java.awt.Dimension(159, 25));
				// user code begin {1}
				ivjbtnAdditionalInformation.addActionListener(this);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjbtnAdditionalInformation;
	}

	/**
	 * Return the ivjbtnLien property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnLien()
	{
		if (ivjbtnLien == null)
		{
			try
			{
				ivjbtnLien =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnLien.setName("ivjbtnLien");
				ivjbtnLien.setMnemonic(java.awt.event.KeyEvent.VK_L);
				ivjbtnLien.setText(LIENS);
				ivjbtnLien.setMaximumSize(
					new java.awt.Dimension(73, 25));
				ivjbtnLien.setActionCommand(LIENS);
				ivjbtnLien.setEnabled(false);
				ivjbtnLien.setMinimumSize(
					new java.awt.Dimension(73, 25));
				// user code begin {1}
				ivjbtnLien.addActionListener(this);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjbtnLien;
	}

	/**
	 * Return the ivjbtnOwnerAddress property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnOwnerAddress()
	{
		if (ivjbtnOwnerAddress == null)
		{
			try
			{
				ivjbtnOwnerAddress =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnOwnerAddress.setName("ivjbtnOwnerAddress");
				ivjbtnOwnerAddress.setMnemonic(
					java.awt.event.KeyEvent.VK_O);
				ivjbtnOwnerAddress.setText(OWNR_ADDR);
				ivjbtnOwnerAddress.setMaximumSize(
					new java.awt.Dimension(123, 25));
				ivjbtnOwnerAddress.setActionCommand(OWNR_ADDR);
				ivjbtnOwnerAddress.setMinimumSize(
					new java.awt.Dimension(123, 25));
				// user code begin {1}
				ivjbtnOwnerAddress.addActionListener(this);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjbtnOwnerAddress;
	}

	/**
	 * Return the ivjbtnSpecPltInformation property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnSpecPltInformation()
	{
		if (ivjbtnSpecPltInformation == null)
		{
			try
			{
				ivjbtnSpecPltInformation = new RTSButton();
				ivjbtnSpecPltInformation.setText(
					"Special Plate Information");
				ivjbtnSpecPltInformation.setMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjbtnSpecPltInformation.setName(
					"ivjbtnSpecPltInformation");
				// user code begin {1}
				ivjbtnSpecPltInformation.setEnabled(false);
				ivjbtnSpecPltInformation.addActionListener(this);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjbtnSpecPltInformation;
	}

	/**
	 * Return the ivjButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setBounds(334, 388, 243, 39);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the comboMajorColor property value.
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JComboBox getcomboMajorColor()
	{
		if (ivjcomboMajorColor == null)
		{
			try
			{
				ivjcomboMajorColor = new javax.swing.JComboBox();
				ivjcomboMajorColor.setBounds(91, 55, 178, 24);
				ivjcomboMajorColor.setName("comboMajorColor");
				ivjcomboMajorColor.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboMajorColor.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboMajorColor.setBackground(java.awt.Color.white);
				// user code begin (1)
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboMajorColor;
	}

	/**
	 * Return the comboMinorColor property value.
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JComboBox getcomboMinorColor()
	{
		if (ivjcomboMinorColor == null)
		{
			try
			{
				ivjcomboMinorColor = new javax.swing.JComboBox();
				ivjcomboMinorColor.setBounds(91, 82, 178, 24);
				ivjcomboMinorColor.setName("comboMinorColor");
				ivjcomboMinorColor.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboMinorColor.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboMinorColor.setBackground(java.awt.Color.white);
				// user code begin (1)
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboMinorColor;
	}

	/**
	 * Return the ivjFrmRegistrationREG003ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmRegistrationREG003ContentPane1()
	{
		if (ivjFrmRegistrationREG003ContentPane1 == null)
		{
			try
			{
				ivjFrmRegistrationREG003ContentPane1 = new JPanel();
				ivjFrmRegistrationREG003ContentPane1.setName(
					"ivjFrmRegistrationREG003ContentPane1");
				ivjFrmRegistrationREG003ContentPane1.setLayout(null);
				ivjFrmRegistrationREG003ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmRegistrationREG003ContentPane1.setMinimumSize(
					new java.awt.Dimension(654, 564));
				ivjFrmRegistrationREG003ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				ivjFrmRegistrationREG003ContentPane1.add(
					getJPanel1(),
					null);
				ivjFrmRegistrationREG003ContentPane1.add(
					getJPanel2(),
					null);
				ivjFrmRegistrationREG003ContentPane1.add(
					getJPanel3(),
					null);
				ivjFrmRegistrationREG003ContentPane1.add(
					getJPanel4(),
					null);
				ivjFrmRegistrationREG003ContentPane1.add(
					getJPanel5(),
					null);
				ivjFrmRegistrationREG003ContentPane1.add(
					getJScrollPane1(),
					null);
				ivjFrmRegistrationREG003ContentPane1.add(
					getJPanel6(),
					null);
				ivjFrmRegistrationREG003ContentPane1.add(
					getButtonPanel1(),
					null);
				// user code end
				ivjFrmRegistrationREG003ContentPane1.add(
					getpnlInsurance(),
					null);
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjFrmRegistrationREG003ContentPane1;
	}

	/**
	 * Get indicators, build string to display indicators in text area.
	 */
	private void getIndicators()
	{
		// defect 10507 
		MFVehicleData laCopyOrigMfVehData =
			(MFVehicleData) UtilityMethods.copy(
				caOrigVehInqData.getMfVehicleData());

		if (!cbFirstIndiEval)
		{
			laCopyOrigMfVehData.setSpclPltRegisData(
				caMFVehicleData.getSpclPltRegisData());
		}
		cbFirstIndiEval = false;

		Vector lvIndis =
			IndicatorLookup.getIndicators(
				laCopyOrigMfVehData,
				getController().getTransCode(),
				IndicatorLookup.SCREEN);

		//	Vector lvIndis =
		//	 IndicatorLookup.getIndicators(
		//	  caOrigVehInqData.getMfVehicleData(),
		//	  getController().getTransCode(),
		//	  IndicatorLookup.SCREEN);
		// end defect 10507 

		StringBuffer lsIndis = new StringBuffer("");
		int liNumIndis = lvIndis.size();
		if (liNumIndis > 0)
		{
			Vector lvRows = new java.util.Vector();
			for (int i = 0; i < liNumIndis; i++)
			{
				IndicatorData laData = (IndicatorData) lvIndis.get(i);
				lsIndis.append(
					laData.getStopCode() == null
						? " "
						: laData.getStopCode());
				lsIndis.append("  ");
				lsIndis.append(laData.getDesc());
				lvRows.add(lsIndis.toString());
				lsIndis = new StringBuffer("");
			}
			getlstIndiDescription().setListData(lvRows);
			getlstIndiDescription().setSelectedIndex(0);
			ciNumIndis = liNumIndis;
		}
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setBorder(
					new TitledBorder(new EtchedBorder(), REGIS));
				ivjJPanel1.setLayout(null);
				ivjJPanel1.add(getstcLblPlate(), null);
				ivjJPanel1.add(getstcLblExpires(), null);
				ivjJPanel1.add(getstcLblClass(), null);
				ivjJPanel1.add(getstcLblType(), null);
				ivjJPanel1.add(getstcLblCounty(), null);
				ivjJPanel1.add(getlblPlate(), null);
				ivjJPanel1.add(getlblExpiresMonth(), null);
				ivjJPanel1.add(getlblClass(), null);
				ivjJPanel1.add(getlblType(), null);
				ivjJPanel1.add(getlblCounty(), null);
				ivjJPanel1.add(getstcLblAge(), null);
				ivjJPanel1.add(getlblAge(), null);
				ivjJPanel1.add(getlblSlash(), null);
				ivjJPanel1.add(getlblExpiresYear(), null);
				ivjJPanel1.add(getstcLblOrg(), null);
				ivjJPanel1.add(getlblOrg(), null);
				ivjJPanel1.setSize(276, 156);
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(259, 183));
				ivjJPanel1.setLocation(18, 9);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setBorder(
					new TitledBorder(new EtchedBorder(), VEHICLE));
				ivjJPanel2.setLayout(null);
				ivjJPanel2.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel2.setMinimumSize(
					new java.awt.Dimension(289, 198));
				getJPanel2().add(getlblYear(), getlblYear().getName());
				getJPanel2().add(getlblMake(), getlblMake().getName());
				getJPanel2().add(
					getlblVehType(),
					getlblVehType().getName());
				getJPanel2().add(getlblVin(), getlblVin().getName());
				getJPanel2().add(
					getstcLblBodyVin(),
					getstcLblBodyVin().getName());
				getJPanel2().add(
					getstcLblClass2(),
					getstcLblClass2().getName());
				getJPanel2().add(
					getstcLblOdometer(),
					getstcLblOdometer().getName());
				getJPanel2().add(
					getstcLblTrlrType(),
					getstcLblTrlrType().getName());
				getJPanel2().add(
					getlblBodyVin(),
					getlblBodyVin().getName());
				getJPanel2().add(
					getlblVehClass(),
					getlblVehClass().getName());
				getJPanel2().add(
					getlblOdometer(),
					getlblOdometer().getName());
				getJPanel2().add(
					getlblTrlrType(),
					getlblTrlrType().getName());
				getJPanel2().add(
					getstcLblTons(),
					getstcLblTons().getName());
				getJPanel2().add(getlblTons(), getlblTons().getName());
				getJPanel2().add(
					getstcLblLength(),
					getstcLblLength().getName());
				getJPanel2().add(
					getlblLength(),
					getlblLength().getName());
				getJPanel2().add(
					getlblVehModel(),
					getlblVehModel().getName());
				// user code begin {1}
				ivjJPanel2.add(getcomboMajorColor(), null);
				ivjJPanel2.add(getcomboMinorColor(), null);
				ivjJPanel2.add(getstcLblColorMajor(), null);
				ivjJPanel2.add(getstcLblColorMinor(), null);
				ivjJPanel2.setBounds(18, 168, 276, 204);
				//Border b = new TitledBorder(new EtchedBorder(),
				// "Vehicle");
				//ivjJPanel2.setBorder(b);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjJPanel2;
	}

	/**
	 * Return the JPanel3 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new JPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setBorder(
					new TitledBorder(new EtchedBorder(), WEIGHT));
				ivjJPanel3.setLayout(new java.awt.GridBagLayout());
				ivjJPanel3.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel3.setMinimumSize(
					new java.awt.Dimension(172, 105));
				java.awt.GridBagConstraints constraintsstcLblEmpty =
					new java.awt.GridBagConstraints();
				constraintsstcLblEmpty.gridx = 1;
				constraintsstcLblEmpty.gridy = 1;
				constraintsstcLblEmpty.ipadx = 7;
				constraintsstcLblEmpty.insets =
					new java.awt.Insets(16, 17, 1, 9);
				getJPanel3().add(
					getstcLblEmpty(),
					constraintsstcLblEmpty);
				java.awt.GridBagConstraints constraintslblEmpty =
					new java.awt.GridBagConstraints();
				constraintslblEmpty.gridx = 2;
				constraintslblEmpty.gridy = 1;
				constraintslblEmpty.ipadx = 32;
				constraintslblEmpty.insets =
					new java.awt.Insets(16, 10, 1, 19);
				getJPanel3().add(getlblEmpty(), constraintslblEmpty);
				java.awt.GridBagConstraints constraintsstcLblCapacity =
					new java.awt.GridBagConstraints();
				constraintsstcLblCapacity.gridx = 1;
				constraintsstcLblCapacity.gridy = 2;
				constraintsstcLblCapacity.ipadx = 5;
				constraintsstcLblCapacity.insets =
					new java.awt.Insets(1, 5, 1, 9);
				getJPanel3().add(
					getstcLblCapacity(),
					constraintsstcLblCapacity);
				java.awt.GridBagConstraints constraintslblCapacity =
					new java.awt.GridBagConstraints();
				constraintslblCapacity.gridx = 2;
				constraintslblCapacity.gridy = 2;
				constraintslblCapacity.ipadx = 53;
				constraintslblCapacity.insets =
					new java.awt.Insets(1, 10, 1, 19);
				getJPanel3().add(
					getlblCapacity(),
					constraintslblCapacity);
				java.awt.GridBagConstraints constraintsstcLblGross =
					new java.awt.GridBagConstraints();
				constraintsstcLblGross.gridx = 1;
				constraintsstcLblGross.gridy = 3;
				constraintsstcLblGross.ipadx = 8;
				constraintsstcLblGross.insets =
					new java.awt.Insets(1, 17, 20, 9);
				getJPanel3().add(
					getstcLblGross(),
					constraintsstcLblGross);
				java.awt.GridBagConstraints constraintslblGross =
					new java.awt.GridBagConstraints();
				constraintslblGross.gridx = 2;
				constraintslblGross.gridy = 3;
				constraintslblGross.ipadx = 32;
				constraintslblGross.insets =
					new java.awt.Insets(1, 10, 20, 19);
				getJPanel3().add(getlblGross(), constraintslblGross);
				// user code begin {1}
				ivjJPanel3.setBounds(18, 375, 156, 79);
				//Border b = new TitledBorder(new EtchedBorder(),
				// "Weight");
				//ivjJPanel3.setBorder(b);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjJPanel3;
	}

	/**
	 * Return the JPanel4 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel4()
	{
		if (ivjJPanel4 == null)
		{
			try
			{
				ivjJPanel4 = new JPanel();
				ivjJPanel4.setName("JPanel4");
				ivjJPanel4.setBorder(
					new TitledBorder(new EtchedBorder(), OWNR));
				ivjJPanel4.setLayout(new java.awt.GridBagLayout());
				ivjJPanel4.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel4.setMinimumSize(
					new java.awt.Dimension(294, 90));
				java.awt.GridBagConstraints constraintsstcLblId =
					new java.awt.GridBagConstraints();
				constraintsstcLblId.gridx = 0;
				constraintsstcLblId.gridy = 0;
				constraintsstcLblId.ipadx = 8;
				constraintsstcLblId.insets =
					new java.awt.Insets(8, 25, 3, 8);
				getJPanel4().add(getstcLblId(), constraintsstcLblId);
				java.awt.GridBagConstraints constraintslblOwnerId =
					new java.awt.GridBagConstraints();
				constraintslblOwnerId.gridx = 1;
				constraintslblOwnerId.gridy = 0;
				constraintslblOwnerId.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintslblOwnerId.ipadx = 145;
				constraintslblOwnerId.insets =
					new java.awt.Insets(8, 3, 3, 35);
				getJPanel4().add(
					getlblOwnerId(),
					constraintslblOwnerId);
				java.awt.GridBagConstraints constraintslblOwnerName1 =
					new java.awt.GridBagConstraints();
				constraintslblOwnerName1.gridx = 0;
				constraintslblOwnerName1.gridy = 1;
				constraintslblOwnerName1.gridwidth = 2;
				constraintslblOwnerName1.ipadx = 150;
				constraintslblOwnerName1.ipady = -2;
				constraintslblOwnerName1.insets =
					new java.awt.Insets(3, 20, 3, 20);
				getJPanel4().add(
					getlblOwnerName1(),
					constraintslblOwnerName1);
				java.awt.GridBagConstraints constraintslblOwnerName2 =
					new java.awt.GridBagConstraints();
				constraintslblOwnerName2.gridx = 0;
				constraintslblOwnerName2.gridy = 2;
				constraintslblOwnerName2.gridwidth = 2;
				constraintslblOwnerName2.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintslblOwnerName2.ipadx = 150;
				constraintslblOwnerName2.insets =
					new java.awt.Insets(3, 22, 11, 22);
				getJPanel4().add(
					getlblOwnerName2(),
					constraintslblOwnerName2);
				// user code begin {1}
				ivjJPanel4.setBounds(307, 9, 300, 75);
				//Border b = new TitledBorder(new EtchedBorder(),
				// "Owner");
				//ivjJPanel4.setBorder(b);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjJPanel4;
	}

	/**
	 * Return the JPanel5 property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getJPanel5()
	{
		if (ivjJPanel5 == null)
		{
			try
			{
				ivjJPanel5 = new JPanel();
				ivjJPanel5.setName("JPanel5");
				ivjJPanel5.setBorder(
					new TitledBorder(new EtchedBorder(), DOCUMENT));
				ivjJPanel5.setLayout(new java.awt.GridBagLayout());
				ivjJPanel5.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel5.setMinimumSize(
					new java.awt.Dimension(294, 111));
				java.awt.GridBagConstraints constraintsstcLblDocumentNo =
					new java.awt.GridBagConstraints();
				constraintsstcLblDocumentNo.gridx = 1;
				constraintsstcLblDocumentNo.gridy = 1;
				constraintsstcLblDocumentNo.ipadx = 12;
				constraintsstcLblDocumentNo.insets =
					new java.awt.Insets(11, 11, 2, 3);
				getJPanel5().add(
					getstcLblDocumentNo(),
					constraintsstcLblDocumentNo);
				java.awt.GridBagConstraints constraintslblDocumentNo =
					new java.awt.GridBagConstraints();
				constraintslblDocumentNo.gridx = 2;
				constraintslblDocumentNo.gridy = 1;
				constraintslblDocumentNo.ipadx = 76;
				constraintslblDocumentNo.insets =
					new java.awt.Insets(11, 6, 2, 40);
				getJPanel5().add(
					getlblDocumentNo(),
					constraintslblDocumentNo);
				java.awt.GridBagConstraints constraintsstcLblIssued =
					new java.awt.GridBagConstraints();
				constraintsstcLblIssued.gridx = 1;
				constraintsstcLblIssued.gridy = 2;
				constraintsstcLblIssued.ipadx = 4;
				constraintsstcLblIssued.insets =
					new java.awt.Insets(2, 57, 2, 3);
				getJPanel5().add(
					getstcLblIssued(),
					constraintsstcLblIssued);
				java
					.awt
					.GridBagConstraints constraintslblTitleIssueDate =
					new java.awt.GridBagConstraints();
				constraintslblTitleIssueDate.gridx = 2;
				constraintslblTitleIssueDate.gridy = 2;
				constraintslblTitleIssueDate.ipadx = 26;
				constraintslblTitleIssueDate.insets =
					new java.awt.Insets(2, 3, 2, 108);
				getJPanel5().add(
					getlblTitleIssueDate(),
					constraintslblTitleIssueDate);
				java.awt.GridBagConstraints constraintslblDocTypeCdDesc =
					new java.awt.GridBagConstraints();
				constraintslblDocTypeCdDesc.gridx = 1;
				constraintslblDocTypeCdDesc.gridy = 3;
				constraintslblDocTypeCdDesc.gridwidth = 2;
				constraintslblDocTypeCdDesc.ipadx = 170;
				constraintslblDocTypeCdDesc.insets =
					new java.awt.Insets(2, 23, 13, 23);
				getJPanel5().add(
					getlblDocTypeCdDesc(),
					constraintslblDocTypeCdDesc);
				// user code begin {1}
				ivjJPanel5.setBounds(307, 92, 300, 71);
				//Border b = new TitledBorder(new EtchedBorder(),
				// "Document");
				//ivjJPanel5.setBorder(b);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjJPanel5;
	}

	/**
	 * Return the JPanel6 property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getJPanel6()
	{
		if (ivjJPanel6 == null)
		{
			try
			{
				ivjJPanel6 = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints18 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints17 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints19 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints20 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints18.insets =
					new java.awt.Insets(1, 16, 1, 15);
				consGridBagConstraints18.ipady = -1;
				consGridBagConstraints18.ipadx = 54;
				consGridBagConstraints18.gridy = 1;
				consGridBagConstraints18.gridx = 0;
				consGridBagConstraints17.insets =
					new java.awt.Insets(7, 16, 1, 15);
				consGridBagConstraints17.ipady = -1;
				consGridBagConstraints17.ipadx = 104;
				consGridBagConstraints17.gridy = 0;
				consGridBagConstraints17.gridx = 0;
				consGridBagConstraints20.insets =
					new java.awt.Insets(2, 16, 8, 15);
				consGridBagConstraints20.ipady = -1;
				consGridBagConstraints20.ipadx = 1;
				consGridBagConstraints20.gridy = 3;
				consGridBagConstraints20.gridx = 0;
				consGridBagConstraints19.insets =
					new java.awt.Insets(2, 16, 1, 15);
				consGridBagConstraints19.ipady = -1;
				consGridBagConstraints19.ipadx = 19;
				consGridBagConstraints19.gridy = 2;
				consGridBagConstraints19.gridx = 0;
				ivjJPanel6.setName("JPanel6");
				ivjJPanel6.setBorder(
					new TitledBorder(new EtchedBorder(), SELECT));
				ivjJPanel6.setLayout(new java.awt.GridBagLayout());
				ivjJPanel6.add(getbtnLien(), consGridBagConstraints17);
				ivjJPanel6.add(
					getbtnOwnerAddress(),
					consGridBagConstraints18);
				ivjJPanel6.add(
					getbtnAdditionalInformation(),
					consGridBagConstraints19);
				ivjJPanel6.add(
					getbtnSpecPltInformation(),
					consGridBagConstraints20);
				ivjJPanel6.setBounds(350, 251, 208, 135);
				ivjJPanel6.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel6.setMinimumSize(
					new java.awt.Dimension(224, 128));
				//Border b = new TitledBorder(new EtchedBorder(),
				// "Select if needed");
				//ivjJPanel6.setBorder(b);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjJPanel6;
	}

	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */

	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setViewportView(
					getlstIndiDescription());
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setOpaque(true);
				ivjJScrollPane1.setBounds(307, 169, 301, 79);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * Return the lblAge property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblAge()
	{
		if (ivjlblAge == null)
		{
			try
			{
				ivjlblAge = new JLabel();
				ivjlblAge.setSize(34, 14);
				ivjlblAge.setName("lblAge");
				//ivjlblAge.setText(DEFLT_AGE);
				ivjlblAge.setMaximumSize(new java.awt.Dimension(7, 14));
				ivjlblAge.setMinimumSize(new java.awt.Dimension(7, 14));
				// user code begin {1}
				ivjlblAge.setLocation(234, 20);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblAge;
	}

	/**
	 * Return the lblBodyVin property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblBodyVin()
	{
		if (ivjlblBodyVin == null)
		{
			try
			{
				ivjlblBodyVin = new JLabel();
				ivjlblBodyVin.setName("lblBodyVin");
				//ivjlblBodyVin.setText(DEFLT_BODY_VIN);
				ivjlblBodyVin.setMaximumSize(
					new java.awt.Dimension(77, 14));
				ivjlblBodyVin.setMinimumSize(
					new java.awt.Dimension(77, 14));
				ivjlblBodyVin.setBounds(91, 111, 163, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblBodyVin;
	}

	/**
	 * Return the lblCapacity property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblCapacity()
	{
		if (ivjlblCapacity == null)
		{
			try
			{
				ivjlblCapacity = new JLabel();
				ivjlblCapacity.setName("lblCapacity");
				//ivjlblCapacity.setText(DEFLT_ZERO);
				ivjlblCapacity.setMaximumSize(
					new java.awt.Dimension(7, 14));
				ivjlblCapacity.setMinimumSize(
					new java.awt.Dimension(7, 14));
				ivjlblCapacity.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblCapacity;
	}

	/**
	 * Return the lblClass property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblClass()
	{
		if (ivjlblClass == null)
		{
			try
			{
				ivjlblClass = new JLabel();
				ivjlblClass.setBounds(73, 65, 200, 14);
				ivjlblClass.setName("lblClass");
				ivjlblClass.setText("");
				ivjlblClass.setMaximumSize(
					new java.awt.Dimension(158, 14));
				ivjlblClass.setMinimumSize(
					new java.awt.Dimension(158, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblClass;
	}

	/**
	 * Return the lblCounty property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblCounty()
	{
		if (ivjlblCounty == null)
		{
			try
			{
				ivjlblCounty = new JLabel();
				ivjlblCounty.setBounds(73, 135, 200, 14);
				ivjlblCounty.setName("lblCounty");
				//ivjlblCounty.setText(DEFLT_CNTY);
				ivjlblCounty.setMaximumSize(
					new java.awt.Dimension(54, 14));
				ivjlblCounty.setMinimumSize(
					new java.awt.Dimension(54, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblCounty;
	}

	/**
	 * Return the lblDocTypeCdDesc property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblDocTypeCdDesc()
	{
		if (ivjlblDocTypeCdDesc == null)
		{
			try
			{
				ivjlblDocTypeCdDesc = new JLabel();
				ivjlblDocTypeCdDesc.setName("lblDocTypeCdDesc");
				//ivjlblDocTypeCdDesc.setText(DEFLT_DOCTYPE);
				ivjlblDocTypeCdDesc.setMaximumSize(
					new java.awt.Dimension(88, 14));
				ivjlblDocTypeCdDesc.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjlblDocTypeCdDesc.setMinimumSize(
					new java.awt.Dimension(88, 14));
				ivjlblDocTypeCdDesc.setHorizontalAlignment(
					SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblDocTypeCdDesc;
	}

	/**
	 * Return the lblDocumentNo property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblDocumentNo()
	{
		if (ivjlblDocumentNo == null)
		{
			try
			{
				ivjlblDocumentNo = new JLabel();
				ivjlblDocumentNo.setName("lblDocumentNo");
				//ivjlblDocumentNo.setText(DEFLT_DOCNO);
				ivjlblDocumentNo.setMaximumSize(
					new java.awt.Dimension(77, 14));
				ivjlblDocumentNo.setMinimumSize(
					new java.awt.Dimension(77, 14));
				ivjlblDocumentNo.setHorizontalAlignment(2);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblDocumentNo;
	}

	/**
	 * Return the lblEmpty property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblEmpty()
	{
		if (ivjlblEmpty == null)
		{
			try
			{
				ivjlblEmpty = new JLabel();
				ivjlblEmpty.setName("lblEmpty");
				//ivjlblEmpty.setText(DEFLT_WEIGHT);
				ivjlblEmpty.setMaximumSize(
					new java.awt.Dimension(28, 14));
				ivjlblEmpty.setMinimumSize(
					new java.awt.Dimension(28, 14));
				ivjlblEmpty.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblEmpty;
	}

	/**
	 * Return the lblExpiresMonth property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblExpiresMonth()
	{
		if (ivjlblExpiresMonth == null)
		{
			try
			{
				ivjlblExpiresMonth = new JLabel();
				ivjlblExpiresMonth.setBounds(73, 43, 21, 14);
				ivjlblExpiresMonth.setName("lblExpiresMonth");
				//ivjlblExpiresMonth.setText(DEFLT_MO);
				ivjlblExpiresMonth.setMaximumSize(
					new java.awt.Dimension(48, 14));
				ivjlblExpiresMonth.setMinimumSize(
					new java.awt.Dimension(48, 14));
				ivjlblExpiresMonth.setHorizontalAlignment(
					SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblExpiresMonth;
	}

	/**
	 * Return the lblExpiresYear property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblExpiresYear()
	{
		if (ivjlblExpiresYear == null)
		{
			try
			{
				ivjlblExpiresYear = new JLabel();
				ivjlblExpiresYear.setSize(43, 14);
				ivjlblExpiresYear.setName("lblExpiresYear");
				//ivjlblExpiresYear.setText(DEFLT_YR);
				ivjlblExpiresYear.setHorizontalAlignment(2);
				// user code begin {1}
				ivjlblExpiresYear.setLocation(112, 43);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblExpiresYear;
	}

	/**
	 * Return the lblGross property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblGross()
	{
		if (ivjlblGross == null)
		{
			try
			{
				ivjlblGross = new JLabel();
				ivjlblGross.setName("lblGross");
				//ivjlblGross.setText(DEFLT_WEIGHT);
				ivjlblGross.setMaximumSize(
					new java.awt.Dimension(28, 14));
				ivjlblGross.setMinimumSize(
					new java.awt.Dimension(28, 14));
				ivjlblGross.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblGross;
	}

	/**
	 * This method initializes ivjlblInsuranceVerified
	 * 
	 * @return JLabel
	 */
	private JLabel getlblInsuranceVerified()
	{
		if (ivjlblInsuranceVerified == null)
		{
			ivjlblInsuranceVerified = new JLabel();
			ivjlblInsuranceVerified.setBounds(12, 28, 150, 17);
			ivjlblInsuranceVerified.setOpaque(true);
			//ivjlblInsuranceVerified.setText(
			//	CommonConstant.NOT_REQUIRED);
			//ivjlblInsuranceVerified.setBackground(new java.awt.Color(255, 0, 0));
			//ivjlblInsuranceVerified.setForeground(new java.awt.Color(255, 255, 255));
			ivjlblInsuranceVerified.setHorizontalAlignment(
				SwingConstants.LEADING);
		}
		return ivjlblInsuranceVerified;
	}

	/**
	 * Return the lblLength property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblLength()
	{
		if (ivjlblLength == null)
		{
			try
			{
				ivjlblLength = new JLabel();
				ivjlblLength.setName("lblLength");
				//ivjlblLength.setText(DEFLT_LEN);
				ivjlblLength.setMaximumSize(
					new java.awt.Dimension(17, 14));
				ivjlblLength.setMinimumSize(
					new java.awt.Dimension(17, 14));
				ivjlblLength.setBounds(225, 181, 41, 14);
				ivjlblLength.setHorizontalAlignment(
					SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblLength;
	}

	/**
	 * Return the lblMake property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblMake()
	{
		if (ivjlblMake == null)
		{
			try
			{
				ivjlblMake = new JLabel();
				ivjlblMake.setName("lblMake");
				//ivjlblMake.setText(DEFLT_MAKE);
				ivjlblMake.setMaximumSize(
					new java.awt.Dimension(31, 14));
				ivjlblMake.setMinimumSize(
					new java.awt.Dimension(31, 14));
				ivjlblMake.setBounds(85, 16, 54, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblMake;
	}

	/**
	 * Return the lblOdometer property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblOdometer()
	{
		if (ivjlblOdometer == null)
		{
			try
			{
				ivjlblOdometer = new JLabel();
				ivjlblOdometer.setName("lblOdometer");
				//ivjlblOdometer.setText(DEFLT_ODOMETER);
				ivjlblOdometer.setMaximumSize(
					new java.awt.Dimension(42, 14));
				ivjlblOdometer.setMinimumSize(
					new java.awt.Dimension(42, 14));
				ivjlblOdometer.setBounds(91, 156, 64, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblOdometer;
	}

	/**
	 * Return the lblOrg property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblOrg()
	{
		if (ivjlblOrg == null)
		{
			ivjlblOrg = new JLabel();
			ivjlblOrg.setBounds(73, 112, 200, 14);
			ivjlblOrg.setText("");
			ivjlblOrg.setName("lblOrg");
		}
		return ivjlblOrg;
	}

	/**
	 * Return the lblOwnerId property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblOwnerId()
	{
		if (ivjlblOwnerId == null)
		{
			try
			{
				ivjlblOwnerId = new JLabel();
				ivjlblOwnerId.setName("lblOwnerId");
				//ivjlblOwnerId.setText(DEFLT_OWNR_ID);
				ivjlblOwnerId.setMaximumSize(
					new java.awt.Dimension(63, 14));
				ivjlblOwnerId.setMinimumSize(
					new java.awt.Dimension(63, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblOwnerId;
	}

	/**
	 * Return the lblOwnerName1 property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblOwnerName1()
	{
		if (ivjlblOwnerName1 == null)
		{
			try
			{
				ivjlblOwnerName1 = new JLabel();
				ivjlblOwnerName1.setName("lblOwnerName1");
				ivjlblOwnerName1.setOpaque(true);
				//ivjlblOwnerName1.setText(DEFLT_OWNR_NAME1);
				ivjlblOwnerName1.setBackground(
					new java.awt.Color(255, 0, 0));
				ivjlblOwnerName1.setMaximumSize(
					new java.awt.Dimension(130, 16));
				ivjlblOwnerName1.setForeground(
					new java.awt.Color(255, 255, 255));
				ivjlblOwnerName1.setFont(
					new java.awt.Font("Arial", 1, 14));
				ivjlblOwnerName1.setMinimumSize(
					new java.awt.Dimension(130, 16));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblOwnerName1;
	}

	/**
	 * Return the lblOwnerName2 property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblOwnerName2()
	{
		if (ivjlblOwnerName2 == null)
		{
			try
			{
				ivjlblOwnerName2 = new JLabel();
				ivjlblOwnerName2.setName("lblOwnerName2");
				//ivjlblOwnerName2.setText(DEFLT_OWNR_NAME2);
				ivjlblOwnerName2.setMaximumSize(
					new java.awt.Dimension(130, 16));
				ivjlblOwnerName2.setMinimumSize(
					new java.awt.Dimension(130, 16));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblOwnerName2;
	}

	/**
	 * Return the lblPlate property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblPlate()
	{
		if (ivjlblPlate == null)
		{
			try
			{
				ivjlblPlate = new JLabel();
				ivjlblPlate.setBounds(73, 20, 64, 14);
				ivjlblPlate.setName("lblPlate");
				//ivjlblPlate.setText(DEFLT_PLTNO);
				ivjlblPlate.setMaximumSize(
					new java.awt.Dimension(49, 14));
				ivjlblPlate.setMinimumSize(
					new java.awt.Dimension(49, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblPlate;
	}

	/**
	 * Return the lblSlash property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblSlash()
	{
		if (ivjlblSlash == null)
		{
			try
			{
				ivjlblSlash = new JLabel();
				ivjlblSlash.setBounds(94, 43, 12, 14);
				ivjlblSlash.setName("lblSlash");
				ivjlblSlash.setText(DEFLT_SLASH);
				ivjlblSlash.setMaximumSize(
					new java.awt.Dimension(3, 14));
				ivjlblSlash.setMinimumSize(
					new java.awt.Dimension(3, 14));
				ivjlblSlash.setHorizontalAlignment(
					SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblSlash;
	}

	/**
	 * Return the lblTitleIssueDate property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblTitleIssueDate()
	{
		if (ivjlblTitleIssueDate == null)
		{
			try
			{
				ivjlblTitleIssueDate = new JLabel();
				ivjlblTitleIssueDate.setName("lblTitleIssueDate");
				//ivjlblTitleIssueDate.setText(DEFLT_DATE);
				ivjlblTitleIssueDate.setMaximumSize(
					new java.awt.Dimension(62, 14));
				ivjlblTitleIssueDate.setMinimumSize(
					new java.awt.Dimension(62, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblTitleIssueDate;
	}

	/**
	 * Return the lblTons property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblTons()
	{
		if (ivjlblTons == null)
		{
			try
			{
				ivjlblTons = new JLabel();
				ivjlblTons.setName("lblTons");
				//ivjlblTons.setText(DEFLT_TONS);
				ivjlblTons.setMaximumSize(
					new java.awt.Dimension(17, 14));
				ivjlblTons.setMinimumSize(
					new java.awt.Dimension(17, 14));
				ivjlblTons.setBounds(225, 156, 41, 14);
				ivjlblTons.setHorizontalAlignment(SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblTons;
	}

	/**
	 * Return the lblTrlrType property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblTrlrType()
	{
		if (ivjlblTrlrType == null)
		{
			try
			{
				ivjlblTrlrType = new JLabel();
				ivjlblTrlrType.setName("lblTrlrType");
				//ivjlblTrlrType.setText(DEFLT_TRLR_TYPE);
				ivjlblTrlrType.setMaximumSize(
					new java.awt.Dimension(29, 14));
				ivjlblTrlrType.setMinimumSize(
					new java.awt.Dimension(29, 14));
				ivjlblTrlrType.setBounds(91, 181, 64, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblTrlrType;
	}

	/**
	 * Return the lblType property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblType()
	{
		if (ivjlblType == null)
		{
			try
			{
				ivjlblType = new JLabel();
				ivjlblType.setBounds(73, 88, 200, 14);
				ivjlblType.setName("lblType");
				ivjlblType.setText("");
				ivjlblType.setMaximumSize(
					new java.awt.Dimension(74, 14));
				ivjlblType.setMinimumSize(
					new java.awt.Dimension(74, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblType;
	}

	/**
	 * Return the lblVehClass property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblVehClass()
	{
		if (ivjlblVehClass == null)
		{
			try
			{
				ivjlblVehClass = new JLabel();
				ivjlblVehClass.setName("lblVehClass");
				//ivjlblVehClass.setText(DEFLT_VEH_CLASS);
				ivjlblVehClass.setMaximumSize(
					new java.awt.Dimension(29, 14));
				ivjlblVehClass.setMinimumSize(
					new java.awt.Dimension(29, 14));
				ivjlblVehClass.setBounds(91, 133, 124, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblVehClass;
	}

	/**
	 * Return the lblVehModel property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblVehModel()
	{
		if (ivjlblVehModel == null)
		{
			try
			{
				ivjlblVehModel = new JLabel();
				ivjlblVehModel.setName("lblVehModel");
				//ivjlblVehModel.setText(DEFLT_MODEL);
				ivjlblVehModel.setMaximumSize(
					new java.awt.Dimension(15, 14));
				ivjlblVehModel.setMinimumSize(
					new java.awt.Dimension(15, 14));
				ivjlblVehModel.setBounds(212, 16, 45, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblVehModel;
	}

	/**
	 * Return the lblVehType property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblVehType()
	{
		if (ivjlblVehType == null)
		{
			try
			{
				ivjlblVehType = new JLabel();
				ivjlblVehType.setName("lblVehType");
				//ivjlblVehType.setText(DEFLT_VEH_TYPE);
				ivjlblVehType.setMaximumSize(
					new java.awt.Dimension(15, 14));
				ivjlblVehType.setMinimumSize(
					new java.awt.Dimension(15, 14));
				ivjlblVehType.setBounds(149, 16, 45, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblVehType;
	}

	/**
	 * Return the lblVin property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblVin()
	{
		if (ivjlblVin == null)
		{
			try
			{
				ivjlblVin = new JLabel();
				ivjlblVin.setName("lblVin");
				//ivjlblVin.setText(DEFLT_VIN);
				ivjlblVin.setMaximumSize(
					new java.awt.Dimension(90, 14));
				ivjlblVin.setMinimumSize(
					new java.awt.Dimension(90, 14));
				ivjlblVin.setBounds(30, 36, 224, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblVin;
	}

	/**
	 * Return the lblYear property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblYear()
	{
		if (ivjlblYear == null)
		{
			try
			{
				ivjlblYear = new JLabel();
				ivjlblYear.setName("lblYear");
				//ivjlblYear.setText(DEFLT_YR);
				ivjlblYear.setMaximumSize(
					new java.awt.Dimension(28, 14));
				ivjlblYear.setMinimumSize(
					new java.awt.Dimension(28, 14));
				ivjlblYear.setBounds(30, 16, 39, 14);
				ivjlblYear.setHorizontalAlignment(SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlblYear;
	}

	/**
	 * Return the lstIndiDescription property value.
	 * 
	 * @return JList
	 */

	private JList getlstIndiDescription()
	{
		if (ivjlstIndiDescription == null)
		{
			try
			{
				ivjlstIndiDescription = new JList();
				ivjlstIndiDescription.setName("lstIndiDescription");
				// defect 9971 
				ivjlstIndiDescription.setFont(
					new java.awt.Font(
						CommonConstant.FONT_JLIST,
						0,
						12));
				// end defect 9971 
				ivjlstIndiDescription.setSelectedIndex(1);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjlstIndiDescription;
	}

	/**
	 * Return the Major Color Code selected in Drop-Down list
	 * 
	 * @return String
	 */
	private String getMajorColorCdSelected()
	{
		String lsColorCd = CommonConstant.STR_SPACE_EMPTY;
		int liSelectedIndx = getcomboMajorColor().getSelectedIndex();
		if (liSelectedIndx > -1)
		{
			lsColorCd = (String) cvVehColor.elementAt(liSelectedIndx);
			lsColorCd = lsColorCd.substring(40);
		}

		return lsColorCd;
	}

	/**
	 * Return the Minor Color Code selected in Drop-Down list
	 * 
	 * @return String
	 */
	private String getMinorColorCdSelected()
	{
		String lsColorCd = CommonConstant.STR_SPACE_EMPTY;
		int liSelectedIndx = getcomboMinorColor().getSelectedIndex();
		// Must be greater than zero since first row is -NONE- selection
		if (liSelectedIndx > 0)
		{
			lsColorCd = (String) cvVehColor.elementAt(liSelectedIndx - 1);
			lsColorCd = lsColorCd.substring(40);
		}
		return lsColorCd;
	}

	/**
	 * Return value of caOrigVehInqData
	 * 
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData getOrigVehInqData()
	{
		return caOrigVehInqData;
	}

	/**
	 * This method initializes ivjpnlInsurance
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlInsurance()
	{
		if (ivjpnlInsurance == null)
		{
			ivjpnlInsurance = new JPanel();
			ivjpnlInsurance.add(getlblInsuranceVerified(), null);
			ivjpnlInsurance.setBounds(178, 375, 114, 57);
			ivjpnlInsurance.setBorder(
				new TitledBorder(new EtchedBorder(), "Insurance:"));

		}
		return ivjpnlInsurance;
	}

	/**
	 * Return the stcLblAge property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblAge()
	{
		if (ivjstcLblAge == null)
		{
			try
			{
				ivjstcLblAge = new JLabel();
				ivjstcLblAge.setBounds(190, 20, 30, 14);
				ivjstcLblAge.setName("stcLblAge");
				ivjstcLblAge.setText(AGE);
				ivjstcLblAge.setMaximumSize(
					new java.awt.Dimension(25, 14));
				ivjstcLblAge.setMinimumSize(
					new java.awt.Dimension(25, 14));
				ivjstcLblAge.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblAge;
	}

	/**
	 * Return the stcLblBodyVin property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblBodyVin()
	{
		if (ivjstcLblBodyVin == null)
		{
			try
			{
				ivjstcLblBodyVin = new JLabel();
				ivjstcLblBodyVin.setName("stcLblBodyVin");
				// defect 11271 
				ivjstcLblBodyVin.setText(BODY_VIN);
				// end defect 11271 
				ivjstcLblBodyVin.setMaximumSize(
					new java.awt.Dimension(53, 14));
				ivjstcLblBodyVin.setMinimumSize(
					new java.awt.Dimension(53, 14));
				ivjstcLblBodyVin.setHorizontalAlignment(4);
				ivjstcLblBodyVin.setBounds(11, 111, 64, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblBodyVin;
	}

	/**
	 * Return the stcLblCapacity property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblCapacity()
	{
		if (ivjstcLblCapacity == null)
		{
			try
			{
				ivjstcLblCapacity = new JLabel();
				ivjstcLblCapacity.setName("stcLblCapacity");
				// defect 11271 
				ivjstcLblCapacity.setText(CAPACITY);
				// end defect 11271 
				ivjstcLblCapacity.setMaximumSize(
					new java.awt.Dimension(52, 14));
				ivjstcLblCapacity.setMinimumSize(
					new java.awt.Dimension(52, 14));
				ivjstcLblCapacity.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblCapacity;
	}

	/**
	 * Return the stcLblClass property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblClass()
	{
		if (ivjstcLblClass == null)
		{
			try
			{
				ivjstcLblClass = new JLabel();
				ivjstcLblClass.setSize(49, 14);
				ivjstcLblClass.setName("stcLblClass");
				ivjstcLblClass.setText(CLASS);
				ivjstcLblClass.setMaximumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblClass.setMinimumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblClass.setHorizontalAlignment(4);
				// user code begin {1}
				ivjstcLblClass.setLocation(11, 65);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblClass;
	}

	/**
	 * Return the stcLblClass2 property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblClass2()
	{
		if (ivjstcLblClass2 == null)
		{
			try
			{
				ivjstcLblClass2 = new JLabel();
				ivjstcLblClass2.setName("stcLblClass2");
				ivjstcLblClass2.setText(CLASS);
				ivjstcLblClass2.setMaximumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblClass2.setMinimumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblClass2.setHorizontalAlignment(4);
				ivjstcLblClass2.setBounds(11, 133, 64, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblClass2;
	}

	/**
	 * Return the stcLblCounty property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblCounty()
	{
		if (ivjstcLblCounty == null)
		{
			try
			{
				ivjstcLblCounty = new JLabel();
				ivjstcLblCounty.setBounds(11, 135, 49, 14);
				ivjstcLblCounty.setName("stcLblCounty");
				ivjstcLblCounty.setText(CNTY);
				ivjstcLblCounty.setMaximumSize(
					new java.awt.Dimension(42, 14));
				ivjstcLblCounty.setMinimumSize(
					new java.awt.Dimension(42, 14));
				ivjstcLblCounty.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblCounty;
	}

	/**
	 * Return the stcLblDocumentNo property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblDocumentNo()
	{
		if (ivjstcLblDocumentNo == null)
		{
			try
			{
				ivjstcLblDocumentNo = new JLabel();
				ivjstcLblDocumentNo.setName("stcLblDocumentNo");
				ivjstcLblDocumentNo.setText(DOCUMENT_NO);
				ivjstcLblDocumentNo.setMaximumSize(
					new java.awt.Dimension(79, 14));
				ivjstcLblDocumentNo.setMinimumSize(
					new java.awt.Dimension(79, 14));
				ivjstcLblDocumentNo.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblDocumentNo;
	}

	/**
	 * Return the stcLblEmpty property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblEmpty()
	{
		if (ivjstcLblEmpty == null)
		{
			try
			{
				ivjstcLblEmpty = new JLabel();
				ivjstcLblEmpty.setName("stcLblEmpty");
				ivjstcLblEmpty.setText(EMPTY);
				ivjstcLblEmpty.setMaximumSize(
					new java.awt.Dimension(38, 14));
				ivjstcLblEmpty.setMinimumSize(
					new java.awt.Dimension(38, 14));
				ivjstcLblEmpty.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblEmpty;
	}

	/**
	 * Return the stcLblExpires property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblExpires()
	{
		if (ivjstcLblExpires == null)
		{
			try
			{
				ivjstcLblExpires = new JLabel();
				ivjstcLblExpires.setSize(49, 14);
				ivjstcLblExpires.setName("stcLblExpires");
				ivjstcLblExpires.setText(EXPIRES);
				ivjstcLblExpires.setMaximumSize(
					new java.awt.Dimension(46, 14));
				ivjstcLblExpires.setMinimumSize(
					new java.awt.Dimension(46, 14));
				ivjstcLblExpires.setHorizontalAlignment(4);
				// user code begin {1}
				ivjstcLblExpires.setLocation(11, 43);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblExpires;
	}

	/**
	 * Return the stcLblGross property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblGross()
	{
		if (ivjstcLblGross == null)
		{
			try
			{
				ivjstcLblGross = new JLabel();
				ivjstcLblGross.setName("stcLblGross");
				ivjstcLblGross.setText(GROSS);
				ivjstcLblGross.setMaximumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblGross.setMinimumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblGross.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblGross;
	}

	/**
	 * Return the stcLblId property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblId()
	{
		if (ivjstcLblId == null)
		{
			try
			{
				ivjstcLblId = new JLabel();
				ivjstcLblId.setName("stcLblId");
				ivjstcLblId.setText(ID);
				ivjstcLblId.setMaximumSize(
					new java.awt.Dimension(10, 14));
				ivjstcLblId.setMinimumSize(
					new java.awt.Dimension(10, 14));
				ivjstcLblId.setHorizontalAlignment(SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblId;
	}

	/**
	 * Return the stcLblIssued property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblIssued()
	{
		if (ivjstcLblIssued == null)
		{
			try
			{
				ivjstcLblIssued = new JLabel();
				ivjstcLblIssued.setName("stcLblIssued");
				ivjstcLblIssued.setText(ISSUED);
				ivjstcLblIssued.setMaximumSize(
					new java.awt.Dimension(41, 14));
				ivjstcLblIssued.setMinimumSize(
					new java.awt.Dimension(41, 14));
				ivjstcLblIssued.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblIssued;
	}

	/**
	 * Return the stcLblLength property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblLength()
	{
		if (ivjstcLblLength == null)
		{
			try
			{
				ivjstcLblLength = new JLabel();
				ivjstcLblLength.setName("stcLblLength");
				ivjstcLblLength.setText(LEN);
				ivjstcLblLength.setMaximumSize(
					new java.awt.Dimension(42, 14));
				ivjstcLblLength.setMinimumSize(
					new java.awt.Dimension(42, 14));
				ivjstcLblLength.setHorizontalAlignment(4);
				ivjstcLblLength.setBounds(161, 181, 45, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblLength;
	}

	/**
	 * Return the stcLblOdometer property value.
	 * 
	 * @return JLabel
	 */

	/**
	 * Return the stcLblColorMajor property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblColorMajor()
	{
		if (ivjstcLblColorMajor == null)
		{
			try
			{
				ivjstcLblColorMajor = new javax.swing.JLabel();
				ivjstcLblColorMajor.setBounds(10, 60, 77, 14);
				ivjstcLblColorMajor.setName("stcLblColorMajor");
				ivjstcLblColorMajor.setText(MAJORCOLOR);
				ivjstcLblColorMajor.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblColorMajor;
	}

	/**
	 * Return the stcLblColorMinor property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblColorMinor()
	{
		if (ivjstcLblColorMinor == null)
		{
			try
			{
				ivjstcLblColorMinor = new javax.swing.JLabel();
				ivjstcLblColorMinor.setBounds(10, 88, 77, 14);
				ivjstcLblColorMinor.setName("stcLblColorMinor");
				ivjstcLblColorMinor.setText(MINORCOLOR);
				ivjstcLblColorMinor.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblColorMinor;
	}

	private JLabel getstcLblOdometer()
	{
		if (ivjstcLblOdometer == null)
		{
			try
			{
				ivjstcLblOdometer = new JLabel();
				ivjstcLblOdometer.setName("stcLblOdometer");
				ivjstcLblOdometer.setText(ODOMETER);
				ivjstcLblOdometer.setMaximumSize(
					new java.awt.Dimension(60, 14));
				ivjstcLblOdometer.setMinimumSize(
					new java.awt.Dimension(60, 14));
				ivjstcLblOdometer.setHorizontalAlignment(4);
				ivjstcLblOdometer.setBounds(11, 156, 64, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblOdometer;
	}

	/**
	 * Return the stcLblOrg property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOrg()
	{
		if (ivjstcLblOrg == null)
		{
			ivjstcLblOrg = new JLabel();
			ivjstcLblOrg.setSize(49, 14);
			ivjstcLblOrg.setText("Org:");
			ivjstcLblOrg.setHorizontalAlignment(SwingConstants.RIGHT);
			ivjstcLblOrg.setName("stcLblOrg");
			ivjstcLblOrg.setLocation(11, 112);
		}
		return ivjstcLblOrg;
	}

	/**
	 * Return the stcLblPlate property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblPlate()
	{
		if (ivjstcLblPlate == null)
		{
			try
			{
				ivjstcLblPlate = new JLabel();
				ivjstcLblPlate.setBounds(11, 20, 49, 14);
				ivjstcLblPlate.setName("stcLblPlate");
				ivjstcLblPlate.setText(PLATE);
				ivjstcLblPlate.setMaximumSize(
					new java.awt.Dimension(32, 14));
				ivjstcLblPlate.setMinimumSize(
					new java.awt.Dimension(32, 14));
				ivjstcLblPlate.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblPlate;
	}

	/**
	 * Return the stcLblTons property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblTons()
	{
		if (ivjstcLblTons == null)
		{
			try
			{
				ivjstcLblTons = new JLabel();
				ivjstcLblTons.setName("stcLblTons");
				ivjstcLblTons.setText(TONS);
				ivjstcLblTons.setMaximumSize(
					new java.awt.Dimension(31, 14));
				ivjstcLblTons.setMinimumSize(
					new java.awt.Dimension(31, 14));
				ivjstcLblTons.setHorizontalAlignment(4);
				ivjstcLblTons.setBounds(161, 156, 45, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblTons;
	}

	/**
	 * Return the stcLblTrlrType property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblTrlrType()
	{
		if (ivjstcLblTrlrType == null)
		{
			try
			{
				ivjstcLblTrlrType = new JLabel();
				ivjstcLblTrlrType.setName("stcLblTrlrType");
				ivjstcLblTrlrType.setText(TRLR_TYPE);
				ivjstcLblTrlrType.setMaximumSize(
					new java.awt.Dimension(53, 14));
				ivjstcLblTrlrType.setMinimumSize(
					new java.awt.Dimension(53, 14));
				ivjstcLblTrlrType.setHorizontalAlignment(4);
				ivjstcLblTrlrType.setBounds(11, 181, 64, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblTrlrType;
	}

	/**
	 * Return the stcLblType property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblType()
	{
		if (ivjstcLblType == null)
		{
			try
			{
				ivjstcLblType = new JLabel();
				ivjstcLblType.setSize(49, 14);
				ivjstcLblType.setName("stcLblType");
				ivjstcLblType.setText(TYPE);
				ivjstcLblType.setMaximumSize(
					new java.awt.Dimension(30, 14));
				ivjstcLblType.setMinimumSize(
					new java.awt.Dimension(30, 14));
				ivjstcLblType.setHorizontalAlignment(4);
				// user code begin {1}
				ivjstcLblType.setLocation(11, 88);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjstcLblType;
	}
	/**
	 * Return copy of caVehicleInquiryData
	 * 
	 * @return Object
	 */
	private Object getVehInqDataCopy()
	{
		Object laCopyVehInqData =
			UtilityMethods.copy(caVehicleInquiryData);

		// Copy Validation Object
		Object laCopyRegValidData =
			UtilityMethods.copy(
				caVehicleInquiryData.getValidationObject());

		// Set Validation Object
		((VehicleInquiryData) laCopyVehInqData).setValidationObject(
			laCopyRegValidData);

		// Copy Registration Data
		Object laCopyRegData =
			UtilityMethods.copy(
				caVehicleInquiryData.getMfVehicleData().getRegData());

		// Set Registration Data
		((VehicleInquiryData) laCopyVehInqData)
			.getMfVehicleData()
			.setRegData(
			(RegistrationData) laCopyRegData);

		// Copy Special Plates Regis Data 
		Object laCopySpclPltRegData =
			UtilityMethods.copy(
				caVehicleInquiryData
					.getMfVehicleData()
					.getSpclPltRegisData());

		// Set Special Plates Regis Data 
		((VehicleInquiryData) laCopyVehInqData)
			.getMfVehicleData()
			.setSpclPltRegisData(
				(SpecialPlatesRegisData) laCopySpclPltRegData);

		// Copy Vehicle Data
		Object laCopyVehicleData =
			UtilityMethods.copy(
				caVehicleInquiryData
					.getMfVehicleData()
					.getVehicleData());

		((VehicleInquiryData) laCopyVehInqData)
			.getMfVehicleData()
			.setVehicleData((VehicleData) laCopyVehicleData);

		return laCopyVehInqData;

	}

	/**
	 * Process the cancel button or escape key event.
	 * 
	 * @return boolean true if 'Yes' is selected in Confirmation window
	 */
	private boolean handleCancel()
	{
		if (getController()
			.getTransCode()
			.equals(TransCdConstant.ADDR))
		{
			RTSException aeRTSException =
				new RTSException(
					RTSException.CTL001,
					MSG_DYWT_CANCEL,
					null);
			int liButtonSelected = aeRTSException.displayError(this);
			if (liButtonSelected == RTSException.NO)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		/* Uncomment the following lines to print uncaught exceptions to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7894
		RTSException aeRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		aeRTSEx.displayError(this);
		// end defect 7894
	}

	/**
	 * Handle management of annual plate replacement.
	 * Determines whether need to display sticker selection REG001
	 * 
	 */
	private void handleReplAnnualPlt()
	{
		caRegValidData.setNewPltReplIndi(1);

		// get annual plate indicator for the repl plt cd
		PlateTypeData laPlateTypeData =
			PlateTypeCache.getPlateType(
				caRegistrationData.getRegPltCd());

		//if (laRegRenwlData.getAnnualPltIndi() == 0)

		// If Not Special Plate, proceed to issue 
		if (laPlateTypeData
			.getPltOwnrshpCd()
			.equals(SpecialPlatesConstant.VEHICLE))
		{
			// replace annual plt with annual plt
			caRegistrationData.setRegPltCd(
				caRegValidData.getReplPltCd());

			caRegValidData.setInvItms(null);

			RegistrationClientBusiness.procsReplPltInv(
				caVehicleInquiryData);
			cbContinueWithReplacement = true;
		}
		else
		{
			getController().processData(
				VCRegistrationREG003.PLATE_SELECTION,
				caVehicleInquiryData);
		}
	}

	/**
	 * Initialize the class.
	 */

	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName("FrmRegistrationREG003");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(630, 482);
			setTitle(TITLE_REG003);
			setContentPane(getFrmRegistrationREG003ContentPane1());
		}
		catch (Throwable aeException)
		{
			handleException(aeException);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Checks to see if Major Color selected is same as Minor Color.
	 * 
	 * @return boolean
	 */
	private boolean isMajorColorSameAsMinorColor()
	{
		boolean lbRet = false;

		String lsMajorColor = getMajorColorCdSelected();
		String lsMinorColor = getMinorColorCdSelected();
		if (!(lsMajorColor.equals(CommonConstant.STR_SPACE_EMPTY))
				&& (lsMajorColor.equalsIgnoreCase(lsMinorColor)))
		{
				lbRet = true;
		}
		
		return lbRet;
	}

	/**
	 * Checks to see if Minor Color is selected but Major Color is 
	 * not selected.
	 * 
	 * @return boolean
	 */
	private boolean isMinorColorSelectedMajorColorNotSelected()
	{
		boolean lbRet = false;

		String lsMajorColor = getMajorColorCdSelected();
		String lsMinorColor = getMinorColorCdSelected();
		if (lsMajorColor.equals(CommonConstant.STR_SPACE_EMPTY) 
			&& !(lsMinorColor.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
				lbRet = true;
		}
		
		return lbRet;
	}

	/** 
	 * Method to determine if "Out of County" so that prompt for 
	 * Supervisor Override Code 
	 * 
	 * @return boolean 
	 */
	private boolean isOutOfCounty()
	{
		// defect 10112 
		return caRegistrationData.getResComptCntyNo()
			!= caOrigVehInqData
				.getMfVehicleData()
				.getRegData()
				.getResComptCntyNo()
			&& caOwnerData.getAddressData().getSt1().equals(
				caOrigVehInqData
					.getMfVehicleData()
					.getOwnerData()
					.getAddressData()
					.getSt1())
			&& caOwnerData.getAddressData().getSt2().equals(
				caOrigVehInqData
					.getMfVehicleData()
					.getOwnerData()
					.getAddressData()
					.getSt2())
			&& caOwnerData.getAddressData().getCity().equals(
				caOrigVehInqData
					.getMfVehicleData()
					.getOwnerData()
					.getAddressData()
					.getCity())
			&& caOwnerData.getAddressData().getState().equals(
				caOrigVehInqData
					.getMfVehicleData()
					.getOwnerData()
					.getAddressData()
					.getState());
		// end defect 10112 
	}

	/**
	 * Determines which radio button is currently selected. 
	 * Then depending on which arrow key is pressed, it sets that
	 * radio button selected and requests focus.
	 *
	 * @param aaKE KeyEvent the KeyEvent captured by the KeyListener
	 */
	public void keyPressed(java.awt.event.KeyEvent aaKE)
	{
		super.keyPressed(aaKE);
		if (aaKE.getSource() instanceof RTSButton)
		{
			// defect 9085 
			// handle Special Plate Information Button 
			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
			{
				if (getbtnLien().hasFocus())
				{
					if (getbtnOwnerAddress().isEnabled())
					{
						getbtnOwnerAddress().requestFocus();
					}
					else if (getbtnAdditionalInformation().isEnabled())
					{
						getbtnAdditionalInformation().requestFocus();
					}
					else if (getbtnSpecPltInformation().isEnabled())
					{
						getbtnSpecPltInformation().requestFocus();
					}
				}
				else if (getbtnOwnerAddress().hasFocus())
				{
					if (getbtnAdditionalInformation().isEnabled())
					{
						getbtnAdditionalInformation().requestFocus();
					}
					else if (getbtnSpecPltInformation().isEnabled())
					{
						getbtnSpecPltInformation().requestFocus();
					}
					else if (getbtnLien().isEnabled())
					{
						getbtnLien().requestFocus();
					}
				}
				else if (getbtnAdditionalInformation().hasFocus())
				{
					if (getbtnSpecPltInformation().isEnabled())
					{
						getbtnSpecPltInformation().requestFocus();
					}
					else if (getbtnLien().isEnabled())
					{
						getbtnLien().requestFocus();
					}
					else if (getbtnOwnerAddress().isEnabled())
					{
						getbtnOwnerAddress().requestFocus();
					}
				}
				else if (getbtnSpecPltInformation().hasFocus())
				{
					if (getbtnLien().isEnabled())
					{
						getbtnLien().requestFocus();
					}
					else if (getbtnOwnerAddress().isEnabled())
					{
						getbtnOwnerAddress().requestFocus();
					}
					else if (getbtnAdditionalInformation().isEnabled())
					{
						getbtnAdditionalInformation().requestFocus();
					}
				}
				aaKE.consume();
			}
			else if (
				aaKE.getKeyCode() == KeyEvent.VK_LEFT
					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
			{
				if (getbtnLien().hasFocus())
				{
					if (getbtnSpecPltInformation().isEnabled())
					{
						getbtnSpecPltInformation().requestFocus();
					}
					else if (getbtnAdditionalInformation().isEnabled())
					{
						getbtnAdditionalInformation().requestFocus();
					}
					else if (getbtnOwnerAddress().isEnabled())
					{
						getbtnOwnerAddress().requestFocus();
					}
				}
				else if (getbtnOwnerAddress().hasFocus())
				{
					if (getbtnLien().isEnabled())
					{
						getbtnLien().requestFocus();
					}
					else if (getbtnSpecPltInformation().isEnabled())
					{
						getbtnSpecPltInformation().requestFocus();
					}
					else if (getbtnAdditionalInformation().isEnabled())
					{
						getbtnAdditionalInformation().requestFocus();
					}
				}
				else if (getbtnAdditionalInformation().hasFocus())
				{
					if (getbtnOwnerAddress().isEnabled())
					{
						getbtnOwnerAddress().requestFocus();
					}
					else if (getbtnLien().isEnabled())
					{
						getbtnLien().requestFocus();
					}
					else if (getbtnSpecPltInformation().isEnabled())
					{
						getbtnSpecPltInformation().requestFocus();
					}
				}
				else if (getbtnSpecPltInformation().hasFocus())
				{
					if (getbtnAdditionalInformation().isEnabled())
					{
						getbtnAdditionalInformation().requestFocus();
					}
					else if (getbtnOwnerAddress().isEnabled())
					{
						getbtnOwnerAddress().requestFocus();
					}
					else if (getbtnLien().isEnabled())
					{
						getbtnLien().requestFocus();
					}
				}
				// end defect 9085 
				aaKE.consume();
			}
		}
	}

	/**
	 * Key Released
	 *
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		super.keyReleased(aaKE);
	}

	/**
	 * Fills the Select Fee combo box with the VehicleColorData.
	 */
	private void populateMajorColor()
	{
		if (getcomboMajorColor().isEnabled())
		{
			VehicleData laVehData =
				caVehicleInquiryData.getMfVehicleData().getVehicleData();
			String lsVehMajorColorCd = laVehData.getVehMjrColorCd();
			int liColorDefault = -1;
			Vector lvComboVal = new Vector();
			if (cvVehColor != null)
			{
				for (int liIndex = ZERO;
					liIndex < cvVehColor.size();
					liIndex++)
				{
					String lsVehColorCd = cvVehColor
									.get(liIndex)
										.toString()
											.substring(40);
					if (lsVehMajorColorCd != null
							&& lsVehColorCd.equals(lsVehMajorColorCd)
								&& liColorDefault == -1)
					{
						liColorDefault = liIndex;
					}
					String lsDesc =
						((String) cvVehColor.get(liIndex))
												.substring(0, 30);
					lvComboVal.add(lsDesc);
				}
			}
			DefaultComboBoxModel laDCBM =
				new DefaultComboBoxModel(lvComboVal);
			getcomboMajorColor().setModel(laDCBM);
			getcomboMajorColor().setSelectedIndex(liColorDefault);
			// defect 8479
			comboBoxHotKeyFix(getcomboMajorColor());
			// end defect 8479
		}
	}

	/**
	 * Fills the Minor Color combo box with the VehicleColorData.
	 */
	private void populateMinorColor()
	{
		if (getcomboMinorColor().isEnabled())
		{
			VehicleData laVehData =
				caVehicleInquiryData.getMfVehicleData().getVehicleData();
			String lsVehMinorColorCd = laVehData.getVehMnrColorCd();
			int liColorDefault = -1;
			Vector lvComboVal = new Vector();
			if (cvVehColor != null)
			{
				for (int liIndex = ZERO;
					liIndex < cvVehColor.size();
					liIndex++)
				{
					String lsVehColorCd = cvVehColor
									.get(liIndex)
										.toString()
											.substring(40);
					if (lsVehMinorColorCd != null
							&& lsVehColorCd.equals(lsVehMinorColorCd)
								&& liColorDefault == -1)
					{
						// Add 1 due to row with "- NONE -" being added
						liColorDefault = liIndex + 1;
					}
					String lsDesc =
						((String) cvVehColor.get(liIndex))
												.substring(0, 30);
					lvComboVal.add(lsDesc);
				}
				String lsDesc = "- NONE -";
				lvComboVal.insertElementAt(lsDesc, 0);
			}
			DefaultComboBoxModel laDCBM =
				new DefaultComboBoxModel(lvComboVal);
			getcomboMinorColor().setModel(laDCBM);
			getcomboMinorColor().setSelectedIndex(liColorDefault);
			// defect 8479
			comboBoxHotKeyFix(getcomboMinorColor());
			// end defect 8479
		}
	}

	/**
	 * Fills the Vehicle Color vector with VehicleColorData.
	 */
	private void populateVehColorVector()
	{
		cvVehColor = VehicleColorCache.getVehColorVec();
		UtilityMethods.sort(cvVehColor);
		if (cvVehColor != null)
		{
			for (int liIndex = ZERO;
				liIndex < cvVehColor.size();
				liIndex++)
			{
				VehicleColorData laVehColor =
					(VehicleColorData) cvVehColor.get(liIndex);
				String lsDesc =
					UtilityMethods.addPaddingRight(
						laVehColor.getVehColorDesc(),
						40,
						CommonConstant.STR_SPACE_ONE)
					+ laVehColor.getVehColorCd();
				cvVehColor.setElementAt(lsDesc, liIndex);
			}
		}
	}

	/**
	 * Prepares the data needed for calling calcFees().
	 * 
	 */
	private void prepFees()
	{
		caCompTransData =
			RegistrationClientUtilityMethods.prepFees(
				caVehicleInquiryData,
				caOrigVehInqData);
	}

	/**
	 * handle creation of inventory items needed for inventory module.
	 * 
	 */
	private void prepInv()
	{
		caRegValidData.setInvItms(null);
		int liNewStkrReplIndi = 0;

		// Determine if plate and/or sticker required by getting
		// annual plate indicator

		PlateTypeData laPlateTypeData =
			PlateTypeCache.getPlateType(
				caRegistrationData.getRegPltCd());

		//if (laRegRenwlData.getAnnualPltIndi() !=0)
		if (laPlateTypeData.getAnnualPltIndi() == 1)
		{
			// Set new plate indicator to yes
			caRegValidData.setNewPltReplIndi(1);
		}
		else
		{
			// defect 10623
			// Do not want to print sticker for Token Trailer (even 
			// though it's not an annual plate).
//			if (caRegistrationData.getRegClassCd() != 
//				RegistrationConstant.REGCLASSCD_TOKEN_TRLR)
//			{
				liNewStkrReplIndi = 1;
//			}
			// end defect 10623
		}

		// Add sticker to inventory list if required
		if (liNewStkrReplIndi == 1)
		{
			RegistrationClientUtilityMethods.addItmCdToInv(
				caRegValidData,
				caRegistrationData.getRegStkrCd());
		}

		SpecialPlatesRegisData laSpclPltRegisData =
			caMFVehicleData.getSpclPltRegisData();

		// Reset Mfg Request 
		if (laSpclPltRegisData != null)
		{
			laSpclPltRegisData.setMfgSpclPltIndi(0);

			if (caRegValidData.getNewPltReplIndi() == 1)
			{
				// defect 9864 
				// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
				// defect 9223
				// Included Annual Plates in check
				// Different Plate Added or Annual
				boolean lbMfgNewAnnual =
					laPlateTypeData.getAnnualPltIndi() == 1
						&& CommonValidations.isRegistrationExpired(
							laSpclPltRegisData.getPltExpMo(),
							laSpclPltRegisData.getPltExpYr());
				// end defect 9864 

				if (laSpclPltRegisData.isEnterOnSPL002()
					|| laPlateTypeData.getAnnualPltIndi() == 1)
				{
					// This may be disregarded later! 
					RegistrationClientUtilityMethods.addItmCdToInv(
						caRegValidData,
						caRegistrationData.getRegPltCd(),
						laSpclPltRegisData.getRegPltNo());

					if (lbMfgNewAnnual
						|| (laPlateTypeData.getAnnualPltIndi() != 1
							&& laSpclPltRegisData.getRegPltAge(true)
								>= laPlateTypeData.getMandPltReplAge()))
					{
						// defect 9299 
						// Do not remanufacture if sunsetted
						String lsOrgNo =
							laSpclPltRegisData.getOrgNo().trim();
						if (!OrganizationNumberCache
							.isSunsetted(
								caRegistrationData.getRegPltCd(),
								lsOrgNo))
						{
							laSpclPltRegisData.setMfgSpclPltIndi(1);
						}
						// end defect 9299
					}
				}
				else
				{
					laSpclPltRegisData.setMfgSpclPltIndi(1);
				}
				// end defect 9223 

			}
		}
		else if (caRegValidData.getNewPltReplIndi() == 1)
		{
			RegistrationClientUtilityMethods.addItmCdToInv(
				caRegValidData,
				caRegistrationData.getRegPltCd());
			caRegistrationData.setRegStkrNo("");
		}
	}

	/**
	 * Determine if any hard stops exist and have been cleared.
	 * 
	 * @return boolean true if 'Enter' is selected in VTR authorization
	 */
	private boolean procsHardStops()
	{
		boolean lbReturn = true;

		if (caVehicleInquiryData.isMFUp())
		{
			// Test if workstation is located in COUNTY
			if (!SystemProperty.isHQ()
				|| !getController().getTransCode().equals(
					TransCdConstant.EXCH))
			{
				// test for existence of hard stops
				// if indiStopCd == "H"
				Vector lvIndis =
					IndicatorLookup.getIndicators(
						caMFVehicleData,
						caRegValidData.getTransCode(),
						IndicatorLookup.SCREEN);

				if (IndicatorLookup.hasHardStop(lvIndis))
				{
					VehMiscData laVehMiscData =
						caVehicleInquiryData.getVehMiscData();

					if (UtilityMethods
						.isEmpty(laVehMiscData.getAuthCd()))
					{
						getController().processData(
							VCRegistrationREG003.VTR_AUTHORIZATION,
							caVehicleInquiryData);

						// check if 'Enter' is selected in VTR authorization
						lbReturn =
							!UtilityMethods.isEmpty(
								laVehMiscData.getAuthCd());
					}
				}
			}
		}
		return lbReturn;
	}

	/**
	 * Check for soft stops.
	 * 
	 * @return boolean true if 'Enter' is selected in Supervisor Override
	 */
	private boolean procsSoftStops()
	{
		boolean lbReturn = true;

		// Test if workstation is located not at HEADQUARTERS
		if (caVehicleInquiryData.isMFUp()
			&& !SystemProperty.isHQ()
			&& UtilityMethods.isEmpty(
				caVehicleInquiryData.getVehMiscData().getSupvOvride()))
		{
			// if soft stops exist  (indiStopCd == "S") 
			Vector lvIndis =
				IndicatorLookup.getIndicators(
					caMFVehicleData,
					caRegValidData.getTransCode(),
					IndicatorLookup.SCREEN);

			String lsSoftStopReasons = CommonConstant.STR_SPACE_EMPTY;

			// Disregard Out of County for REPL, DUPL
			String lsTransCd = caRegValidData.getTransCode();

			if (!lsTransCd.equals(TransCdConstant.REPL)
				&& !lsTransCd.equals(TransCdConstant.DUPL)
				&& isOutOfCounty())
			{
				lsSoftStopReasons =
					RegistrationConstant.OUT_OF_COUNTY + "\n";
			}

			if (IndicatorLookup.hasSoftStop(lvIndis)
				|| lsSoftStopReasons.length() != 0)
			{
				lsSoftStopReasons =
					lsSoftStopReasons
						+ IndicatorLookup.getSoftStopReasons(lvIndis);

				// below code changed for the VE	
				VehMiscData laVehMiscData = caVehicleInquiryData.getVehMiscData();
				laVehMiscData.setSupvOvrideReason(lsSoftStopReasons); 
//				caVehicleInquiryData
//					.getVehMiscData()
//					.setSupvOvrideReason(
//					lsSoftStopReasons);
				// end VE change

				getController().processData(
					VCRegistrationREG003.SUPV_OVRIDE,
					caVehicleInquiryData);

				lbReturn =
					!UtilityMethods.isEmpty(
						caVehicleInquiryData
							.getVehMiscData()
							.getSupvOvride());
			}
		}
		return lbReturn;
	}

	/**
	 * Reset Registration data if registration invalid indicator is 
	 * equal to 1.
	 * 
	 */
	private void resetRegInfo()
	{
		if (caRegistrationData.getRegInvldIndi() == 1)
		{
			caRegValidData.setInvalidClassPltStkrIndi(1);
			caRegistrationData.setRegInvldIndi(0);
			caRegistrationData.setRegClassCd(0);
			caRegistrationData.setRegEffDt(0);
			caRegistrationData.setRegExpMo(0);
			caRegistrationData.setRegExpYr(0);
			caRegistrationData.setRegPltAge(0);
			caRegistrationData.setRegPltCd("");
			caRegistrationData.setRegPltNo("");
			caRegistrationData.setRegStkrCd("");
			caRegistrationData.setRegStkrNo("");
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 *
	 * @param aaDataObject Object 
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			if (aaDataObject != null)
			{
				if (aaDataObject instanceof Vector)
				{
					Vector lvIsNextVCREG029 = (Vector) aaDataObject;
					if (lvIsNextVCREG029 != null)
					{
						if (lvIsNextVCREG029.size() == 2)
						{
							// determine next vc if NOT reg029
							// 1st element = flag whether to go to REG029
							if (lvIsNextVCREG029.get(0)
								instanceof Boolean)
							{
								getController().processData(
									VCRegistrationREG003
										.REDIRECT_IS_NEXT_VC_REG029,
									lvIsNextVCREG029);
							}
							else if (
								lvIsNextVCREG029.get(0)
									instanceof String)
							{
								getController().processData(
									VCRegistrationREG003
										.REDIRECT_NEXT_VC,
									lvIsNextVCREG029);
							}
						}
						else
						{
							new RTSException(
								RTSException.FAILURE_MESSAGE,
								ERRMSG_DATA_MISS,
								ERRMSG_ERROR).displayError(
								this);
							return;
						}
					}
				}
				else
				{
					// defect 8900
					// Use assignment vs. set methods
					caVehicleInquiryData =
						(VehicleInquiryData) aaDataObject;
					caMFVehicleData =
						caVehicleInquiryData.getMfVehicleData();
					caTitleData = caMFVehicleData.getTitleData();
					caOwnerData = caMFVehicleData.getOwnerData();
					caRegistrationData = caMFVehicleData.getRegData();
					caVehicleData = caMFVehicleData.getVehicleData();
					// end defect 8900 

					// defect 10127 
					csTransCd = getController().getTransCode();

					Object laObject =
						caVehicleInquiryData.getValidationObject();

					if (laObject instanceof RegistrationValidationData)
					{
						// defect 8900
						caRegValidData =
							(RegistrationValidationData) laObject;
						// end defect 8900 
					}

					if (caOrigVehInqData == null)
					{
						// defect 9459
						// determine Insurance panel setting
						if (!(csTransCd.equals(TransCdConstant.RENEW)
							|| csTransCd.equals(TransCdConstant.VEHINQ)))
						{
							getpnlInsurance().setVisible(false);
						}
						else
						{
							if (caRegistrationData == null
								|| !caRegistrationData
									.isInsuranceRequired())
							{
								getlblInsuranceVerified().setText(
									CommonConstant.NOT_REQUIRED);
							}
							else if (
								caRegistrationData
									.isInsuranceVerified())
							{
								getlblInsuranceVerified().setText(
									CommonConstant.VERIFIED);
								getlblInsuranceVerified().setEnabled(
									true);
							}
							else
							{
								getlblInsuranceVerified().setText(
									CommonConstant.VERIFY_MANUALLY);
								getlblInsuranceVerified().setEnabled(
									true);
								getlblInsuranceVerified()
									.setForeground(
									java.awt.Color.WHITE);
								getlblInsuranceVerified()
									.setBackground(
									java.awt.Color.RED);
							}
						}
						// end defect 9459
						if (csTransCd.equals(TransCdConstant.RENEW)
							&& caRegistrationData.getRegInvldIndi() == 1)
						{
							caRegistrationData.setRegExpMo(
								(
									(VCRegistrationREG003) getController())
									.getOrigVehInqData()
									.getMfVehicleData()
									.getRegData()
									.getRegExpMo());
							caRegistrationData.setRegExpYr(
								(
									(VCRegistrationREG003) getController())
									.getOrigVehInqData()
									.getMfVehicleData()
									.getRegData()
									.getRegExpYr());
						}
						// defect 8900
						caOrigVehInqData =
							(VehicleInquiryData) UtilityMethods.copy(
								aaDataObject);
						// end defect 8900 
						caRegValidData.setOrigVehInqData(
							getOrigVehInqData());

						// save resident county, owner address and
						// renewal mailing address data for testing if
						// changed in Owner Address
						caRegValidData.setOrigResComptCntyNo(
							caRegistrationData.getResComptCntyNo());
						caRegValidData.setOrigOwnerData(caOwnerData);
						caRegValidData.setOrigRenwlMailAddr(
							caRegistrationData.getRenwlMailAddr());
						// save vehicle class code for testing in
						// Additional Info
						caRegValidData.setOrigVehClassCd(
							caVehicleData.getVehClassCd());
						caRegValidData.setOrigRegClassCd(
							caRegistrationData.getRegClassCd());
						caRegValidData.setOrigRegPltCd(
							caRegistrationData.getRegPltCd());
						caRegValidData.setOrigRegStkrCd(
							caRegistrationData.getRegStkrCd());

						// defect 9126
						// Default to 'charge fee' for Special Plate on 
						// first time through (may be reset on SPL002).
						if (caMFVehicleData.getSpclPltRegisData()
							!= null)
						{
							caMFVehicleData
								.getSpclPltRegisData()
								.setSpclPltChrgFeeIndi(
								1);
						}
						// end defect 9126			
					}
					// if mainframe is down hide issue date and
					// doc type desc.
					// defect 10127 
					// Use Indicator 
					cbMFUp = caVehicleInquiryData.isMFUp();
					if (cbMFUp)
					{
						// get indicator descriptions
						getIndicators();
					}
					// if registration is invalid then reset
					// registration data for Renewal event.
					if (csTransCd.equals(TransCdConstant.RENEW)
						&& caRegistrationData.getRegInvldIndi() == 1)
					{
						resetRegInfo();
					}
					/* get and set registration data */
					getlblPlate().setText(
						caRegistrationData.getRegPltNo());
					// defect 8901

					// defect 9085 / 9086 / 9123
					// If a SpecialPlate is attached, use the SpecialPlate 
					// Age for Renewal 
					boolean lbRenewal =
						csTransCd.equals(TransCdConstant.RENEW);
					int liAge =
						caRegistrationData.getRegPltAge(lbRenewal);

					SpecialPlatesRegisData laSpecialPlatesRegData =
						caMFVehicleData.getSpclPltRegisData();

					String lsOrgNo = new String();

					// Note:  MF will reset the Record Plate Age upon 
					//  processing 
					if (laSpecialPlatesRegData != null)
					{
						getbtnSpecPltInformation().setEnabled(true);
						lsOrgNo = laSpecialPlatesRegData.getOrgNo();

						if (laSpecialPlatesRegData.isMFDownSP())
						{
							liAge =
								laSpecialPlatesRegData.getRegPltAge(
									false);
						}
						else if (
							laSpecialPlatesRegData.getPltBirthDate()
								!= 0)
						{
							liAge =
								laSpecialPlatesRegData.getRegPltAge(
									lbRenewal);
						}
						//	Populate with SP-RegPltNo
						getlblPlate().setText(
							laSpecialPlatesRegData.getRegPltNo());
					}
					else
					{
						getbtnSpecPltInformation().setEnabled(false);
						lsOrgNo = new String();
					}

					// end defect 9085 / 9086 / 9123
					// end defect 8901 

					// defect 3315 
					// removed VEHINQ test.
					if ((csTransCd.equals(TransCdConstant.RENEW)
						&& caRegValidData.getInvalidClassPltStkrIndi()
							== 1))
						//|| lsTransCd.equals(TransCdConstant.VEHINQ)
					{
						getlblAge().setText(
							liAge == 0 ? "" : Integer.toString(liAge));
					}
					else
					{
						getlblAge().setText(Integer.toString(liAge));
					}
					// end defect 3315 
					int liRegExpMo = caRegistrationData.getRegExpMo();
					getlblExpiresMonth().setText(
						liRegExpMo == 0
							? ""
							: Integer.toString(liRegExpMo));
					int liRegExpYr = caRegistrationData.getRegExpYr();
					getlblExpiresYear().setText(
						liRegExpYr == 0
							? ""
							: Integer.toString(liRegExpYr));
					String lsCntyNo =
						Integer.toString(
							caRegistrationData.getResComptCntyNo());
					getlblCounty().setText(
						lsCntyNo.equals(DEFLT_ZERO) ? "" : lsCntyNo);
					if (getController()
						.getTransCode()
						.equals(TransCdConstant.ADDR)
						|| getController().getTransCode().equals(
							TransCdConstant.RNR))
					{
						getbtnAdditionalInformation().setEnabled(false);
					}
					if (csTransCd.equals(TransCdConstant.HOTCK)
						|| csTransCd.equals(TransCdConstant.HOTDED)
						|| csTransCd.equals(TransCdConstant.CKREDM)
						|| csTransCd.equals(TransCdConstant.HCKITM)
						|| csTransCd.equals(TransCdConstant.REFUND)
						|| csTransCd.equals(TransCdConstant.VEHINQ))
					{
						caVehicleInquiryData.setRTSEffDt(
							new RTSDate().getYYYYMMDDDate());
					}
					// get and set the class code description from cache
					// to display 
					int liRegClassCd =
						caRegistrationData.getRegClassCd();
					if (csTransCd.equals(TransCdConstant.RENEW)
						&& caRegValidData.getInvalidClassPltStkrIndi()
							== 1)
					{
						// do not retrieve reg class and item code
						// descriptions
					}
					else
					{
						CommonFeesData laCommonFeesData =
							CommonFeesCache.getCommonFee(
								liRegClassCd,
								caVehicleInquiryData.getRTSEffDt());
						if (laCommonFeesData != null)
						{
							getlblClass().setText(
								laCommonFeesData.getRegClassCdDesc());
						}
						else
						{
							getlblClass().setText("");
						}
						// get and set the plate type description from
						// cache to display
						
						// defect 11052 
						String lsRegPltCd =
							UtilityMethods.nullSafe(caRegistrationData.getRegPltCd());
						// end defect 11052

						ItemCodesData laItemCodesData =
							ItemCodesCache.getItmCd(lsRegPltCd);
						if (laItemCodesData != null)
						{
							getlblType().setText(
								laItemCodesData.getItmCdDesc());
						}
						else
						{
							getlblType().setText("");
						}
						// defect 9123
						// Get Organization Name  
						String lsOrgName = new String();
						if (laSpecialPlatesRegData != null
							&& lsRegPltCd != null
							&& lsRegPltCd.trim().length() != 0
							&& lsOrgNo != null)
						{
							// defect 9912
							// remove reference to RegisData.setOrgNo() 
							//caRegistrationData.setOrgNo(lsOrgNo);
							// end defect 9912
							lsOrgName =
								OrganizationNumberCache.getOrgName(
									lsRegPltCd,
									lsOrgNo);
						}
						getlblOrg().setText(lsOrgName);
						// end defect 9123
					}
					// get and set the office name from cache to display
					int liOfcNo =
						caRegistrationData.getResComptCntyNo();
					OfficeIdsData laOfficeIdsData =
						OfficeIdsCache.getOfcId(liOfcNo);
					if (laOfficeIdsData != null)
					{
						getlblCounty().setText(
							Integer.toString(liOfcNo)
								+ "   "
								+ laOfficeIdsData.getOfcName());
					}

					/* get and set vehicle data */
					getlblYear().setText(
						Integer.toString(caVehicleData.getVehModlYr()));
					getlblMake().setText(caVehicleData.getVehMk());
					getlblVehType().setText(
						caVehicleData.getVehBdyType());
					getlblVehModel().setText(
						caVehicleData.getVehModl());
					// defect 10830
					if (cvVehColor.size() == 0)
					{
						populateVehColorVector();
						populateMajorColor(); 
						populateMinorColor(); 
						if (!(csTransCd.equals(TransCdConstant.RENEW)
							|| csTransCd.equals(TransCdConstant.EXCH)
							|| csTransCd.equals(TransCdConstant.REPL)))
						{
							getcomboMajorColor().setEnabled(false);
							getcomboMinorColor().setEnabled(false);
						}
						else if 
							(getcomboMajorColor().getSelectedIndex() > -1) 
						{
							getcomboMajorColor().setEnabled(false);
							getcomboMinorColor().setEnabled(false);
						}
					}
					// end defect 10830
					getlblVin().setText(caVehicleData.getVin());
					getlblBodyVin().setText(
						caVehicleData.getVehBdyVin() == null
							? ""
							: caVehicleData.getVehBdyVin());
					getlblVehClass().setText(
						caVehicleData.getVehClassCd());
					getlblOdometer().setText(
						caVehicleData.getVehOdmtrReadng());
					if (caVehicleData.getVehTon() == null)
					{
						getlblTons().setText(DEFLT_ZERO_TONS);
					}
					else
					{
						getlblTons().setText(
							caVehicleData.getVehTon().getValue());
					}
					getlblTrlrType().setText(
						caVehicleData.getTrlrType() == null
							? ""
							: caVehicleData.getTrlrType());
					if (caVehicleData.getVehLngth() > 0)
					{
						getlblLength().setText(
							Integer.toString(
								caVehicleData.getVehLngth()));
					}
					else
					{
						getlblLength().setText("");
					}
					getlblEmpty().setText(
						Integer.toString(
							caVehicleData.getVehEmptyWt()));
					caRegistrationData.setVehCaryngCap(
						caRegistrationData.getVehGrossWt()
							- caVehicleData.getVehEmptyWt());
					getlblCapacity().setText(
						Integer.toString(
							caRegistrationData.getVehCaryngCap()));
					getlblGross().setText(
						Integer.toString(
							caRegistrationData.getVehGrossWt()));
					/* get and set owner data */
					getlblOwnerId().setText(caOwnerData.getOwnrId());

					// defect 10112 
					getlblOwnerName1().setText(caOwnerData.getName1());
					getlblOwnerName2().setText(caOwnerData.getName2());
					// end defect 10112

					/* get and set title data */
					getlblDocumentNo().setText(caTitleData.getDocNo());
					// set mf original values for registration correction
					caRegValidData.setOrigDieselIndi(
						caVehicleData.getDieselIndi());
					caRegValidData.setOrigRegPltNo(
						caRegistrationData.getRegPltNo());
					caRegValidData.setOrigRegStkrNo(
						caRegistrationData.getRegStkrNo());
					caRegValidData.setOrigOwnerData(
						caMFVehicleData.getOwnerData());
					caRegValidData.setOrigRegExpMo(
						caRegistrationData.getRegExpMo());
					caRegValidData.setOrigRegExpYr(
						caRegistrationData.getRegExpYr());
					caRegValidData.setOrigVehModlYr(
						caVehicleData.getVehModlYr());
					caRegValidData.setOrigVehMk(
						caVehicleData.getVehMk());
					caRegValidData.setOrigVehBdyType(
						caVehicleData.getVehBdyType());
					caRegValidData.setOrigVehModl(
						caVehicleData.getVehModl());
					caRegValidData.setOrigVIN(caVehicleData.getVin());
					caRegValidData.setOrigVehEmptyWt(
						caVehicleData.getVehEmptyWt());
					caRegValidData.setOrigTireTypeCd(
						caRegistrationData.getTireTypeCd());
					// MF UP
					// defect 10127 
					// Use boolean  
					if (cbMFUp)
					{
						int liIssueDate = caTitleData.getTtlIssueDate();
						if (liIssueDate > 0)
						{
							getlblTitleIssueDate().setText(
								new RTSDate(
									RTSDate.YYYYMMDD,
									caTitleData.getTtlIssueDate())
									.toString());
						}
						else
						{
							getlblTitleIssueDate().setText("");
						}
						// defect 11052 
						int liDocTypeCd = caTitleData.getDocTypeCd();
						DocumentTypesData laDocTypeData =
							DocumentTypesCache.getDocType(liDocTypeCd);
						if (laDocTypeData != null )
						{
							getlblDocTypeCdDesc().setText(
								laDocTypeData.getDocTypeCdDesc());
						}
						// end defect 11052 
					}
					else
					{
						getstcLblIssued().setVisible(false);
						getlblTitleIssueDate().setVisible(false);
						getlblDocTypeCdDesc().setVisible(false);
					}
					// end defect 10127 
					if (csTransCd.equals(TransCdConstant.HOTCK)
						|| csTransCd.equals(TransCdConstant.HOTDED)
						|| csTransCd.equals(TransCdConstant.CKREDM)
						|| csTransCd.equals(TransCdConstant.HCKITM)
						|| csTransCd.equals(TransCdConstant.REFUND))
					{
						getbtnLien().setVisible(false);
						getbtnOwnerAddress().setVisible(false);
						getbtnAdditionalInformation().setVisible(false);
						getbtnSpecPltInformation().setVisible(false);
						getJPanel6().setBorder(null);
					}
					if (csTransCd.equals(TransCdConstant.VEHINQ))
					{
						// defect 11052 
						if (caVehicleInquiryData.isRecordFound())
						{
							getbtnLien().setEnabled(caTitleData.hasLien());
						}
						else 
						{
							setupForNoRecordFound(); 
						}
						// end defect 11052 

					}
				}
			}
		}
		catch (Exception aeException)
		{
			RTSException laEx =
				new RTSException(RTSException.JAVA_ERROR, aeException);
			laEx.displayError(this);
		}
	}
	
	/**
	 * Sets the data from the screen to the vehicle object.
	 * 
	 */
	private void setDataToDataObject()
	{
		try
		{
			if (caVehicleInquiryData != null)
			{
				MFVehicleData laMfVehData =
					(MFVehicleData) 
						caVehicleInquiryData.getMfVehicleData();
				VehicleData laVehData = null;
				if (laMfVehData != null)
				{
					laVehData = laMfVehData.getVehicleData();
				}
				if (laVehData != null)
				{
					// Set the Major Color
					String lsColorCd = CommonConstant.STR_SPACE_EMPTY;
						
					lsColorCd = getMajorColorCdSelected();
					if (lsColorCd == null)
					{
						lsColorCd = CommonConstant.STR_SPACE_EMPTY;
					}
					laVehData.setVehMjrColorCd(lsColorCd);
	
					// Set the Minor Color
					lsColorCd = getMinorColorCdSelected();
					if (lsColorCd == null)
					{
						lsColorCd = CommonConstant.STR_SPACE_EMPTY;
					}
					laVehData.setVehMnrColorCd(lsColorCd);
				}
			}
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeNPEx);
			leRTSEx.displayError(this);
		}
	}
	
	/**
	 * Set RescomptCntyNo 
	 * 
	 */
	private void setResComptCntyNo()
	{
		if (SystemProperty.isCounty()
			&& caRegValidData.getApprndComptCntyNo() == 0)
		{
			caRegistrationData.setResComptCntyNo(
				SystemProperty.getOfficeIssuanceNo());
		}
	}
	/** 
	 * Set Data for No Record Found 
	 */
	private void setupForNoRecordFound() 
	{
		getbtnLien().setVisible(false);
		getbtnOwnerAddress().setVisible(false);
		getbtnAdditionalInformation().setVisible(false);
		getbtnSpecPltInformation().setVisible(false);
		getJPanel6().setBorder(null);
		getlblOwnerName1().setText("*** NO RECORD FOUND ***");
		getlblTons().setText(""); 
		getlblEmpty().setText("");
		getlblCapacity().setText("");
		getlblSlash().setText("");
		getlblGross().setText("");
		getlblAge().setText("");
		getlblYear().setText("");
		getlblInsuranceVerified().setText(""); 
	}
	
	/**
	 * Check if Class/Plate/Sticker combination is valid
	 * 
	 * @return boolean true if valid combination
	 */
	private boolean validateClassPltStkrCombo()
	{
		// defect 10127 
		boolean lbValid = true;

		if (caRegValidData.getVehClassOK() == 1)
		{
			caRegValidData.setInvalidClassPltStkrIndi(0);
		}
		else if (caRegValidData.getInvalidClassPltStkrIndi() != 0)
		{
			lbValid = false;
			// 411
			new RTSException(
				ErrorsConstant
					.ERR_NUM_CLASS_PLT_STKR_INVALID_TO_ADDL_INFO)
					.displayError(
				this);

			getController().processData(
				VCRegistrationREG003.ADDITIONAL_INFO,
				caVehicleInquiryData);
		}
		return lbValid;
		// end defect 10127 
	}

	/**
	 * Validate disabled person or veteran plates.
	 * 
	 * @return boolean true if passes validation
	 */
	private boolean validateDisabldPlt()
	{
		// defect 10127 
		// Consolidate returns 
		boolean lbValid = true;
		if (caRegistrationData
			.getRegPltCd()
			.equals(DISABLED_PERSON_PLT)
			|| caRegistrationData.getRegPltCd().equals(
				DISABLED_VETERAN_PLT))
		{
			if (caVehicleData.getVehTon() != null
				&& caVehicleData.getVehTon().compareTo(
					DISABLED_PLT_MAX_TONNAGE)
					> 0)
			{
				new RTSException(
					RTSException.FAILURE_MESSAGE,
					ERRMSG_VEH_TONS,
					ERRMSG_VALIDATE_CAP).displayError(
					this);
				lbValid = false;
			}
		}
		return lbValid;
		// end defect 10127 
	}
//	/**
//	 * Validate Empty Weight
//	 *
//	 * @return boolean
//	 */
//	public boolean validateEmptyWt()
//	{
//		boolean lbReturn = true;
//
//		int liRegClassCd = caMFVehicleData.getRegData().getRegClassCd();
//
//		int liVehEmptyWt =
//			caMFVehicleData.getVehicleData().getVehEmptyWt();
//
//		// defect 8986 
//		// Test for all RegClassCd <= 6000 || >= 6000 
//		if (TitleClientUtilityMethods
//			.isEmptyWtInvalidForRegClassLsEq6000(
//				liRegClassCd,
//				Integer.toString(liVehEmptyWt))
//			|| TitleClientUtilityMethods
//				.isEmptyWtInvalidForRegClassGrt6000(
//				liRegClassCd,
//				Integer.toString(liVehEmptyWt)))
//		{
//			lbReturn = false;
//
//			// defect 10127 
//			// 2
//			new RTSException(
//				ErrorsConstant.ERR_NUM_EMPTY_WT_INVALID).displayError(
//				this);
//			// end defect 10127 
//
//			getController().processData(
//				VCRegistrationREG003.ADDITIONAL_INFO,
//				caVehicleInquiryData);
//
//		}
//		// end defect 8986
//		return lbReturn;
//	}

	/**
	 * Validate Empty Weight
	 *
	 * @return boolean
	 */
	public boolean validateGrossWt()
	{
		boolean lbReturn = true;

		// defect 11183 
		int liRegClassCd = caRegistrationData.getRegClassCd(); 
		String lsVehClassCd = caVehicleData.getVehClassCd();
		RegistrationClassData laRegClassData = RegistrationClassCache.getRegisClass(lsVehClassCd,liRegClassCd,new RTSDate().getYYYYMMDDDate());
		if (laRegClassData != null && laRegClassData.getEmptyWtReqd() == 1)
		{
			try
			{
				TitleClientUtilityMethods.validateGrossWtForRegClassCd(
						caRegistrationData.getRegClassCd(), 
						caRegistrationData.getVehGrossWt());
			}
			catch (RTSException aeRTSEx)
			{
				lbReturn = false;
				aeRTSEx.displayError();
				getController().processData(
						VCRegistrationREG003.ADDITIONAL_INFO,
						caVehicleInquiryData);
			}
		}
		// end defect 11183 
		return lbReturn;
	}

	/**
	 * Check if Heavy Veh Use Tax verification required and performed.
	 * 
	 * @return boolean true if validation passes.
	 */
	private boolean validateHvyVehUseTax()
	{
		// defect 10127 
		boolean lbValid = true;

		// Test if heavy vehicle use tax is not required
		if (caRegValidData.getHvyVehUseTaxRequired() == 1
			&& caRegValidData.getHvyVehUseTaxIndi() == 0)
		{
			// Display msg that heavy vehicle use tax must
			// be verified
			lbValid = false;

			new RTSException(204).displayError(this);

			getController().processData(
				VCRegistrationREG003.ADDITIONAL_INFO,
				caVehicleInquiryData);
		}
		return lbValid;
		// end defect 10127 
	}

	/**
	 * Check if Minimum Gross Weight is acceptable.
	 * 
	 * @return boolean true if validation passes.
	 */
	private boolean validateMinGrossWt()
	{
		// defect 10127 
		boolean lbValid = true;

		if (caRegValidData.getInvalidMinGrossWtIndi() == 1)
		{
			if (caRegValidData.isVehWtStatusOK())
			{
				caRegValidData.setInvalidMinGrossWtIndi(0);
			}
			else
			{
				lbValid = false;

				new RTSException(418).displayError(this);

				getController().processData(
					VCRegistrationREG003.ADDITIONAL_INFO,
					caVehicleInquiryData);

			}
		}
		return lbValid;
		// end defect 10127 
	}

	/**
	 * Set error for any registration event where: 
	 *  Owner Address, Renewal Address or Veh Location Address, 
	 *  EMail Address or EReminder (w/ EMail Address) is in  
	 *  error 
	 * 
	 * Force to REG033 
	 * 
	 * @return boolean 
	 */
	private boolean validateOwnerAddressInfo()
	{
		boolean lbValid = true;

		// defect 10299 
		if (!UtilityMethods
			.isEmpty(caRegistrationData.getRecpntName()))
		{
			Vector lvRecipient = new Vector();
			lvRecipient.add(caRegistrationData.getRecpntName());
			lbValid = CommonValidations.isValidForMF(lvRecipient);
		}

		// defect 10508 
		// Invalid if  
		//   - EMail address is invalid or  
		//   - EMailRenwlCd = 1 and EMail address is empty 
		if (lbValid)
		{
			lbValid = caRegistrationData.isValidEMail();
		}
		// end defect 10508 

		if (!(lbValid // end defect 10299 
			&& caOwnerData.getAddressData().isValidAddress(false)
			&& caRegistrationData.getRenwlMailAddr().isValidAddress(true)
			&& caTitleData.getTtlVehAddr().isValidAddress(true)))
		{
			lbValid = false;

			getController().processData(
				VCRegistrationREG003.OWNER_ADDRESS,
				caVehicleInquiryData);
		}
		return lbValid;
	}

	/**
	 * Set err for renew if reg expired and didn't go thru
	 * addl info screen.
	 * 
	 * @return boolean true if validation passes.
	 */
	private boolean validateRegExpired()
	{
		boolean lbValid = true;

		// defect 10127 
		// Consolidated return; Eliminated try/catch  
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) caVehicleInquiryData
				.getValidationObject();

		// defect 8448, 8474
		// Lookup Common Fees to see if Seasonal Ag
		int liRegClassCd = laRegValidData.getOrigRegClassCd();
		int liEffDate = new RTSDate().getYYYYMMDDDate();
		CommonFeesData laCommonFeesData =
			CommonFeesCache.getCommonFee(liRegClassCd, liEffDate);
		boolean lbSeasonalAg = false;
		if (laCommonFeesData != null
			&& laCommonFeesData.getRegPeriodLngth() == 1)
		{
			lbSeasonalAg = true;
			laRegValidData.setRegExpiredReason(1);
		}
		if (laRegValidData.getTransCode().equals(TransCdConstant.RENEW)
			&& !laRegValidData.isEnterOnAddlInfo()
			&& laRegValidData.getRegistrationExpired() == 1
			&& !lbSeasonalAg)
		{
			lbValid = false;

			new RTSException(735).displayError(this);

			getController().processData(
				VCRegistrationREG003.ADDITIONAL_INFO,
				caVehicleInquiryData);

		}
		return lbValid;
		// end defect 10127 
	}

	/**
	 * Validate Vendor Plate.
	 * 
	 * @return boolean true if validation passes.
	 * @deprecated
	 */
	//	private boolean validateVendorPlt()
	//	{
	//
	//		// Modify can continue with expired Vendor Plate
	//		// Renew, Exchange cannot continue with expired Vendor Plate
	//		boolean lbContinue = true;
	//
	//		try
	//		{
	//			if (PlateTypeCache
	//				.isVendorPlate(caRegistrationData.getRegPltCd()))
	//			{
	//				SpecialPlatesRegisData laSpclPltRegData =
	//					caMFVehicleData.getSpclPltRegisData();
	//
	//				// defect 10327
	//				// For unlinked or purged VP, SpclPltRegisData is null 
	//				//if (csTransCd.equals(TransCdConstant.RENEW)
	//				if (laSpclPltRegData != null 
	//					&& (csTransCd.equals(TransCdConstant.RENEW)
	//					|| csTransCd.equals(TransCdConstant.CORREG)
	//					|| csTransCd.equals(TransCdConstant.EXCH)))
	//				{
	//					// end defect 10327
	//					// defect 9864 
	//					// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
	//					int liPltExpMo = laSpclPltRegData.getPltExpMo();
	//					int liPltExpYr = laSpclPltRegData.getPltExpYr();
	//					// end defect 9864
	//					int liEffectiveExpDate = 0;
	//					int liEffectiveExpDatePlusOne = 0;
	//
	//					// For Renew, check 'as of start of new regis'
	//					// defect 10331
	//					// Add check for 'reg is not expired'.
	//					// If reg is expired, we will use today's date.
	//					if (csTransCd.equals(TransCdConstant.RENEW)
	//						&& !CommonValidations.isRegistrationExpired(
	//						 caRegistrationData.getRegExpMo(),
	//						 caRegistrationData.getRegExpYr()))
	//					{
	//						// end defect 10331
	//
	//						// Cannot renew an expired Vendor Plate
	//						// (plate expired as of new reg period).
	//						if (caRegValidData.getOrigRegExpMo() != 12)
	//						{
	//							liEffectiveExpDatePlusOne =
	//								(caRegValidData.getOrigRegExpYr()
	//									* 10000)
	//									+ ((caRegValidData.getOrigRegExpMo()
	//										+ 1)
	//										* 100)
	//									+ 1;
	//						}
	//						else
	//						{
	//							liEffectiveExpDatePlusOne =
	//								((caRegValidData.getOrigRegExpYr() + 1)
	//									* 10000)
	//									+ 100
	//									+ 1;
	//						}
	//
	//						RTSDate laRTSEffExpDate =
	//							(
	//								new RTSDate(
	//									RTSDate.YYYYMMDD,
	//									liEffectiveExpDatePlusOne)).add(
	//								RTSDate.DATE,
	//								-1);
	//						liEffectiveExpDate =
	//							laRTSEffExpDate.getYYYYMMDDDate();
	//					}
	//					// Else check 'as of today'
	//					else
	//					{
	//						liEffectiveExpDate =
	//							new RTSDate().getYYYYMMDDDate();
	//					}
	//
	//					if (CommonValidations
	//						.isPlateExpired(
	//							liPltExpMo,
	//							liPltExpYr,
	//							liEffectiveExpDate))
	//					{
	//						if (csTransCd.equals(TransCdConstant.RENEW))
	//						{
	//							RegistrationValidationData laRegValidData =
	//								(RegistrationValidationData) caVehicleInquiryData
	//									.getValidationObject();
	//
	//							if (laRegValidData.getRegistrationExpired()
	//								== 1)
	//							{
	//								lbContinue = false;
	//								RTSException leRTSEx =
	//									new RTSException(
	//										RTSException.FAILURE_MESSAGE,
	//										"Registration and Vendor Plate "
	//											+ "are expired. You must go to "
	//											+ "Additional Information screen "
	//											+ "to select Reason For Expired "
	//											+ "Registration and to Change "
	//											+ "Registration",
	//										"ERROR");
	//								throw leRTSEx;
	//							}
	//							else
	//							{
	//								lbContinue = false;
	//								throw new RTSException(
	//									ErrorsConstant
	//										.ERR_NUM_EXPIRED_VENDOR_PLATE);
	//							}
	//						}
	//						else
	//						{
	//							String lsRTSExMsg = "";
	//							if (csTransCd.equals(TransCdConstant.EXCH))
	//							{
	//								lbContinue = false;
	//								lsRTSExMsg =
	//									RTSException.FAILURE_MESSAGE;
	//							}
	//							else
	//							{
	//								lsRTSExMsg =
	//									RTSException.WARNING_MESSAGE;
	//							}
	//
	//							// Just give warning for Modify (CORREG) if
	//							// Vendor Plate is expired
	//							RTSException leRTSEx =
	//								new RTSException(
	//									lsRTSExMsg,
	//									"THE VENDOR PLATE ASSOCIATED "
	//										+ "WITH THIS VEHICLE IS EXPIRED ",
	//									"WARNING");
	//							throw leRTSEx;
	//						}
	//					}
	//					// defect 10127
	//					// Consolidated returns  
	//				}
	//			}
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			aeRTSEx.displayError(this);
	//		}
	//		return lbContinue;
	//		// end defect 10127 
	//	}

	/**
	 * Validate RegEvents   
	 */
	private boolean validateRegEvents()
	{
		boolean lbComplete = false;

		// defect 10959 
		// defect 10357
		// Allow Vendor Plate to continue even if expired
		//if (validateVendorPlt()
		//&& validateEmptyWt()
		// end defect 10357
		// defect 10007
		// validate Tow Truck Certificate
		if (validateGrossWt()
				// end defect 10959
			&& validateDisabldPlt()
			&& procsHardStops()
			&& validateClassPltStkrCombo()
			&& validateRegExpired()
			&& validateOwnerAddressInfo()
			// defect 10830
			&& validateVehColor()
			// end defect 10830
			&& validateTowTruckCert())
		{
			lbComplete = completeTrans();
		}
		// end defect 10007
		return lbComplete;
	}
	/**
	 * Verify Vehicle Color selected properly 
	 *
	 * @return boolean
	 */
	private boolean validateVehColor()
	{
		boolean lbValid = true;
		RTSException laRTSE = new RTSException();

		if (getcomboMajorColor().isEnabled())
		{
			if (isMinorColorSelectedMajorColorNotSelected())
			{
				laRTSE.addException(
					new RTSException(150),
					getcomboMajorColor());
				lbValid = false;
				laRTSE.displayError(this);
				laRTSE.getFirstComponent().requestFocus();
			}
			else if (isMajorColorSameAsMinorColor())
			{
				laRTSE.addException(
					new RTSException(160),
					getcomboMinorColor());
				lbValid = false;
				laRTSE.displayError(this);
				laRTSE.getFirstComponent().requestFocus();
			}
			else if (getcomboMajorColor().getSelectedIndex() == -1)
			{
				RTSException aeRTSException =
					new RTSException(
						RTSException.CTL001,
						MSG_VEHICLE_COLOR_NOT_ADDED,
						null);
				int liButtonSelected = aeRTSException.displayError(this);
				if (liButtonSelected == RTSException.YES)
				{
					lbValid = false;
				}
			}
			
		}
		return lbValid;
	}

	/**
	 * Validate Vehicle Weight 
	 *
	 * @return boolean
	 */
	public boolean validateVehWeight()
	{
		boolean lbValid = true;
		
		if (caMFVehicleData.getVehicleData().getFxdWtIndi() == 0)
		{
			try
			{
				RegistrationClientBusiness laRegClntBusn =
					new RegistrationClientBusiness();
				laRegClntBusn.validateWeight(caVehicleInquiryData);
			}
			catch (RTSException aeRTSEx)
			{
				aeRTSEx.displayError(this);
				getController().processData(
					VCRegistrationREG003.ADDITIONAL_INFO,
					caVehicleInquiryData);
				lbValid = false;
			}
		}
		return lbValid; 
	}
	
	/**
	 * Verify Tow Truck Certificate 
	 *
	 * @return boolean
	 */
	private boolean validateTowTruckCert()
	{
		boolean lbValid = true;

		if (getController()
			.getTransCode()
			.equals(TransCdConstant.RENEW)
			|| getController().getTransCode().equals(
			TransCdConstant.EXCH))
		{
			if (caRegistrationData.getRegClassCd()  == 46
				|| caRegistrationData.getRegClassCd()  == 47)
			{
				if (!caRegValidData.isVerifyTowTruckCertIndi())
				{
					RTSException leRTSEx = new RTSException(991);
					leRTSEx.displayError(this);
					getController().processData(
						VCRegistrationREG003.ADDITIONAL_INFO,
						caVehicleInquiryData);
					lbValid = false;
				}
			}
		}
		return lbValid;
	}

	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 *
	 * @param aaWE java.awt.event.WindowEvent
	 */
	public void windowActivated(java.awt.event.WindowEvent aaWE)
	{
		if (ciButtonFocus == VCRegistrationREG003.ADDITIONAL_INFO)
		{
			getbtnAdditionalInformation().requestFocus();
			ciButtonFocus = 0;
		}
		else if (ciButtonFocus == VCRegistrationREG003.OWNER_ADDRESS)
		{
			getbtnOwnerAddress().requestFocus();
			ciButtonFocus = 0;
		}
		// Set focus to REG003 indicator box if more than 4 entries
		// defect 9971 
		else if (ciNumIndis > CommonConstant.MAX_INDI_NO_SCROLL)
		{
			// end defect 9971 
			getlstIndiDescription().requestFocus();
		}
		else
		{
			getButtonPanel1().getBtnEnter().requestFocus();
		}
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
