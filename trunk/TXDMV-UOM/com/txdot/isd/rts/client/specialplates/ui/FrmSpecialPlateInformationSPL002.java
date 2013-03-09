package com.txdot.isd.rts.client.specialplates.ui;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.specialplates.business.SpecialPlatesClientUtilityMethods;
import com.txdot.isd.rts.client.title.ui.FrmCountyConfirmCTL002;
import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * FrmSpecialPlateInformationSPL002.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/11/2003	Initial Prototype
 * K Harrell	02/11/2007	Imported modified version from VisualAge
 * 							defect 9085 Ver Special Plates  
 * J Rue		02/15/2007	Format Label and Input fields.
 * 							Add Record Not Applicable
 * 							Cleanup tabbing
 * 							add getchkRecordNotApplicable()
 * 							defect 9086 Ver Special Plates
 * K Harrell	02/21/2007	Working ....
 * 							defect 9085 Ver Special Plates 
 * J Rue		02/22/2007	Rename getSpclPltRegis() to 
 * 							getSpclPltRegisData()
 * 							modify setData()
 * 							defect 9086 Ver Special Plates
 * K Harrell	03/23/2007	Working ...
 * 							add'l actionPerformed() work
 * 							field defaults
 * 							add USA Address management
 * 							creation of VIInvAllocation Object 
 * 							add e-mail validation  
 * 							do not select 1st item in combo box
 * 							add "do you want to complete ..."
 * 							add HQ validation on County No. 
 * 							defect 9085 Ver Special Plates
 * J Rue		03/23/2007	Call ENTER for NON-SpecialPlates events
 * 							modify actionPerformed()
 * Jeff S.		03/29/2007	Add code to handle PLPDLR for this frame
 * 							and the sticker on the receipt.
 * 							add DEALER_NO_PREFIX
 * 							add handleDealerNoField()
 * 							modify actionPerformed(), gettxtDealerNo(),
 * 								validateFields(), setDataToDataObject()
 * 							defect 9145 Ver Special Plates
 * K Harrell	04/26/2007	Working...
 * 							Calculations of RegExpMo/Yr, 
 * 							max length on DealerNo, moved addition of 
 * 							Plate Sticker if isDlrPlt() 
 * 							modify handlePlateData(), validateExpMoYr()
 * 							 handleChrgSpclPltFee(), actionPerformed(),
 * 							 setDataToDataObject()	
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/01/2007	Added Msg to User re: Manufacture of new 
 * 							plate on SPRNW 
 * 						 	modify setupMfgRequestForNewPlateDesired()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/03/2007	RegExpYr enabled iff HQ;  Only validate Owner
 * 							Address if PltOwnrshpCd != "E"
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/03/2007	Removed 'g' as  mnemonic for Plate Age
 * 							Cannot modify
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/08/2007	Enable Exp Mo/Yr when in CORREG (as is in 
 * 							SPREV) 
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/15/2007	Reset csOrgNo upon selection of Plate Type
 * 							Phone No is no longer required
 * 							Cleanup Frame Layout
 * 							modify validateEMailPhoneNo(),
 * 								getRegPltCdFromDropDown()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/18/2007	Manipulate New Plates Desired on Manufacture
 * 							request.  Add assignment of PltBirthDate.
 * 							modify setDataToDataObject()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/25/2007	Modify VI calls for new VI architecture
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/31/2007	Removed RTSInputType, MaxLength for PlateAge
 * 							Do not verify Plate No on SPREV if not changed
 * 							add isSamePltNoOnSPREV()
 * 							modify gettxtPlateAge(), validatePlateNo()
 * 							defect 9085 Ver Special Plates   
 * B Hargrove	05/30/2007	Add check for 'NONTTL' TransCd when enabling
 * 							'Charge Special Plate Fee' checkbox.
 * 							modify handleChrgSpclPltFee()
 * 							defect 9126 Ver Special Plates
 * K Harrell	06/01/2007	Acknowledge different TransCds for 
 * 							Spcl Plate Application
 * 							modify isSPAPPL()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/06/2007	Only validate PLP length against 6 chars 
 * 							if SPRSRV || SPUNAC
 * 							add shouldValidatePlateNo()
 * 							modify validatePlateNo()
 *							defect 9085 Ver Special Plate
 * K Harrell	06/13/2007	Add SystemProperty.isHQ() to checks for 
 * 							Reserved & Unacceptable
 * 							modify isSPUNAC() && isSPRSRV()
 *							defect 9085 Ver Special Plate
 * K Harrell	06/18/2007	Added temporary text for Help
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/20/2007	Add'l work for CP fees 
 * 							modify calculateExpMoYr()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/21/2007	Do not allow the remanufacture of expired
 * 							plates. 
 * 							add isSunset(), isExpired()
 * 							modify actionPerformed() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/26/2007	Do not enable Exp Mo in CORREG if Apprehended
 * 							rename isCORREG() to isRegCorrection()
 * 							modify isRegCorrection()
 * 						    defect 9085 Ver Special Plates
 * K Harrell	07/10/2007  Move reset of No of Months to FeeCalc()
 * 							modify calculateExpMoYr(), validateExpMoYr()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/16/2007	SPRSRV: Validate County. Corrected NFE 
 * 							if no year specified.
 * 							modify confirmCntyRes(), validateInvItmYr()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/18/2007	Do not throw error on 7 digits if PLP if
 * 							SPUNAC
 * 							modify validatePlateNo()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/23/2007	Use new fields in SpecialPlatesRegisData for
 * 							original RegExpMo/Yr.
 * 							modify setExpMoYrToDisplay()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/25/2007	Do not replace zeros in Plate Owner OfcCd
 * 							Add padding to Non-USA ZipCode prior to split
 * 							modify setHQOnlyFieldsToDisplay(),
 * 							  setDataToDataObject()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/31/2007	Do not execute System.out in production
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/03/2007	Do not trim mfgPltNo when set to field
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/06/2007	add setRequestFocus(false) to initialize
 * 							modify initialize()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/06/2007	If Err 1010 && (SPREV ||SPRNR) on inventory
 * 							validation, throw Err Msg 1004.  There is no 
 * 							call to VI to throw.  Else do nothing. 
 * 							modify validatePlateNo()
 * 							defect 9206 Ver Special Plates
 * K Harrell	08/07/2007	Validate Owner Address if SPAPPR
 * 							modify validateOwnrAddr()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/08/2007	SPAPPR/HQ/PLPDLRxx. No Exp Mo/Yr on 
 * 							Receipt, DB
 * 							modify isSPSRV()
 * 							defect 9242 Ver Special Plates
 * K Harrell	08/10/2007	Reset/Disable Add'l Set and Request Renewal
 * 							Notice when SPREV &&(Unacceptable or Reserved)
 * 							add csPriorRequestType() 
 * 							add resetOnSPREVChangeRequestType()  
 *            	      		delete resetMfgStatusCdOnRevise() 
 * 							modify actionPerformed(), 
 * 							 setAddlSetToDisplay(),
 * 							 setRequestRenewalNoticeToDisplay()  
 * 							defect 9235 Ver Special Plates
 * K Harrell	08/13/2007	Add ISA checkbox if SPREV && Assigned.
 * 							add cbInitPass, cbOrigISA,csOrigRegPltCd,
 * 							  csOrigRequestType,csOrigMfgPltNo
 * 							add getchkISA(), validateMfgPltNo(), 
 * 							  setISAToDisplay(), handleISAChkBox()
 * 							modify actionPerformed(), getJPanel1(), 
 * 							  getRegPltCdFromDropDown(),
 * 							  setDataToDataObject(),
 * 							  setPlateDataToDisplay(),
 * 							  validateExpMoYr(),
 * 							  validateFields()
 * 							defect 9248 Ver Special Plates
 * K Harrell	08/15/2007	On SPRNW, Exp Mo/Yr should be blank
 * 							modify calculateExpMoYr(),
 * 							  setExpMoYrToDisplay(),
 * 							  validateExpMoYr() 
 * 							defect 9247 Ver Special Plates 	    
 * Ray Rowehl	08/14/2007	Now allow 1004 to go through to server side.
 * 							modify validatePlateNo()
 * 							defect 9252 Ver Special Plates
 * K Harrell	08/15/2007	Rolled back of earlier 9206, 9252
 * 							modify validatePlateNo() 
 * 							defect 9206, 9252 Ver Special Plates
 * K Harrell	08/20/2007	Disable Exp Mo/Yr on CORREG
 * 	   						delete isRegCorrection()
 * 							modify setAddlSetToDisplay(), 
 * 							  setExpMoYrToDisplay(),
 * 							  setInvItmYrToDisplay(),
 * 							  setOwnrInfoToDisplay()
 * 							defect 9261 Ver Special Plates
 * K Harrell	08/28/2007	Changes per Walkthru
 * 							Use constants for mnemonics. Cleanup JavaDoc.
 * 							releaseVirtualInventoryIfHeld() from public to 
 * 							private. 
 * 							modify releaseVirtualInventoryIfHeld()
 * 							defect 9248 Ver Special Plates
 * K Harrell	09/10/2007	Only release Inventory if SPAPPL type 
 * 							transaction or REPL
 * 						  	modify releaseVirtualInventoryIfHeld()
 * 							defect 9285 Ver Special Plates
 * K Harrell	09/18/2007	Removed use of trim() for MfgPltNo (again).
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/20/2007	Do not allow special characters on SPREV 
 * 							for ROP 
 * 							modify validateMfgPltNo() 
 * 							defect 9384 Ver Special Plates 2 
 * K Harrell	10/20/2007	Do not validate MfgPltNo when 
 * 							UserPltNoIndi = 0
 * 							modify validateMfgPltNo()
 * 							defect 9385 Ver Special Plates 2
 * K Harrell	10/23/2007	Release VI if SPAPPI
 * 							modify releaseVirtualInventoryIfHeld()
 * 							defect 9386 Ver Special Plates 2
 * K Harrell	11/05/2007	Do not enable ISA checkbox for PLPDPPBP
 * 							Modify handling of setchrgSpclPltFee
 * 							modify setISAToDisplay(), setDataToData(),
 * 							  setChrgSpclPltFeeToDisplay()
 * 							defect 9389 Ver Special Plates 2
 * K Harrell	11/12/2007	Correct Prior Exp Yr for Annual Plates 
 * 							modify calculateExpMoYr()
 * 							defect 9434 Ver Special Plates 2
 * K Harrell	11/19/2007	Do not set nor save the "reset" indicator 
 * 							for	charge fees if Special Plates Application
 * 							modify setChrgSpclPltFeeToDisplay(),
 * 							 setDataToDataObject() 
 * 							defect 9450 Ver Special Plates 2
 * K Harrell	11/26/2007	Temp work prior to full implementation
 * 							for using copy of Original data in 
 * 							setDataToDataObject()
 * 							add caOrigSpclPltRegisData 
 * 							add restoreOrigPltMfgStatus()
 * 							modify setData(), setDataToDataObject()
 * 							defect 9462 Ver Special Plates 2
 * K Harrell	01/16/2008	Remove requirement for "P" as first char
 * 							for PLPDLRGDN.
 * 							delete DEALER_NO_PREFIX  
 * 							modify setDealerNoToDisplay(), 
 * 							   validateDealerLicenseNo()
 * 							defect 9499 Ver 3 Amigos Prep
 * J Rue		03/25/2008	Change DlrGDN length from 6 to 10
 * 							modify gettxtDealerNo()
 * 							defetc 9585 Ver 3_AMIGOS_PH_B  
 * 							defect 9462 Ver Special Plates 2
 * K Harrell	03/11/2008	Do not allow Request Renewal Notice through
 * 							Revise Event if linked. 
 * 							add ERRMSG_LINKED_PLT 
 * 							modify validateRequestRenewalNotice()
 * 							defect 9484 Ver Defect_POS_A
 * J Rhe		06/17/2008	Replace reference from 6 digit DlrGDN to 10
 * 							digit DlrGDN 
 * 							modify setDealerNoToDisplay()
 * 							defect 9557 Ver Defect_POS_A
 * B Hargrove	07/11/2008	Add method for special enable\disable rules
 * 							for Vendor Plates (NOTE: for now, unless HQ,
 * 							we will disable fields for both Special 
 * 							plates and Vendor plates).
 * 							add setVendorPltToDisplay() 
 * 							modify actionPerformed(), setData()
 * 							defect 9689 Ver Defect MyPlates_POS
 * K Harrell	01/07/2009 Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 * 							OrigRegExpMo/Yr methods to OrigPltExpMo/Yr 
 * 							 methods.
 *        					modify calculateExpMoYr(), 
 * 							  resetInvItmYrOnPltChng(),
 * 							  setDataToDataObject(),
 * 							  setExpMoYrToDisplay(),
 * 							  setInvItmYrToDisplay(),
 * 							  setupMfgRequestForNewPlateDesired(), 
 * 							  validateExpMoYr() 
 *        					defect 9864 Ver Defect_POS_D
 * K Harrell	07/01/2009	Implement new OwnerData.  Additional Cleanup.
 * 							modify setData(), setDataToDataObject(),
 * 							  setOwnrInfoToDisplay()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/19/2009	Implement new NameAddressComponent 
 * 							add caNameAddrComp 
 * 							modify initialize(), itemStateChanged(), 
 * 							 setDataToDataObject(), 
 * 							 setOwnrInfoToDisplay()
 * 							defect 10127 Ver Defect_POS_F 
 * K Harrell	07/22/2009	OwnerId only enabled for HQ 
 * 							modify setOwnerInfoToDisplay() 
 * 							defect 10130 Ver Defect_POS_F 
 * K Harrell	09/02/2009	ZpcdP4 numeric only
 * 							modify gettxtOwnerZpcdP4()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	12/28/2009	Use Vendor Plt designation when assessing 
 * 							 validity of PltExpYr.
 * 							modify validateExpMoYr()
 * 							defect 10295 Ver Defect_POS_H
 * K Harrell	12/30/2009  Implement isROPBaseRegPltCd()
 * 							modify populateRequestType()
 * 							defect 10293 Ver Defect_POS_H
 * K Harrell	02/16/2010	Enlarge EMail Address field via Visual Comp 
 * 							defect 10372 Ver POS_640
 * K Harrell	02/16/2010	Remove reference to SpecialPlatesRegisData
 * 							SpclRegStkrNo
 * 							delete ivjstcLblSpclRegstkrNo, 
 * 							ivjtxtSpclRegStkrNo, get methods 
 * 							modify setDataToDataObject(), 
 * 							 setHQOnlyFieldsToDisplay(), getPanel1()   
 * 							defect 10366 Ver POS_640  
 * K Harrell	04/09/2010	No longer validate UserPltNoIndi against 
 * 							max of 6 characters for SPRSRV. 
 * 							modify validatePlateNo() 
 * 							defect 10430 
 * K Harrell	04/12/2010	Add new fields, checkbox, combo box to screen
 * 							add ivjstcLblAuctnPrice,ivjstcLblFINDocNo, 
 * 							 ivjstcLblResrvDate, ivjstcLblTerm, 
 * 							 ivjstcLblVendorTransDate, ivjtxtAuctnPrice, 
 * 							 ivjtxtFINDocNo, ivjtxtResrvDate,ivjtxtTerm,
 * 							 ivjtxtVndrTransDate,ivjchkMktngAllowd, 
 * 							 get methods.  
 * 							add svResrvReasnDesc, buildResrvReasnDescVector(), 
 * 							  populateResrvReasn(), setAuctnPltInfoToDisplay()
 * 							  setTermToDisplay(), validateResrvReasnDesc()
 * 							modify getJPanel1(),getRTSDialogBoxContentPane(),
 * 							 setHQOnlyFieldsToDisplay(), setPlateDataToDisplay(), 
 * 							 setDataToDataObject() 
 * 							defect 10441 Ver POS_640 
 * K Harrell	04/14/2010	Reserve Date should be disabled. 
 * 							modify setHQOnlyFieldsToDisplay()
 * 							defect 10441 Ver POS_640 
 * K Harrell	05/04/2010	Make combo box for Reserve Reason Cd longer
 * 							modify getcomboResrvReasnDesc()
 * 							defect 10441 Ver POS_640 
 * K Harrell	06/15/2010	add checkbox for ElectionPndngIndi 
 * 							add ivjchkElectionPndng, get method
 * 							add setElectionPndngToDisplay()
 * 							modify getRTSDialogBoxContentPane(),
 * 							 setPlateDataToDisplay()
 * 							defect 10507 Ver 6.5.0 
 * K Harrell	07/11/2010	Call setElectionPndngToDisplay() if PlateType 
 * 							modified. (HQ only) Modify resetComboColor()
 * 							to include getcomboResrvReasnDesc(). 
 * 							modify actionPerformed(), resetComboColor(), 
 * 							 setElectionPndngToDisplay() 
 * 							defect 10507 Ver 6.5.0  
 * K Harrell	07/16/2010	OwnerAddress is no longer optional for 
 * 							Entitled Plates 
 * 							modify validateOwnrAddr() 
 * 							defect 10507 Ver 6.5.0 
 * K Harrell	07/26/2010	More changes for ElectionPndngIndi.
 * 							modify calculateExpMoYr(), 
 * 							  setExpMoYrToDisplay()
 * 							defect 10507 Ver 6.5.0 
 * K Harrell	09/14/2010	Implement PlateSymbolCache.ISASYMBOL
 * 							modify validateMfgPltNo()
 * 							defect 10571 Ver 6.6.0 
 * K Harrell	09/21/2010	Save Data on Cancel / Enter for later display.
 * 							add setSavedDataToDisplay() 
 * 							modify actionPerformed(), setData(), 
 * 							 setDataToDataObject()  
 * 							defect 10592 Ver 6.6.0 
 * K Harrell	10/22/2010	Save invalid phone no 
 * 							modify setDataToDataObject()
 * 							defect 10592 Ver 6.6.0 
 * K Harrell	12/07/2010	deleted duplicate setTermToDisplay() 
 * 							6.7.0 Merge Issue 
 * K Harrell	12/07/2010	deleted duplicate setTermToDisplay() 
 * 							6.7.1 Merge Issue 
 * R Pilon		06/10/2011	Implement Special Plate Inquiry
 * 							modify actionPerformed(), 
 * 							  setChrgSpclPltFeeToDisplay, setData()
 * 							defect 10820 Ver 6.8.0
 * K Harrell	11/14/2011	Implement VTR 275 
 * 							modify actionPerformed() 
 * 							defect 11052 Ver 6.9.0 
 * K Harrell	12/08/2011	Modify for VTR 275 No Record Found
 * 							modify setPlateDataToDisplay()
 * 							defect 11052 Ver 6.9.0 
 * K Harrell	12/12/2011	Set CTData NoMFRecs for HQ 
 * 							modify actionPerformed()
 * 							defect 11169 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * Class for screen SPL002, Special Plates Information
 * 
 * @version	6.9.0 			12/12/2011
 * @author  Kathy Harrell 
 * <br>Creation Date:		03/11/2003	09:30:09
 */
public class FrmSpecialPlateInformationSPL002
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkAddlSet = null;
	private JCheckBox ivjchkChrgSpclPltFee = null;
	private JCheckBox ivjchkISA = null;
	private JCheckBox ivjchkNewPlatesDesired = null;
	private JCheckBox ivjchkRequestRenewalNotice = null;
	private JCheckBox ivjchkUSA = null;
	private JComboBox ivjcomboOrganizationName = null;
	private JComboBox ivjcomboPlateType = null;
	private JComboBox ivjcomboRequestType = null;
	private JComboBox ivjcomboResrvReasnDesc = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel3 = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcLblAddress = null;
	private JLabel ivjstcLblApplDate = null;
	private JLabel ivjstcLblCounty = null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblDealerNo = null;
	private JLabel ivjstcLblEMail = null;
	private JLabel ivjstcLblExpires = null;
	private JLabel ivjstcLblMfgDate = null;
	private JLabel ivjstcLblMfgPlateNo = null;
	private JLabel ivjstcLblName = null;
	private JLabel ivjstcLblNew = null;
	private JLabel ivjstcLblOrganization = null;
	private JLabel ivjstcLblOwnerId = null;
	private JLabel ivjstcLblPhoneNo = null;
	private JLabel ivjstcLblPlateAge = null;
	private JLabel ivjstcLblPlateNo = null;
	private JLabel ivjstcLblPlateType = null;
	private JLabel ivjstcLblPltOwnrDist = null;
	private JLabel ivjstcLblPltOwnrOfcCd = null;
	private JLabel ivjstcLblRequestType = null;
	private JLabel ivjstcLblSlash = null;
	private JLabel ivjstcLblSpclRemks = null;
	private JLabel ivjstcLblStatus = null;
	private RTSDateField ivjtxtApplDate = null;
	private RTSInputField ivjtxtDealerNo = null;
	private RTSInputField ivjtxtEMail = null;
	private RTSInputField ivjtxtExpMo = null;
	private RTSInputField ivjtxtExpYr = null;
	private RTSInputField ivjtxtInvItmYr = null;
	private RTSDateField ivjtxtMfgDate = null;
	private RTSInputField ivjtxtMfgPlateNo = null;
	private RTSInputField ivjtxtOwnerCity = null;
	private RTSInputField ivjtxtOwnerCntry = null;
	private RTSInputField ivjtxtOwnerCntryZpcd = null;
	private RTSInputField ivjtxtOwnerId = null;
	private RTSInputField ivjtxtOwnerName1 = null;
	private RTSInputField ivjtxtOwnerName2 = null;
	private RTSInputField ivjtxtOwnerState = null;
	private RTSInputField ivjtxtOwnerStreet1 = null;
	private RTSInputField ivjtxtOwnerStreet2 = null;
	private RTSInputField ivjtxtOwnerZpcdP4 = null;
	private RTSInputField ivjtxtOwnrZpcd = null;
	private RTSPhoneField ivjtxtPhoneNo = null;
	private RTSInputField ivjtxtPlateAge = null;
	private RTSInputField ivjtxtPlateNo = null;
	private RTSInputField ivjtxtPltOwnrDist = null;
	private RTSInputField ivjtxtPltOwnrOfcCd = null;
	private RTSInputField ivjtxtResComptCntyNo = null;
	private RTSInputField ivjtxtSpclRemarks = null;
	private JPanel wsdJPanel2 = null;

	// defect 10441
	private JLabel ivjstcLblAuctnPrice = null;
	private JLabel ivjstcLblFINDocNo = null;
	private JLabel ivjstcLblResrvDate = null;
	private JLabel ivjstcLblTerm = null;
	private JLabel ivjstcLblVndrTransDate = null;

	private RTSInputField ivjtxtAuctnPrice = null;
	private RTSInputField ivjtxtFINDocNo = null;
	private RTSDateField ivjtxtResrvDate = null;
	private RTSInputField ivjtxtTerm = null;
	private RTSDateField ivjtxtVendorTransDate = null;
	private JCheckBox ivjchkMktngAllowd = null;
	// end defect 10441 

	// Objects
	private CompleteTransactionData caCTData;
	private MFVehicleData caMFVehicleData;
	private NameAddressComponent caNameAddrComp = null;
	private SpecialPlatesRegisData caOrigSpclPltRegisData;
	private AddressData caOwnerAddressData;
	private OwnerData caOwnerData;
	private PlateTypeData caPltTypeData = null;
	private SpecialPlatesRegisData caSpclPltRegisData;
	private RTSDate caToday = new RTSDate();
	private VehicleInquiryData caVehInqData;

	// defect 10366 
	// private JLabel ivjstcLblSpclRegstkrNo = null;
	// private RTSInputField ivjtxtSpclRegStkrNo = null;
	// end defect 10366 

	// boolean 
	private boolean cbInitPass = true;
	private boolean cbLabelSet = false;
	private boolean cbOrigISA = false;
	private boolean cbSetDataFinished = false;

	// int
	private int ciOrigMfgDate = 0;
	private int ciOrigReqSelection = 0;
	private int ciPrevConfirmedCntyNo = 0;

	// String 
	private String csMfgPltNo = null;
	private String csOrgNo = null;
	private String csOrigMfgPltNo = null;
	private String csOrigMfgStatusCd = null;
	private String csOrigRegPltCd = null;
	private String csOrigRequestType = null;
	private String csOrigTransCd = null;
	private String csPriorRequestType = null;
	private String csRegPltCd = null;
	private String csRequestType = null;
	private String csTransCd = null;

	// Vector
	private Vector cvOrgNo = new Vector();
	private Vector cvRequestType = new Vector();

	// Constants 
	private final static String ERRMSG_DATA_MISS =
		"Data missing for IsNextVCREG029";
	private final static String ERRMSG_ERROR = "ERROR";
	private final static String ERRMSG_INVALID_PLT_STATUS =
		"  THE PLATE STATUS IS INVALID.";
	// defect 9484 
	private final static String ERRMSG_LINKED_PLT =
		" THE RECORD IS LINKED; USE THE ADDRESS CHANGE/PRINT RENEWAL EVENT.";
	private final static String ERRMSG_REG_EXP =
		" THE REGISTRATION HAS EXPIRED.";
	private final static String ERRMSG_REG_IN_FUTURE =
		" THE REGISTRATION IS IN THE FUTURE.";
	//private final static int ERRNO_INCOMPL_ADDR = 186;
	private final static int ERRORNO_NO_RNR = 738;
	private final static int ERRORNO_PLATENO_TOO_LONG = 2014;
	private final static int ERRORNO_PLT_NOT_AVAIL_FOR_EVENT = 2018;
	private final static int ERRORNO_RESET_ON_EXPIRED = 2019;
	private final static int ERRORNO_SUNSET = 2016;
	private final static int MAX_CNTY_NO = 254;

	private final static String SPCL_PLT_INFO_TITLE =
		"Special Plate Information       SPL002";

	// Static Vector
	private static Vector svPlateType = new Vector();
	private static Vector svStatus = new Vector();

	// defect 10441 
	// Build Vector of Reserve Reason Codes
	private static Vector svResrvReasnDesc = new Vector();
	// end defect 10441

	// defect 10507
	private JCheckBox ivjchkElectionPndng = null;
	// end defect 10507 

	//	Vector for Status/Request Types 
	static {
		svStatus.addElement(SpecialPlatesConstant.UNACCEPTABLE);
		svStatus.addElement(SpecialPlatesConstant.ASSIGNED);
		svStatus.addElement(SpecialPlatesConstant.MANUFACTURE);
		svStatus.addElement(SpecialPlatesConstant.FROM_RESERVE);
	}
	// Build Vector of All Special Plate Types
	static {
		buildPlateTypeVector();
	}
	static {
		buildResrvReasnDescVector();
	}

	/**
	 * Creates the Plate Type Vector   
	 */
	private static void buildPlateTypeVector()
	{
		Vector lvSpclPltTypeCd = null;
		int liToday = new RTSDate().getYYYYMMDDDate();

		lvSpclPltTypeCd =
			PlateTypeCache.getAllSpecialPlateTypes(liToday);

		for (int i = 0; i < lvSpclPltTypeCd.size(); i++)
		{
			PlateTypeData laPltTypeData =
				(PlateTypeData) lvSpclPltTypeCd.get(i);
			String lsRegPltCd = laPltTypeData.getRegPltCd();
			String lsRegPltCdDesc =
				UtilityMethods.addPaddingRight(
					laPltTypeData.getRegPltCdDesc(),
					75,
					" ");
			svPlateType.add(lsRegPltCdDesc + lsRegPltCd);
		}
		UtilityMethods.sort(svPlateType);
	}

	/**
	 * Creates the Reserve Reason Description Vector   
	 */
	private static void buildResrvReasnDescVector()
	{
		Vector lvResrvReasnDescVector = null;

		lvResrvReasnDescVector =
			IndicatorDescriptionsCache.getIndiDescs(
				SpecialPlatesConstant.RESRVREASN_INDINAME);

		for (int i = 0; i < lvResrvReasnDescVector.size(); i++)
		{
			IndicatorDescriptionsData laData =
				(IndicatorDescriptionsData) lvResrvReasnDescVector.get(
					i);
			String lsIndiFieldValue = laData.getIndiFieldValue();
			String lsIndiDesc =
				UtilityMethods.addPaddingRight(
					laData.getIndiDesc(),
					75,
					" ");
			svResrvReasnDesc.add(lsIndiDesc + lsIndiFieldValue);
		}
		UtilityMethods.sort(svResrvReasnDesc);
	}

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmSpecialPlateInformationSPL002 aFrmSpecialPlateInformationSPC002;
			aFrmSpecialPlateInformationSPC002 =
				new FrmSpecialPlateInformationSPL002();
			aFrmSpecialPlateInformationSPC002.setModal(true);
			aFrmSpecialPlateInformationSPC002
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			aFrmSpecialPlateInformationSPC002.show();
			java.awt.Insets insets =
				aFrmSpecialPlateInformationSPC002.getInsets();
			aFrmSpecialPlateInformationSPC002.setSize(
				aFrmSpecialPlateInformationSPC002.getWidth()
					+ insets.left
					+ insets.right,
				aFrmSpecialPlateInformationSPC002.getHeight()
					+ insets.top
					+ insets.bottom);
			aFrmSpecialPlateInformationSPC002.setVisible(true);
		}
		catch (Throwable exception)
		{
			System.err.println(
				"Exception occurred in main() of RTSDialogBox");
			exception.printStackTrace(System.out);
		}
	}

	/**
	 * FrmSpecialPlateInformationSPL002 constructor comment.
	 */
	public FrmSpecialPlateInformationSPL002()
	{
		super();
		initialize();
	}

	/**
	 * FrmSpecialPlateInformationSPL002 constructor comment.
	 */
	public FrmSpecialPlateInformationSPL002(JDialog parent)
	{
		super(parent);
		initialize();
	}

	/**
	 * FrmSpecialPlateInformationSPL002 constructor comment.
	 */
	public FrmSpecialPlateInformationSPL002(JFrame parent)
	{
		super(parent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAe 
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
			
			restoreComboColor();

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 10820
				// special plates inquiry - temporary trans code 
				if (csTransCd.equals(TransCdConstant.SPINQ))
				{
					boolean lbHQ = SystemProperty.isHQ();
					int liPrintOption = caVehInqData.getPrintOptions();

					// defect 11052 
					if (!lbHQ && liPrintOption != VehicleInquiryData.VIEW_ONLY)
						//	&& (liPrintOption
						//		== VehicleInquiryData.RECEIPT
						//		|| liPrintOption
						//			== VehicleInquiryData
						//			.CHARGE_FEE_VIEW_AND_PRINT))
					{
						// end defect 11052 
						getController().processData(
							VCSpecialPlateInformationSPL002.INQ007,
							caVehInqData);
					}
					else
					{
						CompleteTransactionData laCompTransData =
							new CompleteTransactionData();
						laCompTransData.setOrgVehicleInfo(
							caVehInqData.getMfVehicleData());
						laCompTransData.setVehicleInfo(caMFVehicleData);
						
						// defect 11052 
						laCompTransData.setTransCode(
							UtilityMethods.getVehInqTransCd(liPrintOption));
						laCompTransData.setPrintOptions(
							liPrintOption);
						// end defect 11052 
						
						// defect 11169 
						laCompTransData.setNoMFRecs(caVehInqData.getNoMFRecs()); 
						// end defect 11169 
						
						getController().processData(
							AbstractViewController.ENTER,
							laCompTransData);
					}
				}
				else if (validateFields())
				// end defect 10820
				{
					// defect 9248 
					// Confirm No Manufacture on ISA Change 

					// Return to frame if ResComptCntyNo is not valid  
					// or if HQ responds "N" to Confirm for AutoTrans 
					if ((gettxtResComptCntyNo().isEnabled()
						&& !confirmCntyRes())
						|| !confirmForISAChange()
						|| !confirmForAutoTrans())
					{
						return;
					}
					// end defect 9248 

					// Set Data to SpecialPlateRegisData 
					setDataToDataObject();

					// Special Plates Events 
					if (UtilityMethods.isSpecialPlates(csTransCd))
					{
						if (isSPAPPL() || isSPRNW() || isSPREV())
						{
							// Create Complete Transaction Data Object.
							caCTData =
								SpecialPlatesClientUtilityMethods
									.calcFees(
									caMFVehicleData,
									csTransCd);

							// If HQ && !SPAPPI, AutoEnd Trans
							if (SystemProperty.isHQ()
								&& !csTransCd.equals(
									TransCdConstant.SPAPPI))
							{
								getController().processData(
									VCSpecialPlateInformationSPL002
										.ADD_TRANS,
									caCTData);
							}
							// For HQ, prompts for Inventory Only and
							//  then AutoEnd 
							// Else, Inventory prompt if necessary and 
							//   then PMT004  
							else
							{
								getController().processData(
									VCSpecialPlateInformationSPL002
										.ENTER_FEES,
									caCTData);
							}
						}
						// Delete, Reserve, Unacceptable  (all HQ) 
						// AutoEnd Transaction 
						else
						{
							// Delete
							// No Hold Status in VI 
							if (isSPDEL())
							{
								getController().processData(
									VCSpecialPlateInformationSPL002
										.ADD_TRANS,
									caVehInqData);
							}
							// Reserve || Unacceptable
							// Make request to put VI on Hold
							else
							{
								caSpclPltRegisData.setVIAllocData(
									SpecialPlatesClientUtilityMethods
										.setupInvAlloc(
										caSpclPltRegisData,
										new String()));

								// Default to Personalized
								int liFcnId =
									InventoryConstant
										.INV_VI_VALIDATE_PER_PLT;

								if (caPltTypeData.getUserPltNoIndi()
									== 0)
								{
									liFcnId =
										InventoryConstant
											.INV_VI_UPDATE_INV_STATUS_CD_RECOVER;

								}
								getController().processData(
									liFcnId,
									caVehInqData);
							}
						}
					}
					// Not Special Plate Transaction 
					// From KEY002, REG003, REG033, REG011
					// Goes to "Previous"  
					else
					{
						getController().processData(
							VCSpecialPlateInformationSPL002.ENTER,
							caVehInqData);
					}
				}
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				// defect 10592 
				if (isSPAPPL())
				{
					setDataToDataObject();
				}
				// end defect 10592 

				// Release Inventory if Populated 
				releaseVirtualInventoryIfHeld();

				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// Help 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
						"Information");
				leRTSEx.displayError(this);
			}
			// Plate Type 
			else if (aaAE.getSource() == getcomboPlateType())
			{
				getRegPltCdFromDropDown(
					getcomboPlateType().getSelectedIndex());
				populateOrganizationName();
				populateRequestType();
				// defect 10507 
				setElectionPndngToDisplay();
				// end defect 10507  
				getcomboPlateType().requestFocus();

			}
			// Organization 
			else if (aaAE.getSource() == getcomboOrganizationName())
			{
				getOrgNoFromDropDown(
					getcomboOrganizationName().getSelectedIndex());
			}
			// Request Type 
			else if (aaAE.getSource() == getcomboRequestType())
			{
				// defect 9235
				csPriorRequestType = csRequestType;
				csRequestType =
					(String) getcomboRequestType().getSelectedItem();

				try
				{
					resetOnSPREVChangeRequestType();
				}
				// end defect 9235
				catch (RTSException aeRTSEx)
				{
					// If select manufacture and plate expired, 
					// request type will be reset to earlier selection 
					csRequestType = csPriorRequestType;
					aeRTSEx.addException(
						aeRTSEx,
						getcomboRequestType());
					aeRTSEx.displayError(this);
					getcomboRequestType().setSelectedIndex(
						ciOrigReqSelection);
				}
			}
			// defect 9248 
			// Handle set/reset of ISA checkbox
			else if (aaAE.getSource() == ivjchkISA)
			{
				handleISAChkBox();
			}
			// end defect 9248 

			// Request Renewal Notice 
			else if (aaAE.getSource() == ivjchkRequestRenewalNotice)
			{
				if (!getchkRequestRenewalNotice().isSelected())
				{
					csTransCd = csOrigTransCd;
					getController().setTransCode(csOrigTransCd);
				}
				else
				{
					RTSException leRTSEx = new RTSException();

					// Valid ExpMo/Yr prior to Renewal Notice Validation
					validateExpMoYr(leRTSEx);

					if (leRTSEx.isValidationError())
					{
						// Reset Renewal Selection if Exp Mo/Yr not valid
						getchkRequestRenewalNotice().setSelected(false);
						throw leRTSEx;
					}
					else
					{
						// If 
						if (validateRequestRenewalNotice())
						{
							csTransCd = TransCdConstant.SPRNR;
							getController().setTransCode(
								TransCdConstant.SPRNR);
						}

					}
				}

			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
			aeRTSEx.getFirstComponent().requestFocus();
		}
		finally
		{
			doneWorking();
		}

	}

	/**
	 * Calculate Expiration Mo/Yr for SPAPPL & SPRNW
	 * 
	 * @return int  
	 */
	private int calculateExpMoYr()
	{
		int liNoMonths = 0;

		// SPAPPO - 12 Months 
		// No Calculation; No Exp Mo/Yr change 
		if (csTransCd.equals(TransCdConstant.SPAPPO))
		{
			caSpclPltRegisData.setNoMonthsToCharge(12);
		}
		// defect 9247 
		// do not calculate if PLPDLRxx && SPRNW 
		else if (!(isDlrPlt() && (isSPAPPL() || isSPRNW())))
		{
			// defect 9864 
			// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
			int liExpMo = caSpclPltRegisData.getPltExpMo();
			int liExpYr = caSpclPltRegisData.getPltExpYr();
			int liPriorExpMo = caSpclPltRegisData.getPltExpMo();
			int liPriorExpYr = caSpclPltRegisData.getPltExpYr();
			// end defect 9864 
			boolean lbFxdMo = false;

			// Fixed Exp Mo 
			SpecialPlateFixedExpirationMonthData laSpclPltFxdExpMoData =
				SpecialPlateFixedExpirationMonthCache.getRegPltCd(
					csRegPltCd);

			if (laSpclPltFxdExpMoData != null)
			{
				liExpMo = laSpclPltFxdExpMoData.getFxdExpMo();

				if (isSPAPPL())
				{
					liPriorExpMo = liExpMo;
				}
				lbFxdMo = true;
			}

			// Only true in SP Application
			if (caSpclPltRegisData.getInvItmYr() != 0 && isSPAPPL())
			{
				liExpYr = caSpclPltRegisData.getInvItmYr();
				// defect 9434 
				// Correct setting of PriorExpYr for Fixed
				// liPriorExpYr = caToday.getYear(); 
				liPriorExpYr = liExpYr - 1;
				// end defect 9434 
			}

			// Will be expired if SPAPPL && !PlateOwnerShip Change
			if (CommonValidations
				.isRegistrationExpired(liPriorExpMo, liPriorExpYr))
			{
				if (caToday.getMonth() == 1)
				{
					liPriorExpMo = 12;
					liPriorExpYr = caToday.getYear() - 1;
					liExpYr = caToday.getYear();
				}
				else
				{
					liPriorExpMo = caToday.getMonth() - 1;
					liPriorExpYr = caToday.getYear();
					liExpYr = caToday.getYear() + 1;
					// defect 9434 
					if (lbFxdMo && caToday.getMonth() <= liExpMo)
					{
						liExpYr = liExpYr - 1;
					}
					// end defect 9434 
				}
				if (!lbFxdMo)
				{
					liExpMo = liPriorExpMo;
				}
				while (CommonValidations
					.isRegistrationExpired(liExpMo, liExpYr))
				{
					liExpYr = liExpYr + 1;
				}
			}
			if (SystemProperty.getProdStatus()
				!= SystemProperty.APP_PROD_STATUS)
			{
				System.out.println("" + liExpMo + " " + liExpYr);
			}

			int liStartMonths = liPriorExpMo + (liPriorExpYr * 12);

			liNoMonths = (liExpMo + (liExpYr) * 12) - liStartMonths;

			// defect 10505 
			if ((!isSPAPPL() || caPltTypeData.isMultiYrOfclPlt())
				&& liNoMonths <= 3)
			{
				// end defect 10505 
				liNoMonths = liNoMonths + 12;
				liExpYr = liExpYr + 1;
			}

			// Display in Development Only 
			if (SystemProperty.getProdStatus()
				!= SystemProperty.APP_PROD_STATUS)
			{
				System.out.println("" + liExpMo + " " + liExpYr);
				System.out.println("Months to Charge:" + liNoMonths);
			}

			caSpclPltRegisData.setNoMonthsToCharge(liNoMonths);
			// defect 9864 
			// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr 
			caSpclPltRegisData.setPltExpMo(liExpMo);
			caSpclPltRegisData.setPltExpYr(liExpYr);
			// end defect 9064 
		}
		// end defect 9247 
		return caSpclPltRegisData.getNoMonthsToCharge();
	}

	/**
	 * Confirm ResComptCntyNo if enabled and  
	 *   Not ((Unacceptable or Reserved) and empty)
	 *   
	 * @return boolean
	 */
	private boolean confirmCntyRes()
	{
		boolean lbReturn = true;

		if (gettxtResComptCntyNo().isEnabled()
			&& (!(gettxtResComptCntyNo().getText().trim().length() == 0
				&& (isSPRSRV() || isSPUNAC()))))
		{
			int liCurrCnty =
				Integer.parseInt(
					gettxtResComptCntyNo().getText().trim());
			if (liCurrCnty != ciPrevConfirmedCntyNo)
			{
				OfficeIdsData laOfcData =
					OfficeIdsCache.getOfcId(liCurrCnty);
				FrmCountyConfirmCTL002 laCntyCnfrm =
					new FrmCountyConfirmCTL002(
						getController().getMediator().getDesktop(),
						new Integer(liCurrCnty).toString(),
						laOfcData.getOfcName());
				if (laCntyCnfrm.displayWindow()
					== FrmCountyConfirmCTL002.NO)
				{
					lbReturn = false;
				}
				else
				{
					ciPrevConfirmedCntyNo = liCurrCnty;
				}

			}
		}
		return lbReturn;
	}

	/**
	 * Confirm that user wishes to complete transaction for Special Plates
	 * 
	 * Auto Trans  if (HQ  && Special Plates Event and !SPAPPI)  
	 * 
	 * @return booelan 
	 */
	private boolean confirmForAutoTrans()
	{
		boolean lbReturn = true;

		// If HQ && Special Plates && !SPAPPI (Issue from Inventory)		
		if (SystemProperty.isHQ()
			&& UtilityMethods.isSpecialPlates(csTransCd)
			&& !csTransCd.equals(TransCdConstant.SPAPPI))
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.CTL001,
					CommonConstant.TXT_COMPLETE_TRANS_QUESTION,
					ScreenConstant.CTL001_FRM_TITLE);

			int liResponse = leRTSEx.displayError(this);

			if (liResponse == RTSException.NO)
			{
				lbReturn = false;
			}
		}
		return lbReturn;
	}

	/**
	 * Confirm for ISA change
	 * 
	 * @return boolean 
	 */
	private boolean confirmForISAChange()
	{
		boolean lbReturn = true;

		// Revise - !LP && Change ISA status && !Manufacture 
		if (caPltTypeData != null
			&& isSPREV()
			&& !caPltTypeData.getNeedsProgramCd().equals(
				SpecialPlatesConstant.LP_PLATE)
			&& cbOrigISA != (caSpclPltRegisData.getISAIndi() == 1)
			&& !csRequestType.equals(SpecialPlatesConstant.MANUFACTURE))
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.CTL001,
					"Are you sure you do NOT wish to Manufacture this plate?",
					ScreenConstant.CTL001_FRM_TITLE);

			int liResponse = leRTSEx.displayError(this);

			if (liResponse == RTSException.NO)
			{
				lbReturn = false;
			}
		}
		return lbReturn;
	}

	/**
	* Return the ECH property value.
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
				ivjButtonPanel1.setBounds(265, 467, 216, 36);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setAsDefault(this);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				// user code end 
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the ivjchkAddlSet property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkAddlSet()
	{
		if (ivjchkAddlSet == null)
		{
			try
			{
				ivjchkAddlSet = new JCheckBox();
				ivjchkAddlSet.setSize(105, 22);
				ivjchkAddlSet.setName("chkAdditionalSet");
				ivjchkAddlSet.setMnemonic(java.awt.event.KeyEvent.VK_A);
				ivjchkAddlSet.setText("Additional Set");
				ivjchkAddlSet.setLocation(432, 392);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjchkAddlSet;
	}

	/**
	 * Return ivjchkChrgSpclPltFee 
	 * 
	 * @return JCheckBox
	 */

	private JCheckBox getchkChrgSpclPltFee()
	{
		if (ivjchkChrgSpclPltFee == null)
		{
			try
			{
				ivjchkChrgSpclPltFee = new JCheckBox();
				ivjchkChrgSpclPltFee.setSize(166, 22);
				ivjchkChrgSpclPltFee.setName("chkChrgSpclPltFee");
				ivjchkChrgSpclPltFee.setMnemonic(
					java.awt.event.KeyEvent.VK_G);
				ivjchkChrgSpclPltFee.setText(
					"Charge Special Plate Fee");
				ivjchkChrgSpclPltFee.setLocation(568, 392);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjchkChrgSpclPltFee;
	}

	/**
	 * This method initializes ivjchkElectionPndng
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkElectionPndng()
	{
		if (ivjchkElectionPndng == null)
		{
			ivjchkElectionPndng = new JCheckBox();
			ivjchkElectionPndng.setBounds(568, 367, 166, 21);
			ivjchkElectionPndng.setText("Re-Election Pending");
			ivjchkElectionPndng.setEnabled(false);
			ivjchkElectionPndng.setVisible(false);
		}
		return ivjchkElectionPndng;
	}
	/**
	 * This method initializes ivjchkISA
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkISA()
	{
		if (ivjchkISA == null)
		{
			ivjchkISA = new javax.swing.JCheckBox();
			ivjchkISA.setBounds(234, 131, 49, 21);
			ivjchkISA.setMnemonic(java.awt.event.KeyEvent.VK_I);
			ivjchkISA.setText("ISA");
			ivjchkISA.addActionListener(this);
			ivjchkISA.setEnabled(false);
		}
		return ivjchkISA;
	}

	/**
	 * This method initializes ivjchkMktngAllowd
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkMktngAllowd()
	{
		if (ivjchkMktngAllowd == null)
		{
			ivjchkMktngAllowd = new JCheckBox();
			ivjchkMktngAllowd.setSize(137, 21);
			ivjchkMktngAllowd.setText("Marketing Allowed");
			ivjchkMktngAllowd.setLocation(432, 367);
		}
		return ivjchkMktngAllowd;
	}

	/**
	 * This method initializes ivjchkNewPlatesDesired
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkNewPlatesDesired()
	{
		if (ivjchkNewPlatesDesired == null)
		{
			ivjchkNewPlatesDesired = new JCheckBox();
			ivjchkNewPlatesDesired.setSize(137, 21);
			ivjchkNewPlatesDesired.setText("New Plates Desired");
			ivjchkNewPlatesDesired.setLocation(432, 417);
			ivjchkNewPlatesDesired.setMnemonic(
				java.awt.event.KeyEvent.VK_N);
		}
		return ivjchkNewPlatesDesired;
	}

	/**
	 * This method initializes ivjchkRequestRenewalNotice
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkRequestRenewalNotice()
	{
		if (ivjchkRequestRenewalNotice == null)
		{
			ivjchkRequestRenewalNotice = new JCheckBox();
			ivjchkRequestRenewalNotice.setSize(167, 21);
			ivjchkRequestRenewalNotice.setLocation(568, 417);
			ivjchkRequestRenewalNotice.setText(
				"Request Renewal Notice");
			ivjchkRequestRenewalNotice.setMnemonic(
				java.awt.event.KeyEvent.VK_Q);
			ivjchkRequestRenewalNotice.addActionListener(this);
		}
		return ivjchkRequestRenewalNotice;
	}

	/**
	 * This method initializes ivjchkUSA 
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
				ivjchkUSA.setSize(50, 19);
				ivjchkUSA.setName("chkUSA");
				ivjchkUSA.setMnemonic(java.awt.event.KeyEvent.VK_U);
				ivjchkUSA.setText("USA");
				ivjchkUSA.setLocation(208, 86);
				// user code begin {1} 
				ivjchkUSA.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjchkUSA;
	}

	/**
	 * Return the ivjcomboOrganizationName property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboOrganizationName()
	{
		if (ivjcomboOrganizationName == null)
		{
			try
			{
				ivjcomboOrganizationName = new JComboBox();
				ivjcomboOrganizationName.setName("JComboOrganization");
				ivjcomboOrganizationName.setBounds(146, 82, 222, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboOrganizationName;
	}

	/**
	 * Return the ivjcomboPlateType property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboPlateType()
	{
		if (ivjcomboPlateType == null)
		{
			try
			{
				ivjcomboPlateType = new JComboBox();
				ivjcomboPlateType.setName("JComboPlateType");
				ivjcomboPlateType.setBounds(146, 57, 222, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboPlateType;
	}

	/**
	 * Return the ivjcomboRequestType property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboRequestType()
	{
		if (ivjcomboRequestType == null)
		{
			try
			{
				ivjcomboRequestType = new JComboBox();
				ivjcomboRequestType.setName("ivjcomboRequestType");
				ivjcomboRequestType.setBounds(146, 107, 222, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboRequestType;
	}

	/**
	 * This method initializes ivjcomboResrvReasnDesc
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboResrvReasnDesc()
	{
		if (ivjcomboResrvReasnDesc == null)
		{
			ivjcomboResrvReasnDesc = new JComboBox();
			//ivjcomboResrvReasnDesc.setBounds(228, 358, 109, 20);
			ivjcomboResrvReasnDesc.setBounds(228, 358, 140, 20);
		}
		return ivjcomboResrvReasnDesc;
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
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(null);
				getJPanel1().add(
					getstcLblPlateNo(),
					getstcLblPlateNo().getName());
				getJPanel1().add(
					gettxtPlateNo(),
					gettxtPlateNo().getName());
				getJPanel1().add(
					gettxtPlateAge(),
					gettxtPlateAge().getName());
				getJPanel1().add(
					getstcLblPlateAge(),
					getstcLblPlateAge().getName());
				getJPanel1().add(
					getstcLblPlateType(),
					getstcLblPlateType().getName());
				getJPanel1().add(
					getstcLblOrganization(),
					getstcLblOrganization().getName());
				getJPanel1().add(
					getstcLblStatus(),
					getstcLblStatus().getName());
				getJPanel1().add(
					getstcLblRequestType(),
					getstcLblRequestType().getName());
				getJPanel1().add(
					gettxtMfgPlateNo(),
					gettxtMfgPlateNo().getName());
				getJPanel1().add(
					getstcLblMfgPlateNo(),
					getstcLblMfgPlateNo().getName());
				getJPanel1().add(
					getcomboPlateType(),
					getcomboPlateType().getName());
				getJPanel1().add(
					getcomboOrganizationName(),
					getcomboOrganizationName().getName());
				getJPanel1().add(
					getcomboRequestType(),
					getcomboRequestType().getName());
				getJPanel1().add(
					getstcLblExpires(),
					getstcLblExpires().getName());
				getJPanel1().add(
					gettxtExpMo(),
					gettxtExpMo().getName());
				getJPanel1().add(
					gettxtExpYr(),
					gettxtExpYr().getName());
				getJPanel1().add(
					getstcLblSlash(),
					getstcLblSlash().getName());
				getJPanel1().add(
					getstcLblCounty(),
					getstcLblCounty().getName());
				getJPanel1().add(
					gettxtResComptCntyNo(),
					gettxtResComptCntyNo().getName());
				getJPanel1().add(
					gettxtPltOwnrOfcCd(),
					gettxtPltOwnrOfcCd().getName());
				getJPanel1().add(
					getstcLblPltOwnrDist(),
					getstcLblPltOwnrDist().getName());
				getJPanel1().add(
					gettxtPltOwnrDist(),
					gettxtPltOwnrDist().getName());
				getJPanel1().add(
					getstcLblPltOwnrOfcCd(),
					getstcLblPltOwnrOfcCd().getName());
				// defect 10366
				// getJPanel1().add(
				//	gettxtSpclRegStkrNo(),
				//	gettxtSpclRegStkrNo().getName()); 
				// getJPanel1().add(
				//	getstcLblSpclRegStkrNo(),
				//	getstcLblSpclRegStkrNo().getName());
				// end defect 10366 
				ivjJPanel1.add(
					getstcLblMgfDate(),
					getstcLblMgfDate().getName());
				ivjJPanel1.add(
					getstcLblApplDate(),
					getstcLblApplDate().getName());
				ivjJPanel1.add(
					gettxtMfgDate(),
					gettxtMfgDate().getName());
				ivjJPanel1.add(
					gettxtApplDate(),
					gettxtApplDate().getName());
				ivjJPanel1.add(getstcLblSpclRemarks(), null);
				ivjJPanel1.add(gettxtSpclRemarks(), null);
				ivjJPanel1.add(gettxtInvItmYr(), null);
				ivjJPanel1.add(getstcLblNew(), null);
				// defect 9248 
				// add checkbox for ISA
				ivjJPanel1.add(getchkISA(), null);
				// end defect 9248 
				// defect 10441 
				ivjJPanel1.add(getstcLblAuctnPrice(), null);
				ivjJPanel1.add(getstcLblFINDocNo(), null);
				ivjJPanel1.add(gettxtAuctnPrice(), null);
				ivjJPanel1.add(gettxtFINDocNo(), null);
				ivjJPanel1.add(gettxtVendorTransDate(), null);
				ivjJPanel1.add(gettxtResrvDate(), null);
				ivjJPanel1.add(getstcLblVndrTransDate(), null);
				ivjJPanel1.add(getstcLblResrvDate(), null);
				ivjJPanel1.add(getstcLblTerm(), null);
				ivjJPanel1.add(gettxtTerm(), null);
				ivjJPanel1.add(getcomboResrvReasnDesc(), null);
				ivjJPanel1.setBounds(8, 4, 407, 438);
				// end defect 10441 
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the wsdJPanel2 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel2()
	{
		if (wsdJPanel2 == null)
		{
			try
			{
				wsdJPanel2 = new JPanel();
				wsdJPanel2.setName("JPanel2");
				wsdJPanel2.setLayout(null);
				wsdJPanel2.add(getJPanel1(), getJPanel1().getName());
				wsdJPanel2.setBounds(1, 6, 420, 450);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return wsdJPanel2;
	}

	/**
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
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setLayout(null);
				getJPanel3().add(
					gettxtOwnerId(),
					gettxtOwnerId().getName());
				getJPanel3().add(
					getstcLblOwnerName(),
					getstcLblOwnerName().getName());
				getJPanel3().add(
					gettxtOwnerName1(),
					gettxtOwnerName1().getName());
				getJPanel3().add(
					gettxtOwnerName2(),
					gettxtOwnerName2().getName());
				getJPanel3().add(
					getstcLblOwnerAddress(),
					getstcLblOwnerAddress().getName());
				getJPanel3().add(getchkUSA(), getchkUSA().getName());
				getJPanel3().add(
					gettxtOwnerStreet1(),
					gettxtOwnerStreet1().getName());
				getJPanel3().add(
					gettxtOwnerStreet2(),
					gettxtOwnerStreet2().getName());
				getJPanel3().add(
					gettxtOwnerCity(),
					gettxtOwnerCity().getName());
				getJPanel3().add(
					gettxtOwnerState(),
					gettxtOwnerState().getName());
				getJPanel3().add(
					gettxtOwnerZpcd(),
					gettxtOwnerZpcd().getName());
				getJPanel3().add(
					gettxtOwnerZpcdP4(),
					gettxtOwnerZpcdP4().getName());
				getJPanel3().add(
					gettxtOwnerCntry(),
					gettxtOwnerCntry().getName());
				getJPanel3().add(
					gettxtOwnerCntryZpcd(),
					gettxtOwnerCntryZpcd().getName());
				getJPanel3().add(
					getstcLblOwnerId(),
					getstcLblOwnerId().getName());
				getJPanel3().add(
					getstcLblEMail(),
					getstcLblEMail().getName());
				getJPanel3().add(
					gettxtEMail(),
					gettxtEMail().getName());
				getJPanel3().add(
					getstcLblPhoneNo(),
					getstcLblPhoneNo().getName());
				getJPanel3().add(
					gettxtPhoneNo(),
					gettxtPhoneNo().getName());
				getJPanel3().add(
					getstcLblDash(),
					getstcLblDash().getName());
				getJPanel3().add(
					getstcLblDealerNo(),
					getstcLblDealerNo().getName());
				getJPanel3().add(
					gettxtDealerNo(),
					gettxtDealerNo().getName());
				// user code begin {1}
				ivjJPanel3.setBounds(420, 6, 321, 336);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjJPanel3;
	}

	/**
	 * Get the Organization Name, Organization Number from the combo box
	 * 
	 * @param aiIndex
	 */
	private void getOrgNoFromDropDown(int aiIndex)
	{
		String lsOrgNo = (String) cvOrgNo.elementAt(aiIndex);
		csOrgNo = lsOrgNo.substring(50).trim();
		return;
	}

	/**
	 * Get the RegPltCd Description, RegPltCd  
	 * Reset caPltTypeData, Display accordingly 
	 * 
	 * @param aiIndex
	 */
	private void getRegPltCdFromDropDown(int aiIndex)
	{
		String lsRegPltCd = (String) svPlateType.elementAt(aiIndex);
		csRegPltCd = lsRegPltCd.substring(50).trim();

		caPltTypeData = PlateTypeCache.getPlateType(csRegPltCd);
		csOrgNo = "";
		// Display if not production 
		if (SystemProperty.getProdStatus()
			!= SystemProperty.APP_PROD_STATUS)
		{
			System.out.println(
				csRegPltCd
					+ " Annual: "
					+ (caPltTypeData.getAnnualPltIndi() == 1
						? "Yes"
						: "No")
					+ " MaxByteCount: "
					+ caPltTypeData.getMaxByteCount()
					+ " ISAAllowdCd: "
					+ caPltTypeData.getISAAllowdCd());
		}
		setAddlSetToDisplay();
		setNewPlatesDesiredToDisplay();
		setRequestRenewalNoticeToDisplay();
		resetInvItmYrOnPltChng();
		setDealerNoToDisplay();
		// defect 9248 
		setISAToDisplay();
		// end defect 9248 
		return;
	}

	/**
	 * Get the IndiFieldValue from Drop Down for ResrvReasnCd  
	 * 
	 * @param aiIndex
	 */
	private String getResrvReasnCdFromDropDown()
	{
		String lsIndiDesc =
			(String) svResrvReasnDesc.elementAt(
				getcomboResrvReasnDesc().getSelectedIndex());

		String lsResrvReasnCd = lsIndiDesc.substring(50).trim();

		// Display if not production 
		if (SystemProperty.getProdStatus()
			!= SystemProperty.APP_PROD_STATUS)
		{
			System.out.println(lsResrvReasnCd + " " + lsIndiDesc);
		}
		return lsResrvReasnCd;
	}

	/**
	 * Return the ivjRTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(null);
				ivjRTSDialogBoxContentPane.add(
					getchkAddlSet(),
					getchkAddlSet().getName());
				ivjRTSDialogBoxContentPane.add(
					getchkChrgSpclPltFee(),
					getchkChrgSpclPltFee().getName());
				// user code end
				ivjRTSDialogBoxContentPane.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				ivjRTSDialogBoxContentPane.add(
					getJPanel2(),
					getJPanel2().getName());
				ivjRTSDialogBoxContentPane.add(getJPanel3(), null);
				ivjRTSDialogBoxContentPane.add(
					getchkNewPlatesDesired(),
					null);
				ivjRTSDialogBoxContentPane.add(
					getchkRequestRenewalNotice(),
					null);
				// defect 10441 
				ivjRTSDialogBoxContentPane.add(
					getchkMktngAllowd(),
					null);
				// end defect 10441

				// defect 10507  
				ivjRTSDialogBoxContentPane.add(
					getchkElectionPndng(),
					null);
				// end defect 10507
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * Return the ivjstcLblApplDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblApplDate()
	{
		if (ivjstcLblApplDate == null)
		{
			try
			{
				ivjstcLblApplDate = new JLabel();
				ivjstcLblApplDate.setSize(98, 20);
				ivjstcLblApplDate.setName("stcLblApplDate");
				ivjstcLblApplDate.setText(" Application Date:");
				ivjstcLblApplDate.setLocation(38, 157);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblApplDate;
	}

	/**
	 * This method initializes ivjstcLblAuctnPrice
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAuctnPrice()
	{
		if (ivjstcLblAuctnPrice == null)
		{
			ivjstcLblAuctnPrice = new JLabel();
			ivjstcLblAuctnPrice.setSize(104, 20);
			ivjstcLblAuctnPrice.setText("Auction Price:");
			ivjstcLblAuctnPrice.setLocation(32, 257);
			ivjstcLblAuctnPrice.setHorizontalAlignment(
				javax.swing.SwingConstants.RIGHT);
			ivjstcLblAuctnPrice.setHorizontalTextPosition(
				javax.swing.SwingConstants.RIGHT);
		}
		return ivjstcLblAuctnPrice;
	}

	/**
	 * Return the ivjstcLblCounty property value.
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
				ivjstcLblCounty.setName("stcLblCounty");
				ivjstcLblCounty.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_C);
				ivjstcLblCounty.setText("County Of Residence:");
				ivjstcLblCounty.setLabelFor(gettxtResComptCntyNo());
				ivjstcLblCounty.setSize(121, 20);
				ivjstcLblCounty.setLocation(15, 232);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblCounty;
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
				ivjstcLblDash.setSize(7, 14);
				ivjstcLblDash.setName("JLabel1");
				ivjstcLblDash.setText("-");
				ivjstcLblDash.setLocation(224, 163);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblDash;
	}

	/**
	 * Return the ivjstcLblDealerNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDealerNo()
	{
		if (ivjstcLblDealerNo == null)
		{
			try
			{
				ivjstcLblDealerNo = new JLabel();
				ivjstcLblDealerNo.setName("stcLblDealerNo");
				ivjstcLblDealerNo.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_L);
				ivjstcLblDealerNo.setText("Dealer License No:");
				ivjstcLblDealerNo.setLabelFor(gettxtDealerNo());
				ivjstcLblDealerNo.setSize(111, 20);
				ivjstcLblDealerNo.setLocation(13, 285);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblDealerNo;
	}

	/**
	 * Return the ivjstcLblEMail property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblEMail()
	{
		if (ivjstcLblEMail == null)
		{
			try
			{
				ivjstcLblEMail = new JLabel();
				ivjstcLblEMail.setName("stcLblEMail");
				ivjstcLblEMail.setText(CommonConstant.TXT_EMAIL);
				ivjstcLblEMail.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_M);
				ivjstcLblEMail.setLabelFor(gettxtEMail());
				ivjstcLblEMail.setSize(38, 20);
				ivjstcLblEMail.setLocation(13, 185);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblEMail;
	}

	/**
	 * Return the ivjstcLblExpires property value.
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
				ivjstcLblExpires.setName("stcLblExpires");
				ivjstcLblExpires.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_X);
				ivjstcLblExpires.setText("Expires:");
				ivjstcLblExpires.setLabelFor(gettxtExpMo());
				ivjstcLblExpires.setSize(46, 20);
				ivjstcLblExpires.setLocation(90, 207);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblExpires;
	}

	/**
	 * This method initializes ivjstcLblFINDocNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblFINDocNo()
	{
		if (ivjstcLblFINDocNo == null)
		{
			ivjstcLblFINDocNo = new JLabel();
			ivjstcLblFINDocNo.setSize(104, 20);
			ivjstcLblFINDocNo.setText("FIN Doc No:");
			ivjstcLblFINDocNo.setLocation(32, 407);
			ivjstcLblFINDocNo.setHorizontalAlignment(
				javax.swing.SwingConstants.RIGHT);
			ivjstcLblFINDocNo.setHorizontalTextPosition(
				javax.swing.SwingConstants.RIGHT);
		}
		return ivjstcLblFINDocNo;
	}

	/**
	 * Return the ivjstcLblMfgPlateNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblMfgPlateNo()
	{
		if (ivjstcLblMfgPlateNo == null)
		{
			try
			{
				ivjstcLblMfgPlateNo = new JLabel();
				ivjstcLblMfgPlateNo.setSize(74, 20);
				ivjstcLblMfgPlateNo.setName("stcLblMfgPlateNo");
				ivjstcLblMfgPlateNo.setText("Mfg Plate No:");
				ivjstcLblMfgPlateNo.setLocation(62, 132);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblMfgPlateNo;
	}

	/**
	 * Return the ivjstcLblMfgDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblMgfDate()
	{
		if (ivjstcLblMfgDate == null)
		{
			try
			{
				ivjstcLblMfgDate = new JLabel();
				ivjstcLblMfgDate.setSize(104, 20);
				ivjstcLblMfgDate.setName("stcLblMgfDate");
				ivjstcLblMfgDate.setText("Mfg Request Date:");
				ivjstcLblMfgDate.setLocation(32, 182);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblMfgDate;
	}

	/**
	 * Return the ivjstcLblNew property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblNew()
	{
		if (ivjstcLblNew == null)
		{
			ivjstcLblNew = new JLabel();
			ivjstcLblNew.setBounds(238, 207, 164, 20);
			ivjstcLblNew.setText("");
			ivjstcLblNew.setFont(
				new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
			ivjstcLblNew.setForeground(
				java.awt.SystemColor.textInactiveText);
		}
		return ivjstcLblNew;
	}

	/**
	 * Return the ivjstcLblOrganization property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOrganization()
	{
		if (ivjstcLblOrganization == null)
		{
			try
			{
				ivjstcLblOrganization = new JLabel();
				ivjstcLblOrganization.setName("stcLblOrganization");
				ivjstcLblOrganization.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_Z);
				ivjstcLblOrganization.setText("Organization:");
				ivjstcLblOrganization.setLabelFor(
					getcomboOrganizationName());
				ivjstcLblOrganization.setSize(75, 20);
				ivjstcLblOrganization.setLocation(61, 82);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblOrganization;
	}

	/**
	 * Return the ivjstcLblAddress property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOwnerAddress()
	{
		if (ivjstcLblAddress == null)
		{
			try
			{
				ivjstcLblAddress = new JLabel();
				ivjstcLblAddress.setName("stcLblAddress");
				ivjstcLblAddress.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_W);
				ivjstcLblAddress.setText("Owner Address:");
				ivjstcLblAddress.setLabelFor(gettxtOwnerStreet1());
				ivjstcLblAddress.setSize(106, 20);
				ivjstcLblAddress.setLocation(13, 85);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblAddress;
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
				ivjstcLblOwnerId.setName("ivjstcLblOwnerId");
				ivjstcLblOwnerId.setText("Owner Id:");
				ivjstcLblOwnerId.setBounds(123, 10, 58, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblOwnerId;
	}

	/**
	 * Return the ivjstcLblName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOwnerName()
	{
		if (ivjstcLblName == null)
		{
			try
			{
				ivjstcLblName = new JLabel();
				ivjstcLblName.setName("stcLblName");
				ivjstcLblName.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_O);
				ivjstcLblName.setText("Owner Name:");
				ivjstcLblName.setLabelFor(gettxtOwnerName1());
				ivjstcLblName.setSize(96, 20);
				ivjstcLblName.setLocation(13, 10);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblName;
	}

	/**
	 * Return the ivjstcLblPhoneNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPhoneNo()
	{
		if (ivjstcLblPhoneNo == null)
		{
			try
			{
				ivjstcLblPhoneNo = new JLabel();
				ivjstcLblPhoneNo.setName("stcLblPhoneNo");
				ivjstcLblPhoneNo.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_P);
				ivjstcLblPhoneNo.setText("Phone No:");
				ivjstcLblPhoneNo.setLabelFor(gettxtPhoneNo());
				ivjstcLblPhoneNo.setSize(64, 20);
				ivjstcLblPhoneNo.setLocation(13, 235);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblPhoneNo;
	}

	/**
	 * Return the ivjstcLblPlateAge property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateAge()
	{
		if (ivjstcLblPlateAge == null)
		{
			try
			{
				ivjstcLblPlateAge = new JLabel();
				ivjstcLblPlateAge.setName("stcLblPlateAge");
				ivjstcLblPlateAge.setText("Plate Age:");
				ivjstcLblPlateAge.setLabelFor(gettxtPlateAge());
				ivjstcLblPlateAge.setSize(57, 20);
				ivjstcLblPlateAge.setLocation(79, 32);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblPlateAge;
	}

	/**
	 * Return the ivjstcLblPlateNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateNo()
	{
		if (ivjstcLblPlateNo == null)
		{
			try
			{
				ivjstcLblPlateNo = new JLabel();
				ivjstcLblPlateNo.setName("stcLblPlateNo");
				ivjstcLblPlateNo.setText("Plate No:");
				ivjstcLblPlateNo.setLabelFor(gettxtPlateNo());
				ivjstcLblPlateNo.setSize(50, 20);
				ivjstcLblPlateNo.setLocation(86, 7);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblPlateNo;
	}

	/**
	 * Return the ivjstcLblPlateType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateType()
	{
		if (ivjstcLblPlateType == null)
		{
			try
			{
				ivjstcLblPlateType = new JLabel();
				ivjstcLblPlateType.setName("stcLblPlateType");
				ivjstcLblPlateType.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_T);
				ivjstcLblPlateType.setText("Plate Type:");
				ivjstcLblPlateType.setLabelFor(getcomboPlateType());
				ivjstcLblPlateType.setSize(62, 20);
				ivjstcLblPlateType.setLocation(74, 57);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblPlateType;
	}

	/**
	 * Return the ivjstcLblPltOwnrDist property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPltOwnrDist()
	{
		if (ivjstcLblPltOwnrDist == null)
		{
			try
			{
				ivjstcLblPltOwnrDist = new JLabel();
				ivjstcLblPltOwnrDist.setSize(117, 20);
				ivjstcLblPltOwnrDist.setName("stcLblPltOwnrDist");
				ivjstcLblPltOwnrDist.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_D);
				ivjstcLblPltOwnrDist.setText("Plate Owner District:");
				ivjstcLblPltOwnrDist.setLabelFor(gettxtPltOwnrDist());
				ivjstcLblPltOwnrDist.setLocation(19, 307);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblPltOwnrDist;
	}

	/**
	 * Return the ivjstcLblPltOwnrOfcCd property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPltOwnrOfcCd()
	{
		if (ivjstcLblPltOwnrOfcCd == null)
		{
			try
			{
				ivjstcLblPltOwnrOfcCd = new JLabel();
				ivjstcLblPltOwnrOfcCd.setSize(111, 20);
				ivjstcLblPltOwnrOfcCd.setName("stcLblPltOwnrOfcCd");
				ivjstcLblPltOwnrOfcCd.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_F);
				ivjstcLblPltOwnrOfcCd.setText("Plate Owner OfcCd:");
				ivjstcLblPltOwnrOfcCd.setLabelFor(gettxtPltOwnrOfcCd());
				ivjstcLblPltOwnrOfcCd.setLocation(25, 282);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblPltOwnrOfcCd;
	}

	/**
	 * Return the ivjstcLblRequestType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRequestType()
	{
		if (ivjstcLblRequestType == null)
		{
			try
			{
				ivjstcLblRequestType = new JLabel();
				ivjstcLblRequestType.setSize(80, 20);
				ivjstcLblRequestType.setName("stcLblRequestType");
				ivjstcLblRequestType.setText("Request Type:");
				ivjstcLblRequestType.setLocation(56, 107);
				// user code begin {1}
				ivjstcLblRequestType.setLabelFor(getcomboRequestType());
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblRequestType;
	}

	/**
	 * This method initializes ivjstcLblResrvDate
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblResrvDate()
	{
		if (ivjstcLblResrvDate == null)
		{
			ivjstcLblResrvDate = new JLabel();
			ivjstcLblResrvDate.setBounds(12, 356, 124, 20);
			ivjstcLblResrvDate.setText("Reserve Date:");
			ivjstcLblResrvDate.setHorizontalAlignment(
				javax.swing.SwingConstants.RIGHT);
			ivjstcLblResrvDate.setHorizontalTextPosition(
				javax.swing.SwingConstants.RIGHT);
			ivjstcLblResrvDate.setDisplayedMnemonic(
				java.awt.event.KeyEvent.VK_E);
			ivjstcLblResrvDate.setLabelFor(gettxtResrvDate());
		}
		return ivjstcLblResrvDate;
	}

	/**
	 * Return the ivjstcLblSlash property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSlash()
	{
		if (ivjstcLblSlash == null)
		{
			try
			{
				ivjstcLblSlash = new JLabel();
				ivjstcLblSlash.setSize(7, 14);
				ivjstcLblSlash.setName("stcLblSlash");
				ivjstcLblSlash.setText("/");
				ivjstcLblSlash.setLocation(176, 210);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblSlash;
	}

	//	/**
	//	 * Return the ivjstcLblSpclRegstkrNo property value.
	//	 * 
	//	 * @return JLabel
	//	 */
	//	private JLabel getstcLblSpclRegStkrNo()
	//	{
	//		if (ivjstcLblSpclRegstkrNo == null)
	//		{
	//			try
	//			{
	//				ivjstcLblSpclRegstkrNo = new JLabel();
	//				ivjstcLblSpclRegstkrNo.setSize(132, 20);
	//				ivjstcLblSpclRegstkrNo.setName("stcLblSpclRegStkrNo");
	//				ivjstcLblSpclRegstkrNo.setDisplayedMnemonic(
	//					java.awt.event.KeyEvent.VK_K);
	//				ivjstcLblSpclRegstkrNo.setText(
	//					"Special Reg Sticker No:");
	//				ivjstcLblSpclRegstkrNo.setLabelFor(
	//					gettxtSpclRegStkrNo());
	//				ivjstcLblSpclRegstkrNo.setLocation(4, 307);
	//			}
	//			catch (Throwable aeIvjEx)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIvjEx);
	//			}
	//		}
	//		return ivjstcLblSpclRegstkrNo;
	//	}

	/**
	 * Return the ivjstcLblSpclRemks property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSpclRemarks()
	{
		if (ivjstcLblSpclRemks == null)
		{
			try
			{
				ivjstcLblSpclRemks = new JLabel();
				ivjstcLblSpclRemks.setSize(100, 20);
				ivjstcLblSpclRemks.setName("stcLblSpclReMks");
				ivjstcLblSpclRemks.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_R);
				ivjstcLblSpclRemks.setText("Special Remarks:");
				ivjstcLblSpclRemks.setLabelFor(gettxtSpclRemarks());
				ivjstcLblSpclRemks.setLocation(36, 332);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblSpclRemks;
	}

	/**
	 * Return the ivjstcLblStatus property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblStatus()
	{
		if (ivjstcLblStatus == null)
		{
			try
			{
				ivjstcLblStatus = new JLabel();
				ivjstcLblStatus.setSize(42, 20);
				ivjstcLblStatus.setName("stcLblStatus");
				ivjstcLblStatus.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjstcLblStatus.setText("Status:");
				ivjstcLblStatus.setLocation(94, 107);
				// user code begin {1}
				ivjstcLblStatus.setLabelFor(getcomboRequestType());
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblStatus;
	}

	/**
	 * This method initializes ivjstcLblTerm
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTerm()
	{
		if (ivjstcLblTerm == null)
		{
			ivjstcLblTerm = new JLabel();
			ivjstcLblTerm.setHorizontalAlignment(
				javax.swing.SwingConstants.RIGHT);
			ivjstcLblTerm.setHorizontalTextPosition(
				javax.swing.SwingConstants.RIGHT);
			ivjstcLblTerm.setSize(42, 20);
			ivjstcLblTerm.setText("Term: ");
			ivjstcLblTerm.setLocation(248, 32);
		}
		return ivjstcLblTerm;
	}

	/**
	 * This method initializes ivjstcLblVndrTransDate
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVndrTransDate()
	{
		if (ivjstcLblVndrTransDate == null)
		{
			ivjstcLblVndrTransDate = new JLabel();
			ivjstcLblVndrTransDate.setSize(128, 20);
			ivjstcLblVndrTransDate.setText("Vendor Trans Date:");
			ivjstcLblVndrTransDate.setHorizontalAlignment(
				javax.swing.SwingConstants.RIGHT);
			ivjstcLblVndrTransDate.setHorizontalTextPosition(
				javax.swing.SwingConstants.RIGHT);
			ivjstcLblVndrTransDate.setLocation(8, 381);
		}
		return ivjstcLblVndrTransDate;
	}

	/**
	 * Return the ivjtxtApplDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtApplDate()
	{
		if (ivjtxtApplDate == null)
		{
			try
			{
				if (ivjtxtApplDate == null)
				{
					ivjtxtApplDate = new RTSDateField();
					ivjtxtApplDate.setName("txtApplDate");
					ivjtxtApplDate.setBounds(146, 157, 76, 20);
					// user code begin {1}
					// user code end
				}
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtApplDate;
	}

	/**
	 * This method initializes ivjtxtAuctnPrice
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtAuctnPrice()
	{
		if (ivjtxtAuctnPrice == null)
		{
			ivjtxtAuctnPrice = new RTSInputField();
			ivjtxtAuctnPrice.setBounds(146, 256, 85, 20);
			ivjtxtAuctnPrice.setInput(RTSInputField.DOLLAR_ONLY);
			ivjtxtAuctnPrice.setText("");
		}
		return ivjtxtAuctnPrice;
	}

	/**
	 * Return the ivjtxtDealerNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtDealerNo()
	{
		if (ivjtxtDealerNo == null)
		{
			try
			{
				ivjtxtDealerNo = new RTSInputField();
				ivjtxtDealerNo.setSize(81, 20);
				ivjtxtDealerNo.setName("txtDealerNo");
				// defect 9145
				// Changed from numeric
				ivjtxtDealerNo.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				// end defect 9145
				// defect 9585
				//	Change DlrGDN length from 6 to 10
				// ivjtxtDealerNo.setMaxLength(6);
				ivjtxtDealerNo.setMaxLength(
					CommonConstant.DLRGDN_MAX_LENGTH);
				// end defect 9585
				// user code end
				ivjtxtDealerNo.setLocation(13, 310);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtDealerNo;
	}

	/**
	 * Return the ivjtxtEMail property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtEMail()
	{
		if (ivjtxtEMail == null)
		{
			try
			{
				ivjtxtEMail = new RTSInputField();
				ivjtxtEMail.setSize(300, 20);
				ivjtxtEMail.setName("txtEMail");
				ivjtxtEMail.setText(" ");
				ivjtxtEMail.setMaxLength(50);
				ivjtxtEMail.setLocation(13, 210);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtEMail;
	}

	/**
	 * Return the ivjtxtExpMo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtExpMo()
	{
		if (ivjtxtExpMo == null)
		{
			try
			{
				ivjtxtExpMo = new RTSInputField();
				ivjtxtExpMo.setName("txtExpMo");
				ivjtxtExpMo.setText("");
				ivjtxtExpMo.setBounds(146, 207, 23, 20);
				// user code begin {1}
				ivjtxtExpMo.setMaxLength(2);
				ivjtxtExpMo.setInput(RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtExpMo;
	}

	/**
	 * Return the ivjtxtExpYr property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtExpYr()
	{
		if (ivjtxtExpYr == null)
		{
			try
			{
				ivjtxtExpYr = new RTSInputField();
				ivjtxtExpYr.setName("txtExpYr");
				ivjtxtExpYr.setText("");
				ivjtxtExpYr.setBounds(187, 207, 35, 20);
				// user code begin {1}
				ivjtxtExpYr.setMaxLength(4);
				ivjtxtExpYr.setInput(RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtExpYr;
	}
	/**
	 * This method initializes ivjtxtFINDocNo
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtFINDocNo()
	{
		if (ivjtxtFINDocNo == null)
		{
			ivjtxtFINDocNo = new RTSInputField();
			ivjtxtFINDocNo.setSize(76, 20);
			ivjtxtFINDocNo.setLocation(146, 407);
			ivjtxtFINDocNo.setInput(RTSInputField.DEFAULT);
			ivjtxtFINDocNo.setText("");
		}
		return ivjtxtFINDocNo;
	}

	/**
	 * Return the ivjtxtInvItmYr property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtInvItmYr()
	{
		if (ivjtxtInvItmYr == null)
		{
			ivjtxtInvItmYr = new RTSInputField();
			ivjtxtInvItmYr.setSize(33, 20);
			ivjtxtInvItmYr.setText("");
			ivjtxtInvItmYr.setMaxLength(4);
			ivjtxtInvItmYr.setInput(RTSInputField.NUMERIC_ONLY);
			ivjtxtInvItmYr.setLocation(371, 57);
		}
		return ivjtxtInvItmYr;
	}

	/**
	 * Return the ivjtxtMfgDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtMfgDate()
	{
		if (ivjtxtMfgDate == null)
		{
			try
			{
				ivjtxtMfgDate = new RTSDateField();
				ivjtxtMfgDate.setName("txtMfgDate");
				ivjtxtMfgDate.setBounds(146, 182, 76, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtMfgDate;
	}

	/**
	 * Return the ivjtxtMfgPlateNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMfgPlateNo()
	{
		if (ivjtxtMfgPlateNo == null)
		{
			try
			{
				ivjtxtMfgPlateNo = new RTSInputField();
				ivjtxtMfgPlateNo.setName("txtMfgPlateNo");
				ivjtxtMfgPlateNo.setBounds(146, 132, 76, 20);
				// user code begin {1}
				ivjtxtMfgPlateNo.setMaxLength(8);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtMfgPlateNo;
	}

	/**
	 * Return the ivjtxtOwnrCity property value.
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
				ivjtxtOwnerCity.setSize(138, 20);
				ivjtxtOwnerCity.setName("txtCity");
				ivjtxtOwnerCity.setText("");
				ivjtxtOwnerCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
				ivjtxtOwnerCity.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerCity.setLocation(13, 160);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
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
				ivjtxtOwnerCntry.setSize(37, 20);
				ivjtxtOwnerCntry.setName("ivjtxtOwnerCntry");
				ivjtxtOwnerCntry.setText("");
				ivjtxtOwnerCntry.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerCntry.setMaxLength(
					CommonConstant.LENGTH_CNTRY);
				ivjtxtOwnerCntry.setLocation(153, 160);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
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
				ivjtxtOwnerCntryZpcd.setSize(70, 20);
				ivjtxtOwnerCntryZpcd.setName("ivjtxtOwnerCntryZpcd");
				ivjtxtOwnerCntryZpcd.setText("");
				ivjtxtOwnerCntryZpcd.setMaxLength(
					CommonConstant.LENGTH_CNTRY_ZIP);
				ivjtxtOwnerCntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtOwnerCntryZpcd.setLocation(192, 160);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
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
				ivjtxtOwnerId.setName("ivjtxtOwnerId");
				ivjtxtOwnerId.setText("");
				ivjtxtOwnerId.setBounds(187, 10, 75, 20);
				// user code begin {1}
				ivjtxtOwnerId.setMaxLength(
					CommonConstant.LENGTH_OWNERID);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
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
				ivjtxtOwnerName1.setSize(249, 20);
				ivjtxtOwnerName1.setName("ivjtxtOwnerName1");
				ivjtxtOwnerName1.setText("");
				ivjtxtOwnerName1.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerName1.setMaxLength(
					CommonConstant.LENGTH_NAME);
				ivjtxtOwnerName1.setLocation(13, 35);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
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
				ivjtxtOwnerName2.setSize(250, 20);
				ivjtxtOwnerName2.setName("ivjtxtOwnerName2");
				ivjtxtOwnerName2.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerName2.setMaxLength(
					CommonConstant.LENGTH_NAME);
				ivjtxtOwnerName2.setLocation(13, 60);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
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
				ivjtxtOwnerState.setText("");
				ivjtxtOwnerState.setBounds(154, 160, 24, 20);
				// user code begin {1}
				ivjtxtOwnerState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				ivjtxtOwnerState.setInput(RTSInputField.ALPHA_ONLY);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
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
				ivjtxtOwnerStreet1.setSize(250, 20);
				ivjtxtOwnerStreet1.setName("ivjtxtOwnerStreet1");
				ivjtxtOwnerStreet1.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				ivjtxtOwnerStreet1.setLocation(13, 110);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
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
				ivjtxtOwnerStreet2.setSize(249, 20);
				ivjtxtOwnerStreet2.setName("ivjtxtOwnerStreet2");
				ivjtxtOwnerStreet2.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
				ivjtxtOwnerStreet2.setLocation(13, 135);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtOwnerStreet2;
	}

	/**
	 * Return the ivjtxtOwnrZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerZpcd()
	{
		if (ivjtxtOwnrZpcd == null)
		{
			try
			{
				ivjtxtOwnrZpcd = new RTSInputField();
				ivjtxtOwnrZpcd.setName("ivjtxtOwnrZpcd");
				ivjtxtOwnrZpcd.setText("");
				ivjtxtOwnrZpcd.setBounds(181, 160, 41, 20);
				// user code begin {1}
				ivjtxtOwnrZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				ivjtxtOwnrZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtOwnrZpcd;
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
				ivjtxtOwnerZpcdP4.setName("ivjtxtOwnerZpcdP4");
				ivjtxtOwnerZpcdP4.setText("");
				ivjtxtOwnerZpcdP4.setBounds(229, 160, 34, 20);
				// user code begin {1}
				ivjtxtOwnerZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// defect 10012 
				ivjtxtOwnerZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				// end defect 10012 
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtOwnerZpcdP4;
	}

	/**
	 * Return the ivjtxtPhoneNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSPhoneField gettxtPhoneNo()
	{
		if (ivjtxtPhoneNo == null)
		{
			try
			{
				ivjtxtPhoneNo = new RTSPhoneField();
				ivjtxtPhoneNo.setSize(94, 20);
				ivjtxtPhoneNo.setName("ivjtxtPhoneNo");
				ivjtxtPhoneNo.setLocation(13, 260);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtPhoneNo;
	}

	/**
	 * Return the ivjtxtPlateAge property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPlateAge()
	{
		if (ivjtxtPlateAge == null)
		{
			try
			{
				ivjtxtPlateAge = new RTSInputField();
				ivjtxtPlateAge.setName("txtPlateAge");
				ivjtxtPlateAge.setBounds(146, 32, 45, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtPlateAge;
	}

	/**
	 * Return the ivjtxtPlateNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPlateNo()
	{
		if (ivjtxtPlateNo == null)
		{
			try
			{
				ivjtxtPlateNo = new RTSInputField();
				ivjtxtPlateNo.setName("txtPlateNO");
				ivjtxtPlateNo.setText("");
				ivjtxtPlateNo.setBounds(146, 7, 76, 20);
				// user code begin {1}
				ivjtxtPlateNo.setMaxLength(CommonConstant.LENGTH_PLTNO);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtPlateNo;
	}

	/**
	 * Return the ivjtxtPltOwnrDist property value.
	 * 
	 * @return JTextField
	 */
	private RTSInputField gettxtPltOwnrDist()
	{
		if (ivjtxtPltOwnrDist == null)
		{
			try
			{
				ivjtxtPltOwnrDist = new RTSInputField();
				ivjtxtPltOwnrDist.setName("txtPltOwnrDist");
				ivjtxtPltOwnrDist.setBounds(146, 307, 37, 20);
				// user code begin {1}
				ivjtxtPltOwnrDist.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtPltOwnrDist.setMaxLength(3);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtPltOwnrDist;
	}

	/**
	 * Return the ivjtxtPltOwnrOfcCd property value.
	 * 
	 * @return JTextField
	 */
	private RTSInputField gettxtPltOwnrOfcCd()
	{
		if (ivjtxtPltOwnrOfcCd == null)
		{
			try
			{
				ivjtxtPltOwnrOfcCd = new RTSInputField();
				ivjtxtPltOwnrOfcCd.setName("txtPltOwnrOfcCd");
				ivjtxtPltOwnrOfcCd.setBounds(146, 282, 28, 20);
				// user code begin {1}
				ivjtxtPltOwnrOfcCd.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtPltOwnrOfcCd.setMaxLength(2);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtPltOwnrOfcCd;
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
				ivjtxtResComptCntyNo.setName("txtResComptCntyNo");
				ivjtxtResComptCntyNo.setBounds(146, 232, 36, 20);
				// user code begin {1}
				ivjtxtResComptCntyNo.setMaxLength(
					CommonConstant.LENGTH_OFFICE_ISSUANCENO);
				ivjtxtResComptCntyNo.setInput(
					RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtResComptCntyNo;
	}

	/**
	 * This method initializes ivjtxtResrvDate
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtResrvDate()
	{
		if (ivjtxtResrvDate == null)
		{
			ivjtxtResrvDate = new RTSDateField();
			ivjtxtResrvDate.setBounds(146, 357, 76, 20);
		}
		return ivjtxtResrvDate;
	}

	//	/**
	//	 * Return the ivjtxtSpclRegStkrNo property value.
	//	 * 
	//	 * @return RTSInputField
	//	 */
	//	private RTSInputField gettxtSpclRegStkrNo()
	//	{
	//		if (ivjtxtSpclRegStkrNo == null)
	//		{
	//			try
	//			{
	//				ivjtxtSpclRegStkrNo = new RTSInputField();
	//				ivjtxtSpclRegStkrNo.setName("txtSpclRegStkrNo");
	//				ivjtxtSpclRegStkrNo.setBounds(146, 307, 82, 20);
	//				// user code begin {1}
	//				ivjtxtSpclRegStkrNo.setMaxLength(10);
	//				ivjtxtSpclRegStkrNo.setInput(
	//					RTSInputField.ALPHANUMERIC_NOSPACE);
	//				// user code end
	//			}
	//			catch (Throwable aeIvjEx)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIvjEx);
	//			}
	//		}
	//		return ivjtxtSpclRegStkrNo;
	//	}

	/**
	 * Return the ivjtxtSpclRemarks property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtSpclRemarks()
	{
		if (ivjtxtSpclRemarks == null)
		{
			try
			{
				ivjtxtSpclRemarks = new RTSInputField();
				ivjtxtSpclRemarks.setBounds(146, 332, 222, 20);
				ivjtxtSpclRemarks.setName("ivjtxtSpclRemarks");
				// ser code begin {1}
				ivjtxtSpclRemarks.setMaxLength(30);
				// ser code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtSpclRemarks;
	}

	/**
	 * This method initializes ivjtxtTerm
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTerm()
	{
		if (ivjtxtTerm == null)
		{
			ivjtxtTerm = new RTSInputField();
			ivjtxtTerm.setBounds(296, 32, 29, 20);
		}
		return ivjtxtTerm;
	}
	/**
	 * This method initializes ivjtxtVendorTransDate
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtVendorTransDate()
	{
		if (ivjtxtVendorTransDate == null)
		{
			ivjtxtVendorTransDate = new RTSDateField();
			ivjtxtVendorTransDate.setBounds(146, 382, 76, 20);
		}
		return ivjtxtVendorTransDate;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException 
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
	 * Modify Data upon selection/deselection of ISA checkbox
	 */
	private void handleISAChkBox()
	{
		boolean lbISA = getchkISA().isSelected();
		int liISAIndi = lbISA ? 1 : 0;
		caSpclPltRegisData.setISAIndi(liISAIndi);

		// If ISA reset   
		if (lbISA != cbOrigISA)
		{
			gettxtMfgPlateNo().setEnabled(true);
			gettxtMfgPlateNo().selectAll();
			gettxtMfgPlateNo().requestFocus();
		}
		// Reset to Original MfgPltNo 
		else
		{
			gettxtMfgPlateNo().setEnabled(false);
			gettxtMfgPlateNo().setText(csOrigMfgPltNo);
			caSpclPltRegisData.setMfgPltNo(csOrigMfgPltNo);
		}
	}

	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName("FrmSpecialPlateInformationSPL002");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(754, 535);
			setTitle(SPCL_PLT_INFO_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
			// user code begin {1}
			setRequestFocus(false);

			// defect 10127 
			caNameAddrComp =
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
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID,
					ErrorsConstant.ERR_NUM_INCOMPLETE_ADDR_DATA,
					CommonConstant.TX_DEFAULT_STATE);
			// end defect 10127 
			// user code end
		}
		catch (Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Determine if DlrPlt
	 * 
	 * @return boolean
	 */
	private boolean isDlrPlt()
	{
		boolean lbDlrPlt = false;
		if (csRegPltCd.trim().equals(SpecialPlatesConstant.PLPDLR)
			|| csRegPltCd.trim().equals(SpecialPlatesConstant.PLPDLRMC))
		{
			lbDlrPlt = true;
		}
		return lbDlrPlt;
	}

	/** 
	 * Is Expired
	 * 
	 * @return boolean
	 */
	private boolean isExpired()
	{
		boolean lbExpired = false;
		if (gettxtExpMo().getText().length() > 0
			&& gettxtExpYr().getText().length() > 0)
		{
			int liExpMo = Integer.parseInt(gettxtExpMo().getText());
			int liExpYr = Integer.parseInt(gettxtExpYr().getText());
			lbExpired =
				CommonValidations.isRegistrationExpired(
					liExpMo,
					liExpYr);
		}
		return lbExpired;
	}

	/**
	 * Determine if all Data Set on SPL002
	 * 
	 * @return boolean
	 */
	private boolean isSetDataFinished()
	{
		return cbSetDataFinished;
	}

	/**
	 * is SPAPPL
	 * 
	 * @return boolean
	 */
	private boolean isSPAPPL()
	{
		return UtilityMethods.isSPAPPL(csTransCd);
	}

	/**
	 * is SPDEL
	 * 
	 * @return boolean
	 */
	private boolean isSPDEL()
	{
		return csTransCd.equals(TransCdConstant.SPDEL);
	}

	/**
	 * is SPREV || SPRNR 
	 *
	 * @return boolean
	 */
	private boolean isSPREV()
	{
		return csTransCd.equals(TransCdConstant.SPREV)
			|| csTransCd.equals(TransCdConstant.SPRNR);
	}

	/**
	 * is SPRNW
	 *
	 * @return boolean
	 */
	private boolean isSPRNW()
	{
		return csTransCd.equals(TransCdConstant.SPRNW);
	}

	/**
	 * is SPRSRV
	 *
	 * @return boolean
	 */
	private boolean isSPRSRV()
	{
		// defect 9242 
		// add "&& isSPREV()" to disregard SPAPPR 
		return SystemProperty.isHQ()
			&& (csTransCd.equals(TransCdConstant.SPRSRV)
				|| (csRequestType != null
					&& csRequestType.equals(
						SpecialPlatesConstant.FROM_RESERVE)
					&& isSPREV()));
		// end defect 9242 
	}

	/**
	 * is SPUNAC
	 *
	 * @return boolean
	 */
	private boolean isSPUNAC()
	{
		return SystemProperty.isHQ()
			&& (csTransCd.equals(TransCdConstant.SPUNAC)
				|| (csRequestType != null
					&& csRequestType.equals(
						SpecialPlatesConstant.UNACCEPTABLE)
					&& isSPREV()));
	}

	/**
	 * Is Sunset Plate 
	 * 
	 * @return boolean
	 */
	private boolean isSunset()
	{
		boolean lbSunset = false;
		int liToday = new RTSDate().getYYYYMMDDDate();
		OrganizationNumberData laOrgNoData =
			OrganizationNumberCache.getOrgNo(
				caPltTypeData.getBaseRegPltCd(),
				csOrgNo,
				liToday);
		if (laOrgNoData != null)
		{
			int liSunsetDate = laOrgNoData.getSunsetDate();
			lbSunset = (liSunsetDate != 0 && liSunsetDate < liToday);
		}
		return lbSunset;
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aaIE 
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		if (aaIE.getSource() == ivjchkUSA && isSetDataFinished())
		{
			boolean lbEnable =
				!UtilityMethods.isSpecialPlates(csTransCd)
					|| (UtilityMethods.isSpecialPlates(csTransCd)
						&& !isSPDEL()
						&& !isSPUNAC());

			// defect 10127 
			caNameAddrComp.resetPerUSA(
				aaIE.getStateChange() == ItemEvent.SELECTED);
			// end defect 10127

			gettxtOwnerZpcd().setEnabled(
				gettxtOwnerZpcd().isVisible() && lbEnable);
			gettxtOwnerZpcdP4().setEnabled(
				gettxtOwnerZpcdP4().isVisible() && lbEnable);
			gettxtOwnerCntry().setEnabled(
				gettxtOwnerCntry().isVisible() && lbEnable);
			gettxtOwnerCntryZpcd().setEnabled(
				gettxtOwnerCntryZpcd().isVisible() && lbEnable);
		}

	}

	/**
	 * Populate the Organization Name
	 * 
	 * @throws RTSException
	 */
	private void populateOrganizationName() throws RTSException
	{
		getcomboOrganizationName().removeAllItems();
		getcomboOrganizationName().setSelectedIndex(-1);
		// Add 2nd parameter to indicate only want "Current" plates
		if (!csRegPltCd.equals(""))
		{
			// Show all Organizations, including sunset
			Vector lvOrgNo =
				OrganizationNumberCache.getOrgsPerPlt(
					csRegPltCd,
					false);
			if (lvOrgNo.size() != 0)
			{
				cvOrgNo = new Vector();
				for (int i = 0; i < lvOrgNo.size(); i++)
				{
					OrganizationNumberData laOrgNoData =
						(OrganizationNumberData) lvOrgNo.elementAt(i);
					String lsOrgName =
						UtilityMethods.addPaddingRight(
							laOrgNoData.getOrgNoDesc(),
							50,
							" ")
							+ laOrgNoData.getOrgNo();
					cvOrgNo.add(i, lsOrgName);
				}
				UtilityMethods.sort(cvOrgNo);
				int liIndex = -1;
				for (int i = 0; i < lvOrgNo.size(); i++)
				{
					String lsOrgNo = (String) cvOrgNo.elementAt(i);
					getcomboOrganizationName().addItem(
						lsOrgNo.substring(0, 50).trim());
					if (csOrgNo.equals(lsOrgNo.substring(50).trim())
						|| lvOrgNo.size() == 1)
					{
						liIndex = i;
						csOrgNo = lsOrgNo.substring(50).trim();
					}
				}
				comboBoxHotKeyFix(getcomboOrganizationName());
				getcomboOrganizationName().setSelectedIndex(liIndex);
				if (UtilityMethods.isSpecialPlates(csTransCd)
					&& ((isSPREV() || isSPRSRV() || isSPUNAC())
						&& !isSPAPPL()
						&& !isSPDEL()))
				{
					getcomboOrganizationName().setEnabled(true);
					getcomboOrganizationName().addActionListener(this);
				}
				else
				{
					getcomboOrganizationName().setEnabled(false);
				}
			}
		}
		else
		{
			getcomboOrganizationName().setEnabled(false);
		}
	}

	/**
	 * Populate the Plate Type 
	 * 
	 * @throws RTSException
	 */
	private void populatePlateType() throws RTSException
	{
		getcomboPlateType().removeActionListener(this);
		getcomboOrganizationName().removeActionListener(this);
		int liIndex = -1;
		getcomboPlateType().setSelectedIndex(-1);

		for (int i = 0; i < svPlateType.size(); i++)
		{
			String lsPlateType = (String) svPlateType.elementAt(i);
			String lsRegPltCd = lsPlateType.substring(50).trim();

			getcomboPlateType().addItem(
				lsPlateType.substring(0, 50).trim());

			if (caSpclPltRegisData
				.getRegPltCd()
				.trim()
				.equals(lsRegPltCd))
			{
				liIndex = i;
				// Display if not production 
				if (SystemProperty.getProdStatus()
					!= SystemProperty.APP_PROD_STATUS)
				{
					System.out.println(
						lsRegPltCd
							+ " Annual: "
							+ (caPltTypeData.getAnnualPltIndi() == 1
								? "Yes"
								: "No")
							+ " MaxByteCount: "
							+ caPltTypeData.getMaxByteCount()
							+ " ISAAllowdCd: "
							+ caPltTypeData.getISAAllowdCd());
				}
			}
		}
		comboBoxHotKeyFix(getcomboPlateType());
		getcomboPlateType().setSelectedIndex(liIndex);

		// Enable on the following conditions: 
		if (UtilityMethods.isSpecialPlates(csTransCd)
			&& ((isSPREV() || isSPRSRV() || isSPUNAC())
				&& !isSPDEL()
				&& !isSPAPPL()))
		{
			getcomboPlateType().setEnabled(true);
			getcomboPlateType().addActionListener(this);
		}
		else
		{
			getcomboPlateType().setEnabled(false);
		}
		if (liIndex >= 0)
		{
			populateOrganizationName();
		}
		else
		{
			getcomboOrganizationName().setEnabled(false);
		}
		populateRequestType();
	}

	/**
	 * Populate the Request Type combo
	 * 
	 *  - HQ will have Unacceptable, Reserved, Assigned, Manufacture
	 *  - County/Region will have Assigned, Manufacture 
	 *  - If OldPlt, can only have Assigned 
	 *  - If ROP, can only have Assigned, Manufacture
	 *  - If LP, can not have Manufacture  
	 * 
	 * @throws RTSException
	 */
	private void populateRequestType() throws RTSException
	{
		getcomboRequestType().removeActionListener(this);
		getcomboRequestType().removeAllItems();
		if (isSPREV()
			&& !csOrigMfgStatusCd.equals(
				SpecialPlatesConstant.UNACCEPTABLE_MFGSTATUSCD)
			&& !csOrigMfgStatusCd.equals(
				SpecialPlatesConstant.RESERVE_MFGSTATUSCD))
		{
			int liIndex = -1;
			int j = 0;
			boolean lbOldPlt =
				caPltTypeData != null
					&& caPltTypeData.getRegPltCd().equals("OLDPLT");

			// defect 10293 
			boolean lbROP = caPltTypeData != null
				//&& caPltTypeData.getRegPltCd().equals("ROP");
	&& caPltTypeData.isROPBaseRegPltCd();
			// end defect 10293  

			boolean lbLP =
				caPltTypeData != null
					&& caPltTypeData.getNeedsProgramCd().equals(
						SpecialPlatesConstant.LP_PLATE);

			for (int i = 0; i < svStatus.size(); i++)
			{
				String lsRequestType =
					((String) svStatus.elementAt(i)).trim();

				boolean lbMfg =
					lsRequestType.equals(
						SpecialPlatesConstant.MANUFACTURE);

				boolean lbRsrv =
					lsRequestType.equals(
						SpecialPlatesConstant.FROM_RESERVE);

				boolean lbUnacceptable =
					lsRequestType.equals(
						SpecialPlatesConstant.UNACCEPTABLE);

				boolean lbAssign =
					lsRequestType.equals(
						SpecialPlatesConstant.ASSIGNED);

				if (!(lbMfg && (lbOldPlt || lbLP))
					&& !((lbUnacceptable || lbRsrv)
						&& (lbOldPlt || lbROP)))
				{
					if (SystemProperty.isHQ() || lbMfg || lbAssign)
					{
						getcomboRequestType().addItem(
							lsRequestType.trim());

						// Reset if select: 
						//   OldPlt & previous selected Request Type 
						//       was not Assigned
						//   ROP & previous selected Request Type 
						//       was not (Assigned || Manufacture) 
						boolean lbReset =
							(lbOldPlt
								&& !csRequestType.equals(
									SpecialPlatesConstant.ASSIGNED))
								|| (lbROP
									&& !(csRequestType
										.equals(
											SpecialPlatesConstant
												.ASSIGNED)
										|| csRequestType.equals(
											SpecialPlatesConstant
												.MANUFACTURE)));

						if (csRequestType.equals(lsRequestType)
							|| (lbAssign & lbReset))
						{
							liIndex = j;
							csRequestType = lsRequestType;
							// Reset Mfg Date if must restore 
							// to Assigned
							if (isSPREV())
							{
								if (ciOrigMfgDate != 0)
								{
									gettxtMfgDate().setDate(
										new RTSDate(
											RTSDate.YYYYMMDD,
											ciOrigMfgDate));
								}
								else
								{
									gettxtMfgDate().setText("");
								}
							}
						}
						cvRequestType.addElement(svStatus.elementAt(i));
						j = j + 1;
					}
				}
			}
			ciOrigReqSelection = liIndex;
			comboBoxHotKeyFix(getcomboRequestType());
			getcomboRequestType().setEnabled(true);
			getcomboRequestType().setSelectedIndex(liIndex);
			getcomboRequestType().addActionListener(this);
		}
		else
		{
			getcomboRequestType().addItem(csRequestType);
			getcomboRequestType().setEnabled(false);
		}
	}

	/**
	 * Populate the ResrvReasn  
	 * 
	 * @throws RTSException
	 */
	private void populateResrvReasn() throws RTSException
	{
		getcomboResrvReasnDesc().removeActionListener(this);

		int liIndex = -1;

		for (int i = 0; i < svResrvReasnDesc.size(); i++)
		{
			String lsIndiDesc = (String) svResrvReasnDesc.elementAt(i);
			String lsIndiFieldValue = lsIndiDesc.substring(50).trim();

			getcomboResrvReasnDesc().addItem(
				lsIndiDesc.substring(0, 50).trim());

			String lsResrvReasnCd =
				caSpclPltRegisData.getResrvReasnCd();

			if (lsResrvReasnCd != null
				&& lsResrvReasnCd.trim().equals(lsIndiFieldValue))
			{
				liIndex = i;

			}
		}
		comboBoxHotKeyFix(getcomboResrvReasnDesc());

		getcomboResrvReasnDesc().setSelectedIndex(liIndex);

		// Enable on the following conditions: 
		if (UtilityMethods.isSpecialPlates(csTransCd) && (isSPRSRV()))
		{
			getcomboResrvReasnDesc().setEnabled(true);
			getcomboResrvReasnDesc().addActionListener(this);
		}
		else
		{
			getcomboResrvReasnDesc().setEnabled(false);
		}
	}

	/**
	 * Release Virtual Inventory
	 */
	private void releaseVirtualInventoryIfHeld()
	{
		// defect 9285
		// Only Release if SPAPPL type trans or Replacement  
		if (caSpclPltRegisData.getVIAllocData() != null)
		{
			// Release in Replacement iff manufacturing new 
			//  InvItmNo 
			boolean lbREPLRelease =
				csTransCd.equals(TransCdConstant.REPL)
					&& !caSpclPltRegisData
						.getVIAllocData()
						.getInvItmNo()
						.equals(
						caMFVehicleData.getRegData().getRegPltNo());

			// defect 9386 
			// Add SPAPPI 
			// Release if SPAPPL or SPAPPC or SPAPPI 
			//   No VI if SPAPPO,SPAPPR 
			boolean lbSPAPPLRelease =
				csTransCd.equals(TransCdConstant.SPAPPL)
					|| csTransCd.equals(TransCdConstant.SPAPPI)
					|| csTransCd.equals(TransCdConstant.SPAPPC);
			// end defect 9386 

			if (lbREPLRelease || lbSPAPPLRelease)
			{
				if (SystemProperty.getProdStatus()
					!= SystemProperty.APP_PROD_STATUS)
				{
					System.out.println(
						"Releasing "
							+ caSpclPltRegisData
								.getVIAllocData()
								.getInvItmNo());
				}
				getController().processData(
					InventoryConstant.INV_VI_RELEASE_HOLD,
					caSpclPltRegisData.getVIAllocData());
			}
		}
		// end defect 9285 
	}

	/**
	 * Reset Set Year for Annual Plates when modify Plate Type 
	 */
	private void resetInvItmYrOnPltChng()
	{
		boolean lbAnnual = caPltTypeData.getAnnualPltIndi() == 1;
		gettxtInvItmYr().setVisible(lbAnnual);
		gettxtInvItmYr().setEnabled(
			lbAnnual && (isSPRSRV() || isSPUNAC() || isSPREV()));

		// defect 9864 
		// Modified in refactor of RegExpYr to PltExpYr  
		int liYear = caSpclPltRegisData.getPltExpYr();
		gettxtInvItmYr().setText(
			liYear == 0 ? "" : "" + caSpclPltRegisData.getPltExpYr());
		// end defect 9864 
	}

	/**
	 * Reset:
	 *  - SpecialPlatesRegisData Object values MfgStatusCd, MfgDate
	 *  - Checkboxes AddlSetIndi, RequestRenewalNotice, NewPlatesDesired 
	 * 
	 * @throws RTSException 
	 */
	private void resetOnSPREVChangeRequestType() throws RTSException
	{
		if (isSPREV())
		{
			caSpclPltRegisData.setMFGStatusCd(
				(String) SpecialPlatesConstant.STATUSCDS.get(
					csRequestType.trim()));

			// If Request type = Unacceptable or Reserved, both disabled
			// Else, reset according to Plate Type and prior selection 
			if (csRequestType
				.equals(SpecialPlatesConstant.UNACCEPTABLE)
				|| csRequestType.equals(
					SpecialPlatesConstant.FROM_RESERVE))
			{
				getchkAddlSet().setEnabled(false);
				getchkAddlSet().setSelected(
					caPltTypeData.getPltSetImprtnceCd()
						== SpecialPlatesConstant
							.PLTSETIMPRTNCECD_ADDL_SET_ONLY);
				getchkRequestRenewalNotice().setEnabled(false);
				getchkRequestRenewalNotice().setSelected(false);
			}
			else
			{
				boolean lbPreserveSettings =
					csPriorRequestType.equals(
						SpecialPlatesConstant.MANUFACTURE)
						|| csPriorRequestType.equals(
							SpecialPlatesConstant.ASSIGNED);

				boolean lbAddlSetSelected =
					getchkAddlSet().isSelected()
						|| caPltTypeData.getPltSetImprtnceCd()
							== SpecialPlatesConstant
								.PLTSETIMPRTNCECD_ADDL_SET_ONLY;

				boolean lbRNRSelected =
					getchkRequestRenewalNotice().isSelected();

				setAddlSetToDisplay();

				setRequestRenewalNoticeToDisplay();

				// Reassign to values prior to Request Type Change 
				// if prior selection was Assigned or Manufacture 
				if (lbPreserveSettings)
				{
					getchkAddlSet().setSelected(lbAddlSetSelected);
					getchkRequestRenewalNotice().setSelected(
						lbRNRSelected);
				}
			}
			// defect 9248 
			setISAToDisplay();
			// end defect 9248 

			// If Mfg, update MfgDate field 
			if (csRequestType
				.equals(SpecialPlatesConstant.MANUFACTURE))
			{
				if (isSunset())
				{
					throw new RTSException(ERRORNO_SUNSET);
				}
				else if (isExpired())
				{
					throw new RTSException(ERRORNO_RESET_ON_EXPIRED);
				}
				else
				{
					SpecialPlatesRegisData laSpclPltRegData =
						(SpecialPlatesRegisData) UtilityMethods.copy(
							caSpclPltRegisData);
					laSpclPltRegData.setMFGDate(csTransCd);
					gettxtMfgDate().setDate(
						new RTSDate(
							RTSDate.YYYYMMDD,
							laSpclPltRegData.getMFGDate()));
				}
			}
			// Else, restore MfgDate to original date
			else if (ciOrigMfgDate != 0)
			{
				gettxtMfgDate().setDate(
					new RTSDate(RTSDate.YYYYMMDD, ciOrigMfgDate));
			}
			else
			{
				gettxtMfgDate().setText("");
			}
			setNewPlatesDesiredToDisplay();
		}
	}

	/**
	 *  Restore Color of Combo Boxes in case of prior error
	 */
	private void restoreComboColor()
	{
		getcomboPlateType().setForeground(Color.black);
		getcomboPlateType().setBackground(new Color(204, 204, 204));
		getcomboOrganizationName().setForeground(Color.black);
		getcomboOrganizationName().setBackground(
			new Color(204, 204, 204));
		getcomboRequestType().setForeground(Color.black);
		getcomboRequestType().setBackground(new Color(204, 204, 204));
		// defect 10507 
		getcomboResrvReasnDesc().setForeground(Color.black);
		getcomboResrvReasnDesc().setBackground(
			new Color(204, 204, 204));
		// end defect 10507 
	}

	/** 
	 * Restore Original Plate Manufacturing Status  
	 */
	private void restoreOrigPltMfgStatus()
	{
		caSpclPltRegisData.setMFGDate(
			caOrigSpclPltRegisData.getMFGDate());
		caSpclPltRegisData.setMFGStatusCd(
			caOrigSpclPltRegisData.getMFGStatusCd());
		caSpclPltRegisData.setPltBirthDate(
			caOrigSpclPltRegisData.getPltBirthDate());
		caSpclPltRegisData.setPltApplDate(
			caOrigSpclPltRegisData.getPltApplDate());
		caSpclPltRegisData.setMfgSpclPltIndi(
			caOrigSpclPltRegisData.getMfgSpclPltIndi());
		caSpclPltRegisData.setInvItmYr(
			caOrigSpclPltRegisData.getInvItmYr());
	}

	/**
	 * Enable/Disable, select/deselect as appropriate 
	 */
	private void setAddlSetToDisplay()
	{
		if (caPltTypeData != null)
		{
			// Select if RegPltCd has not changed.
			boolean lbAddlSet =
				caSpclPltRegisData.getAddlSetIndi() == 1
					&& csOrigRegPltCd.equals(csRegPltCd);

			// defect 9261 
			// Do not enable AddlSet in RegCorrection
			// defect 9235 
			// Do not enable in SPREV if Unacceptable || Reserve 
			//  already selected.  
			boolean lbEnable =
				isSPAPPL()
					|| (isSPREV() && !(isSPUNAC() || isSPRSRV()));
			//|| isRegCorrection();
			// end defect 9235 
			// end defect 9261 

			switch (caPltTypeData.getPltSetImprtnceCd())
			{
				case SpecialPlatesConstant
					.PLTSETIMPRTNCECD_NOT_IMPORTANT :
					{
						getchkAddlSet().setEnabled(false);
						getchkAddlSet().setSelected(false);
						break;
					}
				case SpecialPlatesConstant
					.PLTSETIMPRTNCECD_FIRST_SET_ONLY :
					{
						getchkAddlSet().setEnabled(false);
						getchkAddlSet().setSelected(false);
						break;
					}
				case SpecialPlatesConstant
					.PLTSETIMPRTNCECD_ADDL_SET_ONLY :
					{
						getchkAddlSet().setEnabled(false);
						getchkAddlSet().setSelected(true);
						break;
					}
				case SpecialPlatesConstant
					.PLTSETIMPRTNCECD_EITHER_FIRST_OR_ADDL_SET :
					{
						getchkAddlSet().setEnabled(lbEnable);
						getchkAddlSet().setSelected(lbAddlSet);
						break;
					}
			}
		}
		else
		{
			getchkAddlSet().setEnabled(false);
			getchkAddlSet().setSelected(false);
		}
	}

	/**
	 * Set Application Date
	 */
	private void setApplDateToDisplay()
	{
		if (isSPAPPL())
		{
			// Application Date - Current Date
			gettxtApplDate().setDate(caToday);
			gettxtApplDate().setEnabled(false);

			// Effective Date - Current Date
			caSpclPltRegisData.setRegEffDate(caToday.getYYYYMMDDDate());
		}
		else
		{
			int liApplDate = caSpclPltRegisData.getPltApplDate();
			if (liApplDate != 0)
			{
				gettxtApplDate().setDate(
					new RTSDate(RTSDate.YYYYMMDD, liApplDate));
			}
		}
		gettxtApplDate().setEnabled(false);
	}

	/** 
	 * Set Auction Plate Info to Display 
	 *
	 */
	private void setAuctnPltInfoToDisplay()
	{

		gettxtAuctnPrice().setText(
			caSpclPltRegisData.isAuctnPlt()
				? "" + caSpclPltRegisData.getAuctnPdAmt()
				: "");

		gettxtAuctnPrice().setEnabled(false);
	}

	/**
	 * Set Charge Special Plate Fee To Display 
	 */
	private void setChrgSpclPltFeeToDisplay()
	{
		// defect 9389 / 9450 
		// Use isResetChrgFee() vs. isEnterOnSPL002()
		// No Fees if HQ || SpclPrortnIncrmnt = 0 
		if (SystemProperty.isHQ() 
			// defect 10820
			|| caPltTypeData == null
			// end defect 10820
			|| caPltTypeData.getSpclPrortnIncrmnt() == 0)
		{
			getchkChrgSpclPltFee().setEnabled(false);
			getchkChrgSpclPltFee().setSelected(false);
		}
		else if (isSPAPPL() || isSPRNW())
		{
			getchkChrgSpclPltFee().setEnabled(true);
			getchkChrgSpclPltFee().setSelected(true);
		}
		else if (
			csTransCd.equals(TransCdConstant.RENEW)
				|| csTransCd.equals(TransCdConstant.TITLE)
				|| csTransCd.equals(TransCdConstant.NONTTL)
				|| csTransCd.equals(TransCdConstant.EXCH)
				|| csTransCd.equals(TransCdConstant.REJCOR))
		{
			getchkChrgSpclPltFee().setEnabled(true);
			getchkChrgSpclPltFee().setSelected(
				!caSpclPltRegisData.isResetChrgFee());
		}
		else
		{
			getchkChrgSpclPltFee().setEnabled(false);
			getchkChrgSpclPltFee().setSelected(false);
		}
		// end defect 9389 /9450 
	}

	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject instanceof Vector)
		{
			Vector lvIsNextVCREG029 = (Vector) aaDataObject;
			if (lvIsNextVCREG029 != null)
			{
				if (lvIsNextVCREG029.size() == 2)
				{
					// determine next vc if NOT reg029
					// first element is flag whether to go to REG029
					if (lvIsNextVCREG029.get(0) instanceof Boolean)
					{
						getController().processData(
							VCSpecialPlateInformationSPL002
								.REDIRECT_IS_NEXT_VC_REG029,
							lvIsNextVCREG029);
					}
					else if (lvIsNextVCREG029.get(0) instanceof String)
					{
						getController().processData(
							VCSpecialPlateInformationSPL002
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
				}
			}
		}
		else if (
			aaDataObject != null
				&& aaDataObject instanceof VehicleInquiryData)
		{
			csTransCd = getController().getTransCode();
			csOrigTransCd = csTransCd;
			// Special Processing for SPRSRV && SPUNAC 
			if (!cbLabelSet)
			{
				if (isSPAPPL())
				{
					getstcLblStatus().setVisible(false);
					getstcLblRequestType().setVisible(true);
				}
				else
				{
					getstcLblStatus().setVisible(true);
					getstcLblRequestType().setVisible(false);
				}
				cbLabelSet = true;
			}

			try
			{
				caVehInqData = (VehicleInquiryData) aaDataObject;
				if (caVehInqData.getMfVehicleData() != null)
				{
					caMFVehicleData =
						(MFVehicleData) caVehInqData.getMfVehicleData();
					if (UtilityMethods.isSpecialPlates(csTransCd))
					{
						caMFVehicleData.setRegData(
							new RegistrationData());
					}
					caSpclPltRegisData =
						caMFVehicleData.getSpclPltRegisData();

					if (caSpclPltRegisData != null)
					{
						caSpclPltRegisData.initWhereNull();
						caOwnerData = caSpclPltRegisData.getOwnrData();

						// defect 10112 
						caOwnerAddressData =
							caOwnerData.getAddressData();
						// end defect 10112

						gettxtPlateNo().setText(
							caSpclPltRegisData.getRegPltNo().trim());
						gettxtMfgPlateNo().setText(
							caSpclPltRegisData.getMfgPltNo());
						if (isSPAPPL())
						{
							csRequestType =
								caSpclPltRegisData
									.getRequestType()
									.trim();
							caSpclPltRegisData.setMFGStatusCd();
						}
						else
						{
							csRequestType =
								(
									String) SpecialPlatesConstant
										.INTERPRET_STATUSCDS
										.get(
									caSpclPltRegisData
										.getMFGStatusCd());
							if (csRequestType == null)
							{
								csRequestType = new String();
							}
							csOrigRequestType = csRequestType.trim();
						}
						csRegPltCd =
							caSpclPltRegisData.getRegPltCd().trim();
						csOrgNo = caSpclPltRegisData.getOrgNo().trim();
						csMfgPltNo = caSpclPltRegisData.getMfgPltNo();

						// defect 9248
						csOrigRegPltCd = csRegPltCd;
						csOrigMfgPltNo = csMfgPltNo;
						cbOrigISA =
							caSpclPltRegisData.getISAIndi() == 1;
						// end defect 9248 
						csOrigMfgStatusCd =
							caSpclPltRegisData.getMFGStatusCd();
						ciOrigMfgDate = caSpclPltRegisData.getMFGDate();
						// defect 9462 
						caOrigSpclPltRegisData =
							(
								SpecialPlatesRegisData) UtilityMethods
									.copy(
								caSpclPltRegisData);
						// end defect 9462 
						if (!csRegPltCd.equals(""))
						{
							caPltTypeData =
								PlateTypeCache.getPlateType(csRegPltCd);
						}
						// Always Disabled 
						gettxtPlateNo().setEnabled(false);
						gettxtMfgPlateNo().setEnabled(false);
						setPlateDataToDisplay();
						setHQOnlyFieldsToDisplay();
						setChrgSpclPltFeeToDisplay();
						// defect 9689
						// Handle special enable\disable rules for
						// Vendor Plates  
						setVendorPltToDisplay();
						// end defect 9689

						// defect 10592 
						if (isSPAPPL())
						{
							Object laSPL002 =
								getController()
									.getMediator()
									.openVault(
									ScreenConstant.SPL002);

							if (laSPL002 != null
								&& laSPL002
									instanceof ScreenSPL002SavedData)
							{
								ScreenSPL002SavedData laSPL002Data =
									(ScreenSPL002SavedData) laSPL002;

								setSavedDataToDisplay(laSPL002Data);
							}
						}
						// end defect 10592 

						// defect 10820
						// special plates inquiry - temporary trans code 
						if (csTransCd.equals(TransCdConstant.SPINQ))
						{
							// disable all inputs for inquiry only
							UtilityMethods.disableAllFields(this);						}
						// end defect 10820
					}
				}
			}
			catch (RTSException aeRTSEx)
			{
				aeRTSEx.displayError(this);
			}

		}
	}

	/**
	 * Set Screen Contents to Data Object 
	 */
	private void setDataToDataObject()
	{
		caSpclPltRegisData.setRequestType(csRequestType);

		if (UtilityMethods.isSpecialPlates(csTransCd))
		{
			caMFVehicleData.setOwnerData(caOwnerData);
		}
		// Data Object does not change for SPDEL 
		if (!isSPDEL())
		{
			// If Revise or RSRV, then assign csRegPltCd, csOrgNo
			if (isSPRSRV() || isSPREV() || isSPUNAC())
			{
				if (getcomboPlateType().getSelectedIndex() >= 0)
				{
					caSpclPltRegisData.setRegPltCd(csRegPltCd);
					caSpclPltRegisData.setOrgNo(csOrgNo);
				}
			}

			if (!isSPRSRV() && !isSPUNAC())
			{
				caSpclPltRegisData.setAddlSetIndi(
					getchkAddlSet().isSelected() ? 1 : 0);
				caSpclPltRegisData.setMfgPltNo(
					gettxtMfgPlateNo().getText());
				caSpclPltRegisData.setRegPltAge(
					Integer.parseInt(gettxtPlateAge().getText()));

				// defect 9248 
				if (isSPREV())
				{
					caSpclPltRegisData.setISAIndi(
						getchkISA().isSelected() ? 1 : 0);
				}
				// end defect 9248 

				// Date fields 
				// Application Date
				if (gettxtApplDate().getDate() == null
					|| gettxtApplDate().getText().length() == 0)
				{
					caSpclPltRegisData.setPltApplDate(0);
				}
				else
				{
					caSpclPltRegisData.setPltApplDate(
						gettxtApplDate().getDate().getYYYYMMDDDate());
				}
				// Setup Mfg Request  
				if (getchkNewPlatesDesired().isSelected())
				{
					caSpclPltRegisData.setPltBirthDate(
						new RTSDate().getYYYYMMDDDate() / 100);
					if (!isSPAPPL())
					{
						setupMfgRequestForNewPlateDesired();
					}
				}
				// In case cancel off PMT004 and deselect NPD
				// defect 9462   
				else
				{
					restoreOrigPltMfgStatus();
				}
				// end defect 9462 

				// defect 9864 
				// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  

				// defect 10592 
				// Check for Empty 
				// Plt Exp Mo 
				if (!gettxtExpMo().isEmpty())
				{
					caSpclPltRegisData.setPltExpMo(
						Integer.parseInt(gettxtExpMo().getText()));
				}
				if (!gettxtExpYr().isEmpty())
				{
					// Plt Exp Yr
					caSpclPltRegisData.setPltExpYr(
						Integer.parseInt(gettxtExpYr().getText()));
				}
				// end defect 9864 

				if (!gettxtResComptCntyNo().isEmpty())
				{
					// ResComptCntyNo 
					if (!SystemProperty.isCounty())
					{
						caSpclPltRegisData.setResComptCntyNo(
							Integer.parseInt(
								gettxtResComptCntyNo().getText()));
					}
					else
					{
						caSpclPltRegisData.setResComptCntyNo(
							SystemProperty.getOfficeIssuanceNo());
					}
				}
				// end defect 10592  

				// Charge Fee Indicator 
				// defect 9389 / 9450 
				if (csTransCd.equals(TransCdConstant.RENEW)
					|| csTransCd.equals(TransCdConstant.TITLE)
					|| csTransCd.equals(TransCdConstant.NONTTL)
					|| csTransCd.equals(TransCdConstant.EXCH)
					|| csTransCd.equals(TransCdConstant.REJCOR))
				{
					caSpclPltRegisData.setResetChrgFee(
						!getchkChrgSpclPltFee().isSelected());
				}
				caSpclPltRegisData.setSpclPltChrgFeeIndi(
					getchkChrgSpclPltFee().isSelected() ? 1 : 0);
				// end defect 9389 / 9450

				caSpclPltRegisData.setTransCd(csTransCd);

				// defect 10441 
				if (!gettxtAuctnPrice().isEmpty())
				{
					String lsAuctnPrice = gettxtAuctnPrice().getText();
					caSpclPltRegisData.setAuctnPdAmt(
						new Dollar(lsAuctnPrice));
					caSpclPltRegisData.setAuctnPltIndi(1);
				}
				else
				{
					caSpclPltRegisData.setAuctnPdAmt(new Dollar("0"));
					caSpclPltRegisData.setAuctnPltIndi(0);
				}
				// end defect 10441  

				// for SPRNR
				getController().setTransCode(csTransCd);
			}

			if (SystemProperty.isHQ())
			{
				caSpclPltRegisData.setPltOwnrDist(
					gettxtPltOwnrDist().getText());
				caSpclPltRegisData.setPltOwnrOfcCd(
					gettxtPltOwnrOfcCd().getText());
				caSpclPltRegisData.setSpclRemks(
					gettxtSpclRemarks().getText());
				// defect 10366 
				//	caSpclPltRegisData.setSpclRegStkrNo(
				//		gettxtSpclRegStkrNo().getText());
				// end defect 10366

				// defect 10441  
				if (getcomboResrvReasnDesc().isEnabled())
				{
					caSpclPltRegisData.setResrvEffDate(
						gettxtResrvDate().getDate().getYYYYMMDDDate());
					caSpclPltRegisData.setResrvReasnCd(
						getResrvReasnCdFromDropDown());
				}

				if (gettxtVendorTransDate().isValidDate())
				{
					caSpclPltRegisData.setVendorTransDate(
						gettxtVendorTransDate()
							.getDate()
							.getYYYYMMDDDate());
				}

				caSpclPltRegisData.setFINDocNo(
					gettxtFINDocNo().getText());

				caSpclPltRegisData.setMktngAllowdIndi(
					getchkMktngAllowd().isSelected() ? 1 : 0);
				// end defect 10441 
			}
			// Owner Id
			caOwnerData.setOwnrId(gettxtOwnerId().getText());

			// Owner Data
			// defect 10127 
			caNameAddrComp.setNameAddressToDataObject(caOwnerData);
			// end defect 10127 

			caSpclPltRegisData.setPltOwnrEMail(gettxtEMail().getText());
			
			// defect 10592 
			caSpclPltRegisData.setPltOwnrPhoneNo(
				//gettxtPhoneNo().getPhoneNo());
				gettxtPhoneNo().getPhoneText());
			// end defect 10592 
			
			// defect 9145
			// Handle PLPDLR
			caSpclPltRegisData.setPltOwnrDlrGDN(
				gettxtDealerNo().getText());
			// end defect 9145

			// defect 10507 
			int liElectionPndngIndi = 0;
			if (getchkElectionPndng().isVisible()
				&& getchkElectionPndng().isSelected())
			{
				liElectionPndngIndi = 1;
			}
			caSpclPltRegisData.setElectionPndngIndi(
				liElectionPndngIndi);
			// end defect 10507 

			// defect 10592
			if (isSPAPPL())
			{
				ScreenSPL002SavedData laSPL002Data =
					new ScreenSPL002SavedData();

				laSPL002Data.setSpclPltRegisData(
					(SpecialPlatesRegisData) UtilityMethods.copy(
						caSpclPltRegisData));

				getController().getMediator().closeVault(
					ScreenConstant.SPL002,
					UtilityMethods.copy(laSPL002Data));
			}
			// end defect 10592 
		}
	}

	/**
	 * Set DealerNo to Display 
	 */
	private void setDealerNoToDisplay()
	{
		boolean lbDealerPlt = isDlrPlt();

		boolean lbEnable =
			lbDealerPlt && (isSPREV() || isSPAPPL() || isSPRSRV());

		getstcLblDealerNo().setEnabled(lbDealerPlt);
		gettxtDealerNo().setEnabled(lbEnable);

		if (lbDealerPlt)
		{
			// If Enabled and Zero Length, Set to Prefix
			if (lbEnable
				&& (caSpclPltRegisData.getPltOwnrDlrGDN() == null
					|| caSpclPltRegisData
						.getPltOwnrDlrGDN()
						.trim()
						.length()
						== 0))
			{
				// defect 9499 
				// DealerNo not required to begin w/ "P"
				// defect 9145 
				//gettxtDealerNo().setText(DEALER_NO_PREFIX);
				gettxtDealerNo().setText(
					CommonConstant.STR_SPACE_EMPTY);
				// end defect 9145
				// end defect 9499 
			}
			else
			{
				// defect 9557
				// Reference the 10 digit DlrGDN
				gettxtDealerNo().setText(
					caSpclPltRegisData.getPltOwnrDlrGDN());
				// defect 9557
			}
		}
		else
		{
			gettxtDealerNo().setText(CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * Set checkbox ElectionPending To Display 
	 */
	private void setElectionPndngToDisplay()
	{
		boolean lbHQ = SystemProperty.isHQ();

		if (caPltTypeData != null)
		{
			boolean lbVisible =
				caPltTypeData.getPltOwnrshpCd().equals(
					SpecialPlatesConstant.ENTITLED_OWNER);

			getchkElectionPndng().setVisible(lbVisible);

			getchkElectionPndng().setSelected(
				lbVisible && caSpclPltRegisData.isElectionPndng());

			getchkElectionPndng().setEnabled(
				lbVisible
					&& lbHQ
					&& UtilityMethods.isSpecialPlates(csTransCd));
		}
	}

	/**
	 * Set Expiration Month / Year on Screen. Enable/disable as appropriate.
	 */
	private void setExpMoYrToDisplay()
	{
		// defect 9864 
		// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr, 
		// OrigRegExpMo/Yr to OrigPltExpMo/Yr

		// If not initialized; set OrigRegExpMo/Yr
		// Saved for later comparison in SPREV, CORREG
		if (caSpclPltRegisData.getOrigPltExpMo() < 0)
		{
			caSpclPltRegisData.setOrigPltExpMo(
				caSpclPltRegisData.getPltExpMo());
			caSpclPltRegisData.setOrigPltExpYr(
				caSpclPltRegisData.getPltExpYr());
		}

		boolean lbEnableYr = false;
		boolean lbAnnual = false;
		if (caPltTypeData != null)
		{
			lbAnnual = caPltTypeData.getAnnualPltIndi() == 1;
		}
		// Special Plates Renew or Application

		if (isSPRNW() || isSPAPPL())
		{
			// defect 9247
			// Exp Mo/Yr should not be populated for DlrPltxx in SPRNW  
			if (isSPRNW() && isDlrPlt())
			{
				caSpclPltRegisData.setPltExpMo(0);
				caSpclPltRegisData.setPltExpYr(0);
			}
			// end defect 9247 
			int liNoMonths = calculateExpMoYr();

			// defect 10507 
			// Add: Enable Year if MultiYrOfclPlt & Special Plate Application
			// and <= 3 months 
			if ((caPltTypeData.isMultiYrOfclPlt()
				&& isSPAPPL()
				&& liNoMonths <= 3)
				|| (lbAnnual && liNoMonths < 3 && isSPRNW()))
			{
				// end defect 10507 
				lbEnableYr = true;
			}
		}
		gettxtExpMo().setText(
			UtilityMethods.isAllZeros(
				"" + caSpclPltRegisData.getPltExpMo())
				? ""
				: "" + caSpclPltRegisData.getPltExpMo());
		gettxtExpYr().setText(
			UtilityMethods.isAllZeros(
				"" + caSpclPltRegisData.getPltExpYr())
				? ""
				: "" + caSpclPltRegisData.getPltExpYr());

		// Enable for: 
		// - SPREV
		// - isDlrPlt() && (SPAPPL || SPRNW)
		// - SPAPPL && (isDlrPlt() || CUSTOMER SUPPLIED)
		// defect 9261
		// Do not enable for RegCorrection
		// defect 9247 
		// Exp Mo/Yr should be enabled for PLPDLRxx in SPRNW
		boolean lbEnableMo =
			//RegCorrection()|| 
	isSPREV()
		|| (isDlrPlt() && (isSPAPPL() || isSPRNW()))
		|| (isSPAPPL()
			&& !lbAnnual
			&& (isDlrPlt()
				|| csRequestType.equals(
					SpecialPlatesConstant.CUSTOMER_SUPPLIED)));
		// end defect 9247
		// end defect 9261 

		gettxtExpMo().setEnabled(lbEnableMo);

		// Enable Year if lbEnableMo or lbEnableYear 
		gettxtExpYr().setEnabled((lbEnableMo || lbEnableYr));

		if (isSPRNW())
		{
			getstcLblNew().setText(
				"(Previous: "
					+ caSpclPltRegisData.getOrigPltExpMo()
					+ "/"
					+ caSpclPltRegisData.getOrigPltExpYr()
					+ ")");
		}
		getstcLblNew().setVisible(isSPRNW());
		// end defect 9864 
	}

	/**
	 *   Handle HQ Only Fields 
	 */
	private void setHQOnlyFieldsToDisplay()
	{
		boolean lbHQ = SystemProperty.isHQ();
		boolean lbEnable =
			lbHQ
				&& !(isSPDEL()
					|| csTransCd.equals(TransCdConstant.SPUNAC));

		// Replace fields of all zeros w/ empty string 
		if (lbHQ)
		{
			// Special Remarks 
			String lsRemarks = caSpclPltRegisData.getSpclRemks();
			if (csTransCd.equals(TransCdConstant.SPUNAC)
				|| csTransCd.equals(TransCdConstant.SPRSRV))
			{
				SecurityData laScrtyData =
					getController()
						.getMediator()
						.getDesktop()
						.getSecurityData();

				lsRemarks =
					csTransCd
						+ " - "
						+ laScrtyData.getEmpId()
						+ " - "
						+ new RTSDate().toString();
			}
			gettxtSpclRemarks().setText(
				UtilityMethods.isAllZeros(lsRemarks) ? "" : lsRemarks);

			// Plate Owner OfcCd 
			// Do not remove zeros; "00" is Valid here.
			gettxtPltOwnrOfcCd().setText(
				caSpclPltRegisData.getPltOwnrOfcCd().trim());

			// Plate Owner OwnrDist 
			gettxtPltOwnrDist().setText(
				UtilityMethods.isAllZeros(
					caSpclPltRegisData.getPltOwnrDist())
					? ""
					: caSpclPltRegisData.getPltOwnrDist().trim());

			// defect 10366 
			//	// Sticker No
			// gettxtSpclRegStkrNo().setText(
			//	UtilityMethods.isAllZeros(
			//		caSpclPltRegisData.getSpclRegStkrNo())
			//		? ""
			//		: caSpclPltRegisData.getSpclRegStkrNo().trim());
			// end defect 10366 
			getchkMktngAllowd().setSelected(
				caSpclPltRegisData.isMktngAllowd());

			int liVndrTransDate =
				caSpclPltRegisData.getVendorTransDate();

			if (liVndrTransDate != 0)
			{
				gettxtVendorTransDate().setDate(
					new RTSDate(RTSDate.YYYYMMDD, liVndrTransDate));
			}

			if (csTransCd.equals(TransCdConstant.SPRSRV))
			{
				gettxtResrvDate().setDate(new RTSDate());
			}
			else
			{
				int liResrvDate = caSpclPltRegisData.getResrvEffDate();
				if (liResrvDate != 0)
				{
					gettxtResrvDate().setDate(
						new RTSDate(RTSDate.YYYYMMDD, liResrvDate));
				}
			}
			if (caSpclPltRegisData.getFINDocNo() != null)
			{
				gettxtFINDocNo().setText(
					caSpclPltRegisData.getFINDocNo());
			}
		}

		// PltOwnrOfcCd 
		getstcLblPltOwnrOfcCd().setEnabled(lbHQ);
		getstcLblPltOwnrOfcCd().setVisible(lbHQ);
		gettxtPltOwnrOfcCd().setEnabled(lbEnable);
		gettxtPltOwnrOfcCd().setVisible(lbHQ);

		// PltOwnrDist 
		getstcLblPltOwnrDist().setEnabled(lbHQ);
		getstcLblPltOwnrDist().setVisible(lbHQ);
		gettxtPltOwnrDist().setEnabled(lbEnable);
		gettxtPltOwnrDist().setVisible(lbHQ);

		// defect 10366 
		// // SpclRegStkrNo
		// getstcLblSpclRegStkrNo().setEnabled(lbHQ);
		// getstcLblSpclRegStkrNo().setVisible(lbHQ);
		// gettxtSpclRegStkrNo().setEnabled(lbEnable);
		// gettxtSpclRegStkrNo().setVisible(lbHQ);
		// end defect 10366 

		// SpclRemarks
		getstcLblSpclRemarks().setEnabled(lbHQ);
		getstcLblSpclRemarks().setVisible(lbHQ);
		gettxtSpclRemarks().setEnabled(
			lbEnable || csTransCd.equals(TransCdConstant.SPUNAC));
		gettxtSpclRemarks().setVisible(lbHQ);

		// defect 10441 
		// Marketing Allowed 
		getchkMktngAllowd().setEnabled(false);
		getchkMktngAllowd().setVisible(lbHQ);

		// Vendor TransDate
		getstcLblVndrTransDate().setVisible(lbHQ);
		gettxtVendorTransDate().setVisible(lbHQ);
		gettxtVendorTransDate().setEnabled(false);

		// Fin Doc No 
		getstcLblFINDocNo().setVisible(lbHQ);
		gettxtFINDocNo().setVisible(lbHQ);
		gettxtFINDocNo().setEnabled(false);

		// Reserve Eff Date 
		lbEnable = isSPRSRV() && !isSPREV();
		getstcLblResrvDate().setVisible(lbHQ);
		gettxtResrvDate().setVisible(lbHQ);
		gettxtResrvDate().setEnabled(false);
		getcomboResrvReasnDesc().setVisible(lbHQ);
		getcomboResrvReasnDesc().setEnabled(lbHQ && lbEnable);
		// end defect 10441 
	}

	/**
	 * Set InvItmYr To Display 
	 */
	private void setInvItmYrToDisplay()
	{

		// InvItmYr will be preset from SPL001 for most Application
		if (caPltTypeData != null
			&& caPltTypeData.getAnnualPltIndi() == 1
			&& ((!isSPAPPL())
				|| csTransCd.equals(TransCdConstant.SPAPPO)))
		{
			// defect 9864 
			// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
			caSpclPltRegisData.setInvItmYr(
				caSpclPltRegisData.getPltExpYr());
			// end defecct 9864 
		}
		int liInvItmYr = caSpclPltRegisData.getInvItmYr();
		if (liInvItmYr != 0 && caPltTypeData.getAnnualPltIndi() == 1)
		{
			ivjtxtInvItmYr.setVisible(true);
			ivjtxtInvItmYr.setText(new Integer(liInvItmYr).toString());
			// defect 9261 
			// do not enable in RegCorrection 
			gettxtInvItmYr().setEnabled(
			//RegCorrection() ||
			isSPUNAC() || isSPRSRV() || isSPREV());
			// end defect 9261 
		}
		else
		{
			ivjtxtInvItmYr.setVisible(false);
			ivjtxtInvItmYr.setEnabled(false);
		}
	}

	/**
	 * Enable/Disable, select/ deselect as appropriate
	 */
	private void setISAToDisplay()
	{
		boolean lbSetToInit = true;
		if (caPltTypeData != null)
		{
			String lsRequestType =
				(String) getcomboRequestType().getSelectedItem();

			if (isSPREV()
				&& caPltTypeData.getUserPltNoIndi() == 1
				&& csOrigRegPltCd.equals(caPltTypeData.getRegPltCd()))
			{
				boolean lbAssgnMfg =
					(lsRequestType
						.equals(SpecialPlatesConstant.ASSIGNED)
						|| lsRequestType.equals(
							SpecialPlatesConstant.MANUFACTURE));

				if ((caPltTypeData
					.getISAAllowdCd()
					.equals(SpecialPlatesConstant.POS_EVENTS)
					|| (caPltTypeData
						.getISAAllowdCd()
						.equals(
							SpecialPlatesConstant
								.BOTH_POS_AND_ITRNT_EVENTS))))
				{
					if (isSPREV() && lbAssgnMfg)
					{
						if (caSpclPltRegisData
							.getRegPltNo()
							.trim()
							.length()
							+ 2
							<= caPltTypeData.getMaxByteCount())
						{
							lbSetToInit = false;

							// defect 9389
							// Do not allow reset of ISA status 
							//  for PLPDPPBP  
							getchkISA().setEnabled(
								!csRegPltCd.equals(
									SpecialPlatesConstant
										.PER_DP_PRIVATE_BUS_PLT));
							// end defect 9389 
						}
					}
					if (cbInitPass)
					{
						getchkISA().setSelected(
							caSpclPltRegisData.getISAIndi() == 1);
						cbInitPass = false;
					}
				}
			}
		}
		if (lbSetToInit)
		{
			getchkISA().setSelected(cbOrigISA);
			getchkISA().setEnabled(false);
			gettxtMfgPlateNo().setText(csOrigMfgPltNo);
			gettxtMfgPlateNo().setEnabled(false);
		}
	}

	/**
	 * Set MfgDate
	 */
	private void setMfgDateToDisplay()
	{
		// MFGDate 
		if (caSpclPltRegisData.getMFGDate() != 0)
		{
			gettxtMfgDate().setDate(
				new RTSDate(
					RTSDate.YYYYMMDD,
					caSpclPltRegisData.getMFGDate()));
		}
		gettxtMfgDate().setEnabled(false);
	}

	/**
	 * Make Visible New Plates Desired if Special Plates Renewal
	 */
	private void setNewPlatesDesiredToDisplay()
	{
		getchkNewPlatesDesired().setEnabled(false);
		getchkNewPlatesDesired().setSelected(false);
		if (caPltTypeData != null
			&& (caPltTypeData.getNeedsProgramCd().equals("D")
				|| caPltTypeData.getNeedsProgramCd().equals("R")
				|| caPltTypeData.getAnnualPltIndi() == 1))
		{
			getchkNewPlatesDesired().setVisible(
				UtilityMethods.isSpecialPlates(csTransCd));

			// SPRNW 	
			if (isSPRNW())
			{
				if (caPltTypeData.getAnnualPltIndi() == 1)
				{
					getchkNewPlatesDesired().setSelected(true);
				}
				else
				{
					boolean lbSunsetted =
						OrganizationNumberCache.isSunsetted(
							csRegPltCd,
							csOrgNo);

					// Cannot remanufacture Sunsetted							
					if (!lbSunsetted
						&& caSpclPltRegisData.getRegPltAge(true)
							>= caPltTypeData.getOptPltReplAge())
					{
						boolean lbReqd =
							caSpclPltRegisData.getRegPltAge(true)
								>= caPltTypeData.getMandPltReplAge();
						getchkNewPlatesDesired().setEnabled(!lbReqd);
						getchkNewPlatesDesired().setSelected(lbReqd);
					}
				}
			}
			// Mark as selected if SPAPPL, SPAPPI, SPAPPR 
			//  || (SPREV && Manufacture) 
			else if (isSPAPPL() || isSPREV())
			{
				getchkNewPlatesDesired().setSelected(
					csTransCd.equals(TransCdConstant.SPAPPI)
						|| csTransCd.equals(TransCdConstant.SPAPPR)
						|| csRequestType.equals(
							SpecialPlatesConstant.MANUFACTURE));
			}
		}
	}

	/**
	 * Get data from OwnerData to display.
	 */
	private void setOwnrInfoToDisplay()
	{
		// Set OwnrId; if all "0" from MF, replace with empty string.
		String lsOwnrId = caOwnerData.getOwnrId();
		boolean lbAllZeros = UtilityMethods.isAllZeros(lsOwnrId);
		gettxtOwnerId().setText(lbAllZeros ? "" : lsOwnrId.trim());

		// defect 10127 
		caNameAddrComp.setNameAddressDataToDisplay(caOwnerData);
		// end defect 10127  

		gettxtEMail().setText(
			caSpclPltRegisData.getPltOwnrEMail().trim());

		// Set PhoneNo; If all "0" from MF, replace with empty string. 
		String lsTemp = caSpclPltRegisData.getPltOwnrPhoneNo();
		lbAllZeros = UtilityMethods.isAllZeros(lsTemp);
		gettxtPhoneNo().setPhoneNo(lbAllZeros ? "" : lsTemp);

		// defect 10130 
		// Enable only for HQ 
		// Enable OwnerId in SPAPPL, SPRSRV, SPREV && Unacceptable Status
		// defect 9261 
		// Do not enable in Reg Correction 
		boolean lbEnableOwnrId =
			SystemProperty.isHQ()
				&& (isSPAPPL()
					|| isSPRSRV()
					|| (isSPREV() && (isSPUNAC())));

		gettxtOwnerId().setEnabled(lbEnableOwnrId);
		getstcLblOwnerId().setEnabled(lbEnableOwnrId);
		// end defect 9261 
		// end defect 10130 

		// Enable in all Special Plates Events but SPDEL || SPUNAC		
		boolean lbEnable =
			UtilityMethods.isSpecialPlates(csTransCd)
				&& !isSPDEL()
				&& !csTransCd.equals(TransCdConstant.SPUNAC);

		// Enable Owner Name as appropriate
		boolean lbEnableOwnrName =
			lbEnable
				&& !isSPRNW()
				&& !(isSPREV() && !(isSPUNAC() || isSPRSRV()));
		gettxtOwnerName1().setEnabled(lbEnableOwnrName);
		gettxtOwnerName2().setEnabled(lbEnableOwnrName);

		getchkUSA().setEnabled(lbEnable);
		gettxtOwnerStreet1().setEnabled(lbEnable);
		gettxtOwnerStreet2().setEnabled(lbEnable);
		gettxtOwnerCity().setEnabled(lbEnable);
		gettxtOwnerState().setEnabled(
			gettxtOwnerState().isVisible() && lbEnable);
		gettxtOwnerZpcd().setEnabled(
			gettxtOwnerZpcd().isVisible() && lbEnable);
		gettxtOwnerZpcdP4().setEnabled(
			gettxtOwnerZpcdP4().isVisible() && lbEnable);
		gettxtOwnerCntry().setEnabled(
			gettxtOwnerCntry().isVisible() && lbEnable);
		gettxtOwnerCntryZpcd().setEnabled(
			gettxtOwnerCntryZpcd().isVisible() && lbEnable);
		gettxtPhoneNo().setEnabled(
			!isSPDEL() && !csTransCd.equals(TransCdConstant.SPUNAC));
		gettxtEMail().setEnabled(
			!isSPDEL() && !csTransCd.equals(TransCdConstant.SPUNAC));
		cbSetDataFinished = true;
	}

	/**
	 * Set Plate Age
	 */
	private void setPlateAgeToDisplay()
	{
		// Note: Plate Age is only for Display 
		// MF maintains PltBirthDate
		if (!(csTransCd.equals(TransCdConstant.SPRSRV)
			|| csTransCd.equals(TransCdConstant.SPUNAC)))
		{
			// Display "0" if SP Application and not Plt Owner Change 
			// or MfgScplPltIndi = 1 
			if ((isSPAPPL()
				&& !(csRequestType
					.equals(SpecialPlatesConstant.PLATE_OWNER_CHANGE)))
				|| caSpclPltRegisData.getMfgSpclPltIndi() == 1)
			{
				gettxtPlateAge().setText("0");
			}
			else
			{
				boolean lbRenewal =
					csTransCd.equals(TransCdConstant.RENEW)
						|| isSPRNW();

				gettxtPlateAge().setText(
					"" + caSpclPltRegisData.getRegPltAge(lbRenewal));
			}
		}
		gettxtPlateAge().setEnabled(false);
	}

	/**
	 * Set values from Special Plate to display  
	 * 
	 * @throws RTSException
	 */
	private void setPlateDataToDisplay() throws RTSException
	{
		// defect 11052 
		if (csTransCd.equals(TransCdConstant.SPINQ) && caVehInqData.getNoMFRecs()==0)
		{
			gettxtPlateNo().setText(caSpclPltRegisData.getRegPltNo());
			setOwnrInfoToDisplay();
			gettxtOwnerState().setText(new String()); 
		}
		else
		{
			// end defect 11052 
			
			setAddlSetToDisplay();
			setApplDateToDisplay();
			setDealerNoToDisplay();
			setInvItmYrToDisplay();
			setMfgDateToDisplay();
			setResComptCntyNoToDisplay();
			setPlateAgeToDisplay();
			setExpMoYrToDisplay();
			setNewPlatesDesiredToDisplay();
			setRequestRenewalNoticeToDisplay();
			populatePlateType();
			setOwnrInfoToDisplay();
			// defect 9248 
			setISAToDisplay();
			// end defect 9248 
			// defect 10441
			setTermToDisplay();
			setAuctnPltInfoToDisplay();
			populateResrvReasn();
			// end defect 10441 
			// defect 10507
			setElectionPndngToDisplay();
			// end defect 10507
			
		// defect 11052 
		}
		// end defect 11052 
		
	}

	/**
	 * Set Renewal Notice
	 */
	private void setRequestRenewalNoticeToDisplay()
	{
		// 	SPREV 
		//  && Renewals Printed
		//  && csRequestType MANUFACTURE || ASSIGNED  
		boolean lbRenewalsPrinted =
			caPltTypeData != null && caPltTypeData.getRegRenwlCd() == 1;
		boolean lbEnable =
			isSPREV()
				&& lbRenewalsPrinted
				&& csRequestType != null
				&& (csRequestType.equals(SpecialPlatesConstant.ASSIGNED)
					|| csRequestType.equals(
						SpecialPlatesConstant.MANUFACTURE));
		getchkRequestRenewalNotice().setEnabled(lbEnable);
		getchkRequestRenewalNotice().setSelected(false);

		// defect 9235 
		// Keep visible if SPREV
		getchkRequestRenewalNotice().setVisible(isSPREV());
		// end defect 9235 
	}

	/**
	 * Set ResComptCntyNo
	 */
	private void setResComptCntyNoToDisplay()
	{
		//	ResComptCntyNo will be 0 for SPAPPL 
		int liResComptCntyNo = caSpclPltRegisData.getResComptCntyNo();
		if (SystemProperty.isCounty() && (isSPAPPL()))
		{
			liResComptCntyNo = SystemProperty.getOfficeIssuanceNo();
		}
		gettxtResComptCntyNo().setText(
			liResComptCntyNo == 0 ? "" : "" + liResComptCntyNo);

		// ResComptCntyNo enabled 
		//  iff !Cnty && !SPDEL && (SPAPPL || SPREV || SPRNW || SPRSRV)
		gettxtResComptCntyNo().setEnabled(
			!SystemProperty.isCounty()
				&& !isSPDEL()
				&& (isSPAPPL() || isSPREV() || isSPRNW() || isSPRSRV()));
	}

	/** 
	 * Set Term To Display 
	 *
	 */
	private void setTermToDisplay()
	{
		gettxtTerm().setEnabled(false);
		gettxtTerm().setText(
			"" + caSpclPltRegisData.getPltValidityTerm());
	}

	/**
	 * Set Saved Data to Display
	 *
	 * @param aaSPL002Data 
	 */
	private void setSavedDataToDisplay(ScreenSPL002SavedData aaSPL002Data)
	{
		SpecialPlatesRegisData laSpclPltRegisData =
			aaSPL002Data.getSpclPltRegisData();

		OwnerData laOwnrData = laSpclPltRegisData.getOwnrData();

		if (laOwnrData != null)
		{
			String lsOwnrId = laOwnrData.getOwnrId();
			boolean lbAllZeros = UtilityMethods.isAllZeros(lsOwnrId);
			gettxtOwnerId().setText(lbAllZeros ? "" : lsOwnrId.trim());

			caNameAddrComp.setNameAddressDataToDisplay(laOwnrData);

			gettxtEMail().setText(
				caSpclPltRegisData.getPltOwnrEMail().trim());

			String lsTemp = laSpclPltRegisData.getPltOwnrPhoneNo();
			lbAllZeros = UtilityMethods.isAllZeros(lsTemp);
			gettxtPhoneNo().setPhoneNo(lbAllZeros ? "" : lsTemp);
		}

		// Exp Mo/Yr enabled for Dealer Plates 
		if (gettxtExpMo().isEnabled()
			&& caSpclPltRegisData.getRegPltCd().equals(
				laSpclPltRegisData.getRegPltCd()))
		{
			int liExpMo = laSpclPltRegisData.getPltExpMo();
			if (liExpMo != 0)
			{
				gettxtExpMo().setText("" + liExpMo);
			}
		}
		if (gettxtExpYr().isEnabled()
			&& caSpclPltRegisData.getRegPltCd().equals(
				laSpclPltRegisData.getRegPltCd()))
		{
			int liExpYr = laSpclPltRegisData.getPltExpYr();
			if (liExpYr != 0)
			{
				gettxtExpYr().setText(
					"" + laSpclPltRegisData.getPltExpYr());
			}
		}
		// County of Residence enabled for HQ 
		if (gettxtResComptCntyNo().isEnabled())
		{
			int liResComptCntyNo =
				laSpclPltRegisData.getResComptCntyNo();

			if (liResComptCntyNo != 0)
			{
				gettxtResComptCntyNo().setText(
					"" + laSpclPltRegisData.getResComptCntyNo());
			}
		}
		if (gettxtDealerNo().isEnabled())
		{
			gettxtDealerNo().setText(
				laSpclPltRegisData.getPltOwnrDlrGDN());
		}
		if (getchkAddlSet().isEnabled())
		{
			getchkAddlSet().setSelected(
				laSpclPltRegisData.getAddlSetIndi() == 1);
		}
		if (getchkChrgSpclPltFee().isEnabled())
		{
			getchkChrgSpclPltFee().setSelected(
				laSpclPltRegisData.getSpclPltChrgFeeIndi() == 1);
		}

		// HQ Only 
		if (gettxtPltOwnrOfcCd().isEnabled())
		{
			gettxtPltOwnrOfcCd().setText(
				laSpclPltRegisData.getPltOwnrOfcCd());
		}
		if (gettxtPltOwnrDist().isEnabled())
		{
			gettxtPltOwnrDist().setText(
				laSpclPltRegisData.getPltOwnrDist());
		}
		if (gettxtSpclRemarks().isEnabled())
		{
			gettxtSpclRemarks().setText(
				laSpclPltRegisData.getSpclRemks());
		}

		if (gettxtAuctnPrice().isEnabled())
		{
			if (laSpclPltRegisData
				.getAuctnPdAmt()
				.compareTo(new Dollar(0.0))
				> 0)
			{
				gettxtAuctnPrice().setText(
					"" + laSpclPltRegisData.getAuctnPdAmt());
			}
		}

		if (gettxtResrvDate().isEnabled())
		{
			int liRsrvEffDate = laSpclPltRegisData.getResrvEffDate();
			if (liRsrvEffDate != 0)
			{
				RTSDate laRsrvDate =
					new RTSDate(RTSDate.YYYYMMDD, liRsrvEffDate);
				gettxtResrvDate().setDate(laRsrvDate);
			}
		}

		if (gettxtVendorTransDate().isEnabled())
		{
			int liVndrTransDate =
				laSpclPltRegisData.getVendorTransDate();
			if (liVndrTransDate != 0)
			{
				RTSDate laVndrDate =
					new RTSDate(RTSDate.YYYYMMDD, liVndrTransDate);
				gettxtVendorTransDate().setDate(laVndrDate);
			}
		}

		if (gettxtFINDocNo().isEnabled())
		{
			gettxtFINDocNo().setText(laSpclPltRegisData.getFINDocNo());
		}

		if (getchkMktngAllowd().isEnabled())
		{
			getchkMktngAllowd().setSelected(
				laSpclPltRegisData.isMktngAllowd());
		}
	}

	/**
	 * Setup Mfg Request for New Plates Desired 
	 */
	private void setupMfgRequestForNewPlateDesired()
	{
		// Setup For Receipt Processing 
		caSpclPltRegisData.setMFGStatusCd(
			SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD);
		caSpclPltRegisData.setMFGDate(csTransCd);
		if (caPltTypeData.getAnnualPltIndi() == 1)
		{
			// defect 9864 
			// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
			caSpclPltRegisData.setInvItmYr(
				caSpclPltRegisData.getPltExpYr());
			// end defect 9864 
		}
		else
		{
			caSpclPltRegisData.setInvItmYr(0);
		}
		caSpclPltRegisData.setMfgSpclPltIndi(1);
		RTSException leRTSEx =
			UtilityMethods.createSpclPltMfgInfoMsg(caSpclPltRegisData);
		leRTSEx.displayError(this);
	}

	/**
	 *   Handle Vendor Plate display 
	 */
	private void setVendorPltToDisplay()
	{

		// defect 9689
		// For Vendor Plates \ Revise \ not HQ: only Owner Addr enabled
		// NOTE: We are also disabling for Special Plates
		if (csTransCd.equals(TransCdConstant.SPREV)
			&& !SystemProperty.isHQ())
		{
			getcomboPlateType().setEnabled(false);
			getcomboOrganizationName().setEnabled(false);
			getcomboRequestType().setEnabled(false);
			gettxtExpMo().setEnabled(false);
			gettxtExpYr().setEnabled(false);
			getchkISA().setEnabled(false);
			gettxtDealerNo().setEnabled(false);
			gettxtOwnerId().setEnabled(false);
		}
		// end defect 9689
	}

	/**
	 *  Validate if: 
	 *    - SPUNAC   (Unacceptable)
	 *    - SPRSRV   (Reserve) 
	 *    - SPREV && Plate No && PltCd 
	 * 
	 * @return boolean
	 */
	private boolean shouldValidatePlateNo()
	{
		boolean lbValidate =
			csRegPltCd != null
				&& csRegPltCd.trim().length() > 0
				&& (isSPRSRV()
					|| isSPUNAC()
					|| (isSPREV()
						&& !(gettxtPlateNo()
							.getText()
							.trim()
							.equals(
								caSpclPltRegisData
									.getRegPltNo()
									.trim())
							&& csRegPltCd.equals(
								caSpclPltRegisData
									.getRegPltCd()
									.trim()))));
		return lbValidate;
	}

	/**
	 * Validate Combo Boxes
	 * 
	 * @param aeRTSEx
	 */
	private void validateComboBoxes(RTSException aeRTSEx)
	{
		// If Revise and Original Plate Type is Reserved || Unacceptable 
		// do not force plate type, organization.  
		// Note: Request type will be set and disabled  
		if (!(isSPREV()
			&& SystemProperty.isHQ()
			&& (csOrigRequestType
				.equals(SpecialPlatesConstant.UNACCEPTABLE)
				|| csOrigRequestType.equals(
					SpecialPlatesConstant.FROM_RESERVE))
			&& (csOrigRequestType.equals(csRequestType))))
		{
			if (getcomboPlateType().getSelectedIndex() == -1)
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getcomboPlateType());
			}
			else
			{
				if (getcomboOrganizationName().getSelectedIndex()
					== -1)
				{

					aeRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						getcomboOrganizationName());
				}
				if (getcomboRequestType().getSelectedIndex() == -1)
				{
					aeRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						getcomboRequestType());
				}
			}
		}

		// Do not allow selection of Plates where duplicates are allowed 
		// (OLDPLT and ROP as of 8/10/07)  
		if ((isSPRSRV() || isSPUNAC())
			&& caPltTypeData != null
			&& caPltTypeData.getDuplsAllowdCd()
				== SpecialPlatesConstant.DUPLSALLOWDCD_EVEN_IF_SAME_YR)
		{
			RTSException leRTSEx =
				new RTSException(ERRORNO_PLT_NOT_AVAIL_FOR_EVENT);
			aeRTSEx.addException(leRTSEx, getcomboPlateType());
		}
		if (isSPREV()
			&& csRegPltCd != null
			&& PlateTypeCache.isOutOfScopePlate(csRegPltCd))
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.FAILURE_MESSAGE,
					"The selected plate type is out of scope for this location",
					"ERROR");
			aeRTSEx.addException(leRTSEx, getcomboPlateType());
		}

	}

	/**
	 * Validate County No 
	 * 
	 * @param aeRTSEx
	 */
	private void validateCountyNo(RTSException aeRTSEx)
	{
		String lsCntyNo = gettxtResComptCntyNo().getText();
		// Only validate if enabled.
		// Note:  Will be modified to current county after display
		if (gettxtResComptCntyNo().isEnabled())
		{
			if (!(lsCntyNo.equals(CommonConstant.STR_SPACE_EMPTY)
				&& (isSPRSRV() || isSPUNAC())))
			{
				if (lsCntyNo.equals(CommonConstant.STR_SPACE_EMPTY)
					|| (Integer.parseInt(gettxtResComptCntyNo().getText())
						< 1
						|| Integer.parseInt(
							gettxtResComptCntyNo().getText())
							> MAX_CNTY_NO))
				{
					aeRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtResComptCntyNo());
				}
			}
		}
	}

	/**
	 * Validate Dealer License No 
	 * 
	 * @param aeRTSEx
	 */
	private void validateDealerLicenseNo(RTSException aeRTSEx)
	{
		// defect 9499 
		// Only throw exception if empty 
		// defect 9145
		// Handle PLPDLR validation on the dealer no field
		if (isDlrPlt()
			&& gettxtDealerNo()
				.isEnabled() //&& (gettxtDealerNo().getText().length() < 2
		//	|| !gettxtDealerNo().getText().startsWith(
		//		DEALER_NO_PREFIX)
		//	|| !UtilityMethods.isNumeric(
		//		gettxtDealerNo().getText().substring(1))))
			&& gettxtDealerNo().getText().length() == 0)
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtDealerNo());
		}
		// end defect 9145
		// end defect 9499 
	}

	/**
	 * Validate EMail Address & Phone No
	 * 
	 * @param aeRTSEx
	 */
	private void validateEMailPhoneNo(RTSException aeRTSEx)
	{
		// Validate EMail Address if supplied
		if (!CommonValidations.isValidEMail(gettxtEMail().getText()))
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtEMail());
		}
		// Confirm Phone No Valid if Provided
		// Note: Phone No is no longer required per VTR 5/14/07  
		if (gettxtPhoneNo().isPhoneNoAllZeros()
			|| (!gettxtPhoneNo().isPhoneNoEmpty()
				&& !gettxtPhoneNo().isValidPhoneNo()))
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtPhoneNo());
		}
	}

	/**
	 * Validate Exp Mo/Yr
	 * 
	 * @param aeRTSEx
	 */
	private void validateExpMoYr(RTSException aeRTSEx)
	{
		// defect 9864 
		// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr, 
		// OrigRegExpMo/Yr to OrigPltExpMo/Yr
		boolean lbExpMoReturn = true;
		boolean lbExpYrReturn = true;

		String lsExpMo = "";
		if (gettxtExpMo().isEnabled() || gettxtExpYr().isEnabled())
		{
			int liExpMo = 0;
			int liExpYr = 0;
			int liCurrentYr = new RTSDate().getYear();
			int liCurrentMo = new RTSDate().getMonth();
			if (isSPRNW()
				&& !CommonValidations.isRegistrationExpired(
					caSpclPltRegisData.getOrigPltExpMo(),
					caSpclPltRegisData.getOrigPltExpYr()))
			{
				liCurrentYr = caSpclPltRegisData.getOrigPltExpYr();
				liCurrentMo = caSpclPltRegisData.getOrigPltExpMo();
				if (liCurrentMo == 12)
				{
					liCurrentMo = 1;
					liCurrentYr = liCurrentYr + 1;
				}
				else
				{
					liCurrentMo = liCurrentMo + 1;
				}
			}
			lsExpMo = gettxtExpMo().getText().trim();
			if (lsExpMo.equals(""))
			{
				// Disregard if empty for SPRSRV || SPUNAC 
				if (!isSPRSRV() && !isSPUNAC())
				{
					lbExpMoReturn = false;
				}
			}
			else
			{
				try
				{
					liExpMo = Integer.parseInt(lsExpMo);
					if (liExpMo <= 0 || liExpMo > 12)
					{
						lbExpMoReturn = false;
						liExpMo = 0;
					}
					else
					{
						SpecialPlateFixedExpirationMonthData laSpclPltFxdExpMoData =
							SpecialPlateFixedExpirationMonthCache
								.getRegPltCd(
								csRegPltCd);
						// Fixed Exp Mo  
						if (laSpclPltFxdExpMoData != null
							&& liExpMo
								!= laSpclPltFxdExpMoData.getFxdExpMo())
						{
							lbExpMoReturn = false;
						}
					}
				}

				catch (NumberFormatException aeNumFEx)
				{
					lbExpMoReturn = false;
				}
			}

			String lsExpYr = gettxtExpYr().getText();
			if (lsExpYr.equals(""))
			{
				// Disregard if empty for SPRSRV || SPUNAC 
				if (!((isSPRSRV() || isSPUNAC())
					&& lsExpMo.equals("")))
				{
					liExpYr = 0;
					lbExpYrReturn = false;
				}
			}
			else
			{
				try
				{
					liExpYr = Integer.parseInt(lsExpYr);

					// defect 10295
					lbExpYrReturn =
						SpecialPlatesClientUtilityMethods
							.isValidPltExpYr(
							caPltTypeData.isVendorPlt(),
							liExpYr);

					if (lbExpYrReturn)
					{
						// end defect 10295

						// Invalid if < Original Year  
						if (liExpYr
							< caSpclPltRegisData.getOrigPltExpYr())
						{
							lbExpYrReturn = false;
						}
						// If Annual, validate InvItmYr against ExpYr
						else if (
							caPltTypeData != null
								&& caPltTypeData.getAnnualPltIndi() == 1)
						{
							String lsInvItmYear =
								gettxtInvItmYr().getText().trim();
							try
							{
								int liInvItmYr =
									Integer.parseInt(lsInvItmYear);

								if (liInvItmYr != liExpYr)
								{
									lbExpYrReturn = false;
								}
							}
							catch (NumberFormatException aeNumFEx)
							{
								lbExpYrReturn = false;
							}
						}

					}
				}
				catch (NumberFormatException aeNumFEx)
				{
					lbExpYrReturn = false;
				}
			}
			// If no errors && (SPAPPL || SPRNW)
			// Verify that stayed within 13 months 
			if (lbExpMoReturn
				&& lbExpYrReturn
				&& (isSPAPPL() || isSPRNW()))
			{
				int liCurrentMonths = liCurrentYr * 12 + liCurrentMo;
				int liExpirMonths = liExpYr * 12 + liExpMo;
				int liNoMonths = liExpirMonths - liCurrentMonths + 1;
				//				Display in Development Only 
				if (SystemProperty.getProdStatus()
					!= SystemProperty.APP_PROD_STATUS)
				{
					System.out.println(
						"validateExpMoYr() : " + liNoMonths);
				}
				// defect 9247 
				// SPRNW will now be validated as RegExp Mo/Yr blank
				//  for PLPDLRxx   
				if (((isSPAPPL() || isSPRNW())
					&& (liNoMonths > 15 || liNoMonths <= 0))
					|| (isSPREV()
						&& (liNoMonths > 15 || liNoMonths < -11)))
				{
					lbExpMoReturn = false;
					lbExpYrReturn = false;
				}
				// end defect 9247 
				// if valid ExpMo/Yr
				if (lbExpMoReturn
					&& lbExpYrReturn
					&& (isSPAPPL() || isSPRNW()))
				{
					caSpclPltRegisData.setNoMonthsToCharge(liNoMonths);
				}
			}
		}
		// end defect 9864

		if (!lbExpMoReturn)
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtExpMo());
		}
		if (!lbExpYrReturn)
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtExpYr());
		}
	}

	/**
	 * Owner Address controller for field validations.
	 * Return true if there were no validation error else return false.
	 * 
	 * @return boolean
	 */
	private boolean validateFields()
	{
		RTSException leEx = new RTSException();
		boolean lbValid = true;

		// Ensure that Plate No is Valid for Plate  
		if (!isSPDEL())
		{
			if (UtilityMethods.isSpecialPlates(csTransCd))
			{
				validateComboBoxes(leEx);
				validatePlateNo(leEx);
				// defect 9248 
				validateMfgPltNo(leEx);
				// end defect 9248 
				validateMfgRequest(leEx);
			}
			// Validate expiration Month/Yr if enabled 
			validateExpMoYr(leEx);
			// Validate Resident County Number 
			validateCountyNo(leEx);
			validateOwnrId(leEx);
			validateOwnrName1(leEx);
			validateOwnrAddr(leEx);
			validateEMailPhoneNo(leEx);
			validateInvItmYr(leEx);
			validateDealerLicenseNo(leEx);
			validateResrvReasnDesc(leEx);
		}

		if (leEx.isValidationError())
		{
			leEx.displayError(this);
			leEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		else if (getchkRequestRenewalNotice().isSelected())
		{
			lbValid = validateRequestRenewalNotice();
		}
		return lbValid;
	}

	/**
	 * Validate InvItmNo
	 * 
	 * @param aeRTSEx
	 */
	private void validateInvItmYr(RTSException aeRTSEx)
	{
		if (gettxtInvItmYr().isEnabled())
		{
			int liYear = 0;
			int liCurrentYear = new RTSDate().getYear();
			if (!gettxtInvItmYr().isEmpty())
			{
				liYear = Integer.parseInt(gettxtInvItmYr().getText());
			}
			if (liYear < liCurrentYear - 5
				|| liYear > liCurrentYear + 5)
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtInvItmYr());
			}
		}
	}

	/**
	 * Validate MfgPltNo for Personalized Plate. 
	 * 
	 * @param aeRTSEx
	 */
	private void validateMfgPltNo(RTSException aeRTSEx)
	{
		// defect 9385
		// Do not validate mfg plate no if patterned plate
		//  add && caPltTpeData.UserPltNoIndi() == 1  
		if (isSPREV()
			&& !isSPRSRV()
			&& !isSPUNAC()
			&& caPltTypeData != null
			&& caPltTypeData.getUserPltNoIndi() == 1)
		{
			boolean lbISA = getchkISA().isSelected();

			if (lbISA != cbOrigISA
				|| !csRegPltCd.equals(csOrigRegPltCd))
			{
				boolean lbValidMfgPltNo = true;

				// defect 9389 
				if (!lbISA
					&& csRegPltCd.equals(
						SpecialPlatesConstant.PER_DP_PRIVATE_BUS_PLT))
				{
					lbValidMfgPltNo = false;
				}
				// end defect 9389 

				else if (
					lbISA
						&& !(caPltTypeData
							.getISAAllowdCd()
							.equals(SpecialPlatesConstant.POS_EVENTS)
							|| (caPltTypeData
								.getISAAllowdCd()
								.equals(
									SpecialPlatesConstant
										.BOTH_POS_AND_ITRNT_EVENTS))))
				{
					lbValidMfgPltNo = false;
				}
				else
				{
					String lsPlateNo = gettxtPlateNo().getText().trim();
					String lsPersonalizedMfgPltNo =
						UtilityMethods.rTrim(
							gettxtMfgPlateNo().getText());
					int liMfgPltNoLength =
						lsPersonalizedMfgPltNo.length();
					String lsDerivedPlateNo = "";

					// Verify one and only one ISA
					// defect 10571  
					// int liISAPos = lsPersonalizedMfgPltNo.indexOf("%");
					int liISAPos =
						lsPersonalizedMfgPltNo.indexOf(
							PlateSymbolCache.ISASYMBOL);
					// end defect 10571 

					lbValidMfgPltNo = liISAPos < 0 ? !lbISA : lbISA;

					if (lbValidMfgPltNo && lbISA)
					{
						// defect 10571 
						lbValidMfgPltNo =
							lsPersonalizedMfgPltNo.indexOf(
							//"%",
	PlateSymbolCache.ISASYMBOL, liISAPos + 1) < 0;
						// end defect 10571 
					}

					if (lbValidMfgPltNo)
					{
						if (lbISA)
						{
							// ISA Symbol takes up 2 characters 
							liMfgPltNoLength = liMfgPltNoLength + 1;
						}

						if (liMfgPltNoLength
							> caPltTypeData.getMaxByteCount())
						{
							if (gettxtMfgPlateNo().isEnabled())
							{
								aeRTSEx.addException(
									new RTSException(ERRORNO_PLATENO_TOO_LONG),
									gettxtMfgPlateNo());
							}
							else
							{
								aeRTSEx.addException(
									new RTSException(ERRORNO_PLATENO_TOO_LONG),
									getcomboPlateType());
							}
						}
						else
						{
							// Verify that Derived Plate No matches Plate No
							for (int i = 0;
								i < lsPersonalizedMfgPltNo.length();
								i++)
							{
								String lsNextChar =
									lsPersonalizedMfgPltNo.substring(
										i,
										i + 1);

								// defect 10571 
								// if (!(lsNextChar
								// 	.equals(" ") 
								//	|| lsNextChar.equals("-")
								//	|| lsNextChar.equals(".")
								//	|| lsNextChar.equals("%")
								//	|| lsNextChar.equals("*")))
								if (!(lsNextChar.equals(" ")
									|| PlateSymbolCache.isPlateSymbol(
										lsNextChar)))
								{
									// end defect 10571 
									lsDerivedPlateNo =
										lsDerivedPlateNo + lsNextChar;
								}
								// defect 9384 
								// Do not allow special characters for ROP
								else
								{
									// defect 10571 
									if (csRegPltCd
										.equals(
											SpecialPlatesConstant
												.RADIO_OPERATOR_PLATE)
										//&& !lsNextChar.equals("%"))
										&& !lsNextChar.equals(
											PlateSymbolCache
												.ISASYMBOL))
									{
										// end defect 10571 
										lbValidMfgPltNo = false;
									}
								}
								// end defect 9384 
							}
							if (!lsPlateNo.equals(lsDerivedPlateNo))
							{
								lbValidMfgPltNo = false;
							}
						}
					}
				}
				if (!lbValidMfgPltNo)
				{
					if (gettxtMfgPlateNo().isEnabled())
					{
						aeRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtMfgPlateNo());
					}

					else
					{
						aeRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_FIELD_ENTRY_INVALID),
							getcomboPlateType());
					}
				}
			}
		}
		// end defect 9385 
	}

	/**
	 * Validate Mfg Request
	 * 
	 * @param aeRTSEx
	 */
	private void validateMfgRequest(RTSException aeRTSEx)
	{
		if (isSPREV()
			&& csOrgNo != null
			&& csRequestType.equals(SpecialPlatesConstant.MANUFACTURE))
		{
			if (isSunset())
			{
				aeRTSEx.addException(
					new RTSException(ERRORNO_SUNSET),
					getcomboOrganizationName());
			}
			if (isExpired())
			{
				aeRTSEx.addException(
					new RTSException(ERRORNO_RESET_ON_EXPIRED),
					getcomboRequestType());
			}
		}
	}

	/**
	 * Validate owner address is complete.
	 * 
	 * @param aeRTSEx RTSException 
	 */

	private void validateOwnrAddr(RTSException aeRTSEx)
	{
		// defect 10507 
		// Entitled must now add address 
		// OK if Empty:
		//         - Entitled
		//         - Reserved
		//         - Unacceptable
		//         - Not a Special Plate Event 
		boolean lbOKIfEmpty =
			caPltTypeData == null || (caPltTypeData != null
			//	(caPltTypeData
		//	.getPltOwnrshpCd()
		//	.equals(SpecialPlatesConstant.ENTITLED_OWNER)
	&& ((isSPRSRV() && !UtilityMethods.isSPAPPL(csTransCd))
		|| isSPUNAC()
		|| !UtilityMethods.isSpecialPlates(csTransCd)));

		if (!lbOKIfEmpty)
		{
			caNameAddrComp.validateAddressFields(aeRTSEx);

		}
		// OK if Empty - must still be valid if entered 
		else
		{
			if (getchkUSA().isSelected())
			{
				if (!gettxtOwnerState().isEmpty()
					&& !gettxtOwnerState().isValidState())
				{
					aeRTSEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_INVALID_STATE_REENTER),
						gettxtOwnerState());
				}

				if (!gettxtOwnerZpcd().isEmpty()
					&& !gettxtOwnerZpcd().isValidZpcd())
				{
					aeRTSEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_ZIP_CODE_MUST_BE_5_DIGITS),
						gettxtOwnerZpcd());
				}
				if (!gettxtOwnerZpcdP4().isEmpty()
					&& !gettxtOwnerZpcdP4().isValidZpcdP4())
				{
					aeRTSEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_ZIP_CODEP4_MUST_BE_4_DIGITS),
						gettxtOwnerZpcdP4());
				}
			}
			else
			{
				if (gettxtOwnerCntry().isValidState())
				{
					aeRTSEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_VALID_STATE_IN_CNTRY_FIELD),
						gettxtOwnerCntry());

					aeRTSEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_VALID_STATE_IN_CNTRY_FIELD),
						getchkUSA());
				}

			}
		}
	}

	/**
	 * Validate Owner Id
	 * 
	 * @param aeRTSEx RTSException
	 * @return boolean  
	 */

	private void validateOwnrId(RTSException aeRTSEx)
	{
		if (gettxtOwnerId().isEnabled()
			&& !gettxtOwnerId().isEmpty()
			&& UtilityMethods.isAllZeros(
				gettxtOwnerId().getText().trim()))
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtOwnerId());
		}
	}

	/**
	 * Validate owner name is complete.
	 * 
	 * @param aeRTSEx RTSException
	 * @return boolean  
	 */

	private void validateOwnrName1(RTSException aeRTSEx)
	{
		if (!isSPRSRV() && !isSPUNAC() && gettxtOwnerName1().isEmpty())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtOwnerName1());
		}
	}

	/**
	 * Validate Plate No in the event that regpltcd has changed
	 * 
	 * @param aeRTSEx
	 */
	private void validatePlateNo(RTSException aeRTSEx)
	{
		// Do not validate if "Grandfathered" plate  
		if (shouldValidatePlateNo())
		{
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(csRegPltCd);
			String lsRegPltNo = gettxtPlateNo().getText().trim();
			ValidateInventoryPattern laValidateInventoryPattern =
				new ValidateInventoryPattern();
			ProcessInventoryData laProcessInventoryData =
				new ProcessInventoryData();
			laProcessInventoryData.setItmCd(csRegPltCd);
			laProcessInventoryData.setInvQty(1);
			laProcessInventoryData.setInvItmNo(lsRegPltNo);
			if (gettxtInvItmYr().isVisible())
			{
				if (gettxtInvItmYr().getText().trim().equals(""))
				{
					aeRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtInvItmYr());
				}
				else
				{
					laProcessInventoryData.setInvItmYr(
						Integer.parseInt(gettxtInvItmYr().getText()));
				}
			}
			else
			{
				laProcessInventoryData.setInvItmYr(0);
			}
			try
			{

				laValidateInventoryPattern.validateItmNoInput(
					laProcessInventoryData.convertToInvAlloctnUIData(
						laProcessInventoryData));
			}
			catch (RTSException aeRTSEx1)
			{
				aeRTSEx.addException(aeRTSEx1, getcomboPlateType());
			}
			// defect 9384 
			// Validate Plate Length if Personalized and not ROP 
			if (laPltTypeData.getUserPltNoIndi() == 1
				&& !csRegPltCd.equals(
					SpecialPlatesConstant.RADIO_OPERATOR_PLATE))
			{
				// defect 10430 
				// if SPRSRV && > 6 characters  ||
				// if (SPRSRV || SPUNAC) && > MaxByteCount 
				//   Except 2014  
				//if ((isSPRSRV()
				//	&& lsRegPltNo.length()
				//		> SpecialPlatesConstant.MAX_PLP_PLTNO_LENGTH)
				//	||

				if ((isSPRSRV() || isSPUNAC())
					&& lsRegPltNo.length()
						> laPltTypeData.getMaxByteCount())
					// end defect 10430 
				{
					aeRTSEx.addException(
						new RTSException(ERRORNO_PLATENO_TOO_LONG),
						getcomboPlateType());
				}
			}
			// end defect 9384 

			// Validate that Plate Not Sunsetted if SPRSRV || SPUNAC
			if (csTransCd.equals(TransCdConstant.SPRSRV)
				|| csTransCd.equals(TransCdConstant.SPUNAC))
			{
				if (OrganizationNumberCache
					.isSunsetted(csRegPltCd, csOrgNo))
				{
					aeRTSEx.addException(
						new RTSException(ERRORNO_PLT_NOT_AVAIL_FOR_EVENT),
						getcomboPlateType());

					aeRTSEx.addException(
						new RTSException(ERRORNO_PLT_NOT_AVAIL_FOR_EVENT),
						getcomboOrganizationName());
				}
			}
		}
	}

	/**
	 * Validate acceptability of Request Renewal Notice
	 * 
	 * @param aeRTSEx
	 */
	private boolean validateRequestRenewalNotice()
	{
		boolean lbValid = true;
		RTSException leRTSEx = new RTSException();
		int liExpMo = Integer.parseInt(gettxtExpMo().getText());
		int liExpYr = Integer.parseInt(gettxtExpYr().getText());

		// SPRNR 
		// - ASSIGN || MANUFACTURE
		// - NOT EXPIRED
		// - IN WINDOW
		//

		// Note: This should not occur as Request Renewal Notice is 
		// reset upon selection of Unacceptable or Reserved. 
		if (!csRequestType.equals(SpecialPlatesConstant.ASSIGNED)
			&& !csRequestType.equals(SpecialPlatesConstant.MANUFACTURE))
		{
			leRTSEx =
				new RTSException(
					ERRORNO_NO_RNR,
					new String[] { ERRMSG_INVALID_PLT_STATUS });
			lbValid = false;
		}

		else if (
			CommonValidations.isRegistrationExpired(liExpMo, liExpYr))
		{
			leRTSEx =
				new RTSException(
					ERRORNO_NO_RNR,
					new String[] { ERRMSG_REG_EXP });
			lbValid = false;
		}

		else if (
			!CommonValidations.isInRenewalWindow(liExpMo, liExpYr))
		{
			leRTSEx =
				new RTSException(
					ERRORNO_NO_RNR,
					new String[] { ERRMSG_REG_IN_FUTURE });
			lbValid = false;
		}
		// defect 9484
		else if (
			caSpclPltRegisData
				.getSpclDocNo()
				.replace('0', ' ')
				.trim()
				.length()
				!= 0)
		{
			leRTSEx =
				new RTSException(
					ERRORNO_NO_RNR,
					new String[] { ERRMSG_LINKED_PLT });
			lbValid = false;

		}
		// end defect 9484

		if (!lbValid)
		{
			leRTSEx.displayError(this);
			getchkRequestRenewalNotice().setSelected(false);
		}
		return lbValid;
	}

	/** 
	 * If Reserve, validate Reserve Reason combo box 
	 * 
	 * @param aeRTSEx
	 */
	private void validateResrvReasnDesc(RTSException aeRTSEx)
	{
		if (isSPRSRV()
			&& getcomboResrvReasnDesc().isEnabled()
			&& getcomboResrvReasnDesc().getSelectedIndex() == -1)
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				getcomboResrvReasnDesc());
		}
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
