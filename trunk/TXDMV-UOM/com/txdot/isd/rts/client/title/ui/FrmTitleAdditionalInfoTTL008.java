package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;
import javax.swing.JCheckBox;
import java.awt.Dimension;
import java.awt.Point;

/*
 * 
 * FrmTitleAdditionalInfoTTL008.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		05/14/2002	Fixed CQU100003916. Check for empty string
 *							in verifyApprehendedCounty
 * T Pederson	05/24/2002	Fixed setData to check for null recond cd
 *							CQU100004018
 * J Rue		07/31/2002	defect 4468, fix code to ensure the ENTER
 *							command process has completed before the
 *							ESC can execute.
 *							modify actionPerformed()
 * MAbs			08/28/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * B Arredondo  10/21/2002	defect 4865, Changed code in method
 *							getchkMailInTransaction() so Mail-in
 *							Transaction is always enabled when entering
 *							screen. State/Country: is enabled only when
 *							Rebuilt Salvage - Issued By is selected.
 * B Arredondo	12/16/2002	Made changes for the user help guide.
 *							modify actionPerformed().
 *							defect 5147 
 * J Rue		02/04/2003	set suvirorship check box for DTA.
 *							modify doDlrTtl()
 *							defect 5178 
 * J Rue		07/22/2003	Ensure the radio buttons can be restored
 *							if ESC from a forward screen. 
 *							modify actionPerformed(),
 *							setDataToDataObject()
 *							add restoreCheckBoxselections(), 
 *							isRecordPrcs(),	setRecordPrcs(),
 *							enableRebuiltSalvage()
 *							defect 5628  Ver Fix 5.1.4
 * B Arredondo	08/01/2003	Added checkbox Charge Title TERP Fee.
 *							defect 6448 Ver 5.1.5
 * B Arredondo	08/05/2003	Code modification for checkbox which is 
 * 	  	 					identical to Charge Title Fee.
 *							defect 6448 
 * B Arredondo 	08/08/2003	Modify restoreCheckBoxSelections(),
 *							setData(), setDataToDataObject(),
 *							initialize() and keyPressed().
 *							defect 6448
 * B Arredondo	08/08/2003	Modify visual composition to set tabs to
 *							include Charge Title TERP Fee after
 *							Charge Title Fee
 *							defect 6448
 * J Rue		08/20/2003	Turn off enable/disable for Salvage 
 *							selection when cancel from foward screen.
 *							modify restoreCheckBoxSelection()
 *							defect 6507
 * J Rue		08/20/2003	Initialize State/Country
 *							modify setDataToData()
 *							defect 6509 
 * B Arredondo	09/15/2003	set Mnemonic to 'E' for the hot key of
 *							Charge Title TERP Fee
 *							defect 6564
 * B Arredondo	09/17/2003	Modified javadoc.
 * J Seifert	12/05/2003	Rebuilt Salvage Issue By cannot be TX so
 *							added edit check to action performed and
 *							displayed error message just like RTSI.
 *							Modify actionPerformed()
 *							defect 6438 Ver 5.1.5 Fix 2 
 * J Seifert	12/05/2003	Linned Up check box for Terp Fee in
 *							VisualComp
 *							defect 6585 Ver 5.1.5 Fix 2
 * J Seifert	12/05/2003	Charge Regis Emissions was Changing when
 *							escaping between TTL008 and TTL011.
 *							Changed: restoreCheckBoxSelections()
 *							defect 6558 Ver 5.1.5 Fix 2
 * B Hargrove	05/10/2004	Non-Title transfer to Original Title was
 *							not changing DocTypeCd.
 *							Problem was that code was still looking at
 *							RegWaivedIndi from the Non-Title. When
 *							setting DocTypeCd we should only care about
 *							'what will be', not 'what was'.
 *							modify setDataToDataObject()
 * B Hargrove	05/19/2004	Re-formatted comments in method above.
 *							defect 6206 Ver 5.2.0
 * J Zwiener	07/09/2004	Check boxes slightly clipped (lower part of
 *							cursor frame).
 *							Increase height of each check box/text from
 *							18 to 19 using Visual Comp.
 *							defect 6857, 6938
 * B Hargrove	11/17/2004	Transfer a DocTypeCd 11 (Insur-No-Regis) was
 *							not keeping DocTypeCd 11 (an insurance
 *							company selling vehicle to another insurance
 *							company).
 *							Modify setDataToDataObject()
 *							defect 7724 Ver 5.2.2
 * B Hargrove	11/24/2004	Cleanup comments to conform to template.
 *							defect 7724 Ver 5.2.2
 * K Harrell	12/16/2004	Change from getAddiToknFeeIndi() to 
 *							getAddlToknFeeIndi()
 *							modify doDlrTtl()
 *							defect 7736 Ver 5.2.2 
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/31/2005	Comment out all setNextFocusableComponent()
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/13/2005	Remove unused imports, methods, variables
 * 							Set tabbing to RTS standard
 * 							deprecated enabledRebuiltSalvage()
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/19/2005	Remove unused variables
 * 							defect 7898 Ver 5.2.3
 * K Harrell	05/17/2005	rename RegistrationClassData.getDiesleReqd()
 * 							to getDieselReqd()
 * 							defect 7786  Ver 5.2.3 
 * J Rue		05/26/2005	Set New Plates Desired to check and 
 * 							disabled if regPltCd changed.
 * 							modify setData()
 * 							defect 7865 Ver 5.2.3
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/19/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		08/23/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		10/28/2005	Add title to borders
 * 							modify getJPanel1()
 * 							defect 8416 Ver 5.2.3
 * J Rue		11/04/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3    
 * J Rue		12/13/2005	Comment out setSelected(true) for arrow key
 * 							movement on radio buttons
 * 							modify KeyPressed()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	12/23/2005	Corrected typo in setData()
 * 							modify setData()
 * 							defect 7898 Ver 5.2.3 
 * Jeff S.		01/04/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), keyReleased(), keyTyped(), 
 * 								focusGained(), focusLost(), 
 * 								carrRebuiltSalv, carrCheckBoxes, 
 * 								NOT_REBUILT, ISSUED_BY, ciSelectedNum, 
 * 								ciCheckBoxWithFocus
 * 							modify getchkChargeAddlTokenTrailerFee(), 
 * 								getchkChargeRegEmissionFee(), 
 * 								getchkChargeRegTransferFee(), 
 * 								getchkChargeTitleFee(), 
 * 								getchkChargeTransferPenalty(), 
 * 								getchkDiesel(), getchkExempt(), 
 * 								getchkDOTProofRequired(), 
 * 								getchkFloodDamage(), 
 * 								getchkGovernmentOwned(), 
 * 								getchkMailInTransaction(), 
 * 								getchkNewPlatesDesired(), 
 * 								getchkReconstructed(), 
 * 								getchkSpecialExamNeeded(), 
 * 								getchkSurvivorshipRights(), 
 * 								getchkVerifiedHeavyVehUseTax(), 
 * 								getchkVINCertificationWaived(), 
 * 								getpnlGrpRebuiltSalvage(), 
 * 								getpnlGrpTireType(), 
 * 								getradioNotRebuilt(), 
 * 								getradioPneumatic(), 
 * 								getradioRebuiltSalvage7594PctLoss(), 
 * 								getradioRebuiltSalvage95PctPlusLoss(), 
 * 								getradioRebuiltSalvageIssuedBy(), 
 * 								getradioRebuiltSalvageLossUnknown(), 
 * 								getradioSolid(), initialize()
 * 							defect 7898 Ver 5.2.3 
 * B Hargrove	02/03/2006	Since Seasonal Ag Combo (RegClassCd 76) was
 *							created, change to look up Emission Fee
 *							Percent from CommonFees table.
 *							Includes defect 8535 to check for null
 *							CommonFees after lookup. 
 *							add caCommFeesData, ZERODOLLAR
 *							modify setData()
 *							delete COMBINATION
 *							defect 8516 Ver 5.2.2 Fix 8
 * B Hargrove	02/06/2006	Needed to check for null CommonFees for code
 *							put in for defect 8516.
 *							modify setData()
 *							defect 8535 Ver 5.2.2 Fix 8
 * Jeff S.		06/26/2006	Used screen constant for CTL001 Title.
 * 							remove CTL001
 * 							modify checkHQ_Exempt()
 * 							defect 8756 Ver 5.2.3
 * Min Wang		10/11/2006	New Requirement for handling plate age 
 * 							modify setData() 
 *							defect 8901 Ver Exempts
 * B Hargrove   11/07/2006 	Counties can now do Exempts. Handle 
 * 	K Harrell				Enable \ Disable and Check \ Uncheck of the
 * 							Exempt Indi and Charge Fee checkboxes.
 * 							modify actionPerformed(), getchkExempt(),
 * 							setData()
 * 							defect 8900 Ver Exempts
 * K Harrell	11/13/2006	Reorganize code for enable/disable Exempt
 * 							Checkbox in setData(). 
 * 							Do not test for enable prior to	checking 
 * 							exempt checkbox.  (restoreCheckBoxSelections())  
 * 							modify restoreCheckBoxSelections(),setData()
 * 							defect 8900 Ver Exempts
 * K Harrell	11/22/2006	Only check Plate Age as Mandatory if 
 * 							RegisRenewals.NeedsProgramCd = "R"; Additional
 * 							code cleanup to match Issue Inventory 
 * 							Processing. 
 * 							add PLT_REPLACEMENT_REQUIRED
 * 							modify setData() 
 * 							defect 8900 Ver Exempts 
 * B Hargrove   11/29/2006 	Check for 'not HQ' before setting Charge 
 *							Transfer Fee to 'selected'. Also changed
 *							edit that was checking for '= County' to 
 *							check for 'not HQ' because Regions are 
 *							supposed to work like County.
 *							Note: ExemptIndi on existing vehicles is 
 *							cleared on TTL003 for all Title events except
 *							Corrected Title.
 * 							modify actionPerformed(), setData()
 * 							defect 9036 Ver Exempts
 * K Harrell	11/30/2006	Modify handling of Standard Exempt. Do not
 * 							mark as "New Plates Desired" inappropriately,
 * 							i.e. Standard Exempt, REJCOR, !Change Plate
 * 							modify setData()
 * 							defect 9041 Ver Exempts
 * K Harrell	02/05/2007	Use PlateTypeCache vs. 
 * 								RegistrationRenewalsCache
 * 							modify setData()
 * 							defect 9085 Ver Special Plates  
 * K Harrell	02/05/2007	Reinstate code enabling "New Plates Desired"
 * 							when in window. 
 * 							modify setData()
 * 							defect 9071 Ver Special Plates
 * K Harrell	03/19/2007	Use SystemProperty.isHQ() && .isCounty() 
 * 							modify actionPerformed(), setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/30/2007	Update SpecialPlatesRegisData to request
 * 							Manufacture if PlateAge > Mandatory
 * 							modify setData()
 * 							defect 9085 Ver Special Plates  
 * J Rue		10/12/2007	Add PltTrnsfrFee check box
 * 							Set ChrgBuyerTagFeeIndi, PltTrnsfrFeeIndi
 * 							add PLT_TRNSFR_FEE, getchkPltTrnsfrFee()
 * 							modify setDataToDataObject(), initialize(),
 * 							 setData(), getJPanel2(), 
 * 							 restoreCheckBoxSelections()
 * 							defect 9367 Ver Special Plates 2
 * K Harrell	10/14/2007	Do not remanufacture if sunsetted. 
 * 							modify setData()
 * 							defect 9299 Ver Special Plates 2 
 * J Rue		10/15/2007	De-select Plate Transfer fee if Exempt check
 * 							is selected.
 * 							modify actionPerformed()
 * 							defect 9367 Ver Special Plates 2 
 * J Rue		10/15/2007	Add BuyerTagFee check box
 * 							Set ChrgBuyerTagFeeIndi
 * 							add BUYER_TAG_TRNSFR_FEE, 
 * 							  getchkBuyerTag()
 * 							modify setDataToDataObject(), initialize(),
 * 							 setData(), getJPanel3(),
 * 							 restoreCheckBoxSelections()
 * 							defect 9367 Ver Special Plates 2
 * J Rue		10/16/2007	Update code to handle PltTrnsfrFee and
 * 							 BuyerTag
 * 							modify setData()
 * 							defect 9367 Ver Special Plates 2
 * B Hargrove	10/18/2007	May enable 'New Plate Desired' if eligible for
 * 							Plate Transfer. If 'New Plates Desired' is 
 * 							not otherwise selected\disabled (ie: new
 * 							vehicle, plate age > 7, etc), then if user 
 * 							selects 'Charge Plate Transfer Fee', we know
 * 							that new plates (either cust supplied or 
 * 							issued inv) will be on the vehicle, so set 
 * 							'New Plates Desired' selected\disabled. 
 * 							If user de-selects 'Charge Plate Transfer 
 * 							Fee', we do not know if new plates are needed
 * 							or not, so set 'New Plates Desired' 
 * 							de-selected\enabled (depending if 
 * 							'cbKeepNewPltsDisabled' is false).
 *							add cbKeepNewPltsDisabled
 * 							modify actionPerformed(), 
 * 							getchkPltTrnsfrFee(), setData()
 * 							defect 9366 Ver Special Plates 2
 * K Harrell	10/21/2007	Removed call to restoreCheckBoxes() if !DTA
 * 							Force issue inventory even when not
 * 							in window if selected. 
 * 							add ciOrigMustChangePltIndi,cbAlreadySet,
 * 							 cbBuyerTagPrevSetting, cbNPDApplicable 
 * 							add setupNewPlatesDesiredForDisplay() 
 * 							modify actionPerformed(),
 * 							  setData(), setDataToDataObject()
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	10/22/2007	Compare to SystemProperty BuyerTagStartDate
 * 							vs. PTOStartDate for enabling BuyerTag checkbox
 * 							modify setData()
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	11/20/2007  Additional work for PTO.  Class cleanup. 
 * 							Removed mnemonics for Privacy Opt
 * 							Change mnemonic to 'M' for Flood Damage.
 * 							Use 'F' for Plate Transfer Fee.  
 * 							add ciOrigMustChangePltIndi,
 * 							 cbNPDPrevSetting, caMFVehData,
 * 							 caTtlData, caVehData, caRegData, csTransCd 
 *							add isNewPlatesDesiredApplicable(),
 *							  setupNewPlatesDesiredForDisplay()
 *							modify actionPerformed(), getchkFloodDamage(),
 *							 getchkPltTrnsfrFee(), setData(), 
 *							 setDataToDataObject() 
 * 							defect 9368/9425 Ver Special Plates 2  
 * B Hargrove	11/21/2007	I already had a constant for 'TONLY', so use
 *							it in Kathy's defect 9367.
 * 							modify isNewPlatesDesiredApplicable()
 * 							remove TONLY_REGPLTCD 
 * 							defect 9337 Ver Special Plates 2
 * K Harrell	05/22/2008	New Plates Required for Title if Plates 
 * 							 removed
 * 							modify setData()
 * 							defect 9670 Ver POS Defect A 
 * K Harrell	05/26/2008	Remove checkbox, logic for Delinquent Penalty
 * 							Moved Plate Transfer, Buyer Tag via Visual
 * 							Editor 
 *  						delete ivjchkChargeTransferPenalty 
 * 							delete getchkChargeTransferPenalty()
 * 							modify initialize(), restoreCheckBoxSelections(),
 * 							 setData(), setDataToDataObjects(),getJPanel12()
 * 							defect 9584 Ver Defect POS A
 * K Harrell	06/02/2008  Show Supervisor Override Screen if deselect 
 * 							Buyer Tag 
 * 							add MODIFIED_BUYER_TAG_SELECTION
 * 							add isSupOvrdRqdforBuyerTag()
 * 							modify actionPerformed()
 * 							defect 9664 Ver Defect POS A
 * K Harrell	06/04/2008	Disable Plate Transfer Fee for DTA if 
 * 							Customer Supplied 
 * 							modify setData()
 * 							defect 9698 Ver Defect POS A 
 * B Hargrove	07/11/2008 	Add check to see if this plate type allows
 * 							'Customer Supplied' (ie: 'OLDPLT2') when
 * 							setting MustChangePltIndi.
 * 							modify setDataToDataObject() 
 * 							defect 9529 Ver MyPlates_POS
 * K Harrell	08/14/2008	Do not enable BuyerTag on REJCOR,CORTTL if
 * 							TtlSignDate is prior to BuyerTagStartDate.
 * 							modify setData() 
 * 							defect 9799 Ver MyPlates_POS
 * K Harrell	09/24/2008	Do not enable New Plates Desired just because
 * 							PTO. 
 * 							modify setupNewPlatesDesiredForDisplay(),
 * 							 actionPerformed() 
 * 							defect 9824 Ver Defect_POS_B
 * K Harrell	10/09/2008	Defect 9824 redefined;If PTO: If Dealer trans
 * 							(DLRGDN != '' || DTA* Trans), select & disable
 * 							NPD. Else, if NPD not mandatory, NPD enabled,
 * 							not selected per 10/9 mtg w/ Jim Z, Kathy, VTR.  
 *  						defect 9824 Ver Defect_POS_B
 * K Harrell	03/05/2009	Moved assignment of DocTypeCd to TTL007. 
 * 							modify setDataToDataObject()
 * 							defect 9971 Ver Defect_POS_E 
 * K Harrell	04/06/2009	add checkbox to retain, remove ETitle 
 * 							 when Corrected Title & prior ETtlCd = 3
 * 							 and no new liens 
 * 							add ivjchkETitle, getchkETitle(), 
 * 								setupETtlChkBox() 
 * 							modify actionPerformed(), setData(), 
 * 							  setDataToDataObject() 
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	04/28/2009	Use prior DocTypeCd to set Rebuilt Salvage 
 * 							  radio buttons.  (Setting of DocTypeCd was
 * 							  moved to TTL007 in this release to set
 * 							  ETtlCd on TTL007, TTL035). 
 * 							Also, treat REJCOR like CORTTL if prior 
 * 							  ETitle w/ no lien. 
 * 							add setupRebuiltSalvageChoice()  
 * 							delete ciOrigDocType, getOrigDocType(), 
 * 							  setOrigDocType() 
 * 							modify setData(), actionPerformed(),
 * 								setupETtlChkBox() 
 * 							defect 9971 Ver Defect_POS_E  
 * Min Wang	 	09/08/2009	add a checkbox for �Private Law Enforcement 
 * 							Vehicle� on the screen.
 * 							add ivjchkPriLawEnfVeh, getchkPrivateLawEnfVeh()
 * 							modify getJPanel12(),setData(),
 * 							setDataToDataObject(),
 * 							Visual Edit
 * 							defect 10153 Ver Defect_POS_F
 * K Harrell	10/14/2009	add checkbox for �Manufacturer Buyback�
 * 							add MANUFACTURER_BUYBACK
 * 							add ivjchkManufacturerBuyback, get method
 * 						   	modify getJPanel2(), setData(), 
 * 							  setDataToDataObject() 
 * 							Visual Edit 
 * 							defect 10252 Ver Defect_POS_G
 * K Harrell	10/14/2009	Remove references to PrivacyOptCd
 *  						modify setDataToDataObject(),
 * 							  restoreCheckBoxSelections()
 * 							defect 10246 Ver Defect_POS_G 
 * Min Wang		11/24/2009	Merge Issue. delete duplicated code.
 * 							defect 10153 Ver Defect_POS_H
 * K Harrell	12/16/2009	DTA cleanup.  
 * 							add caDlrTtlData 
 * 							add isApprhndCntyNoValid()  
 * 							delete restoreCheckBoxSelections(),
 * 							 isApprhndCntyNoInvalid()  
 * 							modify actionPerformed(), doDlrTtl(), 
 * 							 setData(), verifyApprehendedCounty() 
 * 							defect 10290 Ver Defect_POS_H  
 * Min Wang 	12/21/2009	Set solid tire type
 * 							modify setData()
 * 							defect 10197 Ver Defect_POS_H	
 * Min Wang		12/30/2009	Use common constant to set up tire type.
 * 							modify setData()
 * 							defect 10317 Ver Defect_POS_H
 * Min Wang		01/04/2010	Fixed System Error.  If tire type is not null,
 * 							set up tire type.
 * 							modify setData()
 * 							defect 10197 Ver Defect_POS_H
 * K Harrell	01/25/2010  Typo when consolidating data in setData()
 * 							modify setData() 
 * 							defect 10342 Ver Defect_POS_H
 * K Harrell	02/09/2010	TitleData VTRTtlEmrgCd1 refactored to 
 * 							  PvtLawEnfVehCd
 * 							modify setData(), setDataToDataObject()
 * 							defect 10366 Ver POS_640 
 * Min Wang 	02/26/2010	Remove privacy act options from the screen.
 * 							delete BOTH, COMMERCIAL, INDIVIDUAL, NONE,
 * 							SEL_PRIVACY_ACT_OPT, ivjpnlGrpPrivacyAct,
 * 							getpnlGrpPrivacyAct(),
 * 							getradioBoth(), getradioCommercial(),
 * 							getradioIndividual(), getradioNone()
 * 							modify initialize(),
 * 							modify getFrmTitleAdditionalInfoTTL008ContentPane1()
 * 							by Visual Editor.
 * 							defect 10159 Ver POS_640
 * K Harrell	03/12/2010	EmissionsFee not selected if applicable. 
 * 							modify setData()
 * 							defect 10395 Ver POS_640  
 * B Hargrove	07/13/2010	Heavy Vehicle Use Tax indi should be cleared
 * 							for most Title events upon entry to force
 * 							proof of payment.
 *        					modify setData()  
 *        					defect 10381 Ver 6.5.0
 * Min Wang		07/16/2010	Add check box Verified Tow Truck
 * 							Certificate on the screen.
 * 							add ivjchkTowTruckCert, getchkTowTruckCert()
 * 							modify actionPerformed(), initialize(), 
 * 							getJPanel3(), setData()
 * 							defect 10007 Ver 6.5.0
 * Min Wang		07/27/2010	modify setData()
 * 							defect 10007 Ver 6.5.0
 * B Hargrove	08/02/2010	After further review, Heavy Vehicle Use Tax 
 * 							indi should not be cleared for DTA.
 *        					modify setData()  
 *        					defect 10381 Ver 6.5.0
 * Min Wang		10/20/2010  disable and uncheck Survivorship right when 
 * 							a non-title or registration purposes only
 * 							transaction is being processed.
 * 							modify setData()
 * 							defect 10601 Ver 6.6.0
 * B Hargrove	07/27/2011	As-of Dealer Invalidate Registration start 
 * 							date, we will disable the Charge Registration 
 * 							Transfer Fee if Dealer GDN is present and
 *                          this is not CORTTL or REJCOR. Also set 'Must
 * 							Change Plate Indi' so that 'New Plates 
 * 							Desired' is selected. 
 * 							modify doDlrTtl(), setData()
 * 							defect 10949 Ver 6.8.0
 * K Harrell	08/01/2011	Recoding to prevent compile error
 * 							modify isSupOvrdRqdforBuyerTag()
 * 							RAD Migration Ver 6.8.1    
 * B Hargrove	09/06/2011	Refinement of 10949: 
 * 							We will only remove plate if PTO Eligible or
 * 							if not PTO Eligible but is an Annual Plate that
 * 							is Transferable and is not a Special Plate.
 * 							(current plates like this: ATVS, CP, DRP, FP, SCP) 
 * 							modify setData(),
 * 							setupNewPlatesDesiredForDisplay()
 * 							defect 10988 Ver 6.8.1
 * K Harrell	09/28/2011	Change wording for Plate Transfer Fee checkbox
 * 							add ivjchkPTOTrnsfr, get method 
 * 							add PTO_TRNSFR
 * 							delete ivjchkPltTrnsfrFee, get method
 * 							delete PLT_TRNSFR_FEE  
 * 							modify actionPerformed(), getJPanel12(), 
 * 							 initialize(),setData(), setDataToDataObject()  
 *  						defect 11030 Ver 6.9.0 
 *  K Harrell	09/28/2011	Correct movement through checkboxes 
 *  						modify initialize()
 *  						defect 10982 Ver 6.9.0
 * B Hargrove	10/06/2011  We should not be setting 'must change plate 
 * 							indi' in TTL008. This messes us up later when
 * 							a plate change is needed just because of the
 * 							'needs program' (plate age > 7). It causes 
 *    						Fees to treat it as an exchange.
 *    						see: Fees setFromMoFromYr()edit for:
 *    						'&& ciMustChangePltIndi == 0'. This causes
 *    						From Month = Today, calcs credit, and charges
 *    						from today. Should be from end of current reg.
 *    						Note: we DO SET laTtlAddlData.setNewPltDesrdIndi(1)
 *    						and we can use this in Fees.
 *        					modify setData(), setDataToDataObject() 
 *        					defect 10999 Ver 6.9.0
 * K Harrell	10/10/2011	modify setDataToDataObject()
 *  						defect 11030 Ver 6.9.0   
 * K Harrell	10/15/2011	Validate State/Cntry per CommonValidations
 *  						modify actionPerformed() 
 *  						defect 11004 Ver 6.9.0 
 * K Harrell	10/16/2011	add checkbox for Charge Salvage Rebuilt Fee 
 * 							add ivjchkChargeSalvageRebuiltFee, get method
 * 							modify actionPerformed(), getJPanel1(), 
 * 							 setDataToDataObject() 
 * 							defect 11051 Ver 6.9.0  
 * K Harrell	10/27/2011	If RecondCd set for DTA, also select 
 * 							Charge Salvage Rebuilt Fee 
 * 							modify doDlrTtl()
 * 							defect 11051 Ver 6.9.0 
 * K Harrell	10/31/2011	Remove the setting of Charge Salvage 
 * 							Rebuilt Fee for DTA per VTR 
 * 							modify doDlrTtl() 
 * 							defect 11051 Ver 6.9.0 
 * B Hargrove	11/07/2011	Fix 10949: 
 * 							We did not set the 'real original' Reg Data
 * 							object to Reg Invalid.
 * 							We don't need to check if expired (if Dle GDN
 * 							we always use 'Valid Reason' anyway (11054)).
 * 							modify setData()
 * 							defect 11136 Ver 6.8.2  
 * K Harrell	11/07/2011	Charge Salvage Rebuilt Fee is not applicable 
 * 							for Non-Titled 
 * 							modify setData()
 * 							defect 11051 Ver 6.9.0 
 * K Harrell	11/09/2011	Set Focus on Charge Salvage Rebuilt Fee
 * 							checkbox 
 * 							modify actionPerformed()
 * 							defect 11051 Ver 6.9.0 
 * B Hargrove	11/22/2011  Add check for 'Plate Removed code' and if
 * 							DTA has issued a plate in setting 
 * 							display of New Plates Desired checkbox.
 *        					modify setupNewPlatesDesiredForDisplay() 
 *        					defect 10951 Ver 6.9.0
 * K Harrell	12/08/2011	Implement new requirements for Rebuilt Salvage
 * 							add ciOrigDocTypeCd 
 * 							modify setData(), actionPerformed() 
 * 							defect 11051 Ver 6.9.0 
 * K Harrell	12/12/2011	Charge Rebuilt Salvage Fee is not enabled
 * 							for RPO
 * 							modify setData()
 * 							defect 11051 Ver 6.9.0 
 * K Harrell	12/16/2011	Implement new requirements for Rebuilt Salvage(2)
 * 							Charge Rebuilt Salvage Fee will always be enabled
 * 							 if not RPO and not NONTTL  
 * 							modify setData() 
 * 							defect 11051 Ver 6.9.0 
 * K Harrell	02/02/2012	Correctly assign Survivorship Rights checkbox 
 * 								in RejCor.
 * 							Cleanup unused code from 11051 rework re: 
 * 								Original DocTypeCd  
 * 							delete ciOrigDocTypeCd
 * 							modify setData() 
 * 							defect 10946 Ver 6.10.0 
 * B Hargrove 	02/02/2012  We want to select and disable 'New Plates 
 * 							Desired' when Dealer GDN was entered.
 * 							modify setupNewPlatesDesiredForDisplay()
 * 							(moved up from Rel 6.9.0)
 * 							defect 11264 Ver 6.9.0
 * B Hargrove	02/16/2012	modify setDataToDataObject()	
 * 	Kathy Harrell			modify actionPerformed()
 *  						defect 11264 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to capture a variety of vehicle indicators and 
 * title processing information.
 *
 * @version 6.10.0	02/16/2012
 * @author	Ashish Mahajan
 * @since 			06/30/2001 18:37:20
 */

public class FrmTitleAdditionalInfoTTL008
	extends RTSDialogBox
	implements ActionListener
{
	private Border caEtched;

	private JCheckBox ivjchkChargeAddlTokenTrailerFee = null;
	private JCheckBox ivjchkChargeRegTransferFee = null;
	private JCheckBox ivjchkChargeTitleFee = null;
	private JCheckBox ivjchkChargeTitleTERPFee = null;
	private JCheckBox ivjchkGovernmentOwned = null;
	private JCheckBox ivjchkAddlEvidenceSurrendered = null;
	private JCheckBox ivjchkNewPlatesDesired = null;
	private JCheckBox ivjchkMailInTransaction = null;
	private JCheckBox ivjchkSpecialExamNeeded = null;
	private JCheckBox ivjchkDOTProofRequired = null;
	// defect 9367
	
	// defect 11030 
	private JCheckBox ivjchkPTOTrnsfr = null;
	// end defect 11030 
	
	private JCheckBox ivjchkBuyerTag = null;
	// end defect 9367
	private JCheckBox ivjchkVINCertificationWaived = null;
	private JCheckBox ivjchkDiesel = null;
	private JCheckBox ivjchkExempt = null;
	private JCheckBox ivjchkFloodDamage = null;
	private JCheckBox ivjchkReconstructed = null;
	private JCheckBox ivjchkSurvivorshipRights = null;
	private JCheckBox ivjchkChargeRegEmissionFee = null;
	private JCheckBox ivjchkVerifiedHeavyVehUseTax = null;

	// defect 9971 
	private JCheckBox ivjchkETitle = null;
	// end defect 9971

	//	defect 10153
	private JCheckBox ivjchkPrivateLawEnfVeh = null;
	// end defect 10153

	// defect 10252 
	private JCheckBox ivjchkManufacturerBuyback = null;
	// end defect 10252 
	
	// defect 10007
	private JCheckBox ivjchkTowTruckCert = null;
	// end defect 10007

	private JRadioButton ivjradioNotRebuilt = null;
	private JRadioButton ivjradioRebuiltSalvage7594PctLoss = null;
	private JRadioButton ivjradioRebuiltSalvage95PctPlusLoss = null;
	private JRadioButton ivjradioRebuiltSalvageIssuedBy = null;
	private JRadioButton ivjradioRebuiltSalvageLossUnknown = null;
	private JRadioButton ivjradioPneumatic = null;
	private JRadioButton ivjradioSolid = null;

	private RTSButtonGroup caButtonGroup1;
	private RTSButtonGroup caButtonGroup2;

	private JLabel ivjstcLblStateCountry = null;
	private JLabel ivjstcLblApprehendedFundsCountyNo = null;

	private RTSInputField ivjtxtSalvageStateCountry = null;

	private JPanel ivjpnlGrpFeeTypes = null;
	private JPanel ivjpnlGrpRebuiltSalvage = null;
	private JPanel ivjpnlGrpTireType = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjFrmTitleAdditionalInfoTTL008ContentPane1 = null;

	private ButtonPanel ivjECHButtonPanel = null;
	private RTSInputField ivjtxtApprhndCntyNo = null;

	// Objects
	private CommonFeesData caCommFeesData = null;
	private VehicleInquiryData caVehInqData = null;

	// defect 10290 
	private DealerTitleData caDlrTtlData = null;
	// end defect 10290  

	// booleans
	private boolean cbProcessed = false;
	private boolean cbKeepNewPltsDisabled = false;

	// defect 9664 
	private final String MODIFIED_BUYER_TAG_SELECTION =
		"MODIFIED BUYER TAG SELECTION";
	// end defect 9664 

	// defect 9368 
	private boolean cbAlreadySet = false;
	private boolean cbNPDApplicable = false;
	private boolean cbNPDPrevSetting = false;
	private boolean cbBuyerTagPrevSetting = false;
	private MFVehicleData caMFVehData;
	private TitleData caTtlData;
	private VehicleData caVehData;
	private RegistrationData caRegData;
	private String csTransCd = CommonConstant.STR_SPACE_EMPTY;
	// end defect 9368 

	// defect 10946
	// defect 11051 
	//private int ciOrigDocTypeCd = -1; 
	// end defect 11051
	// end defect 10946 
	
	// Constants int
	private final static int CNTYNO_MAX_LEN = 3;
	private final static int TOTAL_NO_CNTY = 254;

	private final static Dollar ZERODOLLAR = new Dollar(0.0);

	// Constants String
	private final static String SEL_REBUILT_SLVG =
		"Select rebuilt salvage choice:";
	private final static String SEL_TIRE_TYPE = "Select tire type:";
	// defect 10159
	//private final static String SEL_PRIVACT_ACT_OPT =
	//	"Select privacy act option:";
	// end defect 10159
	private final static String ADDL_EDVID_SURR =
		"Add'l Evidence Surrendered";
	private final static String CHRG_ADDL_TOKEN_TRLR =
		"Charge Add'l Token Trailer Fee";
	private final static String CHRG_TNRCC_PNTLY =
		"Charge TNRCC Penalty";
	private final static String CHRG_REGIS_TRANSFER_FEE =
		"Charge Registration Transfer Fee";
	private final static String CHRG_REGIS_EMISSION_FEE =
		"Charge Regis Emission Fee";
	private final static String CHRG_TTL_FEE = "Charge Title Fee";
	private final static String CHRG_TTL_TERP_FEE =
		"Charge Title TERP Fee";

	private final static String DIESEL = "Diesel";
	private final static String DOT_PROOF_REQUIRED =
		"DOT Proof Required";
	// defect 11030 
	//private final static String PLT_TRNSFR_FEE = "Plate Transfer Fee";
	private final static String PTO_TRNSFR = "Plate to Owner Transfer";
	// end defect 11030
	// defect 9367
	private final static String BUYER_TAG = "Buyer Tag ";
	// end defect 9367
	private final static String EXEMPT = "Exempt";
	private final static String FLOOD_DAMAGE = "Flood Damage";
	private final static String GOVERMENT_OWNED = "Government Owned";
	private final static String MAILIN_TRANSACTION =
		"Mail-In Transaction";
	private final static String NEW_PLATES_DESIRED =
		"New Plates Desired";
	private final static String RECONSTRUCTED = "Reconstructed";
	private final static String SPECIAL_EXAM_NEEDED =
		"Special Examination Needed";
	private final static String SURVIVORSHIP_RIGHTS =
		"Survivorship Rights";
	private final static String VERIFY_HVY_VEH_USE_TAX =
		"Verified Heavy Vehicle Use Tax";
	private final static String VIN_CERTIFICATION_WAIVED =
		"VIN Certification Waived";
	// defect 10159
	//private final static String BOTH = "Both";
	//private final static String COMMERCIAL = "Commercial";
	//private final static String INDIVIDUAL = "Individual";
	//private final static String NONE = "None";
	// end defect 10159
	private final static String NOT_REBUILD = "Not Rebuilt";
	private final static String PNEUMATIC = "Pneumatic";
	private final static String REBUILT_SLVG_75_94 =
		"Rebuilt Salvage - 75-94% Loss";
	private final static String CHK_IF_APPLICABLE =
		"Check if applicable:";
	private final static String REBUILD_SLVG_95_LOSS =
		"Rebuilt Salvage - 95% Plus Loss";
	private final static String REBUILT_SLVG_ISSUED_BY =
		"Rebuilt Salvage - Issued By";
	private final static String REBUILD_SLVG_LOSS_UNKNOWN =
		"Rebuilt Salvage - Loss Unknown";
	private final static String SOLID = "Solid";
	private final static String ADDL_EVDENCE_SURRENDERED =
		"Add'l Evidence Surrendered";
	private final static String APPREHENDED_FUNDS_CNTY_NO =
		"Apprehended Funds County No:";
	private final static String STATE_COUNTRY = "State/Country:";
	private final static String EXMPT_CHKBOX_NOT_CHK =
		"Exempt checkbox is not checked - Do you want to continue?";
	// defect 6438
	// Issue By cannot be TX - Constants to check for TX and for the 
	//	error message Texas String
	private static final String TX = CommonConstant.STR_TX;
	// Rebuilt Salvage Issue By State/Country error message
	private static final String INVALIDSTATEORCOUNTRY =
		"Invalid state or country.";
	// end defect 6438
	private final static String ENTER_IF_APPRENDEND =
		"Enter if apprehended:";

	// defect 10252
	private final static String MANUFACTURER_BUYBACK =
		"Manufacturer Buyback";
	// end defect 10252  

	private final static String PLT_REPLACEMENT_REQUIRED = "R";
	private final static String PLT_REPLACEMENT_DEFERRED = "D";

	// int
	private int ciAppCnty = 0;

	private JCheckBox ivjchkChargeRebuiltSalvageFee = null;

	/**
	 * FrmTitleAdditionalInfoTTL008 constructor comment.
	 */
	public FrmTitleAdditionalInfoTTL008()
	{
		super();
		initialize();
	}

	/**
	 * FrmTitleAdditionalInfoTTL008 constructor.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmTitleAdditionalInfoTTL008(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmTitleAdditionalInfoTTL008 constructor comment.
	 * 
	 * @param aaOwner java.awt.Frame
	 */
	public FrmTitleAdditionalInfoTTL008(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			//Clear All Fields
			clearAllColor(this);

			if (aaAE.getSource() instanceof JRadioButton)
			{
				if (aaAE.getSource() == getradioNotRebuilt()
						|| aaAE.getSource() == getradioRebuiltSalvage7594PctLoss()
						|| aaAE.getSource()
						== getradioRebuiltSalvage95PctPlusLoss()
						|| aaAE.getSource() == getradioRebuiltSalvageIssuedBy()
						|| aaAE.getSource()
						== getradioRebuiltSalvageLossUnknown())
				{
					handleRadioButtons();
				}
			}
			else if (aaAE.getSource() == getECHButtonPanel().getBtnEnter())
			{
				RTSException leRTSEx = new RTSException();
			
				//set salvage box
				//verify heavy vehicle use tax
				if (getchkVerifiedHeavyVehUseTax().isEnabled()
					&& !getchkVerifiedHeavyVehUseTax().isSelected())
				{
					RTSException leRTSEx204 = new RTSException(204);
					leRTSEx204.displayError(this);
					getchkVerifiedHeavyVehUseTax().requestFocus();
					return;
				}

				// defect 6438
				// Rebuilt Salvage Issue By cannot be TX - error message
				//	 same as RTSI
				if (getradioRebuiltSalvageIssuedBy().isEnabled()
					&& getradioRebuiltSalvageIssuedBy().isSelected())
				{
					// defect 11004 
					// Implement CommonValidations  
					CommonValidations.addRTSExceptionForInvalidCntryStateCntry(gettxtSalvageStateCountry(), 
							leRTSEx, TitleConstant.REQUIRED);

					//	if (gettxtSalvageStateCountry()
					//				.getText()
					//				.length()
					//				!= STATE_MAX_LEN)
					//			{
					//					leRTSEx.addException(
					//					new RTSException(150),
					//					gettxtSalvageStateCountry());
					//				}
					//		else if (
					// end defect 11004 
					if (gettxtSalvageStateCountry()
							.getText()
							.equalsIgnoreCase(
							TX))
					{
						leRTSEx.addException(
							new RTSException(
								RTSException.WARNING_MESSAGE,
								INVALIDSTATEORCOUNTRY,
								MiscellaneousRegConstant.ERROR_TITLE),
							gettxtSalvageStateCountry());
					}
				}
				// end defect 6438
				

				// verify Apprehended County Number if entered && != 0
				String lsAppCnty = gettxtApprhndCntyNo().getText();

				// defect 10290 
				// Use Valid vs. Invalid 
				if (!isApprhndCntyNoValid(lsAppCnty))
				{
					// end defect 10290 
					leRTSEx.addException(
						new RTSException(150),
						gettxtApprhndCntyNo());
				}
				// defect 11051 
				if (getchkChargeRebuiltSalvageFee().isSelected())
				{
					if (getradioNotRebuilt().isSelected()) 
					{
						leRTSEx.addException(
								new RTSException(
										ErrorsConstant.ERR_NUM_MUST_SELECT_REBUILT_SALVAGE),
										getradioNotRebuilt());
					}
				}
				// removed per Monica 
				// do not require salvage type vehicles to pay rebuilt salvage fee
				//				else
				//				{
				//					if (caVehInqData.getNoMFRecs() != 0 && 
				//							UtilityMethods.isSalvageTypeDocTypeCd(ciOrigDocTypeCd))
				//					{
				//						leRTSEx.addException(
				//								new RTSException(
				//										ErrorsConstant.ERR_NUM_MUST_SELECT_REBUILT_SALVAGE_FEE),
				//										getchkChargeRebuiltSalvageFee()); 
				//					}
				//				}
				// end defect 11051 
				
				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}

				if (!verifyApprehendedCounty(lsAppCnty))
				{
					return;
				}

				checkIndiForSpclTtlExm();

				if (checkHQ_Exempt())
				{
					return;
				}

				// defect 5628
				// If NOT process save original docTypeNo
				if (!isRecordPrcs())
				{
					// defect 9971 
					// Not used 
					// setOrigDocType(
					//	caVehInqData
					//		.getMfVehicleData()
					//		.getTitleData()
					//		.getDocTypeCd());
					// end defect 9971 

					setRecordPrcs(true);
				}
				// end defect 5628

				// defect 9664 
				if (isSupOvrdRqdforBuyerTag())
				{
					return;
				}
				// end defect 9664 

				// defect 9971
				// Verify that Owner wants ETitle on Corrected Title
				if (getchkETitle().isEnabled()
					&& getchkETitle().isSelected())
				{
					RTSException leRTSEx1 =
						new RTSException(
							RTSException.CTL001,
							"An Electronic Title will be issued. "
								+ CommonConstant.TXT_CONTINUE_QUESTION,
							ScreenConstant.CTL001_FRM_TITLE);

					if (leRTSEx1.displayError(this) == RTSException.NO)
					{
						return;
					}
				}
				// end defect 9971 
				
				// defect 10007
				if ( getchkTowTruckCert().isEnabled()
					&& !getchkTowTruckCert().isSelected())
				{
					leRTSEx =
						 new RTSException(991);
						//	throw leRTSEx;
						leRTSEx.displayError(this);
						getchkTowTruckCert().requestFocus();
						 return;
				}
				// end defect 10007	
				
				// defect 11051 
				if (getchkChargeRebuiltSalvageFee().isEnabled()
						&& !getchkChargeRebuiltSalvageFee().isSelected()
						&& !getradioNotRebuilt().isSelected())
					{
						RTSException leRTSEx1 =
							new RTSException(
								RTSException.CTL001,
								"Is Charge Rebuilt Salvage Fee applicable? ",
								ScreenConstant.CTL001_FRM_TITLE);

						if (leRTSEx1.displayError(this) == RTSException.YES)
						{
							getchkChargeRebuiltSalvageFee().requestFocus(); 
							return;
						}
					}
				// end defect 11051 
				
				// defect 11264 
				//setDataToDataObject();
				//getController().processData(
				//	AbstractViewController.ENTER,
				//	caVehInqData);
				getController().processData(
						AbstractViewController.ENTER,
						setDataToDataObject());
				// end defect 11264 
			}
			else if (
				aaAE.getSource() == getECHButtonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caVehInqData);
			}
			else if (
				aaAE.getSource() == getECHButtonPanel().getBtnHelp())
			{
				// defect 9368 
				// Modified to use class variable, csTransCd
				// String lsTransCd = getController().getTransCd();  
				if (csTransCd.equals(TransCdConstant.DTAORK)
					|| csTransCd.equals(TransCdConstant.DTANTK))
				{
					RTSHelp.displayHelp(RTSHelp.TTL008B);
				}
				else if (
					csTransCd.equals(TransCdConstant.DTANTD)
						|| csTransCd.equals(TransCdConstant.DTAORD))
				{
					RTSHelp.displayHelp(RTSHelp.TTL008C);
				}
				else if (
					csTransCd.equals(TransCdConstant.TITLE)
						|| csTransCd.equals(TransCdConstant.NONTTL)
						|| csTransCd.equals(TransCdConstant.REJCOR))
				{
					if (UtilityMethods.isMFUP())
					{
						RTSHelp.displayHelp(RTSHelp.TTL008A);
					}
					else
					{
						RTSHelp.displayHelp(RTSHelp.TTL008D);
					}
				}
				// end defect 9368 
			}
			else if (
				aaAE.getSource() == getchkAddlEvidenceSurrendered())
			{
				if (getchkAddlEvidenceSurrendered().isSelected())
				{
					getchkSpecialExamNeeded().setSelected(true);
				}
				else
				{
					if (caVehInqData
						.getMfVehicleData()
						.getTitleData()
						.getTtlExmnIndi()
						== 0)
					{
						getchkSpecialExamNeeded().setSelected(false);
					}
				}
			}

			// defect 8900
			// If user unchecks Exempt Indi, check the enabled 
			// charge fee checkboxes
			else if (aaAE.getSource() == getchkExempt())
			{
				if (getchkExempt().isSelected())
				{
					// defect 9584
					// No longer reset for Exempt 
					// defect 9367
					// if (getchkPltTrnsfrFee().isSelected())
					// {
					// 	    getchkPltTrnsfrFee().setSelected(false);
					// }
					//	if (getchkBuyerTag().isSelected())
					//	{
					//		getchkBuyerTag().setSelected(false);
					//	}
					// end defect 9367
					// end defect 9584

					if (getchkChargeTitleFee().isEnabled())
					{
						getchkChargeTitleFee().setSelected(false);
					}
					if (getchkChargeTitleTERPFee().isEnabled())
					{
						getchkChargeTitleTERPFee().setSelected(false);
					}
					if (getchkChargeRegTransferFee().isEnabled())
					{
						getchkChargeRegTransferFee().setSelected(false);
					}
					if (getchkChargeRegEmissionFee().isEnabled())
					{
						getchkChargeRegEmissionFee().setSelected(false);
					}
					if (getchkChargeAddlTokenTrailerFee().isEnabled())
					{
						getchkChargeAddlTokenTrailerFee().setSelected(
							false);
					}
				}
				else
				{
					if (getchkChargeTitleFee().isEnabled())
					{
						getchkChargeTitleFee().setSelected(true);
					}

					if (getchkChargeTitleTERPFee().isEnabled())
					{
						getchkChargeTitleTERPFee().setSelected(true);
					}
					if (getchkChargeRegTransferFee().isEnabled())
					{
						// defect 9036
						// Check for 'not HQ' instead of '= County'
						// because Regions should work like County.
						if ((caVehInqData.getNoMFRecs() > 0)
							&& !(getController()
								.getTransCode()
								.equals(
									TransCdConstant.REJCOR))
							//&& laOfcData.getOfcIssuanceCd() == 3)
							// && !laOfcData.getOfcIssuanceCd() == 1)
							&& !(SystemProperty.isHQ()))
						{
							getchkChargeRegTransferFee().setSelected(
								true);
						}
						// end defect 9036
					}
					if (getchkChargeRegEmissionFee().isEnabled())
					{
						getchkChargeRegEmissionFee().setSelected(true);
					}
					//token fee may be enabled but is not selected when come into the screen					
					//if (getchkChargeAddlTokenTrailerFee().isEnabled())
					//{
					//	getchkChargeAddlTokenTrailerFee().setSelected(true);
					//}
				}
			}
			// end defect 8900

			// defect 9366 
			// New Plate Desired is enabled if eligible for Plate Transfer. 
			// If user selects 'Charge Plate Transfer Fee', we know that
			// new plates (either Cust Supplied or Issued Inv) will be 
			// on the vehicle (*if NOT Corrected Title*), so set 'New 
			// Plates Desired' selected\disabled.
			// If user de-selects 'Charge Plate Transfer Fee', we do not
			// know if new plates are needed or not, so set 'New Plates 
			// Desired' de-selected\enabled.
			// Note: class boolean cbKeepNewPltsDisabled is set if 'other'
			// reasons cause 'New Plates Desired' to always be disabled.
			// defect 11030 
			else if (aaAE.getSource() == getchkPTOTrnsfr())
			{
				if (getchkPTOTrnsfr().isSelected())
				{
					// end defect 11030 
					
					if (!cbKeepNewPltsDisabled)
					{
						getchkNewPlatesDesired().setSelected(true);
						getchkNewPlatesDesired().setEnabled(false);
					}
				}
				else
				{
					if (!cbKeepNewPltsDisabled)
					{
						getchkNewPlatesDesired().setEnabled(true);
						getchkNewPlatesDesired().setSelected(
							cbNPDPrevSetting);
					}
				}
			}
			// end defect 9366

			// defect 9368 
			else if (aaAE.getSource() == getchkNewPlatesDesired())
			{
				cbNPDPrevSetting =
					getchkNewPlatesDesired().isSelected();
			}
			// end defect 9368
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return boolean to denote if NPD checkbox is applicable
	 * 
	 * @return boolean 
	 */
	private boolean isNewPlatesDesiredApplicable()
	{
		boolean lbReturn = false;

		// defect 9337
		// use constant 'TONLY'
		String lsRegPltCd = caRegData.getRegPltCd();
		if (lsRegPltCd != null
			&& lsRegPltCd.trim().length() > 0
			&& !lsRegPltCd.trim().equals(CommonConstant.TONLY_REGPLTCD))
		{
			lbReturn =
				caRegData.getOffHwyUseIndi() == 0
					&& caRegData.getRegWaivedIndi() == 0
					&& caVehData.getDpsStlnIndi() == 0
					&& caTtlData.getTtlTypeIndi()
						!= TitleTypes.INT_CORRECTED
					&& !PlateTypeCache.isOutOfScopePlate(
						lsRegPltCd.trim());
		}
		// end defect 9337
		return lbReturn;
	}

	/**
	 * Determine if HQ-291/Exempt.
	 * 
	 * @return boolean
	 */
	private boolean checkHQ_Exempt()
	{
		boolean lbRet = false;
		if (SystemProperty.getOfficeIssuanceNo() == 291)
		{
			if (getchkExempt().isSelected() == false)
			{
				// defect 8756
				// set the title to empty so that the default CTL001 
				// title will be used.
				RTSException leRTSExex =
					new RTSException(
						RTSException.CTL001,
						EXMPT_CHKBOX_NOT_CHK,
						CommonConstant.STR_SPACE_EMPTY);
				// defect 8756
				int liRet = leRTSExex.displayError(this);
				if (liRet == RTSException.NO)
				{
					lbRet = true;
				}
				getchkExempt().requestFocus();
			}
		}

		return lbRet;
	}

	/**
	 * Determine if Special Exm is needed.
	 */
	private void checkIndiForSpclTtlExm()
	{
		boolean lbSetSpclTtlExm = false;
		if (getchkFloodDamage().isSelected() == false)
		{
			if (caVehData.getFloodDmgeIndi() == 1)
			{
				lbSetSpclTtlExm = true;
			}
		}

		if (getchkReconstructed().isSelected() == false)
		{
			if (caVehData.getReContIndi() == 1)
			{
				lbSetSpclTtlExm = true;
			}
		}

		if (caVehData.getPrmtReqrdIndi() == 1)
		{
			TitleValidObj laValidObj =
				(TitleValidObj) caVehInqData.getValidationObject();

			MFVehicleData laMfVehData =
				(MFVehicleData) laValidObj.getMfVehOrig();

			if (laMfVehData.getVehicleData().getPrmtReqrdIndi() == 0)
			{
				lbSetSpclTtlExm = true;
			}
		}

		if (getchkDOTProofRequired().isSelected() == false)
		{
			if (caVehData.getDotStndrdsIndi() == 1)
			{
				lbSetSpclTtlExm = true;
			}
		}

		if (lbSetSpclTtlExm)
		{
			caTtlData.setTtlExmnIndi(1);
		}
	}

	/**
	 * Set Rebuilt Salvage radio buttons.
	 */
	private void disableRebuiltSalvage()
	{
		getradioNotRebuilt().setEnabled(false);
		getradioRebuiltSalvageLossUnknown().setEnabled(false);
		getradioRebuiltSalvage7594PctLoss().setEnabled(false);
		getradioRebuiltSalvage95PctPlusLoss().setEnabled(false);
		getradioRebuiltSalvageIssuedBy().setEnabled(false);
		gettxtSalvageStateCountry().setEnabled(false);
	}

	/**
	 * Assign values based upon Dealer Title Data
	 * 
	 *  - ChrgRegTrnsFee
	 *  - Survivorship 
	 *  - ChrgAddlTokenTrlrFee
	 */
	private void doDlrTtl()
	{
		// defect 10290 
		TitleData laTtlData =
			caDlrTtlData.getMFVehicleData().getTitleData();

		// Survivorship  - TitleData 
		getchkSurvivorshipRights().setSelected(
			getchkSurvivorshipRights().isEnabled()
				&& laTtlData.getSurvshpRightsIndi() == 1);

		// ChrgRegTrnsFee - DealerTitleData
		// defect 10949
		// If Dealer GDN is present and after start date, do not charge
		// Reg Transfer Fee
		if (caTtlData.getDlrGdn().length() == 0 ||
			caVehInqData.getRTSEffDt() <
			SystemProperty.getDlrInvalidRegStartDate().
				getYYYYMMDDDate())
		{ 
			getchkChargeRegTransferFee().setSelected(
				getchkChargeRegTransferFee().isEnabled()
					&& caDlrTtlData.getChrgTrnsfrFee() == 1);
		}
		// end defect 10949				
		// ChrgAddlTokenTrlrFee - DealerTitleData
		getchkChargeAddlTokenTrailerFee().setSelected(
			getchkChargeAddlTokenTrailerFee().isEnabled()
				&& caDlrTtlData.getAddlToknFeeIndi() == 1);
		// end defect 10290 
		
		// defect 11051 
		//  removed per VTR
		// For DTA, if RecondCd is set, 
		//      also select Charge Rebuilt Salvage Fee
		//getchkChargeRebuiltSalvageFee().setSelected(
		//		!caVehData.getRecondCd().equals(TitleConstant.NOT_REBUILT));
		// end defect 11051 
	}

	/**
	 * Return the ivjchkAddlEvidenceSurrendered property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkAddlEvidenceSurrendered()
	{
		if (ivjchkAddlEvidenceSurrendered == null)
		{
			try
			{
				ivjchkAddlEvidenceSurrendered = new JCheckBox();
				ivjchkAddlEvidenceSurrendered.setName(
					"ivjchkAddlEvidenceSurrendered");
				ivjchkAddlEvidenceSurrendered.setMnemonic(
					KeyEvent.VK_V);
				ivjchkAddlEvidenceSurrendered.setSize(new Dimension(182, 19));
				ivjchkAddlEvidenceSurrendered.setLocation(new Point(0, 74));
				ivjchkAddlEvidenceSurrendered.setText(
					ADDL_EVDENCE_SURRENDERED);
				ivjchkAddlEvidenceSurrendered.setMaximumSize(
					new java.awt.Dimension(182, 22));
				ivjchkAddlEvidenceSurrendered
					.setHorizontalTextPosition(
					4);
				ivjchkAddlEvidenceSurrendered.setActionCommand(
					ADDL_EDVID_SURR);
				ivjchkAddlEvidenceSurrendered.setMinimumSize(
					new java.awt.Dimension(182, 22));
				// user code begin {1}
				ivjchkAddlEvidenceSurrendered.addKeyListener(this);
				ivjchkAddlEvidenceSurrendered.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkAddlEvidenceSurrendered;
	}

	/**
	 * Return the chkChargeAddlTokenTrailerFee property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkChargeAddlTokenTrailerFee()
	{
		if (ivjchkChargeAddlTokenTrailerFee == null)
		{
			try
			{
				ivjchkChargeAddlTokenTrailerFee = new JCheckBox();
				ivjchkChargeAddlTokenTrailerFee.setName(
					"chkChargeAddlTokenTrailerFee");
				ivjchkChargeAddlTokenTrailerFee.setMnemonic(
					KeyEvent.VK_K);
				ivjchkChargeAddlTokenTrailerFee.setSize(new Dimension(198, 19));
				ivjchkChargeAddlTokenTrailerFee.setLocation(new Point(5, 52));
				ivjchkChargeAddlTokenTrailerFee.setText(
					CHRG_ADDL_TOKEN_TRLR);
				ivjchkChargeAddlTokenTrailerFee.setMaximumSize(
					new java.awt.Dimension(198, 22));
				ivjchkChargeAddlTokenTrailerFee.setActionCommand(
					CHRG_ADDL_TOKEN_TRLR);
				ivjchkChargeAddlTokenTrailerFee.setMinimumSize(
					new java.awt.Dimension(198, 22));
				// user code begin {1}
				// defect 7898
				//ivjchkChargeAddlTokenTrailerFee.addKeyListener(this);
				// end defect 7898
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkChargeAddlTokenTrailerFee;
	}

	/**
	 * This method initializes ivjchkChargeRebuiltSalvageFee	
	 * 	
	 * @return JCheckBox	
	 */
	private JCheckBox getchkChargeRebuiltSalvageFee()
	{
		if (ivjchkChargeRebuiltSalvageFee == null)
		{
			try
			{
				ivjchkChargeRebuiltSalvageFee = new JCheckBox();
				ivjchkChargeRebuiltSalvageFee.setText("Charge Rebuilt Salvage Fee");
				ivjchkChargeRebuiltSalvageFee.setSize(new Dimension(207, 21));
				ivjchkChargeRebuiltSalvageFee.setName("ivjchkChargeRebuiltSalvageFee") ;
				ivjchkChargeRebuiltSalvageFee.setLocation(new Point(10, 52));
				ivjchkChargeRebuiltSalvageFee.setMnemonic(KeyEvent.VK_S);
				ivjchkChargeRebuiltSalvageFee.setHorizontalTextPosition(
						javax.swing.SwingConstants.RIGHT);
				ivjchkChargeRebuiltSalvageFee.addActionListener(this); 
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkChargeRebuiltSalvageFee;
	}
	
	/**
	 * Return the chkChargeRegEmissionFee property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkChargeRegEmissionFee()
	{
		if (ivjchkChargeRegEmissionFee == null)
		{
			try
			{
				ivjchkChargeRegEmissionFee = new JCheckBox();
				// defect 10153
				//ivjchkChargeRegEmissionFee.setBounds(10, 84, 207, 19);
				ivjchkChargeRegEmissionFee.setName(
					"chkChargeRegEmissionFee");
				ivjchkChargeRegEmissionFee.setMnemonic(KeyEvent.VK_A);
				ivjchkChargeRegEmissionFee.setSize(new Dimension(207, 19));
				ivjchkChargeRegEmissionFee.setLocation(new Point(10, 96));
				ivjchkChargeRegEmissionFee.setText(
					CHRG_REGIS_EMISSION_FEE);
				ivjchkChargeRegEmissionFee.setMaximumSize(
					new java.awt.Dimension(153, 22));
				ivjchkChargeRegEmissionFee.setHorizontalTextPosition(4);
				ivjchkChargeRegEmissionFee.setActionCommand(
					CHRG_TNRCC_PNTLY);
				ivjchkChargeRegEmissionFee.setMinimumSize(
					new java.awt.Dimension(153, 22));
				// user code begin {1}
				// defect 7898
				//ivjchkChargeRegEmissionFee.addKeyListener(this);
				// end defect 7898
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkChargeRegEmissionFee;
	}

	/**
	 * Return the chkChargeRegTransferFee property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkChargeRegTransferFee()
	{
		if (ivjchkChargeRegTransferFee == null)
		{
			try
			{
				ivjchkChargeRegTransferFee = new JCheckBox();
				// defect 10153
				//ivjchkChargeRegTransferFee.setBounds(10, 57, 213, 19);
				ivjchkChargeRegTransferFee.setName(
					"chkChargeRegTransferFee");
				ivjchkChargeRegTransferFee.setMnemonic(KeyEvent.VK_R);
				ivjchkChargeRegTransferFee.setSize(new Dimension(213, 19));
				ivjchkChargeRegTransferFee.setLocation(new Point(10, 74));
				ivjchkChargeRegTransferFee.setText(
					CHRG_REGIS_TRANSFER_FEE);
				ivjchkChargeRegTransferFee.setMaximumSize(
					new java.awt.Dimension(213, 22));
				ivjchkChargeRegTransferFee.setHorizontalTextPosition(4);
				ivjchkChargeRegTransferFee.setActionCommand(
					CHRG_REGIS_TRANSFER_FEE);
				ivjchkChargeRegTransferFee.setMinimumSize(
					new java.awt.Dimension(213, 22));
				// user code begin {1}
				// defect 7898
				//ivjchkChargeRegTransferFee.addKeyListener(this);
				// end defect 7898
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkChargeRegTransferFee;
	}

	/**
	 * Return the chkChargeTitleFee property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkChargeTitleFee()
	{
		if (ivjchkChargeTitleFee == null)
		{
			try
			{
				ivjchkChargeTitleFee = new JCheckBox();
				// defect 10153
				//ivjchkChargeTitleFee.setBounds(10, 3, 207, 19);
				ivjchkChargeTitleFee.setName("chkChargeTitleFee");
				ivjchkChargeTitleFee.setMnemonic(KeyEvent.VK_C);
				ivjchkChargeTitleFee.setSize(new Dimension(207, 19));
				ivjchkChargeTitleFee.setLocation(new Point(10, 8));
				ivjchkChargeTitleFee.setText(CHRG_TTL_FEE);
				ivjchkChargeTitleFee.setMaximumSize(
					new java.awt.Dimension(116, 22));
				ivjchkChargeTitleFee.setHorizontalTextPosition(4);
				ivjchkChargeTitleFee.setActionCommand(CHRG_TTL_FEE);
				ivjchkChargeTitleFee.setMinimumSize(
					new java.awt.Dimension(116, 22));
				// user code begin {1}
				// defect 7898
				//ivjchkChargeTitleFee.addKeyListener(this);
				// end defect 7898
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkChargeTitleFee;
	}

	/**
	 * Return the chkChargeTitleTERPFee property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkChargeTitleTERPFee()
	{
		if (ivjchkChargeTitleTERPFee == null)
		{
			try
			{
				ivjchkChargeTitleTERPFee = new JCheckBox();
				// defect 10153
				//ivjchkChargeTitleTERPFee.setBounds(10, 30, 207, 19);
				ivjchkChargeTitleTERPFee.setName(
					"chkChargeTitleTERPFee");
				ivjchkChargeTitleTERPFee.setMnemonic(KeyEvent.VK_E);
				ivjchkChargeTitleTERPFee.setSize(new Dimension(207, 19));
				ivjchkChargeTitleTERPFee.setLocation(new Point(10, 30));
				ivjchkChargeTitleTERPFee.setText(CHRG_TTL_TERP_FEE);
				ivjchkChargeTitleTERPFee.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				ivjchkChargeTitleTERPFee.setActionCommand(
					CHRG_TTL_TERP_FEE);
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
		return ivjchkChargeTitleTERPFee;
	}

	/**
	 * Return the chkDiesel property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkDiesel()
	{
		if (ivjchkDiesel == null)
		{
			try
			{
				ivjchkDiesel = new JCheckBox();
				// defect 10153
				//ivjchkDiesel.setBounds(5, 3, 182, 19);
				ivjchkDiesel.setName("chkDiesel");
				ivjchkDiesel.setMnemonic(KeyEvent.VK_D);
				ivjchkDiesel.setSize(new Dimension(182, 19));
				ivjchkDiesel.setLocation(new Point(0, 8));
				ivjchkDiesel.setText(DIESEL);
				ivjchkDiesel.setMaximumSize(
					new java.awt.Dimension(60, 22));
				ivjchkDiesel.setActionCommand(DIESEL);
				ivjchkDiesel.setMinimumSize(
					new java.awt.Dimension(60, 22));
				// user code begin {1}
				// defect 7898
				//ivjchkDiesel.addKeyListener(this);
				// end defect 7898
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkDiesel;
	}

	/**
	 * Return the chkDOTProofRequired property value.
	 * 
	 * @return JCheckBox
	 * 
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkDOTProofRequired()
	{
		if (ivjchkDOTProofRequired == null)
		{
			try
			{
				ivjchkDOTProofRequired = new JCheckBox();
				// defect 10153
				//ivjchkDOTProofRequired.setBounds(5, 138, 137, 19);
				ivjchkDOTProofRequired.setName("chkDOTProofRequired");
				ivjchkDOTProofRequired.setSize(new Dimension(137, 19));
				ivjchkDOTProofRequired.setLocation(new Point(5, 118));
				ivjchkDOTProofRequired.setText(DOT_PROOF_REQUIRED);
				ivjchkDOTProofRequired.setMaximumSize(
					new java.awt.Dimension(137, 22));
				ivjchkDOTProofRequired.setMinimumSize(
					new java.awt.Dimension(137, 22));
				ivjchkDOTProofRequired.setActionCommand(
					DOT_PROOF_REQUIRED);
				// user code begin {1}
				// defect 7898
				//ivjchkDOTProofRequired.addKeyListener(this);
				// end defect 7898
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkDOTProofRequired;
	}

	/**
	 * Return the ivjchkPTOTrnsfr property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkPTOTrnsfr()
	{
		if (ivjchkPTOTrnsfr == null)
		{
			try
			{
				ivjchkPTOTrnsfr = new JCheckBox();
				ivjchkPTOTrnsfr.setName("chkPltTrnsfrFee");
				ivjchkPTOTrnsfr.setSize(new Dimension(207, 19));
				ivjchkPTOTrnsfr.setLocation(new Point(10, 162));
				ivjchkPTOTrnsfr.setText(PTO_TRNSFR);
				ivjchkPTOTrnsfr.setActionCommand(PTO_TRNSFR);
				ivjchkPTOTrnsfr.setEnabled(false);
				ivjchkPTOTrnsfr.setMnemonic(KeyEvent.VK_F);
				ivjchkPTOTrnsfr.setMaximumSize(
					new java.awt.Dimension(136, 22));
				ivjchkPTOTrnsfr.setHorizontalTextPosition(4);
				ivjchkPTOTrnsfr.setMinimumSize(
					new java.awt.Dimension(136, 22));
				// user code begin {1}
				// Need to change 'New Plates Desired' if Plate Transfer 
				// Indi checked-unchecked				
				ivjchkPTOTrnsfr.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkPTOTrnsfr;
	}

	/**
	 * Return the chkExempt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkExempt()
	{
		if (ivjchkExempt == null)
		{
			try
			{
				ivjchkExempt = new JCheckBox();
				// defect 10153
				//ivjchkExempt.setBounds(5, 138, 182, 19);
				ivjchkExempt.setName("chkExempt");
				ivjchkExempt.setMnemonic(KeyEvent.VK_X);
				ivjchkExempt.setSize(new Dimension(182, 19));
				ivjchkExempt.setLocation(new Point(0, 118));
				ivjchkExempt.setText(EXEMPT);
				ivjchkExempt.setMaximumSize(
					new java.awt.Dimension(68, 22));
				ivjchkExempt.setActionCommand(EXEMPT);
				ivjchkExempt.setMinimumSize(
					new java.awt.Dimension(68, 22));
				// user code begin {1}
				// defect 7898
				//ivjchkExempt.addKeyListener(this);

				// defect 8900
				// Need to check/uncheck fee checkboxes if Exempt Indi 
				// checked-unchecked				
				ivjchkExempt.addActionListener(this);
				// end  defect 8900

				// end defect 7898
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkExempt;
	}

	/**
	 * Return the chkBuyerTag property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkBuyerTag()
	{
		if (ivjchkBuyerTag == null)
		{
			try
			{
				ivjchkBuyerTag = new JCheckBox();
				// defect 10153
				//ivjchkBuyerTag.setBounds(5, 165, 182, 19);
				ivjchkBuyerTag.setBounds(5, 162, 182, 19);
				// end defect 10153
				ivjchkBuyerTag.setName("chkBuyerTag");
				ivjchkBuyerTag.setText(BUYER_TAG);
				ivjchkBuyerTag.setEnabled(false);
				ivjchkBuyerTag.setMaximumSize(
					new java.awt.Dimension(68, 22));
				ivjchkBuyerTag.setActionCommand(BUYER_TAG);
				ivjchkBuyerTag.setMinimumSize(
					new java.awt.Dimension(68, 22));
				// user code begin {1}
				ivjchkBuyerTag.setMnemonic(KeyEvent.VK_T);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkBuyerTag;
	}

	/**
	 * Return the chkFloodDamage property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkFloodDamage()
	{
		if (ivjchkFloodDamage == null)
		{
			try
			{
				ivjchkFloodDamage = new JCheckBox();
				// defect 10153
				//ivjchkFloodDamage.setBounds(9, 30, 170, 19);
				ivjchkFloodDamage.setName("chkFloodDamage");
				ivjchkFloodDamage.setBorder(
					new javax
						.swing
						.plaf
						.BorderUIResource
						.CompoundBorderUIResource(
						null,
						null));
				ivjchkFloodDamage.setMnemonic(KeyEvent.VK_M);
				ivjchkFloodDamage.setSize(new Dimension(170, 19));
				ivjchkFloodDamage.setLocation(new Point(4, 30));
				ivjchkFloodDamage.setText(FLOOD_DAMAGE);
				ivjchkFloodDamage.setMaximumSize(
					new java.awt.Dimension(60, 22));
				ivjchkFloodDamage.setActionCommand(FLOOD_DAMAGE);
				ivjchkFloodDamage.setMinimumSize(
					new java.awt.Dimension(60, 22));
				// user code begin {1}
				ivjchkFloodDamage.setPreferredSize(
					new java.awt.Dimension(60, 24));
				// defect 7898
				//ivjchkFloodDamage.addKeyListener(this);
				// end defect 7898
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkFloodDamage;
	}

	/**
	 * Return the chkGovernmentOwned property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkGovernmentOwned()
	{
		if (ivjchkGovernmentOwned == null)
		{
			try
			{
				ivjchkGovernmentOwned = new JCheckBox();
				// defect 10153
				//ivjchkGovernmentOwned.setBounds(5, 111, 182, 19);
				ivjchkGovernmentOwned.setName("chkGovernmentOwned");
				ivjchkGovernmentOwned.setMnemonic(KeyEvent.VK_G);
				ivjchkGovernmentOwned.setSize(new Dimension(182, 19));
				ivjchkGovernmentOwned.setLocation(new Point(0, 96));
				ivjchkGovernmentOwned.setText(GOVERMENT_OWNED);
				ivjchkGovernmentOwned.setMaximumSize(
					new java.awt.Dimension(137, 22));
				ivjchkGovernmentOwned.setActionCommand(GOVERMENT_OWNED);
				ivjchkGovernmentOwned.setMinimumSize(
					new java.awt.Dimension(137, 22));
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
		return ivjchkGovernmentOwned;
	}

	/**
	 * Return the chkMailInTransaction property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkMailInTransaction()
	{
		if (ivjchkMailInTransaction == null)
		{
			try
			{
				ivjchkMailInTransaction = new JCheckBox();
				// defect 10153
				//ivjchkMailInTransaction.setBounds(5, 30, 133, 19);
				ivjchkMailInTransaction.setName("chkMailInTransaction");
				ivjchkMailInTransaction.setSize(new Dimension(133, 19));
				ivjchkMailInTransaction.setLocation(new Point(5, 30));
				ivjchkMailInTransaction.setText(MAILIN_TRANSACTION);
				ivjchkMailInTransaction.setMaximumSize(
					new java.awt.Dimension(133, 22));
				ivjchkMailInTransaction.setActionCommand(
					MAILIN_TRANSACTION);
				ivjchkMailInTransaction.setMinimumSize(
					new java.awt.Dimension(133, 22));
				ivjchkMailInTransaction.setEnabled(false);
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
		return ivjchkMailInTransaction;
	}

	/**
	 * This method initializes ivjchkManufacturerBuyback
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkManufacturerBuyback()
	{
		if (ivjchkManufacturerBuyback == null)
		{
			ivjchkManufacturerBuyback = new JCheckBox();
			ivjchkManufacturerBuyback.setSize(new Dimension(200, 21));
			ivjchkManufacturerBuyback.setLocation(new Point(5, 140));
			ivjchkManufacturerBuyback.setText(MANUFACTURER_BUYBACK);
		}
		return ivjchkManufacturerBuyback;
	}

	/**
	 * Return the chkNewPlatesDesired property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkNewPlatesDesired()
	{
		if (ivjchkNewPlatesDesired == null)
		{
			try
			{
				ivjchkNewPlatesDesired = new JCheckBox();
				// defect 10153
				//ivjchkNewPlatesDesired.setBounds(10, 165, 207, 19);
				ivjchkNewPlatesDesired.setName("chkNewPlatesDesired");
				ivjchkNewPlatesDesired.setMnemonic(KeyEvent.VK_P);
				ivjchkNewPlatesDesired.setSize(new Dimension(213, 19));
				ivjchkNewPlatesDesired.setLocation(new Point(10, 184));
				ivjchkNewPlatesDesired.setText(NEW_PLATES_DESIRED);
				ivjchkNewPlatesDesired.setMaximumSize(
					new java.awt.Dimension(136, 22));
				ivjchkNewPlatesDesired.setHorizontalTextPosition(4);
				ivjchkNewPlatesDesired.setActionCommand(
					NEW_PLATES_DESIRED);
				ivjchkNewPlatesDesired.setMinimumSize(
					new java.awt.Dimension(136, 22));
				ivjchkNewPlatesDesired.addActionListener(this);
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
		return ivjchkNewPlatesDesired;
	}

	/**
	 * This method initializes ivjchkPriLawEnfVeh
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkPrivateLawEnfVeh()
	{
		if (ivjchkPrivateLawEnfVeh == null)
		{
			ivjchkPrivateLawEnfVeh = new JCheckBox();
			ivjchkPrivateLawEnfVeh.setText(
				"Private Law Enforcement Vehicle");
			ivjchkPrivateLawEnfVeh.setLocation(new Point(10, 140));
			ivjchkPrivateLawEnfVeh.setSize(new Dimension(213, 19));
		}
		return ivjchkPrivateLawEnfVeh;
	}

	/**
	 * Return the chkReconstructed property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkReconstructed()
	{
		if (ivjchkReconstructed == null)
		{
			try
			{
				ivjchkReconstructed = new JCheckBox();
				// defect 10153
				//ivjchkReconstructed.setBounds(5, 57, 182, 19);
				ivjchkReconstructed.setName("chkReconstructed");
				ivjchkReconstructed.setSize(new Dimension(182, 19));
				ivjchkReconstructed.setLocation(new Point(0, 52));
				ivjchkReconstructed.setText(RECONSTRUCTED);
				ivjchkReconstructed.setMaximumSize(
					new java.awt.Dimension(109, 22));
				ivjchkReconstructed.setMinimumSize(
					new java.awt.Dimension(109, 22));
				ivjchkReconstructed.setActionCommand(RECONSTRUCTED);
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
		return ivjchkReconstructed;
	}

	/**
	 * Return the chkSpecialExamNeeded property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSpecialExamNeeded()
	{
		if (ivjchkSpecialExamNeeded == null)
		{
			try
			{
				ivjchkSpecialExamNeeded = new JCheckBox();
				// defect 10139
				//ivjchkSpecialExamNeeded.setBounds(5, 3, 186, 19);
				ivjchkSpecialExamNeeded.setName("chkSpecialExamNeeded");
				ivjchkSpecialExamNeeded.setMnemonic(KeyEvent.VK_L);
				ivjchkSpecialExamNeeded.setSize(new Dimension(186, 19));
				ivjchkSpecialExamNeeded.setLocation(new Point(5, 8));
				ivjchkSpecialExamNeeded.setText(SPECIAL_EXAM_NEEDED);
				ivjchkSpecialExamNeeded.setMaximumSize(
					new java.awt.Dimension(186, 22));
				ivjchkSpecialExamNeeded.setActionCommand(
					SPECIAL_EXAM_NEEDED);
				ivjchkSpecialExamNeeded.setMinimumSize(
					new java.awt.Dimension(186, 22));
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
		return ivjchkSpecialExamNeeded;
	}

	/**
	 * Return the chkSurvivorshipRights property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSurvivorshipRights()
	{
		if (ivjchkSurvivorshipRights == null)
		{
			try
			{
				ivjchkSurvivorshipRights = new JCheckBox();
				// defect 10153
				//ivjchkSurvivorshipRights.setBounds(10, 111, 207, 19);
				ivjchkSurvivorshipRights.setName(
					"chkSurvivorshipRights");
				ivjchkSurvivorshipRights.setMnemonic(KeyEvent.VK_I);
				ivjchkSurvivorshipRights.setSize(new Dimension(207, 19));
				ivjchkSurvivorshipRights.setLocation(new Point(10, 118));
				ivjchkSurvivorshipRights.setText(SURVIVORSHIP_RIGHTS);
				ivjchkSurvivorshipRights.setMaximumSize(
					new java.awt.Dimension(135, 22));
				ivjchkSurvivorshipRights.setActionCommand(
					SURVIVORSHIP_RIGHTS);
				ivjchkSurvivorshipRights.setSelected(false);
				ivjchkSurvivorshipRights.setMinimumSize(
					new java.awt.Dimension(135, 22));
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
		return ivjchkSurvivorshipRights;
	}

	/**
	 * Return the chkVerifiedHeavyVehUseTax property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkVerifiedHeavyVehUseTax()
	{
		if (ivjchkVerifiedHeavyVehUseTax == null)
		{
			try
			{
				ivjchkVerifiedHeavyVehUseTax = new JCheckBox();
				// defect 10153
				//ivjchkVerifiedHeavyVehUseTax.setBounds(5, 84, 200, 19);
				ivjchkVerifiedHeavyVehUseTax.setName(
					"chkVerifiedHeavyVehUseTax");
				ivjchkVerifiedHeavyVehUseTax.setSize(new Dimension(200, 19));
				ivjchkVerifiedHeavyVehUseTax.setLocation(new Point(5, 74));
				ivjchkVerifiedHeavyVehUseTax.setText(
					VERIFY_HVY_VEH_USE_TAX);
				ivjchkVerifiedHeavyVehUseTax.setMaximumSize(
					new java.awt.Dimension(200, 22));
				ivjchkVerifiedHeavyVehUseTax.setMinimumSize(
					new java.awt.Dimension(200, 22));
				ivjchkVerifiedHeavyVehUseTax.setActionCommand(
					VERIFY_HVY_VEH_USE_TAX);
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
		return ivjchkVerifiedHeavyVehUseTax;
	}

	/**
	 * Return the chkVINCertificationWaived property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkVINCertificationWaived()
	{
		if (ivjchkVINCertificationWaived == null)
		{
			try
			{
				ivjchkVINCertificationWaived = new JCheckBox();
				// defect 10153
				//ivjchkVINCertificationWaived.setBounds(5, 111, 161, 19);
				ivjchkVINCertificationWaived.setName(
					"chkVINCertificationWaived");
				ivjchkVINCertificationWaived.setMnemonic(KeyEvent.VK_W);
				ivjchkVINCertificationWaived.setSize(new Dimension(161, 19));
				ivjchkVINCertificationWaived.setLocation(new Point(5, 96));
				ivjchkVINCertificationWaived.setText(
					VIN_CERTIFICATION_WAIVED);
				ivjchkVINCertificationWaived.setMaximumSize(
					new java.awt.Dimension(161, 22));
				ivjchkVINCertificationWaived.setActionCommand(
					VIN_CERTIFICATION_WAIVED);
				ivjchkVINCertificationWaived.setMinimumSize(
					new java.awt.Dimension(161, 22));
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
		return ivjchkVINCertificationWaived;
	}

	/**
	 * Return the ECHButtonPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getECHButtonPanel()
	{
		if (ivjECHButtonPanel == null)
		{
			try
			{
				ivjECHButtonPanel = new ButtonPanel();
				ivjECHButtonPanel.setBounds(183, 461, 273, 44);
				ivjECHButtonPanel.setName("ECHButtonPanel");
				ivjECHButtonPanel.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjECHButtonPanel.setMinimumSize(
					new java.awt.Dimension(197, 25));
				// user code begin {1}
				ivjECHButtonPanel.addActionListener(this);
				ivjECHButtonPanel.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjECHButtonPanel;
	}

	/**
	 * Return the FrmTitleAdditionalInfoTTL008ContentPane1 property
	 *  value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmTitleAdditionalInfoTTL008ContentPane1()
	{
		if (ivjFrmTitleAdditionalInfoTTL008ContentPane1 == null)
		{
			try
			{
				ivjFrmTitleAdditionalInfoTTL008ContentPane1 =
					new JPanel();
				ivjFrmTitleAdditionalInfoTTL008ContentPane1.setName(
					"FrmTitleAdditionalInfoTTL008ContentPane1");
				ivjFrmTitleAdditionalInfoTTL008ContentPane1.setLayout(null);
				ivjFrmTitleAdditionalInfoTTL008ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmTitleAdditionalInfoTTL008ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(750, 563));

				ivjFrmTitleAdditionalInfoTTL008ContentPane1.add(getpnlGrpFeeTypes(), null);
				ivjFrmTitleAdditionalInfoTTL008ContentPane1.add(getpnlGrpRebuiltSalvage(), null);
				ivjFrmTitleAdditionalInfoTTL008ContentPane1.add(getJPanel1(), null);
				ivjFrmTitleAdditionalInfoTTL008ContentPane1.add(getECHButtonPanel(), null);
				ivjFrmTitleAdditionalInfoTTL008ContentPane1.add(getpnlGrpTireType(), null);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmTitleAdditionalInfoTTL008ContentPane1;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("JPanel1");
				// defect 8416
				//	Add title to border
				//ivjJPanel1.setBorder(
				//	javax.swing.BorderFactory.createEtchedBorder(
				//		javax.swing.border.EtchedBorder.LOWERED));
				ivjJPanel1.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						ENTER_IF_APPRENDEND));
				// end defect 8416
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(207, 50));

				java
					.awt
					.GridBagConstraints constraintsstcLblApprehendedFundsCountyNo =
					new java.awt.GridBagConstraints();
				constraintsstcLblApprehendedFundsCountyNo.gridx = 1;
				constraintsstcLblApprehendedFundsCountyNo.gridy = 1;
				constraintsstcLblApprehendedFundsCountyNo.ipadx = 14;
				constraintsstcLblApprehendedFundsCountyNo.ipady = 7;
				constraintsstcLblApprehendedFundsCountyNo.insets =
					new java.awt.Insets(18, 9, 17, 1);
				getJPanel1().add(
					getstcLblApprehendedFundsCountyNo(),
					constraintsstcLblApprehendedFundsCountyNo);

				java.awt.GridBagConstraints constraintstxtApprhndCntyNo =
					new java.awt.GridBagConstraints();
				constraintstxtApprhndCntyNo.gridx = 2;
				constraintstxtApprhndCntyNo.gridy = 1;
				constraintstxtApprhndCntyNo.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtApprhndCntyNo.weightx = 1.0;
				constraintstxtApprhndCntyNo.ipadx = 36;
				constraintstxtApprhndCntyNo.insets =
					new java.awt.Insets(18, 1, 18, 29);
				getJPanel1().add(
					gettxtApprhndCntyNo(),
					constraintstxtApprhndCntyNo);
				// user code begin {1}
				ivjJPanel1.setBounds(83, 372, 273, 82);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the pnlGrpFeeTypes property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlGrpFeeTypes()
	{
		if (ivjpnlGrpFeeTypes == null)
		{
			try
			{
				ivjpnlGrpFeeTypes = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints58 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints59 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints60 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints61 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints63 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints62 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints64 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints65 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints67 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints66 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints68 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints70 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints71 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints69 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints72 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints73 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints75 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints76 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints74 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints68.insets =
					new java.awt.Insets(4, 5, 3, 0);
				consGridBagConstraints68.ipady = -5;
				consGridBagConstraints68.gridy = 3;
				consGridBagConstraints68.gridx = 1;
				consGridBagConstraints61.insets =
					new java.awt.Insets(4, 11, 3, 10);
				consGridBagConstraints61.ipady = -5;
				consGridBagConstraints61.ipadx = 44;
				consGridBagConstraints61.gridy = 3;
				consGridBagConstraints61.gridx = 0;
				consGridBagConstraints73.insets =
					new java.awt.Insets(4, 0, 3, 54);
				consGridBagConstraints73.ipady = -5;
				consGridBagConstraints73.ipadx = 22;
				consGridBagConstraints73.gridwidth = 2;
				consGridBagConstraints73.gridy = 2;
				consGridBagConstraints73.gridx = 2;
				consGridBagConstraints65.insets =
					new java.awt.Insets(14, 5, 3, 0);
				consGridBagConstraints65.ipady = -5;
				consGridBagConstraints65.ipadx = 18;
				consGridBagConstraints65.gridwidth = 2;
				consGridBagConstraints65.gridy = 0;
				consGridBagConstraints65.gridx = 1;
				consGridBagConstraints67.insets =
					new java.awt.Insets(4, 5, 3, 2);
				consGridBagConstraints67.ipady = -5;
				consGridBagConstraints67.gridy = 2;
				consGridBagConstraints67.gridx = 1;
				consGridBagConstraints70.insets =
					new java.awt.Insets(4, 5, 3, 63);
				consGridBagConstraints70.ipady = -5;
				consGridBagConstraints70.gridy = 5;
				consGridBagConstraints70.gridx = 1;
				consGridBagConstraints63.insets =
					new java.awt.Insets(4, 11, 3, 10);
				consGridBagConstraints63.ipady = -5;
				consGridBagConstraints63.ipadx = 72;
				consGridBagConstraints63.gridy = 5;
				consGridBagConstraints63.gridx = 0;
				consGridBagConstraints60.insets =
					new java.awt.Insets(4, 11, 3, 4);
				consGridBagConstraints60.ipady = -5;
				consGridBagConstraints60.gridy = 2;
				consGridBagConstraints60.gridx = 0;
				consGridBagConstraints72.insets =
					new java.awt.Insets(5, 0, 4, 48);
				consGridBagConstraints72.ipady = 3;
				consGridBagConstraints72.ipadx = 36;
				consGridBagConstraints72.gridy = 1;
				consGridBagConstraints72.gridx = 3;
				consGridBagConstraints62.insets =
					new java.awt.Insets(4, 11, 3, 10);
				consGridBagConstraints62.ipady = -5;
				consGridBagConstraints62.ipadx = 28;
				consGridBagConstraints62.gridy = 4;
				consGridBagConstraints62.gridx = 0;
				consGridBagConstraints64.insets =
					new java.awt.Insets(3, 11, 11, 10);
				consGridBagConstraints64.ipady = -5;
				consGridBagConstraints64.ipadx = 71;
				consGridBagConstraints64.gridy = 6;
				consGridBagConstraints64.gridx = 0;
				consGridBagConstraints58.insets =
					new java.awt.Insets(14, 11, 3, 10);
				consGridBagConstraints58.ipady = -5;
				consGridBagConstraints58.ipadx = 91;
				consGridBagConstraints58.gridy = 0;
				consGridBagConstraints58.gridx = 0;
				consGridBagConstraints59.insets =
					new java.awt.Insets(4, 11, 3, 10);
				consGridBagConstraints59.ipady = -3;
				consGridBagConstraints59.ipadx = 58;
				consGridBagConstraints59.gridy = 1;
				consGridBagConstraints59.gridx = 0;
				consGridBagConstraints66.insets =
					new java.awt.Insets(5, 5, 4, 67);
				consGridBagConstraints66.ipady = -5;
				consGridBagConstraints66.gridy = 1;
				consGridBagConstraints66.gridx = 1;
				consGridBagConstraints69.insets =
					new java.awt.Insets(4, 5, 3, 39);
				consGridBagConstraints69.ipady = -5;
				consGridBagConstraints69.gridy = 4;
				consGridBagConstraints69.gridx = 1;
				consGridBagConstraints75.insets =
					new java.awt.Insets(4, 0, 3, 45);
				consGridBagConstraints75.ipady = -5;
				consGridBagConstraints75.ipadx = 3;
				consGridBagConstraints75.gridwidth = 2;
				consGridBagConstraints75.gridy = 4;
				consGridBagConstraints75.gridx = 2;
				consGridBagConstraints76.insets =
					new java.awt.Insets(4, 0, 3, 54);
				consGridBagConstraints76.ipady = -5;
				consGridBagConstraints76.ipadx = 63;
				consGridBagConstraints76.gridwidth = 2;
				consGridBagConstraints76.gridy = 5;
				consGridBagConstraints76.gridx = 2;
				consGridBagConstraints71.insets =
					new java.awt.Insets(14, 0, 3, 54);
				consGridBagConstraints71.ipady = -5;
				consGridBagConstraints71.ipadx = 71;
				consGridBagConstraints71.gridwidth = 2;
				consGridBagConstraints71.gridy = 0;
				consGridBagConstraints71.gridx = 2;
				consGridBagConstraints74.insets =
					new java.awt.Insets(4, 0, 3, 3);
				consGridBagConstraints74.ipady = -5;
				consGridBagConstraints74.gridwidth = 2;
				consGridBagConstraints74.gridy = 3;
				consGridBagConstraints74.gridx = 2;
				ivjpnlGrpFeeTypes.setName("pnlGrpFeeTypes");
				ivjpnlGrpFeeTypes.setBorder(
					new javax.swing.border.EtchedBorder());
				ivjpnlGrpFeeTypes.setLayout(null);
				ivjpnlGrpFeeTypes.add(getJPanel12(), null);
				ivjpnlGrpFeeTypes.add(getJPanel2(), null);
				ivjpnlGrpFeeTypes.add(getJPanel3(), null);
				ivjpnlGrpFeeTypes.setBounds(2, 0, 631, 228);
				ivjpnlGrpFeeTypes.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlGrpFeeTypes.setMinimumSize(
					new java.awt.Dimension(623, 180));

				ivjpnlGrpFeeTypes.setBorder(
					BorderFactory.createTitledBorder(
						caEtched,
						CHK_IF_APPLICABLE));
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlGrpFeeTypes;
	}

	/**
	 * Return the pnlGrpRebuiltSalvage property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlGrpRebuiltSalvage()
	{
		if (ivjpnlGrpRebuiltSalvage == null)
		{
			try
			{
				ivjpnlGrpRebuiltSalvage = new JPanel();
				ivjpnlGrpRebuiltSalvage.setName("pnlGrpRebuiltSalvage");
				ivjpnlGrpRebuiltSalvage.setBorder(
					new javax.swing.border.EtchedBorder());
				ivjpnlGrpRebuiltSalvage.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlGrpRebuiltSalvage.setMinimumSize(
					new java.awt.Dimension(0, 0));

				java.awt.GridBagConstraints constraintsradioNotRebuilt =
					new java.awt.GridBagConstraints();
				constraintsradioNotRebuilt.gridx = 1;
				constraintsradioNotRebuilt.gridy = 1;
				constraintsradioNotRebuilt.ipadx = 6;
				constraintsradioNotRebuilt.insets =
					new java.awt.Insets(19, 52, 3, 147);
				getpnlGrpRebuiltSalvage().add(
					getradioNotRebuilt(),
					constraintsradioNotRebuilt);

				java
					.awt
					.GridBagConstraints constraintsradioRebuiltSalvageLossUnknown =
					new java.awt.GridBagConstraints();
				constraintsradioRebuiltSalvageLossUnknown.gridx = 2;
				constraintsradioRebuiltSalvageLossUnknown.gridy = 1;
				constraintsradioRebuiltSalvageLossUnknown.gridwidth = 2;
				constraintsradioRebuiltSalvageLossUnknown.ipadx = 21;
				constraintsradioRebuiltSalvageLossUnknown.insets =
					new java.awt.Insets(19, 24, 3, 80);
				getpnlGrpRebuiltSalvage().add(
					getradioRebuiltSalvageLossUnknown(),
					constraintsradioRebuiltSalvageLossUnknown);

				java
					.awt
					.GridBagConstraints constraintsradioRebuiltSalvage7594PctLoss =
					new java.awt.GridBagConstraints();
				constraintsradioRebuiltSalvage7594PctLoss.gridx = 1;
				constraintsradioRebuiltSalvage7594PctLoss.gridy = 2;
				constraintsradioRebuiltSalvage7594PctLoss.ipadx = 21;
				constraintsradioRebuiltSalvage7594PctLoss.insets =
					new java.awt.Insets(4, 52, 4, 24);
				getpnlGrpRebuiltSalvage().add(
					getradioRebuiltSalvage7594PctLoss(),
					constraintsradioRebuiltSalvage7594PctLoss);

				java
					.awt
					.GridBagConstraints constraintsradioRebuiltSalvage95PctPlusLoss =
					new java.awt.GridBagConstraints();
				constraintsradioRebuiltSalvage95PctPlusLoss.gridx = 2;
				constraintsradioRebuiltSalvage95PctPlusLoss.gridy = 2;
				constraintsradioRebuiltSalvage95PctPlusLoss.gridwidth =
					2;
				constraintsradioRebuiltSalvage95PctPlusLoss.ipadx = 23;
				constraintsradioRebuiltSalvage95PctPlusLoss.insets =
					new java.awt.Insets(4, 24, 4, 80);
				getpnlGrpRebuiltSalvage().add(
					getradioRebuiltSalvage95PctPlusLoss(),
					constraintsradioRebuiltSalvage95PctPlusLoss);

				java
					.awt
					.GridBagConstraints constraintsradioRebuiltSalvageIssuedBy =
					new java.awt.GridBagConstraints();
				constraintsradioRebuiltSalvageIssuedBy.gridx = 1;
				constraintsradioRebuiltSalvageIssuedBy.gridy = 3;
				constraintsradioRebuiltSalvageIssuedBy.ipadx = 38;
				constraintsradioRebuiltSalvageIssuedBy.insets =
					new java.awt.Insets(4, 52, 20, 24);
				getpnlGrpRebuiltSalvage().add(
					getradioRebuiltSalvageIssuedBy(),
					constraintsradioRebuiltSalvageIssuedBy);

				java
					.awt
					.GridBagConstraints constraintsstcLblStateCountry =
					new java.awt.GridBagConstraints();
				constraintsstcLblStateCountry.gridx = 2;
				constraintsstcLblStateCountry.gridy = 3;
				constraintsstcLblStateCountry.ipady = 5;
				constraintsstcLblStateCountry.insets =
					new java.awt.Insets(4, 25, 23, 4);
				getpnlGrpRebuiltSalvage().add(
					getstcLblStateCountry(),
					constraintsstcLblStateCountry);

				java
					.awt
					.GridBagConstraints constraintstxtSalvageStateCountry =
					new java.awt.GridBagConstraints();
				constraintstxtSalvageStateCountry.gridx = 3;
				constraintstxtSalvageStateCountry.gridy = 3;
				constraintstxtSalvageStateCountry.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtSalvageStateCountry.weightx = 1.0;
				constraintstxtSalvageStateCountry.ipadx = 38;
				constraintstxtSalvageStateCountry.ipady = -1;
				constraintstxtSalvageStateCountry.insets =
					new java.awt.Insets(4, 5, 23, 175);
				getpnlGrpRebuiltSalvage().add(
					gettxtSalvageStateCountry(),
					constraintstxtSalvageStateCountry);
				// user code begin {1}
				ivjpnlGrpRebuiltSalvage.setBounds(2, 237, 631, 119);
				ivjpnlGrpRebuiltSalvage.setBorder(
					BorderFactory.createTitledBorder(
						caEtched,
						SEL_REBUILT_SLVG));

				caButtonGroup1 = new RTSButtonGroup();
				caButtonGroup1.add(getradioNotRebuilt());
				caButtonGroup1.add(getradioRebuiltSalvageLossUnknown());
				caButtonGroup1.add(getradioRebuiltSalvage7594PctLoss());
				caButtonGroup1.add(
					getradioRebuiltSalvage95PctPlusLoss());
				caButtonGroup1.add(getradioRebuiltSalvageIssuedBy());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlGrpRebuiltSalvage;
	}

	/**
	 * Return the pnlGrpTireType property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlGrpTireType()
	{
		if (ivjpnlGrpTireType == null)
		{
			try
			{
				ivjpnlGrpTireType = new JPanel();
				ivjpnlGrpTireType.setName("pnlGrpTireType");
				ivjpnlGrpTireType.setBorder(
					new javax.swing.border.EtchedBorder());
				ivjpnlGrpTireType.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlGrpTireType.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlGrpTireType.setMinimumSize(
					new java.awt.Dimension(106, 86));

				java.awt.GridBagConstraints constraintsradioPneumatic =
					new java.awt.GridBagConstraints();
				constraintsradioPneumatic.gridx = 1;
				constraintsradioPneumatic.gridy = 1;
				constraintsradioPneumatic.insets =
					new java.awt.Insets(20, 13, 4, 13);
				getpnlGrpTireType().add(
					getradioPneumatic(),
					constraintsradioPneumatic);

				java.awt.GridBagConstraints constraintsradioSolid =
					new java.awt.GridBagConstraints();
				constraintsradioSolid.gridx = 1;
				constraintsradioSolid.gridy = 2;
				constraintsradioSolid.insets =
					new java.awt.Insets(4, 13, 21, 46);
				getpnlGrpTireType().add(
					getradioSolid(),
					constraintsradioSolid);
				// user code begin {1}
				ivjpnlGrpTireType.setBounds(439, 372, 115, 82);
				ivjpnlGrpTireType.setBorder(
					BorderFactory.createTitledBorder(
						caEtched,
						SEL_TIRE_TYPE));

				caButtonGroup2 = new RTSButtonGroup();
				caButtonGroup2.add(getradioPneumatic());
				caButtonGroup2.add(getradioSolid());

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlGrpTireType;
	}

	/**
	 * Return the radioNotRebuilt property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioNotRebuilt()
	{
		if (ivjradioNotRebuilt == null)
		{
			try
			{
				ivjradioNotRebuilt = new javax.swing.JRadioButton();
				ivjradioNotRebuilt.setName("radioNotRebuilt");
				ivjradioNotRebuilt.setMnemonic(KeyEvent.VK_N);
				ivjradioNotRebuilt.setText(NOT_REBUILD);
				ivjradioNotRebuilt.setMaximumSize(
					new java.awt.Dimension(86, 22));
				ivjradioNotRebuilt.setActionCommand(NOT_REBUILD);
				ivjradioNotRebuilt.setMinimumSize(
					new java.awt.Dimension(86, 22));
				// user code begin {1}
				ivjradioNotRebuilt.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioNotRebuilt;
	}

	/**
	 * Return the radioPneumatic property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioPneumatic()
	{
		if (ivjradioPneumatic == null)
		{
			try
			{
				ivjradioPneumatic = new javax.swing.JRadioButton();
				ivjradioPneumatic.setName("radioPneumatic");
				ivjradioPneumatic.setText(PNEUMATIC);
				ivjradioPneumatic.setMaximumSize(
					new java.awt.Dimension(86, 22));
				ivjradioPneumatic.setMinimumSize(
					new java.awt.Dimension(86, 22));
				ivjradioPneumatic.setActionCommand(PNEUMATIC);
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
		return ivjradioPneumatic;
	}

	/**
	 * Return the radioRebuiltSalvage7594PctLoss property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JRadioButton getradioRebuiltSalvage7594PctLoss()
	{
		if (ivjradioRebuiltSalvage7594PctLoss == null)
		{
			try
			{
				ivjradioRebuiltSalvage7594PctLoss =
					new javax.swing.JRadioButton();
				ivjradioRebuiltSalvage7594PctLoss.setName(
					"radioRebuiltSalvage7594PctLoss");
				ivjradioRebuiltSalvage7594PctLoss.setMnemonic(
					KeyEvent.VK_7);
				ivjradioRebuiltSalvage7594PctLoss.setText(
					REBUILT_SLVG_75_94);
				ivjradioRebuiltSalvage7594PctLoss.setMaximumSize(
					new java.awt.Dimension(194, 22));
				ivjradioRebuiltSalvage7594PctLoss.setActionCommand(
					REBUILT_SLVG_75_94);
				ivjradioRebuiltSalvage7594PctLoss.setMinimumSize(
					new java.awt.Dimension(194, 22));
				// user code begin {1}
				ivjradioRebuiltSalvage7594PctLoss.addActionListener(
					this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioRebuiltSalvage7594PctLoss;
	}

	/**
	 * Return the radioRebuiltSalvage95PctPlusLoss property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JRadioButton getradioRebuiltSalvage95PctPlusLoss()
	{
		if (ivjradioRebuiltSalvage95PctPlusLoss == null)
		{
			try
			{
				ivjradioRebuiltSalvage95PctPlusLoss =
					new javax.swing.JRadioButton();
				ivjradioRebuiltSalvage95PctPlusLoss.setName(
					"radioRebuiltSalvage95PctPlusLoss");
				ivjradioRebuiltSalvage95PctPlusLoss.setMnemonic(
					KeyEvent.VK_9);
				ivjradioRebuiltSalvage95PctPlusLoss.setText(
					REBUILD_SLVG_95_LOSS);
				ivjradioRebuiltSalvage95PctPlusLoss.setMaximumSize(
					new java.awt.Dimension(204, 22));
				ivjradioRebuiltSalvage95PctPlusLoss.setActionCommand(
					REBUILD_SLVG_95_LOSS);
				ivjradioRebuiltSalvage95PctPlusLoss.setMinimumSize(
					new java.awt.Dimension(204, 22));
				// user code begin {1}
				ivjradioRebuiltSalvage95PctPlusLoss.addActionListener(
					this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioRebuiltSalvage95PctPlusLoss;
	}

	/**
	 * Return the radioRebuiltSalvageIssuedBy property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioRebuiltSalvageIssuedBy()
	{
		if (ivjradioRebuiltSalvageIssuedBy == null)
		{
			try
			{
				ivjradioRebuiltSalvageIssuedBy =
					new javax.swing.JRadioButton();
				ivjradioRebuiltSalvageIssuedBy.setName(
					"radioRebuiltSalvageIssuedBy");
				ivjradioRebuiltSalvageIssuedBy.setMnemonic(
					KeyEvent.VK_Y);
				ivjradioRebuiltSalvageIssuedBy.setText(
					REBUILT_SLVG_ISSUED_BY);
				ivjradioRebuiltSalvageIssuedBy.setMaximumSize(
					new java.awt.Dimension(177, 22));
				ivjradioRebuiltSalvageIssuedBy.setActionCommand(
					REBUILT_SLVG_ISSUED_BY);
				ivjradioRebuiltSalvageIssuedBy.setMinimumSize(
					new java.awt.Dimension(177, 22));
				// user code begin {1}
				ivjradioRebuiltSalvageIssuedBy.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioRebuiltSalvageIssuedBy;
	}

	/**
	 * Return the radioRebuiltSalvageLossUnknown property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JRadioButton getradioRebuiltSalvageLossUnknown()
	{
		if (ivjradioRebuiltSalvageLossUnknown == null)
		{
			try
			{
				ivjradioRebuiltSalvageLossUnknown =
					new javax.swing.JRadioButton();
				ivjradioRebuiltSalvageLossUnknown.setName(
					"radioRebuiltSalvageLossUnknown");
				ivjradioRebuiltSalvageLossUnknown.setMnemonic(
					KeyEvent.VK_B);
				ivjradioRebuiltSalvageLossUnknown.setText(
					REBUILD_SLVG_LOSS_UNKNOWN);
				ivjradioRebuiltSalvageLossUnknown.setMaximumSize(
					new java.awt.Dimension(206, 22));
				ivjradioRebuiltSalvageLossUnknown.setActionCommand(
					REBUILD_SLVG_LOSS_UNKNOWN);
				ivjradioRebuiltSalvageLossUnknown.setMinimumSize(
					new java.awt.Dimension(206, 22));
				// user code begin {1}
				ivjradioRebuiltSalvageLossUnknown.addActionListener(
					this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioRebuiltSalvageLossUnknown;
	}

	/**
	 * Return the radioSolid property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioSolid()
	{
		if (ivjradioSolid == null)
		{
			try
			{
				ivjradioSolid = new javax.swing.JRadioButton();
				ivjradioSolid.setName("radioSolid");
				//ivjradioSolid.setMnemonic(KeyEvent.VK_S);
				ivjradioSolid.setText(SOLID);
				ivjradioSolid.setMaximumSize(
					new java.awt.Dimension(53, 22));
				ivjradioSolid.setActionCommand(SOLID);
				ivjradioSolid.setMinimumSize(
					new java.awt.Dimension(53, 22));
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
		return ivjradioSolid;
	}

	/**
	 * Return the stcLblApprehendedFundsCountyNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblApprehendedFundsCountyNo()
	{
		if (ivjstcLblApprehendedFundsCountyNo == null)
		{
			try
			{
				ivjstcLblApprehendedFundsCountyNo =
					new javax.swing.JLabel();
				ivjstcLblApprehendedFundsCountyNo.setName(
					"stcLblApprehendedFundsCountyNo");
				ivjstcLblApprehendedFundsCountyNo.setDisplayedMnemonic(
					KeyEvent.VK_U);
				ivjstcLblApprehendedFundsCountyNo.setText(
					APPREHENDED_FUNDS_CNTY_NO);
				ivjstcLblApprehendedFundsCountyNo.setMaximumSize(
					new java.awt.Dimension(176, 14));
				ivjstcLblApprehendedFundsCountyNo.setMinimumSize(
					new java.awt.Dimension(176, 14));
				// user code begin {1}
				ivjstcLblApprehendedFundsCountyNo.setLabelFor(
					gettxtApprhndCntyNo());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblApprehendedFundsCountyNo;
	}

	/**
	 * Return the stcLblStateCountry property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblStateCountry()
	{
		if (ivjstcLblStateCountry == null)
		{
			try
			{
				ivjstcLblStateCountry = new javax.swing.JLabel();
				ivjstcLblStateCountry.setName("stcLblStateCountry");
				ivjstcLblStateCountry.setText(STATE_COUNTRY);
				ivjstcLblStateCountry.setMaximumSize(
					new java.awt.Dimension(80, 14));
				ivjstcLblStateCountry.setMinimumSize(
					new java.awt.Dimension(80, 14));
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
		return ivjstcLblStateCountry;
	}

	/**
	 * Return the txtApprhndCntyNo property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtApprhndCntyNo()
	{
		if (ivjtxtApprhndCntyNo == null)
		{
			try
			{
				ivjtxtApprhndCntyNo = new RTSInputField();
				ivjtxtApprhndCntyNo.setName("txtApprhndCntyNo");
				ivjtxtApprhndCntyNo.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtApprhndCntyNo.setMaxLength(CNTYNO_MAX_LEN);
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
		return ivjtxtApprhndCntyNo;
	}

	/**
	 * Return the txtSalvageStateCountry property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtSalvageStateCountry()
	{
		if (ivjtxtSalvageStateCountry == null)
		{
			try
			{
				ivjtxtSalvageStateCountry = new RTSInputField();
				ivjtxtSalvageStateCountry.setName(
					"txtSalvageStateCountry");
				
				ivjtxtSalvageStateCountry.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtSalvageStateCountry.setMaxLength(
						TitleConstant.STATE_CNTRY_MAX_LEN);
						
				ivjtxtSalvageStateCountry.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
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
		return ivjtxtSalvageStateCountry;
	}

	/**
	 * Setup ETtlChkBox   
	 * 
	 */
	private void setupETtlChkbox()
	{
		TitleValidObj laValidObj =
			(TitleValidObj) caVehInqData.getValidationObject();

		MFVehicleData laOrigMfVehData =
			(MFVehicleData) laValidObj.getMfVehOrig();

		TitleData laOrigTitleData = laOrigMfVehData.getTitleData();

		boolean lbETitleLien = caTtlData.isETitle();
		boolean lbTitleLien = caTtlData.hasLien();

		boolean lbPriorETitleNoLien =
			(csTransCd.equals(TransCdConstant.CORTTL)
				|| csTransCd.equals(TransCdConstant.REJCOR))
				&& laOrigTitleData.isETitleNoLien();

		boolean lbCorTtlETtlNoNewLien =
			lbPriorETitleNoLien && !lbTitleLien;

		getchkETitle().setEnabled(lbCorTtlETtlNoNewLien);

		getchkETitle().setSelected(
			lbCorTtlETtlNoNewLien || lbETitleLien);
	}

	/**
	 * Setup New Plates Desired for Display() 
	 * 
	 * @param aeException
	 */
	private void setupNewPlatesDesiredForDisplay()
	{
		// default to disabled/deselected 
		getchkNewPlatesDesired().setEnabled(false);
		getchkNewPlatesDesired().setSelected(false);

		if (cbNPDApplicable)
		{
			String lsRegPltCd = caRegData.getRegPltCd();

			SpecialPlatesRegisData laSpclPltRegisData =
				caVehInqData.getMfVehicleData().getSpclPltRegisData();

			PlateTypeData laPlateTypeData =
				PlateTypeCache.getPlateType(lsRegPltCd);

			// This makes no sense and doesn't really work!  (KPH)
			if (SystemProperty.getOfficeIssuanceNo() == 291)
			{
				getchkNewPlatesDesired().setEnabled(true);
			}
			else if (
				caVehInqData.getNoMFRecs() == 0
					|| (caTtlData.getMustChangePltIndi() == 1))
			{
				getchkNewPlatesDesired().setSelected(true);
			}
			else
			{
				// Disect the original record   
				TitleValidObj laTtlValidObj =
					(TitleValidObj) caVehInqData.getValidationObject();

				MFVehicleData laMFVehOrigData =
					(MFVehicleData) laTtlValidObj.getMfVehOrig();

				RegistrationData laOrigRegData =
					laMFVehOrigData.getRegData();

				int liOrigRegClassCd = laOrigRegData.getRegClassCd();
				String lsOrigPltCd = laOrigRegData.getRegPltCd();

				boolean lbNewPlateType =
					lsOrigPltCd == null
						|| lsOrigPltCd.trim().length() == 0
						|| !lsRegPltCd.equals(lsOrigPltCd);

				// Issue Plate for REJCOR if RegClassCd or RegPltCd 
				//  has changed 
				if (csTransCd.equals(TransCdConstant.REJCOR))
				{
					// If RegClassCd or RegPltCd have changed 
					if (liOrigRegClassCd != caRegData.getRegClassCd()
						|| lbNewPlateType)
					{
						getchkNewPlatesDesired().setSelected(true);
					}
				}
			    // defect 10951
				// Check if DTA has a new plate
			    else if (UtilityMethods.isDTA(csTransCd) &&
			      !UtilityMethods.isEmpty(caDlrTtlData.getNewPltNo()))
			      {
			       getchkNewPlatesDesired().setSelected(true);
			      }
			    // end defect 10951 
				else
				{
					int liRegExpMo = caRegData.getRegExpMo();
					int liRegExpYr = caRegData.getRegExpYr();
					int liPltAge = caRegData.getRegPltAge(false);
					boolean lbRegExp = false;

					// defect 9071
					// Default to replace plate if Expired or in window
					// where annual or Mandatory Plate Age >= Req'd  
					if (!CommonFeesCache
						.isStandardExempt(caRegData.getRegClassCd()))
					{
						lbRegExp =
							CommonValidations.isRegistrationExpired(
								liRegExpMo,
								liRegExpYr,
								caVehInqData.getRTSEffDt())
								|| CommonValidations.isInRenewalWindow(
									liRegExpMo,
									liRegExpYr);
					}
					// end defect 9071

					// defect 7865
					// Set NewPlateDesired to checked and 
					// disabled if RegPltCd differs from original
					// New Plate required if: 
					//   - No Prior Plate Code 
					//   - Change Plate Codes
					//   - Expired or In Window Regis and Annual Plate
					if (lbNewPlateType
						|| (lbRegExp
							&& laPlateTypeData.getAnnualPltIndi() == 1))
					{
						getchkNewPlatesDesired().setSelected(true);
					}
					// end defect 7865

					// If (Expired || In Window) && NeedsProgramCd ="R" 
					// defect 11264
					// Set if expired or mandatory age or plate removed or Dealer GDN
					//else if (
					//	lbRegExp
					//		&& laPlateTypeData.getNeedsProgramCd().equals(
					//			PLT_REPLACEMENT_REQUIRED))
					//{
					//    // defect 10951
					//    // Also do if plate removed
					//    if (liPltAge >= laPlateTypeData.getMandPltReplAge()
					//       || caRegData.getPltRmvCd() > 0)
					//    {
					//    // end defect 10951
					else if ((lbRegExp 
							&& (laPlateTypeData.getNeedsProgramCd().equals(
						PLT_REPLACEMENT_REQUIRED) 
							&& liPltAge >= laPlateTypeData.getMandPltReplAge())) 
						|| caRegData.getPltRmvCd() > 0)
					{
							getchkNewPlatesDesired().setSelected(true);
							getchkNewPlatesDesired().setEnabled(false);
					}
					else if (liPltAge >= laPlateTypeData.getOptPltReplAge()
						|| caMFVehData.isPTOEligible())
					{
						getchkNewPlatesDesired().setEnabled(true);
						if (caTtlData.getDlrGdn().length() > 0)
						{
							getchkNewPlatesDesired().setEnabled(false);
							getchkNewPlatesDesired().setSelected(true);
						}
					    // end defect 11264
					}
					// defect 10988
					// If not PTO Eligible, but it is Annual Plate that 
					// is transferable (ie: Combo)and is not a Special Plate,
					// then we will remove the plate if Dealer GDN is entered.
					else if (!caVehInqData.getMfVehicleData().isPTOEligible() &&
							laPlateTypeData.getAnnualPltIndi() == 1 &&
						laPlateTypeData.getPltOwnrshpCd().equals(
							SpecialPlatesConstant.VEHICLE) &&
						caVehData != null && 
						caVehInqData.getMfVehicleData().getTitleData()
							.getDlrGdn().trim().length()> 0)
					{
						ItemCodesData laItmCdData =
							ItemCodesCache.getItmCd(caRegData.getRegPltCd());
						if (laItmCdData != null &&
							laItmCdData.getInvProcsngCd() == 1)
						{
							getchkNewPlatesDesired().setSelected(true);
							getchkNewPlatesDesired().setEnabled(false);
						}
					}
					// end defect 10988
					// defect 11264
					// This is handled above
					//else 
					//{
					//	  getchkNewPlatesDesired().setEnabled(
					//	  caMFVehData.isPTOEligible());
					//}
					// end defect 11264						
				}
			}

			// Note: The Special Plate will be manufactured regardless 
			//   of new registration selection  
			if (laSpclPltRegisData != null
				&& laPlateTypeData.getAnnualPltIndi() == 0
				&& laPlateTypeData.getNeedsProgramCd().equals(
					PLT_REPLACEMENT_DEFERRED))
			{
				if (laSpclPltRegisData.getRegPltAge(false)
					>= laPlateTypeData.getMandPltReplAge())
				{
					// defect 9299
					// Do not remanufacture if sunsetted
					String lsOrgNo =
						laSpclPltRegisData.getOrgNo().trim();
					if (!OrganizationNumberCache
						.isSunsetted(lsRegPltCd, lsOrgNo))
					{
						laSpclPltRegisData.setMfgSpclPltIndi(1);
					}
					// end defect 9299

				} // Exceeds Mandatory Plate Age 
			} // Special Plates  
		} // cbNPDApplicable 

		// Keep New Plts Disabled if: 
		//   --  Already selected\disabled
		//	 --  !cbNPDApplicable
		//   --  Correct Title Rejection (REJCOR)
		//   --  DTA Transaction 
		if ((!getchkNewPlatesDesired().isEnabled()
			&& getchkNewPlatesDesired().isSelected())
			|| !cbNPDApplicable
			|| csTransCd.equals(TransCdConstant.REJCOR)
			|| UtilityMethods.isDTA(csTransCd))
		{
			cbKeepNewPltsDisabled = true;
		}
		cbNPDPrevSetting = getchkNewPlatesDesired().isSelected();
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception Throwable
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
	 * Set State/County if Salvage Issue-By
	 */
	private void handleRadioButtons()
	{
		if (getradioRebuiltSalvageIssuedBy().isSelected())
		{
			getstcLblStateCountry().setEnabled(true);
			gettxtSalvageStateCountry().setEnabled(true);
			gettxtSalvageStateCountry().requestFocus();
		}
		else
		{
			getstcLblStateCountry().setEnabled(false);
			gettxtSalvageStateCountry().setEnabled(false);
			gettxtSalvageStateCountry().setText(
				CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * Initialize the class.
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			caEtched = BorderFactory.createEtchedBorder();
			// defect 7898
			// Add all of the Check Boxes into an RTSButtonGroup
			// so that all of the arrowing will be handled

			// defect 10982
			// Arrowing/tabbing: put ALL checkboxes in order
			// Add some (getchkPrivateLawEnfVeh(),
			// getchkManufacturerBuyback(), getchkETitle(), 
			// getchkTowTruckCert()), move others (getchkPltTrnsfrFee())
			RTSButtonGroup laRTSBtnGrp = new RTSButtonGroup();
			// Column 1 
			laRTSBtnGrp.add(getchkChargeTitleFee());
			laRTSBtnGrp.add(getchkChargeTitleTERPFee());
			// defect 11051 
			laRTSBtnGrp.add(getchkChargeRebuiltSalvageFee());
			// end defect 11051 
			laRTSBtnGrp.add(getchkChargeRegTransferFee());
			laRTSBtnGrp.add(getchkChargeRegEmissionFee());
			laRTSBtnGrp.add(getchkSurvivorshipRights());
			laRTSBtnGrp.add(getchkPrivateLawEnfVeh());
			// Add Plate to Owner Transfer
			// defect 11030 
			laRTSBtnGrp.add(getchkPTOTrnsfr());
			// end defect 11030 
			
			laRTSBtnGrp.add(getchkNewPlatesDesired());
			
			// Column 2 
			laRTSBtnGrp.add(getchkSpecialExamNeeded());
			laRTSBtnGrp.add(getchkMailInTransaction());
			laRTSBtnGrp.add(getchkChargeAddlTokenTrailerFee());
			laRTSBtnGrp.add(getchkVerifiedHeavyVehUseTax());
			laRTSBtnGrp.add(getchkVINCertificationWaived());
			laRTSBtnGrp.add(getchkDOTProofRequired());
			// defect 10982 
			laRTSBtnGrp.add(getchkManufacturerBuyback()); 
			// end defect 10982 
			laRTSBtnGrp.add(getchkBuyerTag());
			
			// Column 3 
			laRTSBtnGrp.add(getchkDiesel());
			laRTSBtnGrp.add(getchkFloodDamage());
			laRTSBtnGrp.add(getchkReconstructed());
			laRTSBtnGrp.add(getchkAddlEvidenceSurrendered());
			laRTSBtnGrp.add(getchkGovernmentOwned());
			laRTSBtnGrp.add(getchkExempt());
			// defect 10982 
			laRTSBtnGrp.add(getchkETitle());
			laRTSBtnGrp.add(getchkTowTruckCert()); 
			// end defect 10982 
			
			getradioNotRebuilt().setSelected(true);
			// end defect 7898
			getstcLblStateCountry().setEnabled(false);
			gettxtSalvageStateCountry().setEnabled(false);
			// default the tire type
			getradioPneumatic().setSelected(true);
			// defect 10007
			getchkTowTruckCert().setEnabled(false);
			// end defect 10007
			
			// user code end
			setName(ScreenConstant.TTL008_FRAME_NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(647, 533);
			setTitle(ScreenConstant.TTL008_FRAME_TITLE);
			setContentPane(
				getFrmTitleAdditionalInfoTTL008ContentPane1());
		}
		catch (Throwable leException)
		{
			handleException(leException);
		}
		// user code begin {2}
		gettxtApprhndCntyNo().setText(CommonConstant.STR_ZERO);
		// user code end
	}

	/**
	 * Validate Apprehended County Number.
	 * 
	 * @param asAppCnty String
	 * @return boolean
	 */
	private boolean isApprhndCntyNoValid(String asAppCnty)
	{
		boolean lbRet = true;

		if (asAppCnty.length() != 0)
		{
			try
			{
				int liAppCnty = Integer.parseInt(asAppCnty);
				if (liAppCnty != 0
					&& (liAppCnty < 1 || liAppCnty > TOTAL_NO_CNTY))
				{
					lbRet = false;
				}
			}
			catch (NumberFormatException leNFEx)
			{
				lbRet = false;
			}
		}
		return lbRet;
	}

	/**
	 * Return Is Record Processed.
	 * 
	 * @return boolean
	 */
	public boolean isRecordPrcs()
	{
		return cbProcessed;
	}

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrAgs java.lang.String[]
	 */
	public static void main(java.lang.String[] aarrAgs)
	{
		try
		{
			FrmTitleAdditionalInfoTTL008 laFrmTitleAdditionalInfoTTL008;
			laFrmTitleAdditionalInfoTTL008 =
				new FrmTitleAdditionalInfoTTL008();
			laFrmTitleAdditionalInfoTTL008.setModal(true);
			laFrmTitleAdditionalInfoTTL008
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent laRTSDBox)
				{
					System.exit(0);
				}
			});
			laFrmTitleAdditionalInfoTTL008.show();
			java.awt.Insets insets =
				laFrmTitleAdditionalInfoTTL008.getInsets();
			laFrmTitleAdditionalInfoTTL008.setSize(
				laFrmTitleAdditionalInfoTTL008.getWidth()
					+ insets.left
					+ insets.right,
				laFrmTitleAdditionalInfoTTL008.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmTitleAdditionalInfoTTL008.setVisibleRTS(true);
		}
		catch (Throwable leException)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			leException.printStackTrace(System.out);
		}
	}

	/**
	 * Is Supervisor Override Code Required for Modified Buyer Tag
	 * 
	 * @return boolean 
	 */
	private boolean isSupOvrdRqdforBuyerTag()
	{
		boolean lbReturn = false;

		if (cbBuyerTagPrevSetting != getchkBuyerTag().isSelected())
		{
			// RAD Migration 
//			caVehInqData.getVehMiscData().setSupvOvrideReason(
//			MODIFIED_BUYER_TAG_SELECTION);
			VehMiscData laVehMiscData = caVehInqData.getVehMiscData(); 
			laVehMiscData.setSupvOvrideReason(MODIFIED_BUYER_TAG_SELECTION);
			// end RAD Migration 


			getController().processData(
				VCTitleAdditionalInfoTTL008.SUPV_OVRIDE,
				caVehInqData);

			if (caVehInqData.getVehMiscData().getSupvOvride() == null
				|| caVehInqData.getVehMiscData().getSupvOvride().length()
					== 0)
			{
				lbReturn = true;
			}
		}
		return lbReturn;
	}

	//	/**
	//	 * Restore Check Box Selections  
	//	 */
	//	private void restoreCheckBoxSelections()
	//	{
	//		try
	//		{
	//			// defect 5828	
	//			//	Get data from Title Additional Info TTL008
	//			Object laScrTTL008a =
	//				getController().getMediator().openVault(
	//					ScreenConstant.TTL008);
	//			// Define the object
	//			if (laScrTTL008a != null)
	//			{
	//				// Review process
	//			}
	//			// Set up local objects
	//			if (caVehInqData != null)
	//			{
	//				MFVehicleData laMfVehData =
	//					(MFVehicleData) caVehInqData.getMfVehicleData();
	//				VehicleData laVehData = null;
	//				RegistrationData laRegData = null;
	//				TitleData laTtlData = null;
	//				RegTtlAddlInfoData laTtlAddlData =
	//					(RegTtlAddlInfoData) ((TitleValidObj) caVehInqData
	//						.getValidationObject())
	//						.getRegTtlAddData();
	//				// Get data
	//				if (laMfVehData != null)
	//				{
	//					laVehData = laMfVehData.getVehicleData();
	//					laRegData = laMfVehData.getRegData();
	//					laTtlData = laMfVehData.getTitleData();
	//				}
	//
	//				if (getchkChargeTitleFee().isEnabled())
	//				{
	//					if (laTtlAddlData.getChrgTtlFeeIndi() == 1)
	//					{
	//						getchkChargeTitleFee().setSelected(true);
	//					}
	//					else
	//					{
	//						getchkChargeTitleFee().setSelected(false);
	//					}
	//				}
	//				// defect 6448
	//				// added code for charge title TERP fee checkbox
	//				if (getchkChargeTitleTERPFee().isEnabled())
	//				{
	//					if (laTtlAddlData.getChrgTtlTERPFeeIndi() == 1)
	//					{
	//						getchkChargeTitleTERPFee().setSelected(true);
	//					}
	//					else
	//					{
	//						getchkChargeTitleTERPFee().setSelected(false);
	//					}
	//				}
	//				// end defect 6448
	//				if (getchkChargeRegTransferFee().isEnabled())
	//				{
	//					if (laTtlAddlData.getChrgTrnsfrFeeIndi() == 1)
	//					{
	//						getchkChargeRegTransferFee().setSelected(true);
	//					}
	//					else
	//					{
	//						getchkChargeRegTransferFee().setSelected(false);
	//					}
	//				}
	//				// defect 9584
	//				//				if (getchkChargeTransferPenalty().isEnabled())
	//				//				{
	//				//					if (laTtlAddlData.getTrnsfrPnltyIndi() == 1)
	//				//					{
	//				//						getchkChargeTransferPenalty().setSelected(true);
	//				//					}
	//				//					else
	//				//					{
	//				//						getchkChargeTransferPenalty().setSelected(
	//				//							false);
	//				//					}
	//				//				}
	//				// end defect 9584 
	//				if (getchkChargeRegEmissionFee().isEnabled())
	//				{
	//					// defect 6558
	//					// getchkChargeRegEmissionFee() changes when moving
	//					// back and forth between TTL008 and TTL011
	//					// Changed from if (ttlAddlData.
	//					//	getNoChrgRegEmiFeeIndi() == 1)
	//					if (laTtlAddlData.getNoChrgRegEmiFeeIndi() == 0)
	//						// end defect 6558
	//					{
	//						getchkChargeRegEmissionFee().setSelected(true);
	//					}
	//					else
	//					{
	//						getchkChargeRegEmissionFee().setSelected(false);
	//					}
	//				}
	//				if (getchkAddlEvidenceSurrendered().isEnabled())
	//				{
	//					if (laTtlAddlData.getAddlEviSurndIndi() == 1)
	//					{
	//						getchkAddlEvidenceSurrendered().setSelected(
	//							true);
	//					}
	//					else
	//					{
	//						getchkAddlEvidenceSurrendered().setSelected(
	//							false);
	//					}
	//				}
	//				if (getchkSpecialExamNeeded().isEnabled())
	//				{
	//					if (laTtlData.getTtlExmnIndi() == 1)
	//					{
	//						getchkSpecialExamNeeded().setSelected(true);
	//					}
	//					else
	//					{
	//						getchkSpecialExamNeeded().setSelected(false);
	//					}
	//				}
	//				if (getchkMailInTransaction().isEnabled())
	//				{
	//					if (laTtlAddlData.getProcsByMailIndi() == 1)
	//					{
	//						getchkMailInTransaction().setSelected(true);
	//					}
	//					else
	//					{
	//						getchkMailInTransaction().setSelected(false);
	//					}
	//				}
	//				if (getchkChargeAddlTokenTrailerFee().isEnabled())
	//				{
	//					if (laTtlAddlData.getChrgAddlTknTrlrFeeIndi() == 1)
	//					{
	//						getchkChargeAddlTokenTrailerFee().setSelected(
	//							true);
	//					}
	//					else
	//					{
	//						getchkChargeAddlTokenTrailerFee().setSelected(
	//							false);
	//					}
	//				}
	//				if (getchkVerifiedHeavyVehUseTax().isEnabled())
	//				{
	//					if (laRegData.getHvyVehUseTaxIndi() == 1)
	//					{
	//						getchkVerifiedHeavyVehUseTax().setSelected(
	//							true);
	//					}
	//					else
	//					{
	//						getchkVerifiedHeavyVehUseTax().setSelected(
	//							false);
	//					}
	//				}
	//				if (getchkVINCertificationWaived().isEnabled())
	//				{
	//					if (laTtlData.getInspectnWaivedIndi() == 1)
	//					{
	//						getchkVINCertificationWaived().setSelected(
	//							true);
	//					}
	//					else
	//					{
	//						getchkVINCertificationWaived().setSelected(
	//							false);
	//					}
	//				}
	//				if (getchkDOTProofRequired().isEnabled())
	//				{
	//					if (laVehData.getDotStndrdsIndi() == 1)
	//					{
	//						getchkDOTProofRequired().setSelected(true);
	//					}
	//					else
	//					{
	//						getchkDOTProofRequired().setSelected(false);
	//					}
	//				}
	//				if (getchkDiesel().isEnabled())
	//				{
	//					if (laVehData.getDieselIndi() == 1)
	//					{
	//						getchkDiesel().setSelected(true);
	//					}
	//					else
	//					{
	//						getchkDiesel().setSelected(false);
	//					}
	//				}
	//				if (getchkFloodDamage().isEnabled())
	//				{
	//					if (laVehData.getFloodDmgeIndi() == 1)
	//					{
	//						getchkFloodDamage().setSelected(true);
	//					}
	//					else
	//					{
	//						getchkFloodDamage().setSelected(false);
	//					}
	//				}
	//				if (getchkReconstructed().isEnabled())
	//				{
	//					if (laVehData.getReContIndi() == 1)
	//					{
	//						getchkReconstructed().setSelected(true);
	//					}
	//					else
	//					{
	//						getchkReconstructed().setSelected(false);
	//					}
	//				}
	//				if (getchkSurvivorshipRights().isEnabled())
	//				{
	//					if (laTtlData.getSurvshpRightsIndi() == 1)
	//					{
	//						getchkSurvivorshipRights().setSelected(true);
	//					}
	//					else
	//					{
	//						getchkSurvivorshipRights().setSelected(false);
	//					}
	//				}
	//				if (getchkGovernmentOwned().isEnabled())
	//				{
	//					if (laTtlData.getGovtOwndIndi() == 1)
	//					{
	//						getchkGovernmentOwned().setSelected(true);
	//					}
	//					else
	//					{
	//						getchkGovernmentOwned().setSelected(false);
	//					}
	//				}
	//				// defect 8900 
	//				//if (getchkExempt().isEnabled())
	//				//{
	//				if (laRegData.getExmptIndi() == 1)
	//				{
	//					getchkExempt().setSelected(true);
	//				}
	//				else
	//				{
	//					getchkExempt().setSelected(false);
	//				}
	//				//}
	//				// end defect 8900 
	//				if (getradioPneumatic().isEnabled())
	//				{
	//					if (laRegData.getTireTypeCd().equals("P"))
	//					{
	//						getradioPneumatic().setSelected(true);
	//					}
	//					else
	//					{
	//						getradioSolid().setSelected(true);
	//					}
	//				}
	//				// Set Salvage Radio Button
	//				// defect 6507
	//				//		Turn of code. Do not need to reset 
	//				//			enable/disable
	//				/*         if (getOrigDocType() != 4
	//				            && getOrigDocType() != 5
	//				            && getOrigDocType() != 12
	//				            && getOrigDocType() != 13
	//				            && getOrigDocType() != 14
	//				            && getOrigDocType() != 15)
	//				         {
	//				            enabledRebuiltSalvage();
	//				         }
	//				*/ // end defect 6507         
	//				if (laVehData
	//					.getRecondCd()
	//					.equals(TitleConstant.NOT_REBUILT))
	//				{
	//					getradioNotRebuilt().setSelected(true);
	//				}
	//				else if (
	//					laVehData.getRecondCd().equals(
	//						TitleConstant.SALVAGE_LOSS_UNKNOWN))
	//				{
	//					getradioRebuiltSalvageLossUnknown().setSelected(
	//						true);
	//				}
	//				else if (
	//					laVehData.getRecondCd().equals(
	//						TitleConstant.REBUILT_SALVAGE_75_94))
	//				{
	//					getradioRebuiltSalvage7594PctLoss().setSelected(
	//						true);
	//				}
	//				else if (
	//					laVehData.getRecondCd().equals(
	//						TitleConstant.REBUILT_SALVAGE_95_PLUS))
	//				{
	//					getradioRebuiltSalvage95PctPlusLoss().setSelected(
	//						true);
	//				}
	//				else if (
	//					laVehData.getRecondCd().equals(
	//						TitleConstant.REBUILT_SALVAGE_ISSUED))
	//				{
	//					getradioRebuiltSalvageIssuedBy().setSelected(true);
	//				}
	//				if (gettxtSalvageStateCountry().isEnabled());
	//				{
	//					gettxtSalvageStateCountry().setText(
	//						laTtlData.getSalvStateCntry());
	//				}
	//
	//				// defect 10246 
	//				// get the privacy act option
	//				//				if (getradioNone().isEnabled())
	//				//				{
	//				//					if (laOwnData.getPrivacyOptCd() == 0)
	//				//					{
	//				//						getradioNone().setSelected(true);
	//				//					}
	//				//				}
	//				//				else if (getradioIndividual().isEnabled())
	//				//				{
	//				//					if (laOwnData.getPrivacyOptCd() == 1)
	//				//					{
	//				//						getradioIndividual().setSelected(true);
	//				//					}
	//				//				}
	//				//				else if (getradioCommercial().isEnabled())
	//				//				{
	//				//					if (laOwnData.getPrivacyOptCd() == 2)
	//				//					{
	//				//						getradioCommercial().setSelected(true);
	//				//					}
	//				//				}
	//				//				else if (getradioBoth().isEnabled())
	//				//				{
	//				//					if (laOwnData.getPrivacyOptCd() == 3)
	//				//					{
	//				//						getradioBoth().setSelected(true);
	//				//					}
	//				//				}
	//				getradioBoth().setSelected(true);
	//				// end defect 10246 
	//
	//				// get the apprehended county number
	//				if (gettxtApprhndCntyNo().isEnabled())
	//				{
	//					String lsApprhndCntyNo =
	//						Integer.toString(
	//							laTtlAddlData.getApprhndFndsCntyNo());
	//					gettxtApprhndCntyNo().setText(lsApprhndCntyNo);
	//				}
	//				repaint();
	//			}
	//		}
	//		catch (NullPointerException leNPE)
	//		{
	//			RTSException laRTSEx =
	//				new RTSException(RTSException.JAVA_ERROR, leNPE);
	//			laRTSEx.displayError(this);
	//		}
	//	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		// defect 10290
		// Do not process RTSException, CompleteTransactionData
		// TODO CompleteTransactionData doesn't make sense 
		if (aaDataObject != null
			&& !(aaDataObject instanceof RTSException)
			&& !(aaDataObject instanceof CompleteTransactionData))
		{
			try
			{
				csTransCd = getController().getTransCode();
				
				if (aaDataObject instanceof Vector)
				{
					Vector lvIsNextVCREG029 = (Vector) aaDataObject;
					if (lvIsNextVCREG029 != null)
					{
						if (lvIsNextVCREG029.size() == 2)
						{
							// determine next vc if NOT reg029
							if (lvIsNextVCREG029.get(0)
								instanceof Boolean)
							{
								// first element is flag whether to go to 
								//	REG029
								getController().processData(
									VCSalesTaxTTL012
										.REDIRECT_IS_NEXT_VC_REG029,
									lvIsNextVCREG029);
							}
							else if (
								lvIsNextVCREG029.get(0)
									instanceof String)
							{
								getController().processData(
									VCSalesTaxTTL012.REDIRECT_NEXT_VC,
									lvIsNextVCREG029);
							}
						}
					}
				}
				else
				{
					caVehInqData = (VehicleInquiryData) aaDataObject;
					caMFVehData = caVehInqData.getMfVehicleData();
					caTtlData = caMFVehData.getTitleData();
					caRegData = caMFVehData.getRegData();
					caVehData = caMFVehData.getVehicleData();
					
					// determine if new plate (checkbox) is applicable 
					cbNPDApplicable = isNewPlatesDesiredApplicable();
					TitleValidObj laTtlValidObj =
						(TitleValidObj) caVehInqData
							.getValidationObject();
					caDlrTtlData = laTtlValidObj.getDlrTtlData();
					
					// defect 10946 
					// defect 11051
					//	if (caVehInqData.getNoMFRecs() != 0 && laTtlValidObj != null)
					//	{
					//		MFVehicleData laMfVehicleData = (MFVehicleData) laTtlValidObj.getMfVehOrig();
					//		if (laMfVehicleData != null && laMfVehicleData.getTitleData() != null)
					//		{
					//			ciOrigDocTypeCd = laMfVehicleData.getTitleData().getDocTypeCd(); 
					//		}
					//	}
					// end defect 11051 
					// end defect 10946 

					// disable Charge Title Fee, Charge TERP fee for 
					// Non-Titled title type 
					if (caTtlData.getTtlTypeIndi()
						== TitleTypes.INT_NONTITLED)
					{
						getchkChargeTitleFee().setEnabled(false);
						getchkChargeTitleTERPFee().setEnabled(false);
						
						// defect 11051 
						getchkChargeRebuiltSalvageFee().setEnabled(false); 
						// end defect 11051 

						// defect 10252
						getchkManufacturerBuyback().setEnabled(false);
						getchkManufacturerBuyback().setSelected(false);
					}
					else
					{
						// defect 11051 
						//	getchkChargeRebuiltSalvageFee().setEnabled(
						//		(caTtlData.getTtlTypeIndi()
						//		!= TitleTypes.INT_REGPURPOSE) && 
						//		(csTransCd.equals(TransCdConstant.REJCOR) ||
						//		caVehInqData.getNoMFRecs() == 0 || 
						//		UtilityMethods.isSalvageTypeDocTypeCd(ciOrigDocTypeCd))); 
						getchkChargeRebuiltSalvageFee().setEnabled(
								caTtlData.getTtlTypeIndi()
								!= TitleTypes.INT_REGPURPOSE);  
						// end defect 11051
							
						getchkManufacturerBuyback().setSelected(
							caTtlData.getLemonLawIndi() == 1);
						// end defect 10252

						// defect 8900
						// If FeeCalcCat = 4 (Standard Exempt) set and 
						// disable Exempt Indi, set to not charge fees
						if (csTransCd.equals(TransCdConstant.REJCOR)
							|| !SystemProperty.isCounty()
							|| CommonFeesCache.isStandardExempt(
								caRegData.getRegClassCd()))
						{
							getchkChargeTitleFee().setSelected(false);
							getchkChargeTitleTERPFee().setSelected(
								false);
						}
						// end defect 8900
						else
						{
							// default Charge Title Fee to checked and 
							// default Charge Title TERP Fee to checked
							getchkChargeTitleFee().setSelected(true);
							getchkChargeTitleTERPFee().setSelected(
								true);
						}
					}
					// set checkboxes associated with the vehicle record
					getchkDOTProofRequired().setSelected(
						caVehData.getDotStndrdsIndi() == 1);

					getchkVINCertificationWaived().setSelected(
						caTtlData.getInspectnWaivedIndi() == 1);

					getchkDiesel().setSelected(
						caVehData.getDieselIndi() == 1);

					getchkFloodDamage().setSelected(
						caVehData.getFloodDmgeIndi() == 1);

					getchkReconstructed().setSelected(
						caVehData.getReContIndi() == 1);

					// PCR 41
					// defect 10946 
					// Do not set to false if ORIGINAL && REJCOR; 
					//   REJCOR (& CORRECTED) will be assigned in final else according to prior setting 
					if (caTtlData.getTtlTypeIndi()
						== TitleTypes.INT_ORIGINAL && !csTransCd.equals(TransCdConstant.REJCOR))
					{
						getchkSurvivorshipRights().setSelected(false);
					}
					// end defect 10946 
					// defect 10601
					else if (caTtlData.getDocTypeCd() == 
							DocTypeConstant.REGISTRATION_PURPOSES_ONLY
							|| caTtlData.getDocTypeCd() == 
							DocTypeConstant.NON_TITLED_VEHICLE)
					{
						getchkSurvivorshipRights().setSelected(false);
						getchkSurvivorshipRights().setEnabled(false);
					}
					// end defect 10601
					else
					{
						// RejCor, Corrected Title 
						getchkSurvivorshipRights().setSelected(
							caTtlData.getSurvshpRightsIndi() == 1);
					}
					// end PCR 41

					getchkGovernmentOwned().setSelected(
						caTtlData.getGovtOwndIndi() == 1);

					// disable Government Owned if title type is not 
					//	Registration Purposes Only
					getchkGovernmentOwned().setEnabled(
						caTtlData.getTtlTypeIndi()
							== TitleTypes.INT_REGPURPOSE);

					// special examination needed
					getchkSpecialExamNeeded().setSelected(
						caTtlData.getTtlExmnIndi() == 1
							|| caVehInqData.getMfDown() == 1);

					// default Charge Transfer Fee
					// defect 8900
					// Default to not charge if Standard Exempt or ExemptIndi.
					// Must check ExemptIndi in case this is Title Correction.
					// defect 9036						
					// HQ should not set Charge Transfer Fee to 'selected'.						
					if ((caVehInqData.getNoMFRecs() > 0)
						&& !(csTransCd.equals(TransCdConstant.REJCOR))
						&& !SystemProperty.isHQ()
						&& caRegData.getExmptIndi() != 1
						&& !CommonFeesCache.isStandardExempt(
							caRegData.getRegClassCd())
						&& caMFVehData.getRegData().getOffHwyUseIndi() != 1
						&& caMFVehData.getRegData().getRegInvldIndi() != 1)
					{
						getchkChargeRegTransferFee().setSelected(true);
					}
					// end defect 9036					
					// end defect 8900
					
					// defect 10949
					// As-of start date, if Dealer GDN is present, and
					// this is not Corrected Title nor Reject Correction,
					// do not charge registration transfer fee, and if
					// registration is not expired set Reg Invalid indi 
					// so registration is discarded
					if (caTtlData.getDlrGdn().length() > 0 &&
						caVehInqData.getRTSEffDt() >=
							SystemProperty.getDlrInvalidRegStartDate().
								getYYYYMMDDDate() &&
						!csTransCd.equals(TransCdConstant.CORTTL) &&
						!csTransCd.equals(TransCdConstant.REJCOR))
					{
						getchkChargeRegTransferFee().
							setSelected(false);
						getchkChargeRegTransferFee().
							setEnabled(false);
						// defect 11136
						// Don't need to check if expired
						//int liCurrMo =
						//	RTSDate.getCurrentDate().getMonth();
						//int liCurrYr =
						//	RTSDate.getCurrentDate().getYear();
						//int liCurrMo = laRTSEffDate.getMonth();
						//int liCurrYr = laRTSEffDate.getYear();
						//if (caRegData.getRegExpYr() > liCurrYr ||
						//	(caRegData.getRegExpYr() == liCurrYr &&
						//		caRegData.getRegExpMo() >= liCurrMo))
						//{
						
						// defect 11136
						// Set the original data object to Reg Invalid
						// Note: Fees uses laDlrTtlData.getMFVehicleDataFromMF() 
						//        as the original
						// caRegData.setRegInvldIndi(1);
						MFVehicleData laOrigMf = 
							(MFVehicleData) laTtlValidObj.getMfVehOrig(); 
						laOrigMf.getRegData().setRegInvldIndi(1);

						if (UtilityMethods.isDTA(csTransCd))
						{
							DealerTitleData laDlrTtlData = laTtlValidObj.getDlrTtlData(); 
							if (laDlrTtlData != null)
							{
								MFVehicleData laMFFromMf = laDlrTtlData.getMFVehicleDataFromMF();
								if (laMFFromMf != null && laMFFromMf.getRegData()!= null)
								{
									laMFFromMf.getRegData().setRegInvldIndi(1); 
								}
							}
						}
						// end defect 11136
							
						// Must set so New Plates Desired is selected
						// defect 10988
						// Must Change only if PTO Eligible, and this
						// is already handled later.
						//caTtlData.setMustChangePltIndi(1);
						// end defect 10988
						//}
						//end defect 11136
					}
					// end defect 10949
					
					// disable Regis Emission Fee if Reg Class NOT 
					// Combination
					// defect 8516, 8535 
					// Use CommonFees lookup for Emission Percent > 0.			
					//if (laRegData.getRegClassCd() != COMBINATION)
					int liRegClassCd = caRegData.getRegClassCd();
					int liRTSCurrDate = new RTSDate().getYYYYMMDDDate();
					caCommFeesData =
						CommonFeesCache.getCommonFee(
							liRegClassCd,
							liRTSCurrDate);
							
					// defect 10342, 10395 
					boolean lbChrgEmission = caCommFeesData != null
					&& caCommFeesData
						.getEmissionsPrcnt()
						.compareTo(
						ZERODOLLAR)
						!= 0; 
						
					getchkChargeRegEmissionFee().setEnabled(lbChrgEmission); 
					getchkChargeRegEmissionFee().setSelected(lbChrgEmission);
					// end defect 10342, 10395 
					// end defect 8516, 8535 

					String lsVehCd = caVehData.getVehClassCd();
					int liRegCd = caRegData.getRegClassCd();
					int liEffDt =
						RTSDate.getCurrentDate().getYYYYMMDDDate();
					RegistrationClassData laRegClassData =
						(
							RegistrationClassData) RegistrationClassCache
								.getRegisClass(
							lsVehCd,
							liRegCd,
							liEffDt);

					if (laRegClassData != null)
					{
						// disable Token Trailer Fee check box if not 
						//	required
						getchkChargeAddlTokenTrailerFee().setEnabled(
							laRegClassData.getToknTrlrFeeReqd() != 0);

						// disable Heavy Vehicle Use check box if not 
						// required
						if (laRegClassData.getHvyVehUseWt() == 0)
						{
							getchkVerifiedHeavyVehUseTax().setEnabled(
								false);
						}
						else if (
							caRegData.getVehGrossWt()
								< laRegClassData.getHvyVehUseWt())
						{
							getchkVerifiedHeavyVehUseTax().setEnabled(
								false);
						}
						else if (caRegData.getHvyVehUseTaxIndi() == 1)
						{
							// defect 10381
							// Force user to provide proof of payment 
							// for Title and Non-Title events
							if (!(csTransCd.equals(
								TransCdConstant.TITLE)
								|| csTransCd.equals(
									TransCdConstant.NONTTL)))
							//	|| UtilityMethods.isDTA(csTransCd)))
							{							
								getchkVerifiedHeavyVehUseTax().
									setSelected(true);
							}
							// end defect 10381								
						}
						// disable Diesel check box if not required
						if (laRegClassData.getDieselReqd() == 0)
						{
							getchkDiesel().setEnabled(false);
							getchkDiesel().setSelected(false);
						}
					}
					// disable checkboxes since no regis class data was
					// found
					else
					{
						getchkChargeAddlTokenTrailerFee().setEnabled(
							false);
						getchkVerifiedHeavyVehUseTax().setEnabled(
							false);
						getchkVerifiedHeavyVehUseTax().setEnabled(
							false);
						getchkDiesel().setEnabled(false);
						getchkDiesel().setSelected(false);
					}
					// defect 8900
					// Counties can now do Exempt
					//if (laOfcData.getOfcIssuanceCd() == 3)
					//{
					//	getchkExempt().setEnabled(false);
					//}
					boolean lbDTA = UtilityMethods.isDTA(csTransCd);
					boolean lbStandardExempt =
						CommonFeesCache.isStandardExempt(
							caRegData.getRegClassCd());

					// All except Standard Exempt or Corrected Title 
					// previously reset in TTL003  
					getchkExempt().setSelected(
						caRegData.getExmptIndi() == 1
							|| lbStandardExempt);
					// Disable if Unauthorized or Standard Exempt or DTA 				
					if ((getController()
						.getMediator()
						.getDesktop()
						.getSecurityData()
						.getExmptAuthAccs()
						!= 1)
						|| lbStandardExempt
						|| lbDTA)
					{
						getchkExempt().setEnabled(false);
					}
					// end defect 8900					
					// disable solid tire type if not allowed for vehicles 
					//	reg class
					// Get Tire type codes from Regis Wt Fees
					int liRegCode = caRegData.getRegClassCd();
					int liDate =
						RTSDate.getCurrentDate().getYYYYMMDDDate();
					int liGrossWt = caRegData.getVehGrossWt();
					Vector lvRegWtFeesData =
						RegistrationWeightFeesCache.getRegWtFee(
							liRegCode,
							liDate,
							liGrossWt);
							
					//defect 10197
					// Set Solid Tire Indi if it is not null.
					// defect 10317
					// use common constant to set up Solid Tire Type
					if (lvRegWtFeesData == null
						|| lvRegWtFeesData.size() == 1)
					{
						getradioSolid().setEnabled(false);
					}
					else if ( caRegData.getTireTypeCd() != null
	  					&& caRegData.getTireTypeCd().equals(
	   					CommonConstant.TIRE_TYPE_SOLID))
	 				{
		   				getradioSolid().setSelected(true);
	 				}
	 				// end defect 10317
					// end defect 10197

					// defect 9368 
					// Evaluate only first time through 
					if (!cbAlreadySet)
					{
						// defect 10999
						// Do not set 'must change Plt indi in TTL008
//						ciOrigMustChangePltIndi =
//							caVehInqData
//								.getMfVehicleData()
//								.getTitleData()
//								.getMustChangePltIndi();
//
//						// defect 9670 
//						if (caRegData.getPltRmvCd() != 0
//							&& cbNPDApplicable)
//						{
//							caVehInqData
//								.getMfVehicleData()
//								.getTitleData()
//								.setMustChangePltIndi(
//								1);
//						}
//						// defect 9425 
//						else if (lbDTA)
//						{
//							String lsNewPlateNo =
//								caDlrTtlData.getNewPltNo();
//
//							if (!UtilityMethods.isEmpty(lsNewPlateNo))
//							{
//								caVehInqData
//									.getMfVehicleData()
//									.getTitleData()
//									.setMustChangePltIndi(
//									1);
//							}
//						}
//						// end defect 9425
//						// end defect 9670
						// end defect 10999


						// defect 9367 
						if (cbNPDApplicable
							&& caVehInqData
								.getMfVehicleData()
								.isPTOEligible())
						{
							// defect 10999
							// Do not set 'must change Plt indi in TTL008
//							// defect 9425 / 9824  
//							// add logic for DTA
//							String lsDlrGDN =
//								caVehInqData
//									.getMfVehicleData()
//									.getTitleData()
//									.getDlrGdn();
//
//							if (lbDTA
//								|| !UtilityMethods.isEmpty(lsDlrGDN))
//							{
//								caVehInqData
//									.getMfVehicleData()
//									.getTitleData()
//									.setMustChangePltIndi(
//									1);
//							}
//							// end defect 9824
							// end defect 10999


							if (lbDTA)
							{
								// defect 1030
								getchkPTOTrnsfr().setSelected(
									caDlrTtlData.isCustSuppliedPlt());

								// defect 9698  
								getchkPTOTrnsfr().setEnabled(false);
								// end defect 9698 

							}
							else
							{
								getchkPTOTrnsfr().setEnabled(true);
								// end defect 11030
							}
							// end defect 9425 
						}

						// Enable/Check Buyer Tag if applicable
						// defect 9799 
						boolean lbTitleCorr =
							csTransCd.equals(TransCdConstant.REJCOR)
								|| csTransCd.equals(
									TransCdConstant.CORTTL);

						int liDateComp =
							lbTitleCorr
								? caTtlData.getTtlSignDate()
								: new RTSDate().getYYYYMMDDDate();

						if (liDateComp
							>= SystemProperty
								.getBuyerTagStartDate()
								.getYYYYMMDDDate()
							&& (caVehData != null
								&& caVehInqData
									.getMfVehicleData()
									.getTitleData()
									.getDlrGdn()
									.trim()
									.length()
									> 0))
						{
							getchkBuyerTag().setEnabled(true);
							getchkBuyerTag().setSelected(!lbTitleCorr);
							cbBuyerTagPrevSetting = !lbTitleCorr;
						}
						// end defect 9799 

						setupNewPlatesDesiredForDisplay();

						// defect 9971 
						setupETtlChkbox();
						// end defect 9971 

						cbAlreadySet = true;
					}
					// defect 10999
					// Do not set 'must change Plt indi in TTL008
//					else
//					{
//						caVehInqData
//							.getMfVehicleData()
//							.getTitleData()
//							.setMustChangePltIndi(
//							ciOrigMustChangePltIndi);
//					}
					// end defect 10999
					// end defect 9368
					// end defect 9367

					// Set rebuilt salvage choice to match vehicle record 
					// recondcd
					// defect 9971 
					// Compare DocTypeCd from Orig Data as updated in TTL007 
					//  using new method
					setupRebuiltSalvageChoice();

					// defect 10366 
					// VTRTtlEmrgCd1 refactored to PvtLawEnfVehCd
					// defect 10153
					// set the check box by data
					if ((getController()
						.getTransCode()
						.equalsIgnoreCase("REJCOR")
						|| getController()
							.getTransCode()
							.equalsIgnoreCase(
							"CORTTL"))
						&& caTtlData.getPvtLawEnfVehCd().equals("1"))
					{
						// end defect 10366 
						getchkPrivateLawEnfVeh().setSelected(true);
					}
					else
					{
						getchkPrivateLawEnfVeh().setSelected(false);
					}
					// determine if check box should be enabled.
					getchkPrivateLawEnfVeh().setEnabled(false);
					if (SystemProperty.isHQ())
					{
						SecurityData laSecData =
							getController()
								.getMediator()
								.getDesktop()
								.getSecurityData();
						if (laSecData.getPrivateLawEnfVehAccs() == 1)
						{
							getchkPrivateLawEnfVeh().setEnabled(true);
						}
					}
					// end defect 10153

					// Only doDlrTtl if transaction is a dealer diskette 
					// trans
					if (csTransCd.equals(TransCdConstant.DTAORD)
						|| getController().getTransCode().equals(
							TransCdConstant.DTANTD))
					{
						doDlrTtl();
					}
				}
			}
			catch (NullPointerException aeNPE)
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						aeNPE);
				leRTSEx.displayError(this);
				leRTSEx = null;
			}
			catch (NumberFormatException aeNFE)
			{
				RTSException laRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						aeNFE);
				laRTSEx.displayError(this);
				laRTSEx = null;
			}
			// defect 10007
			if (csTransCd.equals(TransCdConstant.TITLE))
			//	|| csTransCd.equals(TransCdConstant.RENEW))
			//	|| csTransCd.equals(TransCdConstant.EXCH)
			//	|| UtilityMethods.isDTA(getController().getTransCode()))
				{
					if (caRegData.getRegClassCd() == 46
						|| caRegData.getRegClassCd() == 47)
					{
						getchkTowTruckCert().setEnabled(true);
					}
					else 
					{
						getchkTowTruckCert().setEnabled(false);
					}
				}
				else
				{
					getchkTowTruckCert().setEnabled(false);
				}
				// end defect 10007 
		}
		// end defect 10290 
	}

	/**
	 * Set data to Vehicle Inquiry Object
	 * 
	 * @return VehicleInquiryData
	 */
	private VehicleInquiryData setDataToDataObject()
	{
		// Make copy of VehicleInquiryData prior to updating 
		VehicleInquiryData laVehInqData = (VehicleInquiryData) UtilityMethods.copy(caVehInqData);
		TitleData laTtlData = laVehInqData.getMfVehicleData().getTitleData();
		RegistrationData laRegData = laVehInqData.getMfVehicleData().getRegData();
		VehicleData laVehData = laVehInqData.getMfVehicleData().getVehicleData(); 
		
		try
		{
			// defect 9368 
			// Use class variables
			TitleValidObj laTtlValObj = null;
			RegTtlAddlInfoData laTtlAddlData =
				(RegTtlAddlInfoData) ((TitleValidObj) laVehInqData
					.getValidationObject())
					.getRegTtlAddData();

			laTtlValObj =
				(TitleValidObj) laVehInqData.getValidationObject();

			if (laTtlAddlData == null)
			{
				laTtlAddlData = new RegTtlAddlInfoData();
				(
					(TitleValidObj) laVehInqData
						.getValidationObject())
						.setRegTtlAddData(
					laTtlAddlData);
			}
			// defect 9367
			// Set Plate To Owner Transfer (Check box)
			// defect 11030 
			if (getchkPTOTrnsfr().isSelected())
			{
				// end defect 11030 
				laTtlAddlData.setPTOTrnsfrIndi(1);
			}
			else
			{
				laTtlAddlData.setPTOTrnsfrIndi(0);
			}

			//	Set Charge Dealer Tag Fee (GDN Number) if checked
			if (getchkBuyerTag().isSelected())
			{
				laTtlAddlData.setChrgBuyerTagFeeIndi(1);
			}
			else
			{
				laTtlAddlData.setChrgBuyerTagFeeIndi(0);
			}
			// end defect 9367
			if (getchkChargeTitleFee().isSelected())
			{
				laTtlAddlData.setChrgTtlFeeIndi(1);
			}
			else
			{
				laTtlAddlData.setChrgTtlFeeIndi(0);
			}
			// defect 11051 
			laTtlAddlData.setChrgRbltSlvgFeeIndi(
					getchkChargeRebuiltSalvageFee().isSelected() ? 1:0); 
			// end defect 11051
			
			// defect 6448
			// add code for charge title TERP fee checkbox
			if (getchkChargeTitleTERPFee().isSelected())
			{
				laTtlAddlData.setChrgTtlTERPFeeIndi(1);
			}
			else
			{
				laTtlAddlData.setChrgTtlTERPFeeIndi(0);
			}
			// end defect 6448
			if (getchkChargeRegTransferFee().isSelected())
			{
				laTtlAddlData.setChrgTrnsfrFeeIndi(1);
			}
			else
			{
				laTtlAddlData.setChrgTrnsfrFeeIndi(0);
			}
			// defect 9584 
			//			if (getchkChargeTransferPenalty().isSelected())
			//			{
			//				laTtlAddlData.setTrnsfrPnltyIndi(1);
			//			}
			//			else
			//			{
			//				laTtlAddlData.setTrnsfrPnltyIndi(0);
			//			}
			// end defect 9584 

			if (getchkChargeRegEmissionFee().isSelected())
			{
				laTtlAddlData.setNoChrgRegEmiFeeIndi(0);
			}
			else
			{
				laTtlAddlData.setNoChrgRegEmiFeeIndi(1);
			}

			if (getchkAddlEvidenceSurrendered().isSelected())
			{
				laTtlAddlData.setAddlEviSurndIndi(1);
			}
			else
			{
				laTtlAddlData.setAddlEviSurndIndi(0);
			}
			// defect 9368 
			if (getchkNewPlatesDesired().isSelected())
			{
				laTtlAddlData.setNewPltDesrdIndi(1);
				// defect 10999
				// Do not set 'must change Plt indi in TTL008
//				// Force issue plate, even if in window
//				// defect 9529
//				// Also set if 'Customer Supplied Allowed' (ie: OLDPLT2) 
//				if (caMFVehData.isPTOEligible()
//					|| PlateTypeCache.isCustSuppliedAllowed(
//						caVehInqData
//							.getMfVehicleData()
//							.getRegData()
//							.getRegPltCd()))
//				{
//					// end defect 9529
//					caTtlData.setMustChangePltIndi(1);
//				}
			}
//			else
//			{
//				laTtlAddlData.setNewPltDesrdIndi(0);
//				caTtlData.setMustChangePltIndi(ciOrigMustChangePltIndi);
//			}
//			// end defect 9368
			// end defect 10999
			if (getchkSpecialExamNeeded().isSelected())
			{
				laTtlData.setTtlExmnIndi(1);
			}
			else
			{
				laTtlData.setTtlExmnIndi(0);
			}

			if (getchkMailInTransaction().isSelected())
			{
				laTtlAddlData.setProcsByMailIndi(1);
			}
			else
			{
				laTtlAddlData.setProcsByMailIndi(0);
			}

			if (getchkChargeAddlTokenTrailerFee().isSelected())
			{
				laTtlAddlData.setChrgAddlTknTrlrFeeIndi(1);
			}
			else
			{
				laTtlAddlData.setChrgAddlTknTrlrFeeIndi(0);
			}

			if (getchkVerifiedHeavyVehUseTax().isSelected())
			{
				laRegData.setHvyVehUseTaxIndi(1);
			}
			else
			{
				laRegData.setHvyVehUseTaxIndi(0);
			}

			if (getchkVINCertificationWaived().isSelected())
			{
				laTtlData.setInspectnWaivedIndi(1);
			}
			else
			{
				laTtlData.setInspectnWaivedIndi(0);
			}

			if (getchkDOTProofRequired().isSelected())
			{
				laVehData.setDotStndrdsIndi(1);
			}
			else
			{
				//needed for spcl examination
				//if it is unselected now but before coming
				//to this screen it was selected then spcl exmn 
				//	needed
				if (((MFVehicleData) laTtlValObj.getMfVehOrig())
					.getVehicleData()
					.getDotStndrdsIndi()
					== 1)
				{
					laTtlData.setTtlExmnIndi(1);
				}
				// defect 5628
				// set flood damage to 0
				laVehData.setDotStndrdsIndi(0);
				// end eefect 5628
			}

			if (getchkDiesel().isSelected())
			{
				laVehData.setDieselIndi(1);
			}
			else
			{
				laVehData.setDieselIndi(0);
			}

			if (getchkFloodDamage().isSelected())
			{
				laVehData.setFloodDmgeIndi(1);
			}
			else
			{
				//needed for spcl examination
				//if it is unselected now but before coming
				//to this screen it was selected then spcl exmn 
				//	needed
				if (((MFVehicleData) laTtlValObj.getMfVehOrig())
					.getVehicleData()
					.getFloodDmgeIndi()
					== 1)
				{
					laTtlData.setTtlExmnIndi(1);
				}
				// defect 5628
				// set flood damage to 0
				laVehData.setFloodDmgeIndi(0);
				// end defect 5628
			}

			if (getchkReconstructed().isSelected())
			{
				laVehData.setReContIndi(1);
			}
			else
			{
				//needed for spcl examination
				//if it is unselected now but before coming
				//to this screen it was selected then spcl exmn 
				//	needed
				if (((MFVehicleData) laTtlValObj.getMfVehOrig())
					.getVehicleData()
					.getReContIndi()
					== 1)
				{
					laTtlData.setTtlExmnIndi(1);
					laVehData.setReContIndi(0);
				}
				// defect 5628
				// set flood damage to 0
				laVehData.setReContIndi(0);
				// end defect 5628
			}

			if (getchkSurvivorshipRights().isSelected())
			{
				laTtlData.setSurvshpRightsIndi(1);
			}
			else
			{
				laTtlData.setSurvshpRightsIndi(0);
			}

			if (getchkGovernmentOwned().isSelected())
			{
				laTtlData.setGovtOwndIndi(1);
			}
			else
			{
				laTtlData.setGovtOwndIndi(0);
			}

			if (getchkExempt().isSelected())
			{
				laRegData.setExmptIndi(1);
			}
			else
			{
				laRegData.setExmptIndi(0);
			}
			// defect 10317
			// Set the tire type
			// Use common constant to set tire type
			if (getradioPneumatic().isSelected())
			{
				//caRegData.setTireTypeCd("P");
				laRegData.setTireTypeCd
					(CommonConstant.TIRE_TYPE_PNEUMATIC);
			}
			else if (getradioSolid().isSelected())
			{
				laRegData.setTireTypeCd(CommonConstant.TIRE_TYPE_SOLID);
			}
			// end defect 10317
			// defect 6509
			// Initialize the State/Country
			laTtlData.setSalvStateCntry(CommonConstant.STR_SPACE_EMPTY);
			// end defect 6509
			// Set the rebuilt salvage choice
			if (getradioNotRebuilt().isSelected())
			{
				laVehData.setRecondCd(TitleConstant.NOT_REBUILT);
			}
			else if (getradioRebuiltSalvageLossUnknown().isSelected())
			{
				laVehData.setRecondCd(
					TitleConstant.SALVAGE_LOSS_UNKNOWN);
			}
			else if (getradioRebuiltSalvage7594PctLoss().isSelected())
			{
				laVehData.setRecondCd(
					TitleConstant.REBUILT_SALVAGE_75_94);
			}
			else if (
				getradioRebuiltSalvage95PctPlusLoss().isSelected())
			{
				laVehData.setRecondCd(
					TitleConstant.REBUILT_SALVAGE_95_PLUS);
			}
			else if (getradioRebuiltSalvageIssuedBy().isSelected())
			{
				laVehData.setRecondCd(
					TitleConstant.REBUILT_SALVAGE_ISSUED);
				laTtlData.setSalvStateCntry(
					gettxtSalvageStateCountry().getText().trim());
			}
			// defect 10246 
			// Set the privacy act option
			//			if (getradioNone().isSelected())
			//			{
			//				laOwnData.setPrivacyOptCd(0);
			//			}
			//			else if (getradioIndividual().isSelected())
			//			{
			//				laOwnData.setPrivacyOptCd(1);
			//			}
			//			else if (getradioCommercial().isSelected())
			//			{
			//				laOwnData.setPrivacyOptCd(2);
			//			}
			//			else if (getradioBoth().isSelected())
			//			{
			//				laOwnData.setPrivacyOptCd(3);
			//			}
			// end defect 10246 
			// Set the apprehended county number
			laRegData.setApprndCntyNo(ciAppCnty);
			laTtlAddlData.setApprhndFndsCntyNo(ciAppCnty);
			if (ciAppCnty == 0)
			{
				laTtlValObj.setRegPnltyChrgIndi(0);
			}
			else
			{
				laTtlValObj.setRegPnltyChrgIndi(1);
			}
			// defect 9971 
			if (getchkETitle().isEnabled()
				&& getchkETitle().isSelected())
			{
				laTtlData.setETtlCd(TitleConstant.ELECTRONIC_ETTLCD);
			}
			// defect 10366 
			// VTRTtlEmrgCd1 refactored to PvtLawEnfVehCd
			// defect 10153
			if (getchkPrivateLawEnfVeh().isSelected())
			{
				laTtlData.setPvtLawEnfVehCd("1");
			}
			else
			{
				laTtlData.setPvtLawEnfVehCd(
					CommonConstant.STR_SPACE_ONE);
			}
			// end defect 10153
			// end defect 10366 

			// defect 10252
			laTtlData.setLemonLawIndi(
				getchkManufacturerBuyback().isSelected() ? 1 : 0);
			// end defect 10252 

			// Set the document type code moved to TTL002
			// end defect 9971 
		}
		// end defect 9368 
		catch (NullPointerException aeNPE)
		{
			RTSException laRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeNPE);
			laRTSEx.displayError(this);
		}
		return laVehInqData; 
	}

	/**
	 * Set Rebuilt Salvage Choice 
	 * 
	 */
	public void setupRebuiltSalvageChoice()
	{
		TitleValidObj laTtlValidObj =
			(TitleValidObj) caVehInqData.getValidationObject();

		MFVehicleData laOrigMFVehData =
			(MFVehicleData) laTtlValidObj.getMfVehOrig();

		int liOrigDocTypeCd =
			laOrigMFVehData.getTitleData().getDocTypeCd();

		if (caVehInqData.getMfDown() == 1
			|| caVehInqData.getNoMFRecs() == 0)
		{
			if (csTransCd.equals(TransCdConstant.DTAORD)
				|| getController().getTransCode().equals(
					TransCdConstant.DTANTD))
			{
				// DTA expects either 0 or 1 on diskette
				if (caVehData.getRecondCd() == null
					|| caVehData.getRecondCd().equals(
						CommonConstant.STR_ZERO))
				{
					getradioNotRebuilt().setSelected(true);
				}
				else if (caVehData.getRecondCd().equals("1"))
				{
					getradioRebuiltSalvageLossUnknown().setSelected(
						true);
				}
			}
			else
			{
				getradioNotRebuilt().setSelected(true);
			}
		}
		else
		{
			// Salvage Certificate 
			if (caVehData.getRecondCd() != null
				&& caVehData.getRecondCd().equals("1")
				|| (liOrigDocTypeCd
					== DocTypeConstant.SALVAGE_CERTIFICATE_NO_REGIS
					|| liOrigDocTypeCd
						== DocTypeConstant.SALVAGE_CERTIFICATE))
			{
				getradioRebuiltSalvageLossUnknown().setSelected(true);
				// disable rebuilt salvage choices
				disableRebuiltSalvage();
			}
			// Salvage Vehicle Title 
			else if (
				caVehData.getRecondCd() != null
					&& caVehData.getRecondCd().equals("2")
					|| (liOrigDocTypeCd
						== DocTypeConstant.SALV_TITLE_DAMAGED_NO_REG
						|| liOrigDocTypeCd
							== DocTypeConstant.SALV_TITLE_DAMAGED))
			{
				getradioRebuiltSalvage7594PctLoss().setSelected(true);
				// disable rebuilt salvage choices
				disableRebuiltSalvage();
			}
			// Non-Repairable 
			else if (
				caVehData.getRecondCd() != null
					&& caVehData.getRecondCd().equals("3")
					|| (liOrigDocTypeCd
						== DocTypeConstant.NONREPAIR_95_PLUS_LOSS_NO_REG
						|| liOrigDocTypeCd
							== DocTypeConstant.NONREPAIR_95_PLUS_LOSS))
			{
				getradioRebuiltSalvage95PctPlusLoss().setSelected(true);
				// disable rebuilt salvage choices
				disableRebuiltSalvage();
			}
			else if (
				caVehData.getRecondCd() != null
					&& caVehData.getRecondCd().equals("4"))
			{
				getradioRebuiltSalvageIssuedBy().setSelected(true);
				gettxtSalvageStateCountry().setText(
					caTtlData.getSalvStateCntry());
				// disable rebuilt salvage choices
				disableRebuiltSalvage();
			}
			else
			{
				getradioNotRebuilt().setSelected(true);
			}
		}
	}

	/**
	 * Set isProcessed.
	 * 
	 * @param abNewProcessed boolean
	 */
	public void setRecordPrcs(boolean abNewProcessed)
	{
		cbProcessed = abNewProcessed;
	}

	/**
	 * Verify Apprehended County.
	 * 
	 * @param asAppCnty String
	 * @return boolean
	 */
	private boolean verifyApprehendedCounty(String asAppCnty)
	{
		boolean lbRtn = true;

		// defect 10290 
		if (!asAppCnty.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			ciAppCnty = Integer.parseInt(asAppCnty);

			if (ciAppCnty != 0)
			{
				FrmCountyConfirmCTL002 laCntyCnfrm =
					new FrmCountyConfirmCTL002(
						getController().getMediator().getDesktop(),
						asAppCnty,
						OfficeIdsCache.getOfcName(ciAppCnty));

				if (laCntyCnfrm.displayWindow()
					== FrmCountyConfirmCTL002.NO)
				{
					gettxtApprhndCntyNo().requestFocus();
					lbRtn = false;
				}
			}
		}
		// end defect 10290 
		return lbRtn;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel12()
	{
		if (jPanel1 == null)
		{
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.add(getchkChargeTitleFee(), null);
			jPanel1.add(getchkChargeTitleTERPFee(), null);
			jPanel1.add(getchkChargeRegTransferFee(), null);
			// defect 9584 
			// jPanel1.add(getchkChargeTransferPenalty(), null);
			// end defect 9584
			jPanel1.add(getchkChargeRegEmissionFee(), null);
			jPanel1.add(getchkSurvivorshipRights(), null);
			jPanel1.add(getchkNewPlatesDesired(), null);
			// defect 11030 
			jPanel1.add(getchkPTOTrnsfr(), null);
			// end defect 11030 
			
			// defect 10153
			jPanel1.add(getchkPrivateLawEnfVeh(), null);
			// end defect 10153
			jPanel1.setBounds(8, 16, 222, 206);
			jPanel1.add(getchkChargeRebuiltSalvageFee(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel2()
	{
		if (jPanel2 == null)
		{
			jPanel2 = new JPanel();
			jPanel2.setLayout(null);
			jPanel2.add(getchkSpecialExamNeeded(), null);
			jPanel2.add(getchkMailInTransaction(), null);
			jPanel2.add(getchkChargeAddlTokenTrailerFee(), null);
			jPanel2.add(getchkVerifiedHeavyVehUseTax(), null);
			jPanel2.add(getchkVINCertificationWaived(), null);
			jPanel2.add(getchkDOTProofRequired(), null);
			// defect 9367
			jPanel2.add(getchkBuyerTag(), null);
			// Add PltFrnsfrFee
			jPanel2.add(getchkManufacturerBuyback(), null);
			jPanel2.setBounds(231, 16, 206, 205);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel3
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel3()
	{
		if (jPanel3 == null)
		{
			jPanel3 = new JPanel();
			jPanel3.setLayout(null);
			jPanel3.add(getchkDiesel(), null);
			jPanel3.add(getchkFloodDamage(), null);
			jPanel3.add(getchkReconstructed(), null);
			jPanel3.add(getchkAddlEvidenceSurrendered(), null);
			jPanel3.add(getchkGovernmentOwned(), null);
			jPanel3.add(getchkExempt(), null);
			// defect 9367
			jPanel3.add(getchkETitle(), null);
			// Add BuyerTag
			
			// defect 10007
			jPanel3.add(getchkTowTruckCert(), null);
			// end defect 10007
			jPanel3.setBounds(437, 16, 190, 202);
		}
		return jPanel3;
	}
	/**
	 * This method initializes ivjchkETitle
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkETitle()
	{
		if (ivjchkETitle == null)
		{
			ivjchkETitle = new JCheckBox();
			// defect 10153
			//ivjchkETitle.setBounds(5, 163, 92, 21);
			ivjchkETitle.setText("ETitle");
			ivjchkETitle.setSize(new Dimension(92, 21));
			ivjchkETitle.setLocation(new Point(0, 140));
			ivjchkETitle.setMnemonic(KeyEvent.VK_T);
		}
		return ivjchkETitle;
	}

	/**
	 * This method initializes ivjchkTowTruckCert
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getchkTowTruckCert() {
		if(ivjchkTowTruckCert == null) 
		{
			try
			{
				ivjchkTowTruckCert = new javax.swing.JCheckBox();
				ivjchkTowTruckCert.setName("ivjchkTowTruckCert");
				ivjchkTowTruckCert.setMnemonic(java.awt.event.KeyEvent.VK_O);
				ivjchkTowTruckCert.setSize(new Dimension(194, 21));
				ivjchkTowTruckCert.setLocation(new Point(0, 162));
				ivjchkTowTruckCert.setText("Verified Tow Truck Certificate");
			}
			catch (Throwable aeIvjEx)
			{
				handleException(aeIvjEx);
			}
		}
		return ivjchkTowTruckCert;
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"	