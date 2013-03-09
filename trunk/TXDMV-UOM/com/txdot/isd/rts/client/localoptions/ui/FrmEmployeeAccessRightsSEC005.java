package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextUI.BasicHighlighter;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.SecurityClientDataObject;
import com.txdot.isd.rts.services.data.SecurityData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.localoptions.JniAdInterface;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * FrmEmployeeAccessRightsSEC005.java 
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/20/2002	Changed white back to gray 
 *							CQU100004011
 * S Govindappa 05/22/2002  Moved the code for Employee ID 
 *							Validation from FocusLost() 
 *							to FocusGained()
 *							CQU100004059
 * MAbs		  	06/3/2002	Returned focus to the button that was  
 *							pressed when changing security 
 *							CQU100004208
 * MAbs		  	08/26/2002	PCR 41
 * MAbs		  	09/17/2002	PCR 41 Integration
 * MWang		01/29/2003	Modified reinitialize() to reset 
 *							checkbox password.
 * Min Wang     05/28/2003	Fixed Defect 6125. Modified focusLost() 
 *							to fix Null pointer when empId is empty. 
 * Ray Rowehl	08/26/2003	Add handling for new Active Directory 
 *							UserId.
 *							Also some VCE changes due to moving frame 
 *							objects around.  Also updated comments in 
 *							code.
 *							add getchkUsePrefix(), getstclblAdUserId(),
 *								gettxtEmpIdUserIdPrefix()
 *							modify 
 *						 	getFrmEmployeeAccessRightsSEC005ContentPane1(),
 *								isValidEmpId(), setData(), 
 *								focusGained(),
 *								setEmpTextFieldsToDataObj()
 *							defect 6445 Ver  5.1.6
 * Ray Rowehl	12/01/2003	Rename AdUserId to SysUserId
 *							defect 6445 Ver  5.1.6
 * Ray Rowehl	12/18/2003	Rename SysUserId to UserName.
 *							modify clearCheckBoxs()
 *							defect 6445  Ver  5.1.6
 * Ray Rowehl	02/03/2004	Remove ClearAllColor from focus gained.
 *							Allow edit on first name even though last
 *							name edit failed.
 *							Disable edit when setting up 
 *							txtUserNameBaseId initially.
 *							Set focus on UserName if check box is 
 *							selected otherwise put the focus back on 
 *							EmpId.  Do formulate the user name unless  
 *							both the last name and first name are 
 *							populated.  Trim the UserName String before 
 *							putting it to the frame.
 *							modify focusGained(), focusLost(), 
 *							actionPerformed(), 
 *							setData(), setupUserName()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/05/2004	Change error messages
 *							modify	actionPerformed(),
 *									focusGained(),
 *									handleException()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/09/2004	When UserName is enabled and EmpId has
 *							a length error, paint error on UserName
 *							as well.  clearColor if focus is gained
 *							on EmpId or UserName.  Also handle focus
 *							issues for EmpId and UserName.
 *							Removed requestFocus from ex.displayMsg.
 *							modify	actionPerformed(),
 *									focusGained(),
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/12/2004	Needed to set JNI boolean in when
 *							copying AD affected data.
 *							modify updtOrigDataObj()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/18/2004	change for updated isRtsUserName
 * 							reflow java comments
 *							Update to set focus through key press
 *							on chkUsePrefix or chkSearchUserName.
 *							Clear resetPassword check box before
 *							leaving actionPerformed.
 *							modify actionPerformed(), clearCheckBoxs(),
 *							focusLost(), getchkSearchUserName(),
 *							getchkUsePrefix(), keyPressed(), setData() 
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/19/2004	only remove color on empid if focus was not
 *							there before.
 *							modify focusGained()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/20/2004	Add startWorking to prevent multiple look
 *							requests
 *							modify focusGained(), focusLost()
 *									gettxtUserNameBaseId()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/26/2004	Avoid double cursors.
 *							Trim UserNameBaseId before putting to frame.
 *							Make setupUserName private.
 *							Do not replace an Empty UserName if we are
 *							not in add mode.
 *							Do not show password reset message until 
 *							it is reset actually.
 *							modify actionPerformed(), focusLost(), 
 *									setData(), setupUserName()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	03/04/2004	Set the Revise flag is Security Access has
 *							changed.
 *							Change how UserName is setup.
 *							modify setupUserName(), updtOrigDataObj()
 *							defect 6445 Ver 5.1.6
 * K Harrell	03/04/2004	Remove the setting of the Revise flag if
 *							Local Options button selected.
 *							modify actionPerformed()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	03/08/2004	Recompute UserName in counties if last name
 *							or first initial changes and we are doing
 *							add.  Recompute also works when moving
 *							between screens.
 *							Disable edit on Employee Id if UserName is
 *							not populated.
 *							Work on focus issues when Popping AD
 *							messages.
 *							Look at avoiding the cancel - focus problem
 *							In Developer / Test mode, set the prefix 
 *							based on whether testing county or not.
 *							Add a new method to make parsing out the
 *							prefix easier for handling the string.
 *							add keyReleased(), parseOutPrefix() 
 *							modify actionPerformed(), clearCheckBoxs(), 
 *								setData(), setEmpTxtFieldsToDataObj(), 
 *								setupUserName()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	03/10/2004	Prevent prefix from coming into 
 *							UserNameBaseId.
 *							Also make setting of Editable consistent.
 *							Make Confirmation on user name change
 *							consistent.
 *							modify parseOutPrefix(), 
 *							reinitializeWindow(), setData(),
 *							setEmpTextFieldsToDataObj()
 *							defect 6926 Ver 5.1.6
 * Min Wang		03/23/2004	Fixed Alt+N key cursor focus stays on "Enable
 *							User Name for Search" unless you hold down 
 *     						Alt+N. User cannot depress fast.
 *							modify actionPerformed(), keyReleased()
 * 							defect 6947 Ver 5.1.6
 * Ray Rowehl	03/26/2004	Work on focus issues when just using
 *							letter key to select button.
 *							Kept super.keyPressed in keyPressed.
 *							But do not call super if on a check box.
 *							modify actionPerformed(), keyReleased()
 *							defect 6956 Ver 5.1.6
 * Ray Rowehl	03/31/2004	Disable text fields if we are updating
 *							an employee.  These fields can only change
 *							through delete and add.
 *							We now allow search directly from UserName.
 *							add enableTextFields()
 *							modify cEmpIdFocusGained, doInitAdEmp(), 
 *							focusGained(), setData()
 *							defect 6990 Ver 5.1.6
 * Ray Rowehl	04/15/2004	If the County Administrator types in a 
 *							UserName keep it but also track a boolean so
 *							we do not attempt to add bogus UserName to
 *							Active Directory.
 *							modify doInitAddEmp(), focusGained(),
 *							setupUserName()
 *							defect 7007 Ver 5.1.6
 * Min Wang 	04/22/2004	Fixed tabbing will get the cursor to the 
 *							enable search but it cannot go anywhere else. 
 *							shift tabbing will get you to last name and 
 *                          then no where else Cannot get to the 
 *       					employee ID no matter what you do when try 
 *							deleting an employee with inventory.
 *							modify actionPerformed() 
 *							defect 7005 Ver 5.1.6
 * K Harrell	04/02/2004	Remove reference to isWindowsPlatform(),
 *							any references to password, etc.
 *							modify focusgained(),getchkUsePrefix(),
 *							getlblUserNamePrefix(),getstcLblAdUserId(),
 *							gettxtUserNameBaseId(),isValidEmpId(),
 *							reinitializeWindow(),setData(),
 *							doAddNewEmp,updtOrigDataObj()
 *							remove PASSWORD,EMPID_LENGTH 
 *							defect 6955 Ver 5.2.0
 * K Harrell	06/02/2004	Error in implementation of 6995
 *							remove disabling of UserName label
 *							modify getstclblAdUserId()
 *							defect 7139 Ver 5.2.0
 * K Harrell	06/14/2004	Need to set setReprntStkrRptAccs
 * 							in caSecDataUpdtOrig
 *							modify updtOrigDataObj()
 *							defect 7160 Ver 5.2.0
 * Min Wang		07/20/2004	Need to setRSPSUpdtAccs in caSecDataUpdtOrig
 *							modify updtOrigDataObj()
 *							defect 7310 Ver 5.2.1
 * Ray Rowehl	11/15/2004	Allow VTR Users to have 8 positions in their
 *							UserName (non-prefix).
 *							Also a little formatting to methods touched.
 *							modify actionPerformed(), parseOutPrefix(),
 *									setData()
 *							defect 7604 Ver 5.2.2
 * Ray Rowehl	11/17/2004	Discovered a flaw in username setup.
 *							Setting the length of the field resets the
 *							text.
 *							modify doInitAddEmp(), setData()
 *							defect 7604 Ver 5.2.2
 * Ray Rowehl	11/23/2004	Disable the delete button if the employee
 *							being revised is self.
 *							delete reinitializeWindow() (deprecated)
 *							modify setData()
 *							defect 7749 Ver 5.2.2
 * Ray Rowehl	11/29/2004	Cleanup a few documentation issues
 *							modify doInitAddEmp()
 *							defect 7604 Ver 5.2.2
 * Ray Rowehl	11/30/2004	Cleanup the line length in setData.  It was
 *							past 72 on several lines.
 *							modify setData()
 *							defect 7604 Ver 5.2.2
 * Min Wang		02/16/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3
 * Min Wang		03/30/2005	Remove setNextFocusable's
 * 							defect 7891 Ver 5.2.3
 * Min Wang		04/08/2005	When entering an empid, tabbing off did not
 * 							start the search.  The problem is that we
 * 							are now tabbing to the UserName Search 
 * 							chkBox.  Will now initiate search when 
 * 							tabbing on to the chk box.
 * 							modify focusGained(), getchkSearchUserName()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		04/16/2005	Further cleanup.  rename args.
 * 							modify main()
 * 							defect 7891 Ver 5.2.3
 * Ray Rowehl	06/16/2005	A little clean up on focus when tabbbing 
 * 							off of EmployeeId.
 * 							modify focusGained()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3      
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Create and move constants to approriate 
 * 							constants classes.
 * 							defect 7891 Ver 5.2.3
 * Min Wang		10/27/2005  Uncomment code for arrow key function.
 * 							modify keyPressed()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		11/08/2005	Made string the right length.
 * 							modify setupUserName()
 * 							defect 7891 Ver 5.2.3.
 * Min Wang		01/03/2006	Fix no focus on screen SEC005 for Revise 
 * 							after Select "No" on CTL001 - CONFIRM ACTION.
 * 							modify actionPerformed() 
 * 							defect 7891 Ver 5.2.3
 * K Harrell	01/10/2006	Replaced  "System.getProperty(
 *							CommonConstant.SYSTEM_LINE_SEPARATOR)" with
 *							CommonConstant.SYSTEM_LINE_SEPARATOR
 *							modify setEmpTextFieldsToDataObj() 
 *							defect 7891 Ver 5.2.3  
 * Jeff S.		01/23/2006	Fixed problem where focus was on a disabled
 * 							field when you choose no on the delete.
 *							modify actionPerformed()
 *							defect 7891 Ver 5.2.3
 * Min Wang		01/26/2006  Fixed problem that entry fields User Name 
 * 							and Employee Id are not 
 * 							changed to red when tabbing off 
 * 							"Enable User Name for Search" checkbox.
 * 							modify focusGained()
 *   						defect 7891 Ver 5.2.3
 * Min Wang 	04/05/2006	Allow to enter a space between characters in 
 * 							Employee Id and User Name input fields.
 * 							modify gettxtEmployeeId(), 
 * 							gettxtUserNameBaseId()
 * 							defect 8674 Ver 5.2.3
 * Min Wang		10/02/2006  Allow to enter a ' ', and '-' between 
 * 							characters in User Name, Employee Id, First
 * 							Name and Last Name.
 * 							modify gettxtEmployeeId(), 
 * 							gettxtFirstName(), gettxtLastName,
 * 							gettxtUserNameBaseId()
 * 							defect 8938 Ver FallAdminTables 							
 * M Reyes	    10/11/2006  Changes for Exempts
 * 							modify updtOrigDataObj()
 * 							defect 8900 Ver Exempts
 * M Reyes		02/12/2007	Changes for Special plates
 * 							modify disableBtnsForRegMainOffc()
 * 							defect 9124 Ver Special Plates
 * M Reyes		02/15/2007	Changes for Special Plates
 * 							Used visual editor to add buttons and labels
 * 							add getbtnSpecialPlates(), getchkSplPlts() 
 * 							defect 9124 Ver Special Plates
 * K Harrell	02/16/2007	Removed try/catch 
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * M Reyes		03/08/2007	Code Clean-up
 * 							Removed commented out block of code
 * 							at bottom of class
 * 							defect 9124 Ver Special Plates
 * K Harrell	04/20/2007	Implemented SystemProperty.isCounty(), 
 * 							 isRegion() 
 * 							modify setData(), disableBtnsForRegMainOffc(),
 * 							 setupUserName() 
 * 							defect 9085 Ver Special Plates 
 * M Reyes		06/04/2007	Changes for Special Plates
 * 							modify keyPressed()
 * 							defect 9124 Ver Special Plates
 * K Harrell	08/05/2007	Mnemonic change from 'a' to 'e' for 
 * 							Special Plates button
 * 							modify getbtnSpecialPlates()
 * 							defect 9188 Ver Special Plates
 * K Harrell	05/07/2008	Enable Funds for HQ (Trans Recon Rpt Only)
 * 							delete disableBtnsForRegMainOffc() 
 * 							modify setData()
 * 							defect 9653 Ver 3 Amigos PH B  
 * Min Wang		04/26/2008	Widen last-first name field on SEC005 screen.
 * 							modify getlblUserNamePrefix(),
 * 							getstclblAdUserId(), getstcLblEmployeeId(),
 * 							getstcLblFirstName(), getstcLblLastName(),
 * 							gettxtFirstName(), gettxtLastName(),
 * 							gettxtMiddleInit()
 * 							defect 8724 Ver Defect_POS_A
 * Min Wang		04/28/2008	Fix no message appearing when user name has
 * 							been changed the first time. 
 * 							modify setEmpTextFieldsToDataObj()
 * 							defect 7058 Ver Defect_POS_A
 * Min Wang		05/08/2008	Should not validate Empid when hit 
 * 							"Escape" key on keyboard.
 * 							modify clearCheckBoxs()
 * 							defect 8970 Ver Defect_POS_A
 * K Harrell	09/10/2008	Implement new Security columns for Local
 * 							Options Reporting 
 * 							modify updtOrigDataObj() 
 * 							defect 9710 Ver Defect_POS_B
 * K Harrell	10/27/2008	Update for Disabled Placards 
 * 							modify updtOrigDataObj()
 * 							defect 9831 Ver Defect_POS_B 
 * B Hargrove	02/24/2009	Update for EttlRptAccs 
 * 							modify updtOrigDataObj()
 * 							defect 9960 Ver Defect_POS_E 
 * Min Wang		08/07/2009	Update for PrivateLawEnfVehAccs.
 * 							modify updtOrigDataObj()
 * 							defect 10153 Ver Defect_POS_F
 * Min Wang		01/18/2011	Update for 	WebAgntAccs.
 * 							modify updtOrigDataObj()
 * 							defect 10717 Ver POS_670
 * K Harrell	01/20/2011	Update for BatchRptMgmtAccs 
 * 							modify updtOrigDataObj()	
 * 							defect 10701 Ver 6.7.0 
 * K Harrell	05/28/2011	Update for ModfyTimedPrmtAccs 
 * 							modify updtOrigDataObj()	
 * 							defect 10844 Ver 6.8.0 
 * B Woodson    01/11/2012	modify updtOrigDataObj()
 * 							defect 11228 Ver 6.10.0
 * K Harrell	01/11/2012	Update for DsabldPlcrdReinstateAccs 
 * 							modify updtOrigDataObj()	
 * 							defect 11214 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class is used for managing employee access rights.  
 * This screen uses SecurityClientDataObject object. 
 *
 * @version	6.10.0			01/11/2012
 * @author 	Ashish Marajan
 * <br>Creation Date: 		06/27/2001 18:18:19
 */

public class FrmEmployeeAccessRightsSEC005
	extends RTSDialogBox
	implements ActionListener, FocusListener, KeyListener, MouseMotionListener
{
	public static final String TXT_SAMPLE_PREFIX = "999-";

	private RTSButton ivjbtnAccounting = null;
	private RTSButton ivjbtnFunds = null;
	private RTSButton ivjbtnInquiry = null;
	private RTSButton ivjbtnInventory = null;
	private RTSButton ivjbtnLocalOptions = null;
	private RTSButton ivjbtnMiscellaneous = null;
	private RTSButton ivjbtnMiscellaneousRegistration = null;
	private RTSButton ivjbtnRegistrationOnly = null;
	private RTSButton ivjbtnReports = null;
	private RTSButton ivjbtnSpecialPlates = null;
	private RTSButton ivjbtnStatusChanged = null;
	private RTSButton ivjbtnTitleRegistration = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblFirstName = null;
	private JLabel ivjstcLblLastName = null;
	private JLabel ivjstcLblMiddleInit = null;
	private RTSInputField ivjtxtEmployeeId = null;
	private RTSInputField ivjtxtFirstName = null;
	private RTSInputField ivjtxtLastName = null;
	private RTSInputField ivjtxtMiddleInit = null;
	private RTSButton ivjbtnHelp = null;
	private JPanel ivjpnlSelect = null;
	private JCheckBox ivjchkResetPassword = null;
	private JCheckBox ivjchkUsePrefix = null;
	private JPanel ivjFrmEmployeeAccessRightsSEC005ContentPane1 = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnAdd = null;
	private RTSButton ivjbtnDelete = null;
	private RTSButton ivjbtnRevise = null;
	private JLabel ivjchkAcct = null;
	private JLabel ivjchkFunds = null;
	private JLabel ivjchkInq = null;
	private JLabel ivjchkInv = null;
	private JLabel ivjchkLclOptn = null;
	private JLabel ivjchkMisc = null;
	private JLabel ivjchkMiscReg = null;
	private JLabel ivjchkReg = null;
	private JLabel ivjchkRpt = null;
	private JLabel ivjchkSession = null;
	private JLabel ivjchkStChng = null;
	// defect 9124
	private JLabel ivjchkSplPlts = null;
	// end defect 9124
	private JLabel ivjchkTtlReg = null;
	private JTextArea ivjtxtAreaSession = null;
	private SecurityClientDataObject caSecData = null;
	private SecurityData caSecDataUpdtOrig = null;
	private JTextArea ivjtxtRstPass = null;

	//defect 7891
	private RTSButton[] caarBtnArr = new RTSButton[5];
	// defect 9124
	private RTSButton[] caarBtnArrSec = new RTSButton[12];
	// end defect 9124
	//end defect 7891
	private int ciSelect = 0;
	private int ciSelectSec = 0;

	private boolean cbAddSecurity = false;
	private boolean cbIsCancelClicked = false;
	// initialize this to false
	private boolean cbEmpIdFocusGained = false;
	private JComponent caButtonPressed;
	private JLabel ivjstclblAdUserId = null;
	private JCheckBox ivjchkSearchUserName = null;
	private JLabel ivjlblUserNamePrefix = null;
	private RTSInputField ivjtxtUserNameBaseId = null;

	/**
	 * FrmEmployeeAccessRightsSEC005 constructor comment.
	 */
	public FrmEmployeeAccessRightsSEC005()
	{
		super();
		initialize();
	}

	/**
	 * FrmEmployeeAccessRightsSEC005 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmEmployeeAccessRightsSEC005(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmEmployeeAccessRightsSEC005 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmEmployeeAccessRightsSEC005(Frame aaOwner)
	{
		super(aaOwner);
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
			// move these up to avoid extra processing
			if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
				return;
			}
			else if (aaAE.getSource() == getbtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.SEC005);
				return;
			}

			// change display of prefix based on chk box
			else if (aaAE.getSource() == getchkUsePrefix())
			{
				clearAllColor(this);
				if (getchkUsePrefix().isSelected())
				{
					getlblUserNamePrefix().setVisible(true);
					getlblUserNamePrefix().setEnabled(true);

					gettxtUserNameBaseId().setMaxLength(
						LocalOptionConstant
							.USER_NAME_BASE_ID_MAX_LENGTH_PREFIX);
				}
				else
				{
					getlblUserNamePrefix().setVisible(false);
					getlblUserNamePrefix().setEnabled(false);
					gettxtUserNameBaseId().setMaxLength(
						LocalOptionConstant
							.USER_NAME_BASE_ID_MAX_LENGTH_NONPREFIX);
				}
				if (getchkSearchUserName().isSelected())
				{
					gettxtUserNameBaseId().requestFocus();
				}
				else
				{
					gettxtEmployeeId().requestFocus();
				}
				return;

			}
			// enable UserName so it can be used for searching
			else if (aaAE.getSource() == getchkSearchUserName())
			{
				clearAllColor(this);
				if (getchkSearchUserName().isSelected())
				{
					gettxtUserNameBaseId().setEnabled(true);
					gettxtUserNameBaseId().setEditable(true);
					gettxtUserNameBaseId().requestFocus();
				}
				else
				{
					gettxtUserNameBaseId().setEditable(false);
					gettxtUserNameBaseId().setEnabled(false);
					gettxtEmployeeId().requestFocus();
				}
				return;
			}

			clearAllColor(this);

			//First do the validation
			String lsEmpLstNm = gettxtLastName().getText().trim();
			String lsEmpFstNm = gettxtFirstName().getText().trim();
			// also validate for delete to protect security log
			if (aaAE.getSource() == getbtnAdd()
				|| aaAE.getSource() == getbtnRevise()
				|| aaAE.getSource() == getbtnDelete())
			{
				RTSException leRTSEx = new RTSException();
				if (lsEmpLstNm.length() == 0)
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtLastName());
				}
				// allow first name to be checked even if last name is  
				// in error remove else.
				if (lsEmpFstNm.length() == 0)
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtFirstName());
				}
				// verify Employee Id length before attempting to 
				// process 
				if (gettxtEmployeeId().getText().trim().length()
					< LocalOptionConstant.EMP_ID_MIN_LENGTH)
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_EMP_ID_TOO_SHORT),
						gettxtEmployeeId());
				}
				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
			}

			setEmpTextFieldsToDataObj();
			if (aaAE.getSource() == getbtnAdd())
			{
				gettxtEmployeeId().requestFocus();
				RTSException leRTSEx =
					new RTSException(
						RTSException.CTL001,
						LocalOptionConstant.MSG_NEW_EMP_WILL_BE_ADDED,
						ScreenConstant.CTL001_FRM_TITLE);
				int liRetCode = leRTSEx.displayError(this);

				// clear the selection
				getbtnAdd().setSelected(false);

				if (liRetCode == RTSException.YES)
				{
					setDefaultNewEmpBtns();
					doAddNewEmp();
					getController().processData(
						VCEmployeeAccessRightsSEC005.ADD,
						caSecDataUpdtOrig);
				}
			}
			else if (aaAE.getSource() == getbtnRevise())
			{
				// defect 7891
				//gettxtEmployeeId().requestFocus();
				// end defect 7891
				RTSException leRTSEx =
					new RTSException(
						RTSException.CTL001,
						LocalOptionConstant.MSG_EMP_ID_WILL_BE_UPDATED,
						ScreenConstant.CTL001_FRM_TITLE);
				int liRetCode = leRTSEx.displayError(this);

				// clear the selection
				getbtnRevise().setSelected(false);
				if (liRetCode == RTSException.YES)
				{
					if (getchkResetPassword().isSelected())
					{
						// do not show error message here anymore
						//RTSException ex = new RTSException(164);
						//ex.displayError(this);
						caSecDataUpdtOrig.setResetPassword(true);
						// set the update JNI flag
						caSecDataUpdtOrig.setJniUpdate(true);
						// clear the check box before leaving
						getchkResetPassword().setSelected(false);
					}
					else
					{
						// turn off reset password if it is off
						caSecDataUpdtOrig.setResetPassword(false);
					}
					updtOrigDataObj();
					getController().processData(
						VCEmployeeAccessRightsSEC005.REVISE,
						caSecDataUpdtOrig);
					//reinitializeWindow();
				}
			}
			else if (aaAE.getSource() == getbtnDelete())
			{
				// defect 7891
				// Do not need to move the focus to EmployeeID because
				// that field is not enabled.
				//gettxtEmployeeId().requestFocus();
				// clear the selection
				//getbtnDelete().setSelected(false);
				// end defect 7891

				RTSException leRTSEX =
					new RTSException(
						RTSException.CTL001,
						LocalOptionConstant.MSG_EMP_ID_WILL_BE_DELETED,
						ScreenConstant.CTL001_FRM_TITLE);
				int liRetCode = leRTSEX.displayError(this);
				if (liRetCode == RTSException.YES)
				{
					getController().processData(
						VCEmployeeAccessRightsSEC005.DELETE,
						caSecDataUpdtOrig);
				}
			}
			else if (aaAE.getSource() == getbtnRegistrationOnly())
			{
				caButtonPressed = getbtnRegistrationOnly();
				// request focus to button before sending request out.
				getbtnRegistrationOnly().requestFocus();
				getController().processData(
					VCEmployeeAccessRightsSEC005.REGONLY,
					caSecData);
			}
			else if (aaAE.getSource() == getbtnTitleRegistration())
			{
				caButtonPressed = getbtnTitleRegistration();
				// request focus to button before sending request out.
				getbtnTitleRegistration().requestFocus();
				getController().processData(
					VCEmployeeAccessRightsSEC005.TITLE_REG,
					caSecData);
			}
			else if (aaAE.getSource() == getbtnStatusChanged())
			{
				caButtonPressed = getbtnStatusChanged();
				// request focus to button before sending request out.
				getbtnStatusChanged().requestFocus();
				getController().processData(
					VCEmployeeAccessRightsSEC005.STATCHNG,
					caSecData);
			}
			else if (aaAE.getSource() == getbtnInquiry())
			{
				caButtonPressed = getbtnInquiry();
				// request focus to button before sending request out.
				getbtnInquiry().requestFocus();
				getController().processData(
					VCEmployeeAccessRightsSEC005.INQRY,
					caSecData);
			}
			else if (
				aaAE.getSource() == getbtnMiscellaneousRegistration())
			{
				caButtonPressed = getbtnMiscellaneousRegistration();
				// request focus to button before sending request out.
				getbtnMiscellaneousRegistration().requestFocus();
				getController().processData(
					VCEmployeeAccessRightsSEC005.MISCREG,
					caSecData);
			}
			// defect 9124
			else if (aaAE.getSource() == getbtnSpecialPlates())
			{
				caButtonPressed = getbtnSpecialPlates();
				getbtnSpecialPlates().requestFocus();
				getController().processData(
					VCEmployeeAccessRightsSEC005.SPLPLTS,
					caSecData);
			}
			// end defect 9124
			else if (aaAE.getSource() == getbtnMiscellaneous())
			{
				caButtonPressed = getbtnMiscellaneous();
				// request focus to button before sending request out.
				getbtnMiscellaneous().requestFocus();
				getController().processData(
					VCEmployeeAccessRightsSEC005.MISC,
					caSecData);
			}
			else if (aaAE.getSource() == getbtnReports())
			{
				caButtonPressed = getbtnReports();
				// request focus to button before sending request out.
				getbtnReports().requestFocus();
				getController().processData(
					VCEmployeeAccessRightsSEC005.REPRT,
					caSecData);
			}
			else if (aaAE.getSource() == getbtnLocalOptions())
			{
				caButtonPressed = getbtnLocalOptions();
				// request focus to button before sending request out.
				getbtnLocalOptions().requestFocus();

				getController().processData(
					VCEmployeeAccessRightsSEC005.LOCAL,
					caSecData);
			}
			else if (aaAE.getSource() == getbtnAccounting())
			{
				caButtonPressed = getbtnAccounting();
				// request focus to button before sending request out.
				getbtnAccounting().requestFocus();
				getController().processData(
					VCEmployeeAccessRightsSEC005.ACCT,
					caSecData);
			}
			else if (aaAE.getSource() == getbtnInventory())
			{
				caButtonPressed = getbtnInventory();
				// request focus to button before sending request out.
				getbtnInventory().requestFocus();
				getController().processData(
					VCEmployeeAccessRightsSEC005.INV,
					caSecData);
			}
			else if (aaAE.getSource() == getbtnFunds())
			{
				caButtonPressed = getbtnFunds();
				// request focus to button before sending request out.
				getbtnFunds().requestFocus();
				getController().processData(
					VCEmployeeAccessRightsSEC005.FUNDS,
					caSecData);
			}
			else if (aaAE.getSource() == getchkResetPassword())
			{
				if (getchkResetPassword().isSelected())
				{
					RTSException leRTSEX =
						new RTSException(
							RTSException.CTL001,
							LocalOptionConstant.MSG_RESET_PASSWORD,
							ScreenConstant.CTL001_FRM_TITLE);
					int liRetCode = leRTSEX.displayError(this);
					if (liRetCode == RTSException.YES)
					{
						gettxtRstPass().setVisible(true);
					}
					else
					{
						getchkResetPassword().setSelected(false);
					}
				}
				else
				{
					gettxtRstPass().setVisible(false);
				}
			}
		}
		catch (RTSException leRTSEx)
		{
			leRTSEx.displayError(this);
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Clears all checkboxes in the frame
	 * Also clear out the buttons.
	 * 
	 */
	private void clearCheckBoxs()
	{
		getchkAcct().setIcon(null);
		getchkFunds().setIcon(null);
		getchkInq().setIcon(null);
		getchkInv().setIcon(null);
		getchkLclOptn().setIcon(null);
		getchkMisc().setIcon(null);
		getchkMiscReg().setIcon(null);
		getchkReg().setIcon(null);
		getchkResetPassword().setIcon(null);
		getchkRpt().setIcon(null);
		// defect 9124
		getchkSplPlts().setIcon(null);
		// end defect 9124
		getchkStChng().setIcon(null);
		getchkTtlReg().setIcon(null);
		// clear button pressed
		caButtonPressed = null;
		// reset buttons to un-selected
		getbtnAdd().setSelected(false);
		getbtnDelete().setSelected(false);
		getbtnRevise().setSelected(false);
		getbtnCancel().setSelected(false);
		getbtnHelp().setSelected(false);
		getbtnAccounting().setSelected(false);
		getbtnFunds().setSelected(false);
		getbtnInquiry().setSelected(false);
		getbtnInventory().setSelected(false);
		getbtnLocalOptions().setSelected(false);
		getbtnMiscellaneous().setSelected(false);
		getbtnMiscellaneousRegistration().setSelected(false);
		getbtnRegistrationOnly().setSelected(false);
		getbtnReports().setSelected(false);
		// defect 9124
		getbtnSpecialPlates().setSelected(false);
		// end defect 9124
		// make sure reset password is cleared
		getchkResetPassword().setSelected(false);
		getbtnStatusChanged().setSelected(false);
		getbtnTitleRegistration().setSelected(false);
		// defect 8970
		// clear out cancel clicked boolean
		//cbIsCancelClicked = false;
		// end defect 8970
	}

	//	/**
	//	 * Disables the Inventory and Funds button for VTR HQ
	//	 * 
	//	 */
	//	private void disableBtnsForRegMainOffc()
	//	{
	//		if (caSecData.getWorkStationType()
	//			== LocalOptionConstant.COUNTY)
	//		{
	//			// No action
	//		}
	//		else if (
	//			caSecData.getWorkStationType()
	//				== LocalOptionConstant.REGION)
	//		{
	//			// No action
	//		}
	//		else if (
	// caSecData.getWorkStationType() == LocalOptionConstant.VTR)
	// defect 9085  
	//if (SystemProperty.isHQ())
	//{
	// defect 9124
	//getbtnInventory().setEnabled(false);
	// end defect 9124
	//getbtnFunds().setEnabled(false);
	//}
	// end defect 9085 
	//}

	/**
	 * Set up to add a new employee
	 * 
	 * @throws RTSException
	 */
	private void doAddNewEmp() throws RTSException
	{
		setEmpTextFieldsToDataObj();
		updtOrigDataObj();
		setCustomer();
		caSecDataUpdtOrig.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		caSecDataUpdtOrig.setSubstaId(SystemProperty.getSubStationId());
	}

	/**
	 * Intialize the new employee security data.
	 */
	private void doInitAddEmp()
	{
		//Comes here when it is a new Add Object
		caSecData.setSecData(new SecurityData());
		caSecDataUpdtOrig = new SecurityData();
		//Set the default values
		getbtnAdd().setEnabled(true);
		getbtnRevise().setEnabled(false);
		getbtnDelete().setEnabled(false);
		enableTextFields(true);
		gettxtEmployeeId().setEnabled(true);
		gettxtEmployeeId().setEditable(true);

		// populate empid from UserName
		if (gettxtEmployeeId().getText().trim().length() < 1)
		{
			gettxtEmployeeId().setText(
				gettxtUserNameBaseId().getText().trim());
			caSecDataUpdtOrig.setEmpId(
				gettxtUserNameBaseId().getText());

			// setup the boolean indicating that the
			// User Name was provided by the user
			// move this up before setupUserName
			caSecDataUpdtOrig.setUserProvidedUserName(true);
			caSecDataUpdtOrig.setUserName(setupUserName());
		}
	}

	/**
	 * Enable or Disable all buttons depending on the boolean argument
	 * 
	 * @param abVal boolean
	 */
	private void enableButtons(boolean abVal)
	{
		getbtnAdd().setEnabled(abVal);
		getbtnRevise().setEnabled(abVal);
		getbtnDelete().setEnabled(abVal);
		getbtnRegistrationOnly().setEnabled(abVal);
		getbtnTitleRegistration().setEnabled(abVal);
		// defect 9124
		getbtnSpecialPlates().setEnabled(abVal);
		// end defect 9124
		getbtnStatusChanged().setEnabled(abVal);
		getbtnInquiry().setEnabled(abVal);
		getbtnMiscellaneousRegistration().setEnabled(abVal);
		getbtnMiscellaneous().setEnabled(abVal);
		getbtnReports().setEnabled(abVal);
		getbtnLocalOptions().setEnabled(abVal);
		getbtnAccounting().setEnabled(abVal);
		getbtnInventory().setEnabled(abVal);
		getbtnFunds().setEnabled(abVal);
	}

	/**
	 * This method sets the editablility of the text fields.
	 * 
	 * @param abSwitch boolean
	 */
	private void enableTextFields(boolean abSwitch)
	{
		gettxtLastName().setEnabled(abSwitch);
		gettxtLastName().setEditable(abSwitch);
		gettxtFirstName().setEnabled(abSwitch);
		gettxtFirstName().setEditable(abSwitch);
		gettxtMiddleInit().setEnabled(abSwitch);
		gettxtMiddleInit().setEditable(abSwitch);
	}

	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{
		// restructure if statements to so that search
		// will occur if moving from UserNameBaseId to EmpId

		// use chkSearchUserName if an Employee Id was entered.
		if (cbEmpIdFocusGained
			&& ((aaFE.getSource() == getchkSearchUserName()
				&& gettxtEmployeeId().getText().trim().length() > 0)
				|| aaFE.getSource() == gettxtFirstName()
				|| aaFE.getSource() == gettxtLastName()
				|| aaFE.getSource() == gettxtMiddleInit()
				|| (aaFE.getSource() == gettxtEmployeeId()
					&& gettxtUserNameBaseId().getText().trim().length()
						> 0)))
		{
			// do not act on focus events while working the previous one.
			if (!startWorking())
			{
				return;
			}
			try
			{
				RTSException leRTSEx = new RTSException();
				cbEmpIdFocusGained = false;

				// get the UserName
				String lsUserName = null;
				lsUserName = setupUserName();

				// check for null on EmployeeId field before 
				// referencing
				String lsEmpId = CommonConstant.STR_SPACE_EMPTY;
				if (gettxtEmployeeId().getText() != null)
				{
					lsEmpId = gettxtEmployeeId().getText().trim();
				}
				// populate strEmpId from gettxtUserNameBaseId() 
				// if strEmpId is empty
				if (lsEmpId.length() == 0)
				{
					lsEmpId = gettxtUserNameBaseId().getText();
				}
				if (caSecData != null)
				{
					caSecData.getSecData().setEmpId(lsEmpId);
					caSecData.getSecData().setUserName(lsUserName);
				}
				// replace the if logic that was here with a new 
				// method
				if (!isValidEmpId())
				{
					// change message number to 753 from 222	
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_EMP_ID_TOO_SHORT),
						gettxtEmployeeId());
					// if UserName is enabled, paint it red.
					if (getchkSearchUserName().isSelected())
					{
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_EMP_ID_TOO_SHORT),
							gettxtUserNameBaseId());
						if (!gettxtUserNameBaseId().hasFocus()
							&& !gettxtEmployeeId().hasFocus())
						{
							gettxtUserNameBaseId().requestFocus();
						}
					}
					else
					{
						// put the focus back on empid for errors
						if (!gettxtEmployeeId().hasFocus()
							&& !gettxtUserNameBaseId().hasFocus())
						{
							gettxtEmployeeId().requestFocus();
						}
					}
				}
				else
				{
					//Extract employee information with Employee ID
					if (caSecData == null && !cbIsCancelClicked)
					{
						// clear the color before getting the data
						clearAllColor(this);

						SecurityData laSecData = new SecurityData();
						laSecData.setEmpId(
							gettxtEmployeeId().getText());

						// set boolean based on entry of UserNameBaseId
						if (gettxtUserNameBaseId()
							.getText()
							.trim()
							.length()
							> 0
							&& gettxtUserNameBaseId().isEnabled())
						{
							laSecData.setUserProvidedUserName(true);
						}

						laSecData.setUserName(setupUserName());

						laSecData.setOfcIssuanceNo(
							SystemProperty.getOfficeIssuanceNo());
						laSecData.setSubstaId(
							SystemProperty.getSubStationId());
						// check for user id
						getController().processData(
							AbstractViewController.SEARCH,
							laSecData);
					}
				}
				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					if (!leRTSEx
						.getFirstComponent()
						.equals(gettxtUserNameBaseId())
						&& !leRTSEx.getFirstComponent().equals(
							gettxtEmployeeId()))
					{
						leRTSEx.getFirstComponent().requestFocus();
					}
					return;
				}
			}
			// turn off the working indicator
			finally
			{
				doneWorking();
			}
		}
		else if (aaFE.getSource() == gettxtEmployeeId())
		{
			// do not clear color at this time	
			// clearAllColor(this);
			// manage focus gained indicator and take the appropriate 
			// action clear color if focus is gained on Employee Id
			//			if (!cbEmpIdFocusGained)
			//			{
			//				clearAllColor(this);
			//			}

			if (caSecData == null)
			{
				cbEmpIdFocusGained = true;
			}
		}
		else if (aaFE.getSource() == gettxtUserNameBaseId())
		{
			// clear color if focus is gained on User Name
			//			if (!cbEmpIdFocusGained)
			//			{
			//				clearAllColor(this);
			//			}
			// also set focus boolean for UserName
			if (caSecData == null)
			{
				cbEmpIdFocusGained = true;
			}
		}
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		//Make sure security data is not null
		if (caSecData != null)
		{
			if (aaFE.getSource() == gettxtLastName())
			{
				caSecData.getSecData().setEmpLastName(
					gettxtLastName().getText());
			}
			else if (aaFE.getSource() == gettxtFirstName())
			{
				caSecData.getSecData().setEmpFirstName(
					gettxtFirstName().getText());
				// Populate User Name Base if possible
				// is we are in add mode
				if (gettxtUserNameBaseId().getText().trim().length()
					< 1
					&& getbtnAdd().isEnabled())
				{
					String lsUserName = setupUserName();

					// remove the prefix if is exists
					lsUserName = parseOutPrefix(lsUserName);

					// trim the username back before putting 
					// on screen
					if (lsUserName.length()
						> LocalOptionConstant.EMP_ID_MAX_LENGTH)
					{
						lsUserName =
							lsUserName.substring(
								0,
								LocalOptionConstant.EMP_ID_MAX_LENGTH);
					}

					// trim the string before putting it to frame.
					gettxtUserNameBaseId().setText(lsUserName);
				}
			}
			else if (aaFE.getSource() == gettxtMiddleInit())
			{
				caSecData.getSecData().setEmpMI(
					gettxtMiddleInit().getText());
			}
		}
	}

	/**
	 * Return the btnAccounting property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnAccounting()
	{
		if (ivjbtnAccounting == null)
		{
			try
			{
				ivjbtnAccounting = new RTSButton();
				ivjbtnAccounting.setName("btnAccounting");
				ivjbtnAccounting.setMnemonic('c');
				ivjbtnAccounting.setText(
					GeneralConstant.TXT_MODULE_NAME_ACCOUNTING);
				ivjbtnAccounting.setMaximumSize(new Dimension(99, 25));
				ivjbtnAccounting.setActionCommand(
					GeneralConstant.TXT_MODULE_NAME_ACCOUNTING);
				ivjbtnAccounting.setBounds(20, 286, 229, 25);
				ivjbtnAccounting.setMinimumSize(new Dimension(99, 25));
				// user code begin {1}
				ivjbtnAccounting.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnAccounting;
	}

	/**
	 * Return the btnAdd property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnAdd()
	{
		if (ivjbtnAdd == null)
		{
			try
			{
				ivjbtnAdd = new RTSButton();
				ivjbtnAdd.setName("btnAdd");
				ivjbtnAdd.setMnemonic('a');
				ivjbtnAdd.setText(CommonConstant.BTN_TXT_ADD);
				ivjbtnAdd.setBounds(20, 448, 99, 25);
				// user code begin {1}
				ivjbtnAdd.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnAdd;
	}

	/**
	 * Return the btnCancel property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText(CommonConstant.BTN_TXT_CANCEL);
				ivjbtnCancel.setBounds(377, 448, 99, 25);
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				ivjbtnCancel.addMouseMotionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnCancel;
	}

	/**
	 * Return the btnDelete property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnDelete()
	{
		if (ivjbtnDelete == null)
		{
			try
			{
				ivjbtnDelete = new RTSButton();
				ivjbtnDelete.setName("btnDelete");
				ivjbtnDelete.setMnemonic('d');
				ivjbtnDelete.setText(CommonConstant.BTN_TXT_DELETE);
				ivjbtnDelete.setBounds(258, 448, 99, 25);
				// user code begin {1}
				ivjbtnDelete.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnDelete;
	}

	/**
	 * Return the btnFunds property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnFunds()
	{
		if (ivjbtnFunds == null)
		{
			try
			{
				ivjbtnFunds = new RTSButton();
				ivjbtnFunds.setName("btnFunds");
				ivjbtnFunds.setMnemonic('f');
				ivjbtnFunds.setText(
					GeneralConstant.TXT_MODULE_NAME_FUNDS);
				ivjbtnFunds.setMaximumSize(new Dimension(69, 25));
				ivjbtnFunds.setActionCommand(
					GeneralConstant.TXT_MODULE_NAME_FUNDS);
				ivjbtnFunds.setBounds(20, 346, 229, 25);
				ivjbtnFunds.setMinimumSize(new Dimension(69, 25));
				// user code begin {1}
				ivjbtnFunds.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnFunds;
	}

	/**
	 * Return the btnHelp property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnHelp()
	{
		if (ivjbtnHelp == null)
		{
			try
			{
				ivjbtnHelp = new RTSButton();
				ivjbtnHelp.setName("btnHelp");
				ivjbtnHelp.setMnemonic('H');
				ivjbtnHelp.setText(CommonConstant.BTN_TXT_HELP);
				ivjbtnHelp.setMaximumSize(new Dimension(59, 25));
				ivjbtnHelp.setActionCommand(
					CommonConstant.BTN_TXT_HELP);
				ivjbtnHelp.setBounds(496, 448, 99, 25);
				ivjbtnHelp.setMinimumSize(new Dimension(59, 25));
				// user code begin {1}
				ivjbtnHelp.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnHelp;
	}

	/**
	 * Return the btnInquiry property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnInquiry()
	{
		if (ivjbtnInquiry == null)
		{
			try
			{
				ivjbtnInquiry = new RTSButton();
				ivjbtnInquiry.setName("btnInquiry");
				ivjbtnInquiry.setMnemonic('I');
				ivjbtnInquiry.setText(
					GeneralConstant.TXT_MODULE_NAME_INQUIRY);
				ivjbtnInquiry.setMaximumSize(new Dimension(73, 25));
				ivjbtnInquiry.setActionCommand(
					GeneralConstant.TXT_MODULE_NAME_INQUIRY);
				ivjbtnInquiry.setBounds(20, 106, 229, 25);
				ivjbtnInquiry.setMinimumSize(new Dimension(73, 25));
				// user code begin {1}
				ivjbtnInquiry.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnInquiry;
	}

	/**
	 * Return the btnInventory property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnInventory()
	{
		if (ivjbtnInventory == null)
		{
			try
			{
				ivjbtnInventory = new RTSButton();
				ivjbtnInventory.setName("btnInventory");
				ivjbtnInventory.setMnemonic('v');
				ivjbtnInventory.setText(
					GeneralConstant.TXT_MODULE_NAME_INVENTORY);
				ivjbtnInventory.setMaximumSize(new Dimension(87, 25));
				ivjbtnInventory.setActionCommand(
					GeneralConstant.TXT_MODULE_NAME_INVENTORY);
				ivjbtnInventory.setBounds(20, 316, 229, 25);
				ivjbtnInventory.setMinimumSize(new Dimension(87, 25));
				// user code begin {1}
				ivjbtnInventory.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnInventory;
	}

	/**
	 * Return the btnLocalOptions property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnLocalOptions()
	{
		if (ivjbtnLocalOptions == null)
		{
			try
			{
				ivjbtnLocalOptions = new RTSButton();
				ivjbtnLocalOptions.setName("btnLocalOptions");
				ivjbtnLocalOptions.setMnemonic('l');
				ivjbtnLocalOptions.setText(
					GeneralConstant.TXT_MODULE_NAME_LOCAL_OPTIONS);
				ivjbtnLocalOptions.setMaximumSize(
					new Dimension(113, 25));
				ivjbtnLocalOptions.setActionCommand(
					GeneralConstant.TXT_MODULE_NAME_LOCAL_OPTIONS);
				ivjbtnLocalOptions.setBounds(20, 256, 229, 25);
				ivjbtnLocalOptions.setMinimumSize(
					new Dimension(113, 25));
				// user code begin {1}
				ivjbtnLocalOptions.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnLocalOptions;
	}

	/**
	 * Return the btnMiscellaneous property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnMiscellaneous()
	{
		if (ivjbtnMiscellaneous == null)
		{
			try
			{
				ivjbtnMiscellaneous = new RTSButton();
				ivjbtnMiscellaneous.setName("btnMiscellaneous");
				ivjbtnMiscellaneous.setMnemonic('u');
				ivjbtnMiscellaneous.setText(
					GeneralConstant.TXT_MODULE_NAME_MISCELLANEOUS);
				ivjbtnMiscellaneous.setMaximumSize(
					new Dimension(117, 25));
				ivjbtnMiscellaneous.setActionCommand(
					GeneralConstant.TXT_MODULE_NAME_MISCELLANEOUS);
				ivjbtnMiscellaneous.setBounds(20, 196, 229, 25);
				ivjbtnMiscellaneous.setMinimumSize(
					new Dimension(117, 25));
				// user code begin {1}
				ivjbtnMiscellaneous.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnMiscellaneous;
	}

	/**
	 * Return the btnMiscellaneousRegistration property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnMiscellaneousRegistration()
	{
		if (ivjbtnMiscellaneousRegistration == null)
		{
			try
			{
				ivjbtnMiscellaneousRegistration = new RTSButton();
				ivjbtnMiscellaneousRegistration.setName(
					"btnMiscellaneousRegistration");
				ivjbtnMiscellaneousRegistration.setMnemonic('m');
				ivjbtnMiscellaneousRegistration.setText(
					GeneralConstant
						.TXT_MODULE_NAME_MISCELLANEOUS_REGISTRATION);
				ivjbtnMiscellaneousRegistration.setMaximumSize(
					new Dimension(189, 25));
				ivjbtnMiscellaneousRegistration.setActionCommand(
					GeneralConstant
						.TXT_MODULE_NAME_MISCELLANEOUS_REGISTRATION);
				ivjbtnMiscellaneousRegistration.setBounds(
					20,
					136,
					229,
					25);
				ivjbtnMiscellaneousRegistration.setMinimumSize(
					new Dimension(189, 25));
				// user code begin {1}
				ivjbtnMiscellaneousRegistration.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnMiscellaneousRegistration;
	}

	/**
	 * Return the btnRegistrationOnly property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnRegistrationOnly()
	{
		if (ivjbtnRegistrationOnly == null)
		{
			try
			{
				ivjbtnRegistrationOnly = new RTSButton();
				ivjbtnRegistrationOnly.setName("btnRegistrationOnly");
				ivjbtnRegistrationOnly.setMnemonic('O');
				ivjbtnRegistrationOnly.setText(
					GeneralConstant.TXT_MODULE_NAME_REGISTRATION_ONLY);
				ivjbtnRegistrationOnly.setMaximumSize(
					new Dimension(131, 25));
				ivjbtnRegistrationOnly.setActionCommand(
					GeneralConstant.TXT_MODULE_NAME_REGISTRATION_ONLY);
				ivjbtnRegistrationOnly.setBounds(20, 16, 229, 25);
				ivjbtnRegistrationOnly.setMinimumSize(
					new Dimension(131, 25));
				// user code begin {1}
				ivjbtnRegistrationOnly.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnRegistrationOnly;
	}

	/**
	 * Return the btnReports property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnReports()
	{
		if (ivjbtnReports == null)
		{
			try
			{
				ivjbtnReports = new RTSButton();
				ivjbtnReports.setName("btnReports");
				ivjbtnReports.setMnemonic('p');
				ivjbtnReports.setText(
					GeneralConstant.TXT_MODULE_NAME_REPORTS);
				ivjbtnReports.setMaximumSize(new Dimension(79, 25));
				ivjbtnReports.setActionCommand(
					GeneralConstant.TXT_MODULE_NAME_REPORTS);
				ivjbtnReports.setBounds(20, 226, 229, 25);
				ivjbtnReports.setMinimumSize(new Dimension(79, 25));
				// user code begin {1}
				ivjbtnReports.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnReports;
	}

	/**
	 * Return the btnRevise property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnRevise()
	{
		if (ivjbtnRevise == null)
		{
			try
			{
				ivjbtnRevise = new RTSButton();
				ivjbtnRevise.setName("btnRevise");
				ivjbtnRevise.setMnemonic('r');
				ivjbtnRevise.setText(CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setBounds(139, 448, 99, 25);
				// user code begin {1}
				ivjbtnRevise.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnRevise;
	}

	/**
	 * Return the btnStatusChanged property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnStatusChanged()
	{
		if (ivjbtnStatusChanged == null)
		{
			try
			{
				ivjbtnStatusChanged = new RTSButton();
				ivjbtnStatusChanged.setName("btnStatusChanged");
				ivjbtnStatusChanged.setMnemonic('s');
				ivjbtnStatusChanged.setText(
					GeneralConstant.TXT_MODULE_SUB_TITLE_STATUS_CHANGE);
				ivjbtnStatusChanged.setMaximumSize(
					new Dimension(125, 25));
				ivjbtnStatusChanged.setActionCommand(
					GeneralConstant.TXT_MODULE_SUB_TITLE_STATUS_CHANGE);
				ivjbtnStatusChanged.setBounds(20, 76, 229, 25);
				ivjbtnStatusChanged.setMinimumSize(
					new Dimension(125, 25));
				// user code begin {1}
				ivjbtnStatusChanged.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnStatusChanged;
	}

	/**
	 * Return the btnTitleRegistration property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnTitleRegistration()
	{
		if (ivjbtnTitleRegistration == null)
		{
			try
			{
				ivjbtnTitleRegistration = new RTSButton();
				ivjbtnTitleRegistration.setName("btnTitleRegistration");
				ivjbtnTitleRegistration.setMnemonic('T');
				ivjbtnTitleRegistration.setText(
					GeneralConstant.TXT_MODULE_NAME_TITLE_REGISTRATION);
				ivjbtnTitleRegistration.setMaximumSize(
					new Dimension(131, 25));
				ivjbtnTitleRegistration.setActionCommand(
					GeneralConstant.TXT_MODULE_NAME_TITLE_REGISTRATION);
				ivjbtnTitleRegistration.setBounds(20, 46, 229, 25);
				ivjbtnTitleRegistration.setMinimumSize(
					new Dimension(131, 25));
				// user code begin {1}
				ivjbtnTitleRegistration.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnTitleRegistration;
	}
	/**
	 * Return the btnSpecialPlate property value.
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
				ivjbtnSpecialPlates.setName("btnSpecialPlates");
				// defect 9188 
				// Change from 'a' to 'e' 
				ivjbtnSpecialPlates.setMnemonic('e');
				// end defect 9188 
				ivjbtnSpecialPlates.setText(
					GeneralConstant.TXT_MODULE_NAME_SPECIALPLATES);
				ivjbtnSpecialPlates.setMaximumSize(
					new Dimension(189, 25));
				ivjbtnSpecialPlates.setActionCommand(
					GeneralConstant.TXT_MODULE_NAME_SPECIALPLATES);
				ivjbtnSpecialPlates.setBounds(20, 166, 229, 25);
				ivjbtnSpecialPlates.setMinimumSize(
					new Dimension(189, 25));
				// user code begin {1}
				ivjbtnSpecialPlates.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnSpecialPlates;
	}
	/**
	 * Return the chkAcct property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkAcct()
	{
		if (ivjchkAcct == null)
		{
			try
			{
				ivjchkAcct = new JLabel();
				ivjchkAcct.setName("chkAcct");
				ivjchkAcct.setOpaque(true);
				ivjchkAcct.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkAcct.setBackground(Color.white);
				ivjchkAcct.setBounds(163, 350, 25, 25);
				ivjchkAcct.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkAcct;
	}

	/**
	 * Return the chkFunds property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkFunds()
	{
		if (ivjchkFunds == null)
		{
			try
			{
				ivjchkFunds = new JLabel();
				ivjchkFunds.setName("chkFunds");
				ivjchkFunds.setOpaque(true);
				ivjchkFunds.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkFunds.setBackground(Color.white);
				ivjchkFunds.setBounds(163, 410, 25, 25);
				ivjchkFunds.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkFunds;
	}

	/**
	 * Return the chkInq property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkInq()
	{
		if (ivjchkInq == null)
		{
			try
			{
				ivjchkInq = new JLabel();
				ivjchkInq.setName("chkInq");
				ivjchkInq.setOpaque(true);
				ivjchkInq.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkInq.setBackground(Color.white);
				ivjchkInq.setBounds(163, 170, 25, 25);
				ivjchkInq.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkInq;
	}

	/**
	 * Return the chkInv property value.
	 * 
	 * @return jJLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkInv()
	{
		if (ivjchkInv == null)
		{
			try
			{
				ivjchkInv = new JLabel();
				ivjchkInv.setName("chkInv");
				ivjchkInv.setOpaque(true);
				ivjchkInv.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkInv.setBackground(Color.white);
				ivjchkInv.setBounds(163, 380, 25, 25);
				ivjchkInv.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkInv;
	}

	/**
	 * Return the chkLclOptn property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkLclOptn()
	{
		if (ivjchkLclOptn == null)
		{
			try
			{
				ivjchkLclOptn = new JLabel();
				ivjchkLclOptn.setName("chkLclOptn");
				ivjchkLclOptn.setOpaque(true);
				ivjchkLclOptn.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkLclOptn.setBackground(Color.white);
				ivjchkLclOptn.setBounds(163, 320, 25, 25);
				ivjchkLclOptn.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkLclOptn;
	}

	/**
	 * Return the chkMisc property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkMisc()
	{
		if (ivjchkMisc == null)
		{
			try
			{
				ivjchkMisc = new JLabel();
				ivjchkMisc.setName("chkMisc");
				ivjchkMisc.setOpaque(true);
				ivjchkMisc.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkMisc.setBackground(Color.white);
				ivjchkMisc.setBounds(163, 260, 25, 25);
				ivjchkMisc.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkMisc;
	}

	/**
	 * Return the chkMiscReg property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkMiscReg()
	{
		if (ivjchkMiscReg == null)
		{
			try
			{
				ivjchkMiscReg = new JLabel();
				ivjchkMiscReg.setName("chkMiscReg");
				ivjchkMiscReg.setOpaque(true);
				ivjchkMiscReg.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkMiscReg.setBackground(Color.white);
				ivjchkMiscReg.setBounds(163, 200, 25, 25);
				ivjchkMiscReg.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkMiscReg;
	}

	/**
	 * Return the chkReg property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkReg()
	{
		if (ivjchkReg == null)
		{
			try
			{
				ivjchkReg = new JLabel();
				ivjchkReg.setName("chkReg");
				ivjchkReg.setOpaque(true);
				ivjchkReg.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkReg.setBackground(Color.white);
				ivjchkReg.setBounds(163, 80, 25, 25);
				ivjchkReg.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkReg;
	}
	// defect 9124
	/**
		 * Return the chkSplPlts property value.
		 * 
		 * @return JLabel
		 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkSplPlts()
	{
		if (ivjchkSplPlts == null)
		{
			try
			{
				ivjchkSplPlts = new JLabel();
				ivjchkSplPlts.setName("chkSplPlt");
				ivjchkSplPlts.setOpaque(true);
				ivjchkSplPlts.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkSplPlts.setBackground(Color.white);
				ivjchkSplPlts.setBounds(163, 230, 25, 25);
				ivjchkSplPlts.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkSplPlts;
	}
	// end defect 9124
	/**
	 * Return the chkResetPassword property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkResetPassword()
	{
		if (ivjchkResetPassword == null)
		{
			try
			{
				ivjchkResetPassword = new JCheckBox();
				ivjchkResetPassword.setName("chkResetPassword");
				ivjchkResetPassword.setMnemonic('w');
				ivjchkResetPassword.setText(
					LocalOptionConstant.TXT_RESET_PASSWORD);
				ivjchkResetPassword.setMaximumSize(
					new Dimension(119, 22));
				ivjchkResetPassword.setActionCommand(
					LocalOptionConstant.TXT_RESET_PASSWORD);
				ivjchkResetPassword.setBounds(471, 65, 125, 46);
				ivjchkResetPassword.setMinimumSize(
					new Dimension(119, 22));
				ivjchkResetPassword.setEnabled(false);
				// user code begin {1}
				ivjchkResetPassword.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkResetPassword;
	}

	/**
	 * Return the chkRpt property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkRpt()
	{
		if (ivjchkRpt == null)
		{
			try
			{
				ivjchkRpt = new JLabel();
				ivjchkRpt.setName("chkRpt");
				ivjchkRpt.setOpaque(true);
				ivjchkRpt.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkRpt.setBackground(Color.white);
				ivjchkRpt.setBounds(163, 290, 25, 25);
				ivjchkRpt.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkRpt;
	}

	/**
	 * Return the chkSearchUserName property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSearchUserName()
	{
		if (ivjchkSearchUserName == null)
		{
			try
			{
				ivjchkSearchUserName = new JCheckBox();
				ivjchkSearchUserName.setName("chkSearchUserName");
				ivjchkSearchUserName.setMnemonic('n');
				ivjchkSearchUserName.setText(
					LocalOptionConstant.TXT_ENABLE_USER_NAME_SEARCH);
				ivjchkSearchUserName.setBounds(412, 9, 198, 22);
				// user code begin {1}
				ivjchkSearchUserName.addActionListener(this);
				ivjchkSearchUserName.addKeyListener(this);
				//defect 7891
				ivjchkSearchUserName.addFocusListener(this);
				//end defect 7891
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkSearchUserName;
	}

	/**
	 * Return the chkSession property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkSession()
	{
		if (ivjchkSession == null)
		{
			try
			{
				ivjchkSession = new JLabel();
				ivjchkSession.setName("chkSession");
				ivjchkSession.setOpaque(true);
				ivjchkSession.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkSession.setBackground(Color.white);
				ivjchkSession.setForeground(new Color(102, 102, 153));
				ivjchkSession.setHorizontalTextPosition(
					SwingConstants.CENTER);
				ivjchkSession.setBounds(8, 65, 25, 25);
				ivjchkSession.setEnabled(true);
				ivjchkSession.setHorizontalAlignment(
					SwingConstants.CENTER);
				// user code begin {1}
				getchkSession().setText(CommonConstant.STR_SPACE_EMPTY);
				URL laURL =
					getClass().getResource(
						SystemProperty.getImagesDir()
							+ LocalOptionConstant.CHECK_ICON
							+ CommonConstant.STR_PERIOD
							+ SystemProperty.getImageType());
				if (laURL != null)
				{
					ImageIcon laIcon = new ImageIcon(laURL);
					if (laIcon != null)
						getchkSession().setIcon(laIcon);
				}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkSession;
	}

	/**
	 * Return the chkStChng property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkStChng()
	{
		if (ivjchkStChng == null)
		{
			try
			{
				ivjchkStChng = new JLabel();
				ivjchkStChng.setName("chkStChng");
				ivjchkStChng.setOpaque(true);
				ivjchkStChng.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkStChng.setBackground(Color.white);
				ivjchkStChng.setBounds(163, 140, 25, 25);
				ivjchkStChng.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkStChng;
	}

	/**
	 * Return the chkTtlReg property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getchkTtlReg()
	{
		if (ivjchkTtlReg == null)
		{
			try
			{
				ivjchkTtlReg = new JLabel();
				ivjchkTtlReg.setName("chkTtlReg");
				ivjchkTtlReg.setOpaque(true);
				ivjchkTtlReg.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjchkTtlReg.setBackground(Color.white);
				ivjchkTtlReg.setBounds(163, 110, 25, 25);
				ivjchkTtlReg.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkTtlReg;
	}

	/**
	 * Return the getchkUsePrefix property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkUsePrefix()
	{
		if (ivjchkUsePrefix == null)
		{
			try
			{
				ivjchkUsePrefix = new JCheckBox();
				ivjchkUsePrefix.setName("chkUsePrefix");
				ivjchkUsePrefix.setSelected(true);
				ivjchkUsePrefix.setMnemonic('x');
				ivjchkUsePrefix.setText(
					LocalOptionConstant.TXT_USER_NAME_PREFIX);
				ivjchkUsePrefix.setBounds(462, 215, 151, 27);
				ivjchkUsePrefix.setActionCommand(
					LocalOptionConstant.TXT_USER_NAME_PREFIX);
				// user code begin {1}
				ivjchkUsePrefix.addActionListener(this);
				ivjchkUsePrefix.addKeyListener(this);

				if (SystemProperty.getProdStatus()
					== SystemProperty.APP_PROD_STATUS)
				{
					ivjchkUsePrefix.setEnabled(false);
					ivjchkUsePrefix.setVisible(false);
					ivjchkUsePrefix.setSelected(true);
				}
				else
				{
					ivjchkUsePrefix.setEnabled(true);
					ivjchkUsePrefix.setVisible(true);
					ivjchkUsePrefix.setSelected(false);
					getlblUserNamePrefix().setEnabled(false);
					getlblUserNamePrefix().setVisible(false);
					ivjchkUsePrefix.addActionListener(this);
				}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkUsePrefix;
	}

	/**
	 * Get the employee ID
	 * 
	 * @return String
	 */
	public String getEmpId()
	{
		if (caSecData != null)
		{
			return gettxtEmployeeId().getText();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Return the FrmEmployeeAccessRightsSEC005ContentPane1 property 
	 * value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmEmployeeAccessRightsSEC005ContentPane1()
	{
		if (ivjFrmEmployeeAccessRightsSEC005ContentPane1 == null)
		{
			try
			{
				ivjFrmEmployeeAccessRightsSEC005ContentPane1 =
					new JPanel();
				ivjFrmEmployeeAccessRightsSEC005ContentPane1.setName(
					"FrmEmployeeAccessRightsSEC005ContentPane1");
				ivjFrmEmployeeAccessRightsSEC005ContentPane1.setLayout(
					null);
				ivjFrmEmployeeAccessRightsSEC005ContentPane1
					.setMaximumSize(
					new Dimension(630, 470));
				ivjFrmEmployeeAccessRightsSEC005ContentPane1
					.setMinimumSize(
					new Dimension(630, 470));
				ivjFrmEmployeeAccessRightsSEC005ContentPane1
					.setVisible(
					true);
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					gettxtUserNameBaseId(),
					gettxtUserNameBaseId().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					gettxtEmployeeId(),
					gettxtEmployeeId().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkUsePrefix(),
					getchkUsePrefix().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					gettxtLastName(),
					gettxtLastName().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					gettxtFirstName(),
					gettxtFirstName().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					gettxtMiddleInit(),
					gettxtMiddleInit().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkResetPassword(),
					getchkResetPassword().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getpnlSelect(),
					getpnlSelect().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getstcLblEmployeeId(),
					getstcLblEmployeeId().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getstcLblFirstName(),
					getstcLblFirstName().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getstcLblLastName(),
					getstcLblLastName().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getstcLblMiddleInit(),
					getstcLblMiddleInit().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getbtnAdd(),
					getbtnAdd().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getbtnRevise(),
					getbtnRevise().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getbtnDelete(),
					getbtnDelete().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getbtnCancel(),
					getbtnCancel().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getbtnHelp(),
					getbtnHelp().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkSession(),
					getchkSession().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					gettxtAreaSession(),
					gettxtAreaSession().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkReg(),
					getchkReg().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkTtlReg(),
					getchkTtlReg().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkStChng(),
					getchkStChng().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkInq(),
					getchkInq().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkMiscReg(),
					getchkMiscReg().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkMisc(),
					getchkMisc().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkRpt(),
					getchkRpt().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkLclOptn(),
					getchkLclOptn().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkAcct(),
					getchkAcct().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkInv(),
					getchkInv().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkFunds(),
					getchkFunds().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					gettxtRstPass(),
					gettxtRstPass().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getstclblAdUserId(),
					getstclblAdUserId().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getlblUserNamePrefix(),
					getlblUserNamePrefix().getName());
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkSearchUserName(),
					getchkSearchUserName().getName());
				// defect 9124
				getFrmEmployeeAccessRightsSEC005ContentPane1().add(
					getchkSplPlts(),
					getchkSplPlts().getName());
				// end defect 9124
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjFrmEmployeeAccessRightsSEC005ContentPane1;
	}

	/**
	 * Return the lblUserNamePrefix property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblUserNamePrefix()
	{
		if (ivjlblUserNamePrefix == null)
		{
			try
			{
				ivjlblUserNamePrefix = new JLabel();
				ivjlblUserNamePrefix.setName("lblUserNamePrefix");
				ivjlblUserNamePrefix.setText(TXT_SAMPLE_PREFIX);
				// defect 8724
				//ivjlblUserNamePrefix.setBounds(112, 9, 27, 20);
				ivjlblUserNamePrefix.setBounds(83, 9, 27, 20);
				// end defect 8724
				// user code begin {1}
				String lsADOfcId =
					UtilityMethods.addPadding(
						String.valueOf(
							SystemProperty.getOfficeIssuanceNo()),
						LocalOptionConstant.LENGTH_PREFIX_NUMBERS,
						CommonConstant.STR_ZERO);
				lsADOfcId = lsADOfcId + CommonConstant.STR_DASH;
				ivjlblUserNamePrefix.setText(lsADOfcId);
				ivjlblUserNamePrefix.setVisible(true);
				ivjlblUserNamePrefix.setEnabled(true);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjlblUserNamePrefix;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlSelect()
	{
		if (ivjpnlSelect == null)
		{
			try
			{
				ivjpnlSelect = new JPanel();
				ivjpnlSelect.setName("pnlSelect");
				ivjpnlSelect.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						LocalOptionConstant
							.TXT_SELECT_IF_NEEDED_COLON));
				ivjpnlSelect.setLayout(null);
				ivjpnlSelect.setMaximumSize(new Dimension(299, 346));
				ivjpnlSelect.setMinimumSize(new Dimension(299, 346));
				ivjpnlSelect.setBounds(198, 65, 264, 379);
				getpnlSelect().add(
					getbtnRegistrationOnly(),
					getbtnRegistrationOnly().getName());
				getpnlSelect().add(
					getbtnTitleRegistration(),
					getbtnTitleRegistration().getName());
				getpnlSelect().add(
					getbtnStatusChanged(),
					getbtnStatusChanged().getName());
				getpnlSelect().add(
					getbtnInquiry(),
					getbtnInquiry().getName());
				getpnlSelect().add(
					getbtnMiscellaneousRegistration(),
					getbtnMiscellaneousRegistration().getName());
				getpnlSelect().add(
					getbtnMiscellaneous(),
					getbtnMiscellaneous().getName());
				getpnlSelect().add(
					getbtnReports(),
					getbtnReports().getName());
				getpnlSelect().add(
					getbtnLocalOptions(),
					getbtnLocalOptions().getName());
				getpnlSelect().add(
					getbtnAccounting(),
					getbtnAccounting().getName());
				getpnlSelect().add(
					getbtnInventory(),
					getbtnInventory().getName());
				getpnlSelect().add(
					getbtnFunds(),
					getbtnFunds().getName());
				// defect 9124
				getpnlSelect().add(
					getbtnSpecialPlates(),
					getbtnSpecialPlates().getName());
				// end defect 9124
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjpnlSelect;
	}

	/**
	 * Return the stclblAdUserId property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstclblAdUserId()
	{
		if (ivjstclblAdUserId == null)
		{
			try
			{
				ivjstclblAdUserId = new JLabel();
				ivjstclblAdUserId.setName("stclblAdUserId");
				ivjstclblAdUserId.setAlignmentX(
					Component.RIGHT_ALIGNMENT);
				ivjstclblAdUserId.setText(
					LocalOptionConstant.TXT_USER_NAME_COLON);
				// defect 8724
				//ivjstclblAdUserId.setBounds(28, 9, 73, 20);
				ivjstclblAdUserId.setBounds(10, 9, 67, 20);
				// end defect 8724
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstclblAdUserId;
	}

	/**
	 * Return the stcLblEmployeeId property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEmployeeId()
	{
		if (ivjstcLblEmployeeId == null)
		{
			try
			{
				ivjstcLblEmployeeId = new JLabel();
				ivjstcLblEmployeeId.setName("stcLblEmployeeId");
				ivjstcLblEmployeeId.setText(
					LocalOptionConstant.TXT_EMPLOYEE_ID_COLON);
				ivjstcLblEmployeeId.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblEmployeeId.setMaximumSize(
					new Dimension(71, 14));
				ivjstcLblEmployeeId.setMinimumSize(
					new Dimension(71, 14));
				// defect 8724
				//ivjstcLblEmployeeId.setBounds(249, 9, 78, 20);
				ivjstcLblEmployeeId.setBounds(252, 9, 75, 20);
				// end defect 8724
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblEmployeeId;
	}

	/**
	 * Return the stcLblFirstName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblFirstName()
	{
		if (ivjstcLblFirstName == null)
		{
			try
			{
				ivjstcLblFirstName = new JLabel();
				ivjstcLblFirstName.setName("stcLblFirstName");
				ivjstcLblFirstName.setText(
					LocalOptionConstant.TXT_FIRST_NAME_COLON);
				ivjstcLblFirstName.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblFirstName.setMaximumSize(
					new Dimension(64, 14));
				ivjstcLblFirstName.setMinimumSize(
					new Dimension(64, 14));
				// defect 8724
				//ivjstcLblFirstName.setBounds(249, 38, 78, 14);
				ivjstcLblFirstName.setBounds(252, 38, 75, 14);
				// end defect 8724
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblFirstName;
	}

	/**
	 * Return the stcLblLastName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblLastName()
	{
		if (ivjstcLblLastName == null)
		{
			try
			{
				ivjstcLblLastName = new JLabel();
				ivjstcLblLastName.setName("stcLblLastName");
				ivjstcLblLastName.setText(
					LocalOptionConstant.TXT_LAST_NAME_COLON);
				ivjstcLblLastName.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblLastName.setMaximumSize(new Dimension(64, 14));
				ivjstcLblLastName.setMinimumSize(new Dimension(64, 14));
				// defect 8724
				//ivjstcLblLastName.setBounds(27, 36, 71, 14);
				ivjstcLblLastName.setBounds(10, 36, 67, 20);
				// end defect 8724
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblLastName;
	}

	/**
	 * Return the stcLblMiddleInit property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblMiddleInit()
	{
		if (ivjstcLblMiddleInit == null)
		{
			try
			{
				ivjstcLblMiddleInit = new JLabel();
				ivjstcLblMiddleInit.setName("stcLblMiddleInit");
				ivjstcLblMiddleInit.setText(
					LocalOptionConstant.TXT_MIDDLE_INIT_COLON);
				ivjstcLblMiddleInit.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblMiddleInit.setMaximumSize(
					new Dimension(60, 14));
				ivjstcLblMiddleInit.setMinimumSize(
					new Dimension(60, 14));
				// defect 8724
				//ivjstcLblMiddleInit.setBounds(479, 39, 71, 14);
				ivjstcLblMiddleInit.setBounds(508, 39, 66, 14);
				// end defect 8724
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblMiddleInit;
	}

	/**
	 * Return the txtAreaSession property value.
	 * 
	 * @return JTextArea
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JTextArea gettxtAreaSession()
	{
		if (ivjtxtAreaSession == null)
		{
			try
			{
				ivjtxtAreaSession = new JTextArea();
				ivjtxtAreaSession.setName("txtAreaSession");
				ivjtxtAreaSession.setText(
					LocalOptionConstant
						.TXT_EQUAL_CHECKED_DURING_SESSION);
				ivjtxtAreaSession.setBackground(java.awt.Color.white);
				ivjtxtAreaSession.setCaretColor(new Color(0, 0, 0));
				ivjtxtAreaSession.setDisabledTextColor(
					new Color(207, 0, 0));
				ivjtxtAreaSession.setBounds(42, 65, 108, 42);
				ivjtxtAreaSession.setEnabled(false);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtAreaSession;
	}

	/**
	 * Return the txtEmployeeId property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtEmployeeId()
	{
		if (ivjtxtEmployeeId == null)
		{
			try
			{
				ivjtxtEmployeeId = new RTSInputField();
				ivjtxtEmployeeId.setName("txtEmployeeId");
				ivjtxtEmployeeId.setHighlighter(new BasicHighlighter());
				ivjtxtEmployeeId.setText(
					CommonConstant.STR_SPACE_EMPTY);
				// defect 8938
				// defect 8674 
				//ivjtxtEmployeeId.setInput(
				//	RTSInputField.ALPHANUMERIC_NOSPACE);
				//ivjtxtEmployeeId.setInput(
				//	RTSInputField.ALPHANUMERIC_ONLY);
				// end defect 8674
				ivjtxtEmployeeId.setInput(RTSInputField.NAME_FIELD);
				// end defect 8938
				ivjtxtEmployeeId.setBounds(335, 9, 72, 20);
				//ivjtxtEmployeeId.setNextFocusableComponent(
				//	gettxtLastName());
				ivjtxtEmployeeId.setMaxLength(
					LocalOptionConstant.EMP_ID_MAX_LENGTH);
				// user code begin {1}
				ivjtxtEmployeeId.addFocusListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtEmployeeId;
	}

	/**
	 * Return the txtFirstName property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtFirstName()
	{
		if (ivjtxtFirstName == null)
		{
			try
			{
				ivjtxtFirstName = new RTSInputField();
				ivjtxtFirstName.setName("txtFirstName");
				// defect 8938
				//ivjtxtFirstName.setInput(
				//	RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtFirstName.setInput(RTSInputField.NAME_FIELD);
				// end defect 8938
				ivjtxtFirstName.setHighlighter(new BasicHighlighter());
				ivjtxtFirstName.setText(CommonConstant.STR_SPACE_EMPTY);
				// defect 8724
				//ivjtxtFirstName.setBounds(335, 35, 118, 20);
				ivjtxtFirstName.setBounds(335, 35, 170, 20);
				// end defect 8724
				ivjtxtFirstName.setMaxLength(
					LocalOptionConstant.LENGTH_NAME_EMP_SEC);
				// user code begin {1}
				ivjtxtFirstName.addFocusListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtFirstName;
	}
	/**
	 * Return the txtLastName property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLastName()
	{
		if (ivjtxtLastName == null)
		{
			try
			{
				ivjtxtLastName = new RTSInputField();
				ivjtxtLastName.setName("txtLastName");
				// defect 8938
				//ivjtxtLastName.setInput(
				//	RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtLastName.setInput(RTSInputField.NAME_FIELD);
				// end defect 8938
				ivjtxtLastName.setHighlighter(new BasicHighlighter());
				ivjtxtLastName.setText(CommonConstant.STR_SPACE_EMPTY);
				// defect 8724
				//ivjtxtLastName.setBounds(112, 36, 115, 20);
				ivjtxtLastName.setBounds(83, 36, 170, 20);
				// end defect 8724
				ivjtxtLastName.setMaxLength(
					LocalOptionConstant.LENGTH_NAME_EMP_SEC);
				// user code begin {1}
				ivjtxtLastName.addFocusListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtLastName;
	}

	/**
	 * Return the txtMiddleInit property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtMiddleInit()
	{
		if (ivjtxtMiddleInit == null)
		{
			try
			{
				ivjtxtMiddleInit = new RTSInputField();
				ivjtxtMiddleInit.setName("txtMiddleInit");
				ivjtxtMiddleInit.setInput(RTSInputField.DEFAULT);
				ivjtxtMiddleInit.setHighlighter(new BasicHighlighter());
				ivjtxtMiddleInit.setText(
					CommonConstant.STR_SPACE_EMPTY);
				// defect 8724
				//ivjtxtMiddleInit.setBounds(564, 36, 19, 20);
				ivjtxtMiddleInit.setBounds(578, 36, 19, 20);
				// end defect 8724
				ivjtxtMiddleInit.setMaxLength(
					LocalOptionConstant.LENGTH_MIDDLE_INIT);
				// user code begin {1}
				ivjtxtMiddleInit.addFocusListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtMiddleInit;
	}

	/**
	 * Return the txtRstPass property value.
	 * 
	 * @return JTextArea
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JTextArea gettxtRstPass()
	{
		if (ivjtxtRstPass == null)
		{
			try
			{
				ivjtxtRstPass = new JTextArea();
				ivjtxtRstPass.setName("txtRstPass");
				ivjtxtRstPass.setText(
					LocalOptionConstant.MSG_TO_COMPLETE_PRESS_REVISE);
				ivjtxtRstPass.setBackground(new Color(204, 204, 204));
				ivjtxtRstPass.setVisible(false);
				ivjtxtRstPass.setCaretColor(new Color(102, 102, 153));
				ivjtxtRstPass.setBounds(466, 120, 142, 71);
				ivjtxtRstPass.setEditable(false);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtRstPass;
	}

	/**
	 * Return the txtUserNameBaseId property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtUserNameBaseId()
	{
		if (ivjtxtUserNameBaseId == null)
		{
			try
			{
				ivjtxtUserNameBaseId = new RTSInputField();
				ivjtxtUserNameBaseId.setName("txtUserNameBaseId");
				ivjtxtUserNameBaseId.setText(
					CommonConstant.STR_SPACE_EMPTY);
				// defect 8938
				// defect 8674
				//ivjtxtUserNameBaseId.setInput(
				//	RTSInputField.ALPHANUMERIC_ONLY);
				// end defect 8674
				ivjtxtUserNameBaseId.setInput(RTSInputField.NAME_FIELD);
				// end defect 8938
				// defect 8724
				//ivjtxtUserNameBaseId.setBounds(140, 9, 89, 20);
				ivjtxtUserNameBaseId.setBounds(110, 9, 89, 20);
				// end defect 8724
				ivjtxtUserNameBaseId.setEditable(true);
				ivjtxtUserNameBaseId.setMaxLength(
					LocalOptionConstant
						.USER_NAME_BASE_ID_MAX_LENGTH_PREFIX);
				ivjtxtUserNameBaseId.setEnabled(false);
				// user code begin {1}
				// listen for focus
				ivjtxtUserNameBaseId.addFocusListener(this);
				ivjtxtUserNameBaseId.setEnabled(true);
				ivjtxtUserNameBaseId.setVisible(true);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtUserNameBaseId;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable 
	 */
	private void handleException(java.lang.Throwable aeEx)
	{
		//defect 7891
		///* Uncomment the following lines to print uncaught exceptions 
		// * to stdout */
		//System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		//exception.printStackTrace();
		//Log.write(Log.SQL_EXCP, this, "Frame Exception " + exception);
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRTSEx.displayError(this);
		//end defect 7891
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
			addMouseMotionListener(this);
			// focus manager necessary to make form follow tab tag order
			//FocusManager.setCurrentManager(
			//	new ContainerOrderFocusManager());
			addWindowListener(this);
			// user code end
			setName(ScreenConstant.SEC005_FRAME_NAME);
			setSize(620, 510);
			setTitle(ScreenConstant.SEC005_FRAME_TITLE);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setContentPane(
				getFrmEmployeeAccessRightsSEC005ContentPane1());
		}
		catch (Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		cbIsCancelClicked = false;
		enableButtons(false);
		//defect 7891
		caarBtnArr[0] = getbtnAdd();
		caarBtnArr[1] = getbtnRevise();
		caarBtnArr[2] = getbtnDelete();
		caarBtnArr[3] = getbtnCancel();
		caarBtnArr[4] = getbtnHelp();
		caarBtnArrSec[0] = getbtnRegistrationOnly();
		caarBtnArrSec[1] = getbtnTitleRegistration();
		caarBtnArrSec[2] = getbtnStatusChanged();
		caarBtnArrSec[3] = getbtnInquiry();
		caarBtnArrSec[4] = getbtnMiscellaneousRegistration();
		// defect 9124
		caarBtnArrSec[5] = getbtnSpecialPlates();
		// end defect 9124
		caarBtnArrSec[6] = getbtnMiscellaneous();
		caarBtnArrSec[7] = getbtnReports();
		caarBtnArrSec[8] = getbtnLocalOptions();
		caarBtnArrSec[9] = getbtnAccounting();
		caarBtnArrSec[10] = getbtnInventory();
		caarBtnArrSec[11] = getbtnFunds();
		//end defect 7891
		// user code end
	}

	/**
	 * Check to see if empid is valid.
	 * 
	 * @return boolean
	 */
	private boolean isValidEmpId()
	{
		// set up the return boolean
		boolean lbValidEmp = true;
		String lsEmpId = gettxtEmployeeId().getText().trim();
		String lsUserId = gettxtUserNameBaseId().getText().trim();
		// return true if cancel was clicked
		if (cbIsCancelClicked)
		{
			lbValidEmp = cbIsCancelClicked;
		}
		else if (
			lsEmpId.length() < LocalOptionConstant.EMP_ID_MIN_LENGTH
				&& lsUserId.length()
					< LocalOptionConstant.EMP_ID_MIN_LENGTH)
		{
			lbValidEmp = false;
		}

		return lbValidEmp;
	}

	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		//defect 7891
		if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			cbIsCancelClicked = true;
		}

		if (aaKE.getSource() instanceof JButton)
		{
			JButton laButton = (JButton) aaKE.getSource();

			if (laButton.getParent() == getpnlSelect())
			{
				ciSelectSec = 0;
				// defect 9124
				for (int i = 0; i < 12; i++)
					// end defect 9124
				{
					if (caarBtnArrSec[i].hasFocus())
					{
						ciSelectSec = i;
						break;
					}

				}
				if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
					|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
				{
					boolean lbStop = true;
					do
					{
						ciSelectSec++;
						// defect 9124
						if (ciSelectSec > 11)
							// end defect 9124
						{
							ciSelectSec = 0;
						}
						// defect 9124
						if (ciSelectSec < 12
							&& caarBtnArrSec[ciSelectSec].isEnabled())
							//end defect 9124
						{
							caarBtnArrSec[ciSelectSec].requestFocus();
							lbStop = false;
						}
					}
					while (lbStop);
				}
				else if (
					aaKE.getKeyCode() == KeyEvent.VK_LEFT
						|| aaKE.getKeyCode() == KeyEvent.VK_UP)
				{
					boolean lbStop = true;
					do
					{
						ciSelectSec--;
						if (ciSelectSec < 0)
						{
							// defect 9124
							ciSelectSec = 11;
							// end defect 9124
						}
						if (ciSelectSec >= 0
							&& caarBtnArrSec[ciSelectSec].isEnabled())
						{
							caarBtnArrSec[ciSelectSec].requestFocus();
							lbStop = false;
						}
					}
					while (lbStop);
				}
			}
			else
			{
				ciSelect = 0;
				for (int i = 0; i < 5; i++)
				{
					if (caarBtnArr[i].hasFocus())
					{
						ciSelect = i;
						break;
					}

				}
				if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
					|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
				{
					boolean lbStop = true;
					do
					{
						ciSelect = ciSelect + 1;
						if (ciSelect > 4)
						{
							ciSelect = 0;
						}
						if (ciSelect < 5
							&& caarBtnArr[ciSelect].isEnabled())
						{
							caarBtnArr[ciSelect].requestFocus();
							lbStop = false;
						}
					}
					while (lbStop);
				}
				else if (
					aaKE.getKeyCode() == KeyEvent.VK_LEFT
						|| aaKE.getKeyCode() == KeyEvent.VK_UP)
				{
					boolean lbStop = true;
					do
					{
						ciSelect--;
						if (ciSelect < 0)
						{
							ciSelect = 4;
						}
						if (ciSelect >= 0
							&& caarBtnArr[ciSelect].isEnabled())
						{
							caarBtnArr[ciSelect].requestFocus();
							lbStop = false;
						}
					}
					while (lbStop);
				}
			}
		}
		//end defect 7891
		super.keyPressed(aaKE);
	}

	/**
	 * Act on KeyRelease.
	 * Do not take super action on the two new check boxes
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		// if the source is on the one of these checkboxes
		// just eat the event.
		if (!aaKE.getSource().equals(getchkSearchUserName())
			&& !aaKE.getSource().equals(getchkUsePrefix()))
		{
			super.keyReleased(aaKE);
		}
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
			FrmEmployeeAccessRightsSEC005 laFrmEmployeeAccessRightsSEC005;
			laFrmEmployeeAccessRightsSEC005 =
				new FrmEmployeeAccessRightsSEC005();
			laFrmEmployeeAccessRightsSEC005.setModal(true);
			laFrmEmployeeAccessRightsSEC005
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmEmployeeAccessRightsSEC005.show();
			java.awt.Insets insets =
				laFrmEmployeeAccessRightsSEC005.getInsets();
			laFrmEmployeeAccessRightsSEC005.setSize(
				laFrmEmployeeAccessRightsSEC005.getWidth()
					+ insets.left
					+ insets.right,
				laFrmEmployeeAccessRightsSEC005.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmEmployeeAccessRightsSEC005.setVisibleRTS(true);
		}
		catch (Throwable leEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leEx.printStackTrace(System.out);
		}
	}

	/**
	 * Invoked when a mouse button is pressed on a component and then 
	 * dragged.  Mouse drag events will continue to be delivered to
	 * the component where the first originated until the mouse button 
	 * is released (regardless of whether the mouse position is within 
	 * the bounds of the component).
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseDragged(MouseEvent aaME)
	{
		// empty code block
	}

	/**
	 * Invoked when the mouse button has been moved on a component
	 * (with no buttons no down).
	 * 
	 * @param aaME MouseEvent
	 */
	public void mouseMoved(MouseEvent aaME)
	{
		if (aaME.getSource() == getbtnCancel())
		{
			cbIsCancelClicked = true;
		}
		else if (aaME.getSource() == this)
		{
			cbIsCancelClicked = false;
		}
	}

	/**
	 * This method will parse out the prefix so we can 
	 * handle just the UserNameBaseId.
	 * 
	 * @return String
	 * @param asUserName String
	 */
	private String parseOutPrefix(String asUserName)
	{
		// setup the initial return string
		String lsUserNameBaseId = asUserName;

		// if the length is greater than the prefix length
		// and we start with a prefix, remove it for 
		if (asUserName.trim().length()
			>= LocalOptionConstant.USER_NAME_PREFIX_LENGTH)
		{
			if (JniAdInterface
				.isRtsUserName(
					asUserName,
					SystemProperty.getOfficeIssuanceNo()))
			{
				lsUserNameBaseId =
					asUserName.substring(
						LocalOptionConstant.USER_NAME_PREFIX_LENGTH);
			}
		}

		// trim the UserName back to Employee Max Length
		// use the fields max length since it reflects the real max 
		// length
		if (lsUserNameBaseId.length()
			> gettxtUserNameBaseId().getMaxLength())
		{
			lsUserNameBaseId =
				lsUserNameBaseId.substring(
					0,
					gettxtUserNameBaseId().getMaxLength());
		}
		return lsUserNameBaseId;
	}

	/**
	 * Establish relationship to controller.
	 * 
	 * @param aaController AbstractViewController
	 */
	public void setController(AbstractViewController aaController)
	{
		super.setController(aaController);
	}

	/**
	 * Set the CustServAccs based on the underlying accesses.
	 * 
	 */
	private void setCustomer()
	{
		if (caSecDataUpdtOrig.getStatusChngAccs() == 1)
		{
			caSecDataUpdtOrig.setTtlRegAccs(1);
		}
		//Following menu items are contained in Customer menu
		//If one of these is set set the customer menu to be enabled
		if (caSecDataUpdtOrig.getRegOnlyAccs() == 1
			|| caSecDataUpdtOrig.getTtlRegAccs() == 1
			|| caSecDataUpdtOrig.getInqAccs() == 1
			|| caSecDataUpdtOrig.getMiscRegAccs() == 1)
		{
			caSecDataUpdtOrig.setCustServAccs(1);
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
			if (aaDataObject == null)
			{
				//Comes here if found an Object when the frame is 
				// opened for the first time
				//Disable buttons
				enableButtons(false);
				clearCheckBoxs();
				gettxtEmployeeId().setEnabled(true);
				gettxtEmployeeId().setEditable(true);
				gettxtEmployeeId().requestFocus();
				gettxtEmployeeId().setText(
					CommonConstant.STR_SPACE_EMPTY);

				gettxtUserNameBaseId().setText(
					CommonConstant.STR_SPACE_EMPTY);
				getchkSearchUserName().setSelected(false);
				gettxtUserNameBaseId().setEditable(false);
				gettxtUserNameBaseId().setEnabled(false);
				// defect 9085 
				//boolean lbCounty = UtilityMethods.isCounty();
				boolean lbCounty = SystemProperty.isCounty();
				// end defect 9085
				if (SystemProperty.getProdStatus()
					== SystemProperty.APP_PROD_STATUS)
				{
					// in production mode.
					if (lbCounty)
					{
						// in counties, do not give an option to 
						// disable the prefix
						// prefix is selected.
						getchkUsePrefix().setEnabled(false);
						getchkUsePrefix().setSelected(true);

						gettxtUserNameBaseId().setMaxLength(
							LocalOptionConstant
								.USER_NAME_BASE_ID_MAX_LENGTH_PREFIX);

						getlblUserNamePrefix().setVisible(true);
						getlblUserNamePrefix().setEnabled(true);
					}
					else
					{
						// If not a county, allow prefix to be 
						// selected the default is off 
						getchkUsePrefix().setEnabled(true);
						getchkUsePrefix().setSelected(false);

						gettxtUserNameBaseId().setMaxLength(
							LocalOptionConstant
								.USER_NAME_BASE_ID_MAX_LENGTH_NONPREFIX);

						getlblUserNamePrefix().setVisible(false);
						getlblUserNamePrefix().setEnabled(false);
					}
				}
				else
				{
					// in development mode, allow user to select if
					// prefix will be used
					// default is depends on Office Code
					// County is checked.
					// Vtr is not checked
					getchkUsePrefix().setEnabled(true);
					if (lbCounty)
					{
						getchkUsePrefix().setSelected(true);

						gettxtUserNameBaseId().setMaxLength(
							LocalOptionConstant
								.USER_NAME_BASE_ID_MAX_LENGTH_PREFIX);

						getlblUserNamePrefix().setVisible(true);
						getlblUserNamePrefix().setEnabled(true);
					}
					else
					{
						getchkUsePrefix().setSelected(false);

						gettxtUserNameBaseId().setMaxLength(
							LocalOptionConstant
								.USER_NAME_BASE_ID_MAX_LENGTH_NONPREFIX);

						getlblUserNamePrefix().setVisible(false);
						getlblUserNamePrefix().setEnabled(false);
					}
				}
				getchkSearchUserName().setSelected(false);
				getchkSearchUserName().setEnabled(true);

				enableTextFields(true);
				gettxtEmployeeId().setEnabled(true);
				gettxtEmployeeId().setEditable(true);

				gettxtFirstName().setText(
					CommonConstant.STR_SPACE_EMPTY);
				gettxtLastName().setText(
					CommonConstant.STR_SPACE_EMPTY);
				gettxtMiddleInit().setText(
					CommonConstant.STR_SPACE_EMPTY);
				gettxtRstPass().setVisible(false);
				caSecData = null;
				caSecDataUpdtOrig = null;
				getchkResetPassword().setEnabled(false);
				cbAddSecurity = false;
			}
			else
			{
				// make sure selection is cleared and disabled when 
				// there is data
				getchkSearchUserName().setEnabled(false);
				getchkSearchUserName().setSelected(false);
				getchkUsePrefix().setEnabled(false);
				// disable the User Name Field so it can not be 
				// entered into
				gettxtUserNameBaseId().setEditable(false);

				gettxtUserNameBaseId().setEnabled(false);

				//Comes here if found an Object when employee Id is 
				// typed
				caSecData = (SecurityClientDataObject) aaDataObject;
				//new SecurityClientDataObject();
				//Make a copy of the incoming securitydata object and 
				// store it
				if (caSecDataUpdtOrig == null)
				{
					// clear everything out when bringing in the new 
					// object
					clearCheckBoxs();
					caSecDataUpdtOrig =
						(SecurityData) UtilityMethods.copy(
							caSecData.getSecData());
				}
				enableButtons(true);
				if (caSecData.getSecData() != null)
				{
					String lsLastNm =
						caSecData.getSecData().getEmpLastName();
					if (lsLastNm != null)
					{
						gettxtLastName().setText(
							caSecData.getSecData().getEmpLastName());
					}
					if (caSecData.getSecData().getEmpFirstName()
						!= null)
					{
						gettxtFirstName().setText(
							caSecData.getSecData().getEmpFirstName());
					}
					if (caSecData.getSecData().getEmpMI() != null)
					{
						gettxtMiddleInit().setText(
							caSecData.getSecData().getEmpMI());
					}
					// copy over empid
					gettxtEmployeeId().setText(
						caSecData.getSecData().getEmpId());
					// copy over UserName if it exists
					if (caSecData.getSecData().getUserName() != null)
					{
						String lsUserNameFromDb =
							caSecData.getSecData().getUserName();
						if (JniAdInterface
							.isRtsUserName(
								lsUserNameFromDb,
								SystemProperty.getOfficeIssuanceNo()))
						{
							// the UserName has a prefix
							getlblUserNamePrefix().setVisible(true);
							getchkUsePrefix().setSelected(true);
							getchkUsePrefix().setEnabled(false);
							if (gettxtUserNameBaseId().getMaxLength()
								!= LocalOptionConstant
									.USER_NAME_BASE_ID_MAX_LENGTH_PREFIX)
							{
								gettxtUserNameBaseId().setMaxLength(
									LocalOptionConstant
										.USER_NAME_BASE_ID_MAX_LENGTH_PREFIX);
							}
						}
						else
						{
							// the UserName does not have a prefix
							getlblUserNamePrefix().setVisible(false);
							getchkUsePrefix().setSelected(false);
							getchkUsePrefix().setEnabled(false);
							if (gettxtUserNameBaseId().getMaxLength()
								!= LocalOptionConstant
									.USER_NAME_BASE_ID_MAX_LENGTH_NONPREFIX)
							{
								gettxtUserNameBaseId().setMaxLength(
									LocalOptionConstant
										.USER_NAME_BASE_ID_MAX_LENGTH_NONPREFIX);
							}
						}

						gettxtUserNameBaseId().setText(
							parseOutPrefix(lsUserNameFromDb));
					}
					else
					{
						getchkUsePrefix().setSelected(true);
						if (SystemProperty.getProdStatus()
							!= SystemProperty.APP_PROD_STATUS)
						{
							getchkUsePrefix().setEnabled(true);
							if (gettxtUserNameBaseId().getMaxLength()
								!= LocalOptionConstant
									.USER_NAME_BASE_ID_MAX_LENGTH_PREFIX)
							{
								gettxtUserNameBaseId().setMaxLength(
									LocalOptionConstant
										.USER_NAME_BASE_ID_MAX_LENGTH_PREFIX);
							}
						}
					}
					if (caSecDataUpdtOrig != null && !cbAddSecurity)
					{
						// Update Mode on User
						getbtnAdd().setEnabled(false);
						// disable editing of text fields
						enableTextFields(false);
						gettxtEmployeeId().setEditable(false);
						gettxtEmployeeId().setEnabled(false);

						// If on windows, check to see if password
						//  reset should be enabled 
						if (JniAdInterface
							.isRtsUserName(
								caSecData.getSecData().getUserName(),
								caSecData
									.getSecData()
									.getOfcIssuanceNo()))
						{
							// on Windows and is an rts user, 
							// enable password reset
							getchkResetPassword().setEnabled(true);
						}
						else
						{
							// on Windows and not an rts user, 
							// disable password reset
							getchkResetPassword().setEnabled(false);
						}

						// disable the delete button if the employee 
						// being worked is the logged on user.
						String lsLoggedInUser =
							getController()
								.getMediator()
								.getAppController()
								.getDesktop()
								.getSecurityData()
								.getUserName()
								.trim();
						if (lsLoggedInUser
							.equalsIgnoreCase(
								caSecData
									.getSecData()
									.getUserName()
									.trim()))
						{
							getbtnDelete().setEnabled(false);
						}

						getbtnRevise().requestFocus();
					}
					else
					{
						getbtnAdd().setEnabled(true);
						getbtnDelete().setEnabled(false);
						getbtnRevise().setEnabled(false);
						getchkResetPassword().setEnabled(false);
						// in add mode allow edit of text fields
						// EmpId is handled below.
						enableTextFields(true);
						gettxtEmployeeId().setEnabled(true);
						gettxtEmployeeId().setEditable(true);
					}

					gettxtUserNameBaseId().setEditable(false);
					gettxtUserNameBaseId().setEnabled(false);
				}
				else
				{
					doInitAddEmp();
					cbAddSecurity = true;
				}
				if (caSecData.isSEC006())
				{
					getchkReg().setText(CommonConstant.STR_SPACE_EMPTY);
					URL laURL =
						getClass().getResource(
							SystemProperty.getImagesDir()
								+ LocalOptionConstant.CHECK_ICON
								+ CommonConstant.STR_PERIOD
								+ SystemProperty.getImageType());
					if (laURL != null)
					{
						ImageIcon laIcon = new ImageIcon(laURL);
						if (laIcon != null)
						{
							getchkReg().setIcon(laIcon);
						}
					}
				}
				if (caSecData.isSEC007())
				{
					getchkTtlReg().setText(
						CommonConstant.STR_SPACE_EMPTY);
					URL laURL =
						getClass().getResource(
							SystemProperty.getImagesDir()
								+ LocalOptionConstant.CHECK_ICON
								+ CommonConstant.STR_PERIOD
								+ SystemProperty.getImageType());
					if (laURL != null)
					{
						ImageIcon laIcon = new ImageIcon(laURL);
						if (laIcon != null)
						{
							getchkTtlReg().setIcon(laIcon);
						}
					}
				}
				if (caSecData.isSEC008())
				{
					getchkStChng().setText(
						CommonConstant.STR_SPACE_EMPTY);
					URL laURL =
						getClass().getResource(
							SystemProperty.getImagesDir()
								+ LocalOptionConstant.CHECK_ICON
								+ CommonConstant.STR_PERIOD
								+ SystemProperty.getImageType());
					if (laURL != null)
					{
						ImageIcon laIcon = new ImageIcon(laURL);
						if (laIcon != null)
						{
							getchkStChng().setIcon(laIcon);
						}
					}
				}
				if (caSecData.isSEC009())
				{
					getchkInq().setText(CommonConstant.STR_SPACE_EMPTY);
					URL laURL =
						getClass().getResource(
							SystemProperty.getImagesDir()
								+ LocalOptionConstant.CHECK_ICON
								+ CommonConstant.STR_PERIOD
								+ SystemProperty.getImageType());
					if (laURL != null)
					{
						ImageIcon laIcon = new ImageIcon(laURL);
						if (laIcon != null)
						{
							getchkInq().setIcon(laIcon);
						}
					}
				}
				if (caSecData.isSEC010())
				{
					getchkMiscReg().setText(
						CommonConstant.STR_SPACE_EMPTY);
					URL laURL =
						getClass().getResource(
							SystemProperty.getImagesDir()
								+ LocalOptionConstant.CHECK_ICON
								+ CommonConstant.STR_PERIOD
								+ SystemProperty.getImageType());
					if (laURL != null)
					{
						ImageIcon laIcon = new ImageIcon(laURL);
						if (laIcon != null)
						{
							getchkMiscReg().setIcon(laIcon);
						}
					}
				}
				if (caSecData.isSEC011())
				{
					getchkMisc().setText(
						CommonConstant.STR_SPACE_EMPTY);
					URL laURL =
						getClass().getResource(
							SystemProperty.getImagesDir()
								+ LocalOptionConstant.CHECK_ICON
								+ CommonConstant.STR_PERIOD
								+ SystemProperty.getImageType());
					if (laURL != null)
					{
						ImageIcon laIcon = new ImageIcon(laURL);
						if (laIcon != null)
						{
							getchkMisc().setIcon(laIcon);
						}
					}
				}
				if (caSecData.isSEC012())
				{
					getchkRpt().setText(CommonConstant.STR_SPACE_EMPTY);
					URL laURL =
						getClass().getResource(
							SystemProperty.getImagesDir()
								+ LocalOptionConstant.CHECK_ICON
								+ CommonConstant.STR_PERIOD
								+ SystemProperty.getImageType());
					if (laURL != null)
					{
						ImageIcon laIcon = new ImageIcon(laURL);
						if (laIcon != null)
						{
							getchkRpt().setIcon(laIcon);
						}
					}
				}
				if (caSecData.isSEC013())
				{
					getchkLclOptn().setText(
						CommonConstant.STR_SPACE_EMPTY);
					URL laURL =
						getClass().getResource(
							SystemProperty.getImagesDir()
								+ LocalOptionConstant.CHECK_ICON
								+ CommonConstant.STR_PERIOD
								+ SystemProperty.getImageType());
					if (laURL != null)
					{
						ImageIcon laIcon = new ImageIcon(laURL);
						if (laIcon != null)
						{
							getchkLclOptn().setIcon(laIcon);
						}
					}
				}
				if (caSecData.isSEC014())
				{
					getchkAcct().setText(
						CommonConstant.STR_SPACE_EMPTY);
					URL laURL =
						getClass().getResource(
							SystemProperty.getImagesDir()
								+ LocalOptionConstant.CHECK_ICON
								+ CommonConstant.STR_PERIOD
								+ SystemProperty.getImageType());
					if (laURL != null)
					{
						ImageIcon laIcon = new ImageIcon(laURL);
						if (laIcon != null)
						{
							getchkAcct().setIcon(laIcon);
						}
					}
				}
				if (caSecData.isSEC015())
				{
					getchkInv().setText(CommonConstant.STR_SPACE_EMPTY);
					URL laURL =
						getClass().getResource(
							SystemProperty.getImagesDir()
								+ LocalOptionConstant.CHECK_ICON
								+ CommonConstant.STR_PERIOD
								+ SystemProperty.getImageType());
					if (laURL != null)
					{
						ImageIcon laIcon = new ImageIcon(laURL);
						if (laIcon != null)
						{
							getchkInv().setIcon(laIcon);
						}
					}
				}
				if (caSecData.isSEC016())
				{
					getchkFunds().setText(
						CommonConstant.STR_SPACE_EMPTY);
					URL laURL =
						getClass().getResource(
							SystemProperty.getImagesDir()
								+ LocalOptionConstant.CHECK_ICON
								+ CommonConstant.STR_PERIOD
								+ SystemProperty.getImageType());
					if (laURL != null)
					{
						ImageIcon laIcon = new ImageIcon(laURL);
						if (laIcon != null)
						{
							getchkFunds().setIcon(laIcon);
						}
					}
				}
				// defect 9124
				if (caSecData.isSEC018())
				{
					getchkSplPlts().setText(
						CommonConstant.STR_SPACE_EMPTY);
					URL laURL =
						getClass().getResource(
							SystemProperty.getImagesDir()
								+ LocalOptionConstant.CHECK_ICON
								+ CommonConstant.STR_PERIOD
								+ SystemProperty.getImageType());
					if (laURL != null)
					{
						ImageIcon laIcon = new ImageIcon(laURL);
						if (laIcon != null)
						{
							getchkSplPlts().setIcon(laIcon);
						}
					}
				}
				// end defect 9124

				// do not disable empid field
				// gettxtEmployeeId().setEnabled(false);

				// disable edit of UserNameBaseId
				gettxtUserNameBaseId().setEditable(false);
				gettxtUserNameBaseId().setEnabled(false);
				// defect 9653 
				// disableBtnsForRegMainOffc();
				// end defect 9653 
			}
		}
		catch (NullPointerException leNPEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, leNPEx);
			leRTSEx.displayError(this);
		}
	}

	/**
	 * Set default buttons for a new Employee.
	 */
	private void setDefaultNewEmpBtns()
	{
		if (caSecDataUpdtOrig != null)
		{
			//Title Registration
			caSecDataUpdtOrig.setTtlRegAccs(1);
			//Status change
			caSecDataUpdtOrig.setStatusChngAccs(1);
			caSecDataUpdtOrig.setMailRtrnAccs(1);
			caSecDataUpdtOrig.setRgstrByAccs(1);
			caSecDataUpdtOrig.setMiscRemksAccs(1);
			//Miscellaneous
			//Note for mis, complete vehicle trans, printDesitnation
			caSecDataUpdtOrig.setReprntRcptAccs(1);
			//Reports, there is not tile package report field in dbase.
			//its always selected if its a county.
			caSecDataUpdtOrig.setRptsAccs(1);
		}
	}

	/**
	 * Set the objects employee fields based on screen fields.
	 * 
	 * @throws RTSExeception
	 */
	private void setEmpTextFieldsToDataObj() throws RTSException
	{
		if (caSecDataUpdtOrig != null)
		{
			// Set the JNI Update boolean when appropriate 
			if (caSecDataUpdtOrig.getEmpId() == null
				|| !caSecDataUpdtOrig.getEmpId().equalsIgnoreCase(
					gettxtEmployeeId().getText()))
			{
				caSecDataUpdtOrig.setEmpId(
					gettxtEmployeeId().getText());
				caSecDataUpdtOrig.setJniUpdate(true);
			}
			if (caSecDataUpdtOrig.getEmpFirstName() == null
				|| !caSecDataUpdtOrig.getEmpFirstName().equalsIgnoreCase(
					gettxtFirstName().getText()))
			{
				caSecDataUpdtOrig.setEmpFirstName(
					gettxtFirstName().getText());
				caSecDataUpdtOrig.setJniUpdate(true);
			}
			if (caSecDataUpdtOrig.getEmpLastName() == null
				|| !caSecDataUpdtOrig.getEmpLastName().equalsIgnoreCase(
					gettxtLastName().getText()))
			{
				caSecDataUpdtOrig.setEmpLastName(
					gettxtLastName().getText());
				caSecDataUpdtOrig.setJniUpdate(true);
			}
			if (caSecDataUpdtOrig.getEmpMI() == null
				|| !caSecDataUpdtOrig.getEmpMI().equalsIgnoreCase(
					gettxtMiddleInit().getText()))
			{
				caSecDataUpdtOrig.setEmpMI(
					gettxtMiddleInit().getText());
				caSecDataUpdtOrig.setJniUpdate(true);
			}
			// setup the potential UserName
			String lsNewUserName = setupUserName();
			if ((caSecDataUpdtOrig.getUserName() != null
				&& !caSecDataUpdtOrig
					.getUserName()
					.trim()
					.equalsIgnoreCase(
					lsNewUserName.trim())) // defect 7058
				|| (gettxtUserNameBaseId().getText().trim() != null
					&& !gettxtUserNameBaseId().getText().trim().equals(
						parseOutPrefix(lsNewUserName.trim()))))
				// defect 7058
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.CTL001,
						LocalOptionConstant.TXT_USER_NAME_CHANGED
							+ lsNewUserName
							+ CommonConstant.SYSTEM_LINE_SEPARATOR
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.TXT_CONTINUE_QUESTION,
						ScreenConstant.CTL001_FRM_TITLE);
				int liRetCode = leRTSEx.displayError(this);
				if (liRetCode == RTSException.YES)
				{
					caSecDataUpdtOrig.setUserName(lsNewUserName);
					gettxtUserNameBaseId().setText(
						parseOutPrefix(lsNewUserName));
					if (caSecData.getSecData() != null)
					{
						caSecData.getSecData().setUserName(
							lsNewUserName);
					}
				}
				else
				{
					RTSException leRTSEx2 =
						new RTSException(
							RTSException.INFORMATION_MESSAGE,
							LocalOptionConstant
								.MSG_USER_NAME_NOT_ACCEPTED,
							LocalOptionConstant
								.CTL001_FRAME_TITLE_USER_NAME_REJECTED);
					throw leRTSEx2;
				}
			}
			else if (caSecDataUpdtOrig.getUserName() == null)
			{
				caSecDataUpdtOrig.setUserName(lsNewUserName);
				gettxtUserNameBaseId().setText(
					parseOutPrefix(lsNewUserName));
				if (caSecData.getSecData() != null)
				{
					caSecData.getSecData().setUserName(lsNewUserName);
				}
			}
		}
		if (caSecData != null)
		{
			if (caSecData.getSecData().getEmpId() == null
				|| !caSecData.getSecData().getEmpId().equalsIgnoreCase(
					gettxtEmployeeId().getText()))
			{
				caSecData.getSecData().setEmpId(
					gettxtEmployeeId().getText());
				caSecData.getSecData().setJniUpdate(true);
			}
			if (caSecData.getSecData().getEmpFirstName() == null
				|| !caSecData
					.getSecData()
					.getEmpFirstName()
					.equalsIgnoreCase(
					gettxtFirstName().getText()))
			{
				caSecData.getSecData().setEmpFirstName(
					gettxtFirstName().getText());
				caSecData.getSecData().setJniUpdate(true);
			}
			if (caSecData.getSecData().getEmpLastName() == null
				|| !caSecData
					.getSecData()
					.getEmpLastName()
					.equalsIgnoreCase(
					gettxtLastName().getText()))
			{
				caSecData.getSecData().setEmpLastName(
					gettxtLastName().getText());
				caSecData.getSecData().setJniUpdate(true);
			}
			if (caSecData.getSecData().getEmpMI() == null
				|| !caSecData.getSecData().getEmpMI().equalsIgnoreCase(
					gettxtMiddleInit().getText()))
			{
				caSecData.getSecData().setEmpMI(
					gettxtMiddleInit().getText());
				caSecData.getSecData().setJniUpdate(true);
			}
			// setup the potential UserName
			String lsNewUserName = setupUserName();
			if (caSecData.getSecData().getUserName() != null
				&& !caSecData
					.getSecData()
					.getUserName()
					.trim()
					.equalsIgnoreCase(
					lsNewUserName.trim()))
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.CTL001,
						LocalOptionConstant.TXT_USER_NAME_CHANGED
							+ lsNewUserName
							+ CommonConstant.SYSTEM_LINE_SEPARATOR
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.TXT_CONTINUE_QUESTION,
						ScreenConstant.CTL001_FRM_TITLE);
				int liRetCode = leRTSEx.displayError(this);
				if (liRetCode == RTSException.YES)
				{
					caSecData.getSecData().setUserName(lsNewUserName);
					gettxtUserNameBaseId().setText(
						parseOutPrefix(lsNewUserName));
				}
				else
				{
					RTSException leRTSEx2 =
						new RTSException(
							RTSException.INFORMATION_MESSAGE,
							LocalOptionConstant
								.MSG_USER_NAME_NOT_ACCEPTED,
							LocalOptionConstant
								.CTL001_FRAME_TITLE_USER_NAME_REJECTED);
					throw leRTSEx2;
				}
			}
			else if (caSecData.getSecData().getUserName() == null)
			{
				caSecData.getSecData().setUserName(lsNewUserName);
				gettxtUserNameBaseId().setText(
					parseOutPrefix(lsNewUserName));
			}
		}
	}

	/**
	 * Populate UserName from security object.
	 * 
	 * @return String
	 */
	private String setupUserName()
	{
		// setup the return string
		String lsReturnString = CommonConstant.STR_SPACE_ONE;
		// defect 7891
		lsReturnString =
			UtilityMethods.addPaddingRight(
				lsReturnString,
				LocalOptionConstant.USER_NAME_BASE_ID_MAX_LENGTH_PREFIX,
				CommonConstant.STR_SPACE_ONE);
		// end defect 7891
		if (caSecDataUpdtOrig != null
			&& caSecDataUpdtOrig.getUserName() != null
			&& !getbtnAdd().isEnabled())
		{
			lsReturnString = caSecDataUpdtOrig.getUserName();
		}
		// Just return the original UserName
		// if we are not adding
		if (getbtnRevise().isEnabled() || getbtnDelete().isEnabled())
		{
			return lsReturnString;
		}
		// setup the potential UserName
		StringBuffer lsNewUserName = new StringBuffer();
		if (getlblUserNamePrefix().isVisible())
		{
			// add the prefix if needed
			lsNewUserName.append(getlblUserNamePrefix().getText());
		}

		String lsLastName = gettxtLastName().getText();
		lsLastName =
			UtilityMethods.addPaddingRight(
				lsLastName,
				LocalOptionConstant.USER_NAME_BASE_ID_MAX_LENGTH_PREFIX
					- 1,
				CommonConstant.STR_SPACE_ONE);
		String lsFirstName = gettxtFirstName().getText();
		lsFirstName =
			UtilityMethods.addPaddingRight(
				lsFirstName,
				1,
				CommonConstant.STR_SPACE_ONE);

		// if the county is not publishing security and
		// the administrator provided the UserNameBaseId
		// use it instead of computing it.
		if (caSecDataUpdtOrig != null
			&& caSecDataUpdtOrig.isUserProvidedUserName())
		{
			lsNewUserName.append(gettxtUserNameBaseId().getText());
		}
		else if (
			caSecData != null
				&& ((gettxtUserNameBaseId().getText().trim().length()
					< 1) // defect 9085 
		// || (caSecData.getWorkStationType()
		//	== LocalOptionConstant.COUNTY
		// end defect 9085 
					|| (SystemProperty.isCounty()
						&& getlblUserNamePrefix().isVisible()
						&& lsLastName.trim().length() > 0
						&& lsFirstName.trim().length() > 0
						&& !lsLastName
							.substring(
								0,
								LocalOptionConstant
									.USER_NAME_BASE_ID_MAX_LENGTH_PREFIX
									- 1)
							.trim()
							.equalsIgnoreCase(
								lsReturnString
									.substring(
										1,
										LocalOptionConstant
											.USER_NAME_BASE_ID_MAX_LENGTH_PREFIX)
									.trim())
						&& !lsFirstName
							.substring(0, 1)
							.trim()
							.equalsIgnoreCase(
							lsReturnString.substring(0, 1).trim()))))
		{
			// make sure caSecData is not null before continuing 
			// if the username has not been computed, compute it now.
			// Otherwise, if this is a county and a name has changed
			// recompute it (development mode requires a check for prefix).
			// Get last and first name from screen and pad the length

			// formulate new UserName from first initial and last name
			if (gettxtLastName().getText().length() > 0
				&& gettxtFirstName().getText().length() > 0)
			{
				lsNewUserName.append(
					gettxtFirstName().getText().substring(0, 1));
				lsNewUserName.append(gettxtLastName().getText().trim());
			}
		}
		else
		{
			// use UserName entered
			lsNewUserName.append(gettxtUserNameBaseId().getText());
		}

		// move the new UserName string to the object 
		if (lsNewUserName.toString().length()
			> LocalOptionConstant.USER_NAME_MAX_LENGTH)
		{
			// shorten to proper length
			lsReturnString =
				lsNewUserName.toString().substring(
					0,
					LocalOptionConstant.USER_NAME_MAX_LENGTH);
		}
		else if (
			lsNewUserName.toString().length()
				< LocalOptionConstant.USER_NAME_MAX_LENGTH)
		{
			// pad the length if it is short
			lsReturnString =
				(String) (UtilityMethods
					.addPaddingRight(
						lsNewUserName.toString(),
						LocalOptionConstant.USER_NAME_MAX_LENGTH,
						CommonConstant.STR_SPACE_ONE));
		}
		else
		{
			// just set the return string 
			lsReturnString = lsNewUserName.toString();
		}
		// return the string
		return lsReturnString;
	}

	/**
	 * Update the Original Object.
	 * 
	 * @return boolean
	 */
	private boolean updtOrigDataObj()
	{
		boolean lbRetVal = false;
		if (caSecData.isSEC006())
		{
			lbRetVal = true;
			caSecDataUpdtOrig.setAddrChngAccs(
				caSecData.getSecData().getAddrChngAccs());
			caSecDataUpdtOrig.setDuplAccs(
				caSecData.getSecData().getDuplAccs());
			caSecDataUpdtOrig.setExchAccs(
				caSecData.getSecData().getExchAccs());
			caSecDataUpdtOrig.setModfyAccs(
				caSecData.getSecData().getModfyAccs());

			caSecDataUpdtOrig.setRegOnlyAccs(
				caSecData.getSecData().getRegOnlyAccs());
			caSecDataUpdtOrig.setRenwlAccs(
				caSecData.getSecData().getRenwlAccs());
			caSecDataUpdtOrig.setReplAccs(
				caSecData.getSecData().getReplAccs());
			caSecDataUpdtOrig.setSubconRenwlAccs(
				caSecData.getSecData().getSubconRenwlAccs());
			caSecDataUpdtOrig.setItrntRenwlAccs(
				caSecData.getSecData().getItrntRenwlAccs());
			caSecDataUpdtOrig.setIssueDrvsEdAccs(
				caSecData.getSecData().getIssueDrvsEdAccs());
			// defect 10717
			caSecDataUpdtOrig.setWebAgntAccs(
				caSecData.getSecData().getWebAgntAccs());
			// end defect 10717

		}
		if (caSecData.isSEC007())
		{
			lbRetVal = true;
			caSecDataUpdtOrig.setTtlRegAccs(
				caSecData.getSecData().getTtlRegAccs());
			caSecDataUpdtOrig.setTtlApplAccs(
				caSecData.getSecData().getTtlApplAccs());
			caSecDataUpdtOrig.setCorrTtlRejAccs(
				caSecData.getSecData().getCorrTtlRejAccs());
			caSecDataUpdtOrig.setDlrTtlAccs(
				caSecData.getSecData().getDlrTtlAccs());
			caSecDataUpdtOrig.setCCOAccs(
				caSecData.getSecData().getCCOAccs());
			caSecDataUpdtOrig.setCOAAccs(
				caSecData.getSecData().getCOAAccs());
			caSecDataUpdtOrig.setSalvAccs(
				caSecData.getSecData().getSalvAccs());
			caSecDataUpdtOrig.setDelTtlInProcsAccs(
				caSecData.getSecData().getDelTtlInProcsAccs());
			caSecDataUpdtOrig.setAdjSalesTaxAccs(
				caSecData.getSecData().getAdjSalesTaxAccs());
			// defect 8900
			caSecDataUpdtOrig.setExmptAuthAccs(
				caSecData.getSecData().getExmptAuthAccs());
			// end defect 8900
			// defect 10153
			caSecDataUpdtOrig.setPrivateLawEnfVehAccs(
				caSecData.getSecData().getPrivateLawEnfVehAccs());
			// end defect 10153
		}
		if (caSecData.isSEC008())
		{
			lbRetVal = true;
			caSecDataUpdtOrig.setBndedTtlCdAccs(
				caSecData.getSecData().getBndedTtlCdAccs());
			caSecDataUpdtOrig.setCancRegAccs(
				caSecData.getSecData().getCancRegAccs());
			caSecDataUpdtOrig.setLegalRestrntNoAccs(
				caSecData.getSecData().getLegalRestrntNoAccs());
			caSecDataUpdtOrig.setMailRtrnAccs(
				caSecData.getSecData().getMailRtrnAccs());
			caSecDataUpdtOrig.setMiscRemksAccs(
				caSecData.getSecData().getMiscRemksAccs());
			caSecDataUpdtOrig.setRgstrByAccs(
				caSecData.getSecData().getRgstrByAccs());
			caSecDataUpdtOrig.setRegRefAmtAccs(
				caSecData.getSecData().getRegRefAmtAccs());
			caSecDataUpdtOrig.setStatusChngAccs(
				caSecData.getSecData().getStatusChngAccs());
			caSecDataUpdtOrig.setStlnSRSAccs(
				caSecData.getSecData().getStlnSRSAccs());
			caSecDataUpdtOrig.setTtlRevkdAccs(
				caSecData.getSecData().getTtlRevkdAccs());
			//defect 11228
			caSecDataUpdtOrig.setExportAccs(
					caSecData.getSecData().getExportAccs());
			//enddefect 11228
			caSecDataUpdtOrig.setTtlSurrAccs(
				caSecData.getSecData().getTtlSurrAccs());
			caSecDataUpdtOrig.setJnkAccs(
				caSecData.getSecData().getJnkAccs());
		}
		if (caSecData.isSEC009())
		{
			lbRetVal = true;
			caSecDataUpdtOrig.setInqAccs(
				caSecData.getSecData().getInqAccs());
			caSecDataUpdtOrig.setPltNoAccs(
				caSecData.getSecData().getPltNoAccs());
		}
		if (caSecData.isSEC010())
		{
			lbRetVal = true;
			caSecDataUpdtOrig.setDsabldPersnAccs(
				caSecData.getSecData().getDsabldPersnAccs());

			// defect 9831 
			caSecDataUpdtOrig.setDsabldPlcrdRptAccs(
				caSecData.getSecData().getDsabldPlcrdRptAccs());
			caSecDataUpdtOrig.setDsabldPlcrdInqAccs(
				caSecData.getSecData().getDsabldPlcrdInqAccs());
			// end defect 9831 
			// defect 11214 
			caSecDataUpdtOrig.setDsabldPlcrdReInstateAccs(
					caSecData.getSecData().getDsabldPlcrdReInstateAccs());
			// end defect 11214 
			caSecDataUpdtOrig.setMiscRegAccs(
				caSecData.getSecData().getMiscRegAccs());
			caSecDataUpdtOrig.setNonResPrmtAccs(
				caSecData.getSecData().getNonResPrmtAccs());
			caSecDataUpdtOrig.setTempAddlWtAccs(
				caSecData.getSecData().getTempAddlWtAccs());
			caSecDataUpdtOrig.setTimedPrmtAccs(
				caSecData.getSecData().getTimedPrmtAccs());
			// defect 10844 
			caSecDataUpdtOrig.setModfyTimedPrmtAccs(
				caSecData.getSecData().getModfyTimedPrmtAccs());
			// end defect 10844 
			caSecDataUpdtOrig.setTowTrkAccs(
				caSecData.getSecData().getTowTrkAccs());
		}
		if (caSecData.isSEC011())
		{
			lbRetVal = true;
			//Rest of the values correspond to reprnt rcpt accs
			caSecDataUpdtOrig.setPrntImmedAccs(
				caSecData.getSecData().getPrntImmedAccs());
			caSecDataUpdtOrig.setReprntRcptAccs(
				caSecData.getSecData().getReprntRcptAccs());
			caSecDataUpdtOrig.setVoidTransAccs(
				caSecData.getSecData().getVoidTransAccs());
		}
		if (caSecData.isSEC012())
		{
			lbRetVal = true;

			caSecDataUpdtOrig.setRptsAccs(
				caSecData.getSecData().getRptsAccs());
			caSecDataUpdtOrig.setReprntRptAccs(
				caSecData.getSecData().getReprntRptAccs());
			// Assign value of ReprntStkrRptAccs 
			caSecDataUpdtOrig.setReprntStkrRptAccs(
				caSecData.getSecData().getReprntStkrRptAccs());
			caSecDataUpdtOrig.setCntyRptsAccs(
				caSecData.getSecData().getCntyRptsAccs());
			// defect 8900
			caSecDataUpdtOrig.setExmptAuditRptAccs(
				caSecData.getSecData().getExmptAuditRptAccs());
			// end defect 8900
			// defect 9960
			caSecDataUpdtOrig.setETtlRptAccs(
				caSecData.getSecData().getETtlRptAccs());
			// end defect 9960
		}
		if (caSecData.isSEC013())
		{
			lbRetVal = true;
			caSecDataUpdtOrig.setAdminAccs(
				caSecData.getSecData().getAdminAccs());
			caSecDataUpdtOrig.setDlrAccs(
				caSecData.getSecData().getDlrAccs());
			caSecDataUpdtOrig.setLienHldrAccs(
				caSecData.getSecData().getLienHldrAccs());

			caSecDataUpdtOrig.setLocalOptionsAccs(
				caSecData.getSecData().getLocalOptionsAccs());
			caSecDataUpdtOrig.setSecrtyAccs(
				caSecData.getSecData().getSecrtyAccs());
			// set JniUpdate if there is a change
			if (caSecDataUpdtOrig.getEmpSecrtyAccs()
				!= caSecData.getSecData().getEmpSecrtyAccs())
			{
				caSecDataUpdtOrig.setJniUpdate(true);
			}
			caSecDataUpdtOrig.setEmpSecrtyAccs(
				caSecData.getSecData().getEmpSecrtyAccs());
			caSecDataUpdtOrig.setEmpSecrtyRptAccs(
				caSecData.getSecData().getEmpSecrtyRptAccs());
			caSecDataUpdtOrig.setSubconAccs(
				caSecData.getSecData().getSubconAccs());
			caSecDataUpdtOrig.setCrdtCardFeeAccs(
				caSecData.getSecData().getCrdtCardFeeAccs());
			caSecDataUpdtOrig.setRSPSUpdtAccs(
				caSecData.getSecData().getRSPSUpdtAccs());
			// defect 9710
			caSecDataUpdtOrig.setDlrRptAccs(
				caSecData.getSecData().getDlrRptAccs());
			caSecDataUpdtOrig.setLienHldrRptAccs(
				caSecData.getSecData().getLienHldrRptAccs());
			caSecDataUpdtOrig.setSubconRptAccs(
				caSecData.getSecData().getSubconRptAccs());
			// end defect 9710

			// defect 10701 
			caSecDataUpdtOrig.setBatchRptMgmtAccs(
				caSecData.getSecData().getBatchRptMgmtAccs());
			// end defect 10701 

		}
		if (caSecData.isSEC014())
		{
			lbRetVal = true;
			caSecDataUpdtOrig.setAcctAccs(
				caSecData.getSecData().getAcctAccs());
			caSecDataUpdtOrig.setFundsAdjAccs(
				caSecData.getSecData().getFundsAdjAccs());
			caSecDataUpdtOrig.setFundsRemitAccs(
				caSecData.getSecData().getFundsRemitAccs());
			caSecDataUpdtOrig.setModfyHotCkAccs(
				caSecData.getSecData().getModfyHotCkAccs());
			caSecDataUpdtOrig.setFundsInqAccs(
				caSecData.getSecData().getFundsInqAccs());
			caSecDataUpdtOrig.setHotCkCrdtAccs(
				caSecData.getSecData().getHotCkCrdtAccs());
			caSecDataUpdtOrig.setHotCkRedemdAccs(
				caSecData.getSecData().getHotCkRedemdAccs());
			caSecDataUpdtOrig.setItmSeizdAccs(
				caSecData.getSecData().getItmSeizdAccs());
			caSecDataUpdtOrig.setRefAccs(
				caSecData.getSecData().getRefAccs());
			caSecDataUpdtOrig.setRegnlColltnAccs(
				caSecData.getSecData().getRegnlColltnAccs());
		}
		if (caSecData.isSEC015())
		{
			lbRetVal = true;
			caSecDataUpdtOrig.setInvAllocAccs(
				caSecData.getSecData().getInvAllocAccs());
			caSecDataUpdtOrig.setInvDelAccs(
				caSecData.getSecData().getInvDelAccs());
			caSecDataUpdtOrig.setInvHldRlseAccs(
				caSecData.getSecData().getInvHldRlseAccs());
			caSecDataUpdtOrig.setInvInqAccs(
				caSecData.getSecData().getInvInqAccs());
			caSecDataUpdtOrig.setInvActionAccs(
				caSecData.getSecData().getInvActionAccs());
			caSecDataUpdtOrig.setInvAccs(
				caSecData.getSecData().getInvAccs());
			caSecDataUpdtOrig.setInvProfileAccs(
				caSecData.getSecData().getInvProfileAccs());
			caSecDataUpdtOrig.setInvAckAccs(
				caSecData.getSecData().getInvAckAccs());
		}
		if (caSecData.isSEC016())
		{
			lbRetVal = true;
			caSecDataUpdtOrig.setCashOperAccs(
				caSecData.getSecData().getCashOperAccs());
			caSecDataUpdtOrig.setFundsBalAccs(
				caSecData.getSecData().getFundsBalAccs());
			caSecDataUpdtOrig.setFundsMgmtAccs(
				caSecData.getSecData().getFundsMgmtAccs());
		}
		// defect 9124
		if (caSecData.isSEC018())
		{
			lbRetVal = true;
			caSecDataUpdtOrig.setSpclPltAccs(
				caSecData.getSecData().getSpclPltAccs());
			caSecDataUpdtOrig.setSpclPltApplAccs(
				caSecData.getSecData().getSpclPltApplAccs());
			caSecDataUpdtOrig.setSpclPltRenwPltAccs(
				caSecData.getSecData().getSpclPltRenwPltAccs());
			caSecDataUpdtOrig.setSpclPltRevisePltAccs(
				caSecData.getSecData().getSpclPltRevisePltAccs());
			caSecDataUpdtOrig.setSpclPltResrvPltAccs(
				caSecData.getSecData().getSpclPltResrvPltAccs());
			caSecDataUpdtOrig.setSpclPltUnAccptblPltAccs(
				caSecData.getSecData().getSpclPltUnAccptblPltAccs());
			caSecDataUpdtOrig.setSpclPltDelPltAccs(
				caSecData.getSecData().getSpclPltDelPltAccs());
			caSecDataUpdtOrig.setSpclPltRptsAccs(
				caSecData.getSecData().getSpclPltRptsAccs());
		}
		// end defect 9124
		if (getchkResetPassword().isSelected())
		{
			lbRetVal = true;
		}
		setCustomer();
		return lbRetVal;
	}
}
