package com.txdot.isd.rts.client.title.ui;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.registration.business.RegistrationVerify;

import com.txdot.isd.rts.services.cache.DocumentTypesCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmOwnerEntryTTL007.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M. Rajangam				validation updated
 * MAbs			04/19/2002	Owner ID must be exactly 9 digits CQ 3589
 * T. Pederson	05/15/2002	Making Record Liens field bigger so focus
 *							 will display all around checkbox CQ 3851
 * T. Pederson	05/21/2002	Added error 205 in actionPerformed for RPO
 *							 check CQU100004021.
 * MAbs			06/26/2002	Validated Previous Owner State CQU100004325
 * MAbs			06/27/2002	Focus to address field 1 after USA CQ 4295
 * Govindappa   07/10/2002  Fixed defect#CQU100004452, shift tab was not
 *							 taking the cursor from ownerid to help
 *							 button and that has been fixed by setting
 *							 the next focusable component for
 *							 ButtonPanel to ownerid field.
 * J Rue		07/09/2002	Defect 4406, trim the fields to be written
 *							 to the objects. method saveOwnerInfo()
 * J Rue		07/23/2002	Defect 4516, Adjust logic flow to test for
 *							 State then Country to set State/Zip/ZipP4.
 *							method doDlrTtl().
 * J Rue		07/23/2002	Defect 4406, trim the input for all input
 *							 fields including DlrTitle,
 *	(continued)	08/12/2002	add new method to trim and reset the input
 *							 fields on screen TTL007. 
 *							method added trimScrInputFields()
 *							modified doDlrTtl() and setData
 *  (continued) 10/21/2002	Add Recipient Name, Renewal Address and
 *							 Vehicle Location.
 *							method trimScrInputFields().
 * J Rue 		09/20/2002	defect 4768, Fix edits to display correct
 *							 message if previous owner state char length
 *							 is incorrect. Allow the previous owner
 *							 state to be any 2 digit character format.
 *							method actionPerformed().
 * B Arredondo	12/16/2002	defect# 5147. Made changes for the user help
 *							 guide so had to make changes
 *							modified actionPerformed().
 * J Rue		03/11/2003	defect 5529, Insure the application returns
 *							 to the main menu if RPO, same owner.
 *							method actionPerformed()
 * K Harrell    04/25/2003  defect 6005 Do not accept ResComptCntyNo>254
 *                          method checkCounty()
 * B Arredondo	05/21/2003	defect 6154, Modified method focusGained()
 *							 by commenting out the code so USA checkbox
 *							 can be checked by mouse.
 * J Rue		06/12/2003	defect 6171, Carry forward the owner Id if
 *							 record not found
 *							modify setData()
 * J Rue 		06/17/2003	defect 6210, Ensure owner Id info is carried
 *							 forward if data is found.
 *							new method setUserSuppliedData()
 *							modify setData()
 * J Rue		12/08/2003	Move parsing ResCompCntyNo string inside
 *							 Try/Catch Box. If exception then return 0
 *							modified checkCounty()
 *							defect 6215, Version 5.1.5.2
 * B Arredondo	01/23/2004	Modified tabbing to go from state to zipcode
 *							 to zipcode+4 and aligned fields.
 *							defect 6817 Version 5.1.5.2
 * B Hargrove	05/11/2004	When 'Non-USA', the zipcode+4 field
 *							 contained data from the original USA
 *							 zipcode. Modify methods to clear zipcode+4.
 * 							modify setDataToVehObj(), saveOwnerInfo() 
 * B Hargrove	05/19/2004	Re-formatted comments in methods above.
 *							defect 6911 Ver 5.2.0
 * J Zwiener	07/23/2004  TTL007 has USA addr checked when Cntry field
 *							 is populated.
 *							modify setData()
 *							defects 5914, 6188 Ver 5.2.0 Fix 1
 * Min Wang 	12/30/2004  Fix multiple focuses.
 *							modify FrmOwnerEntryTTL007,
 *							actionPerformed(), getchkUSA(),
 *							gettxtOwnerState(), gettxtOwnerTitleName1(),
 *							gettxtOwnerTitleName2(), 
 *							gettxtPreviousOwnerName(),
 *							gettxtPreviousOwnerState(),
 *							gettxtRecipientName(),
 *							gettxtRenewalMailingState(),
 *							gettxtTitleVehicleLocationState()
 *							defect 7844 Ver 5.2.2	
 * J Rue\		03/01/2005	Tabbing Traversal Policy - Inner class used 
 * S Johnston				to set the order for tabbing through the 
 * 							components in a frame.
 * 							add class RTSTabbingPolicy
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/10/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/15/2005	VAJ to WSAD Clean Up
 * 							uncomment defect 7844 (double cursor)
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/29/2005	Set PrevOwnerCity/PrevOwnerState field 
 * 							length to equal OwnrCity/OwnerState field 
 * 							length
 * 							Modify field length usig Visual Editor
 * 							defect 7354 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/29/2005	Clean up code
 * 							deprecated setOwnrDataTitleDta()
 * 							deprecated verifyCountyRes()
 * 							defect 7898 Ver 5.2.3
 * J Rue		05/18/2005	De-Activate the TransveralPolicy
 * 							Tabbing will be handle by Java natural order
 * 							defect 7898 Ver 5.2.3
 * J Rue		05/25/2005	Clean up State, ZpCd, ZpCdP4, Cntry, 
 * 							CntryZpCd
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3 
 * J Rue		06/20/2005	Remove TransveralPolicy
 * 							defect 7898 Ver 5.2.3   
 * J Rue		06/23/2005	Move Renewal Recipient name text to the 
 * 							correct position.
 * 							Used Visual Editor
 * 							defect 7898 Ver 5.2.3           
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/17/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3  
 * J Rue		12/15/2005	Set hot key to ver 5.2.2.7
 * 							Center dash between Owner ZipCd/ZipCdP4
 * 							Align Owner ZipCd/ZipCdP4 with street input 
 * 							box.
 * 							Switch RecipientName and address header 
 * 							locations using Visual Editor.
 * 							modify getstcLblRenewalRecipientName()
 * 							modify getstcLblRenewalNoticeAddress()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	01/24/2006	Field alignments; Move focus to 1st line of
 * 							address on itemStateChanged on chkUSA.
 * 							modify itemStateChanged(),
 * 							gettxtOwnerCntryZpCd()
 * 							defect 7898 Ver 5.2.3 
 * J Rue		03/23/2007	Add call to SpecialPlates SPL002
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates 
 * K Harrell	04/09/2007	Enable chkbtnSpecialPlates() as appropriate
 * 							modify disableFields()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/07/2007	use SystemProperty.isCounty()
 * 							modify setData()
 * 							defect 9085 Ver Special Plates 
 * J Rue		11/08/2007	Substring the DlrGDN to 6 characters and
 * 	K Harrell				add setDlrGDN()
 * 							modify doDlrTtl(), setData()
 * 							defect 9367 Ver Special Plates 2
 * J Rue		03/28/2008	Change DlrGDN length from 7 to 10
 * 							Replace DLRGDN_MAX_LENGTH with 
 * 							CommonConstant.DLRGDN_MAX_LENGTH
 * 							delect DLR_GDN_MAX_LEN
 * 							modify setDlrGDN(), gettxtDealerGDN()
 * 							defect 9585 Ver 3_AMIGOS_PH_B
 * K Harrell	05/24/2008	Add Mnemonics for County of Residence, Dealer
 * 							 License No; Add'l class cleanup. 
 * 							modify checkCounty(), 
 * 							 getstcLblCountyOfResidence(),
 * 							 getstcLblDealerLicenseNo()
 * 							defect 9584 Ver 3_AMIGOS_PH_B 
 * T Pederson	01/28/2009	Add check for any lien data present in a 
 * 							DTA transaction. If lien data present, the  
 * 							record lien checkbox will be checked. 
 * 							add isDTALienPresent()
 * 							modify doDlrTtl()
 * 							defect 9917 Ver Defect_POS_D
 * K Harrell	02/18/2009	Present msg if SpecialPlate Address is to 
 * 							be changed. 
 * 							modify actionPerformed()
 * 							defect 9893 Ver Defect_POS_D 	 
 * K Harrell	03/04/2009	Reset PermLienHldrIdx, LienRlseDatex 
 * 							Set default ETtlCd if not REJCOR, CORTTL 
 * 							modify clearExistingLienData()
 * 							defect 9971 Ver Defect_POS_E 
 * K Harrell	03/26/2009	Add exception coding for DocTypeCd of 11, 
 * 							ETtlCd =2 
 * 							modify clearExistingLienData() 
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	03/27/2009	Select Lien checkbox for DTA if any Lien Info 
 * 							 or any errors w/ Lien data provided.
 * 							modify isDTALienPresent() 
 * 							defect 9979 Ver Defect_POS_E  
 * K Harrell	04/06/2009	Remove code from 3/26/2009.  Use 
 * 							DocumentTypesCache.getDefaultETtlCd()
 * 							modify clearExistingLienData() 
 * 							defect 9971 Ver Defect_POS_E 
 * K Harrell	04/08/2009	delete ZIPCD_MAX_LEN, ZIPCDP4_MAX_LEN, 
 * 							 STATE_MAX_LEN, NAME_24_MAX_LEN, 
 * 							 CITY_MAX_LEN, STREET_MAX_LEN,
 * 							 NAME_30_MAX_LEN, PREV_CITY_MAX_LEN
 * 							 CNTRY_MAX_LEN, CNTRY_ZIPCD_MAX_LEN,
 * 							 OWNR_ID_MAX_LEN,CNTY_MAX_LEN,
 * 							 VEH_UNIT_NO_MAX_LEN
 *							add LENGTH_PREV_OWNR_CITY, LENGTH_OWNR_ID,
 *							 LENGTH_PREV_OWNR_NAME, LENGTH_COUNTY_NO,
 *							 LENGTH_VEH_UNIT_NO
 *							defect 9971 Ver Defect_POS_E
 * K Harrell	04/13/2009	Move assignment of DocTypeCd from TTL002. 
 * 							(Earlier moved from TTL008 to TTL002.) 
 * 							add setDocTypeCd()  
 * 							modify setDataToVehObj()
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	04/14/2009	Assign ETtlCd as required for RejCor/CorTtl
 * 							modify clearExistingLienData() 
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	07/01/2009	Implement new LienholderData, OwnerData 
 * 							delete focusLost(), focusGained() 
 * 							modify actionPerformed(), doDlrTtl(), 
 * 							 clearExistingLienData(), isNonTitled(), 
 * 							 setData()  
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/18/2009	Implement new validation of Country field.
 * 							 refactor RTSInputFields, get methods to 
 * 							 standards 
 * 							add csTransCd
 * 							add caOwnrNameAddrComp, caRenwlAddrComp,
 * 							 caVehLocAddrComp
 * 							add confirmResComptCntyNo(), 
 * 							 disableRenewalFieldsifApportioned(), 
 * 							 enableSpclPlatesifAttached(), isRejCor(), 
 * 							 isSameOwnerRPO(), setResComptCntyNo(),
 * 							 setSSNColor(),  
 * 							 validateOwnrId(), validatePrevOwnrInfo(),
 * 							 validateRenwlMailingAddr(), 
 * 							 validateVehLocAddr()
 * 							delete INCORR_FLD_ENTRY, LENGTH_OWNR_ID,
 * 							 LENGTH_COUNTY_NO, LENGTH_VEH_UNIT_NO, 
 * 							 LENGTH_PREV_OWNR_CITY, LENGTH_PREV_OWNR_NAME
 * 							delete checkCounty(), setUserSuppliedData(), 
 * 							 trimScrInputFields(),setUSAAddressDisplay(),
 * 							 setNonUSAAddressDisplay(), validateOwnrAddr()
 * 							modify actionPerformed(),isDTALienPresent(), 
 * 							 gettxtVehicleUnitNo(), gettxtPreviousOwnerName(), 
 * 							 gettxtPreviousOwnerCity(), 
 * 							 gettxtResComptCntyNo(), gettxtOwnerId(), 
 * 							 initialize(), itemStateChanged(),
 * 							 setDataToDataObj(), validateFields(),
 * 							 validateOwnrName1() 
 * 							defect 10127 Ver Defect_POS_D  
 * K Harrell	07/22/2009	Disable OwnerId Label, Input field if not 
 * 							HQ.
 * 							modify initialize() 
 * 							defect 10130 Ver Defect_POS_F  	
 * K Harrell	09/05/2009	Add UtilityMethods.trimRTSInputField()
 * 							modify setDataToDataObject()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	12/16/2009	DTA Cleanup 
 * 							modify doDlrTtl()
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	12/28/2009  Implement character validation for MF
 * 	Min Wang				add cvAddlMFValid
 *  						modify initialize(), validateFields(),
 * 							 setDataToDataObject()  
 * 							defect 10299 Ver Defect_POS_H
 * K Harrell	02/16/2010	Enlarge Screen to accommodate EMail 
 * 							add ivjstclblEMail, ivjtxtRecpntEMail, 
 * 							 get methods 
 * 							add validateRecpntEMail() 
 * 							modify setNonOwnerInfoToDisplay(), 
 * 							 getFrmOwnerEntryTTL007ContentPane1(), 
 * 							 disableRenewalFieldsifApportioned(),
 * 							 setDataToDataObj()     
 * 							defect 10372 Ver POS_640
 * K Harrell	03/09/2010 	Move OwnerId validation prior to 
 * 							Name/Address.  Only validate Owner Id if 
 * 							enabled. 
 * 							modify validateFields(), validateOwnrId() 
 * 							defect 10189 Ver POS_640
 * K Harrell	07/10/2010	Add E-Reminder Checkbox, processing logic
 * 							Modification via Visual Editor 
 * 							add ivjchkEReminder, get method, 
 * 							 setEReminderToDisplay(), validateEReminder()
 * 							modify setData(), setNonOwnerInfoToDisplay(), 
 * 							 setDataToDataObject(), validateFields(),
 * 							 getFrmOwnerEntryTTL007ContentPane1(), 
 * 							 itemStateChanged()
 * 							defect 10508 Ver 6.5.0   
 * K Harrell	08/03/2010	modify so that DTA RecpntEMail address will 
 * 							show on existing Vehicle 
 * 							modify setNonOwnerInfoToDisplay()
 * 							defect 10508 Ver 6.5.0
 * K Harrell	09/16/2010	Able to issue RPO to same Veh/Ownr w/ Reg 
 * 							Title. Cannot set DocTypeCd 'til end.
 * 							add getDocTypeCd()
 * 							delete setDocTypeCd()
 * 							modify setData(), setDataToDataObj(), 
 * 							 setEReminderToDisplay() 
 *							defect 10608 Ver 6.5.0   
 * K Harrell	09/16/2010	Modify to save all data for later restore vs.
 * 							just OwnerData 
 * 							add setSavedDataToDisplay()
 * 							add setRegisDataFromDisplay(), 
 * 							  setTitleDataFromDisplay()
 * 							delete saveOwnerData()  
 * 							modify actionPerformed(), setData(), 
 * 							  setDataToDataObject()
 * 							defect 10592 Ver 6.6.0  
 * K Harrell	10/05/2010	add DELETE_LOC_ADDR, DELETE_RECIP_NAME,
 * 							 DELETE_RENEW_ADDR, DELETE_EMAIL
 * 							add ivjbtnDeleteLocation, 
 * 							 ivjbtnDeleteRecipient,
 * 							 ivjbtnDeleteRenewal, ivjbtnDeleteEMail, 
 * 							 ivjJPanelRecordLien, get methods
 * 							add setupDeleteButtons(), keyReleased()
 * 							modify getJPanel1(), getstcLblDealerLicenseNo(), 
 * 							 getstcLblCountyOfResidence(),
 * 							 actionPerformed(), 
 * 							 getFrmOwnerEntryTTL007ContentPane1(), 
 * 							 itemStateChanged()  
 * 							defect 10592 Ver 6.6.0  
 * K Harrell	12/07/2010	Correct 6.7.0 Merge Issues
 * K Harrell	03/08/2011	Correct 6.7.1 Merge Issues
 * K Harrell	06/02/2011	Set EMailRenwlReqCd (even) when disabled 
 * 							modify setRegisDataFromDisplay()
 * 							defect 10821 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to capture the owner information submitted with 
 * a title application.
 *
 * @version	6.8.0	 			06/02/2011
 * @author	Todd Pederson
 * <br>Creation Date:			06/11/2001 23:55:59
 */
public class FrmOwnerEntryTTL007
	extends RTSDialogBox
	implements ActionListener, ItemListener
{

	private RTSButton ivjbtnSpecialPlates = null;
	private ButtonPanel ivjButtonPanel1 = null;

	// defect 10592 
	private RTSButton ivjbtnDeleteEMail = null;
	private RTSButton ivjbtnDeleteLocation = null;
	private RTSButton ivjbtnDeleteRecipient = null;
	private RTSButton ivjbtnDeleteRenewal = null;
	// end defect 10592 

	private JCheckBox ivjchkRecordLien = null;
	private JCheckBox ivjchkUSA = null;
	private JPanel ivjFrmOwnerEntryTTL007ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JLabel ivjstcLblCountyOfResidence = null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblDash2 = null;
	private JLabel ivjstcLblDash3 = null;
	private JLabel ivjstcLblDealerLicenseNo = null;
	private JLabel ivjstcLblOwnerAddress = null;
	private JLabel ivjstcLblOwnerId = null;
	private JLabel ivjstcLblOwnerName = null;
	private JLabel ivjstcLblPreviousOwner = null;
	private JLabel ivjstcLblRenewalNoticeAddress = null;
	private JLabel ivjstcLblRenewalRecipientName = null;
	private JLabel ivjstcLblUnitNo = null;
	private JLabel ivjstcLblVehicleLocationAddress = null;
	private RTSInputField ivjtxtDealerGDN = null;
	private RTSInputField ivjtxtOwnerCity = null;
	private RTSInputField ivjtxtOwnerCntry = null;
	private RTSInputField ivjtxtOwnerCntryZpcd = null;
	private RTSInputField ivjtxtOwnerId = null;
	private RTSInputField ivjtxtOwnerName1 = null;
	private RTSInputField ivjtxtOwnerName2 = null;
	private RTSInputField ivjtxtOwnerState = null;
	private RTSInputField ivjtxtOwnerStreet1 = null;
	private RTSInputField ivjtxtOwnerStreet2 = null;
	private RTSInputField ivjtxtOwnerZpcd = null;
	private RTSInputField ivjtxtOwnerZpcdP4 = null;
	private RTSInputField ivjtxtPreviousOwnerCity = null;
	private RTSInputField ivjtxtPreviousOwnerName = null;
	private RTSInputField ivjtxtPreviousOwnerState = null;
	private RTSInputField ivjtxtRenewalMailingCity = null;
	private RTSInputField ivjtxtRenewalMailingState = null;
	private RTSInputField ivjtxtRenewalMailingStreet1 = null;
	private RTSInputField ivjtxtRenewalMailingStreet2 = null;
	private RTSInputField ivjtxtRenewalMailingZpcd = null;
	private RTSInputField ivjtxtRenewalMailingZpCdP4 = null;
	private RTSInputField ivjtxtRenewalRecipientName = null;
	private RTSInputField ivjtxtResComptCntyNo = null;
	private RTSInputField ivjtxtVehicleLocationCity = null;
	private RTSInputField ivjtxtVehicleLocationState = null;
	private RTSInputField ivjtxtVehicleLocationStreet1 = null;
	private RTSInputField ivjtxtVehicleLocationStreet2 = null;
	private RTSInputField ivjtxtVehicleLocationZpcd = null;
	private RTSInputField ivjtxtVehicleLocationZpcdP4 = null;
	private RTSInputField ivjtxtVehicleUnitNo = null;
	private JPanel jPanel = null;

	// defect 10592
	private JPanel ivjJPanelRecordLien = null;
	// end defect 10592

	// defect 10508 
	private JCheckBox ivjchkEReminder = null;
	// end defect 10508 

	// defect 10372 
	private JLabel ivjstcLblEMail = null;
	private RTSInputField ivjtxtRecpntEMail = null;
	// end defect 10372

	// Object 
	private OwnerData caOwnerData = null;
	private NameAddressComponent caOwnrNameAddrComp = null;
	private RegistrationData caRegisData = null;
	private NameAddressComponent caRenwlAddrComp = null;
	private TitleData caTitleData = null;
	private VehicleInquiryData caVehInqData = null;
	private NameAddressComponent caVehLocAddrComp = null;

	// boolean 
	private boolean cbInitRecordLien;

	// int 
	private int ciPrevCntyNo = 0;

	// String
	private String csTransCd = CommonConstant.STR_SPACE_EMPTY;

	// Constants
	private final static String CHK_REC_LIENS =
		"Check to record lien(s):";
	private final static String COUNTY_OF_RESIDENT =
		"County Of Residence:";
	private static Vector cvAddlMFValid = new Vector();

	private final static String DEALER_LICENSE_NO =
		"Dealer License No:";

	// defect 10592 
	private final static String DELETE_LOC_ADDR =
		"Delete Location Address";
	private final static String DELETE_RECIP_NAME =
		"Delete Recipient Name";
	private final static String DELETE_RENEW_ADDR =
		"Delete Renewal Address";
	private final static String DELETE_EMAIL = "Delete E-Mail";
	// end defect 10592 
	private final static String OWNER_ADDRESS = "Owner Address:";
	private final static String OWNER_ID = "Owner Id:";
	private final static String OWNER_NAME = "Owner Name:";
	private final static String PREV_OWNER = "Previous Owner:";
	private final static String RECORD_LIENS = "Record Lien(s)";
	private final static String RENWL_ADDRESS =
		"Renewal Notice Address: (If different)";
	private final static String RENWL_RECIPIENT =
		"Renewal Recipient Name: (If different)";
	private final static String SELECT = "Select if needed:";
	private final static String SPECIAL_PLATES_INFORMATION =
		"Special Plate Information";
	private final static String UNIT_NO = "Unit No:";
	private final static String USA = "USA";
	private final static String VEH_LOC_ADDRESS =
		"Vehicle Location Address: (If different)";

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs an array of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmOwnerEntryTTL007 laFrmOwnerEntryTTL007;
			laFrmOwnerEntryTTL007 = new FrmOwnerEntryTTL007();
			laFrmOwnerEntryTTL007.setModal(true);
			laFrmOwnerEntryTTL007
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmOwnerEntryTTL007.show();
			java.awt.Insets insets = laFrmOwnerEntryTTL007.getInsets();
			laFrmOwnerEntryTTL007.setSize(
				laFrmOwnerEntryTTL007.getWidth()
					+ insets.left
					+ insets.right,
				laFrmOwnerEntryTTL007.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmOwnerEntryTTL007.setVisibleRTS(true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeIVJEx.printStackTrace(System.out);
		}
	}

	private final RTSInputField[] NAMEFIELD_ARRAY =
		{
			gettxtOwnerName1(),
			gettxtOwnerName2(),
			gettxtPreviousOwnerName(),
			gettxtRenewalRecipientName()};

	/**
	 * FrmOwnerEntryTTL007 constructor comment.
	 */
	public FrmOwnerEntryTTL007()
	{
		super();
		initialize();
	}

	/**
	 * FrmOwnerEntryTTL007 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmOwnerEntryTTL007(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmOwnerEntryTTL007 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmOwnerEntryTTL007(JFrame aaParent)
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

			// defect 10127 
			setSSNColor(NAMEFIELD_ARRAY, Color.white);
			// end defect 10127 

			// defect 9086
			// SPECIAL PLATES 
			if (aaAE.getSource() == ivjbtnSpecialPlates)
			{
				getController().processData(
					VCOwnerEntryTTL007.SPECIAL_PLATES,
					caVehInqData);
			}
			// end defect 9086

			// ENTER 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 10127 
				if (validateFields())
				{
					if (isSameOwnerRPO())
					{
						getController().processData(
							AbstractViewController.FINAL,
							caVehInqData);
					}
					else if (confirmResComptCntyNo())
					{
						setDataToDataObj();

						// Validate after setDataToVehObj() as 
						// VTRAuth CTL010 returns via PREVIOUS/setData() 
						if (validateNameWithSSN())
						{
							// defect 9893 
							RegistrationVerify
								.verifySpclPltAddrUpdtType(
								caVehInqData.getMfVehicleData());
							// end defect 9893 

							getController().processData(
								AbstractViewController.ENTER,
								getController().getData());
						}
						// end defect 10127
					}
				}
			}
			// defect 10592 
			else if (aaAE.getSource() == getbtnDeleteRecipient())
			{
				gettxtRenewalRecipientName().setText("");
				getbtnDeleteRecipient().setEnabled(false);
			}
			else if (aaAE.getSource() == getbtnDeleteRenewal())
			{
				caRenwlAddrComp.resetNonOwnerAddr();
				getbtnDeleteRenewal().setEnabled(false);
			}
			else if (aaAE.getSource() == getbtnDeleteLocation())
			{
				caVehLocAddrComp.resetNonOwnerAddr();
				getbtnDeleteLocation().setEnabled(false);
			}
			else if (aaAE.getSource() == getbtnDeleteEMail())
			{
				gettxtRecpntEMail().setText("");
				if (!getchkEReminder().isSelected())
				{
					clearAllColor(gettxtRecpntEMail());
				}
				getbtnDeleteEMail().setEnabled(false);
			}
			// end defect 10592 
			// CANCEL  
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				// defect 10592 
				//	// defect 10127
				//	saveOwnerData(false);
				//	// end defect 10127 
				saveScreenTTL007Data();

				getController().processData(
					AbstractViewController.CANCEL,
					null);
				// end defect 10592 
			}

			// HELP 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{

				if (csTransCd.equals(TransCdConstant.DTAORK)
					|| csTransCd.equals(TransCdConstant.DTANTK))
				{
					RTSHelp.displayHelp(RTSHelp.TTL007B);
				}
				else if (
					csTransCd.equals(TransCdConstant.DTANTD)
						|| csTransCd.equals(TransCdConstant.DTAORD))
				{
					RTSHelp.displayHelp(RTSHelp.TTL007C);
				}
				else
					// defect 10112
					// Missing CORTTL 
					//	if (
					//		csTransCd.equals(TransCdConstant.TITLE)
					//			|| csTransCd.equals(TransCdConstant.NONTTL)
					//			|| csTransCd.equals(TransCdConstant.REJCOR))
					// end defect 10112 
					{
					if (UtilityMethods.isMFUP())
					{
						RTSHelp.displayHelp(RTSHelp.TTL007A);
					}
					else
					{
						RTSHelp.displayHelp(RTSHelp.TTL007D);
					}
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Clear out the existing Lienholder data if not doing a corrected 
	 * title or reject correction.
	 */
	private void clearExistingLienData()
	{
		TitleData laTtlData =
			caVehInqData.getMfVehicleData().getTitleData();

		// defect 10112 
		if (laTtlData != null && !isRejCor() && !isCorTtl())
		{
			// end defect 10112 

			// defect 9971 
			// Clear the dates and additional lien indi
			laTtlData.clearLien1Data();
			laTtlData.clearLien2Data();
			laTtlData.clearLien3Data();
			laTtlData.setAddlLienRecrdIndi(0);

			// Set to default ETtlCd per DocTypeCd 
			laTtlData.setETtlCd(
				DocumentTypesCache.getDefaultETtlCd(
					laTtlData.getDocTypeCd()));
		}
		// This will handle REJCOR from OrigTitle, etc. to Non-Ttl 
		else if (
			DocumentTypesCache.getDefaultETtlCd(
				laTtlData.getDocTypeCd())
				== TitleConstant.NON_NEGOTIABLE_ETTLCD)
		{
			laTtlData.setETtlCd(TitleConstant.NON_NEGOTIABLE_ETTLCD);
		}
		// end defect 9971
	}

	/**
	 * Validate County of Resident. 
	 * 
	 * Return true if continuing processing/ answer YES to County
	 *  Confirmation 
	 *
	 * @return boolean 
	 */
	private boolean confirmResComptCntyNo()
	{
		boolean lbReturn = true;

		String lsCurrCnty = gettxtResComptCntyNo().getText();

		int liCurrCnty = Integer.parseInt(lsCurrCnty);

		if (liCurrCnty != SystemProperty.getOfficeIssuanceNo()
			&& liCurrCnty != ciPrevCntyNo)
		{
			OfficeIdsData laOfcData =
				OfficeIdsCache.getOfcId(liCurrCnty);

			FrmCountyConfirmCTL002 laCntyCnfrm =
				new FrmCountyConfirmCTL002(
					getController().getMediator().getDesktop(),
					lsCurrCnty,
					laOfcData.getOfcName());

			if (laCntyCnfrm.displayWindow()
				== FrmCountyConfirmCTL002.YES)
			{
				ciPrevCntyNo = liCurrCnty;
			}
			else
			{
				lbReturn = false;
				ciPrevCntyNo = 0;
				// TODO This doesn't work. 
				gettxtResComptCntyNo().requestFocus();
			}
		}
		return lbReturn;
	}

	/**
	 * Set Enabled() for RenewalMailing
	 * 
	 */
	private void disableRenewalFieldsifApportioned()
	{
		RegistrationData laRegisData =
			(RegistrationData) caVehInqData
				.getMfVehicleData()
				.getRegData();

		if (laRegisData != null)
		{
			//Check if apportioned vehicle
			// defect 10112 
			if (laRegisData.isApportioned())
			{
				// end defect 10112 
				getstcLblRenewalRecipientName().setEnabled(false);
				gettxtRenewalRecipientName().setEnabled(false);
				getstcLblRenewalNoticeAddress().setEnabled(false);
				gettxtRenewalMailingStreet1().setEnabled(false);
				gettxtRenewalMailingStreet2().setEnabled(false);
				gettxtRenewalMailingCity().setEnabled(false);
				gettxtRenewalMailingState().setEnabled(false);
				gettxtRenewalMailingZpcd().setEnabled(false);
				gettxtRenewalMailingZpcdP4().setEnabled(false);
				// defect 10372 
				gettxtRecpntEMail().setEnabled(false);
				// end defect 10372 
			}
		}
	}

	/**
	 * Set display fields if DTA.
	 * 
	 */
	private void doDlrTtl()
	{
		if (caVehInqData != null)
		{
			TitleValidObj laValidationObj =
				(TitleValidObj) caVehInqData.getValidationObject();

			// defect 10290
			// refactored laDlrTtlData from laDlrTtlObj 
			DealerTitleData laDlrTtlData =
				(DealerTitleData) laValidationObj.getDlrTtlData();

			if (laDlrTtlData != null)
			{

				if (laDlrTtlData.isKeyBoardEntry())
				{
					gettxtResComptCntyNo().setText(
						"" + SystemProperty.getOfficeIssuanceNo());
				}
				else
				{
					int liCntyNo =
						laDlrTtlData
							.getMFVehicleData()
							.getRegData()
							.getResComptCntyNo();

					if (liCntyNo > 0)
					{
						gettxtResComptCntyNo().setText("" + liCntyNo);
					}

					// Set Record Lien checkbox
					// defect 9917
					if (!isNonTitled() && !isDTALienPresent())
					{
						getchkRecordLien().setSelected(false);
					}
					// end defect 9917

					// defect 10112 
					setOwnerDataToDisplay(
						(OwnerData) laDlrTtlData
							.getMFVehicleData()
							.getOwnerData());

					//populate renewal info/vehicle location addres
					TitleData laTitleData =
						laDlrTtlData.getMFVehicleData().getTitleData();

					RegistrationData laRegisData =
						laDlrTtlData.getMFVehicleData().getRegData();
					// end defect 10290 

					setNonOwnerInfoToDisplay(laTitleData, laRegisData);
					// end defect 10112

				}
			}
		}
	}

	/**
	 * Enabled Special Plates if attached 
	 */
	private void enableSpclPlatesifAttached()
	{
		// defect 9085 
		MFVehicleData laMFVehData = caVehInqData.getMfVehicleData();
		if (laMFVehData != null)
		{
			getbtnSpecialPlates().setEnabled(laMFVehData.isSpclPlt());
		}
		// end defect 9085 
	}

	/**
	 * Return the getbtnSpecialPlates property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnSpecialPlates()
	{
		if (ivjbtnSpecialPlates == null)
		{
			try
			{
				ivjbtnSpecialPlates = new RTSButton();
				ivjbtnSpecialPlates.setSize(176, 28);
				ivjbtnSpecialPlates.setName("ivjbtnSpecialPlates");
				ivjbtnSpecialPlates.setMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjbtnSpecialPlates.setText(SPECIAL_PLATES_INFORMATION);
				ivjbtnSpecialPlates.setActionCommand(
					SPECIAL_PLATES_INFORMATION);
				// user code begin {1}

				ivjbtnSpecialPlates.setLocation(355, 400);
				ivjbtnSpecialPlates.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjbtnSpecialPlates;
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
				ivjButtonPanel1.setSize(239, 35);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel1.setLocation(327, 458);
				ivjButtonPanel1.getBtnCancel().addActionListener(this);
				ivjButtonPanel1.getBtnEnter().addActionListener(this);
				ivjButtonPanel1.getBtnHelp().addActionListener(this);
				// user code end
			}
			catch (Throwable aIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aIVJEx);
			}
		}

		return ivjButtonPanel1;
	}
	/**
	 * This method initializes ivjbtnDeleteEMail
	 * 
	 * @return JCheckBox
	 */
	private RTSButton getbtnDeleteEMail()
	{
		if (ivjbtnDeleteEMail == null)
		{
			ivjbtnDeleteEMail = new RTSButton();
			ivjbtnDeleteEMail.setSize(205, 26);
			ivjbtnDeleteEMail.setText(DELETE_EMAIL);
			//ivjbtnDeleteEMail.setLocation(25, 108);
			ivjbtnDeleteEMail.setLocation(25, 79);
			ivjbtnDeleteEMail.setMnemonic(KeyEvent.VK_I);
			ivjbtnDeleteEMail.addActionListener(this);
		}
		return ivjbtnDeleteEMail;
	}

	/**
	 * Return the ivjbtnDeleteLocation property value.
	 * 
	 * @return JCheckBox
	 */
	private RTSButton getbtnDeleteLocation()
	{
		if (ivjbtnDeleteLocation == null)
		{
			try
			{
				ivjbtnDeleteLocation = new RTSButton();
				ivjbtnDeleteLocation.setSize(205, 26);
				ivjbtnDeleteLocation.setName("ivjbtnDeleteLocation");
				ivjbtnDeleteLocation.setMnemonic(KeyEvent.VK_T);
				ivjbtnDeleteLocation.setText(DELETE_LOC_ADDR);
				ivjbtnDeleteLocation.setMaximumSize(
					new java.awt.Dimension(164, 22));
				ivjbtnDeleteLocation.setActionCommand(DELETE_LOC_ADDR);
				ivjbtnDeleteLocation.setMinimumSize(
					new java.awt.Dimension(164, 22));
				//ivjbtnDeleteLocation.setLocation(25, 79);
				ivjbtnDeleteLocation.setLocation(25, 108);
				// user code begin {1}
				ivjbtnDeleteLocation.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjbtnDeleteLocation;
	}

	/**
	 * Return the ivjbtnDeleteRecipient property value.
	 * 
	 * @return JCheckBox
	 */
	private RTSButton getbtnDeleteRecipient()
	{
		if (ivjbtnDeleteRecipient == null)
		{
			try
			{
				ivjbtnDeleteRecipient = new RTSButton();
				ivjbtnDeleteRecipient.setSize(205, 26);
				ivjbtnDeleteRecipient.setName("ivjbtnDeleteRecipient");
				ivjbtnDeleteRecipient.setMnemonic(68);
				ivjbtnDeleteRecipient.setText(DELETE_RECIP_NAME);
				ivjbtnDeleteRecipient.setMaximumSize(
					new java.awt.Dimension(153, 22));
				ivjbtnDeleteRecipient.setActionCommand(
					DELETE_RECIP_NAME);
				ivjbtnDeleteRecipient.setMinimumSize(
					new java.awt.Dimension(153, 22));
				ivjbtnDeleteRecipient.setLocation(25, 21);
				// user code begin {1}
				ivjbtnDeleteRecipient.setPreferredSize(
					new java.awt.Dimension(173, 26));
				ivjbtnDeleteRecipient.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjbtnDeleteRecipient;
	}

	/**
	 * Return the ivjbtnDeleteRenewal property value.
	 * 
	 * @return JCheckBox
	 */
	private RTSButton getbtnDeleteRenewal()
	{
		if (ivjbtnDeleteRenewal == null)
		{
			try
			{
				ivjbtnDeleteRenewal = new RTSButton();
				ivjbtnDeleteRenewal.setSize(205, 26);
				ivjbtnDeleteRenewal.setName("ivjbtnDeleteRenewal");
				ivjbtnDeleteRenewal.setMnemonic(82);
				ivjbtnDeleteRenewal.setText(DELETE_RENEW_ADDR);
				ivjbtnDeleteRenewal.setMaximumSize(
					new java.awt.Dimension(164, 22));
				ivjbtnDeleteRenewal.setActionCommand(DELETE_RENEW_ADDR);
				ivjbtnDeleteRenewal.setMinimumSize(
					new java.awt.Dimension(164, 22));
				ivjbtnDeleteRenewal.setLocation(25, 50);
				ivjbtnDeleteRenewal.setPreferredSize(
					new java.awt.Dimension(173, 26));
				ivjbtnDeleteRenewal.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjbtnDeleteRenewal;
	}

	/**
	 * This method initializes ivjchkEReminder
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkEReminder()
	{
		if (ivjchkEReminder == null)
		{
			ivjchkEReminder = new JCheckBox();
			ivjchkEReminder.setBounds(491, 175, 91, 20);
			ivjchkEReminder.setText(
				RegistrationConstant.EREMINDER_CHKBX_LABEL);
			ivjchkEReminder.setMnemonic(KeyEvent.VK_E);
			ivjchkEReminder.addItemListener(this);
		}
		return ivjchkEReminder;
	}

	/**
	 * Return the ivjchkRecordLien property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkRecordLien()
	{
		if (ivjchkRecordLien == null)
		{
			try
			{
				ivjchkRecordLien = new JCheckBox();
				ivjchkRecordLien.setSize(108, 24);
				ivjchkRecordLien.setName("ivjchkRecordLien");
				ivjchkRecordLien.setMnemonic(KeyEvent.VK_L);
				ivjchkRecordLien.setText(RECORD_LIENS);
				ivjchkRecordLien.setMaximumSize(
					new java.awt.Dimension(108, 22));
				ivjchkRecordLien.setActionCommand(RECORD_LIENS);
				ivjchkRecordLien.setSelected(true);
				ivjchkRecordLien.setMinimumSize(
					new java.awt.Dimension(108, 22));
				// user code begin {1}

				ivjchkRecordLien.setLocation(45, 19);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkRecordLien;
	}

	/**
	 * Return the ivjchkUSA property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkUSA()
	{
		if (ivjchkUSA == null)
		{
			try
			{
				ivjchkUSA = new JCheckBox();
				ivjchkUSA.setBounds(224, 79, 51, 22);
				ivjchkUSA.setName("ivjchkUSA");
				ivjchkUSA.setMnemonic(KeyEvent.VK_U);
				ivjchkUSA.setText(USA);
				ivjchkUSA.setMaximumSize(
					new java.awt.Dimension(108, 22));
				ivjchkUSA.setActionCommand(RECORD_LIENS);
				ivjchkUSA.setSelected(true);
				ivjchkUSA.setMinimumSize(
					new java.awt.Dimension(108, 22));
				// user code begin {1}
				ivjchkUSA.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkUSA;
	}

	/**
	 * Set Title DocTypeCd 
	 * 
	 * @return int 
	 */
	private int getDocTypeCd()
	{
		TitleData laTitleData =
			caVehInqData.getMfVehicleData().getTitleData();

		int liDocTypeCd = laTitleData.getDocTypeCd();

		if (laTitleData.getTtlTypeIndi() == TitleTypes.INT_REGPURPOSE)
		{
			liDocTypeCd = DocTypeConstant.REGISTRATION_PURPOSES_ONLY;
		}
		else if (
			laTitleData.getTtlTypeIndi() == TitleTypes.INT_NONTITLED)
		{
			liDocTypeCd = DocTypeConstant.NON_TITLED_VEHICLE;
		}
		else if (
			caVehInqData
				.getMfVehicleData()
				.getRegData()
				.getOffHwyUseIndi()
				== 1)
		{
			liDocTypeCd = DocTypeConstant.OFF_HIGHWAY_USE_ONLY;
		}
		else if (
			laTitleData.getTtlTypeIndi() == TitleTypes.INT_ORIGINAL
				&& caVehInqData.getNoMFRecs() == 0
				&& caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegWaivedIndi()
					== 1)
		{
			liDocTypeCd = DocTypeConstant.INSURANCE_NO_REGIS_TITLE;
		}
		else if (
			laTitleData.getTtlTypeIndi() == TitleTypes.INT_ORIGINAL
				&& caVehInqData.getNoMFRecs() == 1
				&& caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegWaivedIndi()
					== 1
				&& laTitleData.getDocTypeCd()
					== DocTypeConstant.INSURANCE_NO_REGIS_TITLE)
		{
			// Review else logic
		}
		else if (
			laTitleData.getTtlTypeIndi() == TitleTypes.INT_ORIGINAL)
		{
			liDocTypeCd = DocTypeConstant.REGULAR_TITLE;
		}
		else if (
			laTitleData.getTtlTypeIndi() == TitleTypes.INT_CORRECTED
				&& laTitleData.getDocTypeCd()
					!= DocTypeConstant.OFF_HIGHWAY_USE_ONLY
				&& laTitleData.getDocTypeCd()
					!= DocTypeConstant.OS_REGISTERED_APPORTIONED_VEH
				&& laTitleData.getDocTypeCd()
					!= DocTypeConstant.INSURANCE_NO_REGIS_TITLE)
		{
			liDocTypeCd = DocTypeConstant.REGULAR_TITLE;
		}
		return liDocTypeCd;
	}

	/**
	* Return the ivjFrmOwnerEntryTTL007ContentPane1 property value.
	* 
	* @return JPanel
	*/
	private JPanel getFrmOwnerEntryTTL007ContentPane1()
	{
		if (ivjFrmOwnerEntryTTL007ContentPane1 == null)
		{
			try
			{
				ivjFrmOwnerEntryTTL007ContentPane1 = new JPanel();
				ivjFrmOwnerEntryTTL007ContentPane1.setName(
					"ivjFrmOwnerEntryTTL007ContentPane1");
				ivjFrmOwnerEntryTTL007ContentPane1.setLayout(null);
				ivjFrmOwnerEntryTTL007ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmOwnerEntryTTL007ContentPane1.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmOwnerEntryTTL007ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtRenewalRecipientName(),
					gettxtRenewalRecipientName().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtRenewalMailingStreet1(),
					gettxtRenewalMailingStreet1().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtRenewalMailingStreet2(),
					gettxtRenewalMailingStreet2().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtRenewalMailingCity(),
					gettxtRenewalMailingCity().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtRenewalMailingState(),
					gettxtRenewalMailingState().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtRenewalMailingZpcd(),
					gettxtRenewalMailingZpcd().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtRenewalMailingZpcdP4(),
					gettxtRenewalMailingZpcdP4().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtVehicleLocationStreet1(),
					gettxtVehicleLocationStreet1().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtVehicleLocationStreet2(),
					gettxtVehicleLocationStreet2().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtVehicleLocationCity(),
					gettxtVehicleLocationCity().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtVehicleLocationState(),
					gettxtVehicleLocationState().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtVehicleLocationZpcd(),
					gettxtVehicleLocationZpcd().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					gettxtVehicleLocationZpcdP4(),
					gettxtVehicleLocationZpcdP4().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					getstcLblDash3(),
					getstcLblDash3().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					getstcLblDash2(),
					getstcLblDash2().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					getstcLblRenewalRecipientName(),
					getstcLblRenewalRecipientName().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					getstcLblRenewalNoticeAddress(),
					getstcLblRenewalNoticeAddress().getName());
				getFrmOwnerEntryTTL007ContentPane1().add(
					getstcLblVehicleLocationAddress(),
					getstcLblVehicleLocationAddress().getName());
				// user code begin {1}
				ivjFrmOwnerEntryTTL007ContentPane1.add(
					getJPanel(),
					null);
				// user code end
				ivjFrmOwnerEntryTTL007ContentPane1.add(
					getstcLblEMail(),
					null);
				ivjFrmOwnerEntryTTL007ContentPane1.add(
					gettxtRecpntEMail(),
					null);
				// end defect 10372

				// defect 10508 
				ivjFrmOwnerEntryTTL007ContentPane1.add(
					getchkEReminder(),
					null);
				// end defect 10508 

				ivjFrmOwnerEntryTTL007ContentPane1.add(
					getJPanelRecordLien(),
					null);
				ivjFrmOwnerEntryTTL007ContentPane1.add(
					getbtnSpecialPlates(),
					null);
				// defect 10592
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmOwnerEntryTTL007ContentPane1;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getstcLblOwnerName(), null);
			jPanel.add(getstcLblOwnerId(), null);
			jPanel.add(gettxtOwnerId(), null);
			jPanel.add(gettxtOwnerName1(), null);
			jPanel.add(gettxtOwnerName2(), null);
			jPanel.add(getstcLblOwnerAddress(), null);
			jPanel.add(gettxtOwnerStreet1(), null);
			jPanel.add(getchkUSA(), null);
			jPanel.add(gettxtOwnerStreet2(), null);
			jPanel.add(getstcLblPreviousOwner(), null);
			jPanel.add(gettxtPreviousOwnerName(), null);
			jPanel.add(gettxtPreviousOwnerCity(), null);
			jPanel.add(gettxtPreviousOwnerState(), null);
			jPanel.add(getJPanel1(), null);
			jPanel.add(getJPanel2(), null);
			jPanel.add(getJPanel3(), null);
			jPanel.setBounds(9, 15, 288, 486);

		}
		return jPanel;
	}

	/**
	 * Return the ivjJPanel1 property value.
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
				ivjJPanel1.setName("ivjJPanel1");
				ivjJPanel1.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT));
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel1.setPreferredSize(
					new java.awt.Dimension(245, 66));
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(245, 103));
				// defect 10592
				ivjJPanel1.add(getbtnDeleteRecipient(), null);
				ivjJPanel1.add(getbtnDeleteLocation(), null);
				ivjJPanel1.add(getbtnDeleteRenewal(), null);
				ivjJPanel1.add(getbtnDeleteEMail(), null);

				ivjJPanel1.setSize(254, 142);
				ivjJPanel1.setLocation(9, 254);
				// user code begin {1}
				RTSButtonGroup laButtonGrp = new RTSButtonGroup();
				laButtonGrp.add(getbtnDeleteRecipient());
				laButtonGrp.add(getbtnDeleteRenewal());
				laButtonGrp.add(getbtnDeleteEMail());
				laButtonGrp.add(getbtnDeleteLocation());
				// end defect 10592 

				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel1;
	}

	/**
	* Return the ivjJPanel2 property value.
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
				ivjJPanel2.setName("ivjJPanel2");
				ivjJPanel2.setLayout(null);
				ivjJPanel2.add(getstcLblCountyOfResidence(), null);
				ivjJPanel2.add(gettxtResComptCntyNo(), null);
				ivjJPanel2.add(getstcLblDealerLicenseNo(), null);
				ivjJPanel2.add(gettxtDealerGDN(), null);
				ivjJPanel2.add(getstcLblUnitNo(), null);
				ivjJPanel2.add(gettxtVehicleUnitNo(), null);
				ivjJPanel2.setSize(234, 81);
				ivjJPanel2.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel2.setMinimumSize(
					new java.awt.Dimension(371, 110));
				ivjJPanel2.setLocation(10, 397);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel2;
	}

	/**
	 * 
	 * Return the ivjJPanel3 property value.
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
				ivjJPanel3.setName("ivjJPanel3");
				ivjJPanel3.setLayout(null);
				getJPanel3().add(
					gettxtOwnerCity(),
					gettxtOwnerCity().getName());
				getJPanel3().add(
					gettxtOwnerState(),
					gettxtOwnerState().getName());
				ivjJPanel3.add(gettxtOwnerZpcd(), null);
				ivjJPanel3.add(getstcLblDash(), null);
				ivjJPanel3.add(gettxtOwnerZpcdP4(), null);
				ivjJPanel3.add(gettxtOwnerCntryZpcd(), null);
				ivjJPanel3.add(gettxtOwnerCntry(), null);
				ivjJPanel3.setBounds(1, 146, 273, 29);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel3;
	}

	/**
	 * This method initializes ivjJPanelRecordLien
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelRecordLien()
	{
		if (ivjJPanelRecordLien == null)
		{
			ivjJPanelRecordLien = new JPanel();
			ivjJPanelRecordLien.setLayout(null);
			ivjJPanelRecordLien.add(getchkRecordLien(), null);
			ivjJPanelRecordLien.setSize(187, 55);
			ivjJPanelRecordLien.setBorder(
				new javax.swing.border.TitledBorder(
					new javax.swing.border.EtchedBorder(),
					CHK_REC_LIENS));
			ivjJPanelRecordLien.setLocation(350, 330);
		}
		return ivjJPanelRecordLien;
	}
	/**
	 * Return the ivjstcLblCountyOfResidence property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCountyOfResidence()
	{
		if (ivjstcLblCountyOfResidence == null)
		{
			try
			{
				ivjstcLblCountyOfResidence = new JLabel();
				ivjstcLblCountyOfResidence.setSize(127, 18);
				ivjstcLblCountyOfResidence.setName(
					"ivjstcLblCountyOfResidence");
				ivjstcLblCountyOfResidence.setText(COUNTY_OF_RESIDENT);
				ivjstcLblCountyOfResidence.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblCountyOfResidence.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblCountyOfResidence.setHorizontalAlignment(4);
				ivjstcLblCountyOfResidence.setLocation(8, 9);
				// user code begin {1}
				// defect 9584 
				ivjstcLblCountyOfResidence.setLabelFor(
					gettxtResComptCntyNo());

				// defect 10592 
				// Mnemonic from R to C
				ivjstcLblCountyOfResidence.setDisplayedMnemonic(
					KeyEvent.VK_C);
				// end defect 10592 
				// end defect 9584 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblCountyOfResidence;
	}

	/**
	 * Return the ivjstcLblDash property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDash()
	{
		if (ivjstcLblDash == null)
		{
			try
			{
				ivjstcLblDash = new JLabel();
				ivjstcLblDash.setSize(5, 17);
				ivjstcLblDash.setName("stcLblDash");
				ivjstcLblDash.setText(CommonConstant.STR_DASH);
				ivjstcLblDash.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setMinimumSize(
					new java.awt.Dimension(4, 14));
				// user code begin {1}
				ivjstcLblDash.setLocation(232, 5);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDash;
	}

	/**
	 * Return the ivjstcLblDash2 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDash2()
	{
		if (ivjstcLblDash2 == null)
		{
			try
			{
				ivjstcLblDash2 = new JLabel();
				ivjstcLblDash2.setSize(9, 14);
				ivjstcLblDash2.setName("ivjstcLblDash2");
				ivjstcLblDash2.setText(CommonConstant.STR_DASH);
				ivjstcLblDash2.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash2.setMinimumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash2.setLocation(534, 144);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDash2;
	}

	/**
	 * Return the ivjstcLblDash3 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDash3()
	{
		if (ivjstcLblDash3 == null)
		{
			try
			{
				ivjstcLblDash3 = new JLabel();
				ivjstcLblDash3.setName("stcLblDash3");
				ivjstcLblDash3.setText(CommonConstant.STR_DASH);
				ivjstcLblDash3.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash3.setMinimumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash3.setBounds(534, 296, 9, 14);
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
		return ivjstcLblDash3;
	}

	/**
	 * Return the ivjstcLblDealerLicenseNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDealerLicenseNo()
	{
		if (ivjstcLblDealerLicenseNo == null)
		{
			try
			{
				ivjstcLblDealerLicenseNo = new JLabel();
				ivjstcLblDealerLicenseNo.setName(
					"ivjstcLblDealerLicenseNo");
				ivjstcLblDealerLicenseNo.setText(DEALER_LICENSE_NO);
				ivjstcLblDealerLicenseNo.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblDealerLicenseNo.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblDealerLicenseNo.setHorizontalAlignment(4);
				ivjstcLblDealerLicenseNo.setSize(116, 18);
				ivjstcLblDealerLicenseNo.setLocation(19, 30);
				// user code begin {1}
				// defect 9584
				ivjstcLblDealerLicenseNo.setLabelFor(gettxtDealerGDN());

				// defect 10592 
				// Mnemonic from D to 0 
				ivjstcLblDealerLicenseNo.setDisplayedMnemonic(
					KeyEvent.VK_O);
				// end defect 10592 

				// end defect 9584 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDealerLicenseNo;
	}

	/**
	 * Return the ivjstcLblOwnerAddress property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOwnerAddress()
	{
		if (ivjstcLblOwnerAddress == null)
		{
			try
			{
				ivjstcLblOwnerAddress = new JLabel();
				ivjstcLblOwnerAddress.setBounds(6, 80, 101, 20);
				ivjstcLblOwnerAddress.setName("ivjstcLblOwnerAddress");
				ivjstcLblOwnerAddress.setText(OWNER_ADDRESS);
				ivjstcLblOwnerAddress.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblOwnerAddress.setMinimumSize(
					new java.awt.Dimension(45, 14));
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
		return ivjstcLblOwnerAddress;
	}

	/**
	 * Return the ivjstcLblOwnerId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOwnerId()
	{
		if (ivjstcLblOwnerId == null)
		{
			try
			{
				ivjstcLblOwnerId = new JLabel();
				ivjstcLblOwnerId.setBounds(132, 6, 57, 20);
				ivjstcLblOwnerId.setName("ivjstcLblOwnerId");
				ivjstcLblOwnerId.setText(OWNER_ID);
				ivjstcLblOwnerId.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblOwnerId.setMinimumSize(
					new java.awt.Dimension(45, 14));
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
		return ivjstcLblOwnerId;
	}

	/**
	 * Return the ivjstcLblOwnerName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOwnerName()
	{
		if (ivjstcLblOwnerName == null)
		{
			try
			{
				ivjstcLblOwnerName = new JLabel();
				ivjstcLblOwnerName.setBounds(6, 6, 82, 20);
				ivjstcLblOwnerName.setName("ivjstcLblOwnerName");
				ivjstcLblOwnerName.setText(OWNER_NAME);
				ivjstcLblOwnerName.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblOwnerName.setMinimumSize(
					new java.awt.Dimension(45, 14));
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
		return ivjstcLblOwnerName;
	}

	/**
	 * Return the ivjstcLblPreviousOwner property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPreviousOwner()
	{
		if (ivjstcLblPreviousOwner == null)
		{
			try
			{
				ivjstcLblPreviousOwner = new JLabel();
				ivjstcLblPreviousOwner.setName(
					"ivjstcLblPreviousOwner");
				ivjstcLblPreviousOwner.setText(PREV_OWNER);
				ivjstcLblPreviousOwner.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblPreviousOwner.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblPreviousOwner.setBounds(6, 182, 108, 20);
				// user code begin {1}
				ivjstcLblPreviousOwner.setLabelFor(
					gettxtPreviousOwnerName());
				ivjstcLblPreviousOwner.setDisplayedMnemonic(
					KeyEvent.VK_P);
				// user code end

			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblPreviousOwner;
	}

	/**
	 * This method initializes ivjstcLblEMail
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEMail()
	{
		if (ivjstcLblEMail == null)
		{
			ivjstcLblEMail = new JLabel();
			ivjstcLblEMail.setBounds(313, 175, 41, 20);
			ivjstcLblEMail.setText(CommonConstant.TXT_EMAIL);
			ivjstcLblEMail.setDisplayedMnemonic(
				java.awt.event.KeyEvent.VK_M);
			ivjstcLblEMail.setLabelFor(gettxtRecpntEMail());
		}
		return ivjstcLblEMail;
	}

	/**
	 * Return the ivjstcLblRenewalNoticeAddress property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRenewalNoticeAddress()
	{
		if (ivjstcLblRenewalNoticeAddress == null)
		{
			try
			{
				ivjstcLblRenewalNoticeAddress = new JLabel();
				ivjstcLblRenewalNoticeAddress.setName(
					"ivjstcLblRenewalNoticeAddress");
				ivjstcLblRenewalNoticeAddress.setBounds(
					313,
					75,
					221,
					20);
				ivjstcLblRenewalNoticeAddress.setText(RENWL_ADDRESS);
				ivjstcLblRenewalNoticeAddress.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblRenewalNoticeAddress.setMinimumSize(
					new java.awt.Dimension(45, 14));
				// user code begin {1}
				ivjstcLblRenewalNoticeAddress.setLabelFor(
					gettxtRenewalMailingStreet1());
				ivjstcLblRenewalNoticeAddress.setDisplayedMnemonic(
					KeyEvent.VK_N);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblRenewalNoticeAddress;
	}

	/**
	 * Return the ivjstcLblRenewalRecipientName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRenewalRecipientName()
	{
		if (ivjstcLblRenewalRecipientName == null)
		{
			try
			{
				ivjstcLblRenewalRecipientName = new JLabel();
				ivjstcLblRenewalRecipientName.setName(
					"stcLblRenewalRecipientName");
				ivjstcLblRenewalRecipientName.setBounds(
					313,
					21,
					221,
					20);
				ivjstcLblRenewalRecipientName.setText(RENWL_RECIPIENT);
				ivjstcLblRenewalRecipientName.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblRenewalRecipientName.setMinimumSize(
					new java.awt.Dimension(45, 14));
				// user code begin {1}
				ivjstcLblRenewalRecipientName.setDisplayedMnemonic(
					KeyEvent.VK_A);
				ivjstcLblRenewalRecipientName.setLabelFor(
					gettxtRenewalRecipientName());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblRenewalRecipientName;
	}

	/**
	 * Return the ivjstcLblUnitNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblUnitNo()
	{

		if (ivjstcLblUnitNo == null)
		{
			try
			{
				ivjstcLblUnitNo = new JLabel();
				ivjstcLblUnitNo.setSize(50, 18);
				ivjstcLblUnitNo.setName("ivjstcLblUnitNo");
				ivjstcLblUnitNo.setText(UNIT_NO);
				ivjstcLblUnitNo.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblUnitNo.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblUnitNo.setHorizontalAlignment(4);
				ivjstcLblUnitNo.setLocation(85, 51);
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
		return ivjstcLblUnitNo;
	}

	/**
	 * Return the ivjstcLblVehicleLocationAddress property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVehicleLocationAddress()
	{
		if (ivjstcLblVehicleLocationAddress == null)
		{
			try
			{
				ivjstcLblVehicleLocationAddress = new JLabel();
				ivjstcLblVehicleLocationAddress.setName(
					"ivjstcLblVehicleLocationAddress");
				ivjstcLblVehicleLocationAddress.setText(
					VEH_LOC_ADDRESS);
				ivjstcLblVehicleLocationAddress.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblVehicleLocationAddress.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblVehicleLocationAddress.setDisplayedMnemonic(
					KeyEvent.VK_V);
				ivjstcLblVehicleLocationAddress.setLabelFor(
					gettxtVehicleLocationStreet1());
				// user code end
				ivjstcLblVehicleLocationAddress.setSize(221, 20);
				ivjstcLblVehicleLocationAddress.setLocation(313, 229);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblVehicleLocationAddress;
	}

	/**
	 * Return the ivjtxtDealerGDN property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtDealerGDN()
	{
		if (ivjtxtDealerGDN == null)
		{
			try
			{
				ivjtxtDealerGDN = new RTSInputField();
				ivjtxtDealerGDN.setBounds(145, 30, 80, 18);
				ivjtxtDealerGDN.setName("ivjtxtDealerGDN");
				ivjtxtDealerGDN.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtDealerGDN.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtDealerGDN.setMaxLength(
					CommonConstant.DLRGDN_MAX_LENGTH);
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
		return ivjtxtDealerGDN;
	}

	/**
	 * Return the ivjtxtOwnerCity property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerCity()
	{
		if (ivjtxtOwnerCity == null)
		{
			try
			{
				ivjtxtOwnerCity = new RTSInputField();
				ivjtxtOwnerCity.setName("ivjtxtOwnerCity");
				ivjtxtOwnerCity.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerCity.setBounds(5, 2, 135, 20);
				ivjtxtOwnerCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
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
		return ivjtxtOwnerCity;
	}

	/**
	 * Return the ivjtxtOwnerCntry property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerCntry()
	{
		if (ivjtxtOwnerCntry == null)
		{
			try
			{
				ivjtxtOwnerCntry = new RTSInputField();
				ivjtxtOwnerCntry.setBounds(148, 2, 40, 20);
				ivjtxtOwnerCntry.setName("ivjtxtOwnerCntry");
				ivjtxtOwnerCntry.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerCntry.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerCntry.setMaxLength(
					CommonConstant.LENGTH_CNTRY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtOwnerCntry;
	}

	/**
	 * Return the ivjtxtOwnerCntryZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerCntryZpcd()
	{
		if (ivjtxtOwnerCntryZpcd == null)
		{
			try
			{
				ivjtxtOwnerCntryZpcd = new RTSInputField();
				ivjtxtOwnerCntryZpcd.setBounds(203, 2, 70, 20);
				ivjtxtOwnerCntryZpcd.setName("ivjtxtOwnerCntryZpcd");
				ivjtxtOwnerCntryZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtOwnerCntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtOwnerCntryZpcd.setMaxLength(
					CommonConstant.LENGTH_CNTRY_ZIP);
				// user code end
				ivjtxtOwnerCntryZpcd.setText("123456789");
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtOwnerCntryZpcd;
	}

	/**
	 * Return the ivjtxtOwnerId property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerId()
	{
		if (ivjtxtOwnerId == null)
		{
			try
			{
				ivjtxtOwnerId = new RTSInputField();
				ivjtxtOwnerId.setBounds(193, 6, 81, 20);
				ivjtxtOwnerId.setName("ivjtxtOwnerId");
				ivjtxtOwnerId.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtOwnerId.setInput(RTSInputField.NUMERIC_ONLY);
				// defect 10127 
				ivjtxtOwnerId.setMaxLength(
					CommonConstant.LENGTH_OWNERID);
				// end defect 10127
				ivjtxtOwnerId.setFocusable(true);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtOwnerId;
	}

	/**
	 * Return the ivjtxtOwnerName1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerName1()
	{
		if (ivjtxtOwnerName1 == null)
		{
			try
			{
				ivjtxtOwnerName1 = new RTSInputField();
				ivjtxtOwnerName1.setBounds(6, 27, 269, 20);
				ivjtxtOwnerName1.setName("ivjtxtOwnerName1");
				ivjtxtOwnerName1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerName1.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerName1.setMaxLength(
					CommonConstant.LENGTH_NAME);
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
		return ivjtxtOwnerName1;
	}

	/**
	 * Return the ivjtxtOwnerName2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerName2()
	{
		if (ivjtxtOwnerName2 == null)
		{
			try
			{
				ivjtxtOwnerName2 = new RTSInputField();
				ivjtxtOwnerName2.setBounds(6, 49, 269, 20);
				ivjtxtOwnerName2.setName("ivjtxtOwnerName2");
				ivjtxtOwnerName2.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerName2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerName2.setMaxLength(
					CommonConstant.LENGTH_NAME);
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
		return ivjtxtOwnerName2;
	}

	/**
	 * Return the ivjtxtOwnerState property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerState()
	{
		if (ivjtxtOwnerState == null)
		{
			try
			{
				ivjtxtOwnerState = new RTSInputField();
				ivjtxtOwnerState.setName("ivjtxtOwnerState");
				ivjtxtOwnerState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerState.setText(CommonConstant.STR_TX);
				ivjtxtOwnerState.setBounds(149, 2, 25, 20);
				ivjtxtOwnerState.setInput(RTSInputField.ALPHA_ONLY);
				ivjtxtOwnerState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// user code begin {1}
				//end defect 7844
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtOwnerState;
	}

	/**
	 * Return the ivjtxtOwnerStreet1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerStreet1()
	{
		if (ivjtxtOwnerStreet1 == null)
		{
			try
			{
				ivjtxtOwnerStreet1 = new RTSInputField();
				ivjtxtOwnerStreet1.setBounds(6, 101, 269, 20);
				ivjtxtOwnerStreet1.setName("ivjtxtOwnerStreet1");
				ivjtxtOwnerStreet1.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
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
		return ivjtxtOwnerStreet1;
	}

	/**
	 * Return the ivjtxtOwnerStreet2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerStreet2()
	{
		if (ivjtxtOwnerStreet2 == null)
		{
			try
			{
				ivjtxtOwnerStreet2 = new RTSInputField();
				ivjtxtOwnerStreet2.setBounds(6, 124, 269, 20);
				ivjtxtOwnerStreet2.setName("ivjtxtOwnerStreet2");
				ivjtxtOwnerStreet2.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
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
		return ivjtxtOwnerStreet2;
	}

	/**
	 * Return the ivjtxtOwnerZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerZpcd()
	{
		if (ivjtxtOwnerZpcd == null)
		{
			try
			{
				ivjtxtOwnerZpcd = new RTSInputField();
				ivjtxtOwnerZpcd.setBounds(181, 2, 43, 20);
				ivjtxtOwnerZpcd.setName("ivjtxtOwnerZpcd");
				ivjtxtOwnerZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
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
		return ivjtxtOwnerZpcd;
	}

	/**
	 * Return the ivjtxtOwnerZpcdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerZpcdP4()
	{
		if (ivjtxtOwnerZpcdP4 == null)
		{
			try
			{
				ivjtxtOwnerZpcdP4 = new RTSInputField();
				ivjtxtOwnerZpcdP4.setBounds(240, 2, 34, 20);
				ivjtxtOwnerZpcdP4.setName("ivjtxtOwnerZpcdP4");
				ivjtxtOwnerZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
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
		return ivjtxtOwnerZpcdP4;
	}

	/**
	 * Return the ivjtxtPreviousOwnerCity property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPreviousOwnerCity()
	{
		if (ivjtxtPreviousOwnerCity == null)
		{
			try
			{
				ivjtxtPreviousOwnerCity = new RTSInputField();
				ivjtxtPreviousOwnerCity.setBounds(6, 225, 104, 20);
				ivjtxtPreviousOwnerCity.setName(
					"ivjtxtPreviousOwnerCity");
				ivjtxtPreviousOwnerCity.setInput(RTSInputField.DEFAULT);
				ivjtxtPreviousOwnerCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// defect 10127 
				ivjtxtPreviousOwnerCity.setMaxLength(
					CommonConstant.LENGTH_PREV_OWNR_CITY);
				// end defect 10127 
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
		return ivjtxtPreviousOwnerCity;
	}

	/**
	 * Return the ivjtxtPreviousOwnerName property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPreviousOwnerName()
	{
		if (ivjtxtPreviousOwnerName == null)
		{
			try
			{
				ivjtxtPreviousOwnerName = new RTSInputField();
				ivjtxtPreviousOwnerName.setBounds(6, 202, 221, 20);
				ivjtxtPreviousOwnerName.setName(
					"ivjtxtPreviousOwnerName");
				ivjtxtPreviousOwnerName.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

				// user code begin {1}
				ivjtxtPreviousOwnerName.setInput(RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtPreviousOwnerName.setMaxLength(
					CommonConstant.LENGTH_PREV_OWNR_NAME);
				// end defect 10127 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtPreviousOwnerName;
	}

	/**
	 * Return the ivjtxtPreviousOwnerState property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPreviousOwnerState()
	{
		if (ivjtxtPreviousOwnerState == null)
		{
			try
			{
				ivjtxtPreviousOwnerState = new RTSInputField();
				ivjtxtPreviousOwnerState.setBounds(113, 225, 25, 20);
				ivjtxtPreviousOwnerState.setName(
					"txtPreviousOwnerState");
				ivjtxtPreviousOwnerState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtPreviousOwnerState.setInput(
					RTSInputField.ALPHA_ONLY);
				ivjtxtPreviousOwnerState.setText(CommonConstant.STR_TX);
				ivjtxtPreviousOwnerState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtPreviousOwnerState;
	}

	/**
	 * This method initializes ivjtxtRecpntEMail
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRecpntEMail()
	{
		if (ivjtxtRecpntEMail == null)
		{
			ivjtxtRecpntEMail = new RTSInputField();
			ivjtxtRecpntEMail.setSize(300, 20);
			ivjtxtRecpntEMail.setLocation(313, 197);
			ivjtxtRecpntEMail.setInput(RTSInputField.DEFAULT);
			ivjtxtRecpntEMail.setMaxLength(CommonConstant.LENGTH_EMAIL);
			ivjtxtRecpntEMail.setText("");
			ivjtxtRecpntEMail.addKeyListener(this);
			ivjtxtRecpntEMail.addActionListener(this);
		}
		return ivjtxtRecpntEMail;
	}

	/**
	 * Return the ivjtxtRenewalMailingCity property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalMailingCity()
	{
		if (ivjtxtRenewalMailingCity == null)
		{
			try
			{
				ivjtxtRenewalMailingCity = new RTSInputField();
				ivjtxtRenewalMailingCity.setSize(135, 20);
				ivjtxtRenewalMailingCity.setName(
					"ivjtxtRenewalMailingCity");
				ivjtxtRenewalMailingCity.setInput(
					RTSInputField.DEFAULT);
				ivjtxtRenewalMailingCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalMailingCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
				// user code begin {1}
				ivjtxtRenewalMailingCity.setLocation(313, 140);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtRenewalMailingCity;
	}

	/**
	 * Return the ivjtxtRenewalMailingState property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalMailingState()
	{
		if (ivjtxtRenewalMailingState == null)
		{
			try
			{
				ivjtxtRenewalMailingState = new RTSInputField();
				ivjtxtRenewalMailingState.setName(
					"ivjtxtRenewalMailingState");
				ivjtxtRenewalMailingState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalMailingState.setBounds(457, 140, 25, 20);
				// user code begin {1}
				ivjtxtRenewalMailingState.setInput(
					RTSInputField.ALPHA_ONLY);
				ivjtxtRenewalMailingState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtRenewalMailingState;
	}

	/**
	 * Return the ivjtxtRenewalMailingStreet1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalMailingStreet1()
	{
		if (ivjtxtRenewalMailingStreet1 == null)
		{
			try
			{
				ivjtxtRenewalMailingStreet1 = new RTSInputField();
				ivjtxtRenewalMailingStreet1.setName(
					"txtRenewalMailingStreet1");
				ivjtxtRenewalMailingStreet1.setInput(
					RTSInputField.DEFAULT);
				ivjtxtRenewalMailingStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalMailingStreet1.setBounds(313, 96, 269, 20);
				// user code begin {1}
				ivjtxtRenewalMailingStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtRenewalMailingStreet1;
	}

	/**
	 * Return the ivjtxtRenewalMailingStreet2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalMailingStreet2()
	{
		if (ivjtxtRenewalMailingStreet2 == null)
		{
			try
			{
				ivjtxtRenewalMailingStreet2 = new RTSInputField();
				ivjtxtRenewalMailingStreet2.setName(
					"ivjtxtRenewalMailingStreet2");
				ivjtxtRenewalMailingStreet2.setInput(
					RTSInputField.DEFAULT);
				ivjtxtRenewalMailingStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalMailingStreet2.setBounds(
					313,
					118,
					269,
					20);
				// user code begin {1}
				ivjtxtRenewalMailingStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtRenewalMailingStreet2;
	}

	/**
	 * Return the ivjtxtRenewalMailingZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalMailingZpcd()
	{
		if (ivjtxtRenewalMailingZpcd == null)
		{
			try
			{
				ivjtxtRenewalMailingZpcd = new RTSInputField();
				ivjtxtRenewalMailingZpcd.setName(
					"ivjtxtRenewalMailingZpcd");
				ivjtxtRenewalMailingZpcd.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtRenewalMailingZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalMailingZpcd.setBounds(487, 140, 43, 20);
				// user code begin {1}
				ivjtxtRenewalMailingZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtRenewalMailingZpcd;
	}

	/**
	 * Return the ivjtxtRenewalMailingZpCdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalMailingZpcdP4()
	{
		if (ivjtxtRenewalMailingZpCdP4 == null)
		{
			try
			{
				ivjtxtRenewalMailingZpCdP4 = new RTSInputField();
				ivjtxtRenewalMailingZpCdP4.setName(
					"ivjtxtRenewalMailingZpCdP4");
				ivjtxtRenewalMailingZpCdP4.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtRenewalMailingZpCdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalMailingZpCdP4.setBounds(548, 140, 34, 20);
				// user code begin {1}
				ivjtxtRenewalMailingZpCdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtRenewalMailingZpCdP4;
	}

	/**
	 * Return the ivjtxtRenewalRecipientName property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalRecipientName()
	{
		if (ivjtxtRenewalRecipientName == null)
		{
			try
			{
				ivjtxtRenewalRecipientName = new RTSInputField();
				ivjtxtRenewalRecipientName.setSize(269, 20);
				ivjtxtRenewalRecipientName.setName(
					"ivjtxtRenewalRecipientName");
				ivjtxtRenewalRecipientName.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalRecipientName.setInput(
					RTSInputField.DEFAULT);
				ivjtxtRenewalRecipientName.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// user code end
				ivjtxtRenewalRecipientName.setLocation(313, 42);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtRenewalRecipientName;
	}

	/**
	 * Return the ivjtxtResComptCntyNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtResComptCntyNo()
	{
		if (ivjtxtResComptCntyNo == null)
		{
			try
			{
				ivjtxtResComptCntyNo = new RTSInputField();
				ivjtxtResComptCntyNo.setBounds(145, 9, 39, 18);
				ivjtxtResComptCntyNo.setName("ivjtxtResComptCntyNo");
				ivjtxtResComptCntyNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				// defect 10127 
				ivjtxtResComptCntyNo.setMaxLength(
					CommonConstant.LENGTH_OFFICE_ISSUANCENO);
				// end defect 10127 
				ivjtxtResComptCntyNo.setInput(
					RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtResComptCntyNo;
	}

	/**
	 * Return the ivjtxtVehicleLocationCity property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleLocationCity()
	{
		if (ivjtxtVehicleLocationCity == null)
		{
			try
			{
				ivjtxtVehicleLocationCity = new RTSInputField();
				ivjtxtVehicleLocationCity.setName(
					"ivjtxtVehicleLocationCity");
				ivjtxtVehicleLocationCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleLocationCity.setBounds(313, 294, 135, 20);
				// user code begin {1}
				ivjtxtVehicleLocationCity.setInput(
					RTSInputField.DEFAULT);
				ivjtxtVehicleLocationCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleLocationCity;
	}

	/**
	 * Return the ivjtxtVehicleLocationState property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleLocationState()
	{
		if (ivjtxtVehicleLocationState == null)
		{
			try
			{
				ivjtxtVehicleLocationState = new RTSInputField();
				ivjtxtVehicleLocationState.setName(
					"ivjtxtVehicleLocationState");
				ivjtxtVehicleLocationState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleLocationState.setBounds(457, 294, 25, 20);

				// user code begin {1}
				ivjtxtVehicleLocationState.setInput(
					RTSInputField.ALPHA_ONLY);
				ivjtxtVehicleLocationState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleLocationState;
	}

	/**
	 * Return the ivjtxtVehicleLocationStreet1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleLocationStreet1()
	{
		if (ivjtxtVehicleLocationStreet1 == null)
		{
			try
			{
				ivjtxtVehicleLocationStreet1 = new RTSInputField();
				ivjtxtVehicleLocationStreet1.setName(
					"ivjtxtVehicleLocationStreet1");
				ivjtxtVehicleLocationStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleLocationStreet1.setBounds(
					313,
					250,
					269,
					20);
				// user code begin {1}
				ivjtxtVehicleLocationStreet1.setInput(
					RTSInputField.DEFAULT);
				ivjtxtVehicleLocationStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleLocationStreet1;
	}

	/**
	 * Return the ivjtxtVehicleLocationStreet2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleLocationStreet2()
	{
		if (ivjtxtVehicleLocationStreet2 == null)
		{
			try
			{
				ivjtxtVehicleLocationStreet2 = new RTSInputField();
				ivjtxtVehicleLocationStreet2.setName(
					"ivjtxtVehicleLocationStreet2");
				ivjtxtVehicleLocationStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleLocationStreet2.setBounds(
					313,
					272,
					269,
					20);
				// user code begin {1}
				ivjtxtVehicleLocationStreet2.setInput(
					RTSInputField.DEFAULT);
				ivjtxtVehicleLocationStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleLocationStreet2;
	}

	/**
	 * Return the ivjtxtVehicleLocationZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleLocationZpcd()
	{
		if (ivjtxtVehicleLocationZpcd == null)
		{
			try
			{
				ivjtxtVehicleLocationZpcd = new RTSInputField();
				ivjtxtVehicleLocationZpcd.setName(
					"ivjtxtVehicleLocationZpcd");

				ivjtxtVehicleLocationZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleLocationZpcd.setBounds(487, 294, 43, 20);
				// user code begin {1}
				ivjtxtVehicleLocationZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				ivjtxtVehicleLocationZpcd.setInput(
					RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleLocationZpcd;
	}

	/**
	 * Return the ivjtxtVehicleLocationZpcdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVehicleLocationZpcdP4()
	{
		if (ivjtxtVehicleLocationZpcdP4 == null)
		{
			try
			{
				ivjtxtVehicleLocationZpcdP4 = new RTSInputField();
				ivjtxtVehicleLocationZpcdP4.setName(
					"ivjtxtVehicleLocationZpcdP4");
				ivjtxtVehicleLocationZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleLocationZpcdP4.setBounds(546, 294, 34, 20);
				// user code begin {1}
				ivjtxtVehicleLocationZpcdP4.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtVehicleLocationZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleLocationZpcdP4;
	}

	/**
	 * Return the ivjtxtVehicleUnitNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleUnitNo()
	{
		if (ivjtxtVehicleUnitNo == null)
		{
			try
			{
				ivjtxtVehicleUnitNo = new RTSInputField();
				ivjtxtVehicleUnitNo.setBounds(145, 51, 58, 18);
				ivjtxtVehicleUnitNo.setName("ivjtxtVehicleUnitNo");
				ivjtxtVehicleUnitNo.setInput(RTSInputField.DEFAULT);
				ivjtxtVehicleUnitNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// defect 10127
				ivjtxtVehicleUnitNo.setMaxLength(
					CommonConstant.LENGTH_UNIT_NO);
				// end defect 10127 
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
		return ivjtxtVehicleUnitNo;
	}

	/**
	 * 
	 * Enable/Disabled Delete Buttons per data
	 * 
	 */
	private void setupDeleteButtons()
	{
		getbtnDeleteRecipient().setEnabled(
			!(gettxtRenewalRecipientName().isEmpty()));
		getbtnDeleteEMail().setEnabled(
			!(gettxtRecpntEMail().isEmpty()));
		getbtnDeleteRenewal().setEnabled(
			!caRenwlAddrComp.isUSAAddressEmpty());
		getbtnDeleteLocation().setEnabled(
			!caVehLocAddrComp.isUSAAddressEmpty());
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEexception Throwable
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
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(ScreenConstant.TTL007_FRAME_NAME);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(625, 534);
			setTitle(ScreenConstant.TTL007_FRAME_TITLE);
			setContentPane(getFrmOwnerEntryTTL007ContentPane1());
			setRequestFocus(false);
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// defect 10127 
		caOwnrNameAddrComp =
			new NameAddressComponent(
				gettxtOwnerName1(),
				gettxtOwnerName2(),
				gettxtOwnerStreet1(),
				gettxtOwnerStreet2(),
				gettxtOwnerCity(),
				gettxtOwnerState(),
				gettxtOwnerZpcd(),
				gettxtOwnerZpcdP4(),
				gettxtOwnerCntry(),
				gettxtOwnerCntryZpcd(),
				getchkUSA(),
				getstcLblDash(),
				ErrorsConstant.ERR_NUM_INCOMPLETE_OWNR_DATA,
				ErrorsConstant.ERR_NUM_INCOMPLETE_OWNR_DATA,
				CommonConstant.TX_DEFAULT_STATE);

		caRenwlAddrComp =
			new NameAddressComponent(
				gettxtRenewalMailingStreet1(),
				gettxtRenewalMailingStreet2(),
				gettxtRenewalMailingCity(),
				gettxtRenewalMailingState(),
				gettxtRenewalMailingZpcd(),
				gettxtRenewalMailingZpcdP4(),
				ErrorsConstant.ERR_NUM_INCOMPLETE_REN_ADDR_DATA);

		caVehLocAddrComp =
			new NameAddressComponent(
				gettxtVehicleLocationStreet1(),
				gettxtVehicleLocationStreet2(),
				gettxtVehicleLocationCity(),
				gettxtVehicleLocationState(),
				gettxtVehicleLocationZpcd(),
				gettxtVehicleLocationZpcdP4(),
				ErrorsConstant.ERR_NUM_INCOMPLETE_VEH_ADDR_DATA);
		// end defect 10127

		// defect 10299  
		cvAddlMFValid = new Vector();
		cvAddlMFValid.add(gettxtPreviousOwnerName());
		cvAddlMFValid.add(gettxtPreviousOwnerCity());
		cvAddlMFValid.add(gettxtPreviousOwnerState());
		cvAddlMFValid.add(gettxtRenewalRecipientName());
		// end defect 10299 

		getRootPane().setDefaultButton(getButtonPanel1().getBtnEnter());

		// defect 10130 
		gettxtOwnerId().setEnabled(SystemProperty.isHQ());
		getstcLblOwnerId().setEnabled(SystemProperty.isHQ());
		// end defect 10130
		// user code end
	}

	/**
	 * Return if Corrected Title.
	 * 
	 * @return boolean
	 */
	private boolean isCorTtl()
	{
		return csTransCd.equals(TransCdConstant.CORTTL);
	}

	/**
	 * Check for Lien data in DTA transaction.
	 * 
	 * @return boolean
	 */
	private boolean isDTALienPresent()
	{
		TitleValidObj laValidationObj =
			(TitleValidObj) caVehInqData.getValidationObject();

		DealerTitleData laDlrTtlData =
			(DealerTitleData) laValidationObj.getDlrTtlData();

		// defect 10112 
		// True if Lien Errors OR hasLien() 
		return (
			laDlrTtlData.getLienError() != null
				&& laDlrTtlData.getLienError().size() > 0)
			|| laDlrTtlData.getMFVehicleData().getTitleData().hasLien();
		// end defect 10112 
	}

	/**
	 * Return is Non-Titled 
	 * 
	 * @return boolean
	 */
	private boolean isNonTitled()
	{
		TitleData laTitleData =
			(TitleData) caVehInqData.getMfVehicleData().getTitleData();

		// defect 10112 
		return laTitleData.getTtlTypeIndi() == TitleTypes.INT_NONTITLED;
		// end defect 10112 
	}

	/**
	 * Return is Record Lien selected.
	 * 
	 * @return boolean
	 */
	public boolean isRecordLien()
	{
		return getchkRecordLien().isSelected();
	}

	/**
	 * Return is Transcd = "REJCOR"
	 * 
	 * @return boolean 
	 */
	private boolean isRejCor()
	{
		return csTransCd.equals(TransCdConstant.REJCOR);
	}

	/**
	 * Return true if RPO and prev owner name same as new 
	 *  
	 */
	private boolean isSameOwnerRPO()
	{
		boolean lbReturn = false;

		String lsOthrSt =
			caVehInqData
				.getMfVehicleData()
				.getTitleData()
				.getOthrStateCntry();

		// Don't allow an RPO with the same owner name
		if (caVehInqData
			.getMfVehicleData()
			.getTitleData()
			.getTtlTypeIndi()
			== TitleTypes.INT_REGPURPOSE
			&& caVehInqData
				.getMfVehicleData()
				.getTitleData()
				.getDocTypeCd()
				== DocTypeConstant.REGULAR_TITLE //== 1
			&& caVehInqData
				.getMfVehicleData()
				.getTitleData()
				.getSurrTtlDate()
				== 0
			&& (UtilityMethods.isEmpty(lsOthrSt))
			&& caVehInqData.getNoMFRecs() == 1
			&& !isRejCor())
		{
			TitleValidObj laValidObj =
				(TitleValidObj) caVehInqData.getValidationObject();

			String lsOwnrOrig = null;

			if (laValidObj != null)
			{
				MFVehicleData laMfOrig =
					(MFVehicleData) laValidObj.getMfVehOrig();

				lsOwnrOrig = laMfOrig.getOwnerData().getName1();

				String lsOwnrName1 = gettxtOwnerName1().getText();

				if (lsOwnrOrig != null
					&& lsOwnrOrig.equals(lsOwnrName1))
				{
					//	205);
					RTSException leRTSEx =
						new RTSException(
							ErrorsConstant.ERR_NUM_RPO_CANCELLED);
					leRTSEx.displayError(this);
					lbReturn = true;
				}
			}
		}
		return lbReturn;
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		// defect 10592
		if (aaIE.getSource() instanceof JCheckBox)
		{
			JCheckBox laChkbx = (JCheckBox) aaIE.getSource();

			if (laChkbx == getchkUSA())
			{
				caOwnrNameAddrComp.resetPerUSA(
					aaIE.getStateChange() == ItemEvent.SELECTED);
			}
			else if (
				laChkbx == getchkEReminder()
					&& aaIE.getStateChange() == ItemEvent.DESELECTED
					&& gettxtRecpntEMail().isEmpty())
			{
				clearAllColor(gettxtRecpntEMail());
			}

		}
	}

	// defect 10592 
	//	/**
	//	 * Save owner data.
	//	 * 
	//	 * @param abEnter 
	//	 */
	//	private void saveOwnerData(boolean abEnter)
	//	{
	//		OwnerData laOwnerData = caOwnerData;
	//
	//		if (!abEnter)
	//		{
	//			laOwnerData = new OwnerData();
	//		}
	//		setOwnerDataFromDisplay(laOwnerData);
	//
	//		if (csTransCd.equals(TransCdConstant.TITLE)
	//			|| csTransCd.equals(TransCdConstant.NONTTL)
	//			|| csTransCd.equals(TransCdConstant.DTANTK)
	//			|| csTransCd.equals(TransCdConstant.DTAORK))
	//		{
	//			getController().getMediator().closeVault(
	//				ScreenConstant.TTL007,
	//				UtilityMethods.copy(laOwnerData));
	//		}
	//	}
	// end defect 10592 

	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		if (aaKE.getKeyChar() == KeyEvent.VK_ESCAPE)
		{
			saveScreenTTL007Data();

			getController().processData(
				AbstractViewController.CANCEL,
				null);

		}
		else
		{
			setupDeleteButtons();
		}
	}

	/** 
	 * Save Screen Data for later Display 
	 */
	private void saveScreenTTL007Data()
	{
		ScreenTTL007SavedData laTTL007Data =
			new ScreenTTL007SavedData();
		TitleData laTitleData = new TitleData();
		RegistrationData laRegisData = new RegistrationData();
		OwnerData laOwnerData = new OwnerData();

		setOwnerDataFromDisplay(laOwnerData);
		setRegisDataFromDisplay(laRegisData);
		setTitleDataFromDisplay(laTitleData);

		laTTL007Data.setOwnerData(laOwnerData);
		laTTL007Data.setRegisData(laRegisData);
		laTTL007Data.setTitleData(laTitleData);

		boolean lbRecordLien = getchkRecordLien().isSelected();

		if (getchkRecordLien().isEnabled()
			&& lbRecordLien != cbInitRecordLien)
		{
			laTTL007Data.setModifiedRecordLien(true);
			laTTL007Data.setRecordLien(getchkRecordLien().isSelected());
		}

		getController().getMediator().closeVault(
			ScreenConstant.TTL007,
			UtilityMethods.copy(laTTL007Data));
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information to the 
	 * view
	 * 
	 * @param aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		// defect 10112/ 10127 
		csTransCd = getController().getTransCode();

		try
		{
			gettxtOwnerId().requestFocus();

			if (aaDataObject != null)
			{
				// Get data objects
				caVehInqData = (VehicleInquiryData) aaDataObject;

				caOwnerData =
					(OwnerData) caVehInqData
						.getMfVehicleData()
						.getOwnerData();

				caTitleData =
					(TitleData) caVehInqData
						.getMfVehicleData()
						.getTitleData();

				caRegisData =
					(RegistrationData) caVehInqData
						.getMfVehicleData()
						.getRegData();

				disableRenewalFieldsifApportioned();

				enableSpclPlatesifAttached();

				setResComptCntyNo(caRegisData.getResComptCntyNo());

				// Set Address for CORTTL, REJCOR 
				//	correct title rejection
				if (isCorTtl() || isRejCor())
				{
					setOwnerDataToDisplay(caOwnerData);
					setNonOwnerInfoToDisplay(caTitleData, caRegisData);
				}
				else
				{
					setOwnerDataToDisplay(new OwnerData());
				}

				// Default Record Lien checkbox
				getchkRecordLien().setSelected(!isNonTitled());
				getchkRecordLien().setEnabled(
					!isNonTitled() && !isCorTtl() && !isRejCor());

				// Preset to "TX" if state empty 
				if (gettxtOwnerState().isEmpty())
				{
					gettxtOwnerState().setText(CommonConstant.STR_TX);
				}

				if (gettxtPreviousOwnerState().isEmpty())
				{
					gettxtPreviousOwnerState().setText(
						CommonConstant.STR_TX);
				}

				// defect 10604 
				// setDocTypeCd();
				// end defect 10604

				// defect 10508
				setEReminderToDisplay();
				// defect 10508 

				// defect 10592 
				cbInitRecordLien = getchkRecordLien().isSelected();

				Object laTTL007 =
					getController().getMediator().openVault(
						ScreenConstant.TTL007);

				if (laTTL007 != null
					&& laTTL007 instanceof ScreenTTL007SavedData)
				{
					ScreenTTL007SavedData laTTL007Data =
						(ScreenTTL007SavedData) laTTL007;

					setSavedDataToDisplay(laTTL007Data);
				}
				else
				{
					gettxtOwnerId().setText(caVehInqData.getOwnerId());

					OwnerData laUserSuppl =
						caVehInqData
							.getMfVehicleData()
							.getUserSuppliedOwnerData();

					setOwnerDataToDisplay(laUserSuppl);

					// Set Data from DealerTitleData object
					doDlrTtl();
				}
				setupDeleteButtons();

				// end defect 10592
			}
		}
		catch (NullPointerException aeNPE)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNPE);
			leRTSEx.displayError(this);
			leRTSEx = null;
		}
		catch (NumberFormatException aeNFE)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNFE);
			leRTSEx.displayError(this);
			leRTSEx = null;
		}
	}

	/**
	 * Set data to VehInqury Data.
	 * 
	 */
	private void setDataToDataObj()
	{
		setOwnerDataFromDisplay(caOwnerData);
		setRegisDataFromDisplay(caRegisData);
		setTitleDataFromDisplay(caTitleData);

		//		// REGISTRATION DATA
		//		// Recipient Name, ResComptCntyNo 
		//		caRegisData.setRecpntName(
		//			gettxtRenewalRecipientName().getText());
		//		caRegisData.setResComptCntyNo(
		//			Integer.parseInt(gettxtResComptCntyNo().getText()));
		//
		//		// Renewal Mailing Address
		//		// defect 10127  
		//		caRenwlAddrComp.setAddressToDataObject(
		//			caRegisData.getRenwlMailAddr());
		//		// end defect 10127  
		//
		//		// defect 10372 
		//		caRegisData.setRecpntEMail(gettxtRecpntEMail().getText());
		//		// end defect 10372 
		//
		//		// defect 10508 
		//		if (getchkEReminder().isEnabled())
		//		{
		//			int liEMailRenwlReqCd =
		//				getchkEReminder().isSelected() ? 1 : 0;
		//			caRegisData.setEMailRenwlReqCd(liEMailRenwlReqCd);
		//		}
		//		// end defect 10508 
		//
		//		// TITLE DATA 
		//		// Prev Owner 
		//		caTitleData.setPrevOwnrCity(
		//			gettxtPreviousOwnerCity().getText());
		//		caTitleData.setPrevOwnrName(
		//			gettxtPreviousOwnerName().getText());
		//		caTitleData.setPrevOwnrState(
		//			gettxtPreviousOwnerState().getText());
		//
		//		// Veh Loc Addr
		//		// defect 10127 
		//		caVehLocAddrComp.setAddressToDataObject(
		//			caTitleData.getTtlVehAddr());
		//		// end defect 10127 
		//
		//		caTitleData.setVehUnitNo(gettxtVehicleUnitNo().getText());
		//		caTitleData.setDlrGdn(gettxtDealerGDN().getText());
		// end defect 10112

		// defect 10508
		// defect 9971 
		//setDocTypeCd();
		// end defect 9971
		// end defect 10508

		// defect 10604
		caTitleData.setDocTypeCd(getDocTypeCd());
		// end defect 10604    

		clearExistingLienData();

		// defect 10592 
		saveScreenTTL007Data();
		// end defect 10592

	}

	/**
	 *  Set EReminder To Display 
	 */
	private void setEReminderToDisplay()
	{
		// defect 10604 
		int liDocTypeCd = getDocTypeCd();
		// end defect 10604  

		boolean lbEReminderApplicable =
			liDocTypeCd != DocTypeConstant.OFF_HIGHWAY_USE_ONLY
				&& liDocTypeCd != DocTypeConstant.INSURANCE_NO_REGIS_TITLE
				&& caRegisData.getRegClassCd()
					!= CommonConstant.TONLY_REGCLASSCD;

		getchkEReminder().setEnabled(lbEReminderApplicable);
	}

	//	/**
	//	 * Set Title DocTypeCd 
	//	 * 
	//	 */
	//	private void setDocTypeCd()
	//	{
	//		TitleData laTitleData =
	//			caVehInqData.getMfVehicleData().getTitleData();
	//
	//		// Set the document type code - Moved from TTL008
	//		if (laTitleData.getTtlTypeIndi() == TitleTypes.INT_REGPURPOSE)
	//		{
	//			//laTitleData.setDocTypeCd(5);
	//			laTitleData.setDocTypeCd(
	//				DocTypeConstant.REGISTRATION_PURPOSES_ONLY);
	//		}
	//		else if (
	//			laTitleData.getTtlTypeIndi() == TitleTypes.INT_NONTITLED)
	//		{
	//			//laTitleData.setDocTypeCd(9);
	//			laTitleData.setDocTypeCd(
	//				DocTypeConstant.NON_TITLED_VEHICLE);
	//		}
	//		else if (
	//			caVehInqData
	//				.getMfVehicleData()
	//				.getRegData()
	//				.getOffHwyUseIndi()
	//				== 1)
	//		{
	//			// laTitleData.setDocTypeCd(2);
	//			laTitleData.setDocTypeCd(
	//				DocTypeConstant.OFF_HIGHWAY_USE_ONLY);
	//		}
	//		else if (
	//			laTitleData.getTtlTypeIndi() == TitleTypes.INT_ORIGINAL
	//				&& caVehInqData.getNoMFRecs() == 0
	//				&& caVehInqData
	//					.getMfVehicleData()
	//					.getRegData()
	//					.getRegWaivedIndi()
	//					== 1)
	//		{
	//			//laTitleData.setDocTypeCd(11);
	//			laTitleData.setDocTypeCd(
	//				DocTypeConstant.INSURANCE_NO_REGIS_TITLE);
	//		}
	//		// defect 7724 Keep doctypecd 11 if regwaived on found 
	//		// doctypecd 11
	//		else if (
	//			laTitleData.getTtlTypeIndi() == TitleTypes.INT_ORIGINAL
	//				&& caVehInqData.getNoMFRecs() == 1
	//				&& caVehInqData
	//					.getMfVehicleData()
	//					.getRegData()
	//					.getRegWaivedIndi()
	//					== 1
	//			//&& laTitleData.getDocTypeCd() = 11 	
	//				&& laTitleData.getDocTypeCd()
	//					== DocTypeConstant.INSURANCE_NO_REGIS_TITLE)
	//		{
	//			// Review else logic
	//		}
	//		// end defect 7724				
	//		else if (
	//			laTitleData.getTtlTypeIndi() == TitleTypes.INT_ORIGINAL)
	//		{
	//			// laTitleData.setDocTypeCd(1);			
	//			laTitleData.setDocTypeCd(DocTypeConstant.REGULAR_TITLE);
	//		}
	//		// end defect 6206
	//		else if (
	//			laTitleData.getTtlTypeIndi()
	//				== TitleTypes
	//					.INT_CORRECTED //&& laTitleData.getDocTypeCd() != 2
	//		//				&& laTitleData.getDocTypeCd() != 10
	//		//				&& laTitleData.getDocTypeCd() != 11
	//				&& laTitleData.getDocTypeCd()
	//					!= DocTypeConstant.OFF_HIGHWAY_USE_ONLY
	//				&& laTitleData.getDocTypeCd()
	//					!= DocTypeConstant.OS_REGISTERED_APPORTIONED_VEH
	//				&& laTitleData.getDocTypeCd()
	//					!= DocTypeConstant.INSURANCE_NO_REGIS_TITLE)
	//		{
	//			// laTitleData.setDocTypeCd(1);
	//			laTitleData.setDocTypeCd(DocTypeConstant.REGULAR_TITLE);
	//		}
	//		// end defect 9971 
	//	}

	/**
	 * Set Prev Owner & Addr, Renwl Name, Addr, Veh Loc Addr, 
	 *   DLRGDN, UnitNo 
	 * 
	 * @param aaTitleData 
	 * @param aaRegisData 
	 */
	private void setNonOwnerInfoToDisplay(
		TitleData aaTitleData,
		RegistrationData aaRegisData)
	{
		// From TitleData - Prev Owner 
		gettxtPreviousOwnerName().setText(
			aaTitleData.getPrevOwnrName());
		gettxtPreviousOwnerCity().setText(
			aaTitleData.getPrevOwnrCity());
		gettxtPreviousOwnerState().setText(
			aaTitleData.getPrevOwnrState());

		// From RegistrationData  - Renewal Name/Addr 	
		gettxtRenewalRecipientName().setText(
			aaRegisData.getRecpntName());

		// defect 10127 
		// Renewal Address 
		caRenwlAddrComp.setAddressDataToDisplay(
			aaRegisData.getRenwlMailAddr());

		// From TitleData - Vehicle Location
		caVehLocAddrComp.setAddressDataToDisplay(
			aaTitleData.getTtlVehAddr());
		// end defect 10127 

		// defect 10508 
		// Use aaRegisData vs. caRegisData for DTA 
		// defect 10372 
		// Renewal EMail Address 
		if (aaRegisData.getRecpntEMail() != null)
		{
			gettxtRecpntEMail().setText(aaRegisData.getRecpntEMail());
		}
		// end defect 10372
		// end defect 10508  

		// defect 10592 
		// defect 10508
		if (getchkEReminder().isEnabled())
		{
			// end defect 10592 

			getchkEReminder().setSelected(aaRegisData.isEReminder());
		}
		// end defect 10508  

		// ResComptCntyNo 
		gettxtResComptCntyNo().setText(
			"" + aaRegisData.getResComptCntyNo());

		// DlrGDN 
		gettxtDealerGDN().setText(trimDlrGDN(aaTitleData.getDlrGdn()));

		// VehUnitNo 
		gettxtVehicleUnitNo().setText(aaTitleData.getVehUnitNo());
	}

	/**
	 * Set Owner Data From Display 
	 * 
	 * @param aaOwnerData
	 */
	private void setOwnerDataFromDisplay(OwnerData aaOwnerData)
	{
		AddressData laAddrData = aaOwnerData.getAddressData();
		aaOwnerData.setOwnrId(gettxtOwnerId().getText());

		aaOwnerData.setName1(gettxtOwnerName1().getText());
		aaOwnerData.setName2(gettxtOwnerName2().getText());

		laAddrData.setSt1(gettxtOwnerStreet1().getText());
		laAddrData.setSt2(gettxtOwnerStreet2().getText());
		laAddrData.setCity(gettxtOwnerCity().getText());

		if (getchkUSA().isSelected())
		{
			laAddrData.setState(gettxtOwnerState().getText());
			laAddrData.setZpcd(gettxtOwnerZpcd().getText());
			laAddrData.setZpcdp4(gettxtOwnerZpcdP4().getText());
			laAddrData.setCntry(CommonConstant.STR_SPACE_EMPTY);
		}
		else
		{
			laAddrData.setCntry(gettxtOwnerCntry().getText());
			laAddrData.setCntryZpcd(gettxtOwnerCntryZpcd().getText());
			laAddrData.setState(CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * Set Owner Name and Address  
	 * 
	 * @param aaOwnerData
	 */
	private void setOwnerDataToDisplay(OwnerData aaOwnerData)
	{
		if (aaOwnerData != null)
		{
			gettxtOwnerId().setText(aaOwnerData.getOwnrId());

			// defect 10127 
			caOwnrNameAddrComp.setNameAddressDataToDisplay(aaOwnerData);
			// end defect 10127 
		}
	}

	/**
	 * Set Registration Data from Display
	 *
	 * @param aaRegisData 
	 */
	private void setRegisDataFromDisplay(RegistrationData aaRegisData)
	{
		// Recipient Name, ResComptCntyNo 
		aaRegisData.setRecpntName(
			gettxtRenewalRecipientName().getText());

		aaRegisData.setResComptCntyNo(0);

		if (!gettxtResComptCntyNo().isEmpty())
		{
			aaRegisData.setResComptCntyNo(
				Integer.parseInt(gettxtResComptCntyNo().getText()));
		}

		// Renewal Mailing Address
		caRenwlAddrComp.setAddressToDataObject(
			aaRegisData.getRenwlMailAddr());

		aaRegisData.setRecpntEMail(gettxtRecpntEMail().getText());

		// defect 10821
		// Set even if disabled (where it will not be selected)  
		//if (getchkEReminder().isEnabled())
		//{
		int liEMailRenwlReqCd = getchkEReminder().isSelected() ? 1 : 0;
		aaRegisData.setEMailRenwlReqCd(liEMailRenwlReqCd);
		//}
		// end defect 10821 
	}

	/**
	 * Set RescomptCntyNo 
	 * 
	 * @param aiResComptCntyNo 
	 */
	private void setResComptCntyNo(int aiResComptCntyNo)
	{
		if (SystemProperty.isCounty())
		{
			if (!isRejCor())
			{
				aiResComptCntyNo = SystemProperty.getOfficeIssuanceNo();
			}
		}
		else
		{
			aiResComptCntyNo = 0;
		}
		gettxtResComptCntyNo().setText("" + aiResComptCntyNo);
	}

	/** 
	 * Display data saved earlier 
	 * 
	 * @param aaTTL007Data
	 */
	private void setSavedDataToDisplay(ScreenTTL007SavedData aaTTL007Data)
	{
		setOwnerDataToDisplay(aaTTL007Data.getOwnerData());

		setNonOwnerInfoToDisplay(
			aaTTL007Data.getTitleData(),
			aaTTL007Data.getRegisData());

		if (getchkRecordLien().isEnabled()
			&& aaTTL007Data.isModifiedRecordLien())
		{
			getchkRecordLien().setSelected(aaTTL007Data.isRecordLien());
		}
	}

	/**
	 * 
	 * Set SSN Color for fields in Array/Vector 
	 * 
	 * @param aaOwnerData
	 */
	private void setSSNColor(Object aaFieldSet, Color aaColor)
	{
		RTSInputField[] larrField;

		if (aaFieldSet instanceof Vector)
		{
			Vector lvField = (Vector) aaFieldSet;
			larrField =
				(RTSInputField[]) lvField.toArray(
					new RTSInputField[lvField.size()]);
		}
		else
		{
			larrField = (RTSInputField[]) aaFieldSet;
		}

		for (int i = 0; i < larrField.length; i++)
		{
			((RTSInputField) larrField[i]).setBackground(aaColor);
		}
	}

	/**
	 * Set Title Data From Display 
	 *
	 * @param aaTitleData 
	 */
	private void setTitleDataFromDisplay(TitleData aaTitleData)
	{
		// TITLE DATA 
		// Prev Owner 
		aaTitleData.setPrevOwnrCity(
			gettxtPreviousOwnerCity().getText());
		aaTitleData.setPrevOwnrName(
			gettxtPreviousOwnerName().getText());
		aaTitleData.setPrevOwnrState(
			gettxtPreviousOwnerState().getText());

		caVehLocAddrComp.setAddressToDataObject(
			aaTitleData.getTtlVehAddr());

		aaTitleData.setVehUnitNo(gettxtVehicleUnitNo().getText());
		aaTitleData.setDlrGdn(gettxtDealerGDN().getText());
	}

	/**
	 * Trim DLRGDN to Max length if required (from end) 
	 *
	 * @param asDlrGDN	String
	 * @return String 
	 */
	private String trimDlrGDN(String asDlrGDN)
	{
		if (asDlrGDN.length() > CommonConstant.DLRGDN_MAX_LENGTH)
		{
			asDlrGDN =
				asDlrGDN.substring(
					asDlrGDN.length()
						- CommonConstant.DLRGDN_MAX_LENGTH);
		}
		return asDlrGDN;
	}

	/**
	 * Validate EReminder 
	 * 
	 * @return
	 */
	private void validateEReminder(RTSException aeRTSEx)
	{
		if (getchkEReminder().isSelected()
			&& gettxtRecpntEMail().isEmpty())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtRecpntEMail());
		}
	}

	/**
	 * Validate Fields
	 *
	 * @return boolean 
	 */
	private boolean validateFields()
	{
		// defect 10299 
		// Moved from setDataToDataObj()
		UtilityMethods.trimRTSInputField(this);
		// end defect 10299 
		RTSException leRTSEx = new RTSException();

		boolean lbValid = true;

		// defect 10189
		// Move prior to Name/Address Validation 
		validateOwnrId(leRTSEx);
		// end defect 10189 

		// defect 10127 
		caOwnrNameAddrComp.validateNameAddressFields(leRTSEx);
		// end defect 10127 

		validatePrevOwnrInfo(leRTSEx);

		// defect 10299
		CommonValidations.addRTSExceptionForInvalidMFCharacters(
			cvAddlMFValid,
			leRTSEx);
		// end defect 10299

		validateRenwlMailngAddr(leRTSEx);

		// defect 10333 
		validateRecpntEMail(leRTSEx);
		// end defect 10333 

		// defect 10508
		validateEReminder(leRTSEx);
		// end defect 10508

		validateVehLocAddr(leRTSEx);

		validateResComptCntyNo(leRTSEx);

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;
	}

	/**
	 * Validate owner name does not contain SSN.
	 * 
	 * @return boolean
	 */
	private boolean validateNameWithSSN()
	{
		boolean lbValid = true;

		boolean lbVTRAuthReq =
			UtilityMethods.isEmpty(
				caVehInqData.getVehMiscData().getAuthCd());

		if (lbVTRAuthReq)
		{
			Vector lvFieldWithSSN = new Vector();

			for (int i = NAMEFIELD_ARRAY.length - 1; i >= 0; i--)
			{
				RTSInputField laField =
					(RTSInputField) NAMEFIELD_ARRAY[i];

				String lsText = laField.getText();
				if (CommonValidations.isStringWithSSN(lsText))
				{
					lvFieldWithSSN.add(laField);
				}
			}
			if (lvFieldWithSSN.size() != 0)
			{
				getController().processData(
					VCOwnerEntryTTL007.VTR_AUTH,
					caVehInqData);

				if (UtilityMethods
					.isEmpty(caVehInqData.getVehMiscData().getAuthCd()))
				{
					setSSNColor(lvFieldWithSSN, RTSException.ERR_COLOR);

					lbValid = false;
				}
			}
		}
		return lbValid;
	}

	/**
	 * Validate OwnerId if specified
	 * 
	 * @param aeRTSEx
	 */
	private void validateOwnrId(RTSException aeRTSEx)
	{
		// defect 10189 
		// add "isEnabled()"
		if (gettxtOwnerId().isEnabled()
			&& !gettxtOwnerId().isValidOwnerId())
		{
			// end defect 10189 

			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtOwnerId());
		}
	}

	/**
	 * Validate Previous Owner Information 
	 * 
	 * @param aeRTSEx
	 */
	private void validatePrevOwnrInfo(RTSException aeRTSEx)
	{
		// Empty & Reqd: ERR_NUM_ERR_NUM_INCOMPLETE_PREV_OWNR_DATA (190)
		// Invalid:  	  ERR_NUM_FIELD_ENTRY_INVALID   (150) 
		if (gettxtPreviousOwnerName().isEmpty())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_INCOMPLETE_PREV_OWNR_DATA),
				gettxtPreviousOwnerName());
		}
		if (gettxtPreviousOwnerCity().isEmpty())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_INCOMPLETE_PREV_OWNR_DATA),
				gettxtPreviousOwnerCity());
		}
		if (gettxtPreviousOwnerState().isEmpty())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_INCOMPLETE_PREV_OWNR_DATA),
				gettxtPreviousOwnerState());
		}
		// Invalid - 150 
		else if (
			gettxtPreviousOwnerState().getText().length()
				< CommonConstant.LENGTH_STATE)
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtPreviousOwnerState());
		}
	}

	/**
	 * Validate Recipient EMail Address 
	 * 
	 * @param aeRTSEx
	 */
	private void validateRecpntEMail(RTSException aeRTSEx)
	{
		// Validate EMail Address if supplied
		if (!CommonValidations
			.isValidEMail(gettxtRecpntEMail().getText()))
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtRecpntEMail());
		}
	}

	/**
	 * Validate Renwl Maining Address is complete 
	 * 
	 * @param aeRTSEx RTSException 
	 */
	private void validateRenwlMailngAddr(RTSException aeRTSEx)
	{
		// defect 10127 
		if (!caRenwlAddrComp.isUSAAddressEmpty())
		{
			caRenwlAddrComp.validateAddressFields(aeRTSEx);
		}
		// end defect 10127 

	}

	/**
	 * ValidateResComptCntyNo
	 * 
	 * @param aeRTSEx
	 */
	private void validateResComptCntyNo(RTSException aeRTSEx)
	{
		if (!gettxtResComptCntyNo().isValidCountyNo())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtResComptCntyNo());
		}
	}

	/**
	 * Validate owner address is complete.
	 * 
	 * @param aeRTSEx RTSException 
	 */
	private void validateVehLocAddr(RTSException aeRTSEx)
	{
		// defect 10127 
		if (!caVehLocAddrComp.isUSAAddressEmpty())
		{
			caVehLocAddrComp.validateAddressFields(aeRTSEx);
		}
		// end defect 10127 
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
