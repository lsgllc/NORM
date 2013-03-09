package com.txdot.isd.rts.client.title.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmSalvageTTL016.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Rajangam				Made changes for validations
 * MAbs			04/18/2002	Global change for startWorking() and 
 *							doneWorking()
 * MAbs			04/19/2002	Owner ID must be exactly 9 digits
 *							CQU100003589
 * TPederson	05/08/2002	Fixed setData so os/2 window won't flash
 *							when error 211 is displayed
 * MAbs			05/16/2002	Added capturing of mailing information 
 * 							CQU100003963
 * MAbs			05/28/2002	Added mail object in VehInqData to store 
 * 							mail info CQU100004122
 * J Rue		04/22/2003	Defect 5939, 5948 Do not perform TIMA if 
 * 							DUPLICATE is selected.
 *							method actionPerformed().
 * J Rue		04/22/2003	Defect 5938, Add dash between ZipCd/ZipCdP4.
 *							create getstcLbldash().
 * J Rue		04/22/2003	Defect 5949, Do not modify TtlIssueDate if 
 * 							DUPLICATE is selected
 *							method actionPerformed().
 * J Rue		04/28/2003	Defect 5959, Default OdomtrBrnd = 'ACTUAL 
 * 							MILEAGE' for OdomtrBrnd = null
 *							and OdomtrBrnd = "". method lostFocus()
 * K Harrell 	05/14/2003	Defect 6111  Remove implementation of 5942 
 * 							(Recoded in Transaction())
 *  & Arredondo
 * J Rue		05/20/2003	Defect 6153, Call Common.isVehModlYr() to 
 * 							validate vehicle year.
 *							method actionPerformed()
 * J Rue		05/20/2003	Defect 6126/6130, Do not perform TIMA if 
 * 							RegClassCd is 0
 *							method checkForTIMA(), checkOdomtrReqd()
 * J Rue		04/28/2003	Defect 5942, JnkCd and JnkDate is reset if 
 * 							duplicate. The code and date should remain 
 * 							unchange.
 *							method: setTransCdJnkCd(), 
 *							setDataToDaaObject, new method 
 *							setCurrSalvData()
 *							** continue defect 5942 **
 *							TransCd = CCOSCT Add method to retain and 
 *							restore salvage data for ESC
 *							new methods saveOrigSalvData(), 
 *							restoreSlvgData()
 *							method actionPerformed() to call 
 *							restoreSlvgData()
 *							method setData() to call saveOrigSalvData()
 * J Rue		05/22/2003	Defect 6164, 6167 - Do not perform 
 * 							OdmtrReadng validation if disabled.
 * J Rue		06/11/2003	Defect 6232, Populate Vehicle year, make and
 * 							model with VINA (if data exist)
 *							for	NO RECORD FOUND. new method 
 *							setVINAData(), update setData()
 * J Rue		06/11/2003	Defect 6233, 6235 - Display user supplied 
 * 							data (Insurance Info) if OwnerId was
 *							entered and data was returned.
 *							new method setUserSupplied(), modify 
 *							setData()
 * J Rue		06/16/2003	Defect 6247 Check for record not Salvage 
 * 							(non-duplicate)
 *							before getting userSupplied data
 * J Rue		06/25/2003	Defect 6104, Had to resize the frame from 
 * 							649/509 to 649/490.
 * J Rue		07/14/2003	Defect 6197, Re-Org the odometer brand and 
 * 							reading setting
 *							method setData(), doRegionalOfcProc(), 
 *							doSlvgScreenNoRec()
 * J Rue		07/14/2003	Defect 6353, Set to - 1 for blank dispaly if
 * 							VehBdyType not found
 *							method populateBodyTypes()
 * J Rue		07/17/2003	Defect 6236, Comment focusListener() out. 
 * 							method getRadioSalvageCertificate().
 * J Rue		07/17/2003	Defect 6339/Release 5.1.4, Use USA check box
 * 							selected to determine the correct 
 *							edits for USA or Country Cntry, Zip Code, 
 *							Zip code P4
 *							Additionally: Defect 6339	Comment out, no 
 *							ZipCdP4 number on Country
 *							method setDataToData().
 *							method actionPerformed()
 * J Rue		07/18/2003	Defect 6176, Ensure CCO duplicate error 
 * 							display if CCOdate+14>currDate
 *							method actionPerformed()
 * J Rue		07/21/2003	Defect 6354, Ensure the mailing address is 
 * 							printed on the receipt.
 *							method actionPerformed
 * J Zwiener	10/30/2003	Defect 6630. On TTL016, make owner state & 
 * 							zip visible when out-of-country.
 *                          method doCntryStateTxtField()
 * J Zwiener	11/12/2003	Defect 6683. Nullpointerexception in method 
 * 							doCntryStateTxtField() 
 * 							method doCntryStateTxtField()
 *                          Release 5.1.5.1
 * B Arredondo	12/10/2003	Modified vehiclemake field to be able to fit
 * 							make names and not cut off with dots.
 *							Modified in visual composition.
 *							Defect 6583. Version 5.1.5.2
 * B Arredondo	12/23/2003	Modified actionPerformed. Added if statement
 * 							that will not allow for tonnage 
 *							entered to be over 25 tons.
 *							Defect 6587. Version 5.1.5.2
 * B Arredondo	01/06/2004	Modified checkOdomtrReqd(). Code from defect
 * 							6126 caused this defect. Commented out code. 
 *							Code in method checkForTIMA for defect 6126 
 *							did not affect this defect. Leaving code in.
 *							Test case that created 6126 was wrong and 
 *							has now be removed due to the fact that is 
 *							was for a Regional office. Mark R. has 
 *							researched the defect and agrees that the 
 *							test case is wrong. 
 *							Cleaned up code for defect 6164. No return.
 *							Defect 6524. Version 5.1.5.2
 * B Arredondo	01/07/2004	Added method windowOpened and implemented 
 * 							WindowListener in class.
 *							Defect 6525. Version 5.1.5.2
 * B Arredondo	01/12/2004	Added code to method checkForTIMA to check 
 * 							if odometer is required. And if required 
 * 							then check for TIMA. If not required don't 
 * 							check. 
 *							Defect 6788. Version 5.1.5.2
 * B Hargrove	05/25/2004	Modified code so that full 9 digits on 
 *							non-USA zipcode (zip + zip4) are maintained.
 *							modify setDataToDataObject()
 *							defect 6911 Ver 5.2.0
 * J Zwiener	05/26/2004	Do not carry forward SurrTtlDate for Salvage
 *							modify setDataToDataObject()
 *							defect 6767 Ver 5.2.0
 * B Hargrove	11/15/2004	Modified code so Owner ID is carried forward 
 *							when Owner ID is entered and no Owner ID
 *							info is found.
 *							see also: client.common.ui.
 *							          VCVinKeySelectionKEY006.
 *							          processData()
 *							modify doCntryStateTxtField()
 *							defect 6689 Ver 5.2.2
 * B Hargrove	12/15/2004	Modified earlier fix. Was not falling thru 
 *							code to set address fields when no record
 *							found.
 *							modify doCntryStateTxtField()
 *							defect 6689 Ver 5.2.2
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Zwiener	04/22/2005	Set RegInvalid to 1 for Salvages
 * 							Also disable Salvage Cert radio button
 * 							modify setDataToDataObject()
 * 							modify getRadioSalvageCertificate()
 * 							modify actionPerformed()
 * 							modify doBtnRecordNotApplicable()
 * 							modify doHQorRegionalOfcProc()
 * 							modify doSlvgScreenNoRec()
 * 							defect 7762 Ver 5.2.3
 * J Rue		04/29/2005	Clean up code
 * 							deprecated setFocusOnCertRdioBtn()
 * 							getSelectedCertRadioBtn()
 * 							defect 7898 Ver 5.2.3
 * J Rue		05/25/2005	Clean up State, ZpCd, ZpCdP4, Cntry, 
 * 							CntryZpCd
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884  Ver 5.2.3                 
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/19/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * T Pederson	10/31/2005	Comment out keyPressed() method entirely. 
 * 							Code not necessary due to 8240.
 * 							defect 7898 Ver 5.2.3
 * J Rue		11/04/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3    
 * T Pederson	12/14/2005	Added a temp fix for the JComboBox problem.
 * 							deprecate odomtrDefaultRecordFound().
 * 							modify disableFields(), populateBodyTypes(), 
 * 							populateVehicleMakes() and  
 * 							setOdometerBrandSelection().
 * 							defect 8479 Ver 5.2.3  
 * T. Pederson	12/30/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * J Zwiener	01/05/2006	Disable and uncheck duplicate checkbox when
 * 							no MV rec.
 * 							modify doSlvgScreenNoRec()
 * 							defect 7762 Ver 5.2.3
 * Jeff S.		01/06/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.  Added checkboxes to
 * 							an RTSButtonGroup to handle arrowing.
 * 							modify initialize(), getpnlGrpIndicators()
 * 							defect 7898 Ver 5.2.3
 * J Zwiener	01/10/2006	OwnerName2 not aligned.
 * 							modify gettxtOwnerName2()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	01/20/2006	Modify test for length of city name against
 * 							PREV_CITY_MAX_LEN vs. CITY_MAX_LEN
 * 							modify checkDocType()
 * 							defect 7898 Ver 5.2.3 
 * J Zwiener	03/15/2006	Allow for Lien entry when no record found
 * 							modify doLienEntryScreen()
 * 							defect 7762 Ver 5.2.3 
 * M Reyes		04/05/2006	Used visual editor to adjust screen layout. 
 * 							modify getstcLblOwnerId(), gettxtOwnerId()
 * 							getlblOwnerId(), getJLabel1(), 
 *   						gettxtOwnerName(), gettxtOwnerName2(),
 * 							getchkUSA1(), gettxtRegPlateNo(),
 * 							getstcLblYearMake(), 
 * 							gettxtVehicleModelYear()
 * 							Removed slash located between year and 
 * 							make field. 
 * 							modify getJPanel2(), getstcLblSlash()  
 *    						defect 8659 Ver 5.2.3
 * J Zwiener	04/28/2006	Do not carry forward Liens for non-salvage
 * 							type MF recs.
 * 							modify doLienEntryScreen()
 * 							defect 8746 Ver 5.2.3	
 * J Zwiener	04/28/2006	Default Duplicate checkbox to checked when
 * 							MF rec's doctype is the same as the doctype
 * 							chosen on the screen
 * 							modify actionPerformed(), 
 *								doHQorRegionalOfcProc()
 * 							defect 8747 Ver 5.2.3
 * K Harrell	05/07/2007	Use SystemProperty.isCounty(), isHQ(), 
 * 							isRegion()
 * 							modify setData() 
 * 							defect 9085 Ver SpecialPlates 		  
 * B Hargrove	04/04/2008	Comment out references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe (see: defect 9557).
 * 							modify setTransCdJnkCd()
 * 							defect 9631 Ver Defect POS A
 * K Harrell	04/16/2008	Reset Owner Retained Legal Restraint for 
 * 							all Salvage transactions. 
 * 							modify setDataToDataObject()
 * 							defect 9634 Ver Defect POS A   
 * K Harrell	05/07/2008	Interface Cleanup 
 * 							defect 9636 Ver Defect POS A 
 * K Harrell	05/18/2008	Add Owner Retained Processing
 * 							add ivjchkOwnerRetained
 * 							add getchkOwnerRetained(),
 * 							  setupOwnerRetainedToDisplay() 
 * 							modify actionPerformed(), getpnlScreenLeft(),
 * 							 handlRecordNotApplicable(), resetScreen(),
 *							 setupOwnerDataToDisplay()
 * 							defect 9637 Ver Defect POS A 
 * K Harrell	05/18/2008	Process Dealer where Dealer Id <= 999
 * 							add populateNewOwnerDataIfDealerId() 
 * 							modify focusLost(), setData()
 * 							defect 9654 Ver Defect POS A 
 * K Harrell	05/28/2008	Add COA processing 
 * 							add ivjradioCOA
 * 							add getradioCOA()
 * 							modify actionPerformed(), 
 * 							  setupDocTypeRadioToDisplay(), 
 * 							  setupTransTypeRadioToDisplay(),
 * 							  setupOwnerRetainedToDisplay(),
 * 							  getFrmSalvageTTL016ContentPane1(), 
 * 							  setTransCdJnkCd()
 * 							defect 9642 Ver Defect POS A 
 * K Harrell	06/05/2008	Reset SalvStateCntry  
 * 							modify setDataToDataObject()
 * 							defect 9692 Ver Defect POS A
 * K Harrell	06/08/2008	Default to EXEMPT odometer per VINA based on  
 * 							TIMA when Corrected or Original
 * 							modify setOdometerBrandSelection()
 * 							defect 9636 Ver Defect POS A
 * K Harrell	06/09/2008	Use TitleConstants for OwnerId Lengths 
 * 							modify focusLost()
 * 							defect 9636 Ver Defect POS A
 * K Harrell	06/09/2008	No longer check for HQ (vs. Region)
 * 							Region can no longer access the event
 * 							modify setupDocTypeRadioToDisplay()
 * 							defect 9701 Ver Defect POS A
 * K Harrell	06/09/2008	Add logic method to reset Lienholder 
 * 							checkbox on COA 
 * 							add resetLienIfCOA(), NO_LIEN_MSG
 * 							modify itemStateChanged(), resetScreen()
 * 							defect 9642 Ver Defect POS A
 * K Harrell	06/12/2008	Disable Flood Damage on Original if 
 * 							previously set. Reset TransCd with each
 * 							radio button selection vs. at end.
 * 							add setTransCd()  
 * 							modify setupChkBoxesToDisplay(), setData(),
 * 							 actionPerformed(), setOdometerBrandSelection(),
 * 							 setTransCd() 
 * 							defect 9636 Ver Defect POS A 
 * K Harrell	06/20/2008	Copy substring of values into Prev Owner 
 * 							Name, Prev Owner City, Prev Owner State to 
 * 							prevent beeps. 
 * 							modify setupOwnerDataToDisplay()
 * 							defect 9271 Ver Defect POS A 
 * K Harrell	06/27/2008	Modify assessment of cbOdmtrReqd on Record
 * 							Found.  Add assessment of current record
 * 							Odometer mileage, brand
 * 							modify setData()
 * 							defect 9636 Ver Defect POS A
 * K Harrell	07/09/2008	Do not carry forward Vehicle Location, 
 * 							Renewal Recipient, Renewal Address info 
 * 							modify setDataToDataObject() 
 * 							defect 9730 Ver MyPlates_POS   
 * K Harrell	07/09/2008	Restrict OwnerName2 length to 30 
 * 							modify gettxtOwnerName2() 
 * 							defect 9733 Ver MyPlates_POS
 * K Harrell	07/09/2008	Populate state from vehicle record if 
 * 							Duplicate Salvage or Non-Repairable 
 * 							add setupOthrStateCntryToDisplay() 
 * 							modify setData()
 * 	       					defect 9734 Ver MyPlates_POS 
 * K Harrell	07/09/2008	Clear Special Plates Regis Data 
 * 							modify setDataToDataObject()
 * 							defect 9738 Ver MyPlates_POS
  * K Harrell	07/09/2008	Do not allow resizable 
 * 							modify initialize()
 * 							defect 9740 Ver MyPlates_POS
 * K Harrell 	07/11/2008	Allow modification on Not Actual Mileage	
 * 							when corrected.
 * 							modify setOdometerBrandSelection()  
 * 							defect 9749 Ver MyPlates_POS 
 * K Harrell	07/11/2008	Correct validation on BodyStyle.  Highlight
 * 							text field when visible and in error.  
 * 							modify validateFields()
 * 							defect 9750 Ver MyPlates_POS 
 * K Harrell	08/07/2008	Reset TtlSignDate for each transaction type
 * 							modify setDataToDataObject()
 * 							defect 9787 Ver MyPlates_POS
 * K Harrell	02/26/2009	Modify screen to increase size of Remarks 
 * 							panel via Visual Composition. Use 
 * 							CommonConstant.FONT_JLIST in Remarks Listing.
 * 							add MAX_INDI_NO_SCROLL
 * 							modify getlstIndiDescription() 
 * 							defect 9971 Ver Defect_POS_E  
 * K Harrell	03/07/2009	Incorporate use of DocTypeConstant
 * 							delete COA_NO_REGIS, COA_REGIS, NON_TITLED
 * 							modify isNonRepairableVehicleTitle(), 
 * 							 isSalvageCertificate(), isValidDocType(), 
 * 							 isSalvageVehicleTitle()
 * 							defect 9971 Ver Defect_POS_E 
 * K Harrell	03/17/2005	Clear PermLienholderx, LienRlseDatax when 
 * 							 reset Lien Data. Assign ETtlCd for 
 * 							 Salvage. 
 * 							add clearExistingLienData()
 * 							modify actionPerformed(), focusLost(),  
 * 							   handleRecordNotApplicable(), 
 * 							   setDataToDataObject()  
 * 							defect 9975 Ver Defect_POS_E 	
 * K Harrell	04/08/2009	Use CommonConstant for field lengths as 
 * 							 available. 
 * 							delete ZIPCD_MAX_LEN, ZIPCDP4_MAX_LEN, 
 * 							 STATE_MAX_LEN, NAME_24_MAX_LEN, 
 * 							 CITY_MAX_LEN, STREET_MAX_LEN,
 * 							 NAME_30_MAX_LEN, YEAR_MAX_LEN,
 * 							 CNTRY_MAX_LEN, CNTRY_ZIPCD_MAX_LEN,
 * 							 ODOMTR_MAX_LEN, PREV_CITY_MAX_LEN,
 * 							 REGPLTNO_MAX_LEN, EMPTY_WT_MAX_LEN, 
 * 							 MAKE_MAX_LEN,MODEL_MAX_LEN
 *							add LENGTH_PREV_OWNR_CITY, LENGTH_REGPLTNO,
 *							 LENGTH_ODOMTR, LENGTH_PREV_OWNR_NAME,
 *							 LENGTH_EMPTY_WT, LENGTH_MAKE, LENGTH_MODEL,
 *							 LENGTH_TONNAGE
 *							defect 9971 Ver Defect_POS_E
 * K Harrell	06/09/2009	Throw error if OwnerId = 0; Use Error 
 * 							Constants.
 * 							add validateOwnerInfo() 
 * 							modify displayMsgIfCC0LE14Days(),
 *							 focusLost(), isValidDocType(),
 *							 validateOdometer(), validateOwnerInfo(),
 *							 validateVehicleInfo(), validateFields()
 *							defect 10003 Ver Defect_POS_F
 * K Harrell	07/02/2009	Implement new DealerData, new OwnerData
 * 							modify getNewOwnerData(), setData(), 
 * 							 populateNewOwnerDataIfDealerId(), 
 *  						 setDataToDataObject(), 
 * 							 setupOwnerDataToDisplay(), 
 * 							defect 10112 Ver Defect_POS_F     
 * K Harrell	07/18/2009	Cleanup for consistency between TTL007 and 
 * 							REG033. Implement new validation of Country 
 * 							field. Add Input types.
 * 							add valididateOwnrId(), validatePrevOwnrInfo()  
 * 							delete validateOwnerInfo()
 * 							delete LENGTH_REGPLTNO, LENGTH_PREV_OWNR_CITY,
 * 							 LENGTH_PREV_OWNR_NAME
 * 							add caNameAddrComp, cvIndicator
 * 							delete setNonUSAAddressDisplay(), 
 * 							 setUSAAddressDisplay(), caIndicator  
 * 							modify focusLost(), gettxtOwnerName1(), 
 * 							 gettxtOwnerName2(),gettxtOwnerState(), 
 * 						  	 gettxtOwnerStreet1(), gettxtOwnerStreet2(),
 * 							 gettxtPreviousOwnerName(), 
 * 							 gettxtPreviousOwnerCity(), 
 * 							 gettxtRegPlateNo(), handleRecordNotApplicable(), 
 * 							 itemStateChanged(), setDataToDataObject(), 
 * 							 setupOwnerDataToDisplay(),  
 * 							 validateOwnerId(), validateVehicleInfo()
 * 							modify initialize(), itemStateChanged(), 
 * 							  refreshScreen(), setData(), 
 * 							  setDataToDataObject(), validateFields() 
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	08/25/2009	Reset VTRTTLEMRGCD1 for new Salvage, NRCOT, 
 * 							COA. 
 * 							modify setDataToData()
 * 							defect 10153 Ver Defect_POS_F  
 * K Harrell	10/14/2009	Add code for ManufacturerBuyback 
 * 							add MANUFACTURER_BUYBACK 
 * 							add ivjchkManufacturerBuyback, get method 
 * 							modify handleRecordNotApplicable(), 
 * 							 setupChkBoxesToDisplay(), 
 * 							 setupVehDataToDisplay()
 * 							defect 10252 Ver Defect_POS_G
 * K Harrell	10/14/2009	Reset FloodDamage on Duplicate 
 * 							modify setupVehDataToDisplay() 
 * 							defect 10206 Ver Defect_POS_G 
 * K Harrell	12/23/2009	'Lost' Original MFVehicleData in TitleValidObj
 * 							when reset radio for TransCd selection. 
 * 							DocNo not written to Trans, written to 
 * 							MvFuncTrans.
 * 							modify setData()
 * 							defect 10073 Defect_POS_H 
 * K Harrell	01/08/2009  Implement character validation for MF
 * 							add cvAddlMFValid
 *  						modify initialize(), validateFields()
 * 							defect 10299 Ver Defect_POS_H
 * K Harrell	02/09/2010	TitleData VTRTtlEmrgCd1 refactored to 
 * 							  PvtLawEnfVehCd
 * 							modify setDataToDataObject()
 * 							defect 10366 Ver POS_640  
 * Min Wang 	03/12/2010	Remove privacy act options from the screen.
 * 							delete BOTH, COMMERCIAL, INDIVIDUAL, NONE,
 * 							SEL_PRIVACY_ACT_OPT, getpnlPrivacyOpt(),
 * 							getradioBoth(), getradioCommercial(),
 * 							getradioIndividual(), getradioNone(),
 * 							setupPrivacyOptionsToDisplay()
 * 							modify getpnlTransCdType(), SetData()
 * 							defect 10159 Ver POS_640  
 * K Harrell	03/22/2010	Reset Recipient EMail Address 
 * 							modify setDataToDataObject() 
 * 							defect 10372 Ver POS_640 
 * K Harrell	10/04/2010	ButtonPanel1 was overlapping Record Not 
 * 							Applicable button 
 * 							modify via Visual Editor
 * 							defect 10592 Ver 6.6.0  
 * K Harrell	10/06/2010	Arrow key movement through checkboxes 	
 * 							missed Manufacturer BuyBack
 * 							modify getpnlGrpIndicators() 
 * 							defect 10592 Ver 6.6.0 
 * K Harrell	10/22/2010	add displayError(), verifyCCOIssueDate() 
 * 							delete displayMsgIfCC0LE14Days()
 *							modify actionPerformed() 
 * 							defect 10639 Ver 6.6.0 
 * T Pederson	01/31/2011	Added vehicle color drop down boxes 
 * 							add ivjcomboMajorColor, ivjcomboMinorColor,
 * 							ivjstcLblColorMajor, ivjstcLblColorMinor,
 * 							MAJORCOLOR, MINORCOLOR, INT_ZERO
 * 							add getcomboMajorColor(), getcomboMinorColor(),
 * 							populateMajorColor(), populateMinorColor()
 * 							getMajorColorCdSelected(),
 * 							getMinorColorCdSelected(),
 * 							isMajorColorReqdButNotSelected(),
 * 							isMinorColorSelectedMajorColorNotSelected()
 * 							modify setupVehDataToDisplay(),
 * 							setDataToDataObject(), validateVehicleInfo()
 * 							defect 10689 Ver 6.7.0
 * K Harrell	10/27/2011	reset ETtlPrntDate if new Salvage/NRCOT,COA 
 * 							modify setDataToDataObject()
 * 							defect 10841 Ver 6.9.0 
 * K Harrell	11/17/2011	Ensure at least 2 characters for State/Cntry
 * 							delete cbTonnageReqd 
 * 							modify validateFields(),setData()  
 * 							defect 11004 Ver 6.9.0
 * B Woodson	02/10/2012	modify actionPerformed() 
 * 							defect 11228 Ver 6.10.0
* K Harrell	05/25/2012	Add fields/logic for Replica Year/Make
 * 							Alignment work w/ Visual Editor. Move
 * 							Record Not Applicable to Screen Left Bottom
 * 							add ivjstcLblReplicaYearMake,
 * 							 ivjtxtReplicaVehicleModelYear,
 * 							 ivjtxtReplicaVehicleMake, get methods
 * 							add REPLICA_YEAR_MAKE
 * 							add getEmptyStringIfZero()
 * 							modify getpnlScreenRight(),
 * 							 getbtnRecordNotApplicable(), 
 * 							 validateVehicleInfo(),setDataToDataObject(),
 * 							 handleRecordNotApplicable(),
 * 							 setupVehDataToDisplay()  
 * 							defect 11368 Ver 6.10.1   
 * ---------------------------------------------------------------------
 */
/**
 * This class is used to issue certificates for Original, Corrected and
 * Duplicate Salvage Vehicle and Non-Repairable Vehicle Title.   
 * With POS Defect A, it has been enhanced to include Original COA.
 * 
 * @version	6.10.1  		05/25/2012
 * @author	Todd Pederson
 * @author  K Harrell	
 * <br>Creation Date:		06/29/2001 11:20:50
 */

public class FrmSalvageTTL016
	extends RTSDialogBox
	implements ActionListener, FocusListener, ItemListener
{
	private RTSButton ivjbtnRecordNotApplicable = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkDOTStandards = null;
	private JCheckBox ivjchkFloodDamage = null;
	private JCheckBox ivjchkManufacturerBuyback = null;
	private JCheckBox ivjchkOwnerRetained = null;
	private JCheckBox ivjchkRecordDeleteLien = null;
	private JCheckBox ivjchkUSA = null;
	private JComboBox ivjcomboBodyType = null;
	// defect 10689 
	private JComboBox ivjcomboMajorColor = null;
	private JComboBox ivjcomboMinorColor = null;
	// end defect 10689 
	private JComboBox ivjcomboVehicleMake = null;
	private JComboBox ivjcomboVehicleOdometerBrand = null;
	private JPanel ivjFrmSalvageTTL016ContentPane1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblDocNo = null;
	private JLabel ivjlblDocTypeCodeDesc = null;
	private JLabel ivjlblRegClass = null;
	private JLabel ivjlblRTSIssueDate = null;
	private JLabel ivjlblVehClass = null;
	private JLabel ivjlblVIN = null;
	private JList ivjlstIndiDescription = null;  
	private JPanel ivjpanelScreenRight = null;
	private JPanel ivjpnlGrpIndicators = null;
	private JPanel ivjpnlScreenLeft = null;
	private JPanel ivjpnlTransCdType = null;
	private JRadioButton ivjradioCOA = null;
	private JRadioButton ivjradioCorrected = null;
	private JRadioButton ivjradioDuplicate = null;
	private JRadioButton ivjRadioNonRepairable = null;
	private JRadioButton ivjradioOriginal = null;
	private JRadioButton ivjradioSalvageVehicleTitle = null;
	private JLabel ivjstcLblBrand = null;
	// defect 10689 
	private JLabel ivjstcLblColorMajor = null;
	private JLabel ivjstcLblColorMinor = null;
	// end defect 10689 
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblDocumentNo = null;
	private JLabel ivjstcLblEmptyWeight = null;
	private JLabel ivjstcLblMailingAddress = null;
	private JLabel ivjstcLblModel = null;
	private JLabel ivjstcLblOdometer = null;
	private JLabel ivjstcLblOwnerId = null;
	private JLabel ivjstcLblOwnerName = null;
	private JLabel ivjstcLblPlateNo = null;
	private JLabel ivjstcLblPreviousOwnerName = null;
	private JLabel ivjstclblRegClass = null;
	private JLabel ivjstcLblState = null;
	private JLabel ivjstcLblTitleIssueDate = null;
	private JLabel ivjstcLblTonnage = null;
	private JLabel ivjstclblVehClass = null;
	private JLabel ivjstcLblVehicleBodyType = null;
	private JLabel ivjstcLblVIN = null;
	private JLabel ivjstcLblYearMake = null;
	private RTSInputField ivjtxtOdometerReading = null;
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
	private RTSInputField ivjtxtRegPlateNo = null;
	private RTSInputField ivjtxtState = null;
	private RTSInputField ivjtxtVehicleBodyType = null;
	private RTSInputField ivjtxtVehicleEmptyWeight = null;
	private RTSInputField ivjtxtVehicleMake = null;
	private RTSInputField ivjtxtVehicleModel = null;
	private RTSInputField ivjtxtVehicleModelYear = null;
	private RTSInputField ivjtxtVehicleTonnage = null;
	private RTSButtonGroup privacyRecordTypeGroup =
		new RTSButtonGroup();
	private RTSButtonGroup salvageRadioGroup = new RTSButtonGroup();

	// defect 10127
	private Vector cvIndicator = null;
	private NameAddressComponent caNameAddrComp = null;
	// end defect 10127 

	// defect 10299 
	private static Vector cvAddlMFValid = new Vector();
	// end defect 10299 

	private OwnerData caNewOwnerData = null;
	private OwnerData caOwnerData = null;
	private RegistrationData caRegData = null;
	private TitleData caTitleData = null;
	private VehicleData caVehData = null;
	private VehicleInquiryData caVehInqData = null;
	private VehicleInquiryData caVehInqDataOrig = null;

	// boolean 
	private boolean cbAlreadySet = false;
	private boolean cbMFRecordFound = false;
	private boolean cbNewOwner = false;
	private boolean cbOdmtrReqd = false;
	private boolean cbOdometerReset = false;
	private boolean cbRcdNotAppl = false;
	private boolean cbSetDataFinished = false;
	// defect 11004 
	//private boolean cbTonnageReqd = false;
	// end defect 11004 

	// int 
	private int ciActionType = 0;
	private int ciCurrSalvData = 0;
	private int ciOrigDocTypeCd = 0;
	private int ciRecordTypeCd = 0;

	// String 
	private String csTransCd = TransCdConstant.SCOT;

	// Vectors
	private Vector cvOrigSlvgData = new Vector();
	// defect 10689 
	private Vector cvVehColor = new Vector();
	// end defect 10689 


	// Constants  
	private final static String A = "A";
	private final static String BODY_STYLE = "Body Style:";
	// defect 10159
	//private final static String BOTH = "Both";
	// end defect 10159
	private final static String BRAND = "Brand:";
	private final static String CHK_IF_APPLICABLE =
		"Check if applicable:";
	private final static String COA = "COA";
	private final static int COMBO_NOT_SELECTED = -1;
	// defect 10159
	//private final static String COMMERCIAL = "Commercial";
	// end defect 10159
	private final static int CORRECTED = 2;
	private final static String DATE_LAYOUT = "  /  /    ";
	private final static int DOCTYPE = 1;
	private final static String DOCUMENT_NO = "Document No:";
	private final static String DOT_PROOF_REQUIRED =
		"DOT Proof Required";
	private final static int DUPLICATE = 3;
	private final static String EMPTY_WEIGHT = "Empty Weight:";
	private final static String EXEMPT = CommonConstant.STR_EXEMPT;
	private final static String FLOOD_DAMAGE = "Flood Damage";
	private final static String INDIVIDUAL = "Individual";
	private final static String INFORMATION = "Information";
	// defect 10689 
	private final static int INT_ZERO = 0;
	// end defect 10689 
	private final static String ISSUED = "Issued:";
	private final static int LENGTH_EMPTY_WT = 6;
	private final static int LENGTH_MAKE = 4;
	private final static int LENGTH_MODEL = 3;
	private final static int LENGTH_ODOMTR = 6;
	private final static int LENGTH_TONNAGE = 5;
	private final static String MAILING_ADDRESS = "Mailing Address";
	// defect 10689 
	private static final String MAJORCOLOR = "Major Color:";
	private static final String MINORCOLOR = "Minor Color:";
	// end defect 10689 
	private final static int MAX_INDI_NO_SCROLL = 5;
	// defect 10252
	private final static String MANUFACTURER_BUYBACK =
		"Manufacturer Buyback";
	// end defect 10252 
	private final static String MODEL = "Model:";
	private final static String N = "N";
	private final static String NEW = "- NEW -";
	private final static String NO_LIEN_MSG =
		"LIENHOLDER DATA WILL NOT BE TRANSFERRED "
			+ " WITH THIS TRANSACTION.";
	// defect 10159
	//private final static String NONE = "None";
	// end defect 10159
	private final static String ODOMETER_READING = "Odometer Reading:";
	private final static int ORIGINAL = 1;
	private final static String OWNER_ID = "Owner Id:";
	private final static String OWNER_NAME = "Owner Name";
	private final static String OWNER_RETAINED = "Owner Retained";
	private final static String PLATE_NO = "Plate No:";
	private final static String PREVIOUS_OWNER = "Previous Owner";
	private final static String REC_NOT_APPLICABE =
		"Record Not Applicable";
	private final static String RECORD_DELETE_LIEN =
		"Record/Delete Lien";
	private final static int RECORDTYPE = 2;
	private final static String REG_CLASS = "Reg Class:";
	// defect 10159
	//private final static String SEL_PRIVACY_ACT_OPT =
	//	"Select privacy act option:";
	// end defect 10159
	private final static String SELECT_ONE = "Select One:";
	private final static String STATE_COUNTRY = "State/Country: ";
	private final static String STR_NRVT =
		"Non-Repairable Vehicle Title";
	private final static String STR_SVT = "Salvage Vehicle Title";
	private final static String TONNAGE = "Tonnage:";
	private final static String TX = CommonConstant.STR_TX;
	private final static String USA = "USA";
	private final static String VEH_CLASS = " Vehicle Class:";
	private final static String VIN = "VIN:";
	private final static String X = "X";
	private final static String YEAR_MAKE = " Year/Make:";
	private final static double ZEROTON =
		Double.parseDouble(CommonConstant.STR_ZERO_DOLLAR);
	
	// defect 11368 
	private JLabel ivjstcLblReplicaYearMake = null;
	private RTSInputField ivjtxtReplicaVehicleModelYear = null;
	private RTSInputField ivjtxtReplicaVehicleMake = null;
	private final static String REPLICA_YEAR_MAKE = "Replica Year/Make:";
	// end defect 11368 



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
			FrmSalvageTTL016 laFrmSalvageTTL016;
			laFrmSalvageTTL016 = new FrmSalvageTTL016();
			laFrmSalvageTTL016.setModal(true);
			laFrmSalvageTTL016
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent laRTSDBox)
				{
					System.exit(0);
				}
			});
			laFrmSalvageTTL016.show();
			java.awt.Insets insets = laFrmSalvageTTL016.getInsets();
			laFrmSalvageTTL016.setSize(
				laFrmSalvageTTL016.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSalvageTTL016.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSalvageTTL016.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmSalvageTTL016 constructor comment.
	 */
	public FrmSalvageTTL016()
	{
		super();
		initialize();
	}

	/**
	 * FrmSalvageTTL016 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSalvageTTL016(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmSalvageTTL016 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSalvageTTL016(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}

		try
		{
			clearAllColor(this);

			// defect 9642 
			// add COA 
			// If select SVT / NRT / COA
			if (aaAE.getSource().equals(getradioSalvageVehicleTitle())
				|| (aaAE.getSource().equals(getradioNonRepair()))
				|| (aaAE.getSource().equals(getradioCOA())))
			{
				ciActionType = DOCTYPE;
				resetScreen(aaAE);
				setTransCd();
			}
			// end defect 9642 
			else if (aaAE.getSource().equals(getradioDuplicate()))
			{
				ciActionType = RECORDTYPE;
				ciRecordTypeCd = DUPLICATE;
				setData(UtilityMethods.copy(caVehInqDataOrig));
				setTransCd();
			}
			else if (aaAE.getSource().equals(getradioCorrected()))
			{
				ciActionType = RECORDTYPE;
				ciRecordTypeCd = CORRECTED;
				setData(UtilityMethods.copy(caVehInqDataOrig));
				setTransCd();
			}
			else if (aaAE.getSource().equals(getradioOriginal()))
			{
				ciActionType = RECORDTYPE;
				ciRecordTypeCd = ORIGINAL;
				boolean lbNRT = getradioNonRepair().isSelected();
				boolean lbSVT =
					getradioSalvageVehicleTitle().isSelected();
				setData(UtilityMethods.copy(caVehInqDataOrig));
				getradioNonRepair().setSelected(lbNRT);
				getradioSalvageVehicleTitle().setSelected(lbSVT);
				setTransCd();
			}
			// defect 9637 
			else if (aaAE.getSource().equals(getchkOwnerRetained()))
			{
				cbSetDataFinished = false;
				setupOwnerDataToDisplay();
			}
			// end defect 9637 

			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				
				//defect 11228
				if (caVehInqData
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
				
				if (isValidDocType() && validateFields())
				{
					// defect 10639 
					verifyCCOIssueDate();
					// end defect 10639 

					VehicleInquiryData laNewVehInqData =
						(VehicleInquiryData) UtilityMethods.copy(
							caVehInqData);
					setTransCdJnkCd(laNewVehInqData);
					setDataToDataObject(laNewVehInqData);

					if (!isVTRAuthOKOnHardStop(laNewVehInqData))
					{
						return;
					}

					// Present Mailing Screen if Duplicate
					if (isDuplicate())
					{
						laNewVehInqData.setSalvageLienIndi(
							isLienEntryScreenApplicable());

						getController().processData(
							VCSalvageTTL016.MAILING,
							laNewVehInqData);
					}

					if (laNewVehInqData.shouldGoPrevious())
					{
						return;
					}

					// defect 5949
					// Do not update TtlIssueDate if duplicate
					if (!isDuplicate())
					{
						laNewVehInqData
							.getMfVehicleData()
							.getTitleData()
							.setTtlIssueDate(
							RTSDate.getCurrentDate().getYYYYMMDDDate());
					}
					// end defect 5949

					if (isLienEntryScreenApplicable())
					{
						getController().processData(
							VCSalvageTTL016.LIEN_ENTRY,
							laNewVehInqData);

						if (laNewVehInqData.shouldGoPrevious())
						{
							return;
						}
					}
					else
					{
						// defect 9975 
						clearExistingLienData(
							laNewVehInqData
								.getMfVehicleData()
								.getTitleData());
						// end defect 9975 
					}

					// save data and prepare to write to DB
					CompleteTitleTransaction laTtlTrans =
						new CompleteTitleTransaction(
							laNewVehInqData,
							csTransCd);

					CompleteTransactionData laCompTransData = null;

					try
					{
						laCompTransData =
							laTtlTrans.doCompleteTransaction();

						// defect 6323, 6354 
						// save Mailing address info
						if (csTransCd.equals(TransCdConstant.CCOSCT)
							|| csTransCd.equals(TransCdConstant.CCONRT))
						{
							laCompTransData.setMailingAddrData(
								laNewVehInqData.getMailingAddress());
						}
						// end defect 6323, 6354

						getController().setTransCode(csTransCd);
						getController().processData(
							AbstractViewController.ENTER,
							laCompTransData);
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(this);
					}
				}
			}

			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{

				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());

			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.TTL016);
			}
			else if (aaAE.getSource() == getbtnRecordNotApplicable())
			{
				handleRecordNotApplicable();
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/** 
	 * Clear Existing Lien Data 
	 * 
	 * @param aaTitleData
	 */
	private void clearExistingLienData(TitleData aaTitleData)
	{
		aaTitleData.clearLien1Data();
		aaTitleData.clearLien2Data();
		aaTitleData.clearLien3Data();
	}

	/**
	 * Displays the error message
	 * 
	 * @param aiCode int
	 */
	private void displayError(int aiCode)
	{
		RTSException leRTSEx = new RTSException(aiCode);
		leRTSEx.displayError(this);
	}
	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{

	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aeFE)
	{
		if (!aeFE.isTemporary()
			&& aeFE.getSource() == gettxtOdometerReading())
		{
			cbOdometerReset = true;
			String lsOdoRdg = gettxtOdometerReading().getText().trim();
			if (lsOdoRdg.equalsIgnoreCase(EXEMPT))
			{
				getstcLblBrand().setEnabled(false);
				getcomboVehicleOdometerBrand().setFocusable(false);
				getcomboVehicleOdometerBrand().setEnabled(false);
				getcomboVehicleOdometerBrand().removeAllItems();
			}
			else
			{
				try
				{
					getcomboVehicleOdometerBrand().setFocusable(true);

					String lsOdoBrnd =
						caVehInqData
							.getMfVehicleData()
							.getVehicleData()
							.getVehOdmtrBrnd();

					if (lsOdoBrnd == null
						|| lsOdoBrnd == CommonConstant.STR_SPACE_EMPTY)
					{
						caVehData.setVehOdmtrBrnd(A);
					}

					if (getcomboVehicleOdometerBrand()
						.getSelectedItem()
						== null
						|| getcomboVehicleOdometerBrand()
							.getSelectedItem()
							.equals(
							CommonConstant.STR_SPACE_EMPTY))
					{
						setOdometerBrandSelection();
					}
				}
				catch (NumberFormatException aeNFE)
				{
					// Empty catch block
				}
			}
			cbOdometerReset = false;
		}
		// defect 9654 
		else if (
			!aeFE.isTemporary()
				&& aeFE.getSource() == gettxtOwnerId()
				&& !aeFE.getOppositeComponent().equals(null)
				&& !aeFE.getOppositeComponent().equals(
					getButtonPanel1().getBtnCancel()))
		{
			cbSetDataFinished = false;
			clearAllColor(this);

			// defect 10127 
			if (!gettxtOwnerId().isEmpty())
			{
				String lsOwnerId = gettxtOwnerId().getText();
				// end defect 10127

				int liOwnerId = Integer.parseInt(lsOwnerId);

				// defect 10003 
				if (liOwnerId == 0)
				{
					RTSException leRTSEx = new RTSException();
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtOwnerId());
					leRTSEx.displayError(this);
				}
				else if (liOwnerId <= TitleConstant.MAX_POS_DEALERID)
				{
					// defect 9975 
					// Have Lookup behave same as TTL001 - Salvage 
					//  Lienholder Lookup 
					lsOwnerId =
						UtilityMethods.removeLeadingZeros(lsOwnerId);
					gettxtOwnerId().setText(lsOwnerId);
					if (lsOwnerId.length() > 0)
					{
						populateNewOwnerDataIfDealerId(lsOwnerId);
					}
					// end defect 9975 
				}
				else if (
					lsOwnerId.length()
						< TitleConstant.REQD_MF_OWNERID_LENGTH)
				{
					RTSException leRTSEx = new RTSException();
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtOwnerId());
					leRTSEx.displayError(this);
				}
				// defect 10127 
			}
			// end defect 10127 

			cbSetDataFinished = true;
		}
		// end defect 9654 
	}

	/**
	 * Return the btnRecordNotApplicable property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnRecordNotApplicable()
	{
		if (ivjbtnRecordNotApplicable == null)
		{
			try
			{
				ivjbtnRecordNotApplicable = new RTSButton();
				ivjbtnRecordNotApplicable.setSize(161, 25);
				ivjbtnRecordNotApplicable.setName(
					"btnRecordNotApplicable");
				ivjbtnRecordNotApplicable.setMnemonic(KeyEvent.VK_R);
				ivjbtnRecordNotApplicable.setText(REC_NOT_APPLICABE);
				ivjbtnRecordNotApplicable.setMaximumSize(
					new java.awt.Dimension(159, 25));
				ivjbtnRecordNotApplicable.setActionCommand(
					REC_NOT_APPLICABE);
				ivjbtnRecordNotApplicable.setMinimumSize(
					new java.awt.Dimension(159, 25));
				// user code begin {1}
				// defect 11368 
				ivjbtnRecordNotApplicable.setLocation(52, 490);
				// end defect 11368 
				ivjbtnRecordNotApplicable.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnRecordNotApplicable;
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setSize(267, 31);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
				ivjButtonPanel1.setLocation(221, 512);
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
	 * Return the chkDOTStandards property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkDOTStandards()
	{
		if (ivjchkDOTStandards == null)
		{
			try
			{
				ivjchkDOTStandards = new JCheckBox();
				ivjchkDOTStandards.setBounds(6, 52, 137, 22);
				ivjchkDOTStandards.setName("chkDOTStandards");
				ivjchkDOTStandards.setText(DOT_PROOF_REQUIRED);
				ivjchkDOTStandards.setMaximumSize(
					new java.awt.Dimension(137, 22));
				ivjchkDOTStandards.setActionCommand(DOT_PROOF_REQUIRED);
				ivjchkDOTStandards.setMinimumSize(
					new java.awt.Dimension(137, 22));
				// user code begin {1}
				ivjchkDOTStandards.setMnemonic(KeyEvent.VK_T);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkDOTStandards;
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
				ivjchkFloodDamage.setBounds(148, 23, 125, 22);
				ivjchkFloodDamage.setName("chkFloodDamage");
				ivjchkFloodDamage.setText(FLOOD_DAMAGE);
				ivjchkFloodDamage.setMaximumSize(
					new java.awt.Dimension(105, 22));
				ivjchkFloodDamage.setActionCommand(FLOOD_DAMAGE);
				ivjchkFloodDamage.setMinimumSize(
					new java.awt.Dimension(105, 22));
				// user code begin {1}
				ivjchkFloodDamage.setMnemonic(KeyEvent.VK_F);
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
	 * This method initializes ivjchkManufacturerBuyback
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkManufacturerBuyback()
	{
		if (ivjchkManufacturerBuyback == null)
		{
			ivjchkManufacturerBuyback = new JCheckBox();
			ivjchkManufacturerBuyback.setBounds(148, 53, 159, 21);
			ivjchkManufacturerBuyback.setMnemonic(KeyEvent.VK_B);
			ivjchkManufacturerBuyback.setText(MANUFACTURER_BUYBACK);
		}
		return ivjchkManufacturerBuyback;
	}

	/**
	 * This method initializes ivjchkOwnerRetained
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkOwnerRetained()
	{
		if (ivjchkOwnerRetained == null)
		{
			ivjchkOwnerRetained = new JCheckBox();
			ivjchkOwnerRetained.setBounds(188, 23, 117, 21);
			ivjchkOwnerRetained.setText(OWNER_RETAINED);
			// user code begin {1}
			ivjchkOwnerRetained.setMnemonic(KeyEvent.VK_E);
			ivjchkOwnerRetained.addActionListener(this);
			// user code end
		}
		return ivjchkOwnerRetained;
	}

	/**
	 * Return the chkRecordDeleteLien property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkRecordDeleteLien()
	{
		if (ivjchkRecordDeleteLien == null)
		{
			try
			{
				ivjchkRecordDeleteLien = new JCheckBox();
				ivjchkRecordDeleteLien.setBounds(6, 23, 132, 22);
				ivjchkRecordDeleteLien.setName("chkRecordDeleteLien");
				ivjchkRecordDeleteLien.setText(RECORD_DELETE_LIEN);
				ivjchkRecordDeleteLien.setMaximumSize(
					new java.awt.Dimension(132, 22));
				ivjchkRecordDeleteLien.setActionCommand(
					RECORD_DELETE_LIEN);
				ivjchkRecordDeleteLien.setMinimumSize(
					new java.awt.Dimension(132, 22));
				// user code begin {1}
				ivjchkRecordDeleteLien.addItemListener(this);
				ivjchkRecordDeleteLien.setMnemonic(KeyEvent.VK_L);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkRecordDeleteLien;
	}

	/**
	 * Return the chkUSA1 property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkUSA()
	{
		if (ivjchkUSA == null)
		{
			try
			{
				ivjchkUSA = new JCheckBox();
				ivjchkUSA.setText(USA);
				ivjchkUSA.setBounds(250, 94, 55, 21);
				ivjchkUSA.setName("ivjchkUSA");
				ivjchkUSA.setActionCommand(USA);
				ivjchkUSA.setRequestFocusEnabled(true);
				// user code begin {1}
				ivjchkUSA.setMnemonic(KeyEvent.VK_U);
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
	 * Return the comboBodyType property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboBodyType()
	{
		if (ivjcomboBodyType == null)
		{
			try
			{
				ivjcomboBodyType = new JComboBox();
				ivjcomboBodyType.setSize(182, 20);
				ivjcomboBodyType.setName("comboBodyType");
				ivjcomboBodyType.setBackground(java.awt.Color.white);
				ivjcomboBodyType.setLocation(120, 140);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboBodyType;
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
				ivjcomboMajorColor =
					new javax.swing.JComboBox();
				ivjcomboMajorColor.setBounds(120, 182, 178, 20);
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
				ivjcomboMajorColor.setBackground(
					java.awt.Color.white);
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
				ivjcomboMinorColor =
					new javax.swing.JComboBox();
				ivjcomboMinorColor.setBounds(120, 203, 178, 20);
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
				ivjcomboMinorColor.setBackground(
					java.awt.Color.white);
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
	 * Return the comboVehicleMake property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboVehicleMake()
	{
		if (ivjcomboVehicleMake == null)
		{
			try
			{
				ivjcomboVehicleMake = new JComboBox();
				ivjcomboVehicleMake.setSize(175, 20);
				ivjcomboVehicleMake.setName("comboVehicleMake");
				ivjcomboVehicleMake.setBackground(java.awt.Color.white);
				// user code begin {1}
				ivjcomboVehicleMake.setLocation(160, 118);
				ivjcomboVehicleMake.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboVehicleMake;
	}

	/**
	 * Return the comboVehicleOdometerBrand property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboVehicleOdometerBrand()
	{
		if (ivjcomboVehicleOdometerBrand == null)
		{
			try
			{
				ivjcomboVehicleOdometerBrand = new JComboBox();
				ivjcomboVehicleOdometerBrand.setSize(230, 20);
				ivjcomboVehicleOdometerBrand.setName(
					"comboVehicleOdometerBrand");
				ivjcomboVehicleOdometerBrand.setBackground(
					java.awt.Color.white);
				ivjcomboVehicleOdometerBrand.setLocation(72, 212);
				populateBrand();
				// user code begin {1} 
				ivjcomboVehicleOdometerBrand.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboVehicleOdometerBrand;
	}

	/**
	 * Return DocTypeDesc.
	 * 
	 * @return String
	 */
	private String getDocTypeDesc()
	{
		String lsRtn = CommonConstant.STR_SPACE_EMPTY;
		DocumentTypesData laDocTypeData =
			DocumentTypesCache.getDocType(ciOrigDocTypeCd);
		if (laDocTypeData != null)
		{
			lsRtn = laDocTypeData.getDocTypeCdDesc();
		}
		return lsRtn;
	}
	
	/**
	 * Return Empty String if Zero 
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
	 * Return the FrmSalvageTTL016ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSalvageTTL016ContentPane1()
	{
		if (ivjFrmSalvageTTL016ContentPane1 == null)
		{
			try
			{
				ivjFrmSalvageTTL016ContentPane1 = new JPanel();
				ivjFrmSalvageTTL016ContentPane1.setName(
					"FrmSalvageTTL016ContentPane1");
				ivjFrmSalvageTTL016ContentPane1.setLayout(null);
				ivjFrmSalvageTTL016ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmSalvageTTL016ContentPane1.setMinimumSize(
					new java.awt.Dimension(0, 0));
				getFrmSalvageTTL016ContentPane1().add(
					getpnlScreenLeft(),
					getpnlScreenLeft().getName());
				getFrmSalvageTTL016ContentPane1().add(
					getpnlScreenRight(),
					getpnlScreenRight().getName());
				getFrmSalvageTTL016ContentPane1().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
				ivjFrmSalvageTTL016ContentPane1.add(
					getradioSalvageVehicleTitle(),
					null);
				ivjFrmSalvageTTL016ContentPane1.add(
					getradioNonRepair(),
					null);
				// defect 9642 
				ivjFrmSalvageTTL016ContentPane1.add(
					getradioCOA(),
					null);
				// end defect 9642 
				ivjFrmSalvageTTL016ContentPane1.add(
					getButtonPanel1(),
					null);
				ivjFrmSalvageTTL016ContentPane1.add(
					getbtnRecordNotApplicable(),
					null);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmSalvageTTL016ContentPane1;
	}

	/**
	 * Return the JScrollPane1 property value.
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
				ivjJScrollPane1.setSize(286, 97);
				ivjJScrollPane1.setLocation(new Point(336, 412));
				ivjJScrollPane1.setLocation(336, 412);
				getJScrollPane1().setViewportView(
						getlstIndiDescription());
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
	 * Return the lblDocNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblDocNo()
	{
		if (ivjlblDocNo == null)
		{
			try
			{
				ivjlblDocNo = new JLabel();
				ivjlblDocNo.setSize(144, 19);
				ivjlblDocNo.setMaximumSize(
					new java.awt.Dimension(119, 14));
				ivjlblDocNo.setMinimumSize(
					new java.awt.Dimension(119, 14));
				ivjlblDocNo.setHorizontalAlignment(2);
				ivjlblDocNo.setLocation(123, 315);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblDocNo;
	}

	/**
	 * Return the lblDocTypeCodeDesc property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblDocTypeCodeDesc()
	{
		if (ivjlblDocTypeCodeDesc == null)
		{
			try
			{
				ivjlblDocTypeCodeDesc = new JLabel();
				ivjlblDocTypeCodeDesc.setSize(253, 16);
				ivjlblDocTypeCodeDesc.setMaximumSize(
					new java.awt.Dimension(74, 14));
				ivjlblDocTypeCodeDesc.setMinimumSize(
					new java.awt.Dimension(74, 14));
				ivjlblDocTypeCodeDesc.setHorizontalAlignment(
					SwingConstants.CENTER);
				ivjlblDocTypeCodeDesc.setLocation(28, 337);
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
		return ivjlblDocTypeCodeDesc;
	}

	/**
	 * This method initializes ivjlblRegClass
	 * 
	 * @return JLabel
	 */
	private JLabel getlblRegClass()
	{
		if (ivjlblRegClass == null)
		{
			ivjlblRegClass = new JLabel();
			ivjlblRegClass.setSize(214, 18);
			ivjlblRegClass.setText(CommonConstant.STR_SPACE_EMPTY);
			// user code begin {1}
			ivjlblRegClass.setLocation(120, 97);
			// user code end 
		}
		return ivjlblRegClass;
	}

	/**
	 * Return the rtsIssueDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblRTSIssueDate()
	{
		if (ivjlblRTSIssueDate == null)
		{
			try
			{
				ivjlblRTSIssueDate = new JLabel();
				ivjlblRTSIssueDate.setSize(74, 19);
				ivjlblRTSIssueDate.setEnabled(true);
				// user code begin {1}
				ivjlblRTSIssueDate.setLocation(125, 356);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblRTSIssueDate;
	}

	/**
	 * This method initializes ivjlblVehClass
	 * 
	 * @return JLabel
	 */
	private JLabel getlblVehClass()
	{
		if (ivjlblVehClass == null)
		{
			ivjlblVehClass = new JLabel();
			ivjlblVehClass.setSize(214, 18);
			ivjlblVehClass.setText(CommonConstant.STR_SPACE_EMPTY);
			// user code begin {1}
			ivjlblVehClass.setLocation(120, 75);
			// user code end 
		}
		return ivjlblVehClass;
	}

	/**
	 * Return the lblVIN property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblVIN()
	{
		if (ivjlblVIN == null)
		{
			try
			{
				ivjlblVIN = new JLabel();
				ivjlblVIN.setSize(213, 18);
				ivjlblVIN.setText("");
				ivjlblVIN.setMaximumSize(
					new java.awt.Dimension(131, 14));
				ivjlblVIN.setMinimumSize(
					new java.awt.Dimension(131, 14));
				ivjlblVIN.setLocation(120, 225);
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
		return ivjlblVIN;
	}

	/**
	 * Return the lstIndiDescription property value.
	 * 
	 * @return JList
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JList getlstIndiDescription()
	{
		if (ivjlstIndiDescription == null)
		{
			try
			{
				ivjlstIndiDescription = new JList();
				// defect 9971 
				ivjlstIndiDescription.setFont(
					new java.awt.Font(
						CommonConstant.FONT_JLIST,
						0,
						12));
				// end defect 9971 
				ivjlstIndiDescription.setSize(new Dimension(285, 99));
				// user code begin{1} 
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
	 * Create new OwnerData, default TX to state
	 * 
	 *@return OwnerData 
	 */
	private OwnerData getNewOwnerData()
	{
		OwnerData laNewOwner = new OwnerData();
		// defect 10112 
		// Assigned in OwnerData constructor 
		// laNewOwner.setAddressData(new AddressData());
		// end defect 10112 
		laNewOwner.getAddressData().setState(TX);
		return laNewOwner;
	}

	/**
	 * Return the pnlGrpIndicators property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlGrpIndicators()
	{
		if (ivjpnlGrpIndicators == null)
		{
			try
			{
				ivjpnlGrpIndicators = new JPanel();
				ivjpnlGrpIndicators.setBorder(new EtchedBorder());
				ivjpnlGrpIndicators.setLayout(null);
				ivjpnlGrpIndicators.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlGrpIndicators.setMinimumSize(
					new java.awt.Dimension(313, 72));
				ivjpnlGrpIndicators.add(getchkRecordDeleteLien(), null);
				ivjpnlGrpIndicators.add(getchkFloodDamage(), null);
				ivjpnlGrpIndicators.add(getchkDOTStandards(), null);
				ivjpnlGrpIndicators.add(
					getchkManufacturerBuyback(),
					null);
				ivjpnlGrpIndicators.setBounds(6, 352, 313, 92);
				// user code begin {1} 
				Border b =
					new TitledBorder(
						new EtchedBorder(),
						CHK_IF_APPLICABLE);
				ivjpnlGrpIndicators.setBorder(b);
				RTSButtonGroup laRTSBtnGrp = new RTSButtonGroup();
				laRTSBtnGrp.add(getchkRecordDeleteLien());
				laRTSBtnGrp.add(getchkFloodDamage());
				laRTSBtnGrp.add(getchkDOTStandards());
				// defect 10592 
				laRTSBtnGrp.add(getchkManufacturerBuyback());
				// end defect 10592 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlGrpIndicators;
	}

	/**
	 * Return the pnlScreenLeft property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlScreenLeft()
	{
		if (ivjpnlScreenLeft == null)
		{
			try
			{
				ivjpnlScreenLeft = new JPanel();
				ivjpnlScreenLeft.setLayout(null);
				ivjpnlScreenLeft.setBounds(5, 32, 325, 452);
				getpnlScreenLeft().add(
					getstcLblOwnerId(),
					getstcLblOwnerId().getName());
				getpnlScreenLeft().add(
					getstcLblOwnerName(),
					getstcLblOwnerName().getName());
				getpnlScreenLeft().add(
					gettxtOwnerName1(),
					gettxtOwnerName1().getName());
				getpnlScreenLeft().add(
					gettxtOwnerName2(),
					gettxtOwnerName2().getName());
				getpnlScreenLeft().add(
					getstcLblMailingAddress(),
					getstcLblMailingAddress().getName());
				getpnlScreenLeft().add(
					getchkUSA(),
					getchkUSA().getName());
				getpnlScreenLeft().add(
					gettxtOwnerStreet1(),
					gettxtOwnerStreet1().getName());
				getpnlScreenLeft().add(
					gettxtOwnerStreet2(),
					gettxtOwnerStreet2().getName());
				getpnlScreenLeft().add(
					gettxtOwnerCity(),
					gettxtOwnerCity().getName());
				getpnlScreenLeft().add(
					getstcLblOdometer(),
					getstcLblOdometer().getName());
				getpnlScreenLeft().add(
					gettxtOwnerState(),
					gettxtOwnerState().getName());
				getpnlScreenLeft().add(
					gettxtOwnerZpcd(),
					gettxtOwnerZpcd().getName());
				getpnlScreenLeft().add(
					gettxtOwnerZpcdP4(),
					gettxtOwnerZpcdP4().getName());
				getpnlScreenLeft().add(
					gettxtOwnerCntry(),
					gettxtOwnerCntry().getName());
				getpnlScreenLeft().add(
					gettxtOwnerCntryZpcd(),
					gettxtOwnerCntryZpcd().getName());
				getpnlScreenLeft().add(
					gettxtOdometerReading(),
					gettxtOdometerReading().getName());
				getpnlScreenLeft().add(
					getstcLblBrand(),
					getstcLblBrand().getName());
				getpnlScreenLeft().add(
					getcomboVehicleOdometerBrand(),
					getcomboVehicleOdometerBrand().getName());
				getpnlScreenLeft().add(
					getstcLblState(),
					getstcLblState().getName());
				getpnlScreenLeft().add(
					gettxtOwnerId(),
					gettxtOwnerId().getName());
				getpnlScreenLeft().add(
					gettxtState(),
					gettxtState().getName());
				getpnlScreenLeft().add(
					gettxtOwnerId(),
					gettxtOwnerId().getName());
				getpnlScreenLeft().add(
					getstcLblDash(),
					getstcLblDash().getName());
				// user code begin {1}
				ivjpnlScreenLeft.add(getpnlTransCdType(), null);
				ivjpnlScreenLeft.add(getpnlGrpIndicators(), null);
				ivjpnlScreenLeft.add(getchkOwnerRetained(), null);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlScreenLeft;
	}

	/**
	 * Return the ivjpanelScreenRight property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlScreenRight()
	{
		if (ivjpanelScreenRight == null)
		{
			try
			{
				// defect 11368 
				ivjstcLblReplicaYearMake = new JLabel();
				ivjstcLblReplicaYearMake.setText(REPLICA_YEAR_MAKE);
				ivjstcLblReplicaYearMake.setSize(new Dimension(108, 19));
				ivjstcLblReplicaYearMake.setLocation(new Point(4, 290));
				// end defect 11368 
				
				ivjpanelScreenRight = new JPanel();
				ivjpanelScreenRight.setLayout(null);
				ivjpanelScreenRight.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpanelScreenRight.setMinimumSize(
					new java.awt.Dimension(303, 273));
				getpnlScreenRight().add(
					gettxtPreviousOwnerName(),
					gettxtPreviousOwnerName().getName());
				getpnlScreenRight().add(
					gettxtPreviousOwnerCity(),
					gettxtPreviousOwnerCity().getName());
				getpnlScreenRight().add(
					gettxtPreviousOwnerState(),
					gettxtPreviousOwnerState().getName());
				getpnlScreenRight().add(
					getstcLblVehicleBodyType(),
					getstcLblVehicleBodyType().getName());
				getpnlScreenRight().add(
					gettxtVehicleBodyType(),
					gettxtVehicleBodyType().getName());
				getpnlScreenRight().add(
					getstcLblModel(),
					getstcLblModel().getName());
				getpnlScreenRight().add(
					getstcLblVIN(),
					getstcLblVIN().getName());
				getpnlScreenRight().add(
					getlblVIN(),
					getlblVIN().getName());
				getpnlScreenRight().add(
					getstcLblPlateNo(),
					getstcLblPlateNo().getName());
				getpnlScreenRight().add(
					getstcLblEmptyWeight(),
					getstcLblEmptyWeight().getName());
				getpnlScreenRight().add(
					getstcLblTonnage(),
					getstcLblTonnage().getName());
				getpnlScreenRight().add(
					getstcLblDocumentNo(),
					getstcLblDocumentNo().getName());
				getpnlScreenRight().add(
					getlblDocNo(),
					getlblDocNo().getName());
				getpnlScreenRight().add(
					getlblDocTypeCodeDesc(),
					getlblDocTypeCodeDesc().getName());
				getpnlScreenRight().add(
					getstcLblTitleIssueDate(),
					getstcLblTitleIssueDate().getName());
				getpnlScreenRight().add(
					getstcLblPreviousOwnerName(),
					getstcLblPreviousOwnerName().getName());
				getpnlScreenRight().add(
					getlblRTSIssueDate(),
					getlblRTSIssueDate().getName());
				getpnlScreenRight().add(
					getcomboBodyType(),
					getcomboBodyType().getName());
				getpnlScreenRight().add(
					gettxtVehicleModel(),
					gettxtVehicleModel().getName());
				getpnlScreenRight().add(
					gettxtRegPlateNo(),
					gettxtRegPlateNo().getName());
				getpnlScreenRight().add(
					gettxtVehicleEmptyWeight(),
					gettxtVehicleEmptyWeight().getName());
				getpnlScreenRight().add(
					gettxtVehicleTonnage(),
					gettxtVehicleTonnage().getName());
				// user code begin {1}
				ivjpanelScreenRight.add(getcomboVehicleMake(), null);
				ivjpanelScreenRight.add(getstcLblYearMake(), null);
				ivjpanelScreenRight.add(gettxtVehicleModelYear(), null);
				ivjpanelScreenRight.add(gettxtVehicleMake(), null);
				ivjpanelScreenRight.add(getstcLblVehClass(), null);
				ivjpanelScreenRight.add(getstcLblRegClass(), null);
				ivjpanelScreenRight.add(getlblVehClass(), null);
				ivjpanelScreenRight.add(getlblRegClass(), null);
				// user code end
				ivjpanelScreenRight.add(getcomboMinorColor(), null);
				ivjpanelScreenRight.add(getcomboMajorColor(), null);
				ivjpanelScreenRight.add(getstcLblColorMajor(), null);
				ivjpanelScreenRight.add(getstcLblColorMinor(), null);
				ivjpanelScreenRight.setSize(339, 379);
				ivjpanelScreenRight.setLocation(331, 30);
				// defect 11368 
				ivjpanelScreenRight.add(ivjstcLblReplicaYearMake, null);
				ivjpanelScreenRight.add(gettxtReplicaVehicleModelYear(), null);
				ivjpanelScreenRight.add(gettxtReplicaVehicleMake(), null);
				// end defect 11368
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpanelScreenRight;
	}

	/**
	 * Return the ivjpnlTransCdType property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlTransCdType()
	{
		if (ivjpnlTransCdType == null)
		{
			try
			{
				ivjpnlTransCdType = new JPanel();
				ivjpnlTransCdType.setName("ivjpnlTransCdType");
				ivjpnlTransCdType.setOpaque(false);
				ivjpnlTransCdType.setBorder(new EtchedBorder());
				ivjpnlTransCdType.setLayout(null);
				ivjpnlTransCdType.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlTransCdType.setMinimumSize(
					new java.awt.Dimension(252, 73));
				ivjpnlTransCdType.setEnabled(true);

				ivjpnlTransCdType.add(getradioOriginal(), null);
				ivjpnlTransCdType.add(getradioCorrected(), null);
				ivjpnlTransCdType.add(getradioDuplicate(), null);
				// defect 10159
				//ivjpnlTransCdType.setBounds(6, 343, 313, 65);
				ivjpnlTransCdType.setBounds(5, 277, 313, 65);
				// end defect 10159
				// user code begin {1}
				Border b =
					new TitledBorder(new EtchedBorder(), SELECT_ONE);
				ivjpnlTransCdType.setBorder(b);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlTransCdType;
	}

	/**
	 * This method initializes ivjradioCOA
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioCOA()
	{
		if (ivjradioCOA == null)
		{
			ivjradioCOA = new JRadioButton();
			ivjradioCOA.setBounds(507, 8, 108, 21);
			ivjradioCOA.setText(COA);
			salvageRadioGroup.add(ivjradioCOA);
			// user code begin {1}
			ivjradioCOA.setMnemonic(KeyEvent.VK_A);
			ivjradioCOA.addActionListener(this);
			// user code end
		}
		return ivjradioCOA;
	}

	/**
	 * Return the ivjradioCorrected property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioCorrected()
	{
		if (ivjradioCorrected == null)
		{
			try
			{
				ivjradioCorrected = new JRadioButton();
				ivjradioCorrected.setBounds(91, 24, 108, 20);
				ivjradioCorrected.setText("Corrected");
				ivjradioCorrected.setMaximumSize(
					new java.awt.Dimension(78, 22));
				ivjradioCorrected.setActionCommand(INDIVIDUAL);
				ivjradioCorrected.setEnabled(true);
				ivjradioCorrected.setMinimumSize(
					new java.awt.Dimension(78, 22));
				// user code begin {1}
				ivjradioCorrected.addActionListener(this);
				ivjradioCorrected.setMnemonic(KeyEvent.VK_C);
				privacyRecordTypeGroup.add(ivjradioCorrected);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioCorrected;
	}

	/**
	 * Return the ivjradioDuplicate property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioDuplicate()
	{
		if (ivjradioDuplicate == null)
		{
			try
			{
				ivjradioDuplicate = new JRadioButton();
				ivjradioDuplicate.setSize(91, 20);
				ivjradioDuplicate.setName("ivjradioDuplicate");
				ivjradioDuplicate.setText("Duplicate");
				ivjradioDuplicate.setMaximumSize(
					new java.awt.Dimension(94, 22));
				ivjradioDuplicate.setEnabled(true);
				ivjradioDuplicate.setMinimumSize(
					new java.awt.Dimension(94, 22));
				ivjradioDuplicate.setLocation(206, 24);
				// user code begin {1}
				ivjradioDuplicate.setMnemonic(KeyEvent.VK_D);
				ivjradioDuplicate.addActionListener(this);
				privacyRecordTypeGroup.add(ivjradioDuplicate);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioDuplicate;
	}

	/**
	 * Return the ivjRadioNonRepairable property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioNonRepair()
	{
		if (ivjRadioNonRepairable == null)
		{
			try
			{
				ivjRadioNonRepairable = new JRadioButton();
				ivjRadioNonRepairable.setBounds(235, 9, 226, 20);
				ivjRadioNonRepairable.setName("RadioNonRepair");
				ivjRadioNonRepairable.setMnemonic(KeyEvent.VK_N);
				ivjRadioNonRepairable.setText(STR_NRVT);
				salvageRadioGroup.add(ivjRadioNonRepairable);
				// user code begin {1}
				ivjRadioNonRepairable.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjRadioNonRepairable;
	}

	/**
	 * Return the ivjradioOriginal property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioOriginal()
	{
		if (ivjradioOriginal == null)
		{
			try
			{
				ivjradioOriginal = new JRadioButton();
				ivjradioOriginal.setBounds(4, 24, 82, 20);
				ivjradioOriginal.setText("Original");
				ivjradioOriginal.setMaximumSize(
					new java.awt.Dimension(54, 22));
				ivjradioOriginal.setEnabled(true);
				ivjradioOriginal.setMinimumSize(
					new java.awt.Dimension(54, 22));
				// user code begin {1}
				ivjradioOriginal.setMnemonic(KeyEvent.VK_O);
				ivjradioOriginal.addActionListener(this);
				privacyRecordTypeGroup.add(ivjradioOriginal);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioOriginal;
	}

	/**
	 * Return the ivjradioSalvageVehicleTitle property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioSalvageVehicleTitle()
	{
		if (ivjradioSalvageVehicleTitle == null)
		{
			try
			{
				ivjradioSalvageVehicleTitle = new JRadioButton();
				ivjradioSalvageVehicleTitle.setBounds(2, 9, 191, 20);
				ivjradioSalvageVehicleTitle.setText(STR_SVT);
				ivjradioSalvageVehicleTitle.setMaximumSize(
					new java.awt.Dimension(175, 22));
				ivjradioSalvageVehicleTitle.setMinimumSize(
					new java.awt.Dimension(175, 22));
				// user code begin {1}
				salvageRadioGroup.add(ivjradioSalvageVehicleTitle);
				ivjradioSalvageVehicleTitle.setMnemonic(KeyEvent.VK_V);
				ivjradioSalvageVehicleTitle.addActionListener(this);

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioSalvageVehicleTitle;
	}

	/**
	 * Return the ivjstcLblBrand property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblBrand()
	{
		if (ivjstcLblBrand == null)
		{
			try
			{
				ivjstcLblBrand = new JLabel();
				ivjstcLblBrand.setSize(45, 20);
				ivjstcLblBrand.setText(BRAND);
				ivjstcLblBrand.setLocation(8, 212);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblBrand;
	}

	/**
	 * Return the stcLblDash property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDash()
	{
		if (ivjstcLblDash == null)
		{
			try
			{
				ivjstcLblDash = new JLabel();
				ivjstcLblDash.setText(CommonConstant.STR_DASH);
				ivjstcLblDash.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setMinimumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setBounds(267, 168, 8, 14);
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
		return ivjstcLblDash;
	}

	/**
	 * Return the stcLblDocumentNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDocumentNo()
	{
		if (ivjstcLblDocumentNo == null)
		{
			try
			{
				ivjstcLblDocumentNo = new JLabel();
				ivjstcLblDocumentNo.setSize(79, 19);
				ivjstcLblDocumentNo.setText(DOCUMENT_NO);
				ivjstcLblDocumentNo.setMaximumSize(
					new java.awt.Dimension(79, 14));
				ivjstcLblDocumentNo.setMinimumSize(
					new java.awt.Dimension(79, 14));
				ivjstcLblDocumentNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblDocumentNo.setLocation(36, 315);
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEmptyWeight()
	{
		if (ivjstcLblEmptyWeight == null)
		{
			try
			{
				ivjstcLblEmptyWeight = new JLabel();
				ivjstcLblEmptyWeight.setSize(81, 19);
				ivjstcLblEmptyWeight.setText(EMPTY_WEIGHT);
				ivjstcLblEmptyWeight.setMaximumSize(
					new java.awt.Dimension(81, 14));
				ivjstcLblEmptyWeight.setMinimumSize(
					new java.awt.Dimension(81, 14));
				ivjstcLblEmptyWeight.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblEmptyWeight.setLocation(31, 267);
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
	 * Return the ivjstcLblMailingAddress property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblMailingAddress()
	{
		if (ivjstcLblMailingAddress == null)
		{
			try
			{
				ivjstcLblMailingAddress = new JLabel();
				ivjstcLblMailingAddress.setSize(106, 20);
				ivjstcLblMailingAddress.setText(MAILING_ADDRESS);
				ivjstcLblMailingAddress.setLocation(8, 95);
				// user code begin {1}
				ivjstcLblMailingAddress.setLabelFor(
					gettxtOwnerStreet1());
				ivjstcLblMailingAddress.setDisplayedMnemonic(
					KeyEvent.VK_G);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblMailingAddress;
	}

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
				ivjstcLblColorMajor.setBounds(35, 186, 77, 14);
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
				ivjstcLblColorMinor.setBounds(35, 207, 77, 14);
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

	/**
	 * Return the stcLblModel property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblModel()
	{
		if (ivjstcLblModel == null)
		{
			try
			{
				ivjstcLblModel = new JLabel();
				ivjstcLblModel.setSize(37, 19);
				ivjstcLblModel.setText(MODEL);
				ivjstcLblModel.setMaximumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblModel.setMinimumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblModel.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblModel.setLocation(75, 162);
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
		return ivjstcLblModel;
	}

	/**
	 * Return the stcLblOdometer property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblOdometer()
	{
		if (ivjstcLblOdometer == null)
		{
			try
			{
				ivjstcLblOdometer = new JLabel();
				ivjstcLblOdometer.setName("stcLblOdometer");
				ivjstcLblOdometer.setLabelFor(gettxtOdometerReading());
				ivjstcLblOdometer.setSize(114, 20);
				ivjstcLblOdometer.setText(ODOMETER_READING);
				ivjstcLblOdometer.setLocation(7, 189);
				// user code begin {1}
				ivjstcLblOdometer.setDisplayedMnemonic(KeyEvent.VK_M);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblOdometer;
	}

	/**
	 * Return the ivjstcLblOwnerId property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblOwnerId()
	{
		if (ivjstcLblOwnerId == null)
		{
			try
			{
				ivjstcLblOwnerId = new JLabel();
				ivjstcLblOwnerId.setSize(66, 20);
				ivjstcLblOwnerId.setText(OWNER_ID);
				ivjstcLblOwnerId.setLocation(8, 5);
				// user code begin {1}
				ivjstcLblOwnerId.setLabelFor(gettxtOwnerId());
				ivjstcLblOwnerId.setDisplayedMnemonic(KeyEvent.VK_I);
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblOwnerName()
	{
		if (ivjstcLblOwnerName == null)
		{
			try
			{
				ivjstcLblOwnerName = new JLabel();
				ivjstcLblOwnerName.setSize(86, 20);
				ivjstcLblOwnerName.setText(OWNER_NAME);
				ivjstcLblOwnerName.setLocation(8, 26);
				// user code begin {1} 
				ivjstcLblOwnerName.setLabelFor(gettxtOwnerName1());
				ivjstcLblOwnerName.setDisplayedMnemonic(KeyEvent.VK_W);
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
	 * Return the ivjstcLblPlateNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblPlateNo()
	{
		if (ivjstcLblPlateNo == null)
		{
			try
			{
				ivjstcLblPlateNo = new JLabel();
				ivjstcLblPlateNo.setSize(50, 19);
				ivjstcLblPlateNo.setText(PLATE_NO);
				ivjstcLblPlateNo.setMaximumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblPlateNo.setMinimumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblPlateNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblPlateNo.setLocation(62, 246);
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
		return ivjstcLblPlateNo;
	}

	/**
	 * Return the ivjstcLblPreviousOwnerName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblPreviousOwnerName()
	{
		if (ivjstcLblPreviousOwnerName == null)
		{
			try
			{
				ivjstcLblPreviousOwnerName = new JLabel();
				ivjstcLblPreviousOwnerName.setText(PREVIOUS_OWNER);
				ivjstcLblPreviousOwnerName.setDisplayedMnemonic(
					KeyEvent.VK_P);
				ivjstcLblPreviousOwnerName.setMaximumSize(
					new java.awt.Dimension(91, 14));
				ivjstcLblPreviousOwnerName.setLabelFor(
					gettxtPreviousOwnerName());
				ivjstcLblPreviousOwnerName.setSize(109, 19);
				ivjstcLblPreviousOwnerName.setMinimumSize(
					new java.awt.Dimension(91, 14));
				ivjstcLblPreviousOwnerName.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjstcLblPreviousOwnerName.setLocation(8, 7);
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
		return ivjstcLblPreviousOwnerName;
	}

	/**
	 * This method initializes ivjstclblRegClass
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegClass()
	{
		if (ivjstclblRegClass == null)
		{
			ivjstclblRegClass = new JLabel();
			ivjstclblRegClass.setSize(60, 18);
			ivjstclblRegClass.setText(REG_CLASS);
			ivjstclblRegClass.setLocation(52, 97);
			// user code begin {1}
			// user code end 
		}
		return ivjstclblRegClass;
	}

	/**
	 * Return the ivjstcLblState property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblState()
	{
		if (ivjstcLblState == null)
		{
			try
			{
				ivjstcLblState = new JLabel();
				ivjstcLblState.setSize(84, 20);
				ivjstcLblState.setText(STATE_COUNTRY);
				ivjstcLblState.setLocation(8, 235);
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
		return ivjstcLblState;
	}

	/**
	 * Return the ivjstcLblTitleIssueDate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblTitleIssueDate()
	{
		if (ivjstcLblTitleIssueDate == null)
		{
			try
			{
				ivjstcLblTitleIssueDate = new JLabel();
				ivjstcLblTitleIssueDate.setSize(52, 19);
				ivjstcLblTitleIssueDate.setText(ISSUED);
				ivjstcLblTitleIssueDate.setMaximumSize(
					new java.awt.Dimension(74, 14));
				ivjstcLblTitleIssueDate.setAlignmentX(1.0F);
				ivjstcLblTitleIssueDate.setMinimumSize(
					new java.awt.Dimension(74, 14));
				ivjstcLblTitleIssueDate.setHorizontalAlignment(4);
				ivjstcLblTitleIssueDate.setLocation(63, 355);
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
		return ivjstcLblTitleIssueDate;
	}

	/**
	 * Return the ivjstcLblTonnage property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblTonnage()
	{
		if (ivjstcLblTonnage == null)
		{
			try
			{
				ivjstcLblTonnage = new JLabel();
				ivjstcLblTonnage.setSize(55, 19);
				ivjstcLblTonnage.setText(TONNAGE);
				ivjstcLblTonnage.setMaximumSize(
					new java.awt.Dimension(52, 14));
				ivjstcLblTonnage.setMinimumSize(
					new java.awt.Dimension(52, 14));
				ivjstcLblTonnage.setHorizontalAlignment(4);
				ivjstcLblTonnage.setLocation(174, 267);
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
	 * This method initializes ivjstclblVehClass
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVehClass()
	{
		if (ivjstclblVehClass == null)
		{
			ivjstclblVehClass = new JLabel();
			ivjstclblVehClass.setSize(83, 19);
			ivjstclblVehClass.setText(VEH_CLASS);
			ivjstclblVehClass.setLocation(29, 75);
			// user code begin {1}
			// user code end 
		}
		return ivjstclblVehClass;
	}

	/**
	 * Return the ivjstcLblVehicleBodyType property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblVehicleBodyType()
	{
		if (ivjstcLblVehicleBodyType == null)
		{
			try
			{
				ivjstcLblVehicleBodyType = new JLabel();
				ivjstcLblVehicleBodyType.setSize(62, 20);
				ivjstcLblVehicleBodyType.setText(BODY_STYLE);
				ivjstcLblVehicleBodyType.setMaximumSize(
					new java.awt.Dimension(62, 14));
				ivjstcLblVehicleBodyType.setMinimumSize(
					new java.awt.Dimension(62, 14));
				ivjstcLblVehicleBodyType.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblVehicleBodyType.setLocation(50, 140);
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
		return ivjstcLblVehicleBodyType;
	}

	/**
	 * Return the ivjstcLblVIN property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblVIN()
	{
		if (ivjstcLblVIN == null)
		{
			try
			{
				ivjstcLblVIN = new JLabel();
				ivjstcLblVIN.setSize(22, 18);
				ivjstcLblVIN.setText(VIN);
				ivjstcLblVIN.setMaximumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVIN.setMinimumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVIN.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblVIN.setLocation(90, 225);
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
	 * Return the ivjstcLblYearMake property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblYearMake()
	{
		if (ivjstcLblYearMake == null)
		{
			try
			{
				ivjstcLblYearMake = new JLabel();
				ivjstcLblYearMake.setSize(66, 20);
				ivjstcLblYearMake.setText(YEAR_MAKE);
				ivjstcLblYearMake.setMaximumSize(
					new java.awt.Dimension(63, 14));
				ivjstcLblYearMake.setMinimumSize(
					new java.awt.Dimension(63, 14));
				ivjstcLblYearMake.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjstcLblYearMake.setLocation(46, 118);
				// user code begin {1}
				ivjstcLblYearMake.setLabelFor(gettxtVehicleModelYear());
				ivjstcLblYearMake.setDisplayedMnemonic(KeyEvent.VK_Y);
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
	 * Return the ivjtxtOdometerReading property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtOdometerReading()
	{
		if (ivjtxtOdometerReading == null)
		{
			try
			{
				ivjtxtOdometerReading = new RTSInputField();
				ivjtxtOdometerReading.setBounds(129, 189, 70, 20);
				// user code begin {1}
				ivjtxtOdometerReading.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtOdometerReading.setMaxLength(LENGTH_ODOMTR);
				ivjtxtOdometerReading.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtOdometerReading;
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
				ivjtxtOwnerCity.setSize(170, 20);
				ivjtxtOwnerCity.setName("ivjtxtOwnerCity");
				// user code begin {1}
				ivjtxtOwnerCity.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
				ivjtxtOwnerCity.setLocation(8, 166);
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
				ivjtxtOwnerCntry.setName("ivjtxtOwnerCntry");
				ivjtxtOwnerCntry.setBounds(183, 166, 45, 20);
				// user code begin {1}
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
				ivjtxtOwnerCntryZpcd.setBounds(233, 166, 73, 20);
				// user code begin {1}
				ivjtxtOwnerCntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtOwnerCntryZpcd.setMaxLength(
					CommonConstant.LENGTH_CNTRY_ZIP);
				// user code end
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtOwnerId()
	{
		if (ivjtxtOwnerId == null)
		{
			try
			{
				ivjtxtOwnerId = new RTSInputField();
				ivjtxtOwnerId.setBounds(79, 5, 73, 20);
				ivjtxtOwnerId.setVisible(true);
				// user code begin {1}
				ivjtxtOwnerId.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerId.setMaxLength(
					TitleConstant.REQD_MF_OWNERID_LENGTH);
				ivjtxtOwnerId.addFocusListener(this);
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
				ivjtxtOwnerName1.setSize(297, 20);
				ivjtxtOwnerName1.setLocation(8, 48);
				// user code begin {1}
				// defect 10127 
				ivjtxtOwnerName1.setInput(RTSInputField.DEFAULT);
				// end defect 10127  
				ivjtxtOwnerName1.setMaxLength(
					CommonConstant.LENGTH_NAME);
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
				ivjtxtOwnerName2.setSize(297, 20);
				ivjtxtOwnerName2.setName("ivjtxtOwnerName2");
				ivjtxtOwnerName2.setLocation(8, 70);
				// user code begin {1}
				// defect 10127 
				ivjtxtOwnerName2.setInput(RTSInputField.DEFAULT);
				// end defect 10127  
				// defect 9733 
				ivjtxtOwnerName2.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// end defect 9733
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtOwnerState()
	{
		if (ivjtxtOwnerState == null)
		{
			try
			{
				ivjtxtOwnerState = new RTSInputField();
				ivjtxtOwnerState.setBounds(185, 166, 30, 20);
				// user code begin {1}
				// defect 10127 
				ivjtxtOwnerState.setInput(RTSInputField.ALPHA_ONLY);
				// end defect 10127  
				ivjtxtOwnerState.setMaxLength(
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
		return ivjtxtOwnerState;
	}

	/**
	 * Return the ivjtxtOwnerStreet1 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtOwnerStreet1()
	{
		if (ivjtxtOwnerStreet1 == null)
		{
			try
			{
				ivjtxtOwnerStreet1 = new RTSInputField();
				ivjtxtOwnerStreet1.setName("ivjtxtOwnerStreet1");
				ivjtxtOwnerStreet1.setSize(297, 20);
				ivjtxtOwnerStreet1.setLocation(8, 118);
				ivjtxtOwnerStreet1.setRequestFocusEnabled(true);
				// user code begin {1}
				// defect 10127 
				ivjtxtOwnerStreet1.setInput(RTSInputField.DEFAULT);
				// end defect 10127  
				ivjtxtOwnerStreet1.setMaxLength(
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
		return ivjtxtOwnerStreet1;
	}

	/**
	 * Return the ivjtxtOwnerStreet2 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtOwnerStreet2()
	{
		if (ivjtxtOwnerStreet2 == null)
		{
			try
			{
				ivjtxtOwnerStreet2 = new RTSInputField();
				ivjtxtOwnerStreet2.setSize(297, 20);
				ivjtxtOwnerStreet2.setLocation(8, 142);
				// user code begin {1}
				// defect 10127 
				ivjtxtOwnerStreet2.setInput(RTSInputField.DEFAULT);
				// end defect 10127  
				ivjtxtOwnerStreet2.setMaxLength(
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
				ivjtxtOwnerZpcd.setBounds(224, 166, 41, 20);
				// user code begin {1}
				ivjtxtOwnerZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				ivjtxtOwnerZpcd.setText(CommonConstant.STR_SPACE_EMPTY);
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
				ivjtxtOwnerZpcdP4.setBounds(273, 166, 33, 20);
				// user code begin {1}
				ivjtxtOwnerZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				ivjtxtOwnerZpcdP4.setText(
					CommonConstant.STR_SPACE_EMPTY);
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
				ivjtxtPreviousOwnerCity.setName("txtPreviousOwnerCity");
				ivjtxtPreviousOwnerCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtPreviousOwnerCity.setBounds(8, 51, 124, 20);
				// user code begin {1}
				ivjtxtPreviousOwnerCity.setInput(RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtPreviousOwnerCity.setMaxLength(
					CommonConstant.LENGTH_PREV_OWNR_CITY);
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
		return ivjtxtPreviousOwnerCity;
	}

	/**
	 * Return the ivjtxtPreviousOwnerName property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtPreviousOwnerName()
	{
		if (ivjtxtPreviousOwnerName == null)
		{
			try
			{
				ivjtxtPreviousOwnerName = new RTSInputField();
				ivjtxtPreviousOwnerName.setSize(232, 20);
				ivjtxtPreviousOwnerName.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtPreviousOwnerName.setLocation(8, 29);
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
				ivjtxtPreviousOwnerState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtPreviousOwnerState.setBounds(140, 51, 30, 20);
				// user code begin {1}
				ivjtxtPreviousOwnerState.setInput(
					RTSInputField.ALPHA_ONLY);
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
	 * Return the ivjtxtRegPlateNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRegPlateNo()
	{
		if (ivjtxtRegPlateNo == null)
		{
			try
			{
				ivjtxtRegPlateNo = new RTSInputField();
				ivjtxtRegPlateNo.setSize(64, 19);
				ivjtxtRegPlateNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// defect 10127 
				ivjtxtRegPlateNo.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtRegPlateNo.setMaxLength(
					CommonConstant.LENGTH_PLTNO);
				// end defect 10127
				// user code end
				ivjtxtRegPlateNo.setLocation(120, 246);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtRegPlateNo;
	}
	
	/**
	 * This method initializes ivjtxtReplicaVehicleMake	
	 * 	
	 * @return RTSInputField	
	 */
	private RTSInputField gettxtReplicaVehicleMake()
	{
		if (ivjtxtReplicaVehicleMake == null)
		{
			ivjtxtReplicaVehicleMake = new RTSInputField();
			ivjtxtReplicaVehicleMake.setLocation(new Point(165, 289));
			ivjtxtReplicaVehicleMake.setSize(new Dimension(49, 20));
			ivjtxtReplicaVehicleMake.setMaxLength(LENGTH_MAKE); 
			ivjtxtReplicaVehicleMake.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
		}
		return ivjtxtReplicaVehicleMake;
	}
	
	/**
	 * This method initializes ivjtxtReplicaVehicleModelYear	
	 * 	
	 * @return RTSInputField	
	 */
	private RTSInputField gettxtReplicaVehicleModelYear()
	{
		if (ivjtxtReplicaVehicleModelYear == null)
		{
			ivjtxtReplicaVehicleModelYear = new RTSInputField();
			ivjtxtReplicaVehicleModelYear.setLocation(new Point(120, 289));
			ivjtxtReplicaVehicleModelYear.setSize(new Dimension(40, 20));
			ivjtxtReplicaVehicleModelYear.setMaxLength(CommonConstant.LENGTH_YEAR);
			ivjtxtReplicaVehicleModelYear.setInput(
					RTSInputField.NUMERIC_ONLY);
		}
		return ivjtxtReplicaVehicleModelYear;
	}

	/**
	 * Return the ivjtxtState property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtState()
	{
		if (ivjtxtState == null)
		{
			try
			{
				ivjtxtState = new RTSInputField();
				ivjtxtState.setBounds(106, 235, 30, 20);
				// user code begin {1}
				ivjtxtState.setInput(RTSInputField.ALPHA_ONLY);
				ivjtxtState.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjtxtState.setMaxLength(CommonConstant.LENGTH_STATE);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtState;
	}

	/**
	 * Return the ivjtxtVehicleBodyType property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVehicleBodyType()
	{
		if (ivjtxtVehicleBodyType == null)
		{
			try
			{
				ivjtxtVehicleBodyType = new RTSInputField();
				ivjtxtVehicleBodyType.setSize(56, 20);
				ivjtxtVehicleBodyType.setPreferredSize(
					new java.awt.Dimension(128, 14));
				ivjtxtVehicleBodyType.setMaximumSize(
					new java.awt.Dimension(14, 14));
				ivjtxtVehicleBodyType.setMinimumSize(
					new java.awt.Dimension(14, 14));
				ivjtxtVehicleBodyType.setLocation(121, 140);
				// user code begin {1} 
				ivjtxtVehicleBodyType.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtVehicleBodyType.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjtxtVehicleBodyType.setMaxLength(2);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleBodyType;
	}

	/**
	 * Return the ivjtxtVehicleEmptyWeight property value.
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
				ivjtxtVehicleEmptyWeight.setSize(52, 19);
				ivjtxtVehicleEmptyWeight.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleEmptyWeight.setHorizontalAlignment(
					JTextField.RIGHT);
				ivjtxtVehicleEmptyWeight.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjtxtVehicleEmptyWeight.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtVehicleEmptyWeight.setMaxLength(LENGTH_EMPTY_WT);
				// user code end
				ivjtxtVehicleEmptyWeight.setLocation(120, 267);
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
	 * Return the ivjtxtVehicleMake property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleMake()
	{
		if (ivjtxtVehicleMake == null)
		{
			try
			{
				ivjtxtVehicleMake = new RTSInputField();
				ivjtxtVehicleMake.setSize(156, 20);
				ivjtxtVehicleMake.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				// defect 10127 
				ivjtxtVehicleMake.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				// end defect 10127 
				ivjtxtVehicleMake.setMaxLength(LENGTH_MAKE);
				// user code end
				ivjtxtVehicleMake.setLocation(160, 118);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleMake;
	}

	/**
	 * Return the ivjtxtVehicleModel property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVehicleModel()
	{
		if (ivjtxtVehicleModel == null)
		{
			try
			{
				ivjtxtVehicleModel = new RTSInputField();
				ivjtxtVehicleModel.setSize(56, 19);
				ivjtxtVehicleModel.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

				// defect 10127 
				ivjtxtVehicleModel.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				// end defect 10127 

				ivjtxtVehicleModel.setMaxLength(LENGTH_MODEL);
				// user code end
				ivjtxtVehicleModel.setPreferredSize(
					new java.awt.Dimension(4, 919));
				ivjtxtVehicleModel.setLocation(120, 162);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleModel;
	}

	/**
	 * Return the ivjtxtVehicleModelYear property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleModelYear()
	{
		if (ivjtxtVehicleModelYear == null)
		{
			try
			{
				ivjtxtVehicleModelYear = new RTSInputField();
				ivjtxtVehicleModelYear.setBounds(120, 118, 35, 20);
				ivjtxtVehicleModelYear.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtVehicleModelYear.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtVehicleModelYear.setMaxLength(
					CommonConstant.LENGTH_YEAR);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleModelYear;
	}

	/**
	 * Return the ivjtxtVehicleTonnage property value.
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
				ivjtxtVehicleTonnage.setSize(52, 19);
				ivjtxtVehicleTonnage.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleTonnage.setInput(
					RTSInputField.DOLLAR_ONLY);
				ivjtxtVehicleTonnage.setMaxLength(LENGTH_TONNAGE);
				// user code end
				ivjtxtVehicleTonnage.setLocation(232, 267);
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
	 * Set buttons for Record Not applicable.
	 */
	private void handleRecordNotApplicable()
	{
		cbRcdNotAppl = true;
		cbMFRecordFound = false;
		cbOdmtrReqd = false;
		ciOrigDocTypeCd = 0;
		getbtnRecordNotApplicable().setEnabled(false);
		gettxtVehicleModelYear().setEnabled(true);
		gettxtVehicleModel().setEnabled(true);
		gettxtRegPlateNo().setEnabled(true);
		gettxtVehicleEmptyWeight().setEnabled(true);
		gettxtVehicleTonnage().setEnabled(true);
		getstcLblOdometer().setEnabled(true);
		gettxtOdometerReading().setEnabled(true);

		gettxtVehicleBodyType().setVisible(false);
		getcomboBodyType().setVisible(true);
		getcomboBodyType().setEnabled(true);
		gettxtVehicleMake().setVisible(false);
		getcomboVehicleMake().setVisible(true);
		getcomboVehicleMake().setEnabled(true);

		getstcLblVehClass().setEnabled(false);
		getlblVehClass().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblVehClass().setEnabled(false);
		getstcLblRegClass().setEnabled(false);
		getlblRegClass().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblRegClass().setEnabled(false);

		populateBodyTypes();
		populateVehicleMakes();

		// defect 11368 
		gettxtReplicaVehicleModelYear().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtReplicaVehicleMake().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtReplicaVehicleModelYear().setEnabled(true); 
		gettxtReplicaVehicleMake().setEnabled(true);
		// end defect 11368 
		
		// defect 10689
		getcomboMajorColor().setSelectedIndex(COMBO_NOT_SELECTED); 
		getcomboMinorColor().setSelectedIndex(COMBO_NOT_SELECTED); 
		// end defect 10689 

		// defect 9637 
		if (!isOriginal() || getchkOwnerRetained().isSelected())
		{
			gettxtOwnerName1().setEnabled(true);
			gettxtOwnerName2().setEnabled(true);
			gettxtOwnerStreet1().setEnabled(true);
			gettxtOwnerStreet2().setEnabled(true);
			gettxtOwnerCity().setEnabled(true);
			gettxtOwnerState().setEnabled(true);
			gettxtOwnerZpcd().setEnabled(true);
			gettxtOwnerZpcdP4().setEnabled(true);
			gettxtOwnerCntry().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtOwnerCntryZpcd().setEnabled(true);
			getchkUSA().setEnabled(true);
			gettxtOwnerId().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtOwnerName1().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtOwnerName2().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtOwnerStreet1().setText(
				CommonConstant.STR_SPACE_EMPTY);
			gettxtOwnerStreet2().setText(
				CommonConstant.STR_SPACE_EMPTY);
			gettxtOwnerCity().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtOwnerState().setText(TX);
			gettxtOwnerZpcd().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtOwnerZpcdP4().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtOwnerCntry().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtOwnerCntryZpcd().setText(
				CommonConstant.STR_SPACE_EMPTY);
			getchkUSA().setSelected(true);
		}
		getchkOwnerRetained().setSelected(false);
		getchkOwnerRetained().setEnabled(false);
		// end defect 9637 
		gettxtVehicleEmptyWeight().setText(
			CommonConstant.STR_SPACE_EMPTY);
		gettxtVehicleTonnage().setText(CommonConstant.STR_ZERO_DOLLAR);

		// per demo 
		//gettxtVehicleModel().setText(CommonConstant.STR_SPACE_EMPTY);
		//gettxtVehicleModelYear().setText(
		//	CommonConstant.STR_SPACE_EMPTY);
		int liVehModlYr =
			Integer.parseInt(gettxtVehicleModelYear().getText());
		if (caVehData.getVehOdmtrReadng().equals(EXEMPT)
			|| (new RTSDate().getYear() - liVehModlYr >= 10))
		{
			getstcLblBrand().setEnabled(false);
			getcomboVehicleOdometerBrand().setEnabled(false);
			getcomboVehicleOdometerBrand().removeAllItems();
			gettxtOdometerReading().setText(EXEMPT);
		}
		else
		{
			gettxtOdometerReading().setText(
				CommonConstant.STR_SPACE_EMPTY);
			getcomboVehicleOdometerBrand().setEnabled(true);
			getstcLblBrand().setEnabled(true);
			populateBrand();
		}
		// end per demo 

		getlblDocTypeCodeDesc().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblDocNo().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblRTSIssueDate().setText(DATE_LAYOUT);
		gettxtRegPlateNo().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtPreviousOwnerCity().setText(
			CommonConstant.STR_SPACE_EMPTY);
		gettxtPreviousOwnerName().setText(
			CommonConstant.STR_SPACE_EMPTY);
		gettxtPreviousOwnerState().setText(TX);

		getchkDOTStandards().setSelected(false);
		getchkDOTStandards().setEnabled(true);
		getchkFloodDamage().setSelected(false);
		getchkFloodDamage().setEnabled(true);
		getchkRecordDeleteLien().setSelected(false);
		getchkRecordDeleteLien().setEnabled(true);
		// defect 10252 
		getchkManufacturerBuyback().setSelected(false);
		getchkManufacturerBuyback().setEnabled(true);
		// end defect 10252 
		getradioOriginal().setSelected(true);
		getradioDuplicate().setSelected(false);
		getradioDuplicate().setEnabled(false);
		getradioCorrected().setSelected(false);
		getradioCorrected().setEnabled(false);
		getlstIndiDescription().setListData(new Vector());
		String lsVin =
			caVehInqData.getMfVehicleData().getVehicleData().getVin();
		MFVehicleData laMFData = new MFVehicleData();
		laMFData.setOwnerData(new OwnerData());
		laMFData.setRegData(new RegistrationData());
		laMFData.setVctSalvage(new Vector());
		laMFData.getVctSalvage().add(new SalvageData());
		laMFData.setTitleData(new TitleData());
		laMFData.setUserSuppliedOwnerData(new OwnerData());
		laMFData.setVehicleData(new VehicleData());
		TitleValidObj laValidObj =
			(TitleValidObj) caVehInqData.getValidationObject();
		laValidObj.setMfVehOrig(laMFData);
		caVehInqData.setMfVehicleData(
			(MFVehicleData) UtilityMethods.copy(laMFData));
		caVehInqData.getMfVehicleData().getVehicleData().setVin(lsVin);
		caVehInqData.setNoMFRecs(0);
		// defect 9975
		caTitleData = caVehInqData.getMfVehicleData().getTitleData();
		// end defect 9975  
		
		gettxtOwnerName1().requestFocus();
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
			setName(ScreenConstant.TTL016_FRAME_NAME);
			// defect 9740 
			//setResizable(true);
			setResizable(false);
			// end defect 9740 
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setRequestFocus(false);
			setSize(685, 578);
			setTitle(ScreenConstant.TTL016_FRAME_TITLE);
			setContentPane(getFrmSalvageTTL016ContentPane1());
		}
		catch (Throwable aeTHRWEx)
		{
			// user code begin {2}
			handleException(aeTHRWEx);
			// user code end 
		}
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
				ErrorsConstant.ERR_NUM_INCOMPLETE_OWNR_DATA,
				ErrorsConstant.ERR_NUM_INCOMPLETE_OWNR_DATA,
				CommonConstant.TX_DEFAULT_STATE);
		// end defect 10127 

		// defect 10299  
		cvAddlMFValid = new Vector();
		cvAddlMFValid.add(gettxtPreviousOwnerName());
		cvAddlMFValid.add(gettxtPreviousOwnerCity());
		cvAddlMFValid.add(gettxtPreviousOwnerState());
		// end defect 10299 

		getcomboVehicleMake().setVisible(false);
		getcomboBodyType().setVisible(false);
		gettxtVehicleMake().setVisible(true);
		gettxtVehicleBodyType().setVisible(true);
		addWindowListener(this);
	}

	/**
	 * Return boolean to designate if Corrected Request
	 * 
	 * @return boolean 
	 */

	private boolean isCorrected()
	{
		return ciRecordTypeCd == CORRECTED;
	}

	/**
	 * Return boolean to designate if Duplicate Request
	 * 
	 * @return boolean 
	 */

	private boolean isDuplicate()
	{
		return ciRecordTypeCd == DUPLICATE;
	}

	/**
	 * Return true if Lien Entry required.
	 * 
	 * @return boolean
	 */
	private boolean isLienEntryScreenApplicable()
	{
		boolean lbRet = false;

		// defect 9975 
		//  Use "hasLien() vs. checking Lienhldr1Name1...   
		// defect 9642 
		//  Include COA 
		// defect 8746
		//   Use Force to TTL001 if Salvage/NRCOT & already has lien
		if (!getradioCOA().isSelected())
		{
			lbRet =
				(caTitleData.hasLien()
					&& isSalvageOrNonRepairDocTypeCd())
					|| (getchkRecordDeleteLien().isSelected());
		}
		return lbRet;
		// end defect 8746
		// end defect 9642
		// end defect 9975 		
	}

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
	 * Return boolean to designate if one of the NonRepairable 
	 * Doc Type Cds 
	 * 
	 * @return boolean
	 */
	private boolean isNonRepairableVehicleTitle()
	{
		// defect 9971 
		// return ciOrigDocTypeCd == 14 || ciOrigDocTypeCd == 15;
		return ciOrigDocTypeCd
			== DocTypeConstant.NONREPAIR_95_PLUS_LOSS
			|| ciOrigDocTypeCd
				== DocTypeConstant.NONREPAIR_95_PLUS_LOSS_NO_REG;
		// end defect 9971 
	}

	/**
	 * Return boolean to designate if Original Request
	 * 
	 * @return boolean 
	 */
	private boolean isOriginal()
	{
		return ciRecordTypeCd == ORIGINAL;
	}

	/**
	 * Return boolean to designate if one of the Salvage Certificate
	 * Doc Type Cds 
	 * 
	 * @return boolean 
	 */

	private boolean isSalvageCertificate()
	{
		// defect 9971
		// return ciOrigDocTypeCd == 3 || ciOrigDocTypeCd == 4;
		return ciOrigDocTypeCd
			== DocTypeConstant.SALVAGE_CERTIFICATE_NO_REGIS
			|| ciOrigDocTypeCd == DocTypeConstant.SALVAGE_CERTIFICATE;
		// end defect 9971 
	}

	/**
	 * Return boolean to designate if one of the Salvage Types 
	 * (Salvage Certificate, SCOT, NRCOT) 
	 * 
	 * @return boolean
	 */
	private boolean isSalvageOrNonRepairDocTypeCd()
	{
		return (
			isSalvageCertificate()
				|| isSalvageVehicleTitle()
				|| isNonRepairableVehicleTitle());
	}

	/**
	 * Return boolean to designate if one of the Salvage Vehicle Title
	 * Doc Type Cds 
	 * 
	 * @return
	 */
	private boolean isSalvageVehicleTitle()
	{
		// defect 9971
		//return ciOrigDocTypeCd == 12 || ciOrigDocTypeCd == 13;
		return ciOrigDocTypeCd == DocTypeConstant.SALV_TITLE_DAMAGED
			|| ciOrigDocTypeCd
				== DocTypeConstant.SALV_TITLE_DAMAGED_NO_REG;
		// end defect 9971 
	}

	/**
	 * Verify DocTypeCd valid for Processing
	 * 
	 * @return boolean
	 */
	private boolean isValidDocType()
	{
		boolean lbRet = true;

		// defect 10003 
		// User ErrorConstants 
		//if (ciOrigDocTypeCd == COA_NO_REGIS
		//	|| ciOrigDocTypeCd == COA_REGIS)
		if (ciOrigDocTypeCd == DocTypeConstant.CERTIFICATE_OF_AUTHORITY
			|| ciOrigDocTypeCd
				== DocTypeConstant.CERTIFICATE_OF_AUTHORITY_NO_REGIS)
		{

			RTSException leRTSEx = new RTSException(
				//24);
	ErrorsConstant.ERR_NUM_SALVAGE_NOT_AVAILABLE_FOR_COA);
			leRTSEx.displayError(this);
			lbRet = false;

		}
		//else if (ciOrigDocTypeCd == NON_TITLED		
		else if (ciOrigDocTypeCd == DocTypeConstant.NON_TITLED_VEHICLE)
		{
			RTSException leRTSEx = new RTSException(
				//97);
	ErrorsConstant.ERR_NUM_SALVAGE_NOT_AVAILABLE_FOR_NONTTL);
			leRTSEx.displayError(this);
			lbRet = false;
		}
		// end defect 10003 
		return lbRet;
	}

	/**
	 * Determine if any hard stops exist and have been cleared.
	 * 
	 * @return boolean
	 */
	private boolean isVTRAuthOKOnHardStop(VehicleInquiryData aaVehInqData)
	{
		boolean lbRet = true;

		if (cbMFRecordFound
			&& IndicatorLookup.hasHardStop(cvIndicator))
		{
			String lsAuth = aaVehInqData.getVehMiscData().getAuthCd();

			if (lsAuth == null || lsAuth.length() == 0)
			{
				getController().processData(
					VCSalvageTTL016.VTR_AUTH,
					aaVehInqData);

				lsAuth = aaVehInqData.getVehMiscData().getAuthCd();
			}

			if (lsAuth == null || lsAuth.length() == 0)
			{
				lbRet = false;
			}
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
	public void itemStateChanged(ItemEvent aaIE)
	{
		if (aaIE.getSource() == getcomboVehicleMake())
		{
			String lsStyle =
				(String) getcomboVehicleMake().getSelectedItem();
			if ((lsStyle != null) && (lsStyle.equals(NEW)))
			{
				getcomboVehicleMake().setEnabled(false);
				getcomboVehicleMake().setVisible(false);
				gettxtVehicleMake().setVisible(true);
				gettxtVehicleMake().setEditable(true);
				gettxtVehicleMake().setEnabled(true);
				gettxtVehicleMake().requestFocus();
			}
		}

		else if (aaIE.getSource() == ivjchkUSA && cbSetDataFinished)
		{
			// defect 10127 
			caNameAddrComp.resetPerUSA(
				aaIE.getStateChange() == ItemEvent.SELECTED);
			// end defect 10127
		}
		else if (aaIE.getSource() == getchkRecordDeleteLien())
		{
			resetLienIfCOA();
		}
	}

	/**
	 * Populate Body Types.
	 * 
	 */
	private void populateBodyTypes()
	{
		if (getcomboBodyType().isEnabled())
		{
			Vector lvVehBdyType =
				VehicleBodyTypesCache.getVehBdyTypesVec();
			UtilityMethods.sort(lvVehBdyType);

			//Set the default based on data received
			String lsVehBodyType = CommonConstant.STR_SPACE_EMPTY;
			if (caVehData != null)
			{
				lsVehBodyType = caVehData.getVehBdyType();
			}

			// defect 6353
			// Set to - 1 for blank display if VehBdyType not found
			int liIndiDefaultSelection = COMBO_NOT_SELECTED;
			// end defect 6353

			Vector lvComboValues = new Vector();
			if (lvVehBdyType != null)
			{
				for (int liIndex = 0;
					liIndex < lvVehBdyType.size();
					liIndex++)
				{
					VehicleBodyTypesData laData =
						(VehicleBodyTypesData) lvVehBdyType.get(
							liIndex);

					String lsDesc = laData.getVehBdyType();

					if (lsVehBodyType != null
						&& lsVehBodyType.length() > 0
						&& lsDesc.trim().equals(lsVehBodyType.trim()))
					{
						liIndiDefaultSelection = liIndex;
					}

					lsDesc =
						lsDesc
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ laData.getVehBdyTypeDesc();
					lvComboValues.add(lsDesc);
				}
			}
			DefaultComboBoxModel laMDL =
				new DefaultComboBoxModel(lvComboValues);
			getcomboBodyType().setModel(laMDL);
			getcomboBodyType().setSelectedIndex(liIndiDefaultSelection);
			// defect 8479
			comboBoxHotKeyFix(getcomboBodyType());
			// end defect 8479
		}
	}

	/**
	 * Populate Odometer Brands.
	 * 
	 */
	private void populateBrand()
	{
		Vector vcBrand = TitleClientUtilityMethods.getOdometerBrands();
		Vector vcComboVal = new Vector();
		for (int liIndex = 0; liIndex < vcBrand.size(); liIndex++)
		{
			String lsNext = (String) vcBrand.get(liIndex);
			vcComboVal.add(lsNext);
		}

		DefaultComboBoxModel laMDL =
			new DefaultComboBoxModel(vcComboVal);
		getcomboVehicleOdometerBrand().setModel(laMDL);
		if (!cbMFRecordFound)
		{
			getcomboVehicleOdometerBrand().setSelectedIndex(
				COMBO_NOT_SELECTED);
		}
	}

	/**
	 * Fills the Major Color combo box with the VehicleColorData.
	 */
	private void populateMajorColor()
	{
		if (getcomboMajorColor().isEnabled())
		{
			VehicleData laVehData =
				caVehInqData.getMfVehicleData().getVehicleData();
			String lsVehMajorColorCd = laVehData.getVehMjrColorCd();
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
			VehicleData laVehData =
				caVehInqData.getMfVehicleData().getVehicleData();
			String lsVehMinorColorCd = laVehData.getVehMnrColorCd();
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
	 * Populate New Owner Data Object from Dealer tables 
	 * 
	 * @param asDealerId 
	 */
	private void populateNewOwnerDataIfDealerId(String asDealerId)
	{
		if (asDealerId != null)
		{
			int liDealerId = 0;
			try
			{
				liDealerId = Integer.parseInt(asDealerId);
			}
			catch (NumberFormatException aeNFE)
			{

			}
			if (liDealerId <= TitleConstant.MAX_POS_DEALERID)
			{
				if (caNewOwnerData == null)
				{
					caNewOwnerData = new OwnerData();
					// defect 10112 
					// No longer required
					// caNewOwnerData.setAddressData(new AddressData());
					// end defect 10112
				}

				try
				{
					DealerData laDealerData =
						DealersCache.getDlr(
							SystemProperty.getOfficeIssuanceNo(),
							SystemProperty.getSubStationId(),
							liDealerId);

					if (laDealerData == null)
					{
						laDealerData = new DealerData();
					}

					// defect 10112 
					caNewOwnerData.setName1(laDealerData.getName1());

					caNewOwnerData.setName2(laDealerData.getName2());

					caNewOwnerData.setAddressData(
						(AddressData) UtilityMethods.copy(
							laDealerData.getAddressData()));
					// end defect 10112 

					caNewOwnerData.setOwnrId(
						Integer.toString(liDealerId));

					cbNewOwner = true;
					setupOwnerDataToDisplay();
					setupOwnerRetainedToDisplay();
					gettxtOwnerName1().requestFocus();
				}
				catch (RTSException aeRTSEx)
				{
				}
			}
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
	 * Populate Vehicle Makes.
	 */
	private void populateVehicleMakes()
	{
		if (getcomboVehicleMake().isEnabled())
		{
			Vector lvVehMk = VehicleMakesCache.getVehMks();
			UtilityMethods.sort(lvVehMk);

			//Set the default based on data received
			String lsVehMkFromRec = CommonConstant.STR_SPACE_EMPTY;
			if (caVehData != null)
			{
				lsVehMkFromRec = caVehData.getVehMk();
			}
			int liIndiDefaultSelection = -1;

			Vector vcComboVal = new Vector();
			if (lvVehMk != null)
			{
				for (int liIndex = 0;
					liIndex < lvVehMk.size();
					liIndex++)
				{
					VehicleMakesData laVehMkData =
						(VehicleMakesData) lvVehMk.get(liIndex);
					String lsDesc = laVehMkData.getVehMk();

					if (lsVehMkFromRec != null
						&& lsVehMkFromRec.length() > 0
						&& lsDesc.trim().equals(lsVehMkFromRec.trim()))
					{
						liIndiDefaultSelection = liIndex;
					}
					lsDesc =
						lsDesc
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ laVehMkData.getVehMkDesc();
					vcComboVal.add(lsDesc);

				}
				vcComboVal.add(NEW);
			}

			DefaultComboBoxModel laMDL =
				new DefaultComboBoxModel(vcComboVal);
			getcomboVehicleMake().setModel(laMDL);
			getcomboVehicleMake().setSelectedIndex(
				liIndiDefaultSelection);
			// defect 8479
			comboBoxHotKeyFix(getcomboVehicleMake());
			// end defect 8479
		}
	}

	/**
	 * Display Msg and reset Lien selection if Lien Selected and 
	 * COA selected
	 */
	private void resetLienIfCOA()
	{
		if (getradioCOA().isSelected()
			&& getchkRecordDeleteLien().isSelected())
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.INFORMATION_MESSAGE,
					NO_LIEN_MSG,
					INFORMATION);
			leRTSEx.displayError(this);

			ivjchkRecordDeleteLien.setSelected(false);
		}
	}

	/**
	 * If Select (SVT && isNRT) || (NRT && isSVT)  
	 * Only Original applies 
	 * 
	 * @param aaAE
	 */
	private void resetScreen(ActionEvent aaAE)
	{
		resetLienIfCOA();
		if ((aaAE.getSource().equals(getradioSalvageVehicleTitle())
			&& isNonRepairableVehicleTitle())
			|| (aaAE.getSource().equals(getradioNonRepair())
				&& isSalvageVehicleTitle()))
		{
			boolean lbNRT = getradioNonRepair().isSelected();
			boolean lbSVT = getradioSalvageVehicleTitle().isSelected();
			if (!getradioOriginal().isSelected())
			{
				setData(UtilityMethods.copy(caVehInqDataOrig));
				getradioCorrected().setEnabled(true);
				getradioCorrected().setSelected(true);
				getradioOriginal().setSelected(false);
				getradioOriginal().setEnabled(true);
				getradioSalvageVehicleTitle().setSelected(lbSVT);
				getradioNonRepair().setSelected(lbNRT);
			}
			else
			{
				// defect 9637 
				if (getchkOwnerRetained().isSelected())
				{
					setupOwnerDataToDisplay();
					setupOwnerRetainedToDisplay();
				}
				// end defect 9637 
				getradioCorrected().setEnabled(!cbNewOwner);
				getradioCorrected().setSelected(false);
			}
			getradioDuplicate().setEnabled(false);
			getradioDuplicate().setSelected(false);
		}
		else if (!cbRcdNotAppl && cbMFRecordFound)
		{
			setData(UtilityMethods.copy(caVehInqDataOrig));
		}
		gettxtOwnerName1().requestFocus();
	}

	/**
	 * Return Empty String if String is Null 
	 * 
	 * @return String 
	 */
	private String returnEmptyIfNull(String asString)
	{
		return asString != null
			? asString
			: CommonConstant.STR_SPACE_EMPTY;

	}

	/**
	 * Set ciCurrSalvData = last record.
	 * 
	 * @param aaSalvData VehicleInquiryData
	 */
	public void setCurrSalvData(VehicleInquiryData aaSlvgData)
	{

		ciCurrSalvData =
			aaSlvgData.getMfVehicleData().getVctSalvage().size();

		if (ciCurrSalvData > 0)
		{
			ciCurrSalvData = ciCurrSalvData - 1;
		}
	}

	/**
	 * Set Data.
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject != null
			&& aaDataObject instanceof VehicleInquiryData)
		{
			cbSetDataFinished = false;
			caVehInqData = (VehicleInquiryData) aaDataObject;

			cbMFRecordFound = caVehInqData.getNoMFRecs() > 0;

			if (caVehInqData.getValidationObject() == null)
			{
				TitleValidObj laTtlValidObj = new TitleValidObj();
				caVehInqData.setValidationObject(laTtlValidObj);
			}

			// defect 10073
			// Took assignment of TitleValidObj outside conditional
			if (caVehInqDataOrig == null)
			{
				caVehInqDataOrig =
					(VehicleInquiryData) UtilityMethods.copy(
						caVehInqData);
			}
			TitleValidObj laValidObj =
				(TitleValidObj) caVehInqData.getValidationObject();
			laValidObj.setMfVehOrig(
				caVehInqDataOrig.getMfVehicleData());
			// end defect 10073 

			caTitleData =
				caVehInqData.getMfVehicleData().getTitleData();

			caRegData = caVehInqData.getMfVehicleData().getRegData();

			caVehData =
				caVehInqData.getMfVehicleData().getVehicleData();
			caOwnerData =
				caVehInqData.getMfVehicleData().getOwnerData();

			// defect 10112
			// No longer required  
			//if (caOwnerData.getAddressData() == null)
			//{
			//	caOwnerData = getNewOwnerData();
			//	}
			// end defect 10112

			caNewOwnerData =
				caVehInqData
					.getMfVehicleData()
					.getUserSuppliedOwnerData();

			cbNewOwner = caNewOwnerData == null ? false : true;

			// defect 9654			
			if (cbNewOwner)
			{
				populateNewOwnerDataIfDealerId(
					caNewOwnerData.getOwnrId());
			}
			// end defect 9654

			ciOrigDocTypeCd =
				caVehInqDataOrig
					.getMfVehicleData()
					.getTitleData()
					.getDocTypeCd();

			if (cbMFRecordFound)
			{
				RegistrationClassData laRegClsData =
					RegistrationClassCache.getRegisClass(
						caVehData.getVehClassCd(),
						caRegData.getRegClassCd(),
						RTSDate.getCurrentDate().getYYYYMMDDDate());

				if (laRegClsData != null)
				{
					cbOdmtrReqd = laRegClsData.getOdmtrReqd() == 1;
					// defect 11004 
					//	cbTonnageReqd = laRegClsData.getTonReqd() == 1;
					// end defect 11004 
				}
				else
				{
					cbOdmtrReqd =
						(caVehData.getVehOdmtrBrnd() != null
							&& !caVehData.getVehOdmtrBrnd().equals(""))
							|| (caVehData.getVehOdmtrReadng() != null
								&& !caVehData.getVehOdmtrReadng().equals(
									""));
				}
			}

			// ***************************************************			
			// defect 5942, 6111, 6163 - 
			//	Clear out Junk Data
			// 	Save original data

			SalvageData laSlvgData = new SalvageData();

			for (int liIndex = 0;
				liIndex
					< caVehInqData
						.getMfVehicleData()
						.getVctSalvage()
						.size();
				liIndex++)
			{
				// Save original data
				laSlvgData =
					(SalvageData) caVehInqData
						.getMfVehicleData()
						.getVctSalvage()
						.get(
						liIndex);

				cvOrigSlvgData.add(
					liIndex,
					UtilityMethods.copy(laSlvgData));
				// End save original data

				// Reset Salvage Yard and Other Goverment Title number
				laSlvgData.setSalvYardNo(
					CommonConstant.STR_SPACE_EMPTY);
				laSlvgData.setOthrGovtTtlNo(
					CommonConstant.STR_SPACE_EMPTY);
				// End Reset Salvage Yard and Other Government Title 
				//	number

				// Write Salvage data to vector
				caVehInqData
					.getMfVehicleData()
					.getVctSalvage()
					.setElementAt(
					laSlvgData,
					liIndex);
				// End Write Salvage data to vector

			} // end for loop            
			// end defect 5942, 6111, 6163
			// ***************************************************
			setupDocTypeRadioToDisplay();
			setupVehDataToDisplay();
			setupTransTypeRadioToDisplay();
			// defect 9734 
			setupOthrStateCntryToDisplay();
			// end defect 9734 
			// defect 9637
			setupOwnerRetainedToDisplay();
			// end defect9637 
			setupOwnerDataToDisplay();
			// defect 10159
			//setupPrivacyOptionsToDisplay();
			// end defect 10159
			setOdometerBrandSelection();
			setupChkBoxesToDisplay();
			if (!cbAlreadySet)
			{
				setTransCd();
			}

			// defect 8494
			// Moved from windowOpened
			if (isNonRepairableVehicleTitle())
			{
				setDefaultFocusField(getradioNonRepair());
			}
			if (cvIndicator != null)
			{
				int liSize = cvIndicator.size();
				if (liSize > 4)
				{
					setDefaultFocusField(getlstIndiDescription());
				}
			}
			// end defect 8494
			cbAlreadySet = true;
			//gettxtOwnerId().requestFocus(); 
		}
	}

	/**
	 * Set Data to Data Object from Screen
	 * 
	 * @param aaVehInqData
	 */
	private void setDataToDataObject(VehicleInquiryData aaVehInqData)
	{
		setNewOdometerBrand(aaVehInqData);
		setNewDocTypeCd(aaVehInqData);

		// defect 9971
		// Set to default ETtlCd per DocTypeCd
		TitleData laTtlData =
			aaVehInqData.getMfVehicleData().getTitleData();

		laTtlData.setETtlCd(
			DocumentTypesCache.getDefaultETtlCd(
				laTtlData.getDocTypeCd()));
		// end defect 9971 

		String lsBodyType = CommonConstant.STR_SPACE_EMPTY;

		if (getcomboBodyType().isVisible())
		{
			if (getcomboBodyType().getSelectedIndex() > -1)
			{
				lsBodyType =
					(String) getcomboBodyType().getSelectedItem();
				lsBodyType =
					lsBodyType
						.substring(
							0,
							lsBodyType.indexOf(
								CommonConstant.STR_DASH))
						.trim();
			}
			else
			{
				lsBodyType = CommonConstant.STR_SPACE_EMPTY;
			}
		}
		else
		{
			lsBodyType = gettxtVehicleBodyType().getText().trim();
		}
		aaVehInqData.getMfVehicleData().getVehicleData().setVehBdyType(
			lsBodyType);

		String lsVehMk = CommonConstant.STR_SPACE_EMPTY;
		if (getcomboVehicleMake().isVisible())
		{
			if (getcomboVehicleMake().getSelectedIndex() > -1)
			{
				lsVehMk =
					(String) getcomboVehicleMake().getSelectedItem();
				lsVehMk =
					lsVehMk.substring(0, lsVehMk.indexOf("-")).trim();
			}
			else
			{
				lsVehMk = CommonConstant.STR_SPACE_EMPTY;
			}
		}
		else
		{
			lsVehMk = gettxtVehicleMake().getText().trim();
		}
		aaVehInqData.getMfVehicleData().getVehicleData().setVehMk(
			lsVehMk);

		aaVehInqData.getMfVehicleData().getVehicleData().setVehModl(
			gettxtVehicleModel().getText());
		aaVehInqData.getMfVehicleData().getVehicleData().setVehModlYr(
			Integer.parseInt(gettxtVehicleModelYear().getText()));
		aaVehInqData.getMfVehicleData().getRegData().setRegPltNo(
			gettxtRegPlateNo().getText());

		// defect 10689
		// Set the Major Color
		String lsColorCd = CommonConstant.STR_SPACE_EMPTY;
					
		lsColorCd = getMajorColorCdSelected();
		if (lsColorCd == null)
		{
			lsColorCd = CommonConstant.STR_SPACE_EMPTY;
		}
		aaVehInqData.getMfVehicleData().getVehicleData()
			.setVehMjrColorCd(lsColorCd);

		// Set the Minor Color
		lsColorCd = getMinorColorCdSelected();
		if (lsColorCd == null)
		{
			lsColorCd = CommonConstant.STR_SPACE_EMPTY;
		}
		aaVehInqData.getMfVehicleData().getVehicleData()
			.setVehMnrColorCd(lsColorCd);
		// end defect 10689
					
		// defect 11368 
		// Replica Model Year / Make  
		String lsRepVehModlYr =	gettxtReplicaVehicleModelYear().getText();
		String lsRepVehMk =	gettxtReplicaVehicleMake().getText();
		int liRepVehModlYr = 0; 
		if (lsRepVehModlYr != null
				&& lsRepVehModlYr.length() != 0)
		{
			liRepVehModlYr =
				Integer.parseInt(lsRepVehModlYr);
			
		}
		aaVehInqData.getMfVehicleData().getVehicleData().setReplicaVehModlYr(liRepVehModlYr);
		aaVehInqData.getMfVehicleData().getVehicleData().setReplicaVehMk(lsRepVehMk);
		// end defect 11368 
		
		// defect 7762
		// Set Registration Invalid Indicator 
		aaVehInqData.getMfVehicleData().getRegData().setRegInvldIndi(1);
		// end defect 7762

		if (gettxtVehicleEmptyWeight()
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			aaVehInqData
				.getMfVehicleData()
				.getVehicleData()
				.setVehEmptyWt(
				0);
		}
		else
		{
			aaVehInqData
				.getMfVehicleData()
				.getVehicleData()
				.setVehEmptyWt(
				Integer.parseInt(gettxtVehicleEmptyWeight().getText()));
		}
		aaVehInqData.getMfVehicleData().getVehicleData().setVehTon(
			new Dollar(gettxtVehicleTonnage().getText()));

		// Set Owner Data  
		OwnerData laOwnrData =
			aaVehInqData.getMfVehicleData().getOwnerData();

		if (laOwnrData == null)
		{
			laOwnrData = new OwnerData();
			aaVehInqData.getMfVehicleData().setOwnerData(laOwnrData);
		}

		// defect 10112 
		AddressData laAddrData = laOwnrData.getAddressData();
		// No longer required
		//		if (laAddrData == null)
		//		{
		//			laAddrData = new AddressData();
		//			laOwnrData.setOwnrAddr(laAddrData);
		//		}
		// end defect 10112 

		// defect 9636 
		// Save OwnerId only if 9 digits 
		String lsOwnerId = gettxtOwnerId().getText().trim();
		laOwnrData.setOwnrId(CommonConstant.STR_SPACE_EMPTY);

		if (lsOwnerId.length() != 0)
		{
			int liOwnerId = Integer.parseInt(lsOwnerId);
			if (lsOwnerId.length()
				== TitleConstant.REQD_MF_OWNERID_LENGTH
				&& liOwnerId > TitleConstant.MAX_POS_DEALERID)
			{
				laOwnrData.setOwnrId(lsOwnerId);
			}
		}
		// end defect 9636 

		// defect 10112 
		laOwnrData.setName1(gettxtOwnerName1().getText().trim());
		laOwnrData.setName2(gettxtOwnerName2().getText().trim());
		// end defect 10112 

		laAddrData.setCity(gettxtOwnerCity().getText().trim());
		laAddrData.setSt1(gettxtOwnerStreet1().getText().trim());
		laAddrData.setSt2(gettxtOwnerStreet2().getText().trim());

		if (getchkUSA().isSelected())
		{
			laAddrData.setState(gettxtOwnerState().getText().trim());
			laAddrData.setZpcd(gettxtOwnerZpcd().getText().trim());
			laAddrData.setZpcdp4(gettxtOwnerZpcdP4().getText().trim());
			laAddrData.setCntry(CommonConstant.STR_SPACE_EMPTY);
		}
		else
		{
			laAddrData.setCntry(gettxtOwnerCntry().getText().trim());

			// defect 10112 
			// Implement setCntryZpCd 
			laAddrData.setCntryZpcd(
				gettxtOwnerCntryZpcd().getText().trim());
			// end defect 10112 
			laAddrData.setState(CommonConstant.STR_SPACE_EMPTY);
		}

		aaVehInqData.getMfVehicleData().getTitleData().setPrevOwnrCity(
			gettxtPreviousOwnerCity().getText().trim());
		aaVehInqData.getMfVehicleData().getTitleData().setPrevOwnrName(
			gettxtPreviousOwnerName().getText().trim());
		aaVehInqData
			.getMfVehicleData()
			.getTitleData()
			.setPrevOwnrState(
			gettxtPreviousOwnerState().getText().trim());

		// defect 5942, 6163
		//	Get getVctSalvage().size() - 1 to add data to record
		SalvageData laSalvdata =
			(SalvageData) aaVehInqData
				.getMfVehicleData()
				.getVctSalvage()
				.get(
				ciCurrSalvData);

		laSalvdata.setOthrStateCntry(gettxtState().getText().trim());
		aaVehInqData
			.getMfVehicleData()
			.getVehicleData()
			.setVehOdmtrReadng(
			gettxtOdometerReading().getText().trim());

		aaVehInqData
			.getMfVehicleData()
			.getVehicleData()
			.setFloodDmgeIndi(
			getchkFloodDamage().isSelected() ? 1 : 0);

		aaVehInqData
			.getMfVehicleData()
			.getVehicleData()
			.setDotStndrdsIndi(
			getchkDOTStandards().isSelected() ? 1 : 0);

		// defect 10252
		aaVehInqData.getMfVehicleData().getTitleData().setLemonLawIndi(
			getchkManufacturerBuyback().isSelected() ? 1 : 0);
		// end defect 10252 

		// defect 9787 
		// Clear TtlSignDate 
		aaVehInqData.getMfVehicleData().getTitleData().setTtlSignDate(
			0);
		// end defect 9787 

		// defect 6767
		// do not carry forward SurrTtlDate
		aaVehInqData.getMfVehicleData().getTitleData().setSurrTtlDate(
			0);
		// end defect 6767

		// defect 9730 
		// Clear Vehicle Address, Recipient Name, Renwl Mail Addr 
		aaVehInqData.getMfVehicleData().getTitleData().setTtlVehAddr(
			null);
		aaVehInqData.getMfVehicleData().getRegData().setRecpntName(
			null);
		aaVehInqData.getMfVehicleData().getRegData().setRenwlMailAddr(
			null);
		// end defect 9730 
		
		// defect 10372 
		aaVehInqData.getMfVehicleData().getRegData().setRecpntEMail(null);
		// end defect 10372
		
		
		// defect 9738 
		aaVehInqData.getMfVehicleData().setSpclPltRegisData(null);
		// end defect 9738
		
		// defect 9642 
		if (getradioCOA().isSelected())
		{
			aaVehInqData
				.getMfVehicleData()
				.getRegData()
				.setEmissionSourceCd(
				CommonConstant.STR_SPACE_EMPTY);
			aaVehInqData
				.getMfVehicleData()
				.getRegData()
				.setClaimComptCntyNo(
				0);
			aaVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setCcoIssueDate(
				0);
			aaVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setPriorCCOIssueIndi(
				0);
			aaVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setVehSoldDate(
				0);
			aaVehInqData
				.getMfVehicleData()
				.getVehicleData()
				.setRecondCd(
				CommonConstant.STR_ZERO);
			aaVehInqData
				.getMfVehicleData()
				.getRegData()
				.setRenwlMailRtrnIndi(
				0);
		}
		// end defect 9642
		// defect 10366 
		// VTRTtlEmrgCd1 refactored to PvtLawEnfVehCd
		// defect 10153
		if (csTransCd.equals(TransCdConstant.SCOT)
			|| csTransCd.equals(TransCdConstant.NRCOT)
			|| csTransCd.equals(TransCdConstant.COA))
		{
			aaVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setPvtLawEnfVehCd(
				new String());
			
			// defect 10841 
			aaVehInqData
			.getMfVehicleData()
			.getTitleData()
			.setETtlPrntDate(0);
			// end defect 10841 
		}
		// end defect 10153 
		// end defect 10366 

		// defect 9634 
		// Remove Owner Retained Legal Restraint 
		if (aaVehInqData
			.getMfVehicleData()
			.getTitleData()
			.getLegalRestrntNo()
			!= null
			&& aaVehInqData
				.getMfVehicleData()
				.getTitleData()
				.getLegalRestrntNo()
				.startsWith(TitleConstant.OWNR_RETAINED_PREFIX))
		{
			aaVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setLegalRestrntNo(
				CommonConstant.STR_SPACE_EMPTY);
		}
		// end defect 9634 

		// defect 9692
		// Reset SalvStateCntry 
		aaVehInqData
			.getMfVehicleData()
			.getTitleData()
			.setSalvStateCntry(
			CommonConstant.STR_SPACE_EMPTY);
		// end defect 9692 

		// temp storage to let other screens know that duplicate is 
		// selected
		(
			(TitleValidObj) aaVehInqData
				.getValidationObject())
				.setChangeRegis(
			getradioDuplicate().isSelected());
	}

	/**
	 * Set Document type for transaction.
	 * 
	 * @param aaVehInqData
	 */
	private void setNewDocTypeCd(VehicleInquiryData aaVehInqData)
	{
		boolean lbRegistered = false;
		int liDocTypeCd = 12;
		if (aaVehInqData != null && caVehInqData.getNoMFRecs() == 1)
		{
			String lsPltNo =
				caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegPltNo();

			lbRegistered =
				lsPltNo != null
					&& !lsPltNo.equals(CommonConstant.STR_SPACE_EMPTY);
			{

			}
		}

		// defect 9642 
		if (getradioCOA().isSelected())
		{
			liDocTypeCd = lbRegistered ? 7 : 6;
		}
		// end defect 9642 
		else if (getradioSalvageVehicleTitle().isSelected())
		{
			liDocTypeCd = lbRegistered ? 12 : 13;
		}
		else
		{
			liDocTypeCd = lbRegistered ? 14 : 15;
		}

		aaVehInqData.getMfVehicleData().getTitleData().setDocTypeCd(
			liDocTypeCd);
	}

	/**
	 * Set Odometer Brand.
	 * 
	 * @param aaVehInqData
	 */
	private void setNewOdometerBrand(VehicleInquiryData aaVehInqData)
	{
		if (getcomboVehicleOdometerBrand().isVisible())
		{
			String lsSelected =
				(String) getcomboVehicleOdometerBrand()
					.getSelectedItem();

			if (lsSelected != null
				&& lsSelected.equals(OdometerBrands.ACTUAL))
			{
				aaVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.setVehOdmtrBrnd(
					A);
			}
			else if (
				lsSelected != null
					&& lsSelected.equals(OdometerBrands.NOTACT))
			{
				aaVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.setVehOdmtrBrnd(
					N);
			}
			else if (
				lsSelected != null
					&& lsSelected.equals(OdometerBrands.EXCEED))
			{
				aaVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.setVehOdmtrBrnd(
					X);
			}
			else if (
				lsSelected == null
					|| lsSelected.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				aaVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.setVehOdmtrBrnd(
					null);
			}
		}
	}

	/**
	 * Set Odometer Brand Selection.
	 */
	private void setOdometerBrandSelection()
	{
		if (caVehInqData != null)
		{
			if (!cbOdmtrReqd && cbMFRecordFound)
			{
				getstcLblBrand().setEnabled(false);
				getcomboVehicleOdometerBrand().setEnabled(false);
				getcomboVehicleOdometerBrand().removeAllItems();
				gettxtOdometerReading().setEnabled(false);
				gettxtOdometerReading().setText(
					CommonConstant.STR_SPACE_EMPTY);
				getstcLblOdometer().setEnabled(false);

			}
			else
			{
				// Reset Odometer prior to display if we know it should
				// be EXEMPT  
				String lsOdmtrReading =
					returnEmptyIfNull(caVehData.getVehOdmtrReadng());

				if (cbOdometerReset)
				{
					lsOdmtrReading = gettxtOdometerReading().getText();
				}

				String lsOdmtrBrand =
					returnEmptyIfNull(caVehData.getVehOdmtrBrnd());

				if (!lsOdmtrReading.equals(EXEMPT)
					&& !isDuplicate()
					&& !cbOdometerReset)
				{
					int liCurrentYear = new RTSDate().getYear();
					int liVehModlYr = caVehData.getVehModlYr();
					if (liVehModlYr != 0
						&& liCurrentYear - liVehModlYr >= 10)
					{
						lsOdmtrReading = EXEMPT;
						if (SystemProperty.getProdStatus() != 0)
						{
							System.out.println(
								"AGE: Odometer changed to EXEMPT");
						}
					}
					// Record found only  - else do not know Gross Weight
					else if (cbMFRecordFound)
					{
						String lsVehMdlYr =
							gettxtVehicleModelYear().getText().trim();
						String lsVehTon =
							gettxtVehicleTonnage().getText().trim();
						String lsGrossWt =
							"" + caRegData.getVehGrossWt();
						if (TitleClientUtilityMethods
							.isInvalidOdometerReadingBasedOnTIMA(
								lsVehMdlYr,
								lsVehTon,
								lsGrossWt,
								lsOdmtrReading))
						{
							lsOdmtrReading = EXEMPT;
							if (SystemProperty.getProdStatus() != 0)
							{
								System.out.println(
									"WEIGHT: Odometer changed to EXEMPT");
							}
						}
					}
				}

				gettxtOdometerReading().setEnabled(!isDuplicate());
				if (lsOdmtrReading.equals(EXEMPT))
				{
					getstcLblBrand().setEnabled(false);
					getcomboVehicleOdometerBrand().setEnabled(false);
					getcomboVehicleOdometerBrand().removeAllItems();
					gettxtOdometerReading().setText(EXEMPT);
				}
				else
				{
					gettxtOdometerReading().setText(lsOdmtrReading);
					populateBrand();
					getstcLblBrand().setEnabled(true);
					boolean lbEnable = true;
					if (lsOdmtrBrand.equals(X))
					{
						getcomboVehicleOdometerBrand().setSelectedItem(
							OdometerBrands.EXCEED);
						lbEnable = false;
					}
					else if (lsOdmtrBrand.equals(N))
					{
						getcomboVehicleOdometerBrand().setSelectedItem(
							OdometerBrands.NOTACT);
						// defect 9749
						//lbEnable = false; 
						lbEnable = isCorrected();
						// end defect 9749
					}
					else if (cbMFRecordFound || lsOdmtrBrand.equals(A))
					{
						getcomboVehicleOdometerBrand().setSelectedItem(
							OdometerBrands.ACTUAL);
					}
					getcomboVehicleOdometerBrand().setEnabled(
						lbEnable && !isDuplicate());
				}
			}
			// defect 8479
			comboBoxHotKeyFix(getcomboVehicleOdometerBrand());
			// end defect 8479
		}
	}

	/**
	 * Set Record Type Cd Variable
	 */
	private void setRecordTypeCd()
	{
		if (getradioOriginal().isSelected())
		{
			ciRecordTypeCd = ORIGINAL;
		}
		else if (getradioCorrected().isSelected())
		{
			ciRecordTypeCd = CORRECTED;
		}
		else if (getradioDuplicate().isSelected())
		{
			ciRecordTypeCd = DUPLICATE;
		}
		else
		{
			ciRecordTypeCd = 0;
		}
	}

	/**
	 * 
	 * Set TransCd
	 * 
	 */
	private void setTransCd()
	{
		if (getradioDuplicate().isSelected())
		{
			csTransCd =
				getradioSalvageVehicleTitle().isSelected()
					? TransCdConstant.CCOSCT
					: TransCdConstant.CCONRT;
		}
		else
		{
			if (getradioSalvageVehicleTitle().isSelected())
			{
				csTransCd =
					isCorrected()
						? TransCdConstant.CORSLV
						: TransCdConstant.SCOT;
			}
			else if (getradioNonRepair().isSelected())
			{
				csTransCd =
					isCorrected()
						? TransCdConstant.CORNRT
						: TransCdConstant.NRCOT;
			}
			else
			{
				csTransCd = TransCdConstant.COA;
			}
		}
		if (SystemProperty.getProdStatus() != 0)
		{
			System.out.println(csTransCd);
		}
		setupIndicatorsToDisplay();
	}

	/**
	 * Set ciCurrSalvData to last record, salvCd and TransCd
	 * 
	 * @param aaVehInqData 
	 */
	private void setTransCdJnkCd(VehicleInquiryData aaVehInqData)
	{
		if (aaVehInqData != null
			&& aaVehInqData.getMfVehicleData().getVctSalvage().size()
				== 0)
		{
			aaVehInqData.getMfVehicleData().getVctSalvage().add(
				new SalvageData());
		}
		// defect 6111
		else if (aaVehInqData != null)
		{
			// defect 5942 
			//	Use record index = 
			//		caVehInqData.getMfVehicleData().getVctSalvage().
			//			size() - 1 
			//		for processing     
			setCurrSalvData(aaVehInqData);
			// end defect 5942
		}

		// end defect 6111

		// defect 5942
		//	Get getVctSalvage().size() - 1 to add the JnkCd and JnkDate 
		//		to record
		SalvageData laSlvgData =
			(SalvageData) aaVehInqData
				.getMfVehicleData()
				.getVctSalvage()
				.get(
				ciCurrSalvData);

		if (getradioDuplicate().isSelected())
		{
			aaVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setCcoIssueDate(
				RTSDate.getCurrentDate().getYYYYMMDDDate());
		}
		else
		{
			aaVehInqData
				.getMfVehicleData()
				.getRegData()
				.setClaimComptCntyNo(
				0);
			aaVehInqData
				.getMfVehicleData()
				.getRegData()
				.setEmissionSourceCd(
				CommonConstant.STR_SPACE_EMPTY);
			aaVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setCcoIssueDate(
				0);

			// Cannot imagine why this is applicable; Introduced in 2002
			// if (!isLienEntryScreenApplicable())
			//{
			aaVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setPriorCCOIssueIndi(
				0);
			//}

			aaVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setVehSoldDate(
				0);

			aaVehInqData
				.getMfVehicleData()
				.getVehicleData()
				.setRecondCd(
				CommonConstant.STR_SPACE_EMPTY);

			aaVehInqData
				.getMfVehicleData()
				.getRegData()
				.setRenwlMailRtrnIndi(
				0);

			// defect 5942
			// Get getVctSalvage().size() - 1 to add the JnkCd to rcd 
			laSlvgData =
				(SalvageData) aaVehInqData
					.getMfVehicleData()
					.getVctSalvage()
					.get(
					ciCurrSalvData);

			laSlvgData.setSlvgDt(RTSDate.getCurrentDate());
			if (getradioSalvageVehicleTitle().isSelected())
			{
				laSlvgData.setSlvgCd(8);
			}
			else if (getradioNonRepair().isSelected())
			{
				laSlvgData.setSlvgCd(9);
			}
			// defect 9642 
			else
			{
				laSlvgData.setSlvgCd(3);
			}
			// end defect 9642

			aaVehInqData
				.getMfVehicleData()
				.getRegData()
				.setCustActlRegFee(
				new Dollar(CommonConstant.STR_ZERO_DOLLAR));

			// defect 9631
			//caVehInqData
			//	.getMfVehicleData()
			//	.getRegData()
			//	.setCustBaseRegFee(
			//	new Dollar(CommonConstant.STR_ZERO_DOLLAR));
			// }
			// end defect 9631

			if (!isLienEntryScreenApplicable())
			{
				aaVehInqData
					.getMfVehicleData()
					.getTitleData()
					.setVehSalesPrice(
					new Dollar(CommonConstant.STR_ZERO_DOLLAR));
				aaVehInqData
					.getMfVehicleData()
					.getTitleData()
					.setSalesTaxPdAmt(
					new Dollar(CommonConstant.STR_ZERO_DOLLAR));
			}

		}
		getController().setTransCode(csTransCd);
	}

	/**
	 * Reset ChkBox availability based upon status of Duplicate Radio
	 * Button
	 */
	private void setupChkBoxesToDisplay()
	{
		getchkDOTStandards().setEnabled(!isDuplicate());
		getchkRecordDeleteLien().setEnabled(!isDuplicate());

		boolean lbFloodDmgeIndi =
			caVehInqData
				.getMfVehicleData()
				.getVehicleData()
				.getFloodDmgeIndi()
				== 1;

		boolean lbEnableFlood =
			(!lbFloodDmgeIndi && !isDuplicate())
				|| (lbFloodDmgeIndi && isCorrected());

		getchkFloodDamage().setEnabled(lbEnableFlood);

		// defect 10252 
		getchkManufacturerBuyback().setEnabled(!isDuplicate());
		// end defect 10252 

		if (isDuplicate())
		{
			getchkDOTStandards().setSelected(false);
			getchkRecordDeleteLien().setSelected(false);
		}
	}

	/**
	 * Set Up Radio Button to Reflect Doc Type Cd
	 * 
	 * Salvage Vehicle Title, Non-Repairable Vehicle Title, COA 
	 */
	private void setupDocTypeRadioToDisplay()
	{
		if (ciActionType != DOCTYPE)
		{
			// defect 9701 
			// No longer check if HQ; Region cannot access 
			boolean lbNRV = isNonRepairableVehicleTitle();
			getradioSalvageVehicleTitle().setEnabled(true);
			getradioSalvageVehicleTitle().setSelected(!lbNRV);
			getradioNonRepair().setEnabled(true);
			getradioNonRepair().setSelected(lbNRV);
			// end defect 9701 

			// defect 9642 
			SecurityData laScrtyData =
				getController()
					.getMediator()
					.getDesktop()
					.getSecurityData();
			getradioCOA().setEnabled(laScrtyData.getCOAAccs() == 1);
			getradioCOA().setSelected(false);
			// end defect 9642 
		}
	}

	/**
	 * Get indicators and build string to display indicators in text 
	 * area.
	 */
	private void setupIndicatorsToDisplay()
	{
		if (caVehInqData != null && cbMFRecordFound)
		{
			if (SystemProperty.getProdStatus() != 0)
			{
				System.out.println(
					"Setting Indicators for: " + csTransCd);
			}

			cvIndicator =
				IndicatorLookup.getIndicators(
					caVehInqData.getMfVehicleData(),
					csTransCd,
					IndicatorLookup.SCREEN);

			StringBuffer lsIndis =
				new StringBuffer(CommonConstant.STR_SPACE_EMPTY);
			int liNumIndis = cvIndicator.size();

			if (liNumIndis > 0)
			{
				Vector lvRows = new java.util.Vector();

				for (int liIndex = 0; liIndex < liNumIndis; liIndex++)
				{
					IndicatorData laData =
						(IndicatorData) cvIndicator.get(liIndex);

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

				// Set focus to indicator box if more than 5 entries
				if (!cbAlreadySet && liNumIndis > MAX_INDI_NO_SCROLL)
				{
					getlstIndiDescription().requestFocus();
					getlstIndiDescription().setSelectedIndex(0);
				}
			}
		}
	}
	/** 
	 * Setup Other State/Country to Display 
	 */
	private void setupOthrStateCntryToDisplay()
	{
		// Default State to TX for Owner Address and O/S
		String lsOthrStateCntry = TX;
		if ((isDuplicate() || isCorrected())
			&& caVehInqData.getMfVehicleData().getVctSalvage().size()
				!= 0)
		{
			SalvageData laSalvdata =
				(SalvageData) caVehInqData
					.getMfVehicleData()
					.getVctSalvage()
					.get(
					ciCurrSalvData);

			if (laSalvdata != null
				&& laSalvdata.getOthrStateCntry() != null
				&& laSalvdata.getOthrStateCntry().trim().length() != 0)
			{
				lsOthrStateCntry = laSalvdata.getOthrStateCntry();
			}
		}
		gettxtState().setText(lsOthrStateCntry);
		gettxtState().setEnabled(!isDuplicate());

	}

	/**
	 * Setup Owner Data To Display
	 */
	private void setupOwnerDataToDisplay()
	{
		OwnerData laPriorOwner = getNewOwnerData();

		if (!cbRcdNotAppl && !isDuplicate())
		{
			laPriorOwner = (OwnerData) UtilityMethods.copy(caOwnerData);

			// defect 10112 
			String lsCntry =
				laPriorOwner.getAddressData().getCntry().trim();

			if (lsCntry.length() != 0)
			{
				laPriorOwner.getAddressData().setState(lsCntry);
				laPriorOwner.getAddressData().setCntry(
					CommonConstant.STR_SPACE_EMPTY);
			}
			// end defect 10112 
		}

		OwnerData laNewOwner =
			(OwnerData) UtilityMethods.copy(caNewOwnerData);

		if (laNewOwner == null)
		{
			// defect 9642  
			if (!isDuplicate())
			{
				cbNewOwner = true;

				// defect 9637 
				if (getchkOwnerRetained().isSelected()
					|| isCorrected())
				{
					laNewOwner =
						(OwnerData) UtilityMethods.copy(caOwnerData);
				}
				else
				{
					laNewOwner = getNewOwnerData();
				}
				// end defect 9637 
			}
			// end defect 9642 
			else
			{
				// defect 10112 
				laPriorOwner.setName1(caTitleData.getPrevOwnrName());
				laPriorOwner.getAddressData().setCity(
					caTitleData.getPrevOwnrCity());
				laPriorOwner.getAddressData().setState(
					caTitleData.getPrevOwnrState());
				// end defect 10112

				laNewOwner =
					(OwnerData) UtilityMethods.copy(caOwnerData);
			}
		}

		// PREVIOUS 
		// defect 9721
		// Truncate the fields as required to prevent system beeping

		// defect 10112  
		String lsPriorName = laPriorOwner.getName1();

		// defect 10127 
		if (lsPriorName != null
			&& lsPriorName.length()
				> CommonConstant.LENGTH_PREV_OWNR_NAME)
		{
			lsPriorName =
				lsPriorName.substring(
					0,
					CommonConstant.LENGTH_PREV_OWNR_NAME);
		}

		String lsPriorCity = laPriorOwner.getAddressData().getCity();

		if (lsPriorCity != null
			&& lsPriorCity.length()
				> CommonConstant.LENGTH_PREV_OWNR_CITY)
		{
			lsPriorCity =
				lsPriorCity.substring(
					0,
					CommonConstant.LENGTH_PREV_OWNR_CITY);
		}
		// end defect 10127 

		String lsPriorStateCntry =
			laPriorOwner.getAddressData().getState();

		if (lsPriorStateCntry != null
			&& lsPriorStateCntry.length() > CommonConstant.LENGTH_STATE)
		{
			lsPriorStateCntry =
				lsPriorStateCntry.substring(
					0,
					CommonConstant.LENGTH_STATE);
		}

		gettxtPreviousOwnerName().setText(lsPriorName);
		gettxtPreviousOwnerCity().setText(lsPriorCity);
		gettxtPreviousOwnerState().setText(lsPriorStateCntry);
		// end defect 9721 

		boolean lbEnable = !isDuplicate();

		// Name/Address 			
		caNameAddrComp.setNameAddressDataToDisplay(laNewOwner);
		caNameAddrComp.setEnabled(lbEnable);

		// OwnerId
		gettxtOwnerId().setText(
			returnEmptyIfNull(laNewOwner.getOwnrId()));
		gettxtOwnerId().setEnabled(lbEnable);

		// Previous Owner 
		gettxtPreviousOwnerName().setEnabled(lbEnable);
		gettxtPreviousOwnerCity().setEnabled(lbEnable);
		gettxtPreviousOwnerState().setEnabled(lbEnable);

		cbSetDataFinished = true;
	}

	/**
	 * Initialize the Owner Retained CheckBox 
	 * 
	 */
	private void setupOwnerRetainedToDisplay()
	{
		// defect 9642 
		boolean lbCOA = getradioCOA().isSelected();
		boolean lbOwnrRetApplicable =
			(caVehInqDataOrig.getNoMFRecs() > 0 && !cbNewOwner)
				&& (lbCOA || (!isSalvageOrNonRepairDocTypeCd() && !lbCOA));
		// end defect 9642

		getchkOwnerRetained().setEnabled(lbOwnrRetApplicable);
		if (!lbOwnrRetApplicable)
		{
			getchkOwnerRetained().setSelected(false);
		}
	}
	// defect 10159
	/**
	 * Set Privacy Options to Display 
	 */
	//private void setupPrivacyOptionsToDisplay()
	//{
	//getpnlPrivacyOpt().setEnabled(false);
	//getradioBoth().setEnabled(false);
	//getradioBoth().setSelected(true);
	//getradioCommercial().setEnabled(false);
	//getradioNone().setEnabled(false);
	//getradioIndividual().setEnabled(false);
	//}
	// end defect 10159
	/**
	 * Set up Transaction Type Radio To Display
	 * 
	 * Original / Corrected / Duplicate 
	 * 
	 */
	private void setupTransTypeRadioToDisplay()
	{
		if (ciActionType != RECORDTYPE)
		{
			// defect 9642 
			boolean lbCOA = getradioCOA().isSelected();

			if (!lbCOA
				&& (isSalvageVehicleTitle()
					|| isNonRepairableVehicleTitle()))
			{
				getradioCorrected().setEnabled(!cbNewOwner);
				boolean lbCorrect = !isOriginal() && !cbNewOwner;
				getradioCorrected().setSelected(lbCorrect);
				getradioDuplicate().setSelected(
					false || (isDuplicate() && !lbCorrect));
				getradioDuplicate().setEnabled(!cbNewOwner);
				getradioOriginal().setEnabled(true);
				getradioOriginal().setSelected(
					isOriginal() || cbNewOwner);

			}
			// Title or Salvage
			else
			{
				getradioCorrected().setEnabled(false);
				getradioCorrected().setSelected(false);
				getradioDuplicate().setSelected(false);
				getradioDuplicate().setEnabled(false);
				getradioOriginal().setEnabled(true);
				getradioOriginal().setSelected(true);
			}
			setRecordTypeCd();
		}
		// end defect 9642 
	}

	/**
	 * Setup Vehicle Fields for Display
	 * 
	 */
	private void setupVehDataToDisplay()
	{
		getlblVIN().setText(caVehData.getVin());
		getstcLblVehClass().setEnabled(cbMFRecordFound);
		getstcLblRegClass().setEnabled(cbMFRecordFound);
		getlblRegClass().setEnabled(cbMFRecordFound);
		getlblVehClass().setEnabled(cbMFRecordFound);
		// defect 10689
		populateVehColorVector();
		populateMajorColor(); 
		populateMinorColor(); 
		// end defect 10689 

		if (!cbMFRecordFound)
		{

			RTSException leRTSEx = new RTSException(211);
			RTSDialogBox laRTSDBox =
				getController().getMediator().getParent();
			if (laRTSDBox != null)
			{
				leRTSEx.displayError(laRTSDBox);
			}
			else
			{
				leRTSEx.displayError(this);
			}

			gettxtVehicleMake().setVisible(false);
			gettxtVehicleBodyType().setVisible(false);
			getcomboVehicleMake().setVisible(true);
			getcomboBodyType().setVisible(true);
			populateVehicleMakes();
			populateBodyTypes();
			gettxtVehicleModelYear().setText(
				caVehData.getVehModlYr() == 0
					? CommonConstant.STR_SPACE_EMPTY
					: Integer.toString(caVehData.getVehModlYr()));
			// defect 6232
			// Check for VINA VehModlYr
			setVINAData();
			// end defect 6232 
			//getlstIndiDescription().setEnabled(false);
			getbtnRecordNotApplicable().setEnabled(false);
			getlblRTSIssueDate().setText(DATE_LAYOUT);
			getlblDocNo().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtVehicleTonnage().setText(
				CommonConstant.STR_ZERO_DOLLAR);
			getlblDocTypeCodeDesc().setText(
				CommonConstant.STR_SPACE_EMPTY);
		}
		else
		{
			getlblDocTypeCodeDesc().setText(getDocTypeDesc());

			// defect 10206
			//if (!getchkFloodDamage().isSelected())
			if (!getchkFloodDamage().isSelected() || isDuplicate())
			{
				// end defect 10206 
				getchkFloodDamage().setSelected(
					caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.getFloodDmgeIndi()
						== 1);
			}

			if (caTitleData != null)
			{
				getlblDocNo().setText(caTitleData.getDocNo());

				if (caTitleData.getTtlIssueDate() == 0)
				{
					getlblRTSIssueDate().setText(
						CommonConstant.STR_SPACE_EMPTY);
				}
				else
				{
					RTSDate laRTSDate =
						new RTSDate(
							RTSDate.YYYYMMDD,
							caTitleData.getTtlIssueDate());
					getlblRTSIssueDate().setText(laRTSDate.toString());
				}

				// defect 10252 
				if (!getchkManufacturerBuyback().isSelected()
					|| isDuplicate())
				{
					getchkManufacturerBuyback().setSelected(
						caTitleData.getLemonLawIndi() == 1);
				}
				// end defect 10252 
			}

			if (caVehData != null)
			{
				int liRegClassCd = caRegData.getRegClassCd();
				CommonFeesData laCommonFeesData =
					CommonFeesCache.getCommonFee(
						liRegClassCd,
						RTSDate.getCurrentDate().getYYYYMMDDDate());
				if (laCommonFeesData != null)
				{
					getlblRegClass().setText(
						laCommonFeesData.getRegClassCdDesc());
				}
				getlblVehClass().setText(caVehData.getVehClassCd());
				gettxtVehicleMake().setText(caVehData.getVehMk());
				gettxtVehicleModel().setText(caVehData.getVehModl());
				gettxtVehicleModelYear().setText(
					CommonConstant.STR_SPACE_EMPTY
						+ caVehData.getVehModlYr());
				gettxtVehicleBodyType().setText(
					caVehData.getVehBdyType());
				if (caVehData.getVehEmptyWt() == 0)
				{
					gettxtVehicleEmptyWeight().setText(
						CommonConstant.STR_SPACE_EMPTY);
				}
				else
				{
					gettxtVehicleEmptyWeight().setText(
						CommonConstant.STR_SPACE_EMPTY
							+ caVehData.getVehEmptyWt());
				}
				
				// defect 11368 
				gettxtReplicaVehicleModelYear().setText(getEmptyStringIfZero(""+(caVehData.getReplicaVehModlYr()))); 
				gettxtReplicaVehicleMake().setText(caVehData.getReplicaVehMk() != null ? caVehData.getReplicaVehMk() : new String());
				gettxtReplicaVehicleModelYear().setEnabled(!isDuplicate());
				gettxtReplicaVehicleMake().setEnabled(!isDuplicate());
				// end defect 11368
				
				gettxtVehicleTonnage().setText(
					CommonConstant.STR_SPACE_EMPTY
						+ caVehData.getVehTon());
				gettxtVehicleMake().setEnabled(!isDuplicate());
				gettxtVehicleBodyType().setEnabled(!isDuplicate());
				gettxtVehicleModel().setEnabled(!isDuplicate());
				gettxtVehicleModelYear().setEnabled(!isDuplicate());
				gettxtVehicleTonnage().setEnabled(!cbMFRecordFound);
				gettxtVehicleEmptyWeight().setEnabled(!cbMFRecordFound);
			}

			if (caRegData != null && caRegData.getRegPltNo() != null)
			{
				gettxtRegPlateNo().setText(caRegData.getRegPltNo());
				gettxtRegPlateNo().setEnabled(false);
			}

		}
	}

	/**
	 * Set vehicle year, vehicle model from VINA.
	 */
	private void setVINAData()
	{
		// Set VehModlYr
		gettxtVehicleModelYear().setText(
			caVehData.getVehModlYr() == 0
				? CommonConstant.STR_SPACE_EMPTY
				: Integer.toString(caVehData.getVehModlYr()));

		// Set VehModel
		gettxtVehicleModel().setText(
			returnEmptyIfNull(caVehData.getVehModl()));
	}

	/**
	 * Valiate Fields  
	 *
	 * @return boolean 
	 */
	private boolean validateFields()
	{
		boolean lbValid = true;
		RTSException leRTSEx = new RTSException();

		// defect 10299 
		// Moved from setDataToDataObj()
		UtilityMethods.trimRTSInputField(this);
		// end defect 10299 

		// defect 10003
		validateOwnrId(leRTSEx);
		// end defect 10003

		// defect 10127 
		caNameAddrComp.validateNameAddressFields(leRTSEx);
		// end defect 10127

		validatePrevOwnrInfo(leRTSEx);

		// defect 10299
		CommonValidations.addRTSExceptionForInvalidMFCharacters(
			cvAddlMFValid,
			leRTSEx);
		// end defect 10299
		
		// defect 11004 
		//Implement CommonValidations  
		CommonValidations.addRTSExceptionForInvalidCntryStateCntry(gettxtState(), 
				leRTSEx, TitleConstant.NOT_REQUIRED);
		// end defect 11004 

		validateVehicleInfo(leRTSEx);
		validateOdometer(leRTSEx);
		
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;
	}

	/**
	 * Verify Odometer Reading 
	 * 
	 * @param aeRTSEx 
	 */
	private void validateOdometer(RTSException aeRTSEx)
	{
		String lsOdmtrRdg = gettxtOdometerReading().getText().trim();
		String lsVehMdlYr = gettxtVehicleModelYear().getText().trim();
		String lsVehTon = gettxtVehicleTonnage().getText().trim();
		String lsGrossWt = "";
		if (cbMFRecordFound)
		{
			lsGrossWt = lsGrossWt + caRegData.getVehGrossWt();
		}

		if (gettxtOdometerReading().isEnabled())
		{
			// defect 10003 
			// Cannot determine if Odometer Required on No Record Found
			if (lsOdmtrRdg.length() == 0)
			{
				if (cbOdmtrReqd)
				{
					aeRTSEx
						.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_FIELD_ENTRY_INVALID),
					//150),
					gettxtOdometerReading());
				}
			}
			else if (
				!TitleClientUtilityMethods.isValidOdometerReading(
					lsOdmtrRdg))
			{
				aeRTSEx
					.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				//150),
				gettxtOdometerReading());
			}
			// Cannot dispute EXEMPT on No Record Found
			// Do not have Gross Weight 
			else if (cbMFRecordFound || (!(lsOdmtrRdg.equals(EXEMPT))))
			{
				if (TitleClientUtilityMethods
					.isInvalidOdometerReadingBasedOnTIMA(
						lsVehMdlYr,
						lsVehTon,
						lsGrossWt,
						lsOdmtrRdg))
				{
					aeRTSEx.addException(
					//new RTSException(711),
					new RTSException(
						ErrorsConstant
							.ERR_NUM_ODOMETER_INVALID_BASED_ON_TIMA),
						gettxtOdometerReading());
				}
			}
			// end defect 10003 
		}
	}

	/**
	 * Validate Owner Id
	 * 
	 * @param aeRTSEx
	 */
	private void validateOwnrId(RTSException aeRTSEx)
	{
		if (!gettxtOwnerId().isEmpty())
		{
			String lsOwnerId = gettxtOwnerId().getText();
			int liOwnerId = Integer.parseInt(lsOwnerId);

			if (liOwnerId == 0)
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtOwnerId());
			}
			else
			{
				if (liOwnerId > TitleConstant.MAX_POS_DEALERID
					&& lsOwnerId.length()
						!= TitleConstant.REQD_MF_OWNERID_LENGTH)
				{
					aeRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtOwnerId());
				}
			}
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
	 * Validate Vehicle Information
	 * 
	 * @param aeRTSEx 
	 */
	private void validateVehicleInfo(RTSException aeRTSEx)
	{
		// defect 10127 
		// String lsVehModlYr = gettxtVehicleModelYear().getText().trim();
		// end defect 10127 
		String lsVehMk = CommonConstant.STR_SPACE_EMPTY;
		String lsBodyType = CommonConstant.STR_SPACE_EMPTY;
		String lsVehTon = gettxtVehicleTonnage().getText().trim();

		if (getcomboVehicleMake().isVisible())
		{
			if (getcomboVehicleMake().getSelectedIndex() > -1)
			{
				lsVehMk =
					(String) getcomboVehicleMake().getSelectedItem();
				lsVehMk =
					lsVehMk
						.substring(
							0,
							lsVehMk.indexOf(CommonConstant.STR_DASH))
						.trim();
			}
			else
			{
				lsVehMk = CommonConstant.STR_SPACE_EMPTY;
			}
		}
		else
		{
			lsVehMk = gettxtVehicleMake().getText().trim();
		}

		if (getcomboBodyType().isVisible())
		{
			if (getcomboBodyType().getSelectedIndex() > -1)
			{
				lsBodyType =
					(String) getcomboBodyType().getSelectedItem();
				lsBodyType =
					lsBodyType
						.substring(
							0,
							lsBodyType.indexOf(
								CommonConstant.STR_DASH))
						.trim();
			}
			else
			{
				lsBodyType = CommonConstant.STR_SPACE_EMPTY;
			}
		}
		else
		{
			lsBodyType = gettxtVehicleBodyType().getText().trim();
		}

		// defect 10003
		// Use ErrorsConstant
		// defect 10127 
		//		int liVehModYr = 0;
		// 
		//		try
		//		{
		//			liVehModYr = Integer.parseInt(lsVehModlYr);
		//		}
		//		catch (NumberFormatException aeNFE)
		//		{
		//			aeRTSEx
		//				.addException(
		//					new RTSException(
		//						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
		//			//150),
		//			gettxtVehicleModelYear());
		//		}
		//
		// defect 6153
		// Verify vehicle model year is 1880 and currYr + 2
		if (gettxtVehicleModelYear().isEnabled())
		{
			if (!CommonValidations
				.isValidYearModel(gettxtVehicleModelYear().getText()))
			{
				//150 to 2006
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_MODEL_YEAR_NOT_VALID),
					gettxtVehicleModelYear());
			}
		}
		// end defect 6153
		// end defect 10127 

		if (lsVehMk.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			if (getcomboVehicleMake().isVisible())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getcomboVehicleMake());
			}
			else
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtVehicleMake());
			}
		}
		if (lsBodyType.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			// defect 9750 
			// reference getcomboBodyType only if visible
			if (getcomboBodyType().isVisible())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getcomboBodyType());
			}
			else
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtVehicleBodyType());
			}
			// end defect 9750 
		}

		// defect 10689 
		if (isMajorColorReqdButNotSelected()
				|| isMinorColorSelectedMajorColorNotSelected())
		{
			aeRTSEx.addException(
				new RTSException(150),
				getcomboMajorColor());
		}
		else if (isMajorColorSameAsMinorColor())
		{
			aeRTSEx.addException(
				new RTSException(160),
				getcomboMinorColor());
		}
		// end defect 10689 

		if (gettxtVehicleTonnage().isEnabled() && lsVehTon != null)
		{

			try
			{
				// defect 6587
				// Add if statement to verify tonnage. 
				// Validating the tonnage from rts_comm_veh_wts 
				//	table.
				double ldVehTon = ZEROTON;
				ldVehTon = Double.parseDouble(lsVehTon);

				if (ldVehTon != ZEROTON)
				{
					if (gettxtVehicleTonnage().isEnabled()
						&& TitleClientUtilityMethods.isVehTonInvalid(
							lsVehTon))
					{
						aeRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_VEH_TONNAGE_INVALID),
							gettxtVehicleTonnage());
					}
				}
				// end defect 6587					
			}
			catch (NumberFormatException aeNFE)
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtVehicleTonnage());
			}

		}
		// end defect 10003 
		
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
					aeRTSEx.addException(
							new RTSException(ErrorsConstant.ERR_NUM_MODEL_YEAR_NOT_VALID),
							gettxtReplicaVehicleModelYear());
				}
				if (gettxtReplicaVehicleMake().isEmpty())
				{
					aeRTSEx.addException(
							new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtReplicaVehicleMake());
				}
			}
		}
		// end defect 11368 

	}

	/**
	 * Verify CCO Issue Date 
	 * 
	 */
	private void verifyCCOIssueDate()
	{
		if (getradioDuplicate().isSelected())
		{
			int liCCOIssueDate =
				caVehInqData
					.getMfVehicleData()
					.getTitleData()
					.getCcoIssueDate();

			if (liCCOIssueDate != 0)
			{
				RTSDate laCCOIssueDate =
					new RTSDate(RTSDate.YYYYMMDD, liCCOIssueDate);

				if (laCCOIssueDate.showMsgForRecentCCO())
				{
					displayError(
						ErrorsConstant
							.ERR_NUM_CCO_WITHIN_SPECIFIED_INTERVAL);
				}
			}
		}
	}

}  //  @jve:visual-info  decl-index=0 visual-constraint="8,1"

