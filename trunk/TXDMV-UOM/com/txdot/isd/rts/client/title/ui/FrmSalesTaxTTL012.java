package com.txdot.isd.rts.client.title.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.*;
import java.util.HashSet;
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
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * 
 * FrmSalesTaxTTL012.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validation updated
 * T Pederson   04/30/2002  Don't allow spaces to be entered in input
 *							field
 * T Pederson   05/07/2002  Removed panel around tax permit number so 
 * 							text could be disabled when input field is 
 * 							disabled
 * T Pederson   05/08/2002  Clear red error in input fields when a 
 * 							sales tax category is selected
 * MAbs			06/25/2002	Cleared red after deleting trade-in 
 *							allowance. 
 *							defect 4327
 * J Rue		06/27/2002	verifySalesTaxDate, added code 
 *							to use the DlrTransDt from DTA008 screen if 
 *							Dealer title
 *							defect 4315 
 * J Rue		06/27/2002	add test if IMNCo exist, add to 
 *							frame display
 *							defect 4379 
 * J Rue		06/27/2002 	add test if Rebate exist, add 
 *							to frame display. Method DoDlrTtl()
 *							defect 4367  
 * MAbs			07/02/2002	Changing error message
 * 							defect 4327 
 * J Rue		07/09/2002	set else return value to true 
 *							if date validation fails, method 
 *							validateSalesTaxDate()
 *							defect 4446 
 * J Rue		07/10/2002	correct logic for IMCNo process 
 *							add method IMCNoReqd, modify doDlrTtl()
 *							defect 4446 
 * B Arred/JRue	07/11/2002	Change message to errmsgno=658 
 *							for SaleTaxDt > CurrDate. method 
 *							verifySalesTaxDate() and 
 *							validateSalesTaxDate().
 *							defect 4435 
 * B Arred/JRue 07/12/2002	Corrected code fixed yesterday 
 *							from today's date to effective date
 *							defect 4435 
 * J Rue 		07/15/2002	Added parameter to 
 *							RTDDate(int, int) to return cotrrected RTS 
 *							date.
 *							modify verifySalesTaxDate()
 *							defect 4435 
 * B Arredondo	07/24/2002	Aligned the Rebate with the Sales Price 
 * 							so they can be even.
 *							defect 4478 
 * J Rue		07/25/2002	Declare IMCNo as a long and 
 *							process correctly. method doDlrTtl().
 *							defect 4437 
 * B Arredondo	08/02/2002 	Added code to display the 
 *							trade-in information when needed in 
 *							Sales/Use, Leasing and Even Trade.  
 *							add itemDTAStatedChange.
 *							defect 4512 
 * B Arredondo	08/07/2002	Added code to not display '0.00' 
 *							when there is no information in the Rebate 
 *							field. Changed code in method doDltTTl. 
 *							Rebate field is blank when there is no 
 *							information.
 *							defect 4568	
 * J Rue		08/07/2002	Remove/edit that only displays 
 *							RTSException 658 once. 
 *							modify actionPerformed()
 *							defect 4552	
 * B Arredondo	08/16/2002	Went into composition editor to 
 *							correct alignment of "rebate):", changed 
 *							from 10 to Right Align.
 *							defect 4478 
 * B Arredondo	08/19/2002	Added code in method 
 *							itemDTAStateChange to correct the enabling 
 *							of fields that should be disabled.
 *							defect 4611 
 * B Arredondo 	08/19/2002	Changed code in 
 *							method doDlrTtl to check for nullpointer 
 *							exception for Rebate field.
 *							defect 4609/Duplicate 4631 
 * SGovin		08/22/2002	Added code to method 
 *	/BArred					verifySalesTaxDate and actionPerformed to 
 *							allow the date being entered to be in the 
 *							range from 5/01/1941 to more than one year 
 *							ago from the EffDate and get error but still 
 *							continue.
 *							defect 4640 
 * JRue/BArred	09/30/2002	Modified code in the class and 
 *							methods doDlrTtl() and itemDTAStateChange() 
 *							so the TradeIn Allowance and Sales Price 
 *							would appear after selecting another 
 *							category and going back to Sales/Use. Do 
 *							not have to reenter the allowance or sales 
 *							price.
 *							defect 4716 
 * JRue/BArred	10/01/2002	Modified code in class 
 *							FrmSalesTaxTTL012. Defined dTrdIn and 
 *							dSlsPrc in class to be used in multiple 
 *							methods. Modified code in method doDlrTtl() 
 *							so the private fields woud not be local to 
 *							the method. Also added code in method
 *							itemDTAStateChange() so that the sales price 
 *							and trade-in allowance would display even 
 *							when another category is chosen but then 
 *							sales/use is chosen again. The amounts will 
 *							display as it had done once entering into 
 *							the screen.
 *							defect 4660 
 * JRue/BArred	10/02/2002	When doing DTA disk, the date 
 *							being used was the date for the first record 
 *							and not the date of each record. Usually the 
 *							dates for all records are of one date but 
 *							there were disks with multiple records and 
 *							different dates. Correction will retrieve 
 *							the date for each record. Modified code to 
 *							determine the current record being process, 
 *							using that DlrTransDate for verification. 
 *							Method (new) findCurrRec(), 
 *							verifySalesTaxDate(), doDlrTtl(), 
 *							itemDTAStateChange.
 *							defect 4766 
 * J Rue		11/15/2002	Add isVisible() == False to 
 *							ensure the actionPerformed() is processed 
 *							once. method actionPerformed()
 *							defect 4896 
 * B Arredondo	12/16/2002	Made changes for the 
 *							user help guide so had to make changes
 *							in actionPerformed().
 *							defect 5147 
 * J Rue		01/14/2003	Changed indexing to method 
 *							findCurrRec() so the variable is updated 
 *							before it is returned to 
 *							verifySalesTaxDate().
 *							defect 5166 
 * B Arredondo	08/01/2003	Screen changes to add Vehicle 
 *							AbstractValue and depricate method
 *							getchkVehSoldinTx().
 *							defect 6448 
 * J Rue/B Arredondo
 *				08/07/2003	Set logic to capture emission 
 *							data for TERP. method setData(), 
 * 							itemStateChanged(), setDataToDataObject(), 
 *							actionPerformed() 
 *							deprecate displayEmissionsCheckboxes(),
 *							setEmissionsCheckbox()
 *							new method displayVehicleValue()
 *							defect 6448 
 * B Arredondo	08/08/2003	Modified visual composition 
 *							to set tabs to include Vehicle AbstractValue
 *							defect 6448 
 * B Arredondo	08/27/2003	Inserted Charge Sales Tax 
 *							Emission Fee checkbox along with the 
 *							Vehicle AbstractValue which replaced Sold In TX
 *							checkbox. Code was modified to include 
 *							Vehicle AbstractValue and to delete sold in tx and
 *							sales tax emission checkboxes.
 *							Modified methods: 
 *							displayEmissionsCheckboxes(), 
 *							handleSalesPriceDisplay(), 
 *							itemStateChanged(), setData() and 
 *							setDatatoDataObject(), 
 *							setEmissionsCheckbox() and actionPerformed()
 *							Created methods: enableSalesTaxEmission() 
 *							and enableVehicle().
 *							defect 6448 
 * B Arredondo	09/08/2003	Made changes to methods actionPerformed(), 
 *							itemstatechanged(), setdata(), 
 *							setdatatodata(), displayVehicleValue(), 
 *							enableVehicleValue()
 * B Arredondo	09/16/2003	add	determineVehicleValue()	
 * 							modify itemStateChanged()
 * 							defect 6565 
 * B Arredondo	09/29/2003	Added clearAllColor() to method 
 *							setEmissionsCheckbox() to clear the red in 
 *							vehicle value input field after emission 
 *							checkbox is rechecked.
 *							defect 6606 Ver 5.1.6
 * T Pederson   11/24/2004	Changed handleSalesPriceDisplay to clear 
 * 							sales tax input fields only if fields are
 * 							being disabled. Added check to 
 * 							itemStateChanged to keep trade in fields 
 *							enabled if an amount is in the input field.
 * 							modify handleSalesPriceDisplay(), 
 * 							itemStateChanged()
 *							defect 6594 Ver 5.2.2
 * T Pederson   12/14/2004	Changed itemStateChanged to not go to  
 * 							itemDTAStateChange on keyboard entry DTA.
 * 							modify itemStateChanged()
 *							defect 6220 Ver 5.2.2
 * J Rue		03/10/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/23/2005	Change ScreenConstant from DTA008a to DTA008
 * 							modify verifySalesTaxDate()
 * 							defect 6963,7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/14/2005	Remove unused variable
 * 							Deprecate unused methods
 * 							deprecated 
 * 							populateExemptReasonsDropDownWithEmptyString
 * 							defect 7898 Ver 5.2.3
 * J Rue		05/24/2005	Check for "-" precedes a number 
 * 							(negitive numbers) 
 * 							Clean up code
 * 							modify actionPerformed()
 * 							defect 6777, 7898 Ver 5.2.3	
 * J Rue		06/01/2005	Replace checkForNegativeNum 
 * 							with isNegative
 * 							modify actionPerformed() 
 * 							defect 6777 Ver 5.2.3
 * S Johnston	06/23/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							defect 8240 Ver 5.2.3 
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/19/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3    
 * J Rue		12/02/2005	Move constants to class level (case stmt)
 * 							modify itemDTAStateChange()
 * 							defect 7898 Ver 5.2.3
 * J Rue		12/13/2005	Add compareTo and set Trade-In dispaly to 
 *							false if Trade-In amount is null or less 
 *							than/equal to zero dollars. 
 *							This will set Additional Trade-In to 
 *							unchecked and disabled.
 * 							modify itemDTAStateChange()
 * 							defect 7898 Ver 5.2.3
 * J Rue		12/13/2005	Check if Trade-In year is > 0, populate with
 * 							Trade-In year value.
 * 							modify itemDTAStateChange()
 * 							defect 7898 Ver 5.2.3
 * T Pederson	12/14/2005	Added a temp fix for the JComboBox problem.
 * 							modify populateExemptReasonsDropDown()  
 * 							and populateSalesTaxCatDropDown().
 * 							defect 8479 Ver 5.2.3  
 * T. Pederson	12/30/2005	Removed window methods (not being used).
 * 							delete windowActivated(), windowClosed(),
 * 							windowClosing(), windowDeactivated(),
 * 							windowDeiconified(), windowIconified() and 
 * 							windowOpened().
 * 							defect 8494 Ver 5.2.3
 * J Rue		01/06/2006	Capitalize title text.
 * 							add SALES_TAX_INFO
 * 							defect 8447 Ver 5.2.3 
 * K Harrell	01/17/2006	Reset values in data objects in case return
 * 							from next screen. Add'l Java 1.4 cleanup.
 * 							add ZERO_DOLLAR 
 * 							modify setDataToDataObject()
 * 							defect 8511 Ver 5.2.2 Fix 8
 * K Harrell	01/17/2006	Convert I/O to 1/0 in Trade-In Vehicle VIN
 *							modify focusLost(),gettxtTradeInVehicleVIN()  
 *							defect 8522 Ver 5.2.2 Fix 8
 * K Harrell	02/10/2006	Modify to correctly handle DTA, add'l Java
 * 	J Rue					1.4 Work
 * 							itemDTAStateChanged()
 * 							defect 7898 Ver 5.2.3 
 * T Pederson	09/28/2006	Changed call for converting vin i's and o's 
 * 							to 1's and 0's to combined non-static 
 * 							method convert_i_and_o_to_1_and_0(). 
 * 							delete getBuilderData()
 * 							modify focusLost()
 * 							defect 8902 Ver Exempts
 * K Harrell	10/29/2006	Class Cleanup, including ZERO_DOLLAR, comments
 * 							Initialize VehMiscData EmissionSalesTax  
 * 							delete populateExemptReasonsDropDownWithEmptyString(),
 * 								window* methods (see T Pederson 12/30/2005)
 * 							modify setDataToDataObject()
 * 							defect 8900 Ver Exempts
 * K Harrell	04/06/2007	CommonValidations.convert_i_and_o_to_1_and_0() 
 * 							call is now static
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/06/2008	Modify for V21 Deliquent Penalty
 * 							add cbVehMdlYrApplicable,cbDTATrans,
 * 							 caSavedPenaltyFee, cbInitSet 
 * 							add SALES_TAX_DATE, TXT_EXEMPT, TXT_DEALER,
 * 							 TXT_GENERAL_PUBLIC, TXT_MILITARY, 
 * 							 MILITARY_CD, DEALER_CD, GENERAL_PUBLIC_CD,
 * 							 EXEMPT_CD, MAX_PENALTY_YR_DELTA, 
 * 							 MAX_DAYS_NO_PENALTY 
 * 							add ivjpnlDelinquentTransfer,ivjstclblPenaltyFee,
 * 							 ivjtxtPenaltyFee,ivjpnlOwnerType,ivjradioDealer,
 * 							 ivjradioExempt,ivjradioGeneralPublic,
 * 							 ivjradioMilitary, ivjstclblDateOfAssignment,
 * 							 ivjstclblSalesTaxDate,ivjradioNoneSelected
 * 							 get/set methods
 * 							add displayDelinquentTransfer(),
 * 							 calcTrnsfrPnltyFee(), isSalesTaxDateValid(),
 * 							 isDelqntPnltyFeeApplicable(), keyPressed(),
 * 							 keyReleased(),  isSupOvrdRqdforPnltyFee(),
 * 							 validatePenaltyFee(), setVehMdlYrApplicable()
 * 							delete getEtched(),getpnlGrpSalesTaxDate(),
 * 							 validateSalesTaxDate()
 * 							modify actionPerformed(), doDlrTtl(),
 * 							 itemStateChanged(), 
 * 							 getFrmSalesTaxTTL012ContentPane1(),
 * 							 initialize(), setData(), 
 * 							 setDataToDataObject(),gettxtSalesTaxDate()
 * 							defect 9584 Ver POS Defect A 
 * K Harrell	06/12/2008	System.out.println only if !Production. Only
 * 							recalc on button selected. 
 * 							modify displayDelinquentTransfer(), 
 * 							  calcTrnsfrPnltyFee(), itemStateChanged()  
 * 							defect 9584 Ver Defect POS A
 * K Harrell	06/25/2008	Modifications for determining Exemptions for
 * 							Delinquent Penalty Fee
 * 							add TRK_LEQ_ONE, TRK_GT_ONE, PASS, PASS_TRK,
 * 							 MTRCYCLE, ANTIQUE_CLASSIC_ENT_CD, 
 * 							 NOT_REGISTERED_ENT_CD
 * 							add ivjlblDelqntPnltyFeeMsg, 
 * 							 ivjlblDelqntPnltyDaysMsg,
 * 							 ivjlblPnltyExmptDescMsg  
 * 							add getDelqntPnltyFeeMsg(),
 * 							 getlblDelqntPnltyDaysMsg(),
 * 							 getlblPnltyExmptDescMsg(), isRegExempt(), 
 * 							 isDocTypeExempt(), isOwnrEvidCdExmpt(), 
 * 							 isRegClassExempt(), setDelqntPnltyFeeExempt()
 * 							delete setVehMdlYrapplicable() 
 * 							modify isVehModlYrExmpt(), isTransCdExmpt(),
 * 							 keyPressed(), setDataToDataObject(),
 * 							 isSalesTaxDateValid() 
 * 							defect 9724 Ver Defect POS A
 * K Harrell	06/26/2008	Only show DelqntMsg if fee !=0
 * 							modify calcTrnsfrPnltyFee()
 * 							defect 9724 Ver Defect POS A  
 * K Harrell	07/09/2008	Exception coding for Date of Assignment prior
 * 							to 2008 
 * 							add cbPriorDelqntPnltyApplies,cbInitExmpt,
 * 							 NEW_DELQNT_FEE_START_DATE, PRIOR_PNLTY_FEE,
 * 							 ANTIQUE_CLASSIC_ENT_CD, NOT_REGISTERED_ENT_CD,
 *							 NOT_REGISTERED_ENT_CD, EXEMPT_AGENCY_ENT_CD, 							
 * 							 PENALTY_FEE   
 * 							modify displayDelinquentTransfer(), 
 * 							 calcTrnsfrPnltyFee(), 
 * 							 setDelqntPnltyFeeExmpt(),
 * 							 isSalesTaxDateValid(), 
 * 							 validatePenaltyFee()			  
 *   						defect 9737 Ver MyPlates_POS
 * K Harrell	08/14/2008	Display Information Msg that the Buyer Tag 
 * 							Fee will not be charged if Date of Assignment
 * 							prior to BuyerTag Start Date.
 * 							add NO_BUYER_TAG_MSG
 * 							modify actionPerformed() 
 * 							defect 9799 Ver MyPlates_POS
 * J Rue		09/16/2008	Set VehTrdIn for all scenarios
 * 								(null, empty, 0.0, > 0.0)
 * 							modify actionPerformed()
 * 							defect 8682 Ver Defect_POS_B 
 * J Rue		09/22/2008	Add "end" to // end defect 8682
 * 							modify actionPerformed()
 * 							defect 8682 Ver Defect_POS_B
 * J Rue		06/25/2009	Create a method to manage Trade In Info
 * 							Move defect 8682 changes from 
 * 							actionPerformed() to manageTradeInInfo()
 * 							add manageTradeInInfo() 
 * 							modify actionPerformed()
 * 							defect 10100 Ver Defect_POS_F
 * J Rue		07/14/2009	Use RTSInputField.isEmpty() to check for size
 * 							add isTradeInValueProvided()
 * 							modify manageTradeInInfo(),focusLost(), 
 * 							 handleSalesTaxCategorySelection(), 
 * 							  validateTradeInInfo(),setDataToDataObject()
 * 							defect 10100 Ver Defect_POS_F
 * J Rue		07/16/2009	Sort class members. Add comments to methods
 * 							Replace error message number 150 and 733 with
 * 							ErrorsConstants.
 * 							modify actionPerformed(), manageTradeInInfo()
 * 							defect 10100 Ver Defect_POS_F
 * K Harrell	07/26/2009	Implement new logic for calculation of 
 * 							 Delinquent Penalty Fee for Dealer Seller 
 * 							 Financed. Action vs. ItemState Listener
 * 							 on SalesTaxCat & ExemptReason combo. 
 * 							 Sorted methods.  
 * 							add cbDealerFinancedSale, 
 * 							 DEALER_SELLER_FINANCED_CD
 * 							add isDealerFinancedSale(), 
 * 							  handleSalesTaxCategorySelection(), 
 * 							 handleTaxExemptSelection() 
 * 							delete cbVisible, cbIsVisible 
 * 							modify setData(), itemStateChanged() 
 * 							defect 10134 Ver Defect_POS_F
 * K Harrell	08/18/2009	Removed implementation of defect 10134.
 * 							retain actionListener on comboBox 
 * K Harrell	09/09/2009 	Restored to itemListener on 
 * 							 SalesTaxCategory comboBox
 * 							modify setData()
 * 							defect 10134 Ver Defect_POS_F
 * K Harrell	12/16/2009	DTA Cleanup
 * 							add caDlrTtlData, csTransCd
 * 							delete findCurrRec() 
 * 							modify verifySalesTaxDate(),doDlrTtl(), 
 * 							 verifySalesTaxDate()
 * 							defect 10290 Ver Defect_POS_H   
 * K Harrell	07/01/2010	Re-Implement SB 5210015 
 * 							add cbDealerFinancedSale, 
 * 							 DEALER_SELLER_FINANCED_CD
 * 							add isDealerFinancedSale(), 
 * 							  handleSalesTaxCategorySelection(), 
 * 							 handleTaxExemptSelection() 
 * 							modify setData(), calcTrnsfrPnltyFee()
 * 							defect 10518 Ver 6.5.0 
 * K Harrell	09/16/2010	add determineVehValueDisplay() to  
 * 							 handleSalesTaxCategorySelection()
 * 							modify handleSalesTaxCategorySelection(), 
 * 							 itemStateChanged()
 * 							defect 10603 Ver 6.5.0 
 * Min Wang		09/20/2010 	Accommodate new max lengh of input field for 
 * 							sales price.
 * 							modify SALES_PRICE_MAX_LEN
 * 							defect 10596 Ver 6.6.0
 * Min Wang		10/04/2010 	modify ONEMILLION
 * 							defect 10596 Ver 6.6.0
 * Min Wang		10/26/2010	delete ONEMILLION
 * 							add ONEHUNDREDMILLION
 * 							modify actionPerformed()
 * 							defect 10596 Ver 6.6.0
 * T Pederson	01/07/2011  Added default for sales tax penalty	
 * 							percent based on sale tax date.  Added 
 * 							checkbox for military applied to sales tax
 * 							penalty rules.
 * 							add THIRTY, SIXTY, NINETY, MILITARY,
 * 							MODIFIED_SALES_TAX_PENALTY_REASON,
 * 							SLS_TAX_PNLTY_DFLT_TO, ivjchkMilitary,
 * 							ivjlblSalesTaxPnltyFeeMsg, ciDefaultPnltyPer 
 * 							add displaySalesTaxPenaltyPercentDefault(),
 * 							isSupOvrdRqdforSalesTaxPnltyPrcnt(),
 * 							calcDelinquentDays(), getchkMilitary, 
 * 							getlblSalesTaxPnltyFeeMsg
 * 							modify actionPerformed(), calcTrnsfrPnltyFee()
 * 							handleSalesPriceDisplay(), itemStateChanged(),
 * 							recalcForSalesTaxDate(), doDlrTtl()
 * 							defect 10683 Ver 6.7.0
 * K Harrell	02/15/2011	Delinquent Penalty not set for DTA on 
 * 							initial display. 
 * 							modify setData() 
 * 							defect 10752 Ver 6.7.0
 * K Harrell	10/13/2011	Do not show Delinquent Penalty as recalculated
 * 							if no change or if uncalculated and results in 0.
 * 							modify actionPerformed() 
 * 							defect 10570 Ver 6.9.0
 * K Harrell	10/21/2011	Auto-select Exempt/Off-Highway for ATV/ROV
 * 							add selectSalesTaxCat(),handleATV_ROV(), 
 * 							 selectSalesTaxExemptDesc()
 * 							modify doDlrTtl(), setData() 
 * 							defect 11047 Ver 6.9.0   
 * K Harrell	10/26/2011	Correct Validations for $$ fields 
 * 							add MAX_SALES_PRICE, MAX_TRADE_IN,
 * 							 MAX_TAX_PAID_OTHER_STATE, MAX_VEH_VALUE, 
 *							delete ONEHUNDREDTHOUSAND, ONEHUNDREDMILLION
 *							modify TOT_REBATE_MAX_LEN,
 *							 TRADEIN_ALLWC_MAX_LEN, 
 *							 VEH_VALUE_MAX_LEN 
 * 							modify actionPerformed() 
 * 							refactor ivjtxtPaidOtherState, 
 * 							 gettxtPaidOtherState() to  
 * 							 ivjtxtTaxPaidOtherState, 
 * 							 gettxtTaxPaidOtherState()
 * 							defect 11081 Ver 6.9.0 
 * K Harrell	10/30/2011	add cbNewTtlTrnsfrPnltyExmptCd
 * 							delete ciRegExpMonths, NOT_REGISTERED_ENT_CD 
 *							modify isDelqntPnltyFeeApplicable(),
 *							 handleSalesTaxCategorySelection(),isRegExmpt(),
 *							 setData() 
 *							defect 11048 Ver 6.9.0 
 * K Harrell	11/13/2011	Do not modify Exempt Reason on New Resident
 * 							selection if initially disabled.  Restore 
 * 							prior selection after New Resident
 * 							add caLastSelected  
 * 							modify handleSalesTaxCategorySelection(), 
 * 							 calcTrnsfrPnltyFee(), itemStateChanged()  
 * 							defect 11048 Ver 6.9.0 
 * K Harrell	11/30/2011	modify MAX_DAYS_NO_PENALTY
 * 							defect 11161 Ver 6.9.0 
 * K Harrell	01/30/2012	Additional Trade-In for DTA inappropriately 
 * 							 selected when Trade-In specified for 
 * 							 Sales/Use (or Lease) & AddlTradeIn=0    
 * 							modify itemDTAStateChanged()  
 * 							defect 11267 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */
/**
 * This form is used to capture sales tax information used by fee calculations.
 * 
 * @version 6.9.0 	01/30/2012
 * @author Todd Pederson
 * @since 			06/21/2001 12:22:45
 */
public class FrmSalesTaxTTL012 extends RTSDialogBox implements
		ActionListener, FocusListener, ItemListener, KeyListener
{
	private Border etched = null;
	private JCheckBox ivjchkAdditionalTradeIn = null;
	private JCheckBox ivjchkChrgSalesTaxEmi = null;

	// defect 10683
	private JCheckBox ivjchkMilitary = null;
	// end defect 10683

	private JComboBox ivjcomboSalesTaxCategories = null;
	private JComboBox ivjcomboSalesTaxExemptDesc = null;
	private JComboBox ivjcomboSalesTaxPenaltyPercent = null;
	private ButtonPanel ivjECHButtonPanel = null;
	private JPanel ivjFrmSalesTaxTTL012ContentPane1 = null;
	private JLabel ivjlblDelqntPnltyDaysMsg = null;
	private JLabel ivjlblDelqntPnltyFeeMsg = null;
	private JLabel ivjlblPnltyExmptDescMsg = null;

	// defect 10683
	private JLabel ivjlblSalesTaxPnltyFeeMsg = null;
	// end defect 10683
	private JPanel ivjpnlDelinquentTransfer = null;
	private JPanel ivjpnlGrpSalesTaxEmissions = null;
	private JPanel ivjpnlGrpSalesTaxInfo = null;
	private JPanel ivjpnlGrpTradeinInfo = null;
	private JPanel ivjpnlOwnerType = null;
	private JRadioButton ivjradioDealer = null;
	private JRadioButton ivjradioExempt = null;
	private JRadioButton ivjradioGeneralPublic = null;
	private JRadioButton ivjradioMilitary = null;
	private JRadioButton ivjradioNoneSelected = null;
	private JLabel ivjstcLblCategories = null;
	private JLabel ivjstclblDateOfAssignment = null;
	private JLabel ivjstcLblExemptReasons = null;
	private JLabel ivjstclblPenaltyFee = null;
	private JLabel ivjstcLblPenaltyPercent = null;
	private JLabel ivjstcLblRebate = null;
	private JLabel ivjstclblSalesTaxDate = null;
	private JLabel ivjstcLblTaxPaidOtherState = null;
	private JLabel ivjstcLblTaxPermit = null;
	private JLabel ivjstcLblTotalRebateAmount = null;
	private JLabel ivjstcLblTradeIn = null;
	private JLabel ivjstcLblVehicleValue = null;
	private JLabel ivjstcLblVIN = null;
	private JLabel ivjstcLblYearMake = null;
	private RTSInputField ivjtxtIMCNo = null;

	// defect 11081
	private RTSInputField ivjtxtTaxPaidOtherState = null;

	// end defect 11081

	private RTSInputField ivjtxtPenaltyFee = null;
	private RTSDateField ivjtxtSalesTaxDate = null;
	private RTSInputField ivjtxtTotalRebateAmount = null;
	private RTSInputField ivjtxtTradeInVehicleMake = null;
	private RTSInputField ivjtxtTradeInVehicleVIN = null;
	private RTSInputField ivjtxtTradeInVehicleYear = null;
	private RTSInputField ivjtxtVehicleSalesPrice = null;
	private RTSInputField ivjtxtVehicleTradeInAllowance = null;
	private RTSInputField ivjtxtVehicleValue = null;
	private RTSButtonGroup caButtonGroup1;

	// Objects
	protected RTSMediator caMediator;

	// Dollar
	private Dollar caRebate = new Dollar("0.00");
	private Dollar caSavedPenaltyFee = null;
	private RTSDate caSavedSalesTaxDate = null;
	private Dollar caSlsPrc = new Dollar("0.00");
	private Dollar caTrdIn = new Dollar("0.00");
	private VehicleInquiryData caVehInqData = null;
	private VehMiscData caVehMiscData = null;

	// defect 10290
	private DealerTitleData caDlrTtlData = null;
	private String csTransCd = CommonConstant.STR_SPACE_EMPTY;
	// end defect 10290

	// boolean
	private boolean cb2008DelqntPnltyExmpt = false;
	private boolean cbAllowedToShowEmissions;
	private boolean cbDelqntPnltyFeeExmpt = false;
	private boolean cbDTATrans = false;
	private boolean cbInitCalc = true;
	private boolean cbInitExmpt = false;
	private boolean cbInitialTabOff = true;
	private boolean cbInitSet = true;
	private boolean cbNewSalesTaxDate = false;
	private boolean cbPriorDelqntPnltyApplies = false;

	// double
	private double cdTradeInAmt = 0.00;

	// int
	// defect 10683
	private int ciDefaultPnltyPer = 0;

	// private int ciPnltyPer = 0;
	// end defect 10683
	private int ciExmptCd = 0;
	private int ciSlsTxDt = 0;
	private int ciYr = 0;

	// long
	private long clIMCNo = 0l;

	// String
	private String csMk = CommonConstant.STR_SPACE_EMPTY;
	private String csSlsCat = CommonConstant.STR_SPACE_EMPTY;
	private String csTtlTrnsfrEntCd = EXEMPT_CD;
	private String csTtlTrnsfrPnltyExmptCd = CommonConstant.STR_SPACE_EMPTY;
	private String csVIN = CommonConstant.STR_SPACE_EMPTY;

	// Vector
	private Vector cvExemptReasons;
	private Vector cvSalesTaxCat;

	// defect 10518
	private boolean cbDealerFinancedSale = false;
	private final static String DEALER_SELLER_FINANCED_CD = "S";
	private final static int SELLER_FINANCED_SALE_CD = 16;

	// end defect 10518

	// defect 11048
	private boolean cbNewTtlTrnsfrPnltyExmptCd;
	private JRadioButton caLastSelected = null; 

	// private int ciRegExpMonths = 0;
	// private final static String NOT_REGISTERED_ENT_CD = "NR";
	// end defect 11048

	// Constant
	private final static String ADDL_TRADE_INS = "Additional Trade-In(s)";
	private final static String ANTIQUE_CLASSIC_ENT_CD = "ACE";
	private final static Dollar BASE_PNLTY_FEE = new Dollar("25.00");
	private final static Dollar BASE_PNLTY_FEE_DEALER = new Dollar(
			"10.00");
	private final static String CATEGORIES = "Categories";
	private final static String CHRG_SALES_TAX_EMISSION = "   Charge Sales Tax Emission Fee";
	private final static String DATA_MISSING_VCREG029 = "Data missing for IsNextVCREG029";
	private final static String DATE_OF_ASSIGMENT = "Date of Assignment/";
	private final static String DEALER_CD = "D";
	private final static String GENERAL_PUBLIC_CD = "P";
	private final static String EXEMPT_CD = "X";
	private final static String MILITARY_CD = "M";
	private final static String ERROR = "ERROR!";
	private final static int EVEN_TRADE = 5;
	private final static int EXEMPT = 4;
	private final static String EXEMPT_AGENCY_ENT_CD = "EXA";
	private final static String EXEMPT_REASON = "Exempt Reasons";
	private final static String FIELD_VALIDATION_ERR_MSG = "Field Validation - Trade-In Year Must "
			+ "Be 4 Digits, No Greater Than ";

	private final static String FIVE_PER = "5";
	private final static int GIFT = 3;
	private final static int IMCNO_MAX_LEN = 11;
	private final static int LEASE = 2;
	private final static String LEASING = "LEASING";
	private final static int MAKE_MAX_LEN = 4;

	// defect 11161 
	//private final static int MAX_DAYS_NO_PENALTY = 29;
	private final static int MAX_DAYS_NO_PENALTY = 30;
	// end defect 1161 

	private final static int MAX_PENALTY_YR_DELTA = 25;
	private final static String MILITARY = "Military";
	private final static int MIN_SALES_TAX_DATE = 19410501;
	private final static String MODIFIED_PENALTY_FEE_REASON = "Modified Penalty Fee";

	// defect 10683
	private final static String MODIFIED_SALES_TAX_PENALTY_REASON = "Modified Sales Tax Penalty Percent";
	// end defect 10683
	private final static String MTRCYCLE = "MTRCYCLE";
	private final static int NEW_DELQNT_FEE_START_DATE = 20080101;
	private final static int NEW_RESIDT = 1;
	private final static String NEWRESIDT = "NEW RESIDT";
	private final static String NO_BUYER_TAG_MSG = "Buyer Tag Fee will not be charged.  The Date of Assignment/Sales"
			+ " Tax Date is before "
			+ SystemProperty.getBuyerTagStartDate().getMMDDYYYY() + ".";
	private final static int NO_SLS_TAX_DATE_ERR = 0;

	// defect 11081
	// TOTALREBATEAMT DEC(9,2) 9,999,999.99
	private final static double MAX_TOTAL_REBATE = 9999999.99;

	// VEHSALESPRICE DEC(10,2) 99,999,999.99
	private final static double MAX_SALES_PRICE = 99999999.99;

	// VEHTRADEINALLOWNCE DEC(9,2) 9,999,999.99
	private final static double MAX_TRADE_IN = 9999999.99;

	// TAXPDOTHRSTATE DEC(8,2) 999,999.99
	private final static double MAX_TAX_PAID_OTHER_STATE = 999999.99;

	// VEHVALUE DEC(10,2); doesn't work; pretend DEC(9,2)
	// 9,999,999.99
	private final static double MAX_VEH_VALUE = 9999999.99;

	// private final static int TOT_REBATE_MAX_LEN = 8;
	private final static int TOT_REBATE_MAX_LEN = 10;

	// private final static int TRADEIN_ALLWC_MAX_LEN = 9;
	private final static int TRADEIN_ALLWC_MAX_LEN = 10;

	// Should be 11, but SalesTaxEmissions cannot handle
	// private final static int VEH_VALUE_MAX_LEN = 9;
	private final static int VEH_VALUE_MAX_LEN = 10;

	// end defect 11081

	private final static String PASS = "PASS";
	private final static String PASS_TRK = "PASS_TK";
	private final static String PENALTY_FEE = "Delinquent Transfer Penalty Fee ";
	private final static String PENALTY_PERCENT = "Penalty Percent:";
	private final static Dollar PRIOR_PNLTY_FEE = new Dollar("10.00");
	private final static String REBATE = "rebate):";
	private final static String SALES_PRICE = "Sales Price (less $";

	// defect 10596
	// private final static int SALES_PRICE_MAX_LEN = 9;
	private final static int SALES_PRICE_MAX_LEN = 11;
	// end defect 10596
	private final static String SALES_TAX_DATE = "Sales Tax Date:";
	private final static String SALES_TAX_INFO = "Sales Tax Info:";
	private final static String SALES_USE = "SALES/USE";

	private final static int SALESUSE = 0;
	private final static int SLS_TAX_DATE_ERR_GT_EFF_DATE = 2;
	private final static int SLS_TAX_DATE_ERR_GT_YR_AGO = 1;
	private final static String SLS_TAX_PNLTY_DFLT_TO = "Sales Tax Penalty defaulted to ";
	private final static int TAX_PAID_MAX_LEN = 9;
	private final static String TAX_PAID_OTHER_STATE = "Tax Paid Other State:";
	private final static String TAX_PERMIT_NO = "Tax Permit Number:";
	private final static String TEN_PER = "10";
	private final static String TRADE_IN = "Trade-In:";
	private final static String TRADE_IN_INFO = "Trade-In Info:";
	private final static String TRK_GT_ONE = "TRK>1";
	private final static String TRK_LEQ_ONE = "TRK<=1";
	private final static String TXT_DEALER = "Dealer";
	private final static String TXT_EXEMPT = "Exempt";
	private final static String TXT_GENERAL_PUBLIC = "General Public";
	private final static String TXT_MILITARY = "Military";
	private final static String VEHICLE_VALUE = " Vehicle AbstractValue:";
	private final static String VIN = "VIN:";
	private final static int VIN_MAX_LEN = 22;
	private final static String YEAR_MAKE = "Year/Make:";
	private final static int YEAR_MAX_LEN = 4;
	private final static String ZERO_PER = "0";

	// defect 10683
	private final static int THIRTY = 30;
	private final static int SIXTY = 60;
	private final static int NINETY = 90;
	// end defect 10683

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs
	 *            String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmSalesTaxTTL012 laFrmSalesTaxTTL012;
			laFrmSalesTaxTTL012 = new FrmSalesTaxTTL012();
			laFrmSalesTaxTTL012.setModal(true);
			laFrmSalesTaxTTL012.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmSalesTaxTTL012.show();
			Insets laInsets = laFrmSalesTaxTTL012.getInsets();
			laFrmSalesTaxTTL012.setSize(laFrmSalesTaxTTL012.getWidth()
					+ laInsets.left + laInsets.right,
					laFrmSalesTaxTTL012.getHeight() + laInsets.top
							+ laInsets.bottom);
			laFrmSalesTaxTTL012.setVisibleRTS(true);

		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmSalesTaxTTL012 constructor
	 */
	public FrmSalesTaxTTL012()
	{

		super();
		initialize();
	}

	/**
	 * FrmSalesTaxTTL012 constructor
	 * 
	 * @param aaParent
	 *            JFrame
	 */
	public FrmSalesTaxTTL012(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmSalesTaxTTL012 constructor
	 * 
	 * @param aaParent
	 *            JFrame
	 */
	public FrmSalesTaxTTL012(JFrame aaParent)
	{

		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{

			return;
		}
		try
		{
			RTSException leRTSEx = new RTSException();

			clearAllColor(this);

			// ENTER
			if (aaAE.getSource() == getECHButtonPanel().getBtnEnter())
			{
				// defect 9584
				boolean lbRecalc = false;
				boolean lbValidSalesTax = true;

				if (!isSalesTaxDateValid())
				{
					leRTSEx.addException(new RTSException(733),
							gettxtSalesTaxDate());
					lbValidSalesTax = false;
				}
				else if (verifySalesTaxDate() != NO_SLS_TAX_DATE_ERR)
				{
					if (verifySalesTaxDate() == SLS_TAX_DATE_ERR_GT_EFF_DATE)
					{
						leRTSEx.addException(new RTSException(658),
								gettxtSalesTaxDate());
						lbValidSalesTax = false;
					}
					else
					{
						// Over a year ago; Allow screen to go forward
						RTSException leRTSEx658 = new RTSException(658);
						leRTSEx658.displayError(this);
					}
				}
				if (lbValidSalesTax)
				{
					// defect 10570
					if (caSavedSalesTaxDate == null
							|| (caSavedSalesTaxDate.getAMDate() != gettxtSalesTaxDate()
									.getDate().getAMDate())
							|| gettxtPenaltyFee().isEmpty())
					{
						// Show only if:
						// - Was previously NOT calculated and is now NOT ZERO
						// - Was previously calculated and is now changed
						Dollar laCurrentDelPenaltyFee = caSavedPenaltyFee;
						recalcForSalesTaxDate();
						lbRecalc = isOwnerTypeSelected()
								&& (((laCurrentDelPenaltyFee == null && !caSavedPenaltyFee
										.equals(new Dollar(0))) || (laCurrentDelPenaltyFee != null && laCurrentDelPenaltyFee
										.compareTo(caSavedPenaltyFee) != 0)));

						// end defect 10570
					}
				}
				else
				{
					displayDelinquentTransfer();
				}
				// end defect 9584

				// defect 10100
				// Move all edits and field display functions to
				//
				// Calling handleTradeInDisplay() is handle in
				// FocusLost() and ItemStateChange() String lsVehTrdIn
				// = gettxtVehicleTradeInAllowance().getText().trim();
				// defect 8682
				// // Set TradeInDispaly and TradeInAll based on the
				// // VehTrdIn amount.
				// //if (lsVehTrdIn.length() > 0)
				// //{
				// double ldTradeInAll = 0.0;
				// try
				// {
				// ldTradeInAll = Double.parseDouble(lsVehTrdIn);
				// }
				// catch (NumberFormatException aeNFE)
				// {
				// ldTradeInAll = 0.0;
				// }
				// if (ldTradeInAll > 0.0)
				// {
				// handleTradeInDisplay(true);
				// }
				// else
				// {
				// handleTradeInDisplay(false);
				// }
				// //}
				// end defect 8682
				// end defect 10100

				// defect 10100
				if (!gettxtTotalRebateAmount().isEmpty())
				{
					// end defect 10100
					double ldTotalRebate;
					try
					{
						ldTotalRebate = Double
								.parseDouble(gettxtTotalRebateAmount()
										.getText());

						// defect 11081
						// if (ldTotalRebate >= ONEHUNDREDTHOUSAND
						// || UtilityMethods
						// .isNegative(gettxtTotalRebateAmount()
						// .getText()))
						if (ldTotalRebate > MAX_TOTAL_REBATE
								|| ldTotalRebate < 0)
						{
							// end defect 11081

							leRTSEx.addException(new RTSException(150),
									gettxtTotalRebateAmount());
						}
					}
					catch (NumberFormatException aeNFE)
					{
						gettxtTotalRebateAmount().setText(
								CommonConstant.STR_SPACE_EMPTY);
						leRTSEx.addException(new RTSException(150),
								gettxtTotalRebateAmount());
					}
				}

				// defect 10100
				if (!gettxtVehicleSalesPrice().isEmpty())
				{
					// end defect 10100
					double ldVehSalesPrice;
					try
					{
						ldVehSalesPrice = Double
								.parseDouble(gettxtVehicleSalesPrice()
										.getText());
						// defect 11081
						if (ldVehSalesPrice <= 0.0
								|| ldVehSalesPrice > MAX_SALES_PRICE)
						// || ldVehSalesPrice >= ONEHUNDREDMILLION
						// || UtilityMethods
						// .isNegative(gettxtVehicleSalesPrice()
						// .getText()))
						{
							// end defect 11081
							leRTSEx.addException(new RTSException(150),
									gettxtVehicleSalesPrice());
						}
					}
					catch (NumberFormatException aeNFE)
					{
						gettxtVehicleSalesPrice().setText(
								CommonConstant.STR_SPACE_EMPTY);
						leRTSEx.addException(new RTSException(150),
								gettxtVehicleSalesPrice());
					}
				}
				else if (gettxtVehicleSalesPrice().isEnabled())
				{
					leRTSEx.addException(new RTSException(150),
							gettxtVehicleSalesPrice());
					gettxtVehicleSalesPrice().requestFocus();
				}

				// defect 9584
				if (lbValidSalesTax && !cbDelqntPnltyFeeExmpt)
				{
					// Verify that either 0 or a multiple of
					// - 10 for dealer
					// - 25 else
					validatePenaltyFee(leRTSEx);
				}
				// end defect 9584

				// defect 6448
				// Gives error message that a value has to be inserted in
				// field
				// defect 10100
				if (!gettxtVehicleValue().isEmpty())
				{
					// end defect 10100
					double ldVehValue;
					try
					{
						ldVehValue = Double
								.parseDouble(gettxtVehicleValue()
										.getText());

						// defect 11081
						if (ldVehValue <= 0.0
								|| ldVehValue > MAX_VEH_VALUE)
						// || ldVehValue >= ONEHUNDREDMILLION
						// || UtilityMethods
						// .isNegative(gettxtVehicleValue()
						// .getText()))
						// end defect 11081
						{
							leRTSEx.addException(new RTSException(150),
									gettxtVehicleValue());
						}
					}
					catch (NumberFormatException aeNFE)
					{
						gettxtVehicleValue().setText(
								CommonConstant.STR_SPACE_EMPTY);
						leRTSEx.addException(new RTSException(150),
								gettxtVehicleValue());
					}
				}
				else if (gettxtVehicleValue().isEnabled())
				{
					leRTSEx.addException(new RTSException(150),
							gettxtVehicleValue());
					gettxtVehicleValue().requestFocus();
				}
				// end defect 6448

				// defect 10100
				if (isTradeInValueProvided())
				{
					// end defect 10100
					try
					{
						cdTradeInAmt = Double
								.parseDouble(gettxtVehicleTradeInAllowance()
										.getText().trim());

						// defect 11081
						// if (cdTradeInAmt >= ONEHUNDREDMILLION
						// || UtilityMethods
						// .isNegative(gettxtVehicleTradeInAllowance()
						// .getText()))
						if (cdTradeInAmt > MAX_TRADE_IN
								|| cdTradeInAmt < 0)
						// end defect 11081

						{
							// end defect 11081
							leRTSEx.addException(new RTSException(150),
									gettxtVehicleTradeInAllowance());
						}
					}
					catch (NumberFormatException aeNFE)
					{
						cdTradeInAmt = 0.0;
						gettxtVehicleTradeInAllowance().setText(
								CommonConstant.STR_SPACE_EMPTY);
						leRTSEx.addException(new RTSException(150),
								gettxtVehicleTradeInAllowance());
					}
				}
				// defect 10100
				if (!gettxtTaxPaidOtherState().isEmpty())
				{
					// end defect 10100

					double ldTaxPaidOtherState;
					try
					{
						ldTaxPaidOtherState = Double
								.parseDouble(gettxtTaxPaidOtherState()
										.getText());

						// defect 11081
						// if (ldTaxPaidOtherState >= ONEHUNDREDMILLION
						// || UtilityMethods
						// .isNegative(gettxtTaxPaidOtherState()
						// .getText()))
						if (ldTaxPaidOtherState > MAX_TAX_PAID_OTHER_STATE
								|| ldTaxPaidOtherState < 0)
						{
							// end defect 11081
							leRTSEx.addException(new RTSException(150),
									gettxtTaxPaidOtherState());
						}
					}
					catch (NumberFormatException aeNFE)
					{
						gettxtTaxPaidOtherState().setText(
								CommonConstant.STR_SPACE_EMPTY);
						leRTSEx.addException(new RTSException(150),
								gettxtTaxPaidOtherState());
					}
				}

				// defect 10100
				// Move all edits and field display functions to
				// validateTradeInInfo(). Perform display setups. Return
				// RTSException if errors were detected.
				validateTradeInInfo(leRTSEx);
				// if (gettxtTradeInVehicleYear().isEnabled())
				// {
				// String lsTradeinYr =
				// gettxtTradeInVehicleYear().getText().trim();
				//
				// if (CommonValidations
				// .isInvalidYearModel(lsTradeinYr))
				// {
				// RTSDate laToday = RTSDate.getCurrentDate();
				// leRTSEx.addException(
				// new RTSException(
				// RTSException.WARNING_MESSAGE,
				// FIELD_VALIDATION_ERR_MSG
				// + (laToday.getYear() + 2),
				// ERROR),
				// gettxtTradeInVehicleYear());
				// }
				// }
				// if (gettxtTradeInVehicleMake().isEnabled())
				// {
				// String lsTradeinMk =
				// gettxtTradeInVehicleMake().getText().trim();
				//
				// if (lsTradeinMk.length() < 1)
				// {
				// leRTSEx.addException(
				// new RTSException(150),
				// gettxtTradeInVehicleMake());
				// }
				// }
				// if (gettxtTradeInVehicleVIN().isEnabled())
				// {
				// String lsTradeinVin =
				// gettxtTradeInVehicleVIN().getText().trim();
				//
				// if (lsTradeinVin.length() < 1)
				// {
				// leRTSEx.addException(
				// new RTSException(150),
				// gettxtTradeInVehicleVIN());
				// }
				// }
				// // end Trade-In Info check *****************************
				// }
				//	
				// /**
				// * Determine whether to display Vehicle AbstractValue field.
				// */
				// private void determineVehValueDisplay()
				// {
				// // defect 6448
				// // Add condition to set vehicle value field
				// if (getchkChrgSalesTaxEmi().isSelected()
				// && getcomboSalesTaxCategories().getSelectedIndex() == 1)
				// {
				// displayVehicleValue();
				// }
				// else
				// {
				// enableVehicleValue(false);
				// }
				// end defect 6448
				// end Trade-In Info check *****************************
				// end defect 10100

				if (gettxtIMCNo().isEnabled())
				{
					String lsIMCNo = gettxtIMCNo().getText().trim();

					if (isInvalidIMCNo(lsIMCNo))
					{
						leRTSEx.addException(new RTSException(150),
								gettxtIMCNo());
					}
				}

				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				// cbIsVisible = false;

				// defect 9584
				if (lbRecalc)
				{
					new RTSException(2020).displayError(this);
					return;
				}

				if (isSupOvrdRqdforPnltyFee())
				{
					return;
				}
				// end defect 9584

				// defect 10683
				if (isSupOvrdRqdforSalesTaxPnltyPrcnt())
				{
					return;
				}
				// end defect 10683

				setDataToDataObject();

				// Do Fee Calculation
				CompleteTitleTransaction laCompTtlTrans = null;
				CompleteTransactionData laCompTtlTransData = null;
				try
				{
					laCompTtlTrans = new CompleteTitleTransaction(
							caVehInqData, csTransCd);

					laCompTtlTransData = laCompTtlTrans
							.doCompleteTransaction();
				}
				catch (RTSException aeRTSExc)
				{
					aeRTSExc.displayError(this);
					return;
				}
				// defect 9799
				// Show msg if Buyer Tag Selected but Sales Tax Date prior
				// to Buyer Tag Start Date
				if (laCompTtlTransData.getRegTtlAddlInfoData()
						.getChrgBuyerTagFeeIndi() == 1
						&& gettxtSalesTaxDate().getDate()
								.getYYYYMMDDDate() < SystemProperty
								.getBuyerTagStartDate()
								.getYYYYMMDDDate())
				{
					new RTSException(RTSException.INFORMATION_MESSAGE,
							NO_BUYER_TAG_MSG, "No Buyer Tag Fee")
							.displayError(this);
				}
				// end defect 9799

				// cbVisible = false;
				getController().processData(
						AbstractViewController.ENTER,
						laCompTtlTransData);
			}

			// defect 10518
			// SALES TAX CATEGORY Combo
			else if (aaAE.getSource() == getcomboSalesTaxCategories())
			{
				handleSalesTaxCategorySelection();
			}

			// SALES TAX EXEMPT DESC Combo
			else if (aaAE.getSource() == getcomboSalesTaxExemptDesc())
			{
				handleSalesTaxExemptSelection();
			}
			// end defect 10518

			// CANCEL
			else if (aaAE.getSource() == getECHButtonPanel()
					.getBtnCancel())
			{
				// cbVisible = false;
				getController().processData(
						AbstractViewController.CANCEL,
						getController().getData());
			}

			// HELP
			else if (aaAE.getSource() == getECHButtonPanel()
					.getBtnHelp())
			{
				// defect 10290
				// Use csTransCd
				if (csTransCd.equals(TransCdConstant.DTAORK)
						|| csTransCd.equals(TransCdConstant.DTANTK))
				{
					RTSHelp.displayHelp(RTSHelp.TTL012B);
				}
				else if (csTransCd.equals(TransCdConstant.DTANTD)
						|| csTransCd.equals(TransCdConstant.DTAORD))
				{
					RTSHelp.displayHelp(RTSHelp.TTL012C);
				}
				else if (csTransCd.equals(TransCdConstant.TITLE)
						|| csTransCd.equals(TransCdConstant.NONTTL)
						|| csTransCd.equals(TransCdConstant.REJCOR))
				{
					// end defect 10290
					if (UtilityMethods.isMFUP())
					{
						RTSHelp.displayHelp(RTSHelp.TTL012A);
					}
					else
					{
						RTSHelp.displayHelp(RTSHelp.TTL012D);
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
	 * Calculate number of days delinquent in filing title transfer
	 * 
	 * @return int This represents the number of days delinquent
	 */
	private int calcDelinquentDays()
	{
		int liDelinquentDays = 0;

		if (gettxtSalesTaxDate().getDate() != null
				&& gettxtSalesTaxDate().getDate().getYYYYMMDDDate() != 0)
		{
			RTSDate laEffDate = new RTSDate(RTSDate.YYYYMMDD,
					caVehInqData.getRTSEffDt());

			liDelinquentDays = laEffDate.getAMDate()
					- gettxtSalesTaxDate().getDate().getAMDate();
		}

		return liDelinquentDays;

	}

	/**
	 * Calculate Transfer Penalty Fee
	 * 
	 * @param abEnableFlag
	 */
	private void calcTrnsfrPnltyFee()
	{
		Dollar ldPenaltyFee = new Dollar(0.0);

		// defect 9724
		String lsDaysMsg = CommonConstant.STR_SPACE_EMPTY;
		String lsMsg = CommonConstant.STR_SPACE_EMPTY;
		String lsExmptMsg = CommonConstant.STR_SPACE_EMPTY;
		String lsSelection = CommonConstant.STR_SPACE_EMPTY;
		boolean lbNewSalesTaxDate = false;

		// defect 9737
		if (isDelqntPnltyFeeApplicable())
		{
			// defect 11048 
			boolean lbPrior = caLastSelected != null && caLastSelected.isEnabled();
			// end defect 11048 
			
			if (cbPriorDelqntPnltyApplies)
			{
				gettxtPenaltyFee().setText(PRIOR_PNLTY_FEE.toString());
				getstclblPenaltyFee().setEnabled(true);
				gettxtPenaltyFee().setEnabled(false);
				lsMsg = PENALTY_FEE
						+ "has been set for transactions prior to 2008.";

				if (!isOwnerTypeSelected()
						|| getradioMilitary().isSelected()
						|| cb2008DelqntPnltyExmpt)
				{
					getradioDealer().setSelected(cbDTATrans);
					// defect 11048 
					getradioGeneralPublic().setSelected(!cbDTATrans && !lbPrior);
					if (caLastSelected != null)
					{
						caLastSelected.setSelected(lbPrior);
					}
					// end defect 11048 
					cb2008DelqntPnltyExmpt = false;
				}
				else if (getradioExempt().isSelected())
				{
					gettxtPenaltyFee().setText("0.00");
					lsMsg = PENALTY_FEE
							+ "has been set for Exempt selection.";
				}
				cbInitCalc = false;
			}
			// end defect 9737
			else if (isSalesTaxDateValid())
			{
				// defect 10683
				int liDelinquentDays = calcDelinquentDays();
				// RTSDate laEffDate =
				// new RTSDate(
				// RTSDate.YYYYMMDD,
				// caVehInqData.getRTSEffDt());
				//
				// liDelinquentDays =
				// laEffDate.getAMDate()
				// - gettxtSalesTaxDate().getDate().getAMDate();
				// end defect 10683

				lsDaysMsg = "Delinquent Days: " + liDelinquentDays;
				if (caSavedSalesTaxDate == null
						|| caSavedSalesTaxDate.getAMDate() != gettxtSalesTaxDate()
								.getDate().getAMDate())
				{
					lbNewSalesTaxDate = true;
				}
				// defect 11048 
				if ((liDelinquentDays <= MAX_DAYS_NO_PENALTY || lbPrior)
						&& !isOwnerTypeSelected())
				{
					getradioDealer().setSelected(cbDTATrans);
					getradioGeneralPublic().setSelected(!cbDTATrans && !lbPrior);
					if (caLastSelected != null)
					{
						caLastSelected.setSelected(lbPrior);
					}
					// end defect 11048 
				}

				if (isOwnerTypeSelected())
				{
					if (getradioDealer().isSelected())
					{
						// defect 10518
						csTtlTrnsfrEntCd = isDealerFinancedSale() ? DEALER_SELLER_FINANCED_CD
								: DEALER_CD;
						// end defect 10518

						lsSelection = getradioDealer().getName();
					}
					else if (getradioMilitary().isSelected())
					{
						csTtlTrnsfrEntCd = MILITARY_CD;
						lsSelection = getradioMilitary().getName();
					}
					else if (getradioExempt().isSelected())
					{
						csTtlTrnsfrEntCd = EXEMPT_CD;
						lsSelection = getradioExempt().getName();
					}
					else
					{
						csTtlTrnsfrEntCd = GENERAL_PUBLIC_CD;
						lsSelection = getradioGeneralPublic().getName();
					}

					TitleTransferPenaltyFeeData laTtlTrnsfrPnltyFeeData = TitleTransferPenaltyFeeCache
							.getTitleTransferPenaltyFeeCache(
									csTtlTrnsfrEntCd, new RTSDate()
											.getYYYYMMDDDate(),
									liDelinquentDays);

					if (laTtlTrnsfrPnltyFeeData != null)
					{
						ldPenaltyFee = laTtlTrnsfrPnltyFeeData
								.getBaseTtlTrnsfrPnltyAmt();

						if (laTtlTrnsfrPnltyFeeData
								.getDelnqntPrortnIncrmnt() > 0
								&& liDelinquentDays > laTtlTrnsfrPnltyFeeData
										.getBegCalndrDaysCount())
						{
							int liMult = (liDelinquentDays - laTtlTrnsfrPnltyFeeData
									.getBegCalndrDaysCount())
									/ laTtlTrnsfrPnltyFeeData
											.getDelnqntPrortnIncrmnt();

							Dollar ldAddlPenaltyFee = laTtlTrnsfrPnltyFeeData
									.getAddlPnltyAmt().multiplyNoRound(
											new Dollar(liMult));

							ldPenaltyFee = ldPenaltyFee
									.addNoRound(ldAddlPenaltyFee);

						}
						if (ldPenaltyFee.compareTo(new Dollar(0.00)) == 0)
						{
							lsMsg = CommonConstant.STR_SPACE_EMPTY;
							cbInitCalc = false;
						}
						else if (cbInitCalc)
						{
							lsMsg = PENALTY_FEE
									+ "has been calculated for "
									+ lsSelection + " selection.";
							cbInitCalc = false;
						}
						else
						{
							lsMsg = PENALTY_FEE
									+ "has been recalculated for ";
							if (lbNewSalesTaxDate)
							{
								lsMsg = lsMsg + "new Sales Tax Date.";
							}
							// defect 10518
							else if (getradioDealer().isSelected()
									&& cbDealerFinancedSale != isDealerFinancedSale())
							{
								lsMsg = PENALTY_FEE
										+ "has been recalculated for new "
										+ (getcomboSalesTaxExemptDesc()
												.isEnabled() ? "Exempt Reason"
												: "Sales Tax Category")
										+ " selection.";
								cbInitCalc = false;
							}
							// end defect 10518

							else
							{
								lsMsg = lsMsg + lsSelection
										+ " selection. ";
							}
						}
					}

					else
					{
						lsMsg = PENALTY_FEE + "has been set for "
								+ lsSelection + " selection.";
						lsExmptMsg = CommonConstant.STR_SPACE_EMPTY;
						ldPenaltyFee = new Dollar(0.0);
					}
					gettxtPenaltyFee().setText(ldPenaltyFee.toString());
					gettxtPenaltyFee().setEnabled(
							!getradioExempt().isSelected());
					getstclblPenaltyFee().setEnabled(true);
					if (!getradioExempt().isSelected())
					{
						gettxtPenaltyFee().requestFocus();
					}
				}
				caSavedPenaltyFee = ldPenaltyFee;
				caSavedSalesTaxDate = gettxtSalesTaxDate().getDate();
			}
		}
		else
		{
			String lsReason = CommonConstant.STR_SPACE_EMPTY;
			TitleTransferPenaltyExemptCodeData laData = TitleTransferPenaltyExemptCodeCache
					.getTitleTransferPenaltyExemptCode(csTtlTrnsfrPnltyExmptCd);
			if (laData != null)
			{
				lsReason = laData.getTtlTrnsfrPnltyExmptDesc();
			}
			lsExmptMsg = lsReason;
			lsMsg = PENALTY_FEE + "Not Applicable. ";
		}
		getlblDelqntPnltyDaysMsg().setText(lsDaysMsg);
		getlblPnltyExmptDescMsg().setText(lsExmptMsg);
		getlblDelqntPnltyFeeMsg().setText(lsMsg);
		// end defect 9724
	}

	/**
	 * Determine whether to display Vehicle AbstractValue field.
	 */
	private void determineVehValueDisplay()
	{
		// defect 6448
		// Add condition to set vehicle value field
		if (getchkChrgSalesTaxEmi().isSelected()
				&& getcomboSalesTaxCategories().getSelectedIndex() == 1)
		{
			displayVehicleValue();
		}
		else
		{
			enableVehicleValue(false);
		}
		// end defect 6448
	}

	/**
	 * Display Delinquent Penalty
	 * 
	 */
	private void displayDelinquentTransfer()
	{

		if (!isDelqntPnltyFeeApplicable() && !cbInitSet)
		{
			getpnlDeliquentTransfer().setEnabled(true);
			gettxtPenaltyFee().setEnabled(false);
			if (isSalesTaxDateValid())
			{
				gettxtPenaltyFee().setText("0.00");
			}
			getradioDealer().setEnabled(false);
			getradioGeneralPublic().setEnabled(false);
			getradioExempt().setEnabled(false);
			getradioMilitary().setEnabled(false);
			caSavedPenaltyFee = new Dollar(0.00);
		}
		else
		{
			boolean lbEnable = isSalesTaxDateValid()
					&& !cbDelqntPnltyFeeExmpt;

			getpnlDeliquentTransfer().setEnabled(lbEnable);
			getradioDealer().setEnabled(lbEnable);
			getradioGeneralPublic().setEnabled(lbEnable);
			getradioExempt().setEnabled(lbEnable);
			// defect 9737
			getradioMilitary().setEnabled(
					lbEnable && !cbPriorDelqntPnltyApplies);
			// end defect 9737

			if (!lbEnable)
			{
				getradioNoneSelected().setSelected(!cbInitExmpt);
				getradioExempt().setSelected(cbInitExmpt);
				getstclblPenaltyFee().setEnabled(lbEnable);
				gettxtPenaltyFee().setEnabled(lbEnable);
				if (cbDelqntPnltyFeeExmpt)
				{
					gettxtPenaltyFee().setText("0.00");
				}
				else
				{
					gettxtPenaltyFee().setText(
							CommonConstant.STR_SPACE_EMPTY);
				}
			}
			else if (!isOwnerTypeSelected())
			{
				gettxtPenaltyFee().setText(
						CommonConstant.STR_SPACE_EMPTY);
				getradioDealer().setSelected(cbDTATrans);
			}
		}
	}

	/**
	 * This method will display the emissions checkbox if the criteria is met.
	 * If vehicle is diesel and over 14000 in gross weight then a percent is
	 * charged on the sales price, lease price or vehicle value.
	 */
	private void displayEmissionsCheckboxes()
	{

		VehicleData laVehData = caVehInqData.getMfVehicleData()
				.getVehicleData();
		RegistrationData laRegData = caVehInqData.getMfVehicleData()
				.getRegData();

		// Need to modify code to be table driven
		// create defect to modify code to be table driven
		if (laVehData != null && laVehData.getDieselIndi() == 1
				&& laRegData.getVehGrossWt() > 14000)

		// defect 6448
		// Version 5.1.5
		// comment out model year--different % for certain year
		// but displays for any year that meets requirements
		// && vehData.getVehModlYr() <= 1996)
		// end defect 6488
		{
			cbAllowedToShowEmissions = true;
			getchkChrgSalesTaxEmi().setSelected(true);
			getchkChrgSalesTaxEmi().setVisible(true);
		}

		else
		{
			getchkChrgSalesTaxEmi().setSelected(false);
			getchkChrgSalesTaxEmi().setVisible(false);
		}
	}

	/**
	 * Display Sales Penalty Percent default - Based on the number of days
	 * delinquent, sets the appropriate sales tax penalty percent.
	 */
	private void displaySalesTaxPenaltyPercentDefault()
	{
		String lsSalesTaxPnltyMsg = CommonConstant.STR_SPACE_EMPTY;

		if (getcomboSalesTaxPenaltyPercent().isEnabled())
		{
			int liDelinquentDays = calcDelinquentDays();

			if (getchkMilitary().isSelected()) // Military
			{
				if (liDelinquentDays <= SIXTY)
				{
					getcomboSalesTaxPenaltyPercent().setSelectedItem(
							ZERO_PER);
					ciDefaultPnltyPer = Integer.parseInt(ZERO_PER);
				}
				else if (liDelinquentDays > SIXTY
						&& liDelinquentDays <= NINETY)
				{
					getcomboSalesTaxPenaltyPercent().setSelectedItem(
							FIVE_PER);
					lsSalesTaxPnltyMsg = SLS_TAX_PNLTY_DFLT_TO
							+ FIVE_PER + CommonConstant.STR_PERCENT;
					ciDefaultPnltyPer = Integer.parseInt(FIVE_PER);
				}
				else
				// delinquent days greater than 90
				{
					getcomboSalesTaxPenaltyPercent().setSelectedItem(
							TEN_PER);
					lsSalesTaxPnltyMsg = SLS_TAX_PNLTY_DFLT_TO
							+ TEN_PER + CommonConstant.STR_PERCENT;
					ciDefaultPnltyPer = Integer.parseInt(TEN_PER);
				}
			}
			else
			// Not Military
			{
				if (liDelinquentDays <= THIRTY)
				{
					getcomboSalesTaxPenaltyPercent().setSelectedItem(
							ZERO_PER);
					ciDefaultPnltyPer = Integer.parseInt(ZERO_PER);
				}
				else if (liDelinquentDays > THIRTY
						&& liDelinquentDays <= SIXTY)
				{
					getcomboSalesTaxPenaltyPercent().setSelectedItem(
							FIVE_PER);
					lsSalesTaxPnltyMsg = SLS_TAX_PNLTY_DFLT_TO
							+ FIVE_PER + CommonConstant.STR_PERCENT;
					ciDefaultPnltyPer = Integer.parseInt(FIVE_PER);
				}
				else
				// delinquent days greater than 60
				{
					getcomboSalesTaxPenaltyPercent().setSelectedItem(
							TEN_PER);
					lsSalesTaxPnltyMsg = SLS_TAX_PNLTY_DFLT_TO
							+ TEN_PER + CommonConstant.STR_PERCENT;
					ciDefaultPnltyPer = Integer.parseInt(TEN_PER);
				}
			}

			getlblSalesTaxPnltyFeeMsg().setText(lsSalesTaxPnltyMsg);
			if (lsSalesTaxPnltyMsg == CommonConstant.STR_SPACE_EMPTY)
			{
				getlblSalesTaxPnltyFeeMsg().setVisible(false);
			}
			else
			{
				getlblSalesTaxPnltyFeeMsg().setVisible(true);
			}
		}
		else
		{
			getcomboSalesTaxPenaltyPercent().setSelectedItem(ZERO_PER);
			getlblSalesTaxPnltyFeeMsg().setVisible(false);
		}
	}

	/**
	 * Vehicle AbstractValue is displayed based on TitleTERPPercentCache table. Criteria
	 * that needs to be met is that the vehicle is diesel, gross weight is over
	 * 14000 and new resident.
	 */
	private void displayVehicleValue()
	{
		SalesTaxCategoryData laSalesTaxData = (SalesTaxCategoryData) cvSalesTaxCat
				.get(getcomboSalesTaxCategories().getSelectedIndex());
		VehicleData laVehData = caVehInqData.getMfVehicleData()
				.getVehicleData();
		String lsSalesTaxCat = (String) getcomboSalesTaxCategories()
				.getSelectedItem();
		int liRTSCurrDate = new RTSDate().getYYYYMMDDDate();

		TitleTERPPercentData laTtlTERPPData = TitleTERPPercentCache
				.getTERPPrcnt(lsSalesTaxCat, liRTSCurrDate, laVehData
						.getDieselIndi(), laVehData.getVehModlYr(),
						caVehInqData.getMfVehicleData().getRegData()
								.getVehGrossWt());

		if (laTtlTERPPData != null && lsSalesTaxCat.equals(NEWRESIDT))
		{
			getstcLblVehicleValue().setVisible(true);
			gettxtVehicleValue().setVisible(true);
			getstcLblVehicleValue().setEnabled(true);
			gettxtVehicleValue().setEnabled(true);
			gettxtVehicleValue()
					.setText(CommonConstant.STR_SPACE_EMPTY);
			setEmissionsCheckbox(laSalesTaxData);
		}
		else
		{
			getstcLblVehicleValue().setVisible(false);
			gettxtVehicleValue().setVisible(false);
		}
	}

	/**
	 * Set display field with dealer data
	 */
	private void doDlrTtl()
	{
		// defect 9654
		String lsDlrGDN = caVehInqData.getMfVehicleData()
				.getTitleData().getDlrGdn();

		cbDTATrans = (UtilityMethods.isDTA(csTransCd) || (lsDlrGDN != null && lsDlrGDN
				.trim().length() > 0));
		// end defect 9654

		long llZeroOne = 0l;

		// Only DTAORD || DTANTD
		if (caDlrTtlData != null
				&& csTransCd.equals(TransCdConstant.DTAORD)
				|| csTransCd.equals(TransCdConstant.DTANTD))
		{

			// Get the Dealer diskette data
			caSlsPrc = caDlrTtlData.getMFVehicleData().getTitleData()
					.getVehSalesPrice();

			caTrdIn = caDlrTtlData.getMFVehicleData().getTitleData()
					.getVehTradeinAllownce();

			Dollar laDtxPdOthr = caDlrTtlData.getVehMiscData()
					.getTaxPdOthrState();

			csMk = caDlrTtlData.getVehMiscData().getTradeInVehMk();

			csVIN = caDlrTtlData.getVehMiscData().getTradeInVehVIN();
			ciYr = caDlrTtlData.getVehMiscData().getTradeInVehYr();

			csSlsCat = caDlrTtlData.getVehMiscData().getSalesTaxCat();

			ciSlsTxDt = caDlrTtlData.getVehMiscData().getSalesTaxDate();

			ciExmptCd = caDlrTtlData.getVehMiscData()
					.getSalesTaxExmptCd();

			// defect 10683
			// ciPnltyPer = caDlrTtlData.getVehMiscData()
			// .getSalesTaxPnltyPer();
			// end defect 10683
			clIMCNo = caDlrTtlData.getVehMiscData().getIMCNo();

			caRebate = caDlrTtlData.getRebateAmt();

			// this block needs to preceede trade in info to set
			// trade in components to visible

			if (caRebate == null
					|| caRebate.compareTo(CommonConstant.ZERO_DOLLAR) == 0)
			{
				gettxtTotalRebateAmount().setText(
						CommonConstant.STR_SPACE_EMPTY);
			}
			else
			{
				gettxtTotalRebateAmount().setText(caRebate.toString());
			}

			// defect 10752
			if (gettxtVehicleSalesPrice().isEnabled())
			{
				if (caSlsPrc != null
						&& caSlsPrc
								.compareTo(CommonConstant.ZERO_DOLLAR) != 0)
				{
					String strSlsPrc = caSlsPrc.toString();
					gettxtVehicleSalesPrice().setText(strSlsPrc);

				}
				getchkMilitary().setSelected(caDlrTtlData.isMilitary());
			}
			// end defect 10752

			if (gettxtTaxPaidOtherState().isEnabled()
					&& laDtxPdOthr != null
					&& (laDtxPdOthr
							.compareTo(CommonConstant.ZERO_DOLLAR) != 0))
			{
				String lsRbt = laDtxPdOthr.toString();
				gettxtTaxPaidOtherState().setText(lsRbt);
			}

			if (gettxtVehicleTradeInAllowance().isEnabled()
					&& caTrdIn != null
					&& (caTrdIn.compareTo(CommonConstant.ZERO_DOLLAR) != 0))
			{
				gettxtVehicleTradeInAllowance().setText(
						caTrdIn.toString());
				handleTradeInDisplay(true);
			}

			if (csMk != null && gettxtTradeInVehicleMake().isEnabled())
			{
				gettxtTradeInVehicleMake().setText(csMk);
			}

			if (ciYr > 0 && gettxtTradeInVehicleYear().isEnabled())
			{
				gettxtTradeInVehicleYear().setText(
						CommonConstant.STR_SPACE_EMPTY + ciYr);
			}

			if (csVIN != null && gettxtTradeInVehicleVIN().isEnabled())
			{
				gettxtTradeInVehicleVIN().setText(csVIN);
			}
			
			if (getchkAdditionalTradeIn().isEnabled()
					&& caDlrTtlData.getVehMiscData()
							.getAddlTradeInIndi() == 1)
			{
				getchkAdditionalTradeIn().setSelected(true);
			}
			else
			{
				getchkAdditionalTradeIn().setSelected(false);
			}

			if (ciSlsTxDt > 0 && gettxtSalesTaxDate().isEnabled())
			{
				RTSDate laRTSDate = new RTSDate(RTSDate.YYYYMMDD,
						ciSlsTxDt);
				gettxtSalesTaxDate().setDate(laRTSDate);
				caSavedSalesTaxDate = gettxtSalesTaxDate().getDate();
			}

			// defect 11047
			if (csSlsCat != null)
			{
				selectSalesTaxCat(csSlsCat);
			}

			// Set exempt reason code, if exist, then set combo
			// box to default
			if (ciExmptCd >= 0)
			{
				TaxExemptCodeData laTaxExmptCdData = selectSalesTaxExemptDesc(ciExmptCd);

				if (laTaxExmptCdData != null
						&& laTaxExmptCdData.getIMCNoReqd() > 0)
				{
					handleIMCNoDisplay(true);

					if (clIMCNo != llZeroOne)
					{
						String lsIMCNo = CommonConstant.STR_SPACE_EMPTY
								+ clIMCNo;
						gettxtIMCNo().setText(lsIMCNo);
					}
					else
					{
						String lsZeroLong = Long.toString(llZeroOne);
						gettxtIMCNo().setText(lsZeroLong);
					}
				}
			}
			// end defect 11047
		}
	}

	/**
	 * Enable/disable checkbox.
	 * 
	 * @param abEnableFlag
	 *            boolean
	 */
	public void enableSalesTaxEmission(boolean abEnableFlag)
	{
		// defect 6448
		// Set Charge Sales Tax Emission Fee
		getchkChrgSalesTaxEmi().setEnabled(abEnableFlag);
		getchkChrgSalesTaxEmi().setSelected(abEnableFlag);
		// end defect 6448
	}

	/**
	 * Enable/Disable Vehicle AbstractValue.
	 * 
	 * @param abVehicleValue
	 *            boolean
	 */
	public void enableVehicleValue(boolean abVehicleValue)
	{
		// defect 6448
		// Enable Vehicle AbstractValue text field and label
		getstcLblVehicleValue().setEnabled(abVehicleValue);
		gettxtVehicleValue().setEnabled(abVehicleValue);
		getstcLblVehicleValue().setVisible(abVehicleValue);
		gettxtVehicleValue().setVisible(abVehicleValue);
		// end defect 6448
	}

	// /**
	// * Find the current record being processed.
	// *
	// * @param avDlrTTLData Vector
	// * @return int
	// */
	// public int findCurrRec(Vector avDlrTTLData)
	// {
	// int liRecIndex = 0;
	// DealerTitleData laDlrTTLData =
	// (DealerTitleData) avDlrTTLData.elementAt(liRecIndex);
	//
	// for (int i = 1; i < avDlrTTLData.size(); i++)
	// {
	// liRecIndex++;
	// laDlrTTLData = (DealerTitleData) avDlrTTLData.elementAt(i);
	// if (laDlrTTLData.isProcessed() == false)
	// {
	// break;
	// }
	// }
	// if (laDlrTTLData.isProcessed() == true)
	// {
	// return liRecIndex;
	// }
	// else
	// {
	// return --liRecIndex;
	// }
	// }

	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE
	 *            FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE
	 *            FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{

		if (!aaFE.isTemporary()
				&& aaFE.getSource() == gettxtVehicleTradeInAllowance())
		{

			try
			{
				// defect 10100
				if (!isTradeInValueProvided())
				{
					clearAllColor(gettxtTradeInVehicleYear());
					clearAllColor(gettxtTradeInVehicleMake());
					clearAllColor(gettxtTradeInVehicleVIN());
					handleTradeInDisplay(false);
				}
				else
				{
					handleTradeInDisplay(true);
				}
				// end defect 10100
			}

			catch (NumberFormatException aeNFE)
			{
				RTSException leRTSEx = new RTSException(150);
				leRTSEx.displayError(this);
				gettxtVehicleTradeInAllowance().setText(
						CommonConstant.STR_SPACE_EMPTY);
				gettxtVehicleTradeInAllowance().requestFocus();
				return;
			}
		}
		// defect 8522
		// Convert I/O => 1/0
		else if (!aaFE.isTemporary()
				&& aaFE.getSource() == gettxtTradeInVehicleVIN())
		{
			String lsVin = gettxtTradeInVehicleVIN().getText().trim()
					.toUpperCase();
			// defect 8902
			lsVin = CommonValidations.convert_i_and_o_to_1_and_0(lsVin);
			// end defect 8902
			gettxtTradeInVehicleVIN().setText(lsVin);
		}
		// end defect 8522
	}

	/**
	 * Return the chkAdditionalTradeIn property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkAdditionalTradeIn()
	{

		if (ivjchkAdditionalTradeIn == null)
		{

			try
			{
				ivjchkAdditionalTradeIn = new JCheckBox();
				ivjchkAdditionalTradeIn.setName("chkAdditionalTradeIn");
				ivjchkAdditionalTradeIn.setBorder(new EtchedBorder());
				ivjchkAdditionalTradeIn.setMnemonic(KeyEvent.VK_A);
				ivjchkAdditionalTradeIn.setText(ADDL_TRADE_INS);
				ivjchkAdditionalTradeIn.setMaximumSize(new Dimension(
						146, 22));
				ivjchkAdditionalTradeIn
						.setActionCommand(ADDL_TRADE_INS);
				ivjchkAdditionalTradeIn.setBounds(92, 87, 157, 21);
				ivjchkAdditionalTradeIn.setMinimumSize(new Dimension(
						146, 22));
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
		return ivjchkAdditionalTradeIn;
	}

	/**
	 * Return the chkChrgSalesTaxEmi property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkChrgSalesTaxEmi()
	{
		if (ivjchkChrgSalesTaxEmi == null)
		{
			try
			{
				ivjchkChrgSalesTaxEmi = new JCheckBox();
				ivjchkChrgSalesTaxEmi.setName("chkChrgSalesTaxEmi");
				ivjchkChrgSalesTaxEmi.setSelected(true);
				ivjchkChrgSalesTaxEmi.setMnemonic(KeyEvent.VK_F);
				ivjchkChrgSalesTaxEmi.setText(CHRG_SALES_TAX_EMISSION);
				ivjchkChrgSalesTaxEmi.setBounds(25, 37, 222, 22);
				// user code begin {1}
				// defect 6448
				// call itemlistener
				ivjchkChrgSalesTaxEmi.addItemListener(this);
				// end defect 6448
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkChrgSalesTaxEmi;
	}

	/**
	 * Return the chkMilitary property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkMilitary()
	{

		if (ivjchkMilitary == null)
		{

			try
			{
				ivjchkMilitary = new JCheckBox();
				ivjchkMilitary.setBounds(10, 116, 90, 21);
				ivjchkMilitary.setName("chkMilitary");
				ivjchkMilitary.setBorder(new EtchedBorder());
				ivjchkMilitary.setMnemonic(KeyEvent.VK_L);
				ivjchkMilitary.setText(MILITARY);
				ivjchkMilitary.setMaximumSize(new Dimension(146, 22));
				ivjchkMilitary.setMinimumSize(new Dimension(146, 22));
				// user code begin {1}
				ivjchkMilitary.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkMilitary;
	}

	/**
	 * Return the comboSalesTaxCategories property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboSalesTaxCategories()
	{
		if (ivjcomboSalesTaxCategories == null)
		{
			try
			{
				ivjcomboSalesTaxCategories = new JComboBox();
				ivjcomboSalesTaxCategories.setBounds(14, 36, 322, 30);
				ivjcomboSalesTaxCategories
						.setName("comboSalesTaxCategories");
				ivjcomboSalesTaxCategories
						.setEditor(new javax.swing.plaf.metal.MetalComboBoxEditor.UIResource());
				ivjcomboSalesTaxCategories
						.setRenderer(new javax.swing.plaf.basic.BasicComboBoxRenderer.UIResource());
				ivjcomboSalesTaxCategories.setEditable(false);
				ivjcomboSalesTaxCategories.setEnabled(true);
				ivjcomboSalesTaxCategories.addActionListener(this);
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
		return ivjcomboSalesTaxCategories;
	}

	/**
	 * Return the comboSalesTaxExemptDesc property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboSalesTaxExemptDesc()
	{
		if (ivjcomboSalesTaxExemptDesc == null)
		{
			try
			{
				ivjcomboSalesTaxExemptDesc = new JComboBox();
				ivjcomboSalesTaxExemptDesc
						.setName("comboSalesTaxExemptDesc");
				ivjcomboSalesTaxExemptDesc
						.setEditor(new javax.swing.plaf.metal.MetalComboBoxEditor.UIResource());
				ivjcomboSalesTaxExemptDesc
						.setRenderer(new javax.swing.plaf.basic.BasicComboBoxRenderer.UIResource());
				ivjcomboSalesTaxExemptDesc.setBounds(349, 112, 271, 30);
				ivjcomboSalesTaxExemptDesc.setEditable(false);
				ivjcomboSalesTaxExemptDesc.setEnabled(true);
				// user code begin {1}
				// ivjcomboSalesTaxExemptDesc.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboSalesTaxExemptDesc;
	}

	/**
	 * Return the comboSalesTaxPenaltyPercent property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboSalesTaxPenaltyPercent()
	{
		if (ivjcomboSalesTaxPenaltyPercent == null)
		{
			try
			{
				ivjcomboSalesTaxPenaltyPercent = new JComboBox();
				ivjcomboSalesTaxPenaltyPercent
						.setName("comboSalesTaxPenaltyPercent");
				ivjcomboSalesTaxPenaltyPercent
						.setEditor(new javax.swing.plaf.metal.MetalComboBoxEditor.UIResource());
				ivjcomboSalesTaxPenaltyPercent
						.setRenderer(new javax.swing.plaf.basic.BasicComboBoxRenderer.UIResource());
				ivjcomboSalesTaxPenaltyPercent.setBounds(232, 116, 85,
						20);
				ivjcomboSalesTaxPenaltyPercent.setEditable(false);
				ivjcomboSalesTaxPenaltyPercent.setEnabled(true);
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
		return ivjcomboSalesTaxPenaltyPercent;
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
				ivjECHButtonPanel.setName("ECHButtonPanel");
				ivjECHButtonPanel.setBounds(199, 430, 242, 40);
				ivjECHButtonPanel
						.setMinimumSize(new Dimension(217, 35));
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

	// /**
	// * getEtched
	// *
	// * @return Border
	// */
	// public Border getEtched()
	// {
	//
	// return etched;
	//
	// }

	/**
	 * Return the FrmSalesTaxTTL012ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSalesTaxTTL012ContentPane1()
	{
		if (ivjFrmSalesTaxTTL012ContentPane1 == null)
		{
			try
			{
				ivjFrmSalesTaxTTL012ContentPane1 = new JPanel();
				ivjFrmSalesTaxTTL012ContentPane1
						.setName("FrmSalesTaxTTL012ContentPane1");
				ivjFrmSalesTaxTTL012ContentPane1
						.setBorder(new EtchedBorder());
				ivjFrmSalesTaxTTL012ContentPane1.setLayout(null);
				ivjFrmSalesTaxTTL012ContentPane1
						.setMaximumSize(new Dimension(2147483647,
								2147483647));
				ivjFrmSalesTaxTTL012ContentPane1
						.setMinimumSize(new Dimension(965, 514));
				getFrmSalesTaxTTL012ContentPane1().add(
						getstcLblExemptReasons(),
						getstcLblExemptReasons().getName());
				getFrmSalesTaxTTL012ContentPane1().add(
						getcomboSalesTaxExemptDesc(),
						getcomboSalesTaxExemptDesc().getName());
				getFrmSalesTaxTTL012ContentPane1().add(
						getpnlGrpSalesTaxInfo(),
						getpnlGrpSalesTaxInfo().getName());
				getFrmSalesTaxTTL012ContentPane1().add(
						getpnlGrpTradeinInfo(),
						getpnlGrpTradeinInfo().getName());
				getFrmSalesTaxTTL012ContentPane1().add(gettxtIMCNo(),
						gettxtIMCNo().getName());
				getFrmSalesTaxTTL012ContentPane1().add(
						getpnlGrpSalesTaxEmissions(),
						getpnlGrpSalesTaxEmissions().getName());
				getFrmSalesTaxTTL012ContentPane1().add(
						getECHButtonPanel(),
						getECHButtonPanel().getName());
				getFrmSalesTaxTTL012ContentPane1().add(
						getstcLblTaxPermit(),
						getstcLblTaxPermit().getName());
				// defect 9584
				ivjFrmSalesTaxTTL012ContentPane1.add(
						getpnlDeliquentTransfer(), null);
				ivjFrmSalesTaxTTL012ContentPane1.add(
						getcomboSalesTaxCategories(), null);
				ivjFrmSalesTaxTTL012ContentPane1.add(
						getstcLblCategories(), null);
				ivjFrmSalesTaxTTL012ContentPane1.add(
						gettxtSalesTaxDate(), null);
				ivjFrmSalesTaxTTL012ContentPane1.add(
						getstclblDateOfAssignment(), null);
				ivjFrmSalesTaxTTL012ContentPane1.add(
						getstclblSalesTaxDate(), null);
				// end defect 9584
				ivjFrmSalesTaxTTL012ContentPane1.add(
						getlblDelqntPnltyFeeMsg(), null);
				// user code begin {1}
				ivjFrmSalesTaxTTL012ContentPane1.add(
						getlblDelqntPnltyDaysMsg(), null);
				ivjFrmSalesTaxTTL012ContentPane1.add(
						getlblPnltyExmptDescMsg(), null);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmSalesTaxTTL012ContentPane1;
	}

	/**
	 * This method initializes ivjlblDelqntPnltyDaysMsg
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDelqntPnltyDaysMsg()
	{
		if (ivjlblDelqntPnltyDaysMsg == null)
		{
			ivjlblDelqntPnltyDaysMsg = new JLabel();
			ivjlblDelqntPnltyDaysMsg.setSize(203, 20);
			ivjlblDelqntPnltyDaysMsg.setText("");
			ivjlblDelqntPnltyDaysMsg.setEnabled(false);
			ivjlblDelqntPnltyDaysMsg.setLocation(382, 64);
			ivjlblDelqntPnltyDaysMsg.setFont(new java.awt.Font(
					"Dialog", java.awt.Font.ITALIC, 12));
		}
		return ivjlblDelqntPnltyDaysMsg;
	}

	/**
	 * This method initializes ivjlblDelqntPnltyFeeMsg
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDelqntPnltyFeeMsg()
	{
		if (ivjlblDelqntPnltyFeeMsg == null)
		{
			ivjlblDelqntPnltyFeeMsg = new javax.swing.JLabel();
			ivjlblDelqntPnltyFeeMsg.setSize(612, 20);
			ivjlblDelqntPnltyFeeMsg
					.setText(CommonConstant.STR_SPACE_EMPTY);
			ivjlblDelqntPnltyFeeMsg.setForeground(Color.red);
			ivjlblDelqntPnltyFeeMsg
					.setHorizontalAlignment(SwingConstants.CENTER);
			ivjlblDelqntPnltyFeeMsg.setLocation(9, 405);
		}
		return ivjlblDelqntPnltyFeeMsg;
	}

	/**
	 * This method initializes ivjlblPnltyExmptDescMsg
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPnltyExmptDescMsg()
	{
		if (ivjlblPnltyExmptDescMsg == null)
		{
			ivjlblPnltyExmptDescMsg = new javax.swing.JLabel();
			ivjlblPnltyExmptDescMsg.setSize(276, 20);
			ivjlblPnltyExmptDescMsg.setText("");
			ivjlblPnltyExmptDescMsg.setLocation(348, 268);
			ivjlblPnltyExmptDescMsg.setEnabled(false);
			ivjlblPnltyExmptDescMsg
					.setHorizontalAlignment(SwingConstants.CENTER);
			ivjlblPnltyExmptDescMsg.setFont(new java.awt.Font("Dialog",
					java.awt.Font.ITALIC, 12));
		}
		return ivjlblPnltyExmptDescMsg;
	}

	/**
	 * This method initializes ivjlblSalesTaxPnltyFeeMsg
	 * 
	 * @return JLabel
	 */
	private JLabel getlblSalesTaxPnltyFeeMsg()
	{
		if (ivjlblSalesTaxPnltyFeeMsg == null)
		{
			ivjlblSalesTaxPnltyFeeMsg = new javax.swing.JLabel();
			ivjlblSalesTaxPnltyFeeMsg.setBounds(8, 144, 309, 20);
			ivjlblSalesTaxPnltyFeeMsg
					.setText(CommonConstant.STR_SPACE_EMPTY);
			ivjlblSalesTaxPnltyFeeMsg.setForeground(Color.red);
			ivjlblSalesTaxPnltyFeeMsg
					.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return ivjlblSalesTaxPnltyFeeMsg;
	}

	// /**
	// * Return number of months given Month, Year
	// *
	// */
	// private int getNoMonths(int aiMo, int aiYr)
	// {
	// return aiMo + aiYr * 12;
	// }

	/**
	 * This method initializes ivjpnlDelinquentTransfer
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getpnlDeliquentTransfer()
	{
		if (ivjpnlDelinquentTransfer == null)
		{
			ivjpnlDelinquentTransfer = new javax.swing.JPanel();
			ivjpnlDelinquentTransfer.setLayout(null);
			ivjpnlDelinquentTransfer.add(gettxtPenaltyFee(), null);
			ivjpnlDelinquentTransfer.add(getpnlOwnerType(), null);
			ivjpnlDelinquentTransfer.add(getstclblPenaltyFee(), null);
			ivjpnlDelinquentTransfer.setBounds(348, 155, 276, 108);
			ivjpnlDelinquentTransfer.setBorder(new TitledBorder(
					new EtchedBorder(), "Delinquent Transfer:"));
		}
		return ivjpnlDelinquentTransfer;
	}

	/**
	 * Return the pnlGrpSalesTaxEmissions property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlGrpSalesTaxEmissions()
	{
		if (ivjpnlGrpSalesTaxEmissions == null)
		{
			try
			{
				ivjpnlGrpSalesTaxEmissions = new JPanel();
				ivjpnlGrpSalesTaxEmissions
						.setName("pnlGrpSalesTaxEmissions");
				ivjpnlGrpSalesTaxEmissions.setLayout(null);
				ivjpnlGrpSalesTaxEmissions
						.setMaximumSize(new Dimension(2147483647,
								2147483647));
				ivjpnlGrpSalesTaxEmissions
						.setMinimumSize(new Dimension(214, 91));
				ivjpnlGrpSalesTaxEmissions.setBounds(350, 329, 271, 69);
				getpnlGrpSalesTaxEmissions().add(gettxtVehicleValue(),
						gettxtVehicleValue().getName());
				getpnlGrpSalesTaxEmissions().add(
						getchkChrgSalesTaxEmi(),
						getchkChrgSalesTaxEmi().getName());
				getpnlGrpSalesTaxEmissions().add(
						getstcLblVehicleValue(),
						getstcLblVehicleValue().getName());
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
		return ivjpnlGrpSalesTaxEmissions;
	}

	/**
	 * Return the pnlGrpSalesTaxInfo property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlGrpSalesTaxInfo()
	{
		if (ivjpnlGrpSalesTaxInfo == null)
		{
			try
			{
				ivjpnlGrpSalesTaxInfo = new JPanel();
				ivjpnlGrpSalesTaxInfo.setName("pnlGrpSalesTaxInfo");
				ivjpnlGrpSalesTaxInfo.setBorder(new TitledBorder(
						new EtchedBorder(), SALES_TAX_INFO));
				ivjpnlGrpSalesTaxInfo.setLayout(null);
				ivjpnlGrpSalesTaxInfo.setMaximumSize(new Dimension(
						2147483647, 2147483647));
				ivjpnlGrpSalesTaxInfo.setMinimumSize(new Dimension(297,
						174));
				ivjpnlGrpSalesTaxInfo.setBounds(8, 89, 328, 174);
				getpnlGrpSalesTaxInfo().add(getchkMilitary(),
						getchkMilitary().getName());
				getpnlGrpSalesTaxInfo().add(gettxtTotalRebateAmount(),
						gettxtTotalRebateAmount().getName());
				getpnlGrpSalesTaxInfo().add(gettxtVehicleSalesPrice(),
						gettxtVehicleSalesPrice().getName());
				getpnlGrpSalesTaxInfo().add(
						gettxtVehicleTradeInAllowance(),
						gettxtVehicleTradeInAllowance().getName());
				getpnlGrpSalesTaxInfo().add(gettxtTaxPaidOtherState(),
						gettxtTaxPaidOtherState().getName());
				getpnlGrpSalesTaxInfo().add(
						getstcLblTaxPaidOtherState(),
						getstcLblTaxPaidOtherState().getName());
				getpnlGrpSalesTaxInfo().add(getstcLblPenaltyPercent(),
						getstcLblPenaltyPercent().getName());
				getpnlGrpSalesTaxInfo().add(
						getcomboSalesTaxPenaltyPercent(),
						getcomboSalesTaxPenaltyPercent().getName());
				getpnlGrpSalesTaxInfo().add(getstcLblRebate(),
						getstcLblRebate().getName());
				getpnlGrpSalesTaxInfo().add(
						getstcLblTotalRebateAmount(),
						getstcLblTotalRebateAmount().getName());
				getpnlGrpSalesTaxInfo().add(getstcLblTradeIn(),
						getstcLblTradeIn().getName());
				getpnlGrpSalesTaxInfo().add(getchkMilitary(),
						getchkMilitary().getName());
				getpnlGrpSalesTaxInfo().add(
						getlblSalesTaxPnltyFeeMsg(),
						getlblSalesTaxPnltyFeeMsg().getName());
				// user code begin {1}
				ivjpnlGrpSalesTaxInfo.setBorder(BorderFactory
						.createTitledBorder(etched, SALES_TAX_INFO));
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlGrpSalesTaxInfo;
	}

	/**
	 * Return the pnlGrpTradeinInfo property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlGrpTradeinInfo()
	{
		if (ivjpnlGrpTradeinInfo == null)
		{
			try
			{
				ivjpnlGrpTradeinInfo = new JPanel();
				ivjpnlGrpTradeinInfo.setName("pnlGrpTradeinInfo");
				ivjpnlGrpTradeinInfo.setBorder(new TitledBorder(
						new EtchedBorder(), TRADE_IN_INFO));
				ivjpnlGrpTradeinInfo.setLayout(null);
				ivjpnlGrpTradeinInfo.setMaximumSize(new Dimension(
						2147483647, 2147483647));
				ivjpnlGrpTradeinInfo.setMinimumSize(new Dimension(12,
						26));
				ivjpnlGrpTradeinInfo.setBounds(8, 274, 328, 124);
				getpnlGrpTradeinInfo().add(gettxtTradeInVehicleYear(),
						gettxtTradeInVehicleYear().getName());
				getpnlGrpTradeinInfo().add(gettxtTradeInVehicleMake(),
						gettxtTradeInVehicleMake().getName());
				getpnlGrpTradeinInfo().add(gettxtTradeInVehicleVIN(),
						gettxtTradeInVehicleVIN().getName());
				getpnlGrpTradeinInfo().add(getchkAdditionalTradeIn(),
						getchkAdditionalTradeIn().getName());
				getpnlGrpTradeinInfo().add(getstcLblVIN(),
						getstcLblVIN().getName());
				getpnlGrpTradeinInfo().add(getstcLblYearMake(),
						getstcLblYearMake().getName());
				// user code begin {1}
				ivjpnlGrpTradeinInfo.setBorder(BorderFactory
						.createTitledBorder(etched, TRADE_IN_INFO));
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlGrpTradeinInfo;
	}

	/**
	 * This method initializes ivjpnlOwnerType
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getpnlOwnerType()
	{
		if (ivjpnlOwnerType == null)
		{
			ivjpnlOwnerType = new javax.swing.JPanel();
			ivjpnlOwnerType.setLayout(null);
			ivjpnlOwnerType.add(getradioDealer(), null);
			ivjpnlOwnerType.add(getradioExempt(), null);
			ivjpnlOwnerType.add(getradioGeneralPublic(), null);
			ivjpnlOwnerType.add(getradioMilitary(), null);
			ivjpnlOwnerType.add(getradioNoneSelected(), null);
			ivjpnlOwnerType.setBounds(16, 44, 240, 54);
			caButtonGroup1 = new RTSButtonGroup();
			caButtonGroup1.add(getradioDealer());
			caButtonGroup1.add(getradioExempt());
			caButtonGroup1.add(getradioGeneralPublic());
			caButtonGroup1.add(getradioMilitary());
			caButtonGroup1.add(getradioNoneSelected());
		}
		return ivjpnlOwnerType;
	}

	/**
	 * This method initializes ivjradioDealer
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getradioDealer()
	{
		if (ivjradioDealer == null)
		{
			ivjradioDealer = new javax.swing.JRadioButton();
			ivjradioDealer.setBounds(10, 10, 89, 21);
			ivjradioDealer.setText(TXT_DEALER);
			ivjradioDealer.setName(TXT_DEALER);
			ivjradioDealer.setMnemonic(KeyEvent.VK_D);
			ivjradioDealer.addItemListener(this);
		}
		return ivjradioDealer;
	}

	/**
	 * This method initializes ivjradioExempt
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getradioExempt()
	{
		if (ivjradioExempt == null)
		{
			ivjradioExempt = new javax.swing.JRadioButton();
			ivjradioExempt.setBounds(10, 34, 84, 21);
			ivjradioExempt.setText(TXT_EXEMPT);
			ivjradioExempt.setName(TXT_EXEMPT);
			ivjradioExempt.setMnemonic(KeyEvent.VK_X);
			ivjradioExempt.addItemListener(this);
		}
		return ivjradioExempt;
	}

	/**
	 * This method initializes ivjradioGeneralPublic
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getradioGeneralPublic()
	{
		if (ivjradioGeneralPublic == null)
		{
			ivjradioGeneralPublic = new javax.swing.JRadioButton();
			ivjradioGeneralPublic.setBounds(116, 10, 119, 21);
			ivjradioGeneralPublic.setText(TXT_GENERAL_PUBLIC);
			ivjradioGeneralPublic.setName(TXT_GENERAL_PUBLIC);
			ivjradioGeneralPublic.setMnemonic(KeyEvent.VK_G);
			ivjradioGeneralPublic.addItemListener(this);
		}
		return ivjradioGeneralPublic;
	}

	/**
	 * This method initializes ivjradioMilitary
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getradioMilitary()
	{
		if (ivjradioMilitary == null)
		{
			ivjradioMilitary = new javax.swing.JRadioButton();
			ivjradioMilitary.setBounds(116, 34, 90, 21);
			ivjradioMilitary.setText(TXT_MILITARY);
			ivjradioMilitary.setName(TXT_MILITARY);
			ivjradioMilitary.setMnemonic(KeyEvent.VK_M);
			ivjradioMilitary.addItemListener(this);
		}
		return ivjradioMilitary;
	}

	/**
	 * This method initializes ivjradioNoneSelected
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getradioNoneSelected()
	{
		if (ivjradioNoneSelected == null)
		{
			ivjradioNoneSelected = new javax.swing.JRadioButton();
			ivjradioNoneSelected.setBounds(207, 31, 21, 18);
		}
		return ivjradioNoneSelected;
	}

	/**
	 * Return the stcLblCategories property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblCategories()
	{

		if (ivjstcLblCategories == null)
		{

			try
			{
				ivjstcLblCategories = new JLabel();
				ivjstcLblCategories.setBounds(16, 15, 89, 19);
				ivjstcLblCategories.setName("stcLblCategories");
				ivjstcLblCategories.setText(CATEGORIES);
				ivjstcLblCategories
						.setMaximumSize(new Dimension(62, 14));
				ivjstcLblCategories
						.setMinimumSize(new Dimension(62, 14));
				// user code end

			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}

		return ivjstcLblCategories;

	}

	/**
	 * This method initializes ivjstclblDateOfAssignment
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblDateOfAssignment()
	{
		if (ivjstclblDateOfAssignment == null)
		{
			ivjstclblDateOfAssignment = new javax.swing.JLabel();
			ivjstclblDateOfAssignment.setSize(122, 20);
			ivjstclblDateOfAssignment.setText(DATE_OF_ASSIGMENT);
			ivjstclblDateOfAssignment.setLocation(349, 17);
		}
		return ivjstclblDateOfAssignment;
	}

	/**
	 * Return the stcLblExemptReasons property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblExemptReasons()
	{

		if (ivjstcLblExemptReasons == null)
		{

			try
			{
				ivjstcLblExemptReasons = new JLabel();
				ivjstcLblExemptReasons.setSize(122, 20);
				ivjstcLblExemptReasons.setName("stcLblExemptReasons");
				ivjstcLblExemptReasons.setText(EXEMPT_REASON);
				ivjstcLblExemptReasons.setMaximumSize(new Dimension(96,
						14));
				ivjstcLblExemptReasons.setMinimumSize(new Dimension(96,
						14));
				ivjstcLblExemptReasons.setLocation(349, 88);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblExemptReasons;
	}

	/**
	 * This method initializes ivjstclblPenaltyFee
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblPenaltyFee()
	{
		if (ivjstclblPenaltyFee == null)
		{
			ivjstclblPenaltyFee = new javax.swing.JLabel();
			ivjstclblPenaltyFee.setSize(74, 20);
			ivjstclblPenaltyFee.setText("Penalty Fee:");
			ivjstclblPenaltyFee.setLocation(45, 22);
			// user code begin {1}
			ivjstclblPenaltyFee.setLabelFor(gettxtPenaltyFee());
			ivjstclblPenaltyFee.setDisplayedMnemonic(KeyEvent.VK_P);
			// user code end

		}
		return ivjstclblPenaltyFee;
	}

	/**
	 * Return the stcLblPenaltyPercent property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblPenaltyPercent()
	{
		if (ivjstcLblPenaltyPercent == null)
		{

			try
			{
				ivjstcLblPenaltyPercent = new JLabel();
				ivjstcLblPenaltyPercent.setName("stcLblPenaltyPercent");
				ivjstcLblPenaltyPercent.setText(PENALTY_PERCENT);
				ivjstcLblPenaltyPercent.setMaximumSize(new Dimension(
						93, 14));
				ivjstcLblPenaltyPercent.setMinimumSize(new Dimension(
						93, 14));
				ivjstcLblPenaltyPercent.setHorizontalAlignment(4);
				ivjstcLblPenaltyPercent.setBounds(119, 116, 110, 20);
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
		return ivjstcLblPenaltyPercent;
	}

	/**
	 * Return the stcLblRebate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblRebate()
	{
		if (ivjstcLblRebate == null)
		{
			try
			{
				ivjstcLblRebate = new JLabel();
				ivjstcLblRebate.setName("stcLblRebate");
				ivjstcLblRebate.setText(REBATE);
				ivjstcLblRebate.setMaximumSize(new Dimension(41, 14));
				ivjstcLblRebate.setMinimumSize(new Dimension(41, 14));
				ivjstcLblRebate.setBounds(182, 17, 47, 24);
				ivjstcLblRebate
						.setHorizontalAlignment(SwingConstants.RIGHT);
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
		return ivjstcLblRebate;
	}

	/**
	 * This method initializes ivjstclblSalesTaxDate
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblSalesTaxDate()
	{
		if (ivjstclblSalesTaxDate == null)
		{
			ivjstclblSalesTaxDate = new javax.swing.JLabel();
			ivjstclblSalesTaxDate.setBounds(382, 41, 90, 19);
			ivjstclblSalesTaxDate.setText(SALES_TAX_DATE);
			ivjstclblSalesTaxDate.setLabelFor(gettxtSalesTaxDate());
			ivjstclblSalesTaxDate.setDisplayedMnemonic(KeyEvent.VK_T);
		}
		return ivjstclblSalesTaxDate;
	}

	/**
	 * Return the stcLblTaxPaidOtherState property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblTaxPaidOtherState()
	{

		if (ivjstcLblTaxPaidOtherState == null)
		{

			try
			{
				ivjstcLblTaxPaidOtherState = new JLabel();
				ivjstcLblTaxPaidOtherState
						.setName("stcLblTaxPaidOtherState");
				ivjstcLblTaxPaidOtherState
						.setText(TAX_PAID_OTHER_STATE);
				ivjstcLblTaxPaidOtherState
						.setMaximumSize(new Dimension(120, 14));
				ivjstcLblTaxPaidOtherState
						.setMinimumSize(new Dimension(120, 14));
				ivjstcLblTaxPaidOtherState.setHorizontalAlignment(4);
				ivjstcLblTaxPaidOtherState.setBounds(90, 84, 139, 20);

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
		return ivjstcLblTaxPaidOtherState;
	}

	/**
	 * Return the stcLblTaxPermit property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblTaxPermit()
	{
		if (ivjstcLblTaxPermit == null)
		{
			try
			{
				ivjstcLblTaxPermit = new JLabel();
				ivjstcLblTaxPermit.setSize(116, 20);
				ivjstcLblTaxPermit.setName("stcLblTaxPermit");
				ivjstcLblTaxPermit.setText(TAX_PERMIT_NO);
				ivjstcLblTaxPermit.setLocation(351, 297);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTaxPermit;
	}

	/**
	 * Return the stcLblTotalRebateAmount property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblTotalRebateAmount()
	{

		if (ivjstcLblTotalRebateAmount == null)
		{

			try
			{
				ivjstcLblTotalRebateAmount = new JLabel();
				ivjstcLblTotalRebateAmount
						.setName("stcLblTotalRebateAmount");
				ivjstcLblTotalRebateAmount.setText(SALES_PRICE);
				ivjstcLblTotalRebateAmount
						.setMaximumSize(new Dimension(96, 14));
				ivjstcLblTotalRebateAmount
						.setMinimumSize(new Dimension(96, 14));
				ivjstcLblTotalRebateAmount.setBounds(8, 17, 109, 24);
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

		return ivjstcLblTotalRebateAmount;

	}

	/**
	 * Return the stcLbltradein property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblTradeIn()
	{
		if (ivjstcLblTradeIn == null)
		{
			try
			{
				ivjstcLblTradeIn = new JLabel();
				ivjstcLblTradeIn.setName("stcLblTradeIn");
				ivjstcLblTradeIn.setText(TRADE_IN);
				ivjstcLblTradeIn.setBounds(169, 53, 60, 20);
				ivjstcLblTradeIn
						.setHorizontalAlignment(SwingConstants.RIGHT);

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
		return ivjstcLblTradeIn;
	}

	/**
	 * New field...Version 5.1.5 Return the stcLblVehicleValue property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblVehicleValue()
	{
		if (ivjstcLblVehicleValue == null)
		{
			try
			{
				ivjstcLblVehicleValue = new JLabel();
				ivjstcLblVehicleValue.setName("stcLblVehicleValue");
				ivjstcLblVehicleValue.setText(VEHICLE_VALUE);
				ivjstcLblVehicleValue.setBounds(30, 8, 86, 22);
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
		return ivjstcLblVehicleValue;
	}

	/**
	 * Return the stcLblVIN property value.
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
				ivjstcLblVIN.setName("stcLblVIN");
				ivjstcLblVIN.setDisplayedMnemonic(KeyEvent.VK_V);
				ivjstcLblVIN.setLabelFor(gettxtTradeInVehicleVIN());
				ivjstcLblVIN.setText(VIN);
				ivjstcLblVIN.setMaximumSize(new Dimension(22, 14));
				ivjstcLblVIN.setBounds(49, 58, 40, 19);
				ivjstcLblVIN.setMinimumSize(new Dimension(22, 14));
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblYearMake()
	{
		if (ivjstcLblYearMake == null)
		{
			try
			{
				ivjstcLblYearMake = new JLabel();
				ivjstcLblYearMake.setName("stcLblYearMake");
				ivjstcLblYearMake.setDisplayedMnemonic(KeyEvent.VK_Y);
				ivjstcLblYearMake.setText(YEAR_MAKE);
				ivjstcLblYearMake.setMaximumSize(new Dimension(63, 14));
				ivjstcLblYearMake
						.setLabelFor(gettxtTradeInVehicleYear());
				ivjstcLblYearMake.setBounds(15, 26, 74, 19);
				ivjstcLblYearMake.setMinimumSize(new Dimension(63, 14));
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
	 * Return the txtIMCNo property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtIMCNo()
	{
		if (ivjtxtIMCNo == null)
		{
			try
			{
				ivjtxtIMCNo = new RTSInputField();
				ivjtxtIMCNo.setName("txtIMCNo");
				ivjtxtIMCNo.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtIMCNo
						.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
				ivjtxtIMCNo.setText("");
				ivjtxtIMCNo.setBounds(478, 297, 93, 20);
				ivjtxtIMCNo.setMaxLength(IMCNO_MAX_LEN);
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
		return ivjtxtIMCNo;
	}

	/**
	 * Return the ivjtxtTaxPaidOtherState property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTaxPaidOtherState()
	{
		if (ivjtxtTaxPaidOtherState == null)
		{
			try
			{
				ivjtxtTaxPaidOtherState = new RTSInputField();
				ivjtxtTaxPaidOtherState.setName("txtPaidOtherState");
				ivjtxtTaxPaidOtherState
						.setInput(RTSInputField.DOLLAR_ONLY);
				ivjtxtTaxPaidOtherState
						.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
				ivjtxtTaxPaidOtherState.setBounds(232, 84, 85, 20);
				ivjtxtTaxPaidOtherState.setMaxLength(TAX_PAID_MAX_LEN);
				ivjtxtTaxPaidOtherState
						.setHorizontalAlignment(JTextField.RIGHT);
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
		return ivjtxtTaxPaidOtherState;
	}

	/**
	 * This method initializes ivjtxtPenaltyFee
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtPenaltyFee()
	{
		if (ivjtxtPenaltyFee == null)
		{
			ivjtxtPenaltyFee = new RTSInputField();
			ivjtxtPenaltyFee.setSize(60, 20);
			ivjtxtPenaltyFee.setLocation(129, 22);
			// user code begin {1}
			ivjtxtPenaltyFee.setInput(RTSInputField.DOLLAR_ONLY);
			ivjtxtPenaltyFee.setMaxLength(8);
			// user code end
		}
		return ivjtxtPenaltyFee;
	}

	/**
	 * Return the txtSalesTaxDate property value.
	 * 
	 * @return RTSDateField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private RTSDateField gettxtSalesTaxDate()
	{
		if (ivjtxtSalesTaxDate == null)
		{
			try
			{
				ivjtxtSalesTaxDate = new RTSDateField();
				ivjtxtSalesTaxDate.setBounds(477, 40, 69, 20);
				ivjtxtSalesTaxDate.setName("txtSalesTaxDate");
				ivjtxtSalesTaxDate
						.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
				ivjtxtSalesTaxDate.setFocusTraversalKeys(
						KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
						new HashSet());
				ivjtxtSalesTaxDate.setFocusTraversalKeys(
						KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
						new HashSet());
				ivjtxtSalesTaxDate.addKeyListener(this);
				// ivjtxtSalesTaxDate.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtSalesTaxDate;
	}

	/**
	 * Return the txtTotalRebateAmount property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private RTSInputField gettxtTotalRebateAmount()
	{
		if (ivjtxtTotalRebateAmount == null)
		{
			try
			{
				ivjtxtTotalRebateAmount = new RTSInputField();
				ivjtxtTotalRebateAmount.setName("txtTotalRebateAmount");
				ivjtxtTotalRebateAmount
						.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
				ivjtxtTotalRebateAmount
						.setInput(RTSInputField.DOLLAR_ONLY);
				ivjtxtTotalRebateAmount.setBounds(117, 19, 61, 20);
				ivjtxtTotalRebateAmount
						.setMaxLength(TOT_REBATE_MAX_LEN);
				ivjtxtTotalRebateAmount.setSelectionColor(new Color(
						204, 204, 255));
				ivjtxtTotalRebateAmount
						.setHorizontalAlignment(JTextField.RIGHT);
				// user code begin {1}
				ivjtxtTotalRebateAmount.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtTotalRebateAmount;
	}

	/**
	 * Return the txtTradeInVehicleMake property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtTradeInVehicleMake()
	{
		if (ivjtxtTradeInVehicleMake == null)
		{
			try
			{
				ivjtxtTradeInVehicleMake = new RTSInputField();
				ivjtxtTradeInVehicleMake
						.setName("txtTradeInVehicleMake");
				ivjtxtTradeInVehicleMake
						.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtTradeInVehicleMake
						.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
				ivjtxtTradeInVehicleMake.setBounds(135, 25, 48, 20);
				ivjtxtTradeInVehicleMake.setMaxLength(MAKE_MAX_LEN);
				// user code begin {1}
				ivjtxtTradeInVehicleMake.setText("");
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtTradeInVehicleMake;
	}

	/**
	 * Return the txtTradeInVehicleVIN property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private RTSInputField gettxtTradeInVehicleVIN()
	{
		if (ivjtxtTradeInVehicleVIN == null)
		{
			try
			{
				ivjtxtTradeInVehicleVIN = new RTSInputField();
				ivjtxtTradeInVehicleVIN.setName("txtTradeInVehicleVIN");
				ivjtxtTradeInVehicleVIN
						.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtTradeInVehicleVIN
						.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
				ivjtxtTradeInVehicleVIN.setBounds(92, 59, 212, 20);
				ivjtxtTradeInVehicleVIN.setMaxLength(VIN_MAX_LEN);
				// user code begin {1}
				// defect 8522
				ivjtxtTradeInVehicleVIN.addFocusListener(this);
				// end defect 8522
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtTradeInVehicleVIN;
	}

	/**
	 * Return the txtTradeInVehicleYear property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private RTSInputField gettxtTradeInVehicleYear()
	{
		if (ivjtxtTradeInVehicleYear == null)
		{
			try
			{
				ivjtxtTradeInVehicleYear = new RTSInputField();
				ivjtxtTradeInVehicleYear
						.setName("txtTradeInVehicleYear");
				ivjtxtTradeInVehicleYear
						.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtTradeInVehicleYear
						.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
				ivjtxtTradeInVehicleYear.setText("2008");
				ivjtxtTradeInVehicleYear.setBounds(92, 25, 37, 20);
				ivjtxtTradeInVehicleYear.setMaxLength(YEAR_MAX_LEN);
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
		return ivjtxtTradeInVehicleYear;
	}

	/**
	 * Return the txtVehicleSalesPrice property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private RTSInputField gettxtVehicleSalesPrice()
	{
		if (ivjtxtVehicleSalesPrice == null)
		{
			try
			{
				ivjtxtVehicleSalesPrice = new RTSInputField();
				ivjtxtVehicleSalesPrice.setName("txtVehicleSalesPrice");
				ivjtxtVehicleSalesPrice
						.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
				ivjtxtVehicleSalesPrice
						.setInput(RTSInputField.DOLLAR_ONLY);
				ivjtxtVehicleSalesPrice.setBounds(232, 19, 85, 20);
				ivjtxtVehicleSalesPrice
						.setMaxLength(SALES_PRICE_MAX_LEN);
				ivjtxtVehicleSalesPrice.setSelectionColor(new Color(
						204, 204, 255));
				ivjtxtVehicleSalesPrice
						.setHorizontalAlignment(JTextField.RIGHT);
				// user code begin {1}
				ivjtxtVehicleSalesPrice.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleSalesPrice;
	}

	/**
	 * Return the txtVehicleTradeInAllowance property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private RTSInputField gettxtVehicleTradeInAllowance()
	{
		if (ivjtxtVehicleTradeInAllowance == null)
		{
			try
			{
				ivjtxtVehicleTradeInAllowance = new RTSInputField();
				ivjtxtVehicleTradeInAllowance
						.setName("txtVehicleTradeInAllowance");
				ivjtxtVehicleTradeInAllowance
						.setInput(RTSInputField.DOLLAR_ONLY);
				ivjtxtVehicleTradeInAllowance
						.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
				ivjtxtVehicleTradeInAllowance
						.setBounds(232, 53, 85, 20);
				ivjtxtVehicleTradeInAllowance
						.setMaxLength(TRADEIN_ALLWC_MAX_LEN);
				ivjtxtVehicleTradeInAllowance
						.setHorizontalAlignment(JTextField.RIGHT);
				// user code begin {1}
				ivjtxtVehicleTradeInAllowance.addFocusListener(this);
				// ivjtxtVehicleTradeInAllowance.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleTradeInAllowance;
	}

	/**
	 * New field...Version 5.1.5 Return the txtVehicleValue property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVehicleValue()
	{
		if (ivjtxtVehicleValue == null)
		{
			try
			{
				ivjtxtVehicleValue = new RTSInputField();
				ivjtxtVehicleValue.setName("txtVehicleValue");
				ivjtxtVehicleValue
						.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
				ivjtxtVehicleValue.setInput(RTSInputField.DOLLAR_ONLY);
				ivjtxtVehicleValue.setBounds(127, 8, 93, 22);
				ivjtxtVehicleValue.setMaxLength(VEH_VALUE_MAX_LEN);
				ivjtxtVehicleValue.setSelectionColor(new Color(204,
						204, 255));
				ivjtxtVehicleValue
						.setHorizontalAlignment(JTextField.RIGHT);
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
		return ivjtxtVehicleValue;
	}

	/**
	 * Set SalesTaxCategory, Salex Tax Exempt Description for ATV && ROV
	 * vehicles
	 * 
	 */
	private void handleATV_ROV()
	{
		if (caVehInqData.getMfVehicleData().getRegData().isATV_ROV())
		{
			selectSalesTaxCat("EXEMPT");
			selectSalesTaxExemptDesc(18);
		}
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException
	 *            Throwable
	 */
	private void handleException(Throwable aeException)
	{
		RTSException leRTSEx = new RTSException(
				RTSException.JAVA_ERROR, (Exception) aeException);
		leRTSEx.displayError(this);
	}

	/**
	 * Enables/Disables the Exempt Reasons combo box.
	 * 
	 * @param abEnableFlag
	 *            boolean
	 */
	private void handleExemptReasonsDisplay(boolean abEnableFlag)
	{
		if (abEnableFlag)
		{
			// populateExemptReasonsDropDown();
			getstcLblExemptReasons().setEnabled(abEnableFlag);
			getcomboSalesTaxExemptDesc().setEnabled(abEnableFlag);
			getcomboSalesTaxExemptDesc().setSelectedIndex(0);
		}
		else
		{
			// getcomboSalesTaxExemptDesc().removeAllItems();
			getstcLblExemptReasons().setEnabled(abEnableFlag);
			getcomboSalesTaxExemptDesc().setEnabled(abEnableFlag);
			getcomboSalesTaxExemptDesc().setSelectedIndex(-1);
			gettxtIMCNo().setText(CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * Enables/Disables the IMC number field.
	 * 
	 * @param abEnableFlag
	 *            boolean
	 */
	private void handleIMCNoDisplay(boolean abEnableFlag)
	{
		gettxtIMCNo().setEnabled(abEnableFlag);
		getstcLblTaxPermit().setEnabled(abEnableFlag);
		if (!abEnableFlag)
		{
			gettxtIMCNo().setText(CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * Enables/Disables all sales price related fields.
	 * 
	 * @param abEnableFlag
	 *            boolean
	 */
	private void handleSalesPriceDisplay(boolean abEnableFlag)
	{
		getstcLblRebate().setEnabled(abEnableFlag);
		getstcLblTotalRebateAmount().setEnabled(abEnableFlag);
		getstcLblTradeIn().setEnabled(abEnableFlag);
		getstcLblTaxPaidOtherState().setEnabled(abEnableFlag);
		getstcLblPenaltyPercent().setEnabled(abEnableFlag);
		getpnlGrpSalesTaxInfo().setEnabled(abEnableFlag);
		gettxtTotalRebateAmount().setEnabled(abEnableFlag);
		gettxtVehicleSalesPrice().setEnabled(abEnableFlag);
		gettxtVehicleTradeInAllowance().setEnabled(abEnableFlag);
		gettxtTaxPaidOtherState().setEnabled(abEnableFlag);
		getcomboSalesTaxPenaltyPercent().setEnabled(abEnableFlag);
		// defect 10683
		getchkMilitary().setEnabled(abEnableFlag);
		displaySalesTaxPenaltyPercentDefault();
		// end defect 10683
		// defect 6594
		// Clear sales price fields if disabling
		if (!abEnableFlag)
		{
			gettxtTotalRebateAmount().setText(
					CommonConstant.STR_SPACE_EMPTY);
			gettxtVehicleSalesPrice().setText(
					CommonConstant.STR_SPACE_EMPTY);
			gettxtVehicleTradeInAllowance().setText(
					CommonConstant.STR_SPACE_EMPTY);
			gettxtTaxPaidOtherState().setText(
					CommonConstant.STR_SPACE_EMPTY);
			getcomboSalesTaxPenaltyPercent().setSelectedIndex(0);
		}
		// end defect 6594

		// defect 6448
		// Make call to set Sales Tax Emission
		// Checkbox always enabled if meets
		enableSalesTaxEmission(true);
		// end defect 6448
	}

	/**
	 * Handle selection of Sales Tax Category
	 * 
	 */
	private void handleSalesTaxCategorySelection()
	{
		SalesTaxCategoryData laSalesTaxData = (SalesTaxCategoryData) cvSalesTaxCat
				.get(getcomboSalesTaxCategories().getSelectedIndex());

		// defect 11048
		if (!cbInitExmpt)
		{
			if (!laSalesTaxData.getTtlTrnsfrPnltyExmptCd().trim().equals(
					csTtlTrnsfrPnltyExmptCd))
			{
				if (UtilityMethods.isEmpty(laSalesTaxData
						.getTtlTrnsfrPnltyExmptCd()))
				{
					csTtlTrnsfrPnltyExmptCd = new String();
					getpnlDeliquentTransfer().setEnabled(true);
					getstcLblExemptReasons().setText(new String());
					getradioNoneSelected().setSelected(true);
					cbDelqntPnltyFeeExmpt = false;
				}
				else
				{
					csTtlTrnsfrPnltyExmptCd = laSalesTaxData
					.getTtlTrnsfrPnltyExmptCd().trim();
					cbDelqntPnltyFeeExmpt = true;
				}
				cbNewTtlTrnsfrPnltyExmptCd = true;
				displayDelinquentTransfer();
				calcTrnsfrPnltyFee();
				cbNewTtlTrnsfrPnltyExmptCd = false;
			}
		}
		// end defect 11048

		// if DTA Diskette, does not change the state of trade in display
		if ((csTransCd.equals(TransCdConstant.DTANTD) || csTransCd
				.equals(TransCdConstant.DTAORD)))
		{
			itemDTAStateChange(getcomboSalesTaxCategories()
					.getSelectedIndex());

			// defect 10603
			// defect 6565
			// Sets Emission checkbox if allowed
			// Determines if Vehicle AbstractValue field should be displayed.
			// if (cbAllowedToShowEmissions)
			// {
			// setEmissionsCheckbox(laSalesTaxData);
			// determineVehValueDisplay();
			// }
			// end defect 6565
			// end defect 10603
		}
		else
		{
			handleSalesPriceDisplay(laSalesTaxData
					.isSalesTaxPriceReqd());

			handleExemptReasonsDisplay(laSalesTaxData
					.isExmptReasonsReqd());

			handleIMCNoDisplay(false);

			// defect 10100
			// Implement isTradeInValueProvided
			handleTradeInDisplay(laSalesTaxData.isTradeInReqd()
					|| isTradeInValueProvided());
			// end defect 10100

			// defect 10603
			// if (cbAllowedToShowEmissions)
			// {
			// // Enable/Disable Emissions checkboxes
			// setEmissionsCheckbox(laSalesTaxData);
			// }
			// end defect 10603
		}

		// defect 10518
		if (isDealerFinancedSale() != cbDealerFinancedSale)
		{
			calcTrnsfrPnltyFee();
			cbDealerFinancedSale = isDealerFinancedSale();
		}
		// end defect 10518

		// defect 10603
		if (cbAllowedToShowEmissions)
		{
			setEmissionsCheckbox(laSalesTaxData);
		}
		determineVehValueDisplay();
		// end defect 10603

	}

	/**
	 * Handle selection from Sales Tax Exempt Combo Box
	 * 
	 */
	private void handleSalesTaxExemptSelection()
	{
		if (getcomboSalesTaxExemptDesc().isEnabled()
				&& getcomboSalesTaxExemptDesc().getSelectedIndex() != -1)
		{
			TaxExemptCodeData laTaxExemptData = (TaxExemptCodeData) cvExemptReasons
					.get(getcomboSalesTaxExemptDesc()
							.getSelectedIndex());

			handleIMCNoDisplay(laTaxExemptData.isIMCNoReqd());

			// defect 10518
			if (isDealerFinancedSale() != cbDealerFinancedSale)
			{
				calcTrnsfrPnltyFee();
				cbDealerFinancedSale = isDealerFinancedSale();
			}
			// end defect 10518
		}
	}

	/**
	 * Enables/Disables all Trade-In related fields.
	 * 
	 * @param abEnableFlag
	 *            boolean
	 */
	private void handleTradeInDisplay(boolean abEnableFlag)
	{
		getstcLblYearMake().setEnabled(abEnableFlag);
		getstcLblVIN().setEnabled(abEnableFlag);
		getpnlGrpTradeinInfo().setEnabled(abEnableFlag);
		gettxtTradeInVehicleYear().setEnabled(abEnableFlag);
		gettxtTradeInVehicleMake().setEnabled(abEnableFlag);
		gettxtTradeInVehicleVIN().setEnabled(abEnableFlag);
		getchkAdditionalTradeIn().setEnabled(abEnableFlag);
		getpnlGrpTradeinInfo().setEnabled(abEnableFlag);
		if (!abEnableFlag)
		{
			gettxtTradeInVehicleYear().setText(
					CommonConstant.STR_SPACE_EMPTY);
			gettxtTradeInVehicleMake().setText(
					CommonConstant.STR_SPACE_EMPTY);
			gettxtTradeInVehicleVIN().setText(
					CommonConstant.STR_SPACE_EMPTY);
			getchkAdditionalTradeIn().setSelected(false);
		}
	}

	// /**
	// * Get the vector of TaxExemptCodeData Description
	// *
	// * @param asExptDesc
	// * String
	// * @param asSlsCat
	// * String
	// * @return int
	// */
	// protected int IMCNoReqd(String asExptDesc, String asSlsCat)
	// {
	// SalesTaxCategoryData laSalesTaxCat = new SalesTaxCategoryData();
	// TaxExemptCodeData laTaxExmptCd = new TaxExemptCodeData();
	// int liIMCNoReqd = 0;
	//
	// for (int i = 0; i < cvSalesTaxCat.size(); i++)
	// {
	// laSalesTaxCat = (SalesTaxCategoryData) cvSalesTaxCat
	// .elementAt(i);
	//
	// if (laSalesTaxCat.getSalesTaxCat().equals(asSlsCat)
	// && laSalesTaxCat.getExmptReasonsReqd() == 1)
	// {
	//
	// for (int j = 0; j < cvExemptReasons.size(); j++)
	// {
	//
	// laTaxExmptCd = (TaxExemptCodeData) cvExemptReasons
	// .elementAt(j);
	//
	// if (laTaxExmptCd.getSalesTaxExmptDesc().equals(
	// asExptDesc))
	// {
	//
	// liIMCNoReqd = laTaxExmptCd.getIMCNoReqd();
	// break;
	//
	// } // end if
	// } // end for loop
	// } // end if
	// }
	// return liIMCNoReqd;
	// }

	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{

			// user code begin {1}
			// focus manager necessary to make form follow tab tag order
			// etched = BorderFactory.createEtchedBorder();
			// user code end
			setName(ScreenConstant.TTL012_FRAME_NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(639, 498);
			setTitle(ScreenConstant.TTL012_FRAME_TITLE);
			setContentPane(getFrmSalesTaxTTL012ContentPane1());
			// defect 9854
			setRequestFocus(false);
			// end defect 9854
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		addWindowListener(this);
		getRootPane().setDefaultButton(
				getECHButtonPanel().getBtnEnter());
		// user code end
	}

	/**
	 * 
	 * Determine if Dealer Financed Sale Transfer Entity Code applies
	 * 
	 * @return boolean
	 */
	private boolean isDealerFinancedSale()
	{
		boolean lbReturn = false;

		if (getradioDealer().isSelected())
		{
			if (getcomboSalesTaxExemptDesc().isEnabled())
			{
				int liSelection = getcomboSalesTaxExemptDesc()
						.getSelectedIndex();
				TaxExemptCodeData laData = (TaxExemptCodeData) cvExemptReasons
						.elementAt(liSelection);
				lbReturn = laData.getSalesTaxExmptCd() == SELLER_FINANCED_SALE_CD
						&& TitleTransferPenaltyFeeCache
								.getTitleTransferPenaltyFeeCache(
										DEALER_SELLER_FINANCED_CD,
										new RTSDate().getYYYYMMDDDate(),
										0) != null;
			}
		}
		return lbReturn;
	}

	/**
	 * Determine if Delinquent Penalty Fee Applicable
	 * 
	 * @return boolean
	 */
	private boolean isDelqntPnltyFeeApplicable()
	{
		boolean lbExempt = cbInitExmpt;

		// defect 11048
		if (!cbNewSalesTaxDate && !cbNewTtlTrnsfrPnltyExmptCd)
		{
			// end defect 11048
			lbExempt = cbDelqntPnltyFeeExmpt;
		}
		else if (!lbExempt && isSalesTaxDateValid())
		{
			// defect 9737
			int liSalesTaxDate = gettxtSalesTaxDate().getDate()
					.getYYYYMMDDDate();

			cbPriorDelqntPnltyApplies = liSalesTaxDate < NEW_DELQNT_FEE_START_DATE;

			lbExempt = isRegClassExmpt();

			if (!cbPriorDelqntPnltyApplies && !lbExempt)
			{
				lbExempt = isVehModlYrExmpt();
			}

			if (!cbPriorDelqntPnltyApplies)
			{
				cb2008DelqntPnltyExmpt = csTtlTrnsfrPnltyExmptCd != null
						&& csTtlTrnsfrPnltyExmptCd.length() > 0;
			}
			// defect 11048
			if (!lbExempt)
			{
				SalesTaxCategoryData laSalesTaxData = (SalesTaxCategoryData) cvSalesTaxCat
						.get(getcomboSalesTaxCategories()
								.getSelectedIndex());
				lbExempt = !UtilityMethods.isEmpty(laSalesTaxData
						.getTtlTrnsfrPnltyExmptCd());
			}
			getradioExempt().setSelected(lbExempt);

			// defect 11048
			// No required that currently registered
			// if (!lbExempt)
			// {
			// RTSDate laSalesTaxDate = gettxtSalesTaxDate().getDate();
			// int liSalesTaxNoMonths = getNoMonths(laSalesTaxDate
			// .getMonth(), laSalesTaxDate.getYear());
			// lbExempt = ciRegExpMonths < liSalesTaxNoMonths;
			//
			// if (!lbExempt)
			// {
			// if (csTtlTrnsfrPnltyExmptCd
			// .equals(NOT_REGISTERED_ENT_CD))
			// {
			// getradioNoneSelected().setSelected(true);
			// csTtlTrnsfrPnltyExmptCd = CommonConstant.STR_SPACE_EMPTY;
			// }
			// }
			// else
			// {
			// csTtlTrnsfrPnltyExmptCd = NOT_REGISTERED_ENT_CD;
			// getradioExempt().setSelected(true);
			// }
			// }
			// end defect 11048

			cbDelqntPnltyFeeExmpt = lbExempt;
			// end defect 9737
		}
		return !lbExempt;
	}

	/**
	 * Return indicator to denote if DocTypeCd is Exempt from Delinquent Penalty
	 * Fees
	 * 
	 * @return boolean
	 */
	private boolean isDocTypeExmpt()
	{
		boolean lbExmpt = false;
		String lsExmptCd = CommonConstant.STR_SPACE_EMPTY;
		int liDocTypeCd = caVehInqData.getMfVehicleData()
				.getTitleData().getDocTypeCd();
		DocumentTypesData laDocTypeData = DocumentTypesCache
				.getDocType(liDocTypeCd);
		if (laDocTypeData != null)
		{
			lsExmptCd = laDocTypeData.getTtlTrnsfrPnltyExmptCd();
			if (lsExmptCd != null && lsExmptCd.trim().length() != 0)
			{
				lbExmpt = true;
				csTtlTrnsfrPnltyExmptCd = lsExmptCd;
			}
		}
		return lbExmpt;

	}

	/**
	 * Validate IMCNo
	 * 
	 * @param asIMCNo
	 *            String
	 * @return boolean
	 */
	private boolean isInvalidIMCNo(String asIMCNo)
	{
		long llbig = 1000000000000l;
		boolean lbRet = false;

		if (asIMCNo != null)
		{
			try
			{
				long llNum = Long.parseLong(asIMCNo);
				if (llNum <= 0 || llNum > llbig)
				{
					lbRet = true;
				}
			}

			catch (NumberFormatException aeNFE)
			{
				lbRet = true;
			}
		}
		return lbRet;
	}

	/**
	 * Is Owner Type Selected
	 * 
	 * @return boolean
	 */
	private boolean isOwnerTypeSelected()
	{
		return !getradioNoneSelected().isSelected();
	}

	/**
	 * Return indicator to denote if selected OwnrEvidCd is Exempt from
	 * Delinquent Penalty Fees
	 * 
	 * @return boolean
	 */
	private boolean isOwnrEvidCdExmpt()
	{
		boolean lbExmpt = false;
		String lsExmptCd = OwnershipEvidenceCodesCache
				.getTtlTrnsfrPnltyExmptCd(caVehInqData
						.getMfVehicleData().getTitleData()
						.getOwnrShpEvidCd());
		if (lsExmptCd != null && lsExmptCd.trim().length() != 0)
		{
			lbExmpt = true;
			csTtlTrnsfrPnltyExmptCd = lsExmptCd;
		}
		return lbExmpt;

	}

	/**
	 * Return indicator to denote if VehClass/RegClass is Exempt from Delinquent
	 * Penalty Fees
	 * 
	 * @return boolean
	 */
	private boolean isRegClassExmpt()
	{
		boolean lbExmpt = false;
		String lsExmptCd = CommonConstant.STR_SPACE_EMPTY;

		String lsVehClassCd = caVehInqData.getMfVehicleData()
				.getVehicleData().getVehClassCd();

		int liRegClassCd = caVehInqData.getMfVehicleData().getRegData()
				.getRegClassCd();

		RegistrationClassData laRegClassData = RegistrationClassCache
				.getRegisClass(lsVehClassCd, liRegClassCd,
						new RTSDate().getYYYYMMDDDate());

		if (laRegClassData != null)
		{
			lsExmptCd = laRegClassData.getTtlTrnsfrPnltyExmptCd();

			if (lsExmptCd != null && lsExmptCd.trim().length() != 0)
			{
				if (!(lsExmptCd.equals(ANTIQUE_CLASSIC_ENT_CD) && cbPriorDelqntPnltyApplies))
				{
					csTtlTrnsfrPnltyExmptCd = lsExmptCd;
					lbExmpt = true;
				}
			}
		}
		return lbExmpt;
	}

	/**
	 * Return indicator to denote if Exempt fromn Delinquent Penalty Fees as the
	 * vehicle has no prior registration.
	 * 
	 * @return boolean
	 */
	private boolean isRegExmpt()
	{
		boolean lbExmpt = false;
		TitleValidObj laTtlValidationObj = (TitleValidObj) caVehInqData
				.getValidationObject();
		MFVehicleData laOrigMfVehData = (MFVehicleData) laTtlValidationObj
				.getMfVehOrig();
		RegistrationData laOrigRegData = laOrigMfVehData.getRegData();

		if (caVehInqData.getMfVehicleData().getRegData().getExmptIndi() == 1
				|| laOrigRegData.getExmptIndi() == 1)
		{
			caVehInqData.getValidationObject();
			lbExmpt = true;
			csTtlTrnsfrPnltyExmptCd = EXEMPT_AGENCY_ENT_CD;
		}
		// defect 11048
		// else if (ciRegExpMonths == 0)
		// {
		// lbExmpt = true;
		// csTtlTrnsfrPnltyExmptCd = NOT_REGISTERED_ENT_CD;
		// }
		// end defect 11048
		return lbExmpt;
	}

	/**
	 * Validates the Sales Tax Date. Must be: - Valid Date - Greater than or
	 * equal to 05/01/1941
	 * 
	 * @return boolean
	 */
	private boolean isSalesTaxDateValid()
	{
		boolean lbRtn = true;

		// Is Valid Date Entered
		if (gettxtSalesTaxDate().getDate() == null
				|| gettxtSalesTaxDate().getDate().getYYYYMMDDDate() == 0)
		{
			lbRtn = false;
		}
		else
		{
			int liDtSalesTaxDate = gettxtSalesTaxDate().getDate()
					.getYYYYMMDDDate();

			if (liDtSalesTaxDate < MIN_SALES_TAX_DATE)
			{
				lbRtn = false;
			}
			// defect 9584
			else if (liDtSalesTaxDate > new RTSDate().getYYYYMMDDDate())
			{
				lbRtn = false;
			}
			else if (liDtSalesTaxDate > caVehInqData.getRTSEffDt())
			{
				lbRtn = false;
			}
			// end defect 9584

		}
		if (!lbRtn)
		{
			// defect 9724
			cbInitCalc = true;
			cbDelqntPnltyFeeExmpt = cbInitExmpt;
			caSavedSalesTaxDate = null;
			if (!cbInitExmpt)
			{
				getlblDelqntPnltyDaysMsg().setText(
						CommonConstant.STR_SPACE_EMPTY);
				getlblPnltyExmptDescMsg().setText(
						CommonConstant.STR_SPACE_EMPTY);
				getlblDelqntPnltyFeeMsg().setText(
						CommonConstant.STR_SPACE_EMPTY);
				gettxtSalesTaxDate().requestFocus();
			}
			// defect 9724
		}
		return lbRtn;
	}

	/**
	 * Is Supervisor Override Code Required for Modified Penalty Fee
	 * 
	 * @return boolean
	 */
	private boolean isSupOvrdRqdforPnltyFee()
	{
		boolean lbReturn = false;

		if (gettxtPenaltyFee().isEnabled() && caSavedPenaltyFee != null)
		{
			String lsPenaltyFee = gettxtPenaltyFee().getText();
			Dollar laPenaltyFee = new Dollar(0.00);
			if (lsPenaltyFee != null && lsPenaltyFee.length() > 0)
			{
				laPenaltyFee = new Dollar(lsPenaltyFee);
			}

			if (laPenaltyFee.compareTo(caSavedPenaltyFee) != 0)
			{
				// defect VE fix
				/*
				 * caVehInqData.getVehMiscData().setSupvOvrideReason(
				 * MODIFIED_PENALTY_FEE_REASON);
				 */
				VehMiscData laVehMiscData = caVehInqData
						.getVehMiscData();
				laVehMiscData
						.setSupvOvrideReason(MODIFIED_PENALTY_FEE_REASON);
				// end defect VE fix

				getController().processData(
						VCSalesTaxTTL012.SUPV_OVRIDE, caVehInqData);

				if (caVehInqData.getVehMiscData().getSupvOvride() == null
						|| caVehInqData.getVehMiscData()
								.getSupvOvride().length() == 0)
				{
					lbReturn = true;
				}
			}
		}
		return lbReturn;
	}

	/**
	 * Is Supervisor Override Code Required for Modified Sales Tax Penalty
	 * Percent
	 * 
	 * @return boolean
	 */
	private boolean isSupOvrdRqdforSalesTaxPnltyPrcnt()
	{
		boolean lbReturn = false;

		if (getcomboSalesTaxPenaltyPercent().isEnabled())
		{
			String lsSalesTxPnlty = (String) getcomboSalesTaxPenaltyPercent()
					.getSelectedItem();

			int liSalesTxPnlty = Integer.parseInt(lsSalesTxPnlty);

			if (liSalesTxPnlty != ciDefaultPnltyPer)
			{
				// defect VE fix
				/*
				 * caVehInqData.getVehMiscData().setSupvOvrideReason(
				 * MODIFIED_SALES_TAX_PENALTY_REASON);
				 */
				VehMiscData laVehMiscData = caVehInqData
						.getVehMiscData();
				laVehMiscData
						.setSupvOvrideReason(MODIFIED_SALES_TAX_PENALTY_REASON);
				// end defect VE fix

				getController().processData(
						VCSalesTaxTTL012.SUPV_OVRIDE, caVehInqData);

				if (caVehInqData.getVehMiscData().getSupvOvride() == null
						|| caVehInqData.getVehMiscData()
								.getSupvOvride().length() == 0)
				{
					lbReturn = true;
				}
			}
		}
		return lbReturn;
	}

	/**
	 * Is Trade-In provided
	 * 
	 * @return boolean
	 */
	private boolean isTradeInValueProvided()
	{
		return !gettxtVehicleTradeInAllowance().isEmpty()
				&& Double.parseDouble(gettxtVehicleTradeInAllowance()
						.getText()) > 0;
	}

	/**
	 * Is TransCd Exempt
	 * 
	 * @return boolean
	 */
	private boolean isTransCdExmpt()
	{
		boolean lbExmpt = false;
		String lsExmptCd = CommonConstant.STR_SPACE_EMPTY;
		try
		{
			TransactionCodesData laTransCdData = TransactionCodesCache
					.getTransCd(csTransCd);

			if (laTransCdData != null)
			{
				lsExmptCd = laTransCdData.getTtlTrnsfrPnltyExmptCd()
						.trim();
				if (lsExmptCd != null && lsExmptCd.length() != 0)
				{
					lbExmpt = true;
					csTtlTrnsfrPnltyExmptCd = lsExmptCd;
				}
			}
		}
		catch (RTSException aeRTSExc)
		{
			aeRTSExc.displayError(this);
		}
		return lbExmpt;
	}

	/**
	 * Return indicator to denote if Vehicle Model Year is Exempt from
	 * Delinquent Penalty Fees
	 * 
	 * @return boolean
	 */
	private boolean isVehModlYrExmpt()
	{
		boolean lbExmpt = false;
		String lsVehClassCd = caVehInqData.getMfVehicleData()
				.getVehicleData().getVehClassCd();

		if (lsVehClassCd.equals(TRK_GT_ONE)
				|| lsVehClassCd.equals(TRK_LEQ_ONE)
				|| lsVehClassCd.equals(PASS)
				|| lsVehClassCd.equals(PASS_TRK)
				|| lsVehClassCd.equals(MTRCYCLE))
		{
			// Check Vehicle Age
			int liModlYr = caVehInqData.getMfVehicleData()
					.getVehicleData().getVehModlYr();

			if (((new RTSDate()).getYear() - liModlYr) >= MAX_PENALTY_YR_DELTA)
			{
				lbExmpt = true;
				csTtlTrnsfrPnltyExmptCd = ANTIQUE_CLASSIC_ENT_CD;
			}
		}
		return lbExmpt;
	}

	/**
	 * Set frame based on Sales Tax Catagory
	 * 
	 * @param aiIndex
	 *            int
	 */
	private void itemDTAStateChange(int aiIndex)
	{
		// defect 11267 
		TitleValidObj laTtlValidationObj = (TitleValidObj) caVehInqData
		.getValidationObject();
		DealerTitleData laDlrTtlData = null;
		if (laTtlValidationObj != null)
		{
			laDlrTtlData = (DealerTitleData) laTtlValidationObj
			.getDlrTtlData();
		}
		// end defect 11267

		switch (aiIndex)
		{
		case SALESUSE:
		{
			// sales tax info and date enabled unless there
			// is a trade-in then trade-in info is enabled
			handleExemptReasonsDisplay(false);
			handleSalesPriceDisplay(true);
			handleIMCNoDisplay(false);
			// defect 7898
			// Add compareTo and set Trade-In display to false
			// if Trade-In amount is null or less than/equal to
			// zero dollars.
			if (caTrdIn == null
					|| caTrdIn.compareTo(CommonConstant.ZERO_DOLLAR) == 0)
			{
				handleTradeInDisplay(false);
				gettxtVehicleTradeInAllowance().setText(
						CommonConstant.STR_SPACE_EMPTY);
			}
			else
			{
				handleTradeInDisplay(true);
				gettxtVehicleTradeInAllowance().setText(
						caTrdIn.toString());
			}
			// added dSlsPrc and dTrdIn to display when sales/use
			// is chosen then another category is chosen and
			// sales/use is chosen once more redisplays the
			// totals--defect4716 also added code to display
			// the amount or leave field blank and not 0.00
			// if (caTrdIn == null
			// || caTrdIn.equals(laZeroDollar))
			// {
			// gettxtVehicleTradeInAllowance().setText(
			// CommonConstant.STR_SPACE_EMPTY);
			// }
			// else
			// {
			// gettxtVehicleTradeInAllowance().setText(
			// caTrdIn.toString());
			// }

			// changed dSlsPrc.toString() to null
			// gettxtVehicleSalesPrice().setText(null);
			gettxtTradeInVehicleMake().setText(csMk);
			// defect 7898
			// Add if field is enabled and Trade-In year > 0
			// then populate field with value.
			if (gettxtTradeInVehicleYear().isEnabled() && ciYr > 0)
			{
				gettxtTradeInVehicleYear().setText(
						CommonConstant.STR_SPACE_EMPTY + ciYr);
			}
			// end defect 7898
			gettxtTradeInVehicleVIN().setText(csVIN);

			if (caRebate == null
					|| caRebate.compareTo(CommonConstant.ZERO_DOLLAR) == 0)
			{
				gettxtTotalRebateAmount().setText(
						CommonConstant.STR_SPACE_EMPTY);
			}
			else
			{
				gettxtTotalRebateAmount().setText(caRebate.toString());
			}
			// defect 11267 
			//if (getchkAdditionalTradeIn().isEnabled())
			//{
			if (getchkAdditionalTradeIn().isEnabled() 
					&& laDlrTtlData != null 
					&& laDlrTtlData.getVehMiscData().getAddlTradeInIndi() == 1)
				// end defect 11267
			{
				getchkAdditionalTradeIn().setSelected(true);
			}

			break;
		}
		case NEW_RESIDT:
		{
			// new resident has ONLY date enabled
			handleSalesPriceDisplay(false);
			handleExemptReasonsDisplay(false);
			handleTradeInDisplay(false);
			handleIMCNoDisplay(false);
			break;
		}
		case LEASE:
		{
			// leasing has sales tax info and date enabled
			// if there is a trade-in then trade-in info is
			// enabled
			handleExemptReasonsDisplay(false);
			handleSalesPriceDisplay(true);
			handleIMCNoDisplay(false);
			//			 defect 11267 
			//			if (ivjtxtVehicleTradeInAllowance != null)
			//			{
			//				handleTradeInDisplay(false);
			//			}
			//			else
			//			{
			//				handleTradeInDisplay(true);
			//			}
			if (caTrdIn == null
					|| caTrdIn.compareTo(CommonConstant.ZERO_DOLLAR) == 0)
			{
				handleTradeInDisplay(false);
				gettxtVehicleTradeInAllowance().setText(
						CommonConstant.STR_SPACE_EMPTY);
			}
			else
			{
				handleTradeInDisplay(true);
				gettxtVehicleTradeInAllowance().setText(
						caTrdIn.toString());
			}
			// end defect 11267

			gettxtTradeInVehicleMake().setText(csMk);
			// defect 7898
			// Add if field is enabled and Trade-In year > 0
			// then populate field with value.
			if (gettxtTradeInVehicleYear().isEnabled() && ciYr > 0)
			{
				gettxtTradeInVehicleYear().setText(
						CommonConstant.STR_SPACE_EMPTY + ciYr);
			}
			// end defect 7898
			gettxtTradeInVehicleVIN().setText(csVIN);
			
			// defect 11267 
			//if (getchkAdditionalTradeIn().isEnabled())
			//{
			if (getchkAdditionalTradeIn().isEnabled() 
					&& laDlrTtlData != null 
					&& laDlrTtlData.getVehMiscData().getAddlTradeInIndi() == 1)
				// end defect 11267
			{
				getchkAdditionalTradeIn().setSelected(true);
			}
			break;

		}
		case GIFT:
		{
			// gift has ONLY date enabled
			handleExemptReasonsDisplay(false);
			handleTradeInDisplay(false);
			handleSalesPriceDisplay(false);
			handleIMCNoDisplay(false);
			break;
		}
		case EXEMPT:
		{
			// exempt reasons and date are enabled and when reason
			// Rental is chosen tax permit is enabled
			handleExemptReasonsDisplay(true);
			handleTradeInDisplay(false);
			handleSalesPriceDisplay(false);
			break;
		}
		case EVEN_TRADE:
		{
			// even trade-in selected date and trade-in info are
			// enabled with vehicle information in the fields
			handleExemptReasonsDisplay(false);
			handleIMCNoDisplay(false);
			handleSalesPriceDisplay(false);
			handleTradeInDisplay(true);
			if (caVehInqData != null)
			{
//				 defect 11267 
//				TitleValidObj laTtlValidationObj = (TitleValidObj) caVehInqData
//				.getValidationObject();
//				if (laTtlValidationObj != null)
//				{
//					DealerTitleData laDlrTtlData = (DealerTitleData) laTtlValidationObj
//					.getDlrTtlData();
				// end defect 11267 

					if (laDlrTtlData != null)
					{
						if (csMk != null
								&& gettxtTradeInVehicleMake()
										.isEnabled())
						{
							gettxtTradeInVehicleMake().setText(csMk);
						}
						if (ciYr > 0
								&& gettxtTradeInVehicleYear()
										.isEnabled())
						{
							gettxtTradeInVehicleYear().setText(
									CommonConstant.STR_SPACE_EMPTY
											+ ciYr);
						}
						if (csVIN != null
								&& gettxtTradeInVehicleVIN()
										.isEnabled())
						{
							gettxtTradeInVehicleVIN().setText(csVIN);
						}
						if (getchkAdditionalTradeIn().isEnabled()
								&& laDlrTtlData.getVehMiscData()
										.getAddlTradeInIndi() == 1)
						{
							getchkAdditionalTradeIn().setSelected(true);
						}
					}
					// defect 11267 
				//}
					// end defect 11267 
			}
			break;
		}
		}
	}

	/**
	 * Invoked when an item has been selected or deselected. The code written
	 * for this method performs the operations that need to occur when an item
	 * is selected (or deselected).
	 * 
	 * @param aaIE
	 *            ItemEvent
	 */
	public void itemStateChanged(ItemEvent aaIE)
	{
		// defect 10603
		// defect 10134
		// if (aaIE.getSource() == getcomboSalesTaxCategories())
		// {
		// handleSalesTaxCategorySelection();
		// }
		// end defect 10603

		// clearAllColor(this);
		// SalesTaxCategoryData laSalesTaxData =
		// (SalesTaxCategoryData) cvSalesTaxCat.get(
		// getcomboSalesTaxCategories().getSelectedIndex());
		//
		// //if DTA Diskette, does not change the state of trade in display
		// if ((getController()
		// .getTransCode()
		// .equals(TransCdConstant.DTANTD)
		// || getController().getTransCode().equals(
		// TransCdConstant.DTAORD)))
		// // defect 6220
		// // Remove checks for DTA keyboard transactions
		// //|| controller.getTransCode().equals(
		// // TransCdConstant.DTANTK)
		// //|| controller.getTransCode().equals(
		// // TransCdConstant.DTAORK)))
		// // end defect 6220
		// {
		//
		// itemDTAStateChange(
		// getcomboSalesTaxCategories().getSelectedIndex());
		//
		// //defect 6565
		// //Sets Emission checkbox if allowed
		// //Determines if Vehicle AbstractValue field should be displayed.
		// if (cbAllowedToShowEmissions)
		// {
		// setEmissionsCheckbox(laSalesTaxData);
		// determineVehValueDisplay();
		// }
		// //end defect 6565
		// return;
		// }
		//
		// if (laSalesTaxData.getSalesTaxPriceReqd() == 1)
		// {
		// handleSalesPriceDisplay(true);
		// }
		// else
		// {
		// handleSalesPriceDisplay(false);
		// }
		//
		// if (laSalesTaxData.getExmptReasonsReqd() == 1)
		// {
		// handleExemptReasonsDisplay(true);
		// }
		// else
		// {
		// handleExemptReasonsDisplay(false);
		// }
		//
		// handleIMCNoDisplay(false);
		//
		// // defect 6594
		// // Check to see if trade-in allowance has value
		// if ((laSalesTaxData.getTradeInReqd() == 1)
		// || ((gettxtVehicleTradeInAllowance()
		// .getText()
		// .trim()
		// .length()
		// > 0)
		// && (Double
		// .parseDouble(
		// gettxtVehicleTradeInAllowance().getText())
		// > 0.0)))
		// // end defect 6594
		// {
		// handleTradeInDisplay(true);
		// }
		// else
		// {
		// handleTradeInDisplay(false);
		// }
		//
		// if (!cbAllowedToShowEmissions)
		// {
		// return;
		// }
		// // Enable/Disable Emissions checkboxes
		// setEmissionsCheckbox(laSalesTaxData);
		// }
		// else
		// {
		// if (aaIE.getSource() == getcomboSalesTaxExemptDesc()
		// && getcomboSalesTaxExemptDesc().isEnabled())
		// {
		// TaxExemptCodeData laTaxExemptData =
		// (TaxExemptCodeData) cvExemptReasons.get(
		// getcomboSalesTaxExemptDesc().getSelectedIndex());
		// if (laTaxExemptData.getIMCNoReqd() == 1)
		// {
		// handleIMCNoDisplay(true);
		// }
		// else
		// {
		// handleIMCNoDisplay(false);
		// }
		// }
		// }
		// end defect 10134
		// defect 9584 / 9724
		if (aaIE.getSource() instanceof JRadioButton && !cbInitSet)
		{
			// Do not recalculate on New Sales Tax Date - included
			// in keyPressed.
			if (((JRadioButton) aaIE.getSource()).isSelected()
					&& !cbNewSalesTaxDate)
			{
				clearAllColor(this);
				displayDelinquentTransfer();
				calcTrnsfrPnltyFee();
				
				// defect 11048
				// Retain last selection for restoring after New Resident
				//  Selection 
				if (((JRadioButton)aaIE.getSource()).isEnabled()) 
				{
					caLastSelected = (JRadioButton) aaIE.getSource();
				}
				// end defect 11048
			}
		}
		// defect 10683
		else if (aaIE.getSource() instanceof JCheckBox)
		{
			displaySalesTaxPenaltyPercentDefault();
		}
		// end defect 10683
		// end defect 9584 / 9724

		// defect 6565
		// Method added to determine whether Vehicle AbstractValue should be
		// displayed.
		determineVehValueDisplay();
		// end defect 6565
	}

	/**
	 * <ul>
	 * <li>Perform button panel navigation using up/down/left/right arrows</li>
	 * <li>Set focus to appropriate components</li>
	 * <li>Calls fee recalculation when tab off expiration day/month field</li>
	 * </ul>
	 * 
	 * @param aaKE
	 *            KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);

		if (aaKE.getSource().equals(gettxtSalesTaxDate())
				&& (aaKE.getKeyCode() == KeyEvent.VK_TAB))
		{
			clearAllColor(this);
			RTSException leRTSEx = new RTSException();
			if (!isSalesTaxDateValid())
			{
				// if validation fails display exception
				leRTSEx.addException(new RTSException(733),
						gettxtSalesTaxDate());
			}
			if (leRTSEx.isValidationError())
			{
				displayDelinquentTransfer();
				leRTSEx.displayError(this);
				leRTSEx.getFirstComponent().requestFocus();
				return;
			}
			else
			{
				// if (verifySalesTaxDate() != 0)
				// {
				// leRTSEx.addException(
				// new RTSException(658),null);
				// leRTSEx.displayError(this);
				// }
				// defect 9724
				if (caSavedSalesTaxDate == null
						|| caSavedSalesTaxDate.getAMDate() != gettxtSalesTaxDate()
								.getDate().getAMDate())
				{
					recalcForSalesTaxDate();
				}
				// end defect 9724

				if (aaKE.getModifiers() == KeyEvent.SHIFT_MASK)
				{
					getcomboSalesTaxCategories().requestFocus();
				}
				else if (gettxtTotalRebateAmount().isEnabled())
				{
					gettxtTotalRebateAmount().requestFocus();
				}
				else if (gettxtTradeInVehicleYear().isEnabled())
				{
					gettxtTradeInVehicleYear().requestFocus();
				}
				else if (getcomboSalesTaxExemptDesc().isEnabled())
				{
					getcomboSalesTaxExemptDesc().requestFocus();
				}
				else if (gettxtPenaltyFee().isEnabled())
				{
					gettxtPenaltyFee().requestFocus();
				}
				else if (getradioDealer().isEnabled())
				{
					getradioDealer().requestFocus();
				}
				else if (gettxtVehicleValue().isVisible()
						&& gettxtVehicleValue().isEnabled())
				{
					gettxtVehicleValue().requestFocus();
				}
				else
				{
					getECHButtonPanel().getBtnEnter().requestFocus();
				}
			}
		}
	}

	/**
	 * Key Released
	 * 
	 * @param aaKE
	 *            KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		super.keyReleased(aaKE);
	}

	/**
	 * Performed display setup and validations for the Trade-In Info
	 * 
	 * @param aeRTSEx
	 *            RTSException
	 */
	private void validateTradeInInfo(RTSException aeRTSEx)
	{
		// Get SalesTaxCategoryData
		SalesTaxCategoryData laSalesTaxData = (SalesTaxCategoryData) cvSalesTaxCat
				.get(getcomboSalesTaxCategories().getSelectedIndex());

		// Set display for Trade-In Info if a Trade-In Allowance was
		// entered or if Trade-In Info is required. If Trade-In Info
		// was removed then clear fileds and disable.

		// defect 10100
		// if (gettxtVehicleTradeInAllowance().getText().length()
		// > 0 || laSalesTaxData.getTradeInReqd() == 1)
		handleTradeInDisplay(isTradeInValueProvided()
				|| laSalesTaxData.isTradeInReqd());
		// end defect 10100

		// Trade-In Info ***************************************
		// Check if TradeInVehicleYear, TradeInVehicleMake
		// and TradeInVehicleVIN() has been enter if enabled.
		// Display error message.
		if (gettxtTradeInVehicleYear().isEnabled()
				&& CommonValidations
						.isInvalidYearModel(gettxtTradeInVehicleYear()
								.getText().trim()))
		{
			// "laToday.getYear() + 2" means add 2 years to vehicle
			// model year for maximum model year
			RTSDate laToday = RTSDate.getCurrentDate();
			aeRTSEx.addException(new RTSException(
					RTSException.WARNING_MESSAGE,
					FIELD_VALIDATION_ERR_MSG + (laToday.getYear() + 2),
					ERROR), gettxtTradeInVehicleYear());
		}
		// Use RTSInputField.isEmpty()
		if (gettxtTradeInVehicleMake().isEnabled()
				&& gettxtTradeInVehicleMake().isEmpty())
		{
			aeRTSEx.addException(new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtTradeInVehicleMake());
		}
		// Use RTSInputField.isEmpty()
		if (gettxtTradeInVehicleVIN().isEnabled()
				&& gettxtTradeInVehicleVIN().isEmpty())
		{
			aeRTSEx.addException(new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtTradeInVehicleVIN());
		}
	}

	/**
	 * Fills the Select Fee combo box with the AccountCodesData.
	 */
	private void populateExemptReasonsDropDown()
	{

		cvExemptReasons = TaxExemptCodeCache.getTaxExmptCds();
		com.txdot.isd.rts.services.util.UtilityMethods
				.sort(cvExemptReasons);
		int liSize = cvExemptReasons.size();

		for (int i = 0; i < liSize; i++)
		{

			TaxExemptCodeData laTempData = (TaxExemptCodeData) cvExemptReasons
					.get(i);
			String lsData = laTempData.getSalesTaxExmptDesc().trim();

			if (getcomboSalesTaxExemptDesc() != null)
			{
				getcomboSalesTaxExemptDesc().addItem(lsData);
				// defect 8479
				comboBoxHotKeyFix(getcomboSalesTaxExemptDesc());
				// end defect 8479
			}
		}
	}

	/**
	 * Fills the Select Fee combo box with the AccountCodesData.
	 */
	private void populatePenaltyPercentDropDown()
	{

		getcomboSalesTaxPenaltyPercent().addItem(ZERO_PER);
		getcomboSalesTaxPenaltyPercent().addItem(FIVE_PER);
		getcomboSalesTaxPenaltyPercent().addItem(TEN_PER);

	}

	/**
	 * Fills the Select Fee combo box with the AccountCodesData.
	 */
	private void populateSalesTaxCatDropDown()
	{

		cvSalesTaxCat = SalesTaxCategoryCache.getDistinctSalesTaxCat();
		UtilityMethods.sort(cvSalesTaxCat);
		int liSize = cvSalesTaxCat.size();

		for (int i = 0; i < liSize; i++)
		{
			SalesTaxCategoryData laTempData = (SalesTaxCategoryData) cvSalesTaxCat
					.get(i);
			String lsData = laTempData.getSalesTaxCat().trim();

			if (getcomboSalesTaxCategories() != null)
			{
				getcomboSalesTaxCategories().addItem(lsData);
				// defect 8479
				comboBoxHotKeyFix(getcomboSalesTaxCategories());
				// end defect 8479
			}
		}
	}

	/**
	 * Recalculate on New Sales Tax Date
	 */
	private void recalcForSalesTaxDate()
	{
		cbNewSalesTaxDate = true;
		displayDelinquentTransfer();
		calcTrnsfrPnltyFee();
		// defect 10683
		displaySalesTaxPenaltyPercentDefault();
		// end defect 10683
		caSavedSalesTaxDate = gettxtSalesTaxDate().getDate();
		cbNewSalesTaxDate = false;
	}

	/**
	 * Select Sales Tax Category
	 * 
	 * @param asSalesTaxCat
	 */
	private void selectSalesTaxCat(String asSalesTaxCat)
	{
		if (getcomboSalesTaxCategories().isEnabled())
		{
			int liSelected = 0;

			for (int i = 0; i < getcomboSalesTaxCategories()
					.getItemCount(); i++)
			{
				String lsCurr = (String) getcomboSalesTaxCategories()
						.getItemAt(i);

				if (lsCurr.equals(asSalesTaxCat))
				{
					liSelected = i;
					break;
				}
			}
			getcomboSalesTaxCategories().setSelectedIndex(liSelected);
		}
	}

	/**
	 * Select Sales Tax Exempt Reason
	 * 
	 * @param aiSalesTaxExmptCd
	 */
	private TaxExemptCodeData selectSalesTaxExemptDesc(
			int aiSalesTaxExmptCd)
	{
		TaxExemptCodeData laTaxExmptCdData = null;
		if (getcomboSalesTaxExemptDesc().isEnabled())
		{
			laTaxExmptCdData = TaxExemptCodeCache
					.getTaxExmptCd(aiSalesTaxExmptCd);

			if (laTaxExmptCdData != null
					&& !UtilityMethods.isEmpty(laTaxExmptCdData
							.getSalesTaxExmptDesc()))
			{
				getcomboSalesTaxExemptDesc().setSelectedItem(
						laTaxExmptCdData.getSalesTaxExmptDesc().trim());
			}
		}
		return laTaxExmptCdData;
	}

	/**
	 * all subclasses must implement this method - it sets the data on the
	 * screen and is how the controller relays information to the view
	 * 
	 * @param aaDataObject
	 *            Object
	 */
	public void setData(Object aaDataObject)
	{
		// cbIsVisible = false;
		try
		{
			if (aaDataObject != null && aaDataObject instanceof Vector)
			{
				Vector lvIsNextVCREG029 = (Vector) aaDataObject;
				if (lvIsNextVCREG029 != null)
				{
					if (lvIsNextVCREG029.size() == 2)
					{
						// determine next vc if NOT reg029
						if (lvIsNextVCREG029.get(0) instanceof Boolean)
						// first element is flag whether to go to
						// REG029
						{
							getController()
									.processData(
											VCSalesTaxTTL012.REDIRECT_IS_NEXT_VC_REG029,
											lvIsNextVCREG029);
						}
						else if (lvIsNextVCREG029.get(0) instanceof String)
						{
							getController().processData(
									VCSalesTaxTTL012.REDIRECT_NEXT_VC,
									lvIsNextVCREG029);
						}
					}
					else
					{
						new RTSException(RTSException.FAILURE_MESSAGE,
								DATA_MISSING_VCREG029,
								CommonConstant.STR_ERROR)
								.displayError(this);
						return;
					}
				}
			}
			else if (aaDataObject != null)
			{
				caVehInqData = (VehicleInquiryData) aaDataObject;

				// defect 10290
				TitleValidObj laTtlValidObj = (TitleValidObj) caVehInqData
						.getValidationObject();

				caDlrTtlData = laTtlValidObj.getDlrTtlData();

				csTransCd = getController().getTransCode();
				// end defect 10290

				// TODO We can remove cbInitSet now that RTSException
				// no longer processed
				// defect 9584
				if (cbInitSet)
				{
					caVehInqData.getMfVehicleData().getTitleData()
							.setVehTradeinAllownce(new Dollar("0.00"));
					caVehInqData.getVehMiscData().setTotalRebateAmt(
							new Dollar("0.00"));
					populateSalesTaxCatDropDown();
					// populate and set default
					populatePenaltyPercentDropDown();
					getcomboSalesTaxPenaltyPercent().setSelectedItem(
							ZERO_PER);
					populateExemptReasonsDropDown();
					// defect 10518
					getcomboSalesTaxCategories()
							.addActionListener(this);
					// end defect 10518

					getcomboSalesTaxExemptDesc()
							.addActionListener(this);

					getcomboSalesTaxCategories().requestFocus();

					// defect 9724
					getlblDelqntPnltyDaysMsg().setVisible(
							SystemProperty.isShowDelqntPnlty());
					// end defect 9724
					handleExemptReasonsDisplay(false);
					handleTradeInDisplay(false);
					handleIMCNoDisplay(false);

					// defect 6448
					// Displays vehicle value field visible if needed
					displayVehicleValue();
					// end defect 6448
					// Makes sales tax emissions checkboxes invisible if not
					// needed
					displayEmissionsCheckboxes();
					doDlrTtl();

					// defect 9584
					// To determine registration at time of Sale
					// MFVehicleData laOrigMfVehData = (MFVehicleData)
					// laTtlValidObj
					// .getMfVehOrig();
					//
					// RegistrationData laOrigRegData = laOrigMfVehData
					// .getRegData();
					//
					// int liExpMo = laOrigRegData.getRegExpMo();
					// int liExpYr = laOrigRegData.getRegExpYr();
					//					
					// ciRegExpMonths = getNoMonths(liExpMo, liExpYr);

					// Default to "None" selected (not visible)
					getradioNoneSelected().setVisible(false);
					getradioNoneSelected().setEnabled(false);
					getradioNoneSelected().setSelected(true);
					setDelqntPnltyFeeExmpt();
					// Only "Dealer" will be pre-Selected if DTA or
					// DLRGDN exists
					displayDelinquentTransfer();
					calcTrnsfrPnltyFee();
					cbInitSet = false;
					// end defect 9584
					// defect 10752
					displaySalesTaxPenaltyPercentDefault();
					// end defect 10752
					// defect 11047
					handleATV_ROV();
					// end defect 11047
					gettxtSalesTaxDate().requestFocus();
				}
			}
		}
		catch (NullPointerException aeNFE)
		{
			RTSException leRTSEx = new RTSException(
					RTSException.FAILURE_MESSAGE, aeNFE);
			leRTSEx.displayError(this);
			leRTSEx = null;
		}
		catch (NumberFormatException aeNFE)
		{
			RTSException leRTSEx = new RTSException(
					RTSException.FAILURE_MESSAGE, aeNFE);
			leRTSEx.displayError(this);
			leRTSEx = null;
		}
	}

	/**
	 * setDataToDataObject
	 */
	private void setDataToDataObject()
	{
		try
		{
			if (caVehInqData != null)
			{
				// defect 8511
				// Reset values in case ESC back to TTL012
				// Use ZERO_DOLLAR vs. null when target is Dollar field
				TitleData laTtlData = caVehInqData.getMfVehicleData()
						.getTitleData();
				caVehMiscData = caVehInqData.getVehMiscData();

				// Set Sales Tax Category to VehMiscData
				String lsSalesTaxCat = (String) getcomboSalesTaxCategories()
						.getSelectedItem();
				caVehMiscData.setSalesTaxCat(lsSalesTaxCat);
				caVehMiscData.setSalesTaxDate(gettxtSalesTaxDate()
						.getDate().getYYYYMMDDDate());

				// defect 9724
				caVehMiscData
						.setTtlTrnsfrPnltyExmptCd(csTtlTrnsfrPnltyExmptCd);
				// end defect 9724

				// defect 9584
				laTtlData.setTtlSignDate(gettxtSalesTaxDate().getDate()
						.getYYYYMMDDDate());

				caVehMiscData.setTtlTrnsfrEntCd(csTtlTrnsfrEntCd);

				RegTtlAddlInfoData laTtlAddlData = (RegTtlAddlInfoData) ((TitleValidObj) caVehInqData
						.getValidationObject()).getRegTtlAddData();

				String lsPenaltyFee = gettxtPenaltyFee().getText()
						.trim();

				Dollar laPenaltyFee = new Dollar(lsPenaltyFee);
				if (laPenaltyFee.compareTo(new Dollar(0.00)) > 0)
				{
					laTtlAddlData.setTrnsfrPnltyIndi(1);
					caVehMiscData.setTtlTrnsfrPnltyFee(laPenaltyFee);
				}
				else
				{
					laTtlAddlData.setTrnsfrPnltyIndi(0);
					caVehMiscData.setTtlTrnsfrPnltyFee(new Dollar(0.0));
				}

				// Set Sales Tax Exempt Reason to VehMiscData if exempt
				// category picked
				caVehMiscData.setSalesTaxExmptCd(0);

				if (getcomboSalesTaxExemptDesc().isVisible()
						&& getcomboSalesTaxExemptDesc().isEnabled())
				{
					String lsSalesTaxReason = (String) getcomboSalesTaxExemptDesc()
							.getSelectedItem();
					Vector lvTaxExmptDesc = TaxExemptCodeCache
							.getTaxExmptCds();

					for (int i = 0; i < lvTaxExmptDesc.size(); i++)
					{
						TaxExemptCodeData laCodeData = (TaxExemptCodeData) lvTaxExmptDesc
								.get(i);

						if (laCodeData.getSalesTaxExmptDesc().equals(
								lsSalesTaxReason))
						{
							caVehMiscData.setSalesTaxExmptCd(laCodeData
									.getSalesTaxExmptCd());
							break;
						}
					}
				}

				// Set Rebate Amount to VehMiscData
				caVehMiscData.setTotalRebateAmt(new Dollar("0.00"));

				if (gettxtTotalRebateAmount().isEnabled())
				{
					String lsRebateAmt = gettxtTotalRebateAmount()
							.getText().trim();

					if (lsRebateAmt != null && lsRebateAmt.length() > 0)
					{
						Dollar laDlrRebateAmt = new Dollar(lsRebateAmt);
						caVehMiscData.setTotalRebateAmt(laDlrRebateAmt);
					}
				}

				// Set Vehicle Sales Price to TitleData
				laTtlData.setVehSalesPrice(new Dollar("0.00"));

				if (gettxtVehicleSalesPrice().isEnabled())
				{
					String lsSalesPrice = gettxtVehicleSalesPrice()
							.getText().trim();

					if (lsSalesPrice != null
							&& lsSalesPrice.length() > 0)
					{
						Dollar laDlrSalesPrice = new Dollar(
								lsSalesPrice);
						laTtlData.setVehSalesPrice(laDlrSalesPrice);
					}
				}

				// Set Vehicle Trade In Allowance to TitleData
				laTtlData.setVehTradeinAllownce(new Dollar("0.00"));

				// defect 10100
				if (isTradeInValueProvided())
				{
					String lsTradeInAmt = gettxtVehicleTradeInAllowance()
							.getText();

					laTtlData.setVehTradeinAllownce(new Dollar(
							lsTradeInAmt));
				}
				// end defect 10100

				// Set Tax Paid Other State to VehMiscData
				caVehMiscData.setTaxPdOthrState(new Dollar("0.00"));

				if (gettxtTaxPaidOtherState().isEnabled())
				{
					String lsTaxPd = gettxtTaxPaidOtherState()
							.getText().trim();

					if (lsTaxPd != null && lsTaxPd.length() > 0)
					{
						Dollar laDlrTaxPd = new Dollar(lsTaxPd);
						caVehMiscData.setTaxPdOthrState(laDlrTaxPd);
					}
				}

				// Set Sales Tax Penalty Percent to VehMiscData
				caVehMiscData.setSalesTaxPnltyPer(0);

				if (getcomboSalesTaxPenaltyPercent().isEnabled())
				{
					String lsSalesTxPnlty = (String) getcomboSalesTaxPenaltyPercent()
							.getSelectedItem();

					int liSalesTxPnlty = Integer
							.parseInt(lsSalesTxPnlty);

					caVehMiscData.setSalesTaxPnltyPer(liSalesTxPnlty);
				}

				// Set Trade In Year to VehMiscData
				caVehMiscData.setTradeInVehYr(0);

				if (gettxtTradeInVehicleYear().isEnabled())
				{
					String lsTradeYr = gettxtTradeInVehicleYear()
							.getText().trim();

					if (lsTradeYr != null
							&& lsTradeYr.length() == YEAR_MAX_LEN)
					{
						int liTradeYr = Integer.parseInt(lsTradeYr);
						caVehMiscData.setTradeInVehYr(liTradeYr);
					}
				}

				// Set Trade In Make to VehMiscData
				caVehMiscData.setTradeInVehMk(null);

				if (gettxtTradeInVehicleMake().isEnabled())
				{
					String lsTradeMake = gettxtTradeInVehicleMake()
							.getText().trim();

					if (lsTradeMake != null && lsTradeMake.length() > 0)
					{
						caVehMiscData.setTradeInVehMk(lsTradeMake);
					}
				}

				// Set Trade In VIN to VehMiscData
				caVehMiscData.setTradeInVehVIN(null);

				if (gettxtTradeInVehicleVIN().isEnabled())
				{
					String lsTradeVIN = gettxtTradeInVehicleVIN()
							.getText().trim();

					if (lsTradeVIN != null && lsTradeVIN.length() > 0)
					{
						caVehMiscData.setTradeInVehVIN(lsTradeVIN);
					}
				}

				// Set Additional Trade In
				caVehMiscData.setAddlTradeInIndi(0);

				if (getchkAdditionalTradeIn().isEnabled()
						&& getchkAdditionalTradeIn().isSelected())
				{
					caVehMiscData.setAddlTradeInIndi(1);
				}

				// Set IMC Number to VehMiscData
				caVehMiscData.setIMCNo(0);

				if (gettxtIMCNo().isEnabled())
				{
					String lsIMCNo = gettxtIMCNo().getText().trim();
					long clIMCNo = 0;

					if (lsIMCNo != null && lsIMCNo.length() > 0)
					{
						try
						{
							clIMCNo = Long.parseLong(lsIMCNo);
						}

						catch (NumberFormatException leNFE)
						{
							// Empty catch box
						}
						caVehMiscData.setIMCNo(clIMCNo);
					}
				}

				// Set Vehicle Misc Data
				// defect 6448
				// Capture the vehicle value price
				Dollar laVehValue = new Dollar("0.00");

				if (gettxtVehicleValue().isEnabled())
				{
					// defect 10100
					// Use RTSInputField.isEmpty()
					if (!gettxtVehicleValue().isEmpty())
					{
						// end defect 10100
						laVehValue = new Dollar(gettxtVehicleValue()
								.getText());
					}
				}
				laTtlData.setVehicleValue(laVehValue);
				// end defect 6448
			}

			// Set NoChrgSalTaxEmiFeeIndi
			caVehMiscData.setNoChrgSalTaxEmiFeeIndi(1);

			// defect 8900
			// AbstractValue will be set in Fees.
			caVehMiscData.setEmissionSalesTax(new Dollar("0.00"));
			// end defect 8900

			if (getchkChrgSalesTaxEmi().isSelected()
					&& getchkChrgSalesTaxEmi().isVisible())
			{
				caVehMiscData.setNoChrgSalTaxEmiFeeIndi(0);
			}
			// end defect 8511

		}

		catch (NullPointerException aeNFE)
		{
			RTSException leRTSEx = new RTSException(
					CommonConstant.STR_SPACE_EMPTY, aeNFE);
			leRTSEx.displayError(this);
		}
	}

	/**
	 * Set cbDelqntPnltyFeeExmpt
	 */
	private void setDelqntPnltyFeeExmpt()
	{
		cbInitExmpt = isDocTypeExmpt() || isOwnrEvidCdExmpt()
				|| isRegExmpt() || isTransCdExmpt();
		cbDelqntPnltyFeeExmpt = cbInitExmpt;
		// defect 9737
		// Will now check these w/ valid Sales Tax Date
		// isRegClassExmpt()
		// isVehModlYrExmpt()
		// end defect 9737
	}

	/**
	 * Checkbox for Charge Sales Tax Emission Fee Visible or invisible depending
	 * on sales tax category
	 * 
	 * @param aaSalesTxData
	 *            SalesTaxCategoryData
	 */
	private void setEmissionsCheckbox(
			SalesTaxCategoryData aaSalesTaxData)
	{
		// Enable/Disable Emissions checkboxes
		if (getpnlGrpSalesTaxEmissions().isVisible())
		{
			// defect 6448
			// added New Resident and Leasing to the categories now being
			// chosen for the emission charge
			if (aaSalesTaxData.getSalesTaxCat().toUpperCase().trim()
					.equals(SALES_USE)
					|| aaSalesTaxData.getSalesTaxCat().toUpperCase()
							.trim().equals(NEWRESIDT)
					|| aaSalesTaxData.getSalesTaxCat().toUpperCase()
							.trim().equals(LEASING))
			{
				if (cbInitialTabOff)
				{
					cbInitialTabOff = false;
					return;
				}
				// defect 6606
				// Clears the red when vehicle value is redisplayed
				// when checkbox is rechecked
				clearAllColor(this);
				// end defect 6606

				getchkChrgSalesTaxEmi().setVisible(true);
				getchkChrgSalesTaxEmi().setEnabled(true);
			}
			else
			{
				getchkChrgSalesTaxEmi().setSelected(false);
				getchkChrgSalesTaxEmi().setEnabled(false);
				// defect 6448
				// added to set checkbox invisible
				getchkChrgSalesTaxEmi().setVisible(false);
				// end defect 6448
			}
		}
	}

	/**
	 * Validate PenaltyFee
	 * 
	 * @param RTSException
	 */
	private void validatePenaltyFee(RTSException aeRTSEx)
	{
		boolean lbFeeError = false;

		if (!isOwnerTypeSelected())
		{
			aeRTSEx.addException(new RTSException(150),
					getradioGeneralPublic());
		}
		else
		{
			String lsPenaltyFee = gettxtPenaltyFee().getText();

			if (lsPenaltyFee == null || lsPenaltyFee.length() == 0)
			{
				lbFeeError = true;
			}
			else
			{
				Dollar laPenaltyFee = new Dollar(lsPenaltyFee);
				if (laPenaltyFee.compareTo(new Dollar(0.00)) == -1)
				{
					lbFeeError = true;

				}
				else if (laPenaltyFee.compareTo(new Dollar(0.00)) != 0)
				{
					if (getradioDealer().isSelected())
					{
						if (!(laPenaltyFee
								.compareTo(BASE_PNLTY_FEE_DEALER) == 0))
						{
							lbFeeError = true;
						}
					}
					// defect 9737
					// No need to validate if prior to 2008; disabled
					else if (!cbPriorDelqntPnltyApplies)
					{
						Dollar laMult = laPenaltyFee
								.divideNoRound(BASE_PNLTY_FEE);
						String lsMult = laMult.getValue();
						int liIndexCents = lsMult.indexOf('.');
						Double ldCents = new Double(lsMult
								.substring(liIndexCents));
						if (ldCents.compareTo(new Double(0)) != 0)
						{
							lbFeeError = true;
						}
					}
					// end defect 9737
				}
			}
			if (lbFeeError)
			{
				aeRTSEx.addException(new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtPenaltyFee());
				getlblDelqntPnltyFeeMsg().setText(
						CommonConstant.STR_SPACE_EMPTY);
			}
		}
	}

	/**
	 * Verifies that the Sales Tax Date is not more than a year ago.
	 * 
	 * @return int This represents the error type
	 */
	private int verifySalesTaxDate()
	{
		// TODO This should not be different for DTA
		// TransDate is assigned to RTSEffDate
		int liYYYYMMDD = 1;
		int liRTN = NO_SLS_TAX_DATE_ERR;
		int liDateString = 1;
		int liDtTtlDate = caVehInqData.getRTSEffDt();

		// Get date from the SalesTax Screen and pre-date one year
		RTSDate laDtEffDt = new RTSDate(liYYYYMMDD, liDtTtlDate);
		RTSDate laDtOneYearAgo = laDtEffDt.add(RTSDate.DATE, -365);
		RTSDate laDtSalesTaxDate = gettxtSalesTaxDate().getDate();

		// defect 10290
		// int liRecIndex = 0;
		if (UtilityMethods.isDTA(csTransCd))
		{
			// Determine if Dealer Title data was enterred, use the Dealer
			// Trans Date from DTA008 screen
			// defect 6963
			// Change DTA008a to DTA008
			// Object laScrDTA008 =
			// getController().getMediator().openVault(
			// ScreenConstant.DTA008);
			// // end defect 6963
			//
			// if (laScrDTA008 != null)
			// {
			// Vector lvDlrTTLData = (Vector) laScrDTA008;
			//
			// if (lvDlrTTLData.size() != 0 || lvDlrTTLData != null)
			// {
			// //defect 4766
			// // Find current record being processed
			// liRecIndex = findCurrRec(lvDlrTTLData);
			// DealerTitleData laDlrTTLData =
			// (DealerTitleData) lvDlrTTLData.elementAt(
			// liRecIndex);
			String lsTransDt = (String) caDlrTtlData.getTransDate();
			laDtEffDt = new RTSDate(liDateString, Integer
					.parseInt(lsTransDt));
			laDtOneYearAgo = laDtEffDt.add(RTSDate.DATE, -365);
			// }
			// }
		}
		// end defect 10290

		if (laDtSalesTaxDate != null
				&& laDtEffDt.compareTo(laDtSalesTaxDate) != -1)
		{
			if (laDtSalesTaxDate.compareTo(laDtOneYearAgo) == -1)
			{
				liRTN = SLS_TAX_DATE_ERR_GT_YR_AGO;
			}
		}
		else
		{
			liRTN = SLS_TAX_DATE_ERR_GT_EFF_DATE;
		}

		return liRTN;
	}
} // @jve:visual-info decl-index=0 visual-constraint="10,10"
