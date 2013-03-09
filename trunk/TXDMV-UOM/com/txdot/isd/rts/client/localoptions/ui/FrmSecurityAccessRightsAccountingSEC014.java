package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.SecurityClientDataObject;
import com.txdot.isd.rts.services.data.SecurityData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 *
 * FrmSecurityAccessRightsAccountingSEC014.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		11/24/2004	Fix Space Bar does not enable all in HQ with
 *							record which does have authorization not 
 *							ususally given to HQ.
 *							modify selectChkButtons()
 *							defect 7100 Ver 5.2.2
 * Min Wang		03/11/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3 
 * Min Wang		04/06/2005	Remove the arrays due to initialization 
 * 							errors.
 * 							delect carrRTSBtn, carrBtnSec
 * 							modify initialize(), keyPressed()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		04/16/2005	remove unused method
 * 							delete setController()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3          
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Min Wang		08/30/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Min Wang		10/28/2005  Uncomment code for arrow key function.
 * 							modify keyPressed()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	01/19/2006	Screen manipulation for Tab Key movement
 * 							through checkboxes. 
 * 							defect 7891 Ver 5.2.3 
 * K Harrell	08/15/2006	Correct misspelling of "Additional" 
 * 							modify ADD_COLLECTION
 * 							defect 8891 Ver 5.2.4
 * K Harrell	04/20/2007	Implemented SystemProperty.isCounty(), 
 * 							 isRegion() 
 * 							modify disableBtnOnOfcId()
 * 							defect 9085 Ver Special Plates    
 * ---------------------------------------------------------------------
 */

/**
 * This class is used for managing security access rights for
 * Accounting. Data on this screen is managed through 
 * SecurityClientDataObject.
 * 
 * @version	Special Plates	04/20/2007	
 * @author Ashish Mahajan
 * <br>Creation Date:		06/28/2001 17:24:11 
 */

public class FrmSecurityAccessRightsAccountingSEC014
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	private JCheckBox ivjchkAccounting = null;
	private JCheckBox ivjchkAdditonalColl = null;
	private JCheckBox ivjchkCountyFundsRemitt = null;
	private JCheckBox ivjchkDdcthotCrdt = null;
	private JCheckBox ivjchkFundsInquiry = null;
	private JCheckBox ivjchkHotCheckCrdt = null;
	private JCheckBox ivjchkHotCheckRedmd = null;
	private JCheckBox ivjchkItemSized = null;
	private JCheckBox ivjchkRefund = null;
	private JLabel ivjlblEmployeeId = null;
	private JLabel ivjstcLblAccounting = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblEmployeeName = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmSecurityAccessRightsAccountingSEC014ContentPane1 =
		null;
	//  @jve:visual-info  decl-index=0 visual-constraint="10,-84"
	private int ciEnableCount = 0;
	//private final static int NUM_CHKBOX = 9;
	private JLabel ivjlblEmployeeFirstName = null;
	private JLabel ivjlblEmployeeLastName = null;
	private JLabel ivjlblEmployeeMName = null;
	private SecurityClientDataObject caSecData = null;
	private boolean cbSelectAll = false;
	private JCheckBox ivjchkRegnlColl = null;
	// defect 7891
	private RTSButton[] carrRTSBtn = new RTSButton[3];
	private JCheckBox[] carrBtnSec = new JCheckBox[10];
	// end defect 7891
	private int ciSelect = 0;
	private int ciSelectSec = 0;
	private static final String ACCOUNTING = "Accounting";
	// defect 8891 
	// Correct misspelling of "Additional"
	private static final String ADD_COLLECTION =
		//	"Additonal Collections";
	"Additional Collections";
	// end defect 8891 	
	private static final String COUNTY_FUNDS_REMIT =
		"County Funds Remittance";
	private static final String DEDUCT_HOT_CHECK =
		"Deduct Hot Check Credit";
	private static final String FUND_INQUIRY = "Funds Inquiry";
	private static final String HOT_CHECK_CREDIT = "Hot Check Credit";
	private static final String HOT_CHECK_REDEEMED =
		"Hot Check Redeemed";
	private static final String ITEM_SEIZED = "Item Seized";
	private static final String REFUND = "Refund";
	private static final String REGIONAL_COLLECT =
		"Regional Collections";
	private static final String JLABEL1 = "JLabel1";
	private static final String JLABEL2 = "JLabel2";
	private static final String ACCOUNT = "Accounting";
	private static final String EMP_ID = "Employee Id:";
	private static final String EMP_NAME = "Employee Name:";
	private static final String SEC014_FRAME_TITLE =
		"Security Access Rights Accounting   SEC014";

	private javax.swing.JPanel jPanel = null;
	/**
	 * FrmSecurityAccessRightsAccountingSEC014 constructor comment.
	 */
	public FrmSecurityAccessRightsAccountingSEC014()
	{
		super();
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsAccountingSEC014 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSecurityAccessRightsAccountingSEC014(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsAccountingSEC014 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsAccountingSEC014(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsAccountingSEC014 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param cbModal boolean
	 */
	public FrmSecurityAccessRightsAccountingSEC014(
		Dialog aaOwner,
		String asTitle,
		boolean cbModal)
	{
		super(aaOwner, asTitle, cbModal);
	}

	/**
	 * FrmSecurityAccessRightsAccountingSEC014 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param cbModal boolean
	 */
	public FrmSecurityAccessRightsAccountingSEC014(
		Dialog aaOwner,
		boolean cbModal)
	{
		super(aaOwner, cbModal);
	}

	/**
	 * FrmSecurityAccessRightsAccountingSEC014 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSecurityAccessRightsAccountingSEC014(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsAccountingSEC014 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsAccountingSEC014(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsAccountingSEC014 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param cbModal boolean
	 */
	public FrmSecurityAccessRightsAccountingSEC014(
		Frame aaOwner,
		String asTitle,
		boolean cbModal)
	{
		super(aaOwner, asTitle, cbModal);
	}

	/**
	 * FrmSecurityAccessRightsAccountingSEC014 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param cbModal boolean
	 */
	public FrmSecurityAccessRightsAccountingSEC014(
		Frame aaOwner,
		boolean cbModal)
	{
		super(aaOwner, cbModal);
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
			return;
		try
		{
			if (aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				caSecData.setSEC014(false);
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
				return;
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				setValuesToDataObj();
				getController().processData(
					AbstractViewController.ENTER,
					caSecData);
				return;
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.SEC014);
				return;
			}
			else if (aaAE.getSource() == getchkAccounting())
			{
				//SelectAll
				if (!getchkAccounting().isSelected())
				{
					cbSelectAll = false;
				}
				else
				{
					cbSelectAll = true;
				}
				//selectChkButtons(getchkAccounting().isSelected());
				selectChkButtons(cbSelectAll);
			}
			else if (aaAE.getSource() == getchkAdditonalColl())
			{
				if (getchkAdditonalColl().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkCountyFundsRemitt())
			{
				if (getchkCountyFundsRemitt().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkFundsInquiry())
			{
				if (getchkFundsInquiry().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkDdcthotCrdt())
			{
				if (getchkDdcthotCrdt().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkHotCheckCrdt())
			{
				if (getchkHotCheckCrdt().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkHotCheckRedmd())
			{
				if (getchkHotCheckRedmd().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkItemSized())
			{
				if (getchkItemSized().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkRefund())
			{
				if (getchkRefund().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkRegnlColl())
			{
				if (getchkRegnlColl().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			//else if(e.getSource() == getchkVTRFundsAck())
			//{
			//	if(getchkVTRFundsAck().isSelected())
			//		iEnableCount++;
			//	else
			//		iEnableCount--;
			//}
			checkCountZero();
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Check for EnableCount field for zero value and set the 
	 * LocalOptions checkbox
	 */
	private void checkCountZero()
	{
		if (ciEnableCount == 0)
		{
			getchkAccounting().setSelected(false);
		}
		else
		{
			getchkAccounting().setSelected(true);
		}
	}

	/**
	 * Disable the various checkbox for County, Region and VTR
	 */
	private void disableBtnOnOfcId()
	{
		// defect 9085
		if (SystemProperty.isCounty())
		{
			getchkRegnlColl().setEnabled(false);
		}
		else if (SystemProperty.isRegion())
		{
			getchkAdditonalColl().setEnabled(false);
		}
		else if (SystemProperty.isHQ())
		{
			getchkCountyFundsRemitt().setEnabled(false);
			getchkRegnlColl().setEnabled(false);
			getchkAdditonalColl().setEnabled(false);

		}
		//		if (caSecData.getWorkStationType()
		//			== LocalOptionConstant.COUNTY)
		//		{
		//			getchkRegnlColl().setEnabled(false);
		//		}
		//		else if (
		//			caSecData.getWorkStationType()
		//				== LocalOptionConstant.REGION)
		//		{
		//			getchkAdditonalColl().setEnabled(false);
		//		}
		//		else if (
		//			caSecData.getWorkStationType() == LocalOptionConstant.VTR)
		//		{
		//			getchkCountyFundsRemitt().setEnabled(false);
		//			getchkRegnlColl().setEnabled(false);
		//			getchkAdditonalColl().setEnabled(false);
		//		}
		// end defect 9085 
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
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(117, 310, 370, 74);
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.getBtnCancel().addActionListener(this);
				ivjButtonPanel1.getBtnEnter().addActionListener(this);
				ivjButtonPanel1.getBtnHelp().addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the chkAccounting property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkAccounting()
	{
		if (ivjchkAccounting == null)
		{
			try
			{
				ivjchkAccounting = new JCheckBox();
				ivjchkAccounting.setBounds(54, 27, 194, 22);
				ivjchkAccounting.setName("chkAccounting");
				ivjchkAccounting.setMnemonic('a');
				ivjchkAccounting.setText(ACCOUNTING);
				ivjchkAccounting.setMaximumSize(new Dimension(89, 22));
				ivjchkAccounting.setActionCommand(ACCOUNTING);
				ivjchkAccounting.setMinimumSize(new Dimension(89, 22));
				// user code begin {1}
				ivjchkAccounting.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkAccounting;
	}
	/**
	 * Return the chkAdditonalColl property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkAdditonalColl()
	{
		if (ivjchkAdditonalColl == null)
		{
			try
			{
				ivjchkAdditonalColl = new JCheckBox();
				ivjchkAdditonalColl.setName("chkAdditonalColl");
				ivjchkAdditonalColl.setMnemonic('c');
				ivjchkAdditonalColl.setText(ADD_COLLECTION);
				ivjchkAdditonalColl.setMaximumSize(
					new Dimension(144, 22));
				ivjchkAdditonalColl.setActionCommand(ADD_COLLECTION);
				ivjchkAdditonalColl.setBounds(328, 245, 165, 22);
				ivjchkAdditonalColl.setMinimumSize(
					new Dimension(144, 22));
				// user code begin {1}
				ivjchkAdditonalColl.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkAdditonalColl;
	}

	/**
	 * Return the chkCountyFundsRemitt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkCountyFundsRemitt()
	{
		if (ivjchkCountyFundsRemitt == null)
		{
			try
			{
				ivjchkCountyFundsRemitt = new JCheckBox();
				ivjchkCountyFundsRemitt.setBounds(54, 64, 194, 22);
				ivjchkCountyFundsRemitt.setName("chkCountyFundsRemitt");
				ivjchkCountyFundsRemitt.setMnemonic('f');
				ivjchkCountyFundsRemitt.setText(COUNTY_FUNDS_REMIT);
				ivjchkCountyFundsRemitt.setMaximumSize(
					new Dimension(169, 22));
				ivjchkCountyFundsRemitt.setActionCommand(
					COUNTY_FUNDS_REMIT);
				ivjchkCountyFundsRemitt.setMinimumSize(
					new Dimension(169, 22));
				// user code begin {1}
				ivjchkCountyFundsRemitt.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkCountyFundsRemitt;
	}
	/**
	 * Return the chkDdcthotCrdt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkDdcthotCrdt()
	{
		if (ivjchkDdcthotCrdt == null)
		{
			try
			{
				ivjchkDdcthotCrdt = new JCheckBox();
				ivjchkDdcthotCrdt.setName("chkDdcthotCrdt");
				ivjchkDdcthotCrdt.setMnemonic('d');
				ivjchkDdcthotCrdt.setText(DEDUCT_HOT_CHECK);
				ivjchkDdcthotCrdt.setMaximumSize(
					new Dimension(163, 22));
				ivjchkDdcthotCrdt.setActionCommand(DEDUCT_HOT_CHECK);
				ivjchkDdcthotCrdt.setBounds(328, 174, 165, 22);
				ivjchkDdcthotCrdt.setMinimumSize(
					new Dimension(163, 22));
				// user code begin {1}
				ivjchkDdcthotCrdt.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkDdcthotCrdt;
	}

	/**
	 * Return the chkFundsInquiry property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkFundsInquiry()
	{
		if (ivjchkFundsInquiry == null)
		{
			try
			{
				ivjchkFundsInquiry = new JCheckBox();
				ivjchkFundsInquiry.setBounds(54, 96, 194, 22);
				ivjchkFundsInquiry.setName("chkFundsInquiry");
				ivjchkFundsInquiry.setMnemonic('n');
				ivjchkFundsInquiry.setText(FUND_INQUIRY);
				ivjchkFundsInquiry.setMaximumSize(
					new Dimension(100, 22));
				ivjchkFundsInquiry.setActionCommand(FUND_INQUIRY);
				ivjchkFundsInquiry.setMinimumSize(
					new Dimension(100, 22));
				// user code begin {1}
				ivjchkFundsInquiry.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkFundsInquiry;
	}

	/**
	 * Return the chkHotCheckCrdt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkHotCheckCrdt()
	{
		if (ivjchkHotCheckCrdt == null)
		{
			try
			{
				ivjchkHotCheckCrdt = new JCheckBox();
				ivjchkHotCheckCrdt.setName("chkHotCheckCrdt");
				ivjchkHotCheckCrdt.setMnemonic('t');
				ivjchkHotCheckCrdt.setText(HOT_CHECK_CREDIT);
				ivjchkHotCheckCrdt.setMaximumSize(
					new Dimension(120, 22));
				ivjchkHotCheckCrdt.setActionCommand(HOT_CHECK_CREDIT);
				ivjchkHotCheckCrdt.setBounds(328, 105, 165, 22);
				ivjchkHotCheckCrdt.setMinimumSize(
					new Dimension(120, 22));
				// user code begin {1}
				ivjchkHotCheckCrdt.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkHotCheckCrdt;
	}

	/**
	 * Return the chkHotCheckRedmd property value.
	 * 
	 * @return JCheckBox
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkHotCheckRedmd()
	{
		if (ivjchkHotCheckRedmd == null)
		{
			try
			{
				ivjchkHotCheckRedmd = new javax.swing.JCheckBox();
				ivjchkHotCheckRedmd.setName("chkHotCheckRedmd");
				ivjchkHotCheckRedmd.setMnemonic('o');
				ivjchkHotCheckRedmd.setText(HOT_CHECK_REDEEMED);
				ivjchkHotCheckRedmd.setMaximumSize(
					new Dimension(147, 22));
				ivjchkHotCheckRedmd.setActionCommand(
					HOT_CHECK_REDEEMED);
				ivjchkHotCheckRedmd.setBounds(328, 142, 165, 22);
				ivjchkHotCheckRedmd.setMinimumSize(
					new Dimension(147, 22));
				// user code begin {1}
				ivjchkHotCheckRedmd.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkHotCheckRedmd;
	}

	/**
	 * Return the chkItemSized property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkItemSized()
	{
		if (ivjchkItemSized == null)
		{
			try
			{
				ivjchkItemSized = new JCheckBox();
				ivjchkItemSized.setName("chkItemSized");
				ivjchkItemSized.setMnemonic('z');
				ivjchkItemSized.setText(ITEM_SEIZED);
				ivjchkItemSized.setMaximumSize(new Dimension(84, 22));
				ivjchkItemSized.setActionCommand(ITEM_SEIZED);
				ivjchkItemSized.setBounds(328, 208, 165, 22);
				ivjchkItemSized.setMinimumSize(new Dimension(84, 22));
				// user code begin {1}
				ivjchkItemSized.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkItemSized;
	}

	/**
	 * Return the chkRefund property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkRefund()
	{
		if (ivjchkRefund == null)
		{
			try
			{
				ivjchkRefund = new JCheckBox();
				ivjchkRefund.setBounds(54, 130, 194, 22);
				ivjchkRefund.setName("chkRefund");
				ivjchkRefund.setMnemonic('r');
				ivjchkRefund.setText(REFUND);
				ivjchkRefund.setMaximumSize(new Dimension(65, 22));
				ivjchkRefund.setActionCommand(REFUND);
				ivjchkRefund.setMinimumSize(new Dimension(65, 22));
				// user code begin {1}
				ivjchkRefund.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkRefund;
	}

	/**
	 * Return the chkRegnlColl property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkRegnlColl()
	{
		if (ivjchkRegnlColl == null)
		{
			try
			{
				ivjchkRegnlColl = new JCheckBox();
				ivjchkRegnlColl.setBounds(54, 167, 177, 22);
				ivjchkRegnlColl.setName("chkRegnlColl");
				ivjchkRegnlColl.setMnemonic('g');
				ivjchkRegnlColl.setText(REGIONAL_COLLECT);
				ivjchkRegnlColl.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkRegnlColl;
	}

	/**
	 * Return the FrmSecurityAccessRightsAccountingSEC014ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
	{
		if (ivjFrmSecurityAccessRightsAccountingSEC014ContentPane1
			== null)
		{
			try
			{
				ivjFrmSecurityAccessRightsAccountingSEC014ContentPane1 =
					new JPanel();
				ivjFrmSecurityAccessRightsAccountingSEC014ContentPane1
					.setName(
					"FrmSecurityAccessRightsAccountingSEC014ContentPane1");
				ivjFrmSecurityAccessRightsAccountingSEC014ContentPane1
					.setLayout(
					null);
				ivjFrmSecurityAccessRightsAccountingSEC014ContentPane1
					.setMaximumSize(
					new Dimension(503, 322));
				ivjFrmSecurityAccessRightsAccountingSEC014ContentPane1
					.setMinimumSize(
					new Dimension(503, 322));
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getstcLblEmployeeId(),
					getstcLblEmployeeId().getName());
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getlblEmployeeLastName(),
					getlblEmployeeLastName().getName());
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getstcLblEmployeeName(),
					getstcLblEmployeeName().getName());
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getlblEmployeeId(),
					getlblEmployeeId().getName());
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getchkHotCheckCrdt(),
					getchkHotCheckCrdt().getName());
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getchkHotCheckRedmd(),
					getchkHotCheckRedmd().getName());
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getchkDdcthotCrdt(),
					getchkDdcthotCrdt().getName());
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getchkItemSized(),
					getchkItemSized().getName());
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getchkAdditonalColl(),
					getchkAdditonalColl().getName());
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getstcLblAccounting(),
					getstcLblAccounting().getName());
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getlblEmployeeFirstName(),
					getlblEmployeeFirstName().getName());
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1()
					.add(
					getlblEmployeeMName(),
					getlblEmployeeMName().getName());
				// user code begin {1}
				ivjFrmSecurityAccessRightsAccountingSEC014ContentPane1
					.add(
					getJPanel(),
					null);
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjFrmSecurityAccessRightsAccountingSEC014ContentPane1;
	}

	/**
	 * Get the int equivalent for the boolean parameter
	 * Returns 1 if parameter is true or returns 0.
	 * 
	 * @return int
	 * @param abVal boolean
	 */
	private int getIntValue(boolean abVal)
	{
		int liReturn = 0;
		if (abVal == true)
		{
			liReturn = 1;
		}
		return liReturn;
	}

	/**
	 * Return the lblEmployeeFirstName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblEmployeeFirstName()
	{
		if (ivjlblEmployeeFirstName == null)
		{
			try
			{
				ivjlblEmployeeFirstName = new JLabel();
				ivjlblEmployeeFirstName.setName("lblEmployeeFirstName");
				ivjlblEmployeeFirstName.setText(JLABEL1);
				ivjlblEmployeeFirstName.setBounds(328, 30, 203, 14);
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
		return ivjlblEmployeeFirstName;
	}

	/**
	 * Return the lblEmployeeId property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblEmployeeId()
	{
		if (ivjlblEmployeeId == null)
		{
			try
			{
				ivjlblEmployeeId = new JLabel();
				ivjlblEmployeeId.setName("lblEmployeeId");
				ivjlblEmployeeId.setText(JLABEL1);
				ivjlblEmployeeId.setMaximumSize(new Dimension(45, 14));
				ivjlblEmployeeId.setMinimumSize(new Dimension(45, 14));
				ivjlblEmployeeId.setBounds(134, 9, 68, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJjEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJjEx);
			}
		}
		return ivjlblEmployeeId;
	}

	/**
	 * Return the lblEmployeeName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblEmployeeLastName()
	{
		if (ivjlblEmployeeLastName == null)
		{
			try
			{
				ivjlblEmployeeLastName = new JLabel();
				ivjlblEmployeeLastName.setName("lblEmployeeLastName");
				ivjlblEmployeeLastName.setText(JLABEL1);
				ivjlblEmployeeLastName.setMaximumSize(
					new Dimension(45, 14));
				ivjlblEmployeeLastName.setMinimumSize(
					new Dimension(45, 14));
				ivjlblEmployeeLastName.setBounds(134, 30, 190, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblEmployeeLastName;
	}

	/**
	 * Return the lblEmployeeMName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblEmployeeMName()
	{
		if (ivjlblEmployeeMName == null)
		{
			try
			{
				ivjlblEmployeeMName = new javax.swing.JLabel();
				ivjlblEmployeeMName.setName("lblEmployeeMName");
				ivjlblEmployeeMName.setText(JLABEL2);
				ivjlblEmployeeMName.setBounds(536, 30, 47, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblEmployeeMName;
	}

	/**
	 * Return the stcLblAccounting property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblAccounting()
	{
		if (ivjstcLblAccounting == null)
		{
			try
			{
				ivjstcLblAccounting = new JLabel();
				ivjstcLblAccounting.setName("stcLblAccounting");
				ivjstcLblAccounting.setText(ACCOUNT);
				ivjstcLblAccounting.setMaximumSize(
					new Dimension(64, 14));
				ivjstcLblAccounting.setMinimumSize(
					new Dimension(64, 14));
				ivjstcLblAccounting.setBounds(265, 64, 83, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblAccounting;
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
				ivjstcLblEmployeeId.setText(EMP_ID);
				ivjstcLblEmployeeId.setMaximumSize(
					new Dimension(71, 14));
				ivjstcLblEmployeeId.setMinimumSize(
					new Dimension(71, 14));
				ivjstcLblEmployeeId.setBounds(13, 9, 83, 18);
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
	 * Return the stcLblEmployeeName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEmployeeName()
	{
		if (ivjstcLblEmployeeName == null)
		{
			try
			{
				ivjstcLblEmployeeName = new JLabel();
				ivjstcLblEmployeeName.setName("stcLblEmployeeName");
				ivjstcLblEmployeeName.setText(EMP_NAME);
				ivjstcLblEmployeeName.setMaximumSize(
					new Dimension(94, 14));
				ivjstcLblEmployeeName.setMinimumSize(
					new Dimension(94, 14));
				ivjstcLblEmployeeName.setBounds(13, 30, 113, 14);
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
		return ivjstcLblEmployeeName;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		//defect 7891
		//System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		//aeEx.printStackTrace(System.out);
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
			// user code end
			setName("FrmSecurityAccessRightsAccountingSEC014");
			setSize(600, 400);
			setTitle(SEC014_FRAME_TITLE);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setContentPane(
				getFrmSecurityAccessRightsAccountingSEC014ContentPane1());
		}
		catch (Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		// defect 7891 
		carrRTSBtn[0] = getButtonPanel1().getBtnEnter();
		carrRTSBtn[1] = getButtonPanel1().getBtnCancel();
		carrRTSBtn[2] = getButtonPanel1().getBtnHelp();
		carrBtnSec[0] = getchkAccounting();
		carrBtnSec[1] = getchkCountyFundsRemitt();
		carrBtnSec[2] = getchkFundsInquiry();
		carrBtnSec[3] = getchkRefund();
		carrBtnSec[4] = getchkRegnlColl();
		carrBtnSec[5] = getchkHotCheckCrdt();
		carrBtnSec[6] = getchkHotCheckRedmd();
		carrBtnSec[7] = getchkDdcthotCrdt();
		carrBtnSec[8] = getchkItemSized();
		carrBtnSec[9] = getchkAdditonalColl();
		// end defect 7891
		// user code end
	}

	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(java.awt.event.KeyEvent aaKE)
	{
		// defect 7891
		if (aaKE.getSource() instanceof JCheckBox)
		{
			ciSelectSec = 0;
			for (int i = 0; i < 10; i++)
			{
				if (carrBtnSec[i].hasFocus())
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
					if (ciSelectSec > 9)
					{
						ciSelectSec = 0;
					}
					if (ciSelectSec < 10
						&& carrBtnSec[ciSelectSec].isEnabled())
					{
						carrBtnSec[ciSelectSec].requestFocus();
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
						ciSelectSec = 9;
					}
					if (ciSelectSec >= 0
						&& carrBtnSec[ciSelectSec].isEnabled())
					{
						carrBtnSec[ciSelectSec].requestFocus();
						lbStop = false;
					}
				}
				while (lbStop);
			}
		}
		else if (aaKE.getSource() instanceof JButton)
		{
			ciSelect = 0;
			for (int i = 0; i < 3; i++)
			{
				if (carrRTSBtn[i].hasFocus())
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
					ciSelect++;
					if (ciSelect > 2)
					{
						ciSelect = 0;
					}
					if (ciSelect < 3
						&& carrRTSBtn[ciSelect].isEnabled())
					{
						carrRTSBtn[ciSelect].requestFocus();
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
						ciSelect = 2;
					}
					if (ciSelect >= 0
						&& carrRTSBtn[ciSelect].isEnabled())
					{
						carrRTSBtn[ciSelect].requestFocus();
						lbStop = false;
					}
				}
				while (lbStop);
			}
		}
		// end defect 7891
		super.keyPressed(aaKE);
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
			FrmSecurityAccessRightsAccountingSEC014 laFrmSecurityAccessRightsAccountingSEC014;
			laFrmSecurityAccessRightsAccountingSEC014 =
				new FrmSecurityAccessRightsAccountingSEC014();
			laFrmSecurityAccessRightsAccountingSEC014.setModal(true);
			laFrmSecurityAccessRightsAccountingSEC014
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSecurityAccessRightsAccountingSEC014.show();
			Insets insets =
				laFrmSecurityAccessRightsAccountingSEC014.getInsets();
			laFrmSecurityAccessRightsAccountingSEC014.setSize(
				laFrmSecurityAccessRightsAccountingSEC014.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSecurityAccessRightsAccountingSEC014.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSecurityAccessRightsAccountingSEC014.setVisibleRTS(
				true);
		}
		catch (Throwable leIVJex)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			leIVJex.printStackTrace(System.out);
		}
	}

	/**
	 * Sets the various check boxes depending on whether 
	 * they are enabled and updates iEnableCount
	 *
	 * @param abVal boolean
	 */
	private void selectChkButtons(boolean abVal)
	{
		if (getchkAdditonalColl().isEnabled())
		{
			if (abVal && !getchkAdditonalColl().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkAdditonalColl().isSelected())
			{
				ciEnableCount--;
			}
			getchkAdditonalColl().setSelected(abVal);
		}
		//defect 7100
		//only happen on developer setup
		else
		{
			if (getchkAdditonalColl().isSelected())
			{
				ciEnableCount--;
				getchkAdditonalColl().setSelected(false);

			}
		}
		//end defect 7100
		if (getchkCountyFundsRemitt().isEnabled())
		{
			if (abVal && !getchkCountyFundsRemitt().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkCountyFundsRemitt().isSelected())
			{
				ciEnableCount--;
			}
			getchkCountyFundsRemitt().setSelected(abVal);
		}
		//defect 7100
		//only happen on developer setup
		else
		{
			if (getchkCountyFundsRemitt().isSelected())
			{
				ciEnableCount--;
				getchkCountyFundsRemitt().setSelected(false);

			}
		}
		//end defect 7100
		if (getchkDdcthotCrdt().isEnabled())
		{
			if (abVal && !getchkDdcthotCrdt().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkDdcthotCrdt().isSelected())
			{
				ciEnableCount--;
			}
			getchkDdcthotCrdt().setSelected(abVal);
		}
		if (getchkFundsInquiry().isEnabled())
		{
			if (abVal && !getchkFundsInquiry().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkFundsInquiry().isSelected())
			{
				ciEnableCount--;
			}
			getchkFundsInquiry().setSelected(abVal);
		}
		if (getchkHotCheckCrdt().isEnabled())
		{
			if (abVal && !getchkHotCheckCrdt().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkHotCheckCrdt().isSelected())
			{
				ciEnableCount--;
			}
			getchkHotCheckCrdt().setSelected(abVal);
		}
		if (getchkHotCheckRedmd().isEnabled())
		{
			if (abVal && !getchkHotCheckRedmd().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkHotCheckRedmd().isSelected())
			{
				ciEnableCount--;
			}
			getchkHotCheckRedmd().setSelected(abVal);
		}
		if (getchkItemSized().isEnabled())
		{
			if (abVal && !getchkItemSized().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkItemSized().isSelected())
			{
				ciEnableCount--;
			}
			getchkItemSized().setSelected(abVal);
		}
		if (getchkRefund().isEnabled())
		{
			if (abVal && !getchkRefund().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkRefund().isSelected())
			{
				ciEnableCount--;
			}
			getchkRefund().setSelected(abVal);
		}
		if (getchkRegnlColl().isEnabled())
		{
			if (abVal && !getchkRegnlColl().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkRegnlColl().isSelected())
			{
				ciEnableCount--;
			}
			getchkRegnlColl().setSelected(abVal);
		}
		//defect 7100
		//only happen on developer setup
		else
		{
			if (getchkRegnlColl().isSelected())
			{
				ciEnableCount--;
				getchkRegnlColl().setSelected(false);

			}
		}
		//end defect 7100
		//if(getchkVTRFundsAck().isEnabled())
		//{
		//	if(bVal && !getchkVTRFundsAck().isSelected())
		//		iEnableCount++;
		//	else if(!bVal  && getchkVTRFundsAck().isSelected())
		//		iEnableCount--;
		//	getchkVTRFundsAck().setSelected(bVal);
		//}
		//if(bVal)
		//	iEnableCount = NUM_CHKBOX;
		//else
		//	iEnableCount = 0;
	}
	// defect 7891
	///**
	// * Sets the controller for the frame
	// *  
	// * @param controller controller.Controller
	// */
	//public void setController(AbstractViewController aaController)
	//{
	//	super.setController(aaController);
	//}
	// end defect 7891

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information to 
	 * the view
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject != null)
		{
			caSecData = (SecurityClientDataObject) aaDataObject;
			SecurityData laSecData =
				(SecurityData) caSecData.getSecData();
			getlblEmployeeId().setText(laSecData.getEmpId());
			getlblEmployeeLastName().setText(
				laSecData.getEmpLastName());
			getlblEmployeeFirstName().setText(
				laSecData.getEmpFirstName());
			getlblEmployeeMName().setText(laSecData.getEmpMI());
			//Enable disable check boxes
			disableBtnOnOfcId();
			//Not sure of this
			if (laSecData.getAcctAccs() == 1)
				getchkAccounting().setSelected(true);
			if (laSecData.getFundsRemitAccs() == 1)
			{
				getchkCountyFundsRemitt().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getFundsInqAccs() == 1)
			{
				getchkFundsInquiry().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getRefAccs() == 1)
			{
				getchkRefund().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getHotCkCrdtAccs() == 1)
			{
				getchkHotCheckCrdt().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getHotCkRedemdAccs() == 1)
			{
				getchkHotCheckRedmd().setSelected(true);
				ciEnableCount++;
			}
			//dont know deduct hot check credit
			if (laSecData.getFundsAdjAccs() == 1)
			{
				getchkAdditonalColl().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getItmSeizdAccs() == 1)
			{
				getchkItemSized().setSelected(true);
				ciEnableCount++;
			}
			//Additional Collections missing or dont know
			if (laSecData.getModfyHotCkAccs() == 1)
			{
				getchkDdcthotCrdt().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getRegnlColltnAccs() == 1)
			{
				getchkRegnlColl().setSelected(true);
				ciEnableCount++;
			}
		}
		checkCountZero();
	}

	/**
	 * Populate the SecurityClientDataObject object with security 
	 * access rights for Accounting. 
	 */
	private void setValuesToDataObj()
	{
		caSecData.setSEC014(true);
		//Take the new Values fromt the window and set to data object
		caSecData.getSecData().setAcctAccs(
			getIntValue(getchkAccounting().isSelected()));
		caSecData.getSecData().setFundsAdjAccs(
			getIntValue(getchkAdditonalColl().isSelected()));
		caSecData.getSecData().setFundsRemitAccs(
			getIntValue(getchkCountyFundsRemitt().isSelected()));
		caSecData.getSecData().setModfyHotCkAccs(
			getIntValue(getchkDdcthotCrdt().isSelected()));
		caSecData.getSecData().setFundsInqAccs(
			getIntValue(getchkFundsInquiry().isSelected()));
		caSecData.getSecData().setHotCkCrdtAccs(
			getIntValue(getchkHotCheckCrdt().isSelected()));
		caSecData.getSecData().setHotCkRedemdAccs(
			getIntValue(getchkHotCheckRedmd().isSelected()));
		caSecData.getSecData().setItmSeizdAccs(
			getIntValue(getchkItemSized().isSelected()));
		caSecData.getSecData().setRefAccs(
			getIntValue(getchkRefund().isSelected()));
		caSecData.getSecData().setRegnlColltnAccs(
			getIntValue(getchkRegnlColl().isSelected()));
	}
	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new javax.swing.JPanel();
			jPanel.setLayout(null);
			jPanel.add(getchkAccounting(), null);
			jPanel.add(getchkCountyFundsRemitt(), null);
			jPanel.add(getchkFundsInquiry(), null);
			jPanel.add(getchkRefund(), null);
			jPanel.add(getchkRegnlColl(), null);
			jPanel.setBounds(15, 78, 290, 218);
		}
		return jPanel;
	}
}
