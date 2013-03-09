package com.txdot.isd.rts.client.common.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.PlateSurchargeCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * 
 * FrmEnterRegistrationREG029.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/12/2002	CQU100003459 - changed cust supplied age max
 *							length to 2.
 * J Kwik		04/15/2002	CQU100003415 - put focus to the checkbox if 
 *							available.
 * N Ting		04/17/2002	Global change for startWorking() and 
 *							doneWorking()
 * N Ting		04/18/2002	CQU100003565 - Fix message appearing and 
 *							disappearing
 * Ting/Brown	04/29/2002	CQU100003680 - Fix alignment of dollar
 * J Kwik		05/02/2002	CQU100003751 - lengthen min/max and total 
 *							fields to accomodate larger dollar amounts.
 * J Kwik		05/02/2002	CQU100003753 - Set input type to 
 *							ALPHANUMERIC_NOSPACE for cust supplied plate 
 *							number.
 * MAbs			05/08/2002	CQU100003822 - Made button panel not smush 
 *							when date changes
 * MAbs			05/14/2002	Taking out "type not expected in setData" 
 *							message CQU100003893
 * J Kwik		05/23/2002	CQU100004074 - changed cust supplied age 
 *							max length to 2.
 * S Govindappa 08/06/2002  Fixing defect 4480 - Customer supplied 
 * Robin					inventory panel enabled for headquarters 
 *							special plates by making changes to setData 
 *							method.
 * S Govindappa 08/06/2002	Fixing defect 4480 - Made changes to setData 
 * Robin					method to validate for Owner supplied plate 
 * 		 					for special plate at headquarters.
 * B Brown     	08/08/2002  Fixed for defect #4422. For special plates, 
 *							removed check in method validateOwnPlt() for
 *                          owner supplied plate being expired, 
 *							therefore circumventing error #255: THE 
 *							OWNER SUPPLIED PLATE IS AN ANNUAL PLATE AND 
 *							IS EXPIRED.
 * B Brown    	09/26/2002  For defect #4730, added a check in method 
 *							actionPerformed() for displaying the 
 *							confirmation window CTL001, "Are you sure 
 *                          you want to complete this transaction?" when 
 *							caCompleteTransactionData.getRegFeesData().
 *                          getVectFees() is empty. This occurs when 
 *							doing a registration exchange to a special 
 *							plate.
 * Ray Rowehl	11/11/2002	Defect CQU100004821
 *							remove color when disabling customer 
 *							supplied fields
 * B Brown    	12/05/2002  For defect #4770,  added an if 
 *							(gettxtPlateNo().getText().equals("")) check
 *							in both the itemStateChanged and set Data 
 *							methods, so the customer supplied plate 
 *							number and plate age, and checkbox are not 
 *							reset after pressing enter after the "fees 
 *							have been recalculated" message.
 * B Arredondo	12/16/2002	Fixing Defect# 5147. Made changes for the 
 *							user help guide so had to make changes
 *							In actionPerformed().
 * S Govindappa 01/30/2003 	Fixing Defect# 5319. Changed 
 *							actionPerformed(..) to show CTL001 screen 
 *							only for special plates.
 *                         	This was correction of fix for 4730 defect.
 * Ray Rowehl	03/14/2003	Defect 5752.  allow customer supplied check 
 *							box to clear customer supplied data when 
 *							deselected.
 * K Harrell    04/25/2003  Defect 5979  Sticker Number is now enabled 
 *                          for HQ 291
 *                          modify setData()
 * K Harrell    04/25/2003  Defect 6022  Prompt for sticker when not 
 *							specified as owner supplied in HQ 291
 *                          modify actionPerformed()
 * K Harrell    05/02/2003  Defect 5979:  Also need to set cbStkrNo = 
 *							true to force validation.
 *                          modify setData()
 * Ray Rowehl	06/11/2003  Allow entry of 10 charaters on owner 
 *							supplied sticker number
 *							modify gettxtStickerNo()
 *							defect 6237.
 * Min Wang     07/02/2003  Fixed defect 6010. Modified 
 *							actionPerformed() to check frame is visible 
 *							to prevent null pointer.
 * Min Wang		08/20/2003  Reflowed the screen for XP.
 *							defect 6043. Ver 5.1.6
 * Min Wang		08/22/2003  Modified gettxtEnterExp() to make shift tab 
 *							and Enter Field expiration month/year work.
 *                          defect 6027. Version 5.1.6
 * Min Wang		12/10/2003	Keep LblFeesRecalNotice visible when focus 
 *							lost.
 *							modify keyPressed() and mouseClicked(). 
 *							Reorganized header of the class.
 *							defect 6511  Ver 5.1.5 Fix 2
 * K Harrell	04/23/2004	Remove Sticker No field from REG029
 *							modify initialize(), actionPerformed(),
 *							itemstateChanged(), setData()
 *							deprecated validateOwnStkr()
 *							delete getstcLblSkrNo(), gettxtStickerNo() 
 *							defect 7042 Ver 5.2.0
 * K Harrell	05/05/2004	Regenerated for BuilderData()
 *							Ver 5.2.0
 * T Pederson	12/08/2004	Changed actionPerformed() to get transcd 
 *							from controller.
 *							modify actionPerformed() 
 *							defect 7044 Ver 5.2.2
 * T Pederson	12/20/2004	Add check for annual plate back in for  
 *							Special Plates (office 291). It was 
 *							inadvertently removed in defect 7042.
 *							modify actionPerformed()
 *							defect 7498 Ver 5.2.2
 * T Pederson	03/10/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	03/31/2005	Removed setNextFocusableComponent.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	05/04/2005	setFocusTraversalKeysEnabled(false) for tab 
 * 							modify gettxtEnterExp() 
 * 							defect 7885 Ver 5.2.3 
 * S Johnston	06/16/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify keyPressed
 * 							defect 8240 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3 
 * T Pederson	07/18/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	08/23/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * T Pederson	10/19/2005	Add check for Special Plates when disabling
 * 							chkCustomer checkbox. 
 * 							modify setData() 
 * 							defect 7120 Ver 5.2.3
 * B Hargrove	11/02/2005	Do not do the date edit for 'months of reg 
 * 							must be 12 - 23 months' when Reg Period 
 * 							Length = 1 (ie: Seasonal Ag) because the
 * 							user may buy from 1 to 6 months of reg.
 * 							modify handleExpDateValidation()
 * 							defect 8404 Ver 5.2.2 Fix 7
 * K Harrell	12/06/2005	Correct handling of tab off EnterMo/Yr field
 * 							modify focusLost()
 * 							defect 7885 Ver 5.2.3 
 * Jeff S.		12/15/2005	Fixed the tabbing action on the text field
 * 							gettxtEnterExp().  Moved all the code that
 * 							handles the validation back to KeyPressed
 * 							and added traversal keys to gettxtEnterExp()
 * 							so that TAB and SHIFT+TAB goes to KeyPressed
 * 							remove focusGained(), focusLost(), 
 * 								windowActivated(), windowOpened(), 
 * 								ciRecalFocusCount
 * 							modify gettxtEnterExp(), keyPressed(), 
 * 								actionPerformed()
 * 							defect 7885 Ver 5.2.3
 * K Harrell	01/24/2006	Modify to handle Alt-S to enter Customer
 * 							Supplied Plate
 * 							modify initialize(),itemStateChanged()
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	03/10/2006	Add code to be able to TAB to the 
 * 							Customer Supplied checkbox and customer  
 * 							supplied entry fields.  
 * 							modify keyPressed() 
 * 							defect 8620 Ver 5.2.3
 * T Pederson	04/17/2006	Added check to see if panel holding the 
 * 							Customer Supplied checkbox and customer  
 * 							supplied entry fields is visible before 
 * 							setting focus to either customer supplied
 * 							field.  
 * 							modify keyPressed() 
 * 							defect 8620 Ver 5.2.3
 * Jeff S.		06/22/2006	Used screen constant for CTL001 Title.
 * 							remove FRM_TITLE_CTL001
 * 							modify actionPerformed()
 * 							defect 8756 Ver 5.2.3
 * B Hargrove 	09/27/2006 	Valid vehicle registration periods are from
 * 							12 to 23 months, or 36 months for new vehicle.
 * 							Add edit for cannot be > 23 and < 36. 
 * 							modify handleExpDateValidation()
 * 							defect 8960 Ver 5.3.0 
 * Min Wang		10/11/2006	New Requirement for handling plate age 
 * 							modify setData()
 *							defect 8901 Ver Exempts
 * Min Wang		10/19/2006	comment out unused code.
 * 							modify setData()
 * 							defect 8901 Ver Exempts
 * B Hargrove 	11/07/2006 	Do not edit for cannot be > 23 and < 36
 * 							months of registration if Fixed Exp Month. 
 * 							modify handleExpDateValidation()
 * 							defect 8960 Ver Exempts 
 * Min Wang		11/07/2006  Show plate age correctly when it is multiple 
 * 							transaction on same vehicle.
 * 							comment out ciSavedPltAge
 * 							modify actionPerformed()
 * 							defect 8901 Ver Exempts
 * K Harrell	11/14/2006	Null Pointer Exception when 
 * 							caCompleteTransactionData.getRegisPenaltyFee()
 * 							is null 
 * 							modify setData() 
 * 							defect 9015 Ver Exempts
 * B Hargrove 	11/20/2006 	Do not edit for cannot be > 23 and < 36
 * 							months of registration if not eligible for
 * 							36 months of registration. 
 * 							modify handleExpDateValidation()
 * 							defect 8960 Ver Exempts
 * K Harrell	02/05/2007 Use PlateTypeCache vs. 
 * 									RegistrationRenewalsCache
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * B Hargrove 	02/12/2007 	Remove Customer Supplied fields from this
 * 							frame. In addition:
 * 							modify actionPerformed(),
 * 							handleExpDateValidation(), initialize(),
 * 							keyPressed(), setData(), 
 * 							delete TXT_CUST_SUPP_INV, TXT_CUST_SUPP_MSG, 
 * 							MAX_PLT_AGE, TXT_PLT_AGE, TXT_PLT_NO,
 * 							TXT_COMP_TRANS_QUES,
 * 							ItemListener, itemStateChanged(),
 * 							validateOwnPlt(),  
 * 							defect 9126 Ver Special Plates
 * J Rue		02/22/2007	Rename getSpclPltRegis() to 
 * 							getSpclPltRegisData()
 * 							modify handleExpDateValidation()
 * 							defect 9086 Ver Special Plates
 * B Hargrove 	04/10/2007 	Add message for 'Number of months of Special
 * 							Plate Fee charged'.
 * 							modify handleExpCountDown()
 * 							defect 9086 Ver Special Plates
 * K Harrell	05/07/2007	Use SystemProperty.isRegion()
 * 							delete REGION, cbSpecialPlates 
 * 							modify setData()
 * 							defect 9085 Ver Special Plates 
 * B Hargrove 	05/23/2007 	After further review, add 'Issue Inventory'
 * 							chekbox. In effect, this is the reverse of
 * 							'Customer Supplied', so we are basically 
 * 							adding back in the 'Customer Supplied' 
 * 							process we removed on 02/12.
 * 							modify actionPerformed(),
 * 							handleExpDateValidation(), initialize(),
 * 							keyPressed(), setData(), 
 * 							ItemListener, itemStateChanged()  
 * 							defect 9126 Ver Special Plates
 * K Harrell	05/31/2007	add'l work on above.
 * 							add handleSpclPltInv()
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates 
 * B Hargrove 	07/13/2007 	Remove old 'free pass' edit for Special Plates
 * 							that allowed them to renew a Title-in-window
 * 							for any number of months (ie: less than 12).
 * 							Add 'Previous Expirations' msg (Reg and Plt).
 * 							modify handleExpDateValidation(), setData()
 * 							defect 9126 Ver Special Plates
 * K Harrell	07/03/2007	add'l work on handleSpclPltInv() for CP
 * 							Null pointer in setData()if not same spclPlt
 * 							modify handleSpclPltInv(), setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/08/2007	Do not present "Issue From Inventory" if 
 * 							NeedsProgramCd = "O".  
 * 							modify setData()
 * 							defect 9085 Ver Special Plates 
 * B Hargrove 	07/17/2007 	Fix comparison of Min - Max dates: compare
 * 							the calculated Min / Max Month and Year, not
 * 							the calculated Min / Max Number of months.
 * 							modify setData()
 * 							defect 9126 Ver Special Plates
 * K Harrell	08/04/2007	Do not present issue from inventory if annual,
 * 							expired Special Plate selected on KEY002.
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/06/2007	Do not present issue from inventory if
 * 							Special Plate must be remanufactured or if  
 * 							SpclPlate Trans and not SPAPPL, SPAPPR
 * 							modify setData()
 * 							defect 9232 Ver Special Plate  
 * B Hargrove 	10/18/2007 	Add Customer Supplied checkbox, Plate Number,
 * 							Plate Age. These will only be visible if
 * 							'Plate transfer eligible', that is, Small
 * 							Pass or Light Truck w/ PSP or TKP.
 * 							Note: 'Issue Inventory' (used with a Special 
 * 							Plate) and 'Customer Supplied' are mutually
 * 							exclusive and occupy same screen space.
 * 							add getchkCustSupplied(),
 * 							getCustSuppliedPlateAge(),
 * 							getCustSuppliedPlateNo(), getlblPlateAge(), 
 * 							getlblPlateNo(), handleChkCustSupplied(),
 * 							validateCustSuppliedPlateAge(),
 * 							validateCustSuppliedPlateNo()
 * 							modify actionPerformed(), keyPressed(),
 * 							setData() 
 * 							defect 9366 Ver Special Plates 2
 * B Hargrove 	10/19/2007 	Undo edit for defect 8960. Allow all reg
 * 							periods from 12 to 36 months. 
 * 							modify handleExpDateValidation()
 * 							defect 9266 Ver Special Plates 2 
 * K Harrell	10/22/2007	Reset CustomerSupplied info if not selected
 * 							modify actionPerformed()
 * 							defect 9368 Ver Special Plates 2 
 * K Harrell	11/12/2007	Reset SpecialPlates Info in case return to 
 * 							REG029 and modify dates.  
 * 							modify handleSpclPltInv()
 * 							defect 9434 Ver Special Plates 2
 * K Harrell	11/17/2007	Present Customer Supplied if PTO and 
 * 							> Mandatory Plate Age. Limit PlateAge to 
 * 							2 characters.  Use Constant for Input type.
 * 							Require PlateNo to be AlphaNumericNoSpace.
 * 							Add inventory message like pre-5.4.0 when 
 * 							 Customer Supplied selected. 
 * 							add TXT_CUST_SUPP_MSG
 * 							add cbAlreadySet, ciOrigPlateAge, 
 * 							 ivjlblInvNotice
 * 							add getlblInvNotice(), initCustSupplied()
 * 							modify actionPerformed(), getCustSuppliedPlateAge(), 
 * 							 getCustSuppliedPlateNo, getJPanel3(), 
 * 							 handleChkCustSupplied(), setData() 
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	11/20/2007	Reset OwnrSuppliedInfo in actionPerformed()
 * 							in case cancel back from REG029
 * 							add resetCustomerSuppliedInfo(), 
 * 							 assignCustomerSuppliedInfo()
 * 							modify actionPerformed()
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	01/21/2008	Do not Manufacture LP Plate 
 * 							modify setData(), handleSpclPltInv()
 * 							defect 9496 Ver Tres Amigos Prep    
 * B Hargrove	02/05/2008	Check laPltTypeData object for null to avoid
 * 							null pointer exception. 'TOWP' has no row in
 * 							RTS_PLT_TYPE. 
 * 							modify setData()
 * 							defect 9540 Ver Tres Amigos Prep
 * K Harrell	06/04/2008	Do not show Customer Supplied if Plate 
 * 							Transfer Fee not selected.
 * 							modify initCustSupplied()
 * 							defect 9698 Ver Defect POS A     
 * B Hargrove	05/30/2008 	Check to see if this plate type allows
 * 							'Customer Supplied' (ie: 'OLDPLT2').
 * 							modify initCustSupplied(), setData() 
 * 							defect 9529 Ver MyPlates_POS
 * B Hargrove	07/11/2008 	Set verbiage for either Special Plate or
 * 							Vendor Plate.
 * 							modify setData() 
 * 							defect 9689 Ver MyPlates_POS
 * K Harrell	01/07/2009 Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify handleSpclPltInv(), setData()  
 *        					defect 9864 Ver Defect_POS_D 
 * B Hargrove	05/05/2010 	New rules for proration of Vendor Plate fees
 * 							when synching plate expiration when the new
 * 							reg expiration exceeds the existing Vendor 
 * 							Plate expiration.
 * 							add ciNewPltExpMo, ciNewPltExpYr, 
 * 							ciOrigVPExpMo, ciOrigVPExpYr,
 * 							SELECT_VP_RENEW,
 * 							VPPanel, jRB_VP_NoRenew, jRB_VP_OneYear,
 * 							jRB_VP_FiveYear, jRB_VP_TenYear,
 * 							txtVendorPltExpMMYYYY, lblVendorPltExpMMYYYY,
 * 							lblRateOneYr, lblRateFiveYr, lblRateTenYr,
 * 							 getVPPanel(), getjRB_VP_NoRenew(),
 * 							getjRB_VP_OneYear(), getjRB_VP_FiveYear(),
 * 							getjRB_VP_TenYear(),
 * 							getVPExpMoYrRTSTextArea(),
 * 							getLblVendorPltExpMMYYYY(),
 * 							adjVendorPltExpDt(), isVPRenewEligible() 
 * 							modify actionPerformed(), 
 * 							handleExpDateValidation(),keyPressed(),
 * 							setData() 
 * 							defect 10357 Ver 6.4.0
 * B Hargrove	07/06/2010 	Adjustment to VP number of months to charge 
 * 							(need to calculate here (vs Fees) to handle
 *                          setting number of plate months message).
 * 							Disable 'No Renew' until it's valid to have
 * 							no renewal.
 * 							Use different object to get original VP
 *                          ExpMo\Yr and Plate Term.
 * 							Use constants vs magic numbers. 
 * 							modify adjVendorPltExpDt(),
 * 							handleExpDateValidation(), setData()
 * 							(copied from Ver 6.4.0)
 * 							defect 10523 Ver 6.5.0
 * B Hargrove	07/08/2010 	If synched renew and Plt term > 1, renew 
 * 							plate at that plate term. Otherwise, if 
 * 							months to charge > 12, set to One Year Renew
 * 							modify setData()
 * 							(copied from Ver 6.4.0)
 * 							defect 10523  Ver 6.5.0 
 * Min Wang		09/21/2010	Accommodate new max lenghs of Min fee, 
 * 							Max fee, and Trans Total by visual editor. 
 * 							defect 10596 Ver 6.6.0
 * Min Wang		10/14/2010	Show dollar format for MinFeeTotal and
 * 							MaxFeeTotal.
 * 							modify SetData(),
 * 							defect 10596 Ver 6.6.0
 * B Hargrove 	10/26/2010  Token Trailer now does not print sticker.
 * 							We need to hold the new expiration year in 
 * 							the 'owner supplied' field so mf can grab it.
 * 							modify actionPerformed()
 * 							defect 10623 Ver 6.6.0
 * B Hargrove 	10/29/2010  Token Trailer now does not print sticker.
 * 							Comment out all changes. They want to store 
 * 							inv detail, just don't print sticker.
 * 							modify actionPerformed()
 * 							defect 10623 Ver 6.6.0
 * K Harrell	10/10/2011	implement RegTtlAddlInfoData.getPTOTrnsfrIndi() 
 * 							modify initCustSupplied() 
 * 							defect 11030 Ver 6.9.0 
 * K Harrell	11/16/2011	Customer Supplied input fields were not showing
 * 							up for Title.  Modified to use getNewPltDesrdIndi()
 * 							for Title && Registration.  
 *							modify setData() 
 *							defect 10951 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**  
 * Screen for Enter Registration REG029
 *
 * @version	6.9.0			11/16/2011	
 * @author	Nancy Ting
 * <br>Creation Date:		06/27/2001 08:36:38
 */

public class FrmEnterRegistrationREG029
	extends RTSDialogBox
	implements ActionListener
{
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JLabel ivjstcLblMaxiExpMonth = null;
	private JLabel ivjstcLblMaxiFeeTotal = null;
	private JLabel ivjstcLblMiniExpMonth = null;
	private JLabel ivjstcLblMiniFeeTotal = null;
	private RTSDateField ivjtxtEnterExp = null;
	private JPanel ivjFrmEnterRegistrationREG029ContentPane1 = null;
	private ButtonPanel ivjbuttonPanel = null;
	private JLabel ivjstcLblCreditRemaining = null;
	private JLabel ivjstcLblFeesRecalNotice = null;
	private JLabel ivjstcLblRegPenaltyFee = null;
	private JLabel ivjstcLblTransTotal = null;
	private RTSTextArea ivjtaEnterExpMoYr = null;
	private JLabel ivjlblCreditRemaining = null;
	private JLabel ivjlblMaxExpMoYr = null;
	private JLabel ivjlblMaxFeeTotal = null;
	private JLabel ivjlblMinExpMoYr = null;
	private JLabel ivjlblMinFeeTotal = null;
	private JLabel ivjlblTransTotal = null;

	// defect 9366 
	private javax.swing.JPanel jPanel = null;
	private JCheckBox chkIssueInv = null;
	private JTextArea jPrevExpMsg = null;
	private JCheckBox chkCustSupplied = null;
	private JLabel lblPlateNo = null;
	private JLabel lblPlateAge = null;
	private RTSInputField custSuppliedPlateNo = null;
	private RTSInputField custSuppliedPlateAge = null;
	// defect 9366 

	// defect 9368
	private RTSTextArea ivjlblInvNotice = null;
	private boolean cbAlreadySet = false;
	private int ciOrigPlateAge = -1;
	private final static String TXT_CUST_SUPP_MSG =
		"**Inventory will not be issued by RTS when specified as "
			+ "customer supplied!**";
	// end defect 9368 

	//Data Objects
	private CompleteTransactionData caCompleteTransactionData = null;
	private SpecialPlatesRegisData caOrigSpclPltRegisData = null;
	private RTSDate cdMinDate = null;
	private RTSDate cdMaxDate = null;
	private JPanel ivjJPanel5 = null;

	private static final String REGION_ACCT_CD_SUFX = "-R";
	private static final int MONTHS_IN_YR = 12;

	//screen control
	private boolean cbRecalScreen = false; //true if it is a recal
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjScrollPaneTable = null;
	private RTSTextArea ivjtaExpCountdown = null;
	private JLabel ivjstcLblRegPenaltyFeeAmt = null;

	// Text Constants 
	private final static String FRM_NAME_REG029 =
		"FrmEnterRegistrationREG029";
	private final static String FRM_TITLE_REG029 =
		"Enter registration expiration month and year                    REG029";
	private final static String TXT_CRDT_REM = "Credit Remaining:";
	private final static String TXT_FEES_RECALC_MSG =
		"Fees have been recalculated based on new expiration month and year.";
	private final static String TXT_MAX_EXP =
		"Maximum Exp. Month/Year:";
	private final static String TXT_MAX_FEE = "Maximum fee total:";
	private final static String TXT_MIN_EXP =
		"Minimum Exp. Month/Year:";
	private final static String TXT_MIN_FEE = "Minimum fee total:";
	private final static String TXT_REG_PEN_INC =
		"Registration penalty included: $";
	private final static String TXT_TRANS_TOT = "Transaction Total:";
	private final static String TXT_ENTER_EXP =
		"Enter Expiration Month and Year (MM/YYYY):";
	private final static String TXT_12MO_MSG =
		"This reflects 12 months of registration.";

	// defect 10357
	private boolean cbRecalcVP = false;
	private boolean cbSynchedRenew = false;
	private boolean cbVPRenewElig = false;
	private JPanel VPPanel = null;
	private JRadioButton jRB_VP_NoRenew = null;
	private JRadioButton jRB_VP_OneYear = null;
	private JRadioButton jRB_VP_FiveYear = null;
	private JRadioButton jRB_VP_TenYear = null;
	private RTSTextArea txtVendorPltExpMMYYYY = null;
	private final static String SELECT_VP_RENEW =
		"Vendor plate renewal:";
	private int ciNewPltExpMo = 0;
	private int ciNewPltExpYr = 0;
	private int ciOrigVPExpMo = 0;
	private int ciOrigVPExpYr = 0;
	private int ciOrigPltValidityTerm = 0;
	private JLabel lblRateOneYr = null;
	private JLabel lblRateFiveYr = null;
	private JLabel lblRateTenYr = null;
	private JLabel lblVendorPltExpMMYYYY = null;
	// end defect 10357
	
	// defect 10596
	protected Dollar cdDollarMinFeeTotal = null;
	protected Dollar cdDollarMaxFeeTotal = null;
	// end defect 10596
	
	/**
	 * FrmEnterRegistrationREG029
	 */
	public FrmEnterRegistrationREG029()
	{
		super();
		initialize();
	}

	/**
	 * FrmEnterRegistrationREG029 constructor
	 * 
	 * @param aaOwner JDialog
	 */
	public FrmEnterRegistrationREG029(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmEnterRegistrationREG029 constructor
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmEnterRegistrationREG029(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when Enter/Cancel/Help is clicked
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		//defect 6101
		// Check frame is visible to prevent null pointer
		// defect 10357
		// Whether or not to show the 'fees have been recalced' msg
		cbRecalcVP = false;
		// end defect 10357

		if (!startWorking() || !isVisible())
		{
			//end defect 6101
			return;
		}
		try
		{
			clearAllColor(this);
			// defect 10357
			// Handle Vendor Plate validity term radio buttons
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter()
				|| aaAE.getSource() instanceof JRadioButton)
			{
				try
				{
					if (aaAE.getSource() instanceof JRadioButton)
					{
						cbRecalcVP = true;
					}
					if (handleExpDateValidation())
					{
						gettxtEnterExp().requestFocus();
						return;
					}
				}
				catch (RTSException aeRTSException)
				{
					RTSException leEx = new RTSException();
					leEx.addException(aeRTSException, gettxtEnterExp());
					leEx.displayError(this);
					leEx.getFirstComponent().requestFocus();
					return;
				}
				RTSDate ldEnterExpDate = gettxtEnterExp().getDate();
				caCompleteTransactionData.setExpYr(
					ldEnterExpDate.getYear());

				caCompleteTransactionData.setExpMo(
					ldEnterExpDate.getMonth());

				// defect 9368 
				// Reset in case return to REG029 and modify   
				resetCustomerSuppliedInfo();
				// end defect 9368 

				if (caCompleteTransactionData
					.getVehicleInfo()
					.isSpclPlt())
				{
					handleSpclPltInv();
				}
				else
				{
					// defect 9366 / 9368  
					// Validate Customer Supplied fields
					if (getchkCustSupplied().isSelected())
					{
						RTSException leRTSEx = new RTSException();
						validateCustSuppliedPlateNo(leRTSEx);
						validateCustSuppliedPlateAge(leRTSEx);
						if (leRTSEx.isValidationError())
						{
							leRTSEx.displayError(this);
							leRTSEx.getFirstComponent().requestFocus();
							return;
						}
						else
						{
							assignCustomerSuppliedInfo();
						}
					}
					// end defect 9366 /9368 
				}
				
				// defect 10623
				// Since we no longer have a sticker for Token Trailer,
				// set the new RegExpYr in Owner Supplied Exp Yr.					
//				if (caCompleteTransactionData.getVehicleInfo().
//					getRegData().getRegClassCd() == 
//						RegistrationConstant.REGCLASSCD_TOKEN_TRLR)
//				{
//					caCompleteTransactionData.getVehicleInfo()
//						.getRegData().setOwnrSuppliedExpYr(
//							caCompleteTransactionData.getExpYr());
//				}
				// end defect 10623						

				if (displayMsg572())
				{
					new RTSException(572).displayError(this);
				}
				getController().processData(
					AbstractViewController.ENTER,
					caCompleteTransactionData);
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caCompleteTransactionData);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				String lsTransCd = getController().getTransCode();

				if (UtilityMethods.isMFUP())
				{
					if (lsTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.REG029A);
					}
					else if (lsTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.REG029B);
					}
					else if (
						lsTransCd.equals(TransCdConstant.TITLE)
							|| lsTransCd.equals(TransCdConstant.NONTTL)
							|| lsTransCd.equals(TransCdConstant.REJCOR))
					{
						RTSHelp.displayHelp(RTSHelp.REG029D);
					}
				} //if MF is unavailable
				else
				{
					if (lsTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.REG029E);
					}
					else if (
						lsTransCd.equals(TransCdConstant.TITLE)
							|| lsTransCd.equals(TransCdConstant.NONTTL)
							|| lsTransCd.equals(TransCdConstant.REJCOR))
					{
						RTSHelp.displayHelp(RTSHelp.REG029F);
					}
				}
			}
		// defect 9366 
			else if (aaAE.getSource() == getchkCustSupplied())
			{
				handleChkCustSupplied();
			}
			// end defect 9366 
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Each time the 'Enter Expiration Month and Year' is set\changed,
	 * or the user selects one of the 'Renew Vendor Plate' radio buttons,
	 * this method is called to adjust the Vendor Plate expiration.
	 * If the Vendor Plate expires before the end of the new registration,
	 * the VP is prorated up to the new reg ExpMo\Yr.
	 * If the user decides to renew (1\5\10 years), then the VP is renewed
	 * for that length of time from the original VP ExpMo\Yr.
	 */
	private void adjVendorPltExpDt()
	{
		if (PlateTypeCache
			.isVendorPlate(caCompleteTransactionData
				.getVehicleInfo().getRegData().getRegPltCd())
			&& isVPRenewEligible(ciOrigVPExpMo, ciOrigVPExpYr))
		{
			
			RTSDate ldEnterExpDate = gettxtEnterExp().getDate();

			int liToYr = ldEnterExpDate.getYear();
			int liToMo = ldEnterExpDate.getMonth();
			int liRegMonths = gettxtEnterExp().getDate().getYear() * 12 
								+ gettxtEnterExp().getDate().getMonth();

			int liPltMonths = ciOrigVPExpYr * 12 + ciOrigVPExpMo;
			
			int liVPMonthsToCharge = liRegMonths - liPltMonths;

			if (liVPMonthsToCharge > 0)
			{
				ciNewPltExpMo = liToMo;
				ciNewPltExpYr = liToYr;
			}
			else
			{
				ciNewPltExpMo = ciOrigVPExpMo;
				ciNewPltExpYr = ciOrigVPExpYr;
			}

			// 'No Renew' will be enabled if it is possible to buy less
			// than 12 months of plate. Once 12+ months are bought, 'No
			// Renew' becomes disabled. If 'No Renew' is selected when  
			// purchase becomes 12+ months, the selection will move to 
			// 'One Year' and plate is charged at One Year rate.
			getjRB_VP_NoRenew().setEnabled(false);
			if (cbVPRenewElig && liVPMonthsToCharge < 12 && 
				!cbSynchedRenew)
			{
				getjRB_VP_NoRenew().setEnabled(true);
			}

			if (getjRB_VP_FiveYear().isSelected())
			{
				liVPMonthsToCharge = 60;
				ciNewPltExpMo = ciOrigVPExpMo;
				ciNewPltExpYr = ciOrigVPExpYr + 5;
				caCompleteTransactionData
					.getVehicleInfo()
					.getSpclPltRegisData()
					.setPltValidityTerm(5);
			}
			else if (getjRB_VP_TenYear().isSelected())
			{
				liVPMonthsToCharge = 120;
				ciNewPltExpMo = ciOrigVPExpMo;
				ciNewPltExpYr = ciOrigVPExpYr + 10;
				caCompleteTransactionData
					.getVehicleInfo()
					.getSpclPltRegisData()
					.setPltValidityTerm(10);
			}
			else
			{
				// Set to One Year Renew if >= 12 months
				if (liVPMonthsToCharge >= 12)
				{
					getjRB_VP_NoRenew().setEnabled(false);
					getjRB_VP_OneYear().setSelected(true);
					caCompleteTransactionData
						.getVehicleInfo()
						.getSpclPltRegisData()
						.setPltValidityTerm(1);
				}
				// If < 12 months, user may or may not Renew
				else if (getjRB_VP_OneYear().isSelected())
				{
					liVPMonthsToCharge = 12;
					ciNewPltExpMo = ciOrigVPExpMo;
					ciNewPltExpYr = ciOrigVPExpYr + 1;
					 
					caCompleteTransactionData
						.getVehicleInfo()
						.getSpclPltRegisData()
						.setPltValidityTerm(1);
				}
				else
				{
					getjRB_VP_NoRenew().setSelected(cbVPRenewElig);
					caCompleteTransactionData
						.getVehicleInfo()
						.getSpclPltRegisData()
						.setPltValidityTerm(ciOrigPltValidityTerm);
				}
			}
			
			// Reset 'number of months of plate sold' message			
			String lsTxtSpclPltMoSold = CommonConstant.STR_SPACE_EMPTY;
			if (liVPMonthsToCharge > 0)
			{
				lsTxtSpclPltMoSold = "This reflects " + 
					liVPMonthsToCharge;
				if (liVPMonthsToCharge == 1)
				{
					lsTxtSpclPltMoSold =
						lsTxtSpclPltMoSold + 
							" month of Plate Fee.";
				}
				else
				{
					lsTxtSpclPltMoSold =
						lsTxtSpclPltMoSold
							+ " months of Plate Fee.";
				}
			}
			caCompleteTransactionData.setTxtSpclPltMoSold(
				lsTxtSpclPltMoSold);
			handleExpCountDown();
			

			caCompleteTransactionData
				.getVehicleInfo()
				.getSpclPltRegisData()
				.setPltExpMo(ciNewPltExpMo);
			caCompleteTransactionData
				.getVehicleInfo()
				.getSpclPltRegisData()
				.setPltExpYr(ciNewPltExpYr);
				
			getLblVendorPltExpMMYYYY().setText(
				UtilityMethods.addPadding(
					new String[] {
						String.valueOf(ciNewPltExpMo),
						CommonConstant.STR_SLASH,
						String.valueOf(ciNewPltExpYr)},
					new int[] { 2, 1, 4 },
					CommonConstant.STR_ZERO));
			

			caCompleteTransactionData.setSpclPlateNoMoCharge(
				Math.max(0, liVPMonthsToCharge));
		}
	}

	/** 
	 *  Assign Customer Supplied Info   
	 */
	private void assignCustomerSuppliedInfo()
	{
		caCompleteTransactionData.setOwnrSuppliedPltNo(
			getCustSuppliedPlateNo().getText().trim());

		int liPlateAge =
			Integer.parseInt(getCustSuppliedPlateAge().getText());

		caCompleteTransactionData
			.getVehicleInfo()
			.getRegData()
			.setRegPltAge(
			liPlateAge);

		caCompleteTransactionData
			.getVehicleInfo()
			.getRegData()
			.setOwnrSuppliedExpYr(caCompleteTransactionData.getExpYr());

	}

	/**
	 * Displays Message 572 when expiration month is changed
	 * 
	 * @return boolean true if Msg 572 is displayed
	 */
	private boolean displayMsg572()
	{
		MFVehicleData laMFVehicleData =
			caCompleteTransactionData.getOrgVehicleInfo();
		if (laMFVehicleData != null)
		{
			RegistrationData laRegistrationData =
				laMFVehicleData.getRegData();
			if (laRegistrationData != null)
			{
				if (gettxtEnterExp().getDate().getMonth()
					!= laRegistrationData.getRegExpMo()
					&& laRegistrationData.getRegExpMo() != 0)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Return the buttonPanel property value.
	 * 
	 * @return gui.ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setName("buttonPanel");
				ivjbuttonPanel.setLayout(new FlowLayout());
				ivjbuttonPanel.setBounds(7, 508, 601, 49);
				ivjbuttonPanel.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				getbuttonPanel().addActionListener(this);
				//getbuttonPanel().addKeyListener(this);
				getbuttonPanel().setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbuttonPanel;
	}

	/**
	 * This method initializes Customer Supplied checkbox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getchkCustSupplied()
	{
		if (chkCustSupplied == null)
		{
			chkCustSupplied = new javax.swing.JCheckBox();
			chkCustSupplied.setBounds(5, 7, 140, 21);
			chkCustSupplied.setText("Customer Supplied");
			chkCustSupplied.setMnemonic(java.awt.event.KeyEvent.VK_C);
			chkCustSupplied.addActionListener(this);
		}
		return chkCustSupplied;
	}

	/**
	 * This method initializes chkIssueInv for Issue Inventory
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getchkIssueInv()
	{
		if (chkIssueInv == null)
		{
			chkIssueInv = new javax.swing.JCheckBox();
			chkIssueInv.setBounds(207, 327, 146, 21);
			chkIssueInv.setName("issueInv");
			chkIssueInv.setText("Issue from Inventory");
			chkIssueInv.setMnemonic(java.awt.event.KeyEvent.VK_I);
		}
		return chkIssueInv;
	}

	/**
	 * This method initializes Customer Supplied Plate Age
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField getCustSuppliedPlateAge()
	{
		if (custSuppliedPlateAge == null)
		{
			// defect 9368 
			custSuppliedPlateAge =
				new RTSInputField(RTSInputField.NUMERIC_ONLY);
			custSuppliedPlateAge.setMaxLength(2);
			// end defect 9368 
			custSuppliedPlateAge.setBounds(237, 32, 28, 20);
			custSuppliedPlateAge.setText("");
			custSuppliedPlateAge.setName("ivjtxtPlateAge");
		}
		return custSuppliedPlateAge;
	}

	/**
	 * This method initializes Customer Supplied Plate Number
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField getCustSuppliedPlateNo()
	{
		if (custSuppliedPlateNo == null)
		{
			// defect 9368 
			custSuppliedPlateNo =
				new RTSInputField(RTSInputField.ALPHANUMERIC_NOSPACE);
			// end defect 9368 
			custSuppliedPlateNo.setBounds(237, 7, 75, 20);
			custSuppliedPlateNo.setText("");
			custSuppliedPlateNo.setName("ivjtxtPlateNo");
			custSuppliedPlateNo.setMaxLength(7);
		}
		return custSuppliedPlateNo;
	}

	/**
	 * Return the FrmEnterRegistrationREG029ContentPane1 property value
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmEnterRegistrationREG029ContentPane1()
	{
		if (ivjFrmEnterRegistrationREG029ContentPane1 == null)
		{
			try
			{
				ivjFrmEnterRegistrationREG029ContentPane1 =
					new JPanel();
				ivjFrmEnterRegistrationREG029ContentPane1.setName(
					"FrmEnterRegistrationREG029ContentPane1");
				ivjFrmEnterRegistrationREG029ContentPane1.setLayout(
					null);
				ivjFrmEnterRegistrationREG029ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmEnterRegistrationREG029ContentPane1
					.setMinimumSize(
					new Dimension(600, 460));
				getFrmEnterRegistrationREG029ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());
				getFrmEnterRegistrationREG029ContentPane1().add(
					getJPanel2(),
					getJPanel2().getName());
				getFrmEnterRegistrationREG029ContentPane1().add(
					getJPanel3(),
					getJPanel3().getName());
				getFrmEnterRegistrationREG029ContentPane1().add(
					getJPanel(),
					getJPanel().getName());
				ivjFrmEnterRegistrationREG029ContentPane1.add(
					getchkIssueInv(),
					getchkIssueInv().getName());
				ivjFrmEnterRegistrationREG029ContentPane1.add(
					getPrevExpMsg(),
					null);
				ivjFrmEnterRegistrationREG029ContentPane1.add(
					getJPanel(),
					null);
				ivjFrmEnterRegistrationREG029ContentPane1.add(
					getbuttonPanel(), 
					null);
				ivjFrmEnterRegistrationREG029ContentPane1.add(
					getVPPanel(), 
					null);
				ivjFrmEnterRegistrationREG029ContentPane1.add(
					getVPExpMoYrTextArea(), 
					null);
				ivjFrmEnterRegistrationREG029ContentPane1.add(
					getLblVendorPltExpMMYYYY(), 
					null);
				ivjFrmEnterRegistrationREG029ContentPane1.add(
					getJPanel5(), 
					null);
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjFrmEnterRegistrationREG029ContentPane1;
	}

	/**
	 * This method initializes jPanel Customer Supplied fields
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new javax.swing.JPanel();
			jPanel.setLayout(null);
			jPanel.add(
				getCustSuppliedPlateNo(),
				getCustSuppliedPlateNo().getName());
			jPanel.add(
				getCustSuppliedPlateAge(),
				getCustSuppliedPlateAge().getName());
			jPanel.add(getlblPlateNo(), getlblPlateNo().getName());
			jPanel.add(getlblPlateAge(), getlblPlateAge().getName());
			jPanel.add(
				getchkCustSupplied(),
				getchkCustSupplied().getName());
			jPanel.setBounds(7, 325, 332, 56);
		}
		return jPanel;
	}

	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setPreferredSize(new Dimension(605, 50));
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(new Dimension(558, 50));
				ivjJPanel1.setBounds(5, 5, 610, 50);

				ivjJPanel1.add(getstcLblMiniExpMonth(), null);
				ivjJPanel1.add(getlblMinExpMoYr(), null);
				ivjJPanel1.add(getstcLblMiniFeeTotal(), null);
				ivjJPanel1.add(getlblMinFeeTotal(), null);
				ivjJPanel1.add(getstcLblMaxiExpMonth(), null);
				ivjJPanel1.add(getlblMaxExpMoYr(), null);
				ivjJPanel1.add(getstcLblMaxiFeeTotal(), null);
				ivjJPanel1.add(getlblMaxFeeTotal(), null);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setLayout(null);
				ivjJPanel2.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjJPanel2.setMinimumSize(new Dimension(558, 35));
				ivjJPanel2.setBounds(5, 55, 610, 48);
				getJPanel2().add(
					gettxtEnterExp(),
					gettxtEnterExp().getName());
				getJPanel2().add(
					getstcLblRegPenaltyFee(),
					getstcLblRegPenaltyFee().getName());
				getJPanel2().add(
					getstcLblRegPenaltyFeeAmt(),
					getstcLblRegPenaltyFeeAmt().getName());
				getJPanel2().add(
					gettaEnterExpMoYr(),
					gettaEnterExpMoYr().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel2;
	}

	/**
	 * Return the JPanel3 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new JPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setLayout(null);
				ivjJPanel3.add(gettaExpCountdown(), null);
				ivjJPanel3.add(getstcLblTransTotal(), null);
				ivjJPanel3.add(getlblTransTotal(), null);
				ivjJPanel3.add(getstcLblCreditRemaining(), null);
				ivjJPanel3.add(getlblCreditRemaining(), null);
				ivjJPanel3.add(getJScrollPane1(), null);
				// defect 9368 
				ivjJPanel3.add(getlblInvNotice(), null);
				// end defect 9368 
				ivjJPanel3.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjJPanel3.setMinimumSize(new Dimension(558, 146));
				ivjJPanel3.setBounds(5, 103, 610, 220);

				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel3;
	} /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel5()
	{
		if (ivjJPanel5 == null)
		{
			try
			{
				ivjJPanel5 = new JPanel();
				ivjJPanel5.setName("JPanel5");
				ivjJPanel5.setLayout(new GridBagLayout());
				ivjJPanel5.setVisible(true);

				GridBagConstraints laConstraintsstcLblFeesRecalNotice =
					new GridBagConstraints();
				laConstraintsstcLblFeesRecalNotice.gridx = 1;
				laConstraintsstcLblFeesRecalNotice.gridy = 1;
				laConstraintsstcLblFeesRecalNotice.ipadx = 91;
				laConstraintsstcLblFeesRecalNotice.insets =
					new Insets(5, 56, 5, 56);
				getJPanel5().add(
					getstcLblFeesRecalNotice(),
					laConstraintsstcLblFeesRecalNotice);
				// user code begin {1}
				ivjJPanel5.setBounds(1, 477, 610, 24);
				getJPanel5().setFocusable(false);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel5;
	}
	
	/**
	 * This method initializes jRB_VP_NoRenew
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getjRB_VP_NoRenew() {
		if(jRB_VP_NoRenew == null) {
			jRB_VP_NoRenew = new javax.swing.JRadioButton();
			jRB_VP_NoRenew.setBounds(8, 23, 87, 21);
			jRB_VP_NoRenew.setText("No Renew");
			jRB_VP_NoRenew.setMnemonic(java.awt.event.KeyEvent.VK_N);
			getjRB_VP_NoRenew().addActionListener(this);
		}
		return jRB_VP_NoRenew;
	}
	
	/**
	 * This method initializes jRB_VP_OneYear
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getjRB_VP_OneYear() {
		if(jRB_VP_OneYear == null) {
			jRB_VP_OneYear = new javax.swing.JRadioButton();
			jRB_VP_OneYear.setBounds(8, 47, 80, 21);
			jRB_VP_OneYear.setText("One Year");
			jRB_VP_OneYear.setMnemonic(java.awt.event.KeyEvent.VK_O);
			getjRB_VP_OneYear().addActionListener(this);
		}
		return jRB_VP_OneYear;
	}
	
	/**
	 * This method initializes jRB_VP_FiveYear
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getjRB_VP_FiveYear() {
		if(jRB_VP_FiveYear == null) {
			jRB_VP_FiveYear = new javax.swing.JRadioButton();
			jRB_VP_FiveYear.setBounds(8, 71, 80, 21);
			jRB_VP_FiveYear.setText("Five Year");
			jRB_VP_FiveYear.setMnemonic(java.awt.event.KeyEvent.VK_F);
			getjRB_VP_FiveYear().addActionListener(this);
		}
		return jRB_VP_FiveYear;
	}
	
	/**
	 * This method initializes jRB_VP_TenYear
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getjRB_VP_TenYear() {
		if(jRB_VP_TenYear == null) {
			jRB_VP_TenYear = new javax.swing.JRadioButton();
			jRB_VP_TenYear.setBounds(8, 95, 80, 21);
			jRB_VP_TenYear.setText("Ten Year");
			jRB_VP_TenYear.setMnemonic(java.awt.event.KeyEvent.VK_T);
			getjRB_VP_TenYear().addActionListener(this);
		}
		return jRB_VP_TenYear;
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
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setBackground(new Color(204, 204, 204));
				getJScrollPane1().setViewportView(getScrollPaneTable());
				// user code begin {1}
				ivjJScrollPane1.setBounds(201, 9, 393, 149);
				ivjJScrollPane1.setFont(
					new java.awt.Font(
						"Dialog",
						java.awt.Font.ITALIC,
						12));
				ivjJScrollPane1.getViewport().setBackground(
					Color.white);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * Return the lblTransaction1 property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblCreditRemaining()
	{
		if (ivjlblCreditRemaining == null)
		{
			try
			{
				ivjlblCreditRemaining = new JLabel();
				ivjlblCreditRemaining.setBounds(516, 193, 59, 14);
				ivjlblCreditRemaining.setName("lblCreditRemaining");
				ivjlblCreditRemaining.setText("$10.54");
				ivjlblCreditRemaining.setMaximumSize(
					new Dimension(38, 14));
				ivjlblCreditRemaining.setMinimumSize(
					new Dimension(38, 14));
				ivjlblCreditRemaining.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjlblCreditRemaining;
	}

	/**
	 * Return the RTSTextArea1 property value
	 * 
	 * @return RTSTextArea lblInvNotice
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTextArea getlblInvNotice()
	{
		if (ivjlblInvNotice == null)
		{
			try
			{
				ivjlblInvNotice = new RTSTextArea();
				ivjlblInvNotice.setName("lblInvNotice");
				ivjlblInvNotice.setLineWrap(true);
				ivjlblInvNotice.setWrapStyleWord(true);
				ivjlblInvNotice.setBounds(8, 166, 167, 50);
				ivjlblInvNotice.setText(TXT_CUST_SUPP_MSG);
				ivjlblInvNotice.setBackground(new Color(204, 204, 204));
				ivjlblInvNotice.setForeground(java.awt.Color.black);
				ivjlblInvNotice.setFont(new Font("Arial", 1, 12));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjlblInvNotice;
	}

	/**
	 * Return the lblMaxExpMonth property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblMaxExpMoYr()
	{
		if (ivjlblMaxExpMoYr == null)
		{
			try
			{
				ivjlblMaxExpMoYr = new JLabel();
				ivjlblMaxExpMoYr.setBounds(459, 4, 45, 14);
				ivjlblMaxExpMoYr.setName("lblMaxExpMoYr");
				ivjlblMaxExpMoYr.setText("06/2003");
				ivjlblMaxExpMoYr.setMaximumSize(new Dimension(45, 14));
				ivjlblMaxExpMoYr.setMinimumSize(new Dimension(45, 14));
				ivjlblMaxExpMoYr.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjlblMaxExpMoYr;
	}

	/**
	 * Return the lblMaxFeeTotal property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblMaxFeeTotal()
	{
		if (ivjlblMaxFeeTotal == null)
		{
			try
			{
				ivjlblMaxFeeTotal = new JLabel();
				ivjlblMaxFeeTotal.setBounds(455, 32, 130, 14);
				ivjlblMaxFeeTotal.setName("lblMaxFeeTotal");
				ivjlblMaxFeeTotal.setText("61.90");
				ivjlblMaxFeeTotal.setMaximumSize(new Dimension(40, 14));
				ivjlblMaxFeeTotal.setMinimumSize(new Dimension(40, 14));
				ivjlblMaxFeeTotal.setHorizontalAlignment(2);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjlblMaxFeeTotal;
	}

	/**
	 * Return the lblExpMonth property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblMinExpMoYr()
	{
		if (ivjlblMinExpMoYr == null)
		{
			try
			{
				ivjlblMinExpMoYr = new JLabel();
				ivjlblMinExpMoYr.setBounds(203, 4, 45, 14);
				ivjlblMinExpMoYr.setName("lblMinExpMoYr");
				ivjlblMinExpMoYr.setText("06/2001");
				ivjlblMinExpMoYr.setMaximumSize(new Dimension(45, 14));
				ivjlblMinExpMoYr.setMinimumSize(new Dimension(45, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjlblMinExpMoYr;
	}

	/**
	 * Return the lblMiniFeeTotal property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblMinFeeTotal()
	{
		if (ivjlblMinFeeTotal == null)
		{
			try
			{
				ivjlblMinFeeTotal = new JLabel();
				ivjlblMinFeeTotal.setBounds(199, 32, 115, 14);
				ivjlblMinFeeTotal.setName("lblMinFeeTotal");
				ivjlblMinFeeTotal.setText("16.40");
				ivjlblMinFeeTotal.setMaximumSize(new Dimension(40, 14));
				ivjlblMinFeeTotal.setMinimumSize(new Dimension(40, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjlblMinFeeTotal;
	}

	/**
	 * This method initializes Plate Age label
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getlblPlateAge()
	{
		if (lblPlateAge == null)
		{
			lblPlateAge = new javax.swing.JLabel();
			lblPlateAge.setBounds(164, 32, 69, 20);
			lblPlateAge.setText("Plate Age:");
		}
		return lblPlateAge;
	}

	/**
	 * This method initializes Plate No label
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getlblPlateNo()
	{
		if (lblPlateNo == null)
		{
			lblPlateNo = new javax.swing.JLabel();
			lblPlateNo.setBounds(164, 7, 69, 20);
			lblPlateNo.setText("Plate No:");
		}
		return lblPlateNo;
	}
	
	/**
	 * This method initializes lblRateOneYr
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLblRateOneYr() {
		if(lblRateOneYr == null) {
			lblRateOneYr = new javax.swing.JLabel();
			lblRateOneYr.setBounds(94, 47, 53, 19);
			lblRateOneYr.setText("$1.00");
			lblRateOneYr.setName("lblRateOneYr");
			lblRateOneYr.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
			lblRateOneYr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		}
		return lblRateOneYr;
	}
	
	/**
	 * This method initializes lblRateFiveYr
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLblRateFiveYr() {
		if(lblRateFiveYr == null) {
			lblRateFiveYr = new javax.swing.JLabel();
			lblRateFiveYr.setBounds(94, 71, 53, 19);
			lblRateFiveYr.setText("$5.00");
			lblRateFiveYr.setName("lblRateFiveYr");
			lblRateFiveYr.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
			lblRateFiveYr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		}
		return lblRateFiveYr;
	}
	
	/**
	 * This method initializes lblRateTenYr
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLblRateTenYr() {
		if(lblRateTenYr == null) {
			lblRateTenYr = new javax.swing.JLabel();
			lblRateTenYr.setBounds(94, 95, 53, 19);
			lblRateTenYr.setText("$10.00");
			lblRateTenYr.setName("lblRateTenYr");
			lblRateTenYr.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
			lblRateTenYr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			lblRateTenYr.setRequestFocusEnabled(false);
		}
		return lblRateTenYr;
	}

	/**
	 * Return the lblTransaction property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblTransTotal()
	{
		if (ivjlblTransTotal == null)
		{
			try
			{
				ivjlblTransTotal = new JLabel();
				ivjlblTransTotal.setBounds(485, 169, 90, 14);
				ivjlblTransTotal.setName("lblTransTotal");
				ivjlblTransTotal.setText("$10.54");
				ivjlblTransTotal.setMaximumSize(new Dimension(45, 14));
				ivjlblTransTotal.setMinimumSize(new Dimension(45, 14));
				ivjlblTransTotal.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjlblTransTotal;
	}
	
	/**
	 * This method initializes lblVendorPltExpMMYYYY
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLblVendorPltExpMMYYYY() {
		if(lblVendorPltExpMMYYYY == null) {
			lblVendorPltExpMMYYYY = new javax.swing.JLabel();
			lblVendorPltExpMMYYYY.setBounds(525, 358, 78, 16);
			lblVendorPltExpMMYYYY.setText("01/1900");
			lblVendorPltExpMMYYYY.setName("lblVendorPltExpMMYYYY");
		}
		return lblVendorPltExpMMYYYY;
	}

	/**
	 * This method initializes Previous Reg and Plt Expiration message
	 * 
	 * @return jPrevExpMsg
	 */
	private javax.swing.JTextArea getPrevExpMsg()
	{
		if (jPrevExpMsg == null)
		{
			jPrevExpMsg = new javax.swing.JTextArea();
			jPrevExpMsg.setSize(190, 48);
			jPrevExpMsg.setEditable(false);
			jPrevExpMsg.setFont(
				new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
			jPrevExpMsg.setLineWrap(true);
			jPrevExpMsg.setLocation(9, 326);
			jPrevExpMsg.setBackground(
				new java.awt.Color(204, 204, 204));
		}
		return jPrevExpMsg;
	}

	/**
	 * Return the ScrollPaneTable property value
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable getScrollPaneTable()
	{
		if (ivjScrollPaneTable == null)
		{
			try
			{
				ivjScrollPaneTable = new RTSTable();
				ivjScrollPaneTable.setName("ScrollPaneTable");
				getJScrollPane1().setColumnHeaderView(
					ivjScrollPaneTable.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjScrollPaneTable.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjScrollPaneTable.setShowVerticalLines(false);
				ivjScrollPaneTable.setShowHorizontalLines(false);
				ivjScrollPaneTable.setIntercellSpacing(
					new Dimension(0, 0));
				ivjScrollPaneTable.setBounds(37, 0, 360, 146);
				// user code begin {1}
				ivjScrollPaneTable.setModel(new TMREG029());
				TMREG029 laTableModel =
					(TMREG029) ivjScrollPaneTable.getModel();
				ivjScrollPaneTable.setModel(laTableModel);
				TableColumn laTableColumnA =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(0));
				laTableColumnA.setPreferredWidth(270);
				TableColumn laTableColumnB =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(1));
				laTableColumnB.setPreferredWidth(80);
				ivjScrollPaneTable.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjScrollPaneTable.init();
				laTableColumnA.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnB.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
						RTSTable.RIGHT));
				ivjScrollPaneTable.setTraversable(false);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjScrollPaneTable;
	}

	/**
	 * Return the stcLblCreditRemaining property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblCreditRemaining()
	{
		if (ivjstcLblCreditRemaining == null)
		{
			try
			{
				ivjstcLblCreditRemaining = new JLabel();
				ivjstcLblCreditRemaining.setBounds(237, 193, 123, 16);
				ivjstcLblCreditRemaining.setName(
					"stcLblCreditRemaining");
				ivjstcLblCreditRemaining.setAlignmentX(
					Component.CENTER_ALIGNMENT);
				ivjstcLblCreditRemaining.setText(TXT_CRDT_REM);
				ivjstcLblCreditRemaining.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblCreditRemaining;
	}

	/**
	 * Return the stcLblFeesRecalNotice property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblFeesRecalNotice()
	{
		if (ivjstcLblFeesRecalNotice == null)
		{
			try
			{
				ivjstcLblFeesRecalNotice = new JLabel();
				ivjstcLblFeesRecalNotice.setName(
					"stcLblFeesRecalNotice");
				ivjstcLblFeesRecalNotice.setText(TXT_FEES_RECALC_MSG);
				ivjstcLblFeesRecalNotice.setForeground(Color.red);
				ivjstcLblFeesRecalNotice.setHorizontalAlignment(
					SwingConstants.CENTER);
				ivjstcLblFeesRecalNotice.setVisible(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblFeesRecalNotice;
	}

	/**
	 * Return the stcLblMaxiExpMonth property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblMaxiExpMonth()
	{
		if (ivjstcLblMaxiExpMonth == null)
		{
			try
			{
				ivjstcLblMaxiExpMonth = new JLabel();
				ivjstcLblMaxiExpMonth.setBounds(292, 4, 153, 14);
				ivjstcLblMaxiExpMonth.setName("stcLblMaxiExpMonth");
				ivjstcLblMaxiExpMonth.setText(TXT_MAX_EXP);
				ivjstcLblMaxiExpMonth.setMaximumSize(
					new Dimension(153, 14));
				ivjstcLblMaxiExpMonth.setMinimumSize(
					new Dimension(153, 14));
				ivjstcLblMaxiExpMonth.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblMaxiExpMonth;
	}

	/**
	 * Return the stcLblMaxiFeeTotal property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblMaxiFeeTotal()
	{
		if (ivjstcLblMaxiFeeTotal == null)
		{
			try
			{
				ivjstcLblMaxiFeeTotal = new JLabel();
				ivjstcLblMaxiFeeTotal.setBounds(321, 32, 124, 14);
				ivjstcLblMaxiFeeTotal.setName("stcLblMaxiFeeTotal");
				ivjstcLblMaxiFeeTotal.setText(TXT_MAX_FEE);
				ivjstcLblMaxiFeeTotal.setMaximumSize(
					new Dimension(108, 14));
				ivjstcLblMaxiFeeTotal.setMinimumSize(
					new Dimension(108, 14));
				ivjstcLblMaxiFeeTotal.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblMaxiFeeTotal;
	}

	/**
	 * Return the stcLblMiniExpMonth property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblMiniExpMonth()
	{
		if (ivjstcLblMiniExpMonth == null)
		{
			try
			{
				ivjstcLblMiniExpMonth = new JLabel();
				ivjstcLblMiniExpMonth.setBounds(27, 4, 164, 14);
				ivjstcLblMiniExpMonth.setName("stcLblMiniExpMonth");
				ivjstcLblMiniExpMonth.setText(TXT_MIN_EXP);
				ivjstcLblMiniExpMonth.setMaximumSize(
					new Dimension(145, 14));
				ivjstcLblMiniExpMonth.setMinimumSize(
					new Dimension(145, 14));
				ivjstcLblMiniExpMonth.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblMiniExpMonth;
	}

	/**
	 * Return the stcLblMiniFeeTotal property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblMiniFeeTotal()
	{
		if (ivjstcLblMiniFeeTotal == null)
		{
			try
			{
				ivjstcLblMiniFeeTotal = new JLabel();
				ivjstcLblMiniFeeTotal.setBounds(59, 32, 132, 14);
				ivjstcLblMiniFeeTotal.setName("stcLblMiniFeeTotal");
				ivjstcLblMiniFeeTotal.setText(TXT_MIN_FEE);
				ivjstcLblMiniFeeTotal.setMaximumSize(
					new Dimension(109, 14));
				ivjstcLblMiniFeeTotal.setMinimumSize(
					new Dimension(109, 14));
				ivjstcLblMiniFeeTotal.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblMiniFeeTotal;
	}

	/**
	 * Return the stcLblRegPenaltyFee property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblRegPenaltyFee()
	{
		if (ivjstcLblRegPenaltyFee == null)
		{
			try
			{
				ivjstcLblRegPenaltyFee = new JLabel();
				ivjstcLblRegPenaltyFee.setName("stcLblRegPenaltyFee");
				ivjstcLblRegPenaltyFee.setText(TXT_REG_PEN_INC);
				ivjstcLblRegPenaltyFee.setBounds(324, 15, 182, 14);
				ivjstcLblRegPenaltyFee.setForeground(Color.red);
				ivjstcLblRegPenaltyFee.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblRegPenaltyFee.setVisible(false);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblRegPenaltyFee;
	}

	/**
	 * Return the stcLblRegPenaltyFeeAmt property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblRegPenaltyFeeAmt()
	{
		if (ivjstcLblRegPenaltyFeeAmt == null)
		{
			try
			{
				ivjstcLblRegPenaltyFeeAmt = new JLabel();
				ivjstcLblRegPenaltyFeeAmt.setName(
					"stcLblRegPenaltyFeeAmt");
				ivjstcLblRegPenaltyFeeAmt.setText("100.00");
				ivjstcLblRegPenaltyFeeAmt.setBounds(511, 17, 44, 11);
				ivjstcLblRegPenaltyFeeAmt.setForeground(Color.red);
				ivjstcLblRegPenaltyFeeAmt.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjstcLblRegPenaltyFeeAmt.setVisible(false);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblRegPenaltyFeeAmt;
	}

	/**
	 * Return the stcLblTransaction property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblTransTotal()
	{
		if (ivjstcLblTransTotal == null)
		{
			try
			{
				ivjstcLblTransTotal = new JLabel();
				ivjstcLblTransTotal.setBounds(240, 170, 123, 14);
				ivjstcLblTransTotal.setName("stcLblTransTotal");
				ivjstcLblTransTotal.setText(TXT_TRANS_TOT);
				ivjstcLblTransTotal.setMaximumSize(
					new Dimension(71, 14));
				ivjstcLblTransTotal.setMinimumSize(
					new Dimension(71, 14));
				ivjstcLblTransTotal.setHorizontalAlignment(4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblTransTotal;
	}

	/**
	 * Return the taEnterExpMoYr property value
	 * 
	 * @return txtVendorPltExpMMYYYY
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTextArea gettaEnterExpMoYr()
	{
		if (ivjtaEnterExpMoYr == null)
		{
			try
			{
				ivjtaEnterExpMoYr = new RTSTextArea();
				ivjtaEnterExpMoYr.setName("taEnterExpMoYr");
				ivjtaEnterExpMoYr.setLineWrap(true);
				ivjtaEnterExpMoYr.setWrapStyleWord(true);
				ivjtaEnterExpMoYr.setText(TXT_ENTER_EXP);
				ivjtaEnterExpMoYr.setBackground(
					new Color(204, 204, 204));
				ivjtaEnterExpMoYr.setForeground(java.awt.Color.black);
				ivjtaEnterExpMoYr.setFont(new Font("Arial", 1, 12));
				ivjtaEnterExpMoYr.setBounds(4, 8, 165, 33);
				// user code begin {1}
				ivjtaEnterExpMoYr.setEditable(false);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtaEnterExpMoYr;
	}

	/**
	 * Return the stcLblExpCountdown property value
	 * 
	 * @return txtVendorPltExpMMYYYY
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTextArea gettaExpCountdown()
	{
		if (ivjtaExpCountdown == null)
		{
			try
			{
				ivjtaExpCountdown = new RTSTextArea();
				ivjtaExpCountdown.setBounds(8, 18, 167, 128);
				ivjtaExpCountdown.setName("taExpCountdown");
				ivjtaExpCountdown.setLineWrap(true);
				ivjtaExpCountdown.setWrapStyleWord(true);
				ivjtaExpCountdown.setText(TXT_12MO_MSG);
				ivjtaExpCountdown.setBackground(
					new Color(204, 204, 204));
				ivjtaExpCountdown.setMaximumSize(
					new Dimension(220, 14));
				ivjtaExpCountdown.setForeground(java.awt.Color.black);
				ivjtaExpCountdown.setFont(new Font("Arial", 1, 12));
				ivjtaExpCountdown.setMinimumSize(
					new Dimension(220, 14));
				ivjtaExpCountdown.setEditable(false);
				// user code begin {1}
				ivjtaExpCountdown.setEnabled(true);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtaExpCountdown;
	}

	/**
	 * Return the txtEnterExp property value.
	 * 
	 * @return vendorPltExpMMYYYY
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSDateField gettxtEnterExp()
	{
		if (ivjtxtEnterExp == null)
		{
			try
			{
				ivjtxtEnterExp = new RTSDateField();
				ivjtxtEnterExp.setName("txtEnterExp");
				ivjtxtEnterExp.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtEnterExp.setText("05/2002");
				ivjtxtEnterExp.setBounds(173, 14, 76, 17);
				// user code begin {1}
				ivjtxtEnterExp.setMonthYrOnly(true);
				ivjtxtEnterExp.setFocusTraversalKeys(
					KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
					new HashSet());
				ivjtxtEnterExp.setFocusTraversalKeys(
					KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
					new HashSet());
				ivjtxtEnterExp.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtEnterExp;
	}
	
	/**
	 * This method initializes VPExpMoYr txtVendorPltExpMMYYYY
	 * 
	 * @return txtVendorPltExpMMYYYY
	 */
	private RTSTextArea getVPExpMoYrTextArea() {
		if(txtVendorPltExpMMYYYY == null) {
			txtVendorPltExpMMYYYY = new com.txdot.isd.rts.client.general.ui.RTSTextArea();
			txtVendorPltExpMMYYYY.setBounds(525, 329, 79, 30);
			txtVendorPltExpMMYYYY.setText("New Plate Exp. Mo/Yr:");
			txtVendorPltExpMMYYYY.setEditable(false);
			txtVendorPltExpMMYYYY.setLineWrap(true);
			txtVendorPltExpMMYYYY.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
			txtVendorPltExpMMYYYY.setName("taVPExpMoYr");
			txtVendorPltExpMMYYYY.setWrapStyleWord(true);
			txtVendorPltExpMMYYYY.setBackground(new java.awt.Color(204,204,204));
		}
		return txtVendorPltExpMMYYYY;
	}
	
	/**
	 * This method initializes VPPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getVPPanel() {
		if(VPPanel == null) {
			VPPanel = new javax.swing.JPanel();
			VPPanel.setLayout(null);
			VPPanel.setBorder(
				new javax.swing.border.TitledBorder(
					new javax.swing.border.EtchedBorder(),
					SELECT_VP_RENEW));
			VPPanel.add(getjRB_VP_OneYear(), null);
			VPPanel.add(getjRB_VP_FiveYear(), null);
			VPPanel.add(getjRB_VP_TenYear(), null);
			VPPanel.add(getjRB_VP_NoRenew(), null);
			VPPanel.add(getLblRateOneYr(), null);
			VPPanel.add(getLblRateFiveYr(), null);
			VPPanel.add(getLblRateTenYr(), null);
			VPPanel.setBounds(351, 329, 163, 134);
			RTSButtonGroup laButtonGroup = new RTSButtonGroup();
			laButtonGroup.add(getjRB_VP_NoRenew());
			laButtonGroup.add(getjRB_VP_OneYear());
			laButtonGroup.add(getjRB_VP_FiveYear());
			laButtonGroup.add(getjRB_VP_TenYear());
		}
		return VPPanel;
	}

	/**
	 * Handle Customer Supplied Checkbox
	 *
	 */
	private void handleChkCustSupplied()
	{
		boolean lbSelected = getchkCustSupplied().isSelected();
		getlblPlateNo().setEnabled(lbSelected);
		getCustSuppliedPlateNo().setEnabled(lbSelected);
		getlblPlateAge().setEnabled(lbSelected);
		getCustSuppliedPlateAge().setEnabled(lbSelected);

		// defect 9368 
		getlblInvNotice().setVisible(lbSelected);
		getlblInvNotice().setEnabled(lbSelected);
		// end defect 9368 

		if (lbSelected)
		{
			getCustSuppliedPlateNo().requestFocus();
		}
		else
		{
			getCustSuppliedPlateNo().setText(
				CommonConstant.STR_SPACE_EMPTY);
			getCustSuppliedPlateAge().setText(
				CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * Called whenever the part throws an exception
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
	 * Sets the display message - There reflects N months of 
	 * registration
	 */
	private void handleExpCountDown()
	{
		// defect 9126
		// Add message for Months of Special Plate Fee sold.
		gettaExpCountdown().setText(
			caCompleteTransactionData.getTxtRegMoSold()
				+ " "
				+ caCompleteTransactionData.getTxtSpclPltMoSold());
		// end defect 9126
	}

	/**
	 * Validate the expiration month and year
	 * 
	 * @return boolean true if recal fees is called
	 * @throws RTSException
	 */
	private boolean handleExpDateValidation() throws RTSException
	{
		if (gettxtEnterExp().isValidDate())
		{
			RTSDate ldEnterExpDate = gettxtEnterExp().getDate();
			//ASSIGN FromMonth/Year
			int liFromYr =
				(caCompleteTransactionData.getRegFeesData())
					.getFromYr();
			int liFromMo =
				(caCompleteTransactionData.getRegFeesData())
					.getFromMo();
			int liToYr = ldEnterExpDate.getYear();
			int liToMo = ldEnterExpDate.getMonth();
			//ASSIGN FCalc{0}.NoMoCharge
			caCompleteTransactionData.setNoMoChrg(
				Math.max(
					0,
					(liToYr * MONTHS_IN_YR + liToMo)
						- (liFromYr * MONTHS_IN_YR + liFromMo)
						+ 1));

			// defect 9126
			// Handle setting number of months to charge for Special Plt.
			if (PlateTypeCache
				.isSpclPlate(
					caCompleteTransactionData
						.getVehicleInfo()
						.getRegData()
						.getRegPltCd()))
			{
				// defect 10357
				// For Vendor Plate, use original Vendor Plate
				// For Special Plate, use max of orig exp or today				
				//int liSpclPlateFromYr =
				//	caCompleteTransactionData.getSpclPlateFromYr();
				//int liSpclPlateFromMo =
				//	caCompleteTransactionData.getSpclPlateFromMo();
				int liSpclPlateFromMo = caOrigSpclPltRegisData.getOrigPltExpMo();
				int liSpclPlateFromYr = caOrigSpclPltRegisData.getOrigPltExpYr();
					
				if (!PlateTypeCache.isVendorPlate(
					caCompleteTransactionData
						.getVehicleInfo()
						.getRegData()
						.getRegPltCd()))
				{
					int liExpMonths = caOrigSpclPltRegisData.getOrigPltExpYr() * 12 +
						caOrigSpclPltRegisData.getOrigPltExpMo();
					int liCurrMo = RTSDate.getCurrentDate().getMonth();
					int liCurrYr = RTSDate.getCurrentDate().getYear();
					int liCurrMonths = liCurrYr * 12 + liCurrMo;		
 	
					if (liExpMonths < liCurrMonths)
					{
						liSpclPlateFromMo = liCurrMo;
						liSpclPlateFromYr = liCurrYr;	
					}
				}				
				// end defect 10357
					
				// defect 10523
				// fix calculation for 'Plate Months Sold' in 
				// SR_FUNC_TRANS.					
				caCompleteTransactionData.setSpclPlateNoMoCharge(
					Math.max(
						0,
						(liToYr * MONTHS_IN_YR + liToMo)
							- (liSpclPlateFromYr * MONTHS_IN_YR
								+ liSpclPlateFromMo)));
				//			+ 1));
				// end defect 10523
				
				// defect 10357
				// defect 10523
				// Always go thru Vendor Plate process so that fields
				// are set for SR_FUNC_TRANS
				//if (cbVPRenewElig)
				//{
					adjVendorPltExpDt();
				//}
				// end defect 10523
				// end defect 10357
			}
			// end defect 9126

			// defect 9266
			// Undo 8960. Allow them to enter 12 - 36 months.
			// defect 8960
			// Edit for months cannot be > 23 < 36
			// Use CommonFees lookup for Fixed Expiration Month = 0.
			//int liRegClassCd =
			//	caCompleteTransactionData
			//		.getVehicleInfo()
			//		.getRegData()
			//		.getRegClassCd();
			//int liRTSCurrDate = new RTSDate().getYYYYMMDDDate();
			//CommonFeesData laCommFeesCache =
			//	CommonFeesCache.getCommonFee(
			//		liRegClassCd,
			//		liRTSCurrDate);
			//// defect 8535
			//// check for null CommonFees
			//if (laCommFeesCache != null
			//	&& laCommFeesCache.getFxdExpMo() == 0)
			//{
			//	//Only check this if eligible for 36 months
			//	if (caCompleteTransactionData.getNoMFRecs() == 0
			//		&& laCommFeesCache.getMaxMYrPeriodLngth() > 0
			//		&& caCompleteTransactionData
			//			.getVehicleInfo()
			//			.getVehicleData()
			//			.getVehModlYr()
			//			>= (RTSDate.getCurrentDate().getYear() - 1)
			//		&& caCompleteTransactionData
			//			.getVehicleInfo()
			//			.getTitleData()
			//			.getOwnrShpEvidCd()
			//			== 6)
			//	{
			//
			//		if (caCompleteTransactionData.getNoMoChrg() > 23
			//			&& caCompleteTransactionData.getNoMoChrg() < 36)
			//		{
			//			//throw exception
			//			throw new RTSException(733);
			//		}
			//	}
			//}
			// end defect 8960
			// end defect 9266

			// defect 9126
			// For Special Plates Full integration, we now know the  
			// Special Plate expiration, so do not need this 'free pass' 
			// edit that allowed them to renew for any number of months
			// (ie: allowed them to bump up expiration less than 12 months).
			//if (caCompleteTransactionData
			//	.getTtlInWinInvPrCdNotEqTwoIndi()
			//	== 1)
			// defect 8404
			// This date edit (for 'months of registration must be 
			// 12 - 23 months) does not make sense for RegClassCds 
			// with Reg Period Length = 1
			// (ie: Seasonal Ag where user may buy 1 - 6 months)
			if (caCompleteTransactionData.getRegPeriodLngth() != 1)
			{
				// end 8404
				RegFeesData laRegFeesData =
					caCompleteTransactionData.getRegFeesData();
				int liExpMinMonths =
					laRegFeesData.getToYearMin() * MONTHS_IN_YR
						+ laRegFeesData.getToMonthMin();
				int liNewExpMonths = liToYr * MONTHS_IN_YR + liToMo;
				if (!(liNewExpMonths == liExpMinMonths
					|| caCompleteTransactionData.getNoMoChrg()
						>= MONTHS_IN_YR))
				{
					//throw exception
					throw new RTSException(733);
				}
			}
			//}
			// end defect 9126

			if (ldEnterExpDate.compareTo(cdMinDate) == 1
				&& ldEnterExpDate.compareTo(cdMaxDate) == -1)
			{
				//Edit NewExpMMYYYY for possible fixed exp mo
				int liFxdExpMo =
					caCompleteTransactionData.getFxdExpMo();
				if (liFxdExpMo != 0 && liFxdExpMo != liToMo)
				{
					throw new RTSException(733);
				}
				// defect 10357
				// Also recalc if have used Vendor Plate radiobuttons
				if (!(ldEnterExpDate.getYear()
					== caCompleteTransactionData.getExpYr()
					&& ldEnterExpDate.getMonth()
						== caCompleteTransactionData.getExpMo())
					|| cbRecalcVP)
				{
					//call fees recalculation
					caCompleteTransactionData.setExpYr(
						ldEnterExpDate.getYear());
					caCompleteTransactionData.setExpMo(
						ldEnterExpDate.getMonth());
					caCompleteTransactionData.setReCalcIndi(1);
					getController().processData(
						VCEnterRegistrationREG029.RECAL_FEES,
						caCompleteTransactionData);
					return true;
				}
			}
			else
			{
				throw new RTSException(733);
			}
		}
		else
		{
			throw new RTSException(733);
		}
		return false;
	}

	/**
	 * Handle Special Plate Inventory
	 * 
	 */
	private void handleSpclPltInv()
	{
		SpecialPlatesRegisData laSpclPltRegisData =
			caCompleteTransactionData
				.getVehicleInfo()
				.getSpclPltRegisData();

		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(
				laSpclPltRegisData.getRegPltCd());

		// defect 9434
		// Reset in case return to modify REG029 options  		
		laSpclPltRegisData.setMFGDate(
			caOrigSpclPltRegisData.getMFGDate());

		laSpclPltRegisData.setMFGStatusCd(
			caOrigSpclPltRegisData.getMFGStatusCd());

		laSpclPltRegisData.setMfgSpclPltIndi(
			caOrigSpclPltRegisData.getMfgSpclPltIndi());

		laSpclPltRegisData.setInvItmYr(
			caOrigSpclPltRegisData.getInvItmYr());

		// For EVERY Special Plate scenario where do not 
		// Select Issue from Inventory 
		if (!(getchkIssueInv().isSelected()))
		{
			// defect 9864 
			// Modified in refactor of RegExpYr to PltExpYr  

			// May only know if must Mfg Annual after REG029 
			// defect 9496 
			// Compare against RegExpYr vs. InvItmYr 
			boolean lbNewAnnual =
				laPltTypeData.getAnnualPltIndi() == 1
					&& caOrigSpclPltRegisData.getPltExpYr()
						!= caCompleteTransactionData.getExpYr();
			// end defect 9496 
			// end defect 9864 

			if (laSpclPltRegisData.getMfgSpclPltIndi() == 1
				|| lbNewAnnual
				|| laSpclPltRegisData.isEnterOnSPL002())
			{
				// Owner Supplied Info
				caCompleteTransactionData.setOwnrSuppliedPltNo(
					laSpclPltRegisData.getRegPltNo());

				caCompleteTransactionData
					.getVehicleInfo()
					.getRegData()
					.setOwnrSuppliedExpYr(
						caCompleteTransactionData.getExpYr());

				if (lbNewAnnual)
				{
					laSpclPltRegisData.setInvItmYr(
						caCompleteTransactionData.getExpYr());
				}

				if (laSpclPltRegisData.getMfgSpclPltIndi() == 1
					|| lbNewAnnual)
				{
					// Setup Mfg Request 
					laSpclPltRegisData.setMFGDate(
						getController().getTransCode());

					laSpclPltRegisData.setMFGStatusCd(
						SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD);

					// Present Mfg Msg 	
					RTSException leRTSEx =
						UtilityMethods.createSpclPltMfgInfoMsg(
							laSpclPltRegisData);
					leRTSEx.displayError(this);
				}
			}
		}
		// end defect 9434 
	}

	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(FRM_NAME_REG029);
			setRequestFocus(false);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(620, 596);
			setTitle(FRM_TITLE_REG029);
			setContentPane(getFrmEnterRegistrationREG029ContentPane1());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
		// user code begin {2}
		getlblCreditRemaining().setVisible(false);
		getstcLblCreditRemaining().setVisible(false);
	}

	/**
	 * Initialize Customer Supplied 
	 * 
	 * @param abEnable
	 */
	private void initCustSupplied(boolean abEnable)
	{
		// defect 11030 
		// defect 9529
		// Add check to see if this plate type allows
		// Customer Supplied
		boolean lbSet =
			(caCompleteTransactionData
				.getRegTtlAddlInfoData()
				.getPTOTrnsfrIndi()
				== 1
				|| PlateTypeCache.isCustSuppliedAllowed(
					caCompleteTransactionData
						.getVehicleInfo()
						.getRegData()
						.getRegPltCd()));
		// end defect 9529
		// end defect 11030 

		// defect 9698 
		// Plate Transfer Fee is no longer optional 
		abEnable = abEnable && lbSet;
		// end defect 9698 

		getchkCustSupplied().setVisible(abEnable);
		getlblPlateNo().setVisible(abEnable);
		getCustSuppliedPlateNo().setVisible(abEnable);
		getlblPlateAge().setVisible(abEnable);
		getCustSuppliedPlateAge().setVisible(abEnable);
		getchkCustSupplied().setSelected(abEnable && lbSet);
		getchkCustSupplied().setEnabled(abEnable && !lbSet);
		getlblPlateNo().setEnabled(abEnable && lbSet);
		getCustSuppliedPlateNo().setEnabled(abEnable && lbSet);
		getlblPlateAge().setEnabled(abEnable && lbSet);
		getCustSuppliedPlateAge().setEnabled(abEnable && lbSet);
		getlblInvNotice().setVisible(abEnable && lbSet);
		getlblInvNotice().setEnabled(abEnable && lbSet);
	}
		
	/**
	 * Sets boolean for if Vendor Plate is eligible for Renewal
	 *
	 * @param aiPltExpMo int
	 * @param aiPltExpYr int
	 * @return boolean
	 */
	public boolean isVPRenewEligible(int aiPltExpMo, int aiPltExpYr)
	{
		// If the Vendor Plate expires before the end of registration,  
		// allow user to change the Vendor Plate Term and Renew.							
		boolean lbVPRenewElig = false;
		
		int liRegExpYr = caCompleteTransactionData.getRegFeesData()
			.getToYearMax();
		int liRegExpMo = caCompleteTransactionData.getRegFeesData()
			.getToMonthMax();
			
		int liRegExpMonths = liRegExpYr * 12 + liRegExpMo;
		int liPltExpMonths = aiPltExpYr * 12 + aiPltExpMo;
						
		if (liPltExpMonths <= liRegExpMonths)		
		{
			lbVPRenewElig = true;
		}
		return lbVPRenewElig;
	}

	/**
	 * <ul>
	 * 	<li>Perform button panel navigation using up/down/left/right arrows</li>
	 * 	<li>Set focus to appropriate components</li>
	 * 	<li>Calls fee recalculation when tab off expiration day/month field</li>
	 * </ul>
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);

		if (aaKE.getSource().equals(gettxtEnterExp())
			&& (aaKE.getKeyCode() == KeyEvent.VK_TAB))
		{
			clearAllColor(this);
			try
			{
				if (handleExpDateValidation())
				{
					gettxtEnterExp().requestFocus();
				}
				else if (aaKE.getModifiers() == KeyEvent.SHIFT_MASK)
				{
					getbuttonPanel().getBtnHelp().requestFocus();
				}

				// defect 9366
				// Handle tabbing to Cust Supplied fields or Issue Inv
				else if (
					getchkCustSupplied().isVisible()
						&& getchkCustSupplied().isEnabled())
				{
					getchkCustSupplied().requestFocus();
				}
				else if (
					getCustSuppliedPlateNo().isVisible()
						&& getCustSuppliedPlateNo().isEnabled())
				{
					getCustSuppliedPlateNo().requestFocus();
				}
				else if (
					getchkIssueInv().isVisible()
						&& getchkIssueInv().isEnabled())
				{
					getchkIssueInv().requestFocus();
				}
				// end defect 9366
				
				// defect 10357
				// Handle tabbing to Vendor Plate fields
				else if (
					getjRB_VP_NoRenew().isVisible()
						&& getjRB_VP_NoRenew().isEnabled())				
				{
					getjRB_VP_NoRenew().requestFocus();
				}
				else if (
					getjRB_VP_OneYear().isVisible()
						&& getjRB_VP_OneYear().isEnabled())				
				{
					getjRB_VP_OneYear().requestFocus();
				}
				// end defect 10357

				else
				{
					getbuttonPanel().getBtnEnter().requestFocus();
				}
				// end defect 8620
			}
			catch (RTSException aeRTSException)
			{
				RTSException leEX = new RTSException();
				leEX.addException(aeRTSException, gettxtEnterExp());
				leEX.displayError(this);
				leEX.getFirstComponent().requestFocus();
				return;
			}
		}
	}

	/**
	 * Request focus on components
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		super.keyReleased(aaKE);
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
			FrmEnterRegistrationREG029 aFrmEnterRegistrationREG029;
			aFrmEnterRegistrationREG029 =
				new FrmEnterRegistrationREG029();
			aFrmEnterRegistrationREG029.setModal(true);
			aFrmEnterRegistrationREG029
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			aFrmEnterRegistrationREG029.show();
			Insets insets = aFrmEnterRegistrationREG029.getInsets();
			aFrmEnterRegistrationREG029.setSize(
				aFrmEnterRegistrationREG029.getWidth()
					+ insets.left
					+ insets.right,
				aFrmEnterRegistrationREG029.getHeight()
					+ insets.top
					+ insets.bottom);
			aFrmEnterRegistrationREG029.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 *  Reset Customer Supplied Info 
	 * 
	 */
	private void resetCustomerSuppliedInfo()
	{
		caCompleteTransactionData.setOwnrSuppliedPltNo(
			CommonConstant.STR_SPACE_EMPTY);

		caCompleteTransactionData
			.getVehicleInfo()
			.getRegData()
			.setOwnrSuppliedExpYr(0);

		caCompleteTransactionData
			.getVehicleInfo()
			.getRegData()
			.setRegPltAge(
			ciOrigPlateAge);
	}

	/**
	 * Get CompleteTransactionData and populate screen with data
	 * 
	 * @param aaDataObject CompleteTransactionData
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			clearAllColor(this);

			if (aaDataObject instanceof CompleteTransactionData)
			{
				//populate objects
				caCompleteTransactionData =
					(CompleteTransactionData) UtilityMethods.copy(
						aaDataObject);

				RegFeesData laRegFeesData =
					caCompleteTransactionData.getRegFeesData();

				cdMaxDate =
					new RTSDate(
						laRegFeesData.getToYearMax(),
						laRegFeesData.getToMonthMax(),
						30);
				cdMinDate =
					new RTSDate(
						laRegFeesData.getToYearMin(),
						laRegFeesData.getToMonthMin(),
						1);
				Vector lvFeeData =
					caCompleteTransactionData
						.getRegFeesData()
						.getVectFees();

				// defect 9085 
				// defect 9496 
				// Create copy of Original Special Plate Regis Data 
				if (caCompleteTransactionData
					.getVehicleInfo()
					.isSpclPlt()
					&& caOrigSpclPltRegisData == null)
				{
					caOrigSpclPltRegisData =
						(SpecialPlatesRegisData) UtilityMethods.copy(
							caCompleteTransactionData
								.getVehicleInfo()
								.getSpclPltRegisData());

				}
				// end defect 9496 

				//for regions
				if (SystemProperty.isRegion())
				{
					int liEffDate = new RTSDate().getYYYYMMDDDate();
					for (int i = 0; i < lvFeeData.size(); i++)
					{
						FeesData laFeesData =
							(FeesData) lvFeeData.get(i);
						String lsTmpRegionCd =
							laFeesData.getAcctItmCd()
								+ REGION_ACCT_CD_SUFX;
						AccountCodesData laAccountCodesData =
							AccountCodesCache.getAcctCd(
								lsTmpRegionCd,
								liEffDate);
						if (laAccountCodesData != null)
						{
							laFeesData.setAcctItmCd(
								laAccountCodesData.getAcctItmCd());
							laFeesData.setCrdtAllowedIndi(
								laAccountCodesData.getCrdtAllowdIndi());
							laFeesData.setDesc(
								laAccountCodesData.getAcctItmCdDesc());
						}
					}
				}
				// end defect 9085 
				((TMREG029) getScrollPaneTable().getModel()).add(
					lvFeeData);
				//Populate header
				getlblMinExpMoYr().setText(
					UtilityMethods.addPadding(
						new String[] {
							String.valueOf(
								laRegFeesData.getToMonthMin()),
							CommonConstant.STR_SLASH,
							String.valueOf(
								laRegFeesData.getToYearMin())},
						new int[] { 2, 1, 4 },
						CommonConstant.STR_ZERO));
				getlblMaxExpMoYr().setText(
					UtilityMethods.addPadding(
						new String[] {
							String.valueOf(
								laRegFeesData.getToMonthMax()),
							CommonConstant.STR_SLASH,
							String.valueOf(
								laRegFeesData.getToYearMax())},
						new int[] { 2, 1, 4 },
						CommonConstant.STR_ZERO));

				// defect 9126
				// Compare Min \ Max Mo\Yr
				//if (laRegFeesData.getExpMaxMonths()
				//	== laRegFeesData.getExpMinMonths())
				if (laRegFeesData.getToYearMin()
					== laRegFeesData.getToYearMax()
					&& laRegFeesData.getToMonthMin()
						== laRegFeesData.getToMonthMax())
				{
					gettxtEnterExp().setEnabled(false);
				}
				// end defect 9126
				
				// defect 10596
//				getlblMinFeeTotal().setText(
//					laRegFeesData.getFeeTotalMin().toString());
//				getlblMaxFeeTotal().setText(
//					laRegFeesData.getFeeTotalMax().toString());
				getlblMinFeeTotal().setText(
					laRegFeesData.getFeeTotalMin().printDollar());
				getlblMaxFeeTotal().setText(
					laRegFeesData.getFeeTotalMax().printDollar());
				// end defect 10596
				
				//initialize expiration year
				if (caCompleteTransactionData.getExpYr() == 0)
				{
					caCompleteTransactionData.setExpYr(
						laRegFeesData.getToYearMin());
					caCompleteTransactionData.setExpMo(
						laRegFeesData.getToMonthMin());
				}

				gettxtEnterExp().setDate(
					new RTSDate(
						(caCompleteTransactionData.getRegFeesData())
							.getToYearDflt(),
						(caCompleteTransactionData.getRegFeesData())
							.getToMonthDflt(),
						1));

				caCompleteTransactionData.setExpYr(
					caCompleteTransactionData
						.getRegFeesData()
						.getToYearDflt());

				caCompleteTransactionData.setExpMo(
					caCompleteTransactionData
						.getRegFeesData()
						.getToMonthDflt());
						
				// defect 10357
				// Handle fields for Vendor Plates
				if (PlateTypeCache.isVendorPlate(
						caCompleteTransactionData
						.getVehicleInfo().getRegData().getRegPltCd()))
				{
					if (!cbAlreadySet)
					{
						// defect 10523
						// Use Original VP ExpMoYr, Plate Term
						//ciOrigVPExpMo = caCompleteTransactionData
						//	.getVehicleInfo().getSpclPltRegisData().
						//		getPltExpMo();
						//ciOrigVPExpYr = caCompleteTransactionData
						//	.getVehicleInfo().getSpclPltRegisData().
						//		getPltExpYr();
						//ciOrigPltValidityTerm = caCompleteTransactionData
						//	.getVehicleInfo().getSpclPltRegisData().
						//		getPltValidityTerm();
						ciOrigVPExpMo = caOrigSpclPltRegisData.getOrigPltExpMo();						
						ciOrigVPExpYr = caOrigSpclPltRegisData.getOrigPltExpYr();						
						ciOrigPltValidityTerm = caOrigSpclPltRegisData.getPltValidityTerm();
						// end defect 10523 						
						
						setVPRates();

						// If not eligible for Reg renewal, then also 
						// not eligible for Plate renewal					
						getjRB_VP_NoRenew().setSelected(true);
						if (isVPRenewEligible(ciOrigVPExpMo, 
							ciOrigVPExpYr)
							&& gettxtEnterExp().isEnabled())
						{
							cbVPRenewElig = true;
						}

						getVPPanel().setEnabled(cbVPRenewElig);
						getjRB_VP_NoRenew().setEnabled(cbVPRenewElig);
						getjRB_VP_OneYear().setEnabled(cbVPRenewElig);
						getjRB_VP_FiveYear().setEnabled(cbVPRenewElig);
						getjRB_VP_TenYear().setEnabled(cbVPRenewElig);
						getVPExpMoYrTextArea().setEnabled(cbVPRenewElig);
						getLblVendorPltExpMMYYYY().setEnabled(cbVPRenewElig);
						getLblRateOneYr().setEnabled(cbVPRenewElig);
						getLblRateFiveYr().setEnabled(cbVPRenewElig);
						getLblRateTenYr().setEnabled(cbVPRenewElig);
							
						int liMo = 0;
						int liYr = 0;
						
						// 'No VP Renew' is only available if eligible  
						// for renew and can buy less than 12 months
						// defect 10523
						// Disable until proven it should be enabled
						getjRB_VP_NoRenew().setEnabled(false);
						// end defect 10523
													
						RTSDate ldEnterExpDate = 
							gettxtEnterExp().getDate();
						int liToYr = ldEnterExpDate.getYear();
						int liToMo = ldEnterExpDate.getMonth();
						int liRegMonths = liToYr * 12 + liToMo;
						int liPltMonths = 
							ciOrigVPExpYr * 12 + ciOrigVPExpMo;
			
						int liVPMonthsToCharge = 
							liRegMonths - liPltMonths;
							
						if (cbVPRenewElig && liVPMonthsToCharge < 12)
						{
							getjRB_VP_NoRenew().setEnabled(true);
						}
						
						if (liVPMonthsToCharge > 0)
						{	
							liMo = liToMo;
							liYr = liToYr;
						}
						else
						{
							liMo = ciOrigVPExpMo;
							liYr = ciOrigVPExpYr;
						}
							
						if (cbVPRenewElig 
							&& gettxtEnterExp().isEnabled())
						{

							// If Renewal and Reg\Plt expiration synched,
							// or
							// Plt must be renewed (12+ months required)
							// default to renew plate at current plate term
							cbSynchedRenew = false;
							if (getController().getTransCode().equals(
								TransCdConstant.RENEW)
								&& ciOrigVPExpYr == caCompleteTransactionData
								.getVehicleInfo().getRegData().getRegExpYr()
								&& ciOrigVPExpMo == caCompleteTransactionData
								.getVehicleInfo().getRegData().getRegExpMo())
							{
								cbSynchedRenew = true;
							}
							// defect 10523
							// If synched renew and Plt term > 1, renew 
							// plate at that plt term. Otherwise, if
							// months to charge > 12, set to One Year Renew
							//if (cbVPRenewElig
							//	&& (cbSynchedRenew
							//	|| liVPMonthsToCharge >= 12))
							//{
							//	// Will renew at current plate term
							//	if (ciOrigPltValidityTerm > 1)
							//	{
							//	liYr = 
							//		ciOrigVPExpYr + ciOrigPltValidityTerm;
							//	} 						
							//	getjRB_VP_OneYear().setSelected(
							//		ciOrigPltValidityTerm == 1);
							//	getjRB_VP_FiveYear().setSelected(
							//		ciOrigPltValidityTerm == 5);
							//	getjRB_VP_TenYear().setSelected(
							//		ciOrigPltValidityTerm == 10);

							if (cbVPRenewElig)
							{
								if (cbSynchedRenew && 
									ciOrigPltValidityTerm > 1)
								{
									liYr = ciOrigVPExpYr + 
										ciOrigPltValidityTerm;
									getjRB_VP_OneYear().setSelected(
										ciOrigPltValidityTerm == 1);
									getjRB_VP_FiveYear().setSelected(
										ciOrigPltValidityTerm == 5);
									getjRB_VP_TenYear().setSelected(
										ciOrigPltValidityTerm == 10);
								}
								else if (liVPMonthsToCharge >= 12)
								{
									getjRB_VP_OneYear().setSelected(true);
									caCompleteTransactionData
										.getVehicleInfo()
										.getSpclPltRegisData()
										.setPltValidityTerm(1);
								}
							}
							// end defect 10523   
						}
						
						// Set VP expiration 
						getLblVendorPltExpMMYYYY().setText(
							UtilityMethods.addPadding(
								new String[] {
									String.valueOf(liMo),
									CommonConstant.STR_SLASH,
									String.valueOf(liYr)},
								new int[] { 2, 1, 4 },
								CommonConstant.STR_ZERO));
					}
				}
				else
				{
					// Not a Vendor Plate, hide all fields
					getVPPanel().setVisible(false);
					getjRB_VP_NoRenew().setVisible(false);
					getjRB_VP_OneYear().setVisible(false);
					getjRB_VP_FiveYear().setVisible(false);
					getjRB_VP_TenYear().setVisible(false);
					getVPExpMoYrTextArea().setVisible(false);
					getLblVendorPltExpMMYYYY().setVisible(false);
					getLblRateOneYr().setVisible(false);
					getLblRateFiveYr().setVisible(false);
					getLblRateTenYr().setVisible(false);
					getVPPanel().setEnabled(false);
					getjRB_VP_NoRenew().setEnabled(false);
					getjRB_VP_OneYear().setEnabled(false);
					getjRB_VP_FiveYear().setEnabled(false);
					getjRB_VP_TenYear().setEnabled(false);
					getVPExpMoYrTextArea().setEnabled(false);
					getLblVendorPltExpMMYYYY().setEnabled(false);
					getLblRateOneYr().setEnabled(false);
					getLblRateFiveYr().setEnabled(false);
					getLblRateTenYr().setEnabled(false);
				}
				// end defect 10357

				// defect 9015 
				// if Penalty && PenaltyFee != null && PenaltyFee > 0.00
				if (caCompleteTransactionData.getRegPnltyChrgIndi()
					== 1
					&& caCompleteTransactionData.getRegisPenaltyFee()
						!= null
					&& caCompleteTransactionData
						.getRegisPenaltyFee()
						.compareTo(
						new Dollar(0.00))
						== 1)
				{
					getstcLblRegPenaltyFeeAmt().setText(
						caCompleteTransactionData
							.getRegisPenaltyFee()
							.toString());
					getstcLblRegPenaltyFeeAmt().setVisible(true);
					getstcLblRegPenaltyFee().setVisible(true);
				}
				else
				{
					getstcLblRegPenaltyFee().setVisible(false);
					getstcLblRegPenaltyFeeAmt().setVisible(false);
				}
				// end defect 9015  

				handleExpCountDown();

				//Fees recal message
				if (caCompleteTransactionData.getReCalcIndi() == 1)
				{
					caCompleteTransactionData.setReCalcIndi(0);
					cbRecalScreen = true;
					getJPanel5().setVisible(true);
					gettxtEnterExp().requestFocus();
				}
				else
				{
					getJPanel5().setVisible(false);
				}
				//Compute Transaction Total and Credit Remaining
				Dollar ldZeroDollar =
					new Dollar(CommonConstant.STR_ZERO_DOLLAR);

				if (caCompleteTransactionData.getCrdtRemaining()
					!= null
					&& (
						caCompleteTransactionData
							.getCrdtRemaining())
							.compareTo(
						ldZeroDollar)
						!= 0)
				{
					getlblCreditRemaining().setText(
						caCompleteTransactionData
							.getCrdtRemaining()
							.printDollar());
					getlblCreditRemaining().setVisible(true);
					getstcLblCreditRemaining().setVisible(true);
				}
				else
				{
					getlblCreditRemaining().setVisible(false);
					getstcLblCreditRemaining().setVisible(false);
				}

				Dollar ldTransTotal =
					new Dollar(CommonConstant.STR_ZERO_DOLLAR);

				for (int i = 0; i < lvFeeData.size(); i++)
				{
					FeesData laFeesData = (FeesData) lvFeeData.get(i);
					ldTransTotal =
						ldTransTotal.add(laFeesData.getItemPrice());
				}

				getlblTransTotal().setText(ldTransTotal.printDollar());

				// 9366 / 9368 
				// Only evaluate once 
				if (!cbAlreadySet)
				{
					// Add 'Issue from Inventory' checkbox for Special Plate.
					String lsRegPltCd =
						caCompleteTransactionData
							.getVehicleInfo()
							.getRegData()
							.getRegPltCd();

					PlateTypeData laPltTypeData =
						PlateTypeCache.getPlateType(lsRegPltCd);

					// defect 9540
					// Check for null. 'TOWP' has no row RTS_PLT_TYPE.
					// boolean lbSpclPlt =
					// !laPltTypeData.getPltOwnrshpCd().equals(
					// 		SpecialPlatesConstant.VEHICLE);

					// boolean lbOwner =
					// 		laPltTypeData.getNeedsProgramCd().equals(
					//			 SpecialPlatesConstant.OWNER);
					boolean lbSpclPlt = false;
					boolean lbOwner = false;
					if (laPltTypeData != null)
					{
						lbSpclPlt =
							!laPltTypeData.getPltOwnrshpCd().equals(
								SpecialPlatesConstant.VEHICLE);

						lbOwner =
							laPltTypeData.getNeedsProgramCd().equals(
								SpecialPlatesConstant.OWNER);
					}
					// end defect 9540					

					SpecialPlatesRegisData laSpclPltRegisData =
						caCompleteTransactionData
							.getVehicleInfo()
							.getSpclPltRegisData();

					if (ciOrigPlateAge == -1)
					{
						ciOrigPlateAge =
							caCompleteTransactionData
								.getVehicleInfo()
								.getRegData()
								.getRegPltAge(
								true);
					}

					// defect 9864 
					// Modified in refactor of RegExpYr to PltExpYr  

					// Do not present if have already determined that must 
					// remanufacture plate. 
					boolean lbMfgSpclPlt =
						lbSpclPlt
							&& ((laSpclPltRegisData.getMfgSpclPltIndi()
								== 1)
								|| (laPltTypeData.getAnnualPltIndi() == 1
									&& laSpclPltRegisData.getPltExpYr()
										!= caCompleteTransactionData
											.getRegFeesData()
											.getToYearDflt()));
					// end defect 9864 

					if (lbSpclPlt
						&& !lbOwner
						&& !lbMfgSpclPlt
						&& laSpclPltRegisData.isEnterOnSPL002())
					{
						// Always deselected
						// Enable if the prior event for the special plate
						//  record was not a special plate event or it was  
						//  either SPAPPL || SPAPPR (imply manufacture)  
						String lsOrigTransCd =
							laSpclPltRegisData.getOrigTransCd();

						// defect 9232 
						boolean lbVisible =
							lsOrigTransCd == null
								|| (!(UtilityMethods
									.isSpecialPlates(lsOrigTransCd))
									|| lsOrigTransCd.equals(
										TransCdConstant.SPAPPL)
									|| lsOrigTransCd.equals(
										TransCdConstant.SPAPPR));
						// end defect 9232 

						getchkIssueInv().setVisible(lbVisible);
						getchkIssueInv().setEnabled(lbVisible);
						getchkIssueInv().setSelected(false);
						initCustSupplied(false);
					}
					else
					{
						// 'Issue Inventory' and 'Customer Supplied' are
						// mutually exclusive.
						getchkIssueInv().setVisible(false);
						getchkIssueInv().setEnabled(false);

						String lsTransCd =
							getController().getTransCode();

						MFVehicleData laMFVehData =
							caCompleteTransactionData.getVehicleInfo();

						// Show Customer Supplied if 
						//   PTO Eligible &&  
						//   ( Title && MustChangePltIndi == 1
						//      || Reg && NewPlatesDesrdIndi = 1) 
						// defect 9529
						// Add check to see if this plate type allows
						// Customer Supplied (ie: if PTO Elible OR
						// is Cust Supplied Allowed AND...)
						
						// defect 10951 
						boolean lbShowCustSupplied =
							(laMFVehData.isPTOEligible()
								|| PlateTypeCache.isCustSuppliedAllowed(
									lsRegPltCd))
								&& ((UtilityMethods
									.getEventType(lsTransCd)
									.equals(
										TransCdConstant.TTL_EVENT_TYPE)
//									&& (laMFVehData
//										.getTitleData()
//										.getMustChangePltIndi()
//										== 1))
									|| (UtilityMethods
										.getEventType(lsTransCd)
										.equals(
											TransCdConstant
												.REG_EVENT_TYPE))))
										&& caCompleteTransactionData
											.getRegTtlAddlInfoData()
											.getNewPltDesrdIndi()
											== 1;
						// end defect 9529
						// end defect 10951 

						initCustSupplied(lbShowCustSupplied);
					}

					// Set message for Previous Reg Expdt, Plate Expdt
					if (lbSpclPlt)
					{
						// defect 9689
						// Set verbiage for Special Plate or Vendor Plate
						String lsSpclPltType = " Special Plate";
						// defect 10357
						// Vendor Plate may have different reg\plt exp
						int liPltExpMo = 0;
						int liPltExpYr = 0;
						if (PlateTypeCache.isVendorPlate(lsRegPltCd))
						{
							String lsPltTerm = String.valueOf(
								caOrigSpclPltRegisData.
								getPltValidityTerm());
							lsSpclPltType = " Vendor Plate (" +
							lsPltTerm + " Year)";
							
							liPltExpMo = ciOrigVPExpMo;
							liPltExpYr = ciOrigVPExpYr;
						}
						else
						{
							// defect 10357
							// This should be using the Special Plate
							// info in CompleteTransactionData
							// (ie: we may have exchanged to a Spcl Plt
							//liPltExpMo = caCompleteTransactionData
							//	.getOrgVehicleInfo().getRegData()
							//	.getRegExpMo();
							//liPltExpYr = caCompleteTransactionData
							//	.getOrgVehicleInfo().getRegData()
							//	.getRegExpYr();
							// end defect 10357
							liPltExpMo = caCompleteTransactionData
								.getVehicleInfo().getSpclPltRegisData().
								getPltExpMo();
							liPltExpYr = caCompleteTransactionData
								.getVehicleInfo().getSpclPltRegisData().
								getPltExpYr();
						}
						// end defect 9689
						String lsPrevRegExp = "";
						String lsPrevPltExp = "";
						if (caCompleteTransactionData
							.getOrgVehicleInfo()
							.getRegData()
							.getRegExpMo()
							== 0)
						{
							lsPrevRegExp =
								"   N/A          Registration\n";
						}
						else
						{
							lsPrevRegExp =
								"   "
									+ UtilityMethods.addPadding(
										Integer.toString(
											caCompleteTransactionData
												.getOrgVehicleInfo()
												.getRegData()
												.getRegExpMo()),
										2,
										"0")
									+ "/"
									+ caCompleteTransactionData
										.getOrgVehicleInfo()
										.getRegData()
										.getRegExpYr()
									+ " Registration\n";
						}
						// defect 9864 
						// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
						lsPrevPltExp =
							"   "
								+ UtilityMethods.addPadding(
									Integer.toString(
						//				laSpclPltRegisData
						//					.getPltExpMo()),
						//				caCompleteTransactionData
						//					.getOrgVehicleInfo()
						//					.getSpclPltRegisData()
						//					.getOrigPltExpMo()),
									liPltExpMo),
									2,
									"0")
								+ "/"
						//		+ laSpclPltRegisData.getPltExpYr()
						//		+ caCompleteTransactionData
						//			.getOrgVehicleInfo()
						//			.getSpclPltRegisData()
						//			.getOrigPltExpYr()
								+ liPltExpYr
								+ lsSpclPltType;
						// end defect 10357
						// end defect 9864 

						jPrevExpMsg.setText(
							"Previous Expirations:\n"
								+ lsPrevRegExp
								+ lsPrevPltExp);
						getPrevExpMsg().setVisible(true);
					}
					else
					{
						jPrevExpMsg.setText("");
						getPrevExpMsg().setVisible(false);
					}
					cbAlreadySet = true;
				}
			}
			// end defect 9366 / 9368

			else if (aaDataObject instanceof Vector)
			{
				Vector lvData = (Vector) aaDataObject;
				getController().processData(
					VCEnterRegistrationREG029.REDIRECT,
					lvData);
			}
		}
		catch (Exception aeEX)
		{
			// TODO shouldn't this display a new rtsex?
			aeEX.getMessage();
		}
	}

	/**
	 * Set Vendor Plate One\Five\Ten year rates for this VP
	 */
	private void setVPRates()
	{
		Vector lvFees = new Vector();
		Dollar laVPRate = null;
		// Return all rows (all Validity Terms) for a RegPltCd
		lvFees =
			PlateSurchargeCache.getPltSurcharge(
				caCompleteTransactionData
					.getVehicleInfo()
					.getSpclPltRegisData()
					.getRegPltCd(),
				caCompleteTransactionData
					.getVehicleInfo()
					.getSpclPltRegisData()
					.getOrgNo(),
				caCompleteTransactionData
					.getVehicleInfo()
					.getSpclPltRegisData()
					.getAddlSetIndi(),
					0,
					new RTSDate().getYYYYMMDDDate());
					
		for (int i = 0; i < lvFees.size(); i++)
		{
			PlateSurchargeData laData =
				(PlateSurchargeData) lvFees.elementAt(i);
				laVPRate = laData.getPltSurchargeFee();
			if (laData.getPltValidityTerm() == 1)
			{
				getLblRateOneYr().setText(laVPRate.toString());
			}
			else if (laData.getPltValidityTerm() == 5)
			{
				getLblRateFiveYr().setText(laVPRate.toString());
			}
			else
			{
				getLblRateTenYr().setText(laVPRate.toString());
			}
		}
	}

	/**
	 * Validate Customer Supplied Plate Age Field
	 * 
	 * @param RTSException
	 */
	private void validateCustSuppliedPlateAge(RTSException aeRTSEx)
	{
		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(
				caCompleteTransactionData
					.getVehicleInfo()
					.getRegData()
					.getRegPltCd());

		int liMandReplPltAge = laPltTypeData.getMandPltReplAge();
		String lsPlateAge = getCustSuppliedPlateAge().getText();

		if (lsPlateAge.length() == 0
			|| Integer.parseInt(lsPlateAge) > liMandReplPltAge)
		{
			aeRTSEx.addException(
				new RTSException(150),
				getCustSuppliedPlateAge());
		}
	}

	/**
	 * Validate Customer Supplied Plate Number Field
	 * 
	 * @param RTSException
	 */
	private void validateCustSuppliedPlateNo(RTSException aeRTSEx)
	{
		String lsRegPltCd =
			caCompleteTransactionData
				.getVehicleInfo()
				.getRegData()
				.getRegPltCd();
		String lsRegPltNo = getCustSuppliedPlateNo().getText().trim();

		if (lsRegPltNo.length() == 0)
		{
			aeRTSEx.addException(
				new RTSException(150),
				getCustSuppliedPlateNo());
		}
		else
		{
			ValidateInventoryPattern laValidateInventoryPattern =
				new ValidateInventoryPattern();

			ProcessInventoryData laProcessInventoryData =
				new ProcessInventoryData();
			laProcessInventoryData.setItmCd(lsRegPltCd);
			laProcessInventoryData.setInvQty(1);
			laProcessInventoryData.setInvItmNo(lsRegPltNo);
			int liYear = 0;
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(lsRegPltCd);
			if (laPltTypeData.getAnnualPltIndi() == 1)
			{
				liYear = gettxtEnterExp().getDate().getYear();
			}
			laProcessInventoryData.setInvItmYr(liYear);

			try
			{
				laValidateInventoryPattern.validateItmNoInput(
					laProcessInventoryData.convertToInvAlloctnUIData(
						laProcessInventoryData));
			}
			catch (RTSException aeRTSEx1)
			{
				aeRTSEx.addException(
					aeRTSEx1,
					getCustSuppliedPlateNo());
			}
		}
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"