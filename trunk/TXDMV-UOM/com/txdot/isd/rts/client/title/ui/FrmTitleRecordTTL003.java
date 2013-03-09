package com.txdot.isd.rts.client.title.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.registration.business.RegistrationSpecialExemptions;
import com.txdot.isd.rts.client.registration.business.RegistrationVerify;
import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmTitleRecordTTL003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * T Pederson	04/26/2002	Corrected doClassPltStckrCheck to check 
 *							RegisPltStkr cache properly
 *							and stop os/2 window from flashing.	
 * T Pederson   04/30/2002	Don't allow spaces to be entered in input 
 *							field
 * T Pederson  	05/10/2002 	Changed doClassPltStckrCheck to check 
 *							OffHwyUseIndi
 * T Pederson  	05/15/2002  Corrected doCheckIfRegisMustChange by adding 
 *							parentheses to last check in if statement
 * T Pederson  	05/21/2002  Added blank field validation for make and 
 *							body style fields CQU100004037.
 * RArredondo	07/01/2002	Fixed defect CQU100004336--deleted scroll 
 * 							bar
 * J Rue		08/02/2002	Defect 4648, Determine which method to call 
 *							to initialize data for Record Not Applicable 
 *							DlrTitle or .. 
 *							method actionPerformed()
 * MAbs			08/29/2002	PCR 41isChangeRegis
 * MAbs			09/17/2002	PCR 41 Integration
 * J Rue 		09/13/2002	Defect 4486, Set plate age = 99. This will 
 *							force issuance of a new plate and set the
 *						 	title additional info screen up propertly. 
 *							method doInvalidRegis(), setData().
 * J Rue/		10/14/2002	Defect 4864, Use the original RegPltCd to 
 *							retrieve the InvPrcsCd from ItemCodes table.
 *	RArrendondo			  	method setDataToDataObject()
 * Min Wang 	10/16/2002	Defect 4834, Modified getlblVehicleClass().
 * MAbs			10/22/2002	Fixed defect in PCR 41 code addition
 * K Salvi		10/30/2002	CQU100004905 fix - populate width if atleast 
 *							one of feet and inches is not empty.
 * J Rue		11/04/2002	Defect 4970, Set ValidationObject()).
 * 							isChangeRegis() to ensure message (84) is 
 * 							displayed correctly. 
 *							method actionPerform()
 * J Rue		11/08/2002	Defect 4823,Modify process to enable/disable 
 *							fields on TTL003. 
 *							method disableFields(), 
 *							new method enableDisableFlag, setEnableFlag, 
 *							isEnableFlag
 * J Rue		11/26/2002	Defect 5140, initialize text to 0. method 
 *							getlblPlateAge.
 * B Arredondo	12/16/2002	Defect# 5147. Made changes for the user help 
 *							guide so had to make changes
 *				12/19/2003	in actionPerformed().
 * J Rue		01/16/2003	Fixed defect 4823. Modified code to display 
 *							odometer reading when vehicle class is misc.
 *							Modified method disableFields().
 * B Arredondo	01/20/2003	Defect 5213. Modified visual composition to 
 *							align the text boxes to be able to
 *							show 22 characters in the vin.
 * S Govindappa 01/27/2003  Fixed defect 5303. Made changes to 
 * 							disableFields methods to disable the 
 * 							Odometer brand choice depending on the 
 * 							default Odometer brand selection.
 * B Arredondo	03/24/2003	Defect 4966, made changes to 
 * 							displayedmnemonic in visual composition so 
 * 							'O' can be underlined.
 *							Change made in method 
 *							getstcLblOdometerReading.
 * J Rue		08/18/2003	Defect 6377, Initialize value if not 
 *							required and not DTA. 
 *							method disableFields().
 * J Rue		12/11/2003	Use original data when testing for 
 * 							RegClassCd. 
 *							Previous code used current RegClassCd
 *							modified doTitleTypeNotCorrect() 
 *							Defect 6198, Ver 5.1.5.2
 * K Harrell	04/05/2004	5.2.0 Merge. JavaDoc Work
 *							Ver 5.2.0 
 * K Harrell	05/25/2004	Default values on RegExpMo,RegExpYr,RegClass
 *							had been updated to 0,2005,3 respectively
 *							modify getlblRegExpMo(), getlblRegExpYr(),
 *							 getlblRegClass()
 *							defect 7117  Ver 5.2.0
 * K Harrell	06/01/2004	Reinstate the builderdata from 5.2.0
 *							defect 6978 Ver 5.2.0
 * J Zwiener	07/02/2004	Text "Select for expired registration" was
 *							clipped.
 *							modified thru Visual Comp.
 *							defect 6939 Ver 5.2.1
 * J Zwiener	11/12/2004	Retain "Select for Expired Registraton" 
 * 							button choice after going into 
 * 							"Change Registration"
 *							modify disableFields() & Declaration
 *							defect 6500 Ver 5.2.2
 * B Hargrove	11/16/2004  Change errmsg process so that they beep:
 *							model year, vehicle tonnage (also edit for
 *							model year was done twice,commented one out)
 *							see also : FrmNOTitleRecordTTL004.
 *							            actionPerformed()
 *							add error messages to RTS_ERR_MSGS
 *							modified actionPerformed()
 *							defect 7062, 7064 Ver 5.2.2
 * B Hargrove	11/22/2004  Mnemonic for Odometer Reading was not 
 * 							showing.
 *							As per Robin\Min : moved 
 *							ivjstcLblOdometerReading.
 *							setDisplayedMnemonic(79)to next line below 
 *							"// user code begin {1}\".
 *							modified getstcLblOdometerReading()
 *							defect 4966 Ver 5.2.2
 * Min Wang		11/23/2004	Carrying capacity should be cleared so that 
 *							when empty weight is changed (increased in 
 *							this case) it will not cause gross weight to
 *							go over max allowed of 80.000 lbs.
 *							modify disableFields()
 *							defect 7587 Ver 5.2.2 
 * B Hargrove	11/24/2004  Cleanup method documentation. Remove blank
 *							lines and add comment. 
 *							modified getstcLblOdometerReading()
 *							defect 4966 Ver 5.2.2
 * B Hargrove	11/30/2004  Format code.Standardize version etc comments
 *							modified getstcLblOdometerReading(),
 *							actionPerformed()
 *							defect 4966, 7064 Ver 5.2.2
 * B Hargrove	12/07/2004  Move comment verbiage on separate line.
 *							modify actionPerformed()
 *							defect 4966, 7064 Ver 5.2.2
 * J Rue		01/03/2005	Display ErrMsg 84 for Out Of Scope plates, 
 *							DTA
 *							modify actionPerformed(),
 *							doCheckIfRegisMustChange()
 *							defect 6818 Ver 5.2.2
 * J Rue		02/01/2005	Comment out code that set RegInvalidIndi for
 *							DTA.
 *							modify doInvalidRegis()
 *							defect 7934 Ver 5.2.2
 * K Harrell &	02/24/2005	Did not prompt for PLP
 *	J Rue					modify doCheckIfRegisMustChange(),
 *							actionPerformed()
 *							(Rollback of 6818) 
 *							defect 8033 Ver 5.2.2 Fix 2	
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * K Harrell	03/22/2005	Merge of defect 8033 (02/25/2005) 
 * 							Ver 5.2.3 
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/19/2005	Remove unused methods, imports, variables
 * 							deprecated displayError(String, String)
 * J Rue		05/05/2005	Correct Java tabbing into a disable
 * 							VehOdoBrand comboBox.
 * 							modify focusGain(), focusLost(),keyPressed()
 * 		`					gettxtVehicleOdometerReading()
 * 							getcomboOdometerBrandChoice()
 * 							defect 7895 Ver 5.2.3
 * K Harrell	06/12/2005  ClassToPlate, PlateToSticker Implementation
 * 							renamed doClassPltStckrCheck() to 
 * 							doClassPltStkrCheck()
 * 							modify doClassPltStkrCheck()
 * 							defect 8218 Ver 5.2.3 
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/23/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * K Harrell	09/12/2005  Added missing text for JPanel6()	
 * 							add constant EXPIRED_BORDER
 * 							modify getJPanel6() 
 * 							defect 7898 Ver 5.2.3
 * J Rue		10/28/2005	Add title to borders
 * 							modify getJPanel1()
 * 							defect 8416 Ver 5.2.3
 * J Rue		11/07/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3
 * J Zwiener	11/14/2005	Set expired reason default for Seasonal Ag
 *                          to Valid/disabled.
 *							add variable cbSeasonalAg
 *							remove variable cbInitRadio
 *							modify disabledFields()
 *							defect 8404 Ver 5.2.2 Fix 7
 * J Zwiener	11/14/2005	Set expired reason default for expired
 *							vehicles to Invalid/enabled.
 *							modify disabledFields()
 *							defect 8390 Ver 5.2.2 Fix 7
 * J Rue		11/21/2005	Change "tilte" to "title"
 * 							modify getJPanel4()
 * 							defect 7898 Ver 5.2.3
 * J Zwiener	11/27/2005	Null pointer when referencing CommonFees data
 *							modify disableFields()
 *							defect 8439 Ver 5.2.2 Fix 7
 * J Rue		12/13/2005	Comment out setSelected(true) for arrow key
 * 							movement on radio buttons
 * 							modify KeyPressed()
 * 							defect 7898 Ver 5.2.3
 * T Pederson	12/14/2005	Added a temp fix for the JComboBox problem.
 * 							modify initComboBox().
 * 							defect 8479 Ver 5.2.3
 * K Harrell	12/29/2005	Complete implementation of defect 8033
 * 							modify doCheckIfRegisMustChange(),
 *							actionPerformed()
 * 							Ver 5.2.3   
 * T. Pederson	12/30/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * Jeff S.		01/04/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyReleased(), ciSelectedNum 
 * 							modify initialize(), getJPanel6(), 
 * 								getradioCitation(),  getradioInvalid(),
 * 								getradioValid(), keyPressed()
 * 							defect 7898 Ver 5.2.3
 * Jeff S.		01/06/2006	Made changes to handle the recalc on the 
 * 							focus lost of the odometer txt field.  This 
 * 							will be handled the same both the TTL003 and
 * 							the TTL004 screen.
 * 							add cbTabKeyPressed
 *							modify actionPerformed(), focusLost(), 
 *								gettxtVehicleOdometerReading(),
 *								keyPressed(), focusGained()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	01/17/2006	Alignment management. Ensure assignment
 * 							of brand when change to exempt and press
 * 							Enter. Also, modify via Visual Editor to 
 * 							provide sufficient room for 
 * 							"Registration Purposes Only"  
 * 							modify setDataToDataObject()  
 * 							defect 7898 Ver 5.2.3
 * B Hargrove	02/02/2006	Ensure Odometer Reading is not blank before
 * 							trying to set the Brand.
 * 							modify setDataToDataObject()  
 * 							defect 7898 Ver 5.2.3
 * K Harrell	02/06/2006	Convert I/O to 1/0 in Body VIN
 *							add convertBodyVIN()
 *							modify actionPerformed(),focusLost(),
 *							gettxtVehicleBodyVIN()  
 *							defect 8523 Ver 5.2.2 Fix 8
 * K Harrell	02/17/2006	Check for null in odometer brand selection 
 * 							to handle bad rcd, e.g. TRLR w/ ODO reading   
 * 							modify setDataToDataObject() 
 * 							defect 7898 Ver 5.2.3   
 * J Rue		04/07/2006	Do not set list box (-1) if disabled and
 * 							 not EXEMPT
 * 							modify keyPressed()
 * 							defect 8639 Ver 5.2.3
 * T Pederson	09/08/2006	Added button to get the presumptive value
 * 							of vehicle displayed using VIN and odometer
 * 							reading.
 * 							add getbtnSPV()
 *							modify actionPerformed(), getJPanel(),
 *							keyPressed()
 * 							defect 8926 Ver 5.2.5
 * Jeff S.		09/15/2006	Added the Office of Issuance as a value
 * 							that is passed from the client so that
 * 							we can send it to BlackBook for reporting.
 * 							Added Mnemonic to SPV button.
 * 							add SPV
 * 							modify actionPerformed()
 * 							defect 8926 Ver 5.2.5
 * B Hargrove   09/28/2006 	If not a Standard Exempt set Exempt Indi off
 * 							modify setDataToDataObject()   
 * 							defect 8900 Ver Exempts 
 * T Pederson	09/28/2006	Changed call for converting vin i's and o's 
 * 							to 1's and 0's to combined non-static 
 * 							method convert_i_and_o_to_1_and_0(). 
 * 							delete getBuilderData()
 * 							modify convertBodyVIN(), focusLost(), 
 * 							setDataToDataObject()
 * 							defect 8902 Ver Exempts
 * K Harrell	10/07/2006	Use CommonFeesCache.isStandardExempts()
 * 							add'l variable name cleanup
 * 							modify setDataToDataObject() 
 * 							defect 8900 Ver Exempts
 * K Harrell	10/12/2006	Add "/" between Exp Mo & Exp Yr
 * 							add ivjLblSlash
 * 							add getivjlblSlash()
 * 							modify getjPanel4()
 * 							defect 8900 Ver Exempts
 * B Hargrove   10/12/2006 	If Standard Exempt, follow Registration 
 * 							Invalid process
 * 							modify doInvalidRegis()   
 * 							defect 8900 Ver Exempts 
 * Min Wang		10/10/2006	New Requirement for handling plate age 
 * 							modify doInvalidRegis(), setData()
 *							defect 8901 Ver Exempts
 * K Harrell	11/01/2006	Valid & Disabled Expired Reason Radio 
 * 							Buttons; Combine w/ Seasonal Ag logic
 * 							Class cleanup
 *							add cbOrigRegInvalid
 * 							modify disableFields()
 * 							defect 8900 Ver Exempts
 * K Harrell	11/08/2006	Expand text area for Registration Class 
 * 							Description via Visual Editor 
 * 							defect 8900 Ver Exempts 
 * Min Wang		11/08/2006  Using Integer.MAX_VALUE instead of 99 to 
 * 							verify Invalid RegPltAge.
 * 							modify doInvalidRegis(),setData(),
 * 							setDataToDataObject()
 * 							defect 8900 Ver Exempts
 * K Harrell	11/13/2006	Do not make Reg Invalid if Corrected Title
 * 							& Standard Exempt	
 * 							modify setData(), doInvalidRegis()  
 * 							defect 8900 Ver Exempts
 * K Harrell	11/28/2006	Reset RegPltAge of Integer.MAX_VALUE.  Not
 * 							always reset if no inventory issued.
 * 							modify setDataToDataObject() 
 * 							defect 9037 Ver Exempts
 * K Harrell	11/30/2006	Do not treat as RegInvalid if Standard Exempt
 * 							and RejCor.  Do not reset ExemptIndi if 
 * 							RejCor. SetMustChangePltIndi on RegInvalid, 
 * 							(Standard Exempt && !Corrected Title && 
 * 							!REJCOR) 
 * 							modify setData(), doInvalidRegis()
 * 							defect 9041  Ver Exempts							
 * B Hargrove	01/29/2007	Check for Exempt Indicator (not just 
 * 							Standard Exempt) when determining to set
 * 							as RegInvalid. 
 * 							modify doInvalidRegis()
 * 							defect 9093  Ver Region Credit Card
 * T Pederson	02/12/2007	Added organization name to screen.  
 * 							modify setData(). 
 * 							defect 9123 Ver Special Plates
 * K Harrell	03/09/2007 	Removing reference to InvProcsngCd.
 * 							  use !SystemProperty.isHQ()
 * 							modify doCheckIfRegisMustChange(),
 * 							 setDataToDataObject(),doTitleTypeNotCorrect()
 * 							delete displayError()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/08/2007	CommonValidations.convert_i_and_o_to_1_and_0() 
 * 							call is now static
 * 							defect 9085 Ver Special Plates 
 * K Harrell	04/16/2007	Added call to verify Valid Special Plate
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates  
 * K Harrell	05/30/2007	Update TTL003 upon selection of Special Plate
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/09/2007	Throw error msg 84 if Special Plate and press 
 * 							Cancel on REG008.
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/01/2007	Continue to throw err msg 84 if InvProcsngCd
 * 							2 or 3. 
 * 							modify doCheckIfRegisMustChange()
 * 							defect 9198 Ver Special Plates
 * K Harrell	09/07/2007	Use getRegPltAge(false) for display of 
 * 							Special Plate Plate Age 
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/04/2007	Initialize lsOrgNo to null to prevent 
 * 							OrgName lookup
 * 							modify setData()
 * 							defect 9123 Ver Special Plates   
 * B Hargrove	10/29/2007	If 'isChangeRegCls() is false, the 'Change 
 * 							Registration' button is disabled (set in 
 * 							doClassPltStkrCheck() when, for example, 
 * 							this is a Corrected Title).				
 *							If there is no chance to 'Change Registration', 
 *							do not need to edit Special Plate.
 * 							modify actionPerformed()
 * 							defect 9398 Ver Special Plates
 * B Hargrove	12/31/2008	Add check for Vehicle Class when checking
 * 							Trk<=1 (Apportioned (RegClassCd 6) does not
 * 							have 'Appr<=1' type of RegClassCd, so must 
 * 							check VehClassCd = 'Trk <=1').
 * 							modify actionPerformed()
 * 							defect 8703 Ver Defect_POS_D
 * K Harrell	01/08/2009	Remove reference to RegisData.setOrgNo() 
 * 							modify setData() 
 * 							defect 9912 Ver Defect_POS_D   
 * J Rue		01/09/2009	createDlrEmptyVehInqObj() was move to 
 * 							VehicleInquiryData class. Reference new 
 * 							location
 * 							modify actionPerformed()
 * 							defect 8631 Ver defect_POS_D
 * B Hargrove	01/10/2009	Add check for Vehicle Class when checking
 * 							Trk>1 so can get rid of hard-coded
 * 							RegClassCds in TitleClientUtilityMethods. 
 * 							modify actionPerformed()
 * 							defect 9910 Ver Defect_POS_D
 * J Zwiener	01/19/2009	Always clear out VehTon if not required
 * 							regardless if DTA or not.
 * 							modify disableFields()
 * 							defect 9500 Ver Defect_POS_D
 * J Rue		01/22/2009	Change createDlrEmptyVehInqObj() to a non
 * 							static call.
 * 							modify actionPerformed()
 * 							defect 8631 Ver Defect_POS_D
 * J Rue		01/22/2009	Remove parameters passed and returnd for 
 * 							VehicleInquiryData.createDlrEmptyVehInqObj()
 * 							modify actionPerformed()
 * 							defect 8631 Ver Defect_POS_D
 * B Hargrove	01/26/2009	Change edit from defect 9398. Check for 
 * 							Change Registration button is enabled or 
 * 							disabled to determine need to re-specify
 * 							a Special Plate. 
 * 							modify actionPerformed()
 * 							defect 9830 Ver Defect_POS_D
 * J Rue		01/22/2009	Clean up comments
 * 							modify actionPerformed()
 * 							defect 8631 Ver Defect_POS_D
 * K Harrell	03/05/2009	Modify screen to increase size of Remarks 
 * 							panel via Visual Composition. Use 
 * 							CommonConstant.FONT_JLIST in Remarks Listing.
 * 							modify getlstIndiDescription(), 
 * 							  doNoLienCheck()  
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	04/01/2009	Implement CommonConstant.MAX_INDI_NO_SCROLL
 * 							Addl class cleanup/member sort. 
 * 							modify getIndicators(), setData()  
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	06/09/2009	Class changed in refactor of TitleValidObj
 * 							modify actionPerformed()
 * 							defect 10035 Ver Defect_POS_F
 * K Harrell	07/02/2009	Implement new OwnerData. 
 * 							modify setData() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	12/19/2009	DTA Cleanup 
 * 							Do not reprocess setData() on RTSException
 * 							modify setData()
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	02/27/2010	Do not validate registration or allow 
 * 							modification of registration in CORTTL
 * 							delete isChangeRegCls() 
 * 							modify setData(), actionPerformed()
 * 							defect 10369 Ver POS_640  
 * K Harrell	03/19/2010	Disable Record Not Applicable button 
 * 							when Corrected Title. 
 * 							modify setData() 
 * 							defect 10416 Ver POS_640 
 * K Harrell	04/02/2010	Correct management of CarryingCapacity. 
 * 							Reset to 0 if not applicable on entry or 
 * 							after Change Registration.  
 * 							modify disableFields() 
 * 							defect 9533 Ver POS_640 
 * K Harrell	04/20/2010	Throw 208 error if ATV and not Off-Highway.
 * 							If Off-Highway, clear Plate, Plate Age, 
 * 							Expiration Mo/Yr, Type, OrgNo, Sticker, 
 * 							SpclPltRegisData 
 * 							add resetRegForOffHwyATV()   
 * 							modify doClassPltStkrCheck() 
 * 							defect 10453 Ver POS_640  
 * K Harrell	04/26/2010	add resetRegForOffHwy_ATV_ROV()
 * 							delete resetRegForOffHwyATV()
 * 							defect 10453 Ver POS_640 
 * K Harrell	06/19/2010	reevaluate Indicators on subsequent 
 * 							pass through setData()
 * 							add cbAlreadySet
 * 							delete cbIndicatorsDone 
 * 							modify setData(), getIndicators(), 
 * 							 checkHardStops(), checkSoftStops(), 
 * 							 actionPerformed()
 * 							defect 10507 Ver 6.5.0 
 * K Harrell	06/19/2010	Set Odometer to Exempt if appropriate based
 * 							on TIMA 
 * 							modify setData()
 * 							defect 10524 Ver 6.5.0 
 * B Hargrove 	10/28/2010  Token Trailer now does not print sticker
 * 							(even though it is not an Annual Plt).
 * 							Skip the 'plate to sticker' check.
 * 							modify doClassPltStkrCheck()
 * 							defect 10623 Ver 6.6.0
 * B Hargrove 	10/29/2010  Token Trailer now does not print sticker.
 * 							Comment out all changes. They want to store 
 * 							inv detail, just don't print sticker.
 * 							modify doClassPltStkrCheck()
 * 							defect 10623 Ver 6.6.0
 * T Pederson	01/31/2011	Added vehicle color drop down boxes 
 * 							add ivjcomboMajorColor, ivjcomboMinorColor,
 * 							ivjstcLblColorMajor, ivjstcLblColorMinor,
 * 							MAJORCOLOR, MINORCOLOR
 * 							add getcomboMajorColor(), getcomboMinorColor(),
 * 							populateMajorColor(), populateMinorColor(),
 * 							getMajorColorCdSelected(),
 * 							getMinorColorCdSelected(),
 * 							isMajorColorReqdButNotSelected(),
 * 							isMajorColorSameAsMinorColor(),
 * 							isMinorColorSelectedMajorColorNotSelected()
 * 							modify setData(), setDataToDataObject(),
 * 							actionPerformed()
 * 							defect 10689 Ver 6.7.0
 * K Harrell	03/17/2011	Veh Color Lost when Change Reg/VTR Auth
 * 							modify setData()
 * 							defect 10779 Ver 6.7.0
 * K Harrell	10/13/2011	Reset ETtlPrntDate  
 * 							modify setDataToDataObject() 
 * 							defect 10841 Ver 6.9.0 
 * K Harrell	10/15/2011	Implement CommonFees max/min weight validation
 * 							delete MAX_GROSS_WT
 * 							modify actionPerformed()
 * 							defect 10959 Ver 6.9.0 
 * K Harrell	10/15/2011	Reset Odometer when disabled
 * 							delete setChangeRegis(),
 * 							modify disableFields()
 * 							defect 11047 Ver 6.9.0
 * K Harrell	10/15/2011	Add checkbox for Travel Trailer 
 * 							add ivjchkTravelTrailer, get method
 * 							add TRAVEL_TRAILER
 * 							add handleTravelTrailer(),
 * 							  disableFields(Object,boolean) 
 * 							modify itemStateChanged(),disableFields(Object) 
 * 							defect 11049 Ver 6.9.0
 * B Hargrove	10/17/2011	Make Reg Invalid if titling DocTypeCd 11
 * 							(Insur - No Regis) so they have to go to
 * 							Class/Plt/Stkr screen.
 * 							modify doInvalidRegis()
 * 							defect 10047 Ver 6.9.0 
 * K Harrell	10/26/2011  ... cleanup in process
 * 							defect 11126 Ver 6.9.0 
 * B Hargrove	12/16/2011	Check put in for 10047 was put in the wrong
 * 							place. Comment that out and move it to 
 * 							correct place.
 * 							modify doInvalidRegis()
 * 							defect 11187 Ver 6.9.0 
 * B Woodson	02/09/2012	modify validateData()
 * 							defect 11228 Ver 6.10.0
 * K Harrell	02/10/2011	Ensure Odometer Brand is captured when modify
 * 							vehicle year, change odometer from Exempt and
 * 							use keyboard Enter.
 * 							modify validateData() 
 * 							defect 11257 Ver 6.10.0 
 * K Harrell	02/14/2012	Check for null in itemStateChanged() 
 * 							modify itemStateChanged()
 * 							defect 11286 Ver 6.9.0 
 * K Harrell	05/25/2012	Add fields/logic for Replica Year/Make
 * 							add ivjstcLblReplicaYearMake,
 * 							 ivjtxtReplicaVehicleModelYear,
 * 							 ivjtxtReplicaVehicleMake, get methods
 * 							add REPLICA_YEAR_MAKE
 * 							modify getJPanel1(), validateData(),
 * 							 setDataToDataObject(),
 * 							 setVehicleDataToDisplay() 
 * 							defect 11368 Ver 6.10.1  
 * K Harrell	06/06/2012	Changed requirement:  Replica data should be
 * 							disabled for DTA.
 * 							modify setVehicleDataToDisplay() 
 * 							defect 11368 Ver 6.10.1  
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to display/capture vehicle information of an  
 * existing mainframe record.
 *
 * @version	6.10.1  			06/06/2012
 * @author	Ashish Mahajan
 * <br>Creation Date:			06/27/2001 20:54:25
 */
public class FrmTitleRecordTTL003
	extends RTSDialogBox
	implements ActionListener, FocusListener, ItemListener, KeyListener
{
	private RTSButton ivjbtnChangeRegistration = null;
	private RTSButton ivjbtnRecNotApplicable = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkFixedWeight = null;
	private JCheckBox ivjchkParkModel = null;
	// defect 10689 
	private JComboBox ivjcomboMajorColor = null;
	private JComboBox ivjcomboMinorColor = null;
	// end defect 10689 
	private JComboBox ivjcomboOdometerBrandChoice = null;
	private JComboBox ivjcomboTrailerTypeChoice = null;
	private JPanel ivjFrmTitleRecordTTL003ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel3 = null;
	private JPanel ivjJPanel4 = null;
	private JPanel ivjJPanel5 = null;
	private JPanel ivjJPanel6 = null;
	private JPanel jPanel = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblCntyName = null;
	private JLabel ivjlblDocumentNo = null;
	private JLabel ivjlblDocumentType = null;
	private JLabel ivjlblIssuedDate = null;
	private JLabel ivjlblNewTitleType = null;
	private JLabel ivjlblOrg = null;
	private JLabel ivjlblOwnerId = null;
	private JLabel ivjlblOwnerName = null;
	private JLabel ivjlblOwnerName2 = null;
	private JLabel ivjlblPlateAge = null;
	private JLabel ivjlblPlateNo = null;
	private JLabel ivjlblPlateType = null;
	private JLabel ivjlblRegClass = null;
	private JLabel ivjlblRegExpMo = null;
	private JLabel ivjlblRegExpYr = null;
	private JLabel ivjlblResComptCntyNo = null;
	private JLabel ivjlblSlash = null;
	private JLabel ivjlblVehicleClass = null;
	private JLabel ivjlblVehicleGrossWeight = null;
	private JList ivjlstIndiDescription = null;
	private JRadioButton ivjradioCitation = null;
	private JRadioButton ivjradioInvalid = null;
	private JRadioButton ivjradioValid = null;
	private JLabel ivjstcLblAge = null;
	private JLabel ivjstcLblBodyType = null;
	private JLabel ivjstcLblBodyVIN = null;
	private JLabel ivjstcLblCarryingCapacity = null;
	private JLabel ivjstcLblClass = null;
	// defect 10689 
	private JLabel ivjstcLblColorMajor = null;
	private JLabel ivjstcLblColorMinor = null;
	// end defect 10689 
	private JLabel ivjstcLblCounty = null;
	private JLabel ivjstcLblDocumentNo = null;
	private JLabel ivjstcLblEmptyWeight = null;
	private JLabel ivjstcLblExpires = null;
	private JLabel ivjstcLblFeet = null;
	private JLabel ivjstcLblGrossWeight = null;
	private JLabel ivjstcLblInches = null;
	private JLabel ivjstcLblIssued = null;
	private JLabel ivjstcLblNewTitleType = null;
	private JLabel ivjstcLblOdometerBrand = null;
	private JLabel ivjstcLblOdometerReading = null;
	private JLabel ivjstcLblOrg = null;
	private JLabel ivjstcLblOwner = null;
	private JLabel ivjstcLblPlate = null;
	private JLabel ivjstcLblTonnage = null;
	private JLabel ivjstcLblTrailerType = null;
	private JLabel ivjstcLblTrailerType2 = null;
	private JLabel ivjstcLblTravelTlrWidth = null;
	private JLabel ivjstcLblTrlLength = null;
	private JLabel ivjstcLblType = null;
	private JLabel ivjstcLblVehicleClass = null;
	private JLabel ivjstcLblVehicleModel = null;
	private JLabel ivjstcLblVIN = null;
	private JLabel ivjstcLblYearMake = null;
	// defect 8926
	private RTSButton btnSPV = null;
	// end defect 7898
	private RTSInputField ivjtxtBodyStyle = null;
	private RTSInputField ivjtxtMake = null;
	private RTSInputField ivjtxtModel = null;
	private RTSInputField ivjtxtTlrWidthFeet = null;
	private RTSInputField ivjtxtTlrWidthInches = null;
	private RTSInputField ivjtxtVehicleBodyVIN = null;
	private RTSInputField ivjtxtVehicleCarryingCapacity = null;
	private RTSInputField ivjtxtVehicleEmptyWeight = null;
	private RTSInputField ivjtxtVehicleOdometerReading = null;
	private RTSInputField ivjtxtVehicleTonnage = null;
	private RTSInputField ivjtxtTrlrLngth = null;
	private RTSInputField ivjtxtVIN = null;
	private RTSInputField ivjtxtYear = null;

	// Objects
	private MFVehicleData caMFVehicleData = null;
	private RegistrationClassData caRegClassData = null;
	private VehicleInquiryData caVehInqData = null;
	
	// defect 11126 
	private RegistrationData caRegData = null;
	private TitleData caTtlData = null; 
	private VehicleData caVehData = null; 
	private String csTransCd = new String();
	// end defect 11126 

	// boolean
	private boolean cbOrigRegInvalid = false;
	private boolean cbSeasonalAg = true;
	// defect 7898
	// added to handle the focuslost event
	// If KeyPressed has already handled it there is no need to.
	private boolean cbTabKeyPressed = false;

	// defect 10507
	private boolean cbAlreadySet = false;
	// end defect 10507 

	// Vector 
	private Vector cvIndicator = null;
	
	// defect 10689 
	private Vector cvVehColor = new Vector();
	// end defect 10689 

	// String Constants  
	private final static String AGE = "Age:";
	private final static String BODY_STYLE = "Body Style:";
	private final static String BODY_VIN = "Body VIN:";
	private static final String BRAND = "Brand:";
	private static final String CARRYING_CAPACITY =
		"Carrying Capacity:";
	private static final String CHANGE_REGISTRATION =
		"Change Registration";
	private static final String CITATION_PENTLY =
		"Citation/Charge Penalty";
	private static final String CLASS = "Class:";
	// defect 10689 
	private static final String MAJORCOLOR = "Major Color:";
	private static final String MINORCOLOR = "Minor Color:";
	// end defect 10689 
	private static final String COUNTY = "County:";
	private static final String DOCUMENT = "Document:";
	private static final String DOCUMENT_NO = "Document No:";
	private static final String EMPTY_WEIGHT = "Empty Weight:";
	private static final String EXEMPT = "EXEMPT";
	private static final String EXPIRED_BORDER =
		"Select for expired registration";
	private static final String EXPIRES = "Expires:";
	private static final String FIXED_WEIGHT = "Fixed Weight";
	private static final String FT = "Ft";
	private static final String GROSS_WEIGHT = "Gross Weight:";
	private static final String IN = "In";
	private static final String INVALID_REASON = "Invalid Reason";
	private static final String ISSUED = "Issued:";
	private static final String MISC = "MISC";
	private static final String MODEL = "Model:";
	private static final String N = "N";
	private static final String NEW_TITLE_TYPE = "New Title Type:";
	private static final String ODOMETER_READING = "Odometer Reading:";
	private static final String OWNER_ID = "Owner Id:";
	private static final String PARK_MODEL_TRAILER =
		"Park Model Trailer";
	private static final String PLATE = "Plate:";
	private static final String RECORD_NOT_APPLICABLE =
		"Record Not Applicable";
	private static final String REGISTRATION = "Registration:";
	private static final String SPV = "SPV";
	private static final String STOLEN_WAIVED_MSG =
		"Stolen Waived     TTL009";
	private static final String STR_N = "NOT ACTUAL MILEAGE";
	private static final String STR_X = "EXCEEDS MECH. LIMITS";
	private static final String TONNAGE = "Tonnage:";
	private static final String TOWTRUCK = "TOWTP";
	private static final String TRAILER = "Trailer";
	// defect 11049 
	private final static String TRAVEL_TRAILER = "Travel Trailer";
	// end defect 11049
	private static final String TRVL_TRL_LENGTH = "Trvl Tlr Length:";
	private static final String TRVL_TRL_WIDTH = "Trvl Tlr Width:";
	private static final String TYPE = "Type:";
	private static final String VALID_REASON = "Valid Reason";
	// Constants String
	private static final String VEH_STOLEN_MSG =
		"<b><center><font color=#ff0000> *** VEHICLE STOLEN ***</font><p><font color=#666699>Override Stolen Notice</font>";
	private static final String VEHICLE_CLASS = "Vehicle Class:";
	private static final String VIN = "VIN:";
	private static final String W = "W";
	private static final String X = "X";
	private static final String YEAR_MAKE = "Year/Make:";

	// Constant double
	// defect 11049
	//private static final String ZERO_ZERO = "0.0";
	//private static final double TRLRWIDTH12_5 = 12.0;
	private static final int INCHES_PER_FOOT = 12;
	// end defect 11049 

	// Constants int
	private static final int CARYCAP_MAX_LEN = 6;
	private static final int EMPTYWT_MAX_LEN = 6;
	private static final int GROSSWT4000 = 4000;
	private static final int INSURNOREG = 11;
	private static final int INT_ZERO = 0;
	private static final int LNGTH_MAX_LEN = 2;
	// defect 10959
	//private static final int MAX_GROSS_WT = 80000;
	// end defect 10959
	private static final int ODORDNG_MAX_LEN = 6;
	private static final int TONNAGE_MAX_LEN = 5;
	private static final int TRLNGTHFT_MAX_LEN = 2;
	private static final int TRLNGTHIN_MAX_LEN = 2;
	private static final int TRLRWT34000 = 34000;
	private static final int VEHBDY = 3;
	private static final int VEHBDYSTLY_MAX_LEN = 2;
	private static final int VEHMAKE_MAX_LEN = 4;
	private static final int VIN_MAX_LEN = 22;
	private static final int YR_MAX_LEN = 4;
	
	// defect 11049 
	private JCheckBox ivjchkTravelTrailer = null;
	// end defect 11049
	
	// defect 11368 
	private JLabel ivjstcLblReplicaYearMake = null;
	private RTSInputField ivjtxtReplicaVehicleModelYear = null;
	private RTSInputField ivjtxtReplicaVehicleMake = null;
	private final static String REPLICA_YEAR_MAKE = "Replica Year/Make:"; 
	// end defect 11368
	
	/**
	 * FrmTitleRecordTTL003 constructor
	 */
	public FrmTitleRecordTTL003()
	{
		super();
		initialize();
	}

	/**
	 * FrmTitleRecordTTL003 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmTitleRecordTTL003(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmTitleRecordTTL003 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmTitleRecordTTL003(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * This method initializes ivjtxtReplicaYear	
	 * 	
	 * @return RTSInputField	
	 */
	private RTSInputField gettxtReplicaVehicleModelYear()
	{
		if (ivjtxtReplicaVehicleModelYear == null)
		{
			ivjtxtReplicaVehicleModelYear = new RTSInputField();
			ivjtxtReplicaVehicleModelYear.setMaxLength(YR_MAX_LEN);
			ivjtxtReplicaVehicleModelYear.setInput(
					RTSInputField.NUMERIC_ONLY);
			ivjtxtReplicaVehicleModelYear.setLocation(new Point(120, 451));
			ivjtxtReplicaVehicleModelYear.setSize(new Dimension(40, 20));
		}
		return ivjtxtReplicaVehicleModelYear;
	}

	/**
	 * This method initializes ivjtxtReplicaVehicleMake	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private RTSInputField gettxtReplicaVehicleMake()
	{
		if (ivjtxtReplicaVehicleMake == null)
		{
			ivjtxtReplicaVehicleMake = new RTSInputField();
			ivjtxtReplicaVehicleMake.setMaxLength(VEHMAKE_MAX_LEN); 
			ivjtxtReplicaVehicleMake.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
			ivjtxtReplicaVehicleMake.setBounds(new Rectangle(167, 451, 49, 20));
		}
		return ivjtxtReplicaVehicleMake;
	}

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmTitleRecordTTL003 laFrmTitleRecordTTL003;
			laFrmTitleRecordTTL003 = new FrmTitleRecordTTL003();
			laFrmTitleRecordTTL003.setModal(true);
			laFrmTitleRecordTTL003
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent laWE)
				{
					System.exit(INT_ZERO);
				}
			});
			laFrmTitleRecordTTL003.show();
			java.awt.Insets insets = laFrmTitleRecordTTL003.getInsets();
			laFrmTitleRecordTTL003.setSize(
				laFrmTitleRecordTTL003.getWidth()
					+ insets.left
					+ insets.right,
				laFrmTitleRecordTTL003.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmTitleRecordTTL003.setVisibleRTS(true);
		}
		catch (Throwable leException)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			leException.printStackTrace(System.out);
		}
	}

	/**
	 * Invoked when user performs an action.  The user completes the  
	 * screen by making appropriate selections/entries and pressing 
	 * enter.
	 *
	 * Cancel or Help buttons respectively result in destroying the 
	 * window or providing appropriate help.
	 * 
	 * @param aaAE ActionEvent
	 * 
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
			
			// ENTER 
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 11126
				UtilityMethods.trimRTSInputField(this);
				recalcGrossWt(); 
				convertVIN(gettxtVIN());
				convertVIN(gettxtVehicleBodyVIN());
				
				if (validateData())
				{
					if (!checkHardStops() || !checkSoftStops())
					{
						return;
					}

					// Present Msg if Not In Scope; Continue  
					if (!UtilityMethods.isEmpty(caRegData.getRegPltCd())
							&& RegistrationSpecialExemptions.verifyPltTypeScope(
									caRegData)!= 0
									&& !SystemProperty.isHQ())
					{
						// 18 
						RTSException laEx = new RTSException(
								ErrorsConstant.ERR_NUM_PLATES_ONLY_AVAILABLE_FROM_VTR);
						laEx.displayError(this);
					}

					if (displayTTL009())
					{
						return;
					}
					doNoLienCheck();
					
					// Send a copy of the object to the next screen 
					getController().processData(
							AbstractViewController.ENTER,
							setDataToDataObject());
				}
				// end defect 11126 
			}
			// CANCEL 
			else if (aaAE.getSource()
					== getButtonPanel1().getBtnCancel())
			{
				// defect 4970
				// Set ChangeRegis to FALSE when ESC to previous screen
				TitleValidObj laTtlValidObj =
					(TitleValidObj) caVehInqData
					.getValidationObject();
				
				if (laTtlValidObj != null)
				{
					laTtlValidObj.setChangeRegis(false);
				}
				
				getController().processData(
						AbstractViewController.CANCEL,
						caVehInqData);
			}
			// HELP 
			else if (aaAE.getSource()
					== getButtonPanel1().getBtnHelp())
			{
				// defect 11126 
				handleHelp(); 
				// end defect 11126 
			}
			// SPV 
			else if (
					aaAE.getSource() == getbtnSPV())
			{
				handleSPV(); 
				
			}
			// CHANGE REGISTRATION 
			else if (aaAE.getSource()
					== getbtnChangeRegistration())
			{
				TitleValidObj laTtlValidObj =
					(TitleValidObj) caVehInqData
					.getValidationObject();

				laTtlValidObj.setFromTTL003(true);
				laTtlValidObj.setChangeRegis(true);
				getController().processData(
						VCTitleRecordTTL003.CHANGE_REG,
						caVehInqData);
			}
			// RECORD NOT APPLICABLE 
			else if (aaAE.getSource()
					== getbtnRecNotApplicable())
			{
				//Create a new Vehicle Inquiry Data,
				//	 Record Not Applicatable
				VehicleInquiryData laVehInqDataNew =
					new VehicleInquiryData();

				// save to vault if DTA
				if (UtilityMethods.isDTA(csTransCd))
				{
					// defect 8631
					//	createDlrEmptyVehInqObj() was
					//	 move to VehicleInquiryData
					caVehInqData
					.createDlrEmptyVehInqObj();
					laVehInqDataNew =
						(
								VehicleInquiryData) UtilityMethods
								.copy(
										caVehInqData);
					// end defect 8631
				}
				else
				{
					laVehInqDataNew =
						TitleClientUtilityMethods
						.createEmptyVehInqObj();
					laVehInqDataNew
					.setValidationObject(
							new TitleValidObj());
				}
				if (laVehInqDataNew
						.getValidationObject()
						!= null)
				{
					TitleValidObj laValidObj =
						(TitleValidObj) laVehInqDataNew
						.getValidationObject();
					laValidObj
					.setRecordNotApplicable(
							true);

				}
				laVehInqDataNew
				.getMfVehicleData()
				.getVehicleData()
				.setVin(
						caVehData.getVin());
				getController().processData(
						VCTitleRecordTTL003
						.RECOR_NOTAPPL,
						laVehInqDataNew);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Determine if any hard stops exist and have been cleared.
	 * 
	 * @return boolean
	 */
	private boolean checkHardStops()
	{
		// defect 10507
		// Streamline
		boolean lbValid = true;

		if (caVehInqData.getNoMFRecs() == 1)
		{
			if (IndicatorLookup.hasHardStop(cvIndicator)
				&& !caVehInqData.hasAuthCode())
			{
				getController().processData(
					VCTitleRecordTTL003.VTR_AUTH,
					caVehInqData);

				lbValid = caVehInqData.hasAuthCode();
			}
		}
		return lbValid;
		// end 10507 
	}

	/**
	 * Determine if any soft stops exist and have been cleared.
	 * 
	 * @return boolean 
	 */
	private boolean checkSoftStops()
	{
		// defect 10507
		// Streamline 
		boolean lbValid = true;

		if (caVehInqData.getNoMFRecs() == 1)
		{
			if (IndicatorLookup.hasSoftStop(cvIndicator))
			{
				caVehInqData.getVehMiscData().setSupvOvrideReason(
					IndicatorLookup.getSoftStopReasons(cvIndicator));

				getController().processData(
					VCTitleRecordTTL003.VTR_SUPV,
					caVehInqData);

				lbValid = caVehInqData.hasAuthCode();
			}
		}
		return lbValid;
		// end defect 10507 
	}

	/**
	 * convertVIN
	 * 
	 */
	public void convertVIN(RTSInputField aaRTSInputField)
	{
		String lsVin = aaRTSInputField.getText();
		
		lsVin = CommonValidations.convert_i_and_o_to_1_and_0(lsVin);
		
		aaRTSInputField.setText(lsVin);
	}
	
	/**
	 * This method is called to disable screen components based on 
	 * various checks.
	 * 
	 * @param aaData Object
	 */
	private void disableFields(Object aaData,boolean abNewRegisClass)
	{
		if (aaData != null)
		{
			boolean lbEnable = false; 
			TitleValidObj laVObj = (TitleValidObj) aaData;
			String lsVehCd = caVehData.getVehClassCd();
			int liRegCd =caRegData.getRegClassCd();
			int liEffDt = caVehInqData.getRTSEffDt();
			
			if(abNewRegisClass)
			{
				caRegClassData =
					RegistrationClassCache.getRegisClass(
						lsVehCd,
						liRegCd,
						liEffDt);
			}
			
			// defect 4823
			// Set Enable/Disable based on field required 
			// flag from RegisClass table
			if (caRegClassData != null)
			{
				// Vehicle Body VIN
				setFromRegisClassVehData(caRegClassData.getBdyVinReqd(), 
						getstcLblBodyVIN(), gettxtVehicleBodyVIN(), 
						caVehData.getVehBdyVin());

				// Fixed Weight &&  Carrying Capacity
				// Note that FxdWtReqd is not really Required. Means it is available. 
				
				// If Fixed, Carrying Capacity is not collected
				lbEnable = int2bool(caRegClassData.getFxdWtReqd());
				if (!lbEnable)
				{
					getchkFixedWeight().setSelected(false); 
				}
				getchkFixedWeight().setEnabled(lbEnable);
				
				int liValue = caRegClassData.getCaryngCapReqd();
				liValue = getchkFixedWeight().isSelected() ? 0 : liValue; 
				
				setFromRegisClassVehData(liValue, 
						getstcLblCarryingCapacity(), gettxtVehicleCarryingCapacity(), 
						""+caRegData.getVehCaryngCap());
				
				// Empty Weight
				setFromRegisClassVehData(caRegClassData.getEmptyWtReqd(), 
						getstcLblEmptyWeight(), gettxtVehicleEmptyWeight(), 
						""+caVehData.getVehEmptyWt());
				
				// Gross Weight
				recalcGrossWt(); 
				lbEnable = int2bool(caRegClassData.getEmptyWtReqd());
				getstcLblGrossWeight().setEnabled(lbEnable);  
				getlblVehicleGrossWeight().setEnabled(lbEnable);
				
				// Length
				setFromRegisClassVehData(caRegClassData.getLngthReqd(),getstcLblTrlLength(),
						gettxtTrlrLngth(),""+caVehData.getVehLngth());
				
				// Width - Feet  
				setFromRegisClassVehData(caRegClassData.getWidthReqd(),getstcLblTravelTlrWidth(),
						gettxtTlrWidthFeet(),(""+ (int)caVehData.getVehWidth()/INCHES_PER_FOOT));
				
				// Width - Inches
				setFromRegisClassVehData(caRegClassData.getWidthReqd(),getstcLblTravelTlrWidth(),
						gettxtTlrWidthInches(),(""+ (int)caVehData.getVehWidth() %INCHES_PER_FOOT));
				
				getstcLblFeet().setEnabled(int2bool(caRegClassData.getWidthReqd()));
				getstcLblInches().setEnabled(int2bool(caRegClassData.getWidthReqd()));
				
				// Tonnage 
				setFromRegisClassVehData(caRegClassData.getTonReqd(),getstcLblTonnage(),
						gettxtVehicleTonnage(),""+ caVehData.getVehTon());
				
				
				// *** Set VehOdmtrReadng
				lbEnable = int2bool(caRegClassData.getOdmtrReqd());
				if (!lbEnable)
				{
					// defect 11047
					gettxtVehicleOdometerReading().setText(
							CommonConstant.STR_SPACE_EMPTY);
					// end defect 11047
					getcomboOdometerBrandChoice().removeAllItems();
					getcomboOdometerBrandChoice().setSelectedIndex(-1);
				}
				gettxtVehicleOdometerReading().setEnabled(lbEnable);
				getstcLblOdometerReading().setEnabled(lbEnable);
				getcomboOdometerBrandChoice().setEnabled(lbEnable);
				getstcLblOdometerBrand().setEnabled(lbEnable);
				
				if (lbEnable)
				{
					// This may reset enabling 
					initComboBox(
							getcomboOdometerBrandChoice(),
							laVObj.getOdBrn());	
				}
			
				// *** Set PrmtToMove
				 // Permit To Move  (Park Trailer) 
				 // defect 11049 
				lbEnable = int2bool(caRegClassData.getPrmtToMoveReqd());
				
				 getchkParkModel().setEnabled(lbEnable);
				 if (!lbEnable)
				 {
					 getchkParkModel().setSelected(false);
				 }

				 handleTravelTrailerCheckbox();
				 // end defect 11049  
				 
				// Trailer Type
				lbEnable = int2bool(caRegClassData.getTrlrTypeReqd());
				if (!lbEnable)
				{
					getcomboTrailerTypeChoice().removeAllItems();
				}
				else
				{
					initComboBox(
						getcomboTrailerTypeChoice(),
						laVObj.getTrlTyp());
				}
				getcomboTrailerTypeChoice().setEnabled(lbEnable);
				getstcLblTrailerType().setEnabled(lbEnable);
				getstcLblTrailerType2().setEnabled(lbEnable);

			}
			// Disable fields when Veh Class/Reg Class not found in
			// RegistrationClassData
			// This will not happen 
			else
			{
				gettxtVehicleBodyVIN().setEnabled(false);
				getstcLblBodyVIN().setEnabled(false);
				gettxtVehicleCarryingCapacity().setEnabled(false);
				getstcLblCarryingCapacity().setEnabled(false);
				gettxtVehicleEmptyWeight().setEnabled(false);
				getstcLblEmptyWeight().setEnabled(false);
				getstcLblGrossWeight().setEnabled(false);
				getlblVehicleGrossWeight().setEnabled(false);
				getchkFixedWeight().setEnabled(false);
				gettxtTrlrLngth().setEnabled(false);
				getstcLblTrlLength().setEnabled(false);
				gettxtTlrWidthFeet().setEnabled(false);
				gettxtTlrWidthInches().setEnabled(false);
				getstcLblTravelTlrWidth().setEnabled(false);
				getstcLblFeet().setEnabled(false);
				getstcLblInches().setEnabled(false);
				gettxtVehicleOdometerReading().setEnabled(false);
				getstcLblOdometerReading().setEnabled(false);
				getcomboOdometerBrandChoice().setEnabled(false);
				getstcLblOdometerBrand().setEnabled(false);
				getchkParkModel().setEnabled(false);
				gettxtVehicleTonnage().setEnabled(false);
				getstcLblTonnage().setEnabled(false);
				getcomboTrailerTypeChoice().setEnabled(false);
				getstcLblTrailerType().setEnabled(false);
				getstcLblTrailerType2().setEnabled(false);
			}

			if (UtilityMethods.isDTA(csTransCd))
			{
				getradioValid().setSelected(true);
				getradioValid().setEnabled(false);
				getradioInvalid().setEnabled(false);
				getradioCitation().setEnabled(false);
			}
			else
			{
				if (!laVObj.isRegExpired())
				{
					getradioValid().setSelected(true);
					getradioValid().setEnabled(false);
					getradioInvalid().setEnabled(false);
					getradioCitation().setEnabled(false);
				}
				else
				{
					if (caTtlData != null
						&& caTtlData.getTtlTypeIndi()
							!= TitleTypes.INT_CORRECTED)
					{
						// defect 8900, 8390, 8404
						int liRegClassCd = caRegData.getRegClassCd();

						// SeasonalAg, Standard Exempt, Orig RegInvalid  
						if (cbOrigRegInvalid
							|| CommonFeesCache.isSeasonalAg(liRegClassCd)
							|| CommonFeesCache.isStandardExempt(
								liRegClassCd))
						{
							cbSeasonalAg = true;
							getradioValid().setSelected(true);
							getradioValid().setEnabled(false);
							getradioInvalid().setEnabled(false);
							getradioCitation().setEnabled(false);
						}
						else if (cbSeasonalAg == true)
						{
							cbSeasonalAg = false;
							getradioInvalid().setSelected(true);
							getradioValid().setEnabled(true);
							getradioInvalid().setEnabled(true);
							getradioCitation().setEnabled(true);
						}
						// end defect 8900, 8390, 8404
					}
				}
			}
		}
	}

	/**
	 * Display error message.
	 * 
	 * @param aiErr int
	 */
	private void displayError(int aiErr)
	{
		RTSException laMsg = new RTSException(aiErr);
		laMsg.displayError(this);
	}

	/**
	 * Displays the vehicle stolen message.  Allows user to answer 'yes'
	 * to continue processing or 'no' to stop processing the title 
	 * transaction.
	 * 
	 * @returns boolean 
	 */
	private boolean displayTTL009()
	{
		boolean lbRet = false;
		
		//If the stolen indi is set display TTL009 message
		//Currently showing as RTSException.
		if (caVehInqData != null)
		{
			VehicleData laVehDataForStl =
				caVehInqData.getMfVehicleData().getVehicleData();
			TitleData laTtlData =
				caVehInqData.getMfVehicleData().getTitleData();
			RegistrationData laRegData =
				caVehInqData.getMfVehicleData().getRegData();
			if (laVehDataForStl != null)
			{
				if (laVehDataForStl.getDpsStlnIndi() == 1)
				{
					String lsMsg = VEH_STOLEN_MSG;
					String lsTtl = STOLEN_WAIVED_MSG;
					RTSException laEx =
						new RTSException(
							RTSException.CTL001,
							lsMsg,
							lsTtl,
							true);
					
					int liRet = laEx.displayError(this);
					laEx = null;
					if (liRet == RTSException.YES)
					{
						if (laTtlData != null && laRegData != null)
						{
							laTtlData.setTtlExmnIndi(1);
							laRegData.setRegWaivedIndi(1);
						}
					}
					else
					{
						getController().processData(
							AbstractViewController.FINAL,
							caVehInqData);
						lbRet = true;
					}
				}
			}
		}
		return lbRet;
	}

	/**
	 * Determines if registration must be changed. Displays error and 
	 * sets
	 * <code>MustChangePltIndi</code> if registration must be changed.
	 *
	 */
	private void doCheckIfRegisMustChange()
	{
		RegistrationData laRegData = caMFVehicleData.getRegData();
		TitleData laTtlData = caMFVehicleData.getTitleData();

		// defect 9085 / 9198
		// Must change plate if PltOwnrshpCd != "V", i.e. goes with 
		// vehicle
		boolean lbMustChgPlt = false;
		if (laRegData != null)
		{
			String lsRegPltCd = laRegData.getRegPltCd();

			if (lsRegPltCd != null && lsRegPltCd.length() > 0)
			{
				ItemCodesData laItmCdData =
					ItemCodesCache.getItmCd(lsRegPltCd);
				if (laItmCdData != null)
				{
					int liInvProcsngCd = laItmCdData.getInvProcsngCd();
					lbMustChgPlt =
						liInvProcsngCd == 2 || liInvProcsngCd == 3;
				}
				lbMustChgPlt =
					lbMustChgPlt
						|| PlateTypeCache.isSpclPlate(lsRegPltCd)
						|| PlateTypeCache.isOutOfScopePlate(lsRegPltCd);
			}
		}
		// end defect 9198 

		// defect 8033
		// Rollback changes from 6818 
		//   Not Reject Correction
		//   Not Corrected Title
		//   Plate Code exists
		//	 InvProcsCd != 1  || Plate Code = Tow Truck 
		//   Haven't already changed Registration
		// Then Display Error Msg 84 
		//   
		if ((!getController()
			.getTransCode()
			.equalsIgnoreCase(TransCdConstant.REJCOR))
			&& (laTtlData.getTtlTypeIndi() != TitleTypes.INT_CORRECTED)
			&& (laRegData.getRegPltCd().length()
				> INT_ZERO) // & ((((TitleValidObj) caVehInqData.getValidationObject())
		//				.getInvProcsngCd()
		//				!= 1)
			&& (lbMustChgPlt
				|| (laRegData.getRegPltCd().equalsIgnoreCase(TOWTRUCK)))
			&& (!((TitleValidObj) caVehInqData.getValidationObject())
				.isChangeRegis()))
		{
			caVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setMustChangePltIndi(
				1);
			RTSException leRTSEx = new RTSException(84);
			RTSDialogBox laRDB =
				getController().getMediator().getParent();
			if (laRDB != null)
			{
				leRTSEx.displayError(laRDB);
			}
			else
			{
				leRTSEx.displayError(this);
			}
		}
		// end defect 8033
		// end defect 9085

	}

	/**
	 * Determines if the vehicle class/registration class/plate type/ 
	 * sticker type combination is valid.
	 * Returns <code> true</code> if <code>vehclasscd</code> and 
	 * <code>regclasscd</code>
	 * combination or <code>regclasscd</code>, <code>regpltcd</code> 
	 * and <code>regstkrcd</code>
	 * combination are not valid.
	 *
	 * @return boolean 
	 */
	private boolean doClassPltStkrCheck()
	{
		// defect 11126
		// Throw RTSException 208 for any error detected below
		boolean lbMustChangeReg = false; 

		int liEffDt = RTSDate.getCurrentDate().getYYYYMMDDDate();
		int liRegClassCd = caRegData.getRegClassCd();

		String lsVehClassCd = caVehData.getVehClassCd();
		String lsRegPltCd = caRegData.getRegPltCd();
		String lsRegStkrCd =caRegData.getRegStkrCd();
		int liDocTypeCd = caTtlData.getDocTypeCd();

		if ((UtilityMethods.isEmpty(lsVehClassCd)
				|| liRegClassCd == INT_ZERO)
				&& liDocTypeCd != INSURNOREG)
		{
			lbMustChangeReg = true;
		}
		else
		{
			boolean lbOffHwyUse = caRegData.isOffHwyUse();
			boolean lbATV_ROV = caRegData.isATV_ROV();

			caRegClassData = (RegistrationClassData) RegistrationClassCache
			.getRegisClass(
					lsVehClassCd,
					liRegClassCd,
					liEffDt);

			if (caRegClassData == null && liDocTypeCd != INSURNOREG)
			{
				lbMustChangeReg = true;
			}
			else if (lbOffHwyUse || lbATV_ROV)
			{
				resetRegForOffHwy_ATV_ROV();

				if (lbATV_ROV && !lbOffHwyUse)
				{
					lbMustChangeReg = true;
				}
			}
			else
			{
				// defect 10623
				// Token Trailer no longer has sticker, skip this check.
				// if (liRegCd != RegistrationConstant.REGCLASSCD_TOKEN_TRLR)
				// {
				Vector lvPltToStkrData = 
					RegistrationPlateStickerCache.getPltStkrs(
							liRegClassCd,
							lsRegPltCd,
							liEffDt,
							lsRegStkrCd);

				if (lvPltToStkrData == null
						&& liDocTypeCd != INSURNOREG 
						&& !lbOffHwyUse)
				{
					lbMustChangeReg = true;
				}
			}
		}
		if (lbMustChangeReg)
		{
			new RTSException(ErrorsConstant.ERR_NUM_MUST_CHANGE_REGIS).
			displayError(this);	
		}
		return lbMustChangeReg;
		// end defect 11126 
	}

	/**
	 * Clears registration data if vehicle record has invalid 
	 * registration.
	 *
	 * @param aaVehInq VehicleInquiryData
	 * @param aiTitleType int 
	 */
	private void doInvalidRegis()
	{
		// defect 9041 
		// Do not treat Standard Exempt as Reg Invalid if REJCOR
		// setMustChangePltIndi(1) as indicator for issue inventory
		// (Will be disregarded if no plate specified)   
		// defect 8900
		// For Standard Exempts, make it act like Reg Invalid if 
		// !Corrected Title 
		// defect 9093
		// Make ALL exempt indis for title 'reg invalid',
		// not just Standard Exempt (see: defect 8900)			
		//if (aaVehInq.getMfVehicleData().getRegData().getRegInvldIndi()
		//	== 1)
		if ((caRegData.getRegInvldIndi()== 1) //|| (CommonFeesCache
		//	.isStandardExempt(
		//		aaVehInq
		//			.getMfVehicleData()
		//			.getRegData()
		//			.getRegClassCd())
			|| (caRegData.getExmptIndi()
				== 1
				// defect 11187
				// Move defect 10047 to correct place
				//// defect 10047 
				//// If originally DocType 11 (Insurance No Regis, set to RegInvalid
				//|| caTtlData.getDocTypeCd() == DocTypeConstant.INSURANCE_NO_REGIS_TITLE
				//	// end defect 10047
					&& caTtlData.getTtlTypeIndi()!= TitleTypes.INT_CORRECTED
					&& !getController().getTransCode().equals(
						TransCdConstant.REJCOR))
						// defect 10047 
						// If originally DocType 11 (Insurance No Regis, set to RegInvalid
						|| caTtlData.getDocTypeCd() == DocTypeConstant.INSURANCE_NO_REGIS_TITLE)
							// end defect 10047
				// end defect 11187					
		{
			caRegData.setRegInvldIndi(INT_ZERO);
			caRegData.setRegClassCd(INT_ZERO);
			caRegData.setRegEffDt(INT_ZERO);
			caRegData.setRegExpMo(INT_ZERO);
			caRegData.setRegExpYr(INT_ZERO);
			caRegData.setRegPltAge(Integer.MAX_VALUE);
			caRegData.setRegPltCd(CommonConstant.STR_SPACE_EMPTY);
			caRegData.setRegPltNo(CommonConstant.STR_SPACE_EMPTY);
			caRegData.setRegStkrCd(CommonConstant.STR_SPACE_EMPTY);
			caRegData.setRegStkrNo(CommonConstant.STR_SPACE_EMPTY);
			caTtlData.setMustChangePltIndi(1);
			cbOrigRegInvalid = true;
		}
		
	}

	/**
	 * Displays no lien warning message if no liens exist on vehicle 
	 * record.
	 */
	private void doNoLienCheck()
	{
		if (!caVehInqData.getMfVehicleData().getTitleData().hasLien())
		{
			int liErrMsgNo =
				caVehInqData.getMfDown() == 0
					? ErrorsConstant.ERR_NUM_NO_LIEN_ON_VEHICLE
					: ErrorsConstant.ERR_NUM_VERIFY_LIENS_RELEASED;
			RTSException laWarning = new RTSException(liErrMsgNo);
			laWarning.displayError(this);
		}
	}

	/**
	 * Determines whether the expired registration radio buttons are 
	 * disabled.
	 * Determines whether to set the <code>UnregisterVehIndi
	 * </code> flag.
	 * Determines whether the change registration push button is 
	 * disabled.
	 *
	 */
	private void doTitleTypeNotCorrect()
	{
		// defect 6198
		// Add method to get the original RegisData
		RegistrationData laOrigRegData = getOrigRegisData();
		int liOrgRegClassCd = laOrigRegData.getRegClassCd();
		// end defect 6198

		if (caTtlData.getTtlTypeIndi()!= TitleTypes.INT_CORRECTED)
		{
			if (((TitleValidObj) caVehInqData.getValidationObject())
				.isRegExpired())
			{
				enableExpiredRegisBtns(false);
			}
			// defect 6198
			// Evaluate the original RegClassd
			// Previous code evaluated the current RegClassCd

			// defect 9085 
			// Remove reference to InvProcsngCd
			boolean lbUnregistered = false;
			String lsRegPltCd = laOrigRegData.getRegPltCd();
			if (lsRegPltCd != null && lsRegPltCd.length() > 0)
			{
				lbUnregistered =
					PlateTypeCache.isOutOfScopePlate(lsRegPltCd);
			}

			//	if (((TitleValidObj) laVehInq.getValidationObject())
			//		.getInvProcsngCd()
			//		== 3
			if (lbUnregistered
				|| liOrgRegClassCd == 4
				|| liOrgRegClassCd == 5)
			{
				//set unregister vehicle flag
				caRegData.setUnregisterVehIndi(1);
			}
			// end defect 6198
			// end defect 9085 
		}
		// Title Type is Corrected
		// Disable expired registration radio buttons and
		else
		{
			enableExpiredRegisBtns(false);

			// defect 10369
			// change registration push button if doClassPltStkrCheck 
			//	passed 
			//if (!isChangeRegCls()) // set in doClassPltStkrCheck
			//{
			getbtnChangeRegistration().setEnabled(false);
			//}
			// end defect 10369 
		}
	}

	/**
	 * Enables/Disables the expired registration radio buttons.
	 *
	 * @param abEnable boolean
	 */
	private void enableExpiredRegisBtns(boolean abEnable)
	{
		getradioCitation().setEnabled(abEnable);
		getradioInvalid().setEnabled(abEnable);
		getradioValid().setEnabled(abEnable);
	}

	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE FocuseEvent
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		// Not used
	}

	/**
	 * Invoked when Odometer Reading, Empty Weight, Carrying Capacity or 
	 * VIN field loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		// defect 7898
		// Removed the isTemporary check b/c when we left this frame by
		// using the enter key focus lost would be called but the
		// isTemporary value would be true since you did not move to 
		// another field on this frame.  The only problem with removing
		// isTemporary is that if you move focus away from the 
		// application this code is excecuted.
		//if (!aaFE.isTemporary() 
		//	&& aaFE.getSource() == gettxtVehicleOdometerReading())
		if (aaFE.getSource() == gettxtVehicleOdometerReading())
		{
			// If KeyPressed has already handles the setVehOdomtrBrand
			// then don't do it here.
			if (!cbTabKeyPressed)
			{
				setVehOdomtrBrand();
			}
			else
			{
				cbTabKeyPressed = false;
			}
		}
		// end defect 7898
		else if (
			!aaFE.isTemporary()
				&& (aaFE.getSource() == gettxtVehicleEmptyWeight() || 
						(aaFE.getSource() == gettxtVehicleCarryingCapacity())))
		{
			recalcGrossWt(); 
		}
		else if (
			!aaFE.isTemporary() && (aaFE.getSource() == gettxtVIN() 
					|| aaFE.getSource() == gettxtVehicleBodyVIN()))
		{
			convertVIN((RTSInputField) aaFE.getSource());
		}
	}

	/**
	 * Return the btnChangeRegistration property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnChangeRegistration()
	{
		if (ivjbtnChangeRegistration == null)
		{
			try
			{
				ivjbtnChangeRegistration = new RTSButton();
				ivjbtnChangeRegistration.setName(
					"ivjbtnChangeRegistration");
				//ivjbtnChangeRegistration.setMnemonic('r');
				ivjbtnChangeRegistration.setMnemonic(KeyEvent.VK_R);
				ivjbtnChangeRegistration.setText(CHANGE_REGISTRATION);
				ivjbtnChangeRegistration.setBounds(155, 371, 149, 25);
				// user code begin {1}
				ivjbtnChangeRegistration.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnChangeRegistration;
	}

	/**
	 * Return the btnRecNotApplicable property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnRecNotApplicable()
	{
		if (ivjbtnRecNotApplicable == null)
		{
			try
			{
				ivjbtnRecNotApplicable = new RTSButton();
				ivjbtnRecNotApplicable.setSize(159, 25);
				ivjbtnRecNotApplicable.setName("btnRecNotApplicable");
				//ivjbtnRecNotApplicable.setMnemonic('n');
				ivjbtnRecNotApplicable.setMnemonic(KeyEvent.VK_N);
				ivjbtnRecNotApplicable.setText(RECORD_NOT_APPLICABLE);
				ivjbtnRecNotApplicable.setVerticalAlignment(
					SwingConstants.CENTER);
				ivjbtnRecNotApplicable.setVerticalTextPosition(
					SwingConstants.CENTER);
				// user code begin {1}
				ivjbtnRecNotApplicable.setLocation(7, 482);
				ivjbtnRecNotApplicable.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnRecNotApplicable;
	}

	/**
	 * Return the btnSPV property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnSPV()
	{
		if (btnSPV == null)
		{
			btnSPV = new RTSButton();
			btnSPV.setBounds(203, 2, 99, 25);
			btnSPV.setText(SPV);
			btnSPV.setMnemonic(KeyEvent.VK_S);
			btnSPV.addActionListener(this);
		}
		return btnSPV;
	}

	/**
	 * Return the ButtonPanel1 property value.
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
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(329, 472, 301, 50);
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				getButtonPanel1().getBtnCancel().addActionListener(
					this);
				getButtonPanel1().getBtnEnter().addActionListener(this);
				getButtonPanel1().getBtnHelp().addActionListener(this);
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
	 * Return the chkFixedWeight property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkFixedWeight()
	{
		if (ivjchkFixedWeight == null)
		{
			try
			{
				ivjchkFixedWeight = new JCheckBox();
				ivjchkFixedWeight.setName("chkFixedWeight");
				//ivjchkFixedWeight.setMnemonic('F');
				ivjchkFixedWeight.setMnemonic(KeyEvent.VK_F);
				ivjchkFixedWeight.setLocation(new Point(3, 364));
				ivjchkFixedWeight.setSize(new Dimension(123, 20));
				ivjchkFixedWeight.setText(FIXED_WEIGHT);
				// defect 11126 
				ivjchkFixedWeight.addItemListener(this);
				// end defect 11126 
				
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkFixedWeight;
	}

	/**
	 * Return the chkParkModel property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkParkModel()
	{
		if (ivjchkParkModel == null)
		{
			try
			{
				ivjchkParkModel = new JCheckBox();
				ivjchkParkModel.setName("chkParkModel");
				//ivjchkParkModel.setMnemonic('p');
				ivjchkParkModel.setMnemonic(KeyEvent.VK_P);
				ivjchkParkModel.setLocation(new Point(3, 404));
				ivjchkParkModel.setSize(new Dimension(136, 18));
				ivjchkParkModel.setText(PARK_MODEL_TRAILER);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkParkModel;
	}
	
	/**
	 * This method initializes ivjchkTravelTrailer	
	 * 	
	 * @return JCheckBox	
	 */
	private JCheckBox getchkTravelTrailer()
	{
		if (ivjchkTravelTrailer == null)
		{
			ivjchkTravelTrailer = new JCheckBox();
			ivjchkTravelTrailer.setLocation(new Point(3, 385));
			ivjchkTravelTrailer.setText(TRAVEL_TRAILER);
			ivjchkTravelTrailer.setSize(new Dimension(127, 18));
			ivjchkTravelTrailer.addItemListener(this);
			ivjchkTravelTrailer.setEnabled(false);
		}
		return ivjchkTravelTrailer;
	}
	/**
	 * Return the comboMajorColor property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboMajorColor()
	{
		if (ivjcomboMajorColor == null)
		{
			try
			{
				ivjcomboMajorColor =
					new JComboBox();
				ivjcomboMajorColor.setBounds(127, 87, 178, 20);
				ivjcomboMajorColor.setName(
					"comboMajorColor");
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
	 * @return JComboBox
	 */
	private JComboBox getcomboMinorColor()
	{
		if (ivjcomboMinorColor == null)
		{
			try
			{
				ivjcomboMinorColor =
					new JComboBox();
				ivjcomboMinorColor.setBounds(127, 110, 178, 20);
				ivjcomboMinorColor.setName(
					"comboMinorColor");
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
	 * Return the comboOdometerBrandChoice property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboOdometerBrandChoice()
	{
		if (ivjcomboOdometerBrandChoice == null)
		{
			try
			{
				ivjcomboOdometerBrandChoice =
					new JComboBox();
				ivjcomboOdometerBrandChoice.setBounds(124, 28, 178, 20);
				ivjcomboOdometerBrandChoice.setName(
					"comboOdometerBrandChoice");
				ivjcomboOdometerBrandChoice.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboOdometerBrandChoice.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboOdometerBrandChoice.setBackground(
					java.awt.Color.white);
				// user code begin (1)
				// defect 7895
				//	Add FocusListener for focusLost()
				ivjcomboOdometerBrandChoice.addFocusListener(this);
				// end defect 7895
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboOdometerBrandChoice;
	}

	/**
	 * Return the comboOdometerBrandChoice1 property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboTrailerTypeChoice()
	{
		if (ivjcomboTrailerTypeChoice == null)
		{
			try
			{
				ivjcomboTrailerTypeChoice = new JComboBox();
				ivjcomboTrailerTypeChoice.setName(
					"comboTrailerTypeChoice");
				ivjcomboTrailerTypeChoice.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboTrailerTypeChoice.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboTrailerTypeChoice.setBackground(
					java.awt.Color.white);
				ivjcomboTrailerTypeChoice.setBounds(222, 314, 84, 23);
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
		return ivjcomboTrailerTypeChoice;
	}
	/**
	 * 
	 * @param asInput
	 * @return
	 */
	private String getEmptyStringIfZero(String asInput)
	{
		String lsOutput = new String();
		int liInt = 0; 
		try
		{
			liInt = Integer.parseInt(asInput); 
		}
		catch (NumberFormatException aeNFEx)
		{
			
		}
		lsOutput = liInt == 0 ? new String() : asInput;  
		
		return lsOutput; 
	}
	/**
	 * Return the FrmTitleRecordTTL003ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmTitleRecordTTL003ContentPane1()
	{
		if (ivjFrmTitleRecordTTL003ContentPane1 == null)
		{
			try
			{
				ivjFrmTitleRecordTTL003ContentPane1 =
					new JPanel();
				ivjFrmTitleRecordTTL003ContentPane1.setName(
					"FrmTitleRecordTTL003ContentPane1");
				ivjFrmTitleRecordTTL003ContentPane1.setLayout(null);
				ivjFrmTitleRecordTTL003ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				ivjFrmTitleRecordTTL003ContentPane1.setMinimumSize(
					new java.awt.Dimension(0, 0));
				getFrmTitleRecordTTL003ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());
				getFrmTitleRecordTTL003ContentPane1().add(
					getJPanel3(),
					getJPanel3().getName());
				getFrmTitleRecordTTL003ContentPane1().add(
					getJPanel4(),
					getJPanel4().getName());
				getFrmTitleRecordTTL003ContentPane1().add(
					getJPanel5(),
					getJPanel5().getName());
				getFrmTitleRecordTTL003ContentPane1().add(
					getJPanel6(),
					getJPanel6().getName());
				getFrmTitleRecordTTL003ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmTitleRecordTTL003ContentPane1().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
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
		return ivjFrmTitleRecordTTL003ContentPane1;
	}

	/**
	 * Get indicators and build string to display indicators in text 
	 * area.
	 * 
	 */
	private void getIndicators()
	{
		if (caVehInqData != null)
		{
			TitleValidObj laTtlValidObj =
				(TitleValidObj) caVehInqData.getValidationObject();
			if (laTtlValidObj != null)
			{
				Vector lvIndis =
					IndicatorLookup.getIndicators(
						caMFVehicleData,
						csTransCd,
						IndicatorLookup.SCREEN);
				cvIndicator = lvIndis;
				StringBuffer lsIndis =
					new StringBuffer(CommonConstant.STR_SPACE_EMPTY);
				int liNumIndis = lvIndis.size();
				if (liNumIndis > 0)
				{
					Vector lvRows = new java.util.Vector();
					for (int liIndex = 0;
						liIndex < liNumIndis;
						liIndex++)
					{
						IndicatorData laData =
							(IndicatorData) lvIndis.get(liIndex);

						// defect 10507 
						//	String lsHStop = laData.getStopCode();
						//	if (lsHStop != null && lsHStop.equals("H"))
						//	{
						//		//int liJ = 0;
						//	}
						// end defect 10507

						lsIndis.append(
							laData.getStopCode() == null
								? CommonConstant.STR_SPACE_ONE
								: laData.getStopCode());
						lsIndis.append(CommonConstant.STR_SPACE_TWO);
						lsIndis.append(laData.getDesc());
						lvRows.add(lsIndis.toString());
						lsIndis =
							new StringBuffer(
								CommonConstant.STR_SPACE_EMPTY);
					}
					getlstIndiDescription().setListData(lvRows);
					getlstIndiDescription().setSelectedIndex(0);
				}
			}
		}
	}

	/**
	 * This method initializes ivjlblSlash
	 * 
	 * @return JLabel
	 */
	private JLabel getivjlblSlash()
	{
		if (ivjlblSlash == null)
		{
			ivjlblSlash = new JLabel();
			ivjlblSlash.setSize(10, 14);
			ivjlblSlash.setText(CommonConstant.STR_SLASH);
			ivjlblSlash.setLocation(108, 34);

		}
		return ivjlblSlash;
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
			jPanel.add(getstcLblOdometerReading(), null);
			jPanel.add(gettxtVehicleOdometerReading(), null);
			jPanel.add(getstcLblOdometerBrand(), null);
			jPanel.add(getcomboOdometerBrandChoice(), null);
			jPanel.add(getbtnSPV(), null);
			jPanel.setSize(309, 53);
			jPanel.setLocation(5, 206);
		}
		return jPanel;
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
				// defect 11368 
				ivjstcLblReplicaYearMake = new JLabel();
				ivjstcLblReplicaYearMake.setText(REPLICA_YEAR_MAKE);
				ivjstcLblReplicaYearMake.setSize(new Dimension(108, 20));
				ivjstcLblReplicaYearMake.setLocation(new Point(7, 451));
				ivjstcLblReplicaYearMake.setLabelFor(gettxtReplicaVehicleModelYear()); 
				ivjstcLblReplicaYearMake.setDisplayedMnemonic(KeyEvent.VK_Y); 
				// end defect 11368 
				
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel1.setBounds(9, 3, 316, 525);
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(291, 297));
				getJPanel1().add(
					getstcLblNewTitleType(),
					getstcLblNewTitleType().getName());
				getJPanel1().add(
					getlblNewTitleType(),
					getlblNewTitleType().getName());
				getJPanel1().add(
					getstcLblYearMake(),
					getstcLblYearMake().getName());
				getJPanel1().add(gettxtYear(), gettxtYear().getName());
				getJPanel1().add(gettxtMake(), gettxtMake().getName());
				getJPanel1().add(
					gettxtBodyStyle(),
					gettxtBodyStyle().getName());
				getJPanel1().add(
					getstcLblBodyType(),
					getstcLblBodyType().getName());
				getJPanel1().add(
					getstcLblVehicleModel(),
					getstcLblVehicleModel().getName());
				getJPanel1().add(
					gettxtModel(),
					gettxtModel().getName());
				getJPanel1().add(
					getstcLblVIN(),
					getstcLblVIN().getName());
				getJPanel1().add(gettxtVIN(), gettxtVIN().getName());
				getJPanel1().add(
					getstcLblBodyVIN(),
					getstcLblBodyVIN().getName());
				getJPanel1().add(
					gettxtVehicleBodyVIN(),
					gettxtVehicleBodyVIN().getName());
				getJPanel1().add(
					getstcLblVehicleClass(),
					getstcLblVehicleClass().getName());
				getJPanel1().add(
					getlblVehicleClass(),
					getlblVehicleClass().getName());
				getJPanel1().add(
					getstcLblEmptyWeight(),
					getstcLblEmptyWeight().getName());
				getJPanel1().add(
					gettxtVehicleEmptyWeight(),
					gettxtVehicleEmptyWeight().getName());
				getJPanel1().add(
					getstcLblCarryingCapacity(),
					getstcLblCarryingCapacity().getName());
				getJPanel1().add(
					gettxtVehicleCarryingCapacity(),
					gettxtVehicleCarryingCapacity().getName());
				getJPanel1().add(
					getstcLblTrailerType(),
					getstcLblTrailerType().getName());
				getJPanel1().add(
					getstcLblTrailerType2(),
					getstcLblTrailerType2().getName());
				getJPanel1().add(
					getcomboTrailerTypeChoice(),
					getcomboTrailerTypeChoice().getName());
				getJPanel1().add(
					getstcLblGrossWeight(),
					getstcLblGrossWeight().getName());
				getJPanel1().add(
					getlblVehicleGrossWeight(),
					getlblVehicleGrossWeight().getName());
				getJPanel1().add(
					getstcLblTonnage(),
					getstcLblTonnage().getName());
				getJPanel1().add(
					gettxtVehicleTonnage(),
					gettxtVehicleTonnage().getName());
				getJPanel1().add(
					getbtnChangeRegistration(),
					getbtnChangeRegistration().getName());
				getJPanel1().add(
					getchkFixedWeight(),
					getchkFixedWeight().getName());
				getJPanel1().add(
					getchkParkModel(),
					getchkParkModel().getName());
				getJPanel1().add(
					getstcLblTrlLength(),
					getstcLblTrlLength().getName());
				getJPanel1().add(
					gettxtTrlrLngth(),
					gettxtTrlrLngth().getName());
				getJPanel1().add(
					gettxtTlrWidthFeet(),
					gettxtTlrWidthFeet().getName());
				getJPanel1().add(
					getstcLblTravelTlrWidth(),
					getstcLblTravelTlrWidth().getName());
				getJPanel1().add(
					getstcLblFeet(),
					getstcLblFeet().getName());
				getJPanel1().add(
					getstcLblInches(),
					getstcLblInches().getName());
				getJPanel1().add(
					gettxtTlrWidthInches(),
					gettxtTlrWidthInches().getName());
				getJPanel1().add(
					getbtnRecNotApplicable(),
					getbtnRecNotApplicable().getName());
				// user code begin {1}
				ivjJPanel1.add(getJPanel(), null);
				// user code end
				ivjJPanel1.add(getcomboMajorColor(), null);
				ivjJPanel1.add(getstcLblColorMajor(), null);
				ivjJPanel1.add(getstcLblColorMinor(), null);
				ivjJPanel1.add(getcomboMinorColor(), null);
				
				// defect 11049 
				ivjJPanel1.add(getchkTravelTrailer(), null);
				RTSButtonGroup laBtnGrp = new RTSButtonGroup(); 
				laBtnGrp.add(getchkFixedWeight()); 
				laBtnGrp.add(getchkTravelTrailer());
				laBtnGrp.add(getchkParkModel());
				// end defect 11049
				// defect 11368 
				ivjJPanel1.add(ivjstcLblReplicaYearMake, null);
				ivjJPanel1.add(gettxtReplicaVehicleModelYear(), null);
				ivjJPanel1.add(gettxtReplicaVehicleMake(), null);
				// end defect 11368
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
				ivjJPanel3.setLayout(null);
				ivjJPanel3.setBounds(327, 7, 301, 54);
				getJPanel3().add(
					getstcLblOwner(),
					getstcLblOwner().getName());
				getJPanel3().add(
					getlblOwnerId(),
					getlblOwnerId().getName());
				getJPanel3().add(
					getlblOwnerName(),
					getlblOwnerName().getName());
				getJPanel3().add(
					getlblOwnerName2(),
					getlblOwnerName2().getName());
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
					new TitledBorder(new EtchedBorder(), REGISTRATION));
				ivjJPanel4.setLayout(null);
				ivjJPanel4.setBounds(327, 67, 301, 130);
				getJPanel4().add(
					getstcLblPlate(),
					getstcLblPlate().getName());
				getJPanel4().add(
					getstcLblExpires(),
					getstcLblExpires().getName());
				getJPanel4().add(
					getstcLblClass(),
					getstcLblClass().getName());
				getJPanel4().add(
					getstcLblType(),
					getstcLblType().getName());
				getJPanel4().add(
					getstcLblCounty(),
					getstcLblCounty().getName());
				getJPanel4().add(
					getstcLblAge(),
					getstcLblAge().getName());
				getJPanel4().add(
					getlblPlateNo(),
					getlblPlateNo().getName());
				getJPanel4().add(
					getlblRegExpMo(),
					getlblRegExpMo().getName());
				getJPanel4().add(
					getlblRegClass(),
					getlblRegClass().getName());
				getJPanel4().add(
					getlblPlateType(),
					getlblPlateType().getName());
				getJPanel4().add(
					getlblCntyName(),
					getlblCntyName().getName());
				getJPanel4().add(
					getlblPlateAge(),
					getlblPlateAge().getName());
				getJPanel4().add(
					getlblRegExpYr(),
					getlblRegExpYr().getName());
				getJPanel4().add(
					getlblResComptCntyNo(),
					getlblResComptCntyNo().getName());
				getJPanel4().add(getivjlblSlash(), null);
				getJPanel4().add(getstcLblOrg(), null);
				getJPanel4().add(getlblOrg(), null);
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
				ivjJPanel5.setLayout(null);
				getJPanel5().add(
					getstcLblDocumentNo(),
					getstcLblDocumentNo().getName());
				getJPanel5().add(
					getstcLblIssued(),
					getstcLblIssued().getName());
				getJPanel5().add(
					getlblDocumentNo(),
					getlblDocumentNo().getName());
				getJPanel5().add(
					getlblIssuedDate(),
					getlblIssuedDate().getName());
				getJPanel5().add(
					getlblDocumentType(),
					getlblDocumentType().getName());
				// user code begin {1}
				ivjJPanel5.setSize(301, 75);
				ivjJPanel5.setLocation(327, 207);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
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
				ivjJPanel6.setName("JPanel6");
				ivjJPanel6.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						EXPIRED_BORDER));
				ivjJPanel6.setLayout(null);
				ivjJPanel6.setBounds(328, 371, 301, 73);
				getJPanel6().add(
					getradioValid(),
					getradioValid().getName());
				getJPanel6().add(
					getradioInvalid(),
					getradioInvalid().getName());
				getJPanel6().add(
					getradioCitation(),
					getradioCitation().getName());
				// user code begin {1}
				RTSButtonGroup laBG = new RTSButtonGroup();
				laBG.add(getradioValid());
				laBG.add(getradioInvalid());
				laBG.add(getradioCitation());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
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
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				getJScrollPane1().setViewportView(
					getlstIndiDescription());
				// user code begin {1}
				ivjJScrollPane1.setOpaque(true);
				ivjJScrollPane1.setSize(298, 79);
				ivjJScrollPane1.setLocation(327, 286);
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
	 * Return the lblCntyName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCntyName()
	{
		if (ivjlblCntyName == null)
		{
			try
			{
				ivjlblCntyName = new JLabel();
				ivjlblCntyName.setSize(165, 14);
				ivjlblCntyName.setName("lblCntyName");
				ivjlblCntyName.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblCntyName.setLocation(124, 110);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblCntyName;
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
				ivjlblDocumentNo.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblDocumentNo.setBounds(119, 15, 140, 14);
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
		return ivjlblDocumentNo;
	}

	/**
	 * Return the lblDocumentType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDocumentType()
	{
		if (ivjlblDocumentType == null)
		{
			try
			{
				ivjlblDocumentType = new JLabel();
				ivjlblDocumentType.setName("lblDocumentType");
				ivjlblDocumentType.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblDocumentType.setBounds(34, 54, 221, 14);
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
		return ivjlblDocumentType;
	}

	/**
	 * Return the lblIssuedDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblIssuedDate()
	{
		if (ivjlblIssuedDate == null)
		{
			try
			{
				ivjlblIssuedDate = new JLabel();
				ivjlblIssuedDate.setName("lblIssuedDate");
				ivjlblIssuedDate.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblIssuedDate.setBounds(118, 34, 67, 14);
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
		return ivjlblIssuedDate;
	}

	/**
	 * Return the lblNewTitleType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblNewTitleType()
	{
		if (ivjlblNewTitleType == null)
		{
			try
			{
				ivjlblNewTitleType = new JLabel();
				ivjlblNewTitleType.setName("lblNewTitleType");
				ivjlblNewTitleType.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblNewTitleType.setMaximumSize(
					new java.awt.Dimension(54, 14));
				ivjlblNewTitleType.setMinimumSize(
					new java.awt.Dimension(54, 14));
				ivjlblNewTitleType.setBounds(127, 3, 188, 14);
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
		return ivjlblNewTitleType;
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
			ivjlblOrg.setSize(201, 14);
			ivjlblOrg.setText("");
			ivjlblOrg.setName("lblOrg");
			ivjlblOrg.setLocation(88, 91);
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
				ivjlblOwnerId.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblOwnerId.setBounds(87, 2, 99, 14);
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
		return ivjlblOwnerId;
	}

	/**
	 * Return the lblOwnerName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblOwnerName()
	{
		if (ivjlblOwnerName == null)
		{
			try
			{
				ivjlblOwnerName = new JLabel();
				ivjlblOwnerName.setName("lblOwnerName");
				ivjlblOwnerName.setOpaque(true);
				ivjlblOwnerName.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblOwnerName.setBackground(java.awt.Color.red);
				ivjlblOwnerName.setBounds(21, 19, 235, 14);
				ivjlblOwnerName.setForeground(java.awt.Color.white);
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
		return ivjlblOwnerName;
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
				ivjlblOwnerName2.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblOwnerName2.setBounds(21, 38, 235, 14);
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
		return ivjlblOwnerName2;
	}

	/**
	 * Return the lbllblPlateAge property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPlateAge()
	{
		if (ivjlblPlateAge == null)
		{
			try
			{
				ivjlblPlateAge = new JLabel();
				ivjlblPlateAge.setName("lblPlateAge");
				ivjlblPlateAge.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblPlateAge.setBounds(209, 15, 45, 14);
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
		return ivjlblPlateAge;
	}

	/**
	 * Return the lblPlateNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPlateNo()
	{
		if (ivjlblPlateNo == null)
		{
			try
			{
				ivjlblPlateNo = new JLabel();
				ivjlblPlateNo.setName("lblPlateNo");
				ivjlblPlateNo.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblPlateNo.setBounds(88, 15, 59, 14);
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
		return ivjlblPlateNo;
	}

	/**
	 * Return the lblPlateType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPlateType()
	{
		if (ivjlblPlateType == null)
		{
			try
			{
				ivjlblPlateType = new JLabel();
				ivjlblPlateType.setSize(201, 14);
				ivjlblPlateType.setName("lblPlateType");
				ivjlblPlateType.setText(CommonConstant.STR_SPACE_EMPTY);
				// user code begin {1}
				ivjlblPlateType.setLocation(88, 72);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblPlateType;
	}

	/**
	 * Return the lblRegClass property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblRegClass()
	{
		if (ivjlblRegClass == null)
		{
			try
			{
				ivjlblRegClass = new JLabel();
				ivjlblRegClass.setSize(201, 14);
				ivjlblRegClass.setName("lblRegClass");
				ivjlblRegClass.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblRegClass.setLocation(88, 53);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblRegClass;
	}

	/**
	 * Return the lblRegExpMo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblRegExpMo()
	{
		if (ivjlblRegExpMo == null)
		{
			try
			{
				ivjlblRegExpMo = new JLabel();
				ivjlblRegExpMo.setName("lblRegExpMo");
				ivjlblRegExpMo.setAlignmentX(
					java.awt.Component.LEFT_ALIGNMENT);
				ivjlblRegExpMo.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblRegExpMo.setBounds(88, 34, 19, 14);
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
		return ivjlblRegExpMo;
	}

	/**
	 * Return the lblRegExpYr property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblRegExpYr()
	{
		if (ivjlblRegExpYr == null)
		{
			try
			{
				ivjlblRegExpYr = new JLabel();
				ivjlblRegExpYr.setSize(37, 14);
				ivjlblRegExpYr.setName("lblRegExpYr");
				ivjlblRegExpYr.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjlblRegExpYr.setLocation(118, 34);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblRegExpYr;
	}

	/**
	 * Return the lblResComptCntyNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblResComptCntyNo()
	{
		if (ivjlblResComptCntyNo == null)
		{
			try
			{
				ivjlblResComptCntyNo = new JLabel();
				ivjlblResComptCntyNo.setName("lblResComptCntyNo");
				ivjlblResComptCntyNo.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblResComptCntyNo.setBounds(88, 110, 28, 14);
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
		return ivjlblResComptCntyNo;
	}

	/**
	 * Return the lblVehicleClass property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblVehicleClass()
	{
		if (ivjlblVehicleClass == null)
		{
			try
			{
				ivjlblVehicleClass = new JLabel();
				ivjlblVehicleClass.setName("lblVehicleClass");
				ivjlblVehicleClass.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjlblVehicleClass.setMaximumSize(
					new java.awt.Dimension(32, 14));
				ivjlblVehicleClass.setMinimumSize(
					new java.awt.Dimension(32, 14));
				ivjlblVehicleClass.setBounds(126, 185, 83, 14);
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
		return ivjlblVehicleClass;
	}

	/**
	 * Return the lblVehicleGrossWeight property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblVehicleGrossWeight()
	{
		if (ivjlblVehicleGrossWeight == null)
		{
			try
			{
				ivjlblVehicleGrossWeight = new JLabel();
				ivjlblVehicleGrossWeight.setName(
					"lblVehicleGrossWeight");
				ivjlblVehicleGrossWeight.setText(
					CommonConstant.STR_ZERO);
				ivjlblVehicleGrossWeight.setMaximumSize(
					new java.awt.Dimension(28, 14));
				ivjlblVehicleGrossWeight.setMinimumSize(
					new java.awt.Dimension(28, 14));
				ivjlblVehicleGrossWeight.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjlblVehicleGrossWeight.setBounds(127, 317, 43, 17);
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
		return ivjlblVehicleGrossWeight;
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
				ivjlstIndiDescription.setBounds(0, 0, 288, 54);
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
	 * Return Odometer Brand
	 * 
	 * @param asOdometer 
	 * @return String
	 */
	private String getOdometerSelection(String asOdometer) 
	{
		String lsBrand = CommonConstant.STR_SPACE_EMPTY;
		if (!asOdometer.equals(CommonConstant.STR_SPACE_EMPTY)
				&& !asOdometer.equals(EXEMPT))
		{
			String lsChoice =
				(String) getcomboOdometerBrandChoice()
				.getSelectedItem();

			if (lsChoice != null)
			{
				if (lsChoice.equals(OdometerBrands.ACTUAL))
				{
					lsBrand = OdometerBrands.ACTUAL_CODE;
				}
				else if (
						lsChoice.equals(OdometerBrands.EXCEED))
				{
					lsBrand = OdometerBrands.EXCEED_CODE;
				}
				else if (
						lsChoice.equals(OdometerBrands.NOTACT))
				{
					lsBrand = OdometerBrands.NOTACT_CODE;
				}
			}
		}
		return lsBrand;
	}
	
	/**
	 * Get the Original Registration Data if exist.
	 * 
	 * @return RegistrationData
	 */
	public RegistrationData getOrigRegisData()
	{
		RegistrationData laRegOriginal = null;
		if (caVehInqData != null)
		{
			TitleValidObj lsTtlValidObj =
				(TitleValidObj) caVehInqData.getValidationObject();
			MFVehicleData laMFOriginal =
				(MFVehicleData) lsTtlValidObj.getMfVehOrig();
			if (laMFOriginal != null)
			{
				laRegOriginal = laMFOriginal.getRegData();
				if (laRegOriginal != null)
				{
					// Review code logic
				}
			}
		}
		return laRegOriginal;
	}

	/**
	 * Return the radioCitation property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioCitation()
	{
		if (ivjradioCitation == null)
		{
			try
			{
				ivjradioCitation = new JRadioButton();
				ivjradioCitation.setName("radioCitation");
				ivjradioCitation.setMnemonic(KeyEvent.VK_C);
				ivjradioCitation.setText(CITATION_PENTLY);
				ivjradioCitation.setBounds(58, 39, 171, 19);
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
		return ivjradioCitation;
	}

	/**
	 * Return the radioInvalid property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioInvalid()
	{
		if (ivjradioInvalid == null)
		{
			try
			{
				ivjradioInvalid = new JRadioButton();
				ivjradioInvalid.setName("radioInvalid");
				ivjradioInvalid.setMnemonic(KeyEvent.VK_I);
				ivjradioInvalid.setText(INVALID_REASON);
				ivjradioInvalid.setBounds(145, 16, 111, 19);
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
		return ivjradioInvalid;
	}

	/**
	 * Return the radioValid property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioValid()
	{
		if (ivjradioValid == null)
		{
			try
			{
				ivjradioValid = new JRadioButton();
				ivjradioValid.setName("radioValid");
				ivjradioValid.setMnemonic(KeyEvent.VK_V);
				ivjradioValid.setText(VALID_REASON);
				ivjradioValid.setBounds(25, 16, 100, 19);
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
		return ivjradioValid;
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
				ivjstcLblAge.setName("stcLblAge");
				ivjstcLblAge.setText(AGE);
				ivjstcLblAge.setBounds(155, 15, 42, 14);
				ivjstcLblAge.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblAge;
	}

	/**
	 * Return the stcLblBodyType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBodyType()
	{
		if (ivjstcLblBodyType == null)
		{
			try
			{
				ivjstcLblBodyType = new JLabel();
				ivjstcLblBodyType.setName("stcLblBodyType");
				ivjstcLblBodyType.setText(BODY_STYLE);
				ivjstcLblBodyType.setMaximumSize(
					new java.awt.Dimension(62, 14));
				ivjstcLblBodyType.setHorizontalTextPosition(4);
				ivjstcLblBodyType.setBounds(5, 44, 112, 14);
				ivjstcLblBodyType.setMinimumSize(
					new java.awt.Dimension(62, 14));
				ivjstcLblBodyType.setHorizontalAlignment(4);
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
		return ivjstcLblBodyType;
	}

	/**
	 * Return the stcLblBodyVIN property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBodyVIN()
	{
		if (ivjstcLblBodyVIN == null)
		{
			try
			{
				ivjstcLblBodyVIN = new JLabel();
				ivjstcLblBodyVIN.setName("stcLblBodyVIN");
				ivjstcLblBodyVIN.setText(BODY_VIN);
				ivjstcLblBodyVIN.setMaximumSize(
					new java.awt.Dimension(53, 14));
				ivjstcLblBodyVIN.setHorizontalTextPosition(2);
				ivjstcLblBodyVIN.setBounds(3, 162, 112, 14);
				ivjstcLblBodyVIN.setMinimumSize(
					new java.awt.Dimension(53, 14));
				ivjstcLblBodyVIN.setHorizontalAlignment(4);
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
		return ivjstcLblBodyVIN;
	}

	/**
	 * Return the stcLblCarryingCapacity property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCarryingCapacity()
	{
		if (ivjstcLblCarryingCapacity == null)
		{
			try
			{
				ivjstcLblCarryingCapacity = new JLabel();
				ivjstcLblCarryingCapacity.setName(
					"stcLblCarryingCapacity");
				ivjstcLblCarryingCapacity.setText(CARRYING_CAPACITY);
				ivjstcLblCarryingCapacity.setMaximumSize(
					new java.awt.Dimension(103, 14));
				ivjstcLblCarryingCapacity.setHorizontalTextPosition(4);
				ivjstcLblCarryingCapacity.setBounds(4, 294, 112, 14);
				ivjstcLblCarryingCapacity.setMinimumSize(
					new java.awt.Dimension(103, 14));
				ivjstcLblCarryingCapacity.setHorizontalAlignment(4);
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
		return ivjstcLblCarryingCapacity;
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
				ivjstcLblClass.setName("stcLblClass");
				ivjstcLblClass.setText(CLASS);
				ivjstcLblClass.setBounds(29, 53, 45, 14);
				ivjstcLblClass.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblClass;
	}

	/**
	 * Return the stcLblColorMajor property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblColorMajor()
	{
		if (ivjstcLblColorMajor == null)
		{
			try
			{
				ivjstcLblColorMajor = new JLabel();
				ivjstcLblColorMajor.setBounds(39, 89, 77, 14);
				ivjstcLblColorMajor.setName("stcLblColorMajor");
				ivjstcLblColorMajor.setText(MAJORCOLOR);
				ivjstcLblColorMajor.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
	 * @return JLabel
	 */
	private JLabel getstcLblColorMinor()
	{
		if (ivjstcLblColorMinor == null)
		{
			try
			{
				ivjstcLblColorMinor = new JLabel();
				ivjstcLblColorMinor.setBounds(39, 113, 77, 14);
				ivjstcLblColorMinor.setName("stcLblColorMinor");
				ivjstcLblColorMinor.setText(MINORCOLOR);
				ivjstcLblColorMinor.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
				ivjstcLblCounty.setName("stcLblCounty");
				ivjstcLblCounty.setText(COUNTY);
				ivjstcLblCounty.setBounds(29, 110, 45, 14);
				ivjstcLblCounty.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
				ivjstcLblDocumentNo.setBounds(9, 15, 96, 14);
				ivjstcLblDocumentNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblDocumentNo;
	}

	/**
	 * Return the stcLblEmptyWeight property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEmptyWeight()
	{
		if (ivjstcLblEmptyWeight == null)
		{
			try
			{
				ivjstcLblEmptyWeight = new JLabel();
				ivjstcLblEmptyWeight.setName("stcLblEmptyWeight");
				ivjstcLblEmptyWeight.setText(EMPTY_WEIGHT);
				ivjstcLblEmptyWeight.setMaximumSize(
					new java.awt.Dimension(81, 14));
				ivjstcLblEmptyWeight.setHorizontalTextPosition(4);
				ivjstcLblEmptyWeight.setBounds(4, 268, 112, 14);
				ivjstcLblEmptyWeight.setMinimumSize(
					new java.awt.Dimension(81, 14));
				ivjstcLblEmptyWeight.setHorizontalAlignment(4);
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
		return ivjstcLblEmptyWeight;
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
				ivjstcLblExpires.setName("stcLblExpires");
				ivjstcLblExpires.setText(EXPIRES);
				ivjstcLblExpires.setBounds(17, 34, 56, 14);
				ivjstcLblExpires.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblExpires;
	}

	/**
	 * Return the stcLblFeet property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblFeet()
	{
		if (ivjstcLblFeet == null)
		{
			try
			{
				ivjstcLblFeet = new JLabel();
				ivjstcLblFeet.setName("stcLblFeet");
				ivjstcLblFeet.setText(FT);
				ivjstcLblFeet.setBounds(248, 428, 15, 14);
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
		return ivjstcLblFeet;
	}

	/**
	 * Return the stcLblGrossWeight property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblGrossWeight()
	{
		if (ivjstcLblGrossWeight == null)
		{
			try
			{
				ivjstcLblGrossWeight = new JLabel();
				ivjstcLblGrossWeight.setName("stcLblGrossWeight");
				ivjstcLblGrossWeight.setText(GROSS_WEIGHT);
				ivjstcLblGrossWeight.setMaximumSize(
					new java.awt.Dimension(80, 14));
				ivjstcLblGrossWeight.setHorizontalTextPosition(4);
				ivjstcLblGrossWeight.setBounds(4, 318, 112, 14);
				ivjstcLblGrossWeight.setMinimumSize(
					new java.awt.Dimension(80, 14));
				ivjstcLblGrossWeight.setHorizontalAlignment(4);
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
		return ivjstcLblGrossWeight;
	}

	/**
	 * Return the stcLblInches property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblInches()
	{
		if (ivjstcLblInches == null)
		{
			try
			{
				ivjstcLblInches = new JLabel();
				ivjstcLblInches.setName("stcLblInches");
				ivjstcLblInches.setText(IN);
				ivjstcLblInches.setBounds(292, 428, 14, 14);
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
		return ivjstcLblInches;
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
				ivjstcLblIssued.setBounds(59, 34, 45, 14);
				ivjstcLblIssued.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblIssued;
	}

	/**
	 * Return the stcLblNewTitleType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblNewTitleType()
	{
		if (ivjstcLblNewTitleType == null)
		{
			try
			{
				ivjstcLblNewTitleType = new JLabel();
				ivjstcLblNewTitleType.setName("stcLblNewTitleType");
				ivjstcLblNewTitleType.setText(NEW_TITLE_TYPE);
				ivjstcLblNewTitleType.setMaximumSize(
					new java.awt.Dimension(85, 14));
				ivjstcLblNewTitleType.setHorizontalTextPosition(4);
				ivjstcLblNewTitleType.setBounds(4, 3, 112, 14);
				ivjstcLblNewTitleType.setMinimumSize(
					new java.awt.Dimension(85, 14));
				ivjstcLblNewTitleType.setHorizontalAlignment(4);
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
		return ivjstcLblNewTitleType;
	}

	/**
	 * Return the stcLblOdometerBrand property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOdometerBrand()
	{
		if (ivjstcLblOdometerBrand == null)
		{
			try
			{
				ivjstcLblOdometerBrand = new JLabel();
				ivjstcLblOdometerBrand.setSize(37, 14);
				ivjstcLblOdometerBrand.setName("stcLblOdometerBrand");
				
				// defect 11126
				// This does nothing  
				//ivjstcLblOdometerBrand.setDisplayedMnemonic(79);
				// end defect 11126
				
				ivjstcLblOdometerBrand.setText(BRAND);
				ivjstcLblOdometerBrand.setMaximumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblOdometerBrand.setHorizontalTextPosition(4);
				ivjstcLblOdometerBrand.setMinimumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblOdometerBrand.setHorizontalAlignment(4);
				// user code begin {1}
				ivjstcLblOdometerBrand.setLocation(76, 31);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblOdometerBrand;
	}

	/**
	 * Return the stcLblOdometerReading property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOdometerReading()
	{
		if (ivjstcLblOdometerReading == null)
		{
			try
			{
				ivjstcLblOdometerReading = new JLabel();
				ivjstcLblOdometerReading.setName(
					"stcLblOdometerReading");
				ivjstcLblOdometerReading.setDisplayedMnemonic(79);
				ivjstcLblOdometerReading.setText(ODOMETER_READING);
				ivjstcLblOdometerReading.setMaximumSize(
					new java.awt.Dimension(109, 14));
				ivjstcLblOdometerReading.setHorizontalTextPosition(4);
				ivjstcLblOdometerReading.setMinimumSize(
					new java.awt.Dimension(109, 14));
				ivjstcLblOdometerReading.setHorizontalAlignment(4);
				// user code begin {1}
				// defect 4966 - move here so underscore displays
				ivjstcLblOdometerReading.setDisplayedMnemonic(79);
				// end defect 4966
				ivjstcLblOdometerReading.setLabelFor(
					gettxtVehicleOdometerReading());
				// user code end
				ivjstcLblOdometerReading.setSize(109, 19);
				ivjstcLblOdometerReading.setLocation(4, 3);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblOdometerReading;
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
			ivjstcLblOrg.setBounds(41, 91, 32, 14);
			ivjstcLblOrg.setText("Org:");
			ivjstcLblOrg.setName("stcLblOrg");
			ivjstcLblOrg.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblOrg;
	}

	/**
	 * Return the stcLblOwner property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOwner()
	{
		if (ivjstcLblOwner == null)
		{
			try
			{
				ivjstcLblOwner = new JLabel();
				ivjstcLblOwner.setName("stcLblOwner");
				ivjstcLblOwner.setText(OWNER_ID);
				ivjstcLblOwner.setBounds(21, 2, 58, 14);
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
		return ivjstcLblOwner;
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
				ivjstcLblPlate.setName("stcLblPlate");
				ivjstcLblPlate.setText(PLATE);
				ivjstcLblPlate.setBounds(28, 15, 45, 14);
				ivjstcLblPlate.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblPlate;
	}

	/**
	 * Return the stcLblTonnage property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTonnage()
	{
		if (ivjstcLblTonnage == null)
		{
			try
			{
				ivjstcLblTonnage = new JLabel();
				ivjstcLblTonnage.setName("stcLblTonnage");
				ivjstcLblTonnage.setText(TONNAGE);
				ivjstcLblTonnage.setMaximumSize(
					new java.awt.Dimension(52, 14));
				ivjstcLblTonnage.setHorizontalTextPosition(4);
				ivjstcLblTonnage.setBounds(4, 346, 112, 14);
				ivjstcLblTonnage.setMinimumSize(
					new java.awt.Dimension(52, 14));
				ivjstcLblTonnage.setHorizontalAlignment(4);
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
		return ivjstcLblTonnage;
	}

	/**
	 * Return the stcLblTrailerType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTrailerType()
	{
		if (ivjstcLblTrailerType == null)
		{
			try
			{
				ivjstcLblTrailerType = new JLabel();
				ivjstcLblTrailerType.setName("stcLblTrailerType");
				ivjstcLblTrailerType.setText(TRAILER);
				ivjstcLblTrailerType.setMaximumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblTrailerType.setHorizontalTextPosition(4);
				ivjstcLblTrailerType.setVerticalTextPosition(1);
				ivjstcLblTrailerType.setVerticalAlignment(1);
				ivjstcLblTrailerType.setBounds(180, 315, 39, 17);
				ivjstcLblTrailerType.setMinimumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblTrailerType.setHorizontalAlignment(4);
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
		return ivjstcLblTrailerType;
	}

	/**
	 * Return the stcLblTrailerType2 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTrailerType2()
	{
		if (ivjstcLblTrailerType2 == null)
		{
			try
			{
				ivjstcLblTrailerType2 = new JLabel();
				ivjstcLblTrailerType2.setName("stcLblTrailerType2");
				ivjstcLblTrailerType2.setText(TYPE);
				ivjstcLblTrailerType2.setMaximumSize(
					new java.awt.Dimension(30, 14));
				ivjstcLblTrailerType2.setHorizontalTextPosition(4);
				ivjstcLblTrailerType2.setVerticalTextPosition(1);
				ivjstcLblTrailerType2.setVerticalAlignment(1);
				ivjstcLblTrailerType2.setBounds(178, 335, 39, 17);
				ivjstcLblTrailerType2.setMinimumSize(
					new java.awt.Dimension(30, 14));
				ivjstcLblTrailerType2.setHorizontalAlignment(4);
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
		return ivjstcLblTrailerType2;
	}

	/**
	 * Return the stcLblTravelTlrWidth property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTravelTlrWidth()
	{
		if (ivjstcLblTravelTlrWidth == null)
		{
			try
			{
				ivjstcLblTravelTlrWidth = new JLabel();
				ivjstcLblTravelTlrWidth.setName("stcLblTravelTlrWidth");
				ivjstcLblTravelTlrWidth.setText(TRVL_TRL_WIDTH);
				ivjstcLblTravelTlrWidth.setBounds(139, 428, 78, 14);
				ivjstcLblTravelTlrWidth.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblTravelTlrWidth;
	}

	/**
	 * Return the stcLblTrlLength property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTrlLength()
	{
		if (ivjstcLblTrlLength == null)
		{
			try
			{
				ivjstcLblTrlLength = new JLabel();
				ivjstcLblTrlLength.setName("stcLblTrlLength");
				ivjstcLblTrlLength.setText(TRVL_TRL_LENGTH);
				ivjstcLblTrlLength.setBounds(3, 428, 88, 14);
				ivjstcLblTrlLength.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblTrlLength;
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
				ivjstcLblType.setName("stcLblType");
				ivjstcLblType.setText(TYPE);
				ivjstcLblType.setBounds(28, 72, 45, 14);
				ivjstcLblType.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblType;
	}

	/**
	 * Return the stcLblVehicleClass property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVehicleClass()
	{
		if (ivjstcLblVehicleClass == null)
		{
			try
			{
				ivjstcLblVehicleClass = new JLabel();
				ivjstcLblVehicleClass.setName("stcLblVehicleClass");
				ivjstcLblVehicleClass.setText(VEHICLE_CLASS);
				ivjstcLblVehicleClass.setMaximumSize(
					new java.awt.Dimension(80, 14));
				ivjstcLblVehicleClass.setHorizontalTextPosition(2);
				ivjstcLblVehicleClass.setBounds(3, 185, 112, 14);
				ivjstcLblVehicleClass.setMinimumSize(
					new java.awt.Dimension(80, 14));
				ivjstcLblVehicleClass.setHorizontalAlignment(4);
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
		return ivjstcLblVehicleClass;
	}

	/**
	 * Return the stcLblVehicleModel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVehicleModel()
	{
		if (ivjstcLblVehicleModel == null)
		{
			try
			{
				ivjstcLblVehicleModel = new JLabel();
				ivjstcLblVehicleModel.setName("stcLblVehicleModel");
				ivjstcLblVehicleModel.setText(MODEL);
				ivjstcLblVehicleModel.setMaximumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblVehicleModel.setHorizontalTextPosition(4);
				ivjstcLblVehicleModel.setBounds(4, 66, 112, 14);
				ivjstcLblVehicleModel.setMinimumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblVehicleModel.setHorizontalAlignment(4);
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
		return ivjstcLblVehicleModel;
	}

	/**
	 * Return the stcLblVIN property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVIN()
	{
		if (ivjstcLblVIN == null)
		{
			try
			{
				ivjstcLblVIN = new JLabel();
				ivjstcLblVIN.setName("stcLblVIN");
				ivjstcLblVIN.setText(VIN);
				ivjstcLblVIN.setMaximumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVIN.setHorizontalTextPosition(4);
				ivjstcLblVIN.setBounds(3, 136, 112, 14);
				ivjstcLblVIN.setMinimumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVIN.setHorizontalAlignment(4);
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
		return ivjstcLblVIN;
	}

	/**
	 * Return the stcLblYearMake property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblYearMake()
	{
		if (ivjstcLblYearMake == null)
		{
			try
			{
				ivjstcLblYearMake = new JLabel();
				ivjstcLblYearMake.setName("stcLblYearMake");
				ivjstcLblYearMake.setText(YEAR_MAKE);
				ivjstcLblYearMake.setMaximumSize(
					new java.awt.Dimension(63, 14));
				ivjstcLblYearMake.setHorizontalTextPosition(4);
				ivjstcLblYearMake.setBounds(6, 23, 112, 14);
				ivjstcLblYearMake.setMinimumSize(
					new java.awt.Dimension(63, 14));
				ivjstcLblYearMake.setHorizontalAlignment(4);
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
		return ivjstcLblYearMake;
	}

	/**
	 * Return the txtBodyStyle property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtBodyStyle()
	{
		if (ivjtxtBodyStyle == null)
		{
			try
			{
				ivjtxtBodyStyle = new RTSInputField();
				ivjtxtBodyStyle.setName("txtBodyStyle");
				ivjtxtBodyStyle.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtBodyStyle.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtBodyStyle.setBounds(127, 41, 47, 20);
				ivjtxtBodyStyle.setMaxLength(VEHBDYSTLY_MAX_LEN);
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
		return ivjtxtBodyStyle;
	}

	/**
	 * Return the txtMake property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMake()
	{
		if (ivjtxtMake == null)
		{
			try
			{
				ivjtxtMake = new RTSInputField();
				ivjtxtMake.setName("txtMake");
				ivjtxtMake.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtMake.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMake.setBounds(181, 20, 42, 20);
				ivjtxtMake.setMaxLength(VEHMAKE_MAX_LEN);
				// user code begin {1}
				// defect 11126
				// ivjtxtMake.addActionListener(this);
				// end defect 11126 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMake;
	}

	/**
	 * Return the txtModel property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtModel()
	{
		if (ivjtxtModel == null)
		{
			try
			{
				ivjtxtModel = new RTSInputField();
				ivjtxtModel.setName("txtModel");
				ivjtxtModel.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtModel.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtModel.setBounds(127, 63, 47, 20);
				ivjtxtModel.setMaxLength(VEHBDY);
				// user code begin {1}
				// defect 11126
				// ivjtxtModel.addActionListener(this);
				// end defect 11126 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtModel;
	}

	/**
	 * Return the txtTlrWidthFeet property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTlrWidthFeet()
	{
		if (ivjtxtTlrWidthFeet == null)
		{
			try
			{
				ivjtxtTlrWidthFeet = new RTSInputField();
				ivjtxtTlrWidthFeet.setName("txtTlrWidthFeet");
				ivjtxtTlrWidthFeet.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtTlrWidthFeet.setBounds(221, 425, 24, 20);
				ivjtxtTlrWidthFeet.setMaxLength(TRLNGTHFT_MAX_LEN);
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
		return ivjtxtTlrWidthFeet;
	}

	/**
	 * Return the txtTlrWidthInches property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTlrWidthInches()
	{
		if (ivjtxtTlrWidthInches == null)
		{
			try
			{
				ivjtxtTlrWidthInches = new RTSInputField();
				ivjtxtTlrWidthInches.setName("txtTlrWidthInches");
				ivjtxtTlrWidthInches.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtTlrWidthInches.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjtxtTlrWidthInches.setBounds(267, 425, 22, 20);
				ivjtxtTlrWidthInches.setMaxLength(TRLNGTHIN_MAX_LEN);
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
		return ivjtxtTlrWidthInches;
	}

	/**
	 * Return the txtVehicleBodyVIN property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleBodyVIN()
	{
		if (ivjtxtVehicleBodyVIN == null)
		{
			try
			{
				ivjtxtVehicleBodyVIN = new RTSInputField();
				ivjtxtVehicleBodyVIN.setName("txtVehicleBodyVIN");
				ivjtxtVehicleBodyVIN.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtVehicleBodyVIN.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleBodyVIN.setBounds(126, 159, 178, 20);
				ivjtxtVehicleBodyVIN.setMaxLength(VIN_MAX_LEN);
				// user code begin {1}
				// defect 11126
				// ivjtxtVehicleBodyVIN.addActionListener(this);
				// end defect 11126 
				// defect 8523 
				ivjtxtVehicleBodyVIN.addFocusListener(this);
				// end defect 8523 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleBodyVIN;
	}

	/**
	 * Return the txtVehicleCarryingCapacity property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleCarryingCapacity()
	{
		if (ivjtxtVehicleCarryingCapacity == null)
		{
			try
			{
				ivjtxtVehicleCarryingCapacity = new RTSInputField();
				ivjtxtVehicleCarryingCapacity.setName(
					"txtVehicleCarryingCapacity");
				ivjtxtVehicleCarryingCapacity.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtVehicleCarryingCapacity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleCarryingCapacity.setBounds(127, 291, 82, 20);
				ivjtxtVehicleCarryingCapacity.setHorizontalAlignment(4);
				ivjtxtVehicleCarryingCapacity.setMaxLength(
					CARYCAP_MAX_LEN);
				// user code begin {1}
				ivjtxtVehicleCarryingCapacity.addFocusListener(this);
				// defect 11126
				// ivjtxtVehicleCarryingCapacity.addActionListener(this);
				// end defect 11126 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleCarryingCapacity;
	}

	/**
	 * Return the txtVehicleEmptyWeight property value.
	 * 
	 * @return RTSInputField 
	 */
	private RTSInputField gettxtVehicleEmptyWeight()
	{
		if (ivjtxtVehicleEmptyWeight == null)
		{
			try
			{
				ivjtxtVehicleEmptyWeight = new RTSInputField();
				ivjtxtVehicleEmptyWeight.setName(
					"txtVehicleEmptyWeight");
				ivjtxtVehicleEmptyWeight.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtVehicleEmptyWeight.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleEmptyWeight.setBounds(127, 265, 82, 20);
				ivjtxtVehicleEmptyWeight.setHorizontalAlignment(4);
				ivjtxtVehicleEmptyWeight.setMaxLength(EMPTYWT_MAX_LEN);
				// user code begin {1}
				ivjtxtVehicleEmptyWeight.addFocusListener(this);
				// defect 11126
				// ivjtxtVehicleEmptyWeight.addActionListener(this);
				// end defect 11126 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleEmptyWeight;
	}

	/**
	 * Return the txtVehicleOdometerReading property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleOdometerReading()
	{
		if (ivjtxtVehicleOdometerReading == null)
		{
			try
			{
				ivjtxtVehicleOdometerReading = new RTSInputField();
				ivjtxtVehicleOdometerReading.setBounds(124, 2, 66, 20);
				ivjtxtVehicleOdometerReading.setName(
					"txtVehicleOdometerReading");
				
				// defect 11126 
				ivjtxtVehicleOdometerReading.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				// end defect 11126 
				
				ivjtxtVehicleOdometerReading.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleOdometerReading.setMaxLength(
					ODORDNG_MAX_LEN);
				// user code begin {1}
				// defect 7895
				//	Add FocusListener, setFocusTraversalKeysEnabled
				//	 for focusGain, focusLost
				ivjtxtVehicleOdometerReading.addFocusListener(this);
				ivjtxtVehicleOdometerReading
					.setFocusTraversalKeysEnabled(
					false);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleOdometerReading;
	}

	/**
	 * Return the txtVehicleTonnage property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleTonnage()
	{
		if (ivjtxtVehicleTonnage == null)
		{
			try
			{
				ivjtxtVehicleTonnage = new RTSInputField();
				ivjtxtVehicleTonnage.setName("txtVehicleTonnage");
				ivjtxtVehicleTonnage.setInput(
					RTSInputField.DOLLAR_ONLY);
				ivjtxtVehicleTonnage.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleTonnage.setBounds(127, 343, 42, 20);
				ivjtxtVehicleTonnage.setHorizontalAlignment(4);
				ivjtxtVehicleTonnage.setMaxLength(TONNAGE_MAX_LEN);
				// user code begin {1}
				// defect 11126
				// ivjtxtVehicleTonnage.addActionListener(this);
				// end defect 11126 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleTonnage;
	}

	/**
	 * Return the ivjtxtTrlrLngth property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTrlrLngth()
	{
		if (ivjtxtTrlrLngth == null)
		{
			try
			{
				ivjtxtTrlrLngth = new RTSInputField();
				ivjtxtTrlrLngth.setName("ivjtxtTrlrLngth");
				ivjtxtTrlrLngth.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtTrlrLngth.setBounds(100, 425, 33, 20);
				ivjtxtTrlrLngth.setMaxLength(LNGTH_MAX_LEN);
				// user code begin {1}
				// defect 11126 
				// ivjtxtTrlrLngth.addActionListener(this);
				// end defect 11126
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtTrlrLngth;
	}

	/**
	 * Return the txtVIN property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVIN()
	{
		if (ivjtxtVIN == null)
		{
			try
			{
				ivjtxtVIN = new RTSInputField();
				ivjtxtVIN.setName("txtVIN");
				ivjtxtVIN.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtVIN.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVIN.setBounds(126, 133, 178, 20);
				ivjtxtVIN.setMaxLength(VIN_MAX_LEN);
				// user code begin {1}
				ivjtxtVIN.addFocusListener(this);
				// defect 11126
				// ivjtxtVIN.addActionListener(this);
				// end defect 11126 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVIN;
	}

	/**
	 * Return the txtYear property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtYear()
	{
		if (ivjtxtYear == null)
		{
			try
			{
				ivjtxtYear = new RTSInputField();
				ivjtxtYear.setName("txtYear");
				ivjtxtYear.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtYear.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtYear.setBounds(127, 20, 47, 20);
				ivjtxtYear.setMaxLength(YR_MAX_LEN);
				// user code begin {1}
				//ivjtxtYear.addFocusListener(this);
				// defect 11126  
				// ivjtxtYear.addActionListener(this);
				// end defect 11126
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtYear;
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
	 * Process Help Request based upon TransCd 
	 *
	 */
	private void handleHelp()
	{
		if (csTransCd.equals(TransCdConstant.DTAORK)
				|| csTransCd.equals(TransCdConstant.DTANTK))
		{
			RTSHelp.displayHelp(RTSHelp.TTL003B);
		}
		else
		{
			if (csTransCd
					.equals(TransCdConstant.DTANTD)
					|| csTransCd.equals(
							TransCdConstant.DTAORD))
			{
				RTSHelp.displayHelp(RTSHelp.TTL003C);
			}
			else
			{
				if (csTransCd
						.equals(TransCdConstant.TITLE)
						|| csTransCd.equals(
								TransCdConstant.NONTTL)
								|| csTransCd.equals(
										TransCdConstant.REJCOR))
				{
					if (UtilityMethods.isMFUP())
					{
						RTSHelp.displayHelp(
								RTSHelp.TTL003A);
					}
					else
					{
						RTSHelp.displayHelp(
								RTSHelp.TTL003D);
					}
				}
			}
		}

	}
	/**
	 * Make call for Standard Presumptive AbstractValue
	 * 
	 */
	private void handleSPV()
	{
		PresumptiveValueData laPresumptiveValueDataNew =
			new PresumptiveValueData();

		String lsOdmtrRdng =
			gettxtVehicleOdometerReading().getText();

		int liOdmtrRdng = 0;
		try
		{
			liOdmtrRdng =
				Integer.parseInt(
						lsOdmtrRdng);
		}
		catch (NumberFormatException aeNFEx)
		{
			liOdmtrRdng = 0;
		}
		laPresumptiveValueDataNew
		.setOdometerReading(
				liOdmtrRdng);
		laPresumptiveValueDataNew.setVIN(gettxtVIN().getText());

		// Used for reporting
		laPresumptiveValueDataNew
		.setOfficeIssuanceNo(
				SystemProperty
				.getOfficeIssuanceNo());

		getController().processData(
				VCTitleRecordTTL003.PRESUMP_VAL,
				laPresumptiveValueDataNew);
	}
	
	/**
	 * Handle the enabling/selection of Travel Trailer Checkbox 
	 * 
	 */
	private void handleTravelTrailerCheckbox()
	{
		String lsVehClassCd = caVehData.getVehClassCd();

		int liRegClassCd = caRegData.getRegClassCd();

		if (!UtilityMethods.isEmpty(lsVehClassCd))
		{
			if (lsVehClassCd.equals("TRLR"))
			{
				if (liRegClassCd == 
					RegistrationConstant.REGCLASSCD_TITLE_ONLY)
				{
					getchkTravelTrailer().setEnabled(true);
					getchkTravelTrailer().setSelected(
							caRegClassData.getRegClassCd()== 
								RegistrationConstant.REGCLASSCD_TRAVEL_TRAILER);
				}
				else if (liRegClassCd == RegistrationConstant.REGCLASSCD_TRAVEL_TRAILER)
					{
						getchkTravelTrailer().setSelected(true);
						getchkTravelTrailer().setEnabled(false);
					}
			}
		else 
		{
			getchkTravelTrailer().setSelected(false);
			getchkTravelTrailer().setEnabled(false);
		}
	}
	}

	/**
	 * Initialize the combo boxes.
	 * 
	 * @param aaBox JComboBox
	 * @param avElmts Vector
	 */
	private void initComboBox(JComboBox aaBox, Vector avElmts)
	{
		int liSelctdIndx = INT_ZERO;
		if (avElmts != null)
		{
			if (aaBox.getSelectedIndex() > -1)
			{
				liSelctdIndx = aaBox.getSelectedIndex();
			}
			aaBox.removeAllItems();
			if (aaBox
				.getName()
				.equals(getcomboOdometerBrandChoice().getName()))
			{
				String lsOdoRdng =
					gettxtVehicleOdometerReading().getText();
				String lsOdoBrnd =
					caVehData.getVehOdmtrBrnd();
				if (lsOdoRdng != null && lsOdoRdng.equals(EXEMPT))
				{
					getstcLblOdometerBrand().setEnabled(false);
					aaBox.setEnabled(false);
					aaBox.removeAllItems();
					getcomboOdometerBrandChoice().setSelectedIndex(-1);
				}
				else if (lsOdoBrnd != null && lsOdoBrnd.equals(N))
				{
					getstcLblOdometerBrand().setEnabled(false);
					aaBox.setEnabled(false);
					for (int liIndex = INT_ZERO;
						liIndex < avElmts.size();
						liIndex++)
					{
						if (avElmts
							.get(liIndex)
							.toString()
							.equals(STR_N))
						{
							aaBox.addItem(avElmts.get(liIndex));
							break;
						}
					}
					aaBox.setSelectedIndex(INT_ZERO);
				}
				else if (lsOdoBrnd != null && lsOdoBrnd.equals(X))
				{
					getstcLblOdometerBrand().setEnabled(false);
					aaBox.setEnabled(false);
					for (int liIndex = INT_ZERO;
						liIndex < avElmts.size();
						liIndex++)
					{
						if (avElmts
							.get(liIndex)
							.toString()
							.equals(STR_X))
						{
							aaBox.addItem(avElmts.get(liIndex));
							break;
						}
					}
					aaBox.setSelectedIndex(INT_ZERO);
				}
				else
				{
					for (int liIndex = INT_ZERO;
						liIndex < avElmts.size();
						liIndex++)
						aaBox.addItem(avElmts.get(liIndex));
					{
					}
					aaBox.setSelectedIndex(liSelctdIndx);
				}
			}
			else
			{
				for (int liIndex = INT_ZERO;
					liIndex < avElmts.size();
					liIndex++)
					aaBox.addItem(avElmts.get(liIndex));
				{
				}
				aaBox.setSelectedIndex(liSelctdIndx);
			}
		}
		// defect 8479
		comboBoxHotKeyFix(aaBox);
		// end defect 8479
	}

	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName(ScreenConstant.TTL003_FRAME_NAME);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(653, 558);
			setModal(true);
			setTitle(ScreenConstant.TTL003_FRAME_TITLE);
			setContentPane(getFrmTitleRecordTTL003ContentPane1());
			// user code begin {1}
			setRequestFocus(false);
			getradioValid().setSelected(true);
			// user code end
		}
		catch (Throwable leException)
		{
			handleException(leException);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Convert an int to a boolean.
	 * <br>0 is false.
	 * <br>Non-zero is true.
	 * 
	 * @param aiIndi int
	 * @return boolean
	 */
	private boolean int2bool(int aiIndi)
	{
		return (aiIndi != 0);
	}

//	/**
//	 * Determines whether the year model entered is invalid.
//	 *
//	 * @return boolean
//	 * @param strYr String
//	 */
//	private boolean isInvalidYear(String asYr)
//	{
//		boolean lbRet = false;
//		if (asYr != null)
//		{
//			asYr = asYr.trim();
//			if (asYr.length() < YR_MAX_LEN)
//			{
//				lbRet = true;
//			}
//			else if (CommonValidations.isInvalidYearModel(asYr))
//			{
//				lbRet = true;
//			}
//		}
//		return lbRet;
//	}

	/**
	 * Checks to see if Major Color is required to be selected
	 * based on the System AbstractProperty VehColorStartDate.  Returns true
	 * if required and not selected.
	 * 
	 * @return boolean
	 */
	private boolean isMajorColorReqdButNotSelected()
	{
		boolean lbRet = false;

		String lsMajorColor = getMajorColorCdSelected();
		if (RTSDate.getCurrentDate().getYYYYMMDDDate() 
				>= SystemProperty.getVehColorStartDate()
					.getYYYYMMDDDate() 
			&& (lsMajorColor.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
				lbRet = true;
		}
		
		return lbRet;
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
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		// defect 11049 
		if (aaIE.getSource() == getchkTravelTrailer())
		{
			if (getchkTravelTrailer().isSelected()) 
			{
				// defect 11286 
				// Check for null caRegClassData 
				if (caRegClassData!= null && caRegClassData.getRegClassCd() == RegistrationConstant.REGCLASSCD_TITLE_ONLY)
				{
					// end defect 11286 
					caRegClassData = RegistrationClassCache.getRegisClass("TRLR",
							RegistrationConstant.REGCLASSCD_TRAVEL_TRAILER,
							new RTSDate().getYYYYMMDDDate());
					disableFields(caVehInqData.getValidationObject(),false); 
				}
			}
			else
			{
				disableFields(caVehInqData.getValidationObject(),true); 
			}
		}
		// end defect 11049 
		// defect 11126
		
		// defect 11286 
		// Check for null caRegClassData 
		else if (aaIE.getSource() == getchkFixedWeight() && caRegClassData!= null)
		{
			// end defect 11286 

			int liValue = caRegClassData.getCaryngCapReqd();
			
			liValue = getchkFixedWeight().isSelected() ? 0 : liValue; 
			
			setFromRegisClassVehData(liValue, 
					getstcLblCarryingCapacity(), gettxtVehicleCarryingCapacity(), 
					""+caRegData.getVehCaryngCap());
			
			recalcGrossWt(); 
		}
		// end defect 11126 
	}

	/**
	 * Invoked when a key has been pressed.  Handles movement between 
	 * the radio buttons when the up/down, right/left arrow keys are 
	 * pressed.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(java.awt.event.KeyEvent aaKE)
	{
		if (aaKE.getSource() == gettxtVehicleOdometerReading()
			&& aaKE.getKeyCode() == KeyEvent.VK_TAB)
		{
			cbTabKeyPressed = true;
			setVehOdomtrBrand();
			if (aaKE.isShiftDown())
			{
				// we are going backwards
				if (gettxtVehicleBodyVIN().isEnabled())
				{
					gettxtVehicleBodyVIN().requestFocus();
				}
				else
				{
					gettxtVIN().requestFocus();
				}
			}
			else
			{
				// We are going forward
				// defect 8926
				if (getbtnSPV().isEnabled())
				{
					getbtnSPV().requestFocus();
				}
				// end defect 8926
				else if (getcomboOdometerBrandChoice().isEnabled())
				{
					getcomboOdometerBrandChoice().requestFocus();
				}
				else
				{
					// defect 8639
					// Only set to -1 if EXEMPT
					if (gettxtVehicleOdometerReading()
						.getText()
						
						.equals(EXEMPT))
					{
						getcomboOdometerBrandChoice().setSelectedIndex(
							-1);
					}
					// end defect 8639
					gettxtVehicleEmptyWeight().requestFocus();
				}
			}
		}
	}

	/**
	 * Fills the Major Color combo box with the VehicleColorData.
	 */
	private void populateMajorColor()
	{
		if (getcomboMajorColor().isEnabled())
		{
			String lsVehMajorColorCd = caVehData.getVehMjrColorCd();
			int liColorDefault = -1;
			Vector lvComboVal = new Vector();
			if (cvVehColor != null)
			{
				for (int liIndex = INT_ZERO;
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
			String lsVehMinorColorCd = caVehData.getVehMnrColorCd();
			int liColorDefault = -1;
			Vector lvComboVal = new Vector();
			if (cvVehColor != null)
			{
				for (int liIndex = INT_ZERO;
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
			for (int liIndex = INT_ZERO;
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
	 * 
	 */
	private void recalcGrossWt()
	{
		String lsEmptyWt =
			gettxtVehicleEmptyWeight().getText();
		String lsCarryingCap =
			gettxtVehicleCarryingCapacity().getText();
		int liGrossWt =
			CommonValidations.calcGrossWeight(
				lsEmptyWt,
				lsCarryingCap);
		String lsGrossWt = liGrossWt == 0 ? new String() : ""+liGrossWt; 
		getlblVehicleGrossWeight().setText(lsGrossWt);
	}
	
	/**
	 * Reset Registration for Off-Highway & (ATV || ROV) 
	 * 
	 * Reset Prior Registration if previously (ATV || ROV) 
	 */
	private void resetRegForOffHwy_ATV_ROV()
	{
		caRegData.setRegPltCd(new String());
		caRegData.setRegPltNo(new String());
		caRegData.setHoopsRegPltNo(new String());
		caRegData.setRegPltAge(INT_ZERO);
		caRegData.setRegStkrCd(new String());
		caRegData.setRegExpMo(INT_ZERO);
		caRegData.setRegExpYr(INT_ZERO);
		caRegData.setRegEffDt(INT_ZERO);
		caRegData.setRegIssueDt(INT_ZERO);

		caVehInqData.getMfVehicleData().setSpclPltRegisData(null);

		Object laObj = caVehInqData.getValidationObject();

		// Ensure that Registration cleared for Original Vehicle if ATV 
		//  || ROV 
		if (laObj != null && laObj instanceof TitleValidObj)
		{
			TitleValidObj laTtlValObj = (TitleValidObj) laObj;
			MFVehicleData laMFVehData =
				(MFVehicleData) laTtlValObj.getMfVehOrig();

			if (laMFVehData != null
				&& laMFVehData.getRegData() != null)
			{
				if (laMFVehData.getRegData().isATV_ROV())
				{
					laMFVehData.setRegData(
						(RegistrationData) UtilityMethods.copy(caRegData));
				}
			}
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information to the 
	 * view.
	 *
	 * param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			// defect 11126
			// defect 10290
			// Ignore RTSException  (from DTA) 
			if (aaDataObject != null
				&& !(aaDataObject instanceof RTSException))
			{
				caVehInqData = (VehicleInquiryData) aaDataObject;
				caMFVehicleData = caVehInqData.getMfVehicleData();
				
				caVehData =
					caMFVehicleData.getVehicleData();
				caRegData = 
					caMFVehicleData.getRegData();
				caTtlData = caMFVehicleData.getTitleData();
				csTransCd = getController().getTransCode(); 
				OwnerData laOwnrData = caMFVehicleData.getOwnerData();
				TitleValidObj laVObj =
					(TitleValidObj) caVehInqData.getValidationObject();

				// defect 10507 	
				// Indicators may need reevaluation upon return thru setData()
				getIndicators();

				if (cbAlreadySet == false)
				{
					cbAlreadySet = true;
					// end defect 10507 

					// defect 10369 
					// ByPass Registration validation for 
					// Corrected Title 
					// Check for Invalid Registration
					// defect 8900 
					// Pass TitleType 
					if (caTtlData.getTtlTypeIndi()
						!= TitleTypes.INT_CORRECTED)
					{
						doInvalidRegis();
						doClassPltStkrCheck();
					}
					// end defect 10369 

					// doTitleTypeNotCorrect must follow doClassPltStkrCheck
					doTitleTypeNotCorrect();

					// defect 10369 	
					if (caTtlData.getTtlTypeIndi()
						!= TitleTypes.INT_CORRECTED)
					{
						doCheckIfRegisMustChange();
					}
					// end defect 10369 

					// Get and Set descriptions on screen
					getlblDocumentType().setText(
						laVObj.getDocTypeCdDesc());
					
					// Get and Set owner data
					getlblOwnerId().setText(laOwnrData.getOwnrId());
					getlblOwnerName().setText(laOwnrData.getName1());
					getlblOwnerName2().setText(laOwnrData.getName2());
					
					// Set Vehicle Data 
					setVehicleDataToDisplay();
					
					// setTitle Data
					setTitleDataToDisplay(); 
					
					// Set Registration Data 
					setRegistrationDataToDisplay(); 

					// This block of code was moved outside the 
					//	bIndicatorsDone check because it needs
					//   to be set everytime you come to setData.  
					//	The rest of the fields inside the
					//   bIndicatorsDone check should only be done the 
					//	first time you enter the screen.
					//   Otherwise the values the user enters are wiped 
					//	out the next time setData is called.
					// BEGIN BLOCK OF CODE
					
					// defect 10779 
					// defect 10689
					populateVehColorVector();
					populateMajorColor(); 
					populateMinorColor(); 
					// end defect 10689
					// end defect 10779 
				} // End If cbIndicatorsDone
				
				// The following may change w/ Change Registration
				
				// VehicleClassData
				if (caVehData.getVehClassCd() != null)
				{
					getlblVehicleClass().setText(
						caVehData.getVehClassCd());
				}

				// Registration Data 
				int liRegCode = caRegData.getRegClassCd();
				int liDate = RTSDate.getCurrentDate().getYYYYMMDDDate();
				
				// defect 10959 
				String lsRegDesc =
					CommonFeesCache
						.getRegClassCdDesc(
								liRegCode,
								liDate);
				//	TitleClientUtilityMethods.getRegClassCdDesc(
				//		liRegCode,
				//		liDate);
				getlblRegClass().setText(lsRegDesc);
				// end defect 10959
		
				
				// Plate Data 
				setPlateDataToDisplay(); 

				// defect 10416
				// Disable record not applicable push button if 
				//	MF Down or Corrected Title  
				if (caVehInqData.isMFDown()
					|| caTtlData.getTtlTypeIndi()
						== TitleTypes.INT_CORRECTED)
				{
					// end defect 10416 
					getbtnRecNotApplicable().setEnabled(false);
				}

				disableFields(laVObj,true);

				// defect 10524 
				if (gettxtVehicleOdometerReading().isEnabled()
					&& TitleClientUtilityMethods
						.isInvalidOdometerReadingBasedOnTIMA(
						"" + caVehData.getVehModlYr(),
						"" + caVehData.getVehTon(),
						getlblVehicleGrossWeight().getText(),
						gettxtVehicleOdometerReading().getText()))
				{
					gettxtVehicleOdometerReading().setText(EXEMPT);
					setVehOdomtrBrand();
				}
				// end defect 10524 

				if (cvIndicator != null && cvIndicator.size()
						> CommonConstant.MAX_INDI_NO_SCROLL)
				{
					setDefaultFocusField(getlstIndiDescription());
				}
			}
			// end defect 11126 
		}
		catch (NullPointerException aeNPE)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeNPE);
			leRTSEx.displayError(this);
		}
	}

	/**
	 * Sets the data from the screen to the vehicle object.
	 * 
	 */
	private VehicleInquiryData setDataToDataObject()
	{
			VehicleInquiryData laVehInqData = 
				(VehicleInquiryData) UtilityMethods.copy(caVehInqData);
			
			MFVehicleData laMFVehicleData = laVehInqData.getMfVehicleData();
			TitleData laTtlData = laMFVehicleData.getTitleData();
			RegistrationData laRegData = laMFVehicleData.getRegData();
			VehicleData laVehData =
				laMFVehicleData.getVehicleData();

			
			// Validation Object 	
			TitleValidObj laTtlValidObj =
				(TitleValidObj) laVehInqData
				.getValidationObject();
			
			// VEHICLE DATA 
			laVehData.setVehModlYr(Integer.parseInt(gettxtYear().getText()));
			laVehData.setVehMk(gettxtMake().getText());
			laVehData.setVehBdyType(gettxtBodyStyle().getText());
			laVehData.setVehModl(gettxtModel().getText());

			// defect 10689 
			// Major/Minor Color 
			laVehData.setVehMjrColorCd( getMajorColorCdSelected());
			laVehData.setVehMnrColorCd(getMinorColorCdSelected());
			// end defect 10689

			laVehData.setVin( gettxtVIN().getText());
			laVehData.setVehBdyVin(gettxtVehicleBodyVIN().getText());
			
			// ODOMETER / BRAND  
			String lsOdo =
				gettxtVehicleOdometerReading().getText();
			laVehData.setVehOdmtrReadng(lsOdo);
			laVehData.setVehOdmtrBrnd(getOdometerSelection(lsOdo));
	
			// EMPTY WEIGHT
			String lsEmpWt = gettxtVehicleEmptyWeight().getText();
			lsEmpWt = UtilityMethods.isEmpty(lsEmpWt) ? "0": lsEmpWt; 
			int liEmpWt = Integer.parseInt(lsEmpWt);
			laVehData.setVehEmptyWt(liEmpWt);
			
			// CARRYING CAPACITY  
			String lsCarCap =
				gettxtVehicleCarryingCapacity().getText();
			lsCarCap = UtilityMethods.isEmpty(lsCarCap) ? "0" : lsCarCap; 
			int liCarrCap = Integer.parseInt(lsCarCap);
			laRegData.setVehCaryngCap(liCarrCap);
			
			// GROSS WEIGHT 
			String lsGrossWt =
				getlblVehicleGrossWeight().getText();
			lsGrossWt = UtilityMethods.isEmpty(lsGrossWt) ? "0" : lsGrossWt; 
			int	liGrossWt = Integer.parseInt(lsGrossWt);
			laRegData.setVehGrossWt(liGrossWt);
			
			// TONNAGE 
			String lsTon = gettxtVehicleTonnage().getText();
			lsTon = UtilityMethods.isEmpty(lsTon)? "0" : lsTon; 
			Dollar laDlrTon = new Dollar(lsTon);
			laVehData.setVehTon(laDlrTon);
			
			// FIXED WEIGHT 
			laVehData.setFxdWtIndi(getchkFixedWeight().isSelected() ? 1 :0);
			
			// PARK MODEL  
			if (getchkParkModel().isSelected())
			{
				laVehData.setPrmtReqrdIndi(1);
			}
			else
			{
				// needed for spcl exmn 
				if (laVehData.getPrmtReqrdIndi() == 1)
				{
					laVehData.setPrmtReqrdIndi(0);
					laTtlData.setTtlExmnIndi(1);
				}
			}
			
			// TRAILER LENGTH
			String lsTrlrLngth = gettxtTrlrLngth().getText();
			lsTrlrLngth = UtilityMethods.isEmpty(lsTrlrLngth) ? "0" : lsTrlrLngth; 
			laVehData.setVehLngth(Integer.parseInt(lsTrlrLngth)); 
			
			// TRAILER WIDTH  (in inches) 
			String lsTrlWidthFeet = gettxtTlrWidthFeet().getText();
			String lsTrlWidthInch = gettxtTlrWidthInches().getText();

			lsTrlWidthFeet = UtilityMethods.isEmpty(lsTrlWidthFeet) ? "0" :
				lsTrlWidthFeet; 
			
			lsTrlWidthInch = UtilityMethods.isEmpty(lsTrlWidthInch) ? "0" :
				lsTrlWidthInch;
			
			// defect 11049 
			int liWidth = Integer.parseInt(lsTrlWidthFeet)* INCHES_PER_FOOT 
				+ Integer.parseInt(lsTrlWidthInch);
			// end defect 11049 
			
			laVehData.setVehWidth(liWidth);

			// TRAILER TYPE 
			// If enabled, always selected; Use 1st character
			
			String lsTrlType = new String();
			
			if (getcomboTrailerTypeChoice().isEnabled())
			{
				lsTrlType = (String) getcomboTrailerTypeChoice()
				.getSelectedItem();
				lsTrlType = lsTrlType.substring(0, 1);
			}
			laVehData.setTrlrType(lsTrlType);
		
			// VINERRINDI 
			laVehData.setVinErrIndi(0);

			// REGIS DATA 
			laRegData.setPltSeizedIndi(0);
			laRegData.setStkrSeizdIndi(0);
			laRegData.setRegHotCkIndi(0);
			laRegData.setNotfyngCity(new String());
			laRegData.setRegStkrNo(new String());
			
			if (laTtlData.getMustChangePltIndi() == 1)
			{
				laRegData.setRegPltNo(new String());
			}
			
			// Get Original MFVehicle Data 
			MFVehicleData laMfOriginal =
				(MFVehicleData) laTtlValidObj.getMfVehOrig();
			
			// Clear RegExpMo/Yr when UnregisterVehIndi
			if (laRegData.getUnregisterVehIndi() == 1)
			{
				laRegData.setRegExpYr(0);

				PlateTypeData laPltTypeData =
					PlateTypeCache.getPlateType(
					  laMfOriginal.getRegData().getRegPltCd());

				boolean lbOwnerPlt =
					laPltTypeData.getPltOwnrshpCd().equals(
							SpecialPlatesConstant.OWNER);

				if (!lbOwnerPlt)
				{
					laRegData.setRegExpMo(0);
				}
			}
			
			// Clear RegData for Special Plates 
			if (SystemProperty.getOfficeIssuanceNo() == 291
					&& caTtlData.getTtlTypeIndi()
					!= TitleTypes.INT_CORRECTED)
			{
				laRegData.setRegPltNo(
						CommonConstant.STR_SPACE_EMPTY);
				laRegData.setRegExpMo(0);
				laRegData.setRegExpYr(0);
				laRegData.setRegPltAge(
						caRegData.getRegPltAge(false));
			}
			
			// defect 9037
			if (laRegData.getRegPltAge(false)
					== Integer.MAX_VALUE)
			{
				laRegData.setRegPltAge(0);
			}
			// end defect 9037
			
			if (laTtlData.getTtlTypeIndi()
					!= TitleTypes.INT_CORRECTED)
			{
				laRegData.setDpsSaftySuspnIndi(0);
				laRegData.setRegPltOwnrName(new String());
				laRegData.setRegRefAmt(new Dollar(0.00));
				
				// defect 9041 
				// do not reset ExmptIndi if RejCor 
				// If !Standard Exempt, set ExmptIndi to 0 
				if (!CommonFeesCache
						.isStandardExempt(
								caRegData.getRegClassCd())
								&& !csTransCd.equals(
										TransCdConstant.REJCOR))
				{
					laRegData.setExmptIndi(0);
				}
				// end defect 9041 
			}
			
			// TITLE DATA 
			laTtlData.setTtlIssueDate(0);
			laTtlData.setAgncyLoandIndi(0);
			laTtlData.setTtlHotCkIndi(0);
			if (laTtlData.getBndedTtlCd() != null
					&& laTtlData.getBndedTtlCd().equals(W))
			{
				laTtlData.setBndedTtlCd(new String());
			}
			laTtlData.setCcoIssueDate(0);
			laTtlData.setSurrTtlDate(0);
			
			// defect 10841 
			laTtlData.setETtlPrntDate(0);
			// end defect 10841

			// EXPIRED REASON 
			RegTtlAddlInfoData laTtlAddlData =
				new RegTtlAddlInfoData();

			if (getradioValid().isSelected())
			{
				// 1
				laTtlAddlData.setRegExpiredReason(
						RegistrationConstant.EXP_REG_VALID_REASON);
			}
			else if (getradioInvalid().isSelected())
			{
				// 2
				laTtlAddlData.setRegExpiredReason(
						RegistrationConstant.EXP_REG_INVALID_REASON );
			}
			else if (getradioCitation().isSelected())
			{
				// 3
				laTtlAddlData.setRegExpiredReason(RegistrationConstant.EXP_REG_CITATION);
			}
			
			((TitleValidObj) laVehInqData
					.getValidationObject())
					.setRegTtlAddData(
							laTtlAddlData);
			
			// defect 11368 
			// Replica Model Year & Make 
			String lsRepVehModlYr =	gettxtReplicaVehicleModelYear().getText();
			String lsRepVehMk =	gettxtReplicaVehicleMake().getText();
			int liRepVehModlYr = 0; 
			
			if (lsRepVehModlYr != null
					&& lsRepVehModlYr.length() > 0)
			{
				liRepVehModlYr =
					Integer.parseInt(lsRepVehModlYr);
			}
			laVehData.setReplicaVehModlYr(liRepVehModlYr);
			laVehData.setReplicaVehMk(lsRepVehMk);
			// end defect 11368 
			
			return laVehInqData; 
	}
	
	/**
	 * Set Field from RegisClassValue 
	 * 
	 * @param liIndi
	 * @param aaLabel
	 * @param aaInputField
	 * @param asInputData
	 */
	private void setFromRegisClassVehData(int liIndi, JLabel aaLabel, 
			RTSInputField aaInputField, String asInputData)
	{
		boolean lbEnable = int2bool(liIndi);
		
		if (lbEnable) 
		{
			if (aaInputField.isEmpty())
			{
				try
				{
					int liInputData = Integer.parseInt(asInputData);
					if (liInputData == 0)
					{
						asInputData = new String(); 
					}
				}
				catch (NumberFormatException aeNFE)
				{
					
				}
				aaInputField.setText(asInputData);
			}
		}
		else
		{
			aaInputField.setText(new String()); 
		}
		aaInputField.setEnabled(lbEnable);
		aaLabel.setEnabled(lbEnable);
	}
	
	/**
	 * Set Plate Data To display 
	 *
	 */
	private void setPlateDataToDisplay()
	{
		String lsItemCode = caRegData.getRegPltCd();
		getlblPlateType().setText(ItemCodesCache.getItmCdDesc(lsItemCode));

		SpecialPlatesRegisData laSpecialPlatesRegData =
			caMFVehicleData.getSpclPltRegisData();

		String lsOrgNo = null;
		if (laSpecialPlatesRegData != null)
		{
			lsOrgNo = laSpecialPlatesRegData.getOrgNo();

			int liAge = 0;
			if (laSpecialPlatesRegData.isMFDownSP() 
					|| laSpecialPlatesRegData.getPltBirthDate() != 0)
			{
				liAge =
					laSpecialPlatesRegData.getRegPltAge(false);
			}

			// Populate with SP-RegPltNo
			getlblPlateNo().setText(
					laSpecialPlatesRegData.getRegPltNo());
			getlblPlateAge().setText("" + liAge);

			// Get Organization Name
			// Note that OrgNo can be empty string
			String lsOrgName = new String();
			if (!UtilityMethods.isEmpty(lsItemCode)
					&& lsOrgNo != null)
			{
				lsOrgName =
					OrganizationNumberCache.getOrgName(
							lsItemCode,
							lsOrgNo);
			}
			getlblOrg().setText(lsOrgName);
		}
	}
	/**
	 * Set Registration Data to Display 
	 *
	 */
	private void setRegistrationDataToDisplay()
	{
		if (caRegData.getVehCaryngCap() >0)
		{
			gettxtVehicleCarryingCapacity().setText(
					Integer.toString(caRegData.getVehCaryngCap()));
		}
		if (caRegData.getVehGrossWt() > 0)
		{
			getlblVehicleGrossWeight().setText(
					Integer.toString(caRegData.getVehGrossWt()));
		}

		getlblPlateNo().setText(caRegData.getRegPltNo());

		if (caRegData.getRegPltAge(false) > INT_ZERO
				&& caRegData.getRegPltAge(false)
				< Integer.MAX_VALUE)
		{
			getlblPlateAge().setText(
					Integer.toString(
							caRegData.getRegPltAge(false)));
		}

		if (caRegData.getRegExpMo() > INT_ZERO)
		{
			getlblRegExpMo().setText(
					Integer.toString(caRegData.getRegExpMo()));
		}
		if (caRegData.getRegExpYr() > INT_ZERO)
		{
			getlblRegExpYr().setText(
					Integer.toString(caRegData.getRegExpYr()));
		}
		// Get and Set county number and name
		int liOfcNo = caRegData.getResComptCntyNo();
		if (liOfcNo >0)
		{
			getlblResComptCntyNo().setText(
					Integer.toString(liOfcNo));
		}

		// defect 10524
		getlblCntyName().setText(
				OfficeIdsCache.getOfcName(liOfcNo));
		// end defect 10524 
	}
	
	/**
	 * Set Title Data To Display 
	 *
	 */
	private void setTitleDataToDisplay()
	{
		// DocNo
		getlblDocumentNo().setText(caTtlData.getDocNo());
		
		// Issue Date
		int liIssueDate = caTtlData.getTtlIssueDate();
		if (liIssueDate > INT_ZERO)
		{
			getlblIssuedDate().setText(
				new RTSDate(
					RTSDate.YYYYMMDD,
					caTtlData.getTtlIssueDate())
					.toString());
		}
		// Title Type
		int liTtlType = caTtlData.getTtlTypeIndi();
		
		String lsVal =
			CommonValidations.getTtlTypeString(liTtlType);
		
		getlblNewTitleType().setText(lsVal);
	}
	
	/**
	 * Set Vehicle Data To Display 
	 * 
	 */
	private void setVehicleDataToDisplay()
	{
		gettxtYear().setText(
			Integer.toString(caVehData.getVehModlYr()));
		gettxtMake().setText(caVehData.getVehMk());
		gettxtBodyStyle().setText(
					caVehData.getVehBdyType());
		gettxtModel().setText(caVehData.getVehModl());
		gettxtVIN().setText(caVehData.getVin());
		gettxtVehicleBodyVIN().setText(caVehData.getVehBdyVin());
		gettxtVehicleOdometerReading().setText(getEmptyStringIfZero(caVehData.getVehOdmtrReadng()));
		gettxtVehicleEmptyWeight().setText(getEmptyStringIfZero(""+caVehData.getVehEmptyWt()));
		if (caVehData.getVehTon() == null)
		{
			caVehData.setVehTon(new Dollar(0)); 
		}
		gettxtVehicleTonnage().setText(getEmptyStringIfZero(caVehData.getVehTon().getValue()));
		if (caVehData.getFxdWtIndi() == 1)
		{
			getchkFixedWeight().setSelected(true);
			gettxtVehicleCarryingCapacity().setText(new String());
			getstcLblCarryingCapacity().setEnabled(false);
			gettxtVehicleCarryingCapacity().setEnabled(
				false);
			getlblVehicleGrossWeight().setText(
				Integer.toString(
					CommonValidations.calcGrossWeight(
						gettxtVehicleEmptyWeight()
							.getText(),
						gettxtVehicleCarryingCapacity()
							.getText())));
		}
		
		if (caVehData.getPrmtReqrdIndi() == 1)
		{
			// defect 11049 
			// Can only process "MISC" w/ DTANRD  
			if (csTransCd.equals(TransCdConstant.DTAORD))
			{
				if (caVehData.getVehClassCd() != null 
						&&	caVehData.getVehClassCd().equals("TRLR")
						&& caRegData.getRegClassCd() == RegistrationConstant.REGCLASSCD_TITLE_ONLY)
				{
					caRegClassData = RegistrationClassCache.getRegisClass(
							"TRLR",	RegistrationConstant.REGCLASSCD_TRAVEL_TRAILER, 
							new RTSDate().getYYYYMMDDDate());
					handleTravelTrailerCheckbox(); 
				}
			}
			// end defect 11049 
			
			getchkParkModel().setSelected(true);
		}
		gettxtTrlrLngth().setText(getEmptyStringIfZero(""+caVehData.getVehLngth()));
		gettxtTlrWidthFeet().setText(getEmptyStringIfZero(""+(caVehData.getVehWidth() / INCHES_PER_FOOT)));
		gettxtTlrWidthInches().setText(getEmptyStringIfZero(""+(caVehData.getVehWidth() % INCHES_PER_FOOT)));
		
		// defect 11368 
		gettxtReplicaVehicleModelYear().setText(getEmptyStringIfZero(""+(caVehData.getReplicaVehModlYr()))); 
		gettxtReplicaVehicleMake().setText(caVehData.getReplicaVehMk() != null ? caVehData.getReplicaVehMk() : new String());
		if (UtilityMethods.isDTA(csTransCd))
		{
			gettxtReplicaVehicleModelYear().setEnabled(false); 
			gettxtReplicaVehicleMake().setEnabled(false); 
		}
		// end defect 11368 
	}
	
	/**
	 * Set Odometer Brand
	 * 
	 */
	private void setVehOdomtrBrand()
	{
		String lsOdoRd =
			gettxtVehicleOdometerReading().getText();
		if (lsOdoRd.equalsIgnoreCase(EXEMPT))
		{
			getstcLblOdometerBrand().setEnabled(false);
			getcomboOdometerBrandChoice().setEnabled(false);
			getcomboOdometerBrandChoice().setSelectedIndex(-1);
		}
		else
		{
			getstcLblOdometerBrand().setEnabled(true);
			getcomboOdometerBrandChoice().setEnabled(true);
			TitleValidObj laTtlValidObj =
				(TitleValidObj) caVehInqData.getValidationObject();
			initComboBox(
				getcomboOdometerBrandChoice(),
				laTtlValidObj.getOdBrn());
		}
	}
	/**
	 *  Validate Data 
	 *  
	 *  @return boolean 
	 */
	private boolean validateData()
	{
		boolean lbValid = true;

		RTSException leRTSEx = new RTSException(); 
		
		//defect 11228
		if (caTtlData.getExportIndi() == 1)
		{
			RTSException leRTSEx1 = new RTSException(
					ErrorsConstant.ERR_NUM_ACTION_INVALID_EXPORT); 
			leRTSEx1.displayError(this); 
			return false;
		}
		//defect 11228
		
		// defect 10369 
		if (caTtlData.getTtlTypeIndi()
				!= TitleTypes.INT_CORRECTED
				&& doClassPltStkrCheck())
		{
			return false; 
		}
		// end defect 10369 

	
		String lsRegPltCd = caRegData.getRegPltCd();
		String lsVehClassCd = caVehData.getVehClassCd();

		String lsYrMdl = gettxtYear().getText();

		//If VehClassCd = 'MISC' and Original or RPO return  
		if (lsVehClassCd.equals(MISC)
				&& ((caTtlData.getTtlTypeIndi()
						== TitleTypes.INT_ORIGINAL)
						|| caTtlData.getTtlTypeIndi()
						== TitleTypes.INT_REGPURPOSE))
		{
			// 472
			RTSException leRTSEx1 = new RTSException(
					ErrorsConstant.ERR_NUM_TITLE_TYPE_INVALID_FOR_VEHCLASS); 
			leRTSEx1.displayError(this); 
			return false;  
		}

		String lsEmptyWt = gettxtVehicleEmptyWeight().getText();
		String lsVehTon = gettxtVehicleTonnage().getText();
		String lsCarrCap = gettxtVehicleCarryingCapacity().getText();
		String lsGrossWt = getlblVehicleGrossWeight().getText();
		
		int liGrossWt =
			CommonValidations.calcGrossWeight(
					lsEmptyWt,
					lsCarrCap);
		
	
		if (caTtlData.getMustChangePltIndi() == 1)
		{
			boolean lbChgRegCompl =
				((TitleValidObj) caVehInqData
						.getValidationObject())
						.isChangeRegis();

			if (caMFVehicleData.isSpclPlt())
			{
				SpecialPlatesRegisData laSpclPltRegData =
					caMFVehicleData
					.getSpclPltRegisData();
				lbChgRegCompl =
					lbChgRegCompl
					&& laSpclPltRegData.isEnterOnSPL002();
			}

			if (!lbChgRegCompl)
			{
				// 84 
				RTSException leRTSEx1 = 
					new RTSException(ErrorsConstant.ERR_NUM_PLATES_NON_TRANSFERRABLE); 
				leRTSEx1.displayError(this);				
				return false;  
			}
		}

		// defect 9830
		// Check if Change Registration button is enabled. 
		//   Will be disabled if Corrected Title, enabled for Correct Title Rejection.
		if (getbtnChangeRegistration().isEnabled())
		{
			// end defect 9830 
			try
			{
				RegistrationVerify.verifyValidSpclPlt(
						caVehInqData,
						getController().getTransCode());
			}
			catch (RTSException aeRTSEx)
			{
				aeRTSEx.displayError(this);
				return false;  
			}
		}
		// MAKE 
		if (gettxtMake().isEnabled() && gettxtMake().isEmpty())
		{
			// 150
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtMake());
		}
		// defect 10689 
		if (isMajorColorReqdButNotSelected()
				|| isMinorColorSelectedMajorColorNotSelected())
		{
			// 150
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getcomboMajorColor());
		}
		else if (isMajorColorSameAsMinorColor())
		{
			// 160
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_MINOR_MAJOR_COLORS_CANNOT_BE_SAME),
					getcomboMinorColor());
		}
		// end defect 10689
		
		// BODYSTYLE
		if (gettxtBodyStyle().isEnabled() && gettxtBodyStyle().isEmpty())
		{
			// 150 
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtBodyStyle());
		}
		// VIN
		if (gettxtVIN().isEmpty() && !lsVehClassCd.equals(MISC))
		{
			// 176
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_VIN_REQUIRED_TO_TITLE),
					gettxtVIN());
		}
		// YEAR 
		if (gettxtYear().isEmpty() || 
				CommonValidations.isInvalidYearModel(gettxtYear().getText()))
		{
			// 2006
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_MODEL_YEAR_NOT_VALID),
					gettxtYear());
		}
		
		// ODOMETER 
		if (gettxtVehicleOdometerReading().isEnabled())
		{
			if (gettxtVehicleOdometerReading().isEmpty())
			{
				// 150
				leRTSEx.addException(
						new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtVehicleOdometerReading());
			}
			else 
			{
				String lsOdo = gettxtVehicleOdometerReading().getText();		
				if (!TitleClientUtilityMethods.isValidOdometerReading(lsOdo))
				{
					// 150
					leRTSEx.addException(
							new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtVehicleOdometerReading());
				}
				else if (TitleClientUtilityMethods
						.isInvalidOdometerReadingBasedOnTIMA(
								lsYrMdl,
								lsVehTon,
								lsGrossWt,
								lsOdo))
				{
					// 711
					leRTSEx.addException(
							new RTSException(ErrorsConstant.ERR_NUM_ODOMETER_INVALID_BASED_ON_TIMA), 
							gettxtVehicleOdometerReading());
				}
				// defect 11257
				else if (!lsOdo.equalsIgnoreCase(EXEMPT) && 
						getcomboOdometerBrandChoice().getSelectedIndex() == -1) 
				{
					setVehOdomtrBrand(); 
				}
				// end defect 11257 
			}
		}
		// EMPTY WEIGHT
		if (gettxtVehicleEmptyWeight().isEnabled()
				&& (gettxtVehicleEmptyWeight().isEmpty() ||
					Integer.parseInt(gettxtVehicleEmptyWeight().getText())
					== 0))
		{
			// 150
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtVehicleEmptyWeight());
		}
		
		// CARRYING CAPACITY
		if (gettxtVehicleCarryingCapacity().isEnabled() 
				&& (gettxtVehicleCarryingCapacity().isEmpty()
				|| Integer.parseInt(gettxtVehicleCarryingCapacity().getText())
				== 0))

		{
			// 150 
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtVehicleCarryingCapacity());
		}
		// TONNAGE 
		if (gettxtVehicleTonnage().isEnabled()) 
		{
			if (TitleClientUtilityMethods.isVehTonInvalid(lsVehTon))
			{
				// 201
				leRTSEx.addException(
						new RTSException(ErrorsConstant.ERR_NUM_VEH_TONNAGE_INVALID), 
						gettxtVehicleTonnage());
			}
			else if (TitleClientUtilityMethods
					.isVehTonInvalidForDisabledPlates(
							lsRegPltCd,
							lsVehTon))
			{
				// 2010
				leRTSEx.addException(
						new RTSException(ErrorsConstant.ERR_NUM_TONNAGE_LE_2_FOR_DISABLED), 
						gettxtVehicleTonnage());
			}
			else if (TitleClientUtilityMethods
					.isVehTonInvalidForTruckLsEqOne(
							caRegData.getRegClassCd(),
							caVehData.getVehClassCd(),
							lsVehTon))
			{
				// 201
				leRTSEx.addException(
						new RTSException(
								ErrorsConstant.ERR_NUM_VEH_TONNAGE_INVALID), 
								gettxtVehicleTonnage());
			}
			// defect 8703
			// Add Vehicle Class (Apportioned does not have specific
			// '<= 1 ton' and '> 1 ton' Reg Classes.
			else if (TitleClientUtilityMethods
					.isVehTonInvalidForTruckGrt1orComb(
							caRegData.getRegClassCd(),
							caVehData.getVehClassCd(),
							lsVehTon))
			{
				// end defect 9910
				// 201 
				leRTSEx.addException(
						new RTSException(
								ErrorsConstant.ERR_NUM_VEH_TONNAGE_INVALID),
								gettxtVehicleTonnage());
			}
			else if (caRegClassData != null && getchkFixedWeight().isSelected()
					&& TitleClientUtilityMethods.isFixedWtIndi(
							caRegClassData,
							lsVehTon,
							lsGrossWt))
			{
				// 90
				leRTSEx.addException(
						new RTSException(
								ErrorsConstant.ERR_NUM_INCREASE_GROSS_TO_MIN_FOR_FIXED_WT),
								gettxtVehicleEmptyWeight());
			}
		}
		
		if (caRegClassData != null && !getchkFixedWeight().isSelected()
				&& TitleClientUtilityMethods.isTrlCarrCapInvalid(
						caRegClassData,
						lsEmptyWt,
						lsCarrCap))
		{
			// 13
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_CARRYING_CAPACITY_GTE_THIRD_EMPTY_WT), 
					gettxtVehicleCarryingCapacity());
		}
		if (caRegClassData != null && TitleClientUtilityMethods
				.isCarrCapInvalid(
						caRegClassData,
						lsGrossWt,
						lsCarrCap,
						lsVehTon,
						caVehData.getFxdWtIndi()))
		{
			// 200
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_MUST_INCR_CARRYING_CAPACITY),
					gettxtVehicleCarryingCapacity());
		}
		
		String lsTrlType =
			(String) getcomboTrailerTypeChoice()
			.getSelectedItem();
		
		if (caRegClassData != null && !TitleClientUtilityMethods
				.isNonTtlTrlrWtValid(
						caRegClassData,
						caVehData.getVehClassCd(),
						lsTrlType,
						lsGrossWt,
						lsEmptyWt))
		{
			int liGrssWt = Integer.parseInt(lsGrossWt);
			if (liGrssWt > TRLRWT34000)
			{
				// 96; Continues!!! 
				displayError(
						ErrorsConstant.ERR_NUM_TRAILER_MUST_BE_TITLED_IF_NOT_JUST_FARM);  
			}
			else if (liGrssWt > GROSSWT4000)
			{
				// 580
				leRTSEx.addException(
						new RTSException(
								ErrorsConstant.ERR_NUM_TRAILER_MUST_BE_TITLED_OR_REG_FARM_TRAILER),
								gettxtVehicleEmptyWeight());
			}
		}

		if (TitleClientUtilityMethods
				.isTrailerWtInvalid(
						lsVehClassCd,
						caRegData.getRegClassCd(),
						lsTrlType,
						lsGrossWt,
						lsEmptyWt))
		{
			leRTSEx.addException(
					// 142
					new RTSException(
							ErrorsConstant.ERR_NUM_MUST_INCREASE_GROSS_WT_TO_TITLE_TRAILER),
							gettxtVehicleEmptyWeight());
		}
		// defect 10959
		// Now handled w/ Min/Max weight in CommonFees
		// defect 10959 
		//		if (liGrossWt > MAX_GROSS_WT)
		//			{
		//				laRTSE.addException(
		//					new RTSException(89),
		//					gettxtVehicleEmptyWeight());
		//			}
		//	if (TitleClientUtilityMethods
		//		.isEmptyWtInvalidForRegClassLsEq6000(
		//				liRegCd,
		//				lsEmptyWt))
		//		{
		//					laRTSE.addException(
		//					new RTSException(2),
		//					gettxtVehicleEmptyWeight());
		//		}
		//	if (TitleClientUtilityMethods
		//			.isEmptyWtInvalidForRegClassGrt6000(
		//					liRegCd,
		//					lsEmptyWt))
		//		{
		//					laRTSE.addException(
		//					new RTSException(2),
		//					gettxtVehicleEmptyWeight());
		//		}
		//		else if (
		//					TitleClientUtilityMethods
		//						.isGrossWtInvalidForCombVeh(
		//						liRegCd,
		//						lsGrossWt))
		//				{
		//					displayError(371);
		//					return;
		//				}
		if (caRegClassData != null && caRegClassData.getEmptyWtReqd() == 1)
		{
			try
			{
				TitleClientUtilityMethods.validateGrossWtForRegClassCd(
						caRegData.getRegClassCd(),liGrossWt); 
			}
			catch (RTSException aeRTSEx)
			{
				leRTSEx.addException(aeRTSEx, gettxtVehicleEmptyWeight()); 
			}
		}
		// end defect 10959 
		
		// TRAVEL TRAILER LENGTH
		String lsTrlLen = gettxtTrlrLngth().getText();
		if (gettxtTrlrLngth().isEnabled()
				&& !getchkParkModel().isSelected()
				&& TitleClientUtilityMethods
				.isLengthInvalidForTrvTrlr(
						lsTrlLen))
		{
			// 150 / 203
			int liError = ErrorsConstant. ERR_NUM_TRAVEL_TRAILER_MUST_BE_LT_40_FT;
			if (gettxtTrlrLngth().isEmpty() || 
					Integer.parseInt(gettxtTrlrLngth().getText()) ==0)
			{
				liError = ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID; 
			}

				leRTSEx.addException(
					new RTSException(liError),gettxtTrlrLngth());
		}
		// PCR 41
		String lsTrlWidthFeet = gettxtTlrWidthFeet().getText();
		String lsTrlWidthInch =	gettxtTlrWidthInches().getText();
		lsTrlWidthFeet = UtilityMethods.isEmpty(gettxtTlrWidthFeet().getText()) ? "0" :
			lsTrlWidthFeet;
		lsTrlWidthInch = UtilityMethods.isEmpty(lsTrlWidthInch) ? "0" :
			lsTrlWidthInch; 

		// defect 11049 
		if (gettxtTlrWidthInches().isEnabled()
				&& Integer.parseInt(lsTrlWidthInch) >= INCHES_PER_FOOT)
		{
			// end defect 11049 
			
			// 150 
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtTlrWidthInches());
		}

		int liWidth = Integer.parseInt(lsTrlWidthFeet)* INCHES_PER_FOOT 
			+ Integer.parseInt(lsTrlWidthInch);

		// Very interesting; Error only if both are invalid  (KPH)
		if (gettxtTrlrLngth().isEnabled()
				&& getchkParkModel().isSelected()
				&& TitleClientUtilityMethods
				.isLengthInvalidForParkTlr(
						lsTrlLen)
						&& !TitleClientUtilityMethods.isWidthValidForParkTlr(
								liWidth))
		{
			// 150 
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtTrlrLngth());
		}
		// END PCR 41

		if (caRegClassData != null && TitleClientUtilityMethods
				.isFrmTrlMinWt(caRegClassData, lsGrossWt))
		{
			// 202
			leRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_GROSS_WT_INVALID_FOR_FARM_TRAILER), 
					gettxtVehicleCarryingCapacity());
		}
		
		// defect 11368 
		if (gettxtReplicaVehicleModelYear().isEnabled()
				&& gettxtReplicaVehicleMake().isEnabled())
		{
			if (!gettxtReplicaVehicleModelYear().isEmpty()
					|| !gettxtReplicaVehicleMake().isEmpty())
			{
				if (gettxtReplicaVehicleModelYear().isEmpty()
						|| CommonValidations.isInvalidYearModel(
								gettxtReplicaVehicleModelYear()
								.getText()
						))
				{
					leRTSEx.addException(
							new RTSException(ErrorsConstant.ERR_NUM_MODEL_YEAR_NOT_VALID),
							gettxtReplicaVehicleModelYear());
				}
				if (gettxtReplicaVehicleMake().isEmpty())
				{
					leRTSEx.addException(
							new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtReplicaVehicleMake());
				}
			}
		}
		// end defect 11368 
		
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false; 
		}
		return lbValid;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
